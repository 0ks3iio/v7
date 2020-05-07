package net.zdsoft.stuwork.data.service.impl;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dao.DyClassOtherCheckDao;
import net.zdsoft.stuwork.data.entity.DyClassOtherCheck;
import net.zdsoft.stuwork.data.service.DyClassOtherCheckService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyClassOtherCheckService")
public class DyClassOtherCheckServiceImpl extends BaseServiceImpl<DyClassOtherCheck,String> implements DyClassOtherCheckService{

	@Autowired
	private DyClassOtherCheckDao dyClassOtherCheckDao;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@Override
	public List<DyClassOtherCheck> findByUnitId(String unitId, String acadyear,Integer semester) {
		List<DyClassOtherCheck> checks = dyClassOtherCheckDao.findByUnitId(unitId,acadyear,semester);
		getClassName(checks);
		return checks;
	}
	public List<DyClassOtherCheck> findByClassIdAndWeek(String classId,int week,String acadyear, Integer semester){
		if(StringUtils.isNotBlank(classId)){
			return dyClassOtherCheckDao.findByClassIdAndWeek(classId, week, acadyear, semester);
		}else{
			return dyClassOtherCheckDao.findByClassIdAndWeek(week, acadyear, semester);
		}
	}
	@Override
	public List<DyClassOtherCheck> findByClassId(String classId,String acadyear, Integer semester) {
		List<DyClassOtherCheck> checks = dyClassOtherCheckDao.findByClassId(classId,acadyear,semester);
		getClassName(checks);
		return checks;
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyClassOtherCheck, String> getJpaDao() {
		return dyClassOtherCheckDao;
	}

	@Override
	protected Class<DyClassOtherCheck> getEntityClass() {
		return DyClassOtherCheck.class;
	}

	public void getClassName(List<DyClassOtherCheck> checks) {
		List<String> classIdList = EntityUtils.getList(checks, "classId");
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIdList.toArray(new String[]{})),new TR<List<Clazz>>(){}); 
		Map<String,String> classNameMap = EntityUtils.getMap(clazzs, "id", "classNameDynamic");
		for (DyClassOtherCheck check : checks) {
			check.setClassName(classNameMap.get(check.getClassId()));
		}
	}

}
