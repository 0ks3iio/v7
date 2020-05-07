package net.zdsoft.career.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.career.data.constant.CarConstants;
import net.zdsoft.career.data.dao.CarResourceCenterDao;
import net.zdsoft.career.data.entity.CarResourceCenter;
import net.zdsoft.career.data.service.CarResourceCenterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service("carResourceCenterService")
public class CarResourceCenterServiceImpl extends BaseServiceImpl<CarResourceCenter,String> implements CarResourceCenterService{

	@Autowired
	private CarResourceCenterDao carResourceCenterDao;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private FilePathRemoteService filePathRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<CarResourceCenter, String> getJpaDao() {
		return carResourceCenterDao;
	}

	@Override
	protected Class<CarResourceCenter> getEntityClass() {
		return CarResourceCenter.class;
	}
	
	@Override
	public List<CarResourceCenter> findByResourceType(Integer resourceType,
				Integer type,String title,boolean showPicture,Pagination page) {
		List<CarResourceCenter> carResources = null;
		Pageable pageable = Pagination.toPageable(page);
		Specification<CarResourceCenter> specification = new Specification<CarResourceCenter>() {
			@Override
			public Predicate toPredicate(Root<CarResourceCenter> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("resourceType").as(Integer.class), resourceType));
				if (type == CarConstants.CAR_RESOURCE_TYPE_1) {
					ps.add(cb.notEqual(root.get("type").as(Integer.class), CarConstants.CAR_RESOURCE_TYPE_2));
				}
				if (type == CarConstants.CAR_RESOURCE_TYPE_2) {
					ps.add(cb.notEqual(root.get("type").as(Integer.class), CarConstants.CAR_RESOURCE_TYPE_1));
				}
				if (StringUtils.isNotBlank(title)) {
					ps.add(cb.like(root.get("title").as(String.class), "%"+title+"%"));
				}
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(root.<Date>get("creationTime")));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}
		};
		Page<CarResourceCenter> findAll = carResourceCenterDao.findAll(specification, pageable);
		page.setMaxRowCount((int) findAll.getTotalElements());
		carResources = findAll.getContent();
		if (showPicture) {
			if (CollectionUtils.isNotEmpty(carResources)) {
				String[] idsList = carResources.stream().map(car -> car.getId()).toArray(String[]::new);
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(CarConstants.CAR_ATTACH_TYPE_1,idsList),new TR<List<Attachment>>(){});
				Map<String,Attachment> pictureMap = EntityUtils.getMap(attachments, Attachment::getObjId);
				Attachment attachment = null;
				for (CarResourceCenter center : carResources) {
					attachment = pictureMap.get(center.getId());
					if (attachment != null) {
						center.setPictureUrl(attachment.getShowPicUrl());
					}
				}
			}
		}
		return carResources;
	}

	@Override
	public CarResourceCenter findOneById(String resourceId, boolean showPicture) {
		CarResourceCenter carResourceCenter = carResourceCenterDao.findOneById(resourceId);
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(resourceId),new TR<List<Attachment>>(){});
		String furl = filePathRemoteService.getFilePath();
		for (Attachment attachment : attachments) {
			if (CarConstants.CAR_ATTACH_TYPE_1.equals(attachment.getObjecttype()) && showPicture) {
				carResourceCenter.setPictureUrl(attachment.getShowPicUrl());
			} 
			if (CarConstants.CAR_ATTACH_TYPE_2.equals(attachment.getObjecttype())) {
				carResourceCenter.setVideoUrl(furl+"/store/"+attachment.getFilePath().replace("\\","/"));
				carResourceCenter.setVideoName(attachment.getFilename());
			}
		}
		return carResourceCenter;
	}

	@Override
	public void deleteByIds(String resourceIds) {
		String[] ids = resourceIds.split(",");
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(ids),new TR<List<Attachment>>() {});
		String[] attachIds = attachments.stream().map(att -> att.getId()).toArray(String[]::new);
		try {
			attachmentRemoteService.deleteAttachments(attachIds, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		carResourceCenterDao.deleteByIds(ids);
	}

	@Override
	public void saveResource(CarResourceCenter carResource,
			String pictureArray, String videoArray) {
		if (StringUtils.isNotBlank(carResource.getId())) {
			CarResourceCenter center = carResourceCenterDao.findOneById(carResource.getId());
			center.setTitle(carResource.getTitle());
			center.setDescription(carResource.getDescription());
			center.setContent(carResource.getContent());
			center.setLinkUrl(carResource.getLinkUrl());
			center.setModifyTime(new Date());
			carResourceCenterDao.save(center);
			if (StringUtils.isNotBlank(pictureArray)) {
				try {
					List<Attachment> attPicture =  SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(CarConstants.CAR_ATTACH_TYPE_1, carResource.getId()),new TR<List<Attachment>>() {});
					attachmentRemoteService.deleteAttachments(new String[]{attPicture.get(0).getId()}, null);
					List<AttFileDto> pictureDtos = SUtils.dt(pictureArray,new TR<List<AttFileDto>>() {});
					for(AttFileDto pictureDto:pictureDtos){
						pictureDto.setObjectType(CarConstants.CAR_ATTACH_TYPE_1);
						pictureDto.setObjectId(carResource.getId());
						pictureDto.setObjectUnitId(BaseConstants.ZERO_GUID);
					}
					SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(pictureDtos)),new TR<List<Attachment>>() {});
				} catch (Exception e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
			if (StringUtils.isNotBlank(videoArray)) {
				try {
					List<Attachment> attVideo =  SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(CarConstants.CAR_ATTACH_TYPE_2, carResource.getId()),new TR<List<Attachment>>() {});
					attachmentRemoteService.deleteAttachments(new String[]{attVideo.get(0).getId()}, null);
					List<AttFileDto> videoDtos = SUtils.dt(videoArray,new TR<List<AttFileDto>>() {});
					for(AttFileDto videoDto:videoDtos){
						videoDto.setObjectType(CarConstants.CAR_ATTACH_TYPE_2);
						videoDto.setObjectId(carResource.getId());
						videoDto.setObjectUnitId(BaseConstants.ZERO_GUID);
					}
					SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(videoDtos)),new TR<List<Attachment>>() {});
				} catch (Exception e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		} else {
			String id = UuidUtils.generateUuid();
			List<AttFileDto> pictureDtos = SUtils.dt(pictureArray,new TR<List<AttFileDto>>() {});
			for(AttFileDto pictureDto:pictureDtos){
				pictureDto.setObjectType(CarConstants.CAR_ATTACH_TYPE_1);
				pictureDto.setObjectId(id);
				pictureDto.setObjectUnitId(BaseConstants.ZERO_GUID);
			}
			try {
				SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(pictureDtos)),new TR<List<Attachment>>() {});
			} catch (Exception e) {
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			if (StringUtils.isNotBlank(videoArray)) {
				List<AttFileDto> videoDtos = SUtils.dt(videoArray,new TR<List<AttFileDto>>() {});
				for(AttFileDto videoDto:videoDtos){
					videoDto.setObjectType(CarConstants.CAR_ATTACH_TYPE_2);
					videoDto.setObjectId(id);
					videoDto.setObjectUnitId(BaseConstants.ZERO_GUID);
				}
				try {
					SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(videoDtos)),new TR<List<Attachment>>() {});
				} catch (Exception e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
			carResource.setId(id);
			carResource.setIsDeleted(0);
			carResource.setCreationTime(new Date());
			carResource.setModifyTime(new Date());
			carResourceCenterDao.save(carResource);
		}
	}

}
