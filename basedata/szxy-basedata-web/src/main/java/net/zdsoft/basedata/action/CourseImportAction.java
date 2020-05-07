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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

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
@RequestMapping("/basedata/import/course/")
public class CourseImportAction extends DataImportAction{
	@Autowired
	private UnitService unitService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CourseTypeService courseTypeService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	@RequestMapping("/index")
	public String importIndex(String type, ModelMap map){
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
	    Integer unitClass = unitService.findOne(unitId).getUnitClass();
		// 业务名称
		map.put("businessName", "课程");
		// 导入URL 
		map.put("businessUrl", "/basedata/import/course/import");
		// 导入模板
		map.put("templateDownloadUrl", "/basedata/import/course/template?type="+type);
        map.put("exportErrorExcelUrl", "/basedata/import/course/exportErrorExcel");
		// 导入对象
		map.put("objectName", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/basedata/import/course/validate?type="+type);
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("type", type);
	    obj.put("subjectType", unitClass);
	    
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
		map.put("type", type);
		map.put("subjectType", unitClass);
		return "/basedata/course/courseImportIndex.ftl";
	}
	
	/**
	 * 下载课程导入模板
	 * @param type
	 * @param response
	 */
	@RequestMapping("/template")
	@Override
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String type = request.getParameter("type");
		
		List<String> titleList;;//表头
		if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
			titleList = getRowTitleList();
		}else{
			titleList = getRowTitleList2();
		}
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet();
	    int size = titleList.size();
	    //标题行固定
        sheet.createFreezePane(0, 2);
	    //sheet 合并单元格
	    CellRangeAddress car = new CellRangeAddress(0,0,0,size-1);
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
        HSSFRow titleRow = sheet.createRow(0);
        //高度：8倍默认高度
        //注意事项
        String remark ="";
        if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
        	titleRow.setHeightInPoints(8*sheet.getDefaultRowHeightInPoints());
        	remark=getRemark();
        }else {
        	titleRow.setHeightInPoints(10*sheet.getDefaultRowHeightInPoints());
        	remark=getRemark2();
        }
  
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(headStyle);
        
        titleCell.setCellValue(new HSSFRichTextString(remark));
        titleCell.setCellStyle(headStyle);
	    
        HSSFRow rowTitle = sheet.createRow(1);
        for(int j=0;j<size;j++){
        	sheet.setColumnWidth(j, 20 * 256);//列宽
        	HSSFCell cell = rowTitle.createCell(j);
            cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
        }
		
		String fileName = "";
		if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
			fileName = "必修课程导入";
		}else{
			fileName = "选修课程导入";
		}
		
		ExportUtils.outputData(workbook, fileName, response);
		
	}

	@RequestMapping("/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    /**
	 * 模板校验
	 * @return
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(HttpServletRequest request,String filePath, String validRowStartNo){
		validRowStartNo = "1";
		if (StringUtils.isBlank(validRowStartNo)) {
		}
		String type = request.getParameter("type");
		
		List<String> titleList;;//表头
		if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
			titleList = getRowTitleList();
		}else{
			titleList = getRowTitleList2();
		}
		int size = titleList.size();
		try{
			List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath,size);
			return templateValidate(datas, titleList);
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	
	/**
	 * 导入数据
	 */
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		
		//学校段：学校+教育局：验证科目编码唯一
		//教育局段：验证科目编码唯一 （没有控制学校是否将该科目编号提前使用）
		
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String unitId = (String) performance.get("unitId");
		Integer unitClass = unitService.findOne(unitId).getUnitClass();
		String type = (String) performance.get("type");
		Integer subjectType = (Integer) performance.get("subjectType");
		
		List<String> rowTitleList;
		if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
			rowTitleList = getRowTitleList();
		}else{
			rowTitleList = getRowTitleList2();
		}
		
		//获取上传数据 第一行行标是0
		List<String[]> courseDatas = ExcelUtils.readExcelIgnoreDesc(filePath,rowTitleList.size());
		courseDatas.remove(0);

		//错误数据序列号
        int sequence = 0;
        int totalSize =courseDatas.size();
        
        //先拿到该单位所有科目
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
        
        //先获取科目编码-科目类型Map
        //目前就是456获取1课程模块
        List<CourseType> courseTypeList = new ArrayList<CourseType>();
        if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
        	courseTypeList = courseTypeService.findByType(type);
        }else{
        	courseTypeList = courseTypeService.findByTypes(new String[]{BaseConstants.SUBJECT_TYPE_BX,BaseConstants.SUBJECT_TYPE_XX});
        }
    	Map<String, CourseType> codeMap = EntityUtils.getMap(courseTypeList, CourseType::getCode);
    	//必修跟选修也不能重复
    	List<Course> list = courseService.getListByConditionWithMaster(unitId,null , null, null, null, null, null);

    	//key:subjectCode+"_"+subjectName
    	Map<String,List<Course>> courseMap=new HashMap<>();
    	//subjectCode+"_"+subjectName 全局唯一才能修改 为了之后垃圾数据清除
    	Set<String> sameSubjectCode=new HashSet<>();
    	//当section不为空 代表所有年级都可以
    	Map<String,Set<String>> sectionSubjectNameMap=new HashMap<>();
    	//section为空
    	Set<String> sectionNullSubName=new HashSet<>();
    	int orderId=0;
    	//同一个学段内科目名称不能重复
    	//全局科目编号可以重复
    	//也就是subjectCode+"_"+subjectName唯一条件
    	if(CollectionUtils.isNotEmpty(list)) {
    		for(Course c:list) {
    			if(c.getOrderId()!=null && c.getOrderId()>orderId) {
    				orderId=c.getOrderId();
    			}
    			List<Course> list2 = courseMap.get(c.getSubjectCode()+"_"+c.getSubjectName());
    			if(list2==null) {
    				list2=new ArrayList<>();
    				courseMap.put(c.getSubjectCode()+"_"+c.getSubjectName(), list2);
    			}
    			list2.add(c);
    			sameSubjectCode.add(c.getSubjectCode());
    			String s=c.getSection();
    			if(StringUtils.isNotBlank(s)) {
    				String[] arr=s.split(",");
    				for(String ss:arr) {
    					if(!sectionSubjectNameMap.containsKey(ss)) {
    						sectionSubjectNameMap.put(ss, new HashSet<>());
    					}
    					sectionSubjectNameMap.get(ss).add(c.getSubjectName());
    				}
    			}else {
    				sectionNullSubName.add(c.getSubjectName());
    			}
    		}
    	}
    			
    	//获取此单位的所有学段
    	Set<String> sectionSet = getSectionByUnit(unitId);
    	//根据课程码获取所有课程
		List<String> codeList = new ArrayList<String>();
		for (String[] array : courseDatas) {
			if(StringUtils.isNotBlank(array[0])){
				codeList.add(array[0]);
			}		
		}

		if(codeList.size()<1){
			String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="";
            errorData[2]="";
            errorData[3]="所有课程码必填";
            errorDataList.add(errorData);
            return result(courseDatas.size(),0,courseDatas.size(),errorDataList,"");
		}		

		
		//记录将要导入的课程码和课程名称 K:课程码或课程名称   V:所在行数
		Map<String,Integer> codeRowsMap = new HashMap<>();
		Map<String,Integer> nameRowsMap = new HashMap<>();
		
    	int successCount=0;
		List<Course> courseList = new ArrayList<>();
		int i=0;
        for (String[] array : courseDatas) {
        	i++;
        	Course makeCourse=new Course();
        	String[] errorData=checkRight(array,type,i,sectionSet,unitClass,makeCourse);
        	if(errorData!=null) {
        		sequence++;
        		errorDataList.add(errorData);
         		continue;
        	}
        	//验证行重复
        	//校验本次导入的 多条数据中有没有 课程编码、名称 重复的
        	if(codeRowsMap.get(makeCourse.getSubjectCode())!=null){
        		errorData=new String[4];
        		sequence++;
                errorData[0]=i+"";
        		errorData[1]= "第"+i+"行";
        		errorData[2]="课程码："+makeCourse.getSubjectCode();
        		errorData[3]= "第"+i+"行与第"+codeRowsMap.get(makeCourse.getSubjectCode())+"行的课程码重复";
        		errorDataList.add(errorData);
        		continue;
        	}
        	if(nameRowsMap.get(makeCourse.getSubjectName())!=null){
        		errorData=new String[4];
        		sequence++;
                errorData[0]=i+"";
        		errorData[1]= "第"+i+"行";
        		errorData[2]="课程名称："+makeCourse.getSubjectName();
        		errorData[3]= "第"+i+"行与第"+nameRowsMap.get(makeCourse.getSubjectName())+"行的课程名称重复";
        		errorDataList.add(errorData);
        		continue;
        	}
        	
        	//最终
        	Course returnCourse = new Course();
        	
        	//验证唯一性
        	//验证本单位
        	//科目名称 科目编码一致 才能算修改
        	String key1=makeCourse.getSubjectCode()+"_"+makeCourse.getSubjectName();
        	if(courseMap.containsKey(key1)) {
        		List<Course> oldCourseList = courseMap.get(key1);
        		if(oldCourseList.size()==1) {
        			Course oldCourse = oldCourseList.get(0);
        			if(oldCourse.getUnitId().equals(unitId)) {
        				//修改
        				if(!oldCourse.getType().equals(makeCourse.getType())){
        	        		//选修 必修 类型不一致错误
        	        		errorData=new String[4];
        	        		sequence++;
        	                errorData[0]=i+"";
        	        		errorData[1]= "第"+i+"行";
        	        		errorData[2]="课程码："+oldCourse.getSubjectCode();
        	        		errorData[3]= "此课程码已经被其他类型课程使用";
        	        		errorDataList.add(errorData);
        	        		continue;
        				}
        				returnCourse=oldCourse;
        				returnCourse.setModifyTime(new Date());
        			}else {
        				 errorData=new String[4];
        				 sequence++;
        				 errorData[0]=i+"";
                         errorData[1]="第"+i+"行";
                         errorData[2]= "*学科编号";
                         errorData[3]= "根据科目编号重复";
                         errorDataList.add(errorData);
                 		continue;
        			}
        		}else {
        			 errorData=new String[4];
            		 sequence++;
        			 errorData[0]=i+"";
                     errorData[1]="第"+i+"行";
                     errorData[2]= "*学科编号";
                     errorData[3]= "根据科目编号和名称未找到唯一对应科目";
                     errorDataList.add(errorData);
             		continue;
        		}
        	}else {
        		//新增
        		if(sameSubjectCode.contains(makeCourse.getSubjectCode())) {
        			errorData=new String[4];
           		    sequence++;
        			errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]= "*学科编号";
                    errorData[3]= "根据科目编号重复："+makeCourse.getSubjectCode();
                    errorDataList.add(errorData);
            		continue;
        		}
        		//名称学段内不重复
        		if(sectionNullSubName.contains(makeCourse.getSubjectName())) {
        			errorData=new String[4];
           		    sequence++;
        			errorData[0]=i+"";
                    errorData[1]="第"+i+"行";
                    errorData[2]= "*学科名称";
                    errorData[3]= "根据科目名称重复："+makeCourse.getSubjectName();
                    errorDataList.add(errorData);
            		continue;
        		}
        		if(isHw()){
        			//makeCourse.getSection()多选
        			String[] sections = makeCourse.getSection().split(",");
        			boolean flag = false;
        			for (String s : sections) {
        				Set<String> samename = sectionSubjectNameMap.get(s);
        				if(CollectionUtils.isNotEmpty(samename) && samename.contains(makeCourse.getSubjectName())) {
        					errorData=new String[4];
        					sequence++;
        					errorData[0]=i+"";
        					errorData[1]="第"+i+"行";
        					errorData[2]= "*学科名称";
        					errorData[3]= "根据科目名称重复："+makeCourse.getSubjectName();
        					errorDataList.add(errorData);
        					flag=true;
        					break;
        				}
					}
        			if(flag){
        				continue;
        			}
        		}else{
        			//makeCourse.getSection()单选
        			Set<String> samename = sectionSubjectNameMap.get(makeCourse.getSection());
        			if(CollectionUtils.isNotEmpty(samename) && samename.contains(makeCourse.getSubjectName())) {
        				errorData=new String[4];
        				sequence++;
        				errorData[0]=i+"";
        				errorData[1]="第"+i+"行";
        				errorData[2]= "*学科名称";
        				errorData[3]= "根据科目名称重复："+makeCourse.getSubjectName();
        				errorDataList.add(errorData);
        				continue;
        			}
        		}
        		//新增
            	returnCourse.setSubjectCode(makeCourse.getSubjectCode());
            	returnCourse.setSubjectType(subjectType+"");
            	returnCourse.setUnitId(unitId);
            	returnCourse.setType(makeCourse.getType());
            	orderId=orderId+1;
            	if(orderId>=999999999) {
            		//跟页面最大值保持一致
            		orderId=999999999;
            	}
            	returnCourse.setOrderId(orderId);
            	returnCourse.setIsDeleted(Constant.IS_DELETED_FALSE);
            	returnCourse.setId(UuidUtils.generateUuid());
            	returnCourse.setCreationTime(new Date());
            	returnCourse.setModifyTime(new Date());
        	}
        	
        
        	String courseTypeCode=makeCourse.getCourseTypeName();
        	//科目编码  验证
        	if(codeMap.get(courseTypeCode) == null){
        		errorData=new String[4];
                sequence++;
                errorData[0]=i+"";
                errorData[1]="第"+i+"行";
                errorData[2]= "*学科编号";
                errorData[3]= "此学科编号可能不存在";
                errorDataList.add(errorData);
        		continue;
        	}else{
        		CourseType courseType = codeMap.get(courseTypeCode);
        		if((BaseConstants.SUBJECT_TYPE_XX.equals(makeCourse.getType()) && !BaseConstants.SUBJECT_TYPE_XX.equals(courseType.getType())) ||
    				(!BaseConstants.SUBJECT_TYPE_XX.equals(makeCourse.getType())&&!BaseConstants.SUBJECT_TYPE_BX.equals(courseType.getType()))){
    				errorData=new String[4];
    				sequence++;
    				errorData[0]=i+"";
    				errorData[1]="第"+i+"行";
    				errorData[2]= "*学科编号";
    				errorData[3]= "请填写与选修类型匹配的学科编号";
    				errorDataList.add(errorData);
    				continue;
        		}
        	}
        	String courseTypeId = codeMap.get(courseTypeCode.trim()).getId();
        	
			returnCourse.setSubjectName(makeCourse.getSubjectName());
			returnCourse.setShortName(makeCourse.getShortName());
			returnCourse.setCourseTypeId(courseTypeId);
			returnCourse.setInitCredit(makeCourse.getInitCredit());
			returnCourse.setFullMark(makeCourse.getFullMark());
			returnCourse.setInitPassMark(makeCourse.getInitPassMark());
			returnCourse.setIsUsing(makeCourse.getIsUsing());
			returnCourse.setSection(makeCourse.getSection());
			returnCourse.setTotalHours(makeCourse.getTotalHours());
			courseList.add(returnCourse);
			successCount++;
			//记录课程码和课程名称
			codeRowsMap.put(returnCourse.getSubjectCode(), i);
			nameRowsMap.put(returnCourse.getSubjectName(), i);
		}

        // 错误数据Excel导出
        String errorExcelPath = "";
        if(CollectionUtils.isNotEmpty(errorDataList)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            //标题行固定
            sheet.createFreezePane(0, 1);
            List<String> titleList;;//表头
			if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
				titleList = getRowTitleList();
			}else{
				titleList = getRowTitleList2();
			}
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

        if (CollectionUtils.isNotEmpty(courseList)) {
        	try{
        		courseService.saveAllEntitys(courseList.toArray(new Course[0]));
        	}catch(Exception e){
        		e.printStackTrace();
        		return result(totalSize,0,totalSize,errorDataList,errorExcelPath);
        	}
		}
		
		int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList,errorExcelPath);
		return result;
	}
	/**
	 * 验证报空等数据
	 * @param array
	 * @param type
	 * @param index
	 * @param sectionSet
	 * @param unitClass
	 * @param course 存放字段数据
	 * @return
	 */
	private String[] checkRight(String[] array,String type,int index,Set<String> sectionSet,int unitClass,Course course) {
		String section = StringUtils.trim(array[0]);
    	String subjectCode = StringUtils.trim(array[1]);
    	String subjectName = StringUtils.trim(array[2]);
    	String shortName = StringUtils.trim(array[3]);
    	int i=4;
    	String realType=null;
    	if(!BaseConstants.SUBJECT_TYPE_BX.equals(type)){
    		realType = StringUtils.trim(array[i++]);
    	}
    	String courseTypeCode = StringUtils.trim(array[i++]);
    	String initCredit = StringUtils.trim(array[i++]);
    	String fullMark = StringUtils.trim(array[i++]);
    	String initPassMark = StringUtils.trim(array[i++]);
    	String isUsing = StringUtils.trim(array[i++]);
    	String totalHours=null;
    	if(!BaseConstants.SUBJECT_TYPE_BX.equals(type)){
    		totalHours=StringUtils.trim(array[i++]);
		}
    	String errorField=null;
    	//必填验证
    	if(StringUtils.isBlank(section)){
    		errorField = "学段";
    	}else if(StringUtils.isBlank(subjectCode)){
    		errorField = "课程码";
    	}else if(StringUtils.isBlank(subjectName)){
    		errorField = "课程名称";
    	}else if(StringUtils.isBlank(courseTypeCode)){
    		errorField = "学科编号";
    	}else if(StringUtils.isBlank(isUsing)){
    		errorField = "是否启用";
    	}
    	//选修时学分必填
    	if(!BaseConstants.SUBJECT_TYPE_BX.equals(type)){
    		if(StringUtils.isBlank(initCredit)){
    			errorField="学分";
    		}else if(StringUtils.isBlank(realType)){
    			errorField="选修类型";
    		}
    	}
    	if(StringUtils.isNotBlank(errorField)){
    		 String[] errorData=new String[4];
    		 errorData[0]=index+"";
             errorData[1]="第"+index+"行";
             errorData[2]=errorField;
             errorData[3]="加*为必填数据";
    		 return errorData;
    	}
    	
    	//先验证字段长度
    	if(subjectCode.getBytes().length > 100){
    		errorField = "课程码";
    	}else if(subjectName.getBytes().length > 100){
    		errorField = "课程名称";
    	}else if(StringUtils.isNotBlank(shortName)&&shortName.getBytes().length > 100){
    		errorField = "简称";
    	}
    	if(StringUtils.isNotBlank(errorField)){
    		 String[] errorData=new String[4];
             errorData[0]=index+"";
             errorData[1]="第"+index+"行";
             errorData[2]=errorField;
             errorData[3]="名称、编码等不得超过50个字";
             return errorData;
    	}
    	course.setShortName(shortName);
    	course.setSubjectCode(subjectCode);
    	course.setSubjectName(subjectName);
		course.setCourseTypeName(courseTypeCode);

		if(StringUtils.isNotBlank(realType)){
			if(!getRealTypeMap().containsKey(realType)){
				String[] errorData=new String[4];
	            errorData[0]=index+"";
				errorData[1]="第"+index+"行";
				errorData[2]=realType;
				errorData[3]="请填写正确的选修类型";
				return errorData;
			}
			course.setType(getRealTypeMap().get(realType));
		}else{
			course.setType(type);
		}    	
		//学段验证
		if(isHw()){
			String[] sections = section.replaceAll("，", ",").split(",");
			for (String s : sections) {
				if(!s.matches("^[0-9]*$")){
					String[] errorData=new String[4];
					errorData[0]=index+"";
					errorData[1]="第"+index+"行";
					errorData[2]=section;
					errorData[3]="学段需为数字";
					return errorData;
				}
				if(!sectionSet.contains(s)){
					String[] errorData=new String[4];
					errorData[0]=index+"";
					errorData[1]= "第"+index+"行";
					errorData[2]=section;
					errorData[3]="您的单位不支持您输入的学段";
					return errorData;
				}
			}
			course.setSection(StringUtils.join(sections, ","));
		}else{
			if(!section.matches("^[0-9]*$")){
				String[] errorData=new String[4];
				errorData[0]=index+"";
				errorData[1]="第"+index+"行";
				errorData[2]=section;
				errorData[3]="学段需为数字";
				return errorData;
			}
			if(!sectionSet.contains(section)){
				String[] errorData=new String[4];
				errorData[0]=index+"";
				errorData[1]= "第"+index+"行";
				errorData[2]=section;
				errorData[3]="您的单位不支持您输入的学段";
				return errorData;
			}
			course.setSection(section);
		}
    	//验证课程码的 格式
    	if(!subjectCode.matches("^[0-9a-zA-Z]+$")){
    		String[] errorData=new String[4];
            errorData[0]=index+"";
    		errorData[1]="第"+index+"行";
    		errorData[2]=subjectCode;
    		errorData[3]="课程码只支持数字、字母";
    		return errorData;
    	}
    	if(unitClass==1){
        	if(!subjectCode.startsWith(section)){
        		String[] errorData=new String[4];
                errorData[0]=index+"";
        		errorData[1]="第"+index+"行";
        		errorData[2]=subjectCode;
        		errorData[3]="课程码必须以学段数字开头";
        		return errorData;
        	}
    	}
    	
    	//验证数字相关 变量
    	String regex = "^[0]|([1-9][0-9]{0,2})$";
    	if(StringUtils.isNotBlank(initCredit)&&!initCredit.matches(regex)){
    		errorField = "学分";
    	}else if(StringUtils.isNotBlank(fullMark)&&!fullMark.matches(regex)){
    		errorField = "满分";
		}else if(StringUtils.isNotBlank(initPassMark)&&!initPassMark.matches(regex)){
			errorField = "及格分";
		}
    	if(StringUtils.isNotBlank(errorField)){
    		 String[] errorData=new String[4];
             errorData[0]=index+"";
             errorData[1]="第"+index+"行";
             errorData[2]=errorField;
             errorData[3]="分数范围是0 ~ 999的整数";
             return errorData;
    	}
    	
    	//及格分不得大于满分
    	if(StringUtils.isNotBlank(fullMark)&&StringUtils.isNotBlank(initPassMark)&&
    			Integer.parseInt(fullMark)<Integer.parseInt(initPassMark)){
    		String[] errorData=new String[4];
            errorData[0]=index+"";
    		errorData[1]="第"+index+"行";
    		errorData[2]="及格分";
    		errorData[3]="及格分不得大于满分";
    		return errorData;
		}
    	course.setInitCredit(toInteger(initCredit));
    	course.setFullMark(toInteger(fullMark));
    	course.setInitPassMark(toInteger(initPassMark));
    	
    	//是否启用 格式 验证
    	if(!isUsing.matches("^[0,1]$")){
    		String[] errorData=new String[4];
            errorData[0]=index+"";
            errorData[1]="第"+index+"行";
            errorData[2]= "是否启用";
            errorData[3]="是否启用只能填写0 或 1";
            return errorData;
    	}
    	if(StringUtils.isBlank(isUsing)) {
    		isUsing = "0";
    	}
    	course.setIsUsing(Integer.parseInt(isUsing));
    	
    	if(StringUtils.isNotBlank(totalHours)) {
    		regex="^[0]|([1-9][0-9]{0,3})$";
    		if(!totalHours.matches(regex)) {
    			String[] errorData=new String[4];
                errorData[0]=index+"";
                errorData[1]="第"+index+"行";
                errorData[2]= "总课时";
                errorData[3]="分数范围是0 ~ 9999的整数";
                return errorData;
    		}
    	}
    	return null;
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
	
	private Integer toInteger(String s){
		if(StringUtils.isBlank(s)){
			return null;
		}
		return Integer.parseInt(s);
	}
	@Override
	public String getObjectName() {
		
		return "courseImport";
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
		List<String> titleList = new ArrayList<>();
		titleList.add("*学段");
		titleList.add("*课程码");
		titleList.add("*课程名称");
		titleList.add("简称");
		titleList.add("*学科编号");
		titleList.add("学分");
		titleList.add("满分");
		titleList.add("及格分");
		titleList.add("*是否启用");
		return titleList;
	}
	
	public List<String> getRowTitleList2() {
		List<String> titleList = new ArrayList<>();
		titleList.add("*学段");
		titleList.add("*课程码");
		titleList.add("*课程名称");
		titleList.add("简称");
		titleList.add("*选修类型");
		titleList.add("*学科编号");
		titleList.add("*学分");
		titleList.add("满分");
		titleList.add("及格分");
		titleList.add("*是否启用");
		titleList.add("总课时");
		return titleList;
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
	/**
	 * 获取excel文件的备注
	 * @return
	 */
	private String getRemark(){
		String sectionStrs = getRemarkSectionDisplay();
		Integer unitClass = unitService.findOne(getLoginInfo().getUnitId()).getUnitClass();
		String remark = "填写注意：\n"
				+ "1.带 * 为必填\n"
				+ "2.课程码只支持数字、字母"+(unitClass==1?"，且必须以学段数字开头\n":"\n")
				+ "3.是否启用：0不启用  1启用\n"
				+ "4.学段请填写数字,"+(isHw()?"多个学段之间以逗号相隔,":"") +"学段：" + sectionStrs + "\n"
				+ "5.分数只能填写整数";
		return remark;
	}

	/**
	 * 选修课备注
	 * @return
	 */
	private String getRemark2(){
		String sectionStrs = getRemarkSectionDisplay();
		Integer unitClass = unitService.findOne(getLoginInfo().getUnitId()).getUnitClass();
		List<CourseType> courseTypeList = courseTypeService.findByType(BaseConstants.SUBJECT_TYPE_XX);
		StringBuffer courseTypeStr = new StringBuffer();
		for (CourseType courseType : courseTypeList) {
			courseTypeStr.append(courseType.getCode()).append(courseType.getName()).append("  ");
		}
		String remark = "填写注意：\n"
				+ "1.带 * 为必填\n"
				+ "2.课程码只支持数字、字母"+(unitClass==1?"，且必须以学段数字开头\n":"\n")
				+ "3.是否启用：0不启用  1启用\n"
				+ "4.学段请填写数字,"+(isHw()?"多个学段之间以逗号相隔,":"") +"学段：" + sectionStrs + "\n"
				+ "5.分数只能填写整数\n"
				+ "6.选修类型：1选修  2选修Ⅰ-A  3选修Ⅰ-B  4选修Ⅱ\n"
				+ "7.选修类型为1时，学科编号请从以下编号选择："+courseTypeStr.toString()+"\n"
				+ "8.选修类型为2,3,4时，请根据课程模块填写相应的学科编号";
		return remark;
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
	
	private Map<String, String> getRealTypeMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "2");
		map.put("2", "4");
		map.put("3", "5");
		map.put("4", "6");
		return map;
	}
	
	private boolean isHw(){
		return BaseConstants.DEPLOY_HANGWAI.equals(systemIniRemoteService.findValue("SYSTEM.DEPLOY.REGION"));
	}
}
