package net.zdsoft.studevelop.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.mobile.dao.StuOutsideDao;
import net.zdsoft.studevelop.mobile.entity.StuAttachmentDto;
import net.zdsoft.studevelop.mobile.entity.StuOutside;
import net.zdsoft.studevelop.mobile.service.StuOutsideService;
import net.zdsoft.studevelop.mobile.utils.ImageUtils;

@Service("stuOutsideService")
public class StuOutsideServiceImpl extends BaseServiceImpl<StuOutside,String> implements StuOutsideService{
	
	@Autowired
	private StuOutsideDao stuOutsideDao;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	public void save(StuOutside item){
		if(StringUtils.isBlank(item.getId())){
			item.setId(UuidUtils.generateUuid());
		}
		stuOutsideDao.save(item);
	}
	
	public void save(StuOutside item, List<MultipartFile> files){
		if(StringUtils.isBlank(item.getId())){
			item.setId(UuidUtils.generateUuid());
		}
		StuOutside ent = stuOutsideDao.save(item);
		Student stu = SUtils.dc(studentRemoteService.findOneById(ent.getStudentId()), Student.class);
		
		
		//删除
		if(StringUtils.isNotBlank(item.getDelImgIds())){
			String[] ids = item.getDelImgIds().split(",");
			studevelopAttachmentService.delete(ids);
		}
		//新增
		String objType = toTypeStr(item.getType());
		if(files!=null && files.size()>0){
			//附件上传
			Iterator<MultipartFile> iterators = files.iterator();
			StudevelopAttachment att = null;
			MultipartFile file = null;
			while(iterators.hasNext()){
				file = iterators.next();
				att = new StudevelopAttachment();
				att.setObjecttype(objType);
				att.setObjId(ent.getId());
				att.setUnitId(stu.getSchoolId());
				studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
			}
		}
	}
	
	public void update(StuOutside item){
		stuOutsideDao.update(item.getTitle(), item.getContent(), item.getModifyTime(), item.getId());
	}
	
	public void update(StuOutside item, List<MultipartFile> files){
		this.update(item);
		
		//删除
		if(StringUtils.isNotBlank(item.getDelImgIds())){
			String[] ids = item.getDelImgIds().split(",");
			studevelopAttachmentService.delete(ids);
		}
		String objType = toTypeStr(item.getType());
		//更新附件
		Student stu = SUtils.dc(studentRemoteService.findOneById(item.getStudentId()), Student.class);
		if(files!=null && files.size()>0){
			//附件上传
			Iterator<MultipartFile> iterators = files.iterator();
			StudevelopAttachment att = null;
			MultipartFile file = null;
			while(iterators.hasNext()){
				file = iterators.next();
				att = new StudevelopAttachment();
				att.setObjecttype(objType);
				att.setObjId(item.getId());
				att.setUnitId(stu.getSchoolId());
				studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
			}
		}
	}
	
	public StuOutside findObj(String id){
		StuOutside item = stuOutsideDao.findObj(id);
		if(item==null)
			return item;
		String objType = toTypeStr(item.getType());
		List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(item.getId(), objType);
		if(atts==null || atts.size()==0)
			return item;
		
		String typeStr = toTypeStr(item.getType());
		if(atts!=null && atts.size()>0){
			Iterator<StudevelopAttachment> iterator = atts.iterator();
			StuAttachmentDto dto = null;
			while(iterator.hasNext()){
				StudevelopAttachment ent = iterator.next();
				if(StringUtils.isNotBlank(ent.getSmallFullPath())){
					if(item.getImages()==null){
						item.setImages(new ArrayList<StuAttachmentDto>());
					}
					dto = new StuAttachmentDto();
					dto.setId(ent.getId());
					dto.setFilePath(UrlUtils.ignoreFirstLeftSlash(ent.getFilePath()));
					dto.setPath(ImageUtils.getImageUrl(null, ent.getSmallFullPath(), typeStr));
					item.getImages().add(dto);
				}
			}
		} 
		return item;
	}

	@Override
	public List<StuOutside> findList(String acadyear, String semester, String studentId, int type) {
		List<StuOutside> list = stuOutsideDao.findList(acadyear, semester, studentId, type);

		return handleList(list);
	}
	private List<StuOutside>  handleList( List<StuOutside> list){
		if(CollectionUtils.isEmpty(list))
			return list;

		Set<String> ids = list.stream().map(StuOutside::getId).collect(Collectors.toSet());

		Map<String, List<StudevelopAttachment>> map = studevelopAttachmentService.findMapByObjIds(ids.toArray(new String[0]));
		Iterator<StuOutside> iterator1 = list.iterator();
		while(iterator1.hasNext()){
			StuOutside ent = iterator1.next();
			if(map.containsKey(ent.getId())){
				List<StudevelopAttachment> l = map.get(ent.getId());
				if(l!=null && l.size()>0){
					ent.setImageCount(l.size());
					ent.setImagePath(ImageUtils.getImageUrl(null, l.get(0).getSmallFullPath(), null));
				}
			}
		}
		return list;
	}
	@Override
	public List<StuOutside> findStuOutSideList(String acadyear, String semester, String[] studentIds) {
		List<StuOutside> list = stuOutsideDao.findStuOutSideList(acadyear,semester,studentIds);

		return handleList(list);
	}

	private String toTypeStr(int type){
		if(type==StuOutside.TYPE_1){
			return StuDevelopConstant.ACTIVITY_TYPE_OUT_SCHOOL_PERFORMANCE;
		}else{
			return StuDevelopConstant.ACTIVITY_TYPE_KIDS_HOLIDAY;
		}
	}

	@Override
	protected BaseJpaRepositoryDao<StuOutside, String> getJpaDao() {
		return stuOutsideDao;
	}

	@Override
	protected Class<StuOutside> getEntityClass() {
		return StuOutside.class;
	}
}
