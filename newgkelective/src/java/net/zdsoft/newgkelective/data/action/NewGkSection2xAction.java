package net.zdsoft.newgkelective.data.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkSectionEndDto;
import net.zdsoft.newgkelective.data.dto.Section2xSaveDto;
import net.zdsoft.newgkelective.data.dto.Section2xSaveResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkSection;
import net.zdsoft.newgkelective.data.entity.NewGkSectionBegin;
import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCount;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCountInput;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.api.Sectioning2A1X;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning2.api.Sectioning2A1XInput;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.service.NewGkDivideService;
import net.zdsoft.newgkelective.data.service.NewGkSectionBeginService;
import net.zdsoft.newgkelective.data.service.NewGkSectionEndService;
import net.zdsoft.newgkelective.data.service.NewGkSectionResultService;
import net.zdsoft.newgkelective.data.service.NewGkSectionService;

/**
 * 
 * 2+x算法
 *
 */
@Controller
@RequestMapping("/newgkelective/{divideId}/section2x")
public class NewGkSection2xAction extends BaseAction{
	@Autowired
	private NewGkDivideService newGkDivideService;
	@Autowired
	private NewGkSectionService newGkSectionService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private NewGkSectionBeginService newGkSectionBeginService;
	@Autowired
	private NewGkSectionResultService newGkSectionResultService;
	@Autowired
	private NewGkSectionEndService newGkSectionEndService;
	
	@RequestMapping("/index/page")
	@ControllerInfo(value = "2+x分班列表")
	public String showSection2xIndex(@PathVariable String divideId,String type,ModelMap map){
		List<NewGkSection> list = newGkSectionService.findListBy("divideId", divideId);
		map.put("divideId",divideId);
		if(CollectionUtils.isNotEmpty(list) || "1".equals(type)) {
			map.put("sectionList", list);
			return "/newgkelective/section2x/section2xIndex.ftl";
		}else {
			//新增的部分
			return showSection2xAdd(divideId,map);
		}
	}
	@RequestMapping("/add/page")
	@ControllerInfo(value = "2+x新增")
	public String showSection2xAdd(@PathVariable String divideId,ModelMap map){
		//newgkelective_section newgkelective_section_begin newgkelective_section_result
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		String unitId=getLoginInfo().getUnitId();
		Date nowTime=new Date();
		//统计数据
		NewGkSection newGkSection = new NewGkSection();
		newGkSection.setCreationTime(nowTime);
		newGkSection.setModifyTime(nowTime);
		newGkSection.setDivideId(divideId);
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:dd");
		newGkSection.setSectionName(ft.format(nowTime)+" 2+x分班");
		newGkSection.setId(UuidUtils.generateUuid());
		newGkSection.setStat("0");//未开始
		//选课结果
		String choiceId=divide.getChoiceId();
		List<NewGkChoResult> stuChooseList = newGkChoResultService.findByChoiceIdAndKindType(unitId, NewGkElectiveConstant.KIND_TYPE_01, choiceId);
		List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkSectionResult> resultList=new ArrayList<>();
		List<NewGkSectionBegin> beginList=new ArrayList<>();
		makeStudentChooseList(nowTime, newGkSection.getId(), stuChooseList, classList, resultList, beginList);
		try {
			
			newGkSectionService.saveSection(newGkSection,beginList,resultList);
			map.put("newGkSection", newGkSection);
			Map<String,List<NewGkSectionBegin>> mapBySubjectType=EntityUtils.getListMap(beginList, e->e.getSubjectType(), e->e);
			map.put("dtoList3", mapBySubjectType.get(NewGkElectiveConstant.SUBJECT_TYPE_3));
			map.put("dtoList2", mapBySubjectType.get(NewGkElectiveConstant.SUBJECT_TYPE_2));
			map.put("isNow", false);
			return "/newgkelective/section2x/section2xEdit.ftl";
		}catch (Exception e) {
			e.printStackTrace();
			//保存报错
			return "/newgkelective/section2x/section2xError.ftl";
		}
		
	}
	
	@RequestMapping("/edit/page")
	@ControllerInfo(value = "2+x修改")
	public String showSection2xEdit(@PathVariable String divideId,String sectionId,ModelMap map){
		NewGkSection newGkSection = newGkSectionService.findOne(sectionId);
		map.put("newGkSection", newGkSection);
		List<NewGkSectionBegin> beginList = newGkSectionBeginService.findListBy("sectionId", sectionId);
		Map<String,List<NewGkSectionBegin>> mapBySubjectType=EntityUtils.getListMap(beginList, e->e.getSubjectType(), e->e);
		map.put("dtoList3", mapBySubjectType.get(NewGkElectiveConstant.SUBJECT_TYPE_3));
		map.put("dtoList2", mapBySubjectType.get(NewGkElectiveConstant.SUBJECT_TYPE_2));
		if(isNowArrange(sectionId)) {
			map.put("isNow", true);
		}else {
			map.put("isNow", false);
		}
		return "/newgkelective/section2x/section2xEdit.ftl";

	}
	
	@RequestMapping("/result/page")
	@ControllerInfo(value = "2+x分班列表")
	public String showSection2xResult(@PathVariable String divideId,String sectionId,ModelMap map){
		map.put("divideId",divideId);
		NewGkSection newGkSection = newGkSectionService.findOne(sectionId);
		map.put("newGkSection", newGkSection);
		String unitId=getLoginInfo().getUnitId();
		//3科
		List<NewGkSectionBegin> beginList = newGkSectionBeginService.findListBy(new String[] {"sectionId","subjectType"}, new String[] {sectionId,NewGkElectiveConstant.SUBJECT_TYPE_3});
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId),Course.class);
		Map<String,Course> courseMap=EntityUtils.getMap(courseList, e->e.getId());
		map.put("courseMap", courseMap);
		//结果
		List<NewGkSectionEnd> endList = newGkSectionEndService.findListBy("sectionId", sectionId);
		Map<String,Integer> endArrange=new HashMap<>();
		Map<String,String> subjectName3Map=new HashMap<>();
		List<NewGkSectionEndDto> dtoList=new  ArrayList<>();
		NewGkSectionEndDto dto;
		Map<String,NewGkSectionEnd> endMap=EntityUtils.getMap(endList,e->e.getId());
		
		//所有结果
		List<NewGkSectionResult> oldResultList = newGkSectionResultService.findListBy("sectionId", sectionId);
		List<NewGkSectionEndDto> resultDtoList=new  ArrayList<>();//结果
		
		Map<String,Integer> arrangeByEndId=new HashMap<>();
		Map<String,List<String[]>> groupNameByEndId=new  HashMap<>();
		Map<String,Integer> numBySubjectIds=new HashMap<>();
		Map<String,Integer> xSubjectNumMap=new  HashMap<>();
		if(CollectionUtils.isNotEmpty(oldResultList)) {
			for(NewGkSectionResult r:oldResultList){
				dto=new NewGkSectionEndDto();
				dto.setItemId(r.getId());
				dto.setGroupName(r.getGroupName());//开设班级名称
				dto.setResultSubs(r.getSubjectIds());//班级组合
				String[] arr = r.getSubjectIds().split("_");
				dto.setResultSubNames(makeName(courseMap,arr));
				dto.setSubject3Map(new HashMap<>());
				dto.setArrangeType(r.getArrangeType());
				dto.setSubjectType(r.getSubjectType());
				String[] arrs=r.getStudentScource().split(",");
				int allnum=0;
				for(String ss:arrs) {
					String[] arr1 = ss.split("-");
					int ssNum=Integer.parseInt(arr1[1]);
					dto.getSubject3Map().put(arr1[0], ssNum);
					allnum=allnum+Integer.parseInt(arr1[1]);
					if(subjectName3Map.containsKey(arr1[0])) {
						
					}else {
						subjectName3Map.put(arr1[0], makeName(courseMap, arr1[0].split("_")));
					}
					if(numBySubjectIds.containsKey(arr1[0])) {
						numBySubjectIds.put(arr1[0], numBySubjectIds.get(arr1[0])+ssNum);
					}else {
						numBySubjectIds.put(arr1[0], ssNum);
					}
					if(r.getSubjectIds().equals(ss)) {
						//3+0
						continue;
					}else {
						//ss去除r.getSubjectIds()剩余x
						String x=showLeftX(arr1[0],r.getSubjectIds());
						if(StringUtils.isNotBlank(x)) {
							if(!xSubjectNumMap.containsKey(x)) {
								xSubjectNumMap.put(x, ssNum);
							}else {
								xSubjectNumMap.put(x,  xSubjectNumMap.get(x)+ssNum);
							}
						}
					}
				}
				dto.setAllStudengNum(allnum);
				
				if(endMap.containsKey(r.getGroupClassId())) {
					dto.setSubjectIds(endMap.get(r.getGroupClassId()).getSubjectIds());//班级组合
					String[] arr1 = endMap.get(r.getGroupClassId()).getSubjectIds().split("_");
					dto.setSubjectNames(makeName(courseMap,arr1));
					if(!arrangeByEndId.containsKey(r.getGroupClassId())) {
						arrangeByEndId.put(r.getGroupClassId(), allnum);
						groupNameByEndId.put(r.getGroupClassId(), new ArrayList<>());
						groupNameByEndId.get(r.getGroupClassId()).add(new String[] {r.getId(),r.getGroupName()});
					}else {
						arrangeByEndId.put(r.getGroupClassId(), arrangeByEndId.get(r.getGroupClassId())+allnum);
						groupNameByEndId.get(r.getGroupClassId()).add(new String[] {r.getId(),r.getGroupName()});
					}
				}
				
				resultDtoList.add(dto);
			}
		}
		map.put("resultList", resultDtoList);
		if(CollectionUtils.isNotEmpty(endList)) {
			for(NewGkSectionEnd end:endList) {
 				dto=new NewGkSectionEndDto();
 				dto.setItemId(end.getId());
 				dto.setSubjectIds(end.getSubjectIds());
				String[] arr = end.getSubjectIds().split("_");
				dto.setSubjectNames(makeName(courseMap,arr));
				dto.setOpenClassNum(end.getOpenClassNum());
				dto.setClazzList(new ArrayList<>());
				if(groupNameByEndId.containsKey(end.getId())) {
					dto.getClazzList().addAll(groupNameByEndId.get(end.getId()));
				}
				
				dto.setSubject3Map(new HashMap<>());
				String[] arrs=end.getStudentScource().split(",");
				int allnum=0;
				for(String ss:arrs) {
					String[] arr1 = ss.split("-");
					dto.getSubject3Map().put(arr1[0], Integer.parseInt(arr1[1]));
					int ssNum=Integer.parseInt(arr1[1]);
 					allnum=allnum+ssNum;
					if(endArrange.containsKey(arr1[0])) {
						endArrange.put(arr1[0], endArrange.get(arr1[0])+ssNum);
					}else {
						endArrange.put(arr1[0], ssNum);
						subjectName3Map.put(arr1[0], makeName(courseMap, arr1[0].split("_")));
					}
					//r.getSubjectIds()去除ss剩余x
					String x=showLeftX(arr1[0],end.getSubjectIds());
					if(StringUtils.isNotBlank(x)) {
						if(!xSubjectNumMap.containsKey(x)) {
							xSubjectNumMap.put(x, ssNum);
						}else {
							xSubjectNumMap.put(x,  xSubjectNumMap.get(x)+ssNum);
						}
					}
				}
				if(arrangeByEndId.containsKey(end.getId())) {
					allnum=allnum+arrangeByEndId.get(end.getId());
				}
				dto.setAllStudengNum(allnum);
				dtoList.add(dto);
			}
		}
		for(NewGkSectionBegin item:beginList) {
			//已安排人数重新计算 原来的值保存的还是arrangeType=0的值
			if(numBySubjectIds.containsKey(item.getSubjectIds())) {
				item.setArrangeNum(numBySubjectIds.get(item.getSubjectIds()));
			}else {
				item.setArrangeNum(0);
			}
			if(endArrange.containsKey(item.getSubjectIds())) {
				item.setLeftArrangeNum(endArrange.get(item.getSubjectIds()));
			}else {
				item.setLeftArrangeNum(0);
			}
		}
		
		
		map.put("subjectName3Map", subjectName3Map);
		map.put("beginList", beginList);
		map.put("dtoList", dtoList);
		
		List<String[]> xList=new ArrayList<>();
		
		for(Course c:courseList) {
			if(xSubjectNumMap.containsKey(c.getId())) {
				xList.add(new String[] {c.getId(),c.getSubjectName(),""+xSubjectNumMap.get(c.getId())});
			}else {
				xList.add(new String[] {c.getId(),c.getSubjectName(),"0"});
			}
			
		}
		map.put("xList", xList);
		return "/newgkelective/section2x/section2xResult.ftl";
	}
	
	private String showLeftX(String sub3,String sub2) {
		String[] arr3 = sub3.split("_");
		for(String r3:arr3) {
			if(sub2.indexOf(r3)>-1) {
				continue;
			}
			return r3;
		}
		return  null;
	}
	
	
	private String makeName(Map<String,Course> courseMap,String[] arr) {
		String names="";
		for(String s:arr) {
			if(StringUtils.isBlank(courseMap.get(s).getShortName())) {
				names=names+courseMap.get(s).getSubjectName();
			}else {
				names=names+courseMap.get(s).getShortName();
			}
		}
		return names;
	}
	
	private void makeStudentChooseList(Date nowTime,String sectionId,List<NewGkChoResult> stuChooseList,List<NewGkDivideClass> classList,
			List<NewGkSectionResult> resultList,List<NewGkSectionBegin> beginList) {
		NewGkSectionResult newGkSectionResult;
		Map<String,List<String>> stuChooseMap=EntityUtils.getListMap(stuChooseList, NewGkChoResult::getStudentId, e->e.getSubjectId());;
		
		//1:已经安排的3+0 2+x 学生
		Set<String> allArrangeStudent=new HashSet<>();
		Map<String,Integer> numMap;
		List<String> chooseList;
		if(CollectionUtils.isNotEmpty(classList)) {
			for(NewGkDivideClass item:classList) {
				newGkSectionResult=new NewGkSectionResult();
				newGkSectionResult.setCreationTime(nowTime);
				newGkSectionResult.setGroupClassId(item.getId());
				newGkSectionResult.setSectionId(sectionId);
				newGkSectionResult.setArrangeType("0");
				newGkSectionResult.setId(UuidUtils.generateUuid());
				newGkSectionResult.setModifyTime(nowTime);
				newGkSectionResult.setSubjectType(item.getSubjectType());
				newGkSectionResult.setGroupName(item.getClassName());
				String arr = item.getSubjectIds();
				arr=arr.replace(",", "_");
				newGkSectionResult.setSubjectIds(arr);
				numMap=new HashMap<String,Integer>();
				for(String stuId:item.getStudentList()) {
					allArrangeStudent.add(stuId);
					if(stuChooseMap.containsKey(stuId)){
						chooseList = stuChooseMap.get(stuId);
						int chooseNum=stuChooseMap.get(stuId).size();
						if(chooseNum!=3) {
							//学生选课信息数据不正确
							continue;
						}
						Collections.sort(chooseList);
						String subjectIds=ArrayUtil.print(chooseList.toArray(new String[] {}),"_");
						if(numMap.containsKey(subjectIds)) {
							numMap.put(subjectIds, numMap.get(subjectIds)+1);
						}else {
							numMap.put(subjectIds, 1);
						}
					}else {
						//error
						//学生选课信息找不到
						continue;
					}
				}
				String studentScource="";
				for(Map.Entry<String, Integer> itt:numMap.entrySet()) {
					studentScource=studentScource+","+itt.getKey()+"-"+itt.getValue();
				}
				if(StringUtils.isNotBlank(studentScource)) {
					studentScource=studentScource.substring(1);
				}
				newGkSectionResult.setStudentScource(studentScource);
				resultList.add(newGkSectionResult);
			}
		}
		Map<String,Integer[]> studentId3Map=new HashMap<>();
		Map<String,Integer[]> studentId2Map=new HashMap<>();
		boolean f=true;
		Set<String> subjectIdSet=new HashSet<>();
		for(Map.Entry<String, List<String>> entry:stuChooseMap.entrySet()) {
			f=false;
			String stuId=entry.getKey();
			chooseList = stuChooseMap.get(stuId);
			int chooseNum=stuChooseMap.get(stuId).size();
			if(chooseNum!=3) {
				//学生选课信息数据不正确
				continue;
			}
			subjectIdSet.addAll(chooseList);
			Collections.sort(chooseList);
			String subjectIds=ArrayUtil.print(chooseList.toArray(new String[] {}),"_");
			Integer[] arr = studentId3Map.get(subjectIds);
			if(arr==null) {
				arr=new Integer[]{0,0};
				studentId3Map.put(subjectIds, arr);
			}
			arr[0]=arr[0]+1;
			if(allArrangeStudent.contains(stuId)) {
				f=true;
			}
			if(f) {
				arr[1]=arr[1]+1;
			}
			//2科
			for(int i=0;i<chooseNum-1;i++) {
				for(int j=i+1;j<chooseNum;j++) {
					String s1=chooseList.get(i)+"_"+chooseList.get(j);
					arr = studentId2Map.get(s1);
					if(arr==null) {
						arr=new Integer[]{0,0};
						studentId2Map.put(s1, arr);
					}
					arr[0]=arr[0]+1;
					if(f) {
						arr[1]=arr[1]+1;
					}
				}
			}
		}
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdSet.toArray(new String[] {})), new TR<List<Course>>() {});
		Map<String,Course> courseMap=EntityUtils.getMap(courseList,e->e.getId() );
		//组装
		beginList.addAll(makeBegin(studentId3Map, nowTime, sectionId, NewGkElectiveConstant.SUBJECT_TYPE_3, courseMap));
		beginList.addAll(makeBegin(studentId2Map, nowTime, sectionId, NewGkElectiveConstant.SUBJECT_TYPE_2, courseMap));
	}
	private List<NewGkSectionBegin> makeBegin(Map<String,Integer[]> studentIdMap,Date nowTime,String sectionId,String subjectType,Map<String,Course> courseMap){
		List<NewGkSectionBegin> beginList=new ArrayList<>();
		NewGkSectionBegin newGkSectionBegin;
		for(Map.Entry<String,Integer[]> entry:studentIdMap.entrySet()) {
			newGkSectionBegin=new NewGkSectionBegin();
			newGkSectionBegin.setId(UuidUtils.generateUuid());
			newGkSectionBegin.setSectionId(sectionId);
			newGkSectionBegin.setCreationTime(nowTime);
			newGkSectionBegin.setModifyTime(nowTime);
			newGkSectionBegin.setNoJoin(1);//默认1
			newGkSectionBegin.setSubjectType(subjectType);
			Integer[] arr = entry.getValue();
			newGkSectionBegin.setStudentNum(arr[0]);
			newGkSectionBegin.setArrangeNum(arr[1]);
			newGkSectionBegin.setSubjectIds(entry.getKey());
			String[] strArr = entry.getKey().split("_");
			String groupName=makeName(courseMap, strArr);
			newGkSectionBegin.setGroupName(groupName);
			beginList.add(newGkSectionBegin);
		}
		return beginList;
	}
	
	@ResponseBody
	@RequestMapping("/saveSectionBegin")
	@ControllerInfo(value = "2+x保存参数")
	public String saveSectionBegin(Section2xSaveDto section2xSaveDto) {
		String[] noJoinId = section2xSaveDto.getSectionBeginId();
		NewGkSection newGkSection = newGkSectionService.findOne(section2xSaveDto.getSectionId());
		if(newGkSection==null) {
			return error("2+x数据不存在");
		}
		if(isNowArrange(newGkSection.getId())) {
			return success("1");
		}
		if("0".equals(newGkSection.getStat()) || "3".equals(newGkSection.getStat())) {
			newGkSection.setMarginSize(section2xSaveDto.getMarginSize());
			newGkSection.setMeanSize(section2xSaveDto.getMeanSize());
			newGkSection.setModifyTime(new Date());
			newGkSection.setOpenClassNum(section2xSaveDto.getOpenClassNum());
			newGkSection.setRemark("");
			newGkSection.setStat("1");//进入分班
			try {
				newGkSectionService.updateSection(newGkSection,noJoinId);
			}catch (Exception e) {
				e.printStackTrace();
				return error("保存参数失败");
			}
		}else {
			if(isNowArrange(newGkSection.getId())) {
				
				return success(newGkSection.getStat());
			}else {
				if("1".equals(newGkSection.getStat())) {
					
				}else {
					return success(newGkSection.getStat());
				}
			}
			
		}
		String unitId=getLoginInfo().getUnitId();
		
		RedisUtils.set("DIVIDE_SECTION_"+newGkSection.getId(),newGkSection.getStat());
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					solveSection2x(newGkSection,3,unitId);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			
		}).start(); 
	
		return success(newGkSection.getStat());
	}
	
	@ResponseBody
	@RequestMapping("/circleSection")
	@ControllerInfo(value = "2+x状态")
	public String circleSection(String sectionId) {
		if(isNowArrange(sectionId)) {
			return success("1");
		}else {
			NewGkSection newGkSection = newGkSectionService.findOne(sectionId);
			if(newGkSection==null) {
				return error("2+x数据不存在");
			}
			if("1".equals(newGkSection.getStat())) {
				//服务器中断 或者出错
				newGkSection.setRemark("失败");
				newGkSection.setModifyTime(new Date());
				newGkSection.setStat("3");
				newGkSectionService.save(newGkSection);
			}
			return success(newGkSection.getStat());
		}
		
	}
	
	public boolean isNowArrange(String sectionId) {
		// 缓存有时限
		String redisStr=RedisUtils.get("DIVIDE_SECTION_"+sectionId);
		if(StringUtils.isNotBlank(redisStr) && "1".equals(redisStr)) {
			//分班中
			return true;
		}
		
		return  false;
	}

	
	public void solveSection2x(NewGkSection newGkSection,int batch,String unitId) throws IOException{
		//先自己校验参数问题
		System.out.println("准备数据");
		Sectioning2A1XInput s2a1xInput = new Sectioning2A1XInput();
		//平均班级人数
		s2a1xInput.setSectionSizeMean(newGkSection.getMeanSize());
//		//平均班级人数的误差值，也就是说，可以额外多塞几个人
		s2a1xInput.setSectionSizeMargin(newGkSection.getMarginSize());
		
//		//总的教室数量 这里就是开设行政班数量
		s2a1xInput.setMaxRoomCount(newGkSection.getOpenClassNum());
		
//		//输入1：group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
		List<List<String>> group3AList = new ArrayList<List<String>>();
		//输入3：excludedGroup2AList，不允许出现的2A组合: {<选课1-2A><选课2-2A>}
		List<List<String>> excludedGroup2AList = new ArrayList<List<String>>(); 
		
		//总共学生人数
		int allStuNum=0;
		List<NewGkSectionBegin> beginList = newGkSectionBeginService.findListBy("sectionId",newGkSection.getId());
		for(NewGkSectionBegin begin:beginList) {
			if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(begin.getSubjectType())){
				if(begin.getNoJoin()==1) {
					int cc=begin.getStudentNum()-begin.getArrangeNum();
					if(cc>0) {
						String[] subjectIds = begin.getSubjectIds().split("_");
						List<String> str=new ArrayList<>();
						str.addAll(Arrays.asList(subjectIds));
						str.add(String.valueOf(cc));
						group3AList.add(str);
						allStuNum=allStuNum+cc;
					}
					
				}
			}else {
				if(begin.getNoJoin()==0) {
					String[] subjectIds = begin.getSubjectIds().split("_");
					List<String> str=Arrays.asList(subjectIds);
					excludedGroup2AList.add(str);
				}
			}
		}
		//最大容纳 人数
		int maxCanArrange=(newGkSection.getMeanSize()+newGkSection.getMarginSize())*newGkSection.getOpenClassNum();
		if(allStuNum>maxCanArrange) {
			newGkSection.setStat("3");//不成功 
			newGkSection.setRemark("根据参数设置，最大可安排人数为"+maxCanArrange+",实际安排的人数为"+allStuNum+",超过最大值,请修改参数");
			newGkSection.setModifyTime(new Date());
			newGkSectionService.save(newGkSection);
			RedisUtils.del("DIVIDE_SECTION_"+newGkSection.getId());
			return; //不玩了
		}
		
		s2a1xInput.setGroup3AList(group3AList);
		
		s2a1xInput.setExcludedGroup2AList(excludedGroup2AList);
		
//				
	   //输入2： maxTeacherCountList，每门课的老师数量： {{<课名><老师数量供1X部分使用>}}
		List<List<String>> maxTeacherCountList =new ArrayList<>();
//				List<List<String>> maxTeacherCountList = calculateMaxTeacherCountList(group3AList, 
//						s2a1xInput.getSectionSizeMean(), s2a1xInput.getSectionSizeMargin()); 
		s2a1xInput.setMaxTeacherCountList(maxTeacherCountList);
//						
		List<NewGkSectionResult> resultList = newGkSectionResultService.findListBy(new String[] {"sectionId","arrangeType"}, new String[]{newGkSection.getId(),"0"});
		//输入4：pre1XList，手动2+X遗留下来的1X部分: {<选课1-3A><选课2-3A><选课3-3A><人数><选课1X>}
		List<List<String>> pre1XList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(resultList)) {
			//key:subjectId_subjectId_subjectId-subjectIdx
			Map<String,Integer> pre1xMap=new HashMap<>();
			for(NewGkSectionResult result:resultList) {
				if(result.getSubjectType().equals(NewGkElectiveConstant.SUBJTCT_TYPE_2)) {
					String  subject2Id=result.getSubjectIds();
					String studentScource=result.getStudentScource();
					String[] scourceArr = studentScource.split(",");
					for(String s:scourceArr) {
						String[] sarr= s.split("-");
						String sub3=sarr[0];
						String[] sub3Arr= sub3.split("_");
						String subIdx=null;
						for(String ss:sub3Arr) {
							if(subject2Id.indexOf(ss)==-1) {
								subIdx=ss;
								break;
							}
						}
						String key=sub3+"-"+subIdx;
						int  num=Integer.parseInt(sarr[1]);
						if(!pre1xMap.containsKey(key)) {
							pre1xMap.put(key, num);
						}else {
							pre1xMap.put(key, pre1xMap.get(key)+num);
						}
						
					}
				}
			}
			for(Entry<String, Integer> item:pre1xMap.entrySet()) {
				String[] keyArr = item.getKey().split("-");
				List<String> ll=new ArrayList<String>();
				String[] subId = keyArr[0].split("_");
				ll.addAll(Arrays.asList(subId));
				ll.add(String.valueOf(item.getValue()));
				ll.add(keyArr[1]);
				pre1XList.add(ll);
			}
			
		}
		//		List<List<String>> pre1XList = makeupPre1XList(); 
		s2a1xInput.setPre1XList(pre1XList);
				
	   //输入5：sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
//				List<List<String>> sectionSizeList = calculateSectionSize(group3AList, sectionSizeMean, sectionSizeMargin);
		List<List<String>> sectionSizeList = calculateSectionSize2(s2a1xInput,batch);
		s2a1xInput.setSectionSizeList(sectionSizeList);		
//				
//				//DEBUG: 如果教室数量不够的话，就不玩了 
//				int roomCountRequired = getTotalRequiredRoomCount(sectionSizeList);
//				int roomCountAvailable = s2a1xInput.getMaxRoomCount() * 3;
//				if ( roomCountRequired > roomCountAvailable ) {
//					System.out.println("ERROR: In class SectioningApp3::prepareInput(...): 教室数量不够啊！");
//					System.out.println("\t" + roomCountRequired + " rooms requested, but only " + roomCountAvailable + " rooms available!");
//					System.exit(-1);
//				}
//				
//				return s2a1xInput;
		
		
		//DEBUG: 检验一下输入数据的有效性
		List<List<String>> errorGroup = isExcludingTooMany2A(s2a1xInput.getGroup3AList(), s2a1xInput.getExcludedGroup2AList());
		if (CollectionUtils.isNotEmpty(errorGroup)) {
			newGkSection.setStat("3");//不成功 
			String sss="";
			List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId),Course.class);
			Map<String,Course> courseMap=EntityUtils.getMap(courseList, e->e.getId());
			for(List<String> ss:errorGroup) {
				sss=sss+"、"+makeName(courseMap, ss.toArray(new String[] {}));
			}
			sss=sss.substring(1);
			newGkSection.setRemark("组合"+sss+"被排除在外");
			newGkSection.setModifyTime(new Date());
			newGkSectionService.save(newGkSection);
			RedisUtils.del("DIVIDE_SECTION_"+newGkSection.getId());
			return; //不玩了
		}
		System.out.println("准备数据完成，开始分班");
		//2. 创建2+X算法的工作现场
		Sectioning2A1X s2a1x = new Sectioning2A1X(s2a1xInput);
		
		//3. 开拔！调用2+X算法，获得结果
		//resultGroup2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
		List<List<String>> resultGroup2AList = s2a1x.calculateSectioning2A1X();
		System.out.println("分班完成，进行校验");
		//DEBUG，打印出来看看结果
//		printResult2A(resultGroup2AList);
//		printResult1X(s2a1xInput, resultGroup2AList); //可以参考一下，如何为1X部分的学生开班

		//检验一下，结果是不是对的
		if (!verifyResult(s2a1xInput.getGroup3AList(), resultGroup2AList) ) {
			System.out.println("ERROR: Something error in the result!");
			newGkSection.setStat("3");//不成功 存在错误数据
			newGkSection.setRemark("ERROR: Something error in the result!");
			newGkSection.setModifyTime(new Date());
			newGkSectionService.save(newGkSection);
			RedisUtils.del("DIVIDE_SECTION_"+newGkSection.getId());
		}
		else {
			System.out.println("INFO: Above result is feasible and CORRECT! Verified.");
			saveResult(newGkSection,resultGroup2AList);
			newGkSection.setStat("2");//成功
			newGkSection.setModifyTime(new Date());
			newGkSectionService.save(newGkSection);
			RedisUtils.del("DIVIDE_SECTION_"+newGkSection.getId());
		}
		
	}
	/**
	 * 
	 * @param newGkSection
	 * @param resultGroup2AList <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	 */
	public void saveResult(NewGkSection newGkSection,List<List<String>> resultGroup2AList) {
		NewGkSectionEnd newGkSectionEnd;
		Map<String,NewGkSectionEnd> map=new HashMap<>();
		List<NewGkSectionEnd> endList=new  ArrayList<>();
		for(List<String> ss:resultGroup2AList) {
			String[] sub3Id=new String[] {ss.get(3),ss.get(4),ss.get(5)};
			Arrays.sort(sub3Id);
			int num3=Integer.parseInt(ss.get(6));
			if(num3<=0) {
				continue;
			}
			String value1=sub3Id[0]+"_"+sub3Id[1]+"_"+sub3Id[2]+"-"+num3;
			String[] sub2Id=new String[] {ss.get(0),ss.get(1)};
			Arrays.sort(sub2Id);
			String key=sub2Id[0]+"_"+sub2Id[1];
			newGkSectionEnd=map.get(key);
			if(newGkSectionEnd==null) {
				int num=Integer.parseInt(ss.get(2));
				if(num<=0) {
					continue;
				}
				newGkSectionEnd=new NewGkSectionEnd();
				newGkSectionEnd.setId(UuidUtils.generateUuid());
				newGkSectionEnd.setCreationTime(new Date());
				newGkSectionEnd.setModifyTime(new Date());
				newGkSectionEnd.setSectionId(newGkSection.getId());
				newGkSectionEnd.setSubjectIds(key);
				newGkSectionEnd.setOpenClassNum(Integer.parseInt(ss.get(2)));
				map.put(key, newGkSectionEnd);
				endList.add(newGkSectionEnd);
			}
			String studentScource = newGkSectionEnd.getStudentScource();
			
			if(StringUtils.isBlank(studentScource)) {
				studentScource=value1;
			}else {
				studentScource=studentScource+","+value1;
			}
			newGkSectionEnd.setStudentScource(studentScource);
		}
		
		newGkSectionEndService.saveNewGkSectionEndList(endList);
		
	}
	//input3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	//result2AList: <选课1-2A><选课2-2A> <开班数> <选课1-3A><选课2-3A><选课3-3A><人数>
	private static boolean verifyResult(List<List<String>> input3AList, List<List<String>> result2AList) {
		boolean isCorrect = true;
		
		//原始输入中，每个3A组合的人数
		Map<String, Integer> input3ACounters = new HashMap<>();
		for (List<String> inputLine : input3AList) {
			List<String> code3AList = new ArrayList<>();
			code3AList.add(inputLine.get(0));
			code3AList.add(inputLine.get(1));
			code3AList.add(inputLine.get(2));
			Collections.sort(code3AList);
			input3ACounters.put(code3AList.get(0) + code3AList.get(1) + code3AList.get(2), new Integer(inputLine.get(3)));
		}
		
		//结果中的，3A组合的人数汇总一下，应该跟原始输入的总人数一致
		Map<String, Integer> output3ACounters = new HashMap<>();
		for (List<String> resultLine : result2AList) {
			List<String> code3AList = new ArrayList<>();
			code3AList.add(resultLine.get(3));
			code3AList.add(resultLine.get(4));
			code3AList.add(resultLine.get(5));
			Collections.sort(code3AList);
			
			//2A中的第一个，应该在3A中出现
			if (!code3AList.contains(resultLine.get(0))) {
				isCorrect = false;
				System.out.println("ERROR: " + resultLine);
				break;
			}
			
			//2A中的第二个，应该在3A中出现
			if (!code3AList.contains(resultLine.get(1))) {
				isCorrect = false;
				System.out.println("ERROR: " + resultLine);
				break;
			}
			
			//把分散在不同的2A中的3A组合人数，汇总起来
			Integer count = new Integer(resultLine.get(6));
			if (output3ACounters.containsKey(code3AList.get(0) + code3AList.get(1) + code3AList.get(2))) {
				Integer oldCount = output3ACounters.get(code3AList.get(0) + code3AList.get(1) + code3AList.get(2));
				oldCount += count;
				output3ACounters.put(code3AList.get(0) + code3AList.get(1) + code3AList.get(2), oldCount);
			}
			else {
				output3ACounters.put(code3AList.get(0) + code3AList.get(1) + code3AList.get(2), count);
			}
		}
		
		//把原始输入的每个3A组合弄成一个String，放到Set里
		Set<String> inputLineSet = new HashSet<>();
		for (Map.Entry<String, Integer> me1 : input3ACounters.entrySet()) {
			String s1 = me1.getKey() + me1.getValue();
			inputLineSet.add(s1);
		}
		
		//把结果数据中的每个3A组合也弄成一个String，放到Set里
		Set<String> outputLineSet = new HashSet<>();
		for (Map.Entry<String, Integer> me2 : output3ACounters.entrySet()) {
			String s2 = me2.getKey() + me2.getValue();
			outputLineSet.add(s2);
		}
		
		//这两个Set，应该是一样的才行
		if (!inputLineSet.containsAll(outputLineSet) || !outputLineSet.containsAll(inputLineSet)) {
			isCorrect = false;
			System.out.println("inputLineSet = ");
			inputLineSet.stream().forEach(e -> System.out.println(e));

			System.out.println("outputLineSet = ");
			outputLineSet.stream().forEach(e -> System.out.println(e));
		}
		
		return isCorrect;
	}
	
	
	/**
	 * 检测一下，会不会有3A组合，被要排除的2A组合覆盖了
	 * @param group3AList
	 * @param excludedGroup2AList
	 * @param noArrangSubjectIds 返回被覆盖的3A
	 * @return
	 */
	private List<List<String>> isExcludingTooMany2A(List<List<String>> group3AList, List<List<String>> excludedGroup2AList) {
		List<List<String>> noArrangSubjectIds=new ArrayList<>();
		
		if(CollectionUtils.isEmpty(excludedGroup2AList) || CollectionUtils.isEmpty(group3AList)) {
			return noArrangSubjectIds;
		}
		//excludedGroup2A
		List<String> excludedGroup2A = new ArrayList<>();
		for (List<String> line : excludedGroup2AList) {
			Collections.sort(line);
			excludedGroup2A.add(line.get(0)+line.get(1));
		}
		for(List<String> line : group3AList) {
			List<String> newLine = new ArrayList<>(); //拷贝出来，自己排序，不要破坏原来的数据
			newLine.add(line.get(0));
			newLine.add(line.get(1));
			newLine.add(line.get(2));
			Collections.sort(newLine);
			if (excludedGroup2A.contains(newLine.get(0).trim() + line.get(1).trim()) &&
					excludedGroup2A.contains(newLine.get(0).trim() + line.get(2).trim()) &&
					excludedGroup2A.contains(newLine.get(1).trim() + line.get(2).trim())) {
				
				noArrangSubjectIds.add(line);
//					System.out.println("3A 组合【" + newLine.get(0).trim() + newLine.get(1).trim() + newLine.get(2).trim() + "】被完全排除掉了！");
			}
		}
		
		return noArrangSubjectIds;
	}	
	

	/**
	 * 
	 * @param 学生选课列表，group3AList，每个3A组合的人数: { <选课1-3A> <选课2-3A> <选课3-3A> <人数> }
	 * @return 每门课的教学班大小，sectionSizeList: {<选课><每个教学班平均人数><误差大小><教学班数量>}
	 * @throws IOException 
	 */
	private static List<List<String>> calculateSectionSize2(Sectioning2A1XInput s2a1xInput,int batch) throws IOException {
		
		//studentCountByCourse：统计一下每门课总共有多少人上
		Map<String, Integer> studentCountByCourse = new HashMap<>();
		
		//先取3A部分的学生人数
		for (List<String> line : s2a1xInput.getGroup3AList()) {
			for (int i = 0; i < 3; i ++) {
				studentCountByCourse.merge(line.get(i).trim(), Integer.parseInt(line.get(3)), Integer::sum);
			}
		}
		
		//再取Pre1X部分的学生人数
		for(List<String> line : s2a1xInput.getPre1XList()) {
			String courseName =  line.get(0);
			int studentCount = Integer.parseInt(line.get(1));
			studentCountByCourse.merge(courseName.trim(), studentCount, Integer::sum);
		}
		
		//现在，studentCountByCourse里是全体的学生数了
		
		//把studentCountByCourse整理成 BestSectionCount 算法的输入格式
		List<List<String>> courseStudentCountList = new ArrayList<>();
		for (Map.Entry<String, Integer> me : studentCountByCourse.entrySet()) {
			List<String> line = new ArrayList<>();
			line.add(me.getKey());
			line.add("" + me.getValue().intValue());
			courseStudentCountList.add(line);
		}
		
		//1. 准备输入数据
		BestSectionCountInput solutionInput = new BestSectionCountInput();
		solutionInput.setMaxRoomCount(s2a1xInput.getMaxRoomCount());		//算法参数1：总的教室数量
		solutionInput.setTimeSlotCount(batch); 									//算法参数2：3个时间点是常见的，如果是7选4的B课情况，这里应该是4
		solutionInput.setCourseStudentCountList(courseStudentCountList); 	//算法参数3：每门课的学生人数
		
		//2. 创建算法对象
		BestSectionCount launcher = new BestSectionCount(solutionInput);
		
		//3. Go Fly! 开拔，调用算法，获得结果
		//resultList: <课程名><开班数><学生数> 
		List<List<String>> resultList = launcher.calculateSectionCount();

		//DEBUG 把结果打印出来看看
//		printSectionSizeList(solutionInput, resultList);
		
		//整理成sectionSizeList要求的格式
		List<List<String>> sectionSizeList = new ArrayList<>();
		int allMarginSize=s2a1xInput.getSectionSizeMargin();
		int allMeanSize=s2a1xInput.getSectionSizeMean();
		int max=allMeanSize+allMarginSize;
		int min=allMeanSize+allMarginSize;
		for (List<String> line : resultList) {
			String courseName = line.get(0);
			int sectionCount = Integer.parseInt(line.get(1));
			int studentCount = Integer.parseInt(line.get(2));
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			
			List<String> oneCourseSizeInfo = new ArrayList<>();
			oneCourseSizeInfo.add(courseName);
			int sizeMean = sizeList.get(sizeList.size()/2);		//取中间的一个
			oneCourseSizeInfo.add("" + sizeMean); 				//sectionSizeMean;
			//oneCourseSizeInfo.add("" + (int)(sizeMean * 0.15)); //sectionSizeMargin;
			if(sizeMean>=min && sizeMean<=max) {
				if(sizeMean>allMeanSize) {
					oneCourseSizeInfo.add("" + (max-sizeMean));
				}else {
					oneCourseSizeInfo.add("" + (sizeMean-min));
				}
			}else {
				oneCourseSizeInfo.add("" + allMarginSize);
			}
			
			oneCourseSizeInfo.add("" + sectionCount);
			sectionSizeList.add(oneCourseSizeInfo);
		}

		return sectionSizeList;
	}
	
	@ResponseBody
	@RequestMapping("/deleteOrStartSection")
	@ControllerInfo(value = "2+x保存参数")
	public String deleteOrStartSection(String sectionId,String type) {
		if(isNowArrange(sectionId)) {
			return error("正在分班中，不能操作");
		}
		try {
			if("0".equals(type)) {
				//完全删除
				newGkSectionService.deleteBySectionId(sectionId);
			}else {
				//重新维护  去除操作的数据
				newGkSectionService.deleteResultBySectionId(sectionId);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		
		return success("");
	}
	
	
	@ResponseBody
	@RequestMapping("/saveSectionResult")
	@ControllerInfo(value = "2+x保存结果")
	public String saveSectionResult(Section2xSaveResultDto dto) {
		try {
			//老数据
			String sectionId = dto.getSectionId();
			
			//暂时修改3科的安排人数=算法前的人数
			List<NewGkSectionBegin> oldBeginList = newGkSectionBeginService.findListBy(new String[] {"sectionId","subjectType"}, new String[] {sectionId,NewGkElectiveConstant.SUBJECT_TYPE_3});
			//算法结果更新
			List<NewGkSectionEnd> oldEndList = newGkSectionEndService.findListBy("sectionId", sectionId);
			Map<String,NewGkSectionEnd> endMap=EntityUtils.getMap(oldEndList, e->e.getId());
			//所有结果
			List<NewGkSectionResult> oldResultList = newGkSectionResultService.findListBy("sectionId", sectionId);
			Map<String,NewGkSectionResult> resultMap=EntityUtils.getMap(oldResultList, e->e.getId());
			
			List<NewGkSectionResult> insertResultList=new ArrayList<>();
			Set<String> updateResultIds=new HashSet<>();
			
			//暂时不会有删除新增的地方，以及增加组合范围
			List<NewGkSectionEnd> insertEndList=new ArrayList<>();
			
			List<NewGkSectionResult> resultList = dto.getSectionResultList();
			Map<String,Integer> arrangeBySubIds=new HashMap<>();
			NewGkSectionResult newResult=null;
			if(CollectionUtils.isNotEmpty(resultList)) {
				for(NewGkSectionResult result:resultList) {
					if(result==null) {
						continue;
					}
					if(StringUtils.isBlank(result.getSubjectIds())) {
						continue;
					}
					String studentScource="";
					String[] nums = result.getStudentScourceNum();
					String[] subs = result.getStudentScourceSub();
					for(int ii=0;ii<subs.length;ii++) {
						//当结果都为0则不保存
						if(Integer.parseInt(nums[ii])==0) {
							continue;
						}
						if(StringUtils.isBlank(studentScource)) {
							studentScource=subs[ii]+"-"+nums[ii];
						}else {
							studentScource=studentScource+","+subs[ii]+"-"+nums[ii];
						}
						if("0".equals(result.getArrangeType())) {
							if(arrangeBySubIds.containsKey(subs[ii])) {
								arrangeBySubIds.put(subs[ii], arrangeBySubIds.get(subs[ii])+Integer.parseInt(nums[ii]));
							}else {
								arrangeBySubIds.put(subs[ii], Integer.parseInt(nums[ii]));
							}
						}
						
					}
					if(StringUtils.isBlank(studentScource)) {
						//总人数为0 不保存
						continue;
					}
					
					if(StringUtils.isNotBlank(result.getId())) {
						newResult=resultMap.get(result.getId());
						updateResultIds.add(result.getId());
					}else {
						newResult=null;
					}
					if(newResult==null) {
						newResult=new NewGkSectionResult();
						newResult.setId(UuidUtils.generateUuid());
						newResult.setArrangeType(result.getArrangeType());
						newResult.setSectionId(sectionId);
						newResult.setCreationTime(new Date());
						newResult.setSubjectIds(result.getSubjectIds());
						newResult.setSubjectType(result.getSubjectType());
						newResult.setGroupClassId(result.getGroupClassId());
					}
					newResult.setModifyTime(new Date());
					newResult.setGroupName(result.getGroupName());
					
					newResult.setStudentScource(studentScource);
					insertResultList.add(newResult);
				}
			}
			
			List<NewGkSectionEnd> endList = dto.getSectionEndList();
			NewGkSectionEnd newEnd;
			for(NewGkSectionEnd end:endList) {
				newEnd = endMap.get(end.getId());
				if(newEnd==null) {
					//暂时不可能存在
					continue;
				}
				newEnd.setModifyTime(new Date());
				String studentScource="";
				String[] nums = end.getStudentScourceNum();
				String[] subs = end.getStudentScourceSub();
				for(int ii=0;ii<subs.length;ii++) {
					if(ii==0) {
						studentScource=subs[ii]+"-"+nums[ii];
					}else {
						studentScource=studentScource+","+subs[ii]+"-"+nums[ii];
					}
				}
				newEnd.setStudentScource(studentScource);
				insertEndList.add(newEnd);
			}
			//修改开始值
			for(NewGkSectionBegin begin:oldBeginList) {
				if(arrangeBySubIds.containsKey(begin.getSubjectIds())) {
					begin.setArrangeNum(arrangeBySubIds.get(begin.getSubjectIds()));
				}else {
					begin.setArrangeNum(0);
				}
			}
			
			Set<String> allIds=resultMap.keySet();
			String[] delId=null;
			if(CollectionUtils.isNotEmpty(allIds)) {
				allIds.removeAll(updateResultIds);
				if(CollectionUtils.isNotEmpty(allIds)) {
					delId=allIds.toArray(new String[] {});
				}
			}
			
			newGkSectionService.updateSection(delId,oldBeginList,insertEndList, insertResultList);
		}catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		
		return success("");
	}
	@ResponseBody
	@RequestMapping("/useSectionResult")
	@ControllerInfo(value = "使用结果")
	public String useSectionResult(String sectionId,ModelMap map) {
		String unitId=getLoginInfo().getUnitId();
		NewGkSection section = newGkSectionService.findOne(sectionId);
		//验证是否可以使用 如果可以--暂时不验证 使用已经开完的班级就好
		if(section==null) {
			return error("已经不存在该轮次");
		}
		NewGkDivide divideItem = newGkDivideService.findOne(section.getDivideId());
		if(divideItem==null) {
			return error("已经不存在对应的分班");
		}
		List<NewGkSectionResult> resultList = newGkSectionResultService.findListBy("sectionId", sectionId);
		if(CollectionUtils.isEmpty(resultList)) {
			return error("没有维护班级");
		}
		Map<String,NewGkSectionResult> oldClassSection=new HashMap<>();
		for(NewGkSectionResult r:resultList) {
			if(r.getArrangeType().equals("0") && StringUtils.isNotBlank(r.getGroupClassId())) {
				oldClassSection.put(r.getGroupClassId(), r);
			}
		}
		
		List<NewGkChoResult> stuChooseList = newGkChoResultService.findByChoiceIdAndKindType(unitId, NewGkElectiveConstant.KIND_TYPE_01, divideItem.getChoiceId());
		if(CollectionUtils.isEmpty(stuChooseList)) {
			return error("选课数据已经不存在");
		}
		Map<String,List<String>> stuIdByChoose=new HashMap<>();
		Map<String,List<String>> stuChooseMap=EntityUtils.getListMap(stuChooseList, NewGkChoResult::getStudentId, e->e.getSubjectId());;
		for(Map.Entry<String,List<String>> stuChoose:stuChooseMap.entrySet()) {
			List<String> chooseList = stuChoose.getValue();
			int chooseNum=chooseList.size();
			if(chooseNum!=3) {
				//学生选课信息数据不正确
				continue;
			}
			Collections.sort(chooseList);
			String key=ArrayUtil.print(chooseList.toArray(new String[] {}),"_");
			if(!stuIdByChoose.containsKey(key)) {
				stuIdByChoose.put(key, new ArrayList<>());
			}
			stuIdByChoose.get(key).add(stuChoose.getKey());
		}
		
		//old
		List<NewGkDivideClass> list = newGkDivideClassService.findByDivideIdAndClassType(unitId, section.getDivideId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> insertClassList=new ArrayList<>();
		List<NewGkClassStudent> insertStudentList=new ArrayList<>();
		NewGkDivideClass clazzItem;
		NewGkClassStudent clazzStudentItem;
		Set<String> deleteIds=new HashSet<>();
		
		Set<String> moveIds=new HashSet<>();
		
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId),Course.class);
		Map<String,Course> courseMap=EntityUtils.getMap(courseList, e->e.getId());
		Set<String> oldNameSet=new HashSet<>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(NewGkDivideClass n:list) {
				deleteIds.add(n.getId());
				if(oldClassSection.containsKey(n.getId())) {
					clazzItem=n;
					NewGkSectionResult item = oldClassSection.get(n.getId());
					String[] arr = item.getStudentScource().split(",");
					
					List<String> oldStudentList = n.getStudentList();
					Set<String> arrangeStuId=new HashSet<>();
					for(String s:arr) {
						String[] arr2 = s.split("-");
						int num=Integer.parseInt(arr2[1]);
						if(num>0) {
							List<String> sub3List = stuIdByChoose.get(arr2[0]);
							if(CollectionUtils.isEmpty(sub3List)) {
								continue;
							}
							//取交集oldStudentList  sub3List
							List<String> oldStuId = (List<String>) CollectionUtils.intersection(oldStudentList, sub3List);
							if(num==oldStuId.size()) {
								//刚刚好
								arrangeStuId.addAll(oldStuId);
								sub3List.removeAll(oldStuId);
								stuIdByChoose.put(arr2[0], sub3List);
							}else if(num>oldStuId.size()){
								arrangeStuId.addAll(oldStuId);
								sub3List.removeAll(oldStuId);
								
								int toIndex=num-oldStuId.size();
								if(toIndex>sub3List.size()) {
									toIndex=sub3List.size();
								}
								List<String> other = sub3List.subList(0, toIndex);
								arrangeStuId.addAll(other);
								sub3List.removeAll(other);
								stuIdByChoose.put(arr2[0], sub3List);
							}else {
								List<String> onlyNeed = oldStuId.subList(0, num);
								arrangeStuId.addAll(onlyNeed);
								sub3List.removeAll(onlyNeed);
								stuIdByChoose.put(arr2[0], sub3List);
							}
						}
					}
					if(CollectionUtils.isEmpty(arrangeStuId)) {
						//没有学生数据 直接删除
					}else {
						String subjIds = item.getSubjectIds().replace("_", ",");
						n.setSubjectIds(subjIds);
						n.setSubjectType(item.getSubjectType());
						n.setModifyTime(new Date());
						//沿用老名字
//						n.setClassName(item.getGroupName());
						insertClassList.add(n);
						//增加学生数据
						for(String s:arrangeStuId) {
							clazzStudentItem=initClassStudent(unitId, divideItem.getId(), clazzItem.getId(), s);
							insertStudentList.add(clazzStudentItem);
						}
						oldNameSet.add(n.getClassName());
					}
					
					moveIds.add(item.getId());
				}
				
			}
		}
		Map<String,Integer> nameSubIdsMap=new HashMap<>();
		Map<String,String> nameSubNameMap=new HashMap<>();
		//对于新的
		for(NewGkSectionResult r:resultList) {
			if(moveIds.contains(r.getId())) {
				continue;
			}
			String[] arr = r.getStudentScource().split(",");
			Set<String> arrangeStuId=new HashSet<>();
			for(String s:arr) {
				String[] arr2 = s.split("-");
				int num=Integer.parseInt(arr2[1]);
				if(num>0) {
					List<String> sub3List = stuIdByChoose.get(arr2[0]);
					if(CollectionUtils.isEmpty(sub3List)) {
						continue;
					}
					int toIndex=num;
					if(toIndex>sub3List.size()) {
						toIndex=sub3List.size();
					}
					List<String> other = sub3List.subList(0, toIndex);
					arrangeStuId.addAll(other);
					sub3List.removeAll(other);
					stuIdByChoose.put(arr2[0], sub3List);
					
				}
			}
			if(CollectionUtils.isEmpty(arrangeStuId)) {
				//没有学生数据 
			}else {
				String subjIds = r.getSubjectIds().replace("_", ",");
				if(!nameSubNameMap.containsKey(subjIds)) {
					nameSubNameMap.put(subjIds, makeName(courseMap, subjIds.split(",")));
				}
				clazzItem=initNewGkDivideClass(divideItem.getId(),subjIds,r.getSubjectType());
				
				int ii=nameSubIdsMap.get(subjIds)==null?1:nameSubIdsMap.get(subjIds);
				while(true) {
					String nameKey=nameSubNameMap.get(subjIds)+ii+"班";
					if(!oldNameSet.contains(nameKey)) {
						clazzItem.setClassName(nameKey);
						break;
					}
					ii++;
				}
				nameSubIdsMap.put(subjIds, ii+1);
				
				insertClassList.add(clazzItem);
				//增加学生数据
				for(String s:arrangeStuId) {
					clazzStudentItem=initClassStudent(unitId, divideItem.getId(), clazzItem.getId(), s);
					insertStudentList.add(clazzStudentItem);
				}
			}
		}
		String[] divideClassIds=null;
		if(CollectionUtils.isNotEmpty(deleteIds)){
			divideClassIds=deleteIds.toArray(new String[] {});
		}
		try {
		newGkDivideClassService.saveAllList(unitId, divideItem.getId(),
				divideClassIds, insertClassList, insertStudentList, false);
		}catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		return success("");
	}
	
	public NewGkClassStudent initClassStudent(String unitId,String divideId,String classId,String studentId) {
		NewGkClassStudent item=new NewGkClassStudent();
		item.setId(UuidUtils.generateUuid());
		item.setUnitId(unitId);
		item.setUnitId(unitId);
		item.setDivideId(divideId);
		item.setClassId(classId);
		item.setStudentId(studentId);
		item.setModifyTime(new Date());
		item.setCreationTime(new Date());
		return item;
	}
	
	private NewGkDivideClass initNewGkDivideClass(String divideId,
			String subjectIds,String subjectType) {
		NewGkDivideClass newGkDivideClass = new NewGkDivideClass();
		newGkDivideClass.setId(UuidUtils.generateUuid());
		newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
		newGkDivideClass.setModifyTime(new Date());
		newGkDivideClass.setCreationTime(new Date());
		newGkDivideClass
				.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		newGkDivideClass.setDivideId(divideId);
		newGkDivideClass.setSubjectIds(subjectIds);
		newGkDivideClass.setSubjectType(subjectType);
		newGkDivideClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_0);
		return newGkDivideClass;
	}
}
