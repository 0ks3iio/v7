package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface NosqlDatabaseService extends BaseService<NosqlDatabase, String>{


	/**
	 * 根据单位获取database数据源
	 * @param unitId
	 * @return
	 */
	List<NosqlDatabase> findNosqlDatabasesByUnitId(String unitId);
	
	
	/**
	 * 根据单位和类型获取database数据源(这里的types根据业务需要固定）
	 * @param unitId
	 * @return
	 */
	List<NosqlDatabase> findNosqlDatabasesByUnitIdAndTypes(String unitId);

	/**
	 * 根据单位和类型获取database数据源
	 * @param unitId
	 * @return
	 */
	List<NosqlDatabase> findNosqlDatabasesByUnitIdAndType(String unitId, String type);

	void saveNosqlDatabase(NosqlDatabase nosqlDatabase) throws IOException;

	long count(Date start, Date end);
}
