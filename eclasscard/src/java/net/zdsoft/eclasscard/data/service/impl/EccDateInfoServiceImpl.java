package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccDateInfoDao;
import net.zdsoft.eclasscard.data.dto.EccDateInfoDto;
import net.zdsoft.eclasscard.data.entity.EccDateInfo;
import net.zdsoft.eclasscard.data.service.EccDateInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccDateInfoService")
public class EccDateInfoServiceImpl extends BaseServiceImpl<EccDateInfo, String> implements EccDateInfoService{
	@Autowired
	private EccDateInfoDao eccDateInfoDao;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<EccDateInfo, String> getJpaDao() {
		return eccDateInfoDao;
	}

	@Override
	protected Class<EccDateInfo> getEntityClass() {
		return EccDateInfo.class;
	}

	@Override
	public List<EccDateInfoDto> getDateList(String schoolId,String grade,String justSunDay) {
		Semester currentSemester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, schoolId), Semester.class);
		if(currentSemester == null){
			return new ArrayList<EccDateInfoDto>();
		}
		List<EccDateInfoDto> dataList = getDateInfo(schoolId,currentSemester.getSemesterBegin(),
					currentSemester.getSemesterEnd(),justSunDay);
		Map<String, EccDateInfo> infoMap =  getDateMap(schoolId, grade);
		for(EccDateInfoDto dto:dataList){
			String infoDate = DateUtils.date2StringByDay(dto.getInfoDate());
			if(infoMap.containsKey(infoDate)&&infoMap.get(infoDate)!=null){
				dto.setId(infoMap.get(infoDate).getId());
				dto.setMarkup(true);
				dto.setRemark(infoMap.get(infoDate).getRemark());
			}
		}
		return dataList;
	}

	private List<EccDateInfoDto> getDateInfo(String schoolId, Date beginDate, Date endDate,String justSunDay) {
		EccDateInfoDto entity;
		List<EccDateInfoDto> dateInfos = Lists.newArrayList();
        Calendar theDate = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

        // 初始化日期
        theDate.setTime(beginDate);
        calendarEnd.setTime(endDate);
        calendarEnd.add(Calendar.DATE, 1);

        int iWeekday;
        int i = 0;
        int theWeek = 0;
        // 若循环日期大于结束日期
        while (theDate.before(calendarEnd)) {
            i++;
            entity = new EccDateInfoDto();
            // 第一天不管是星期几都为第一周
            if (i == 1) {
                theWeek = 1;
            } else if (theDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // 若为星期天，加一周
                theWeek++;
            }

            entity.setSchoolId(schoolId); // 学校
            entity.setInfoDate(theDate.getTime()); // 日期

            // 周次已第一学期开始
            entity.setWeek(theWeek); // 周次

            // 老外以星期日为一周的第一天
            iWeekday = theDate.get(Calendar.DAY_OF_WEEK);
            if (iWeekday == 1) {
                entity.setWeekDay("日"); // 星期日
            } else {
                entity.setWeekDay(getWeekDayName(iWeekday - 1)); // 星期日
            }
            entity.setMarkup(false);
            if("1".equals(justSunDay)&&iWeekday!=1){
            	theDate.add(Calendar.DATE, 1);
            	continue;
            }
            dateInfos.add(entity);
            theDate.add(Calendar.DATE, 1);
        }
		return dateInfos;
	}
	private String getWeekDayName(int weekDay) {
		switch (weekDay) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "日";

		default:
			return "一";
		}
		
	}

	@Override
	public void saveDateInfos(String schoolId,String grade,List<EccDateInfoDto> eccDateInfoDtos) {
		List<EccDateInfo> eccDateInfos = Lists.newArrayList();
		Set<String> deleteIds = Sets.newHashSet();
		for(EccDateInfoDto dto:eccDateInfoDtos){
			if(dto.toEccDateInfo()!=null){
				dto.setGradeId(grade);
				dto.setSchoolId(schoolId);
				eccDateInfos.add(dto.toEccDateInfo());
			}
			if(!dto.isMarkup()&&StringUtils.isNotBlank(dto.getId())){
				deleteIds.add(dto.getId());
			}
		}
		//先删除
		if(deleteIds.size()>0){
			eccDateInfoDao.deleteDateInfo(deleteIds.toArray(new String[deleteIds.size()]));
		}
		if(eccDateInfos.size()>0){
			saveAll(eccDateInfos.toArray(new EccDateInfo[eccDateInfos.size()]));
		}
		
	}

	@Override
	public Map<String, EccDateInfo> getDateMap(String schoolId, String grade) {
		List<EccDateInfo> dateInfos = findListBy(new String[]{"schoolId","gradeId"}, new String[]{schoolId,grade});
		Map<String, EccDateInfo> map = Maps.newHashMap();
		for(EccDateInfo dateInfo:dateInfos){
			map.put(DateUtils.date2StringByDay(dateInfo.getInfoDate()), dateInfo);
		}
		return map;
	}

	@Override
	public EccDateInfo getByDateGrade(String gradeId, String date) {
		return eccDateInfoDao.getByDateGrade(gradeId, date);
	}

	@Override
	public List<EccDateInfo> getDateSchoolId(String unitId,
			String date) {
		return eccDateInfoDao.getDateSchoolId(unitId, date);
	}

	@Override
	public List<Date> getInfoDateList(String unitId, String gradeId,
			Date beginDate, Date endDate) {
		if(StringUtils.isBlank(unitId)||StringUtils.isBlank(gradeId)){
			return new ArrayList<Date>();
		}
		if(beginDate==null){
			beginDate = new Date();
		}
		if(endDate==null){
			endDate = new Date();
		}
		String bDate = DateUtils.date2StringByDay(beginDate);
		String eDate = DateUtils.date2StringByDay(endDate);
		return eccDateInfoDao.getInfoDateList(unitId,gradeId,bDate,eDate);
	}
	
}
