package net.zdsoft.api.base.service.impl;

import java.util.List;
import java.util.Map;






import net.zdsoft.api.base.dao.ApiBaseCommonDao;
import net.zdsoft.api.base.service.ApiBaseCommonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiBaseCommonService")
public class ApiBaseCommonServiceImpl implements ApiBaseCommonService {

    @Autowired
    private ApiBaseCommonDao baseCommonDao;
    @Override
    public List<Map<String, Object>> getDataMapByParamMap(String physicalTableName,
            Map<String, String> paramMap, Map<String, String> columnMap, String metadataId) {
    	if(physicalTableName.indexOf("#")!=-1){
    		return baseCommonDao.getDataMapByParamMapMT(physicalTableName, paramMap, columnMap, metadataId);
    	}else{
    		return baseCommonDao.getDataMapByParamMap(physicalTableName, paramMap, metadataId);
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

	@Override
	public int updateData(String sql, List<Object[]> insertObjList,
			int[] argTypes, String metadataId) {
		return baseCommonDao.updateData(sql, insertObjList,argTypes, metadataId);
	}
}
