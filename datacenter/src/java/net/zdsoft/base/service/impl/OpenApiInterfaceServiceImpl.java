package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.base.dao.OpenApiInterfaceJpaDao;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("openApiInterfaceService")
public class OpenApiInterfaceServiceImpl extends BaseServiceImpl<OpenApiInterface, String>
		implements OpenApiInterfaceService {

	@Autowired
	private OpenApiInterfaceJpaDao openApiInterfaceJpaDao;

	@Override
	public List<String> findInterfaceType() {
		return openApiInterfaceJpaDao.findInterfaceType(OpenApiConstants.INTERFACE_DATA_TYPE_1);
	}

	@Override
	public List<OpenApiInterface> findInterfaces(String type) {
		return openApiInterfaceJpaDao.findByTypeOrderByDisplayOrder(type);
	}

	@Override
	public OpenApiInterface findByUri(final String uri) {
		return RedisUtils.getObject("openapi.findByUri." + DigestUtils.md5Hex(uri) + uri.hashCode(),
				RedisUtils.TIME_HALF_MINUTE, OpenApiInterface.class, new RedisInterface<OpenApiInterface>() {
					@Override
					public OpenApiInterface queryData() {
						return openApiInterfaceJpaDao.findByUri(uri);
					}
				});
	}

	@Override
	protected BaseJpaRepositoryDao<OpenApiInterface, String> getJpaDao() {
		return openApiInterfaceJpaDao;
	}

	@Override
	protected Class<OpenApiInterface> getEntityClass() {
		return OpenApiInterface.class;
	}

	@Override
	public void updateUsingByIds(int status, String[] ids) {
		openApiInterfaceJpaDao.updateUsingByIds(status,ids);
	}

	@Override
	public void updateUsingById(int status, String id) {
		openApiInterfaceJpaDao.updateUsingById(status,id);
	}

	@Override
	public List<OpenApiInterface> findInterfacesTkList(final String ticketKeyId) {
		return RedisUtils.getObject("openapi.findByTicketKeyId." + ticketKeyId,
				RedisUtils.TIME_FIVE_SECONDS, new TypeReference<List<OpenApiInterface>>(){}, new RedisInterface<List<OpenApiInterface>>() {
					@Override
					public List<OpenApiInterface> queryData() {
						List<OpenApiInterface> list = new ArrayList<OpenApiInterface>();
						return list;
					}
				});
	}
	
	@Override
    public List<OpenApiInterface> getByType(String type) {
        return openApiInterfaceJpaDao.findByType(type);
    }

    @Override
    public List<OpenApiInterface> getByResultType(String type) {
        return openApiInterfaceJpaDao.findByResultType(type);
    }

	@Override
	public List<OpenApiInterface> findByTypeAndDataType(String type, Integer dataType) {
		return openApiInterfaceJpaDao.findByTypeAndDataType(type,dataType);
	}

	@Override
	public void updateInterfaceById(int isUsing, String interId) {
		openApiInterfaceJpaDao.updateInterfaceById(isUsing, interId);
	}

	@Override
	public List<OpenApiInterface> getAllInterfaces(String typeName,
			Integer isUsing, Integer dataType, Pagination page) {
		Specification<OpenApiInterface> specification = new Specification<OpenApiInterface>() {
            @Override
            public Predicate toPredicate(Root<OpenApiInterface> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                if (isUsing != null && isUsing > -1) {
                    ps.add(cb.equal(root.get("isUsing").as(Integer.class), isUsing));
                }
                if (dataType != null && dataType > -1) {
                    ps.add(cb.equal(root.get("dataType").as(Integer.class), dataType));
                }
                if (StringUtils.isNotBlank(typeName)) {
                    ps.add(cb.like(root.get("typeName").as(String.class), "%" + typeName + "%"));
                }
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<OpenApiInterface> findAll = openApiInterfaceJpaDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        }
        else {
            return openApiInterfaceJpaDao.findAll(specification);
        }
	}

	@Override
	public List<OpenApiInterface> findByIsUsing(int isUsing) {
		return openApiInterfaceJpaDao.findByIsUsing(isUsing);
	}

	@Override
	public void deleteByType(String type) {
		openApiInterfaceJpaDao.deleteByType(type);
	}

	@Override
	public void updatetTypeNameByType(String typeName, String type) {
		openApiInterfaceJpaDao.updatetTypeNameByType(typeName, type);
	}

	@Override
	public void updatetTypeNameAndType(String typeName, String newType,
			String oldType) {
		openApiInterfaceJpaDao.updatetTypeNameAndType(typeName, newType, oldType);
	}
	
	@Override
	public List<String> findDistinctTypeByDataType(Integer dataType) {
		if(dataType == null){
			return openApiInterfaceJpaDao.findDistinctType();
		}else{
			return openApiInterfaceJpaDao.findDistinctTypeByDataType(dataType);
		}
	}

	@Override
	public List<OpenApiInterface> findByTypAndDataType(String type,
			Integer dataType) {
		return openApiInterfaceJpaDao.findByTypeAndDataType(type,dataType);
	}

	@Override
	public void updateResultType(String newType, String oldType) {
		openApiInterfaceJpaDao.updateResultType(newType,oldType);
	}

	@Override
	public void deleteByResultType(String resultType) {
		openApiInterfaceJpaDao.deleteByResultType(resultType);
	}

	@Override
	public List<OpenApiInterface> findByUriIn(String[] uris) {
		return openApiInterfaceJpaDao.findByUriIn(uris);
	}

	@Override
	public List<OpenApiInterface> findByDataType(Integer dataType) {
		return openApiInterfaceJpaDao.findByDataType(dataType);
	}
}
