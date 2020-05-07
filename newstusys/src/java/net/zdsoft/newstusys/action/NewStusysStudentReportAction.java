package net.zdsoft.newstusys.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.dto.ClazzCountDto;
import net.zdsoft.newstusys.dto.GradeCountDto;
import net.zdsoft.newstusys.dto.UnitCountDto;
import net.zdsoft.newstusys.service.BaseStudentService;
import net.zdsoft.newstusys.service.StudentReportService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
@Controller
@RequestMapping("/newstusys/student/report")
public class NewStusysStudentReportAction extends BaseAction{
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentReportService studentReportService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private BaseStudentService baseStudentService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	private int section = 0;
	
	@RequestMapping("/index/page")
	public String indexPage(ModelMap map){
		Unit un = Unit.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()));
		map.put("topEdu", un.getUnitType() == Unit.UNIT_EDU_TOP);
		String deploy = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_DEPLOY_SCHOOL);
		map.put("deploy", deploy);
		return "/newstusys/edu/student/report/reportAdmin.ftl";
	}
	
	//花名册
	@RequestMapping("/studentRoster")
	public String roster(String type, ModelMap map){
		map.put("unitClass", getLoginInfo().getUnitClass());
		map.put("unitId", getLoginInfo().getUnitId());
		map.put("type", type);
		if(getLoginInfo().getUnitClass() == Unit.UNIT_CLASS_EDU && "1".equals(type)) {
			Unit edu = Unit.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()));
			if(edu != null) {
				map.put("topUnitId", edu.getId());
			}
		}
		if("1".equals(type) || "3".equals(type)) {
			List<String[]> gcs = new ArrayList<>();
			String[] secs = null;
			List<String[]> gcStrs = new ArrayList<>();
			if(getLoginInfo().getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
				School sch = SUtils.dc(schoolRemoteService.findOneById(getLoginInfo().getUnitId()), School.class);
				secs = sch.getSections().split(",");
				for(String sec : secs) {
					if("1".equals(sec)) {
						Integer years = sch.getGradeYear();
						if(years != null) {
							gcStrs.add(new String[] {sec, years+"", "小学"});
						} else {
							gcStrs.add(new String[] {sec, "0", "小学"});
						}
					} else if("2".equals(sec)) {
						Integer years = sch.getJuniorYear();
						if(years != null) {
							gcStrs.add(new String[] {sec, years+"", "初中"});
						} else {
							gcStrs.add(new String[] {sec, "0", "初中"});
						}
					} else if("3".equals(sec)) {
						Integer years = sch.getSeniorYear();
						if(years != null) {
							gcStrs.add(new String[] {sec, years+"", "高中"});
						} else {
							gcStrs.add(new String[] {sec, "0", "高中"});
						}
					}
				}
			} else {
				gcStrs.add(new String[] {"1", "6", "小学"});
				gcStrs.add(new String[] {"2", "3", "初中"});
				gcStrs.add(new String[] {"3", "3", "高中"});
			}
			for(String[] gcStr : gcStrs) {
				String sec = gcStr[0];
				int years = NumberUtils.toInt(gcStr[1]);
				String secStr = gcStr[2];
				gcs.add(new String[] {sec+"0", secStr + "全部"});
				if(years > 0) {
					for(int i=1;i<=years;i++) {
						gcs.add(new String[] {sec+i, secStr + BaseConstants.strOfNumberMap.get(i+"") + "年级"});
					}
				}
			}
			map.put("gradeCodes", gcs);
		}
		return "/newstusys/edu/student/report/studentRoster.ftl";
	}
	
	//花名册
	@RequestMapping("/studentRosterList")
	public String studentRosterList(HttpServletRequest req, String unitId, ModelMap map){
		Pagination page = createPagination();
		String gc = req.getParameter("gradeCode");
		String st = req.getParameter("searchType");
		List<Student> studentList = studentList(unitId, gc, st, page);
		map.put("gradeCode", gc);
		map.put("searchType", st);
		map.put("unitId", unitId);
		map.put("studentList", studentList);
		sendPagination(req, map, page);
		return "/newstusys/edu/student/report/studentRosterList.ftl";
	}
	
	@RequestMapping("/studentRosterExport")
	public void studentRosterExport(HttpServletRequest req, String unitId,HttpServletResponse response){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(unit.getUnitName()+"花名册报表");
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 12);// 字体大小
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
		String[] titles = { "所在镇乡（街道）", "学段", "学校名称", "年级", "班级", "班内编号", "学生姓名", "性别", "民族", "学籍主号", "户籍省县", "户籍镇街",
				"户籍社区/村", "父亲姓名", "母亲姓名", "是否为随迁子女或留守儿童", "是否随班就读", "是否为住宿生", "无户口学生", "休学中", "备注" };
		for(int i=0;i<titles.length;i++) {
			sheet.setColumnWidth(i, 20*256);
			String title = titles[i];
			HSSFCell cell11 = row1.createCell(i);
			cell11.setCellStyle(style);
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
		
		List<Student> studentList = studentList(unitId, req.getParameter("gradeCode"), req.getParameter("searchType"), null);
		Map<String, McodeDetail> xdmcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-RKXD"),new TypeReference<Map<String,McodeDetail>>(){});
		Map<String, McodeDetail> xbmcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
		Map<String, McodeDetail> mzmcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
		int i=1;
		for(Student stu : studentList){
			HSSFRow row2 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell21 = row2.createCell(j++);
			cell21.setCellStyle(style2);
			cell21.setCellValue(new HSSFRichTextString(stu.getPartyIntroducer()));
			
			HSSFCell cell22 = row2.createCell(j++);
			cell22.setCellStyle(style2);		
			if(null!=xdmcodeDetailMap.get(String.valueOf(stu.getCompatriots()))){
				cell22.setCellValue(new HSSFRichTextString(xdmcodeDetailMap.get(String.valueOf(stu.getCompatriots())).getMcodeContent()));
			}else{
				cell22.setCellValue(new HSSFRichTextString(""));
			}
			
			HSSFCell cell23 = row2.createCell(j++);
			cell23.setCellStyle(style2);
			cell23.setCellValue(new HSSFRichTextString(stu.getSchoolName()));
			
			HSSFCell cell24 = row2.createCell(j++);
			cell24.setCellStyle(style2);
			cell24.setCellValue(new HSSFRichTextString(stu.getBackground()));
			
			HSSFCell cell25 = row2.createCell(j++);
			cell25.setCellStyle(style2);
			cell25.setCellValue(new HSSFRichTextString(stu.getClassName()));
			
			HSSFCell cell26 = row2.createCell(j++);
			cell26.setCellStyle(style2);
			cell26.setCellValue(new HSSFRichTextString(stu.getClassInnerCode()));
			
			HSSFCell cell27 = row2.createCell(j++);
			cell27.setCellStyle(style2);
			cell27.setCellValue(new HSSFRichTextString(stu.getStudentName()));
			
			HSSFCell cell28 = row2.createCell(j++);
			cell28.setCellStyle(style2);
			if(null!=xbmcodeDetailMap.get(String.valueOf(stu.getSex()))){
				cell28.setCellValue(new HSSFRichTextString(xbmcodeDetailMap.get(String.valueOf(stu.getSex())).getMcodeContent()));
			}else{
				cell28.setCellValue(new HSSFRichTextString(""));
			}
			
			HSSFCell cell29 = row2.createCell(j++);
			cell29.setCellStyle(style2);
			if(null!=mzmcodeDetailMap.get(String.valueOf(stu.getNation()))){
				cell29.setCellValue(new HSSFRichTextString(mzmcodeDetailMap.get(String.valueOf(stu.getNation())).getMcodeContent()));
			}else{
				cell29.setCellValue(new HSSFRichTextString(""));
			}
			
			HSSFCell cell210 = row2.createCell(j++);
			cell210.setCellStyle(style2);
			cell210.setCellValue(new HSSFRichTextString(stu.getUnitiveCode()));
			
			HSSFCell cell211 = row2.createCell(j++);
			cell211.setCellStyle(style2);
			cell211.setCellValue(new HSSFRichTextString(stu.getRegisterPlace()));
			
			HSSFCell cell212 = row2.createCell(j++);
			cell212.setCellStyle(style2);
			cell212.setCellValue(new HSSFRichTextString(stu.getRegisterAddress()));
			
			HSSFCell cell213 = row2.createCell(j++);
			cell213.setCellStyle(style2);
			cell213.setCellValue(new HSSFRichTextString(""));
			
			HSSFCell cell214 = row2.createCell(j++);
			cell214.setCellStyle(style2);
			cell214.setCellValue(new HSSFRichTextString(stu.getLinkAddress()));
			
			HSSFCell cell215 = row2.createCell(j++);
			cell215.setCellStyle(style2);
			cell215.setCellValue(new HSSFRichTextString(stu.getNowaddress()));
			
			HSSFCell cell216 = row2.createCell(j++);
			cell216.setCellStyle(style2);
			String migration = "";
			String stayin = "";
			if("1".equals(stu.getIsMigration())){
				migration = migration+"随迁子女";
			}
			if(null!=stu.getStayin() && stu.getStayin()==1){
				stayin = stayin+"留守儿童";
			}
			cell216.setCellValue(new HSSFRichTextString(migration+stayin));
			
			HSSFCell cell217 = row2.createCell(j++);
			cell217.setCellStyle(style2);
			String regularClass = "";
			if(StringUtils.isNotBlank(stu.getRegularClass()) && !"1".equals(stu.getRegularClass())){
				regularClass = "随班就读";
			}
			cell217.setCellValue(new HSSFRichTextString(regularClass));
						
			HSSFCell cell218 = row2.createCell(j++);
			cell218.setCellStyle(style2);
			String boarding = "";
			if("1".equals(stu.getIsBoarding())){
				boarding = "住宿生";
			}
			cell218.setCellValue(new HSSFRichTextString(boarding));
			
			HSSFCell cell219 = row2.createCell(j++);
			cell219.setCellStyle(style2);
			String source = "";
			if("4".equals(stu.getSource())){
				source = "无户口";
			}
			cell219.setCellValue(new HSSFRichTextString(source));
			
			HSSFCell cell220 = row2.createCell(j++);
			cell220.setCellStyle(style2);
			cell220.setCellValue(new HSSFRichTextString(""));
			
			HSSFCell cell221 = row2.createCell(j++);
			cell221.setCellStyle(style2);
			cell221.setCellValue(new HSSFRichTextString(stu.getRemark()));
		}
		
		ExportUtils.outputData(workbook, unit.getUnitName()+"花名册报表", response);
	}
	
	public List<Student> studentList(String unitId, String gradeCode, String searchType, Pagination page){
		List<Student> studentList = new ArrayList<>();
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
			return studentList;
		}
		schIds = EntityUtils.getList(schs, Unit::getId);
		Set<String> pids = EntityUtils.getSet(schs, Unit::getParentId);
		Map<String, Unit> unitNames = EntityUtils.getMap(schs, Unit::getId);
		pids.removeAll(unitNames.keySet());
		unitNames.putAll(EntityUtils.getMap(Unit.dt(unitRemoteService.findListByIds(pids.toArray(new String[0]))), Unit::getId));
//		Unit punit = SUtils.dc(unitRemoteService.findOneById(unit.getParentId()), Unit.class);
		int section = -1;
		String enrollYear = null;
		if(StringUtils.isNotEmpty(gradeCode)) {
			section = NumberUtils.toInt(gradeCode.charAt(0)+"");
			if(!gradeCode.endsWith("0")) {
				Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
				int endYear = NumberUtils.toInt(sem.getAcadyear().substring(5));
				int grade = NumberUtils.toInt(gradeCode.substring(1));
				enrollYear = (endYear-grade)+"-"+(endYear-grade+1);
			}
		}
		//if(null!=sem){		
			//String acadyear = sem.getAcadyear();
			//List<Clazz> clsList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId,acadyear), new TR<List<Clazz>>(){});
			String[] names = null;
			Object[] params = null;
			if(StringUtils.isNotEmpty(enrollYear)) {
				names = new String[]{"isDeleted", "isGraduate", "section", "acadyear"};
				params = new Object[]{0, 0, section, enrollYear};
			} else if(section != -1){
				names = new String[]{"isDeleted", "isGraduate", "section"};
				params = new Object[]{0, 0, section};
			} else {
				names = new String[]{"isDeleted", "isGraduate"};
				params = new Object[]{0, 0};
			}
			List<Clazz> clsList = classRemoteService.findListObjectBy(Clazz.class, names, params, "schoolId", schIds.toArray(), 
					new String[] {"id", "gradeId", "className", "section", "acadyear", "classCode"}); 
//			Clazz.dt(classRemoteService.findBySchoolIdIn(new String[]{unitId}));
			Set<String> clsIdSet = new HashSet<String>();
			Set<String> gradeIdSet = new HashSet<String>();
			Map<String,Clazz> clsMap = new HashMap<String, Clazz>();
			for(Clazz cls : clsList){
				if (!gradeIdSet.contains(cls.getGradeId())) {
					gradeIdSet.add(cls.getGradeId());
				}
				clsIdSet.add(cls.getId());
				clsMap.put(cls.getId(), cls);
			}
			Map<String, String> gradeNameMap = new HashMap<String, String>();
			Map<String, String> clsGradeNameMap = new HashMap<String, String>();
			if(CollectionUtils.isNotEmpty(gradeIdSet)){
				int per = 900;
				List<Grade> gradeList = new ArrayList<>();
				if(gradeIdSet.size() < 900) {
					gradeList.addAll(SUtils.dt(gradeRemoteService.findListByIds(gradeIdSet.toArray(new String[0])), new TR<List<Grade>>(){}));
				} else {
					int size = (gradeIdSet.size()/per)+(gradeIdSet.size()%per==0?0:1);
					String[] clsIds = gradeIdSet.toArray(new String[0]);
					for(int i=0;i<size;i++) {
						int min = i*per;
						int max = (i+1)*per;
						if(max > gradeIdSet.size()) {
							max = gradeIdSet.size();
						}
						gradeList.addAll(SUtils.dt(gradeRemoteService.findListByIds(ArrayUtils.subarray(clsIds, min, max)),new TR<List<Grade>>(){}));
					}
				}
				
				for(Grade grade : gradeList){
					gradeNameMap.put(grade.getId(), grade.getGradeName());
				}
				
				for(Clazz cls : clsList){
					cls.setClassNameDynamic(StringUtils.trimToEmpty(gradeNameMap.get(cls.getGradeId()))+cls.getClassName());
					clsGradeNameMap.put(cls.getId(), gradeNameMap.get(cls.getGradeId()));
				}
			}
			if(CollectionUtils.isNotEmpty(clsIdSet)){
//				studentList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>(){});
				int per = 900;
				if(clsIdSet.size() < per) {
					studentList = baseStudentService.findStudentByClsIds(clsIdSet.toArray(new String[0]), searchType, null);
				} else {
					int size = (clsIdSet.size()/per)+(clsIdSet.size()%per==0?0:1);
					String[] clsIds = clsIdSet.toArray(new String[0]);
					for(int i=0;i<size;i++) {
						int min = i*per;
						int max = (i+1)*per;
						if(max > clsIdSet.size()) {
							max = clsIdSet.size();
						}
						studentList.addAll(baseStudentService.findStudentByClsIds(ArrayUtils.subarray(clsIds, min, max), searchType, null));
					}
				}
				
				for(Student student : studentList){
					student.setCompatriots(clsMap.get(student.getClassId()).getSection());//学段
				}
				Collections.sort(studentList,new Comparator<Student>() {
		            @Override
		            public int compare(Student o1, Student o2) {
		            	String sid1 = o1.getSchoolId();
		            	String sid2 = o2.getSchoolId();
		            	if(!StringUtils.equals(sid1, sid2)) {
		            		Unit sch1 = unitNames.get(sid1);
		            		Unit sch2 = unitNames.get(sid2);
		            		if(!StringUtils.equals(sch1.getParentId(), sch2.getParentId())) {
		            			return StringUtils.trimToEmpty(sch1.getParentId()).compareTo(sch2.getParentId());
		            		}
		            		return sid1.compareTo(sid2);
		            	}
		            	
		            	int sec1 = o1.getCompatriots();
		                int sec2 = o2.getCompatriots();
		                if(sec1 != sec2) {// 学段
		                	return sec1 - sec2;
		                }
		                String street1 = clsMap.get(o1.getClassId()).getAcadyear();
		                String street2 = clsMap.get(o2.getClassId()).getAcadyear();
		                if(!StringUtils.equals(street1, street2)) {// 年级
		                	return StringUtils.trimToEmpty(street2).compareTo(street1);
		                }
		                street1 =  clsMap.get(o1.getClassId()).getClassCode();
		                street2 =  clsMap.get(o2.getClassId()).getClassCode();
		                if(!StringUtils.equals(street1, street2)) {// 年级
		                	return StringUtils.trimToEmpty(street1).compareTo(street2);
		                }
		                String c1 = o1.getClassInnerCode();
		                String c2 = o2.getClassInnerCode();
		                if(StringUtils.isEmpty(c1)) {
		                	c1 = "9999999999";
		                }
		                if(StringUtils.isEmpty(c2)) {
		                	c2 = "9999999999";
		                }
		                long l1 = NumberUtils.toLong(c1);
		                long l2 = NumberUtils.toLong(c2);
		                if(l1 != l2) {// 学段
		                	return l1<l2?-1:1;
		                }
		                return 0;
		            }
		        });
				if(null!=page){
					//studentList = Student.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0]), SUtils.s(page)), page);
					List<Student> enrollStudentNewList = new ArrayList<Student>();
						
					page.setMaxRowCount(studentList.size());
					Integer pageSize = page.getPageSize();
					Integer pageIndex = page.getPageIndex();
					for(int i=pageSize*(pageIndex-1);i<studentList.size();i++){
						if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
							enrollStudentNewList.add(studentList.get(i));
						} else {
							break;
						}
					}
					studentList = enrollStudentNewList;
				}//else{
					//studentList = SUtils.dt(studentRemoteService.findByClassIds(clsIdSet.toArray(new String[0])), new TR<List<Student>>(){});	
				//}				
			}
			Set<String> registerPlaceSet = new HashSet<String>();
			Set<String> stuIds = new HashSet<>();
			for(Student student : studentList){
				if (!registerPlaceSet.contains(student.getRegisterPlace())) {
					registerPlaceSet.add(student.getRegisterPlace());
				}
				stuIds.add(student.getId());
			}
			List<Family> familyList = new ArrayList<>(); 
			int per = 900;
			if (stuIds.size() < per) {
				familyList.addAll(SUtils.dt(familyRemoteService.findByStudentIds(stuIds.toArray(new String[0])), new TR<List<Family>>() {
				}));
			} else {
				int size = (stuIds.size()/per)+(stuIds.size()%per==0?0:1);
				String[] clsIds = stuIds.toArray(new String[0]);
				for(int i=0;i<size;i++) {
					int min = i*per;
					int max = (i+1)*per;
					if(max > stuIds.size()) {
						max = stuIds.size();
					}
					familyList.addAll(SUtils.dt(familyRemoteService.findByStudentIds(ArrayUtils.subarray(clsIds, min, max)), new TR<List<Family>>() {
					}));
				}
			}
			Map<String,String> famMap = new HashMap<String, String>();
			for(Family family : familyList){
				famMap.put(family.getStudentId()+family.getRelation(), family.getRealName());
			}
			Map<String, String> registerPlaceMap = new HashMap<String, String>();
			if(CollectionUtils.isNotEmpty(registerPlaceSet)){								
				List<Region> regionList = new ArrayList<>(); 
				if (registerPlaceSet.size() <= per) {
					regionList.addAll(SUtils.dt(regionRemoteService.findByFullCodes("1", registerPlaceSet.toArray(new String[0])),
							new TR<List<Region>>() {
							}));
				} else {
					int size = (registerPlaceSet.size()/per)+(registerPlaceSet.size()%per==0?0:1);
					String[] clsIds = registerPlaceSet.toArray(new String[0]);
					for(int i=0;i<size;i++) {
						int min = i*per;
						int max = (i+1)*per;
						if(max > registerPlaceSet.size()) {
							max = registerPlaceSet.size();
						}
						regionList.addAll(SUtils.dt(regionRemoteService.findByFullCodes("1", ArrayUtils.subarray(clsIds, min, max)),
								new TR<List<Region>>() {
								}));
					}
				}
				for(Region region : regionList){
					registerPlaceMap.put(region.getFullCode(), region.getFullName());
				}
			}
			for(Student student : studentList){
				student.setClassName(clsMap.get(student.getClassId()).getClassNameDynamic());
				student.setCompatriots(clsMap.get(student.getClassId()).getSection());//学段
				student.setBackground(clsGradeNameMap.get(student.getClassId()));//年级
				Unit sch = unitNames.get(student.getSchoolId());
				student.setSchoolName(sch.getUnitName());
				if (unitNames.containsKey(sch.getParentId())) {
					student.setPartyIntroducer(unitNames.get(sch.getParentId()).getUnitName());//上级单位
				}
				student.setBankCardNo(famMap.get(student.getId()+"51"));//父亲
				student.setBelongContinents(famMap.get(student.getId()+"52"));//母亲
				if(null!=registerPlaceMap.get(student.getRegisterPlace())){
					student.setRegisterPlace(registerPlaceMap.get(student.getRegisterPlace()));
				}
			}
		return studentList;
	}
	
	//按班级统计
	@RequestMapping("/classCount")
	public String classCount(String unitId, ModelMap map){
		List<ClazzCountDto> dtoList = clazzCountDtoList(unitId);
		map.put("unitId", unitId);
		map.put("dtoList", dtoList);
		return "/newstusys/edu/student/report/classCount.ftl";
	}
	
	@RequestMapping("/classCountExport")
	public void classCountExport(String unitId,HttpServletResponse response){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);

		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(unit.getUnitName()+"在校生统计表（按班）");
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 12);// 字体大小
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
		int col = 1;
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,col++);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car12 = new CellRangeAddress(0,1,col, col++);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
	    sheet.addMergedRegion(car12);
		HSSFCell cell12 = row1.createCell(col-1);
		cell12.setCellStyle(style);
		cell12.setCellValue(new HSSFRichTextString("年级"));
		
		CellRangeAddress car13 = new CellRangeAddress(0,1,col,col++);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car13, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car13, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car13, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car13, sheet); 
	    sheet.addMergedRegion(car13);
		HSSFCell cell13 = row1.createCell(col-1);
		cell13.setCellStyle(style);
		cell13.setCellValue(new HSSFRichTextString("班级"));
		
		CellRangeAddress car14 = new CellRangeAddress(0,0,col,col+3);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
	    sheet.addMergedRegion(car14);
		HSSFCell cell14 = row1.createCell(col);
		cell14.setCellStyle(style);
		cell14.setCellValue(new HSSFRichTextString("实际学生总数"));
		col = col+4;
		
		CellRangeAddress car15 = new CellRangeAddress(0,0,col,col+8);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
	    sheet.addMergedRegion(car15);
		HSSFCell cell15 = row1.createCell(col);
		cell15.setCellStyle(style);
		cell15.setCellValue(new HSSFRichTextString("其他各类学生数"));
		
		HSSFRow row2 = sheet.createRow(1);
		String[] titles = new String[] {"所在镇乡（街道）","校点名称","", "","班级班额","诸暨户籍学生","市外户籍","无户口学生",
				"随迁子女（总）","随迁子女（省外）","留守儿童","随班就读","外籍学生","港澳台生","待转入学生","住宿学生","备注"};
		for (int i = 0; i < titles.length; i++) {
			if (i < 4) {
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			HSSFCell cell21 = row2.createCell(i);
			cell21.setCellStyle(style);
			cell21.setCellValue(new HSSFRichTextString(titles[i]));
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
		List<ClazzCountDto> dtoList = clazzCountDtoList(unitId);
		int i=2;
		for(ClazzCountDto dto : dtoList){
			HSSFRow row3 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell31 = row3.createCell(j++);
			cell31.setCellStyle(style2);
			cell31.setCellValue(new HSSFRichTextString(dto.getAddress()));
			
			HSSFCell cell32 = row3.createCell(j++);
			cell32.setCellStyle(style2);
			cell32.setCellValue(new HSSFRichTextString(dto.getSchoolName()));
			
			HSSFCell cell33 = row3.createCell(j++);
			cell33.setCellStyle(style2);
			cell33.setCellValue(new HSSFRichTextString(dto.getGradeName()));
			
			HSSFCell cell34 = row3.createCell(j++);
			cell34.setCellStyle(style2);
			cell34.setCellValue(new HSSFRichTextString(dto.getClassName()));
			
			HSSFCell cell35 = row3.createCell(j++);
			cell35.setCellStyle(style2);
			cell35.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClsStudentCount())));
			
			HSSFCell cell36 = row3.createCell(j++);
			cell36.setCellStyle(style2);
			cell36.setCellValue(new HSSFRichTextString(String.valueOf(dto.getInCityCount())));
			
			HSSFCell cell37 = row3.createCell(j++);
			cell37.setCellStyle(style2);
			cell37.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotCityCount())));
			
			HSSFCell cell38 = row3.createCell(j++);
			cell38.setCellStyle(style2);
			cell38.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotHkStuCount())));
			
			HSSFCell cell39 = row3.createCell(j++);
			cell39.setCellStyle(style2);
			cell39.setCellValue(new HSSFRichTextString(String.valueOf(dto.getMigrationStuCount())));
			
			HSSFCell cell40 = row3.createCell(j++);
			cell40.setCellStyle(style2);
			cell40.setCellValue(new HSSFRichTextString(String.valueOf(dto.getSyMigrationStuCount())));
			
			HSSFCell cell41 = row3.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(String.valueOf(dto.getStayinStuCount())));
			
			HSSFCell cell42 = row3.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(String.valueOf(dto.getRegularClassStuCount())));
			
			HSSFCell cell43 = row3.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNormalStuCount())));
			
			HSSFCell cell44 = row3.createCell(j++);
			cell44.setCellStyle(style2);
			cell44.setCellValue(new HSSFRichTextString(String.valueOf(dto.getCompatriotsCount())));
			
			HSSFCell cell45 = row3.createCell(j++);
			cell45.setCellStyle(style2);
			cell45.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNowStateStuCount())));
			
			HSSFCell cell46 = row3.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(dto.getBoardingStuCount())));
			
			HSSFCell cell47 = row3.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(""));
		}
		
		ExportUtils.outputData(workbook, unit.getUnitName()+"在校生统计表（按班）", response);
	}
	

	public List<ClazzCountDto> clazzCountDtoList(String unitId){
		List<ClazzCountDto> dtoList = new ArrayList<ClazzCountDto>();
		//Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
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
		//if(null!=sem){			
			//String acadyear = sem.getAcadyear();
			//List<Clazz> clsList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId,acadyear), new TR<List<Clazz>>(){});
			List<Clazz> clsList = SUtils.dt(classRemoteService.findBySchoolIdIn(schIds.toArray(new String[0])), new TR<List<Clazz>>(){});
			Set<String> gradeIdSet = new HashSet<String>();
			Set<String> clsIdSet = new HashSet<String>();
			for(Clazz cls : clsList){
				if(cls.getIsGraduate() != null && cls.getIsGraduate() == 1) {
					continue;
				}
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
			Map<String, Integer> clsStudentMap = new HashMap<String, Integer>();//班级学生数
			Map<String, Integer> inCityCountMap = new HashMap<String, Integer>();//市内学生总数			
			Map<String, Integer> notCityCountMap = new HashMap<String, Integer>();//市外学生数
			Map<String, Integer> notHkStuCountMap = new HashMap<String, Integer>();//无户口学生数
			Map<String, Integer> migrationStuCountMap = new HashMap<String, Integer>();//随迁子女数总
			Map<String, Integer> syMigrationStuCountMap = new HashMap<String, Integer>();//随迁子女数省外
			Map<String, Integer> stayinStuCountMap = new HashMap<String, Integer>();//留守儿童
			Map<String, Integer> regularClassStuCountMap = new HashMap<String, Integer>();//随班就读
			Map<String, Integer> normalStuCountMap = new HashMap<String, Integer>();//外籍学生数
			Map<String, Integer> compatriotsCountMap = new HashMap<String, Integer>();//港澳台生数
			Map<String, Integer> nowStateStuCountMap = new HashMap<String, Integer>();//待转入学生数
			Map<String, Integer> boardingStuCountMap = new HashMap<String, Integer>();//住宿学生数
			if(CollectionUtils.isNotEmpty(clsIdSet)){
				clsStudentMap = studentReportService.stuCountByClass(clsIdSet.toArray(new String[0]));
				inCityCountMap = studentReportService.inCityStuCountByClass(clsIdSet.toArray(new String[0]));
				notCityCountMap = studentReportService.notInCityStuCountByClass(clsIdSet.toArray(new String[0]));
				notHkStuCountMap = studentReportService.notHkStuCountByClass(clsIdSet.toArray(new String[0]));
				migrationStuCountMap = studentReportService.migrationStuCountByClass(clsIdSet.toArray(new String[0]));
				syMigrationStuCountMap = studentReportService.sYmigrationStuCountByClass(clsIdSet.toArray(new String[0]));
				stayinStuCountMap = studentReportService.stayinStuCountByClass(clsIdSet.toArray(new String[0]));
				regularClassStuCountMap = studentReportService.regularClassStuCountByClass(clsIdSet.toArray(new String[0]));
				normalStuCountMap = studentReportService.normalStuCountByClass(clsIdSet.toArray(new String[0]));
				compatriotsCountMap = studentReportService.compatriotsCountByClass(clsIdSet.toArray(new String[0]));
				nowStateStuCountMap = studentReportService.nowStateStuCountByClass(clsIdSet.toArray(new String[0]));
				boardingStuCountMap = studentReportService.boardingStuCountByClass(clsIdSet.toArray(new String[0]));
			}
			int clsStudentCount = 0;
			int inCityCount = 0;
			int notCityCount = 0;
			int notHkStuCount = 0;
			int migrationStuCount = 0;
			int syMigrationStuCount = 0;
			int stayinStuCount = 0;
			int regularClassStuCount = 0;
			int normalStuCount = 0;			
			int compatriotsCount = 0;
			int nowStateStuCount = 0;
			int boardingStuCount = 0;
//			int classCount = 0;
			for(Clazz cls : clsList){
				ClazzCountDto dto = new ClazzCountDto();
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
				if(null!=inCityCountMap.get(cls.getId())){	
					inCityCount = inCityCount + inCityCountMap.get(cls.getId());
					dto.setInCityCount(inCityCountMap.get(cls.getId()));
				}
				if(null!=notCityCountMap.get(cls.getId())){
					notCityCount = notCityCount + notCityCountMap.get(cls.getId());
					dto.setNotCityCount(notCityCountMap.get(cls.getId()));
				}
				if(null!=notHkStuCountMap.get(cls.getId())){
					notHkStuCount = notHkStuCount + notHkStuCountMap.get(cls.getId());
					dto.setNotHkStuCount(notHkStuCountMap.get(cls.getId()));
				}
				if(null!=migrationStuCountMap.get(cls.getId())){	
					migrationStuCount = migrationStuCount + migrationStuCountMap.get(cls.getId());
					dto.setMigrationStuCount(migrationStuCountMap.get(cls.getId()));
				}
				if(null!=syMigrationStuCountMap.get(cls.getId())){	
					syMigrationStuCount = syMigrationStuCount + syMigrationStuCountMap.get(cls.getId());
					dto.setSyMigrationStuCount(syMigrationStuCountMap.get(cls.getId()));
				}
				if(null!=stayinStuCountMap.get(cls.getId())){	
					stayinStuCount = stayinStuCount + stayinStuCountMap.get(cls.getId());
					dto.setStayinStuCount(stayinStuCountMap.get(cls.getId()));
				}
				if(null!=regularClassStuCountMap.get(cls.getId())){		
					regularClassStuCount = regularClassStuCount + regularClassStuCountMap.get(cls.getId());
					dto.setRegularClassStuCount(regularClassStuCountMap.get(cls.getId()));
				}
				if(null!=normalStuCountMap.get(cls.getId())){	
					normalStuCount = normalStuCount + normalStuCountMap.get(cls.getId());
					dto.setNormalStuCount(normalStuCountMap.get(cls.getId()));
				}
				if(null!=compatriotsCountMap.get(cls.getId())){	
					compatriotsCount = compatriotsCount + compatriotsCountMap.get(cls.getId());
					dto.setCompatriotsCount(compatriotsCountMap.get(cls.getId()));
				}
				if(null!=nowStateStuCountMap.get(cls.getId())){	
					nowStateStuCount = nowStateStuCount + nowStateStuCountMap.get(cls.getId());
					dto.setNowStateStuCount(nowStateStuCountMap.get(cls.getId()));
				}
				if(null!=boardingStuCountMap.get(cls.getId())){	
					boardingStuCount = boardingStuCount + boardingStuCountMap.get(cls.getId());
					dto.setBoardingStuCount(boardingStuCountMap.get(cls.getId()));
				}
				dtoList.add(dto);
			}
			
			Collections.sort(dtoList, new Comparator<ClazzCountDto>() {
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
				dto.setSchoolName("小计");
				dto.setClsStudentCount(clsStudentCount);		
				dto.setInCityCount(inCityCount);
				dto.setNotCityCount(notCityCount);
				dto.setNotHkStuCount(notHkStuCount);
				dto.setMigrationStuCount(migrationStuCount);
				dto.setSyMigrationStuCount(syMigrationStuCount);
				dto.setStayinStuCount(stayinStuCount);
				dto.setRegularClassStuCount(regularClassStuCount);
				dto.setNormalStuCount(normalStuCount);
				dto.setCompatriotsCount(compatriotsCount);
				dto.setBoardingStuCount(boardingStuCount);
				dto.setNowStateStuCount(nowStateStuCount);
				dtoList.add(dto);
			}
		//}
		return dtoList;
	}
	
	//按年级统计
	@RequestMapping("/gradeCount")
	public String gradeCount(String unitId, HttpServletRequest req, ModelMap map){
		List<GradeCountDto> dtoList = gradeCountDtoList(unitId, req.getParameter("gradeCode"));
		map.put("unitId", unitId);
		map.put("dtoList", dtoList);
		map.put("gradeCode", req.getParameter("gradeCode"));
		return "/newstusys/edu/student/report/gradeCount.ftl";
	}
	
	@RequestMapping("/gradeCountExport")
	public void gradeCountExport(String unitId, HttpServletRequest req, HttpServletResponse response){
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);

		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet(unit.getUnitName()+"在校生统计表（按年级）");
		
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 12);// 字体大小
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
		int col = 1;
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,col++);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car12 = new CellRangeAddress(0,1,col,col++);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
	    sheet.addMergedRegion(car12);
	    HSSFCell cell12 = row1.createCell(col-1);
		cell12.setCellStyle(style);
		cell12.setCellValue(new HSSFRichTextString("年级"));
		
		
		CellRangeAddress car14 = new CellRangeAddress(0,0,col,col+4);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
	    sheet.addMergedRegion(car14);
		HSSFCell cell14 = row1.createCell(col);
		cell14.setCellStyle(style);
		cell14.setCellValue(new HSSFRichTextString("实际学生总数"));
		col = col+5;
		
		CellRangeAddress car15 = new CellRangeAddress(0,0,col,col+8);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
	    sheet.addMergedRegion(car15);
		HSSFCell cell15 = row1.createCell(col);
		cell15.setCellStyle(style);
		cell15.setCellValue(new HSSFRichTextString("其他各类学生数"));
		
		HSSFRow row2 = sheet.createRow(1);
		String[] titles = new String[] {"所在镇乡（街道）","校点名称","", "班级总数","学生总数","诸暨户籍学生","市外户籍","无户口学生",
				"随迁子女（总）","随迁子女（省外）","留守儿童","随班就读","外籍学生","港澳台生","待转入学生","住宿学生","备注"};
		for (int i = 0; i < titles.length; i++) {
			if (i != 2) {
				sheet.setColumnWidth(i, 20 * 256);
			}
			HSSFCell cell21 = row2.createCell(i);
			cell21.setCellStyle(style);
			cell21.setCellValue(new HSSFRichTextString(titles[i]));
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
		List<GradeCountDto> dtoList = gradeCountDtoList(unitId, req.getParameter("gradeCode"));
		int i=2;
		for(GradeCountDto dto : dtoList){
			HSSFRow row3 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell31 = row3.createCell(j++);
			cell31.setCellStyle(style2);
			cell31.setCellValue(new HSSFRichTextString(dto.getAddress()));
			
			HSSFCell cell32 = row3.createCell(j++);
			cell32.setCellStyle(style2);
			cell32.setCellValue(new HSSFRichTextString(dto.getSchoolName()));
			
			HSSFCell cell33 = row3.createCell(j++);
			cell33.setCellStyle(style2);
			cell33.setCellValue(new HSSFRichTextString(dto.getGradeName()));
			
			HSSFCell cell35 = row3.createCell(j++);
			cell35.setCellStyle(style2);
			cell35.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClassCount())));
			
			HSSFCell cell66 = row3.createCell(j++);
			cell66.setCellStyle(style2);
			cell66.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClsStudentCount())));
			
			HSSFCell cell36 = row3.createCell(j++);
			cell36.setCellStyle(style2);
			cell36.setCellValue(new HSSFRichTextString(String.valueOf(dto.getInCityCount())));
			
			HSSFCell cell37 = row3.createCell(j++);
			cell37.setCellStyle(style2);
			cell37.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotCityCount())));
			
			HSSFCell cell38 = row3.createCell(j++);
			cell38.setCellStyle(style2);
			cell38.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotHkStuCount())));
			
			HSSFCell cell39 = row3.createCell(j++);
			cell39.setCellStyle(style2);
			cell39.setCellValue(new HSSFRichTextString(String.valueOf(dto.getMigrationStuCount())));
			
			HSSFCell cell40 = row3.createCell(j++);
			cell40.setCellStyle(style2);
			cell40.setCellValue(new HSSFRichTextString(String.valueOf(dto.getSyMigrationStuCount())));
			
			HSSFCell cell41 = row3.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(String.valueOf(dto.getStayinStuCount())));
			
			HSSFCell cell42 = row3.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(String.valueOf(dto.getRegularClassStuCount())));
			
			HSSFCell cell43 = row3.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNormalStuCount())));
			
			HSSFCell cell44 = row3.createCell(j++);
			cell44.setCellStyle(style2);
			cell44.setCellValue(new HSSFRichTextString(String.valueOf(dto.getCompatriotsCount())));
			
			HSSFCell cell45 = row3.createCell(j++);
			cell45.setCellStyle(style2);
			cell45.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNowStateStuCount())));
			
			HSSFCell cell46 = row3.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(dto.getBoardingStuCount())));
			
			HSSFCell cell47 = row3.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(""));
		}
		
		ExportUtils.outputData(workbook, unit.getUnitName()+"在校生统计表（按年级）", response);
	}
	
	public List<GradeCountDto> gradeCountDtoList(String unitId, String gradeCode){
		List<GradeCountDto> dtoList = new ArrayList<GradeCountDto>();
		Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
		Set<String> gradeIdSet = new HashSet<String>();
		List<Grade> gradeList = new ArrayList<Grade>();
		Map<String, String> pNameMap = new HashMap<String, String>();
		Map<String, String> pUnitNameMap = new HashMap<String, String>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		
		Set<String> unitIdSet = new HashSet<String>();
		int section = -1;
		String enrollYear = null;
		if(StringUtils.isNotEmpty(gradeCode)) {
			section = NumberUtils.toInt(gradeCode.charAt(0)+"");
			if(!gradeCode.endsWith("0")) {
				Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
				int endYear = NumberUtils.toInt(sem.getAcadyear().substring(5));
				int grade = NumberUtils.toInt(gradeCode.substring(1));
				enrollYear = (endYear-grade)+"-"+(endYear-grade+1);
			}
		}
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
		    	Map<String, List<Grade>> gradeMap = SUtils.dt(gradeRemoteService.findBySchoolIdMap(unitIdSet.toArray(new String[0])), new TypeReference<Map<String, List<Grade>>>(){});
		        for(String key : gradeMap.keySet()){
		        	List<Grade> gList = gradeMap.get(key);
		        	for(Grade grade : gList){
		        		if(StringUtils.isNotEmpty(enrollYear) && !enrollYear.equals(grade.getOpenAcadyear())) {
		        			continue;
		        		}
		        		if(section != -1 && grade.getSection() != section) {
		        			continue;
		        		}
		        		gradeIdSet.add(grade.getId());
		        		gradeList.add(grade);
		        	}
		        }
		    }
		}else{
			Unit punit = SUtils.dc(unitRemoteService.findOneById(unit.getParentId()), Unit.class);			
			pUnitNameMap.put(unit.getId(), punit.getUnitName());
			unitNameMap.put(unit.getId(), unit.getUnitName());
			gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>(){});
			Iterator<Grade> git = gradeList.iterator();
			while(git.hasNext()){
				Grade grade = git.next();
				if(StringUtils.isNotEmpty(enrollYear) && !enrollYear.equals(grade.getOpenAcadyear())) {
					git.remove();
        			continue;
        		}
        		if(section != -1 && grade.getSection() != section) {
        			git.remove();
        			continue;
        		}
				gradeIdSet.add(grade.getId());
			}			
		}
		
		Map<String, Integer> classCountMap = new HashMap<String, Integer>();//班级总数
		Map<String, Integer> clsStudentMap = new HashMap<String, Integer>();//班级学生数
		Map<String, Integer> inCityCountMap = new HashMap<String, Integer>();//市内学生总数			
		Map<String, Integer> notCityCountMap = new HashMap<String, Integer>();//市外学生数
		Map<String, Integer> notHkStuCountMap = new HashMap<String, Integer>();//无户口学生数
		Map<String, Integer> migrationStuCountMap = new HashMap<String, Integer>();//随迁子女数总
		Map<String, Integer> syMigrationStuCountMap = new HashMap<String, Integer>();//随迁子女数省外
		Map<String, Integer> stayinStuCountMap = new HashMap<String, Integer>();//留守儿童
		Map<String, Integer> regularClassStuCountMap = new HashMap<String, Integer>();//随班就读
		Map<String, Integer> normalStuCountMap = new HashMap<String, Integer>();//外籍学生数
		Map<String, Integer> compatriotsCountMap = new HashMap<String, Integer>();//港澳台生数
		Map<String, Integer> nowStateStuCountMap = new HashMap<String, Integer>();//待转入学生数
		Map<String, Integer> boardingStuCountMap = new HashMap<String, Integer>();//住宿学生数
		if(CollectionUtils.isNotEmpty(gradeIdSet)){
			classCountMap = studentReportService.classCountByGrade(gradeIdSet.toArray(new String[0]));
			clsStudentMap = studentReportService.stuCountByGrade(gradeIdSet.toArray(new String[0]));
			inCityCountMap = studentReportService.inCityCountByGrade(gradeIdSet.toArray(new String[0]));
			notCityCountMap = studentReportService.notCityCountByGrade(gradeIdSet.toArray(new String[0]));
			notHkStuCountMap = studentReportService.notHkCountByGrade(gradeIdSet.toArray(new String[0]));
			migrationStuCountMap = studentReportService.migrationStuCountByGrade(gradeIdSet.toArray(new String[0]));
			syMigrationStuCountMap = studentReportService.sYmigrationStuCountByGrade(gradeIdSet.toArray(new String[0]));
			stayinStuCountMap = studentReportService.stayinStuCountByGrade(gradeIdSet.toArray(new String[0]));
			regularClassStuCountMap = studentReportService.regularClassStuCountByGrade(gradeIdSet.toArray(new String[0]));
			normalStuCountMap = studentReportService.normalStuCountByGrade(gradeIdSet.toArray(new String[0]));
			compatriotsCountMap = studentReportService.compatriotsCountByGrade(gradeIdSet.toArray(new String[0]));
			nowStateStuCountMap = studentReportService.nowStateStuCountByGrade(gradeIdSet.toArray(new String[0]));
			boardingStuCountMap = studentReportService.boardingStuCountByGrade(gradeIdSet.toArray(new String[0]));
		}
		int clsStudentCount = 0;
		int inCityCount = 0;
		int notCityCount = 0;
		int notHkStuCount = 0;
		int migrationStuCount = 0;
		int syMigrationStuCount = 0;
		int stayinStuCount = 0;
		int regularClassStuCount = 0;
		int normalStuCount = 0;			
		int compatriotsCount = 0;
		int nowStateStuCount = 0;
		int boardingStuCount = 0;
		int classCount = 0;
		for(Grade grade : gradeList){
			GradeCountDto dto = new GradeCountDto();
			dto.setAddress(pUnitNameMap.get(grade.getSchoolId()));
			dto.setGradeName(grade.getGradeName());
			dto.setSchoolName(unitNameMap.get(grade.getSchoolId()));
			dto.setGradeCode(grade.getGradeCode());
			if(null!=classCountMap.get(grade.getId())){	
				classCount = classCount + classCountMap.get(grade.getId());
				dto.setClassCount(classCountMap.get(grade.getId()));
			}
			if(null!=clsStudentMap.get(grade.getId())){
				clsStudentCount = clsStudentCount + clsStudentMap.get(grade.getId());
				dto.setClsStudentCount(clsStudentMap.get(grade.getId()));
			}
			if(null!=inCityCountMap.get(grade.getId())){
				inCityCount = inCityCount + inCityCountMap.get(grade.getId());
				dto.setInCityCount(inCityCountMap.get(grade.getId()));
			}
			if(null!=notCityCountMap.get(grade.getId())){
				notCityCount = notCityCount + notCityCountMap.get(grade.getId());
				dto.setNotCityCount(notCityCountMap.get(grade.getId()));
			}
			if(null!=notHkStuCountMap.get(grade.getId())){	
				notHkStuCount = notHkStuCount + notHkStuCountMap.get(grade.getId());
				dto.setNotHkStuCount(notHkStuCountMap.get(grade.getId()));
			}
			if(null!=migrationStuCountMap.get(grade.getId())){
				migrationStuCount = migrationStuCount + migrationStuCountMap.get(grade.getId());
				dto.setMigrationStuCount(migrationStuCountMap.get(grade.getId()));
			}
			if(null!=syMigrationStuCountMap.get(grade.getId())){	
				syMigrationStuCount = syMigrationStuCount + syMigrationStuCountMap.get(grade.getId());
				dto.setSyMigrationStuCount(syMigrationStuCountMap.get(grade.getId()));
			}
			if(null!=stayinStuCountMap.get(grade.getId())){		
				stayinStuCount = stayinStuCount + stayinStuCountMap.get(grade.getId());
				dto.setStayinStuCount(stayinStuCountMap.get(grade.getId()));
			}
			if(null!=regularClassStuCountMap.get(grade.getId())){	
				regularClassStuCount = regularClassStuCount + regularClassStuCountMap.get(grade.getId());
				dto.setRegularClassStuCount(regularClassStuCountMap.get(grade.getId()));
			}
			if(null!=normalStuCountMap.get(grade.getId())){	
				normalStuCount = normalStuCount + normalStuCountMap.get(grade.getId());
				dto.setNormalStuCount(normalStuCountMap.get(grade.getId()));
			}
			if(null!=compatriotsCountMap.get(grade.getId())){	
				compatriotsCount = compatriotsCount + compatriotsCountMap.get(grade.getId());
				dto.setCompatriotsCount(compatriotsCountMap.get(grade.getId()));
			}
			if(null!=nowStateStuCountMap.get(grade.getId())){	
				nowStateStuCount = nowStateStuCount + nowStateStuCountMap.get(grade.getId());
				dto.setNowStateStuCount(nowStateStuCountMap.get(grade.getId()));
			}
			if(null!=boardingStuCountMap.get(grade.getId())){	
				boardingStuCount = boardingStuCount + boardingStuCountMap.get(grade.getId());
				dto.setBoardingStuCount(boardingStuCountMap.get(grade.getId()));
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
			dto.setClassCount(classCount);
			dto.setClsStudentCount(clsStudentCount);		
			dto.setInCityCount(inCityCount);
			dto.setNotCityCount(notCityCount);
			dto.setNotHkStuCount(notHkStuCount);
			dto.setMigrationStuCount(migrationStuCount);
			dto.setSyMigrationStuCount(syMigrationStuCount);
			dto.setStayinStuCount(stayinStuCount);
			dto.setRegularClassStuCount(regularClassStuCount);
			dto.setNormalStuCount(normalStuCount);
			dto.setCompatriotsCount(compatriotsCount);
			dto.setBoardingStuCount(boardingStuCount);
			dto.setNowStateStuCount(nowStateStuCount);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	//全市小学
	@RequestMapping("/cityPrimary")
	public String cityPrimary(ModelMap map){	
		List<UnitCountDto> dtoList = unitPrimaryCountDtoList();	
		map.put("dtoList", dtoList);
		return "/newstusys/edu/student/report/cityPrimary.ftl";
	}
	
	@RequestMapping("/cityPrimaryExport")
	public void cityPrimaryExport(HttpServletResponse response){	
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet("在校生统计表（全市小学");
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 12);// 字体大小
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
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car12 = new CellRangeAddress(0,1,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
	    sheet.addMergedRegion(car12);
	    HSSFCell cell12 = row1.createCell(2);
		cell12.setCellStyle(style);
		cell12.setCellValue(new HSSFRichTextString("学段"));
		
		CellRangeAddress car14 = new CellRangeAddress(0,0,3,7);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
	    sheet.addMergedRegion(car14);
		HSSFCell cell14 = row1.createCell(3);
		cell14.setCellStyle(style);
		cell14.setCellValue(new HSSFRichTextString("实际学生总数"));
		
		CellRangeAddress car15 = new CellRangeAddress(0,0,8,16);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
	    sheet.addMergedRegion(car15);
		HSSFCell cell15 = row1.createCell(8);
		cell15.setCellStyle(style);
		cell15.setCellValue(new HSSFRichTextString("其他各类学生数"));
		
		HSSFRow row2 = sheet.createRow(1);
		String[] titles = new String[] {"所在镇乡（街道）","校点名称","", "班级总数","学生总数","诸暨户籍学生","市外户籍","无户口学生",
				"随迁子女（总）","随迁子女（省外）","留守儿童","随班就读","外籍学生","港澳台生","待转入学生","住宿学生","备注"};
		for (int i = 0; i < titles.length; i++) {
			if (i != 2) {
				sheet.setColumnWidth(i, 20 * 256);
			}
			HSSFCell cell21 = row2.createCell(i);
			cell21.setCellStyle(style);
			cell21.setCellValue(new HSSFRichTextString(titles[i]));
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
		List<UnitCountDto> dtoList = unitPrimaryCountDtoList();
		int i=2;
		for(UnitCountDto dto : dtoList){
			HSSFRow row3 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell31 = row3.createCell(j++);
			cell31.setCellStyle(style2);
			cell31.setCellValue(new HSSFRichTextString(dto.getAddress()));
			
			HSSFCell cell32 = row3.createCell(j++);
			cell32.setCellStyle(style2);
			cell32.setCellValue(new HSSFRichTextString(dto.getSchName()));
			
			HSSFCell cell33 = row3.createCell(j++);
			cell33.setCellStyle(style2);
			cell33.setCellValue(new HSSFRichTextString(dto.getSectionName()));
			
			HSSFCell cell35 = row3.createCell(j++);
			cell35.setCellStyle(style2);
			cell35.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClassCount())));
			
			HSSFCell cell66 = row3.createCell(j++);
			cell66.setCellStyle(style2);
			cell66.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClsStudentCount())));
			
			HSSFCell cell36 = row3.createCell(j++);
			cell36.setCellStyle(style2);
			cell36.setCellValue(new HSSFRichTextString(String.valueOf(dto.getInCityCount())));
			
			HSSFCell cell37 = row3.createCell(j++);
			cell37.setCellStyle(style2);
			cell37.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotCityCount())));
			
			HSSFCell cell38 = row3.createCell(j++);
			cell38.setCellStyle(style2);
			cell38.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotHkStuCount())));
			
			HSSFCell cell39 = row3.createCell(j++);
			cell39.setCellStyle(style2);
			cell39.setCellValue(new HSSFRichTextString(String.valueOf(dto.getMigrationStuCount())));
			
			HSSFCell cell40 = row3.createCell(j++);
			cell40.setCellStyle(style2);
			cell40.setCellValue(new HSSFRichTextString(String.valueOf(dto.getSyMigrationStuCount())));
			
			HSSFCell cell41 = row3.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(String.valueOf(dto.getStayinStuCount())));
			
			HSSFCell cell42 = row3.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(String.valueOf(dto.getRegularClassStuCount())));
			
			HSSFCell cell43 = row3.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNormalStuCount())));
			
			HSSFCell cell44 = row3.createCell(j++);
			cell44.setCellStyle(style2);
			cell44.setCellValue(new HSSFRichTextString(String.valueOf(dto.getCompatriotsCount())));
			
			HSSFCell cell45 = row3.createCell(j++);
			cell45.setCellStyle(style2);
			cell45.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNowStateStuCount())));
			
			HSSFCell cell46 = row3.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(dto.getBoardingStuCount())));
			
			HSSFCell cell47 = row3.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(""));
		}
		
		ExportUtils.outputData(workbook, "在校生统计表（全市小学）", response);
	}
	
	public List<UnitCountDto> unitPrimaryCountDtoList(){
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
		Map<String, Integer> classCountMap = new HashMap<String, Integer>();//班级总数
		Map<String, Integer> clsStudentMap = new HashMap<String, Integer>();//班级学生数
		Map<String, Integer> inCityCountMap = new HashMap<String, Integer>();//市内学生总数			
		Map<String, Integer> notCityCountMap = new HashMap<String, Integer>();//市外学生数
		Map<String, Integer> notHkStuCountMap = new HashMap<String, Integer>();//无户口学生数
		Map<String, Integer> migrationStuCountMap = new HashMap<String, Integer>();//随迁子女数总
		Map<String, Integer> syMigrationStuCountMap = new HashMap<String, Integer>();//随迁子女数省外
		Map<String, Integer> stayinStuCountMap = new HashMap<String, Integer>();//留守儿童
		Map<String, Integer> regularClassStuCountMap = new HashMap<String, Integer>();//随班就读
		Map<String, Integer> normalStuCountMap = new HashMap<String, Integer>();//外籍学生数
		Map<String, Integer> compatriotsCountMap = new HashMap<String, Integer>();//港澳台生数
		Map<String, Integer> nowStateStuCountMap = new HashMap<String, Integer>();//待转入学生数
		Map<String, Integer> boardingStuCountMap = new HashMap<String, Integer>();//住宿学生数
		if(CollectionUtils.isNotEmpty(unitIdSet)){
			classCountMap = studentReportService.classCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			clsStudentMap = studentReportService.stuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			inCityCountMap = studentReportService.inCityCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			notCityCountMap = studentReportService.notCityCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			notHkStuCountMap = studentReportService.notHkCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			migrationStuCountMap = studentReportService.migrationStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			syMigrationStuCountMap = studentReportService.sYmigrationStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			stayinStuCountMap = studentReportService.stayinStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			regularClassStuCountMap = studentReportService.regularClassStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			normalStuCountMap = studentReportService.normalStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			compatriotsCountMap = studentReportService.compatriotsCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			nowStateStuCountMap = studentReportService.nowStateStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			boardingStuCountMap = studentReportService.boardingStuCountBySchoolPrimary(unitIdSet2.toArray(new String[0]));
			
		}
		int clsStudentCount = 0;
		int inCityCount = 0;
		int notCityCount = 0;
		int notHkStuCount = 0;
		int migrationStuCount = 0;
		int syMigrationStuCount = 0;
		int stayinStuCount = 0;
		int regularClassStuCount = 0;
		int normalStuCount = 0;			
		int compatriotsCount = 0;
		int nowStateStuCount = 0;
		int boardingStuCount = 0;
		int classCount = 0;
		for(School u : schList){
			UnitCountDto dto = new UnitCountDto();
			if(null!=pUnitNameMap.get(pUnitIdMap.get(u.getId()))){
				dto.setAddress(pUnitNameMap.get(pUnitIdMap.get(u.getId())));
			}else{
				dto.setAddress("");
			}
			dto.setSchName(u.getSchoolName());
			dto.setSectionName("小学");
			if(null!=classCountMap.get(u.getId())){
				classCount = classCount + classCountMap.get(u.getId());
				dto.setClassCount(classCountMap.get(u.getId()));
			}
			if(null!=clsStudentMap.get(u.getId())){		
				clsStudentCount = clsStudentCount + clsStudentMap.get(u.getId());
				dto.setClsStudentCount(clsStudentMap.get(u.getId()));
			}
			if(null!=inCityCountMap.get(u.getId())){
				inCityCount = inCityCount + inCityCountMap.get(u.getId());
				dto.setInCityCount(inCityCountMap.get(u.getId()));
			}
			if(null!=notCityCountMap.get(u.getId())){	
				notCityCount = notCityCount + notCityCountMap.get(u.getId());
				dto.setNotCityCount(notCityCountMap.get(u.getId()));
			}
			if(null!=notHkStuCountMap.get(u.getId())){		
				notHkStuCount = notHkStuCount + notHkStuCountMap.get(u.getId());
				dto.setNotHkStuCount(notHkStuCountMap.get(u.getId()));
			}
			if(null!=migrationStuCountMap.get(u.getId())){		
				migrationStuCount = migrationStuCount + migrationStuCountMap.get(u.getId());
				dto.setMigrationStuCount(migrationStuCountMap.get(u.getId()));
			}
			if(null!=syMigrationStuCountMap.get(u.getId())){	
				syMigrationStuCount = syMigrationStuCount + syMigrationStuCountMap.get(u.getId());
				dto.setSyMigrationStuCount(syMigrationStuCountMap.get(u.getId()));
			}
			if(null!=stayinStuCountMap.get(u.getId())){			
				stayinStuCount = stayinStuCount + stayinStuCountMap.get(u.getId());
				dto.setStayinStuCount(stayinStuCountMap.get(u.getId()));
			}
			if(null!=regularClassStuCountMap.get(u.getId())){
				regularClassStuCount = regularClassStuCount + regularClassStuCountMap.get(u.getId());
				dto.setRegularClassStuCount(regularClassStuCountMap.get(u.getId()));
			}
			if(null!=normalStuCountMap.get(u.getId())){	
				normalStuCount = normalStuCount + normalStuCountMap.get(u.getId());
				dto.setNormalStuCount(normalStuCountMap.get(u.getId()));
			}
			if(null!=compatriotsCountMap.get(u.getId())){
				compatriotsCount = compatriotsCount + compatriotsCountMap.get(u.getId());
				dto.setCompatriotsCount(compatriotsCountMap.get(u.getId()));
			}
			if(null!=nowStateStuCountMap.get(u.getId())){	
				nowStateStuCount = nowStateStuCount + nowStateStuCountMap.get(u.getId());
				dto.setNowStateStuCount(nowStateStuCountMap.get(u.getId()));
			}
			if(null!=boardingStuCountMap.get(u.getId())){	
				boardingStuCount = boardingStuCount + boardingStuCountMap.get(u.getId());
				dto.setBoardingStuCount(boardingStuCountMap.get(u.getId()));
			}
			dtoList.add(dto);
		}		
		
		if(CollectionUtils.isNotEmpty(dtoList)){			
			Collections.sort(dtoList, new Comparator<UnitCountDto>() {
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
			
			UnitCountDto dto = new UnitCountDto();
			dto.setSchName("小计");
			dto.setClassCount(classCount);
			dto.setClsStudentCount(clsStudentCount);		
			dto.setInCityCount(inCityCount);
			dto.setNotCityCount(notCityCount);
			dto.setNotHkStuCount(notHkStuCount);
			dto.setMigrationStuCount(migrationStuCount);
			dto.setSyMigrationStuCount(syMigrationStuCount);
			dto.setStayinStuCount(stayinStuCount);
			dto.setRegularClassStuCount(regularClassStuCount);
			dto.setNormalStuCount(normalStuCount);
			dto.setCompatriotsCount(compatriotsCount);
			dto.setBoardingStuCount(boardingStuCount);
			dto.setNowStateStuCount(nowStateStuCount);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	
	//全市初高
	@RequestMapping("/cityHign")
	public String cityHign(HttpServletRequest req, ModelMap map){
		section = NumberUtils.toInt(req.getParameter("section"));
		List<GradeCountDto> dtoList = unitHighCountDtoList();
		map.put("dtoList", dtoList);
		map.put("section", section);
		return "/newstusys/edu/student/report/cityHign.ftl";
	}
	
	@RequestMapping("/cityHignExport")
	public void cityHignExport(HttpServletRequest req, HttpServletResponse response){
		section = NumberUtils.toInt(req.getParameter("section"));
		HSSFWorkbook workbook = new HSSFWorkbook();	
		String exName = "在校生统计表（全市"+(section==2?"初中":"高中")+"）";
		HSSFSheet sheet = workbook.createSheet(exName);
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 12);// 字体大小
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
		CellRangeAddress car11 = new CellRangeAddress(0,0,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car12 = new CellRangeAddress(0,1,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
	    sheet.addMergedRegion(car12);
	    HSSFCell cell12 = row1.createCell(2);
		cell12.setCellStyle(style);
		cell12.setCellValue(new HSSFRichTextString("年级"));
		
		CellRangeAddress car14 = new CellRangeAddress(0,0,3,7);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
	    sheet.addMergedRegion(car14);
		HSSFCell cell14 = row1.createCell(3);
		cell14.setCellStyle(style);
		cell14.setCellValue(new HSSFRichTextString("实际学生总数"));
		
		CellRangeAddress car15 = new CellRangeAddress(0,0,8,16);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
	    sheet.addMergedRegion(car15);
		HSSFCell cell15 = row1.createCell(8);
		cell15.setCellStyle(style);
		cell15.setCellValue(new HSSFRichTextString("其他各类学生数"));
		
		HSSFRow row2 = sheet.createRow(1);
		String[] titles = new String[] {"所在镇乡（街道）","校点名称","", "班级总数","学生总数","诸暨户籍学生","市外户籍","无户口学生",
				"随迁子女（总）","随迁子女（省外）","留守儿童","随班就读","外籍学生","港澳台生","待转入学生","住宿学生","备注"};
		for (int i = 0; i < titles.length; i++) {
			if (i != 2) {
				sheet.setColumnWidth(i, 20 * 256);
			}
			HSSFCell cell21 = row2.createCell(i);
			cell21.setCellStyle(style);
			cell21.setCellValue(new HSSFRichTextString(titles[i]));
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
		List<GradeCountDto> dtoList = unitHighCountDtoList();
		int i=2;
		for(GradeCountDto dto : dtoList){
			HSSFRow row3 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell31 = row3.createCell(j++);
			cell31.setCellStyle(style2);
			cell31.setCellValue(new HSSFRichTextString(dto.getAddress()));
			
			HSSFCell cell32 = row3.createCell(j++);
			cell32.setCellStyle(style2);
			cell32.setCellValue(new HSSFRichTextString(dto.getSchoolName()));
			
			HSSFCell cell33 = row3.createCell(j++);
			cell33.setCellStyle(style2);
			cell33.setCellValue(new HSSFRichTextString(dto.getGradeName()));
			
			HSSFCell cell35 = row3.createCell(j++);
			cell35.setCellStyle(style2);
			cell35.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClassCount())));
			
			HSSFCell cell66 = row3.createCell(j++);
			cell66.setCellStyle(style2);
			cell66.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClsStudentCount())));
			
			HSSFCell cell36 = row3.createCell(j++);
			cell36.setCellStyle(style2);
			cell36.setCellValue(new HSSFRichTextString(String.valueOf(dto.getInCityCount())));
			
			HSSFCell cell37 = row3.createCell(j++);
			cell37.setCellStyle(style2);
			cell37.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotCityCount())));
			
			HSSFCell cell38 = row3.createCell(j++);
			cell38.setCellStyle(style2);
			cell38.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotHkStuCount())));
			
			HSSFCell cell39 = row3.createCell(j++);
			cell39.setCellStyle(style2);
			cell39.setCellValue(new HSSFRichTextString(String.valueOf(dto.getMigrationStuCount())));
			
			HSSFCell cell40 = row3.createCell(j++);
			cell40.setCellStyle(style2);
			cell40.setCellValue(new HSSFRichTextString(String.valueOf(dto.getSyMigrationStuCount())));
			
			HSSFCell cell41 = row3.createCell(j++);
			cell41.setCellStyle(style2);
			cell41.setCellValue(new HSSFRichTextString(String.valueOf(dto.getStayinStuCount())));
			
			HSSFCell cell42 = row3.createCell(j++);
			cell42.setCellStyle(style2);
			cell42.setCellValue(new HSSFRichTextString(String.valueOf(dto.getRegularClassStuCount())));
			
			HSSFCell cell43 = row3.createCell(j++);
			cell43.setCellStyle(style2);
			cell43.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNormalStuCount())));
			
			HSSFCell cell44 = row3.createCell(j++);
			cell44.setCellStyle(style2);
			cell44.setCellValue(new HSSFRichTextString(String.valueOf(dto.getCompatriotsCount())));
			
			HSSFCell cell45 = row3.createCell(j++);
			cell45.setCellStyle(style2);
			cell45.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNowStateStuCount())));
			
			HSSFCell cell46 = row3.createCell(j++);
			cell46.setCellStyle(style2);
			cell46.setCellValue(new HSSFRichTextString(String.valueOf(dto.getBoardingStuCount())));
			
			HSSFCell cell47 = row3.createCell(j++);
			cell47.setCellStyle(style2);
			cell47.setCellValue(new HSSFRichTextString(""));
		}
		ExportUtils.outputData(workbook, exName, response);
	}
	
	public List<GradeCountDto> unitHighCountDtoList(){
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
		int clsStudentCount = 0;
		int inCityCount = 0;
		int notCityCount = 0;
		int notHkStuCount = 0;
		int migrationStuCount = 0;
		int syMigrationStuCount = 0;
		int stayinStuCount = 0;
		int regularClassStuCount = 0;
		int normalStuCount = 0;			
		int compatriotsCount = 0;
		int nowStateStuCount = 0;
		int boardingStuCount = 0;
		int classCount = 0;
		if(CollectionUtils.isNotEmpty(unitIdSet)){
			Map<String, List<Grade>> gradeMap = SUtils.dt(gradeRemoteService.findBySchoolIdMap(unitIdSet.toArray(new String[0])), new TypeReference<Map<String, List<Grade>>>(){});
			Set<String> gradeIdSet = new HashSet<String>();
			for(String key : gradeMap.keySet()){
		    	List<Grade> gradeList = gradeMap.get(key);
		    	for(Grade grade : gradeList){
		    		if(grade.getSection() == section){//grade.getSection()==2 || grade.getSection()==3  			
		    			gradeIdSet.add(grade.getId());
		    		}
		    	}
		    }
			Map<String, Integer> classCountMap = new HashMap<String, Integer>();//班级总数
			Map<String, Integer> clsStudentMap = new HashMap<String, Integer>();//班级学生数
			Map<String, Integer> inCityCountMap = new HashMap<String, Integer>();//市内学生总数			
			Map<String, Integer> notCityCountMap = new HashMap<String, Integer>();//市外学生数
			Map<String, Integer> notHkStuCountMap = new HashMap<String, Integer>();//无户口学生数
			Map<String, Integer> migrationStuCountMap = new HashMap<String, Integer>();//随迁子女数总
			Map<String, Integer> syMigrationStuCountMap = new HashMap<String, Integer>();//随迁子女数省外
			Map<String, Integer> stayinStuCountMap = new HashMap<String, Integer>();//留守儿童
			Map<String, Integer> regularClassStuCountMap = new HashMap<String, Integer>();//随班就读
			Map<String, Integer> normalStuCountMap = new HashMap<String, Integer>();//外籍学生数
			Map<String, Integer> compatriotsCountMap = new HashMap<String, Integer>();//港澳台生数
			Map<String, Integer> nowStateStuCountMap = new HashMap<String, Integer>();//待转入学生数
			Map<String, Integer> boardingStuCountMap = new HashMap<String, Integer>();//住宿学生数
			if(CollectionUtils.isNotEmpty(gradeIdSet)){
				classCountMap = studentReportService.classCountByGrade(gradeIdSet.toArray(new String[0]));
				clsStudentMap = studentReportService.stuCountByGrade(gradeIdSet.toArray(new String[0]));
				inCityCountMap = studentReportService.inCityCountByGrade(gradeIdSet.toArray(new String[0]));
				notCityCountMap = studentReportService.notCityCountByGrade(gradeIdSet.toArray(new String[0]));
				notHkStuCountMap = studentReportService.notHkCountByGrade(gradeIdSet.toArray(new String[0]));
				migrationStuCountMap = studentReportService.migrationStuCountByGrade(gradeIdSet.toArray(new String[0]));
				syMigrationStuCountMap = studentReportService.sYmigrationStuCountByGrade(gradeIdSet.toArray(new String[0]));
				stayinStuCountMap = studentReportService.stayinStuCountByGrade(gradeIdSet.toArray(new String[0]));
				regularClassStuCountMap = studentReportService.regularClassStuCountByGrade(gradeIdSet.toArray(new String[0]));
				normalStuCountMap = studentReportService.normalStuCountByGrade(gradeIdSet.toArray(new String[0]));
				compatriotsCountMap = studentReportService.compatriotsCountByGrade(gradeIdSet.toArray(new String[0]));
				nowStateStuCountMap = studentReportService.nowStateStuCountByGrade(gradeIdSet.toArray(new String[0]));
				boardingStuCountMap = studentReportService.boardingStuCountByGrade(gradeIdSet.toArray(new String[0]));
			}
			for(String key : gradeMap.keySet()){
		    	List<Grade> gradeList = gradeMap.get(key);
		    	for(Grade grade : gradeList){
		    		if(grade.getSection() == section){		    			
		    			GradeCountDto dto = new GradeCountDto();
		    			dto.setAddress(pUnitNameMap.get(punitIdMap.get(key)));
		    			dto.setSchoolName(unitNameMap.get(key));
		    			dto.setGradeName(grade.getGradeName());
		    			dto.setGradeCode(grade.getGradeCode());
		    			if(null!=classCountMap.get(grade.getId())){
		    				classCount = classCount + classCountMap.get(grade.getId());
		    				dto.setClassCount(classCountMap.get(grade.getId()));
		    			}
		    			if(null!=clsStudentMap.get(grade.getId())){
		    				clsStudentCount = clsStudentCount +clsStudentMap.get(grade.getId());
		    				dto.setClsStudentCount(clsStudentMap.get(grade.getId()));
		    			}
		    			if(null!=inCityCountMap.get(grade.getId())){	
		    				inCityCount = inCityCount + inCityCountMap.get(grade.getId());
		    				dto.setInCityCount(inCityCountMap.get(grade.getId()));
		    			}
		    			if(null!=notCityCountMap.get(grade.getId())){	
		    				notCityCount = notCityCount + notCityCountMap.get(grade.getId());
		    				dto.setNotCityCount(notCityCountMap.get(grade.getId()));
		    			}
		    			if(null!=notHkStuCountMap.get(grade.getId())){
		    				notHkStuCount = notHkStuCount + notHkStuCountMap.get(grade.getId());
		    				dto.setNotHkStuCount(notHkStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=migrationStuCountMap.get(grade.getId())){	
		    				migrationStuCount = migrationStuCount + migrationStuCountMap.get(grade.getId());
		    				dto.setMigrationStuCount(migrationStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=syMigrationStuCountMap.get(grade.getId())){	
		    				syMigrationStuCount = syMigrationStuCount + syMigrationStuCountMap.get(grade.getId());
		    				dto.setSyMigrationStuCount(syMigrationStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=stayinStuCountMap.get(grade.getId())){	
		    				stayinStuCount = stayinStuCount + stayinStuCountMap.get(grade.getId());
		    				dto.setStayinStuCount(stayinStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=regularClassStuCountMap.get(grade.getId())){		
		    				regularClassStuCount = regularClassStuCount + regularClassStuCountMap.get(grade.getId());
		    				dto.setRegularClassStuCount(regularClassStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=normalStuCountMap.get(grade.getId())){	
		    				normalStuCount = normalStuCount + normalStuCountMap.get(grade.getId());
		    				dto.setNormalStuCount(normalStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=compatriotsCountMap.get(grade.getId())){	
		    				compatriotsCount = compatriotsCount + compatriotsCountMap.get(grade.getId());
		    				dto.setCompatriotsCount(compatriotsCountMap.get(grade.getId()));
		    			}
		    			if(null!=nowStateStuCountMap.get(grade.getId())){
		    				nowStateStuCount = nowStateStuCount + nowStateStuCountMap.get(grade.getId());
		    				dto.setNowStateStuCount(nowStateStuCountMap.get(grade.getId()));
		    			}
		    			if(null!=boardingStuCountMap.get(grade.getId())){
		    				boardingStuCount = boardingStuCount + boardingStuCountMap.get(grade.getId());
		    				dto.setBoardingStuCount(boardingStuCountMap.get(grade.getId()));
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
			dto.setClassCount(classCount);
			dto.setClsStudentCount(clsStudentCount);		
			dto.setInCityCount(inCityCount);
			dto.setNotCityCount(notCityCount);
			dto.setNotHkStuCount(notHkStuCount);
			dto.setMigrationStuCount(migrationStuCount);
			dto.setSyMigrationStuCount(syMigrationStuCount);
			dto.setStayinStuCount(stayinStuCount);
			dto.setRegularClassStuCount(regularClassStuCount);
			dto.setNormalStuCount(normalStuCount);
			dto.setCompatriotsCount(compatriotsCount);
			dto.setBoardingStuCount(boardingStuCount);
			dto.setNowStateStuCount(nowStateStuCount);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	
	//汇总统计表
	@RequestMapping("/allCount")
	public String allCount(ModelMap map){
		List<ClazzCountDto> dtoList = allClazzCountDtoList();
		map.put("dtoList", dtoList);
		return "/newstusys/edu/student/report/allCount.ftl";
	}
	
	@RequestMapping("/allClazzCountDtoExport")
	public void allClazzCountDtoExport(HttpServletResponse response){
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet("汇总统计表");
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 12);// 字体大小
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
		CellRangeAddress car11 = new CellRangeAddress(0,1,0,0);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString("学校名称"));
		
		CellRangeAddress car12 = new CellRangeAddress(0,1,1,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
	    sheet.addMergedRegion(car12);
		HSSFCell cell12 = row1.createCell(1);
		cell12.setCellStyle(style);
		cell12.setCellValue(new HSSFRichTextString("年级"));
		
		CellRangeAddress car13 = new CellRangeAddress(0,1,2,2);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car13, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car13, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car13, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car13, sheet); 
	    sheet.addMergedRegion(car13);
		HSSFCell cell13 = row1.createCell(2);
		cell13.setCellStyle(style);
		cell13.setCellValue(new HSSFRichTextString("班级"));
		
		CellRangeAddress car14 = new CellRangeAddress(0,1,3,3);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
	    sheet.addMergedRegion(car14);
		HSSFCell cell14 = row1.createCell(3);
		cell14.setCellStyle(style);
		cell14.setCellValue(new HSSFRichTextString("学生数"));
		
		CellRangeAddress car15 = new CellRangeAddress(0,0,4,10);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
	    sheet.addMergedRegion(car15);
		HSSFCell cell15 = row1.createCell(4);
		cell15.setCellStyle(style);
		cell15.setCellValue(new HSSFRichTextString("其中"));
		
		HSSFRow row2 = sheet.createRow(1);
		String[] titles = new String[] {"","","","","市外生数", "随迁子女数","留守儿童数","随班就读数","住宿生数","未报户口学生","外籍学生"};
		for (int i = 0; i < titles.length; i++) {
			if (i == 0 || i == 2) {
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.setColumnWidth(i, 15 * 256);
			}
			HSSFCell cell21 = row2.createCell(i);
			cell21.setCellStyle(style);
			cell21.setCellValue(new HSSFRichTextString(titles[i]));
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
		List<ClazzCountDto> dtoList =  allClazzCountDtoList();
		int i=2;
		for(ClazzCountDto dto : dtoList){
			HSSFRow row3 = sheet.createRow(i++);
			int j=0;
			HSSFCell cell31 = row3.createCell(j++);
			cell31.setCellStyle(style2);
			cell31.setCellValue(new HSSFRichTextString(dto.getSchoolName()));
			
			HSSFCell cell32 = row3.createCell(j++);
			cell32.setCellStyle(style2);
			cell32.setCellValue(new HSSFRichTextString(dto.getGradeName()));
			
			HSSFCell cell33 = row3.createCell(j++);
			cell33.setCellStyle(style2);
			cell33.setCellValue(new HSSFRichTextString(dto.getClassName()));
			
			HSSFCell cell34 = row3.createCell(j++);
			cell34.setCellStyle(style2);
			cell34.setCellValue(new HSSFRichTextString(String.valueOf(dto.getClsStudentCount())));
			
			HSSFCell cell35 = row3.createCell(j++);
			cell35.setCellStyle(style2);
			cell35.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotCityCount())));
			
			HSSFCell cell36 = row3.createCell(j++);
			cell36.setCellStyle(style2);
			cell36.setCellValue(new HSSFRichTextString(String.valueOf(dto.getMigrationStuCount())));
			
			HSSFCell cell37 = row3.createCell(j++);
			cell37.setCellStyle(style2);
			cell37.setCellValue(new HSSFRichTextString(String.valueOf(dto.getStayinStuCount())));
			
			HSSFCell cell38 = row3.createCell(j++);
			cell38.setCellStyle(style2);
			cell38.setCellValue(new HSSFRichTextString(String.valueOf(dto.getRegularClassStuCount())));
			
			HSSFCell cell39 = row3.createCell(j++);
			cell39.setCellStyle(style2);
			cell39.setCellValue(new HSSFRichTextString(String.valueOf(dto.getBoardingStuCount())));
			
			HSSFCell cell310 = row3.createCell(j++);
			cell310.setCellStyle(style2);
			cell310.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNotHkStuCount())));
			
			HSSFCell cell311 = row3.createCell(j++);
			cell311.setCellStyle(style2);
			cell311.setCellValue(new HSSFRichTextString(String.valueOf(dto.getNormalStuCount())));
		}
		
		ExportUtils.outputData(workbook, "汇总统计表", response);
	}
	
	public List<ClazzCountDto> allClazzCountDtoList(){
		List<ClazzCountDto> dtoList = new ArrayList<>();
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);
		List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});	
		Set<String> unitIdSet = new HashSet<String>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		for(Unit u : unitList){
			unitIdSet.add(u.getId());
			unitNameMap.put(u.getId(), u.getUnitName());
		}
		if(CollectionUtils.isNotEmpty(unitIdSet)){			
			List<Clazz> clsList = SUtils.dt(classRemoteService.findBySchoolIdIn(unitIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
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
//			Map<String, Integer> numMap = new HashMap<String, Integer>();
			Map<String, Integer> clsStudentMap = new HashMap<String, Integer>();//班级学生数
			Map<String, Integer> inCityCountMap = new HashMap<String, Integer>();//市内学生总数			
			Map<String, Integer> notCityCountMap = new HashMap<String, Integer>();//市外学生数
			Map<String, Integer> notHkStuCountMap = new HashMap<String, Integer>();//无户口学生数
			Map<String, Integer> migrationStuCountMap = new HashMap<String, Integer>();//随迁子女数总
			Map<String, Integer> syMigrationStuCountMap = new HashMap<String, Integer>();//随迁子女数省外
			Map<String, Integer> stayinStuCountMap = new HashMap<String, Integer>();//留守儿童
			Map<String, Integer> regularClassStuCountMap = new HashMap<String, Integer>();//随班就读
			Map<String, Integer> normalStuCountMap = new HashMap<String, Integer>();//外籍学生数
			Map<String, Integer> compatriotsCountMap = new HashMap<String, Integer>();//港澳台生数
			Map<String, Integer> nowStateStuCountMap = new HashMap<String, Integer>();//待转入学生数
			Map<String, Integer> boardingStuCountMap = new HashMap<String, Integer>();//住宿学生数
			if(CollectionUtils.isNotEmpty(clsIdSet)){				
//				numMap = SUtils.dt(studentRemoteService.countMapByClassIds(clsIdSet.toArray(new String[0])), new TR<Map<String, Integer>>(){});
				clsStudentMap = studentReportService.stuCountByClass(clsIdSet.toArray(new String[0]));
				inCityCountMap = studentReportService.inCityStuCountByClass(clsIdSet.toArray(new String[0]));
				notCityCountMap = studentReportService.notInCityStuCountByClass(clsIdSet.toArray(new String[0]));
				notHkStuCountMap = studentReportService.notHkStuCountByClass(clsIdSet.toArray(new String[0]));
				migrationStuCountMap = studentReportService.migrationStuCountByClass(clsIdSet.toArray(new String[0]));
				syMigrationStuCountMap = studentReportService.sYmigrationStuCountByClass(clsIdSet.toArray(new String[0]));
				stayinStuCountMap = studentReportService.stayinStuCountByClass(clsIdSet.toArray(new String[0]));
				regularClassStuCountMap = studentReportService.regularClassStuCountByClass(clsIdSet.toArray(new String[0]));
				normalStuCountMap = studentReportService.normalStuCountByClass(clsIdSet.toArray(new String[0]));
				compatriotsCountMap = studentReportService.compatriotsCountByClass(clsIdSet.toArray(new String[0]));
				nowStateStuCountMap = studentReportService.nowStateStuCountByClass(clsIdSet.toArray(new String[0]));
				boardingStuCountMap = studentReportService.boardingStuCountByClass(clsIdSet.toArray(new String[0]));
			}
			int clsStudentCount = 0;
			int inCityCount = 0;
			int notCityCount = 0;
			int notHkStuCount = 0;
			int migrationStuCount = 0;
			int syMigrationStuCount = 0;
			int stayinStuCount = 0;
			int regularClassStuCount = 0;
			int normalStuCount = 0;			
			int compatriotsCount = 0;
			int nowStateStuCount = 0;
			int boardingStuCount = 0;
			for(Clazz cls : clsList){
				ClazzCountDto dto = new ClazzCountDto();
				dto.setClassName(cls.getClassNameDynamic());
				dto.setGradeName(gradeNameMap.get(cls.getGradeId()));
				dto.setSchoolName(unitNameMap.get(cls.getSchoolId()));
				dto.setSection(cls.getSection());
				dto.setClassCode(cls.getClassCode());
				dto.setOpenAcadyear(cls.getAcadyear());
				if(null!=clsStudentMap.get(cls.getId())){
					clsStudentCount = clsStudentCount + clsStudentMap.get(cls.getId());
					dto.setClsStudentCount(clsStudentMap.get(cls.getId()));
				}
				if(null!=inCityCountMap.get(cls.getId())){	
					inCityCount = inCityCount + inCityCountMap.get(cls.getId());
					dto.setInCityCount(inCityCountMap.get(cls.getId()));
				}
				if(null!=notCityCountMap.get(cls.getId())){	
					notCityCount = notCityCount + notCityCountMap.get(cls.getId());
					dto.setNotCityCount(notCityCountMap.get(cls.getId()));
				}
				if(null!=notHkStuCountMap.get(cls.getId())){
					notHkStuCount = notHkStuCount + notHkStuCountMap.get(cls.getId());
					dto.setNotHkStuCount(notHkStuCountMap.get(cls.getId()));
				}
				if(null!=migrationStuCountMap.get(cls.getId())){	
					migrationStuCount = migrationStuCount + migrationStuCountMap.get(cls.getId());
					dto.setMigrationStuCount(migrationStuCountMap.get(cls.getId()));
				}
				if(null!=syMigrationStuCountMap.get(cls.getId())){	
					syMigrationStuCount = syMigrationStuCount + syMigrationStuCountMap.get(cls.getId());
					dto.setSyMigrationStuCount(syMigrationStuCountMap.get(cls.getId()));
				}
				if(null!=stayinStuCountMap.get(cls.getId())){	
					stayinStuCount = stayinStuCount + stayinStuCountMap.get(cls.getId());
					dto.setStayinStuCount(stayinStuCountMap.get(cls.getId()));
				}
				if(null!=regularClassStuCountMap.get(cls.getId())){	
					regularClassStuCount = regularClassStuCount + regularClassStuCountMap.get(cls.getId());
					dto.setRegularClassStuCount(regularClassStuCountMap.get(cls.getId()));
				}
				if(null!=normalStuCountMap.get(cls.getId())){	
					normalStuCount = normalStuCount + normalStuCountMap.get(cls.getId());
					dto.setNormalStuCount(normalStuCountMap.get(cls.getId()));
				}
				if(null!=compatriotsCountMap.get(cls.getId())){		
					compatriotsCount = compatriotsCount + compatriotsCountMap.get(cls.getId());
					dto.setCompatriotsCount(compatriotsCountMap.get(cls.getId()));
				}
				if(null!=nowStateStuCountMap.get(cls.getId())){	
					nowStateStuCount = nowStateStuCount + nowStateStuCountMap.get(cls.getId());
					dto.setNowStateStuCount(nowStateStuCountMap.get(cls.getId()));
				}
				if(null!=boardingStuCountMap.get(cls.getId())){	
					boardingStuCount = boardingStuCount + boardingStuCountMap.get(cls.getId());
					dto.setBoardingStuCount(boardingStuCountMap.get(cls.getId()));
				}
				dtoList.add(dto);
			}
			Collections.sort(dtoList,new Comparator<ClazzCountDto>() {
	            @Override
	            public int compare(ClazzCountDto o1, ClazzCountDto o2) {
	                String street1 = o1.getSchoolName();
	                String street2 = o2.getSchoolName();
	                if(!StringUtils.equals(street1, street2)) {// 学校
	                	return StringUtils.trimToEmpty(street1).compareTo(street2);
	                }
	                
	                int sec1 = o1.getSection();
	                int sec2 = o2.getSection();
	                if(sec1!=sec2) {// 学校
	                	return sec1-sec2;
	                }
	                
	                street1 = o1.getOpenAcadyear();
	                street2 = o2.getOpenAcadyear();
	                if(!StringUtils.equals(street1, street2)) {
	                	return StringUtils.trimToEmpty(street2).compareTo(street1);
	                }
	                
	                street1 = o1.getClassCode();
	                street2 = o2.getClassCode();
	                if(!StringUtils.equals(street1, street2)) {// 学校
	                	return StringUtils.trimToEmpty(street1).compareTo(street2);
	                }
	                	                
	                return StringUtils.trimToEmpty(street1).compareTo(street2);

	            }
	        });
			if(CollectionUtils.isNotEmpty(dtoList)){			
				ClazzCountDto dto = new ClazzCountDto();
				dto.setSchoolName("小计");
				dto.setClsStudentCount(clsStudentCount);		
				dto.setInCityCount(inCityCount);
				dto.setNotCityCount(notCityCount);
				dto.setNotHkStuCount(notHkStuCount);
				dto.setMigrationStuCount(migrationStuCount);
				dto.setSyMigrationStuCount(syMigrationStuCount);
				dto.setStayinStuCount(stayinStuCount);
				dto.setRegularClassStuCount(regularClassStuCount);
				dto.setNormalStuCount(normalStuCount);
				dto.setCompatriotsCount(compatriotsCount);
				dto.setBoardingStuCount(boardingStuCount);
				dto.setNowStateStuCount(nowStateStuCount);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
