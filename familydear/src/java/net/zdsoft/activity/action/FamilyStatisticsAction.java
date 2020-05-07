package net.zdsoft.activity.action;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.activity.dto.FamilyActureDto;
import net.zdsoft.activity.dto.FamilyMonthDto;
import net.zdsoft.activity.dto.FamilyStaDto;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.activity.service.FamilyDearRegisterService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.familydear.entity.FamdearMonth;
import net.zdsoft.familydear.service.FamdearActualReportService;
import net.zdsoft.familydear.service.FamdearMonthService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/familydear")
public class FamilyStatisticsAction extends BaseAction{
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private FamdearActualReportService famdearActualReportService;
	@Autowired
	private FamilyDearRegisterService familyDearRegisterService;
	@Autowired
	private FamdearMonthService famdearMonthService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@RequestMapping("/statistics/index/page")
	public String pageIndex(ModelMap map){
		return "/familydear/statistics/statisticsTab.ftl";
	}
	
	@RequestMapping("/statistics/beginRegister")
	public String beginRegister(HttpServletRequest req, String index, ModelMap map){
		Date startTime = DateUtils.string2Date(req.getParameter("startTime"),"yyyy-MM-dd"); 
		Date endTime =DateUtils.string2Date(req.getParameter("endTime"),"yyyy-MM-dd");
		String deptId=req.getParameter("deptId");
		Calendar c = Calendar.getInstance();
		if(endTime==null){
			endTime = c.getTime();
		}
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		if(startTime==null){
			startTime = c.getTime();
		}
		List<Dept> depts = SUtils.dt(deptRemoteService.findByUnitId(getLoginInfo().getUnitId()),new TR<List<Dept>>() {});
		map.put("depts", depts);
		map.put("deptId", deptId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("index", index);
		String modelUrl="";
		if(index.equals("1")){
			modelUrl="/familydear/statistics/statisticsBeginList.ftl";
			FamilyStaDto familyStaDto=new FamilyStaDto();
			List<FamdearActualReport> famdearActualReports=famdearActualReportService.getListByTime(getLoginInfo().getUnitId(), startTime, endTime);
			if(CollectionUtils.isNotEmpty(famdearActualReports)){
//				Set<String> activityIds=EntityUtils.getSet(famdearActualReports, e->e.getActivityId());
				Set<String> desUser=new HashSet<>();
				if(StringUtils.isNotBlank(deptId)){
					List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
					Set<String> teacherIds = EntityUtils.getSet(teachers, e->e.getId());
					if(CollectionUtils.isNotEmpty(teacherIds)){
						List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[0])),User.class);
						desUser=EntityUtils.getSet(users, e->e.getId());
					}
				}
				
				int i=0;
				Set<String> userSet=new HashSet<>();
				for (FamdearActualReport famdearActualReport : famdearActualReports) {
					if(famdearActualReport.getWalkPeople()!=null){
						if(StringUtils.isNotBlank(deptId)){
							if(CollectionUtils.isNotEmpty(desUser)&&desUser.contains(famdearActualReport.getCreateUserId())){
								userSet.add(famdearActualReport.getCreateUserId());
								i+=famdearActualReport.getWalkPeople();
							}
						}else{
							userSet.add(famdearActualReport.getCreateUserId());
							i+=famdearActualReport.getWalkPeople();
						}
					}
				}
				familyStaDto.setMeetingCount(i);
				familyStaDto=familyDearRegisterService.getListByUnitAndDeptId(familyStaDto,famdearActualReports,userSet.toArray(new String[0]));
			}
			familyStaDto.setTempDate(new Date());
			map.put("familyStaDto", familyStaDto);
		}else if(index.equals("2")){
			modelUrl="/familydear/statistics/statisticsTwoList.ftl";
			FamilyActureDto familyActureDto=new FamilyActureDto();
			List<FamdearActualReport> famdearActualReports=famdearActualReportService.getListByTime(getLoginInfo().getUnitId(), startTime, endTime);
			Set<String> desUser=new HashSet<>();
			if(StringUtils.isNotBlank(deptId)){
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
				Set<String> teacherIds = EntityUtils.getSet(teachers, e->e.getId());
				if(CollectionUtils.isNotEmpty(teacherIds)){
					List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[0])),User.class);
					desUser=EntityUtils.getSet(users, e->e.getId());
				}
			}
			if(CollectionUtils.isNotEmpty(famdearActualReports)){
				Set<String> activityIds=new HashSet<>();
				int donateMoneyCount=0;
				int donateObjectCount=0;//捐物
				int seekMedicalCount=0;//就医
				int seekStudyCount=0;//求学
				int seekEmployCount=0;//就业
				int developProductCount=0;//发展生产
				int otherThingCount=0;//其他
				int benefitPeopleCount=0;//惠及群众数
				int i=0;
				for (FamdearActualReport famdearActualReport : famdearActualReports) {
					if(StringUtils.isNotBlank(deptId)){
						if(CollectionUtils.isNotEmpty(desUser)&&desUser.contains(famdearActualReport.getCreateUserId())){
							i++;
							activityIds.add(famdearActualReport.getActivityId());
							if(famdearActualReport.getDonateMoney()!=null){
								donateMoneyCount+=famdearActualReport.getDonateMoney();
							}
							if(famdearActualReport.getDonateObjectnum()!=null){
								donateObjectCount+=famdearActualReport.getDonateObjectnum();
							}
							if(famdearActualReport.getSeekMedical()!=null){
								seekMedicalCount+=famdearActualReport.getSeekMedical();
							}
							if(famdearActualReport.getSeekStudy()!=null){
								seekStudyCount+=famdearActualReport.getSeekStudy();
							}
							if(famdearActualReport.getSeekEmploy()!=null){
								seekEmployCount+=famdearActualReport.getSeekEmploy();
							}
							if(famdearActualReport.getDevelopProduct()!=null){
								developProductCount+=famdearActualReport.getDevelopProduct();
							}
							if(famdearActualReport.getOtherThingsnum()!=null){
								otherThingCount+=famdearActualReport.getOtherThingsnum();
							}
							if(famdearActualReport.getBenefitPeople()!=null){
								benefitPeopleCount+=famdearActualReport.getBenefitPeople();
							}
						}
					}else{
						i++;
						activityIds.add(famdearActualReport.getActivityId());
						if(famdearActualReport.getDonateMoney()!=null){
							donateMoneyCount+=famdearActualReport.getDonateMoney();
						}
						if(famdearActualReport.getDonateObjectnum()!=null){
							donateObjectCount+=famdearActualReport.getDonateObjectnum();
						}
						if(famdearActualReport.getSeekMedical()!=null){
							seekMedicalCount+=famdearActualReport.getSeekMedical();
						}
						if(famdearActualReport.getSeekStudy()!=null){
							seekStudyCount+=famdearActualReport.getSeekStudy();
						}
						if(famdearActualReport.getSeekEmploy()!=null){
							seekEmployCount+=famdearActualReport.getSeekEmploy();
						}
						if(famdearActualReport.getDevelopProduct()!=null){
							developProductCount+=famdearActualReport.getDevelopProduct();
						}
						if(famdearActualReport.getOtherThingsnum()!=null){
							otherThingCount+=famdearActualReport.getOtherThingsnum();
						}
						if(famdearActualReport.getBenefitPeople()!=null){
							benefitPeopleCount+=famdearActualReport.getBenefitPeople();
						}
					}
				}
				familyActureDto.setBenefitPeopleCount(benefitPeopleCount);
				familyActureDto.setDevelopProductCount(developProductCount);
				familyActureDto.setDonateMoneyCount(donateMoneyCount);
				familyActureDto.setDonateObjectCount(donateObjectCount);
				familyActureDto.setOtherThingCount(otherThingCount);
				familyActureDto.setSeekEmployCount(seekEmployCount);
				familyActureDto.setSeekMedicalCount(seekMedicalCount);
				familyActureDto.setSeekStudyCount(seekStudyCount);
				familyActureDto.setTotalCount(seekMedicalCount+seekStudyCount+seekEmployCount+developProductCount+otherThingCount);
				familyActureDto.setGanbuCount(i);
				//familyActureDto=familyDearRegisterService.getListByUnitAndDeptId(familyActureDto, getLoginInfo().getUnitId(), deptId, activityIds.toArray(new String[0]));
			}
			familyActureDto.setTempDate(new Date());
			map.put("familyActureDto", familyActureDto);
		}else if(index.equals("3")){
			modelUrl="/familydear/statistics/statisticsThreeList.ftl";
			
			FamilyMonthDto familyMonthDto=new FamilyMonthDto();
			familyMonthDto.setTempDate(new Date());
			familyMonthDto=famdearMonthService.findListByTimeAndDeptId(familyMonthDto, getLoginInfo().getUnitId(), deptId, startTime, endTime);
			map.put("familyMonthDto", familyMonthDto);
		}
		
		return modelUrl;
	}

	@RequestMapping("/statistics/export1")
	public void doExport1(HttpServletRequest req, String index, ModelMap map){
		Date startTime = DateUtils.string2Date(req.getParameter("startTime"),"yyyy-MM-dd");
		Date endTime =DateUtils.string2Date(req.getParameter("endTime"),"yyyy-MM-dd");
		String deptId=req.getParameter("deptId");
		FamilyStaDto familyStaDto=new FamilyStaDto();
		List<FamdearActualReport> famdearActualReports=famdearActualReportService.getListByTime(getLoginInfo().getUnitId(), startTime, endTime);
		if(CollectionUtils.isNotEmpty(famdearActualReports)){
//				Set<String> activityIds=EntityUtils.getSet(famdearActualReports, e->e.getActivityId());
			Set<String> desUser=new HashSet<>();
			if(StringUtils.isNotBlank(deptId)){
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
				Set<String> teacherIds = EntityUtils.getSet(teachers, e->e.getId());
				if(CollectionUtils.isNotEmpty(teacherIds)){
					List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[0])),User.class);
					desUser=EntityUtils.getSet(users, e->e.getId());
				}
			}

			int i=0;
			Set<String> userSet=new HashSet<>();
			for (FamdearActualReport famdearActualReport : famdearActualReports) {
				if(famdearActualReport.getWalkPeople()!=null){
					if(StringUtils.isNotBlank(deptId)){
						if(CollectionUtils.isNotEmpty(desUser)&&desUser.contains(famdearActualReport.getCreateUserId())){
							userSet.add(famdearActualReport.getCreateUserId());
							i+=famdearActualReport.getWalkPeople();
						}
					}else{
						userSet.add(famdearActualReport.getCreateUserId());
						i+=famdearActualReport.getWalkPeople();
					}
				}
			}
			familyStaDto.setMeetingCount(i);
			familyStaDto=familyDearRegisterService.getListByUnitAndDeptId(familyStaDto,famdearActualReports,userSet.toArray(new String[0]));
		}
		familyStaDto.setTempDate(new Date());
		String titleName = "";
		if(StringUtils.isNotBlank(deptId)){
			Dept dept = SUtils.dt(deptRemoteService.findOneById(deptId),new TR<Dept>(){});
			if(dept!=null){
				titleName = titleName+dept.getDeptName();
			}
		}
		doExportList1(familyStaDto,getResponse(),titleName);
	}

	private void doExportList1(FamilyStaDto familyStaDto, HttpServletResponse response, String titleName){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(familyStaDto!=null){
			Map<String, String> sMap = new HashMap<String, String>();
			sMap.put("统计时间", DateUtils.date2String(familyStaDto.getTempDate(),"yyyy-MM-dd"));
			sMap.put("地厅级干部", familyStaDto.getTjCount()+"");
			sMap.put("县处级干部", familyStaDto.getXjCount()+"");
			sMap.put("乡科级干部", familyStaDto.getKjCount()+"");
			sMap.put("一般干部", familyStaDto.getNormalCount()+"");
			sMap.put("专业技术人员", familyStaDto.getJisCount()+"");
			sMap.put("工勤人员", familyStaDto.getGqCount()+"");
			sMap.put("企业人员", familyStaDto.getQyeCount()+"");
			sMap.put("其他", familyStaDto.getOtherCount()+"");
			sMap.put("小计", familyStaDto.getTotalCount()+"");
			sMap.put("走访见面各族群众户/次", familyStaDto.getMeetingCount()+"");
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put("报名审核",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList1();
		titleMap.put("报名审核", tis);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile( titleName+"走访见面统计信息导出", titleMap, sheetName2RecordListMap, response);
	}

	private List<String> getRowTitleList1() {
		List<String> tis = new ArrayList<String>();
		tis.add("统计时间");
		tis.add("地厅级干部");
		tis.add("县处级干部");
		tis.add("乡科级干部");
		tis.add("一般干部");
		tis.add("专业技术人员");
		tis.add("工勤人员");
		tis.add("企业人员");
		tis.add("其他");
		tis.add("小计");
		tis.add("走访见面各族群众户/次");
		return tis;
	}

	@RequestMapping("/statistics/export2")
	public void doExport2(HttpServletRequest req, String index, ModelMap map){
		Date startTime = DateUtils.string2Date(req.getParameter("startTime"),"yyyy-MM-dd");
		Date endTime =DateUtils.string2Date(req.getParameter("endTime"),"yyyy-MM-dd");
		String deptId=req.getParameter("deptId");
		FamilyActureDto familyActureDto=new FamilyActureDto();
		List<FamdearActualReport> famdearActualReports=famdearActualReportService.getListByTime(getLoginInfo().getUnitId(), startTime, endTime);
		Set<String> desUser=new HashSet<>();
		if(StringUtils.isNotBlank(deptId)){
			List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
			Set<String> teacherIds = EntityUtils.getSet(teachers, e->e.getId());
			if(CollectionUtils.isNotEmpty(teacherIds)){
				List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[0])),User.class);
				desUser=EntityUtils.getSet(users, e->e.getId());
			}
		}
		if(CollectionUtils.isNotEmpty(famdearActualReports)){
			Set<String> activityIds=new HashSet<>();
			int donateMoneyCount=0;
			int donateObjectCount=0;//捐物
			int seekMedicalCount=0;//就医
			int seekStudyCount=0;//求学
			int seekEmployCount=0;//就业
			int developProductCount=0;//发展生产
			int otherThingCount=0;//其他
			int benefitPeopleCount=0;//惠及群众数
			int i=0;
			for (FamdearActualReport famdearActualReport : famdearActualReports) {
				if(StringUtils.isNotBlank(deptId)){
					if(CollectionUtils.isNotEmpty(desUser)&&desUser.contains(famdearActualReport.getCreateUserId())){
						i++;
						activityIds.add(famdearActualReport.getActivityId());
						if(famdearActualReport.getDonateMoney()!=null){
							donateMoneyCount+=famdearActualReport.getDonateMoney();
						}
						if(famdearActualReport.getDonateObjectnum()!=null){
							donateObjectCount+=famdearActualReport.getDonateObjectnum();
						}
						if(famdearActualReport.getSeekMedical()!=null){
							seekMedicalCount+=famdearActualReport.getSeekMedical();
						}
						if(famdearActualReport.getSeekStudy()!=null){
							seekStudyCount+=famdearActualReport.getSeekStudy();
						}
						if(famdearActualReport.getSeekEmploy()!=null){
							seekEmployCount+=famdearActualReport.getSeekEmploy();
						}
						if(famdearActualReport.getDevelopProduct()!=null){
							developProductCount+=famdearActualReport.getDevelopProduct();
						}
						if(famdearActualReport.getOtherThingsnum()!=null){
							otherThingCount+=famdearActualReport.getOtherThingsnum();
						}
						if(famdearActualReport.getBenefitPeople()!=null){
							benefitPeopleCount+=famdearActualReport.getBenefitPeople();
						}
					}
				}else{
					i++;
					activityIds.add(famdearActualReport.getActivityId());
					if(famdearActualReport.getDonateMoney()!=null){
						donateMoneyCount+=famdearActualReport.getDonateMoney();
					}
					if(famdearActualReport.getDonateObjectnum()!=null){
						donateObjectCount+=famdearActualReport.getDonateObjectnum();
					}
					if(famdearActualReport.getSeekMedical()!=null){
						seekMedicalCount+=famdearActualReport.getSeekMedical();
					}
					if(famdearActualReport.getSeekStudy()!=null){
						seekStudyCount+=famdearActualReport.getSeekStudy();
					}
					if(famdearActualReport.getSeekEmploy()!=null){
						seekEmployCount+=famdearActualReport.getSeekEmploy();
					}
					if(famdearActualReport.getDevelopProduct()!=null){
						developProductCount+=famdearActualReport.getDevelopProduct();
					}
					if(famdearActualReport.getOtherThingsnum()!=null){
						otherThingCount+=famdearActualReport.getOtherThingsnum();
					}
					if(famdearActualReport.getBenefitPeople()!=null){
						benefitPeopleCount+=famdearActualReport.getBenefitPeople();
					}
				}
			}
			familyActureDto.setBenefitPeopleCount(benefitPeopleCount);
			familyActureDto.setDevelopProductCount(developProductCount);
			familyActureDto.setDonateMoneyCount(donateMoneyCount);
			familyActureDto.setDonateObjectCount(donateObjectCount);
			familyActureDto.setOtherThingCount(otherThingCount);
			familyActureDto.setSeekEmployCount(seekEmployCount);
			familyActureDto.setSeekMedicalCount(seekMedicalCount);
			familyActureDto.setSeekStudyCount(seekStudyCount);
			familyActureDto.setTotalCount(seekMedicalCount+seekStudyCount+seekEmployCount+developProductCount+otherThingCount);
			familyActureDto.setGanbuCount(i);
			//familyActureDto=familyDearRegisterService.getListByUnitAndDeptId(familyActureDto, getLoginInfo().getUnitId(), deptId, activityIds.toArray(new String[0]));
		}
		familyActureDto.setTempDate(new Date());
		String titleName = "";
		if(StringUtils.isNotBlank(deptId)){
			Dept dept = SUtils.dt(deptRemoteService.findOneById(deptId),new TR<Dept>(){});
			if(dept!=null){
				titleName = titleName+dept.getDeptName();
			}
		}
		doExportList2(familyActureDto,getResponse(),titleName);
	}

	private void doExportList2(FamilyActureDto familyActureDto, HttpServletResponse response, String titleName){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(familyActureDto!=null){
			Map<String, String> sMap = new HashMap<String, String>();
			sMap.put("统计时间", DateUtils.date2String(familyActureDto.getTempDate(),"yyyy-MM-dd"));
			sMap.put("捐款（元）", familyActureDto.getDonateMoneyCount()+"");
			sMap.put("捐物（件）", familyActureDto.getDonateObjectCount()+"");
			sMap.put("参就医（件）", familyActureDto.getSeekMedicalCount()+"");
			sMap.put("就学（件）", familyActureDto.getSeekStudyCount()+"");
			sMap.put("就业（件）", familyActureDto.getSeekEmployCount()+"");
			sMap.put("发展生产（件）", familyActureDto.getDevelopProductCount()+"");
			sMap.put("其他（件）", familyActureDto.getOtherThingCount()+"");
			sMap.put("合计", familyActureDto.getTotalCount()+"");
			sMap.put("参与干部职工人/次", familyActureDto.getGanbuCount()+"");
			sMap.put("惠及各族群众数（户）", familyActureDto.getBenefitPeopleCount()+"");
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put("报名审核",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList2();
		titleMap.put("报名审核", tis);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile( titleName+"办实事办好事统计信息导出", titleMap, sheetName2RecordListMap, response);
	}

	private List<String> getRowTitleList2() {
		List<String> tis = new ArrayList<String>();
		tis.add("统计时间");
		tis.add("捐款（元）");
		tis.add("捐物（件）");
		tis.add("参就医（件）");
		tis.add("就学（件）");
		tis.add("就业（件）");
		tis.add("发展生产（件）");
		tis.add("其他（件）");
		tis.add("合计");
		tis.add("参与干部职工人/次");
		tis.add("惠及各族群众数（户）");
		return tis;
	}

	@RequestMapping("/statistics/export3")
	public void doExport3(HttpServletRequest req, String index, ModelMap map){
		Date startTime = DateUtils.string2Date(req.getParameter("startTime"),"yyyy-MM-dd");
		Date endTime =DateUtils.string2Date(req.getParameter("endTime"),"yyyy-MM-dd");
		String deptId=req.getParameter("deptId");
		FamilyMonthDto familyMonthDto=new FamilyMonthDto();
		familyMonthDto.setTempDate(new Date());
		familyMonthDto=famdearMonthService.findListByTimeAndDeptId(familyMonthDto, getLoginInfo().getUnitId(), deptId, startTime, endTime);
		String titleName = "";
		if(StringUtils.isNotBlank(deptId)){
			Dept dept = SUtils.dt(deptRemoteService.findOneById(deptId),new TR<Dept>(){});
			if(dept!=null){
				titleName = titleName+dept.getDeptName();
			}
		}
		doExportList3(familyMonthDto,getResponse(),titleName);
	}

	private void doExportList3(FamilyMonthDto familyMonthDto, HttpServletResponse response, String titleName){
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		if(familyMonthDto!=null){
			Map<String, String> sMap = new HashMap<String, String>();
			sMap.put("统计时间", DateUtils.date2String(familyMonthDto.getTempDate(),"yyyy-MM-dd"));
			sMap.put("座谈报告会（场）", familyMonthDto.getReportMeeting()+"");
			sMap.put("联欢会（场）", familyMonthDto.getRelateMeeting()+"");
			sMap.put("文体活动（场）", familyMonthDto.getWjhd()+"");
			sMap.put("双语学习（次）", familyMonthDto.getDoubleLangue()+"");
			sMap.put("参观学习（次）", familyMonthDto.getVisityStudy()+"");
			sMap.put("党组织生活（次）", familyMonthDto.getDzzLife()+"");
			sMap.put("主题班会（场）", familyMonthDto.getTitleOrg()+"");
			sMap.put("主题团、队会（场）", familyMonthDto.getTitleStyle()+"");
			sMap.put("其他（场）", familyMonthDto.getOtherTime()+"");
			sMap.put("合计", familyMonthDto.getTotal()+"");
			sMap.put("实际参加人", familyMonthDto.getActureAmount()+"");
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put("报名审核",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList3();
		titleMap.put("报名审核", tis);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile( titleName+"每月活动填报信息导出", titleMap, sheetName2RecordListMap, response);
	}

	private List<String> getRowTitleList3() {
		List<String> tis = new ArrayList<String>();
		tis.add("统计时间");
		tis.add("座谈报告会（场）");
		tis.add("联欢会（场）");
		tis.add("文体活动（场）");
		tis.add("双语学习（次）");
		tis.add("参观学习（次）");
		tis.add("党组织生活（次）");
		tis.add("主题班会（场）");
		tis.add("主题团、队会（场）");
		tis.add("其他（场）");
		tis.add("合计");
		tis.add("实际参加人");
		return tis;
	}

}
