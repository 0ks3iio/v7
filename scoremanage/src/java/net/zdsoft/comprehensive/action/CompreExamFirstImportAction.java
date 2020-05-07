package net.zdsoft.comprehensive.action;

import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.framework.dataimport.DataImportAction;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 *
 * 暂时用不到
 *
 */
@Controller
@RequestMapping("/comprehensive/exam/firstImport")
public class CompreExamFirstImportAction extends DataImportAction{

    private Logger logger = Logger.getLogger(CompreExamFirstImportAction.class);
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private CompreScoreService compreScoreService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;

    @Override
    public String getObjectName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getRowTitleList() {
        return null;
    }

    @Override
    public String dataImport(String filePath, String params) {
        return null;
    }

    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

    }
	
	/*@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map,String gradeId,String classId) {
		// 业务名称
		map.put("businessName", "学考原始成绩");
		// 导入URL 
		map.put("businessUrl", "/comprehensive/exam/firstImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/comprehensive/exam/firstImport/template?gradeId="+gradeId+"&classId="+classId);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		//如果列名在第一行的就不需要传
		map.put("validRowStartNo",0);
		//模板校验
		map.put("validateUrl", "/comprehensive/exam/firstImport/validate");

		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()),
				new TR<List<Grade>>(){});
		//根据年级id查询所有班级
		List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(),
								gradeId), new TR<List<Clazz>>(){});
		JSONObject obj=new JSONObject();
		obj.put("gradeId", gradeId);
		obj.put("classId", classId);
		map.put("paramObj",JSON.toJSONString(obj));

		map.put("gradeId", gradeId);
		map.put("classId", classId);
		map.put("gradeList", gradeList);
		map.put("classList", classList);

		map.put("businessKey", UuidUtils.generateUuid());

		return "/comprehensive/exam/examFirstImport.ftl";
	}


	@Override
	public String getObjectName() {
		return "examImport";
	}

	@Override
	public String getDescription() {
		return "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确，且根据列表页选择的年级或班级导入对应的数据</p>"
				+ "<p>2、导入学生信息不能重复</p>"
				+ "<p>3、导入班级名称为年级名称+班级名称</p>";
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("学号");
		tis.add("姓名");
		tis.add("班级");
		String[] allstr=new String[] {};

		List<Course> courseList=SUtils.dt(courseRemoteService.findByCodes73(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		if(courseList != null)
			courseList.addAll(SUtils.dt(courseRemoteService.findByCodesYSY(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){}));

		for(Course course:courseList){
			tis.add(course.getSubjectName());
		}
		return tis;
	}
	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		String gradeId=request.getParameter("gradeId");
		String classId=request.getParameter("classId");

		List<Course> courseList=SUtils.dt(courseRemoteService.findByCodes73(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){});
		if(courseList != null)
			courseList.addAll(SUtils.dt(courseRemoteService.findByCodesYSY(this.getLoginInfo().getUnitId()),new TR<List<Course>>(){}));

		//加入模板数据
		getRecordList(recordList,gradeId,classId,courseList);
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		//获取年级或班级名称
		Grade grade=SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
		String gradeName=grade==null?"":grade.getGradeName();
		Clazz clazz=null;
		if(StringUtils.isNotBlank(classId)){
			clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
		}
		String className=clazz==null?"":clazz.getClassNameDynamic();
		
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(gradeName+className+"学考原始成绩导入", titleMap, sheetName2RecordListMap, response);

	}

	private void getRecordList(List<Map<String, String>> recordList,String gradeId,String classId,List<Course> courseList){
		String unitId = getLoginInfo().getUnitId();
		String[] classIds=new String[]{classId};
		if(StringUtils.isBlank(classId)){
			classIds=EntityUtils.getList(SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId),new TR<List<Clazz>>(){})
					,"id").toArray(new String[0]);
		}
		Map<String,String> classNameMap=EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(classIds),new TR<List<Clazz>>(){}),"id","classNameDynamic");
		
		List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds),new TR<List<Student>>(){});
		for (Student student : studentList) {
			student.setClassName(classNameMap.get(student.getClassId()));
		}
		Collections.sort(studentList, new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
				if(o1.getClassName()==null || o2.getClassName()==null){
					return 0;
				}
				if(o1.getClassName().equals(o2.getClassName())){
					return o1.getStudentCode().compareTo(o2.getStudentCode());
				}
				return o1.getClassName().compareTo(o2.getClassName());
			}
		});
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId),Semester.class);
		List<CompreScore> scoreList=compreScoreService.getFirstList(unitId, se.getAcadyear(), se.getSemester()+"", CompreStatisticsConstants.SCORE_XKFS,classIds);
		Map<String,String> scoreMap=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(scoreList)){
			for(CompreScore score:scoreList){
				scoreMap.put(score.getStudentId()+"_"+score.getSubjectId(), score.getScore());
			}
		}
		if(CollectionUtils.isNotEmpty(studentList)){
			Map<String,String> valueMap = null;
			for(Student stu:studentList){
				valueMap=new HashMap<String, String>();
				valueMap.put("学号", stu.getStudentCode());
				valueMap.put("姓名", stu.getStudentName());
				valueMap.put("班级", classNameMap.get(stu.getClassId()));
				for(Course course:courseList){
					valueMap.put(course.getSubjectName(), scoreMap.get(stu.getId()+"_"+course.getId()));
				}
				recordList.add(valueMap);
			}
		}
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
			String errorMsg=templateValidate(datas, getRowTitleList());
			if(StringUtils.isBlank(errorMsg)){
				String[] data=datas.get(0);
				String[] courseNames=getRowTitleList().toArray(new String[0]);
				boolean equals = Arrays.equals(data, courseNames);
				if (!equals) {
					errorMsg="列名顺序不符合模板要求";
				}
			}
			return errorMsg;
			
		}catch (Exception e) {
			e.printStackTrace();
			return "上传文件不符合模板要求";
		}
	}
	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath,String params) {
		logger.info("业务数据处理中......");
		List<String[]> datas = ExcelUtils.readExcel(filePath,getRowTitleList().size());
		System.out.println(datas.size());
		// 业务处理模块　具体的业务在这里处理
		//自己的处理接口　数据范围格式如下
		//业务类中返回给我的一个json格式的数据
		Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()),Semester.class);
		
		String jsonMsg =compreScoreService.doImport(getLoginInfo().getUnitId(), se.getAcadyear(), se.getSemester()+"", params, datas);
		logger.info("导入结束......");
		return jsonMsg;
	}*/
}
