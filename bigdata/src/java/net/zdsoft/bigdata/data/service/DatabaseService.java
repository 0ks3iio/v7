package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.Database;

import java.util.Date;
import java.util.List;

public interface DatabaseService extends BaseService<Database, String>{

	/**
	 * 根据单位获取database数据源
	 * @param unitId
	 * @return
	 */
	public List<Database> findDatabasesByUnitId(String unitId);
	
	/**
	 * 保存database数据源
	 * @param database
	 * @return
	 */
	public void saveDatabase(Database database);
	
	/**
	 * 删除database数据源
	 * @param id 
	 * @return
	 */
	public void deleteDatabase(String id);

	long count(Date start, Date end);
}
