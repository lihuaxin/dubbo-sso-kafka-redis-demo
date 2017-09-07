package tech.lihx.demo.core.common.enums;


/**
 * 定义所有枚举类的接口类型
 * <p>
 * 所有枚举类必须实现此接口
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public interface IEnum {

	/**
	 * 定义枚举值
	 */
	public int key();


	/**
	 * 定义枚举描述
	 */
	public String desc();

}
