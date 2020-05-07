package net.zdsoft.scoremanage.data.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.scoremanage.data.constant.HwConstants;
import net.zdsoft.scoremanage.data.dao.HwPlanDao;
import net.zdsoft.scoremanage.data.entity.HwPlan;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;
import net.zdsoft.scoremanage.data.service.HwPlanService;
import net.zdsoft.scoremanage.data.service.HwStatisExService;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author niuchao
 * @date 2019/11/5 11:32
 */
@Service("hwPlanService")
public class HwPlanServiceImpl extends BaseServiceImpl<HwPlan, String> implements HwPlanService {

    @Autowired
    private HwPlanDao hwPlanDao;
    @Autowired
    private HwStatisService hwStatisService;
    @Autowired
    private HwStatisExService hwStatisExService;

    @Override
    protected BaseJpaRepositoryDao<HwPlan, String> getJpaDao() {
        return hwPlanDao;
    }

    @Override
    protected Class<HwPlan> getEntityClass() {
        return HwPlan.class;
    }

    @Override
    public List<HwPlan> findListByGradeIdAcadyearAndSemester(String unitId, String gradeId, String acadyear, String semester) {
        return distinctPlan(hwPlanDao.findAll((Root<HwPlan> root, CriteriaQuery<?> query, CriteriaBuilder build)-> {
            List<Predicate> p = new ArrayList<>();
            p.add(build.equal(root.get("gradeId").as(String.class), gradeId));
            if(StringUtils.isNotBlank(acadyear)){
                p.add(build.equal(root.get("acadyear").as(String.class), acadyear));
            }
            if(StringUtils.isNotBlank(semester)){
                p.add(build.equal(root.get("semester").as(String.class), semester));
            }
            p.add(build.equal(root.get("isDeleted").as(Integer.class),0));
            query.where(p.toArray(new Predicate[0]));
            return query.getRestriction();
        }));
    }

    @Override
    public List<HwPlan> findLastPlanListByGradeIdIn(String[] gradeIds) {
        return distinctPlan(hwPlanDao.findPlanListByGradeIdIn(gradeIds));
    }

    @Override
    public List<HwPlan> findListByAcadyearAndSemester(String unitId, String acadyear, String semester, Pagination page) {
        int count = hwPlanDao.countByUnitIdAndAcadyearAndSemesterAndIsDeleted(unitId, acadyear, semester, Constant.IS_DELETED_FALSE);
        page.setMaxRowCount(count);
        return hwPlanDao.findByUnitIdAndAcadyearAndSemesterAndIsDeletedOrderByCreationTimeDesc(unitId, acadyear, semester, Constant.IS_DELETED_FALSE, Pagination.toPageable(page));
    }

    /**
     * plan 列表  按 examId 去重,取CreationTime 最近的那条
     * @param list
     * @return
     */
    private List<HwPlan> distinctPlan(List<HwPlan> list) {
        return CollectionUtils.isEmpty(list)?new ArrayList<>():new ArrayList<>(list.stream()
                .collect(Collectors.toMap(x->x.getExamId(),x->x,(a,b)->a.getCreationTime().after(b.getCreationTime())?a:b)).values());
    }


    @Override
    public HwPlan findLastOne(String unitId, String acadyear, String semester, String gradeId, String examId) {
        return hwPlanDao.findFirstByUnitIdAndAcadyearAndSemesterAndGradeIdAndExamIdAndIsDeletedOrderByCreationTimeDesc(unitId, acadyear, semester, gradeId, examId,Constant.IS_DELETED_FALSE);
    }

    @Override
    public void savePlan(List<HwPlan> addPlanList, List<HwStatis> addStatisList, List<HwStatisEx> addStatisExList) {
        if(CollectionUtils.isNotEmpty(addPlanList)){
            saveAll(addPlanList.toArray(new HwPlan[addPlanList.size()]));
        }
        if(CollectionUtils.isNotEmpty(addStatisList)){
            hwStatisService.saveAll(addStatisList.toArray(new HwStatis[addStatisList.size()]));
        }
        if(CollectionUtils.isNotEmpty(addStatisExList)){
            hwStatisExService.saveAll(addStatisExList.toArray(new HwStatisEx[addStatisExList.size()]));
        }
    }

    @Override
    public void deletePlan(String unitId, String planId, String userId) {
        HwPlan hwPlan = findOne(planId);
        hwPlan.setIsDeleted(Constant.IS_DELETED_TRUE);
        hwPlan.setModifyTime(new Date());
        hwPlan.setOperator(userId);
        save(hwPlan);
        List<HwStatis> hwStatisList = hwStatisService.findListByPlanId(unitId, planId, HwConstants.PLAN_TYPE_1, false, null);
        String[] hwStatisIds = EntityUtils.getList(hwStatisList, HwStatis::getId).toArray(new String[hwStatisList.size()]);
        hwStatisService.deleteAll(hwStatisList.toArray(new HwStatis[hwStatisList.size()]));
        hwStatisExService.deleteByUnitIdAndHwPlanIdAndHwStatisIds(unitId, planId, HwConstants.PLAN_TYPE_1, hwStatisIds);
    }

    @Override
    public List<HwPlan> findListByExamIds(String unitId, String acadyear, String semester, List<String> examIds) {
        List<HwPlan> planList = hwPlanDao.findByUnitIdAndAcadyearAndSemesterAndExamIdInAndIsDeleted(unitId, acadyear, semester, examIds, Constant.IS_DELETED_FALSE);
        List<HwPlan> returnList = new ArrayList<>();
        planList.stream().collect(Collectors.groupingBy(e->e.getExamId()+e.getGradeId())).entrySet().forEach(e->{
            HwPlan hwPlan = e.getValue().stream().sorted((a, b) -> b.getModifyTime().compareTo(a.getModifyTime())).collect(Collectors.toList()).get(0);
            returnList.add(hwPlan);
        });
        return returnList;
    }

    @Override
    public List<HwPlan> findLastListByOldConditions(String[] oldConditions) {
        List<HwPlan> hwPlanList = hwPlanDao.findByOldConditions(oldConditions);
        List<HwPlan> returnList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(hwPlanList)){
            Map<String, List<HwPlan>> hwPlanListMap = EntityUtils.getListMap(hwPlanList, e -> e.getAcadyear() + e.getGradeId() + e.getExamId(), Function.identity());
            for (Map.Entry<String, List<HwPlan>> entry : hwPlanListMap.entrySet()) {
                entry.getValue().sort((a,b)->b.getModifyTime().compareTo(a.getModifyTime()));
                returnList.add(entry.getValue().get(0));
            }
        }
        return returnList;
    }

    @Override
    public List<HwPlan> findListByGradeId(String unitId, String gradeId) {
        return distinctPlan(hwPlanDao.findPlanListByGradeId(unitId,gradeId));
    }

}
