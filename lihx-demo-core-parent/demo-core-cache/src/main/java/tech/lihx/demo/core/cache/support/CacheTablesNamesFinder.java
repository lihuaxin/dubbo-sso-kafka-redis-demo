package tech.lihx.demo.core.cache.support;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * @author lihx
 * @version 2013年11月1日 上午10:47:24
 */
@SuppressWarnings( "unused" )
public class CacheTablesNamesFinder extends TablesNamesFinder implements SelectItemVisitor {

	private final List<String> tables;

	private boolean canCache = true;


	public CacheTablesNamesFinder() {
		tables = new ArrayList<String>(4);
	}


	@Override
	public List<String> getTableList( Delete delete ) {
		tables.addAll(super.getTableList(delete));
		return tables;
	}


	@Override
	public List<String> getTableList( Insert insert ) {

		tables.addAll(super.getTableList(insert));
		return tables;
	}


	public boolean isCanCache() {
		return canCache;
	}


	@Override
	public List<String> getTableList( Replace replace ) {

		tables.addAll(super.getTableList(replace));
		return tables;
	}


	@Override
	public List<String> getTableList( Select select ) {
		tables.addAll(super.getTableList(select));
		return tables;
	}


	@Override
	public List<String> getTableList( Update update ) {
		tables.addAll(super.getTableList(update));
		return tables;
	}


	/*
	 * group by子句
	 */
	@Override
	public void visit( PlainSelect plainSelect ) {
		super.visit(plainSelect);
		List<SelectItem> selectItemsList = plainSelect.getSelectItems();
		for ( SelectItem selectItem : selectItemsList ) {
			selectItem.accept(this);
		}
		Expression expression = plainSelect.getHaving();
		if ( expression != null ) {
			getOtherTableName(expression.toString());
		}
	}


	@Override
	public void visit( AllColumns allColumns ) {
	}


	@Override
	public void visit( AllTableColumns allTableColumns ) {
	}


	/*
	 * 扫描select子句中的表名
	 */
	@Override
	public void visit( SelectExpressionItem selectExpressionItem ) {
		Expression expression = selectExpressionItem.getExpression();
		if ( expression != null ) {
			String sql = expression.toString();
			getOtherTableName(sql);
		}
	}


	public void getOtherTableName( String sql ) {
		int from = sql.indexOf(" FROM ");
		if ( from < 0 ) { return; }
		// 如果select子句或group by子句中有from,则不进行缓存操作
		canCache = false;
		// int where = sql.indexOf(" WHERE ");
		// if (sql.indexOf(" JOIN ") > -1) {
		// getJoinTableName(sql);
		// } else if (from > -1 && where > -1) {
		// String multiple = sql.substring(from, where);
		// if (multiple.contains(",")) {
		// getMultipleTableName(sql);
		// } else {
		// getCommonTableName(sql);
		// }
		// } else {
		// getCommonTableName(sql);
		// }
	}


	// public void getJoinTableName(String sql) {
	//
	// }
	//
	// public void getMultipleTableName(String sql) {
	//
	// }

	public void getCommonTableName( String sql ) {
		String reg = "\\((.*FROM\\s)(\\w*)(.*)\\)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(sql);
		while ( matcher.find() ) {
			String sqlUpper = matcher.group(2).toUpperCase();
			tables.add(sqlUpper.trim());
		}
	}
}
