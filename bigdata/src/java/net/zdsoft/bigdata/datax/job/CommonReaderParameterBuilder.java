package net.zdsoft.bigdata.datax.job;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentColumn;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 17:43.
 */
public abstract class CommonReaderParameterBuilder {

    /**
     * reader参数
     * @param paramMap
     * @param database
     * @return
     * @throws BigDataBusinessException
     */
    public abstract JobContentParameter getJobContentReaderParameter(Map<String, String> paramMap, AbstractDatabase database) throws BigDataBusinessException;

    protected List<JobContentColumn> getJobContentColumns(Map<String, String> paramMap) {
        List<JobContentColumn> column = Lists.newArrayList();
        String readerColumn = paramMap.get(DataxJobParamEnum.READER_COLUMN.getCode());
        String[] columns = readerColumn.split(",");
        int i =0;
        for (String c : columns) {
            JobContentColumn tfc = new JobContentColumn();
            tfc.setIndex(i++);
            if (c.contains("date|")) {
                String[] cc = StringUtils.splitByWholeSeparator(c, "|");
                tfc.setType(cc[0]);
                tfc.setFormat(cc[1]);
            } else {
                tfc.setType(c);
            }
            column.add(tfc);
        }
        return column;
    }
}
