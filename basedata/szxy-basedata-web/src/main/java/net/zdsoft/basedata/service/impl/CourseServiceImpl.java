package net.zdsoft.basedata.service.impl;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.CourseDao;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CourseTypeService;
import net.zdsoft.basedata.service.SchoolService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Service("courseService")
public class CourseServiceImpl extends BaseServiceImpl<Course, String> implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private CourseTypeService courseTypeService;
    @Autowired
    private SchoolService schoolService;

    private static Logger logger = Logger.getLogger(CourseServiceImpl.class);
    
    @Override
    public List<Course> findByUnitIdIn(String[] unitIds,String type, String courseTypeId, String subjName, String section, String subjectType) {
    	List<Course> courseList = null;
    	courseList = courseDao.getCourseBySection(unitIds, type, subjName,courseTypeId, section, null,subjectType);
    	makeOtherParm(courseList);

        return courseList;
    }
    
   

    @Override
    protected BaseJpaRepositoryDao<Course, String> getJpaDao() {
        return courseDao;
    }

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }

    @Override
    public List<Course> findBySubjectCodes(String[] subjectCodes) {
        return courseDao.findBySubjectCodeIn(subjectCodes);
    }

    @Override
    public int findMaxOrderId(String unitId) {
        try {
        	List<String> unitIds = new ArrayList<String>();
        	Unit unit = unitService.findTopUnit(unitId);
    		String topUnitId = unit.getId();
    		unitIds.add(unitId);
    		unitIds.add(topUnitId);
            return courseDao.findMaxOrderId(unitIds.toArray(new String[0]));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public List<Course> findByUnitIdInAndSubjectName(String unitId, String subjectName) {
    	List<String> unitIds = new ArrayList<String>();
		Unit unit = unitService.findTopUnit(unitId);
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);
        return courseDao.findByIsDeletedAndUnitIdInAndSubjectName(Constant.IS_DELETED_FALSE, unitIds.toArray(new String[0]), subjectName);
    }

    @Override
    public void updateIsDeleteds(String[] ids) {
    	//删除扩展业务数据
		Evn.syncBasedataDelete(Course.class, ids);
		List<Course> courseList = findListByIdIn(ids);
		// 若为虚拟课程重新排序
		if (CollectionUtils.isNotEmpty(courseList)) {
			Course tmp = courseList.get(0);
			if (BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(tmp.getType())) {
				List<Course> virtualCourses = getListByCondition(tmp.getUnitId(), new String[]{BaseConstants.SUBJECT_TYPE_VIRTUAL}, null, null, null, null, 1);
				Set<String> idSet = new HashSet<>();
				CollectionUtils.addAll(idSet, ids);
				int index = 1;
				for (Course one : virtualCourses) {
					if (idSet.contains(one.getId())) {
						one.setIsDeleted(1);
					} else {
						one.setOrderId(index++);
					}
				}
				courseDao.saveAll(virtualCourses);
				return;
			}
		}
        courseDao.updateIsDeleteds(ids);
    }

	@Override
	public List<Course> findByUnitIdIn(String[] unitIds, String type,
			String... sections) {
		
		List<Course> courseList = new ArrayList<Course>();
        if (sections != null && sections.length > 0) {
        	TreeSet<String> set = new TreeSet<String>();
        	for (String section2 : sections) {
        		if(StringUtils.isNotBlank(section2))
        			set.addAll(Arrays.asList(section2.split(",")));
			}
        	String section = StringUtils.join(set.toArray(new String[0]),",");
        	courseList = courseDao.getCourseBySection(unitIds, type,null, null, section, null,null);
        } else {
        	courseList = courseDao.getCourseBySection(unitIds, type,null, null, null, null,null);
        }
        
		return courseList;
	}
    @Override
    public List<Course> findByUnitIdAndIsUsingList(String unitId, Integer isUsing) {
        Unit ut = unitService.findTopUnit(unitId);
        String topid = "";
        if (ut != null) {
            topid = ut.getId();
        }

        if (isUsing != null) {
            return courseDao.findByIsDeletedAndIsUsingAndUnitIdInOrderBySubjectTypeAscOrderIdAsc(Course.FALSE_0,
                    isUsing, new String[] { unitId, topid });
        }
        else {
            return courseDao.findByIsDeletedAndUnitIdInOrderBySubjectTypeAscOrderIdAsc(Course.FALSE_0, new String[] {
                    unitId, topid });
        }
    }

    @Override
    public List<Course> saveAllEntitys(Course... course) {
        return courseDao.saveAll(checkSave(course));
    }
    
    /**
     * 检查课程编码和课程名称的唯一性
     * @param course
     * @param AddOrUp
     * @return
     */
    @Override
    public String checkUnique(Course course,String AddOrUp,String unitId){
    	String mess="";
    	List<Course> c1 = findByUnitIdInAndSubjectName(unitId,course.getSubjectName());
    	for(Course c:c1){
    		String section =c.getSection()==null?"":c.getSection();
    		String section1 =course.getSection()==null?section:course.getSection();
    		if(("add".equals(AddOrUp) && course.getSubjectName().equals(c.getSubjectName()) && section.indexOf(section1)>=0 ||
    				("update".equals(AddOrUp) && course.getSubjectName().equals(c.getSubjectName()) 
    						&& section.indexOf(section1)>=0 && (!c.getId().equals(course.getId()))))){
    			mess=mess+ "学段内课程名称已重复";
    			break;
    		}
    	}
    	//修改时不验证课程码唯一性，因为课程码不可修改
    	if(StringUtils.isBlank(course.getId())){
    		List<Course> c2 = findByUnitIdInAndCourseCode(unitId, course.getSubjectCode());
    		
	    	for(Course c:c2){
	    		if(("add".equals(AddOrUp) && course.getSubjectCode().equals(c.getSubjectCode())) ||
	    				("update".equals(AddOrUp) && course.getSubjectCode().equals(c.getSubjectCode()) && (!c.getId().equals(course.getId())))){
	    			if(StringUtils.isNotBlank(mess)){
	    				mess=mess+ ",课程码已重复";
	    			}else{
	    				mess=mess+ "课程码已重复";
	    			}
	    			break;
	    		}
	    	}
	    	//验证编号的 格式
	    	if(!course.getSubjectCode().matches("^[0-9a-zA-Z]+$")){
	    		mess=mess+ "课程码只支持数字、字母";
	    	}
    	}
    	
    	if(StringUtils.isNotBlank(mess)){
			mess=mess+ ",保存失败";
		}
    	return mess;
    }

	@Override
	public List<Course> findByUnitIdAndCourseTypeIdIn(String unitId,
			String[] courseTypeIds) {
		return courseDao.findByIsDeletedAndUnitIdAndCourseTypeIdIn(Constant.IS_DELETED_FALSE,unitId,courseTypeIds);
	}

	@Override
	public List<Course> findByUnitIdInAndCourseCode(String unitId,
			String code) {
		List<String> unitIds = new ArrayList<String>();
		Unit unit = unitService.findTopUnit(unitId);
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);
		return courseDao.findByIsDeletedAndUnitIdInAndSubjectCode(Constant.IS_DELETED_FALSE, unitIds.toArray(new String[0]),code);
	}

	@Override
	public void updateInitCourse(String topUnitId) {
		List<Course> list =this.findListBy(new String[] {"unitId", "isDeleted"}, new Object[] {BaseConstants.ZERO_GUID, 0});
		if(CollectionUtils.isNotEmpty(list)) {
			List<String[]> courseList =new ArrayList<>();
			for(Course c : list) {
				String[] strs=new String[3];
				strs[0]=UuidUtils.generateUuid();
				strs[1]= topUnitId;
				strs[2]= c.getId();
				courseList.add(strs);
			}
			courseDao.updateIdAndUnitId(courseList);
		}
		
		//courseDao.saveAll(list);
	}

	@Override
	public List<Course> findBySection(String[] unitIds, String type, String courseTypeId, String subjName, String section, Integer isUsing, String subjectType) {
		return courseDao.getCourseBySection(unitIds, type, subjName, courseTypeId, section, isUsing, subjectType);
	}

	@Override
	public LinkedHashMap<String, String> findPartCouByIds(String[] ids) {
		List<Object[]> coulist=null;
		if(ids != null && ids.length>0) {
			coulist = courseDao.findPartCouByIds(ids);
		}
		LinkedHashMap<String, String> map =new LinkedHashMap<>();
		if(CollectionUtils.isNotEmpty(coulist)){
			for(Object[] strs :coulist){
				map.put((String)strs[0], (String)strs[1]);
			}
		}
		return map;
	}

	@Override
	public List<Course> findByTopUnitAndUnitId(String unitId) {
		List<String> unitIds = new ArrayList<String>();
		Unit unit = unitService.findTopUnit(unitId);
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);
		return courseDao.findByUnitIdsOrderId(unitIds.toArray(new String[0]));
	}

	@Override
	public List<Course> findByUnitIdAndTypeAndLikeSection(String unitId, String type, String section) {
		List<String> unitIds = new ArrayList<String>();
		Unit unit = unitService.findTopUnit(unitId);
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);
		return courseDao.findByUnitIdsAndTypeAndLikeSection(unitIds.toArray(new String[0]), type, "%"+section+"%");
	}

	@Override
	public List<Course> getListByCondition(String unitId, String[] types, String courseTypeId, String subjName,
			String section, String subjectType, Integer isUsing) {
		List<String> unitIds = new ArrayList<String>();
		Unit unit = unitService.findTopUnit(unitId);
		String topUnitId = unit.getId();
		unitIds.add(unitId);
		unitIds.add(topUnitId);
		return courseDao.getListByCondition(unitIds.toArray(new String[0]), types, subjName, courseTypeId, section, isUsing, subjectType);
	}
	
	@Override
	public List<Course> getVirtualCourses(String unitId,String section){
		Integer isUsing = null;
		// 虚拟课程的学科编号
		String courseTypeId = BaseConstants.VIRTUAL_COURSE_TYPE;
		List<Course> virtualCourses = getListByCondition(unitId, new String[]{BaseConstants.SUBJECT_TYPE_VIRTUAL}, courseTypeId, null, section, null, isUsing);
		return virtualCourses;
	}
	
	@Override
	public void makeOtherParm(List<Course> courseList) {
		if(CollectionUtils.isEmpty(courseList)) {
			return;
		}
    	Map<String,CourseType> courseTypeMap = new HashMap<String,CourseType>();
    	 
        Set<String> ids = EntityUtils.getSet(courseList, e->e.getCourseTypeId());
         
        courseTypeMap = courseTypeService.findMapByIdIn(ids.toArray(new String[0]));
         
        String sectionMcode = ColumnInfoUtils.getColumnInfo(Course.class, "section").getMcodeId();
        Map<String, McodeDetail> sectionMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(sectionMcode),new TypeReference<Map<String,McodeDetail>>(){});
         
        if (CollectionUtils.isNotEmpty(courseList)) {
              for (Course course : courseList) {
                 if (Constant.IS_DELETED_FALSE == course.getIsUsing()) {
                     course.setIsUsingName("否");
                 }
                 else {
                     course.setIsUsingName("是");
                 }
                 //TODO 填充課程類型名稱 1.科目表中的科目名稱 2.微代碼中的mcodeContent
                 CourseType subject = courseTypeMap.get(course.getCourseTypeId());
                 if(subject!=null){
                 	course.setCourseTypeName(subject.getName());
                 }
                 
                 //设置学段 1.先获取学段数据
                 String[] sectionArray = null;
                 if(StringUtils.isBlank(course.getSection())){
                 	//如果section为空的情况，将其设为空串
                 	 course.setSectionName("");
                 	 continue;
                 }
                 sectionArray = course.getSection().split(",");
                 
                 if(sectionArray.length<1){
                 	sectionArray = schoolService.findOne(course.getUnitId()).getSections().split(",");
                 	if(sectionArray.length<1){
                 		sectionArray = sectionMap.keySet().toArray(new String[0]);
                 	}
                 }
                 //拼接字符串
                 String sectionNames = "";
                 for (String section2 : sectionArray) {
                 	if(sectionMap.get(section2) == null){
                 		logger.error("mcodeId:"+sectionMcode+", this_id"+section2+"; 微代码中未找到对应学段");
                 		continue;
                 	}
                 	sectionNames += "," + sectionMap.get(section2).getMcodeContent();
 				}
                 if(sectionNames.startsWith(",")){
                 	sectionNames = sectionNames.substring(1);
                 }
                 
                 course.setSectionName(sectionNames);
             }
         }
    }



	@Override
	public List<Course> getListByConditionWithMaster(String unitId, String[] types, String courseTypeId, String subjName,
			String section, String subjectType, Integer isUsing) {
		return getListByCondition(unitId, types, courseTypeId, subjName, section, subjectType, isUsing);
	}

	@Override
	public List<Course> getCourseByUnitIdIn(String... unitId) {
		return courseDao.getCourseByUnitIdIn(unitId);
	}
}
