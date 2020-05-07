package net.zdsoft.api.openapi.remote.openapi.dto;

/**
 * @author shenke
 * @since 2019/5/23 下午5:05
 */
public final class ApplyIpInput {

    private String id;
    private String ips;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }
}
