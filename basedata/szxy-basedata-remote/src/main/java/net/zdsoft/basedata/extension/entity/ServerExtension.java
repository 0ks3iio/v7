package net.zdsoft.basedata.extension.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/1/21 上午9:52
 */
@Entity
@Table(name = "base_server_extension")
public class ServerExtension extends BaseEntity<String> {

    /**
     * 试用
     */
    public static final Integer NATURE_TRIAL = 0;

    /**
     * 正式
     */
    public static final Integer NATURE_OFFICIAL = 1;

    /**
     * 正常
     */
    public static final Integer STATE_NORMAL = 0;
    /**
     * 停用
     */
    public static final Integer STATE_DISABLE = 1;
    /**
     * 到期停用
     */
    public static final Integer STATE_EXPIRE = 2;

    /**
     * 关联单位ID
     */
    private String unitId;
    /**
     * 关联系统的Code
     */
    private String serverCode;
    /**
     * 系统过期时间 （null，跟随单位时间）
     */
    private Date expireTime;
    /**
     * 系统使用性质 （试用、正式）
     */
    private Integer usingNature;
    /**
     * 系统使用状态 （停用、到期停用、正常）
     */
    private Integer usingState;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getUsingNature() {
        return usingNature;
    }

    public void setUsingNature(Integer usingNature) {
        this.usingNature = usingNature;
    }

    public Integer getUsingState() {
        return usingState;
    }

    public void setUsingState(Integer usingState) {
        this.usingState = usingState;
    }
}
