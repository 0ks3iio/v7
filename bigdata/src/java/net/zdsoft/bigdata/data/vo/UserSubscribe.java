/**
 * FileName: UserSubscribe.java
 * Author:   shenke
 * Date:     2018/5/18 下午2:09
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

/**
 * @author shenke
 * @since 2018/5/18 下午2:09
 */
public class UserSubscribe {

    private String userId;
    private String realName;
    private Boolean subscribe;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }
}
