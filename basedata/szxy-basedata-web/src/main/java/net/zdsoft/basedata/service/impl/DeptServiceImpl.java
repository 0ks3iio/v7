package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dao.DeptDao;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.enums.DeptConstants;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("deptService")
public class DeptServiceImpl extends BaseServiceImpl<Dept, String> implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public List<Dept> findByUnitId(final String unitId) {
        return deptDao.findByUnitId(unitId);
    }

    @Override
    public Integer countByUnitId(final String unitId) {
        return deptDao.countByUnitId(unitId);
    }

    @Override
    public List<Dept> findByUnitId(final String unitId, final Pagination page) {
        Integer count = countByUnitId(unitId);
        page.setMaxRowCount(count);
        return deptDao.findByUnitId(unitId, Pagination.toPageable(page));
    }

    @Override
    protected BaseJpaRepositoryDao<Dept, String> getJpaDao() {
        return deptDao;
    }

    @Override
    public List<Dept> findByParentId(String parentId) {
        return deptDao.findByParentId(parentId);
    }

    @Override
    protected Class<Dept> getEntityClass() {
        return Dept.class;
    }

    @Override
    public List<Dept> findByParentId(String parentId, Pagination page) {
        Integer count = deptDao.countByParentId(parentId);
        page.setMaxRowCount(count);
        return deptDao.findByParentId(parentId, Pagination.toPageable(page));
    }

    @Override
    public Map<String, List<Dept>> findByUnitIdMap(final String[] unitIds) {
        List<Dept> deptList = findByUnitIds(unitIds);
        Map<String, List<Dept>> map = new HashMap<String, List<Dept>>();
        for (Dept item : deptList) {
            List<Dept> list = map.get(item.getUnitId());
            if (list == null) {
                list = new ArrayList<Dept>();
                map.put(item.getUnitId(), list);
            }
            list.add(item);
        }
        return map;
    }

    private List<Dept> findByUnitIds(final String[] unitIds) {
        List<Dept> deptList = new ArrayList<Dept>();
        if (unitIds != null && unitIds.length > 0) {
            Specification<Dept> s = new Specification<Dept>() {
                @Override
                public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = new ArrayList<Predicate>();
                    queryIn("unitId", unitIds, root, ps, null);
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.asc(root.get("deptCode").as(String.class)));
                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
            deptList = deptDao.findAll(s);
        }
        return deptList;
    }

    @Override
    public List<Dept> findByUnitIdAndParentId(String unitId, String parentId) {
        return deptDao.findByUnitIdAndParentId(unitId, parentId);
    }

    @Override
    public List<Dept> findByParentIdAndDeptName(String parentId, String deptName) {
        return deptDao.findByParentIdAndDeptName(parentId, deptName);
    }

    @Override
    public List<Dept> findByUnitIdAndDeputyHeadId(String unitId, String deputyHeadId) {
        return deptDao.findByUnitIdAndDeputyHeadId(unitId, deputyHeadId);
    }

    @Override
    public List<Dept> findByAreaId(String areaId) {
        return deptDao.findByAreaId(areaId);
    }

    @Override
    public List<Dept> findByTeacherId(String userId) {
        return deptDao.findByTeacherId(userId);
    }

    @Override
    public List<Dept> findByUnitIdAndLeaderId(String unitId, String leaderId) {
        return deptDao.findByUnitIdAndLeaderId(unitId, leaderId);
    }
    
    public Dept findByUnitAndCode(String unitId, String code) {
    	List<Dept> dps = deptDao.findByUnitAndCode(unitId, code);
    	if(CollectionUtils.isNotEmpty(dps)) {
    		return dps.get(0);
    	}
    	return null;
    }

    @Override
    public List<Dept> findByInstituteId(String instituteId) {
        return deptDao.findByInstituteId(instituteId);
    }

    @Override
    public List<Dept> findByUnitIdAndDeptNameLike(String unitId, String deptName) {
        return deptDao.findByUnitIdAndDeptNameLike(unitId, deptName);
    }

    @Override
    public List<Dept> saveAllEntitys(Dept... dept) {
        return deptDao.saveAll(checkSave(dept));
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0)
            deptDao.deleteAllByIds(id);
    }
    

    /**
     * 根据单位和更新时间获取部门
     *
     * @param 
     * @return
     */
    @Override
    public List<Dept> findByUnitIdAndModifyTime(String unitId, Date modifyTime){
    	return deptDao.findByUnitIdAndModifyTime(unitId, modifyTime);
    }
    
    /**
     * 根据单位获取部门
     *
     * @param 
     * @return
     */
    @Override
    public List<Dept> findAllByUnitId(String unitId){
    	return deptDao.findAllByUnitId(unitId);
    }
    
    /**
     * 更新用户钉钉号
     */
    @Override
    public void updateDingDingIdById(String dingdingId, String id){
    	deptDao.updateDingDingIdById(dingdingId, id);
    }

    @Override
    public String getAvailableDeptCodeByUnitId(String unitId) {
        Integer maxCode = deptDao.getMaxDeptCodeByUnitId(unitId);
        if (maxCode == null) {
            maxCode = 0;
        }
        maxCode = maxCode + 1;
        return String.format("%1$0" + DeptConstants.DEPT_CODE_MAX_LENGTH + "d", maxCode);
    }

    @Override
    public void deleteDeptsByUnitId(String unitId) {
        deptDao.deleteDeptsByUnitId(unitId);
    }
}
