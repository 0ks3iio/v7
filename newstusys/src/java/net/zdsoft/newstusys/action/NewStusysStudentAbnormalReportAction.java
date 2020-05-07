package net.zdsoft.newstusys.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.dto.ClazzCountDto;
import net.zdsoft.newstusys.dto.GradeCountDto;
import net.zdsoft.newstusys.dto.UnitCountDto;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import net.zdsoft.newstusys.service.StudentAbnormalFlowService;
import net.zdsoft.newstusys.service.StudentReportService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

@Controller
@RequestMapping("/newstusys/student/abnormalreport")
public class NewStusysStudentAbnormalReportAction extends BaseAction{	
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentReportService studentReportService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StudentAbnormalFlowService studentAbnormalFlowService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	private int section = 0;
	private List<McodeDetail> allFlowTypes = new ArrayList<>();
	private List<McodeDetail> leaveFlowTypes = new ArrayList<>();
	private List<McodeDetail> enterFlowTypes = new ArrayList<>();
	private Map<String, Integer> clsFlowCountMap = new HashMap<>();
	private String deploy;
	
	@RequestMapping("/index/page")
	public String indexPage(ModelMap map){
		Unit un = Unit.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()));
		map.put("topEdu", un.getUnitType() == Unit.UNIT_EDU_TOP);
		return "/newstusys/abnormalreport/abnormalreportAdmin.ftl";
	}
	
	@RequestMapping("/unitList")
	public String roster(String type, ModelMap map){
		map.put("unitClass", getLoginInfo().getUnitClass());
		map.put("unitId", getLoginInfo().getUnitId());
		map.put("type", type);
		return "/newstusys/abnormalreport/unitList.ftl";
	}
	
	//按班级统计
	@RequestMapping("/classCount")
	public String classCount(String unitId, String acadyear, String semester, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		Semester se;
		se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(null==se){
			se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		}
		if(StringUtils.isBlank(acadyear)){		
			acadyear = se.getAcadyear();
		}
		if(StringUtils.isBlank(semester)){			
			semester = String.valueOf(se.getSemester());
		}	
		deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deploy", deploy);
		getFlowTypes();
		List<ClazzCountDto> dtoList = clazzCountDtoList(acadyear,semester,unitId);
		map.put("unitId", unitId);
		map.put("dtoList", dtoList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("leaveFlowTypes", leaveFlowTypes);
		map.put("enterFlowTypes", enterFlowTypes);
		map.put("clsFlowCountMap", clsFlowCountMap);
		return "/newstusys/abnormalreport/classCount.ftl";
	}
	
	/**
	 * 获取系统转入/出的异动类型数据
	 */
	private void getFlowTypes() {
		allFlowTypes.clear();
		leaveFlowTypes.clear();
		leaveFlowTypes.addAll(SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-YDLB"), new TR<List<McodeDetail>>() {}));
		String[] withIn = BaseStudentConstants.getFlowTypeBelong(BaseStudentConstants.FLOWTYPE_LEAVE);
		leaveFlowTypes = BaseStudentConstants.findInUsingMcode(leaveFlowTypes, withIn);
		allFlowTypes.addAll(leaveFlowTypes);
		
		enterFlowTypes.clear();
		enterFlowTypes = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-YDLB"), new TR<List<McodeDetail>>() {});
		String[] enWithIn = BaseStudentConstants.getFlowTypeBelong(BaseStudentConstants.FLOWTYPE_ENTER);
		enterFlowTypes = BaseStudentConstants.findInUsingMcode(enterFlowTypes, enWithIn);
		allFlowTypes.addAll(enterFlowTypes);
	}
	
	@RequestMapping("/classCountExport")
	public void classCountExport(String unitId, String acadyear, String semester,HttpServletResponse response){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(acadyear+"学年第"+semester+"学期"+unit.getUnitName()+"异动按班统计表");
		
		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFFont headfont1 = workbook.createFont();
		headfont1.setFontHeightInPoints((short) 12);// 字体大小
		headfont1.setBold(true);// 加粗
		style1.setFont(headfont1);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setVerticalAlignment(VerticalAlignment.CENTER);
		
		//第2行
		HSSFRow row2 = sheet.createRow(1);
		CellRangeAddress car21 = new CellRangeAddress(1,1,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
	    sheet.addMergedRegion(car21);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellStyle(style1);
		cell21.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car22 = new CellRangeAddress(1,2,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
	    sheet.addMergedRegion(car22);
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellStyle(style1);
		cell22.setCellValue(new HSSFRichTextString("年级"));
		
		CellRangeAddress car23 = new CellRangeAddress(1,2,3,3);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car23, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car23, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car23, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car23, sheet); 
	    sheet.addMergedRegion(car23);
		HSSFCell cell23 = row2.createCell(3);
		cell23.setCellStyle(style1);
		cell23.setCellValue(new HSSFRichTextString("班级"));
		
		CellRangeAddress car25 = new CellRangeAddress(1,1,4,11);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car25, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car25, sheet); 
	    sheet.addMergedRegion(car25);
		HSSFCell cell25 = row2.createCell(4);
		cell25.setCellStyle(style1);
		cell25.setCellValue(new HSSFRichTextString("转入学生"));
		
		CellRangeAddress car26 = new CellRangeAddress(1,1,12,18);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car26, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car26, sheet); 
	    sheet.addMergedRegion(car26);
		HSSFCell cell26 = row2.createCell(12);
		cell26.setCellStyle(style1);
		cell26.setCellValue(new HSSFRichTextString("转出学生"));
		
		//第3行
		HSSFRow row3 = sheet.createRow(2);
		String[] titles = new String[] { "所在镇乡（街道）", "校点名称", "","","复 学", "入 境", "县区内转入", "省内转入	", "省外转入", "户口新报", "一年级补报",
				"其他增加", "休 学", "出 境", "转往县区内", "转往省内", "转往省外", "死 亡", "其他减少" };
		for (int i = 0; i < titles.length; i++) {
			if (i < 4) {
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			HSSFCell cell31 = row3.createCell(i);
			cell31.setCellStyle(style1);
			cell31.setCellValue(new HSSFRichTextString(titles[i]));
		}
		
		//第一行  大标题行
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 16);// 字体大小
		headfont.setBold(true);// 加粗
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		HSSFRow row1 = sheet.createRow(0);
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,titles.length-1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString(acadyear+"学年第"+semester+"学期"+unit.getUnitName()+"异动按班统计表"));
		
		// 内容行
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFFont headfont2 = workbook.createFont();
		headfont2.setFontHeightInPoints((short) 12);// 字体大小
		style2.setFont(headfont2);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		style2.setWrapText(true);
		List<ClazzCountDto> dtoList = clazzCountDtoList(acadyear, semester, unitId);//TODO
		int i=3;
		for(ClazzCountDto cls : dtoList){
			HSSFRow row4 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell41 = row4.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(cls.getAddress()));
			
			HSSFCell cell42 = row4.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(cls.getSchoolName()));
			
			HSSFCell cell43 = row4.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(cls.getGradeName()));
			
			HSSFCell cell44 = row4.createCell(j++);
			cell44.setCellStyle(style2);
			cell44.setCellValue(new HSSFRichTextString(cls.getClassName()));
			
			HSSFCell cell46 = row4.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(cls.getFx())));
			
			HSSFCell cell47 = row4.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(String.valueOf(cls.getRj())));
			
			HSSFCell cell48 = row4.createCell(j++);
			cell48.setCellStyle(style2);
			cell48.setCellValue(new HSSFRichTextString(String.valueOf(cls.getXqnzr())));
			
			HSSFCell cell49 = row4.createCell(j++);
			cell49.setCellStyle(style2);
			cell49.setCellValue(new HSSFRichTextString(String.valueOf(cls.getSnzr())));
			
			HSSFCell cell410 = row4.createCell(j++);
			cell410.setCellStyle(style2);
			cell410.setCellValue(new HSSFRichTextString(String.valueOf(cls.getSwzr())));
			
			HSSFCell cell411 = row4.createCell(j++);
			cell411.setCellStyle(style2);
			cell411.setCellValue(new HSSFRichTextString(String.valueOf(cls.getHkxb())));
			
			HSSFCell cell412 = row4.createCell(j++);
			cell412.setCellStyle(style2);
			cell412.setCellValue(new HSSFRichTextString(String.valueOf(cls.getYnjbb())));
			
			HSSFCell cell413 = row4.createCell(j++);
			cell413.setCellStyle(style2);
			cell413.setCellValue(new HSSFRichTextString(String.valueOf(cls.getQtzj())));
			
			HSSFCell cell414 = row4.createCell(j++);
			cell414.setCellStyle(style2);
			cell414.setCellValue(new HSSFRichTextString(String.valueOf(cls.getXx())));
			
			HSSFCell cell415 = row4.createCell(j++);
			cell415.setCellStyle(style2);
			cell415.setCellValue(new HSSFRichTextString(String.valueOf(cls.getCj())));
			
			HSSFCell cell416 = row4.createCell(j++);
			cell416.setCellStyle(style2);
			cell416.setCellValue(new HSSFRichTextString(String.valueOf(cls.getZwxqn())));
			
			HSSFCell cell417 = row4.createCell(j++);
			cell417.setCellStyle(style2);
			cell417.setCellValue(new HSSFRichTextString(String.valueOf(cls.getZwsn())));
			
			HSSFCell cell418 = row4.createCell(j++);
			cell418.setCellStyle(style2);
			cell418.setCellValue(new HSSFRichTextString(String.valueOf(cls.getZwsw())));
			
			HSSFCell cell419 = row4.createCell(j++);
			cell419.setCellStyle(style2);
			cell419.setCellValue(new HSSFRichTextString(String.valueOf(cls.getSw())));
			
			HSSFCell cell420 = row4.createCell(j++);
			cell420.setCellStyle(style2);
			cell420.setCellValue(new HSSFRichTextString(String.valueOf(cls.getQtjs())));
		}
		
		ExportUtils.outputData(workbook, acadyear+"学年第"+semester+"学期"+unit.getUnitName()+"异动按班统计表", response);
	}
	
	public List<ClazzCountDto> clazzCountDtoList(String acadyear,String semester,String unitId){
		List<ClazzCountDto> dtoList = new ArrayList<ClazzCountDto>();
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		List<String> schIds = new ArrayList<>();
		List<Unit> schs;
		if(unit.getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
			schs = new ArrayList<>();
			schs.add(unit);
		} else {
			schs = Unit.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL));
		}
		if(CollectionUtils.isEmpty(schs)) {
			return dtoList;
		}
		schIds = EntityUtils.getList(schs, Unit::getId);
		Set<String> pids = EntityUtils.getSet(schs, Unit::getParentId);
		Map<String, Unit> unitNames = EntityUtils.getMap(schs, Unit::getId);
		pids.removeAll(unitNames.keySet());
		unitNames.putAll(EntityUtils.getMap(Unit.dt(unitRemoteService.findListByIds(pids.toArray(new String[0]))), Unit::getId));
		List<Clazz> clsList = new ArrayList<>();
		for (String schId : schIds) {
			clsList.addAll(SUtils.dt(classRemoteService.findByIdCurAcadyear(schId, acadyear), new TR<List<Clazz>>() {
			}));
		}
		Set<String> gradeIdSet = new HashSet<String>();
		Set<String> clsIdSet = new HashSet<String>();
		for(Clazz cls : clsList){
			gradeIdSet.add(cls.getGradeId());
			clsIdSet.add(cls.getId());
		}
		Map<String, String> gradeNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(gradeIdSet)){
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIdSet.toArray(new String[0])), new TR<List<Grade>>(){});
			for(Grade grade : gradeList){
				gradeNameMap.put(grade.getId(), grade.getGradeName());
			}
		}
		Map<String, Integer> clsStudentMap = new HashMap<String, Integer>();//班内学生数
		if(CollectionUtils.isNotEmpty(clsIdSet)){
			clsStudentMap = studentReportService.stuCountByClass(clsIdSet.toArray(new String[0]));
			clsFlowCountMap = studentReportService.ydByClassIdsTypes(acadyear, semester, EntityUtils.getList(allFlowTypes, McodeDetail::getThisId).toArray(new String[0]), clsIdSet.toArray(new String[0])) ;
		}
		int clsStudentCount=0;//班内学生数
		for(Clazz cls : clsList){
			ClazzCountDto dto = new ClazzCountDto();
			dto.setClsId(cls.getId());
			dto.setClassName(cls.getClassNameDynamic());
			Unit sch = unitNames.get(cls.getSchoolId());
			dto.setSchoolName(sch.getUnitName());
			if (unitNames.containsKey(sch.getParentId())) {
				dto.setAddress(unitNames.get(sch.getParentId()).getUnitName());
			}
			dto.setGradeName(gradeNameMap.get(cls.getGradeId()));
			dto.setOpenAcadyear(cls.getAcadyear());
			dto.setClassCode(cls.getClassCode());
			dto.setSection(cls.getSection());
			if(null!=clsStudentMap.get(cls.getId())){		
				clsStudentCount = clsStudentCount+clsStudentMap.get(cls.getId());
				dto.setClsStudentCount(clsStudentMap.get(cls.getId()));
			}
			dtoList.add(dto);
		}
		
		Collections.sort(dtoList,new Comparator<ClazzCountDto>() {
            @Override
            public int compare(ClazzCountDto o1, ClazzCountDto o2) {	                
            	String street1 = o1.getAddress();
                String street2 = o2.getAddress();
            	if(!StringUtils.equals(street1, street2)) {
            		return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
            	
            	street1 = o1.getSchoolName();
                street2 = o2.getSchoolName();
                if(!StringUtils.equals(street1, street2)) {
            		return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
            	
            	int sec1 = o1.getSection();
                int sec2 = o2.getSection();
                if(sec1!=sec2) {
                	return sec1-sec2;
                }
                
                street1 = o1.getOpenAcadyear();
                street2 = o2.getOpenAcadyear();
                if(!StringUtils.equals(street1, street2)) {
                	return StringUtils.trimToEmpty(street2).compareTo(street1);
                }
                
                street1 = o1.getClassCode();
                street2 = o2.getClassCode();
                if(!StringUtils.equals(street1, street2)) {
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                	                
                return StringUtils.trimToEmpty(street1).compareTo(street2);
            }
        });
		
		if(CollectionUtils.isNotEmpty(dtoList)){				
			ClazzCountDto dto = new ClazzCountDto();
			dto.setSchoolName("合计");
			dto.setClsStudentCount(clsStudentCount);
			dto.setClsId(BaseConstants.ZERO_GUID);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	
	//按年级统计
	@RequestMapping("/gradeCount")
	public String gradeCount(String acadyear, String semester, String unitId, ModelMap map){
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		Semester se;
		se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(null==se){
			se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		}
		if(StringUtils.isBlank(acadyear)){		
			acadyear = se.getAcadyear();
		}
		if(StringUtils.isBlank(semester)){			
			semester = String.valueOf(se.getSemester());
		}
		deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deploy", deploy);
		getFlowTypes();
		List<GradeCountDto> dtoList = gradeCountDtoList(acadyear, semester, unitId);
		map.put("unitId", unitId);
		map.put("dtoList", dtoList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		return "/newstusys/abnormalreport/gradeCount.ftl";
	}
	
	public List<GradeCountDto> gradeCountDtoList(String acadyear, String semester, String unitId){
		List<GradeCountDto> dtoList = new ArrayList<GradeCountDto>();
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		Set<String> gradeIdSet = new HashSet<String>();
		List<Grade> gradeList = new ArrayList<Grade>();
		Map<String, String> pNameMap = new HashMap<String, String>();
		Map<String, String> pUnitNameMap = new HashMap<String, String>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		
		Set<String> unitIdSet = new HashSet<String>();
		if(unit.getUnitClass()==1){
			List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});	
		    Set<String> punitIdSet = new HashSet<String>();
			for(Unit u : unitList){
		    	unitIdSet.add(u.getId());
		    	punitIdSet.add(u.getParentId());
		    }
		    List<Unit> punitList = SUtils.dt(unitRemoteService.findListByIds(punitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});	
			for(Unit u : punitList){
				pNameMap.put(u.getId(), u.getUnitName());
			}
			for(Unit u : unitList){
				pUnitNameMap.put(u.getId(), pNameMap.get(u.getParentId()));
				unitNameMap.put(u.getId(), u.getUnitName());
		    }
		    if(CollectionUtils.isNotEmpty(unitIdSet)){
		    	gradeList = SUtils.dt(gradeRemoteService.findByUnitIdsAndCurrentAcadyear(unitIdSet.toArray(new String[0]),acadyear), new TR<List<Grade>>(){});
		    	for(Grade grade : gradeList){
					gradeIdSet.add(grade.getId());
				}
		    }
		}else{
			Unit punit = SUtils.dc(unitRemoteService.findOneById(unit.getParentId()), Unit.class);			
			pUnitNameMap.put(unit.getId(), punit.getUnitName());
			unitNameMap.put(unit.getId(), unit.getUnitName());
			gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId,acadyear), new TR<List<Grade>>(){});
			for(Grade grade : gradeList){
				gradeIdSet.add(grade.getId());
			}			
		}
		Map<String, Integer> fxCountMap = new HashMap<String, Integer>();//复学
		Map<String, Integer> rjCountMap = new HashMap<String, Integer>();//入境			
		Map<String, Integer> xqnzrCountMap = new HashMap<String, Integer>();//县区内转入
		Map<String, Integer> snzrCountMap = new HashMap<String, Integer>();//省内转入
		Map<String, Integer> swzrCountMap = new HashMap<String, Integer>();//省外转入
		Map<String, Integer> hkxbCountMap = new HashMap<String, Integer>();//户口新报
		Map<String, Integer> ynjbbCountMap = new HashMap<String, Integer>();//一年级补报
		Map<String, Integer> qtzjCountMap = new HashMap<String, Integer>();//其他增加
		Map<String, Integer> xxCountMap = new HashMap<String, Integer>();//休学
		Map<String, Integer> cjCountMap = new HashMap<String, Integer>();//出境
		Map<String, Integer> zwxqnCountMap = new HashMap<String, Integer>();//转往县区内
		Map<String, Integer> zwsnCountMap = new HashMap<String, Integer>();//转往省内	
		Map<String, Integer> zwswCountMap = new HashMap<String, Integer>();//转往省外
		Map<String, Integer> swCountMap = new HashMap<String, Integer>();//死亡
		Map<String, Integer> qtjsCountMap = new HashMap<String, Integer>();//其他减少
		if(CollectionUtils.isNotEmpty(gradeIdSet)){
			fxCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_FX, gradeIdSet.toArray(new String[0]));
			qtzjCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_QTZJ, gradeIdSet.toArray(new String[0]));
			qtjsCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_QTJS, gradeIdSet.toArray(new String[0]));
			swCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_SW, gradeIdSet.toArray(new String[0]));
			xxCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_XX, gradeIdSet.toArray(new String[0]));
			cjCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_CJ, gradeIdSet.toArray(new String[0]));
			if(BaseConstants.DEPLOY_HANGWAI.equals(deploy)) {
				xqnzrCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZR, gradeIdSet.toArray(new String[0]));
				zwxqnCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZC, gradeIdSet.toArray(new String[0]));
			} else {
				rjCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_RJ, gradeIdSet.toArray(new String[0]));
				xqnzrCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_XQNZR, gradeIdSet.toArray(new String[0]));
				snzrCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_SNZR, gradeIdSet.toArray(new String[0]));
				swzrCountMap =studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_SWZR, gradeIdSet.toArray(new String[0]));
				hkxbCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_HKXB, gradeIdSet.toArray(new String[0]));
				ynjbbCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_YNJBB, gradeIdSet.toArray(new String[0]));
				zwxqnCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZWXQN, gradeIdSet.toArray(new String[0]));
				zwsnCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZWSN, gradeIdSet.toArray(new String[0]));
				zwswCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZWSW, gradeIdSet.toArray(new String[0]));
			}
		}
		int fx=0;//复学
		int rj=0;//入境
		int xqnzr=0;//县区内转入
		int snzr=0;//省内转入
		int swzr=0;//省外转入
		int hkxb=0;//户口新报
		int ynjbb=0;//一年级补报
		int qtzj=0;//其他增加	
		int xx=0;//休学
		int cj=0;//出境
		int zwxqn=0;//转往县区内
		int zwsn=0;//转往省内
		int zwsw=0;//转往省外
		int sw=0;//死亡	
		int qtjs=0;//其他减少
		for(Grade grade : gradeList){
			GradeCountDto dto = new GradeCountDto();
			dto.setAddress(pUnitNameMap.get(grade.getSchoolId()));
			dto.setGradeName(grade.getGradeName());
			dto.setSchoolName(unitNameMap.get(grade.getSchoolId()));
			dto.setGradeCode(grade.getGradeCode());
			if(null!=fxCountMap.get(grade.getId())){	
				fx = fx + fxCountMap.get(grade.getId());
				dto.setFx(fxCountMap.get(grade.getId()));
			}
			if(null!=rjCountMap.get(grade.getId())){
				rj = rj + rjCountMap.get(grade.getId());
				dto.setRj(rjCountMap.get(grade.getId()));
			}
			if(null!=xqnzrCountMap.get(grade.getId())){
				xqnzr = xqnzr + xqnzrCountMap.get(grade.getId());
				dto.setXqnzr(xqnzrCountMap.get(grade.getId()));
			}
			if(null!=snzrCountMap.get(grade.getId())){	
				snzr = snzr + snzrCountMap.get(grade.getId());
				dto.setSnzr(snzrCountMap.get(grade.getId()));
			}
			if(null!=swzrCountMap.get(grade.getId())){	
				swzr = swzr + swzrCountMap.get(grade.getId());
				dto.setSwzr(swzrCountMap.get(grade.getId()));
			}
			if(null!=hkxbCountMap.get(grade.getId())){	
				hkxb = hkxb + hkxbCountMap.get(grade.getId());
				dto.setHkxb(hkxbCountMap.get(grade.getId()));
			}
			if(null!=ynjbbCountMap.get(grade.getId())){		
				ynjbb = ynjbb + ynjbbCountMap.get(grade.getId());
				dto.setYnjbb(ynjbbCountMap.get(grade.getId()));
			}
			if(null!=qtzjCountMap.get(grade.getId())){	
				qtzj = qtzj + qtzjCountMap.get(grade.getId());
				dto.setQtzj(qtzjCountMap.get(grade.getId()));
			}
			if(null!=xxCountMap.get(grade.getId())){	
				xx = xx + xxCountMap.get(grade.getId());
				dto.setXx(xxCountMap.get(grade.getId()));
			}
			if(null!=cjCountMap.get(grade.getId())){	
				cj = cj + cjCountMap.get(grade.getId());
				dto.setCj(cjCountMap.get(grade.getId()));
			}
			if(null!=zwxqnCountMap.get(grade.getId())){	
				zwxqn = zwxqn + zwxqnCountMap.get(grade.getId());
				dto.setZwxqn(zwxqnCountMap.get(grade.getId()));
			}
			if(null!=zwsnCountMap.get(grade.getId())){	
				zwsn = zwsn + zwsnCountMap.get(grade.getId());
				dto.setZwsn(zwsnCountMap.get(grade.getId()));
			}
			if(null!=zwswCountMap.get(grade.getId())){	
				zwsw = zwsw + zwswCountMap.get(grade.getId());
				dto.setZwsw(zwswCountMap.get(grade.getId()));
			}
			if(null!=swCountMap.get(grade.getId())){	
				sw = sw + swCountMap.get(grade.getId());
				dto.setSw(swCountMap.get(grade.getId()));
			}
			if(null!=qtjsCountMap.get(grade.getId())){	
				qtjs = qtjs + qtjsCountMap.get(grade.getId());
				dto.setQtjs(qtjsCountMap.get(grade.getId()));
			}
			dtoList.add(dto);
		}
		
		Collections.sort(dtoList,new Comparator<GradeCountDto>() {
            @Override
            public int compare(GradeCountDto o1, GradeCountDto o2) {
                String street1 = o1.getAddress();
                String street2 = o2.getAddress();
                if(!StringUtils.equals(street1, street2)) {// 教育局
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                street1 = o1.getSchoolName();
                street2 = o2.getSchoolName();
                if(!StringUtils.equals(street1, street2)) {// 学校
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                street1 = o1.getGradeCode();
                street2 = o2.getGradeCode();
                if(!StringUtils.equals(street1, street2)) {// 年级
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                return StringUtils.trimToEmpty(street1).compareTo(street2);
            }
        });
		
		
		if(CollectionUtils.isNotEmpty(dtoList)){				
			GradeCountDto dto = new GradeCountDto();
			dto.setSchoolName("小计");
			dto.setFx(fx);
			dto.setRj(rj);
			dto.setXqnzr(xqnzr);
			dto.setSnzr(snzr);
			dto.setSwzr(swzr);
			dto.setHkxb(hkxb);
			dto.setYnjbb(ynjbb);
			dto.setQtzj(qtzj);
			dto.setXx(xx);
			dto.setCj(cj);
			dto.setZwxqn(zwxqn);			
			dto.setZwsn(zwsn);
			dto.setZwsw(zwsw);
			dto.setSw(sw);
			dto.setQtjs(qtjs);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	
	@RequestMapping("/gradeCountExport")
	public void gradeCountExport(String unitId, String acadyear, String semester,HttpServletResponse response){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(acadyear+"学年第"+semester+"学期"+unit.getUnitName()+"异动按年级统计表");
     			
		//第2行
		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFFont headfont1 = workbook.createFont();
		headfont1.setFontHeightInPoints((short) 12);// 字体大小
		headfont1.setBold(true);// 加粗
		style1.setFont(headfont1);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setVerticalAlignment(VerticalAlignment.CENTER);
		
		HSSFRow row2 = sheet.createRow(1);
		CellRangeAddress car21 = new CellRangeAddress(1,1,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
	    sheet.addMergedRegion(car21);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellStyle(style1);
		cell21.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car22 = new CellRangeAddress(1,2,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
	    sheet.addMergedRegion(car22);
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellStyle(style1);
		cell22.setCellValue(new HSSFRichTextString("年级"));
		
		CellRangeAddress car25 = new CellRangeAddress(1,1,3,10);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car25, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car25, sheet); 
	    sheet.addMergedRegion(car25);
		HSSFCell cell25 = row2.createCell(3);
		cell25.setCellStyle(style1);
		cell25.setCellValue(new HSSFRichTextString("转入学生"));
		
		CellRangeAddress car26 = new CellRangeAddress(1,1,11,17);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car26, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car26, sheet); 
	    sheet.addMergedRegion(car26);
		HSSFCell cell26 = row2.createCell(11);
		cell26.setCellStyle(style1);
		cell26.setCellValue(new HSSFRichTextString("转出学生"));
		
		//第3行
		HSSFRow row3 = sheet.createRow(2);
		String[] titles = new String[] { "所在镇乡（街道）", "校点名称", "","复 学", "入 境", "县区内转入", "省内转入	", "省外转入", "户口新报", "一年级补报",
				"其他增加", "休 学", "出 境", "转往县区内", "转往省内", "转往省外", "死 亡", "其他减少" };
		for (int i = 0; i < titles.length; i++) {
			if (i < 3) {
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			HSSFCell cell31 = row3.createCell(i);
			cell31.setCellStyle(style1);
			cell31.setCellValue(new HSSFRichTextString(titles[i]));
		}
		
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 16);// 字体大小
		headfont.setBold(true);// 加粗
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		//第一行
		HSSFRow row1 = sheet.createRow(0);
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,titles.length-1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString(acadyear+"学年第"+semester+"学期"+unit.getUnitName()+"异动按年级统计表"));
		
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFFont headfont2 = workbook.createFont();
		headfont2.setFontHeightInPoints((short) 12);// 字体大小
		style2.setFont(headfont2);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		style2.setWrapText(true);
		deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		List<GradeCountDto> dtoList = gradeCountDtoList(acadyear, semester, unitId);
		int i=3;
		for(GradeCountDto grade : dtoList){
			HSSFRow row4 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell41 = row4.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(grade.getAddress()));
			
			HSSFCell cell42 = row4.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(grade.getSchoolName()));
			
			HSSFCell cell43 = row4.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(grade.getGradeName()));
			
			HSSFCell cell46 = row4.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(grade.getFx())));
			
			HSSFCell cell47 = row4.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(String.valueOf(grade.getRj())));
			
			HSSFCell cell48 = row4.createCell(j++);
			cell48.setCellStyle(style2);
			cell48.setCellValue(new HSSFRichTextString(String.valueOf(grade.getXqnzr())));
			
			HSSFCell cell49 = row4.createCell(j++);
			cell49.setCellStyle(style2);
			cell49.setCellValue(new HSSFRichTextString(String.valueOf(grade.getSnzr())));
			
			HSSFCell cell410 = row4.createCell(j++);
			cell410.setCellStyle(style2);
			cell410.setCellValue(new HSSFRichTextString(String.valueOf(grade.getSwzr())));
			
			HSSFCell cell411 = row4.createCell(j++);
			cell411.setCellStyle(style2);
			cell411.setCellValue(new HSSFRichTextString(String.valueOf(grade.getHkxb())));
			
			HSSFCell cell412 = row4.createCell(j++);
			cell412.setCellStyle(style2);
			cell412.setCellValue(new HSSFRichTextString(String.valueOf(grade.getYnjbb())));
			
			HSSFCell cell413 = row4.createCell(j++);
			cell413.setCellStyle(style2);
			cell413.setCellValue(new HSSFRichTextString(String.valueOf(grade.getQtzj())));
			
			HSSFCell cell414 = row4.createCell(j++);
			cell414.setCellStyle(style2);
			cell414.setCellValue(new HSSFRichTextString(String.valueOf(grade.getXx())));
			
			HSSFCell cell415 = row4.createCell(j++);
			cell415.setCellStyle(style2);
			cell415.setCellValue(new HSSFRichTextString(String.valueOf(grade.getCj())));
			
			HSSFCell cell416 = row4.createCell(j++);
			cell416.setCellStyle(style2);
			cell416.setCellValue(new HSSFRichTextString(String.valueOf(grade.getZwxqn())));
			
			HSSFCell cell417 = row4.createCell(j++);
			cell417.setCellStyle(style2);
			cell417.setCellValue(new HSSFRichTextString(String.valueOf(grade.getZwsn())));
			
			HSSFCell cell418 = row4.createCell(j++);
			cell418.setCellStyle(style2);
			cell418.setCellValue(new HSSFRichTextString(String.valueOf(grade.getZwsw())));
			
			HSSFCell cell419 = row4.createCell(j++);
			cell419.setCellStyle(style2);
			cell419.setCellValue(new HSSFRichTextString(String.valueOf(grade.getSw())));
			
			HSSFCell cell420 = row4.createCell(j++);
			cell420.setCellStyle(style2);
			cell420.setCellValue(new HSSFRichTextString(String.valueOf(grade.getQtjs())));
		}
		
		ExportUtils.outputData(workbook, acadyear+"学年第"+semester+"学期"+unit.getUnitName()+"异动按年级统计表", response);
	}
	
	
	//全市小学
	@RequestMapping("/cityPrimary")
	public String cityPrimary(String acadyear, String semester, ModelMap map){	
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		Semester se;
		se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(null==se){
			se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		}
		if(StringUtils.isBlank(acadyear)){		
			acadyear = se.getAcadyear();
		}
		if(StringUtils.isBlank(semester)){			
			semester = String.valueOf(se.getSemester());
		}	
		List<UnitCountDto> dtoList = unitPrimaryCountDtoList(acadyear, semester);	
		map.put("dtoList", dtoList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		return "/newstusys/abnormalreport/cityPrimary.ftl";
	}
	
	public List<UnitCountDto> unitPrimaryCountDtoList(String acadyear, String semester){
		List<UnitCountDto> dtoList = new ArrayList<UnitCountDto>();
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);
		List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});	
		Set<String> punitIdSet = new HashSet<String>();
		Set<String> unitIdSet = new HashSet<String>();
		Map<String, String> pUnitIdMap = new HashMap<String, String>();
		for(Unit u : unitList){
			punitIdSet.add(u.getParentId());
			unitIdSet.add(u.getId());
			pUnitIdMap.put(u.getId(), u.getParentId());
		}
		Map<String, String> pUnitNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(punitIdSet)){
			List<Unit> punitList = SUtils.dt(unitRemoteService.findListByIds(punitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});	
			for(Unit u : punitList){
				pUnitNameMap.put(u.getId(), u.getUnitName());
			}
		}
		List<School> schList = new ArrayList<School>();
		Set<String> unitIdSet2 = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(unitIdSet)){
			List<School> schoolList = SUtils.dt(schoolRemoteService.findListByIds(unitIdSet.toArray(new String[0])), new TR<List<School>>(){});
			for(School sch : schoolList){
				if(sch.getSections().contains("1")){
					schList.add(sch);
					unitIdSet2.add(sch.getId());
				}
			}
		}
		Map<String, Integer> fxCountMap = new HashMap<String, Integer>();//复学
		Map<String, Integer> rjCountMap = new HashMap<String, Integer>();//入境			
		Map<String, Integer> xqnzrCountMap = new HashMap<String, Integer>();//县区内转入
		Map<String, Integer> snzrCountMap = new HashMap<String, Integer>();//省内转入
		Map<String, Integer> swzrCountMap = new HashMap<String, Integer>();//省外转入
		Map<String, Integer> hkxbCountMap = new HashMap<String, Integer>();//户口新报
		Map<String, Integer> ynjbbCountMap = new HashMap<String, Integer>();//一年级补报
		Map<String, Integer> qtzjCountMap = new HashMap<String, Integer>();//其他增加
		Map<String, Integer> xxCountMap = new HashMap<String, Integer>();//休学
		Map<String, Integer> cjCountMap = new HashMap<String, Integer>();//出境
		Map<String, Integer> zwxqnCountMap = new HashMap<String, Integer>();//转往县区内
		Map<String, Integer> zwsnCountMap = new HashMap<String, Integer>();//转往省内	
		Map<String, Integer> zwswCountMap = new HashMap<String, Integer>();//转往省外
		Map<String, Integer> swCountMap = new HashMap<String, Integer>();//死亡
		Map<String, Integer> qtjsCountMap = new HashMap<String, Integer>();//其他减少
		if(CollectionUtils.isNotEmpty(unitIdSet2)){
			fxCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_FX, unitIdSet2.toArray(new String[0]));
			rjCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_RJ, unitIdSet2.toArray(new String[0]));
			xqnzrCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_XQNZR, unitIdSet2.toArray(new String[0]));
			snzrCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_SNZR, unitIdSet2.toArray(new String[0]));
			swzrCountMap =studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_SWZR, unitIdSet2.toArray(new String[0]));
			hkxbCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_HKXB, unitIdSet2.toArray(new String[0]));
			ynjbbCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_YNJBB, unitIdSet2.toArray(new String[0]));
			qtzjCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_QTZJ, unitIdSet2.toArray(new String[0]));
			xxCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_XX, unitIdSet2.toArray(new String[0]));
			cjCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_CJ, unitIdSet2.toArray(new String[0]));
			zwxqnCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_ZWXQN, unitIdSet2.toArray(new String[0]));
			zwsnCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_ZWSN, unitIdSet2.toArray(new String[0]));
			zwswCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_ZWSW, unitIdSet2.toArray(new String[0]));
			swCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_SW, unitIdSet2.toArray(new String[0]));
			qtjsCountMap = studentReportService.ydBySchIdPrimary(acadyear, semester, BaseStudentConstants.YD_QTJS, unitIdSet2.toArray(new String[0]));
		}
		int fx=0;//复学
		int rj=0;//入境
		int xqnzr=0;//县区内转入
		int snzr=0;//省内转入
		int swzr=0;//省外转入
		int hkxb=0;//户口新报
		int ynjbb=0;//一年级补报
		int qtzj=0;//其他增加	
		int xx=0;//休学
		int cj=0;//出境
		int zwxqn=0;//转往县区内
		int zwsn=0;//转往省内
		int zwsw=0;//转往省外
		int sw=0;//死亡	
		int qtjs=0;//其他减少
		for(School u : schList){
			UnitCountDto dto = new UnitCountDto();
			if(null!=pUnitNameMap.get(pUnitIdMap.get(u.getId()))){
				dto.setAddress(pUnitNameMap.get(pUnitIdMap.get(u.getId())));
			}else{
				dto.setAddress("");
			}
			dto.setSchName(u.getSchoolName());
			dto.setSectionName("小学");
			if(null!=fxCountMap.get(u.getId())){	
				fx = fx + fxCountMap.get(u.getId());
				dto.setFx(fxCountMap.get(u.getId()));
			}
			if(null!=rjCountMap.get(u.getId())){
				rj = rj + rjCountMap.get(u.getId());
				dto.setRj(rjCountMap.get(u.getId()));
			}
			if(null!=xqnzrCountMap.get(u.getId())){
				xqnzr = xqnzr + xqnzrCountMap.get(u.getId());
				dto.setXqnzr(xqnzrCountMap.get(u.getId()));
			}
			if(null!=snzrCountMap.get(u.getId())){	
				snzr = snzr + snzrCountMap.get(u.getId());
				dto.setSnzr(snzrCountMap.get(u.getId()));
			}
			if(null!=swzrCountMap.get(u.getId())){	
				swzr = swzr + swzrCountMap.get(u.getId());
				dto.setSwzr(swzrCountMap.get(u.getId()));
			}
			if(null!=hkxbCountMap.get(u.getId())){	
				hkxb = hkxb + hkxbCountMap.get(u.getId());
				dto.setHkxb(hkxbCountMap.get(u.getId()));
			}
			if(null!=ynjbbCountMap.get(u.getId())){		
				ynjbb = ynjbb + ynjbbCountMap.get(u.getId());
				dto.setYnjbb(ynjbbCountMap.get(u.getId()));
			}
			if(null!=qtzjCountMap.get(u.getId())){	
				qtzj = qtzj + qtzjCountMap.get(u.getId());
				dto.setQtzj(qtzjCountMap.get(u.getId()));
			}
			if(null!=xxCountMap.get(u.getId())){	
				xx = xx + xxCountMap.get(u.getId());
				dto.setXx(xxCountMap.get(u.getId()));
			}
			if(null!=cjCountMap.get(u.getId())){	
				cj = cj + cjCountMap.get(u.getId());
				dto.setCj(cjCountMap.get(u.getId()));
			}
			if(null!=zwxqnCountMap.get(u.getId())){	
				zwxqn = zwxqn + zwxqnCountMap.get(u.getId());
				dto.setZwxqn(zwxqnCountMap.get(u.getId()));
			}
			if(null!=zwsnCountMap.get(u.getId())){	
				zwsn = zwsn + zwsnCountMap.get(u.getId());
				dto.setZwsn(zwsnCountMap.get(u.getId()));
			}
			if(null!=zwswCountMap.get(u.getId())){	
				zwsw = zwsw + zwswCountMap.get(u.getId());
				dto.setZwsw(zwswCountMap.get(u.getId()));
			}
			if(null!=swCountMap.get(u.getId())){	
				sw = sw + swCountMap.get(u.getId());
				dto.setSw(swCountMap.get(u.getId()));
			}
			if(null!=qtjsCountMap.get(u.getId())){	
				qtjs = qtjs + qtjsCountMap.get(u.getId());
				dto.setQtjs(qtjsCountMap.get(u.getId()));
			}
			dtoList.add(dto);
		}		
		
		Collections.sort(dtoList,new Comparator<UnitCountDto>() {
            @Override
            public int compare(UnitCountDto o1, UnitCountDto o2) {
                String street1 = o1.getAddress();
                String street2 = o2.getAddress();
                if(!StringUtils.equals(street1, street2)) {// 教育局
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                street1 = o1.getSchName();
                street2 = o2.getSchName();
                if(!StringUtils.equals(street1, street2)) {// 学校
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                return StringUtils.trimToEmpty(street1).compareTo(street2);
            }
        });
		if(CollectionUtils.isNotEmpty(dtoList)){			
			UnitCountDto dto = new UnitCountDto();
			dto.setSchName("小计");
			dto.setFx(fx);
			dto.setRj(rj);
			dto.setXqnzr(xqnzr);
			dto.setSnzr(snzr);
			dto.setSwzr(swzr);
			dto.setHkxb(hkxb);
			dto.setYnjbb(ynjbb);
			dto.setQtzj(qtzj);
			dto.setXx(xx);
			dto.setCj(cj);
			dto.setZwxqn(zwxqn);			
			dto.setZwsn(zwsn);
			dto.setZwsw(zwsw);
			dto.setSw(sw);
			dto.setQtjs(qtjs);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	@RequestMapping("/cityPrimaryExport")
	public void cityPrimaryExport(String acadyear, String semester,HttpServletResponse response){
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(acadyear+"学年第"+semester+"学期全市小学异动统计表");
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 16);// 字体大小
		headfont.setBold(true);// 加粗
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFFont headfont1 = workbook.createFont();
		headfont1.setFontHeightInPoints((short) 12);// 字体大小
		headfont1.setBold(true);// 加粗
		style1.setFont(headfont1);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setVerticalAlignment(VerticalAlignment.CENTER);
		
		String[] titles = new String[] { "所在镇乡（街道）", "校点名称", "","复 学", "入 境", "县区内转入", "省内转入	", "省外转入", "户口新报", "一年级补报",
				"其他增加", "休 学", "出 境", "转往县区内", "转往省内", "转往省外", "死 亡", "其他减少" };
		//第一行
		HSSFRow row1 = sheet.createRow(0);
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,titles.length-1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString(acadyear+"学年第"+semester+"学期全市小学异动统计表"));
		
		HSSFRow row2 = sheet.createRow(1);
		CellRangeAddress car21 = new CellRangeAddress(1,1,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
	    sheet.addMergedRegion(car21);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellStyle(style1);
		cell21.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car22 = new CellRangeAddress(1,2,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
	    sheet.addMergedRegion(car22);
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellStyle(style1);
		cell22.setCellValue(new HSSFRichTextString("学段"));
		
		CellRangeAddress car25 = new CellRangeAddress(1,1,3,10);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car25, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car25, sheet); 
	    sheet.addMergedRegion(car25);
		HSSFCell cell25 = row2.createCell(3);
		cell25.setCellStyle(style1);
		cell25.setCellValue(new HSSFRichTextString("转入学生"));
		
		CellRangeAddress car26 = new CellRangeAddress(1,1,11,17);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car26, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car26, sheet); 
	    sheet.addMergedRegion(car26);
		HSSFCell cell26 = row2.createCell(11);
		cell26.setCellStyle(style1);
		cell26.setCellValue(new HSSFRichTextString("转出学生"));
		
		//第3行
		HSSFRow row3 = sheet.createRow(2);
		for (int i = 0; i < titles.length; i++) {
			if (i < 2) {
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			HSSFCell cell31 = row3.createCell(i);
			cell31.setCellStyle(style1);
			cell31.setCellValue(new HSSFRichTextString(titles[i]));
		}
		
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFFont headfont2 = workbook.createFont();
		headfont2.setFontHeightInPoints((short) 12);// 字体大小
		style2.setFont(headfont2);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		style2.setWrapText(true);
		List<UnitCountDto> dtoList = unitPrimaryCountDtoList(acadyear, semester);
		int i=3;
		for(UnitCountDto u : dtoList){
			HSSFRow row4 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell41 = row4.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(u.getAddress()));
			
			HSSFCell cell42 = row4.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(u.getSchName()));
			
			HSSFCell cell43 = row4.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(u.getSectionName()));
			
			HSSFCell cell46 = row4.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(u.getFx())));
			
			HSSFCell cell47 = row4.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(String.valueOf(u.getRj())));
			
			HSSFCell cell48 = row4.createCell(j++);
			cell48.setCellStyle(style2);
			cell48.setCellValue(new HSSFRichTextString(String.valueOf(u.getXqnzr())));
			
			HSSFCell cell49 = row4.createCell(j++);
			cell49.setCellStyle(style2);
			cell49.setCellValue(new HSSFRichTextString(String.valueOf(u.getSnzr())));
			
			HSSFCell cell410 = row4.createCell(j++);
			cell410.setCellStyle(style2);
			cell410.setCellValue(new HSSFRichTextString(String.valueOf(u.getSwzr())));
			
			HSSFCell cell411 = row4.createCell(j++);
			cell411.setCellStyle(style2);
			cell411.setCellValue(new HSSFRichTextString(String.valueOf(u.getHkxb())));
			
			HSSFCell cell412 = row4.createCell(j++);
			cell412.setCellStyle(style2);
			cell412.setCellValue(new HSSFRichTextString(String.valueOf(u.getYnjbb())));
			
			HSSFCell cell413 = row4.createCell(j++);
			cell413.setCellStyle(style2);
			cell413.setCellValue(new HSSFRichTextString(String.valueOf(u.getQtzj())));
			
			HSSFCell cell414 = row4.createCell(j++);
			cell414.setCellStyle(style2);
			cell414.setCellValue(new HSSFRichTextString(String.valueOf(u.getXx())));
			
			HSSFCell cell415 = row4.createCell(j++);
			cell415.setCellStyle(style2);
			cell415.setCellValue(new HSSFRichTextString(String.valueOf(u.getCj())));
			
			HSSFCell cell416 = row4.createCell(j++);
			cell416.setCellStyle(style2);
			cell416.setCellValue(new HSSFRichTextString(String.valueOf(u.getZwxqn())));
			
			HSSFCell cell417 = row4.createCell(j++);
			cell417.setCellStyle(style2);
			cell417.setCellValue(new HSSFRichTextString(String.valueOf(u.getZwsn())));
			
			HSSFCell cell418 = row4.createCell(j++);
			cell418.setCellStyle(style2);
			cell418.setCellValue(new HSSFRichTextString(String.valueOf(u.getZwsw())));
			
			HSSFCell cell419 = row4.createCell(j++);
			cell419.setCellStyle(style2);
			cell419.setCellValue(new HSSFRichTextString(String.valueOf(u.getSw())));
			
			HSSFCell cell420 = row4.createCell(j++);
			cell420.setCellStyle(style2);
			cell420.setCellValue(new HSSFRichTextString(String.valueOf(u.getQtjs())));
		}		
		ExportUtils.outputData(workbook, acadyear+"学年第"+semester+"学期全市小学异动统计表", response);
	}
	
	//全市初高
	@RequestMapping("/cityHign")
	public String cityHign(String acadyear, String semester, HttpServletRequest req, ModelMap map){
		section = NumberUtils.toInt(req.getParameter("section"));
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		Semester se;
		se = SUtils.dc(semesterRemoteService.getCurrentSemester(0,getLoginInfo().getUnitId()),Semester.class);
		if(null==se){
			se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		}
		if(StringUtils.isBlank(acadyear)){		
			acadyear = se.getAcadyear();
		}
		if(StringUtils.isBlank(semester)){			
			semester = String.valueOf(se.getSemester());
		}	
		List<GradeCountDto> dtoList = unitHighCountDtoList(acadyear, semester);
		map.put("dtoList", dtoList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("section", section);
		return "/newstusys/abnormalreport/cityHign.ftl";
	}
	
	public List<GradeCountDto> unitHighCountDtoList(String acadyear, String semester){
		List<GradeCountDto> dtoList = new ArrayList<GradeCountDto>();
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);
		List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});	
		Set<String> punitIdSet = new HashSet<String>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		Map<String, String> punitIdMap = new HashMap<String, String>();
		Set<String> unitIdSet = new HashSet<String>();
		for(Unit u : unitList){
			punitIdSet.add(u.getParentId());
			unitNameMap.put(u.getId(), u.getUnitName());
			unitIdSet.add(u.getId());
			punitIdMap.put(u.getId(), u.getParentId());
		}
		Map<String, String> pUnitNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(punitIdSet)){
			List<Unit> punitList = SUtils.dt(unitRemoteService.findListByIds(punitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});	
			for(Unit u : punitList){
				pUnitNameMap.put(u.getId(), u.getUnitName());
			}
		}		
		int fx=0;//复学
		int rj=0;//入境
		int xqnzr=0;//县区内转入
		int snzr=0;//省内转入
		int swzr=0;//省外转入
		int hkxb=0;//户口新报
		int ynjbb=0;//一年级补报
		int qtzj=0;//其他增加	
		int xx=0;//休学
		int cj=0;//出境
		int zwxqn=0;//转往县区内
		int zwsn=0;//转往省内
		int zwsw=0;//转往省外
		int sw=0;//死亡	
		int qtjs=0;//其他减少
		if(CollectionUtils.isNotEmpty(unitIdSet)){
			Map<String, List<Grade>> gradeMap = SUtils.dt(gradeRemoteService.findBySchoolIdMap(unitIdSet.toArray(new String[0])), new TypeReference<Map<String, List<Grade>>>(){});
			Set<String> gradeIdSet = new HashSet<String>();
			for(String key : gradeMap.keySet()){
		    	List<Grade> gradeList = gradeMap.get(key);
		    	for(Grade grade : gradeList){
		    		if(grade.getSection()==section){		    			
		    			gradeIdSet.add(grade.getId());
		    		}
		    	}
		    }
			Map<String, Integer> fxCountMap = new HashMap<String, Integer>();//复学
			Map<String, Integer> rjCountMap = new HashMap<String, Integer>();//入境			
			Map<String, Integer> xqnzrCountMap = new HashMap<String, Integer>();//县区内转入
			Map<String, Integer> snzrCountMap = new HashMap<String, Integer>();//省内转入
			Map<String, Integer> swzrCountMap = new HashMap<String, Integer>();//省外转入
			Map<String, Integer> hkxbCountMap = new HashMap<String, Integer>();//户口新报
			Map<String, Integer> ynjbbCountMap = new HashMap<String, Integer>();//一年级补报
			Map<String, Integer> qtzjCountMap = new HashMap<String, Integer>();//其他增加
			Map<String, Integer> xxCountMap = new HashMap<String, Integer>();//休学
			Map<String, Integer> cjCountMap = new HashMap<String, Integer>();//出境
			Map<String, Integer> zwxqnCountMap = new HashMap<String, Integer>();//转往县区内
			Map<String, Integer> zwsnCountMap = new HashMap<String, Integer>();//转往省内	
			Map<String, Integer> zwswCountMap = new HashMap<String, Integer>();//转往省外
			Map<String, Integer> swCountMap = new HashMap<String, Integer>();//死亡
			Map<String, Integer> qtjsCountMap = new HashMap<String, Integer>();//其他减少
			if(CollectionUtils.isNotEmpty(gradeIdSet)){
//				fxCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_FX, gradeIdSet.toArray(new String[0]));
//				rjCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_RJ, gradeIdSet.toArray(new String[0]));
//				xqnzrCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_XQNZR, gradeIdSet.toArray(new String[0]));
//				snzrCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_SNZR, gradeIdSet.toArray(new String[0]));
//				swzrCountMap =studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_SWZR, gradeIdSet.toArray(new String[0]));
//				hkxbCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_HKXB, gradeIdSet.toArray(new String[0]));
//				ynjbbCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_YNJBB, gradeIdSet.toArray(new String[0]));
//				qtzjCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_QTZJ, gradeIdSet.toArray(new String[0]));
//				xxCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_XX, gradeIdSet.toArray(new String[0]));
//				cjCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_CJ, gradeIdSet.toArray(new String[0]));
//				zwxqnCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZWXQN, gradeIdSet.toArray(new String[0]));
//				zwsnCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZWSN, gradeIdSet.toArray(new String[0]));
//				zwswCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_ZWSW, gradeIdSet.toArray(new String[0]));
//				swCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_SW, gradeIdSet.toArray(new String[0]));
//				qtjsCountMap = studentReportService.ydByGradeId(acadyear, semester, BaseStudentConstants.YD_QTJS, gradeIdSet.toArray(new String[0]));
			}
			for(String key : gradeMap.keySet()){
		    	List<Grade> gradeList = gradeMap.get(key);
		    	for(Grade grade : gradeList){
		    		if(grade.getSection()==section){		    			
		    			GradeCountDto dto = new GradeCountDto();
		    			dto.setAddress(pUnitNameMap.get(punitIdMap.get(key)));
		    			dto.setSchoolName(unitNameMap.get(key));
		    			dto.setGradeName(grade.getGradeName());
		    			dto.setGradeCode(grade.getGradeCode());
		    			if(null!=fxCountMap.get(grade.getId())){	
		    				fx = fx + fxCountMap.get(grade.getId());
		    				dto.setFx(fxCountMap.get(grade.getId()));
		    			}
		    			if(null!=rjCountMap.get(grade.getId())){
		    				rj = rj + rjCountMap.get(grade.getId());
		    				dto.setRj(rjCountMap.get(grade.getId()));
		    			}
		    			if(null!=xqnzrCountMap.get(grade.getId())){
		    				xqnzr = xqnzr + xqnzrCountMap.get(grade.getId());
		    				dto.setXqnzr(xqnzrCountMap.get(grade.getId()));
		    			}
		    			if(null!=snzrCountMap.get(grade.getId())){	
		    				snzr = snzr + snzrCountMap.get(grade.getId());
		    				dto.setSnzr(snzrCountMap.get(grade.getId()));
		    			}
		    			if(null!=swzrCountMap.get(grade.getId())){	
		    				swzr = swzr + swzrCountMap.get(grade.getId());
		    				dto.setSwzr(swzrCountMap.get(grade.getId()));
		    			}
		    			if(null!=hkxbCountMap.get(grade.getId())){	
		    				hkxb = hkxb + hkxbCountMap.get(grade.getId());
		    				dto.setHkxb(hkxbCountMap.get(grade.getId()));
		    			}
		    			if(null!=ynjbbCountMap.get(grade.getId())){		
		    				ynjbb = ynjbb + ynjbbCountMap.get(grade.getId());
		    				dto.setYnjbb(ynjbbCountMap.get(grade.getId()));
		    			}
		    			if(null!=qtzjCountMap.get(grade.getId())){	
		    				qtzj = qtzj + qtzjCountMap.get(grade.getId());
		    				dto.setQtzj(qtzjCountMap.get(grade.getId()));
		    			}
		    			if(null!=xxCountMap.get(grade.getId())){	
		    				xx = xx + xxCountMap.get(grade.getId());
		    				dto.setXx(xxCountMap.get(grade.getId()));
		    			}
		    			if(null!=cjCountMap.get(grade.getId())){	
		    				cj = cj + cjCountMap.get(grade.getId());
		    				dto.setCj(cjCountMap.get(grade.getId()));
		    			}
		    			if(null!=zwxqnCountMap.get(grade.getId())){	
		    				zwxqn = zwxqn + zwxqnCountMap.get(grade.getId());
		    				dto.setZwxqn(zwxqnCountMap.get(grade.getId()));
		    			}
		    			if(null!=zwsnCountMap.get(grade.getId())){	
		    				zwsn = zwsn + zwsnCountMap.get(grade.getId());
		    				dto.setZwsn(zwsnCountMap.get(grade.getId()));
		    			}
		    			if(null!=zwswCountMap.get(grade.getId())){	
		    				zwsw = zwsw + zwswCountMap.get(grade.getId());
		    				dto.setZwsw(zwswCountMap.get(grade.getId()));
		    			}
		    			if(null!=swCountMap.get(grade.getId())){	
		    				sw = sw + swCountMap.get(grade.getId());
		    				dto.setSw(swCountMap.get(grade.getId()));
		    			}
		    			if(null!=qtjsCountMap.get(grade.getId())){	
		    				qtjs = qtjs + qtjsCountMap.get(grade.getId());
		    				dto.setQtjs(qtjsCountMap.get(grade.getId()));
		    			}
		    			dtoList.add(dto);
		    		}
		    	}
		    	
		    }
			
		}		
		Collections.sort(dtoList,new Comparator<GradeCountDto>() {
            @Override
            public int compare(GradeCountDto o1, GradeCountDto o2) {
                String street1 = o1.getAddress();
                String street2 = o2.getAddress();
                if(!StringUtils.equals(street1, street2)) {// 教育局
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                street1 = o1.getSchoolName();
                street2 = o2.getSchoolName();
                if(!StringUtils.equals(street1, street2)) {// 学校
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                street1 = o1.getGradeCode();
                street2 = o2.getGradeCode();
                if(!StringUtils.equals(street1, street2)) {// 年级
                	return StringUtils.trimToEmpty(street1).compareTo(street2);
                }
                return StringUtils.trimToEmpty(street1).compareTo(street2);
            }
        });
		if(CollectionUtils.isNotEmpty(dtoList)){			
			GradeCountDto dto = new GradeCountDto();
			dto.setSchoolName("小计");
			dto.setFx(fx);
			dto.setRj(rj);
			dto.setXqnzr(xqnzr);
			dto.setSnzr(snzr);
			dto.setSwzr(swzr);
			dto.setHkxb(hkxb);
			dto.setYnjbb(ynjbb);
			dto.setQtzj(qtzj);
			dto.setXx(xx);
			dto.setCj(cj);
			dto.setZwxqn(zwxqn);			
			dto.setZwsn(zwsn);
			dto.setZwsw(zwsw);
			dto.setSw(sw);
			dto.setQtjs(qtjs);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	@RequestMapping("/cityHignExport")
	public void cityHignExport(String acadyear, String semester, HttpServletRequest req, HttpServletResponse response){
		section = NumberUtils.toInt(req.getParameter("section"));
		HSSFWorkbook workbook = new HSSFWorkbook();
		String exName = acadyear+"学年第"+semester+"学期全市"+(section==2?"初中":"高中")+"异动统计表";
		HSSFSheet sheet = workbook.createSheet(exName);
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 16);// 字体大小
		headfont.setBold(true);// 加粗
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFFont headfont1 = workbook.createFont();
		headfont1.setFontHeightInPoints((short) 12);// 字体大小
		headfont1.setBold(true);// 加粗
		style1.setFont(headfont1);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setVerticalAlignment(VerticalAlignment.CENTER);
		
		String[] titles = new String[] { "所在镇乡（街道）", "校点名称", "","复 学", "入 境", "县区内转入", "省内转入	", "省外转入", "户口新报", "一年级补报",
				"其他增加", "休 学", "出 境", "转往县区内", "转往省内", "转往省外", "死 亡", "其他减少" };
		//第一行
		HSSFRow row1 = sheet.createRow(0);
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,titles.length - 1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString(exName));
		
		//第2行
		HSSFRow row2 = sheet.createRow(1);
		CellRangeAddress car21 = new CellRangeAddress(1,1,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
	    sheet.addMergedRegion(car21);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellStyle(style1);
		cell21.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car22 = new CellRangeAddress(1,2,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
	    sheet.addMergedRegion(car22);
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellStyle(style1);
		cell22.setCellValue(new HSSFRichTextString("年级"));
		
		CellRangeAddress car25 = new CellRangeAddress(1,1,3,10);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car25, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car25, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car25, sheet); 
	    sheet.addMergedRegion(car25);
		HSSFCell cell25 = row2.createCell(3);
		cell25.setCellStyle(style1);
		cell25.setCellValue(new HSSFRichTextString("转入学生"));
		
		CellRangeAddress car26 = new CellRangeAddress(1,1,11,17);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car26, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car26, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car26, sheet); 
	    sheet.addMergedRegion(car26);
		HSSFCell cell26 = row2.createCell(11);
		cell26.setCellStyle(style1);
		cell26.setCellValue(new HSSFRichTextString("转出学生"));
		
		//第3行
		HSSFRow row3 = sheet.createRow(2);
		for (int i = 0; i < titles.length; i++) {
			if (i < 3) {
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			HSSFCell cell31 = row3.createCell(i);
			cell31.setCellStyle(style1);
			cell31.setCellValue(new HSSFRichTextString(titles[i]));
		}
		
		// 内容行
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFFont headfont2 = workbook.createFont();
		headfont2.setFontHeightInPoints((short) 12);// 字体大小
		style2.setFont(headfont2);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		style2.setWrapText(true);
		List<GradeCountDto> dtoList = unitHighCountDtoList(acadyear, semester);
		int i=3;
		for(GradeCountDto grade : dtoList){
			HSSFRow row4 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell41 = row4.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(grade.getAddress()));
			
			HSSFCell cell42 = row4.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(grade.getSchoolName()));
			
			HSSFCell cell43 = row4.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(grade.getGradeName()));
			
			HSSFCell cell46 = row4.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(grade.getFx())));
			
			HSSFCell cell47 = row4.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(String.valueOf(grade.getRj())));
			
			HSSFCell cell48 = row4.createCell(j++);
			cell48.setCellStyle(style2);
			cell48.setCellValue(new HSSFRichTextString(String.valueOf(grade.getXqnzr())));
			
			HSSFCell cell49 = row4.createCell(j++);
			cell49.setCellStyle(style2);
			cell49.setCellValue(new HSSFRichTextString(String.valueOf(grade.getSnzr())));
			
			HSSFCell cell410 = row4.createCell(j++);
			cell410.setCellStyle(style2);
			cell410.setCellValue(new HSSFRichTextString(String.valueOf(grade.getSwzr())));
			
			HSSFCell cell411 = row4.createCell(j++);
			cell411.setCellStyle(style2);
			cell411.setCellValue(new HSSFRichTextString(String.valueOf(grade.getHkxb())));
			
			HSSFCell cell412 = row4.createCell(j++);
			cell412.setCellStyle(style2);
			cell412.setCellValue(new HSSFRichTextString(String.valueOf(grade.getYnjbb())));
			
			HSSFCell cell413 = row4.createCell(j++);
			cell413.setCellStyle(style2);
			cell413.setCellValue(new HSSFRichTextString(String.valueOf(grade.getQtzj())));
			
			HSSFCell cell414 = row4.createCell(j++);
			cell414.setCellStyle(style2);
			cell414.setCellValue(new HSSFRichTextString(String.valueOf(grade.getXx())));
			
			HSSFCell cell415 = row4.createCell(j++);
			cell415.setCellStyle(style2);
			cell415.setCellValue(new HSSFRichTextString(String.valueOf(grade.getCj())));
			
			HSSFCell cell416 = row4.createCell(j++);
			cell416.setCellStyle(style2);
			cell416.setCellValue(new HSSFRichTextString(String.valueOf(grade.getZwxqn())));
			
			HSSFCell cell417 = row4.createCell(j++);
			cell417.setCellStyle(style2);
			cell417.setCellValue(new HSSFRichTextString(String.valueOf(grade.getZwsn())));
			
			HSSFCell cell418 = row4.createCell(j++);
			cell418.setCellStyle(style2);
			cell418.setCellValue(new HSSFRichTextString(String.valueOf(grade.getZwsw())));
			
			HSSFCell cell419 = row4.createCell(j++);
			cell419.setCellStyle(style2);
			cell419.setCellValue(new HSSFRichTextString(String.valueOf(grade.getSw())));
			
			HSSFCell cell420 = row4.createCell(j++);
			cell420.setCellStyle(style2);
			cell420.setCellValue(new HSSFRichTextString(String.valueOf(grade.getQtjs())));
		}
		
		ExportUtils.outputData(workbook, exName, response);
	}
	
	@RequestMapping("/normalFlowRecord")
	public String normalFlowRecord(HttpServletRequest request,String state, String flowType, String unitId,ModelMap map){
		Pagination page = createPagination();
		getFlowTypes();
		deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deploy", deploy);
		List<StudentAbnormalFlow> dtoList = abnormalFlowList(state, flowType, unitId, page);
		map.put("dtoList", dtoList);
		sendPagination(request, map, page);
		map.put("unitId", unitId);
		map.put("state", state);
		map.put("flowType", flowType);
		map.put("leaveFlowTypes", leaveFlowTypes);
		map.put("enterFlowTypes", enterFlowTypes);
		return "/newstusys/abnormalreport/normalFlowRecord.ftl";
	}
	
	public List<StudentAbnormalFlow> abnormalFlowList(String state, String flowType, String unitId,Pagination page){
		Set<String> zrSet = EntityUtils.getSet(enterFlowTypes, McodeDetail::getThisId);
//		zrSet.add("12");
//		zrSet.add("07");
//		zrSet.add("08");
//		zrSet.add("26");
//		zrSet.add("27");
//		zrSet.add("28");
//		zrSet.add("29");
//		zrSet.add("89");
		
		Set<String> zcSet = EntityUtils.getSet(leaveFlowTypes, McodeDetail::getThisId);
//		zcSet.add("11");
//		zcSet.add("35");
//		zcSet.add("36");
//		zcSet.add("37");
//		zcSet.add("38");
//		zcSet.add("51");
//		zcSet.add("99");
		
		String[] flowTypes;
		if(StringUtils.isBlank(state) && StringUtils.isBlank(flowType)){
			flowTypes = zrSet.toArray(new String[0]);
			flowTypes = (String[]) ArrayUtils.addAll(flowTypes, zcSet.toArray(new String[0]));
//			flowTypes = new String[]{BaseStudentConstants.YD_FX, BaseStudentConstants.YD_RJ, 
//					BaseStudentConstants.YD_XQNZR, BaseStudentConstants.YD_SNZR, BaseStudentConstants.YD_SWZR, 
//					BaseStudentConstants.YD_HKXB, BaseStudentConstants.YD_YNJBB, BaseStudentConstants.YD_QTZJ,
//					BaseStudentConstants.YD_XX, BaseStudentConstants.YD_CJ, BaseStudentConstants.YD_ZWXQN, 
//					BaseStudentConstants.YD_ZWSN, BaseStudentConstants.YD_ZWSW, 
//					BaseStudentConstants.YD_SW, BaseStudentConstants.YD_QTJS};
		}else if(StringUtils.isNotBlank(state) && StringUtils.isBlank(flowType)){
			if("1".equals(state)){
				flowTypes = zrSet.toArray(new String[0]);
//				flowTypes = new String[]{BaseStudentConstants.YD_FX, BaseStudentConstants.YD_RJ, 
//						BaseStudentConstants.YD_XQNZR, BaseStudentConstants.YD_SNZR, BaseStudentConstants.YD_SWZR, 
//						BaseStudentConstants.YD_HKXB, BaseStudentConstants.YD_YNJBB, BaseStudentConstants.YD_QTZJ,
//						};
			}else{
				flowTypes = zcSet.toArray(new String[0]);
//				flowTypes = new String[]{
//						BaseStudentConstants.YD_XX, BaseStudentConstants.YD_CJ, BaseStudentConstants.YD_ZWXQN, BaseStudentConstants.YD_ZWSN, BaseStudentConstants.YD_ZWSW, BaseStudentConstants.YD_SW, BaseStudentConstants.YD_QTJS};
			}
		}else if(StringUtils.isBlank(state) && StringUtils.isNotBlank(flowType)){
			flowTypes = new String[]{flowType};
		}else if(StringUtils.isNotBlank(state) && StringUtils.isNotBlank(flowType)){
			if("1".equals(state) && zcSet.contains(flowType)){
				flowTypes = zrSet.toArray(new String[0]);
//				flowTypes = new String[]{BaseStudentConstants.YD_FX, BaseStudentConstants.YD_RJ, 
//						BaseStudentConstants.YD_XQNZR, BaseStudentConstants.YD_SNZR, BaseStudentConstants.YD_SWZR, 
//						BaseStudentConstants.YD_HKXB, BaseStudentConstants.YD_YNJBB, BaseStudentConstants.YD_QTZJ};
			}else if("2".equals(state) && zrSet.contains(flowType)){
				flowTypes = zcSet.toArray(new String[0]);
//				flowTypes = new String[]{
//						BaseStudentConstants.YD_XX, BaseStudentConstants.YD_CJ, BaseStudentConstants.YD_ZWXQN, BaseStudentConstants.YD_ZWSN, BaseStudentConstants.YD_ZWSW, BaseStudentConstants.YD_SW, BaseStudentConstants.YD_QTJS};
			}else{
				flowTypes = new String[]{flowType};
			}
		}else{
			flowTypes = zrSet.toArray(new String[0]);
			flowTypes = (String[]) ArrayUtils.addAll(flowTypes, zcSet.toArray(new String[0]));
//			flowTypes = new String[]{BaseStudentConstants.YD_FX, BaseStudentConstants.YD_RJ, BaseStudentConstants.YD_XQNZR, BaseStudentConstants.YD_SNZR, BaseStudentConstants.YD_SWZR, BaseStudentConstants.YD_HKXB, BaseStudentConstants.YD_YNJBB, BaseStudentConstants.YD_QTZJ,
//					BaseStudentConstants.YD_XX, BaseStudentConstants.YD_CJ, BaseStudentConstants.YD_ZWXQN, BaseStudentConstants.YD_ZWSN, BaseStudentConstants.YD_ZWSW, BaseStudentConstants.YD_SW, BaseStudentConstants.YD_QTJS};
		}
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		List<String> schIds = new ArrayList<>();
		List<Unit> schs;
		if(unit.getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
			schs = new ArrayList<>();
			schs.add(unit);
		} else {
			schs = Unit.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL));
		}
		if(CollectionUtils.isEmpty(schs)) {
			return new ArrayList<>();
		}
		schIds = EntityUtils.getList(schs, Unit::getId);
		Set<String> pids = EntityUtils.getSet(schs, Unit::getParentId);
		Map<String, Unit> unitMap = EntityUtils.getMap(schs, Unit::getId);
		pids.removeAll(unitMap.keySet());
		unitMap.putAll(EntityUtils.getMap(Unit.dt(unitRemoteService.findListByIds(pids.toArray(new String[0]))), Unit::getId));
		
		List<StudentAbnormalFlow> flowList = studentAbnormalFlowService.findAbnormalFlowByFlowTypes(schIds.toArray(new String[0]), flowTypes, page);
		Set<String> stuIdSet = new HashSet<String>();
		Set<String> clsIdSet = new HashSet<String>();
		for(StudentAbnormalFlow flow : flowList){
			stuIdSet.add(flow.getStuid());
			clsIdSet.add(flow.getCurrentclassid());
		}
		Map<String, Student> stuMap = new HashMap<String, Student>();
		Map<String, Clazz> clsMap = new HashMap<String, Clazz>();
		List<Student> studentList = new ArrayList<Student>();
		List<Clazz> clsList = new ArrayList<Clazz>();
		if(CollectionUtils.isNotEmpty(stuIdSet)){
			studentList = SUtils.dt(studentRemoteService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>(){});
			for(Student stu : studentList){
				stuMap.put(stu.getId(), stu);
			}
		}
		Set<String> gradeIdSet = new HashSet<String>();
		Map<String, String> clsGradeIdMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(clsIdSet)){
			clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
			for(Clazz cls : clsList){
				clsMap.put(cls.getId(), cls);
				gradeIdSet.add(cls.getGradeId());
				clsGradeIdMap.put(cls.getId(), cls.getGradeId());
			}
		}
		List<Grade> gradeList = new ArrayList<Grade>();
		Map<String, String> gradeMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(gradeIdSet)){
			gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIdSet.toArray(new String[0])), new TR<List<Grade>>(){});
			for(Grade grade : gradeList){
				gradeMap.put(grade.getId(), grade.getGradeName());
			}
		}
		Map<String, McodeDetail> mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-YDLB"),new TypeReference<Map<String,McodeDetail>>(){});
		for(StudentAbnormalFlow flow : flowList){			
			if(null!=flow.getStuid() && null!=stuMap.get(flow.getStuid())){
				flow.setIdCardNo(stuMap.get(flow.getStuid()).getIdentityCard());
				flow.setStudentName(stuMap.get(flow.getStuid()).getStudentName());
			}
			
			if(null!=flow.getSchid() && null!=unitMap.get(flow.getSchid())){				
				flow.setSchName(unitMap.get(flow.getSchid()).getUnitName());
			}			
			if(null!=flow.getCurrentclassid() && null!=clsMap.get(flow.getCurrentclassid())){
				flow.setClassName(clsMap.get(flow.getCurrentclassid()).getClassNameDynamic());
			}
			if(null!=flow.getCurrentclassid() && null!=clsGradeIdMap.get(flow.getCurrentclassid())){				
				flow.setGradeName(gradeMap.get(clsGradeIdMap.get(flow.getCurrentclassid())));
			}
			if (mcodeDetailMap.containsKey(flow.getFlowtype())) {
				flow.setFlowreason(mcodeDetailMap.get(flow.getFlowtype())
						.getMcodeContent());
			}
			
			String enStr = "转入";
			String leStr = "转出";
			if(BaseConstants.DEPLOY_HANGWAI.equals(deploy)) {
				enStr = "学籍增加";
				leStr = "学籍减少";
			}
			if(StringUtils.isNotBlank(flow.getFlowtype())){
				if(zrSet.contains(flow.getFlowtype())){
					flow.setFlowtypeName(enStr);
				}
				if(zcSet.contains(flow.getFlowtype())){
					flow.setFlowtypeName(leStr);
				}
			}
		}
		return flowList;
	}
	
	@RequestMapping("/normalFlowRecordExport")
	public void normalFlowRecordExport(String state, String flowType, String unitId,HttpServletResponse response){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(unit.getUnitName()+"异动记录");
		
		HSSFCellStyle style1 = workbook.createCellStyle();
		HSSFFont headfont1 = workbook.createFont();
		headfont1.setFontHeightInPoints((short) 12);// 字体大小
		headfont1.setBold(true);// 加粗
		style1.setFont(headfont1);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setVerticalAlignment(VerticalAlignment.CENTER);
		
		//第一行
		HSSFRow row1 = sheet.createRow(0);
		String[] titles = new String[] {"姓名","身份证号","异动类型","转入（转出）学校","年级","班级","异动时间","异动原因","备注"};
		for(int i=0;i<titles.length;i++) {
			if (i == 1 || i == titles.length -1 ) {
				sheet.setColumnWidth(i, 30 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			String title = titles[i];
			HSSFCell cell11 = row1.createCell(i);
			cell11.setCellStyle(style1);
			cell11.setCellValue(new HSSFRichTextString(title));
		}
		
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFFont headfont2 = workbook.createFont();
		headfont2.setFontHeightInPoints((short) 12);// 字体大小
		style2.setFont(headfont2);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		style2.setWrapText(true);
		List<StudentAbnormalFlow> dtoList = abnormalFlowList(state, flowType, unitId, null);
		int i=1;
		for(StudentAbnormalFlow flow : dtoList){
			HSSFRow row2 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell21 = row2.createCell(j++);
			cell21.setCellStyle(style2);
			cell21.setCellValue(new HSSFRichTextString(flow.getStudentName()));
			
			HSSFCell cell22 = row2.createCell(j++);
			cell22.setCellStyle(style2);
			cell22.setCellValue(new HSSFRichTextString(flow.getIdCardNo()));
			
			HSSFCell cell23 = row2.createCell(j++);
			cell23.setCellStyle(style2);
			cell23.setCellValue(new HSSFRichTextString(flow.getFlowtypeName()));
			
			HSSFCell cell24 = row2.createCell(j++);
			cell24.setCellStyle(style2);
			cell24.setCellValue(new HSSFRichTextString(flow.getSchName()));
			
			HSSFCell cell26 = row2.createCell(j++);
			cell26.setCellStyle(style2);
			cell26.setCellValue(new HSSFRichTextString(flow.getGradeName()));
			
			HSSFCell cell27 = row2.createCell(j++);
			cell27.setCellStyle(style2);
			cell27.setCellValue(new HSSFRichTextString(flow.getClassName()));
			
			HSSFCell cell28 = row2.createCell(j++);
			cell28.setCellStyle(style2);
			cell28.setCellValue(new HSSFRichTextString(String.valueOf(flow.getFlowdate()).split(" ")[0]));
			
			HSSFCell cell29 = row2.createCell(j++);
			cell29.setCellStyle(style2);
			cell29.setCellValue(new HSSFRichTextString(flow.getFlowreason()));
			
			HSSFCell cell30 = row2.createCell(j++);
			cell30.setCellStyle(style2);
			cell30.setCellValue(new HSSFRichTextString(flow.getRemark()));
		}
		
		
		ExportUtils.outputData(workbook, unit.getUnitName()+"异动记录", response);
	}
}
