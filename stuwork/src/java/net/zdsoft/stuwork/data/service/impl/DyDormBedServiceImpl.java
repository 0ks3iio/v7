package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyDormBedDao;
import net.zdsoft.stuwork.data.entity.DyDormBed;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

@Service("dyDormBedService")
public class DyDormBedServiceImpl extends BaseServiceImpl<DyDormBed, String> implements DyDormBedService{
	@Autowired
	private DyDormBedDao dyDormBedDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyDormBuildingService dyDormBuildingService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	
	@Override
	public DyDormBed getbedByStudentId(String studentId,String unitId,String acadyear,String semesterStr){
		return dyDormBedDao.getbedByStudentId(studentId, unitId, acadyear, semesterStr);
	}
	@Override
	public String saveAllBed(List<DyDormBed> bedList,String roomProperty) {
		if(CollectionUtils.isEmpty(bedList)){
			return "没有需要保存的数据";
		}
		boolean isStu="1".equals(roomProperty);
		String unitId = bedList.get(0).getUnitId();
		String acadyear = bedList.get(0).getAcadyear();
		String semester = bedList.get(0).getSemester();
		Set<String> allIdsSet = new HashSet<String>();
		for(DyDormBed bed:bedList){
			if(StringUtils.isNotBlank(bed.getId())){
				allIdsSet.add(bed.getId());
			}
		}
		List<DyDormBed> everList = getDormBedsByUnitId(
				unitId, acadyear, semester, null,allIdsSet.toArray(new String[0]),roomProperty);//查出分页外的数据
		//得到所有的数据     为了判断学生是否重复
		everList.addAll(bedList);
		boolean flag = false;
		String ownerId = "";
		for(int i=0;i<everList.size();i++){
			for(int j=i+1;j<everList.size();j++){
				if(StringUtils.isNotEmpty(everList.get(i).getOwnerId())
					&& StringUtils.isNotEmpty(everList.get(j).getOwnerId())
					&& everList.get(i).getOwnerId().equals(everList.get(j).getOwnerId())){
					flag = true;
					ownerId = everList.get(i).getOwnerId();
					break;
				}
			}
			if(flag){
				break;
			}
		}
		if(isStu){
			if(flag){
				Student student = SUtils.dt(studentRemoteService.findOneById(ownerId),new TypeReference<Student>(){});
				return student.getStudentName()+" 学生数据重复";
			}
		}else{
			if(flag){
				Teacher teacher = SUtils.dt(teacherRemoteService.findOneById(ownerId),new TypeReference<Teacher>(){});
				return teacher.getTeacherName()+" 老师数据重复";
			}
		}
		//分别增加学期1 以及学期2的数据
		List<DyDormBed> bedInsertList = new ArrayList<DyDormBed>();
		//List<DyDormBed> bedInsertList1 = new ArrayList<DyDormBed>();
		//原先有的床位空余出来，清空原先的相关信息
		Set<String> deleteIdsSet = new HashSet<String>();
		for(DyDormBed sdb:bedList){
			if(StringUtils.isNotBlank(sdb.getId())){
				deleteIdsSet.add(sdb.getId());
			}
			if(StringUtils.isNotBlank(sdb.getOwnerId())){
				sdb.setId(UuidUtils.generateUuid());
				sdb.setCreationTime(new Date());
				sdb.setOwnerType(roomProperty);//值相同
				bedInsertList.add(sdb);
			}
		}
		if(deleteIdsSet!=null && deleteIdsSet.size() > 0){
			List<DyDormBed> delList = new ArrayList<DyDormBed>();
			delList = findListByIdIn(deleteIdsSet.toArray(new String[0]));
			Set<String> dels = new HashSet<String>();
			for(DyDormBed e : delList){
				dels.add(e.getOwnerId());//通过学生老师id 以及学年等删除
			}
			dyDormBedDao.deletedByUAStuIds(unitId, acadyear,semester, dels.toArray(new String[0]));
		}
		//添加数据操作
		if(CollectionUtils.isNotEmpty(bedInsertList)){
			saveAll(bedInsertList.toArray(new DyDormBed[0]));
			/*for(DyDormBed bed:bedInsertList){
				bed.setId(UuidUtils.generateUuid());
				if(semester.equals("1")){
					bed.setSemester("2");
				}else{
					bed.setSemester("1");
				}
				bedInsertList1.add(bed);
			}
			saveAll(bedInsertList1.toArray(new DyDormBed[0]));*/
		}
		return "success";
	}
	@Override
	public List<DyDormBed> getDormBedsByRoomIds(String unitId,String[] roomIds,String acadyear,String semesterStr){
		if(roomIds!=null && roomIds.length>0){
			List<DyDormBed> bedList=new ArrayList<>();
			int cyc = roomIds.length / 1000 + (roomIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > roomIds.length)
					max = roomIds.length;
				String[] roomId = ArrayUtils.subarray(roomIds, i * 1000, max);
				if(StringUtils.isNotBlank(acadyear) && StringUtils.isNotBlank(semesterStr)){
					bedList.addAll(dyDormBedDao.getDormBedsByRoomIds(unitId, acadyear,semesterStr,roomId));
				}else
					bedList.addAll(dyDormBedDao.getDormBedsByRoomIds(unitId,roomId));
			}
			return bedList;
		}
		return new ArrayList<DyDormBed>();
	}
	@Override
	public List<DyDormBed> findStudentByRoomIds(String unitId, String acadyear,
			String semester, String[] roomIds) {
		return dyDormBedDao.findStudentByRoomIds(unitId, acadyear,semester,roomIds);
	}
	@Override
	public List<DyDormBed> getDormBedsByProCon(String unitId,String acadyear,String semesterStr,String roomProperty){
		if(StringUtils.isEmpty(semesterStr)){
			if(StringUtils.isBlank(roomProperty)){
				return dyDormBedDao.getDormBedsByProCon(unitId, acadyear);
			}
			return dyDormBedDao.getDormBedsByProCon(unitId, acadyear,roomProperty);
		}
		if(StringUtils.isBlank(roomProperty)){
			return dyDormBedDao.getDormBedsByCon(unitId,acadyear,semesterStr);
		}
		return dyDormBedDao.getDormBedsByCon(unitId,acadyear,semesterStr,roomProperty);
	}
	@Override
	public List<DyDormBed> getDormBedsByCon(String classId,String unitId,String acadyear,String semesterStr,String roomProperty){
		if(StringUtils.isBlank(roomProperty)){
			return dyDormBedDao.getDormBedsByClaCon(classId,unitId,acadyear,semesterStr);
		}
		return dyDormBedDao.getDormBedsByClaCon(classId,unitId,acadyear,semesterStr,roomProperty);
	}
	@Override
	public List<DyDormBed> getDormBedsByUnitId(final String unitId, final String acadyear,
			final String semester, final String[] roomIds,final String[] ids,final String roomProperty) {
		Specification<DyDormBed> specification=new Specification<DyDormBed>() {
			@Override
			public Predicate toPredicate(Root<DyDormBed> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				
				if (roomIds != null && roomIds.length > 0) {
                    queryIn("roomId", roomIds, root, ps, cb);
                }
				if (ids != null && ids.length > 0) {
					queryNotIn("id", ids, root, ps,cb);
				}
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				if(StringUtils.isNotBlank(acadyear)){
					ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				}
				if(StringUtils.isNotBlank(semester)){
					ps.add(cb.equal(root.get("semester").as(String.class), semester));
				}
				if(StringUtils.isNotBlank(roomProperty)){
					ps.add(cb.equal(root.get("ownerType").as(String.class), roomProperty));
				}
//                List<Order> orderList = new ArrayList<Order>();
//                orderList.add(cb.asc(root.get("acadyear").as(String.class)));
                cq.where(ps.toArray(new Predicate[0]));//.orderBy(orderList);
	                return cq.getRestriction();
			}
		};
		return dyDormBedDao.findAll(specification);
	}
	@Override
	public void deletedByUARoomId(String unitId,String acadyear,String semester,String roomId,String roomProperty){
		if(StringUtils.isNotBlank(roomId)){
			dyDormBedDao.deletedByUARoomId(unitId, acadyear,semester, roomId);	
		}else{
			if("1".equals(roomProperty)){
				dyDormBedDao.deletedStuByUA(unitId, acadyear,semester);//空的ownerType 也是学生 老数据
			}else{
				dyDormBedDao.deletedTeachByUA(unitId, acadyear,semester);
			}
		}
	}
	@Override
	public List<DyDormBed> findByClassIds(String unitId, String acadyear,
			String semester, String[] classIds) {
		return dyDormBedDao.findByClassIds(unitId, acadyear,semester,classIds);
	}
	public String deTeachBedImprot(String unitId, String acadyear,String semester,String roomProperty, List<String[]> datas){
		//key - teacherCode
		Map<String,Teacher> teacherMap = null; 
		
		List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>(){});
		teacherMap=EntityUtils.getMap(teacherList, "teacherCode"); 
		
		//  学生信息导入数据处理
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		
		List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
		List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByProUnitId(unitId,roomProperty);
		//key - name
		Map<String,DyDormBuilding> buildingMap=EntityUtils.getMap(buildingList, "name"); 
		//key - buildingId+"_"+roomName
		Map<String,DyDormRoom> roomMap=new HashMap<>(); 
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				roomMap.put(room.getBuildingId()+"_"+room.getRoomName(), room);
			}
		}
		//取出该学期下的老数据，更新是需要先删除老数据
		List<DyDormBed> bedList=getDormBedsByProCon(unitId, acadyear,semester,roomProperty);//2
		Map<String,DyDormBed> strbedMap=new HashMap<String, DyDormBed>();
		for(DyDormBed bed:bedList){
			strbedMap.put(bed.getRoomId()+"_"+bed.getNo(), bed);
		}
		
		List<DyDormBed> inserList=new ArrayList<DyDormBed>();
		List<DyDormBed> pickList=new ArrayList<DyDormBed>();
		//List<DyDormBed> pickList1=new ArrayList<DyDormBed>();
		
		Set<String> deleteIds = new HashSet<String>();
		Set<String> otherIds = new HashSet<String>();
		//String[] ses=new String[]{"1","2"};
		Map<String,String> testMap=new HashMap<String, String>();//判断room的床位是否重复
		Map<String,String> testStuMap=new HashMap<String, String>();//判断学生老师是否重复
		DyDormBed bed = null;
		DyDormBuilding building=null;
		String[] errorData=null;//new String[4]
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			int length=dataArr.length;
			String buildingName=length>0?dataArr[0]:"";
			if(StringUtils.isNotBlank(buildingName)){
				buildingName=buildingName.trim();
			}else{
				buildingName="";
			}
			building=buildingMap.get(buildingName);
			if(building==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室楼";
				errorData[2]=buildingName;
				errorData[3]="寝室楼名称有误";
				errorDataList.add(errorData);
				continue;
			}
			String roomName=length>1?dataArr[1]:"";
			if(StringUtils.isNotBlank(roomName)){
				roomName=roomName.trim();
			}else{
				roomName="";
			}
			DyDormRoom room=roomMap.get(building.getId()+"_"+roomName);
			if(room==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="寝室号";
				errorData[2]=roomName;
				errorData[3]="寝室号有误或不在对应的寝室楼下";
				errorDataList.add(errorData);
				continue;
			}
			if(length<3 ||StringUtils.isBlank(dataArr[2])){
				errorData = new String[4];
				errorData[0]=errorDataList.size() +1 +"";
				errorData[1]="床位号";
				errorData[2]="";
				errorData[3]="床位号不能为空";
				errorDataList.add(errorData);
				continue;
			}
			int no = NumberUtils.toInt(dataArr[2]);
			if(no<1 || no>room.getCapacity()){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="床位号";
				errorData[2]=dataArr[2];
				errorData[3]="床位号不在有效范围内，有效范围为1~"+room.getCapacity();
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(testMap.get(room.getId()+no))){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="床位号";
				errorData[2]=roomName+" "+dataArr[2];
				errorData[3]="该寝室下此床位的数据重复";
				errorDataList.add(errorData);
				continue;
			}
			testMap.put(room.getId()+no, "one");
			
			String teacherCode=length>3?dataArr[3]:"";
			if(StringUtils.isNotBlank(teacherCode)){
				teacherCode=teacherCode.trim();
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="教师号";
				errorData[2]=teacherCode;
				errorData[3]="教师号不能为空";
				errorDataList.add(errorData);
				continue;
			}
			Teacher	teacher=teacherMap.get(teacherCode);
			if(teacher==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="教师号";
				errorData[2]=teacherCode;
				errorData[3]="教师号有误";
				errorDataList.add(errorData);
				continue;
			}
			String teacherName=length>4?dataArr[4]:"";
			if(StringUtils.isNotBlank(teacherName)){
				teacherName=teacherName.trim();
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=teacherName;
				errorData[3]="教师姓名不能为空";
				errorDataList.add(errorData);
			}
			if(!teacher.getTeacherName().equals(teacherName)){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=teacherName;
				errorData[3]="教师姓名有误，或该姓名和教师号不匹配";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(testStuMap.get(teacher.getId()))){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=teacherName;
				errorData[3]="教师重复：导入数据有重复教师";
				errorDataList.add(errorData);
				continue;
			}
			testStuMap.put(teacher.getId(),"one");
			
			String remark=length>5?dataArr[5]:"";
			if(StringUtils.isNotBlank(remark) && remark.length()>100){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="备注";
				errorData[2]=remark;
				errorData[3]="备注不能超过100个字符";
				errorDataList.add(errorData);
				continue;
			}
			bed=new DyDormBed();
			bed.setId(UuidUtils.generateUuid());
			bed.setUnitId(unitId);
			bed.setAcadyear(acadyear);
			bed.setSemester(semester);
			bed.setOwnerId(teacher.getId());
			bed.setOwnerType(StuworkConstants.TEACH_TYPE);
			bed.setOwnerName(teacherName);
			bed.setRoomId(room.getId());
			bed.setNo(no);
			bed.setRemark(remark);
			bed.setDormState(0);
//			bed.setClassId(clazz.getId());
			bed.setCreationTime(new Date());
			inserList.add(bed);
			
			DyDormBed oldBed=strbedMap.get(room.getId()+"_"+no);
			if(oldBed!=null){
				deleteIds.add(oldBed.getOwnerId());//通过教师id去删除
				otherIds.add(oldBed.getId());//得到除这些数据之外的id
			}
			successCount++;
		}
		List<DyDormBed> everList = getDormBedsByUnitId(
				unitId, acadyear, semester, null,otherIds.toArray(new String[0]),roomProperty);//查出需要导入数据外的数据
		//以studentId 作为主键。判断 即将导入的数据 与其它寝室床位的比较是否重复
		Map<String,DyDormBed> ownerIdHaveMap=EntityUtils.getMap(everList, "ownerId");
		for(DyDormBed inbed:inserList){
			String ownerId=inbed.getOwnerId();
			if(ownerIdHaveMap.get(ownerId)!=null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=inbed.getOwnerName();
				errorData[3]="教师重复：此教师已安排在其它寝室的床位";
				errorDataList.add(errorData);
				continue;
			}
			pickList.add(inbed);
		}
		if(CollectionUtils.isNotEmpty(deleteIds)){
			List<DyDormBed> bedList1=dyDormBedDao.getDormBedsByStuIds(unitId, acadyear,semester, deleteIds.toArray(new String[0]));
			deleteAll(bedList1.toArray(new DyDormBed[0]));
		}
		if(CollectionUtils.isNotEmpty(pickList)){
			saveAll(pickList.toArray(new DyDormBed[0]));
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", datas.size()-errorDataList.size());
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	@Override
	public String doBedImport(String unitId, String acadyear,String semester,String roomProperty, List<String[]> datas){
			//  学生信息导入数据处理
			Json importResultJson=new Json();
			List<String[]> errorDataList=new ArrayList<String[]>();
			int successCount  =0;
			if("2".equals(roomProperty)){
				return deTeachBedImprot(unitId, acadyear, semester, roomProperty, datas);
			}
			
			//key - studentCode
			Map<String,Student> studentMap = null; 
			//key - className
			Map<String,Clazz> clazzMap = null; 
			List<Clazz> clazzlist = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId,acadyear), new TR<List<Clazz>>(){});
			List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolIdIn(null,new String[]{unitId}), new TR<List<Student>>(){});
			studentMap = EntityUtils.getMap(studentList, "studentCode"); 
			clazzMap = EntityUtils.getMap(clazzlist, "classNameDynamic"); 
			
			List<DyDormBuilding> buildingList=dyDormBuildingService.findByUnitId(unitId);
			List<DyDormRoom> roomList=dyDormRoomService.getDormRoomByUnitId(unitId,roomProperty);
			//key - name
			Map<String,DyDormBuilding> buildingMap=EntityUtils.getMap(buildingList, "name"); 
			//key - buildingId+"_"+roomName
			Map<String,DyDormRoom> roomMap=new HashMap<>(); 
			if(CollectionUtils.isNotEmpty(roomList)){
				for(DyDormRoom room:roomList){
					roomMap.put(room.getBuildingId()+"_"+room.getRoomName(), room);
				}
			}
			//取出该学期下的老数据，更新是需要先删除老数据
			List<DyDormBed> bedList=getDormBedsByProCon(unitId, acadyear,semester,"1");
			Map<String,DyDormBed> strbedMap=new HashMap<String, DyDormBed>();
			for(DyDormBed bed:bedList){
				strbedMap.put(bed.getRoomId()+"_"+bed.getNo(), bed);
			}
			
			List<DyDormBed> inserList=new ArrayList<DyDormBed>();
			List<DyDormBed> pickList=new ArrayList<DyDormBed>();
			//List<DyDormBed> pickList1=new ArrayList<DyDormBed>();
			
			Set<String> deleteIds = new HashSet<String>();
			Set<String> otherIds = new HashSet<String>();
			//String[] ses=new String[]{"1","2"};
			Map<String,String> testMap=new HashMap<String, String>();//判断room的床位是否重复
			Map<String,String> testStuMap=new HashMap<String, String>();//判断学生老师是否重复
			DyDormBed bed = null;
			DyDormBuilding building = null;
			String[] errorData=null;//new String[4]
			for(int i =0;i< datas.size();i++){
				String[] dataArr = datas.get(i);
				int length=dataArr.length;
				String buildingName=length>0?dataArr[0]:"";
				if(StringUtils.isNotBlank(buildingName)){
					buildingName=buildingName.trim();
				}else{
					buildingName="";
				}
				building=buildingMap.get(buildingName);
				if(building==null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="寝室楼";
					errorData[2]=buildingName;
					errorData[3]="寝室楼名称有误";
					errorDataList.add(errorData);
					continue;
				}
				String roomName=length>1?dataArr[1]:"";
				if(StringUtils.isNotBlank(roomName)){
					roomName=roomName.trim();
				}else{
					roomName="";
				}
				DyDormRoom room=roomMap.get(building.getId()+"_"+roomName);
				if(room==null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="寝室号";
					errorData[2]=roomName;
					errorData[3]="寝室号有误或不在对应的寝室楼下";
					errorDataList.add(errorData);
					continue;
				}
				if(length<3 ||StringUtils.isBlank(dataArr[2])){
					errorData = new String[4];
					errorData[0]=errorDataList.size() +1 +"";
					errorData[1]="床位号";
					errorData[2]="";
					errorData[3]="床位号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				int no = NumberUtils.toInt(dataArr[2]);
				if(no<1 || no>room.getCapacity()){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="床位号";
					errorData[2]=dataArr[2];
					errorData[3]="床位号不在有效范围内，有效范围为1~"+room.getCapacity();
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isNotBlank(testMap.get(room.getId()+no))){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="床位号";
					errorData[2]=roomName+" "+dataArr[2];
					errorData[3]="该寝室下此床位的数据重复";
					errorDataList.add(errorData);
					continue;
				}
				testMap.put(room.getId()+no, "one");
				
				String className=length>3?dataArr[3]:"";
				if(StringUtils.isNotBlank(className)){
					className=className.trim();
				}else{
					className="";
				}
				if(StringUtils.isBlank(className)){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="班级";
					errorData[2]=className;
					errorData[3]="班级不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Clazz clazz=clazzMap.get(className);
				if(clazz==null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="班级";
					errorData[2]=className;
					errorData[3]="班级名称有误";
					errorDataList.add(errorData);
					continue;
				}
				String studentCode=length>4?dataArr[4]:"";
				if(StringUtils.isNotBlank(studentCode)){
					studentCode=studentCode.trim();
				}else{
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]=studentCode;
					errorData[3]="学号不能为空";
					errorDataList.add(errorData);
					continue;
				}
				Student	stu=studentMap.get(studentCode);
				if(stu==null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学号";
					errorData[2]=studentCode;
					errorData[3]="学号有误";
					errorDataList.add(errorData);
					continue;
				}
				String studentName=length>5?dataArr[5]:"";
				if(StringUtils.isNotBlank(studentName)){
					studentName=studentName.trim();
				}else{
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="姓名";
					errorData[2]=studentName;
					errorData[3]="学生姓名不能为空";
					errorDataList.add(errorData);
					continue;
				}
				if(!stu.getStudentName().equals(studentName)){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="姓名";
					errorData[2]=studentName;
					errorData[3]="学生姓名有误，或该姓名和学号不匹配";
					errorDataList.add(errorData);
					continue;
				}
				if(!stu.getClassId().equals(clazz.getId())){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="姓名";
					errorData[2]=studentName;
					errorData[3]="学生姓名和班级不匹配";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isNotBlank(testStuMap.get(stu.getId()))){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="姓名";
					errorData[2]=studentName;
					errorData[3]="学生重复：导入数据有重复学生";
					errorDataList.add(errorData);
					continue;
				}
				testStuMap.put(stu.getId(),"one");
				
				String remark=length>6?dataArr[6]:"";
				if(StringUtils.isNotBlank(remark) && remark.length()>100){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="备注";
					errorData[2]=remark;
					errorData[3]="备注不能超过100个字符";
					errorDataList.add(errorData);
					continue;
				}
				//for(String se:ses){
					bed=new DyDormBed();
					bed.setId(UuidUtils.generateUuid());
					bed.setUnitId(unitId);
					bed.setAcadyear(acadyear);
					//bed.setSemester(se);
					bed.setSemester(semester);
					bed.setOwnerId(stu.getId());
					bed.setOwnerName(studentName);
					bed.setOwnerType(StuworkConstants.STU_TYPE);
					bed.setRoomId(room.getId());
					bed.setNo(no);
					bed.setRemark(remark);
					bed.setDormState(0);
					bed.setClassId(clazz.getId());
					bed.setCreationTime(new Date());
					inserList.add(bed);
					//if(se.equals(semester)){
						//pickList.add(bed);
					//}
				//}
				DyDormBed oldBed=strbedMap.get(room.getId()+"_"+no);
				if(oldBed!=null){
					deleteIds.add(oldBed.getOwnerId());//通过学生id去删除
					otherIds.add(oldBed.getId());//得到除这些数据之外的id
				}
				/*if(CollectionUtils.isNotEmpty(bedList)){
					for(DyDormBed oldBed:bedList){
						if(oldBed.getRoomId().equals(room.getId())&& oldBed.getNo()==no){
							deleteIds.add(oldBed.getStudentId());//通过学生id去删除
							otherIds.add(oldBed.getId());//得到除这些数据之外的id
						}
					}
				}*/
				successCount++;
			}
			List<DyDormBed> everList = getDormBedsByUnitId(
					unitId, acadyear, semester, null,otherIds.toArray(new String[0]),roomProperty);//查出需要导入数据外的数据
			//以studentId 作为主键。判断 即将导入的数据 与其它寝室床位的比较是否重复
			Map<String,DyDormBed> studentIdHaveMap=EntityUtils.getMap(everList, "ownerId");
			for(DyDormBed inbed:inserList){
				String studentId=inbed.getOwnerId();
				if(studentIdHaveMap.get(studentId)!=null){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="姓名";
					errorData[2]=inbed.getOwnerName();
					errorData[3]="学生重复：此学生已安排在其它寝室的床位";
					errorDataList.add(errorData);
					//deleteIds.add(studentId);
					continue;
				}
				pickList.add(inbed);
			}
			//insertListAll.addAll(pickList);
			/*//得到所有的数据     为了判断学生是否重复
			everList.addAll(pickList);
			boolean flag = false;
			String studentId = "";
			for(int i=0;i<everList.size();i++){
				for(int j=i+1;j<everList.size();j++){
					if(everList.get(i).getStudentId().equals(everList.get(j).getStudentId())){
						flag = true;
						studentId = everList.get(i).getStudentId();
						break;
					}
				}
				if(flag){
					break;
				}
			}
			Student student = SUtils.dt(studentRemoteService.findById(studentId),new TypeReference<Student>(){});
			if(flag){
				List<String[]> errorDataList1=new ArrayList<String[]>();
				errorData = new String[4];
				errorData[0]=1+"";
				errorData[1]="姓名";
				errorData[2]=student.getStudentName();
				errorData[3]="学生数据重复 无法导入数据";
				errorDataList1.add(errorData);
				importResultJson.put("totalCount", datas.size());
				importResultJson.put("successCount", 0);
				importResultJson.put("errorCount", datas.size());
				importResultJson.put("errorData", errorDataList1);
				return importResultJson.toJSONString();
			}else{*/
			if(CollectionUtils.isNotEmpty(deleteIds)){
				List<DyDormBed> bedList1=dyDormBedDao.getDormBedsByStuIds(unitId, acadyear,semester, deleteIds.toArray(new String[0]));
				deleteAll(bedList1.toArray(new DyDormBed[0]));
				//dyDormBedDao.deletedByUAStuIds(unitId, acadyear, deleteIds.toArray(new String[0]));
			}
			if(CollectionUtils.isNotEmpty(pickList)){
				saveAll(pickList.toArray(new DyDormBed[0]));
				/*for(DyDormBed inBed:pickList){
					inBed.setId(UuidUtils.generateUuid());
					if(semester.equals("1")){
						inBed.setSemester("2");
					}else{
						inBed.setSemester("1");
					}
					pickList1.add(inBed);
				}
				saveAll(pickList1.toArray(new DyDormBed[0]));*/
			}
		/*	DyDormBed[] beds=insertListAll.toArray(new DyDormBed[0]);
			checkSave(beds);
			saveAll(beds);*/
			//}
			importResultJson.put("totalCount", datas.size());
			importResultJson.put("successCount", datas.size()-errorDataList.size());
			importResultJson.put("errorCount", errorDataList.size());
			importResultJson.put("errorData", errorDataList);
			return importResultJson.toJSONString();
	}
	
	@Override
	public void deletedById(String id) {
		dyDormBedDao.deleteById(id);
	}
	@Override
	protected BaseJpaRepositoryDao<DyDormBed, String> getJpaDao() {
		return dyDormBedDao;
	}
	@Override
	protected Class<DyDormBed> getEntityClass() {
		return DyDormBed.class;
	}
	@Override
	public List<DyDormBed> findDyDormBedByUnitId(String unitId,
			String acadyear, String semester, String[] studentIds) {
		return dyDormBedDao.findDyDormBedByUnitId(unitId, acadyear, semester, studentIds);
	}


}

