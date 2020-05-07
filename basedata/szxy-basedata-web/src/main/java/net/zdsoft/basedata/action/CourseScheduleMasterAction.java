package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolCalendarService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * 总课表信息，包含行政班和教学班数据
 * 
 * @author weixh
 * @since 2018年3月27日 上午11:11:22
 */
@RequestMapping("/basedata/mastercourseschedule")
@Controller
public class CourseScheduleMasterAction extends RoleCommonAction {
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ClassService classService;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
    private GradeService gradeService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
    private SchoolCalendarService schoolCalendarRemoteService;
	
	private final static String TYPE_TEA_1 = "1";
	private final static String TYPE_CLS_2 = "2";
	private final static String TYPE_PLACE_3 = "3";

	private final static String WEEK_ODD = "1";
	private final static String WEEK_EVEN = "2";
	
	@ControllerInfo("教师总课表首页")
	@RequestMapping("/teacher/index/page")
	public String teacherIndex(HttpServletRequest request, ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		if(!isAdmin(loginInfo.getUnitId(), loginInfo.getUserId())){
			return errorFtl(map, NO_ROLE_MSG);
		}
		map.put("type", TYPE_TEA_1);
		return "/basedata/courseSchedule/scheduleHead.ftl";
	}
	
	@ControllerInfo("班级总课表首页")
	@RequestMapping("/cls/index/page")
	public String clsIndex(HttpServletRequest request, ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		if(!isAdmin(loginInfo.getUnitId(), loginInfo.getUserId())){
			return errorFtl(map, NO_ROLE_MSG);
		}
		map.put("type", TYPE_CLS_2);
		return "/basedata/courseSchedule/scheduleHead.ftl";
	}
	
	@ControllerInfo("场地总课表首页")
	@RequestMapping("/place/index/page")
	public String placeIndex(HttpServletRequest request, ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
		if(!isAdmin(loginInfo.getUnitId(), loginInfo.getUserId())){
			return errorFtl(map, NO_ROLE_MSG);
		}
		map.put("type", TYPE_PLACE_3);
		return "/basedata/courseSchedule/scheduleHead.ftl";
	}
	
	@ControllerInfo("总课表首页")
	@RequestMapping("/index/page")
	public String index(HttpServletRequest request, ModelMap map) {
		String type = request.getParameter("type");
		String unitId = getLoginInfo().getUnitId();
		List<String> acadyears = semesterService.findAcadeyearList();
		if(CollectionUtils.isEmpty(acadyears)) {
			return errorFtl(map, "还没有维护过学年学期！");
		}
		if (TYPE_CLS_2.equals(type)) {
			List<Grade> gradeList = gradeService.findByUnitId(unitId);
			map.put("gradeList", gradeList);
		}
		String acadyear = request.getParameter("acadyear");
		int semester ;
		if (StringUtils.isEmpty(acadyear)) {
			Semester ns = semesterService.findCurrentSemester(0, unitId);
			if(ns != null) {
				acadyear = ns.getAcadyear();
				semester = ns.getSemester();
			} else {
				acadyear = acadyears.get(0);
				semester = 1;
			}
		} else {
			semester = NumberUtils.toInt(request.getParameter("semester"));
		}
		List<DateInfo> dates = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, semester);
		Set<Integer> weeks = EntityUtils.getSet(dates, "week");
		List<Integer> weekList = new ArrayList<Integer>(); 
		CollectionUtils.addAll(weekList, weeks.iterator());
		if(!weekList.isEmpty()) {
			Collections.sort(weekList);
		}
		//获取当前周
        Integer weekSearch = schoolCalendarRemoteService.findWeekByInfoDateAndSchoolId(DateUtils.getStartDate(new Date()),unitId);
        if(weekSearch==null){
        	weekSearch=1;
        }
        map.put("weekSearch", weekSearch);
		map.put("acadyears", acadyears);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("weeks", weekList);
		map.put("type", type);
		return "/basedata/courseSchedule/scheduleIndex.ftl";
	}
	
	@ControllerInfo("总课表数据")
	@RequestMapping("/list/page")
	public String scheduleList(CourseScheduleDto dto, HttpServletRequest request, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		String type = request.getParameter("type");
		if (StringUtils.isEmpty(dto.getSchoolId())) {
			dto.setSchoolId(unitId);
		}
		dealSchedules(dto, type, false, map);
		return "/basedata/courseSchedule/scheduleList.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@ControllerInfo("总课表数据导出")
	@RequestMapping("/download")
	public void downSchedues(CourseScheduleDto dto, String showTeacher, String showClass, String showPlace, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		String type = request.getParameter("type");
		if (StringUtils.isEmpty(dto.getSchoolId())) {
			dto.setSchoolId(unitId);
		}
		map.put("showTeacher", showTeacher);
		map.put("showClass", showClass);
		map.put("showPlace", showPlace);
		dealSchedules(dto, type, true, map);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setBold(true);
        style.setBorderBottom(BorderStyle.THIN);  
        style.setBorderLeft(BorderStyle.THIN);  
        style.setBorderRight(BorderStyle.THIN);  
        style.setBorderTop(BorderStyle.THIN);
        String typeName = "教师";
        if(TYPE_CLS_2.equals(type)) {
        	typeName = "班级";
        } else if(TYPE_PLACE_3.equals(type)) {
        	typeName = "场地";
        }
        int allCols = NumberUtils.toInt(""+map.get("allCols"));
        HSSFSheet sheet = workbook.createSheet(typeName + "总课表");

        // 行首固定
		sheet.createFreezePane(0, 3);
        
        // 有课表数据的行列需加宽加高
        Map<String, Integer> colrowMap = (Map<String, Integer>) map.get("colrowMap");
        Map<Integer, Integer> rowHeightMap = new HashMap<Integer, Integer>();
        sheet.setColumnWidth(0, 10 * 256);// 内容列宽度
		for (int i = 1; i < allCols; i++) {
			sheet.setColumnWidth(i, 3 * 256);// 内容列宽度
		}
        if(MapUtils.isNotEmpty(colrowMap)) {
        	Iterator<Entry<String, Integer>> it = colrowMap.entrySet().iterator();
        	while(it.hasNext()) {
        		Entry<String, Integer> en = it.next();
        		String key = en.getKey();
        		Integer val = en.getValue();
        		
        		if(key.startsWith("col")) {// 列有内容，列加宽
        			int col = NumberUtils.toInt(key.replaceAll("col", ""));
        		} else {
        			int row = NumberUtils.toInt(key.replaceAll(key, "row"));
        			rowHeightMap.put(row, val);
        		}
        	}
        }
        
        
        //sheet 合并单元格
        CellRangeAddress car = new CellRangeAddress(0,0,0,allCols-1);
        RegionUtil.setBorderBottom(BorderStyle.THIN, car, sheet); 
        RegionUtil.setBorderLeft(BorderStyle.THIN, car, sheet); 
    	RegionUtil.setBorderTop(BorderStyle.THIN, car, sheet); 
    	RegionUtil.setBorderRight(BorderStyle.THIN, car, sheet); 
        sheet.addMergedRegion(car);
        //标题行
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints((1.5f)*sheet.getDefaultRowHeightInPoints());
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        titleCell.setCellValue(new HSSFRichTextString(typeName + "总课表"));
        
        //周次节次
        HSSFRow wkRow = sheet.createRow(1);// 周次行
        /*CellRangeAddress car1 = new CellRangeAddress(1,2,0,0);
        RegionUtil.setBorderBottom(BorderStyle.THIN, car1, sheet); 
        RegionUtil.setBorderLeft(BorderStyle.THIN, car1, sheet); 
    	RegionUtil.setBorderTop(BorderStyle.THIN, car1, sheet); 
    	RegionUtil.setBorderRight(BorderStyle.THIN, car1, sheet); 
        sheet.addMergedRegion(car1);*/
        int totalPeriods = NumberUtils.toInt("" + map.get("totalPeriods"));
        HSSFCell typeCell = wkRow.createCell(0);
        typeCell.setCellStyle(style);
        typeCell.setCellValue(new HSSFRichTextString("星期"));
        style.setAlignment(HorizontalAlignment.CENTER);
        List<String> dayStrs = (List<String>) map.get("dayStrs");
        if(dayStrs == null) {
        	dayStrs = new ArrayList<String>();
        }
        int index = 0;
        for(int i=1;i<allCols;i++) {
        	int nowCol = totalPeriods*i+1;
        	/*CellRangeAddress car2 = new CellRangeAddress(1, 1, nowCol, nowCol+totalPeriods-1);
        	RegionUtil.setBorderBottom(BorderStyle.THIN, car2, sheet);
        	RegionUtil.setBorderLeft(BorderStyle.THIN, car2, sheet);
        	RegionUtil.setBorderTop(BorderStyle.THIN, car2, sheet);
        	RegionUtil.setBorderRight(BorderStyle.THIN, car2, sheet);
        	sheet.addMergedRegion(car2);*/
        	HSSFCell wkCell = wkRow.createCell(i);
        	wkCell.setCellStyle(style);
			if (i % totalPeriods == 1) {
				wkCell.setCellValue(new HSSFRichTextString(dayStrs.get(index++).substring(2)));
			} else {
				wkCell.setCellValue(new HSSFRichTextString(""));
			}
		}
        HSSFRow peRow = sheet.createRow(2);// 节次行
		HSSFCell classCell = peRow.createCell(0);
		classCell.setCellStyle(style);
		classCell.setCellValue(new HSSFRichTextString("班级"));
        int ds = dayStrs.size();
        for(int i=0;i<ds;i++) {
	        for(int j=1;j<=totalPeriods;j++) {
	    		HSSFCell peCell = peRow.createCell(totalPeriods*i+j);
	    		peCell.setCellStyle(style);
	    		peCell.setCellValue(j);
	    	}
        }
        
        // 课表内容
        HSSFCellStyle cstyle = workbook.createCellStyle();
        cstyle.setBorderBottom(BorderStyle.THIN);  
        cstyle.setBorderLeft(BorderStyle.THIN);  
        cstyle.setBorderRight(BorderStyle.THIN);  
        cstyle.setBorderTop(BorderStyle.THIN);
        cstyle.setWrapText(true);
        List<String[]> scheduleList = (List<String[]>) map.get("scheduleList");
        boolean hasRow = !rowHeightMap.isEmpty();
        if (CollectionUtils.isNotEmpty(scheduleList)) {
        	for (int i=0;i<scheduleList.size();i++) {
        		String[] sches = scheduleList.get(i);
        		HSSFRow scRow = sheet.createRow(3+i);// 课表内容行
				scRow.setHeight((short) 480);
        		/*if(hasRow && rowHeightMap.containsKey(i)) {
        			Integer height = rowHeightMap.get(i);
        			scRow.setHeightInPoints(height*sheet.getDefaultRowHeightInPoints());
        		}*/
        		for(int j=0;j<sches.length;j++) {
        			HSSFCell chCell = scRow.createCell(j);
        			chCell.setCellStyle(cstyle);
        			String sc = sches[j];
        			chCell.setCellValue(new HSSFRichTextString(sc));
        		}
			} 
		}
        
        McodeDetail md = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XQ", dto.getSemester()+""), McodeDetail.class);
        StringBuilder fileName = new StringBuilder(dto.getAcadyear());
        if(md != null) {
        	fileName.append(md.getMcodeContent());
        }
        fileName.append("第" + dto.getWeekOfWorktime() + "周" + typeName + "总课表");
		ExportUtils.outputData(workbook, fileName.toString(), response);
	}
	
	/**
	 * 处理组装总课表数据
	 * @param dto
	 * @param type 课表显示类型
	 */
	private ModelMap dealSchedules(CourseScheduleDto dto, String type, boolean isExport, ModelMap map) {
		map.put("type", type);
		Grade grades = gradeService.findTimetableMaxRangeBySchoolId(dto.getSchoolId(),null);
        Integer ms = grades.getMornPeriods();
        Integer ams = grades.getAmLessonCount();
        Integer pms = grades.getPmLessonCount();
        Integer nts = grades.getNightLessonCount();
        String showTeacher = map.get("showTeacher") == null ? "0" : (String)map.get("showTeacher");
		String showClass = map.get("showClass") == null ? "0" : (String)map.get("showClass");
		String showPlace = map.get("showPlace") == null ? "0" : (String)map.get("showPlace");
    
		int totalPeriods = ms+ams+pms+nts;
		if(totalPeriods == 0) {
			totalPeriods = 1;
		}
		int days = grades.getWeekDays();
		map.put("totalPeriods", totalPeriods);
		// 总列数
		int allCols = totalPeriods*days+1;
		map.put("allCols", allCols);
		List<String> dayStrs = new ArrayList<String>();
		for(int i=0;i<days;i++) {
			if(i > 6) {
				break;
			}
			dayStrs.add(BaseConstants.dayOfWeekMap.get(i+""));
		}
		map.put("dayStrs", dayStrs);
		List<CourseSchedule> scs = courseScheduleService.getByCourseScheduleDto(dto);
		if(CollectionUtils.isEmpty(scs)) {
			return map;
		}
		// key:教师id/班级id/场地id，value：对应的课表记录
		Map<String, List<CourseSchedule>> scMap = new HashMap<String, List<CourseSchedule>>();
		// 1教师 2班级 3场地
		Set<String> tids = new HashSet<String>();
		Set<String> pids = new HashSet<String>();
		Set<String> bcids = new HashSet<String>();
		Set<String> tcids = new HashSet<String>();
		Set<String> sids = new HashSet<String>();
		for(CourseSchedule sc : scs) {
			if(CourseSchedule.CLASS_TYPE_4 == sc.getClassType()) {
				continue;
			}
			String key ;
			if(TYPE_TEA_1.equals(type)) {
				key = sc.getTeacherId();
			} else if(TYPE_CLS_2.equals(type)) {
				key = sc.getClassId();
			} else {
				key = sc.getPlaceId();
			}
			if(StringUtils.isEmpty(key)) {
				continue;
			}
			List<CourseSchedule> cous = scMap.get(key);
			if(cous == null) {
				cous = new ArrayList<CourseSchedule>();
				scMap.put(key, cous);
			}
			cous.add(sc);
			
			if(StringUtils.isNotEmpty(sc.getSubjectId()) 
					&& !sids.contains(sc.getSubjectId())) {
				sids.add(sc.getSubjectId());
			}
			if(StringUtils.isNotEmpty(sc.getTeacherId()) 
					&& !tids.contains(sc.getTeacherId())) {
				tids.add(sc.getTeacherId());
			}
			if(StringUtils.isNotEmpty(sc.getPlaceId()) 
					&& !pids.contains(sc.getPlaceId())) {
				pids.add(sc.getPlaceId());
			}
			if (StringUtils.isNotEmpty(sc.getClassId())) {
				if (sc.getClassType() == CourseSchedule.CLASS_TYPE_NORMAL) {
					bcids.add(sc.getClassId());
				} else {
					tcids.add(sc.getClassId());
				} 
			}
		}
		// 显示信息名称
		Map<String, Teacher> tm;
		if (tids.size() > 0) {
			tm = teacherService.findMapByIdIn(tids.toArray(new String[0]));
		} else {
			tm = new HashMap<String, Teacher>();
		}
		Map<String, Course> cm;
		if (sids.size() > 0) {
			cm = courseService.findMapByIdIn(sids.toArray(new String[0]));
		} else {
			cm = new HashMap<String, Course>();
		}
		Map<String, Clazz> clsm = null;
		if (bcids.size() > 0) {
			List<Clazz> clzs = classService.findListByIdIn(bcids.toArray(new String[0]));
			if (CollectionUtils.isNotEmpty(clzs)) {
				Set<String> gids = EntityUtils.getSet(clzs, "gradeId");
				Map<String, Grade> findByIdInMap = gradeService.findMapByIdIn(gids.toArray(new String[0]));
		        for (Clazz item : clzs) {
		            Grade grade = findByIdInMap.get(item.getGradeId());
		            if (grade != null) {
		                item.setClassNameDynamic(grade.getGradeName() + item.getClassName());
		            }
		            else {
		                item.setClassNameDynamic(item.getClassName());
		            }
		        }
				clsm = EntityUtils.getMap(clzs, "id");
			}
		}
		if(clsm == null) {
			clsm = new HashMap<String, Clazz>();
		}
		Map<String, TeachClass> tcm;
		if (tcids.size() > 0) {
			tcm = teachClassService.findMapByIdIn(tcids.toArray(new String[0]));
		} else {
			tcm = new HashMap<String, TeachClass>();
		}
		Map<String, TeachPlace> pm;
		if (pids.size() > 0) {
			pm = teachPlaceService.findMapByIdIn(pids.toArray(new String[0]));
		} else {
			pm = new HashMap<String, TeachPlace>();
		}
		List<String[]> scheduleList = new ArrayList<String[]>();
		Iterator<Entry<String, List<CourseSchedule>>> scIt = scMap.entrySet().iterator();
		int[] pes = {ms,ams, pms, nts};
		String[] pis = {CourseSchedule.PERIOD_INTERVAL_1,CourseSchedule.PERIOD_INTERVAL_2, CourseSchedule.PERIOD_INTERVAL_3, CourseSchedule.PERIOD_INTERVAL_4};
		// 判断行列有没有数据
		Map<String, Integer> colrowMap = new HashMap<String, Integer>();
		int row=0;
		while(scIt.hasNext()) {
			int rowh = 0;
			Entry<String, List<CourseSchedule>> en = scIt.next();
			String key = en.getKey();
			List<CourseSchedule> csList = en.getValue();
			String keyName = null;
			if(TYPE_CLS_2.equals(type)) {
				if(!clsm.containsKey(key) && !tcm.containsKey(key)) {
					continue;
				}
				if(clsm.containsKey(key)) {
					if(clsm.get(key).getIsDeleted()==1){
						continue;
					}
					keyName = "1"+clsm.get(key).getClassNameDynamic();
				} else {
					if(tcm.get(key).getIsDeleted()==1){
						continue;
					}
					keyName = "2"+tcm.get(key).getName();
				}
			} else if(TYPE_TEA_1.equals(type)) {
				if(!tm.containsKey(key)) {
					continue;
				}
				keyName = tm.get(key).getTeacherName();
			} else {
				if(!pm.containsKey(key)) {
					continue;
				}
				keyName = pm.get(key).getPlaceName();
			}
			String[] cols = new String[allCols];
			int index = 0;
			cols[index++]=keyName;
			for(int i=0;i<days;i++) {
				for (int k=0;k<pes.length;k++) {
					int pe = pes[k];
					String pi = pis[k];// 上下午晚上
					for (int j = 1; j <= pe; j++) {
						Iterator<CourseSchedule> it = csList.iterator();
						StringBuilder sb = new StringBuilder();
						int rowh1 = 0;
						while (it.hasNext()) {
							CourseSchedule cs = it.next();
							if (!cm.containsKey(cs.getSubjectId())) {
								it.remove();
								continue;
							}
							if(StringUtils.isNotEmpty(cs.getClassId())){
								if(cs.getClassType() == CourseSchedule.CLASS_TYPE_NORMAL){
									if(!clsm.containsKey(cs.getClassId()) || clsm.get(cs.getClassId()).getIsDeleted() == 1){
										it.remove();
										continue;
									}
								} else if(!tcm.containsKey(cs.getClassId()) || tcm.get(cs.getClassId()).getIsDeleted() == 1){
									it.remove();
									continue;
								}
							}
							if (pi.equals(cs.getPeriodInterval())
									&& cs.getDayOfWeek() == i && cs.getPeriod() == j) {
								rowh1++;
								if (isExport) {
									sb = getExcelColDisplay(sb, type, cs, tm, cm, clsm, tcm, pm, showTeacher, showClass, showPlace);
								} else {
									sb = getColDisplay(sb, type, cs, tm, cm, clsm, tcm, pm);
								}
							}
						}
						if(rowh1>0) {
							colrowMap.put("col"+index, 1);
						}
						rowh = Math.max(rowh, rowh1);
						cols[index++] = sb.toString();
					} 
				}
			}
			scheduleList.add(cols);
			colrowMap.put("row"+row, rowh);
			row++;
		}
		if(scheduleList.size() > 0) {
			Collections.sort(scheduleList, new Comparator<String[]>() {

				public int compare(String[] o1, String[] o2) {
					return StringUtils.trimToEmpty(o1[0]).compareTo(StringUtils.trimToEmpty(o2[0]));
				}
			});
			if (TYPE_CLS_2.equals(type)) {
				for (String[] strs : scheduleList) {
					strs[0] = strs[0].substring(1);
				} 
			}
		}
		map.put("colrowMap", colrowMap);
		map.put("scheduleList", scheduleList);
		return map;
	}
	
	/**
	 * 获取每节课的课表显示信息
	 * @return 科目（班级，老师，场地）
	 */
	private StringBuilder getColDisplay(StringBuilder sb, String type, 
			CourseSchedule cs, Map<String, Teacher> tm, 
			Map<String, Course> cm, Map<String, Clazz> clsm, 
			Map<String, TeachClass> tcm, Map<String, TeachPlace> pm) {
		sb.append("<p style='white-space: nowrap;'>" + cm.get(cs.getSubjectId()).getSubjectName());
		// 科目（班级，老师，场地）
		boolean hasFirstData = false;
		boolean hasSecondData = false;
		if(!TYPE_CLS_2.equals(type)) {
			if(cs.getClassType() == CourseSchedule.CLASS_TYPE_NORMAL) {
				if(clsm.containsKey(cs.getClassId())) {
					sb.append("<sapn class='addition'>（<span class='addition_class'>&nbsp" + clsm.get(cs.getClassId()).getClassNameDynamic() + "</span>&nbsp");
					hasFirstData = true;
				}
			} else if(tcm.containsKey(cs.getClassId())) {
				sb.append("<sapn class='addition'>（<span class='addition_class'>&nbsp" + tcm.get(cs.getClassId()).getName() + "</span>&nbsp");
				hasFirstData = true;
			}
		}
		if(!TYPE_TEA_1.equals(type)) {
			if(tm.containsKey(cs.getTeacherId())) {
				if(hasFirstData) {
					sb.append("<span class='addition_teacher'>" + tm.get(cs.getTeacherId()).getTeacherName() + "&nbsp</span>）</span>");
					hasSecondData = true;
				} else {
					sb.append("<sapn class='addition'>（<span class='addition_teacher'>&nbsp" + tm.get(cs.getTeacherId()).getTeacherName() + "</span>&nbsp");
					hasFirstData = true;
				}
			}
		}
		if(!TYPE_PLACE_3.equals(type)) {
			if(pm.containsKey(cs.getPlaceId())) {
				if(hasFirstData) {
					sb.append("<span class='addition_place'>");
					hasSecondData = true;
				} else {
					sb.append("<sapn class='addition'>（<span class='addition_place'>&nbsp");
					hasFirstData = true;
				}
				sb.append(pm.get(cs.getPlaceId()).getPlaceName() + "&nbsp</span>）</span>");
			}
		}
		if (hasFirstData && !hasSecondData) {
			sb.append("）</span>");
		}
		sb.append("</p>");
		return sb;
	}

	private StringBuilder getExcelColDisplay(StringBuilder sb, String type,
										CourseSchedule cs, Map<String, Teacher> tm,
										Map<String, Course> cm, Map<String, Clazz> clsm,
										Map<String, TeachClass> tcm, Map<String, TeachPlace> pm,
										String showTeacher, String showClass, String showPlace) {
		sb.append(cm.get(cs.getSubjectId()).getSubjectName());
		// 科目（班级，老师，场地）
		boolean hasFirstData = false;
		boolean hasSecondData = false;
		if(!TYPE_CLS_2.equals(type) && BaseConstants.ONE_STR.equals(showClass)) {
			if(cs.getClassType() == CourseSchedule.CLASS_TYPE_NORMAL) {
				if(clsm.containsKey(cs.getClassId())) {
					sb.append("（" + clsm.get(cs.getClassId()).getClassNameDynamic());
					hasFirstData = true;
				}
			} else if(tcm.containsKey(cs.getClassId())) {
				sb.append("（" + tcm.get(cs.getClassId()).getName());
				hasFirstData = true;
			}
		}
		if(!TYPE_TEA_1.equals(type) && BaseConstants.ONE_STR.equals(showTeacher)) {
			if(tm.containsKey(cs.getTeacherId())) {
				if(hasFirstData) {
					sb.append("，" + tm.get(cs.getTeacherId()).getTeacherName() + "）");
					hasSecondData = true;
				} else {
					sb.append("（" + tm.get(cs.getTeacherId()).getTeacherName());
					hasFirstData = true;
				}
			}
		}
		if(!TYPE_PLACE_3.equals(type) && BaseConstants.ONE_STR.equals(showPlace)) {
			if(pm.containsKey(cs.getPlaceId())) {
				if(hasFirstData) {
					hasSecondData = true;
					sb.append("，");
					sb.append(pm.get(cs.getPlaceId()).getPlaceName() + "）");
				} else {
					hasFirstData = true;
					sb.append("（");
					sb.append(pm.get(cs.getPlaceId()).getPlaceName());
				}
			}
		}
		if (hasFirstData && !hasSecondData) {
			sb.append("）");
		}
		if (Integer.valueOf(WEEK_ODD).equals(cs.getWeekType())) {
			sb.append("[单]");
		}
		if (Integer.valueOf(WEEK_EVEN).equals(cs.getWeekType())) {
			sb.append("[双]");
		}
		return sb;
	}
	
}
