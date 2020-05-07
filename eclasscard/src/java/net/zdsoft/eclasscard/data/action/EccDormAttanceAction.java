package net.zdsoft.eclasscard.data.action;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.eclasscard.data.dto.AttanceSearchDto;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.eclasscard.data.entity.EccStudormAttence;
import net.zdsoft.eclasscard.data.service.EccAttenceDormGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceDormPeriodService;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.eclasscard.data.service.EccStudormAttenceService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/eclasscard")
public class EccDormAttanceAction extends BaseAction{
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private EccAttenceDormPeriodService eccAttenceDormPeriodService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private EccStudormAttenceService eccStudormAttenceService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
	private EccDormAttenceService eccDormAttenceService;
	
	@RequestMapping("/dorm/attance/index/page")
	public String attanceIndex(ModelMap map){
		return "/eclasscard/dorm/attanceIndex.ftl";
	}
	@RequestMapping("/dorm/attance/list/page")
	public String attanceList(ModelMap map,AttanceSearchDto attDto,HttpServletRequest request){
		String unitId=getLoginInfo().getUnitId();
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(se==null){
			return errorFtl(map, "当前时间不在学年学期范围内");
		}
		if(attDto.getSearchDate()==null){
			attDto.setSearchDate(new Date());
		}
		List<DormBuildingDto> buildingList=new ArrayList<DormBuildingDto>();
		JSONArray array=JSONArray.parseArray(stuworkRemoteService.getBuildingSbyUnitId(unitId));
		for(int i=0;i<array.size();i++){
			DormBuildingDto building=new DormBuildingDto();
			JSONObject json=array.getJSONObject(i);
			building.setBuildingId(json.getString("buildingId"));
			building.setBuildingName(json.getString("buildingName"));
			buildingList.add(building);
		}
		String acadyear=se.getAcadyear();
		String semesterStr=se.getSemester()+"";
		if(StringUtils.isBlank(attDto.getBuildingId())){
			if(CollectionUtils.isNotEmpty(buildingList)){
				attDto.setBuildingId(buildingList.get(0).getBuildingId());
			}
		}
		
		List<Grade> gradeList =getGradeListByBid(attDto.getBuildingId(), unitId, acadyear, semesterStr);
		List<EccAttenceDormPeriod> dormPdList=getPeriodListByGrade(gradeList, unitId, null, attDto.getGradeCode(), acadyear, semesterStr,attDto.getSearchDate());
		if(StringUtils.isBlank(attDto.getPeriodId()) && CollectionUtils.isNotEmpty(dormPdList)){
			attDto.setPeriodId(dormPdList.get(0).getId());
		}
		//初始化并获取最终数据
		List<EccStudormAttence> attenceList=eccStudormAttenceService.findListByDto(unitId, attDto,
				acadyear,semesterStr,null);
		
		List<EccStudormAttence> attenceNewList = Lists.newArrayList();
		Pagination page=createPagination();
		
		if (CollectionUtils.isNotEmpty(attenceList)) {
			//排序
			Collections.sort(attenceList,new Comparator<EccStudormAttence>() {
				Collator collator = Collator.getInstance(Locale.CHINA);
				
				@Override
				public int compare(EccStudormAttence o1, EccStudormAttence o2) {
					
					if (o1.getSection() == o2.getSection()) {
						if (o1.getAcadyear().equals(o2.getAcadyear())) {
							if (o1.getClassCode().equals(o2.getClassCode())) {
								if (o1.getRoomName().equals(o2.getRoomName())) {
									if (o1.getStudentName().equals(o2.getStudentName())) {
										return 0;
									} else {
										return collator.getCollationKey(o1.getStudentName()).compareTo(collator.getCollationKey(o2.getStudentName()));
									}
								} else {
									return o1.getRoomName().compareTo(o2.getRoomName());
								}
							} else {
								return Integer.parseInt(o1.getClassCode()) - Integer.parseInt(o2.getClassCode());
							}
						} else {
							return o2.getAcadyear().compareTo(o1.getAcadyear());
						}
					} else {
						return o1.getSection() - o2.getSection();
					}
				}
				
			});
			
			//假分页
			page.setMaxRowCount(attenceList.size());
			Integer pageSize = page.getPageSize();
			Integer pageIndex = page.getPageIndex();
			for(int i=pageSize*(pageIndex-1);i<attenceList.size();i++){
				if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
					attenceNewList.add(attenceList.get(i));
				} else {
					break;
				}
			}
		}
		
		map.put("beginDate", se.getSemesterBegin());
		map.put("nowDate",new Date());
		map.put("attDto",attDto);
		map.put("buildingList", buildingList);
		map.put("dormPdList", dormPdList);
		map.put("gradeList", gradeList);
		
		map.put("attenceList",attenceNewList);
		sendPagination(request, map, page);
		return "/eclasscard/dorm/attanceList.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/dorm/attance/saveStatus")
	public String saveStatus(ModelMap map,String id,int status){
		try{
			eccStudormAttenceService.updateStatus(id, status);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	@RequestMapping("/dorm/attance/export")
	public void doExport(ModelMap map,AttanceSearchDto attDto){
		String unitId=getLoginInfo().getUnitId();
		JSONArray array=JSONArray.parseArray(stuworkRemoteService.getBuildingSbyUnitId(unitId));
		Map<String,String> buildingNameMap=new HashMap<String,String>();
		for(int i=0;i<array.size();i++){
			DormBuildingDto building=new DormBuildingDto();
			JSONObject json=array.getJSONObject(i);
			building.setBuildingId(json.getString("buildingId"));
			building.setBuildingName(json.getString("buildingName"));
			buildingNameMap.put(json.getString("buildingId"), json.getString("buildingName"));
		}
		String searchDate=DateUtils.date2String(attDto.getSearchDate(),"yyyy-MM-dd");
		String titleName=searchDate+"日"+buildingNameMap.get(attDto.getBuildingId());
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(se!=null){
			//初始化并获取最终数据
			List<EccStudormAttence> attenceList=eccStudormAttenceService.findListByDto(unitId, attDto,se.getAcadyear(),se.getSemester()+"",null);
			if (CollectionUtils.isNotEmpty(attenceList)) {
				//排序
				Collections.sort(attenceList,new Comparator<EccStudormAttence>() {
					Collator collator = Collator.getInstance(Locale.CHINA);
					@Override
					public int compare(EccStudormAttence o1, EccStudormAttence o2) {
						if (o1.getSection() == o2.getSection()) {
							if (o1.getAcadyear().equals(o2.getAcadyear())) {
								if (o1.getClassCode().equals(o2.getClassCode())) {
									if (o1.getRoomName().equals(o2.getRoomName())) {
										if (o1.getStudentName().equals(o2.getStudentName())) {
											return 0;
										} else {
											return collator.getCollationKey(o1.getStudentName()).compareTo(collator.getCollationKey(o2.getStudentName()));
										}
									} else {
										return o1.getRoomName().compareTo(o2.getRoomName());
									}
								} else {
									return Integer.parseInt(o1.getClassCode()) - Integer.parseInt(o2.getClassCode());
								}
							} else {
								return o2.getAcadyear().compareTo(o1.getAcadyear());
							}
						} else {
							return o1.getSection() - o2.getSection();
						}
					}
				});
			}
			doExportTea(attenceList, getResponse(),titleName);
		}
		doExportTea(null, getResponse(),titleName);
	}
	private void doExportTea(List<EccStudormAttence> attenceList,HttpServletResponse response,String titleName){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(CollectionUtils.isNotEmpty(attenceList)){
			for(EccStudormAttence attence : attenceList){
				Map<String,String> sMap = new HashMap<String,String>();
				sMap.put("刷卡时间", DateUtils.date2String(attence.getClockDate(),"yyyy-MM-dd HH:mm:ss"));
				sMap.put("学生姓名", attence.getStudentName());
				if (StringUtils.isNotBlank(attence.getContent())) {
					sMap.put("请假信息", attence.getContent().replace("<br/>", "，"));
				} else {
					sMap.put("请假信息", "");
				}
				sMap.put("寝室", attence.getRoomName());
				sMap.put("行政班", attence.getGradeName()+attence.getClassName());
				sMap.put("班主任", attence.getTeacherName());
				String state="";
				if(attence.getStatus()==1){
					state="未签到";
				}else if(attence.getStatus()==2){
					state="请假";
				}else{
					state="已签到";
				}
				sMap.put("状态", state);
				recordList.add(sMap);
			}
		}
		sheetName2RecordListMap.put("寝室考勤",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put("寝室考勤", tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile( titleName+"寝室考勤信息", titleMap, sheetName2RecordListMap, response);	
	}
	private List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("刷卡时间");
		tis.add("学生姓名");
		tis.add("请假信息");
		tis.add("寝室");
		tis.add("行政班");
		tis.add("班主任");
		tis.add("状态");
		return tis;
	}
	
	
	//*************分割线***************** 以下汇总
	
	@RequestMapping("/dorm/attance/statIndex/page")
	public String statIndex(ModelMap map){
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(se==null){
			return errorFtl(map, "当前时间不在学年学期范围内");
		}
		map.put("beginDate", se.getSemesterBegin());
		map.put("startTime", EccUtils.getSundayOfLastWeek());
		return "/eclasscard/dorm/attanceStatIndex.ftl";
	}
	@RequestMapping("/dorm/attance/statList/page")
	public String statList(ModelMap map,AttanceSearchDto attDto){
		String classId=attDto.getClassId();
		
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		List<EccStudormAttence> attenceList=eccStudormAttenceService.findStatByDto(getLoginInfo().getUnitId(), attDto, se.getAcadyear(), se.getSemester()+"");
		
		map.put("className",clazz==null?"":clazz.getClassNameDynamic());
		map.put("attDto", attDto);
		map.put("attenceList",attenceList);
		return "/eclasscard/dorm/attanceStatList.ftl";
	}
	@RequestMapping("/dorm/attance/statCheck")
	public String statList(ModelMap map,HttpServletRequest request){
		String studentId=request.getParameter("studentId");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		Pagination page=createPagination();
		
		List<EccStudormAttence> attenceList=eccStudormAttenceService.findCheckByCon(getLoginInfo().getUnitId(),
				studentId,startTime,endTime,page);
		
		Student stu=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		JSONObject jsonObject=JSONObject.parseObject(stuworkRemoteService.findbuildRoomByStudentId(studentId, getLoginInfo().getUnitId(), null,null));
		map.put("attenceList", attenceList);
		map.put("roomName", jsonObject.getString("roonName"));
		map.put("studentName", stu!=null?stu.getStudentName():"");
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		sendPagination(request, map, page);
		return "/eclasscard/dorm/attanceStatCheck.ftl";
	}
	//获取考勤时段
	public List<EccAttenceDormPeriod> getPeriodListByGrade(List<Grade> gradeList,String unitId,String buildingId,
			String gradeCode,String acadyear,String semesterStr,Date searchDate){
		Set<String> gradeCodes=new HashSet<String>();
		Set<String> gradeIds=new HashSet<String>();
		if(StringUtils.isNotBlank(gradeCode)){
			gradeCodes.add(gradeCode);
			List<Grade> glist=SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId, new String[]{gradeCode}),new TR<List<Grade>>(){});
			for(Grade g:glist){
				gradeIds.add(g.getId());
			}
		}else{
			for(Grade grade:gradeList){
				gradeCodes.add(grade.getGradeCode());
				gradeIds.add(grade.getId());
			}
		}
		Set<Integer> types=new HashSet<Integer>();
		Map<String, Integer> typeMap=eccDormAttenceService.findDormAttType(unitId, gradeIds.toArray(new String[0]), searchDate);
		Set<Entry<String, Integer>> entries=typeMap.entrySet();
		for(Entry<String, Integer> entry:entries){
			types.add(entry.getValue());
		}
		//通过type 以及 查询条件gradeCode  来获取list  得到periodIds 
		List<EccAttenceDormGrade>  dormGradeList=eccAttenceDormGradeService.findListByCon(unitId, types.toArray(new Integer[0]), gradeCodes.toArray(new String[0]));
		Set<String>  periodIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(dormGradeList)){
			for(EccAttenceDormGrade dormGrade:dormGradeList){
				periodIds.add(dormGrade.getPeriodId());
			}
		}
		return eccAttenceDormPeriodService.findByIdsOrderBy(periodIds.toArray(new String[0]));
	}
	//通过buildingId获取gradeList
	public List<Grade> getGradeListByBid(String buildingId,String unitId,String acadyear,String semesterStr){
		//通过buildingId获取所有的学生id
		final Set<String> studentIds=new HashSet<String>();
		List<String> studentIdsList=SUtils.dt(stuworkRemoteService.findStuIdsByBuiId(unitId, buildingId, acadyear, semesterStr),new TR<List<String>>(){});
		for(String studentId:studentIdsList){
			studentIds.add(studentId);
		}
		List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>(){});
		Set<String> classIds=new HashSet<String>();
		for(Student stu:studentList){
			classIds.add(stu.getClassId());
		}
		List<Clazz> clazzList=SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
		Set<String> gradeIds=new HashSet<String>();
		for(Clazz clazz:clazzList){
			gradeIds.add(clazz.getGradeId());
		}
		List<Grade> gradeList=SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>(){});
		Collections.sort(gradeList, new Comparator<Grade>() {
			@Override
			public int compare(Grade o1, Grade o2) {
				return o1.getGradeCode().compareTo(o2.getGradeCode());
			}});
		return gradeList;
	} 
}
