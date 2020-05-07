package net.zdsoft.api.base.service.impl;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.api.base.dao.ApiInterfaceJpaDao;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.openapi.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

@Service("apiInterfaceService")
public class ApiInterfaceServiceImpl extends BaseServiceImpl<ApiInterface, String>
		implements ApiInterfaceService {

	@Autowired
	private ApiInterfaceJpaDao apiInterfaceJpaDao;
	@Resource
	private BigLogService bigLogService;
	@Autowired
	private MetadataRelationService metadataRelationService;

	@Override
	public List<String> findInterfaceType() {
		return apiInterfaceJpaDao.findInterfaceType(OpenApiConstants.INTERFACE_DATA_TYPE_1);
	}

	@Override
	public List<ApiInterface> findInterfaces(String type) {
		return apiInterfaceJpaDao.findByTypeOrderByDisplayOrder(type);
	}

	@Override
	public ApiInterface findByUri(final String uri) {
		return RedisUtils.getObject("openapi.findByUri." + DigestUtils.md5Hex(uri) + uri.hashCode(),
				RedisUtils.TIME_HALF_MINUTE, ApiInterface.class, new RedisInterface<ApiInterface>() {
					@Override
					public ApiInterface queryData() {
						return apiInterfaceJpaDao.findByUri(uri);
					}
				});
	}

	@Override
	protected BaseJpaRepositoryDao<ApiInterface, String> getJpaDao() {
		return apiInterfaceJpaDao;
	}

	@Override
	protected Class<ApiInterface> getEntityClass() {
		return ApiInterface.class;
	}

	@Override
	public void updateUsingByIds(int status, String[] ids) {
		apiInterfaceJpaDao.updateUsingByIds(status,ids);
	}

	@Override
	public void updateUsingById(int status, String id) {
		apiInterfaceJpaDao.updateUsingById(status,id);
	}

	@Override
	public List<ApiInterface> findInterfacesTkList(final String ticketKeyId) {
		return RedisUtils.getObject("openapi.findByTicketKeyId." + ticketKeyId,
				RedisUtils.TIME_FIVE_SECONDS, new TypeReference<List<ApiInterface>>(){}, new RedisInterface<List<ApiInterface>>() {
					@Override
					public List<ApiInterface> queryData() {
						List<ApiInterface> list = new ArrayList<ApiInterface>();
						return list;
					}
				});
	}
	
	@Override
    public List<ApiInterface> getByType(String type) {
        return apiInterfaceJpaDao.findByType(type);
    }

    @Override
    public List<ApiInterface> getByResultType(String type) {
        return apiInterfaceJpaDao.findByResultType(type);
    }

	@Override
	public List<ApiInterface> findByTypeAndDataType(String type, Integer dataType) {
		return apiInterfaceJpaDao.findByTypeAndDataType(type,dataType);
	}

	@Override
	public void updateInterfaceById(int isUsing, String interId) {
		ApiInterface apiInterface = apiInterfaceJpaDao.findById(interId).get();
		apiInterfaceJpaDao.updateInterfaceById(isUsing, interId);
		//业务日志埋点  修改
		LogDto logDto=new LogDto();
		logDto.setBizCode("update-interface");
		logDto.setDescription("接口 "+apiInterface.getUri()+" 的启用状态");
		logDto.setOldData(apiInterface.getIsUsing());
		logDto.setNewData(isUsing);
		logDto.setBizName("数据接口管理");
		bigLogService.updateLog(logDto);

	}

	@Override
	public List<ApiInterface> getAllInterfaces(String typeName,
			Integer isUsing, Integer dataType, Pagination page) {
		Specification<ApiInterface> specification = new Specification<ApiInterface>() {
            @Override
            public Predicate toPredicate(Root<ApiInterface> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("modifyTime").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<ApiInterface> findAll = apiInterfaceJpaDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        }
        else {
            return apiInterfaceJpaDao.findAll(specification);
        }
	}

	@Override
	public List<ApiInterface> findByIsUsing(int isUsing) {
		return apiInterfaceJpaDao.findByIsUsing(isUsing);
	}

	@Override
	public void deleteByType(String type) {
		List<ApiInterface> apiInterfaces = apiInterfaceJpaDao.findByType(type);
		for (ApiInterface apiInterface : apiInterfaces) {
			metadataRelationService.deleteBySourceIdAndTargetId(apiInterface.getMetadataId(),apiInterface.getId());
		}
		apiInterfaceJpaDao.deleteByType(type);
	}

	@Override
	public void updatetTypeNameByType(String typeName, String type) {
		apiInterfaceJpaDao.updatetTypeNameByType(typeName, type);
	}

	@Override
	public void updatetTypeNameAndType(String typeName, String newType,
			String oldType) {
		apiInterfaceJpaDao.updatetTypeNameAndType(typeName, newType, oldType);
	}
	
	@Override
	public List<String> findDistinctTypeByDataType(Integer dataType) {
		if(dataType == null){
			return apiInterfaceJpaDao.findDistinctType();
		}else{
			return apiInterfaceJpaDao.findDistinctTypeByDataType(dataType);
		}
	}

	@Override
	public List<ApiInterface> findByTypAndDataType(String type,
			Integer dataType) {
		return apiInterfaceJpaDao.findByTypeAndDataType(type,dataType);
	}

	@Override
	public void updateResultType(String newType, String oldType) {
		apiInterfaceJpaDao.updateResultType(newType,oldType);
	}

	@Override
	public void deleteByResultType(String resultType) {
		List<ApiInterface> apiInterfaces = apiInterfaceJpaDao.findByResultType(resultType);
		for (ApiInterface apiInterface : apiInterfaces) {
			metadataRelationService.deleteBySourceIdAndTargetId(apiInterface.getMetadataId(),apiInterface.getId());
		}
		apiInterfaceJpaDao.deleteByResultType(resultType);
	}

	@Override
	public List<ApiInterface> findByUriIn(String[] uris) {
		return apiInterfaceJpaDao.findByUriIn(uris);
	}

	@Override
	public List<ApiInterface> findByDataType(Integer dataType) {
		return apiInterfaceJpaDao.findByDataType(dataType);
	}

	@Override
	public List<ApiInterface> findByResultTypeAndDataType(String resultType,
			Integer dataType) {
		return apiInterfaceJpaDao.findByResultTypeAndDataType(resultType,dataType);
	}

	@Override
	public List<ApiInterface> findByType(String type) {
		return apiInterfaceJpaDao.findByType(type);
	}
}
