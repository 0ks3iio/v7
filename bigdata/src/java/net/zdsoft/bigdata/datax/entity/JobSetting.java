package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/4/26 15:07.
 */
public class JobSetting {

    public JobSetting() {
    }

    public JobSetting(JobSettingSpeed speed) {
        this.speed = speed;
    }

    private JobSettingSpeed speed;

    private JobSettingErrorLimit errorLimit;


    public JobSettingSpeed getSpeed() {
        return speed;
    }

    public JobSetting setSpeed(JobSettingSpeed speed) {
        this.speed = speed;
        return this;
    }

    public JobSettingErrorLimit getErrorLimit() {
        return errorLimit;
    }

    public JobSetting setErrorLimit(JobSettingErrorLimit errorLimit) {
        this.errorLimit = errorLimit;
        return this;
    }
}
