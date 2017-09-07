package tech.lihx.demo.core.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 所有endpoint层需要继承的父类
 * <p>
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class AbstractEndpoint extends AbstractResponse {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
