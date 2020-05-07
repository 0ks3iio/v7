package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.ChartsConstants;
import net.zdsoft.basedata.dao.ChartsDao;
import net.zdsoft.basedata.entity.Charts;
import net.zdsoft.basedata.entity.ChartsModel;
import net.zdsoft.basedata.entity.ChartsRole;
import net.zdsoft.basedata.entity.ChartsRoleLab;
import net.zdsoft.basedata.entity.ChartsUser;
import net.zdsoft.basedata.service.ChartsModelService;
import net.zdsoft.basedata.service.ChartsRoleLabService;
import net.zdsoft.basedata.service.ChartsRoleService;
import net.zdsoft.basedata.service.ChartsService;
import net.zdsoft.basedata.service.ChartsUserService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;

@Service("chartsService")
public class ChartsServiceImpl implements ChartsService {
	
	//图表所有
	@Autowired
	private ChartsDao chartsDao;
	//图表角色
	@Autowired
	private ChartsRoleService chartsRoleService;
	//图表角色对应的图表
	@Autowired
	private ChartsRoleLabService chartsRoleLabService;
	//图表对应模块
	@Autowired
	private ChartsModelService chartsModelService;
	//用户对应的图表
	@Autowired
	private ChartsUserService chartsUserService;
	
	@Override
	public List<Charts> findChartsByModelIdAndUserId(String modelType, final Integer modelId,final String userId,final String tagType) {
		if(StringUtils.isBlank(modelType)){
			modelType = ChartsConstants.MODEL_TYPE_1;
		}
		final String modelTypeCopy = modelType;
		
		return RedisUtils.getObject(ChartsConstants.KEY_BEFORE_USER+userId+".modelType."+modelTypeCopy+".modelId."+modelId+".tagType."+tagType,0, new TypeReference<List<Charts>>() {
        }, new RedisInterface<List<Charts>>(){

			@Override
			public List<Charts> queryData() {
				List<ChartsRole> roleList = chartsRoleService.findByUserId(userId);
				if(CollectionUtils.isEmpty(roleList)){
					roleList = chartsRoleService.findByUserId(Constant.GUID_ZERO);
				}
				if(CollectionUtils.isNotEmpty(roleList)){
					Set<Integer> roleIds = new HashSet<Integer>();
					for (ChartsRole item : roleList) {
						roleIds.add(item.getId());
					}
					List<Charts> finList = new ArrayList<Charts>();
					//用户角色获取图表
					List<Charts> chartList = findChartsByRoleId(roleIds.toArray(new Integer[0]));
					if(CollectionUtils.isEmpty(chartList)){
						return finList;
					}
					//用户获取图表
					List<Charts> chartList2 = findChartsByUserId(userId);
					Map<String,Charts> chartMap2 = new HashMap<String,Charts>();
					if(CollectionUtils.isNotEmpty(chartList2)){
						for (Charts item : chartList2) {
							chartMap2.put(item.getOtherId(),item);
						}
					}
					//模块获取图表
					List<Charts> chartList3 = findChartsByModelId(modelTypeCopy, modelId, tagType);
					if(CollectionUtils.isEmpty(chartList3)){
						return finList;
					}
					Map<String,Charts> chartMap3 = new HashMap<String,Charts>();
					for (Charts item : chartList3) {
						chartMap3.put(item.getOtherId(),item);
					}
					//取交集
					for (Charts item : chartList) {
						if((chartMap2.size() == 0 || chartMap2.get(item.getId()) != null) && chartMap3.get(item.getId()) != null){
							finList.add(item);
						}
					}
					return finList;
				}else{
					return new ArrayList<Charts>();
				}
			}
			
		});
	}
	
	@Override
	public List<Charts> findChartsByRoleId(final Integer... roleIds){
		if(roleIds!=null && roleIds.length>0){
			final List<Charts> returnList = new ArrayList<Charts>();
			for (Integer item : roleIds) {
				final Integer roId = item;
				returnList.addAll(RedisUtils.getObject(ChartsConstants.KEY_BEFORE_ROLE+roId+".charts",0, new TypeReference<List<Charts>>() {
		        }, new RedisInterface<List<Charts>>(){

					@Override
					public List<Charts> queryData() {
						List<ChartsRoleLab> crlList = chartsRoleLabService.findByChartsRoleIdIn(roId);
						if(CollectionUtils.isNotEmpty(crlList)){
							final Set<Integer> chartIds = new HashSet<Integer>();
							for (ChartsRoleLab item : crlList) {
								chartIds.add(item.getChartsId());
							}
							return findAll(chartIds.toArray(new Integer[0]));
						}else{
							return new ArrayList<Charts>();
						}
					}
					
				}));
			}
			return returnList;
		}else{
			return new ArrayList<Charts>();
		}
		
	};
	
	@Override
	public List<Charts> findChartsByUserId(final String userId){
		return RedisUtils.getObject(ChartsConstants.KEY_BEFORE_USER+userId+".charts",0, new TypeReference<List<Charts>>() {
        }, new RedisInterface<List<Charts>>(){

			@Override
			public List<Charts> queryData() {
				List<ChartsUser> list = chartsUserService.findByUserId(userId);
				if(CollectionUtils.isNotEmpty(list)){
					final Set<Integer> chartIds = new HashSet<Integer>();
					for (ChartsUser item : list) {
						chartIds.add(item.getChartsId());
					}
					return findAll(chartIds.toArray(new Integer[0]));
				}else{
					return new ArrayList<Charts>();
				}
			}
			
		});
	}
	
	@Override
	public List<Charts> findChartsByModelId(String modelType,final Integer modelId, final String tagType){
		if(StringUtils.isBlank(modelType)){
			modelType = ChartsConstants.MODEL_TYPE_1;
		}
		final String modelTypeCopy = modelType;
		
		return RedisUtils.getObject(ChartsConstants.KEY_BEFORE_MODEL+modelId+".modelType."+modelTypeCopy+".tagType."+tagType+".charts",0, new TypeReference<List<Charts>>() {
        }, new RedisInterface<List<Charts>>(){

			@Override
			public List<Charts> queryData() {
				List<ChartsModel> list = chartsModelService.findByModelId(modelId,modelTypeCopy,tagType);
				if(CollectionUtils.isNotEmpty(list)){
					final Set<Integer> chartIds = new HashSet<Integer>();
					for (ChartsModel item : list) {
						chartIds.add(item.getChartsId());
					}
					return findAll(chartIds.toArray(new Integer[0]));
				}else{
					return new ArrayList<Charts>();
				}
			}
			
		});
	}
	
	/**
     * 组装in
     * @param name
     * @param params
     * @param root
     * @param ps
     */
    public void queryIn(final String name, final Integer[] params, Root<Charts> root, List<Predicate> ps) {
		if (params.length <= 1000) {
        	ps.add(root.<Integer> get(name).in((Object[]) params));
        }else{
        	int cyc = params.length / 1000 + (params.length % 1000 == 0 ? 0 : 1);
        	for (int i = 0; i < cyc; i++) {
        		int max = (i + 1) * 1000;
        		if (max > params.length)
        			max = params.length;
        		Predicate p = root.<Integer> get(name).in(
        				(Object[]) ArrayUtils.subarray(params, i * 1000, max));
        		ps.add(p);
        	}
        }
	}
	@Override
	public void doRefreshCacheAll() {
		RedisUtils.delBeginWith(ChartsConstants.KEY_BEFORE);
		chartsRoleService.findByUserId(Constant.GUID_ZERO);
		List<ChartsRole> crList = chartsRoleService.findAll();
		if(CollectionUtils.isNotEmpty(crList)){
			Set<Integer> crIntIds = new HashSet<Integer>();
			for (ChartsRole item : crList) {
				crIntIds.add(item.getId());
			}
			if(CollectionUtils.isNotEmpty(crIntIds))
				findChartsByRoleId(crIntIds.toArray(new Integer[0]));
		}
		List<ChartsModel> cmList = chartsModelService.findAll();
		if(CollectionUtils.isNotEmpty(cmList)){
			for (ChartsModel item : cmList) {
				findChartsByModelId(item.getType(),item.getModelId(),item.getTagType());
			}
		}
	}

	@Override
	public void deleteCacheUser(String[] userIds) {
		if(userIds == null){
			RedisUtils.delBeginWith(ChartsConstants.KEY_BEFORE_USER);
		}else{
			for (String userId : userIds) {
				RedisUtils.delBeginWith(ChartsConstants.KEY_BEFORE_USER+userId);
			}
		}
	}
	
	@Override
	public List<Charts> findAll(final Integer[] intIds){
		Specification<Charts> s = new Specification<Charts>() {
			@Override
			public Predicate toPredicate(Root<Charts> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				queryIn("intId",intIds,root,ps);
				ps.add(cb.equal(root.get("isUsing").as(Integer.class), 1));
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(root.get("orderid").as(String.class)));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}
		};
		return chartsDao.findAll(s);
	};

}
