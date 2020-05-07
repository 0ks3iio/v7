package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyDormRoomDao;
import net.zdsoft.stuwork.data.dao.DyDormRoomJdbcDao;
import net.zdsoft.stuwork.data.dto.DyDormConDto;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("dyDormRoomService")
public class DyDormRoomServiceImpl extends BaseServiceImpl<DyDormRoom, String> implements DyDormRoomService{
	@Autowired
	private DyDormRoomDao dyDormRoomDao;
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	
	@Override
	public List<DyDormRoom>	findByName(String unitId,String roomName,String buildingId,String roomProperty){
		if(StringUtils.isBlank(roomName)){
			if(StringUtils.isNotBlank(roomProperty)){
				return dyDormRoomDao.findByBuildingId(unitId, buildingId, roomProperty);
			}
			return dyDormRoomDao.findByBuildingId(unitId, buildingId);
		}
		if(StringUtils.isNotBlank(roomProperty)){
			return dyDormRoomDao.findByName(unitId,roomName, buildingId, roomProperty);
		}
		return dyDormRoomDao.findByName(unitId,roomName,buildingId);
	}
	@Override
	public List<DyDormRoom> getDormRoomByUnitId(String unitId,String roomProperty){
		if(StringUtils.isNotBlank(roomProperty)){
			return dyDormRoomDao.findByUnitId(unitId, roomProperty);
		}
		return dyDormRoomDao.findByUnitId(unitId);
	}
	@Override
	public List<DyDormRoom> getDormRoomByProUnitId(String unitId,String roomProperty) {
		return dyDormRoomDao.findByUnitId(unitId,roomProperty);
	}
	//通过条件 获取roomList
	public List<DyDormRoom> getRoomList(final String unitId,
			final String buildingId,final String roomType,final String roomProperty,final Pagination page){
		List<DyDormRoom> roomList=new ArrayList<DyDormRoom>();
		Specification<DyDormRoom> specification=new Specification<DyDormRoom>() {
			@Override
			public Predicate toPredicate(Root<DyDormRoom> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				if(StringUtils.isNotBlank(buildingId)){
					ps.add(cb.equal(root.get("buildingId").as(String.class), buildingId));
				}
				if(StringUtils.isNotBlank(roomType)){
					ps.add(cb.equal(root.get("roomType").as(String.class), roomType));
				}
				if(StringUtils.isNotBlank(roomProperty)){
					ps.add(cb.equal(root.get("roomProperty").as(String.class), roomProperty));
				}
                List<Order> orderList = new ArrayList<Order>();
                //orderList.add(cb.asc(root.get("roomType").as(String.class)));
                orderList.add(cb.asc(root.get("roomName").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
			}
		};
		 if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<DyDormRoom> findAll = dyDormRoomDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            roomList=findAll.getContent();
        }
        else {
        	roomList=dyDormRoomDao.findAll(specification);
        }
		return roomList;
	}
	@Override
	public List<DyDormRoom> getDormRoomByCon(String unitId,String buildingId,String roomType,String roomProperty,Pagination page){
		return getRoomList(unitId, buildingId, roomType,roomProperty, page);
	}

	@Override
	public List<DyDormRoom> getDormRoomByCon(String unitId, String buildingId, String roomFloor,String roomName, String roomType,
																			String roomProperty,Pagination page) {
		List<DyDormRoom> roomList=new ArrayList<DyDormRoom>();
		Specification<DyDormRoom> specification = new Specification<DyDormRoom>() {
			@Nullable
			@Override
			public Predicate toPredicate(Root<DyDormRoom> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(criteriaBuilder.equal(root.get("unitId").as(String.class),unitId));
				if(StringUtils.isNotBlank(buildingId)){
					ps.add(criteriaBuilder.equal(root.get("buildingId").as(String.class),buildingId));
				}
				if(StringUtils.isNotBlank(roomFloor)){
					ps.add(criteriaBuilder.equal(root.get("floor").as(String.class),roomFloor));
				}
				if(StringUtils.isNotBlank(roomName)){
					ps.add(criteriaBuilder.equal(root.get("roomName").as(String.class),roomName));
				}
				if(StringUtils.isNotBlank(roomType)){
					ps.add(criteriaBuilder.equal(root.get("roomType").as(String.class), roomType));
				}
				if(StringUtils.isNotBlank(roomProperty)){
					ps.add(criteriaBuilder.equal(root.get("roomProperty").as(String.class), roomProperty));
				}
				List<Order> orderList = new ArrayList<>();
				orderList.add(criteriaBuilder.asc(root.get("roomName").as(String.class)));
				criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return criteriaQuery.getRestriction();
			}
		};
		if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<DyDormRoom> findAll = dyDormRoomDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			roomList=findAll.getContent();
		}
		else {
			roomList=dyDormRoomDao.findAll(specification);
		}
		return roomList;
	}
	public List<DyDormRoom> getDormRoomByRoomCon(DyDormConDto conDto,String[] roomIds,Pagination page) {
		List<DyDormRoom> roomList=new ArrayList<DyDormRoom>();
		Specification<DyDormRoom> specification = new Specification<DyDormRoom>() {
			@Nullable
			@Override
			public Predicate toPredicate(Root<DyDormRoom> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(criteriaBuilder.equal(root.get("unitId").as(String.class),conDto.getUnitId()));
				if (roomIds != null && roomIds.length > 0) {
                    queryIn("id", roomIds, root, ps, criteriaBuilder);
                }
				if(StringUtils.isNotBlank(conDto.getBuildingId())){
					ps.add(criteriaBuilder.equal(root.get("buildingId").as(String.class),conDto.getBuildingId()));
				}
				if(StringUtils.isNotBlank(conDto.getFloor())){
					ps.add(criteriaBuilder.equal(root.get("floor").as(String.class),conDto.getFloor()));
				}
				if(StringUtils.isNotBlank(conDto.getRoomName())){
					ps.add(criteriaBuilder.equal(root.get("roomName").as(String.class),conDto.getRoomName()));
				}
				if(StringUtils.isNotBlank(conDto.getRoomType())){
					ps.add(criteriaBuilder.equal(root.get("roomType").as(String.class), conDto.getRoomType()));
				}
				if(StringUtils.isNotBlank(conDto.getRoomProperty())){
					ps.add(criteriaBuilder.equal(root.get("roomProperty").as(String.class), conDto.getRoomProperty()));
				}
				List<Order> orderList = new ArrayList<>();
				orderList.add(criteriaBuilder.asc(root.get("roomName").as(String.class)));
				criteriaQuery.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return criteriaQuery.getRestriction();
			}
		};
		if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<DyDormRoom> findAll = dyDormRoomDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			roomList=findAll.getContent();
		}
		else {
			roomList=dyDormRoomDao.findAll(specification);
		}
		return roomList;
	}
	@Override
	public List<DyDormRoom> getDormRoomByUnitId( String unitId,String buildingId, String roomType, String acadyear, 
		String semester,String floor,String roomName,String roomState,String roomProperty,Pagination page) {
		
		DyDormConDto conDto=getCondto(unitId, buildingId, roomType, acadyear, semester, floor, roomName, roomState, roomProperty);
		Set<String> allRoomIds=new HashSet<>();
		List<DyDormRoom> roomList=null;
		if(StringUtils.isNotBlank(roomState) && !"1".equals(roomState)){//入住情况未传 或者全部的状态时不需要查询对应的寝室
			List<DyDormRoom> allRoomList=getDormRoomByRoomCon(conDto,null,null);
			if(CollectionUtils.isNotEmpty(allRoomList)){
				allRoomIds=getStateRoomIds(allRoomList, roomState, unitId, acadyear, semester);
				if(CollectionUtils.isNotEmpty(allRoomIds)){
					roomList=getDormRoomByRoomCon(conDto,allRoomIds.toArray(new String[]{}),page);
				}
			}
		}else{
			roomList=getDormRoomByRoomCon(conDto,null,page);
		}
		
		List<DyDormRoom> roomListLast=new ArrayList<DyDormRoom>();
		boolean isStu="1".equals(roomProperty);
		if(CollectionUtils.isNotEmpty(roomList)){
			Set<String> roomIds = new HashSet<String>();
			for(DyDormRoom room:roomList){
				roomIds.add(room.getId());
				roomListLast.add(room);
			}
			List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
			Map<String,DyDormBuilding> buildingMap=new HashMap<String, DyDormBuilding>();
			if(CollectionUtils.isNotEmpty(buildingList)){
				for(DyDormBuilding building:buildingList){
					buildingMap.put(building.getId(),building);
				}
			}
			List<DyDormBed> bedList = dyDormBedService.getDormBedsByUnitId(
					unitId, acadyear, semester, roomIds.toArray(new String[0]),null,roomProperty);
			//学生老师id
			Set<String> ownerIds = new HashSet<String>();
			Set<String> classIds = new HashSet<String>();
			for(DyDormRoom room:roomList){
				DyDormBuilding building=buildingMap.get(room.getBuildingId());
				room.setBuildName(building!=null?building.getName():"");
				List<DyDormBed> bedListForRoom=new ArrayList<DyDormBed>();
				for(DyDormBed bed:bedList){
					if(room.getId().equals(bed.getRoomId())){
						bedListForRoom.add(bed);
						if(StringUtils.isNotBlank(bed.getOwnerId())){
							ownerIds.add(bed.getOwnerId());
						}
						if(StringUtils.isNotEmpty(bed.getClassId())){
							classIds.add(bed.getClassId());
						}
					}
				}
				room.setBedList(bedListForRoom);
			}
			Map<String,Student> studentMap=new HashMap<String, Student>();
			Map<String,Teacher> teacherMap=new HashMap<String, Teacher>();
			Map<String,Clazz>   classMap=new HashMap<String, Clazz>();
			if(isStu){
				List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(ownerIds.toArray(new String[0])), new TR<List<Student>>(){});
				studentMap=studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
				List<Clazz> classList=SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
				classMap=classList.stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
			}else{
				List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(ownerIds.toArray(new String[0])), new TR<List<Teacher>>(){});
				teacherMap=teacherList.stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
			}
			for(DyDormRoom room:roomListLast){
				List<DyDormBed> sdbTempList = room.getBedList();
				List<DyDormBed> list = new ArrayList<DyDormBed>();
				for(int i=1;i<room.getCapacity()+1;i++){
					boolean flag=false;
					for(DyDormBed bed:sdbTempList){
						if(bed.getNo()==i){
							if(isStu){
								Student student = studentMap.get(bed.getOwnerId());
								if(student != null){
									bed.setOwnerName(student.getStudentName());
									bed.setOwnerCode(student.getStudentCode());
									bed.setClassId(student.getClassId());
									if(classMap.containsKey(student.getClassId())){
										bed.setClassName(classMap.get(student.getClassId()).getClassNameDynamic());
									}
								}
							}else{
								Teacher teacher=teacherMap.get(bed.getOwnerId());
								if(teacher != null){
									bed.setOwnerName(teacher.getTeacherName());
									bed.setOwnerCode(teacher.getTeacherCode());
//									bed.setClassId(teacher.getClassId());
								}
							}
							list.add(bed);
							flag = true;
							break;
						}
					}
					if(!flag){
						DyDormBed sdb = new DyDormBed();
						sdb.setUnitId(unitId);
						sdb.setAcadyear(acadyear);
						sdb.setSemester(semester);
						sdb.setNo(i);
						sdb.setRoomId(room.getId());
						list.add(sdb);
					}
				}
				room.setBedList(list);
			}
		}
		return getListOrderBy(roomListLast);
	}
	@Override
	public List<DyDormRoom> getListOrderBy(List<DyDormRoom> roomList){
		Collections.sort(roomList,new Comparator<DyDormRoom>(){
			@Override
			public int compare(DyDormRoom o1, DyDormRoom o2) {
				return o1.getBuildName().compareTo(o2.getBuildName());
			}
		});
		return roomList;
	}
	public List<DyDormRoom> getListByState(String unitId,String buildingId, String roomType, String acadyear,
										   String semester,String floor,String roomName,String roomState,String roomProperty,Pagination pagination){
		
		List<DyDormRoom> roomList = new ArrayList<>();
		if(StringUtils.isNotBlank(roomState)) {
			if (roomState.equals("4") || StringUtils.isBlank(roomState)) {
				roomList = getDormRoomByCon(unitId, buildingId, floor, roomName, roomType, roomProperty, pagination);
			} else {
				roomList = getDormRoomByCon(unitId, buildingId, floor, roomName, roomType, roomProperty, null);
			}
		}else {
			roomList = getDormRoomByCon(unitId, buildingId, floor, roomName, roomType, roomProperty, pagination);
		}

		List<DyDormRoom> roomList1=new ArrayList<DyDormRoom>();
		List<DyDormRoom> roomListLast=new ArrayList<DyDormRoom>();
		boolean isStu="1".equals(roomProperty);
		if(CollectionUtils.isNotEmpty(roomList)){
			Set<String> roomIds = new HashSet<String>();
			for(DyDormRoom room:roomList){
				roomIds.add(room.getId());
				roomListLast.add(room);
			}
			List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
			Map<String,DyDormBuilding> buildingMap=new HashMap<String, DyDormBuilding>();
			if(CollectionUtils.isNotEmpty(buildingList)){
				for(DyDormBuilding building:buildingList){
					buildingMap.put(building.getId(),building);
				}
			}
			List<DyDormBed> bedList = dyDormBedService.getDormBedsByUnitId(
					unitId, acadyear, semester, roomIds.toArray(new String[0]),null,roomProperty);
			//学生老师id
			Set<String> ownerIds = new HashSet<String>();
			Set<String> classIds = new HashSet<String>();
			for(DyDormRoom room:roomListLast){
				DyDormBuilding building=buildingMap.get(room.getBuildingId());
				room.setBuildName(building!=null?building.getName():"");
				List<DyDormBed> bedListForRoom=new ArrayList<DyDormBed>();
				for(DyDormBed bed:bedList){
					if(room.getId().equals(bed.getRoomId())){
						bedListForRoom.add(bed);
						if(StringUtils.isNotBlank(bed.getOwnerId())){
							ownerIds.add(bed.getOwnerId());
						}
						if(StringUtils.isNotEmpty(bed.getClassId())){
							classIds.add(bed.getClassId());
						}
					}
				}
				room.setBedList(bedListForRoom);
			}
			Map<String,Student> studentMap=new HashMap<String, Student>();
			Map<String,Teacher> teacherMap=new HashMap<String, Teacher>();
			Map<String,Clazz>   classMap=new HashMap<String, Clazz>();
			if(isStu){
				List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(ownerIds.toArray(new String[0])), new TR<List<Student>>(){});
				studentMap=studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
				List<Clazz> classList=SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
				classMap=classList.stream().collect(Collectors.toMap(Clazz::getId, Function.identity()));
			}else{
				List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(ownerIds.toArray(new String[0])), new TR<List<Teacher>>(){});
				teacherMap=teacherList.stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
			}
			for(DyDormRoom room:roomListLast){
				List<DyDormBed> sdbTempList = room.getBedList();
				List<DyDormBed> list = new ArrayList<DyDormBed>();
				for(int i=1;i<room.getCapacity()+1;i++){
					boolean flag=false;
					for(DyDormBed bed:sdbTempList){
						if(bed.getNo()==i){
							if(isStu){
								Student student = studentMap.get(bed.getOwnerId());
								if(student != null){
									bed.setOwnerName(student.getStudentName());
									bed.setOwnerCode(student.getStudentCode());
									bed.setClassId(student.getClassId());
									if(classMap.containsKey(student.getClassId())){
										bed.setClassName(classMap.get(student.getClassId()).getClassNameDynamic());
									}
								}
							}else{
								Teacher teacher=teacherMap.get(bed.getOwnerId());
								if(teacher != null){
									bed.setOwnerName(teacher.getTeacherName());
									bed.setOwnerCode(teacher.getTeacherCode());
//									bed.setClassId(teacher.getClassId());
								}
							}
							list.add(bed);
							flag = true;
							break;
						}
					}
					if(!flag){
						DyDormBed sdb = new DyDormBed();
						sdb.setUnitId(unitId);
						sdb.setAcadyear(acadyear);
						sdb.setSemester(semester);
						sdb.setNo(i);
						sdb.setRoomId(room.getId());
						list.add(sdb);
					}
				}
				room.setBedList(list);
			}
		}
		for(DyDormRoom dyDormRoom:roomListLast){
			List<DyDormBed> bedList = dyDormRoom.getBedList();
			if(roomState!=null) {
				if (roomState.equals("3")) {
					int alreadyArrangeNum=0;
					for(DyDormBed dyDormBed:bedList){
						if(StringUtils.isNotBlank(dyDormBed.getOwnerId())){
							alreadyArrangeNum = alreadyArrangeNum+1;
						}
					}
					if(alreadyArrangeNum>0&&alreadyArrangeNum==dyDormRoom.getCapacity()){
						roomList1.add(dyDormRoom);
					}
				} else if (roomState.equals("2")) {
					int alreadyArrangeNum=0;
					for(DyDormBed dyDormBed:bedList){
						if(StringUtils.isNotBlank(dyDormBed.getOwnerId())){
							alreadyArrangeNum = alreadyArrangeNum+1;
						}
					}
					if(alreadyArrangeNum>0&&alreadyArrangeNum<dyDormRoom.getCapacity()){
						roomList1.add(dyDormRoom);
					}
				} else if (roomState.equals("4")) {
					boolean hasArrange = false;
					for(DyDormBed dyDormBed:bedList){
						if(StringUtils.isNotBlank(dyDormBed.getOwnerId())){
							hasArrange = true;
						}
					}
					if(!hasArrange) {
						roomList1.add(dyDormRoom);
					}
				}else {
					roomList1.add(dyDormRoom);
				}
			}else {
				roomList1.add(dyDormRoom);
			}
		}
		return roomList1;
	}

	@Override
	public String doDyDormRoomImport(String unitId,String buildingId, String roomType,List<String[]> datas) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId,"");
		List<DyDormRoom> dyDormRooms = new ArrayList<>();
//		Map<String,String> buildingNameMap=EntityUtils.getMap(dyDormBuildingService.findByUnitId(unitId),"id", "name");
		Map<String,DyDormBuilding> buildingMap=EntityUtils.getMap(buildingList, "name");
		Map<String,DyDormBuilding> buildingIdMap =EntityUtils.getMap(buildingList, "id");
//		Map<String,DyDormRoom> roomMap=EntityUtils.getMap(roomList, "roomName");
		Map<String,DyDormRoom> tempMap = new HashMap<>();
		for(DyDormRoom dyDormRoom:roomList){
			if(buildingIdMap.containsKey(dyDormRoom.getBuildingId())){
				DyDormBuilding dyDormBuilding = buildingIdMap.get(dyDormRoom.getBuildingId());
				tempMap.put(dyDormBuilding.getName()+dyDormRoom.getRoomName(),dyDormRoom);
			}
		}
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			int length=dataArr.length;
			String buildingName=length>0?dataArr[0]:"";
			if(StringUtils.isNotBlank(buildingName)){
				buildingName=buildingName.trim();
			}else{
				buildingName="";
			}
			String[] errorData=null;//new String[4]
			if(buildingMap.get(buildingName)==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室楼";
				errorData[2]=buildingName;
				errorData[3]="寝室楼名称有误";
				errorDataList.add(errorData);
				continue;
			}
			String type=length>1?dataArr[1]:"";
			if(StringUtils.isNotBlank(type)){
				type=type.trim();
			}else{
				type="";
			}
			if((!type.equals("男寝室")&&!type.equals("女寝室"))||type.equals("")){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室类别";
				errorData[2]=type;
				errorData[3]="寝室类别有误";
				errorDataList.add(errorData);
				continue;
			}
			String proprety=length>1?dataArr[2]:"";
			if((!proprety.equals("学生寝室")&&!proprety.equals("老师寝室"))||proprety.equals("")){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室属性";
				errorData[2]=proprety;
				errorData[3]="寝室属性有误";
				errorDataList.add(errorData);
				continue;
			}
			String roomName=length>1?dataArr[3]:"";
			if(StringUtils.isNotBlank(roomName)){
				roomName=roomName.trim();
			}else{
				roomName="";
			}
			if(StringUtils.isNotBlank(roomName) && roomName.length()>20){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室名";
				errorData[2]=roomName;
				errorData[3]="寝室名不能超过20个字符";
				errorDataList.add(errorData);
				continue;
			}
			if(roomName.equals("")){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室名";
				errorData[2]=roomName;
				errorData[3]="寝室名不能为空";
				errorDataList.add(errorData);
				continue;
			}
			String capacity=length>1?dataArr[4]:"";
			try {
				int num = Integer.parseInt(capacity);
				if(num>20){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="容纳人数";
					errorData[2]=capacity;
					errorData[3]="容纳人数超出";
					errorDataList.add(errorData);
					continue;
				}
			} catch (NumberFormatException e) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="容纳人数";
				errorData[2]=capacity;
				errorData[3]="容纳人数有误";
				errorDataList.add(errorData);
				e.printStackTrace();
				continue;
			}
			String floor=length>1?dataArr[5]:"";
			try {
				int floorNum = Integer.parseInt(floor);
				if(floorNum>100||floorNum==0){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="楼层";
					errorData[2]=floor;
					errorData[3]="楼层不能为0或超出";
					errorDataList.add(errorData);
					continue;
				}

			} catch (NumberFormatException e) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="楼层";
				errorData[2]=floor;
				errorData[3]="格式有误";
				errorDataList.add(errorData);
				e.printStackTrace();
				continue;
			}
			if(tempMap.containsKey(buildingName+roomName)){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室名";
				errorData[2]=roomName;
				errorData[3]="同一寝室楼,不能有相同寝室名";
				errorDataList.add(errorData);
				continue;
			}
			DyDormRoom room = new DyDormRoom();
			room.setId(UuidUtils.generateUuid());
			room.setUnitId(unitId);
			if(type.equals("男寝室")) {
				room.setRoomType("1");
			}else if(type.equals("女寝室")){
				room.setRoomType("2");
			}

			if(proprety.equals("学生寝室")) {
				room.setRoomProperty("1");
			}else if(proprety.equals("老师寝室")){
				room.setRoomProperty("2");
			}
			room.setRoomName(roomName);
			DyDormBuilding dyDormBuilding = buildingMap.get(buildingName);
			room.setBuildingId(dyDormBuilding.getId());
			room.setCapacity(Integer.parseInt(capacity));
			room.setFloor(Integer.parseInt(floor));
			room.setUnitId(unitId);
			tempMap.put(buildingName+roomName,room);
			dyDormRooms.add(room);
		}
		if(CollectionUtils.isNotEmpty(dyDormRooms)){
			saveAll(dyDormRooms.toArray(new DyDormRoom[0]));
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", datas.size()-errorDataList.size());
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	/**
	 * 获取入住情况的寝室楼
	 * @param roomList
	 * @param roomState
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public Set<String> getStateRoomIds(List<DyDormRoom> roomList,String roomState,
			String unitId,String acadyear, String semester){
		Set<String> roomIds=roomList.stream().map(DyDormRoom::getId).collect(Collectors.toSet());
		List<DyDormBed> bedList=dyDormBedService.getDormBedsByRoomIds(unitId, roomIds.toArray(new String[]{}), acadyear, semester);
		Map<String,List<DyDormBed>> bedMap=new HashMap<>();//一个寝室 对应的已提交的床位(size为 空-容纳人数)
		if(CollectionUtils.isNotEmpty(bedList)){
			for(DyDormBed bed:bedList){
				if(!bedMap.containsKey(bed.getRoomId())){
					bedMap.put(bed.getRoomId(), new ArrayList<DyDormBed>());
				}
				bedMap.get(bed.getRoomId()).add(bed);
			}
		}
		roomIds=new HashSet<>();
		if("2".equals(roomState)){//未住满
			for(DyDormRoom room:roomList){
				String roomId=room.getId();
				if(bedMap.containsKey(roomId) && bedMap.get(roomId).size()<room.getCapacity()){
					roomIds.add(roomId);
				}
			}
		}else if("3".equals(roomState)){//已住满
			for(DyDormRoom room:roomList){
				String roomId=room.getId();
				if(bedMap.containsKey(roomId) && bedMap.get(roomId).size()==room.getCapacity()){
					roomIds.add(roomId);
				}
			}
		}else{//未入住
			for(DyDormRoom room:roomList){
				String roomId=room.getId();
				if(!bedMap.containsKey(roomId)){
					roomIds.add(roomId);
				}
			}
		}
		return roomIds;
	}
	/**
	 * 获取dto 目前暂时用
	 * @param unitId
	 * @param buildingId
	 * @param roomType
	 * @param acadyear
	 * @param semester
	 * @param floor
	 * @param roomName
	 * @param roomState
	 * @param roomProperty
	 * @return
	 */
	public DyDormConDto getCondto(String unitId,String buildingId, String roomType, String acadyear, 
			String semester,String floor,String roomName,String roomState,String roomProperty){
		DyDormConDto conDto=new DyDormConDto();
		conDto.setAcadyear(acadyear);
		conDto.setBuildingId(buildingId);
		conDto.setFloor(floor);
		conDto.setRoomName(roomName);
		conDto.setRoomProperty(roomProperty);
		conDto.setRoomState(roomState);
		conDto.setRoomType(roomType);
		conDto.setSemester(semester);
		conDto.setUnitId(unitId);
		return conDto;
	}
	public void deletedByBuildingId(String buildingId){
		dyDormRoomDao.deletedByBuildingId(buildingId);
	}
	@Override
	public void deletedById(String id) {
		dyDormRoomDao.deleteById(id);
	}
	@Override
	protected BaseJpaRepositoryDao<DyDormRoom, String> getJpaDao() {
		return dyDormRoomDao;
	}

	@Override
	protected Class<DyDormRoom> getEntityClass() {
		return DyDormRoom.class;
	}


}
