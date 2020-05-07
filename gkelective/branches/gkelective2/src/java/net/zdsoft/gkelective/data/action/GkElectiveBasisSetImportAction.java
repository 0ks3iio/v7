package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkStuScoreDto;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/gkelective/scoreImport")
public class GkElectiveBasisSetImportAction extends DataImportAction{

	private Logger logger = Logger.getLogger(GkElectiveBasisSetImportAction.class);

	@Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkStuRemarkService gkStuRemarkService;
	@Autowired
	private GkRelationshipService gkRelationshipService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	private String arrId;
	
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String importMain(String arrangeId,ModelMap map) {
	    // 业务名称
	    map.put("businessName", "参考成绩导入");
	 	// 导入URL 
	 	map.put("businessUrl", "/gkelective/scoreImport/import");
	 	// 导入模板
	 	map.put("templateDownloadUrl", "/gkelective/scoreImport/template");
	 	// 导入对象
	 	map.put("objectName", "");
	 	// 导入说明
	 	StringBuffer description=new StringBuffer();
	 	description.append(getDescription());
	 	 		
	 	// 如果导入文件中前面有说明性文字的  这里需要传一个有效数据开始行（列名那一行）
	 	//如果列名在第一行的就不需要传
	 	map.put("validRowStartNo",0);
	 	//模板校验
	 	map.put("validateUrl", "/gkelective/scoreImport/validate");
	 	 		
	 	map.put("description", description);
	 	map.put("businessKey", UuidUtils.generateUuid());
	 	 		
	 	JSONObject obj=new JSONObject();
	 	obj.put("arrangeId", arrangeId);
	 	
	 	arrId = arrangeId;
	 	
	 	map.put("monthPermance",JSON.toJSONString(obj));
	 	map.put("arrangeId", arrangeId);
		return "/gkelective2/basisSet/basisSetScoreImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "basisSetScoreFilter";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、请正确填写学号、姓名、班级和相应的学科成绩</p>"
				+ "<p>2、班级请填写正确格式（年级+班级名称）</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
		//下载文件表头
		List<String> tis = new ArrayList<String>();
		tis.add("学号");
		tis.add("姓名");
		tis.add("行政班");
		for (Course course : coursesList) {
			tis.add(course.getSubjectName());
		}
		tis.add("语数英");
		return tis;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		//每一行数据   表头列名：0
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,1,getRowTitleList().size());
		 //错误数据序列号
        int sequence =1;
        int totalSize =datas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(datas)){
        	String[] errorData=new String[4];
        	sequence++;
        	errorData[0]=String.valueOf(sequence);
        	errorData[1]="学号";
        	errorData[2]="";
        	errorData[3]="没有导入数据";
        	errorDataList.add(errorData);
        	return result(datas.size()-1,0,0,errorDataList);
        }
        
        JSONObject performance = JSON.parseObject(params,JSONObject.class);
        final String arrangeId = (String) performance.get("arrangeId");
        
        GkSubjectArrange gkArrange =gkSubjectArrangeService.findArrangeById(arrangeId);
        if(gkArrange==null){
        	String[] errorData=new String[4];
        	sequence++;
        	errorData[0]=String.valueOf(sequence);
        	errorData[1]="学号";
        	errorData[2]="";
        	errorData[3]="该选课课程不存在";
        	errorDataList.add(errorData);
        	return result(datas.size()-1,0,0,errorDataList);
        }
        
        ChosenSubjectSearchDto searchDto = new ChosenSubjectSearchDto();
        searchDto.setSearchSelectType("2");
        List<GkStuScoreDto> gkStuScoreDtoList = gkStuRemarkService.findStuScoreDtoList(arrangeId,searchDto,null);
        
        Map<String,String> coursesMap = new LinkedHashMap<String,String>();
        List<Course> coursesList = new ArrayList<Course>();
        coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
        for (Course course : coursesList) {
        	coursesMap.put(course.getSubjectName(), course.getId());
        }
        Map<String,Student> studentsMap = new LinkedHashMap<String,Student>();
        for (GkStuScoreDto dto : gkStuScoreDtoList) {
        	studentsMap.put(dto.getStudent().getStudentCode(), dto.getStudent());
        }
        
        Pattern pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$"); 
        
        List<GkStuRemark> stuRemarkList  = new ArrayList<GkStuRemark>();
        Map<String,String> subScoreMap = null;
        List<String> studentIdsList=new ArrayList<String>();
//        String[] studentIds = new String[datas.size()];
        int index = 0;
        int successCount=0;
        for (String[] arr:datas) {
        	String[] errorData=new String[4];
            sequence++;
            
            if(StringUtils.isBlank(arr[0])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[1])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="姓名";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            if(StringUtils.isBlank(arr[2])){
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="行政班";
                errorData[2]="";
                errorData[3]="不能为空";
                errorDataList.add(errorData);
                continue;
            }
            
            Student student = studentsMap.get(arr[0]);
            if (student == null) {
            	errorData[0]=String.valueOf(sequence);
                errorData[1]="学号";
                errorData[2]="";
                errorData[3]="学号不存在";
                errorDataList.add(errorData);
                continue;
            } else {
            	if (!arr[1].equals(student.getStudentName())) {
            		errorData[0]=String.valueOf(sequence);
                    errorData[1]="姓名";
                    errorData[2]="";
                    errorData[3]="学号所对应的学生姓名错误";
                    errorDataList.add(errorData);
                    continue;
            	}
//            	if (!arr[2].equals(student.getClassName())) {
//            		errorData[0]=String.valueOf(sequence);
//                    errorData[1]="行政班";
//                    errorData[2]="";
//                    errorData[3]="不是该行政班的学生";
//                    errorDataList.add(errorData);
//                    continue;
//            	}
            }
            
            subScoreMap = new LinkedHashMap<String,String>();
            int indexSize = 3;
            for (Course course : coursesList) {
            	if (StringUtils.isNotBlank(arr[indexSize])) {
            		if (!pattern.matcher(arr[indexSize]).matches()) {
            			errorData[0]=String.valueOf(sequence);
                        errorData[1]=course.getSubjectName();
                        errorData[2]="";
                        errorData[3]="格式不正确(最多3位整数，2位小数)!";
                        errorDataList.add(errorData);
                        continue;
            		}
            		subScoreMap.put(course.getSubjectName(), arr[indexSize]);
            	}
            	indexSize++;
            }
            
            if (StringUtils.isNotBlank(arr[indexSize])) {
            	if (!pattern.matcher(arr[indexSize]).matches()) {
            		errorData[0]=String.valueOf(sequence);
                    errorData[1]="语数英";
                    errorData[2]="";
                    errorData[3]="格式不正确(最多3位整数，2位小数)!";
                    errorDataList.add(errorData);
                    continue;
            	}
            	subScoreMap.put("语数英", arr[indexSize]);
            }
            
            successCount++;
            studentIdsList.add(student.getId());
//            studentIds[index] = student.getId();
            index++;
            GkStuRemark gkStuRemark = null;
            for (Map.Entry<String, String> entry : subScoreMap.entrySet()) {
            	gkStuRemark = new GkStuRemark();
            	gkStuRemark.setStudentId(student.getId());
            	gkStuRemark.setSubjectArrangeId(arrangeId);
            	gkStuRemark.setScore(Double.parseDouble(entry.getValue()));
            	if ("语数英".equals(entry.getKey())) {
            		gkStuRemark.setSubjectId(GkStuRemark.YSY_SUBID);
            		gkStuRemark.setType(GkStuRemark.TYPE_SCORE_YSY);
            	}else {
            		gkStuRemark.setSubjectId(coursesMap.get(entry.getKey()));
            		gkStuRemark.setType(GkStuRemark.TYPE_SCORE);
            	}
            	stuRemarkList.add(gkStuRemark);
            }
            
        }
        
        try{
			if (CollectionUtils.isNotEmpty(stuRemarkList)) {
				gkStuRemarkService.deleteByStudentIdsAndArrangeId(arrangeId, GkStuRemark.TYPE_SCORE,  studentIdsList.toArray(new String[]{}));
				gkStuRemarkService.deleteByStudentIdsAndArrangeId(arrangeId, GkStuRemark.TYPE_SCORE_YSY, studentIdsList.toArray(new String[]{}));
				gkStuRemarkService.saveAll(stuRemarkList);
			}
        }catch (Exception e){
            e.printStackTrace();
        }
        
        int errorCount = totalSize - successCount;
        String result = result(totalSize,successCount,errorCount,errorDataList);
		return result;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> titleList = getRowTitleList();//表头
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),new ArrayList<Map<String, String>>());
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile("参考成绩导入", titleMap, sheetName2RecordListMap, response);
	}
	
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList().size());
			return templateValidate(datas, getRowTitleList());
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}

	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	 }
}
