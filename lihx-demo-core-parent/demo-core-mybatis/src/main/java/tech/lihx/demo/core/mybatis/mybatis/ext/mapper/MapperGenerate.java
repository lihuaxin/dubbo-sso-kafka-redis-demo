package tech.lihx.demo.core.mybatis.mybatis.ext.mapper;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;


/**
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class MapperGenerate {

	private static Logger logger = LoggerFactory.getLogger(MapperGenerate.class);

	private final String tableName;

	private final String beanName;

	private final Configuration configuration;

	private final Class<?> beanClass;

	private final Class<?> mapperClass;

	private final String idName;


	public MapperGenerate(
			Configuration configuration,
			Class<?> beanClass,
			Class<?> mapperClass,
			String tableName,
			String idName ) {
		this.configuration = configuration;
		this.beanClass = beanClass;
		this.tableName = tableName;
		this.mapperClass = mapperClass;
		this.idName = idName;
		beanName = beanClass.getName();
	}


	private Map<String, String> getColumnAndType() {
		LinkedHashMap<String, String> propertyType = new LinkedHashMap<String, String>();
		MetaClass metaClass = MetaClass.forClass(beanClass);
		Set<String> propertySet = new HashSet<String>();
		String[] getter = metaClass.getGetterNames();
		for ( String property : getter ) {
			String lower = property.toLowerCase();
			if ( !propertySet.contains(lower) ) {
				propertyType.put(property, metaClass.getGetterType(property).getName());
			} else {
				logger.info(beanName + "存在相同属性:" + property + ",请查看getter方法命名");
			}
		}
		return propertyType;
	}


	private Map<String, List<String>> indexingMap() throws Exception {
		Map<String, List<String>> indexMap = null;
		try {
			DataSource ds = configuration.getEnvironment().getDataSource();
			Connection conn = ds.getConnection();
			indexMap = new HashMap<String, List<String>>();
			PreparedStatement statement = conn
					.prepareStatement("SELECT COLUMN_NAME,INDEX_NAME FROM information_schema.STATISTICS WHERE TABLE_NAME=? AND UPPER(INDEX_NAME)!='PRIMARY' ORDER BY SEQ_IN_INDEX ASC");
			statement.setString(1, tableName);

			ResultSet results = statement.executeQuery();

			while ( results.next() ) {
				String column_name = results.getString("COLUMN_NAME");
				String index_name = results.getString("INDEX_NAME");

				List<String> indexSet = indexMap.get(index_name);
				if ( indexSet == null ) {
					indexSet = new ArrayList<String>();
					indexMap.put(index_name, indexSet);
				}
				indexSet.add(column_name);
			}

			results.close();
			statement.close();
			conn.close();
		} catch ( Exception e ) {
			logger.error("查询索引失败", e);

		}

		return indexMap;
	}


	private List<String> matchIndex( Map<String, List<String>> indexMap, List<String> columns ) {
		if ( indexMap == null ) { return columns; }
		List<String> indexColumn = null;
		int maxMatch = 0;
		for ( Map.Entry<String, List<String>> indexEntry : indexMap.entrySet() ) {
			List<String> indexSet = indexEntry.getValue();// .contains(o);
			if ( indexSet.size() > maxMatch ) {
				maxMatch = indexSet.size();
				indexColumn = indexSet;
			}
		}
		if ( indexColumn != null ) {
			List<String> newColumn = new ArrayList<String>(indexColumn);
			// 重新组织列,以适合where查询
			for ( String column : columns ) {
				if ( !indexColumn.contains(column) ) {
					newColumn.add(column);
				}
			}
			return newColumn;
		}
		return columns;
	}


	public void build() {
		try {
			String mapperStr = buildMapperXml();
			ByteArrayResource resourse = new ByteArrayResource(mapperStr.getBytes("UTF-8"));
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resourse.getInputStream(), configuration, beanName
					+ ".commonMapper", configuration.getSqlFragments());
			xmlMapperBuilder.parse();
		} catch ( Exception e ) {
			throw new RuntimeException("Failed to parse mapping resource", e);
		}
	}


	private String buildMapperXml() throws Exception {
		String namespace = mapperClass.getName();
		StringWriter bw = new StringWriter();
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bw.write("\r\n");
		bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
		bw.write("\r\n");
		bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		bw.write("\r\n");
		bw.write("<mapper namespace=\"" + namespace + "\">");
		bw.write("\r\n");
		bw.write("\r\n");
		buildSQL(bw);
		bw.write("</mapper>");
		bw.flush();
		bw.close();

		return bw.toString();
	}


	private String selectById( String id, List<String> columns, Map<String, String> property ) {
		int size = columns.size();
		StringWriter writer = new StringWriter();
		// 查询（根据主键ID查询）
		writer.write("\t<!-- 查询（根据主键ID查询） -->");
		writer.write("\r\n");
		writer.write("\t<select id=\""
				+ id + "\" resultType=\"" + beanName + "\" parameterType=\"" + property.get(idName) + "\">");
		writer.write("\r\n");
		writer.write("\t\t SELECT");
		writer.write("\r\n");

		for ( int i = 0 ; i < size ; i++ ) {
			if ( i != 0 ) {
				writer.write(",");
			}
			writer.write("\t" + columns.get(i));
		}


		writer.write("\r\n");
		writer.write("\t\t FROM " + tableName);
		writer.write("\r\n");
		writer.write("\t\t WHERE " + idName + " = #{" + idName + "}");
		writer.write("\r\n");
		writer.write("\t</select>");
		writer.write("\r\n");
		writer.write("\r\n");
		// 查询完
		return writer.toString();
	}


	private String deleteById( String id, Map<String, String> property ) {
		StringWriter writer = new StringWriter();
		// 删除（根据主键ID删除）
		writer.write("\t<!--删除：根据主键ID删除-->");
		writer.write("\r\n");
		writer.write("\t<delete id=\"" + id + "\" parameterType=\"" + property.get(idName) + "\">");
		writer.write("\r\n");
		writer.write("\t\t DELETE FROM " + tableName);
		writer.write("\r\n");
		writer.write("\t\t WHERE " + idName + " = #{" + idName + "}");
		writer.write("\r\n");
		writer.write("\t</delete>");
		writer.write("\r\n");
		writer.write("\r\n");
		// 删除完
		return writer.toString();
	}


	private String insert( String id1, String id2, List<String> columns ) {
		int size = columns.size();
		StringWriter bw = new StringWriter();
		// 添加insert方法
		bw.write("\t<!-- 添加 -->");
		bw.write("\r\n");
		bw.write("\t<insert id=\"" + id1 + "\" parameterType=\"" + beanName + "\">");
		bw.write("\r\n");
		bw.write("\t\t INSERT INTO " + tableName);
		bw.write("\r\n");
		bw.write(" \t\t(");
		for ( int i = 0 ; i < size ; i++ ) {
			bw.write(columns.get(i));
			if ( i != size - 1 ) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.write("\r\n");
		bw.write("\t\t VALUES ");
		bw.write("\r\n");
		bw.write(" \t\t(");
		for ( int i = 0 ; i < size ; i++ ) {
			bw.write("#{" + columns.get(i) + "}");
			if ( i != size - 1 ) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.write("\r\n");
		bw.write("\t</insert>");
		bw.write("\r\n");
		bw.write("\r\n");
		// 添加insert完


		// --------------- insert方法（匹配有值的字段）
		bw.write("\t<!-- 添加 （匹配有值的字段）-->");
		bw.write("\r\n");
		bw.write("\t<insert id=\"" + id2 + "\" parameterType=\"" + beanName + "\">");
		bw.write("\r\n");
		bw.write("\t\t INSERT INTO " + tableName);
		bw.write("\r\n");
		bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		bw.write("\r\n");

		String tempField = null;
		for ( int i = 0 ; i < size ; i++ ) {
			tempField = columns.get(i);
			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
			bw.write("\r\n");
			bw.write("\t\t\t\t " + tempField + ",");
			bw.write("\r\n");
			bw.write("\t\t\t</if>");
			bw.write("\r\n");
		}

		bw.write("\r\n");
		bw.write("\t\t </trim>");
		bw.write("\r\n");

		bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
		bw.write("\r\n");

		tempField = null;
		for ( int i = 0 ; i < size ; i++ ) {
			tempField = columns.get(i);
			bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
			bw.write("\r\n");
			bw.write("\t\t\t\t #{" + tempField + "},");
			bw.write("\r\n");
			bw.write("\t\t\t</if>");
			bw.write("\r\n");
		}

		bw.write("\t\t </trim>");
		bw.write("\r\n");
		bw.write("\t</insert>");
		bw.write("\r\n");
		bw.write("\r\n");
		// --------------- 完毕

		return bw.toString();
	}


	private String update( String id1, String id2, List<String> columns ) {
		int size = columns.size();
		StringWriter bw = new StringWriter();
		// 修改update方法
		bw.write("\t<!-- 修 改-->");
		bw.write("\r\n");
		bw.write("\t<update id=\"" + id2 + "\" parameterType=\"" + beanName + "\">");
		bw.write("\r\n");
		bw.write("\t\t UPDATE " + tableName);
		bw.write("\r\n");
		bw.write(" \t\t <set> ");
		bw.write("\r\n");

		String tempField = null;
		for ( int i = 0 ; i < size ; i++ ) {
			tempField = columns.get(i);
			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
			bw.write("\r\n");
			bw.write("\t\t\t\t " + tempField + " = #{" + tempField + "},");
			bw.write("\r\n");
			bw.write("\t\t\t</if>");
			bw.write("\r\n");
		}

		bw.write("\r\n");
		bw.write(" \t\t </set>");
		bw.write("\r\n");
		bw.write("\t\t WHERE " + idName + " = #{" + idName + "}");
		bw.write("\r\n");
		bw.write("\t</update>");
		bw.write("\r\n");
		bw.write("\r\n");
		// update方法完毕

		// ----- 修改（匹配有值的字段）
		bw.write("\t<!-- 修 改-->");
		bw.write("\r\n");
		bw.write("\t<update id=\"" + id1 + "\" parameterType=\"" + beanName + "\">");
		bw.write("\r\n");
		bw.write("\t\t UPDATE " + tableName);
		bw.write("\r\n");
		bw.write("\t\t SET ");

		bw.write("\r\n");
		tempField = null;
		for ( int i = 0 ; i < size ; i++ ) {
			tempField = columns.get(i);
			bw.write("\t\t\t " + tempField + " = #{" + tempField + "}");
			if ( i != size - 1 ) {
				bw.write(",");
			}
			bw.write("\r\n");
		}

		bw.write("\t\t WHERE " + idName + " = #{" + idName + "}");
		bw.write("\r\n");
		bw.write("\t</update>");
		bw.write("\r\n");

		return bw.toString();
	}


	private void buildSQL( StringWriter bw ) throws Exception {
		Map<String, String> property = getColumnAndType();
		List<String> columns = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		for ( Map.Entry<String, String> entry : property.entrySet() ) {
			columns.add(entry.getKey());
			types.add(entry.getValue());
		}
		bw.write("\r\n");
		bw.write("\r\n");
		// 查询
		String selectOne = selectById("selectById", columns, property);
		String selectBatch = selectById("selectByIdBatch", columns, property);
		bw.write(selectOne);
		bw.write(selectBatch);
		// 删除
		String deleteOne = deleteById("deleteById", property);
		String deleteBatch = deleteById("deleteByIdBatch", property);
		bw.write(deleteOne);
		bw.write(deleteBatch);

		// 插入
		String insertOne = insert("insert", "insertSelective", columns);
		String insertBatch = insert("insertBatch", "insertSelectiveBatch", columns);
		bw.write(insertOne);
		bw.write(insertBatch);
		// 更新
		String updateOne = update("updateById", "updateByIdSelective", columns);
		String updateBatch = update("updateByIdBatch", "updateByIdSelectiveBatch", columns);
		bw.write(updateOne);
		bw.write(updateBatch);

		// 拼装按条件查询,自动匹配索引列
		Map<String, List<String>> indexMap = indexingMap();
		List<String> newColumns = matchIndex(indexMap, columns);
		String selectList = selectListAndOne("selectList", false, newColumns);
		String select = selectListAndOne("selectOne", true, newColumns);

		bw.write(selectList);
		bw.write(select);


		bw.write("\r\n");
	}


	private String selectListAndOne( String id, boolean isOne, List<String> newColumns ) {

		int size = newColumns.size();
		StringWriter writer = new StringWriter();
		// 查询（根据主键ID查询）
		writer.write("\r\n");
		writer.write("\t<select id=\"" + id + "\" resultType=\"" + beanName + "\">");
		writer.write("\r\n");
		writer.write("\t\t SELECT");
		writer.write("\r\n");

		for ( int i = 0 ; i < size ; i++ ) {
			if ( i != 0 ) {
				writer.write(",");
			}
			writer.write("\t" + newColumns.get(i));
		}


		writer.write("\r\n");
		writer.write("\t\t FROM " + tableName);
		writer.write("\r\n");
		writer.write("\t\t <where>\r\n");

		String tempField = null;
		for ( int i = 0 ; i < size ; i++ ) {
			tempField = newColumns.get(i);
			writer.write("\t\t\t<if test=\"" + tempField + "!=null\">\r\n");
			writer.write("\t\t\t AND " + tempField + "=#{" + tempField + "}\r\n");
			writer.write("\t\t\t</if>\r\n");
		}

		writer.write("\t\t </where>\r\n");
		if ( isOne ) {
			writer.write("\t\t limit 1\r\n");
		}
		writer.write("\t</select>");
		writer.write("\r\n");
		writer.write("\r\n");
		// 查询完
		return writer.toString();
	}
}
