package tech.lihx.demo.core.cache.util;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.cache.support.CacheTablesNamesFinder;

/**
 * @author  lihx
 * @version 2017-9-5 10:45:10
 */
public class TableNamesUtil {

	private static final Logger logger = LoggerFactory.getLogger(TableNamesUtil.class);

	private static CCJSqlParserManager pm = new CCJSqlParserManager();


	public static List<String> getTableNames( String sql ) {
		try {
			Statement statement = pm.parse(new StringReader(sql));
			CacheTablesNamesFinder finder = new CacheTablesNamesFinder();
			if ( !finder.isCanCache() ) { return Collections.emptyList(); }
			if ( statement instanceof Select ) {
				return finder.getTableList((Select) statement);
			} else if ( statement instanceof Update ) {
				return finder.getTableList((Update) statement);
			} else if ( statement instanceof Insert ) {
				return finder.getTableList((Insert) statement);
			} else if ( statement instanceof Delete ) {
				return finder.getTableList((Delete) statement);
			} else if ( statement instanceof Replace ) { return finder.getTableList((Replace) statement); }
		} catch ( JSQLParserException e ) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}


	public static String getCacheKey( String sql ) {
		List<String> tableNames = getTableNames(sql);
		return getCacheKey(tableNames);
	}


	public static String getCacheKey( List<String> tableNames ) {
		if ( tableNames == null ) { return null; }
		StringBuilder builder = new StringBuilder();
		for ( String name : tableNames ) {
			String tempNmae = name.toUpperCase();
			// 视图
			if ( tempNmae.startsWith("V_") ) { return null; }
			builder.append(tempNmae);
			builder.append("-");
		}
		return builder.append(":").toString();
	}
}
