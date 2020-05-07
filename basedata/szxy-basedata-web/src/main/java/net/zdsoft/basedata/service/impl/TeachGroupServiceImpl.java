package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.TeachGroupDao;
import net.zdsoft.basedata.dao.TeachGroupExDao;
import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.TeachGroupExService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PinyinUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("teachGroupService")
public class TeachGroupServiceImpl extends BaseServiceImpl<TeachGroup, String> implements TeachGroupService{

	@Autowired
	private TeachGroupDao teachGroupDao;
	@Autowired
	private TeachGroupExDao teachGroupExDao;
	@Autowired
	private TeachGroupExService teachGroupExService;
	@Autowired
	private TeacherService teacherService;
	
	@Override
	protected BaseJpaRepositoryDao<TeachGroup, String> getJpaDao() {
		return teachGroupDao;
	}

	@Override
	protected Class<TeachGroup> getEntityClass() {
		return TeachGroup.class;
	}

	@Override
	public List<TeachGroup> findBySchoolId(String unitId) {
		return teachGroupDao.findBySchoolId(unitId);
	}

    @Override
    public List<TeachGroup> findBySchoolIdWithMaster(String unitId) {
        return teachGroupDao.findBySchoolId(unitId);
    }

	@Override
	public void deleteAndSave(String teachGroupId, TeachGroup teachGroup,
			List<TeachGroupEx> teachGroupExList) {
		teachGroupDao.deleteById(teachGroupId);
		teachGroupDao.save(teachGroup);
		teachGroupExDao.deleteByTeacherGroupId(teachGroupId);
		teachGroupExDao.saveAll(teachGroupExList);
	}

	@Override
	public void delete(String[] teachGroupIds) {
		teachGroupDao.deleteByIdIn(teachGroupIds);
	}

	@Override
	public void save(List<TeachGroup> saveTeachGroupList,
			List<TeachGroupEx> saveTeachGroupExList) {
		teachGroupDao.saveAll(saveTeachGroupList);
		teachGroupExDao.saveAll(saveTeachGroupExList);
	}

	@Override
	public List<TeachGroup> findBySchoolId(String unitId, boolean isMakeTeacher) {
		List<TeachGroup> list = teachGroupDao.findBySchoolId(unitId);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<TeachGroup>();
		}
		if(isMakeTeacher){
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			List<TeachGroupEx> exlist = teachGroupExService.findByTeachGroupId(ids.toArray(new String[]{}));
			Map<String,Set<String>> tIdByGroupId=new HashMap<String,Set<String>>();
			if(CollectionUtils.isNotEmpty(exlist)){
				for(TeachGroupEx ee:exlist){
					Set<String> set = tIdByGroupId.get(ee.getTeachGroupId());
					if(CollectionUtils.isEmpty(set)){
						set=new HashSet<String>();
						tIdByGroupId.put(ee.getTeachGroupId(), set);
					}
					set.add(ee.getTeacherId());
				}
				for(TeachGroup gg:list){
					if(tIdByGroupId.containsKey(gg.getId())){
						gg.setTeacherIdSet(tIdByGroupId.get(gg.getId()));
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<TeachGroup> findBySchoolIdAndType(String unitId, Integer type) {
		List<TeachGroup> list = teachGroupDao.findBySchoolId(unitId);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<TeachGroup>();
		}
		if(type == null){
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			List<TeachGroupEx> exlist = teachGroupExService.findByTeachGroupId(ids.toArray(new String[]{}));
			Map<String,Set<String>> tIdByGroupId=new HashMap<String,Set<String>>();
			if(CollectionUtils.isNotEmpty(exlist)){
				for(TeachGroupEx ee:exlist){
					Set<String> set = tIdByGroupId.get(ee.getTeachGroupId());
					if(CollectionUtils.isEmpty(set)){
						set=new HashSet<String>();
						tIdByGroupId.put(ee.getTeachGroupId(), set);
					}
					set.add(ee.getTeacherId());
				}
				for(TeachGroup gg:list){
					if(tIdByGroupId.containsKey(gg.getId())){
						gg.setTeacherIdSet(tIdByGroupId.get(gg.getId()));
					}
				}
			}
			return list;
		}else {
			Set<String> ids = EntityUtils.getSet(list, e->e.getId());
			List<TeachGroupEx> exlist = teachGroupExService.findByTypeAndTeachGroupIdIn(type,ids.toArray(new String[]{}));
			Map<String,Set<String>> tIdByGroupId=new HashMap<String,Set<String>>();
			if(CollectionUtils.isNotEmpty(exlist)){
				for(TeachGroupEx ee:exlist){
					Set<String> set = tIdByGroupId.get(ee.getTeachGroupId());
					if(CollectionUtils.isEmpty(set)){
						set=new HashSet<String>();
						tIdByGroupId.put(ee.getTeachGroupId(), set);
					}
					set.add(ee.getTeacherId());
				}
				for(TeachGroup gg:list){
					if(tIdByGroupId.containsKey(gg.getId())){
						gg.setTeacherIdSet(tIdByGroupId.get(gg.getId()));
					}
				}
			}

			return list;
		}
	}

	/**
	  * 教研组教师数据
	  * @param unitId
	  * @return
	  */
	 public List<TeachGroupDto> findTeachers(String unitId){
		 //需要的字段：teachGroupName; mainTeacherList( teacherId,teacherName)
		 List<TeachGroupDto> groupDtoList=new ArrayList<TeachGroupDto>();
		 List<TeachGroup> groupList = findBySchoolId(unitId, true);
		 Set<String> teacherIds=new HashSet<String>();
		 for(TeachGroup gg:groupList){
			 if(CollectionUtils.isNotEmpty(gg.getTeacherIdSet())){
				 teacherIds.addAll(gg.getTeacherIdSet());
			 }
		 }
		 Map<String, Teacher> teacherMap=new HashMap<String, Teacher>();
		 if(CollectionUtils.isNotEmpty(teacherIds)){
			 List<Teacher> teacherList = teacherService.findListByIdIn(teacherIds.toArray(new String[]{}));
			 teacherMap = EntityUtils.getMap(teacherList, e->e.getId());
		 }
		 TeachGroupDto teachGroupDto;
		 TeacherDto teacherDto;
		 for(TeachGroup gg:groupList){
			 if(CollectionUtils.isNotEmpty(gg.getTeacherIdSet())){
				 teachGroupDto=new TeachGroupDto();
				 teachGroupDto.setTeachGroupId(gg.getId());
				 teachGroupDto.setTeachGroupName(gg.getTeachGroupName());
				 teachGroupDto.setMainTeacherList(new ArrayList<TeacherDto>());
				 for(String tt:gg.getTeacherIdSet()){
					 if(teacherMap.containsKey(tt)){
						 teacherDto=new TeacherDto();
						 teacherDto.setTeacherId(tt);
						 Teacher teacher = teacherMap.get(tt);
						 teacherDto.setTeacherName(teacher.getTeacherName());
						 teachGroupDto.getMainTeacherList().add(teacherDto);
					 }
					
				 }
				 groupDtoList.add(teachGroupDto);
			 }
		 }
		 return groupDtoList;
	 }

	@Override
	public List<TeachGroup> findBySchoolIdAndSubjectIdIn(String unitId, String[] subids) {
		return teachGroupDao.findBySchoolIdAndSubjectIdIn(unitId, subids);
	}

	@Override
	public Integer findMaxOrder(String unitId) {
		return teachGroupDao.findMaxOrder(unitId);
	}

	@Override
	public List<TeachGroupDto> findAllTeacherGroup(String unitId,boolean isSortFirst) {
		List<TeachGroupDto> groupDtoList=new ArrayList<>();
		TeachGroupDto dto;
		TeacherDto teacherDto;
		List<TeacherDto> teacherDtoList;
		List<Teacher> teacherList = teacherService.findByUnitId(unitId);
		
		Map<String,Teacher> teacherMap=new HashMap<>();
		Set<String> tIds=new HashSet<>();
		if(CollectionUtils.isNotEmpty(teacherList)) {
			teacherMap=EntityUtils.getMap(teacherList, e->e.getId());
			tIds=new HashSet<>(teacherMap.keySet());
		}
		List<TeachGroup> groupList = findBySchoolId(unitId);
		if(CollectionUtils.isNotEmpty(groupList)) {
			Set<String> ids=EntityUtils.getSet(groupList, e->e.getId());
			List<TeachGroupEx> exList = teachGroupExService.findByTeachGroupId(ids.toArray(new String[] {}));
			Map<String, List<String>> map=new HashMap<>();
			if(CollectionUtils.isNotEmpty(exList)) {
				map = EntityUtils.getListMap(exList, TeachGroupEx::getTeachGroupId, e->e.getTeacherId());
			}
			for(TeachGroup group:groupList) {
				dto=new TeachGroupDto();
				dto.setTeachGroupId(group.getId());
				dto.setTeachGroupName(group.getTeachGroupName());
				dto.setOrderId(group.getOrderId());
				teacherDtoList=new ArrayList<>();
				
				if(map.containsKey(group.getId())) {
					//去除重复老师
					Set<String> tId=new HashSet<>();
					for(String tt:map.get(group.getId())) {
						if(tId.contains(tt)) {
							continue;
						}
						if(teacherMap.containsKey(tt)) {
							teacherDto=new TeacherDto();
							teacherDto.setTeacherId(teacherMap.get(tt).getId());
							teacherDto.setTeacherName(teacherMap.get(tt).getTeacherName());
							String pingyingFirse=PinyinUtils.toHanyuPinyin(teacherDto.getTeacherName(), true);
							if(pingyingFirse==null) {
								pingyingFirse="";
							}
							//获取首字母
							teacherDto.setFirsePinyin(pingyingFirse);
							teacherDtoList.add(teacherDto);
							tIds.remove(tt);
							tId.add(tt);
						}
						
					}
				}
				if(isSortFirst) {
					if(CollectionUtils.isNotEmpty(teacherDtoList)) {
						//根据名称首字母排序
						Collections.sort(teacherDtoList, new Comparator<TeacherDto>() {
							@Override
							public int compare(TeacherDto o1, TeacherDto o2) {
								return o1.getFirsePinyin().compareTo(o2.getFirsePinyin());
							}
							
						});
					}
				}
				dto.setMainTeacherList(teacherDtoList);
				groupDtoList.add(dto);
			}
		}
		if(CollectionUtils.isNotEmpty(tIds)) {
			dto=new TeachGroupDto();
			dto.setTeachGroupId(BaseConstants.ZERO_GUID);
			dto.setTeachGroupName("未分配教研组");
			teacherDtoList=new ArrayList<>();
			for(Teacher tt:teacherList) {
				if(!tIds.contains(tt.getId())) {
					continue;
				}
				teacherDto=new TeacherDto();
				teacherDto.setTeacherId(tt.getId());
				teacherDto.setTeacherName(tt.getTeacherName());
				String pingyingFirse=PinyinUtils.toHanyuPinyin(teacherDto.getTeacherName(), true);
				if(pingyingFirse==null) {
					pingyingFirse="";
				}
				//获取首字母
				teacherDto.setFirsePinyin(pingyingFirse);
				teacherDtoList.add(teacherDto);
			}
			if(isSortFirst) {
				if(CollectionUtils.isNotEmpty(teacherDtoList)) {
					//根据名称首字母排序
					Collections.sort(teacherDtoList, new Comparator<TeacherDto>() {
						@Override
						public int compare(TeacherDto o1, TeacherDto o2) {
							return o1.getFirsePinyin().compareTo(o2.getFirsePinyin());
						}
						
					});
				}
			}
			dto.setMainTeacherList(teacherDtoList);
			groupDtoList.add(dto);
		}
		return groupDtoList;
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		 teachGroupDao.deleteBySubjectIdIn(subjectIds);
	}
}
