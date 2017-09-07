package tech.lihx.demo.core.common.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 减少强制转换的痛苦 从数据库中查询出对象后直接得到具体的类型
 * <p>
 * 
 * @author LHX
 * @Date 2016-7-11
 */
@SuppressWarnings( "unchecked" )
public class SmartMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = -2056047758732968455L;


	public <T> T getObjectByType( String key, Class<T> cls ) {
		Object obj = get(key);
		if ( obj == null ) { return null; }
		if ( obj.getClass() == cls ) {
			return (T) get(key);
		} else {
			return getObj(obj, cls);
		}
	}


	private <T> T getObj( Object obj, Class<T> cls ) {
		try {
			return cls.getConstructor(String.class).newInstance(obj.toString());
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}
	}


	public <T> T getPrimaryObject( String key, Class<T> cls ) {
		Object value = get(key);
		if ( value == null ) { return getDefaultValue(cls); }
		if ( cls == int.class ) { return (T) new Integer(value.toString()); }
		if ( cls == long.class ) { return (T) new Long(value.toString()); }
		if ( cls == byte[].class ) { return (T) value; }
		if ( cls == double.class ) { return (T) new Double(value.toString()); }
		if ( cls == float.class ) { return (T) new Float(value.toString()); }
		if ( cls == boolean.class ) { return (T) new Boolean(value.toString()); }
		if ( cls == short.class ) { return (T) new Short(Short.parseShort(value.toString())); }
		return null;
	}


	public <T> T getDefaultValue( Class<T> cls ) {
		if ( cls == int.class ) { return (T) new Integer(0); }
		if ( cls == long.class ) { return (T) new Long(0); }
		if ( cls == byte[].class ) { return (T) new byte[ ] { 0 }; }
		if ( cls == double.class ) { return (T) new Double(0); }
		if ( cls == float.class ) { return (T) new Float(0); }
		if ( cls == boolean.class ) { return (T) new Boolean(false); }
		if ( cls == char.class ) { return (T) new Character('\0'); }
		if ( cls == short.class ) { return (T) new Short((short) 0); }
		return null;
	}


	// 基本类型
	public int getint( String key ) {
		return getPrimaryObject(key, int.class);
	}


	public float getfloat( String key ) {
		return getPrimaryObject(key, float.class);
	}


	public double getdouble( String key ) {
		return getPrimaryObject(key, double.class);
	}


	public long getlong( String key ) {
		return getPrimaryObject(key, long.class);
	}


	public boolean getboolean( String key ) {
		return getPrimaryObject(key, boolean.class);
	}


	// 以下对象类型
	public Integer getInteger( String key ) {
		return getObjectByType(key, Integer.class);
	}


	public Long getLong( String key ) {
		return getObjectByType(key, Long.class);
	}


	public Double getDouble( String key ) {
		return getObjectByType(key, Double.class);
	}


	public Boolean getBoolean( String key ) {
		return getObjectByType(key, Boolean.class);
	}


	public Float getFloat( String key ) {
		return getObjectByType(key, Float.class);
	}


	public String getString( String key ) {
		return getObjectByType(key, String.class);
	}


	public BigDecimal getBigDecimal( String key ) {
		return getObjectByType(key, BigDecimal.class);
	}


	public Date getDate( String key ) {
		return getObjectByType(key, Date.class);
	}


	public Timestamp getTimestamp( String key ) {
		return getObjectByType(key, Timestamp.class);
	}


	public java.sql.Date getSqlDate( String key ) {
		return getObjectByType(key, java.sql.Date.class);
	}


	@SuppressWarnings( "rawtypes" )
	public <T> List<T> getList( String key, Class<T> cls ) {
		Object obj = get(key);
		if ( obj == null ) { return null; }
		if ( obj instanceof List ) {
			List lst = (List) obj;
			if ( lst.size() > 0 ) {
				if ( lst.get(0).getClass() == cls ) {
					return lst;
				} else {
					List<T> newLst = new ArrayList<T>();
					for ( Object object : lst ) {
						newLst.add(getObj(object, cls));
					}
					return newLst;
				}
			}
		}
		if ( obj.getClass().isArray() ) {
			List<T> newLst = new ArrayList<T>();
			int length = Array.getLength(obj);
			if ( length > 0 ) {
				if ( Array.get(obj, 0).getClass() == cls ) {
					for ( int i = 0 ; i < length ; i++ ) {
						newLst.add((T) Array.get(obj, i));
					}
				} else {
					for ( int i = 0 ; i < length ; i++ ) {
						newLst.add(getObj(Array.get(obj, i), cls));
					}
				}
			}
			return newLst;
		}
		List<T> newLst = new ArrayList<T>(1);
		if ( obj.getClass() == cls ) {
			newLst.add((T) obj);
			return newLst;
		} else {
			newLst.add(getObj(obj, cls));
			return newLst;
		}
	}
}
