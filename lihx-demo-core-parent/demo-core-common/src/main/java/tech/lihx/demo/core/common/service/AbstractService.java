package tech.lihx.demo.core.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 所有Service需要继承的父类
 * <p>
 * 里面可以封装Service层需要公用的方法等
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public abstract class AbstractService extends AbstractResponse {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());


}
