package net.zdsoft.scoremanage.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ScoreInfoService;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/scoremanage/required")
public class ScoreSummaryAction extends BaseAction {

    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private ScoreInfoService scoreInfoService;
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StuworkRemoteService stuworkRemoteService;

    @RequestMapping("/index/page")
    public String showIndex(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
        List<String> mcodeIds = new ArrayList<>();
        for (String section : school.getSections().split(",")) {
            mcodeIds.add("DM-RKXD-" + section);
        }
        List<McodeDetail> mcodeDetails = SUtils.dt(mcodeRemoteService.findAllByMcodeIds(mcodeIds.toArray(new String[0])), McodeDetail.class);
        Iterator<McodeDetail> iterator = mcodeDetails.iterator();
        while (iterator.hasNext()) {
            McodeDetail next = iterator.next();
            switch (next.getMcodeId().split("-")[2]) {
                case "0":
                    if (Integer.valueOf(next.getThisId()) > school.getInfantYear()) {
                        iterator.remove();
                    }
                    break;
                case "1":
                    if (Integer.valueOf(next.getThisId()) > school.getGradeYear()) {
                        iterator.remove();
                    }
                    break;
                case "2":
                    if (Integer.valueOf(next.getThisId()) > school.getJuniorYear()) {
                        iterator.remove();
                    }
                    break;
                case "3":
                    if (Integer.valueOf(next.getThisId()) > school.getSeniorYear()) {
                        iterator.remove();
                    }
                    break;
                case "9":
                    if (Integer.valueOf(next.getThisId()) > school.getSeniorYear()) {
                        iterator.remove();
                    }
                    break;
            }
        }
        mcodeDetails.stream().forEach(e -> {
            e.setFieldValue(e.getMcodeId().split("-")[2] + e.getThisId());
        });
        map.put("unitName", school.getSchoolName());
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
        map.put("acadyearList", acadyearList);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        map.put("semester", semester);
        map.put("gradeList", mcodeDetails);
        return "/scoremanage/summary/index.ftl";
    }

    @RequestMapping("/exam/list")
    @ResponseBody
    public String examlist(@RequestParam String acadyear, @RequestParam String semester) {
        String unitId = getLoginInfo().getUnitId();
        List<ExamInfo> examInfoList = examInfoService.findExamInfoList(unitId, acadyear, semester);
        return Json.toJSONString(examInfoList);
    }

    @RequestMapping("/detail")
    public String examDetail(String gradeCode, String examId, String acadyear, String semester, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        scoreInfoService.findJsonScoreInfo(unitId, examId, gradeCode, map);
        if (map.size() <= 1){
            return "/scoremanage/summary/scoreList.ftl";
        }
        List<String> courseNameList = new ArrayList<>((Set<String>) map.get("courseNameList"));
        courseNameList.add("操行等第");
        courseNameList.add("评语");
        map.put("courseNameList", courseNameList);
        List<Student> studentList = (List<Student>) map.get("studentList");
        Map<String, Map<String, JSONObject>> infoMap = (Map<String, Map<String, JSONObject>>) map.get("infoMap");

        Iterator<Student> iterator = studentList.iterator();
        Set<String> studentIdSet = new HashSet<>();
        while (iterator.hasNext()) {
            Student next = iterator.next();
            if (Integer.valueOf(1).equals(next.getIsLeaveSchool())) {
                iterator.remove();
            }
            studentIdSet.add(next.getId());
        }
        List<DyStuEvaluation> dyList = SUtils.dt(stuworkRemoteService.findStuEvaluationListByUnitIdAndStudentIds(unitId, acadyear, semester, studentIdSet.toArray(new String[0])), DyStuEvaluation.class);
        for (DyStuEvaluation one : dyList) {
            if (infoMap.containsKey(one.getStudentId())) {
                JSONObject tmp = new JSONObject();
                tmp.put("score", one.getGrade());
                infoMap.get(one.getStudentId()).put("操行等第", tmp);
                tmp = new JSONObject();
                tmp.put("score", one.getRemark());
                infoMap.get(one.getStudentId()).put("评语", tmp);
            }
        }

        return "/scoremanage/summary/scoreList.ftl";
    }

    @RequestMapping("/export")
    @ResponseBody
    public String export(String gradeCode, String examId, String acadyear, String semester, ModelMap map, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        scoreInfoService.findJsonScoreInfo(unitId, examId, gradeCode, map);

        List<String> courseNameList = new ArrayList<>((Set<String>) map.get("courseNameList"));
        courseNameList.add("操行等第");
        courseNameList.add("评语");
        map.put("courseNameList", courseNameList);
        Map<String, Map<String, JSONObject>> infoMap = (Map<String, Map<String, JSONObject>>) map.get("infoMap");
        List<Student> studentList = (List<Student>) map.get("studentList");

        Iterator<Student> iterator = studentList.iterator();
        Set<String> studentIdSet = new HashSet<>();
        while (iterator.hasNext()) {
            Student next = iterator.next();
            if (Integer.valueOf(1).equals(next.getIsLeaveSchool())) {
                iterator.remove();
            }
            studentIdSet.add(next.getId());
        }
        List<DyStuEvaluation> dyList = SUtils.dt(stuworkRemoteService.findStuEvaluationListByUnitIdAndStudentIds(unitId, acadyear, semester, studentIdSet.toArray(new String[0])), DyStuEvaluation.class);
        for (DyStuEvaluation one : dyList) {
            if (infoMap.containsKey(one.getStudentId())) {
                JSONObject tmp = new JSONObject();
                tmp.put("score", one.getGrade());
                infoMap.get(one.getStudentId()).put("操行等第", tmp);
                tmp = new JSONObject();
                tmp.put("score", one.getRemark());
                infoMap.get(one.getStudentId()).put("评语", tmp);
            }
        }

        //表头
        List<String> titleList = new ArrayList<>();
        titleList.add("序号");
        titleList.add("班级");
        titleList.add("姓名");
        titleList.addAll(courseNameList);

        //导出
        String fileName = (String)map.get("title");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("成绩汇总");
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        HSSFCellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);//水平
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
        centerStyle.setBorderBottom(BorderStyle.THIN);
        centerStyle.setBorderLeft(BorderStyle.THIN);
        centerStyle.setBorderRight(BorderStyle.THIN);
        centerStyle.setBorderTop(BorderStyle.THIN);
        //标题行固定
        sheet.createFreezePane(0, 1);
        int rownum = 0;
        HSSFRow rowTitle = sheet.createRow(rownum++);
        rowTitle.setHeightInPoints((2f) * sheet.getDefaultRowHeightInPoints());

        for (int i = 0; i < titleList.size(); i++) {
            sheet.setColumnWidth(i, 10 * 256);//列宽
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
            cell.setCellStyle(titleStyle);
        }

        int index = 1;
        if (CollectionUtils.isNotEmpty(studentList)) {
            for (Student student : studentList) {
                HSSFRow rowTmp = sheet.createRow(rownum++);
                int i = 0;
                HSSFCell cellTmp = rowTmp.createCell(i++);
                cellTmp.setCellStyle(centerStyle);
                cellTmp.setCellValue(new HSSFRichTextString("" + index++));
                cellTmp = rowTmp.createCell(i++);
                cellTmp.setCellStyle(centerStyle);
                cellTmp.setCellValue(new HSSFRichTextString(student.getClassName()));
                cellTmp = rowTmp.createCell(i++);
                cellTmp.setCellStyle(centerStyle);
                cellTmp.setCellValue(new HSSFRichTextString(student.getStudentName()));
                for (String courseName : courseNameList) {
                    cellTmp = rowTmp.createCell(i++);
                    cellTmp.setCellStyle(centerStyle);
                    if (infoMap.containsKey(student.getId()) && infoMap.get(student.getId()).containsKey(courseName)) {
                        cellTmp.setCellValue(new HSSFRichTextString(infoMap.get(student.getId()).get(courseName).getString("score")));
                    }
                }
            }
        }

        sheet.autoSizeColumn(titleList.size() - 1);

        ExportUtils.outputData(workbook, fileName, response);
        return returnSuccess();
    }

}
