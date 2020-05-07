package net.zdsoft.api.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.api.base.dao.ApiParameterJpaDao;
import net.zdsoft.api.base.entity.eis.ApiParameter;
import net.zdsoft.api.base.service.ApiParameterService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiParameterService")
public class ApiParameterServiceImpl extends BaseServiceImpl<ApiParameter, String>
		implements ApiParameterService {

	@Autowired
	private ApiParameterJpaDao openApiParameterJpaDao;

	@Override
	public Map<String, List<ApiParameter>> findParameters(final String... interfaceId) {
		String s = SUtils.s(interfaceId);
		return RedisUtils.getObject("openapi.findParameters." + DigestUtils.md5Hex(s) + s.hashCode(),
				RedisUtils.TIME_HALF_MINUTE, new TR<Map<String, List<ApiParameter>>>() {
				}, new RedisInterface<Map<String, List<ApiParameter>>>() {
					@Override
					public Map<String, List<ApiParameter>> queryData() {
						List<ApiParameter> lists = openApiParameterJpaDao.findByInterfaceIdIn(interfaceId);
						Map<String, List<ApiParameter>> ret = new HashMap<String, List<ApiParameter>>();
						for (ApiParameter p : lists) {
							String i = p.getInterfaceId();
							List<ApiParameter> params = ret.get(i);
							if (params == null) {
								params = new ArrayList<ApiParameter>();
								ret.put(i, params);
							}
							params.add(p);
						}
						return ret;
					}
				});

	}

	@Override
	public void delete(String id) {
		openApiParameterJpaDao.deleteById(id);
	}

	@Override
	protected BaseJpaRepositoryDao<ApiParameter, String> getJpaDao() {
		return openApiParameterJpaDao;
	}

	@Override
	protected Class<ApiParameter> getEntityClass() {
		return ApiParameter.class;
	}

	@Override
	public List<ApiParameter> findByInterfaceId(String interfaceId) {
		return openApiParameterJpaDao.findByInterfaceId(interfaceId);
	}

	@Override
	public List<ApiParameter> findByInterfaceIdIn(String... interfaceIds) {
		return openApiParameterJpaDao.findByInterfaceIdIn(interfaceIds);
	}

	@Override
	public void deleteByInterfaceId(String interfaceId) {
		openApiParameterJpaDao.deleteByInterfaceId(interfaceId);
	}

}
