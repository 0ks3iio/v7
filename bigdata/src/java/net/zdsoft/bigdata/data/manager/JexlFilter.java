/**
 * FileName: JexlFilter.java
 * Author:   shenke
 * Date:     2018/5/24 上午11:36
 * Descriptor:
 */
package net.zdsoft.bigdata.data.manager;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.api.Filter;
import net.zdsoft.bigdata.data.manager.api.Invocation;
import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.bigdata.data.utils.JexlUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenke
 * @since 2018/5/24 上午11:36
 */
public class JexlFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(JexlFilter.class);

    @Override
    public void doFilter(Invocation invocation) {
        if (!DataSourceType.STATIC.equals(invocation.getDataSourceType())) {
            String sql = invocation.getQueryStatement();
            if (StringUtils.isBlank(sql)) {
                return;
            }
            String wrapped = JexlUtils.evaluate(sql, JexlContextHolder.getJexlContext());
            wrapped = wrapped.replaceAll("\n", " ");
            invocation.setQueryStatement(wrapped);
            if (logger.isDebugEnabled()) {
                logger.debug("old queryStatement: [{}] \n new queryStatement: [{}]", sql, wrapped);
            }
        }
    }

}
