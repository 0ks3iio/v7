package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGKStudentRangeDto;
import net.zdsoft.newgkelective.data.dto.NewGKStudentRangeExDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkOpenSubjectService;

@Controller
@RequestMapping("/newgkelective/{divideId}")
public class NewGKStudentRangeExAction extends BaseAction{
	
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGKStudentRangeService newGKStudentRangeService;
	@Autowired
	private NewGKStudentRangeExService newGKStudentRangeExService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private NewGkOpenSubjectService newGkOpenSubjectService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	
	//莱州中学定制逻辑
	private static boolean isLZZX=false;
	
	@RequestMapping("/singleList/page")
	@ControllerInfo(value = "分班下一步-全走班单科分层模式")
	public String showSingleList(@PathVariable String divideId, ModelMap map){
		NewGkDivide divide = newGkDivideService.findById(divideId);
		if(divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
    		return errorFtl(map, "分班记录不存在或已被删除！");
    	}
		map.put("gradeId",divide.getGradeId());
		NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
		if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
    		return errorFtl(map, "选课记录不存在或已被删除！");
    	}
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)){
			return errorFtl(map, "选课的科目记录不存在或已被删除！");
		}
		//allChoose 小于chooseNum 暂时不考虑
		int allChoose = subjectIdList.size();
		int chooseNum=choice.getChooseNum()==null?3:choice.getChooseNum();
	
		int subjectANums=0;
		int subjectBNums=0;
		
		Set<String> subIds=new HashSet<String>();
		List<String> subjectAIds = new ArrayList<String>();
		List<String> subjectBIds = new ArrayList<String>();
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[]{NewGkElectiveConstant.SUBJECT_TYPE_B,NewGkElectiveConstant.SUBJECT_TYPE_A});
		for (NewGkOpenSubject openSubject : openSubjectList) {
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(openSubject
					.getSubjectType())) {
				subjectAIds.add(openSubject.getSubjectId());
			} else {
				subjectBIds.add(openSubject.getSubjectId());
			}
			subIds.add(openSubject.getSubjectId());
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService
				.findListByIds(subIds.toArray(new String[] {})),
				new TR<List<Course>>() {
				});
		Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,Course::getId);
		
		Map<String,Integer> stuNumABySubjectId=new HashMap<String,Integer>();
		Map<String,Integer> stuNumBBySubjectId=new HashMap<String,Integer>();
		//学生选课
		List<StudentResultDto> stuChooselist = newGkChoResultService
				.findGradeIdList(divide.getUnitId(),divide.getGradeId(),divide.getChoiceId(), null);
		
		for(StudentResultDto s:stuChooselist){
			List<NewGkChoResult> rList = s.getResultList();
			if(CollectionUtils.isEmpty(rList)){
				System.out.println(s.getStudentName()+"没有选课");
				continue;
			}
			Set<String> chooseSet = EntityUtils.getSet(rList, NewGkChoResult::getSubjectId);
			for(String ss:chooseSet){
				if(subjectAIds.contains(ss)){
					if(!stuNumABySubjectId.containsKey(ss)){
						stuNumABySubjectId.put(ss, 1);
					}else{
						stuNumABySubjectId.put(ss, stuNumABySubjectId.get(ss)+1);
					}
				}
				
			}
			for(String bId:subjectBIds){
				if(!chooseSet.contains(bId)){
					if(!stuNumBBySubjectId.containsKey(bId)){
						stuNumBBySubjectId.put(bId, 1);
					}else{
						stuNumBBySubjectId.put(bId, stuNumBBySubjectId.get(bId)+1);
					}
				}
			}
			
		}
		//选考
		List<NewGKStudentRange> allStuRangeList = newGKStudentRangeService.findByDivideId(divide.getUnitId(), divideId);
		Map<String,Map<String,Integer>> stuNumMap=new HashMap<String,Map<String,Integer>>();
		
		if(CollectionUtils.isNotEmpty(allStuRangeList)){
			for(NewGKStudentRange range:allStuRangeList){
				if(!stuNumMap.containsKey(range.getSubjectId()+"_"+range.getSubjectType())){
					stuNumMap.put(range.getSubjectId()+"_"+range.getSubjectType(), new HashMap<String,Integer>());
				}
				Map<String, Integer> map1 = stuNumMap.get(range.getSubjectId()+"_"+range.getSubjectType());
				String key=range.getSubjectId()+"_"+range.getSubjectType()+"_"+range.getRange();
				if(!map1.containsKey(key)){
					map1.put(key, 1);
				}else{
					map1.put(key, map1.get(key)+1);
				}
			}
		}
		List<NewGKStudentRangeEx> allExList = newGKStudentRangeExService.findByDivideId(divideId);
		//返回值

		
		Map<String,NewGKStudentRangeEx> aex=new HashMap<String,NewGKStudentRangeEx>();
		
		Map<String,NewGKStudentRangeEx> bExMap=new HashMap<String,NewGKStudentRangeEx>();
		Map<String,NewGKStudentRangeEx> cExMap=new HashMap<String,NewGKStudentRangeEx>();
		Map<String,NewGKStudentRangeEx> ysyExMap=new HashMap<String,NewGKStudentRangeEx>();
		
		if(CollectionUtils.isNotEmpty(allExList)){
		
			for(NewGKStudentRangeEx ex:allExList){
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(ex.getSubjectType())){
					aex.put(ex.getSubjectId()+"_"+ex.getSubjectType()+"_"+ex.getRange(), ex);
				}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(ex.getSubjectType())){
					bExMap.put(ex.getSubjectId(), ex);
				}else if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(ex.getSubjectType())) {
					ysyExMap.put(ex.getSubjectId()+"_"+ex.getSubjectType()+"_"+ex.getRange(), ex);
				}else if("C".equals(ex.getSubjectType())) {
					cExMap.put(ex.getSubjectId(), ex);
				}
			}
		}
		NewGKStudentRangeDto dto;
		NewGKStudentRangeEx ex;
		
		
		List<NewGKStudentRangeDto> aDtoList=new ArrayList<NewGKStudentRangeDto>();
		
		
		List<NewGKStudentRangeEx> bExList=new ArrayList<NewGKStudentRangeEx>();
		for (NewGkOpenSubject openSubject : openSubjectList) {
			if(!subjectNameMap.containsKey(openSubject.getSubjectId())){
				continue;
			}
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(openSubject
					.getSubjectType())) {
				//A
				Map<String, Integer> oneMap = stuNumMap.get(openSubject.getSubjectId()+"_"+NewGkElectiveConstant.SUBJECT_TYPE_A);
				if(oneMap==null){
					continue;
				}
				dto=new NewGKStudentRangeDto();
				dto.setExList(new ArrayList<NewGKStudentRangeEx>());
				dto.setSubjectId(openSubject.getSubjectId());
				dto.setSubjectName(subjectNameMap.get(openSubject.getSubjectId()).getSubjectName());
				dto.setSubjectType(openSubject.getSubjectId());
				if(stuNumABySubjectId.containsKey(openSubject.getSubjectId())){
					dto.setStuNum(stuNumABySubjectId.get(openSubject.getSubjectId()));
				}else{
					dto.setStuNum(0);
				}
				aDtoList.add(dto);
				
				for(Entry<String, Integer> one:oneMap.entrySet()){
					String key = one.getKey();
					String[] arr = key.split("_");
					ex=aex.get(key);
					if(ex==null){
						ex=new NewGKStudentRangeEx();
					}
					ex.setSubjectId(openSubject.getSubjectId());
					ex.setRange(arr[2]);
					ex.setStuNum(one.getValue());
					dto.getExList().add(ex);
				}
				if(CollectionUtils.isNotEmpty(dto.getExList())){
					Collections.sort(dto.getExList(), new Comparator<NewGKStudentRangeEx>() {
						@Override
						public int compare(NewGKStudentRangeEx o1, NewGKStudentRangeEx o2) {
							if(o1 == null || o2 == null) {
								
								return 0;
							}
							if(o1.getSubjectId().equals(o2.getSubjectId())){
								return o1.getRange().compareTo(o2.getRange());
							}
							return o1.getSubjectId().compareTo(o2.getSubjectId());
						}
					});
				}
				subjectANums++;
				
			} else {
				//B
				ex=bExMap.get(openSubject.getSubjectId());
				if(ex==null){
					ex=new NewGKStudentRangeEx();
					ex.setSubjectId(openSubject.getSubjectId());
					ex.setSubjectType(openSubject.getSubjectType());
				}
				ex.setSubjectName(subjectNameMap.get(openSubject.getSubjectId()).getSubjectName());
				if(stuNumBBySubjectId.containsKey(openSubject.getSubjectId())){
					ex.setStuNum(stuNumBBySubjectId.get(openSubject.getSubjectId()));
				}else{
					ex.setStuNum(0);
				}
				bExList.add(ex);
				subjectBNums++;
			}
			
		}
		
//		if(false) {
//			//TODO 
//			Set<String> studentIds = EntityUtils.getSet(stuChooselist, "studentId");
//			List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);
//			List<NewGKStudentRangeDto> ysyDtoList = new ArrayList<>();
//			for (Course course : ysyCourses) {
//				Map<String, Integer> oneMap = stuNumMap.get(course.getId()+"_"+NewGkElectiveConstant.SUBJECT_TYPE_O);
//				if(oneMap==null){
//					continue;
//				}
//				dto = new NewGKStudentRangeDto();
//				dto.setSubjectId(course.getId());
//				dto.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_O);
//				dto.setSubjectName(course.getSubjectName());
//				dto.setStuNum(studentIds.size());
//				dto.setExList(new ArrayList<>());
//				for (String key : oneMap.keySet()) {
//					String[] arr = key.split("_");
//					ex=ysyExMap.get(key);
//					if(ex==null){
//						ex=new NewGKStudentRangeEx();
//					}
//					ex.setSubjectId(course.getId());
//					ex.setRange(arr[2]);
//					ex.setSubjectType(dto.getSubjectType());
//					ex.setStuNum(oneMap.get(key));
//					dto.getExList().add(ex);
//				}
//				
//				ysyDtoList.add(dto);
//			}
//			aDtoList.addAll(ysyDtoList);
//			
//			List<NewGkChoiceEx> choiceExList = choiceExService.findByIn("choiceId", new String[] {choice.getId()});
//			Map<String, List<NewGkChoiceEx>> choiceExMap = EntityUtils.getListMap(choiceExList, "subjectId", null);
//			
//			List<NewGKStudentRangeEx> list = new ArrayList<>();
//			for (NewGKStudentRangeEx e : bExList) {
//				ex = cExMap.get(e.getSubjectId());
//				if(ex == null) {
//					ex = new NewGKStudentRangeEx();
//					ex.setDivideId(e.getDivideId());
//					ex.setSubjectId(e.getSubjectId());
//					ex.setSubjectType("C");
//		}
//				ex.setSubjectName(e.getSubjectName());
//		
//				List<NewGkChoiceEx> list2 = choiceExMap.get(e.getSubjectId());
//				if(CollectionUtils.isNotEmpty(list2)) {
//					ex.setStuNum(list2.size());
//					e.setStuNum(e.getStuNum() - list2.size());
//				}else {
//					ex.setStuNum(0);
//				}
//				
//				list.add(e);
//				list.add(ex);
//			}
//			bExList = list;
//		}
		
		map.put("floNum", divide.getMaxGalleryful());
		map.put("aDtoList", aDtoList);
		map.put("bExList", bExList);
		//判断是否分班完成
		boolean canEdit = false;
		if(!NewGkElectiveConstant.IF_1.equals(divide.getStat())){
			//判断是否正在分班
			String key = NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId;
			if(RedisUtils.get(key)!=null && "start".equals(RedisUtils.get(key))){
				map.put("haveDivideIng", true);
			}else{
				map.put("haveDivideIng", false);
				canEdit = true;
			}
		}
		if(subjectANums>0 && subjectANums<chooseNum){
			divide.setBatchCountTypea(subjectANums);
		}else{
			divide.setBatchCountTypea(chooseNum);
		}
		if(subjectBNums>0 && subjectBNums<(allChoose-chooseNum)){
			divide.setBatchCountTypeb(subjectBNums);
		}else{
			divide.setBatchCountTypeb(allChoose-chooseNum);
		}
		
		
		map.put("divide", divide);
		map.put("gradeId", divide.getGradeId());
		
		
		map.put("canEdit", canEdit);
		
		//需要
		if(isLZZX) {
			map.put("isxzbNum", true);
		}else {
			map.put("isxzbNum", false);
		}
		
		return "/newgkelective/divideGroup/singleArrangeList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/singleList/save")
    @ControllerInfo(value = "分班下一步-全走班单科分层模式-保存")
	public String saveSingleList(@PathVariable String divideId,NewGKStudentRangeExDto saveDto, ModelMap map){
		if(CollectionUtils.isEmpty(saveDto.getExList())){
			return success("没有可保存的数据");
		}
		if(isNowDivide(divideId)){
			return error("正在开班中，不能操作！");
		}
		try {
			NewGkDivide divide = newGkDivideService.findById(divideId);
			if(divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
	    		return error("分班记录不存在或已被删除！");
	    	}
			if(NewGkElectiveConstant.IF_1.equals(divide.getStat())){
				return error("已经开班完成，不能操作！");
			}
			if(saveDto.getBatchCountTypea()!=null) {
				divide.setBatchCountTypea(saveDto.getBatchCountTypea());
			}
			if(saveDto.getBatchCountTypeb()!=null) {
				divide.setBatchCountTypeb(saveDto.getBatchCountTypeb());
			}

			newGkDivideService.save(divide);
			for (NewGKStudentRangeEx rangeEx : saveDto.getExList()) {
				if(StringUtils.isBlank(rangeEx.getId())){
					rangeEx.setId(UuidUtils.generateUuid());
				}
				rangeEx.setModifyTime(new Date());
			}
			newGKStudentRangeExService.saveAndDelete(saveDto.getExList(),divideId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}

	@ResponseBody
	@RequestMapping("/singleList/threeOneTwo/save")
	@ControllerInfo(value = "分班下一步-全走班单科分层模式-保存")
	public String saveSingle312List(@PathVariable String divideId, String subjectType, NewGKStudentRangeExDto saveDto) {
		if (CollectionUtils.isEmpty(saveDto.getExList())) {
			return success("没有可保存的数据");
		}
		if (isNowDivide(divideId)) {
			return error("正在开班中，不能操作！");
		}
		try {
			NewGkDivide divide = newGkDivideService.findById(divideId);
			if (divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
				return error("分班记录不存在或已被删除！");
			}
			if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
				return error("已经开班完成，不能操作！");
			}
			if (NewGkElectiveConstant.DIVIDE_TYPE_09.equals(divide.getOpenType())) {
				if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
					divide.setFollowType(saveDto.getFollowType());
				}
			} else if (NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())) {
				if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
					if (divide.getFollowType() != null && divide.getFollowType().contains("B")) {
						String tmp = divide.getFollowType();
						divide.setFollowType(saveDto.getFollowType() + "," + tmp.substring(tmp.indexOf("B")));
					} else {
						divide.setFollowType(saveDto.getFollowType());
					}
				}
				if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
					String tmp = divide.getFollowType();
					if (tmp == null) {
						return error("请重组选考班");
					}
					if (tmp.contains("B")) {
						divide.setFollowType(tmp.substring(0, tmp.indexOf("B")) + saveDto.getFollowType());
					} else {
						divide.setFollowType(tmp + "," + saveDto.getFollowType());
					}
				}
			}
			if (saveDto.getBatchCountTypea() != null) {
				divide.setBatchCountTypea(saveDto.getBatchCountTypea());
			}
			if (saveDto.getBatchCountTypeb() != null) {
				divide.setBatchCountTypeb(saveDto.getBatchCountTypeb());
			}

			newGkDivideService.save(divide);
			for (NewGKStudentRangeEx rangeEx : saveDto.getExList()) {
				if (StringUtils.isBlank(rangeEx.getId())) {
					rangeEx.setId(UuidUtils.generateUuid());
				}
				rangeEx.setModifyTime(new Date());
			}
			newGKStudentRangeExService.saveAndDelete(saveDto.getExList(), subjectType, divideId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	
	/**
	 * 是否正在分班中
	 * 
	 * @return
	 */
	public boolean isNowDivide(String divideId) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId;
//		final String keyA = NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_A";
//		final String keyB = NewGkElectiveConstant.DIVIDE_CLASS+"_"+divideId+"_B";
//		if(RedisUtils.get(key)!=null && ("error".equals(RedisUtils.get(keyA)) || "error".equals(RedisUtils.get(keyB)))){
//			RedisUtils.set(key, "error");
//		}
//		if(RedisUtils.get(key)!=null && ("success".equals(RedisUtils.get(keyA)) && "success".equals(RedisUtils.get(keyB)))){
//			RedisUtils.set(key, "success");
//		}
		if (RedisUtils.get(key) != null && "start".equals(RedisUtils.get(key))) {
			return true;
		}
		return false;
	}

}
