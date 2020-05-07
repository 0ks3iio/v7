package net.zdsoft.stuwork.data.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;
import net.zdsoft.stuwork.data.service.DyCourseRecordService;

/**
 * 上课日志、晚自习日志导入
 * @author weixh
 * 2017年11月24日	
 */
@Controller
@RequestMapping("/stuwork/courserecord/import")
public class DyCourseRecordImportAction extends DataImportAction {
	@Autowired
	private DyCourseRecordService dyCourseRecordService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	
	private static OpenApiNewElectiveService openApiNewElectiveService;
	
	private String recordType;
	
	@RequestMapping("/index")
	public String index(String type, HttpServletRequest request, ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
	    int period = NumberUtils.toInt(request.getParameter("period"));
	    map.put("period", period);
	    String gradeId = request.getParameter("gradeId");
	    map.put("gradeId", gradeId);
	    String qd = request.getParameter("queryDate");
    	String acadyear = request.getParameter("acadyear");
    	String semester = request.getParameter("semester");
    	Date queryDate = DateUtils.string2Date(qd);
		DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, Integer.parseInt(semester), queryDate), DateInfo.class);
		if(dateInfo == null){
			return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
		}
	    
	    String name = "上课日志信息";
		if("2".equals(type)) {
			name = "晚自习日志信息";
		}
		recordType = type;
		
	    // 业务名称
		map.put("businessName", name);
		// 导入URL 
		map.put("businessUrl", "/stuwork/courserecord/import/dataImport?type="+type);
		// 导入模板
		map.put("templateDownloadUrl", "/stuwork/courserecord/import/template?type="+type+"&acadyear="+acadyear+"&semester="+semester+"&queryDate="+qd);
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/stuwork/courserecord/import/validate?type="+type);
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
    	obj.put("acadyear", acadyear);
    	obj.put("semester", semester);
    	obj.put("queryDate", qd);
    	obj.put("unitId", unitId);
	    obj.put("type", type);
	    obj.put("teacherId", getLoginInfo().getOwnerId());
	    
	    map.put("importParams",JSON.toJSONString(obj));
	    map.put("description", description);
	    map.put("type", type);
	    map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("queryDate", qd);
		return "/stuwork/courserecord/recordImport.ftl";
	}

	public String getObjectName() {
		String name = "courseRecordImport";
		if("2".equals(recordType)) {
			name = "nightCourseRecordImport";
		}
		return name;
	}

	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、带*为必填</p>"
				+ "<p>2、[违纪学生]列维护学生<font style='color:red;'>学号</font>，多个学生以“,”隔开</p>"
				+ "<p>3、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>4、改变选项后请重新上传模板，同时不要随意修改模板中内容，否则容易导致上传文件与模板的不匹配</p>"
				+"<p>5、导入日志信息，增加系统里没有的班级日志信息，已有记录的班级则会覆盖原来的记录</p>"
				+"<p>6、请仔细查看模板中的提示，请严格根据提示填写数据</p>";
		return desc;
	}

	public List<String> getRowTitleList() {
		List<String> titles = new ArrayList<>();
		titles.add("*班级");
		if(StuworkConstants.COURSERECORD_SK_TYPE.equals(recordType)) {
			titles.add("*节次");
		}
		titles.add("考核分");
		titles.add("违纪学生");
		titles.add("备注");
		return titles;
	}

	@RequestMapping("/dataImport")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		JSONObject ob = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) ob.get("unitId");
		String acadyear = (String) ob.get("acadyear");
    	String semester = (String) ob.get("semester");
    	String teacherId = (String) ob.get("teacherId");
		recordType = (String) ob.get("type");
		String qd = (String) ob.get("queryDate");
		List<String[]> rowDatas = ExcelUtils.readExcelByRow(filePath, 1, getRowTitleList().size());
		int successCount = 0;
		int totalSize = rowDatas.size();
		List<String[]> errorDataList=new ArrayList<String[]>();
		if(CollectionUtils.isEmpty(rowDatas)){
        	return errorResult("0", "", "", "没有导入数据", 
					0, 0, 0, errorDataList);
        }
		try {
			Date queryDate = DateUtils.string2Date(qd);
			DateInfo dateInfo =  SUtils.dc(dateInfoRemoteService.findByDate(unitId, acadyear, Integer.parseInt(semester), queryDate), DateInfo.class);
			int week = dateInfo.getWeek();
			int weekDay = dateInfo.getWeekday();
			Map<String, List<CourseSchedule>> clsSches = new HashMap<>();
			Map<String, String> clsNames ;
			Map<String, DyCourseRecord> exMap = new HashMap<>();
			if(StuworkConstants.COURSERECORD_SK_TYPE.equals(recordType)) {
				List<CourseSchedule> courseScheduleList2 = getSchedulData(acadyear, semester, queryDate);
				clsSches = EntityUtils.getListMap(courseScheduleList2, CourseSchedule::getClassName, Function.identity());
				clsNames = EntityUtils.getMap(courseScheduleList2, CourseSchedule::getClassName, CourseSchedule::getClassId);
				List<DyCourseRecord> dyCourseRecordList = dyCourseRecordService.findListByPeriod(getLoginInfo().getUnitId(), acadyear, semester, StuworkConstants.COURSERECORD_SK_TYPE, week, weekDay, 0);
				if(CollectionUtils.isNotEmpty(dyCourseRecordList)) {
					for(DyCourseRecord re : dyCourseRecordList) {
						exMap.put(re.getRecordClass()+re.getPeriod()+re.getSubjectId(), re);
					}
				}
			} else {
				List<Clazz> clss = Clazz.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear));
				clsNames = EntityUtils.getMap(clss, Clazz::getClassNameDynamic, Clazz::getId);
			}
			Set<String> classIds = new HashSet<>();
			Map<String, DyCourseRecord> clsRecordMap = new HashMap<>();
			List<DyCourseRecord> records = new ArrayList<>();
			List<DyCourseStudentRecord> dyCourseStudentRecordList = new ArrayList<DyCourseStudentRecord>();
			int sequence = 0;
			int i = 0;
			Map<String, Set<String>> cstus = new HashMap<>();
			Pattern pattern = Pattern.compile("^(0|[1-5]\\d{0,1})(\\.\\d{0,1})?$");
			Pattern pattern2 = Pattern.compile("[0-3]?");
			for(String[] rows : rowDatas) {
				i++;
				int colIndex = 0;
				try {
					String clsName = StringUtils.trimToEmpty(rows[colIndex++]);
					if(StringUtils.isEmpty(clsName)) {
						addError(i+"", "班级", "", "班级不能为空。", sequence, errorDataList);
						continue;
					}
					String cid = clsNames.get(clsName);
					if(StringUtils.isEmpty(cid)) {
						addError(i+"", "班级", "", "班级在本校中不存在。", sequence, errorDataList);
						continue;
					}
					DyCourseRecord record = new DyCourseRecord();
					String key = cid;
					CourseSchedule cs = null;
					if(StuworkConstants.COURSERECORD_SK_TYPE.equals(recordType)) {
						List<CourseSchedule> css = clsSches.get(clsName);
						if(CollectionUtils.isEmpty(css)) {
							addError(i+"", "班级", "", "该班级没有课表信息，无需维护。", sequence, errorDataList);
							continue;
						}
						String periodStr = StringUtils.trimToEmpty(rows[colIndex++]);
						if(!NumberUtils.isDigits(periodStr)) {
							addError(i+"", "节次", "", "节次不是有效的正整数。", sequence, errorDataList);
							continue;
						}
						int period = NumberUtils.toInt(periodStr);
						Map<Integer, CourseSchedule> pecs = EntityUtils.getMap(css, CourseSchedule::getPeriod);
						cs = pecs.get(period);
						if(cs == null) {
							addError(i+"", "节次", "", "该班级本节次没有课。", sequence, errorDataList);
							continue;
						}
						key = cid + period;
						// 节次判断是否有效》学期数据，是否有课 TODO
						record.setPeriod(period);
						record.setSubjectId(cs.getSubjectId());
						record.setSubjectName(cs.getSubjectName());
						record.setTeacherId(cs.getTeacherId());
					}
					if(clsRecordMap.containsKey(key)) {
						addError(i+"", "班级", "", "本次导入中同样的记录已存在。", sequence, errorDataList);
						continue;
					}
					clsRecordMap.put(key, record);
					String score = StringUtils.trimToEmpty(rows[colIndex++]);
					if(StringUtils.isNotEmpty(score)) {
						if(StuworkConstants.COURSERECORD_WZX_TYPE.equals(recordType)) {//TODO
							float min = 0.0f;
							float max = 5.0f;
							if (!pattern.matcher(score).matches()){
								addError(i+"", "考核分", score, "考核分是["+min+"~"+max+"]范围内的数，最多一位小数。", sequence, errorDataList);
								continue;
							}
							if(!NumberUtils.isNumber(score)) {
								addError(i+"", "考核分", score, "考核分是["+min+"~"+max+"]范围内的数，最多一位小数。", sequence, errorDataList);
								continue;
							}
							float sc = NumberUtils.toFloat(score);
							if(sc < min || sc > max) {
								addError(i+"", "考核分", score, "考核分是["+min+"~"+max+"]范围内的数，最多一位小数。", sequence, errorDataList);
								continue;
							}
						} else {
							if (!pattern2.matcher(score).matches()){
								addError(i+"", "考核分", score, "考核分是[0~3]范围内的整数。", sequence, errorDataList);
								continue;
							}
						}
						DecimalFormat df=new DecimalFormat("0.#");
						score = df.format(NumberUtils.toFloat(score));
					}
					String codeStr = StringUtils.trimToEmpty(rows[colIndex++]);
					StringBuffer clsIds = new StringBuffer(); 
					List<Student> stus = null;
					if(StringUtils.isNotEmpty(codeStr)) {
						String[] codes = codeStr.replaceAll("，", ",").replaceAll(" ", "").replaceAll(" ", "").split(",");
						stus = Student.dt(studentRemoteService.findBySchIdStudentCodes(unitId, codes));
						Map<String, Student> stuMap = EntityUtils.getMap(stus, Student::getStudentCode);
						Set<String> stuIdSet = new HashSet<String>();
						if(cs != null) {
							if(CourseSchedule.CLASS_TYPE_TEACH == cs.getClassType()) {
								stuIdSet = cstus.get(cid);
								if(stuIdSet == null) {
									stuIdSet = new HashSet<>();
									List<TeachClassStu> teachClassStuList =  SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(new String[]{cid}),new TR<List<TeachClassStu>>() {});
									for(TeachClassStu item : teachClassStuList){
										stuIdSet.add(item.getStudentId());
								    }
									cstus.put(cid, stuIdSet);
								}
							} else if(CourseSchedule.CLASS_TYPE_4 == cs.getClassType()){
								stuIdSet = cstus.get(cid);
								if(stuIdSet == null) {
									stuIdSet = new HashSet<>();
									List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(cid);
									CollectionUtils.addAll(stuIdSet, stuIds.iterator());
								}
							}
						}
						StringBuilder nos = new StringBuilder();// 不存在
						StringBuilder notIns = new StringBuilder();// 不在这个班
						StringBuilder leaves = new StringBuilder();// 不在这个班
						for(String code : codes) {
							if(!stuMap.containsKey(code)) {
								nos.append(code+",");
								continue;
							}
							Student stu = stuMap.get(code);
							// 晚自习或者上课中的行政班
							if(StuworkConstants.COURSERECORD_WZX_TYPE.equals(recordType) 
									|| (cs != null && CourseSchedule.CLASS_TYPE_NORMAL == cs.getClassType())) {
								if(!cid.equals(stu.getClassId())) {
									notIns.append(code+",");
									continue;
								}
							} else if(!stuIdSet.contains(stu.getId())) {// 非行政班学生
								notIns.append(code+",");
								continue;
							}
							if(stu.getIsLeaveSchool() != null && stu.getIsLeaveSchool() == 1) {
								leaves.append(code+",");
								continue;
							}
							if(clsIds.indexOf(stu.getClassId()) == -1) {
								if(clsIds.length() > 0) {
									clsIds.append(",");
								}
								clsIds.append(stu.getClassId());
							}
						}
						if(nos.length() > 0 || notIns.length() > 0 || leaves.length() > 0) {
							StringBuilder msg = new StringBuilder();
							if(nos.length() > 0) {
								nos.setLength(nos.length() - 1);
								msg.append("学生["+nos.toString()+"]在系统中不存在;");
							}
							if(notIns.length() > 0) {
								notIns.setLength(notIns.length() - 1);
								msg.append("学生["+notIns.toString()+"]不是本班学生;");
							}
							if(leaves.length() > 0) {
								leaves.setLength(leaves.length() - 1);
								msg.append("学生["+leaves.toString()+"]已离校。");
							}
							addError(i+"", "违纪学生", "", msg.toString(), sequence, errorDataList);
							continue;
						}
					}
					String remark = StringUtils.trimToEmpty(rows[colIndex++]);
					if(StringUtils.isNotEmpty(remark)) {
						if(net.zdsoft.framework.utils.StringUtils.getRealLength(remark) > 500) {
							addError(i+"", "备注", "", "备注内容超出最大长度（500个字符）。", sequence, errorDataList);
							continue;
						}
					}
					successCount ++;
					classIds.add(cid);
					record.setRecordClass(cid);
					
					DyCourseRecord ex = exMap.get(record.getRecordClass()+record.getPeriod()+record.getSubjectId());
					if (ex == null) {
						record.setId(UuidUtils.generateUuid());
					} else {
						record.setId(ex.getId());
					}
					record.setAcadyear(acadyear);
					record.setSemester(semester);
					record.setDay(weekDay);
					record.setWeek(week);
					record.setRecordDate(queryDate);
					record.setSchoolId(unitId);
					if(clsIds.length() == 0) {
						clsIds.append(cid);
					}
					record.setClassId(clsIds.toString());
					record.setType(recordType);
					if (StringUtils.isEmpty(record.getTeacherId())) {
						record.setTeacherId(teacherId);
					}
					record.setScore(NumberUtils.toFloat(score));
					record.setRemark(remark);
					records.add(record);
					if(CollectionUtils.isNotEmpty(stus)) {
						for(Student stu : stus) {
							DyCourseStudentRecord stuRec = new DyCourseStudentRecord();
							stuRec.setId(UuidUtils.generateUuid());
							stuRec.setSchoolId(getLoginInfo().getUnitId());
							stuRec.setAcadyear(acadyear);
							stuRec.setSemester(semester);
							stuRec.setTeacherId(record.getTeacherId());
							stuRec.setType(record.getType());
							stuRec.setClassId(stu.getClassId());
							stuRec.setWeek(week);
							stuRec.setDay(weekDay);
							stuRec.setRecordId(record.getId());
							stuRec.setRecordDate(queryDate);
							stuRec.setStudentId(stu.getId());
							stuRec.setPeriod(record.getPeriod());
							dyCourseStudentRecordList.add(stuRec);
						}
					}
				} catch (RuntimeException re) {
					re.printStackTrace();
					addError(i+"", "第"+(i+1)+"行", "", re.getMessage(), sequence, errorDataList);
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					addError(i+"", "第"+(i+1)+"行", "", "数据整理出错。", sequence, errorDataList);
					log.error(e.getMessage(), e);
					continue;
				}
			}
			if (!records.isEmpty()) {
				dyCourseRecordService.saveNightCourseRecord(records, dyCourseStudentRecordList, acadyear, semester,
						week, weekDay, recordType, classIds.toArray(new String[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return errorResult("0", "", "", "导入失败：" + e.getMessage(), 0, totalSize, 0, errorDataList);
		}
		return result(totalSize, successCount, totalSize-successCount, errorDataList);
	}
	
	/**
	 * 返回错误消息
	 */
	private String errorResult(String dataIndex, String title, String colContent, String errorMsg, 
			int sequence, int total, int success, List<String[]> errorDataList) {
		addError(dataIndex, title, colContent, errorMsg, sequence, errorDataList);
        return result(total, success, total-success, errorDataList);
	}
	
	/**
	 * 结果信息
	 * @param totalCount
	 * @param successCount
	 * @param errorCount
	 * @param errorDataList
	 * @return
	 */
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	}
	
	/**
	 * 添加错误
	 */
	private void addError(String da1, String colStr, String colContent, String errorMsg, 
			int sequence, List<String[]> errorDataList) {
		String[] errorData=new String[4];
		sequence++;
        errorData[0]=StringUtils.trimToEmpty(da1);
        errorData[1]=StringUtils.trimToEmpty(colStr);
        errorData[2]=StringUtils.trimToEmpty(colContent);
        errorData[3]=StringUtils.trimToEmpty(errorMsg);
        errorDataList.add(errorData);
	}

	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
		recordType = request.getParameter("type");
		List<String> titleList = getRowTitleList();//表头
		Map<String,List<Map<String,String>>> sheetName2Map = new HashMap<String, List<Map<String, String>>>();
		sheetName2Map.put(getObjectName(),new ArrayList<Map<String, String>>());
		Map<String,List<String>> titleMap = new HashMap<String, List<String>>();
	    titleMap.put(getObjectName(),titleList);
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
        HSSFRow rowTitle = sheet.createRow(0);
        for(int j=0;j<size;j++){
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
        
        if (StuworkConstants.COURSERECORD_SK_TYPE.equals(recordType)) {
			String qd = request.getParameter("queryDate");
			String acadyear = request.getParameter("acadyear");
			String semester = request.getParameter("semester");
			Date queryDate = DateUtils.string2Date(qd);
			List<CourseSchedule> courseScheduleList2 = getSchedulData(acadyear, semester, queryDate);
			Collections.sort(courseScheduleList2,new Comparator<CourseSchedule>(){
	            public int compare(CourseSchedule arg0, CourseSchedule arg1) {
	                return String.valueOf(arg0.getPeriod()).compareTo(String.valueOf(arg1.getPeriod()));
	            }
	        });
			int rowIndex = 1;
			for (CourseSchedule item1 : courseScheduleList2) {
				HSSFRow row = sheet.createRow(rowIndex++);
				for(int j=0;j<size;j++){
		        	HSSFCell cell = row.createCell(j);
		        	if(j==0) {
		        		cell.setCellValue(new HSSFRichTextString(item1.getClassName()));
		        	} else if(j==1) {
		        		cell.setCellValue(new HSSFRichTextString(item1.getPeriod()+""));
		        	} else {
		        		cell.setCellValue(new HSSFRichTextString(""));
		        	}
		        }
			} 
		}
		// TODO 班级数据
		String fileName = getObjectName();
		ExportUtils.outputData(workbook, fileName, response);
	}
	
	private List<CourseSchedule> getSchedulData(String acadyear, String semester, Date queryDate){
		List<CourseSchedule> courseScheduleList2 = new ArrayList<CourseSchedule>();
		String unitId = getLoginInfo().getUnitId();
		Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
		DateInfo dateInfo = SUtils.dc(
				dateInfoRemoteService.findByDate(unitId, acadyear, Integer.parseInt(semester), queryDate),
				DateInfo.class);
		int week = dateInfo.getWeek();
		int weekDay = dateInfo.getWeekday();
		Integer per = 0;
		CourseScheduleDto dto = new CourseScheduleDto();
		dto.setSchoolId(unitId);
		dto.setAcadyear(acadyear);
		dto.setSemester(NumberUtils.toInt(semester));
		dto.setWeekOfWorktime(week);
		dto.setDayOfWeek(weekDay - 1);
		dto.setPeriod(per);
		List<CourseSchedule> courseScheduleList = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),
				new TR<List<CourseSchedule>>() {
				});
		//取出要查询的周次
		Set<String> teachClsIdSet2 = new HashSet<String>();
		Set<String> clsSet = new HashSet<String>();
		Set<String> subjectSet = new HashSet<String>();
		Set<String> xuanxiukeIdSet = new HashSet<String>();
		for (CourseSchedule item : courseScheduleList) {
			if ((weekDay - 1) == item.getDayOfWeek()) {
				//矫正课表中节次与日志中节次不一致
				if (CourseSchedule.PERIOD_INTERVAL_1.equals(item.getPeriodInterval())) {
					item.setPeriod(item.getPeriod());
				} else if (CourseSchedule.PERIOD_INTERVAL_2.equals(item.getPeriodInterval())) {
					item.setPeriod(item.getPeriod() + sem.getMornPeriods());
				} else if (CourseSchedule.PERIOD_INTERVAL_3.equals(item.getPeriodInterval())) {
					item.setPeriod(item.getPeriod() + sem.getMornPeriods() + sem.getAmPeriods());
				} else if (CourseSchedule.PERIOD_INTERVAL_4.equals(item.getPeriodInterval())) {
					item.setPeriod(
							item.getPeriod() + sem.getMornPeriods() + sem.getAmPeriods() + sem.getPmPeriods());
				}
				courseScheduleList2.add(item);
				subjectSet.add(item.getSubjectId());
				if (CourseSchedule.CLASS_TYPE_TEACH == item.getClassType()) {
					teachClsIdSet2.add(item.getClassId());
				} else if (CourseSchedule.CLASS_TYPE_4 == item.getClassType()) {
					xuanxiukeIdSet.add(item.getClassId());
				} else {
					clsSet.add(item.getClassId());
				}
			}
		}
		List<Clazz> clsList2 = SUtils.dt(classRemoteService.findClassListByIds(clsSet.toArray(new String[0])),
				new TR<List<Clazz>>() {
				});
		Map<String, String> clsMap2 = new HashMap<String, String>();
		for (Clazz cls : clsList2) {
			clsMap2.put(cls.getId(), cls.getClassNameDynamic());
		}
		Map<String, String> clsNameMap = getTeachClsNameMap(teachClsIdSet2);
		for (CourseSchedule item1 : courseScheduleList2) {
			if (CourseSchedule.CLASS_TYPE_TEACH == item1.getClassType()) {
				item1.setClassName(clsNameMap.get(item1.getClassId()));
			} else if (CourseSchedule.CLASS_TYPE_4 == item1.getClassType()) {
				item1.setClassName(item1.getSubjectName());
			} else {
				item1.setClassName(clsMap2.get(item1.getClassId()));
			}
		}
		return courseScheduleList2;
	}
	
	/**
	 * 获取教学班
	 * @param teachClsIdSet
	 * @return
	 */
	private Map<String, String> getTeachClsNameMap(Set<String> teachClsIdSet){
		Map<String, String> teachClsNameMap = new HashMap<String, String>();
		if(CollectionUtils.isEmpty(teachClsIdSet)){
			return teachClsNameMap;
		}
		List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findTeachClassContainNotUseByIds(teachClsIdSet.toArray(new String[0])),TeachClass.class);
		if(CollectionUtils.isNotEmpty(teachClasses)){
			for (TeachClass c : teachClasses) {
				teachClsNameMap.put(c.getId(), c.getName());
			}
		}
		return teachClsNameMap;
	}
	
	/**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo, HttpServletRequest request){
		log.info("模板校验中......");
		validRowStartNo = "0";
		try{
			recordType = request.getParameter("type");
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
		} catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	public OpenApiNewElectiveService getOpenApiNewElectiveService() {
        if (openApiNewElectiveService == null) {
        	openApiNewElectiveService = Evn.getBean("openApiNewElectiveService");
            if(openApiNewElectiveService == null){
				System.out.println("openApiNewElectiveService为null，需开启dubbo服务");
			}
        }
        return openApiNewElectiveService;
    }
	
}
