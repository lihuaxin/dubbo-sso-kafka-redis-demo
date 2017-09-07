package tech.lihx.demo.core.cache.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import tech.lihx.demo.core.cache.annotation.CacheDisable;
import tech.lihx.demo.core.cache.annotation.Cacheable;
import tech.lihx.demo.core.cache.interfaces.EntityCache;
import tech.lihx.demo.core.cache.interfaces.ExpireCache;
import tech.lihx.demo.core.cache.support.CacheOperation;
import tech.lihx.demo.core.cache.support.CacheOperationContext;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.response.Response;
import tech.lihx.demo.core.common.response.ResponseHead;
import tech.lihx.demo.core.common.util.ApplicationUtil;

/**
 * @author lihx
 *
 */
public abstract class ServiceCacheAspectSupport implements ApplicationContextAware {

	protected ApplicationContext applicationContext;

	private ExpireCache expireCache;

	private EntityCache entityCache;


	protected Object execute( Invoker invoker, Method method, Object[] args ) {
		final Collection<CacheOperation> cacheOp = getCacheOperations(method);

		Object retVal = null;
		if ( !CollectionUtils.isEmpty(cacheOp) ) {
			Collection<CacheOperationContext> ops = createOperationContext(cacheOp, method, args);
			retVal = inspectTimeCaches(ops, invoker);
			return retVal;

		}
		return invoker.invoke();
	}


	public Collection<CacheOperation> getCacheOperations( Method method ) {
		Annotation[] anntations = method.getAnnotations();
		if ( anntations.length == 0 ) { return null; }
		List<CacheOperation> list = new ArrayList<CacheOperation>(2);
		for ( Annotation annotation : anntations ) {
			Class<?> c = annotation.getClass();
			if ( CacheDisable.class.isAssignableFrom(c) ) {
				CacheOperationContext.setCache(false);
			} else if ( Cacheable.class.isAssignableFrom(c) ) {
				Cacheable annot = (Cacheable) annotation;
				CacheOperation cacheOperation = new CacheOperation();
				cacheOperation.setExpire(annot.expire());
				cacheOperation.setCompress(annot.compress());
				cacheOperation.setEvict(annot.evict());
				cacheOperation.setType(annot.type());
				cacheOperation.setTable(annot.table());
				cacheOperation.setKey(annot.key());
				list.add(cacheOperation);
			}
		}
		return list;
	}


	private Object inspectTimeCaches( Collection<CacheOperationContext> cacheables, Invoker invoker ) {
		Object retVal = null;
		if ( !cacheables.isEmpty() ) {
			for ( CacheOperationContext context : cacheables ) {
				// 如果禁用缓存,不从缓存进行查询操作,但是执行删除操作
				if ( !CacheOperationContext.getCache() ) { return invoker.invoke(); }
				CacheOperation opt = context.getOperation();
				if ( opt.getType() == Cacheable.EXPIRE ) {
					if ( expireCache == null ) {
						expireCache = ApplicationUtil.getBean(ExpireCache.class);
					}
					String key_string = null;
					Object key = context.generateKey();
					key_string = key.toString();
					retVal = expireCache.get(key_string, invoker, context.getExpireTime(), context.isCompress());
				} else if ( opt.getType() == Cacheable.ENTITY ) {
					if ( entityCache == null ) {
						entityCache = ApplicationUtil.getBean(EntityCache.class);
					}
					if ( opt.getTable().length() == 0 ) { throw new RuntimeException("实体缓存不能没有表名称"); }
					if ( opt.getKey().length() == 0 ) { throw new RuntimeException("实体缓存不能没有主键"); }
					Object[] args = context.getArgs();
					if ( args.length == 1 && args[0] instanceof List ) {
						List<?> param = (List<?>) args[0];
						Class<?> returnType = context.getMethod().getReturnType();
						Map<String, Object> paramMap = context.getParamMap();
						List<Object> result = new ArrayList<Object>();

						for ( int i = 0 , size = param.size() ; i < size ; i++ ) {
							paramMap.remove("i");
							paramMap.put("i", i);
							Object id = MVEL.eval(opt.getKey(), paramMap);
							Object temp = entityCache.get(opt.getTable(), id, invoker, opt.isCompress());
							result.add(temp);
						}
						if ( Response.class == returnType ) {
							retVal = new Response<List<Object>>(new ResponseHead(CodeConstants.SUCCESS, "正常返回"), result);
						} else if ( List.class == returnType ) {
							retVal = result;
						}
					} else {
						Object id = MVEL.eval(opt.getKey(), context.getParamMap());
						// TODO 处理返回值,不一样
						retVal = entityCache.get(opt.getTable(), id, invoker, opt.isCompress());
					}
				}
			}
		}

		return retVal;
	}


	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;

	}


	private List<CacheOperationContext> createOperationContext(
			Collection<CacheOperation> cacheOp, Method method, Object[] args ) {
		List<CacheOperationContext> list_time = new ArrayList<CacheOperationContext>(2);
		for ( CacheOperation cacheOperation : cacheOp ) {
			CacheOperationContext opContext = new CacheOperationContext(cacheOperation, method, args);
			list_time.add(opContext);
		}

		return list_time;
	}


	public ExpireCache getExpireCache() {
		return expireCache;
	}


	public void setExpireCache( ExpireCache expireCache ) {
		this.expireCache = expireCache;
	}


	public EntityCache getEntityCache() {
		return entityCache;
	}


	public void setEntityCache( EntityCache entityCache ) {
		this.entityCache = entityCache;
	}

}
