package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;

public class Account implements Serializable {

    private static final long serialVersionUID = -6118619532731523463L;
    private String loginName;
    private String password;
    private String smsServerUrl;
    private String ticket;

    public String getSmsServerUrl() {
        return smsServerUrl;
    }

    public void setSmsServerUrl(String smsServerUrl) {
        this.smsServerUrl = smsServerUrl;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

}
