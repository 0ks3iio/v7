package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccClientLog;
import net.zdsoft.framework.entity.Pagination;

import org.springframework.web.multipart.MultipartFile;

public interface EccClientLogService extends BaseService<EccClientLog, String>{
	public void saveClientLog(String cardId,String filename,String fileType,MultipartFile file);
	
	public List<EccClientLog> findEccClientLogByInfo(String cardId,Pagination page);

	public void deleteBeforeByDay(Date date);
}
