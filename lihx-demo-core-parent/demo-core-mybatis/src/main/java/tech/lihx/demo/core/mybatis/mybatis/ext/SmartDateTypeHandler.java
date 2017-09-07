package tech.lihx.demo.core.mybatis.mybatis.ext;

//
// package cn.vko.mybatis.ext;
//
// import java.sql.CallableStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Timestamp;
// import java.util.Date;
//
// import org.apache.ibatis.type.DateTypeHandler;
//
// import cn.vko.core.common.mybatis.SmartDate;
//
//
// /**
// * 日期处理
// * <p>
//
// * @author 宋汝波
// * @date 2014年12月9日
// * @version 1.0.0
// */
// public class SmartDateTypeHandler extends DateTypeHandler {
//
//
// @Override
// public Date getNullableResult( ResultSet rs, String columnName ) throws
// SQLException {
// Timestamp sqlTimestamp = rs.getTimestamp(columnName);
// if ( sqlTimestamp != null ) {
// return new SmartDate(sqlTimestamp.getTime());
// }
// return null;
// }
//
//
// @Override
// public Date getNullableResult( ResultSet rs, int columnIndex ) throws
// SQLException {
// Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
// if ( sqlTimestamp != null ) {
// return new SmartDate(sqlTimestamp.getTime());
// }
// return null;
// }
//
//
// @Override
// public Date getNullableResult( CallableStatement cs, int columnIndex ) throws
// SQLException {
// Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
// if ( sqlTimestamp != null ) {
// return new SmartDate(sqlTimestamp.getTime());
// }
// return null;
// }
//}
