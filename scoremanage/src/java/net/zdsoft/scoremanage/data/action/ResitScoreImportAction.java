package net.zdsoft.scoremanage.data.action;

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

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.entity.ResitInfo;
import net.zdsoft.scoremanage.data.entity.ResitScore;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ResitInfoService;
import net.zdsoft.scoremanage.data.service.ResitScoreService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/scoremanage/resitScoreImport")
public class ResitScoreImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(ResitScoreImportAction.class);
	@Autowired
	private SemesterRemoteService semesterService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private SchoolRemoteService  schoolService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ResitInfoService resitInfoService;
	@Autowired
	private GradeRemoteService gradeService;
	@Autowired
	private ClassRemoteService classService;
	@Autowired
	private ScoreInfoService scoreInfoService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private SubjectInfoService subjectInfoService;
	@Autowired
	private StudentRemoteService studentService;
	@Autowired
	private ResitScoreService resitScoreService;
	@RequestMapping("/main")
	public String execute(String acadyear, String semester, String examId, String gradeId,String subjectId,ModelMap map) {
		LoginInfo loginInfo = getLoginInfo();
	    String unitId=loginInfo.getUnitId();
		// 业务名称
		map.put("businessName", "补考成绩录入");
		// 导入URL 
		map.put("businessUrl", "/scoremanage/resitScoreImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/scoremanage/resitScoreImport/template?acadyear="+acadyear+"&semester="+semester+"&examId="+examId
				+"&gradeId="+gradeId+"&subjectId="+subjectId);
		// 导入对象
		map.put("objectName", "");
		// 导入说明
		map.put("description", "");
		map.put("businessKey", UuidUtils.generateUuid());
		// 导入说明
		StringBuffer description=new StringBuffer();
		description.append(getDescription());
		
		//导入参数
    	map.put("acadyear", acadyear);
    	map.put("semester", semester);
    	map.put("examId", examId);
		map.put("unitId", unitId);
	    map.put("gradeId", gradeId);
	    map.put("subjectId", subjectId);
	    map.put("description", description);
		return "/scoremanage/resitScore/resitScoreImport.ftl";
	}

	@Override
	public String getObjectName() {
		return "resitScoreExport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("学号");
		tis.add("姓名");
		tis.add("行政班级");
		tis.add("科目");
		tis.add("补考成绩类型");
		tis.add("原始成绩");
		tis.add("录入补考成绩");
		return tis;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		logger.info("业务数据处理中......");
		JSONObject jsStr = JSONObject.parseObject(params); //将字符串{“id”：1}
		String examId = jsStr.getString("examId");
		String acadyear = jsStr.getString("acadyear");
		String semester = jsStr.getString("semester");
		String gradeId = jsStr.getString("gradeId");
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		String jsonMsg = resitScoreService.saveAllResitScore(getLoginInfo().getUnitId(),acadyear,semester, examId,gradeId, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String acadyear = request.getParameter("acadyear");
		String semester = request.getParameter("semester");
		String examId = request.getParameter("examId");
		String gradeId = request.getParameter("gradeId");
		List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
		scoreInfoList = scoreInfoList2(acadyear, semester, examId, gradeId);
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		int i =1;
		for(ScoreInfo item : scoreInfoList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(i));
			sMap.put("学号", item.getStudentCode());
			sMap.put("姓名", item.getStudentName());
			sMap.put("行政班级", item.getClsName());
			sMap.put("科目", item.getSubjectName());
			String bkType="";
			String ysScore="";
			if(StringUtils.isNotBlank(item.getToScore())){
				bkType="总评成绩";
				ysScore = item.getToScore();
			}else{
				bkType="考评成绩";
				ysScore = item.getScore();
			}
			sMap.put("补考成绩类型", bkType);
			sMap.put("原始成绩", ysScore);
			sMap.put("录入补考成绩", item.getResitScore());
			recordList.add(sMap);
			i++;
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("学生补考信息导入", titleMap, sheetName2RecordListMap, response);		
	}

	public List<ScoreInfo> scoreInfoList2(String acadyear, String semester, String examId, String gradeId) {
		List<Clazz> clsList = SUtils.dt(classService.findByInGradeIds(new String[]{gradeId}), new TR<List<Clazz>>(){}); 
		Set<String> clsIdSet = new HashSet<String>();
		for(Clazz cls : clsList){
			clsIdSet.add(cls.getId());
		}
		List<ScoreInfo> scoreInfoListTemp = scoreInfoService.findListByClsIds(getLoginInfo().getUnitId(), acadyear, semester, examId, ScoreDataConstants.ACHI_SCORE,clsIdSet.toArray(new String[0]));
        
		List<ResitInfo> resitInfoList = resitInfoService.listResitInfoBy(getLoginInfo().getUnitId(), acadyear, semester, examId, gradeId);
		Set<String> courseIdSet = new HashSet<String>();
		for(ResitInfo info : resitInfoList){
			courseIdSet.add(info.getSubjectId());
		}
		List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
		Set<String> stuIdSet = new HashSet<String>();
		Set<String> clsIdSet2 = new HashSet<String>();
		Map<String, Course> courseMap = new HashMap<String, Course>();
        if(CollectionUtils.isNotEmpty(courseIdSet)){       	
        	List<Course> coureList = SUtils.dt(courseService.findListByIds(courseIdSet.toArray(new String[0])), new TR<List<Course>>(){}); 
        	for(Course course : coureList){    	
        		courseMap.put(course.getId(), course);
        		int initPassMark = course.getInitPassMark();
        		for(ScoreInfo scoreInfo : scoreInfoListTemp){
        			if(StringUtils.isNotBlank(scoreInfo.getToScore())){
        				if(course.getId().equals(scoreInfo.getSubjectId()) && Float.parseFloat(scoreInfo.getToScore())<initPassMark){
        					scoreInfoList.add(scoreInfo);
        					stuIdSet.add(scoreInfo.getStudentId());
        					clsIdSet2.add(scoreInfo.getClassId());
        				}
        			}else{
        				if(course.getId().equals(scoreInfo.getSubjectId()) && Float.parseFloat(scoreInfo.getScore())<initPassMark){
        					scoreInfoList.add(scoreInfo);
        					stuIdSet.add(scoreInfo.getStudentId());
        					clsIdSet2.add(scoreInfo.getClassId());
        				}
        			}
        		}
        	}
        	Map<String, String> stuNameMap = new HashMap<String, String>();
        	Map<String, String> clsNameMap = new HashMap<String, String>();
        	Map<String, String> stuCodeMap = new HashMap<String, String>();
        	Map<String, String> clsCodeMap = new HashMap<String, String>();
        	if(CollectionUtils.isNotEmpty(stuIdSet)){      	
        		List<Student> stuList = SUtils.dt(studentService.findListByIds(stuIdSet.toArray(new String[0])), new TR<List<Student>>(){}); 
        		for(Student student : stuList){
        			stuNameMap.put(student.getId(), student.getStudentName());
        			stuCodeMap.put(student.getId(), student.getStudentCode());
        		}
        	}
        	if(CollectionUtils.isNotEmpty(clsIdSet2)){  
        		List<Clazz> clsList2 = SUtils.dt(classService.findClassListByIds(clsIdSet2.toArray(new String[0])), new TR<List<Clazz>>(){}); 
        		for(Clazz cls : clsList2){
        			clsNameMap.put(cls.getId(), cls.getClassNameDynamic());
        			clsCodeMap.put(cls.getId(), cls.getClassCode());
        		}
        	}
        	List<ResitScore> resitScoreList = resitScoreService.listResitScoreBy(getLoginInfo().getUnitId(), examId);
        	Map<String, String> resitScoreMap = new HashMap<String, String>();
        	for(ResitScore score : resitScoreList){
        		resitScoreMap.put(score.getStudentId()+score.getSubjectId(), score.getScore());
        	}
        	for(ScoreInfo scoreInfo : scoreInfoList){
        		scoreInfo.setStudentName(stuNameMap.get(scoreInfo.getStudentId()));
        		scoreInfo.setClsName(clsNameMap.get(scoreInfo.getClassId()));
        		scoreInfo.setStudentCode(stuCodeMap.get(scoreInfo.getStudentId()));
        		scoreInfo.setClsCode(clsCodeMap.get(scoreInfo.getClassId()));
        		scoreInfo.setSubjectName(courseMap.get(scoreInfo.getSubjectId()).getSubjectName());
        		scoreInfo.setResitScore(resitScoreMap.get(scoreInfo.getStudentId()+scoreInfo.getSubjectId()));
        		scoreInfo.setSubjectCode(courseMap.get(scoreInfo.getSubjectId()).getSubjectCode());
        	}
        	Collections.sort(scoreInfoList, new Comparator<ScoreInfo>() {
        		@Override
        		public int compare(ScoreInfo o1, ScoreInfo o2) {
        			if(StringUtils.isNotBlank(o1.getSubjectCode()) && StringUtils.isNotBlank(o2.getSubjectCode())){
        				if(!o1.getSubjectCode().equals(o2.getSubjectCode())){
        					return o1.getSubjectCode().compareTo(o2.getSubjectCode());
        				}
        			}
        			if(StringUtils.isNotBlank(o1.getClsCode()) && StringUtils.isNotBlank(o2.getClsCode())){
        				if(!o1.getClsCode().equals(o2.getClsCode())){
        					return o1.getClsCode().compareTo(o2.getClsCode());
        				}
        			}
        			if(StringUtils.isNotBlank(o1.getStudentId()) && StringUtils.isNotBlank(o2.getStudentId())){
        				if(!o1.getStudentId().equals(o2.getStudentId())){
        					return o1.getStudentId().compareTo(o2.getStudentId());
        				}
        			}
        			return o1.getStudentId().compareTo(o2.getStudentId());
        		}
        	});
        }		
        return scoreInfoList;
	}
}
