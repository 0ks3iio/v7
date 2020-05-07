package net.zdsoft.bigdata.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午5:34
 */
@Entity
@Table(name = "bg_sys_api")
public class Api extends AbstractDatabase<String> {

	private static final long serialVersionUID = 6205632241093350609L;
	
	private String url;

	private String paramDescription;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }

    @Override
    public String fetchCacheEntitName() {
        return "api";
    }

    @Override
    public String toString() {
        return "Api{" +
                "url='" + url + '\'' +
                ", unitId='" + unitId + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", creationTime=" + creationTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
