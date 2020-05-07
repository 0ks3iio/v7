package net.zdsoft.exammanage.data.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExammanageConstants {
    public static final String EXAM_TYPE_FINAL = "12";
    /**
     * 成绩类型：分数或等第
     */
    public static final String ACHI_SCORE = "S";// 分数
    public static final String ACHI_GRADE = "G";// 等第
    public static final String RANGE_TYPE99 = "99";//考试科目中range_type=99表示教学班
    //时间段
    public static final int DAY = 30;
    //报名学生的审核状态
    public static final String ENROLL_STU_PASS_0 = "0"; //待审核
    public static final String ENROLL_STU_PASS_1 = "1"; //通过
    public static final String ENROLL_STU_PASS_2 = "2"; //不通过
    public static final String SUBJECT_9 = "99999999999999999999999999999999";
    public static final String SUBJECT_8 = "88888888888888888888888888888888";
    //教育局统考类型
    public static Map<String, String> eduTklx = new LinkedHashMap<String, String>();
    public static Map<String, String> eduTklxNew = new LinkedHashMap<String, String>();
    //学校统考类型
    public static Map<String, String> schTklx = new LinkedHashMap<String, String>();
    //用于查询
    public static Map<String, String> allTklx = new LinkedHashMap<String, String>();
    public static String TKLX_0 = "0";//校内考试
    public static String TKLX_1 = "1";//直属学校统考
    //public static String TKLX_2="2";//下属学校统考
    public static String TKLX_3 = "3";//校校联考
    public static String TKLX_4 = "4";//校校联考
    public static String EXAM_SUBSYSTEM = "39";
    public static String REPORT_SCHOOL_LEVEL = "report_school_level";
    public static String CLASS_TYPE1 = "1";//行政班
    public static String CLASS_TYPE2 = "2";//教学班
    public static String FILTER_TYPE1 = "1";//不排考
    public static String FILTER_TYPE2 = "2";//不统分
    public static String TEACHER_TYPE1 = "1";//监考老师
    public static String TEACHER_TYPE2 = "2";//巡考老师
    public static String ZERO32 = "00000000000000000000000000000000";
    //高考：语数英+选考赋分
    public static String CON_SUM_ID = "99999999999999999999999999999999";
    //语数英
    public static String CON_YSY_ID = "88888888888888888888888888888888";
    //考试科目类型
    public static String SUB_TYPE_0 = "0";//语数英or非高考
    public static String SUB_TYPE_1 = "1";//选考
    public static String SUB_TYPE_2 = "2";//学考
    /***/
    public static String SUB_TYPE_3 = "3";//选考分数统计
    //分数线设置
    public static String STAT_TYPE10 = "10";//分数线
    public static String STAT_METHOD_DO11 = "11";//分数线百分比
    public static String STAT_METHOD_DO12 = "12";//分数线名次
    public static String STAT_METHOD_DO13 = "13";//分数线分值
    public static Map<String, String> statType1 = new LinkedHashMap<String, String>();
    public static Map<String, String> STAT_METHOD_DO_MAP = new LinkedHashMap<String, String>();
    public static String STAT_TYPE21 = "21";//分数段百分比
    public static String STAT_TYPE22 = "22";//分数段名次
    public static String STAT_TYPE23 = "23";//分数段分值
    public static Map<String, String> statType2 = new LinkedHashMap<String, String>();

    static {

        allTklx.put(TKLX_0, "校内考试");
        allTklx.put(TKLX_1, "直属学校统考");
        //allTklx.put(TKLX_2, "下属学校统考");
        allTklx.put(TKLX_3, "校校联考");
        allTklx.put(TKLX_4, "校校联考");

        eduTklx.put(TKLX_1, allTklx.get(TKLX_1));
        //eduTklx.put(TKLX_2, allTklx.get(TKLX_2));
        eduTklx.put(TKLX_3, allTklx.get(TKLX_3));

        eduTklxNew.put(TKLX_4, allTklx.get(TKLX_4));

        schTklx.put(TKLX_0, allTklx.get(TKLX_0));
        schTklx.put(TKLX_3, allTklx.get(TKLX_3));

    }

    static {
        statType1.put(STAT_TYPE10, "分数线");

        STAT_METHOD_DO_MAP.put(STAT_METHOD_DO11, "百分比");
        STAT_METHOD_DO_MAP.put(STAT_METHOD_DO12, "名次");
        STAT_METHOD_DO_MAP.put(STAT_METHOD_DO13, "分值");
    }

    static {
        statType2.put(STAT_TYPE21, "百分比");
        statType2.put(STAT_TYPE22, "名次");
        statType2.put(STAT_TYPE23, "分值");
    }


}
