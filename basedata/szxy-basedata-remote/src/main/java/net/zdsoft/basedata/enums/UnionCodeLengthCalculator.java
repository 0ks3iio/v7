package net.zdsoft.basedata.enums;

/**
 * @author shenke
 * @since 2019/2/28 上午9:46
 */
public final class UnionCodeLengthCalculator {

    /**
     * 省级长度
     */
    public static final Integer PROVINCE_UNION_CODE_LENGTH = 2;
    /**
     * 市
     */
    public static final Integer CITY_UNION_CODE_LENGTH = 4;
    /**
     * 区县
     */
    public static final Integer COUNTY_UNION_CODE_LENGTH = 6;
    /**
     * 乡镇
     */
    public static final Integer TOWN_UNION_CODE_LENGTH = 9;
    /**
     * 学校
     */
    public static final Integer SCHOOL_UNION_CODE_LENGTH = 12;

    /**
     * 获取指定级别的 unionCode 的长度
     * @param regionLevel
     * @return
     */
    public static Integer getUnionCodeLength(Integer regionLevel) {
        switch (regionLevel) {
            case 1: return 1;
            case 2: return PROVINCE_UNION_CODE_LENGTH;
            case 3: return CITY_UNION_CODE_LENGTH;
            case 4: return COUNTY_UNION_CODE_LENGTH;
            case 5: return TOWN_UNION_CODE_LENGTH;
            case 6: return SCHOOL_UNION_CODE_LENGTH;
            default:
                throw new RuntimeException("Not support regionLevel");
        }
    }
}
