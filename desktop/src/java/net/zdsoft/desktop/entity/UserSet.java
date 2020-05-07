package net.zdsoft.desktop.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by shenke on 2017/4/6.
 */
@Table(name = "desktop_user_set")
@Entity
public class UserSet extends BaseEntity<String> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**  */
    public static final String LAYOUT_TWO2ONE = "two2one";
    public static final String LAYOUT_DEFAULT = "default";

    private String userId;
    private String layout;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    @Override
    public String fetchCacheEntitName() {
        return "userSet";
    }
}
