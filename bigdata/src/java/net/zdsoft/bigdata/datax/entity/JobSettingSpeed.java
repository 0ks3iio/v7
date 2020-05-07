package net.zdsoft.bigdata.datax.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wangdongdong on 2019/4/28 17:55.
 */
public class JobSettingSpeed {

    private String channel;

    @JSONField(name = "byte")
    private String speedByte;

    public String getChannel() {
        return channel;
    }

    public JobSettingSpeed setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getSpeedByte() {
        return speedByte;
    }

    public JobSettingSpeed setSpeedByte(String speedByte) {
        this.speedByte = speedByte;
        return this;
    }
}
