package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.utils.AttachmentUtils;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccHonorDao;
import net.zdsoft.eclasscard.data.entity.EccBulletin;
import net.zdsoft.eclasscard.data.entity.EccHonor;
import net.zdsoft.eclasscard.data.entity.EccHonorTo;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.service.EccHonorService;
import net.zdsoft.eclasscard.data.service.EccHonorToService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccPhotoAlbumService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.eclasscard.data.task.EccTask;
import net.zdsoft.eclasscard.data.task.HonorTask;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service("eccHonorService")
public class EccHonorServiceImpl extends BaseServiceImpl<EccHonor,String> implements EccHonorService{

	@Autowired
	private EccHonorDao eccHonorDao;
	@Autowired
	private EccHonorToService eccHonorToService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccTaskService eccTaskService;
	@Autowired
	private EccInfoService eccInfoService;
	
	@Override
	protected BaseJpaRepositoryDao<EccHonor, String> getJpaDao() {
		return eccHonorDao;
	}

	@Override
	protected Class<EccHonor> getEntityClass() {
		return EccHonor.class;
	}

	@Override
	public List<EccHonor> findByUnitIdAndType(String unitId, Integer type,Pagination page) {
		 Specification<EccHonor> specification = new Specification<EccHonor>() {
			 @Override
			 public Predicate toPredicate(Root<EccHonor> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				 ps.add(cb.equal(root.get("type").as(Integer.class), type));
	                
				 List<Order> orderList = new ArrayList<Order>();
				 orderList.add(cb.desc(root.get("createTime").as(Date.class)));

				 cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				 return cq.getRestriction();
	         }
	     };
	    Pageable pageable = Pagination.toPageable(page);
	    Page<EccHonor> findAll = eccHonorDao.findAll(specification, pageable);
	    page.setMaxRowCount((int) findAll.getTotalElements());
	    List<EccHonor> eccHonors = findAll.getContent();
		if (CollectionUtils.isNotEmpty(eccHonors)) {
			if (EccConstants.ECC_HONOR_TYPE_1.equals(type)) {
				List<String> honorIds = EntityUtils.getList(eccHonors, "id");
				List<EccHonorTo> eccHonorTos = eccHonorToService.findByHonorIdIn(honorIds.toArray(new String[0]));
				if (CollectionUtils.isEmpty(eccHonorTos)) {
					return new ArrayList<EccHonor>();
				}
				Map<String,String> honorClassMap = getclassName(eccHonorTos);
				for (EccHonor eccHonor : eccHonors) {
					eccHonor.setClassName(honorClassMap.get(eccHonor.getId()));
				}
			}
		}
		return eccHonors;
	}
	
	@Override
	public EccHonor findById(String honorId) {
		EccHonor eccHonor = findOne(honorId);
		List<EccHonorTo> eccHonors = eccHonorToService.findByHonorIdIn(new String[]{honorId});
		List<String> objectIds = EntityUtils.getList(eccHonors, "objectId");
		if (EccConstants.ECC_HONOR_TYPE_1.equals(eccHonor.getType())) {
			Map<String,String> honorClassMap = getclassName(eccHonors);
			String classId = "";
			for (String str : objectIds) {
				classId += "," + str;
			}
			classId = classId.substring(1);
			eccHonor.setClassId(classId);
			eccHonor.setClassName(honorClassMap.get(eccHonor.getId()));
		} else {
			Student student = SUtils.dc(studentRemoteService.findOneById(objectIds.get(0)), Student.class);
			eccHonor.setStudentId(student.getId());
			eccHonor.setStudentName(student.getStudentName());
		}
		return eccHonor;
	}

	@Override
	public List<EccHonor> findByIdsDesc(String[] honorIds) {
		return eccHonorDao.findByIdsDesc(honorIds);
	}
	
	private Map<String,String> getclassName(List<EccHonorTo> eccHonorTos) {
		Set<String> classIdsSet = EntityUtils.getSet(eccHonorTos, EccHonorTo::getObjectId);
		String[] classIds = classIdsSet.toArray(new String[0]);
		List<Clazz> classList = SUtils.dt(classRemoteService.findByIdsSort(classIds), new TR<List<Clazz>>(){});
		int i = 1;
		for (Clazz clazz : classList) {
			clazz.setState(i);
			i++;
		}
		Map<String,Clazz> classMap = EntityUtils.getMap(classList, Clazz::getId);
		Map<String,List<Clazz>> honorClassesMap = Maps.newHashMap();
		List<Clazz> clazzList = Lists.newArrayList();
		for (EccHonorTo eccHonorTo : eccHonorTos) {
			if (honorClassesMap.containsKey(eccHonorTo.getHonorId())) {
				clazzList = honorClassesMap.get(eccHonorTo.getHonorId());
			} else {
				clazzList = Lists.newArrayList();
			}
			clazzList.add(classMap.get(eccHonorTo.getObjectId()));
			honorClassesMap.put(eccHonorTo.getHonorId(),clazzList);
		}
		Map<String,String> honorClassMap = Maps.newHashMap();
		for (Map.Entry<String, List<Clazz>> entry : honorClassesMap.entrySet()) {
			List<Clazz> namesList = entry.getValue();
			Collections.sort(namesList, new Comparator<Clazz>(){
				@Override
				public int compare(Clazz o1, Clazz o2) {
					if(o1==null){
						return -1;
					}
					if(o2==null){
						return -1;
					}
					return o1.getState()-o2.getState();
				}
			});
 			String names = "";
			for (Clazz clazz : namesList) {
				if (clazz == null) {
					names += "," + "班级已删除";
				} else {
					names += "," + clazz.getClassNameDynamic();
				}
			}
			honorClassMap.put(entry.getKey(), names.substring(1));
		}
		return honorClassMap;
	}

	
	@Override
	public void saveHonor(EccHonor eccHonor) throws Exception{
		boolean isEdit = false;
		Date oldUpdateTime = null;
		if (StringUtils.isNotBlank(eccHonor.getId())) {
			isEdit = true;
			EccHonor oldEccHonor = findOne(eccHonor.getId());
			oldUpdateTime = oldEccHonor.getUpdateTime();
		}
		List<EccHonorTo> eccHonorTos = Lists.newArrayList();
		EccHonorTo eccHonorTo = null;
		if (EccConstants.ECC_HONOR_TYPE_1.equals(eccHonor.getType())) {
			String[] classIds = eccHonor.getClassId().split(",");
			if (StringUtils.isNotBlank(eccHonor.getId())) {
				eccHonorToService.deleteByHonorId(eccHonor.getId());
			} else {
				eccHonor.setId(UuidUtils.generateUuid());
			}
			for (String str : classIds) {
				eccHonorTo = new EccHonorTo();
				eccHonorTo.setId(UuidUtils.generateUuid());
				eccHonorTo.setType(eccHonor.getType());
				eccHonorTo.setHonorId(eccHonor.getId());
				eccHonorTo.setObjectId(str);
				eccHonorTos.add(eccHonorTo);
			}
		} else {
			if (StringUtils.isNotBlank(eccHonor.getId())) {
				eccHonorToService.deleteByHonorId(eccHonor.getId());
			} else {
				eccHonor.setId(UuidUtils.generateUuid());
				JSONObject cutImgSize = JSONObject.parseObject(eccHonor.getCutImgSize());
				Integer x = (int) Math.round(cutImgSize.getDouble("x"));
				Integer y = (int) Math.round(cutImgSize.getDouble("y"));
				Integer width = (int) Math.round(cutImgSize.getDouble("width"));
				Integer height = (int) Math.round(cutImgSize.getDouble("height"));
			
				List<AttFileDto> attFileDtos = Lists.newArrayList();
				AttFileDto attFileDto = new AttFileDto();
				attFileDto.setFileName(eccHonor.getFileName());
				attFileDto.setFilePath(eccHonor.getFilePath());
				attFileDto.setObjectType(EccConstants.ECC_ATTACHMENT_TYPE);
				attFileDto.setObjectId(eccHonor.getId());
				attFileDto.setObjectUnitId(eccHonor.getUnitId());
				attFileDtos.add(attFileDto);
				List<Attachment> attachments = SUtils.dt(attachmentRemoteService.saveAttachment(SUtils.s(attFileDtos)),new TR<List<Attachment>>(){});
				Attachment attachment = attachments.get(0);
				AttachmentUtils.saveCutPictrue(attachment.getDirPath(), attachment.getFilePath(), x, y, width, height, EccConstants.ECC_SMALL_IMG);
			}
			eccHonorTo = new EccHonorTo();
			eccHonorTo.setId(UuidUtils.generateUuid());
			eccHonorTo.setType(eccHonor.getType());
			eccHonorTo.setHonorId(eccHonor.getId());
			eccHonorTo.setObjectId(eccHonor.getStudentId());
			eccHonorTos.add(eccHonorTo);
		}
		eccHonor.setUpdateTime(new Date());
		eccHonorDao.save(eccHonor);
		eccHonorToService.saveAll(eccHonorTos.toArray(new EccHonorTo[0]));
		
		String beginTime = eccHonor.getBeginTime();
		String endTime = eccHonor.getEndTime();
		if(DateUtils.date2StringByMinute(new Date()).compareTo(endTime) < 0){
			//加入定时队列
			Calendar calendarEnd = Calendar.getInstance();
			if(DateUtils.date2StringByMinute(new Date()).compareTo(beginTime)>=0){
				calendarEnd.add(Calendar.SECOND, 5);//开始时间在当前时间之前，5秒后显示
				beginTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
				calendarEnd.add(Calendar.SECOND, -5);
			}else{
				beginTime+=":00";
			}
			// 初始化日期
			calendarEnd.setTime(DateUtils.string2DateTime(endTime+":00"));
			calendarEnd.add(Calendar.MINUTE, 1);
			endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
			EccTask honorTaskStart = new HonorTask(eccHonor.getId(),false);
			EccTask honorTaskEnd = new HonorTask(eccHonor.getId(),true);
			eccTaskService.addEccTaskBandE(honorTaskStart, honorTaskEnd, beginTime, endTime,eccHonor,oldUpdateTime,isEdit);
		}
	}

	@Override
	public void deleteHonor(String honorId,String attachmentId) throws Exception{
		if (StringUtils.isNotBlank(attachmentId)) {
			attachmentRemoteService.deleteAttachments(new String[]{attachmentId}, new String[]{EccConstants.ECC_SMALL_IMG});
		}
		EccHonor eccHonor = eccHonorDao.findById(honorId).orElse(null);
		List<EccHonorTo> eccHonorTos = eccHonorToService.findByHonorIdIn(new String[]{honorId});
		eccHonorDao.deleteById(honorId);
		eccHonorToService.deleteByHonorId(honorId);
		if (!EccConstants.ECC_HONOR_STATUS_3.equals(eccHonor.getStatus())) {
			eccTaskService.deleteEccTaskBandE(eccHonor, eccHonor.getUpdateTime());
		}
		if (EccConstants.ECC_HONOR_STATUS_2.equals(eccHonor.getStatus())) {
			List<String> clazzIds = null;
			if (CollectionUtils.isNotEmpty(eccHonorTos)) {
				if (EccConstants.ECC_HONOR_TYPE_1.equals(eccHonorTos.get(0).getType())) {
					clazzIds = EntityUtils.getList(eccHonorTos, "objectId");
				} else {
					EccHonorTo eccHonorTo = eccHonorTos.get(0);
					Student student = SUtils.dc(studentRemoteService.findOneById(eccHonorTo.getObjectId()), Student.class);
					clazzIds = Lists.newArrayList();
					clazzIds.add(student.getClassId());
				}
			}
			Set<String> sids = Sets.newHashSet();
			if (CollectionUtils.isNotEmpty(clazzIds)) {
				List<EccInfo> eccInfos = eccInfoService.findByClassIdIn(clazzIds.toArray(new String[0]));
				for (EccInfo eccInfo : eccInfos) {
					sids.add(eccInfo.getId());
				}
			}
			if (CollectionUtils.isNotEmpty(sids)) {
				EccNeedServiceUtils.postHonor(sids);
			}
		}
	}

	@Override
	public List<EccHonor> findByIdsAndTime(String[] honorIds, String time) {
		return eccHonorDao.findByIdsAndTime(honorIds,time);
	}

	@Override
	public void honorTaskRun(String honorId, boolean isEnd) {
		List<EccHonorTo> eccHonorTos = eccHonorToService.findByHonorIdIn(new String[]{honorId});
		List<String> clazzIds = null;
		if (CollectionUtils.isNotEmpty(eccHonorTos)) {
			if (EccConstants.ECC_HONOR_TYPE_1.equals(eccHonorTos.get(0).getType())) {
				clazzIds = EntityUtils.getList(eccHonorTos, "objectId");
			} else {
				EccHonorTo eccHonorTo = eccHonorTos.get(0);
				Student student = SUtils.dc(studentRemoteService.findOneById(eccHonorTo.getObjectId()), Student.class);
				clazzIds = Lists.newArrayList();
				clazzIds.add(student.getClassId());
			}
		}
		Set<String> sids = Sets.newHashSet();
		if (CollectionUtils.isNotEmpty(clazzIds)) {
			List<EccInfo> eccInfos = eccInfoService.findByClassIdIn(clazzIds.toArray(new String[0]));
			for (EccInfo eccInfo : eccInfos) {
				sids.add(eccInfo.getId());
			}
		}
		if (CollectionUtils.isNotEmpty(sids)) {
			EccNeedServiceUtils.postHonor(sids);
		}
		EccHonor eccHonor = findOne(honorId);
		if (eccHonor != null) {
			if(isEnd){
				eccHonor.setStatus(EccConstants.ECC_HONOR_STATUS_3);
			}else{
				eccHonor.setStatus(EccConstants.ECC_HONOR_STATUS_2);
			}
			save(eccHonor);
		}
	}

	@Override
	public List<EccHonor> findShowList(String classId, Integer type) {
		List<EccHonor> eccHonors = Lists.newArrayList();
		List<EccHonorTo> eccHonorTos = Lists.newArrayList();
		Map<String,String> studentNameMap = Maps.newHashMap();
		Map<String,String> stuHonorNameMap = Maps.newHashMap();
		if (EccConstants.ECC_HONOR_TYPE_1.equals(type)) {
			eccHonorTos = eccHonorToService.findByObjectIdIn(new String[]{classId});
		} else {
			List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
			List<String> stuIds = EntityUtils.getList(students, "id");
			eccHonorTos = eccHonorToService.findByObjectIdIn(stuIds.toArray(new String[0]));
			studentNameMap = EntityUtils.getMap(students, "id", "studentName");
			for (EccHonorTo eccHonorTo : eccHonorTos) {
				stuHonorNameMap.put(eccHonorTo.getHonorId(), studentNameMap.get(eccHonorTo.getObjectId()));
			}
		}
		Set<String> eccHonorIds = EntityUtils.getSet(eccHonorTos, "honorId");
		if (CollectionUtils.isNotEmpty(eccHonorIds)) {
			eccHonors = findListPeriod(eccHonorIds.toArray(new String[eccHonorIds.size()]));
			if (CollectionUtils.isNotEmpty(eccHonors)) {
				for (EccHonor eccHonor : eccHonors) {
					eccHonor.setStatus(EccConstants.ECC_HONOR_STATUS_2);
				}
				saveAll(eccHonors.toArray(new EccHonor[eccHonors.size()]));
			
				if (EccConstants.ECC_HONOR_TYPE_2.equals(type)) {
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(eccHonorIds.toArray(new String[0])),new TR<List<Attachment>>(){});
					Map<String,Attachment> pictureMap = EntityUtils.getMap(attachments,"objId");
					Attachment attachment = null;
					String smallFilePath = "";
					for (EccHonor eccHonor : eccHonors) {
						eccHonor.setStudentName(stuHonorNameMap.get(eccHonor.getId()));
						attachment = pictureMap.get(eccHonor.getId());
						eccHonor.setAttachmentId(attachment.getId());
						smallFilePath = AttachmentUtils.getAddSuffixName(attachment.getFilePath(), EccConstants.ECC_SMALL_IMG);
						attachment.setFilePath(smallFilePath);
						eccHonor.setPictureUrl(attachment.getShowPicUrl());
					}
				}
			}
		}
		return eccHonors;
	}
	
	@Override
	public List<EccHonor> findListPeriod(final String[] ids) {
		Specification<EccHonor> specification = new Specification<EccHonor>() {
            @Override
            public Predicate toPredicate(Root<EccHonor> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                String nowTime = DateUtils.date2StringBySecond(new Date());
            	ps.add(cb.greaterThanOrEqualTo(root.get("endTime").as(String.class),nowTime));
            	ps.add(cb.lessThanOrEqualTo(root.get("beginTime").as(String.class),nowTime));
            	if (null != ids&&ids.length > 0) {
                    In<String> in = cb.in(root.get("id").as(String.class));
                    for (int i = 0; i < ids.length; i++) {
                        in.value(ids[i]);
                    }
                    ps.add(in);
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("createTime").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
		return eccHonorDao.findAll(specification);
	}
	
	@Override
	public void addHonorQueue() {
		List<EccHonor> honors = findListNotShow();
		//加入定时队列
        Calendar calendarEnd = Calendar.getInstance();
        for(EccHonor honor:honors){
        	String beginTime = honor.getBeginTime();
        	String endTime = honor.getEndTime();
        	if(DateUtils.date2StringByMinute(new Date()).compareTo(beginTime)>0){
        		calendarEnd.add(Calendar.SECOND, 5);//开始时间在当前时间之前，5秒后显示
        		beginTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
        	}else{
        		beginTime+=":00";
        	}
        	// 初始化日期
        	calendarEnd.setTime(DateUtils.string2DateTime(endTime+":00"));
        	calendarEnd.add(Calendar.MINUTE, 1);
        	endTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
        	EccTask honorTaskStart = new HonorTask(honor.getId(),false);
        	EccTask honorTaskEnd = new HonorTask(honor.getId(),true);
        	eccTaskService.addEccTaskBandE(honorTaskStart, honorTaskEnd, beginTime, endTime, honor,null, false);
        }
		
	}
	
	private List<EccHonor> findListNotShow() {
		List<EccHonor> eccHonors = eccHonorDao.findListNotShow();
		List<EccHonor> updateHonors = Lists.newArrayList();
		for(EccHonor honor:eccHonors){
			if(DateUtils.date2StringByMinute(new Date()).compareTo(honor.getEndTime())>=0){
				honor.setStatus(EccConstants.ECC_HONOR_STATUS_3);
				updateHonors.add(honor);
			}
		}
		if(updateHonors.size()>0){
			saveAll(updateHonors.toArray(new EccHonor[updateHonors.size()]));
		}
		eccHonors.removeAll(updateHonors);
		return eccHonors;
	}

	@Override
	public List<EccHonor> findByHonorToObjIds(String[] honorToObjIds, Pagination page) {
		List<EccHonorTo> eccHonorTos = eccHonorToService.findByObjectIdIn(honorToObjIds);
		Set<String> honorIds = EntityUtils.getSet(eccHonorTos, EccHonorTo::getHonorId);
		if (CollectionUtils.isEmpty(honorIds)) {
			 return Lists.newArrayList();
		}
		Specification<EccHonor> specification = new Specification<EccHonor>() {
			 @Override
			 public Predicate toPredicate(Root<EccHonor> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 if (honorIds.size()> 0) {
                	 String[] honorIdArr = honorIds.toArray(new String[honorIds.size()]);
                	 queryIn("id", honorIdArr, root, ps, null);
                 }
				 List<Order> orderList = new ArrayList<Order>();
				 orderList.add(cb.desc(root.get("createTime").as(Date.class)));

				 cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				 return cq.getRestriction();
	         }
	     };
	     
		if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<EccHonor> findAll = eccHonorDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			return findAll.getContent();
		}
		else {
			return eccHonorDao.findAll(specification);
		}
	}
}
