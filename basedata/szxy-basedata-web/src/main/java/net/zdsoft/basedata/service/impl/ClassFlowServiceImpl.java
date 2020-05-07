/*
 * Project: v7
 * Author : shenke
 * @(#) ClassFlowServiceImpl.java Created on 2016-9-27
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.dao.ClassFlowDao;
import net.zdsoft.basedata.dto.ClassFlowDto;
import net.zdsoft.basedata.entity.ClassFlow;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.ImportEntity;
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
import net.zdsoft.basedata.service.ClassFlowService;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.basedata.service.ImportService;
import net.zdsoft.basedata.service.SemesterService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-27下午4:27:45
 */
@Service("classFlowService")
public class ClassFlowServiceImpl extends BaseServiceImpl<ClassFlow, String> implements ClassFlowService {

    @Autowired
    private ClassFlowDao classFlowDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ImportService importService;
    @Autowired
    private ClassService classService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UnitService unitService;

    @Override
    protected BaseJpaRepositoryDao<ClassFlow, String> getJpaDao() {
        return classFlowDao;
    }

    @Override
    protected Class<ClassFlow> getEntityClass() {
        return ClassFlow.class;
    }

    @Override
    public void flowClass(Student student, String newClassId, String operateUserId) {

        String oldClassId = student.getClassId();
        ClassFlow classFlow = new ClassFlow();
        classFlow.setId(UuidUtils.generateUuid());
        classFlow.setNewClassId(newClassId);
        classFlow.setOldClassId(oldClassId);
        classFlow.setStudentId(student.getId());
        classFlow.setOperateUserId(operateUserId);
        User user = userService.findOne(operateUserId);
        classFlow.setOperateUserName(user != null ? user.getRealName() : StringUtils.EMPTY);
        classFlow.setCreationTime(new Date());

        Semester semester = semesterService.getCurrentSemester(1);
        classFlow.setSemester(semester.getSemester());
        classFlow.setAcadyear(semester.getAcadyear());
        classFlow.setSchoolId(student.getSchoolId());

        student.setClassId(newClassId);

        studentService.saveAllEntitys(student);
        classFlowDao.save(classFlow);

    }

    @Override
    public void saveImport(MultipartFile uploadFile, String unitId, String userId) throws Exception {
        String id = UuidUtils.generateUuid();
        String fileName = uploadFile.getOriginalFilename();
        String newFileName = id + "." + StorageFileUtils.getFileExtension(fileName);
        String storagePath = getFilePath(unitId);
        File path = new File(storagePath);
        if (!path.exists()) {
            boolean dir = path.mkdirs();
            if (!dir) {
                throw new IllegalArgumentException("文件目录创建失败");
            }
        }
        uploadFile.transferTo(new File(storagePath.toString() + newFileName));

        ImportEntity importEntity = new ImportEntity();
        importEntity.setId(id);
        importEntity.setCreationTime(new Date());
        importEntity.setModifyTime(new Date());
        importEntity.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
        importEntity.setFilePath(storagePath);
        importEntity.setFileType(StorageFileUtils.getFileExtension(fileName));
        importEntity.setImportType(ImportEntity.IMPORT_TYPE_CLASSFLOW);
        importEntity.setImportUserId(userId);
        importEntity.setUnitId(unitId);
        importEntity.setStatus(ImportEntity.IMPORT_STATUS_WAIT);
        importService.saveAllEntitys(importEntity);
    }

    private String getFilePath(String unitId) {
        return new StringBuilder().append(Evn.getString(Constant.STORE_PATH)).append(File.separator)
                .append(StudentFlow.STORE_PATH_CHILD).append(File.separator).append(unitId).append(File.separator)
                .append(new Date().getYear() + 1900).append(File.separator).toString();
    }

    @Override
    public void batchImport(ImportEntity importEntity) {
        ImportFileParamOut paramOut = null;
        try {
            Unit unit = unitService.findOne(importEntity.getUnitId());
            User loginUser = userService.findOne(importEntity.getImportUserId());

            ImportFileParamOut errorOut = new ImportFileParamOut();
            ImportFileParamOut successOut = new ImportFileParamOut();

            List<String[]> rowDatas = Lists.newArrayList();
            List<Integer> indexs = Lists.newArrayList();
            errorOut.setRowDatas(rowDatas);
            errorOut.setIndexs(indexs);

            List<String> newParams = Lists.newArrayList();

            List<String[]> success = Lists.newArrayList();

            // 检查模板正确性

            ImportFileParamIn paramIn = new ImportFileParamIn();
            paramIn.setImportFile(importEntity.getFilePath() + importEntity.getId() + "." + importEntity.getFileType());
            paramIn.setXlsSheetName(StringUtils.EMPTY);
            paramIn.setXmlObjecDefine(StringUtils.EMPTY);
            if ("xls".equals(importEntity.getFileType())) {
                paramOut = XlsFileParser.parseFile(paramIn);
            }
            else if ("xlsx".equals(importEntity.getFileType())) {
                paramOut = XlsxFileParser.parseFile(paramIn);
            }
            else {
                throw new RuntimeException("文件格式错误！");
            }

            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("学生姓名", "studentName");
            paramMap.put("身份证件号", "identityCard");
            paramMap.put("原班级", "originClassName");
            paramMap.put("转入班级", "newClassName");
            String[] titles = paramOut.getFields();
            if (ArrayUtils.isEmpty(titles) || titles.length != 4) {
                paramOut.setErrorMsg("模板错误，列不匹配");
                ImportFileResult.exportXLSFileResult(paramOut, importEntity, ImportFileResult.FILE_ERROR);
                importEntity.setStatus(ImportEntity.IMPORT_STATUS_ERROR);
                importEntity.setModifyTime(new Date());
                importService.saveAllEntitys(importEntity);
                return;
            }
            List<String> errorFields = Lists.newArrayList("错误信息");
            errorFields.addAll(Arrays.asList(titles));
            errorOut.setFields(errorFields.toArray(new String[0]));

            for (String title : titles) {
                if (!paramMap.containsKey(title)) {
                    paramOut.setErrorMsg("模板错误，" + title + "是无效列");
                    ImportFileResult.exportXLSFileResult(paramOut, importEntity, ImportFileResult.FILE_ERROR);
                    importEntity.setModifyTime(new Date());
                    importEntity.setStatus(ImportEntity.IMPORT_STATUS_ERROR);
                    importService.saveAllEntitys(importEntity);
                    return;
                }
                newParams.add(paramMap.get(title));
            }

            // 组装数据并检查
            Map<ClassFlowDto, Integer> dataMap = Maps.newHashMap();
            List<String[]> originDatas = paramOut.getRowDatas();
            if (CollectionUtils.isEmpty(originDatas)) {
                paramOut.setErrorMsg("数据为空");
                ImportFileResult.exportXLSFileResult(paramOut, importEntity, ImportFileResult.FILE_ERROR);
                importEntity.setModifyTime(new Date());
                importEntity.setStatus(ImportEntity.IMPORT_STATUS_ERROR);
                importService.saveAllEntitys(importEntity);
                return;
            }
            for (String[] rowData : originDatas) {
                ClassFlowDto classFlowDto = new ClassFlowDto();
                for (int i = 0; i < rowData.length; i++) {
                    String param = newParams.get(i);
                    classFlowDto = packageRowData(classFlowDto, param, rowData[i]);
                }
                if (classFlowDto != null) {
                    dataMap.put(classFlowDto, originDatas.indexOf(rowData));
                }
            }

            List<Grade> gradeList = gradeService.findByUnitId(importEntity.getUnitId());
            List<String> gradeIds = EntityUtils.getList(gradeList, "id");
            Map<String, Grade> gradeMap = EntityUtils.getMap(gradeList, "id");
            List<Clazz> clazzList = classService.findByGradeIdIn(gradeIds.toArray(new String[0]));
            Map<String, Clazz> clazzMap = EntityUtils.getMap(clazzList, "id");
            Map<String, Clazz> clazzNameMap = Maps.newHashMap();

            for (Clazz clazz : clazzList) {
                String gradeId = gradeId = clazz.getGradeId();
                Grade grade = grade = gradeMap.get(gradeId);
                clazzNameMap.put(grade.getGradeName() + clazz.getClassName(), clazz);
            }

            List<ClassFlow> finalDatas = Lists.newArrayList();
            List<Student> studentList = Lists.newArrayList();
            Semester semester = semesterService.getCurrentSemester(1);
            for (ClassFlowDto classFlowDto : dataMap.keySet()) {
                if (StringUtils.isBlank(classFlowDto.getStudentName())) {
                    List<String> newRowData = Lists.newArrayList("学生姓名不能为空");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                    continue;
                }
                if (StringUtils.isBlank(classFlowDto.getOldClassName())) {
                    List<String> newRowData = Lists.newArrayList("原班级不能为空");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("originClassName")));
                    continue;
                }
                if (StringUtils.isBlank(classFlowDto.getNewClassName())) {
                    List<String> newRowData = Lists.newArrayList("转入班级不能为空");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("newClassName")));
                    continue;
                }
                Student student = null;
                List<Student> students = studentService.findListBy(
                        ArrayUtils.toArray("studentName", "schoolId", "isDeleted", "isLeaveSchool"),
                        ArrayUtils.toArray(classFlowDto.getStudentName(), importEntity.getUnitId(), "0", "0"));
                if (CollectionUtils.isEmpty(students)) {
                    List<String> newRowData = Lists.newArrayList("该学生不存在");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                    continue;
                }
                Clazz originClazz = clazzNameMap.get(classFlowDto.getOldClassName());
                if (originClazz == null) {
                    List<String> newRowData = Lists.newArrayList("原班级不存在");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("originClassName")));
                    continue;
                }

                if (StringUtils.isNotBlank(classFlowDto.getIdentityCard())) {

                    for (Student student1 : students) {
                        student = classFlowDto.getIdentityCard().equals(student1.getIdentityCard()) ? student1 : null;
                        if (student != null) {
                            break;
                        }
                    }
                    if (student == null) {
                        List<String> newRowData = Lists.newArrayList("身份证件号码错误");
                        newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                        errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                        errorOut.getIndexs().add(new Integer(newParams.indexOf("identityCard")));
                        continue;
                    }
                    else {
                        System.out.println("找到的学生classId：" + student.getClassId() + "===原班级Id：" + originClazz.getId());
                        if (!StringUtils.equals(originClazz.getId(), student.getClassId())) {
                            List<String> newRowData = Lists.newArrayList("该学生不在该班级下");
                            newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                            errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                            errorOut.getIndexs().add(new Integer(newParams.indexOf("originClassName")));
                            continue;
                        }
                    }
                }
                else {
                    int identityCardEmptyNum = 0;
                    List<Student> oneClassStudents = Lists.newArrayList();
                    for (Student student1 : students) {
                        if (student1.getClassId().equals(originClazz.getId())) {
                            oneClassStudents.add(student1);
                            identityCardEmptyNum = identityCardEmptyNum
                                    + (StringUtils.isBlank(student1.getIdentityCard()) ? 1 : 0);
                        }
                    }

                    if (oneClassStudents.size() > 1) {

                        List<String> newRowData = Lists.newArrayList("该班级下有多个姓名为【" + classFlowDto.getStudentName()
                                + "】的学生,填写身份证件号或者到页面上单独操作");
                        newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                        errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                        errorOut.getIndexs().add(new Integer(newParams.indexOf("studentName")));
                        continue;
                    }
                    else if (oneClassStudents.size() == 1) {
                        student = oneClassStudents.get(0);
                    }
                    else {
                        List<String> newRowData = Lists.newArrayList("原班级错误");
                        newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                        errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                        errorOut.getIndexs().add(new Integer(newParams.indexOf("originClassName")));
                        continue;
                    }
                }

                if (classFlowDto.getOldClassName().equals(classFlowDto.getNewClassName())) {
                    List<String> newRowData = Lists.newArrayList("转入班级和原班级相同");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("newClassName")));
                    continue;
                }

                String classId = student.getClassId();
                Clazz clazz = clazzMap.get(classId);
                String gradeId = clazz.getGradeId();
                Grade grade = gradeMap.get(gradeId);
                String splicingClazzName = (grade == null ? StringUtils.EMPTY : grade.getGradeName())
                        + (clazz == null ? StringUtils.EMPTY : clazz.getClassName());

                Clazz finalClazz = clazzNameMap.get(splicingClazzName);

                Clazz newClazz = clazzNameMap.get(classFlowDto.getNewClassName());

                if (newClazz == null) {
                    List<String> newRowData = Lists.newArrayList("转入班级错误");
                    newRowData.addAll(Arrays.asList(paramOut.getRowDatas().get(dataMap.get(classFlowDto))));
                    errorOut.getRowDatas().add(newRowData.toArray(new String[0]));
                    errorOut.getIndexs().add(new Integer(newParams.indexOf("newClassName")));
                    continue;
                }

                ClassFlow dto = new ClassFlow();

                // BeanUtils.copyProperties(dto,classFlowDto);

                dto.setOldClassId(originClazz.getId());
                dto.setNewClassId(newClazz.getId());
                dto.setOperateUserId(importEntity.getImportUserId());

                dto.setOperateUserName(loginUser != null ? loginUser.getRealName() : "");
                dto.setSchoolId(importEntity.getUnitId());
                dto.setId(UuidUtils.generateUuid());
                dto.setStudentId(student.getId());
                dto.setCreationTime(new Date());
                dto.setSemester(semester.getSemester());
                dto.setAcadyear(semester.getAcadyear());

                finalDatas.add(dto);
                student.setClassId(newClazz.getId());
                studentList.add(student);
            }
            if (CollectionUtils.isNotEmpty(errorOut.getRowDatas())) {
                ImportFileResult.exportXLSFileResult(errorOut, importEntity, ImportFileResult.FILE_ERROR);
            }

            if (CollectionUtils.isNotEmpty(finalDatas)) {
                saveAllEntitys(finalDatas.toArray(new ClassFlow[0]));
                studentService.saveAllEntitys(studentList.toArray(new Student[0]));
            }
            importEntity.setStatus(ImportEntity.IMPORT_STATUS_SUCCESS);
            importEntity.setModifyTime(new Date());
            importService.saveAllEntitys(importEntity);

            // 检查数据有效性
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                paramOut.setErrorMsg("导入出错" + e.getMessage());
                ImportFileResult.exportXLSFileResult(paramOut, importEntity, ImportFileResult.FILE_ERROR);
            }
            catch (IOException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e1);
            }
            System.out.println("批量出错");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveAllEntitys(ClassFlow... classFlow) {
        classFlowDao.saveAll(checkSave(classFlow));
    }

    private ClassFlowDto packageRowData(ClassFlowDto dto, String param, String data) {
        if (param.equals("studentName")) {
            dto.setStudentName(data);
            return dto;
        }
        if (param.equals("identityCard")) {
            dto.setIdentityCard(data);
            return dto;
        }
        if (param.equals("originClassName")) {
            dto.setOldClassName(data);
            return dto;
        }
        if (param.equals("newClassName")) {
            dto.setNewClassName(data);
            return dto;
        }
        return null;
    }
}
