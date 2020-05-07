package net.zdsoft.desktop.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;


import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017-5-9下午2:39:07
 */
@Entity
@Table(name = "desktop_calendar")
public class DesktopCalendar extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_IS_DELETED = "0";

    @Override
    public String fetchCacheEntitName() {
        return "desktopCalendar";
    }

    private String userId;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginTime;    //开始时间

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;   //结束时间

    private String content; //内容

    private String color;   //颜色

    private String isDeleted;  //软删除

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;  //创建时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;  //修改时间

    private String type;   //类型   1 -- 本地新增, 2 -- 6.0或5.0推送的

    private String isAllday; //是否全天  1 --全天 ,0 --不是
    @Transient
    private String showTime;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifyTime;            //新增提醒时间 2017.10.17 by shenke

    private Integer systemNotifyType; //使用系统提示时间 提前5、10、15、30、60、120、180分钟

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getIsAllday() {
        return isAllday;
    }

    public void setIsAllday(String isAllday) {
        this.isAllday = isAllday;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Integer getSystemNotifyType() {
        return systemNotifyType;
    }

    public void setSystemNotifyType(Integer systemNotifyType) {
        this.systemNotifyType = systemNotifyType;
    }
}
