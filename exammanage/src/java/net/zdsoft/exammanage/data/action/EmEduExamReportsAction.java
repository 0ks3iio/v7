package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.EmSubDateDto;
import net.zdsoft.exammanage.data.dto.EmSubDto;
import net.zdsoft.exammanage.data.dto.EmTicketDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.HtmlToPdf;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEduExamReportsAction extends BaseAction {

    @Autowired
    EmExamInfoService emExamInfoService;
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    McodeRemoteService mcodeRemoteService;
    @Autowired
    SchoolRemoteService schoolRemoteService;
    @Autowired
    EmClassInfoService emClassInfoService;
    @Autowired
    EmPlaceStudentService emPlaceStudentService;
    @Autowired
    EmExamRegionService emExamRegionService;
    @Autowired
    EmOptionService emOptionService;
    @Autowired
    EmPlaceService emPlaceService;
    @Autowired
    StudentRemoteService studentRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private StorageDirRemoteService storageDirRemoteService;

    @RequestMapping("/examReports/index/page")
    @ControllerInfo(value = "考场报表")
    public String showIndex(ModelMap map) {
        return "/exammanage/edu/examReports/examReportsIndex.ftl";
    }

    @RequestMapping("/examReports/head/page")
    @ControllerInfo(value = "考试编排设置")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examReports/examReportsHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examReports/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examReports/examReportsList.ftl";
        return showExamInfoIn(map, httpSession, url, true);
    }

    @RequestMapping("/examReports/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examReports/examReportsList.ftl";
        return showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
    }

    @RequestMapping("/examReports/examItemIndex/page")
    @ControllerInfo("tab")
    public String showArrangeTab(String examId, String type, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/edu/examReports/examReportsTabIndex.ftl";
        return showTabIndex(examId, type, map, httpSession, url);
    }

    @RequestMapping("/examReports/exportTicketList/page")
    @ControllerInfo(value = "导出对象页面")
    public String showSchList(String examId, HttpServletRequest request, ModelMap map) {
        String type = request.getParameter("type");
        if (StringUtils.isNotBlank(examId)) {
            Set<String> clsAllIds = emPlaceStudentService.getClsSchMap(examId);
            if (CollectionUtils.isEmpty(clsAllIds)) {
                return errorFtl(map, "没有编排结果，请先编排！");
            }
            List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findListByIds(clsAllIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Set<String> schIds = new HashSet<>();
            for (Clazz cls : clsAlllist) {
                schIds.add(cls.getSchoolId());
            }
            List<School> schlist = SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
            });
            map.put("schlist", schlist);
        }
        map.put("examId", examId);
        map.put("type", type);
        return "/exammanage/edu/examReports/examReportsTicketExport.ftl";
    }

    @RequestMapping("/examReports/exportOptionList/page")
    @ControllerInfo(value = "导出对象页面")
    public String exportOptionList(String examId, HttpServletRequest request, ModelMap map) {
        String regionId = request.getParameter("regionId");

        List<EmOption> emOptionList = emOptionService.findByExamIdAndExamRegionIdIn(examId, regionId, null);
        Set<String> optionSet = getOptionSet(examId);
        List<EmOption> lastOptionList = new ArrayList<EmOption>();
        if (CollectionUtils.isNotEmpty(emOptionList)) {
            for (EmOption option : emOptionList) {
                if (optionSet.contains(option.getId())) {
                    lastOptionList.add(option);
                }
            }
        }
        map.put("emOptionList", lastOptionList);
        return "/exammanage/edu/examReports/examExportOption.ftl";
    }

    @RequestMapping("/examReports/exportList/page")
    @ControllerInfo(value = "导出对象页面")
    public String showExamExport(String examId, HttpServletRequest request, ModelMap map) {
        String regionId = request.getParameter("regionId");
        String type = request.getParameter("type");
        Set<String> regionSet = getRegionSet(examId);

        if (CollectionUtils.isNotEmpty(regionSet)) {
            List<EmRegion> emRegionList = emExamRegionService.findListByIdIn(regionSet.toArray(new String[0]));
            if (CollectionUtils.isNotEmpty(emRegionList)) {
                if (StringUtils.isBlank(regionId)) {
                    regionId = emRegionList.get(0).getId();
                }
            }
            map.put("emRegionList", emRegionList);
        }
        map.put("examId", examId);
        map.put("regionId", regionId);
        map.put("type", type);
        return "/exammanage/edu/examReports/examReportsExport.ftl";
    }

    @RequestMapping("/examReports/exportHtmlToPdf/page")
    @ControllerInfo("生成pdf并导出")
    public String exportHtmlToPdf(String examId, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        String regionId = request.getParameter("regionId");
        String optionId = request.getParameter("optionId");
        String schoolId = request.getParameter("schIds");
        String type = request.getParameter("type");
        boolean flag = "true".equals(request.getParameter("flag")) ? true : false;
        StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
        String typeName = "";
        String fileName = "";
        String sourcePath = "";
        Vector<Thread> vectors = new Vector<Thread>();
        if (StringUtils.equals(type, "4")) {
            sourcePath = dir.getDir() + File.separator + "examReports" + File.separator + examId + File.separator + schoolId + File.separator + "report" + type;//有schoolId
            typeName = "准考证";
            School sch = SUtils.dc(schoolRemoteService.findOneById(schoolId), School.class);
            Set<String> clsAllIds = emPlaceStudentService.getClsSchMap(examId);
            List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findListByIds(clsAllIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Set<String> clsIds = new HashSet<>();
            for (Clazz cls : clsAlllist) {
                if (StringUtils.equals(cls.getSchoolId(), schoolId)) {
                    clsIds.add(cls.getId());
                }
            }
            List<Clazz> clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(clslist)) {
                for (Clazz result : clslist) {
                    MyThread myThread = new MyThread();
                    myThread.setServerUrl(UrlUtils.getPrefix(request));
                    myThread.setExamId(examId);
                    myThread.setFlag(flag);
                    myThread.setType(type);
                    myThread.setSchoolId(schoolId);
                    myThread.setClassId(result.getId());
                    myThread.setClassName(result.getClassNameDynamic());
                    Thread thread = new Thread(myThread);
                    vectors.add(thread);
                    thread.start();
                }
            }
            fileName = sch.getSchoolName() + "的" + typeName + ".zip";
        } else {
            sourcePath = dir.getDir() + File.separator + "examReports" + File.separator + examId + File.separator + regionId +
                    File.separator + optionId + File.separator + "report" + type;//有region optionId
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            EmRegion emRegion = emExamRegionService.findOne(regionId);
            EmOption emOption = emOptionService.findOne(optionId);
            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndOptionIds(examId, new String[]{optionId}, null);
            if (CollectionUtils.isNotEmpty(emPlaceList)) {
                List<List<EmPlace>> resultList = null;
                if (emPlaceList.size() < 20) {
                    resultList = averageAssign(emPlaceList, 5);//创建5个线程
                } else {
                    //创建20个线程
                    resultList = averageAssign(emPlaceList, 20);
                }
                for (List<EmPlace> result : resultList) {
                    MyThread myThread = new MyThread();
                    myThread.setPlaceList(result);
                    myThread.setSomeName("考点：" + emOption.getOptionName());
                    myThread.setServerUrl(UrlUtils.getPrefix(request));
                    myThread.setExamId(examId);
                    myThread.setFlag(flag);
                    myThread.setOptionId(optionId);
                    myThread.setRegionId(regionId);
                    myThread.setType(type);
                    Thread thread = new Thread(myThread);
                    vectors.add(thread);
                    thread.start();
                }
            }
            if ("1".equals(type)) {
                typeName = "考场门贴";
            } else if ("2".equals(type)) {
                typeName = "考场对照单";
            } else if ("3".equals(type)) {
                typeName = "考生桌贴";
            }
            fileName = emRegion.getRegionName() + emOption.getOptionName() + examInfo.getExamName() + "的" + typeName + ".zip";
        }
        for (Thread thread : vectors) {
            try {
                thread.join();//使用join来保证thread的多个线程都执行完后，才执行主线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exportZip(response, sourcePath, fileName);
        return "";
    }

    public void exportZip(HttpServletResponse response, String sourcePath, String fileName) {
        File sourceFile = new File(sourcePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        if (sourceFile.exists() == false) {
            //System.out.println("待压缩的文件目录："+sourcePath+"不存在.");
        } else {
            try {
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    //System.out.println("待压缩的文件目录：" + sourcePath + "里面不存在文件，无需压缩.");
                } else {
                    response.setHeader("Content-Disposition", "attachment; filename=" + UrlUtils.encode(fileName, "UTF-8"));
                    zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
                    byte[] bufs = new byte[1024 * 10];
                    for (int i = 0; i < sourceFiles.length; i++) {
                        //创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(sourceFiles[i]);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (null != bis) bis.close();
                    if (null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @RequestMapping("/examReports/exportDetailList/page")
    @ControllerInfo(value = "导出考试桌贴列表")
    public String showExportDetailList(String examId, HttpServletRequest request, ModelMap map) {
        String regionId = request.getParameter("regionId");
        String optionId = request.getParameter("optionId");
        String placeId = request.getParameter("placeId");
        String type = request.getParameter("type");
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        EmRegion emRegion = emExamRegionService.findOne(regionId);
        EmOption emOption = emOptionService.findOne(optionId);
        EmPlace emPlace = emPlaceService.findOne(placeId);
        map.put("regionName", emRegion == null ? "" : emRegion.getRegionName());
        map.put("optionName", emOption == null ? "" : emOption.getOptionName());
        map.put("placeName", emPlace == null ? "" : emPlace.getExamPlaceCode());
        List<EmPlaceStudent> emPlaceStudents = emPlaceStudentService.findByExamPlaceIdAndGroupId(null, placeId);
        if (StringUtils.isNotBlank(type) && StringUtils.equals("1", type)) {
            map.put("firstNum", emPlaceStudents.get(0).getExamNumber());
            map.put("lastNum", emPlaceStudents.get(emPlaceStudents.size() - 1).getExamNumber());
        }
        map.put("emPlaceStudents", emPlaceStudents);
        map.put("examName", examInfo == null ? "" : examInfo.getExamName());
        if (StringUtils.isNotBlank(type) && StringUtils.equals("3", type)) {
            return "/exammanage/edu/examReports/examReportsZTList2.ftl";
        } else if (StringUtils.isNotBlank(type) && StringUtils.equals("2", type)) {
            return "/exammanage/edu/examReports/examReportsCheckList2.ftl";
        } else {
            return "/exammanage/edu/examReports/examReportsDoorList2.ftl";
        }
    }

    @RequestMapping("/examReports/exportTicketPDF/page")
    @ControllerInfo(value = "导出准考证列表")
    public String showTicketPDFList(String examId, HttpServletRequest request, ModelMap map) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String classId = request.getParameter("classId");
        Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        String schoolId = cls.getSchoolId();
        School sch = SUtils.dc(schoolRemoteService.findOneById(schoolId), School.class);

        List<Student> stus = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {
        });
        List<EmTicketDto> dtos = emPlaceStudentService.findByExamIdStuIds(examId, stus);
        for (EmTicketDto dto : dtos) {
            dto.setClassName(cls.getClassNameDynamic());
            dto.setSchoolName(sch.getSchoolName());
        }
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        List<EmSubDateDto> subDtos = new ArrayList<>();
        int maxTr = 0;
        for (EmSubjectInfo sub : subjectInfoList) {
            String date1 = sdf1.format(sub.getStartDate());
            String startDate = sdf2.format(sub.getStartDate());
            String endDate = sdf2.format(sub.getEndDate());
            EmSubDateDto subDto = null;
            for (EmSubDateDto dto2 : subDtos) {
                if (StringUtils.equals(dto2.getDate(), date1)) {
                    subDto = dto2;
                }
            }
            if (subDto == null) {
                subDto = new EmSubDateDto();
                subDto.setDate(date1);
                subDtos.add(subDto);
            }

            List<EmSubDto> dto2s = subDto.getSubDtos();
            EmSubDto dto2 = new EmSubDto();
            dto2.setEndDate(endDate);
            dto2.setStartDate(startDate);
            dto2.setSubName(sub.getCourseName());
            dto2s.add(dto2);
            if (dto2s.size() > maxTr) {
                maxTr = dto2s.size();
            }
        }
        map.put("maxTr", maxTr);
        map.put("subDtos", subDtos);
        map.put("classId", classId);
        map.put("schoolId", schoolId);
        map.put("className", cls.getClassNameDynamic());
        map.put("schoolName", sch.getSchoolName());
        map.put("examName", examInfo == null ? "" : examInfo.getExamName());
        map.put("dtos", dtos);
        map.put("examId", examId);
        return "/exammanage/edu/examReports/examTicketPDF.ftl";
    }

    @RequestMapping("/examReports/examTicket/page")
    @ControllerInfo(value = "准考证号")
    public String showExamTickettList(String examId, String type, HttpServletRequest request, ModelMap map, HttpSession httpSession) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String classId = request.getParameter("classId");
        String schoolId = request.getParameter("schoolId");
        //编排班级
        Set<String> clsAllIds = emPlaceStudentService.getClsSchMap(examId);
        if (CollectionUtils.isEmpty(clsAllIds)) {
            return errorFtl(map, "没有编排结果，请先编排！");
        }
        List<Clazz> clsAlllist = SUtils.dt(classRemoteService.findListByIds(clsAllIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        Set<String> schIds = new HashSet<>();
        Map<String, String> clsSchMap = new HashMap<>();
        for (Clazz cls : clsAlllist) {
            schIds.add(cls.getSchoolId());
            clsSchMap.put(cls.getId(), cls.getSchoolId());
        }
        List<School> schlist = SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
        });
        if (StringUtils.isBlank(schoolId)) {
            schoolId = schlist.get(0).getId();
        }
        Set<String> clsIds = new HashSet<>();
        for (String key : clsSchMap.keySet()) {
            if (StringUtils.equals(clsSchMap.get(key), schoolId)) {
                clsIds.add(key);
            }
        }
        List<Clazz> clslist = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        if (StringUtils.isBlank(classId)) {
            classId = clslist.get(0).getId();
        }
        if (StringUtils.isBlank(schoolId) || StringUtils.isBlank(classId)) {
            return errorFtl(map, "编排结果有误，请重新编排！");
        }
        School sch = SUtils.dc(schoolRemoteService.findOneById(schoolId), School.class);
        Clazz cls = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        List<Student> stus = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {
        });
        List<EmTicketDto> dtos = emPlaceStudentService.findByExamIdStuIds(examId, stus);
        for (EmTicketDto dto : dtos) {
            dto.setClassName(cls.getClassNameDynamic());
            dto.setSchoolName(sch.getSchoolName());
        }
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        List<EmSubDateDto> subDtos = new ArrayList<>();
        int maxTr = 0;
        for (EmSubjectInfo sub : subjectInfoList) {
            String date1 = sdf1.format(sub.getStartDate());
            String startDate = sdf2.format(sub.getStartDate());
            String endDate = sdf2.format(sub.getEndDate());
            EmSubDateDto subDto = null;
            for (EmSubDateDto dto2 : subDtos) {
                if (StringUtils.equals(dto2.getDate(), date1)) {
                    subDto = dto2;
                }
            }
            if (subDto == null) {
                subDto = new EmSubDateDto();
                subDto.setDate(date1);
                subDtos.add(subDto);
            }

            List<EmSubDto> dto2s = subDto.getSubDtos();
            EmSubDto dto2 = new EmSubDto();
            dto2.setEndDate(endDate);
            dto2.setStartDate(startDate);
            dto2.setSubName(sub.getCourseName());
            dto2s.add(dto2);
            if (dto2s.size() > maxTr) {
                maxTr = dto2s.size();
            }
        }
        map.put("maxTr", maxTr);
        map.put("subDtos", subDtos);
        map.put("classId", classId);
        map.put("schoolId", schoolId);
        map.put("className", cls.getClassNameDynamic());
        map.put("schoolName", sch.getSchoolName());
        map.put("schlist", schlist);
        map.put("clslist", clslist);
        map.put("optionPick", request.getParameter("optionPick"));
        map.put("optionPick1", request.getParameter("optionPick1"));
        map.put("examName", examInfo == null ? "" : examInfo.getExamName());
        map.put("dtos", dtos);
        map.put("examId", examId);
        return "/exammanage/edu/examReports/examTicket.ftl";
    }

    @RequestMapping("/examReports/detailList/page")
    @ControllerInfo(value = "考试桌贴列表")
    public String showExamReportList(String examId, HttpServletRequest request, ModelMap map, HttpSession httpSession) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        String regionId = request.getParameter("regionId");
        String optionId = request.getParameter("optionId");
        String placeId = request.getParameter("placeId");
        String type = request.getParameter("type");

        //TODO
        //Set<String> regionSet=getRegionSet(examId);
        Set<String> regionSet = getRegionIdSet(examId);
        //Set<String> optionSet=getOptionSet(examId);
        Set<String> optionSet = getOptionIdSet(examId);
        //Set<String> placeSet=getPlaceSet(examId);
        Set<String> placeSet = getPlaceIdSet(examId);

        if (CollectionUtils.isEmpty(regionSet)) {
            return errorFtl(map, "编排结果为空，请检查！");
        } else {
            List<EmRegion> emRegionList = emExamRegionService.findListByIdIn(regionSet.toArray(new String[0]));
//			List<EmRegion> emRegionList=emExamRegionService.findByExamIdAndUnitId(examId, getLoginInfo().getUnitId());
            List<EmOption> emOptionList = emOptionService.findByExamIdAndExamRegionIdIn(examId, StringUtils.isNotBlank(regionId) ? regionId : emRegionList.get(0).getId(), null);
            if (CollectionUtils.isNotEmpty(emOptionList)) {
                if (CollectionUtils.isNotEmpty(optionSet)) {
                    List<EmOption> desOptions = new ArrayList<EmOption>();
                    for (EmOption emOption : emOptionList) {
                        if (optionSet.contains(emOption.getId())) {
                            desOptions.add(emOption);
                        }
                    }
                    map.put("emOptionList", desOptions);
                    map.put("optionId", StringUtils.isNotBlank(optionId) ? optionId : desOptions.get(0).getId());
                    List<EmOption> emOptions = emOptionService.findListByIdIn(StringUtils.isNotBlank(optionId) ? new String[]{optionId} : new String[]{desOptions.get(0).getId()});
                    map.put("emOptionName", CollectionUtils.isNotEmpty(emOptions) ? emOptions.get(0).getOptionName() : "");
                    String temp = StringUtils.isNotBlank(optionId) ? optionId : desOptions.get(0).getId();
                    List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndOptionIds(examId, new String[]{temp});
                    if (CollectionUtils.isNotEmpty(emPlaceList)) {
                        List<EmPlace> desPlaces = new ArrayList<EmPlace>();
                        for (EmPlace emPlace : emPlaceList) {
                            if (placeSet.contains(emPlace.getId())) {
                                desPlaces.add(emPlace);
                            }
                            map.put("emPlaceList", desPlaces);
                        }
                        if (CollectionUtils.isNotEmpty(desPlaces)) {
                            map.put("placeId", StringUtils.isNotBlank(placeId) ? placeId : desPlaces.get(0).getId());
                            List<EmPlaceStudent> emPlaceStudents = emPlaceStudentService.findByExamPlaceIdAndGroupId(null, StringUtils.isNotBlank(placeId) ? placeId : desPlaces.get(0).getId());
                            map.put("emPlaceStudents", emPlaceStudents);
                            if (StringUtils.isNotBlank(type) && StringUtils.equals("1", type)) {
                                map.put("firstNum", emPlaceStudents.get(0).getExamNumber());
                                map.put("lastNum", emPlaceStudents.get(emPlaceStudents.size() - 1).getExamNumber());
                            }
                            EmPlace emPlace = emPlaceService.findByEmPlaceId(StringUtils.isNotBlank(placeId) ? placeId : desPlaces.get(0).getId());
                            map.put("placeName", emPlace == null ? "" : emPlace.getExamPlaceCode());
                        }
                    }
                }
                EmRegion emRegion = emExamRegionService.findOne(StringUtils.isNotBlank(regionId) ? regionId : emRegionList.get(0).getId());
                map.put("regionName", emRegion == null ? "" : emRegion.getRegionName());
            }
            map.put("examName", examInfo == null ? "" : examInfo.getExamName());
            map.put("emRegionList", emRegionList);
            map.put("examId", examId);
            map.put("regionId", StringUtils.isNotBlank(regionId) ? regionId : emRegionList.get(0).getId());
            map.put("regionPick", request.getParameter("regionPick"));
            map.put("optionPick", request.getParameter("optionPick"));
            map.put("type", type);
            map.put("numPick", request.getParameter("numPick"));
            if (StringUtils.isNotBlank(type) && StringUtils.equals("3", type)) {
                return "/exammanage/edu/examReports/examReportsZTList.ftl";
            } else if (StringUtils.isNotBlank(type) && StringUtils.equals("2", type)) {
                return "/exammanage/edu/examReports/examReportsCheckList.ftl";
            } else {
                return "/exammanage/edu/examReports/examReportsDoorList.ftl";
            }
        }
    }

    /**
     * 获取考区
     *
     * @param regionSet
     * @param examId
     */
    public Set<String> getRegionSet(String examId) {
        Set<String> regionSet = new HashSet<String>();
        Map<String, List<EmPlaceStudent>> stuMap = emPlaceStudentService.findMapByExamIds(new String[]{examId});
        if (stuMap != null && stuMap.size() > 0) {
            List<EmPlaceStudent> emPlaceStudents = stuMap.get(examId);
            Set<String> placeIds = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(emPlaceStudents)) {
                placeIds = EntityUtils.getSet(emPlaceStudents, "examPlaceId");
            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                List<EmPlace> emPlaces = emPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
                Set<String> optionIds = new HashSet<String>();
                if (CollectionUtils.isNotEmpty(emPlaces)) {
                    optionIds = EntityUtils.getSet(emPlaces, "optionId");
                }
                if (CollectionUtils.isNotEmpty(optionIds)) {
                    List<EmOption> emOptions = emOptionService.findListByIdIn(optionIds.toArray(new String[0]));
                    if (CollectionUtils.isNotEmpty(emOptions)) {
                        regionSet = EntityUtils.getSet(emOptions, "examRegionId");
                    }
                }
            }
        }
        return regionSet;
    }

    public Set<String> getRegionIdSet(String examId) {
        Set<String> regionSet = new HashSet<String>();
        regionSet = emOptionService.getExamRegionId(examId);
        return regionSet;
    }

    /**
     * 获取考点
     *
     * @param regionSet
     * @param examId
     */
    public Set<String> getOptionSet(String examId) {
        Set<String> optionIds = new HashSet<String>();
        Map<String, List<EmPlaceStudent>> stuMap = emPlaceStudentService.findMapByExamIds(new String[]{examId});
        if (stuMap != null && stuMap.size() > 0) {
            List<EmPlaceStudent> emPlaceStudents = stuMap.get(examId);
            Set<String> placeIds = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(emPlaceStudents)) {
                placeIds = EntityUtils.getSet(emPlaceStudents, "examPlaceId");
            }
            if (CollectionUtils.isNotEmpty(placeIds)) {
                List<EmPlace> emPlaces = emPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(emPlaces)) {
                    optionIds = EntityUtils.getSet(emPlaces, "optionId");
                }
            }
        }
        return optionIds;
    }

    public Set<String> getOptionIdSet(String examId) {
        Set<String> optionIds = new HashSet<String>();
        optionIds = emPlaceService.getOptionId(examId);
        return optionIds;
    }

    /**
     * 获取考场
     *
     * @param regionSet
     * @param examId
     */
    public Set<String> getPlaceSet(String examId) {
        Set<String> placeIds = new HashSet<String>();
        Map<String, List<EmPlaceStudent>> stuMap = emPlaceStudentService.findMapByExamIds(new String[]{examId});
        if (stuMap != null && stuMap.size() > 0) {
            List<EmPlaceStudent> emPlaceStudents = stuMap.get(examId);
            if (CollectionUtils.isNotEmpty(emPlaceStudents)) {
                placeIds = EntityUtils.getSet(emPlaceStudents, "examPlaceId");
            }
        }
        return placeIds;
    }

    public Set<String> getPlaceIdSet(String examId) {
        Set<String> placeIds = new HashSet<String>();
        placeIds = emPlaceStudentService.getPlaceIdSet(examId);
        return placeIds;
    }

    /**
     * 考试主列表
     *
     * @param map
     * @param httpSession
     * @param url
     * @return
     */
    public String showHead(ModelMap map, HttpSession httpSession, String url) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
		/*List<String> acadyearList=new ArrayList<>();
		acadyearList.add("2017-2018");*/
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        map.put("unitClass", unit.getUnitClass());
        //年级Code列表
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        List<Grade> gradeList = new ArrayList<Grade>();
        if (unit.getUnitClass() == 2) {
            //学校
            School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
            gradeList = getSchGradeList(findMapMapByMcodeIds, school);
            map.put("gradeList", gradeList);
        } else {
            List<McodeDetail> mcodelist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JYJXZ"), new TR<List<McodeDetail>>() {
            });
            gradeList = getEduGradeList(mcodelist, findMapMapByMcodeIds);
            map.put("gradeList", gradeList);
        }
        return url;
    }

    /**
     * 30天考试列表
     *
     * @param map
     * @param httpSession
     * @param isClear     是否去除没有设置班级的考试
     * @return
     */
    public String showExamInfoIn(ModelMap map, HttpSession httpSession, String url, boolean isClear) {
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        examInfoList = emExamInfoService.findExamList(ExammanageConstants.DAY, unitId, searchDto, true);
        if (isClear) {
            clearNoExam(examInfoList, unitId);
        }
        //System.out.println(System.currentTimeMillis()-a);
        //a = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
            //Map<String,List<EmPlaceStudent>> emMap=emPlaceStudentService.findMapByExamIds(examIds.toArray(new String[0]));
            Set<String> userExamIds = emPlaceStudentService.getExamIds(examIds.toArray(new String[0]));
            if (CollectionUtils.isNotEmpty(userExamIds)) {
                //Set<String> userExamIds=new HashSet<String>();
                //for (Entry<String, List<EmPlaceStudent>> entry : emMap.entrySet()) {
                //	if(CollectionUtils.isNotEmpty(entry.getValue())){
                //		userExamIds.add(entry.getKey());
                //	}
                //}
                List<EmExamInfo> newExamInfoList = new ArrayList<EmExamInfo>();
                for (EmExamInfo item : examInfoList) {
                    if (userExamIds.contains(item.getId())) {
                        newExamInfoList.add(item);
                    }
                }
                examInfoList = newExamInfoList;
            } else {
                examInfoList = new ArrayList<EmExamInfo>();
            }
        }
        //System.out.println(System.currentTimeMillis()-a);
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "1");
        return url;
    }

    /**
     * 30天前考试列表
     *
     * @param searchAcadyear
     * @param searchSemester
     * @param searchType
     * @param searchGradeCode
     * @param map
     * @param httpSession
     * @return
     */
    public String showExamInfoBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession, String url, boolean isClear) {
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        searchDto.setSearchAcadyear(searchAcadyear);
        searchDto.setSearchSemester(searchSemester);
        searchDto.setSearchType(searchType);
        searchDto.setSearchGradeCode(searchGradeCode);
        examInfoList = emExamInfoService.findExamList(ExammanageConstants.DAY, unitId, searchDto, false);
        if (isClear) {
            clearNoExam(examInfoList, unitId);
        }
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
            //Map<String,List<EmPlaceStudent>> emMap=emPlaceStudentService.findMapByExamIds(examIds.toArray(new String[0]));
            Set<String> userExamIds = emPlaceStudentService.getExamIds(examIds.toArray(new String[0]));
            if (CollectionUtils.isNotEmpty(userExamIds)) {
                //Set<String> userExamIds=new HashSet<String>();
                //for (Entry<String, List<EmPlaceStudent>> entry : emMap.entrySet()) {
                //	if(CollectionUtils.isNotEmpty(entry.getValue())){
                //		userExamIds.add(entry.getKey());
                //	}
                //}
                List<EmExamInfo> newExamInfoList = new ArrayList<EmExamInfo>();
                for (EmExamInfo item : examInfoList) {
                    if (userExamIds.contains(item.getId())) {
                        newExamInfoList.add(item);
                    }
                }
                examInfoList = newExamInfoList;
            } else {
                examInfoList = new ArrayList<EmExamInfo>();
            }
        }
        map.put("examInfoList", examInfoList);
        map.put("unitId", unitId);
        map.put("viewType", "2");
        return url;
    }

    private List<Grade> getSchGradeList(Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds, School school) {
        List<Grade> gradeList = new ArrayList<Grade>();
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

    private List<Grade> getEduGradeList(List<McodeDetail> mcodelist, Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds) {
        List<Grade> gradeList = new ArrayList<Grade>();
        // 取教育局学制微代码信息
        for (int i = 0; i < mcodelist.size(); i++) {
            McodeDetail detail = mcodelist.get(i);
            int section = Integer.parseInt(detail.getThisId());
            String thisId = detail.getThisId();
            Map<String, McodeDetail> mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-" + thisId);
            if (mcodeMap == null || mcodeMap.size() <= 0) {
                continue;
            }
            int nz = Integer.parseInt(detail.getMcodeContent());// 年制
            for (int j = 0; j < nz; j++) {
                int grade = j + 1;
                Grade dto = new Grade();
                dto.setGradeCode(section + "" + grade);
                if (mcodeMap.containsKey(grade + "")) {
                    dto.setGradeName(mcodeMap.get(grade + "").getMcodeContent());
                }
                gradeList.add(dto);
            }
        }
        Collections.sort(gradeList, new Comparator<Grade>() {
            public int compare(Grade o1, Grade o2) {
                return (o1.getGradeCode().compareToIgnoreCase(o2.getGradeCode()));
            }
        });
        return gradeList;
    }

    /**
     * 去除没有设置班级的
     *
     * @param examInfoList
     */
    private void clearNoExam(List<EmExamInfo> examInfoList, String schoolId) {
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            Set<String> examIds = EntityUtils.getSet(examInfoList, "id");
            List<EmClassInfo> classInfoList = emClassInfoService.findBySchoolIdAndExamIdIn(examIds.toArray(new String[]{}), schoolId);
            if (CollectionUtils.isNotEmpty(classInfoList)) {
                Set<String> userExamIds = EntityUtils.getSet(classInfoList, "examId");
                List<EmExamInfo> newExamInfoList = new ArrayList<EmExamInfo>();
                for (EmExamInfo item : examInfoList) {
                    if (userExamIds.contains(item.getId())) {
                        newExamInfoList.add(item);
                    }
                }
                examInfoList = newExamInfoList;
            } else {
                examInfoList = new ArrayList<EmExamInfo>();
            }
        }
    }

    /**
     * tab
     *
     * @param examId
     * @param type
     * @param map
     * @param httpSession
     * @return
     */
    public String showTabIndex(String examId, String type, ModelMap map, HttpSession httpSession, String url) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examId", examId);
        map.put("type", type);
        return url;
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

    class MyThread implements Runnable {
        //
        private String serverUrl;
        private String examId;
        private String regionId;
        private String optionId;
        private List<EmPlace> placeList;
        private String type;
        private String schoolId;
        private String classId;
        private String className;
        private String someName;
        private boolean flag = false;//是否覆盖已有的pdf

        @Override
        public void run() {

            StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
            if ("4".equals(type)) {
                String uu = dir.getDir() + File.separator + "examReports" + File.separator + examId + File.separator + schoolId + File.separator + "report" +
                        type + File.separator + className + ".pdf";
                File f = new File(uu);
                String urlStr = "/exammanage/edu/examReports/exportTicketPDF/page?classId=" + classId + "&examId=" + examId + "&type=" + type;
                if (f.exists()) {
                    if (flag) {
                        f.delete();//删除
                        HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, "1300", 2000, null);
                    }
                } else {
                    HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, "1300", 2000, null);
                }
            } else {
                if (CollectionUtils.isNotEmpty(placeList)) {
                    Map<String, String> parMap = new HashMap<String, String>();
                    parMap.put("Landscape", String.valueOf("1".equals(type)));//参数设定
                    for (EmPlace emplace : placeList) {
                        if ("2".equals(type)) {
                            parMap.put("someName", someName + "(" + emplace.getExamPlaceCode().trim() + "考场)");
                        }
                        String uu = dir.getDir() + File.separator + "examReports" + File.separator + examId + File.separator + regionId + File.separator +
                                optionId + File.separator + "report" + type + File.separator + "第" + emplace.getExamPlaceCode().trim() + "考场.pdf";
                        File f = new File(uu);
                        String urlStr = "/exammanage/edu/examReports/exportDetailList/page?placeId=" +
                                emplace.getId() + "&regionId=" + regionId + "&optionId=" + optionId + "&examId=" + examId + "&type=" + type;
                        if (f.exists()) {
                            if (flag) {
                                f.delete();//删除
                                HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, "1300", 2000, parMap);
                            }
                        } else {
                            HtmlToPdf.convertFin(new String[]{serverUrl + urlStr}, uu, null, "1300", 2000, parMap);
                        }
                        /*f = new File(uu);String path="";if(f.exists()){path=f.getAbsolutePath();}*/
                    }
                }
            }
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public void setOptionId(String optionId) {
            this.optionId = optionId;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setPlaceList(List<EmPlace> placeList) {
            this.placeList = placeList;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public void setSomeName(String someName) {
            this.someName = someName;
        }
    }
}
