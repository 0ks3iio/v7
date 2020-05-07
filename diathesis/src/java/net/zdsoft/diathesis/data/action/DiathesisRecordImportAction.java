package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/diathesis/recordImport")
public class DiathesisRecordImportAction extends DataImportAction{
	
	private Logger logger = Logger.getLogger(DiathesisRecordImportAction.class);
	@Autowired
	private DiathesisCustomAuthorService diathesisCustomAuthorService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DiathesisStructureService diathesisStructureService;
	@Autowired
	private DiathesisOptionService diathesisOptionService;
	@Autowired
	private DiathesisRecordService diathesisRecordService;
	@Autowired
	private DiathesisRoleService diathesisRoleService;
	@Autowired
	private DiathesisProjectExService diathesisProjectExService;

	@Override
	public String getObjectName() {
		return "diathesisRecordImportAction";
	}

	@Override
	public String getDescription() {
		String desc = "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、上传文件中请不要在数据内容中出现<font style='color:red;'>空行</font>状态，否则可能造成报错信息的提示对象中对应的行数不准确</p>"
				+ "<p>2、改变选项后请重新上传模板</p>";
		return desc;
	}

	@Override
	public List<String> getRowTitleList() {
		return null;
	}
	
	public List<String> getRowTitleList2(String projectId) {
		List<String> rowTitleList=new ArrayList<String>();
		rowTitleList.add("学号");
		rowTitleList.add("姓名");
		//List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(getLoginInfo().getUnitId(), projectId);
		List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId( projectId);
		List<String> titleList = structureList.stream().filter(e->!DiathesisConstant.DATA_TYPE_4.equals(e.getDataType())).map(e->e.getTitle()).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(titleList)){
			rowTitleList.addAll(titleList);
		}
		return rowTitleList;
	}

	/**
	 * 导入数据
	 */
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {


		logger.info("业务数据处理中......");
		//获取参数
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = getLoginInfo().getUnitId();
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		String projectId = (String) performance.get("projectId");
		List<String> titleList = getRowTitleList2(projectId);


		//获取上传数据 第一行行标是0
		List<String[]> rowDatas = ExcelUtils.readExcelIgnoreDesc(filePath, titleList.size());
		rowDatas.remove(0);
		
		//错误数据序列号
        int sequence = 0;
        int totalSize =rowDatas.size();
		List<String[]> errorDataList=new ArrayList<String[]>();
		
		//判断基本数据
        if(CollectionUtils.isEmpty(rowDatas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
        }
        if(StringUtils.isBlank(unitId) || StringUtils.isBlank(acadyear) || StringUtils.isBlank(semester) || StringUtils.isBlank(projectId)){
			String[] errorData=new String[4];
			sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="参数丢失，无法保存";
            errorDataList.add(errorData);
            return result(totalSize,0,totalSize,errorDataList,"");
		}
        
        //根据权限获取学生数据
		//所有年级
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY,BaseConstants.SECTION_JUNIOR,BaseConstants.SECTION_HIGH_SCHOOL}),Grade.class);
        //所有学生
        List<Student> studentList = SUtils.dt(studentRemoteService.findByGradeIds(EntityUtils.getList(gradeList, Grade::getId).toArray(new String[gradeList.size()])),Student.class);

        Map<String, String> stuCodeMap = new HashMap<String, String>();
		List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(EntityUtils.getSet(studentList, Student::getClassId).toArray(new String[0])),Clazz.class);
		Map<String, List<String>> classStuMap = EntityUtils.getListMap(studentList, Student::getClassId, Student::getId);
		Map<String, List<String>> gradeClaMap = EntityUtils.getListMap(clazzList, Clazz::getGradeId, Clazz::getId);



		//todo 权限控制
		Map<String, List<String>> roleMap = diathesisRoleService.findRoleByUserId(unitId, getLoginInfo().getUserId());
		List<String> roles = getProjectExRole(projectId, unitId);
		Set<String> myRoleStuIdSet=new HashSet<>();
		if (roleMap!=null){
			if(roles.contains(DiathesisConstant.ROLE_GRADE)&&roleMap.containsKey(DiathesisConstant.ROLE_2)){
				//年级主任
				List<String> gradeIds = roleMap.get(DiathesisConstant.ROLE_2);
				for (String gradeId : gradeIds) {
					Set<String> stuIds = gradeClaMap.get(gradeId).stream().flatMap(x -> classStuMap.get(x).stream()).collect(Collectors.toSet());
					myRoleStuIdSet.addAll(stuIds);
				}
			}
			if(roles.contains(DiathesisConstant.ROLE_CLASS)&&roleMap.containsKey(DiathesisConstant.ROLE_3)){
				//班主任
				List<String> classIds = roleMap.get(DiathesisConstant.ROLE_3);
				Set<String> stuIds = classIds.stream().flatMap(x -> classStuMap.get(x).stream()).collect(Collectors.toSet());
				myRoleStuIdSet.addAll(stuIds);
			}
			if(roles.contains(DiathesisConstant.ROLE_TUTOR)&&roleMap.containsKey(DiathesisConstant.ROLE_4)){
				//导师
				List<String> stuIds = roleMap.get(DiathesisConstant.ROLE_4);
				myRoleStuIdSet.addAll(stuIds);
			}
		}

		Map<String, Student> studentMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(studentList)){
			for (Student student : studentList) {
				if(myRoleStuIdSet.contains(student.getId())){
					studentMap.put(student.getStudentCode(),student);
				}
			}
		}


		for (Grade grade : gradeList) {
			String[] openAcadyear = grade.getOpenAcadyear().split("-");
			String[] inputAcadyear = acadyear.split("-");
			String gradeCode = grade.getSection().toString();
			gradeCode += (Integer.parseInt(inputAcadyear[0])-Integer.parseInt(openAcadyear[0])+1);
//			gradeCode += semester;
			if(gradeClaMap.containsKey(grade.getId())){
				for (String claId : gradeClaMap.get(grade.getId())) {
					for (String stuId : classStuMap.get(claId)) {
						if(myRoleStuIdSet.contains(stuId)) {
							stuCodeMap.put(stuId, gradeCode);
						}
					}
				}
			}
		}
        
        //获取自定义字段
        List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectId(projectId);
		String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisOption> optionList = diathesisOptionService.findListByUnitIdAndProjectId(usingUnitId, projectId);
        Map<String, List<String>> optionListMap = EntityUtils.getListMap(optionList, DiathesisOption::getStructureId, DiathesisOption::getContentTxt);
        Map<String, Map<String, String>> optionMap = new HashMap<String, Map<String,String>>();
        for (DiathesisOption option : optionList) {
			if(!optionMap.containsKey(option.getStructureId())){
				optionMap.put(option.getStructureId(), new HashMap<String, String>());
			}
			optionMap.get(option.getStructureId()).put(option.getContentTxt(), option.getId());
		}
        Map<String, DiathesisStructure> structureMap = EntityUtils.getMap(structureList, DiathesisStructure::getTitle);
        
        DiathesisRecord record = null;
        List<DiathesisRecord> saveRecordList = new ArrayList<DiathesisRecord>();
        DiathesisRecordInfo recordInfo = null;
        List<DiathesisRecordInfo> saveInfoList = new ArrayList<DiathesisRecordInfo>();
        
        int successCount=0;
		int i=0;
		
		Student student = null;
		for(String[] arr:rowDatas){
			i++;
			//判断学号
			String studentCode = arr[0]==null?"":arr[0].trim();
			if(StringUtils.isBlank(studentCode)){
				String[] errorData=new String[4];
                sequence++;
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]=studentCode;
                errorData[3]="学号不能为空";
                errorDataList.add(errorData);
                continue;
			}else if(!studentMap.containsKey(studentCode)){
				String[] errorData = new String[4];
                errorData[0]=i+"";
				errorData[1]="第"+i+"行";
				errorData[2]=studentCode;
				errorData[3]="当前权限下不存在该学号所属的学生";
				errorDataList.add(errorData);
				continue;
			}
			String studentName = arr[1]==null?"":arr[1].trim();
			if(StringUtils.isBlank(studentName)){
				String[] errorData = new String[4];
                errorData[0]=i+"";
				errorData[1]="第"+i+"行";
				errorData[2]=studentName;
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
			}else if(!studentName.equals(studentMap.get(studentCode).getStudentName())){
				String[] errorData = new String[4];
                errorData[0]=i+"";
				errorData[1]="第"+i+"行";
				errorData[2]="姓名："+studentName+"；学号："+studentCode;
				errorData[3]="学生姓名与学号不匹配";
				errorDataList.add(errorData);
				continue;
			}
			
			student = studentMap.get(studentCode);
			boolean flag = false;
			for (int j = 2; j < titleList.size(); j++) {
				String contentTxt = arr[j]==null?"":arr[j].trim();
				if(StringUtils.isBlank(contentTxt)){
					String[] errorData = new String[4];
	                errorData[0]=i+"";
					errorData[1]="第"+i+"行";
					errorData[2]=contentTxt;
					errorData[3]="录入值不能为空";
					errorDataList.add(errorData);
					flag=true;
					break;
				}
				
				String title = titleList.get(j);
				DiathesisStructure structure = structureMap.get(title);
				if(DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType())){
					if(!(optionListMap.containsKey(structure.getId())&&optionListMap.get(structure.getId()).contains(contentTxt))){
						String[] errorData = new String[4];
		                errorData[0]=i+"";
						errorData[1]="第"+i+"行";
						errorData[2]=contentTxt;
						errorData[3]=title+"中没有设置该选项";
						errorDataList.add(errorData);
						flag=true;
						break;
					}
				}else if(DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())){
					optionListMap.get(structure.getId());
					String[] contentTxts = contentTxt.replace("，", ",").split(",");
					boolean flag2 = false;
					for (String txt : contentTxts) {
						if(!(optionListMap.containsKey(structure.getId())&&optionListMap.get(structure.getId()).contains(txt))){
							String[] errorData = new String[4];
			                errorData[0]=i+"";
							errorData[1]="第"+i+"行";
							errorData[2]=txt;
							errorData[3]=title+"中没有设置该选项";
							errorDataList.add(errorData);
							flag2=true;
							break;
						}
					}
					if(flag2){
						flag = true;
						break;
					}
				}
			}
			
			if(flag){
				continue;
			}
			
			record = new DiathesisRecord();
			record.setId(UuidUtils.generateUuid());
			record.setUnitId(unitId);
			record.setProjectId(projectId);
			record.setStuId(student.getId());
			record.setStatus(DiathesisConstant.AUDIT_STATUS_READY);
			record.setCreationTime(new Date());
			record.setOperator(getLoginInfo().getRealName());
			record.setAcadyear(acadyear);
			record.setSemester(Integer.parseInt(semester));
			record.setGradeCode(stuCodeMap.get(student.getId()));
			saveRecordList.add(record);
			
			for (int j = 2; j < titleList.size(); j++) {
				String contentTxt = arr[j]==null?"":arr[j].trim();
				String title = titleList.get(j);
				DiathesisStructure structure = structureMap.get(title);
				
				recordInfo = new DiathesisRecordInfo();
				recordInfo.setId(UuidUtils.generateUuid());
				recordInfo.setUnitId(unitId);
				recordInfo.setRecordId(record.getId());
				recordInfo.setStructureId(structure.getId());
				
				String value = "";
				if(DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())){
					String[] contentTxts = contentTxt.replace("，", ",").split(",");
					for (String txt : contentTxts) {
						value += ","+optionMap.get(structure.getId()).get(txt);
					}
					value = value.substring(1);
				}else if(DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType())){
						value = optionMap.get(structure.getId()).get(contentTxt);
				}else{
					value = contentTxt;
				}
				recordInfo.setContentTxt(value);
				saveInfoList.add(recordInfo);
			}
			
			successCount++;
		}//for结尾
		
		// 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();

            List<String> titleList2 = getRowTitleList2(projectId);
            titleList2.add("错误数据");
            titleList2.add("错误原因");

            // HSSFCell----单元格样式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);//水平
            style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            style.setWrapText(true);//自动换行

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            errorStyle.setAlignment(HorizontalAlignment.LEFT);
            errorStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
            errorStyle.setWrapText(true);//自动换行
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int j=0;j<titleList2.size();j++){
                HSSFCell cell = rowTitle.createCell(j);
                cell.setCellValue(new HSSFRichTextString(titleList2.get(j)));
                cell.setCellStyle(style);
            }

            for(int j=0;j<errorDataList.size();j++){
                HSSFRow row = sheet.createRow(j+1);
                String[] datasDetail = rowDatas.get(Integer.parseInt(errorDataList.get(j)[0]) - 1);
                for (int k=0;k<titleList2.size();k++) {
                    HSSFCell cell = row.createCell(k);
                    if (k<titleList2.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[k]));
                        cell.setCellStyle(style);
                    } else if (k==titleList2.size()-2) {
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
		
		try {
			diathesisRecordService.deleteAndSave(unitId, null, saveRecordList, saveInfoList);
		} catch (Exception e) {
			e.printStackTrace();
			return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
		}
		int errorCount = totalSize - successCount;
	    String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
	    logger.info("导入结束......");
		return result;
	}
	
	private String result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList, String errorExcelPath){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        importResultJson.put("errorExcelPath", errorExcelPath);
        return importResultJson.toJSONString();
	}
	
	/**
	 * 下载写实记录导入模板
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
		String projectId = request.getParameter("projectId");
		List<String> titleList = getRowTitleList2(projectId);//表头
		int size = titleList.size();
		
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
        //列宽
        for(int i=0;i<size;i++){
        	 sheet.setColumnWidth(i, 15 * 256);
        }
	    
	    //单元格样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        
        //首行注意事项
        CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
        sheet.addMergedRegion(car);
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(5*sheet.getDefaultRowHeightInPoints());//5倍默认高度
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(style);
        String remark = getRemark();//注意事项
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(style);
	    
        //表头
        HSSFRow rowTitle = sheet.createRow(1);
        for(int j=0;j<size;j++){
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		
		String fileName = "写实记录导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}
	
	 /**
     * 模板校验
     * @return
     */
	@ResponseBody
    @RequestMapping("/validate")
    public String validate(String filePath, String projectId){
        try{
            List<String> titleList = getRowTitleList2(projectId);
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, titleList.size());
           String errorMsg = templateValidate(datas, titleList);
           if(StringUtils.isNotBlank(errorMsg)){
        	   return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError(errorMsg));
           }
        }catch (Exception e) {
            e.printStackTrace();
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("上传文件不符合模板要求"));
        }
        return success(getLoginInfo().getRealName());
    }
    
	private String getRemark(){
		
		String remark = "填写注意：\n"
				+ "1.班主任只能导入本班学生写实记录，年级主任只能导入本年级学生写实记录，管理员可以导入所有学生写实记录\n"
				+ "2.多选项填写时以逗号相隔，如绘画，跳远";
		return remark;
	}

	private List<String> getProjectExRole(String projectId, String unitId) {
		//todo v1.2.0 把审核人录入人从 原来的项目表中分离出来
		DiathesisProjectEx projectEx = diathesisProjectExService.findByUnitIdAndProjectId(unitId, projectId);
		//DiathesisProject project = diathesisProjectService.findOne(projectId);
		String[] role=null;
		if(StringUtils.isNotBlank(projectEx.getInputTypes())){
			role = projectEx.getInputTypes().split(",");
		}
		return role==null?new ArrayList<>():Arrays.stream(role).collect(Collectors.toList());
	}

}
