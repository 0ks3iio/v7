package net.zdsoft.bigdata.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统数据库配置表
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 上午11:06
 */
@Entity
@Table(name = "bg_sys_database")
public class Database extends AbstractDatabase<String> {

    public static final String DEFAULT_CHASET_ENCODING = "UTF-8";

    private static final long serialVersionUID = -58886423225724582L;

    private String type;
    private String domain;
    private int port;
    private String dbName;
    @Column(name = "user_name")
    private String username;
    private String password;
    private String thumbnail;
    private String characterEncoding;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    @Override
    public String fetchCacheEntitName() {
        return "dataSource";
    }
}
