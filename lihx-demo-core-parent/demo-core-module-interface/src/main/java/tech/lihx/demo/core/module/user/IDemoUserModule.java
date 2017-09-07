package tech.lihx.demo.core.module.user;

import tech.lihx.demo.core.common.response.Response;


/**
 * 此类为Demo类
 * <p>
 *
 * @author   LHX
 * @date	 2017年9月6日
 * @version  1.0.0	 
 */
public interface IDemoUserModule {


	public Response<String> sayHello( String pre );

}
