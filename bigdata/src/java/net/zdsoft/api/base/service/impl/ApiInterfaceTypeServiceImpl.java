package net.zdsoft.api.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.api.base.dao.ApiInterfaceTypeDao;
import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service(value="apiInterfaceTypeService")
public class ApiInterfaceTypeServiceImpl extends BaseServiceImpl<ApiInterfaceType, String> 
        implements ApiInterfaceTypeService {
    
	@Autowired
	private ApiInterfaceTypeDao openApiInterfaceTypeDao;
	
	@Override
	protected BaseJpaRepositoryDao<ApiInterfaceType, String> getJpaDao() {
		return openApiInterfaceTypeDao;
	}

	@Override
	protected Class<ApiInterfaceType> getEntityClass() {
		return ApiInterfaceType.class;
	}
	
	@Override
	public List<ApiInterfaceType> findByClassifyIn(Integer... classify) {
		return openApiInterfaceTypeDao.findByClassifyIn(classify);
	}

	@Override
	public List<ApiInterfaceType> getInterfaceTypes(String typeName,
			Integer classify) {
        return findByTypeAndByClassifyAndTypeName(StringUtils.EMPTY,classify,typeName);
	}

	@Override
	public List<ApiInterfaceType> findByTypeAndByClassify(String type,
			Integer classify) {
        return findByTypeAndByClassifyAndTypeName(type,classify,StringUtils.EMPTY);
	}
	
	private List<ApiInterfaceType> findByTypeAndByClassifyAndTypeName(String type,Integer classify, String typeName) {
		Specification<ApiInterfaceType> specification = new Specification<ApiInterfaceType>() {
            @Override
            public Predicate toPredicate(Root<ApiInterfaceType> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                if (classify != null && classify > -1) {
                    ps.add(cb.equal(root.get("classify").as(Integer.class), classify));
                }
                if (StringUtils.isNotBlank(typeName)) {
                    ps.add(cb.like(root.get("typeName").as(String.class), "%" + typeName + "%"));
                }
                if (StringUtils.isNotBlank(type)) {
                	ps.add(cb.like(root.get("type").as(String.class), type));
                }
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        return openApiInterfaceTypeDao.findAll(specification);
	}
}


