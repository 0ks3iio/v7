package net.zdsoft.bigdata.datax.entity.txtfile;

import net.zdsoft.bigdata.datax.entity.JobContentColumn;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class TxtFilleJobContentReaderParameter extends JobContentParameter {

    private List<String> path;

    private String encoding;

    private List<JobContentColumn> column;

    private String fieldDelimiter;

    private String compress;

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public List<JobContentColumn> getColumn() {
        return column;
    }

    public void setColumn(List<JobContentColumn> column) {
        this.column = column;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }
}
