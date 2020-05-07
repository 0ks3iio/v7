package net.zdsoft.stutotality.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.dto.StudentDto;
import net.zdsoft.stutotality.data.entity.*;
import net.zdsoft.stutotality.data.service.*;
import net.zdsoft.stutotality.data.util.StutotalityConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.zdsoft.stutotality.data.constant.ResultType.*;

@Controller
@RequestMapping("/stutotality/result")
public class StutotalityResultAction extends BaseAction{
    @Autowired
    StutotalityStuResultService stutotalityStuResultService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    TeachClassRemoteService teachClassRemoteService;
    @Autowired
    ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    SemesterRemoteService semesterRemoteService;
    @Autowired
    StudentRemoteService studentRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    StutotalityTypeService stutotalityTypeService;
    @Autowired
    StutotalityItemService stutotalityItemService;
    @Autowired
    StutotalityItemOptionService stutotalityItemOptionService;
    @Autowired
    StutotalityOptionDescService stutotalityOptionDescService;
    @Autowired
    StutotalityHealthService stutotalityHealthService;
    @Autowired
    StutotalityRewardService stutotalityRewardService;
    @Autowired
    StutotalityStuRewardService stutotalityStuRewardService;
    @Autowired
    StutotalityStuFinalService stutotalityStuFinalService;
    @Autowired
    StutotalityHealthOptionService stutotalityHealthOptionService;
    @Autowired
    StutotalityCheckStuService stutotalityCheckStuService;
    @RequestMapping("/scoreIndex/page")
    @ControllerInfo("成绩录入index")
    public String stutotalityScoreIndex(ModelMap map){
        LoginInfo info = getLoginInfo();
        List<Clazz> clazzList =findClazzListBy(info.getUnitId(),info.getUserId(),info.getOwnerId(),true,map);
        Semester se = SUtils.dc(semesterRemoteService.getCurrentSemester(1,info.getUnitId()), Semester.class);
        List<StutotalityCheckStu> checkStuLis=null;
       /* if(Boolean.parseBoolean(map.get("isAdmin").toString())){
            checkStuLis=stutotalityCheckStuService.findByUParms(info.getUnitId(),se.getAcadyear(),se.getSemester().toString());
        }else{
            //非管理员下
            if(CollectionUtils.isNotEmpty(clazzList)){
                Set<String> classIds=EntityUtils.getSet(clazzList,Clazz::getId);
                Set<String> stuIds=EntityUtils.getSet(SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])),Student.class),Student::getId);
                checkStuLis=stutotalityCheckStuService.findBySParms(stuIds.toArray(new String[0]),se.getAcadyear(),se.getSemester().toString());
            }
        }*/
        return "/stutotality/result/stutotalityScoreIndex.ftl";
    }
    public int findSizeBy(boolean isTeaching,String unitId,Semester semester,String gradeId,Set<String> subjectIds,Set<String> itemIds){
        int size=0;
        List<StutotalityType> types=stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId,semester.getAcadyear(),semester.getSemester().toString(),gradeId);
        if(CollectionUtils.isNotEmpty(types)) {
            Set<String> typeIds0 = new HashSet<>();
            Set<String> typeIds1 = new HashSet<>();
            types.forEach(t -> {
                if (t.getHasStat() == 1) {
                    typeIds1.add(t.getId());
                } else {
                    typeIds0.add(t.getId());
                }
            });
            if (!isTeaching && CollectionUtils.isNotEmpty(typeIds0)) {
                itemIds.addAll(EntityUtils.getSet(stutotalityItemService.getListByTypeIds(typeIds0.toArray(new String[0])), i -> i.getId()));
            }
            if (CollectionUtils.isNotEmpty(typeIds1)) {
                //计入总分的有大项成绩
                List<StutotalityItem> itemList = stutotalityItemService.getListByTypeIds(typeIds1.toArray(new String[0]));
                if(isTeaching){
                    if(CollectionUtils.isNotEmpty(itemList)){
                        List<StutotalityItem> lastItemList=new ArrayList<>();
                        for(StutotalityItem item:itemList){
                            if(subjectIds.contains(item.getSubjectId())){
                                lastItemList.add(item);
                                break;//暂时只考虑取一个
                            }
                        }
                        size = lastItemList.size();
                        itemIds.addAll(EntityUtils.getSet(lastItemList, i -> i.getId()));
                    }
                }else{
                    size = CollectionUtils.isNotEmpty(itemList) ? itemList.size() : 0;
                    itemIds.addAll(EntityUtils.getSet(itemList, i -> i.getId()));
                }
            }
            if (CollectionUtils.isNotEmpty(itemIds)) {
                //所有类型对应的option都需要有成绩
                List<StutotalityItemOption> optionList = stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
                size += CollectionUtils.isNotEmpty(optionList) ? optionList.size() : 0;
            }
        }
        return size;
    }
    @RequestMapping("/checkStuHaveOver")
    @ControllerInfo("获取学生的评价情况")
    @ResponseBody
    public String CheckStuHaveOver(String gradeId,String classId,String studentId,ModelMap map){
        JSONObject json=new JSONObject();
        LoginInfo info = getLoginInfo();
        String unitId=info.getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        boolean isAdmin = isAdmin(unitId, info.getUserId());//管理员权限

        List<Student> stuList=null;
        boolean isGrade=StringUtils.isNotBlank(gradeId);
        List<Clazz> clazzList=null;
        if(isGrade){
            clazzList=findClazzListBy(unitId,info.getUserId(),info.getOwnerId(),true,map);
            List<Clazz> lastList=new ArrayList<>();
            for(Clazz clazz:clazzList){
                if(clazz.getGradeId().equals(gradeId)){
                    lastList.add(clazz);
                }
            }
            if(CollectionUtils.isNotEmpty(lastList)){
                stuList=SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getSet(lastList,c->c.getId()).toArray(new String[0])),Student.class);
            }
        }else{
            /*if(StringUtils.isNotBlank(studentId)){
                Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
                stuList=new ArrayList<>();
                classId=student.getClassId();
                stuList.add(student);
                gradeId=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class).getGradeId();
            }else{
                stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),Student.class);
                gradeId=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class).getGradeId();
            }*/
            //为未评做实时交互 修改
            if(StringUtils.isNotBlank(studentId)){
                Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
                classId=student.getClassId();
            }
            stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),Student.class);
            clazzList=new ArrayList<>();
            Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
            clazzList.add(clazz);
            gradeId=clazz.getGradeId();
        }
        Set<String> subjectIds=new HashSet<>();
        boolean isTeaching=checkTeaching(unitId,isAdmin,info.getOwnerId(),classId,subjectIds);
        if(CollectionUtils.isNotEmpty(stuList)){
            Set<String> stuIds=EntityUtils.getSet(stuList,s->s.getId());
            Set<String> itemIds=new HashSet<>();
            int size = findSizeBy(isTeaching,unitId,semester,gradeId,subjectIds,itemIds);
            List<StutotalityStuResult> resultList=null;
            if(isTeaching){
                //取出任课老师的成绩结果
                resultList=stutotalityStuResultService.findListByStudentIds(semester.getAcadyear(),semester.getSemester().toString(),stuIds.toArray(new String[0]),itemIds.toArray(new String[0]));
            }else{
                //取出学生所有成绩结果
                resultList=isGrade&&isAdmin?stutotalityStuResultService.findListByStudentIds(semester.getAcadyear(),semester.getSemester().toString(),null,itemIds.toArray(new String[0]))
                        :stutotalityStuResultService.findListByStudentIds(semester.getAcadyear(),semester.getSemester().toString(),stuIds.toArray(new String[0]));
            }
            Map<String,List<StutotalityStuResult>> resultListMap=null;
            if(CollectionUtils.isNotEmpty(resultList)){
                resultListMap=resultList.stream().collect(Collectors.groupingBy(r->r.getStudentId()));
            }
            if(resultListMap==null) resultListMap=new HashMap<>();
            JSONObject in=null;
            if(isGrade){
                JSONArray classArray=getArrray(stuList,resultListMap,size,clazzList);
                json.put("classArray",classArray);
            }else{
                JSONArray classArray=getArrray(stuList,resultListMap,size,clazzList);
                json.put("classArray",classArray);
                JsonArray array=new JsonArray();
                for(Student stu:stuList){
                    in=new JSONObject();
                    in.put("studentId",stu.getId());
                    in.put("haveOver","0");
                    if(resultListMap.containsKey(stu.getId())){
                        if(resultListMap.get(stu.getId()).size()>=size){
                            in.put("haveOver","1");
                        }
                    }
                    array.add(in);
                }
                json.put("array",array);
            }
        }
        return json.toJSONString();
    }

    @RequestMapping("/scoreTab/page")
    @ControllerInfo("成绩录入tab")
    public String stutotalityScoreTab(Integer type,String classId,String studentId,String resultType,
                                        String itemId,String optionId,String itemExpand,ModelMap map){
        LoginInfo info = getLoginInfo();
        if(type==null){
            type=0;
        }
        if(StringUtils.isBlank(resultType)){
            resultType="1";
        }
        String gradeId="";
        if(StringUtils.isNotBlank(studentId)){
            classId=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class).getClassId();
        }
        gradeId=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class).getGradeId();
        Set<String> subjectIds=new HashSet<>();
        String unitId=getLoginInfo().getUnitId();
        boolean isAdmin = isAdmin(unitId, info.getUserId());//管理员权限
        boolean isTeaching=checkTeaching(unitId,isAdmin,info.getOwnerId(),classId,subjectIds);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        if (isTeaching) {//只有任课老师的权限
            type = 1;
        }
        List<StutotalityItem> itemList=null;
        Set<String> itemIds=null;
        if(StringUtils.isBlank(studentId)) {//班级有项目，个人没有
            if (isTeaching) {//只有任课老师权限
                itemList = getTeachingItemList(unitId, semester.getAcadyear(), semester.getSemester().toString(), gradeId, subjectIds);
                itemIds = EntityUtils.getSet(itemList, StutotalityItem::getId);
            } else {
                itemList = stutotalityItemService.getItemListByParams(unitId, semester.getAcadyear(), semester.getSemester().toString(), gradeId, type);
                itemIds = EntityUtils.getSet(itemList, StutotalityItem::getId);
            }
        }
        List<StutotalityItemOption> optionList=null;

        if(CollectionUtils.isNotEmpty(itemList)){//计入总分的班级，日常有内容 期末只有项目itemList
            if(!itemIds.contains(itemId)){
                itemId=itemList.get(0).getId();
            }
            if(!"2".equals(resultType)){//不是期末的情况下有项目内容
                optionList=stutotalityItemOptionService.findByItemIds(new String[]{itemId});
            }else{
                optionId=null;
            }
        }else{
            itemId=null;
            optionId=null;
        }
        map.put("isTeaching",isTeaching);
        map.put("resultType",resultType);//1日常 2期末
        map.put("itemList",itemList);
        map.put("optionList",optionList);
        map.put("itemId",itemId);
        if(CollectionUtils.isNotEmpty(optionList) && !EntityUtils.getSet(optionList, StutotalityItemOption::getId).contains(optionId)){
            optionId=optionList.get(0).getId();
        }
        map.put("optionId",optionId);
        map.put("type",type);
        map.put("studentId",studentId);
        map.put("itemExpand",itemExpand);

        return "/stutotality/result/stutotalityScoreTab.ftl";
    }
    @RequestMapping("/scoreClassList/page")
    @ControllerInfo("班级成绩录入list页面")
    public String stutotalityScoreClassList(Integer type,String classId,String itemId,String optionId,String resultType,ModelMap map){
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<StudentDto> list = new ArrayList<>();
        List<StutotalityStuResult> stutotalityStuResults = new ArrayList<>();
        StutotalityItemOption option = new StutotalityItemOption();
        //resultType代表是否期末
        //type代表是否计分
        if(StringUtils.isBlank(itemId)){
            return "/stutotality/result/stutotalityScoreClassList.ftl";
        }
        if(StringUtils.isNotBlank(optionId)) {
            stutotalityStuResults = stutotalityStuResultService.findItemHealthIdAndOptionIdWithMaster
                    (semester.getAcadyear(), semester.getSemester().toString(), unitId, resultType, itemId, optionId);
            option= stutotalityItemOptionService.findOne(optionId);
        }else {
            stutotalityStuResults = stutotalityStuResultService.findItemHealthIdWithMaster(semester.getAcadyear(), semester.getSemester().toString(), unitId, resultType, itemId);
        }
        Map<String,StutotalityStuResult> stuResultMap = stutotalityStuResults.stream().collect(Collectors.toMap(StutotalityStuResult::getStudentId,Function.identity()));
        for(Student student:studentList){
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            studentDto.setStudentName(student.getStudentName());
            studentDto.setSex(student.getSex());
            studentDto.setResultId(UuidUtils.generateUuid());
            studentDto.setResult(0f);
            studentDto.setStudentCode(student.getStudentCode());
            if(MapUtils.isNotEmpty(stuResultMap)) {
                StutotalityStuResult stutotalityStuResult = stuResultMap.get(student.getId());
                if(stutotalityStuResult!=null) {
                    studentDto.setResultId(stutotalityStuResult.getId());
                    studentDto.setResult(stutotalityStuResult.getResult());
                    String mark = stutotalityStuResult.getRemark();
                    if(StringUtils.isNotBlank(mark)){
                        studentDto.setRemark(mark);
                        studentDto.setRemarkSubStr(mark);
                        if(mark.length()>20) {
                            mark = mark.substring(0, 20) + "...";
                            studentDto.setRemarkSubStr(mark);
                        }
                    }
                }
            }
            list.add(studentDto);
        }
        map.put("desc","");
        if(option!=null){
            map.put("desc",option.getDescription());
        }
        map.put("stuDtoList",list);
        map.put("classId",classId);
        map.put("type",type);
        //计分类型
        map.put("resultType",resultType);
        return "/stutotality/result/stutotalityScoreClassList.ftl";
    }
    @RequestMapping("/scoreStuList/page")
    @ControllerInfo("个人学生list情况")
    public String stutotalityScoreStuList(String studentId,Integer type,String resultType,Boolean isTeaching,ModelMap map){
        LoginInfo info=getLoginInfo();
        String unitId = info.getUnitId();
        String gradeId="";
        String classId=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class).getClassId();
        if(StringUtils.isNotBlank(studentId)){
            gradeId=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class).getGradeId();
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<StutotalityItem> itemList=null;
        if(isTeaching!=null && isTeaching){
            Set<String> subjectIds=new HashSet<>();
            boolean isAdmin = isAdmin(unitId, info.getUserId());//管理员权限
            checkTeaching(unitId,isAdmin,info.getOwnerId(),classId,subjectIds);
            itemList = getTeachingItemList(unitId, semester.getAcadyear(), semester.getSemester().toString(), gradeId, subjectIds);
        }else{
            itemList=stutotalityItemService.getItemListByParams(unitId,semester.getAcadyear(),semester.getSemester()+"",gradeId,type);
        }
        Map<String, List<StutotalityItemOption>> optionMap = new HashMap<>();
        List<StutotalityStuResult> results = null;
        if(CollectionUtils.isEmpty(itemList)) {
            return "/stutotality/result/stutotalityScoreStuList2.ftl";
        }
        Set<String> itemIds = itemList.stream().map(StutotalityItem::getId).collect(Collectors.toSet());
        if("2".equals(resultType)){//2是期末 只取项目大类即可
            results = stutotalityStuResultService.findListByItemIdsWithMaster(semester.getAcadyear(), semester.getSemester().toString(), studentId, itemIds.toArray(new String[0]), Type2);
            Map<String, StutotalityStuResult> resultMap = EntityUtils.getMap(results,StutotalityStuResult::getItemHealthId);
            for (StutotalityItem stutotalityItem : itemList) {
                if (resultMap.containsKey(stutotalityItem.getId())) {
                    stutotalityItem.setResultId(resultMap.get(stutotalityItem.getId()).getId());
                    stutotalityItem.setResult(resultMap.get(stutotalityItem.getId()).getResult());
                } else {
                    stutotalityItem.setResultId(UuidUtils.generateUuid());
                    stutotalityItem.setResult(0f);
                }
            }
            map.put("resultType",resultType);
            map.put("type",type);
            map.put("itemList",itemList);
            map.put("studentId",studentId);
            return "/stutotality/result/stutotalityScoreStuList2.ftl";
        }else{//计分日常或不计入分
            List<StutotalityItemOption> optionList = stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
            if(CollectionUtils.isNotEmpty(optionList)) {
                Set<String> optionIds = optionList.stream().map(StutotalityItemOption::getId).collect(Collectors.toSet());
                results = stutotalityStuResultService.findListByOptionIdsWithMaster(semester.getAcadyear(),semester.getSemester().toString(),new String[]{studentId},optionIds.toArray(new String[0]),Type1);
                Map<String,StutotalityStuResult> resultMap = new HashMap<>();
                if(CollectionUtils.isNotEmpty(results)){
                    resultMap = results.stream().collect(Collectors.toMap(StutotalityStuResult::getOptionId, Function.identity()));
                }
                for(StutotalityItemOption stutotalityItemOption:optionList){
                    if(resultMap.containsKey(stutotalityItemOption.getId())){
                        stutotalityItemOption.setResultId(resultMap.get(stutotalityItemOption.getId()).getId());
                        stutotalityItemOption.setResult(resultMap.get(stutotalityItemOption.getId()).getResult());
                    }else {
                        stutotalityItemOption.setResultId(UuidUtils.generateUuid());
                        stutotalityItemOption.setResult(0f);
                    }
                }
                optionMap = optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
            }
//            map.put("isAdmin", isAdmin);
            map.put("studentId",studentId);
            map.put("optionMap",optionMap);
            map.put("itemList",itemList);
            map.put("resultType",resultType);
            map.put("type",type);
            return "/stutotality/result/stutotalityScoreStuList1.ftl";
        }
    }
    @RequestMapping("/saveOneStuResult")
    @ControllerInfo("班级-评价单个学生结果（备注或星星）")
    @ResponseBody
    public String saveOneStuResult(String itemId,String optionId,String resultId,String studentId,Float result,String remark,String resultType){
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<StutotalityStuResult> stutotalityStuResults = null;
        if(StringUtils.isNotBlank(optionId)) {
            stutotalityStuResults = stutotalityStuResultService.findListByOptionIdsWithMaster
                    (semester.getAcadyear(), semester.getSemester().toString(), new String[]{studentId}, new String[]{optionId}, resultType);
        }else {
            stutotalityStuResults = stutotalityStuResultService.findListByItemIdsWithMaster(semester.getAcadyear(), semester.getSemester().toString(), studentId, new String[]{itemId}, resultType);
        }
        try {
            StutotalityStuResult stutotalityStuResult = new StutotalityStuResult();
            if(CollectionUtils.isNotEmpty(stutotalityStuResults)) {
                remark=StringUtils.isBlank(remark)?stutotalityStuResults.get(0).getRemark():remark;
                result=result==null?stutotalityStuResults.get(0).getResult():result;
            }
            stutotalityStuResultService.deleteAll(stutotalityStuResults.toArray(new StutotalityStuResult[0]));
            String unitId= getLoginInfo().getUnitId();
            stutotalityStuResult.setId(resultId);
            stutotalityStuResult.setItemHealthId(itemId);
            stutotalityStuResult.setOptionId(optionId);
            stutotalityStuResult.setResult(result);
            stutotalityStuResult.setRemark(remark);
            if(StringUtils.isNotBlank(resultType)){
                stutotalityStuResult.setType(resultType);
            }else {
                stutotalityStuResult.setType(Type1);
            }
            stutotalityStuResult.setUnitId(unitId);
            stutotalityStuResult.setCreationTime(new Date());
            stutotalityStuResult.setAcadyear(semester.getAcadyear());
            stutotalityStuResult.setSemester(semester.getSemester()+"");
            stutotalityStuResult.setStudentId(studentId);
            stutotalityStuResult.setModifyTime(new Date());
            stutotalityStuResultService.save(stutotalityStuResult);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/saveSomeStusResult")
    @ControllerInfo("班级-批量评价学生结果")
    @ResponseBody
    public String saveSomeStusResult(String[] params,String itemId,String optionId,String result,String remark,String resultType){
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<StutotalityStuResult> stutotalityStuResults = stutotalityStuResultService.findItemHealthIdAndOptionIdWithMaster
                (semester.getAcadyear(),semester.getSemester().toString(),unitId,Type1,itemId,optionId);
        Map<String,StutotalityStuResult> stuResultMap = stutotalityStuResults.stream().collect(Collectors.toMap(StutotalityStuResult::getId, Function.identity()));
        List<StutotalityStuResult> list = new ArrayList<>();
        for(String param:params){
            if(StringUtils.isNotBlank(param)) {
                String[] temps = param.split("-");
                if (stuResultMap.containsKey(temps[1])) {
                    StutotalityStuResult stutotalityStuResult = stuResultMap.get(temps[1]);
                    stutotalityStuResult.setResult(Float.parseFloat(result));
                    stutotalityStuResult.setModifyTime(new Date());
                    stutotalityStuResult.setRemark(remark);
                    list.add(stutotalityStuResult);
                } else {
                    if (StringUtils.isNotBlank(param) && StringUtils.isNotBlank(temps[0])) {
                        StutotalityStuResult stutotalityStuResult = new StutotalityStuResult();
                        stutotalityStuResult.setResult(Float.parseFloat(result));
                        stutotalityStuResult.setId(temps[1]);
                        stutotalityStuResult.setItemHealthId(itemId);
                        stutotalityStuResult.setOptionId(optionId);
                        if (StringUtils.isNotBlank(resultType)) {
                            stutotalityStuResult.setType(resultType);
                        } else {
                            stutotalityStuResult.setType(Type1);
                        }
                        stutotalityStuResult.setUnitId(unitId);
                        stutotalityStuResult.setCreationTime(new Date());
                        stutotalityStuResult.setAcadyear(semester.getAcadyear());
                        stutotalityStuResult.setSemester(semester.getSemester() + "");
                        stutotalityStuResult.setStudentId(temps[0]);
                        stutotalityStuResult.setRemark(remark);
                        list.add(stutotalityStuResult);
                    }
                }
            }
        }
        try {
            stutotalityStuResultService.saveAll(list.toArray(new StutotalityStuResult[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/saveStuMoreResult")
    @ControllerInfo("个人-批量评价")
    @ResponseBody
    public String saveStuMoreResult(String[] params,String studentId,String result,String remark,String resultType){
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<StutotalityStuResult> stutotalityStuResults = stutotalityStuResultService.findByAcadyearAndSemesterAndUnitIdAndTypeWithMaster
                (semester.getAcadyear(),semester.getSemester().toString(),unitId,resultType,studentId);
        Map<String,StutotalityStuResult> stuResultMap = stutotalityStuResults.stream().collect(Collectors.toMap(StutotalityStuResult::getId, Function.identity()));
        List<StutotalityStuResult> list = new ArrayList<>();
        for(String param:params){
            if(StringUtils.isNotBlank(param)) {
                String[] temps = param.split("-");
                if (stuResultMap.containsKey(temps[0])) {
                    StutotalityStuResult stutotalityStuResult = stuResultMap.get(temps[0]);
                    stutotalityStuResult.setResult(Float.parseFloat(result));
                    stutotalityStuResult.setModifyTime(new Date());
                    stutotalityStuResult.setRemark(remark);
                    list.add(stutotalityStuResult);
                } else {
                    if (StringUtils.isNotBlank(param) && StringUtils.isNotBlank(temps[0])) {
                        StutotalityStuResult stutotalityStuResult = new StutotalityStuResult();
                        stutotalityStuResult.setResult(Float.parseFloat(result));
                        stutotalityStuResult.setId(temps[0]);
                        stutotalityStuResult.setItemHealthId(temps[1]);
                        if (temps.length > 2) {
                            stutotalityStuResult.setOptionId(temps[2]);
                        }
                        if (StringUtils.isNotBlank(resultType)) {
                            stutotalityStuResult.setType(resultType);
                        } else {
                            stutotalityStuResult.setType(Type1);
                        }
                        stutotalityStuResult.setUnitId(unitId);
                        stutotalityStuResult.setCreationTime(new Date());
                        stutotalityStuResult.setAcadyear(semester.getAcadyear());
                        stutotalityStuResult.setSemester(semester.getSemester() + "");
                        stutotalityStuResult.setStudentId(studentId);
                        stutotalityStuResult.setRemark(remark);
                        list.add(stutotalityStuResult);
                    }
                }
            }
        }
        try {
            stutotalityStuResultService.saveAll(list.toArray(new StutotalityStuResult[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/healthIndex/page")
    @ControllerInfo("学生身体素质")
    public String getHealIndex(ModelMap map){

        LoginInfo info = getLoginInfo();
        findClazzListBy(info.getUnitId(),info.getUserId(),info.getOwnerId(),false,map);
        return "/stutotality/result/stutotalityHeathIndex.ftl";
//        return getClassList(map,gradeId,SearchType0);
    }

    @RequestMapping("/getStuPhysicalQualityList")
    public String getStuPhysicalQualityList(String studentId,ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
        List<StutotalityHealthOption> stutotalityHealthOptions = stutotalityHealthOptionService.findByUnitIdAndGradeCode(unitId,grade.getGradeCode());
        if(CollectionUtils.isNotEmpty(stutotalityHealthOptions)){
            Set<String> healthIds = stutotalityHealthOptions.stream().map(StutotalityHealthOption::getHealthId).collect(Collectors.toSet());
            Set<String> optionIds = stutotalityHealthOptions.stream().map(StutotalityHealthOption::getId).collect(Collectors.toSet());
            Map<String,StutotalityStuResult> stuResultMap = new HashMap<>();
            List<StutotalityHealth> healthList = stutotalityHealthService.findListByIds(healthIds.toArray(new String[0]));
            Map<String,StutotalityHealth> healthMap = healthList.stream().collect(Collectors.toMap(StutotalityHealth::getId,Function.identity()));
            List<StutotalityStuResult> stuResults = stutotalityStuResultService.findListByOptionIdsWithMaster(semester.getAcadyear(),semester.getSemester().toString(),new String[]{studentId},optionIds.toArray(new String[0]),Type3);
            if(CollectionUtils.isNotEmpty(stuResults)) {
                stuResultMap = stuResults.stream().collect(Collectors.toMap(StutotalityStuResult::getItemHealthId, Function.identity()));
            }
            for(int i = stutotalityHealthOptions.size() - 1; i >= 0; i--){
                StutotalityHealthOption stutotalityHealthOption = stutotalityHealthOptions.get(i);
                stutotalityHealthOption.setResultId(UuidUtils.generateUuid());
                StutotalityHealth stutotalityHealth = healthMap.get(stutotalityHealthOption.getHealthId());
                if(stutotalityHealth!=null){
                    stutotalityHealthOption.setHealthName(stutotalityHealth.getHealthName());
                }else {
                    stutotalityHealthOptions.remove(stutotalityHealthOption);
                    continue;
                }
                if(MapUtils.isNotEmpty(stuResultMap)){
                    StutotalityStuResult stutotalityStuResult = stuResultMap.get(stutotalityHealthOption.getHealthId());
                    if(stutotalityStuResult!=null){
                        stutotalityHealthOption.setResultId(stutotalityStuResult.getId());
                        if("视力".equals(stutotalityHealthOption.getHealthName())) {//视力专门定制
                            if(StringUtils.isNotBlank(stutotalityStuResult.getHealthResult())){
                                String[] ss=stutotalityStuResult.getHealthResult().split("_");
                                stutotalityHealthOption.setResult(ss[0]);
                                if(ss.length>1){
                                    stutotalityHealthOption.setResult2(ss[1]);
                                }
                            }
                        }else{
                            stutotalityHealthOption.setResult(stutotalityStuResult.getHealthResult());
                        }
                    }
                }
            }
        }
        map.put("stutotalityHealthOptions",stutotalityHealthOptions);
        map.put("studentId",studentId);
        map.put("gradeId",grade.getId());
        return "/stutotality/result/stutotalityHeathList.ftl";
    }
    @RequestMapping("/saveResultByHealth")
    @ResponseBody
    public String saveResultByHealth(StutotalityHealth stutotalityHealth){
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        boolean isAdmin = isAdmin(unitId,userId);
        if(!isAdmin){
            String studentId = stutotalityHealth.getStudentId();
            Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId,semester.getAcadyear(), semester.getSemester()+"", getLoginInfo().getOwnerId()), new TR<List<ClassTeaching>>() {});
            Set<String> classIds = EntityUtils.getSet(classTeachingList,ClassTeaching::getClassId);
            if(!classIds.contains(student.getClassId())){
                return returnSuccess("-1","非班主任或管理员无权限保存");
            }
        }
        List<StutotalityHealthOption> healthOptions= stutotalityHealth.getHealthOptions();
        Set<String> resultIds = healthOptions.stream().map(StutotalityHealthOption::getResultId).collect(Collectors.toSet());
        //对比数据库中是否存在的result
        List<StutotalityStuResult> results = stutotalityStuResultService.findListByIdsWithMaster(resultIds.toArray(new String[0]));

        Map<String,StutotalityStuResult> resultHashMap = results.stream().collect(Collectors.toMap(StutotalityStuResult::getId,Function.identity()));
        //需要保存的resultlist
        List<StutotalityStuResult> stutotalityStuResults = new ArrayList<>();
        for(StutotalityHealthOption stutotalityHealthOption:healthOptions){
//            if(StringUtils.isBlank(stutotalityHealthOption.getResult()) && StringUtils.isBlank(stutotalityHealthOption.getResult2())){//不保存空的身体素质结果
//                continue;
//            }
            if("视力".equals(stutotalityHealthOption.getHealthName())){//视力专门定制
                if(StringUtils.isBlank(stutotalityHealthOption.getResult()) ){
                    if(stutotalityHealthOption.getResult().contains("_")){
                        return returnError("-1","视力定制左右，不能维护_符号！");
                    }
                    stutotalityHealthOption.setResult("");
                }
                if(StringUtils.isBlank(stutotalityHealthOption.getResult2())){
                    if(stutotalityHealthOption.getResult2().contains("_")){
                        return returnError("-1","视力定制左右，不能维护_符号！");
                    }
                    stutotalityHealthOption.setResult2("");
                }
                if(StringUtils.isNotBlank(stutotalityHealthOption.getResult())&&StringUtils.isNotBlank(stutotalityHealthOption.getResult2())) {
                    stutotalityHealthOption.setResult(stutotalityHealthOption.getResult() + "_" + stutotalityHealthOption.getResult2());
                }
            }
            StutotalityStuResult stutotalityStuResult= new StutotalityStuResult();
            if(resultHashMap.containsKey(stutotalityHealthOption.getResultId())){
                stutotalityStuResult =resultHashMap.get(stutotalityHealthOption.getResultId());
                stutotalityStuResult.setHealthResult(stutotalityHealthOption.getResult());
                stutotalityStuResult.setModifyTime(new Date());
                stutotalityStuResults.add(stutotalityStuResult);
            }else {
                stutotalityStuResult.setId(UuidUtils.generateUuid());
                stutotalityStuResult.setUnitId(unitId);
                stutotalityStuResult.setAcadyear(semester.getAcadyear());
                stutotalityStuResult.setSemester(semester.getSemester().toString());
                stutotalityStuResult.setStudentId(stutotalityHealth.getStudentId());
                stutotalityStuResult.setItemHealthId(stutotalityHealthOption.getHealthId());
                stutotalityStuResult.setOptionId(stutotalityHealthOption.getId());
                stutotalityStuResult.setHealthResult(stutotalityHealthOption.getResult());
                stutotalityStuResult.setType(Type3);
                stutotalityStuResult.setCreationTime(new Date());
                stutotalityStuResult.setModifyTime(new Date());
                stutotalityStuResults.add(stutotalityStuResult);
            }
        }
        try {
            stutotalityStuResultService.saveAll(stutotalityStuResults.toArray(new StutotalityStuResult[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/rewardIndex/page")
    @ControllerInfo("学生获奖")
    public String getRewardIndex(ModelMap map){
        LoginInfo info = getLoginInfo();
        findClazzListBy(info.getUnitId(),info.getUserId(),info.getOwnerId(),false,map);
        return "/stutotality/result/stutotalityRewardIndex.ftl";
    }

    @RequestMapping("/getStuRewardList")
    public String getStuRewardList(String studentId,ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()),Clazz.class);
        //List<StutotalityHealth> stutotalityHealths = stutotalityHealthService.findListBy(new String[]{"unitId","acadyear","semester","gradeId"},new String[]{unitId,semester.getAcadyear(),semester.getSemester().toString(),gradeId});
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()),Grade.class);
        List<StutotalityReward> stutotalityRewards = stutotalityRewardService.findByAcadyearAndSemesterAndUnitIdAndGradeId(semester.getAcadyear(),semester.getSemester().toString(),unitId,BaseConstants.ZERO_GUID);
        List<StutotalityStuReward> stutotalityStuRewards = stutotalityStuRewardService.getByAcadyearAndSemesterAndUnitIdAndStudentIdWithMaster(semester.getAcadyear(),semester.getSemester().toString(),unitId,studentId);
        if(CollectionUtils.isNotEmpty(stutotalityStuRewards)) {
            Set<String> rewardIds = stutotalityStuRewards.stream().map(StutotalityStuReward::getRewardId).collect(Collectors.toSet());
            List<StutotalityReward> stutotalityRewards1 = stutotalityRewardService.findListByIds(rewardIds.toArray(new String[0]));
            Map<String,StutotalityReward> rewardMap = stutotalityRewards1.stream().collect(Collectors.toMap(StutotalityReward::getId, Function.identity()));
            for (StutotalityStuReward stutotalityStuReward : stutotalityStuRewards) {
                StutotalityReward stutotalityReward2 = rewardMap.get(stutotalityStuReward.getRewardId());
                if(stutotalityReward2!=null){
                    stutotalityStuReward.setRewardName(stutotalityReward2.getRewardName());
                    stutotalityStuReward.setStarNum(stutotalityReward2.getStarNumber());
                }
                String mark = stutotalityStuReward.getDescription();
                if(StringUtils.isNotBlank(mark)&&mark.length()>10){
                    mark = mark.substring(0,10)+"...";
                    stutotalityStuReward.setDescription(mark);
                }
                stutotalityStuReward.setCreationTimeStr(DateUtils.date2String(stutotalityStuReward.getCreationTime(), "yyyy-MM-dd"));
            }
        }

        map.put("stutotalityRewards",stutotalityRewards);
        map.put("stutotalityStuRewards",stutotalityStuRewards);
        map.put("studentId",studentId);
        map.put("gradeId",grade.getId());
        return "/stutotality/result/stutotalityRewardList.ftl";
    }

    @RequestMapping("/saveStuReward")
    @ResponseBody
    public String saveStuReward(String rewardId,String remark,String studentId,String id){
        String unitId= getLoginInfo().getUnitId();
        StutotalityStuReward stutotalityStuReward = null;
        if(StringUtils.isNotBlank(id)){
            stutotalityStuReward =  stutotalityStuRewardService.findOne(id);
            stutotalityStuReward.setDescription(remark);
            stutotalityStuReward.setRewardId(rewardId);
        }else {
            StutotalityReward stutotalityReward = stutotalityRewardService.findOneWithMaster(rewardId);
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
            stutotalityStuReward = new StutotalityStuReward();
            stutotalityStuReward.setId(UuidUtils.generateUuid());
            stutotalityStuReward.setCreationTime(new Date());
            stutotalityStuReward.setAcadyear(semester.getAcadyear());
            stutotalityStuReward.setSemester(semester.getSemester().toString());
            stutotalityStuReward.setStudentId(studentId);
            stutotalityStuReward.setUnitId(unitId);
            stutotalityStuReward.setRewardId(rewardId);
            if(stutotalityReward!=null) {
                stutotalityStuReward.setStarNum(stutotalityReward.getStarNumber());
            }
            stutotalityStuReward.setDescription(remark);
        }
        try {
            stutotalityStuRewardService.save(stutotalityStuReward);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/delStuReward")
    @ResponseBody
    public String delStuReward(String rewardId){
        try {
            stutotalityStuRewardService.delete(rewardId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/acadIndex/page")
    @ControllerInfo("教师评语")
    public String getAcadIndex(ModelMap map,String gradeId){
        LoginInfo info = getLoginInfo();
        findClazzListBy(info.getUnitId(),info.getUserId(),info.getOwnerId(),false,map);
        return "/stutotality/result/stutotalityAcadIndex.ftl";
//        return  getClassList(map,gradeId,SearchType2);
    }
    @RequestMapping("/saveStuAcad")
    @ResponseBody
    public String saveStuAcad(String id,String teacContent,String myContent,String parentContent,String studentId){
        String unitId= getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<StutotalityStuFinal> stutotalityStuFinals = stutotalityStuFinalService.findByAcadyearAndSemesterAndUnitIdAndStudentIdsWithMaster
                (semester.getAcadyear(),semester.getSemester().toString(),unitId,new String[]{studentId});
        StutotalityStuFinal stutotalityStuFinal = new StutotalityStuFinal();
        if(CollectionUtils.isNotEmpty(stutotalityStuFinals)){
            stutotalityStuFinal = stutotalityStuFinals.get(0);
        }
        if(StringUtils.isBlank(stutotalityStuFinal.getId())) {
            stutotalityStuFinal = new StutotalityStuFinal();
            stutotalityStuFinal.setId(UuidUtils.generateUuid());
            stutotalityStuFinal.setTeacherContent(teacContent);
            stutotalityStuFinal.setFamilyContent(parentContent);
            stutotalityStuFinal.setStudentContent(myContent);
//            stutotalityStuFinal.set
            stutotalityStuFinal.setStudentId(studentId);
            stutotalityStuFinal.setCreationTime(new Date());
            stutotalityStuFinal.setUnitId(unitId);
            stutotalityStuFinal.setAcadyear(semester.getAcadyear());
            stutotalityStuFinal.setSemester(semester.getSemester().toString());
        }else {
            stutotalityStuFinal.setModifyTime(new Date());
            stutotalityStuFinal.setTeacherContent(teacContent);
            stutotalityStuFinal.setFamilyContent(parentContent);
            stutotalityStuFinal.setStudentContent(myContent);
        }
        try {
            stutotalityStuFinalService.save(stutotalityStuFinal);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/getStuAcadList")
    public String getStuAcadList(String studentId,ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<StutotalityStuFinal> stutotalityStuFinals = stutotalityStuFinalService.findByAcadyearAndSemesterAndUnitIdAndStudentIdsWithMaster
                (semester.getAcadyear(),semester.getSemester().toString(),unitId,new String[]{studentId});
        if(CollectionUtils.isNotEmpty(stutotalityStuFinals)){
            map.put("finalId",stutotalityStuFinals.get(0).getId());
            map.put("teacContent",stutotalityStuFinals.get(0).getTeacherContent());
            map.put("parentContent",stutotalityStuFinals.get(0).getFamilyContent());
            map.put("myContent",stutotalityStuFinals.get(0).getStudentContent());
        }else {
            map.put("finalId",UuidUtils.generateUuid());
            map.put("teacContent","");
        }
        boolean isAdmin = isAdmin(unitId, userId);
        map.put("isAdmin", isAdmin);
        map.put("studentId",studentId);
        return "/stutotality/result/stutotalityAcadList.ftl";
    }
    @RequestMapping("/studyIndex/page")
    @ControllerInfo("出勤记录")
    public String getStuAttendanceRecordIndex(ModelMap map,String gradeId, String classId){
        String unitId = getLoginInfo().getUnitId();
        String userId = getLoginInfo().getUserId();
        User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
        boolean isAdmin = isAdmin(unitId, userId);
        List<Grade> gradeList = new ArrayList<>();
        Map<String,List<Clazz>> stringListMap = new HashMap<>();
        if(isAdmin){
            gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),new TR<List<Grade>>(){});
        }else {
            String ownId = user.getOwnerId();
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByTeacherId(ownId), new TR<List<Clazz>>() {
            });
            if(CollectionUtils.isNotEmpty(clazzsList)) {
                Set<String> classIds = clazzsList.stream().map(Clazz::getGradeId).collect(Collectors.toSet());
                gradeList = SUtils.dt(gradeRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Grade>>() {
                });
            }
        }
        gradeList = gradeList.stream().filter(grade -> grade.getSection()==1).collect(Collectors.toList());
        if(StringUtils.isNotBlank(gradeId)){
            map.put("gradeId",gradeId);
            map.put("classId",classId);
        }
        map.put("gradeList",gradeList);
        return "/stutotality/result/stuAttendanceRecordIndex.ftl";
    }
    @RequestMapping("/getClassesByGradeId")
    @ResponseBody
    public String getClassesByGradeId(String gradeId){
        JSONObject jsonObject = new JSONObject();
        LoginInfo login=getLoginInfo();
        if(isAdmin(login.getUnitId(),login.getUserId())){
            List<Clazz> clazzes = SUtils.dt(classRemoteService.findByInGradeIds(new String[]{gradeId}),new TR<List<Clazz>>(){});
            jsonObject.put("classList",clazzes);
        }else{
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findByTeacherId(login.getOwnerId()), new TR<List<Clazz>>() {});
            List<Clazz> lastList =new ArrayList<>();
            if(CollectionUtils.isNotEmpty(clazzList)){
                for(Clazz clazz:clazzList){
                    if(StringUtils.equals(gradeId,clazz.getGradeId())){
                        lastList.add(clazz);
                    }
                }
            }
            jsonObject.put("classList",lastList);
        }

        return jsonObject.toJSONString();
    }
    @RequestMapping("/getStuAttendanceRecordList")
    @ControllerInfo("出勤记录列表")
    public String getStuAttendanceRecordList(String classId,ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        List<StudentDto> list = new ArrayList<>();
        if(StringUtils.isBlank(classId)){
            map.put("studentDto",list);
            return "/stutotality/result/stuAttendanceRecordList.ftl";
        }
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
        if(CollectionUtils.isNotEmpty(studentList)) {
            Set<String> studentIds=EntityUtils.getSet(studentList,Student::getId);
            //获取原数据结果
            List<StutotalityStuFinal> stutotalityStuFinals = stutotalityStuFinalService.findByAcadyearAndSemesterAndUnitIdAndStudentIdsWithMaster
                    (semester.getAcadyear(),semester.getSemester().toString(),unitId,studentIds.toArray(new String[0]));

            Map<String,StutotalityStuFinal> stuFinalMap =EntityUtils.getMap(stutotalityStuFinals,StutotalityStuFinal::getStudentId);
            for (Student student : studentList) {
                StudentDto studentDto = new StudentDto();
                studentDto.setId(student.getId());
                studentDto.setStudentName(student.getStudentName());
                studentDto.setSex(student.getSex());
                studentDto.setStudentCode(student.getStudentCode());
                if(stuFinalMap.containsKey(student.getId())){
                    StutotalityStuFinal stutotalityStuFinal =stuFinalMap.get(student.getId());
                    studentDto.setFinalId(stutotalityStuFinal.getId());
                    studentDto.setCasualLeave(stutotalityStuFinal.getCasualLeave());
                    studentDto.setSickLeave(stutotalityStuFinal.getSickLeave());
                    studentDto.setOtherLeave(stutotalityStuFinal.getOtherLeave());
                }
                list.add(studentDto);
            }
        }
        map.put("studentDto",list);
        return "/stutotality/result/stuAttendanceRecordList.ftl";
    }
    @RequestMapping("/saveAttendanceRecord")
    @ControllerInfo("保存出勤记录")
    @ResponseBody
    public String saveAttendanceRecord(String finalId,String studentId,Float casualLeave,Float sickLeave,Float otherLeave){
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        String unitId = getLoginInfo().getUnitId();
        try {
            if(StringUtils.isNotBlank(finalId)){
                StutotalityStuFinal stutotalityStuFinal=stutotalityStuFinalService.findOne(finalId);
                stutotalityStuFinal.setCasualLeave(casualLeave);
                stutotalityStuFinal.setSickLeave(sickLeave);
                stutotalityStuFinal.setOtherLeave(otherLeave);
                stutotalityStuFinal.setModifyTime(new Date());
                stutotalityStuFinalService.save(stutotalityStuFinal);
            }else {
                StutotalityStuFinal stutotalityStuFinal = new StutotalityStuFinal();
                stutotalityStuFinal.setId(UuidUtils.generateUuid());
                stutotalityStuFinal.setCasualLeave(casualLeave);
                stutotalityStuFinal.setSickLeave(sickLeave);
                stutotalityStuFinal.setOtherLeave(otherLeave);
                stutotalityStuFinal.setStudentId(studentId);
                stutotalityStuFinal.setCreationTime(new Date());
                stutotalityStuFinal.setModifyTime(new Date());
                stutotalityStuFinal.setUnitId(unitId);
                stutotalityStuFinal.setAcadyear(semester.getAcadyear());
                stutotalityStuFinal.setSemester(semester.getSemester().toString());
                stutotalityStuFinalService.save(stutotalityStuFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/saveStutotalityStuFinal")
    @ResponseBody
    public String saveStutotalityStuFinal(StutotalityStuFinal stutotalityStuFinal){
        if (StringUtils.isNotBlank(stutotalityStuFinal.getId())) {
            stutotalityStuFinal.setModifyTime(new Date());
        } else {
            stutotalityStuFinal.setCreationTime(new Date());
        }
        try {
            stutotalityStuFinalService.save(stutotalityStuFinal);
        } catch (Exception e) {
            e.printStackTrace();
            returnError();
        }
        return returnSuccess();
    }


    public JsonArray getArrray(List<Student> stuList, Map<String,List<StutotalityStuResult>> resultListMap, int size, List<Clazz> clazzList){
        JsonArray array=new JsonArray();
        Map<String,Integer> numberMap=new HashMap<>();
        for(Student stu:stuList){
                    /*if(stu.getId().equals("70E1C2B4E49F4218BEFB73A88C78ACC3")){
                        System.err.println("");
                    }*/
            if(!resultListMap.containsKey(stu.getId()) || resultListMap.get(stu.getId()).size()<size){
                int number=numberMap.containsKey(stu.getClassId())?numberMap.get(stu.getClassId()):0;
                number++;
                numberMap.put(stu.getClassId(),number);
            }
        }
        JSONObject in=null;
        for(Clazz clazz:clazzList){
            in=new JSONObject();
            in.put("classId",clazz.getId());
            if(numberMap.containsKey(clazz.getId())){
                in.put("number",numberMap.get(clazz.getId())+"");
            }else{
                in.put("number","0");
            }
            array.add(in);
        }
        return array;
    }
    /**
     * 判断是否为教务管理员
     */
    private boolean isAdmin(String unitId, String userId) {
        boolean res = customRoleRemoteService.checkUserRole(unitId, StutotalityConstant.STUTOTALITY_SUBSYSTEM, StutotalityConstant.STUTOTALITY_MANAGE_CODE, userId);
        return res;
//        return true;
    }

    /**
     * 根据班级id 判断是否只有任课老师权限 且得到对应的教学科目
     * @param unitId
     * @param isAdmin
     * @param ownerId
     * @param classId
     * @return
     */
    public boolean checkTeaching(String unitId,Boolean isAdmin,String ownerId,String classId,Set<String> subjectIds){
        if (!isAdmin) {//非管理员的权限
            //获取班主任的班级
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByTeacherId(ownerId), new TR<List<Clazz>>() {});
            Set<String> classIds= EntityUtils.getSet(clazzsList, Clazz::getId);
            if(classIds.contains(classId)){//班主任
                return false;
            }else{
                Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
                List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId,semester.getAcadyear(), semester.getSemester()+"", ownerId), new TR<List<ClassTeaching>>() {});
                if(CollectionUtils.isNotEmpty(classTeachingList)){
                    for(ClassTeaching classTeaching:classTeachingList){
                        if(StringUtils.isBlank(classId)){
                            subjectIds.add(classTeaching.getSubjectId());
                        }else if(classTeaching.getClassId().equals(classId)){
                            subjectIds.add(classTeaching.getSubjectId());//该班的某些课程
                        }
                    }
                }
                return true;
            }

        }
        return false;
    }
    /**
     * 根据任课老师的所教科目获取对应项目
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeId
     * @param subjectIds
     * @return
     */
    public List<StutotalityItem> getTeachingItemList(String unitId,String acadyear,String semester,String gradeId,Set<String> subjectIds){
        //默认是学业水平的
        List<StutotalityItem> itemListType1 = stutotalityItemService.getItemListByParams(unitId, acadyear, semester, gradeId, 1);
        List<StutotalityItem> itemList=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(itemListType1)){
            for(StutotalityItem item:itemListType1){
                if(subjectIds.contains(item.getSubjectId())){
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }
    /**
     * 获取用户的班级以及年级
     * @param unitId
     * @param userId
     * @param ownerId
     * @param map
     * @return
     */
    public List<Clazz> findClazzListBy(String unitId,String userId,String ownerId,boolean haveTeaching,ModelMap map){
        boolean isAdmin = isAdmin(unitId, userId);//管理员权限
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,unitId), Semester.class);
        List<Grade> gradeList =null;
        Map<String,List<Clazz>> classListMap=null;
        List<Clazz> clazzList =null;
        if (!isAdmin) {//非管理员的权限
            //获取班主任的班级
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findByTeacherId(ownerId), new TR<List<Clazz>>() {});
            //对用户下所有的班级id 包括班主任的班级以及任课老师的班级
            Set<String> classIds= EntityUtils.getSet(clazzsList, Clazz::getId);
            if(haveTeaching){ //根据参数判断是否获取任课老师的班级
                List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingList(unitId,semester.getAcadyear(), semester.getSemester()+"", ownerId), new TR<List<ClassTeaching>>() {});
                classIds.addAll(EntityUtils.getSet(classTeachingList,ClassTeaching::getClassId));
            }
            //所有不重复的班级
            clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {});
            if(CollectionUtils.isNotEmpty(clazzList)){
                Set<String> gradeIds=EntityUtils.getSet(clazzList, Clazz::getGradeId);
                gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>(){});
                classListMap=clazzList.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
            }
        }else{
            //只取小学年级数据
            gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{1},semester.getAcadyear()), new TR<List<Grade>>(){});
            clazzList = SUtils.dt(classRemoteService.findByInGradeIds(EntityUtils.getSet(gradeList,Grade::getId).toArray(new String[0])), new TR<List<Clazz>>() {});
            if(CollectionUtils.isNotEmpty(clazzList)){
                classListMap=clazzList.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
            }
        }
        if(CollectionUtils.isNotEmpty(gradeList)){
            Collections.sort(gradeList, new Comparator<Grade>() {
                @Override
                public int compare(Grade o1, Grade o2) {
                    return o1.getGradeCode().compareTo(o2.getGradeCode());
                }
            });
        }
        map.put("gradeList",gradeList);
        map.put("classListMap",classListMap);
        map.put("isAdmin",isAdmin);
        if(clazzList==null){
            return new ArrayList<>();
        }
        return clazzList;
    }

    @RequestMapping("/findStuListByClassId")
    @ControllerInfo("通过班级id查学生列表")
    @ResponseBody
    public String findStuListByClassId(String classId,String searchParam,String haveTeaching, ModelMap map){

        JSONObject jsonObject = new JSONObject();

        List<Student> studentList = new ArrayList<>();
        if(StringUtils.isNotBlank(classId)) {
            studentList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>() {});
            jsonObject.put("studentList",studentList);
        }else {
            LoginInfo loginInfo=getLoginInfo();
            String unitId = loginInfo.getUnitId();
            String userId = loginInfo.getUserId();
            String ownerId = loginInfo.getOwnerId();
            //获取对应权限的班级
            List<Clazz> classList =findClazzListBy(unitId,userId,ownerId,StringUtils.equals(haveTeaching,"1"),map);
            Set<String> classIds=EntityUtils.getSet(classList, Clazz::getId);
            if(CollectionUtils.isNotEmpty(classIds)) {
                studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>() {});
            }
            if(StringUtils.isNotBlank(searchParam)){
                //模糊过滤
                List<Student> lastList=studentList.stream().filter(Student -> Student.getStudentName().contains(searchParam.trim())).collect(Collectors.toList());
                Map<String,Clazz> clazzMap = EntityUtils.getMap(classList,Clazz::getId);
                if (CollectionUtils.isNotEmpty(lastList)) {
                    for(Student student:lastList){
                        if(clazzMap.containsKey(student.getClassId())) {
                            student.setStudentName(student.getStudentName()+"("+clazzMap.get(student.getClassId()).getClassNameDynamic()+")");
                        }
                    }
                }
                jsonObject.put("studentList",lastList);
            }
        }
        return jsonObject.toJSONString();
    }
    @RequestMapping("/getItemOptionsByItemId")
    @ResponseBody
    public String getItemOptionsByItemId(String itemId){
        List<StutotalityItemOption> list = stutotalityItemOptionService.findByItemIds(new String[]{itemId});
        JSONObject jsonObject = new JSONObject();
        if(CollectionUtils.isNotEmpty(list)){
            List<StutotalityOptionDesc> optionDescs = stutotalityOptionDescService.findListByOptionIds(new String[]{list.get(0).getId()});
            if(CollectionUtils.isNotEmpty(optionDescs)) {
                jsonObject.put("optionDes", optionDescs.get(0).getDescription());
            }
        }
        jsonObject.put("itemOptionList",list);
        return jsonObject.toJSONString();
    }
}