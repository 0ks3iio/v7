package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 大屏分组，可分组合并到一张大屏上显示
 * @author shenke
 * @since 2018/11/14 上午9:18
 */
@Entity
@Table(name = "bg_screen_group")
public class ScreenGroup extends BaseEntity<String> {

    /**
     * 以 ',' 分隔
     */
    private String screenIds;
    private String name;
    private Integer interval;
    private String createUserId;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getScreenIds() {
        return screenIds;
    }

    public void setScreenIds(String screenIds) {
        this.screenIds = screenIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
