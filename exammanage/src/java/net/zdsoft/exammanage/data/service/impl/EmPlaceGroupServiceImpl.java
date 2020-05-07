package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmPlaceGroupDao;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.exammanage.data.entity.EmPlaceGroup;
import net.zdsoft.exammanage.data.entity.EmSubGroup;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("emPlaceGroupService")
public class EmPlaceGroupServiceImpl extends BaseServiceImpl<EmPlaceGroup, String> implements EmPlaceGroupService {
    @Autowired
    private EmPlaceGroupDao emPlaceGroupDao;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmStudentGroupService emStudentGroupService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;

    @Override
    public List<EmPlaceGroup> findByGroupIdAndSchoolId(String groupId, String schoolId) {
        return emPlaceGroupDao.findByGroupIdAndSchoolId(groupId, schoolId);
    }

    @Override
    public List<EmPlaceGroup> findByExamIdAndSchoolId(String examId, String schoolId) {
        return emPlaceGroupDao.findByExamIdAndSchoolId(examId, schoolId);
    }

    @Transactional
    public String saveAndDel(String examId, String unitId, String groupId, List<EmPlaceGroup> saveAll) {
        List<EmSubGroup> grouplist = emSubGroupService.findListByExamId(examId);
        List<EmSubGroup> lastList=new ArrayList<>();
        EmSubGroup group=new EmSubGroup();
        grouplist.forEach(g->{
            if(g.getId().equals(groupId)){
                group.setSubjectId(g.getSubjectId());
                group.setSubType(g.getSubType());
            }else{
                lastList.add(g);
            }
        });
        Set<String> placeIds=EntityUtils.getSet(saveAll,EmPlaceGroup::getExamPlaceId);
        //获取已有的科目组对应的所有场地
        List<EmPlaceGroup> emPlaceGroups=emPlaceGroupDao.findByExamIdAndSchoolId(examId,unitId);
        Map<String,Set<String>> groupPlaceMap=new HashMap<>();
        if(CollectionUtils.isNotEmpty(emPlaceGroups)){
            emPlaceGroups.forEach(emPlaceGroup -> {
                if(!groupPlaceMap.containsKey(emPlaceGroup.getGroupId())){
                    groupPlaceMap.put(emPlaceGroup.getGroupId(),new HashSet<>());
                }
                groupPlaceMap.get(emPlaceGroup.getGroupId()).add(emPlaceGroup.getExamPlaceId());
            });
        }
        List<EmSubjectInfo> subList=emSubjectInfoService.findByExamId(examId);
        List<EmSubjectInfo> allList=getAllList(subList);
        Map<String,EmSubjectInfo> allMap=EntityUtils.getMap(allList,k->k.getSubjectId()+"_"+k.getGkSubType(),Function.identity());
        boolean flag=false;
        for(EmSubGroup last:lastList){
            if("0".equals(group.getSubType())){//当前保存的科目组是非7选3的情况
                for(String subId:group.getSubjectId().split(",")){
                    if(allMap.containsKey(subId+"_0") && allMap.containsKey(last.getSubjectId()+"_"+last.getSubType())){
                        if(allMap.get(subId+"_0").getEndDate().compareTo(allMap.get(last.getSubjectId()+"_"+last.getSubType()).getStartDate())<=0
                                || allMap.get(subId+"_0").getStartDate().compareTo(allMap.get(last.getSubjectId()+"_"+last.getSubType()).getEndDate())>=0){
                            //是不交叉的
                        }else{
                            flag=true;
                            //判断两个之间是否存在交集
                            Set<String> inSets=groupPlaceMap.get(last.getId());
                            if(CollectionUtils.isNotEmpty(inSets)){
                                inSets.retainAll(placeIds);
                                if(CollectionUtils.isNotEmpty(inSets)){
                                    return "与"+last.getGroupName()+"在考试时间和考场上存在交叉";
                                }
                            }
                        }
                    }
                }
            }else{//当前保存的科目组是7选3的情况下
                for(String lastId:last.getSubjectId().split(",")){
                    if(allMap.containsKey(lastId+"_"+last.getSubType()) && allMap.containsKey(group.getSubjectId()+"_"+group.getSubType())){
                        if(allMap.get(lastId+"_"+last.getSubType()).getEndDate().compareTo(allMap.get(group.getSubjectId()+"_"+group.getSubType()).getStartDate())<=0
                                || allMap.get(lastId+"_"+last.getSubType()).getStartDate().compareTo(allMap.get(group.getSubjectId()+"_"+group.getSubType()).getEndDate())>=0){
                            //是不交叉的
                        }else{
                            flag=true;
                            //判断两个之间是否存在交集
                            Set<String> setIds=groupPlaceMap.get(last.getId());
                            if(CollectionUtils.isNotEmpty(setIds)){
                                setIds.retainAll(placeIds);
                                if(CollectionUtils.isNotEmpty(setIds)){
                                    return "与"+last.getGroupName()+"在考试时间和考场上存在交叉";
                                }
                            }

                        }
                    }
                }
            }
        }
        emPlaceGroupDao.deleteByExamIdAndSchoolIdAndGroupId(examId, unitId, groupId);
        if (CollectionUtils.isNotEmpty(saveAll)) {
            saveAll(saveAll.toArray(new EmPlaceGroup[0]));
        }
        return "";
    }

    @Transactional
    public String autoArrangePlace(String examId, String unitId) {
        emPlaceGroupDao.deleteByExamIdAndSchoolId(examId, unitId);
        List<EmSubGroup> grouplist = emSubGroupService.findListByExamId(examId);
        //获取科目的时间等信息
//        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
//        Map<String, Course> course73Map = courseList73.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        List<EmSubjectInfo> subList=emSubjectInfoService.findByExamId(examId);
        //时间交叉的科目id
        Set<String> repeatIds=new HashSet<>();
        String str=checkDate(subList,repeatIds);

        Map<String,EmSubjectInfo> subMap=EntityUtils.getMap(emSubjectInfoService.findByExamId(examId), EmSubjectInfo::getSubjectId, Function.identity());
        Set<String> groupIds = EntityUtils.getSet(grouplist, "id");
        Map<String, Set<String>> stuGroupMap = emStudentGroupService.findGroupMap(unitId, examId, groupIds.toArray(new String[0]));
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
        Set<String> removeStuId = new HashSet<>();
        if (filterMap.size() > 0) {
            removeStuId = filterMap.keySet();
        }
        List<EmPlace> placeAlls = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        Set<String> arrangePlaceIds = new HashSet<>();
        Map<String, Set<String>> arrangePlaceMap = new HashMap<>();
        List<EmPlaceGroup> saveList = new ArrayList<>();
        boolean flag=false;
        //有交集时间的科目组暂不自动分配
        for (EmSubGroup g : grouplist) {
            flag=false;
            if("0".equals(g.getSubType())){//语数英等非7选3的情况下
                for(String ysyId:g.getSubjectId().split("_")){
                    if(repeatIds.contains(ysyId+"_0")){
                        flag=true;
                        break;
                    }
                }
            }else{
                if(repeatIds.contains(g.getSubjectId()+"_"+g.getSubType())){
                    continue;
                }
            }
            if(flag) continue;;

            int arrangeNum = 0;
            Set<String> stus = stuGroupMap.get(g.getId());
            if (CollectionUtils.isNotEmpty(stus)) {
                stus.removeAll(removeStuId);
                arrangeNum = stus.size();
            }
            if (arrangeNum > 0) {

                for (EmPlace p : placeAlls) {
//					if(arrangePlaceIds.contains(p.getId())) {
//						continue;
//					}
                    if (!StringUtils.equals(g.getSubType(), "0")) {
                        if (StringUtils.equals(g.getSubType(), "1")) {
                            Set<String> ss = arrangePlaceMap.get(g.getSubjectId() + "2");
                            if (ss != null && ss.contains(p.getId())) {
                                continue;
                            }
                        }
                        if (StringUtils.equals(g.getSubType(), "2")) {
                            Set<String> ss = arrangePlaceMap.get(g.getSubjectId() + "1");
                            if (ss != null && ss.contains(p.getId())) {
                                continue;
                            }
                        }
                    }
                    EmPlaceGroup e = new EmPlaceGroup();
                    e.setId(UuidUtils.generateUuid());
                    e.setExamId(examId);
                    e.setExamPlaceId(p.getId());
                    e.setSchoolId(unitId);
                    e.setGroupId(g.getId());
                    saveList.add(e);
//					arrangePlaceIds.add(p.getId());
                    if (!arrangePlaceMap.containsKey(g.getSubjectId() + g.getSubType())) {
                        arrangePlaceMap.put(g.getSubjectId() + g.getSubType(), new HashSet<>());
                    }
                    arrangePlaceMap.get(g.getSubjectId() + g.getSubType()).add(p.getId());
                    arrangeNum = arrangeNum - p.getCount();
                    if (arrangeNum < 1) {
                        break;
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(saveList)) {
            saveAll(saveList.toArray(new EmPlaceGroup[0]));
        }
        return str;
    }

    @Override
    public Map<String, Set<String>> findGroupMap(String unitId, String[] groupIds) {
        List<EmPlaceGroup> places = emPlaceGroupDao.findBySchoolIdAndGroupIdIn(unitId, groupIds);
        Map<String, Set<String>> map = new HashMap<>();
        for (EmPlaceGroup e : places) {
            if (!map.containsKey(e.getGroupId())) {
                map.put(e.getGroupId(), new HashSet<String>());
            }
            map.get(e.getGroupId()).add(e.getExamPlaceId());
        }
        return map;
    }

    /**
     * 得到交叉的科目
     * @param subList
     * @param repeatIds
//     * @param course73Map
     * @return
     */
    public String checkDate(List<EmSubjectInfo> subList,Set<String> repeatIds){//,Map<String, Course> course73Map
        String str="";
        if(CollectionUtils.isNotEmpty(subList)){
            List<EmSubjectInfo> allList=getAllList(subList);
            int length=allList.size();
            for(int i=0;i<length;i++){
                for(int j=i+1;j<length;j++){
                    if(allList.get(i).getEndDate().compareTo(allList.get(j).getStartDate())<=0
                            || allList.get(i).getStartDate().compareTo(allList.get(j).getEndDate())>=0){
                        //是不交叉的
                    }else{
                        EmSubjectInfo sub1=allList.get(i);
                        EmSubjectInfo sub2=allList.get(j);
//                        String subjectId1=sub1.getSubjectId();
//                        String subjectId2=sub2.getSubjectId();
                        repeatIds.add(sub1.getSubjectId()+"_"+sub1.getGkSubType());
                        repeatIds.add(sub2.getSubjectId()+"_"+sub2.getGkSubType());
//                        repeatMap.put(subjectId1+","+subjectId2,subjectId1);
//                        repeatMap.put(subjectId2+","+subjectId1,subjectId1);
                        if(StringUtils.isNotBlank(str)){
                            str+=",";
                        }
                        str+=sub1.getCourseName();
                        if("1".equals(sub1.getGkSubType())){//选考
                            str+="选考与";
                        }else if("2".equals(sub1.getGkSubType())){
                            str+="学考与";
                        }else{
                            str+="与";
                        }
                        str+=sub2.getCourseName();
                        if("1".equals(sub2.getGkSubType())){//选考
                            str+="选考";
                        }else if("2".equals(sub2.getGkSubType())){
                            str+="学考";
                        }
                    }
                }
            }
        }
        return str;
    }
    /**
     * 将选考学考科目分开
     * @param subList
     * @return
     */
    public List<EmSubjectInfo> getAllList(List<EmSubjectInfo> subList){
        List<EmSubjectInfo> allList=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(subList)) {
            subList.forEach(sub -> {
                if ("0".equals(sub.getGkSubType()) && sub.getGkStartDate() != null) {//0的情况下且有这个时间，说明是7选三科目 所有学生范围
                    EmSubjectInfo inSub = new EmSubjectInfo();
                    inSub.setSubjectId(sub.getSubjectId());
                    inSub.setCourseName(sub.getCourseName());
                    inSub.setGkSubType("2");//学考
                    inSub.setStartDate(sub.getGkStartDate());
                    inSub.setEndDate(sub.getGkEndDate());
                    allList.add(inSub);
                    inSub=new EmSubjectInfo();
                    inSub.setSubjectId(sub.getSubjectId());
                    inSub.setCourseName(sub.getCourseName());
                    inSub.setGkSubType("1");//设为选考类型即可
                    inSub.setStartDate(sub.getStartDate());
                    inSub.setEndDate(sub.getEndDate());
                    allList.add(inSub);
                } else if ("2".equals(sub.getGkSubType())) {
                    EmSubjectInfo inSub = new EmSubjectInfo();
                    inSub.setSubjectId(sub.getSubjectId());
                    inSub.setCourseName(sub.getCourseName());
                    inSub.setGkSubType(sub.getGkSubType());
                    inSub.setStartDate(sub.getGkStartDate());
                    inSub.setEndDate(sub.getGkEndDate());
                    allList.add(inSub);
                } else {
                    allList.add(sub);
                }
            });
        }
        return allList;
    }

    @Override
    protected BaseJpaRepositoryDao<EmPlaceGroup, String> getJpaDao() {
        return emPlaceGroupDao;
    }

    @Override
    protected Class<EmPlaceGroup> getEntityClass() {
        return EmPlaceGroup.class;
    }

}
