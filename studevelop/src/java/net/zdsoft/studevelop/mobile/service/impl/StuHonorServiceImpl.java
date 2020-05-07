package net.zdsoft.studevelop.mobile.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.mobile.dao.StuHonorDao;
import net.zdsoft.studevelop.mobile.entity.StuAttachmentDto;
import net.zdsoft.studevelop.mobile.entity.StuHonor;
import net.zdsoft.studevelop.mobile.service.StuHonorService;
import net.zdsoft.studevelop.mobile.utils.ImageUtils;

@Service("stuHonorService")
public class StuHonorServiceImpl extends BaseServiceImpl<StuHonor,String> implements StuHonorService{
	
	@Autowired
	private StuHonorDao stuHonorDao;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	public void save(StuHonor item, MultipartFile file) {
		item.setId(UuidUtils.generateUuid());
		item.setCreationTime(new Date());
		StuHonor ent = stuHonorDao.save(item);
		if(file!=null){
//			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(ent.getId(), StuDevelopConstant.OBJTYPE_STUDEV_HONOR);
			Student stu = SUtils.dc(studentRemoteService.findOneById(ent.getStudentId()), Student.class);
			StudevelopAttachment att = new StudevelopAttachment();
			att.setObjecttype(StuDevelopConstant.OBJTYPE_STUDEV_HONOR);
			att.setObjId(ent.getId());
			att.setUnitId(stu.getSchoolId());
			studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
		}
	}

	public void update(StuHonor item, MultipartFile file) {
		stuHonorDao.updateById(item.getModifyTime(), item.getId());
		
		if(file!=null){
			Student stu = SUtils.dc(studentRemoteService.findOneById(item.getStudentId()), Student.class);
			StudevelopAttachment att = new StudevelopAttachment();
			att.setObjecttype(StuDevelopConstant.OBJTYPE_STUDEV_HONOR);
			att.setObjId(item.getId());
			att.setUnitId(stu.getSchoolId());
			studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
		}
	}

	public StuHonor findObj(String acadyear, String semester, String studentId){
		StuHonor item = null;
		List<StuHonor> honorList = stuHonorDao.findList(acadyear, semester, studentId);
		if (CollectionUtils.isNotEmpty(honorList)) {
			item = honorList.get(0);
		}
		if(item==null)
			return item;
		List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(item.getId(), StuDevelopConstant.OBJTYPE_STUDEV_HONOR);
		if(atts==null || atts.size()==0)
			return item;
		Iterator<StudevelopAttachment> iterator = atts.iterator();
		StuAttachmentDto dto = null;
		while(iterator.hasNext()){
			StudevelopAttachment a = iterator.next();
			if(StringUtils.isNotBlank(a.getSmallFullPath())){
				if(item.getImages()==null){
					item.setImages(new ArrayList<StuAttachmentDto>());
				}
				dto = new StuAttachmentDto();
				dto.setId(a.getId());
				dto.setFilePath(UrlUtils.ignoreFirstLeftSlash(a.getFilePath()));
				dto.setPath(ImageUtils.getImageUrl(null, a.getSmallFullPath(), StuDevelopConstant.OBJTYPE_STUDEV_HONOR));
				item.getImages().add(dto);
			}
		}
		return item;
	}

	@Override
	public List<StuHonor> getStuHonorList(String acadyear, String semester, String[] studentIds) {
		List<StuHonor> list = stuHonorDao.getStuHonorList(acadyear,semester,studentIds);
		if(CollectionUtils.isNotEmpty(list)){

			Set<String> ids = list.stream().map(StuHonor::getId).collect(Collectors.toSet());
			Map<String ,List<StudevelopAttachment> > attsMap = studevelopAttachmentService.findMapByObjIds(ids.toArray(new String[0]));
			for (StuHonor item : list) {
				List<StudevelopAttachment> atts = attsMap.get(item.getId());
				if(CollectionUtils.isNotEmpty(atts)){
					Iterator<StudevelopAttachment> iterator = atts.iterator();
					StuAttachmentDto dto = null;
					while(iterator.hasNext()){
						StudevelopAttachment a = iterator.next();
						if(StringUtils.isNotBlank(a.getSmallFullPath())){
							if(item.getImages()==null){
								item.setImages(new ArrayList<StuAttachmentDto>());
							}
							dto = new StuAttachmentDto();
							dto.setId(a.getId());
							dto.setFilePath(UrlUtils.ignoreFirstLeftSlash(a.getFilePath()));
							dto.setPath(ImageUtils.getImageUrl(null, a.getSmallFullPath(), StuDevelopConstant.OBJTYPE_STUDEV_HONOR));
							item.getImages().add(dto);
						}
					}
				}

			}

		}
		return list;
	}

	@Override
	protected BaseJpaRepositoryDao<StuHonor, String> getJpaDao() {
		return stuHonorDao;
	}

	@Override
	protected Class<StuHonor> getEntityClass() {
		return StuHonor.class;
	}

}
