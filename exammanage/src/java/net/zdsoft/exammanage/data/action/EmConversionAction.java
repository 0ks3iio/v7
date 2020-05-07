package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.exammanage.data.dto.EmConversionDto;
import net.zdsoft.exammanage.data.dto.EmScoreInfoDto;
import net.zdsoft.exammanage.data.entity.EmConversion;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.service.EmConversionService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmScoreInfoService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/exammanage")
public class EmConversionAction extends BaseAction {
    @Autowired
    private EmConversionService emConversionService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;


    @RequestMapping("/conversionList/index/page")
    @ControllerInfo(value = "选考科目赋分计算index")
    public String showConList(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        map.put("unitId", unitId);
        //学年学期
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        LoginInfo loginInfo = getLoginInfo();
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        School school = SUtils.dc(schoolRemoteService.findOneById(loginInfo.getUnitId()), School.class);
        List<Grade> gradeList = getSchGradeList(findMapMapByMcodeIds, school);
        map.put("gradeList", gradeList);
        return "/exammanage/conversion/conversionIndex.ftl";
    }

    @RequestMapping("/scoreConList/list/page")
    @ControllerInfo(value = "选考科目赋分计算list")
    public String showListPage(String acadyear, String semester, String examId, String subjectId,
                               ModelMap map, HttpServletRequest request) {
        String unitId = getLoginInfo().getUnitId();
        map.put("unitId", unitId);
        Pagination page = createPagination();
        List<EmScoreInfoDto> dtoList = emScoreInfoService.findByXkConListDto(unitId, acadyear, semester, examId, subjectId, page);
        map.put("dtoList", dtoList);
        map.put("Pagination", page);
        sendPagination(request, map, page);
        return "/exammanage/conversion/conversionList.ftl";
    }

    @RequestMapping("/scoreConList/export")
    @ControllerInfo(value = "选考科目赋分计算export")
    public void exportConList(String acadyear, String semester, String examId, String subjectId,
                              ModelMap map, HttpServletRequest request) {
        String unitId = getLoginInfo().getUnitId();
        List<EmScoreInfoDto> dtoList = emScoreInfoService.findByXkConListDto(unitId, acadyear, semester, examId, subjectId, null);
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        String subjectName = course == null ? "" : course.getSubjectName();

        List<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            EmScoreInfoDto dto = null;
            Map<String, String> valueMap = null;
            for (int i = 0; i < dtoList.size(); i++) {
                dto = dtoList.get(i);
                valueMap = new HashMap<String, String>();
                valueMap.put("序号", String.valueOf(i + 1));//
                valueMap.put("学生", dto.getStuName());
                valueMap.put("考号", dto.getExamNum());
                valueMap.put("学号", dto.getStuCode());
                valueMap.put("行政班", dto.getClassName());
                valueMap.put("教学班", dto.getTeachClassName());
                valueMap.put("考试原始分", dto.getScoreInfo().getScore());
                valueMap.put("考试分排名", dto.getScoreInfo().getRank());
                valueMap.put("赋分", dto.getScoreInfo().getConScore());
                valueMap.put("赋分等级", dto.getScoreInfo().getScoreRank());
                recordList.add(valueMap);
            }
        }
        List<String> tis = new ArrayList<>();
        tis.add("序号");
        tis.add("学生");
        tis.add("考号");
        tis.add("学号");
        tis.add("行政班");
        tis.add("教学班");
        tis.add("考试原始分");
        tis.add("考试分排名");
        tis.add("赋分");
        tis.add("赋分等级");
        sheetName2RecordListMap.put("EmScoreInfoExport", recordList);
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put("EmScoreInfoExport", tis);
        String ss = "";
        if ("1".equals(semester)) {
            ss = "第一学期";
        } else {
            ss = "第二学期";
        }
        ExportUtils ex = ExportUtils.newInstance();
        ex.exportXLSFile(acadyear + "学年" + ss + subjectName + "赋分计算", titleMap, sheetName2RecordListMap, getResponse());
    }


    @ResponseBody
    @RequestMapping("/conCount/count")
    @ControllerInfo("计算赋分")
    public String copyTestNum(String unitId, String examId) {
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null || exam.getIsDeleted() == Constant.IS_DELETED_TRUE) {
            return error("该考试不存在或已删除！");
        }
        try {
            emScoreInfoService.conversionCount(unitId, examId);
        } catch (Exception e) {
            return error("操作失败");
        }
        return success("操作成功！");
    }

    @RequestMapping("/conversion/index/page")
    @ControllerInfo(value = "选考科目赋分设置index")
    public String showList(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<EmConversion> conlist = emConversionService.findByUnitId(unitId);
        if (CollectionUtils.isEmpty(conlist)) {
            conlist = new ArrayList<>();
        }
        map.put("conlist", conlist);
        map.put("unitId", unitId);
        return "/exammanage/conversion/conversionSet.ftl";
    }

    @ResponseBody
    @RequestMapping("/conversionSet/save")
    @ControllerInfo(value = "保存赋分等级设置")
    public String saveNotLimit(EmConversionDto dto, HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        String unitId = loginInfo.getUnitId();
        List<EmConversion> insertlist = new ArrayList<>();
        int countBalance = 0;
        for (EmConversion e : dto.getEmConlist()) {
            if (e != null && e.getScoreRank() > 0) {
                if (e.getStartScore() <= e.getEndScore()) {
                    countBalance += e.getBalance();
                    e.setUnitId(unitId);
                    e.setCreationTime(new Date());
                    e.setModifyTime(new Date());
                    e.setId(UuidUtils.generateUuid());
                    insertlist.add(e);
                } else {
                    return error("分数段范围设置，最低赋分不能大于最高赋分，第" + e.getScoreRank() + "等级设置有误");
                }
            }
        }
        if (countBalance != 100) {
            return error("人数比例之和必须为100%,当前为" + countBalance + "%");
        }
        try {
            if (CollectionUtils.isNotEmpty(insertlist)) {
                emConversionService.saveAllEntity(unitId, insertlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnError();
        }
        return success("保存成功！");
    }


    private List<Grade> getSchGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds, School school) {
        List<Grade> gradeList = new ArrayList<>();
        String sections = school.getSections();
        if (StringUtils.isNotBlank(sections)) {
            String[] sectionArr = sections.split(",");
            Integer yearLength = 0;
            Map<String, McodeDetail> map = null;
            for (String ss : sectionArr) {
                int section = Integer.parseInt(ss);
                switch (section) {
                    case 0:
                        yearLength = school.getInfantYear();
                        map = findMapMapByMcodeIds.get("DM-RKXD-0");
                        break;
                    case 1:
                        yearLength = school.getGradeYear();
                        map = findMapMapByMcodeIds.get("DM-RKXD-1");
                        break;
                    case 2:
                        yearLength = school.getJuniorYear();
                        map = findMapMapByMcodeIds.get("DM-RKXD-2");
                        break;
                    case 3:
                        yearLength = school.getSeniorYear();
                        map = findMapMapByMcodeIds.get("DM-RKXD-3");
                        break;
                    case 9:
                        yearLength = school.getSeniorYear();
                        map = findMapMapByMcodeIds.get("DM-RKXD-9");
                        break;
                    default:
                        break;
                }
                if (yearLength == null || yearLength == 0) {
                    continue;
                }
                for (int j = 0; j < yearLength; j++) {
                    int grade = j + 1;
                    Grade dto = new Grade();
                    dto.setGradeCode(section + "" + grade);
                    if (map.containsKey(grade + "")) {
                        dto.setGradeName(map.get(grade + "").getMcodeContent());
                    }
                    gradeList.add(dto);
                }
            }
        }
        Collections.sort(gradeList, new Comparator<Grade>() {
            public int compare(Grade o1, Grade o2) {
                return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
            }
        });
        return gradeList;
    }


}
