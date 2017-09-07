package tech.lihx.demo.core.cache.dao;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.cache.jedis.RedisCommands;
import tech.lihx.demo.core.cache.util.TableNamesUtil;
import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.util.ApplicationUtil;
import tech.lihx.demo.core.support.mq.Handler;
import tech.lihx.demo.core.support.mq.MQService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Intercepts( {
		@Signature( type = Executor.class, method = "update", args = { MappedStatement.class, Object.class } ),
		@Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class } ) } )
public class SqlOptimizeInterceptor implements Interceptor {

	static Logger logger = LoggerFactory.getLogger(SqlOptimizeInterceptor.class);

	static String REDIS_KEY_NAME = "yunxiaoyuan.net:sqlmerge";

	RedisCommands commands;

	MQService mqService;


	@Override
	public Object intercept( Invocation invocation ) throws Throwable {
		if ( EnvironmentDetect.detectEnvironment().isRelease() ) {
			return invocation.proceed();
		} else {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameter = null;
			if ( invocation.getArgs().length > 1 ) {
				parameter = invocation.getArgs()[1];
			}
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			Object returnValue = null;
			long start = System.currentTimeMillis();
			returnValue = invocation.proceed();
			long end = System.currentTimeMillis();

			// 统计处理
			if ( mqService != null ) {
				mqService.putData(new Object[ ] { boundSql.getSql(), (end - start) }, handler);
			} else {
				handler.handle(new Object[ ] { boundSql.getSql(), (end - start) });
			}
			// DruidStatService statService = DruidStatService.getInstance();
			// statService.service(url)
			// DruidStatManagerFacade statManagerFacade =
			// DruidStatManagerFacade.getInstance();
			// Map<String, Object> map =
			// statManagerFacade.getDataSourceStatDataList().get(0);
			// logger.debug(map.get("Identity"));
			// Object ds = statManagerFacade.getDruidDataSourceById((Integer)
			// map.get("Identity"));
			// List<Map<String, Object>> array =
			// statManagerFacade.getSqlStatDataList(ds);
			// logger.debug(JSON.toJSONString(array));
			// List<Map<String, Object>> sortedArray = comparatorOrderBy(array,
			// parameters);
			return returnValue;
		}

	}

	Handler<Object[]> handler = new Handler<Object[]>() {

		@Override
		public void handle( Object[] data ) {
			if ( commands != null ) {
				String sql = (String) data[0];
				Long time = (Long) data[1];
				sql = sql.replaceAll("[\\s]+", " ").toUpperCase();
				if ( sql.contains(" LIMIT ") ) {
					sql = sql.substring(0, sql.lastIndexOf(" LIMIT "));
				}
				String key = TableNamesUtil.getCacheKey(sql);
				String value = commands.hget(REDIS_KEY_NAME, key);
				JSONObject obj = new JSONObject();
				if ( value != null ) {
					obj = JSON.parseObject(value);
					JSONObject param = obj.getJSONObject(sql);
					int times = 0;
					long elapse = 0;
					if ( param == null ) {
						param = new JSONObject();
						obj.put(sql, param);
						times = 1;
						elapse = time;
					} else {
						times = param.getIntValue("times") + 1;
						elapse = param.getLongValue("elapse") + time;
					}
					param.put("times", times);
					param.put("elapse", elapse);
					param.put("average", elapse / times);
				} else {
					JSONObject param = new JSONObject();
					obj.put(sql, param);
					param.put("times", 1);
					param.put("elapse", time);
					param.put("average", time);
				}
				commands.hset(REDIS_KEY_NAME, key, obj.toJSONString());
			}

		}
	};


	@Override
	public Object plugin( Object target ) {
		try {
			if ( commands == null ) {
				commands = ApplicationUtil.getBean(RedisCommands.class);
				mqService = ApplicationUtil.getBean(MQService.class);
			}
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return Plugin.wrap(target, this);
	}


	@Override
	public void setProperties( Properties properties0 ) {

	}
}
