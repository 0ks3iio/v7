package net.zdsoft.api.openapi.remote.openapi.vo;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/5/22 上午10:09
 */
public final class OpenApiDeveloperVo {

    private String id;
    private String realName;
    private int appNumber;
    private Date creationTime;
    private boolean hasApply;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(int appNumber) {
        this.appNumber = appNumber;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isHasApply() {
        return hasApply;
    }

    public void setHasApply(boolean hasApply) {
        this.hasApply = hasApply;
    }
}
