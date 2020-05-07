package net.zdsoft.remote.openapi.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.remote.openapi.dao.BaseCommonDao;
import net.zdsoft.remote.openapi.service.BaseCommonService;

@Service("baseCommonService")
public class BaseCommonServiceImpl implements BaseCommonService {

    @Autowired
    private BaseCommonDao baseCommonDao;

    @Override
    public List<Map<String, Object>> getDataMapByParamMap(String physicalTableName,
            Map<String, String> paramMap, Map<String, String> columnMap) {
    	if(physicalTableName.indexOf("#")!=-1){
    		return baseCommonDao.getDataMapByParamMapMT(physicalTableName, paramMap, columnMap);
    	}else{
    		return baseCommonDao.getDataMapByParamMap(physicalTableName, paramMap);
    	}
    }

    @Override
    public void execSql(String sql, Object[] objs) {
        baseCommonDao.execSql(sql, objs);
    }

    @Override
    public List<Object[]> querySql(String sql, Object[] objs) {
        return baseCommonDao.querySql(sql, objs);
    }
}
