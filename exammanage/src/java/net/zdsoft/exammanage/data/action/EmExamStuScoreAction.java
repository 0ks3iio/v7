package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.service.EmClassInfoService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmScoreInfoService;
import net.zdsoft.exammanage.data.service.EmSubjectInfoService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.ZipUtils;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.exammanage.data.action
 * @ClassName: EmExamStuScoreAction
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2018/7/27 10:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/7/27 10:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/exammanage")
public class EmExamStuScoreAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private DateInfoRemoteService dateInfoRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private UnitRemoteService unitService;
    @Autowired
    private StuworkRemoteService stuworkRemoteService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private CourseRemoteService courseService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private CourseTypeRemoteService courseTypeRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;

    @RequestMapping("studentScore/index/page")
    @ControllerInfo(value = "学生成绩单")
    public String showStudentScore(ModelMap map) {
        Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (null != sem) {
            String defaultAcadyear = sem.getAcadyear();
            int defaultSemester = sem.getSemester();
            DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), defaultAcadyear, defaultSemester, new Date()), DateInfo.class);
            if (dateInfo == null) {
                return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
            }
            map.put("acadyear", defaultAcadyear);
            map.put("semester", defaultSemester);
            List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadyear(getLoginInfo().getUnitId(), sem.getAcadyear(), String.valueOf(sem.getSemester()));
            if (CollectionUtils.isNotEmpty(examList)) {
                map.put("examList", examList);
                map.put("examId", examList.get(0).getId());
                Set<String> examIdSet = new HashSet<String>();
                examIdSet.add(examList.get(0).getId());
//                List<EmSubjectInfo> emSubjectInfoList = emSubjectInfoService.findByUnitIdAndExamId(getLoginInfo().getUnitId(), new String[]{examList.get(0).getId()});
//                Set<String> subInfoIdSet = new HashSet<String>();
//                if(CollectionUtils.isNotEmpty(emSubjectInfoList)) {
//                    for (EmSubjectInfo emSubjectInfo : emSubjectInfoList) {
//                        subInfoIdSet.add(emSubjectInfo.getId());
//                    }
//                }
                if (CollectionUtils.isNotEmpty(examIdSet)) {
                    List<EmClassInfo> emClassInfos = emClassInfoService.findBySchoolIdAndExamIdIn(examIdSet.toArray(new String[0]), getLoginInfo().getUnitId());
                    Set<String> classIdSet = new HashSet<String>();
                    for (EmClassInfo emClassInfo : emClassInfos) {
                        if ("1".equals(emClassInfo.getClassType())) {
                            classIdSet.add(emClassInfo.getClassId());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(classIdSet)) {
                        List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
                        });
                        Collections.sort(clsList, new Comparator<Clazz>() {
                            @Override
                            public int compare(Clazz o1, Clazz o2) {
                                //升序
                                return o1.getClassNameDynamic().compareTo(o2.getClassNameDynamic());
                            }
                        });
                        map.put("clsList", clsList);
                        if (CollectionUtils.isNotEmpty(clsList)) {
                            map.put("classId", clsList.get(0).getId());
                            map.put("className", clsList.get(0).getClassNameDynamic());
                            List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{clsList.get(0).getId()}), new TR<List<Student>>() {
                            });
                            if (CollectionUtils.isNotEmpty(studentList)) {
                                Collections.sort(studentList, new Comparator<Student>() {
                                    @Override
                                    public int compare(Student o1, Student o2) {
                                        //升序
                                        return o1.getStudentName().compareTo(o2.getStudentName());
                                    }
                                });
                                dealData(defaultAcadyear, String.valueOf(defaultSemester), studentList.get(0).getId(), clsList.get(0).getGradeId(), examList.get(0).getId(), map);
                                map.put("studentId", studentList.get(0).getId());
                                map.put("studentList", studentList);
                                map.put("studentName", studentList.get(0).getStudentName());
                                map.put("studentCode", studentList.get(0).getStudentCode());
                            }
                        }
                    }
                }
            }
            map.put("acadyearList", acadyearList);
            Unit unit = SUtils.dc(unitService.findOneById(getLoginInfo().getUnitId()), Unit.class);
            String unitName = unit.getUnitName();
            String schName = "";
            if (unitName.contains("数字校园")) {
                schName = unitName.replace("数字校园", "");
            } else {
                schName = unitName;
            }
            map.put("unitName", schName);
            map.put("nowDate", new Date());
        } else {
            return errorFtl(map, "当前时间不在学年学期内，无法维护！");
        }
        return "/exammanage/studentScore/examStudentScoreIndex.ftl";
    }

    public void dealData(String acadyear, String semester, String studentId, String gradeId, String examId, ModelMap map) {
        //操行等第
        DyBusinessOptionDto dyBusinessOptionDto = new DyBusinessOptionDto();
        dyBusinessOptionDto.setAcadyear(acadyear);
        dyBusinessOptionDto.setSemester(String.valueOf(semester));
        dyBusinessOptionDto.setStudentId(studentId);
        DyStuEvaluation dyStuEvaluation = SUtils.dc(stuworkRemoteService.findStuEvaluationOneByUidAndDto(getLoginInfo().getUnitId(), dyBusinessOptionDto), DyStuEvaluation.class);
        if (null != dyStuEvaluation) {
            map.put("optionName", dyStuEvaluation.getGrade());
            map.put("remark", dyStuEvaluation.getRemark());
        }
        //体检信息
        List<DyStuHealthResult> dyStuHealthResultList = SUtils.dt(stuworkRemoteService.findStuHealthResultOneByStudnetId(getLoginInfo().getUnitId(), acadyear, semester, studentId), new TR<List<DyStuHealthResult>>() {
        });
        map.put("dyStuHealthResultList", dyStuHealthResultList);
        //期末考试必修课成绩
        List<EmScoreInfo> emScoreInfos = emScoreInfoService.findByCondition(getLoginInfo().getUnitId(), acadyear, semester, studentId, examId);
        Set<String> subjectIdSet = new HashSet<>();
        for (EmScoreInfo emScoreInfo : emScoreInfos) {
            subjectIdSet.add(emScoreInfo.getSubjectId());
        }
        if (CollectionUtils.isNotEmpty(subjectIdSet)) {
            List<Course> courseList = SUtils.dt(courseService.findBySubjectIdIn(subjectIdSet.toArray(new String[0])), new TR<List<Course>>() {
            });
            Map<String, String> courseNameMap = new HashMap<String, String>();
            for (Course course : courseList) {
                courseNameMap.put(course.getId(), course.getSubjectName());
            }
            for (EmScoreInfo emScoreInfo : emScoreInfos) {
                emScoreInfo.setSubjectName(courseNameMap.get(emScoreInfo.getSubjectId()));
            }
        }
        map.put("scoreInfoList", emScoreInfos);
        //选修课成绩
        List<EmScoreInfo> optionalScoreInfoList = emScoreInfoService.findByCondition(getLoginInfo().getUnitId(), acadyear, semester, studentId, "00000000000000000000000000000000");
        Set<String> teachSubjectIdSet = new HashSet<String>();
        for (EmScoreInfo emScoreInfo : optionalScoreInfoList) {
            teachSubjectIdSet.add(emScoreInfo.getSubjectId());
        }
        if (CollectionUtils.isNotEmpty(teachSubjectIdSet)) {
            List<Course> teachCourseList = SUtils.dt(courseRemoteService.findListByIds(teachSubjectIdSet.toArray(new String[0])), Course.class);
            Map<String, String> courseNameMap = new HashMap<String, String>();
            Map<String, String> courseTypeIdMap = new HashMap<String, String>();
            Set<String> courseTypeIdSet = new HashSet<String>();
            for (Course course : teachCourseList) {
                courseNameMap.put(course.getId(), course.getSubjectName());
                courseTypeIdMap.put(course.getId(), course.getCourseTypeId());
                courseTypeIdSet.add(course.getCourseTypeId());
            }
            List<CourseType> courseTypeList = SUtils.dt(courseTypeRemoteService.findListByIds(courseTypeIdSet.toArray(new String[0])), new TR<List<CourseType>>() {
            });
            Map<String, String> courseTypeNameMap = new HashMap<String, String>();
            for (CourseType type : courseTypeList) {
                courseTypeNameMap.put(type.getId(), type.getName());
            }
            for (EmScoreInfo score : optionalScoreInfoList) {
                score.setSubjectName(courseNameMap.get(score.getSubjectId()));
                score.setCourseTypeName(courseTypeNameMap.get(courseTypeIdMap.get(score.getSubjectId())));
            }
        }
        map.put("optionalScoreInfoList", optionalScoreInfoList);
    }

    @RequestMapping("/studentScore/searchStuScore")
    public String searchScoreReport(String acadyear, String semester, String classId, String studentId, String examId, ModelMap map) {

        Semester sem = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (null != sem) {
            String defaultAcadyear = sem.getAcadyear();
            int defaultSemester = sem.getSemester();
            DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(getLoginInfo().getUnitId(), defaultAcadyear, defaultSemester, new Date()), DateInfo.class);
            if (dateInfo == null) {
                return errorFtl(map, "所选日期不在当前学期的节假日设置范围内！");
            }
            map.put("acadyear", defaultAcadyear);
            map.put("semester", defaultSemester);
        } else {
            return errorFtl(map, "当前时间不在学年学期内，无法维护！");
        }
        List<EmExamInfo> examList = emExamInfoService.findByUnitIdAndAcadyear(getLoginInfo().getUnitId(), acadyear, semester);
        map.put("examList", examList);
        if (CollectionUtils.isNotEmpty(examList)) {
            if (StringUtils.isBlank(examId)) {
                examId = examList.get(0).getId();
            }
            if (StringUtils.isNotBlank(examId)) {
                map.put("examId", examId);
                Set<String> examIdSet = new HashSet<String>();
                examIdSet.add(examId);
                if (CollectionUtils.isNotEmpty(examIdSet)) {
                    List<EmExamInfo> emClassInfos = emExamInfoService.findListByIds(examIdSet.toArray(new String[0]));
                    Set<String> gradeCodeSet = new HashSet<>();
                    for (EmExamInfo emExamInfo : emClassInfos) {
                        gradeCodeSet.add(emExamInfo.getGradeCodes());
                    }
                    if(CollectionUtils.isNotEmpty(gradeCodeSet)) {
                        List<Grade> list = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(getLoginInfo().getUnitId(),gradeCodeSet.toArray(new String[]{})),new TR<List<Grade>>(){});
                        if(CollectionUtils.isNotEmpty(list)){
                            gradeCodeSet = list.stream().map(grade -> grade.getId()).collect(Collectors.toSet());
//                            Clazz clazz =
                        }
                    }
                    if (CollectionUtils.isNotEmpty(gradeCodeSet)) {
                        List<Clazz> clsList = SUtils.dt(classRemoteService.findByInGradeIds(gradeCodeSet.toArray(new String[0])), new TR<List<Clazz>>() {
                        });
                        Collections.sort(clsList, new Comparator<Clazz>() {
                            @Override
                            public int compare(Clazz o1, Clazz o2) {
                                //升序
                                return o1.getClassNameDynamic().compareTo(o2.getClassNameDynamic());
                            }
                        });
                        map.put("clsList", clsList);
                        List<Student> studentList = new ArrayList<Student>();
                        if (StringUtils.isBlank(classId)) {
                            if (CollectionUtils.isNotEmpty(clsList)) {
                                classId = clsList.get(0).getId();
                            } else {
                                map.put("studentList", studentList);
                            }
                        }else {
                            if(CollectionUtils.isEmpty(clsList)){
                                classId="";
                                map.put("studentList", studentList);
                            }
                        }
                        if (StringUtils.isNotBlank(classId)) {
                            map.put("classId", classId);
                            Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
                            map.put("className", cls.getClassNameDynamic());
                            studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
                            });
                            Collections.sort(studentList, new Comparator<Student>() {
                                @Override
                                public int compare(Student o1, Student o2) {
                                    //升序
                                    return o1.getStudentName().compareTo(o2.getStudentName());
                                }
                            });
                            map.put("studentList", studentList);
                            if (CollectionUtils.isNotEmpty(studentList)) {
                                if (StringUtils.isBlank(studentId) || "undefined".equals(studentId)) {
                                    studentId = studentList.get(0).getId();
                                }
                                if (StringUtils.isNotBlank(studentId)) {
                                    map.put("studentId", studentId);
                                    Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
                                    map.put("studentName", student.getStudentName());
                                    map.put("studentCode", student.getStudentCode());
                                } else {
                                    studentId = studentList.get(0).getId();
                                    map.put("studentId", studentList.get(0).getId());
                                    Student student = SUtils.dc(studentRemoteService.findOneById(studentList.get(0).getId()), Student.class);
                                    map.put("studentName", student.getStudentName());
                                    map.put("studentCode", student.getStudentCode());
                                }
                            }
                            dealData(acadyear, semester, studentId, cls.getGradeId(), examId, map);
                        }
                    }
                }
            }
        }


        Unit unit = SUtils.dc(unitService.findOneById(getLoginInfo().getUnitId()), Unit.class);
        String unitName = unit.getUnitName();
        String schName = "";
        if (unitName.contains("数字校园")) {
            schName = unitName.replace("数字校园", "");
        } else {
            schName = unitName;
        }
        map.put("unitName", schName);
        map.put("acadyearList", acadyearList);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("nowDate", new Date());
        return "/exammanage/studentScore/examStudentScoreIndex.ftl";
    }

    @RequestMapping("/studentScore/doExport")
    public void doExport(String acadyear, String semester, String classId, String studentId, String examId, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        try {
            Unit unit = SUtils.dc(unitService.findOneById(getLoginInfo().getUnitId()), Unit.class);
            String unitName = unit.getUnitName();
            String schName = "";
            if (unitName.contains("数字校园")) {
                schName = unitName.replace("数字校园", "");
            } else {
                schName = unitName;
            }
            List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
            });
            Map<String, HSSFWorkbook> excels = new HashMap<String, HSSFWorkbook>();

            for (Student student : studentList) {
                searchScoreReport(acadyear, semester, classId, student.getId(), examId, map);
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFFont headfont = workbook.createFont();
                headfont.setFontHeightInPoints((short) 10);// 字体大小
                headfont.setBold(true);
                HSSFSheet sheet = workbook.createSheet(student.getStudentName() + "成绩");
                sheet.setColumnWidth(0, 2000);
                sheet.setColumnWidth(1, 2800);
                sheet.setColumnWidth(2, 2800);
                sheet.setColumnWidth(3, 2800);
                sheet.setColumnWidth(4, 2800);
                sheet.setColumnWidth(5, 2800);
                sheet.setColumnWidth(6, 2800);
                sheet.setColumnWidth(7, 2800);

				/*// 插入 PNG 图片至 Excel
			    String fileName =Evn.getRequest().getRealPath("/scoremanage/images/logo1.png");
			    InputStream is = new FileInputStream(fileName);
			    byte[] bytes = IOUtils.toByteArray(is);
			    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			    CreationHelper helper = workbook.getCreationHelper();
			    Drawing drawing = sheet.createDrawingPatriarch();
			    ClientAnchor anchor = helper.createClientAnchor();
			    // 图片插入坐标
			    anchor.setCol1(0);
			    anchor.setRow1(0);

			    // 插入图片
			    Picture pict = drawing.createPicture(anchor, pictureIdx);
			    pict.resize();*/

                HSSFCellStyle style = workbook.createCellStyle();
                style.setFont(headfont);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setWrapText(true);

                HSSFCellStyle style2 = workbook.createCellStyle();
                style2.setFont(headfont);
                style2.setBorderBottom(BorderStyle.THIN);
                style2.setBorderLeft(BorderStyle.THIN);
                style2.setBorderRight(BorderStyle.THIN);
                style2.setBorderTop(BorderStyle.THIN);
                style2.setAlignment(HorizontalAlignment.LEFT);
                style2.setVerticalAlignment(VerticalAlignment.CENTER);

                HSSFCellStyle style3 = workbook.createCellStyle();
                HSSFFont headfont3 = workbook.createFont();
                headfont3.setFontHeightInPoints((short) 15);// 字体大小
                headfont3.setBold(true);
                style3.setFont(headfont3);
                style3.setBorderBottom(BorderStyle.THIN);
                style3.setBorderLeft(BorderStyle.THIN);
                style3.setBorderRight(BorderStyle.THIN);
                style3.setBorderTop(BorderStyle.THIN);
                style3.setAlignment(HorizontalAlignment.CENTER);
                style3.setVerticalAlignment(VerticalAlignment.CENTER);

                HSSFCellStyle style4 = workbook.createCellStyle();
                HSSFFont headfont4 = workbook.createFont();
                headfont4.setFontHeightInPoints((short) 10);// 字体大小
                headfont4.setBold(true);
                style4.setFont(headfont4);
                style4.setBorderBottom(BorderStyle.THIN);
                style4.setBorderLeft(BorderStyle.THIN);
                style4.setBorderRight(BorderStyle.THIN);
                style4.setBorderTop(BorderStyle.THIN);
                style4.setAlignment(HorizontalAlignment.LEFT);
                style4.setVerticalAlignment(VerticalAlignment.TOP);
                style4.setWrapText(true);
                //第一行
                HSSFRow row1 = sheet.createRow(0);
                CellRangeAddress car1 = new CellRangeAddress(0, 3, 0, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car1, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car1, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car1, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car1, sheet);
                sheet.addMergedRegion(car1);
                HSSFCell cell11 = row1.createCell(0);
                cell11.setCellStyle(style3);
                cell11.setCellValue(new HSSFRichTextString(schName + acadyear + "学年" + semester + "学期学生成绩单"));
                //第二行
                HSSFRow row2 = sheet.createRow(4);
                CellRangeAddress car2 = new CellRangeAddress(4, 6, 0, 1);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car2, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car2, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car2, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car2, sheet);
                sheet.addMergedRegion(car2);
                HSSFCell cell21 = row2.createCell(0);
                cell21.setCellStyle(style2);
                cell21.setCellValue(new HSSFRichTextString("班级：" + map.get("className")));

                CellRangeAddress car3 = new CellRangeAddress(4, 6, 2, 3);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car3, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car3, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car3, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car3, sheet);
                sheet.addMergedRegion(car3);
                HSSFCell cell22 = row2.createCell(2);
                cell22.setCellStyle(style2);
                cell22.setCellValue(new HSSFRichTextString("姓名：" + map.get("studentName")));

                CellRangeAddress car4 = new CellRangeAddress(4, 6, 4, 5);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car4, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car4, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car4, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car4, sheet);
                sheet.addMergedRegion(car4);
                HSSFCell cell23 = row2.createCell(4);
                cell23.setCellStyle(style2);
                cell23.setCellValue(new HSSFRichTextString("学号：" + map.get("studentCode")));

                CellRangeAddress car5 = new CellRangeAddress(4, 6, 6, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car5, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car5, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car5, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car5, sheet);
                sheet.addMergedRegion(car5);
                HSSFCell cell24 = row2.createCell(6);
                cell24.setCellStyle(style2);
                cell24.setCellValue(new HSSFRichTextString("操行等第：" + StringUtils.trimToEmpty((String) map.get("optionName"))));

                //第三行
                HSSFRow row3 = sheet.createRow(7);
                CellRangeAddress car6 = new CellRangeAddress(7, 9, 0, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car6, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car6, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car6, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car6, sheet);
                sheet.addMergedRegion(car6);
                HSSFCell cell31 = row3.createCell(0);
                cell31.setCellStyle(style2);
                List<DyStuHealthResult> dyStuHealthResultList = (List<DyStuHealthResult>) map.get("dyStuHealthResultList");
                String healthStr = "";
                for (DyStuHealthResult item : dyStuHealthResultList) {
                    healthStr = healthStr + item.getItemName() + "  " + StringUtils.trimToEmpty(item.getItemResult()) + StringUtils.trimToEmpty(item.getItemUnit()) + ";";
                }
                cell31.setCellValue(new HSSFRichTextString("体检信息：" + healthStr));

                //第四行
                HSSFRow row4 = sheet.createRow(10);
                CellRangeAddress car7 = new CellRangeAddress(10, 12, 0, 1);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car7, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car7, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car7, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car7, sheet);
                sheet.addMergedRegion(car7);
                HSSFCell cell41 = row4.createCell(0);
                cell41.setCellStyle(style2);
                cell41.setCellValue(new HSSFRichTextString("事假：天"));

                CellRangeAddress car8 = new CellRangeAddress(10, 12, 2, 3);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car8, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car8, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car8, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car8, sheet);
                sheet.addMergedRegion(car8);
                HSSFCell cell42 = row4.createCell(2);
                cell42.setCellStyle(style2);
                cell42.setCellValue(new HSSFRichTextString("病假：天"));

                CellRangeAddress car9 = new CellRangeAddress(10, 12, 4, 5);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car9, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car9, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car9, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car9, sheet);
                sheet.addMergedRegion(car9);
                HSSFCell cell43 = row4.createCell(4);
                cell43.setCellStyle(style2);
                cell43.setCellValue(new HSSFRichTextString("旷课：天"));

                CellRangeAddress car10 = new CellRangeAddress(10, 12, 6, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car10, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car10, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car10, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car10, sheet);
                sheet.addMergedRegion(car10);
                HSSFCell cell44 = row4.createCell(6);
                cell44.setCellStyle(style);
                cell44.setCellValue(new HSSFRichTextString(""));

                //第5行
                HSSFRow row5 = sheet.createRow(13);
                CellRangeAddress car11 = new CellRangeAddress(13, 32, 0, 3);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car11, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car11, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car11, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car11, sheet);
                sheet.addMergedRegion(car11);
                HSSFCell cell51 = row5.createCell(0);
                cell51.setCellStyle(style4);
                List<EmScoreInfo> scoreInfoList = (List<EmScoreInfo>) map.get("scoreInfoList");
                String scoreInfoStr = "必修课：\n";
                for (EmScoreInfo item : scoreInfoList) {
                    if ("S".equals(item.getInputType())) {
                        scoreInfoStr = scoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：" + StringUtils.trimToEmpty(item.getScore()) + "（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（总评）\n";
                    } else {
                        if ("A".equals(item.getScore())) {
                            scoreInfoStr = scoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：优秀（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（总评）\n";
                        } else if ("B".equals(item.getScore())) {
                            scoreInfoStr = scoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：良好（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（总评）\n";
                        } else if ("C".equals(item.getScore())) {
                            scoreInfoStr = scoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：中等（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（总评）\n";
                        } else if ("D".equals(item.getScore())) {
                            scoreInfoStr = scoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：合格（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（总评）\n";
                        } else {
                            scoreInfoStr = scoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：不合格（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（总评）\n";
                        }
                    }
                }
                cell51.setCellValue(new HSSFRichTextString(scoreInfoStr));

                CellRangeAddress car12 = new CellRangeAddress(13, 32, 4, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car12, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car12, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car12, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car12, sheet);
                sheet.addMergedRegion(car12);
                HSSFCell cell52 = row5.createCell(4);
                cell52.setCellStyle(style4);
                List<EmScoreInfo> optionalScoreInfoList = (List<EmScoreInfo>) map.get("optionalScoreInfoList");
                String optionalScoreInfoStr = "选修课：\n";
                for (EmScoreInfo item : optionalScoreInfoList) {
                    if ("S".equals(item.getInputType())) {
                        optionalScoreInfoStr = optionalScoreInfoStr + item.getSubjectName() + "（" + item.getCourseTypeName() + "）：" + item.getToScore() + "学分\n";
                    } else {
                        if ("A".equals(item.getScore())) {
                            optionalScoreInfoStr = optionalScoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：优秀（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（学分）\n";
                        } else if ("B".equals(item.getScore())) {
                            optionalScoreInfoStr = optionalScoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：良好（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（学分）\n";
                        } else if ("C".equals(item.getScore())) {
                            optionalScoreInfoStr = optionalScoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：中等（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（学分）\n";
                        } else if ("D".equals(item.getScore())) {
                            optionalScoreInfoStr = optionalScoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：合格（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（学分）\n";
                        } else {
                            optionalScoreInfoStr = optionalScoreInfoStr + StringUtils.trimToEmpty(item.getSubjectName()) + "：不合格（考试），" + StringUtils.trimToEmpty(item.getToScore()) + "（学分）\n";
                        }
                    }
                }
                cell52.setCellValue(new HSSFRichTextString(optionalScoreInfoStr));

                //第6行
                HSSFRow row6 = sheet.createRow(33);
                CellRangeAddress car13 = new CellRangeAddress(33, 35, 0, 1);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car13, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car13, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car13, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car13, sheet);
                sheet.addMergedRegion(car13);
                HSSFCell cell61 = row6.createCell(0);
                cell61.setCellStyle(style2);
                cell61.setCellValue(new HSSFRichTextString("期末评语"));

                CellRangeAddress car14 = new CellRangeAddress(33, 35, 2, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car14, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car14, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car14, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car14, sheet);
                sheet.addMergedRegion(car14);
                HSSFCell cell62 = row6.createCell(2);
                cell62.setCellStyle(style);
                String remark = "";
                if (null == map.get("remark")) {
                    remark = "";
                } else {
                    remark = String.valueOf(map.get("remark"));
                }
                cell62.setCellValue(new HSSFRichTextString("" + remark));

                //第7行
                HSSFRow row7 = sheet.createRow(36);
                CellRangeAddress car15 = new CellRangeAddress(36, 38, 0, 5);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car15, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car15, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car15, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car15, sheet);
                sheet.addMergedRegion(car15);
                HSSFCell cell71 = row7.createCell(0);
                cell71.setCellStyle(style2);
                cell71.setCellValue(new HSSFRichTextString("备注："));
                ;

                CellRangeAddress car16 = new CellRangeAddress(36, 38, 6, 7);
                RegionUtil.setBorderBottom(BorderStyle.THIN, car16, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, car16, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, car16, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, car16, sheet);
                sheet.addMergedRegion(car16);
                HSSFCell cell72 = row7.createCell(6);
                cell72.setCellStyle(style);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(new Date());
                cell72.setCellValue(new HSSFRichTextString(schName + "教务处\n" + dateString));
                excels.put(student.getStudentName() + "成绩", workbook);
                map.clear();
            }
            String className = "";
            if (StringUtils.isNotBlank(classId)) {
                className = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class).getClassNameDynamic();
            }
            ZipUtils.makeZip(excels, className + "学生成绩单", response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Clazz> setClassName(List<Clazz> classList) {
        if (CollectionUtils.isNotEmpty(classList)) {
            Set<String> gradeIds = new HashSet<String>();
            for (Clazz clazz : classList) {
                gradeIds.add(clazz.getGradeId());
            }
            List<Grade> gradeList = SUtils.dt((gradeRemoteService.findListByIds(gradeIds.toArray(new String[0]))), new TR<List<Grade>>() {
            });
            Map<String, String> gradeNameMap = new HashMap<String, String>();
            for (Grade grade : gradeList) {
                gradeNameMap.put(grade.getId(), grade.getGradeName());
            }
            for (Clazz clazz : classList) {
                clazz.setClassNameDynamic(gradeNameMap.get(clazz.getGradeId()) + clazz.getClassName());
            }
        }
        return classList;
    }
}
