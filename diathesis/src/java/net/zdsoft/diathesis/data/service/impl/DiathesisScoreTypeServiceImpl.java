package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisScoreTypeDao;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfoEx;
import net.zdsoft.diathesis.data.entity.DiathesisScoreType;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service("diathesisScoreTypeService")
public class DiathesisScoreTypeServiceImpl
        extends BaseServiceImpl<DiathesisScoreType, String>
        implements DiathesisScoreTypeService {

    @Autowired
    private DiathesisScoreInfoExService diathesisScoreInfoExService;
    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private ExamManageRemoteService examManageRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;

    @Autowired
    private DiathesisSubjectFieldService diathesisSubjectFieldService;

    @Autowired
    private DiathesisSetSubjectService diathesisSetSubjectService;
    @Autowired
    private DiathesisScoreTypeDao diathesisScoreTypeDao;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;

    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;

    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;

    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Override
    public List<DiathesisScoreType> findByUnitIdAndGradeIdAndType(String unitId, String gradeId, String type) {
        if (StringUtils.isBlank(gradeId)) {
            return diathesisScoreTypeDao.findByUnitIdAndType(unitId, type);
        } else {
            return diathesisScoreTypeDao.findByUnitIdAndGradeIdAndType(unitId, gradeId, type);
        }
    }

    @Override
    public DiathesisScoreType findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(String gradeId, String type, String scoreType, String gradeCode, String semester, String examName) {
        if (examName == null) {
            return diathesisScoreTypeDao.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemester(gradeId, type, scoreType, gradeCode, Integer.valueOf(semester));
        } else {
            return diathesisScoreTypeDao.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(gradeId, type, scoreType, gradeCode, Integer.valueOf(semester), examName);
        }
    }

    @Override
    public List<DiathesisScoreType> findComprehensiveList(String gradeId, String gradeCode, String semester) {
        return diathesisScoreTypeDao.findByGradeIdAndGradeCodeAndSemesterAndTypeAndScoreTypeInOrderByScoreType(gradeId, gradeCode, Integer.valueOf(semester), DiathesisConstant.INPUT_TYPE_3, DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.keySet().toArray(new String[0]));
    }

    @Override
    public void deleteById(String id) {
        diathesisScoreTypeDao.deleteById(id);
        diathesisScoreInfoService.deleteByScoreTypeId(id);
    }

    @Override
    protected BaseJpaRepositoryDao<DiathesisScoreType, String> getJpaDao() {
        return diathesisScoreTypeDao;
    }

    @Override
    protected Class<DiathesisScoreType> getEntityClass() {
        return DiathesisScoreType.class;
    }

	@Override
	public List<DiathesisScoreType> findListByGradeId(String unitId, String gradeId, String gradeCode, String semester,
			String type) {
		return diathesisScoreTypeDao.findListByGradeId(unitId,gradeId,gradeCode,Integer.valueOf(semester),type);
	}

	@Override
	public void saveSate(List<DiathesisScoreInfo> scoreInfoList, DiathesisScoreType scoreType,
			String[] delScoreTypeIds) {
		if(delScoreTypeIds!=null && delScoreTypeIds.length>0) {
			diathesisScoreInfoService.deleteByScoreTypeIdIn(delScoreTypeIds);
		}
		diathesisScoreTypeDao.save(scoreType);
		if(CollectionUtils.isNotEmpty(scoreInfoList)) {
			diathesisScoreInfoService.saveAll(scoreInfoList.toArray(new DiathesisScoreInfo[] {}));
		}
	}

    @Override
    public void save(DiathesisScoreType diathesisScoreType, List<DiathesisScoreInfo> diathesisScoreInfoList) {
        diathesisScoreTypeDao.save(diathesisScoreType);
        if(CollectionUtils.isNotEmpty(diathesisScoreInfoList))
            diathesisScoreInfoService.saveAll(diathesisScoreInfoList.toArray(new DiathesisScoreInfo[0]));
    }

    @Override
    public List<DiathesisScoreType> findListByUnitIdsAndGradeCodeAndSemester(List<String> unitIds, String gradeCode, String year, Integer semester) {
        return diathesisScoreTypeDao.findListByUnitIdsAndGradeCodeAndSemester(unitIds,gradeCode,year,semester);
    }

    @Override
    public List<DiathesisScoreType> findListByGradeIdAndSemesterAndTypeAndScoreType(String gradeId, String semester, String type, String scoreType) {
        return diathesisScoreTypeDao.findListByGradeIdAndSemesterAndTypeAndScoreType(gradeId,semester,type,scoreType);
    }

    //成绩导入 学分
    @Override
    public DiathesisScoreType findTotalScoreType(String gradeId, String gradeCode, String semester) {
        return diathesisScoreTypeDao.findByGradeIdAndTypeAndGradeCodeAndSemester(gradeId, DiathesisConstant.INPUT_TYPE_4, gradeCode, Integer.parseInt(semester));
    }

    public void saveScoreToDiathesis(String unitId, String gradeId,String gradeCode, String semester,String scoreType, List<EmScoreInfo> scoreInfoList,String realName) {


        //所有成绩的科目id
        String[] subIds = EntityUtils.getSet(scoreInfoList, x -> x.getSubjectId()).toArray(new String[0]);
        //所有课程
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds), Course.class);
        //科目id: 学分
        Map<String, Integer> subIdAndCreditMap = EntityUtils.getMap(courseList, x -> x.getId(), x -> x.getInitCredit()==null?0:x.getInitCredit());


        //负责人
        String fieldCode =DiathesisConstant.COMPULSORY_PRINCIPAL;
        //学分
        String fieldGreditCode = DiathesisConstant.COMPULSORY_GREDIT;
        //审核人
        String auditorGreditCode = DiathesisConstant.COMPULSORY_AUDITOR;
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        String acadyear = getAcadyearBy(gradeId, gradeCode);

   /*      List<Clazz> clazz = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
         List<String> classIds = EntityUtils.getList(clazz, x -> x.getId());
        List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), Student.class);
        Map<String, String> stuClassMap = EntityUtils.getMap(stuList, x -> x.getId(), x -> x.getClassId());

      //班级任课老师 集合 负责人
        List<ClassTeaching> classTeacheringList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester,classIds.toArray(new String[0])), ClassTeaching.class);

        List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(EntityUtils.getSet(classTeacheringList,x->x.getTeacherId()).toArray(new String[0])), Teacher.class);
        Map<String, String> teacherMap = EntityUtils.getMap(teacherList, x -> x.getId(), x -> x.getTeacherName());

        //负责人 (任课老师)
        Map<String, String> pricipalMap = classTeacheringList.stream().filter(x ->StringUtils.isNotBlank(x.getTeacherId()) && StringUtils.isNotBlank(x.getClassId()) && StringUtils.isNotBlank(x.getSubjectId())).collect(Collectors.toMap(x -> x.getClassId() + "_" + x.getSubjectId(), x ->teacherMap.get(x.getTeacherId()) ));
*/
        DiathesisScoreType diathesisScoreType = findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(grade.getId(), DiathesisConstant.INPUT_TYPE_1, scoreType, gradeCode, semester, null);
        Map<String, DiathesisScoreInfo> oldScoreMap=new HashMap<>();
        Map<String, DiathesisScoreInfoEx> oldScoreExMap=new HashMap<>();

        if (diathesisScoreType == null) {
            diathesisScoreType = new DiathesisScoreType();
            diathesisScoreType.setId(UuidUtils.generateUuid());
            diathesisScoreType.setUnitId(unitId);
            diathesisScoreType.setGradeId(grade.getId());
            diathesisScoreType.setGradeCode(gradeCode);
            diathesisScoreType.setSemester(Integer.parseInt(semester));
            diathesisScoreType.setYear(grade.getOpenAcadyear().substring(0, 4));
            diathesisScoreType.setType(DiathesisConstant.INPUT_TYPE_1);
            diathesisScoreType.setScoreType(scoreType);
            String typeName = BaseConstants.SUBJECT_TYPE_BX.equals(scoreType) ? "必修课" : "选修课";
            diathesisScoreType.setExamName(grade.getGradeName() + acadyear + "学年第" + semester+ "学期" + typeName + "学科成绩");
            diathesisScoreType.setModifyTime(new Date());
            diathesisScoreType.setOperator(realName);
        } else {
            diathesisScoreType.setModifyTime(new Date());
            List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(scoreType, diathesisScoreType.getId());
            List<DiathesisScoreInfoEx> infoExList=diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());

            if (CollectionUtils.isNotEmpty(diathesisScoreInfoList)) {
                oldScoreMap = diathesisScoreInfoList.stream().collect(Collectors.toMap(e -> (e.getStuId() + "_" + e.getObjId()), e -> e));
            }
            if (CollectionUtils.isNotEmpty(infoExList)) {
                oldScoreExMap = infoExList.stream().collect(Collectors.toMap(e -> (e.getScoreInfoId() + "_" + e.getFieldCode()), e -> e));
            }
        }
        Set<String> operatorIds = EntityUtils.getSet(scoreInfoList, x -> x.getOperatorId());
        List<User> userList = SUtils.dt(userRemoteService.findListByIds(operatorIds.toArray(new String[0])), User.class);
        Map<String, String> operatorMap = EntityUtils.getMap(userList, x -> x.getId(), x -> StringUtils.defaultString(x.getRealName(),""));
        List<DiathesisScoreInfoEx> saveExList=new ArrayList<>();
        List<DiathesisScoreInfo> saveInfoList=new ArrayList<>();
        for (EmScoreInfo x : scoreInfoList) {
            String key=x.getStudentId()+"_"+x.getSubjectId();
            DiathesisScoreInfo tmp;
            if (oldScoreMap.containsKey(key)){
                //已经存在
                tmp=oldScoreMap.get(key);
                tmp.setScore(x.getScore());
                tmp.setModifyTime(new Date());

            }else {
                tmp = new DiathesisScoreInfo();
                tmp.setId(UuidUtils.generateUuid());
                tmp.setUnitId(unitId);
                tmp.setType(scoreType);
                tmp.setScoreTypeId(diathesisScoreType.getId());
                tmp.setStuId(x.getStudentId());
                tmp.setObjId(x.getSubjectId());
                tmp.setScore(x.getScore());
                tmp.setModifyTime(new Date());
            }

            //判断负责人是否存在
            //key diathesisScoreInfo.id  field.id   field负责人id
            /*DiathesisScoreInfoEx exEntity = oldScoreExMap.get(tmp.getId() + "_" + fieldCode);
            if(exEntity==null){
                exEntity=new DiathesisScoreInfoEx();
                exEntity.setFieldCode(fieldCode);
                exEntity.setScoreTypeId(diathesisScoreType.getId());
                exEntity.setScoreInfoId(tmp.getId());
                exEntity.setId(UuidUtils.generateUuid());
                exEntity.setModifyTime(new Date());
            }
            //输入负责人
            try{
                String pricipal=pricipalMap.get(stuClassMap.get(x.getStudentId()) + "_" + x.getSubjectId());
                exEntity.setFieldValue(StringUtils.defaultString(pricipal,""));
            }catch (Exception e){
                e.printStackTrace();
            }
            saveExList.add(exEntity);*/

            //判断学分是否存在
            DiathesisScoreInfoEx exgreditEntity = oldScoreExMap.get(tmp.getId() + "_" + fieldGreditCode);
            if(exgreditEntity==null){
                exgreditEntity=new DiathesisScoreInfoEx();
                exgreditEntity.setFieldCode(fieldGreditCode);
                exgreditEntity.setScoreTypeId(diathesisScoreType.getId());
                exgreditEntity.setScoreInfoId(tmp.getId());
                exgreditEntity.setId(UuidUtils.generateUuid());
                exgreditEntity.setModifyTime(new Date());
            }
            //输入学分
            String greditValue = ""+subIdAndCreditMap.get(x.getSubjectId());
            exgreditEntity.setFieldValue(greditValue);
            saveExList.add(exgreditEntity);

            /*//判断审核人是否存在
            DiathesisScoreInfoEx exAuditorEntity = oldScoreExMap.get(tmp.getId() + "_" + auditorGreditCode);
            if(exgreditEntity==null){
                exgreditEntity=new DiathesisScoreInfoEx();
                exgreditEntity.setFieldCode(auditorGreditCode);
                exgreditEntity.setScoreTypeId(diathesisScoreType.getId());
                exgreditEntity.setScoreInfoId(tmp.getId());
                exgreditEntity.setId(UuidUtils.generateUuid());
                exgreditEntity.setModifyTime(new Date());
            }
            //输入审核人

            exgreditEntity.setFieldValue(operatorMap.get(x.getOperatorId()));
            saveExList.add(exgreditEntity);*/

            saveInfoList.add(tmp);
        }
        save(diathesisScoreType,saveInfoList,saveExList);
    }

    @Override
    public List<DiathesisScoreType> findListByUnitIdsAndSemesterAndYear(List<String> unitIds, Integer semester, String year) {
        Assert.notEmpty(unitIds,"unitIds不能为空");
        return diathesisScoreTypeDao.findListByUnitIdsAndSemesterAndYear(unitIds, semester,year);
    }

    @Override
    public void deleteByIds(String[] typeIds) {
        diathesisScoreTypeDao.deleteByIds(typeIds);
    }

    @Override
    public Integer countByUnitIdAndTypeAndScoreTypeIn(String unitId, String type, String[] scoreTypes) {
        Integer count= diathesisScoreTypeDao.countByUnitIdAndTypeAndScoreType(unitId,type,scoreTypes);
        return count==null?0:count;
    }

    /**
     * 从教务管理中获取成绩 学分
     * @return
     */
    @Override
    public List<String> saveValueToDiathesis(String unitId, String gradeId, String gradeCode, String semester, String scoreType, String realName, String examId) {
        //导入成绩
        List<EmScoreInfo> scoreInfoList = SUtils.dt(examManageRemoteService.getExamScoreInfoByExamIdAndUnitId(unitId, examId), EmScoreInfo.class);
        Set<String> subIdsSet = EntityUtils.getSet(scoreInfoList, x -> x.getSubjectId());
        List<EmSubjectInfo> subList = SUtils.dt(examManageRemoteService.getExamSubByUnitIdAndExamId(unitId, examId), EmSubjectInfo.class);
        List<String> subIds =new ArrayList<>();
        if(CollectionUtils.isNotEmpty(subList)){
            subIds=subList.stream().filter(x -> !subIdsSet.contains(x.getSubjectId())).map(x -> x.getSubjectId()).collect(Collectors.toList());
        }

        if(CollectionUtils.isNotEmpty(scoreInfoList)) {
            saveScoreToDiathesis(unitId,gradeId,gradeCode,semester,scoreType,scoreInfoList,realName);
        }
        if(CollectionUtils.isNotEmpty(subIds)){
            List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), Course.class);
            return EntityUtils.getList(courses,x->x.getSubjectName());
        }
        return null;
    }

    @Override
    public String getAcadyearBy(String gradeId, String gradeCode) {
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        String openAcadyear = grade.getOpenAcadyear();
        int n = Integer.parseInt(openAcadyear.split("-")[0]);
        int level = Integer.parseInt(gradeCode.substring(1));
        return (n+level-1)+"-"+(n+level);
    }

    @Override
    public List<DiathesisScoreType> findListByUnitIdAndGradeIdAndType(String unitId, String gradeId, String type) {
        return diathesisScoreTypeDao.findListByUnitIdAndGradeIdAndType(unitId,gradeId,type);
    }

    @Override
    public void save(DiathesisScoreType diathesisScoreType, List<DiathesisScoreInfo> insertList, List<DiathesisScoreInfoEx> insertExList) {
        save(diathesisScoreType,insertList);
        if(CollectionUtils.isNotEmpty(insertExList))diathesisScoreInfoExService.saveAll(insertExList);
    }

    @Override
    public List<DiathesisScoreType> findByUnitIdAndTypeAndGradeIdAndGradeCodeAndSemester(String unitId, String type, String gradeId, String gradeCode, Integer semester) {
        return diathesisScoreTypeDao.findByUnitIdAndTypeAndGradeIdAndGradeCodeAndSemester(unitId,type,gradeId,gradeCode,semester);
    }

    @Override
	public List<DiathesisScoreType> findByUnitIdAndGradeIdAndTypeIn(String unitId, String gradeId, String[] types) {
		 return diathesisScoreTypeDao.findListByUnitIdAndGradeIdAndTypeIn(unitId,gradeId,types);
	}


}
