package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleUserRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.DiathesisMsgDto;
import net.zdsoft.diathesis.data.dto.DiathesisRecordDto;
import net.zdsoft.diathesis.data.dto.DiathesisRecordInfoDto;
import net.zdsoft.diathesis.data.dto.DiathesisStudentDto;
import net.zdsoft.diathesis.data.dto.FileDto;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroup;
import net.zdsoft.diathesis.data.entity.DiathesisOption;
import net.zdsoft.diathesis.data.entity.DiathesisProject;
import net.zdsoft.diathesis.data.entity.DiathesisProjectEx;
import net.zdsoft.diathesis.data.entity.DiathesisRecord;
import net.zdsoft.diathesis.data.entity.DiathesisRecordInfo;
import net.zdsoft.diathesis.data.entity.DiathesisRecordMessage;
import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.diathesis.data.entity.DiathesisStructure;
import net.zdsoft.diathesis.data.service.DiathesisCustomAuthorService;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupService;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupStuService;
import net.zdsoft.diathesis.data.service.DiathesisOptionService;
import net.zdsoft.diathesis.data.service.DiathesisProjectExService;
import net.zdsoft.diathesis.data.service.DiathesisProjectService;
import net.zdsoft.diathesis.data.service.DiathesisRecordInfoService;
import net.zdsoft.diathesis.data.service.DiathesisRecordMessageService;
import net.zdsoft.diathesis.data.service.DiathesisRecordService;
import net.zdsoft.diathesis.data.service.DiathesisRoleService;
import net.zdsoft.diathesis.data.service.DiathesisSetService;
import net.zdsoft.diathesis.data.service.DiathesisStructureService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.tutor.remote.service.TutorRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 写实记录留言板
 *
 * @Author: panlf
 * @Date: 2019/10/29 13:54
 */
@RequestMapping("/diathesis/recordMessage")
@RestController
public class DiathesisRecordMessageAction extends BaseAction {
    @Autowired
    private TutorRemoteService tutorRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private DiathesisProjectExService diathesisProjectServiceEx;
    @Autowired
    private DiathesisRecordService diathesisRecordService;
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private CustomRoleUserRemoteService customRoleUserRemoteService;
    @Autowired
    private DiathesisRoleService diathesisRoleService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private DiathesisRecordMessageService diathesisRecordMessageService;
    @Autowired
    private DiathesisRecordInfoService diathesisRecordInfoService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;
    @Autowired
    private DiathesisStructureService diathesisStructureService;
    @Autowired
    private DiathesisOptionService diathesisOptionService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;

    @Autowired
    private DiathesisMutualGroupStuService diathesisMutualGroupStuService;
    @Autowired
    private DiathesisMutualGroupService diathesisMutualGroupService;
    @Autowired
    private DiathesisSetService diathesisSetService;



    /**
     * 获取该老师下面可以审核写实记录 的学生
     *
     * @return
     */
    @GetMapping("/stuTreeForTeacher")
    public String stuTreeForTeacher() {
        List<Json> classArr=new ArrayList<>();
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        Set<String> stuIds = new HashSet<>();
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisProject> projectList = diathesisProjectService.findByUnitIdAndProjectTypeIn(usingUnitId, new String[]{DiathesisConstant.PROJECT_RECORD});
        List<DiathesisProjectEx> exList = diathesisProjectServiceEx.findByUnitIdAndProjectIdIn(unitId, EntityUtils.getList(projectList, x -> x.getId()));

        //key: roleCode   value: List<Project>
        HashMap<String, List<String>> proRoleMap = new HashMap<>();
        for (DiathesisProjectEx ex : exList) {
            String[] split = ex.getAuditorTypes().split(",");
            for (String s : split) {
                proRoleMap.putIfAbsent(s, new ArrayList<>());
                proRoleMap.get(s).add(ex.getProjectId());
            }
        }

        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<CustomRole> roleList = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId", "subsystems"}, new String[]{unitId, "78,"}), CustomRole.class);
        String[] roleIds = roleList.stream().filter(x -> !DiathesisConstant.ROLE_CODE_LIST.contains(x.getRoleCode())).map(x -> x.getId()).toArray(String[]::new);

        // 自定义角色下 学生的id
        if (roleIds != null && roleIds.length > 0) {
            List<CustomRoleUser> roleUserList = SUtils.dt(customRoleUserRemoteService.findListByIn("roleId", roleIds), CustomRoleUser.class);
            if (CollectionUtils.isNotEmpty(roleUserList)) {
                Map<String, String> roleMap = EntityUtils.getMap(roleList, x -> x.getId(), x -> x.getRoleCode());
                List<String> curRoleCodeList = roleUserList.stream().filter(x -> x.getUserId().equals(userId)).map(x -> roleMap.get(x.getRoleId())).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(curRoleCodeList)) {
                    Set<String> projectIds = new HashSet<>();
                    for (String s : curRoleCodeList) {
                        if (CollectionUtils.isNotEmpty(proRoleMap.get(s))) {
                            projectIds.addAll(proRoleMap.get(s));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(projectIds)) {
                        List<DiathesisRecord> records = diathesisRecordService.findListByUnitIdAndProjectIdIn(unitId, semester.getAcadyear(), semester.getSemester(), projectIds.toArray(new String[0]));
                        Set<String> stuSet = EntityUtils.getSet(records, x -> x.getStuId());
                        if (CollectionUtils.isNotEmpty(stuSet)) stuIds.addAll(stuSet);
                    }
                }
            }
        }

        //管理员：1;  年级老师 2;  班主任 :3     导师: 4
        HashMap<String, String> map = new HashMap<>();
        map.put(DiathesisConstant.ROLE_2, DiathesisConstant.ROLE_GRADE);
        map.put(DiathesisConstant.ROLE_3, DiathesisConstant.ROLE_CLASS);
        map.put(DiathesisConstant.ROLE_4, DiathesisConstant.ROLE_TUTOR);
        Map<String, List<String>> role = diathesisRoleService.findRoleByUserId(unitId, userId);
        addStuIdSet(unitId, stuIds, role, DiathesisConstant.ROLE_2, semester, proRoleMap, map);
        addStuIdSet(unitId, stuIds, role, DiathesisConstant.ROLE_3, semester, proRoleMap, map);
        addStuIdSet(unitId, stuIds, role, DiathesisConstant.ROLE_4, semester, proRoleMap, map);

        Json json = new Json();
        if (CollectionUtils.isEmpty(stuIds)) return json.toJSONString();
        String[] stuIdArr = stuIds.toArray(new String[0]);

        List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIdArr), Student.class);
        Set<String> classIds = EntityUtils.getSet(students, x -> x.getClassId());
        List<Clazz> classList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), Clazz.class);
        Map<String, List<Json>> stuMap = students.stream().collect(Collectors.groupingBy(x -> x.getClassId(), Collectors.mapping(x -> {
            Json json1 = new Json();
            json1.put("studentId", x.getId());
            json1.put("studentName", x.getStudentName());
            return json1;
        }, Collectors.toList())));
        classArr = classList.stream().sorted((x, y) -> {
            Integer sectionX = x.getSection();
            Integer sectionY = y.getSection();
            if (sectionX == null) return 1;
            if (sectionY == null) return -1;
            if (!sectionX.equals(sectionY)) return sectionX - sectionY;

            String yearX = x.getAcadyear();
            String yearY = y.getAcadyear();
            if (StringUtils.isBlank(yearX)) return 1;
            if (StringUtils.isBlank(yearY)) return -1;
            if (!yearX.equals(yearY)) return yearX.compareTo(yearY);

            String codeX = x.getClassCode();
            String codeY = y.getClassCode();
            if (StringUtils.isBlank(codeX)) return 1;
            if (StringUtils.isBlank(codeY)) return -1;
            return codeX.compareTo(codeY);
        }).map(x -> {
            Json c = new Json();
            c.put("classId", x.getId());
            c.put("className", x.getClassNameDynamic());
            c.put("studentList", stuMap.get(x.getId()));
            return c;
        }).collect(Collectors.toList());

        return JSON.toJSONString(classArr);
    }

    private void addStuIdSet(String unitId, Set<String> stuIds, Map<String, List<String>> role,
                             String role2, Semester semester, HashMap<String, List<String>> proRoleMap,
                             HashMap<String, String> map) {
        List<String> proIds = proRoleMap.get(map.get(role2));
        List<String> stuIdList = new ArrayList<>();
        List<String> ids = role.get(role2);
        if (CollectionUtils.isEmpty(ids)) return;
        if (role2.equals(DiathesisConstant.ROLE_2)) {
            List<Student> stuList = SUtils.dt(studentRemoteService.findByGradeIds(ids.toArray(new String[0])), Student.class);
            List<String> studentIds = EntityUtils.getList(stuList, x -> x.getId());
            if (CollectionUtils.isNotEmpty(studentIds)) stuIdList.addAll(studentIds);
        } else if (role2.equals(DiathesisConstant.ROLE_3)) {
            List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(ids.toArray(new String[0])), Student.class);
            List<String> studentIds = EntityUtils.getList(stuList, x -> x.getId());
            if (CollectionUtils.isNotEmpty(studentIds)) stuIdList.addAll(studentIds);
        } else if (role2.equals(DiathesisConstant.ROLE_4)) {
            stuIdList.addAll(ids);
        }

        if (CollectionUtils.isNotEmpty(stuIdList)) {
            List<DiathesisRecord> temp = diathesisRecordService.findListByUnitIdAndStuIdInAndProjectIdIn(unitId, semester.getAcadyear(), semester.getSemester(), stuIdList, proIds);
            if (CollectionUtils.isNotEmpty(temp)) stuIds.addAll(EntityUtils.getSet(temp, x -> x.getStuId()));
        }
    }


    /**
     * 获得当前 教师权限下,一个学生的所有 写实记录
     * <p>
     * //按 一级,写实 分装
     *
     * @param studentId
     * @return
     */
    @GetMapping("/recordListByStuId")
    public String recordListByStuId(String studentId) {
        if (StringUtils.isBlank(studentId)) return error("studentId不能为空");
        if(User.OWNER_TYPE_STUDENT==getLoginInfo().getOwnerType()){
            return getStuModelRecordListByStuId(studentId);
        }
        List<Json> result = new ArrayList<>();
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        String teacherId = getLoginInfo().getOwnerId();
        Set<String> auditorSet = new HashSet<>();

        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisProject> projectList = diathesisProjectService.findByUnitIdAndProjectTypeIn(usingUnitId, new String[]{DiathesisConstant.PROJECT_RECORD, DiathesisConstant.PROJECT_TOP});
        Map<String, List<DiathesisProject>> allProject = projectList.stream().collect(Collectors.groupingBy(x -> x.getProjectType()));
        List<DiathesisProjectEx> exList = diathesisProjectServiceEx.findByUnitIdAndProjectIdIn(unitId, EntityUtils.getList(allProject.get(DiathesisConstant.PROJECT_RECORD), x -> x.getId()));

//        Map<String, List<String>> role = diathesisRoleService.findRoleByUserId(unitId, userId);
//        HashMap<String, String> roleMap = new HashMap<>();
        /**
         *     1：管理员 2：年级组长 3：班主任 4:导师
         */
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
        List<String> stuIds = SUtils.dt(tutorRemoteService.getTutorStuByTeacherId(teacherId), String.class);
        if (teacherId.equals(clazz.getTeacherId()) || teacherId.equals(clazz.getViceTeacherId())) {
            auditorSet.add(DiathesisConstant.ROLE_CLASS);
        }
        if (teacherId.equals(grade.getTeacherId())) {
            auditorSet.add(DiathesisConstant.ROLE_GRADE);
        }
        if (CollectionUtils.isNotEmpty(stuIds) || stuIds.contains(studentId)) {
            auditorSet.add(DiathesisConstant.ROLE_TUTOR);
        }

        List<CustomRole> roleList = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId", "subsystems"}, new String[]{unitId, "78,"}), CustomRole.class);
        String[] roleIds = roleList.stream().filter(x -> !DiathesisConstant.ROLE_CODE_LIST.contains(x.getRoleCode())).map(x -> x.getId()).toArray(String[]::new);

        if (roleIds != null && roleIds.length > 0) {
            List<CustomRoleUser> roleUserList = SUtils.dt(customRoleUserRemoteService.findListByIn("roleId", roleIds), CustomRoleUser.class);
            if (CollectionUtils.isNotEmpty(roleUserList)) {
                Map<String, String> roleMap1 = EntityUtils.getMap(roleList, x -> x.getId(), x -> x.getRoleCode());
                List<String> curRoleCodeList = roleUserList.stream().filter(x -> x.getUserId().equals(userId)).map(x -> roleMap1.get(x.getRoleId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(curRoleCodeList)) auditorSet.addAll(curRoleCodeList);
            }
        }
        if (CollectionUtils.isEmpty(auditorSet)) return JSON.toJSONString(result);
        List<String> projectIds = exList.stream().filter(x -> {
            String[] split = x.getAuditorTypes().split(",");
            for (String s : split) {
                if (auditorSet.contains(s)) return true;
            }
            return false;
        }).map(x -> x.getProjectId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(projectIds)) return JSON.toJSONString(result);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<DiathesisRecord> recordList = diathesisRecordService.findListByUnitIdAndProjectIdInAndStuId(unitId, semester.getAcadyear(), semester.getSemester(), projectIds, studentId);
        if (CollectionUtils.isEmpty(recordList)) return JSON.toJSONString(result);
        //recordList 找到info
        setRecordResultList(result, unitId, allProject, student, recordList);
        return JSON.toJSONString(result);
    }

    private void setRecordResultList(List<Json> result, String unitId, Map<String, List<DiathesisProject>> allProject, Student student, List<DiathesisRecord> recordList) {
        List<DiathesisRecordInfo> recordInfoAll = diathesisRecordInfoService.findListByUnitIdAndRecordIds(unitId, EntityUtils.getArray(recordList, x -> x.getId(), String[]::new));

        if (CollectionUtils.isNotEmpty(recordInfoAll)) {
            List<DiathesisStructure> infos = diathesisStructureService.findListByIds(EntityUtils.getSet(recordInfoAll, x -> x.getStructureId()).toArray(new String[0]));
            infos.sort((x, y) -> x.getColNo() - y.getColNo());
            List<DiathesisOption> options = diathesisOptionService.findListByStructureIdIn(EntityUtils.getSet(infos, x -> x.getId()).toArray(new String[0]));

            Map<String, String> keyValueMap = EntityUtils.getMap(options, x -> x.getId(), x -> x.getContentTxt());
            Map<String, List<DiathesisOption>> optionMap = EntityUtils.getListMap(options, DiathesisOption::getStructureId, Function.identity());
            // Map<String, List<DiathesisRecordInfo>> recordListInfoMap = recordInfoAll.stream().collect(Collectors.groupingBy(x -> x.getRecordId()));
            Map<String, DiathesisRecordInfo> recordInfoMap = new HashMap<String, DiathesisRecordInfo>();
            if (CollectionUtils.isNotEmpty(recordInfoAll)) {
                recordInfoMap = EntityUtils.getMap(recordInfoAll, x -> x.getRecordId() + "_" + x.getStructureId());
            }


            List<DiathesisRecordDto> recordDtoList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (DiathesisRecord record : recordList) {
                DiathesisRecordDto recordDto = new DiathesisRecordDto();
                recordDto.setId(record.getId());
                recordDto.setStuId(record.getStuId());
                recordDto.setStuName(student.getStudentName());
                recordDto.setProjectId(record.getProjectId());
                recordDto.setAcadyear(record.getAcadyear());
                recordDto.setSemester(record.getSemester());
                recordDto.setCreationTime(sdf.format(record.getCreationTime()));
                recordDto.setStatus(record.getStatus());
                if (!DiathesisConstant.AUDIT_STATUS_READY.equals(record.getStatus())) {
                    recordDto.setAuditTime(sdf.format(record.getAuditTime()));
                    recordDto.setAuditor(record.getAuditor());
                    recordDto.setAuditOpinion(record.getAuditOpinion());
                }

                //当前写实记录的所有字段
                // List<DiathesisRecordInfo> recordInfoList = recordListInfoMap.get(record.getId());
                List<DiathesisRecordInfoDto> infoDtoList = new ArrayList<DiathesisRecordInfoDto>();
                //infos 排序好的字段
                for (DiathesisStructure structure : infos) {

                    if (recordInfoMap.containsKey(record.getId() + "_" + structure.getId())) {
                        DiathesisRecordInfoDto infoDto = new DiathesisRecordInfoDto();
                        infoDto.setTitle(structure.getTitle());
                        infoDto.setStructureId(structure.getId());
                        infoDto.setDataType(structure.getDataType());
                        infoDto.setIsMust(structure.getIsMust());
                        DiathesisRecordInfo recordInfo = recordInfoMap.get(record.getId() + "_" + structure.getId());
                        infoDto.setId(recordInfo.getId());
                        infoDto.setContentTxt(recordInfo.getContentTxt());
                        if (DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())) {
                            if (StringUtils.isNotBlank(recordInfo.getContentTxt())) {
                                String[] split = recordInfo.getContentTxt().split(",");
                                String resultTxt = "";
                                for (String sp : split) {
                                    resultTxt += "," + keyValueMap.get(sp);
                                }
                                infoDto.setResultTxt(resultTxt.substring(1));
                            }
                        } else if (DiathesisConstant.DATA_TYPE_4.equals(structure.getDataType())) {
                            //panlf msg
                            String contxt = recordInfo.getContentTxt();
//                            if(StringUtils.isNotBlank(contxt)){
//                                infoDto.setContentTxt(StringUtils.substringAfter(contxt, ","));
//                                infoDto.setResultTxt(StringUtils.substringBefore(contxt, ","));
//                            }
                            ArrayList<FileDto> fileDtos = new ArrayList<>();
                            if (StringUtils.isNotBlank(contxt)) {
                                String[] allFile = contxt.split(DiathesisConstant.FILE_SPLIT);
                                for (String one : allFile) {
                                    FileDto dto = new FileDto();
                                    dto.setFileName(StringUtils.substringBefore(one, ","));
                                    dto.setFilePath(StringUtils.substringAfter(one, ","));
                                    fileDtos.add(dto);
                                }
                                infoDto.setFileList(fileDtos);
                            }
                        }
                        if (DiathesisConstant.DATA_TYPE_2.equals(structure.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(structure.getDataType())) {
                            infoDto.setOptionList(optionMap.get(structure.getId()));
                        }
                        infoDtoList.add(infoDto);
                    }

                }
                recordDto.setRecordInfoList(infoDtoList);
                recordDtoList.add(recordDto);
            }
            Map<String, List<DiathesisRecordDto>> recordDtoMap = recordDtoList.stream().collect(Collectors.groupingBy(x -> x.getProjectId()));
            // Map<String, DiathesisRecordDto> recordDtoMap = EntityUtils.getMap(recordDtoList, x -> x.getProjectId()+"_"+x.getId());
            Map<String, List<DiathesisProject>> recordProMap = allProject.get(DiathesisConstant.PROJECT_RECORD).stream().collect(Collectors.groupingBy(x -> x.getParentId()));
            for (DiathesisProject project : allProject.get(DiathesisConstant.PROJECT_TOP)) {
                boolean flag = false;
                Json json = new Json();
                json.put("projectName", project.getProjectName());
                json.put("projectId", project.getId());
                List<Json> childList = new ArrayList<>();
                List<DiathesisProject> x = recordProMap.get(project.getId());
                if (CollectionUtils.isEmpty(x)) continue;
                for (DiathesisProject y : x) {
                    if (recordDtoMap.keySet().contains(y.getId())) {
                        Json c = new Json();
                        c.put("projectName", y.getProjectName());
                        c.put("projectId", y.getId());
                        c.put("recordList", recordDtoMap.get(y.getId()));
                        childList.add(c);
                        flag = true;
                    }
                }
                json.put("childList", childList);
                if (flag) {
                    result.add(json);
                }
            }
        }
    }

    /**
     * 获得某一条写实记录的所有留言
     *
     * @param recordId
     * @return
     */
    @GetMapping("/getMsgByRecordId")
    public String getMsgByRecordId(String recordId) {
        if (StringUtils.isBlank(recordId)) return error("recordId不能为空");
        List<DiathesisRecordMessage> list = diathesisRecordMessageService.findByRecordId(recordId);
        Json json = new Json();
        Json currentPeople = new Json();
        currentPeople.put("id", getLoginInfo().getUserId());
        currentPeople.put("name", getLoginInfo().getRealName());
        currentPeople.put("userType",User.OWNER_TYPE_STUDENT==getLoginInfo().getUserType()?"学生":"老师");
        json.put("currentPeople", currentPeople);

        if (CollectionUtils.isEmpty(list)) return json.toJSONString();

        List<String> userIds = EntityUtils.getList(list, x -> x.getLeaveMsgPeopleId());
        List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])), User.class);
        Map<String, User> userMap = EntityUtils.getMap(userList, x -> x.getId(), x -> x);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Json> msgList = list.stream().map(x -> {
            Json c = new Json();
            c.put("leaveMsgPeopleId", x.getLeaveMsgPeopleId());
            User leaveMsgUser = userMap.get(x.getLeaveMsgPeopleId());
            String type=User.OWNER_TYPE_STUDENT==leaveMsgUser.getOwnerType()?"学生":"老师";
            c.put("name", leaveMsgUser.getRealName()+"("+type+")");
            c.put("contentTxt", x.getContent());
            c.put("modifyTime", sdf.format(x.getModifyTime()));
            return c;
        }).collect(Collectors.toList());
        json.put("msgList", msgList);
        return json.toJSONString();
    }

    /**
     * 单条留言保存
     *
     * @param msgDto
     * @param errors
     * @return
     */
    @PostMapping("/saveMsg")
    public String saveMsg(@RequestBody DiathesisMsgDto msgDto, Errors errors) {
        if (errors.hasFieldErrors()) return error(errors.getFieldError().getDefaultMessage());
        try {
            DiathesisRecordMessage old = diathesisRecordMessageService.findByRecordIdAndLeaveMsgPeopleId(msgDto.getRecordId(), getLoginInfo().getUserId());
            DiathesisRecordMessage msg = new DiathesisRecordMessage();
            if (old == null) {
                msg.setId(UuidUtils.generateUuid());
                msg.setLeaveMsgPeopleId(getLoginInfo().getUserId());
                msg.setRecordId(msgDto.getRecordId());
                msg.setUnitId(getLoginInfo().getUnitId());

                msg.setModifyTime(new Date());
                msg.setContent(msgDto.getMsg());
            } else {
                msg = EntityUtils.copyProperties(old, msg);
                msg.setModifyTime(new Date());
                msg.setContent(msgDto.getMsg());
            }
            diathesisRecordMessageService.saveRecordMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("保存成功");
    }

    /**
     * 获取写实记录留言学生列表 --学生端
     * @return
     */
    @GetMapping("/stuTreeForStudent")
    public String getRecordMsgStudentList() {
        List<DiathesisStudentDto> result=new ArrayList<>();
        String studentId = getLoginInfo().getOwnerId();
        // 全局设置 用自己的设置
        DiathesisSet set = diathesisSetService.findByUnitId(getLoginInfo().getUnitId());
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, getLoginInfo().getUnitId()), Semester.class);
        List<String> groupIds = new ArrayList<>();

        if (set.getMutualType().equals(DiathesisConstant.MUTUAL_EVALUATE_LEADER)) {
            //组长评
            List<DiathesisMutualGroup> groupList = diathesisMutualGroupService.findByLeadIdAndSemester(studentId, semester.getAcadyear(), semester.getSemester());
            if (CollectionUtils.isEmpty(groupList)){
                return Json.toJSONString(result);
            }
            List<String> ids = groupList.stream().map(x -> x.getId()).collect(Collectors.toList());
            groupIds.addAll(ids);
        } else {
            //互评
            String groupId = diathesisMutualGroupStuService.findMutualGroupIdByStudentId(studentId, semester.getAcadyear(), semester.getSemester());
            if(StringUtils.isBlank(groupId)){
                return Json.toJSONString(result);
            }
            groupIds.add(groupId);
        }
        if (CollectionUtils.isEmpty(groupIds)) return new Json().toJSONString();
        String[] ids = EntityUtils.getArray(diathesisMutualGroupStuService.findListByMutualGroupIdIn(groupIds), x -> x.getStudentId(), String[]::new);
        result = SUtils.dt(studentRemoteService.findListByIds(ids), Student.class).stream().map(x -> {
            DiathesisStudentDto stu = new DiathesisStudentDto();
            stu.setStudentId(x.getId());
            stu.setStudentName(x.getStudentName());
            stu.setStudentCode(x.getStudentCode());
            return stu;
        }).sorted(Comparator.comparingInt(x -> studentId.equals(x.getStudentId()) ? -1 : 1)).collect(Collectors.toList());
        return JSON.toJSONString(result);
    }

    /**
     * 获得学生端 的学生写实记录  (当前学期)
     *
     * @param studentId
     * @return
     */
    public String getStuModelRecordListByStuId(String studentId) {
        List<Json> result = new ArrayList<>();
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<DiathesisRecord> recordList = diathesisRecordService.findListByAcadyearAndSemesterAndStuId(unitId, semester.getAcadyear(), semester.getSemester(), studentId);
        if (CollectionUtils.isEmpty(recordList)) return JSON.toJSONString(result);

        List<DiathesisRecordInfo> recordInfoList = diathesisRecordInfoService.findListByUnitIdAndRecordIds(unitId, EntityUtils.getArray(recordList, x -> x.getId(), String[]::new));
        //找到实际使用的设置单位
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisProject> projectList = diathesisProjectService.findByUnitIdAndProjectTypeIn(usingUnitId, new String[]{DiathesisConstant.PROJECT_RECORD, DiathesisConstant.PROJECT_TOP});
        //项目Map
        Map<String, List<DiathesisProject>> allProject = projectList.stream().collect(Collectors.groupingBy(x -> x.getProjectType()));
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        if(student==null){
            return error("找不到这个学生");
        }{
            setRecordResultList(result, unitId, allProject, student, recordList);
        }
        return JSON.toJSONString(result);
    }
}

