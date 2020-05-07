package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;
import net.zdsoft.stuwork.data.service.DyClassstatWeekService;
import net.zdsoft.stuwork.data.service.DyPermissionService;

/**
 * @author yangsj 2017年11月28日下午4:50:59
 */
@Controller
@RequestMapping("/stuwork/check")
public class DyClassCheckRankingAction extends BaseAction {

	@Autowired
	private DyClassstatWeekService dyClassstatWeekService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private DyPermissionService dyPermissionService;

	@RequestMapping("/ranking/index/page")
	@ControllerInfo(value = "统计表admin")
	public String classStartIndex(HttpServletRequest request, ModelMap map) {
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
		});
		String acadyear = request.getParameter("acadyear");
		if (CollectionUtils.isEmpty(acadyearList)) {
			return errorFtl(map, "学年学期不存在");
		}

		// 查找当前学校的学段
		List<String> tis = new ArrayList<String>();
		String sections = schoolRemoteService.findSectionsById(unitId);
		if (StringUtils.isBlank(sections)) {
			return errorFtl(map, "请先维护该学校的学段");
		} else {
			String[] se = sections.split(",");
			tis = Arrays.asList(se);
		}
		map.put("tis", tis);
		map.put("acadyearList", acadyearList);
		map.put("semester", semester);
		map.put("acadyear", acadyear);
		return "/stuwork/classStat/checkRanking/rankIndex.ftl";
	}

	@RequestMapping("/ranking/list/page")
	@ControllerInfo(value = "统计列表")
	public String classStartList(String acadyear, String semester, String section, ModelMap map) {

		String unitId = getLoginInfo().getUnitId();
		// 根据学年，学段查出所有的班级
		List<Clazz> clazzs = SUtils.dt(
				classRemoteService.findByAllSchoolId(unitId),
				new TR<List<Clazz>>() {
				});
		// 过滤掉detailedId为空 的
		clazzs = EntityUtils.filter(clazzs, new EntityUtils.Filter<Clazz>() {
			@Override
			public boolean doFilter(Clazz clazz) {
				return clazz.getSection() != Integer.valueOf(section);
			}
		});
		if (CollectionUtils.isEmpty(clazzs)) {
			return errorFtl(map, "该学段下没有班级");
		}
		Set<String> classIdList = EntityUtils.getSet(clazzs, Clazz::getId);
		// 得到有权限的班级id
//		Set<String> classIds1 = dyPermissionService.findClassSetByUserId(getLoginInfo().getUserId());
//
//		// 取两个集合的交集
//		List<String> showClassIds = (List<String>) CollectionUtils.intersection(classIdList, classIds1);

		Map<String, Clazz> cMap = EntityUtils.getMap(clazzs, Clazz::getId);
		// 得到所有的gradeids
		List<String> gradeIds = EntityUtils.getList(clazzs, Clazz::getGradeId);
		removeNull(gradeIds);
		Map<String, String> gnMap = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(gradeIds) && gradeIds.size() > 0) {
			List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])),
					Grade.class);
			gnMap = EntityUtils.getMap(grades, Grade::getId, Grade::getGradeName);
		}
		// 得到班级的全名
		Map<String, String> classNameMap = new HashMap<String, String>();
		for (String classId : classIdList) {
			String className = gnMap.get(cMap.get(classId).getGradeId()) + cMap.get(classId).getClassName();
			classNameMap.put(classId, className);
		}
		String[] classIds = ArrayUtil.toArray(classIdList);
		List<DateInfo> dateInfos = SUtils.dt(
				dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, Integer.valueOf(semester)),
				new TR<List<DateInfo>>() {
				});
		if (CollectionUtils.isEmpty(dateInfos)) {
			return errorFtl(map, "请先维护该学年学期下的节假日信息");
		}
		int maxWeek = 0;
		for (DateInfo dateInfo : dateInfos) {
			if (dateInfo.getWeek() > maxWeek) {
				maxWeek = dateInfo.getWeek();
			}
		}
		map.put("maxWeek", maxWeek);
		Map<String, List<DyClassstatWeek>> weekMap1 = new LinkedHashMap<String, List<DyClassstatWeek>>();

		List<Integer> weeks = new ArrayList<Integer>();
		for (int i = 1; i <= maxWeek; i++) {
			weeks.add(i);
		}
		List<DyClassstatWeek> statWeekList1 = dyClassstatWeekService.findRankingList(getLoginInfo().getUnitId(),
				acadyear, semester, weeks.toArray(new Integer[weeks.size()]), classIds);
		if (CollectionUtils.isEmpty(statWeekList1)) {
			return errorFtl(map, "请先统计该学年学期下的教室考核");
		}
		for (DyClassstatWeek e : statWeekList1) {
			if(cMap.containsKey(e.getClassId())) {
				e.setClassCode(cMap.get(e.getClassId()).getClassCode());
			}
		}
		Collections.sort(statWeekList1,new Comparator<DyClassstatWeek>() {
			@Override
			public int compare(DyClassstatWeek o1, DyClassstatWeek o2) {
				return o1.getClassCode().compareTo(o2.getClassCode());
			}});
		Map<Integer, List<DyClassstatWeek>> statWeekMap = EntityUtils.getListMap(statWeekList1, DyClassstatWeek::getWeek, Function.identity());
		for (int i = 1; i <= maxWeek; i++) {
			weekMap1.put(String.valueOf(i), statWeekMap.containsKey(i)?statWeekMap.get(i):getListDCW(Lists.newArrayList(classIdList)));
		}
		Iterator<Entry<String, List<DyClassstatWeek>>> wit = weekMap1.entrySet().iterator();
		while(wit.hasNext()) {
			List<DyClassstatWeek> wks = wit.next().getValue();
			Collections.sort(wks, new Comparator<DyClassstatWeek>() {

				@Override
				public int compare(DyClassstatWeek a, DyClassstatWeek b) {
					Clazz o1 = cMap.get(a.getClassId());
					Clazz o2 = cMap.get(b.getClassId());
					if (o1.getSection() != o2.getSection()) {
						return o1.getSection() - o2.getSection();
					}
					if (!o1.getAcadyear().equals(o2.getAcadyear())) {
						return o2.getAcadyear().compareTo(o1.getAcadyear());
					}
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
			});
		}
		map.put("weekMap", weekMap1);
		map.put("classNameMap", classNameMap);

		return "/stuwork/classStat/checkRanking/rankList.ftl";
	}

	/**
	 * @param showClassIds
	 * @return
	 */
	private List<DyClassstatWeek> getListDCW(List<String> showClassIds) {
		// TODO Auto-generated method stub
		List<DyClassstatWeek> listDCW = new ArrayList<DyClassstatWeek>();
		for (String classId : showClassIds) {
			DyClassstatWeek dyClassstatWeek = new DyClassstatWeek();
			dyClassstatWeek.setClassId(classId);
			listDCW.add(dyClassstatWeek);
		}
		return listDCW;
	}

	/**
	 * @param 移除所有的null值
	 */
	private void removeNull(List<String> ids) {
		for (int i = 0; i < ids.size(); i++) {
			if (ids.get(i) == null) {
				ids.remove(ids.get(i));
				i--;
			}
		}
	}
}
