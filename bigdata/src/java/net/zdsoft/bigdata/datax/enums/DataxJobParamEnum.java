package net.zdsoft.bigdata.datax.enums;

/**
 * Created by wangdongdong on 2019/5/13 15:11.
 */
public enum DataxJobParamEnum {


    SOURCE_ID("源数据ID", "sourceId"),

    SOURCE_TYPE("源数据类型", "sourceType"),

    TARGET_ID("目标数据ID", "targetId"),

    TARGET_TYPE("目标数据类型", "targetType"),

    OLD_SOURCE_ID("历史源数据ID", "oldSourceId"),

    OLD_TARGET_ID("历史目标数据ID", "oldTargetId"),

    APP_ID("APP ID", "appId"),

    READER_COLUMN("读字段", "readerColumn"),

    WRITER_COLUMN("写字段", "writerColumn"),

    QUERY_SQL("查询sql", "querySql"),

    CHANNEL("通道数", "channel"),

    BYTE("记录流速度", "byte"),

    RECORD("字节流", "record"),

    ERROR_RECORD("脏数据限制", "errorRecord"),

    ERROR_PERCENTAGE("脏数据所占比例上限", "errorPercentage"),

    READER_TABLE("源数据表名", "readerTable"),

    READER_SPLIT_PK("切分主键", "readerSplitPk"),

    READER_WHERE("筛选条件", "readerWhere"),

    READER_FETCH_SIZE("每次批量数据获取条数", "readerFetchSize"),

    WRITE_SESSION("session", "writerSession"),

    PRE_SQL("写入数据前sql", "preSql"),

    POST_SQL("写入数据后sql", "postSql"),

    BATCH_SIZE("批量提交的记录数", "batchSize"),

    WRITE_MODE("写模式", "writeMode"),

    NULL_MODE("null值处理", "nullMode"),

    WAL_FLAG("是否写wal日志", "walFlag"),

    PHOENIX_TABLE("phoenix表名", "phoenixTable"),

    HBASE_TABLE("hbase表名", "hbaseTable"),

    HBASE_READ_MODE("hbase读取模式", "hbaseReadMode"),

    HBASE_MAXVERSION("hbase读取版本数", "hbaseMaxVersion"),

    KAFKA_TOPIC("kafka主题", "kafkaTopic"),

    KAFKA_SERVERS("kafka集群地址", "kafkaServers"),

    KAFKA_CREATETOPIC("kafka自动创建主题", "kafkaCreateTopic"),

    KAFKA_PARTITION("kafka主题分区数", "kafkaPartition"),

    KAFKA_REPLICATION_FACTOR("kafka主题备份数", "kafkaReplicationFactor"),

    TEXT_FILE_PATH("本地文件路径", "textfilePath"),

    FIELD_DELIMITER("字段分隔符", "fieldDelimiter"),

    TEXT_FILE_COMPRESS("文本压缩类型", "textfileCompress"),

    FTP_PROTOCOL("ftp服务器协议", "ftpProtocol"),

    FTP_HOST("ftp服务器地址", "ftpHost"),

    FTP_PORT("ftp服务器端口", "ftpPort"),

    FTP_USERNAME("用户名", "ftpUsername"),

    FTP_PASSWORD("密码", "ftpPassword"),

    FTP_PATH("ftp路径", "ftpPath"),

    FTP_COMPRESS("ftp压缩类型", "ftpCompress"),

    ES_ACCESS_ID("esAccessId", "esAccessId"),

    ES_ACCESS_KEY("esAccessKey", "esAccessKey"),

    ES_INDEX("esIndex", "esIndex"),

    ES_TYPE("esType", "esType"),

    ES_CLEANUP("是否删除原表", "esCleanup"),

    ES_TRY_SIZE("失败后重试次数", "esTrySize"),

    ES_TIMEOUT("客户端超时时间", "esTimeout"),

    ES_DISCOVERY("是否启用节点发现", "esDiscovery"),

    ES_COMPRESSION("是否开启压缩", "esCompression"),

    ES_MULTI_THREAD("是否多线程", "esMultiThread"),

    ES_IGNORE_WRITE_ERROR("忽略写入错误", "esIgnoreWriteError"),

    ES_IGNORE_PARSE_ERROR("忽略解析数据格式错误", "esIgnoreParseError"),

    ES_ALIAS("写入别名", "esAlias"),

    ES_ALIAS_MODE("增加别名模式", "esAliasMode"),

    ES_SETTINGS("增加别名模式", "esSettings"),

    ES_SPLITTER("分隔符", "esSplitter"),

    ES_DYNAMIC("es自动mappings", "esDynamic");

    private String name;

    private String code;

    DataxJobParamEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
