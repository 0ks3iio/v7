package net.zdsoft.system.entity.server;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分类影射表
 * @author shenke
 * @since 2019/3/13 上午10:58
 */
@Entity
@Table(name = "base_server_classify_relation")
public class ServerClassifyRelation extends BaseEntity<String> {

    private String classifyId;
    private String serverCode;

    @Override
    public String fetchCacheEntitName() {
        return "serverClassifyRelation";
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }
}
