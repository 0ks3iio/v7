package net.zdsoft.eclasscard.data.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccClientLogDao;
import net.zdsoft.eclasscard.data.dto.FaceGroupDTO;
import net.zdsoft.eclasscard.data.entity.EccClientLog;
import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.eclasscard.data.service.EccClientLogService;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccUserFaceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
@Service("eccClientLogService")
public class EccClientLogServiceImpl  extends BaseServiceImpl<EccClientLog, String>implements EccClientLogService{

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccUserFaceService eccUserFaceService;
	@Autowired
	private EccClientLogDao eccClientLogDao;
	@Override
	protected BaseJpaRepositoryDao<EccClientLog, String> getJpaDao() {
		return eccClientLogDao;
	}

	@Override
	protected Class<EccClientLog> getEntityClass() {
		return EccClientLog.class;
	}

	@Override
	public void saveClientLog(String cardId, String filename,String fileType, MultipartFile file) {
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		String unitId = net.zdsoft.framework.entity.Constant.GUID_ONE;
		if(eccInfo!=null){
			unitId = eccInfo.getUnitId();
		}
		String logId = UuidUtils.generateUuid();
		if("2".equals(fileType)){
			filename = logId;
		}
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
//		fileSystemPath="G:\\11";
		File dir = new File(fileSystemPath);
		if (!dir.exists())// 目录不存在则创建
			dir.mkdirs();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());

		String filePath = "ecclogs" + File.separator
				+ unitId + File.separator + ymd+ File.separator + cardId;
		String savePath = fileSystemPath + File.separator + filePath;
		File f = null;
		try {
			File destFile = new File(savePath);
			if (!destFile.exists()) {
				destFile.mkdirs();
			}
			String tfilePath = destFile.getAbsolutePath() + File.separator
					+ filename;
			f = new File(tfilePath);
			file.transferTo(f);
			f.createNewFile();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		EccClientLog clog = new EccClientLog();
		clog.setCreationTime(new Date());
		clog.setFileName(filename);
		clog.setFilePath(filePath);
		clog.setId(logId);
		clog.setInfoId(cardId);
		clog.setModifyTime(new Date());
		clog.setUnitId(unitId);
		save(clog);
		if("2".equals(fileType) && f!=null){
			analyzeFile(f,cardId,unitId);
		}
	}

	private void analyzeFile(File f, String cardId,String unitId) {
		try {
			String input = FileUtils.readFileToString(f, "UTF-8");
			FaceGroupDTO faceGroupDTO = new Gson().fromJson(input, FaceGroupDTO.class);
			Set<String> ids = EntityUtils.getSet(faceGroupDTO.getCurrentFaceInfos(), FaceGroupDTO.FaceInfo::getFaceId);
			EccFaceActivate face = eccFaceActivateService.findByInfoId(unitId,cardId);
			int curentNum = ids.size();
			face.setCurrentFaceNum(curentNum);
			face.setIsLower(0);
			face.setLastClientTime(new Date(faceGroupDTO.getLastLowerTime()));
			Set<String> failIds = EntityUtils.getSet(faceGroupDTO.getFailedFaceInfos(), FaceGroupDTO.FaceInfo::getFaceId);
			if(CollectionUtils.isNotEmpty(failIds)){
				List<EccUserFace> faces = eccUserFaceService.findByOwnerIds(failIds.toArray(new String[failIds.size()]));
				for(EccUserFace fa:faces){
					fa.setFailTimes(fa.getFailTimes()+1);
					fa.setModifyTime(new Date());
				}
				eccUserFaceService.saveAll(faces.toArray(new EccUserFace[faces.size()]));
			}
			if(CollectionUtils.isNotEmpty(ids)){
				List<EccUserFace> savefaces = Lists.newArrayList();
				List<EccUserFace> faces = eccUserFaceService.findByOwnerIds(ids.toArray(new String[ids.size()]));
				for(EccUserFace fa:faces){
					if(fa.getFailTimes()>0){
						fa.setFailTimes(fa.getFailTimes()-1);
						savefaces.add(fa);
					}
				}
				if(CollectionUtils.isNotEmpty(savefaces)){
					eccUserFaceService.saveAll(savefaces.toArray(new EccUserFace[savefaces.size()]));
				}
			}
//			List<EccUserFace> userFaces = eccUserFaceService.findByUnitIdPage(unitId,false,null);
//			Set<String> ufIds = EntityUtils.getSet(userFaces, EccUserFace::getOwnerId);
//			if(ufIds.size()>0 && ids.size()>ufIds.size()){
//				for(String id:ids){
//					if(!ufIds.contains(id)){
//						FaceUtils.deleteFace(id, unitId);
//					}
//				}
//				face.setNeedLower(1);
//			}
			eccFaceActivateService.save(face);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<EccClientLog> findEccClientLogByInfo(String cardId,
			Pagination page) {
		 Specification<EccClientLog> specification = new Specification<EccClientLog>() {

	            @Override
	            public Predicate toPredicate(Root<EccClientLog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	                List<Predicate> ps = new ArrayList<>();
                	ps.add(cb.equal(root.get("infoId").as(String.class), cardId));
	                	
	                List<Order> orderList = new ArrayList<>();
	                orderList.add(cb.desc(root.get("creationTime").as(Date.class)));

	                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
	            }
			};
	        if (page != null) {
	        	Pageable pageable = Pagination.toPageable(page);
	        	Page<EccClientLog> findAll = eccClientLogDao.findAll(specification, pageable);
	        	page.setMaxRowCount((int) findAll.getTotalElements());
	        	return findAll.getContent();
	        }
	        else {
	        	return eccClientLogDao.findAll(specification);
	        }
	}

	@Override
	public void deleteBeforeByDay(Date date) {
		 eccClientLogDao.deleteBeforeByDay(date);
	}


}
