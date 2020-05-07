package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkChoResultDao;
import net.zdsoft.newgkelective.data.dao.NewGkChoResultJdbcDao;
import net.zdsoft.newgkelective.data.dto.ChosenSearchDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;

@Service("newGkChoResultService")
public class NewGkChoResultServiceImpl extends BaseServiceImpl<NewGkChoResult, String> implements NewGkChoResultService{
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkChoResultDao newGkChoResultDao;
	@Autowired
	private NewGkChoResultJdbcDao newGkChoResultJdbcDao;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Override
	protected BaseJpaRepositoryDao<NewGkChoResult, String> getJpaDao() {
		return newGkChoResultDao;
	}

	@Override
	protected Class<NewGkChoResult> getEntityClass() {
		return NewGkChoResult.class;
	}

	@Override
	public List<NewGkChoResult> findByChoiceIdAndStudentIdAndKindType(String unitId,String kindType,String choiceId,
			String studentId) {
		return newGkChoResultDao.findByUnitIdAndKindTypeInAndChoiceIdAndStudentId(unitId,new String[]{kindType},choiceId,studentId);
	}
	@Override
	public List<String> findByChoiceIdAndSubjectIdAndKindType(String unitId,String kindType,String choiceId,
			String subjectId) {
		return newGkChoResultDao.findStudentBySubjectAndKindType(unitId,kindType,choiceId,subjectId);
	}
	@Override
	public List<NewGkChoResult> findByChoiceIdAndKindType(String unitId,String kindType,String choiceId){
		return newGkChoResultDao.findByUnitIdAndKindTypeInAndChoiceId(unitId,new String[]{kindType},choiceId);
	}
	@Override
	public Set<String> findSetByChoiceIdAndKindType(String unitId,String kindType,String choiceId){
		Set<String> studentIds=new HashSet<String>();
        List<NewGkChoResult> resultList=findByChoiceIdAndKindType(unitId,kindType,choiceId);
	    if(CollectionUtils.isNotEmpty(resultList)){
	    	studentIds = EntityUtils.getSet(resultList, NewGkChoResult::getStudentId);
	    }
	    return studentIds;
	}
	@Override
	public Map<String,Set<String>>  findMapByChoiceIdAndKindType(String unitId,String kindType,String choiceId){
		List<NewGkChoResult> resultList=findByChoiceIdAndKindType(unitId,kindType,choiceId);
		//获取学生对应的选课ids
		Map<String,Set<String>> stuIdOfSubIdsMap=new HashMap<String,Set<String>>();
		if(CollectionUtils.isNotEmpty(resultList)){
			for(NewGkChoResult result:resultList){
				Set<String> subjectIds=stuIdOfSubIdsMap.get(result.getStudentId());
				if(CollectionUtils.isEmpty(subjectIds)){
					subjectIds=new HashSet<String>();
					stuIdOfSubIdsMap.put(result.getStudentId(), subjectIds);
				}
				subjectIds.add(result.getSubjectId());
				
			}
		}
		return stuIdOfSubIdsMap;
		
}
	@Override
	public void saveNewGkChoResult(String unitId, String choiceId, String studentId,
			List<String> subjectIds,String[] subjectTypes,String[] wantToIds,String[] noWantToIds) {
		newGkChoResultJdbcDao.deleteByChoiceIdAndStudentIdIn(unitId,choiceId,new String[]{studentId});
		List<NewGkChoResult> newGkChoResultList = new ArrayList<NewGkChoResult>(subjectIds.size());
		int i=0;
		NewGkChoResult newGkChoResult = null;
		for (String subjectId : subjectIds) {
			newGkChoResult = new NewGkChoResult();
			newGkChoResult.setId(UuidUtils.generateUuid());
			newGkChoResult.setUnitId(unitId);
			newGkChoResult.setChoiceId(choiceId);
			newGkChoResult.setStudentId(studentId);
			newGkChoResult.setSubjectId(subjectId);
			newGkChoResult.setSubjectType(subjectTypes[i]);
			newGkChoResult.setKindType(NewGkElectiveConstant.KIND_TYPE_01);
			newGkChoResult.setCreationTime(new Date());
			newGkChoResult.setModifyTime(new Date());
			newGkChoResultList.add(newGkChoResult);
			i++;
		}
		if(wantToIds!=null){
			for (String wantToId : wantToIds) {
				newGkChoResult = new NewGkChoResult();
				newGkChoResult.setId(UuidUtils.generateUuid());
				newGkChoResult.setUnitId(unitId);
				newGkChoResult.setChoiceId(choiceId);
				newGkChoResult.setStudentId(studentId);
				newGkChoResult.setSubjectId(wantToId);
				newGkChoResult.setKindType(NewGkElectiveConstant.KIND_TYPE_02);
				newGkChoResult.setCreationTime(new Date());
				newGkChoResult.setModifyTime(new Date());
				newGkChoResultList.add(newGkChoResult);
			}
		}
		if(noWantToIds!=null){
			for (String noWantToId : noWantToIds) {
				newGkChoResult = new NewGkChoResult();
				newGkChoResult.setId(UuidUtils.generateUuid());
				newGkChoResult.setUnitId(unitId);
				newGkChoResult.setChoiceId(choiceId);
				newGkChoResult.setStudentId(studentId);
				newGkChoResult.setSubjectId(noWantToId);
				newGkChoResult.setKindType(NewGkElectiveConstant.KIND_TYPE_03);
				newGkChoResult.setCreationTime(new Date());
				newGkChoResult.setModifyTime(new Date());
				newGkChoResultList.add(newGkChoResult);
			}
		}
		newGkChoResultJdbcDao.insertBatch(newGkChoResultList);
		RedisUtils.del("NEW_GK_CHORESULT_THREE"+choiceId);
	}

	@Override
	public List<StudentResultDto> findNotChosenList(String unitId,String choiceId, ChosenSearchDto dto) {
		Set<String> chooseStudentIds=findSetByChoiceIdAndKindType(unitId, NewGkElectiveConstant.KIND_TYPE_01,choiceId);
		//用于后面赋值
//		Map<String,String> clazzNameMap=new HashMap<String,String>();
		List<StudentResultDto> returnList=new ArrayList<StudentResultDto>();
		if(CollectionUtils.isEmpty(chooseStudentIds)) {
			chooseStudentIds=new HashSet<>();
		}
		//所有学生
		List<Student> studentList=findBySearch(dto);
		
		if(CollectionUtils.isEmpty(studentList)){
			return returnList;
		}
		StudentResultDto studentDto=null;
		List<String> noJoinStudentIds = newGkChoRelationService.findByChoiceIdAndObjectType(unitId, choiceId, NewGkElectiveConstant.CHOICE_TYPE_04);
		for(Student s:studentList){
			if(chooseStudentIds.contains(s.getId())){
				continue;
			}
			studentDto=toMakeDto(s);
//			studentDto.setClassName(clazzNameMap.get(studentDto.getClassId()));
			if(noJoinStudentIds.contains(s.getId())){
				studentDto.setNojoinChoose(NewGkElectiveConstant.IF_1);
			}
			returnList.add(studentDto);
		}
		return returnList;
	}
	
	private StudentResultDto toMakeDto(Student student){
		StudentResultDto studentDto=new StudentResultDto();
		studentDto.setClassId(student.getClassId());
		studentDto.setSex(student.getSex()==null?"":String.valueOf(student.getSex()));
		studentDto.setStudentCode(student.getStudentCode());
		studentDto.setStudentId(student.getId());
		studentDto.setStudentName(student.getStudentName());
		studentDto.setClassName(student.getClassName());
		return studentDto;
	}

	public List<StudentResultDto> findGradeIdList(String unitId,String gradeId,String choiceId, String subids){
		Map<String, List<NewGkChoResult>> resultMap = this.findByChoiceId(unitId,new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03},choiceId);
		if(MapUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(resultMap.get(NewGkElectiveConstant.KIND_TYPE_01))){
			return new ArrayList<StudentResultDto>();
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
		});
		Map<String,String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		List<NewGkChoResult> resultList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_01);

		//key:studentId 
		Map<String,List<String>> subjectByStudentId=new HashMap<String, List<String>>();
		//key=studentId_subjectId
		Map<String,String> subjectTypeByStudentId = new HashMap<String, String>();
		Set<String> subjectIds=new HashSet<String>();
		for(NewGkChoResult result:resultList){
			if(!subjectByStudentId.containsKey(result.getStudentId())){
				subjectByStudentId.put(result.getStudentId(), new ArrayList<String>());
			}
			subjectByStudentId.get(result.getStudentId()).add(result.getSubjectId());
			subjectIds.add(result.getSubjectId());
			subjectTypeByStudentId.put(result.getStudentId()+"_"+result.getSubjectId(), result.getSubjectType());
		}

		//科目赋值
		if (CollectionUtils.isNotEmpty(subjectIds) && CollectionUtils.isNotEmpty(courseList)) {
			Iterator<Course> iter = courseList.iterator();
			while (iter.hasNext()) {
				Course item = iter.next();
				if (!subjectIds.contains(item.getId())) {
					iter.remove();
				}
			}
		}
		//所有学生
		List<Student> studentList = RedisUtils.getObject("STUALLGRAD"+gradeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Student>>(){}, new RedisInterface<List<Student>>(){
				@Override
				public List<Student> queryData() {
					List<Student> list = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, gradeId, null, null), new TR<List<Student>>(){});
					if(CollectionUtils.isNotEmpty(list)){
						Grade g= gradeRemoteService.findOneObjectById(gradeId);
						Set<String> clids=EntityUtils.getSet(list, Student::getClassId);
						List<Clazz>clist =classRemoteService.findListObjectByIds(clids.toArray(new String[0]));
						Map<String,String> clMap = EntityUtils.getMap(clist, Clazz::getId,Clazz::getClassName);
						for(Student ent:list){
							ent.setClassName(g.getGradeName()+clMap.get(ent.getClassId()));
						}
					}
					return list;
				}
	        });
		
		List<StudentResultDto> returnList=new ArrayList<StudentResultDto>();
		if(CollectionUtils.isEmpty(studentList)){
			return returnList;
		}
		StudentResultDto studentDto=null;
		List<String> chooseList=null;
		String[] chooses=null;
		NewGkChoResult rr=null;
		List<NewGkChoResult> resultList1;
		boolean isSubjectSearch=false;
		String[] subjectSearch =null;
		if(StringUtils.isNotBlank(subids)){
			isSubjectSearch=true;
			subjectSearch = subids.split(",");
		}
		for(Student s:studentList){
			if(!subjectByStudentId.containsKey(s.getId())){
				continue;
			}
			studentDto=toMakeDto(s);
			chooseList = subjectByStudentId.get(s.getId());
			if(isSubjectSearch && subjectSearch!=null && subjectSearch.length>0){
				//判断chooses 是不是有选择这两个
				if(checkIsSubjectSearchIn(chooseList,subjectSearch)){
//					chooses=Arrays.copyOf(subjectSearch, subjectSearch.length);
					chooses = chooseList.toArray(new String[]{});
				}else{
					continue;
				}
				
			}else{
				chooses = chooseList.toArray(new String[]{});
			}
			//排序
			Arrays.sort(chooses);
			studentDto.setCourseIds(chooses);
			resultList1 = new ArrayList<NewGkChoResult>();
			for (int i = 0; i < chooses.length; i++) {
				if (courseNameMap.containsKey(chooses[i])) {
					rr = new NewGkChoResult();
					rr.setSubjectId(chooses[i]);
					rr.setSubjectName(courseNameMap.get(chooses[i]));
					rr.setSubjectType(subjectTypeByStudentId.get(s.getId()+"_"+chooses[i]));
					resultList1.add(rr);
				}
			}
			studentDto.setResultList(resultList1);
			returnList.add(studentDto);
		}
		return returnList;
	}
	@Override
	public List<StudentResultDto> findChosenList(String unitId,String choiceId,
			ChosenSearchDto dto) {
		Map<String, List<NewGkChoResult>> resultMap = this.findByChoiceId(unitId,new String[]{NewGkElectiveConstant.KIND_TYPE_01,NewGkElectiveConstant.KIND_TYPE_02,NewGkElectiveConstant.KIND_TYPE_03},choiceId);
		if(MapUtils.isEmpty(resultMap) || CollectionUtils.isEmpty(resultMap.get(NewGkElectiveConstant.KIND_TYPE_01))){
			return new ArrayList<StudentResultDto>();
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(dto.getUnitId()), new TR<List<Course>>() {
		});
		Map<String,String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
		List<NewGkChoResult> resultList = resultMap.get(NewGkElectiveConstant.KIND_TYPE_01);

		//key:studentId 
		Map<String,List<String>> subjectByStudentId=new HashMap<String, List<String>>();
		//key=studentId_subjectId
		Map<String,String> subjectTypeByStudentId = new HashMap<String, String>();
		Set<String> subjectIds=new HashSet<String>();
		for(NewGkChoResult result:resultList){
			if(!subjectByStudentId.containsKey(result.getStudentId())){
				subjectByStudentId.put(result.getStudentId(), new ArrayList<String>());
			}
			subjectByStudentId.get(result.getStudentId()).add(result.getSubjectId());
			subjectIds.add(result.getSubjectId());
			subjectTypeByStudentId.put(result.getStudentId()+"_"+result.getSubjectId(), result.getSubjectType());
		}

		//科目赋值
		if (CollectionUtils.isNotEmpty(subjectIds) && CollectionUtils.isNotEmpty(courseList)) {
			Iterator<Course> iter = courseList.iterator();
			while (iter.hasNext()) {
				Course item = iter.next();
				if (!subjectIds.contains(item.getId())) {
					iter.remove();
				}
			}
		}
		//所有学生
		List<Student> studentList=findBySearch(dto);
		List<StudentResultDto> returnList=new ArrayList<StudentResultDto>();
		if(CollectionUtils.isEmpty(studentList)){
			return returnList;
		}
		StudentResultDto studentDto=null;
		List<String> chooseList=null;
		String[] chooses=null;
		NewGkChoResult rr=null;
		List<NewGkChoResult> resultList1;
		boolean isSubjectSearch=false;
		String[] subjectSearch =null;
		if(StringUtils.isNotBlank(dto.getSubjectIds())){
			isSubjectSearch=true;
			subjectSearch = dto.getSubjectIds().split(",");
		}
		for(Student s:studentList){
			if(!subjectByStudentId.containsKey(s.getId())){
				continue;
			}
			studentDto=toMakeDto(s);
			chooseList = subjectByStudentId.get(s.getId());
			if(isSubjectSearch && subjectSearch!=null && subjectSearch.length>0){
				//判断chooses 是不是有选择这两个
				if(checkIsSubjectSearchIn(chooseList,subjectSearch)){
//					chooses=Arrays.copyOf(subjectSearch, subjectSearch.length);
					chooses = chooseList.toArray(new String[]{});
				}else{
					continue;
				}
				
			}else{
				chooses = chooseList.toArray(new String[]{});
			}
			//排序
			Arrays.sort(chooses);
			studentDto.setCourseIds(chooses);
			resultList1 = new ArrayList<NewGkChoResult>();
			for (int i = 0; i < chooses.length; i++) {
				if (courseNameMap.containsKey(chooses[i])) {
					rr = new NewGkChoResult();
					rr.setSubjectId(chooses[i]);
					rr.setSubjectName(courseNameMap.get(chooses[i]));
					rr.setSubjectType(subjectTypeByStudentId.get(s.getId()+"_"+chooses[i]));
					resultList1.add(rr);
				}
			}
			studentDto.setResultList(resultList1);
			returnList.add(studentDto);
		}
		return returnList;
	}
	
	
	private boolean checkIsSubjectSearchIn(List<String> chooseList,
			String[] subjectSearch) {
		for(String s:subjectSearch){
			if(!chooseList.contains(s)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据班级，学号，性别，姓名 取得学生列表
	 * @param dto
	 * @param clazzNameMap(只用赋值)
	 * @return
	 */
	private List<Student> findBySearch(ChosenSearchDto dto){
		//组装其他的要求
		Student searchStudent = new Student();
		if(ChosenSearchDto.SEARCH_NAME_1.equals(dto.getSearchType())) {
			// 姓名
        	if(StringUtils.isNotBlank(dto.getSearchTex())){
        		//防止注入
        		String linStr = dto.getSearchTex().replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentName(linStr+ "%");
        	}
		}else if(ChosenSearchDto.SEARCH_CODE_2.equals(dto.getSearchType())){	
        	// 学号模糊查询
        	if(StringUtils.isNotBlank(dto.getSearchTex())){
        		//防止注入
        		String linStr = dto.getSearchTex().replaceAll("%", "");
        		//右匹配
        		searchStudent.setStudentCode(linStr+ "%");
        	}
        }
		//性别
		if(StringUtils.isNotBlank(dto.getSex())){
			searchStudent.setSex(Integer.parseInt(dto.getSex()));
		}
		String[] classIds=null;
		if(StringUtils.isNotBlank(dto.getClassIds())){
			classIds=dto.getClassIds().split(",");
		}
		List<Student> stuList = Student.dt(studentRemoteService.findByClaIdsLikeStuCodeNames(dto.getUnitId(),dto.getGradeId(), classIds, Json.toJSONString(searchStudent)));
		return stuList;
	}

	@Override
	public void saveAllResult(String unitId,String choiceId,List<NewGkChoResult> resultList, String[] stuIds,
			String[] noJoinStuIds,String[] moveNoJoinStuIds) {
		Set<String> delnoJoinStu=new HashSet<String>();
		if(ArrayUtils.isNotEmpty(moveNoJoinStuIds)){
			for(String s:moveNoJoinStuIds){
				delnoJoinStu.add(s);
			}
		}
		
		if(ArrayUtils.isNotEmpty(noJoinStuIds)){
			List<String> oldStu = newGkChoRelationService.findByChoiceIdAndObjectType(unitId, choiceId, NewGkElectiveConstant.CHOICE_TYPE_04);
			NewGkChoRelation re=null;
			List<NewGkChoRelation> reList = new ArrayList<NewGkChoRelation>();
			for(String s:noJoinStuIds){
				if(!oldStu.contains(s)){
					re=new NewGkChoRelation();
					re.setChoiceId(choiceId);
					re.setCreationTime(new Date());
					re.setId(UuidUtils.generateUuid());
					re.setModifyTime(new Date());
					re.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_04);
					re.setObjectValue(s);
					re.setUnitId(unitId);
					delnoJoinStu.remove(s);
					reList.add(re);
				}
			}
			if(CollectionUtils.isNotEmpty(reList)){
				newGkChoRelationService.saveAndDeleteByList(reList,null,unitId,NewGkElectiveConstant.CHOICE_TYPE_04);
			}
		}
		if(stuIds!=null && stuIds.length>0){
			deleteByStudentIds(unitId,choiceId, stuIds);
		}
		
		if(CollectionUtils.isNotEmpty(delnoJoinStu)){
			newGkChoRelationService.deleteByChoiceIdType(unitId, choiceId, NewGkElectiveConstant.CHOICE_TYPE_04, delnoJoinStu.toArray(new String[]{}));
		}
		
		if(CollectionUtils.isNotEmpty(resultList)){
			newGkChoResultJdbcDao.insertBatch(resultList);
		}	
		
		//去除缓存
		RedisUtils.del("NEW_GK_CHO_RESULT_LIST"+choiceId+NewGkElectiveConstant.KIND_TYPE_01);
		RedisUtils.del("NEW_GK_CHO_RESULT_MAP"+choiceId);
		RedisUtils.delBeginWith("NEW_GK_CHORELATION_STU_UNPARTIN_");
	}

	@Override
	public void saveLock(String unitId,String choiceId, boolean isLock, String[] studentIds) {
		if(isLock){
			NewGkChoRelation newGkChoRelation=null;
			List<NewGkChoRelation> reList = new ArrayList<NewGkChoRelation>();
			if(ArrayUtils.isNotEmpty(studentIds)){
				for (String studentId : studentIds) {
					newGkChoRelation = new NewGkChoRelation();
					newGkChoRelation.setChoiceId(choiceId);
					newGkChoRelation.setCreationTime(new Date());
					newGkChoRelation.setId(UuidUtils.generateUuid());
					newGkChoRelation.setModifyTime(new Date());
					newGkChoRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_05);
					newGkChoRelation.setObjectValue(studentId);
					newGkChoRelation.setUnitId(unitId);
					reList.add(newGkChoRelation);
				}
			}else{
				List<NewGkChoResult> resultList=newGkChoResultDao.findByUnitIdAndKindTypeInAndChoiceId(unitId,new String[]{NewGkElectiveConstant.KIND_TYPE_01},choiceId);
				Set<String> chooseStudentIds=new HashSet<String>();
				if(CollectionUtils.isNotEmpty(resultList)){
					chooseStudentIds=EntityUtils.getSet(resultList, NewGkChoResult::getStudentId);
					List<String> oldIds = newGkChoRelationService.findByChoiceIdAndObjectType(unitId, choiceId, NewGkElectiveConstant.CHOICE_TYPE_05);
					chooseStudentIds.removeAll(oldIds);
					if(chooseStudentIds.size()>0){
						for(String s:chooseStudentIds){
							 newGkChoRelation = new NewGkChoRelation();
							 newGkChoRelation.setChoiceId(choiceId);
							 newGkChoRelation.setCreationTime(new Date());
							 newGkChoRelation.setId(UuidUtils.generateUuid());
							 newGkChoRelation.setModifyTime(new Date());
							 newGkChoRelation.setObjectType(NewGkElectiveConstant.CHOICE_TYPE_05);
							 newGkChoRelation.setObjectValue(s);
							 newGkChoRelation.setUnitId(unitId);
							 reList.add(newGkChoRelation);
						}
					}
				}
				
			}
			if(CollectionUtils.isNotEmpty(reList)){
				newGkChoRelationService.saveAndDeleteByList(reList,null,unitId,NewGkElectiveConstant.CHOICE_TYPE_05);
			}

		}else{
//			newGkChoRelationService.deleteByChoiceIdType(choiceId,NewGkElectiveConstant.CHOICE_TYPE_05, StringUtils.trimToNull(studentId));
			if(ArrayUtils.isEmpty(studentIds)){
				newGkChoRelationService.deleteByChoiceIdType(unitId,choiceId, NewGkElectiveConstant.CHOICE_TYPE_05);
			}else{
				newGkChoRelationService.deleteByChoiceIdType(unitId,choiceId,NewGkElectiveConstant.CHOICE_TYPE_05, studentIds);
			}

		}
	}

	@Override
	public Map<String, Integer> getCountMapByGradeIdAndKindType(String gradeId,String kindType) {
		return RedisUtils.getObject("NEW_GK_CHORESULT_COUNT"+gradeId, RedisUtils.TIME_SHORT_CACHE, new TypeReference<Map<String, Integer>>(){}, new RedisInterface<Map<String, Integer>>(){
			@Override
			public Map<String, Integer> queryData() {
				Map<String, Integer> map =new HashMap<String, Integer>();
				List<Object[]> strs= newGkChoResultDao.countByGradeIdAndKindType(gradeId,kindType);
				if(CollectionUtils.isNotEmpty(strs)){
					for(Object[] sts:strs){
						long num=(Long)sts[1];
						int num1=(int)num;
						map.put((String)sts[0], num1);
					}
				}
				return map;
			}
        });
		
	}

	@Override
	public void deleteByStudentIds(String unitId,String choiceId, String[] studentIds) {
		if(studentIds!=null && studentIds.length>0){
			if(studentIds.length<=1000){
				newGkChoResultJdbcDao.deleteByChoiceIdAndStudentIdIn(unitId,choiceId,studentIds);
			}else{
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
					newGkChoResultJdbcDao.deleteByChoiceIdAndStudentIdIn(unitId,choiceId,stuId);
				}
			}
			RedisUtils.del("NEW_GK_CHO_RESULT_LIST"+choiceId+NewGkElectiveConstant.KIND_TYPE_01);
			RedisUtils.del("NEW_GK_CHO_RESULT_MAP"+choiceId);
		}
	}

	@Override
	public List<NewGkChoResult> findByKindTypeAndChoiceIdAndStudentIds(String unitId,String kindType,String choiceId,
			String[] studentIds) {
		if(studentIds!=null && studentIds.length>0){
			if(studentIds.length<=1000){
				return newGkChoResultDao.findByUnitIdAndKindTypeAndChoiceIdAndStudentIdIn(unitId,kindType,choiceId,studentIds);
			}else{
				List<NewGkChoResult> list=new ArrayList<NewGkChoResult>();
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
					List<NewGkChoResult> list1=newGkChoResultDao.findByUnitIdAndKindTypeAndChoiceIdAndStudentIdIn(unitId,kindType,choiceId,stuId);
					if(CollectionUtils.isNotEmpty(list1)){
						list.addAll(list1);
					}
					
				}
				return list;
			}
			
		}else{
			return new ArrayList<NewGkChoResult>();
		}
		
	}

	@Override
	public List<NewGkChoResult> findByGradeIdAndKindType(String gradeId,String kindType) {
		return RedisUtils.getObject("NEW_GK_CHO_RESULT_LIST"+gradeId, RedisUtils.TIME_HALF_MINUTE, new TypeReference<List<NewGkChoResult>>(){}, new RedisInterface<List<NewGkChoResult>>(){
			@Override
			public List<NewGkChoResult> queryData() {
				return newGkChoResultDao.findByGradeIdAndKindType(gradeId,kindType);
			}
        });
	}
	
	@Override
	public List<NewGkChoResult> findByGradeIdAndKindType(String unitId,String kindType,String[] choiceIds) {
		return RedisUtils.getObject("NEW_GK_CHO_RESULT_LIST"+EntityUtils.getCode(JSONObject.toJSONString(choiceIds)), RedisUtils.TIME_HALF_MINUTE, new TypeReference<List<NewGkChoResult>>(){}, new RedisInterface<List<NewGkChoResult>>(){
			@Override
			public List<NewGkChoResult> queryData() {
				return newGkChoResultDao.findByGradeIdAndKindType(unitId,kindType,choiceIds);
			}
		});
	}
	
	@Override
	public List<Object[]> findCountByChoiceIdAndKindType(String unitId,String kindType,String[] choiceIds) {
		return newGkChoResultDao.findCountByChoiceIdAndKindType(unitId,kindType,choiceIds);
	}
	
	@Override
	public List<Object[]> findCountByChoiceIdAndKindTypeAndStudentIds(String unitId,String kindType,String[] choiceIds, String[] studentIds) {
		return newGkChoResultDao.findCountByChoiceIdAndKindTypeAndStudentIds(unitId,kindType,choiceIds,studentIds);
	}

	@Override
	public int findStudentNumByChoiceIdAndKindType(String unitId,String kindType,String choiceId) {
		return newGkChoResultDao.findCountByKindTypeAndChoiceId(unitId,kindType,choiceId);
	}

	@Override
	public Map<String, List<NewGkChoResult>> findMapByChoiceIdAndStudentId(String unitId,String[] kindTypes,String choiceId, String studentId) {
			List<NewGkChoResult> resultList = newGkChoResultDao.findByUnitIdAndKindTypeInAndChoiceIdAndStudentId(unitId,kindTypes,choiceId, studentId);
			Map<String, List<NewGkChoResult>> resultMap = new HashMap<String, List<NewGkChoResult>>();
			if(CollectionUtils.isNotEmpty(resultList)){
				for (NewGkChoResult result : resultList) {
					if(!resultMap.containsKey(result.getKindType())){
						resultMap.put(result.getKindType(), new ArrayList<NewGkChoResult>());
					}
					resultMap.get(result.getKindType()).add(result);
				}
			}
			return resultMap;
	}

	@Override
	public Map<String, List<NewGkChoResult>> findByChoiceId(String unitId,String[] kindTypes,String choiceId) {
		Map<String, List<NewGkChoResult>> resultMap = new HashMap<String, List<NewGkChoResult>>();
		List<NewGkChoResult> resultList = newGkChoResultDao.findByUnitIdAndKindTypeInAndChoiceId(unitId,kindTypes,choiceId);
		if(CollectionUtils.isNotEmpty(resultList)){
			for (NewGkChoResult result : resultList) {
				if(!resultMap.containsKey(result.getKindType())){
					resultMap.put(result.getKindType(), new ArrayList<NewGkChoResult>());
				}
				resultMap.get(result.getKindType()).add(result);
			}
		}
		return resultMap;
	}

	@Override
	public Map<String,Set<String>> findStuIdListByChoiceIdAndSubjectId(String unitId,String choiceId, String[] subjectId,boolean isXuankao) {
		Map<String,Set<String>> map=new HashMap<>();
		if(isXuankao) {
			List<NewGkChoResult> list = newGkChoResultDao.findStuIdListByChoiceIdAndSubjectId(unitId,NewGkElectiveConstant.KIND_TYPE_01,choiceId,subjectId);
			if(CollectionUtils.isEmpty(list)) {
				return new HashMap<>();
			}
			for(NewGkChoResult r:list) {
				if(!map.containsKey(r.getSubjectId())) {
					map.put(r.getSubjectId(), new HashSet<>());
				}
				map.get(r.getSubjectId()).add(r.getStudentId());
			}
		}else {
			List<NewGkChoResult> list = newGkChoResultDao.findByUnitIdAndKindTypeInAndChoiceId(unitId, new String[]{NewGkElectiveConstant.KIND_TYPE_01},choiceId);
			Map<String,Set<String>> stuMap=new HashMap<>();
			for(NewGkChoResult r:list) {
				if(!stuMap.containsKey(r.getStudentId())) {
					stuMap.put(r.getStudentId(), new HashSet<>());
				}
				stuMap.get(r.getStudentId()).add(r.getSubjectId());
			}
			String stuId;
			Set<String> chooseSub;
			for(String sub: subjectId) {
				map.put(sub, new HashSet<>());
			}
			for(Entry<String, Set<String>> stuSub:stuMap.entrySet()) {
				stuId = stuSub.getKey();
				chooseSub = stuSub.getValue();
				for(String sub: subjectId) {
					if(!chooseSub.contains(sub)) {
						map.get(sub).add(stuId);
					}
				}
			}
		}
		
		return map;
	}

	@Override
	public String getCount(String unitId,String choiceId,List<String> courseIds, boolean isReverse) {
		return RedisUtils.getObject("NEW_GK_CHORESULT_THREE"+choiceId, RedisUtils.TIME_HALF_MINUTE, new TypeReference<String>(){},new RedisInterface<String>() {
			@Override
			public String queryData() {
				List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), new TR<List<Course>>(){});
				
				//获取所有的选课结果
				List<NewGkChoResult>  resultList=findByChoiceIdAndKindType(unitId,NewGkElectiveConstant.KIND_TYPE_01,choiceId);
				//已选学生ids
				Set<String> chosenStuIds=new HashSet<String>();
				NewGkChoResultDto dto=new NewGkChoResultDto();	
				//获取学生对应的选课ids
				Map<String,NewGkChoResultDto> dtoMap=new HashMap<String,NewGkChoResultDto>();
				Map<String, Integer> courseIdCountMap= new HashMap<String, Integer>();
				if(CollectionUtils.isNotEmpty(resultList)){
					for(NewGkChoResult result:resultList){
						chosenStuIds.add(result.getStudentId());
						if(!dtoMap.containsKey(result.getStudentId())){
							dto = new NewGkChoResultDto();
			                dto.setChooseSubjectIds(new HashSet<String>());
			                dto.setStudentId(result.getStudentId());
			                dtoMap.put(result.getStudentId(), dto);
			            }
			            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
			            
			            Integer integer = courseIdCountMap.get(result.getSubjectId());
				    	if(integer == null){
				    		integer = 0;
				    	}
				    	courseIdCountMap.put(result.getSubjectId(), ++integer);
					}
				}
				Map<String, Integer> courseNameCountMap = new HashMap<String, Integer>();
				for(Course course:courseList){
					Integer integer = courseIdCountMap.get(course.getId());
					if(integer==null) integer=0;
					courseNameCountMap.put(course.getSubjectName(), integer);
				}
		      
		        //各科目选择结果
		        courseNameCountMap = sortByValue(courseNameCountMap);
		        //计算组合
		        Integer[] cSize = new Integer[courseList.size()];
		        for(int i = 0;i < courseList.size();i++){
		        	cSize[i] = i;
		        }
		        
		        //三科目选择结果
		        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
		        Integer[][] result = combineAlgorithm.getResutl();
		        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
		        JSONObject json=new JSONObject();
		        JSONObject json2=null;
		        JSONArray jsonArr = new JSONArray();
		        List<String> subjectNames=new ArrayList<String>();
				if(CollectionUtils.isNotEmpty(newConditionList3)){
					if(isReverse){
						Collections.reverse(newConditionList3);
					}
					for(int i=newConditionList3.size()-1;i>=0;i--){
						NewGkConditionDto newDto=newConditionList3.get(i);
						json2=new JSONObject();
						subjectNames.add(newDto.getSubShortNames());
						json2.put("value", newDto.getSumNum());
						json2.put("name", newDto.getSubShortNames());
						json2.put("subjectId", newDto.getSubjectIdstr());
						jsonArr.add(json2);
					}
				}
				json.put("legendData", subjectNames.toArray(new String[0]));
				json.put("loadingData", jsonArr);
				String jsonStringData=json.toString();
				return jsonStringData;
			}
		});
	}

    @Override
    public void deleteByStudentIds(String... stuids) {
        newGkChoResultDao.deleteByStudentIdIn(stuids);
    }

    @Override
    public void deleteBySubjectIds(String... subids) {
        newGkChoResultDao.deleteBySubjectIdIn(subids);
    }

    private Map<String, Integer> sortByValue(Map<String, Integer> map) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

	@Override
	public List<StudentResultDto> findNotChosenListWithMaster(String unitId, String choiceId, ChosenSearchDto dto) {
		return findNotChosenList(unitId, choiceId, dto);
	}

	@Override
	public List<StudentResultDto> findChosenListWithMaster(String unitId, String choiceId, ChosenSearchDto dto) {
		return findChosenList(unitId, choiceId, dto);
	}

	@Override
	public Map<String, List<NewGkChoResult>> findMapByChoiceIdAndStudentIdWithMaster(String unitId, String[] kindTypes, String choiceId, String studentId) {
		return findMapByChoiceIdAndStudentId(unitId, kindTypes, choiceId, studentId);
	}

	@Override
	public Map<String, Map<String, List<NewGkChoResult>>> findByStudentIdAndChoiceIds(String unitId, String studentId, String[] choiceIds) {
		List<NewGkChoResult> resultList = newGkChoResultDao.findByUnitIdAndStudentIdAndChoiceIdIn(unitId,studentId,choiceIds);
		if(resultList==null){
			return new HashMap<String, Map<String,List<NewGkChoResult>>>();
		}
		Map<String, List<NewGkChoResult>> listMap = EntityUtils.getListMap(resultList, NewGkChoResult::getChoiceId, Function.identity());
		HashMap<String, Map<String, List<NewGkChoResult>>> resultMap = new HashMap<String, Map<String,List<NewGkChoResult>>>();
		for (Entry<String, List<NewGkChoResult>> entry : listMap.entrySet()) {
			List<NewGkChoResult> value = entry.getValue();
			if(CollectionUtils.isNotEmpty(value)){
				Map<String, List<NewGkChoResult>> listMap2 = EntityUtils.getListMap(value, NewGkChoResult::getKindType, Function.identity());
				resultMap.put(entry.getKey(), listMap2);
			}
		}
		return resultMap;
	}

}
