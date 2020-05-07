package net.zdsoft.base.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.base.dao.BaseRelationDao;
import net.zdsoft.base.entity.base.BaseRelation;
import net.zdsoft.base.service.BaseRelationService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

@Service("baseRelationService")
public class BaseRelationServiceImpl extends BaseServiceImpl<BaseRelation, String> implements BaseRelationService {
	public static final String BASE_RELATION_SERVICE_CACHE_HEAD = "base.relation.cache.head.";
	@Autowired
	private BaseRelationDao baseRelationDao;
	@Override
	public String findByRelationParm(String businessId, String ticketKey,
			String sourceAp, String model) {
		String key = BASE_RELATION_SERVICE_CACHE_HEAD+businessId+ticketKey+sourceAp+model;
		return RedisUtils.get(key, new RedisInterface<String>() {
			@Override
			public String queryData() {
				return baseRelationDao.findByRelationParm(businessId, ticketKey, sourceAp, model);
			}
		});
		
	}

	@Override
	protected BaseJpaRepositoryDao<BaseRelation, String> getJpaDao() {
		return baseRelationDao;
	}

	@Override
	protected Class<BaseRelation> getEntityClass() {
		return BaseRelation.class;
	}

	@Override
	public void savelists(List<BaseRelation> relations) {
		if(CollectionUtils.isEmpty(relations)){
			return;
		}
		Set<String> deletedKeys = Sets.newHashSet();
		for(BaseRelation br:relations){
			if(StringUtils.isBlank(br.getId())){
				br.setId(UuidUtils.generateUuid());
			}
			if(br.getCreationTime() == null){
				br.setCreationTime(new Date());
			}
			br.setModifyTime(new Date());
			if(br.getIsDeleted() == 1){
				deletedKeys.add(BASE_RELATION_SERVICE_CACHE_HEAD+br.getBusinessId()+br.getTicketKey()+br.getSourceAp()+br.getModel());
			}
		}
		saveAll(relations.toArray(new BaseRelation[relations.size()]));
		if(CollectionUtils.isNotEmpty(deletedKeys)){
			RedisUtils.del(deletedKeys.toArray(new String[deletedKeys.size()]));
		}
	}

	@Override
	public List<BaseRelation> findListByRelationParm(String[] businessIds,
			String ticketKey, String sourceAp, String model,int unitClass) {
		return baseRelationDao.findListByRelationParm(businessIds, ticketKey, sourceAp, model,unitClass);
	}

	@Override
	public void deleteByDcIds(String[] dcIds) {
		baseRelationDao.deleteByDcIds(dcIds);
		
	}

}
