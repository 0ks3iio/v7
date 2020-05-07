package net.zdsoft.szxy.operation.servermanage.dto;


import java.util.Date;


public class AuthorizedSystem {
    private String  serverName;
    private Integer usingNature;
    private Integer usingState;
    private String id;
    private Date expireTime;
    private Long dayApart;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Long getDayApart() {
        return dayApart;
    }

    public void setDayApart(Long dayApart) {
        this.dayApart = dayApart;
    }
}
