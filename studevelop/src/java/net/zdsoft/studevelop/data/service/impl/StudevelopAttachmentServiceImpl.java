package net.zdsoft.studevelop.data.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.JsCropUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dao.StudevelopAttachmentDao;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment.StudevelopAttachmentObjType;
import net.zdsoft.studevelop.data.service.StudevelopActivityService;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
/**
 * studevelop_attachment
 * @author 
 * 
 */
@Service("studevelopAttachmentService")
public class StudevelopAttachmentServiceImpl extends BaseServiceImpl<StudevelopAttachment,String> implements StudevelopAttachmentService{
	@Autowired
	private StudevelopAttachmentDao studevelopAttachmentDao;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private StudevelopActivityService studevelopActivityService;

	public void saveAttachment(StudevelopAttachment attachment, MultipartFile file){
		if(file == null){
			throw new IllegalArgumentException("上传文件为空!");
		}
		if(file.getSize() == 0){
			throw new IllegalArgumentException("上传文件大小为空!");
		}
		attachment.setContenttype(file.getContentType());
		attachment.setFilename(file.getOriginalFilename());
		attachment.setExtName(StorageFileUtils.getFileExtension(file.getOriginalFilename()));
		attachment.setFilesize(file.getSize());
		try {
			saveAttachmentWithBytes(attachment, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAttachment(StudevelopAttachment attachment, MultipartFile multfile, File file){

		if(multfile == null){
			throw new IllegalArgumentException("上传文件为空!");
		}
		if(multfile.getSize() == 0){
			throw new IllegalArgumentException("上传文件大小为空!");
		}
//		Thumbnails.of(file).scale(1f).outputQuality(0.5f).toFile(file);
		attachment.setContenttype(multfile.getContentType());
		attachment.setFilename(multfile.getOriginalFilename());
		attachment.setExtName(StorageFileUtils.getFileExtension(multfile.getOriginalFilename()));
		attachment.setFilesize(multfile.getSize());
		try{
			saveAttachmentWithScaleQuality(attachment,file,1f,0.5f);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void saveAttachmentWithScaleQuality(StudevelopAttachment att ,File file , double scole , double quality){

		// 更新附件表，先删除原附件文件（一个附件一个目录文件夹，删除整个文件夹）
		deleteFile(att);

		List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), new TR<List<StorageDir>>(){});
		if(CollectionUtils.isEmpty(dirs)){
			return;
		}
		StorageDir dir = dirs.get(0);
//		dir.setDir("D://store");
		att.setDirId(dir.getId());
		Date now = new Date();
		String year = DateUtils.date2String(now, "yyyy");
		if(StringUtils.isBlank(att.getId())){
			att.setId(UuidUtils.generateUuid());
		}
		att.setCreationTime(now);
		att.setModifyTime(now);
		String filePath = StuDevelopConstant.ATTACHMENT_FILEPATH + File.separator + att.getObjecttype()
				+ File.separator + att.getUnitId() + File.separator + year;
		String ranId = getRangeIdByType(att);
		if(StringUtils.isNotEmpty(ranId)) {
			filePath += (File.separator + ranId);
		}
		filePath += (File.separator + att.getObjId() + File.separator + att.getId());
		String dirPath = dir.getDir() + File.separator + filePath;
		File dirFile = new File(dirPath);
		dirFile.mkdirs();
//		File orFile = new File(dirPath + File.separator+  StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName());
		String orFileStr = dirPath + File.separator+  StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName();
		try {
			Thumbnails.of(file).scale(scole).outputQuality(quality).toFile(orFileStr);
			StudevelopAttachmentObjType objType = StudevelopAttachmentObjType.getType(att.getObjecttype());
			if(objType != null){
				Thumbnails.of(file).size(objType.getWight(),objType.getHeight()).outputQuality(quality).toFile(dirPath + File.separator+ att.getId()+"."+att.getExtName());
			}
			att.setFilePath(filePath + File.separator+ att.getId()+"."+att.getExtName());
			studevelopAttachmentDao.save(att);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 获取附件对象（班级、学生、学校）id
	 * @param att
	 * @return
	 */
	private String getRangeIdByType(StudevelopAttachment att) {
		// TODO
		if(StuDevelopConstant.isActAttachment(att.getObjecttype())) {
			StudevelopActivity act = studevelopActivityService.findOne(att.getObjId());
			if(act != null) {
				return act.getRangeId();
			}
			return null;
		}
		return null;
	}
	
	private void saveAttachmentWithBytes(StudevelopAttachment att, byte[] bts){
		if(ArrayUtils.isEmpty(bts)){
			throw new IllegalArgumentException("上传文件为空或者文件大小为空!");
		}
		// 更新附件表，先删除原附件文件（一个附件一个目录文件夹，删除整个文件夹）
		deleteFile(att);
		
		List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), new TR<List<StorageDir>>(){});
		if(CollectionUtils.isEmpty(dirs)){
			return;
		}
		StorageDir dir = dirs.get(0);
		att.setDirId(dir.getId());
		Date now = new Date();
		String year = DateUtils.date2String(now, "yyyy");
		if(StringUtils.isBlank(att.getId())){
			att.setId(UuidUtils.generateUuid());
		}
		att.setCreationTime(now);
		att.setModifyTime(now);
		String filePath = StuDevelopConstant.ATTACHMENT_FILEPATH + File.separator + att.getObjecttype()
				+ File.separator + att.getUnitId() + File.separator + year;
		String ranId = getRangeIdByType(att);
		if(StringUtils.isNotEmpty(ranId)) {
			filePath += (File.separator + ranId);
		}
		filePath += (File.separator + att.getObjId() + File.separator + att.getId());
//		dir.setDir("D://store");
		String dirPath = dir.getDir() + File.separator + filePath;
		File dirFile = new File(dirPath);
		dirFile.mkdirs();
		File orFile = new File(dirPath + File.separator+ StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName());
		try {

			FileUtils.writeByteArrayToFile(orFile, bts);
			StudevelopAttachmentObjType objType = StudevelopAttachmentObjType.getType(att.getObjecttype());
			if(objType != null){
				BufferedImage zoomImage = JsCropUtils.zoom(orFile, objType.getWight(), objType.getHeight());
				File desFile = new File(dirPath + File.separator+ att.getId()+"."+att.getExtName());
				FileUtils.writeByteArrayToFile(desFile, JsCropUtils.toBytes(zoomImage));
				
				/*BufferedImage zoomImageMobile =JsCropUtils.zoom(zoomImage, 0.5);
				File mobileFile = new File(dirPath + File.separator+ StuDevelopConstant.PIC_MOBILE_NAME+"."+att.getExtName());
				FileUtils.writeByteArrayToFile(mobileFile, JsCropUtils.toBytes(zoomImageMobile));*/
			}
			att.setFilePath(filePath + File.separator+ att.getId()+"."+att.getExtName());
			studevelopAttachmentDao.save(att);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void saveAttachment(StudevelopAttachment attachment, File oriFile){
		if(oriFile == null || !oriFile.exists()){
			throw new IllegalArgumentException("上传文件为空!");
		}
		if(oriFile.length() == 0){
			throw new IllegalArgumentException("上传文件大小为空!");
		}
		attachment.setFilename(oriFile.getName());
		attachment.setExtName(StorageFileUtils.getFileExtension(oriFile.getName()));
		attachment.setFilesize(oriFile.length());
		try {
			attachment.setContenttype(Files.probeContentType(Paths.get(oriFile.toURI())));
			saveAttachmentWithBytes(attachment, FileUtils.readFileToByteArray(oriFile));
			FileUtils.forceDelete(oriFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteFile(StudevelopAttachment attachment){
		if(StringUtils.isNotEmpty(attachment.getDirId())
				&& StringUtils.isNotEmpty(attachment.getFilePath())){
			try {
				StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(attachment.getDirId()), StorageDir.class);
				if(dir != null){
					File img = new File(dir.getDir() + File.separator + attachment.getFilePath());
					if (img != null && img.exists()) {
						String dirPath = img.getParent();
						File dirFile = new File(dirPath);
						FileUtils.deleteDirectory(dirFile);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Integer delete(String[] ids) {
		if(ArrayUtils.isEmpty(ids)) {
			return 0;
		}
		studevelopAttachmentDao.deleteByIds(ids);
		return null;
	}
	
	/**
	 * 删除附件及文件
	 * @param atts
	 */
	private void deleteWithEntity(List<StudevelopAttachment> atts){
		if(CollectionUtils.isEmpty(atts)){
			return ;
		}
		Set<String> ids = EntityUtils.getSet(atts, StudevelopAttachment::getId);
		studevelopAttachmentDao.deleteByIds(ids.toArray(new String[0]));
	}
	
	public void deleteByObjIds(String[] objIds){
		deleteWithEntity(studevelopAttachmentDao.getAttachmentByObjIds(objIds));
	}
	
	public List<StudevelopAttachment> findListByObjIds(String... objId){
		return studevelopAttachmentDao.findListByObjIds(objId);
	}
	public 	List<StudevelopAttachment> findListByObjIds(String acadyear,String semester,String... objId){
		return studevelopAttachmentDao.findListByObjIds(acadyear,semester,objId);
	}
	public 	Integer deleteByObjIds(String acadyear,String semester,String... objId){
		return studevelopAttachmentDao.deleteByObjIds(acadyear,semester,objId);
	}
	
	public Map<String, List<StudevelopAttachment>> findMapByObjIds(String...objId){
		Map<String, List<StudevelopAttachment>> map = new HashMap<String, List<StudevelopAttachment>>();
		
		List<StudevelopAttachment> list = this.findListByObjIds(objId);
		Set<String> dirIds = EntityUtils.getSet(list, StudevelopAttachment::getDirId);
		List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findListByIds(dirIds.toArray(new String[0])), new TR<List<StorageDir>>(){});
		if(CollectionUtils.isEmpty(dirs)){
			return map;
		}
		Map<String, String> dirPathMap = EntityUtils.getMap(dirs, StorageDir::getId, StorageDir::getDir);
		
		Iterator<StudevelopAttachment> iterator = list.iterator();
		while(iterator.hasNext()){
			StudevelopAttachment ent = iterator.next();
			
			String dir = dirPathMap.get(ent.getDirId());
			if(StringUtils.isEmpty(dir)){
				continue;
			}
//			dir = "D://store";

			File img = new File(dir + File.separator + ent.getFilePath());
			if(img.exists()) {
				ent.setSmallFile(img);
				ent.setSmallFullPath(img.getAbsolutePath());
				
				String dirPath = img.getParent();
				String originFilePath = dirPath + File.separator + StuDevelopConstant.PIC_ORIGIN_NAME+"."+ent.getExtName();
				File ori = new File(originFilePath);
				if(ori.exists()){
					ent.setOriginFilePath(originFilePath);
					ent.setOriginFile(ori);
				} else {
					System.out.println("原图不存在="+originFilePath);
					ent.setOriginFile(img);
					ent.setOriginFilePath(img.getAbsolutePath());
				}
				
				if(map.containsKey(ent.getObjId())){
					map.get(ent.getObjId()).add(ent);
				}else{
					List<StudevelopAttachment> l = new ArrayList<StudevelopAttachment>();
					l.add(ent);
					map.put(ent.getObjId(), l);
				}
			} else {
				System.out.println("smallimg不存在="+(dir + File.separator + ent.getFilePath()));
			}
		}
		return map;
	}

	public int findAttachmentNumByObjId(String objId, String objType){
		Integer num = studevelopAttachmentDao.findAttachmentNumByObjId(objId, objType);
		if(num != null && num > 0){
			return num;
		}
		return 0;
	}
	@Override
	public List<StudevelopAttachment> getAttachmentByObjId(String objId, String objType){
		List<StudevelopAttachment> atts = studevelopAttachmentDao.getAttachmentByObjId(objId, objType);
		List<StudevelopAttachment> dtos = new ArrayList<StudevelopAttachment>();
		if(CollectionUtils.isEmpty(atts)){
			return dtos;
		}
		Set<String> dirIds = EntityUtils.getSet(atts, StudevelopAttachment::getDirId);
		List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findListByIds(dirIds.toArray(new String[0])), new TR<List<StorageDir>>(){});
		if(CollectionUtils.isEmpty(dirs)){
			return dtos;
		}
		Map<String, String> dirPathMap = EntityUtils.getMap(dirs, StorageDir::getId, StorageDir::getDir);
		Iterator<StudevelopAttachment> iterator = atts.iterator();
		while(iterator.hasNext()){
			StudevelopAttachment att = iterator.next();
			att.setFilePath(UrlUtils.ignoreFirstLeftSlash(att.getFilePath()));
			String dir = dirPathMap.get(att.getDirId());
			if(StringUtils.isEmpty(dir)){
				continue;
			}
//			dir = "D://store";
			File img = new File(dir + File.separator + att.getFilePath());
			if(img.exists()) {
				att.setSmallFile(img);
				att.setSmallFullPath(img.getAbsolutePath());
				
				String dirPath = img.getParent();
				String originFilePath = dirPath + File.separator + StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName();
				File ori = new File(originFilePath);
				if(ori.exists()){
					att.setOriginFile(ori);
					att.setOriginFilePath(originFilePath);
				} else {
					System.out.println("原图不存在="+originFilePath);
					att.setOriginFile(img);
					att.setOriginFilePath(img.getAbsolutePath());
				}
			} else {
				System.out.println("smallimg不存在="+(dir + File.separator + att.getFilePath()));
//				iterator.remove();
			}
		}
		return atts;
	}
	@Override
	public void saveFile(String objId,String objType,String insertId){
		List<StudevelopAttachment> atts =getAttachmentByObjId(objId, objType);
		if(CollectionUtils.isNotEmpty(atts)){
			StudevelopAttachment att = atts.get(0);
			List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), new TR<List<StorageDir>>(){});
			if(CollectionUtils.isNotEmpty(dirs)){
				String dir=dirs.get(0).getDir();
				File file=new File(dir+File.separator+att.getFilePath());
				String parentPath=file.getParent();
				File originFile=new File(parentPath+File.separator+StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName());
				StudevelopAttachment insertAtt = new StudevelopAttachment();
				insertAtt.setObjecttype(objType);
				insertAtt.setObjId(insertId);
				insertAtt.setUnitId(att.getUnitId());
				//不能删除原文件 此为copy的过程
				if(originFile == null || !originFile.exists()){
					throw new IllegalArgumentException("上传文件为空!");
				}
				if(originFile.length() == 0){
					throw new IllegalArgumentException("上传文件大小为空!");
				}
				insertAtt.setFilename(originFile.getName());
				insertAtt.setExtName(StorageFileUtils.getFileExtension(originFile.getName()));
				insertAtt.setFilesize(originFile.length());
				try {
					insertAtt.setContenttype(Files.probeContentType(Paths.get(originFile.toURI())));
					saveAttachmentWithBytes(insertAtt, FileUtils.readFileToByteArray(originFile));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	@Override
	protected BaseJpaRepositoryDao<StudevelopAttachment, String> getJpaDao() {
		return studevelopAttachmentDao;
	}

	@Override
	protected Class<StudevelopAttachment> getEntityClass() {
		return StudevelopAttachment.class;
	}


}