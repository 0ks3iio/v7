package net.zdsoft.datacollection.dao;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.datacollection.entity.DcDataModel;
import net.zdsoft.framework.entity.JsonArray;

public interface DcReportJdbcDao {

	boolean checkReportTable(String tableName);

	boolean createReportTable(JSONObject data);

	boolean createReportMTable(JSONObject data);

	void saveReportData(JSONObject data);

	void saveReportData(JsonArray datas);

	void remoteReportMByDataId(String reportCode, String dataId);

	void remoteReportDataById(String reportCode, String id);

	JSONObject findData(String tableName, String dataId);

	List<JSONObject> findDatas(String tableName, String oderBys[], DcDataModel dcDataModel, Integer top);

	JSONArray findMData(String tableName, String dataId);

	List<JSONObject> findDataByUnitId(String tableName, String unitId);

	List<JSONObject> findDataByUserId(String tableName, String userId);

	int findMaxCode(String codePrefix);

}
