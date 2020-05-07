package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.service.NewGkChoCategoryService;
import net.zdsoft.newgkelective.data.service.NewGkChoRelationService;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/newgkelective")
public class NewGkChoResultImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(NewGkChoResultImportAction.class);
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private NewGkChoRelationService newGkChoRelationService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
    private NewGkChoCategoryService newGkChoCategoryService;

	@RequestMapping("/newGkChoResult/import/main")
	public String execute(String chioceId, String chosenType, ModelMap map) {
		// 业务名称
		map.put("businessName", "选课结果");
		// 导入URL 
		map.put("businessUrl", "/newgkelective/newGkChoResult/import");
		
		map.put("templateDownloadUrl", "/newgkelective/newGkChoResult/template?chioceId="+chioceId);
        map.put("exportErrorExcelUrl", "/newgkelective/newGkChoResult/exportErrorExcel");
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());			
		map.put("businessKey", UuidUtils.generateUuid());
		NewGkChoice choice = newGkChoiceService.findOne(chioceId);
		if(choice==null){
			return errorFtl(map, "对应选课数据不存在");
		}
		String chioseName = choice.getChoiceName();
		map.put("chioceName", chioseName);
		map.put("chioceId", chioceId);
		map.put("chosenType", chosenType);
		//模板校验
		map.put("validateUrl", "/newgkelective/newGkChoResult/validate?choiceId="+chioceId+"&unitId="+choice.getUnitId());
		return "/newgkelective/chosen/newGkChoResultImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "newGkChoResultImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		/*tis.add("姓名");
		tis.add("学号");
		tis.add("科目名称");
		tis.add("科目编号");*/
		return tis;
	}

	@Override
	@RequestMapping("/newGkChoResult/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		params = params.replace("&quot;", "\"");
		JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
		String choiceId = jsStr.getString("chioceId");
		NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList(choiceId, newGkChoice.getUnitId()).size());
		String[] titleArr = datas.get(0);
		datas.remove(0);
		List<String[]> errorDataList=new ArrayList<String[]>();
		Json importResultJson=new Json();
		if(CollectionUtils.isEmpty(datas)){
            String[] errorData=new String[4];
            errorData[0]=String.valueOf(1);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            importResultJson.put("totalCount", datas.size());
			importResultJson.put("successCount", 0);
			importResultJson.put("errorCount", datas.size());
			importResultJson.put("errorData", errorDataList);
            importResultJson.put("errorExcelPath", "");
			return importResultJson.toJSONString();
            
        }
		String gradeId = newGkChoice.getGradeId();
		String gradeName = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class).getGradeName();
		
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		List<NewGkChoResult> insertList=new ArrayList<NewGkChoResult>();
		for(String[] arr : datas){
			if(StringUtils.isNotBlank(arr[1])){
				stuCodeSet.add(arr[1]);
			}
        }
		//学号重复---暂时没有考虑
		List<Student> stuList = new ArrayList<Student>();
		Set<String> clsIdSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(stuCodeSet)){
			stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(newGkChoice.getUnitId(), stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
		    clsIdSet = EntityUtils.getSet(stuList, Student::getClassId);
		}
		List<Clazz> clsList = new ArrayList<Clazz>();
		Map<String, String> clsMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(clsIdSet)){
			clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>() {});
			clsMap = EntityUtils.getMap(clsList, Clazz::getId, Clazz::getGradeId);
		}
		Map<String, String> stuClsMap = new HashMap<String, String>();
		for(Student stu : stuList){
			stuClsMap.put(stu.getId(), clsMap.get(stu.getClassId()));
	    }
        Map<String, String> stuCodeNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeIdMap = new HashMap<String, String>();
		for(Student stu : stuList){
			stuCodeNameMap.put(stu.getStudentCode(), stu.getStudentName());
			stuCodeIdMap.put(stu.getStudentCode(), stu.getId());
        }
		
		List<String> courseIdList = newGkChoRelationService.findByChoiceIdAndObjectType(newGkChoice.getUnitId(),choiceId, NewGkElectiveConstant.CHOICE_TYPE_01);
		Map<String, String> courNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(courseIdList)){
			List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIdList.toArray(new String[0])), new TR<List<Course>>(){});
		    for(Course course : courseList){
		    	courNameMap.put(course.getSubjectName(), course.getId());
		    }
		}
		
		//禁选组合
		List<String> courseIdList2 = newGkChoRelationService.findByChoiceIdAndObjectType(newGkChoice.getUnitId(),choiceId, NewGkElectiveConstant.CHOICE_TYPE_02);
		Set<String> courseNoIdSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(courseIdList2)){
			String courseIds = courseIdList2.get(0);
			String[] courseNoIdArr = courseIds.split(",");
			List<Course> courseList2 = SUtils.dt(courseRemoteService.findListByIds(courseNoIdArr), new TR<List<Course>>(){});
		    for(Course course : courseList2){
		    	courseNoIdSet.add(course.getId());
		    }
		}
		//获取表里已有数据
		//成功数据的学生
		Set<String> chooseStudentId=new HashSet<String>();
		int t=0;
		String errorMess;
		String chooseGroup;

		// 判断各类目选课数量是否在限定范围内
		List<NewGkChoCategory> categoryList = newGkChoCategoryService.findByChoiceId(newGkChoice.getUnitId(), choiceId);
    	List<List<String>> combinationList = new ArrayList<List<String>>();
    	if(CollectionUtils.isNotEmpty(categoryList)){
    		categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_2.equals(a.getCategoryType())).collect(Collectors.toList())
    		.forEach(e->combinationList.add(e.getCourseList()));
    		categoryList= categoryList.stream().filter(a -> NewGkElectiveConstant.CATEGORY_TYPE_1.equals(a.getCategoryType())).collect(Collectors.toList());
    	}

		for(String[] arr : datas){

			t++;
			//姓名
			if(StringUtils.isBlank(arr[0])){
        		errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(t)+"行";
				errorData[2]="姓名";
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[1])){
        		errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(t)+"行";
				errorData[2]="学号";
				errorData[3]="学号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		if(StringUtils.isBlank(stuCodeNameMap.get(arr[1]))){
        			errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
    				errorData[1]="第"+String.valueOf(t)+"行";
    				errorData[2]="学号";
    				errorData[3]="不存在该学号所属的学生";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[0]) && !arr[0].equals(stuCodeNameMap.get(arr[1]))){
        				errorData = new String[4];
                        // errorData[0]=errorDataList.size()+1+"";
                        errorData[0]=t+"";
        				errorData[1]="第"+String.valueOf(t)+"行";
        				errorData[2]="姓名："+arr[0]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学号："+arr[1];
        				errorData[3]="学生姓名与该学号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        			if(!gradeId.equals(stuClsMap.get(stuCodeIdMap.get(arr[1])))){
        				errorData = new String[4];
                        // errorData[0]=errorDataList.size()+1+"";
                        errorData[0]=t+"";
        				errorData[1]="第"+String.valueOf(t)+"行";
        				errorData[2]="年级";
        				errorData[3]="该学生不属于"+gradeName+"年级";
        				errorDataList.add(errorData);
        				continue;
        			}
        		}
        	}
			String studentId=stuCodeIdMap.get(arr[1]);
			if(chooseStudentId.contains(studentId)){
				errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(t)+"行";
				errorData[2]=arr[0];
				errorData[3]="学生重复！";
				errorDataList.add(errorData);
				continue;
			}
			chooseStudentId.add(stuCodeIdMap.get(arr[1]));
			
			chooseGroup = arr[arr.length-1];
			Set<String> chooseGroupSet = new HashSet<String>();
			Set<String> chooseSubjectIds=new HashSet<String>();
			errorMess="";
			if(StringUtils.isNotBlank(chooseGroup)){
				String[] chooseGroupArr = chooseGroup.replaceAll("，", ",").split(",");
				if(chooseGroupArr.length!=newGkChoice.getChooseNum()){
					errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
    				errorData[1]="第"+String.valueOf(t)+"行";
    				errorData[2]="科目组合";
    				errorData[3]="填写格式不正确";
    				errorDataList.add(errorData);
    				continue;
				}
				chooseGroupSet.addAll(Arrays.asList(chooseGroupArr));
				if(chooseGroupSet.size()!=newGkChoice.getChooseNum()){
					errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(t)+"行";
					errorData[2]=arr[0];
					errorData[3]="科目组合中有重复课程，请核对";
					errorDataList.add(errorData);
					continue;
				}
				for (String item : chooseGroupSet) {
					if(null == courNameMap.get(item)){
						errorMess=errorMess+";"+"本次选课中不存在课程"+item;
						continue;
					}
					chooseSubjectIds.add(courNameMap.get(item));
				}
				if(StringUtils.isNotBlank(errorMess)) {
					errorMess=errorMess.substring(1);
					errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(t)+"行";
					errorData[2]="科目组合";
					errorData[3]=errorMess;
					errorDataList.add(errorData);
					continue;
				}
			}else{
				for(int i=2;i<arr.length-1;i++){
					if(StringUtils.isNotBlank(arr[i])){
						if(!arr[i].equals(titleArr[i])){
							errorMess=errorMess+";填写科目"+arr[i]+"与表头不一致";
						}else{
							chooseGroupSet.add(arr[i]);
						}
					}
				}
				if(StringUtils.isNotBlank(errorMess)) {
					errorMess=errorMess.substring(1);
					errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(t)+"行";
					errorData[2]="科目";
					errorData[3]=errorMess;
					errorDataList.add(errorData);
					continue;
				}
				if(chooseGroupSet.size()!=newGkChoice.getChooseNum()){
					errorData = new String[4];
                    // errorData[0]=errorDataList.size()+1+"";
                    errorData[0]=t+"";
					errorData[1]="第"+String.valueOf(t)+"行";
					errorData[2]="科目";
					errorData[3]="选课门数应等于"+newGkChoice.getChooseNum();
					errorDataList.add(errorData);
					continue;
				}
				for (String item : chooseGroupSet) {
					chooseSubjectIds.add(courNameMap.get(item));
				}
			}
			
			//取得学生课程 courseNameList--无需考虑科目数量 之前判断已控制
			//判断禁选组合courseNoIdSet
			if(courseNoIdSet.containsAll(chooseSubjectIds)){
				errorData = new String[4];
                // errorData[0]=errorDataList.size()+1+"";
                errorData[0]=t+"";
				errorData[1]="第"+String.valueOf(t)+"行";
				errorData[2]=arr[0];
				errorData[3]="科目选择为不推荐组合！";
				errorDataList.add(errorData);
				continue;
			}

            // 判断各类目选课数量是否在限定范围内
            boolean flag2=false;
        	for (NewGkChoCategory category : categoryList) {
        		List<String> selectList = chooseSubjectIds.stream().collect(Collectors.toList());
        		int i=0;
        		//组合
        		List<List<String>> cList = category.getCourseLists();
        		if(CollectionUtils.isNotEmpty(cList)){
        			for(List<String> cl:cList){
        				if(CollectionUtils.isNotEmpty(cl)){
                    		if(selectList.containsAll(cl)){
                    			selectList.removeAll(cl);
                    			i+=1;
                    		}
                		}
        			}
        		}else{
        			if(CollectionUtils.isNotEmpty(combinationList)){
        				for(List<String> cl:combinationList){
            				if(CollectionUtils.isNotEmpty(cl)){
                        		if(selectList.containsAll(cl)){
                        			selectList.removeAll(cl);
                        		}
                    		}
            			}
        			}
        		}
        		//单科
        		List<String> courseList = category.getCourseList();
        		if(CollectionUtils.isNotEmpty(courseList)){
    				int size = CollectionUtils.intersection(courseList, selectList).size();
        			i+=size;
        		}
        		
        		if(category.getMinNum()>i || i>category.getMaxNum()){
        			flag2=true;
        			break;
        		}
        		
        	}
        	if(flag2){
        		errorData = new String[4];
                errorData[0]=t+"";
                errorData[1]="第"+String.valueOf(t)+"行";
                errorData[2]="科目";
                errorData[3]="不符合类别门数限制";
                errorDataList.add(errorData);
        		continue;
        	}

			for(String s:chooseSubjectIds){
				NewGkChoResult choResult = new NewGkChoResult();
				choResult.setChoiceId(choiceId);
				choResult.setUnitId(newGkChoice.getUnitId());
				choResult.setStudentId(studentId);
				choResult.setCreationTime(new Date());
				choResult.setModifyTime(new Date());
				choResult.setId(UuidUtils.generateUuid());
				choResult.setSubjectId(s);
				choResult.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_1);
				choResult.setKindType(NewGkElectiveConstant.KIND_TYPE_01);
				insertList.add(choResult);
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

            List<String> titleList = getRowTitleList(choiceId,newGkChoice.getUnitId());;
            titleList.add("错误数据");
            titleList.add("错误原因");

            HSSFCellStyle errorStyle = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            errorStyle.setFont(font);

            HSSFRow rowTitle = sheet.createRow(0);
            for(int i=0;i<titleList.size();i++){
            	if(i>=titleList.size()-3){
            		sheet.setColumnWidth(i, 15 * 256);//列宽
            	}else{
            		sheet.setColumnWidth(i, 10 * 256);//列宽
            	}
                HSSFCell cell = rowTitle.createCell(i);
                cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
            }

            for(int i=0;i<errorDataList.size();i++){
                HSSFRow row = sheet.createRow(i+1);
                String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(i)[0]) - 1);
                for (int j=0;j<titleList.size();j++) {
                    HSSFCell cell = row.createCell(j);
                    if (j<titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(datasDetail[j]));
                    } else if (j==titleList.size()-2) {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[2]));
                        cell.setCellStyle(errorStyle);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[3]));
                        cell.setCellStyle(errorStyle);
                    }
                }
            }
            errorExcelPath = saveErrorExcel(filePath, workbook);
        }
        importResultJson.put("errorExcelPath", errorExcelPath);

        if(CollectionUtils.isNotEmpty(insertList)){
			try{
				newGkChoResultService.saveAllResult(newGkChoice.getUnitId(),choiceId, insertList,  chooseStudentId.toArray(new String[]{}), null,chooseStudentId.toArray(new String[]{}));
			}catch(Exception e){
				e.printStackTrace();
				importResultJson.put("totalCount", datas.size());
				importResultJson.put("successCount", 0);
				importResultJson.put("errorCount", datas.size());
				importResultJson.put("errorData", errorDataList);
				return importResultJson.toJSONString();
			}
			
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		logger.info("导入结束......");
		return importResultJson.toJSONString();
		
	}

	@Override
	@RequestMapping("/newGkChoResult/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String choiceId = request.getParameter("chioceId"); 
		NewGkChoice newGkChoice = newGkChoiceService.findOne(choiceId);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		//标题行固定
		sheet.createFreezePane(0, 2);
		List<String> titleList = getRowTitleList(choiceId,newGkChoice.getUnitId());
		
		CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
        sheet.addMergedRegion(car);
		
		// 注意事项样式
	    HSSFCellStyle headStyle = workbook.createCellStyle();
	    HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);// 字体大小
        headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
        headStyle.setFont(headfont);
        headStyle.setAlignment(HorizontalAlignment.LEFT);//水平居左
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        headStyle.setWrapText(true);//自动换行
        
        //第一行
        HSSFRow remarkRow = sheet.createRow(0);
        //高度：5倍默认高度
        remarkRow.setHeightInPoints(5*sheet.getDefaultRowHeightInPoints());
  
        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(headStyle);
        //注意事项
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(headStyle);
        
        HSSFRow rowTitle = sheet.createRow(1);
        for(int i=0;i<titleList.size();i++){
        	if(i==titleList.size()-1){
        		sheet.setColumnWidth(i, 15 * 256);//列宽
        	}else{
        		sheet.setColumnWidth(i, 10 * 256);//列宽
        	}
        	HSSFCell cell = rowTitle.createCell(i);
        	cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }
		
		String fileName = "选课结果导入";
		
		ExportUtils.outputData(workbook, fileName, response);
	}

	@RequestMapping("/newGkChoResult/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {

		String remark = "填写注意：\n" 
						+ "1.科目组合优先级大于单科填写优先级;\n"
						+ "2.科目组合填写时以逗号隔开，如“物理,化学,生物”;\n"
						+ "3.如未填写科目组合，则请在要选择的科目下分别填写科目名称.";
		return remark;
	}
	
	
	@RequestMapping("/newGkChoResult/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo,String choiceId,String unitId) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "1";
		}
		try{
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,getRowTitleList(choiceId, unitId).size());
			return templateValidate(datas, getRowTitleList(choiceId, unitId));
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	private List<String> getRowTitleList(String choiceId,String unitId){
		List<String> tis = new ArrayList<String>();
		tis.add("姓名");
		tis.add("学号");
		List<Course> courseList = newGkChoRelationService.findChooseSubject(choiceId, unitId);
		tis.addAll(EntityUtils.getList(courseList, Course::getSubjectName));
		tis.add("科目组合");
		return tis;
	}
}
