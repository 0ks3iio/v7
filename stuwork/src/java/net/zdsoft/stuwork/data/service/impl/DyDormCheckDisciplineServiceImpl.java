package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyDormCheckDisciplineDao;
import net.zdsoft.stuwork.data.dto.DormSearchDto;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormCheckDiscipline;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormCheckDisciplineService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("dyDormCheckDisciplineService")
public class DyDormCheckDisciplineServiceImpl extends BaseServiceImpl<DyDormCheckDiscipline, String> implements DyDormCheckDisciplineService{

	@Autowired
	private DyDormCheckDisciplineDao dyDormCheckDisciplineDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	
	
	@Override
	public List<DyDormCheckDiscipline> getDetailByCon(String unitId,DormSearchDto dormDto){
		return dyDormCheckDisciplineDao.getDetailByCon(unitId, dormDto.getAcadyear(), dormDto.getSemesterStr(), dormDto.getStudentId());
	}
	@Override
	public List<DyDormCheckDiscipline> getPoolByCon(String unitId,String[] classIds,DormSearchDto dormDto){
		List<DyDormCheckDiscipline> checkDisList=new ArrayList<DyDormCheckDiscipline>();
		String studentId=dormDto.getStudentId();
		String buildingId=dormDto.getSearchBuildId();
		boolean studentIdFlag=StringUtils.isNotBlank(studentId);
		boolean buildingIdFlag=StringUtils.isNotBlank(buildingId);
		if(classIds!=null && classIds.length>0){
			if(studentIdFlag||buildingIdFlag){
				if(studentIdFlag && buildingIdFlag){
					checkDisList=dyDormCheckDisciplineDao.getPoolByConThree(unitId, dormDto.getAcadyear(), dormDto.getSemesterStr(), buildingId, studentId, classIds);
				}else if(studentIdFlag){
					checkDisList=dyDormCheckDisciplineDao.getPoolByConOne(unitId, dormDto.getAcadyear(), dormDto.getSemesterStr(), studentId, classIds);
				}else if(buildingIdFlag){
					checkDisList=dyDormCheckDisciplineDao.getPoolByConTwo(unitId, dormDto.getAcadyear(), dormDto.getSemesterStr(), buildingId, classIds);
				}
			}else{
				checkDisList=dyDormCheckDisciplineDao.getPoolByCon(unitId, dormDto.getAcadyear(), dormDto.getSemesterStr(), classIds);
			}
		}
		
		List<DyDormCheckDiscipline> disList=new  ArrayList<DyDormCheckDiscipline>();
		if(CollectionUtils.isNotEmpty(checkDisList)){
			//学生名称  班级名称 寝室楼 寝室号 各个map
			List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(classIds),new TR<List<Student>>(){});
			Map<String,String> studentNameMap=EntityUtils.getMap(studentList,"id","studentName");
			List<Clazz> classList=SUtils.dt(classRemoteService.findListByIds(classIds),new TR<List<Clazz>>(){});
			Map<String,String> classNameMap=EntityUtils.getMap(classList,"id","classNameDynamic");
			List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
			Map<String,String> buildingNameMap=EntityUtils.getMap(buildingList,"id","name");
			List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId,StuworkConstants.STU_TYPE);
			Map<String,String> roomNameMap=EntityUtils.getMap(roomList,"id","roomName");
			
			Map<String,DyDormCheckDiscipline> map=new HashMap<String,DyDormCheckDiscipline>();
			for(DyDormCheckDiscipline checkDis:checkDisList){
				DyDormCheckDiscipline dis=map.get(checkDis.getStudentId());
				//将一个学生的违纪情况进行封装
				if(dis==null){
					dis=new DyDormCheckDiscipline();
					dis.setStudentName(studentNameMap.get(checkDis.getStudentId()));
					dis.setClassName(classNameMap.get(checkDis.getClassId()));
					dis.setBuildingName(buildingNameMap.get(checkDis.getBuildingId()));
					dis.setRoomName(roomNameMap.get(checkDis.getRoomId()));
					dis.setStudentId(checkDis.getStudentId());
					//dis.setClassId(checkDis.getClassId());
					//dis.setBuildingId(checkDis.getBuildingId());
					//dis.setRoomId(checkDis.getRoomId());
					dis.setScore(checkDis.getScore());
				}else{
					dis.setScore(dis.getScore()+checkDis.getScore());
				}
				map.put(checkDis.getStudentId(), dis);
			}
			//把每个学生的违纪总情况（总分） 放入list中
			Set<Entry<String, DyDormCheckDiscipline>> entries=map.entrySet();
			for(Entry<String, DyDormCheckDiscipline> entry:entries){
				disList.add(entry.getValue());
			}
		}
		return disList;
	}
	@Override
	public List<DyDormCheckDiscipline> getCheckDisList(final DormSearchDto dormDto,String[] buildingIds,Pagination page){
		List<DyDormCheckDiscipline> checkDisList=new ArrayList<DyDormCheckDiscipline>();
		Specification<DyDormCheckDiscipline> specification=new Specification<DyDormCheckDiscipline>() {
			@Override
			public Predicate toPredicate(Root<DyDormCheckDiscipline> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				
				ps.add(cb.equal(root.get("schoolId").as(String.class), dormDto.getUnitId()));
				if(StringUtils.isNotBlank(dormDto.getAcadyear())){
					ps.add(cb.equal(root.get("acadyear").as(String.class), dormDto.getAcadyear()));
				}
				if(StringUtils.isNotBlank(dormDto.getSemesterStr())){
					ps.add(cb.equal(root.get("semester").as(String.class), dormDto.getSemesterStr()));
				}
//                List<Order> orderList = new ArrayList<Order>();
//                orderList.add(cb.asc(root.get("acadyear").as(String.class)));
                cq.where(ps.toArray(new Predicate[0]));//.orderBy(orderList);
	                return cq.getRestriction();
			}
		};
		if(page!=null){
			checkDisList=findAll(specification, page);
		}else{
			checkDisList=findAll(specification);
		}
		if(CollectionUtils.isNotEmpty(checkDisList)){
			Set<String> studentIds=new HashSet<String>();
			for(DyDormCheckDiscipline checkDis:checkDisList){
				studentIds.add(checkDis.getStudentId());
			}
			List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){});
			//Map<String,Student> studentMap=EntityUtils.getMap(studentList, "id");TODO
			Map<String,String> studentNameMap=new HashMap<String, String>();

			if(CollectionUtils.isNotEmpty(studentList)){
				for(Student stu:studentList){
					studentNameMap.put(stu.getId(), stu.getStudentName());
				}
			}
			Map<String,DyDormRoom> roomMap=dyDormRoomService.findMapByIn("buildingId", buildingIds);
			for(DyDormCheckDiscipline checkDis:checkDisList){
				checkDis.setStudentName(studentNameMap.get(checkDis.getStudentId()));
				DyDormRoom room=roomMap.get(checkDis.getRoomId());
				if(room!=null){
					checkDis.setRoomName(room.getRoomName());
				}
			}
		}
		return checkDisList;
	}
	
	@Override
	protected BaseJpaRepositoryDao<DyDormCheckDiscipline, String> getJpaDao() {
		return dyDormCheckDisciplineDao;
	}

	@Override
	protected Class<DyDormCheckDiscipline> getEntityClass() {
		return DyDormCheckDiscipline.class;
	}

}
