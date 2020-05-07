package net.zdsoft.bigdata.datax.entity.ftp;

import net.zdsoft.bigdata.datax.entity.JobContentColumn;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;

import java.util.List;

/**
 * Created by wangdongdong on 2019/6/17 20:52.
 */
public class FtpJobContentReaderParameter extends JobContentParameter {


    private String protocol;

    private String host;

    private Integer port;

    private List<String> path;

    private List<JobContentColumn> column;

    private String encoding;

    private String fieldDelimiter;

    private String compress;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<JobContentColumn> getColumn() {
        return column;
    }

    public void setColumn(List<JobContentColumn> column) {
        this.column = column;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
