package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class JobContentWriterParameter extends JobContentParameter {

    /**
     * 控制写入数据到目标表采用 insert into 或者 replace into 或者 ON DUPLICATE KEY UPDATE 语句
     */
    private String writeMode;

    /**
     * DataX在获取Mysql连接时，执行session指定的SQL语句，修改当前connection session属性
     */
    private List<String> session;

    /**
     * 写入数据到目的表前，会先执行这里的标准语句。如果 Sql 中有你需要操作到的表名称，
     * 请使用 @table 表示，这样在实际执行 Sql 语句时，会对变量按照实际表名称进行替换。
     * 比如你的任务是要写入到目的端的100个同构分表
     * (表名称为:datax_00,datax01, ... datax_98,datax_99)，
     * 并且你希望导入数据前，先对表中数据进行删除操作，
     * 那么你可以这样配置："preSql":["delete from 表名"]，
     * 效果是：在执行到每个表写入数据前，会先执行对应的 delete from 对应表名称
     */
    private List<String> preSql;

    /**
     * 写入数据到目的表后，会执行这里的标准语句。（原理同 preSql ）
     */
    private List<String> postSql;

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
     * 一次性批量提交的记录数大小，该值可以极大减少DataX与Mysql的网络交互次数，并提升整体吞吐量。但是该值设置过大可能会造成DataX运行进程OOM情况
     */
    private Long batchSize;

    public String getWriteMode() {
        return writeMode;
    }

    public JobContentWriterParameter setWriteMode(String writeMode) {
        this.writeMode = writeMode;
        return this;
    }

    public List<String> getSession() {
        return session;
    }

    public JobContentWriterParameter setSession(List<String> session) {
        this.session = session;
        return this;
    }

    public List<String> getPreSql() {
        return preSql;
    }

    public JobContentWriterParameter setPreSql(List<String> preSql) {
        this.preSql = preSql;
        return this;
    }

    public List<String> getPostSql() {
        return postSql;
    }

    public JobContentWriterParameter setPostSql(List<String> postSql) {
        this.postSql = postSql;
        return this;
    }

    public List<String> getColumn() {
        return column;
    }

    public JobContentWriterParameter setColumn(List<String> column) {
        this.column = column;
        return this;
    }

    public Long getBatchSize() {
        return batchSize;
    }

    public JobContentWriterParameter setBatchSize(Long batchSize) {
        this.batchSize = batchSize;
        return this;
    }
}
