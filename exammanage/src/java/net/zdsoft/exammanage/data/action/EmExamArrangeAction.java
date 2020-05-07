package net.zdsoft.exammanage.data.action;


import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmFilterSaveDto;
import net.zdsoft.exammanage.data.dto.EmPlaceGroupDto;
import net.zdsoft.exammanage.data.dto.EmPlaceSaveDto;
import net.zdsoft.exammanage.data.dto.EmStudentDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ArrayUtil;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage")
public class EmExamArrangeAction extends EmExamCommonAction {

    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmPlaceGroupService emPlaceGroupService;
    @Autowired
    private EmStudentGroupService emStudentGroupService;

    @RequestMapping("/examArrange/index/page")
    @ControllerInfo(value = "考试编排")
    public String showIndex(ModelMap map) {
        return "/exammanage/examArrange/examArrangeIndex.ftl";
    }

    @RequestMapping("/examArrange/head/page")
    @ControllerInfo(value = "考试编排设置")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examArrange/examArrangeHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/examArrange/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examArrange/examArrangeList.ftl";
        return showExamInfoIn(map, httpSession, url, true);
    }

    @RequestMapping("/examArrange/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examArrange/examArrangeList.ftl";
        return showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
    }

    @RequestMapping("/examArrange/examItemIndex/page")
    @ControllerInfo("tab")
    public String showArrangeTab(String examId, String type, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/examArrange/arrangeTabIndex.ftl";
        return showTabIndex(examId, type, map, httpSession, url);
    }

    @RequestMapping("/examArrange/filterIndex/page")
    @ControllerInfo("不排考设置")
    public String showFilterIndex(String examId, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        //List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
        String[] classIds = showFindClass(unitId, examInfo).stream().map(Clazz::getId).collect(Collectors.toSet()).toArray(new String[0]);
        ;
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds), new TR<List<Clazz>>() {
        });
		/*if(CollectionUtils.isNotEmpty(classInfoList)){
			//暂不考虑教学班
			Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
			clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>(){} );
		}*/
        map.put("clazzList", clazzList);
        map.put("examId", examId);
        Map<String, String> nummap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);

        if (nummap.size() > 0) {
            map.put("isEdit", false);
        } else {
            map.put("isEdit", true);
        }

        Boolean canEdit = true;
        if (ExammanageConstants.TKLX_4.equals(examInfo.getExamUeType())) {
            canEdit = false;
        }
        map.put("canEdit", canEdit);
        return "/exammanage/examArrange/filterIndex.ftl";
    }

    @RequestMapping("/examArrange/filterList/page")
    @ControllerInfo("不排考列表")
    public String showFilterList(String examId, String searchClassId, String searchFilterType, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
//		Set<String> classIds=new HashSet<String>();
//		if(StringUtils.isNotBlank(searchClassId)){
//			classIds.add(searchClassId);
//		}else{
//			List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
//			if(CollectionUtils.isNotEmpty(classInfoList)){
//				//暂不考虑教学班
//				classIds = EntityUtils.getSet(classInfoList, "classId");
//			}
//		}
//		List<Student> studentList=new ArrayList<Student>();
        //行政班
        Map<String, String> classMap = new LinkedHashMap<String, String>();
//		if(classIds.size()>0){
//			studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})),new TR<List<Student>>(){});
//			List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>(){});
//			if(CollectionUtils.isNotEmpty(classList)){
//				for(Clazz z:classList){
//					classMap.put(z.getId(), z.getClassNameDynamic());
//				}
//			}
//		}

        List<Student> studentList = findStudentByExamId(examInfo, unitId, searchClassId, false, classMap);

        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);

        List<EmStudentDto> stuDtoList = new ArrayList<EmStudentDto>();
        EmStudentDto dto = new EmStudentDto();
        if (CollectionUtils.isNotEmpty(studentList)) {
            for (Student stu : studentList) {
                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }
                if ("1".equals(searchFilterType)) {
                    //排考
                    if (!filterMap.containsKey(stu.getId())) {
                        dto.setFilter("1");
                        stuDtoList.add(dto);
                    }
                    continue;

                } else if ("0".equals(searchFilterType)) {
                    //不排考
                    if (filterMap.containsKey(stu.getId())) {
                        dto.setFilter("0");
                        stuDtoList.add(dto);
                    }
                    continue;
                } else {
                    //全部
                    if (filterMap.containsKey(stu.getId())) {
                        dto.setFilter("0");
                    } else {
                        dto.setFilter("1");
                    }
                    stuDtoList.add(dto);
                    continue;
                }
            }
        }
        map.put("stuDtoList", stuDtoList);
        map.put("examId", examId);
        map.put("unitId", unitId);
        return "/exammanage/examArrange/filterList.ftl";
    }

    @RequestMapping("/examArrange/filterOneList/page")
    @ControllerInfo("不排考列表")
    public String showFilterOneList(String examId, String searchSelectType, String searchCondition, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        Set<String> classIds = showFindClass(unitId, examInfo).stream().map(Clazz::getId).collect(Collectors.toSet());
		/*List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
		if(CollectionUtils.isNotEmpty(classInfoList)){
			//暂不考虑教学班
			classIds = EntityUtils.getSet(classInfoList, "classId");
		}*/
        try {
            searchCondition = URLDecoder.decode(searchCondition, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<Student> studentList = new ArrayList<Student>();
        //行政班
        Map<String, String> classMap = new LinkedHashMap<String, String>();
        if (classIds.size() > 0) {
            List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classList)) {
                for (Clazz z : classList) {
                    classMap.put(z.getId(), z.getClassNameDynamic());
                }
            }
        }
        String valCode = "";
        if ("1".equals(searchSelectType)) {
            valCode = "studentName";
        } else if ("2".equals(searchSelectType)) {
            valCode = "studentCode";
        } else if ("3".equals(searchSelectType)) {
            valCode = "identityCard";
        }
        if (StringUtils.isNotBlank(valCode)) {
            studentList = SUtils.dt(studentRemoteService.findByUnitLikeCode(unitId, valCode, searchCondition), new TR<List<Student>>() {
            });
        }

        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);

        List<EmStudentDto> stuDtoList = new ArrayList<EmStudentDto>();
        EmStudentDto dto = new EmStudentDto();
        if (CollectionUtils.isNotEmpty(studentList)) {
            for (Student stu : studentList) {
                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }
                //全部
                if (filterMap.containsKey(stu.getId())) {
                    dto.setFilter("0");
                } else {
                    dto.setFilter("1");
                }
                stuDtoList.add(dto);
            }
        }
        map.put("stuDtoList", stuDtoList);
        map.put("examId", examId);
        map.put("unitId", unitId);
        return "/exammanage/examArrange/filterList.ftl";
    }

    @ResponseBody
    @RequestMapping("/examArrange/filterSave")
    @ControllerInfo(value = "保存排考信息")
    public String filterSave(EmFilterSaveDto saveDto, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(saveDto.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            String examId = saveDto.getExamId();
            List<EmStudentDto> stuDtoList = saveDto.getStuDtoList();
            if (CollectionUtils.isNotEmpty(stuDtoList)) {
                Set<String> studentIds = EntityUtils.getSet(stuDtoList, "studentId");
                List<EmFiltration> fList = new ArrayList<EmFiltration>();
                EmFiltration f = null;
                for (EmStudentDto e : stuDtoList) {
                    if ("0".equals(e.getFilter())) {
                        f = new EmFiltration();
                        f.setExamId(examId);
                        f.setId(UuidUtils.generateUuid());
                        f.setSchoolId(unitId);
                        f.setStudentId(e.getStudentId());
                        f.setType(ExammanageConstants.FILTER_TYPE1);
                        fList.add(f);
                    }
                }

                emFiltrationService.saveOrDel(fList, examId, studentIds, ExammanageConstants.FILTER_TYPE1);

            } else {
                return error("没有需要保存的数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/filterSet")
    @ControllerInfo(value = "批量设置排考信息")
    public String filterBatchSet(String ids, String examId, String type, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            String[] stuIds = ids.split(",");
            if (stuIds == null || stuIds.length <= 0) {
                return error("没有需要保存的数据！");
            }
            if ("1".equals(type)) {
                //排考
                emFiltrationService.deleteByExamIdAndStudentIdIn(examId, ExammanageConstants.FILTER_TYPE1, stuIds);
            } else {
                //不排考
                List<EmFiltration> fList = new ArrayList<EmFiltration>();
                Set<String> studentIds = new HashSet<String>();
                EmFiltration f = null;
                for (String id : stuIds) {
                    f = new EmFiltration();
                    f.setExamId(examId);
                    f.setId(UuidUtils.generateUuid());
                    f.setSchoolId(unitId);
                    f.setStudentId(id);
                    f.setType(ExammanageConstants.FILTER_TYPE1);
                    studentIds.add(id);
                    fList.add(f);
                }
                emFiltrationService.saveOrDel(fList, examId, studentIds, ExammanageConstants.FILTER_TYPE1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/placeArrange/arrangeAutoSave")
    @ControllerInfo(value = "考场自动分配")
    public String autoArrangePlace(String examId, ModelMap map, HttpServletRequest request, HttpSession httpSession) {
        String str="";
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            str=emPlaceGroupService.autoArrangePlace(examId, unitId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("编排失败！", e.getMessage());
        }
        if(StringUtils.isNotBlank(str)){
            return success("编排部分成功，"+str+"时间交叉需手动分配");
        }
        return success("编排成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/placeArrange/groupPlaceSave")
    @ControllerInfo(value = "考场分配保存")
    public String autoArrangePlace(String examId, String placeId, String groupId, ModelMap map, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            List<EmPlaceGroup> saveAll = new ArrayList<>();
            if (StringUtils.isNotBlank(placeId)) {
                String[] placeIds = placeId.split(",");
                for (String id : placeIds) {
                    EmPlaceGroup g = new EmPlaceGroup();
                    g.setId(UuidUtils.generateUuid());
                    g.setExamId(examId);
                    g.setExamPlaceId(id);
                    g.setSchoolId(unitId);
                    g.setGroupId(groupId);
                    saveAll.add(g);
                }
            }
            String str=emPlaceGroupService.saveAndDel(examId, unitId, groupId, saveAll);
            if(StringUtils.isNotBlank(str)){
                return returnError("编排失败！", str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("编排失败！", e.getMessage());
        }
        return success("编排成功");
    }

    @RequestMapping("/examArrange/placeArrange/placeGroupIndex")
    @ControllerInfo(value = "考场分配详细设置")
    public String showPlaceGroupIndex(String examId, ModelMap map, HttpServletRequest request, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        String groupId = request.getParameter("groupId");
        String arrangePlaceNum = request.getParameter("arrangePlaceNum");
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        EmSubGroup group = emSubGroupService.findOne(groupId);
        String subId = group.getSubjectId();
        String subType = group.getSubType();
        List<EmSubGroup> subGroups = emSubGroupService.findByExamIdAndSubjectId(examId, subId);
        Set<String> gIds = new HashSet<>();
        for (EmSubGroup e : subGroups) {
            if (!StringUtils.equals(e.getSubType(), subType)) {
                gIds.add(e.getId());
            }
        }
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        List<EmPlaceGroup> emGroupPlace = emPlaceGroupService.findByExamIdAndSchoolId(examId, unitId);
        Set<String> emPlaceIds = new HashSet<>();
        Set<String> gPlaceIds = new HashSet<>();
        for (EmPlaceGroup e : emGroupPlace) {
            if (CollectionUtils.isNotEmpty(gIds) && gIds.contains(e.getGroupId())) {
                emPlaceIds.add(e.getExamPlaceId());
            }
            if (StringUtils.equals(groupId, e.getGroupId())) {
                gPlaceIds.add(e.getExamPlaceId());
            }
        }
        int arrangeStuNum = 0;
        for (EmPlace e : emPlaceList) {
            /*if (emPlaceIds.contains(e.getId())) {
                e.setCanCheck("0");
            } else {
                e.setCanCheck("1");
            }*/
            e.setCanCheck("1");//选考学考开始时间不一致，可用同一个考场
            if (gPlaceIds.contains(e.getId())) {
                e.setHasCheck("1");
                arrangeStuNum = arrangeStuNum + e.getCount();
            } else {
                e.setHasCheck("0");
            }
        }
        int arrangeNum = 0;
        Map<String, Set<String>> stuGroupMap = emStudentGroupService.findGroupMap(unitId, examId, new String[]{groupId});
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Set<String> removeStuId = new HashSet<>();
        if (filterMap.size() > 0) {
            removeStuId = filterMap.keySet();
        }
        Set<String> stus = stuGroupMap.get(groupId);
        if (CollectionUtils.isNotEmpty(stus)) {
            stus.removeAll(removeStuId);
            arrangeNum = stus.size();
        }
        if (arrangeStuNum < arrangeNum) {
            map.put("noArrangeNum", arrangeNum - arrangeStuNum);
        } else {
            map.put("noArrangeNum", 0);
        }
        map.put("arrangeNum", arrangeNum);
        map.put("arrangeStuNum", arrangeStuNum);
        map.put("emPlaceList", emPlaceList);
        map.put("groupId", groupId);
        map.put("examId", examId);
        map.put("arrangePlaceNum", arrangePlaceNum);
        //有安排学生
//		List<EmPlaceStudent> sslist = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
//        if(CollectionUtils.isNotEmpty(sslist)){
//			map.put("isEdit", false);
//		}else{
//			map.put("isEdit", true);
//		}
        boolean flag = emPlaceStudentService.hasStudent(examId, unitId);
        map.put("isEdit", flag);
        return "/exammanage/examArrange/placeGroupIndex.ftl";
    }

    @RequestMapping("/examArrange/placeArrange/page")
    @ControllerInfo(value = "考场分配设置")
    public String showPlaceArrangeIndex(String examId, ModelMap map, HttpServletRequest request, HttpSession httpSession) {
        //排考总人数
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        List<EmSubGroup> grouplist = emSubGroupService.findListByExamId(examId);
        if (CollectionUtils.isEmpty(grouplist)) {
            //return errorFtl(map,"没有科目组");
        }
        map.put("grouplist", grouplist);
        Set<String> groupIds = EntityUtils.getSet(grouplist, "id");
        Map<String, Set<String>> stuGroupMap = emStudentGroupService.findGroupMap(unitId, examId, groupIds.toArray(new String[0]));
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Set<String> removeStuId = new HashSet<>();
        if (filterMap.size() > 0) {
            removeStuId = filterMap.keySet();
        }

        Map<String, Set<String>> placeGroupMap = emPlaceGroupService.findGroupMap(unitId, groupIds.toArray(new String[0]));
        List<EmPlace> placeAlls = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        Map<String, EmPlace> placeMap = EntityUtils.getMap(placeAlls, "id");
        List<EmPlaceGroupDto> dtolist = new ArrayList<>();

        for (EmSubGroup g : grouplist) {
            EmPlaceGroupDto dto = new EmPlaceGroupDto();
            dto.setGroupId(g.getId());
            dto.setGroupName(g.getGroupName());
            Set<String> stus = stuGroupMap.get(g.getId());
            if (CollectionUtils.isNotEmpty(stus)) {
                stus.removeAll(removeStuId);
                dto.setArrangeNum(stus.size());
            }
            Set<String> placeIds = placeGroupMap.get(g.getId());
            if (CollectionUtils.isEmpty(placeIds)) {
                dto.setNoArrangeStuNum(dto.getArrangeNum());
            } else {
                for (String id : placeIds) {
                    if (!placeMap.containsKey(id)) {
                        continue;
                    }
                    dto.setArrangePlaceNum(dto.getArrangePlaceNum() + 1);
                    dto.setArrangeStuNum(dto.getArrangeStuNum() + placeMap.get(id).getCount());
                }
                if (dto.getArrangeStuNum() < dto.getArrangeNum()) {
                    dto.setNoArrangeStuNum(dto.getArrangeNum() - dto.getArrangeStuNum());
                }
            }
            dtolist.add(dto);
        }
        map.put("dtolist", dtolist);
        //有安排学生
//		List<EmPlaceStudent> sslist = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
//        if(CollectionUtils.isNotEmpty(sslist)){
//			map.put("isEdit", false);
//		}else{
//			map.put("isEdit", true);
//		}
        boolean flag = emPlaceStudentService.hasStudent(examId, unitId);
        map.put("isEdit", flag);
        map.put("examInfo", examInfo);
        return "/exammanage/examArrange/placeArrange.ftl";
    }

    @RequestMapping("/examArrange/placeIndex/page")
    @ControllerInfo(value = "考场设置")
    public String showPlaceIndex(String examId, ModelMap map, HttpSession httpSession) {
        //排考总人数
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examInfo", examInfo);
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
            map.put("emPlaceList", emPlaceList);
            //有安排学生
//			List<EmPlaceStudent> sslist = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
//	        if(CollectionUtils.isNotEmpty(sslist)){
//				map.put("isEdit", false);
//			}else{
//				map.put("isEdit", true);
//			}
            boolean flag = emPlaceStudentService.hasStudent(examId, unitId);
            map.put("isEdit", flag);
            return "/exammanage/examArrange/placeGkIndex.ftl";
        } else {
            List<Student> studentList = findStudentByExamId(examInfo, unitId, null, false, null);
            Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
            Set<String> stuIds = new HashSet<String>();
            Set<String> removeStuId = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(studentList)) {
                stuIds = EntityUtils.getSet(studentList, "id");
            }
            if (filterMap.size() > 0) {
                removeStuId = filterMap.keySet();
            }
            stuIds.removeAll(removeStuId);
            map.put("allstuNum", stuIds.size());
            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);

            int arrangeNum = findArrangeNum(emPlaceList);
            map.put("arrangeNum", arrangeNum);

            int arrangeStuNum = findArrangeStuNum(examId, unitId, emPlaceList, null);
            map.put("arrangeStuNum", arrangeStuNum);
            map.put("examInfo", examInfo);

            map.put("emPlaceList", emPlaceList);
            Map<String, Integer> sameMap = pandun(emPlaceList);
            if (sameMap.size() > 0) {
                String mymess = makeSamePlace(sameMap);
                map.put("mymess", mymess);
            }
            //有安排学生
//			List<EmPlaceStudent> sslist = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
            boolean flag = emPlaceStudentService.hasStudent(examId, unitId);
            map.put("isEdit", flag);
//	        if(CollectionUtils.isNotEmpty(sslist)){
//				map.put("isEdit", false);
//			}else{
//				map.put("isEdit", true);
//			}
            return "/exammanage/examArrange/placeIndex.ftl";
        }

    }

    private int findArrangeNum(List<EmPlace> emPlaceList) {
        int count = 0;
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            for (EmPlace pp : emPlaceList) {
                count = count + pp.getCount();
            }
        }
        return count;
    }

    @RequestMapping("/examArrange/placeAdd/page")
    @ControllerInfo(value = "添加考场")
    public String addEmPlace(String examId, ModelMap map, HttpSession httpSession) {
        //排考总人数
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("isgk", examInfo.getIsgkExamType());
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            map.put("examInfo", examInfo);
            return "/exammanage/examArrange/placeAdd.ftl";
        }
        List<Student> studentList = findStudentByExamId(examInfo, unitId, null, false, null);

        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Set<String> stuIds = new HashSet<String>();
        Set<String> removeStuId = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(studentList)) {
            stuIds = EntityUtils.getSet(studentList, "id");
        }
        if (filterMap.size() > 0) {
            removeStuId = filterMap.keySet();
        }
        stuIds.removeAll(removeStuId);
        int allCount = stuIds.size();
        int arrangeCount = 0;
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            for (EmPlace em : emPlaceList) {
                arrangeCount = arrangeCount + em.getCount();
            }
        }
        if (allCount <= arrangeCount) {
            //无需安排
            return promptFlt(map, "考场足够，无需安排");
        }
        map.put("allnum", allCount - arrangeCount);
        map.put("examInfo", examInfo);
        return "/exammanage/examArrange/placeAdd.ftl";
    }

    @ResponseBody
    @RequestMapping("/examArrange/placeSave")
    @ControllerInfo(value = "保存添加考场")
    public String placeSave(EmPlaceSaveDto dto, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(dto.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            String placeIds = dto.getPlaceIds();
            if (StringUtils.isBlank(placeIds)) {
                return error("没有需要添加的考场！");
            }
            String[] pIds = placeIds.split(",");
            if (pIds == null || pIds.length <= 0) {
                return error("没有需要添加的考场！");
            }
            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(dto.getExamId(), unitId, false);
            boolean flag = false;//是不是存在已使用的场地
            boolean flag2 = false;//超过999
            Set<String> oldPlaceIds = new HashSet<String>();
            int ii = 0;
            if (CollectionUtils.isNotEmpty(emPlaceList)) {
                String pcode = emPlaceList.get(emPlaceList.size() - 1).getExamPlaceCode();
                ii = Integer.parseInt(pcode);
                oldPlaceIds = EntityUtils.getSet(emPlaceList, "placeId");
            }
            if (ii >= 999999999) {
                //已经超过999999999--数据库参数是varchar(9)--，请回到列表，如果考场数少于999999999，那么请先页面保存，自动降低考场编号最大值
                return error("考场编号已达到最大值999999999，不能继续添加。");
            }
            EmPlace e = null;
            List<EmPlace> insertEmPList = new ArrayList<EmPlace>();
            DecimalFormat countFormat = new DecimalFormat("000");
            for (String p : pIds) {
                if (oldPlaceIds.contains(p)) {
                    if (!flag) {
                        flag = true;
                    }
                    continue;
                }
                e = new EmPlace();
                e.setExamId(dto.getExamId());
                e.setCount(dto.getAvgCount());
                e.setSchoolId(unitId);
                int code = ii + 1;
                //默认三位 但是也可超过3位
                String strValue = String.valueOf(code);
                if (code <= 999999999) {
                    strValue = countFormat.format(code);
                } else {
                    flag2 = true;
                    break;
                }
                e.setExamPlaceCode(strValue);
                e.setId(UuidUtils.generateUuid());
                e.setPlaceId(p);
                ii++;
                insertEmPList.add(e);
            }
            if (CollectionUtils.isNotEmpty(insertEmPList)) {
                emPlaceService.insertEmPlaceList(insertEmPList, null);
                String ss = "";
                if (flag) {
                    ss = "部分的场地都已经被使用";
                }
                if (flag2) {
                    //已经超过999--数据库参数是char(3)
                    if (StringUtils.isNotBlank(ss)) {
                        ss = ss + ",考场编号已达到999，不能继续添加场地";
                    } else {
                        ss = "考场编号已达到999，部分场地不能添加";
                    }
                }
                if (StringUtils.isNotBlank(ss)) {
                    return success("保存成功，但" + ss + "！");
                }

            } else {
                if (flag) {
                    return error("所选择的教室其中已经有被添加为考场！");
                }
                if (flag2) {
                    //已经超过999--数据库参数是char(3)--，请回到列表，如果考场数少于999，那么请先页面保存，自动降低考场编号最大值。
                    return error("考场编号已达到最大值999，不能继续添加！");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/placeSaveAll")
    @ControllerInfo(value = "保存考场列表")
    public String placeSaveAll(EmPlaceSaveDto dto, String[] ids, String num, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(dto.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();


            List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(dto.getExamId(), unitId, false);
            Set<String> delIds = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(emPlaceList)) {
                delIds = EntityUtils.getSet(emPlaceList, "id");
            }

            List<EmPlace> insertEmPList = new ArrayList<EmPlace>();
            List<EmPlace> list = dto.getEmPlaceList();
            if (CollectionUtils.isNotEmpty(list)) {
                for (EmPlace ee : list) {
                    if (ee == null || StringUtils.isBlank(ee.getId())) {
                        continue;
                    }
                    delIds.remove(ee.getId());
                    ee.setExamId(dto.getExamId());
                    ee.setSchoolId(unitId);
                    if (ArrayUtil.toList(ids).contains(ee.getId())) {
                        ee.setCount(Integer.parseInt(num));
                    }
                    insertEmPList.add(ee);
                }
            }
            String[] arr = null;
            if (delIds.size() > 0) {
                arr = delIds.toArray(new String[]{});
            }
            Map<String, Integer> map = pandun(insertEmPList);
            if (map.size() > 0) {
                String mess = makeSamePlace(map);
                return error("存在重复使用考场，包括" + mess + "，请删除重复再保存");
            }
            emPlaceService.insertEmPlaceList(insertEmPList, arr);

        } catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功");
    }

    private String makeSamePlace(Map<String, Integer> countByPlaceIds) {
        String str = "";
        if (countByPlaceIds.size() > 0) {
            Set<String> ids = countByPlaceIds.keySet();
            Map<String, String> placeMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(ids.toArray(new String[]{})), new TR<Map<String, String>>() {
            });

            for (String s : ids) {
                if (placeMap.containsKey(s)) {
                    str = str + "," + placeMap.get(s) + "(" + countByPlaceIds.get(s) + ")";
                }
            }
        }
        if (StringUtils.isNotBlank(str)) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * 查出场地多次使用的数据
     *
     * @param insertEmPList
     * @return
     */
    private Map<String, Integer> pandun(List<EmPlace> insertEmPList) {
        Map<String, Integer> countByPlaceIds = new HashMap<String, Integer>();
        for (EmPlace e : insertEmPList) {
            if (!countByPlaceIds.containsKey(e.getPlaceId())) {
                countByPlaceIds.put(e.getPlaceId(), 1);
            } else {
                countByPlaceIds.put(e.getPlaceId(), countByPlaceIds.get(e.getPlaceId()) + 1);
            }
        }
        Map<String, Integer> returnCountByPlaceIds = new HashMap<String, Integer>();
        for (String key : countByPlaceIds.keySet()) {
            if (countByPlaceIds.get(key) > 1) {
                returnCountByPlaceIds.put(key, countByPlaceIds.get(key));
            }
        }
        return returnCountByPlaceIds;

    }

    @RequestMapping("/examArrange/placeStudentIndex/page")
    @ControllerInfo(value = "考场考生设置")
    public String placeStudentIndex(String examId, HttpServletRequest request, ModelMap map, HttpSession httpSession) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        List<EmPlace> placeList = new ArrayList<>();
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
            List<EmSubGroup> groupList = emSubGroupService.findListByExamId(examId);
            if (CollectionUtils.isEmpty(groupList)) {
                map.put("placeList", placeList);
                return "/exammanage/examArrange/placeStudentIndex.ftl";
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
                placeList = emPlaceService.findListByIdIn(placeIds.toArray(new String[0]));
                Collections.sort(placeList, new Comparator<EmPlace>() {
                    @Override
                    public int compare(EmPlace o1, EmPlace o2) {
                        return o1.getExamPlaceCode().compareTo(o2.getExamPlaceCode());
                    }
                });
            }
        } else {
            map.put("isgk", "0");
            placeList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        }
        map.put("placeList", placeList);
        map.put("examId", examId);

        Boolean canEdit = true;
        if (ExammanageConstants.TKLX_4.equals(examInfo.getExamUeType())) {
            canEdit = false;
        }
        map.put("canEdit", canEdit);
        return "/exammanage/examArrange/placeStudentIndex.ftl";
    }

    @RequestMapping("/examArrange/placeStudentList/page")
    @ControllerInfo(value = "考场考生设置")
    public String placeStudentList(String examId, String examPlaceId, String groupId, ModelMap map, HttpSession httpSession) {
        List<EmPlaceStudent> list = new ArrayList<>();
        if (StringUtils.isNotBlank(groupId)) {//36197328113471912757457677572188,51072854271268731013499092797427
            list = emPlaceStudentService.findByExamPlaceIdAndGroupId(groupId, examPlaceId);
        } else {
            list = emPlaceStudentService.findByExamPlaceId(examPlaceId);
        }
        List<EmStudentDto> dtoList = new ArrayList<EmStudentDto>();
        if (CollectionUtils.isNotEmpty(list)) {
            EmStudentDto dto = null;
            EmPlace emPlace = emPlaceService.findByEmPlaceId(examPlaceId);
            Map<String, Student> studentMap = new HashMap<String, Student>();
            HashMap<String, String> classMap = new HashMap<String, String>();
            Set<String> stuIds = EntityUtils.getSet(list, "studentId");
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
                dto.setPlaceName(emPlace.getPlaceName());
                dto.setSeatNum(es.getSeatNum());
                dto.setSeatNumInt(Integer.parseInt(es.getSeatNum()));
                dtoList.add(dto);
            }
        }
        Collections.sort(dtoList, new Comparator<EmStudentDto>() {
            @Override
            public int compare(EmStudentDto o1, EmStudentDto o2) {
                return o1.getSeatNumInt() - o2.getSeatNumInt();
            }

        });

        map.put("dtoList", dtoList);

        return "/exammanage/examArrange/placeStudentList.ftl";
    }

    @ResponseBody
    @RequestMapping("/examArrange/saveExportAuto")
    @ControllerInfo(value = "自动导入")
    public String saveExportAuto(String examId, HttpServletRequest request, HttpSession httpSession) {
        try {
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null) {
                return error("考试已不存在！");
            }
            String chooseType = request.getParameter("chooseType");
//			if(StringUtils.equals(chooseType, "1")) {
//				//随机排序
//				emPlaceStudentService.autoByRandom(unitId,examId,examInfo.getIsgkExamType());
//			}else {
//				//按考号顺序
            String str=emPlaceStudentService.autoByExamNum(unitId, examId, examInfo.getIsgkExamType(), chooseType);
            if(StringUtils.isNotBlank(str)){
                return returnError("保存失败！", str);
            }
//			}
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
        return success("保存成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/clearAll")
    @ControllerInfo(value = "清除所有")
    public String clearAll(String examId, HttpSession httpSession) {
        try {
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            emPlaceStudentService.deleteByExamId(examId, unitId);

        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }


//	private Grade showGrade(String schoolId,EmExamInfo examInfo){
//		int section = NumberUtils.toInt(examInfo.getGradeCodes().substring(0,1));
//		int afterGradeCode = NumberUtils.toInt(examInfo.getGradeCodes().substring(1,2));
//		int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(examInfo.getAcadyear(), "-"));
//		String openAcadyear = (beforeSelectAcadyear-afterGradeCode+1)+"-"+(beforeSelectAcadyear-afterGradeCode+2);
//		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchidSectionAcadyear(schoolId, openAcadyear,new Integer[]{section}),new TR<List<Grade>>(){});
//		if(CollectionUtils.isNotEmpty(gradeList)){
//			return gradeList.get(0);
//		}
//		return null;
//	}

    @RequestMapping("/examArrange/examineeNumberIndex/page")
    @ControllerInfo(value = "考号设置")
    public String examineeNumberIndex(String examId, ModelMap map) {
        LoginInfo login = getLoginInfo();
        String unitId = login.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        Set<String> classIds = showFindClass(unitId, examInfo).stream().map(Clazz::getId).collect(Collectors.toSet());
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
        });
		/*List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
		if(CollectionUtils.isNotEmpty(classInfoList)){
			//暂不考虑教学班
			Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
			clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>(){} );
		}*/
        map.put("clazzList", clazzList);
        map.put("examId", examId);
        map.put("examInfo", examInfo);
//		List<EmPlaceStudent> sslist = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
//		if(CollectionUtils.isNotEmpty(sslist)){
//			map.put("isEdit", false);
//		}else{
//			map.put("isEdit", true);
//		}
        boolean flag = emPlaceStudentService.hasStudent(examId, unitId);
        map.put("isEdit", flag);

        Boolean canEdit = true;
        if (ExammanageConstants.TKLX_4.equals(examInfo.getExamUeType())) {
            canEdit = false;
        }
        map.put("canEdit", canEdit);
        return "/exammanage/examArrange/examineeNumberIndex.ftl";

    }


    @RequestMapping("/examArrange/examineeNumberList/page")
    @ControllerInfo("考号列表")
    public String showExamineeNumberList(String examId, String searchClassId, String searchFilterType, ModelMap map, HttpSession httpSession) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        //行政班
        Map<String, String> classMap = new LinkedHashMap<String, String>();
        List<Student> studentList = new ArrayList<>();
        if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
            studentList = findGkStudentByExamId(examInfo, unitId, searchClassId, false, classMap);
        } else {
            studentList = findStudentByExamId(examInfo, unitId, searchClassId, false, classMap);
        }

        //exammanageExamNumService
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Map<String, String> stuNumberMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
        List<EmStudentDto> stuDtoList = new ArrayList<EmStudentDto>();
        EmStudentDto dto = new EmStudentDto();
        if (CollectionUtils.isNotEmpty(studentList)) {
            for (Student stu : studentList) {
                dto = new EmStudentDto();
                dto.setStudent(stu);
                if (classMap.containsKey(stu.getClassId())) {
                    dto.setClassName(classMap.get(stu.getClassId()));
                }
                if (stuNumberMap.containsKey(stu.getId())) {
                    dto.setExamNumber(stuNumberMap.get(stu.getId()));
                }
                if (!filterMap.containsKey(stu.getId())) {
                    stuDtoList.add(dto);
                }
            }
        }
        map.put("stuDtoList", stuDtoList);
        map.put("examId", examId);
        map.put("examInfo", examInfo);
        map.put("unitId", unitId);

//       List<EmPlaceStudent> sslist = emPlaceStudentService.findByExamIdAndSchoolIdAndGroupId(examId, unitId, null);
//       if(CollectionUtils.isNotEmpty(sslist)){
//			map.put("isEdit", false);
//		}else{
//			map.put("isEdit", true);
//		}
//        boolean flag = emPlaceStudentService.hasStudent(examId,unitId);
//		map.put("isEdit", flag);
        return "/exammanage/examArrange/examineeNumberList.ftl";
    }

    @ResponseBody
    @RequestMapping("/examArrange/examineeNumberSave")
    @ControllerInfo(value = "保存考号信息")
    public String examineeNumberSave(EmFilterSaveDto saveDto, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(saveDto.getExamId());
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo(httpSession);
            String unitId = info.getUnitId();
            String examId = saveDto.getExamId();
            List<EmStudentDto> stuDtoList = saveDto.getStuDtoList();
            //考号不能重复--传递页面不能重复
            Map<String, String> stuNumberMap = emExamNumService.findBySchoolIdAndExamId(unitId, examId);
            //stuNumberMap去除页面维护数据  不能重复
            if (CollectionUtils.isNotEmpty(stuDtoList)) {
                Set<String> studentIds = EntityUtils.getSet(stuDtoList, "studentId");
                if (studentIds.size() > 0) {
                    for (String s : studentIds) {
                        stuNumberMap.remove(s);
                    }

                }
                Set<String> stuNumber = new HashSet<String>();
                if (stuNumberMap.size() > 0) {
                    for (String k : stuNumberMap.keySet()) {
                        stuNumber.add(stuNumberMap.get(k));
                    }
                }
                List<EmExamNum> fList = new ArrayList<EmExamNum>();
                EmExamNum f = null;
                String str = "";
                for (EmStudentDto e : stuDtoList) {
                    if (stuNumber.contains(e.getExamNumber())) {
                        str = str + "、" + e.getStudentName();
                        continue;
                    }
                    f = new EmExamNum();
                    f.setExamId(examId);
                    f.setSchoolId(unitId);
                    f.setStudentId(e.getStudentId());
                    f.setId(UuidUtils.generateUuid());
                    f.setExamNumber(e.getExamNumber());
                    fList.add(f);
                }
                if (StringUtils.isNotBlank(str)) {
                    str = str.substring(1);
                    return error(str + "学生考号重复，请修改后提交！");
                }
                emExamNumService.addOrDel(fList, examId, studentIds);
            } else {
                return error("没有需要保存的数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/clearNumber")
    @ControllerInfo(value = "清空考号信息")
    public String clearNumber(String examId, HttpSession httpSession) {
        try {
            LoginInfo loginInfo = getLoginInfo();
            emExamNumService.deleteBy(loginInfo.getUnitId(), examId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }

    @ResponseBody
    @RequestMapping("/examArrange/examineeNumberAutoSave")
    @ControllerInfo(value = "自动设置考号信息")
    public String examineeNumberAutoSave(String examId, String chooseType, HttpSession httpSession) {
        try {
            EmExamInfo examInfo = emExamInfoService.findOne(examId);
            if (examInfo == null || examInfo.getIsDeleted() == 1) {
                return error("考试已不存在！");
            }
            LoginInfo info = getLoginInfo();
            String unitId = info.getUnitId();
            //--全部考生
            List<Student> stuList = new ArrayList<>();
            if (StringUtils.equals(examInfo.getIsgkExamType(), "1")) {
                //新高考
                stuList = findGkStudentByExamId(examInfo, unitId, null, true, null);
            } else {
                //否
                stuList = findStudentByExamId(examInfo, unitId, null, true, null);
            }
            List<EmExamNum> fList = new ArrayList<EmExamNum>();
            EmExamNum f = null;
            if ("1".equals(chooseType)) {
                //TODO 按学籍号
                for (Student s : stuList) {
                    f = new EmExamNum();
                    f.setExamId(examId);
                    f.setSchoolId(unitId);
                    f.setStudentId(s.getId());
                    f.setId(UuidUtils.generateUuid());
                    f.setExamNumber(s.getUnitiveCode());
                    fList.add(f);
                }
            } else {
                //随机
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String eeee = sdf.format(dt);//前缀
                int i = 1;
                for (Student s : stuList) {
                    f = new EmExamNum();
                    f.setExamId(examId);
                    f.setSchoolId(unitId);
                    f.setStudentId(s.getId());
                    f.setId(UuidUtils.generateUuid());
                    String zzz = StringUtils.leftPad(i + "", 4, '0');
                    if (i > 9999) {
                        zzz = String.valueOf(i);
                    }
                    f.setExamNumber(eeee + zzz);
                    fList.add(f);
                    i++;
                }
            }

            emExamNumService.insertAllAndDeleteAll(fList, examId, unitId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError("操作失败！", e.getMessage());
        }
        return success("操作成功");
    }

    /**
     * @param examId
     * @param unitId
     * @param flag   是否排除不排考
     * @return
     */
    private List<Student> findGkStudentByExamId(EmExamInfo examInfo, String unitId, String searchClassId, boolean flag, Map<String, String> classMap) {
        List<Student> studentList = new ArrayList<>();
        Set<String> classIds = new HashSet<>();
        if (StringUtils.isNotBlank(searchClassId)) {
            classIds.add(searchClassId);
        } else {
            classIds = showFindClass(unitId, examInfo).stream().map(Clazz::getId).collect(Collectors.toSet());
			/*List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
			if(CollectionUtils.isNotEmpty(classInfoList)){
				//暂不考虑教学班
				classIds = EntityUtils.getSet(classInfoList, "classId");
			}*/
        }
        if (CollectionUtils.isNotEmpty(classIds)) {
            List<Student> studentListAll = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentListAll)) {
                Set<String> stuIds = studentListAll.stream().map(Student::getId).collect(Collectors.toSet());
                List<EmEnrollStudent> emStus = emEnrollStudentService.findByExamIdAndSchoolIdAndStudentIdIn(examInfo.getId(), unitId, stuIds.toArray(new String[0]));
                Set<String> someStuIds = emStus.stream().map(EmEnrollStudent::getStudentId).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(someStuIds)) {
                    studentListAll.forEach(stu -> {
                        if (someStuIds.contains(stu.getId())) {
                            studentList.add(stu);
                        }
                    });
                }
//				studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})),new TR<List<Student>>(){});
                if (classMap != null) {
                    List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                    });
                    if (CollectionUtils.isNotEmpty(classList)) {
                        for (Clazz z : classList) {
                            classMap.put(z.getId(), z.getClassNameDynamic());
                        }
                    }
                }
            }
        }
        if (flag) {
            Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examInfo.getId(), unitId, ExammanageConstants.FILTER_TYPE1);
            List<Student> newstudentList = new ArrayList<Student>();
            for (Student s : studentList) {
                if (!filterMap.containsKey(s.getId())) {
                    newstudentList.add(s);
                }
            }
            return newstudentList;
        }
        return studentList;
    }

    /**
     * @param examId
     * @param unitId
     * @param searchClassId
     * @param flag              是否排除不排考
     * @param classMap//用于之后的命名 为null 不进行返回
     * @return
     */
    private List<Student> findStudentByExamId(EmExamInfo examInfo, String unitId, String searchClassId, boolean flag, Map<String, String> classMap) {
        List<Student> studentList = new ArrayList<>();
        Set<String> classIds = new HashSet<>();
        if (StringUtils.isNotBlank(searchClassId)) {
            classIds.add(searchClassId);
        } else {
            classIds = showFindClass(unitId, examInfo).stream().map(Clazz::getId).collect(Collectors.toSet());
			/*List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
			if(CollectionUtils.isNotEmpty(classInfoList)){
				//暂不考虑教学班
				classIds = EntityUtils.getSet(classInfoList, "classId");
			}*/
        }
        if (CollectionUtils.isNotEmpty(classIds)) {
            studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (classMap != null) {
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }

        }
        if (flag) {
            Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examInfo.getId(), unitId, ExammanageConstants.FILTER_TYPE1);
            List<Student> newstudentList = new ArrayList<Student>();
            for (Student s : studentList) {
                if (!filterMap.containsKey(s.getId())) {
                    newstudentList.add(s);
                }
            }
            return newstudentList;
        }
        return studentList;
    }
}



