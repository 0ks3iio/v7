package net.zdsoft.basedata.action.div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.popup.BaseDivAction;
import net.zdsoft.framework.utils.PinyinUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.enums.unit.SectionsTypeEnum;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/common/div/student")
public class StudentDivAction extends BaseDivAction {
	Logger logger = Logger.getLogger(StudentDivAction.class);

	private static String business_key = "student-popup-";
	private static String student_recent_key = "student-popup-recent-";

	@Autowired
	SchoolService schoolService;

	@Autowired
	SemesterService semesterService;

	@Autowired
	GradeService gradeService;

	@Autowired
	ClassService classService;

	@Autowired
	StudentService studentService;

	/**
	 * 学生选择的结构如下 学段－－>年级－－>班级－－>学生 id 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索学生的时候　可以看到班级
	 * 全拼 模糊搜索用 首字母拼音　模糊搜索用 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data
	 * 历史搜索data-history code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
	 */
	@RequestMapping("/popupData")
	@ResponseBody
	public String showPopUpData() {
		String dataJson = RedisUtils.get(business_key
				+ getLoginInfo().getUnitId());
		if (dataJson == null) {
			List<String[]> dataList = new LinkedList<String[]>();
			// 学段
			String sections = schoolService.findSectionsById(getLoginInfo()
					.getUnitId());
			if (StringUtils.isBlank(sections)) {
				logger.debug("学段不存在");
				return JsonArray.toJSON(dataList).toString();
			}
			String[] sectionArray = sections.split(",");

			// 当前学年学期
			//Semester currSemester = semesterService.getCurrentSemester(0);
			String unitId = getLoginInfo().getUnitId();
			Semester currSemester = semesterService.findCurrentSemester(1, unitId);
			if (currSemester == null) {
				logger.debug("当前学年学期不存在");
				return JsonArray.toJSON(dataList).toString();
			}
			List<Grade> gradeList = gradeService
					.findByUnitIdAndCurrentAcadyear(getLoginInfo().getUnitId(),
							currSemester.getAcadyear(), null, true);

			int startCode = 10000;
			int level = 1;
			// -----------------------------如果只有一个学段的话　直接从年级开始------------------------
			if (sectionArray.length > 1) {
				// ---------------------------------学段--------------------------------------
				for (int i = 0; i < sectionArray.length; i++) {
					startCode++;
					String[] data = new String[9];
					data[0] = sectionArray[i];
					data[1] = SectionsTypeEnum.getName(Integer.valueOf(
							sectionArray[i]).intValue());
					data[2] = "";
					data[3] = PinyinUtils.toHanyuPinyin(data[1], false);
					data[4] = PinyinUtils.toHanyuPinyin(data[1], true);
					data[5] = TYPE_TOP;
					data[6] = Constant.GUID_ZERO;
					data[7] = String.valueOf(level);
					data[8] = String.valueOf(startCode);
					codeMap.put(business_key + sectionArray[i],
							String.valueOf(startCode));
					dataList.add(data);
				}
				level++;
				// -------------------------------年级---------------------------------
				startCode = 10000;
				for (Grade grade : gradeList) {
					startCode++;
					String[] data = new String[9];
					data[0] = grade.getId();
					data[1] = grade.getGradeName();
					data[2] = "";
					data[3] = PinyinUtils.toHanyuPinyin(grade.getGradeName(),
							false);
					data[4] = PinyinUtils.toHanyuPinyin(grade.getGradeName(),
							true);
					data[5] = TYPE_SUB;
					data[6] = grade.getSection().toString();
					data[7] = String.valueOf(level);
					String businessCode = codeMap.get(business_key
							+ grade.getSection())
							+ String.valueOf(startCode);
					codeMap.put(business_key + grade.getId(), businessCode);
					data[8] = String.valueOf(businessCode);
					dataList.add(data);
				}
				level++;
			} else {
				// -------------------------年级做为第一级---------------------------------
				for (Grade grade : gradeList) {
					startCode++;
					String[] data = new String[9];
					data[0] = grade.getId();
					data[1] = grade.getGradeName();
					data[2] = "";
					data[3] = PinyinUtils.toHanyuPinyin(grade.getGradeName(),
							false);
					data[4] = PinyinUtils.toHanyuPinyin(grade.getGradeName(),
							true);
					data[5] = TYPE_TOP;
					data[6] = Constant.GUID_ZERO;
					data[7] = String.valueOf(level);
					data[8] = String.valueOf(startCode);
					codeMap.put(business_key + grade.getId(),
							String.valueOf(startCode));
					dataList.add(data);
				}
				level++;
			}
			// －－－－－－－－－－－－－－－－－－－班级－－－－－－－－－－－－－－－－－－－－－－－－－
			startCode = 10000;
			List<Clazz> classList = classService.findByIdCurAcadyear(
					getLoginInfo().getUnitId(), currSemester.getAcadyear());
			Set<String> classIds = new HashSet<String>();
			Map<String, String> classNameMap = new HashMap<String, String>();
			startCode = 10000;
			for (Clazz clazz : classList) {
				startCode++;
				String[] data = new String[9];
				data[0] = clazz.getId();
				data[1] = clazz.getClassNameDynamic();
				data[2] = "";
				data[3] = PinyinUtils.toHanyuPinyin(
						clazz.getClassNameDynamic(), false);
				data[4] = PinyinUtils.toHanyuPinyin(
						clazz.getClassNameDynamic(), true);
				data[5] = TYPE_SUB;
				data[6] = clazz.getGradeId();
				data[7] = String.valueOf(level);
				String businessCode = codeMap.get(business_key
						+ clazz.getGradeId())
						+ String.valueOf(startCode);
				codeMap.put(business_key + clazz.getId(), businessCode);
				data[8] = businessCode;
				classIds.add(clazz.getId());
				classNameMap.put(clazz.getId(), clazz.getClassNameDynamic());
				dataList.add(data);
			}
			level++;
			// －－－－－－－－－－－－－－－－－－－学生－－－－－－－－－－－－－－－－－－－－－－－－－
			startCode = 10000;
			List<Student> studentList = studentService.findByClassIds(classIds
					.toArray(new String[0]));
			for (Student stu : studentList) {
				startCode++;
				String[] data = new String[9];
				data[0] = stu.getId();
				data[1] = stu.getStudentName();
				data[2] = classNameMap.get(stu.getClassId());
				data[3] = PinyinUtils
						.toHanyuPinyin(stu.getStudentName(), false);
				data[4] = PinyinUtils.toHanyuPinyin(stu.getStudentName(), true);
				data[5] = TYPE_DATA;
				data[6] = stu.getClassId();
				data[7] = String.valueOf(level);
				String businessCode = codeMap.get(business_key
						+ stu.getClassId())
						+ String.valueOf(startCode);
				codeMap.put(business_key + stu.getId(), businessCode);
				data[8] = businessCode;
				dataList.add(data);
			}
			RedisUtils.set(business_key + getLoginInfo().getUnitId(), JsonArray
					.toJSON(dataList).toString(), 30 * 60);
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> recentDataList = RedisUtils
				.queryDataFromList(student_recent_key
						+ getLoginInfo().getUserId(),true);
		if (CollectionUtils.isEmpty(recentDataList))
			recentDataList = new ArrayList<String>();
		String recentDataJson = JsonArray.toJSON(recentDataList).toString();
		List<String> resultList = new LinkedList<String>();
		resultList.add(dataJson);
		resultList.add(recentDataJson);
		return JsonArray.toJSON(resultList).toString();
	}

	/**
	 * 按照班级显示学生 名称 搜索显示tip　在搜索的时候提示的一些信息　比如搜索学生的时候　可以看到班级 全拼 模糊搜索用 首字母拼音　模糊搜索用
	 * 上一级id 第一级目录的上一级id为32个0 类型　 类型　第一级是top 下级的sub 数据data 历史搜索data-history
	 * code　100001000010000 从上往下有关联关系 level 第几层　第一级为1 第二级为2...
	 * String[]{"id","名称","搜索显示tip","全拼","首字母拼音","类型","上一级id","code","level"}
	 */
	@RequestMapping("/popupData/class")
	@ResponseBody
	public String showPopUpDataByClassId(String classId) {
		String dataJson = null;
		/*String dataJson = RedisUtils.get(business_key + "find_by_class-"
				+ getLoginInfo().getUnitId());*/
		if (dataJson == null) {
			List<String[]> dataList = new LinkedList<String[]>();

			// －－－－－－－－－－－－－－－－－－－学生－－－－－－－－－－－－－－－－－－－－－－－－－
			int startCode = 10000;
			int level = 1;
			List<Student> studentList = studentService.findByClassIds(classId);
			for (Student stu : studentList) {
				startCode++;
				String[] data = new String[9];
				data[0] = stu.getId();
				data[1] = stu.getStudentName();
				data[2] = "";
				data[3] = PinyinUtils
						.toHanyuPinyin(stu.getStudentName(), false);
				data[4] = PinyinUtils.toHanyuPinyin(stu.getStudentName(), true);
				data[5] = TYPE_DATA;
				data[6] = stu.getClassId();
				data[7] = String.valueOf(level);
				data[8] = String.valueOf(startCode);
				dataList.add(data);
			}
			RedisUtils.set(business_key + "find_by_class-"
					+ getLoginInfo().getUnitId(), JsonArray.toJSON(dataList)
					.toString(), 30 * 60);
			dataJson = JsonArray.toJSON(dataList).toString();
		}
		List<String> resultList = new LinkedList<String>();
		resultList.add(dataJson);

		return JsonArray.toJSON(resultList).toString();
	}

	@RequestMapping("/recentData")
	@ResponseBody
	@ControllerInfo(value = "获取最近的数据", parameter = "{ids}")
	public String putRecentPopUpData(String ids) {
		String[] teacherIds = ids.split(",");
		for (int i = 0; i < teacherIds.length; i++) {
			RedisUtils.addDataToList(student_recent_key
					+ getLoginInfo().getUserId(), teacherIds[i], MAX_COUNT);
		}
		return "";
	}
}
