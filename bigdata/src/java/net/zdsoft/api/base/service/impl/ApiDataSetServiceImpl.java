package net.zdsoft.api.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.api.base.dao.ApiDataSetDao;
import net.zdsoft.api.base.entity.eis.ApiDataSet;
import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.api.base.service.ApiDataSetRuleService;
import net.zdsoft.api.base.service.ApiDataSetService;
import net.zdsoft.api.dataset.vo.DataSetRuleDto;
import net.zdsoft.api.dataset.vo.DateSetDto;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service("apiDataSetService")
public class ApiDataSetServiceImpl extends BaseServiceImpl<ApiDataSet, String> implements ApiDataSetService{

	@Autowired
	private ApiDataSetDao apiDataSetDao;
	@Autowired
	private ApiDataSetRuleService apiDataSetRuleService;
	
	@Override
	protected BaseJpaRepositoryDao<ApiDataSet, String> getJpaDao() {
		return apiDataSetDao;
	}
	@Override
	protected Class<ApiDataSet> getEntityClass() {
		return ApiDataSet.class;
	}

	@Override
	public List<ApiDataSet> findAllByPage(Pagination page, String dataSetName) {
		dataSetName = StringUtils.isBlank(dataSetName) ? "%%" : "%" + dataSetName + "%";
        Integer count = apiDataSetDao.countByNameLike(dataSetName);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return apiDataSetDao.findByNameAndPage(Pagination.toPageable(page), dataSetName);
	}
	
	@Override
	public void deleteDataSet(String id) {
		// TODO Auto-generated method stub
		apiDataSetDao.deleteById(id);
		apiDataSetRuleService.deleteByDsId(id);
	}
	@Override
	public void save(DateSetDto dataDateSetDto) {
		ApiDataSet apiDataSet;
		    if (StringUtils.isBlank(dataDateSetDto.getId())) {
			    apiDataSet = new ApiDataSet();
			    apiDataSet.setId(UuidUtils.generateUuid());
			    apiDataSet.setCreationTime(new Date());
			    apiDataSet.setMdId(dataDateSetDto.getMetadataId());
	        } else {
	        	apiDataSet = apiDataSetDao.findById(dataDateSetDto.getId()).get();
	        	//删除之前的规则，重新保存新的
	        	apiDataSetRuleService.deleteByDsId(apiDataSet.getId());
	        }
		    apiDataSet.setModifyTime(new Date());
		    apiDataSet.setName(dataDateSetDto.getName());
		    apiDataSet.setRemark(dataDateSetDto.getRemark());
		    List<ApiDataSetRule> apiDataSetRules = new ArrayList<>();
		    List<DataSetRuleDto> saveDataSetRuleDtos = JSON.parseArray(dataDateSetDto.getDataSetRuleDtos(), DataSetRuleDto.class);
		    saveDataSetRuleDtos.forEach(c->{
		    	ApiDataSetRule apiDataSetRule = new ApiDataSetRule();
		    	apiDataSetRule.setId(UuidUtils.generateUuid());
		    	apiDataSetRule.setDsId(apiDataSet.getId());
		    	apiDataSetRule.setParamType(c.getParamType());
		    	apiDataSetRule.setParamValue(c.getValue());
		    	apiDataSetRule.setParamJson(JSON.toJSON(c.getRuleXXs()).toString());
		    	apiDataSetRules.add(apiDataSetRule);
		    });
		    apiDataSetDao.save(apiDataSet);
		    apiDataSetRuleService.saveAll(apiDataSetRules.toArray(new ApiDataSetRule[0]));
		    
	}
	@Override
	public ApiDataSet findByMdId(String metadataId) {
		return apiDataSetDao.findByMdId(metadataId);
	}
	@Override
	public List<ApiDataSet> findByMdIdIn(String[] metadataIds) {
		return apiDataSetDao.findByMdIdIn(metadataIds);
	}
	@Override
	public void deleteDataSetByMbid(String mbid) {
		// TODO Auto-generated method stub
		ApiDataSet apiDataSet = apiDataSetDao.findById(mbid).get();
		if(apiDataSet != null) {
			apiDataSetDao.deleteById(apiDataSet.getId());
			apiDataSetRuleService.deleteByDsId(apiDataSet.getId());
		}
	}
	
}
