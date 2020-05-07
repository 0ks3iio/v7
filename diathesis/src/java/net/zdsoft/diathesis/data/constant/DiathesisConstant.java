package net.zdsoft.diathesis.data.constant;

import net.zdsoft.basedata.constant.BaseConstants;

import java.util.*;

/**
 * @DATE: 2019/03/29
 *
 */
public class DiathesisConstant {
    public static final String SYSTEM_NAME="diathesis";
    /**
     * 1:必修课 2:选修课
     * 1:学考
     * 1:学生自评 2:家长评价 3:学生互评 4:导师评价 5:班主任评价    6,学生组长评价  7: 学生间互相评价
     */
    public static final String SCORE_TYPE_1 = "1";
    public static final String SCORE_TYPE_2 = "2";
    public static final String SCORE_TYPE_3 = "3";
    public static final String SCORE_TYPE_4 = "4";
    public static final String SCORE_TYPE_5 = "5";
   // public static final String SCORE_TYPE_6 = "6";

    public static final String SCORE_TYPE_7 = "7";
    /**
     * 1:学科成绩录入 2:学业水平录入 3:综合素质录入 4:综合素质统计 5.综合素质学期得分
     */
    public static final String INPUT_TYPE_1 = "1";
    public static final String INPUT_TYPE_2 = "2";
    public static final String INPUT_TYPE_3 = "3";
    public static final String INPUT_TYPE_4 = "4";
    public static final String INPUT_TYPE_5 = "5";

    public static final Map<String, String> DIATHESIS_SCORE_TYPE_MAP = new TreeMap<>();

    static {
        DIATHESIS_SCORE_TYPE_MAP.put(SCORE_TYPE_1, "学生自评");
        DIATHESIS_SCORE_TYPE_MAP.put(SCORE_TYPE_2, "家长评价");
        DIATHESIS_SCORE_TYPE_MAP.put(SCORE_TYPE_3, "学生互评");
        DIATHESIS_SCORE_TYPE_MAP.put(SCORE_TYPE_4, "导师评价");
        DIATHESIS_SCORE_TYPE_MAP.put(SCORE_TYPE_5, "班主任评价");
    }

    /**
     * GRADE_CODE
     * 高一 31
     * 高二 32
     * 高三 33
     */
    public static final String GRADE_CODE_SENIOR_ONE = "31";
    public static final String GRADE_CODE_SENIOR_TWO = "32";
    public static final String GRADE_CODE_SENIOR_THREE = "33";

    /**
     * 学年学期名称
     */
    public static final List<String> HIGH_ACADYEAR_LIST = new ArrayList<>();

    static {
        HIGH_ACADYEAR_LIST.add("高一上");
        HIGH_ACADYEAR_LIST.add("高一下");
        HIGH_ACADYEAR_LIST.add("高二上");
        HIGH_ACADYEAR_LIST.add("高二下");
        HIGH_ACADYEAR_LIST.add("高三上");
        HIGH_ACADYEAR_LIST.add("高三下");
    }

    public static final List<String> JUNIOR_ACADYEAR_LIST = new ArrayList<>();

    static {
        JUNIOR_ACADYEAR_LIST.add("初一上");
        JUNIOR_ACADYEAR_LIST.add("初一下");
        JUNIOR_ACADYEAR_LIST.add("初二上");
        JUNIOR_ACADYEAR_LIST.add("初二下");
        JUNIOR_ACADYEAR_LIST.add("初三上");
        JUNIOR_ACADYEAR_LIST.add("初三下");
    }

    public static final List<String> PRIMARY_ACADYEAR_LIST = new ArrayList<>();

    static {
        PRIMARY_ACADYEAR_LIST.add("小一上");
        PRIMARY_ACADYEAR_LIST.add("小一下");
        PRIMARY_ACADYEAR_LIST.add("小二上");
        PRIMARY_ACADYEAR_LIST.add("小二下");
        PRIMARY_ACADYEAR_LIST.add("小三上");
        PRIMARY_ACADYEAR_LIST.add("小三下");
        PRIMARY_ACADYEAR_LIST.add("小四上");
        PRIMARY_ACADYEAR_LIST.add("小四下");
        PRIMARY_ACADYEAR_LIST.add("小五上");
        PRIMARY_ACADYEAR_LIST.add("小五下");
        PRIMARY_ACADYEAR_LIST.add("小六上");
        PRIMARY_ACADYEAR_LIST.add("小六下");
    }


    /**
     *  录入人,审核人code:
     *  diathesis_S : 学生本人   diathesis_C :班主任   diathesis_G :年级组长   diathesis_T: 导师
     *  * v1.2.0 新增导师角色  从德育管理取值
     */
    public static final String ROLE_STUDENT="diathesis_S";
    public static final String ROLE_CLASS="diathesis_C";
    public static final String ROLE_GRADE="diathesis_G";
    public static final String ROLE_TUTOR="diathesis_T";

    public static final List<String> ROLE_CODE_LIST=new ArrayList<>();
    public static final Map<String,String> ROLE_CODE_MAP=new HashMap<>();
    static {
        ROLE_CODE_LIST.add(ROLE_STUDENT);
        ROLE_CODE_LIST.add(ROLE_CLASS);
        ROLE_CODE_LIST.add(ROLE_GRADE);
        ROLE_CODE_LIST.add(ROLE_TUTOR);
        ROLE_CODE_MAP.put(ROLE_STUDENT,"学生本人");
        ROLE_CODE_MAP.put(ROLE_CLASS,"班主任");
        ROLE_CODE_MAP.put(ROLE_GRADE,"年级组长");
        ROLE_CODE_MAP.put(ROLE_TUTOR,"导师");
    }
    /**
     * 评价录入形式:
     *  G:等第   S:分值
     *
     */
    public static final String INPUT_RANK="G";
    public static final String INPUT_SCORE="S";

    public static final Map<String, String> INPUT_VALUE_TYPE_MAP=new HashMap<>();
    static {
        INPUT_VALUE_TYPE_MAP.put(INPUT_RANK,"G");
        INPUT_VALUE_TYPE_MAP.put(INPUT_SCORE,"C");
    }
    /**
     * 项目类型
     *  1: 类  2:子项目  3:写实记录
     */
    public static final String PROJECT_TOP="1";
    public static final String PROJECT_CHILD="2";
    public static final String PROJECT_RECORD="3";
    public static final String GOBAL_SET="4";
    
    /**
     * 数据类型
     * 1：文本 2：单选 3：多选 4：附件 5:数字 6:日期 7:文本域
     */
    public static final String DATA_TYPE_1 = "1";
    public static final String DATA_TYPE_2 = "2";
    public static final String DATA_TYPE_3 = "3";
    public static final String DATA_TYPE_4 = "4";
    public static final String DATA_TYPE_5 = "5";
    public static final String DATA_TYPE_6 = "6";
    public static final String DATA_TYPE_7 = "7";

    /**
     * 审核状态
     * 0：待审核 1：审核通过 2：审核不通过
     */
    public static final String AUDIT_STATUS_READY = "0";
    public static final String AUDIT_STATUS_PASS = "1";
    public static final String AUDIT_STATUS_FAIL = "2";

    /**
     * 模版 Template
     *  unitId: 00000000000000000000000000000000    32位0
     */
    public static final String TEMPLATE_UNIT_ID="00000000000000000000000000000000";

    /**
     * 用户权限
     * 1：管理员 2：年级组长 3：班主任 4:导师
     */
    public static final String ROLE_1="1";//管理员
	public static final String ROLE_2="2";//年级组长
	public static final String ROLE_3="3";//班主任
	public static final String ROLE_4="4";//导师

    /**
     *   写实记录设置,structure字段  isShow 是否显示
     *   0:不显示  1:显示
     */
    public static final Integer STRUCTURE_SHOW=1;
    public static final Integer STRUCTURE_NOT_SHOW=0;
	/**
	 * 附件上传文件夹名称
	 */
	public static final String FILEPATH="diathesis";

    /**
     * 分数制度
     *  S:分数范围制度 P:分数比例制度
     */
    public static final String SCORE_RANGE_REGULAR="S";
    public static final String SCORE_PROPORTION_REGULAR="P";

    /**
     *   L:组长统一录入  M:组内人员相互评价
     */
    public static final String MUTUAL_EVALUATE_LEADER="L";
    public static final String MUTUAL_EVALUATE_MEMBER="M";
    public static final Map<String,String> MUTUAL_TYPE_MAP=new HashMap<>();
    static {
        MUTUAL_TYPE_MAP.put(MUTUAL_EVALUATE_LEADER,"由小组长统一录入");
        MUTUAL_TYPE_MAP.put(MUTUAL_EVALUATE_MEMBER,"由组内人员相互评价");
    }

    /**
     *  权限
     *      1: 对下属单位授权的权利 (教育局才有)
     *      2: 全局设置
     *      3: 所有类目(一级,二级,三级,写实)
     *      4: 项目设置(二级,三级)
     *      5: 写实设置
     */
    public static final Integer AUTHOR_ADMIN=1;
    public static final Integer AUTHOR_GOBAL_SET=2;
    public static final Integer AUTHOR_PROJECT_ALL=3;
    public static final Integer AUTHOR_PROJECT_CHILD=4;
    public static final Integer AUTHOR_PROJECT_RECORD=5;
    /**
     *  key: 某权限   value: 哪些权限下有这个权限
     */
    public static final HashMap<Integer, List<Integer>> AUTHOR_TREE_MAP = new HashMap<>();
    public static final List<Integer> AUTHOR_TREE_LIST = new ArrayList<>();
    static {
        AUTHOR_TREE_MAP.put(AUTHOR_ADMIN,Arrays.asList(AUTHOR_ADMIN));
        AUTHOR_TREE_MAP.put(AUTHOR_GOBAL_SET,Arrays.asList(AUTHOR_ADMIN,AUTHOR_GOBAL_SET));
        AUTHOR_TREE_MAP.put(AUTHOR_PROJECT_ALL,Arrays.asList(AUTHOR_ADMIN,AUTHOR_PROJECT_ALL));
        AUTHOR_TREE_MAP.put(AUTHOR_PROJECT_CHILD,Arrays.asList(AUTHOR_ADMIN,AUTHOR_PROJECT_ALL,AUTHOR_PROJECT_CHILD));
        AUTHOR_TREE_MAP.put(AUTHOR_PROJECT_RECORD,Arrays.asList(AUTHOR_ADMIN,AUTHOR_PROJECT_ALL,AUTHOR_PROJECT_RECORD));
        AUTHOR_TREE_LIST.addAll(Arrays.asList(AUTHOR_ADMIN,AUTHOR_GOBAL_SET,AUTHOR_PROJECT_ALL,AUTHOR_PROJECT_CHILD,AUTHOR_PROJECT_RECORD));
    }

    /**
     *   0:成绩   1:学分    2:绩点   3: 负责人
     *
     */
    public static final String SCORE="0";
    public static final String CREDIT="1";
    public static final String GPA="2";
    public static final String PRINCIPAL="3";
    public static final Map<String,String> SCORE_TYPE_MAP=new HashMap<>();
    static {
        SCORE_TYPE_MAP.put(SCORE,"成绩");
        SCORE_TYPE_MAP.put(CREDIT,"学分");
        SCORE_TYPE_MAP.put(GPA,"绩点");
        SCORE_TYPE_MAP.put(PRINCIPAL,"负责人");
    }

    /**
     * 0:逐条展示    1:按统计次数展示
     */
    public static final String COUNT_TYPE_0="0";
    public static final String COUNT_TYPE_1="1";

    /**
     *  0:按次数加分    1: 按单选加
     */
    public static final String SCORE_ADD_TYPE_0="0";
    public static final String SCORE_ADD_TYPE_1="1";

    /**
     *  0: 不累计  1 累计
     */
    public static final String STRUCTURE_COUNT_TYPE_0="0";
    public static final String STRUCTURE_COUNT_TYPE_1="1";

    /**
     * newdiathesis_subject_field 表默认字段 --必修
     *   成绩   学分    审核人   负责人
     */
    public static final String COMPULSORY_SCORE="COMPULSORY_SCORE";
    public static final String COMPULSORY_GREDIT="COMPULSORY_GREDIT";
    public static final String COMPULSORY_AUDITOR="COMPULSORY_AUDITOR";
    public static final String COMPULSORY_PRINCIPAL="COMPULSORY_PRINCIPAL";
    public static final Map<String,String> COMPULSORY_MAP = new LinkedHashMap<String,String>();
    static {
        COMPULSORY_MAP.put(COMPULSORY_SCORE,"成绩");
        COMPULSORY_MAP.put(COMPULSORY_GREDIT,"学分");
        COMPULSORY_MAP.put(COMPULSORY_PRINCIPAL,"审核人");
        COMPULSORY_MAP.put(COMPULSORY_AUDITOR,"负责人");
    }

    /**
     * newdiathesis_subject_field 表默认字段 --选修  ELECTIVE
     *   成绩   课程类型(体育/语文)    选修类型(选修I,选修II..)   总学时   学分 审核人（录入人） 负责人（任课老师）
     */
    public static final String ELECTIVE_SCORE="ELECTIVE_SCORE";   //成绩
    public static final String ELECTIVE_COURSE_TYPE="ELECTIVE_COURSE_TYPE"; //课程类型(体育/语文)
    public static final String ELECTIVE_TYPE="ELECTIVE_TYPE";   //选修类型(选修I,选修II..)
    public static final String ELECTIVE_TOTAL_HOURS="ELECTIVE_TOTAL_HOURS";    //总学时
    public static final String ELECTIVE_GREDIT="ELECTIVE_GREDIT";     //学分
    public static final String ELECTIVE_AUDITOR="ELECTIVE_AUDITOR";     //审核人（录入人）
    public static final String ELECTIVE_PRINCIPAL="ELECTIVE_PRINCIPAL";     //负责人（任课老师）

    public static final Map<String,String> ELECTIVE_MAP= new LinkedHashMap<String,String>();
    static {
        ELECTIVE_MAP.put(ELECTIVE_SCORE,"成绩");
        ELECTIVE_MAP.put(ELECTIVE_COURSE_TYPE,"课程类型");
        ELECTIVE_MAP.put(ELECTIVE_TYPE,"选修类型");
        ELECTIVE_MAP.put(ELECTIVE_TOTAL_HOURS,"总学时");
        ELECTIVE_MAP.put(ELECTIVE_GREDIT,"学分");
        ELECTIVE_MAP.put(ELECTIVE_AUDITOR,"审核人");
        ELECTIVE_MAP.put(ELECTIVE_PRINCIPAL,"负责人");
    }

    /**
     * newdiathesis_subject_field 表默认字段 --学业水平   ACADEMIC
     *   0 合格性考试、1 等级性考试、2 操作测试
     */
    public static final String ACADEMIC_QUALIFY_TEST="ACADEMIC_QUALIFY_TEST";   //格性考试
    public static final String ACADEMIC_LEVEL_TEST="ACADEMIC_LEVEL_TEST";       //等级性考试
    public static final String ACADEMIC_OPERATE_TEST="ACADEMIC_OPERATE_TEST";   //操作测试

    public static final Map<String,String> ACADEMIC_MAP= new LinkedHashMap<String,String>();
    static {
        ACADEMIC_MAP.put(ACADEMIC_QUALIFY_TEST,"合格性考试");
        ACADEMIC_MAP.put(ACADEMIC_LEVEL_TEST,"等级性考试");
        ACADEMIC_MAP.put(ACADEMIC_OPERATE_TEST,"操作测试");
    }

    /**
     * 0 必修   1 选修   2 学业
     */
    public static final String SUBJECT_FEILD_BX="0";
    public static final String SUBJECT_FEILD_XX="1";
    public static final String SUBJECT_FEILD_XY="2";

    public static final Map<String,String> FIELD_TYPE_PRE_MAP= new LinkedHashMap<String,String>();
    static {
        FIELD_TYPE_PRE_MAP.put(SUBJECT_FEILD_BX,"COMPULSORY_");
        FIELD_TYPE_PRE_MAP.put(SUBJECT_FEILD_XX,"ELECTIVE_");
        FIELD_TYPE_PRE_MAP.put(SUBJECT_FEILD_XY,"ACADEMIC_");
    }

    public static final String DIATHESIS_YES="1";
    public static final String DIATHESIS_NO="0";

    public static final Map<String,String> SUBJECT_TYPE_MAP= new HashMap<String,String>();
    static {
        SUBJECT_TYPE_MAP.put(BaseConstants.SUBJECT_TYPE_BX,"必修");
        SUBJECT_TYPE_MAP.put(BaseConstants.SUBJECT_TYPE_XX,"选修");
        SUBJECT_TYPE_MAP.put(BaseConstants.SUBJECT_TYPE_XX_4,"选修Ⅰ-A");
        SUBJECT_TYPE_MAP.put(BaseConstants.SUBJECT_TYPE_BX,"选修Ⅰ-B");
        SUBJECT_TYPE_MAP.put(BaseConstants.SUBJECT_TYPE_XX_5,"选修Ⅱ");
    }

    
    
    /**
     * 福建省
     */
    public static final String FUJIAN_REGION_CODE = "35";

    /**
     *  写实记录 一个字段 多个附件上传 时 文件名分隔符号
     */
    public static final String FILE_SPLIT="///";

}
