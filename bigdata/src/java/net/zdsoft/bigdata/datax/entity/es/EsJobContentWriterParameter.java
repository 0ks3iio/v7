package net.zdsoft.bigdata.datax.entity.es;

import net.zdsoft.bigdata.datax.entity.JobContentParameter;

import java.util.List;

/**
 * Created by wangdongdong on 2019/6/18 10:34.
 */
public class EsJobContentWriterParameter extends JobContentParameter {


    private String endpoint;

    private String accessId;

    private String accessKey;

    private String index;

    private String type;

    private Boolean cleanup;

    private Long batchSize;

    private Long trySize;

    private Long timeout;

    private Boolean discovery;

    private Boolean compression;

    private Boolean multiThread;

    private Boolean ignoreWriteError;

    private Boolean ignoreParseError;

    private String alias;

    private String aliasMode;

    private String setting;

    private String splitter;

    private List<EsColumn> column;

    private Boolean dynamic;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getCleanup() {
        return cleanup;
    }

    public void setCleanup(Boolean cleanup) {
        this.cleanup = cleanup;
    }

    public Long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Long batchSize) {
        this.batchSize = batchSize;
    }

    public Long getTrySize() {
        return trySize;
    }

    public void setTrySize(Long trySize) {
        this.trySize = trySize;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Boolean getDiscovery() {
        return discovery;
    }

    public void setDiscovery(Boolean discovery) {
        this.discovery = discovery;
    }

    public Boolean getCompression() {
        return compression;
    }

    public void setCompression(Boolean compression) {
        this.compression = compression;
    }

    public Boolean getMultiThread() {
        return multiThread;
    }

    public void setMultiThread(Boolean multiThread) {
        this.multiThread = multiThread;
    }

    public Boolean getIgnoreWriteError() {
        return ignoreWriteError;
    }

    public void setIgnoreWriteError(Boolean ignoreWriteError) {
        this.ignoreWriteError = ignoreWriteError;
    }

    public Boolean getIgnoreParseError() {
        return ignoreParseError;
    }

    public void setIgnoreParseError(Boolean ignoreParseError) {
        this.ignoreParseError = ignoreParseError;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAliasMode() {
        return aliasMode;
    }

    public void setAliasMode(String aliasMode) {
        this.aliasMode = aliasMode;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getSplitter() {
        return splitter;
    }

    public void setSplitter(String splitter) {
        this.splitter = splitter;
    }

    public List<EsColumn> getColumn() {
        return column;
    }

    public void setColumn(List<EsColumn> column) {
        this.column = column;
    }

    public Boolean getDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }
}
