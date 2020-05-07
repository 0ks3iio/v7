package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.DiathesisCustomAuthor;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
import net.zdsoft.diathesis.data.entity.DiathesisScoreType;
import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/6/3 13:01
 */
@RestController
@RequestMapping("/diathesis/unitQuery")
public class DiathesisUnitQueryAction extends BaseAction {
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private DiathesisUnitService diathesisUnitService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private DiathesisScoreTypeService diathesisScoreTypeService;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private DiathesisMutualGroupService diathesisMutualGroupService;
    @Autowired
    private DiathesisMutualGroupStuService diathesisMutualGroupStuService;
    @Autowired
    DiathesisCustomAuthorService diathesisCustomAuthorService;

    /**
     * 查找子单位(不含自己)
     *
     * @return
     */
    @GetMapping("/findUnitList")
    public String findUnitList() {

        List<Unit> list = diathesisUnitService.findAllChildUnit(getLoginInfo().getUnitId());
        if (list == null || list.isEmpty()) return JSON.toJSONString(new ArrayList<DiathesisTreeDto>());
        List<DiathesisTreeDto> treeList = list.stream().map(x -> {
            DiathesisTreeDto dto = new DiathesisTreeDto();
            dto.setId(x.getId());
            dto.setpId(x.getParentId());
            dto.setName(x.getUnitName());
            dto.setType("" + x.getUnitClass());
            return dto;
        }).collect(Collectors.toList());
        List<DiathesisTreeDto> result = build(treeList, getLoginInfo().getUnitId());
        return JSON.toJSONString(result);
    }


    //根据学校id获得该学校下的班级
    @GetMapping("/findClassBySchoolId")
    public String findClassBySchoolId(String schoolId) {
        if (StringUtils.isBlank(schoolId)) return error("schoolId不能为空");
        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolId(schoolId), Clazz.class);
        if(CollectionUtils.isNotEmpty(classList)){
            classList=EntityUtils.filter2(classList,x->Clazz.NOT_GRADUATED==x.getIsGraduate());
        }
        List<Student> studentList = SUtils.dt(studentRemoteService.findBySchoolId(schoolId), Student.class);
        Map<String, List<DiathesisStudentDto>> studentMap = studentList.stream().collect(Collectors.groupingBy(x -> x.getClassId(), Collectors.mapping(x -> {
            DiathesisStudentDto dto = new DiathesisStudentDto();
            dto.setStudentId(x.getId());
            dto.setStudentName(x.getStudentName());
            dto.setStudentCode(x.getStudentCode());
            return dto;
        }, Collectors.toList())));
        List<DiathesisClassDto> classResult = classList.stream().sorted((y, x) -> x.getAcadyear().compareTo(y.getAcadyear())).map(x -> {
            DiathesisClassDto dto = new DiathesisClassDto();
            dto.setClassId(x.getId());
            dto.setClassName(x.getClassNameDynamic());
            dto.setStudentList(studentMap.get(x.getId()));
            return dto;
        }).collect(Collectors.toList());
        return JSON.toJSONString(classResult);
    }

    /**
     * 根据classId获得学生
     *
     * @param classId
     * @return
     */
    @GetMapping("/findStudentByClassId")
    public String findStudentByClassId(String classId) {
        if (StringUtils.isBlank(classId)) return error("classId不能为空");
        List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(classId), Student.class);
        List<DiathesisStudentDto> result = EntityUtils.getList(stuList, x -> {
            DiathesisStudentDto dto = new DiathesisStudentDto();
            dto.setStudentId(x.getId());
            dto.setStudentName(x.getStudentName());
            dto.setStudentCode(x.getStudentCode());
            return dto;
        });
        return JSON.toJSONString(result);
    }

    /**
     * 模糊查询学生名字
     *
     * @param studentName
     * @return
     */
    @GetMapping("findByStudentName")
    public String findByStudentName(String studentName) {
        //过滤schoolId 为教育局下的学生
        if (StringUtils.isBlank(studentName)) return error("参数不能为空");
        String schoolId = getLoginInfo().getUnitId();
        List<Student> stuList = SUtils.dt(
                studentRemoteService.findBySchoolIdIn(studentName, new String[]{schoolId}), Student.class);
        List<DiathesisStudentDto> result = EntityUtils.getList(stuList, x -> {
            DiathesisStudentDto dto = new DiathesisStudentDto();
            dto.setStudentId(x.getId());
            dto.setStudentName(x.getStudentName());
            dto.setStudentCode(x.getStudentCode());
            return dto;
        });
        return JSON.toJSONString(result);
    }

    //综合素质分统计  分页查询
    @RequestMapping("/comprehenStatistics")
    public String comprehenStatistics(HttpServletRequest request, String acadyear, Integer semester, String gradeCode, String schoolName) {
        if (Unit.UNIT_CLASS_EDU != getLoginInfo().getUnitClass()) return error("教育局专用统计接口!");
        if (StringUtils.isBlank(acadyear) || semester == null || StringUtils.isBlank(gradeCode)) {
            return error("参数缺失");
        }
        String unitId = getLoginInfo().getUnitId();

        Pagination page = createPagination(request);
        //todo 太耗时了
        long t1 = System.currentTimeMillis();
        List<Unit> unitListAll = diathesisUnitService.findAllChildUnit(unitId);
        long t2 = System.currentTimeMillis();
        if (StringUtils.isNotBlank(schoolName)) {
            unitListAll = EntityUtils.filter2(unitListAll, x -> x.getUnitName() != null && x.getUnitName().indexOf(schoolName) >= 0);
        }
        List<String> unitIds = EntityUtils.getList(unitListAll, x -> x.getId());
       // List<DiathesisCustomAuthor> authorList =new ArrayList<>();
        long t3 = System.currentTimeMillis();
        List<DiathesisCustomAuthor> authorList = findMoreThen900(unitIds, x -> diathesisCustomAuthorService.findAuthorListByUnitIdInAndTypeIn(x, new Integer[]{DiathesisConstant.AUTHOR_ADMIN, DiathesisConstant.AUTHOR_PROJECT_ALL}));
        long t4 = System.currentTimeMillis();

        Set<String> authorSet = EntityUtils.getSet(authorList, x -> x.getUnitId());

        List<DiathesisTreeDto> unitNodes = EntityUtils.getList(unitListAll, x -> {
            DiathesisTreeDto dto = new DiathesisTreeDto();
            dto.setId(x.getId());
            dto.setpId(x.getParentId());
            return dto;
        });
        List<DiathesisTreeDto> treeNodes = build(unitNodes, unitId);
        ArrayList<String> resultIds = new ArrayList<>();
        setResultIds(treeNodes, authorSet, resultIds);
        //分离出没有自主权限的学校 进行下一步统计
        // List<String> unitIds = unitIds;
        long t5 = System.currentTimeMillis();
        List<Unit> unitList = unitListAll.stream().filter(x -> resultIds.contains(x.getId()) && x.getUnitClass()==Unit.UNIT_CLASS_SCHOOL).collect(Collectors.toList());
        long t6 = System.currentTimeMillis();

        if (CollectionUtils.isEmpty(unitList)) {
            JSONObject json = new JSONObject();
            json.put("page", page);
            return json.toJSONString();
        }
        page.setMaxRowCount(unitList.size());
        int end = page.getPageIndex() * page.getPageSize();
        unitList = unitList.subList((page.getPageIndex() - 1) * page.getPageSize(), end > unitList.size() ? unitList.size() : end);
        page.initialize();
        unitIds = EntityUtils.getList(unitList, x -> x.getId());

        int pre = Integer.parseInt(acadyear.substring(0, 4)) - Integer.parseInt(gradeCode.substring(1, 2)) + 1;
        String openAcadyear = pre + "-" + (pre + 1);

        //todo 太耗时了
        long t7 = System.currentTimeMillis();
        List<Grade> gradeList = findMoreThen900(unitIds, x -> SUtils.dt(gradeRemoteService.findBySchoolIdsAndOpenAcaday(x.toArray(new String[0]), openAcadyear), Grade.class));
        long t8 = System.currentTimeMillis();

        if (CollectionUtils.isEmpty(gradeList)) {
            JSONObject json = new JSONObject();
            json.put("page", page);
            return json.toJSONString();
        }
        Map<String, String> gradeIdSchoolIdMap = EntityUtils.getMap(gradeList, x -> x.getId(), x -> x.getSchoolId());
        //todo 太耗时了
        long t9 = System.currentTimeMillis();
        Map<String,Integer> stuNumMap = SUtils.dc(studentRemoteService.countMapByGradeIds(EntityUtils.getArray(gradeList, x -> x.getId(), String[]::new)), Map.class);
        long t10 = System.currentTimeMillis();
        HashMap<String, Integer> studentCountMap = new HashMap<>();

        if(CollectionUtils.isEmpty(stuNumMap.keySet())){
            JSONObject json = new JSONObject();
            json.put("page", page);
            return json.toJSONString();
        }
        for (String s : stuNumMap.keySet()) {
            studentCountMap.put(gradeIdSchoolIdMap.get(s),stuNumMap.get(s));
        }


//        Iterator<Unit> it = unitList.iterator();
//        while (it.hasNext()) {
//            String id = it.next().getId();
//            if (studentCountMap.get(id) == null || studentCountMap.get(id) == 0) it.remove();
//        }


        DiathesisSet set = diathesisSetService.findByUnitId(unitId);
        String[] rankItems = set.getRankItems().split(",");
        String maxScore = rankItems[0];
        String minScore = rankItems[rankItems.length - 1];


        if (CollectionUtils.isEmpty(unitIds)) {
            JSONObject json = new JSONObject();
            json.put("page", page);
            return json.toJSONString();
        }
        List<DiathesisScoreType> scoreTypeList = findMoreThen900(unitIds, x -> diathesisScoreTypeService.findListByUnitIdsAndGradeCodeAndSemester(x, gradeCode, pre + "", semester));
       // List<DiathesisScoreType> scoreTypeList = diathesisScoreTypeService.findListByUnitIdsAndGradeCodeAndSemester(unitIds, gradeCode, pre + "", semester);
        List<String> scoreTypeIds = EntityUtils.getList(scoreTypeList,x->x.getId());

       // findMoreThen900(scoreTypeIds,x->findMoreThen900(scoreTypeIds, x -> diathesisScoreInfoService.findListByIn("scoreTypeId", x.toArray(new String[0]))))
        List<DiathesisScoreInfo> infoList = new ArrayList<>() ;
        if(CollectionUtils.isNotEmpty(scoreTypeIds)){
            infoList=findMoreThen900(scoreTypeIds, x -> diathesisScoreInfoService.findListByIn("scoreTypeId", scoreTypeIds.toArray(new String[0])));
        }
        //List<DiathesisScoreInfo> infoList = diathesisScoreInfoService.findListByIn("scoreTypeId", scoreTypeIds.toArray(new String[0]));
        // key : projectId    value: projectName
        //一级类目的id和name
        //Map<String, String> projectNameMap = diathesisProjectService.findListByIdIn(EntityUtils.getSet(infoList, x -> x.getObjId()).toArray(new String[0])).stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getProjectName()));
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        List<DiathesisIdAndNameDto> projectList = diathesisProjectService.findTopProjectByUnitId(usingUnitId);
        Map<String, String> projectNameMap =EntityUtils.getMap(projectList,x->x.getId(),x->x.getName());

        //计算全a 和 有d
        //key: schoolId  value: 不合格人数
        Map<String, Long> countDMap =new HashMap<>();
        Map<String, Long> hasScorePeople=new HashMap<>();
        Map<String, Long> countNotAMap =new HashMap<>();
        Map<String, Long> scoreMap =new HashMap<>();
        if(CollectionUtils.isNotEmpty(infoList)){
            countDMap=infoList.stream().filter(x -> x.getScore() != null && x.getScore().equalsIgnoreCase(minScore)).distinct().collect(Collectors.groupingBy(x -> x.getUnitId(), Collectors.counting()));
            //key: schoolId   value:有成绩的人
            hasScorePeople = infoList.stream().filter(x -> x.getScore() != null).distinct().collect(Collectors.groupingBy(x -> x.getUnitId(), Collectors.counting()));
            //key: schoolId  value: 不是全A的人数
            countNotAMap = infoList.stream().filter(x -> x.getScore() != null && !x.getScore().equalsIgnoreCase(maxScore)).distinct().collect(Collectors.groupingBy(x -> x.getUnitId(), Collectors.counting()));
            //key: unitId_projectName_score     value:count   每个得分的人数
            scoreMap = infoList.stream().collect(Collectors.groupingBy(x -> x.getUnitId() + "_" + projectNameMap.get(x.getObjId()) + "_" + x.getScore(), Collectors.counting()));
        }

        List<DiathesisComprehenSchoolStatisticsDto> result = new ArrayList<>();
        for (Unit x : unitList) {
            DiathesisComprehenSchoolStatisticsDto dto = new DiathesisComprehenSchoolStatisticsDto();
            dto.setSchoolId(x.getId());
            dto.setSchoolName(x.getUnitName());
            if (studentCountMap.get(x.getId()) == null) {
                dto.setStudentNum(0);
            } else {
                dto.setStudentNum(studentCountMap.get(x.getId()));
            }
            long hasScoreNum = hasScorePeople.get(x.getId()) == null ? 0L : hasScorePeople.get(x.getId());
            long notANum = countNotAMap.get(x.getId()) == null ? 0L : countNotAMap.get(x.getId());
            Integer countA = Math.toIntExact(hasScoreNum - notANum);
            if (CollectionUtils.isEmpty(infoList)) countA = 0;
            dto.setExcellentNum(countA);
            dto.setFailNum(countDMap.get(x.getId()) == null ? 0 : countDMap.get(x.getId()).intValue());
            //String[] scodeList={"A","B","C","D"};
            for (DiathesisIdAndNameDto d : projectList) {
                DiathesisProjectScoreDto p = new DiathesisProjectScoreDto();
                p.setName(d.getName());
                for (String s : rankItems) {
                    DiathesisScoreCountDto c = new DiathesisScoreCountDto();
                    c.setScore(s);
                    String key = x.getId() + "_" + d.getName() + "_" + s;
                    c.setCount(scoreMap.get(key) == null ? 0L : scoreMap.get(key));
                    p.getScoreList().add(c);
                }
                dto.getProjectScoreList().add(p);
            }
            result.add(dto);
        }

        JSONObject json = new JSONObject();
        json.put("unitList", result);
        json.put("page", page);
        long t11 = System.currentTimeMillis();
//        System.out.println("t1: "+t1);
//        System.out.println("t2: "+(t2-t1));
//        System.out.println("t3: "+(t3-t1));
//        System.out.println("t4: "+(t4-t1));
//        System.out.println("t5: "+(t5-t1));
//        System.out.println("t6: "+(t6-t1));
//        System.out.println("t7: "+(t7-t1));
//        System.out.println("t8: "+(t8-t1));
//        System.out.println("t9: "+(t9-t1));
//        System.out.println("t10: "+(t10-t1));
//        System.out.println("t11: "+(t11-t1));
        return json.toJSONString();
    }

    private <R> List<R> findMoreThen900(List<String> params, Function<List<String>,List<R>> function) {
        List<R> list=new ArrayList<>();
        if(CollectionUtils.isEmpty(params))return list;
        for (int i = 0; i < 900; i+=900) {
            List<String> temp = params.stream().skip(i).limit(900).collect(Collectors.toList());
            List<R> apply = function.apply(temp);
            if(CollectionUtils.isNotEmpty(apply))list.addAll(apply);
        }
        return list;
    }


    /**
     * 获得需要有效的id集合
     *
     * @param treeNodes
     * @param authorSet
     * @param resultIds
     */
    private void setResultIds(List<DiathesisTreeDto> treeNodes, Set<String> authorSet, ArrayList<String> resultIds) {
        for (DiathesisTreeDto treeNode : treeNodes) {
            if (!authorSet.contains(treeNode.getId())) {
                resultIds.add(treeNode.getId());
                if (CollectionUtils.isNotEmpty(treeNode.getChildList()))
                    setResultIds(treeNode.getChildList(), authorSet, resultIds);
            }
        }
    }

    /**
     * 封装成 tree结构
     *
     * @param unitNodes
     * @param topUnitId
     * @return
     */
    private List<DiathesisTreeDto> build(List<DiathesisTreeDto> unitNodes, String topUnitId) {
        List<DiathesisTreeDto> treeNodes = new ArrayList<>();
        for (DiathesisTreeDto node : unitNodes) {
            if (node.getpId().equals(topUnitId)) {
                treeNodes.add(node);
            }
            for (DiathesisTreeDto nodeChild : unitNodes) {
                if (!nodeChild.getpId().equals(node.getId())) continue;
                if (CollectionUtils.isEmpty(node.getChildList())) {
                    node.setChildList(new ArrayList<DiathesisTreeDto>());
                }
                node.getChildList().add(nodeChild);
            }

        }
        return treeNodes;
    }

}
