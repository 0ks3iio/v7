package net.zdsoft.eclasscard.data.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccAttachFolderDao;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;
import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.eclasscard.data.entity.EccFullObjAll;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccAttachFolderToService;
import net.zdsoft.eclasscard.data.service.EccFullObjAllService;
import net.zdsoft.eclasscard.data.service.EccFullObjService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccPhotoAlbumService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service("eccAttachFolderService")
public class EccAttachFolderServiceImpl extends BaseServiceImpl<EccAttachFolder, String> implements EccAttachFolderService {

	@Autowired
	private EccAttachFolderDao eccAttachFolderDao;
	@Autowired
	private EccAttachFolderToService eccAttachFolderToService;
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccFullObjAllService eccFullObjAllService;
	@Autowired
	private EccFullObjService eccFullObjService;
	
	@Override
	protected BaseJpaRepositoryDao<EccAttachFolder, String> getJpaDao() {
		return eccAttachFolderDao;
	}

	@Override
	protected Class<EccAttachFolder> getEntityClass() {
		return EccAttachFolder.class;
	}

	@Override
	public List<EccAttachFolder> findByObjIdAndRangeAndType(String objectId,
			Integer range, Integer type) {
		List<EccAttachFolder> attachFolders = Lists.newArrayList();
		List<EccAttachFolderTo> folderTos = eccAttachFolderToService.findByObjIdAndRangeAndType(objectId,range,type);
		Set<String> folderIds = EntityUtils.getSet(folderTos, ft -> ft.getFolderId());
		if (CollectionUtils.isNotEmpty(folderIds)) {
			attachFolders = eccAttachFolderDao.findByIdsOrder(folderIds.toArray(new String[folderIds.size()]));
		}
		return attachFolders;
	}
	
	@Override
	public List<EccAttachFolder> findListByObjId(String objectId,int range) {
		List<EccAttachFolder> attachFolders = Lists.newArrayList();
		List<EccAttachFolderTo> folderTos = eccAttachFolderToService.findByObjIdAndRange(objectId,range);
		Set<String> folderIds = EntityUtils.getSet(folderTos, ft -> ft.getFolderId());
		if(CollectionUtils.isNotEmpty(folderIds)){
			attachFolders = eccAttachFolderDao.findByIdsOrder(folderIds.toArray(new String[folderIds.size()]));
		}
		getCoverUrlAndNumber(attachFolders);
		return attachFolders;
	}

	@Override
	public List<EccAttachFolder> findListBySchIdRange2(String unitId, int ranges) {
		List<EccAttachFolder> attachFolders = eccAttachFolderDao.findListBySchIdRange2(unitId,ranges);
		getCoverUrlAndNumber(attachFolders);
		return attachFolders;
	}
	
	private void getCoverUrlAndNumber(List<EccAttachFolder> attachFolders) {
		Set<String> objIds =  EntityUtils.getSet(attachFolders, EccAttachFolder::getId);
		if (CollectionUtils.isNotEmpty(objIds)) {
			List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(objIds.toArray(new String[objIds.size()])),new TR<List<Attachment>>() {});
			Map<String,List<Attachment>> atmap = attachments.stream().collect(Collectors.groupingBy(Attachment::getObjId));
			for (EccAttachFolder attachFolder : attachFolders) {
				List<Attachment> attmt = atmap.get(attachFolder.getId());
				if (CollectionUtils.isNotEmpty(attmt)) {
					if (attachFolder.getType() == EccConstants.ECC_FOLDER_TYPE_3) {
						attmt = attmt.stream().filter(line -> AttFileDto.PPT_STATUS_PAGE_END == line.getStatus()).collect(Collectors.toList());
						Attachment attachment = attmt.stream().sorted((a1,a2) -> a2.getCreationTime().compareTo(a1.getCreationTime())).findFirst().get();
						attachFolder.setCoverUrl(attachment.getShowPicUrl());
					}
					attachFolder.setNumber(attmt.size());
				}else{
					attachFolder.setNumber(0);
				}
			}
		}
	}
	
	@Override
	public void saveNewFolder(EccAttachFolder attachFolder,String objectId) {
		if(EccConstants.ECC_FOLDER_RANGE_1 == attachFolder.getRange()){
			EccAttachFolderTo attachFolderTo = new EccAttachFolderTo();
			attachFolderTo.setId(UuidUtils.generateUuid());
			attachFolderTo.setFolderId(attachFolder.getId());
			attachFolderTo.setRange(attachFolder.getRange());
			attachFolderTo.setSendObjectId(objectId);
			attachFolderTo.setType(attachFolder.getType());
			eccAttachFolderToService.save(attachFolderTo);
			save(attachFolder);
		}else{
			save(attachFolder);
		}
		
	}

	@Override
	public void deleteById(String id,int range) {
		//1.删除文件夹内容
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(id),new TR<List<Attachment>>() {});
		Set<String> attachmentIds = attachments.stream().map(a -> a.getId()).collect(Collectors.toSet());
		try {
			if(CollectionUtils.isNotEmpty(attachmentIds)){
				attachmentRemoteService.deleteAttachments(attachmentIds.toArray(new String[attachmentIds.size()]), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//2.删除关系
		if(EccConstants.ECC_FOLDER_RANGE_2 != range){
			range = EccConstants.ECC_FOLDER_RANGE_1;
		}
		eccAttachFolderToService.deleteByFolderAndRange(id,range);
		
		List<EccFullObjAll> eccFullObjAllList = eccFullObjAllService.findByObjectId(id);
		if (CollectionUtils.isNotEmpty(eccFullObjAllList)) {
			List<EccFullObjAll> eccFullObjAlls = eccFullObjAllList.stream().filter(full -> full.getStatus() == EccConstants.ECC_SHOW_STATUS_1).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(eccFullObjAlls)){
				String[] eccFullIds = eccFullObjAlls.stream().map(fulls -> fulls.getId()).toArray(String[] :: new);
				List<EccFullObj> fullObjs = eccFullObjService.findBySourceIds(eccFullIds);
				Set<String> infoIds = fullObjs.stream().map(full -> full.getEccInfoId()).collect(Collectors.toSet());
				Set<String> sids = Sets.newHashSet();
				if(CollectionUtils.isNotEmpty(infoIds)){
					List<EccInfo> eccInfos = eccInfoService.findListByIdIn(infoIds.toArray(new String[infoIds.size()]));
					if (CollectionUtils.isNotEmpty(eccInfos)) {
						sids = eccInfos.stream().map(info -> info.getId()).collect(Collectors.toSet());
					}
				}
				if(CollectionUtils.isNotEmpty(sids)){
					EccNeedServiceUtils.postCheckFullScreen(sids);
				}
			}
		}
		//3.删除全屏发布信息
		eccFullObjAllService.deleteByObjectIds(id);
		//3.删除文件夹
		delete(id);
	}

	@Override
	public void updateIsShow(String id) {
		//1.展示对象的其他相册设为不展示
		EccAttachFolder attachFolder = findOne(id);
		if(EccConstants.ECC_FOLDER_RANGE_1 == attachFolder.getRange()){
			EccAttachFolderTo attachFolderTo = eccAttachFolderToService.findOneBy("folderId", id);
			if(attachFolderTo!=null&&StringUtils.isNotBlank(attachFolderTo.getSendObjectId())){
				List<EccAttachFolderTo> folderTos = eccAttachFolderToService.findListBy("sendObjectId",attachFolderTo.getSendObjectId());
				List<EccAttachFolderTo> removeFolderTos = Lists.newArrayList();
				Set<String> notShowFolderIds = Sets.newHashSet();
				Set<String> removeFolderIds = Sets.newHashSet();
				Set<String> removeFolderToIds = Sets.newHashSet();
				for(EccAttachFolderTo folderTo:folderTos){
					if(EccConstants.ECC_FOLDER_RANGE_2 == folderTo.getRange()){
						removeFolderTos.add(folderTo);
						removeFolderToIds.add(folderTo.getId());
						removeFolderIds.add(folderTo.getFolderId());
					}else{
						notShowFolderIds.add(folderTo.getFolderId());
					}
				}
				notShowFolderIds.remove(id);
				
				if(CollectionUtils.isNotEmpty(removeFolderIds)){//获取没有展示关系的全校相册的ids
					List<EccAttachFolderTo> reAttachFolderTos = eccAttachFolderToService.findListByIn("folderId", removeFolderIds.toArray(new String[removeFolderIds.size()]));
					Iterator<EccAttachFolderTo> it = reAttachFolderTos.iterator();
					while(it.hasNext()){
						EccAttachFolderTo to = it.next();
						if(removeFolderToIds.contains(to.getId())){
							it.remove();
						}
					}
					Set<String> showFolderIds = EntityUtils.getSet(reAttachFolderTos, "folderId");
					removeFolderIds.removeAll(showFolderIds);
					notShowFolderIds.addAll(removeFolderIds);
				}
				
				//1.单个范围的设置为不展示,多个范围没有展示关系的设置为不展示
				if(CollectionUtils.isNotEmpty(notShowFolderIds)){
					updateIsNotShow(notShowFolderIds.toArray(new String[notShowFolderIds.size()]));
				}
				
				//2.多个范围的删除展示关系
				if(CollectionUtils.isNotEmpty(removeFolderTos)){
					eccAttachFolderToService.deleteAll(removeFolderTos.toArray(new EccAttachFolderTo[removeFolderTos.size()]));
				}
			}
			// 2.当前相册设为展示
			eccAttachFolderDao.updateIsShow(id);
		}
		
	}

	@Override
	public void updateIsNotShow(String[] ids) {
		eccAttachFolderDao.updateIsNotShow(ids);
	}

	@Override
	public void saveVideo(List<AttFileDto> fileDtos, String folderId,String unitId) {
		AttFileDto fileDto = fileDtos.get(0);
		if(fileDto!=null){
			fileDto.setObjectType(EccConstants.ECC_ATTACHMENT_TYPE);
			fileDto.setObjectId(folderId);
			fileDto.setObjectUnitId(unitId);
			List<AttFileDto> saveFileDtos = Lists.newArrayList();
			saveFileDtos.add(fileDto);
			try {
				attachmentRemoteService.saveAttachment(SUtils.s(saveFileDtos));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateIsShow(String id, String eccInfoIds) {
		EccAttachFolder attachFolder = findOne(id);
		if(EccConstants.ECC_FOLDER_RANGE_2 == attachFolder.getRange()){
			List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findListBy("folderId", id);
//			List<EccInfo> eccInfosList = eccInfoService.findListByIdIn(eccInfoIds.split(","));
//				Set<String> adminIds = eccInfosList.stream().filter(c -> EccConstants.ECC_MCODE_BPYT_1.equals(c.getType())).map(c -> c.getClassId()).collect(Collectors.toSet());
//				Set<String> noadminIds = eccInfosList.stream().filter(c -> !EccConstants.ECC_MCODE_BPYT_1.equals(c.getType())).map(c -> c.getId()).collect(Collectors.toSet());
			Set<String> objIds = new HashSet<>(Arrays.asList(eccInfoIds.split(",")));//需要显示的
			Set<String> sids = new HashSet<>(Arrays.asList(eccInfoIds.split(",")));//需要推送的
//				objIds.addAll(adminIds);
//				objIds.addAll(noadminIds);
			Set<String> delIds = Sets.newHashSet();
			
			Iterator<EccAttachFolderTo> it = attachFolderTos.iterator();
			while(it.hasNext()){
				EccAttachFolderTo to = it.next();
				if (objIds.contains(to.getSendObjectId())) {
					it.remove();
					delIds.add(to.getSendObjectId());
				}else{
					sids.add(to.getSendObjectId());
				}
			}
			if (CollectionUtils.isNotEmpty(delIds)) {
				objIds.removeAll(delIds);
			}
			if(CollectionUtils.isNotEmpty(attachFolderTos)){
				eccAttachFolderToService.deleteAll(attachFolderTos.toArray(new EccAttachFolderTo[attachFolderTos.size()]));
			}
			
			if(CollectionUtils.isNotEmpty(objIds)){
				saveNewFolderTo(objIds, attachFolder);
			}
			
			eccAttachFolderDao.updateIsShow(id);
			
			if(CollectionUtils.isNotEmpty(sids)){
				EccNeedServiceUtils.postPhoto(sids, attachFolder.getUnitId());
			}
		}
		
	}

	@Override
	public void updateNotShow(String id) {
		EccAttachFolder attachFolder = findOne(id);
		Set<String> sids = Sets.newHashSet();
		List<EccAttachFolderTo> attachFolderTos = eccAttachFolderToService.findListBy("folderId", id);
		eccAttachFolderDao.updateIsNotShow(new String[] {id});
		if(CollectionUtils.isNotEmpty(attachFolderTos)){
			eccAttachFolderToService.deleteAll(attachFolderTos.toArray(new EccAttachFolderTo[attachFolderTos.size()]));
			sids.addAll(EntityUtils.getSet(attachFolderTos, EccAttachFolderTo::getSendObjectId));
			if(CollectionUtils.isNotEmpty(sids)){
				EccNeedServiceUtils.postPhoto(sids, attachFolder.getUnitId());
			}
		}
	}
	
	private void saveNewFolderTo(Set<String> sendObjIds,EccAttachFolder attachFolder) {
		List<EccAttachFolderTo> saveFolderTos = Lists.newArrayList();
		List<EccAttachFolderTo> removeFolderTos = Lists.newArrayList();
		Set<String> notShowFolderIds = Sets.newHashSet();
		Set<String> removeFolderIds = Sets.newHashSet();
		Set<String> removeFolderToIds = Sets.newHashSet();
		//已在展示的文件夹
		List<EccAttachFolderTo> sendObjFolderTos =  eccAttachFolderToService.findListByIn("sendObjectId", sendObjIds.toArray(new String[sendObjIds.size()]));
		for(EccAttachFolderTo folderTo:sendObjFolderTos){
			if(EccConstants.ECC_FOLDER_RANGE_2 == folderTo.getRange()){
				removeFolderTos.add(folderTo);
				removeFolderToIds.add(folderTo.getId());
				removeFolderIds.add(folderTo.getFolderId());
			}else{
				notShowFolderIds.add(folderTo.getFolderId());
			}
		}
		if(CollectionUtils.isNotEmpty(removeFolderIds)){//获取没有展示关系的全校相册的ids
			List<EccAttachFolderTo> reAttachFolderTos = eccAttachFolderToService.findListByIn("folderId", removeFolderIds.toArray(new String[removeFolderIds.size()]));
			Iterator<EccAttachFolderTo> it = reAttachFolderTos.iterator();
			while(it.hasNext()){
				EccAttachFolderTo to = it.next();
				if(removeFolderToIds.contains(to.getId())){
					it.remove();
				}
			}
			Set<String> showFolderIds = EntityUtils.getSet(reAttachFolderTos, "folderId");//这些是还有展示关系的
			removeFolderIds.removeAll(showFolderIds);
			notShowFolderIds.addAll(removeFolderIds);
		}
		//1.单个范围的设置为不展示,多个范围没有展示关系的设置为不展示
		if(CollectionUtils.isNotEmpty(notShowFolderIds)){
			updateIsNotShow(notShowFolderIds.toArray(new String[notShowFolderIds.size()]));
		}
		//2.多个范围的删除展示关系
		if(CollectionUtils.isNotEmpty(removeFolderTos)){
			eccAttachFolderToService.deleteAll(removeFolderTos.toArray(new EccAttachFolderTo[removeFolderTos.size()]));
		}
		//3.保存新的展示关系
		for(String sendObjId:sendObjIds){
			EccAttachFolderTo attachFolderTo = new EccAttachFolderTo();
			attachFolderTo.setId(UuidUtils.generateUuid());
			attachFolderTo.setFolderId(attachFolder.getId());
			attachFolderTo.setRange(attachFolder.getRange());
			attachFolderTo.setSendObjectId(sendObjId);
			attachFolderTo.setType(attachFolder.getType());
			saveFolderTos.add(attachFolderTo);
		}
		if(CollectionUtils.isNotEmpty(saveFolderTos)){
			eccAttachFolderToService.saveAll(saveFolderTos.toArray(new EccAttachFolderTo[saveFolderTos.size()]));
		}
	}
}
