package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class JobContentReaderParameter extends JobContentParameter {


    /**
     * 筛选条件，MysqlReader根据指定的column、table、where条件拼接SQL，
     * 并根据这个SQL进行数据抽取。在实际业务场景中，往往会选择当天的数据进行同步，
     * 可以将where条件指定为gmt_create > $bizdate 。注意：不可以将where条件指定为limit 10，limit不是SQL的合法where子句
     */
    private String where;

    /**
     * OracleReader进行数据抽取时，如果指定splitPk，表示用户希望使用splitPk代表的字段进行数据分片，DataX因此会启动并发任务进行数据同步，这样可以大大提供数据同步的效能。
     * <p>
     * 推荐splitPk用户使用表主键，因为表主键通常情况下比较均匀，因此切分出来的分片也不容易出现数据热点。
     * <p>
     * 目前splitPk仅支持整形、字符串型数据切分，不支持浮点、日期等其他类型。如果用户指定其他非支持类型，OracleReader将报错！
     * <p>
     * splitPk如果不填写，将视作用户不对单表进行切分，OracleReader使用单通道同步全量数据。
     */
    private String splitPk;

    /**
     * 所配置的表中需要同步的列名集合，使用JSON的数组描述字段信息。用户使用*代表默认使用所有列配置，例如['*']。
     * <p>
     * 支持列裁剪，即列可以挑选部分列进行导出。
     * <p>
     * 支持列换序，即列可以不按照表schema信息进行导出。
     * <p>
     * 支持常量配置，用户需要按照JSON格式: ["id", "table", "1", "'bazhen.csy'", "null", "to_char(a + 1)", "2.3" , "true"] id为普通列名，`table`为包含保留在的列名，1为整形数字常量，'bazhen.csy'为字符串常量，null为空指针，to_char(a + 1)为表达式，2.3为浮点数，true为布尔值。
     * <p>
     * Column必须显示填写，不允许为空！
     */
    private List<String> column;

    /**
     * 该配置项定义了插件和数据库服务器端每次批量数据获取条数，该值决定了DataX和服务器端的网络交互次数，能够较大的提升数据抽取性能。
     */
    private Long fetchSize;

    public String getSplitPk() {
        return splitPk;
    }

    public JobContentReaderParameter setSplitPk(String splitPk) {
        this.splitPk = splitPk;
        return this;
    }

    public String getWhere() {
        return where;
    }

    public JobContentReaderParameter setWhere(String where) {
        this.where = where;
        return this;
    }

    public List<String> getColumn() {
        return column;
    }

    public JobContentReaderParameter setColumn(List<String> column) {
        this.column = column;
        return this;
    }

    public Long getFetchSize() {
        return fetchSize;
    }

    public JobContentReaderParameter setFetchSize(Long fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }
}
