package net.zdsoft.scoremanage.data.service.impl;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.constant.HwConstants;
import net.zdsoft.scoremanage.data.dao.HwStatisDao;
import net.zdsoft.scoremanage.data.entity.HwPlan;
import net.zdsoft.scoremanage.data.entity.HwPlanSet;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;
import net.zdsoft.scoremanage.data.service.HwPlanService;
import net.zdsoft.scoremanage.data.service.HwPlanSetService;
import net.zdsoft.scoremanage.data.service.HwStatisExService;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author niuchao
 * @date 2019/11/5 11:32
 */
@Service("hwStatisService")
public class HwStatisServiceImpl extends BaseServiceImpl<HwStatis, String> implements HwStatisService {

    @Autowired
    private HwStatisDao hwStatisDao;
    @Autowired
    private HwStatisExService hwStatisExService;
    @Autowired
    private HwPlanService hwPlanService;
    @Autowired
    private HwPlanSetService hwPlanSetService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;

    @Override
    protected BaseJpaRepositoryDao<HwStatis, String> getJpaDao() {
        return hwStatisDao;
    }

    @Override
    protected Class<HwStatis> getEntityClass() {
        return HwStatis.class;
    }

    @Override
    public List<HwStatis> findListByPlanId(String unitId, String planId, String planType, boolean makeScore, Pagination page) {
        List<HwStatis> statisList = new ArrayList<>();
        if (page == null) {
            statisList = hwStatisDao.findByUnitIdAndHwPlanIdAndPlanTypeOrderByClaCodeAscStuCodeAsc(unitId, planId, planType);
        } else {
            int count = hwStatisDao.countByUnitIdAndHwPlanIdAndPlanType(unitId, planId, planType);
            page.setMaxRowCount(count);
            statisList = hwStatisDao.findByUnitIdAndHwPlanIdAndPlanTypeOrderByClaCodeAscStuCodeAsc(unitId, planId, planType, Pagination.toPageable(page));
        }
        if (CollectionUtils.isNotEmpty(statisList) && makeScore) {
            List<String> statisIds = EntityUtils.getList(statisList, HwStatis::getId);
            List<HwStatisEx> exList = hwStatisExService.findListByStatisIds(unitId, planId, planType, statisIds.toArray(new String[statisIds.size()]));
            Map<String, List<HwStatisEx>> exMap = exList.stream().collect(Collectors.groupingBy(HwStatisEx::getHwStatisId));
            statisList.forEach(e -> {
                if (exMap.containsKey(e.getId())) {
                    Map<String, List<HwStatisEx>> exListMap = exMap.get(e.getId()).stream().collect(Collectors.groupingBy(HwStatisEx::getObjectType));
                    if (exListMap.containsKey(HwConstants.OBJECT_TYPE_1)) {
                        e.setExList1(exListMap.get(HwConstants.OBJECT_TYPE_1));
                    }
                    if (exListMap.containsKey(HwConstants.OBJECT_TYPE_2)) {
                        e.setExList2(exListMap.get(HwConstants.OBJECT_TYPE_2));
                    }
                }
            });
        }
        return statisList;
    }

    @Override
    public List<HwStatis> findListByPlanIds(String unitId, String[] planIds, String planType) {
        List<HwStatis> statisList = hwStatisDao.findByUnitIdAndHwPlanIdInAndPlanType(unitId, planIds, planType);
        if (CollectionUtils.isNotEmpty(statisList)) {
            List<String> statisIds = EntityUtils.getList(statisList, HwStatis::getId);
            List<HwStatisEx> exList = hwStatisExService.findListByPlanIdsAndStatisIds(unitId, planIds, planType, statisIds.toArray(new String[statisIds.size()]));
            Map<String, List<HwStatisEx>> exMap = exList.stream().collect(Collectors.groupingBy(HwStatisEx::getHwStatisId));
            statisList.forEach(e -> {
                if (exMap.containsKey(e.getId())) {
                    Map<String, List<HwStatisEx>> exListMap = exMap.get(e.getId()).stream().collect(Collectors.groupingBy(HwStatisEx::getObjectType));
                    if (exListMap.containsKey(HwConstants.OBJECT_TYPE_1)) {
                        e.setExList1(exListMap.get(HwConstants.OBJECT_TYPE_1));
                    }
                    if (exListMap.containsKey(HwConstants.OBJECT_TYPE_2)) {
                        e.setExList2(exListMap.get(HwConstants.OBJECT_TYPE_2));
                    }
                }
            });
        }
        return statisList;
    }

    @Override
    public HwStatis findOneByPlanIdAndStudentId(String unitId, String planId, String planType, String studentId) {
        HwStatis statis = hwStatisDao.findByUnitIdAndHwPlanIdAndPlanTypeAndStudentId(unitId, planId, planType, studentId);
        if (statis != null) {
            List<HwStatisEx> exList = hwStatisExService.findListByStatisIds(unitId, planId, planType, new String[]{statis.getId()});
            if (CollectionUtils.isNotEmpty(exList)) {
                statis.setExList1(exList);
            }
        }
        return statis;
    }

    @Override
    public void saveStatisticGrade(String unitId, String gradeId) {
        List<HwPlan> planList = hwPlanService.findListByGradeId(unitId, gradeId);
        if (CollectionUtils.isEmpty(planList)) {
            throw new RuntimeException("该年级下没有任何方案!");
        }
        List<HwPlanSet> planSetList = hwPlanSetService.findListByPlanIdIn(EntityUtils.getList(planList, x -> x.getId()));
        if (CollectionUtils.isEmpty(planSetList)) {
            throw new RuntimeException("该年级没有设置汇总方案!");
        }

        //获得statistic 下面所有方案和学生信息
        //用gradeId 和 planIds 去查询  plan_type='01'
        List<String> planIds = planSetList.stream().map(x -> x.getHwPlanId()).distinct().collect(Collectors.toList());
        List<HwStatis> statisticList = hwStatisDao.findListByUnitIdAndHwPlanIdIn(unitId, planIds);
        List<HwStatisEx> statisticExList = hwStatisExService.findListByUnitIdAndPlanIdIn(unitId, planIds);
        if (CollectionUtils.isEmpty(statisticExList) || CollectionUtils.isEmpty(statisticList)) {
            throw new RuntimeException("该年级设置的方案下没有任何数据!");
        }

        //需要保存的2个list
        List<HwStatis> saveStatisticList = new ArrayList<>();
        List<HwStatisEx> saveStatisticExList = new ArrayList<>();

        //key 老id   value 新id
        HashMap<String, String> idMap = new HashMap<>(statisticList.size());
        //statistic 表复制
        for (HwStatis hwStatis : statisticList) {
            String id = UuidUtils.generateUuid();
            idMap.put(hwStatis.getId(), id);
            HwStatis statis = new HwStatis();
            EntityUtils.copyProperties(hwStatis, statis);
            statis.setId(id);
            statis.setPlanType(HwConstants.PLAN_TYPE_2);
            saveStatisticList.add(statis);
        }
        //科目类型map
        HashMap<String, String> subjectMap = new HashMap<>();
        //总分,排名等除了科目成绩以外的

        Map<String,String> exceptSubjectMap = new HashMap<>();
        HashSet<String> subjectTypeMap = new HashSet<>();
        for (HwPlanSet hwPlanSet : planSetList) {
            if (HwConstants.SUM_SCORE_RANK.equals(hwPlanSet.getObjKey())) {
                //总分,排名的设置
                exceptSubjectMap.put(hwPlanSet.getHwPlanId() ,hwPlanSet.getObjVal());
            } else {
                //科目的设置
                subjectMap.put(hwPlanSet.getHwPlanId() + "_" + hwPlanSet.getObjKey(), hwPlanSet.getObjVal());
                subjectTypeMap.add(hwPlanSet.getObjKey());
            }
        }
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectTypeMap.toArray(new String[0])), Course.class);
        List<String> selectSubjectList = Arrays.asList(HwConstants.SUBJECT_73);
        List<String> selectIds = courseList.stream().filter(x -> selectSubjectList.contains(x.getSubjectCode())).map(x -> x.getId()).collect(Collectors.toList());


        //statisEx 表复制

        for (HwStatisEx ex : statisticExList) {
            //若果是科目类型的
            if (HwConstants.OBJECT_TYPE_1.equals(ex.getObjectType())) {
                //如果是等第的格式,直接录入
                String originalScore = ex.getScore();
                if(StringUtils.isNotBlank(originalScore) && HwConstants.IS_LEVEL_PATTERN.matcher(ex.getScore().trim()).matches()){
                    HwStatisEx saveEntity = getNewHwStatisEx(ex, idMap, HwConstants.PLAN_TYPE_2);
                    saveEntity.setObjectVal(originalScore);
                    saveStatisticExList.add(saveEntity);
                    continue;
                }
                //科目类型的数据
                String key = ex.getHwPlanId() + "_" + ex.getObjectId();
                String subType = subjectMap.get(key);
                if (StringUtils.isNotBlank(subType)) {
                    HwStatisEx saveEntity = getNewHwStatisEx(ex, idMap, HwConstants.PLAN_TYPE_2);
                    if (HwConstants.SCORE_TYPE_EXAM.equals(subType)) {
                        //考试成绩类型
                        if (selectIds.contains(ex.getObjectId())) {
                            //选考
                            saveEntity.setObjectVal(StringUtils.defaultString(ex.getConvertScore(), ""));
                        } else {
                            //非选考
                            saveEntity.setObjectVal(StringUtils.defaultString(ex.getScore(), ""));
                        }
                    } else {
                        //总评类型
                        if (selectIds.contains(ex.getObjectId())) {
                            //选考
                            saveEntity.setObjectVal(StringUtils.defaultString(ex.getTotalConvertScore(), ""));
                        } else {
                            //非选考
                            saveEntity.setObjectVal(StringUtils.defaultString(ex.getTotalScore(), ""));
                        }
                    }
                    saveStatisticExList.add(saveEntity);
                }
            } else {
                //总分或排名类型的数据
                if (HwConstants.OBJECT_ID_7.equals(ex.getObjectId()) || HwConstants.OBJECT_ID_8.equals(ex.getObjectId())) {
                    //年级人数,和班级人数,不用过滤,直接保存
                    HwStatisEx saveEntity = getNewHwStatisEx(ex, idMap, HwConstants.PLAN_TYPE_2);
                    saveStatisticExList.add(saveEntity);
                } else {
                    //考试总分 排名 或者总评 总分 排名
//                    String key = ex.getHwPlanId() + "_";
//                    key += HwConstants.idNameMap2.keySet().contains(ex.getObjectId()) ? HwConstants.SCORE_TYPE_EXAM : HwConstants.SCORE_TYPE_GENERAL;
                    String inputType = exceptSubjectMap.get(ex.getHwPlanId());
                    if(HwConstants.SCORE_TYPE_EXAM.equals(inputType)){
                        //考试类型
                        if (HwConstants.idNameMap2.keySet().contains(ex.getObjectId())){
                            HwStatisEx saveEntity = getNewHwStatisEx(ex, idMap, HwConstants.PLAN_TYPE_2);
                            saveStatisticExList.add(saveEntity);
                        }
                    }else{
                        //总评类型
                        if (!HwConstants.idNameMap2.keySet().contains(ex.getObjectId())){
                            HwStatisEx saveEntity = getNewHwStatisEx(ex, idMap, HwConstants.PLAN_TYPE_2);
                            saveStatisticExList.add(saveEntity);
                        }
                    }
                }
            }
        }

        //先删除原先的数据
        hwStatisExService.deleteCollectByUnitIdAndGradeIdAndPlanType(unitId, gradeId,HwConstants.PLAN_TYPE_2);
        hwStatisExService.deleteByUnitIdAndHwPlanIdsAndPlanType(unitId,planIds,HwConstants.PLAN_TYPE_2);
        hwStatisDao.deleteCollectByUnitIdAndGradeIdAndPlanType(unitId, gradeId,HwConstants.PLAN_TYPE_2);
        hwStatisDao.deleteByUnitIdAndHwPlanIdsAndPlanType(unitId,planIds,HwConstants.PLAN_TYPE_2);

        if (CollectionUtils.isNotEmpty(saveStatisticList)) {
            hwStatisDao.saveAll(saveStatisticList);
        }

        if (CollectionUtils.isNotEmpty(saveStatisticExList)) {
            hwStatisExService.saveAll(saveStatisticExList.toArray(new HwStatisEx[0]));
        }
    }

    @Override
    public List<List<String>> getReportByStudentId(String unitId, String studentId) {
        //返回result
        List<List<String>> result = new ArrayList<>();
        List<HwStatis> statisList = findListByUnitIdAndPlanTypeAndStudentId(unitId, HwConstants.PLAN_TYPE_2, studentId);
        if (CollectionUtils.isEmpty(statisList)) {
            return result;
        }
        String[] planIds = EntityUtils.getSet(statisList, x -> x.getHwPlanId()).toArray(new String[0]);
        List<HwStatisEx> exList = hwStatisExService.findListByIn("hwStatisId", EntityUtils.getSet(statisList, x -> x.getId()).toArray(new String[0]));
        if (CollectionUtils.isEmpty(exList)) {
            return result;
        }
        List<HwPlan> planList = hwPlanService.findListByIdIn(planIds);
        planList.sort(Comparator.comparingLong(x -> x.getModifyTime().getTime()));
        List<String> planSortedIdList = EntityUtils.getList(planList, x -> x.getId());
        //标题行

        ArrayList<String> bigTile = new ArrayList<>();
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
        School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
        //默认设置为高中
        String sectionName = getSectionName(grade.getSection());
        bigTile.add(school.getSchoolName() + sectionName + "阶段成绩汇总");
        bigTile.add(school.getSchoolName());
        bigTile.add(sectionName);

        ArrayList<String> infoTitle = new ArrayList<>();
        if (student.getSex()==null){
            student.setSex(1);
        }
        infoTitle.add(String.format("姓名:%s  性别:%s  班级:%s  学号:%s", student.getStudentName(), student.getSex() == 1 ? "男" : "女", grade.getGradeName()+clazz.getClassName(), student.getStudentCode()));

        //endSchRow
        ArrayList<String> endSchRow = new ArrayList<>();
        endSchRow.add(school.getSchoolName() + "(盖章)");

        //endDateRow
        ArrayList<String> endDateRow = new ArrayList<>();
        endDateRow.add("      年     月     日");

        List<String> subTitle = EntityUtils.getList(planList, x -> x.getExamName());
        subTitle.add(0, "科目");
        result.add(bigTile);
        result.add(infoTitle);
        result.add(subTitle);

        //列    科目+总分+班级排名+年级排名+年级人数
        //所有科目
        TreeMap<String, String> subTreemap = new TreeMap<>();
        HashMap<String, String> exMap = new HashMap<>();
        for (HwStatisEx ex : exList) {
            String objectId = ex.getObjectId();
            String key = ex.getHwPlanId() + "_";
            if (HwConstants.OBJECT_TYPE_1.equals(ex.getObjectType())) {
                subTreemap.put(objectId, ex.getObjectName());
                key += objectId;
            } else {
                if (HwConstants.OBJECT_ID_1.equals(objectId) || HwConstants.OBJECT_ID_2.equals(objectId)) {
                    key += "totalScore";
                } else if (HwConstants.OBJECT_ID_3.equals(objectId) || HwConstants.OBJECT_ID_5.equals(objectId)) {
                    key += "classRank";
                } else if (HwConstants.OBJECT_ID_4.equals(objectId) || HwConstants.OBJECT_ID_6.equals(objectId)) {
                    key += "gradeRank";
                } else if (HwConstants.OBJECT_ID_7.equals(objectId)) {
                    key += "classStuNum";
                } else {
                    key += "gradeStuNum";
                }
            }
            exMap.put(key, ex.getObjectVal());
        }
        subTreemap.put("totalScore", "总分");
        subTreemap.put("classRank", "班级排名");
        subTreemap.put("classStuNum", "班级人数");
        subTreemap.put("gradeRank", "年级排名");
        subTreemap.put("gradeStuNum", "年级人数");

        for (String col : subTreemap.keySet()) {
            ArrayList<String> list = new ArrayList<>();
            //每行第一个名字
            list.add(subTreemap.get(col));
            for (String planId : planSortedIdList) {
                String key = planId + "_" + col;
                list.add(StringUtils.defaultString(exMap.get(key), ""));
            }
            result.add(list);
        }
        result.add(new ArrayList<>(endSchRow));
        result.add(new ArrayList<>(endDateRow));
        return result;
    }


    @Override
    public List<HwStatis> findListByUnitIdAndPlanTypeAndStudentId(String unitId, String planType, String studentId) {
        return hwStatisDao.findListByUnitIdAndPlanTypeAndStudentId(unitId, planType, studentId);
    }

    @Override
    public Map<String, List<List<String>>> getReportByClassId(String unitId, String classId) {
        Map<String, List<List<String>>> result = new LinkedHashMap<>();
        List<HwStatis> statisList = findListByUnitIdAndPlanTypeAndClassId(unitId, HwConstants.PLAN_TYPE_2, classId);
        if (CollectionUtils.isEmpty(statisList)) {
            return result;
        }
        String[] planIds = EntityUtils.getSet(statisList, x -> x.getHwPlanId()).toArray(new String[0]);
        List<HwStatisEx> exList = hwStatisExService.findListByIn("hwStatisId", EntityUtils.getSet(statisList, x -> x.getId()).toArray(new String[0]));
        if (CollectionUtils.isEmpty(exList)) {
            return result;
        }
        List<HwPlan> planList = hwPlanService.findListByIdIn(planIds);
        planList.sort(Comparator.comparingLong(x -> x.getModifyTime().getTime()));
        List<String> planSortedIdList = EntityUtils.getList(planList, x -> x.getId());

        //列    科目+总分+班级排名+年级排名+年级人数
        //所有科目
        TreeMap<String, String> subTreemap = new TreeMap<>();
        //key : stuId_hwPlanId_objectId
        HashMap<String, String> exMap = new HashMap<>();
        Map<String, String> stuIdMap = EntityUtils.getMap(statisList, x -> x.getId(), x -> x.getStudentId());
        Set<String> stuIdSet = EntityUtils.getSet(statisList, x -> x.getStudentId());
        for (HwStatisEx ex : exList) {
            String objectId = ex.getObjectId();
            String stuId = stuIdMap.get(ex.getHwStatisId());
            String key = stuId + "_" + ex.getHwPlanId() + "_";
            if (HwConstants.OBJECT_TYPE_1.equals(ex.getObjectType())) {
                subTreemap.put(objectId, ex.getObjectName());
                key += objectId;
            } else {
                if (HwConstants.OBJECT_ID_1.equals(objectId) || HwConstants.OBJECT_ID_2.equals(objectId)) {
                    key += "totalScore";
                } else if (HwConstants.OBJECT_ID_3.equals(objectId) || HwConstants.OBJECT_ID_5.equals(objectId)) {
                    key += "classRank";
                } else if (HwConstants.OBJECT_ID_4.equals(objectId) || HwConstants.OBJECT_ID_6.equals(objectId)) {
                    key += "gradeRank";
                } else if (HwConstants.OBJECT_ID_7.equals(objectId)) {
                    key += "classStuNum";
                } else {
                    key += "gradeStuNum";
                }
            }
            exMap.put(key, ex.getObjectVal());
        }
        subTreemap.put("totalScore", "总分");
        subTreemap.put("classRank", "班级排名");
        subTreemap.put("classStuNum", "班级人数");
        subTreemap.put("gradeRank", "年级排名");
        subTreemap.put("gradeStuNum", "年级人数");

        getSheetDataMap(unitId,classId,result,planList,planSortedIdList,subTreemap,exMap,stuIdSet);
        return result;
    }

    /**
     * 个人成绩 年级统计
     *
     * @param unitId
     * @param gradeId
     */
    @Override
    public void savePersonalGrade(String unitId, String gradeId) {
        List<HwPlan> planList = hwPlanService.findListByGradeId(unitId, gradeId);
        if (CollectionUtils.isEmpty(planList)) {
            throw new RuntimeException("该年级下没有任何方案!");
        }
        List<HwPlanSet> planSetList = hwPlanSetService.findListByPlanIdIn(EntityUtils.getList(planList, x -> x.getId()));
        if (CollectionUtils.isEmpty(planSetList)) {
            throw new RuntimeException("该年级没有设置汇总方案!");
        }

        //获得statistic 下面所有方案和学生信息
        //用gradeId 和 planIds 去查询  plan_type='01'
        List<String> planIds = planSetList.stream().map(x -> x.getHwPlanId()).distinct().collect(Collectors.toList());
        List<HwStatis> statisticList = hwStatisDao.findListByUnitIdAndHwPlanIdIn(unitId, planIds);
        List<HwStatisEx> statisticExList = hwStatisExService.findListByUnitIdAndPlanIdIn(unitId, planIds);
        if (CollectionUtils.isEmpty(statisticExList) || CollectionUtils.isEmpty(statisticList)) {
            throw new RuntimeException("该年级设置的方案下没有任何数据!");
        }

        //需要保存的2个list
        List<HwStatis> saveStatisticList = new ArrayList<>();
        List<HwStatisEx> saveStatisticExList = new ArrayList<>();

        //key 老id   value 新id
        HashMap<String, String> idMap = new HashMap<>();
        //statistic 表复制
        for (HwStatis hwStatis : statisticList) {
            String id = UuidUtils.generateUuid();
            idMap.put(hwStatis.getId(), id);
            HwStatis statis = new HwStatis();
            EntityUtils.copyProperties(hwStatis, statis);
            statis.setId(id);
            statis.setPlanType(HwConstants.PLAN_TYPE_3);
            saveStatisticList.add(statis);
        }
        //科目类型map
        HashMap<String, String> subjectMap = new HashMap<>();
        for (HwPlanSet hwPlanSet : planSetList) {
            if (!HwConstants.SUM_SCORE_RANK.equals(hwPlanSet.getObjKey())) {
                //科目的设置
                subjectMap.put(hwPlanSet.getHwPlanId() + "_" + hwPlanSet.getObjKey(), hwPlanSet.getObjVal());
            }
        }
        //statisEx 表复制
        for (HwStatisEx ex : statisticExList) {
            if (HwConstants.OBJECT_TYPE_1.equals(ex.getObjectType())) {

                //科目类型的数据
                String key = ex.getHwPlanId() + "_" + ex.getObjectId();
                String subType = subjectMap.get(key);
                if (StringUtils.isNotBlank(subType)) {
                    HwStatisEx saveEntity = getNewHwStatisEx(ex, idMap, HwConstants.PLAN_TYPE_3);
                    if (HwConstants.SCORE_TYPE_EXAM.equals(subType)) {
                        //考试成绩类型
                        saveEntity.setObjectVal(StringUtils.defaultString(ex.getScore(), ""));
                    } else {
                        //总评类型
                        //如果有补考,直接取补考成绩
                        if(StringUtils.isNotBlank(ex.getMakeScore())){
                            saveEntity.setObjectVal(ex.getMakeScore());
                        }else{
                            //如果是等第的格式,直接录入
                            String originalScore = ex.getScore();
                            if (StringUtils.isNotBlank(originalScore) && HwConstants.IS_LEVEL_PATTERN.matcher(ex.getScore().trim()).matches()) {
                                saveEntity.setObjectVal(originalScore);
                            }else{
                                saveEntity.setObjectVal(StringUtils.defaultString(ex.getTotalScore(), ""));
                            }
                        }
                    }
                    saveStatisticExList.add(saveEntity);
                }
            }
        }

        //先删除原先的数据  因为复学的gradeId和当前年级的grade不一致,所以,要再用hwPlanId删除一次
        hwStatisExService.deleteCollectByUnitIdAndGradeIdAndPlanType(unitId, gradeId,HwConstants.PLAN_TYPE_3);
        hwStatisExService.deleteByUnitIdAndHwPlanIdsAndPlanType(unitId,planIds,HwConstants.PLAN_TYPE_3);
        hwStatisDao.deleteCollectByUnitIdAndGradeIdAndPlanType(unitId, gradeId,HwConstants.PLAN_TYPE_3);
        hwStatisDao.deleteByUnitIdAndHwPlanIdsAndPlanType(unitId,planIds,HwConstants.PLAN_TYPE_3);

        if (CollectionUtils.isNotEmpty(saveStatisticList)) {
            hwStatisDao.saveAll(saveStatisticList);
        }
        if (CollectionUtils.isNotEmpty(saveStatisticExList)) {
            hwStatisExService.saveAll(saveStatisticExList.toArray(new HwStatisEx[0]));
        }
    }

    @Override
    public List<List<String>> getPersonalReportByStudentId(String unitId, String studentId) {
        //返回result
        List<List<String>> result = new ArrayList<>();
        List<HwStatis> statisList = findListByUnitIdAndPlanTypeAndStudentId(unitId, HwConstants.PLAN_TYPE_3, studentId);
        if (CollectionUtils.isEmpty(statisList)) {
            return result;
        }
        String[] planIds = EntityUtils.getSet(statisList, x -> x.getHwPlanId()).toArray(new String[0]);
        List<HwStatisEx> exList = hwStatisExService.findListByIn("hwStatisId", EntityUtils.getSet(statisList, x -> x.getId()).toArray(new String[0]));
        // List<HwStatisEx> exList=hwStatisExService.findListByUnitIdAndPlanIds(unitId,planIds);
        if (CollectionUtils.isEmpty(exList)){
            return result;
        }
        List<HwPlan> planList = hwPlanService.findListByIdIn(planIds);
        planList.sort(Comparator.comparingLong(x -> x.getModifyTime().getTime()));
        List<String> planSortedIdList = EntityUtils.getList(planList, x -> x.getId());
        //标题行

        ArrayList<String> bigTile = new ArrayList<>();
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
        School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
        String sectionName = getSectionName(grade.getSection());
        bigTile.add(school.getSchoolName() + sectionName + "阶段成绩汇总");
        bigTile.add(school.getSchoolName());
        bigTile.add(sectionName);

        ArrayList<String> infoTitle = new ArrayList<>();
        if (student.getSex()==null){
            student.setSex(1);
        }
        infoTitle.add(String.format("姓名:%s   性别:%s   班级:%s   学号:%s", student.getStudentName(), student.getSex() == 1 ? "男" : "女", grade.getGradeName()+ clazz.getClassName(), student.getStudentCode()));

        //endSchRow
        ArrayList<String> endSchRow = new ArrayList<>();
        endSchRow.add(school.getSchoolName() + " (盖章)");

        //endDateRow
        ArrayList<String> endDateRow = new ArrayList<>();
        endDateRow.add("    年   月   日");

        List<String> titleList = EntityUtils.getList(planList, x -> x.getExamName());
        titleList.add(0, "科目");
        result.add(bigTile);
        result.add(infoTitle);
        result.add(titleList);

        //列    科目+总分+班级排名+年级排名+年级人数
        //所有科目
        TreeMap<String, String> subTreemap = new TreeMap<>();
        HashMap<String, String> exMap = new HashMap<>();
        for (HwStatisEx ex : exList) {
            String objectId = ex.getObjectId();
            String key = ex.getHwPlanId() + "_";
            if (HwConstants.OBJECT_TYPE_1.equals(ex.getObjectType())) {
                subTreemap.put(objectId, ex.getObjectName());
                key += objectId;
            }
            exMap.put(key, ex.getObjectVal());
        }
        for (String col : subTreemap.keySet()) {
            ArrayList<String> list = new ArrayList<>();
            //每行第一个是科目名字
            list.add(subTreemap.get(col));
            for (String planId : planSortedIdList) {
                String key = planId + "_" + col;
                list.add(StringUtils.defaultString(exMap.get(key), ""));
            }
            result.add(list);
        }
        result.add(new ArrayList<>(endSchRow));
        result.add(new ArrayList<>(endDateRow));
        return result;
    }

    @Override
    public Map<String, List<List<String>>> getPersonalReportByClassId(String unitId, String classId) {
        Map<String, List<List<String>>> result = new LinkedHashMap<>();
        List<HwStatis> statisList = findListByUnitIdAndPlanTypeAndClassId(unitId, HwConstants.PLAN_TYPE_3, classId);
        if (CollectionUtils.isEmpty(statisList)) {
            return result;
        }
        String[] planIds = EntityUtils.getSet(statisList, x -> x.getHwPlanId()).toArray(new String[0]);
        List<HwStatisEx> exList = hwStatisExService.findListByIn("hwStatisId", EntityUtils.getSet(statisList, x -> x.getId()).toArray(new String[0]));
        if (CollectionUtils.isEmpty(exList)){
            return result;
        }
        List<HwPlan> planList = hwPlanService.findListByIdIn(planIds);
        planList.sort(Comparator.comparingLong(x -> x.getModifyTime().getTime()));
        List<String> planSortedIdList = EntityUtils.getList(planList, x -> x.getId());

        //列    科目+总分+班级排名+年级排名+年级人数
        //所有科目
        TreeMap<String, String> subTreemap = new TreeMap<>();
        //key : stuId_hwPlanId_objectId
        HashMap<String, String> exMap = new HashMap<>();
        Map<String, String> stuIdMap = EntityUtils.getMap(statisList, x -> x.getId(), x -> x.getStudentId());
        Set<String> stuIdSet = EntityUtils.getSet(statisList, x -> x.getStudentId());

        for (HwStatisEx ex : exList) {
            String objectId = ex.getObjectId();
            String stuId = stuIdMap.get(ex.getHwStatisId());
            String key = stuId + "_" + ex.getHwPlanId() + "_";
            if (HwConstants.OBJECT_TYPE_1.equals(ex.getObjectType())) {
                subTreemap.put(objectId, ex.getObjectName());
                key += objectId;
            }
            exMap.put(key, ex.getObjectVal());
        }


        getSheetDataMap(unitId, classId, result, planList, planSortedIdList, subTreemap,exMap,stuIdSet);

        return result;
    }

    @Override
    public Integer countDataByPlanType(String unitId, String planType, String classId, String studentId) {
        Integer count=0;
        if(StringUtils.isNotBlank(classId)){
            count=hwStatisDao.countDataByPlanTypeAndClassId(unitId,planType,classId);
        }else if(StringUtils.isNotBlank(studentId)){
            count=hwStatisDao.countDataByPlanTypeAndStuId(unitId,planType,studentId);
        }
        return count;
    }

    private void getSheetDataMap(String unitId,
                                 String classId,
                                 Map<String, List<List<String>>> result,
                                 List<HwPlan> planList,
                                 List<String> planSortedIdList,
                                 TreeMap<String, String> subTreemap,
                                 HashMap<String, String> exMap,
                                 Set<String> stuIdSet) {
        //标题行
        ArrayList<String> bigTile = new ArrayList<>();
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
        School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);
        String sectionName = getSectionName(grade.getSection());
        bigTile.add(school.getSchoolName() + sectionName + "阶段成绩汇总");
        bigTile.add(school.getSchoolName());
        bigTile.add(sectionName);

        //endSchRow
        ArrayList<String> endSchRow = new ArrayList<>();
        endSchRow.add(school.getSchoolName() + "(盖章)");

        //endDateRow
        ArrayList<String> endDateRow = new ArrayList<>();
        endDateRow.add("    年   月   日");

        List<String> titleList = EntityUtils.getList(planList, x -> x.getExamName());
        titleList.add(0, "科目");


        List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIdSet.toArray(new String[0])), Student.class);
        students.sort((x, y) -> {
            if (x.getStudentCode() == null){
                return -1;
            }
            if (y.getStudentCode() == null){
                return 1;
            }
            return x.getStudentCode().compareTo(y.getStudentCode());
        });
        for (Student student : students) {
            String stuName = student.getStudentName();
            String stuId = student.getId();
            List<List<String>> perStu = new ArrayList<>();
            perStu.add(new ArrayList<String>(bigTile));
            ArrayList<String> infoTitle = new ArrayList<>();
            if (student.getSex()==null){
                student.setSex(1);
            }
            infoTitle.add(String.format("姓名:%s   性别:%s   班级:%s   学号:%s", stuName, student.getSex() == 1 ? "男" : "女", grade.getGradeName()+ clazz.getClassName(), student.getStudentCode()));
            perStu.add(infoTitle);
            perStu.add(new ArrayList<String>(titleList));
            for (String col : subTreemap.keySet()) {
                ArrayList<String> list = new ArrayList<>();
                //每行第一个名字
                list.add(subTreemap.get(col));
                for (String planId : planSortedIdList) {
                    String key = stuId + "_" + planId + "_" + col;
                    list.add(StringUtils.defaultString(exMap.get(key), ""));
                }
                perStu.add(list);
            }
            //如果一个班级有重名
            if (result.keySet().contains(stuName)) {
                result.put(stuName + "(" + student.getStudentCode() + ")", perStu);
            } else {
                result.put(stuName, perStu);
            }
            perStu.add(new ArrayList<>(endSchRow));
            perStu.add(new ArrayList<>(endDateRow));
        }
    }

    private String getSectionName(Integer section) {
        String sectionName="";
        List<McodeDetail> mcodeList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XD"), McodeDetail.class);
        for (McodeDetail mcode : mcodeList) {
            if (mcode.getThisId().equals(String.valueOf(section))) {
                sectionName = mcode.getMcodeContent();
                break;
            }
        }
        return sectionName;
    }

    private List<HwStatis> findListByUnitIdAndPlanTypeAndClassId(String unitId, String planType, String classId) {
        return hwStatisDao.findListByUnitIdAndPlanTypeAndClassId(unitId, planType, classId);
    }

    private HwStatisEx getNewHwStatisEx(HwStatisEx ex, HashMap<String, String> idMap, String planType) {
        HwStatisEx entity = new HwStatisEx();
        EntityUtils.copyProperties(ex, entity);
        entity.setId(UuidUtils.generateUuid());
        entity.setPlanType(planType);
        entity.setHwStatisId(idMap.get(ex.getHwStatisId()));
        return entity;
    }

    @Override
    public List<HwStatis> findListByConditions(String unitId, String planType, List<String> conditions) {
        List<HwStatis> statisList = hwStatisDao.findByConditions(unitId, planType, conditions);
        if (CollectionUtils.isNotEmpty(statisList)) {
            List<String> statisIds = EntityUtils.getList(statisList, HwStatis::getId);
            Set<String> planIds = EntityUtils.getSet(statisList, HwStatis::getHwPlanId);
            List<HwStatisEx> exList = hwStatisExService.findListByPlanIdsAndStatisIds(unitId, planIds.toArray(new String[planIds.size()]), planType, statisIds.toArray(new String[statisIds.size()]));
            Map<String, List<HwStatisEx>> exMap = exList.stream().collect(Collectors.groupingBy(HwStatisEx::getHwStatisId));
            statisList.forEach(e -> {
                if (exMap.containsKey(e.getId())) {
                    e.setExList1(exMap.get(e.getId()));
                }
            });
        }
        return statisList;
    }

}
