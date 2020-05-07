package net.zdsoft.bigdata.data.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdongdong on 2018/8/1 14:10.
 */
@Entity
@Table(name = "bg_sys_nosql_database")
public class NosqlDatabase extends AbstractDatabase<String> {

    private String type;

    private String userName;

    private String password;

    private String domain;

    private int port;

    private String nameSpace;

    private String dbName;

    private String thumbnail;

    private String connectMode;

    private String authWay;

    private String serverPrincipal;

    private String clientPrincipal;

    private String keytabFile;

    private String initSql;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(String connectMode) {
        this.connectMode = connectMode;
    }

    public String getAuthWay() {
        return authWay;
    }

    public void setAuthWay(String authWay) {
        this.authWay = authWay;
    }

    public String getServerPrincipal() {
        return serverPrincipal;
    }

    public void setServerPrincipal(String serverPrincipal) {
        this.serverPrincipal = serverPrincipal;
    }

    public String getClientPrincipal() {
        return clientPrincipal;
    }

    public void setClientPrincipal(String clientPrincipal) {
        this.clientPrincipal = clientPrincipal;
    }

    public String getKeytabFile() {
        return keytabFile;
    }

    public void setKeytabFile(String keytabFile) {
        this.keytabFile = keytabFile;
    }

    public String getInitSql() {
        return initSql;
    }

    public void setInitSql(String initSql) {
        this.initSql = initSql;
    }

    @Override
    public String fetchCacheEntitName() {
        return "nosqlDatabase";
    }
}
