package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkSubjectArrangeDao;
import net.zdsoft.gkelective.data.dao.GkTimetableLimitArrangDao;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkTimetableLimitArrangService")
public class GkTimetableLimitArrangServiceImpl extends BaseServiceImpl<GkTimetableLimitArrang, String> implements GkTimetableLimitArrangService {
	@Autowired
    private GkTimetableLimitArrangDao gkTimetableLimitArrangDao;
    @Autowired
    private GkSubjectArrangeDao gkSubjectArrangeDao;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private GkRoundsService gkRoundsService;
    @Autowired
    private CourseScheduleRemoteService courseScheduleRemoteService;
    @Autowired
    private GkTeachPlacePlanService gkTeachPlacePlanService;
    @Autowired
    private GkBatchService gkBatchService;
    
    @Override
    public void save(GkTimetableLimitArrang gkTimetableLimitArrang) {
        gkTimetableLimitArrangDao.save(gkTimetableLimitArrang);
    }

    @Override
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangList(String acadyear, String semester,
            String limitId, String limitType) {
        return gkTimetableLimitArrangDao.findGkTimetableLimitArrangList(acadyear, semester, limitId, limitType);
    }

    @Override
    public void deleteById(String id) {
        gkTimetableLimitArrangDao.deleteById(id);
    }

    @Override
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListBylimitIdIn(String acadyear, String semester,
            String limitType, String arrangId, String[] limitIds) {
        return gkTimetableLimitArrangDao.findGkTimetableLimitArrangListBylimitIdIn(acadyear, semester, limitType,
                arrangId, limitIds);
    }

    @Override
    public void batchSave(List<GkTimetableLimitArrang> gkTimetableLimitArrangList) {
        gkTimetableLimitArrangDao.saveAll(gkTimetableLimitArrangList);
    }

    @Override
    public void deleteBySelectXyz(String acadyear, String semester, String planId, String period,
            String periodInterval, String weekday) {
    	 //根据年级取得班级
    	//GkRounds rent = gkRoundsService.findRoundById(roundsId);
    	GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId,true);
		
        GkSubjectArrange gkSubjectArrange = gkSubjectArrangeDao.getOne(plan.getSubjectArrangeId());
        List<Clazz> classList = SUtils.dt(classRemoteService.findByInGradeIds(new String[]{gkSubjectArrange.getGradeId()}),new TR<List<Clazz>>(){});
        Set<String> limitIds=new HashSet<String>();
        Set<String> delid=new HashSet<String>();
        
        if(CollectionUtils.isNotEmpty(classList)){
        	for(int i=0;i<classList.size();i++){
     			limitIds.add(classList.get(i).getId());
     		}
        }
        //删除年级限制limitIds
      	limitIds.add(planId);
      	//删除班级在该节次的所有限制
 		List<GkTimetableLimitArrang> limitList = gkTimetableLimitArrangDao
 	                .findGkTimetableLimitArrangBySelectXyz(acadyear, semester,period, periodInterval, weekday,limitIds.toArray(new String[]{}));
 		if(CollectionUtils.isNotEmpty(limitList)){
 	    	for(GkTimetableLimitArrang g:limitList){
 	    		delid.add(g.getId());
 	    		if(StringUtils.isNotBlank(g.getArrangId()) && g.getArrangId().length()==32){
 	    			//班级关联的教师信息
 	    			delid.add(g.getArrangId());
 	    		}
 	    	}
 	    }
		//删除
		if(delid.size()>0){
			 gkTimetableLimitArrangDao.deleteByIds(delid.toArray(new String[]{}));
		}
    }

    @Override
    public GkTimetableLimitArrang findGkTimetableLimitArrangBySelectXyz(String acadyear, String semester,
            String limitId, String period, String periodInterval, String weekday) {
        return gkTimetableLimitArrangDao.findGkTimetableLimitArrangBySelectXyz(acadyear, semester, limitId, period,
                periodInterval, weekday);
    }

    @Override
    public String saveTimetableLimitArrang(String selectxyz, String planId, String dataFilter) {
    	GkTeachPlacePlan plan = gkTeachPlacePlanService.findPlanById(planId,true);
        GkSubjectArrange gkSubjectArrange = gkSubjectArrangeDao.getOne(plan.getSubjectArrangeId());
        String acadyear = plan.getAcadyear();
        String semester = plan.getSemester();
        String gradeId = gkSubjectArrange.getGradeId();
        String[] array = selectxyz.split("_");
        String weekday = array[0];
        String period = array[1];
        String periodInterval = array[2];
        
        CourseScheduleDto dto=new CourseScheduleDto();
        dto.setGradeId(gradeId);
        dto.setAcadyear(acadyear);
        dto.setSemester(Integer.parseInt(semester));
        dto.setSchoolId(gkSubjectArrange.getUnitId());
        dto.setWeekOfWorktime1(plan.getWeekOfWorktime());
        dto.setWeekOfWorktime2(plan.getWeekOfWorktime2());
//        dto.setDayOfWeek1(rent.getDayOfWeek());
//        dto.setDayOfWeek2(rent.getDayOfWeek2());
        dto.setPeriod(Integer.parseInt(period));
        dto.setPeriodInterval(periodInterval);
        dto.setDayOfWeek(Integer.parseInt(weekday));
        //根据年级取得班级
        List<Clazz> classList = SUtils.dt(classRemoteService.findByInGradeIds(new String[]{gradeId}),new TR<List<Clazz>>(){}); 
        //查出那第几周到第几周的 星期1的某个节点是否有上课信息
//        List<CourseSchedule> crlist = SUtils.dt(courseScheduleRemoteService.getByCourseScheduleDto(dto),new TR<List<CourseSchedule>>(){});
//        if(CollectionUtils.isNotEmpty(crlist)){
//        	//DOTO 排除notIds的课程
//        	return "课程表中节次已存在课程信息,不能设置教学班上课信息";
//        }
        Set<String> limitIds=new HashSet<String>();
        Set<String> delid=new HashSet<String>();
        if(CollectionUtils.isNotEmpty(classList)){
        	for(int i=0;i<classList.size();i++){
     			limitIds.add(classList.get(i).getId());
     		}
        }
        
        //删除年级限制limitIds  planId:从原来的轮次id改为方案id
      	limitIds.add(planId);
      	//删除班级在该节次的所有限制
 		List<GkTimetableLimitArrang> limitList = gkTimetableLimitArrangDao
 	                .findGkTimetableLimitArrangBySelectXyz(acadyear, semester,period, periodInterval, weekday,limitIds.toArray(new String[]{}));
 	    if(CollectionUtils.isNotEmpty(limitList)){
 	    	for(GkTimetableLimitArrang g:limitList){
 	    		delid.add(g.getId());
 	    		if(StringUtils.isNotBlank(g.getArrangId())){
 	    			//班级关联的教师信息
 	    			delid.add(g.getArrangId());
 	    		}
 	    	}
 	    }
		//删除
		if(delid.size()>0){
			 gkTimetableLimitArrangDao.deleteByIds(delid.toArray(new String[]{}));
		}
		List<GkTimetableLimitArrang> insertList=new ArrayList<GkTimetableLimitArrang>();
        GkTimetableLimitArrang gkTimetableLimitArrang = new GkTimetableLimitArrang();
        gkTimetableLimitArrang.setAcadyear(acadyear);
        gkTimetableLimitArrang.setSemester(semester);
        gkTimetableLimitArrang.setLimitId(planId);
        gkTimetableLimitArrang.setLimitType(GkElectveConstants.LIMIT_TYPE_6); // 06 代表7选3 类型
        gkTimetableLimitArrang.setPeriod(period);
        gkTimetableLimitArrang.setPeriodInterval(periodInterval);
        gkTimetableLimitArrang.setWeekday(weekday);
        gkTimetableLimitArrang.setArrangId(dataFilter);
        gkTimetableLimitArrang.setId(UuidUtils.generateUuid());
        insertList.add(gkTimetableLimitArrang);
        if(CollectionUtils.isNotEmpty(classList)){
        	for(int i=0;i<classList.size();i++){
     			gkTimetableLimitArrang = new GkTimetableLimitArrang();
     	        gkTimetableLimitArrang.setAcadyear(acadyear);
     	        gkTimetableLimitArrang.setSemester(semester);
     	        gkTimetableLimitArrang.setLimitId(classList.get(i).getId());
     	        gkTimetableLimitArrang.setLimitType(GkElectveConstants.LIMIT_TYPE_4); // 04
     	        gkTimetableLimitArrang.setPeriod(period);
     	        gkTimetableLimitArrang.setPeriodInterval(periodInterval);
     	        gkTimetableLimitArrang.setWeekday(weekday);
     	        gkTimetableLimitArrang.setArrangId(planId);
     	        gkTimetableLimitArrang.setId(UuidUtils.generateUuid());
     	        gkTimetableLimitArrang.setRemark(BaseConstants.PC_KC+dataFilter);
     	        insertList.add(gkTimetableLimitArrang);
     		}
        }
        this.saveAllEntitys(insertList.toArray(new GkTimetableLimitArrang[]{}));
        return null;
    }
    
    @Override
    public List<GkTimetableLimitArrang> saveAllEntitys(GkTimetableLimitArrang... gkTimetableLimitArrang) {
        return gkTimetableLimitArrangDao.saveAll(checkSave(gkTimetableLimitArrang));
    }

    @Override
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListbyLimitIds(String searchAcadyear,
            String searchSemester, String limitType, String... limitIds) {
        return gkTimetableLimitArrangDao.findGkTimetableLimitArrangListbyLimitIds(searchAcadyear, searchSemester,
                limitType, Arrays.asList(limitIds));
    }

    @Override
	public void deleteByRoundsId(String acadyear, String semester, String roundsId) {
		gkTimetableLimitArrangDao.deleteByLimitId(acadyear, semester, roundsId,GkElectveConstants.LIMIT_TYPE_6);
		gkTimetableLimitArrangDao.deleteByArrangeId(acadyear, semester, roundsId,GkElectveConstants.LIMIT_TYPE_4);
	}
    @Override
    public void notUsing(String acadyear, String semester, String roundsId) {
    	gkTimetableLimitArrangDao.notUsingByLimitId(acadyear, semester, roundsId,GkElectveConstants.LIMIT_TYPE_6);
		gkTimetableLimitArrangDao.notUsingByArrangeId(acadyear, semester, roundsId,GkElectveConstants.LIMIT_TYPE_4);
    }
    
    @Override
    public void yesUsing(String acadyear, String semester, String roundsId) {
    	gkTimetableLimitArrangDao.yesUsingByLimitId(acadyear, semester, roundsId,GkElectveConstants.LIMIT_TYPE_6);
		gkTimetableLimitArrangDao.yesUsingByArrangeId(acadyear, semester, roundsId,GkElectveConstants.LIMIT_TYPE_4);
    }
    
	@Override
	protected BaseJpaRepositoryDao<GkTimetableLimitArrang, String> getJpaDao() {
		return gkTimetableLimitArrangDao;
	}

	@Override
	protected Class<GkTimetableLimitArrang> getEntityClass() {
		return GkTimetableLimitArrang.class;
	}

	@Override
	public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListIsUsing(
			String searchAcadyear, String searchSemester, String limitId,
			String limitType) {
		return gkTimetableLimitArrangDao.findGkTimetableLimitArrangListIsUsing(searchAcadyear,searchSemester,limitId,limitType);
	}

	@Override
	public List<GkTimetableLimitArrang> findByArrangeIdType(String acadyear, String semester,String arrangeId,
			String limitType) {
		return gkTimetableLimitArrangDao.findByArrangeIdType(acadyear,semester,arrangeId,limitType);
	}

	@Override
	public void deleteBtIds(String[] ids) {
		if(ids!=null && ids.length>0){
			gkTimetableLimitArrangDao.deleteByIds(ids);
		}
	}
}
