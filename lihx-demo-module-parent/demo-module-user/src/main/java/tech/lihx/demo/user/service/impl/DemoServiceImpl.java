package tech.lihx.demo.user.service.impl;

import org.springframework.stereotype.Service;

import tech.lihx.demo.core.common.service.AbstractService;
import tech.lihx.demo.user.service.IDemoService;

/**
 * 所有Sevice实现类需要继承AbstractService类
 * <p>
 *
 * @author   LHX
 * @date	 2016年4月28日 
 * @version  1.0.0	 
 */
@Service
public class DemoServiceImpl extends AbstractService implements IDemoService {


	@Override
	public String sayHello( String suffix ) {
		return "你好," + suffix;
	}

}
