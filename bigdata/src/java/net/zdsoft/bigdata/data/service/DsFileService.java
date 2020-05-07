package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.DsFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface DsFileService extends BaseService<DsFile, String>{

	/**
	 * 根据单位获取数据源
	 * @param unitId
	 * @return
	 */
	List<DsFile> findDsFileByUnitId(String unitId);

	/**
	 * 保存文件
	 * @param dsFile
	 * @param file
	 */
    void saveDsFile(DsFile dsFile, MultipartFile file) throws IOException;

	long count(Date start, Date end);
}
