package net.zdsoft.dataimport.biz;

import java.util.Date;

/**
 * 导入记录
 * @author shenke
 * @since 2017.08.10
 */
public class ImportRecord {

     private String fileName;
     private Date creationTime;
     private String originFilename;
     private String cacheId;
     private int stateCode;

     private String stateMsg; //状态显示

     private long redisIndex;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getOriginFilename() {
        return originFilename;
    }

    public void setOriginFilename(String originFilename) {
        this.originFilename = originFilename;
    }

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

    public long getRedisIndex() {
        return redisIndex;
    }

    public void setRedisIndex(long redisIndex) {
        this.redisIndex = redisIndex;
    }
}
