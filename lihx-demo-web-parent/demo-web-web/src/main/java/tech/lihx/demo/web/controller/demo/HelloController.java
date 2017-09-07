package tech.lihx.demo.web.controller.demo;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.lihx.demo.core.module.user.IDemoUserModule;


/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   LHX
 * @date	 2017年9月6日 
 * @version  1.0.0	 
 */
@Controller
@RequestMapping( "/demo/" )
public class HelloController {

	@Resource( name = "demoUserModuleImpl" )
	private IDemoUserModule demoUserModuleImpl;


	@ResponseBody
	@RequestMapping( "/hello" )
	public String hello() {
		System.out.println( "进入" );
		return demoUserModuleImpl.sayHello("云").getBody();
	}
	
}
