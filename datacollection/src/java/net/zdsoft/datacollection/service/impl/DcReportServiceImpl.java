package net.zdsoft.datacollection.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datacollection.dao.DcReportDao;
import net.zdsoft.datacollection.dao.DcReportJdbcDao;
import net.zdsoft.datacollection.entity.DcDataModel;
import net.zdsoft.datacollection.entity.DcOperationData;
import net.zdsoft.datacollection.entity.DcReport;
import net.zdsoft.datacollection.service.DcReportService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service
public class DcReportServiceImpl extends BaseServiceImpl<DcReport, String> implements DcReportService {

	@Autowired
	private DcReportJdbcDao dcReportJdbcDao;

	@Autowired
	private DcReportDao dcReportDao;

	@Override
	public boolean checkReportTable(String tableName) {
		return dcReportJdbcDao.checkReportTable(tableName);
	}

	@Override
	public boolean createReportTable(JSONObject data) {
		return dcReportJdbcDao.createReportTable(data);
	}

	@Override
	public void saveReportData(JSONObject data) {
		JsonArray array = new JsonArray();

		String dataId = data.getString("_dataId");
		String reportCode = data.getString("_reportCode");
		Set<String> columns = new HashSet<String>();
		int count = 0;
		for (String key : data.keySet()) {
			if (StringUtils.contains(key, "@")) {
				String columnName = StringUtils.substringBefore(key, "@");
				columns.add(columnName);
				int _count = NumberUtils.toInt(StringUtils.substringAfter(key, "@"));
				if (count < _count)
					count = _count;
			}
		}
		for (int i = 0; i < count; i++) {
			JSONObject dataM = new JSONObject();
			dataM.put("_dataId", dataId);
			dataM.put("_reportCode", "dc_data_m_" + reportCode);
			dataM.put("data_id", dataId);
			dataM.put("id", UuidUtils.generateUuid());
			dataM.put("report_code", reportCode);
			for (String columnName : columns) {
				dataM.put(columnName, data.getString(columnName + "@" + (i + 1)));
			}
			array.add(dataM);
		}

		String[] ks = data.keySet().toArray(new String[0]);
		for (String key : ks) {
			if (StringUtils.contains(key, "@")) {
				data.remove(key);
			}
		}

		dcReportJdbcDao.saveReportData(data);
		if (NumberUtils.isNumber(reportCode))
			dcReportJdbcDao.remoteReportMByDataId(JsonUtils.getString(data, "_reportCode"),
					JsonUtils.getString(data, "_dataId"));
		dcReportJdbcDao.saveReportData(array);

	}

	@Override
	protected BaseJpaRepositoryDao<DcReport, String> getJpaDao() {
		return dcReportDao;
	}

	@Override
	protected Class<DcReport> getEntityClass() {
		return DcReport.class;
	}

	@Override
	public JSONObject findData(String tableName, String dataId) {
		return dcReportJdbcDao.findData(tableName, dataId);
	}

	@Override
	public List<JSONObject> findDataByUnitId(String tableName, String unitId) {
		return dcReportJdbcDao.findDataByUnitId(tableName, unitId);
	}

	@Override
	public List<JSONObject> findDataByUserId(String tableName, String userId) {
		return dcReportJdbcDao.findDataByUserId(tableName, userId);
	}

	@Override
	public boolean createReportMTable(JSONObject data) {
		return dcReportJdbcDao.createReportMTable(data);
	}

	@Override
	public JSONArray findMData(String tableName, String dataId) {
		return dcReportJdbcDao.findMData(tableName, dataId);
	}

	@Override
	public List<JSONObject> findDatas(String tableName, String oderBys[], DcDataModel dcDataModel, Integer top) {
		List<DcOperationData> dods = dcDataModel.getDcOperationDatas();
		DeptRemoteService deptRemoteService = Evn.getBean("deptRemoteService");
		List<Dept> depts = new ArrayList<>();
		Map<String, List<Dept>> map = new HashMap<>();
		for(DcOperationData dod : dods){
			String columnName = dod.getColumnName();
			if(StringUtils.equalsIgnoreCase("unit_id", columnName)){
				depts = SUtils.dt(deptRemoteService.findByUnitId(String.valueOf(dod.getDataValue())), new TR<List<Dept>>(){});
				break;
			}
		}
		
		for(Dept dept : depts){
			String deptId = dept.getParentId();
			List<Dept> subDepts = map.get(deptId);
			if(subDepts == null){
				subDepts = new ArrayList<>();
				map.put(deptId, subDepts);
			}
			subDepts.add(dept);
		}
		
		for(DcOperationData dod : dods){
			String columnName = dod.getColumnName();
			if(StringUtils.equalsIgnoreCase("dept_id", columnName)){
				String dataValue = String.valueOf(dod.getDataValue());
				List<String> deptIds = cycDept(dataValue, map);
				if(!deptIds.contains(dataValue))
					deptIds.add(dataValue);
				dod.setOperation(" IN ");
				dod.setDataValue(StringUtils.join(deptIds, ","));
			}
		}
		return dcReportJdbcDao.findDatas(tableName, oderBys, dcDataModel, top);
	}
	
	private List<String> cycDept(String deptId, Map<String, List<Dept>> map){
		List<String> ds = new ArrayList<>();
		List<Dept> depts = map.get(deptId);
		if(depts == null){
			return Arrays.asList(deptId);
		}
		for(Dept dept : depts){
			String id = dept.getId();
			ds.addAll(cycDept(id, map));
			if(!ds.contains(id))
			ds.add(id);
		}
		return ds;
	}

	@Override
	public void removeByDataId(String tableName, String dataId) {
		dcReportJdbcDao.remoteReportDataById(tableName, dataId);
	}

	@Override
	public int findMaxCode(String codePrefix) {
		return dcReportJdbcDao.findMaxCode(codePrefix);
	}
}
