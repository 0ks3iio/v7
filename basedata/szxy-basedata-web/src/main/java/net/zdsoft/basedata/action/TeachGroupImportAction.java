package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/basedata/teachGroup/import")
public class TeachGroupImportAction extends DataImportAction{

	@Autowired
	private TeachGroupExService teachGroupExService;
	@Autowired
	private TeachGroupService teachGroupService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SchoolService schoolService;
	
	@RequestMapping("/index/page")
	public String showImporeIndex(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		// 业务名称
		map.put("businessName", "教研组");
		// 导入URL 
		map.put("businessUrl", "/basedata/teachGroup/import/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/teachGroup/import/template");
        map.put("exportErrorExcelUrl", "/basedata/teachGroup/import/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/teachGroup/import/validate");
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		return "/basedata/teachGroup/teachGroupImportIndex.ftl";
	}
	
	// 下载导入模板
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> titleList = getRowTitleList();//表头
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
	    
	    //标题行固定
        sheet.createFreezePane(0, 2);
	    
	    //sheet 合并单元格
	    CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
        sheet.addMergedRegion(car);
	    
	    // 注意事项样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow titleRow = sheet.createRow(0);
        //高度：7倍默认高度
        titleRow.setHeightInPoints(7*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        //注意事项
        String remark = getRemark();
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(style);
	    
        HSSFRow rowTitle = sheet.createRow(1);
        for(int j=0;j<size;j++){
        	sheet.setColumnWidth(j, 20 * 256);//列宽
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		String fileName = "教研组导入";
		ExportUtils.outputData(workbook, fileName, response);
	}

	@RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    @RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo){
		validRowStartNo = "1";
		if (StringUtils.isBlank(validRowStartNo)) {
		}
		try{
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	@RequestMapping("/import")
	@ResponseBody
	@Override
	public String dataImport(String filePath, String params) {
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		List<String[]> courseDatas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList().size());
		courseDatas.remove(0);
		//错误数据序列号
        int sequence = 0;
        int totalSize =courseDatas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(courseDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(courseDatas.size(),0,0,errorDataList,"");
        }
        
        List<Course> courseList = courseService.findByTopUnitAndUnitId(unitId);
        if(CollectionUtils.isEmpty(courseList)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="该单位课程库中没有课程";
            errorDataList.add(errorData);
            return result(courseDatas.size(),0,courseDatas.size(),errorDataList,"");
        }
        Map<String, List<Course>> subjectBySubjectNames=courseList.stream().collect(Collectors.groupingBy(Course::getSubjectName));
        
        List<Teacher> teacherList = teacherService.findByUnitId(unitId);
        if(CollectionUtils.isEmpty(teacherList)){
        	String[] errorData=new String[4];
        	sequence++;
        	errorData[0]=String.valueOf(sequence);
        	errorData[1]="";
        	errorData[2]="";
        	errorData[3]="该单位没有维护老师信息";
        	errorDataList.add(errorData);
        	return result(courseDatas.size(),0,courseDatas.size(),errorDataList,"");
        }
        //获取此单位的所有学段
    	Set<String> sectionSet = getSectionByUnit(unitId);
        Map<String,String> teacherIdByTeacherName = new HashMap<String, String>();
        List<String> sameTeacherNames = new ArrayList<String>();
        for (Teacher teacher : teacherList) {
        	if(teacherIdByTeacherName.containsKey(teacher.getTeacherName())){
        		sameTeacherNames.add(teacher.getTeacherName());
        		continue;
        	}
        	teacherIdByTeacherName.put(teacher.getTeacherName(), teacher.getId());
        }
        Map<String,Teacher> teacherId2Teacher = EntityUtils.getMap(teacherList, "id"); 
        
        List<TeachGroup> teachGroupList = teachGroupService.findBySchoolId(unitId);
        Map<String, TeachGroup> teachGroupId2TeachGroup = EntityUtils.getMap(teachGroupList, "id");
        Map<String, TeachGroup> teachGroupName2TeachGroup = EntityUtils.getMap(teachGroupList, "teachGroupName");
        Set<String> teachGroupIdSet = EntityUtils.getSet(teachGroupList, "id");
        List<TeachGroupEx> teachGroupExList = teachGroupExService.findByTeachGroupId(teachGroupIdSet.toArray(new String[teachGroupIdSet.size()]));
       
        
        // 已有教研组 与 负责人对应
        Map<String, List<Teacher>> teachGroupName2MainTeacherList = new HashMap<String, List<Teacher>>();
        // 已有教研组 与 成员对应
        Map<String, List<Teacher>> teachGroupName2RemberTeacherList = new HashMap<String, List<Teacher>>();
        for (TeachGroupEx teachGroupEx : teachGroupExList) {
        	String teachGroupId = teachGroupEx.getTeachGroupId();
        	String teachGroupName = teachGroupId2TeachGroup.get(teachGroupId).getTeachGroupName();
        	String teacherId = teachGroupEx.getTeacherId();
        	if(teachGroupEx.getType() == 1) {
        		//负责人
        		if(!teachGroupName2MainTeacherList.containsKey(teachGroupName)){
        			teachGroupName2MainTeacherList.put(teachGroupName, new ArrayList<Teacher>());
        		}
        		teachGroupName2MainTeacherList.get(teachGroupName).add(teacherId2Teacher.get(teacherId));
        	}else {
        		//成员
        		if(!teachGroupName2RemberTeacherList.containsKey(teachGroupName)){
        			teachGroupName2RemberTeacherList.put(teachGroupName, new ArrayList<Teacher>());
        		}
        		teachGroupName2RemberTeacherList.get(teachGroupName).add(teacherId2Teacher.get(teacherId));
        	}
        }
        //检查输出
//        for (TeachGroup teachGroup : teachGroupList) {
//			String teachGroupName = teachGroup.getTeachGroupName();
//			System.out.println(teachGroupName + "," + teachGroupName2MainTeacherList.get(teachGroupName).size() + "," + teachGroupName2RemberTeacherList.get(teachGroupName).size());
//		}
        
        // TODO
        Map<String, Set<String>> saveTeachGroupName2MainTeacherId = new HashMap<String, Set<String>>();
        Map<String, Set<String>> saveTeachGroupName2MemberTeacherId = new HashMap<String, Set<String>>();
        Map<String, String> saveTeachGroupName2SubjectId = new HashMap<String, String>();
        
        Map<String, Integer> orderByGroupName = new HashMap<String, Integer>();
        
        List<TeachGroup> saveTeachGroupList = new ArrayList<TeachGroup>();
        List<TeachGroupEx> saveTeachGroupExList = new ArrayList<TeachGroupEx>();
        
        int i=0;
        int successCount=0;
        for (String[] array : courseDatas) {
        	i++;
        	//校验教研组名称唯一，和数据库比，和前面可以保存的数据比
        	String teachGroupName = StringUtils.trimToEmpty(array[0]);
        	String section = StringUtils.trimToEmpty(array[1]);
        	String subjectName = StringUtils.trimToEmpty(array[2]);
        	String mainTeacher = StringUtils.trimToEmpty(array[3]);
        	String memberTeacher = StringUtils.trimToEmpty(array[4]);
        	String orderId=StringUtils.trimToEmpty(array[5]);
//        	System.out.println(teachGroupName + "," + subjectCode + "," + mainTeacher + "," + memberTeacher);
        	Course course = null;
        	Integer orderInt=null;
        	//学段验证
        	if(StringUtils.isBlank(section)){
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="学段必须输入";
                errorDataList.add(errorData);
                continue;
        	}
    		if(!section.matches("^[0-9]*$")){
    			String[] errorData=new String[4];
    			sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
    			errorData[1]="第"+i+"行";
    			errorData[2]=section;
    			errorData[3]="学段需为数字";
    			errorDataList.add(errorData);
    			continue;
    		}
			if(!sectionSet.contains(section)){
				String[] errorData=new String[4];
				sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
				errorData[1]= "第"+i+"行";
				errorData[2]=section;
				errorData[3]="您的单位不支持您输入的学段";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(orderId)) {
				if(!orderId.matches("^[0-9]*$")) {
					String[] errorData=new String[4];
	    			sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	    			errorData[1]="第"+i+"行";
	    			errorData[2]=orderId;
	    			errorData[3]="排序号需为数字";
	    			errorDataList.add(errorData);
	    			continue;
				}
				orderInt = Integer.parseInt(orderId);
				if(orderInt>9999) {
					String[] errorData=new String[4];
	    			sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	    			errorData[1]="第"+i+"行";
	    			errorData[2]=orderId;
	    			errorData[3]="排序号不能超过4位";
	    			errorDataList.add(errorData);
	    			continue;
				}
				
        	}
        	
        	//校验 科目
        	if(StringUtils.isNotBlank(subjectName)) {
	        	//校验 科目是否存在
        		subjectName = subjectName.replaceAll("\\u00A0", " ").replaceAll("\u3000", " ");
        		subjectName = subjectName.trim();
	        	boolean subjectExist = subjectBySubjectNames.containsKey(subjectName);
	        	if(subjectExist == false) {
	        		String[] errorData=new String[4];
	                sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	                errorData[1]="第"+i+"行";
	                errorData[2]=subjectName;
	                errorData[3]="课程库中不存在该科目或该科目未启用";
	                errorDataList.add(errorData);
	                continue;
	        	}
	        	List<Course> cList = subjectBySubjectNames.get(subjectName);
				for(Course cc:cList) {
					if(StringUtils.isNotBlank(cc.getSection()) && cc.getSection().indexOf(String.valueOf(section))>=0){ 
						course=cc;
						break;
					}else{
						course=null;
					}
				}
				if(course==null) {
					String[] errorData=new String[4];
	                sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	                errorData[1]="第"+i+"行";
	                errorData[2]=subjectName;
	                errorData[3]="所填写学段中未开设课程"+subjectName;
	                errorDataList.add(errorData);
	                continue;
				}
        	}else {
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="科目必须输入";
                errorDataList.add(errorData);
                continue;
        	}
        	
        	// 检验老师
        	
        	//校验负责人格式，
        	int flag = 1;
        	if(StringUtils.isNotBlank(mainTeacher)) {
	        	//检验 老师是否存在
        		mainTeacher = mainTeacher.replaceAll("\\u00A0", " ").replaceAll("\u3000", " ");
	        	String[] tempMainTeacher = mainTeacher.replaceAll("，",",").split(",");
	        	for (String str : tempMainTeacher) {
	        		String teacherName = StringUtils.isBlank(str)?str:str.trim();
					boolean teachExist = teacherIdByTeacherName.containsKey(teacherName);
					if(!teachExist) {
						String[] errorData=new String[4];
		                sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
		                errorData[1]="第"+i+"行";
		                errorData[2]=teacherName;
		                errorData[3]="该教师不存在";
		                errorDataList.add(errorData);
		                flag = 0;
		                continue;
					}else if(sameTeacherNames.contains(teacherName)){
						String[] errorData=new String[4];
		                sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
		                errorData[1]="第"+i+"行";
		                errorData[2]=teacherName;
		                errorData[3]="该教师姓名存在多个";
		                errorDataList.add(errorData);
		                flag = 0;
		                continue;
					}
					if( flag == 0) {
						break;
					}
				}
        	}else {
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="负责人必须输入";
                errorDataList.add(errorData);
                continue;
        	}
        	if(flag == 0) {
        		continue;
        	}
        	
        	int flag2 = 1;
        	if(StringUtils.isNotBlank(memberTeacher)) {
	        	//检验 老师是否存在
        		memberTeacher = memberTeacher.replaceAll("\\u00A0", " ").replaceAll("\u3000", " ");
	        	String[] tempMemberTeacher = memberTeacher.replaceAll("，",",").split(",");
	        	for (String str : tempMemberTeacher) {
	        		String teacherName = StringUtils.isBlank(str)?str:str.trim();
					boolean teachExist = teacherIdByTeacherName.containsKey(teacherName);
					if(!teachExist) {
						String[] errorData=new String[4];
		                sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
		                errorData[1]="第"+i+"行";
		                errorData[2]=teacherName;
		                errorData[3]="该教师不存在";
		                errorDataList.add(errorData);
		                flag2 = 0;
		                continue;
					}else if(sameTeacherNames.contains(teacherName)){
						String[] errorData=new String[4];
		                sequence++;
                        // errorData[0]=String.valueOf(sequence);
                        errorData[0]=i+"";
		                errorData[1]="第"+i+"行";
		                errorData[2]=teacherName;
		                errorData[3]="该教师姓名存在多个";
		                errorDataList.add(errorData);
		                flag2 = 0;
		                continue;
					}
					if( flag2 == 0) {
						break;
					}
				}
        	}else {
        		String[] errorData=new String[4];
                sequence++;
                // errorData[0]=String.valueOf(sequence);
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]="";
                errorData[3]="成员必须输入";
                errorDataList.add(errorData);
                continue;
        	}
        	if(flag2 == 0) {
        		continue;
        	}
        	
        	String[] mainTeachers = mainTeacher.replaceAll("，",",").split(",");
        	String[] memberTeachers = memberTeacher.replaceAll("，",",").split(",");
        	
//        	int flag3 = 1;
//        	for (String string1 : mainTeachers) {
//        		String teacher1 = StringUtils.isBlank(string1)?string1:string1.trim();
//        		for (String string2 : memberTeachers) {
//        			String teacher2 = StringUtils.isBlank(string2)?string2:string2.trim();
//    				if(teacher1.equals(teacher2)) {
//    					String[] errorData=new String[4];
//		                sequence++;
//		                errorData[0]=String.valueOf(sequence);
//		                errorData[1]="第"+i+"行";
//		                errorData[2]=teacher1;
//		                errorData[3]="该教师同时出现在负责人和成员中";
//		                errorDataList.add(errorData);
//		                flag3 = 0;
//		                break;
//    				}
//    			}
//        		if(flag3 == 0) {
//        			break;
//        		}
//			}
//        	if(flag3 == 0) {
//        		continue;
//        	}
        	
        	// 教研组名称
        	
        	if(!StringUtils.isNotBlank(teachGroupName)) {
        		teachGroupName = subjectName + "组";
        	}else{
        		teachGroupName=teachGroupName.trim();
        	}
        	//教研组名称已存在
        	if(teachGroupName2TeachGroup.containsKey(teachGroupName)) {
        		// 判断已存在的教研组科目是否和这次维护科目是否相同
        		String haveSubjectId = teachGroupName2TeachGroup.get(teachGroupName).getSubjectId();
        		String saveSujectId = course.getId();
        		// 不同
        		if(!haveSubjectId.equals(saveSujectId)) {
        			String[] errorData=new String[4];
	                sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	                errorData[1]="第"+i+"行";
	                errorData[2]=subjectName;
	                errorData[3]="该教研组已存在，科目与" + subjectName + "对应的科目不一致";
	                errorDataList.add(errorData);
	                continue;
        		}
        		if(saveTeachGroupName2MainTeacherId.containsKey(teachGroupName)) {
        			String[] errorData=new String[4];
	                sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	                errorData[1]="第"+i+"行";
	                errorData[2]=teachGroupName;
	                errorData[3]="导入的教研组名称应唯一";
	                errorDataList.add(errorData);
	                continue;
        		}
        		
        		saveTeachGroupName2SubjectId.put(teachGroupName, saveSujectId);
        		if(!saveTeachGroupName2MainTeacherId.containsKey(teachGroupName)){
        			saveTeachGroupName2MainTeacherId.put(teachGroupName, new HashSet<String>());
        		}
        		if(!saveTeachGroupName2MemberTeacherId.containsKey(teachGroupName)){
        			saveTeachGroupName2MemberTeacherId.put(teachGroupName, new HashSet<String>());
        		}
        		
//        		int flag5 = 1;
        		for (String str : mainTeachers) {
        			String teacherName = StringUtils.isBlank(str)?str:str.trim();
        			String tempTeaId = teacherIdByTeacherName.get(teacherName);
        			int flag4 = 1;
//        			List<Teacher> meTeacherList = teachGroupName2RemberTeacherList.get(teachGroupName);
//        			for (Teacher teacher : meTeacherList) {
//						if(tempTeaId.equals(teacher.getId())) {
//							String[] errorData=new String[4];
//			                sequence++;
//			                errorData[0]=String.valueOf(sequence);
//			                errorData[1]="第"+i+"行";
//			                errorData[2]=teacherName;
//			                errorData[3]="教研组已存在，该老师已经在该教研组的成员中，不能同时为负责人";
//			                errorDataList.add(errorData);
//			                flag4 = 0;
//			                flag5 = 0;
//			                break;
//						}
//					}
//        			if(flag4 == 0) {
//        				break;
//        			}
//        			flag4 = 1;
        			List<Teacher> maTeacherList = teachGroupName2MainTeacherList.get(teachGroupName);
        			for (Teacher teacher : maTeacherList) {
						if(teacher.getId().equals(tempTeaId)) {
							flag4 = 0;
							break;
						}
					}
        			if(flag4 == 1) {
        				saveTeachGroupName2MainTeacherId.get(teachGroupName).add(tempTeaId);
        			}
        			
        		}
//        		if(flag5 == 0) {
//        			continue;
//        		}
        		
//        		int flag7 = 1;
        		for (String str : memberTeachers) {
        			String teacherName = StringUtils.isBlank(str)?str:str.trim();
        			String tempTeaId = teacherIdByTeacherName.get(teacherName);
        			int flag6 = 1;
//        			List<Teacher> maTeacherList = teachGroupName2MainTeacherList.get(teachGroupName);
//        			for (Teacher teacher : maTeacherList) {
//						if(tempTeaId.equals(teacher.getId())) {
//							String[] errorData=new String[4];
//			                sequence++;
//			                errorData[0]=String.valueOf(sequence);
//			                errorData[1]="第"+i+"行";
//			                errorData[2]=teacherName;
//			                errorData[3]="教研组已存在，该老师已经在该教研组的负责人中，不能同时为成员";
//			                errorDataList.add(errorData);
//			                flag6 = 0;
//			                flag7 = 0;
//			                break;
//						}
//					}
//        			if(flag6 == 0) {
//        				break;
//        			}
//        			flag6 = 1;
        			List<Teacher> reTeacherList = teachGroupName2RemberTeacherList.get(teachGroupName);
        			for (Teacher teacher : reTeacherList) {
						if(teacher.getId().equals(tempTeaId)) {
							flag6 = 0;
							break;
						}
					}
        			if(flag6 == 1) {
        				saveTeachGroupName2MemberTeacherId.get(teachGroupName).add(tempTeaId);
        			}
        			
        		}
//        		if(flag7 == 0) {
//        			continue;
//        		}
        		
        	}else {
        		if(saveTeachGroupName2MainTeacherId.containsKey(teachGroupName)) {
        			String[] errorData=new String[4];
	                sequence++;
                    // errorData[0]=String.valueOf(sequence);
                    errorData[0]=i+"";
	                errorData[1]="第"+i+"行";
	                errorData[2]=teachGroupName;
	                errorData[3]="导入的教研组名称应唯一";
	                errorDataList.add(errorData);
	                continue;
        		}
        		
        		saveTeachGroupName2SubjectId.put(teachGroupName, course.getId());
        		if(!saveTeachGroupName2MainTeacherId.containsKey(teachGroupName)) {
        			saveTeachGroupName2MainTeacherId.put(teachGroupName, new HashSet<String>());
        		}
        		if(!saveTeachGroupName2MemberTeacherId.containsKey(teachGroupName)) {
        			saveTeachGroupName2MemberTeacherId.put(teachGroupName, new HashSet<String>());
        		}
        		for (String str : mainTeachers) {
        			String teacherName = StringUtils.isBlank(str)?str:str.trim();
        			String tempTeaId = teacherIdByTeacherName.get(teacherName);
    				saveTeachGroupName2MainTeacherId.get(teachGroupName).add(tempTeaId);
        		}
        		
        		for (String str : memberTeachers) {
        			String teacherName = StringUtils.isBlank(str)?str:str.trim();
        			String tempTeaId = teacherIdByTeacherName.get(teacherName);
    				saveTeachGroupName2MemberTeacherId.get(teachGroupName).add(tempTeaId);
        			
        		}
        	}
        	
        	//orderInt排序号 如果多行同样教研组 取最后的排序号
        	if(orderInt!=null) {
        		orderByGroupName.put(teachGroupName, orderInt);
        	}
        	
        	successCount++;
        }

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题行固定
            sheet.createFreezePane(0, 1);

            List<String> titleList = getRowTitleList();
            titleList.add("错误数据");
            titleList.add("错误原因");

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int j=0;j<titleList.size();j++){
            	sheet.setColumnWidth(j, 20 * 256);//列宽
                HSSFCell cell = rowTitle.createCell(j);
                cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
            }

            for(int j=0;j<errorDataList.size();j++){
                HSSFRow row = sheet.createRow(j+1);
                String[] datasDetail = courseDatas.get(Integer.parseInt(errorDataList.get(j)[0]) - 1);
                for (int k=0;k<titleList.size();k++) {
                    HSSFCell cell = row.createCell(k);
                    if (k<titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[k]));
                    } else if (k==titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[2]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(j)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }
        
//        Map<String, Set<String>> saveTeachGroupName2MainTeacherId = new HashMap<String, Set<String>>();
//        Map<String, Set<String>> saveTeachGroupName2MemberTeacherId = new HashMap<String, Set<String>>();
//        Map<String, String> saveTeachGroupName2SubjectId = new HashMap<String, String>();
//        System.out.println("统计！！！！！！！！！！");
        TeachGroup teachGroup = new TeachGroup();
        TeachGroupEx teachGroupEx = new TeachGroupEx();
        for(Map.Entry<String, String> entry : saveTeachGroupName2SubjectId.entrySet()){
        	String tgName = entry.getKey();
        	String suId = entry.getValue();
        	String teachGroupId = "";
        	if(!teachGroupName2TeachGroup.containsKey(tgName)) {
        		teachGroup = new TeachGroup();
        		teachGroupId = UuidUtils.generateUuid();
        		teachGroup.setId(teachGroupId);
        		teachGroup.setSubjectId(suId);
        		teachGroup.setIsDeleted(0);
        		teachGroup.setSchoolId(unitId);
        		teachGroup.setTeachGroupName(tgName);
        		teachGroup.setCreationTime(new Date());
        		teachGroup.setModifyTime(new Date());
        		if(orderByGroupName.containsKey(tgName)) {
        			teachGroup.setOrderId(orderByGroupName.get(tgName));
        		}
        		saveTeachGroupList.add(teachGroup);
        	}else {
        		//修改排序号
        		teachGroup= teachGroupName2TeachGroup.get(tgName);
        		if(orderByGroupName.containsKey(tgName)) {
        			teachGroup.setOrderId(orderByGroupName.get(tgName));
        			saveTeachGroupList.add(teachGroup);
        		}
        		teachGroupId = teachGroup.getId();
        	}
        	Set<String> maTeId = saveTeachGroupName2MainTeacherId.get(tgName);
        	for (String string : maTeId) {
        		teachGroupEx = new TeachGroupEx();
        		teachGroupEx.setId(UuidUtils.generateUuid());
        		teachGroupEx.setTeachGroupId(teachGroupId);
        		teachGroupEx.setTeacherId(string);
        		teachGroupEx.setType(1);
        		saveTeachGroupExList.add(teachGroupEx);
			}
        	
        	Set<String> meTeId = saveTeachGroupName2MemberTeacherId.get(tgName);
        	for (String string : meTeId) {
        		teachGroupEx = new TeachGroupEx();
        		teachGroupEx.setId(UuidUtils.generateUuid());
        		teachGroupEx.setTeachGroupId(teachGroupId);
        		teachGroupEx.setTeacherId(string);
        		teachGroupEx.setType(0);
        		saveTeachGroupExList.add(teachGroupEx);
			}
        }
        
        try{
    		teachGroupService.save(saveTeachGroupList, saveTeachGroupExList);
    	}catch(Exception e){
    		e.printStackTrace();
    		return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
    	}
        
        int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
		return result;
	}

	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	 }
	
	@Override
	public String getObjectName() {
		return "teachGroupImport";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、改变选项后请重新上传模板</p>";
		return desc;
	}

	//获取表头
	@Override
	public List<String> getRowTitleList() {
		List<String> titleList = new ArrayList<>();
		titleList.add("教研组名称");
		titleList.add("学段");
		titleList.add("科目");
		titleList.add("负责人");
		titleList.add("成员");
		titleList.add("排序号");
		return titleList;
	}

	public String getRemark(){
		String sectionStrs = getRemarkSectionDisplay();
		String remark = "填写注意：\n"
				+ "1.教研组名称唯一，教研组名称可以不填，默认为科目名字+组\n"
				+ "2.学段请填写数字,学段：" + sectionStrs +"\n"
				+ "3.只能输入一门科目\n"
				+ "4.负责人和成员支持多位老师，格式为:教师姓名,教师姓名\n"
				+ "5.排序号，不能超过4位的整数，可以不填\n";
		return remark;
	}
	
	/**
	 * 获取单位支持的学段
	 * @param unitId
	 * @return
	 */
	public Set<String> getSectionByUnit(String unitId) {
		Set<String> sectionSet = new TreeSet<>();
		String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
    	List<McodeDetail> mdList = SUtils.dt(mcodeRemoteService.findAllByMcodeIds(sectionMcode), McodeDetail.class);
		School school = schoolService.findOne(unitId);
		if(school==null || school.getSections()==null){
			sectionSet = EntityUtils.getSet(mdList, McodeDetail::getThisId);
		}else{
			sectionSet = new TreeSet<>(Arrays.asList(school.getSections().split(",")));
		}
		return sectionSet;
	}
	
	private String getRemarkSectionDisplay() {
		Set<String> sectionSet = getSectionByUnit(getLoginInfo().getUnitId());
		String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
		List<McodeDetail> mdList = SUtils.dt(mcodeRemoteService.findAllByMcodeIds(sectionMcode), McodeDetail.class);
		Map<String, String> sectionMap = EntityUtils.getMap(mdList, McodeDetail::getThisId,McodeDetail::getMcodeContent);
		StringBuffer sectionStrs = new StringBuffer();
		for (String section : sectionSet) {
			sectionStrs.append(section).append(sectionMap.get(section)).append("  ");
		}
		return sectionStrs.toString();
	}

}
