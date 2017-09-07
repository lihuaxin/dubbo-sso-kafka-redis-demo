package tech.lihx.demo.core.support.optlog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import tech.lihx.demo.core.common.util.ApplicationUtil;

/**
 * 拦截所有sprinmvc请求, 读取priviledge注解信息 异步操作日志
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:15:25
 */
public class OptLogHandlerInterceptor implements HandlerInterceptor {

	private OptLog optLog;


	// private MQService mqService;

	// private Handler<Object[]> preHandle = new Handler<Object[]>() {
	// @Override
	// public void handle(Object[] data) {
	// HttpServletRequest uri = (HttpServletRequest) data[0];
	// HandlerMethod hm = (HandlerMethod) data[1];
	// LogBean bean = getBean(hm);
	// optLog.preHandle(uri, hm, bean);
	// }
	//
	// };
	// private Handler<Object[]> afterCompletion = new Handler<Object[]>() {
	// @Override
	// public void handle(Object[] data) {
	// HttpServletRequest uri = (HttpServletRequest) data[0];
	// HandlerMethod hm = (HandlerMethod) data[1];
	// LogBean bean = getBean(hm);
	// Exception ex = (Exception) data[2];
	// optLog.afterCompletion(uri, hm, bean, ex);
	// }
	//
	// };

	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		if ( handler instanceof HandlerMethod ) {
			if ( optLog == null ) {
				optLog = ApplicationUtil.getBean(OptLog.class);
			}
			optLog.preHandle(request, (HandlerMethod) handler);

		}
		return true;

	}


	// private LogBean getBean(HandlerMethod hm) {
	// Logger privi = hm.getMethodAnnotation(Logger.class);
	// LogBean bean = new LogBean();
	// if (privi == null) {
	// return null;
	// }
	// bean.setClassName(hm.getBeanType().getName());
	// bean.setMethodName(hm.getMethod().getName());
	// bean.setDesc(privi.value());
	// return bean;
	// }

	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView )
		throws Exception {

	}


	@Override
	public void
			afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
				throws Exception {
		if ( handler instanceof HandlerMethod ) {
			optLog.afterCompletion(request, (HandlerMethod) handler, ex);
		}
	}


	public OptLog getOptLog() {
		return optLog;
	}


	public void setOptLog( OptLog optLog ) {
		this.optLog = optLog;
	}

	// public MQService getMqService() {
	// return mqService;
	// }
	//
	// public void setMqService(MQService mqService) {
	// this.mqService = mqService;
	// }

}
