package net.zdsoft.newstusys.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.dao.BaseStudentDao;
import net.zdsoft.newstusys.dao.StudentAbnormalFlowJdbcDao;
import net.zdsoft.newstusys.entity.BaseStudent;
import net.zdsoft.newstusys.entity.BaseStudentEx;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import net.zdsoft.newstusys.entity.StudentResume;
import net.zdsoft.newstusys.service.BaseStudentExService;
import net.zdsoft.newstusys.service.BaseStudentService;
import net.zdsoft.newstusys.service.StudentResumeService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Administrator on 2018/3/1.
 */
@Service("baseStudentService")
public class BaseStudentServiceImpl extends BaseServiceImpl<Student,String> implements BaseStudentService {
    private Logger log = Logger.getLogger(BaseStudentServiceImpl.class);
	
	@Autowired
    private BaseStudentDao baseStudentDao;
    @Autowired
    private FamilyRemoteService familyRemoteService;
    @Autowired
    private StudentResumeService studentResumeService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
	private BaseStudentExService baseStudentExService;
    @Autowired
    private FilePathRemoteService filePathRemoteService;
    @Autowired
	private StudentAbnormalFlowJdbcDao studentAbnormalFlowJdbcDao;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
    
    @Override
    protected BaseJpaRepositoryDao<Student, String> getJpaDao() {
        return baseStudentDao;
    }

    @Override
    protected Class<Student> getEntityClass() {
        return Student.class;
    }

    @Override
    public void saveStudent(BaseStudent student, List<Family> familyList, List<StudentResume> studentResumeList , boolean hasAddPic) {
        if(hasAddPic){
        	try {
				String fileSystemPath = filePathRemoteService.getFilePath();
				String filePath = BaseStudentConstants.PICTURE_FILEPATH + File.separator  + student.getSchoolId()
									+ File.separator + student.getId();
				String dirPath = fileSystemPath + File.separator + filePath;
				File dirFile = new File(dirPath);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				String suffix = org.apache.commons.lang3.StringUtils.substringAfterLast(student.getFilePath() ,".");
				File orFile = new File(fileSystemPath + File.separator + student.getFilePath());
				File desFile = new File( dirPath +File.separator + student.getStudentCode() +"."+ suffix);
				if(orFile != null && orFile.exists()){
					if(StringUtils.isNotEmpty(student.getOldFilePath())) {
				    	File oldFile = new File(fileSystemPath + File.separator + student.getOldFilePath());
				    	FileUtils.deleteQuietly(oldFile);
				    }
					FileUtils.copyFile(orFile, desFile);
				    FileUtils.deleteQuietly(orFile);
				}
				student.setDirId(BaseConstants.ZERO_GUID);
				student.setFilePath(filePath +File.separator + student.getStudentCode() +"."+ suffix);
			} catch (IOException e) {
				log.error("学生照片保存失败："+e.getMessage(), e);;
			}
        } else {
        	student.setFilePath(student.getOldFilePath());
        }
    	Student stu = new Student();
    	EntityUtils.copyProperties(student, stu);
    	if (StringUtils.isEmpty(stu.getNowState())) {
			stu.setNowState(BaseStudentConstants.NOWSTATE_DJ);
		}
		stu.setModifyTime(new Date());
        
    	if (CollectionUtils.isNotEmpty(familyList)) {
			familyRemoteService.saveAll(SUtils.s(familyList));
		}
		if(CollectionUtils.isNotEmpty(studentResumeList)){
            studentResumeService.saveAll(studentResumeList.toArray(new StudentResume[0]));
        }
        List<Student> baseStudents = new ArrayList<>();
        baseStudents.add(stu);
        BaseStudentEx ex = baseStudentExService.findOne(stu.getId());
        if(ex == null) {
        	ex = new BaseStudentEx();
			ex.setId(stu.getId());
        }
        ex.setSchoolId(stu.getSchoolId());
		ex.setModifyTime(new Date());
		ex.setIsDeleted(0);
        baseStudentExService.save(ex);
        studentRemoteService.saveAll(SUtils.s(baseStudents));
    }
    
    public void saveStudentForAbnormal(BaseStudent student , List<Family> familyList, 
    		List<StudentResume> studentResumeList, StudentAbnormalFlow flow, boolean hasAddPic) {
    	saveStudent(student, familyList, studentResumeList, hasAddPic);
    	flow.setStuid(student.getId());
    	flow.setSchid(student.getSchoolId());
    	flow.inToFlow(student);
    	flow.setCurrentclassid(student.getClassId());
    	flow.setFlowto(student.getClassId());
    	flow.setRemark(student.getRemark());
    	Semester sem = Semester.dc(semesterRemoteService.getCurrentSemester(1, flow.getSchid()));
		if(sem != null) {
			flow.setAcadyear(sem.getAcadyear());
			flow.setSemester(sem.getSemester()+"");
		}
		studentAbnormalFlowJdbcDao.save(flow);
    }
    
    public String saveStudentPics(String unitId) {
    	
    	String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String filePath = "upload" + File.separator + ymd + File.separator
				+ (unitId+BaseStudentConstants.PICTURE_FILEPATH);
		String tempDirPath = fileSystemPath + File.separator + filePath;
		File dir = new File(tempDirPath);
		if(dir == null || !dir.exists()){
			return "临时目录不存在或已被删除，取不到要保存的文件！";
		}
		File[] files = dir.listFiles();
		if(ArrayUtils.isEmpty(files)){
			return "临时目录下没有待保存的文件！";
		}
		String toFileSystemPath = filePathRemoteService.getFilePath();
		Map<String, File> codeFile = new HashMap<String, File>();
		for (int i=0;i<files.length;i++) {
			File file = files[i];
			try {
				String fn = file.getName();
				String code = fn;
				if(fn.indexOf(".") > 0) {
					code = fn.substring(0, fn.lastIndexOf("."));
				}
				if(codeFile.containsKey(code)) {
					System.out.println("文件【"+fn+"】重复，已删除");
					FileUtils.deleteQuietly(file);
				} else {
					codeFile.put(code, file);
				}
			} catch (Exception e) {
				log.error("第"+(i+1)+"个附件保存失败："+e.getMessage(), e);
			}
		}
		List<Student> stus = Student.dt(studentRemoteService.findBySchIdStudentCodes(unitId, codeFile.keySet().toArray(new String[0])));
		Map<String, Student> stuMap = EntityUtils.getMap(stus, Student::getStudentCode);
		Iterator<Entry<String, File>> it = codeFile.entrySet().iterator();
		String schPath = BaseStudentConstants.PICTURE_FILEPATH + File.separator + unitId;
		List<String> oldFilePaths = new ArrayList<String>();
		List<Student> toSaves = new ArrayList<Student>();
		while(it.hasNext()) {
			Entry<String, File> en = it.next();
			File pic = en.getValue();
			try {
				Student stu = stuMap.get(en.getKey());
				if(stu == null) {
					FileUtils.deleteQuietly(pic);
					System.out.println("文件【"+pic.getName()+"】匹配不上，已删除");
					it.remove();
					continue;
				}
				String stuFilePath = schPath + File.separator + stu.getId();
				File sdir = new File(toFileSystemPath + File.separator + stuFilePath);
				if(!sdir.exists()) {
					sdir.mkdirs();
				}
				stuFilePath += File.separator +pic.getName();
				if(StringUtils.isNotEmpty(filePath)) {
					oldFilePaths.add(stu.getFilePath());
				}
				stu.setDirId(BaseConstants.ZERO_GUID);
				stu.setFilePath(stuFilePath);
				stu.setModifyTime(new Date());
				toSaves.add(stu);
			} catch (Exception e) {
				log.error("文件【"+pic.getName()+"】处理失败", e);
			}
		}
		studentRemoteService.saveAllEntitys(SUtils.s(toSaves));
		for(String oldFilePath : oldFilePaths) {
			File oldPic = new File(toFileSystemPath + File.separator + oldFilePath);
			FileUtils.deleteQuietly(oldPic);// 删除原学生照片
		}
		for(Student stu : toSaves) {
			File pic = codeFile.get(stu.getStudentCode());
			try {
				File destPic = new File(toFileSystemPath + File.separator + stu.getFilePath());
				FileUtils.copyFile(pic, destPic);
				FileUtils.deleteQuietly(pic);// 删除临时文件
			} catch (IOException e) {
				log.error("文件【"+pic.getName()+"】处理失败", e);;
			}
		}
		return "";
    }

    @Override
    public void deleteByStuIds(String[] stuIds) {
    	baseStudentDao.deleteByStuIds(stuIds);
        baseStudentExService.deleteByStuIds(stuIds);
        studentResumeService.deleteResumeByStuId(stuIds);
        Map<String, String> wv = new HashMap<String, String>();
        //update T set valueXpath=V where whereXpath=W;
        List<String> ownerIds = new ArrayList<>();
        for(String sid : stuIds) {
        	wv.put(sid, "1");
        	ownerIds.add(sid);
        }
        List<Family> fams = SUtils.dt(familyRemoteService.findByStudentIds(stuIds), Family.class);
        
        if (CollectionUtils.isNotEmpty(fams)) {
        	ownerIds.addAll(EntityUtils.getList(fams, Family::getId));
			familyRemoteService.update(wv, "studentId", "isDeleted");
		}
		UserRemoteService userRemoteService = Evn.getBean("userRemoteService");
        if(userRemoteService != null) {
        	//TODO
        	List<User> us = User.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[0])));
        	if(CollectionUtils.isNotEmpty(us)) {
        		userRemoteService.deleteAllByIds(EntityUtils.getList(us, User::getId).toArray(new String[0]));
        	}
        }
    }


    public List<Student> findStudentByClsIds(String[] clsIds, String searchType, Pagination page){
    	List<Student> stuList = new ArrayList<>();
    	if(ArrayUtils.isEmpty(clsIds)) {
    		return stuList;
    	}
    	Specification<Student> specification = new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				ps.add(cb.equal(root.get("isLeaveSchool").as(Integer.class), 0));
				queryIn("classId", clsIds, root, ps, cb);
				if (StringUtils.isNotEmpty(searchType)) {
					if ("1".equals(searchType)) {// 市外户籍
						ps.add(root.get("source").as(String.class).in(new Object[] {BaseStudentConstants.SOURCE_SNSW, BaseStudentConstants.SOURCE_SW}));
					} else if("2".equals(searchType)) {//无户口学生 
						ps.add(cb.equal(root.get("source").as(String.class), BaseStudentConstants.SOURCE_NONE));
					} else if("3".equals(searchType)) {//随迁子女（总）
						ps.add(cb.equal(root.get("isMigration").as(String.class), BaseConstants.ONE_STR));
					} else if("4".equals(searchType)) {//随迁子女（省外）
						ps.add(cb.equal(root.get("isMigration").as(String.class), BaseConstants.ONE_STR));
						ps.add(cb.equal(root.get("source").as(String.class), BaseStudentConstants.SOURCE_SW));
					} else if("5".equals(searchType)) {//留守儿童
						ps.add(cb.equal(root.get("stayin").as(Integer.class), 1));
					} else if("6".equals(searchType)) {//随班就读
						ps.add(cb.equal(root.get("source").as(String.class), BaseStudentConstants.SOURCE_NONE));
					} else if("7".equals(searchType)) {//外籍​学生
						ps.add(cb.notEqual(root.get("country").as(String.class), BaseStudentConstants.COUNTRY_CHINA));
					} else if("8".equals(searchType)) {//港澳台生
						ps.add(cb.notEqual(root.get("compatriots").as(String.class), 0));
					}
				}
				
				Predicate andPredicate = cb.and(ps.toArray(new Predicate[ps.size()]));
				List<Order> orderList = new ArrayList<Order>();
//				orderList.add(cb.asc(root.get("classId").as(String.class)));
//				orderList.add(cb.asc(root.get("studentCode").as(String.class)));
				cq.where(andPredicate).orderBy(orderList);
				return cq.getRestriction();
			}
    	};
    	
    	if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<Student> findAll = baseStudentDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			return findAll.getContent();
		} else {
			return baseStudentDao.findAll(specification);
		}
    }
}
