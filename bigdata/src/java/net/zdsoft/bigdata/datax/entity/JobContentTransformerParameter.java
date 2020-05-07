package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/5/9 9:37.
 */
public class JobContentTransformerParameter {

    private Integer columnIndex;

    private List<String> paras;

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public List<String> getParas() {
        return paras;
    }

    public void setParas(List<String> paras) {
        this.paras = paras;
    }
}
