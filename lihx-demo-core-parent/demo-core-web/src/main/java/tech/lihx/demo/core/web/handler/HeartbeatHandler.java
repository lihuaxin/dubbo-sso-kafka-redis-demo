package tech.lihx.demo.core.web.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.util.ApplicationUtil;
import tech.lihx.demo.core.sso.SSOConfig;
import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.client.SSOCache;
import tech.lihx.demo.core.sso.client.SSOHelper;
import tech.lihx.demo.core.sso.client.handler.SSOHandler;
import tech.lihx.demo.core.web.cache.RedisCache;
import tech.lihx.demo.core.web.filter.Invocation;


/**
 * 登录用户心跳控制器
 * 
 * @author LHX
 * @date 2016-11-17
 * @version 1.0.0
 */
public class HeartbeatHandler extends SSOHandler {

	protected Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);


	@Override
	public boolean forceOut() {
		boolean forceOut = false;
		RedisCache redis = ApplicationUtil.getBean(RedisCache.class);
		Invocation inv = ApplicationUtil.getBean(Invocation.class);
		SSOToken token = (SSOToken) SSOHelper.getToken(inv.getRequest());
		// 检查心跳
		String key = SSOCache.key(token.getUserId());
		String heartbeat = (String) redis.get(key);
		if ( StringUtils.isBlank(heartbeat) ) {
			/**
			 * 判断超时强制用户退出重新登录 删除缓存用户信息
			 */
			redis.del(SSOCache.key(token.toString()));
			forceOut = true;
		} else {
			try {
				// 延时心跳
				boolean heart = redis.expire(key, SSOConfig.getLoginLoseTime());
				if ( !heart ) {
					logger.error("heartbeat expire return false!");
				}
			} catch ( Exception e ) {
				logger.error(" heartbeat expire error. \n{}", e.toString());
			}
		}

		return forceOut;
	}
}
