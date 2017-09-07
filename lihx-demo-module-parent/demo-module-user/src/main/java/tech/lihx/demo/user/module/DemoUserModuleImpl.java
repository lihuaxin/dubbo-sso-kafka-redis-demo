package tech.lihx.demo.user.module;

import javax.annotation.Resource;

import tech.lihx.demo.core.common.response.Response;
import tech.lihx.demo.core.common.service.AbstractModule;
import tech.lihx.demo.core.module.user.IDemoUserModule;
import tech.lihx.demo.user.service.IDemoService;


/**
 * 每个Module类必须继承AbstractModule类,返回类型必须是Response对象
 * <p>
 *
 * @author   LHX
 * @date	 2016年4月28日 
 * @version  1.0.0	 
 */
public class DemoUserModuleImpl extends AbstractModule implements IDemoUserModule {

	@Resource( )
	private IDemoService demoServiceImpl;

	@Override
	public Response<String> sayHello( String pre ) {
		return success(demoServiceImpl.sayHello(pre));
	}

}
