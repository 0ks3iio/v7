package net.zdsoft.evaluation.data.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dto.EvaluateTableDto;
import net.zdsoft.evaluation.data.dto.ResultStatDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateProjectService;
import net.zdsoft.evaluation.data.service.TeachEvaluateRelationService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultStatService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ZipUtils;
import net.zdsoft.tutor.remote.service.TutorRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/evaluate/stat")
public class TeachEvaluateStatAction extends BaseAction {
	@Autowired
	private TeachEvaluateProjectService teachEvaluateProjectService;
	@Autowired
	private TeachEvaluateResultStatService teachEvaluateResultStatService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired 
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachEvaluateRelationService teachEvaluateRelationService;
	@Autowired
	private TutorRemoteService tutorRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@RequestMapping("/statResult/exportZip")
	@ResponseBody
	@ControllerInfo("班级or个人打包Zip")
    public String exportClassStu(String projectId,String evaluateType, HttpServletResponse resp){
        //创建文件夹
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		String dirName = project.getProjectName() + "统计汇总";
        File dirFile = new File(dirName);
        if(dirFile.exists()){
        	dirFile.delete();
        }
        dirFile.mkdirs();
        List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		Map<String,Float> clsItemScoreMap= teachEvaluateResultStatService.getResultStatAllDto(projectId,project.getEvaluateType());
		List<String> worklist = new ArrayList<>();
		Set<String> teaIds = new HashSet<>();
		if(StringUtils.equals(project.getEvaluateType(), EvaluationConstants.EVALUATION_TYPE_TEACHER)) {
			Set<String> clsIds = new HashSet<>();
			Map<String,String> map = new HashMap<>();
			for (String key : clsItemScoreMap.keySet()) {
				String[] arrIds = key.split("_");
				teaIds.add(arrIds[1]);
				clsIds.add(arrIds[2]);
				map.put(arrIds[2], arrIds[1]);
				
			}
			List<Clazz> clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])),new TR<List<Clazz>>() {});
			Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>() {}), Teacher::getId);
			for (Clazz cla : clslist) {
				String teaId = map.get(cla.getId());
				if(teaId != null && teaMap.containsKey(teaId)) {
					worklist.add(cla.getClassNameDynamic()+"("+teaMap.get(teaId).getTeacherName()+")"+","+teaId+"_"+cla.getId());
				}else {
					worklist.add(cla.getClassNameDynamic()+"(无教师)"+","+teaId+"_"+cla.getId());
				}
			}
		}else {
			for (String key : clsItemScoreMap.keySet()) {
				String[] arrIds = key.split("_");
				teaIds.add(arrIds[1]);
			}
			List<Teacher> teaList = SUtils.dt(teacherRemoteService.findListByIds(teaIds.toArray(new String[0])),new TR<List<Teacher>>() {});
			for (Teacher teacher : teaList) {
				worklist.add(teacher.getTeacherName()+","+teacher.getId());
			}
		}
		Map<String,List<Map<String,String>>> txtDtoMap = teachEvaluateResultService.getResultTxtDto(projectId,project.getEvaluateType());
		
        //excel表相关通过格式及内容
        List<String> titleSelectList = new ArrayList<>();
        titleSelectList.add("序号");
        titleSelectList.add("项目名称");
        titleSelectList.add("选项");
        titleSelectList.add("比例（%）");
        titleSelectList.add("得分");
        int hbRow = 1;
        List<CellRangeAddress> craList = new ArrayList<>();
		for (int a=0;a < dto.getEvaluaList().size(); a++) {
			for (int k = 0; k < titleSelectList.size(); k++) {
				if(k!=2&&k!=3) {
					CellRangeAddress cra =  new CellRangeAddress(hbRow,hbRow+dto.getEvaluaList().get(a).getOptionList().size() - 1,k,k);
					craList.add(cra);
				}
			}
			hbRow = hbRow+dto.getEvaluaList().get(a).getOptionList().size();
		}
		int hbRow2 = hbRow;
		for (int a=0;a < dto.getFillList().size(); a++) {
			for (TeachEvaluateItemOption option : dto.getFillList().get(a).getOptionList()) {
				CellRangeAddress cra =  new CellRangeAddress(hbRow2,hbRow2,3,4);
				craList.add(cra);
				hbRow2++;
			}
			for (int k = 0; k < 2; k++) {
				CellRangeAddress cra =  new CellRangeAddress(hbRow,hbRow+dto.getFillList().get(a).getOptionList().size() - 1,k,k);
				craList.add(cra);
			}
			hbRow = hbRow+dto.getEvaluaList().get(a).getOptionList().size();
		}
        
        HSSFWorkbook workbook = null;
        HSSFCellStyle style = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell titleCell = null;
		//每班为一个excel
		List<String> sheetNameList = new ArrayList<>();
		sheetNameList.add("选择题汇总");
		sheetNameList.add("解答题汇总");
		for(String workStr : worklist) {
			String[] workArr = workStr.split(",");
			String workName = workArr[0];
			String resultStr = workArr[1];
			workbook = new HSSFWorkbook();
			style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);//水平
			style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
			// 设置边框
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);  
			style.setWrapText(true);//自动换行
			//每个类型为一个sheet
				for(String sName :sheetNameList){
					sheet = workbook.createSheet(sName);
					sheet.setDefaultColumnWidth(20);
					titleRow = sheet.createRow(0);
					titleRow.setHeightInPoints(2*sheet.getDefaultRowHeightInPoints());
					if(StringUtils.equals(sName, "选择题汇总")) {
						for (int i = 0; i < titleSelectList.size(); i++) {
							String selectName = titleSelectList.get(i);
							titleCell = titleRow.createCell(i);
							titleCell.setCellValue(new HSSFRichTextString(selectName));
							titleCell.setCellStyle(style);
						}
						//合并单元格
						for (CellRangeAddress cellRangeAddress : craList) {
							sheet.addMergedRegion(cellRangeAddress);
						}
						int index = 0;
						int i = 1;
						for (TeachEvaluateItem item : dto.getEvaluaList()) {
							List<TeachEvaluateItemOption> options = item.getOptionList();
							index++;
							for (TeachEvaluateItemOption option : options) {
								HSSFRow rowListRow = sheet.createRow(i);
								i++;
								for (int k = 0; k < titleSelectList.size(); k++) {
									HSSFCell cell = rowListRow.createCell(k);
									if(k==0) {
										cell.setCellValue(new HSSFRichTextString(index+""));
									}else if(k==1) {
										cell.setCellValue(new HSSFRichTextString(item.getItemName()));
									}else if(k==2) {
										cell.setCellValue(new HSSFRichTextString(option.getOptionName()));
									}else if(k==3) {
										if(clsItemScoreMap.containsKey(option.getId()+"_"+resultStr)) {
											cell.setCellValue(new HSSFRichTextString(clsItemScoreMap.get(option.getId()+"_"+resultStr)+""));
										}else {
											cell.setCellValue(new HSSFRichTextString("0"));
										}
									}else if(k==4) {
										if(clsItemScoreMap.containsKey(item.getId()+"_"+resultStr)) {
											cell.setCellValue(new HSSFRichTextString(clsItemScoreMap.get(item.getId()+"_"+resultStr)+""));
										}else {
											cell.setCellValue(new HSSFRichTextString("0"));
										}
									}
									cell.setCellStyle(style);
								}
							}
						}
						for (TeachEvaluateItem item : dto.getFillList()) {
							List<TeachEvaluateItemOption> options = item.getOptionList();
							index++;
							for (TeachEvaluateItemOption option : options) {
								HSSFRow rowListRow = sheet.createRow(i);
								i++;
								for (int k = 0; k < titleSelectList.size(); k++) {
									HSSFCell cell = rowListRow.createCell(k);
									if(k==0) {
										cell.setCellValue(new HSSFRichTextString(index+""));
									}else if(k==1) {
										cell.setCellValue(new HSSFRichTextString(item.getItemName()));
									}else if(k==2) {
										cell.setCellValue(new HSSFRichTextString(option.getOptionName()));
									}else if(k==3) {
										if(clsItemScoreMap.containsKey(option.getId()+"_"+resultStr)) {
											cell.setCellValue(new HSSFRichTextString(clsItemScoreMap.get(option.getId()+"_"+resultStr)+""));
										}else {
											cell.setCellValue(new HSSFRichTextString("无"));
										}
									}else if(k==4) {
										cell.setCellValue(new HSSFRichTextString());
									}
									cell.setCellStyle(style);
								}
							}
						}
					}else {
						for (int i = 0; i < dto.getAnswerList().size(); i++) {
							TeachEvaluateItem item = dto.getAnswerList().get(i);
							titleCell = titleRow.createCell(i);
							titleCell.setCellValue(new HSSFRichTextString(item.getItemNo()+"、"+item.getItemName()));
							titleCell.setCellStyle(style);
						}
						if(txtDtoMap.containsKey(resultStr)) {
							List<Map<String,String>> relist = txtDtoMap.get(resultStr);
							int row = 1;
							for (Map<String, String> map : relist) {
								HSSFRow hRow = sheet.createRow(row);
								row++;
								for (int i = 0; i < dto.getAnswerList().size(); i++) {
									TeachEvaluateItem item = dto.getAnswerList().get(i);
									titleCell = hRow.createCell(i);
									titleCell.setCellValue(new HSSFRichTextString(map.containsKey(item.getId())?map.get(item.getId()):""));
									titleCell.setCellStyle(style);
								}
							}
						}
					}
				}
			//写入文件
			File file = new File(dirFile,workName+".xls");
			try {
				OutputStream stream = new FileOutputStream(file);
				workbook.write(stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			String zip = ZipUtils.makeZip(dirName);
			ExportUtils.outputFile(zip,resp);
			new File(zip).delete();
			FileUtils.deleteDirectory(dirFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnSuccess();
    }

	@RequestMapping("/statTeaRankResult/page")
	@ControllerInfo(value = "班主任/导师--排名")
	public String showTeaRank(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		List<ResultStatDto> dtolist = teachEvaluateResultStatService.findTeaRankBy(projectId,project.getEvaluateType());
		map.put("dtolist", dtolist);
		return "/evaluation/stat/statResultTeaRank.ftl";
	}
	
	@RequestMapping("/statTeaClsResult/page")
	@ControllerInfo(value = "班主任/导师--班级维度")
	public String showTeableClsTea(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		String itemType = request.getParameter("itemType");
		if(StringUtils.isBlank(itemType)){
			itemType = "0";
		}
		map.put("itemType", itemType);
		if(StringUtils.equals(project.getEvaluateType(), EvaluationConstants.EVALUATION_TYPE_TOTOR)){
			String teaId = request.getParameter("teaId");
			map.put("teaId", teaId);
			if(StringUtils.isBlank(teaId)){
				map.put("stuCount", 0f);
				map.put("countScore", 0f);
				map.put("clsItemScoreMap", new HashMap<String,Float>());
				return "/evaluation/stat/statResultTeaCls.ftl";
			}
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teaId), Teacher.class);
			map.put("teaName", tea.getTeacherName());
			Set<String> stuIds = teachEvaluateResultService.getStuIdByProjectId(projectId,null, null, teaId);
			if(CollectionUtils.isNotEmpty(stuIds)){
				map.put("stuCount", stuIds.size());
			}else{
				map.put("stuCount", 0);
			}
			if(StringUtils.equals(itemType, "0")){
				Map<String,Float> clsItemScoreMap= teachEvaluateResultStatService.getResultStatTeaDto(projectId, teaId);
				map.put("clsItemScoreMap", clsItemScoreMap);
				float countScore = 0f;
				for (TeachEvaluateItem item : dto.getEvaluaList()) {
					if(clsItemScoreMap.containsKey(item.getId())){
						countScore = countScore + clsItemScoreMap.get(item.getId());
					}
				}
				map.put("countScore", countScore);
			}else{
				List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,null,null,teaId);
				map.put("txtDtolist", txtDtolist);
			}
		}else{
			String classId = request.getParameter("classId");
			map.put("classId", classId);
			if(StringUtils.isBlank(classId)){
				map.put("stuCount", 0);
				map.put("countScore", 0f);
				map.put("clsItemScoreMap", new HashMap<String,Float>());
				return "/evaluation/stat/statResultTeaCls.ftl";
			}
			String className = "";
			String teacherId = "";
			Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			className = cls.getClassNameDynamic();
			teacherId = cls.getTeacherId();
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
			if(tea != null) {
				map.put("teacherName", tea.getTeacherName());
			}else {
				map.put("teacherName", "");
			}
			
			map.put("className", className);
			Set<String> stuIds = teachEvaluateResultService.getStuIdByProjectId(projectId,classId, null, null);
			if(CollectionUtils.isNotEmpty(stuIds)){
				map.put("stuCount", stuIds.size());
			}else{
				map.put("stuCount", 0);
			}
			if(StringUtils.equals(itemType, "0")){
				Map<String,Float> clsItemScoreMap= teachEvaluateResultStatService.getResultStatTeaClsDto(projectId, classId);
				map.put("clsItemScoreMap", clsItemScoreMap);
				float countScore = 0f;
				for (TeachEvaluateItem item : dto.getEvaluaList()) {
					if(clsItemScoreMap.containsKey(item.getId())){
						countScore = countScore + clsItemScoreMap.get(item.getId());
					}
				}
				map.put("countScore", countScore);
			}else{
				List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,null,null,teacherId);
				map.put("txtDtolist", txtDtolist);
			}
		}
		return "/evaluation/stat/statResultTeaCls.ftl";
	}
	
	@RequestMapping("/statResultTxt/page")
	@ControllerInfo(value = "文本信息")
	public String showTeableTxt(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		map.put("projectType", project.getEvaluateType());
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		
		String teaId = request.getParameter("teaId");
		String clsId = request.getParameter("clsId");
		String subId = request.getParameter("subId");
		String teaName = "";
		if(StringUtils.isNotBlank(teaId)){
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teaId), Teacher.class);
			teaName = tea.getTeacherName();
		}
		map.put("teaId", teaId);
		map.put("teaName", teaName);
		
		if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())){
			String classId = request.getParameter("classId");
//			String className = "";
//			if(StringUtils.isNotBlank(classId)){
//				Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
//				className = cls.getClassNameDynamic();
//			}
			map.put("classId", classId);
//			map.put("className", className);
			String subjectId = request.getParameter("subjectId");
//			Map<String,Set<String>>stuSubMap = SUtils.dt(courseScheduleRemoteService.findCourseScheduleMap(project.getUnitId(), project.getAcadyear(), NumberUtils.toInt(project.getSemester()))
//					,new TR<Map<String,Set<String>>>(){});
//			Set<String> courseIds=new HashSet<String>();
//			for(Set<String> value : stuSubMap.values()){
//				for (String subTeaId : value) {
//					courseIds.add(subTeaId.split(",")[0]);
//				}
//			}
//			List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
			map.put("subjectId", subjectId);
			String teacherId = request.getParameter("teacherId");
			map.put("teacherId", teacherId);
			
//			map.put("courseList", courseList);
			List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,subId,clsId,teaId);
			map.put("txtDtolist", txtDtolist);
		}else if (StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
			List<ResultStatDto> txtDtolist = teachEvaluateResultService.getResultTxtDto(projectId,null,null,teaId);//15889594639546322492488144629842，77A5970A4B7A4918945012C79375A0CE
			map.put("txtDtolist", txtDtolist);
		}
		return "/evaluation/stat/statResultTxt.ftl";
	}
	
	@RequestMapping("/statResultSub/page")
	@ControllerInfo(value = "教师+学科维度（必须课）")
	public String showTableSub(HttpServletRequest request,ModelMap map){
		String evaluateType = request.getParameter("evaluateType");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		map.put("projectId", projectId);
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		String subjectId = request.getParameter("subjectId");
		String classId = request.getParameter("classId");
		map.put("classId", classId);
		List<TeachEvaluateRelation> relations = teachEvaluateRelationService.findByProjectIds(new String[] {projectId});
		Set<String> clsIds = new HashSet<>();
		Set<String> courseIds = new HashSet<>();
		for (TeachEvaluateRelation e : relations) {
			clsIds.add(e.getValueId());
			courseIds.add(e.getSubjectId());
		}
		List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
		String subId = request.getParameter("subId");
		if(StringUtils.isBlank(subId) && CollectionUtils.isNotEmpty(courseList)) {
//			subId = courseList.get(0).getId();
		}
		map.put("subjectId", subjectId);
		map.put("courseList", courseList);
		String teacherId = request.getParameter("teacherId");
		map.put("subId", subId);
		map.put("teacherId", teacherId);
		map.put("teacherName", "");
		if(StringUtils.isNotBlank(teacherId)) {
			Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
			if(tea !=null) {
				map.put("teacherName", tea.getTeacherName());
			}
		}
		//TODO
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		//取数据
		List<ResultStatDto> subDtoList = teachEvaluateResultStatService.getResultSubStat(projectId,EvaluationConstants.STATE__DIMENSION_SUBJECT,teacherId,subId);
		map.put("subDtoList", subDtoList);
		return "/evaluation/stat/statResultSub.ftl";
	}
	@RequestMapping("/statResult/page")
	@ControllerInfo(value = "班级维度")
	public String showTeable(HttpServletRequest request,ModelMap map) {
		String evaluateType = request.getParameter("evaluateType");
		String acadyear = request.getParameter("acadyear");
		map.put("evaluateType", evaluateType);
		String projectId = request.getParameter("projectId");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		map.put("project", project);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		map.put("dto", dto);
		if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType()) 
				|| StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_ELECTIVE, project.getEvaluateType())){
			//班级维度
			if(StringUtils.equals(EvaluationConstants.EVALUATION_TYPE_TEACH, project.getEvaluateType())){
				// 必修课
				String teaId = request.getParameter("teaId");
				String teaName = "";
				if(StringUtils.isNotBlank(teaId)){
					Teacher tea = SUtils.dc(teacherRemoteService.findOneById(teaId), Teacher.class);
					teaName = tea.getTeacherName();
				}
				map.put("teaId", teaId);
				map.put("teaName", teaName);
				//TODO 科目范围可以直接到项目设置班级关系表中找
				String subjectId = request.getParameter("subjectId");
				List<TeachEvaluateRelation> relations = new ArrayList<>(); 
				relations = teachEvaluateRelationService.findByProjectIds(new String[] {projectId});
				Set<String> clsIds = new HashSet<>();
				Set<String> courseIds = new HashSet<>();
				for (TeachEvaluateRelation e : relations) {
					clsIds.add(e.getValueId());
					courseIds.add(e.getSubjectId()); 
				}
				List<Course> courseList=SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){});
				map.put("subjectId", subjectId);
				map.put("courseList", courseList);
				List<Clazz> classes = new ArrayList<>();
				String classId = request.getParameter("classId");
				if(StringUtils.isNotBlank(classId) && !clsIds.contains(classId)) {
					classId = "";
				}
				classes = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>(){});
				List<TeachClass> teaClses = SUtils.dt(teachClassRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<TeachClass>>(){});
				if(CollectionUtils.isNotEmpty(teaClses)) {
					Clazz clazz = null;
					for (TeachClass e : teaClses) {
						clazz = new Clazz();
						clazz.setId(e.getId());
						clazz.setClassNameDynamic(e.getName());
						classes.add(clazz);
					}
				}
				if(StringUtils.isBlank(classId) && CollectionUtils.isNotEmpty(classes)) {
//					classId = classes.get(0).getId();
				}
				map.put("classId", classId);
				map.put("classes", classes);
//				Pagination page=createPagination();
				List<ResultStatDto> dtoAlllist = teachEvaluateResultStatService.getResultStatDto(projectId,EvaluationConstants.STATE__DIMENSION_CLASS,subjectId,classId,teaId);
//				List<ResultStatDto> dtolist = getPageList(dtoAlllist,page);
//				sendPagination(request, map, page);
				map.put("dtolist",dtoAlllist );
				return "/evaluation/stat/statResultCls.ftl";
			}else{
				// 选修课
				// 获得表格数据
//				Pagination page=createPagination();
				List<ResultStatDto> dtoAlllist = teachEvaluateResultStatService.getResultStatElectiveDto(projectId,EvaluationConstants.STATE__DIMENSION_CLASS);
//				List<ResultStatDto> dtolist = getPageList(dtoAlllist,page);
//				sendPagination(request, map, page);
				map.put("dtolist",dtoAlllist );
				return "/evaluation/stat/statResultElective.ftl";
			}
		}else{
			// 年级维度与全校维度
			//班主任调查和导师调查
			Map<String,Float> gradeItemScoreMap= teachEvaluateResultStatService.getResultStatTeaSchDto(projectId);
			map.put("gradeItemScoreMap", gradeItemScoreMap);
			Set<String> gradeIds = new HashSet<String>();
			for(String gradeItemId : gradeItemScoreMap.keySet()){
				String gradeId = gradeItemId.split(",")[0];
				if(!StringUtils.equals(gradeId, "school")){
					gradeIds.add(gradeId);
				}
			}
			List<Grade> gradelist = new ArrayList<>();
			if(gradeIds.size()>0){
				List<Grade> gradelistAll = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(getLoginInfo().getUnitId(), acadyear), new TR<List<Grade>>(){});
	    		for(Grade grade : gradelistAll){
	    			if(gradeIds.contains(grade.getId())){
	    				gradelist.add(grade);
	    			}
	    		}
				map.put("gradelist", gradelist);
			}else{
				map.put("gradelist", null);
			}
			return "/evaluation/stat/statResultTea.ftl";
		}
	}
	
	@RequestMapping("/index/page")
	@ControllerInfo(value = "评教管理-评教项目")
	public String showIndex(HttpServletRequest request,ModelMap map) {
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
		if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		map.put("acadyearList", acadyearList);
		Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()),Semester.class);
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String evaluateType = request.getParameter("evaluateType");
		if(se != null && StringUtils.isBlank(acadyear)){
			 acadyear = se.getAcadyear();
			 semester = se.getSemester()+"";
		}
		if(StringUtils.isBlank(acadyear)){
			acadyear = acadyearList.get(0);
			semester = "1";
		}
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("evaluateType", evaluateType);
		List<TeachEvaluateProject> projectList = teachEvaluateProjectService.findByUnitIdAndType(getLoginInfo().getUnitId(), acadyear, semester,evaluateType);
		for (TeachEvaluateProject project : projectList) {
			String hasStat = RedisUtils.get("statEvaluateState"+project.getId());
			if(StringUtils.isNotBlank(hasStat)){
				project.setHasStat(hasStat);
			}
		}
		map.put("projectList",projectList);
		return "/evaluation/stat/statIndex.ftl";
	}
	
	public class StatThread extends Thread{
		private String projectId;
		private TeachEvaluateResultStatService teachEvaluateResultStatService;
	    public StatThread(String projectId,TeachEvaluateResultStatService teachEvaluateResultStatService) { 
	    	this.projectId = projectId;
	    	this.teachEvaluateResultStatService = teachEvaluateResultStatService;
	    } 
	    public void run() { 
	    	RedisUtils.set("statEvaluateState"+projectId, "2");
	    	try {
	    		teachEvaluateResultStatService.statResult(projectId);
	    		RedisUtils.del("statEvaluateState"+projectId);
			} catch (Exception e) {
				System.out.println("统计失败：projectId="+projectId);
				RedisUtils.del("statEvaluateState"+projectId);
				e.printStackTrace();
			}
	    } 
	}
	
	@ResponseBody
	@RequestMapping("/statResult")
    @ControllerInfo(value = "统计")
	public String statResult(String projectId,ModelMap map){
		if(StringUtils.isBlank(projectId)){
			return error("项目不存在!");
		}
		try {
			StatThread statThread = new StatThread(projectId, teachEvaluateResultStatService);
			statThread.start();
		} catch (Exception e) {
			return error("统计启动失败!");
		}
		return success("统计中，请稍后进行查看！");
	}
	
	@RequestMapping("/doBjExportAll")
	public void doBjExportAll(HttpServletRequest request,HttpServletResponse response){
		String projectId = request.getParameter("projectId");
		String acadyear = request.getParameter("acadyear");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		// 年级维度与全校维度
		//班主任调查和导师调查
		Map<String,Float> gradeItemScoreMap= teachEvaluateResultStatService.getResultStatTeaSchExportDto(projectId);
		Set<String> gradeIds = new HashSet<String>();
		for(String gradeItemId : gradeItemScoreMap.keySet()){
			String gradeId = gradeItemId.split(",")[0];
			if(!StringUtils.equals(gradeId, "school")){
				gradeIds.add(gradeId);
			}
		}
		List<Grade> gradelist = new ArrayList<>();
		List<Clazz> clsList = new ArrayList<>();
		if(gradeIds.size()>0){
    		List<Grade> gradelistAll = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(getLoginInfo().getUnitId(), acadyear), new TR<List<Grade>>(){});
    		for(Grade grade : gradelistAll){
    			if(gradeIds.contains(grade.getId())){
    				gradelist.add(grade);
    			}
    		}
            
            Set<String> gradeIdSet = new HashSet<String>();
            for(Grade grade : gradelist){
            	gradeIdSet.add(grade.getId());
            }
            Set<String> classIdSet = new HashSet<String>();
            for(String id : gradeIds){
            	if(!gradeIdSet.contains(id)){
            		classIdSet.add(id);
            	}
            }
            if(CollectionUtils.isNotEmpty(classIdSet)){
            	 clsList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
            	 for(Clazz cls : clsList){
            		 Grade grade = new Grade();
            		 grade.setId(cls.getId());
            		 grade.setGradeName(cls.getClassNameDynamic());
            		 gradelist.add(grade);
            	 }
            }
		}
		
		List<TeachEvaluateResult> resultList = teachEvaluateResultService.findBySubmitAndInProjectId(new String[]{projectId});
        Map<String, String> ansMap = new HashMap<String, String>();
		for(TeachEvaluateResult result : resultList){
			if(null==ansMap.get(result.getClassId()+result.getItemId())){
				ansMap.put(result.getClassId()+result.getItemId(), result.getResult());
			}else{
				ansMap.put(result.getClassId()+result.getItemId(), ansMap.get(result.getClassId()+result.getItemId())+"#####"+result.getResult());
			}
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();								
		HSSFSheet sheet = workbook.createSheet("调查结果");
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 2800);
        sheet.setColumnWidth(2, 2800);
        sheet.setColumnWidth(3, 2800);
        sheet.setColumnWidth(4, 2800);
        sheet.setColumnWidth(5, 2800);
        sheet.setColumnWidth(6, 2800);
        sheet.setColumnWidth(7, 2800);
     			
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont headfont = workbook.createFont();
		headfont.setFontHeightInPoints((short) 15);// 字体大小
		headfont.setBold(true);// 加粗
		style.setFont(headfont);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		HSSFCellStyle style2 = workbook.createCellStyle();
		HSSFFont headfont2 = workbook.createFont();
		headfont2.setFontHeightInPoints((short) 10);// 字体大小

		//headfont2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style2.setFont(headfont2);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		style2.setWrapText(true);
		
		//第一行
		HSSFRow row1 = sheet.createRow(0);
		CellRangeAddress car11 = new CellRangeAddress(0,3,0,4);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
	    sheet.addMergedRegion(car11);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellStyle(style);
		cell11.setCellValue(new HSSFRichTextString(project.getProjectName()));
		
		if(CollectionUtils.isNotEmpty(gradelist)){
			CellRangeAddress car12 = new CellRangeAddress(0,0,5,6);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
		    sheet.addMergedRegion(car12);
			HSSFCell cell12 = row1.createCell(5);
			cell12.setCellStyle(style2);
			cell12.setCellValue(new HSSFRichTextString("全校情况"));
			int i=7;
			for(Grade grade : gradelist){
				CellRangeAddress car13 = new CellRangeAddress(0,0,i,i+1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car13, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car13, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car13, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car13, sheet); 
			    sheet.addMergedRegion(car13);
				HSSFCell cell13 = row1.createCell(i);
				cell13.setCellStyle(style2);
				cell13.setCellValue(new HSSFRichTextString(grade.getGradeName()));
				i=i+2;
			}
		}
		
		//第二行
		HSSFRow row2 = sheet.createRow(1);
		if(CollectionUtils.isNotEmpty(gradelist)){
			CellRangeAddress car21 = new CellRangeAddress(1,3,5,6);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
		    sheet.addMergedRegion(car21);
			HSSFCell cell21 = row2.createCell(5);
			cell21.setCellStyle(style2);
			cell21.setCellValue(new HSSFRichTextString("年级总得分平均分："+String.format("%.2f",gradeItemScoreMap.get("school,countScore"))));
			int i=7;
			for(Grade grade : gradelist){
				CellRangeAddress car22 = new CellRangeAddress(1,3,i,i+1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
			    sheet.addMergedRegion(car22);
				HSSFCell cell22 = row2.createCell(i);
				cell22.setCellStyle(style2);
				cell22.setCellValue(new HSSFRichTextString("年级总得分平均分："+String.format("%.2f",gradeItemScoreMap.get(grade.getId()+",countScore"))));
				i=i+2;
			}
		}
		
		//第3行
		HSSFRow row3 = sheet.createRow(4);
		CellRangeAddress car31 = new CellRangeAddress(4,4,0,1);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car31, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car31, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car31, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car31, sheet); 
	    sheet.addMergedRegion(car31);
		HSSFCell cell31 = row3.createCell(0);
		cell31.setCellStyle(style2);
		cell31.setCellValue(new HSSFRichTextString("项目名称"));
		
		CellRangeAddress car32 = new CellRangeAddress(4,4,2,4);
	    RegionUtil.setBorderBottom(BorderStyle.THIN, car32, sheet); 
	    RegionUtil.setBorderLeft(BorderStyle.THIN, car32, sheet); 
		RegionUtil.setBorderTop(BorderStyle.THIN, car32, sheet); 
		RegionUtil.setBorderRight(BorderStyle.THIN, car32, sheet); 
	    sheet.addMergedRegion(car32);
		HSSFCell cell32 = row3.createCell(2);
		cell32.setCellStyle(style2);
		cell32.setCellValue(new HSSFRichTextString("选项"));
		
		if(CollectionUtils.isNotEmpty(gradelist)){
			CellRangeAddress car33 = new CellRangeAddress(4,4,5,5);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car33, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car33, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car33, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car33, sheet); 
		    //sheet.addMergedRegion(car33);
			HSSFCell cell33 = row3.createCell(5);
			cell33.setCellStyle(style2);
			cell33.setCellValue(new HSSFRichTextString("比例（%）"));
			
			CellRangeAddress car34 = new CellRangeAddress(4,4,6,6);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car34, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car34, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car34, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car34, sheet); 
		    //sheet.addMergedRegion(car34);
			HSSFCell cell34 = row3.createCell(6);
			cell34.setCellStyle(style2);
			cell34.setCellValue(new HSSFRichTextString("分数"));
			int i=7;
			for(Grade grade : gradelist){
				CellRangeAddress car35 = new CellRangeAddress(4,4,i,i);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car35, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car35, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car35, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car35, sheet); 
			    //sheet.addMergedRegion(car35);
				HSSFCell cell35 = row3.createCell(i);
				cell35.setCellStyle(style2);
				cell35.setCellValue(new HSSFRichTextString("比例（%）"));
				
				CellRangeAddress car36 = new CellRangeAddress(4,4,i+1,i+1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car36, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car36, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car36, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car36, sheet); 
			    //sheet.addMergedRegion(car36);
				HSSFCell cell36 = row3.createCell(i+1);
				cell36.setCellStyle(style2);
				cell36.setCellValue(new HSSFRichTextString("分数"));					
				i=i+2;
			}
		}
		
		//循环
		int t = 5;
		int indexRow = 5;
		for(TeachEvaluateItem eva : dto.getEvaluaList()){
			int listSize = eva.getOptionList().size();
			HSSFRow row5 = sheet.createRow(indexRow);
			CellRangeAddress car51 = new CellRangeAddress(indexRow,indexRow+listSize-1,0,1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car51, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car51, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car51, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car51, sheet); 
		    sheet.addMergedRegion(car51);
			HSSFCell cell51 = row5.createCell(0);
			cell51.setCellStyle(style2);
			cell51.setCellValue(new HSSFRichTextString(eva.getItemName()));	
			
			if(CollectionUtils.isNotEmpty(gradelist)){			
				int x = 6;					
				CellRangeAddress car52 = new CellRangeAddress(indexRow,indexRow+listSize-1,x,x);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car52, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car52, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car52, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car52, sheet); 
			    sheet.addMergedRegion(car52);
				HSSFCell cell52 = row5.createCell(x);
				cell52.setCellStyle(style2);
				cell52.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get("school,"+eva.getId())+""));	
				for(Grade grade : gradelist){
					CellRangeAddress car53 = new CellRangeAddress(indexRow,indexRow+listSize-1,x+2,x+2);
					RegionUtil.setBorderBottom(BorderStyle.THIN, car53, sheet); 
					RegionUtil.setBorderLeft(BorderStyle.THIN, car53, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car53, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car53, sheet); 
					sheet.addMergedRegion(car53);
					HSSFCell cell53 = row5.createCell(x+2);
					cell53.setCellStyle(style2);
					cell53.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(grade.getId()+","+eva.getId())+""));
					x=x+2;
				}
			}
			for(TeachEvaluateItemOption opt : eva.getOptionList()){
				if(indexRow!=t){						
					HSSFRow row4 = sheet.createRow(t);
					CellRangeAddress car41 = new CellRangeAddress(t,t,2,4);
					RegionUtil.setBorderBottom(BorderStyle.THIN, car41, sheet); 
					RegionUtil.setBorderLeft(BorderStyle.THIN, car41, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car41, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car41, sheet); 
					sheet.addMergedRegion(car41);
					HSSFCell cell41 = row4.createCell(2);
					cell41.setCellStyle(style2);
					cell41.setCellValue(new HSSFRichTextString(opt.getOptionName()));
					
					if(CollectionUtils.isNotEmpty(gradelist)){
						int a = 5;
						CellRangeAddress car43 = new CellRangeAddress(t,t,a,a);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car43, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car43, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car43, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car43, sheet); 
						//sheet.addMergedRegion(car43);
						HSSFCell cell43 = row4.createCell(a);
						cell43.setCellStyle(style2);
						cell43.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get("school,"+opt.getId())+""));
						for(Grade grade : gradelist){
							CellRangeAddress car45 = new CellRangeAddress(t,t,a+2,a+2);
							RegionUtil.setBorderBottom(BorderStyle.THIN, car45, sheet); 
							RegionUtil.setBorderLeft(BorderStyle.THIN, car45, sheet); 
							RegionUtil.setBorderTop(BorderStyle.THIN, car45, sheet); 
							RegionUtil.setBorderRight(BorderStyle.THIN, car45, sheet); 
							//sheet.addMergedRegion(car45);
							HSSFCell cell45 = row4.createCell(a+2);
							cell45.setCellStyle(style2);
							cell45.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(grade.getId()+","+opt.getId())+""));
							a= a+2;
						}
					}
				}else{
					CellRangeAddress car42 = new CellRangeAddress(t,t,2,4);
					RegionUtil.setBorderBottom(BorderStyle.THIN, car42, sheet); 
					RegionUtil.setBorderLeft(BorderStyle.THIN, car42, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car42, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car42, sheet); 
					sheet.addMergedRegion(car42);
					HSSFCell cell42 = row5.createCell(2);
					cell42.setCellStyle(style2);
					cell42.setCellValue(new HSSFRichTextString(opt.getOptionName()));
					
					if(CollectionUtils.isNotEmpty(gradelist)){
						int a = 5;
						CellRangeAddress car44 = new CellRangeAddress(t,t,a,a);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car44, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car44, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car44, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car44, sheet); 
						//sheet.addMergedRegion(car44);
						HSSFCell cell44 = row5.createCell(a);
						cell44.setCellStyle(style2);
						cell44.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get("school,"+opt.getId())+""));
						for(Grade grade : gradelist){
							CellRangeAddress car46 = new CellRangeAddress(t,t,a+2,a+2);
							RegionUtil.setBorderBottom(BorderStyle.THIN, car46, sheet); 
							RegionUtil.setBorderLeft(BorderStyle.THIN, car46, sheet); 
							RegionUtil.setBorderTop(BorderStyle.THIN, car46, sheet); 
							RegionUtil.setBorderRight(BorderStyle.THIN, car46, sheet); 
							//sheet.addMergedRegion(car46);
							HSSFCell cell46 = row5.createCell(a+2);
							cell46.setCellStyle(style2);
							cell46.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(grade.getId()+","+opt.getId())+""));
							a= a+2;
						}
					}
				}
				t++;
			}
			indexRow = indexRow+listSize;
		}
		//满意度
		for(TeachEvaluateItem fill : dto.getFillList()){
			int listSize = fill.getOptionList().size();
			HSSFRow row6 = sheet.createRow(indexRow);
			CellRangeAddress car61 = new CellRangeAddress(indexRow,indexRow+listSize-1,0,1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car61, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car61, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car61, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car61, sheet); 
		    sheet.addMergedRegion(car61);
			HSSFCell cell61 = row6.createCell(0);
			cell61.setCellStyle(style2);
			cell61.setCellValue(new HSSFRichTextString(fill.getItemName()));
			
			
			for(TeachEvaluateItemOption opt : fill.getOptionList()){
				if(indexRow!=t){
					HSSFRow row7 = sheet.createRow(t);
					CellRangeAddress car71 = new CellRangeAddress(t,t,2,4);
					RegionUtil.setBorderBottom(BorderStyle.THIN, car71, sheet); 
					RegionUtil.setBorderLeft(BorderStyle.THIN, car71, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car71, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car71, sheet); 
					sheet.addMergedRegion(car71);
					HSSFCell cell71 = row7.createCell(2);
					cell71.setCellStyle(style2);
					cell71.setCellValue(new HSSFRichTextString(opt.getOptionName()));
					if(CollectionUtils.isNotEmpty(gradelist)){
						int a = 5;
						CellRangeAddress car73 = new CellRangeAddress(t,t,a,a+1);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car73, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car73, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car73, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car73, sheet); 
						sheet.addMergedRegion(car73);
						HSSFCell cell73 = row7.createCell(5);
						cell73.setCellStyle(style2);
						cell73.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get("school,"+opt.getId())+""));
						for(Grade grade : gradelist){
							CellRangeAddress car75 = new CellRangeAddress(t,t,a+2,a+3);
							RegionUtil.setBorderBottom(BorderStyle.THIN, car75, sheet); 
							RegionUtil.setBorderLeft(BorderStyle.THIN, car75, sheet); 
							RegionUtil.setBorderTop(BorderStyle.THIN, car75, sheet); 
							RegionUtil.setBorderRight(BorderStyle.THIN, car75, sheet); 
							sheet.addMergedRegion(car75);
							HSSFCell cell75 = row7.createCell(a+2);
							cell75.setCellStyle(style2);
							cell75.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(grade.getId()+","+opt.getId())+""));
							a= a+2;
						}
					}
				}else{
					CellRangeAddress car72 = new CellRangeAddress(t,t,2,4);
					RegionUtil.setBorderBottom(BorderStyle.THIN, car72, sheet); 
					RegionUtil.setBorderLeft(BorderStyle.THIN, car72, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car72, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car72, sheet); 
					sheet.addMergedRegion(car72);
					HSSFCell cell72 = row6.createCell(2);
					cell72.setCellStyle(style2);
					cell72.setCellValue(new HSSFRichTextString(opt.getOptionName()));
					if(CollectionUtils.isNotEmpty(gradelist)){
						int a=5;
						CellRangeAddress car74 = new CellRangeAddress(t,t,a,a+1);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car74, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car74, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car74, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car74, sheet); 
						sheet.addMergedRegion(car74);
						HSSFCell cell74 = row6.createCell(5);
						cell74.setCellStyle(style2);
						cell74.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get("school,"+opt.getId())+""));
						for(Grade grade : gradelist){
							CellRangeAddress car76 = new CellRangeAddress(t,t,a+2,a+3);
							RegionUtil.setBorderBottom(BorderStyle.THIN, car76, sheet); 
							RegionUtil.setBorderLeft(BorderStyle.THIN, car76, sheet); 
							RegionUtil.setBorderTop(BorderStyle.THIN, car76, sheet); 
							RegionUtil.setBorderRight(BorderStyle.THIN, car76, sheet); 
							sheet.addMergedRegion(car76);
							HSSFCell cell76 = row6.createCell(a+2);
							cell76.setCellStyle(style2);
							cell76.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(grade.getId()+","+opt.getId())+""));
							a= a+2;
						}
					}
				}
				t++;
			}
			indexRow = indexRow+listSize;
		}
		//各班级
		for(Clazz cls : clsList){
			HSSFSheet sheet2 = workbook.createSheet(cls.getClassNameDynamic());
			sheet2.setColumnWidth(0, 2000);
			sheet2.setColumnWidth(1, 2800);
			sheet2.setColumnWidth(2, 2800);
			sheet2.setColumnWidth(3, 2800);
			sheet2.setColumnWidth(4, 2800);
			sheet2.setColumnWidth(5, 2800);
			sheet2.setColumnWidth(6, 2800);
			sheet2.setColumnWidth(7, 2800);
			
			//第一行
			HSSFRow row8 = sheet2.createRow(0);
			CellRangeAddress car81 = new CellRangeAddress(0,0,0,3);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car81, sheet2); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car81, sheet2); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car81, sheet2); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car81, sheet2); 
		    sheet2.addMergedRegion(car81);
			HSSFCell cell81 = row8.createCell(0);
			cell81.setCellStyle(style2);
			cell81.setCellValue(new HSSFRichTextString("项目名称"));
			
			CellRangeAddress car82 = new CellRangeAddress(0,0,4,7);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car82, sheet2); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car82, sheet2); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car82, sheet2); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car82, sheet2); 
		    sheet2.addMergedRegion(car82);
			HSSFCell cell82 = row8.createCell(4);
			cell82.setCellStyle(style2);
			cell82.setCellValue(new HSSFRichTextString("选项"));
			
			CellRangeAddress car83 = new CellRangeAddress(0,0,8,8);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car83, sheet2); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car83, sheet2); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car83, sheet2); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car83, sheet2); 
		    //sheet2.addMergedRegion(car83);
			HSSFCell cell83 = row8.createCell(8);
			cell83.setCellStyle(style2);
			cell83.setCellValue(new HSSFRichTextString("比例（%）"));
			
			CellRangeAddress car84 = new CellRangeAddress(0,0,9,9);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car84, sheet2); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car84, sheet2); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car84, sheet2); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car84, sheet2); 
		    //sheet2.addMergedRegion(car84);
			HSSFCell cell84 = row8.createCell(9);
			cell84.setCellStyle(style2);
			cell84.setCellValue(new HSSFRichTextString("得分"));
			//循环
			int f=1;
			int indexRow2 = 1;
			for(TeachEvaluateItem eva : dto.getEvaluaList()){
				int listSize = eva.getOptionList().size();
				HSSFRow row9 = sheet2.createRow(indexRow2);
				CellRangeAddress car91 = new CellRangeAddress(indexRow2,indexRow2+listSize-1,0,3);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car91, sheet2); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car91, sheet2); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car91, sheet2); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car91, sheet2); 
			    sheet2.addMergedRegion(car91);
				HSSFCell cell91 = row9.createCell(0);
				cell91.setCellStyle(style2);
				cell91.setCellValue(new HSSFRichTextString(eva.getItemName()));
				
				CellRangeAddress car92 = new CellRangeAddress(indexRow2,indexRow2+listSize-1,9,9);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car92, sheet2); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car92, sheet2); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car92, sheet2); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car92, sheet2); 
			    sheet2.addMergedRegion(car92);
				HSSFCell cell92 = row9.createCell(9);
				cell92.setCellStyle(style2);
				cell92.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(cls.getId()+","+eva.getId())+""));
				for(TeachEvaluateItemOption opt : eva.getOptionList()){
					if(f!=indexRow2){							
						HSSFRow row10 = sheet2.createRow(f);
						CellRangeAddress car101 = new CellRangeAddress(f,f,4,7);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car101, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car101, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car101, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car101, sheet2); 
						sheet2.addMergedRegion(car101);
						HSSFCell cell101 = row10.createCell(4);
						cell101.setCellStyle(style2);
						cell101.setCellValue(new HSSFRichTextString(opt.getOptionName()));
						
						CellRangeAddress car103 = new CellRangeAddress(f,f,8,8);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car103, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car103, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car103, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car103, sheet2); 
						//sheet2.addMergedRegion(car103);
						HSSFCell cell103 = row10.createCell(8);
						cell103.setCellStyle(style2);
						cell103.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(cls.getId()+","+opt.getId())+""));
						f++;
					}else{
						CellRangeAddress car102 = new CellRangeAddress(f,f,4,7);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car102, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car102, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car102, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car102, sheet2); 
						sheet2.addMergedRegion(car102);
						HSSFCell cell102 = row9.createCell(4);
						cell102.setCellStyle(style2);
						cell102.setCellValue(new HSSFRichTextString(opt.getOptionName()));
						
						CellRangeAddress car104 = new CellRangeAddress(f,f,8,8);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car104, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car104, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car104, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car104, sheet2); 
						//sheet2.addMergedRegion(car102);
						HSSFCell cell104 = row9.createCell(8);
						cell104.setCellStyle(style2);
						cell104.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(cls.getId()+","+opt.getId())+""));
						f++;
					}
				}
				indexRow2 = indexRow2+listSize;
			}
			
			for(TeachEvaluateItem fill : dto.getFillList()){
				int listSize = fill.getOptionList().size();
				HSSFRow row11 = sheet2.createRow(indexRow2);
				CellRangeAddress car111 = new CellRangeAddress(indexRow2,indexRow2+listSize-1,0,3);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car111, sheet2); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car111, sheet2); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car111, sheet2); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car111, sheet2); 
			    sheet2.addMergedRegion(car111);
				HSSFCell cell111 = row11.createCell(0);
				cell111.setCellStyle(style2);
				cell111.setCellValue(new HSSFRichTextString(fill.getItemName()));
				for(TeachEvaluateItemOption opt : fill.getOptionList()){
					if(f!=indexRow2){
						HSSFRow row12 = sheet2.createRow(f);
						CellRangeAddress car121 = new CellRangeAddress(f,f,4,7);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car121, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car121, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car121, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car121, sheet2); 
						sheet2.addMergedRegion(car121);
						HSSFCell cell121 = row12.createCell(4);
						cell121.setCellStyle(style2);
						cell121.setCellValue(new HSSFRichTextString(opt.getOptionName()));
						
						CellRangeAddress car123 = new CellRangeAddress(f,f,8,9);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car123, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car123, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car123, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car123, sheet2); 
						sheet2.addMergedRegion(car123);
						HSSFCell cell123 = row12.createCell(8);
						cell123.setCellStyle(style2);
						cell123.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(cls.getId()+","+opt.getId())+""));
						f++;
					}else{
						CellRangeAddress car122 = new CellRangeAddress(f,f,4,7);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car122, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car122, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car122, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car122, sheet2); 
						sheet2.addMergedRegion(car122);
						HSSFCell cell122 = row11.createCell(4);
						cell122.setCellStyle(style2);
						cell122.setCellValue(new HSSFRichTextString(opt.getOptionName()));
						
						CellRangeAddress car123 = new CellRangeAddress(f,f,8,9);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car123, sheet2); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car123, sheet2); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car123, sheet2); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car123, sheet2); 
						sheet2.addMergedRegion(car123);
						HSSFCell cell123 = row11.createCell(8);
						cell123.setCellStyle(style2);
						cell123.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(cls.getId()+","+opt.getId())+""));
						f++;
					}
				}
				indexRow2 = indexRow2+listSize;
			}
			
			for(TeachEvaluateItem ans : dto.getAnswerList()){
				HSSFRow row13 = sheet2.createRow(indexRow2);
				CellRangeAddress car131 = new CellRangeAddress(indexRow2,indexRow2+5,0,3);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car131, sheet2); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car131, sheet2); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car131, sheet2); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car131, sheet2); 
			    sheet2.addMergedRegion(car131);
				HSSFCell cell131 = row13.createCell(0);
				cell131.setCellStyle(style2);
				cell131.setCellValue(new HSSFRichTextString(ans.getItemName()));
				
				CellRangeAddress car132 = new CellRangeAddress(indexRow2,indexRow2+5,4,9);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car132, sheet2); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car132, sheet2); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car132, sheet2); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car132, sheet2); 
			    sheet2.addMergedRegion(car132);
				HSSFCell cell132 = row13.createCell(4);
				cell132.setCellStyle(style2);
				cell132.setCellValue(new HSSFRichTextString(ansMap.get(cls.getId()+ans.getId())));					
				indexRow2 = indexRow2+3;
			}
			
		}			
		ExportUtils.outputData(workbook, project.getProjectName(), response);
	}
	
	@RequestMapping("/doDsExportAll1")
	public void doDsExportAll1(HttpServletRequest request,HttpServletResponse response){
		String projectId = request.getParameter("projectId");
		String acadyear = request.getParameter("acadyear");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		// 年级维度与全校维度
		//班主任调查和导师调查
		Map<String,Float> gradeItemScoreMap= teachEvaluateResultStatService.getResultStatTeaSchExportDto(projectId);
		Set<String> gradeIds = new HashSet<String>();
		for(String gradeItemId : gradeItemScoreMap.keySet()){
			String gradeId = gradeItemId.split(",")[0];
			if(!StringUtils.equals(gradeId, "school")){
				gradeIds.add(gradeId);
			}
		}
		List<Grade> gradelist = new ArrayList<>();
		Set<String> teacherIdSetTemp = new HashSet<String>();
		Map<String, Set<String>> gradeTeaMap = new HashMap<String, Set<String>>();
		if(gradeIds.size()>0){
    		List<Grade> gradelistAll = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(getLoginInfo().getUnitId(), acadyear), new TR<List<Grade>>(){});
    		for(Grade grade : gradelistAll){
    			if(gradeIds.contains(grade.getId())){
    				gradelist.add(grade);
    			}
    		}
            Map<String,Set<String>> tutorMap=SUtils.dt(tutorRemoteService.getTutorTeaStuMapByUnitId(getLoginInfo().getUnitId()),new TR<Map<String,Set<String>>>(){});
            Set<String> studentIdSet = new HashSet<String>();
            for(String teacherId : tutorMap.keySet()){
            	for(String stuId : tutorMap.get(teacherId)){
            		studentIdSet.add(stuId);
            	}
            }
            List<Student> stuList = new ArrayList<Student>();
            Map<String, String> stuGradeIdMap = new HashMap<String, String>();
            Set<String> clsIdSet = new HashSet<String>();
            List<Clazz> clsList = new ArrayList<Clazz>();
            Map<String, String> sectionClsMap = new HashMap<String, String>();
            if(CollectionUtils.isNotEmpty(studentIdSet)){
            	stuList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])),new TR<List<Student>>(){});
            	for(Student stu : stuList){
            		clsIdSet.add(stu.getClassId());
            	}
            	if(CollectionUtils.isNotEmpty(clsIdSet)){
            		clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])),new TR<List<Clazz>>(){});
            		for(Clazz cls : clsList){
            			sectionClsMap.put(cls.getId(), cls.getGradeId());
            		}
            	}
            	for(Student stu : stuList){
            		stuGradeIdMap.put(stu.getId(), sectionClsMap.get(stu.getClassId()));
            	}
            }
            Map<String, Set<String>> teaGradeSetMap = new HashMap<String, Set<String>>();
            for(String teacherId : tutorMap.keySet()){
            	Set<String> gradeIdSet = new HashSet<String>();
            	for(String stuId : tutorMap.get(teacherId)){
            		gradeIdSet.add(stuGradeIdMap.get(stuId));
            	}
            	teaGradeSetMap.put(teacherId, gradeIdSet);
            	teacherIdSetTemp.add(teacherId);
            }
            for(Grade grade : gradelist){
            	Set<String> teacherIdSet = new HashSet<String>();
            	for(String teacherId : teaGradeSetMap.keySet()){
            		if(teaGradeSetMap.get(teacherId).contains(grade.getId())){
            			teacherIdSet.add(teacherId);
            		}
            	}
            	gradeTeaMap.put(grade.getId(), teacherIdSet);
            }
		}
		Map<String, String> teacherNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(teacherIdSetTemp)){
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdSetTemp.toArray(new String[0])),new TR<List<Teacher>>(){});			
			for(Teacher teacher : teacherList){
				teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
			}
			for(Grade grade : gradelist){
				teacherNameMap.put(grade.getId(), grade.getGradeName());
			}
			teacherNameMap.put("school", "全校情况");
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();	
		
		for(Grade grade : gradelist){
			HSSFSheet sheet = workbook.createSheet(grade.getGradeName());
	        sheet.setColumnWidth(0, 2000);
	        sheet.setColumnWidth(1, 2800);
	        sheet.setColumnWidth(2, 2800);
	        sheet.setColumnWidth(3, 2800);
	        sheet.setColumnWidth(4, 2800);
	        sheet.setColumnWidth(5, 2800);
	        sheet.setColumnWidth(6, 2800);
	        sheet.setColumnWidth(7, 2800);
	     					
			HSSFCellStyle style = workbook.createCellStyle();
			HSSFFont headfont = workbook.createFont();
			headfont.setFontHeightInPoints((short) 10);// 字体大小
			//headfont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			style.setFont(headfont);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setWrapText(true);
			
			//第一行
			HSSFRow row1 = sheet.createRow(0);
			CellRangeAddress car11 = new CellRangeAddress(0,0,0,0);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
		    //sheet.addMergedRegion(car11);
			HSSFCell cell11 = row1.createCell(0);
			cell11.setCellStyle(style);
			cell11.setCellValue(new HSSFRichTextString("序号"));
			
			CellRangeAddress car12 = new CellRangeAddress(0,0,1,1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
		    //sheet.addMergedRegion(car12);
			HSSFCell cell12 = row1.createCell(1);
			cell12.setCellStyle(style);
			cell12.setCellValue(new HSSFRichTextString(""));
			
			CellRangeAddress car13 = new CellRangeAddress(0,2,2,2);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car13, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car13, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car13, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car13, sheet); 
		    sheet.addMergedRegion(car13);
			HSSFCell cell13 = row1.createCell(2);
			cell13.setCellStyle(style);
			cell13.setCellValue(new HSSFRichTextString("总分"));
			
			//第2行
			HSSFRow row2 = sheet.createRow(1);
			CellRangeAddress car21 = new CellRangeAddress(1,1,0,0);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
		    //sheet.addMergedRegion(car21);
			HSSFCell cell21 = row2.createCell(0);
			cell21.setCellStyle(style);
			cell21.setCellValue(new HSSFRichTextString("项目名称"));
			
			CellRangeAddress car22 = new CellRangeAddress(1,1,1,1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
		    //sheet.addMergedRegion(car22);
			HSSFCell cell22 = row2.createCell(1);
			cell22.setCellStyle(style);
			cell22.setCellValue(new HSSFRichTextString(""));

			//第3行
			HSSFRow row3 = sheet.createRow(2);
			CellRangeAddress car31 = new CellRangeAddress(2,2,0,0);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car31, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car31, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car31, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car31, sheet); 
		    //sheet.addMergedRegion(car31);
			HSSFCell cell31 = row3.createCell(0);
			cell31.setCellStyle(style);
			cell31.setCellValue(new HSSFRichTextString("选项"));
			
			CellRangeAddress car32 = new CellRangeAddress(2,2,1,1);
		    RegionUtil.setBorderBottom(BorderStyle.THIN, car32, sheet); 
		    RegionUtil.setBorderLeft(BorderStyle.THIN, car32, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car32, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car32, sheet); 
		    //sheet.addMergedRegion(car32);
			HSSFCell cell32 = row2.createCell(1);
			cell32.setCellStyle(style);
			cell32.setCellValue(new HSSFRichTextString(""));
			int t = 3;
			int indexRow=3;
			int i=1;
			for(TeachEvaluateItem eva : dto.getEvaluaList()){
				int listSize = eva.getOptionList().size();
				CellRangeAddress car23 = new CellRangeAddress(1,1,indexRow,indexRow+listSize-1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car23, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car23, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car23, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car23, sheet); 
			    sheet.addMergedRegion(car23);
				HSSFCell cell23 = row2.createCell(indexRow);
				cell23.setCellStyle(style);
				cell23.setCellValue(new HSSFRichTextString(eva.getItemName()));
				
				for(TeachEvaluateItemOption opt : eva.getOptionList()){
					CellRangeAddress car33 = new CellRangeAddress(2,2,t,t);
				    RegionUtil.setBorderBottom(BorderStyle.THIN, car33, sheet); 
				    RegionUtil.setBorderLeft(BorderStyle.THIN, car33, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car33, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car33, sheet); 
				    //sheet.addMergedRegion(car33);
					HSSFCell cell33 = row3.createCell(t);
					cell33.setCellStyle(style);
					cell33.setCellValue(new HSSFRichTextString(opt.getOptionName()));
					
					
					if(t!=indexRow){						
						CellRangeAddress car14 = new CellRangeAddress(0,0,t,t);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
						//sheet.addMergedRegion(car14);
						HSSFCell cell14 = row1.createCell(t);
						cell14.setCellStyle(style);
						cell14.setCellValue(new HSSFRichTextString(""));
						t++;
					}else{
						CellRangeAddress car15 = new CellRangeAddress(0,0,t,t);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
						//sheet.addMergedRegion(car15);
						HSSFCell cell14 = row1.createCell(t);
						cell14.setCellStyle(style);
						cell14.setCellValue(new HSSFRichTextString(String.valueOf(i)));
						t++;
					}
				}
				i++;
				indexRow = indexRow+listSize;
			}
			
			for(TeachEvaluateItem full : dto.getFillList()){
				int listSize = full.getOptionList().size();
				CellRangeAddress car23 = new CellRangeAddress(1,1,indexRow,indexRow+listSize-1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car23, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car23, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car23, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car23, sheet); 
			    sheet.addMergedRegion(car23);
				HSSFCell cell23 = row2.createCell(indexRow);
				cell23.setCellStyle(style);
				cell23.setCellValue(new HSSFRichTextString(full.getItemName()));
				
				for(TeachEvaluateItemOption opt : full.getOptionList()){
					CellRangeAddress car33 = new CellRangeAddress(2,2,t,t);
				    RegionUtil.setBorderBottom(BorderStyle.THIN, car33, sheet); 
				    RegionUtil.setBorderLeft(BorderStyle.THIN, car33, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car33, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car33, sheet); 
				    //sheet.addMergedRegion(car33);
					HSSFCell cell33 = row3.createCell(t);
					cell33.setCellStyle(style);
					cell33.setCellValue(new HSSFRichTextString(opt.getOptionName()));
					
					
					if(t!=indexRow){						
						CellRangeAddress car14 = new CellRangeAddress(0,0,t,t);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet); 
						//sheet.addMergedRegion(car14);
						HSSFCell cell14 = row1.createCell(t);
						cell14.setCellStyle(style);
						cell14.setCellValue(new HSSFRichTextString(""));
						t++;
					}else{
						CellRangeAddress car15 = new CellRangeAddress(0,0,t,t);
						RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet); 
						RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet); 
						//sheet.addMergedRegion(car15);
						HSSFCell cell14 = row1.createCell(t);
						cell14.setCellStyle(style);
						cell14.setCellValue(new HSSFRichTextString(String.valueOf(i)));
						t++;
					}
				}
				i++;
				indexRow = indexRow+listSize;
			}
			List<String> obIdList = new ArrayList<String>();
			obIdList.add("school");
			obIdList.add(grade.getId());
			Set<String> teacherIdSet = gradeTeaMap.get(grade.getId());
			for(String teacherId : teacherIdSet){
				obIdList.add(teacherId);
			}
			int x=3;
			for(String teacherId : obIdList){
				HSSFRow row4 = sheet.createRow(x);
				CellRangeAddress car41 = new CellRangeAddress(x,x+1,0,0);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car41, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car41, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car41, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car41, sheet); 
			    sheet.addMergedRegion(car41);
				HSSFCell cell41 = row4.createCell(0);
				cell41.setCellStyle(style);
				cell41.setCellValue(new HSSFRichTextString(teacherNameMap.get(teacherId)));
				
				CellRangeAddress car42 = new CellRangeAddress(x,x,1,1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car42, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car42, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car42, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car42, sheet); 
			    //sheet.addMergedRegion(car42);
				HSSFCell cell42 = row4.createCell(1);
				cell42.setCellStyle(style);
				cell42.setCellValue(new HSSFRichTextString("比例（%）"));
				
				HSSFRow row5 = sheet.createRow(x+1);
				CellRangeAddress car43 = new CellRangeAddress(x+1,x+1,1,1);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car43, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car43, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car43, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car43, sheet); 
			    //sheet.addMergedRegion(car43);
				HSSFCell cell43 = row5.createCell(1);
				cell43.setCellStyle(style);
				cell43.setCellValue(new HSSFRichTextString("得分"));
				
				CellRangeAddress car44 = new CellRangeAddress(x,x+1,2,2);
			    RegionUtil.setBorderBottom(BorderStyle.THIN, car44, sheet); 
			    RegionUtil.setBorderLeft(BorderStyle.THIN, car44, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car44, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car44, sheet); 
			    sheet.addMergedRegion(car44);
				HSSFCell cell44 = row4.createCell(2);
				cell44.setCellStyle(style);
				if(null!=gradeItemScoreMap.get(teacherId+",countScore")){					
					cell44.setCellValue(new HSSFRichTextString(String.format("%.2f",gradeItemScoreMap.get(teacherId+",countScore"))+""));
				}else{
					cell44.setCellValue(new HSSFRichTextString(""));
				}
				int p = 3;
				int indexCol=3;
				for(TeachEvaluateItem eva : dto.getEvaluaList()){
					int listSize = eva.getOptionList().size(); 
					CellRangeAddress car46 = new CellRangeAddress(x+1,x+1,indexCol,indexCol+listSize-1);
				    RegionUtil.setBorderBottom(BorderStyle.THIN, car46, sheet); 
				    RegionUtil.setBorderLeft(BorderStyle.THIN, car46, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car46, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car46, sheet); 
				    sheet.addMergedRegion(car46);
					HSSFCell cell46 = row5.createCell(indexCol);
					cell46.setCellStyle(style);
					if(null!=gradeItemScoreMap.get(teacherId+","+eva.getId())){
						cell46.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(teacherId+","+eva.getId())+""));						
					}else{
						cell46.setCellValue(new HSSFRichTextString(""));
					}
					
					for(TeachEvaluateItemOption opt : eva.getOptionList()){
						CellRangeAddress car45 = new CellRangeAddress(x,x,p,p);
					    RegionUtil.setBorderBottom(BorderStyle.THIN, car45, sheet); 
					    RegionUtil.setBorderLeft(BorderStyle.THIN, car45, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car45, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car45, sheet); 
					    //sheet.addMergedRegion(car45);
						HSSFCell cell45 = row4.createCell(p);
						cell45.setCellStyle(style);
						if(null!=gradeItemScoreMap.get(teacherId+","+opt.getId())){							
							cell45.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(teacherId+","+opt.getId())+""));
						}else{
							cell45.setCellValue(new HSSFRichTextString(""));				
						}
						p++;
					}
					indexCol = indexCol+listSize;
				}
				for(TeachEvaluateItem full : dto.getFillList()){
					int listSize = full.getOptionList().size(); 					
					for(TeachEvaluateItemOption opt : full.getOptionList()){
						CellRangeAddress car45 = new CellRangeAddress(x,x+1,p,p);
					    RegionUtil.setBorderBottom(BorderStyle.THIN, car45, sheet); 
					    RegionUtil.setBorderLeft(BorderStyle.THIN, car45, sheet); 
						RegionUtil.setBorderTop(BorderStyle.THIN, car45, sheet); 
						RegionUtil.setBorderRight(BorderStyle.THIN, car45, sheet); 
					    sheet.addMergedRegion(car45);
						HSSFCell cell45 = row4.createCell(p);
						cell45.setCellStyle(style);
						if(null!=gradeItemScoreMap.get(teacherId+","+opt.getId())){							
							cell45.setCellValue(new HSSFRichTextString(gradeItemScoreMap.get(teacherId+","+opt.getId())+""));
						}else{
							cell45.setCellValue(new HSSFRichTextString(""));
						}
						p++;
					}
					indexCol = indexCol+listSize;
				}
				x=x+2;
			}
		}
		
		
		ExportUtils.outputData(workbook, project.getProjectName(), response);
	}
	
	@RequestMapping("/doDsExportAll2")
	public void doDsExportAll2(HttpServletRequest request,HttpServletResponse response){
		String projectId = request.getParameter("projectId");
		String acadyear = request.getParameter("acadyear");
		TeachEvaluateProject project = teachEvaluateProjectService.findOne(projectId);
		List<TeachEvaluateItem> itemList = teachEvaluateItemService.findByEvaluateType(project.getUnitId(), project.getEvaluateType(),project.getId());
		EvaluateTableDto dto = new EvaluateTableDto();
		for (TeachEvaluateItem item : itemList) {
			if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_EVALUA)){
				dto.getEvaluaList().add(item);
			}else if(StringUtils.equals(item.getItemType(), EvaluationConstants.ITEM_TYPE_FILL)){
				dto.getFillList().add(item);
			}else{
				dto.getAnswerList().add(item);
			}
		}
		// 年级维度与全校维度
		//班主任调查和导师调查
		Map<String,Float> gradeItemScoreMap= teachEvaluateResultStatService.getResultStatTeaSchExportDto(projectId);
		Set<String> gradeIds = new HashSet<String>();
		for(String gradeItemId : gradeItemScoreMap.keySet()){
			String gradeId = gradeItemId.split(",")[0];
			if(!StringUtils.equals(gradeId, "school")){
				gradeIds.add(gradeId);
			}
		}
		List<Grade> gradelist = new ArrayList<>();
		Set<String> teacherIdSetTemp = new HashSet<String>();
		Map<String, Set<String>> gradeTeaMap = new HashMap<String, Set<String>>();
		if(gradeIds.size()>0){
    		List<Grade> gradelistAll = SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(getLoginInfo().getUnitId(), acadyear), new TR<List<Grade>>(){});
    		for(Grade grade : gradelistAll){
    			if(gradeIds.contains(grade.getId())){
    				gradelist.add(grade);
    			}
    		}
            Map<String,Set<String>> tutorMap=SUtils.dt(tutorRemoteService.getTutorTeaStuMapByUnitId(getLoginInfo().getUnitId()),new TR<Map<String,Set<String>>>(){});
            Set<String> studentIdSet = new HashSet<String>();
            for(String teacherId : tutorMap.keySet()){
            	for(String stuId : tutorMap.get(teacherId)){
            		studentIdSet.add(stuId);
            	}
            }
            List<Student> stuList = new ArrayList<Student>();
            Map<String, String> stuGradeIdMap = new HashMap<String, String>();
            Set<String> clsIdSet = new HashSet<String>();
            List<Clazz> clsList = new ArrayList<Clazz>();
            Map<String, String> sectionClsMap = new HashMap<String, String>();
            if(CollectionUtils.isNotEmpty(studentIdSet)){
            	stuList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])),new TR<List<Student>>(){});
            	for(Student stu : stuList){
            		clsIdSet.add(stu.getClassId());
            	}
            	if(CollectionUtils.isNotEmpty(clsIdSet)){
            		clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])),new TR<List<Clazz>>(){});
            		for(Clazz cls : clsList){
            			sectionClsMap.put(cls.getId(), cls.getGradeId());
            		}
            	}
            	for(Student stu : stuList){
            		stuGradeIdMap.put(stu.getId(), sectionClsMap.get(stu.getClassId()));
            	}
            }
            Map<String, Set<String>> teaGradeSetMap = new HashMap<String, Set<String>>();
            for(String teacherId : tutorMap.keySet()){
            	Set<String> gradeIdSet = new HashSet<String>();
            	for(String stuId : tutorMap.get(teacherId)){
            		gradeIdSet.add(stuGradeIdMap.get(stuId));
            	}
            	teaGradeSetMap.put(teacherId, gradeIdSet);
            	teacherIdSetTemp.add(teacherId);
            }
            for(Grade grade : gradelist){
            	Set<String> teacherIdSet = new HashSet<String>();
            	for(String teacherId : teaGradeSetMap.keySet()){
            		if(teaGradeSetMap.get(teacherId).contains(grade.getId())){
            			teacherIdSet.add(teacherId);
            		}
            	}
            	gradeTeaMap.put(grade.getId(), teacherIdSet);
            }
		}
		Map<String, String> teacherNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(teacherIdSetTemp)){
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teacherIdSetTemp.toArray(new String[0])),new TR<List<Teacher>>(){});			
			for(Teacher teacher : teacherList){
				teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
			}
		}
		List<TeachEvaluateResult> resultList = teachEvaluateResultService.findBySubmitAndInProjectId(new String[]{projectId});
        Map<String, String> ansMap = new HashMap<String, String>();
		for(TeachEvaluateResult result : resultList){
			if(null==ansMap.get(result.getTeacherId()+result.getItemId())){
				ansMap.put(result.getTeacherId()+result.getItemId(), result.getResult());
			}else{
				ansMap.put(result.getTeacherId()+result.getItemId(), ansMap.get(result.getTeacherId()+result.getItemId())+"#####"+result.getResult());
			}
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();	
		for(Grade grade : gradelist){			
			HSSFSheet sheet = workbook.createSheet(grade.getGradeName());
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 2800);
			sheet.setColumnWidth(2, 2800);
			sheet.setColumnWidth(3, 2800);
			sheet.setColumnWidth(4, 2800);
			sheet.setColumnWidth(5, 2800);
			sheet.setColumnWidth(6, 2800);
			sheet.setColumnWidth(7, 2800);
			
			HSSFCellStyle style = workbook.createCellStyle();
			HSSFFont headfont = workbook.createFont();
			headfont.setFontHeightInPoints((short) 10);// 字体大小
			//headfont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			style.setFont(headfont);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setWrapText(true);
			
			HSSFRow row1 = sheet.createRow(0);
			CellRangeAddress car11 = new CellRangeAddress(0,0,0,0);
			RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet); 
			//sheet.addMergedRegion(car11);
			HSSFCell cell11 = row1.createCell(0);
			cell11.setCellStyle(style);
			cell11.setCellValue(new HSSFRichTextString("序号"));
			
			
			CellRangeAddress car12 = new CellRangeAddress(0,0,1,2);
			RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet); 
			RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet); 
			sheet.addMergedRegion(car12);
			HSSFCell cell12 = row1.createCell(1);
			cell12.setCellStyle(style);
			cell12.setCellValue(new HSSFRichTextString("导师"));
			int i=3;
			for(TeachEvaluateItem ans : dto.getAnswerList()){
				CellRangeAddress car13 = new CellRangeAddress(0,0,i,i+5);
				RegionUtil.setBorderBottom(BorderStyle.THIN, car13, sheet); 
				RegionUtil.setBorderLeft(BorderStyle.THIN, car13, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car13, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car13, sheet); 
				sheet.addMergedRegion(car13);
				HSSFCell cell13 = row1.createCell(i);
				cell13.setCellStyle(style);
				cell13.setCellValue(new HSSFRichTextString(ans.getItemName()));
				i=i+5;
			}
			Set<String> teacherIdSet = gradeTeaMap.get(grade.getId());
			int t=1;  
			int x=1;
			for(String teacherId : teacherIdSet){
				HSSFRow row2 = sheet.createRow(t);
				CellRangeAddress car21 = new CellRangeAddress(t,t+3,0,0);
				RegionUtil.setBorderBottom(BorderStyle.THIN, car21, sheet); 
				RegionUtil.setBorderLeft(BorderStyle.THIN, car21, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car21, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car21, sheet); 
				sheet.addMergedRegion(car21);
				HSSFCell cell21 = row2.createCell(0);
				cell21.setCellStyle(style);
				cell21.setCellValue(new HSSFRichTextString(String.valueOf(x)));
				
				CellRangeAddress car22 = new CellRangeAddress(t,t+3,1,2);
				RegionUtil.setBorderBottom(BorderStyle.THIN, car22, sheet); 
				RegionUtil.setBorderLeft(BorderStyle.THIN, car22, sheet); 
				RegionUtil.setBorderTop(BorderStyle.THIN, car22, sheet); 
				RegionUtil.setBorderRight(BorderStyle.THIN, car22, sheet); 
				sheet.addMergedRegion(car22);
				HSSFCell cell22 = row2.createCell(1);
				cell22.setCellStyle(style);
				cell22.setCellValue(new HSSFRichTextString(teacherNameMap.get(teacherId)));
				int p=3;
				for(TeachEvaluateItem ans : dto.getAnswerList()){
					CellRangeAddress car23 = new CellRangeAddress(t,t+3,p,p+5);
					RegionUtil.setBorderBottom(BorderStyle.THIN, car23, sheet); 
					RegionUtil.setBorderLeft(BorderStyle.THIN, car23, sheet); 
					RegionUtil.setBorderTop(BorderStyle.THIN, car23, sheet); 
					RegionUtil.setBorderRight(BorderStyle.THIN, car23, sheet); 
					sheet.addMergedRegion(car23);
					HSSFCell cell23 = row2.createCell(p);
					cell23.setCellStyle(style);
					cell23.setCellValue(new HSSFRichTextString(ansMap.get(teacherId+ans.getId())));
					p=p+5;
				}
				t=t+4;
				x++;
			}
		}
		
		ExportUtils.outputData(workbook, project.getProjectName(), response);
	}
}
