package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.dao.GkGroupClassDao;
import net.zdsoft.gkelective.data.dto.GkArrangeGroupResultDto;
import net.zdsoft.gkelective.data.dto.GkClassResultDto;
import net.zdsoft.gkelective.data.dto.GkGroupDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkGroupClassStuService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service("gkGroupClassService")
public class GkGroupClassServiceImpl extends BaseServiceImpl<GkGroupClass, String> implements GkGroupClassService{

	@Autowired
	private GkGroupClassDao gkGroupClassDao;
	@Autowired
	private GkBatchService gkBatchService;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private TeacherRemoteService teacherService;
	@Autowired
	private GkGroupClassStuService gkGroupClassStuService;
	
	@Override
	protected BaseJpaRepositoryDao<GkGroupClass, String> getJpaDao() {
		return gkGroupClassDao;
	}

	@Override
	protected Class<GkGroupClass> getEntityClass() {
		return GkGroupClass.class;
	}
	
	public List<GkArrangeGroupResultDto> findGroupResultsByRoundsId(String roundsId){
		List<GkArrangeGroupResultDto> dtolist = new ArrayList<GkArrangeGroupResultDto>();
		List<GkGroupClass> gkclasslist = this.findByRoundsId(roundsId);
		if(CollectionUtils.isEmpty(gkclasslist)){
			return dtolist;
		}
		Set<String> gids = EntityUtils.getSet(gkclasslist, "id");
		List<GkBatch> batchs = gkBatchService.findBatchListByGroupClassId(roundsId, gids.toArray(new String[0]));
		Set<String> teachClassIds = new HashSet<String>();
		Map<String,Set<String>> groupMap = new HashMap<String, Set<String>>();
		Map<String,GkBatch> batchMap = new HashMap<String, GkBatch>();
		for(GkBatch ba : batchs){
			batchMap.put(ba.getTeachClassId(), ba);
			teachClassIds.add(ba.getTeachClassId());
			if(groupMap.containsKey(ba.getGroupClassId())){
				groupMap.get(ba.getGroupClassId()).add(ba.getTeachClassId());
			}else{
				Set<String> tClassIds = new HashSet<String>();
				tClassIds.add(ba.getTeachClassId());
				groupMap.put(ba.getGroupClassId(),tClassIds);
			}
		}
		List<TeachClass> teaClslist = SUtils.dt(teachClassService.findListByIds(teachClassIds.toArray(new String[0])), new TR<List<TeachClass>>(){});
		Map<String,TeachClass> teaClsMap = new HashMap<String, TeachClass>();
		Set<String> courseIds = new HashSet<String>();
		Set<String> teaIds = new HashSet<String>();
		for(TeachClass c : teaClslist){
			teaClsMap.put(c.getId(), c);
			courseIds.add(c.getCourseId());
			teaIds.add(c.getTeacherId());
		}
		//学科信息
		Map<String,Course> courseMap = EntityUtils.getMap(SUtils.dt(courseService.findListByIds(courseIds.toArray(new String[0])),new TR<List<Course>>(){}),"id");
		
		//教师信息
		Map<String,Teacher> teaMap = EntityUtils.getMap(SUtils.dt(teacherService.findListByIds(teaIds.toArray(new String[0])), new TR<List<Teacher>>(){}),"id");
		
		//对应班级的学生数量
		Map<String,Integer> teaNumMap = new HashMap<String, Integer>();
		
		List<TeachClassStu> stulist = new ArrayList<TeachClassStu>();
		if(teachClassIds.size() > 0){
			stulist = SUtils.dt(teachClassStuService.findByClassIds(teachClassIds.toArray(new String[0])),new TR<List<TeachClassStu>>(){});
		}
		for(TeachClassStu stu : stulist){
			if(teaNumMap.containsKey(stu.getClassId())){
				teaNumMap.put(stu.getClassId(), teaNumMap.get(stu.getClassId())+1);
			}else{
				teaNumMap.put(stu.getClassId(), 1);
			}
		}
		
		
		GkArrangeGroupResultDto dto;
		for(GkGroupClass gc : gkclasslist){
			dto = new GkArrangeGroupResultDto();
			dto.setConditionId(gc.getId());
			String groupName = gc.getGroupName();
			dto.setGroupName(groupName);
			dto.setGroupStr(groupName);
			Set<String> teaClsIdsSet = groupMap.get(gc.getId());
			StringBuffer baIds = new StringBuffer(); 
			List<GkClassResultDto> baList = new ArrayList<GkClassResultDto>();
			GkClassResultDto resDto;
			StringBuffer clsIds = new StringBuffer();
			if (CollectionUtils.isNotEmpty(teaClsIdsSet)) {
				for (String teaClsId : teaClsIdsSet) {
					clsIds.append(teaClsId + ",");
					resDto = new GkClassResultDto();
					TeachClass teaCls = teaClsMap.get(teaClsId);
					Course course = courseMap.get(teaCls.getCourseId());
					Teacher tea = teaMap.get(teaCls.getTeacherId());
					String batchName = "";
					resDto.setClassId(teaClsId);
					resDto.setClassName(teaCls.getName());
					if (course != null) {
						batchName += course.getSubjectName();
					}
					if (tea != null) {
						resDto.setTeaName(tea.getTeacherName());
					}
					GkBatch batch = batchMap.get(teaClsId);
					if (batch != null) {
						// 场地名称 等待接口
						if (baIds.length() > 0) {
							baIds.append(",");
						}
						batchName += StringUtils.trimToEmpty(batch
								.getClassType());
						baIds.append(batch.getId() + "-" + batch.getBatch());
						dto.setPlace(batch.getPlaceId());
						dto.setPlaceId(batch.getPlaceId());
						resDto.setBatch(batch.getBatch());
						resDto.setBatchIds(batch.getId());
					}
					resDto.setSubName(batchName);
					resDto.setStuNum(teaNumMap.containsKey(teaClsId) ? teaNumMap
							.get(teaClsId) : 0);
					dto.setClassId1(teaClsId);// 随便放个小班的班级id
					baList.add(resDto);
					resDto = null;
				}
			}
			if(clsIds.length() > 0){
				clsIds.setLength(clsIds.length() - 1);
			}
			dto.setClassId(clsIds.toString());// 全部小班班级id的拼接字符串
			dto.setDtolist(baList);
			dto.setBatchIds(baIds.toString());
			baIds = null;
			dtolist.add(dto);
		}
		Collections.sort(dtolist, new Comparator<GkArrangeGroupResultDto>(){

			public int compare(GkArrangeGroupResultDto o1,
					GkArrangeGroupResultDto o2) {
				return StringUtils.trimToEmpty(o1.getGroupStr()).compareTo(StringUtils.trimToEmpty(o2.getGroupStr()));
			}
		});
		return dtolist;
	}
	
	@Override
	public List<GkGroupClass> saveAllEntitys(GkGroupClass... gkGroupClass) {
		return gkGroupClassDao.saveAll(checkSave(gkGroupClass));
	}


	public boolean hasGkGroupByRoundsId(String roundsId,String groupType){
		List<GkGroupClass> list=null;
		if(StringUtils.isNotBlank(groupType)){
			list=gkGroupClassDao.findByRoundsIdAndGroupType(roundsId,groupType);
		}else{
			list=gkGroupClassDao.findByRoundsId(roundsId);
		}
		return CollectionUtils.isNotEmpty(list);
	}
	
	@Override
	public List<GkGroupDto> findGkGroupDtoByRoundsId(String roundsId,String groupType) {
		List<GkGroupClass> list=null;
		if(StringUtils.isNotBlank(groupType)){
			list=gkGroupClassDao.findByRoundsIdAndGroupType(roundsId,groupType);
		}else{
			list=gkGroupClassDao.findByRoundsId(roundsId);
		}
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> groupIds=new HashSet<String>();
			GkGroupDto g=null;
			List<GkGroupDto> returnList=new ArrayList<GkGroupDto>();
			//key:subjectIds
			Map<String,GkGroupDto> dto=new HashMap<String, GkGroupDto>();
			for(GkGroupClass group:list){
				groupIds.add(group.getId());
				if(StringUtils.isNotBlank(group.getSubjectIds())){
					String ids = keySort(group.getSubjectIds());
					if(dto.containsKey(ids)){
						g=dto.get(ids);
						g.getGkGroupClassList().add(group);
					}else{
						g=new GkGroupDto();
						g.setSubjectIds(ids);
						g.setGkGroupClassList(new ArrayList<GkGroupClass>());
						g.getGkGroupClassList().add(group);
						returnList.add(g);
						dto.put(ids, g);
					}
				}
			}
			
			Map<String, List<String>> map = gkGroupClassStuService.findByGroupClassIdIn(groupIds.toArray(new String[0]));
			for(GkGroupClass group:list){
				if(map.containsKey(group.getId())){
					group.setStuIdList(map.get(group.getId()));
					group.setNumber(group.getStuIdList().size());
				}
			}
			return returnList;
		}else{
			return new ArrayList<GkGroupDto>();
		}
	}
	
	private String keySort(String s){
		String[] subjectIds = s.split(",");
		List<String> l=new ArrayList<String>();
		for(String s1:subjectIds){
			if(StringUtils.isNotBlank(s1)){
				l.add(s1);
			}
		}
		Collections.sort(l);
		String s2="";
		for(String s1:l){
			s2=s2+","+s1;
		}
		s2=s2.substring(1);
		return s2;
	}
	
	@Override
	public void deleteByRoundsId(String roundsId, String groupType) {
		List<GkGroupClass> list=null;
		if(StringUtils.isNotBlank(groupType)){
			list=gkGroupClassDao.findByRoundsIdAndGroupType(roundsId, groupType);
		}else{
			list=gkGroupClassDao.findByRoundsId(roundsId);
		}
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> groupIds=new HashSet<String>();
			for(GkGroupClass group:list){
				groupIds.add(group.getId());
			}
			gkGroupClassStuService.deleteByGroupClassIdIn(groupIds.toArray(new String[]{}));
			gkGroupClassDao.deleteByIdIn(groupIds.toArray(new String[]{}));
		}
		
	}

	@Override
	public List<GkGroupClass> findByRoundsId(String roundId) {
		return gkGroupClassDao.findByRoundsId(roundId);
	}

	@Override
	public List<GkGroupClass> findGkGroupClassBySubjectIds(String subjectIds,String roundsId) {
		 List<GkGroupClass> list=null;
		 if(StringUtils.isNotBlank(subjectIds)){
			 list= gkGroupClassDao.findGkGroupClassBySubjectIds(subjectIds,roundsId);	
		 }else{
			 list= gkGroupClassDao.findByRoundsId(roundsId);
		 }
		 if(CollectionUtils.isNotEmpty(list)){
			 Set<String> gId=new HashSet<String>();
			 for(GkGroupClass g:list){
				 gId.add(g.getId());
			 }
			 Map<String, List<String>> map = gkGroupClassStuService.findByGroupClassIdIn(gId.toArray(new String[0]));
			 for(GkGroupClass g:list){
				 if(map.containsKey(g.getId())){
					g.setStuIdList(map.get(g.getId())); 
				 }
			 }
		 }
		 return list;
	}

	@Override
	public List<GkGroupClass> findGkGroupClssList(final String roundsId, final String subjectIds, final String id) {
		 Specification<GkGroupClass> s = new Specification<GkGroupClass>() {
             @Override
             public Predicate toPredicate(Root<GkGroupClass> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                 List<Predicate> ps = new ArrayList<Predicate>();
                 ps.add(cb.equal(root.get("roundsId").as(String.class), roundsId));
                 ps.add(cb.equal(root.get("subjectIds").as(String.class), subjectIds));
                 if(StringUtils.isNotBlank(id)){
                	 ps.add(cb.notEqual(root.get("id").as(String.class), id));
                 }
                 cq.where(ps.toArray(new Predicate[0]));
                 return cq.getRestriction();
             }
         };
		return gkGroupClassDao.findAll(s);
	}

	@Override
	public void deleteById(String... groupId) {
		gkGroupClassDao.deleteByIdIn(groupId);
		gkGroupClassStuService.deleteByGroupClassIdIn(groupId);
	}

	@Override
	public void deleteBySubjectIds(String subjectIds,String roundsId) {
		List<GkGroupClass> list = gkGroupClassDao.findGkGroupClassBySubjectIds(subjectIds,roundsId);
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> gId=new HashSet<String>();
			for(GkGroupClass g:list){
				gId.add(g.getId());
			}
			gkGroupClassDao.deleteByIdIn(gId.toArray(new String[0]));
			gkGroupClassStuService.deleteByGroupClassIdIn(gId.toArray(new String[0]));
		}
	}

	@Override
	public GkGroupClass findById(String groupId) {
		GkGroupClass g=gkGroupClassDao.findById(groupId).orElse(null);
		if(g!=null){
			 Map<String, List<String>> map = gkGroupClassStuService.findByGroupClassIdIn(groupId);
			 if(map!=null && map.containsKey(groupId)){
				 g.setStuIdList(map.get(groupId));
			 }
		}
		return g;
	}

	@Override
	public void update(Set<String> groupId, List<GkGroupClass> updateGroup) {
		//先删除再新增
		if(groupId.size()>0){
			gkGroupClassDao.deleteByIdIn(groupId.toArray(new String[0]));
		}
		
		List<GkGroupClassStu> gstuList=new ArrayList<GkGroupClassStu>();
		GkGroupClassStu item=null;
		if(CollectionUtils.isNotEmpty(updateGroup)){
			for(GkGroupClass g:updateGroup){
				groupId.add(g.getId());
				for(String stuId:g.getStuIdList()){
					item=new GkGroupClassStu();
					item.setGroupClassId(g.getId());
					item.setStudentId(stuId);
					item.setId(UuidUtils.generateUuid());
					gstuList.add(item);
				}
			}
		}
		if(groupId.size()>0){
			gkGroupClassStuService.deleteByGroupClassIdIn(groupId.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(gstuList)){
			gkGroupClassStuService.saveAll(gstuList.toArray(new GkGroupClassStu[0]));
		}
			
		
	}

	@Override
	public List<GkGroupClass> findGkGroupClassList(final String roundsId, final String[] subjectIds) {
		Specification<GkGroupClass> s = new Specification<GkGroupClass>() {
            @Override
            public Predicate toPredicate(Root<GkGroupClass> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("roundsId").as(String.class), roundsId));
                for (String string : subjectIds) {
                	ps.add(cb.like(root.get("subjectIds").as(String.class), "%"+string+"%"));
				}
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };
		return gkGroupClassDao.findAll(s);
	}

	@Override
	public void saveGroup(List<GkGroupClass> groupClass,
			List<GkGroupClassStu> groupClassStu) {
		if(CollectionUtils.isNotEmpty(groupClass)){
			this.saveAll(groupClass.toArray(new GkGroupClass[0]));
		}
		if(CollectionUtils.isNotEmpty(groupClassStu)){
			gkGroupClassStuService.saveAll(groupClassStu.toArray(new GkGroupClassStu[0]));
		}
		
	}

	@Override
	public List<GkGroupClass> findByRoundsIdType(String roundsId,
			String groupType) {
		List<GkGroupClass> list;
		if(StringUtils.isNotBlank(groupType)){
			list = gkGroupClassDao.findByRoundsIdAndGroupType(roundsId, groupType);
		}else{
			list = gkGroupClassDao.findByRoundsId(roundsId);
		}
		
		if(CollectionUtils.isNotEmpty(list)){
			 Set<String> gId=new HashSet<String>();
			 for(GkGroupClass g:list){
				 gId.add(g.getId());
			 }
			 Map<String, List<String>> map = gkGroupClassStuService.findByGroupClassIdIn(gId.toArray(new String[0]));
			 for(GkGroupClass g:list){
				 if(map.containsKey(g.getId())){
					g.setStuIdList(map.get(g.getId())); 
				 }
			 }
		 }else{
			 return new ArrayList<GkGroupClass>();
		 }
		
		
		
		return list;
	}
}
