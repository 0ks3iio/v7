package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkResultDao;
import net.zdsoft.gkelective.data.dao.GkResultJdbcDao;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkArrangeGroupResultDto;
import net.zdsoft.gkelective.data.dto.GkClassResultDto;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("gkResultService")
public class GkResultServiceImpl extends BaseServiceImpl<GkResult, String> implements GkResultService {

    @Autowired
    private GkResultDao gkResultDao;
    @Autowired
    private GkResultJdbcDao gkResultJdbcDao;
    @Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private CourseRemoteService courseService;
    @Autowired
    private GkSubjectService gkSubjectService;
    @Autowired
    private TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private GkBatchService gkBatchService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceService;
    @Autowired
    private GkStuRemarkService gkStuRemarkService;
    @Autowired
    private GkTeachClassStoreService gkTeachClassStoreService;
    @Autowired
    private GkRoundsService gkRoundsService;
    
    @Override
    protected BaseJpaRepositoryDao<GkResult, String> getJpaDao() {
        return gkResultDao;
    }

    @Override
    protected Class<GkResult> getEntityClass() {
        return GkResult.class;
    }

    @Override
    public List<GkResult> findByArrangeId(String arrangeId) {
        return gkResultDao.findByGkId(arrangeId);
    }

    @Override
    public List<GkResult> findGkResult(final String arrangeId, ChosenSubjectSearchDto searchDto, Pagination page, Set<String> notStudentIds) {
    	List<GkResult>  gkList =new ArrayList<GkResult>(); 
    	//取当前选课下的科目
    	List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_SUBJECT_ALL_KEY, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				return SUtils.dt(courseService.findByBaseCourseCodes(BaseConstants.SUBJECT_73),new TR<List<Course>>() {});
			}
        });
       
        GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(arrangeId);
			}
		});
        List<GkResult> gList = gkResultJdbcDao.findResultByChosenSubjectSearchDto(arrangeId, gkArrange.getGradeId(), searchDto);
        if(CollectionUtils.isEmpty(gList)){
        	return new ArrayList<GkResult>();
        }
        // 所有选课学生的id（arrangeId指定年级范围）
        Set<String> studentIds=new HashSet<String>();
        //学生对应科目
        Map<String, List<String>> studentId2courseIds = new HashMap<String, List<String>>();
        //key 学生+科目 
        Map<String, Integer> stuSubId2Status = new HashMap<String, Integer>();
        Map<String, String> stuSubId2Id = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(gList)){
        	for(GkResult g:gList){
        		 if (!studentId2courseIds.containsKey(g.getStudentId())) {
                     List<String> courseIdsList = new ArrayList<String>();
                     courseIdsList.add(g.getSubjectId());
                     studentId2courseIds.put(g.getStudentId(), courseIdsList);
                 } else {  
                	 List<String> sList = studentId2courseIds.get(g.getStudentId());
                	 sList.add(g.getSubjectId());
                	 studentId2courseIds.put(g.getStudentId(), sList);
                 }
        		 stuSubId2Status.put(g.getStudentId()+g.getSubjectId(), g.getStatus());
                 stuSubId2Id.put(g.getStudentId()+g.getSubjectId(), g.getId());
        		 studentIds.add(g.getStudentId());
        	}
         } else{
        	return new ArrayList<GkResult>();
        }
        if(CollectionUtils.isNotEmpty(notStudentIds)){
        	studentIds.addAll(notStudentIds);
        	studentIds.removeAll(notStudentIds);
        }
        if (CollectionUtils.isEmpty(studentIds)) {
            return new ArrayList<GkResult>();
        }
        List<Student> studentList = Student.dt(studentService.findListByIds(studentIds.toArray(new String[0])));
        
        if(CollectionUtils.isEmpty(studentList)){
        	return new ArrayList<GkResult>();
        }
        Map<String, Course> courseMap  = EntityUtils.getMap(coursesList, "id");
        Map<String, String> classMap = new HashMap<String, String>();
        Set<String> classIds = new HashSet<String>();
        Map<String, Double> scoreMap =gkStuRemarkService.findByArrangeIdType(arrangeId, GkStuRemark.TYPE_SCORE);
        
        if (StringUtils.isNotBlank(searchDto.getSearchClassId())) {
            classIds.add(searchDto.getSearchClassId());
            Clazz clazz = SUtils.dt(classService.findOneById(searchDto.getSearchClassId()), new TR<Clazz>() {});
            if (clazz != null) {
                classMap.put(clazz.getId(), clazz.getClassName());
            }
        }else if (StringUtils.isNotBlank(gkArrange.getGradeId())) {
            List<Clazz> classList = SUtils.dt(classService.findBySchoolIdGradeId(gkArrange.getUnitId(), gkArrange.getGradeId()),new TR<List<Clazz>>() {});
            if (CollectionUtils.isNotEmpty(classList)) {
                for (Clazz clazz : classList) {
                    classIds.add(clazz.getId());
                    classMap.put(clazz.getId(), clazz.getClassName());
                }
            }
        }
        for (Student stu : studentList) {
            GkResult gkr = new GkResult();
            gkr.setStudentId(stu.getId());
            gkr.setClassId(stu.getClassId());
            gkr.setStucode(stu.getStudentCode());
            gkr.setStuName(stu.getStudentName());
            gkr.setClassName(classMap.get(stu.getClassId()));
            if(stu.getSex()!=null){
            	gkr.setStuSex(stu.getSex()+"");
            }
            if (studentId2courseIds.containsKey(stu.getId())
                    && CollectionUtils.isNotEmpty(studentId2courseIds.get(stu.getId()))) {
                List<String> list = studentId2courseIds.get(stu.getId());
                String[] courseIdArr = new String[list.size()];
                String[] courseNameArr = new String[list.size()];
                Double[] courseScores = new Double[list.size()];
                Integer[] statusArr = new Integer[list.size()];
                String[] idsArr = new String[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    courseIdArr[j] = list.get(j);
                    if(scoreMap.containsKey(stu.getId()+list.get(j))){
                    	courseNameArr[j] = courseMap.get(courseIdArr[j]) == null ? null : courseMap.get(
                                courseIdArr[j]).getSubjectName()+"("+scoreMap.get(stu.getId()+list.get(j))+")";
                    	courseScores[j] = scoreMap.get(stu.getId()+list.get(j));
                    }else{
                    	courseNameArr[j] = courseMap.get(courseIdArr[j]) == null ? null : courseMap.get(
                            courseIdArr[j]).getSubjectName();
                    	courseScores[j] = 0.0;
                    }
                    statusArr[j] = stuSubId2Status.get(stu.getId()+courseIdArr[j]);
                    idsArr[j] = stuSubId2Id.get(stu.getId()+courseIdArr[j]); 
                }
                gkr.setCourseScores(courseScores);
                gkr.setCourseIds(courseIdArr);
                gkr.setCourseNames(courseNameArr);
                gkr.setStatues(statusArr);
                gkr.setIds(idsArr);
            }
            gkList.add(gkr);
        }
        return gkList;
    }

    @Override
    public Map<String, Integer> findResultCountByArrangeIdAndCourseIds(String arrangeId, String[] courseIds,String[] stuids) {
    	Map<String, Integer> id2count;
    	if(stuids==null){
    		id2count = gkResultJdbcDao.findResultCountByArrangeIdAndCourseIds(arrangeId, courseIds);
        }else{
        	id2count = gkResultJdbcDao.findResultCountBySubIdsStuIds(arrangeId, courseIds,stuids);
        }
        return id2count;
    }


    @Override
    public Integer findisChosenStudentCountByArrangeId(String arrangeId, String[] studentIds) {
    	if(ArrayUtils.isEmpty(studentIds)){
    		return 0;
    	}
    	int count=0;
    	if(studentIds.length>1000){
			int length = studentIds.length;
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
				Integer count1 = gkResultDao.countIsChosenByArrangeId(arrangeId, stuId);
				if(count1!=null){
					count=count+count1;
				}
			}
    	}else{
    		count = gkResultDao.countIsChosenByArrangeId(arrangeId, studentIds);
    	}
        return count;
    }

    @Override
    public List<String> findCoursesByStuId(String stuId, String arrangeId) {
        List<String> courseIds = gkResultDao.findCoursesByStuId(stuId, arrangeId);
        return courseIds;
    }

    @Override
    public List<GkResult> findGkByStuId(String[] stuIds, String arrangeId) {
        List<GkResult> gk = gkResultDao.findGkByStuId(stuIds, arrangeId);
        return gk;
    }

    @Override
    public List<GkResult> findUnChosenGkResult(String arrangeId, ChosenSubjectSearchDto searchDto, Pagination page, boolean selectAll,
            String... classIds) {
        GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        String unitId = gkSubjectArrange.getUnitId();
        List<GkResult> gkList = new ArrayList<GkResult>();
        // 所有选课学生的id（arrangeId指定年级范围）
        Set<String> studentIds = gkResultDao.findStudentIdsByArrangeId(arrangeId);
        List<Student> studentList = new ArrayList<Student>();
        Student searchStudent = new Student();
        searchStudent.setSex(StringUtils.isNotBlank(searchDto.getSearchSex())?Integer.valueOf(searchDto.getSearchSex()):null);
        if (("1").equals(searchDto.getSearchSelectType())) {
        	// 学号模糊查询
        	if(StringUtils.isNotBlank(searchDto.getSearchCondition())){
        		//防止注入
        		String linStr = searchDto.getSearchCondition().replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentCode(linStr+ "%");
        	}
        }
        else {
        	// 姓名
        	if(StringUtils.isNotBlank(searchDto.getSearchCondition())){
        		//防止注入
        		String linStr = searchDto.getSearchCondition().replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentName(linStr+ "%");
        	}
        }
        if (selectAll) {
            // 按年级查询 -- 分页
            studentList = Student.dt(studentService.findByNotIdsClaIdLikeStuCodeNames(unitId,
            		studentIds.toArray(new String[0]), classIds, Json.toJSONString(searchStudent), null));
        }
        else {
            // 按班级查询 - -不分页
            studentList = Student.dt(studentService.findByNotIdsClaIdLikeStuCodeNames(unitId,
            		studentIds.toArray(new String[0]), classIds, Json.toJSONString(searchStudent), null));
        }
        //
        for (Student stu : studentList) {
            GkResult gkr = new GkResult();
            gkr.setStudentId(stu.getId());
            gkr.setStucode(stu.getStudentCode());
            gkr.setStuName(stu.getStudentName());
            if(stu.getSex()!=null){
            	gkr.setStuSex(stu.getSex()+"");
            }
            gkList.add(gkr);
        }
        return gkList;
    }

    @Override
    public void removeByArrangeIdAndStudentId(String arrangeId, String id) {
        gkResultDao.deleteByArrangeIdAndStudentId(arrangeId, id);
    }

    @Override
    public void saveAll(List<GkResult> gkResultList, List<GkStuRemark> stuRemarkList, Set<String> studentIds) {
        if (CollectionUtils.isNotEmpty(gkResultList)) {
	        checkSave(gkResultList.toArray(new GkResult[0]));
//	    	for(int i = 0; i < gkResultList.size(); i++) {  
//				entityManager.merge(gkResultList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//	    	entityManager.clear(); 
	        gkResultDao.saveAll(gkResultList);
        }
        if(CollectionUtils.isNotEmpty(stuRemarkList)){
        	GkStuRemark ent =stuRemarkList.get(0);
        	gkStuRemarkService.deleteByStudentIdsAndArrangeId(ent.getSubjectArrangeId(), ent.getType(), studentIds.toArray(new String[0]));
            gkStuRemarkService.saveAll(stuRemarkList);
        }
    }
    
    @Override
    public Map<String,String> findCombineStudentMap(String roundIds) {
        // 从组合班关系(组合排班后 即使有微调，学生所在班级还是组合版班)
    	List<GkBatch> gkBathList=gkBatchService.findByRoundsId(roundIds,GkElectveConstants.GKCONDITION_GROUP_1);
    	Map<String,String> returnMap = new HashMap<String,String>();
    	if(CollectionUtils.isNotEmpty(gkBathList)){
    		Set<String> teachIds = new HashSet<String>();
    		Map<String,Integer> classBath=new HashMap<String, Integer>();
    		for (GkBatch g : gkBathList) {
                teachIds.add(g.getTeachClassId());
                classBath.put(g.getTeachClassId(), g.getBatch());
            }
    		if (teachIds.size() > 0) {
                List<TeachClass> list = SUtils.dt(
                        teachClassRemoteService.findTeachClassListByIds(teachIds.toArray(new String[0])),
                        new TR<List<TeachClass>>() {
                        });
                Map<String, TeachClass> map = EntityUtils.getMap(list, "id", StringUtils.EMPTY);
                if (map == null) {
                    map = new HashMap<String, TeachClass>();
                }
                List<TeachClassStu> stulist = SUtils.dt(
                        teachClassStuRemoteService.findByClassIds(teachIds.toArray(new String[0])),
                        new TR<List<TeachClassStu>>() {
                        });
                if (CollectionUtils.isNotEmpty(stulist)) {
                    for (TeachClassStu stu : stulist) {
                        if (!map.containsKey(stu.getClassId())) {
                            continue;
                        }
                        returnMap.put(stu.getStudentId() + "_" + map.get(stu.getClassId()).getCourseId(),stu.getClassId()+"_"+classBath.get(stu.getClassId()));
                    }
                }
            }
    	}
    	return returnMap;
    }

    @Override
    public List<GkArrangeGroupResultDto> findClassDtoList(String roundId, String classId) {
        List<GkArrangeGroupResultDto> dtolist = new ArrayList<GkArrangeGroupResultDto>();
        GkRounds rounds = gkRoundsService.findRoundById(roundId);
        // 获得各个学科是否走班信息
//        GkSubjectArrangeDto arrangeDto = gkSubjectArrangeService.findById(arrangeId);
//        List<GkSubjectArrangeEx> exlist = arrangeDto.getArrList();
//        Map<String, GkSubjectArrangeEx> subExMap = EntityUtils.getMap(exlist, "subjectId");
        List<GkSubject> findGkSubjectList = gkSubjectService.findByRoundsId(roundId,GkSubject.TEACH_TYPE1);
        Set<String> courseIds = EntityUtils.getSet(findGkSubjectList, "subjectId");
		List<Course> dt = SUtils.dt(courseService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
		Map<String, Course> courseMap = EntityUtils.getMap(dt, "id");
		List<StudentSubjectDto> findAllStudentSubjectDto = findAllStudentSubjectDto(rounds.getSubjectArrangeId(),null);
		Map<String, StudentSubjectDto> studentSubjectDtoMap = EntityUtils.getMap(findAllStudentSubjectDto, "stuId");
//        Map<String, GkSubject> subExMap = EntityUtils.getMap(findGkSubjectList, "subjectId");
        // 获得该班级下的学生
        List<Student> stulist = SUtils.dt(studentService.findByClassIds(classId), new TR<List<Student>>() {});
        if (CollectionUtils.isEmpty(stulist)) {
            return dtolist;
        }
        Set<String> stuIds = EntityUtils.getSet(stulist, "id");
        // 获得走班记录中该班级学生的记录
        // 这里的数据还没有过滤arrangeId
        List<GkTeachClassStuStore> teachClassStuList = gkTeachClassStoreService.findByStuIds(roundId,stuIds.toArray(new String[0]));
        Set<String> allTeachClassIds = EntityUtils.getSet(teachClassStuList, "gkClassId");
        // 在批次中过滤arrangeId
        Set<String> teachClassIds = new HashSet<String>();
        Map<String, GkBatch> batchMap = new HashMap<String, GkBatch>();
        Set<String> placeIds = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(allTeachClassIds)) {
            List<GkBatch> batchList = gkBatchService.findByClassIds(roundId, allTeachClassIds.toArray(new String[0]));
            for (GkBatch g : batchList) {
                teachClassIds.add(g.getTeachClassId());
                batchMap.put(g.getTeachClassId(), g);
                if (StringUtils.isNotBlank(g.getPlaceId())) {
                    placeIds.add(g.getPlaceId());
                }
            }
        }

        // 根据teaClassIds，去取关系表 用于判断是否是组合班
        List<GkTeachClassStore> teachClassList = new ArrayList<GkTeachClassStore>();
        if (CollectionUtils.isNotEmpty(teachClassIds)) {
            teachClassList = gkTeachClassStoreService.findListByIds(teachClassIds.toArray(new String[0]));
        }
        Map<String, GkTeachClassStore> teaClsMap = new HashMap<String, GkTeachClassStore>();
        for (GkTeachClassStore teaCls : teachClassList) {
            teaClsMap.put(teaCls.getId(), teaCls);
        }
        // 将teachClassStuList中无用数据去掉，并组装map，便于后面取数据stu,subid:teachclass
        Map<String, GkTeachClassStore> stuTeachClassMap = new HashMap<String, GkTeachClassStore>();
        for (GkTeachClassStuStore teaStu : teachClassStuList) {
            if (teachClassIds.contains(teaStu.getGkClassId())) {
                if (teaClsMap.containsKey(teaStu.getGkClassId())) {
                    stuTeachClassMap.put(teaStu.getStudentId() + "," + teaClsMap.get(teaStu.getGkClassId()).getSubjectId(),
                            teaClsMap.get(teaStu.getGkClassId()));
                }
            }
        }
        Map<String, String> placeMap = null;
        if (placeIds.size() > 0) {
            placeMap = SUtils.dt(teachPlaceService.findTeachPlaceMap(placeIds.toArray(new String[0])), new TR<Map<String, String>>() {});
        }
        boolean hasPlace = MapUtils.isNotEmpty(placeMap);
        // 数据组装
        GkArrangeGroupResultDto dto;
        StudentSubjectDto studentSubjectDto;
        Set<String> chooseSubjectIds;
        StringBuffer sbf = null;
        Course course = null;
        for (Student stu : stulist) {
            dto = new GkArrangeGroupResultDto();
            dto.setStuName(stu.getStudentName());
            dto.setStuCode(stu.getStudentCode());
            if(stu.getSex()!=null){
            	dto.setStuSex(stu.getSex()+"");
            }
            List<GkClassResultDto> clsReDtoList = new ArrayList<GkClassResultDto>();
            GkClassResultDto clsReDto;
//            for (String subId :subExMap.keySet()) {
        	for (GkSubject gkSubject :findGkSubjectList) {
                clsReDto = new GkClassResultDto();
                if (stuTeachClassMap.containsKey(stu.getId() + "," + gkSubject.getSubjectId())) {
                	GkTeachClassStore teachClass = stuTeachClassMap.get(stu.getId() + "," + gkSubject.getSubjectId());
                    // 接口待定
                    GkBatch gkBatch = batchMap.get(teachClass.getId());
                    if (hasPlace && StringUtils.isNotBlank(gkBatch.getPlaceId())
                            && placeMap.containsKey(gkBatch.getPlaceId())) {
                        clsReDto.setPlace(placeMap.get(gkBatch.getPlaceId()));
                    }
                    clsReDto.setClassName(BaseConstants.PC_KC+gkBatch.getBatch()+"<br>"+teachClass.getClassName());
                }
                
                clsReDtoList.add(clsReDto);
            }
            dto.setDtolist(clsReDtoList);
            studentSubjectDto = studentSubjectDtoMap.get(stu.getId());
            if(studentSubjectDto!=null){
            	 chooseSubjectIds = studentSubjectDto.getChooseSubjectIds();
                 sbf = new StringBuffer();
                 if(chooseSubjectIds!=null && chooseSubjectIds.size()>0){
                	 List<String> chooseStr=new ArrayList<String>(chooseSubjectIds);
             		 Collections.sort(chooseStr);
                	 //组装名称
                     for (String str : chooseStr) {
         				course = courseMap.get(str);
         				if(course!=null){
         					sbf.append(course.getShortName());
         				}
         			}
                 }
                 
                 dto.setStuChosenSubNames(sbf.toString());
            }
           
            dtolist.add(dto);
        }
        return dtolist;
    }

    @Override
    public void saveGkResultList(List<GkResult> gkResultList, String studentId, String arrangeId) {
        // 保存前先删除
//     gkResultDao.deleteByArrangeIdAndStudentId(arrangeId, studentId);
        List<GkResult> gkrList = gkResultDao.findGkByStuId(new String[]{studentId}, arrangeId);
        Map<String,GkResult> gkrMap = EntityUtils.getMap(gkrList, "subjectId");
        Set<String> delIds = new HashSet<String>();
        int j = 0;
        Date now = new Date();
        for(int i = 0; i<gkResultList.size();i++){
        	GkResult gkr = gkResultList.get(i);
        	if(gkrMap.containsKey(gkr.getSubjectId())){
        		j++;
        		gkResultList.remove(i);
        		gkrMap.remove(gkr.getSubjectId());
        		i--;
        	} else {
        		gkr.setCreationTime(now);
        		gkr.setModifyTime(now);
        		gkr.setId(UuidUtils.generateUuid());
        	}
        }
        if((j + gkResultList.size()) == 3){//该判断用于保持学生所选的课程为3门
	        Set<String> subIds = gkrMap.keySet();
	        if(CollectionUtils.isNotEmpty(subIds)){
	        	for(String subId : subIds){
	        		delIds.add(gkrMap.get(subId).getId());
	        	}
	        	gkResultDao.deleteByIds(delIds.toArray(new String[0]));
	        }
	        if(CollectionUtils.isNotEmpty(gkResultList)){
	        	gkResultDao.saveAll(gkResultList);
	        }
        }
    }
    @Override
    public void updateStatus(List<GkResult> gkResults) {
    	Map<String,GkResult> gkMap = EntityUtils.getMap(gkResults, "id");
    	List<GkResult> res = findListByIdIn(gkMap.keySet().toArray(new String[0]));
    	for(GkResult e : res){
    		e.setStatus(gkMap.get(e.getId()).getStatus());
    	} 
    	saveAll(res, null, null);
    }

	@Override
	public List<GkResult> findBySubjectId(String unitId, String arrangeId, String courseId, String classId, Pagination page) {
		List<GkResult> gklist=new ArrayList<GkResult>();
		if(StringUtils.isNotBlank(classId)){
			List<Student> studentList = SUtils.dt(studentService.findByClassIds(classId), new TR<List<Student>>(){});
			if(CollectionUtils.isNotEmpty(studentList)){
				gklist = gkResultDao.findBySubjectArrangeIdAndSubjectIdStuIds(arrangeId, new String[]{courseId}, EntityUtils.getSet(studentList, "id").toArray(new String[0]));
			}
		}else{
			gklist = gkResultDao.findBySubjectArrangeIdAndSubjectId(arrangeId,courseId);
		}
		Set<String> stuIds = EntityUtils.getSet(gklist, "studentId");
		List<Student> studentList = Student.dt(studentService.findByIdsClaIdLikeStuCodeNames(unitId,stuIds.toArray(new String[0]), null, null, SUtils.s(page)), page);
		Set<String> clsIds = EntityUtils.getSet(studentList, "classId");
		Map<String,Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classService.findListByIds(clsIds.toArray(new String[0])),new TR<List<Clazz>>(){}),"id");
		Map<String,Student> stuMap = EntityUtils.getMap(studentList,"id");
		List<GkResult> returnList=new ArrayList<GkResult>();
		for (GkResult gkr :gklist) {
			Student stu =stuMap.get(gkr.getStudentId());
			if(stu !=null){
				gkr.setStudentId(stu.getId());
	            gkr.setStucode(stu.getStudentCode());
	            gkr.setStuName(stu.getStudentName());
	            if(stu.getSex()!=null){
	            	gkr.setStuSex(stu.getSex()+"");
	            }
	            if(clsMap.containsKey(stu.getClassId())){
	            	gkr.setClassName(clsMap.get(stu.getClassId()).getClassNameDynamic());
	            }
	            returnList.add(gkr);
			}
        }
		return returnList;
	}
	
	@Override
	public List<GkResult> findByGroupSubjectId(String unitId, String arrangeId,
			String courseId, String classId, Pagination page) {
		List<GkResult> gkList = new ArrayList<GkResult>();
		String[] subIds = courseId.split(",");
		List<GkResult> results;
		if(StringUtils.isNotBlank(classId)){
			List<Student> studentList = SUtils.dt(studentService.findByClassIds(classId), new TR<List<Student>>(){});
			results = gkResultDao.findBySubjectArrangeIdAndSubjectIdStuIds(arrangeId, subIds, EntityUtils.getSet(studentList, "id").toArray(new String[0]));
		}else{
			results = gkResultDao.findBySubjectArrangeIdAndSubjectId(arrangeId,subIds);
		}
		
		if(CollectionUtils.isEmpty(results)){
			return gkList;
		}
		Map<String, StudentSubjectDto> stuSubs = new HashMap<String, StudentSubjectDto>();
		for(GkResult re : results){
			// 学生选课数据处理
			StudentSubjectDto ssDto = stuSubs.get(re.getStudentId());
			if(ssDto == null){
				ssDto = new StudentSubjectDto();
				ssDto.setStuId(re.getStudentId());
				ssDto.setSubjectIds(new ArrayList<String>());
			}
			List<String> sids = ssDto.getSubjectIds();
			sids.add(re.getSubjectId());
			ssDto.setSubjectIds(sids);
			stuSubs.put(re.getStudentId(), ssDto);
		}
		Iterator<Entry<String, StudentSubjectDto>>  stuSubIt = stuSubs.entrySet().iterator();
		
		List<String> subList = Arrays.asList(subIds);
		Collections.sort(subList);
		String subIdStr = GkElectveConstants.listToStr(subList);
		// 学生课程数据集合
		Set<String> stuIds = new HashSet<String>();
		while(stuSubIt.hasNext()){
			StudentSubjectDto dto = stuSubIt.next().getValue();
			List<String> sids = dto.getSubjectIds();
			Collections.sort(sids);// 学生课程id排序
			String stuSubIdStr = GkElectveConstants.listToStr(sids);
			if(StringUtils.equals(stuSubIdStr, subIdStr)){
				stuIds.add(dto.getStuId());
			}
		}
		if(CollectionUtils.isEmpty(stuIds)){
			return gkList;
		}
		List<Student> studentList = Student.dt(studentService.findByIdsClaIdLikeStuCodeNames(unitId,
				stuIds.toArray(new String[0]), null, null, SUtils.s(page)), page);
		Set<String> clsIds = EntityUtils.getSet(studentList, "classId");
		Map<String,Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classService.findListByIds(clsIds.toArray(new String[0])),new TR<List<Clazz>>(){}),"id");
		for (Student stu : studentList) {
            GkResult gkr = new GkResult();
            gkr.setStudentId(stu.getId());
            gkr.setStucode(stu.getStudentCode());
            gkr.setStuName(stu.getStudentName());
            if(stu.getSex()!=null){
            	gkr.setStuSex(stu.getSex()+"");
            } 
            if(clsMap.containsKey(stu.getClassId())){
            	gkr.setClassName(clsMap.get(stu.getClassId()).getClassNameDynamic());
            }
            gkList.add(gkr);
        }
		return gkList;
	}
	public List<StudentSubjectDto> findStudentSubjectDto(
			String subjectArrangeId,Set<String> subjectIds,String[] stuids) {
		List<GkResult> list = gkResultDao.findGkByStuId(stuids,subjectArrangeId);
        Set<String> stuIds=new HashSet<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            List<StudentSubjectDto> returnList = new ArrayList<StudentSubjectDto>();
            StudentSubjectDto dto = new StudentSubjectDto();
            // 有序
            Map<String, StudentSubjectDto> dtoMap = new LinkedHashMap<String, StudentSubjectDto>();
            for (GkResult result : list) {
                if (!dtoMap.containsKey(result.getStudentId())) {
                    dto = new StudentSubjectDto();
                    dto.setChooseSubjectIds(new HashSet<String>());
                    dto.setScoreMap(new HashMap<String, Double>());
                    dto.setStuId(result.getStudentId());
                    dtoMap.put(result.getStudentId(), dto);
                    stuIds.add(result.getStudentId());
                }
                dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
            }
            if (dtoMap.size() > 0) {
            	if(subjectIds!=null && subjectIds.size()>0){
        			for(String key:dtoMap.keySet()){
//        				if(CollectionUtils.intersection(subjectIds, dtoMap.get(key).getChooseSubjectIds()).size()==
//        						CollectionUtils.union(subjectIds, dtoMap.get(key).getChooseSubjectIds()).size()){
//        					returnList.add(dtoMap.get(key));
//        				}
        				//包含
        				if(CollectionUtils.intersection(subjectIds, dtoMap.get(key).getChooseSubjectIds()).size()==subjectIds.size()){
        					returnList.add(dtoMap.get(key));
        				}
        			}
        		}else{
        			Collection<StudentSubjectDto> valueCollection = dtoMap.values();
                    returnList = new ArrayList<StudentSubjectDto>(valueCollection);
        		}
            }
            //组装性别,姓名,成绩
            if(CollectionUtils.isNotEmpty(returnList) && stuIds.size()>0){
            	makeStu(returnList,subjectArrangeId,stuIds.toArray(new String[0]));
            }
            return returnList;
        }
        else {
            return new ArrayList<StudentSubjectDto>();
        }
	}
	@Override
	public List<StudentSubjectDto> findAllStudentSubjectDto(
			String subjectArrangeId,Set<String> subjectIds) {
		List<GkResult> list = gkResultDao.findByGkId(subjectArrangeId);
        Set<String> stuIds=new HashSet<String>();
      
        if (CollectionUtils.isNotEmpty(list)) {
            List<StudentSubjectDto> returnList = new ArrayList<StudentSubjectDto>();
            StudentSubjectDto dto = new StudentSubjectDto();
            // 有序
            Map<String, StudentSubjectDto> dtoMap = new LinkedHashMap<String, StudentSubjectDto>();
            for (GkResult result : list) {
                if (!dtoMap.containsKey(result.getStudentId())) {
                    dto = new StudentSubjectDto();
                    dto.setChooseSubjectIds(new HashSet<String>());
                    dto.setScoreMap(new HashMap<String, Double>());
                    dto.setStuId(result.getStudentId());
                    dtoMap.put(result.getStudentId(), dto);
                    stuIds.add(result.getStudentId());
                }
                dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
            }
            if (dtoMap.size() > 0) {
            	if(subjectIds!=null && subjectIds.size()>0){
        			for(String key:dtoMap.keySet()){
//        				if(CollectionUtils.intersection(subjectIds, dtoMap.get(key).getChooseSubjectIds()).size()==
//        						CollectionUtils.union(subjectIds, dtoMap.get(key).getChooseSubjectIds()).size()){
//        					returnList.add(dtoMap.get(key));
//        				}
        				//包含
        				if(CollectionUtils.intersection(subjectIds, dtoMap.get(key).getChooseSubjectIds()).size()==subjectIds.size()){
        					returnList.add(dtoMap.get(key));
        				}
        			}
        		}else{
        			Collection<StudentSubjectDto> valueCollection = dtoMap.values();
                    returnList = new ArrayList<StudentSubjectDto>(valueCollection);
        		}
            }
            //组装性别,姓名,成绩
            if(CollectionUtils.isNotEmpty(returnList) && stuIds.size()>0){
            	makeStu(returnList,subjectArrangeId,stuIds.toArray(new String[0]));
            }
            //组装班级名称
            if(CollectionUtils.isNotEmpty(returnList) ){
            	 Set<String> classIds=EntityUtils.getSet(returnList, "classId");
            	 Map<String,Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classService.findListByIds(classIds.toArray(new String[0])),new TR<List<Clazz>>(){}),"id");
            	 for(StudentSubjectDto s:returnList){
            		 if(clsMap.containsKey(s.getClassId())){
            			 s.setClassName(clsMap.get(s.getClassId()).getClassNameDynamic());
            		 }
            	 }
            }
           
            
            return returnList;
        }
        else {
            return new ArrayList<StudentSubjectDto>();
        }
	}
	//组装性别,姓名,成绩
	private void makeStu(List<StudentSubjectDto> returnList,String subjectArrangeId,String[] stuIds){
		//成绩  默认0分
    	List<GkStuRemark> scoreList = gkStuRemarkService.findByStudentIds(subjectArrangeId, new String[]{GkStuRemark.TYPE_SCORE,GkStuRemark.TYPE_SCORE_YSY}, stuIds);
    	Map<String,Map<String,Double>> stuScoreMap=new HashMap<String, Map<String,Double>>();
    	if(CollectionUtils.isNotEmpty(scoreList)){
    		for(GkStuRemark stuScore:scoreList){
    			if(!stuScoreMap.containsKey(stuScore.getStudentId())){
    				stuScoreMap.put(stuScore.getStudentId(), new HashMap<String, Double>());
    			}
    			stuScoreMap.get(stuScore.getStudentId()).put(stuScore.getSubjectId(), stuScore.getScore());
    		}
    	}
    	
    	List<Student> stuList = SUtils.dt(studentService.findListByIds(stuIds),new TR<List<Student>>(){});
    	Map<String, Student> stuMap = EntityUtils.getMap(stuList, "id");
    	for(StudentSubjectDto d:returnList){
    		int sexObj=GkElectveConstants.MALE;
    		//默认男生
    		if(stuMap.containsKey(d.getStuId())){
    			Integer sex = stuMap.get(d.getStuId()).getSex();
    			if(sex!=null && GkElectveConstants.FEMALE==sex){
    				sexObj=GkElectveConstants.FEMALE;
    			}
    			d.setStuName(stuMap.get(d.getStuId()).getStudentName());
    			d.setStuCode(stuMap.get(d.getStuId()).getStudentCode());
    			d.setClassId(stuMap.get(d.getStuId()).getClassId());
    		}
    		if(stuScoreMap.containsKey(d.getStuId())){
    			d.setScoreMap(stuScoreMap.get(d.getStuId()));
    		}
    		d.setSex(sexObj);
    	}
	}

	@Override
	public List<StudentSubjectDto> findStudentSubjectDtoByStuId(
			String subjectArrangeId, String[] stuId) {
		List<GkResult> list = gkResultDao.findGkByStuId(stuId, subjectArrangeId);
		Set<String> stuIds=new HashSet<String>();
		List<StudentSubjectDto> returnList = new ArrayList<StudentSubjectDto>();
        if (CollectionUtils.isNotEmpty(list)) {
            StudentSubjectDto dto = new StudentSubjectDto();
            // 有序
            Map<String, StudentSubjectDto> dtoMap = new LinkedHashMap<String, StudentSubjectDto>();
            for (GkResult result : list) {
                if (!dtoMap.containsKey(result.getStudentId())) {
                    dto = new StudentSubjectDto();
                    dto.setChooseSubjectIds(new HashSet<String>());
                    dto.setScoreMap(new HashMap<String, Double>());
                    dto.setStuId(result.getStudentId());
                    dtoMap.put(result.getStudentId(), dto);
                    stuIds.add(result.getStudentId());
                }
                dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
            }
            if (dtoMap.size() > 0) {
    			Collection<StudentSubjectDto> valueCollection = dtoMap.values();
                returnList = new ArrayList<StudentSubjectDto>(valueCollection);
            }
            //组装性别,姓名,成绩
            if(CollectionUtils.isNotEmpty(returnList) && stuIds.size()>0){
            	makeStu(returnList,subjectArrangeId,stuIds.toArray(new String[0]));
            }
            return returnList;
        }
        return returnList;
	}

	@Override
	public List<StudentSubjectDto> findStudentSubjectDtoByStudent(
			String subjectArrangeId, List<String> stuIds) {
		List<GkResult> list = gkResultDao.findGkByStuId(stuIds.toArray(new String[0]), subjectArrangeId);
		List<StudentSubjectDto> returnList = new ArrayList<StudentSubjectDto>();
		if (CollectionUtils.isNotEmpty(list)) {
            StudentSubjectDto dto = new StudentSubjectDto();
            // 有序
            Map<String, StudentSubjectDto> dtoMap = new LinkedHashMap<String, StudentSubjectDto>();
            for (GkResult result : list) {
                if (!dtoMap.containsKey(result.getStudentId())) {
                    dto = new StudentSubjectDto();
                    dto.setChooseSubjectIds(new HashSet<String>());
                    dto.setScoreMap(new HashMap<String, Double>());
                    dto.setStuId(result.getStudentId());
                    dtoMap.put(result.getStudentId(), dto);
                }
                dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
            }
            if (dtoMap.size() > 0) {
    			Collection<StudentSubjectDto> valueCollection = dtoMap.values();
                returnList = new ArrayList<StudentSubjectDto>(valueCollection);
            }
            //组装性别,姓名,成绩
            if(CollectionUtils.isNotEmpty(returnList) && stuIds.size()>0){
            	makeStu(returnList,subjectArrangeId,stuIds.toArray(new String[0]));
            }
            return returnList;
        }
        return returnList;
	}

	@Override
	public List<GkResult> findResultByArrangeIdAndCourseIds(final String arrangeId,final String[] courseIds,final String[] stuids) {
		if(courseIds != null && courseIds.length == 0){
			return new ArrayList<GkResult>();
		}else if(stuids != null && stuids.length == 0){
			return new ArrayList<GkResult>();
		}
		Specification<GkResult> s = new Specification<GkResult>() {
            @Override
            public Predicate toPredicate(Root<GkResult> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("subjectArrangeId").as(String.class), arrangeId));
                if(courseIds != null){
                	queryIn("subjectId", courseIds, root, ps,cb);
                }
                if(stuids != null){
                	queryIn("studentId", stuids, root, ps,cb);
                }
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
		return gkResultDao.findAll(s);
	}

	@Override
	public List<GkResult> findResultByChosenSubjectSearchDto(String arrangeId,
			String gradeId, ChosenSubjectSearchDto dto) {
		return gkResultJdbcDao.findResultByChosenSubjectSearchDto(arrangeId, gradeId, dto);
	}

	@Override
	public void deleteBySubjectArrangeIdAndSubjectId(String arrangeId,
			String[] subjectIds) {
		gkResultJdbcDao.deleteBySubjectArrangeIdAndSubjectId(arrangeId, subjectIds);
	}

	@Override
	public void deleteByArrangeIdAndStudentIds(String arrangeId, String[] studentIds) {
		if(ArrayUtils.isEmpty(studentIds)){
    		return;
    	}
    	if(studentIds.length>1000){
			int length = studentIds.length;
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
				gkResultDao.deleteByClassIds(arrangeId,stuId);
			}
    	}else{
    		gkResultDao.deleteByClassIds(arrangeId,studentIds);
    	}
	}

	@Override
	public Map<String, Set<String>> getGkResultByGradeId(String gradeId) {
		GkSubjectArrange dto= gkSubjectArrangeService.findEntByGradeId(gradeId);
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		if(dto!=null){
			// 所有选课学生的id（arrangeId指定年级范围）
			List<GkResult> list = gkResultDao.findByArrangeId(dto.getId());
			if(CollectionUtils.isNotEmpty(list)){
				for(GkResult ent: list){
					Set<String> set =map.get(ent.getSubjectId());
					if(set ==null){
						set=new HashSet<String>();
						map.put(ent.getSubjectId(), set);
					}
					set.add(ent.getStudentId());
				}
			}
		} 
		return map;
	}
}
