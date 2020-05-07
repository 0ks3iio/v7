package net.zdsoft.basedata.remote.service.impl;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.CourseDao;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.service.config.SystemIniService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("courseRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CourseRemoteServiceImpl extends BaseRemoteServiceImpl<Course, String> implements CourseRemoteService {

	@Autowired
	private CourseService courseService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private SystemIniService systemIniService;
	@Override
	protected BaseService<Course, String> getBaseService() {
		return courseService;
	}
	/**
	 * Done 1
	 */
	@Override
	public String findByUnitIdIn(String[] unitIds, String... sections) {
		return SUtils.s(courseService.findByUnitIdIn(unitIds, BaseConstants.SUBJECT_TYPE_BX, sections));
	}
	@Override
	public String findByUnitIdIn(String[] unitIds,Integer type, String... sections) {
		if(type==null) {
			return SUtils.s(courseService.findByUnitIdIn(unitIds, BaseConstants.SUBJECT_TYPE_BX+"", sections));
		}
		return SUtils.s(courseService.findByUnitIdIn(unitIds, type+"", sections));
	}

	@Override
	public String findBySubjectUnitIdIsUsingName(String unitId, int isUsing, String likeName) {
		if(likeName==null){
			likeName = "";
		}
		likeName = "%"+likeName+"%";
		return SUtils.s(sortCourses(courseDao.findBySubjectUnitIdIsUsingName(unitId, isUsing, likeName)));
	}
	@Override
	public String findBySubjectUnitIdName(String unitId, String likeName) {
		if(likeName==null){
			likeName = "";
		}
		likeName = "%"+likeName+"%";
		return SUtils.s(sortCourses(courseDao.findBySubjectUnitIdName(unitId, likeName)));
	}
	@Override
	public String findBySubjectIdIn(String[] subids) {
		return SUtils.s(courseDao.findBySubjectIdIn(subids));
	}
	
	@Override
	public String findByBaseCourseCodes(String... codes) {
		return findByUnitCourseCodes(null, codes);
	}
	
	@Override
	public String findByBaseCourseCodes(String[] unitIds, String... codes) {
		return SUtils.s(courseDao.findByBaseCourseCodes(unitIds, codes));
	}

	@Override
	public String findByUnitCourseCodes(String unitId, String... codes) {
		Unit unit = unitService.findTopUnit(unitId);
		List<Course> cs = new ArrayList<Course>();
		//查询单位课程
		if (unitId != null) {
			cs.addAll(courseDao.findByUnitCourseCodes(unitId, codes));
		}
		if(unit!=null){
			if(!unit.getId().equals(unitId)){//如果传入相同的单位就不需要再次查找,如果为空查找顶级教育局数据
				//会默认查询教育局课程
				cs.addAll(courseDao.findByUnitCourseCodes(unit.getId(), codes));
			}	
		}
		return SUtils.s(cs);
	}
	
	@Override
	public String findWuliLiShi(String unitId) {
		return SUtils.s(findByUnitCourseCodes(unitId, new String[] {"3006","3002"}));
	}
	
	@Override
	public String findBy(String[] unitIds, String type, String courseTypeId,
			String subjName, String section, Integer isUsing, String subjectType) {
		return SUtils.s(courseDao.getCourseBySection(unitIds, type, subjName,courseTypeId, section, isUsing,subjectType));
	}

    @Override
    public void updateInitCourse(String unitId) {
        courseService.updateInitCourse(unitId);
    }
	@Override
	public String findByCodes73(String unitId) {
		return RedisUtils.getObject("COURSE_CODE_73_" + unitId, RedisUtils.TIME_ONE_HOUR, new TypeReference<String>(){}, new RedisInterface<String>(){
			@Override
			public String queryData() {
				if(BaseConstants.DEPLOY_HANGWAI.equals(systemIniService.findValue("SYSTEM.DEPLOY.REGION"))){
					return findByUnitCourseCodes(unitId, BaseConstants.SUBJECT_73_HW);
				}else{
					//浙江省的单位都是选7
					Unit unit =unitService.findOne(unitId);
					if(unit !=null && StringUtils.isNotBlank(unit.getRegionCode()) && unit.getRegionCode().indexOf("33")==0){
						return findByUnitCourseCodes(unitId, BaseConstants.SUBJECT_73);
					}else{
						return findByUnitCourseCodes(unitId, BaseConstants.SUBJECT_63);
					}

				}
			}
        });
	}

	@Override
	public String findByCodes73YSY(String unitId) {
		return RedisUtils.getObject("COURSE_CODE_73YSY_" + unitId, RedisUtils.TIME_ONE_HOUR, new TypeReference<String>(){}, new RedisInterface<String>(){
			@Override
			public String queryData() {
				if(BaseConstants.DEPLOY_HANGWAI.equals(systemIniService.findValue("SYSTEM.DEPLOY.REGION"))){
					return findByUnitCourseCodes(unitId, ArrayUtils.addAll(BaseConstants.SUBJECT_TYPES_YSY_HW, BaseConstants.SUBJECT_73_HW));
				}else{
					//浙江省的单位都是选7
					Unit unit =unitService.findOne(unitId);
					if(unit !=null && StringUtils.isNotBlank(unit.getRegionCode()) && unit.getRegionCode().indexOf("33")==0){
						return findByUnitCourseCodes(unitId, ArrayUtils.addAll(BaseConstants.SUBJECT_TYPES_YSY, BaseConstants.SUBJECT_73));
					}else{
						return findByUnitCourseCodes(unitId, ArrayUtils.addAll(BaseConstants.SUBJECT_TYPES_YSY, BaseConstants.SUBJECT_63));
					}
				}
			}
        });
	}
	@Override
	public String findByCodesYSY(String unitId) {
		return RedisUtils.getObject("COURSE_CODE_YSY_" + unitId, RedisUtils.TIME_ONE_HOUR, new TypeReference<String>(){}, new RedisInterface<String>(){
			@Override
			public String queryData() {
				if(BaseConstants.DEPLOY_HANGWAI.equals(systemIniService.findValue("SYSTEM.DEPLOY.REGION"))){
					return findByUnitCourseCodes(unitId, BaseConstants.SUBJECT_TYPES_YSY_HW);
				}else{
					return findByUnitCourseCodes(unitId, BaseConstants.SUBJECT_TYPES_YSY);
				}
			}
        });
	}
	
	public String orderCourse(String courseJsonStrs) {
		List<Course> css = SUtils.dt(courseJsonStrs, new TR<List<Course>>() {});
		return SUtils.s(sortCourses(css));
	}
	
	/**
	 * 排序
	 * @param css 集合
	 */
	private List<Course> sortCourses(List<Course> css) {
		if(CollectionUtils.isNotEmpty(css)) {
			for(Course cs : css) {
				if(cs.getOrderId() == null) {
					cs.setOrderId(Integer.MAX_VALUE);
				}
			}
			Collections.sort(css, (x,y) -> x.getOrderId().compareTo(y.getOrderId()));
		}
		return css;
	}
	@Override
	public String findBySubjectNameIn(String unitId, String[] subNames) {
		Unit unit = unitService.findTopUnit(unitId);
		List<Course> cs = new ArrayList<Course>();
		if(unitId!=null && unit!=null) {
			if(!unit.getId().equals(unitId)) {
				cs=courseDao.findByUnitIdAndSubjecctNameIn(new String[] {unitId,unit.getId()}, subNames);
			}else {
				cs=courseDao.findByUnitIdAndSubjecctNameIn(new String[] {unitId}, subNames);
			}
		}else if(unit!=null){
			cs=courseDao.findByUnitIdAndSubjecctNameIn(new String[] {unit.getId()}, subNames);
		}
		return SUtils.s(cs);
	}
	@Override
	public String findPartCouByIds(String[] ids) {
		return SUtils.s(courseService.findPartCouByIds(ids));
	}
	@Override
	public String findByUnitIdAndTypeAndLikeSection(String unitId, String type, String section) {
		return SUtils.s(courseService.findByUnitIdAndTypeAndLikeSection(unitId, type, section));
	}
	@Override
	public String getListByCondition(String unitId, String type, String subjName, String courseTypeId, String section,
			Integer isUsing, String subjectType) {
		String[] types=null;
		if(StringUtils.isNotBlank(type)) {
			types=new String[] {type};
		}
		return SUtils.s(courseService.getListByCondition(unitId, types, courseTypeId, subjName,  section, subjectType,isUsing));
	}
	@Override
	public String findByUnitIdIn(String... unitId) {
		return SUtils.s(courseService.getCourseByUnitIdIn(unitId));
	}
	@Override
	public String getVirtualCourses(String unitId, String section) {
		return SUtils.s(courseService.getVirtualCourses(unitId, section));
	}
}
