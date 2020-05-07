package net.zdsoft.bigdata.datasource.jdbc;

import net.zdsoft.bigdata.datasource.Adapter;
import net.zdsoft.bigdata.datasource.DataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author shenke
 * @since 2018/11/26 下午3:06
 */
public final class JdbcDatabaseAdapter extends Adapter {

    private static Map<JdbcDatabaseAdapter, JdbcDatabaseAdapter> identityCache = new HashMap<>();
    private static final Object lock = new Object();

    private DataType dataType;
    private String domain;
    private Integer port;
    private String dbName;
    private String username;
    private String password;
    private String characterEncoding;

    private JdbcDatabaseAdapter() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JdbcDatabaseAdapter)) return false;
        JdbcDatabaseAdapter that = (JdbcDatabaseAdapter) o;
        return Objects.equals(dataType, that.dataType) &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(port, that.port) &&
                Objects.equals(dbName, that.dbName) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(characterEncoding, that.characterEncoding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, domain, port, dbName, username, password, characterEncoding);
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

    public String getDbName() {
        return dbName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public static final class JdbcDatabaseAdapterBuilder {
        private DataType type;
        private String domain;
        private Integer port;
        private String dbName;
        private String username;
        private String password;
        private String characterEncoding;

        private JdbcDatabaseAdapterBuilder() {
        }

        public static JdbcDatabaseAdapterBuilder builder() {
            return new JdbcDatabaseAdapterBuilder();
        }

        public JdbcDatabaseAdapterBuilder type(DataType type) {
            this.type = type;
            return this;
        }

        public JdbcDatabaseAdapterBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public JdbcDatabaseAdapterBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public JdbcDatabaseAdapterBuilder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public JdbcDatabaseAdapterBuilder username(String username) {
            this.username = username;
            return this;
        }

        public JdbcDatabaseAdapterBuilder password(String password) {
            this.password = password;
            return this;
        }

        public JdbcDatabaseAdapterBuilder characterEncoding(String characterEncoding) {
            this.characterEncoding = characterEncoding;
            return this;
        }

        public JdbcDatabaseAdapter build() {
            JdbcDatabaseAdapter jdbcDatabaseAdapter = new JdbcDatabaseAdapter();
            jdbcDatabaseAdapter.dataType = type;
            jdbcDatabaseAdapter.domain = domain;
            jdbcDatabaseAdapter.port = port;
            jdbcDatabaseAdapter.dbName = dbName;
            jdbcDatabaseAdapter.username = username;
            jdbcDatabaseAdapter.password = password;
            jdbcDatabaseAdapter.characterEncoding = characterEncoding;
            return jdbcDatabaseAdapter;
        }

        JdbcDatabaseAdapter buildWithCache(JdbcDatabaseAdapter jdbcDatabaseAdapter) {
            //采用类似分段锁的形式
            //针对每个database加锁 （所以需要保证JdbcDatabaseKey的唯一性）
            synchronized (lock) {
                return identityCache.computeIfAbsent(jdbcDatabaseAdapter, k -> k);
            }
        }
    }
}
