package net.zdsoft.datareport.data.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.dao.DataReportObjDao;
import net.zdsoft.datareport.data.entity.DataReportObj;
import net.zdsoft.datareport.data.service.DataReportObjService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("dataReportObjService")
public class DataReportObjServiceImpl extends BaseServiceImpl<DataReportObj,String> implements DataReportObjService{

	@Autowired
	private DataReportObjDao dataReportObjDao;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	@Override
	public List<DataReportObj> findByReportIds(String[] infoIds, boolean needName) {
		List<DataReportObj> dataReportObjs = dataReportObjDao.findByReportIds(infoIds);
		if (needName) {
			getObjectName(dataReportObjs);
		}
		return dataReportObjs;
	}
	
	@Override
	public DataReportObj findById(String objId) {
		List<DataReportObj> dataObj = dataReportObjDao.findOneById(objId);
		getObjectName(dataObj);
		return dataObj.get(0);
	}

	
	@Override
	public List<DataReportObj> findByObjectId(String objectId) {
		return dataReportObjDao.findByObjectId(objectId);
	}
	
	@Override
	public void deleteByReportId(String reportId) {
		dataReportObjDao.deleteByReportId(reportId);
	}
	
	@Override
	public List<DataReportObj> findByObjIds(String[] objIds) {
		List<DataReportObj> dataObj = dataReportObjDao.findByObjIds(objIds);
		getObjectName(dataObj);
		return dataObj;
	}
	
	private void getObjectName(List<DataReportObj> dataReportObjs) {
		if (CollectionUtils.isNotEmpty(dataReportObjs)) {
			Integer objectType = dataReportObjs.get(0).getObjectType();
			String[] objIds = EntityUtils.getArray(dataReportObjs, DataReportObj::getObjectId, String[]::new);
			Map<String,String> idNameMap = null;
			if (Objects.equals(ReportConstants.OBJECT_TYPE_1, objectType)) {
				List<Unit> units = SUtils.dt(unitRemoteService.findListByIds(objIds),new TypeReference<List<Unit>>() {});
				idNameMap = EntityUtils.getMap(units, Unit::getId, Unit::getUnitName);
			}
			if (Objects.equals(ReportConstants.OBJECT_TYPE_2, objectType)) {
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(objIds),new TypeReference<List<Teacher>>() {});
				idNameMap = EntityUtils.getMap(teachers, Teacher::getId, Teacher::getTeacherName);
			}
			for (DataReportObj obj : dataReportObjs) {
				obj.setObjectName(idNameMap.get(obj.getObjectId()));
			}
		}
	}
	
	@Override
	protected BaseJpaRepositoryDao<DataReportObj, String> getJpaDao() {
		return dataReportObjDao;
	}
	
	@Override
	protected Class<DataReportObj> getEntityClass() {
		return DataReportObj.class;
	}
}
