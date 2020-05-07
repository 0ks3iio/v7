package net.zdsoft.comprehensive.action;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dto.CompreScoreMapDto;
import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.comprehensive.entity.CompreSetup;
import net.zdsoft.comprehensive.service.CompreInfoService;
import net.zdsoft.comprehensive.service.CompreScoreService;
import net.zdsoft.comprehensive.service.CompreSetupService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 总评成绩管理
 * @author duhc
 *
 */
@Controller
@RequestMapping("/comprehensive/score")
public class CompreSubGradeAction extends BaseAction{

	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private CompreInfoService compreInfoService;
	@Autowired
	private CompreScoreService compreScoreService;
	@Autowired
	private CompreSetupService compreSetupService;

    @RequestMapping("/index/page")
    public String index() {
        return "/comprehensive/score/totalScoreIndex.ftl";
    }

    @RequestMapping("/filter/page")
    public String gradeIndex(String from, ModelMap map) {
        map.put("from", from);
        List<String> gradeCode = new ArrayList<>();
        gradeCode.add(CompreStatisticsConstants.THIRD_LOWER);
        gradeCode.add(CompreStatisticsConstants.SENIOR_ONE_LOWER);
        gradeCode.add(CompreStatisticsConstants.SENIOR_ONE_UPPER);
        gradeCode.add(CompreStatisticsConstants.SENIOR_TWO_LOWER);
        gradeCode.add(CompreStatisticsConstants.SENIOR_TWO_UPPER);
        gradeCode.add(CompreStatisticsConstants.THIRD_UPPER);
        map.put("gradeCode", gradeCode);
        List<String> gradeName = new ArrayList<>();
        gradeName.add("初三下");
        gradeName.add("高一上");
        gradeName.add("高一下");
        gradeName.add("高二上");
        gradeName.add("高二下");
        gradeName.add("高三上");
        map.put("gradeName", gradeName);
        String unitId = getLoginInfo().getUnitId();
        map.put("unitId", unitId);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>(){});
        Iterator<Grade> iterator = gradeList.iterator();
        while (iterator.hasNext()) {
            Grade grade = iterator.next();
            if (grade.getSection() != 3 && grade.getSection() != 9) {
                iterator.remove();
            }
        }
        map.put("gradeList", gradeList);
        return "/comprehensive/score/totalScoreFilter.ftl";
    }

    @RequestMapping("/model/list")
    public String subModelList(HttpServletRequest request, String unitId, String gradeCode, String gradeId, String type, ModelMap map) {
        Pagination page=createPagination();
        // 检测是否已经统计
        boolean isCounted = false;
        map.put("type", type);
        CompreInfo compreInfo = compreInfoService.findOneByGradeIdAndGradeCode(unitId, gradeId, gradeCode);
        if (compreInfo != null && compreInfo.getStateArea() != null) {
            if (compreInfo.getStateArea().indexOf(type) != -1) {
                isCounted = true;
            }
        }
        List<Course> courseList;
        if (CompreStatisticsConstants.TYPE_OVERALL.equals(type)) {
            courseList = new ArrayList<>();
            if (compreInfo != null) {
                List<CompreSetup> setupList = compreSetupService.findByUnitIdAndInfoIdAndType(unitId, compreInfo.getId(), CompreStatisticsConstants.TYPE_OVERALL);
                Set<String> courseSet = EntityUtils.getSet(setupList, CompreSetup::getSubjectId);
                courseList = SUtils.dt(courseRemoteService.orderCourse(courseRemoteService.findListByIds(courseSet.toArray(new String[0]))), new TR<List<Course>>(){});
            }
            Course course = new Course();
            course.setId(BaseConstants.ZERO_GUID);
            course.setSubjectName("总评总成绩");
            courseList.add(course);
        } else if (CompreStatisticsConstants.TYPE_ENGLISH.equals(type)) {
            Course course = new Course();
            course.setId(CompreStatisticsConstants.SUBJECT_CODE_YY_3);
            course.setSubjectName("英语");
            courseList = new ArrayList<>();
            courseList.add(course);
        } else if (CompreStatisticsConstants.TYPE_ENG_SPEAK.equals(type)) {
            Course course = new Course();
            course.setId(CompreStatisticsConstants.SUBJECT_CODE_KS);
            course.setSubjectName("口试");
            courseList = new ArrayList<>();
            courseList.add(course);
        } else if (CompreStatisticsConstants.TYPE_GYM.equals(type)) {
            Course course = new Course();
            course.setId(CompreStatisticsConstants.SUBJECT_CODE_TY_3);
            course.setSubjectName("体育");
            courseList = new ArrayList<>();
            courseList.add(course);
        } else {
        	courseList = SUtils.dt(courseRemoteService.findByUnitCourseCodes(unitId, CompreStatisticsConstants.HW_CODE_10.toArray(new String[] {})),new TR<List<Course>>(){});
            isCounted = true;
        }
        map.put("isCounted", isCounted);
        map.put("courseList", courseList);
        if (isCounted) {
            List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,
                    gradeId), new TR<List<Clazz>>(){});
            String[] classIds = EntityUtils.getList(classList, Clazz::getId).toArray(new String[0]);
            String compreInfoId = compreInfo == null ? BaseConstants.ZERO_GUID : compreInfo.getId();
            List<CompreScoreMapDto> scoreList=compreScoreService.getFirstOrFinallyList(unitId, type, compreInfoId, classIds, true, page);
            // 按学号排序
            scoreList.sort(new Comparator<CompreScoreMapDto>() {
                @Override
                public int compare(CompreScoreMapDto prev, CompreScoreMapDto next) {
                    String prevCode = prev.getStudentCode();
                    String nextCode = next.getStudentCode();
                    if (prevCode == null) {
                        return -1;
                    }
                    if (nextCode == null) {
                        return 1;
                    }
                    for (int i = 0; i < prevCode.length(); i++) {
                        if (i >= nextCode.length()) {
                            return -1;
                        }
                        if (prevCode.charAt(i) != nextCode.charAt(i)) {
                            return prevCode.charAt(i) - nextCode.charAt(i);
                        }
                    }
                    return 1;
                }
            });
            map.put("scoreList", scoreList);
            sendPagination(request, map, page);
        }
        return "/comprehensive/score/subModelList.ftl";
    }

    @RequestMapping("/model/sync")
    @ResponseBody
    public String subModelSync(String unitId, String acadyear, String semester, String gradeId, String type) {
        try {
            compreScoreService.saveSynFirst(unitId, acadyear, semester, gradeId, type);
        } catch (Exception e) {
            e.printStackTrace();
            return error("同步失败");
        }
        return success("同步完成");
    }

}
