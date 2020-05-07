package net.zdsoft.eclasscard.data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccInfoDao;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccAttachFolderTo;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccBulletinTo;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccPhotoAlbum;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccAttachFolderToService;
import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.eclasscard.data.service.EccBulletinToService;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccPermissionService;
import net.zdsoft.eclasscard.data.service.EccPhotoAlbumService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.delayQueue.DelayQueueService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccInfoService")
public class EccInfoServiceImpl extends BaseServiceImpl<EccInfo, String> implements EccInfoService{
	@Autowired
	private EccInfoDao eccInfoDao;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachPlaceRemoteService teachPlaceRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private EccPermissionService eccPermissionService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private EccBulletinToService eccBulletinToService;
	@Autowired
	private EccBulletinService eccBulletinService;
	@Autowired
	private DelayQueueService delayQueueService;
	@Autowired
	private EccAttachFolderService eccAttachFolderService;
	@Autowired
	private EccAttachFolderToService eccAttachFolderToService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	
	@Override
	protected BaseJpaRepositoryDao<EccInfo, String> getJpaDao() {
		return eccInfoDao;
	}

	@Override
	protected Class<EccInfo> getEntityClass() {
		return EccInfo.class;
	}

	@Override
	public List<EccInfo> findByNameAndType(final String name, final String type,final String gradeId,final int status, final String unitId,Pagination page) {
		List<EccInfo> eccInfos = Lists.newArrayList();
		Set<String> classIdSet = Sets.newHashSet();
		if (StringUtils.isNotEmpty(gradeId)) {
			 List<Clazz> clazzes = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId),new TR<List<Clazz>>() {});
			 for(Clazz clazz:clazzes){
				 classIdSet.add(clazz.getId());
			 }
			 if(classIdSet.size()<1){
				 return eccInfos;
			 }
		}
		final String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
		Specification<EccInfo> specification = new Specification<EccInfo>() {

            @Override
            public Predicate toPredicate(Root<EccInfo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                if (StringUtils.isNotEmpty(type)) {
                	ps.add(cb.equal(root.get("type").as(String.class), type));
                }
                if (status > 0) {
                	ps.add(cb.equal(root.get("status").as(Integer.class), status));
                }
                if (StringUtils.isNotEmpty(name)) {
                	 ps.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
                }
                if (null != classIds&&classIds.length>0) {
                    In<String> in = cb.in(root.get("classId").as(String.class));
                    for (int i = 0; i < classIds.length; i++) {
                        in.value(classIds[i]);
                    }
                    ps.add(in);
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("name").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
        if (page != null) {
        	Pageable pageable = Pagination.toPageable(page);
        	Page<EccInfo> findAll = eccInfoDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	eccInfos = findAll.getContent();
        }
        else {
        	eccInfos = eccInfoDao.findAll(specification);
        }
        fillClsAndPlaceName(eccInfos,unitId);
        return eccInfos;
	}


	@Override
	public List<EccInfo> findListByNotClass(String unitId) {
		List<EccInfo> eccInfos = eccInfoDao.findListByNotClass(unitId);
		fillClsAndPlaceName(eccInfos,unitId);
		return eccInfos;
	}
	private void fillClsAndPlaceName(List<EccInfo> eccInfos,String unitId){
		Set<String> classIdInfoSet = Sets.newHashSet();
        Set<String> palceIdInfoSet = Sets.newHashSet();
        for(EccInfo info:eccInfos){
        	if(StringUtils.isNotBlank(info.getClassId()))
        		classIdInfoSet.add(info.getClassId());
        	if(StringUtils.isNotBlank(info.getPlaceId()))
        		palceIdInfoSet.add(info.getPlaceId());
        }
        if(classIdInfoSet.size()>0){
        	List<Clazz> clazzs = SUtils.dt(classRemoteService.findClassListByIds(classIdInfoSet.toArray(new String[classIdInfoSet.size()])),new TR<List<Clazz>>() {});
        	Set<String> teachPlaceIds = (EntityUtils.getSet(clazzs, "teachPlaceId"));
        	palceIdInfoSet.addAll(teachPlaceIds);
        	Map<String,Clazz> classNameMap = EntityUtils.getMap(clazzs, "id");
        	for(EccInfo info:eccInfos){
        		if(classNameMap.containsKey(info.getClassId())){
        			Clazz clazz = classNameMap.get(info.getClassId());
        			if(clazz!=null){
        				info.setClassName(clazz.getClassNameDynamic());
        				info.setPlaceId(clazz.getTeachPlaceId());
        			}
        		}
        	}
        }
        if(palceIdInfoSet.size()>0){
        	Map<String, String> teachPlaceMap = SUtils.dt(teachPlaceRemoteService.findTeachPlaceMap(palceIdInfoSet.toArray(new String[0])),new TR<Map<String, String>>() {});
        	String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(unitId);
        	List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
    		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, "buildingId","buildingName");
        	for(EccInfo info:eccInfos){
        		if(EccConstants.ECC_MCODE_BPYT_3.equals(info.getType())){
        			if(dormBuildMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(dormBuildMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("（已删除）");
        			}
        		}else{
        			if(teachPlaceMap.containsKey(info.getPlaceId())){
        				info.setPlaceName(teachPlaceMap.get(info.getPlaceId()));
        			}else if(StringUtils.isNotBlank(info.getPlaceId())){
        				info.setPlaceName("（已删除）");
        			}
        		}
        	}
        }
	}

	@Override
	public EccInfo findOneFillName(String eccInfoId) {
		EccInfo eccInfo = findOne(eccInfoId);
		if(eccInfo==null){
			return new EccInfo();
		}
		List<EccInfo> eccInfos = Lists.newArrayList();
		eccInfos.add(eccInfo);
		fillClsAndPlaceName(eccInfos,eccInfo.getUnitId());
		return eccInfo;
	}

	@Override
	public List<EccInfo> findListFillName(String[] eccInfoIds) {
		List<EccInfo> eccInfos = findListByIdIn(eccInfoIds);
		if(CollectionUtils.isNotEmpty(eccInfos)){
			String unitId = eccInfos.get(0).getUnitId();
			fillClsAndPlaceName(eccInfos,unitId);
		}
		return eccInfos;
	}

	@Override
	public List<EccInfo> findClassAttence(Set<String> placeIds,
			Set<String> classIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EccInfo> findListByUnitAndType(String unitId, String... type) {
		return eccInfoDao.findListByUnitAndType(unitId, type);
	}

	@Override
	public List<EccInfo> findByClassIdIn(String[] classIds) {
		return eccInfoDao.findByClassIdIn(classIds);
	}

	@Override
	public void deleteAllById(String id) throws Exception {
		EccInfo eccInfo = eccInfoDao.findById(id).orElse(null);
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		Boolean type = false;
		if (UnitIni.ECC_USE_VERSION_STANDARD.equals(EccNeedServiceUtils.getEClassCardVerison(eccInfo.getUnitId()))) {
			type = true;
		}
		//删除班牌权限数据
		eccPermissionService.deleteByEccName(eccInfo.getName(),eccInfo.getUnitId());
		Map<String,Integer> allMap = Maps.newHashMap();
		List<EccBulletinTo> bulletinToAll = eccBulletinToService.findAll();
		List<EccBulletinTo> bulletinTos = Lists.newArrayList();
		Set<String> bulletinIds = Sets.newHashSet();
		for (EccBulletinTo bulletinTo : bulletinToAll) {
			if (allMap.containsKey(bulletinTo.getBulletinId())) {
				allMap.put(bulletinTo.getBulletinId(),allMap.get(bulletinTo.getBulletinId())+1);
			} else {
				allMap.put(bulletinTo.getBulletinId(), 1);
			}
			if (bulletinTo.getEccInfoId().equals(id)) {
				bulletinTos.add(bulletinTo);
				bulletinIds.add(bulletinTo.getBulletinId());
			}
		}
		//删除班牌公告
		if (CollectionUtils.isNotEmpty(bulletinIds)) {
			List<EccBulletin> bulletins = eccBulletinService.findListByIdIn(bulletinIds.toArray(new String[0]));
			Iterator it = bulletins.iterator();
			while(it.hasNext()) {
				EccBulletin bulletin = (EccBulletin) it.next();
				if (type) {
					if (EccConstants.ECC_BULLETIN_LEVEL_1.equals(bulletin.getBulletinLevel()) && (allMap.get(bulletin.getId()) > 1)) {
						it.remove();
					}
				} else {
					if (!EccConstants.ECC_SENDTYPE_0.equals(bulletin.getSendType()) && (allMap.get(bulletin.getId()) > 1)) {
						it.remove();
					}
				}
			}
			for (EccBulletin bulletin : bulletins) {
				if (!EccConstants.ECC_SHOW_STATUS_2.equals(bulletin.getStatus())) {
					delayQueueService.addRemoveItem2Queue(bulletin.getId()+"start");
					delayQueueService.addRemoveItem2Queue(bulletin.getId()+"end");
				}
			}
			eccBulletinService.deleteAll(bulletins.toArray(new EccBulletin[bulletins.size()]));
			eccBulletinToService.deleteAll(bulletinTos.toArray(new EccBulletinTo[bulletinTos.size()]));
		}
		//删除宣传栏信息
		if (StringUtils.isNotBlank(eccInfo.getType()) && !EccConstants.ECC_MCODE_BPYT_1.equals(eccInfo.getType())) {
			if (type) {
				//标准版
				List<EccAttachFolderTo> folderTos = eccAttachFolderToService.findByObjIdAndRange(id,EccConstants.ECC_FOLDER_RANGE_1);
				Set<String> folderIds = EntityUtils.getSet(folderTos, "folderId");
				List<EccAttachFolder> attachFolders = eccAttachFolderService.findListByIds(folderIds.toArray(new String[folderIds.size()]));
				for (EccAttachFolder attachFolder : attachFolders) {
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(attachFolder.getId()),new TR<List<Attachment>>() {});
					Set<String> attachmentIds = EntityUtils.getSet(attachments, "id");
					if (CollectionUtils.isNotEmpty(attachmentIds)) {
						attachmentRemoteService.deleteAttachments(attachmentIds.toArray(new String[attachmentIds.size()]), null);
					}
					eccAttachFolderToService.deleteByFolderAndRange(attachFolder.getId(),EccConstants.ECC_FOLDER_RANGE_1);
					eccAttachFolderService.delete(attachFolder.getId());
				}
				eccAttachFolderToService.deleteByObjAndRange(id, EccConstants.ECC_FOLDER_RANGE_2);
			} else {
				//德育版
				List<EccPhotoAlbum> albums = eccPhotoAlbumService.findListByObjectIdPage(id, null);
				Set<String> pictrueDirpaths = EntityUtils.getSet(albums, "pictrueDirpath");
				if(!pictrueDirpaths.isEmpty()){//删除没有其他引用的图片
					Set<String> paths = eccPhotoAlbumService.findPictrueDirpathMore(pictrueDirpaths.toArray(new String[pictrueDirpaths.size()]));
					for(EccPhotoAlbum album:albums){
						if(StringUtils.isNotBlank(album.getPictrueDirpath())&&!paths.contains(album.getPictrueDirpath())){
							File file = new File(fileSystemPath+ File.separator+album.getPictrueDirpath());
							if(file.exists()){
								file.delete();
							}
						}
					}
				}
				if(!albums.isEmpty()){
					eccPhotoAlbumService.deleteAll(albums.toArray(new EccPhotoAlbum[albums.size()]));
				}
			}
		}
		//删除班牌信息
		eccFaceActivateService.deleteByInfoId(eccInfo.getUnitId(), eccInfo.getId());
		eccInfoDao.deleteById(id);
		Set<String> sids = Sets.newHashSet();
		sids.add(eccInfo.getId());
		EccNeedServiceUtils.postClassClock(sids,1);
	}

	@Override
	public List<EccInfo> findListByTypes(String... type){
		return eccInfoDao.findListByTypes(type);
	}

	@Override
	public List<EccInfo> findByUnitId(String unitId) {
		return eccInfoDao.findByUnitId(unitId);
	}

	@Override
	public EccInfo findByUnitIdAndName(String unitId,String name) {
		return eccInfoDao.findByUnitIdAndName(unitId,name);
	}

	@Override
	public List<EccInfo> findListOnLineByIds(String[] ids) {
		Specification<EccInfo> specification = new Specification<EccInfo>() {
			 @Override
			 public Predicate toPredicate(Root<EccInfo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
              	 queryIn("id", ids, root, ps, null);
              	 ps.add(cb.equal(root.get("status").as(Integer.class), 2));
				 cq.where(ps.toArray(new Predicate[0]));
				 return cq.getRestriction();
	         }
	     };
	     return eccInfoDao.findAll(specification);
	}

	@Override
	public void updateOnLineStatus(String id, int status) {
		eccInfoDao.updateOnLineStatus(id, status);
	}

}
