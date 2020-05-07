package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.ApiDao;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午6:30
 */
@Service("apiService")
public class ApiServiceImpl extends BaseServiceImpl<Api, String> implements
		ApiService {

	@Resource
	private ApiDao apiDao;

	@Resource
	private BigLogService bigLogService;

	@Override
	protected BaseJpaRepositoryDao<Api, String> getJpaDao() {
		return apiDao;
	}

	@Override
	protected Class<Api> getEntityClass() {
		return Api.class;
	}

	@Override
	public List<Api> findApisByUnitId(String unitId) {
		return apiDao.findApisByUnitId(unitId);
	}

	@Override
	public void saveApi(Api api) {
		if (StringUtils.isNotBlank(api.getId())) {
			// 更新数据源
			Api oldApi = apiDao.findById(api.getId()).get();
			api.setModifyTime(new Date());
			update(api, api.getId(), new String[] { "name", "unitId", "url",
					"remark", "modifyTime", "paramDescription" });
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-api");
			logDto.setDescription("API "+oldApi.getName());
			logDto.setOldData(oldApi);
			logDto.setNewData(api);
			logDto.setBizName("数据源管理");
			bigLogService.updateLog(logDto);

		} else {
			// 新增数据源
			api.setId(UuidUtils.generateUuid());
			api.setCreationTime(new Date());
			api.setModifyTime(new Date());
			save(api);
			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-api");
			logDto.setDescription("API "+api.getName());
			logDto.setNewData(api);
			logDto.setBizName("数据源管理");
			bigLogService.insertLog(logDto);

		}
	}

	@Override
	public void deleteApi(String id) {
		delete(id);
	}

	@Override
	public List<Api> findApis(String unitId, String name, String description) {
		name = StringUtils.isBlank(name) ? "%%" : "%" + name.trim() + "%";
		description = StringUtils.isBlank(description) ? "%%" : "%" + description.trim() + "%";
		return apiDao.findApisByUnitIdAndNameLikeAndRemarkLikeOrderByModifyTimeDesc(unitId, name, description);
	}

	@Override
	public long count(Date start, Date end) {
		if (start == null && end == null) {
			return apiDao.count((Specification<Api>) (root, criteriaQuery, criteriaBuilder)
					-> criteriaQuery.getRestriction());
		} else {
			return apiDao.count((Specification<Api>) (root, criteriaQuery, criteriaBuilder)
					-> {
				Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
				return criteriaQuery.where(time).getRestriction();
			});
		}
	}
}
