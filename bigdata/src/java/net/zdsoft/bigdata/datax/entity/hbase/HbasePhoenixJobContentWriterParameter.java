package net.zdsoft.bigdata.datax.entity.hbase;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:18.
 */
public class HbasePhoenixJobContentWriterParameter extends HbaseJobContentWriterParameter {

    private String mode;

    private List<HbaseRowkeyColumn> rowkeyColumn;

    private List<HbaseColumn> column;

    private HbaseVersionColumn versionColumn;

    private String encoding;

    private boolean walFlag;

    public String getMode() {
        return mode;
    }

    public HbasePhoenixJobContentWriterParameter setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public List<HbaseRowkeyColumn> getRowkeyColumn() {
        return rowkeyColumn;
    }

    public HbasePhoenixJobContentWriterParameter setRowkeyColumn(List<HbaseRowkeyColumn> rowkeyColumn) {
        this.rowkeyColumn = rowkeyColumn;
        return this;
    }

    public List<HbaseColumn> getColumn() {
        return column;
    }

    public void setColumn(List<HbaseColumn> column) {
        this.column = column;
    }

    public HbaseVersionColumn getVersionColumn() {
        return versionColumn;
    }

    public HbasePhoenixJobContentWriterParameter setVersionColumn(HbaseVersionColumn versionColumn) {
        this.versionColumn = versionColumn;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public HbasePhoenixJobContentWriterParameter setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public boolean isWalFlag() {
        return walFlag;
    }

    public HbasePhoenixJobContentWriterParameter setWalFlag(boolean walFlag) {
        this.walFlag = walFlag;
        return this;
    }
}
