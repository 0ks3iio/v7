package net.zdsoft.bigdata.data;

import net.zdsoft.bigdata.datasource.DataType;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午5:18
 */
public enum DatabaseType implements DataType {

	MYSQL {
		@Override
		public String getType() {
			return "01";
		}
		
		@Override
		public String getDbType() {
			return "mysql";
		}

		@Override
		public String getName() {
			return "mysql";
		}

		@Override
		public String getDriverName() {
			return "com.mysql.jdbc.Driver";
		}

		@Override
		public String getThumbnail() {
			return "mysql.png";
		}
	},

	SQL_SERVER {
		@Override
		public String getType() {
			return "02";
		}
		
		@Override
		public String getDbType() {
			return "sqlserver";
		}

		@Override
		public String getName() {
			return "sqlserver";
		}

		@Override
		public String getDriverName() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}

		@Override
		public String getThumbnail() {
			return "sqlserver.png";
		}
	},

	ORACLE {
		@Override
		public String getType() {
			return "03";
		}
		
		@Override
		public String getDbType() {
			return "oracle";
		}

		@Override
		public String getName() {
			return "oracle:thin";
		}

		@Override
		public String getDriverName() {
			return "oracle.jdbc.driver.OracleDriver";
		}

		@Override
		public String getThumbnail() {
			return "oracle.png";
		}
	},

	DB2 {
		@Override
		public String getType() {
			return "04";
		}
		
		@Override
		public String getDbType() {
			return "db2";
		}

		@Override
		public String getName() {
			return "db2";
		}

		@Override
		public String getDriverName() {
			return "com.ibm.db2.jcc.DB2Driver";
		}

		@Override
		public String getThumbnail() {
			return "db2.png";
		}
	},

	PostgreSQL {
		@Override
		public String getType() {
			return "05";
		}

		@Override
		public String getDbType() {
			return "postgresql";
		}
		
		@Override
		public String getName() {
			return "postgresql";
		}

		@Override
		public String getDriverName() {
			return "org.postgresql.Driver";
		}

		@Override
		public String getThumbnail() {
			return "postgresql.png";
		}
	},

	HADOOP_HIVE {
		@Override
		public String getType() {
			return "06";
		}
		
		@Override
		public String getDbType() {
			return "hive";
		}

		@Override
		public String getName() {
			return "hive2";
		}

		@Override
		public String getDriverName() {
			return "org.apache.hive.jdbc.HiveDriver";
		}

		@Override
		public String getThumbnail() {
			return "hive.png";
		}
	},

	KYLIN {
		@Override
		public String getType() {
			return "07";
		}
		
		@Override
		public String getDbType() {
			return "kylin";
		}

		@Override
		public String getName() {
			return "kylin";
		}

		@Override
		public String getDriverName() {
			return "org.apache.kylin.jdbc.Driver";
		}

		@Override
		public String getThumbnail() {
			return "kylin.png";
		}
	},

	SPARK {
		@Override
		public String getType() {
			return "08";
		}
		
		@Override
		public String getDbType() {
			return "spark";
		}

		@Override
		public String getName() {
			return "spark";
		}

		@Override
		public String getDriverName() {
			return "";
		}

		@Override
		public String getThumbnail() {
			return "spark.png";
		}
	},

	IMPALA {
		@Override
		public String getType() {
			return "09";
		}
		
		@Override
		public String getDbType() {
			return "impala";
		}

		@Override
		public String getName() {
			return "impala";
		}

		@Override
		public String getDriverName() {
			return "com.cloudera.impala.jdbc41.Driver";
		}

		@Override
		public String getThumbnail() {
			return "impala.png";
		}
	},

	CSV {
		@Override
		public String getType() {
			return "10";
		}
		
		@Override
		public String getDbType() {
			return "csv";
		}

		@Override
		public String getName() {
			return "csv";
		}

		@Override
		public String getDriverName() {
			return "";
		}

		@Override
		public String getThumbnail() {
			return "csv.png";
		}
	},

	EXCEL {
		@Override
		public String getType() {
			return "11";
		}
		
		@Override
		public String getDbType() {
			return "excel";
		}

		@Override
		public String getName() {
			return "excel";
		}

		@Override
		public String getDriverName() {
			return "";
		}

		@Override
		public String getThumbnail() {
			return "excel.png";
		}
	},

	REDIS {
		@Override
		public String getType() {
			return "12";
		}
		
		@Override
		public String getDbType() {
			return "redis";
		}

		@Override
		public String getName() {
			return "redis";
		}

		@Override
		public String getDriverName() {
			return "";
		}

		@Override
		public String getThumbnail() {
			return "redis.png";
		}
	},

	HBASE {
		@Override
		public String getType() {
			return "13";
		}

		@Override
		public String getDbType() {
			return "hbase";
		}

		@Override
		public String getName() {
			return "phoenix";
		}

		@Override
		public String getDriverName() {
			return "org.apache.phoenix.jdbc.PhoenixDriver";
		}

		@Override
		public String getThumbnail() {
			return "hbase.png";
		}
	};

	public abstract String getDbType();
	
	public abstract String getName();

	public abstract String getDriverName();

	public abstract String getThumbnail();

	public static DatabaseType parse(String type) {
		for (DatabaseType e : DatabaseType.values()) {
			if (e.getType().equals(type)) {
				return e;
			}
		}
		return null;
	}
}
