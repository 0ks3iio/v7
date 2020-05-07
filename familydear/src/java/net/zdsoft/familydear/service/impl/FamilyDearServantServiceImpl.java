package net.zdsoft.familydear.service.impl;import net.zdsoft.basedata.service.impl.BaseServiceImpl;import net.zdsoft.familydear.entity.FamilyDearServant;import net.zdsoft.familydear.service.FamilyDearServantService;import net.zdsoft.familydear.dao.FamilyDearServantDao;import net.zdsoft.framework.dao.BaseJpaRepositoryDao;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.List;/** * family_dear_servant  * @author  *  */@Service("familyDearServantServiceImpl")public class FamilyDearServantServiceImpl extends BaseServiceImpl<FamilyDearServant,String> implements FamilyDearServantService{	@Autowired	private FamilyDearServantDao familyDearServantDao;	@Override	protected BaseJpaRepositoryDao<FamilyDearServant, String> getJpaDao() {		return familyDearServantDao;	}	@Override	protected Class<FamilyDearServant> getEntityClass() {		return FamilyDearServant.class;	}	@Override	public List<FamilyDearServant> getListByTeacherIds(String[] teacherIds) {		return familyDearServantDao.getListByTeacherIds(teacherIds);	}	@Override	public List<FamilyDearServant> getListByObjIds(String[] objIds, String isDeleted) {		return familyDearServantDao.getListByObjIds(objIds,isDeleted);	}}