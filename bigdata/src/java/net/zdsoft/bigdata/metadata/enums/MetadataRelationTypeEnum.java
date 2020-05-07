package net.zdsoft.bigdata.metadata.enums;

/**
 * Created by wangdongdong on 2019/5/22 10:35.
 */
public enum MetadataRelationTypeEnum {


    TABLE("数据表", "table"),

    DATAX_JOB("数据采集", "dataxJob"),

    ETL_JOB("ETL调度", "job"),

    API("数据接口", "api"),

    APP("数据应用", "app"),

    MODEL("数据模型", "model"),

    EVENT("事件管理", "event");

    private String name;

    private String code;

    MetadataRelationTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static MetadataRelationTypeEnum valueOfCode(String code) {
        for (MetadataRelationTypeEnum e : MetadataRelationTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
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
