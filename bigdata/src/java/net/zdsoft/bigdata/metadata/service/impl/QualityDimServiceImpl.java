package net.zdsoft.bigdata.metadata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.metadata.dao.QualityDimDao;
import net.zdsoft.bigdata.metadata.entity.QualityDim;
import net.zdsoft.bigdata.metadata.service.QualityDimService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qualityDimService")
public class QualityDimServiceImpl extends BaseServiceImpl<QualityDim, String>
		implements QualityDimService {

	@Autowired
	private QualityDimDao qualityDimDao;

	@Override
	public List<QualityDim> findQualityDimsByType(Integer type) {
		return qualityDimDao.findQualityDimsByType(type);
	}

	@Override
	public Map<String, QualityDim> findQualityDimMapByType(Integer type) {
		List<QualityDim> dimList = findQualityDimsByType(type);
		Map<String, QualityDim> dimMap = new HashMap<String, QualityDim>();
		for (QualityDim dim : dimList) {
			dimMap.put(dim.getCode(), dim);
		}
		return dimMap;
	}

	@Override
	public void updateQualityDim(QualityDim qualityDim) {
		// QualityDim oldQualityDim = findOne(qualityDim.getId());
		// oldQualityDim.setWeight(qualityDim.getWeight());
		update(qualityDim, qualityDim.getId(), new String[] { "weight" });
	}

	@Override
	protected BaseJpaRepositoryDao<QualityDim, String> getJpaDao() {
		return qualityDimDao;
	}

	@Override
	protected Class<QualityDim> getEntityClass() {
		return QualityDim.class;
	}

}
