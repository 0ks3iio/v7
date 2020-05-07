package net.zdsoft.bigdata.datasource.redis;

import net.zdsoft.bigdata.datasource.Adapter;
import net.zdsoft.bigdata.datasource.DataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author shenke
 * @since 2018/11/27 上午10:51
 */
public final class RedisDatabaseAdapter extends Adapter {

    private static Map<RedisDatabaseAdapter, RedisDatabaseAdapter> identityCache = new HashMap<>();
    private static final Object lock = new Object();

    private String domain;
    private Integer port;
    private Integer dbNumber;
    private String password;
    private DataType dataType;

    private RedisDatabaseAdapter() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RedisDatabaseAdapter)) return false;
        RedisDatabaseAdapter that = (RedisDatabaseAdapter) o;
        return Objects.equals(domain, that.domain) &&
                Objects.equals(port, that.port) &&
                Objects.equals(dbNumber, that.dbNumber) &&
                Objects.equals(password, that.password) &&
                Objects.equals(dataType, that.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, port, dbNumber, password, dataType);
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    public String getDomain() {
        return domain;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getDbNumber() {
        return dbNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setDbNumber(Integer dbNumber) {
        this.dbNumber = dbNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public static final class RedisDatabaseKeyBuilder {
        private String domain;
        private Integer port;
        private Integer dbNumber;
        private String password;
        private DataType dataType;

        private RedisDatabaseKeyBuilder() {
        }

        public static RedisDatabaseKeyBuilder builder() {
            return new RedisDatabaseKeyBuilder();
        }

        public RedisDatabaseKeyBuilder withDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public RedisDatabaseKeyBuilder withPort(Integer port) {
            this.port = port;
            return this;
        }

        public RedisDatabaseKeyBuilder withDbNumber(Integer dbNumber) {
            this.dbNumber = dbNumber;
            return this;
        }

        public RedisDatabaseKeyBuilder withPassword(String password) {
            this.password = password;
            return this;
        }
        public RedisDatabaseKeyBuilder dataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public RedisDatabaseAdapter build() {
            RedisDatabaseAdapter redisDatabaseAdapter = new RedisDatabaseAdapter();
            redisDatabaseAdapter.dbNumber = this.dbNumber;
            redisDatabaseAdapter.domain = this.domain;
            redisDatabaseAdapter.port = this.port;
            redisDatabaseAdapter.password = this.password;
            redisDatabaseAdapter.dataType = this.dataType;
            return redisDatabaseAdapter;
        }

        RedisDatabaseAdapter buildSame(RedisDatabaseAdapter databaseAdapter) {
            synchronized (lock) {
                return identityCache.computeIfAbsent(databaseAdapter, k->k);
            }
        }
    }
}
