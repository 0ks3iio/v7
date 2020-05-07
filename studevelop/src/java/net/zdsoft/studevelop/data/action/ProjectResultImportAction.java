package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StuCheckAttDto;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateResultService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

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

@Controller
@RequestMapping("/studevelop/resultImport") 
public class ProjectResultImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(ProjectResultImportAction.class);
	
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudevelopTemplateService studevelopTemplateService;
	@Autowired
	private StudevelopTemplateItemService studevelopTemplateItemService;
	@Autowired
	private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
	@Autowired
	private StudevelopTemplateResultService studevelopTemplateResultService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	@Autowired
	private StuDevelopSubjectService stuDevelopSubjectService;
	@Autowired
	private StuDevelopCateGoryService stuDevelopCateGoryService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@RequestMapping("/head")
    @ControllerInfo(value = "进入导入头")
    public String showHead(ModelMap map,StuCheckAttDto dto,String isAdmin) {
	    String acadyear=dto.getAcadyear();
	    String semester=dto.getSemester();
	    String classId=dto.getClassId();
	    String code=dto.getCode();
        String gradeId = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getGradeId();
        List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);

//        if(CollectionUtils.isNotEmpty(subjectList)){
//        	map.put("subjectId", subjectList.get(0).getId());
//        }
        map.put("subjectList", subjectList);
	    map.put("acadyear", acadyear);
	    map.put("semester", semester);
	    map.put("classId", classId);
	    map.put("code", code);
	    map.put("isAdmin", isAdmin);
		return "/studevelop/record/export/proItemResultImportIndex.ftl";
	}
	@RequestMapping("/main")
    @ControllerInfo(value = "进入主导入")
    public String showIndex(ModelMap map,StuCheckAttDto dto,String isAdmin) {
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
	    String acadyear=dto.getAcadyear();
	    String semester=dto.getSemester();
	    String classId=dto.getClassId();
	    String code=dto.getCode();
	    String subjectId=dto.getSubjectId();
	    // 导入说明
 		StringBuffer description=new StringBuffer();
 		description.append(getDescription());
	    // 业务名称
	    if("1".equals(code)){
	    	description.append("<p>2、为了素质报告单更好的展示，建议平时+期末+态度习惯维护不要超过10个汉字</p>");
	    	map.put("businessName", "报告单成绩登记");
	    }else if("2".equals(code)){
	    	map.put("businessName", "身心健康");
	    }else if("3".equals(code)){
	    	map.put("businessName", "思想素质");
	    }else{
	    	map.put("businessName", "期末评价");
	    }
		// 导入URL 
		map.put("businessUrl", "/studevelop/resultImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/studevelop/resultImport/template?acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&code="+code+"&subjectId="+subjectId);
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/studevelop/resultImport/validate?acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&code="+code+"&subjectId="+subjectId);
		
		//导入参数
    	JSONObject obj=new JSONObject();
	    obj.put("unitId", unitId);
	    obj.put("acadyear", acadyear);
	    obj.put("semester", semester);
	    obj.put("classId", classId);
	    obj.put("code", code);
	    obj.put("subjectId", subjectId);
		obj.put("ownerId", loginInfo.getOwnerId());
	    map.put("monthPermance",JSON.toJSONString(obj));
	    map.put("description", description);
	    map.put("code", code);
	    if(!"true".equals(isAdmin) && !"1".equals(code)){//不是管理员的情况下
    		List<Clazz> clazzList=SUtils.dt(classRemoteService.findByTeacherId(loginInfo.getOwnerId()),new TR<List<Clazz>>(){});
    		boolean canSave=false;
    		if(CollectionUtils.isNotEmpty(clazzList)){
            	if(clazzList.stream().map(Clazz::getId).collect(Collectors.toSet()).contains(classId)){
            		canSave=true;
            	}
    		}
    		map.put("canSave", canSave);
    	}else
    		map.put("canSave", true);
		return "/studevelop/record/export/proItemResultImport.ftl";
	}
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject performance = JSON.parseObject(params,JSONObject.class);
		String acadyear = (String) performance.get("acadyear");
		String semester = (String) performance.get("semester");
		String classId = (String) performance.get("classId");
		String code = (String) performance.get("code");
		String subjectId = (String) performance.get("subjectId");
		StuCheckAttDto dto=new StuCheckAttDto();
		dto.setAcadyear(acadyear);
		dto.setSemester(semester);
		dto.setClassId(classId);
		dto.setCode(code);
		dto.setSubjectId(subjectId);
		List<StudevelopTemplateItem> templateItemList=new ArrayList<>();
		List<String> titleList = getRowTitleList(dto,templateItemList);//表头
		//每一行数据   表头列名：0
		List<String[]> datas = ExcelUtils.readExcelByRow(filePath,1,titleList.size());
		 //错误数据序列号
        int sequence =1;
        int totalSize =datas.size();
        
        List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(datas)){
            String[] errorData=new String[4];
            sequence++;
            errorData[0]=String.valueOf(sequence);
            errorData[1]="姓名";
            errorData[2]="";
            errorData[3]="没有导入数据";
            errorDataList.add(errorData);
            return result(datas.size(),0,0,errorDataList);
        }
        //班级下学生
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
		Set<String> stuIdSet = new HashSet<>();
		//获取学号对应的学生map
		Map<String,Student> stuCodeMap=new HashMap<>(); 
		if(CollectionUtils.isNotEmpty(stuList)){
			stuCodeMap=stuList.stream().collect(Collectors.toMap(Student::getStudentCode, Function.identity(),(k1,k2)->k1));
		}
		List<String> studentIdsList = new ArrayList<String>();
		int successCount=0;
		boolean isSubject="1".equals(code);
		if(isSubject || "2".equals(code) || "3".equals(code)){
			Set<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toSet());
			//通过项目id获取输入方式为单选的内容
			List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
			//获取 项目名称对应 选项名称的选项
			Map<String,Map<String,StudevelopTemplateOptions>> mapMap=new HashMap<>();
			if(CollectionUtils.isNotEmpty(templateOptionsList)){
				for(StudevelopTemplateOptions option:templateOptionsList){
					if(!mapMap.containsKey(option.getTemplateItemId())){
						mapMap.put(option.getTemplateItemId(), new HashMap<>());
					}
					mapMap.get(option.getTemplateItemId()).put(option.getOptionName(), option);
				}
			}
			//获取成绩时的 学科类别
			List<StuDevelopCateGory> cateGoryList=isSubject?stuDevelopCateGoryService.findListBySubjectId(subjectId):null;
			List<StudevelopTemplateResult> resultList = new ArrayList<>();
			StudevelopTemplateResult result=null;
			for(String[] arr:datas){
				String[] errorData=new String[4];
				sequence++;
				
				if(StringUtils.isBlank(arr[0])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]="";
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(arr[1])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="姓名";
					errorData[2]="";
					errorData[3]="姓名不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Student student=stuCodeMap.get(arr[0]);
				if (student == null) {
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]=arr[0];
					errorData[3]="该学号不存在，或该学号对应的学生不是该班级的学生";
					errorDataList.add(errorData);
					continue;
				}
				
				if (!arr[1].equals(student.getStudentName())) {
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]=arr[1];
					errorData[3]="该学号对应的学生姓名错误";
					errorDataList.add(errorData);
					continue;
				}
				if (studentIdsList.contains(student.getId())) {
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]=arr[1];
					errorData[3]="请勿重复导入成绩";
					errorDataList.add(errorData);
					continue;
				}
				//保存数据
				int i=2;
				StudevelopTemplateOptions option=null;
				boolean flag=false;
				for(StudevelopTemplateItem item:templateItemList){
					if(!isSubject || (isSubject && ("12".equals(item.getObjectType()) || CollectionUtils.isEmpty(cateGoryList)))){//成绩的情况对学科/学科没学科类别    或非成绩情况 
						result=new StudevelopTemplateResult();
						String msg=arr[i];
						if("1".equals(item.getSingleOrInput())){//输入
							if(StringUtils.isNotBlank(msg) && msg.length()>30){
								errorData[0]=String.valueOf(sequence);
								errorData[1]=item.getItemName();
								errorData[2]=msg;
								errorData[3]="不能超过30个字符!";
								errorDataList.add(errorData);
								flag=true;
								break;
							}
							result.setResult(msg);
						}else{
							if(StringUtils.isNotBlank(msg)){
								option=mapMap.get(item.getId()).get(msg);
								if(option==null){
									errorData[0]=String.valueOf(sequence);
									errorData[1]=item.getItemName();
									errorData[2]=msg;
									errorData[3]="对应的选项名称有误";
									errorDataList.add(errorData);
									flag=true;
									break;
								}
								result.setTemplateOptionId(option.getId());
							}
						}
						if(isSubject){
							result.setSubjectId(subjectId);
						}
						result.setTemplateItemId(item.getId());
						result.setTemplateId(item.getTemplateId());
						result.setId(UuidUtils.generateUuid());
						result.setStudentId(student.getId());
						result.setAcadyear(acadyear);
						result.setSemester(semester);
						result.setCreationTime(new Date());
						resultList.add(result);
						stuIdSet.add(student.getId());
						i++;
					}else{//对学科类别
						if(CollectionUtils.isNotEmpty(cateGoryList)){
							for (StuDevelopCateGory gory : cateGoryList) {
								result=new StudevelopTemplateResult();
								String msg=arr[i];
								if("1".equals(item.getSingleOrInput())){//输入
									if(StringUtils.isNotBlank(msg) && msg.length()>30){
										errorData[0]=String.valueOf(sequence);
										errorData[1]=item.getItemName()+"/"+gory.getCategoryName();
										errorData[2]=msg;
										errorData[3]="不能超过30个字符!";
										errorDataList.add(errorData);
										flag=true;
										break;
									}
									result.setResult(msg);
								}else{
									if(StringUtils.isNotBlank(msg)){
										option=mapMap.get(item.getId()).get(msg);
										if(option==null){
											errorData[0]=String.valueOf(sequence);
											errorData[1]=item.getItemName()+"/"+gory.getCategoryName();
											errorData[2]=msg;
											errorData[3]="对应的选项名称有误";
											errorDataList.add(errorData);
											flag=true;
											break;
										}
										result.setTemplateOptionId(option.getId());
									}
								}
								result.setSubjectId(subjectId);
								result.setCategoryId(gory.getId());
								result.setTemplateItemId(item.getId());
								result.setTemplateId(item.getTemplateId());
								result.setId(UuidUtils.generateUuid());
								result.setStudentId(student.getId());
								result.setAcadyear(acadyear);
								result.setSemester(semester);
								result.setCreationTime(new Date());
								resultList.add(result);
								stuIdSet.add(student.getId());
								i++;
							}
							if(flag) break;
						}
					}
				}
				if(flag){
					continue;
				}
				studentIdsList.add(student.getId());
				successCount++;
				//保存数据
			}
			if(isSubject){
				studevelopTemplateResultService.deleteByItemIdsStuIdsSubId(templagetItemIds.toArray(new String[0]),stuIdSet.toArray(new String[]{}), acadyear, semester, subjectId);
			}else
				studevelopTemplateResultService.deleteByItemIdsStuIds(templagetItemIds.toArray(new String[0]),stuIdSet.toArray(new String[]{}),acadyear, semester);
			studevelopTemplateResultService.saveAll(resultList.toArray(new StudevelopTemplateResult[]{}));
		}else if("6".equals(code)){
			mcodeRemoteService.findByMcodeIds("");
			List<McodeDetail> mcodeList=SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-PYDJLB"),new TR<List<McodeDetail>>(){});
			Map<String,McodeDetail> detailContentMap=mcodeList.stream().collect(Collectors.toMap(McodeDetail::getMcodeContent, Function.identity(),(k1,k2)->k1));
			
			List<StuEvaluateRecord> recordList = new ArrayList<>();
			StuEvaluateRecord record=null;
			for(String[] arr:datas){
				String[] errorData=new String[4];
				sequence++;
				if(StringUtils.isBlank(arr[0])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]="";
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(arr[1])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="姓名";
					errorData[2]="";
					errorData[3]="姓名不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(arr[2])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="评语等级";
					errorData[2]="";
					errorData[3]="评语等级不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(arr[3])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="个性特点";
					errorData[2]="";
					errorData[3]="个性特点不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(arr[5])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="老师寄语";
					errorData[2]="";
					errorData[3]="老师寄语不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Student student=stuCodeMap.get(arr[0]);
				if (student == null) {
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]=arr[0];
					errorData[3]="该学号不存在，或该学号对应的学生不是该班级的学生";
					errorDataList.add(errorData);
					continue;
				}
				if (!arr[1].equals(student.getStudentName())) {
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]=arr[1];
					errorData[3]="该学号对应的学生姓名错误";
					errorDataList.add(errorData);
					continue;
				}
				if (studentIdsList.contains(student.getId())) {
					errorData[0]=String.valueOf(sequence);
					errorData[1]="学号";
					errorData[2]=arr[1];
					errorData[3]="请勿重复导入成绩";
					errorDataList.add(errorData);
					continue;
				}
				if(!detailContentMap.containsKey(arr[2])){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="评语等级";
					errorData[2]=arr[2];
					errorData[3]="对应的选项名称有误!";
					errorDataList.add(errorData);
					continue;
				}
				if(arr[3].length()>100){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="个性特点";
					errorData[2]=arr[3];
					errorData[3]="不能超过100个字符!";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isNotBlank(arr[4]) && arr[4].length()>100){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="兴趣爱好";
					errorData[2]=arr[4];
					errorData[3]="不能超过100个字符!";
					errorDataList.add(errorData);
					continue;
				}
				if(arr[5].length()>150){
					errorData[0]=String.valueOf(sequence);
					errorData[1]="老师寄语";
					errorData[2]=arr[5];
					errorData[3]="不能超过150个字符!";
					errorDataList.add(errorData);
					continue;
				}
				String te="<p style=\"line-height: 1.75em;\"><strong><span style=\"font-size: 18px; font-family: 楷体;\">"+arr[5];
				te+="</span></strong><br/></p>";
				studentIdsList.add(student.getId());
				successCount++;
				//保存数据
				record=new StuEvaluateRecord();
				record.setId(UuidUtils.generateUuid());
				record.setStudentId(student.getId());
				record.setAcadyear(acadyear);
				record.setSemester(semester);
				record.setEvaluateLevel(detailContentMap.get(arr[2]).getThisId());
				record.setStrong(arr[3]);
				record.setHobby(arr[4]);
				record.setTeacherEvalContent(te);
				stuIdSet.add(student.getId());
				recordList.add(record);
			}
			if(CollectionUtils.isNotEmpty(stuIdSet)){
				stuEvaluateRecordService.deleteByStuIds(acadyear, semester,stuIdSet.toArray(new String[]{}));
				stuEvaluateRecordService.saveAll(recordList.toArray(new StuEvaluateRecord[]{}));
			}
		}
		int errorCount = totalSize - successCount;
        String result1 = result(totalSize,successCount,errorCount,errorDataList);
        return result1;
	}
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String classId = request.getParameter("classId");
		String code = request.getParameter("code");
		String subjectId = request.getParameter("subjectId");
		StuCheckAttDto dto=new StuCheckAttDto();
		dto.setAcadyear(acadyear);
		dto.setSemester(semester);
		dto.setClassId(classId);
		dto.setCode(code);
		dto.setSubjectId(subjectId);
		ArrayList<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
		//获取学生ids
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
		Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		StringBuilder titleName=new StringBuilder(acadyear+"年");
		titleName.append("1".equals(semester)?"第一学期":"第二学期");
		titleName.append(clazz.getClassNameDynamic());
		Set<String> stuIdSet = stuList.stream().map(Student::getId).collect(Collectors.toSet()); 
		Map<String,String> inMap = null;
		List<String> titleList = null;
	    //获取班级下的所有学生
		if("1".equals(code)){//成绩
			StuDevelopSubject subject=stuDevelopSubjectService.findOne(subjectId);
			titleName.append(subject.getName());
			titleName.append("成绩导入");
			List<StudevelopTemplateItem> templateItemList=new ArrayList<>();
			titleList = getRowTitleList(dto,templateItemList);//表头
			if(CollectionUtils.isNotEmpty(templateItemList)){
				Set<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toSet());
				//通过项目id获取输入方式为单选的内容
				List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
				Map<String ,StudevelopTemplateOptions>  optionIdMap = new HashMap<>();
		        if(CollectionUtils.isNotEmpty(templateOptionsList)){
		            optionIdMap = templateOptionsList.stream().collect(Collectors.toMap(StudevelopTemplateOptions::getId, Function.identity()));
		        }
				//获取对应的结果数据
				List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentIds(templagetItemIds.toArray(new String[0]), 
						stuIdSet.toArray(new String[]{}) ,acadyear , semester);
//				Map<String,StudevelopTemplateResult> noTypeMap=new HashMap<>();
//				Map<String,Map<String,StudevelopTemplateResult>> typeMapMap=new HashMap<>();
				Map<String,StudevelopTemplateResult> resultMap=new HashMap<>();
				for(StudevelopTemplateResult result : templateResultList) {
					if(!subjectId.equals(result.getSubjectId())){
						continue;
					}
					String key=result.getStudentId()+"_"+result.getTemplateItemId();
					if(StringUtils.isNotBlank(result.getCategoryId())){
						resultMap.put(key+"_"+result.getCategoryId(), result);
					}else{
						resultMap.put(key, result);
					}
					/*if(StringUtils.isNotBlank(result.getCategoryId())){
						if(!cateMapMap.containsKey(key)){
							cateMapMap.put(key, new HashMap<>());
						}
						cateMapMap.get(key).put(result.getCategoryId(), result);
					}else{
						noCateMap.put(key,result);
					}*/
				}
				List<StuDevelopCateGory> cateGoryList=stuDevelopCateGoryService.findListBySubjectId(subjectId);	
				StudevelopTemplateResult result=null;
				for(Student stu:stuList){
					inMap = new HashMap<String, String>();
					inMap.put("*学号", stu.getStudentCode());
					inMap.put("*姓名", stu.getStudentName());
					for (StudevelopTemplateItem item : templateItemList) {
						String key=stu.getId()+"_"+item.getId();
						if("11".equals(item.getObjectType()) && CollectionUtils.isNotEmpty(cateGoryList)){//对学科类别
							for (StuDevelopCateGory gory : cateGoryList) {
								if(resultMap.containsKey(key+"_"+gory.getId())){
									result=resultMap.get(key+"_"+gory.getId());
									if("1".equals(item.getSingleOrInput())){//输入
										inMap.put(item.getItemName()+"/"+gory.getCategoryName(), result.getResult());
									}else{
										inMap.put(item.getItemName()+"/"+gory.getCategoryName(),optionIdMap.containsKey(result.getTemplateOptionId())?
												optionIdMap.get(result.getTemplateOptionId()).getOptionName():null);
									}
								}
							}
						}else if("12".equals(item.getObjectType()) || CollectionUtils.isEmpty(cateGoryList)){//对学科
							if(resultMap.containsKey(key)){
								result=resultMap.get(key);
								if("1".equals(item.getSingleOrInput())){//输入
									inMap.put(item.getItemName(), result.getResult());
								}else{
									inMap.put(item.getItemName(),optionIdMap.containsKey(result.getTemplateOptionId())?
											optionIdMap.get(result.getTemplateOptionId()).getOptionName():null);
								}
							}
						}
					}
					recordList.add(inMap);
				}
			}
		}else if("2".equals(code) || "3".equals(code)){
			titleName.append("2".equals(code)?"身心健康导入":"思想素质导入");
			List<StudevelopTemplateItem> templateItemList=new ArrayList<>();
			titleList = getRowTitleList(dto,templateItemList);//表头
			if(CollectionUtils.isNotEmpty(templateItemList)){
				Set<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toSet());
				//通过项目id获取输入方式为单选的内容
				List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
//				Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
				Map<String ,StudevelopTemplateOptions>  optionIdMap = new HashMap<>();
		        if(CollectionUtils.isNotEmpty(templateOptionsList)){
//		            optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
		            optionIdMap = templateOptionsList.stream().collect(Collectors.toMap(StudevelopTemplateOptions::getId, Function.identity()));
		        }
				//获取对应的结果数据
				List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentIds(templagetItemIds.toArray(new String[0]), 
						stuIdSet.toArray(new String[]{}) ,acadyear , semester);
				Map<String,StudevelopTemplateResult> resultMap=templateResultList.stream().collect(Collectors.toMap(k->k.getStudentId()+"_"+k.getTemplateItemId(), Function.identity(),(k1,k2)-> k1));
				StudevelopTemplateResult inResult=null;
				for(Student stu:stuList){
					inMap = new HashMap<String, String>();
					inMap.put("*学号", stu.getStudentCode());
					inMap.put("*姓名", stu.getStudentName());
					for(StudevelopTemplateItem item:templateItemList){
						String value=null; 
						inResult=resultMap.get(stu.getId()+"_"+item.getId());
						if(inResult!=null){
							if("1".equals(item.getSingleOrInput())){//输入
								value=inResult.getResult();
							}else{
								value=optionIdMap.containsKey(inResult.getTemplateOptionId())?
									optionIdMap.get(inResult.getTemplateOptionId()).getOptionName():null;
							}
						}
						inMap.put(item.getItemName(), value);
					}
					recordList.add(inMap);
				}
			}
		}else if("6".equals(code)){
			titleName.append("期末评价导入");
			titleList = getRowTitleList(dto,null);//表头
			List<StuEvaluateRecord> evaRedList = stuEvaluateRecordService.findListByCls(acadyear, semester,stuIdSet.toArray(new String[]{}));
			Map<String,StuEvaluateRecord> evaRedMap=evaRedList.stream().collect(Collectors.toMap(StuEvaluateRecord::getStudentId, Function.identity(),(k1,k2)->k1));
			//评语登记 微代码
			Map<String,McodeDetail> mcodeMap=SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-PYDJLB"),new TR<Map<String,McodeDetail>>(){});
			StuEvaluateRecord evaRed=null;
			for(Student stu:stuList){
				inMap = new HashMap<String, String>();
				inMap.put("*学号", stu.getStudentCode());
				inMap.put("*姓名", stu.getStudentName());
				if(evaRedMap.containsKey(stu.getId())){
					evaRed=evaRedMap.get(stu.getId());
					if(mcodeMap.containsKey(evaRed.getEvaluateLevel())){
						inMap.put("*评语等级", mcodeMap.get(evaRed.getEvaluateLevel()).getMcodeContent());
					}
					inMap.put("*个性特点", evaRed.getStrong());
					inMap.put("兴趣爱好", evaRed.getHobby());
					String te=evaRed.getTeacherEvalContent();
					if(StringUtils.isNotBlank(te) && te.contains("楷体;\">")){
						String te1=te.split("楷体;\">")[1];
						if(te1.contains("</span>")){
							inMap.put("*老师寄语", te1.split("</span>")[0]);
						}
					}else{
						inMap.put("*老师寄语", evaRed.getTeacherEvalContent());
					}
				}
				recordList.add(inMap);
			}
		}
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), titleList);
		ExportUtils exportUtils = ExportUtils.newInstance();
		exportUtils.exportXLSFile(titleName.toString(), titleMap, sheetName2RecordListMap, response);
	}
	public List<String> getRowTitleList(StuCheckAttDto dto,List<StudevelopTemplateItem> templateItemList) {
		List<String> rowTitleList=new ArrayList<>();
		rowTitleList.add("*学号");
		rowTitleList.add("*姓名");
		String acadyear=dto.getAcadyear();
	    String semester=dto.getSemester();
	    String classId=dto.getClassId();
	    String code=dto.getCode();
	    if("1".equals(code) || "2".equals(code) || "3".equals(code)){ //成绩、身心健康和思想素质
  			//获取班级年级信息
			Clazz  cla = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
			int section =grade.getSection();
			String gradeId="1".equals(code)?grade.getId():null;
			String unitId=getLoginInfo().getUnitId();
			//获取模板
			List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(section), code,unitId);
			StudevelopTemplate template = null;
			if(CollectionUtils.isNotEmpty(templateList)){
				template = templateList.get(0);
				//获取项目信息
				List<StudevelopTemplateItem> templateItemList1 = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId(),null,"0");
				if(CollectionUtils.isNotEmpty(templateItemList1)){
					if("1".equals(code)){//成绩
						String subjectId=dto.getSubjectId();
						List<StuDevelopCateGory> cateGoryList=stuDevelopCateGoryService.findListBySubjectId(subjectId);				    	
				    	for (StudevelopTemplateItem item : templateItemList1) {
							if("11".equals(item.getObjectType())){//对学科类别
								if(CollectionUtils.isNotEmpty(cateGoryList)){
									for (StuDevelopCateGory gory : cateGoryList) {
										rowTitleList.add(item.getItemName()+"/"+gory.getCategoryName());
									}
								}else{
									rowTitleList.add(item.getItemName());
								}
							}else{//对学科
								rowTitleList.add(item.getItemName());
							}
						}
					}else{
						Collections.sort(templateItemList1, new Comparator<StudevelopTemplateItem>() {
							@Override
							public int compare(StudevelopTemplateItem o1,
									StudevelopTemplateItem o2) {
								return o1.getObjectType().compareTo(o2.getObjectType());
							}
						});
						for (StudevelopTemplateItem studevelopTemplateItem : templateItemList1) {
							rowTitleList.add(studevelopTemplateItem.getItemName());
						}
					}
					if(templateItemList!=null){
						templateItemList.addAll(templateItemList1);
					}
				}
			}
		}else if("6".equals(code)){//期末评价
			rowTitleList.add("*评语等级");
			rowTitleList.add("*个性特点");
			rowTitleList.add("兴趣爱好");
			rowTitleList.add("*老师寄语");
		}
		
		return rowTitleList;
	}
	@RequestMapping("/validate")
	@ResponseBody
	public String validate(String filePath, String validRowStartNo,StuCheckAttDto dto) {
		logger.info("模板校验中......");
		if (StringUtils.isBlank(validRowStartNo)) {
			validRowStartNo = "0";
		}try{
			List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
					Integer.valueOf(validRowStartNo),getRowTitleList(dto,null).size());
			return templateValidate(datas, getRowTitleList(dto,null));
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、改变选项后请重新上传模板</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		return null;
	}

	@Override
	public String getObjectName() {
		return "resultImport";
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
