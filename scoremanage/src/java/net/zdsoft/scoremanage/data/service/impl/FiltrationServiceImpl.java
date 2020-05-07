package net.zdsoft.scoremanage.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.dao.FiltrationDao;
import net.zdsoft.scoremanage.data.entity.Filtration;
import net.zdsoft.scoremanage.data.service.FiltrationService;

@Service("filtrationService")
public class FiltrationServiceImpl extends BaseServiceImpl<Filtration, String> implements FiltrationService{

	@Autowired
	private FiltrationDao filtrationDao;
	@Override
	protected BaseJpaRepositoryDao<Filtration, String> getJpaDao() {
		return filtrationDao;
	}

	@Override
	protected Class<Filtration> getEntityClass() {
		return Filtration.class;
	}

	@Override
	public Map<String, String> findByExamIdAndSchoolIdAndType(String examId,
			String unitId, String type) {
		List<Filtration> list=filtrationDao.findByExamIdAndSchoolIdAndType(examId,unitId,type);
		if(CollectionUtils.isNotEmpty(list)){
			Map<String, String> returnMap = new HashMap<String,String>();
			for(Filtration f:list){
				returnMap.put(f.getStudentId(), f.getStudentId());
			}
			return returnMap;
		}else{
			return new HashMap<String,String>();
		}
		
	}

	@Override
	public void insertList(List<Filtration> fList) {
		if(CollectionUtils.isNotEmpty(fList)){
			saveAllEntitys(fList.toArray(new Filtration[]{}));
		}
	}

	@Override
	public List<Filtration> saveAllEntitys(Filtration... filtration) {
		return filtrationDao.saveAll(checkSave(filtration));
	}

	@Override
	public void deleteByExamIdAndStudentIdIn(String examId,String type, String[] studentIds) {
		filtrationDao.deleteByExamIdAndTypeAndStudentIdIn(examId,type,studentIds);
	}

	@Override
	public List<Filtration> findListBySchoolIds(String examId, String... schoolIds) {
		return filtrationDao.findListBySchoolIds(examId, schoolIds);
	}

	@Override
	public List<Filtration> findBySchoolIdAndStudentIdAndType(String unitId, String studentId, String type) {
		return filtrationDao.findBySchoolIdAndStudentIdAndType(unitId,studentId,type);
	}

	



}
