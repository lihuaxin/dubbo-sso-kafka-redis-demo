package tech.lihx.demo.core.common.mybatis;


/**
 * 实体实现此接口表示有主键id,且如果插入时为null, 则自动通过idworker获取id并赋值
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public interface Identity {

	public void setId( Long id );


	public Long getId();
}
