package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author shenke
 * @since 2018/10/24 下午2:02
 */
@Entity
@Table(name = "bg_screen_library")
public class ScreenLibrary extends BaseEntity<String> {

    private String name;
    /**
     * 小缩略图
     */
    private String iconPath;
    /**
     * 大缩略图
     */
    private String fullIconPath;

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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getFullIconPath() {
        return fullIconPath;
    }

    public void setFullIconPath(String fullIconPath) {
        this.fullIconPath = fullIconPath;
    }
}
