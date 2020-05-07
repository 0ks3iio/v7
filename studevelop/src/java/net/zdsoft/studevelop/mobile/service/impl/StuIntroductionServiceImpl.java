package net.zdsoft.studevelop.mobile.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.studevelop.mobile.dao.StuIntroductionDao;
import net.zdsoft.studevelop.mobile.entity.StuIntroduction;
import net.zdsoft.studevelop.mobile.service.StuIntroductionService;
import net.zdsoft.studevelop.mobile.utils.ImageUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

@Service("stuIntroductionService")
public class StuIntroductionServiceImpl extends BaseServiceImpl<StuIntroduction,String> implements StuIntroductionService{
	
	@Autowired
	private StuIntroductionDao stuIntroductionDao;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	
	public void save(StuIntroduction item){
		stuIntroductionDao.save(item);
	}
	
	public void save(StuIntroduction item, MultipartFile file){
		StuIntroduction ent = stuIntroductionDao.save(item);
		if(file!=null){
			Student student = SUtils.dc(studentRemoteService.findOneById(ent.getStudentId()), Student.class);
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(ent.getId(), StuDevelopConstant.OBJTYPE_STU_PIC);
			StudevelopAttachment att;
			if(CollectionUtils.isNotEmpty(atts)){
				att = atts.get(0);
			} else {
				att = new StudevelopAttachment();
			}
			att.setObjecttype(StuDevelopConstant.OBJTYPE_STU_PIC);
			att.setObjId(ent.getId());
			att.setUnitId(student.getSchoolId());
			//保存附件
			studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
		}
	}
	
	@Override
	public void update(StuIntroduction item) {
		stuIntroductionDao.update(item.getSpeciality(), item.getContent(), item.getModifyTime(), item.getHasRelease(), item.getAcadyear(), item.getSemester(), item.getStudentId());
	}
	
	public void update(StuIntroduction item, MultipartFile file){
		this.update(item);
		//更新附件
		if(file!=null){
			Student student = SUtils.dc(studentRemoteService.findOneById(item.getStudentId()), Student.class);
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(item.getId(), StuDevelopConstant.OBJTYPE_STU_PIC);
			StudevelopAttachment att;
			if(CollectionUtils.isNotEmpty(atts)){
				att = atts.get(0);
			} else {
				att = new StudevelopAttachment();
			}
			att.setObjecttype(StuDevelopConstant.OBJTYPE_STU_PIC);
			att.setObjId(item.getId());
			att.setUnitId(student.getSchoolId());
			//保存附件
			studevelopAttachmentService.saveAttachment(att, file, ImageUtils.orientationRotate(file));
		}
	}

	@Override
	public StuIntroduction findObj(String acadyear, String semester,
			String studentId) {
		StuIntroduction item = null;//stuIntroductionDao.findObj(acadyear, semester, studentId);
		List<StuIntroduction> stuInTroList=stuIntroductionDao.findListByStudentId(studentId);
		StuIntroduction haveSaved = null;
		if(CollectionUtils.isNotEmpty(stuInTroList)){
			boolean flag1=false;
			boolean flag2=false;
			for(StuIntroduction stuInTro:stuInTroList){
				if(!flag1 && stuInTro.getAcadyear().equals(acadyear) && stuInTro.getSemester().equals(semester)){
					item=stuInTro;
					flag1=true;
				}
				if(!flag2 && StringUtils.isNotBlank(stuInTro.getContent())){
					haveSaved=stuInTro;
					flag2=true;
				}
			}
			if(haveSaved!=null) {//存在已保存过信息的自我介绍
				try {
					if(item==null){//null的情况 此学年学期 未发布 且未保存过信息
						item=new StuIntroduction();
						item.setId(UuidUtils.generateUuid());
						item.setHasRelease(0);
						item.setStudentId(studentId);
						item.setAcadyear(acadyear);
						item.setSemester(semester);
						item.setSpeciality(haveSaved.getSpeciality());
						item.setContent(haveSaved.getContent());
						item.setCreationTime(new Date());
						save(item);
						studevelopAttachmentService.saveFile(haveSaved.getId(), StuDevelopConstant.OBJTYPE_STU_PIC, item.getId());
					}else{
						if(StringUtils.isBlank(item.getContent())){//已发布 但未保存对应信息
							item.setHasRelease(1);
							item.setSpeciality(haveSaved.getSpeciality());
							item.setContent(haveSaved.getContent());
							item.setModifyTime(new Date());
							save(item);
							studevelopAttachmentService.saveFile(haveSaved.getId(), StuDevelopConstant.OBJTYPE_STU_PIC, item.getId());
						}//已保存过对应的信息（发布或者未发布不一定 无需处理）
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(item==null)
			return item;
		List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(item.getId(), StuDevelopConstant.OBJTYPE_STU_PIC);
		if(CollectionUtils.isNotEmpty(atts)){
			StudevelopAttachment att = atts.get(0);
			if (att!=null && att.getSmallFile() != null) {
				item.setImgPath(ImageUtils.getImageUrl(null,
						att.getSmallFullPath(),
						StuDevelopConstant.OBJTYPE_STU_PIC));
				item.setFilePath(UrlUtils.ignoreFirstLeftSlash(att.getFilePath())+"?_="+System.currentTimeMillis());
				item.setImgAttId(att.getId());
				item.setImgAtt(att);
			}
		} 
		return item;
	}
	@Override
	public List<StuIntroduction>  findListByStudentId(String studentId,Integer hasRelease){
		if(hasRelease==null){
			return stuIntroductionDao.findListByStudentId(studentId);
		}else{
			return stuIntroductionDao.findListByStudentId(studentId, hasRelease);
		}
	}
	@Override
	public List<StuIntroduction> findStudentsIntro(String acadyear, String semester, String[] studentIds) {
		List<StuIntroduction> stuIntroductions = stuIntroductionDao.findStudentsIntro(acadyear,semester,studentIds);
		if(CollectionUtils.isEmpty(stuIntroductions)){
			return stuIntroductions;
		}else{
			List<String> objIds = new ArrayList<>();
			for(StuIntroduction introduction : stuIntroductions){
				objIds.add(introduction.getId());
			}
			Map<String , List<StudevelopAttachment> > attMap = studevelopAttachmentService.findMapByObjIds(objIds.toArray(new String[]{}));
			for(StuIntroduction introduction : stuIntroductions){
				List<StudevelopAttachment> atts = attMap.get(introduction.getId());
				if(CollectionUtils.isNotEmpty(atts)){
					StudevelopAttachment att = atts.get(0);
					if (att!=null && att.getOriginFile()!=null && att.getOriginFile().exists()) {
						introduction.setImgPath(ImageUtils.getImageUrl(null,
								att.getSmallFullPath(),
								StuDevelopConstant.OBJTYPE_STU_PIC));
						introduction.setFilePath(UrlUtils.ignoreFirstLeftSlash(att.getFilePath()));
						introduction.setImgAttId(att.getId());
						introduction.setImgAtt(att);
					}
				}
			}
			return stuIntroductions;
		}


	}

	protected BaseJpaRepositoryDao<StuIntroduction, String> getJpaDao() {
		return stuIntroductionDao;
	}

	@Override
	protected Class<StuIntroduction> getEntityClass() {
		return StuIntroduction.class;
	}
	@Override
	public String getFileURL(){
	    HttpServletRequest request = Evn.getRequest();

	    Boolean isSecondUrl;
	    if ( request != null ) {
	        isSecondUrl =sysOptionRemoteService.isSecondUrl(request.getServerName());
        } else {
	        isSecondUrl = false;
        }
	    String fileUrl="";
        if ( isSecondUrl ) {
        	fileUrl=sysOptionRemoteService.findValue(Constant.FILE_SECOND_URL);
        }else {
        	//fileUrl=sysOptionRemoteService.getFileUrl(request.getServerName());
        	fileUrl=sysOptionRemoteService.findValue(Constant.FILE_URL);
        }
//        fileUrl = "Http://192.168.0.160:8043";//TODO
        if (StringUtils.endsWith(fileUrl, "/")) {
			fileUrl = fileUrl + "store/";
		} else {
			fileUrl = fileUrl + "/store/";
		}
        return fileUrl;
    }
}
