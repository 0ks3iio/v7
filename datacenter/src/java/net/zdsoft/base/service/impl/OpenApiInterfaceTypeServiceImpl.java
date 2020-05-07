package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.base.dao.OpenApiInterfaceTypeDao;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.service.OpenApiInterfaceTypeService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service(value="openApiInterfaceTypeService")
public class OpenApiInterfaceTypeServiceImpl extends BaseServiceImpl<OpenApiInterfaceType, String> 
        implements OpenApiInterfaceTypeService {
    
	@Autowired
	private OpenApiInterfaceTypeDao openApiInterfaceTypeDao;
	
	@Override
	protected BaseJpaRepositoryDao<OpenApiInterfaceType, String> getJpaDao() {
		return openApiInterfaceTypeDao;
	}

	@Override
	protected Class<OpenApiInterfaceType> getEntityClass() {
		return OpenApiInterfaceType.class;
	}
	
	@Override
	public List<OpenApiInterfaceType> findByClassifyIn(Integer... classify) {
		return openApiInterfaceTypeDao.findByClassifyIn(classify);
	}

	@Override
	public List<OpenApiInterfaceType> getInterfaceTypes(String typeName,
			Integer classify) {
        return findByTypeAndByClassifyAndTypeName(StringUtils.EMPTY,classify,typeName);
	}

	@Override
	public List<OpenApiInterfaceType> findByTypeAndByClassify(String type,
			Integer classify) {
        return findByTypeAndByClassifyAndTypeName(type,classify,StringUtils.EMPTY);
	}
	
	private List<OpenApiInterfaceType> findByTypeAndByClassifyAndTypeName(String type,Integer classify, String typeName) {
		Specification<OpenApiInterfaceType> specification = new Specification<OpenApiInterfaceType>() {
            @Override
            public Predicate toPredicate(Root<OpenApiInterfaceType> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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


