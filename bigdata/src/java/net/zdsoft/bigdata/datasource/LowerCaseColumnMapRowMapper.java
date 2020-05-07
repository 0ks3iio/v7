package net.zdsoft.bigdata.datasource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;

/**
 * @author shenke
 * @since 2018/11/28 下午6:34
 */
final class LowerCaseColumnMapRowMapper extends ColumnMapRowMapper {

    @Override
    protected String getColumnKey(String columnName) {
        String key = null;
        if ((key=super.getColumnKey(columnName)) == null) {
            return "未知数据";
        } else {
            return StringUtils.lowerCase(key);
        }
    }
}
