package net.zdsoft.gkelective.data.constant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class GkElectveConstants {
	
	/**
	 * 场地类型-教室4
	 */
	public final static String TEACH_PLACE_CLASSROOM = "4"; 
	
	public final static int AM_LESSON_COUNT_DEFAULT = 4;
	public final static int PM_LESSON_COUNT_DEFAULT = 4;
	public final static int NIGHT_LESSON_COUNT_DEFAULT = 0;
	/**
	 * 男
	 */
	public final static int MALE = 1;
	/**
	 * 女
	 */
	public final static int FEMALE = 2;
	
	
	/**
	 * 启用/删除/走班
	 */
	public final static int USE_TRUE=1;
	/**
	 * 不启用/正常/不走班
	 */
	public final static int USE_FALSE=0;
	
	/**
	 * 开学考班/是
	 */
	public final static String TRUE_STR="1";
	/**
	 * 不开学考班/否
	 */
	public final static String FALSE_STR="0";
	
	
	//目前先这几个步骤--用于页面是否可以看
	public final static int STEP_0=0;//新建轮次                       //新建方案
	public final static int STEP_1=1;//进入手动排班		//自动分配老师，进入课程表
	public final static int STEP_2=2;//进入手动排班结果
	public final static int STEP_3=3;//进入未安排的学生
	public final static int STEP_4=4;//单科排班
	public final static int STEP_5=5;//进入全部排班结果
	
//	/**去使用BaseConstants
//	 * 科目id-高中语文
//	 */
//	public static final String SUBJECT_TYPE_7 = "3007";
//	/**
//	 * 科目id-高中数学
//	 */
//	public static final String SUBJECT_TYPE_8 = "3008";
//	/**
//	 * 科目id-高中英语
//	 */
//	public static final String SUBJECT_TYPE_9 = "3009";
//	
//	public static final Set<String> SUBJECT_YSY_CODES = new HashSet<String>();
//	static{
//		SUBJECT_YSY_CODES.add(SUBJECT_TYPE_7);
//		SUBJECT_YSY_CODES.add(SUBJECT_TYPE_8);
//		SUBJECT_YSY_CODES.add(SUBJECT_TYPE_9);
//	}
	
	/**
//	 *  去使用BaseConstants.SUBJECT_73
//	 */
//	public static final String[] SUBJECT_TYPES =new String[]{
//		"3001",//	政治
//		"3002",//	历史
//		"3003",//	地理
//		"3006",//	物理
//		"3011",//	化学
//		"3020",//	生物
//		"3037"//	技术
//	};
	
	/**
	 * 课程对应多个教师(newgkelective_course_arrange);
	 */
	public static final String RELATIONSHIP_TYPE_01= "01";
	/**
	 * 02组合课程信息(newgkelective_condition);
	 */
	public static final String RELATIONSHIP_TYPE_02= "02";
	/**
	 * 02学生课程信息(newgkelective_condition);
	 */
	public static final String RELATIONSHIP_TYPE_03= "03";
	/**
	 * 04场地信息()
	 */
	public static final String RELATIONSHIP_TYPE_04= "04";
	/**
	 * 批次1
	 */
	public static final int BATCH_1 = 1;
	/**
	 * 批次2
	 */
	public static final int BATCH_2 = 2;
	/**
	 * 批次3
	 */
	public static final int BATCH_3 = 3;
	/**
	 * 开班-组合
	 */
	public static final String  GKCONDITION_GROUP_1="1";
	/**
	 * 开班-单科
	 */
	public static final String  GKCONDITION_SINGLE_0="0";
	
	/**
	 * 开班安排》开班结果步骤
	 */
	public static final String GKCONDITION_OPEN_STEP_RESULT = "2";
	
	public static final String LIMIT_TYPE_4="04";//班级
	public static final String LIMIT_TYPE_6="06";//7选3课程批次设置
	
	/**
	 * 字符串list转化为string，用,隔开
	 * @param strList
	 * @return
	 */
	public static String listToStr(List<String> strList){
		if(CollectionUtils.isEmpty(strList)){
			return StringUtils.EMPTY;
		}
		String[] subIdArr = strList.toArray(new String[0]);
		return StringUtils.join(subIdArr, ",");
	}
	
	//redis--Key
	public static final String GRADE_CLASS_LIST_KEY="gkelective_classList_gradeId_";//年级对应的班级列表--List<Clazz>
	public static final String GRADE_CLASS_ID_KEY="gkelective_class_id_";//班级--Clazz
	public static final String GK_SUBJECT_ALL_KEY="gkelective_subject_all_";//新高考所有科目--List<Course>
	public static final String GK_ARRANGE_KEY="gkelective_arrange_id_";//新高考项目--GkSubjectArrange
	public static final String GK_OPENSUBJECT_KEY="gkelective_openSubject_arrangeId_";//开放的科目--List<Course>
	
	public static final String GROUP_TYPE_1="1";//3+0
	public static final String GROUP_TYPE_2="2";//2+X
	public static final String GROUP_TYPE_3="3";//混合班
	
	//1手动预排班级，
//	public static final String USER_HAND="1";//班级
	//2组合系统生成班级
//	public static final String USER_AUTO="2";
	//预排站位课程位置，保存原来是年级id现在改成轮次id
	public static final String LIMIT_ARRANG_TYPE="06";
	//混合组合的subjectids
	public static final String GUID_ZERO = "00000000000000000000000000000000";
}
