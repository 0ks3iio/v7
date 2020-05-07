package net.zdsoft.adapter;

import com.fasterxml.jackson.annotation.JsonAlias;
/**
 * @author shenke
 * @date 2019/10/23 下午2:20
 */

public class Authorization {

    @JsonAlias("apUserId")
    private String credentials;


    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}