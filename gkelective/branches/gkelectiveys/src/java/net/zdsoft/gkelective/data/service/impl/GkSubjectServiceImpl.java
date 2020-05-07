package net.zdsoft.gkelective.data.service.impl;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkRelationshipDao;
import net.zdsoft.gkelective.data.dao.GkSubjectDao;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.service.GkSubjectService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkSubjectService")
public class GkSubjectServiceImpl extends BaseServiceImpl<GkSubject, String> implements GkSubjectService{
	@Autowired
	private GkSubjectDao gkSubjectDao;
	@Autowired
	private GkRelationshipDao gkRelationshipDao;
	@Override
	public List<GkSubject> findByRoundsId(String roundsId,Integer teachModel) {
		if(teachModel==null){
			return gkSubjectDao.findByRoundsId(roundsId);
		}else{
			return gkSubjectDao.findByRoundsIdAndTeachModel(roundsId,teachModel);
		}
		
	}

	@Override
	protected BaseJpaRepositoryDao<GkSubject, String> getJpaDao() {
		return gkSubjectDao;
	}

	@Override
	protected Class<GkSubject> getEntityClass() {
		return GkSubject.class;
	}

	@Override
	public void saveOrUpdate(GkSubject... gkSubjects) {
		if(gkSubjects!=null && gkSubjects.length>0){
			saveAll(checkSave(gkSubjects).toArray(new GkSubject[0]));
		}
	}

	@Override
	public int findSubNumByRoundsIdTeachModel(String roundsId, int teachModel) {
		return gkSubjectDao.findSubNumByRoundsIdTeachModel(roundsId,teachModel);
	}

	@Override
	public void deleteByRoundsId(String roundsId) {
		 List<GkSubject> gkslist = gkSubjectDao.findByRoundsId(roundsId);
		 if(CollectionUtils.isNotEmpty(gkslist)){
			 Set<String> ids = EntityUtils.getSet(gkslist, "id");
			 gkRelationshipDao.deleteByTypePrimaryId(GkElectveConstants.RELATIONSHIP_TYPE_01, ids.toArray(new String[0]));
		 }
		gkSubjectDao.deleteByRoundsId(roundsId);
	}
}
