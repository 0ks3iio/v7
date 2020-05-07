package net.zdsoft.remote.openapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.dao.OpenApiParameterJpaDao;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;
import net.zdsoft.remote.openapi.service.OpenApiParameterService;

@Service("openApiParameterService")
public class OpenApiParameterServiceImpl extends BaseServiceImpl<OpenApiParameter, String>
		implements OpenApiParameterService {

	@Autowired
	private OpenApiParameterJpaDao openApiParameterJpaDao;

	@Override
	public Map<String, List<OpenApiParameter>> findParameters(final String... uris) {
		String s = SUtils.s(uris);
		return RedisUtils.getObject("openapi.findParameters." + DigestUtils.md5Hex(s) + s.hashCode(),
				RedisUtils.TIME_HALF_MINUTE, new TR<Map<String, List<OpenApiParameter>>>() {
				}, new RedisInterface<Map<String, List<OpenApiParameter>>>() {
					@Override
					public Map<String, List<OpenApiParameter>> queryData() {
						List<OpenApiParameter> lists = openApiParameterJpaDao.findByUriIn(uris);
						Map<String, List<OpenApiParameter>> ret = new HashMap<String, List<OpenApiParameter>>();
						for (OpenApiParameter p : lists) {
							String u = p.getUri();
							List<OpenApiParameter> params = ret.get(u);
							if (params == null) {
								params = new ArrayList<OpenApiParameter>();
								ret.put(u, params);
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
		openApiParameterJpaDao.deleteByUri(uri);
	}

	@Override
	public List<OpenApiParameter> findByUri(String uri) {
		return openApiParameterJpaDao.findByUriIn(uri);
	}

	@Override
	public void updateUriByOlduri(String newUri, String oldUri) {
		openApiParameterJpaDao.updateUriByOlduri(newUri,oldUri);
	}

}
