package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkChoiceDao;
import net.zdsoft.newgkelective.data.dto.ChoiceCourseDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoiceDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service("newGkChoiceService")
public class NewGkChoiceServiceImpl extends
		BaseServiceImpl<NewGkChoice, String> implements NewGkChoiceService {

	@Autowired
	private NewGkChoiceDao newGkChoiceDao;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
    private SemesterRemoteService semesterRemoteService;
	@Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
	@Autowired
	private NewGkChoCategoryService newGkChoCategoryService;

	@Override
	protected BaseJpaRepositoryDao<NewGkChoice, String> getJpaDao() {
		return newGkChoiceDao;
	}

	@Override
	protected Class<NewGkChoice> getEntityClass() {
		return NewGkChoice.class;
	}

	@Override
	public List<NewGkChoice> findListByGradeId(String gradeId) {
		return newGkChoiceDao.findByGradeId(gradeId);
	}
	
	public NewGkChoice findDefaultByGradeId(String gradeId) {
		List<NewGkChoice> chs = newGkChoiceDao.findDefaultByGradeId(gradeId);
		if(CollectionUtils.isEmpty(chs)) {
			return null;
		}
		return chs.get(0);
	}

	@Override
	public Integer getChoiceMaxTime(String unitId, String gradeId) {
		return newGkChoiceDao.getChoiceMaxTime(unitId, gradeId);
	}

	@Override
	public String saveNewGkChoice(NewGkChoice newGkChoice) {
		NewGkChoice save = newGkChoiceDao.save(newGkChoice);
		RedisUtils.del("NEW_GK_CHOICE_GRADE_LIST"+newGkChoice.getGradeId());
		return save.getId();
	}

	@Override
	public void saveNewGkChoice(NewGkChoice newGkChoice, List<NewGkChoRelation> newGkChoRelations, List<NewGkChoCategory> newGkChoCategoryList, String[] oldChoCategoryIds, String unitId) {
		this.saveNewGkChoice(newGkChoice);
		newGkChoRelationService.saveAndDeleteList(newGkChoRelations,null, unitId);
		newGkChoCategoryService.saveAndDeleteList(newGkChoCategoryList, oldChoCategoryIds);
	}

	@Override
	public void saveAndDeleteNewGkChoice(NewGkChoice newGkChoice,
			String choiceId) {
		newGkChoiceDao.deleteById(new Date(),choiceId);
		newGkChoiceDao.save(newGkChoice);
		RedisUtils.del("NEW_GK_CHOICE_GRADE_LIST"+newGkChoice.getGradeId());
	}

	@Override
	public void saveAndDeleteNewGkChoice(NewGkChoice newGkChoice, List<NewGkChoRelation> newGkChoRelations, List<NewGkChoCategory> newGkChoCategoryList, String[] oldChoCategoryIds, String choiceId, String unitId) {
		this.saveAndDeleteNewGkChoice(newGkChoice,
				choiceId);
		newGkChoRelationService.saveAndDeleteList(newGkChoRelations,
				choiceId, unitId);
		newGkChoCategoryService.saveAndDeleteList(newGkChoCategoryList, oldChoCategoryIds);
	}

	@Override
	public List<NewGkChoice> findByNowGradeId(String gradeId) {
		return newGkChoiceDao.findListByGradeIdAndNowTimeBetween(gradeId,
				new Date());
	}

	@Override
	public void deleteById(String unitId, String id) {
		NewGkChoice newGkChoice = newGkChoiceDao.findById(id).orElse(new NewGkChoice());
		//newgkelective_choice_relation(选课内关联)
		newGkChoRelationService.deleteByTypeChoiceIds(null, null, new String[]{id});
		//newgkelective_choice_result(选课结果)
		newGkChoResultService.deleteByStudentIds(unitId, id, null);
		//
		newGkChoiceDao.deleteById(new Date(),id);
		RedisUtils.del("NEW_GK_CHOICE_GRADE_LIST"+newGkChoice.getGradeId());
	}

	@Override
	public NewGkChoice findById(String id) {
		return newGkChoiceDao.findById(id).orElse(null);
	}

	@Override
	public  List<NewGkConditionDto> findSubRes(List<Course> courseList, Map<String, NewGkChoResultDto> dtoMap, Integer[][] resutl,int num){
		List<NewGkConditionDto> gkConditionList=new ArrayList<NewGkConditionDto>();
		NewGkConditionDto cDto = null;
        Set<String> linIds = null;
        String[] subNames = null;
        String shortName=null;
		String subjectName=null;
        String subjectIdstr=null;
//        String shortName1=null;
        for(int i = 0;i < resutl.length;i++){
        	cDto = new NewGkConditionDto();
        	shortName="";
			subjectName="";
			subjectIdstr="";
        	linIds = new HashSet<String>();
        	subNames = new String[resutl[i].length];
        	cDto.setSubjectIds(linIds);
        	cDto.setSubNames(subNames);
        	for(int j = 0;j < resutl[i].length;j++){
        		Course course=courseList.get(resutl[i][j]);
        		linIds.add(course.getId());
        		if(StringUtils.isBlank(subjectIdstr)){
        			subjectName=course.getSubjectName();
        			subjectIdstr=course.getId();
        		}else{
        			subjectName=subjectName+","+course.getSubjectName();
        			subjectIdstr=subjectIdstr+','+course.getId();
        		}
        		subNames[j] = course.getSubjectName();
        		shortName=shortName+course.getShortName();
        	}
        	cDto.setSubjectNames(subjectName);
        	cDto.setSubShortNames(shortName);
        	cDto.setSubjectIdstr(subjectIdstr);
        	cDto.setSumNum(0);
        	gkConditionList.add(cDto);
        }
        for (NewGkConditionDto item : gkConditionList) {
			for(Map.Entry<String, NewGkChoResultDto> entry : dtoMap.entrySet()){
				linIds = entry.getValue().getChooseSubjectIds();
				if(num == CollectionUtils.intersection(linIds,item.getSubjectIds()).size()){
					item.setSumNum(item.getSumNum()+1);
					item.getStuIds().add(entry.getKey());
				}
			}
			String[] sids = item.getSubjectIds().toArray(new String[0]);
			Arrays.sort(sids);
			item.setSubjectIdstr(StringUtils.join(sids, ","));
		}
        Collections.sort(gkConditionList, new Comparator<NewGkConditionDto>() {
          @Override
          public int compare(NewGkConditionDto o1, NewGkConditionDto o2) {
              return o2.getSumNum().compareTo(o1.getSumNum());
          }
        });  
        return gkConditionList;
	}
	
	@Override
	public List<NewGkChoiceDto> makeChoiceChart(String gradeId,List<NewGkChoice> newGkChoiceList,List<String> classIdList) {
		List<NewGkChoiceDto> newGkChoiceDtoList = new ArrayList<NewGkChoiceDto>();
		if(CollectionUtils.isEmpty(newGkChoiceList)) {
			return newGkChoiceDtoList;
		}
		
		//权限控制
		List<String> studentIdList = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(classIdList)){
			List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIdList.toArray(new String[classIdList.size()])),Student.class);
			if(CollectionUtils.isNotEmpty(studentList)){
				studentIdList = EntityUtils.getList(studentList, Student::getId);
			}else{
				return newGkChoiceDtoList;
			}
		}
		
		//获取课程列表
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(newGkChoiceList.get(0).getUnitId()),new TR<List<Course>>() {});
		Map<String, Course> courseMap = new LinkedHashMap<String, Course>();
		Map<String, Integer> courseOrder = new HashMap<String, Integer>();
		int order = 0;
		for (Course course : courseList) {
			courseMap.put(course.getId(), course);
			courseOrder.put(course.getSubjectName(), order ++);
		}
		
		//封装一个根据选课Id获取选取结果的map
		//第一个String 选课Id
		//第二个选课的课程subjectId
		Map<String,Map<String,Integer>> choiceResult = new LinkedHashMap<String,Map<String,Integer>>();
		Map<String,Integer> notmap=new HashMap<String,Integer>();//不参与选课分班的
		
		List<NewGkChoRelation> choRelations = newGkChoRelationService.findListByIn("choiceId", EntityUtils.getList(newGkChoiceList, NewGkChoice::getId).toArray(new String[0]));
		List<NewGkChoRelation> choiceCourse = choRelations.stream().filter(x -> NewGkElectiveConstant.CHOICE_TYPE_01.equals(x.getObjectType())).collect(Collectors.toList());
		
		for (NewGkChoRelation newGkChoRelation : choiceCourse) {
			String choiceId = newGkChoRelation.getChoiceId();
			String subjectId = newGkChoRelation.getObjectValue();
			if(!choiceResult.containsKey(choiceId)) {
				choiceResult.put(choiceId, new LinkedHashMap<String,Integer>());
			}
			if(!choiceResult.get(choiceId).containsKey(subjectId)){
				choiceResult.get(choiceId).put(subjectId, 0);
			}
		}
		//根据这些选课id集合拿选课结果
		List<Object[]> sumBySubjectIds;
		if(CollectionUtils.isNotEmpty(classIdList)){
			sumBySubjectIds = newGkChoResultService.findCountByChoiceIdAndKindTypeAndStudentIds(newGkChoiceList.get(0).getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,EntityUtils.getList(newGkChoiceList, NewGkChoice::getId).toArray(new String[0]),studentIdList.toArray(new String[0]));
		}else{
			sumBySubjectIds = newGkChoResultService.findCountByChoiceIdAndKindType(newGkChoiceList.get(0).getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,EntityUtils.getList(newGkChoiceList, NewGkChoice::getId).toArray(new String[0]));
		}
		
		for(Object[] sbs : sumBySubjectIds) {
			Integer sum = Integer.valueOf("" + sbs[0]);
			String sumChoiceId = sbs[1].toString();
			String sumSubjectId = sbs[2].toString();
			if(choiceResult.get(sumChoiceId).get(sumSubjectId) != null) {
				choiceResult.get(sumChoiceId).put(sumSubjectId, sum);
			}
		} 
		List<NewGkChoRelation> list = choRelations.stream().filter(x -> NewGkElectiveConstant.CHOICE_TYPE_04.equals(x.getObjectType())).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(classIdList)){
			final List<String> filterList = studentIdList;
			list = list.stream().filter(e->filterList.contains(e.getObjectValue())).collect(Collectors.toList());
		}
		if(CollectionUtils.isNotEmpty(list)){
			for(NewGkChoRelation ent : list){
				Integer val = notmap.get(ent.getChoiceId());
				if(val==null){
					val=0;
				}
				notmap.put(ent.getChoiceId(), val+1);
			}
		}
		// 总人数
		int totalNum;
		if(CollectionUtils.isNotEmpty(classIdList)){
			totalNum = studentIdList.size();
		}else{
			totalNum = (int)studentRemoteService.CountStudByGradeId(gradeId);
		}

		NewGkChoiceDto newGkChoiceDto;
		for (NewGkChoice newGkChoice : newGkChoiceList) {
			String choiceId = newGkChoice.getId();
			newGkChoiceDto = new NewGkChoiceDto();
			newGkChoiceDto.setChoiceName(newGkChoice.getChoiceName());
			newGkChoiceDto.setStartTime(newGkChoice.getStartTime());
			newGkChoiceDto.setEndTime(newGkChoice.getEndTime());
			newGkChoiceDto.setChoiceId(newGkChoice.getId());
			newGkChoiceDto.setGradeId(newGkChoice.getGradeId());
			newGkChoiceDto.setDefault(newGkChoice.getIsDefault() == 1);
			//设置科目
			//此处
			//防止没有选课数据
			Map<String, Integer> map = new HashMap<String, Integer>();
			if(choiceResult.containsKey(choiceId)){
				map=choiceResult.get(choiceId);
			}
			for (Map.Entry<String, Integer> entry : map.entrySet()) {  
				if(!courseMap.containsKey(entry.getKey())) {
					continue;
				}
				ChoiceCourseDto choiceCourseDto = new ChoiceCourseDto();
				choiceCourseDto.setCourse(courseMap.get(entry.getKey()));
				newGkChoiceDto.getChoiceCourseDtoList().add(choiceCourseDto);
			}  
			
			//封装人数
			int num = 0;
			
			List<String> subjectNames = new ArrayList<String>();
			List<String> subjectIds = new ArrayList<String>();
			List<ChoiceCourseDto> choiceCourseDtoList = newGkChoiceDto.getChoiceCourseDtoList();
			List<NewGkChoiceDto> dtos = new ArrayList<NewGkChoiceDto>();
			for (ChoiceCourseDto choiceCourseDto : choiceCourseDtoList) {
				String subjectId = choiceCourseDto.getCourse().getId();
				Integer tempNum = choiceResult.get(choiceId).get(subjectId);
				NewGkChoiceDto dto = new NewGkChoiceDto();
				dto.setChoiceName(courseMap.get(subjectId).getSubjectName());
				dto.setSelectNum(tempNum);
				dto.setChoiceId(subjectId);
				num += tempNum;
				dtos.add(dto);
			}
			Collections.sort(dtos, (x,y)->courseOrder.get(x.getChoiceName()).compareTo(courseOrder.get(y.getChoiceName())));
			JSONArray jsonArr=new JSONArray();
			JSONObject json1=null;
			for(NewGkChoiceDto dto : dtos) {
				subjectIds.add(dto.getChoiceId());
				json1 = new JSONObject();
				subjectNames.add(dto.getChoiceName());
//				json1.put("name", dto.getChoiceName());
				json1.put("value", dto.getSelectNum());
				jsonArr.add(json1);
			}
			
			// 封装选课人数和未选课人数-减去不参与的人数
			newGkChoiceDto.setSelectNum(num / newGkChoice.getChooseNum());
			Integer notint= notmap.get(newGkChoice.getId());
			if(notint!=null){
				newGkChoiceDto.setNoSelectNum(totalNum - num / newGkChoice.getChooseNum() -notmap.get(newGkChoice.getId()));
			}else{
				newGkChoiceDto.setNoSelectNum(totalNum - num / newGkChoice.getChooseNum());
			}
			//封装jsonObject
			JSONObject json=new JSONObject();
			
			json.put("legendIds", subjectIds.toArray(new String[0]));
			json.put("legendData", subjectNames.toArray(new String[0]));
			json.put("loadingData", jsonArr);
			String json2 = json.toString();
			newGkChoiceDto.setJsonObject(json2);
			newGkChoiceDtoList.add(newGkChoiceDto);
		}
		return newGkChoiceDtoList;
	}

    @Override
    public void saveDefault(NewGkChoice[] chs) {
        saveAll(chs);
       
        NewGkChoice defaultChoice = null;
        for (NewGkChoice one : chs) {
            if (one.getIsDefault() == 1) {
                defaultChoice = one;
            }
        }
        Semester semester=SUtils.dc(semesterRemoteService.getCurrentSemester(2, chs[0].getUnitId()),Semester.class);
        if(semester==null) {
        	return;
        }
        
        if (defaultChoice == null) {
        	//清空数据
            studentSelectSubjectRemoteService.updateStudentSelectsByGradeId(chs[0].getUnitId(), semester.getAcadyear(), semester.getSemester(), chs[0].getGradeId(), null);
            return;
        }
        List<NewGkChoResult> newGkChoResultList = newGkChoResultService.findByChoiceIdAndKindType(defaultChoice.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, defaultChoice.getId());
        List<StudentSelectSubject> studentSelectSubjectList = new ArrayList<>();
        for (NewGkChoResult one : newGkChoResultList) {
            StudentSelectSubject studentSelectSubject = new StudentSelectSubject();
            studentSelectSubject.setId(UuidUtils.generateUuid());
            studentSelectSubject.setSchoolId(defaultChoice.getUnitId());
            studentSelectSubject.setGradeId(defaultChoice.getGradeId());
            studentSelectSubject.setStudentId(one.getStudentId());
            studentSelectSubject.setSubjectId(one.getSubjectId());
            studentSelectSubject.setAcadyear(semester.getAcadyear());
            studentSelectSubject.setSemester(semester.getSemester());
            studentSelectSubject.setModifyTime(new Date());
            studentSelectSubject.setCreationTime(new Date());
            studentSelectSubject.setIsDeleted(0);
            studentSelectSubjectList.add(studentSelectSubject);
        }
        studentSelectSubjectRemoteService.updateStudentSelectsByGradeId(defaultChoice.getUnitId(), semester.getAcadyear(), semester.getSemester(), defaultChoice.getGradeId(), studentSelectSubjectList);
    }

    @Override
    public void deleteByGradeIds(String... gradeIds) {
        newGkChoiceDao.deleteByGradeIds(new Date(), gradeIds);
    }

	@Override
	public List<NewGkChoice> findListByGradeIdWithMaster(String gradeId) {
		return findListByGradeId(gradeId);
	}

	@Override
	public String checkSubjectIds(String choiceId, String subjectIds) {
		NewGkChoice gkChoice=this.findOne(choiceId);
		if(gkChoice==null) {
			return "选课方案不存在";
		}
        // 获取设置的选课数
        String[] courseIds = subjectIds.split(",");
        if (courseIds.length != gkChoice.getChooseNum()) {
            return "请选择" + gkChoice.getChooseNum() + "门课程";
        }
        Set<String[]> set = notChooseSubjects(gkChoice.getUnitId(),choiceId);
        //判断禁选组合
    	if(set!=null && set.size()>0){
    		//判断学生选择结果属于禁选组合
    		if(checkSubjects(courseIds, set)){
    			return "选中的是不推荐组合，请调整";
    		}
    	}

    	// 判断各类目选课数量是否在限定范围内
    	List<NewGkChoCategory> categoryList = newGkChoCategoryService.findByChoiceId(gkChoice.getUnitId(), choiceId);
    	List<List<String>> combinationList = new ArrayList<List<String>>();
    	if(CollectionUtils.isNotEmpty(categoryList)){
    		categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_2.equals(a.getCategoryType())).collect(Collectors.toList())
    		.forEach(e->combinationList.add(e.getCourseList()));
    		categoryList= categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_1.equals(a.getCategoryType())).collect(Collectors.toList());
    	}
    	
    	for (NewGkChoCategory category : categoryList) {
    		//判断选课类别限制
    		List<String> selectList = Arrays.stream(courseIds).collect(Collectors.toList());
    		int i=0;
    		//组合
    		List<List<String>> cList = category.getCourseLists();
    		if(CollectionUtils.isNotEmpty(cList)){
    			for(List<String> cl:cList){
    				if(CollectionUtils.isNotEmpty(cl)){
                		if(selectList.containsAll(cl)){
                			selectList.removeAll(cl);
                			i+=1;
                		}
            		}
    			}
    		}else{
    			if(CollectionUtils.isNotEmpty(combinationList)){
    				for(List<String> cl:combinationList){
        				if(CollectionUtils.isNotEmpty(cl)){
                    		if(selectList.containsAll(cl)){
                    			selectList.removeAll(cl);
                    		}
                		}
        			}
    			}
    		}
    		//单科
    		List<String> courseList = category.getCourseList();
    		if(CollectionUtils.isNotEmpty(courseList)){
				int size = CollectionUtils.intersection(courseList, selectList).size();
    			i+=size;
    		}
    		if(i>category.getMaxNum()){
    			return category.getCategoryName()+"类别最多选"+category.getMaxNum()+"门";
    		}else if(i<category.getMinNum()){
    			return category.getCategoryName()+"类别最少选"+category.getMinNum()+"门";
    		}
    		
    	}
    	return null;
	}
	
	private Set<String[]> notChooseSubjects(String unitId,String choiceId){
		List<String> limitChoList = newGkChoRelationService
				.findByChoiceIdAndObjectType(unitId,
						choiceId, NewGkElectiveConstant.CHOICE_TYPE_02);
    	if(CollectionUtils.isNotEmpty(limitChoList)){
    		Set<String[]> returnSet = new HashSet<String[]>();
    		for(String s:limitChoList){
    			returnSet.add(s.split(","));
    		}
    		return returnSet;
    	}else{
    		return new HashSet<String[]>();
    	}
	}
	
	private boolean checkSubjects(String[] checkSubjects,Set<String[]> subjectIds){
		for (String[] limitSubject : subjectIds) {
    		List<String> limitSubjectList = Arrays.asList(checkSubjects);
    		if (limitSubjectList.containsAll(Arrays.asList(limitSubject))) {
    			 return true;
    		}
    	}
		return false;
	}
}
