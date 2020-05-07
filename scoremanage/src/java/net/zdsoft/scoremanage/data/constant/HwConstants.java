package net.zdsoft.scoremanage.data.constant;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author niuchao
 * @date 2019/11/6 10:56
 */
public class HwConstants {
    /**
     * 科目
     */
    public static final String OBJECT_TYPE_1 = "01";
    /**
     * 总分或排名
     */
    public static final String OBJECT_TYPE_2 = "02";

    /**
     * 统计
     */
    public static final String PLAN_TYPE_1 = "01";
    /**
     * 保送生汇总
     */
    public static final String PLAN_TYPE_2 = "02";
    /**
     * 个人成绩汇总
     */
    public static final String PLAN_TYPE_3 = "03";

    /**
     * 7选3选考科目
     */
    public static final String[] SUBJECT_73 = new String[]{
            "GZ1014",   //高中政治选考
            "GZ1015",    //高中技术选考
            "GZ1016",   //高中生物选考
            "GZ1017",   //高中地理选考
            "GZ1018",   //高中物理选考
            "GZ1019",   //高中化学选考
            "GZ1020"    //高中历史选考
    };

    /**
     * 副科
     */
    public static final String[] SUBJECT_MINOR = new String[]{
            "7777",     //自习
            "7778",     //班会课
            "GZ1011",   //高中音乐
            "GZ1012",   //高中美术
            "GZ1013",   //高中体育
            "GZ1022",   //高中外文选修
            "GZ1023",   //生涯规划（单周上）
            "GZ1024",   //生涯规划（双周上）
            "GZ1025",   //高中日语
            "CZ2009",   //初中音乐
            "CZ2010",   //初中美术
            "CZ2011",   //初中体育
            "CZ2012",   //初中心理健康教育
            "TY3001",   //口试
            "TY3002",   //写作
            "TY3003",   //外教
            "TY3004",   //日本文化
            "TY3005",   //德语
            "TY3006",   //法语
            "TY3007",   //日语
            "TY3008",   //西班牙语
            "TY3009",   //自习
            "TY3010",   //班会
            "TY3011",   //拓展
            "TY3012"    //人生导航
    };

    public static final String OBJECT_ID_1="01";//考试总分
    public static final String OBJECT_ID_2="02";//总评总分
    public static final String OBJECT_ID_3="03";//考试总分班级排名
    public static final String OBJECT_ID_4="04";//考试总分年级排名
    public static final String OBJECT_ID_5="05";//总评总分班级排名
    public static final String OBJECT_ID_6="06";//总评总分年级排名
    public static final String OBJECT_ID_7="07";//班级人数
    public static final String OBJECT_ID_8="08";//年级人数

    /**
     * id-name
     */
    public static final Map<String,String> idNameMap =new LinkedHashMap<>();
    static{
        idNameMap.put("01", "考试总分");
        idNameMap.put("02", "总评总分");
        idNameMap.put("03", "考试总分班级排名");
        idNameMap.put("04", "考试总分年级排名");
        idNameMap.put("05", "总评总分班级排名");
        idNameMap.put("06", "总评总分年级排名");
        idNameMap.put("07", "班级人数");
        idNameMap.put("08", "年级人数");
    }

    /**
     * 成绩类型
     *  考试成绩: 1
     *   总评成绩: 2
     */
    public static final String SCORE_TYPE_EXAM="1";
    public static final String SCORE_TYPE_GENERAL="2";


    /**
     * 总分排名 设置的自定义key
     */
    public static final String SUM_SCORE_RANK="SUM_SCORE_RANK";
    /**
     * id-name
     */
    public static final Map<String,String> idNameMap2 =new LinkedHashMap<>();
    static{
        idNameMap2.put("01", "考试总分");
        idNameMap2.put("03", "考试总分班级排名");
        idNameMap2.put("04", "考试总分年级排名");
        idNameMap2.put("07", "班级人数");
        idNameMap2.put("08", "年级人数");
    }

    public static final String[] FLOW_TYPES = new String[]{"12","22","07","08","26","27","28","29","89"};

    /**
     * 科目成绩是否为等第
     */
    public static final Pattern IS_LEVEL_PATTERN=Pattern.compile("^[a-zA-Z]$");
}
