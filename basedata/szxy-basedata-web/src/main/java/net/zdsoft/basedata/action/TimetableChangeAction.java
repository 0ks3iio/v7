package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dto.TimetableChangeDto;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.TipsayExService;
import net.zdsoft.basedata.service.TipsayService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/basedata")
public class TimetableChangeAction extends BaseAction{
	
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private TipsayExService tipsayExService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private TipsayService tipsayService;
	
	@RequestMapping("/timetableChange/index/page")
	@ControllerInfo("调代管统计首页")
	public String showTimetableChangeIndex(ModelMap map) {
		return "/basedata/timetableChange/timetableChangeTab.ftl";
	}

	@RequestMapping("/timetableChange/monthCount/index/page")
	@ControllerInfo("代管月统计——tab")
	public String showMonthCountIndex(ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
		
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        
        String unitId = getLoginInfo().getUnitId();
        Semester semester = new Semester();
        semester = semesterService.findCurrentSemester(2,unitId);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        
        Calendar c = Calendar.getInstance();    
        c.set(Calendar.DAY_OF_MONTH,1);
		map.put("startDate", c.getTime());
		
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
        map.put("endDate", c.getTime());
        
		return "/basedata/timetableChange/monthCountHead.ftl";
	}
	
	@RequestMapping("/timetableChange/monthCount/list/page")
	@ControllerInfo("代管月统计——列表")
	public String showMonthCountList(ModelMap map, String acadyear, Integer semester, Date startDate, Date endDate, String teacherName) {
		String unitId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, semester);
		List<DateInfo> infoList = new ArrayList<DateInfo>();
		for(DateInfo item:dateInfoList){
    		if(DateUtils.compareForDay(item.getInfoDate(),startDate)>=0 && DateUtils.compareForDay(endDate, item.getInfoDate())>=0){
    			infoList.add(item);
    		}
    	}
		List<String> dateList=new ArrayList<String>();
    	Map<String,String> dateWeekMap = new HashMap<String, String>();
    	if(CollectionUtils.isNotEmpty(infoList)){
    		for(DateInfo dateInfo:infoList){
    			dateWeekMap.put(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1),DateUtils.date2String(dateInfo.getInfoDate(), "MM月dd日"));
    			dateList.add(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1));
    		}
    		List<TimetableChangeDto> dtoList = tipsayExService.findDtoListByMonthCondition(unitId,acadyear,semester,dateList.toArray(new String[dateList.size()]),teacherName);
    		map.put("dtoList", dtoList);
    	}
    		
		return "/basedata/timetableChange/monthCountList.ftl";
	}
	
	@RequestMapping("/timetableChange/dayCount/index/page")
	@ControllerInfo("调代管统计——tab")
	public String showDayCountIndex(ModelMap map) {
		List<String> acadyearList = semesterService.findAcadeyearList();
		
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        
        String unitId = getLoginInfo().getUnitId();
        Semester semester = new Semester();
        semester = semesterService.findCurrentSemester(2,unitId);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        
        Calendar c = Calendar.getInstance();    
        c.set(Calendar.DAY_OF_MONTH,1);
		map.put("startDate", c.getTime());
		
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
        map.put("endDate", c.getTime());
        
		return "/basedata/timetableChange/dayCountHead.ftl";
	}
	
	@RequestMapping("/timetableChange/dayCount/list/page")
	@ControllerInfo("调代管统计——列表")
	public String showDayCountList(ModelMap map, String acadyear, Integer semester, String type, Date startDate, Date endDate) {
		String unitId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, semester);
		List<DateInfo> infoList = new ArrayList<DateInfo>();
		for(DateInfo item:dateInfoList){
    		if(DateUtils.compareForDay(item.getInfoDate(),startDate)>=0 && DateUtils.compareForDay(endDate, item.getInfoDate())>=0){
    			infoList.add(item);
    		}
    	}
		List<String> dateList=new ArrayList<String>();
    	Map<String,String> dateWeekMap = new HashMap<String, String>();
    	map.put("type", type);
    	if(CollectionUtils.isNotEmpty(infoList)){
    		for(DateInfo dateInfo:infoList){
    			dateWeekMap.put(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1),DateUtils.date2String(dateInfo.getInfoDate(), "MM月dd日"));
    			dateList.add(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1));
    		}
    		List<TimetableChangeDto> dtoList = tipsayExService.findDtoListByDayCondition(unitId,acadyear,semester,dateList.toArray(new String[dateList.size()]),type,dateWeekMap);
    		map.put("dtoList", dtoList);
    	}
		return "/basedata/timetableChange/dayCountList.ftl";
	}
	
	@RequestMapping("/tipsay/applyArrange/index/page")
	@ControllerInfo("代课——申请教务安排")
	public String showApplyArrangeIndex(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        Semester semester = new Semester();
        semester = semesterService.findCurrentSemester(2,unitId);
        if(semester==null){
        	return errorFtl(map, "学年学期不存在！");
        }
        List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, semester.getAcadyear(), semester.getSemester());
        Calendar c = Calendar.getInstance();
        Set<Integer> weekSet = dateInfoList.parallelStream().filter(e->DateUtils.compareForDay(e.getInfoDate(),c.getTime())>0).map(e->e.getWeek()).filter(Objects::nonNull).collect(Collectors.toSet());
        List<Integer> weekList = new ArrayList<Integer>();
        weekList.addAll(weekSet);
        Collections.sort(weekList);
        map.put("acadyear", semester.getAcadyear());
        map.put("semester", semester.getSemester());
        map.put("weekList", weekList);
		map.put("startDate", c.getTime());
		map.put("endDate", semester.getSemesterEnd());
		
		return "/basedata/tipsay/applyArrangeIndex.ftl";
	}
	
	@RequestMapping("/tipsay/applyArrange/getScheduleList")
	@ResponseBody
	@ControllerInfo("查询课表")
	public String getScheduleList(ModelMap map, String acadyear, Integer semester, String searchType, Integer week, Date startDate, Date endDate) {
        String teacherId = getLoginInfo().getOwnerId();
        List<CourseSchedule> courseScheduleList;
        if("1".equals(searchType)){
        	courseScheduleList = courseScheduleService.findCourseScheduleListByPerId(acadyear, semester, week, teacherId, "1");
        }else{
        	courseScheduleList = courseScheduleService.findCourseScheduleListByPerId(acadyear, semester, DateUtils.date2String(startDate,"yyyyMMdd"), DateUtils.date2String(endDate,"yyyyMMdd"), teacherId, "1");
        }
        JSONObject json = new JSONObject();
        if(CollectionUtils.isNotEmpty(courseScheduleList)){
        	List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(getLoginInfo().getUnitId(), acadyear, semester);
        	Map<String,String> dateWeekMap = new HashMap<String, String>();
        	Calendar c = Calendar.getInstance();
        	for (DateInfo dateInfo : dateInfoList) {
        		if(DateUtils.compareForDay(dateInfo.getInfoDate(),c.getTime())<0){
        			continue;
        		}
        		dateWeekMap.put(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1),DateUtils.date2String(dateInfo.getInfoDate(), "MM月dd日"));
        	}
        	Collections.sort(courseScheduleList, new Comparator<CourseSchedule>() {
        		
        		@Override
        		public int compare(CourseSchedule o1, CourseSchedule o2) {
        			if(o1.getWeekOfWorktime()==o2.getWeekOfWorktime()){
        				if(o1.getDayOfWeek()==o2.getDayOfWeek()){
        					if(o1.getPeriodInterval().equals(o2.getPeriodInterval())){
        						return o1.getPeriod()-o2.getPeriod();
        					}
        					return o1.getPeriodInterval().compareTo(o2.getPeriodInterval());
        				}
        				return o1.getDayOfWeek()-o2.getDayOfWeek();
        			}
        			return o1.getWeekOfWorktime()-o2.getWeekOfWorktime();
        		}
        		
        	});
        	JSONArray jsonArr = new JSONArray();
        	JSONObject tempJson = null; 
        	for (CourseSchedule cs : courseScheduleList) {
        		if(!dateWeekMap.containsKey(cs.getWeekOfWorktime()+"_"+cs.getDayOfWeek())){
        			continue;
        		}
        		tempJson = new JSONObject();
    			tempJson.put("id", cs.getId());
    			tempJson.put("date", dateWeekMap.get(cs.getWeekOfWorktime()+"_"+cs.getDayOfWeek())+BaseConstants.dayOfWeekMap2.get(cs.getDayOfWeek().toString()));
    			tempJson.put("period", BaseConstants.PERIOD_INTERVAL_Map.get(cs.getPeriodInterval())+"第"+cs.getPeriod()+"节");
    			tempJson.put("className", cs.getClassName());
    			tempJson.put("subjectName", cs.getSubjectName());
    			jsonArr.add(tempJson);
			}
        	json.put("jsonArr", jsonArr);
        }
		
		return json.toJSONString();
	}
	
	@RequestMapping("/tipsay/applyArrange/doSave")
	@ResponseBody
	@ControllerInfo("保存教务安排申请")
	public String saveApply(ModelMap map, String type, String courseScheduleIds, String remark) {
		try {
			LoginInfo loginInfo = getLoginInfo();
			String teacherId = loginInfo.getOwnerId();
			String unitId=loginInfo.getUnitId();
			String[] ids = courseScheduleIds.split(",");
			List<CourseSchedule> courseScheduleList = courseScheduleService.findListByIds(ids);
			List<Tipsay> tipsayList = new ArrayList<Tipsay>();
//			List<TipsayEx> tipsayExList = new ArrayList<TipsayEx>();
			Tipsay tipsay = null;
//			TipsayEx tipsayEx = null;
			for (CourseSchedule cs : courseScheduleList) {
				tipsay = new Tipsay();
				tipsay.setId(UuidUtils.generateUuid());
				tipsay.setSchoolId(cs.getSchoolId());
				tipsay.setAcadyear(cs.getAcadyear());
				tipsay.setSemester(cs.getSemester());
				tipsay.setCourseScheduleId(cs.getId());
				tipsay.setWeekOfWorktime(cs.getWeekOfWorktime());
				tipsay.setDayOfWeek(cs.getDayOfWeek());
				tipsay.setPeriodInterval(cs.getPeriodInterval());
				tipsay.setPeriod(cs.getPeriod());
				tipsay.setClassId(cs.getClassId());
				tipsay.setClassType(cs.getClassType());
				tipsay.setSubjectId(cs.getSubjectId());
				tipsay.setTeacherId(teacherId);
				tipsay.setNewTeacherId(BaseConstants.ZERO_GUID);
				tipsay.setCreationTime(new Date());
				tipsay.setModifyTime(new Date());
				tipsay.setIsDeleted(Constant.IS_DELETED_FALSE);
				tipsay.setRemark(remark);
				tipsay.setState(TipsayConstants.TIPSAY_STATE_0);
				tipsay.setOperator(teacherId);
				tipsay.setTipsayType(TipsayConstants.TIPSAY_TYPE_03);
				tipsay.setType(type);
				tipsayList.add(tipsay);
//				tipsayEx = new TipsayEx();
//				tipsayEx.setId(UuidUtils.generateUuid());
//				tipsayEx.setSchoolId(cs.getSchoolId());
//				tipsayEx.setTipsayId(tipsay.getId());
//				tipsayEx.setAuditorId(BaseConstants.ZERO_GUID);
//				tipsayEx.setSourceType(TipsayConstants.TYPE_01);
//				tipsayEx.setState(TipsayConstants.TIPSAY_STATE_0);
//				tipsayEx.setCreationTime(new Date());
//				tipsayExList.add(tipsayEx);
			}
			tipsayService.saveApplySelfList(unitId,teacherId,tipsayList);
			
//			tipsayExService.saveAll(tipsayExList.toArray(new TipsayEx[tipsayExList.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		
		return returnSuccess();
	}
	
	@RequestMapping("/timetableChange/monthCount/export")
	@ResponseBody
	@ControllerInfo("代管月统计导出Excel")
	public String exportMonthCount(String acadyear, Integer semester, Date startDate, Date endDate, String teacherName, HttpServletResponse resp){
		String unitId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, semester);
		List<DateInfo> infoList = new ArrayList<DateInfo>();
		for(DateInfo item:dateInfoList){
    		if(DateUtils.compareForDay(item.getInfoDate(),startDate)>=0 && DateUtils.compareForDay(endDate, item.getInfoDate())>=0){
    			infoList.add(item);
    		}
    	}
		List<String> dateList=new ArrayList<String>();
    	Map<String,String> dateWeekMap = new HashMap<String, String>();
    	List<List<String>> datas = new ArrayList<List<String>>();
    	if(CollectionUtils.isNotEmpty(infoList)){
    		for(DateInfo dateInfo:infoList){
    			dateWeekMap.put(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1),DateUtils.date2String(dateInfo.getInfoDate(), "MM月dd日"));
    			dateList.add(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1));
    		}
    		List<TimetableChangeDto> dtoList = tipsayExService.findDtoListByMonthCondition(unitId,acadyear,semester,dateList.toArray(new String[dateList.size()]),teacherName);
    		int i = 0;
        	for (TimetableChangeDto dto : dtoList) {
        		i++;
        		List<String> inList = new ArrayList<String>();
        		inList.add(i+"");
        		inList.add(dto.getDeptName());
        		inList.add(dto.getTeacherName());
        		inList.add(dto.getTakeNum()+"");
        		inList.add(dto.getManNum()+"");
        		inList.add(dto.getBeTakeNum()+"");
        		inList.add(dto.getBeManNum()+"");
        		datas.add(inList);
        	}
    	}
    	
    	//表头
        List<String> titleList = new ArrayList<String>();
        titleList.add("序号");
        titleList.add("部门");
        titleList.add("姓名");
        titleList.add("代课数");
        titleList.add("管课数");
        titleList.add("被代课数");
        titleList.add("被管课数");

    	//导出
		String fileName=DateUtils.date2StringByDay(startDate)+"至"+DateUtils.date2StringByDay(endDate);
		String titleName = "代管月统计表";
		fileName += titleName;
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
	    //标题行固定
	    sheet.createFreezePane(0, 1);
	    int rownum=0;
		HSSFRow rowTitle = sheet.createRow(rownum++);
        for(int j=0;j<size;j++){
        	sheet.setColumnWidth(j, 10 * 256);
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        if(CollectionUtils.isNotEmpty(datas)){
        	for (List<String> data : datas) {
        		HSSFRow inRow = sheet.createRow(rownum++);
        		for (int j = 0; j < data.size(); j++) {
        			HSSFCell inCell = inRow.createCell(j);
        			if(j!=1&&j!=2){
        				inCell.setCellValue(Integer.parseInt(data.get(j)));
        			}else{
        				inCell.setCellValue(data.get(j));
        			}
				}
        	}
        }
		ExportUtils.outputData(workbook, fileName, resp);
		return returnSuccess();
	}
	
	@RequestMapping("/timetableChange/dayCount/export")
	@ResponseBody
	@ControllerInfo("调代管统计导出Excel")
	public String exportDayCount(String acadyear, Integer semester, String type, Date startDate, Date endDate, HttpServletResponse resp){
		String unitId = getLoginInfo().getUnitId();
		List<DateInfo> dateInfoList = dateInfoService.findByAcadyearAndSemester(unitId, acadyear, semester);
		List<DateInfo> infoList = new ArrayList<DateInfo>();
		for(DateInfo item:dateInfoList){
    		if(DateUtils.compareForDay(item.getInfoDate(),startDate)>=0 && DateUtils.compareForDay(endDate, item.getInfoDate())>=0){
    			infoList.add(item);
    		}
    	}
		List<String> dateList=new ArrayList<String>();
    	Map<String,String> dateWeekMap = new HashMap<String, String>();
    	List<List<String>> datas = new ArrayList<List<String>>();
    	if(CollectionUtils.isNotEmpty(infoList)){
    		for(DateInfo dateInfo:infoList){
    			dateWeekMap.put(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1),DateUtils.date2String(dateInfo.getInfoDate(), "MM月dd日"));
    			dateList.add(dateInfo.getWeek()+"_"+(dateInfo.getWeekday()-1));
    		}
    		List<TimetableChangeDto> dtoList = tipsayExService.findDtoListByDayCondition(unitId,acadyear,semester,dateList.toArray(new String[dateList.size()]),type,dateWeekMap);
    		int i = 0;
    		for (TimetableChangeDto dto : dtoList) {
    			i++;
    			List<String> inList = new ArrayList<String>();
        		inList.add(i+"");
        		inList.add(dto.getSearchDate());
        		inList.add(dto.getClassName());
        		inList.add(dto.getTeacherName());
    			if("1".equals(type)||"2".equals(type)||"3".equals(type)){
    				inList.add(dto.getChangeStr());
	    		}else{
	    			inList.add(dto.getChangeStr());
	    			inList.add(dto.getType());
	    		}
    			inList.add(dto.getRemark());
    			datas.add(inList);
    		}
    	}
    	
    	//表头
        List<String> titleList = new ArrayList<String>();
        titleList.add("序号");
        titleList.add("日期时间");
        titleList.add("班级");
        titleList.add("现上课老师");
        if("1".equals(type)){
        	titleList.add("代课信息");
		}else if("2".equals(type)){
			titleList.add("管课信息");
		}else if("3".equals(type)){
			titleList.add("调课信息");
		}else{
			titleList.add("调/代/管信息");
			titleList.add("类型");
		}
        titleList.add("备注");

    	//导出
		String fileName=DateUtils.date2StringByDay(startDate)+"至"+DateUtils.date2StringByDay(endDate);
		String titleName = "";
		if("1".equals(type)){
			titleName = "代课统计表";
		}else if("2".equals(type)){
			titleName = "管课统计表";
		}else if("3".equals(type)){
			titleName = "调课统计表";
		}else{
			titleName = "调代管统计表";
		}
		fileName += titleName;
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
	    //标题行固定
	    sheet.createFreezePane(0, 1);
	    int rownum=0;
		HSSFRow rowTitle = sheet.createRow(rownum++);
        for(int j=0;j<size;j++){
        	if(j==0){
        		sheet.setColumnWidth(j, 10 * 256);
        	}else if(j==4){
        		sheet.setColumnWidth(j, 30 * 256);
        	}else{
        		sheet.setColumnWidth(j, 20 * 256);
        	}
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        if(CollectionUtils.isNotEmpty(datas)){
        	for (List<String> data : datas) {
        		HSSFRow inRow = sheet.createRow(rownum++);
        		for (int j = 0; j < data.size(); j++) {
        			HSSFCell inCell = inRow.createCell(j);
        			if(j==0){
        				inCell.setCellValue(Integer.parseInt(data.get(j)));
        			}else{
        				inCell.setCellValue(data.get(j));
        			}
				}
        	}
        }
		ExportUtils.outputData(workbook, fileName, resp);		
		return returnSuccess();
	}
	
	@RequestMapping("/classswitch/export")
	@ResponseBody
	@ControllerInfo("导出调课单")
	public String exportClassSwich(String acadyear, Integer semester, String reason, String adjustedIds, HttpServletResponse resp){
		String unitId = getLoginInfo().getUnitId();
		String unitName = getLoginInfo().getUnitName();
		String[] adjustedIdArr = adjustedIds.split(",");
		List<TimetableChangeDto> dtoList = tipsayExService.findDtoListByExportCondition(unitId, acadyear, semester, adjustedIdArr);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		sheet.setColumnWidth(0, 95*256);//默认列宽
		sheet.setDefaultRowHeightInPoints(20);//默认行高
		
		HSSFFont titleFont = workbook.createFont();//标题字体
		titleFont.setFontName("黑体");
		titleFont.setFontHeightInPoints((short) 14);//字体大小
		titleFont.setBold(true);//加粗
		HSSFCellStyle titleStyle = workbook.createCellStyle();//标题样式
		titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
		titleStyle.setFont(titleFont);
		
		HSSFFont mainFont = workbook.createFont();//正文字体
		mainFont.setFontName("黑体");
		mainFont.setFontHeightInPoints((short) 10);//字体大小
		HSSFCellStyle mainStyle = workbook.createCellStyle();//正文样式
		mainStyle.setFont(mainFont);
		
		HSSFFont underLineFont = workbook.createFont();//下划线字体
		underLineFont.setFontName("黑体");//字体
		underLineFont.setFontHeightInPoints((short) 10);//字体大小
		underLineFont.setUnderline(FontFormatting.U_SINGLE);//单下划线
		
		HSSFCellStyle lastStyle = workbook.createCellStyle();//落款样式
		lastStyle.setAlignment(HorizontalAlignment.RIGHT);//水平居右
		lastStyle.setFont(mainFont);
		
		HSSFCellStyle divideStyle = workbook.createCellStyle();//分割线样式
		divideStyle.setBorderTop(BorderStyle.DASH_DOT);
		
		int rownum=0;
		HSSFRow row = null;
		HSSFCell cell = null;
		String textString = null;
		HSSFRichTextString richString = null;
		HSSFRichTextString reasonString = null;
		List<HSSFRichTextString> reasonStringList = new ArrayList<HSSFRichTextString>();
		List<Integer> rowBreakList = new ArrayList<Integer>();//当前设置下A4纸34行一页
		int realRowNum = 0;
		int rowBreakNum=34;
		double rowLength = 94d;//当前设置下A4纸一行94个字符
		if(StringUtils.isNotBlank(reason)){
			reason = "　　由于 "+reason+" 原因，你所任教班级的课程变动如下：";
			int realLength = StringUtils.getRealLength(reason);
			if(realLength<rowLength){
				reasonString = underLine(mainFont, underLineFont, reason, 4, reason.lastIndexOf("原因"));
				reasonStringList.add(reasonString);
			}else{
				int reasonRowNum = (int)Math.ceil(realLength/rowLength);
				int markRowNum = (int)Math.ceil(StringUtils.getRealLength(reason.substring(0,reason.lastIndexOf("原")+1))/rowLength);
				for (int i = 1; i <= reasonRowNum; i++) {
					textString = subTextString(reason, (int)rowLength);
					if(i==1){
						if(markRowNum==1){
							reasonString = underLine(mainFont, underLineFont, textString, 4, textString.lastIndexOf("原"));
						}else{
							reasonString = underLine(mainFont, underLineFont, textString, 4, textString.length());
						}
					}else if(i<markRowNum){
						reasonString = underLine(mainFont, underLineFont, textString, 0, textString.length());
					}else if(i==markRowNum){
						reasonString = underLine(mainFont, underLineFont, textString, 0, textString.lastIndexOf("原"));
					}else{
						reasonString = new HSSFRichTextString(reason);
					}
					reasonStringList.add(reasonString);
					reason = reason.replaceFirst(textString, "");
				}
			}
		}else{
			reasonString = new HSSFRichTextString("    你所任教班级的课程变动如下：");
			reasonStringList.add(reasonString);
		}
		String time = DateUtils.date2String(new Date(), "yyyy年MM月dd日");
//		time = leftPad(time, (int)rowLength);
//		String rightUnitName = leftPad(unitName, (int)rowLength);
		CellRangeAddress cra = null;
		for (TimetableChangeDto dto : dtoList) {
			row = sheet.createRow(rownum++);
			cell = row.createCell(0);
			if("1".equals(dto.getType())){
				cell.setCellValue(unitName+"临时调课通知单");
			}else{
				cell.setCellValue(unitName+"临时调课通知单(班主任)");
			}
			cell.setCellStyle(titleStyle);
			row = sheet.createRow(rownum++);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			cra = new CellRangeAddress(rownum-2,rownum-1,0,0);
			sheet.addMergedRegion(cra);
			
			row = sheet.createRow(rownum++);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			cell = row.createCell(0);
			textString = " "+dto.getTeacherName()+" 老师：";
			if("1".equals(dto.getType())){
				richString = underLine(mainFont, underLineFont, textString, 0, textString.lastIndexOf("老师"));
			}else{
				richString = underLine(mainFont, underLineFont, textString);
			}
			cell.setCellValue(richString);
			cell.setCellStyle(mainStyle);

			for (HSSFRichTextString hssfRichTextString : reasonStringList) {
				row = sheet.createRow(rownum++);
				row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
				cell = row.createCell(0);
				cell.setCellValue(hssfRichTextString);
				cell.setCellStyle(mainStyle);
			}
			
			dto.getAdjustedList().sort((x,y)->x.compareTo(y));
			if("1".equals(dto.getType())){
				for (int i = 0; i < dto.getAdjustedList().size(); i++) {
					String content = dto.getAdjustedList().get(i);
					row = sheet.createRow(rownum++);
					row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
					cell = row.createCell(0);
					textString = content.substring(0,content.indexOf("，")+1);
					richString = underLine(mainFont, underLineFont, textString, "11");
					cell.setCellValue(richString);
					cell.setCellStyle(mainStyle);
					
					row = sheet.createRow(rownum++);
					row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
					cell = row.createCell(0);
					if(i==dto.getAdjustedList().size()-1){
						textString = content.substring(content.indexOf("，")+1)+"。";
					}else{
						textString = content.substring(content.indexOf("，")+1)+"；";
					}
					richString = underLine(mainFont,underLineFont, textString, "12");
					cell.setCellValue(richString);
					cell.setCellStyle(mainStyle);
				}
			}else{
				for (int i = 0; i < dto.getAdjustedList().size(); i++) {
					row = sheet.createRow(rownum++);
					row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
					cell = row.createCell(0);
					if(i==dto.getAdjustedList().size()-1){
						textString = dto.getAdjustedList().get(i)+"。";
						richString = underLine(mainFont, underLineFont, textString, dto.getType());
					}else{
						textString = dto.getAdjustedList().get(i)+"；";
						richString = underLine(mainFont,underLineFont, textString, dto.getType());
					}
					cell.setCellValue(richString);
					cell.setCellStyle(mainStyle);
				}
			}
			
			row = sheet.createRow(rownum++);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			cell = row.createCell(0);
			cell.setCellValue(unitName);
			cell.setCellStyle(lastStyle);
			row = sheet.createRow(rownum++);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			cell = row.createCell(0);
			cell.setCellValue(time);
			cell.setCellStyle(lastStyle);
			row = sheet.createRow(rownum++);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			row = sheet.createRow(rownum++);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			cell = row.createCell(0);
			cell.setCellStyle(divideStyle);
			
			if(rownum-1-realRowNum>rowBreakNum){//本条超过一页
				if(realRowNum!=0){//不是第一次分页
					rowBreakList.add(realRowNum);
				}
				int breakNum =(int)Math.ceil((rownum-realRowNum)/(double)rowBreakNum);
				for (int i = 1; i < breakNum; i++) {
					rowBreakList.add(realRowNum+i*rowBreakNum);
				}
			}else{//不超过一页
				if(realRowNum<=rowBreakNum&&rownum>rowBreakNum){//第一次分页
					rowBreakList.add(realRowNum);
				}else if(CollectionUtils.isNotEmpty(rowBreakList)){
					int lastBreakRow = rowBreakList.get(rowBreakList.size()-1);
					if(realRowNum-lastBreakRow<=rowBreakNum&&rownum-1-lastBreakRow>rowBreakNum){
						rowBreakList.add(realRowNum);
					}
				}
			}
			realRowNum=rownum-1;
		}
		//打印设置
		for (Integer i : rowBreakList) {
			sheet.setRowBreak(i);
		}
		HSSFPrintSetup printSetup = sheet.getPrintSetup();//打印设置
		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //A4纸
		printSetup.setLandscape(false);//横向打印
		printSetup.setScale((short)100);//缩放比例
//		sheet.setAutobreaks(true);//
//		printSetup.setFitWidth((short)1);//页宽
//		printSetup.setFitHeight((short)0);//页高
		printSetup.setHeaderMargin((double) 0.5); // 页眉1.3
        printSetup.setFooterMargin((double) 0.5);//页脚
        sheet.setHorizontallyCenter(true);//设置打印页面为水平居中  
		sheet.setMargin(HSSFSheet.TopMargin,( double ) 1 );// 页边距（上）2.5
		sheet.setMargin(HSSFSheet.BottomMargin,( double ) 1 );// 页边距（下）
		sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.2 );// 页边距（左）0.75-1.9cm,0.2-0.5cm
		sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.2 );// 页边距（右）
		ExportUtils.outputData(workbook, "调课单", resp);
		return returnSuccess();
	}
	
	private HSSFRichTextString underLine (HSSFFont font, HSSFFont underfont, String text, int start, int end) {
		HSSFRichTextString richString = new HSSFRichTextString(text);
		richString.applyFont(font);
		richString.applyFont(start, end, underfont);//下划线的起始位置，结束位置
		return richString;
	}
	
	private HSSFRichTextString underLine (HSSFFont font, HSSFFont underfont, String text) {
		HSSFRichTextString richString = new HSSFRichTextString(text);
		richString.applyFont(font);
		richString.applyFont(0, text.indexOf("班主任"), underfont);//下划线的起始位置，结束位置
		richString.applyFont(text.indexOf("班主任")+3, text.lastIndexOf("老师"), underfont);//下划线的起始位置，结束位置
		return richString;
	}
	
	/**
	 * 添加下划线
	 * @param font 正文字体
	 * @param underfont 下划线字体
	 * @param text 内容
	 * @param type 类型
	 * @return
	 */
	private HSSFRichTextString underLine (HSSFFont font, HSSFFont underfont, String text, String type) {
		HSSFRichTextString richString = new HSSFRichTextString(text);
		richString.applyFont(font);
		if("11".equals(type)){//教师前半部分
			richString.applyFont(0, text.indexOf("月"), underfont);//几月
			richString.applyFont(text.indexOf("月")+1, text.indexOf("日"), underfont);//几日
			richString.applyFont(text.indexOf("星期")+2, text.indexOf("星期")+5, underfont);//星期几
		}else if("12".equals(type)){//教师后半部分
			richString.applyFont(0, text.indexOf("的"), underfont);//班级
			richString.applyFont(text.indexOf("的")+1, text.indexOf("的")+5, underfont);//上下午晚上
			richString.applyFont(text.indexOf("第")+1, text.indexOf("节"), underfont);//第几节
			richString.applyFont(text.indexOf("节")+1, text.lastIndexOf("课"), underfont);//什么课
			richString.applyFont(text.indexOf("调至")+2, text.lastIndexOf("月"), underfont);//几月
			richString.applyFont(text.lastIndexOf("月")+1, text.lastIndexOf("日"), underfont);//几日
			richString.applyFont(text.lastIndexOf("星期")+2, text.lastIndexOf("星期")+5, underfont);//星期几
			richString.applyFont(text.lastIndexOf(")")+1, text.lastIndexOf("第"), underfont);//上下午晚上
			richString.applyFont(text.lastIndexOf("第")+1, text.lastIndexOf("节"), underfont);//第几节
		}else{//班主任
			richString.applyFont(0, text.indexOf("月"), underfont);//几月
			richString.applyFont(text.indexOf("月")+1, text.indexOf("日"), underfont);//几日
			richString.applyFont(text.indexOf("星期")+2, text.indexOf("星期")+5, underfont);//星期几
			richString.applyFont(text.indexOf("第")+1, text.lastIndexOf("节"), underfont);//第几节
			richString.applyFont(text.lastIndexOf("节")+1, text.lastIndexOf("课改为"), underfont);//什么课
			richString.applyFont(text.lastIndexOf("课改为")+3, text.lastIndexOf("课"), underfont);//什么课
		}
		return richString;
	}
	
	//中英文混合截取字符串
    public String subTextString(String str,int len){
    	if(str.length()<len/2)return str;
    	int count = 0;
    	StringBuffer sb = new StringBuffer();
    	String[] ss = str.split("");
    	for(int i=0;i<ss.length;i++){
    		count+=ss[i].getBytes().length>1?2:1;
    		sb.append(ss[i]);
    		if(count>=len)break;
    	}
    	return sb.toString();
    }
    
    //自动补齐，未用到
    public String leftPad(String str,int len){
    	 int realLength = StringUtils.getRealLength(str);
         if(realLength<len){
         	str = StringUtils.leftPad(str, len-realLength, " ");
         }
         return str;
    }
}
