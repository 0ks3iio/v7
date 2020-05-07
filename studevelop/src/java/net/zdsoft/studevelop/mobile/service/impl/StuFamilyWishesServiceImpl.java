package net.zdsoft.studevelop.mobile.service.impl;

import java.util.Date;
import java.util.List;

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
import net.zdsoft.studevelop.mobile.dao.StuFamilyWishesDao;
import net.zdsoft.studevelop.mobile.entity.StuFamilyWishes;
import net.zdsoft.studevelop.mobile.service.StuFamilyWishesService;
import net.zdsoft.studevelop.mobile.utils.ImageUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("stuFamilyWishesService")
public class StuFamilyWishesServiceImpl extends BaseServiceImpl<StuFamilyWishes,String> implements StuFamilyWishesService{
	
	@Autowired
	private StuFamilyWishesDao stuFamilyWishesDao;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	public void save(StuFamilyWishes item, MultipartFile file){
		StuFamilyWishes ent = stuFamilyWishesDao.save(item);
		if(file!=null){
			Student student = SUtils.dc(studentRemoteService.findOneById(ent.getStudentId()), Student.class);
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(ent.getId(), StuDevelopConstant.OBJTYPE_FALMILY_PIC);
			StudevelopAttachment att;
			if(CollectionUtils.isNotEmpty(atts)){
				att = atts.get(0);
			} else {
				att = new StudevelopAttachment();
			}
			att.setObjecttype(StuDevelopConstant.OBJTYPE_FALMILY_PIC);
			att.setObjId(ent.getId());
			att.setUnitId(student.getSchoolId());
			//保存附件
			studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
		}
	}
	
	@Override
	public void update(StuFamilyWishes item) {
		stuFamilyWishesDao.update(item.getParentContent(), item.getChildContent(), item.getModifyTime(), item.getAcadyear(), item.getSemester(), item.getStudentId());
	}
	
	@Override
	public void update(StuFamilyWishes item, MultipartFile file) {
		this.update(item);
		//更新附件
		if(file!=null){
			Student student = SUtils.dc(studentRemoteService.findOneById(item.getStudentId()), Student.class);
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(item.getId(), StuDevelopConstant.OBJTYPE_FALMILY_PIC);
			StudevelopAttachment att;
			if(CollectionUtils.isNotEmpty(atts)){
				att = atts.get(0);
			} else {
				att = new StudevelopAttachment();
			}
			att.setObjecttype(StuDevelopConstant.OBJTYPE_FALMILY_PIC);
			att.setObjId(item.getId());
			att.setUnitId(student.getSchoolId());
			//更新附件
			studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
		}
	}

	@Override
	public StuFamilyWishes findObj(String acadyear, String semester,
			String studentId) {
		StuFamilyWishes item =null; //stuFamilyWishesDao.findObj(acadyear, semester, studentId);
		StuFamilyWishes haveSaved =null;
		List<StuFamilyWishes> familyList=stuFamilyWishesDao.findListByStudentId(studentId);
		if(CollectionUtils.isNotEmpty(familyList)){
			boolean flag2=false;
			for(StuFamilyWishes family:familyList){
				if(family.getAcadyear().equals(acadyear) && family.getSemester().equals(semester)){
					item=family;
					break;
				}
				if(!flag2 && StringUtils.isNotBlank(family.getChildContent())){
					haveSaved=family;
					flag2=true;
				}
			}
			if(item==null && haveSaved!=null){//没有当前学年学期的幸福一家   有以往学年学期的
				try {
					item=new StuFamilyWishes();
					item.setId(UuidUtils.generateUuid());
					item.setCreationTime(new Date());
					item.setAcadyear(acadyear);
					item.setSemester(semester);
					item.setChildContent(haveSaved.getChildContent());
					item.setParentContent(haveSaved.getParentContent());
					item.setStudentId(studentId);
					save(item);
					studevelopAttachmentService.saveFile(haveSaved.getId(), StuDevelopConstant.OBJTYPE_FALMILY_PIC, item.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(item==null)
			return item;
		List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(item.getId(), StuDevelopConstant.OBJTYPE_FALMILY_PIC);
		if(CollectionUtils.isNotEmpty(atts)){
			StudevelopAttachment att = atts.get(0);
			if(att!=null && att.getSmallFile() != null && att.getSmallFile().exists()){
				item.setExistsImgPath(true);
				item.setImgPath(ImageUtils.getImageUrl(null, att.getSmallFullPath(), StuDevelopConstant.OBJTYPE_FALMILY_PIC));
				item.setFilePath(UrlUtils.ignoreFirstLeftSlash(att.getFilePath()));
				item.setImgAttId(att.getId());
				item.setImgAtt(att);
			}else{
				item.setExistsImgPath(false);
			}
		} 
		
		return item;
	}

	@Override
	public List<StuFamilyWishes> getStuFamily(String acadyear, String semester, String[] studentIds) {
		return stuFamilyWishesDao.getStuFamily(acadyear,semester,studentIds);
	}

	@Override
	protected BaseJpaRepositoryDao<StuFamilyWishes, String> getJpaDao() {
		return stuFamilyWishesDao;
	}

	@Override
	protected Class<StuFamilyWishes> getEntityClass() {
		return StuFamilyWishes.class;
	}
}
