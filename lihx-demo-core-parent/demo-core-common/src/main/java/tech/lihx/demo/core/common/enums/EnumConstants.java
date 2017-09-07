package tech.lihx.demo.core.common.enums;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目所有枚举类在这里统一定义
 * <p>
 * 如果对枚举类增加类型时请自行增加不要删除历史的枚举类型
 *
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public final class EnumConstants {

	private static final Logger logger = LoggerFactory.getLogger(EnumConstants.class);

	public enum FieldType {
		key, desc;
	}


	public static Map<Integer, String> getKeyAndDesc( Class<? extends IEnum> clazz ) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		try {
			for ( Object enu : Arrays.asList(clazz.getEnumConstants()) ) {
				Integer key = null;
				String value = null;
				for ( Method method : clazz.getDeclaredMethods() ) {
					if ( method.getName().equals(FieldType.key.name()) ) {
						key = (Integer) method.invoke(enu);
					}
					if ( method.getName().equals(FieldType.desc.name()) ) {
						value = (String) method.invoke(enu);
					}
				}
				result.put(key, value);
			}
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}


	// 对枚举封装下拉列表,为添加修改页面做下拉列表用
	public static List<Map<String, String>> getList( Class<? extends IEnum> clazz ) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			for ( Object obj : Arrays.asList(clazz.getEnumConstants()) ) {
				Map<String, String> map = new HashMap<String, String>();
				for ( Method method : clazz.getDeclaredMethods() ) {
					if ( method.getName().equals("key") ) {
						map.put("key", method.invoke(obj) + "");
					}
					if ( method.getName().equals("desc") ) {
						map.put("desc", method.invoke(obj) + "");
					}
				}

				list.add(map);
			}
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	// 定义基础配置表枚举类型(YY_BASE_CODE)
	public enum BaseCode implements IEnum {

		EXAMPLE(1, "例子"),;

		// 枚举值
		private final int key;

		// 枚举描述
		private final String desc;


		private BaseCode( final int key, final String desc ) {
			this.key = key;
			this.desc = desc;
		}


		@Override
		public int key() {
			return key;
		}


		@Override
		public String desc() {
			return desc;
		}

	}
	
	// 定义基础配置表枚举类型(YY_BASE_CODE)
		public enum UserType implements IEnum {

			STUDENT(1, "学生"),;

			// 枚举值
			private final int key;

			// 枚举描述
			private final String desc;


			private UserType( final int key, final String desc ) {
				this.key = key;
				this.desc = desc;
			}


			@Override
			public int key() {
				return key;
			}


			@Override
			public String desc() {
				return desc;
			}

		}
		
		
}
