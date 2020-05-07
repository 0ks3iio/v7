package net.zdsoft.bigdata.datasource.jdbc;

import java.util.Optional;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.entity.Database;

import static net.zdsoft.bigdata.data.DatabaseType.*;

/**
 * 数据源链接url构建器
 *
 * @author shenke
 * @since 2018/11/26 下午2:41
 */
public final class JdbcBuilder {

	static String build(JdbcDatabaseAdapter database) {
		return buildNoCache(database);
	}

	static String buildNoCache(JdbcDatabaseAdapter database) {
		DatabaseType type = DatabaseType
				.parse(database.getDataType().getType());
		if (type == null) {
			throw new RuntimeException(String.format(
					"Illegal database type [%s]", database.getDataType()
							.getType()));
		}
		switch (type) {
		case MYSQL:
			return buildMySQLJdbcUrl(database);
		case ORACLE:
			return buildOracleJdbcUrl(database);
		case DB2:
			return buildDb2JdbcUrl(database);
		case PostgreSQL:
			return buildPostgreSQLJdbcUrl(database);
		case SQL_SERVER:
			return buildSqlServerJdbcUrl(database);
		case KYLIN:
			return buildKylinJdbcUrl(database);
		case IMPALA:
			return buildImpalaJdbcUrl(database);
		case HBASE:
			return buildHbaseJdbcUrl(database);
		case HADOOP_HIVE:
			return buildHiveJdbcUrl(database);
		default:
			throw new RuntimeException(String.format(
					"not support database type [%s]", database.getDataType()
							.getType()));
		}
	}

	private static String buildMySQLJdbcUrl(JdbcDatabaseAdapter database) {
		return "jdbc:"
				+ MYSQL.getName()
				+ "://"
				+ database.getDomain()
				+ ":"
				+ database.getPort()
				+ "/"
				+ database.getDbName()
				+ "?useUnicode=true&characterEncoding="
				+ Optional.ofNullable(database.getCharacterEncoding()).orElse(
						Database.DEFAULT_CHASET_ENCODING);
	}

	private static String buildOracleJdbcUrl(JdbcDatabaseAdapter database) {
		return "jdbc:" + ORACLE.getName() + ":@" + database.getDomain() + ":"
				+ database.getPort() + ":" + database.getDbName();
	}

	private static String buildDb2JdbcUrl(JdbcDatabaseAdapter database) {
		return "jdbc:" + DB2.getName() + "://" + database.getDomain() + ":"
				+ database.getPort() + "/" + database.getDbName();
	}

	private static String buildPostgreSQLJdbcUrl(JdbcDatabaseAdapter database) {
		return "jdbc:" + PostgreSQL.getName() + "://" + database.getDomain()
				+ ":" + database.getPort() + "/" + database.getDbName();
	}

	private static String buildSqlServerJdbcUrl(JdbcDatabaseAdapter database) {
		return "jdbc:" + SQL_SERVER.getName() + "://" + database.getDomain()
				+ ":" + database.getPort() + ";database="
				+ database.getDbName();
	}

	private static String buildKylinJdbcUrl(JdbcDatabaseAdapter databaseKey) {
		return "jdbc:" + KYLIN.getName() + "://" + databaseKey.getDomain()
				+ ":" + databaseKey.getPort() + "/" + databaseKey.getDbName();
	}

	private static String buildHiveJdbcUrl(JdbcDatabaseAdapter databaseKey) {
		return "jdbc:" + HADOOP_HIVE.getName() + "://"
				+ databaseKey.getDomain() + ":" + databaseKey.getPort() + "/"
				+ databaseKey.getDbName();
	}

	private static String buildImpalaJdbcUrl(JdbcDatabaseAdapter databaseKey) {
		return "jdbc:" + IMPALA.getName() + "://" + databaseKey.getDomain()
				+ ":" + databaseKey.getPort() + "/" + databaseKey.getDbName();
	}

	private static String buildHbaseJdbcUrl(JdbcDatabaseAdapter databaseKey) {
		return "jdbc:" + HBASE.getName() + ":" + databaseKey.getDomain()
				+ ":" + databaseKey.getPort();
	}
}
