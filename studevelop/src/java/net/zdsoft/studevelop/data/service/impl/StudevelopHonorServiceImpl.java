package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StudevelopHonorDao;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopHonor;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.data.service.StudevelopHonorService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * studevelop_honor
 * @author 
 * 
 */
@Service("studevelopHonorService")
public class StudevelopHonorServiceImpl extends BaseServiceImpl<StudevelopHonor, String> implements StudevelopHonorService{
	@Autowired
	private StudevelopHonorDao studevelopHonorDao;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;

	@Override
	protected BaseJpaRepositoryDao<StudevelopHonor, String> getJpaDao() {
		return studevelopHonorDao;
	}

	@Override
	protected Class<StudevelopHonor> getEntityClass() {
		return StudevelopHonor.class;
	}

	@Override
	public List<StudevelopHonor> getStudevelopHonorByAcadyearAndSemester(String acadyear, int semester , String classId) {
		return studevelopHonorDao.getStudevelopHonorByAcadyearAndSemester(acadyear,semester,classId);
	}

	@Override
	public List<StudevelopHonor> getStudevelopHonorByClassIds(String acadyear, int semester, String[] classIds) {
		List<StudevelopHonor> list = studevelopHonorDao.getStudevelopHonorByClassIds(acadyear , semester , classIds);
		if(CollectionUtils.isEmpty(list)){
			return list;
		}
		Set<String> ids = list.stream().map(StudevelopHonor::getId).collect(Collectors.toSet());
		Map<String , List<StudevelopAttachment>  > attsMap = studevelopAttachmentService.findMapByObjIds(ids.toArray(new String[0]));
		for (StudevelopHonor honor : list) {
			List<StudevelopAttachment> atts = attsMap.get(honor.getId());
			honor.setAtts(atts);
		}
		return list;
	}

}
