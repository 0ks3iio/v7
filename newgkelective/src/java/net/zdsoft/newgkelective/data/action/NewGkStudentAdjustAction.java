package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.shaded.com.google.common.base.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.system.entity.mcode.McodeDetail;

@Controller
@RequestMapping("/newgkelective/studentAdjust")
public class NewGkStudentAdjustAction extends NewGkElectiveDivideCommonAction{
	
	
	
	@ResponseBody
	@RequestMapping("/{divideId}/findStudentIds")
	@ControllerInfo("学生搜索")
	public String findStudentIds(@PathVariable String divideId,String searchType,String searchValue) {
		JSONArray data = new JSONArray();
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide==null) {
			return data.toString();
		}
		
		//查询类型1：学号  2：姓名 
		List<Student> studentList=new ArrayList<>();
		if("1".equals(searchType)) {
			studentList=SUtils.dt(studentRemoteService.findBySchoolIdStudentNameStudentCode(divide.getUnitId(), null,searchValue),new TR<List<Student>>() {});
		}else if("2".equals(searchType)) {
			studentList=SUtils.dt(studentRemoteService.findBySchoolIdStudentNameStudentCode(divide.getUnitId(), searchValue,null), new TR<List<Student>>() {});
			
		}
		JSONObject json1=null;
		if(CollectionUtils.isNotEmpty(studentList)) {
			//找到改年级下班级
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findByInGradeIds(new String[] {divide.getGradeId()}),new TR<List<Clazz>>() {});
			if(CollectionUtils.isEmpty(clazzList)) {
				return data.toString();
			}
			List<String> clazzIds = EntityUtils.getList(clazzList, e->e.getId());
			studentList=studentList.stream().filter(e->clazzIds.contains(e.getClassId())).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(studentList)) {
				return data.toString();
			}
			for(Student s:studentList) {
				json1=new JSONObject();
				json1.put("id", s.getId());
				json1.put("studentName",s.getStudentName());
				data.add(json1);
			}
		}
		return data.toString();
		
	}
	@RequestMapping("/{divideId}/loadStuItem")
	@ControllerInfo("学生信息")
	public String loadStuItem(@PathVariable String divideId,String studentId,String subjectIds,ModelMap map){
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		//学生基本信息
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
		StudentResultDto dto=new StudentResultDto();
		dto.setStudentId(student.getId());
		dto.setStudentName(student.getStudentName());
		dto.setStudentCode(student.getStudentCode());
		Map<String, McodeDetail> mcodeMap=SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TR<Map<String, McodeDetail>>() {});
		if(student.getSex()!=null && mcodeMap!=null) {
			dto.setSex(mcodeMap.get(student.getSex().toString())==null?"":mcodeMap.get(student.getSex().toString()).getMcodeContent());
		}else {
			dto.setSex("");
		}
		//新行政班
		//dto.setClassName(className);
		Set<String> subjectIdset=new HashSet<>();
		List<String> chooseSubIds=new ArrayList<>();
		if(StringUtils.isBlank(subjectIds)) {
			//选课结果
			List<NewGkChoResult> list=newGkChoResultService.findByChoiceIdAndStudentIdAndKindType(divide.getUnitId(),NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(),studentId);
			if(CollectionUtils.isNotEmpty(list)) {
				chooseSubIds=EntityUtils.getList(list, e->e.getSubjectId());
				subjectIdset.addAll(chooseSubIds);
	    	}
		}else {
			String[] subIds = subjectIds.split(",");
			chooseSubIds.addAll(Arrays.asList(subIds));
			subjectIdset.addAll(chooseSubIds);
		}
		
		//学生成绩
		String refScoreId=divide.getReferScoreId();
		if(StringUtils.isBlank(refScoreId)){
			refScoreId=newGkReferScoreService.findDefaultIdByGradeId(divide.getUnitId(), divide.getGradeId());
		}
		//各科成绩+语数英成绩
		List<NewGkScoreResult> scoreList=new ArrayList<>();
		if(StringUtils.isNotBlank(refScoreId)) {
			scoreList = newGkScoreResultService.findByReferScoreIdAndStudentIdIn(divide.getUnitId(), refScoreId, new String[] {studentId});
			if(CollectionUtils.isNotEmpty(scoreList)) {
				subjectIdset.addAll(EntityUtils.getSet(scoreList, e->e.getSubjectId()));
			}
		}
		Map<String, Course> subjectNameMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(subjectIdset)) {
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectIdset.toArray(new String[0])),
					new TR<List<Course>>() {
					});
			subjectNameMap = EntityUtils.getMap(courseList,e->e.getId());
		}
		//暂不考虑科目排序
		String chooseSubjectIds="";
		String subjectNames="";
		if(CollectionUtils.isNotEmpty(chooseSubIds)) {
			for(String r:chooseSubIds) {
				chooseSubjectIds=chooseSubjectIds+","+r;
				subjectNames=subjectNames+" "+subjectNameMap.get(r).getSubjectName();
			}
			chooseSubjectIds=chooseSubjectIds.substring(1);
			subjectNames=subjectNames.substring(1);
		}
		dto.setChooseSubjects(chooseSubjectIds);
		dto.setChoResultStr(subjectNames);
		//成绩
		String scoreStr="";
		float ysy=0.0f;
		List<String> ysyCode =Arrays.asList(BaseConstants.SUBJECT_TYPES_YSY) ;
		if(CollectionUtils.isNotEmpty(scoreList)) {
			for(NewGkScoreResult s:scoreList) {
				if(!subjectNameMap.containsKey(s.getSubjectId())) {
					continue;
				}
				if(ysyCode.contains(subjectNameMap.get(s.getSubjectId()).getSubjectCode())) {
					ysy=ysy+s.getScore();
				}else {
					scoreStr=scoreStr+"、"+subjectNameMap.get(s.getSubjectId()).getSubjectName()+"("+s.getScore()+")";
				}
			}
		}
		if(StringUtils.isNotBlank(scoreStr)) {
			scoreStr=scoreStr+"、语数英("+ysy+")";
			scoreStr=scoreStr.substring(1);
		}else {
			scoreStr="语数英("+ysy+")";
		}
		dto.setMark(scoreStr);
		map.put("stuDto", dto);
		map.put("anum", divide.getBatchCountTypea());
		map.put("bnum", divide.getBatchCountTypeb());
		//新行政班
		List<NewGkClassStudent> classStuList = newGkClassStudentService.findListByStudentId(divide.getUnitId(), divideId, studentId);
		if(CollectionUtils.isNotEmpty(classStuList)) {
			List<NewGkDivideClass> newClassList = newGkDivideClassService.findListByIdIn(EntityUtils.getList(classStuList, e->e.getClassId()).toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(newClassList)) {
				newClassList=newClassList.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
			}
			if(CollectionUtils.isNotEmpty(newClassList)) {
				dto.setClassName(newClassList.get(0).getClassName());
			}
		}
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10) || divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)) {
			return "/newgkelective/divide/divideChange/divideChangeItem2.ftl";
		}
		return "/newgkelective/divide/divideChange/divideChangeItem.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/{divideId}/deleteStudentId")
	@ControllerInfo("删除学生")
	public String deleteStudentId(@PathVariable String divideId,String studentId){
		//删除存在的所有班级 对应辅助表的学生选课，真正方案的学生选课不处理
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide==null) {
			return error("分班方案不存在");
		}
		try {
			newGkDivideClassService.deleteByStudentId(divide,studentId);
		}catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("");
	}
	
	public String loadClassItem2(@PathVariable String divideId,String studentId,String subjectIds) {
		JSONObject json = new JSONObject();
		json.put("arrangeBath", "1");
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		//获取选课科目物理 与历史id
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(divide.getUnitId(),divide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		Set<String> subIdsAll=new HashSet<>();
		subIdsAll.addAll(subjectIdList);
		//该年级下所有学生信息
		List<Student> list = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), divide.getGradeId(), null, null), Student.class);
		Map<String, Student> studentMap = EntityUtils.getMap(list, e->e.getId());
		//学生成绩
		String refScoreId=divide.getReferScoreId();
		if(StringUtils.isBlank(refScoreId)){
			refScoreId=newGkReferScoreService.findDefaultIdByGradeId(divide.getUnitId(), divide.getGradeId());
		}
		//各科成绩+语数英成绩---显示需要安排科目的成绩
		Map<String,Map<String,Float>> stuSubMap=new HashMap<>();
		List<NewGkScoreResult> scoreList=new ArrayList<>();
		if(StringUtils.isNotBlank(refScoreId)) {
			scoreList =newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), refScoreId);
			if(CollectionUtils.isNotEmpty(scoreList)) {
				for(NewGkScoreResult r:scoreList) {
					subIdsAll.add(r.getSubjectId());
					if(!stuSubMap.containsKey(r.getStudentId())) {
						stuSubMap.put(r.getStudentId(), new HashMap<>());
					}
					stuSubMap.get(r.getStudentId()).put(r.getSubjectId(),  r.getScore()==null?0.0f:r.getScore());
				}
			}
		}
		//用于后面显示用
		List<NewGkOpenSubject> openList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A,NewGkElectiveConstant.SUBJECT_TYPE_B});
		Set<String> subIdsA=new HashSet<>();
		Set<String> subIdsB=new HashSet<>();
		for(NewGkOpenSubject oo:openList) {
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(oo.getSubjectType())) {
				if(subjectIds.contains(oo.getSubjectId())) {
					subIdsA.add(oo.getSubjectId());
				}
			}else {
				if(!subjectIds.contains(oo.getSubjectId())) {
					subIdsB.add(oo.getSubjectId());
				}
			}
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIdsAll.toArray(new String[0])),Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courseList, e->e.getId(),e->e);
		//所有班级
		List<NewGkDivideClass> classlist = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId,new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_1,
				NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4} ,true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);	
		
		List<NewGkDivideClass> class0List=classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_0)).collect(Collectors.toList());
		List<NewGkDivideClass> class1List=classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
		List<NewGkDivideClass> class3List=classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
		List<NewGkDivideClass> class4List=classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_4)).collect(Collectors.toList());
		
		List<String> existClazzList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(class3List)) {
			//存在选物理 或者历史的
			for(NewGkDivideClass c:class3List) {
				if(subjectIds.contains(c.getSubjectIds())) {
					for(String kk:c.getRelateId().split(",")) {
						existClazzList.add(kk);
					}	
				}
			}
		}
		Map<String,NewGkDivideClass> zhbMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(class0List)) {
			zhbMap=EntityUtils.getMap(class0List, e->e.getId());
		}
		List<String> ysyCode =Arrays.asList(BaseConstants.SUBJECT_TYPES_YSY) ;
		XzbJxbDivideClassDto classDto=null;
		List<XzbJxbDivideClassDto> zhbList=new ArrayList<>();
		List<String> oldClassList=new ArrayList<>();
		for(NewGkDivideClass n:class4List) {
			if(CollectionUtils.isNotEmpty(n.getStudentList()) && n.getStudentList().contains(studentId)) {
				oldClassList.add(n.getId());
			}
			boolean f=false;
			for(String s:n.getSubjectIds().split(",")) {
				if(!subjectIds.contains(s)) {
					f=true;
					break;
				}
			}
			if(f) {
				continue;
			}
			classDto = makeDto(n);
			List<String> xzbA=new ArrayList<>();
			List<String> xzbB=new ArrayList<>();
			
			String[] subIds = n.getSubjectIds().split(",");
			for(String s:subIds) {
				if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(courseMap.get(s).getSubjectCode())
						|| NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(courseMap.get(s).getSubjectCode())) {
					continue;
				}
				if(subIdsA.contains(s)) {
					xzbA.add(s);
				}
			}
			for(String s:subIdsB) {
				if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(courseMap.get(s).getSubjectCode())
						|| NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(courseMap.get(s).getSubjectCode())) {
					continue;
				}
				if(n.getSubjectIds().contains(s)) {
					continue;
				}
				xzbB.add(s);
			}
			if(CollectionUtils.isNotEmpty(xzbA)) {
				classDto.setSubjectAIds(xzbA.toArray(new String[0]));
				Arrays.sort(classDto.getSubjectAIds());
				String ss="";
				for(String s:classDto.getSubjectAIds()) {
					ss=ss+(StringUtils.isNotBlank(courseMap.get(s).getShortName())?courseMap.get(s).getShortName():courseMap.get(s).getSubjectName());
				}
				classDto.setSubjectANames(ss);
			}
			if(CollectionUtils.isNotEmpty(xzbB)) {
				classDto.setSubjectBIds(xzbB.toArray(new String[0]));
				Arrays.sort(classDto.getSubjectBIds());
				String ss="";
				for(String s:classDto.getSubjectBIds()) {
					ss=ss+(StringUtils.isNotBlank(courseMap.get(s).getShortName())?courseMap.get(s).getShortName():courseMap.get(s).getSubjectName());
				}
				classDto.setSubjectBNames(ss);
			}
			makeXzbTip(classDto, n.getStudentList(), studentMap, stuSubMap, courseMap, ysyCode);
			zhbList.add(classDto);
		}
		
		List<XzbJxbDivideClassDto> xzbList=new ArrayList<>();
		boolean isCheck3=false;
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)) {
			//不重组
			isCheck3=true;
		}
		for(NewGkDivideClass c1:class1List) {
			if(CollectionUtils.isNotEmpty(c1.getStudentList()) && c1.getStudentList().contains(studentId)) {
				oldClassList.add(c1.getId());
			}
			if(isCheck3) {
				//获取有合并数据
				if(!existClazzList.contains(c1.getId())) {
					continue;
				}
				classDto = makeDto(c1);
			}else{
				//获取组合班数据
				if(!zhbMap.containsKey(c1.getRelateId())) {
					continue;
				}
				NewGkDivideClass zhb=zhbMap.get(c1.getRelateId());
				boolean b = false;
				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhb.getSubjectType()) || NewGkElectiveConstant.SUBJTCT_TYPE_1.equals(zhb.getSubjectType())) {
					for(String s:zhb.getSubjectIds().split(",")) {
						if(!subjectIds.contains(s)) {
							b=true;
						    break;
						}
					}
				}else {
					//错误数据 一般不存在
					continue;
				}
				if(b) {
					continue;
				}
				classDto = makeDto(c1);
				classDto.setZhbId(zhb.getId());
				
				List<String> xzbA=new ArrayList<>();
				List<String> xzbB=new ArrayList<>();
				if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhb.getSubjectType())){
					String[] subIds = zhb.getSubjectIds().split(",");
					for(String s:subIds) {
						if(subIdsA.contains(s)) {
							xzbA.add(s);
						}
					}
					for(String s:subIdsB) {
						if(zhb.getSubjectIds().contains(s)) {
							continue;
						}
						xzbB.add(s);
					}
					
				}else {
					//定1走二
					String subIds = zhb.getSubjectIds();
					if(subIdsA.contains(subIds)) {
						xzbA.add(subIds);
					}else if(subIdsB.contains(subIds)) {
						xzbB.add(subIds);
					}
				}
				if(CollectionUtils.isNotEmpty(xzbA)) {
					classDto.setSubjectAIds(xzbA.toArray(new String[0]));
					Arrays.sort(classDto.getSubjectAIds());
					String ss="";
					for(String s:classDto.getSubjectAIds()) {
						ss=ss+(StringUtils.isNotBlank(courseMap.get(s).getShortName())?courseMap.get(s).getShortName():courseMap.get(s).getSubjectName());
					}
					classDto.setSubjectANames(ss);
				}
				if(CollectionUtils.isNotEmpty(xzbB)) {
					classDto.setSubjectBIds(xzbB.toArray(new String[0]));
					Arrays.sort(classDto.getSubjectBIds());
					String ss="";
					for(String s:classDto.getSubjectBIds()) {
						ss=ss+(StringUtils.isNotBlank(courseMap.get(s).getShortName())?courseMap.get(s).getShortName():courseMap.get(s).getSubjectName());
					}
					classDto.setSubjectBNames(ss);
				}
				classDto.setSubjectType(zhb.getSubjectType());
			}
			//设置显示科目数据
			if(classDto!=null) {
				makeXzbTip(classDto,c1.getStudentList(),studentMap,stuSubMap,courseMap,ysyCode);
				xzbList.add(classDto);
			}
		}
		String oldClassIds="";
		if(CollectionUtils.isNotEmpty(oldClassList)) {
			oldClassIds=oldClassList.stream().map(e -> e).collect(Collectors.joining(","));
		}
		json.put("oldClassIds", oldClassIds);
		json.put("zhbList", zhbList);
		json.put("xzbList", xzbList);
		return json.toString();
	}
	
	private void makeXzbTip(XzbJxbDivideClassDto classDto,List<String> studentIdList,Map<String,Student> studentMap,
			Map<String,Map<String,Float>> stuSubMap,Map<String,Course> courseMap,List<String> ysyCode) {
		List<String[]> scoreList1=new ArrayList<>();
		//统计各科目成绩
		if(CollectionUtils.isNotEmpty(studentIdList)) {
			classDto.setStuNum(studentIdList.size());
			int b=0;
			int m=0;
			double ysyall=0.0;
			Map<String,Double> subjectScoreAll=new HashMap<>();
			for(String st:studentIdList) {
				if(studentMap.containsKey(st)) {
					if(Objects.equal(1, studentMap.get(st).getSex())) {
						//男
						b++;
					}else {
						m++;
					}
				}
				//暂时不考虑学生找不到情况
				Map<String, Float> oneScoreMap = stuSubMap.get(st);
				if(oneScoreMap!=null) {
					for(Entry<String, Float> ll:oneScoreMap.entrySet()) {
						if(!courseMap.containsKey(ll.getKey())) {
							continue;
						}
						if(ysyCode.contains(courseMap.get(ll.getKey()).getSubjectCode())) {
							ysyall=ysyall+ll.getValue();
							continue;
						}
						if(!subjectScoreAll.containsKey(ll.getKey())) {
							subjectScoreAll.put(ll.getKey(), (double)ll.getValue());
						}else {
							subjectScoreAll.put(ll.getKey(), subjectScoreAll.get(ll.getKey())+ll.getValue());
						}
					}
				}
			}
			classDto.setBoyNum(b);
			classDto.setGirlNum(m);
			
			if(classDto.getSubjectAIds()!=null) {
				for(String s:classDto.getSubjectAIds()) {
					float ff=0.0f;
					if(subjectScoreAll.containsKey(s)) {
						double all=subjectScoreAll.get(s);
						ff = makeDoubleTwo(all/studentIdList.size());
					}
					scoreList1.add(new String[] {courseMap.get(s).getSubjectName(),String.valueOf(ff)});
				}
			}
			if(classDto.getSubjectBIds()!=null) {
				for(String s:classDto.getSubjectBIds()) {
					float ff=0.0f;
					if(subjectScoreAll.containsKey(s)) {
						double all=subjectScoreAll.get(s);
						ff = makeDoubleTwo(all/studentIdList.size());
					}
					scoreList1.add(new String[] {courseMap.get(s).getSubjectName()+"(学)",String.valueOf(ff)});
				}
				
			}
			float ff = makeDoubleTwo(ysyall/studentIdList.size());
			scoreList1.add(new String[] {"语数英",String.valueOf(ff)});
			
		}else {
			classDto.setStuNum(0);
			if(classDto.getSubjectAIds()!=null) {
				for(String s:classDto.getSubjectAIds()) {
					scoreList1.add(new String[] {courseMap.get(s).getSubjectName(),"0.0"});
				}
			}
			if(classDto.getSubjectBIds()!=null) {
				for(String s:classDto.getSubjectBIds()) {
					scoreList1.add(new String[] {courseMap.get(s).getSubjectName()+"(学)","0.0"});
				}
			}
			scoreList1.add(new String[] {"语数英","0.0"});
		}
		classDto.setScoreList(scoreList1);
	}
	
	
	@ResponseBody
	@RequestMapping("/{divideId}/loadClassItem")
	@ControllerInfo("可以进入的行政班列表")
	public String loadClassItem(@PathVariable String divideId,String studentId,String subjectIds){
		JSONObject json = new JSONObject();
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_10) || divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_12)) {
			return loadClassItem2(divideId, studentId, subjectIds);
		}
		boolean isMoveWL=false;//是否根据限定行政班--根据合并模式class_type=3;
		boolean isNotA=false;//不安排选考物理或者历史 按行政班上课--跟随class_type=3;
		boolean isNotB=false;//不安排选考物理或者历史 按行政班上课--跟随class_type=3;
		boolean isShowABetter=false;//是否显示班级学生选考对应的教学班
		boolean isShowBBetter=false;//是否显示班级学生学考对应的教学班
		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_11)) {
			isMoveWL=true;
			/**
			  A-1,B-0:物理历史选考根据预先的分配组合结果物理历史班分层，物理历史学考跟选考结果无关，独立走班---都是走批次点 
			            （预先的分配组合结果只是一个过渡，学生暂时应该只能位于存在有该历史或者物理科目的行政班，优先同时选考去向该分配组合的学生所在选考班）---所有批次点 只要保证批次点不冲突
			  A-1,B-1：物理历史选考根据预先的分配组合结果物理历史班分层，物理历史学考根据预先的分配组合结果物理历史班分层---都是走批次点 
			                跟A-1,B-0：主要行政班限制，一致方式选考+学考优先同时选考去向该分配组合的学生所在选考班 在行政班这边显示优先选学考班级信息
			  A-1,B-2：物理历史选考根据预先的分配组合结果物理历史班分层--物理或者历史学考科目不安排
			*/
			
			/**
			 * A-2,B-0:预先的分配组合结果物理历史班，选考按行政班上课  剩余4门走批次;相当于物理历史学考跟选考结果无关，独立走班
			   A-2,B-2:预先的分配组合结果物理历史班，选考按行政班上课  剩余4门走批次;预先的分配组合结果物理历史班，学考按行政班上课  剩余4门走批次
			*/
			if(divide.getFollowType().indexOf("A-2")>-1 && divide.getFollowType().indexOf("B-2")>-1) {
				isNotA=true;
				isNotB=true;
			}else if(divide.getFollowType().indexOf("A-2")>-1 && divide.getFollowType().indexOf("B-0")>-1) {
				isNotA=true;
				
			}else if(divide.getFollowType().indexOf("A-1")>-1 && divide.getFollowType().indexOf("B-2")>-1){
				isNotB=true;
				isShowABetter=true;
			}else if(divide.getFollowType().indexOf("A-1")>-1 && divide.getFollowType().indexOf("B-1")>-1){
				isShowABetter=true;
				isShowBBetter=true;
			}else if(divide.getFollowType().indexOf("A-1")>-1 && divide.getFollowType().indexOf("B-0")>-1){
				isShowABetter=true;
				
			}else {
				//这个是分班参数不对
				json.put("arrangeBath", "0");
				json.put("divideError", NewGkElectiveConstant.DIVIDE_TYPE_11);
				return json.toString();
			}
		}else if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_09)){
			isShowABetter=true;//因为重组 行政班来源就是教学班选考物理或者历史
			/**
			 * B-0
			 * B-1
			 */
			if(divide.getFollowType().indexOf("B-0")>-1) {
				//学考不受限制
			}else if(divide.getFollowType().indexOf("B-1")>-1) {
				//学考优先本班学生
				isShowBBetter=true;
			}else {
				//这个是分班参数不对
				json.put("arrangeBath", "0");
				json.put("divideError",NewGkElectiveConstant.DIVIDE_TYPE_09);
				return json.toString();
			}
		}
		
		
		//根据开设科目设置
		List<NewGkOpenSubject> openList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId, new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A,NewGkElectiveConstant.SUBJECT_TYPE_B});
		//需要开设
		Set<String> subIdsAll=new HashSet<>();
		Set<String> subIdsA=new HashSet<>();
		Set<String> subIdsB=new HashSet<>();
		for(NewGkOpenSubject oo:openList) {
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(oo.getSubjectType())) {
				if(subjectIds.contains(oo.getSubjectId())) {
					subIdsA.add(oo.getSubjectId());
					subIdsAll.add(oo.getSubjectId());
				}
			}else {
				if(!subjectIds.contains(oo.getSubjectId())) {
					subIdsB.add(oo.getSubjectId());
					subIdsAll.add(oo.getSubjectId());
				}
			}
		}
		
		
		if(CollectionUtils.isEmpty(subIdsAll)) {
			json.put("arrangeBath", "0");
			return json.toString();
		}
		json.put("arrangeBath", "1");
		
		//该年级下所有学生信息
		List<Student> list = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), divide.getGradeId(), null, null), Student.class);
		Map<String, Student> studentMap = EntityUtils.getMap(list, e->e.getId());
		//学生成绩
		String refScoreId=divide.getReferScoreId();
		if(StringUtils.isBlank(refScoreId)){
			refScoreId=newGkReferScoreService.findDefaultIdByGradeId(divide.getUnitId(), divide.getGradeId());
		}
		//各科成绩+语数英成绩---显示需要安排科目的成绩
		Map<String,Map<String,Float>> stuSubMap=new HashMap<>();
		List<NewGkScoreResult> scoreList=new ArrayList<>();
		if(StringUtils.isNotBlank(refScoreId)) {
			scoreList =newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), refScoreId);
			if(CollectionUtils.isNotEmpty(scoreList)) {
				for(NewGkScoreResult r:scoreList) {
					subIdsAll.add(r.getSubjectId());
					if(!stuSubMap.containsKey(r.getStudentId())) {
						stuSubMap.put(r.getStudentId(), new HashMap<>());
					}
					stuSubMap.get(r.getStudentId()).put(r.getSubjectId(),  r.getScore()==null?0.0f:r.getScore());
				}
			}
		}
		
		List<Course> courseList = SUtils.dt(courseRemoteService
				.findListByIds(subIdsAll.toArray(new String[0])),
				new TR<List<Course>>() {
				});
		Map<String, Course>	subjectNameMap = EntityUtils.getMap(courseList,e->e.getId());
		JSONObject json1=null;
		if(CollectionUtils.isNotEmpty(subIdsA)) {
			JSONArray data1 = new JSONArray();
			for(String t:subIdsA) {
				//去除物理历史
				if(isNotA) {
					if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(subjectNameMap.get(t).getSubjectCode())
							|| NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(subjectNameMap.get(t).getSubjectCode())) {
						continue;
					}
				}
				json1 = new JSONObject();
				json1.put("courseId", t);
				json1.put("courseName", subjectNameMap.get(t).getSubjectName());
				data1.add(json1);
			}
			json.put("subA", data1);
		}
		if(CollectionUtils.isNotEmpty(subIdsB)) {
			JSONArray data1 = new JSONArray();
			for(String t:subIdsB) {
				if(isNotB) {
					//去除物理历史
					if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(subjectNameMap.get(t).getSubjectCode())
							|| NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(subjectNameMap.get(t).getSubjectCode())) {
						continue;
					}
				}
				json1 = new JSONObject();
				json1.put("courseId", t);
				json1.put("courseName", subjectNameMap.get(t).getSubjectName());
				data1.add(json1);
			}
			json.put("subB", data1);
		}
		
		
		
		List<NewGkClassStudent> classStuList = newGkClassStudentService.findListByStudentId(divide.getUnitId(), divideId, studentId);
		String oldClassIds="";
		if(CollectionUtils.isNotEmpty(classStuList)) {
			oldClassIds=ArrayUtil.print(EntityUtils.getSet(classStuList, e->e.getClassId()).toArray(new String[0]));
		}
		json.put("oldClassIds", oldClassIds);
		
		//根据选课结果subjectIds 获取所有的班级 以及信息 还有各种关联性
		List<NewGkDivideClass> classlist = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId,null ,true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> xzbList = classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toList());
		List<NewGkDivideClass> zhbList = classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_0)).collect(Collectors.toList());
		Map<String, NewGkDivideClass> zhbMap = EntityUtils.getMap(zhbList,e->e.getId());
		//过滤班级
		List<NewGkDivideClass> jxbList = classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_2) && StringUtils.isNotBlank(e.getBatch())).collect(Collectors.toList());
		
		//暂时只考虑物理+历史
		Map<String,List<NewGkDivideClass>> jxbByStu=new HashMap<>();
		for(NewGkDivideClass n:jxbList) {
			if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(subjectNameMap.get(n.getSubjectIds()).getSubjectCode())
					|| NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(subjectNameMap.get(n.getSubjectIds()).getSubjectCode())) {
				if(CollectionUtils.isNotEmpty(n.getStudentList())) {
					for(String cs:n.getStudentList()) {
						if(!jxbByStu.containsKey(cs)) {
							jxbByStu.put(cs, new ArrayList<>());
						}
						jxbByStu.get(cs).add(n);
					}
				}
			}
			
		}
		
		//预设物理历史合并班
		List<NewGkDivideClass> class3List=classlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
		List<String> existClazzList=new ArrayList<>();
		//行政班班级所在的合并班级数据
		Map<String,NewGkDivideClass> relateMap=new HashMap<>();
		//存在选物理 或者历史的
		if(CollectionUtils.isNotEmpty(class3List)) {
			for(NewGkDivideClass c:class3List) {
				if(subjectIds.contains(c.getSubjectIds())) {
					for(String kk:c.getRelateId().split(",")) {
						existClazzList.add(kk);
						relateMap.put(kk, c);
					}	
				}
			}
		}
		
		
		Map<String,List<XzbJxbDivideClassDto>> subjectBathAMap=new HashMap<>();
		Map<String,List<XzbJxbDivideClassDto>> subjectBathBMap=new HashMap<>();
		Map<String,List<NewGkDivideClass>> jxbByzhbMap=new HashMap<>();
		XzbJxbDivideClassDto classDto=null;
		Map<String,List<String>> stuExistBathMap=new HashMap<>();
		for(NewGkDivideClass v:jxbList) {
			if(CollectionUtils.isNotEmpty(v.getStudentList())) {
				for(String s:v.getStudentList()) {
					if(!stuExistBathMap.containsKey(s)) {
						stuExistBathMap.put(s, new ArrayList<>());
					}
					stuExistBathMap.get(s).add(v.getSubjectType()+v.getBatch());
				}
			}
			String key=v.getSubjectIds()+"_"+v.getBatch();
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(v.getSubjectType()) && subIdsA.contains(v.getSubjectIds())) {
				if(!subjectBathAMap.containsKey(key)) {
					subjectBathAMap.put(key, new ArrayList<>());
				}
				classDto=makeDto(v);
				float ff=0.0f;
				if(CollectionUtils.isNotEmpty(v.getStudentList())) {
					classDto.setStuNum(v.getStudentList().size());
					int b=0;
					int m=0;
					double chooseall=0.0;
					for(String st:v.getStudentList()) {
						if(studentMap.containsKey(st)) {
							if(Objects.equal(1, studentMap.get(st).getSex())) {
								//男
								b++;
							}else {
								m++;
							}
						}
						//暂时不考虑学生找不到情况
						Map<String, Float> oneScoreMap = stuSubMap.get(st);
						if(oneScoreMap!=null && oneScoreMap.containsKey(v.getSubjectIds())) {
							chooseall=chooseall+oneScoreMap.get(v.getSubjectIds());
						}
					}
					classDto.setBoyNum(b);
					classDto.setGirlNum(m);
					ff = makeDoubleTwo(chooseall/v.getStudentList().size());
				}
				List<String[]> scoreList1=new ArrayList<>();
				scoreList1.add(new String[] {subjectNameMap.get(v.getSubjectIds()).getSubjectName(),String.valueOf(ff)});
				classDto.setScoreList(scoreList1);
				if(StringUtils.isNotBlank(v.getRelateId())) {
					classDto.setZhbId(v.getRelateId());
				}
				subjectBathAMap.get(key).add(classDto);
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(v.getSubjectType()) && subIdsB.contains(v.getSubjectIds())) {
				if(!subjectBathBMap.containsKey(key)) {
					subjectBathBMap.put(key, new ArrayList<>());
				}
				classDto=makeDto(v);
				float ff=0.0f;
				if(CollectionUtils.isNotEmpty(v.getStudentList())) {
					classDto.setStuNum(v.getStudentList().size());
					int b=0;
					int m=0;
					double chooseall=0.0;
					for(String st:v.getStudentList()) {
						if(studentMap.containsKey(st)) {
							if(Objects.equal(1, studentMap.get(st).getSex())) {
								//男
								b++;
							}else {
								m++;
							}
						}
						//暂时不考虑学生找不到情况
						Map<String, Float> oneScoreMap = stuSubMap.get(st);
						if(oneScoreMap!=null && oneScoreMap.containsKey(v.getSubjectIds())) {
							chooseall=chooseall+oneScoreMap.get(v.getSubjectIds());
						}
					}
					classDto.setBoyNum(b);
					classDto.setGirlNum(m);
					ff = makeDoubleTwo(chooseall/v.getStudentList().size());
				}
				List<String[]> scoreList1=new ArrayList<>();
				scoreList1.add(new String[] {subjectNameMap.get(v.getSubjectIds()).getSubjectName()+"(学)",String.valueOf(ff)});
				classDto.setScoreList(scoreList1);
				if(StringUtils.isNotBlank(v.getRelateId())) {
					classDto.setZhbId(v.getRelateId());
				}
				subjectBathBMap.get(key).add(classDto);
			}else {
				continue;
			}
			if(StringUtils.isNotBlank(v.getRelateId())) {
				if(!jxbByzhbMap.containsKey(v.getRelateId())) {
					jxbByzhbMap.put(v.getRelateId(), new ArrayList<>());
				}
				jxbByzhbMap.get(v.getRelateId()).add(v);
			}
		}
		
		json.put("subjectBathAMap", subjectBathAMap);
		json.put("subjectBathBMap", subjectBathBMap);
		
		
		List<XzbJxbDivideClassDto> chooseXzbList=new ArrayList<>();
		
		List<String> ysyCode =Arrays.asList(BaseConstants.SUBJECT_TYPES_YSY) ;
		for(NewGkDivideClass x:xzbList) {
			classDto=null;
			if(StringUtils.isNotBlank(x.getRelateId()) && zhbMap.containsKey(x.getRelateId())) {
				NewGkDivideClass zhbClass = zhbMap.get(x.getRelateId());
				boolean b = false;
				if(!NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(zhbClass.getSubjectType())) {
					for(String s:zhbClass.getSubjectIds().split(",")) {
						if(!subjectIds.contains(s)) {
							b=true;
						    break;
						}
					}
				}
				if(b) {
					continue;
				}
				classDto = makeDto(x);
				classDto.setZhbId(zhbClass.getId());
				
				List<NewGkDivideClass> relaJxbList = jxbByzhbMap.get(zhbClass.getId());
				Set<String> ids=new HashSet<>();
				if(CollectionUtils.isNotEmpty(relaJxbList)) {
					ids = EntityUtils.getSet(relaJxbList, e->e.getSubjectIds());
				}
				List<String> xzbA=new ArrayList<>();
				List<String> xzbB=new ArrayList<>();
				if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(zhbClass.getSubjectType())) {
					//行政班没有A
					if(StringUtils.isNotBlank(zhbClass.getSubjectIdsB())) {
						String[] arr = zhbClass.getSubjectIdsB().split(",");
						for(String s:arr) {
							if(subIdsB.contains(s) && !ids.contains(s)) {
								xzbB.add(s);
							}
						}
					}
				}else if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(zhbClass.getSubjectType())){
					String[] subIds = zhbClass.getSubjectIds().split(",");
					for(String s:subIds) {
						if(subIdsA.contains(s) && !ids.contains(s)) {
							xzbA.add(s);
						}
					}
					for(String s:subIdsB) {
						if(zhbClass.getSubjectIds().contains(s)) {
							continue;
						}
						if(!ids.contains(s)) {
							xzbB.add(s);
						}
					}
					
				}else {
					String[] subIds = zhbClass.getSubjectIds().split(",");
					for(String s:subIds) {
						if(subIdsA.contains(s) && !ids.contains(s)) {
							xzbA.add(s);
						}
					}
					if(StringUtils.isNotBlank(zhbClass.getSubjectIdsB())) {
						String[] arr = zhbClass.getSubjectIdsB().split(",");
						for(String s:arr) {
							if(subIdsB.contains(s) && !ids.contains(s)) {
								xzbB.add(s);
							}
						}
					}
				}
				if(CollectionUtils.isNotEmpty(xzbA)) {
					classDto.setSubjectAIds(xzbA.toArray(new String[0]));
					Arrays.sort(classDto.getSubjectAIds());
					String ss="";
					for(String s:classDto.getSubjectAIds()) {
						ss=ss+(StringUtils.isNotBlank(subjectNameMap.get(s).getShortName())?subjectNameMap.get(s).getShortName():subjectNameMap.get(s).getSubjectName());
					}
					classDto.setSubjectANames(ss);
				}
				if(CollectionUtils.isNotEmpty(xzbB)) {
					classDto.setSubjectBIds(xzbB.toArray(new String[0]));
					Arrays.sort(classDto.getSubjectBIds());
					String ss="";
					for(String s:classDto.getSubjectBIds()) {
						ss=ss+(StringUtils.isNotBlank(subjectNameMap.get(s).getShortName())?subjectNameMap.get(s).getShortName():subjectNameMap.get(s).getSubjectName());
					}
					classDto.setSubjectBNames(ss);
				}
				
				//暂时设定安排时间是这个班级下学生批次教学班最大值--暂时不控制这个 不好说明具体需要多少批次
				//首先可能某个科目不安排 正好也是这个班级的行政班课程 但是subjectB未说明 或者这个班级物生班 但是里面人员都是物化生 化学不安排 
				String[] arrangeBath=null;
//				if(CollectionUtils.isNotEmpty(x.getStudentList())) {
//					Set<String> time=new HashSet<>();
//					for(String s:x.getStudentList()) {
//						if(stuExistBathMap.containsKey(s)) {
//							time.addAll(stuExistBathMap.get(s));
//						}
//					}
//					if(CollectionUtils.isNotEmpty(time)) {
//						arrangeBath=time.toArray(new String[0]);
//					}
//				}
				classDto.setArrangeBath(arrangeBath);
			}else {
				
				if(isMoveWL) {
					//行政班过滤只能进入有该科目的行政班
					if(!existClazzList.contains(x.getId())) {
						continue;
					}
				}
				
				classDto=makeDto(x);
				
				List<String> stuList=new ArrayList<>();
				if(isMoveWL) {
					NewGkDivideClass cc3 = relateMap.get(x.getId());
					stuList=cc3.getStudentList();
				}else if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_09)) {
					stuList=x.getStudentList();
				}
				List<NewGkDivideClass> aclass=new ArrayList<>();
				List<NewGkDivideClass> bclass=new ArrayList<>();
				if(CollectionUtils.isNotEmpty(stuList)) {
					for(String cs:stuList) {
						//学生所在的教学班
						List<NewGkDivideClass> class2 = jxbByStu.get(cs);
						if(CollectionUtils.isNotEmpty(class2)) {
							for(NewGkDivideClass n:class2) {
								if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType()) && !aclass.contains(n)) {
									aclass.add(n);
								}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(n.getSubjectType())&& !bclass.contains(n)) {
									bclass.add(n);
								}
							}
						}
					}
				}
				if(isShowABetter && CollectionUtils.isNotEmpty(aclass)) {
					classDto.setShowBetterA(aclass.stream().map(e -> e.getClassName()).collect(Collectors.joining(",")));
				}
				if(isShowBBetter && CollectionUtils.isNotEmpty(bclass)) {
					classDto.setShowBetterB(bclass.stream().map(e -> e.getClassName()).collect(Collectors.joining(",")));
				}
				
			}
			if(classDto!=null) {
				makeXzbTip(classDto, x.getStudentList(), studentMap, stuSubMap, subjectNameMap, ysyCode);
				chooseXzbList.add(classDto);
			}
			
			
		}
		json.put("xzbList", chooseXzbList);
		return json.toString();
	}
	
	private XzbJxbDivideClassDto makeDto(NewGkDivideClass x) {
		XzbJxbDivideClassDto dto=new XzbJxbDivideClassDto();
		if(NewGkElectiveConstant.CLASS_TYPE_2.equals(x.getClassType())) {
			dto.setSubjectId(x.getSubjectIds());
			dto.setSubjectType(x.getSubjectType());
		}
		dto.setId(x.getId());
		dto.setName(x.getClassName());
		return dto;
	}
	
	class XzbJxbDivideClassDto{
		private String id;
		private String name;
		
		//行政班数据特有上课科目参数
		private String[] subjectAIds;
		private String subjectANames;
		private String[] subjectBIds;
		private String subjectBNames;
		private String[] arrangeBath;//维护的批次点
		
		private String showBetterA;//优先选择班级
		private String showBetterB;
		
		
		private String subjectType;//主要教学班用 但是组合模式时 存放组合班类型
		//教学班
		private String subjectId;
		
		private List<String[]> scoreList; //subjectName,score
		private String zhbId;
		private int stuNum;
		private int boyNum;
		private int girlNum;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String[] getSubjectAIds() {
			return subjectAIds;
		}
		public void setSubjectAIds(String[] subjectAIds) {
			this.subjectAIds = subjectAIds;
		}
		public String getSubjectANames() {
			return subjectANames;
		}
		public void setSubjectANames(String subjectANames) {
			this.subjectANames = subjectANames;
		}
		public String[] getSubjectBIds() {
			return subjectBIds;
		}
		public void setSubjectBIds(String[] subjectBIds) {
			this.subjectBIds = subjectBIds;
		}
		public String getSubjectBNames() {
			return subjectBNames;
		}
		public void setSubjectBNames(String subjectBNames) {
			this.subjectBNames = subjectBNames;
		}
		public String getZhbId() {
			return zhbId;
		}
		public void setZhbId(String zhbId) {
			this.zhbId = zhbId;
		}
		public int getStuNum() {
			return stuNum;
		}
		public void setStuNum(int stuNum) {
			this.stuNum = stuNum;
		}
		public int getBoyNum() {
			return boyNum;
		}
		public void setBoyNum(int boyNum) {
			this.boyNum = boyNum;
		}
		public int getGirlNum() {
			return girlNum;
		}
		public void setGirlNum(int girlNum) {
			this.girlNum = girlNum;
		}
		public List<String[]> getScoreList() {
			return scoreList;
		}
		public void setScoreList(List<String[]> scoreList) {
			this.scoreList = scoreList;
		}
		public String getSubjectType() {
			return subjectType;
		}
		public void setSubjectType(String subjectType) {
			this.subjectType = subjectType;
		}
		public String getSubjectId() {
			return subjectId;
		}
		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}
		public String[] getArrangeBath() {
			return arrangeBath;
		}
		public void setArrangeBath(String[] arrangeBath) {
			this.arrangeBath = arrangeBath;
		}
		public String getShowBetterA() {
			return showBetterA;
		}
		public void setShowBetterA(String showBetterA) {
			this.showBetterA = showBetterA;
		}
		public String getShowBetterB() {
			return showBetterB;
		}
		public void setShowBetterB(String showBetterB) {
			this.showBetterB = showBetterB;
		}
		
	
		
		
	}
	@ResponseBody
	@RequestMapping("/{divideId}/saveStudentClass")
	@ControllerInfo("保存学生")
	public String saveStudentClass(@PathVariable String divideId,String studentId,String subjectIds,String classIds) {
		if(StringUtils.isBlank(studentId)) {
			return error("学生参数丢失");
		}
		if(StringUtils.isBlank(subjectIds)) {
			return error("选课科目参数丢失");
		}
		if(StringUtils.isBlank(classIds)) {
			return error("班级参数丢失");
		}
		try {
			String mess=newGkDivideClassService.saveChangeByStudentId(divideId,studentId,subjectIds,classIds,getLoginInfo().getRealName());
			if(StringUtils.isNotBlank(mess)){
				return error(mess);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}
		
		return success("");
	}
	@ResponseBody
	@RequestMapping("/{divideId}/checkStudentChoose")
	@ControllerInfo("保存学生")
	public String checkStudentChoose(@PathVariable String divideId,String subjectIds) {
		if(StringUtils.isBlank(subjectIds)) {
			return error("科目参数不对");
		}
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		if(divide==null) {
			return error("分班方案不存在");
		}
		String mess=newGkDivideClassService.checkChoose(divide, subjectIds);
		if(StringUtils.isNotBlank(mess)){
			return error(mess);
		}
		return success("");
	}
	@ResponseBody
	@RequestMapping("/{divideId}/findStudentList")
	@ControllerInfo("查学生")
	public String findStudentList(String clazzId) {
		List<Student> studentList=null;
		if(StringUtils.isNotBlank(clazzId)) {
			studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(getLoginInfo().getUnitId(), null, new String[] {clazzId}, null),new TR<List<Student>>() {});
		}
		JSONArray data = new JSONArray();
		JSONObject json1;
		if(CollectionUtils.isNotEmpty(studentList)) {
			for(Student s:studentList) {
				json1 = new JSONObject();
				json1.put("studentId", s.getId());
				json1.put("studentName",s.getStudentName());
				data.add(json1);
			}
		}
		return data.toString();
	}
	
	
	@RequestMapping("/{divideId}/changeStudentByBatch/page")
	@ControllerInfo(value = "同批次调班")
	public String changeStudentByBatch(@PathVariable String divideId,String jxbId,String type,ModelMap map) {
		map.put("divideId", divideId);
		map.put("type", type);
		NewGkDivideClass chooseClass = newGkDivideClassService.findOne(jxbId);
		map.put("chooseClass", chooseClass);
		return "/newgkelective/divide/divideChange/changeSameBatch.ftl";
	}
}


