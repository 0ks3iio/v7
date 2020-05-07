package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 记录所有交互参数的默认值
 * @author shenke
 * @since 2018/10/25 下午6:37
 */
@Table(name = "bg_screen_interaction")
@Entity
public class ScreenInteractionElement extends BaseEntity<String> {

    private String screenId;
    private String bindKey;
    private String defaultValue;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getBindKey() {
        return bindKey;
    }

    public void setBindKey(String bindKey) {
        this.bindKey = bindKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
