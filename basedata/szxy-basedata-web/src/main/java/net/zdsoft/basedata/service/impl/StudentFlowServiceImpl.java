/*
 * Project: v7
 * Author : shenke
 * @(#) StudentNormalFlowServiceImpl.java Created on 2016-8-5
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.dao.StudentFlowDao;
import net.zdsoft.basedata.dto.StudentFlowDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentFlow;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.file.ImportFileParamIn;
import net.zdsoft.basedata.file.ImportFileParamOut;
import net.zdsoft.basedata.file.ImportFileResult;
import net.zdsoft.basedata.file.XlsFileParser;
import net.zdsoft.basedata.file.XlsxFileParser;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.FamilyService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentFlowService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.WeiKeUtils;

/**
 * @description:
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-5上午10:45:11
 */
@Service("studentFlowService")
public class StudentFlowServiceImpl extends BaseServiceImpl<StudentFlow, String> implements StudentFlowService {
    @Autowired
    private StudentFlowDao studentFlowDao;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ImportService importService;

    @Override
    protected BaseJpaRepositoryDao<StudentFlow, String> getJpaDao() {
        return this.studentFlowDao;
    }

    @Override
    public StudentFlow save(Student student, StudentFlow studentFlow) {
        // 保存记录
        User operateUser = userService.findOne(studentFlow.getHandleUserId());
        studentFlow.setHandleUserName(operateUser != null ? operateUser.getRealName() : StringUtils.EMPTY);
        studentFlow.setId(UuidUtils.generateUuid());
        // 转入
        if (StudentFlow.STUDENT_FLOW_IN.equals(studentFlow.getFlowType())) {
            School sch = schoolService.findOne(studentFlow.getSchoolId());
            studentFlow.setSchoolName(sch != null ? sch.getSchoolName() : StringUtils.EMPTY);
            Clazz clazz = classService.findOne(studentFlow.getClassId());
            Grade grade = gradeService.findOne(clazz.getGradeId());

            studentFlow.setClassName(clazz != null ? (grade != null ? (grade.getGradeName() + clazz.getClassName())
                    : clazz.getClassName()) : StringUtils.EMPTY);
            // 修改学生信息
            student.setNowState("40");
            student.setClassId(studentFlow.getClassId());
            student.setSchoolId(studentFlow.getSchoolId());
            student.setModifyTime(studentFlow.getCreationTime());
            student.setIsLeaveSchool(Student.STUDENT_NORMAL);

            // 更新家长学校Id
            List<Family> families = familyService.findListByIn("studentId", new String[] { student.getId() });
            for (Family family : families) {
                family.setSchoolId(student.getSchoolId());
                family.setIsLeaveSchool(student.getIsLeaveSchool());
            }
            familyService.saveAllEntitys(families.toArray(new Family[0]));

        }
        // 调出修改
        else if (StudentFlow.STUDENT_FLOW_LEAVE.equals(studentFlow.getFlowType())) {
            student.setVerifyCode(RandomStringUtils.randomAlphabetic(6).toLowerCase());
            student.setIsLeaveSchool(Student.STUDENT_LEAVE);
            student.setNowState("99");
            studentFlow.setCreationTime(new Date());
            student.setModifyTime(studentFlow.getCreationTime());
            studentFlow.setPin(student.getVerifyCode());

        }

        studentService.saveAllEntitys(student);
        saveAllEntitys(studentFlow);

        List<Student> studentList = new ArrayList<Student>();
        studentList.add(student);
        List<StudentFlow> studentFlowList = new ArrayList<StudentFlow>();
        studentFlowList.add(studentFlow);
        pushWikeMessage(studentList, studentFlowList, studentFlow.getFlowType());
        return studentFlow;
    }

    @Override
    public List<StudentFlow> saveAllEntitys(StudentFlow... studentFlow) {
        return studentFlowDao.saveAll(checkSave(studentFlow));
    }

    @Override
    public List<StudentFlow> searchFlowHistory(final String[] studentIds, final Date startDate, final Date endDate,
            final String studentFlowType, final String unitId, Pagination page) {
        List<StudentFlow> studentFlows = Lists.newArrayList();
        // 组装查询条件
        Specification<StudentFlow> sp = new Specification<StudentFlow>() {
            @Override
            public Predicate toPredicate(Root<StudentFlow> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(root.get("studentId").as(String.class).in((Object[]) studentIds));
                if (StringUtils.isNotEmpty(unitId)) {
                    ps.add(builder.equal(root.get("schoolId"), unitId));
                }
                if (startDate != null) {
                    ps.add(builder.greaterThan(root.get("creationTime").as(Timestamp.class), startDate));
                }
                if (endDate != null) {
                    ps.add(builder.lessThan(root.get("creationTime").as(Timestamp.class), endDate));
                }
                ps.add(builder.equal(root.get("flowType").as(String.class), studentFlowType));
                criteria.where(ps.toArray(new Predicate[0]));
                criteria.orderBy(builder.desc(root.get("creationTime").as(String.class)));
                return criteria.getRestriction();
            }
        };

        if (page == null) {
            studentFlows = findAll(sp);
        }
        else {
            studentFlows = findAll(sp, page);
        }
        return studentFlows;
    }

    @Override
    public List<StudentFlow> searchFlows(String studentname, String identityCard, String pin, String flowType,
            Pagination page) {
        return studentFlowDao.searchFlows(studentname, identityCard, pin, flowType, page);
    }

    public void pushWikeMessage(List<Student> studentList, List<StudentFlow> studentFlowList, String type) {
        // 指定推送类型为空，则全部推送，否则根据指定类型推送
        if (StringUtils.isNotEmpty(getPushWeikeType()) && !type.equals(getPushWeikeType().trim())) {
            return;
        }

        if (CollectionUtils.isEmpty(studentFlowList) || CollectionUtils.isEmpty(studentList)) {
            return;
        }

        if (studentFlowList.size() != studentList.size()) {
            throw new RuntimeException("推送出错,学生记录数和转入调出记录数不对应");
        }
        Map<String, String> studentNameMap = EntityUtils.getMap(studentList, "id", "studentName");
        final List<String> studentIds = EntityUtils.getList(studentList, "id");
        Specification<Family> fs = new Specification<Family>() {
            @Override
            public Predicate toPredicate(Root<Family> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(root.get("studentId").as(String.class).in(studentIds.toArray()));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), new Integer(0)));
                cq.where(cb.and(ps.toArray(new Predicate[0])));
                return cq.getRestriction();
            }
        };
        List<Family> families = familyService.findAll(fs);
        List<String> familyList = EntityUtils.getList(families, "id");
        // Map<String,String> familyMap = EntityUtils.getMap(families, "id",
        // "family");
        Map<String, List<Family>> familyByStudentIdMap = new HashMap<String, List<Family>>();
        for (Family family : families) {
            List<Family> familys = familyByStudentIdMap.get(family.getStudentId());
            if (CollectionUtils.isEmpty(familys)) {
                familys = new ArrayList<Family>();
            }
            familys.add(family);
            familyByStudentIdMap.put(family.getStudentId(), familys);

        }
        // Map<String, Family> f
        // List<User> userList = userService.findByIdIn(familyIds.toArray(new
        // String[0]));
        final List<String> familyIds = familyList;
        if (familyIds.size() > 0) {
            Specification<User> us = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = new ArrayList<Predicate>();
                    ps.add(root.get("ownerId").as(String.class).in(familyIds.toArray()));
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), new Integer(0)));
                    cq.where(cb.and(ps.toArray(new Predicate[0])));
                    return cq.getRestriction();
                }
            };
            List<User> userList = userService.findUsersListByOwnerIds(familyIds.stream().toArray(String[]::new), false);
            Map<String, User> userMap = EntityUtils.getMap(userList, "ownerId", StringUtils.EMPTY);
            int i = 0;
            Map<String, String> param = new HashMap<String, String>();
            List<String> userIds = new ArrayList<String>();
            List<String> contents = new ArrayList<String>();
            for (StudentFlow studentFlow : studentFlowList) {
                if (i == 0) {
                    // param = new HashMap<String, String>();
                    userIds = new ArrayList<String>();
                    contents = new ArrayList<String>();
                }

                List<Family> families2 = familyByStudentIdMap.get(studentFlow.getStudentId());
                if (CollectionUtils.isEmpty(families2)) {
                    continue;
                }
                for (Family family : families2) {
                    User user = userMap.get(family.getId());
                    if (user == null) {
                        continue;
                    }
                    String studentName = studentNameMap.get(studentFlow.getStudentId());
                    if (StudentFlow.STUDENT_FLOW_IN.equals(studentFlow.getFlowType())) {
                        // 您的孩子 *** 转入
                        // **学校**班，转入原因：转学（系统读取转入原因），请您知悉。如有疑问，请联系新班主任。
                        contents.add("您的孩子 " + studentName + " 转入 " + studentFlow.getSchoolName()
                                + studentFlow.getClassName() + "，转入原因：" + studentFlow.getReason()
                                + "，请您知悉。如有疑问，请联系新班主任。");
                        userIds.add(user.getId());
                        i++;
                    }
                    else if (StudentFlow.STUDENT_FLOW_LEAVE.equals(studentFlow.getFlowType())) {
                        // 非毕业转出：“您的孩子 ***
                        // 已从****学校转出，转出原因：转学（系统读取转出原因），转出验证码：******，请您知悉并妥善保管验证码。转入学校将根据您提供的验证码对您孩子的信息进行转入操作。如有疑问，请联系原班主任。”
                        // 毕业转出：“您的孩子 ***
                        // 已从****学校自动转出，转出原因：毕业，转出验证码：******，请您知悉并妥善保管验证码。下学期开学，升学学校将会根据您提供的验证码对您孩子的信息进行转入操作，如有疑问，请联系原班主任。“
                        if (studentFlow.getReason().equals("毕业")) {
                            contents.add("您的孩子 " + studentName + " 已从" + studentFlow.getSchoolName() + "转出，转出原因："
                                    + studentFlow.getReason() + "，转出验证码：" + studentFlow.getPin()
                                    + "，请您知悉并妥善保管验证码。转入学校将根据您提供的验证码对您孩子的信息进行转入操作。如有疑问，请联系原班主任。");
                        }
                        else {
                            contents.add("您的孩子" + studentName + " 已从" + studentFlow.getSchoolName() + "自动转出，转出原因："
                                    + studentFlow.getReason() + "，转出验证码：" + studentFlow.getPin()
                                    + "，请您知悉并妥善保管验证码。下学期开学，升学学校将会根据您提供的验证码对您孩子的信息进行转入操作，如有疑问，请联系原班主任。");
                        }
                        userIds.add(user.getId());
                        i++;
                    }
                }
                if (i >= 500) {
                    WeiKeUtils.push(param, userIds.toArray(new String[0]), contents.toArray(new String[0]));
                    i = 0;
                }
                // WeiKeUtils.push(param, userIds.toArray(new String[0]),
                // contents.toArray(new String[0]));
            }
            WeiKeUtils.push(param, userIds.toArray(new String[0]), contents.toArray(new String[0]));
        }
    }

    @Override
    public List<StudentFlowDto> searchFlowLogs(final String studentname, Date startDate, Date endDate,
            String studentFlowType, Pagination page, final String unitId, final boolean isEdu) {
        List<StudentFlowDto> flowDtos = Lists.newArrayList();

        List<Student> students = studentService.findAll(new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
                List<Predicate> ps = Lists.newArrayList();
                if (StringUtils.isNotEmpty(studentname)) {
                    ps.add(builder.like(root.get("studentName").as(String.class), studentname + "%"));
                }
                if (!isEdu) {
                    ps.add(builder.equal(root.get("schoolId").as(String.class), unitId));
                }
                criteria.where(ps.toArray(new Predicate[0]));
                return criteria.getRestriction();
            }
        });
        Set<String> studentIds = Sets.newHashSet();
        if (CollectionUtils.isEmpty(students)) {
            return flowDtos;
        }
        for (Student student : students) {
            studentIds.add(student.getId());
        }
        Map<String, Student> studentMap = studentService.findMapByIdIn(studentIds.toArray(new String[0]));
        // Map<String,Student> studentMap =
        // studentService.getStudentMap(studentIds.toArray(new String[0]));
        List<StudentFlow> flows = searchFlowHistory(studentIds.toArray(new String[0]), startDate, endDate,
                studentFlowType, isEdu ? null : unitId, page);

        // 封装数据
        for (StudentFlow studentFlow : flows) {
            StudentFlowDto dto = new StudentFlowDto();
            dto.setStudentFlow(studentFlow);
            dto.setStudent(studentMap.get(studentFlow.getStudentId()));
            flowDtos.add(dto);
        }
        return CollectionUtils.isEmpty(flowDtos) ? new ArrayList<StudentFlowDto>() : flowDtos;
    }

    @Override
    public void batchSave(List<StudentFlow> studentFlows, List<Student> students, String type) {

        studentService.saveAllEntitys(students.toArray(new Student[0]));

        // 更新家长信息
        Map<String, String> stId2SchIdMap = EntityUtils.getMap(students, "id", "schoolId");
        List<Family> families = familyService.findListByIn("studentId", stId2SchIdMap.keySet().toArray(new String[0]));
        for (Family family : families) {
            family.setSchoolId(stId2SchIdMap.get(family.getStudentId()));
            if (StudentFlow.STUDENT_FLOW_IN.equals(type)) {
                family.setIsLeaveSchool(0);
            }
        }
        familyService.saveAllEntitys(families.toArray(new Family[0]));

        studentFlowDao.saveAll(studentFlows);
    }

    /**
     * 上传导入
     * 
     * @param uploadFile
     * @param userId
     * @param unitId
     * @param type
     * @throws IOException
     * @throws IllegalStateException
     */
    @Override
    public void saveImport(MultipartFile uploadFile, String userId, String unitId, String type)
            throws IllegalStateException, IOException {
        String newId = UuidUtils.generateUuid();

        String fileName = uploadFile.getOriginalFilename();
        // 获取文件存放路径
        String storePath = getFilePath(unitId);
        String newFileName = newId + "." + StorageFileUtils.getFileExtension(fileName);
        File path = new File(storePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        uploadFile.transferTo(new File(storePath.toString() + newFileName));
        ImportEntity importEntity = new ImportEntity();
        importEntity.setId(newId);
        importEntity.setCreationTime(new Date());
        importEntity.setModifyTime(new Date());
        importEntity.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
        importEntity.setFilePath(storePath);
        importEntity.setFileType(StorageFileUtils.getFileExtension(fileName));
        importEntity.setImportType(type);
        importEntity.setImportUserId(userId);
        importEntity.setUnitId(unitId);
        importEntity.setStatus(ImportEntity.IMPORT_STATUS_WAIT);
        importService.saveAllEntitys(importEntity);
    }

    /**
     * 导入
     * 
     * @param uploadFile
     * @param userId
     * @param unitId
     * @param type
     * @return
     * @throws IOException
     */
    @Override
    public void batchImport(ImportEntity importEntity, String flowType) throws IOException {

        boolean isEisu = false;
        Unit unit = unitService.findOne(importEntity.getUnitId());

        /************************ 基本参数初始化开始 ***********************/
        ImportFileParamOut paramOut = null;
        // 参数，用于存放顺序
        List<String> newParams = Lists.newArrayList();
        // 封装错误数据的List(导出结束时回调参数)
        ImportFileParamOut errorOut = new ImportFileParamOut();
        ImportFileParamOut successOut = new ImportFileParamOut();
        List<String[]> rowDatas = Lists.newArrayList();
        List<Integer> indexs = Lists.newArrayList();
        errorOut.setRowDatas(rowDatas);
        errorOut.setIndexs(indexs);

        // 正确的数据(导出结束时回调参数)
        List<String[]> success = Lists.newArrayList();

        // 初始化基本导入数据
        paramOut = initBaseDatas(importEntity, errorOut, newParams);
        if (StringUtils.isNotEmpty(paramOut.getErrorMsg())) {
            ImportFileResult.exportXLSFileResult(paramOut, importEntity, ImportFileResult.FILE_ERROR);
            importEntity.setStatus(ImportEntity.IMPORT_STATUS_ERROR);
            importEntity.setModifyTime(new Date());
            importService.saveAllEntitys(importEntity);
            errorOut.setErrorMsg("模板错误");
            return;
        }
        /************************ 基本参数初始化结束 ***********************/

        // 首次组装并校验验证码是否正确，剔除错误数据

        Map<StudentFlowDto, Integer> datas = fillData(paramOut, newParams, flowType, errorOut);

        // 组装数据
        if (CollectionUtils.isEmpty(datas.keySet())) {
            // 模版数据全部错误的数据组装
            if (CollectionUtils.isNotEmpty(errorOut.getRowDatas())) {
                ImportFileResult.exportXLSFileResult(errorOut, importEntity, ImportFileResult.FILE_INFO_ERROR);
            }
            importEntity.setStatus(ImportEntity.IMPORT_STATUS_SUCCESS);
            importEntity.setModifyTime(new Date());
            importService.saveAllEntitys(importEntity);
            return;
        }

        /************************** 初始化学校班级等信息开始 **********************/
        Map<String, School> schMap = Maps.newHashMap();
        Map<String, Map<String, Clazz>> clazzMap = Maps.newHashMap();
        Map<String, Grade> gradeMap = Maps.newHashMap();

        initSchoolAndClass(schMap, clazzMap, gradeMap, datas.keySet(), isEisu, unit, flowType);

        /************************** 初始化学校班级等信息结束 **********************/

        /*********************** 过滤学生不存在的或者验证码错误的 *********************/
        // 全部放在最后的过滤方法中过滤
        /*********************** 过滤学生不存在的或者验证码错误的 *********************/

        /************************ 最后数据过滤和封装 ************************/
        List<StudentFlow> finalDatas = Lists.newArrayList();
        List<Student> students = Lists.newArrayList();
        finalFiltered(finalDatas, students, success, datas, schMap, clazzMap, paramOut, newParams,
                importEntity.getImportUserId(), flowType, errorOut, unit);

        // 保存相关呢数据
        batchSave(finalDatas, students, importEntity.getImportType());
        if (CollectionUtils.isNotEmpty(errorOut.getRowDatas())) {
            ImportFileResult.exportXLSFileResult(errorOut, importEntity, ImportFileResult.FILE_INFO_ERROR);
        }
        // 组装成功数据
        if (StudentFlow.STUDENT_FLOW_LEAVE.equals(flowType)) {
            List<String> newFields = Lists.newArrayList(Arrays.asList(paramOut.getFields()));
            newFields.add("验证码");
            successOut.setFields(newFields.toArray(new String[0]));
        }
        else {
            successOut.setFields(paramOut.getFields());
        }
        successOut.setRowDatas(success);

        if (CollectionUtils.isNotEmpty(successOut.getRowDatas())) {
            ImportFileResult.exportXLSFileResult(successOut, importEntity, ImportFileResult.FILE_SUCCESS);
        }
        importEntity.setStatus(ImportEntity.IMPORT_STATUS_SUCCESS);
        importEntity.setModifyTime(new Date());
        importService.saveAllEntitys(importEntity);

        if (students.size() == finalDatas.size()) {
            pushWikeMessage(students, finalDatas, flowType);
        }
    }

    private void finalFiltered(List<StudentFlow> finalDatas, List<Student> students, List<String[]> success,
            Map<StudentFlowDto, Integer> datas, Map<String, School> schMap, Map<String, Map<String, Clazz>> clazzMap,
            ImportFileParamOut paramOut, List<String> newParams, String userId, String type,
            ImportFileParamOut errorOut, Unit unit) {
        boolean isSchool = unit.getUnitClass() == Unit.UNIT_CLASS_SCHOOL;
        String handleUserName = userService.findOne(userId).getRealName();
        Semester semester = semesterService.getCurrentSemester(1);
        List<Student> sts = Lists.newArrayList();
        List<String> studentNames = EntityUtils.getList(Lists.newArrayList(datas.keySet()), "studentName");
        List<String> identityCards = EntityUtils.getList(Lists.newArrayList(datas.keySet()), "identityCard");
        sts = getStudentsByNameAndIdCard(studentNames, identityCards);

        List<StudentFlowDto> dtoss = Lists.newArrayList();

        dtoss = Lists.newArrayList(datas.keySet());
        Map<String, Student> stMap = new HashMap<String, Student>();
        for (Student stu : sts) {
            stMap.put(stu.getStudentName() + "_" + stu.getIdentityCard(), stu);
        }
        for (StudentFlowDto dto : dtoss) {
            StudentFlow studentFlow = dto.getStudentFlow();
            studentFlow.setAcadyear(semester.getAcadyear());
            studentFlow.setSemester(semester.getSemester());
            studentFlow.setFlowType(type);
            studentFlow.setHandleUserId(userId);
            studentFlow.setHandleUserName(handleUserName);

            Student student = null;
            student = stMap.get(dto.getStudentName() + "_" + dto.getIdentityCard());
            // 判断学生是否存在以及是姓名或者身份证件号错误
            if (student == null) {
                boolean isName = EntityUtils.isContainValue(sts, "studentName", dto.getStudentName());
                String me = "该学生:" + dto.getStudentName() + "不存在或者身份证件号错误";
                if (!isName) {
                    me = "该学生:" + dto.getStudentName() + "不存在";
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                }
                else {
                    me = "该学生:" + dto.getStudentName() + "身份证号有误";
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("identityCard")));
                }
                List<String> newRowData = Lists.newArrayList(me);
                newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                continue;
            }

            // 判断学校名和班级名是否正确
            School sch = schMap.get(dto.getStudentFlow().getSchoolName());
            if (sch == null) {
                String me = "该学校不存在";
                List<String> newRowData = Lists.newArrayList(me);
                newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                errorOut.getIndexs().add(new Integer(newParams.indexOf("schoolName")));
                continue;
            }
            String schoolId = sch.getId();

            Clazz clazz = clazzMap.get(sch.getSchoolName()).get(studentFlow.getClassName());
            Lists.newArrayList(clazzMap.values());
            if (clazzMap.get(dto.getStudentFlow().getSchoolName()) == null || clazz == null) {
                List<String> newRowData = Lists.newArrayList("该班级不存在");
                newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                errorOut.getIndexs().add(new Integer(newParams.indexOf("className")));
                continue;
            }
            String classId = clazz.getId();
            // 调离时判断权限，状态
            if (StudentFlow.STUDENT_FLOW_LEAVE.equals(type)) {
                if (isSchool && !student.getSchoolId().equals(unit.getId())) {
                    List<String> newRowData = Lists.newArrayList("只能调离本校的学生");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(newParams.indexOf("schoolName"));
                    continue;
                }

                if (!student.getClassId().equals(classId)) {
                    List<String> newRowData = Lists.newArrayList("该班级下面没有该学生");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(newParams.indexOf("className"));
                    continue;
                }
                if (student.getIsLeaveSchool() == Student.STUDENT_LEAVE) {
                    if (StringUtils.isNotEmpty(student.getVerifyCode())) {
                        String me = "该学生:" + dto.getStudentName() + "已转出";
                        List<String> newRowData = Lists.newArrayList(me);
                        newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                        errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                        errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                        continue;
                    }
                }
                // 未走完流程不能轉出
                if (new Integer(-1).equals(student.getIsFreshman())) {
                    List<String> newRowData = Lists.newArrayList("该学生未走完流程，不能转出");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                    continue;
                }

                student.setModifyTime(new Date());
                student.setNowState("99");
                student.setVerifyCode(RandomStringUtils.randomAlphabetic(6).toLowerCase());
                student.setIsLeaveSchool(Student.STUDENT_LEAVE);

                students.add(student);
            }
            // 转入学生状态和验证码判断
            else if (StudentFlow.STUDENT_FLOW_IN.equals(type)) {
                if (isSchool && !schoolId.equals(unit.getId())) {
                    List<String> newRowData = Lists.newArrayList("您只能调将学生转入本校");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("schoolName")));
                    continue;
                }
                if (student.getIsLeaveSchool() == Student.STUDENT_NORMAL) {
                    List<String> newRowData = Lists.newArrayList("该学生:" + dto.getStudentName() + "不处于转出状态，无法转入");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                    continue;
                }
                if (isUseVerifyCode()) {
                    if (!student.getVerifyCode().equals(studentFlow.getPin())) {
                        List<String> newRowData = Lists.newArrayList("该学生:" + dto.getStudentName() + "验证码错误，无法转入");
                        newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
                        errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                        errorOut.getIndexs().add(new Integer(newParams.indexOf("pin")));
                        continue;
                    }
                }
                student.setClassId(classId);
                student.setSchoolId(schoolId);
                student.setModifyTime(new Date());
                student.setNowState("40");
                // student.setVerifyCode(RandomStringUtils.randomAlphabetic(6));
                student.setIsLeaveSchool(Student.STUDENT_NORMAL);
                students.add(student);
            }
            studentFlow.setCreationTime(new Date());
            studentFlow.setClassId(classId);
            studentFlow.setClassName(studentFlow.getClassName());
            studentFlow.setSchoolId(schoolId);
            studentFlow.setSchoolName(studentFlow.getSchoolName());
            studentFlow.setStudentId(student.getId());
            if (isUseVerifyCode()) {
                studentFlow.setPin(StudentFlow.STUDENT_FLOW_LEAVE.equals(type) ? student.getVerifyCode() : studentFlow
                        .getPin());
            }
            else {
                studentFlow.setPin(StudentFlow.STUDENT_FLOW_LEAVE.equals(type) ? student.getVerifyCode() : student
                        .getVerifyCode());
            }
            studentFlow.setId(UuidUtils.generateUuid());
            List<String> newRowData = Lists.newArrayList(Arrays.asList(paramOut.getRowDatas().get(datas.get(dto))));
            if (StudentFlow.STUDENT_FLOW_LEAVE.equals(type)) {
                newRowData.add(student.getVerifyCode());
            }
            success.add(newRowData.toArray(new String[0]));
            finalDatas.add(studentFlow);
        }

    }

    // 初始化基本数据
    private ImportFileParamOut initBaseDatas(ImportEntity importEntity, ImportFileParamOut errorOut,
            List<String> newParams) {
        ImportFileParamOut paramOut = null;
        try {
            ImportFileParamIn paramIn = new ImportFileParamIn();
            paramIn.setBeginRow(0);
            paramIn.setImportFile(importEntity.getFilePath() + importEntity.getId() + "." + importEntity.getFileType());
            paramIn.setXlsSheetName("");
            paramIn.setXmlObjecDefine("");

            if ("xls".equals(importEntity.getFileType())) {
                paramOut = XlsFileParser.parseFile(paramIn);
            }
            else if ("xlsx".equals(importEntity.getFileType())) {
                paramOut = XlsxFileParser.parseFile(paramIn);
            }

            int titleLength = 0;
            Map<String, String> paramMaps = Maps.newHashMap();
            paramMaps.put("学生姓名", "studentName");
            paramMaps.put("身份证件号", "identityCard");

            if (StudentFlow.STUDENT_FLOW_LEAVE.equals(importEntity.getImportType())) {
                titleLength = 5;
                paramMaps.put("原学校", "schoolName");
                paramMaps.put("原班级", "className");
                paramMaps.put("转出原因", "reason");
            }
            else if (StudentFlow.STUDENT_FLOW_IN.equals(importEntity.getImportType())) {
                titleLength = 5;
                paramMaps.put("转入学校", "schoolName");
                paramMaps.put("转入班级", "className");
                if (isUseVerifyCode()) {
                    paramMaps.put("验证码", "pin");
                    titleLength = titleLength + 1;
                }
                paramMaps.put("转入原因", "reason");
            }

            // 校验模板
            String[] titles = paramOut.getFields();
            if (titles == null || titles.length < titleLength || titles.length > titleLength) {
                newParams = null;
                paramOut.setErrorMsg("模板错误，模板列不匹配");
                return paramOut;
            }
            List<String> errorFields = Lists.newArrayList("错误信息");
            errorFields.addAll(Arrays.asList(titles));

            errorOut.setFields(errorFields.toArray(new String[0]));

            for (String title : titles) {
                if (!paramMaps.containsKey(title)) {

                    paramOut.setErrorMsg("模板错误，" + title + "是无效列");
                    newParams = null;
                    return paramOut;
                }
                newParams.add(paramMaps.get(title));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return paramOut;
    }

    private void initSchoolAndClass(Map<String, School> schMap, Map<String, Map<String, Clazz>> clazzMap,
            Map<String, Grade> gradeMap, Collection<StudentFlowDto> datas, boolean isEisu, Unit unit, String flowType) {
        List<School> schs = null;
        final List<String> schNames = EntityUtils.getList(Lists.newArrayList(datas), "studentFlow.schoolName");
        schs = schoolService.findAll(new Specification<School>() {
            @Override
            public Predicate toPredicate(Root<School> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(root.get("schoolName").as(String.class).in(schNames.toArray()));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                return cq.where(ps.toArray(new Predicate[0])).getRestriction();
            }
        });
        if (CollectionUtils.isNotEmpty(schs)) {
            Map<String, School> sm = EntityUtils.getMap(schs, "schoolName", StringUtils.EMPTY);
            schMap.putAll(sm);
        }

        List<String> schIds = EntityUtils.getList(schs, "id");
        final Set<String> allSchIds = Sets.newHashSet(schIds);

        List<Clazz> allClass = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(allSchIds)) {

            allClass = classService.findAll(new Specification<Clazz>() {

                @Override
                public Predicate toPredicate(Root<Clazz> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = Lists.newArrayList();
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    ps.add(cb.equal(root.get("isGraduate").as(Integer.class), Clazz.NOT_GRADUATED));
                    ps.add(root.get("schoolId").as(String.class).in(allSchIds));
                    cq.where(ps.toArray(new Predicate[0]));
                    return cq.getRestriction();
                }
            });
        }

        List<String> classIds = EntityUtils.getList(allClass, "gradeId");
        Set<String> clazzIds = Sets.newHashSet(classIds);
        List<Grade> grades = gradeService.findListByIdIn(clazzIds.toArray(new String[0]));
        Map<String, Grade> gMap = EntityUtils.getMap(grades, "id", StringUtils.EMPTY);
        gradeMap.putAll(gMap);

        for (String n : schMap.keySet()) {
            List<Clazz> cls = Lists.newArrayList();
            for (Clazz clazz : Sets.newHashSet(allClass)) {
                if (clazz.getSchoolId().equals(schMap.get(n).getId())) {
                    Grade grade = gMap.get(clazz.getGradeId());
                    if (!isEisu) {
                        clazz.setClassName(grade != null ? (grade.getGradeName() + clazz.getClassName()) : clazz
                                .getClassName());
                    }
                    cls.add(clazz);
                }
            }
            Map<String, Clazz> clazzs = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(cls)) {
                clazzs = EntityUtils.getMap(cls, "className", StringUtils.EMPTY);
            }
            clazzMap.put(n, clazzs);
        }
    }

    /**
     * 获取存放上传文件的路径
     * 
     * @param unitId
     * @return
     */
    @SuppressWarnings("deprecation")
    private String getFilePath(String unitId) {
        return new StringBuilder().append(Evn.getString(Constant.STORE_PATH)).append(File.separator)
                .append(StudentFlow.STORE_PATH_CHILD).append(File.separator).append(unitId).append(File.separator)
                .append(new Date().getYear() + 1900).append(File.separator).toString();
    }

    private Map<StudentFlowDto, Integer> fillData(ImportFileParamOut out, List<String> params, String type,
            ImportFileParamOut errorOut) {
        Map<StudentFlowDto, Integer> dtos = Maps.newHashMap();

        List<String[]> originDatas = out.getRowDatas();

        for (String[] rowData : originDatas) {
            StudentFlowDto dto = new StudentFlowDto();
            for (int i = 0; i < rowData.length; i++) {
                // 组装数据
                String param = params.get(i);
                dto = setDatas(dto, param, rowData[i], type);
                if (StudentFlow.STUDENT_FLOW_LEAVE.equals(type)) {
                    if (param.equals("reason")) {
                        if (StringUtils.isEmpty(dto.getStudentFlow().getReason())) {
                            List<String> newRowData = Lists.newArrayList("原因不能为空");
                            newRowData.addAll(Arrays.asList(rowData));
                            errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                            errorOut.getIndexs().add(new Integer(params.indexOf("reason")));
                            dto = null;
                            break;
                        }
                        if (StringUtils.length(dto.getStudentFlow().getReason()) > 20) {
                            List<String> newRowData = Lists.newArrayList("原因长度过长(请输入20个字符以内)");
                            newRowData.addAll(Arrays.asList(rowData));
                            errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                            errorOut.getIndexs().add(new Integer(params.indexOf("reason")));
                            dto = null;
                            break;
                        }
                    }
                    continue;
                }
                if (dto == null) {
                    // 处理错误数据
                    List<String> newRowData = Lists.newArrayList("验证码格式错误");
                    newRowData.addAll(Arrays.asList(rowData));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(params.indexOf("pin")));
                    break;
                }
                else {
                    if (param.equals("reason")) {
                        if (StringUtils.isEmpty(dto.getStudentFlow().getReason())) {
                            List<String> newRowData = Lists.newArrayList("原因不能为空");
                            newRowData.addAll(Arrays.asList(rowData));
                            errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                            errorOut.getIndexs().add(new Integer(params.indexOf("reason")));
                            dto = null;
                            break;
                        }
                        if (StringUtils.length(dto.getStudentFlow().getReason()) > 20) {
                            List<String> newRowData = Lists.newArrayList("原因长度过长(请输入20个字符以内)");
                            newRowData.addAll(Arrays.asList(rowData));
                            errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                            errorOut.getIndexs().add(new Integer(params.indexOf("reason")));
                            dto = null;
                            break;
                        }
                    }
                }

            }
            if (dto == null) {
                continue;
            }
            dtos.put(dto, originDatas.indexOf(rowData));
        }
        return dtos;
    }

    private StudentFlowDto setDatas(StudentFlowDto dto, String param, String value, String type) {
        if (param.equals("studentName")) {
            dto.setStudentName(value);
            return dto;
        }
        if (param.equals("identityCard")) {
            dto.setIdentityCard(value);
            return dto;
        }
        if (param.equals("schoolName")) {
            dto.getStudentFlow().setSchoolName(value);
            return dto;
        }
        if (param.equals("className")) {
            dto.getStudentFlow().setClassName(value);
            return dto;
        }
        if (param.equals("reason")) {
            dto.getStudentFlow().setReason(value);
            return dto;
        }
        if (StudentFlow.STUDENT_FLOW_LEAVE.equals(getPushWeikeType())) {
            return dto;
        }
        if (isUseVerifyCode()) {
            if (param.equals("pin")) {
                if (StringUtils.length(value) != 6) {
                    return null;
                }
                dto.getStudentFlow().setPin(value);
                return dto;
            }
        }
        return null;
    }

    private List<Student> getStudentsByNameAndIdCard(final List<String> studentNames, final List<String> identityCards) {
        return studentService.findAll(new Specification<Student>() {

            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
                Predicate p = root.get("studentName").as(String.class).in(studentNames.toArray());
                criteria.where(p);
                return criteria.getRestriction();
            }
        });
    }

    @Override
    public void importStudentFlow(ImportEntity importEntity, String flowType) throws Exception {
        batchImport(importEntity, flowType);
    }

    @Override
    public ImportEntity checkStudentFlow(final String importType) {
        /**
         * 处理之前可能因为特殊原因阻塞的导入任务,将其改为未导入状态
         */
        ImportEntity improting = importService.findOne(new Specification<ImportEntity>() {
            @Override
            public Predicate toPredicate(Root<ImportEntity> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {

                List<Predicate> ps = Lists.newArrayList();
                ps.add(builder.equal(root.get("importType"), importType));
                ps.add(builder.equal(root.get("status"), ImportEntity.IMPORT_STATUS_START));
                criteria.where(ps.toArray(new Predicate[0]));
                return criteria.getRestriction();
            }
        });
        /**
         * 新的等待导入的任务
         */
        ImportEntity newImproting = importService.findOne(new Specification<ImportEntity>() {
            @Override
            public Predicate toPredicate(Root<ImportEntity> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {

                List<Predicate> ps = Lists.newArrayList();
                ps.add(builder.equal(root.get("importType"), importType));
                ps.add(builder.equal(root.get("status"), ImportEntity.IMPORT_STATUS_WAIT));
                criteria.where(ps.toArray(new Predicate[0])).orderBy(
                        builder.asc(root.get("creationTime").as(Date.class)));
                return criteria.getRestriction();
            }
        });
        if (improting != null) {
            improting.setStatus(ImportEntity.IMPORT_STATUS_WAIT);
            importService.saveAllEntitys(improting);
        }
        if (newImproting != null) {
            newImproting.setStatus(ImportEntity.IMPORT_STATUS_START);
            importService.saveAllEntitys(newImproting);
        }
        return newImproting;
    }

    @Override
    public void remoteOutSchool(String unitId, String[] studentIds) {
        List<Student> students = new ArrayList<Student>();
        List<Unit> unitList = new ArrayList<Unit>();

        if (studentIds != null && studentIds.length > 0) {
            students = studentService.findListByIdIn(studentIds);
        }
        else {
            Unit unit = unitService.findOne(unitId);
            if (unit.getUnitClass().equals(Unit.UNIT_CLASS_EDU)) {
                if (unit.getParentId().equals(Unit.TOP_UNIT_GUID)) {
                    unitList = unitService.findDirectUnitsByParentId(null, Unit.UNIT_CLASS_SCHOOL);
                }
                else {
                    unitList = unitService.findDirectUnitsByParentId(unitId, Unit.UNIT_CLASS_SCHOOL);
                }
            }
            else {
                unitList.add(unit);
            }
            final List<String> unitIdList = EntityUtils.getList(unitList, "id");
            Map<String, Unit> unitMap = EntityUtils.getMap(unitList, "id", "");
            for (final String id : unitIdList) {
                students = studentService.findAll(new Specification<Student>() {
                    @Override
                    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
                        List<Predicate> ps = Lists.newArrayList();
                        ps.add(builder.equal(root.get("nowState").as(String.class), "41"));
                        ps.add(builder.equal(root.get("isLeaveSchool").as(String.class), new Integer(1)));
                        // ps.add(root.get("schoolId").as(String.class).in(newUnitIdList.toArray(new String[0])));
                        ps.add(builder.equal(root.get("schoolId").as(String.class), id));
                        criteria.where(ps.toArray(new Predicate[0]));
                        return criteria.getRestriction();
                    }
                });
                saveStudents(students, unitMap.get(id));
            }

        }

    }

    private void saveStudents(List<Student> students, Unit unit) {
        Map<String, Clazz> classMap = new HashMap<String, Clazz>();
        if (students.size() > 0) {

            classMap = classService.findMapByIdIn(EntityUtils.getList(students, "classId").toArray(new String[0]));
            // Map<String,String> unitMap = EntityUtils.getMap(unitList, "id", "unitName");
            List<StudentFlow> studentFlows = new ArrayList<StudentFlow>();
            for (Student student : students) {
                StudentFlow flow = new StudentFlow();
                student.setVerifyCode(RandomStringUtils.randomAlphabetic(6).toLowerCase());
                student.setIsLeaveSchool(Student.STUDENT_LEAVE);
                student.setNowState("99");
                student.setModifyTime(new Date());
                flow.setId(UuidUtils.generateUuid());
                flow.setStudentId(student.getId());
                flow.setSchoolId(student.getSchoolId());
                // Unit school = unitMap.get(student.getSchoolId());
                // if(StringUtils.isNotEmpty(unitMap.get(student.getSchoolId()))){
                // flow.setSchoolName(unitMap.get(student.getSchoolId()));
                // }else{
                // flow.setSchoolName(StringUtils.EMPTY);
                // }
                flow.setSchoolName(unit != null ? unit.getUnitName() : "");
                flow.setClassId(student.getClassId());
                Clazz clazz = classMap.get(student.getClassId());
                flow.setClassName(clazz != null ? clazz.getClassName() : StringUtils.EMPTY);
                flow.setReason("毕业");
                flow.setHandleUserId(User.ADMIN_USER_ID);
                flow.setHandleUserName(User.ADMIN_USER_NAME);
                flow.setFlowType(StudentFlow.STUDENT_FLOW_LEAVE);
                flow.setCreationTime(new Date());
                flow.setPin(student.getVerifyCode());
                Semester semester = semesterService.getCurrentSemester(1);
                if (semester != null) {
                    flow.setSemester(semester.getSemester());
                    flow.setAcadyear(semester.getAcadyear());
                }
                studentFlows.add(flow);
            }
            studentService.saveAllEntitys(students.toArray(new Student[0]));
            this.saveAllEntitys(studentFlows.toArray(new StudentFlow[0]));
            pushWikeMessage(students, studentFlows, StudentFlow.STUDENT_FLOW_LEAVE);
        }
    }

    /**
     * 获取推送微课类型
     * 
     * @return
     */
    private String getPushWeikeType() {
        // return RedisUtils.get("student.flow.push.weike.type", new RedisInterface<String>() {
        // @Override
        // public String queryData() {
        // SysOption option = sysOptionService.getByOptionCode("PUSH.WEIKE.TYPE");
        // return option!=null?option.getNowValue():null;
        // }
        // });
        return Evn.getString("push_weike_type");
    }

    /**
     * 是否启用验证码
     * 
     * @return
     */
    private boolean isUseVerifyCode() {
        // String isUse = RedisUtils.get("student.flow.is.use.verifycode",new RedisInterface<String>() {
        // @Override
        // public String queryData() {
        // SysOption option = sysOptionService.getByOptionCode("IS.USE.VERIFY.CODE");
        // return option!=null?option.getNowValue():"1";
        // }
        // });
        return Evn.getBoolean("is_use_verify_code");
        // return BooleanUtils.toBoolean(NumberUtils.toInt(isUse));
    }

    @Override
    protected Class<StudentFlow> getEntityClass() {
        return StudentFlow.class;
    }
}
