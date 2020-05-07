package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dao.DatabaseDao;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service("databaseService")
public class DatabaseServiceImpl extends BaseServiceImpl<Database, String>
		implements DatabaseService {

	@Autowired
	private DatabaseDao databaseDao;

	@Resource
	private BigLogService bigLogService;

	@Override
	protected BaseJpaRepositoryDao<Database, String> getJpaDao() {
		return databaseDao;
	}

	@Override
	protected Class<Database> getEntityClass() {
		return Database.class;
	}

	@Override
	public List<Database> findDatabasesByUnitId(String unitId) {
		return databaseDao.findDatabasesByUnitId(unitId);
	}

	@Override
	public void saveDatabase(Database database) {
		if (StringUtils.isNotBlank(database.getId())) {
			Database oldDatabase = databaseDao.findById(database.getId()).get();
			// 更新数据源
			database.setThumbnail(DatabaseType.parse(database.getType())
					.getThumbnail());
			database.setModifyTime(new Date());
			update(database, database.getId(), new String[] { "name", "unitId",
					"type", "domain", "port", "dbName", "username", "password",
					"thumbnail", "characterEncoding","remark", "modifyTime" });
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-database");
			logDto.setDescription("数据源 "+database.getName());
			logDto.setOldData(oldDatabase);
			logDto.setNewData(database);
			logDto.setBizName("数据源管理");
			bigLogService.updateLog(logDto);

		} else {
			// 新增数据源
			database.setId(UuidUtils.generateUuid());
			database.setCreationTime(new Date());
			database.setModifyTime(new Date());
			database.setThumbnail(DatabaseType.parse(database.getType())
					.getThumbnail());
			save(database);
			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-database");
			logDto.setDescription("数据源 "+database.getName());
			logDto.setNewData(database);
			logDto.setBizName("数据源管理");
			bigLogService.insertLog(logDto);
		}
	}

	@Override
	public void deleteDatabase(String id) {
		delete(id);
	}

	@Override
	public long count(Date start, Date end) {
		if (start == null && end == null) {
			return databaseDao.count((Specification<Database>) (root, criteriaQuery, criteriaBuilder)
					-> criteriaQuery.getRestriction());
		} else {
			return databaseDao.count((Specification<Database>) (root, criteriaQuery, criteriaBuilder)
					-> {
				Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
				return criteriaQuery.where(time).getRestriction();
			});
		}
	}

}
