package net.zdsoft.scoremanage.data.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.remote.service.StusysStudentBakRemoteService;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.ScoreInfoDao;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.NotLimitService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.ScoreLimitService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ui.ModelMap;


@Service("scoreInfoService")
public class ScoreInfoServiceImpl extends BaseServiceImpl<ScoreInfo, String> implements ScoreInfoService{

	@Autowired
	private ScoreInfoDao scoreInfoDao;
	@Autowired
	private ScoreLimitService scoreLimitService;
	@Autowired
	private NotLimitService notLimitService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private SchoolRemoteService  schoolService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private StusysStudentBakRemoteService stusysStudentBakRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<ScoreInfo, String> getJpaDao() {
		return scoreInfoDao;
	}

	@Override
	protected Class<ScoreInfo> getEntityClass() {
		return ScoreInfo.class;
	}

	@Override
    public Map<String, ScoreInfo> findByExamIdAndUnitIdAndClassId(String examId,
                                                                  String unitId, String classIdSearch,String classType, Set<String> stuIds, Set<String> courseIds){
        List<ScoreInfo> infoList=new ArrayList<ScoreInfo>();
        Map<String, ScoreInfo> map = new LinkedHashMap<String, ScoreInfo>();
        if(courseIds!=null && courseIds.size()>0){
            //if(StringUtils.isBlank(classType)){
            //infoList = scoreInfoDao.findByExamIdAndUnitIdAndSelectClassIdAndSubjectIdIn(examId,unitId,classIdSearch,courseIds.toArray(new String[]{}));

            List<Student> stulist = SUtils.dt(studentRemoteService.findListBlendClassIds(new String[]{classIdSearch}), new TR<List<Student>>() {});
            if(stulist.isEmpty()){
                return map;
            }
            infoList=scoreInfoDao.findByExamIdAndStudentIdInAndSubjectIdIn(examId,stulist.stream().map(Student::getId).collect(Collectors.toSet()).toArray(new String[0]),courseIds.toArray(new String[]{}));

            //}else if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
            //	//行政班
            //	infoList=scoreInfoDao.findByExamIdAndUnitIdAndClassIdAndSubjectIdIn(examId,unitId,classIdSearch,courseIds.toArray(new String[]{}));
            //}else if(ScoreDataConstants.CLASS_TYPE2.equals(classType)){
            //	//教学班(遗留 某个学生成绩在行政班输入后 在教学班看不到 原因：添加考试科目信息，教学班在行政班输入成绩后 再添加)
            //	if(StringUtils.isNotBlank(classIdSearch)){
            //		infoList=scoreInfoDao.findByExamIdAndUnitIdAndTeachClassIdAndSubjectIdIn(examId,unitId,classIdSearch,courseIds.toArray(new String[]{}));
            //	}else if(stuIds.size() > 0){
            //
            //		infoList = scoreInfoDao.findByExamIdAndUnitIdAndStudentIdInAndSubjectIdIn(examId, unitId, stuIds.toArray(new String[]{}), courseIds.toArray(new String[]{}));
            //	}
            //}
            if(CollectionUtils.isNotEmpty(infoList)){
                for(ScoreInfo info:infoList){
                    ScoreInfo scoreInfo = map.get(info.getStudentId()+"_"+info.getSubjectId());
                    if(scoreInfo == null || info.getCreationTime().after(scoreInfo.getCreationTime())){
                        map.put(info.getStudentId()+"_"+info.getSubjectId(), info);
                        stuIds.add(info.getStudentId());
                    }
                }
            }

        }else{
            //if(StringUtils.isBlank(classType)){
            //	infoList=scoreInfoDao.findByExamIdAndUnitIdAndSelectClassId(examId,unitId,classIdSearch);
            //}else if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
            //	//行政班
            //	infoList=scoreInfoDao.findByExamIdAndUnitIdAndClassId(examId,unitId,classIdSearch);
            //}else if(ScoreDataConstants.CLASS_TYPE2.equals(classType)){
            //	//教学班
            //	infoList=scoreInfoDao.findByExamIdAndUnitIdAndTeachClassId(examId,unitId,classIdSearch);
            //}
            List<Student> stulist = SUtils.dt(studentRemoteService.findListBlendClassIds(new String[]{classIdSearch}), new TR<List<Student>>() {});
            if(stulist.isEmpty()){
                return map;
            }

            infoList=scoreInfoDao.findByExamIdAndStudentIdIn(examId,stulist.stream().map(Student::getId).collect(Collectors.toSet()).toArray(new String[0]));
            if(CollectionUtils.isNotEmpty(infoList)){
                for(ScoreInfo info:infoList){
                    map.put(info.getStudentId()+"_"+info.getSubjectId(), info);
                    courseIds.add(info.getStudentId());
                    stuIds.add(info.getStudentId());
                }
            }
        }
        return map;
    }

    @Override
    public List<ScoreInfo> findByExamId(String examId) {
        return scoreInfoDao.findByExamId(examId);
    }

    @Override
    public List<ScoreInfo> findByExamIdCourseId(String schoolId, String examId,
                                                String courseId, String classIdSearch, String classType) {
        List<ScoreInfo> infoList=new ArrayList<ScoreInfo>();
        //if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
        //	//行政班
        //	infoList=scoreInfoDao.findByExamIdAndUnitIdAndClassIdAndSubjectId(examId,schoolId,classIdSearch,courseId);
        //}else if(ScoreDataConstants.CLASS_TYPE2.equals(classType)){
        //	//教学班
        //	infoList=scoreInfoDao.findByExamIdAndUnitIdAndTeachClassIdAndSubjectId(examId,schoolId,classIdSearch,courseId);
        //}

        List<Student> stulist = SUtils.dt(studentRemoteService.findListBlendClassIds(new String[]{classIdSearch}), new TR<List<Student>>() {});
        if(stulist.isEmpty()){
            return infoList;
        }
        infoList=scoreInfoDao.findByExamIdAndStudentIdIn(examId,stulist.stream().map(Student::getId).collect(Collectors.toSet()).toArray(new String[0]));
        return infoList;
    }

	@Override
	public ScoreInfo findByStudent(String examId, String courseId, String stuId) {
		return scoreInfoDao.findByExamIdAndSubjectIdAndStudentId(examId,courseId,stuId);
	}

	@Override
	public List<ScoreInfo> findScoreInfoListRanking(String examId, String isStuScoreType, String... studentIds) {
		return scoreInfoDao.findScoreInfoListRanking(examId, isStuScoreType, studentIds);
	}

	@Override
	public List<ScoreInfo> findListByExamId(String examId, String scoreType, String isStuScoreType, String... studentIds) {
		return scoreInfoDao.findListByExamId(examId, scoreType, isStuScoreType,studentIds);
	}

	@Override
	public List<ScoreInfo> saveAllEntitys(ScoreInfo... info) {
		return scoreInfoDao.saveAll(checkSave(info));
	}

	@Override
	public Map<String, ScoreInfo> findByExamIdAndUnitIdAndStudentIds(
			String examId, String unitId, String[] stuIds,Set<String> courseIds) {
		Map<String, ScoreInfo> map = new LinkedHashMap<String, ScoreInfo>();
		List<ScoreInfo> infoList=scoreInfoDao.findByExamIdAndUnitIdAndStudentIdIn(examId,unitId,stuIds);
		if(CollectionUtils.isNotEmpty(infoList)){
			for(ScoreInfo info:infoList){
				map.put(info.getStudentId()+"_"+info.getSubjectId(), info);
				courseIds.add(info.getSubjectId());
			}
		}
		return map;
	}
	@Override
	public List<ScoreInfo> findScoreInfoList(String examId, String[] subjectIds) {
		if(ArrayUtils.isEmpty(subjectIds)){
			return new ArrayList<ScoreInfo>();
		}
		return scoreInfoDao.findScoreInfoList(examId,subjectIds,ScoreInfo.SCORE_TYPE_0);
	}
	@Override
	public List<ScoreInfo> findScoreByExamIds(String unitId,String[] examIds,String inputType){
		if(examIds==null || examIds.length==0){
			return null;
		}
		return scoreInfoDao.findScoreByExamIds(unitId, examIds,inputType);
	}
	@Override
	public List<ScoreInfo> findOptionalCourseScore(String unitId, String subjectId,
			String teachClassId) {
		List<ScoreInfo> scoreInfList = scoreInfoDao.findByUnitIdAndExamIdAndSubjectIdAndTeachClassId(unitId,BaseConstants.ZERO_GUID,
				subjectId,teachClassId);
		return scoreInfList;
	}

	@Override
	public int getEditRole(String examId, String classIdSearch,
			String courseId, String unitId, String acadyear, String semester, String teacherId) {
		boolean noLimit = false;  //可以录分
		boolean normal = false;
		//判断录分总管理员权限
		List<String> teacherIdList = notLimitService.findTeacherIdByUnitId(unitId);
		if(teacherIdList.contains(teacherId)){
			noLimit = true;
		}
		//判断普通录分人员权限
		ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
		if(examId != null){
			searchDto.setExamId(examId);
		}else{
			//选修课，从教学班获取学年学期
			searchDto.setExamId(Constant.GUID_ZERO);
		}
		
		searchDto.setAcadyear(acadyear);
		searchDto.setSemester(semester);
		searchDto.setUnitId(unitId);
		searchDto.setSubjectId(courseId);
		searchDto.setClassIds(new String[]{classIdSearch});
		searchDto.setTeacherId(teacherId);
		
		List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
		if(CollectionUtils.isNotEmpty(limitList)){
			normal = true;
		}
		
		if(noLimit && normal){
			//同时具有两个权限
			return 2;
		}else if(noLimit){
			//录分总管理员
			return 0;
		}else if(normal){
			//普通录分人员
			return 1;
		}else{
			//没有录入权限
			return -1;
		}
	}

	@Override
	public List<ScoreInfo> findOptionalCourseScore(String unitId, String[] teachClassIds) {
		List<ScoreInfo> scoreInfList = scoreInfoDao.findByUnitIdAndExamIdAndTeachClassIdIn(unitId,BaseConstants.ZERO_GUID,teachClassIds);
		return scoreInfList;
	}
	
	@Override
	public String findJsonScoreInfo(String unitId, String examId, String gradeCode, ModelMap map) {
		JSONObject json = new JSONObject();
		ExamInfo exam = examInfoService.findOne(examId);
		if(exam == null) {
			return json.toString();
		}
		Unit u = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		String section = gradeCode.trim().substring(0, 1);
		String g = gradeCode.trim().substring(1, 2);
		Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-2DF","DM-RKXD-"+section}), new TR<Map<String, Map<String,McodeDetail>>>(){});
		String gName = findMapMapByMcodeIds.get("DM-RKXD-"+section).get(g).getMcodeContent();
		String title = u.getUnitName() + exam.getExamName() + "成绩汇总（"+gName + "年级)";
		Map params = new HashMap();
		params.put("title", title);
		json.put("params", params);

		Set<String> courseNameSet = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		});
		if (map != null) {
		    map.put("title", title);
        }

		List<JSONObject> infolist = new ArrayList<JSONObject>();
		List<SubjectInfo> sublist = subjectInfoService.findByUnitIdExamId(unitId, examId, gradeCode);
		if(CollectionUtils.isEmpty(sublist)) {
			return json.toString();
		}
//		Set<String> subjectInfoIds = EntityUtils.getSet(sublist, SubjectInfo::getId);
		Map<String,String> idMapSubId = EntityUtils.getMap(sublist, SubjectInfo::getId, SubjectInfo::getSubjectId);
		//考试科目
		Set<String> subIds = EntityUtils.getSet(sublist, SubjectInfo::getSubjectId);
		List<Course> courses = SUtils.dt(courseService.findListByIds(subIds.toArray(new String[0])), new TR<List<Course>>(){});
		Map<String, Course> courseMap = EntityUtils.getMap(courses, Course::getId);
		json.put("courseList", courses);

        if (map != null) {
            map.put("courseList", courses);
        }

		//考试班级
		List<ClassInfo> clslist = classInfoService.findBySchoolIdAndSubjectInfoIdIn(unitId, idMapSubId.keySet().toArray(new String[0]));
		if(CollectionUtils.isEmpty(clslist)) {
			return json.toString();
		}
		//参考班级
		Set<String> clsIds = new HashSet<>();
		Set<String> tClsIds  =new HashSet<>();
		Map<String,Set<String>> subClsIdsMap = new HashMap<>();
		for (ClassInfo classInfo : clslist) {
			if(!idMapSubId.containsKey(classInfo.getSubjectInfoId())) {
				continue;
			}
			if(!subClsIdsMap.containsKey(idMapSubId.get(classInfo.getSubjectInfoId()))) {
				subClsIdsMap.put(idMapSubId.get(classInfo.getSubjectInfoId()), new HashSet<String>());
			}
			subClsIdsMap.get(idMapSubId.get(classInfo.getSubjectInfoId())).add(classInfo.getClassId()+"_"+classInfo.getClassType());
			if(ScoreDataConstants.CLASS_TYPE1.equals(classInfo.getClassType())) {
				clsIds.add(classInfo.getClassId());
			}else {
				tClsIds.add(classInfo.getClassId());
			}
		}
		
		//考试成绩
		List<ScoreInfo> scorelist = new ArrayList<>();
		List<ScoreInfo> scorelist1 = scoreInfoDao.findScoreInfoList(examId,subIds.toArray(new String[0]),ScoreInfo.SCORE_TYPE_0,unitId);
		scorelist.addAll(scorelist1);
		List<ScoreInfo> scorelist2 = scoreInfoDao.findScoreInfoList(examId,subIds.toArray(new String[0]),ScoreInfo.SCORE_TYPE_1,unitId);
		scorelist.addAll(scorelist2);
		List<ScoreInfo> scorelist3 = scoreInfoDao.findScoreInfoList(examId,subIds.toArray(new String[0]),ScoreInfo.SCORE_TYPE_2,unitId);
		scorelist.addAll(scorelist3);
		Semester se = SUtils.dc(semesterService.getCurrentSemester(0, unitId),Semester.class);
		Map<String,List<Student>> clsStuMap = new HashMap<>();
		Map<String,List<Student>> tClsStuMap = new HashMap<>();
		Map<String, String> stuClsMap = new HashMap<>();
		if(se == null || !StringUtils.equals(exam.getAcadyear(), se.getAcadyear())) {
			Set<String> stuIds = EntityUtils.getSet(scorelist, ScoreInfo::getStudentId);
			if(CollectionUtils.isNotEmpty(stuIds)) {
				Map<String, Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {}), Student::getId);
				//TODO
				for (ScoreInfo scoreInfo : scorelist) {
					if(clsIds.contains(scoreInfo.getClassId())) {
						if(!clsStuMap.containsKey(scoreInfo.getClassId())) {
							clsStuMap.put(scoreInfo.getClassId(), new ArrayList<Student>() {});
						}
						clsStuMap.get(scoreInfo.getClassId()).add(stuMap.get(scoreInfo.getStudentId()));
					}
					if(StringUtils.isNotBlank(scoreInfo.getTeachClassId())&&tClsIds.contains(scoreInfo.getTeachClassId())) {
						if(!tClsStuMap.containsKey(scoreInfo.getTeachClassId())) {
							tClsStuMap.put(scoreInfo.getTeachClassId(), new ArrayList<Student>() {});
						}
						tClsStuMap.get(scoreInfo.getTeachClassId()).add(stuMap.get(scoreInfo.getStudentId()));
					}
					stuClsMap.put(scoreInfo.getStudentId(),scoreInfo.getClassId());
				}
			}
		}else {
			if(CollectionUtils.isNotEmpty(clsIds)) {
				List<Student> slist = SUtils.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(unitId,null,clsIds.toArray(new String[0]),null,null),new TR<List<Student>>() {});
				Set<String> stuIds = new HashSet<>();
				for (Student e : slist) {
					stuIds.add(e.getId());
					if(!clsStuMap.containsKey(e.getClassId())) {
						clsStuMap.put(e.getClassId(), new ArrayList<Student>() {});
					}
					clsStuMap.get(e.getClassId()).add(e);
				}
//				List<StudentGraduate> stuGraduateList = SUtils.dt(stusysStudentBakRemoteService.findByClassIds(clsIds.toArray(new String[0])), new TR<List<StudentGraduate>>() {});
//			    Set<String> studentIdSet = new HashSet<>();
//				for(StudentGraduate stu : stuGraduateList){
//					studentIdSet.add(stu.getStuid());
//			    }
//				if(CollectionUtils.isNotEmpty(studentIdSet)){
//					List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>() {});
//					for (Student e : studentList) {
//						if(!stuIds.contains(e.getId())) {
//							if(!clsStuMap.containsKey(e.getClassId())) {
//								clsStuMap.put(e.getClassId(), new ArrayList<Student>() {});
//							}
//							clsStuMap.get(e.getClassId()).add(e);
//						}
//					}
//				}
			}
			if(CollectionUtils.isNotEmpty(tClsIds)) {
				List<TeachClassStu> tSlist = SUtils.dt(teachClassStuRemoteService.findByClassIds(tClsIds.toArray(new String[0])),new TR<List<TeachClassStu>>() {});
				Set<String> stuIds = EntityUtils.getSet(tSlist, TeachClassStu::getStudentId);
				List<Student> stus2 = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
				Map<String,Student> stuMap = EntityUtils.getMap(stus2, Student::getId);
				for (TeachClassStu tStu : tSlist) {
					if(!tClsStuMap.containsKey(tStu.getClassId())) {
						tClsStuMap.put(tStu.getClassId(), new ArrayList<Student>() {});
					}
					if(stuMap.containsKey(tStu.getStudentId())) {
						Student e = stuMap.get(tStu.getStudentId());
						tClsStuMap.get(tStu.getClassId()).add(e);
					}
				}
			}
		}
		
		Map<String,ScoreInfo> stuSubScoreMap = new HashMap<>();
		for (ScoreInfo scoreInfo : scorelist) {
			stuSubScoreMap.put(scoreInfo.getStudentId()+"_"+scoreInfo.getSubjectId(), scoreInfo);
		}
		List<Clazz> clsAlls = SUtils.dt(classService.findBySchoolIdAcadyear(unitId, exam.getAcadyear()), new TR<List<Clazz>>() {});
		Map<String,Clazz> clsAllMap = EntityUtils.getMap(clsAlls,Clazz::getId);
		List<Grade> gradeAlls = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId, exam.getAcadyear()), new TR<List<Grade>>() {});
		Map<String,Grade> gradeMap = EntityUtils.getMap(gradeAlls, Grade::getId);
		for(String subId : subIds) {
			Course subject = courseMap.get(subId);
			if(!subClsIdsMap.containsKey(subId)) {
				continue;
			}
			Set<String> clsTypeIds = subClsIdsMap.get(subId);
			for (String clsIdType : clsTypeIds) {
				String clsId = clsIdType.split("_")[0];
				String clsType = clsIdType.split("_")[1];
				List<Student> stulist = null;
				if(ScoreDataConstants.CLASS_TYPE1.equals(clsType)) {
					stulist = clsStuMap.get(clsId);
				}else {
					stulist = tClsStuMap.get(clsId);
				}
//				System.out.println("stulist.size = "+stulist.size());
				if(CollectionUtils.isEmpty(stulist)) {
					continue;
				}
				for (Student stu : stulist) {
					Clazz cla = null;
					if(stuClsMap.containsKey(stu.getId())) {
						String clsid = stuClsMap.get(stu.getId());
						cla = clsAllMap.get(clsid);
					}else {
						cla = clsAllMap.get(stu.getClassId());
					}
//					System.out.print(stu.getStudentName());
					if(cla == null) {
//						System.out.println("班级为空");
						continue;
//					}else {
//						System.out.println("");
					}
					stu.setClassName(cla.getClassNameDynamic());
					Grade gra = gradeMap.get(cla.getGradeId());
					if(gra == null) {
						continue;
					}
					if(!StringUtils.equals(gra.getGradeCode(), gradeCode)) {
						continue;
					}
					JSONObject info = new JSONObject();
					info.put("clsName", gra.getGradeName() + cla.getClassName());
					info.put("classId", cla.getId());//行政班id
					info.put("studentId", stu.getId());
					info.put("subjectId", subId);
					info.put("subjectName", subject.getSubjectName()+"考试");
					courseNameSet.add(subject.getSubjectName()+"考试");
					info.put("subCode", subject.getSubjectCode());
					info.put("stuName", stu.getStudentName());
					info.put("stuCode", StringUtils.isBlank(stu.getStudentCode())?"":stu.getStudentCode());//
					info.put("clsCode", StringUtils.isBlank(stu.getClassInnerCode())?"":stu.getClassInnerCode());
					ScoreInfo score = stuSubScoreMap.get(stu.getId()+"_"+subId);
					if(score != null) {
						if(StringUtils.isNotBlank(score.getGradeType())) {
							McodeDetail m = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId(score.getGradeType(), score.getScore()),McodeDetail.class);
							info.put("score", m.getMcodeContent());//成绩
						}else {
							info.put("score", score.getScore());//成绩
						}
						infolist.add(info);
						if(StringUtils.isNotBlank(score.getToScore())) {
							JSONObject info1 = new JSONObject();
							info1.put("score", score.getToScore());//成绩
							info1.put("clsName", gra.getGradeName() + cla.getClassName());
							info1.put("classId", cla.getId());//行政班id
							info1.put("studentId", stu.getId());
							info1.put("subjectId", subId);
							info1.put("subCode", subject.getSubjectCode());
							info1.put("stuName", stu.getStudentName());
							info1.put("subjectName", subject.getSubjectName()+"总评");
							courseNameSet.add(subject.getSubjectName()+"总评");
							info1.put("stuCode", StringUtils.isBlank(stu.getStudentCode())?"":stu.getStudentCode());//
							info1.put("clsCode", StringUtils.isBlank(stu.getClassInnerCode())?"":stu.getClassInnerCode());
							infolist.add(info1);
						}else {
							if(StringUtils.equals(exam.getExamType().trim(), "12")) {
								JSONObject info1 = new JSONObject();
								info1.put("score", "");//成绩
								info1.put("clsName", gra.getGradeName() + cla.getClassName());
								info1.put("classId", cla.getId());//行政班id
								info1.put("studentId", stu.getId());
								info1.put("subjectId", subId);
								info1.put("subCode", subject.getSubjectCode());
								info1.put("stuName", stu.getStudentName());
								info1.put("subjectName", subject.getSubjectName()+"总评");
								courseNameSet.add(subject.getSubjectName()+"总评");
								info1.put("stuCode", StringUtils.isBlank(stu.getStudentCode())?"":stu.getStudentCode());//
								info1.put("clsCode", StringUtils.isBlank(stu.getClassInnerCode())?"":stu.getClassInnerCode());
								infolist.add(info1);
							}
						}
					}else {
						info.put("score", "");//成绩
						infolist.add(info);
						if(StringUtils.equals(exam.getExamType().trim(), "12")) {
							JSONObject info1 = new JSONObject();
							info1.put("score", "");//成绩
							info1.put("clsName", gra.getGradeName() + cla.getClassName());
							info1.put("classId", cla.getId());//行政班id
							info1.put("studentId", stu.getId());
							info1.put("subjectId", subId);
							info1.put("subCode", subject.getSubjectCode());
							info1.put("stuName", stu.getStudentName());
							info1.put("subjectName", subject.getSubjectName()+"总评");
							courseNameSet.add(subject.getSubjectName()+"总评");
							info1.put("stuCode", StringUtils.isBlank(stu.getStudentCode())?"":stu.getStudentCode());//
							info1.put("clsCode", StringUtils.isBlank(stu.getClassInnerCode())?"":stu.getClassInnerCode());
							infolist.add(info1);
						}
					}
				}
			}
		}
		json.put("infolist", infolist);
//		System.out.println(infolist);

        if (map != null) {
			/*Map<Object, Map<Object, JSONObject>> infoMap = infolist
					.stream()
					.collect(Collectors
							.groupingBy(e -> e.get("studentId"), Collectors.toMap(e -> e.get("subjectName"), e -> e)));*/
			Map<String, Map<String, JSONObject>> infoMap = new HashMap<>();
			for (JSONObject object : infolist) {
				if (!infoMap.containsKey(object.getString("studentId"))) {
					infoMap.put(object.getString("studentId"), new HashMap<>());
				}
				infoMap.get(object.getString("studentId")).put(object.getString("subjectName"), object);
			}
			map.put("infoMap", infoMap);
			map.put("courseNameList", courseNameSet);
			/*for (List<Student> one : clsStuMap.values()) {
				if (CollectionUtils.isNotEmpty(one)) {
					studentList.addAll(one);
				}
			}*/
			Map<String, Student> studentMap = clsStuMap.values().stream().flatMap(e -> e.stream())
					.collect(Collectors.toMap(Student::getId, e -> e, (origin, replacement) -> origin));
			List<Student> studentList = new ArrayList<>(studentMap.values());
			studentList.sort(Comparator.comparing(Student::getClassName));
			map.put("studentList", studentList);
        }

		return json.toJSONString();
	}
	

	@Override
	public List<ScoreInfo> findByCondition(String unitId, String acadyear, String semester, String studentId, String examId) {
		Specification<ScoreInfo> specification = new Specification<ScoreInfo>(){
			@Override
			public Predicate toPredicate(Root<ScoreInfo> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				ps.add(cb.equal(root.get("semester").as(String.class), semester));
				ps.add(cb.equal(root.get("studentId").as(String.class), studentId));
				if(StringUtils.isNotBlank(examId)){
					ps.add(cb.equal(root.get("examId").as(String.class), examId));
				}
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<ScoreInfo> scoreInfoList = findAll(specification);
		return scoreInfoList;
	}

	@Override
	public List<ScoreInfo> findBystudentIds(String unitId, String acadyear,
			String semester, String[] studentIds, String examId) {
		Specification<ScoreInfo> specification = new Specification<ScoreInfo>(){
			@Override
			public Predicate toPredicate(Root<ScoreInfo> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				if(StringUtils.isNotEmpty(acadyear)){
					ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				}
				if(StringUtils.isNotEmpty(semester)){
					ps.add(cb.equal(root.get("semester").as(String.class), semester));
				}

				if(null!=studentIds && studentIds.length>0){
//                	In<String> in = cb.in(root.get("studentId").as(String.class));
//                    for (int i = 0; i < studentIds.length; i++) {
//                        in.value(studentIds[i]);
//                    }
//                    ps.add(in);
					queryIn("studentId", studentIds, root, ps, cb);
                }
				if(StringUtils.isNotBlank(examId)){
					ps.add(cb.equal(root.get("examId").as(String.class), examId));
				}
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<ScoreInfo> scoreInfoList = findAll(specification);
		return scoreInfoList;
	}

	@Override
	public List<ScoreInfo> findByExamIdAndSubIdsAndStudentIds(String unitId, String[] examIds, String[] subjectIds, String[] studentIds) {
		Specification<ScoreInfo> specification = new Specification<ScoreInfo>(){
			@Override
			public Predicate toPredicate(Root<ScoreInfo> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				if(ArrayUtils.isNotEmpty(examIds)){
                	In<String> in = cb.in(root.get("examId").as(String.class));
                    for (int i = 0; i < examIds.length; i++) {
                        in.value(examIds[i]);
                    }
                    ps.add(in);
                }
				if(ArrayUtils.isNotEmpty(subjectIds)){
					In<String> in = cb.in(root.get("subjectId").as(String.class));
					for (int i = 0; i < subjectIds.length; i++) {
						in.value(subjectIds[i]);
					}
					ps.add(in);
				}
				if(ArrayUtils.isNotEmpty(studentIds)){
//					In<String> in = cb.in(root.get("studentId").as(String.class));
//					for (int i = 0; i < studentIds.length; i++) {
//						in.value(studentIds[i]);
//					}
//					ps.add(in);
					queryIn("studentId", studentIds, root, ps, cb);
				}
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<ScoreInfo> scoreInfoList = findAll(specification);
		return scoreInfoList;
	}

	public Map<String, Integer> findNumByExamIdClsIds(String examId, String clsType, String... clsIds){
		Map<String, Integer> map =new HashMap<>();
		if(ArrayUtils.isEmpty(clsIds)) {
			return map;
		}
		List<Student> stulist = SUtils.dt(studentRemoteService.findListBlendClassIds(clsIds), new TR<List<Student>>() {});
		if(stulist.isEmpty()){
			return map;
		}
		Map<String,String> stuToCla=stulist.stream().collect(Collectors.toMap(t->t.getId(),t->t.getClassId()));
		List<ScoreInfo> infoList=scoreInfoDao.findByExamIdAndStudentIdIn(examId,stulist.stream().map(Student::getId).collect(Collectors.toSet()).toArray(new String[0]));
		if(CollectionUtils.isEmpty(infoList)){
			return map;
		}

		for(ScoreInfo ent : infoList){
			String key = stuToCla.get(ent.getStudentId())+ent.getSubjectId();
			Integer num = map.get(key);
			if(num==null){
				map.put(key,1);
			}else{
				map.put(key,map.get(key)+1);
			}
		}
		return map;
		//if(ScoreDataConstants.CLASS_TYPE2.equals(clsType)) {
		//	return scoreInfoDao.findNumByExamIdTeaClsIds(examId, clsIds);
		//}
		//return scoreInfoDao.findNumByExamIdClsIds(examId, clsIds);
	}

	@Override
	public List<ScoreInfo> findByClsIds(String examId, String inputType, String... clsIds) {
		return scoreInfoDao.findByClsIds(examId,inputType,clsIds);
	}

	@Override
	public List<ScoreInfo> findListByClsIds(String unitId, String acadyear,
			String semester, String examId, String subjectId, String inputType, String... clsIds) {
		return scoreInfoDao.findListByClsIds(unitId, acadyear, semester, examId, subjectId, inputType, clsIds);
	}
	
	@Override
	public List<ScoreInfo> findListByClsIds(String unitId, String acadyear,
			String semester, String examId, String inputType, String... clsIds) {
		return scoreInfoDao.findListByClsIds(unitId, acadyear, semester, examId, inputType, clsIds);
	}
}
