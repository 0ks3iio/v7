package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkRoundsDao;
import net.zdsoft.gkelective.data.dao.GkStuConversionDao;
import net.zdsoft.gkelective.data.dao.GkSubjectArrangeDao;
import net.zdsoft.gkelective.data.dto.GkRoundsDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkConditionService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkGroupClassStuService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("gkRoundsService")
public class GkRoundsServiceImpl  extends BaseServiceImpl<GkRounds, String> implements GkRoundsService{

	@Autowired
	private GkRoundsDao gkRoundsDao;
	@Autowired
	private GkSubjectArrangeDao gkSubjectArrangeDao;
	@Autowired
    private GkSubjectService gkSubjectService;
    @Autowired
    private GkBatchService gkBatchService;
    @Autowired
    private GkConditionService gkConditionService;
    @Autowired
	private GkTimetableLimitArrangService gkTimetableLimitArrangService;
    @Autowired
    private GkStuConversionDao gkStuConversionDao;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private GkGroupClassService gkGroupClassService;
    @Autowired
    private GkGroupClassStuService gkGroupClassStuService;
    @Autowired
    private CourseScheduleRemoteService courseScheduleRemoteService;
    @Autowired
    private GkTeachClassStoreService gkTeachClassStoreService;
	
	@Override
	protected BaseJpaRepositoryDao<GkRounds, String> getJpaDao() {
		return gkRoundsDao;
	}

	@Override
	protected Class<GkRounds> getEntityClass() {
		return GkRounds.class;
	}

	@Override
	public GkRounds findRoundById(String roundsId) {
		return gkRoundsDao.findRoundById(roundsId);
	}

	@Override
	public List<GkRounds> findBySubjectArrangeId(String arrangeId,String openClassType) {
		if(StringUtils.isNotBlank(openClassType)){
			return gkRoundsDao.findByArrangeIdClassType(arrangeId,openClassType);
		}
		return gkRoundsDao.findBySubjectArrangeId(arrangeId);
	}

	@Override
	public GkRounds getCurrentGkRounds(String arrangeId) {
		return gkRoundsDao.getCurrentGkRounds(arrangeId);
	}
	
	@Override
	public void saveRounds(GkRounds gkRounds) {
//		List<GkRounds> roundsList = gkRoundsDao.findBySubjectArrangeId(gkRounds.getSubjectArrangeId());
//		Set<String> roundsids=new HashSet<String>();
//		Map<String,GkRounds> roundsMap=new HashMap<String, GkRounds>();
//		GkRounds currGkRounds=null;
//		
//		if(CollectionUtils.isNotEmpty(roundsList)){
//			for(GkRounds g:roundsList){
//				roundsMap.put(g.getId(), g);
//				roundsids.add(g.getId());
//				if(currGkRounds==null){
//					currGkRounds=g;
//				}else{
//					if(g.getOrderId()>currGkRounds.getOrderId()){
//						currGkRounds=g;
//					}
//				}
//			}
//		}
		if(StringUtils.isNotBlank(gkRounds.getCopyRoundIds()) && GkRounds.OPENT_CLASS_TYPE_1.equals(gkRounds.getOpenClassType())){
			//复制前一次的手动排班数据
			List<GkGroupClass> gk = gkGroupClassService.findByRoundsId(gkRounds.getCopyRoundIds());
			if(CollectionUtils.isNotEmpty(gk)){
				 Set<String> ids = EntityUtils.getSet(gk, "id");
				 Map<String, List<String>> map = gkGroupClassStuService.findByGroupClassIdIn(ids.toArray(new String[0]));
				 List<GkGroupClassStu> gkGroupClassStuList = new ArrayList<GkGroupClassStu>();
				 List<GkGroupClass> gkList=new ArrayList<GkGroupClass>();
				 GkGroupClass gkGroupClass=null;
				 GkGroupClassStu gkGroupClassStu=null;
				 for(GkGroupClass g:gk){
					 if(map.containsKey(g.getId()) && CollectionUtils.isNotEmpty(map.get(g.getId()))){
						 gkGroupClass=new GkGroupClass();
						 gkGroupClass.setId(UuidUtils.generateUuid());
						 gkGroupClass.setRoundsId(gkRounds.getId());
						 gkGroupClass.setGroupType(g.getGroupType());
						 gkGroupClass.setSubjectIds(g.getSubjectIds());
						 gkGroupClass.setGroupName(g.getGroupName());
						 gkGroupClass.setClassId(g.getClassId());
						 gkList.add(gkGroupClass);
						 for(String s:map.get(g.getId())){
							 gkGroupClassStu=new GkGroupClassStu();
							 gkGroupClassStu.setId(UuidUtils.generateUuid());
							 gkGroupClassStu.setGroupClassId(gkGroupClass.getId());
							 gkGroupClassStu.setStudentId(s);
							 gkGroupClassStuList.add(gkGroupClassStu);
						 }
					 }
				 }
				 
				if(CollectionUtils.isNotEmpty(gkList)){
					gkGroupClassService.saveAll(gkList.toArray(new GkGroupClass[0]));
				}
				if(CollectionUtils.isNotEmpty(gkGroupClassStuList)){
					gkGroupClassStuService.saveAll(gkGroupClassStuList.toArray(new GkGroupClassStu[0]));
				}
			}
		}
		//Set<String> roundsids = EntityUtils.getSet(roundsList, "id");
		//Map<String,GkRounds> roundsMap = EntityUtils.getMap(roundsList, "id");
//		for(String roundsId : roundsids){
//			List<GkBatch> gkB = gkBatchService.findByRoundsId(roundsId, null);
//			Set<String> teaClsIds =EntityUtils.getSet(gkB, "teachClassId"); 
//			if(CollectionUtils.isNotEmpty(teaClsIds)){
//				teachClassRemoteService.notUsing(teaClsIds.toArray(new String[0]));
//			}
//			gkTimetableLimitArrangService.notUsing(roundsMap.get(roundsId).getAcadyear(),roundsMap.get(roundsId).getSemester(), roundsId);
//		}
//		GkSubjectArrange g = gkSubjectArrangeDao.findById(gkRounds.getSubjectArrangeId());
//		if(g!=null){
//			g.setIsLock(GkElectveConstants.USE_FALSE);
//			gkSubjectArrangeDao.save(g);
//		}
		//清除当前时间内的其他轮次教学班数据
		//clearCourseSchedule(gkRounds,gkRounds.getSubjectArrangeId());
		
		save(gkRounds);
	}
	//清除当前时间内的其他轮次教学班数据
    private void clearCourseSchedule(GkRounds round,String arrangeId){
    	//拿到当前时间段内所有课程表
//		List<GkRounds> rounds = this.findBySubjectArrangeIdHasDelete(arrangeId);
//		List<String> roundsIds=new ArrayList<String>();
//		List<String> teachClassIds=new ArrayList<String>();
//		if(CollectionUtils.isNotEmpty(rounds)){
//			roundsIds = EntityUtils.getList(rounds, "id");
//			List<GkBatch> list = gkBatchService.findByRoundsId(roundsIds.toArray(new String[]{}));
//			if(CollectionUtils.isNotEmpty(list)){
//				teachClassIds = EntityUtils.getList(list, "teachClassId");
//			}
//		}
//		GkSubjectArrange gkArrange = gkSubjectArrangeDao.findById(arrangeId);
//		//需要删除的课程
//		Set<String> delIds=new HashSet<String>();
//		CourseScheduleDto dto=new CourseScheduleDto();
//		dto.setSchoolId(gkArrange.getUnitId());
//        dto.setAcadyear(round.getAcadyear());
//        dto.setSemester(Integer.parseInt(round.getSemester()));
//        dto.setDayOfWeek1(round.getDayOfWeek());
//        dto.setWeekOfWorktime1(round.getWeekOfWorktime());
//        dto.setDayOfWeek2(round.getDayOfWeek2());
//        dto.setWeekOfWorktime2(round.getWeekOfWorktime2());
//        //当前时间内课程表（包括前与后）
//        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
//        if(CollectionUtils.isNotEmpty(crlist)){
//        	for(CourseSchedule c:crlist){
//        		//删除当前年级 各轮次的产生的教学班课程---遗留非7选3产生的教学班
//        		if(teachClassIds.contains(c.getClassId())){
//        			//开始
//        			if(c.getWeekOfWorktime()==round.getWeekOfWorktime()){
//    					if(c.getDayOfWeek()<round.getDayOfWeek()){
//    						continue;
//    					}
//    				}else if(c.getWeekOfWorktime()==round.getWeekOfWorktime2()){
//    					//结尾	
//    					if(c.getDayOfWeek()>round.getDayOfWeek2()){
//    						continue;
//    					}
//    				}
//        			delIds.add(c.getId());
//        			continue;
//        		}
//        	}
//        	if(delIds.size()>0){
//        		courseScheduleRemoteService.deleteCourseByIds(delIds.toArray(new String[0]));
//        	}
//        }
    }
	
	@Override
	public void removeByRoundsId(String roundsId) {
		GkRounds rounds = gkRoundsDao.findRoundById(roundsId);
		//(除轮次表  全部硬删 )
		//手动排班表
		List<GkGroupClass> gkGroupList = gkGroupClassService.findByRoundsId(roundsId);
		if(CollectionUtils.isNotEmpty(gkGroupList)){
			List<String> gkGroupIds = EntityUtils.getList(gkGroupList, "id");
			gkGroupClassService.deleteById(gkGroupIds.toArray(new String[0]));
		}
		//临时教学班
		List<GkTeachClassStore> gkClassList=gkTeachClassStoreService.findByRoundsId(roundsId);
		if(CollectionUtils.isNotEmpty(gkClassList)){
			List<String> gkClassIds = EntityUtils.getList(gkClassList, "id");
			gkTeachClassStoreService.deleteByIds(gkClassIds.toArray(new String[0]));
		}
		//删除开班结果
		gkBatchService.deleteOnlyByRoundsId(roundsId);
		//删除开班条件
		gkConditionService.deleteByRoundsId(roundsId);
		//删除课程是否走班信息
		gkSubjectService.deleteByRoundsId(roundsId);
		//删除班级调班信息
		gkStuConversionDao.deleteByRoundsId(roundsId);
		gkRoundsDao.deleteById(roundsId);
		//轮次排序不能变
		//gkRoundsDao.updateOrderIdByArrangeId(rounds.getSubjectArrangeId(),rounds.getOrderId());
	}

	@Override
	public void updateStep(int step, String roundsId) {
		gkRoundsDao.updateStep(step, new Date(),roundsId);
	}

	@Override
	public List<GkRounds> findBySubjectArrangeIdHasDelete(String arrangeId) {
		return gkRoundsDao.findBySubjectArrangeIdHasDelete(arrangeId);
	}

	@Override
	public void saveDto(GkRoundsDto dto) {
		this.save(dto.getGkRounds());
        if(CollectionUtils.isNotEmpty(dto.getGksubList())){
        	List<GkSubject>  gksList = dto.getGksubList();
        	for(GkSubject gks:gksList){
        		if(StringUtils.isBlank(gks.getId())){
        			gks.setId(UuidUtils.generateUuid());
        		}
        	}
        	gkSubjectService.saveAll(gksList.toArray(new GkSubject[0]));
        }
	}

}
