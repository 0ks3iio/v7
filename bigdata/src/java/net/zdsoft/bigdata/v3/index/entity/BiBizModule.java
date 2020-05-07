package net.zdsoft.bigdata.v3.index.entity;

import java.io.Serializable;

public class BiBizModule implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
