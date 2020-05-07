package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisMutualGroupDao;
import net.zdsoft.diathesis.data.dao.DiathesisMutualGroupStuDao;
import net.zdsoft.diathesis.data.dto.DiathesisGroupList;
import net.zdsoft.diathesis.data.dto.DiathesisIdAndNameDto;
import net.zdsoft.diathesis.data.dto.DiathesisMutualGroupDto;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroup;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroupStu;
import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupService;
import net.zdsoft.diathesis.data.service.DiathesisMutualGroupStuService;
import net.zdsoft.diathesis.data.service.DiathesisSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:23
 */
@Service("diathesisMutualGroup")
public class DiathesisMutualGroupServiceImpl extends BaseServiceImpl<DiathesisMutualGroup,String>
    implements DiathesisMutualGroupService {

    @Autowired
    private DiathesisMutualGroupDao diathesisMutualGroupDao;
    @Autowired
    private DiathesisMutualGroupStuDao diathesisMutualGroupStuDao;
    @Autowired
    private DiathesisMutualGroupStuService diathesisMutualGroupStuService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;

    @Override
    protected BaseJpaRepositoryDao<DiathesisMutualGroup, String> getJpaDao() {
        return diathesisMutualGroupDao;
    }

    @Override
    protected Class<DiathesisMutualGroup> getEntityClass() {
        return DiathesisMutualGroup.class;
    }


    @Override
    public void deleteById(String groupId) {
        diathesisMutualGroupDao.deleteGroupById(groupId);
        diathesisMutualGroupStuDao.deleteSoftByMutualId(groupId);
    }

    @Override
    public List<DiathesisMutualGroupDto> findGroupByInfo(String classId ,Integer semester,String acadyear) {
        List<DiathesisMutualGroup> group= diathesisMutualGroupDao.findByAcadyearAndSemeterAndClassId(acadyear,semester,classId);
        if(CollectionUtils.isEmpty(group))return new ArrayList<>();
        List<String> groupIds = EntityUtils.getList(group, x -> x.getId());

        List<DiathesisMutualGroupStu> studentList= diathesisMutualGroupStuDao.findListByMutualGroupIdIn(groupIds);

        List<String> list = EntityUtils.getList(studentList, x -> x.getStudentId());
        String[] stuIds = Stream.concat(group.stream().map(x -> x.getLeaderId()), studentList.stream().map(x -> x.getStudentId())).distinct().toArray(String[]::new);
        Map<String, String> stuIdNameMap = SUtils.dt(studentRemoteService.findListByIds(stuIds), Student.class).stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getStudentName()));
        Map<String, List<DiathesisIdAndNameDto>> stuMap = studentList.stream().collect(Collectors.groupingBy(x -> x.getMutualGroupId(), Collectors.mapping(x -> {
            DiathesisIdAndNameDto stu = new DiathesisIdAndNameDto();
            stu.setId(x.getStudentId());
            stu.setName(stuIdNameMap.get(x.getStudentId()));
            return stu;
        }, Collectors.toList())));
        return group.stream().map(x -> {
            List<DiathesisIdAndNameDto> stuList = stuMap.get(x.getId());
            DiathesisMutualGroupDto dto = new DiathesisMutualGroupDto();
            dto.setId(x.getId());
            dto.setGroupName(x.getMutualGroupName());
            dto.setLeaderId(x.getLeaderId());
            dto.setLeaderName(stuIdNameMap.get(x.getLeaderId()));
            dto.setStudentNum(list.size());
            dto.setStudentList(stuList);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DiathesisIdAndNameDto> findMutualStudentList(String studentId,String unitId,String acadyear,Integer semeter) {
        DiathesisSet setting = diathesisSetService.findByUnitId(unitId);
        List<DiathesisMutualGroupStu> mutualGroupStudentList =new ArrayList<>();
        String[] stuIds=new String[]{};
        if(setting.getMutualType().equals(DiathesisConstant.MUTUAL_EVALUATE_LEADER)){
            //组长评价制
            DiathesisMutualGroup group= diathesisMutualGroupDao.findByGroupIdAndTimeInfo(studentId,acadyear,semeter);
            if(group==null)return new ArrayList<>();
            mutualGroupStudentList = diathesisMutualGroupStuService.findListByMutualGroupId(group.getId());
            stuIds = EntityUtils.getArray(mutualGroupStudentList, x -> x.getStudentId(), String[]::new);

        }else if(setting.getMutualType().equals(DiathesisConstant.MUTUAL_EVALUATE_MEMBER)){
            //组员互相评价制
            String groupId= diathesisMutualGroupStuDao.findGroupIdByAcadyearAndSemeterAndStudentId(studentId,acadyear,semeter);
            mutualGroupStudentList = diathesisMutualGroupStuService.findListByMutualGroupId(groupId)
                    .stream().filter(x->!x.getStudentId().equals(studentId)).collect(Collectors.toList());
        }
        Map<String, String> stuIdNameMap = SUtils.dt(studentRemoteService.findListByIds(stuIds), Student.class).stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getStudentName()));
         return mutualGroupStudentList.stream().map(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getStudentId());
            dto.setName(stuIdNameMap.get(x.getStudentId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DiathesisMutualGroup> findByLeadIdAndSemester(String studentId, String acadyear, Integer semester) {
        return diathesisMutualGroupDao.findByLeadIdAndSemester(studentId,acadyear,semester);
    }

    @Override
    public DiathesisMutualGroup findOneById(String groupId) {
        return diathesisMutualGroupDao.findOneById(groupId);
    }

    @Override
    public void saveGroupList(DiathesisGroupList groupList) {

        //1.删除原来所有的,保存新的
        List<DiathesisMutualGroup> list = groupList.getGroupList();
        List<String> oldGroupIds = EntityUtils.getList(list, x -> x.getId());
        HashMap<String, Date> creationTimeMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(oldGroupIds)){
            List<DiathesisMutualGroup> oldList=diathesisMutualGroupDao.findListByIds(oldGroupIds);
            if(CollectionUtils.isNotEmpty(oldList)){
                for (DiathesisMutualGroup group : oldList) {
                    creationTimeMap.put(group.getId(),group.getCreationTime());
                }
            }
        }
        Set<String> stuSet = list.stream().flatMap(x -> x.getStudentIds().stream()).collect(Collectors.toSet());
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(groupList.getClassId()), Student.class);
        if(stuSet.size()!=studentList.size()){
            throw new RuntimeException("学生重复分组,或者存在没有分组的学生");
        }
        Set<String> groupNameSet = EntityUtils.getSet(list, x -> x.getMutualGroupName());
        if(groupNameSet.size()!=list.size()){
            throw new RuntimeException("组名重复!");
        }
        List<DiathesisMutualGroup> oldGroupList = diathesisMutualGroupDao.findByAcadyearAndSemeterAndClassId(groupList.getAcadyear(), groupList.getSemester(), groupList.getClassId());
        List<String> groupIds = EntityUtils.getList(oldGroupList, x -> x.getId());

        if(CollectionUtils.isNotEmpty(groupIds)){
            diathesisMutualGroupDao.deleteByIds(groupIds);
            diathesisMutualGroupStuService.deleteByMutualGroupIds(groupIds);
        }
        int index=0;
        List<DiathesisMutualGroup> groupSaveList=new ArrayList<>();
        List<DiathesisMutualGroupStu> stuSaveList=new ArrayList<>();
        for (DiathesisMutualGroup group : groupList.getGroupList()) {
            DiathesisMutualGroup entity = new DiathesisMutualGroup();
            if (StringUtils.isNotBlank(group.getId()) && Objects.nonNull(creationTimeMap.get(group.getId()))){
                entity.setCreationTime(creationTimeMap.get(group.getId()));
            }else{
                entity.setCreationTime(new Date());
            }
            entity.setId(UuidUtils.generateUuid());
            entity.setModifyTime(new Date());
            entity.setSemester(groupList.getSemester());
            entity.setAcadyear(groupList.getAcadyear());
            entity.setMutualGroupName(group.getMutualGroupName());
            entity.setSortNum(index++);
            entity.setClassId(groupList.getClassId());
            entity.setUnitId(groupList.getUnitId());
            entity.setOperator(groupList.getRealName());
            entity.setLeaderId(group.getLeaderId());
            groupSaveList.add(entity);

            List<String> stuIds = group.getStudentIds();
            for (String stuId : stuIds) {
                DiathesisMutualGroupStu stu = new DiathesisMutualGroupStu();
                stu.setId(UuidUtils.generateUuid());
                stu.setUnitId(groupList.getUnitId());
                stu.setMutualGroupId(entity.getId());
                stu.setStudentId(stuId);
                stuSaveList.add(stu);
            }
        }
        if (CollectionUtils.isNotEmpty(groupSaveList)){
            diathesisMutualGroupDao.saveAll(groupSaveList);
        }
        if(CollectionUtils.isNotEmpty(stuSaveList)){
            diathesisMutualGroupStuService.saveAll(stuSaveList.toArray(new DiathesisMutualGroupStu[0]));
        }
    }

}
