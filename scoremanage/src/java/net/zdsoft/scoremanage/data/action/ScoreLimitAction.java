package net.zdsoft.scoremanage.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSaveDto;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限
 */
@Controller
@RequestMapping("/scoremanage")
public class ScoreLimitAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private ScoreLimitService scoreLimitService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private NotLimitService notLimitService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassInfoService classInfoService;
	
	@RequestMapping("/scoreLimit/index/page")
	@ControllerInfo(value = "录入权限设置index")
	public String showList(ModelMap map){
		String unitId=getLoginInfo().getUnitId();
		List<String> teacherIdsList = notLimitService.findTeacherIdByUnitId(unitId);
		//teacherIds teacherNames
		if(CollectionUtils.isNotEmpty(teacherIdsList)){
			List<Teacher> tList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdsList.toArray(new String[]{})),new TR<List<Teacher>>(){});
			if(CollectionUtils.isNotEmpty(tList)){
				String teacherIds="";
				String teacherNames="";
				for(Teacher t:tList){
					teacherIds=teacherIds+","+t.getId();
					teacherNames=teacherNames+","+t.getTeacherName();
				}
				if(StringUtils.isNotBlank(teacherIds)){
					teacherIds=teacherIds.substring(1);
					teacherNames=teacherNames.substring(1);
				}
				map.put("teacherIds", teacherIds);
				map.put("teacherNames", teacherNames);
			}
		}
		return "/scoremanage/scoreLimit/scoreLimitIndex.ftl"; 
	}
	
	@ResponseBody
	@RequestMapping("/scoreLimit/saveNotLimit")
    @ControllerInfo(value = "保存总权限录分人员")
	public String saveNotLimit(String teacherIds, HttpSession httpSession){
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId=loginInfo.getUnitId();
		String[] teacherArr=null;
		if(StringUtils.isNotBlank(teacherIds)){
			teacherArr=teacherIds.split(",");
		}
		try{
			notLimitService.saveTeacherIds(teacherArr, unitId);
		}catch(Exception e){
			e.printStackTrace();
			returnError();
		}
		return success("保存录分总管理员成功！");		
	}
	
	@RequestMapping("/scoreLimit/tabHead/page")
	@ControllerInfo(value = "录入权限设置index")
	public String showTabHead(ModelMap map,String type){
		//学年学期
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("unitId", unitId);
		if("1".equals(type)){
			//必修课
			map.put("type", "1");
			 
		}else if("2".equals(type)){
			//选修课
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),new TR<List<Grade>>(){});
		    map.put("gradeList", gradeList);
		    map.put("examId", BaseConstants.ZERO_GUID);
		    map.put("type", "2");
		}else{
			return errorFtl(map, "参数丢失，请重新加载！");
		}
		return "/scoremanage/scoreLimit/scoreLimitHead.ftl";
		
	}
	
	@RequestMapping("/scoreLimit/limitList/page")
	@ControllerInfo(value = "录入权限列表")
	public String showLimitList1(ModelMap map,ScoreLimitSearchDto dto){
		dto.setUnitId(getLoginInfo().getUnitId());
		//返回数据
		List<ScoreLimitSaveDto> dtoList=new ArrayList<ScoreLimitSaveDto>();
		//行政班
		List<Clazz> xzbList=new ArrayList<Clazz>();
		//教学班
		List<TeachClass> jxbList=new ArrayList<TeachClass>();
		if("1".equals(dto.getType())){
			//必修课
			//根据考试科目id 拿到考试下所有考试科目信息
			Set<String> classIds = new HashSet<String>();//页面展现班级
			if(StringUtils.isNotBlank(dto.getClassId())){
				dto.setClassIds(new String[]{dto.getClassId()});
				classIds.add(dto.getClassId());
			}else{
				//根据学年学期考试获得班级id
				List<SubjectInfo> infoList=subjectInfoService.findByExamIdAndCourseId(dto.getExamId(),dto.getSubjectId());
				if(CollectionUtils.isNotEmpty(infoList)){
					Set<String> subInfoId = EntityUtils.getSet(infoList, "id");
					List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(dto.getUnitId(), subInfoId.toArray(new String[]{}));
					if(CollectionUtils.isNotEmpty(classInfoList)){
						classIds=EntityUtils.getSet(classInfoList, "classId");
					}
				}
			}
			//classIds
			if(classIds.size()>0){
				xzbList = SUtils.dt(classService.findListByIds(classIds.toArray(new String[]{})),new TR<List<Clazz>>(){});
				if(CollectionUtils.isNotEmpty(xzbList)){
					Set<String> xzbClassId = EntityUtils.getSet(xzbList, "id");
					classIds.removeAll(xzbClassId);
				}
				if(classIds.size()>0){
					jxbList = SUtils.dt(teachClassService.findListByIds(classIds.toArray(new String[]{})),new TR<List<TeachClass>>(){});
					Iterator<TeachClass> iterator = jxbList.iterator();
					while(iterator.hasNext()){
						if(Constant.IS_FALSE_Str.equals(iterator.next().getIsUsing())){
							iterator.remove();
						}
					}
				}
			}
			
		}else if("2".equals(dto.getType())){
			//选修课
			//取得教学班id
			jxbList = SUtils.dt(teachClassService.findBySearch(dto.getUnitId(),dto.getAcadyear(),dto.getSemester(),TeachClass.CLASS_TYPE_ELECTIVE,dto.getGradeId(),dto.getSubjectId()),new TR<List<TeachClass>>(){});
			Iterator<TeachClass> iterator = jxbList.iterator();
    		while(iterator.hasNext()){
    			TeachClass teachClass = iterator.next();
				if(Constant.IS_TRUE_Str.equals(teachClass.getIsUsingMerge())||Constant.IS_FALSE_Str.equals(teachClass.getIsUsing())){
					iterator.remove();
				}
			}
		}else{
			return errorFtl(map, "参数丢失，请重新加载！");
		}
		Map<String,Set<String>> teacherIdsByClassId=new HashMap<String,Set<String>>();
		//根据学年学期单位科目
		List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(dto);
		Set<String> teacherIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(limitList)){
			for(ScoreLimit sl:limitList){
				if(!teacherIdsByClassId.containsKey(sl.getClassId())){
					teacherIdsByClassId.put(sl.getClassId(), new HashSet<String>());
				}
				teacherIdsByClassId.get(sl.getClassId()).add(sl.getTeacherId());
				teacherIds.add(sl.getTeacherId());
			}
		}
		Map<String, Teacher> teacherMap=new HashMap<String, Teacher>();
		if(teacherIds.size()>0){
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[]{})),new TR<List<Teacher>>(){});
			if(CollectionUtils.isNotEmpty(teacherList)){
				teacherMap=EntityUtils.getMap(teacherList, "id");
			}
		}
		//组装数据
		ScoreLimitSaveDto scoreLimitSaveDto=null;
		Set<String> set=null;
		if(CollectionUtils.isNotEmpty(xzbList)){
			for(Clazz zz:xzbList){
				scoreLimitSaveDto=new ScoreLimitSaveDto();
				scoreLimitSaveDto.setClassId(zz.getId());
				scoreLimitSaveDto.setClassName(zz.getClassNameDynamic());
				scoreLimitSaveDto.setClassType(ScoreDataConstants.CLASS_TYPE1);
				set = teacherIdsByClassId.get(zz.getId());
				Map<String, String> tMap = new HashMap<String,String>();
				if(CollectionUtils.isNotEmpty(set)){
					for(String s:set){
						if(teacherMap.containsKey(s)){
							tMap.put(teacherMap.get(s).getId(),teacherMap.get(s).getTeacherName());
						}
					}
				}
				scoreLimitSaveDto.setTeacherMap(tMap);
				dtoList.add(scoreLimitSaveDto);
			}
		}
		if(CollectionUtils.isNotEmpty(jxbList)){
			for(TeachClass zz:jxbList){
				scoreLimitSaveDto=new ScoreLimitSaveDto();
				scoreLimitSaveDto.setClassId(zz.getId());
				scoreLimitSaveDto.setClassName(zz.getName());
				scoreLimitSaveDto.setClassType(ScoreDataConstants.CLASS_TYPE2);
				set = teacherIdsByClassId.get(zz.getId());
				Map<String, String> tMap = new HashMap<String,String>();
				if(CollectionUtils.isNotEmpty(set)){
					for(String s:set){
						if(teacherMap.containsKey(s)){
							tMap.put(teacherMap.get(s).getId(),teacherMap.get(s).getTeacherName());
						}
					}
				}
				scoreLimitSaveDto.setTeacherMap(tMap);
				dtoList.add(scoreLimitSaveDto);
			}
		}
		map.put("dtoList", dtoList);
		map.put("searchSubjectId", dto.getSubjectId());
		return "/scoremanage/scoreLimit/scoreLimitList.ftl";
		
	}
	
	private ScoreLimit tomakeNewLimit(ScoreLimitSearchDto dto){
		ScoreLimit scoreLimit=new ScoreLimit();
		scoreLimit.setUnitId(dto.getUnitId());
		scoreLimit.setAcadyear(dto.getAcadyear());
		scoreLimit.setSemester(dto.getSemester());
		scoreLimit.setSubjectId(dto.getSubjectId());
		scoreLimit.setId(UuidUtils.generateUuid());
		scoreLimit.setExamInfoId(dto.getExamId());
		return scoreLimit;
	}
	
	@ResponseBody
    @RequestMapping("/scoreLimit/saveBySubject")
    @ControllerInfo(value = "保存录入权限")
    public String doSaveCourseInfoList(ScoreLimitSearchDto dto) {
		if(StringUtils.isBlank(dto.getAcadyear()) || StringUtils.isBlank(dto.getSubjectId()) ||
				StringUtils.isBlank(dto.getSemester()) || StringUtils.isBlank(dto.getExamId())){
			return error("参数丢失，请刷新后操作");
		}
		if(ArrayUtils.isEmpty(dto.getClassIds())){
			return error("没有选中班级进行赋权");
		}
		if(ArrayUtils.isEmpty(dto.getTeacherIds())){
			return error("没有选中老师进行赋权");
		}
		if(ArrayUtils.isEmpty(dto.getClassTypes())){
			return error("参数丢失，请刷新后操作");
		}
		if(dto.getClassIds().length!=dto.getClassTypes().length){
			return error("参数丢失，请刷新后操作");
		}
		dto.setUnitId(getLoginInfo().getUnitId());
		//根据班级 科目 考试 学年  已有
		List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(dto);
		Map<String,Set<String>> teacherIdsByClassId=new HashMap<String,Set<String>>();
		Set<String> teacherIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(limitList)){
			for(ScoreLimit sl:limitList){
				if(!teacherIdsByClassId.containsKey(sl.getClassId())){
					teacherIdsByClassId.put(sl.getClassId(), new HashSet<String>());
				}
				teacherIdsByClassId.get(sl.getClassId()).add(sl.getTeacherId());
				teacherIds.add(sl.getTeacherId());
			}
		}
		
		List<ScoreLimit> insertList=new ArrayList<ScoreLimit>();
		ScoreLimit scoreLimit=null;
		Set<String> tset=null;
		String[] classArr = dto.getClassIds();
		String[] classTypeArr=dto.getClassTypes();
		for(int i=0;i<classArr.length;i++){
			String classId = classArr[i];
			String classType=classTypeArr[i];
			tset = teacherIdsByClassId.get(classId);
			if(tset==null){
				tset=new HashSet<String>();
			}
			for(String teacherId:dto.getTeacherIds()){
				if(!tset.contains(teacherId)){
					scoreLimit=tomakeNewLimit(dto);
					scoreLimit.setClassId(classId);
					scoreLimit.setClassType(classType);
					scoreLimit.setTeacherId(teacherId);
					insertList.add(scoreLimit);
				}
			}
		}
		
		if(CollectionUtils.isEmpty(insertList)){
			return success("没有需要新增的权限数据！");
		}
		try{
			scoreLimitService.saveAllEntitys(insertList.toArray(new ScoreLimit[]{}));
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("保存成功！");
    }
	@ResponseBody
    @RequestMapping("/scoreLimit/deleteOne")
    @ControllerInfo(value = "保存录入权限")
    public String deleteOne(ScoreLimitSearchDto dto) {
		if(StringUtils.isBlank(dto.getAcadyear()) || StringUtils.isBlank(dto.getSubjectId()) ||
				StringUtils.isBlank(dto.getSemester()) || StringUtils.isBlank(dto.getExamId())){
			return error("参数丢失，请刷新后操作");
		}
		if(StringUtils.isBlank(dto.getClassId())){
			return error("班级参数丢失，请刷新后操作");
		}
		if(StringUtils.isBlank(dto.getTeacherId())){
			return error("教师参数丢失，请刷新后操作");
		}
		dto.setUnitId(getLoginInfo().getUnitId());
		
		try{
			scoreLimitService.deleteByClassIdAndTeacherId(dto);
		
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("删除成功！");
    }
	
	
	@ResponseBody
    @RequestMapping("/scoreLimit/limitInit")
    @ControllerInfo(value = "初始化")
    public String limitInit(ScoreLimitSearchDto dto) {
		if(StringUtils.isBlank(dto.getAcadyear()) || 
				StringUtils.isBlank(dto.getSemester()) || StringUtils.isBlank(dto.getExamId())){
			return error("参数丢失，请刷新后操作");
		}
		if(StringUtils.isBlank(dto.getType())){
			return error("参数丢失，请刷新后操作");
		}

		dto.setUnitId(getLoginInfo().getUnitId());
		//根据单位考试 学年学期  已有
		List<ScoreLimit> limitList = scoreLimitService.findBySearchDto(dto);
		Map<String,Map<String,Set<String>>> oldteacherIdsByClassId=new HashMap<String,Map<String,Set<String>>>();
		if(CollectionUtils.isNotEmpty(limitList)){
			for(ScoreLimit sl:limitList){
				if(!oldteacherIdsByClassId.containsKey(sl.getSubjectId())){
					oldteacherIdsByClassId.put(sl.getSubjectId(), new HashMap<String,Set<String>>());
				}
				if(!oldteacherIdsByClassId.get(sl.getSubjectId()).containsKey(sl.getClassId())){
					oldteacherIdsByClassId.get(sl.getSubjectId()).put(sl.getClassId(), new HashSet<String>());
				}
				oldteacherIdsByClassId.get(sl.getSubjectId()).get(sl.getClassId()).add(sl.getTeacherId());
			}
		}
		List<ScoreLimit> insertList=new ArrayList<ScoreLimit>();
		ScoreLimit scoreLimit=null;
		//key:subjectId key:classId value:teacherIds
		Map<String,Map<String,Set<String>>> teacherIdsBysubjectClass=new HashMap<String,Map<String,Set<String>>>();
		List<TeachClass> jxbList=new ArrayList<TeachClass>();
		Set<String> xzbClassId=new HashSet<String>();//行政班id
		//教学班 --一个科目一个班
		Set<String> jxbClassId=new HashSet<String>();//教学班id
		if("1".equals(dto.getType())){
			ExamInfo examInfo = examInfoService.findOne(dto.getExamId());
			if(examInfo==null){
				
			}
			//必修课
			//根据学年学期考试获得班级id
			List<SubjectInfo> infoList=subjectInfoService.findByExamIdAndCourseId(dto.getExamId(),null);
			//key:classId value:subjectIds  行政班 可以是多个科目
			
			Map<String,Set<String>> xzbsubjectIdByClassId=new HashMap<String,Set<String>>();
			Set<String> xzbSujectId=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(infoList)){
				Map<String, SubjectInfo> subjectInfoMap = EntityUtils.getMap(infoList, "id");
				Set<String> subInfoId = EntityUtils.getSet(infoList, "id");
				List<ClassInfo> classInfoList = classInfoService.findBySchoolIdAndSubjectInfoIdIn(dto.getUnitId(), subInfoId.toArray(new String[]{}));
				if(CollectionUtils.isNotEmpty(classInfoList)){
					for(ClassInfo c:classInfoList){
						if(ScoreDataConstants.CLASS_TYPE1.equals(c.getClassType())){
							xzbClassId.add(c.getClassId());
							if(!xzbsubjectIdByClassId.containsKey(c.getClassId())){
								xzbsubjectIdByClassId.put(c.getClassId(), new HashSet<String>());
							}
							xzbsubjectIdByClassId.get(c.getClassId()).add(subjectInfoMap.get(c.getSubjectInfoId()).getSubjectId());
							xzbSujectId.add(subjectInfoMap.get(c.getSubjectInfoId()).getSubjectId());
						}else if(ScoreDataConstants.CLASS_TYPE2.equals(c.getClassType())){
							jxbClassId.add(c.getClassId());
						}
					}
				}
			}
			//获取该学年学期行政班教师上课情况TeacherIdsBysubjectClass
			/**
			 * xzbClassId  xzbSujectId行政班参与的科目
			 * List<ClassTeaching>
			 */
			if(xzbClassId.size()>0){
				List<ClassTeaching> classTeachingList=SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(getLoginInfo().getUnitId(),examInfo.getAcadyear(),examInfo.getSemester(),xzbClassId.toArray(new String[]{}),xzbSujectId.toArray(new String[]{}),true), new TR<List<ClassTeaching>>(){});
				if(CollectionUtils.isNotEmpty(classTeachingList)){
					for(ClassTeaching c:classTeachingList){
						if(CollectionUtils.isEmpty(xzbsubjectIdByClassId.get(c.getClassId()))){
							continue;
						}
						if(!xzbsubjectIdByClassId.get(c.getClassId()).contains(c.getSubjectId())){
							continue;
						}
						Set<String> teacherSet=new HashSet<String>();
						if(CollectionUtils.isNotEmpty(c.getTeacherIds())){
							teacherSet.addAll(c.getTeacherIds());
						}
						if(StringUtils.isNotBlank(c.getTeacherId())){
							teacherSet.add(c.getTeacherId());
						}
						if(CollectionUtils.isEmpty(teacherSet)){
							continue;
						}
						if(!teacherIdsBysubjectClass.containsKey(c.getSubjectId())){
							teacherIdsBysubjectClass.put(c.getSubjectId(), new HashMap<String,Set<String>>());
						}
						
						if(!teacherIdsBysubjectClass.get(c.getSubjectId()).containsKey(c.getClassId())){
							teacherIdsBysubjectClass.get(c.getSubjectId()).put(c.getClassId(), new HashSet<String>());
						}
						//取得教师
						teacherIdsBysubjectClass.get(c.getSubjectId()).get(c.getClassId()).addAll(teacherSet);
					}
				}
				
			}

			//获取教学班
			if(jxbClassId.size()>0){
				jxbList = SUtils.dt(teachClassService.findListByIds(jxbClassId.toArray(new String[]{})),new TR<List<TeachClass>>(){});
				
			}
			
		}else if("2".equals(dto.getType())){
			//选修课
			//取得教学班id
			jxbList = SUtils.dt(teachClassService.findBySearch(dto.getUnitId(),dto.getAcadyear(),dto.getSemester(),TeachClass.CLASS_TYPE_ELECTIVE,null,null),new TR<List<TeachClass>>(){});
			
		}else{
			return error("参数丢失，请刷新后操作");
		}
		if(CollectionUtils.isNotEmpty(jxbList)){
			for(TeachClass t:jxbList){
				if(!teacherIdsBysubjectClass.containsKey(t.getCourseId())){
					teacherIdsBysubjectClass.put(t.getCourseId(), new HashMap<String,Set<String>>());
				}
				
				if(!teacherIdsBysubjectClass.get(t.getCourseId()).containsKey(t.getId())){
					teacherIdsBysubjectClass.get(t.getCourseId()).put(t.getId(), new HashSet<String>());
				}
				//取得教师
				Set<String> tSet=new HashSet<String>();
				if(StringUtils.isNotBlank(t.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(t.getTeacherId()))){
					tSet.add(t.getTeacherId());
				}
				if(StringUtils.isNotBlank(t.getAssistantTeachers())){
					if(t.getAssistantTeachers().indexOf(",")>=0){
						String[] arr = t.getAssistantTeachers().split(",");
						tSet.addAll(Arrays.asList(arr));
					}else{
						tSet.add(t.getAssistantTeachers());
					}
				}
				if(CollectionUtils.isNotEmpty(tSet)){
					teacherIdsBysubjectClass.get(t.getCourseId()).get(t.getId()).addAll(tSet);
				}
			}
		}
		
		if(teacherIdsBysubjectClass.size()>0){
			for(Entry<String, Map<String, Set<String>>> item:teacherIdsBysubjectClass.entrySet()){
				String key = item.getKey();
				Map<String, Set<String>> value = item.getValue();
				Map<String, Set<String>> teacherIdsByClass = oldteacherIdsByClassId.get(key);
				if(teacherIdsByClass==null){
					teacherIdsByClass=new HashMap<String, Set<String>>();
				}
				if(value!=null && value.size()>0){
					for( Entry<String, Set<String>> item1:value.entrySet()){
						String key1 = item1.getKey();
						Set<String> sets = item1.getValue();
						Set<String> oldTeachers = teacherIdsByClass.get(key1);
						if(oldTeachers==null){
							oldTeachers=new HashSet<String>();
						}
						if(CollectionUtils.isNotEmpty(sets)){
							for(String s:sets){
								if(!oldTeachers.contains(s)){
									scoreLimit=tomakeNewLimit(dto);
									scoreLimit.setSubjectId(key);;
									scoreLimit.setClassId(key1);
									if(xzbClassId.contains(key1)){
										scoreLimit.setClassType(ScoreDataConstants.CLASS_TYPE1);
									}else{
										scoreLimit.setClassType(ScoreDataConstants.CLASS_TYPE2);
									}
									
									scoreLimit.setTeacherId(s);
									insertList.add(scoreLimit);
								}
							}
						}
					}
				}
			}
			
		}
		if(CollectionUtils.isEmpty(insertList)){
			return success("没有需要新增的权限数据！");
		}
		try{
			scoreLimitService.saveAllEntitys(insertList.toArray(new ScoreLimit[]{}));
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return success("操作成功！");
    }
}
