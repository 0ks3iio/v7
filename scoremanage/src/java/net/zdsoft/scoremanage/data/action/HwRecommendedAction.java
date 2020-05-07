package net.zdsoft.scoremanage.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 保送生
 */
@Controller
@RequestMapping("/scoremanage/recommended")
public class HwRecommendedAction extends BaseAction{
	private static final Logger logger = LoggerFactory
			.getLogger(HwRecommendedAction.class);

	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private HwStatisService hwStatisService;



	@RequestMapping("/index/page")
	public String index(ModelMap modelMap){
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolIdAndIsGraduate(getLoginInfo().getUnitId(),Grade.NOT_GRADUATED), Grade.class);
		if(CollectionUtils.isNotEmpty(gradeList)){
			List<Json> gradeJson = getGradeJson(gradeList, Student.STUDENT_NORMAL);
			modelMap.put("gradeList",gradeJson);
		}
		return "/scoremanage/recommended/recommendedIndex.ftl";
	}

	/**
	 * 毕业: 1  没毕业: 0
	 * @param type
	 * @return
	 */
	@GetMapping("/getGradeList")
	@ResponseBody
	public String getGradeList(Integer type){
		if(!Grade.GRADUATED.equals(type) && !Grade.NOT_GRADUATED.equals(type)){
			return error("参数错误");
		}
		Json result = new Json();
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolIdAndIsGraduate(getLoginInfo().getUnitId(),type), Grade.class);
		if(CollectionUtils.isEmpty(gradeList)){
			result.put("code","-1");
			return result.toJSONString();
		}
		List<Json> grades = getGradeJson(gradeList,type);
		result.put("code","00");
		result.put("gradeList",grades);
		return result.toJSONString();
	}

	private List<Json> getGradeJson(List<Grade> gradeList,Integer type) {
		return gradeList.stream().sorted((x,y)->{
			try {
				return x.getGradeCode().compareTo(y.getGradeCode());
			} catch (Exception e) {
				return 1;
			}
		}).map(x -> {
			Json json = new Json();
			json.put("gradeId", x.getId());
			String name = "";
			if (type ==Grade.GRADUATED) {
				name=String.valueOf(Integer.parseInt(x.getOpenAcadyear().substring(0, 4))+x.getSchoolingLength());
				if(x.getSection()==BaseConstants.SECTION_HIGH_SCHOOL){
					name+="届(高中)";
				}else if(x.getSection()==BaseConstants.SECTION_JUNIOR){
					name+="届(初中)";
				}else if(x.getSection()==BaseConstants.SECTION_PRIMARY){
					name+="届(小学)";
				}else if(x.getSection()==BaseConstants.SECTION_COLLEGE){
					name+="届(剑桥高中)";
				}
			} else {
				name = x.getGradeName();
			}
			json.put("name", name);
			return json;
		}).collect(Collectors.toList());
	}

	@RequestMapping("/stuTree")
	public String stuTree(String callback,String gradeId,ModelMap modelMap){
		modelMap.put("callback",callback);
		modelMap.put("gradeId",gradeId);

		return "/scoremanage/recommended/stuTree.ftl";
	}

	/**
	 *
	 * @param gradeId
	 * @return
	 */
	@RequestMapping("/stuTreeJson")
	@ResponseBody
	public String stuTreeJson(String gradeId){
		Json result = new Json();
		List<Student> stuList=SUtils.dt(studentRemoteService.findAllStudentByGradeId(gradeId),Student.class);
		//封装成ztree
		if(CollectionUtils.isEmpty(stuList)){
			result.put("code","-1");
			return result.toJSONString();
		}
		List<Clazz> classList = SUtils.dt(classRemoteService.findListByIn("id",EntityUtils.getSet(stuList, x -> x.getClassId()).toArray(new String[0])), Clazz.class);
		classList.sort((x,y)->{
			if (StringUtils.isBlank(x.getClassCode())){
				return -1;
			}
			if (StringUtils.isBlank(y.getClassCode())){
				return 1;
			}
			return x.getClassCode().compareTo(y.getClassCode());
		});
		result.put("code","00");
		result.put("nodeList",getNodeList(stuList, classList));
		return Json.toJSONString(result);
	}

	private List<Json> getNodeList(List<Student> stuList, List<Clazz> classList) {
		Stream<Json> stuStream = stuList.stream().sorted((x, y) -> {
			if (x.getStudentCode() == null) {
				return -1;
			}
			if (y.getStudentCode() == null) {
				return 1;
			}
			return x.getStudentCode().compareTo(y.getStudentCode());
		}).map(x -> getOneTreeNode(x.getId(), x.getClassId(), x.getStudentName(), false));

		Stream<Json> classStream = classList.stream().map(x -> getOneTreeNode(x.getId(), "", x.getClassName(), true));

		return Stream.concat(stuStream,classStream).collect(Collectors.toList());

	}

	private Json getOneTreeNode(String id, String s, String className, boolean isParent) {
		Json json = new Json();
		json.put("id", id);
		json.put("pId", s);
		json.put("name", className);
		json.put("isParent", isParent);
		return json;
	}

	/**
	 * 获取一个学生的报表信息
	 * @param modelMap
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/stuReport")
	public String stuReport(ModelMap modelMap,String studentId){
		Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
		Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
		modelMap.put("name",student.getStudentName());
		modelMap.put("className",clazz.getClassNameDynamic());
		if (student.getSex()!=null){
			modelMap.put("sex",student.getSex()==User.USER_WOMAN_SEX ?"女":"男");
		}
		modelMap.put("stuCode",student.getStudentCode());

		List<List<String>> infoList = hwStatisService.getReportByStudentId(getLoginInfo().getUnitId(), studentId);

		if(CollectionUtils.isNotEmpty(infoList)){
			modelMap.put("schName",infoList.get(0).get(1));
			modelMap.put("sectionName",infoList.get(0).get(2));
			List<String> titleList = infoList.get(2);
			infoList=infoList.subList(3,infoList.size()-2);
			modelMap.put("infoList",infoList);
			modelMap.put("titleList",titleList);
		}
		return "/scoremanage/recommended/stuReport.ftl";
	}


	/**
	 * 统计年级的数据
	 * @param gradeId
	 * @return
	 */
	@RequestMapping("/statistic")
	@ResponseBody
	public String statistic(String gradeId){
		if(StringUtils.isBlank(gradeId)){
			return error("gradeId不能为空!");
		}
		try {
			hwStatisService.saveStatisticGrade(getLoginInfo().getUnitId(),gradeId);
		} catch (Exception e) {
			logger.error("",e);
			return error(e.getMessage());
		}
		return success("统计成功!");
	}


}
