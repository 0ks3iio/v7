package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.AdjustedDao;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.service.AdjustedDetailService;
import net.zdsoft.basedata.service.AdjustedService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.TipsayExService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("adjustedService")
public class AdjustedServiceImpl extends BaseServiceImpl<Adjusted, String> implements AdjustedService {
	
	@Autowired
	private AdjustedDao adjustedDao;
	@Autowired
    private AdjustedDetailService adjustedDetailService;
	@Autowired
    private TipsayExService tipsayExService;
	@Autowired
    private CourseScheduleService courseScheduleService;

	@Override
	protected BaseJpaRepositoryDao<Adjusted, String> getJpaDao() {
		return adjustedDao;
	}
	
	@Override
	protected Class<Adjusted> getEntityClass() {
		return Adjusted.class;
	}

    @Override
    public void saveAllAdjust(List<Adjusted> adjustedList, List<AdjustedDetail> adjustedDetailList, List<TipsayEx> tipsayExList) {
        saveAll(adjustedList.toArray(new Adjusted[0]));
        adjustedDetailService.saveAll(adjustedDetailList.toArray(new AdjustedDetail[0]));
        tipsayExService.saveAll(tipsayExList.toArray(new TipsayEx[0]));
    }

    @Override
    public List<Adjusted> findListByTeacherIdAndWeek(String acadyear, String semester, String teacherId, String week) {
    	if(StringUtils.isNotBlank(week)) {
    		return adjustedDao.findListByTeacherIdAndWeek(acadyear, Integer.valueOf(semester), teacherId, Integer.valueOf(week));
    	}else {
    		return adjustedDao.findListByTeacherId(acadyear, Integer.valueOf(semester), teacherId);
    	}
        
    }

    @Override
    public void deleteById(String adjustedId) {
    	if(StringUtils.isBlank(adjustedId))
    		return;
    	deleteByIds(new String[] {adjustedId});
    }
    
    @Override
    public void deleteByIds(String[] adjustedIds) {
    	if(adjustedIds == null || adjustedIds.length ==0)
    		return;
    	adjustedDao.deleteByAdjustedIds(adjustedIds);
    	adjustedDetailService.deleteByAdjustedIds(adjustedIds);
    	tipsayExService.deleteByTipsayIds(adjustedIds);
    }

    @Override
    public List<Adjusted> findListBySchoolIdAndWeek(String acadyear, String semester, String unitId, String week) {
    	if(StringUtils.isNotBlank(week)) {
    		return adjustedDao.findListBySchoolIdAndWeek(acadyear, Integer.valueOf(semester), unitId, Integer.valueOf(week));
    	}else {
    		return adjustedDao.findListBySchoolId(acadyear, Integer.valueOf(semester), unitId);
    	}
    }

    @Override
    public String updateStateById(String adjustedId, String state, String teacherId) {
        String msg = null;
        AdjustedDetail adjustedDetail = adjustedDetailService.findOneBy(new String[] {"adjustedId", "adjustedType"}, new String[]{adjustedId, "01"});
        AdjustedDetail beenAdjustedDetail = adjustedDetailService.findOneBy(new String[] {"adjustedId", "adjustedType"}, new String[]{adjustedId, "02"});
        CourseSchedule courseSchedule = courseScheduleService.findOne(adjustedDetail.getCourseScheduleId());
        CourseSchedule switchCourseSchedule = courseScheduleService.findOne(beenAdjustedDetail.getCourseScheduleId());
        if ("1".equals(state)) {
            // 若原课表改变，直接按审核未通过处理
            if (!(checkTime(adjustedDetail, courseSchedule) && checkTime(beenAdjustedDetail, switchCourseSchedule))) {
                msg = "需调课程或被调课程已被更改，审核未通过，请重新提交";
                state = "2";
            } else {
                int weekOfWorktime = courseSchedule.getWeekOfWorktime();
                int dayOfWeek = courseSchedule.getDayOfWeek();
                String periodInterval = courseSchedule.getPeriodInterval();
                int period = courseSchedule.getPeriod();
                courseSchedule.setWeekOfWorktime(switchCourseSchedule.getWeekOfWorktime());
                courseSchedule.setDayOfWeek(switchCourseSchedule.getDayOfWeek());
                courseSchedule.setPeriodInterval(switchCourseSchedule.getPeriodInterval());
                courseSchedule.setPeriod(switchCourseSchedule.getPeriod());
                switchCourseSchedule.setWeekOfWorktime(weekOfWorktime);
                switchCourseSchedule.setDayOfWeek(dayOfWeek);
                switchCourseSchedule.setPeriodInterval(periodInterval);
                switchCourseSchedule.setPeriod(period);
                courseScheduleService.saveAllEntitys(new CourseSchedule[]{courseSchedule, switchCourseSchedule});
            }
        }
	    adjustedDao.updateStateById(adjustedId, state, new Date());
        tipsayExService.updateAuditorIdAndStateByAdjustId(adjustedId, state, teacherId);
        return msg;
    }

    /**
     * 检测审核时原课表是否变化
     * @param adjustedDetail
     * @param courseSchedule
     * @return
     */
    private boolean checkTime(AdjustedDetail adjustedDetail, CourseSchedule courseSchedule) {
	    if (adjustedDetail.getWeekOfWorktime() == null) {
	        return false;
        } else {
	        if (!adjustedDetail.getWeekOfWorktime().equals(courseSchedule.getWeekOfWorktime())) {
	            return false;
            }
        }

        if (adjustedDetail.getDayOfWeek() == null) {
            return false;
        } else {
            if (!adjustedDetail.getDayOfWeek().equals(courseSchedule.getDayOfWeek())) {
                return false;
            }
        }

        if (adjustedDetail.getPeriodInterval() == null) {
            return false;
        } else {
            if (!adjustedDetail.getPeriodInterval().equals(courseSchedule.getPeriodInterval())) {
                return false;
            }
        }

        if (adjustedDetail.getPeriod() == null) {
            return false;
        } else {
            if (!adjustedDetail.getPeriod().equals(courseSchedule.getPeriod())) {
                return false;
            }
        }

        // 检测任课老师是否因代课更改
        if (adjustedDetail.getTeacherId() == null) {
            // 防止老师原本就为空
            if (courseSchedule.getTeacherId() == null) {
                return true;
            }
            return false;
        } else {
            if (!adjustedDetail.getTeacherId().equals(courseSchedule.getTeacherId())){
                return false;
            }
        }

        return true;
    }

	@Override
	public void saveAll(Adjusted adjusted, AdjustedDetail[] adjustedDetails, TipsayEx tipsayEx, List<CourseSchedule> courseSchedules, List<String> delScheduleIds) {
		save(adjusted);
		adjustedDetailService.saveAll(adjustedDetails);
		tipsayExService.save(tipsayEx);
		
		courseScheduleService.saveScheduleModify(courseSchedules, delScheduleIds);
	}

    @Override
    public void updateRollBack(String adjustId, CourseSchedule adjustedCourseSchedule, CourseSchedule beenAdjustedCourseSchedule) {
        if (beenAdjustedCourseSchedule != null) {
            courseScheduleService.saveAllEntitys(new CourseSchedule[]{adjustedCourseSchedule, beenAdjustedCourseSchedule});
        }
        courseScheduleService.saveAllEntitys(new CourseSchedule[] {adjustedCourseSchedule});
        deleteById(adjustId);
    }

    @Override
    public void saveAllArrange(List<Adjusted> adjustedList, List<AdjustedDetail> adjustedDetailList, List<TipsayEx> tipsayExList, CourseSchedule[] courseSchedules) {
        saveAll(adjustedList.toArray(new Adjusted[0]));
        adjustedDetailService.saveAll(adjustedDetailList.toArray(new AdjustedDetail[0]));
        tipsayExService.saveAll(tipsayExList.toArray(new TipsayEx[0]));
        courseScheduleService.saveAllEntitys(courseSchedules);
    }

	@Override
	public void deleteByClassId(String classId) {
		adjustedDao.deleteByClassId(classId);
	}

	@Override
	public void deleteByWeeks(String searchAcadyear, Integer searchSemester, String schoolId, Integer[] weeks,
			String classId) {
		List<String> adjustedIdList =  adjustedDao.findByClassAndWeeks(searchAcadyear, searchSemester,
				schoolId,weeks, classId);
		if(CollectionUtils.isNotEmpty(adjustedIdList))
			this.deleteByIds(adjustedIdList.toArray(new String[0]));
	}
}
