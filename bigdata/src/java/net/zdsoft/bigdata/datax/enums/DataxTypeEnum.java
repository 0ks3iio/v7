package net.zdsoft.bigdata.datax.enums;

/**
 * Created by wangdongdong on 2019/6/17 10:00.
 */
public enum DataxTypeEnum {

    MYSQL("mysql", "mysqlreader", "mysqlwriter"),

    ORACLE("oracle", "oraclereader", "oraclewriter"),

    SQLSERVER("sqlserver", "sqlserverreader", "sqlserverwriter"),

    POSTGRESQL("postgresql", "postgresqlreader", "postgresqlwriter"),

    HBASE11("hbase", "hbase11xreader", "hbase11xwriter"),

    PHOENIX4("phoenix", "hbase11xsqlreader", "hbase11xsqlwriter"),

    MONGODB("mongodb", "mongodbreader", "mongodbwriter"),

    HIVE("hive", "hdfsreader", "hdfswriter"),

    TEXTFILE("textfile", "txtfilereader", "txtfilewriter"),

    FTP("ftp", "ftpreader", "ftpwriter"),

    HDFS("hdfs", "hdfsreader", "hdfswriter"),

    KAFKA("kafka", "kafkareader", "kafkawriter"),

    ELASTICSEARCH("es", "", "elasticsearchwriter");

    private String code;

    private String rValue;

    private String wValue;

    DataxTypeEnum(String code, String rValue, String wValue) {
        this.code = code;
        this.rValue = rValue;
        this.wValue = wValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getrValue() {
        return rValue;
    }

    public void setrValue(String rValue) {
        this.rValue = rValue;
    }

    public String getwValue() {
        return wValue;
    }

    public void setwValue(String wValue) {
        this.wValue = wValue;
    }

    public static DataxTypeEnum valueOfCode(String code) {
        for (DataxTypeEnum e : DataxTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}


