package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmStudentDto;
import net.zdsoft.exammanage.data.dto.ExamArrangeDto;
import net.zdsoft.exammanage.data.dto.InvigilateTeacherDto;
import net.zdsoft.exammanage.data.dto.InvigilatorReportDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage")
public class EmExamReportAction extends EmExamCommonAction {

    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmStudentGroupService emStudentGroupService;
    @Autowired
    private StorageDirRemoteService storageDirRemoteService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmPlaceGroupService emPlaceGroupService;

    @RequestMapping("/examReport/index/page")
    @ControllerInfo(value = "考场报表")
    public String showIndex(ModelMap map) {
        return "/exammanage/examReport/examReportIndex.ftl";
    }

    @RequestMapping("/examReport/head/page")
    @ControllerInfo(value = "考场报表查看")
    public String showReportHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examReport/examReportHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examReport/list1/page")
    @ControllerInfo("30天考试列表")
    public String showReportIn(ModelMap map, HttpSession httpSession) {
        if ("xhzx".equals(sysOptionRemoteService.findValue("SYSTEM.DEPLOY.SCHOOL"))) {
            map.put("isXhzx", true);
        }
        String url = "/exammanage/examReport/examReportList.ftl";
        return showExamInfoIn(map, httpSession, url, true);

    }

    @RequestMapping("/examReport/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showReportBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        if ("xhzx".equals(sysOptionRemoteService.findValue("SYSTEM.DEPLOY.SCHOOL"))) {
            map.put("isXhzx", true);
        }
        String url = "/exammanage/examReport/examReportList.ftl";
        return showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
    }

    @RequestMapping("/examReport/showItemIndex/page")
    @ControllerInfo("tab")
    public String showReportTab(String examId, String type, ModelMap map, HttpSession httpSession) {
        if ("xhzx".equals(sysOptionRemoteService.findValue("SYSTEM.DEPLOY.SCHOOL"))) {
            map.put("isXhzx", true);
        }
        String url = "/exammanage/examReport/reportTabIndex.ftl";
        return showTabIndex(examId, type, map, httpSession, url);
    }

    @RequestMapping("/examReport/examPlaceList/page")
    @ControllerInfo("考场安排")
    public String showExamPlaceList(String examId, HttpServletRequest request, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            if (CollectionUtils.isEmpty(groupList)) {
                return "/exammanage/examReport/examPlaceList.ftl";
            }
            String groupId = request.getParameter("groupId");
            if (StringUtils.isBlank(groupId)) {
                groupId = groupList.get(0).getId();
            }
            map.put("groupId", groupId);
            map.put("groupList", groupList);

//			List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId,true);
            List<EmPlaceGroup> placeGroupList = emPlaceGroupService.findByGroupIdAndSchoolId(groupId, unitId);//52023460485945013664141221473013，AFC82D3005F74D4983B5235A715A106A
            Set<String> placeIds = EntityUtils.getSet(placeGroupList, EmPlaceGroup::getExamPlaceId);
            if (CollectionUtils.isNotEmpty(placeIds)) {
                List<EmPlace> placeList = emPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
                emPlaceService.makePlaceName(placeList);
                //组装考生数
                findArrangeStuNum(examId, unitId, placeList, groupId);
                map.put("emPlaceList", placeList);
            }
        } else {
            map.put("isgk", "0");
            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
            //组装考生数
            findArrangeStuNum(examId, unitId, emPlaceList, null);
            map.put("emPlaceList", emPlaceList);
        }

        map.put("examId", examId);
        return "/exammanage/examReport/examPlaceList.ftl";
    }

    @RequestMapping("/examReport/studentPlaceList/page")
    @ControllerInfo("考场内学生")
    public String showStudentPlaceList(String emPlaceId, HttpServletRequest request, ModelMap map, HttpSession httpSession) {
        EmPlace emPlace = emPlaceService.findByEmPlaceId(emPlaceId);
        if (emPlace == null) {
            return errorFtl(map, "考场不存在");
        }
        String groupId = request.getParameter("groupId");
        if (StringUtils.isNotBlank(groupId)) {
            EmSubGroup g = emSubGroupService.findOne(groupId);
            map.put("groupName", g.getGroupName());
        }
        makeTableDtoList(emPlaceId, map, groupId);
        map.put("emPlace", emPlace);

        return "/exammanage/examReport/placeStudentList.ftl";
    }

    @RequestMapping("/examReport/exportAllPlace")
    @ControllerInfo(value = "导出考场内学生")
    public String exportResult(String examId, HttpSession httpSession, HttpServletResponse response) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String fileName = "考场安排明细";
        if (examInfo != null) {
            fileName = examInfo.getExamName() + "考场安排明细";
        }

        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        if (CollectionUtils.isEmpty(emPlaceList)) {
            return "";
        }
        List<EmPlaceStudent> list = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
        Map<String, List<EmPlaceStudent>> map = new HashMap<>();
        Set<String> stuIds = new HashSet<>();
        Map<String, Student> studentMap = new HashMap<>();
        Map<String, String> classMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            if (CollectionUtils.isNotEmpty(emPlaceList)) {
                for (EmPlaceStudent ps : list) {
                    if (!map.containsKey(ps.getExamPlaceId())) {
                        map.put(ps.getExamPlaceId(), new ArrayList<EmPlaceStudent>());
                    }
                    map.get(ps.getExamPlaceId()).add(ps);
                }
            }

            stuIds = EntityUtils.getSet(list, "studentId");
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentMap = EntityUtils.getMap(studentList, "id");
                Set<String> classIds = EntityUtils.getSet(studentList, "classId");
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });

                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }

        }

        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
        Map<String, List<String>> fieldTitleMap = new HashMap<>();
        List<String> tis = new ArrayList<String>();
        tis.add("座位号");
        tis.add("考场");
        tis.add("姓名");
        tis.add("考号");
        tis.add("行政班");
        tis.add("学号");
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        fieldTitleMap.put("考场信息", tis);
        for (EmPlace pp : emPlaceList) {
            //sheetName2RecordListMap.put(pp.getExamPlaceCode()+"考场", new ArrayList<Map<String,String>>());
            //fieldTitleMap.put(pp.getExamPlaceCode()+"考场", tis);
            List<EmPlaceStudent> ll = map.get(pp.getId());
            Map<String, String> conMap = null;
            if (CollectionUtils.isNotEmpty(ll)) {
                for (EmPlaceStudent ss : ll) {
                    conMap = new HashMap<String, String>();
                    conMap.put("座位号", ss.getSeatNum());
                    conMap.put("考场", pp.getExamPlaceCode() + "考场");
                    conMap.put("考号", ss.getExamNumber());
                    Student stu = studentMap.get(ss.getStudentId());
                    if (stu != null) {
                        conMap.put("姓名", stu.getStudentName());
                        conMap.put("学号", stu.getStudentCode());
                        if (classMap.containsKey(stu.getClassId())) {
                            conMap.put("行政班", classMap.get(stu.getClassId()));
                        } else {
                            conMap.put("行政班", "");
                        }
                    } else {
                        conMap.put("姓名", "");
                        conMap.put("行政班", "");
                        conMap.put("学号", "");
                    }
                    datas.add(conMap);
                }
            }

        }
        sheetName2RecordListMap.put("考场信息", datas);
        ExportUtils ex = ExportUtils.newInstance();
        ex.exportXLSFile(fileName, fieldTitleMap, sheetName2RecordListMap, response);
        return "";
    }

    @RequestMapping("/examReport/tableIndex/export")
    @ControllerInfo(value = "桌贴导出（单张）")
    public void tableExPort(String examId, String placeId, String groupId, HttpServletRequest request, HttpSession httpSession, HttpServletResponse response) throws IOException {
        //TODO
        EmPlace emplace = emPlaceService.findOne(placeId);
        StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
//		dir.setDir("d:\\1111导出");
        Map<String, String> parMap = new HashMap<>();
        parMap.put("Landscape", String.valueOf(false));//参数设定
        String uu = dir.getDir() + File.separator + "examSchReports" + File.separator + examId + File.separator +
                emplace.getSchoolId() + File.separator + "桌贴导出";
        if (StringUtils.isNotBlank(groupId)) {
            EmSubGroup group = emSubGroupService.findOne(groupId);
            uu = uu + File.separator + group.getGroupName() + File.separator + emplace.getExamPlaceCode().trim() + "考场.pdf";
        } else {
            groupId = "";
            uu = uu + File.separator + emplace.getExamPlaceCode().trim() + "考场.pdf";
        }
        File f = new File(uu);
        String urlStr = "/exammanage/examReoprt/tableList/page?exType=1&groupId=" + groupId + "&examPlaceId=" + emplace.getId();
        if (f.exists()) {
            f.delete();//删除
            HtmlToPdf.convertFin(new String[]{UrlUtils.getPrefix(request) + urlStr}, uu, null, null, 2000, parMap);
        } else {
            HtmlToPdf.convertFin(new String[]{UrlUtils.getPrefix(request) + urlStr}, uu, null, null, 2000, parMap);
        }
        File file = new File(uu);
        //如果文件不存在
        if (!file.exists()) {
            return;
        }
        //设置响应头，控制浏览器下载该文件
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(uu);
        //创建输出流
        OutputStream out = response.getOutputStream();
        //缓存区
        byte buffer[] = new byte[1024];
        int len = 0;
        //循环将输入流中的内容读取到缓冲区中
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        //关闭
        in.close();
        out.close();

    }

    @ResponseBody
    @RequestMapping("/examReport/tableIndex/getNeedExport")
    @ControllerInfo("校验")
    public String verificationData(final String examId, ModelMap map, HttpSession httpSession) {
        String unitId = this.getLoginInfo().getUnitId();
        String needExport = RedisUtils.get("tableIndex" + examId + unitId);
        JSONObject jsonObject = new JSONObject();
        ;
        if (!"1".equals(needExport)) {
            needExport = "0";
        }
        jsonObject.put("needExport", needExport);
        return JSON.toJSONString(jsonObject);
    }

    @RequestMapping("/examReport/tableIndex/batchExport")
    @ControllerInfo(value = "桌贴导出（批量）")
    public void tableBatchPort(String examId, HttpServletRequest request, HttpSession httpSession, HttpServletResponse response) {
        String unitId = this.getLoginInfo().getUnitId();
        String needExport = RedisUtils.get("tableIndex" + examId + unitId);
        if ("1".equals(needExport)) {
            System.out.println("正在导出中");
        } else {
            RedisUtils.set("tableIndex" + examId + unitId, "1");
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
            //		dir.setDir("d:\\1111导出");
            Vector<Thread> vectors = new Vector<>();
            String sourcePath = dir.getDir() + File.separator + "examSchReports" + File.separator + examId + File.separator +
                    unitId + File.separator + "桌贴导出";
            List<EmPlace> emPlaceList = new ArrayList<>();
            if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
                List<EmPlaceGroup> gPlaces = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
                Set<String> placeIds = EntityUtils.getSet(gPlaces, EmPlaceGroup::getExamPlaceId);
                Map<String, EmSubGroup> groupMap = EntityUtils.getMap(emSubGroupService.findListByExamId(examId), EmSubGroup::getId);
                Map<String, EmPlace> emMap = emPlaceService.findMapByIdIn(placeIds.toArray(new String[0]));
                EmPlace emPlace = null;
                for (EmPlaceGroup emPlaceGroup : gPlaces) {
                    emPlace = new EmPlace();
                    EmPlace e = emMap.get(emPlaceGroup.getExamPlaceId());
                    EntityUtils.copyProperties(e, emPlace);
                    emPlace.setGroupId(emPlaceGroup.getGroupId());
                    emPlace.setGroupName(groupMap.get(emPlaceGroup.getGroupId()).getGroupName());
                    emPlaceList.add(emPlace);
                }
            } else {
                emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
            }

            if (CollectionUtils.isNotEmpty(emPlaceList)) {
                List<List<EmPlace>> resultList = null;
                if (emPlaceList.size() < 500) {
                    resultList = averageAssign(emPlaceList, 5);//创建5个线程
                } else {
                    //创建20个线程
                    resultList = averageAssign(emPlaceList, 20);
                }
                for (List<EmPlace> result : resultList) {
                    MyThread myThread = new MyThread();
                    myThread.setPlaceList(result);
                    myThread.setServerUrl(UrlUtils.getPrefix(request));
                    myThread.setExamId(examId);
                    Thread thread = new Thread(myThread);
                    vectors.add(thread);
                    thread.start();
                }
            }
            for (Thread thread : vectors) {
                try {
                    thread.join();//使用join来保证thread的多个线程都执行完后，才执行主线程
                } catch (InterruptedException e) {
                    RedisUtils.del("tableIndex" + examId + unitId);
                    e.printStackTrace();
                }
            }
            try {
                String zipPath = ZipUtils.makeZipWithFile(sourcePath);
                File file = new File(zipPath);
                //如果文件不存在
                if (!file.exists()) {
                    return;
                }
                //设置响应头，控制浏览器下载该文件
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                //读取要下载的文件，保存到文件输入流
                FileInputStream in = new FileInputStream(zipPath);
                //创建输出流
                OutputStream out = response.getOutputStream();
                //缓存区
                byte buffer[] = new byte[1024];
                int len = 0;
                //循环将输入流中的内容读取到缓冲区中
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                //关闭

                in.close();
                out.close();
            } catch (IOException e) {
                RedisUtils.del("tableIndex" + examId + unitId);
                e.printStackTrace();
            }
            RedisUtils.del("tableIndex" + examId + unitId);
        }
//		exportZip(response,sourcePath, fileName,examInfo.getIsgkExamType());
    }

    /**
     * 均分list
     *
     * @param source
     * @param n
     * @return
     */
    public List<List<EmPlace>> averageAssign(List<EmPlace> source, int n) {
        List<List<EmPlace>> result = new ArrayList<List<EmPlace>>();
        if (source.size() >= n) {
            int remaider = source.size() % n;  //(先计算出余数)
            int number = source.size() / n;  //然后是商
            int offset = 0;//偏移量
            for (int i = 0; i < n; i++) {
                List<EmPlace> value = null;
                if (remaider > 0) {
                    value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                    remaider--;
                    offset++;
                } else {
                    value = source.subList(i * number + offset, (i + 1) * number + offset);
                }
                result.add(value);
            }
        } else {
            result.add(source);
        }
        return result;
    }

    @RequestMapping("/examReport/classIndex/export")
    @ControllerInfo(value = "班级清单导出（单张）")
    public void classExPort(String examId, String classId, String groupId, HttpSession httpSession, HttpServletResponse response) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Set<String> clsIds = new HashSet<>();
        clsIds.add(classId);
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Map<String, List<EmStudentDto>> stuDtoMap = getClassDtoMap(examId, clsIds, groupId, unitId, filterMap);
        List<EmStudentDto> dtolist = stuDtoMap.get(classId);
        List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(clsIds.toArray(new String[]{})), new TR<List<Clazz>>() {
        });
        Map<String, EmPlace> empMap = emPlaceService.findByUnitIdAndExamIdMap(unitId, examId);
        Map<String, String> exNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
        Map<String, List<String>> fieldTitleMap = new HashMap<>();
        List<String> tis = new ArrayList<>();
        tis.add("学号");
        tis.add("姓名");
        tis.add("班级");
        tis.add("考号");
        tis.add("考场编号");
        tis.add("考试场地");
        tis.add("座位号");
        List<Map<String, String>> datas = new ArrayList<>();
        String groupName = "";
        if (StringUtils.isNotBlank(groupId)) {
            EmSubGroup group = emSubGroupService.findOne(groupId);
            groupName = group.getGroupName();
        }
        fieldTitleMap.put(groupName + classList.get(0).getClassNameDynamic() + "班级清单", tis);
        if (CollectionUtils.isNotEmpty(dtolist)) {
            for (EmStudentDto dto : dtolist) {
                Map<String, String> conMap = new HashMap<>();
                conMap.put("学号", dto.getStudent().getStudentCode());
                conMap.put("姓名", dto.getStudent().getStudentName());
                conMap.put("班级", classList.get(0).getClassNameDynamic());
                conMap.put("考号", exNumMap.get(dto.getStudentId()));
                conMap.put("考场编号", empMap.get(dto.getEmPlaceId()).getExamPlaceCode());
                conMap.put("考试场地", empMap.get(dto.getEmPlaceId()).getPlaceName());
                conMap.put("座位号", dto.getSeatNum());
                datas.add(conMap);
            }
        }
        sheetName2RecordListMap.put(groupName + classList.get(0).getClassNameDynamic() + "班级清单", datas);
        ExportUtils ex = ExportUtils.newInstance();
        ex.exportXLSFile(groupName + classList.get(0).getClassNameDynamic() + "班级清单", fieldTitleMap, sheetName2RecordListMap, response);
    }

    @RequestMapping("/examReport/classIndex/batchExport")
    @ControllerInfo(value = "班级清单导出（批量）")
    public void classBatchPort(String examId, HttpSession httpSession, HttpServletResponse response) {
        EmExamInfo exam = emExamInfoService.findOne(examId);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        List<Clazz> clslist = SUtils.dt(classRemoteService.findBySchoolId(unitId), new TR<List<Clazz>>() {
        });
        Set<String> clsIds = EntityUtils.getSet(clslist, Clazz::getId);
        Map<String, EmPlace> empMap = emPlaceService.findByUnitIdAndExamIdMap(unitId, examId);
        Map<String, String> exNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        List<String> tis = new ArrayList<>();
        tis.add("学号");
        tis.add("姓名");
        tis.add("班级");
        tis.add("考号");
        tis.add("考场编号");
        tis.add("考试场地");
        tis.add("座位号");
        Map<String, HSSFWorkbook> excels = new HashMap<>();
        if (StringUtils.equals(exam.getIsgkExamType(), "1")) {
            List<EmSubGroup> groups = emSubGroupService.findListByExamId(examId);
            for (EmSubGroup g : groups) {
                Map<String, List<EmStudentDto>> dtosMap = getClassDtoMap(examId, clsIds, g.getId(), unitId, filterMap);
                for (Clazz cls : clslist) {
                    if (!dtosMap.containsKey(cls.getId())) {
                        continue;
                    }
                    //一个考场一个文件
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    // HSSFCell----单元格样式
                    HSSFCellStyle style = workbook.createCellStyle();
                    HSSFFont headfont = workbook.createFont();
                    headfont.setFontHeightInPoints((short) 9);// 字体大小
                    headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
                    style.setFont(headfont);
                    style.setAlignment(HorizontalAlignment.LEFT);//水平
                    style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
                    style.setWrapText(true);//自动换行

                    HSSFSheet sheet = workbook.createSheet();
                    int size = tis.size();
                    HSSFRow rowTitle = sheet.createRow(0);
                    //高度：3倍默认高度
                    rowTitle.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
                    for (int j = 0; j < size; j++) {
                        HSSFCell cell = rowTitle.createCell(j);
                        cell.setCellValue(new HSSFRichTextString(tis.get(j)));
                        cell.setCellStyle(style);
                    }
                    List<EmStudentDto> dtos = dtosMap.get(cls.getId());
                    int row = 1;
                    for (EmStudentDto dto : dtos) {
                        HSSFRow rowSF = sheet.createRow(row++);
                        HSSFCell cell0 = rowSF.createCell(0);
                        cell0.setCellValue(new HSSFRichTextString(dto.getStudent().getStudentCode()));
                        HSSFCell cell1 = rowSF.createCell(1);
                        cell1.setCellValue(new HSSFRichTextString(dto.getStudent().getStudentName()));
                        HSSFCell cell2 = rowSF.createCell(2);
                        cell2.setCellValue(new HSSFRichTextString(cls.getClassNameDynamic()));
                        HSSFCell cell3 = rowSF.createCell(3);
                        cell3.setCellValue(new HSSFRichTextString(exNumMap.get(dto.getStudentId())));
                        HSSFCell cell4 = rowSF.createCell(4);
                        cell4.setCellValue(new HSSFRichTextString(empMap.get(dto.getEmPlaceId()).getExamPlaceCode()));
                        HSSFCell cell5 = rowSF.createCell(5);
                        cell5.setCellValue(new HSSFRichTextString(empMap.get(dto.getEmPlaceId()).getPlaceName()));
                        HSSFCell cell6 = rowSF.createCell(6);
                        cell6.setCellValue(new HSSFRichTextString(dto.getSeatNum()));
                    }
                    excels.put(g.getGroupName() + "/" + cls.getClassNameDynamic() + "班级清单", workbook);
                }
            }
        } else {
            //非高考
            Map<String, List<EmStudentDto>> stuDtoMap = getClassDtoMap(examId, clsIds, null, unitId, filterMap);
            for (Clazz cls : clslist) {
                if (!stuDtoMap.containsKey(cls.getId())) {
                    continue;
                }
                List<EmStudentDto> dtos = stuDtoMap.get(cls.getId());
                //一个考场一个文件
                HSSFWorkbook workbook = new HSSFWorkbook();
                // HSSFCell----单元格样式
                HSSFCellStyle style = workbook.createCellStyle();
                HSSFFont headfont = workbook.createFont();
                headfont.setFontHeightInPoints((short) 9);// 字体大小
                headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
                style.setFont(headfont);
                style.setAlignment(HorizontalAlignment.LEFT);//水平
                style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
                style.setWrapText(true);//自动换行

                HSSFSheet sheet = workbook.createSheet();
                int size = tis.size();
                HSSFRow rowTitle = sheet.createRow(0);
                //高度：3倍默认高度
                rowTitle.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
                for (int j = 0; j < size; j++) {
                    HSSFCell cell = rowTitle.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(tis.get(j)));
                    cell.setCellStyle(style);
                }
                int row = 1;
                for (EmStudentDto dto : dtos) {
                    HSSFRow rowSF = sheet.createRow(row++);
                    HSSFCell cell0 = rowSF.createCell(0);
                    cell0.setCellValue(new HSSFRichTextString(dto.getStudent().getStudentCode()));
                    HSSFCell cell1 = rowSF.createCell(1);
                    cell1.setCellValue(new HSSFRichTextString(dto.getStudent().getStudentName()));
                    HSSFCell cell2 = rowSF.createCell(2);
                    cell2.setCellValue(new HSSFRichTextString(cls.getClassNameDynamic()));
                    HSSFCell cell3 = rowSF.createCell(3);
                    cell3.setCellValue(new HSSFRichTextString(exNumMap.get(dto.getStudentId())));
                    HSSFCell cell4 = rowSF.createCell(4);
                    cell4.setCellValue(new HSSFRichTextString(empMap.get(dto.getEmPlaceId()).getExamPlaceCode()));
                    HSSFCell cell5 = rowSF.createCell(5);
                    cell5.setCellValue(new HSSFRichTextString(empMap.get(dto.getEmPlaceId()).getPlaceName()));
                    HSSFCell cell6 = rowSF.createCell(6);
                    cell6.setCellValue(new HSSFRichTextString(dto.getSeatNum()));
                }
                excels.put(cls.getClassNameDynamic() + "班级清单", workbook);
            }
        }
        ZipUtils.makeZip(excels, exam.getExamName() + "班级清单", response);
    }

    private Map<String, List<EmStudentDto>> getClassDtoMap(String examId, Set<String> classIds,
                                                           String emSubGroupId, String unitId, Map<String, String> filterMap) {
        Map<String, List<EmStudentDto>> map = new HashMap<>();
        List<Student> studentList = new ArrayList<>();
        //行政班
        if (StringUtils.isNotBlank(emSubGroupId)) {
            List<EmStudentGroup> emStudentGroup = emStudentGroupService.findByGroupIdAndExamIdAndSchoolId(emSubGroupId, examId, unitId);
            Set<String> groupStuIds = emStudentGroup.stream().map(EmStudentGroup::getStudentId).collect(Collectors.toSet());
            List<Student> studentListAll = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(groupStuIds)) {
                studentListAll.forEach(stu -> {
                    if (groupStuIds.contains(stu.getId())) {
                        studentList.add(stu);
                    }
                });
            }
        } else {
            studentList.addAll(SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
            }));
        }
        EmStudentDto dto;
        if (CollectionUtils.isNotEmpty(studentList)) {
            List<EmPlaceStudent> plist = new ArrayList<>();
            if (StringUtils.isBlank(emSubGroupId)) {
                plist = emPlaceStudentService.findByExamIdStuIds(examId, EntityUtils.getSet(studentList, Student::getId).toArray(new String[0]));
            } else {
                plist = emPlaceStudentService.findByExamIdAndGroupId(examId, emSubGroupId);
            }
            Map<String, EmPlaceStudent> stuPlaceMap = EntityUtils.getMap(plist, EmPlaceStudent::getStudentId);
            for (Student stu : studentList) {
                if (!stuPlaceMap.containsKey(stu.getId())) {
                    continue;
                }
                dto = new EmStudentDto();
                dto.setStudentId(stu.getId());
                dto.setStudent(stu);
                dto.setEmPlaceId(stuPlaceMap.get(stu.getId()).getExamPlaceId());
                dto.setSeatNum(stuPlaceMap.get(stu.getId()).getSeatNum());
                //排考
                if (!filterMap.containsKey(stu.getId())) {
                    if (!map.containsKey(stu.getClassId())) {
                        map.put(stu.getClassId(), new ArrayList<>());
                    }
                    map.get(stu.getClassId()).add(dto);
                }
            }
        }
        for (List<EmStudentDto> stuDtoList : map.values()) {
            Collections.sort(stuDtoList, new Comparator<EmStudentDto>() {
                @Override
                public int compare(EmStudentDto o1, EmStudentDto o2) {
                	if(o1.getStudent().getStudentCode() == null) {
                		return -1;
                	}else if(o2.getStudent().getStudentCode() == null) {
                		return 1;
                	}else {
                		return o1.getStudent().getStudentCode().compareTo(o2.getStudent().getStudentCode());
                	}
                }
            });
        }
        return map;
    }

    @RequestMapping("/examReport/doorIndex/export")
    @ControllerInfo(value = "考场门贴导出（单张）")
    public void doorExPort(String examId, String placeId, String groupId, HttpSession httpSession, HttpServletResponse response) {
        EmPlace emPlace = emPlaceService.findByEmPlaceId(placeId);
        if (emPlace == null) {
            return;
        }
        List<EmStudentDto> dtoList = getDoorList(groupId, placeId);
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<>();
        Map<String, List<String>> fieldTitleMap = new HashMap<>();
        List<String> tis = new ArrayList<>();
        tis.add("座位号");
        tis.add("姓名");
        tis.add("考号");
        tis.add("行政班");
        tis.add("学号");
        List<Map<String, String>> datas = new ArrayList<>();
        String groupName = "";
        if (StringUtils.isNotBlank(groupId)) {
            EmSubGroup group = emSubGroupService.findOne(groupId);
            groupName = group.getGroupName();
        }
        fieldTitleMap.put(groupName + emPlace.getExamPlaceCode() + "考场门贴", tis);
        for (EmStudentDto dto : dtoList) {
            Map<String, String> conMap = new HashMap<>();
            conMap.put("座位号", dto.getSeatNum());
            conMap.put("姓名", dto.getStudent().getStudentName());
            conMap.put("考号", dto.getExamNumber());
            conMap.put("行政班", dto.getClassName());
            conMap.put("学号", dto.getStudent().getStudentCode());
            datas.add(conMap);
        }
        sheetName2RecordListMap.put(groupName + emPlace.getExamPlaceCode() + "考场门贴", datas);
        ExportUtils ex = ExportUtils.newInstance();
        ex.exportXLSFile(groupName + emPlace.getExamPlaceCode() + "考场门贴", fieldTitleMap, sheetName2RecordListMap, response);
    }

    @RequestMapping("/examReport/doorIndex/batchExport")
    @ControllerInfo(value = "考场门贴导出（批量）")
    public void doorBatchPort(String examId, HttpSession httpSession, HttpServletResponse response) {
        EmExamInfo exam = emExamInfoService.findOne(examId);
        String unitId = this.getLoginInfo().getUnitId();
        Map<String, Student> stuMap = EntityUtils.getMap(SUtils.dt(studentRemoteService.findBySchoolId(unitId), new TR<List<Student>>() {
        }), Student::getId);
        Map<String, Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findBySchoolId(unitId), new TR<List<Clazz>>() {
        }), Clazz::getId);
        List<String> titleList = new ArrayList<>();
        titleList.add("座位号");
        titleList.add("姓名");
        titleList.add("考号");
        titleList.add("行政班");
        titleList.add("学号");
        Map<String, HSSFWorkbook> excels = new HashMap<>();
        if (StringUtils.equals(exam.getIsgkExamType(), "1")) {
            //高考模式
            List<EmSubGroup> groups = emSubGroupService.findListByExamId(examId);
            List<EmPlaceGroup> gPlaces = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
            Map<String, EmPlace> placeMap = EntityUtils.getMap(emPlaceService.findByExamId(examId), EmPlace::getId);
            Map<String, Set<String>> gPlaceIdsMap = new HashMap<>();
            for (EmPlaceGroup e : gPlaces) {
                if (!gPlaceIdsMap.containsKey(e.getGroupId())) {
                    gPlaceIdsMap.put(e.getGroupId(), new HashSet<>());
                }
                gPlaceIdsMap.get(e.getGroupId()).add(e.getExamPlaceId());
            }
            for (EmSubGroup g : groups) {
                if (!gPlaceIdsMap.containsKey(g.getId())) {
                    continue;
                }
                Set<String> placeIds = gPlaceIdsMap.get(g.getId());
                Map<String, List<EmStudentDto>> dtosMap = getDoorList2(g.getId(), placeIds.toArray(new String[0]));
                for (String placeId : placeIds) {
                    //一个考场一个文件
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    // HSSFCell----单元格样式
                    HSSFCellStyle style = workbook.createCellStyle();
                    HSSFFont headfont = workbook.createFont();
                    headfont.setFontHeightInPoints((short) 9);// 字体大小
                    headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
                    style.setFont(headfont);
                    style.setAlignment(HorizontalAlignment.CENTER);//水平
                    style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
                    style.setWrapText(true);//自动换行

                    HSSFSheet sheet = workbook.createSheet();
                    int size = titleList.size();
                    HSSFRow rowTitle = sheet.createRow(0);
                    //高度：3倍默认高度
                    rowTitle.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
                    for (int j = 0; j < size; j++) {
                        HSSFCell cell = rowTitle.createCell(j);
                        cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
                        cell.setCellStyle(style);
                    }
                    if (!dtosMap.containsKey(placeId)) {
                        continue;
                    }
                    List<EmStudentDto> dtos = dtosMap.get(placeId);
                    int row = 1;
                    for (EmStudentDto dto : dtos) {
                        if (!stuMap.containsKey(dto.getStudentId())) {
                            continue;
                        }
                        Student stu = stuMap.get(dto.getStudentId());
                        Clazz cls = clsMap.get(stu.getClassId());
                        HSSFRow rowSF = sheet.createRow(row++);
                        HSSFCell cell0 = rowSF.createCell(0);
                        cell0.setCellValue(new HSSFRichTextString(dto.getSeatNum()));
                        HSSFCell cell1 = rowSF.createCell(1);
                        cell1.setCellValue(new HSSFRichTextString(stu.getStudentName()));
                        HSSFCell cell2 = rowSF.createCell(2);
                        cell2.setCellValue(new HSSFRichTextString(dto.getExamNumber()));
                        HSSFCell cell3 = rowSF.createCell(3);
                        cell3.setCellValue(new HSSFRichTextString(cls.getClassNameDynamic()));
                        HSSFCell cell4 = rowSF.createCell(4);
                        cell4.setCellValue(new HSSFRichTextString(stu.getStudentCode()));
                    }
                    EmPlace place = placeMap.get(placeId);
                    excels.put(g.getGroupName() + "/" + place.getExamPlaceCode() + "考场", workbook);
                }
            }
        } else {
            //非高考
            List<EmPlace> placeList = emPlaceService.findByExamId(examId);
            Set<String> placeIds = EntityUtils.getSet(placeList, EmPlace::getId);
            Map<String, List<EmStudentDto>> dtosMap = getDoorList2(null, placeIds.toArray(new String[0]));
            for (EmPlace emPlace : placeList) {
                if (!dtosMap.containsKey(emPlace.getId())) {
                    continue;
                }
                List<EmStudentDto> dtos = dtosMap.get(emPlace.getId());
                //一个考场一个文件
                HSSFWorkbook workbook = new HSSFWorkbook();
                // HSSFCell----单元格样式
                HSSFCellStyle style = workbook.createCellStyle();
                HSSFFont headfont = workbook.createFont();
                headfont.setFontHeightInPoints((short) 9);// 字体大小
                headfont.setColor(HSSFFont.COLOR_RED);//字体颜色
                style.setFont(headfont);
                style.setAlignment(HorizontalAlignment.LEFT);//水平
                style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直
                style.setWrapText(true);//自动换行

                HSSFSheet sheet = workbook.createSheet();
                int size = titleList.size();
                HSSFRow rowTitle = sheet.createRow(0);
                //高度：3倍默认高度
                rowTitle.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
                for (int j = 0; j < size; j++) {
                    HSSFCell cell = rowTitle.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
                    cell.setCellStyle(style);
                }
                int row = 1;
                for (EmStudentDto dto : dtos) {
                    if (!stuMap.containsKey(dto.getStudentId())) {
                        continue;
                    }
                    Student stu = stuMap.get(dto.getStudentId());
                    Clazz cls = clsMap.get(stu.getClassId());
                    HSSFRow rowSF = sheet.createRow(row++);
                    HSSFCell cell0 = rowSF.createCell(0);
                    cell0.setCellValue(new HSSFRichTextString(dto.getSeatNum()));
                    HSSFCell cell1 = rowSF.createCell(1);
                    cell1.setCellValue(new HSSFRichTextString(stu.getStudentName()));
                    HSSFCell cell2 = rowSF.createCell(2);
                    cell2.setCellValue(new HSSFRichTextString(dto.getExamNumber()));
                    HSSFCell cell3 = rowSF.createCell(3);
                    cell3.setCellValue(new HSSFRichTextString(cls.getClassNameDynamic()));
                    HSSFCell cell4 = rowSF.createCell(4);
                    cell4.setCellValue(new HSSFRichTextString(stu.getStudentCode()));
                }
                excels.put(emPlace.getExamPlaceCode() + "考场", workbook);
            }
        }
        ZipUtils.makeZip(excels, exam.getExamName() + "门贴", response);
    }

    private Map<String, List<EmStudentDto>> getDoorList2(String groupId, String[] examPlaceIds) {
        Map<String, List<EmStudentDto>> dtoMap = new HashMap<>();
        List<EmPlaceStudent> list = emPlaceStudentService.findByNewExamPlaceIdAndGroupId(groupId, examPlaceIds);
        EmStudentDto dto = null;
        for (EmPlaceStudent es : list) {
            if (!dtoMap.containsKey(es.getExamPlaceId())) {
                dtoMap.put(es.getExamPlaceId(), new ArrayList<>());
            }
            dto = new EmStudentDto();
            dto.setStudentId(es.getStudentId());
            dto.setExamNumber(es.getExamNumber());
            dto.setSeatNum(es.getSeatNum());
            dto.setSeatNumInt(Integer.parseInt(es.getSeatNum()));
            dtoMap.get(es.getExamPlaceId()).add(dto);
        }
        for (String key : dtoMap.keySet()) {
            List<EmStudentDto> dtoList = dtoMap.get(key);
            Collections.sort(dtoList, (o1, o2) -> {
                return o1.getSeatNumInt() - o2.getSeatNumInt();
            });
        }
        return dtoMap;
    }

    private List<EmStudentDto> getDoorList(String groupId, String examPlaceId) {
        List<EmPlaceStudent> list = emPlaceStudentService.findByExamPlaceIdAndGroupId(groupId, examPlaceId);
        List<EmStudentDto> dtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            EmStudentDto dto = null;
            Map<String, Student> studentMap = new HashMap<>();
            HashMap<String, String> classMap = new HashMap<>();
            Set<String> stuIds = EntityUtils.getSet(list, EmPlaceStudent::getStudentId);
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentMap = EntityUtils.getMap(studentList, Student::getId);
                Set<String> classIds = EntityUtils.getSet(studentList, Student::getClassId);
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });

                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }
            for (EmPlaceStudent es : list) {
                if (!studentMap.containsKey(es.getStudentId())) {
                    continue;
                }
                Student stu = studentMap.get(es.getStudentId());

                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }

                dto.setExamNumber(es.getExamNumber());
                dto.setSeatNum(es.getSeatNum());
                dto.setSeatNumInt(Integer.parseInt(es.getSeatNum()));
                dtoList.add(dto);
            }
            Collections.sort(dtoList, (o1, o2) -> {
                return o1.getSeatNumInt() - o2.getSeatNumInt();
            });
        }
        return dtoList;
    }

    @RequestMapping("/examReport/doorIndex/page")
    @ControllerInfo(value = "门贴")
    public String doorIndex(String examId, HttpServletRequest request, HttpSession httpSession, ModelMap map) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examInfo", examInfo);
        map.put("examId", examId);
        List<EmPlace> emPlaceList = new ArrayList<>();
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            if (CollectionUtils.isEmpty(groupList)) {
                return "/exammanage/examReport/doorIndex.ftl";
//				return errorFtl(map,"科目组不存在");
            }
            String groupId = request.getParameter("groupId");
            if (StringUtils.isBlank(groupId)) {
                groupId = groupList.get(0).getId();
            }
            map.put("groupId", groupId);
            map.put("groupList", groupList);
            List<EmPlaceGroup> placeGroupList = emPlaceGroupService.findByGroupIdAndSchoolId(groupId, unitId);
            Set<String> placeIds = EntityUtils.getSet(placeGroupList, "examPlaceId");
            if (CollectionUtils.isNotEmpty(placeIds)) {
                emPlaceList = emPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
            }
        } else {
            map.put("isgk", "0");
            emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        }

        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            String[] placeIds = EntityUtils.getList(emPlaceList, "id").toArray(new String[0]);
            String batchId = StringUtils.join(placeIds, ",");
            emPlaceService.makePlaceName(emPlaceList);
            map.put("batchId", batchId);
        }
        map.put("emPlaceList", emPlaceList);
        return "/exammanage/examReport/doorIndex.ftl";
    }

    @RequestMapping("examReoprt/doorList/page")
    @ControllerInfo(value = "门贴")
    public String doorList(String examPlaceId, HttpServletRequest request, HttpSession httpSession, ModelMap map) {
        EmPlace emPlace = emPlaceService.findByEmPlaceId(examPlaceId);
        if (emPlace == null) {
            return "/exammanage/examReport/doorList.ftl";
            //return errorFtl(map,"考场不存在");
        }
        EmExamInfo exam = emExamInfoService.findOne(emPlace.getExamId());
        if (exam != null) {
            map.put("examName", exam.getExamName());
        }
        String groupId = request.getParameter("groupId");
        if (StringUtils.isNotBlank(groupId)) {
            EmSubGroup g = emSubGroupService.findOne(groupId);
            map.put("groupName", g.getGroupName());
        }
        makeDtoList(examPlaceId, map, emPlace, groupId);
        return "/exammanage/examReport/doorList.ftl";
    }

    private void makeDtoList(String examPlaceId, ModelMap map, EmPlace emPlace, String groupId) {
        List<EmPlaceStudent> list = emPlaceStudentService.findByExamPlaceIdAndGroupId(groupId, examPlaceId);
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            emPlace.setStuNum(size);
            String s1 = list.get(0).getExamNumber();
            String s2 = list.get(size - 1).getExamNumber();
            emPlace.setStuNumRange(s1 + "-" + s2);
        }

        map.put("emPlace", emPlace);

        //学生数据隐藏
        List<EmPlaceStudent> stulist = emPlaceStudentService.findByExamPlaceIdAndGroupId(null, emPlace.getId());
        List<EmStudentDto> dtoList = new ArrayList<EmStudentDto>();
        if (CollectionUtils.isNotEmpty(stulist)) {
            EmStudentDto dto = null;
            Map<String, Student> studentMap = new HashMap<String, Student>();
            HashMap<String, String> classMap = new HashMap<String, String>();
            Set<String> stuIds = EntityUtils.getSet(list, EmPlaceStudent::getStudentId);
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentMap = EntityUtils.getMap(studentList, Student::getId);
                Set<String> classIds = EntityUtils.getSet(studentList, Student::getClassId);
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });

                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }
            for (EmPlaceStudent es : list) {
                if (!studentMap.containsKey(es.getStudentId())) {
                    continue;
                }
                Student stu = studentMap.get(es.getStudentId());

                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }

                dto.setExamNumber(es.getExamNumber());
                dto.setSeatNum(es.getSeatNum());
                dto.setSeatNumInt(Integer.parseInt(es.getSeatNum()));
                dtoList.add(dto);
            }
            Collections.sort(dtoList, (o1, o2) -> {
                return o1.getSeatNumInt() - o2.getSeatNumInt();
            });
        }
        map.put("dtoList", dtoList);
    }

    @RequestMapping("/examReoprt/doorList/onBatchPrint")
    @ControllerInfo(value = "批量打印考场门贴")
    public String onBatchPrint(String examId, String batchId, ModelMap map) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        } else {
            map.put("examName", examInfo.getExamName());
        }
        String batchIdLeft = "";
        String doNotPrint = "0";
        if (StringUtils.isNotBlank(batchId)) {
            String[] batchIds = batchId.split(",");
            int i = 0;
            for (; i < batchIds.length; i++) {
                if (StringUtils.isNotBlank(batchIds[i])) {
                    String placeId = batchIds[i];
                    EmPlace emPlace = emPlaceService.findByEmPlaceId(placeId);
                    if (emPlace == null) {
                        i++;
                        break;
                    }
                    makeDtoList(placeId, map, emPlace, null);
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
                batchIdLeft = sb.toString();
            }
        } else {
            doNotPrint = "1";
        }
        batchId = batchIdLeft;
        map.put("batchId", batchId);
        map.put("doNotPrint", doNotPrint);
        map.put("examId", examId);
        return "/exammanage/examReport/doorListExport.ftl";
    }

    @RequestMapping("/examReport/tableIndex/page")
    @ControllerInfo(value = "桌贴")
    public String tableIndex(String examId, HttpServletRequest request, HttpSession httpSession, ModelMap map) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examInfo", examInfo);
        List<EmPlace> emPlaceList = new ArrayList<>();
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            if (CollectionUtils.isEmpty(groupList)) {
                return "/exammanage/examReport/tableIndex.ftl";
            }
            String groupId = request.getParameter("groupId");
            if (StringUtils.isBlank(groupId)) {
                groupId = groupList.get(0).getId();
            }
            map.put("groupId", groupId);
            map.put("groupList", groupList);
            List<EmPlaceGroup> placeGroupList = emPlaceGroupService.findByGroupIdAndSchoolId(groupId, unitId);
            Set<String> placeIds = EntityUtils.getSet(placeGroupList, "examPlaceId");
            if (CollectionUtils.isNotEmpty(placeIds)) {
                emPlaceList = emPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
            }
        } else {
            map.put("isgk", "0");
            emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        }
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            String[] placeIds = EntityUtils.getList(emPlaceList, "id").toArray(new String[0]);
            String batchId = StringUtils.join(placeIds, ",");
            emPlaceService.makePlaceName(emPlaceList);
            map.put("batchId", batchId);
        }
        map.put("emPlaceList", emPlaceList);
        return "/exammanage/examReport/tableIndex.ftl";
    }

    @RequestMapping("examReoprt/tableList/page")
    @ControllerInfo(value = "桌贴")
    public String tableList(String examPlaceId, HttpServletRequest request, HttpSession httpSession, ModelMap map) {
        EmPlace emPlace = emPlaceService.findByEmPlaceId(examPlaceId);
        if (emPlace == null) {
            return "/exammanage/examReport/tableList.ftl";
        }
        String groupId = request.getParameter("groupId");
        if (StringUtils.isNotBlank(groupId)) {
            EmSubGroup g = emSubGroupService.findOne(groupId);
            map.put("groupName", g.getGroupName());
        }
        makeTableDtoList2(examPlaceId, map, groupId);
        map.put("emPlace", emPlace);
        String exType = request.getParameter("exType");
        if (StringUtils.equals(exType, "1")) {
            //导出pdf页面
            return "/exammanage/examReport/tableList2.ftl";
        } else {
            return "/exammanage/examReport/tableList.ftl";
        }
    }

    private void makeTableDtoList2(String examPlaceId, ModelMap map, String groupId) {
        List<EmPlaceStudent> list = emPlaceStudentService.findByNewExamPlaceIdAndGroupId(groupId, examPlaceId);
        List<EmStudentDto> dtoList = new ArrayList<EmStudentDto>();
        if (CollectionUtils.isNotEmpty(list)) {
            EmStudentDto dto = null;
            Map<String, Student> studentMap = new HashMap<>();
            HashMap<String, String> classMap = new HashMap<>();
            Set<String> stuIds = EntityUtils.getSet(list, EmPlaceStudent::getStudentId);
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentMap = EntityUtils.getMap(studentList, Student::getId);
                Set<String> classIds = EntityUtils.getSet(studentList, Student::getClassId);
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });

                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }
            for (EmPlaceStudent es : list) {
                if (!studentMap.containsKey(es.getStudentId())) {
                    continue;
                }
                Student stu = studentMap.get(es.getStudentId());

                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }
                dto.setExamNumber(es.getExamNumber());
                dto.setSeatNum(es.getSeatNum());
                dto.setSeatNumInt(Integer.parseInt(es.getSeatNum()));
                dtoList.add(dto);
            }
            Collections.sort(dtoList, (o1, o2) -> {
                return o1.getSeatNumInt() - o2.getSeatNumInt();
            });
        }
        map.put("dtoList", dtoList);
    }

    private void makeTableDtoList(String examPlaceId, ModelMap map, String groupId) {
        List<EmPlaceStudent> list = emPlaceStudentService.findByExamPlaceIdAndGroupId(groupId, examPlaceId);
        List<EmStudentDto> dtoList = new ArrayList<EmStudentDto>();
        if (CollectionUtils.isNotEmpty(list)) {
            EmStudentDto dto = null;
            Map<String, Student> studentMap = new HashMap<>();
            HashMap<String, String> classMap = new HashMap<>();
            Set<String> stuIds = EntityUtils.getSet(list, EmPlaceStudent::getStudentId);
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentList)) {
                studentMap = EntityUtils.getMap(studentList, Student::getId);
                Set<String> classIds = EntityUtils.getSet(studentList, Student::getClassId);
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });

                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }
            for (EmPlaceStudent es : list) {
                if (!studentMap.containsKey(es.getStudentId())) {
                    continue;
                }
                Student stu = studentMap.get(es.getStudentId());

                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }
                dto.setExamNumber(es.getExamNumber());
                dto.setSeatNum(es.getSeatNum());
                dto.setSeatNumInt(Integer.parseInt(es.getSeatNum()));
                dtoList.add(dto);
            }
            Collections.sort(dtoList, (o1, o2) -> {
                return o1.getSeatNumInt() - o2.getSeatNumInt();
            });
        }
        map.put("dtoList", dtoList);
    }

    @RequestMapping("/examReoprt/tableList/onBatchPrint")
    @ControllerInfo(value = "批量打印考场桌贴")
    public String toBatchPrint(String examId, String batchId, ModelMap map) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        String batchIdLeft = "";
        String doNotPrint = "0";
        if (StringUtils.isNotBlank(batchId)) {
            String[] batchIds = batchId.split(",");
            int i = 0;
            for (; i < batchIds.length; i++) {
                if (StringUtils.isNotBlank(batchIds[i])) {
                    String placeId = batchIds[i];
                    EmPlace emPlace = emPlaceService.findByEmPlaceId(placeId);
                    if (emPlace == null) {
                        i++;
                        break;
                    }
                    map.put("examPlaceCode", emPlace.getExamPlaceCode());
                    makeTableDtoList(placeId, map, null);
                    ;
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
                batchIdLeft = sb.toString();
            }
        } else {
            doNotPrint = "1";
        }
        batchId = batchIdLeft;
        map.put("batchId", batchId);
        map.put("doNotPrint", doNotPrint);
        map.put("examId", examId);
        return "/exammanage/examReport/tableListExport.ftl";
    }

    @RequestMapping("/examReport/invigilatorList/page")
    @ControllerInfo(value = "监考老师安排")
    public String invigilatorIndex(String examId, HttpSession httpSession, ModelMap map) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo emExamInfo = emExamInfoService.findExamInfoOne(examId);
        List<InvigilatorReportDto> invigilatorList = new ArrayList<InvigilatorReportDto>();
        List<EmPlaceTeacher> emplaceTeacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE1);
        Map<String, String> teacherNamesMap = new LinkedHashMap<String, String>();
        //所有在校老师
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        for (Teacher teacher : teacherInLists) {
            teacherNamesMap.put(teacher.getId(), teacher.getTeacherName());
        }
        //所有校外老师
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            teacherNamesMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
        }
        Map<String, EmPlace> emPlaceMap = new LinkedHashMap<String, EmPlace>();
        // 考场信息
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        for (EmPlace emPlace : emPlaceList) {
            emPlaceMap.put(emPlace.getId(), emPlace);
        }
        // 考试科目信息
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        // 学科名称Map
        Map<String, String> courseNamesMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseNamesMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
            }
        }
        Set<String> teacherIdsSet = new HashSet<String>();
        List<InvigilateTeacherDto> invigilateTeacherDtoList = new ArrayList<InvigilateTeacherDto>();
        if (CollectionUtils.isNotEmpty(emplaceTeacherList)) {
            for (EmPlaceTeacher emPlaceTeacher : emplaceTeacherList) {
                String teacherIds = "";
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                    teacherIds += emPlaceTeacher.getTeacherIdsIn() + ",";
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                    teacherIds += emPlaceTeacher.getTeacherIdsOut() + ",";
                }
                if (StringUtils.isBlank(teacherIds)) {
                    continue;
                }
                teacherIds = teacherIds.substring(0, teacherIds.length() - 1);
                List<String> teacherIdsList = Arrays.asList(teacherIds.split(","));
                InvigilateTeacherDto teacherDto = null;
                for (String str : teacherIdsList) {
                    teacherIdsSet.add(str);
                    teacherDto = new InvigilateTeacherDto();
                    teacherDto.setTeacherIds(str);
                    teacherDto.setTeacherNames(teacherNamesMap.get(str));
                    teacherDto.setSubjectId(emPlaceTeacher.getSubjectId());
                    teacherDto.setSubjectName(courseNamesMap.get(emPlaceTeacher.getSubjectId()));
                    teacherDto.setStartTime(emPlaceTeacher.getStartTime());
                    teacherDto.setEndTime(emPlaceTeacher.getEndTime());
                    teacherDto.setEmPlaceId(emPlaceTeacher.getExamPlaceId());
                    teacherDto.setExamPlaceCode(emPlaceMap.get(emPlaceTeacher.getExamPlaceId()).getExamPlaceCode());
                    teacherDto.setExamPlaceName(emPlaceMap.get(emPlaceTeacher.getExamPlaceId()).getPlaceName());
                    invigilateTeacherDtoList.add(teacherDto);
                }
            }
        }
        for (String str : teacherIdsSet) {
            InvigilatorReportDto invigilatorReportDto = new InvigilatorReportDto();
            List<InvigilateTeacherDto> teachReportList = new ArrayList<InvigilateTeacherDto>();
            for (InvigilateTeacherDto dto : invigilateTeacherDtoList) {
                if (str.equals(dto.getTeacherIds())) {
                    teachReportList.add(dto);
                }
            }
            invigilatorReportDto.setTeacherId(str);
            invigilatorReportDto.setTeachReportList(teachReportList);
            invigilatorList.add(invigilatorReportDto);
        }
        map.put("examName", emExamInfo.getExamName());
        map.put("invigilatorList", invigilatorList);
        return "/exammanage/examReport/invigilatorList.ftl";
    }

    @RequestMapping("/examReport/examArrangeList/page")
    @ControllerInfo(value = "考试安排总表")
    public String examArrangeList(String examId, HttpSession httpSession, ModelMap map) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo emExamInfo = emExamInfoService.findExamInfoOne(examId);
        if (StringUtils.equals(emExamInfo.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
        } else {
            map.put("isgk", "0");
        }
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        Map<String, String> teacherNamesMap = new LinkedHashMap<String, String>();
        //所有在校老师
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        for (Teacher teacher : teacherInLists) {
            teacherNamesMap.put(teacher.getId(), teacher.getTeacherName());
        }
        //所有校外老师
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            teacherNamesMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
        }
        Map<String, String> emplaceTeacherMap = new LinkedHashMap<String, String>();
        List<EmPlaceTeacher> emplaceTeacherList = emPlaceTeacherService.findByExamId(examId);
        for (EmPlaceTeacher emPlaceTeacher : emplaceTeacherList) {
            String teacherNames = "";
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                String[] teacherIdsIn = emPlaceTeacher.getTeacherIdsIn().split(",");
                for (int i = 0; i < teacherIdsIn.length; i++) {
                    teacherNames += teacherNamesMap.get(teacherIdsIn[i]) + ",";
                }
            }
            if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                String[] teacherIdsOut = emPlaceTeacher.getTeacherIdsOut().split(",");
                for (int i = 0; i < teacherIdsOut.length; i++) {
                    teacherNames += teacherNamesMap.get(teacherIdsOut[i]) + ",";
                }
            }
            if (StringUtils.isBlank(teacherNames)) {
                continue;
            }
            teacherNames = teacherNames.substring(0, teacherNames.length() - 1);
            if ("1".equals(emPlaceTeacher.getType())) {
                emplaceTeacherMap.put(emPlaceTeacher.getExamPlaceId() + emPlaceTeacher.getSubjectId(), teacherNames);
            } else {
                emplaceTeacherMap.put(emPlaceTeacher.getSubjectId(), teacherNames);
            }
        }
        List<ExamArrangeDto> examArrangeDtoList = new ArrayList<>();
        Map<String, Set<String>> subPlaceMap = new HashMap<>();
        if (StringUtils.equals(emExamInfo.getIsgkExamType(), "1")) {
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            Map<String, Set<String>> subGroupMap = new HashMap<>();
            Set<String> subIds = new HashSet<>();
            for (EmSubGroup g : groupList) {
                String[] subIdArr = g.getSubjectId().split(",");
                for (String subId : subIdArr) {
                    if (!subGroupMap.containsKey(subId)) {
                        subGroupMap.put(subId, new HashSet<>());
                    }
                    subGroupMap.get(subId).add(g.getId());
                    subIds.add(subId);
                }
            }
            List<EmPlaceGroup> placeGlist = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
            for (String subId : subIds) {
                Set<String> groupIds = subGroupMap.get(subId);
                subPlaceMap.put(subId, new HashSet<>());
                for (EmPlaceGroup pg : placeGlist) {
                    if (groupIds.contains(pg.getGroupId())) {
                        subPlaceMap.get(subId).add(pg.getExamPlaceId());
                    }
                }
            }
        }
        for (EmPlace emPlace : emPlaceList) {
            ExamArrangeDto arrangeDto = new ExamArrangeDto();
            List<String> invigilateList = new ArrayList<String>();
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                if (StringUtils.equals(emExamInfo.getIsgkExamType(), "1")) {
                    Set<String> pIds = subPlaceMap.get(emSubjectInfo.getSubjectId());
                    if (pIds.contains(emPlace.getId())) {
                        invigilateList.add(emplaceTeacherMap.get(emPlace.getId() + emSubjectInfo.getSubjectId()));
                    } else {
                        invigilateList.add("该科目无该考场");
                    }
                } else {
                    invigilateList.add(emplaceTeacherMap.get(emPlace.getId() + emSubjectInfo.getSubjectId()));
                }
            }
            arrangeDto.setExamPlaceCode(emPlace.getExamPlaceCode());
            arrangeDto.setExamPlaceName(emPlace.getPlaceName());
            arrangeDto.setInvigilateList(invigilateList);
            examArrangeDtoList.add(arrangeDto);
        }

        List<String> inspectorsTeacherList = new ArrayList<String>();
        for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
            inspectorsTeacherList.add(emplaceTeacherMap.get(emSubjectInfo.getSubjectId()));
        }
        // 学科名称Map
        Map<String, String> courseNamesMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseNamesMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
            }
        }
        Date dayTime = null;
        int index = 0;
        int sizeMax = 0;
        List<EmSubjectInfo> subList = new ArrayList<EmSubjectInfo>();
        for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
            if (emSubjectInfo.getStartDate() == null) {
                break;
            }
            emSubjectInfo.setCourseName(courseNamesMap.get(emSubjectInfo.getSubjectId()));
            if (dayTime == null) {
                dayTime = emSubjectInfo.getStartDate();
                index++;
            } else {
                String time1 = new SimpleDateFormat("yyyy-MM-dd").format(dayTime);
                String time2 = new SimpleDateFormat("yyyy-MM-dd").format(emSubjectInfo.getStartDate());
                if (time1.equals(time2)) {
                    index++;
                } else {
                    EmSubjectInfo subInfo = new EmSubjectInfo();
                    subInfo.setExamSize(String.valueOf(index));
                    String time = new SimpleDateFormat("MM").format(dayTime) + "月" + new SimpleDateFormat("dd").format(dayTime) + "日";
                    subInfo.setDaytime(time);
                    subList.add(subInfo);
                    dayTime = emSubjectInfo.getStartDate();
                    index = 1;
                }
            }
            sizeMax++;
            if (sizeMax == findSByExamIdIn.size()) {
                EmSubjectInfo subInfo = new EmSubjectInfo();
                subInfo.setExamSize(String.valueOf(index));
                String time = new SimpleDateFormat("MM").format(dayTime) + "月" + new SimpleDateFormat("dd").format(dayTime) + "日";
                subInfo.setDaytime(time);
                subList.add(subInfo);
            }
        }
        map.put("examName", emExamInfo.getExamName());
        map.put("subList", subList);
        map.put("findSByExamIdIn", findSByExamIdIn);
        map.put("examArrangeDtoList", examArrangeDtoList);
        map.put("inspectorsTeacherList", inspectorsTeacherList);
        return "/exammanage/examReport/examArrangement.ftl";
    }

    @RequestMapping("/examReport/classIndex/page")
    @ControllerInfo(value = "班级清单")
    public String classIndex(String examId, HttpSession httpSession, ModelMap map) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        EmExamInfo emExamInfo = emExamInfoService.findExamInfoOne(examId);
        String isGkExamType = emExamInfo.getIsgkExamType();
        if (StringUtils.isBlank(isGkExamType)) {
            isGkExamType = "0";
        }
        map.put("isGkExamType", isGkExamType);
        if (isGkExamType.equals("1")) {
            List<EmSubGroup> emSubGroupList = emSubGroupService.findListByExamId(examId);
            map.put("emSubGroupList", emSubGroupList);
        }
        map.put("examInfo", examInfo);
        map.put("examId", examInfo.getId());

        List<EmClassInfo> emClaList = emClassInfoService.findByExamIdAndSchoolId(examId, null);
        if (CollectionUtils.isNotEmpty(emClaList)) {
            //Set<String> claids= EntityUtils.getSet(emClaList, "classId");
            Set<String> claids = showFindClass(getLoginInfo().getUnitId(), examInfo).stream().map(Clazz::getId).collect(Collectors.toSet());
            List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(claids.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            map.put("classList", classList);
			/*Map<String,String> claMaps =EntityUtils.getMap(classList, "id", "classNameDynamic");
			Map<String,Clazz> claCodeMaps = classList.stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
			for(EmClassInfo ent:emClaList){
				String claname =claMaps.get(ent.getClassId());
				Clazz clazz = claCodeMaps.get(ent.getClassId());
				if(StringUtils.isNotBlank(claname))
					ent.setClassName(claname);
					if(clazz!=null)
					ent.setClassCode(clazz.getClassCode());
			}*/
            if (CollectionUtils.isNotEmpty(classList)) {
                String[] classIds = classList.stream().map(Clazz::getId).collect(Collectors.toSet()).toArray(new String[0]);
                String batchId = StringUtils.join(classIds, ",");
                map.put("batchId", batchId);
            }
        }
		/*Collections.sort(emClaList, (o1, o2) -> {
            int ret = Integer.parseInt(o1.getClassCode()) - Integer.parseInt(o2.getClassCode());
            return ret;
        });*/
        map.put("emClaList", emClaList);
        return "/exammanage/examReport/classIndex.ftl";
    }

    @RequestMapping("examReoprt/classList/page")
    @ControllerInfo(value = "班级清单列表")
    public String classList(String examId, String examClaId, String emSubGroupId, HttpSession httpSession, ModelMap map) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmStudentDto> stuDtoList = new ArrayList<>();
        makeClassDtoList(examId, examClaId, emSubGroupId, map, unitId, stuDtoList);
        return "/exammanage/examReport/classList.ftl";
    }

    private void makeClassDtoList(String examId, String examClaId, String emSubGroupId, ModelMap map, String unitId, List<EmStudentDto> stuDtoList) {
        List<Student> studentList = new ArrayList<>();
        //行政班
        Map<String, String> classMap = new LinkedHashMap<>();
        Set<String> classIds = new HashSet<>();
        classIds.add(examClaId);
        if (classIds.size() > 0) {
            if (!StringUtils.isBlank(emSubGroupId)) {
                List<EmStudentGroup> emStudentGroup = emStudentGroupService.findByGroupIdAndExamIdAndSchoolId(emSubGroupId, examId, unitId);
                Set<String> groupStuIds = emStudentGroup.stream().map(EmStudentGroup::getStudentId).collect(Collectors.toSet());
                List<Student> studentListAll = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
                });
                if (CollectionUtils.isNotEmpty(groupStuIds)) {
                    studentListAll.forEach(stu -> {
                        if (groupStuIds.contains(stu.getId())) {
                            studentList.add(stu);
                        }
                    });
                }
            } else {
                studentList.addAll(SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
                }));
            }
            List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classList)) {
                for (Clazz z : classList) {
                    classMap.put(z.getId(), z.getClassNameDynamic());
                }
            }
        }
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Map<String, String> exNumMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        EmStudentDto dto;
        Set<String> stuIds = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(studentList)) {
            for (Student stu : studentList) {
                dto = new EmStudentDto();
                dto.setStudent(stu);
                dto.setStudentId(stu.getId());
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }
                dto.setExamNumber(exNumMap.get(stu.getId()));
                //排考
                if (!filterMap.containsKey(stu.getId())) {
                    stuIds.add(stu.getId());
                    stuDtoList.add(dto);
                }
            }
        }
        List<EmPlaceStudent> plist = emPlaceStudentService.findByExamIdStuIds(examId, stuIds.toArray(new String[0]));
        Map<String, EmPlaceStudent> epMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(plist)) {
            Set<String> epset = EntityUtils.getSet(plist, EmPlaceStudent::getExamPlaceId);
            Map<String, EmPlace> empMap = emPlaceService.findByIdsMap(epset.toArray(new String[0]));
            for (EmPlaceStudent ent : plist) {
                EmPlace ement = empMap.get(ent.getExamPlaceId());
                if (ement != null) {
                    ent.setExPlaceName(ement.getPlaceName());
                    ent.setExPlaceCode(ement.getExamPlaceCode());
                }
                if (StringUtils.isNotBlank(emSubGroupId)) {
                    if (ent.getGroupId().equals(emSubGroupId)) {

                        epMap.put(ent.getStudentId(), ent);
                    }
                } else {
                    epMap.put(ent.getStudentId(), ent);
                }
            }
        }
        for (EmStudentDto ent : stuDtoList) {
            EmPlaceStudent em = epMap.get(ent.getStudentId());
            if (em != null) {
                ent.setExamNumber(em.getExamNumber());
                ent.setPlaceName(em.getExPlaceName());
                ent.setPlaceNumber(em.getExPlaceCode());
                ent.setSeatNum(em.getSeatNum());
            }
        }
        Collections.sort(stuDtoList, new Comparator<EmStudentDto>() {
            @Override
            public int compare(EmStudentDto o1, EmStudentDto o2) {
//                return o1.getStudent().getStudentCode().compareTo(o2.getStudent().getStudentCode());
                if(o1.getStudent().getStudentCode() == null) {
            		return -1;
            	}else if(o2.getStudent().getStudentCode() == null) {
            		return 1;
            	}else {
            		return o1.getStudent().getStudentCode().compareTo(o2.getStudent().getStudentCode());
            	}
            }
        });
        if (map != null) {
            map.put("stuDtoList", stuDtoList);
            map.put("className", classMap.get(examClaId) == null ? "" : classMap.get(examClaId));
        }
    }

    @RequestMapping("/examReoprt/classList/onBatchPrint")
    @ControllerInfo(value = "批量打印班级清单")
    public String doBatchPrint(String examId, String batchId, ModelMap map) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        LoginInfo info = getLoginInfo();
        String unitId = info.getUnitId();
        List<EmStudentDto> stuDtoList = new ArrayList<EmStudentDto>();
        String batchIdLeft = "";
        String doNotPrint = "0";
        if (StringUtils.isNotBlank(batchId)) {
            String[] batchIds = batchId.split(",");
            int i = 0;
            for (; i < batchIds.length; i++) {
                if (StringUtils.isNotBlank(batchIds[i])) {
                    String classId = batchIds[i];
					/*EmClassInfo emClassInfo = emClassInfoService.findByExamIdAndSchoolIdAndClassId(examId, unitId, classId);
					if(emClassInfo==null){
						map.put("stuDtoList", stuDtoList);
						i++;
						break;
					}*/
                    makeClassDtoList(examId, classId, "", map, unitId, stuDtoList);
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
                batchIdLeft = sb.toString();
            }
        } else {
            doNotPrint = "1";
        }
        batchId = batchIdLeft;
        map.put("batchId", batchId);
        map.put("doNotPrint", doNotPrint);
        map.put("examId", examId);
        return "/exammanage/examReport/classListExport.ftl";
    }

    class MyThread implements Runnable {
        private String serverUrl;
        private String examId;
        private List<EmPlace> placeList;
        private boolean flag = true;//是否覆盖已有的pdf

        @Override
        public void run() {
            StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
//			dir.setDir("d:\\1111导出");
            if (CollectionUtils.isNotEmpty(placeList)) {
                Map<String, String> parMap = new HashMap<>();
                parMap.put("Landscape", String.valueOf(false));//参数设定
                for (EmPlace emplace : placeList) {
                    String uu = dir.getDir() + File.separator + "examSchReports" + File.separator + examId + File.separator +
                            emplace.getSchoolId() + File.separator + "桌贴导出";
                    if (StringUtils.isNotBlank(emplace.getGroupId())) {
                        uu = uu + File.separator + emplace.getGroupName() + File.separator + emplace.getExamPlaceCode().trim() + "考场.pdf";
                    } else {
                        uu = uu + File.separator + emplace.getExamPlaceCode().trim() + "考场.pdf";
                    }
                    File f = new File(uu);
                    String urlStr = "/exammanage/examReoprt/tableList/page?exType=1&groupId=" + emplace.getGroupId() + "&examPlaceId=" + emplace.getId();
                    if (f.exists()) {
                        if (flag) {
                            f.delete();//删除
                            HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, null, 2000, parMap);
                        }
                    } else {
                        HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, null, 2000, parMap);
                    }
                }
            }
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public List<EmPlace> getPlaceList() {
            return placeList;
        }

        public void setPlaceList(List<EmPlace> placeList) {
            this.placeList = placeList;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

    }

}
