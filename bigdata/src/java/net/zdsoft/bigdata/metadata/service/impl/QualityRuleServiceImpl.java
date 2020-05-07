package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.metadata.dao.QualityRuleDao;
import net.zdsoft.bigdata.metadata.entity.QualityDim;
import net.zdsoft.bigdata.metadata.entity.QualityRule;
import net.zdsoft.bigdata.metadata.service.QualityDimService;
import net.zdsoft.bigdata.metadata.service.QualityRuleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("qualityRuleService")
public class QualityRuleServiceImpl extends
		BaseServiceImpl<QualityRule, String> implements QualityRuleService {

	@Autowired
	private QualityRuleDao qualityRuleDao;

	@Autowired
	private QualityDimService qualityDimService;
	

	@Resource
	private BigLogService bigLogService;

	@Override
	public List<QualityRule> findQualityRulesByDimCode(String dimCode) {
		List<QualityRule> ruleList = new ArrayList<QualityRule>();
		if (StringUtils.isBlank(dimCode) || "all".equals(dimCode)) {
			ruleList = qualityRuleDao.findQualityRules();
		} else {
			ruleList = qualityRuleDao.findQualityRulesByDimCode(dimCode);
		}
		Map<String, QualityDim> dimMap = qualityDimService
				.findQualityDimMapByType(2);
		for (QualityRule rule : ruleList) {
			if (dimMap.containsKey(rule.getDimCode())) {
				rule.setDimName(dimMap.get(rule.getDimCode()).getName());
			}
		}
		return ruleList;
	}

	@Override
	public void saveQualityRule(QualityRule qualityRule) {
		if (StringUtils.isBlank(qualityRule.getId())) {
			qualityRule.setId(UuidUtils.generateUuid());
			qualityRule.setCreationTime(new Date());
			qualityRule.setModifyTime(new Date());
			save(qualityRule);

			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-qualityRule");
			logDto.setDescription("质量规则 "+qualityRule.getRuleName());
			logDto.setNewData(qualityRule);
			logDto.setBizName("数据质量设置");
			bigLogService.insertLog(logDto);
		} else {
			QualityRule oldQualityRule = qualityRuleDao.findById(qualityRule.getId()).get();
			qualityRule.setModifyTime(new Date());
			update(qualityRule, qualityRule.getId(), new String[] { "ruleType",
					"ruleName", "dimCode", "computerType", "detail", "orderId",
					"remark", "modifyTime" });

			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-qualityRule");
			logDto.setDescription("质量规则 "+qualityRule.getRuleName());
			logDto.setOldData(oldQualityRule);
			logDto.setNewData(qualityRule);
			logDto.setBizName("数据质量设置");
			bigLogService.updateLog(logDto);
		}
	}

	@Override
	public List<QualityRule> findQualityRulesByRuleType(Integer ruleType) {
		return qualityRuleDao.findAllByRuleType(ruleType);
	}

	@Override
	protected BaseJpaRepositoryDao<QualityRule, String> getJpaDao() {
		return qualityRuleDao;
	}

	@Override
	protected Class<QualityRule> getEntityClass() {
		return QualityRule.class;
	}

}
