package tech.lihx.demo.core.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 * excel辅助类
 * <p>
 * 
 * 
 * @author LHX
 * @date 2015-12-8
 * @version 1.0.0
 */
public class ExcelUtil {

	/**
	 * 读取Excel表格表头的内容
	 * <p>
	 *
	 * @param row
	 *            头行内容
	 * @param colNum
	 *            总列数
	 * @return
	 */
	public static String[] readExcelTitle( HSSFRow row, int colNum ) {

		String[] title = new String[colNum];
		for ( int i = 0 ; i < colNum ; i++ ) {
			title[i] = getStringCellValue(row.getCell(i)).trim();
		}
		return title;
	}


	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * <p>
	 *
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	public static String getStringCellValue( HSSFCell cell ) {

		String strCell = "";
		if ( cell != null ) {
			switch ( cell.getCellType() ) {
				case HSSFCell.CELL_TYPE_STRING :
					strCell = cell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC :
					DecimalFormat df = new DecimalFormat("0.0");
					strCell = df.format(cell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN :
					strCell = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA :
					strCell = String.valueOf(cell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_BLANK :
					break;
				default:
					break;
			}
		}
		if ( strCell == null || strCell.equals("") ) { return ""; }
		return strCell.trim();
	}


	/**
	 * sheet 合并单元格
	 * 
	 * <p>
	 *
	 * @param sheet
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束列
	 * @param firstCol
	 *            开始行
	 * @param lastCo
	 *            结束列
	 */
	public static void addRegion( HSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol ) {
		CellRangeAddress range = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(range);
	}


	public static HSSFCellStyle getDefulatStyle( HSSFWorkbook wb ) {
		HSSFCellStyle style = wb.createCellStyle();// cell样式
		// 设置单元格上、下、左、右的边框线
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置单元格字体居中（左右方向）
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 设置字体显示居中(上下方向)

		return style;
	}


	/**
	 * 为excel一行赋值
	 * 
	 * <p>
	 *
	 * @param row
	 * @param cellValues
	 *            值
	 * @param style
	 *            样式
	 */
	public static void addRow( HSSFRow row, List<String> cellValues, HSSFCellStyle style ) {
		for ( int i = 0 ; i < cellValues.size() ; i++ ) {
			String cellValue = cellValues.get(i);
			HSSFCell headerCell = row.createCell(i);
			headerCell.setCellValue(cellValue);
			headerCell.setCellStyle(style);
		}
	}


	public static void addRow( HSSFRow row, List<Object> cellValues ) {
		for ( int i = 0 ; i < cellValues.size() ; i++ ) {
			Object cellValue = cellValues.get(i);
			HSSFCell headerCell = row.createCell(i);
			if ( cellValue instanceof String ) {
				headerCell.setCellValue((String) cellValue);
			} else if ( cellValue instanceof Integer ) {
				headerCell.setCellValue((Integer) cellValue);
			} else if ( cellValue instanceof Long || cellValue instanceof Double || cellValue instanceof BigDecimal ) {
				headerCell.setCellValue(new Double(cellValue.toString()));
			}
		}
	}
}
