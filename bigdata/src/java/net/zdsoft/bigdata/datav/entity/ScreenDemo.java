package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 前台大屏经典案例实体类
 * @author yangkj
 * @since 2019/6/19 17:22
 */
@Entity
@Table(name = "bg_screen_demo")
public class ScreenDemo extends BaseEntity<String> {

    private String name;
    /**
     * 实际地址
     */
    private String url;
    /**
     * 缩略图
     */
    private String thumbnail;
    private String remark;
    private Integer status;
    private Integer orderId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
