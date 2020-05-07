package net.zdsoft.datacollection.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datacollection.entity.DcDataModel;
import net.zdsoft.datacollection.entity.DcReport;

public interface DcReportService extends BaseService<DcReport, String> {

	boolean checkReportTable(String tableName);

	boolean createReportTable(JSONObject data);

	boolean createReportMTable(JSONObject data);

	void saveReportData(JSONObject data);

	int findMaxCode(String codePrefix);

	JSONObject findData(String tableName, String dataId);

	JSONArray findMData(String tableName, String dataId);

	List<JSONObject> findDatas(String tableName, String oderBys[], DcDataModel dcDataModel, Integer top);

	List<JSONObject> findDataByUnitId(String tableName, String unitId);

	List<JSONObject> findDataByUserId(String tableName, String userId);

	void removeByDataId(String tableName, String dataId);
}
