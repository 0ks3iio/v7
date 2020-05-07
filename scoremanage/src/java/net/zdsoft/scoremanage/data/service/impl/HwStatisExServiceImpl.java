package net.zdsoft.scoremanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.scoremanage.data.dao.HwStatisExDao;
import net.zdsoft.scoremanage.data.dto.SubjectDto;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;
import net.zdsoft.scoremanage.data.service.HwStatisExService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author niuchao
 * @date 2019/11/5 11:32
 */
@Service("hwStatisExService")
public class HwStatisExServiceImpl extends BaseServiceImpl<HwStatisEx, String> implements HwStatisExService {

    @Autowired
    private HwStatisExDao hwStatisExDao;

    @Override
    protected BaseJpaRepositoryDao<HwStatisEx, String> getJpaDao() {
        return hwStatisExDao;
    }

    @Override
    protected Class<HwStatisEx> getEntityClass() {
        return HwStatisEx.class;
    }

    @Override
    public List<HwStatisEx> findListByStatisIds(String unitId, String planId, String planType, String[] statisIds) {
        if(statisIds == null || statisIds.length==0) {
            return new ArrayList<>();
        }
        Specification<HwStatisEx> specification = new Specification<HwStatisEx>(){
            @Override
            public Predicate toPredicate(Root<HwStatisEx> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("hwPlanId").as(String.class), planId));
                ps.add(cb.equal(root.get("planType").as(String.class), planType));
                queryIn("hwStatisId", statisIds, root, ps, cb);
                cq.where(ps.toArray(new Predicate[ps.size()]));
                return cq.getRestriction();
            }
        };
        List<HwStatisEx> exList = findAll(specification);
        return exList;
    }


    @Override
    public List<SubjectDto> findObjectListByPlanIdIn(String unitId,String[] planIds) {
        List<Object[]> list = hwStatisExDao.findObjectListByPlanIdIn(unitId,planIds);
        if(CollectionUtils.isEmpty(list))return new ArrayList<>();
        return EntityUtils.getList(list,x->{
            SubjectDto subject = new SubjectDto();
            subject.setHwPlanId(x[0].toString());
            subject.setObjKey(x[1].toString());
            subject.setSubjectName(x[2].toString());
            return subject;
        });
    }

    @Override
    public void deleteCollectByUnitIdAndGradeIdAndPlanType(String unitId, String gradeId,String planType) {
        hwStatisExDao.deleteCollectByUnitIdAndGradeIdAndPlanType(unitId,gradeId,planType);
    }

    @Override
    public List<HwStatisEx> findListByUnitIdAndPlanIdIn(String unitId, List<String> planIds) {
        return hwStatisExDao.findListByUnitIdAndHwPlanIdIn(unitId,planIds);
    }

    @Override
    public List<HwStatisEx> findListByUnitIdAndPlanIds(String unitId, String[] planIds) {
        return hwStatisExDao.findListByUnitIdAndHwPlanIds(unitId,planIds);
    }

    @Override
    public void deleteByUnitIdAndHwPlanIdsAndPlanType(String unitId, List<String> planIds, String planType) {
        if(CollectionUtils.isNotEmpty(planIds)){
            hwStatisExDao.deleteByUnitIdAndHwPlanIdsAndPlanType(unitId,planIds,planType);
        }
    }

    @Override
    public List<SubjectDto> findObjectListByPlanId(String planId) {
        List<Object[]> list = hwStatisExDao.findObjectListByPlanId(planId);
        if(CollectionUtils.isEmpty(list))return new ArrayList<>();
        return EntityUtils.getList(list,x->{
            SubjectDto s = new SubjectDto();
            s.setHwPlanId(x[0].toString());
            s.setObjKey(x[1].toString());
            s.setSubjectName(x[2].toString());
            return s;
        });
    }


    @Override
    public void deleteByUnitIdAndHwPlanIdAndHwStatisIds(String unitId, String planId, String planType, String[] hwStatisIds) {
        if(hwStatisIds!=null && hwStatisIds.length>0){
            hwStatisExDao.deleteByUnitIdAndHwPlanIdAndPlanTypeAndHwStatisIdIn(unitId, planId, planType, hwStatisIds);
        }
    }

    @Override
    public List<HwStatisEx> findListByPlanIdsAndStatisIds(String unitId, String[] planIds, String planType, String[] statisIds) {
        if(planIds ==null || planIds.length == 0 || statisIds == null || statisIds.length==0) {
            return new ArrayList<>();
        }
        Specification<HwStatisEx> specification = new Specification<HwStatisEx>(){
            @Override
            public Predicate toPredicate(Root<HwStatisEx> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                queryIn("hwPlanId", planIds, root, ps, cb);
                ps.add(cb.equal(root.get("planType").as(String.class), planType));
                queryIn("hwStatisId", statisIds, root, ps, cb);
                cq.where(ps.toArray(new Predicate[ps.size()]));
                return cq.getRestriction();
            }
        };
        List<HwStatisEx> exList = findAll(specification);
        return exList;
    }
}
