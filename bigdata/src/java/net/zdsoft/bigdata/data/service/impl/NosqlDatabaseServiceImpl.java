package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.NosqlDatabaseDao;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service("nosqlDatabaseService")
public class NosqlDatabaseServiceImpl extends
		BaseServiceImpl<NosqlDatabase, String> implements NosqlDatabaseService {

	@Autowired
	private NosqlDatabaseDao nosqlDatabaseDao;
	@Resource
	private SysOptionRemoteService sysOptionRemoteService;

	@Override
	protected BaseJpaRepositoryDao<NosqlDatabase, String> getJpaDao() {
		return nosqlDatabaseDao;
	}

	@Override
	protected Class<NosqlDatabase> getEntityClass() {
		return NosqlDatabase.class;
	}

	@Override
	public List<NosqlDatabase> findNosqlDatabasesByUnitId(String unitId) {
		return nosqlDatabaseDao
				.findNosqlDatabaseByUnitIdOrderByModifyTimeDesc(unitId);
	}

	@Override
	public List<NosqlDatabase> findNosqlDatabasesByUnitIdAndTypes(String unitId) {
		return nosqlDatabaseDao.findNosqlDatabasesByUnitIdAndTypes(unitId);
	}

	@Override
	public List<NosqlDatabase> findNosqlDatabasesByUnitIdAndType(String unitId, String type) {
		return nosqlDatabaseDao.findNosqlDatabasesByUnitIdAndType(unitId, type);
	}

	@Override
	public void saveNosqlDatabase(NosqlDatabase nosqlDatabase)
			throws IOException {
		nosqlDatabase.setModifyTime(new Date());
		if (StringUtils.isNotBlank(nosqlDatabase.getKeytabFile())) {
			String systemFilePath = sysOptionRemoteService
					.findValue(Constant.FILE_PATH);
			// 拷贝模版
			String tempPath = nosqlDatabase.getKeytabFile();
			String fileName = tempPath.substring(tempPath.lastIndexOf("/"));
			String templatePath = "bigdata" + File.separator + "hive"
					+ File.separator + nosqlDatabase.getUnitId()
					+ File.separator + fileName;
			String templateRealPath = systemFilePath + File.separator
					+ templatePath;
			copyFile(tempPath, templateRealPath);
			nosqlDatabase.setKeytabFile(templatePath);
		}
		if (StringUtils.isBlank(nosqlDatabase.getId())) {
			nosqlDatabase.setCreationTime(new Date());
			nosqlDatabase.setId(UuidUtils.generateUuid());
			nosqlDatabaseDao.save(nosqlDatabase);
			return;
		}
		nosqlDatabaseDao
				.update(nosqlDatabase, new String[] { "name", "remark",
						"modifyTime", "type", "userName", "password", "domain",
						"port", "nameSpace", "dbName", "thumbnail",
						"connectMode", "authWay", "serverPrincipal",
						"clientPrincipal", "keytabFile", "initSql" });
	}

	@Override
	public long count(Date start, Date end) {
		if (start == null && end == null) {
			return nosqlDatabaseDao.count((Specification<NosqlDatabase>) (root, criteriaQuery, criteriaBuilder)
					-> criteriaQuery.getRestriction());
		} else {
			return nosqlDatabaseDao.count((Specification<NosqlDatabase>) (root, criteriaQuery, criteriaBuilder)
					-> {
				Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
				return criteriaQuery.where(time).getRestriction();
			});
		}
	}

	private void copyFile(String tempPath, String realPath) throws IOException {
		if (tempPath.equalsIgnoreCase(realPath))
			return;
		File tempFile = new File(tempPath);
		File realFile = new File(realPath);
		if (tempFile.exists()) {
			FileUtils.copyFile(tempFile, realFile);
		}
	}
}
