package net.zdsoft.qulity.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.qulity.data.dao.QualityScoreDao;
import net.zdsoft.qulity.data.entity.QualityScore;
import net.zdsoft.qulity.data.service.QualityScoreService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Service("qualityScoreService")
public class QualityScoreServiceImpl extends BaseServiceImpl<QualityScore, String> implements QualityScoreService {

	@Autowired
	private QualityScoreDao qualityScoreDao;
	
	@Override
	protected BaseJpaRepositoryDao<QualityScore, String> getJpaDao() {
		return qualityScoreDao;
	}

	@Override
	protected Class<QualityScore> getEntityClass() {
		return QualityScore.class;
	}

	@Override
	public QualityScore findByUnitIdOne(String unitId, String type) {
		return qualityScoreDao.findByUnitIdOne(unitId, type);
	}

	@Override
	public void deleteByUnitIdAndType(String unitId, String type) {
		if(StringUtils.isBlank(type)){
			qualityScoreDao.deleteByUnitId(unitId);
		}else{
			qualityScoreDao.deleteByUnitIdAndType(unitId, type);
		}
	}

	@Override
	public List<QualityScore> findByClassIdsAndType(final String[] classIds, String type, Pagination page) {
		 Specification<QualityScore> specification = new Specification<QualityScore>() {
            @Override
            public Predicate toPredicate(Root<QualityScore> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("type").as(String.class), type));
            	if(classIds.length>0){
            		 In<String> in = cb.in(root.get("classId").as(String.class));
                     for (int i = 0; i < classIds.length; i++) {
                         in.value(classIds[i]);
                     }
                     ps.add(in);
            	}
                List<Order> orderList = new ArrayList<Order>();
                if(classIds.length>1){
                	orderList.add(cb.asc(root.get("gradeRank").as(Integer.class)));
                }else{
                	orderList.add(cb.asc(root.get("classRank").as(Integer.class)));
                }

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
        if (page != null) {
        	Pageable pageable = Pagination.toPageable(page);
        	Page<QualityScore> findAll = qualityScoreDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	return findAll.getContent();
        }
        else {
        	return qualityScoreDao.findAll(specification);
        }
	}

	@Override
	public List<QualityScore> findByUnitIdAndType(String unitId, String type) {
		return qualityScoreDao.findByUnitIdAndType(unitId, type);
	}

	@Override
	public void saveAndDelete(String unitId, String type, QualityScore[] scoreList) {
		deleteByUnitIdAndType(unitId, type);
		if(ArrayUtils.isNotEmpty(scoreList)){
			saveAll(scoreList);
		}
	}

	@Override
	public void setRecoverStuScore(String unitId) {


	}

}
