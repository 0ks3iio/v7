package net.zdsoft.qulity.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.qulity.data.dto.StuXxhzDto;
import net.zdsoft.stuwork.data.dto.DyCollectDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/quality/collect")
public class QualityCollectAction extends QualityCommonAction{

	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@RequestMapping("/tab/page")
	public String collectTab(ModelMap map) {
		return "/quality/collect/qualityCollectTab.ftl";
	}
	@RequestMapping("/index/page")
	public String collectIndex(ModelMap map,HttpServletRequest request) {
		String type=request.getParameter("type");
		String unitId=getLoginInfo().getUnitId();
		String userId=getLoginInfo().getUserId();
		
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		}
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
//        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){});
		List<Grade> gradeList =getGradeList(userId);
        map.put("gradeList", gradeList);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("unitId", unitId);
        map.put("userId", userId);
        map.put("type", type);
		return "/quality/collect/qualityCollectIndex.ftl";
	}
	
	@RequestMapping("/List1/page")
	@ControllerInfo("学习汇总")
	public String collectList1(ModelMap map,HttpServletRequest request) {
		String gradeId=request.getParameter("gradeId");
		String classId=request.getParameter("classId");
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		Set<String> classIdSet=getClassIds(classId, gradeId);
		if(CollectionUtils.isEmpty(classIdSet)){
			return "/quality/collect/qualityCollectList1.ftl";
		}
		Pagination page = createPagination();
		String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
		List<Clazz>	clazzList = SUtils.dt(classRemoteService.findListByIds(classIds),Clazz.class);
		List<Student> studentList = Student.dt(studentRemoteService.findByClassIds(classIds, SUtils.s(page)), page);
		if(CollectionUtils.isEmpty(studentList)){
			return "/quality/collect/qualityCollectList1.ftl";
		}
		
		String unitId = getLoginInfo().getUnitId();
		Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
		String[] studentIds = EntityUtils.getList(studentList, e->e.getId()).toArray(new String[0]);
		Map<String, String> xkjsMap = SUtils.dt(stuworkRemoteService
				.findXkjsByStudentIds(unitId, acadyear, semester, studentIds), new TR<Map<String, String>>(){});
		Map<String, Map<String, String[]>> xxhzMap = comStatisticsRemoteService
				.findXxhzByStudentIds(unitId, acadyear, semester, gradeId, studentIds);
		List<StuXxhzDto> dtoList = new ArrayList<StuXxhzDto>();
		StuXxhzDto dto = null;
		for (Student student : studentList) {
			dto = new StuXxhzDto();
			dto.setStudentName(student.getStudentName());
			dto.setClassName(classNameMap.get(student.getClassId()));
			Map<String, String[]> culturalMap = xxhzMap.get(student.getId());
			if(MapUtils.isNotEmpty(culturalMap)){
				String[] xkcj = culturalMap.get("1");
				String[] yybs = culturalMap.get("2");
				String[] yyks = culturalMap.get("3");
				String[] xk = culturalMap.get("4");
				if(ArrayUtils.isNotEmpty(xkcj)){
					dto.setXkcjScore(xkcj[0]);
					dto.setXkcjRanking(xkcj[1]);
					dto.setXkcjToScore(xkcj[2]);
				}
				if(ArrayUtils.isNotEmpty(yybs)){
					dto.setYybsScore(yybs[0]);
					dto.setYybsRanking(yybs[1]);
					dto.setYybsToScore(yybs[2]);
				}
				if(ArrayUtils.isNotEmpty(yyks)){
					dto.setYyksScore(yyks[0]);
					dto.setYyksRanking(yyks[1]);
					dto.setYyksToScore(yyks[2]);
				}
				if(ArrayUtils.isNotEmpty(xk)){
					dto.setXkToScore(xk[0]);
				}
			}
			dto.setXkjsToScore(xkjsMap.get(student.getId()));
			dtoList.add(dto);
		}
		map.put("dtoList", dtoList);
		sendPagination(request, map, page);
		return "/quality/collect/qualityCollectList1.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/List1/export")
	@ControllerInfo("学习汇总导出")
	public String exportList1(ModelMap map,HttpServletRequest request,HttpServletResponse response) {
		
		try {
			String gradeId=request.getParameter("gradeId");
			String classId=request.getParameter("classId");
			String acadyear=request.getParameter("acadyear");
			String semester=request.getParameter("semester");
			Set<String> classIdSet=getClassIds(classId, gradeId);
			
			Map<String, List<String>> fieldTitleMap = new HashMap<String, List<String>>();
			List<String> tis = new ArrayList<>();
			tis.add("序号");
			tis.add("姓名");
			tis.add("班级");
			tis.add("学科成绩总分");
			tis.add("年级排名");
			tis.add("学科成绩折分");
			tis.add("英语笔试成绩");
			tis.add("年级排名");
			tis.add("英语笔试折分");
			tis.add("英语口试");
			tis.add("年级排名");
			tis.add("口试折分");
			tis.add("学科竞赛折分");
			tis.add("学考折分");
			fieldTitleMap.put("学习汇总表", tis);
			
			Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String,String>>>();
			List<Map<String, String>> datas = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(classIdSet)){
				String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
				List<Clazz>	clazzList = SUtils.dt(classRemoteService.findListByIds(classIds),Clazz.class);
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds),Student.class);
				if(CollectionUtils.isNotEmpty(studentList)){
					Map<String, Clazz> clazzMap = EntityUtils.getMap(clazzList, Clazz::getId);
					sortStuList(studentList, clazzMap);
					String unitId = getLoginInfo().getUnitId();
					Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
					String[] studentIds = EntityUtils.getList(studentList, e->e.getId()).toArray(new String[0]);
					Map<String, String> xkjsMap = SUtils.dt(stuworkRemoteService
							.findXkjsByStudentIds(unitId, acadyear, semester, studentIds), new TR<Map<String, String>>(){});
					Map<String, Map<String, String[]>> xxhzMap = comStatisticsRemoteService
							.findXxhzByStudentIds(unitId, acadyear, semester, gradeId, studentIds);
					int i = 1;
					for (Student student : studentList) {
						Map<String, String> conMap = new HashMap<>();
						conMap.put("序号", ""+i++);
						conMap.put("姓名", student.getStudentName());
						conMap.put("班级", classNameMap.get(student.getClassId()));
						Map<String, String[]> culturalMap = xxhzMap.get(student.getId());
						if(MapUtils.isNotEmpty(culturalMap)){
							String[] xkcj = culturalMap.get("1");
							String[] yybs = culturalMap.get("2");
							String[] yyks = culturalMap.get("3");
							String[] xk = culturalMap.get("4");
							if(ArrayUtils.isNotEmpty(xkcj)){
								conMap.put("学科成绩总分", xkcj[0]);
								conMap.put("年级排名", xkcj[1]);
								conMap.put("学科成绩折分", xkcj[2]);
							}
							if(ArrayUtils.isNotEmpty(yybs)){
								conMap.put("英语笔试成绩", yybs[0]);
								conMap.put("年级排名", yybs[1]);
								conMap.put("英语笔试折分", yybs[2]);
							}
							if(ArrayUtils.isNotEmpty(yyks)){
								conMap.put("英语口试", yyks[0]);
								conMap.put("年级排名", yyks[1]);
								conMap.put("口试折分", yyks[2]);
							}
							if(ArrayUtils.isNotEmpty(xk)){
								conMap.put("学考折分", xk[0]);
							}
						}
						conMap.put("学科竞赛折分", xkjsMap.get(student.getId()));
						datas.add(conMap);
					}
				}
			}
			sheetName2RecordListMap.put("学习汇总表", datas);
			
			ExportUtils exportUtils = ExportUtils.newInstance();
			exportUtils.exportXLSFile("学习汇总表", fieldTitleMap, sheetName2RecordListMap, response);
		} catch (Exception e) {
			e.printStackTrace();
			return error("导出失败");
		}
		return success("导出完成");
	}
	@RequestMapping("/List2/page")
	public String collectList2(ModelMap map,HttpServletRequest request) {
		String gradeId=request.getParameter("gradeId");
		String classId=request.getParameter("classId");
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		String unitId=getLoginInfo().getUnitId();
		Pagination page = createPagination();
		//获取班级id 以此来获取 学生数据
		Set<String> classIds=getClassIds(classId, gradeId);
		if(CollectionUtils.isNotEmpty(classIds)){
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
			Map<String,String> classNameMap=EntityUtils.getMap(clazzs, Clazz::getId, Clazz::getClassNameDynamic);
			//		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
			List<Student> stuList=Student.dt(studentRemoteService.findByClassIds(
					classIds.toArray(new String[0]), SUtils.s(page)), page);
			Set<String> stuIds=EntityUtils.getSet(stuList, Student::getId);
			
			//学生的详情
			List<DyCollectDto>  dtoList=SUtils.dt(stuworkRemoteService.findCollectList2(unitId, acadyear, semester, stuIds.toArray(new String[0]))
					,new TR<List<DyCollectDto>>(){});
			Map<String,DyCollectDto>  dtoMap=EntityUtils.getMap(dtoList, DyCollectDto::getStudentId);
			
			map.put("dtoMap",dtoMap);
			map.put("stuList",stuList);
			map.put("classNameMap",classNameMap);
		}
		sendPagination(request, map, page);
		return "/quality/collect/qualityCollectList2.ftl";
	}
	@ResponseBody
	@RequestMapping("/List2/export")
	@ControllerInfo("德育汇总导出")
	public String exportList2(ModelMap map,HttpServletRequest request,HttpServletResponse response) {
		try {
			String gradeId=request.getParameter("gradeId");
			String classId=request.getParameter("classId");
			String acadyear=request.getParameter("acadyear");
			String semester=request.getParameter("semester");
			Set<String> classIdSet=getClassIds(classId, gradeId);
			
			Map<String, List<String>> fieldTitleMap = new HashMap<String, List<String>>();
			List<String> tis = new ArrayList<>();
			tis.add("序号");
			tis.add("姓名");
			tis.add("班级");
			tis.add("品德表现等级");
			tis.add("品德表现折分");
			tis.add("学生干部折分");
			tis.add("社团折分");
			tis.add("社会实践折分");
			tis.add("其它奖励折分");
			tis.add("处罚折分");
			fieldTitleMap.put("德育汇总表", tis);
			
			Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String,String>>>();
			List<Map<String, String>> datas = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(classIdSet)){
				String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds),Student.class);
				if(CollectionUtils.isNotEmpty(studentList)){
					List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classIds), new TR<List<Clazz>>(){});
					Map<String,Clazz> clazzMap=EntityUtils.getMap(clazzs, Clazz::getId);
					sortStuList(studentList, clazzMap);
					//		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
					Set<String> stuIds=EntityUtils.getSet(studentList, Student::getId);
					//学生的详情
					List<DyCollectDto>  dtoList=SUtils.dt(stuworkRemoteService.findCollectList2(getLoginInfo().getUnitId(), acadyear, semester, stuIds.toArray(new String[0]))
							,new TR<List<DyCollectDto>>(){});
					Map<String,DyCollectDto>  dtoMap=EntityUtils.getMap(dtoList, DyCollectDto::getStudentId);
					DyCollectDto dto=null;
					if(CollectionUtils.isNotEmpty(studentList)){
						int i = 1;
						for (Student student : studentList) {
							Map<String, String> conMap = new HashMap<>();
							conMap.put("序号", ""+i++);
							conMap.put("姓名", student.getStudentName());
							conMap.put("班级", student.getClassName());
							dto= dtoMap.get(student.getId());
							if(dto!=null){
								conMap.put("品德表现等级",dto.getEvatr());
								conMap.put("品德表现折分",dto.getEvaScore()+"");
								conMap.put("学生干部折分",dto.getStuPerScore()+"");
								conMap.put("社团折分",dto.getGroupScore()+"");
								conMap.put("社会实践折分",dto.getTryScore()+"");
								conMap.put("其它奖励折分",dto.getOtherScore()+"");
								conMap.put("处罚折分",dto.getPunishScore()+"");
							}
							datas.add(conMap);
						}
					}
				}
			}
			sheetName2RecordListMap.put("德育汇总表", datas);
			
			ExportUtils exportUtils = ExportUtils.newInstance();
			exportUtils.exportXLSFile("德育汇总表", fieldTitleMap, sheetName2RecordListMap, response);
		} catch (Exception e) {
			e.printStackTrace();
			return error("导出失败");
		}
		return success("导出完成");
	}

	@RequestMapping("/List3/page")
	@ControllerInfo("体育汇总")
	public String collectList3(ModelMap map,HttpServletRequest request) {
		String gradeId=request.getParameter("gradeId");
		String classId=request.getParameter("classId");
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		Set<String> classIdSet=getClassIds(classId, gradeId);
		if(CollectionUtils.isEmpty(classIdSet)){
			return "/quality/collect/qualityCollectList1.ftl";
		}
		Pagination page = createPagination();
		String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
		List<Clazz>	clazzList = SUtils.dt(classRemoteService.findListByIds(classIds),Clazz.class);
		List<Student> studentList = Student.dt(studentRemoteService.findByClassIds(classIds, SUtils.s(page)), page);
		if(CollectionUtils.isEmpty(studentList)){
			return "/quality/collect/qualityCollectList1.ftl";
		}
		
		String unitId = getLoginInfo().getUnitId();
		Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
		String[] studentIds = EntityUtils.getList(studentList, e->e.getId()).toArray(new String[0]);
		Map<String, String[]> tyhzMap = comStatisticsRemoteService
				.findTyhzByStudentIds(unitId, acadyear, semester, gradeId, studentIds);
		List<StuXxhzDto> dtoList = new ArrayList<StuXxhzDto>();
		StuXxhzDto dto = null;
		for (Student student : studentList) {
			dto = new StuXxhzDto();
			dto.setStudentName(student.getStudentName());
			dto.setClassName(classNameMap.get(student.getClassId()));
			String[] tyhz = tyhzMap.get(student.getId());
			if(ArrayUtils.isNotEmpty(tyhz)){
				dto.setTycjScore(tyhz[0]);
				dto.setTycjToScore(tyhz[1]);
			}
			dtoList.add(dto);
		}
		map.put("dtoList", dtoList);
		sendPagination(request, map, page);
		return "/quality/collect/qualityCollectList3.ftl";
	}
	@ResponseBody
	@RequestMapping("/List3/export")
	@ControllerInfo("体育汇总导出")
	public String exportList3(ModelMap map,HttpServletRequest request,HttpServletResponse response) {
		
		try {
			String gradeId=request.getParameter("gradeId");
			String classId=request.getParameter("classId");
			String acadyear=request.getParameter("acadyear");
			String semester=request.getParameter("semester");
			Set<String> classIdSet=getClassIds(classId, gradeId);
			
			Map<String, List<String>> fieldTitleMap = new HashMap<String, List<String>>();
			List<String> tis = new ArrayList<>();
			tis.add("序号");
			tis.add("姓名");
			tis.add("班级");
			tis.add("体育成绩");
			tis.add("体育成绩折分");
			fieldTitleMap.put("体育汇总表", tis);
			
			Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String,String>>>();
			List<Map<String, String>> datas = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(classIdSet)){
				String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
				List<Clazz>	clazzList = SUtils.dt(classRemoteService.findListByIds(classIds),Clazz.class);
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds),Student.class);
				if(CollectionUtils.isNotEmpty(studentList)){
					Map<String, Clazz> clazzMap = EntityUtils.getMap(clazzList, Clazz::getId);
					sortStuList(studentList, clazzMap);
					String unitId = getLoginInfo().getUnitId();
					Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
					String[] studentIds = EntityUtils.getList(studentList, e->e.getId()).toArray(new String[0]);
					Map<String, String[]> tyhzMap = comStatisticsRemoteService
							.findTyhzByStudentIds(unitId, acadyear, semester, gradeId, studentIds);
					int i = 1;
					for (Student student : studentList) {
						Map<String, String> conMap = new HashMap<>();
						conMap.put("序号", ""+i++);
						conMap.put("姓名", student.getStudentName());
						conMap.put("班级", classNameMap.get(student.getClassId()));
						String[] tyhz = tyhzMap.get(student.getId());
						if(ArrayUtils.isNotEmpty(tyhz)){
							conMap.put("体育成绩", tyhz[0]);
							conMap.put("体育成绩折分", tyhz[1]);
						}
						datas.add(conMap);
					}
				}
			}
			sheetName2RecordListMap.put("体育汇总表", datas);
			
			ExportUtils exportUtils = ExportUtils.newInstance();
			exportUtils.exportXLSFile("体育汇总表", fieldTitleMap, sheetName2RecordListMap, response);
		} catch (Exception e) {
			e.printStackTrace();
			return error("导出失败");
		}
		return success("导出完成");
	}
	@RequestMapping("/List4/page")
	public String collectList4(ModelMap map,HttpServletRequest request) {
		String gradeId=request.getParameter("gradeId");
		String classId=request.getParameter("classId");
		String acadyear=request.getParameter("acadyear");
		String semester=request.getParameter("semester");
		String unitId=getLoginInfo().getUnitId();
		Pagination page = createPagination();
		//获取班级id 以此来获取 学生数据
		Set<String> classIds=getClassIds(classId, gradeId);
		if(CollectionUtils.isNotEmpty(classIds)){
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
			Map<String,String> classNameMap=EntityUtils.getMap(clazzs, Clazz::getId, Clazz::getClassNameDynamic);
			//		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
			List<Student> stuList=Student.dt(studentRemoteService.findByClassIds(
					classIds.toArray(new String[0]), SUtils.s(page)), page);
			Set<String> stuIds=EntityUtils.getSet(stuList, Student::getId);
			//学生的详情
			List<DyCollectDto>  dtoList=SUtils.dt(stuworkRemoteService.findCollectList4(unitId, acadyear, semester, stuIds.toArray(new String[0]))
					,new TR<List<DyCollectDto>>(){});
			Map<String,DyCollectDto>  dtoMap=EntityUtils.getMap(dtoList, DyCollectDto::getStudentId);
			map.put("dtoMap",dtoMap);
			map.put("stuList",stuList);
			map.put("classNameMap",classNameMap);
			sendPagination(request, map, page);
		}
		return "/quality/collect/qualityCollectList4.ftl";
	}
	@ResponseBody
	@RequestMapping("/List4/export")
	@ControllerInfo("节日汇总导出")
	public String exportList4(ModelMap map,HttpServletRequest request,HttpServletResponse response) {
		try {
			String gradeId=request.getParameter("gradeId");
			String classId=request.getParameter("classId");
			String acadyear=request.getParameter("acadyear");
			String semester=request.getParameter("semester");
			Set<String> classIdSet=getClassIds(classId, gradeId);
			
			Map<String, List<String>> fieldTitleMap = new HashMap<String, List<String>>();
			List<String> tis = new ArrayList<>();
			tis.add("序号");
			tis.add("姓名");
			tis.add("班级");
			tis.add("艺术节个人折分");
			tis.add("艺术节团体折分");
			tis.add("体育节个人折分");
			tis.add("体育节团体折分");
			tis.add("外语节个人折分");
			tis.add("外语节团体折分");
			tis.add("科技节个人折分");
			tis.add("科技节团体折分");
			tis.add("文化节个人折分");
			tis.add("文化节团体折分");
			fieldTitleMap.put("节日汇总表", tis);
			
			Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String,String>>>();
			List<Map<String, String>> datas = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(classIdSet)){
				String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
				List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds),Student.class);
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classIds), new TR<List<Clazz>>(){});
				Map<String,Clazz> clazzMap=EntityUtils.getMap(clazzs, Clazz::getId);
				sortStuList(studentList, clazzMap);
				//		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),new TR<List<Student>>(){});
				Set<String> stuIds=EntityUtils.getSet(studentList, Student::getId);
				//学生的详情
				List<DyCollectDto>  dtoList=SUtils.dt(stuworkRemoteService.findCollectList4(getLoginInfo().getUnitId(), acadyear, semester, stuIds.toArray(new String[0]))
						,new TR<List<DyCollectDto>>(){});
				Map<String,DyCollectDto>  dtoMap=EntityUtils.getMap(dtoList, DyCollectDto::getStudentId);
				DyCollectDto dto=null;
				if(CollectionUtils.isNotEmpty(studentList)){
					int i = 1;
					for (Student student : studentList) {
						Map<String, String> conMap = new HashMap<>();
						conMap.put("序号", ""+i++);
						conMap.put("姓名", student.getStudentName());
						conMap.put("班级", student.getClassName());
						dto= dtoMap.get(student.getId());
						if(dto!=null){
							conMap.put("艺术节个人折分",dto.getyPerScore()+"");
							conMap.put("艺术节团体折分",dto.getyGroupScore()+"");
							conMap.put("体育节个人折分",dto.gettPerScore()+"");
							conMap.put("体育节团体折分",dto.gettGroupScore()+"");
							conMap.put("外语节个人折分",dto.getwPerScore()+"");
							conMap.put("外语节团体折分",dto.getwGroupScore()+"");
							conMap.put("科技节个人折分",dto.getkPerScore()+"");
							conMap.put("科技节团体折分",dto.getkGroupScore()+"");
							conMap.put("文化节个人折分",dto.getWenPerScore()+"");
							conMap.put("文化节团体折分",dto.getWenGroupScore()+"");
						}
						datas.add(conMap);
					}
				}
			}
			sheetName2RecordListMap.put("节日汇总表", datas);
			
			ExportUtils exportUtils = ExportUtils.newInstance();
			exportUtils.exportXLSFile("节日汇总表", fieldTitleMap, sheetName2RecordListMap, response);
		} catch (Exception e) {
			e.printStackTrace();
			return error("导出失败");
		}
		return success("导出完成");
	}

}
