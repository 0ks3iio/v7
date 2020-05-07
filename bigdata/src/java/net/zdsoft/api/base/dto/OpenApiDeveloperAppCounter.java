package net.zdsoft.api.base.dto;

/**
 * @author shenke
 * @since 2019/5/22 上午10:12
 */
public final class OpenApiDeveloperAppCounter {

    private String developerId;
    private long appNumber;

    public OpenApiDeveloperAppCounter() {
    }

    public OpenApiDeveloperAppCounter(String developerId, long appNumber) {
        this.developerId = developerId;
        this.appNumber = appNumber;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public long getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(int appNumber) {
        this.appNumber = appNumber;
    }
}
