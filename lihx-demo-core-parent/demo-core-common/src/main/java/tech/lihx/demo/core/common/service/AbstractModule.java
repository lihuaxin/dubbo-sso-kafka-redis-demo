package tech.lihx.demo.core.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 这里是所有MODULE实现都需要继承的父类
 * <p>
 * 在这里你可以定义很多便于使用的抽象方法实现
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public abstract class AbstractModule extends AbstractResponse {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());


}
