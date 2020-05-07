package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/4/26 15:07.
 */
public class JobSettingErrorLimit {

    private String record;

    private String percentage;

    public String getRecord() {
        return record;
    }

    public JobSettingErrorLimit setRecord(String record) {
        this.record = record;
        return this;
    }

    public String getPercentage() {
        return percentage;
    }

    public JobSettingErrorLimit setPercentage(String percentage) {
        this.percentage = percentage;
        return this;
    }
}
