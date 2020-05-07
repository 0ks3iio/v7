package net.zdsoft.basedata.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.AttachmentDao;
import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.utils.AttachmentUtils;
import net.zdsoft.basedata.service.AttachmentService;
import net.zdsoft.basedata.service.StorageDirService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.utils.PathUtils;

@Service("attachmentService")
public class AttachmentServiceImpl extends BaseServiceImpl<Attachment, String>
		implements AttachmentService {
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private StorageDirService storageDirService;

	@Override
	protected BaseJpaRepositoryDao<Attachment, String> getJpaDao() {
		return attachmentDao;
	}

	@Override
	protected Class<Attachment> getEntityClass() {
		return Attachment.class;
	}
	
	@Override
	public List<Attachment> findAttachmentDirPathById(String... id) {
		List<Attachment> attachments = findListByIdIn(id);
		addDirPath(attachments);
		return attachments;
	}
	

	@Override
	public List<Attachment> findAttachmentDirPathByObjId(String... objectId) {
		List<Attachment> attachments = attachmentDao.findByObjectIdOrder(objectId);
		addDirPath(attachments);
		return attachments;
	}

	@Override
	public List<Attachment> saveAttachment(List<AttFileDto> fileDtos) throws Exception {
		List<Attachment> attachments = Lists.newArrayList();
		Date creationTime = new Date();
		String year = DateUtils.date2String(creationTime, "yyyy");
//		StorageDir storageDir = getStorageDir();
//		if (storageDir == null) {
//			throw new RuntimeException("沒有激活系统内置的公共目录，请联系管理员");
//		}
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		String storePath = PathUtils.getFilePath();// 文件系统store地址
		for (AttFileDto fileDto : fileDtos) {
			if(StringUtils.isBlank(fileDto.getObjectType())||StringUtils.isBlank(fileDto.getObjectUnitId())){
				throw new RuntimeException("objectType或objectUnitId不可为空");
			}
			//1.获取临时文件
			String srcPath = fileSystemPath + File.separator
					+ fileDto.getFilePath();
			File srcFile = new File(srcPath);
			//2.组装文件实际路径
			String attachmentId = UuidUtils.generateUuid();
			String fileType = FileUtils.getExtension(fileDto.getFileName());
			String randomName = attachmentId + "." + fileType;
			String filePath = fileDto.getStorageDirType().getSubdirectory()
					+ File.separator + fileDto.getObjectType() + File.separator
					+ fileDto.getObjectUnitId() + File.separator + year + File.separator + fileDto.getObjectId();
			File newfile = AttachmentUtils.getFileLocalPath(storePath, filePath,
					randomName);
			String contentType = new MimetypesFileTypeMap().getContentType(srcFile);
			//3.封装Attachment
			Attachment attachment = new Attachment();
			attachment.setId(attachmentId);
			attachment.setDirId(BaseConstants.ZERO_GUID);
			attachment.setDirPath(storePath);
			attachment.setCreationTime(new Date());
			attachment.setModifyTime(new Date());
			attachment.setObjecttype(fileDto.getObjectType());
			attachment.setUnitId(fileDto.getObjectUnitId());
			attachment.setObjId(fileDto.getObjectId());
			attachment.setExtName(fileType);
			attachment.setFilename(fileDto.getFileName());
			attachment.setStatus(fileDto.getConStatus());
			attachment.setFilePath(filePath + File.separator + randomName);
			attachment.setFilesize(srcFile.length());
			attachment.setContenttype(contentType);
			attachments.add(attachment);
			//4.剪切临时文件到实际路径
			AttachmentUtils.moveFile(srcFile, newfile);
		}
		// 保存attachment
		if (attachments.size() > 0) {
			saveAll(attachments.toArray(new Attachment[attachments.size()]));
		}
		return attachments;
	}

	private void addDirPath(List<Attachment> attachments){
		Set<String> dirIds = EntityUtils.getSet(attachments, "dirId");
		List<StorageDir> storageDirs = storageDirService.findListByIdIn(dirIds.toArray(new String[dirIds.size()]));
		Map<String,String> dirPathMap = EntityUtils.getMap(storageDirs, "id","dir");
		for(Attachment attachment:attachments){
			if(dirPathMap.containsKey(attachment.getDirId())){
				attachment.setDirPath(dirPathMap.get(attachment.getDirId()));
			}
		}
	}
	
//	private StorageDir getStorageDir() {
//		StorageDir storageDir = null;
//		List<StorageDir> storageDirs = storageDirService.findByTypeAndActiove(
//				StorageDirType.ATTACHMENT.getValue(), "1");
//		if (CollectionUtils.isNotEmpty(storageDirs)) {
//			storageDir = storageDirs.get(0);
//		} else {
//			List<StorageDir> publicStorageDirs = storageDirService
//					.findByTypeAndActiove(StorageDirType.PUBLIC.getValue(), "1");
//			storageDir = publicStorageDirs.get(0);
//		}
//		return storageDir;
//	}

	@Override
	public void deleteAttachments(String[] attachmentIds, String[] nameSuffixs)
			throws Exception {
		List<Attachment> attachments = findAttachmentDirPathById(attachmentIds);
		new Thread(new Runnable(){//异步删除文件
			@Override
			public void run() {
				for(Attachment attachment:attachments){
					File file;
					try {
						file = AttachmentUtils.getFileLocalPath(attachment.getDirPath(), attachment.getFilePath(),
								null);
						if(nameSuffixs!=null){
							for(String suffix:nameSuffixs){
								String suffixPath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), suffix);
								File suffixFile = AttachmentUtils.getFileLocalPath(attachment.getDirPath(),suffixPath,null);
								if(suffixFile.exists()){
									org.apache.commons.io.FileUtils.forceDelete(suffixFile);
								}
							}
						}
						if(file.exists()){
							org.apache.commons.io.FileUtils.forceDelete(file);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		deleteAll(attachments.toArray(new Attachment[attachments.size()]));
	}

	@Override
	public void updateFileNameById(String attachmentId, String filename) {
		attachmentDao.updateFileNameById(attachmentId, filename);
	}

	@Override
	public List<Attachment> saveAttachmentByUrls(List<AttFileDto> fileDtos)
			throws Exception {
		List<Attachment> attachments = Lists.newArrayList();
		Date creationTime = new Date();
		String year = DateUtils.date2String(creationTime, "yyyy");
//		StorageDir storageDir = getStorageDir();
//		if (storageDir == null) {
//			throw new RuntimeException("沒有激活系统内置的公共目录，请联系管理员");
//		}
		String storePath = PathUtils.getFilePath();// 文件系统store地址
		for (AttFileDto fileDto : fileDtos) {
			if(StringUtils.isBlank(fileDto.getObjectType())||StringUtils.isBlank(fileDto.getObjectUnitId())){
				throw new RuntimeException("objectType或objectUnitId不可为空");
			}
			//1.组装文件实际路径
			String attachmentId = UuidUtils.generateUuid();
			String fileType = FileUtils.getExtension(fileDto.getFileName());
			String randomName = attachmentId + "." + fileType;
			String filePath = fileDto.getStorageDirType().getSubdirectory()
					+ File.separator + fileDto.getObjectType() + File.separator
					+ fileDto.getObjectUnitId() + File.separator + year + File.separator + fileDto.getObjectId();
			File newfile = AttachmentUtils.getFileLocalPath(storePath, filePath,
					randomName);
			//2.下载文件
			AttachmentUtils.downloadByUrl(fileDto.getFromUrl(), newfile);
			String contentType = new MimetypesFileTypeMap().getContentType(newfile);
			//3.封装Attachment
			Attachment attachment = new Attachment();
			attachment.setId(attachmentId);
			attachment.setDirId(BaseConstants.ZERO_GUID);
			attachment.setDirPath(storePath);
			attachment.setCreationTime(fileDto.getCreateTime());
			attachment.setModifyTime(new Date());
			attachment.setObjecttype(fileDto.getObjectType());
			attachment.setUnitId(fileDto.getObjectUnitId());
			attachment.setObjId(fileDto.getObjectId());
			attachment.setExtName(fileType);
			attachment.setFilename(fileDto.getFileName());
			attachment.setStatus(fileDto.getConStatus());
			attachment.setFilePath(filePath + File.separator + randomName);
			attachment.setFilesize(newfile.length());
			attachment.setContenttype(contentType);
			attachments.add(attachment);
		}
		// 保存attachment
		if (attachments.size() > 0) {
			saveAll(attachments.toArray(new Attachment[attachments.size()]));
		}
		return attachments;
	}

	@Override
	public List<Attachment> findAttachmentDirPathByObjTypeAndId(String objectType,
			String... objectId) {
		List<Attachment> attachments = attachmentDao.findByObjectTypeAndIdOrder(objectType,objectId);
		addDirPath(attachments);
		return attachments;
	}
	
}
