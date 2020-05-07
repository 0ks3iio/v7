package net.zdsoft.bigdata.data.manager.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午1:44
 */
public class ISQLUtils {

    private static Map<String, IColumn[]> columnCache = new ConcurrentHashMap<>(16);

    /**
     * 解析SQL的列名和类型
     */
    public static IColumn[] parseColumns(String sql, ColumnExecutor executor) throws SQLException {
        IColumn[] iColumns;
        if ((iColumns = columnCache.get(sql)) == null) {
            iColumns = executor.execute();
            columnCache.put(sql, iColumns);
        }
        return Arrays.copyOf(iColumns, iColumns.length);
    }

    /** 检查sql是否合法 */
    public static boolean checkSQL(String sql) {
        return true;
    }

    public static IColumn parseValue(ResultSet resultSet, IColumn iColumn) throws SQLException {
        int type = iColumn.getType();
        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGNVARCHAR:
                iColumn.setValue(resultSet.getString(iColumn.getName()));
                break;
            case Types.INTEGER:
                iColumn.setValue(resultSet.getInt(iColumn.getName()));
                break;
            case Types.FLOAT:
                iColumn.setValue(resultSet.getFloat(iColumn.getName()));
                break;
            case Types.BOOLEAN:
            case Types.BIT:
                iColumn.setValue(resultSet.getBoolean(iColumn.getName()));
                break;
            case Types.DOUBLE:
                iColumn.setValue(resultSet.getDouble(iColumn.getName()));
                break;
            case Types.NUMERIC:
                iColumn.setValue(resultSet.getBigDecimal(iColumn.getName()));
                break;
            case Types.TINYINT:
                iColumn.setValue(resultSet.getByte(iColumn.getName()));
                break;
            case Types.SMALLINT:
                iColumn.setValue(resultSet.getShort(iColumn.getName()));
                break;
            case Types.BIGINT:
                iColumn.setValue(resultSet.getLong(iColumn.getName()));
                break;
            case Types.VARBINARY:
            case Types.BINARY:
                iColumn.setValue(resultSet.getString(iColumn.getName()));
                break;
            case Types.DATE:
                iColumn.setValue(resultSet.getDate(iColumn.getName()));
                break;
            case Types.TIMESTAMP:
                iColumn.setValue(resultSet.getTimestamp(iColumn.getName()));
                break;
            case Types.TIME:
                iColumn.setValue(resultSet.getTime(iColumn.getName()));
                break;
            case Types.CLOB:
                iColumn.setValue(resultSet.getClob(iColumn.getName()));
                break;
            case Types.BLOB:
                iColumn.setValue(resultSet.getBlob(iColumn.getName()));
                break;
            case Types.ARRAY:
                iColumn.setValue(resultSet.getArray(iColumn.getName()));
                break;
            case Types.REF:
                iColumn.setValue(resultSet.getRef(iColumn.getName()));
                break;
            case Types.JAVA_OBJECT:
            default:
                iColumn.setValue(resultSet.getObject(iColumn.getName()));
        }
        return iColumn;
    }

    /**
     * 用于从实际的sql中获取对应列属性的回调接口
     */
    @FunctionalInterface
    public interface ColumnExecutor {

        IColumn[] execute() throws SQLException;

    }
}
