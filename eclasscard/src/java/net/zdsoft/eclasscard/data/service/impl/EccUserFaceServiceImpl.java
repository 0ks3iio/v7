package net.zdsoft.eclasscard.data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccUserFaceDao;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.eclasscard.data.service.EccFaceActivateService;
import net.zdsoft.eclasscard.data.service.EccUserFaceService;
import net.zdsoft.eclasscard.data.utils.FaceUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
@Service("eccUserFaceService")
public class EccUserFaceServiceImpl extends BaseServiceImpl<EccUserFace, String> implements EccUserFaceService{

	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccFaceActivateService eccFaceActivateService;
	@Autowired
	private EccUserFaceDao eccUserFaceDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccUserFace, String> getJpaDao() {
		return eccUserFaceDao;
	}

	@Override
	protected Class<EccUserFace> getEntityClass() {
		return EccUserFace.class;
	}

	@Override
	public List<EccUserFace> findByUnitIdPage(String unitId,boolean fillName, Pagination page) {
		List<EccUserFace> faces = Lists.newArrayList();
		 Specification<EccUserFace> specification = new Specification<EccUserFace>() {
	            @Override
	            public Predicate toPredicate(Root<EccUserFace> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	                List<Predicate> ps = new ArrayList<Predicate>();
	                
	                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
	                List<Order> orderList = new ArrayList<Order>();
	                orderList.add(cb.desc(root.get("creationTime").as(Date.class)));

	                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
	            }
			};
        if (page != null) {
        	Pageable pageable = Pagination.toPageable(page);
        	Page<EccUserFace> findAll = eccUserFaceDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	faces =  findAll.getContent();
        }
        else {
        	faces = eccUserFaceDao.findAll(specification);
        }
        if(fillName){
        	fillStuName(faces);
        }
        return faces;
	}

	private void fillStuName(List<EccUserFace> faces) {
		Set<String> stuIds = EntityUtils.getSet(faces, EccUserFace::getOwnerId);
		if(CollectionUtils.isNotEmpty(stuIds)){
			List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {});
			Map<String,String> stuNameMap = EntityUtils.getMap(students, Student::getId,Student::getStudentName);
			for(EccUserFace face:faces){
				if(stuNameMap.containsKey(face.getOwnerId())){
					face.setOwnerName(stuNameMap.get(face.getOwnerId()));
				}
			}
		}
		
	}

	@Override
	public List<EccUserFace> findByOwnerIds(String[] ownerIds) {
//		Specification<EccUserFace> specification = new Specification<EccUserFace>() {
//			 @Override
//			 public Predicate toPredicate(Root<EccUserFace> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//				 List<Predicate> ps = new ArrayList<Predicate>();
//               	 queryIn("ownerId", ownerIds, root, ps, cb);
//				 cq.where(ps.toArray(new Predicate[0]));
//				 return cq.getRestriction();
//	         }
//	     };
//	     return eccUserFaceDao.findAll(specification);
	     if(ownerIds==null || ownerIds.length<=0) {
	    	 return new ArrayList<>();
	     }
	     if (ownerIds.length <= 1000) {
	    	 List<EccUserFace> list = eccUserFaceDao.findByOwnerIds(ownerIds);
	    	 fillStuName(list);
	    	 return list;
		} else {
			List<EccUserFace> list=new ArrayList<>();
			int cyc = ownerIds.length / 1000 + (ownerIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > ownerIds.length)
					max = ownerIds.length;
				List<EccUserFace> list1 = eccUserFaceDao.findByOwnerIds(ArrayUtils.subarray(ownerIds, i * 1000, max));
				if(CollectionUtils.isNotEmpty(list1)) {
					list.addAll(list1);
				}
			}
			fillStuName(list);
			return list;
		}
	     
	    
	}

	@Override
	public String saveBacthUpload(String array, String unitId) {
		List<AttFileDto> fileDtos = SUtils.dt(array,new TR<List<AttFileDto>>() {});
		List<AttFileDto> saveFileDtos = Lists.newArrayList();
		List<EccUserFace> saveFaces = Lists.newArrayList();
		Set<String> updateOwnerIds = Sets.newHashSet();
		JSONObject json = new JSONObject();
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		Set<String> fileNames = EntityUtils.getSet(fileDtos, AttFileDto::getFileName);
		Set<String> stuCodes = fileNames.stream().map(line ->{ if(line.lastIndexOf(".")>0){return line.substring(0,line.lastIndexOf("."));}else{return line;}}).collect(Collectors.toSet());
		List<Student> stuList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(stuCodes)){
			stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodes.toArray(new String[0])), new TR<List<Student>>() {});
		}
		json.put("failNum", 0);
		json.put("susNum", 0);
		if(CollectionUtils.isEmpty(stuList)){
			//没有找到学生
			json.put("wppNum", fileNames.size());
			return json.toJSONString();
		}
		Map<String,Student> stuCodeMap = EntityUtils.getMap(stuList, Student::getStudentCode);
		json.put("wppNum", fileNames.size()-stuCodeMap.size());
		for(AttFileDto fileDto:fileDtos){
			String filename = fileDto.getFileName();
			String ownerId = "";
			if(StringUtils.isNotBlank(filename) && filename.lastIndexOf(".")>0){
				filename = filename.substring(0,filename.lastIndexOf("."));
			}else{
				continue;
			}
			Student student = null;
			if(stuCodeMap.containsKey(filename)){
				student = stuCodeMap.get(filename);
			}
			
			if(student==null){
				continue;
			}else{
				ownerId = student.getId();
			}
			String filePath = "";
			filePath = fileDto.getFilePath();
			String srcPath = fileSystemPath + File.separator
					+ filePath;
			File srcFile = new File(srcPath);
			String userInfo = student==null?"未知用户":student.getStudentName();
			if(srcFile.exists()){
				List<EccUserFace> faces = findByOwnerIds(new String[]{ownerId});
				boolean isOk = false;
				if(CollectionUtils.isNotEmpty(faces)){
					isOk =FaceUtils.updateFace(ownerId, FaceUtils.getImgStr(srcPath), userInfo);
				}else{
					isOk =FaceUtils.registerFace(unitId, ownerId, FaceUtils.getImgStr(srcPath), userInfo);
				}
				if(isOk){
					String objectId = UuidUtils.generateUuid();
					EccUserFace face = new EccUserFace();
					face.setId(objectId);
					face.setCreationTime(new Date());
					face.setModifyTime(new Date());
					face.setOwnerId(ownerId);
					face.setState(0);
					face.setUnitId(unitId);
					face.setFailTimes(0);
					saveFaces.add(face);
					
					fileDto.setObjectType(EccConstants.ECC_ATTACHMENT_TYPE);
					fileDto.setObjectId(objectId);
					fileDto.setObjectUnitId(unitId);
					saveFileDtos.add(fileDto);
					updateOwnerIds.add(ownerId);
				}
			}
		}
		
		try {
			//新上传的，删除之前的
			if(CollectionUtils.isNotEmpty(updateOwnerIds)){
				List<EccUserFace> faces = findByOwnerIds(updateOwnerIds.toArray(new String[updateOwnerIds.size()]));
				Set<String> faceIds = EntityUtils.getSet(faces, EccUserFace::getId);
				if(CollectionUtils.isNotEmpty(faceIds)){
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(faceIds.toArray(new String[faceIds.size()])),new TR<List<Attachment>>() {});
					Set<String> attIds = EntityUtils.getSet(attachments, Attachment::getId);
					if(CollectionUtils.isNotEmpty(attIds)){
						attachmentRemoteService.deleteAttachments(attIds.toArray(new String[attIds.size()]), null);
					}
					deleteAll(faces.toArray(new EccUserFace[faces.size()]));
				}
			}
			//保存附件
			if(CollectionUtils.isNotEmpty(saveFileDtos)){
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(saveFileDtos)),new TR<List<Attachment>>() {});
			}
			//保存人脸信息表
			if(CollectionUtils.isNotEmpty(saveFaces)){
				saveAll(saveFaces.toArray(new EccUserFace[saveFaces.size()]));
				eccFaceActivateService.updateNeedLowerUnitId(unitId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.put("failNum", stuCodeMap.size()-saveFaces.size());
		json.put("susNum", saveFaces.size());
		return json.toJSONString();
	}

	@Override
	public Integer countByUnitId(String unitId) {
		return eccUserFaceDao.countByUnitId(unitId);
	}

	@Override
	public void deleteByOwnerIds(Set<String> failIds,String unitId) {
		Set<String> deleteIds =  Sets.newHashSet();
		for(String userId:failIds){
			if(FaceUtils.deleteFace(userId, unitId)){
				deleteIds.add(userId);
			}
		}
		if(CollectionUtils.isNotEmpty(deleteIds)){
			List<EccUserFace> faces = findByOwnerIds(deleteIds.toArray(new String[deleteIds.size()]));
			Set<String> faceIds = EntityUtils.getSet(faces, EccUserFace::getId);
			if(CollectionUtils.isNotEmpty(faceIds)){
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(faceIds.toArray(new String[faceIds.size()])),new TR<List<Attachment>>() {});
				Set<String> attIds = EntityUtils.getSet(attachments, Attachment::getId);
				if(CollectionUtils.isNotEmpty(attIds)){
					try {
						attachmentRemoteService.deleteAttachments(attIds.toArray(new String[attIds.size()]), null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				deleteAll(faces.toArray(new EccUserFace[faces.size()]));
			}
		}
	}

	@Override
	public void deleteStuNotInSchool() {
		Set<String> deteteUserIds = Sets.newHashSet();
		Set<String> unitIds = findUseFaceUnitIds();
		for(String unitId:unitIds){
			List<Student> students = SUtils.dt(studentRemoteService.findBySchoolId(unitId),new TR<List<Student>>() {});
			Set<String> stuIds = EntityUtils.getSet(students, Student::getId);
			if(CollectionUtils.isNotEmpty(stuIds)){
				List<EccUserFace> userFaces = findByUnitIdPage(unitId,false,null);
				for(EccUserFace face:userFaces){
					if(!stuIds.contains(face.getOwnerId())){
						deteteUserIds.add(face.getOwnerId());
					}
				}
				deleteByOwnerIds(deteteUserIds, unitId);
			}
		}
	}

	@Override
	public Set<String> findUseFaceUnitIds() {
		return eccUserFaceDao.findUseFaceUnitIds();
	}

}
