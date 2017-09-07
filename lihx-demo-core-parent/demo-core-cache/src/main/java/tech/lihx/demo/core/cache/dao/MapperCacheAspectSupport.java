package tech.lihx.demo.core.cache.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.mvel2.MVEL;
import org.springframework.util.CollectionUtils;

import tech.lihx.demo.core.cache.annotation.Cacheable;
import tech.lihx.demo.core.cache.interfaces.EntityCache;
import tech.lihx.demo.core.cache.interfaces.ExpireCache;
import tech.lihx.demo.core.cache.support.CacheOperation;
import tech.lihx.demo.core.cache.support.CacheOperationContext;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.common.util.ApplicationUtil;
import tech.lihx.demo.core.mybatis.mybatis.ext.MapperProxyExt;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInvocation;

/**
 * @author lihx
 *
 */
public abstract class MapperCacheAspectSupport {

	protected final Log logger = LogFactory.getLog(getClass());

	private EntityCache entityCache;

	private ExpireCache expireCache;


	protected Object execute( Invoker invoker, Method method, Object[] args ) {
		Annotation[] anntations = method.getAnnotations();
		if ( anntations == null || anntations.length == 0 ) { return invoker.invoke(); }
		boolean[] isEntity = new boolean[ ] { false };
		final Collection<CacheOperation> cacheOp = getCacheOperations(anntations, isEntity);
		Object retVal = null;
		if ( !CollectionUtils.isEmpty(cacheOp) ) {
			Collection<CacheOperationContext> ops = createOperationContext(cacheOp, method, args);
			if ( !isEntity[0] ) {
				retVal = inspectTimeCaches(ops, invoker);
				return retVal;
			} else {
				retVal = inspectEntityCaches(ops, invoker);
				return retVal;
			}
		}
		return invoker.invoke();
	}


	public Collection<CacheOperation> getCacheOperations( Annotation[] anntations, boolean[] isEntity ) {
		List<CacheOperation> list = new ArrayList<CacheOperation>(2);
		for ( Annotation annotation : anntations ) {
			Class<?> c = annotation.getClass();
			if ( Cacheable.class.isAssignableFrom(c) ) {
				Cacheable annot = (Cacheable) annotation;
				CacheOperation cacheOperation = new CacheOperation();
				cacheOperation.setKey(annot.key());
				cacheOperation.setType(annot.type());
				cacheOperation.setTable(annot.table());
				cacheOperation.setCompress(annot.compress());
				cacheOperation.setEvict(annot.evict());
				cacheOperation.setExpire(annot.expire());
				list.add(cacheOperation);
				isEntity[0] = annot.type() == Cacheable.ENTITY;
			}
		}
		return list;
	}


	private Object inspectTimeCaches( Collection<CacheOperationContext> cacheables, Invoker invoker ) {
		if ( expireCache == null ) {
			expireCache = ApplicationUtil.getBean(ExpireCache.class);
		}
		// 禁用缓存不执行查询
		if ( !CacheOperationContext.getCache() ) { return invoker.invoke(); }
		Object retVal = null;
		if ( !cacheables.isEmpty() ) {
			for ( CacheOperationContext context : cacheables ) {
				Object key = context.generateKey();
				String key_string = key.toString();
				retVal = expireCache.get(key_string, invoker, context.getExpireTime(), context.isCompress());

			}
		}
		return retVal;
	}


	private Map<String, Object> getParamMap( MyBatisInvocation handler ) {
		Object[] args = handler.getArgs();
		Annotation[][] parameterAnnotations = handler.getMethod().getParameterAnnotations();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		for ( int i = 0 ; i < parameterAnnotations.length ; i++ ) {
			for ( Annotation annotation : parameterAnnotations[i] ) {
				if ( annotation instanceof Param ) {
					Param myAnnotation = (Param) annotation;
					paramsMap.put(myAnnotation.value(), args[i]);
				}
			}
		}
		return paramsMap;
	}


	private Object inspectEntityCaches( Collection<CacheOperationContext> entity, Invoker invoker ) {
		if ( entityCache == null ) {
			entityCache = ApplicationUtil.getBean(EntityCache.class);
		}
		Object retObj = null;
		if ( !entity.isEmpty() ) {
			for ( CacheOperationContext context : entity ) {
				Object param = context.getArgs();
				Object id = null;
				if ( !(param instanceof Map) ) {
					// 批处理
					MyBatisInvocation handler = MapperProxyExt.getMyBatisInvocation();
					Map<String, Object> map = getParamMap(handler);
					map.put("i", handler.getBatchIndex());
					id = MVEL.eval(context.getKey(), map);
				} else {
					id = MVEL.eval(context.getKey(), param);
				}

				if ( context.getOperation().isEvict() ) {
					retObj = invoker.invoke();
					entityCache.remove(context.getTableName(), id);
				} else {
					// 禁用缓存不执行查询
					if ( !CacheOperationContext.getCache() ) { return invoker.invoke(); }
					retObj = entityCache.get(context.getTableName(), id, invoker, context.isCompress());
				}

			}
		}
		return retObj;
	}


	public ExpireCache getExpireCache() {
		return expireCache;
	}


	public void setExpireCache( ExpireCache expireCache ) {
		this.expireCache = expireCache;
	}


	private List<CacheOperationContext> createOperationContext(
			Collection<CacheOperation> cacheOp, Method method, Object[] args ) {
		List<CacheOperationContext> list_entity = new ArrayList<CacheOperationContext>(2);
		for ( CacheOperation cacheOperation : cacheOp ) {
			CacheOperationContext opContext = new CacheOperationContext(cacheOperation, method, args);
			list_entity.add(opContext);
		}
		return list_entity;
	}

}
