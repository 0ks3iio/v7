package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.base.dao.OpenApiParameterJpaDao;
import net.zdsoft.base.entity.eis.OpenApiParameter;
import net.zdsoft.base.service.OpenApiParameterService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiParameterService")
public class OpenApiParameterServiceImpl extends BaseServiceImpl<OpenApiParameter, String>
		implements OpenApiParameterService {

	@Autowired
	private OpenApiParameterJpaDao openApiParameterJpaDao;

	@Override
	public Map<String, List<OpenApiParameter>> findParameters(final String... interfaceId) {
		String s = SUtils.s(interfaceId);
		return RedisUtils.getObject("openapi.findParameters." + DigestUtils.md5Hex(s) + s.hashCode(),
				RedisUtils.TIME_HALF_MINUTE, new TR<Map<String, List<OpenApiParameter>>>() {
				}, new RedisInterface<Map<String, List<OpenApiParameter>>>() {
					@Override
					public Map<String, List<OpenApiParameter>> queryData() {
						List<OpenApiParameter> lists = openApiParameterJpaDao.findByInterfaceIdIn(interfaceId);
						Map<String, List<OpenApiParameter>> ret = new HashMap<String, List<OpenApiParameter>>();
						for (OpenApiParameter p : lists) {
							String i = p.getInterfaceId();
							List<OpenApiParameter> params = ret.get(i);
							if (params == null) {
								params = new ArrayList<OpenApiParameter>();
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
	protected BaseJpaRepositoryDao<OpenApiParameter, String> getJpaDao() {
		return openApiParameterJpaDao;
	}

	@Override
	protected Class<OpenApiParameter> getEntityClass() {
		return OpenApiParameter.class;
	}

	@Override
	public void deleteByUri(String uri) {
//		openApiParameterJpaDao.deleteByUri(uri);
	}

	@Override
	public List<OpenApiParameter> findByUri(String uri) {
		return null;
//		return openApiParameterJpaDao.findByUriIn(uri);
	}

	@Override
	public void updateUriByOlduri(String newUri, String oldUri) {
//		openApiParameterJpaDao.updateUriByOlduri(newUri,oldUri);
	}

	@Override
	public List<OpenApiParameter> findByInterfaceId(String interfaceId) {
		return openApiParameterJpaDao.findByInterfaceId(interfaceId);
	}

}
