package tech.lihx.demo.core.mybatis.mybatis.ext.mapper;


/**
 * 基础增删改查操作,子类继承此接口,并加TableName注解
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5 10:22:09
 * @version 1.0.0
 */
public interface CommonMapper<T> extends AutoMapper<T, Long> {


}
