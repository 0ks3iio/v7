package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.DiathesisClassDto;
import net.zdsoft.diathesis.data.dto.DiathesisGradeDto;
import net.zdsoft.diathesis.data.dto.DiathesisSemesterDto;
import net.zdsoft.diathesis.data.entity.DiathesisSetSubject;
import net.zdsoft.diathesis.data.service.DiathesisSetSubjectService;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.remote.service.ExamManageRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/8/16 12:44
 */
@RestController
@RequestMapping("/diathesis/subject")
public class DiathesisSubjectAction extends BaseAction {

    @Autowired
    private GradeTeachingRemoteService gradeTeachingRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ExamManageRemoteService examManageRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;


    @Autowired
    private ClassRemoteService classRemoteService;


    @Autowired
    private CourseRemoteService courseRemoteService;

    @Autowired
    private DiathesisSetSubjectService diathesisSetSubjectService;


  /*  @GetMapping("/getAllGradeList")
    @ResponseBody
    public String getAllGradeList(){
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY,BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);
        Map<Integer, List<Grade>> sectionMap = gradeList.stream().collect(Collectors.groupingBy(x -> x.getSection()));
        String sections = schoolRemoteService.findSectionsById(unitId);
        if(StringUtils.isBlank(sections)){
            return error("改单位没有学段信息");
        }
        List<DiathesisGradeDto> result=new ArrayList<>();
        if(sections.contains("1")){
            //小学
            result.addAll(getGradeEntity("1","小",sectionMap.get(1)));
        }
        if(sections.contains("2")){
            //初中
            result.addAll(getGradeEntity("2","初",sectionMap.get(2)));
        }
        if(sections.contains("3")){
            //高中
            result.addAll(getGradeEntity("3","高",sectionMap.get(3)));
        }

        Json json = new Json();
        json.put("currentSemester",""+SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class).getSemester());
        json.put("gradeList",result);
        return json.toJSONString();
    }*/

    @GetMapping("/getAllGradeList")
    @ResponseBody
    public String getAllGradeList(){
        Json json = new Json();
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY,BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);
        if(CollectionUtils.isEmpty(gradeList))return json.toJSONString();
        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdIn(new String[]{unitId}), Clazz.class);
        if(CollectionUtils.isEmpty(classList))return json.toJSONString();
        Map<String, List<DiathesisClassDto>> gradeIdClassMap = EntityUtils.getListMap(classList, x -> x.getGradeId(), x -> {
            DiathesisClassDto classDto = new DiathesisClassDto();
            classDto.setClassId(x.getId());
            classDto.setClassName(x.getClassName());
            return classDto;
        });
        String[] levelList=new String[]{"一","二","三","四","五","六","七","八","九"};
        String[] sectionList=new String[]{"小","初","高"};
        List<DiathesisGradeDto> result=new ArrayList<>();
        gradeList.sort(Comparator.comparingInt(x->Integer.parseInt(x.getGradeCode())));
        for (Grade grade : gradeList) {
            String gradeCode = grade.getGradeCode();
            int section=Integer.parseInt(gradeCode.substring(0,1));
            int gradeLevel=Integer.parseInt(gradeCode.substring(1));
            DiathesisGradeDto dto = new DiathesisGradeDto();
            dto.setGradeId(grade.getId());
            dto.setGradeName(sectionList[section-1]+levelList[gradeLevel-1]);
            dto.setClassList(gradeIdClassMap.get(grade.getId()));
            for (int i = 1; i <= gradeLevel; i++) {
                String prefix=sectionList[section-1]+levelList[i-1];
                if(i<gradeLevel){
                    prefix="原"+prefix;
                }
                for (int j = 1; j <= 2; j++) {
                    DiathesisSemesterDto semesterDto = new DiathesisSemesterDto();
                    String name=prefix+"(第"+levelList[j-1]+"学期)";
                    semesterDto.setName(name);
                    semesterDto.setGradeCode(""+section+i);
                    semesterDto.setSemester(""+j);
                    dto.getSemesterDtoList().add(semesterDto);
                }
            }
            result.add(dto);
        }
       // Json json = new Json();
        json.put("currentSemester",""+SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class).getSemester());
        json.put("gradeList",result);
        return json.toJSONString();
    }


    /**
     * 查找所有考试 和下面的科目
     *
     */
    @RequestMapping("/findAllExam")
    @ResponseBody
    public String findAllExam(String gradeId, String semester, String gradeCode, String scoreType) {

        if(StringUtils.isBlank(gradeId) || StringUtils.isBlank(semester) || StringUtils.isBlank(gradeCode)){
            return error("参数错误");
        }
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        if(grade==null)return error("不存在这个年级");
        String openAcadyear = grade.getOpenAcadyear();
        int n = Integer.parseInt(openAcadyear.split("-")[0]);
        int level = Integer.parseInt(gradeCode.substring(1));
        String acadyear=(n+level-1)+"-"+(n+level);
        //符合条件的所有 考试集合
        String unitId = getLoginInfo().getUnitId();

        JSONObject jsonObject = JSON.parseObject(examManageRemoteService.getExamList(unitId, gradeCode, acadyear, semester), JSONObject.class);
        JSONArray examlist = (JSONArray)jsonObject.get("examlist");

        String[] examIds = EntityUtils.getArray(examlist, x -> ((JSONObject) x).get("examId").toString(),String[]::new);
        if(examIds==null || examIds.length==0)return JSON.toJSONString(examlist);
        List<EmSubjectInfo> subList = SUtils.dt(examManageRemoteService.getExamSubByUnitIdAndExamId(unitId, examIds), EmSubjectInfo.class);
        Set<String> subIds = EntityUtils.getSet(subList, x -> x.getSubjectId());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), Course.class);
        Map<String, String> subNameMap = EntityUtils.getMap(courseList, x -> x.getId(), x -> x.getSubjectName());
        Map<String, List<String>> examMap = subList.stream()
                .collect(Collectors.groupingBy(x -> x.getExamId()
                        , Collectors.mapping(x -> subNameMap.get(x.getSubjectId()), Collectors.toList())));

        //过滤没有成绩的考试场次
        Iterator<Object> it = examlist.iterator();
        while (it.hasNext()){
            JSONObject next = (JSONObject) (it.next());
            List<String> list = examMap.get(next.get("examId"));
            if(CollectionUtils.isEmpty(list)){
                it.remove();
            }else{
                next.put("subList",list);
            }
        }
        return JSON.toJSONString(examlist);
    }


    private List<DiathesisGradeDto> getGradeEntity(String type,String prefix, List<Grade> list) {
        String unitId = getLoginInfo().getUnitId();
        List<String> gradeList = Arrays.asList("一", "二", "三","四","五","六");
        List<DiathesisGradeDto> result=new ArrayList<>();
        for (int i = 0; i <(BaseConstants.SECTION_PRIMARY== Integer.parseInt(type) ?6:3) ; i++) {
            DiathesisGradeDto dto = new DiathesisGradeDto();
            dto.setGradeName(prefix+gradeList.get(i));
            dto.setGradeId(list.get(i).getId());
            List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, list.get(i).getId()), Clazz.class);
            List<DiathesisClassDto> classDtoList = EntityUtils.getList(classList, x -> {
                DiathesisClassDto classDto = new DiathesisClassDto();
                classDto.setClassId(x.getId());
                classDto.setClassName(x.getClassName());
                return classDto;
            });
            if(CollectionUtils.isNotEmpty(classDtoList)){
                dto.setClassList(classDtoList);
            }else{
                dto.setClassList(new ArrayList<>());
            }
            for (int j = 0; j <i+1 ; j++) {
                for (int k = 0; k <2 ; k++) {
                    DiathesisSemesterDto semesterDto = new DiathesisSemesterDto();
                    String name=prefix+gradeList.get(j)+" (第"+gradeList.get(k)+"学期)";
                    if(j<i){
                        name="原"+name;
                    }
                    semesterDto.setName(name);
                    semesterDto.setGradeCode(type+(j+1));
                    semesterDto.setSemester(""+(k+1));
                    dto.getSemesterDtoList().add(semesterDto);
                }
            }
            result.add(dto);
        }

        return result;
    }


    /**
     * 查找所有科目
     *
     * 0 必修   1 选修   2 学业
     */
    @RequestMapping("/getSubjectSetInfo")
    public String getSubjectSetInfo(String gradeId,String gradeCode,String semester,String type){
        if ((!DiathesisConstant.SUBJECT_FEILD_BX.equals(type) && !DiathesisConstant.SUBJECT_FEILD_XY.equals(type)
                || StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(semester))){
            return error("参数异常");
        }
       // List<DiathesisSetSubject> list=diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId,gradeCode,Integer.parseInt(semester),type);

        String unitId = getLoginInfo().getUnitId();
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        String[] acadyearTmp = grade.getOpenAcadyear().split("-");
        String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
        List<GradeTeaching> gradeTeachingList = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, acadyear, semester, gradeId, Integer.parseInt(BaseConstants.SUBJECT_TYPE_BX)), GradeTeaching.class);
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getArray(gradeTeachingList, GradeTeaching::getSubjectId, String[]::new)), Course.class);
        courseList = courseList.stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId())).collect(Collectors.toList());


        return JSON.toJSONString(EntityUtils.getList(courseList, x -> {
            Json json = new Json();
            json.put("subId", x.getId());
            json.put("subName", x.getSubjectName());
            return json;
        }));

    }

    //0 必修    2 学业
    @RequestMapping("/getSubjectList")
    @ResponseBody
    public String getSubjectList(String gradeId,String gradeCode,String semester,String type){
        if(StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode) ||StringUtils.isBlank(semester) ||StringUtils.isBlank(type) ){
            return error("参数错误");
        }
        List<DiathesisSetSubject> subject = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), type);
        List<String> subIds = EntityUtils.getList(subject, x -> x.getSubjectId());
        List<Course> teachingCourses ;
        if(DiathesisConstant.SUBJECT_FEILD_XY.equals(type)){
            List<Course> list = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(getLoginInfo().getUnitId(), BaseConstants.SUBJECT_TYPE_BX, gradeCode.substring(0, 1)), Course.class);
            teachingCourses=EntityUtils.filter2(list,e -> BaseConstants.SUBJECT_TYPE_BX.equals(e.getType()) && Integer.valueOf(1).equals(e.getIsUsing()) && !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId()));
        }else{
            teachingCourses=diathesisSetSubjectService.getTeachingCourses(gradeId, gradeCode, Integer.parseInt(semester), getLoginInfo().getUnitId());
        }
        List<Json> result = EntityUtils.getList(teachingCourses, x -> {
            Json sub = new Json();
            sub.put("subId", x.getId());
            sub.put("subName", x.getSubjectName());
            sub.put("isUsing", subIds.indexOf(x.getId()) >= 0 ? "1" : "0");
            return sub;
        });
        return JSON.toJSONString(result);
    }

    //0 必修    2 学业
    @PostMapping("/updateSubjectList")
    @ResponseBody
    public String updateSubjectList(@RequestBody @Valid DiathesisGradeDto subjectSet, Errors errors){
        if(errors.hasFieldErrors())return error(errors.getFieldError().getDefaultMessage());
        try {
            subjectSet.setUnitId(getLoginInfo().getUnitId());
            diathesisSetSubjectService.updateSubjectList(subjectSet);
            return success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }
}
