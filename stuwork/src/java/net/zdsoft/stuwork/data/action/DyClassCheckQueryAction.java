package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemService;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * 教室考核日查反馈表
 * 
 * @author weixh
 * 2017年11月28日	
 */
@Controller
@RequestMapping("/stuwork/dailyCheck")
public class DyClassCheckQueryAction extends BaseAction {
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private DyCourseRecordService dyCourseRecordService;
	@Autowired
	private DyWeekCheckResultService dyWeekCheckResultService;
	@Autowired
    private DyWeekCheckItemService dyWeekCheckItemService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	
	@RequestMapping("/index/page")
	public String index(ModelMap map) {
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
		if(null == sem){
			return errorFtl(map,"当前时间不在学年学期内，无法维护！");
		}
		map.put("minDate", sem.getSemesterBegin());
		map.put("maxDate", sem.getSemesterEnd());
		map.put("acadyear", sem.getAcadyear());
		map.put("semester", sem.getSemester());
		String secs = schoolRemoteService.findSectionsById(getLoginInfo().getUnitId());
		if(StringUtils.isEmpty(secs)) {
			return errorFtl(map, "没有取到有效的学校学段信息！");
		}
		String [] secArray = secs.split(",");
		Map<String, McodeDetail> mm = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"), new TR<Map<String, McodeDetail>>(){});
		List<String[]> sections = new ArrayList<>();
		if (MapUtils.isNotEmpty(mm)) {
			for (String sec : secArray) {
				McodeDetail md = mm.get(sec);
				if (md != null) {
					sections.add(new String[] { md.getThisId(), md.getMcodeContent() });
				}
			} 
		}
		if(CollectionUtils.isEmpty(sections)) {
			return errorFtl(map, "没有取到有效的学校学段信息！");
		}
		map.put("nowDate", new Date());
		map.put("sections", sections);
		return "/stuwork/dailyQuery/dailyQueryIndex.ftl";
	}
	
	@RequestMapping("/clsList/page")
	@ControllerInfo("班级列表及考核数据")
	public String clsList(HttpServletRequest request, ModelMap map) {
		String qd = request.getParameter("queryDate");
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String unitId = getLoginInfo().getUnitId();
		Date queryDate = DateUtils.string2Date(qd);
		DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, NumberUtils.toInt(semester), queryDate), DateInfo.class);
		if(dateInfo == null) {
			return errorFtl(map, "所选日期不在当前学年学期范围内！");
		}
		int section = NumberUtils.toInt(request.getParameter("section"));
		List<String[]> clsChecks = new ArrayList<>();
		List<Clazz> clsList = Clazz.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear));
		if (CollectionUtils.isNotEmpty(clsList)) {
			clsList = clsList.stream().filter(e->e.getSection()==section).collect(Collectors.toList());
		}
		if (CollectionUtils.isNotEmpty(clsList)) {
			int day = dateInfo.getWeekday();
			List<String> cids = EntityUtils.getList(clsList, Clazz::getId);
			List<DyWeekCheckResult> results = dyWeekCheckResultService.findByDateAndInClassId(unitId, acadyear, semester, queryDate, cids.toArray(new String[0]));
			//获得考核项信息
			List<DyWeekCheckItem> items = dyWeekCheckItemService.findBySchoolId(unitId);
			// 上课日志、晚自习日志 TODO
			List<DyCourseRecord> crs = dyCourseRecordService.findListByDateClsIds(unitId, queryDate, cids.toArray(new String[0]));
			boolean hasCourseRecord = CollectionUtils.isNotEmpty(crs);
			Map<String, DyCourseRecord> crMap = new HashMap<>();
			if (hasCourseRecord) {
				float sk = 3.0f;
				float wzx = 5.0f;
				for (DyCourseRecord re : crs) {
					if(StuworkConstants.COURSERECORD_SK_TYPE.equals(re.getType())) {
						re.setScore(sk - re.getScore());
					} else {
						re.setScore(wzx - re.getScore());
					}
					DyCourseRecord sum = crMap.get(re.getClassId() + re.getType());
					if (sum == null) {
						sum = re;
						crMap.put(re.getClassId() + re.getType(), sum);
					} else {
						sum.setScore(sum.getScore() + re.getScore());
						StringBuilder remark = new StringBuilder(StringUtils.trimToEmpty(sum.getRemark()));
						if (remark.length() > 0) {
							remark.append("，");
						}
						remark.append(StringUtils.trimToEmpty(re.getRemark()));
						sum.setRemark(remark.toString());
					}
				} 
			}
			List<Grade> grs = SUtils.dt(
					gradeRemoteService
							.findListByIds(EntityUtils.getList(clsList, Clazz::getGradeId).toArray(new String[0])),
					new TR<List<Grade>>() {
					});
			Map<String, String> gradeNames = EntityUtils.getMap(grs, Grade::getId, Grade::getGradeName);
			String[] types = {StuworkConstants.COURSERECORD_SK_TYPE, StuworkConstants.COURSERECORD_WZX_TYPE};
			for (Clazz cls : clsList) {
				StringBuilder remark = new StringBuilder();
				// 教室考核
				for(DyWeekCheckItem item : items){
					//先判断该天有没有该考核项，没有计0分
					if(!item.getDays().contains(day+"")){
						continue;
					}
					float dayScore = 0f;
					StringBuilder itemRemark = new StringBuilder();
					//判断：如果所有考核角色都没有提交 则该考核项计0分
					for(DyWeekCheckItemRole itemRole : item.getItemRoles()){
						Iterator<DyWeekCheckResult> it = results.iterator();
						for(;it.hasNext();){
							DyWeekCheckResult result = it.next();
							//如果有扣分则减去
							if(StringUtils.equals(item.getId(), result.getItemId()) 
									&& StringUtils.equals(result.getClassId(), cls.getId())
									&& StringUtils.equals(itemRole.getRoleType(), result.getRoleType())
									&& result.getDay() == day){
								dayScore += result.getScore();
								if (StringUtils.isNotEmpty(result.getRemark())) {
									if (itemRemark.length() > 0) {
										itemRemark.append("，");
									}
									itemRemark.append(StringUtils.trimToEmpty(result.getRemark()));
								}
								it.remove();
							}
						}
					}
					if (dayScore > 0 || itemRemark.length() > 0) {
						if (remark.length() > 0) {
							remark.append("；");
						} 
						remark.append(item.getItemName());
						remark.append("-"+dayScore);
						if (itemRemark.length() > 0) {
							remark.append("(备注：" + itemRemark.toString() + ")");
						}
					}
					
				}
				
				// 上课日志、晚自习日志
				if (hasCourseRecord) {
					for (String type : types) {
						DyCourseRecord sum = crMap.get(cls.getId() + type);
						if (sum != null) {
							if (remark.length() > 0) {
								remark.append("；");
							}
							remark.append(type.equals("1") ? "上课日志" : "晚自习日志");
							remark.append("-"+sum.getScore());
							if (StringUtils.isNotEmpty(sum.getRemark())) {
								remark.append("(备注：" + sum.getRemark() + ")");
							}
						}
					} 
				}
				clsChecks.add(new String[] {
						StringUtils.trimToEmpty(gradeNames.get(cls.getGradeId())) + cls.getClassName(), remark.toString() });
			} 
			
		}
		map.put("clsChecks", clsChecks);
		return "/stuwork/dailyQuery/clsList.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/clsList/export")
	@ControllerInfo("班级列表及考核数据")
	public void export(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// TODO
		clsList(request, map);
		String qd = request.getParameter("queryDate");
		int section = NumberUtils.toInt(request.getParameter("section"));
		McodeDetail md = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-RKXD", section + ""), McodeDetail.class);
		String[] dates = qd.split("-");
		String name = dates[1]+"月"+dates[2]+"日"+md.getMcodeContent()+"学段教室日志反馈表";
		List<String> titles = new ArrayList<>();
		titles.add("班级");
		titles.add("扣分情况");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
		List<String[]> clsChecks = (List<String[]>) map.get("clsChecks");
		List<Map<String, String>> values = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(clsChecks)) {
			for (String[] vals : clsChecks) {
				Map<String, String> valMap = new HashMap<>();
				valMap.put(titles.get(0), vals[0]);
				valMap.put(titles.get(1), vals[1]);
				values.add(valMap);
			} 
		}
		sheetName2RecordListMap.put(name, values);
		Map<String, List<String>> fieldTitleMap = new HashMap<>();
		fieldTitleMap.put(name, titles);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(name+".xls", fieldTitleMap, sheetName2RecordListMap, response);
	}

}
