package net.zdsoft.syncdata.custom.yaohai.service.impl;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdata.custom.yaohai.connect.MysqlConnect;
import net.zdsoft.syncdata.custom.yaohai.constant.YhBaseConstant;
import net.zdsoft.syncdata.custom.yaohai.service.YhSyncService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


@Service("yhSyncService")
public class YhSyncServiceImpl extends MysqlConnect implements YhSyncService {
	private Logger log = Logger.getLogger(YhSyncServiceImpl.class);
	@Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@Override
	public void saveSchool() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_SCHOOL_REDIS_KEY);
		List<School> schoolList = getBaseDate ("base_school",time,School.class);
		SimpleDateFormat sdf = getDataFormat();
		if(CollectionUtils.isNotEmpty(schoolList)) {
			log.info("更新学校的数据为----"+ schoolList.size());
			Map<String, String> stMap = new HashMap<>();
			stMap.put("01", "111");
			stMap.put("02", "211");
			stMap.put("03", "341");
			stMap.put("10", "411");
			stMap.put("15", "514");
			stMap.put("04", "365");
			stMap.put("05", "342");
			stMap.put("06", "341");
			stMap.put("07", "342");
			for (School school : schoolList) {
				//进行抓化
				school.setId(StringUtils.leftPad(school.getId(), 32, "0"));
				school.setRunSchoolType(School.DEFAULT_RUN_SCHOOL_TYPE);
				String schoolType = school.getSchoolType();
				if(stMap.get(schoolType) == null){
					schoolType = "211";
				}else{
					schoolType = stMap.get(schoolType);
				}
				school.setSchoolType(schoolType);
				if(StringUtils.isBlank(school.getSchoolCode()))
					school.setSchoolCode(StringUtils.substring(school.getId(), 22));
				if(StringUtils.isBlank(school.getRegionCode()))
					school.setRegionCode(YhBaseConstant.YAOHAI_SCHOOL_REGION_CODE_DEFAULT);
				if(StringUtils.isBlank(school.getSections())){
					String section =  schoolType.equals("111") ? "0" : 
						schoolType.equals("211") ? "1" : schoolType.equals("341") ? "2" : "3";
					school.setSections(section);
				}
				time = getEndModifyTime(time, sdf, school.getModifyTime());
			}
			//进行保存数据
			baseSyncSaveService.saveSchool(schoolList.toArray(new School[schoolList.size()]),null,Boolean.TRUE);
		}
		RedisUtils.set(YhBaseConstant.YAOHAI_SCHOOL_REDIS_KEY, time);
	}

	@Override
	public void saveTeacher() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_TEACHER_REDIS_KEY);
		String endTime = time;
		for (int k = 0; k < YhBaseConstant.YAOHAI_INDEX_DEFAULT; k++) {
			List<Teacher> teacherList = getBaseDate("base_teacher",time,Teacher.class, k);
			if(CollectionUtils.isEmpty(teacherList)){
				log.info("------- 教师分页:" + (k + 1) + "获取不到数据");
				break;
			}
			SimpleDateFormat sdf = getDataFormat();
			if(CollectionUtils.isNotEmpty(teacherList)) {
				log.info("更新教师的数据为----"+ teacherList.size());
				Set<String> uidList = new HashSet<>();
				for (Teacher teacher : teacherList) {
					teacher.setId(StringUtils.leftPad(teacher.getId(), 32, "0"));
					teacher.setUnitId(StringUtils.leftPad(teacher.getUnitId(), 32, "0"));
					teacher.setTeacherCode("0000");
					teacher.setSex(1);
					teacher.setIncumbencySign("11");
					uidList.add(teacher.getUnitId());
				}
				Map<String, List<Dept>> deptMap = SUtils.dt(deptRemoteService.findByUnitIdMap(uidList.toArray(new String[uidList.size()])),
						new TypeReference<Map<String, List<Dept>>>(){});
				for (Teacher teacher : teacherList) {
					String unitId = teacher.getUnitId();
	                if(deptMap != null || deptMap.get(unitId) != null){				
	                	List<Dept> depts = deptMap.get(unitId);
	                	for (Dept dept : depts) {
	                		if(dept.getIsDefault() == 1){
	                			teacher.setDeptId(dept.getId());
	                			break;
	                		}
						}
	                }
	                endTime = getEndModifyTime(endTime, sdf, teacher.getModifyTime());
				}
				//进行保存数据
				baseSyncSaveService.saveTeacher(teacherList.toArray(new Teacher[teacherList.size()]));
			}
		}
		log.info("-------教师最后的时间是:"+endTime);
		RedisUtils.set(YhBaseConstant.YAOHAI_TEACHER_REDIS_KEY, endTime);
	}

	@Override
	public void saveGrade() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveClass() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_CLAZZ_REDIS_KEY);
		String endTime = time;
		SimpleDateFormat sdf = getDataFormat();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
		if (semester == null) {
			log.info("------ 请先维护学年学期 -------");
			return;
		}
		List<Grade> gradeList = new ArrayList<Grade>();
		for (int k = 0; k < YhBaseConstant.YAOHAI_INDEX_DEFAULT; k++) {
			List<Clazz> clazzList  = getBaseDate ("base_class",time,Clazz.class,k);
			if(CollectionUtils.isEmpty(clazzList)){
				log.info("------- 班级分页:" + (k + 1) + "获取不到数据");
				break;
			}
			if(CollectionUtils.isNotEmpty(clazzList)) {
				log.info("更新班级的数据为----"+ clazzList.size());
				Map<String, Grade> gradeMap = new HashMap<>();
				for (Clazz clazz : clazzList) {
					clazz.setId(StringUtils.leftPad(clazz.getId(), 32, "0"));
					clazz.setSchoolId(StringUtils.leftPad(clazz.getSchoolId(), 32, "0"));
					Date creatDate = clazz.getCreationTime();
					String creatTime = new SimpleDateFormat("yyyy").format(creatDate);
					String acadyear = creatTime + "-" + (Integer.valueOf(creatTime)+1);
					clazz.setClassCode(StringUtils.substring(sdf.format(creatDate), 0, 8));
					clazz.setAcadyear(acadyear);
					Integer section = clazz.getSection();
					section = section == 03 ? 1 : section == 04 ? 2 : section == 05 ? 3 : 0;
					clazz.setSection(section);
					clazz.setSchoolingLength(section == 1 ? 6 : section == 2 ? 3 : section == 3 ? 3 : 1);
					
					int year = NumberUtils.toInt(StringUtils.substring(clazz.getAcadyear(), 0, 4));
					int year2 = NumberUtils.toInt(StringUtils.substring(semester.getAcadyear(), 0, 4));
//					int startSemester = NumberUtils.toInt(new SimpleDateFormat("MMdd").format(semester.getSemesterBegin()));
					Integer yearNum = null;
					String monthTime = "0910";  //班级的新建时间暂定是9月1号
					if(YhBaseConstant.YAOHAI_ISGRADUATE_DATE_DEFAULT < NumberUtils.toInt(monthTime)){
						year++;
					}
					if (year > (year2 + clazz.getSchoolingLength())) {
						clazz.setIsGraduate(1);
						clazz.setGraduateDate(new Date());
						yearNum = 6;
					} else {
						yearNum = year - year2;
						clazz.setIsGraduate(0);
					}
					String gradesJson = gradeRemoteService.findBySchoolId(clazz.getSchoolId(),
							ArrayUtils.toArray(clazz.getSection()), clazz.getAcadyear(), true);
					List<Grade> gradesExists = SUtils.dt(gradesJson, new TypeReference<List<Grade>>() {
					});
					for (Grade grade1 : gradesExists) {
						String key = grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection();
						gradeMap.put(key, grade1);
					}
					
					//根据学段和当前学期之间的时间差来得到gradename ,gradecode
					String gradeBefore;
					String gradeYear = yearNum == 1 ? "一" : yearNum == 2 ? "二" : yearNum == 3 ? "三" :
						yearNum == 4 ? "四" : yearNum == 5 ? "五" : "六";
					if(clazz.getSection() == 1){
						gradeBefore = "小";
					}else if (clazz.getSection() == 2) {
						gradeBefore = "初";
						if(yearNum > 3){
							gradeYear = "三";
						}
					}else {
						gradeBefore = "高";
						if(yearNum > 3){
							gradeYear = "三";
						}
					}
					String gradeName = gradeBefore + gradeYear;
					String gradeCode = ""+clazz.getSection() + yearNum;
					
					//年级名称的转化
					Grade grade1 = gradeMap.get(clazz.getSchoolId() + clazz.getAcadyear() + clazz.getSection());
					if (grade1 == null) {
						grade1 = new Grade();
						grade1.setGradeName(gradeName);
						grade1.setCreationTime(clazz.getCreationTime());
						grade1.setModifyTime(clazz.getModifyTime());
						grade1.setEventSource(0);
						grade1.setAmLessonCount(5);
						grade1.setPmLessonCount(4);
						grade1.setOpenAcadyear(clazz.getAcadyear());
						grade1.setNightLessonCount(3);
						grade1.setGradeCode(gradeCode);
						grade1.setSubschoolId(clazz.getCampusId());
						grade1.setSchoolId(clazz.getSchoolId());
						grade1.setIsGraduate(0);
						grade1.setSection(clazz.getSection());
						grade1.setSchoolingLength(clazz.getSchoolingLength());
						if (grade1.getSchoolingLength() == null || grade1.getSchoolingLength() == 0) {
							grade1.setSchoolingLength(grade1.getSection() == 1 ? 6 : 3);
						}
						grade1.setId(UuidUtils.generateUuid());
						gradeList.add(grade1);
						gradeMap.put(grade1.getSchoolId() + grade1.getOpenAcadyear() + grade1.getSection(), grade1);
					}
					clazz.setGradeId(grade1.getId());
					
					endTime = getEndModifyTime(endTime, sdf, clazz.getModifyTime());
				}
			}
			//进行保存数据
			if(CollectionUtils.isNotEmpty(clazzList)) {
				baseSyncSaveService.saveClass(clazzList.toArray(new Clazz[clazzList.size()]));
			}
			if (CollectionUtils.isNotEmpty(gradeList)) {
				baseSyncSaveService.saveGrade(gradeList.toArray(new Grade[gradeList.size()]));
			}
		}
		log.info("-------班级最后的时间是:"+endTime);
		RedisUtils.set(YhBaseConstant.YAOHAI_CLAZZ_REDIS_KEY, endTime);
	}

	@Override
	public void saveStudent() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_STUDENT_REDIS_KEY);
		String endTime = time;
		for (int k = 0; k < YhBaseConstant.YAOHAI_INDEX_DEFAULT; k++) {
			List<Student> studentList  = getBaseDate ("base_student",time,Student.class, k);
			if(CollectionUtils.isEmpty(studentList)){
				log.info("------- 学生分页:" + (k + 1) + "获取不到数据");
				break;
			}
			SimpleDateFormat sdf = getDataFormat();
			if(CollectionUtils.isNotEmpty(studentList)){
				log.info("更新学生的数据为----"+ studentList.size());
				for (Student student : studentList) {
					if(StringUtils.isBlank(student.getStudentCode())){
						if(StringUtils.isNotBlank(student.getIdentityCard())){
							student.setStudentCode(student.getIdentityCard());
						}else{
							student.setStudentCode(student.getId());
						}
					}
					student.setId(StringUtils.leftPad(student.getId(), 32, "0"));
					student.setSchoolId(StringUtils.leftPad(student.getSchoolId(), 32, "0"));
					student.setClassId(StringUtils.leftPad(student.getClassId(), 32, "0"));
					if(StringUtils.isBlank(student.getStudentName())){
						student.setStudentName(StringUtils.substring(student.getId(), 26));
					}
					
					endTime = getEndModifyTime(endTime, sdf, student.getModifyTime());
				}
				baseSyncSaveService.saveStudent(studentList.toArray(new Student[studentList.size()]));
			}
		}
		RedisUtils.set(YhBaseConstant.YAOHAI_STUDENT_REDIS_KEY, endTime);
	}

	@Override
	public void saveUser() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_USER_REDIS_KEY);
		String endTime = time;
		for (int k = 0; k < YhBaseConstant.YAOHAI_INDEX_DEFAULT; k++) {
			List<User> userList  = getBaseDate ("base_user",time,User.class,k);
			if(CollectionUtils.isEmpty(userList)){
				log.info("------- 用户分页:" + (k + 1) + "获取不到数据");
				break;
			}
			SimpleDateFormat sdf = getDataFormat();
			if(CollectionUtils.isNotEmpty(userList)){
				log.info("更新用户的数据为----"+ userList.size());
				for (User user : userList) {
					user.setUnitId(StringUtils.leftPad(user.getUnitId(), 32, "0"));
					user.setOwnerId(StringUtils.leftPad(user.getOwnerId(), 32, "0"));
					if(StringUtils.isBlank(user.getRegionCode())){
						user.setRegionCode(YhBaseConstant.YAOHAI_SCHOOL_REGION_CODE_DEFAULT);
					}
					if(StringUtils.isBlank(user.getPassword())){
						user.setPassword(new PWD(YhBaseConstant.YAOHAI_USER_PWD_DEFAULT).encode());
					}
					endTime = getEndModifyTime(endTime, sdf, user.getModifyTime());
				}
				baseSyncSaveService.saveUser(userList.toArray(new User[userList.size()]));
			}
		}
		RedisUtils.set(YhBaseConstant.YAOHAI_USER_REDIS_KEY, endTime);
	}
	

	@Override
	public void saveCourse() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_COURSE_REDIS_KEY);
		List<Course> courseList = getBaseDate ("base_course",time,Course.class);
		SimpleDateFormat sdf = getDataFormat();
		if(CollectionUtils.isNotEmpty(courseList)) {
			log.info("更新学科的数据为----"+ courseList.size());
			for (Course course : courseList) {
				time = getEndModifyTime(time, sdf, course.getModifyTime());
			}
			baseSyncSaveService.saveCourse(courseList.toArray(new Course[courseList.size()]));
		}
		RedisUtils.set(YhBaseConstant.YAOHAI_COURSE_REDIS_KEY, time);
	}

	@Override
	public void saveClassTeaching() {
		String time = getModifyTime(YhBaseConstant.YAOHAI_CLASS_TEACHING_REDIS_KEY);
		String endTime = time;
		for (int k = 0; k < YhBaseConstant.YAOHAI_INDEX_DEFAULT; k++) {
			List<ClassTeaching> classTeachingList  = getBaseDate ("base_class_teaching",time,ClassTeaching.class,k);
			if(CollectionUtils.isEmpty(classTeachingList)){
				log.info("------- 教师任课信息分页:" + (k + 1) + "获取不到数据");
				break;
			}
			SimpleDateFormat sdf = getDataFormat();
			if(CollectionUtils.isNotEmpty(classTeachingList)){
				log.info("更新教师任课信息的数据为----"+ classTeachingList.size());
				for (ClassTeaching classTeaching : classTeachingList) {
					classTeaching.setUnitId(StringUtils.leftPad(classTeaching.getUnitId(), 32, "0"));
					classTeaching.setClassId(StringUtils.leftPad(classTeaching.getClassId(), 32, "0"));
					classTeaching.setSubjectId(StringUtils.leftPad(classTeaching.getSubjectId(), 32, "0"));
					classTeaching.setTeacherId(StringUtils.leftPad(classTeaching.getTeacherId(), 32, "0"));
					endTime = getEndModifyTime(endTime, sdf, classTeaching.getModifyTime());
				}
				baseSyncSaveService.saveClassTeaching(classTeachingList.toArray(new ClassTeaching[classTeachingList.size()]));
			}
		}
		RedisUtils.set(YhBaseConstant.YAOHAI_CLASS_TEACHING_REDIS_KEY, endTime);
	}
	//-----------------------------------------从中间库获取数据代码----------------------------------------
	//从中间库中取出数据，根据时间戳
	private <T>  List<T> getBaseDate(String tableName, String modifyTime,Class<T> clazz){
		return getBaseDate(tableName,modifyTime,clazz,null);
	}
	
	private <T>  List<T> getBaseDate(String tableName, String modifyTime,Class<T> clazz, Integer page) {
		Connection conn = null;
		ResultSet rs = null;
		Statement statement = null;
		try {
			conn = MysqlConnect.getConnection();
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			statement = (Statement) conn.createStatement();
            //要执行的SQL语句
			StringBuilder sql = new StringBuilder();
			sql.append("select * from ");
			sql.append(tableName);
			if(StringUtils.isNotBlank(modifyTime)){
				sql.append(" where DATE_FORMAT(modify_time,'%Y%m%d%H%i%s') > ");
				sql.append(modifyTime);
			}
			if(page != null){
				int a = YhBaseConstant.YAOHAI_PAGESIZE_DEFAULT * page;
				sql.append(" limit ");
				sql.append(a);
				sql.append(",");
				sql.append(YhBaseConstant.YAOHAI_PAGESIZE_DEFAULT);
			}
            //3.ResultSet类，用来存放获取的结果集！！
            rs = statement.executeQuery(sql.toString());
            ArrayList<Object> saveTs = new ArrayList<>();
            if(rs != null){
            	while (rs.next()) {
            		T saveClass = clazz.newInstance();
            		Field[] fields = clazz.getDeclaredFields(); 
            		for (int i = 0; i < fields.length; i++) {
            			//移除@transient 和 不是private 的参数
            			Field field = fields[i];
            			Transient tran = field.getAnnotation(Transient.class);
            			if (tran != null) {
            				continue;
            			}
            			int fieldValue = field.getModifiers();
            			if(!Modifier.isPrivate(fieldValue) || Modifier.isStatic(fieldValue)){
            				continue;
            			}
            			String columnName = EntityUtils.humpToLine(field.getName());
            			field.setAccessible(true);
            			field.set(saveClass, rs.getObject(columnName));
            		}
            		//进行id的赋值
            		Field field=clazz.getSuperclass().getDeclaredField("id");
            		field.setAccessible(true);
            		field.set(saveClass, rs.getObject("id"));
            		
            		saveTs.add(saveClass);
            	}
            }
			return (ArrayList<T>) saveTs;
		}  catch (Exception e) {
			log.error("连接中间库，获取基础数据失败："+ e);
			return null;
		} finally {
			try {
				MysqlConnect.closeAll(conn, statement, rs);
			} catch (SQLException e) {
				log.error("关闭jdbc连接失败："+ e);
				return null;
			}
		}
	}
	//---------------------------------------私有方法区-------------------------------------------
	private String getModifyTime(String key) {
		String time = RedisUtils.get(key);
		if (StringUtils.isBlank(time)) {
			time = "19000101000000";
		}
		return time;
	}
	
	private SimpleDateFormat getDataFormat() {
		return new SimpleDateFormat("yyyyMMddHHmmss");
	}
	
	private String getEndModifyTime(String time, SimpleDateFormat sdf,
			Date modifyTime) {
		String gxsj = sdf.format(modifyTime);
		if (StringUtils.isNotBlank(gxsj) && time.compareTo(gxsj) < 0) {
			time = gxsj;
		}
		return time;
	}
}
