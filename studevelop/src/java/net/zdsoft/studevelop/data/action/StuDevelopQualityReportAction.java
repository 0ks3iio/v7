package net.zdsoft.studevelop.data.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.eis.diathesis.remote.service.DiathesisRemoteService;
import net.zdsoft.eis.remote.service.ExamRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.HtmlToPdf;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.entity.StusysStudentBak;
import net.zdsoft.newstusys.remote.service.StusysStudentBakRemoteService;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StuDevelopSubjectHealthDto;
import net.zdsoft.studevelop.data.dto.StuHealthRecordDto;
import net.zdsoft.studevelop.data.dto.StuSubjectAchiDto;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;
import net.zdsoft.studevelop.data.entity.StuDevelopQualityReportSet;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;
import net.zdsoft.studevelop.data.entity.StudevelopSchoolNotice;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;
import net.zdsoft.studevelop.data.service.StuCheckAttendanceService;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopPunishmentService;
import net.zdsoft.studevelop.data.service.StuDevelopQualityReportSetService;
import net.zdsoft.studevelop.data.service.StuDevelopRewardsService;
import net.zdsoft.studevelop.data.service.StuDevelopSchoolNoticeService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.data.service.StudevelopDutySituationService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateResultService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopQualityReportAction extends BaseAction{
	
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studenteRemoteService;
	@Autowired
	private StuDevelopRewardsService stuDevelopRewardsService;
	@Autowired
	private StuDevelopPunishmentService stuDevelopPunishmentService;
	@Autowired
	private StuEvaluateRecordService stuEvaluateRecordService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private StuCheckAttendanceService stuCheckAttendanceService;
	@Autowired
	private StuDevelopQualityReportSetService stuDevelopQualityReportSetService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private StuDevelopSubjectService stuDevelopSubjectService;
	@Autowired
	private StuDevelopCateGoryService stuDevelopCateGoryService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private StuDevelopSchoolNoticeService stuDevelopSchoolNoticeService;
	@Autowired
	private StudevelopDutySituationService studevelopDutySituationService;
	@Autowired
	private StusysStudentBakRemoteService stusysStudentBakRemoteService;
	@Autowired
	private StudevelopTemplateService studevelopTemplateService;
	@Autowired
	private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
	@Autowired
	private StudevelopTemplateResultService studevelopTemplateResultService;
	@Autowired
	private StudevelopTemplateItemService studevelopTemplateItemService;
	private DiathesisRemoteService diathesisRemoteService;
	private ExamRemoteService examRemoteService;
	
	private boolean forHistory = false;// 历史档案查询
	
	@RequestMapping("/stuQualityReport/index/page")
    @ControllerInfo(value = "素质报告单tab")
	public String stuQualityReportHead(ModelMap map){		
		LoginInfo login=getLoginInfo();
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
        boolean isOne=false;
        //1代表学生  3代表家长
        if(login.getOwnerType()==User.OWNER_TYPE_STUDENT){
        	isOne=true;
        	map.put("studentId", login.getOwnerId());
        }else if(login.getOwnerType()==User.OWNER_TYPE_FAMILY){
        	isOne=true;
        	Family family=SUtils.dc(familyRemoteService.findOneById(login.getOwnerId()),Family.class);
        	map.put("studentId", family==null?"":family.getStudentId());
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,login.getUnitId()), Semester.class);
        List<Clazz> classListTemp = new ArrayList<Clazz>();
        if(!isOne){
        	List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(login.getUnitId(),semester.getAcadyear()), new TR<List<Clazz>>() {});
        	for(Clazz cls : classList){
        		if(cls.getSection()==1 || cls.getSection()==2 || cls.getSection()==3){
        			classListTemp.add(cls);
        		}
        	}
        }
        String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deployRegion" ,deployRegion);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("classList", classListTemp);
        map.put("isOne", isOne);
		return "/studevelop/query/stuQualityReportHead.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/stuQualityReport/clsList")
	public List<Clazz> subjectList(String acadyear){
		return SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
	}
	@ResponseBody
	@RequestMapping("/stuQualityReport/toPdf")
	public String toPdf(String acadyear, String semester,String classId, 
			String stuIds, ModelMap map,HttpServletRequest request){
		final String key=classId+"_"+acadyear+semester;
//		RedisUtils.set(key,"success");
		if(RedisUtils.get(key)==null){
			RedisUtils.set(key,"start");
		}else{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("type", RedisUtils.get(key));
			if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
				RedisUtils.del(key);
			}
			return JSON.toJSONString(jsonObject);
		}
		if(StringUtils.isNotBlank(stuIds)){
			MyThread myThread=new MyThread();
			myThread.setAcadyear(acadyear);
			myThread.setSemester(semester);
			myThread.setKey(key);
			myThread.setServerUrl(UrlUtils.getPrefix(request));
			myThread.setStuIds(stuIds);
			Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
			myThread.setUnitId(clazz.getSchoolId());//学校id
			myThread.setClazz(clazz);
			Thread thread=new Thread(myThread);
			thread.start();
		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("type", RedisUtils.get(key));
		if("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))){
			RedisUtils.del(key);
		}
		return success("操作成功");
	}
	class MyThread implements Runnable{
		private String acadyear;
		private String semester;
		private String key;
		private String unitId;
		private String serverUrl;
		private String stuIds;
		private Clazz clazz;
		@Override
		public void run() {
			try {
        		String[] studentIds = stuIds.split(",");
        		toPdfStart(acadyear, semester, clazz.getId(),clazz.getGradeId()
        				,unitId,studentIds,serverUrl);
        		RedisUtils.set(key,"success");
        	}catch (Exception e) {
        		e.printStackTrace();
        		RedisUtils.set(key,"error");
			}
			return;
		}
		public void setAcadyear(String acadyear) {
			this.acadyear = acadyear;
		}
		public void setSemester(String semester) {
			this.semester = semester;
		}
		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}
		public void setServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
		}
		public void setStuIds(String stuIds) {
			this.stuIds = stuIds;
		}
		public void setClazz(Clazz clazz) {
			this.clazz = clazz;
		}
		public void setKey(String key) {
			this.key = key;
		}
	}
	public void toPdfStart(String acadyear, String semester,String classId
			,String gradeId,String unitId,String[] studentIds, String serverUrl){
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		String sourcePath="attachment"+File.separator+"stu_qReport"+File.separator+unitId//+File.separator+gradeId
				+File.separator+acadyear+semester+File.separator+classId;
		Map<String,String> parMap=new HashMap<>();
		parMap.put("Landscape", String.valueOf(false));//参数设定
		List<StusysStudentBak> stuBakList =new ArrayList<>(); 
		StusysStudentBak stuBak=null;
		//获取学生以及班级数据 
		List<Student> stuList=SUtils.dt(studentRemoteService.findListByIds(studentIds), new TR<List<Student>>(){});
		Map<String, Clazz> clazzMap=EntityUtils.getMap(SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear),new TR<List<Clazz>>(){}),Clazz::getId);
		//判断部署地
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		String height=null;
		String width=null;
		if(StuDevelopConstant.DEPLOY_CIXI.equals(deployRegion)){
			height="330";
			width="240";
		}
		for(Student stu:stuList){
			String studentId=stu.getId();
			String uu=dir.getDir()+File.separator+sourcePath+File.separator+studentId+".pdf";
			File f = new File(uu);
			String urlStr="/studevelop/common/stuQualityReport/reportHtml?acadyear="+
					acadyear+"&semester="+semester+"&studentId="+studentId+"&unitId="+unitId;
			if(f.exists()){
				f.delete();//删除
			}
			boolean flag=HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu,height,width,2000,parMap);
			System.out.println(flag+"---thread  pdf程序运行中----"+serverUrl+urlStr);
			stuBak=new StusysStudentBak();
			EntityUtils.copyProperties(stu, stuBak);
			stuBak.setId(UuidUtils.generateUuid());
			stuBak.setStudentId(stu.getId());
			stuBak.setCreationTime(new Date());
			stuBak.setModifyTime(new Date());
			stuBak.setAcadyear(acadyear);
			stuBak.setSemester(Integer.valueOf(semester));
			Clazz clazz=clazzMap.get(stu.getClassId());
			if(clazz!=null){
				stuBak.setClassName(clazz.getClassNameDynamic());
				stuBak.setTeacherId(clazz.getTeacherId());
			}
			stuBakList.add(stuBak);
		}
		stusysStudentBakRemoteService.deleteByStuIds(acadyear, semester, studentIds);
		stusysStudentBakRemoteService.saveAll(stuBakList.toArray(new StusysStudentBak[]{}));
//		studevelopAttachmentService.deleteByObjIds(acadyear, semester, studentIds);
//		studevelopAttachmentService.saveAll(sAttList.toArray(new StudevelopAttachment[]{}));
	}
	@RequestMapping("/stuQualityReport/studentList")
	@ControllerInfo(value = "学生list")
	public String studentList(String acadyear, String semester,String classId, ModelMap map){
		List<Student> stuList = SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
		if(CollectionUtils.isNotEmpty(stuList)){
			map.put("stuList", stuList);
			String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
			map.put("deployRegion" ,deployRegion);
		}
		map.put("type", RedisUtils.get(classId+"_"+acadyear+semester));
		return "/studevelop/query/stuQualityReportStuList.ftl";
	}
	@ResponseBody
	@RequestMapping("/stuQualityReport/stuHavePdf")
	@ControllerInfo(value = "学生是否有pdf")
	public String stuHavePdf(String acadyear, String semester,String classId, HttpServletResponse response){
		try {
			List<Student> stuList = SUtils.dt(studenteRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
			StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
			if(dir == null){
				return null;
			}
			JSONObject JSON=new JSONObject();
			if(CollectionUtils.isNotEmpty(stuList)){
				JSONArray array=new JSONArray();
				String sourcePath=null;
				String uu=null;
				File file = null;
				for(Student stu:stuList){
					JSONObject json=new JSONObject();
					sourcePath="attachment"+File.separator+"stu_qReport"+File.separator+stu.getSchoolId()+File.separator+acadyear+semester+File.separator+stu.getClassId();
					uu=dir.getDir()+File.separator+sourcePath+File.separator+stu.getId()+".pdf";
					file = new File(uu);
					json.put("studentId", stu.getId());
					if(file == null || !file.exists()) {
						json.put("havePdf", false);
					}else
						json.put("havePdf", true);
					array.add(json);
				}
				JSON.put("haveList", array);
			}
			return JSON.toJSONString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	@RequestMapping("/stuQualityReport/getPdf")
	@ControllerInfo(value = "学生pdf")
	public String getPdf(String acadyear, String semester,String studentId, HttpServletResponse response){
		try {
			StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
			if(dir == null){
				return null;
			}
			Student stu = SUtils.dc(studenteRemoteService.findOneById(studentId), Student.class);
			StringBuilder name=new StringBuilder(stu.getStudentName()+" ").append(acadyear).append("学年");
			name.append("1".equals(semester)?"第一学期":"第二学期").append("素质报告单.pdf");
			
			String sourcePath="attachment"+File.separator+"stu_qReport"+File.separator+stu.getSchoolId()//+File.separator+gradeId
					+File.separator+acadyear+semester+File.separator+stu.getClassId();
			String uu=dir.getDir()+File.separator+sourcePath+File.separator+studentId+".pdf";
			File file = new File(uu);
			if(file == null || !file.exists()) {
				return null;
			}
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ URLEncoder.encode(name.toString(),
							"UTF-8").replace("+", "%20")
							+ "\";");
			response.getOutputStream().write(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	@RequestMapping("/common/stuQualityReport/reportHtml")
	@ControllerInfo(value = "查看学生素质报告生成pdf的html")
	public String reportHtml(String acadyear, String semester, String studentId,String unitId, ModelMap map){
		StuDevelopQualityReportSet stuDevelopQualityReportSet = getTemplate(acadyear, semester, studentId,unitId);
		if(forHistory) {
			stuDevelopQualityReportSet = null;
		}
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		boolean tobj = StuDevelopConstant.DEPLOY_BINJIANG.equals(deployRegion);
		if(tobj || stuDevelopQualityReportSet == null || forHistory){
			try{
				setBinJiang(acadyear,semester,studentId,map);
			}catch (Exception e){
				log.error(e.getMessage());
				e.printStackTrace();
			}
		} else {
			try {
				stuQuality(acadyear,semester,studentId,map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.put("deployRegion" ,deployRegion);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("studentId", studentId);
		map.put("stuDevelopQualityReportSet", stuDevelopQualityReportSet);
		if(!StuDevelopConstant.DEPLOY_BINJIANG.equals(deployRegion) && stuDevelopQualityReportSet!=null){
			if(stuDevelopQualityReportSet.getSection() == 1){
				if(stuDevelopQualityReportSet.getTemplate().equals("1")){
					return "/studevelop/query/html/stuQualityReportHtml.ftl";
				}
				else if(stuDevelopQualityReportSet.getTemplate().equals("2")){
					return "/studevelop/query/html/stuQualityReportHtml3.ftl";
				}
			}
			else if(stuDevelopQualityReportSet.getSection() == 2){
				if(stuDevelopQualityReportSet.getTemplate().equals("1")){
					return "/studevelop/query/html/stuQualityReportHtml2.ftl";
				}
				else if(stuDevelopQualityReportSet.getTemplate().equals("2")){
					return "/studevelop/query/html/stuQualityReportHtml4.ftl";
				}
			}
		}
		return "/studevelop/query/html/stuQualityReportHtml5.ftl";
	}
	@RequestMapping("/stuQualityReport/report")
	@ControllerInfo(value = "查看学生素质报告")
	public String report(String acadyear, String semester, String studentId, ModelMap map){
		StuDevelopQualityReportSet stuDevelopQualityReportSet = getTemplate(acadyear, semester, studentId,null);

		if(forHistory) {
			stuDevelopQualityReportSet = null;
		}
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		boolean tobj = StuDevelopConstant.DEPLOY_BINJIANG.equals(deployRegion);
		if(tobj || stuDevelopQualityReportSet == null || forHistory){
			try{
				setBinJiang(acadyear,semester,studentId,map);
			}catch (Exception e){
				log.error(e.getMessage());
				e.printStackTrace();
			}
		} else {
			try {
				stuQuality(acadyear,semester,studentId,map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		map.put("deployRegion" ,deployRegion);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("studentId", studentId);
		map.put("stuDevelopQualityReportSet", stuDevelopQualityReportSet);
		map.put("isOne", getLoginInfo().getOwnerType()==User.OWNER_TYPE_STUDENT||getLoginInfo().getOwnerType()==User.OWNER_TYPE_FAMILY);
		return "/studevelop/query/stuQualityReport.ftl";
	}
	
	@RequestMapping("/stuQualityReport/onBatchPrint")
	@ControllerInfo(value = "批量打印学生素质报告")
	public String onBatchPrint(String acadyear, String semester, String batchId, ModelMap map){
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		map.put("deployRegion" ,deployRegion);
		StuDevelopQualityReportSet stuDevelopQualityReportSet = new StuDevelopQualityReportSet();
		String doNotPrint = "0";
		String batchCourseIdLeft = "";
		if(StringUtils.isNotBlank(batchId)){
			if(batchId.startsWith(",")) {
				batchId = batchId.substring(1);
			}
			String[] batchIds = batchId.split(",");
			int i = 0;
			for (; i < batchIds.length; i++) {
				if (StringUtils.isNotBlank(batchIds[i])) {
					String stuId = batchIds[i];
					try{
						if(StuDevelopConstant.DEPLOY_BINJIANG.equals(deployRegion)){
							setBinJiang(acadyear,semester,stuId,map);
						}else{
							stuQuality(acadyear,semester,stuId,map);
						}
					}catch (Exception e){
					log.error(e.getMessage());
					e.printStackTrace();
				   }

					//选择模板
					stuDevelopQualityReportSet = getTemplate(acadyear, semester, stuId,null);
					i++;
					break;
				}
			}

			if (i < batchIds.length) {
				StringBuilder sb = new StringBuilder();
				for (int j = i; j < batchIds.length; j++) {
					sb.append(",");
					sb.append(batchIds[j]);
				}
				batchCourseIdLeft = sb.toString();
			}
		} else {
			doNotPrint = "1";
		}
		batchId = batchCourseIdLeft;
		map.put("batchId", batchId);
		map.put("doNotPrint", doNotPrint);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("stuDevelopQualityReportSet", stuDevelopQualityReportSet);
		return "/studevelop/query/stuQualityReportPrint.ftl";
	}
	
	public void stuQuality(String acadyear, String semester, String stuId,ModelMap map){
		Student student = SUtils.dc(studentRemoteService.findOneById(stuId), Student.class);
		String unitId=student.getSchoolId();
		//取出模板
	    StuDevelopQualityReportSet  stuDevelopQualityReportSet = getTemplate(acadyear, semester, stuId,unitId);
	    if(stuDevelopQualityReportSet==null){
	    	setBinJiang(acadyear, semester, stuId, map);
	    	
	    }else{
	    	List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsService.findByAcaAndSemAndStuId(acadyear, semester, stuId);
	    	List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentService.findByAcaAndSemAndStuId(acadyear, semester, stuId);
	    	StuEvaluateRecord stuEvaluateRecord;
	    	stuEvaluateRecord = stuEvaluateRecordService.findById(stuId, acadyear, semester);
	    	if(null == stuEvaluateRecord){
	    		stuEvaluateRecord = new StuEvaluateRecord();
	    	}
	    	//取班级姓名
	    	Unit unit = SUtils.dc(unitRemoteService.findTopUnit(unitId),Unit.class);	    
	    	String regionCode = unit.getRegionCode();
	    	Region region = SUtils.dc(regionRemoteService.findRegionsByFullCode(regionCode), Region.class);
	    	String regionName = region.getRegionName();
	    	//取下学期注册日期
	    	String semStr;
	    	String acaStr;
	    	if("1".equals(semester)){
	    		semStr = "2";
	    		acaStr = acadyear;
	    	}else{
	    		String[] acadyearArr = acadyear.split("-");
	    		acaStr =  String.valueOf(Integer.parseInt(acadyearArr[0])+1)+"-"+String.valueOf(Integer.parseInt(acadyearArr[1])+1);
	    		semStr = "1";
	    	}
	    	Semester sem = SUtils.dc(semesterRemoteService.findByAcadYearAndSemester(acaStr, Integer.parseInt(semStr)), Semester.class);
	    	
	    	String classId = student.getClassId();
	    	Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
	    	String gradeId = clazz.getGradeId();
	    	Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
	    	String className = grade.getGradeName()+clazz.getClassName();
	    	String teacherId = clazz.getTeacherId();
	    	Teacher teacher = new Teacher();
	    	if(StringUtils.isNotBlank(teacherId)){
	    		teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
	    	}
	    	School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
	    	//身体健康
//	   		 StuHealthRecord stuHealthRecord;
	    	List<List<StuDevelopSubjectHealthDto>> stuHealthRecordDtoList = new ArrayList<>();
	    	stuHealthRecordDtoList = setHealthBody(stuId,acadyear,semester,String.valueOf(grade.getSection()) ,"2",  map,unitId);
	    	StudevelopSchoolNotice schoolNotice = stuDevelopSchoolNoticeService.getSchoolNoticeByAcadyearSemesterUnitId(acadyear,semester,String.valueOf(grade.getSection()),unitId);
	    	//考勤
	    	StuCheckAttendance stuCheckAttendance;
	    	stuCheckAttendance = stuCheckAttendanceService.findBystudentId(acadyear, semester, stuId);
	    	if(null == stuCheckAttendance){
	    		stuCheckAttendance = new StuCheckAttendance();
	    	}
	    	if(null!=schoolNotice){
	    		stuCheckAttendance.setStudyDate(schoolNotice.getStudyDate());
	    		stuCheckAttendance.setStudyBegin(schoolNotice.getStudyBegin());
	    		stuCheckAttendance.setRegisterBegin(schoolNotice.getRegisterBegin());
	    	}
	    	if(null == stuCheckAttendance.getLate()){
	    		stuCheckAttendance.setLate(0);
	    	}
	    	if(null == stuCheckAttendance.getLeaveEarly()){
	    		stuCheckAttendance.setLeaveEarly(0);
	    	}
	    	if(null == stuCheckAttendance.getWasteVacation()){
	    		stuCheckAttendance.setWasteVacation(0);
	    	}
	    	
	    	//综合素质
	    	List<String[]> qualityOfMind = setQualityofMind(stuId,acadyear,semester ,String.valueOf(grade.getSection()) ,StuDevelopConstant.TEMPLATE_CODE_THOUGHT,unitId);
	    	
	    	if(null == qualityOfMind){
	    		qualityOfMind = new ArrayList<String[]>();
	    	}
	    	// 小学模板1，初中的模板2
	    	if((clazz.getSection() == 1 && "1".equals(stuDevelopQualityReportSet.getTemplate())) ||
	    			(clazz.getSection() == 2 && "2".equals(stuDevelopQualityReportSet.getTemplate()))){
	    		//考试成绩
	    		
	    		List<StuSubjectAchiDto> achilist = getGradeTable(stuId,acadyear,semester,StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);
	    		map.put("achilist", achilist);
//			//滨江初中报告单
//			List<StuSubjectAchiDto> achilistTemp = new ArrayList<StuSubjectAchiDto>();
	    	}else{
	    		List<StuDevelopSubject> subjectList = setScore(stuId,acadyear,semester,"1",unitId);
	    		map.put("subjectList", subjectList);
	    	}
	    	map.put("stuEvaluateRecord", stuEvaluateRecord);
	    	map.put("stuDevelopRewardsList", stuDevelopRewardsList);
	    	map.put("stuDevelopPunishmentList", stuDevelopPunishmentList);
	    	map.put("acadyear", acadyear);
	    	map.put("semester", semester);
	    	map.put("unit", unit);
	    	map.put("student", student);
	    	map.put("className", className);
	    	map.put("teacher", teacher);
	    	map.put("school", school);
	    	
	    	map.put("stuCheckAttendance", stuCheckAttendance);
	    	map.put("qualityOfMind", qualityOfMind);
	    	map.put("regionName", regionName);
	    	map.put("sem", sem);
	    	map.put("stuHealthRecordDtoList", stuHealthRecordDtoList);
	    }
	    
	}
	
	public List<String[]> setQualityofMind( String studentId ,String acadyear ,String semester  ,String section ,String code,String unitId){
		List<String[]> qualityOfMind = new ArrayList<>();
		if(StringUtils.isBlank(unitId)){
			unitId = getLoginInfo().getUnitId();
		}
		List<StudevelopTemplateItem> templateItemList = getTemplateItemList(studentId,  acadyear,  semester,  unitId,  section,  code);
		for (int m=0; m<10; m++) {
			String[] arr = new String[2];
			qualityOfMind.add(arr);
			if(m >= templateItemList.size()){
				break;
			}
			StudevelopTemplateItem item = templateItemList.get(m);

			arr[0] = item.getItemName();
			List<StudevelopTemplateOptions> options = item.getTemplateOptions();
			StudevelopTemplateResult result = item.getTemplateResult();
			if(result != null){
				for (StudevelopTemplateOptions option : options) {
					if(option.getId().equals(result.getTemplateOptionId())){
						String name = option.getOptionName();
						if("好".equals(name)){
							arr[1] = "A";
						}else if("较好".equals(name)){
							arr[1] = "D";
						}else if("需努力".equals(name)){
							arr[1] = "E";
						}
					}
				}
			}
		}
        return qualityOfMind;
	}
	private List<StudevelopTemplateItem>  getTemplateItemList(String studentId ,String acadyear ,String semester ,String unitId ,String section ,String code){
		List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,null,String.valueOf(section), code,unitId);
		StudevelopTemplate template = null;
		if(CollectionUtils.isNotEmpty(templateList)){
			template = templateList.get(0);
		}else{
			return new ArrayList<>();
		}
		List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId(),null,StuDevelopConstant.HEALTH_IS_NOT_CLOSED);

		List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
		List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.findListByIn("templateItemId",templagetItemIds.toArray());
		List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId ,acadyear,semester);
		Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(templateResultList)) {
			templateResultMap = templateResultList.stream().collect(Collectors.toMap(e -> e.getTemplateItemId(), Function.identity()));
		}
		Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(templateOptionsList)){
			optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
		}
		for (StudevelopTemplateItem item : templateItemList) {
			List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
			item.setTemplateOptions(list);
			StudevelopTemplateResult result = templateResultMap.get(item.getId());
			if (result == null) {
				result = new StudevelopTemplateResult();
				result.setTemplateId(template.getId());
				result.setTemplateItemId(item.getId());
			}
			item.setTemplateResult(result);
		}
		return templateItemList;
	}
	public List<List<StuDevelopSubjectHealthDto>> setHealthBody( String studentId ,String acadyear ,String semester ,String section ,String code, ModelMap map,String unitId){
        if(StringUtils.isBlank(unitId)){
        	unitId=getLoginInfo().getUnitId();
        }
		StuHealthRecordDto stuHealthRecordDto = new StuHealthRecordDto();

		map.put("stuHealthRecordDto", stuHealthRecordDto);
        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,null,String.valueOf(section), code,unitId);
        StudevelopTemplate template = null;
        if(CollectionUtils.isNotEmpty(templateList)){
            template = templateList.get(0);
        }
        else{
            return new ArrayList<>();
        }
        List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.findListBy("templateId",template.getId());
        List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
        List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId ,acadyear ,semester);
        Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(templateResultList)) {
            templateResultMap = templateResultList.stream().collect(Collectors.toMap(e -> e.getTemplateItemId(), Function.identity()));
        }
        Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(templateOptionsList)){
//            optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
			for (StudevelopTemplateOptions options : templateOptionsList) {
				List<StudevelopTemplateOptions> list = optionsMap.get(options.getTemplateItemId());
				if(list == null){
					list = new ArrayList<>();
					optionsMap.put(options.getTemplateItemId(), list);
				}
				list.add(options);
			}
        }
        List<StudevelopTemplateItem> list1 = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId() , null,"0");
        List<List<StuDevelopSubjectHealthDto>> allOptionsDtoList = new ArrayList<>();
        List<List<StuDevelopSubjectHealthDto>> allSingleDtoList = new ArrayList<>();
		List<StuDevelopSubjectHealthDto> dtoListTemp = new ArrayList<>();

        for (StudevelopTemplateItem item : list1) {
            List<StuDevelopSubjectHealthDto> dtoList = new ArrayList<>();
			StuDevelopSubjectHealthDto dto = new StuDevelopSubjectHealthDto();
			dto.setNameOrValue(item.getItemName());
			dtoList.add(dto);
            StudevelopTemplateResult result = templateResultMap.get(item.getId());
			if ("1".equals(item.getSingleOrInput())) {

				dtoListTemp.addAll(dtoList);
				StuDevelopSubjectHealthDto dto2 = new StuDevelopSubjectHealthDto();
				if(result != null){
					dto2.setNameOrValue(result.getResult());
					setStuHealthRecord(stuHealthRecordDto,item,result.getResult());
				}
				dto2.setColRow("colspan");
				dto2.setNumber(2);
				dtoListTemp.add(dto2);


			}else{
				List<StudevelopTemplateOptions> options = optionsMap.get(item.getId());
				if (CollectionUtils.isNotEmpty(options)) {
					int size = options.size();
					for (StudevelopTemplateOptions option : options) {
						StuDevelopSubjectHealthDto dto2 = new StuDevelopSubjectHealthDto();
						dto2.setNameOrValue(option.getOptionName());
						StuDevelopSubjectHealthDto dto3 = new StuDevelopSubjectHealthDto();
						if (result != null && result.getTemplateOptionId().equals(option.getId())) {
							dto3.setNameOrValue("selected");
						}
						if(size ==1){
							dto2.setColRow("colspan");
							dto2.setNumber(3);
							dto3.setColRow("colspan");
							dto3.setNumber(3);
						}else if(size == 2){
							dto2.setColRow("colspan");
							dto2.setNumber(2);
						}
						dtoList.add(dto2);
						dtoList.add(dto3);
					}

				}
				allOptionsDtoList.add(dtoList);
			}
        }
		if (CollectionUtils.isNotEmpty(dtoListTemp)) {
			List<StuDevelopSubjectHealthDto> dtos = null;
			for (int i = 0; i < dtoListTemp.size(); i++) {
				StuDevelopSubjectHealthDto dto = dtoListTemp.get(i);
				if(i%4 == 0){
					dto.setColRow("colspan");
					dto.setNumber(2);
					dtos = new ArrayList<>();
					allSingleDtoList.add(dtos);
				}
				dtos.add(dto);

			}
		}
		List<List<StuDevelopSubjectHealthDto>> dtoList = new ArrayList<>();
		dtoList.addAll(allOptionsDtoList);
		dtoList.addAll(allSingleDtoList);
		map.put("allHealthOptionsDtoList" ,allOptionsDtoList);
		map.put("allHealthSingleDtoList" ,allSingleDtoList);
        return dtoList;
    }
    public void setStuHealthRecord(StuHealthRecordDto recordDto,StudevelopTemplateItem item , String result){
		if("左眼".equals(item.getItemName())){
			recordDto.setLeftEye(result);
		}else if("右眼".equals(item.getItemName())){
			recordDto.setRightEye(result);
		}else if("身高(cm)".equals(item.getItemName())){
			recordDto.setHeight(result);
		}else if("体重(kg)".equals(item.getItemName())){
			recordDto.setWeight(result);
		}else if("视力左".equals(item.getItemName())){
			recordDto.setLeftEye(result);
		}else if("视力右".equals(item.getItemName())){
			recordDto.setRightEye(result);
		}

	}
	@SuppressWarnings("unchecked")
	public List<StuSubjectAchiDto>  getGradeTable(String studentId ,String acadyear ,String semester ,String code,String unitId){
		if(StringUtils.isBlank(unitId)){
			unitId = getLoginInfo().getUnitId();
		}
		List<StuSubjectAchiDto> achilist = new ArrayList<StuSubjectAchiDto>();
		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
		String gradeId = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getGradeId();
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(unitId, acadyear, semester, gradeId);
		Set<String> subIdSet = new HashSet<String>();
		for(StuDevelopSubject sub : subjectList){
			subIdSet.add(sub.getId());
		}
		List<StuDevelopCateGory> stuDevelopCateGoryList;
		Map<String , List<StuDevelopCateGory>> categoryMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subIdSet.toArray(new String[0]));
		}else{
			stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		}
		if(CollectionUtils.isNotEmpty(stuDevelopCateGoryList)){
			categoryMap = stuDevelopCateGoryList.stream().collect(Collectors.groupingBy(c->c.getSubjectId()));
		}
		for(StuDevelopSubject sub : subjectList){
			List<StuDevelopCateGory> cateGoryList =categoryMap.get(sub.getId());
			sub.setCateGoryList(cateGoryList);
		}
		List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(grade.getSection()), StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);
		StudevelopTemplate template = null;
		if(CollectionUtils.isNotEmpty(templateList)){
			template = templateList.get(0);
		}
		List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.findListBy("templateId",template.getId());
        Map<String, StudevelopTemplateItem> templateItemMap = new HashMap<>();
        for (StudevelopTemplateItem templateItem : templateItemList) {
            if ("平时".equals(templateItem.getItemName())) {
                templateItemMap.put("Psachi" ,templateItem);
            } else if ("期末".equals(templateItem.getItemName())) {
                templateItemMap.put("Qmachi" ,templateItem);
            } else if ("学习态度".equals(templateItem.getItemName())) {
                templateItemMap.put("Xxtd" ,templateItem);
            }
        }
		List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
		List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
		Map<String, List<StudevelopTemplateOptions>> tempalteOptionsMap = new HashMap<>();
		for (StudevelopTemplateOptions options : templateOptionsList) {
			List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(options.getTemplateItemId());
			if (list == null) {
				list = new ArrayList<>();
				tempalteOptionsMap.put(options.getTemplateItemId(), list);
			}
			list.add(options);
		}

		List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId ,acadyear ,semester);
		Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(templateResultList)) {
			templateResultMap = templateResultList.stream().collect(Collectors.toMap(e -> e.getTemplateItemId()+"_"+e.getSubjectId()+"_"+(StringUtils.isEmpty(e.getCategoryId())?"":e.getCategoryId()), Function.identity()));
		}
		for (StudevelopTemplateItem item : templateItemList) {
			for(StuDevelopSubject sub : subjectList){
				List<StuDevelopCateGory> cateGoryList =sub.getCateGoryList();
				String key = item.getId()+"_"+sub.getId()+"_";
				if (CollectionUtils.isNotEmpty(cateGoryList)) {
					for (StuDevelopCateGory cateGory : cateGoryList) {
						key += cateGory.getId();
						StudevelopTemplateResult result = templateResultMap.get(key);
						if (result == null) {
							result = new StudevelopTemplateResult();
							result.setStudentId(studentId);
							result.setTemplateItemId(item.getId());
							result.setTemplateId(item.getTemplateId());
							result.setSubjectId(sub.getId());
							result.setCategoryId(cateGory.getId());
							templateResultMap.put(key , result);
						}else {
							if(item.getSingleOrInput().equals("2")){
								List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(item.getId());
								if(CollectionUtils.isNotEmpty(list)){
									for (StudevelopTemplateOptions options : list) {
										if (options.getId().equals(result.getTemplateOptionId())) {
											result.setResult(options.getOptionName());
										}
									}
								}
							}
						}
					}
				}else{
					StudevelopTemplateResult result = templateResultMap.get(key);
					if (result == null) {
						result = new StudevelopTemplateResult();
						result.setStudentId(studentId);
						result.setTemplateItemId(item.getId());
						result.setTemplateId(item.getTemplateId());
						result.setSubjectId(sub.getId());
						templateResultMap.put(key , result);
					}else {
						if(item.getSingleOrInput().equals("2")){
							List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(item.getId());
							if(CollectionUtils.isNotEmpty(list)){
								for (StudevelopTemplateOptions options : list) {
									if (options.getId().equals(result.getTemplateOptionId())) {
										result.setResult(options.getOptionName());
									}
								}
							}
						}

					}
				}

			}
		}

		Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(templateOptionsList)){
			optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
		}
		for (StudevelopTemplateItem item : templateItemList) {
			List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
			item.setTemplateOptions(list);
		}
        String[] strs = new String[]{"Psachi", "Qmachi", "Xxtd"};
        for(StuDevelopSubject sub : subjectList){
            StuSubjectAchiDto achi = new StuSubjectAchiDto();
            achi.setSubid(sub.getId());
            achi.setSubname(sub.getName());
            Class c1 = achi.getClass();
            for (String str : strs) {
                StudevelopTemplateItem item =templateItemMap.get(str);
                if(item == null){
                	break;
				}
                String  key = item.getId()+"_"+sub.getId()+"_";
                StudevelopTemplateResult result = templateResultMap.get(key);
				if(result == null){
					result = new StudevelopTemplateResult();
				}
                try {
                    Method m = c1.getDeclaredMethod("set"+str,String.class);
                    m.invoke(achi , result.getResult());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
			achilist.add(achi);
        }
		return  achilist;
	}

//	public List<StuDevelopSubject> setScore(String studentId ,String acadyear ,String semester ,String code){
//		List<StuSubjectAchiDto> achilist = new ArrayList<StuSubjectAchiDto>();
//		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
//		String studentName = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getStudentName();
//		String gradeId = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getGradeId();
//		List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);
//		Set<String> subIdSet = new HashSet<String>();
//		for(StuDevelopSubject sub : subjectList){
//			subIdSet.add(sub.getId());
//		}
//		List<StuDevelopCateGory> stuDevelopCateGoryList;
//		Map<String , List<StuDevelopCateGory>> categoryMap = new HashMap<>();
//		if(CollectionUtils.isNotEmpty(subIdSet)){
//			stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subIdSet.toArray(new String[0]));
//		}else{
//			stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
//		}
//		if(CollectionUtils.isNotEmpty(stuDevelopCateGoryList)){
//			categoryMap = stuDevelopCateGoryList.stream().collect(Collectors.groupingBy(c->c.getSubjectId()));
//		}
//		for(StuDevelopSubject sub : subjectList){
//			List<StuDevelopCateGory> cateGoryList =categoryMap.get(sub.getId());
//			sub.setCateGoryList(cateGoryList);
//		}
//
//		Student stu = SUtils.dc(studentRemoteService.findOneById(studentId) , Student.class);
//		Clazz  cla = SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
//		Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
//		int section =grade.getSection();
//		String unitId=getLoginInfo().getUnitId();
//		List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(section), StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);
//		StudevelopTemplate template = null;
//		if(CollectionUtils.isNotEmpty(templateList)){
//			template = templateList.get(0);
//		}
//
//		List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.findListBy("templateId",template.getId());
//		Map<String, StudevelopTemplateItem> templateItemMap = new HashMap<>();
//
//		for (StudevelopTemplateItem templateItem : templateItemList) {
//			if ("平时".equals(templateItem.getItemName())) {
//				templateItemMap.put("Psachi" ,templateItem);
//			} else if ("期末".equals(templateItem.getItemName())) {
//				templateItemMap.put("Qmachi" ,templateItem);
//			} else if ("学习态度".equals(templateItem.getItemName())) {
//				templateItemMap.put("Xxtd" ,templateItem);
//			}
//		}
//		List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
//		List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.findListByIn("templateItemId",templagetItemIds.toArray());
//		Map<String, List<StudevelopTemplateOptions>> tempalteOptionsMap = new HashMap<>();
//		for (StudevelopTemplateOptions options : templateOptionsList) {
//			List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(options.getTemplateItemId());
//			if (list == null) {
//				list = new ArrayList<>();
//				tempalteOptionsMap.put(options.getTemplateItemId(), list);
//			}
//			list.add(options);
//		}
//
//		List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId);
//		Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
//		if (CollectionUtils.isNotEmpty(templateResultList)) {
//			templateResultMap = templateResultList.stream().collect(Collectors.toMap(e -> e.getTemplateItemId()+"_"+e.getSubjectId()+"_"+(StringUtils.isEmpty(e.getCategoryId())?"":e.getCategoryId()), Function.identity()));
//		}
//		Map<String, String> scoreMap = new HashMap<>();
//		for (StudevelopTemplateItem item : templateItemList) {
//			for(StuDevelopSubject sub : subjectList){
//				List<StuDevelopCateGory> cateGoryList =sub.getCateGoryList();
//				String key = item.getId()+"_"+sub.getId()+"_";
//				if (CollectionUtils.isNotEmpty(cateGoryList)) {
//					for (StuDevelopCateGory cateGory : cateGoryList) {
//						key += cateGory.getId();
//						StudevelopTemplateResult result = templateResultMap.get(key);
//						if (result == null) {
//							result = new StudevelopTemplateResult();
//							result.setStudentId(studentId);
//							result.setTemplateItemId(item.getId());
//							result.setTemplateId(item.getTemplateId());
//							result.setSubjectId(sub.getId());
//							result.setCategoryId(cateGory.getId());
//							templateResultMap.put(key , result);
//						}else {
//							if(item.getSingleOrInput().equals("2")){
//								List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(item.getId());
//								for (StudevelopTemplateOptions options : list) {
//									if (options.getId().equals(result.getTemplateOptionId())) {
//										result.setResult(options.getOptionName());
//									}
//								}
//							}
//
//
//						}
//					}
//				}else{
//					StudevelopTemplateResult result = templateResultMap.get(key);
//					if (result == null) {
//						result = new StudevelopTemplateResult();
//						result.setStudentId(studentId);
//						result.setTemplateItemId(item.getId());
//						result.setTemplateId(item.getTemplateId());
//						result.setSubjectId(sub.getId());
//						templateResultMap.put(key , result);
//					}else {
//						if(item.getSingleOrInput().equals("2")){
//							List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(item.getId());
//							for (StudevelopTemplateOptions options : list) {
//								if (options.getId().equals(result.getTemplateOptionId())) {
//									result.setResult(options.getOptionName());
//								}
//							}
//						}
//
//					}
//				}
//
//			}
//		}
//
//		Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
//		if(CollectionUtils.isNotEmpty(templateOptionsList)){
//			optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
//		}
//		for (StudevelopTemplateItem item : templateItemList) {
//			List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
//			item.setTemplateOptions(list);
//		}
//		String[] strs = new String[]{"Psachi", "Qmachi", "Xxtd"};
//		for(StuDevelopSubject sub : subjectList){
//			List<StuDevelopCateGory> cateGoryList =sub.getCateGoryList();
//			if (CollectionUtils.isNotEmpty(cateGoryList)) {
//				for (StuDevelopCateGory cateGory : cateGoryList) {
//					StuSubjectAchiDto achi = new StuSubjectAchiDto();
//					Class c1 = achi.getClass();
//					for (String str : strs) {
//						StudevelopTemplateItem item =templateItemMap.get(str);
//						if(item == null){
//							break;
//						}
//						String  key = item.getId()+"_"+sub.getId()+"_" + cateGory.getId();
//						StudevelopTemplateResult result = templateResultMap.get(key);
//						if(result != null){
//							try {
//								Method m = c1.getDeclaredMethod("set"+str,String.class);
//								Object o = m.invoke(achi , result.getResult());
//							} catch (NoSuchMethodException e) {
//								e.printStackTrace();
//							} catch (IllegalAccessException e) {
//								e.printStackTrace();
//							} catch (InvocationTargetException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//					cateGory.setStuSubjectAchiDto(achi);
//				}
//			}else{
//				StuSubjectAchiDto achi = new StuSubjectAchiDto();
//				achi.setSubid(sub.getId());
//				achi.setSubname(sub.getName());
//				Class c1 = achi.getClass();
//				for (String str : strs) {
//					StudevelopTemplateItem item =templateItemMap.get(str);
//					if(item == null){
//						break;
//					}
//					String  key = item.getId()+"_"+sub.getId()+"_";
//					StudevelopTemplateResult result = templateResultMap.get(key);
//					try {
//						Method m = c1.getDeclaredMethod("set"+str,String.class);
//						Object o = m.invoke(achi , result.getResult());
//					} catch (NoSuchMethodException e) {
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					} catch (InvocationTargetException e) {
//						e.printStackTrace();
//					}
//				}
//				sub.setStuSubjectAchiDto(achi);
//			}
//
//
//		}
//		return subjectList;
//	}
	public StuDevelopQualityReportSet getTemplate(String acadyear, String semester, String studentId,String unitId){
		String classId ;
		if (forHistory) {
			StusysStudentBak stu = SUtils.dc(stusysStudentBakRemoteService.findStuByStuIdSemester(acadyear, semester, studentId), StusysStudentBak.class);
			classId = stu.getClassId();
		} else {
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			classId = student.getClassId(); 
		}
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		int section = clazz.getSection();
		StuDevelopQualityReportSet stuDevelopQualityReportSet;
		if(StringUtils.isBlank(unitId)){
			unitId=getLoginInfo().getUnitId();
		}
		stuDevelopQualityReportSet = stuDevelopQualityReportSetService.findByAll(unitId, acadyear, semester, section);
		if(null == stuDevelopQualityReportSet){
			stuDevelopQualityReportSet = stuDevelopQualityReportSetService.findByAll(StuDevelopConstant.DEFAULT_UNIT_ID, StuDevelopConstant.DEFAULT_ACADYEAR, StuDevelopConstant.DEFAULT_SEMESTER, section);
		}
		String deployRegion = systemIniRemoteService.findValue(BaseConstants.SYS_OPTION_REGION);
		if (StuDevelopConstant.DEPLOY_CIXI.equals(deployRegion)) {
			if (stuDevelopQualityReportSet == null) {
				stuDevelopQualityReportSet = new StuDevelopQualityReportSet();
			}
			stuDevelopQualityReportSet.setTemplate("1");
			stuDevelopQualityReportSet.setSection(section);
		}else {
			stuDevelopQualityReportSet = null;
		}

		return stuDevelopQualityReportSet;
	}
	@SuppressWarnings("unchecked")
	public List<StuDevelopSubject>  setScore(String studentId ,String acadyear ,String semester ,String code,String unitId){
		if(StringUtils.isBlank(unitId)){
			unitId=getLoginInfo().getUnitId();
		}
		String classId = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class).getClassId();
		Clazz clazz =  SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		String gradeId = clazz.getGradeId();
		int section = clazz.getSection();
		List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(unitId, acadyear, semester, gradeId);
		Set<String> subIdSet = new HashSet<String>();
		for(StuDevelopSubject sub : subjectList){
			subIdSet.add(sub.getId());
		}
		List<StuDevelopCateGory> stuDevelopCateGoryList;
		Map<String , List<StuDevelopCateGory>> categoryMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subIdSet.toArray(new String[0]));
		}else{
			stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		}
		if(CollectionUtils.isNotEmpty(stuDevelopCateGoryList)){
			categoryMap = stuDevelopCateGoryList.stream().collect(Collectors.groupingBy(c->c.getSubjectId()));
		}
		for(StuDevelopSubject sub : subjectList){
			List<StuDevelopCateGory> cateGoryList =categoryMap.get(sub.getId());
			sub.setCateGoryList(cateGoryList);
		}
		List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(section), StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);

		StudevelopTemplate template = null;
		if(CollectionUtils.isNotEmpty(templateList)){
			template = templateList.get(0);
		}else{
			return new ArrayList<>();
		}

		List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.findListBy("templateId",template.getId());
		Map<String, StudevelopTemplateItem> templateItemMap = new HashMap<>();

		for (StudevelopTemplateItem templateItem : templateItemList) {
			if ("平时".equals(templateItem.getItemName())) {
				templateItemMap.put("Psachi" ,templateItem);
			} else if ("期末".equals(templateItem.getItemName())) {
				templateItemMap.put("Qmachi" ,templateItem);
			} else if ("学习态度".equals(templateItem.getItemName())) {
				templateItemMap.put("Xxtd" ,templateItem);
			} else if ("交流与合作能力".equals(templateItem.getItemName())) {
				templateItemMap.put("Coomunication" ,templateItem);
			} else if ("发现、提出问题能力".equals(templateItem.getItemName())) {
				templateItemMap.put("Discovery" ,templateItem);
			}
		}
		List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
		List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.findListByIn("templateItemId",templagetItemIds.toArray());
		Map<String, List<StudevelopTemplateOptions>> tempalteOptionsMap = new HashMap<>();
		for (StudevelopTemplateOptions options : templateOptionsList) {
			List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(options.getTemplateItemId());
			if (list == null) {
				list = new ArrayList<>();
				tempalteOptionsMap.put(options.getTemplateItemId(), list);
			}
			list.add(options);
		}

		List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId ,acadyear,semester);
		Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(templateResultList)) {
			for (StudevelopTemplateResult result : templateResultList) {
				String key = result.getTemplateItemId()+"_"+result.getSubjectId()+"_";
				if (StringUtils.isNotEmpty(result.getCategoryId())) {
					key +=result.getCategoryId();
				}
				templateResultMap.put(key, result);
			}
		}
		for (StudevelopTemplateItem item : templateItemList) {
			for(StuDevelopSubject sub : subjectList){
				List<StuDevelopCateGory> cateGoryList =sub.getCateGoryList();

				if ("11".equals(item.getObjectType()) && CollectionUtils.isNotEmpty(cateGoryList)) {
					for (StuDevelopCateGory cateGory : cateGoryList) {
						String key = item.getId()+"_"+sub.getId()+"_"+cateGory.getId();
						StudevelopTemplateResult result = templateResultMap.get(key);
						if (result != null) {
							if(item.getSingleOrInput().equals("2")){
								List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(item.getId());
								for (StudevelopTemplateOptions options : list) {
									if (options.getId().equals(result.getTemplateOptionId())) {
										result.setResult(options.getOptionName());
									}
								}
							}
						}
					}
				}
				if("12".equals(item.getObjectType())){
					String key = item.getId()+"_"+sub.getId()+"_";
					StudevelopTemplateResult result = templateResultMap.get(key);
					if (result != null) {
						if(item.getSingleOrInput().equals("2")){
							List<StudevelopTemplateOptions> list = tempalteOptionsMap.get(item.getId());
							for (StudevelopTemplateOptions options : list) {
								if (options.getId().equals(result.getTemplateOptionId())) {
									result.setResult(options.getOptionName());
								}
							}
						}
					}
				}


			}
		}

//		Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
//		if(CollectionUtils.isNotEmpty(templateOptionsList)){
//			optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
//		}
//		for (StudevelopTemplateItem item : templateItemList) {
//			List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
//			item.setTemplateOptions(list);
//		}
		String[] strs = new String[]{"Psachi", "Qmachi", "Xxtd","Coomunication" ,"Discovery"};
		for(StuDevelopSubject sub : subjectList){
			List<StuDevelopCateGory> cateGoryList =sub.getCateGoryList();
			StuSubjectAchiDto achi = new StuSubjectAchiDto();
			achi.setSubid(sub.getId());
			achi.setSubname(sub.getName());
			for (String str : strs) {
				StudevelopTemplateItem item =templateItemMap.get(str);
				if( item == null ){
					break;
				}
				if ("12".equals(item.getObjectType())) {

					Class c1 = achi.getClass();
					String  key = item.getId()+"_"+sub.getId()+"_";
					StudevelopTemplateResult result = templateResultMap.get(key);
					if(result == null){
						result = new StudevelopTemplateResult();
					}
					try {
						Method m = c1.getDeclaredMethod("set"+str,String.class);
						m.invoke(achi , result.getResult());
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
			sub.setStuSubjectAchiDto(achi);
			if (CollectionUtils.isNotEmpty(cateGoryList)) {
				for (StuDevelopCateGory cateGory : cateGoryList) {
					StuSubjectAchiDto achiCateGory = new StuSubjectAchiDto();
					Class c1 = achiCateGory.getClass();
					for (String str : strs) {
						StudevelopTemplateItem item =templateItemMap.get(str);
						if(item == null){
							break;
						}
						String  key = item.getId()+"_"+sub.getId()+"_" + cateGory.getId();
						StudevelopTemplateResult result = templateResultMap.get(key);
						if(result != null){
							try {
								Method m = c1.getDeclaredMethod("set"+str,String.class);
								m.invoke(achiCateGory , result.getResult());
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
					cateGory.setStuSubjectAchiDto(achiCateGory);
				}
			}


		}
		return subjectList;
	}
	public void setBinJiang(String acadyear, String semester ,String studentId ,ModelMap map){
		log.info("binJing start .....");
		// 取学生基本信息 TODO
		Student student=null;
		String className = "";
		String teacherId = "";
		String gradeId;
		if(forHistory) {
			StusysStudentBak bak = SUtils.dc(stusysStudentBakRemoteService.findStuByStuIdSemester(acadyear, semester, studentId), StusysStudentBak.class);
			student = new Student();
			EntityUtils.copyProperties(bak, student);
			student.setId(bak.getStudentId());
			className = bak.getClassName();
			teacherId = bak.getTeacherId();
		} else {
			student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
		}
		if(null==student)
			student=new Student();
		String unitId =student.getSchoolId();
		String classId = student.getClassId();
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
		gradeId = clazz.getGradeId();
		Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
		String section = String.valueOf(grade.getSection());
		if (!forHistory) {
			className = grade.getGradeName() + clazz.getClassName();
			teacherId = clazz.getTeacherId();
		}
		Teacher teacher = new Teacher();
		if(StringUtils.isNotBlank(teacherId)){
			teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class);
		}
		School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
		Unit unit = SUtils.dc(unitRemoteService.findTopUnit(unitId),Unit.class);
		// 基本信息end====
		
		// 奖惩
		List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsService.findByAcaAndSemAndStuId(acadyear, semester, studentId);
		List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentService.findByAcaAndSemAndStuId(acadyear, semester, studentId);
		// 期末评价
		StuEvaluateRecord stuEvaluateRecord = stuEvaluateRecordService.findById(studentId, acadyear, semester);
		if(null == stuEvaluateRecord){
			stuEvaluateRecord = new StuEvaluateRecord();
		}
		map.put("stuEvaluateRecord", stuEvaluateRecord);
		map.put("stuDevelopRewardsList", stuDevelopRewardsList);
		map.put("stuDevelopPunishmentList", stuDevelopPunishmentList);
		map.put("acadyear", acadyear);
		map.put("semester", semester);
		map.put("unit", unit);
		map.put("student", student);
		map.put("studentName", student.getStudentName());
		map.put("className", className);
		map.put("teacher", teacher);
		map.put("school", school);

		List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(unitId,acadyear,semester,gradeId);
//		List<StuDevelopProject> projectList = stuDevelopProjectService.stuDevelopProjectList(unitId,acadyear,semester,gradeId);
//		map.put("subjectProjectList",projectList);

		List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(section), StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);
		StudevelopTemplate template = null;
		if(CollectionUtils.isNotEmpty(templateList)){
			template = templateList.get(0);
			List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.findListBy("templateId",template.getId());

			Set<String> subjectIds = new HashSet<>();
			for(StuDevelopSubject subject : subjectList){
				subjectIds.add(subject.getId());
			}
			int subCategorySize=0;
			Map<String ,List<StuDevelopCateGory>>  cateGoryMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(subjectIds)){
				List<StuDevelopCateGory> cateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subjectIds.toArray(new String[0]));
				if (CollectionUtils.isNotEmpty(cateGoryList)) {
					for(StuDevelopCateGory dc : cateGoryList) {
						List<StuDevelopCateGory> tls = cateGoryMap.get(dc.getSubjectId());
						if(tls == null) {
							tls = new ArrayList<>();
							cateGoryMap.put(dc.getSubjectId(), tls);
						}
						tls.add(dc);
					}
//					cateGoryMap = EntityUtils.getListMap(cateGoryList, StuDevelopCateGory::getSubjectId, null);
				}
				for(StuDevelopSubject subject : subjectList){
					List<StuDevelopCateGory> list = cateGoryMap.get(subject.getId());
					if(CollectionUtils.isEmpty(list)){
						subCategorySize++;
					}else{
						subject.setCateGoryList(list);
						subCategorySize +=list.size();
					}
				}
			}
			
			Map<String ,List<StuDevelopSubjectHealthDto>> subjectHealthDtoMap = new HashMap<>();
			Set<String> templateItemIds = templateItemList.stream().map(StudevelopTemplateItem::getId).collect(Collectors.toSet());
			List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templateItemIds.toArray(new String[0]), studentId ,acadyear,semester);
			List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templateItemIds.toArray(new String[0]));
			Map<String, StudevelopTemplateOptions> templateOptionsMap = templateOptionsList.stream().collect(Collectors.toMap(r -> r.getId(), Function.identity()));
			Map<String, StudevelopTemplateItem> templateItemMap = templateItemList.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
			Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
			for (StudevelopTemplateResult result : templateResultList) {
				StringBuffer sb = new StringBuffer(result.getSubjectId());
				if(StringUtils.isNotEmpty(result.getCategoryId())){
					sb.append("_"+result.getCategoryId());
				}
				sb.append("_"+result.getTemplateItemId());
				StudevelopTemplateItem item = templateItemMap.get(result.getTemplateItemId());
				if ("2".equals(item.getSingleOrInput())) {
					if(StringUtils.isNotEmpty(result.getTemplateOptionId())){
						StudevelopTemplateOptions options = templateOptionsMap.get(result.getTemplateOptionId());
						if(options != null){
							result.setResult(options.getOptionName());
						}
					}
				}
				templateResultMap.put(sb.toString(), result);
			}
			log.info("binJing midle .....");

			List<StuDevelopSubjectHealthDto> healthDtoList = new ArrayList<>();
			healthDtoList = getHealthBodyDtoList(acadyear, semester, studentId, String.valueOf(section), unitId);

			int healthProjectRows =healthDtoList.size()/2;

			if(subCategorySize <= healthProjectRows){
				//多出3行放个性特点
				int dif =0;
				if(healthProjectRows == 0){
					dif = 10;
				}else {
					dif = healthProjectRows-subCategorySize+3;
				}

				for(int i=0;i<dif;i++){
					StuDevelopSubject subject = new StuDevelopSubject();
					subject.setId("subject_is_null"+i);
					subjectList.add(subject);
				}
				subCategorySize=subCategorySize+dif;
			}

			map.put("templateItemList",templateItemList);
			map.put("templateResultMap",templateResultMap);
			map.put("healthDtoList" ,healthDtoList);
			map.put("subjectList" ,subjectList);
			map.put("subjectHealthDtoMap" ,subjectHealthDtoMap);
			map.put("subCategorySize",subCategorySize);
			map.put("healthProjectSize",healthDtoList.size());

			StudevelopSchoolNotice schoolNotice = stuDevelopSchoolNoticeService.getSchoolNoticeByAcadyearSemesterUnitId(acadyear,semester,section,unitId);
			if(schoolNotice == null){
				schoolNotice = new StudevelopSchoolNotice();
				schoolNotice.setUnitId(unitId);
				schoolNotice.setAcadyear(acadyear);
				schoolNotice.setSemester(semester);
				schoolNotice.setSchoolSection(section);
			}
			map.put("schoolNotice" ,schoolNotice);

			List<StudevelopDutySituation> dutySituationList = studevelopDutySituationService.getDutySituationList(studentId, acadyear,semester);
			map.put("dutySituationList",dutySituationList);
		}
			
	}

	public List<StuDevelopSubjectHealthDto>   getHealthBodyDtoList(String acadyear, String semester ,String studentId ,String section ,String unitId){

        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,null,section, StuDevelopConstant.TEMPLATE_CODE_HEALTH,unitId);
        StudevelopTemplate template = null;
        if(CollectionUtils.isNotEmpty(templateList)){
            template = templateList.get(0);
        }

		List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.getTemplateListByObjTypeStuId(template.getId(), studentId ,acadyear ,semester);

        Map<String, List<StudevelopTemplateItem>> templateItemMapObjType = new HashMap<>();
        for (StudevelopTemplateItem item : templateItemList){
            List<StudevelopTemplateItem> list = templateItemMapObjType.get(item.getObjectType());
            if (list == null) {
                list = new ArrayList<>();
                templateItemMapObjType.put(item.getObjectType(), list);
            }
            list.add(item);
        }
		List<StuDevelopSubjectHealthDto> healthDtoList = new ArrayList<>();
		List<McodeDetail> projectTyps = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);
		for (McodeDetail projectTyp : projectTyps) {

			StuDevelopSubjectHealthDto dto1 = new StuDevelopSubjectHealthDto();
			dto1.setNameOrValue(projectTyp.getMcodeContent());
			healthDtoList.add(dto1);
			StuDevelopSubjectHealthDto dto2 = new StuDevelopSubjectHealthDto();
			dto2.setNameOrValue("评价情况");
			healthDtoList.add(dto2);
			String key = "2" + projectTyp.getThisId();
			List<StudevelopTemplateItem> itemList = templateItemMapObjType.get(key);
			if (CollectionUtils.isNotEmpty(itemList)) {
				for (StudevelopTemplateItem item : itemList) {
					StuDevelopSubjectHealthDto dto3 = new StuDevelopSubjectHealthDto();
					dto3.setNameOrValue(item.getItemName());
					healthDtoList.add(dto3);
					StuDevelopSubjectHealthDto dto4 = new StuDevelopSubjectHealthDto();
					healthDtoList.add(dto4);
					StudevelopTemplateResult result = item.getTemplateResult();
					if(result != null){
						if ("1".equals(item.getSingleOrInput())) {
							dto4.setNameOrValue(result.getResult());
						}else{
							List<StudevelopTemplateOptions> optionsList = item.getTemplateOptions();
							for (StudevelopTemplateOptions options : optionsList) {
								if(options.getId().equals(result.getTemplateOptionId())){
									dto4.setNameOrValue(options.getOptionName());
								}

							}
						}
					}

				}
			}
		}

		return healthDtoList;
    }
	@RequestMapping("/historyReport/index/page")
	@ControllerInfo("学生历史信息查询")
	public String historyIndex(ModelMap map) {
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		return "/studevelop/query/historyIndex.ftl";
	}
	
	@RequestMapping("/historyReport/list")
	@ControllerInfo("学生历史信息查询-学生列表")
	public String historyStuList(HttpServletRequest request, ModelMap map) {
		String stuName = request.getParameter("studentName");
		String idcard = request.getParameter("idCard");
		String stuCode = request.getParameter("studentCode");
		List<StusysStudentBak> stuList = SUtils.dt(stusysStudentBakRemoteService.findStuBySchId(getLoginInfo().getUnitId(), stuName, stuCode, idcard), new TR<List<StusysStudentBak>>() {});
		/*StusysStudentBak stu=new StusysStudentBak();
		stu.setStudentId("AFBF3CE2C65F41B1ADD4CF0E3457EDB4");
		stu.setAcadyear("2018-2019");
		stu.setSemester(2);
		stuList.add(stu);*/
		map.put("stuList", stuList);
		return "/studevelop/query/historyStuList.ftl";
	}
	
	@RequestMapping("/historyReport/report")
	@ControllerInfo("学生历史信息查询-单个学生信息")
	public String historyReport(String acadyear, String semester, String studentId, ModelMap map) {
		// TODO
		forHistory = true;
		return report(acadyear, semester, studentId, map);
	}
	
	public DiathesisRemoteService getDiathesisRemoteService() {
		try {
			if(diathesisRemoteService == null){
				diathesisRemoteService = Evn.getBean("diathesisRemoteService");
				log.error("综合素质duddo服务没有开启，没有提供者");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diathesisRemoteService;
	}

	public ExamRemoteService getExamRemoteService() {
		if(examRemoteService == null){
			examRemoteService = Evn.getBean("examRemoteService");
			log.error("教务成绩duddo服务没有开启，没有提供者");
		}
		return examRemoteService;
	}
	
}
