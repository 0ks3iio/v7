package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.DsFileDao;
import net.zdsoft.bigdata.data.entity.DsFile;
import net.zdsoft.bigdata.data.service.DsFileService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class DsFileServiceImpl extends BaseServiceImpl<DsFile, String> implements DsFileService {

	@Autowired
	private DsFileDao dsFileDao;
	@Resource
	private SysOptionRemoteService sysOptionRemoteService;


	@Override
	protected BaseJpaRepositoryDao<DsFile, String> getJpaDao() {
		return dsFileDao;
	}

	@Override
	protected Class<DsFile> getEntityClass() {
		return DsFile.class;
	}

	@Override
	public List<DsFile> findDsFileByUnitId(String unitId) {
		return dsFileDao.findDsFileByUnitId(unitId);
	}

	@Override
	public void saveDsFile(DsFile dsFile, MultipartFile file) throws IOException {
		if (file != null) {
			String systemFilePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
			String filePath = "bigdata" + File.separator + "dsFile" + File.separator + dsFile.getUnitId();

			String fileRealPath = systemFilePath + File.separator + filePath;
			File path = new File(fileRealPath);
			if (!path.exists()) {
				boolean dir = path.mkdirs();
				if (!dir) {
					throw new IllegalArgumentException("文件目录创建失败");
				}
			}

			file.transferTo(new File(path.getPath() + File.separator + file.getOriginalFilename()));
			dsFile.setFileName(file.getOriginalFilename());
			dsFile.setFilePath(filePath + File.separator + file.getOriginalFilename());
		}
		dsFile.setModifyTime(new Date());

		if (StringUtils.isBlank(dsFile.getId())) {
			dsFile.setCreationTime(new Date());
			dsFile.setId(UuidUtils.generateUuid());
			dsFileDao.save(dsFile);
			return;
		}

		if (file != null) {
			dsFileDao.update(dsFile, new String[]{"remark", "name", "filePath", "fileName", "type", "headType", "modifyTime"});
			return;
		}

		dsFileDao.update(dsFile, new String[]{"remark", "name", "type", "headType", "modifyTime"});
	}

	@Override
	public long count(Date start, Date end) {
		if (start == null && end == null) {
			return dsFileDao.count((Specification<DsFile>) (root, criteriaQuery, criteriaBuilder)
					-> criteriaQuery.getRestriction());
		} else {
			return dsFileDao.count((Specification<DsFile>) (root, criteriaQuery, criteriaBuilder)
					-> {
				Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
				return criteriaQuery.where(time).getRestriction();
			});
		}
	}
}
