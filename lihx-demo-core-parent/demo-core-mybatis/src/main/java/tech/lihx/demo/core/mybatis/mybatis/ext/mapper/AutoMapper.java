package tech.lihx.demo.core.mybatis.mybatis.ext.mapper;

import java.util.List;

import tech.lihx.demo.core.mybatis.mybatis.annotation.Batch;

/**
 * 基础增删改查操作,子类继承此接口,并加TableName注解
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5 10:22:23
 * @version 1.0.0
 */
public interface AutoMapper<T, K> {

	/**
	 *
	 * 查询（根据主键ID查询）
	 *
	 **/

	T selectById( K id );


	/**
	 *
	 * 删除（根据主键ID删除）
	 *
	 **/
	int deleteById( K id );


	/**
	 *
	 * 添加
	 *
	 **/
	int insert( T record );


	/**
	 *
	 * 添加 （匹配有值的字段）
	 *
	 **/
	int insertSelective( T record );


	/**
	 *
	 * 修改 （匹配有值的字段）
	 *
	 **/
	int updateByIdSelective( T record );


	/**
	 *
	 * 修改（根据主键ID修改）
	 *
	 **/
	int updateById( T record );


	/**
	 * 以下批量操作
	 * */

	@Batch
	List<T> selectByIdBatch( List<K> idList );


	@Batch
	int deleteByIdBatch( List<K> idList );


	@Batch
	int insertBatch( List<T> recordList );


	@Batch
	int insertSelectiveBatch( List<T> recordList );


	@Batch
	int updateByIdSelectiveBatch( List<T> recordList );


	@Batch
	int updateByIdBatch( List<T> recordList );


	List<T> selectList( T record );


	T selectOne( T record );


}
