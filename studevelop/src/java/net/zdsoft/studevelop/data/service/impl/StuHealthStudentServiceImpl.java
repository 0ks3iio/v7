package net.zdsoft.studevelop.data.service.impl;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.*;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.studevelop.data.dao.StuHealthStudentDao;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudent;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudentDetail;
import net.zdsoft.studevelop.data.service.StuHealthStudentDetailService;
import net.zdsoft.studevelop.data.service.StuHealthStudentService;
import net.zdsoft.studevelop.data.service.StuHealthyProjectService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by Administrator on 2018/4/18.
 */
@Service("StuHealthStudentService")
public class StuHealthStudentServiceImpl extends BaseServiceImpl<StudevelopHealthStudent,String> implements StuHealthStudentService{
    @Autowired
    private StuHealthStudentDao stuHealthStudentDao;
    @Autowired
    private StuHealthStudentDetailService stuHealthStudentDetailService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private StuHealthyProjectService stuHealthyProjectService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;

    @Override
    protected BaseJpaRepositoryDao<StudevelopHealthStudent, String> getJpaDao() {
        return stuHealthStudentDao;
    }

    @Override
    protected Class<StudevelopHealthStudent> getEntityClass() {
        return StudevelopHealthStudent.class;
    }

    @Override
    public void saveHealthStudent(StudevelopHealthStudent studevelopHealthStudent) {

        if(StringUtils.isEmpty(studevelopHealthStudent.getId())){
            studevelopHealthStudent.setId(UuidUtils.generateUuid());
            studevelopHealthStudent.setCreationTime(new Date());
        }else{
            studevelopHealthStudent.setModifyTime(new Date());
            List<StudevelopHealthStudentDetail> list = stuHealthStudentDetailService.findListBy("healthStudentId",studevelopHealthStudent.getId());
            stuHealthStudentDetailService.deleteAll(list.toArray(new StudevelopHealthStudentDetail[0]));
        }
        List<StudevelopHealthStudentDetail> detailList = studevelopHealthStudent.getHealthStudentDetail();
        if(CollectionUtils.isNotEmpty(detailList)){
            for(StudevelopHealthStudentDetail detail : detailList){
                detail.setHealthStudentId(studevelopHealthStudent.getId());
                detail.setId(UuidUtils.generateUuid());
                detail.setCreationTime(new Date());
            }
            stuHealthStudentDetailService.saveAll(detailList.toArray(new StudevelopHealthStudentDetail[0] ));
        }

        save(studevelopHealthStudent);



    }
    public void batchSave(List<StudevelopHealthStudent> studevelopHealthStudentList){
        List<String> healthStuIds =EntityUtils.getList(studevelopHealthStudentList,"id");
        List<StudevelopHealthStudentDetail> list = stuHealthStudentDetailService.findListByIn("healthStudentId",healthStuIds.toArray(new String[0]));
        if(CollectionUtils.isNotEmpty(list)){
            stuHealthStudentDetailService.deleteAll(list.toArray(new StudevelopHealthStudentDetail[0]));
        }
        saveAll(studevelopHealthStudentList.toArray(new StudevelopHealthStudent[0]));
        List<StudevelopHealthStudentDetail> detailList = new ArrayList<>();
        for(StudevelopHealthStudent healthStudent : studevelopHealthStudentList){
            detailList.addAll(healthStudent.getHealthStudentDetail());
        }
        stuHealthStudentDetailService.saveAll(detailList.toArray(new StudevelopHealthStudentDetail[0]));
    }
    @Override
    public String saveHealthStudentImport(String unitId, StudevelopHealthStudent studevelopHealthStudent, String  filePath) {

        //错误数据序列号
        int sequence = 0;
        List<String[]> errorDataList=new ArrayList<String[]>();
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(studevelopHealthStudent.getClassId()), new TypeReference<List<Student>>(){});

        Map<String,Student> studentMap = EntityUtils.getMap(studentList,"studentCode");

        List<StudevelopHealthStudent> healthStudents = findListBy(new String[]{"acadyear","semester","classId"},new String[]{studevelopHealthStudent.getAcadyear(),studevelopHealthStudent.getSemester(),studevelopHealthStudent.getClassId()});

        List<String> healthStuIds = EntityUtils.getList(healthStudents,"id");

        List<StudevelopHealthStudentDetail> studentDetailList = stuHealthStudentDetailService.findListByIn("healthStudentId",healthStuIds.toArray(new String[0]));

        Map<String ,  List<StudevelopHealthStudentDetail> > studentDetailMap = EntityUtils.getListMap(studentDetailList,"healthStudentId" ,null);
        for(StudevelopHealthStudent healthStudent : healthStudents){
            List<StudevelopHealthStudentDetail> detailList = studentDetailMap.get(healthStudent.getId());
            healthStudent.setHealthStudentDetail(detailList);
        }

        Map<String,StudevelopHealthStudent> healthStudentMap = EntityUtils.getMap(healthStudents,"studentId",null);

        StudevelopHealthProject project = new StudevelopHealthProject();
        project.setAcadyear(studevelopHealthStudent.getAcadyear());
        project.setSemester(studevelopHealthStudent.getSemester());
        Clazz cla = SUtils.dc(classRemoteService.findOneById(studevelopHealthStudent.getClassId()),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
        int section =grade.getSection();
        project.setSchSection(String.valueOf(section));
        project.setSchoolId(unitId);

        List<StudevelopHealthProject> projectList = stuHealthyProjectService.getProjectByAcadyearSemesterSection(project);

        Map<String,List<StudevelopHealthProject>> healthProjectMap =  EntityUtils.getListMap(projectList ,"projectType",null);
        List<McodeDetail> detailList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);
        List<String> typeTitle =EntityUtils.getList(detailList,"mcodeContent");


        List<String[]> rowDatas = ExcelUtils.readExcelByRow(filePath,0,projectList.size()+3);


        List<StudevelopHealthStudent> studevelopHealthStudentList = new ArrayList<>();
        int i=0;
        int totalSize = rowDatas.size()-2;
        int successCount=0;

//        for(int m=0;m<2;m++) {
//            String[] datas = rowDatas.get(m);
//
//        }
        for(int m=2;m<rowDatas.size();m++) {

            i++;
            int j = sequence + 1;


            try {
                String[] datas = rowDatas.get(m);
                String studentName = org.apache.commons.lang3.StringUtils.trimToEmpty(datas[0]);
                if (org.apache.commons.lang3.StringUtils.isEmpty(studentName)) {
                    addError(j + "", "第" + i + "行", "", "学生姓名不能为空", sequence, errorDataList);
                    continue;
                }
                String classInnerCode = org.apache.commons.lang3.StringUtils.trimToEmpty(datas[1]);
                String stuCode = org.apache.commons.lang3.StringUtils.trimToEmpty(datas[2]);
                if (org.apache.commons.lang3.StringUtils.isEmpty(stuCode)) {
                    addError(j + "", "第" + i + "行", "", "学生学号不能为空", sequence, errorDataList);
                    continue;
                }
                Student student = studentMap.get(stuCode);
                if (student == null) {
                    addError(j + "", "第" + i + "行", "", "学生学号在该班级下不存在", sequence, errorDataList);
                    continue;
                }
                if (!studentName.equals(student.getStudentName())) {
                    addError(j + "", "第" + i + "行", "", "该班级下学生学号对应的学生姓名与导入文件中对应姓名不匹配", sequence, errorDataList);
                    continue;
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(classInnerCode) && !classInnerCode.equals(student.getClassInnerCode())) {
                    addError(j + "", "第" + i + "行", "", "该班级下学生学号对应的学生编号与导入文件中对应编号不匹配", sequence, errorDataList);
                    continue;
                }

                StudevelopHealthStudent healthStudent = healthStudentMap.get(student.getId());
                if(healthStudent == null){
                    healthStudent = studevelopHealthStudent;
                    healthStudent = new StudevelopHealthStudent();
                    healthStudent.setAcadyear(studevelopHealthStudent.getAcadyear());
                    healthStudent.setSemester(studevelopHealthStudent.getSemester());
                    healthStudent.setClassId(studevelopHealthStudent.getClassId());
                    healthStudent.setSchoolId(unitId);
                    healthStudent.setId(UuidUtils.generateUuid());
                    healthStudent.setStudentId(student.getId());
                    healthStudent.setCreationTime(new Date());

                }
                int n=3;
                List<StudevelopHealthStudentDetail> studentDetails =  new ArrayList<>();

                for(McodeDetail detail:detailList){
                    List<StudevelopHealthProject> healthProjectList = healthProjectMap.get(detail.getThisId());
                    if(CollectionUtils.isNotEmpty(healthProjectList)){
                        for(StudevelopHealthProject healthProject : healthProjectList){
                            String val = datas[n];
                            verifyType(healthProject.getProjectName(),val,"String-30",false,null,"请输入正确的"+ healthProject.getProjectName()+"(不能超过30个字符)");
                            StudevelopHealthStudentDetail studentDetail = new StudevelopHealthStudentDetail();
                            studentDetail.setId(UuidUtils.generateUuid());
                            studentDetail.setProjectId(healthProject.getId());
                            studentDetail.setHealthStudentId(healthStudent.getId());
                            studentDetail.setProjectValue(val);
                            studentDetail.setCreationTime(new Date());
                            studentDetails.add(studentDetail);
                            n++;
                        }
                    }else{
                        throw new RuntimeException("请先维护身心项目在进行导入 !");
                    }


                }
                healthStudent.setHealthStudentDetail(studentDetails);
                studevelopHealthStudentList.add(healthStudent);
                successCount++;
            } catch (RuntimeException re) {
                re.printStackTrace();
                addError(j + "", "第" + i + "行", "", re.getMessage(), sequence, errorDataList);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                addError(j + "", "第" + i + "行", "", "数据整理出错。", sequence, errorDataList);
                continue;
            }

        }

        if(studevelopHealthStudentList.size() > 0){
            batchSave(studevelopHealthStudentList);
        }
        return result(totalSize, successCount, totalSize-successCount, errorDataList);
    }


    /**
     * 数据内容简单格式校验
     * @param fieldName 字段名称
     * @param value 内容
     * @param typeStr
     * @param require 是否必填
     * @param regex 正则
     * @param errorMsg 正则校验不通过时返回的提示信息
     * @return
     * @throws Exception
     */
    private String verifyType(String fieldName, String value, String typeStr, boolean require, String regex, String errorMsg) throws Exception{
        if(require && org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            throw new RuntimeException(fieldName + "不能为空。");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return value;
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(typeStr) && org.apache.commons.lang3.StringUtils.isEmpty(regex)) {
            return value;
        }
        String[] ts = typeStr.split("-");
        String type = ts[0];
        if (type.equalsIgnoreCase("String")
                || type.toLowerCase().indexOf("string") == 0) {
            int strLength = NumberUtils.toInt(ts[1]);
            if (strLength == 0)
                return value;
            if (Validators.isString(value, 0, strLength)
                    && net.zdsoft.framework.utils.StringUtils
                    .getRealLength(value) <= strLength)
                return value;
            else
                throw new RuntimeException(fieldName + "内容超出了最大长度("
                        + strLength + ")。");
        } else if (type.equalsIgnoreCase("Integer")
                || type.equalsIgnoreCase("Long")) {
            if (Validators.isNumber(value))
                return value;
            else
                return "";
//		} else if (type.equalsIgnoreCase("Datetime")) {
//			if (isDateTime(value))
//				return value;
//			else
//				throw new RuntimeException(fieldName + "不是有效的日期类型。");
//		}
            // 只有年和月的类型的
        } else if (type.equalsIgnoreCase("YearMonth")) {
            if (value.indexOf("-") > 0) {
                // 如果是2007-1这类的,改成2007-1-1
                if (value.indexOf("-", value.indexOf("-") + 1) < 0) {
                    value = value + "-1";
                }
                String[] s = value.split("-");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;
                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "-" + month + "-" + day;
                    } else {
                        value = "19" + year + "-" + month + "-" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "-" + month + "-" + day;
                }
            } else if (value.indexOf("/") > 0) {
                if (value.indexOf("/", value.indexOf("/") + 1) < 0) {
                    value = value + "/1";
                }
                String[] s = value.split("/");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;
                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "/" + month + "/" + day;
                    } else {
                        value = "19" + year + "/" + month + "/" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "/" + month + "/" + day;
                }
                value = value.replaceAll("/", "-");
            } else if (value.trim().length() == 6) {
                value = value + "01";
                if (Validators.isNumber(value)) {
                    value = value.substring(0, 4) + "-"
                            + value.substring(4, 6) + "-"
                            + value.substring(6);
                }
            } else if (value.trim().length() == 8) {
                if (Validators.isNumber(value)) {
                    value = value.substring(0, 4) + "-"
                            + value.substring(4, 6) + "-"
                            + value.substring(6);
                }
            }
            if (DateUtils.isDate(value)) {
                return value;
            } else {
                throw new RuntimeException(fieldName + "不是有效的日期类型。");
            }
        } else if (type.equalsIgnoreCase("Date")) {
            if (value.indexOf("-") > 0) {
                String[] s = value.split("-");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;
                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "-" + month + "-" + day;
                    } else {
                        value = "19" + year + "-" + month + "-" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "-" + month + "-" + day;
                }
            } else if (value.indexOf("/") > 0) {
                String[] s = value.split("/");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;

                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "/" + month + "/" + day;
                    } else {
                        value = "19" + year + "/" + month + "/" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "/" + month + "/" + day;
                }
                value = value.replaceAll("/", "-");
            } else if (value.trim().length() == 8) {
                if (Validators.isNumber(value)) {
                    value = value.substring(0, 4) + "-"
                            + value.substring(4, 6) + "-"
                            + value.substring(6);
                }
            }
            if (DateUtils.isDate(value))
                return value;
            else
                throw new RuntimeException(fieldName + "不是有效的日期类型。");
        }
//		else if (type.equalsIgnoreCase("Timestamp")) {
//			if (Validators.isTime(value))
//				return "";
//			else
//				throw new Exception(fieldName + "不是有效的日期类型。");
//		} else if (type.indexOf("Numeric") == 0) {
//			if ("N".equalsIgnoreCase(nonnegative)) {
//				if (!Validators.isNumeric(value, fraction)) {
//					throw new RuntimeException(fieldName + "不是有效的数字类型。");
//				}
//			} else {
//				if (!Validators.isNonNegativeNumeric(value, fraction)) {
//					throw new RuntimeException(fieldName + "不是有效的非负数字类型。");
//				}
//			}
//			// 判断长度时过滤+ -
//			int beginIndex = 0;
//			if (value.indexOf("+") >= 0 || value.indexOf("-") >= 0) {
//				beginIndex = 1;
//			}
//			// 如果数值包括小数点：分别判断整数和小数的位数是否超过指定的长度
//			if (value.indexOf(".") >= 0) {
//				if (value.substring(beginIndex, value.indexOf("."))
//						.length() > precision) {
//					throw new RuntimeException(fieldName
//							+ "数字不符合要求，请控制在整数位不能大于" + precision
//							+ "位，小数位不能大于" + fraction + "位。");
//				}
//				// 如果不包括小数点：只判断整数的位数是否超过了指定的长度
//			} else if (StringUtils
//					.isNotBlank(value)) {
//				if (value.substring(beginIndex, value.length()).length() > precision) {
//					throw new RuntimeException(fieldName
//							+ "数字不符合要求，请控制在整数位不能大于" + precision + "位。");
//				}
//			}
//
//			if (value == null || value.trim().equals("")) {
//				value = "0";
//			}
//			return value;
//		}

        if (org.apache.commons.lang3.StringUtils.isEmpty(regex)) {
            return value;
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if(!matcher.matches()) {
            return errorMsg;
        }
        return value;
    }

    /**
     * 结果信息
     * @param totalCount
     * @param successCount
     * @param errorCount
     * @param errorDataList
     * @return
     */
    private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }
    /**
     * 返回错误消息
     */
    private String errorResult(String da1, String da2, String da3, String da4,
                               int sequence, int total, int success, List<String[]> errorDataList) {
        addError(da1, da2, da3, da4, sequence, errorDataList);
        return result(total, success, total-success, errorDataList);
    }
    /**
     * 添加错误
     */
    private void addError(String da1, String da2, String da3, String da4,
                          int sequence, List<String[]> errorDataList) {
        String[] errorData=new String[4];
        sequence++;
        errorData[0]= org.apache.commons.lang3.StringUtils.trimToEmpty(da1);
        errorData[1]= org.apache.commons.lang3.StringUtils.trimToEmpty(da2);
        errorData[2]= org.apache.commons.lang3.StringUtils.trimToEmpty(da3);
        errorData[3]= org.apache.commons.lang3.StringUtils.trimToEmpty(da4);
        errorDataList.add(errorData);
    }
}
