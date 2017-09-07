package tech.lihx.demo.core.common.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class GeneratorCsv {

	@SuppressWarnings( "rawtypes" )
	public void exportCsv( OutputStream output, Map<String, String> headerMap, Collection dataset ) {
		String pattern = "yyyy-MM-dd";
		ArrayList<String[]> contentList = new ArrayList<String[]>();
		String[] headers = headerMap.keySet().toArray(new String[0]);
		contentList.add(headers);

		Iterator it = dataset.iterator();
		while ( it.hasNext() ) {
			Object t = it.next();
			String[] values = headerMap.values().toArray(new String[0]);
			String[] record = new String[values.length];
			contentList.add(record);
			for ( int i = 0 ; i < values.length ; i++ ) {
				try {
					if ( "".equals(values[i]) ) {
						continue;
					}
					String[] fieldNames = values[i].split("\\.");
					String fieldName = fieldNames[0];
					record[i] = "";
					Object value = null;
					if ( t instanceof Map ) {
						value = ((Map) t).get(fieldName);
					} else {
						Field field = t.getClass().getDeclaredField(fieldName);
						field.setAccessible(true);
						value = field.get(t);
					}

					if ( value == null ) {
						continue;
					}
					if ( value instanceof Boolean ) {
						boolean bValue = (Boolean) value;
						record[i] = "男";
						if ( !bValue ) {
							record[i] = "女";
						}
					} else if ( value instanceof Date ) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						record[i] = sdf.format(date);
					} else if ( value instanceof Map ) {
						Object obj = ((Map) value).get(fieldNames[1]);
						if ( fieldNames.length == 3 ) {
							if ( obj != null ) {
								Field f = obj.getClass().getDeclaredField(fieldNames[2]);
								f.setAccessible(true);
								value = f.get(obj);
								if ( value != null ) {
									record[i] = value.toString();
								} else {
									record[i] = "0";
								}
							} else {
								record[i] = "0";
							}

						} else {
							if ( obj != null ) {
								record[i] = obj.toString();
							} else {
								record[i] = "0";
							}
						}

					} else {
						// 其它数据类型都当作字符串简单处理
						record[i] = value.toString();

					}

				} catch ( Exception e1 ) {
					e1.printStackTrace();
				}
			}
		}
		CsvWriter writer = null;
		try {
			writer = new CsvWriter(output, ',', Charset.forName("GBK"));
			for ( int i = 0 , size = contentList.size() ; i < size ; i++ ) {
				writer.writeRecord(contentList.get(i));
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		} finally {
			if ( writer != null ) {
				writer.close();
			}
		}

	}
}
