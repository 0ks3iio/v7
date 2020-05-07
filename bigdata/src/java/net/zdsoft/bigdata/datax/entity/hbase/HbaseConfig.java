package net.zdsoft.bigdata.datax.entity.hbase;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wangdongdong on 2019/5/17 18:10.
 */
public class HbaseConfig {

    @JSONField(name = "hbase.zookeeper.quorum")
    private String zkUrl;

    public String getZkUrl() {
        return zkUrl;
    }

    public HbaseConfig setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
        return this;
    }
}
