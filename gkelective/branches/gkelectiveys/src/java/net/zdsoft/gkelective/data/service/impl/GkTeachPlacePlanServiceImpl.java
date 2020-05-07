package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkTeachPlacePlanDao;
import net.zdsoft.gkelective.data.dto.GkTeachPlacePlanDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkClassStore;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkStuConversion;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkClassStoreService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkStuConversionService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachClassExService;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;
@Service("gkTeachPlacePlanService")
public class GkTeachPlacePlanServiceImpl extends BaseServiceImpl<GkTeachPlacePlan, String> implements GkTeachPlacePlanService{
	@Autowired
	private GkTeachPlacePlanDao gkTeachPlacePlanDao;
	@Autowired
	private GkRoundsService gkRoundsService;
	@Autowired
	private GkBatchService gkBatchService;
	@Autowired
	private GkTeachClassStoreService gkTeachClassStoreService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
    private GkGroupClassService gkGroupClassService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private GkTeachClassExService gkTeachClassExService;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private GkTimetableLimitArrangService gkTimetableLimitArrangService;
	@Autowired
	private GkSubjectService gkSubjectService;
	@Autowired
	private GkRelationshipService gkRelationshipService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private GkStuConversionService gkStuConversionService;
	@Autowired
	private GkClassStoreService gkClassStoreService;
	@Override
	protected BaseJpaRepositoryDao<GkTeachPlacePlan, String> getJpaDao() {
		return gkTeachPlacePlanDao;
	}

	@Override
	protected Class<GkTeachPlacePlan> getEntityClass() {
		return GkTeachPlacePlan.class;
	}

	@Override
	public List<GkTeachPlacePlan> findBySubjectArrangeId(String arrangeId,boolean isMakeRounds) {
		List<GkTeachPlacePlan> list = gkTeachPlacePlanDao.findBySubjectArrangeId(arrangeId);
		if(CollectionUtils.isEmpty(list)){
			return list;
		}
		if(isMakeRounds){
			//组长轮次
			makeRounds(list);
		}
		return list;
	}
	private void makeRounds(List<GkTeachPlacePlan> list){
		Set<String> roundIds=new HashSet<String>();
		for(GkTeachPlacePlan plan:list){
			roundIds.add(plan.getRoundsId());
		}
		Map<String, GkRounds> roundsMap = gkRoundsService.findMapByIdIn(roundIds.toArray(new String[]{}));
		for(GkTeachPlacePlan plan:list){
			if(roundsMap.containsKey(plan.getRoundsId())){
				plan.setGkRounds(roundsMap.get(plan.getRoundsId()));
			}
		}
	}

	private GkBatch copyBath(GkBatch bath){
		GkBatch returnBath=new GkBatch();
		returnBath.setId(UuidUtils.generateUuid());
		returnBath.setCreationTime(new Date());
		returnBath.setModifyTime(new Date());
		returnBath.setBatch(bath.getBatch());
		returnBath.setClassType(bath.getClassType());
    	return returnBath;
	}
	private void makeTeachClass(TeachClass teachClass,
			List<TeachClassStu> teachClassStuList, GkTeachClassStore gkClass,String acadyear,String semester) {
		teachClass.setId(UuidUtils.generateUuid());
		teachClass.setAcadyear(acadyear);
		teachClass.setSemester(semester);
		teachClass.setCourseId(gkClass.getSubjectId());
		teachClass.setUnitId(gkClass.getUnitId());
		teachClass.setIsDeleted(GkElectveConstants.USE_FALSE);
		//默认7选3
    	teachClass.setClassType(TeachClass.CLASS_TYPE_SEVEN);
    	teachClass.setGradeId(gkClass.getGradeId());
    	teachClass.setCreationTime(new Date());
    	teachClass.setModifyTime(new Date());
    	teachClass.setIsUsing(GkElectveConstants.TRUE_STR);
    	teachClass.setName(gkClass.getClassName());
    	//默认32个0
    	teachClass.setTeacherId(BaseConstants.ZERO_GUID);
    	TeachClassStu teachClassStu = null;
    	for(String s:gkClass.getStuList()){
    		teachClassStu = new TeachClassStu();
    		teachClassStu.setClassId(teachClass.getId());
    		teachClassStu.setId(UuidUtils.generateUuid());
    		teachClassStu.setCreationTime(new Date());
    		teachClassStu.setModifyTime(new Date());
    		teachClassStu.setIsDeleted(GkElectveConstants.USE_FALSE);
    		teachClassStu.setStudentId(s);
    		teachClassStuList.add(teachClassStu);
    	}
		
	}
	
	private String nameSet(Map<String,Course> courseMap,String ids){
		String[] s=ids.split(",");
		String returnS="";
		for(String s1:s){
			returnS=returnS+StringUtils.trimToEmpty(courseMap.get(s1)==null?"":courseMap.get(s1).getShortName());// 简称
		}
		return returnS;
	}
	@Override
	public void savePlan(GkTeachPlacePlan plan) {
		GkRounds rounds = gkRoundsService.findRoundById(plan.getRoundsId());
		if(rounds==null){
			 throw new RuntimeException("当前选择轮次已经不存在");
		}
		GkSubjectArrange arrange = gkSubjectArrangeService.findArrangeById(rounds.getSubjectArrangeId());
		if(arrange==null){
			 throw new RuntimeException("当前系统已经不存在");
		}
		Date nowDate = new Date();
		this.save(plan);
		//不启用其他的plan的限制
		List<GkTeachPlacePlan> planList = this.findBySubjectArrangeId(arrange.getId(), false);
		if(CollectionUtils.isNotEmpty(planList)){
			Set<String> planids = EntityUtils.getSet(planList, "id");
			Map<String,GkTeachPlacePlan> planMap = EntityUtils.getMap(planList, "id");
			
			for(String pId : planids){
				gkTimetableLimitArrangService.notUsing(arrange.getUnitId(),planMap.get(pId).getAcadyear(), planMap.get(pId).getSemester(), pId);
			}
		}
		Set<String> courseIds=new HashSet<String>();
		Map<String,Course> courseMap=new HashMap<String, Course>();
		
		//复制课程设置
		List<GkSubject> subjectList = gkSubjectService.findByRoundsId(rounds.getId(), null);
		List<GkSubject> insertGkSubjectList=new ArrayList<GkSubject>();
		GkSubject gkSubject=null;
		if(CollectionUtils.isNotEmpty(subjectList)){
			for(GkSubject g:subjectList){
				gkSubject=new GkSubject();
				gkSubject.setId(UuidUtils.generateUuid());
				gkSubject.setRoundsId(plan.getId());
				gkSubject.setSubjectId(g.getSubjectId());
				gkSubject.setTeachModel(g.getTeachModel());
				courseIds.add(g.getSubjectId());
				insertGkSubjectList.add(gkSubject);
			}
		}
		if(CollectionUtils.isNotEmpty(insertGkSubjectList)){
			gkSubjectService.deleteByRoundsId(plan.getId());//理论上不需要
			gkSubjectService.saveAll(insertGkSubjectList.toArray(new GkSubject[]{}));
		
			List<Course> courseList = SUtils.dt(
					courseRemoteService.findListByIds(courseIds.toArray(new String[0])),
					new TR<List<Course>>() {});
			
			courseMap=EntityUtils.getMap(courseList, "id");
		}else{
			 throw new RuntimeException("当前选择轮次数据不完整，缺少科目信息");
		}
		
		//包括组合下学生
		List<GkGroupClass> groupList = gkGroupClassService.findGkGroupClassBySubjectIds(null,rounds.getId());
		
		//key:原来groupClassId value:新复制的groupClass
		Map<String,GkGroupClass> oldTonewGroupClass=new HashMap<String, GkGroupClass>();
		List<GkGroupClass> insertGroupClassList=new ArrayList<GkGroupClass>();
		List<GkGroupClass> updateGroupClassList=new ArrayList<GkGroupClass>();//对旧以新的classId
		
		
		List<GkGroupClassStu> insertGroupClassStuList=new ArrayList<GkGroupClassStu>();
		GkGroupClass groupClass=null;
		GkGroupClassStu groupClassStu=null;
		GkStuConversion gkStuConversion=null;
		//写入正式表
		if(GkRounds.OPENT_CLASS_TYPE_1.equals(rounds.getOpenClassType())){
			
			//年级 
			Grade grade = SUtils.dt(gradeRemoteService.findOneById(arrange.getGradeId()), new TR<Grade>(){});
			if(grade==null){
				throw new RuntimeException("当前系统年级找不到");
			}
			//原行政班
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findByInGradeIds(new String[]{arrange.getGradeId()}),
                    new TR<List<Clazz>>() {});
			Map<String,Clazz> clazzMap=new HashMap<String, Clazz>();
			if(CollectionUtils.isNotEmpty(clazzList)){
				clazzMap=EntityUtils.getMap(clazzList, "id");
			}
			//List<Clazz> insertClazzList=new ArrayList<Clazz>();
			//List<Clazz> updateClazzList=new ArrayList<Clazz>();
			List<Clazz> allClazzList=new ArrayList<Clazz>();
			Set<String> delClazzIds=clazzMap.keySet();
			
			List<GkStuConversion> gkStuConversionList = new ArrayList<GkStuConversion>();
			List<GkClassStore> gkClassStoreList=new ArrayList<GkClassStore>();//备份班级--与班级原字段一样
			GkClassStore store=null;
			//拿到年级下所有学生
			List<Student> studentList=Student.dt(studentRemoteService.findByGradeIds(new String[]{arrange.getGradeId()}));
			Map<String,Student> stuMap=new HashMap<String, Student>();
			if(CollectionUtils.isNotEmpty(studentList)){
				stuMap=EntityUtils.getMap(studentList, "id");
			}
			Set<String> stuIds=new HashSet<String>();//未安排的学生
			stuIds=stuMap.keySet();

			//组合名称 是不是随机
			Clazz clazz=null;
			Student student=null;
			List<Student> updateStudentList=new ArrayList<Student>();
			//班级名称序号
			int i=0;
			//班级code前缀  入学年份+2位学段+2位序号
			int enrollYear=NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
			String sectionStr = StringUtils.leftPad(grade.getSection()+"", 2, "0");
			
			for(GkGroupClass g:groupList){
				//班级下人数为0 过滤
				if(CollectionUtils.isEmpty(g.getStuIdList())){
					continue;
				}
				String comName=null;
				if(GkElectveConstants.GROUP_TYPE_3.equals(g.getGroupType())){
					//组合名
					comName = "混合";
				}else{
					//组合名
					comName = nameSet(courseMap,g.getSubjectIds());
				}
				
				//班级名称
				i++;
				String className="("+i+")班";
				//班级code
				String classCode=enrollYear + sectionStr +StringUtils.leftPad(i+"", 2, "0");
				
				clazz=new Clazz();
				clazz.setRemark("因7选3重组班级，该班级定位为"+(comName==null?"":comName)+"组合");
				clazz.setClassName(className);
				clazz.setClassCode(classCode);
				clazz.setSchoolId(grade.getSchoolId());
				clazz.setAcadyear(grade.getOpenAcadyear());
				clazz.setIsGraduate(GkElectveConstants.USE_FALSE);
				clazz.setIsDeleted(GkElectveConstants.USE_FALSE);
				clazz.setSchoolingLength(grade.getSchoolingLength());
				clazz.setGradeId(grade.getId());
				clazz.setSection(grade.getSection());
				clazz.setBuildDate(new Date());
				clazz.setArtScienceType(0);//由于必填字段 默认1
				clazz.setModifyTime(new Date());
				//沿用班级 也就是沿用id
				if(clazzMap.containsKey(g.getClassId())){
					store=new GkClassStore();
					store.setId(UuidUtils.generateUuid());
					store.setTeachPlacePlanId(plan.getId());
					store.toCopy(clazzMap.get(g.getClassId()));
					gkClassStoreList.add(store);
					clazz.setModifyTime(new Date());
					clazz.setCreationTime(clazzMap.get(g.getClassId()).getCreationTime());
					clazz.setId(g.getClassId());
					allClazzList.add(clazz);
					//updateClazzList.add(clazz);
					delClazzIds.remove(g.getClassId());
				}else{
					clazz.setId(UuidUtils.generateUuid());
					clazz.setCreationTime(new Date());
					allClazzList.add(clazz);
					//insertClazzList.add(clazz);
				}
				
				groupClass=copyGroupClass(g);
				groupClass.setClassId(clazz.getId());
				groupClass.setRoundsId(plan.getId());
				insertGroupClassList.add(groupClass);
				if(!clazz.getId().equals(g.getClassId())){
					g.setClassId(clazz.getId());
					updateGroupClassList.add(g);
				}
			
				
				oldTonewGroupClass.put(g.getId(), groupClass);
				
				//修改学生班级信息
				for(String sId:g.getStuIdList()){
					
					groupClassStu=new GkGroupClassStu();
					groupClassStu.setId(UuidUtils.generateUuid());
					groupClassStu.setStudentId(sId);
					groupClassStu.setGroupClassId(groupClass.getId());
					insertGroupClassStuList.add(groupClassStu);
					
					if(stuMap.containsKey(sId)){
						//备份
						student = stuMap.get(sId);
						gkStuConversion=new GkStuConversion();
						gkStuConversion.setCreationTime(nowDate);
						gkStuConversion.setId(UuidUtils.generateUuid());
						gkStuConversion.setModifyTime(nowDate);
						gkStuConversion.setOldClassId(student.getClassId());
						gkStuConversion.setStudentId(student.getId());
						gkStuConversion.setToClassId(clazz.getId());
						gkStuConversion.setRoundsId(plan.getId());
						gkStuConversionList.add(gkStuConversion);
						student.setClassId(clazz.getId());
						student.setModifyTime(nowDate);
						/**
						 * 暂不考虑 如果数据出错 学生位于两个班级
						 */
						updateStudentList.add(student);
						stuIds.remove(sId);

					}else{
						//学生数据不对  学生表改年级下正常班级不存在
					}
				}
				
			}
			if(stuIds.size()>0){
				int dd = stuIds.size();
				String mess="";
				for(String s:stuIds){
					student = stuMap.get(s);
					mess=mess+","+student.getStudentName();
				}
				mess=mess.substring(1);
				
				throw new RuntimeException("保存失败；还有"+dd+"个学生未分班，分别是"+mess);
			}
			
			
			//不需要的班级删除
			Clazz delClazz=null;
			if(delClazzIds.size()>0){
				for(String c:delClazzIds){
					delClazz = clazzMap.get(c);
					if(delClazz!=null){
						store=new GkClassStore();
						store.setId(UuidUtils.generateUuid());
						store.setTeachPlacePlanId(plan.getId());
						store.toCopy(delClazz);
						gkClassStoreList.add(store);
						
						delClazz.setIsDeleted(GkElectveConstants.USE_TRUE);
						delClazz.setRemark("因7选3重组班级，没有用到的行政班");
						allClazzList.add(delClazz);
						
						
					}
				}
			}
			/**
			 * 备份班级
			 * insertClazzList
				updateClazzList
				updateStudentList
				delClazzIds 删除
				更新DOTO
			 */
			
			if(CollectionUtils.isNotEmpty(allClazzList)){
				classRemoteService.saveAllEntitys(SUtils.s(allClazzList));
			}
			if(CollectionUtils.isNotEmpty(updateStudentList)){
				studentRemoteService.saveAllEntitys(SUtils.s(updateStudentList));
			}
			if(CollectionUtils.isNotEmpty(gkStuConversionList)){
				gkStuConversionService.saveAllEntitys(gkStuConversionList.toArray(new GkStuConversion[0]));
			}
			if(CollectionUtils.isNotEmpty(gkClassStoreList)){
				//备份
				gkClassStoreService.saveAll(gkClassStoreList.toArray(new GkClassStore[]{}));
			}
			//清除缓存
			RedisUtils.del(GkElectveConstants.GRADE_CLASS_LIST_KEY+arrange.getGradeId());
			
		}else{
			for(GkGroupClass g:groupList){
				//班级下人数为0 过滤
				if(CollectionUtils.isEmpty(g.getStuIdList())){
					continue;
				}
				
				groupClass=copyGroupClass(g);
				groupClass.setClassId(g.getClassId());
				groupClass.setRoundsId(plan.getId());
				insertGroupClassList.add(groupClass);
				oldTonewGroupClass.put(g.getId(), groupClass);
				
				//修改学生班级信息
				for(String sId:g.getStuIdList()){
					
					groupClassStu=new GkGroupClassStu();
					groupClassStu.setId(UuidUtils.generateUuid());
					groupClassStu.setStudentId(sId);
					groupClassStu.setGroupClassId(groupClass.getId());
					insertGroupClassStuList.add(groupClassStu);
				}
			}
			
		}
		//进入教学班正式表(不管教学班开班 还是行政班 都需要)
		List<TeachClass> insertTeachClassList = new ArrayList<TeachClass>();
		List<TeachClassStu> insertTeachClassStuList = new ArrayList<TeachClassStu>();
		List<GkBatch> insertBathList = new ArrayList<GkBatch>();
		List<GkTeachClassEx> insertExList= new ArrayList<GkTeachClassEx>();//平均分
		
		//批次复制
		List<GkBatch> bathList = gkBatchService.findByRoundsId(rounds.getId(), null);
		//轮次下教学班
		List<GkTeachClassStore> gkClassList = gkTeachClassStoreService.findByRoundsId(rounds.getId());
		Map<String, GkTeachClassEx> exScoreMap = gkTeachClassExService.findByGkRoundId(rounds.getId());
		Map<String,GkTeachClassStore> gkClassMap=new HashMap<String, GkTeachClassStore>();
		if(CollectionUtils.isNotEmpty(gkClassList)){
			 gkClassMap = EntityUtils.getMap(gkClassList, "id");
		}
		/**
		 * 一个教学班只存在于一个批次
		 */
		GkBatch newBath=null;
		GkTeachClassStore gkClass=null;
		TeachClass teachClass=null;
		List<TeachClassStu> oneList=null;
		GkTeachClassEx avgEx = null;
		for(GkBatch bath:bathList){
			if(!gkClassMap.containsKey(bath.getTeachClassId())){
				continue;
			}
			//教学班班级下人数为0 过滤
			gkClass = gkClassMap.get(bath.getTeachClassId());
			if(CollectionUtils.isEmpty(gkClass.getStuList())){
				continue;
			}
			newBath=copyBath(bath);
			newBath.setRoundsId(plan.getId());
			if(BaseConstants.ZERO_GUID.equals(bath.getGroupClassId())){
				newBath.setGroupClassId(BaseConstants.ZERO_GUID);
			}else{
				if(oldTonewGroupClass.containsKey(bath.getGroupClassId())){
					newBath.setGroupClassId(oldTonewGroupClass.get(bath.getGroupClassId()).getId());
				}else{
					//应该不存在这个分支
					newBath.setGroupClassId(BaseConstants.ZERO_GUID);
				}
			}
			
			teachClass=new TeachClass();
			oneList = new ArrayList<TeachClassStu>();
			makeTeachClass(teachClass,oneList,gkClass,plan.getAcadyear(),plan.getSemester());
			avgEx=new GkTeachClassEx();
			if(exScoreMap.containsKey(gkClass.getId())){
				avgEx.setAverageScore(exScoreMap.get(gkClass.getId()).getAverageScore());
			}else{
				avgEx.setAverageScore(0.0);
			}
        	avgEx.setTeachClassId(teachClass.getId());
        	avgEx.setRoundsId(plan.getId());
        	avgEx.setId(UuidUtils.generateUuid());
			insertExList.add(avgEx);
			
			insertTeachClassList.add(teachClass);
			insertTeachClassStuList.addAll(oneList);
			newBath.setTeachClassId(teachClass.getId());
			insertBathList.add(newBath);
		}
		/**
		 * 	insertTeachClassList
			insertTeachClassStuList
			insertBathList
			insertExList
			insertGroupClassList
			updateGroupClassList
			insertGroupClassStuList
			新增DOTO
		 */
		if(CollectionUtils.isNotEmpty(updateGroupClassList)){
			insertGroupClassList.addAll(updateGroupClassList);
		}
		if(CollectionUtils.isNotEmpty(insertGroupClassList)){
//			for(int i = 0; i < insertGroupClassList.size(); i++) {  
//				entityManager.persist(insertGroupClassList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//        	entityManager.clear();
        	gkGroupClassService.saveAll(insertGroupClassList.toArray(new GkGroupClass[0]));
		}
		if(CollectionUtils.isNotEmpty(insertGroupClassStuList)){
			for(int i = 0; i < insertGroupClassStuList.size(); i++) {  
				entityManager.persist(insertGroupClassStuList.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();  
        	entityManager.clear();
		}
		
		
		
		if(CollectionUtils.isNotEmpty(insertTeachClassList)){
			for(int i = 0; i < insertTeachClassList.size(); i++) {  
				entityManager.persist(insertTeachClassList.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();  
        	entityManager.clear();
		}
		if(CollectionUtils.isNotEmpty(insertTeachClassStuList)){
			for(int i = 0; i < insertTeachClassStuList.size(); i++) {  
				entityManager.persist(insertTeachClassStuList.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();  
        	entityManager.clear();
		}
		if(CollectionUtils.isNotEmpty(insertBathList)){
			for(int i = 0; i < insertBathList.size(); i++) {  
				entityManager.persist(insertBathList.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();  
        	entityManager.clear();
		}
		if(CollectionUtils.isNotEmpty(insertExList)){
			for(int i = 0; i < insertExList.size(); i++) {  
				entityManager.persist(insertExList.get(i));  
	            if(i % 30== 0) {  
	            	entityManager.flush();  
	            	entityManager.clear();  
	            }  
	        }
			entityManager.flush();  
        	entityManager.clear();
		}
		
		//轮次步骤改为99
		//gkRoundsService.updateStep(GkElectveConstants.STEP_99, plan.getRoundsId());
		
	}

	

	private GkGroupClass copyGroupClass(GkGroupClass g) {
		GkGroupClass groupClass=new GkGroupClass();
		groupClass.setId(UuidUtils.generateUuid());
		groupClass.setGroupType(g.getGroupType());
		groupClass.setGroupName(g.getGroupName());
		groupClass.setSubjectIds(g.getSubjectIds());
		groupClass.setBatch(g.getBatch());
		return groupClass;
	}

	@Override
	public GkTeachPlacePlan findPlanById(String planId,boolean isMakeRounds) {
		GkTeachPlacePlan plan = gkTeachPlacePlanDao.findGkTeachPlacePlanById(planId);
		if(plan!=null && isMakeRounds){
			List<GkTeachPlacePlan> list =new ArrayList<GkTeachPlacePlan>();
			list.add(plan);
			makeRounds(list);
		}
		return plan;
	}

	@Override
	public void saveItem(GkTeachPlacePlanDto dto,String type) {
		GkTeachPlacePlan plan =this.findPlanById(dto.getPlanId(),true);
        if(plan==null){
        	throw new RuntimeException("该方案已经不存在，请返回刷新后操作！");
        }
		//删除原来的教师  场地
		Set<String> ids=new HashSet<String>();
		String planId = dto.getPlanId();
		ids.add(planId);
		String[] placeIds = dto.getPlaceIds();
		List<GkSubject> gkSubjectList = dto.getGkSubjectList();
		Map<String,List<Integer>> subpunchCard=new HashMap<String,List<Integer>>();
		if(CollectionUtils.isNotEmpty(gkSubjectList)){
			Set<String> gkSubjectIds = EntityUtils.getSet(gkSubjectList, "id");
			ids.addAll(gkSubjectIds);
			subpunchCard = EntityUtils.getListMap(gkSubjectList, "subjectId", "punchCard");
		}
		gkRelationshipService.deleteByPrimaryIds(ids.toArray(new String[0]));
		List<GkRelationship> insertList=new ArrayList<GkRelationship>();
		GkRelationship ship=null;
		Set<String> placeIdSet=new HashSet<String>();
		if(placeIds!=null && placeIds.length>0){
			for(String p:placeIds){
				ship=new GkRelationship();
				ship.setId(UuidUtils.generateUuid());
				ship.setPrimaryId(planId);
				ship.setRelationshipTargetId(p);
				ship.setRelationshipType(GkElectveConstants.RELATIONSHIP_TYPE_04);
				insertList.add(ship);
				placeIdSet.add(p);
			}
		}
		Map<String,Set<String>> subjectTeacherMap=new HashMap<String, Set<String>>();
		
		if(CollectionUtils.isNotEmpty(gkSubjectList)){
			for(GkSubject gs:gkSubjectList){
				if(!subjectTeacherMap.containsKey(gs.getSubjectId())){
					subjectTeacherMap.put(gs.getSubjectId(), new HashSet<String>());
				}
				if(gs.getTeacherIds()!=null && gs.getTeacherIds().length>0){
					for(String p:gs.getTeacherIds()){
						subjectTeacherMap.get(gs.getSubjectId()).add(p);
						ship=new GkRelationship();
						ship.setId(UuidUtils.generateUuid());
						ship.setPrimaryId(gs.getId());
						ship.setRelationshipTargetId(p);
						ship.setRelationshipType(GkElectveConstants.RELATIONSHIP_TYPE_01);
						insertList.add(ship);
					}
				}
				
			}
		}
		
		if(CollectionUtils.isNotEmpty(insertList)){
			gkRelationshipService.saveAll(insertList);
		}
		if("2".equals(type)){
			//清空课表（包括教学班，行政班）
			
	    	GkSubjectArrange gkArrange = gkSubjectArrangeService.findOne(plan.getSubjectArrangeId());
			
			//key:批次 value: 周几_上午_节次
			Map<Integer, Set<String>> timeMap = getTime(plan,gkArrange.getUnitId());
			//拿到当前轮次的时间点 周几_上午_节次
			Set<String> timeSet=getValues(timeMap);
			if(timeSet.size()<=0){
				//没有设置上课时间
				throw new RuntimeException("没有设置上课时间！");
			}
			Map<String,Integer> bathTimeMap=getValuetoKey(timeMap);
			CourseScheduleDto courseScheduleDto=makeSearchDto(plan, gkArrange.getUnitId());
			List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(courseScheduleDto),new TR<List<CourseSchedule>>(){});
			deleteCourseSchedule(plan,timeSet,gkArrange,crlist);
			//科目下预排老师 subjectTeacherMap
			//判断不能排 根据批次所有班级  跟行政班与教学班排无关  场地分配 
			GkRounds round = plan.getGkRounds();
			boolean isTwo=false;
			if(round!=null && GkElectveConstants.TRUE_STR.equals(round.getOpenTwo())){
				isTwo=true;//纯2+x
			}
			if(isTwo){
				//--只负责单科  删除不需要批次的3科行政班设置
				saveTeacherPlace2(subjectTeacherMap,placeIdSet,timeMap,bathTimeMap,plan,gkArrange,crlist,subpunchCard);
			}else{
				//--只负责单科  删除不需要批次的行政班设置(没有啦)
				saveTeacherPlace(subjectTeacherMap,placeIdSet,timeMap,bathTimeMap,plan,gkArrange,crlist,subpunchCard);
			}
			
		}
	}
	
	/**
	 * 
	 * @param subjectTeacherMap
	 * @param timeMap //key:批次 value: 周几_上午_节次
	 * @param bathTimeMap //key:周几_上午_节次 value:批次
	 */
	private void saveTeacherPlace2(Map<String,Set<String>> subjectTeacherMap,Set<String> placeIdSet,
			Map<Integer,Set<String>> timeMap,Map<String,Integer> bathTimeMap,
			GkTeachPlacePlan plan,GkSubjectArrange gkArrange,List<CourseSchedule> crlist,Map<String,List<Integer>> subpunchCard){
		CourseScheduleDto dto=makeSearchDto(plan,gkArrange.getUnitId());	
		
	    //key:批次 value: teacherIds //不能排的教师
      	Map<Integer,Set<String>> noTeacher=new HashMap<Integer, Set<String>>();
        //key:批次 value: placeId //不能排的场地
      	Map<Integer,Set<String>> noPlace=new HashMap<Integer, Set<String>>();
      	
      	getNotTeacherPlace(noTeacher,noPlace,crlist,bathTimeMap,plan);
            
		Map<String,Integer> teacherClassNum=new HashMap<String, Integer>();//教师已排班级数
		Map<String,Integer> placeNum=new HashMap<String, Integer>();//场地已排班级数
		
		List<TeachClass> updateTeachList=new ArrayList<TeachClass>();
	    List<CourseSchedule> insertCourseList=new ArrayList<CourseSchedule>(); 
	    //修改场地
	    List<GkBatch> updateBathList=new ArrayList<GkBatch>();   
		
		List<GkBatch> list = gkBatchService.findByRoundsId(plan.getId(),null);

		if(CollectionUtils.isNotEmpty(list)){
			//批次对应教学班
			List<String> teachClassIds=EntityUtils.getList(list,"teachClassId");
        	List<TeachClass> teachList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
        	for(TeachClass t : teachList) {
        		List<Integer> sclist=subpunchCard.get(t.getCourseId());
        		if(CollectionUtils.isNotEmpty(sclist))
        			t.setPunchCard(sclist.get(0));
        	}
        	Map<String, TeachClass> teachMap = EntityUtils.getMap(teachList, "id");
        	TeachClass teachClass=null;
        	Set<String> teacherIdsSet=null;
        	List<CourseSchedule> cList =null;
        	String placeId=null;
        	for(GkBatch b:list){
        		int index=b.getBatch();
    			Set<String> time=timeMap.get(index);
    			if(time.size()<=0){
    				continue;
    			}
    			if(!noTeacher.containsKey(index)){
    				noTeacher.put(index, new HashSet<String>());
    			}
    			if(!noPlace.containsKey(index)){
    				noPlace.put(index, new HashSet<String>());
    			}
    			Set<String> nottt = noTeacher.get(index);//不能安排的老师
    			Set<String> notpp=noPlace.get(index);//不能安排的场地
    			if(teachMap.containsKey(b.getTeachClassId())){
    				//只自动分配单科的   分配所有
    				if(placeIdSet.size()>0 ){
    					//场地  placeIdSet  placeNum  placeId
        				if(StringUtils.isNotBlank(b.getPlaceId())){
        					if(!notpp.contains(b.getPlaceId())){
        						//沿用原来场地
        						placeId=b.getPlaceId();
        						if(!placeNum.containsKey(placeId)){
        							placeNum.put(placeId, 1);
        						}else{
        							placeNum.put(placeId, placeNum.get(placeId)+1);
        						}
        						noPlace.get(index).add(placeId);
        					}else{
        						//取得最适合老师 已排班级数最少
        						placeId=findMinId(placeNum,notpp,placeIdSet);
        						if(StringUtils.isNotBlank(placeId)){
        							if(!placeNum.containsKey(teachClass.getTeacherId())){
            							placeNum.put(placeId, 1);
            						}else{
            							placeNum.put(placeId, placeNum.get(placeId)+1);
            						}
            						b.setPlaceId(placeId);
            						updateBathList.add(b);
            						noPlace.get(index).add(placeId);
        						}
        					}
        					
        				}else{
        					placeId=findMinId(placeNum,notpp,placeIdSet);
    						if(StringUtils.isNotBlank(placeId)){
    							if(!placeNum.containsKey(placeId)){
        							placeNum.put(placeId, 1);
        						}else{
        							placeNum.put(placeId, placeNum.get(placeId)+1);
        						}
        						b.setPlaceId(placeId);
        						b.setModifyTime(new Date());
        						updateBathList.add(b);
        						noPlace.get(index).add(placeId);
    						}
        				}
    				}
    				//找到教学班
    				teachClass = teachMap.get(b.getTeachClassId());
    				teacherIdsSet = subjectTeacherMap.get(teachClass.getCourseId());
    				if(StringUtils.isNotBlank(teachClass.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(teachClass.getTeacherId()))){
    					/**------------已有老师处理---------开始--------------------**/
    					if(!nottt.contains(teachClass.getTeacherId())){
    						//沿用原来老师-----目前一般不会进来的
    						if(!teacherClassNum.containsKey(teachClass.getTeacherId())){
    							teacherClassNum.put(teachClass.getTeacherId(), 1);
    						}else{
    							teacherClassNum.put(teachClass.getTeacherId(), teacherClassNum.get(teachClass.getTeacherId())+1);
    						}
    						//组装课程表
    						cList = makeCourseSchedule(teachClass, time, dto,placeId);
    						insertCourseList.addAll(cList);
						}else{
							//原来老师不能用
							//取得最适合老师 已排班级数最少
							String teacherId=findMinId(teacherClassNum,nottt,teacherIdsSet);
        					
        					if(StringUtils.isNotBlank(teacherId)){
        						teachClass.setTeacherId(teacherId);
        						noTeacher.get(index).add(teacherId);
        						//组装课程表
        						cList = makeCourseSchedule(teachClass, time, dto,placeId);
        						insertCourseList.addAll(cList);
        						updateTeachList.add(teachClass);
        						if(!teacherClassNum.containsKey(teacherId)){
        							teacherClassNum.put(teacherId, 1);
        						}else{
        							teacherClassNum.put(teacherId, teacherClassNum.get(teacherId)+1);
        						}
        					}else{
        						teachClass.setTeacherId(BaseConstants.ZERO_GUID);
        						teachClass.setModifyTime(new Date());
        						//组装课程表
        						cList = makeCourseSchedule(teachClass, time, dto,placeId);
        						insertCourseList.addAll(cList);
        						updateTeachList.add(teachClass);
        					}
						}
    					/**---已有老师处理----------结束-------------------**/
    				}else{
    					
    					String teacherId=findMinId(teacherClassNum,nottt,teacherIdsSet);
    					
    					if(StringUtils.isNotBlank(teacherId)){
    						teachClass.setTeacherId(teacherId);
    						teachClass.setModifyTime(new Date());
    						noTeacher.get(index).add(teacherId);
    						//组装课程表
    						cList = makeCourseSchedule(teachClass, time, dto,placeId);
    						insertCourseList.addAll(cList);
    						updateTeachList.add(teachClass);
    						if(!teacherClassNum.containsKey(teacherId)){
    							teacherClassNum.put(teacherId, 1);
    						}else{
    							teacherClassNum.put(teacherId, teacherClassNum.get(teacherId)+1);
    						}
    					}else{
    						teachClass.setTeacherId(BaseConstants.ZERO_GUID);
    						teachClass.setModifyTime(new Date());
    						//组装课程表
    						cList = makeCourseSchedule(teachClass, time, dto,placeId);
    						insertCourseList.addAll(cList);
    						updateTeachList.add(teachClass);
    					}
    				}
    			}
        	}/*****for(GkBatch b:list)****/
		}/*****CollectionUtils.isNotEmpty(list)****/

		
		
		if(GkRounds.OPENT_CLASS_TYPE_1.equals(plan.getGkRounds().getOpenClassType()) ){
			//行政班
			List<GkGroupClass> gList = gkGroupClassService.findByRoundsIdType(plan.getGkRounds().getId(), GkElectveConstants.GROUP_TYPE_1);
			//3+0
			Set<String> threeClassIds=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(gList)){
				for(GkGroupClass g:gList){
					if(StringUtils.isNotBlank(g.getClassId())){
						threeClassIds.add(g.getClassId());
					}
				}
			}
			//删除班级不需要的批次3科
			Set<String> limitIds=new HashSet<String>();
			List<GkTimetableLimitArrang> limitList = gkTimetableLimitArrangService.findByArrangeIdType(gkArrange.getUnitId(), plan.getAcadyear(),plan.getSemester(),plan.getId(), GkElectveConstants.LIMIT_TYPE_4);
			if(CollectionUtils.isNotEmpty(limitList)){
				for(GkTimetableLimitArrang l:limitList){
					if(threeClassIds.contains(l.getLimitId())){
						limitIds.add(l.getId());
					}
				}
			}
			if(limitIds.size()>0){
				gkTimetableLimitArrangService.deleteBtIds(limitIds.toArray(new String[]{}));
			}
		}
		if(CollectionUtils.isNotEmpty(updateBathList)){
			gkBatchService.saveAll(updateBathList.toArray(new GkBatch[]{}));
        }
        if(CollectionUtils.isNotEmpty(updateTeachList)){
        	teachClassRemoteService.saveAll(SUtils.s(updateTeachList));     
        }
        if(CollectionUtils.isNotEmpty(insertCourseList)){
        	courseScheduleRemoteService.saveAll(SUtils.s(insertCourseList));
        }
        plan.setStep(GkElectveConstants.STEP_1);
        plan.setModifyTime(new Date());
        this.save(plan);	
	}
	
	/**
	 * 
	 * @param subjectTeacherMap
	 * @param timeMap //key:批次 value: 周几_上午_节次
	 * @param bathTimeMap //key:周几_上午_节次 value:批次
	 */
	private void saveTeacherPlace(Map<String,Set<String>> subjectTeacherMap,Set<String> placeIdSet,
			Map<Integer,Set<String>> timeMap,Map<String,Integer> bathTimeMap,
			GkTeachPlacePlan plan,GkSubjectArrange gkArrange,List<CourseSchedule> crlist,Map<String,List<Integer>> subpunchCard){
		CourseScheduleDto dto=makeSearchDto(plan,gkArrange.getUnitId());	
		
	    //List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
	    //key:批次 value: teacherIds //不能排的教师
      	Map<Integer,Set<String>> noTeacher=new HashMap<Integer, Set<String>>();
        //key:批次 value: placeId //不能排的场地
      	Map<Integer,Set<String>> noPlace=new HashMap<Integer, Set<String>>();
      	
      	getNotTeacherPlace(noTeacher,noPlace,crlist,bathTimeMap,plan);
            
		Map<String,Integer> teacherClassNum=new HashMap<String, Integer>();//教师已排班级数
		Map<String,Integer> placeNum=new HashMap<String, Integer>();//场地已排班级数
		
		List<TeachClass> updateTeachList=new ArrayList<TeachClass>();
	    List<CourseSchedule> insertCourseList=new ArrayList<CourseSchedule>(); 
	    //修改场地
	    List<GkBatch> updateBathList=new ArrayList<GkBatch>();   
		
		List<GkBatch> list = gkBatchService.findByRoundsId(plan.getId(),null);

		if(CollectionUtils.isNotEmpty(list)){
			//批次对应教学班
			List<String> teachClassIds=EntityUtils.getList(list,"teachClassId");
        	List<TeachClass> teachList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClass>>(){});
        	for(TeachClass t : teachList) {
        		List<Integer> sclist=subpunchCard.get(t.getCourseId());
        		if(CollectionUtils.isNotEmpty(sclist))
        			t.setPunchCard(sclist.get(0));
        	}
        	Map<String, TeachClass> teachMap = EntityUtils.getMap(teachList, "id");
        	TeachClass teachClass=null;
        	Set<String> teacherIdsSet=null;
        	List<CourseSchedule> cList =null;
        	String placeId=null;
        	for(GkBatch b:list){
        		int index=b.getBatch();
    			Set<String> time=timeMap.get(index);
    			if(time.size()<=0){
    				continue;
    			}
    			if(!noTeacher.containsKey(index)){
    				noTeacher.put(index, new HashSet<String>());
    			}
    			if(!noPlace.containsKey(index)){
    				noPlace.put(index, new HashSet<String>());
    			}
    			Set<String> nottt = noTeacher.get(index);//不能安排的老师
    			Set<String> notpp=noPlace.get(index);//不能安排的场地
    			if(teachMap.containsKey(b.getTeachClassId())){
    				//只自动分配单科的   分配所有
    				if(placeIdSet.size()>0 ){
    					//场地  placeIdSet  placeNum  placeId
        				if(StringUtils.isNotBlank(b.getPlaceId())){
        					if(!notpp.contains(b.getPlaceId())){
        						//沿用原来场地
        						placeId=b.getPlaceId();
        						if(!placeNum.containsKey(placeId)){
        							placeNum.put(placeId, 1);
        						}else{
        							placeNum.put(placeId, placeNum.get(placeId)+1);
        						}
        						noPlace.get(index).add(placeId);
        					}else{
        						//取得最适合老师 已排班级数最少
        						placeId=findMinId(placeNum,notpp,placeIdSet);
        						if(StringUtils.isNotBlank(placeId)){
        							if(!placeNum.containsKey(teachClass.getTeacherId())){
            							placeNum.put(placeId, 1);
            						}else{
            							placeNum.put(placeId, placeNum.get(placeId)+1);
            						}
            						b.setPlaceId(placeId);
            						updateBathList.add(b);
            						noPlace.get(index).add(placeId);
        						}
        					}
        					
        				}else{
        					placeId=findMinId(placeNum,notpp,placeIdSet);
    						if(StringUtils.isNotBlank(placeId)){
    							if(!placeNum.containsKey(placeId)){
        							placeNum.put(placeId, 1);
        						}else{
        							placeNum.put(placeId, placeNum.get(placeId)+1);
        						}
        						b.setPlaceId(placeId);
        						b.setModifyTime(new Date());
        						updateBathList.add(b);
        						noPlace.get(index).add(placeId);
    						}
        				}
    				}
    				//找到教学班
    				teachClass = teachMap.get(b.getTeachClassId());
    				teacherIdsSet = subjectTeacherMap.get(teachClass.getCourseId());
    				if(StringUtils.isNotBlank(teachClass.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(teachClass.getTeacherId()))){
    					/**------------已有老师处理---------开始--------------------**/
    					if(!nottt.contains(teachClass.getTeacherId())){
    						//沿用原来老师-----目前一般不会进来的
    						if(!teacherClassNum.containsKey(teachClass.getTeacherId())){
    							teacherClassNum.put(teachClass.getTeacherId(), 1);
    						}else{
    							teacherClassNum.put(teachClass.getTeacherId(), teacherClassNum.get(teachClass.getTeacherId())+1);
    						}
    						//组装课程表
    						cList = makeCourseSchedule(teachClass, time, dto,placeId);
    						insertCourseList.addAll(cList);
						}else{
							//原来老师不能用
							//取得最适合老师 已排班级数最少
							String teacherId=findMinId(teacherClassNum,nottt,teacherIdsSet);
        					
        					if(StringUtils.isNotBlank(teacherId)){
        						teachClass.setTeacherId(teacherId);
        						noTeacher.get(index).add(teacherId);
        						//组装课程表
        						cList = makeCourseSchedule(teachClass, time, dto,placeId);
        						insertCourseList.addAll(cList);
        						updateTeachList.add(teachClass);
        						if(!teacherClassNum.containsKey(teacherId)){
        							teacherClassNum.put(teacherId, 1);
        						}else{
        							teacherClassNum.put(teacherId, teacherClassNum.get(teacherId)+1);
        						}
        					}else{
        						teachClass.setTeacherId(BaseConstants.ZERO_GUID);
        						teachClass.setModifyTime(new Date());
        						//组装课程表
        						cList = makeCourseSchedule(teachClass, time, dto,placeId);
        						insertCourseList.addAll(cList);
        						updateTeachList.add(teachClass);
        					}
						}
    					/**---已有老师处理----------结束-------------------**/
    				}else{
    					
    					String teacherId=findMinId(teacherClassNum,nottt,teacherIdsSet);
    					
    					if(StringUtils.isNotBlank(teacherId)){
    						teachClass.setTeacherId(teacherId);
    						teachClass.setModifyTime(new Date());
    						noTeacher.get(index).add(teacherId);
    						//组装课程表
    						cList = makeCourseSchedule(teachClass, time, dto,placeId);
    						insertCourseList.addAll(cList);
    						updateTeachList.add(teachClass);
    						if(!teacherClassNum.containsKey(teacherId)){
    							teacherClassNum.put(teacherId, 1);
    						}else{
    							teacherClassNum.put(teacherId, teacherClassNum.get(teacherId)+1);
    						}
    					}else{
    						teachClass.setTeacherId(BaseConstants.ZERO_GUID);
    						teachClass.setModifyTime(new Date());
    						//组装课程表
    						cList = makeCourseSchedule(teachClass, time, dto,placeId);
    						insertCourseList.addAll(cList);
    						updateTeachList.add(teachClass);
    					}
    				}
    			}
        	}/*****for(GkBatch b:list)****/
		}/*****CollectionUtils.isNotEmpty(list)****/

		
		
//		if(GkRounds.OPENT_CLASS_TYPE_1.equals(plan.getGkRounds().getOpenClassType()) ){
//			//行政班
//			Map<String,Set<Integer>> classBath=new HashMap<String, Set<Integer>>();
//			List<GkGroupClass> gList = gkGroupClassService.findByRoundsId(plan.getId());
//			//3+0
//			Set<String> threeClassIds=new HashSet<String>();
//			if(CollectionUtils.isNotEmpty(gList)){
//				for(GkGroupClass g:gList){
//					if(StringUtils.isNotBlank(g.getClassId())){
//						if(GkElectveConstants.GROUP_TYPE_1.equals(g.getGroupType())){
//							threeClassIds.add(g.getClassId());
//							continue;
//						}
//						if(StringUtils.isNotBlank(g.getBatch())){
//							classBath.put(g.getClassId(), new HashSet<Integer>());
//							String setStr = g.getBatch();
//							String[] arr = setStr.split(",");
//							for(String s:arr){
//								classBath.get(g.getClassId()).add(Integer.parseInt(s));
//							}
//						}
//					}
//				}
//			}
//		
//			
//			if(classBath.size()>0){
//				//删除班级不需要的批次
//				Set<String> limitIds=new HashSet<String>();
//				List<GkTimetableLimitArrang> limitList = gkTimetableLimitArrangService.findByArrangeIdType(plan.getAcadyear(), plan.getSemester(),plan.getId(),GkElectveConstants.LIMIT_TYPE_4);
//				if(CollectionUtils.isNotEmpty(limitList)){
//					for(GkTimetableLimitArrang l:limitList){
//						if(threeClassIds.contains(l.getLimitId())){
//							limitIds.add(l.getId());
//							continue;
//						}
//						if(!classBath.containsKey(l.getLimitId())){
//							continue;
//						}
//						Set<Integer> ss = classBath.get(l.getLimitId());
//						if(ss.size()<=0){
//							continue;
//						}
//						for(Integer ii:ss){
//							Set<String> time=timeMap.get(ii);
//			    			if(time.size()<=0){
//			    				continue;
//			    			}
//			    			String str = l.getWeekday()+"_"+l.getPeriodInterval()+"_"+l.getPeriod();
//			    			
//		            		if(time.contains(str)){
//		            			limitIds.add(l.getId());
//		            		}
//			    			
//						}
//						
//						
//					}
//				}
//				if(limitIds.size()>0){
//					gkTimetableLimitArrangService.deleteBtIds(limitIds.toArray(new String[]{}));
//				}
//			}
//			
//		}
		
		
		
		
		
		
		
		
		
		
		if(CollectionUtils.isNotEmpty(updateBathList)){
//        	for(int i = 0; i < updateBathList.size(); i++) {  
//				entityManager.merge(updateBathList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//        	entityManager.clear();     
			gkBatchService.saveAll(updateBathList.toArray(new GkBatch[]{}));
        }
		
		
		
        if(CollectionUtils.isNotEmpty(updateTeachList)){
//        	for(int i = 0; i < updateTeachList.size(); i++) {  
//				entityManager.merge(updateTeachList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//        	entityManager.clear();  
        	teachClassRemoteService.saveAll(SUtils.s(updateTeachList));     
        }
        if(CollectionUtils.isNotEmpty(insertCourseList)){
//        	for(int i = 0; i < insertCourseList.size(); i++) {  
//				entityManager.persist(insertCourseList.get(i));  
//	            if(i % 30== 0) {  
//	            	entityManager.flush();  
//	            	entityManager.clear();  
//	            }  
//	        }
//			entityManager.flush();  
//        	entityManager.clear();  
        	courseScheduleRemoteService.saveAll(SUtils.s(insertCourseList));
        }
        plan.setStep(GkElectveConstants.STEP_1);
        plan.setModifyTime(new Date());
        this.save(plan);	
	}
	
//	 private Set<Integer> toRemove(Set<Integer> allBath, Set<Integer> itemSet) {
//		 Set<Integer> returnSet=new HashSet<Integer>();
//		 for(Integer i:allBath){
//			 if(!itemSet.contains(i)){
//				 returnSet.add(i);
//			 }
//		 }
//		return returnSet;
//	}

	/**
     * key:批次 value: 周几_上午_节次
     */
    private Map<Integer,Set<String>> getTime(GkTeachPlacePlan plan,String unitId){
    	List<GkTimetableLimitArrang> limtIds = gkTimetableLimitArrangService.findGkTimetableLimitArrangList(unitId, plan.getAcadyear(), plan.getSemester(), plan.getId(), GkElectveConstants.LIMIT_TYPE_6);
    	Map<Integer,Set<String>> returnMap=new HashMap<Integer, Set<String>>();
    	if(CollectionUtils.isNotEmpty(limtIds)){
    		for(GkTimetableLimitArrang l:limtIds){
    			int key = Integer.parseInt(l.getArrangId());
    			String str=l.getWeekday()+"_"+l.getPeriodInterval()+"_"+l.getPeriod();
    			if(!returnMap.containsKey(key)){
    				returnMap.put(key, new HashSet<String>());
    			}
    			returnMap.get(key).add(str);
    		}
    	}
    	return returnMap;
    }
    /**
     * 清除当前时间段内所有课程表（包括教学班（7选3））---不是7选3的教学班先遗留
     * 删除需要的批次上课时间点 (行政班(所有行政班))---先不根据行政班是否需要设置该批次
     * @param plan
     */
    private void deleteCourseSchedule(GkTeachPlacePlan plan,Set<String> timeSet,GkSubjectArrange gkArrange,List<CourseSchedule> crlist){
//    	List<GkTeachPlacePlan> planList = gkTeachPlacePlanDao.findBySubjectArrangeIdHasDelete(plan.getSubjectArrangeId());
//		List<String> planIds=new ArrayList<String>();
		List<String> teachClassIds=new ArrayList<String>();
//		if(CollectionUtils.isNotEmpty(planList)){
//			planIds = EntityUtils.getList(planList, "id");
//			List<GkBatch> list = gkBatchService.findByRoundsId(planIds.toArray(new String[]{}));
//			if(CollectionUtils.isNotEmpty(list)){
//				teachClassIds = EntityUtils.getList(list, "teachClassId");
//			}
//		}
		//清除数据库中 根据年级id取得teachClassIds---之前还有一批前一版本 轮次进来的数据
		List<TeachClass> tList = SUtils.dt(teachClassRemoteService.findTeachClassListByGradeId(
				gkArrange.getUnitId(), plan.getAcadyear(), 
				plan.getSemester(), null, gkArrange.getGradeId()),new TR<List<TeachClass>>(){});
		Map<String,TeachClass> teachMap=new HashMap<String,TeachClass>();
		if(CollectionUtils.isNotEmpty(tList)){
			teachClassIds = EntityUtils.getList(tList, "id");
			teachMap=EntityUtils.getMap(tList, "id");
		}
		
		Set<String> delIds=new HashSet<String>();
		String str=null;
		//CourseScheduleDto dto=makeSearchDto(plan, gkArrange.getUnitId());
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>(){});
        Set<String> clazzIds=new HashSet<String>();
        if(CollectionUtils.isNotEmpty(clazzList)){
        	clazzIds=EntityUtils.getSet(clazzList,"id");
        }
        //当前时间内课程表（包括前与后）
        //List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
        if(CollectionUtils.isNotEmpty(crlist)){
        	for(CourseSchedule c:crlist){
        		//删除当前年级 各轮次的产生的教学班课程---遗留非7选3产生的教学班
        		if(teachClassIds.contains(c.getClassId())){
        			if(!TeachClass.CLASS_TYPE_SEVEN.equals(teachMap.get(c.getClassId()).getClassType())){
        				//排除掉非7选3
        				continue;
        			}
        			//开始
        			if(c.getWeekOfWorktime()==plan.getWeekOfWorktime()){
    					if(c.getDayOfWeek()<plan.getDayOfWeek()){
    						continue;
    					}
    				}else if(c.getWeekOfWorktime()==plan.getWeekOfWorktime2()){
    					//结尾	
    					if(c.getDayOfWeek()>plan.getDayOfWeek2()){
    						continue;
    					}
    				}
        			delIds.add(c.getId());
        			continue;
        		}
        		//删除需要的批次上课时间点 行政班数据
        		if(timeSet.size() > 0 && clazzIds.contains(c.getClassId())){
        			str = c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
            		if(timeSet.contains(str)){
            			delIds.add(c.getId());
            		}
        		}
        	}
        }
		
        if(delIds.size()>0){
    		courseScheduleRemoteService.deleteCourseByIds(delIds.toArray(new String[0]));
    	}
    }
    
    /**
     * 拿到value
     * @param map
     * @return
     */
    private Set<String> getValues(Map<Integer,Set<String>> map){
    	Set<String> returnSet=new HashSet<String>();
    	if(map.size()<=0){
    		return returnSet;
    	}
    	for(Integer key:map.keySet()){
    		returnSet.addAll(map.get(key));
    	}
    	return returnSet;
    }
    
    /**
     * 查询数量最少的id
     * @param numMap id已排数量
     * @param notId 过滤id
     * @param idSet 所有id
     * @return
     */
	private String findMinId(Map<String, Integer> numMap,
			Set<String> notId, Set<String> idSet) {
		String id=null;
		int claNum=0;
		if(CollectionUtils.isNotEmpty(idSet)){
			for(String t:idSet){
				if(!notId.contains(t)){
					if(StringUtils.isNotBlank(id)){
						int temp=0;
						if(numMap.containsKey(t)){
							temp=numMap.get(t);
						}
						if(temp==0){
							id=t;
							claNum=temp;
							break;
						}else{
							if(temp<claNum){
								id=t;
								claNum=temp;
							}
						}
					}else{
						id=t;
						if(numMap.containsKey(t)){
							claNum=numMap.get(t);
						}else{
							claNum=0;
							break;
						}
					}
				}
			}
			
		}
		return id;
	}
	
	/**
	 * 
	 * @param teachClass
	 * @param time 周几_上午_节次
	 * @param dto 开始时间 与结束时间
	 * @return
	 */
	private List<CourseSchedule> makeCourseSchedule(TeachClass teachClass,Set<String> time,CourseScheduleDto dto,String placeId){
		List<CourseSchedule> returnList=new ArrayList<CourseSchedule>();
		for(String t:time){
			String[] arr = t.split("_");
			int dayOfWeek=Integer.parseInt(arr[0]);
			String periodInterval=arr[1];
			int period=Integer.parseInt(arr[2]);
			int start=dto.getWeekOfWorktime1();
			int end=dto.getWeekOfWorktime2();
			int dayStart=dto.getDayOfWeek1();
			int dayEnd=dto.getDayOfWeek2();
			for (int index=start;index<=end;index++){
				CourseSchedule c = makeNewCourseSchedule(teachClass);
				if(index==start){
					if(dayOfWeek<dayStart){
						continue;
					}
				}
				if(index==end){
					if(dayOfWeek>dayEnd){
						continue;
					}
				}
				c.setWeekOfWorktime(index);
				c.setPeriod(period);
				c.setPeriodInterval(periodInterval);
				c.setDayOfWeek(dayOfWeek);
				if(StringUtils.isNotBlank(placeId)){
					c.setPlaceId(placeId);
				}
				returnList.add(c);
			}
		}
		return returnList;
		
	}
	private CourseSchedule makeNewCourseSchedule(TeachClass t){
		CourseSchedule c=new CourseSchedule();
		c.setId(UuidUtils.generateUuid());
		c.setAcadyear(t.getAcadyear());
		c.setSemester(Integer.parseInt(t.getSemester()));
		c.setClassId(t.getId());
		c.setClassType(CourseSchedule.CLASS_TYPE_SEVEN);
		c.setSubjectId(t.getCourseId());
		c.setSchoolId(t.getUnitId());
		c.setTeacherId(t.getTeacherId());
		c.setPunchCard(t.getPunchCard());
		return c;
	}
	private CourseScheduleDto makeSearchDto(GkTeachPlacePlan plan,String unitId){
		CourseScheduleDto dto=new CourseScheduleDto();
        dto.setAcadyear(plan.getAcadyear());
        dto.setSchoolId(unitId);
        dto.setSemester(Integer.parseInt(plan.getSemester()));
        dto.setDayOfWeek1(plan.getDayOfWeek());
        dto.setWeekOfWorktime1(plan.getWeekOfWorktime());
        dto.setDayOfWeek2(plan.getDayOfWeek2());
        dto.setWeekOfWorktime2(plan.getWeekOfWorktime2());
        return dto;
	}
	private void getNotTeacherPlace(Map<Integer,Set<String>> noTeacher,
			Map<Integer,Set<String>> noPlace,List<CourseSchedule> crlist,Map<String,Integer> bathTimeMap,GkTeachPlacePlan plan){
		String str=null;
		if(CollectionUtils.isNotEmpty(crlist)){
			for(CourseSchedule c:crlist){
				//开始
    			if(c.getWeekOfWorktime()==plan.getWeekOfWorktime()){
					if(c.getDayOfWeek()<plan.getDayOfWeek()){
						continue;
					}
				}else if(c.getWeekOfWorktime()==plan.getWeekOfWorktime2()){
					//结尾	
					if(c.getDayOfWeek()>plan.getDayOfWeek2()){
						continue;
					}
				}
        		str = c.getDayOfWeek()+"_"+c.getPeriodInterval()+"_"+c.getPeriod();
        		if(bathTimeMap.containsKey(str)){
        			Integer bath = bathTimeMap.get(str);
        			if(StringUtils.isNotBlank(c.getTeacherId())){
        				if(!noTeacher.containsKey(bath)){
        					noTeacher.put(bath, new HashSet<String>());
        				}
        				noTeacher.get(bath).add(c.getTeacherId());
        			}
        			if(StringUtils.isNotBlank(c.getPlaceId())){
        				if(!noPlace.containsKey(bath)){
        					noPlace.put(bath, new HashSet<String>());
        				}
        				noPlace.get(bath).add(c.getPlaceId());
        			}
        		}
        	
        	}
		}
	}
	
	private Map<String, Integer> getValuetoKey(Map<Integer, Set<String>> map) {
		Map<String, Integer> returnMap=new HashMap<String, Integer>();
		if(map.size()<=0){
			return returnMap;
		}
		for(Integer key:map.keySet()){
			Set<String> set = map.get(key);
			if(set.size()>0){
				for(String s:set){
					returnMap.put(s, key);
				}
			}
		}
		return returnMap;
	}

	@Override
	public void saveTeaAndCourseSch(List<TeachClass> clsList, String planId,
			String unitId) {
		teachClassRemoteService.saveAll(SUtils.s(clsList));
		//修改课表
		Set<String> teaClsIds = EntityUtils.getSet(clsList, "id");
		Map<String,String> teaClsTeaMap = EntityUtils.getMap(clsList, "id","teacherId");
		GkTeachPlacePlan plan = this.findOne(planId);
		CourseScheduleDto dto=new CourseScheduleDto();
        dto.setAcadyear(plan.getAcadyear());
        dto.setSchoolId(unitId);
        dto.setSemester(Integer.parseInt(plan.getSemester()));
        dto.setDayOfWeek1(plan.getDayOfWeek());
        dto.setWeekOfWorktime1(plan.getWeekOfWorktime());
        dto.setDayOfWeek2(plan.getDayOfWeek2());
        dto.setWeekOfWorktime2(plan.getWeekOfWorktime2());
        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
        List<CourseSchedule> updatelist = new ArrayList<CourseSchedule>();
        for(CourseSchedule c : crlist){
        	if(teaClsIds.contains(c.getClassId())){
        		if(!StringUtils.equals(teaClsTeaMap.get(c.getClassId()), c.getTeacherId())){
        			c.setTeacherId(teaClsTeaMap.get(c.getClassId()));
        			updatelist.add(c);
        		}
        	}
		}
        if(CollectionUtils.isNotEmpty(updatelist)){
        	courseScheduleRemoteService.saveAll(SUtils.s(updatelist));
        }
		
	}

	@Override
	public void saveAllAndCourseSchedule(List<GkBatch> gkBatchList, String planId,
			String unitId) {
		gkBatchService.saveAll(gkBatchList.toArray(new GkBatch[0]));
		//修改课表
		Set<String> teaClsIds = EntityUtils.getSet(gkBatchList, "teachClassId");
		Map<String,String> teaClsPlaceMap = EntityUtils.getMap(gkBatchList, "teachClassId","placeId");
		GkTeachPlacePlan plan = this.findOne(planId);
		CourseScheduleDto dto=new CourseScheduleDto();
        dto.setAcadyear(plan.getAcadyear());
        dto.setSchoolId(unitId);
        dto.setSemester(Integer.parseInt(plan.getSemester()));
        dto.setDayOfWeek1(plan.getDayOfWeek());
        dto.setWeekOfWorktime1(plan.getWeekOfWorktime());
        dto.setDayOfWeek2(plan.getDayOfWeek2());
        dto.setWeekOfWorktime2(plan.getWeekOfWorktime2());
        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
        List<CourseSchedule> updatelist = new ArrayList<CourseSchedule>();
        for(CourseSchedule c : crlist){
        	if(teaClsIds.contains(c.getClassId())){
        		if(!StringUtils.equals(teaClsPlaceMap.get(c.getClassId()), c.getPlaceId())){
        			c.setPlaceId(teaClsPlaceMap.get(c.getClassId()));
        			updatelist.add(c);
        		}
        	}
		}
        if(CollectionUtils.isNotEmpty(updatelist)){
        	courseScheduleRemoteService.saveAll(SUtils.s(updatelist));
        }
		
	}

	@Override
	public List<GkTeachPlacePlan> findByRoundId(String roundsId) {
		return gkTeachPlacePlanDao.findByRoundId(roundsId);
	}

	@Override
	public void deleteByPlanId(String planId) {
		GkTeachPlacePlan plan = this.findPlanById(planId, true);
		
		Set<String> delBatchIds=new HashSet<String>();//需要删除的批次id
		Set<String> teachClassIds=new HashSet<String>();//需要删除的教学班id
		Set<String> scheduleIds=new HashSet<String>();//需要删除的课程表id
		Set<String> groupClassIds=new HashSet<String>();//需要删除的组合id
		Set<String> scoreIds=new HashSet<String>();//平均分id
		
		List<GkBatch> findBatchList = gkBatchService.findBatchList(planId, null);
		
		GkSubjectArrange gkArrange = gkSubjectArrangeService.findOne(plan.getSubjectArrangeId());
		
		CourseScheduleDto dto=makeSearchDto(plan, gkArrange.getUnitId());
        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
        //平均分
        List<GkTeachClassEx> scoreList = gkTeachClassExService.findGkTeachClassExList(planId,null);
        if(CollectionUtils.isNotEmpty(scoreList)){
        	scoreIds=EntityUtils.getSet(scoreList, "id");
        }
        boolean flag=false;
        if(GkRounds.OPENT_CLASS_TYPE_1.equals(plan.getGkRounds().getOpenClassType())){
        	flag=true;
        }
       
		if(CollectionUtils.isNotEmpty(findBatchList)){
			delBatchIds=EntityUtils.getSet(findBatchList, "id");
			teachClassIds=EntityUtils.getSet(findBatchList, "teachClassId");
			
			//---------根据teachClassIds 找到课程表开始
			if(CollectionUtils.isNotEmpty(crlist)){
				for(CourseSchedule c:crlist){
					if(teachClassIds.contains(c.getClassId())){
						scheduleIds.add(c.getId());
					}
				}
			}
			//-------根据teachClassIds 找到课程表结束
			
			groupClassIds=EntityUtils.getSet(findBatchList, "groupClassId");
			if(groupClassIds.size()>0){
				if(flag){
					//行政班
					//修改学生表  从备注信息还原  修改班级表  教学计划 还原
					/**
					 * DOTO
					 */
					
				}
			}	
		}
		
		if(delBatchIds.size()>0){
			gkBatchService.deleteAll(delBatchIds.toArray(new String[0]));
		}
		if(teachClassIds.size()>0){
			teachClassRemoteService.deleteByIds(teachClassIds.toArray(new String[0]));
		}
		if(scheduleIds.size()>0){
			courseScheduleRemoteService.deleteCourseByIds(scheduleIds.toArray(new String[0]));
		}
		if(groupClassIds.size()>0){
			gkGroupClassService.deleteById(groupClassIds.toArray(new String[0]));
		}
		if(scoreIds.size()>0){
			gkTeachClassExService.deleteByIds(scoreIds.toArray(new String[0]));
		}
		
		//删除开班设置 以及老师设置
        gkSubjectService.deleteByRoundsId(planId);
		//删除场地设置
        gkRelationshipService.deleteByPrimaryId(planId,GkElectveConstants.RELATIONSHIP_TYPE_04);
        //删除限制
        gkTimetableLimitArrangService.deleteByRoundsId(gkArrange.getUnitId(), plan.getAcadyear(), plan.getSemester(), plan.getId());
    	
        //方案软删
        plan.setModifyTime(new Date());
        plan.setIsDeleted(GkElectveConstants.USE_TRUE);
        this.save(plan);
	}

}
