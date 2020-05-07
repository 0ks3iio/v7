package net.zdsoft.newstusys.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.remote.utils.BusinessUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.dto.StudentDto;
import net.zdsoft.newstusys.entity.*;
import net.zdsoft.newstusys.service.BaseStudentService;
import net.zdsoft.newstusys.service.StudentResumeService;
import net.zdsoft.newstusys.service.StusysColsDisplayService;
import net.zdsoft.newstusys.service.StusysEditOptionService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.tutor.remote.service.TutorRemoteService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2018/2/27.
 */
@Controller
@RequestMapping("/newstusys/sch/student")
public class BaseStudentAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private RegionRemoteService regionRemoteService;
    @Autowired
    private BaseStudentService baseStudentService;
    @Autowired
    private StudentResumeService studentResumeService;
    @Autowired
    private FamilyRemoteService familyRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private TeachClassStuRemoteService teachClassStuRemoteService;
    @Autowired
    private StuworkRemoteService stuworkRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private FilePathRemoteService filePathRemoteService;
    @Autowired
    private TutorRemoteService tutorRemoteService;
    @Autowired
    private SystemIniRemoteService systemIniRemoteService;
    @Autowired
    private StusysEditOptionService stusysEditOptionService;
    @Autowired
    private StusysColsDisplayService stusysColsDisplayService;
    
    @RequestMapping("/studentadmin")
    public String studentAdmin(String classId, String studentId, ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, semester.getAcadyear()), new TR<List<Clazz>>() {
        });
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        map.put("classList", classList);
        map.put("classId", classId);
        map.put("studentId", studentId);
        String bakMsg = RedisUtils.lpop(StusysStudentBak.BAK_PREFIX+unitId+StusysStudentBak.BAK_MSG);
        map.put("bakMsg", StringUtils.trimToEmpty(bakMsg));
        return "/newstusys/sch/student/newStudentAdmin.ftl";
    }

    @RequestMapping("/studentList")
    public String studentList(String studentName, String field, String keyWord, String classId, HttpServletRequest request, ModelMap modelMap) {
        Pagination page = createPagination();
        String unitId = getLoginInfo().getUnitId();
        Student sear = new Student();
        Family searchFamily = new Family();
        sear.setIsLeaveSchool(0);
        String[] clsIds = null;
        if (StringUtils.isNotEmpty(classId)) {
            clsIds = new String[]{classId};
        }
        String[] stuIds = getStuIds(sear, searchFamily, field, keyWord);
        JSONObject resultJson = JSONObject.parseObject(studentRemoteService.findByIdsClaIdLikeStuCodeNames(unitId, stuIds,
                clsIds, SUtils.s(sear), SUtils.s(page)));
        List<Student> studentList = null;
        if (null != resultJson) {
            page.setMaxRowCount(resultJson.getIntValue("count"));
            studentList = Student.dt(resultJson.getString("data"));
        }
        studentList = getStudentList(studentList, null, unitId);
        modelMap.put("Pagination", page);
        sendPagination(request, modelMap, page);
        modelMap.put("studentList", studentList);
        return "/newstusys/sch/student/stuSysStudentList.ftl";
    }
    
    @RequestMapping("/pdfExport")
    public void pdfExport(HttpServletRequest request, HttpServletResponse response) {
    	String clsId = request.getParameter("clsId");
    	try {
//	    	String dp = "D:\\store\\"+System.currentTimeMillis()+".pdf";
	    	String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
	    	String filePath = "studentPdf" + File.separator + getLoginInfo().getUnitId() + File.separator + clsId + ".pdf";
	        String dp = fileSystemPath + File.separator + filePath;
	        File file = new File(dp);
	        if(file.exists()) {
	        	file.delete();
	        }
	        List<Student> stuList = Student.dt(studentRemoteService.findByClassIds(new String[] {clsId}));
	        if(CollectionUtils.isEmpty(stuList)) {
	        	return;
	        }
	        List<String> stuIds = EntityUtils.getList(stuList, Student::getId);
	        List<String> urls = new ArrayList<>();
//	        int i = 0;
	        String domain = UrlUtils.getPrefix(request);
	        for(String sid : stuIds) {
	        	urls.add(domain + "/newstusys/common/pdfHtml?clsId="+clsId+"&stuId="+sid);
//	        	if (i==3) {
//					break;
//				}
//	        	i++;
	        }
	    	HtmlToPdf.convert(urls.toArray(new String[0]), dp, null, null, 10000);
	        //如果文件不存在
	    	file = null;
	    	file = new File(dp);
	        if (!file.exists()) {
	            return;
	        }
//	        String downTime=request.getParameter("downId");//获取下载的时间戳
//			ServletUtils.addCookie(response,"D"+downTime, downTime, 20000);
			ServletUtils.download(new FileInputStream(file), request, response, "班级学生信息导出.pdf");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @RequestMapping("/stuPdfExport")
    public void stuPdfExport(HttpServletRequest request, HttpServletResponse response){
        String stuId = request.getParameter("stuId");
        try {
//	    	String dp = "D:\\store\\"+System.currentTimeMillis()+".pdf";
            Student stu = Student.dc(studentRemoteService.findOneById(stuId));
            if (stu == null) {
                return;
            }
            String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
            String filePath = "studentPdf" + File.separator + getLoginInfo().getUnitId() + File.separator + "student" + stu.getClassId() + File.separator + stuId + ".pdf";
            String dp = fileSystemPath + File.separator + filePath;
            File file = new File(dp);
            if(file.exists()) {
                file.delete();
            }
            List<String> urls = new ArrayList<>();
            String domain = UrlUtils.getPrefix(request);
            urls.add(domain + "/newstusys/common/pdfHtml?clsId="+stu.getClassId()+"&stuId="+stuId);
            HtmlToPdf.convert(urls.toArray(new String[0]), dp, null, null, 10000);
            //如果文件不存在
            file = null;
            file = new File(dp);
            if (!file.exists()) {
                return;
            }
//            String downTime=request.getParameter("downId");//获取下载的时间戳
//            ServletUtils.addCookie(response,"DS"+downTime, downTime, 20000);
            ServletUtils.download(new FileInputStream(file), request, response, "学生信息导出.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getStuIds(Student sear, Family searchFamily, String field, String keyWord) {
        String[] stuIds = null;
        if (StringUtils.isNotEmpty(keyWord) && StringUtils.isNotEmpty(field)) {
            keyWord = "%" + keyWord + "%";
            keyWord = org.apache.commons.lang3.StringUtils.trimToEmpty(keyWord);
            try {
                if (field.startsWith("5")) {
                    String[] arr = field.split("_");
                    PropertyUtils.setProperty(searchFamily, "relation", arr[0]);
                    PropertyUtils.setProperty(searchFamily, arr[1], keyWord);
                    List<Family> familyList = SUtils.dt(familyRemoteService.findListByCondition(searchFamily), Family.class);
                    Set<String> stuIdSet = familyList.stream().map(Family::getStudentId).collect(Collectors.toSet());
                    stuIds = stuIdSet.toArray(new String[0]);
                    if (stuIds == null) {
                        stuIds = new String[0];
                    }
                } else {
                    PropertyUtils.setProperty(sear, field, keyWord);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        return stuIds;
    }
    @RequestMapping("studentExport")
    public void exportStuList(String classId , HttpServletResponse resp) {
        if (StringUtils.isEmpty(classId)) {
            return;
        }
        Student sear = new Student();
        sear.setIsLeaveSchool(0);
        String[] clsIds = null;

        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findByGradeIdSortAll(clazz.getGradeId()), Clazz.class);


        Map<String, String> claNameMap = clazzList.parallelStream().collect(Collectors.toMap(Clazz::getId, Clazz::getClassNameDynamic));
        clsIds = claNameMap.keySet().toArray(new String[0]);
        Map<String, McodeDetail> sexMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> mzMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> zzmmMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-ZZMM"), new TypeReference<Map<String, McodeDetail>>() {
        });
        //DM-COUNTRY DM-GATQ DM-SFZJLX DM-XSLB DM-GX DM-ZZMM

        Map<String, McodeDetail> countryMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-COUNTRY"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> gatqMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-GATQ"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> sfzjlxMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-SFZJLX"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> xslbMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XSLB"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> gxMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-GX"), new TypeReference<Map<String, McodeDetail>>() {
        });
        Map<String, McodeDetail> whcdMcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-WHCD"), new TypeReference<Map<String, McodeDetail>>() {
        });


        List<Region> regionList = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
//        Map<String, String> regionNameMap = regionList.parallelStream().collect(Collectors.toMap(Region::getFullCode, Region::getFullName));
        Map<String, String> regionNameMap = new HashMap<>();
        for(Region region : regionList){
            regionNameMap.put(region.getFullCode(),region.getFullName());
        }

        List<Student> studentList = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(getLoginInfo().getUnitId(), null,
                clsIds, SUtils.s(sear), null));
        studentList = getStudentList(studentList,null,getLoginInfo().getUnitId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateMonthFormat = new SimpleDateFormat("yyyy-MM");
        List<String> stuIdList = studentList.stream().map(Student::getId).collect(Collectors.toList());
        int size = stuIdList.size();
        List<Family> familyList = new ArrayList<>();
        List<StudentResume> studentResumeList = new ArrayList<>();
        if(size > 1000){
            for(int i=0;i<size;){
                int j = i+1000;
                if(j > size){
                    j = size;
                }
                List<String> list = stuIdList.subList(i,j);
                List<Family> familySubList = SUtils.dt(familyRemoteService.findByStudentIds(list.toArray(new String[0])), Family.class);
                familyList.addAll(familySubList);
                List<StudentResume> studentSubResumeList = studentResumeService.findByStuids(list.toArray(new String[0]));
                studentResumeList.addAll(studentSubResumeList);
                i = j;

            }
        }else{
            familyList = SUtils.dt(familyRemoteService.findByStudentIds(stuIdList.toArray(new String[0])), Family.class);
            studentResumeList = studentResumeService.findByStuids(stuIdList.toArray(new String[0]));
        }
//        List<Family> familyList = SUtils.dt(familyRemoteService.findByStudentIds(stuIdList.toArray(new String[0])), Family.class);
        Map<String, Family> gFamilyMap = new HashMap<>();
        Map<String, Family> fFamilyMap = new HashMap<>();
        Map<String, Family> mFamilyMap = new HashMap<>();
//        List<StudentResume> studentResumeList = studentResumeService.findByStuids(stuIdList.toArray(new String[0]));
        Map<String, List<StudentResume>> stuResumeMap = EntityUtils.getListMap(studentResumeList, StudentResume::getStuid, Function.identity());

        for (Family f : familyList) {
            if (f.getIsGuardian() == BaseStudentConstants.RELATION_IS_GUARDIAN) {
                gFamilyMap.put(f.getStudentId(), f);
            }
            if (BaseStudentConstants.RELATION_FATHER.equals(f.getRelation())) {
                fFamilyMap.put(f.getStudentId(), f);
            } else if (BaseStudentConstants.RELATION_MOTHER.equals(f.getRelation())) {
                mFamilyMap.put(f.getStudentId(), f);
            }
        }
        String[] titleArr = new String[]{"*班级","*姓名", "曾用名", "性别", "出生日期", "民族", "政治面貌", "国籍",
                "港澳台侨外", "证件类型", "*证件编号", "学号", "班内编号", "学生类别", "原毕业学校", "入学年月", "一卡通卡号", "户籍省县", "户籍镇/街", "籍贯", "家庭住址",
                "家庭邮编", "家庭电话", "监护人", "监护人与学生关系", "监护人联系电话", "父亲姓名", "父亲政治面貌", "父亲文化程度", "父亲单位", "父亲职务", "父亲手机号", "父亲证件类型", "父亲身份证号",
                "父亲国籍", "父亲出生日期","母亲姓名", "母亲政治面貌", "母亲文化程度", "母亲单位", "母亲职务", "母亲手机号", "母亲证件类型","母亲身份证号", "母亲国籍", "母亲出生日期","特长爱好", "获奖情况", "简历"};

        String[] propertyNames = new String[]{"className","studentName", "oldName",
                 "sex", "birthday", "nation", "background", "country", "compatriots", "identitycardType",
                "identityCard", "studentCode", "classInnerCode", "studentType", "oldSchoolName", "toSchoolDate", "cardNumber", "registerPlace",
                "registerStreet","nativePlace", "homeAddress", "postalcode", "familyMobile",
                "gRealName", "gRelation", "gMobilePhone", "fRealName", "fPoliticalStatus",
                "fCulture", "fCompany", "fDuty", "fMobilePhone", "fIdcardType","fIdcard","fcountry","fBirthdayDateStr", "mRealName", "mPoliticalStatus", "mCulture",
                "mCompany", "mDuty", "mMobilePhone","mIdcardType","mIdcard","mcountry","mBirthdayDateStr","strong", "rewardRemark", "studentResume"};
        List<StudentDto> studentDtoList = new ArrayList<>();
        for (Student student : studentList) {
            StudentDto dto = new StudentDto();
            dto.setClassName(student.getClassName());
            dto.setStudentName(student.getStudentName());
            dto.setOldName(student.getOldName());
            dto.setClassName(claNameMap.get(student.getClassId()));
            dto.setStudentCode(student.getStudentCode());
            dto.setClassInnerCode(student.getClassInnerCode());
            dto.setNativePlace(regionNameMap.get(student.getNativePlace()));
            McodeDetail detail = sexMcodeMap.get(String.valueOf(student.getSex()));
            if (detail != null) {
                dto.setSex(detail.getMcodeContent());
            }

            if (student.getBirthday() != null) {
                dto.setBirthday(dateFormat.format(student.getBirthday()));
            }

            detail = mzMcodeMap.get(student.getNation());
            if (detail != null) {
                dto.setNation(detail.getMcodeContent());
            }

            detail = zzmmMcodeMap.get(student.getBackground());
            if (detail != null) {
                dto.setBackground(detail.getMcodeContent());
            }

            setVal(student, dto, countryMcodeMap, "country");
            setVal(student, dto, gatqMcodeMap, "compatriots");
            setVal(student, dto, sfzjlxMcodeMap, "identitycardType");
            dto.setIdentityCard(student.getIdentityCard());
            setVal(student, dto, xslbMcodeMap, "studentType");
            dto.setCardNumber(student.getCardNumber());
            dto.setOldSchoolName(student.getOldSchoolName());

            if (student.getToSchoolDate() != null) {
                dto.setToSchoolDate(dateMonthFormat.format(student.getToSchoolDate()));
            }
            dto.setRegisterPlace(regionNameMap.get(student.getRegisterPlace()));
            dto.setRegisterStreet(student.getRegisterStreet());

            dto.setHomeAddress(student.getHomeAddress());
            dto.setPostalcode(student.getPostalcode());
            dto.setFamilyMobile(student.getFamilyMobile());
            dto.setStrong(student.getStrong());
            dto.setRewardRemark(student.getRewardRemark());

            Family family = gFamilyMap.get(student.getId());
            if (family != null) {
                dto.setgRealName(family.getRealName());
                detail = gxMcodeMap.get(family.getRelation());
                if (detail != null) {
                    dto.setgRelation(detail.getMcodeContent());
                }
                dto.setgMobilePhone(family.getMobilePhone());
            }

            family = fFamilyMap.get(student.getId());
            if (family != null) {
                dto.setfRealName(family.getRealName());
                dto.setfCompany(family.getCompany());
                dto.setfDuty(family.getDuty());
                detail = whcdMcodeMap.get(family.getCulture());
                if (detail != null) {
                    dto.setfCulture(detail.getMcodeContent());
                }
                detail = zzmmMcodeMap.get(family.getPoliticalStatus());
                if (detail != null) {
                    dto.setfPoliticalStatus(detail.getMcodeContent());
                }
                dto.setfMobilePhone(family.getMobilePhone());
                dto.setfIdcard(family.getIdentityCard());
                if(StringUtils.isNotBlank(family.getIdentitycardType())){                	
                	dto.setfIdcardType(sfzjlxMcodeMap.get(family.getIdentitycardType()).getMcodeContent());
                }
                if(StringUtils.isNotBlank(family.getCountry())){                	
                	dto.setFcountry(countryMcodeMap.get(family.getCountry()).getMcodeContent());
                }
                if(null!=family.getBirthday()){            	
                	dto.setfBirthdayDateStr(DateUtils.date2StringByDay(family.getBirthday()));
                }
            }
            family = mFamilyMap.get(student.getId());
            if (family != null) {
                dto.setmRealName(family.getRealName());
                dto.setmCompany(family.getCompany());
                dto.setmDuty(family.getDuty());
                detail = whcdMcodeMap.get(family.getCulture());
                if (detail != null) {
                    dto.setmCulture(detail.getMcodeContent());
                }
                detail = zzmmMcodeMap.get(family.getPoliticalStatus());
                if (detail != null) {
                    dto.setmPoliticalStatus(detail.getMcodeContent());
                }
                dto.setmMobilePhone(family.getMobilePhone());
                dto.setmIdcard(family.getIdentityCard());
                if(StringUtils.isNotBlank(family.getIdentitycardType())){                	
                	dto.setmIdcardType(sfzjlxMcodeMap.get(family.getIdentitycardType()).getMcodeContent());
                }
                if(StringUtils.isNotBlank(family.getCountry())){                	
                	dto.setMcountry(countryMcodeMap.get(family.getCountry()).getMcodeContent());
                }
                if(null!=family.getBirthday()){            	
                	dto.setmBirthdayDateStr(DateUtils.date2StringByDay(family.getBirthday()));
                }
            }

            List<StudentResume> resumeList = stuResumeMap.get(student.getId());
            StringBuffer buffer = new StringBuffer();
            if (CollectionUtils.isNotEmpty(resumeList)) {
                for (StudentResume resume : resumeList) {
                    Date startDate = resume.getStartdate();
                    if (startDate != null) {
                        String startStr = dateMonthFormat.format(startDate);
                        String[] arr = startStr.split("-");
                        buffer.append(arr[0] + "年" + arr[1] + "月");
                    }

                    Date endDate = resume.getEnddate();
                    if (endDate != null) {
                        String endStr = dateMonthFormat.format(endDate);
                        String[] arr = endStr.split("-");
                        buffer.append("至" + arr[0] + "年" + arr[1] + "月" + ",");
                    }
                    buffer.append(resume.getSchoolname() + "；");
                }
            }
            dto.setStudentResume(buffer.toString());
            studentDtoList.add(dto);
        }

        Map<String,List<StudentDto>> studentDtoListMap = new HashMap<>();
        studentDtoListMap.put("学生信息" ,studentDtoList);

        ExportUtils.newInstance().exportXLSFile(titleArr, propertyNames, studentDtoListMap, "学生信息", "学生信息", resp);
    }

    private void setVal(Student stu, StudentDto dto, Map<String, McodeDetail> map, String columnName) {
        try {
            Object val = PropertyUtils.getProperty(stu, columnName);
            if (val != null) {
                McodeDetail detail = map.get(String.valueOf(val));
                if (detail != null) {
                    PropertyUtils.setProperty(dto, columnName, detail.getMcodeContent());
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private List<Student> getStudentList(List<Student> studentTempList, Pagination page, String unitId) {
        if(CollectionUtils.isEmpty(studentTempList)) {
        	return new ArrayList<>();
        }
    	Set<String> classIdSet = studentTempList.stream().map(Student::getClassId).collect(Collectors.toSet());
        Map<String, List<Student>> studentMap = EntityUtils.getListMap(studentTempList, Student::getClassId, Function.identity());
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findClassListByIds(classIdSet.toArray(new String[0])), Clazz.class);
        studentTempList.clear();
        Map<String, Clazz> clsMap = new HashMap<>();
        for (Clazz clazz : clazzList) {
        	clsMap.put(clazz.getId(), clazz);
            List<Student> list = studentMap.get(clazz.getId());
            list.forEach(student -> {
                student.setClassName(clazz.getClassNameDynamic());
            });
            studentTempList.addAll(list);
        }
        List<Student> studentList = new ArrayList<>();
        if(page != null){
            //假分页
            page.setMaxRowCount(studentTempList.size());
            Integer pageSize = page.getPageSize();
            Integer pageIndex = page.getPageIndex();
            for (int i = pageSize * (pageIndex - 1); i < studentTempList.size(); i++) {
                if (i < pageSize * pageIndex && i >= pageSize * (pageIndex - 1)) {
                    studentList.add(studentTempList.get(i));
                } else {
                    break;
                }
            }
            
        }else{
        	studentList.addAll(studentTempList);
        }
        Collections.sort(studentList, new Comparator<Student>() {

			@Override
			public int compare(Student a, Student b) {
				if(StringUtils.equals(a.getClassId(), b.getClassId())) {
					String classInnerCode1 = a.getClassInnerCode();
	                if (StringUtils.isEmpty(a.getClassInnerCode())) {
	                    classInnerCode1 = "9999999999";
	                }
	                String classInnerCode2 = b.getClassInnerCode();
	                if (StringUtils.isEmpty(b.getClassInnerCode())) {
	                    classInnerCode2 = "9999999999";
	                }
	                long result = NumberUtils.toLong(classInnerCode1) - NumberUtils.toLong(classInnerCode2);
	                if (result == 0l) {
	                	classInnerCode1 = a.getStudentCode();
		                if (StringUtils.isEmpty(a.getStudentCode())) {
		                    classInnerCode1 = "999999999999999";
		                }
		                classInnerCode2 = b.getStudentCode();
		                if (StringUtils.isEmpty(b.getStudentCode())) {
		                    classInnerCode2 = "999999999999999";
		                }
	                    return NumberUtils.toLong(classInnerCode1) <= NumberUtils.toLong(classInnerCode2)?-1:1;
	                }
	                return result>0?1:-1;
				}
				Clazz ac = clsMap.get(a.getClassId());
				Clazz bc = clsMap.get(b.getClassId());
				if(ac.getSection() != bc.getSection()) {
					return ac.getSection() - bc.getSection();
				}
				if(StringUtils.equals(ac.getAcadyear(), bc.getAcadyear())) {
					return bc.getAcadyear().compareTo(ac.getAcadyear());
				}
				return StringUtils.trimToEmpty(ac.getClassName()).compareTo(bc.getClassName());
			}
		});
        return studentList;
    }

    @RequestMapping("/newStudent")
    public String newStudent(String studentId, String classId, String acadyear, ModelMap modelMap) {
    	modelMap.put("hw", isHw());
        modelMap.put("studentId", studentId);
        modelMap.put("classId", classId);
        Student student = new Student();
        String unitId = getLoginInfo().getUnitId();
        if (StringUtils.isNotEmpty(studentId)) {
            student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            if (student == null || student.getIsDeleted() == 1) {
                studentId = null;
            }
        }
        if (StringUtils.isNotEmpty(studentId)) {
            List<Family> familyList = SUtils.dt(familyRemoteService.findByStudentId(studentId), Family.class);
            Family family = new Family();
            if (CollectionUtils.isNotEmpty(familyList)) {
                for (Family f : familyList) {
                    if (f.getIsGuardian() == BaseStudentConstants.RELATION_IS_GUARDIAN) {
                        modelMap.put("family1", f);
                    }
                    if (BaseStudentConstants.RELATION_FATHER.equals(f.getRelation())) {
                        modelMap.put("family2", f);
                    } else if (BaseStudentConstants.RELATION_MOTHER.equals(f.getRelation())) {
                        modelMap.put("family3", f);
                    }
                }
            }
            if (!modelMap.containsKey("family1")) {
                modelMap.put("family1", family);
            }
            if (!modelMap.containsKey("family2")) {
                modelMap.put("family2", family);
            }
            if (!modelMap.containsKey("family3")) {
                modelMap.put("family3", family);
            }
            if (StringUtils.isNotEmpty(student.getFilePath())) {
                modelMap.put("hasPic", true);
            }
            List<StudentResume> studentResumeList = studentResumeService.findByStuid(studentId);
            modelMap.put("studentResumeList", studentResumeList);
        } else {
            student.setSchoolId(unitId);
            student.setClassId(classId);
            Family family = new Family();
            modelMap.put("family1", family);
            modelMap.put("family2", family);
            modelMap.put("family3", family);
            List<StudentResume> studentResumeList = new ArrayList<StudentResume>();
            modelMap.put("studentResumeList", studentResumeList);
        }

        List<Region> regionList = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
        modelMap.put("regionList", regionList);
        modelMap.put("student", student);

        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(), semester.getAcadyear()), new TR<List<Clazz>>() {
        });
        modelMap.put("classList", classList);
        modelMap.put("random", "" + System.currentTimeMillis());
        return "/newstusys/sch/student/newStudentEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/getStudents")
    public List<Student> studentList(String classId) {
        return SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {
        });
    }

    @ResponseBody
    @RequestMapping("/saveStudent")
    public String newStudentSave(BaseStudent student, @RequestParam("hasAddPic") boolean hasAddPic, ExFamily exFamily, ExStudentResume exStudentResume) {

        try {
        	LoginInfo li = getLoginInfo();
        	String unitId = li.getUnitId();
            if (StringUtils.isEmpty(student.getId())) {
                student.setId(UuidUtils.generateUuid());
                if (StringUtils.isEmpty(student.getSchoolId())) {
					student.setSchoolId(unitId);
				}
                if (student.getToSchoolDate() == null) {
					Clazz cls = Clazz.dc(classRemoteService.findOneById(student.getClassId()));
					student.setToSchoolDate(DateUtils.string2Date(cls.getAcadyear().substring(0, 4)+"-09-01"));
					student.setEnrollYear(cls.getAcadyear());
				}
				student.setIsDeleted(0);
                student.setIsLeaveSchool(0);
                student.setEventSource(0);
                student.setCreationTime(new Date());
                student.setModifyTime(new Date());
            }
            if (BaseStudentConstants.IDCARDTYPE_ID.equals(student.getIdentitycardType())) {
                String str = BusinessUtils.validateIdentityCard(student.getIdentityCard(), false);
                if (StringUtils.isNotEmpty(str)) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(str.replaceAll(";", "")));
                }
            }
            if (BaseStudentConstants.SYSDEPLOY_SCHOOL_ZJZJ.equals(systemIniRemoteService.findValue(BaseStudentConstants.SYSTEM_DEPLOY_SCHOOL)) 
            		&& StringUtils.isNotEmpty(student.getStudentCode())) {
            	List<Student> studentList = Student
						.dt(studentRemoteService.findListBy(
								new String[] { "schoolId", "isDeleted", "isLeaveSchool", "studentCode" },
								new String[] { student.getSchoolId(), "0", "0", student.getStudentCode() }))
						.stream().filter(s -> !s.getId().equals(student.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(studentList)) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败，该学号/学籍铺号在本校已存在！"));
                }
                
                if(StringUtils.isNotEmpty(student.getRegisterPlace())) {
                	if(BaseStudentConstants.REGION_ZJZJ.equals(student.getRegisterPlace())) {
                		student.setSource(BaseStudentConstants.SOURCE_SN);
                	} else if(student.getRegisterPlace().startsWith(BaseStudentConstants.REGION_ZJZJ.substring(0, 2))){
                		student.setSource(BaseStudentConstants.SOURCE_SNSW);
                	} else {
                		student.setSource(BaseStudentConstants.SOURCE_SW);
                	}
                } else {
                	student.setSource(BaseStudentConstants.SOURCE_NONE);
                }
            }
            if (StringUtils.isNotEmpty(student.getIdentityCard())) {
                List<Student> studentList = SUtils.dt(studentRemoteService.findByIdentityCards(student.getIdentityCard()), Student.class)
                		.stream().filter(s -> s.getIsLeaveSchool()==0).collect(Collectors.toList());;
                if (CollectionUtils.isNotEmpty(studentList)) {
                    if (!studentList.get(0).getId().equals(student.getId())) {
                        return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败，该身份证号已存在！"));
                    }
                }
            }
            if (StringUtils.isNotEmpty(student.getUnitiveCode())) {
				List<Student> studentList = Student
						.dt(studentRemoteService.findListBy(
								new String[] { "isDeleted", "isLeaveSchool", "unitiveCode" },
								new String[] { "0", "0", student.getUnitiveCode() }))
						.stream().filter(s -> !s.getId().equals(student.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(studentList)) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败，该(全国)学籍号已存在！"));
                }
            }
            if (StringUtils.isNotEmpty(student.getCardNumber())) {
                Student es = Student.dc(studentRemoteService.findByCardNumber(unitId, student.getCardNumber()));
                if (es != null && !StringUtils.equals(student.getId(), es.getId())) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败，该一卡通卡号已存在！"));
                }
            }
            List<Family> familyList = null;
            if (exFamily != null) {
                List<Family> temps = exFamily.getFamilyTempList();
                if (CollectionUtils.isNotEmpty(temps)) {
                    familyList = new ArrayList<Family>();
                    Family gFam = temps.get(0);
                    Family fFam = null;
                    Family mFam = null;
					if (temps.size() >= 3) {
						fFam = temps.get(1);
						mFam = temps.get(2);
					}
                    Date now = new Date();
                    if (StringUtils.isEmpty(gFam.getRelation()) && StringUtils.isEmpty(gFam.getRealName())) {
                        if (StringUtils.isNotEmpty(gFam.getId())) {
                            gFam.setIsDeleted(1);
                            gFam.setModifyTime(now);
                        } else {
                            gFam = null;
                        }
                    }
                    if (fFam != null && StringUtils.isEmpty(fFam.getRealName())) {
                        if (StringUtils.isNotEmpty(fFam.getId())) {
                            fFam.setIsDeleted(1);
                            fFam.setModifyTime(now);
                        } else {
                            fFam = null;
                        }
                    }
                    if (mFam != null && StringUtils.isEmpty(mFam.getRealName())) {
                        if (StringUtils.isNotEmpty(mFam.getId())) {
                            mFam.setIsDeleted(1);
                            mFam.setModifyTime(now);
                        } else {
                            mFam = null;
                        }
                    }
                    dealGuardianFam(gFam, fFam);
                    if (gFam != null && gFam.getIsDeleted() == 9) {
                        gFam = null;
                    }
                    if (fFam != null && fFam.getIsDeleted() == 9) {
                        fFam = null;
                    }
                    dealGuardianFam(gFam, mFam);
                    if (gFam != null && gFam.getIsDeleted() == 9) {
                        gFam = null;
                    }
                    if (mFam != null && mFam.getIsDeleted() == 9) {
                        mFam = null;
                    }
                    if (gFam != null && gFam.getIsDeleted() == 0) {
                        if (StringUtils.isEmpty(gFam.getRelation())) {
                            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败，关系不能为空！"));
                        } else if (StringUtils.isEmpty(gFam.getRealName())) {
                            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败，监护人姓名不能为空！"));
                        }
                    }
                    if (gFam != null) {
                        familyList.add(gFam);
                    }
                    if (fFam != null) {
                        familyList.add(fFam);
                    }
                    if (mFam != null) {
                        familyList.add(mFam);
                    }
                    for (Family family : familyList) {
                        if (family.getIsDeleted() == 1 && StringUtils.isEmpty(family.getRealName())) {
                            family.setRealName(student.getStudentName() + "的家长");
                        }
                        if (StringUtils.isEmpty(family.getId())) {
                            family.setId(UuidUtils.generateUuid());
                            family.setStudentId(student.getId());
                            family.setCreationTime(now);
                            family.setIsDeleted(0);
                        }

                        family.setSchoolId(unitId);
                        family.setEventSource(0);
                        family.setIsLeaveSchool(0);
                        family.setModifyTime(now);
                        family.setLinkPhone(family.getMobilePhone());
                    }
                }
            }
            List<StudentResume> studentResumes = null;
            if (exStudentResume != null) {
                studentResumes = exStudentResume.getStudentResumeList();
                Iterator<StudentResume> iterator = null;
                if (studentResumes != null) {
                    iterator = studentResumes.iterator();
                    while (iterator.hasNext()) {
                        StudentResume studentResume = iterator.next();
                        if (studentResume.isEmpty()) {
                            iterator.remove();
                        } else {
                            if (StringUtils.isEmpty(studentResume.getId())) {
                                studentResume.setSchid(unitId);
                                studentResume.setStuid(student.getId());
                            }
                        }
                    }
                }
            }
            if(exFamily.getForAbnormal() == 1) {
            	StudentAbnormalFlow flow = exFamily.toFlow(null);
            	flow.setOperator(li.getUserId());
    			flow.setOperateunit(li.getUnitName());
            	baseStudentService.saveStudentForAbnormal(student, familyList, studentResumes, flow, hasAddPic);
            } else {
            	baseStudentService.saveStudent(student, familyList, studentResumes, hasAddPic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }

        return returnSuccess();

    }

    /**
     * 处理家长监护人信息
     * isDeleted=9，表示记录要删除
     * @param gFam
     * @param fFam
     */
    private void dealGuardianFam(Family gFam, Family fFam) {
        if (gFam != null && fFam != null) {// 维护家长或者软删家长
            if(fFam.getRelation().equals(gFam.getRelation())){// 监护人和家长是同样关系
            	// 监护人信息有维护
            	if (StringUtils.equals(fFam.getId(), gFam.getId()) // 1一条记录或同新增，
            			|| StringUtils.isEmpty(gFam.getId())) {// 2新增监护人， 则去掉监护人保存家长信息
        			if (StringUtils.isNotEmpty(gFam.getMobilePhone())) {
                        fFam.setMobilePhone(gFam.getMobilePhone());
                    }
                    if (StringUtils.isNotEmpty(gFam.getRealName())) {
                        fFam.setRealName(gFam.getRealName());
                    }
                    if(StringUtils.isNotEmpty(fFam.getRealName())) {
                    	fFam.setIsDeleted(0);
                    }
            		fFam.setIsGuardian(1);
                    gFam.setIsDeleted(9);
                } else {// 家长新增的
                	if (StringUtils.isNotEmpty(gFam.getMobilePhone())) {
                        fFam.setMobilePhone(gFam.getMobilePhone());
                    }
                    if (StringUtils.isNotEmpty(gFam.getRealName())) {
                        fFam.setRealName(gFam.getRealName());
                    }
            		fFam.setIsGuardian(1);
                    if (StringUtils.isEmpty(fFam.getId())) {
						fFam.setId(gFam.getId());
					}
					if (StringUtils.isEmpty(gFam.getId())) {
						gFam.setIsDeleted(9);
					} else {
						gFam.setIsDeleted(1);
					}
                }
            } else {// 关系不一样
            	fFam.setIsGuardian(0);
            	if(StringUtils.isNotEmpty(fFam.getId()) && StringUtils.equals(fFam.getId(), gFam.getId())) {// 同一条记录
            		if(gFam.getIsDeleted() == fFam.getIsDeleted()) {
            			if(gFam.getIsDeleted() == 0) {// 都维护
            				gFam.setId(null);
            			}
            		} else if(fFam.getIsDeleted() == 1) {// 监护人维护了，家长没维护
            			fFam.setIsDeleted(9);
            		} else if(gFam.getIsDeleted() == 1) {// 监护人没维护，家长维护
            			gFam.setIsDeleted(9);
            		}
            	}
            }  	
        } else if (fFam != null) {
            fFam.setIsGuardian(0);
        }
    }

    @RequestMapping("/studentShow")
    @ControllerInfo("学生信息查看")
    public String studentShowAdmin(ModelMap map) {
    	map.put("hw", isHw());
    	LoginInfo loginInfo=getLoginInfo();
    	String ownerId=loginInfo.getOwnerId();
    	String studentId = null;
    	if(User.OWNER_TYPE_FAMILY == loginInfo.getOwnerType()) {
    		Family family = SUtils.dc(familyRemoteService.findOneById(ownerId), Family.class);
    		if(family == null) {
    			return errorFtl(map, "家长信息不存在！");
    		}
    		studentId = family.getStudentId();
    	} else if(User.OWNER_TYPE_STUDENT == loginInfo.getOwnerType()) {
    		studentId = ownerId;
    	}
		if(StringUtils.isNotBlank(studentId)){
			newStudent(studentId, null, null, map);
			map.put("noBack", true);
			StusysEditOption option = stusysEditOptionService.findByUnitId(loginInfo.getUnitId());
			if(option == null || option.getIsOpen() == 0) {
				return "/newstusys/sch/studentShow/newStudentDetail.ftl";
			}
			List<StusysColsDisplay> schCols = stusysColsDisplayService.findByUnitIdType(loginInfo.getUnitId(),
					BaseStudentConstants.STUSYS_COLS_TYPE_STUDENTEDIT);
			if(CollectionUtils.isEmpty(schCols)) {
				return "/newstusys/sch/studentShow/newStudentDetail.ftl";
			}
			option.setDisplayCols(StringUtils
						.join(EntityUtils.getList(schCols, StusysColsDisplay::getColsCode).toArray(new String[0]), ",")
						+ ",");
			map.put("option", option);
			return "/newstusys/sch/studentShow/selfStudentEdit.ftl";
		}
		
        return "/newstusys/sch/studentShow/studentShowAdmin.ftl";
    }

    @RequestMapping("/studentShowMain")
    @ControllerInfo("学生信息查看 班级列表")
    public String studentShowMain(String tabType, ModelMap map) {
        Set<String> classIdSet = stuworkRemoteService.findClassSetByUserIdClaType(getLoginInfo().getUserId(), tabType, BaseStudentConstants.PERMISSION_TYPE_STUDENT);
        List<Clazz> classList = new ArrayList<>();
        if ("1".equals(tabType)) {
            classList = SUtils.dt(classRemoteService.findClassListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Collections.sort(classList, new Comparator<Clazz>() {
                @Override
                public int compare(Clazz o1, Clazz o2) {
                    String acadyear1 = o1.getAcadyear();
                    String acadyear2 = o2.getAcadyear();
                    String claCod1 = o1.getClassCode();
                    String claCod2 = o2.getClassCode();
                    int result = acadyear2.compareTo(acadyear1);
                    if(result == 0){
                        result = claCod1.compareTo(claCod2);
                    }
                    return result;
                }
            });
        } else if("2".equals(tabType)){
            List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(classIdSet.toArray(new String[0])), TeachClass.class);
            for (TeachClass teachClass : teachClassList) {
                Clazz cla = new Clazz();
                cla.setId(teachClass.getId());
                cla.setClassNameDynamic(teachClass.getName());
                classList.add(cla);
            }
        }
        map.put("classList", classList);
        map.put("tabType", tabType);
        return "/newstusys/sch/studentShow/studentShowMain.ftl";
    }

    @RequestMapping("/studentShowList")
    @ControllerInfo("学生信息查看 列表展示")
    public String studentShowList(String classId, String field, String keyWord, String tabType, ModelMap map, HttpServletRequest request) {
        Pagination page = createPagination();
        String[] clsIds = new String[0];
        if (StringUtils.isNotEmpty(classId)) {
            clsIds = new String[]{classId};
        }else{
            Set<String> classIdSet = stuworkRemoteService.findClassSetByUserIdClaType(getLoginInfo().getUserId(), tabType, BaseStudentConstants.PERMISSION_TYPE_STUDENT);
            if(CollectionUtils.isNotEmpty(classIdSet)){
                clsIds = classIdSet.toArray(new String[0]);
            }
        }
        Student searchStu = new Student();
        searchStu.setIsLeaveSchool(0);
        Family searchFamily = new Family();
        String unitId = getLoginInfo().getUnitId();
        String[] stuIds = getStuIds(searchStu, searchFamily, field, keyWord);
        List<Student> studentList = new ArrayList<>();
        if ("1".equals(tabType)) {
            studentList = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(unitId, stuIds,
                    clsIds, SUtils.s(searchStu), null));
        } else {
            List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findByClassIds(clsIds), TeachClassStu.class);
            Map<String, String> teachClaStuMap = teachClassStus.stream().collect(Collectors.toMap(TeachClassStu::getStudentId, TeachClassStu::getClassId));
            Set<String> studentSet = teachClaStuMap.keySet();
            if (stuIds == null) {
                stuIds = studentSet.toArray(new String[0]);

            } else {
                Set<String> stuIdSet = Stream.of(stuIds).collect(Collectors.toSet());
                studentSet.retainAll(stuIdSet);
                stuIds = studentSet.toArray(new String[0]);
            }
            if (stuIds == null) {
                stuIds = new String[0];
            }
            studentList = Student.dt(studentRemoteService.findByIdsClaIdLikeStuCodeNames(unitId, stuIds,
                    null, SUtils.s(searchStu), null));
        }

        studentList = getStudentList(studentList, page, unitId);
        
        map.put("Pagination", page);
        sendPagination(request, map, page);

        map.put("studentList", dealFam4Stus(studentList));
        return "/newstusys/sch/studentShow/studentShowList.ftl";
    }
    
    /**
     * 学生信息整理，添加家长信息显示
     * @param studentList
     * @return
     */
    private List<StudentDto> dealFam4Stus(List<Student> studentList){
    	List<StudentDto> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(studentList)) {
        	List<Family> fams = SUtils.dt(familyRemoteService.findByStudentIds(EntityUtils.getList(studentList, Student::getId).toArray(new String[0])), 
        			new TR<List<Family>>() {});
        	Map<String, List<Family>> famMap = new HashMap<>();
        	boolean hasFam = CollectionUtils.isNotEmpty(fams);
        	for(Family fam : fams) {
        		List<Family> sfs = famMap.get(fam.getStudentId());
        		if(sfs == null) {
        			sfs = new ArrayList<>();
        			famMap.put(fam.getStudentId(), sfs);
        		}
        		sfs.add(fam);
        	}
        	for(Student stu : studentList) {
        		StudentDto dto = new StudentDto();
        		dto.entityToDto(stu);
        		if(hasFam && famMap.containsKey(stu.getId())) {
        			famMap.get(stu.getId()).forEach(e->dto.famToDto(e));
        		}
        		dtoList.add(dto);
        	}
        }
        return dtoList;
    }

    @RequestMapping("/tutorStudentShowList")
    @ControllerInfo("学生信息查看>导师班学生")
    public String tutorStuList(String tabType, ModelMap map) {
    	Set<String> sids = SUtils.dt(tutorRemoteService.getTutorStuByTeacherId(getLoginInfo().getOwnerId()), new TR<Set<String>>() {});
    	List<Student> stus = Student.dt(studentRemoteService.findListByIds(sids.toArray(new String[0])));
    	stus = getStudentList(stus, null, getLoginInfo().getUnitId());
    	map.put("studentList", dealFam4Stus(stus));
    	map.put("tabType", tabType);
        return "/newstusys/sch/studentShow/studentShowList.ftl";
    }
    
    @RequestMapping("/studentDeatilShow")
    @ControllerInfo("学生信息查看")
    public String studentShowDetail(String studentId, String classId, String acadyear, ModelMap modelMap) {
        newStudent(studentId, classId, acadyear, modelMap);
        return "/newstusys/sch/studentShow/newStudentDetail.ftl";
    }

    @ControllerInfo("删除学生")
    @ResponseBody
    @RequestMapping("/deleteStudent")
    public String deleteStudentByIds(@RequestParam(value = "ids[]") String[] ids, ModelMap modelMap) {
        try {
            if (ArrayUtils.isNotEmpty(ids)) {
                baseStudentService.deleteByStuIds(ids);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);;
            return returnError();
        }

        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/studentResumeDelete")
    public String deleteStudentResumeById(String id) {
        try {
            if (StringUtils.isNotEmpty(id)) {
                studentResumeService.delete(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }

        return returnSuccess();

    }

    @ResponseBody
    @RequestMapping("/stuPicSave")
    public String saveStudentPic(ModelMap modelMap, HttpServletRequest request) {

        MultipartFile file = StorageFileUtils.getFile(request);
        if (file == null) {
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("上传失败,该图片为空！"));
        }
        if (file.getSize() == 0) {
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("上传失败,该图片为空！"));
        }
        String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
        if (StringUtils.isEmpty(fileSystemPath)) {
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("上传失败"));
        }
        String extName = StorageFileUtils.getFileExtension(file.getOriginalFilename());
        String filePath = BaseStudentConstants.PICTURE_FILEPATH + File.separator + getLoginInfo().getUnitId();
        String dirPath = fileSystemPath + File.separator + filePath;
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String filePathName = filePath + File.separator + UuidUtils.generateUuid() + "." + extName;
        File orFile = new File(fileSystemPath + File.separator + filePathName);
        try {
            FileUtils.writeByteArrayToFile(orFile, file.getBytes());
        } catch (Exception e) {
            log.error(e);
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("上传失败"));
        }
        return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！").setBusinessValue(filePathName));
    }

    @ResponseBody
    @RequestMapping(value = "/studentpic/show")
    @ControllerInfo(value = "显示学生照片")
    public String showPic(String studentId, String filePath, HttpServletResponse response) {
        try {
            File pic;
            if (StringUtils.isEmpty(studentId)) {
                return null;
            }
            Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            if (StringUtils.isNotEmpty(student.getFilePath())) {
                String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
                pic = new File(fileSystemPath + File.separator + student.getFilePath());
                if (pic != null && pic.exists()) {
                    response.getOutputStream().write(FileUtils.readFileToByteArray(pic));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ===============信息修改设置============================================
    @ControllerInfo("信息修改设置")
    @RequestMapping("/option/index")
    public String optionIndex(HttpServletRequest request, ModelMap map) {
		String unitId = getLoginInfo().getUnitId();
		StusysEditOption option = stusysEditOptionService.findByUnitId(unitId);
		if (option == null) {
			option = new StusysEditOption();
			option.setIsOpen(0);
		} else {
			List<StusysColsDisplay> schCols = stusysColsDisplayService.findByUnitIdType(unitId,
					BaseStudentConstants.STUSYS_COLS_TYPE_STUDENTEDIT);
			if (CollectionUtils.isNotEmpty(schCols)) {
				option.setDisplayCols(StringUtils
						.join(EntityUtils.getList(schCols, StusysColsDisplay::getColsCode).toArray(new String[0]), ",")
						+ ",");
			}
		}
		option.setDisplayCols(StringUtils.trimToEmpty(option.getDisplayCols()));
		List<StusysColsDisplay> cols = stusysColsDisplayService.findByUnitIdType(BaseConstants.ZERO_GUID,
				BaseStudentConstants.STUSYS_COLS_TYPE_STUDENTEDIT);
		map.put("cols", cols);
		map.put("option", option);
		return "/newstusys/sch/student/editOption.ftl";
    }
    
    @ControllerInfo("信息修改设置-保存")
    @ResponseBody
    @RequestMapping("/option/save")
    public String optionSave(StusysEditOption option) {
    	try {
    		if(StringUtils.isEmpty(option.getUnitId())) {
    			option.setUnitId(getLoginInfo().getUnitId());
    		}
    		if (StringUtils.isEmpty(option.getId())) {
				StusysEditOption old = stusysEditOptionService.findByUnitId(option.getUnitId());
				if(old != null) {
					option.setId(old.getId());
				}
			}
			stusysEditOptionService.saveOption(option);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return returnError();
		}
    	return returnSuccess();
    }
    
    public boolean isHw(){
		return BaseConstants.DEPLOY_HANGWAI.equals(systemIniRemoteService.findValue("SYSTEM.DEPLOY.REGION"));
	}

}
