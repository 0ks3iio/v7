package net.zdsoft.activity.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.activity.dao.FamilyDearRegisterDao;
import net.zdsoft.activity.dto.FamilyActureDto;
import net.zdsoft.activity.dto.FamilyDearAuditDto;
import net.zdsoft.activity.dto.FamilyStaDto;
import net.zdsoft.activity.entity.FamilyDearRegister;
import net.zdsoft.activity.entity.OfficeTeacherLeave;
import net.zdsoft.activity.entity.TeacherEx;
import net.zdsoft.activity.service.FamilyDearRegisterService;
import net.zdsoft.activity.service.TeacherExRemoteService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.entity.*;
import net.zdsoft.familydear.service.FamDearArrangeService;
import net.zdsoft.familydear.service.FamdearActualReportService;
import net.zdsoft.familydear.service.FamilyDearObjectService;
import net.zdsoft.familydear.service.FamilyDearServantService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import static net.zdsoft.framework.utils.ExportUtils.outputData;

@Service("FamilyDearRegisterService")
public class FamilyDearRegisterServiceImpl extends BaseServiceImpl<FamilyDearRegister,String> implements FamilyDearRegisterService{
	
	@Autowired
	private FamilyDearRegisterDao familyDearRegisterDao;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private FamDearArrangeService famDearArrangeService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private FamilyDearServantService familyDearServantService;
	@Autowired
	private FamilyDearObjectService familyDearObjectService;
	@Autowired
	private TeacherExRemoteService teacherExRemoteService;
	@Autowired
	private FamdearActualReportService famdearActualReportService;

	
	private static OpenApiOfficeService openApiOfficeService;
    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }

	@Override
	public List<FamilyDearRegister> getFamilyDearRegisterList(String unitId,
			String teacherId, String[] arrangeIds) {
		return familyDearRegisterDao.getFamilyDearRegisterList(unitId, teacherId, arrangeIds);
	}

	@Override
	public List<FamilyDearRegister> getFamilyDearRegisterListByArrangeId(String[] arrangeIds) {
		return familyDearRegisterDao.getFamilyDearRegisterListByArrangeId(arrangeIds);
	}

	@Override
	public List<FamilyDearRegister> getFamilyDearRegisterList(String unitId,
			String teacherId) {
		return familyDearRegisterDao.getFamilyDearRegisterList(unitId, teacherId);
	}

	@Override
	public void deleteByIds(String[] arrangeIds) {
		familyDearRegisterDao.deleteByIds(arrangeIds);
	}

	@Override
	public List<FamilyDearRegister> getFamilyDearReByArrangeIdAndActivityIdAndUnitId(
			String unitId, String activityId, String arrangeId) {
		return familyDearRegisterDao.getFamilyDearReByArrangeIdAndActivityIdAndUnitId(unitId, activityId, arrangeId);
	}

	@Override
	public List<FamilyDearRegister> findBy(String unitId, int status,
			String activityId, String[] arrangeIds,String[] teaIds,String teacherName,String contryName, Pagination page) {
		Specification<FamilyDearRegister> specification=new Specification<FamilyDearRegister>() {
			@Override
			public Predicate toPredicate(Root<FamilyDearRegister> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.notEqual(root.get("status").as(Integer.class), status));
				ps.add(cb.equal(root.get("activityId").as(String.class), activityId));
				if(StringUtils.isNotBlank(contryName)){
					if(ArrayUtils.isNotEmpty(arrangeIds)){
						ps.add(root.<String>get("arrangeId").in(arrangeIds));
					}else{
						ps.add(root.<String>get("arrangeId").in(new String[]{"1"}));
					}
				}
				if(StringUtils.isNotBlank(teacherName)){
					if(ArrayUtils.isNotEmpty(teaIds)){
						ps.add(root.<String>get("teacherId").in(teaIds));
					}else{
						ps.add(root.<String>get("teacherId").in(new String[]{"1"}));
					}
				}
				List<Order> orderList = new ArrayList<Order>();
		        orderList.add(cb.desc(root.get("teacherId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	            return cq.getRestriction();
			}
		};
		List<FamilyDearRegister> list=new ArrayList<FamilyDearRegister>();
		
		if(page!=null){
			list=findAll(specification, page);
		}else{
			list=findAll(specification);
		}
		dealSetThing(list);
		return list;
	}

	@Override
	public List<FamilyDearAuditDto> findByParams(String unitId, int status, String activityId, String[] arrangeIds, String[] teaIds, String teacherName, String contryName, Pagination page) {
		List<FamilyDearObject> familyDearObjects = familyDearObjectService.findByVillageName(contryName);
		Set<String> teacherIds = new HashSet<>();
		if(CollectionUtils.isNotEmpty(familyDearObjects)){
			Set<String> objIds = familyDearObjects.stream().map(FamilyDearObject::getId).collect(Collectors.toSet());
			List<FamilyDearServant> list = familyDearServantService.getListByObjIds(objIds.toArray(new String[0]), FamDearConstant.HAVE_RELATION);
			Set<String> ids = list.stream().map(FamilyDearServant::getTeacherId).collect(Collectors.toSet());
			//取交集
			if(CollectionUtils.isNotEmpty(ids)&&ArrayUtils.isNotEmpty(teaIds)){
				teacherIds.clear();
				teacherIds.addAll(new HashSet<String>(Arrays.asList(teaIds)));
				teacherIds.retainAll(ids);
			}else if(StringUtils.isEmpty(teacherName)&&CollectionUtils.isNotEmpty(ids)) {
				teacherIds.addAll(ids);
			}else {
				teacherIds.addAll(new HashSet<>(Arrays.asList(teaIds)));
			}
		}

    	List<FamilyDearAuditDto> familyDearAuditDtos = new ArrayList<>();
		Specification<FamilyDearRegister> specification=new Specification<FamilyDearRegister>() {
			@Override
			public Predicate toPredicate(Root<FamilyDearRegister> root, CriteriaQuery<?> cq,
										 CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.notEqual(root.get("status").as(Integer.class), status));
				ps.add(cb.equal(root.get("activityId").as(String.class), activityId));
//				if(StringUtils.isNotBlank(contryName)){
//					if(ArrayUtils.isNotEmpty(arrangeIds)){
//						ps.add(root.<String>get("arrangeId").in(arrangeIds));
//					}else{
//						ps.add(root.<String>get("arrangeId").in(new String[]{"1"}));
//					}
//				}
				if(StringUtils.isNotBlank(teacherName)||StringUtils.isNotBlank(contryName)){
					if(ArrayUtils.isNotEmpty(teacherIds.toArray(new String[0]))){
						ps.add(root.<String>get("teacherId").in(teacherIds.toArray(new String[0])));
					}else{
						ps.add(root.<String>get("teacherId").in(new String[]{"1"}));
					}
				}
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(root.get("teacherId").as(String.class)));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}
		};
		List<FamilyDearRegister> list=new ArrayList<FamilyDearRegister>();

		if(page!=null){
			list=findAll(specification, page);
		}else{
			list=findAll(specification);
		}
		dealSetThing(list,familyDearAuditDtos);
		return familyDearAuditDtos;
	}

	@Override
	public List<FamilyDearRegister> getFamilyDearReByActivityIdsAndStatusAndUnitId(
			String unitId, int status, String activityId,String[] arrangeIds,String contryName) {
		List<FamilyDearRegister> familyDearRegisters=new ArrayList<FamilyDearRegister>();
		List<FamilyDearObject> familyDearObjects = familyDearObjectService.findByVillageName(contryName);
		Set<String> teacherIds = new HashSet<>();
		if(CollectionUtils.isNotEmpty(familyDearObjects)){
			Set<String> objIds = familyDearObjects.stream().map(FamilyDearObject::getId).collect(Collectors.toSet());
			List<FamilyDearServant> list = familyDearServantService.getListByObjIds(objIds.toArray(new String[0]), FamDearConstant.HAVE_RELATION);
			Set<String> ids = list.stream().map(FamilyDearServant::getTeacherId).collect(Collectors.toSet());
			if(CollectionUtils.isNotEmpty(ids)){
				teacherIds.addAll(ids);
			}
		}
		Specification<FamilyDearRegister> specification=new Specification<FamilyDearRegister>() {
			@Override
			public Predicate toPredicate(Root<FamilyDearRegister> root, CriteriaQuery<?> cq,
					CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.equal(root.get("status").as(Integer.class), status));
				ps.add(cb.equal(root.get("activityId").as(String.class), activityId));
//				if(StringUtils.isNotBlank(contryName)){
//					if(ArrayUtils.isNotEmpty(arrangeIds)){
//						ps.add(root.<String>get("arrangeId").in(arrangeIds));
//					}else{
//						ps.add(root.<String>get("arrangeId").in(new String[]{"1"}));
//					}
//				}
				if(StringUtils.isNotBlank(contryName)) {
					if (ArrayUtils.isNotEmpty(teacherIds.toArray(new String[0]))) {
						ps.add(root.<String>get("teacherId").in(teacherIds.toArray(new String[0])));
					} else {
						ps.add(root.<String>get("teacherId").in(new String[]{"1"}));
					}
				}
				List<Order> orderList = new ArrayList<Order>();
		        orderList.add(cb.desc(root.get("teacherId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	            return cq.getRestriction();
			}
		};
		familyDearRegisters=findAll(specification);
		dealSetThing(familyDearRegisters);
		return familyDearRegisters;
	}
	
	@Override
	public List<FamilyDearRegister> getListByUnitIdAndTeacherIds(String unitId,
			String deptId,String year,Date startTime,Date endTime, String[] activityIds, Pagination page) {
		List<FamilyDearRegister> familyDearRegisterAlls=familyDearRegisterDao.getListByUnitIdAndTeacherIds(unitId,year, activityIds, page);
		List<FamilyDearRegister> familyDearRegisters=new ArrayList<FamilyDearRegister>();
		List<FamilyDearRegister> familyDearRegisterDes=new ArrayList<FamilyDearRegister>();
		List<FamilyDearRegister> familyDearRegisterDess=new ArrayList<FamilyDearRegister>();
		if(CollectionUtils.isNotEmpty(familyDearRegisterAlls)){
			if(StringUtils.isNotBlank(deptId)){
				List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
				Set<String> teacherIds = EntityUtils.getSet(teachers, e->e.getId());
				for (FamilyDearRegister familyDearRegister : familyDearRegisterAlls) {
					if(CollectionUtils.isNotEmpty(teacherIds)&&teacherIds.contains(familyDearRegister.getTeacherId())){
						familyDearRegisters.add(familyDearRegister);
					}
				}
			}else{
				familyDearRegisters.addAll(familyDearRegisterAlls);
			}
			JSONObject remoteMsgParam = new JSONObject();
			remoteMsgParam.put("unitId", unitId);
			remoteMsgParam.put("year", year);
			if(getOpenApiOfficeService() != null){
				List<OfficeTeacherLeave> officeTeacherLeaves=new ArrayList<OfficeTeacherLeave>();
				String remos=getOpenApiOfficeService().getOfficeTeacherLeave(remoteMsgParam.toJSONString());
//				String remos="[{\"leaveBeignTime\":1560528000000,\"applyUserId\":\"37676FAD572C4548AD13DF0E6EF6E9D6\",\"leaveEndTime\":1560614400000},{\"leaveBeignTime\":1560182400000,\"applyUserId\":\"37676FAD572C4548AD13DF0E6EF6E9D6\",\"leaveEndTime\":1560268800000}]";
				if(StringUtils.isNotBlank(remos)){
					officeTeacherLeaves=JSON.parseArray(remos, OfficeTeacherLeave.class);
					System.out.println("请假条数"+officeTeacherLeaves.size());
				}
				if(CollectionUtils.isNotEmpty(officeTeacherLeaves)){
					Map<String,List<OfficeTeacherLeave>> leaveMap=new HashMap<String,List<OfficeTeacherLeave>>();
					for (OfficeTeacherLeave officeTeacherLeave : officeTeacherLeaves) {
						if(leaveMap.containsKey(officeTeacherLeave.getApplyUserId())){
							List<OfficeTeacherLeave> office=leaveMap.get(officeTeacherLeave.getApplyUserId());
							office.add(officeTeacherLeave);
							leaveMap.put(officeTeacherLeave.getApplyUserId(), office);
						}else{
							List<OfficeTeacherLeave> office=new ArrayList<>();
							office.add(officeTeacherLeave);
							leaveMap.put(officeTeacherLeave.getApplyUserId(), office);
						}
					}
					List<FamDearArrange> famDearArranges=famDearArrangeService.getFamilyDearArrangeList(activityIds);
					Map<String,FamDearArrange> famMap=EntityUtils.getMap(famDearArranges, e->e.getId());
					for (FamilyDearRegister familyDearRegister : familyDearRegisters) {
						familyDearRegisterDess.add(familyDearRegister);
						if(!leaveMap.containsKey(familyDearRegister.getTeaUserId())){
							familyDearRegisterDess.add(familyDearRegister);
							continue;
						}
						List<OfficeTeacherLeave> office=leaveMap.get(familyDearRegister.getTeaUserId()); 
						if(MapUtils.isNotEmpty(famMap)&&famMap.containsKey(familyDearRegister.getArrangeId())){
							OfficeTeacherLeave officeTeacherLeaveDes=null;
							FamDearArrange famDearArrange=famMap.get(familyDearRegister.getArrangeId());
							boolean canIsert=false;
							out:for (OfficeTeacherLeave officeTeacherLeave : office) {
								if(!((famDearArrange.getEndTime().compareTo(officeTeacherLeave.getLeaveBeignTime())<0)||(famDearArrange.getStartTime().compareTo(officeTeacherLeave.getLeaveEndTime())>0))){
									canIsert=true;
									officeTeacherLeaveDes=officeTeacherLeave;
									break out;
								}
							}
							if(canIsert){
								if(officeTeacherLeaveDes!=null){
									familyDearRegister.setRemarkNew(DateUtils.date2String(officeTeacherLeaveDes.getLeaveBeignTime(), "yyyy.MM.dd")+"至"+DateUtils.date2String(officeTeacherLeaveDes.getLeaveEndTime(), "yyyy.MM.dd")+"请假。");
								}
							}
						}
					}
				}else{
					familyDearRegisterDess=familyDearRegisters; 
				}
			}else{
				familyDearRegisterDess=familyDearRegisters;
			}
		}
		List<FamilyDearRegister> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(familyDearRegisterDess)){
			Set<String> teaIdSet = new HashSet<String>();
			teaIdSet=EntityUtils.getSet(familyDearRegisterDess, e->e.getTeacherId());
			List<FamilyDearServant> familyDearServants=familyDearServantService.getListByTeacherIds(teaIdSet.toArray(new String[0]));
			Map<String,Set<String>> desMap=new HashMap<String,Set<String>>();
			for (FamilyDearServant familyDearServant : familyDearServants) {
				if(!desMap.containsKey(familyDearServant.getTeacherId())){
					Set<String> objSet=new HashSet<String>();
					objSet.add(familyDearServant.getObjectId());
					desMap.put(familyDearServant.getTeacherId(), objSet);
				}else{
					Set<String> objSet=desMap.get(familyDearServant.getTeacherId());
					objSet.add(familyDearServant.getObjectId());
					desMap.put(familyDearServant.getTeacherId(), objSet);
				}
			}
			Set<String> objSet=EntityUtils.getSet(familyDearServants, e->e.getObjectId());
			List<FamilyDearObject> familyDearObjects=familyDearObjectService.findListByIdsWithMaster(objSet.toArray(new String[0]));
			Map<String,FamilyDearObject> faMap=EntityUtils.getMap(familyDearObjects, e->e.getId());
			Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeIds(new String[]{"DM-JQLB"}),
	                new TypeReference<Map<String, Map<String, McodeDetail>>>() {
	                });
			Map<String, McodeDetail> detailMap = mcodeMap.get("DM-JQLB");
			Map<String, McodeDetail> nationMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
			Set<String> arrangeIds=EntityUtils.getSet(familyDearRegisterDess, e->e.getArrangeId());
			List<FamdearActualReport> famdearActualReports=famdearActualReportService.getListByUnitIdAndOthers(unitId, year,startTime,endTime, arrangeIds.toArray(new String[0]),FamDearConstant.PLAN_PUBLISH);
			Map<String,List<FamdearActualReport>> reportMap=new HashMap<String,List<FamdearActualReport>>();
			if(CollectionUtils.isNotEmpty(famdearActualReports)){
				for (FamdearActualReport famdearActualReport : famdearActualReports) {
					if(reportMap.containsKey(famdearActualReport.getCreateUserId()+"_"+famdearActualReport.getArrangeId())){
						List<FamdearActualReport> res=reportMap.get(famdearActualReport.getCreateUserId()+"_"+famdearActualReport.getArrangeId());
						res.add(famdearActualReport);
						reportMap.put(famdearActualReport.getCreateUserId()+"_"+famdearActualReport.getArrangeId(), res);
					}else{
						List<FamdearActualReport> res=new ArrayList<FamdearActualReport>();
						res.add(famdearActualReport);
						reportMap.put(famdearActualReport.getCreateUserId()+"_"+famdearActualReport.getArrangeId(), res);
					}
				}
			}
			for (FamilyDearRegister familyDearRegister : familyDearRegisterDess) {
				if(MapUtils.isNotEmpty(reportMap)&&reportMap.containsKey(familyDearRegister.getTeaUserId()+"_"+familyDearRegister.getArrangeId())) {
					List<FamdearActualReport> res = reportMap.get(familyDearRegister.getTeaUserId() + "_" + familyDearRegister.getArrangeId());
					//list.add(familyDearRegister);
					StringBuffer timeSb = new StringBuffer();
					StringBuffer timeSb1 = new StringBuffer();
					StringBuffer remarkSb = new StringBuffer();
					if (StringUtils.isNotBlank(familyDearRegister.getRemarkNew())) {
						remarkSb.append(familyDearRegister.getRemarkNew());
					}
					int i = 0;
					int j = 0;
					if (CollectionUtils.isNotEmpty(res)) {
						for (FamdearActualReport famdearActualReport : res) {
							if (i == 0) {
								timeSb.append(DateUtils.date2String(famdearActualReport.getArriveTime(), "yyyy.MM.dd"));
								timeSb1.append(DateUtils.date2String(famdearActualReport.getBackTime(), "yyyy.MM.dd"));
							} else {
								if(!timeSb.toString().contains(DateUtils.date2String(famdearActualReport.getArriveTime(), "yyyy.MM.dd"))) {
									timeSb.append("，" + DateUtils.date2String(famdearActualReport.getArriveTime(), "yyyy.MM.dd"));
								}
								if(!timeSb1.toString().contains(DateUtils.date2String(famdearActualReport.getBackTime(), "yyyy.MM.dd"))) {
									timeSb1.append("，" + DateUtils.date2String(famdearActualReport.getBackTime(), "yyyy.MM.dd"));
								}
							}
							i++;
							if (j == 0) {
								if (StringUtils.isNotBlank(remarkSb)) {
									if (StringUtils.isNotBlank(famdearActualReport.getMark())) {
										remarkSb.append(famdearActualReport.getMark());
										j++;
									}
								} else {
									if (StringUtils.isNotBlank(famdearActualReport.getMark())) {
										remarkSb.append(famdearActualReport.getMark());
										j++;
									}
								}
							} else {

								if (StringUtils.isNotBlank(famdearActualReport.getMark())) {
									remarkSb.append("，" + famdearActualReport.getMark());
									j++;
								}
							}
						}
						familyDearRegister.setArriveTimeStr(timeSb.toString());
						familyDearRegister.setReturnTimeStr(timeSb1.toString());
						familyDearRegister.setRemarkNew(remarkSb.toString());
						familyDearRegister.setUpload("是");
					}
				}
				if(CollectionUtils.isNotEmpty(reportMap.get(familyDearRegister.getTeaUserId()+"_"+familyDearRegister.getArrangeId()))) {
					if (MapUtils.isNotEmpty(desMap) && desMap.containsKey(familyDearRegister.getTeacherId())) {
						Set<String> objIdSet = desMap.get(familyDearRegister.getTeacherId());
						for (String objId : objIdSet) {
							FamilyDearRegister newFa = new FamilyDearRegister();
							try {
								newFa = (FamilyDearRegister) BeanUtils.cloneBean(familyDearRegister);
								if (MapUtils.isNotEmpty(faMap) && faMap.containsKey(objId)) {
									FamilyDearObject familyDearObject = faMap.get(objId);
									newFa.setObjectName(familyDearObject.getName());
									if (StringUtils.isNotBlank(familyDearObject.getNation())) {
										newFa.setObjectNation(nationMap.get(familyDearObject.getNation()).getMcodeContent());
									}
									newFa.setObjectCard(familyDearObject.getIdentityCard());
									newFa.setObjectDizhi(familyDearObject.getHomeAddress());
									String type = familyDearObject.getType();
									StringBuilder builder = new StringBuilder();

									if (StringUtils.isNotEmpty(type)) {
										String[] types = type.split(",");
										for (String str : types) {
											McodeDetail detail = detailMap.get(str);
											if (detail != null) {
												builder.append(detail.getMcodeContent() + ",");
											}
										}
										if (builder.length() >= 1) {
											newFa.setObjectType(builder.substring(0, builder.length() - 1));
										}

									}
									newFa.setObjectPhone(familyDearObject.getMobilePhone());
									newFa.setContry(familyDearObject.getVillage());
									list.add(newFa);
								}
							} catch (IllegalAccessException
									| InstantiationException
									| InvocationTargetException
									| NoSuchMethodException e1) {
								e1.printStackTrace();
							}
						}
					} else {
						list.add(familyDearRegister);
					}
				}

			}
		}else{
			list=familyDearRegisterDess;
		}
		dealSetThing(list);
		return list;
	}

	@Override
	public FamilyStaDto getListByUnitAndDeptId(FamilyStaDto familyStaDto,List<FamdearActualReport> famdearActualReports,String[] userSet) {
		if(CollectionUtils.isNotEmpty(famdearActualReports)){
			List<User> users =userRemoteService.findListObjectByIds(userSet);
			Map<String,User> userMap=EntityUtils.getMap(users, e->e.getId());
			Set<String> teaIds=EntityUtils.getSet(users, e->e.getOwnerId());
			List<TeacherEx> teacherExList =teacherExRemoteService.findListByIds(teaIds.toArray(new String[0]));
			Map<String,TeacherEx> teaMap=EntityUtils.getMap(teacherExList, e->e.getId());
			Map<String,Integer> tjCountMap=new HashMap<String,Integer>();
			Map<String,Integer> xjCountMap=new HashMap<String,Integer>();
			Map<String,Integer> kjCountMap=new HashMap<String,Integer>();
			Map<String,Integer> normalCountMap=new HashMap<String,Integer>();
			Map<String,Integer> jisCountMap=new HashMap<String,Integer>();
			Map<String,Integer> gqCountMap=new HashMap<String,Integer>();
			Map<String,Integer> qyeCountMap=new HashMap<String,Integer>();
			Map<String,Integer> otherCountMap=new HashMap<String,Integer>();
			int j=0;
			for (FamdearActualReport famdearActualReport : famdearActualReports) {
				if(userMap.containsKey(famdearActualReport.getCreateUserId())){
					User user=userMap.get(famdearActualReport.getCreateUserId());
					if(user!=null&&teaMap.containsKey(user.getOwnerId())){
						TeacherEx teacherEx=teaMap.get(user.getOwnerId());
						if(teacherEx!=null&&StringUtils.isNotBlank(teacherEx.getCadreType())){
							j++;
							if(StringUtils.equals(teacherEx.getCadreType(), "01")){
								if(tjCountMap.containsKey("01")){
									Integer i=tjCountMap.get("01");
									i=i+1;
									tjCountMap.put("01", i);
								}else{
									tjCountMap.put("01", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "02")){
								if(xjCountMap.containsKey("02")){
									Integer i=xjCountMap.get("02");
									i=i+1;
									xjCountMap.put("02", i);
								}else{
									xjCountMap.put("02", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "03")){
								if(kjCountMap.containsKey("03")){
									Integer i=kjCountMap.get("03");
									i=i+1;
									kjCountMap.put("03", i);
								}else{
									kjCountMap.put("03", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "04")){
								if(normalCountMap.containsKey("04")){
									Integer i=normalCountMap.get("04");
									i=i+1;
									normalCountMap.put("04", i);
								}else{
									normalCountMap.put("04", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "05")){
								if(jisCountMap.containsKey("05")){
									Integer i=jisCountMap.get("05");
									i=i+1;
									jisCountMap.put("05", i);
								}else{
									jisCountMap.put("05", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "06")){
								if(gqCountMap.containsKey("06")){
									Integer i=gqCountMap.get("06");
									i=i+1;
									gqCountMap.put("06", i);
								}else{
									gqCountMap.put("06", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "07")){
								if(qyeCountMap.containsKey("07")){
									Integer i=qyeCountMap.get("07");
									i=i+1;
									qyeCountMap.put("07", i);
								}else{
									qyeCountMap.put("07", 1);
								}
							}else if(StringUtils.equals(teacherEx.getCadreType(), "08")){
								if(otherCountMap.containsKey("08")){
									Integer i=otherCountMap.get("08");
									i=i+1;
									otherCountMap.put("08", i);
								}else{
									otherCountMap.put("08", 1);
								}
							}
						}
					}
				}
			}
			if(MapUtils.isNotEmpty(tjCountMap)){
				familyStaDto.setTjCount(tjCountMap.get("01"));
			}
			if(MapUtils.isNotEmpty(xjCountMap)){
				familyStaDto.setXjCount(xjCountMap.get("02"));
			}
			if(MapUtils.isNotEmpty(kjCountMap)){
				familyStaDto.setKjCount(kjCountMap.get("03"));
			}
			if(MapUtils.isNotEmpty(normalCountMap)){
				familyStaDto.setNormalCount(normalCountMap.get("04"));
			}
			if(MapUtils.isNotEmpty(jisCountMap)){
				familyStaDto.setJisCount(jisCountMap.get("05"));
			}
			if(MapUtils.isNotEmpty(gqCountMap)){
				familyStaDto.setGqCount(gqCountMap.get("06"));
			}
			if(MapUtils.isNotEmpty(qyeCountMap)){
				familyStaDto.setQyeCount(qyeCountMap.get("07"));
			}
			if(MapUtils.isNotEmpty(otherCountMap)){
				familyStaDto.setOtherCount(otherCountMap.get("08"));
			}
			familyStaDto.setTotalCount(j);
		}
		return familyStaDto;
	}

	@Override
	public FamilyActureDto getListByUnitAndDeptId(
			FamilyActureDto familyActureDto, String unitId, String deptId,
			String[] activityIds) {
		List<FamilyDearRegister> familyDearRegisterAlls=familyDearRegisterDao.getListByUnitIdAndTeacherIds(unitId,null, activityIds,null);
		List<FamilyDearRegister> familyDearRegisterDes=new ArrayList<FamilyDearRegister>();
		
		return familyActureDto;
	}

	private void dealSetThing(List<FamilyDearRegister> familyDearRegisters,List<FamilyDearAuditDto> familyDearAuditDtos){
		if(CollectionUtils.isNotEmpty(familyDearRegisters)){
			Set<String> teaIdSet = new HashSet<String>();
			Set<String> deptIdSet = new HashSet<String>();
			Set<String> userIdSet = new HashSet<String>();
			Set<String> actIdSet = new HashSet<String>();
			Set<String> arrangeIdSet = new HashSet<String>();
			for (FamilyDearRegister familyDearRegister : familyDearRegisters) {
				teaIdSet.add(familyDearRegister.getTeacherId());
				userIdSet.add(familyDearRegister.getTeaUserId());
				userIdSet.add(familyDearRegister.getAuditUserId());
				actIdSet.add(familyDearRegister.getActivityId());
				arrangeIdSet.add(familyDearRegister.getArrangeId());
			}
			List<FamDearArrange> famDearArranges=famDearArrangeService.findListByIds(arrangeIdSet.toArray(new String[0]));
			Map<String, McodeDetail> sexMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
			Map<String, McodeDetail> nationMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
			if(CollectionUtils.isNotEmpty(teaIdSet)){
				Map<String, McodeDetail> cadreTypeMap =SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-FQGBLB"),new TypeReference<Map<String,McodeDetail>>(){});//走访干部类别
				List<FamilyDearServant> familyDearServants=familyDearServantService.getListByTeacherIds(teaIdSet.toArray(new String[0]));
				Map<String,Set<String>> desMap=new HashMap<String,Set<String>>();
				for (FamilyDearServant familyDearServant : familyDearServants) {
					if(!desMap.containsKey(familyDearServant.getTeacherId())){
						Set<String> objSet=new HashSet<String>();
						objSet.add(familyDearServant.getObjectId());
						desMap.put(familyDearServant.getTeacherId(), objSet);
					}else{
						Set<String> objSet=desMap.get(familyDearServant.getTeacherId());
						objSet.add(familyDearServant.getObjectId());
						desMap.put(familyDearServant.getTeacherId(), objSet);
					}
				}
				Set<String> objSet=EntityUtils.getSet(familyDearServants, e->e.getObjectId());
				List<FamilyDearObject> familyDearObjects=familyDearObjectService.findListByIdsWithMaster(objSet.toArray(new String[0]));
				Map<String,List<FamilyDearObject>> stringListMap= new HashMap<>();
				for(String teacherId:teaIdSet){
					List<FamilyDearObject> list = new ArrayList<>();
					for(FamilyDearObject familyDearObject:familyDearObjects){
						if(familyDearObject.getTeacherId().equals(teacherId)){
							list.add(familyDearObject);
						}
					}
					stringListMap.put(teacherId,list);
				}
				Map<String,FamilyDearObject> faMap=EntityUtils.getMap(familyDearObjects, e->e.getId());
				List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>(){});
				List<TeacherEx> teacherExList =teacherExRemoteService.findListByIds(teaIdSet.toArray(new String[0]));
				List<User> userList=SUtils.dt(userRemoteService.findListByIds(userIdSet.toArray(new String[0])), new TR<List<User>>(){});
				Map<String,TeacherEx> teacherExMap=EntityUtils.getMap(teacherExList, e->e.getId());
				Map<String, User> userMap = EntityUtils.getMap(userList, e->e.getId());
				Map<String, String> teacherNameMap = new HashMap<String, String>();
				Map<String, String> teacherPhoneMap = new HashMap<String, String>();
				Map<String, Teacher> teacherMap = new HashMap<String, Teacher>();
				Map<String, String> deptNameMap = new HashMap<String, String>();
				Map<String, Integer> teacherSexMap = new HashMap<String, Integer>();
				Map<String,FamDearArrange> arrangeMap=new HashMap<String,FamDearArrange>();
				for(Teacher teacher : teacherList){
					teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
					teacherPhoneMap.put(teacher.getId(),teacher.getMobilePhone());
					teacherSexMap.put(teacher.getId(), teacher.getSex());
					deptIdSet.add(teacher.getDeptId());
					teacherMap.put(teacher.getId(), teacher);
				}
				List<Dept> deptList=SUtils.dt(deptRemoteService.findListByIds(deptIdSet.toArray(new String[0])), new TR<List<Dept>>(){});
				for (Dept dept : deptList) {
					deptNameMap.put(dept.getId(), dept.getDeptName());
				}
				for (FamDearArrange famDearArrange : famDearArranges) {
					arrangeMap.put(famDearArrange.getId(), famDearArrange);
				}
				for (FamilyDearRegister familyDearRegister : familyDearRegisters) {
					FamilyDearAuditDto familyDearAuditDto = new FamilyDearAuditDto();
					familyDearAuditDto.setId(familyDearRegister.getId());
					familyDearAuditDto.setTeacherName(teacherNameMap.get(familyDearRegister.getTeacherId()));
					familyDearAuditDto.setTeacherPhone(teacherPhoneMap.get(familyDearRegister.getTeacherId()));
					Teacher teacher=teacherMap.get(familyDearRegister.getTeacherId());
					familyDearAuditDto.setFamilyDearObjectList(stringListMap.get(familyDearRegister.getTeacherId()));
					if(deptNameMap.containsKey(teacher.getDeptId())){
						familyDearAuditDto.setDeptName(deptNameMap.get(teacher.getDeptId()));
					}
					if(MapUtils.isNotEmpty(arrangeMap)&&arrangeMap.containsKey(familyDearRegister.getArrangeId())){
						familyDearAuditDto.setBatchType(arrangeMap.get(familyDearRegister.getArrangeId()).getBatchType());
						familyDearAuditDto.setContrys(arrangeMap.get(familyDearRegister.getArrangeId()).getRural());
						familyDearAuditDto.setContrysSub(arrangeMap.get(familyDearRegister.getArrangeId()).getRural());
					}
					familyDearAuditDto.setRemark(familyDearRegister.getRemark());
					familyDearAuditDto.setState(familyDearRegister.getStatus());
					familyDearAuditDto.setApplyTime(familyDearRegister.getApplyTime());
					if(StringUtils.isNotBlank(familyDearAuditDto.getContrysSub())) {
						if (familyDearAuditDto.getContrysSub().length() > 20) {
							familyDearAuditDto.setContrysSub(familyDearAuditDto.getContrysSub().substring(0, 20) + "....");
						}
					}
					familyDearAuditDtos.add(familyDearAuditDto);
				}
			}
		}
	}

	private void dealSetThing(List<FamilyDearRegister> familyDearRegisters){
		if(CollectionUtils.isNotEmpty(familyDearRegisters)){
			Set<String> teaIdSet = new HashSet<String>();
			Set<String> deptIdSet = new HashSet<String>();
			Set<String> userIdSet = new HashSet<String>();
			Set<String> actIdSet = new HashSet<String>();
			Set<String> arrangeIdSet = new HashSet<String>();
			for (FamilyDearRegister familyDearRegister : familyDearRegisters) {
				teaIdSet.add(familyDearRegister.getTeacherId());
				userIdSet.add(familyDearRegister.getTeaUserId());
				userIdSet.add(familyDearRegister.getAuditUserId());
				actIdSet.add(familyDearRegister.getActivityId());
				arrangeIdSet.add(familyDearRegister.getArrangeId());
			}
			List<FamDearArrange> famDearArranges=famDearArrangeService.findListByIds(arrangeIdSet.toArray(new String[0]));
			Map<String, McodeDetail> sexMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
			Map<String, McodeDetail> nationMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
			if(CollectionUtils.isNotEmpty(teaIdSet)){
				Map<String, McodeDetail> cadreTypeMap =SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-FQGBLB"),new TypeReference<Map<String,McodeDetail>>(){});//走访干部类别
				List<FamilyDearServant> familyDearServants=familyDearServantService.getListByTeacherIds(teaIdSet.toArray(new String[0]));
				Map<String,Set<String>> desMap=new HashMap<String,Set<String>>();
				for (FamilyDearServant familyDearServant : familyDearServants) {
					if(!desMap.containsKey(familyDearServant.getTeacherId())){
						Set<String> objSet=new HashSet<String>();
						objSet.add(familyDearServant.getObjectId());
						desMap.put(familyDearServant.getTeacherId(), objSet);
					}else{
						Set<String> objSet=desMap.get(familyDearServant.getTeacherId());
						objSet.add(familyDearServant.getObjectId());
						desMap.put(familyDearServant.getTeacherId(), objSet);
					}
				}
				Set<String> objSet=EntityUtils.getSet(familyDearServants, e->e.getObjectId());
				List<FamilyDearObject> familyDearObjects=familyDearObjectService.findListByIdsWithMaster(objSet.toArray(new String[0]));
				Map<String,FamilyDearObject> faMap=EntityUtils.getMap(familyDearObjects, e->e.getId());
				List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>(){});
				List<TeacherEx> teacherExList =teacherExRemoteService.findListByIds(teaIdSet.toArray(new String[0]));
				List<User> userList=SUtils.dt(userRemoteService.findListByIds(userIdSet.toArray(new String[0])), new TR<List<User>>(){});
				Map<String,TeacherEx> teacherExMap=EntityUtils.getMap(teacherExList, e->e.getId());
				Map<String, User> userMap = EntityUtils.getMap(userList, e->e.getId());
				Map<String, String> teacherNameMap = new HashMap<String, String>();
				Map<String, String> teacherPhoneMap = new HashMap<String, String>();
				Map<String, Teacher> teacherMap = new HashMap<String, Teacher>();
				Map<String, String> deptNameMap = new HashMap<String, String>();
			    Map<String, Integer> teacherSexMap = new HashMap<String, Integer>();
			    Map<String,FamDearArrange> arrangeMap=new HashMap<String,FamDearArrange>();
				for(Teacher teacher : teacherList){
					teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
					teacherPhoneMap.put(teacher.getId(),teacher.getMobilePhone());
					teacherSexMap.put(teacher.getId(), teacher.getSex());
					deptIdSet.add(teacher.getDeptId());
					teacherMap.put(teacher.getId(), teacher);
			    }
				List<Dept> deptList=SUtils.dt(deptRemoteService.findListByIds(deptIdSet.toArray(new String[0])), new TR<List<Dept>>(){});
				for (Dept dept : deptList) {
					deptNameMap.put(dept.getId(), dept.getDeptName());
				}
				for (FamDearArrange famDearArrange : famDearArranges) {
					arrangeMap.put(famDearArrange.getId(), famDearArrange);
				}
				for (FamilyDearRegister familyDearRegister : familyDearRegisters) {
					if(MapUtils.isNotEmpty(desMap)&&desMap.containsKey(familyDearRegister.getTeacherId())){
						Set<String> desSet=desMap.get(familyDearRegister.getTeacherId());
						Set<String> nameSet=new HashSet<>();
						for (String objId : desSet) {
							if(MapUtils.isNotEmpty(faMap)&&faMap.containsKey(objId)){
								nameSet.add(faMap.get(objId).getVillage());
							}
						}
						StringBuffer sb=new StringBuffer();
						int i=0;
						if(CollectionUtils.isNotEmpty(nameSet)){
							for (String string : nameSet) {
								if(i==0){
									sb.append(string);
								}else{
									sb.append("，"+string);
								}
								i++;
							}
						}
						familyDearRegister.setContry(sb.toString());
						familyDearRegister.setContrySub(sb.toString());
						if(StringUtils.isNotBlank(familyDearRegister.getContrySub())) {
							if (familyDearRegister.getContrySub().length() > 20) {
								familyDearRegister.setContrySub(familyDearRegister.getContrySub().substring(0, 20) + "...");
							}
						}
					}
					if(StringUtils.isNotBlank(familyDearRegister.getAuditUserId())&&userMap.containsKey(familyDearRegister.getAuditUserId())){
						familyDearRegister.setAuditUserName(userMap.get(familyDearRegister.getAuditUserId()).getRealName());
					}
					familyDearRegister.setTeacherName(teacherNameMap.get(familyDearRegister.getTeacherId()));
					familyDearRegister.setTeacherPhone(teacherPhoneMap.get(familyDearRegister.getTeacherId()));
					familyDearRegister.setSex(sexMap.get(String.valueOf(teacherSexMap.get(familyDearRegister.getTeacherId()))).getMcodeContent());
					Teacher teacher=teacherMap.get(familyDearRegister.getTeacherId());
					TeacherEx teacherEx=null;
					if(MapUtils.isNotEmpty(teacherExMap)){
						teacherEx=teacherExMap.get(familyDearRegister.getTeacherId());
					}
					if(teacherEx!=null&&StringUtils.isNotBlank(teacherEx.getCadreType())){
						familyDearRegister.setCadreType(cadreTypeMap.get(teacherEx.getCadreType()).getMcodeContent());
					}
					if(StringUtils.isNotBlank(teacher.getNation())){
						familyDearRegister.setNationName(nationMap.get(teacher.getNation()).getMcodeContent());
					}
					familyDearRegister.setPhone(teacher.getMobilePhone());
					if(deptNameMap.containsKey(teacher.getDeptId())){
						familyDearRegister.setDeptName(deptNameMap.get(teacher.getDeptId()));
					}
					if(MapUtils.isNotEmpty(arrangeMap)&&arrangeMap.containsKey(familyDearRegister.getArrangeId())){
						familyDearRegister.setBatchType(arrangeMap.get(familyDearRegister.getArrangeId()).getBatchType());
						familyDearRegister.setContrys(arrangeMap.get(familyDearRegister.getArrangeId()).getRural());
						familyDearRegister.setContrysSub(arrangeMap.get(familyDearRegister.getArrangeId()).getRural());
					}
					if(StringUtils.isNotBlank(familyDearRegister.getContrysSub())) {
						if (familyDearRegister.getContrysSub().length() > 20) {
							familyDearRegister.setContrysSub(familyDearRegister.getContrysSub().substring(0, 20) + "....");
						}
					}
				}
			}
		}
	}

	@Override
	protected BaseJpaRepositoryDao<FamilyDearRegister, String> getJpaDao() {
		return familyDearRegisterDao;
	}

	@Override
	protected Class<FamilyDearRegister> getEntityClass() {
		return FamilyDearRegister.class;
	}



}
