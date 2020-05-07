package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.scoremanage.data.entity.ClassInfo;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.NotLimitService;
import net.zdsoft.scoremanage.data.service.ScoreLimitService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scoremanage")
public class ScoreCommonAction extends BaseAction{
	
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ScoreLimitService scoreLimitService;
	@Autowired
	private NotLimitService notLimitService;
	@Autowired
	private GradeRemoteService gradeService;
	
	@ResponseBody
  	@RequestMapping("/common/examList")
  	public List<ExamInfo> examList(String acadyear,String semester,String searchType,String unitId,String noLimit,String fromType){
  		List<ExamInfo> examList=new ArrayList<ExamInfo>();
  		if(StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester)){
  			return examList;
  		}
  		ExamInfoSearchDto searchDto=new ExamInfoSearchDto();
  		searchDto.setSearchAcadyear(acadyear);
  		searchDto.setSearchSemester(semester);
  		if(StringUtils.isNotBlank(searchType)){
  			searchDto.setSearchType(searchType);
  		}
  		examList = examInfoService.findExamInfoList(unitId, searchDto, null);
  		//普通录分老师考试列表控制
  		if("1".equals(fromType) && CollectionUtils.isNotEmpty(examList) && !"1".equals(noLimit)){
			ScoreLimitSearchDto dto = new ScoreLimitSearchDto();
			dto.setAcadyear(acadyear);
			dto.setSemester(semester);
			dto.setUnitId(unitId);
			dto.setTeacherId(getLoginInfo().getOwnerId());
			
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(dto);
			if(CollectionUtils.isNotEmpty(limitList)){
				Set<String> examIdSet = EntityUtils.getSet(limitList, ScoreLimit::getExamInfoId);
				examIdSet.remove(Constant.GUID_ZERO);
				Iterator<ExamInfo> iterator = examList.iterator();
				while(iterator.hasNext()){
					if(!examIdSet.contains(iterator.next().getId())){
						iterator.remove();
					}
				}
			}else{
				examList = new ArrayList<ExamInfo>();
			}
	    }
        return examList;
  	}
	
	/**
     * 某个学校某次考试的班级信息
     * @param examId
     * @param schoolId
     * @return
     */
    @ResponseBody
	@RequestMapping("/common/clazzList")
	public Map<String,String> clazzList(String examId,String unitId){
    	Map<String, String> classMap = new LinkedHashMap<String, String>();
		//该考试下某个年级的所有学科
		List<SubjectInfo> subjectInfoList=subjectInfoService.findBySubjectInfoList(examId,unitId,null);
		if(CollectionUtils.isNotEmpty(subjectInfoList)){
			Set<String> subjectInfoIds=new HashSet<String>();
        	for(SubjectInfo info:subjectInfoList){
        		subjectInfoIds.add(info.getId());
        	}
        	List<ClassInfo> classInfoList =classInfoService.findBySchoolIdAndSubjectInfoIdIn(unitId, subjectInfoIds.toArray(new String[]{}));
        	Set<String> classIds1=new HashSet<String>();
        	Set<String> classIds2=new HashSet<String>();
        	if(CollectionUtils.isNotEmpty(classInfoList)){
        		for(ClassInfo info:classInfoList){
        			if(info.getClassType().equals("1")){
        				classIds1.add(info.getClassId());
        			}else{
        				classIds2.add(info.getClassId());
        			}
        			
            	}
        	}
        	if(classIds1.size()>0){
    			toMakeClassMap(ScoreDataConstants.CLASS_TYPE1, classIds1.toArray(new String[]{}),classMap);
        	}
        	if(classIds2.size()>0){
    			toMakeClassMap(ScoreDataConstants.CLASS_TYPE2, classIds2.toArray(new String[]{}),classMap);
        	}
		}
        return classMap;
    }
    
	/**
     * 某个学校某次考试的班级下科目信息
     * @param examId
     * @param schoolId
     * @return
     */
    @ResponseBody
	@RequestMapping("/common/subjectList")
	public List<Course> subjectList(String examId,String unitId,String classId){
    	List<SubjectInfo> infoList=new ArrayList<SubjectInfo>();
    	if(StringUtils.isNotBlank(classId)){
    		infoList=subjectInfoService.findBySubjectInfoList(examId, unitId,classId);
    	}else{
    		infoList=subjectInfoService.findBySubjectInfoList(examId, unitId,null);
    	}
    	List<Course> courseList=new ArrayList<Course>();
    	if(CollectionUtils.isNotEmpty(infoList)){
    		 Set<String> subjectIds = EntityUtils.getSet(infoList, SubjectInfo::getSubjectId);
    		 courseList=SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>(){});
    	}
    	return courseList;
    }
    
 
    /**
     * 某个学校学年学期的选修课
     * @param unitId
     * @param acadyear
     * @param semester
     * @return
     */
    @ResponseBody
	@RequestMapping("/common/subjectByGradeList")
	public List<Course> subjectByGradeList(String unitId,String acadyear,String semester,String gradeId){
    	List<Course> courseList=new ArrayList<Course>();
    	List<TeachClass> list = SUtils.dt(teachClassService.findBySearch(unitId,acadyear,semester,TeachClass.CLASS_TYPE_ELECTIVE,gradeId,null),new TR<List<TeachClass>>(){});
    	if(CollectionUtils.isNotEmpty(list)){
    		Iterator<TeachClass> iterator = list.iterator();
    		while(iterator.hasNext()){
				if(Constant.IS_TRUE_Str.equals(iterator.next().getIsUsingMerge())){
					iterator.remove();
				}
			}
    		Set<String> subjectIds = EntityUtils.getSet(list, TeachClass::getCourseId);
    		 courseList=SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>(){});
    	}
    	return courseList;
    }
    
	
	
	/**
     * 某个学校某次考试的年级code
     * @param examId
     * @param schoolId
     * @return
     */
    @ResponseBody
	@RequestMapping("/common/gradeCodeList")
	public Map<String,String> gradeCodeList(String examId,String unitId,String noLimit,String fromType){
    	Set<String> codes=new HashSet<String>();
    	Map<String,String> gradeCodeMap=new LinkedHashMap<String, String>();
    	if(StringUtils.isEmpty(examId)){
    		return gradeCodeMap;
    	}
    	//普通录分老师年级控制
    	if("1".equals(fromType) && !"1".equals(noLimit)){
    		ExamInfo examInfo = examInfoService.findOne(examId);
    		ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
    		searchDto.setExamId(examId);
    		searchDto.setAcadyear(examInfo.getAcadyear());
    		searchDto.setSemester(examInfo.getSemester());
    		searchDto.setUnitId(getLoginInfo().getUnitId());
    		searchDto.setTeacherId(getLoginInfo().getOwnerId());
    		
    		List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
    		if(CollectionUtils.isNotEmpty(limitList)){
    			Set<String> gradeIdSet = new HashSet<String>();
    			Set<String> classIdSet = EntityUtils.getSet(limitList, ScoreLimit::getClassId);
    			List<TeachClass> teachClassList = SUtils.dt(teachClassService.findListByIds(classIdSet.toArray(new String[0])),TeachClass.class);
    			List<Clazz> classList = SUtils.dt(classService.findListByIds(classIdSet.toArray(new String[0])),Clazz.class);
    			gradeIdSet.addAll(EntityUtils.getSet(teachClassList,TeachClass::getGradeId));
    			gradeIdSet.addAll(EntityUtils.getSet(classList, Clazz::getGradeId));
    			if(CollectionUtils.isNotEmpty(gradeIdSet)){
    				List<Grade> gradeList = SUtils.dt(gradeService.findListByIds(gradeIdSet.toArray(new String[0])),Grade.class);
    				codes.addAll(EntityUtils.getSet(gradeList, Grade::getGradeCode));
    			}
    		}
    	}else{
    		//该考试下所有学科
    		List<SubjectInfo> subjectInfoList=subjectInfoService.findByExamIdIn(null, examId);
    		if(CollectionUtils.isNotEmpty(subjectInfoList)){
    			Set<String> infoIds=new HashSet<String>();
    			Map<String,SubjectInfo> infoMap=new HashMap<String, SubjectInfo>();
    			for(SubjectInfo subjectInfo:subjectInfoList){
    				infoIds.add(subjectInfo.getId());
    				infoMap.put(subjectInfo.getId(), subjectInfo);
    			}
    			List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(unitId, infoIds.toArray(new String[0]));
    			if(CollectionUtils.isNotEmpty(classInfoList)){
    				for(ClassInfo classInfo:classInfoList){
    					codes.add(infoMap.get(classInfo.getSubjectInfoId()).getRangeType());
    				}
    			}
    		}
    		
    	}
    	Map<String,String> allCode=new HashMap<String, String>();
    	List<McodeDetail> mlist = SUtils.dt(mcodeRemoteService.findAllByMcodeIds(new String[]{"DM-RKXD-0","DM-RKXD-1","DM-RKXD-2","DM-RKXD-3","DM-RKXD-9"}),new TR<List<McodeDetail>>(){});
    	if(CollectionUtils.isNotEmpty(mlist)){
    		for(McodeDetail m:mlist){
    			if("DM-RKXD-0".equals(m.getMcodeId())){
    				allCode.put("0"+m.getThisId(), m.getMcodeContent());
    			}else
    			if("DM-RKXD-1".equals(m.getMcodeId())){
    				allCode.put("1"+m.getThisId(), m.getMcodeContent());
    			}else
    			if("DM-RKXD-2".equals(m.getMcodeId())){
    				allCode.put("2"+m.getThisId(), m.getMcodeContent());
    			}else
    			if("DM-RKXD-3".equals(m.getMcodeId())){
    				allCode.put("3"+m.getThisId(), m.getMcodeContent());
    			}
    			else
        			if("DM-RKXD-9".equals(m.getMcodeId())){
        				allCode.put("9"+m.getThisId(), m.getMcodeContent());
        			}
    		}
    	}
    	if(codes.size()>0){
    		//微代码
    		for(String code:codes){
    			if(allCode.containsKey(code)){
    				gradeCodeMap.put(code, allCode.get(code));
    			}
    		}
    	}
		return gradeCodeMap;
	}
	/**
	 * 某个学校某次考试某个年级下班级
	 * @param examId
	 * @param classType
	 * @param unitId
	 * @param gradeCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/common/classList")
	public Map<String,String> classList(String examId,String classType,String unitId,String gradeCode){
		Map<String, String> classMap = new LinkedHashMap<String, String>();
		if(StringUtils.isBlank(gradeCode)){
			return classMap;
		}
		//该考试下某个年级的所有学科
		List<SubjectInfo> subjectInfoList=subjectInfoService.findByUnitIdExamId(null, examId,gradeCode);
		Set<String> classIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(subjectInfoList)){
			Set<String> subjectInfoIds=new HashSet<String>();
        	for(SubjectInfo info:subjectInfoList){
        		subjectInfoIds.add(info.getId());
        	}
        	List<ClassInfo> classInfoList=classInfoService.findList(unitId,classType,null,subjectInfoIds.toArray(new String[]{}));
        	if(CollectionUtils.isNotEmpty(classInfoList)){
        		for(ClassInfo info:classInfoList){
        			classIds.add(info.getClassId());
            	}
        	}
		}
		if(classIds.size()>0){
			toMakeClassMap(classType, classIds.toArray(new String[]{}),classMap);
		}
		
        return classMap;
	}
	private void toMakeClassMap(String classType,String[] classIds,Map<String,String> classMap){
		if(classIds==null || classIds.length<=0){
			return;
		}
		if(ScoreDataConstants.CLASS_TYPE1.equals(classType)){
			//行政班
			List<Clazz> classList = SUtils.dt(classService.findListByIds(classIds), new TR<List<Clazz>>(){});
			if(CollectionUtils.isNotEmpty(classList)){
				for(Clazz z:classList){
					classMap.put(z.getId(), z.getClassNameDynamic());
				}
			}
		}else{
			List<TeachClass> tcalssList = SUtils.dt(teachClassService.findListByIds(classIds), new TR<List<TeachClass>>(){});
			if(CollectionUtils.isNotEmpty(tcalssList)){
				for(TeachClass t:tcalssList){
					//排除删除班级
					if(t.getIsDeleted()!=Constant.IS_TRUE){
						classMap.put(t.getId(), t.getName());
					}
					
				}
			}
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/scoreInfo/classList")
	public Map<String,String> getClassList(String examId,String classType,String unitId,String gradeCode,String noLimit){
		Map<String, String> classMap = classList(examId, classType, unitId, gradeCode);
		if(MapUtils.isEmpty(classMap)){
			return classMap;
		}
		List<String> noLimitTeacherIds = notLimitService.findTeacherIdByUnitId(unitId);
		if(StringUtils.isBlank(noLimit) && noLimitTeacherIds.contains(getLoginInfo().getOwnerId())){
			noLimit = "1";
		}
		//普通录分老师班级控制
		if(!"1".equals(noLimit)){
			
			Set<String> classIds = classMap.keySet();
			ExamInfo examInfo = examInfoService.findOne(examId);
			ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
			searchDto.setExamId(examId);
			searchDto.setAcadyear(examInfo.getAcadyear());
			searchDto.setSemester(examInfo.getSemester());
			searchDto.setUnitId(getLoginInfo().getUnitId());
			searchDto.setClassIds(classIds.toArray(new String[0]));
			searchDto.setTeacherId(getLoginInfo().getOwnerId());
			
			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
			if(CollectionUtils.isNotEmpty(limitList)){
				Set<String> limitClassIds = EntityUtils.getSet(limitList, ScoreLimit::getClassId);
				Set<String> keySet = classMap.keySet();
				Set<String> tempSet = new HashSet<String>();
				tempSet.addAll(keySet);
				for (String key : tempSet) {
					if(!limitClassIds.contains(key)){
						classMap.remove(key);
					}
				}
			}else{
				classMap = new HashMap<String, String>();
			}
		}
		return classMap;
	}

	/**
	 * 
	 * @param examId
	 * @param unitId
	 * @param gradeCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/common/subjectListByGrade")
	public Map<String, String> subjectListByGradeCode(String examId,String unitId,String gradeCode, String noLimit){
		Map<String, String> subMap = new HashMap<>();
		if(StringUtils.isEmpty(examId) || StringUtils.isEmpty(gradeCode)) {
			return subMap;
		}
		List<SubjectInfo> infoList = subjectInfoService.findByUnitIdExamId(unitId, examId,gradeCode);
    	if(CollectionUtils.isNotEmpty(infoList)){
    		
    		if(!"1".equals(noLimit)) {
    			ExamInfo examInfo = examInfoService.findOne(examId);
    			ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
    			searchDto.setExamId(examId);
    			searchDto.setAcadyear(examInfo.getAcadyear());
    			searchDto.setSemester(examInfo.getSemester());
    			searchDto.setUnitId(getLoginInfo().getUnitId());
    			searchDto.setTeacherId(getLoginInfo().getOwnerId());
    			List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(searchDto);
    			Set<String> subIds = EntityUtils.getSet(limitList, ScoreLimit::getSubjectId);
    			infoList = infoList.stream().filter(e->subIds.contains(e.getSubjectId())).collect(Collectors.toList());
    		}
    		Set<String> subjectIds = EntityUtils.getSet(infoList, SubjectInfo::getSubjectId);
    		if(CollectionUtils.isEmpty(subjectIds)) {
    			return subMap;
    		}
    		
    		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>(){});
    		Map<String, String> csMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
    		for(SubjectInfo si : infoList) {
    			if(csMap.containsKey(si.getSubjectId())) {
    				subMap.put(si.getId(), csMap.get(si.getSubjectId()));
    			}
    		}
    	}
		return subMap;
	}
}
