package net.zdsoft.bigdata.datax.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.DataxJobIns;
import net.zdsoft.bigdata.datax.entity.DataxJobInsParam;

import java.util.List;
import java.util.Map;

public interface DataxJobInsParamService extends BaseService<DataxJobInsParam, String> {

    List<DataxJobInsParam> findDataxJobParamByJobInstanceId(String jobInstanceId);

    void deleteByJobInsId(String jobInsId);

    void saveDataxJobInsParamList(List<DataxJobInsParam> dataxJobInsParamList);

    void deleteByJobId(String jobId);

    void saveDataxJobInsParam(Map<String, String> pMap, DataxJobIns dataxJobIns, List<DataxJobInsParam> dataxJobInsParamList) throws BigDataBusinessException;
}
