package net.zdsoft.stutotality.data.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.stutotality.data.constant.StutotalityTypeEnum;
import net.zdsoft.stutotality.data.dto.StutotalityOptionDto;
import net.zdsoft.stutotality.data.dto.StutotalityResultDto;
import net.zdsoft.stutotality.data.dto.StutotalityTypeDto;
import net.zdsoft.stutotality.data.entity.*;
import net.zdsoft.stutotality.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("stutotalityResultRemoteService")
public class StutotalityResultRemoteServiceImpl  implements StutotalityResultRemoteService {

    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private StutotalityItemService stutotalityItemService;
    @Autowired
    private StutotalityItemOptionService stutotalityItemOptionService;
    @Autowired
    private StutotalityScaleService stutotalityScaleService;
    @Autowired
    private StutotalityStuResultService stutotalityStuResultService;
    @Autowired
    private StutotalityRewardService stutotalityRewardService;
    @Autowired
    private StutotalityStuRewardService stutotalityStuRewardService;
    @Autowired
    private StutotalityCodeService stutotalityCodeService;

    @Override
    public List<StutotalityTypeDto> getStutotalitytypeList(String studentId){
        return StutotalityTypeEnum.getTypeDtoList();
    }
    /*@Override
    public StutotalityTypeDto checkCode(String studentId,String codeId){
        StutotalityTypeDto dto=new StutotalityTypeDto();
        StutotalityCode code=stutotalityCodeService.findOne(codeId);
        if(code==null){
            dto.setStatus("-1");
            dto.setMsg("二维码已失效，不可录入哦~");
            return dto;
        }else{
            StutotalityItem item=stutotalityItemService.findOne(code.getItemId());
            if(item==null){
                dto.setStatus("-1");
                dto.setMsg("二维码对应的项目已被删除，不可录入哦~");
                return dto;
            }
            Student stu=SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            String schoolId=stu.getSchoolId();
            Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()), Clazz.class);
            if(!clazz.getGradeId().equals(item.getGradeId())){
                dto.setStatus("-1");
                dto.setMsg("二维码非本年级生成，不可录入哦~");
                return dto;
            }
            dto.setCodeName(code.getName());
            dto.setScore(code.getScore());
            dto.setItemName(item.getItemName());
            dto.setStatus("200");
        }
        return dto;
    }*/
    @Override
    public StutotalityTypeDto saveCode(String studentId,String codeId){
        StutotalityTypeDto dto=new StutotalityTypeDto();
        StutotalityCode code=stutotalityCodeService.findOne(codeId);
        if(code==null || code.getHasUsing()==1){
            dto.setStatus("-1");
            dto.setMsg("二维码已被录入，不可再次录入哦~");
            return dto;
        }else{

            try {
                Student stu=SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
                String schoolId=stu.getSchoolId();
                Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()), Clazz.class);

                Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(1,schoolId),Semester.class);
                String acadyear=se.getAcadyear();
                String semester=se.getSemester().toString();
                //获取到该学生的所有项目 通过名称来匹配二维码
                List<StutotalityItem> itemList=stutotalityItemService.getItemListByParams(schoolId,acadyear,semester,clazz.getGradeId(),1);
                boolean flag=false;
                StutotalityItem item=null;
                if(CollectionUtils.isNotEmpty(itemList)){
                    for(StutotalityItem item1:itemList){
                        if(item1.getItemName().equals(code.getItemName())){
                            item=item1;
                            flag=true;
                            break;
                        }
                    }
                }
                if(!flag){
                    dto.setStatus("-1");
                    dto.setMsg("二维码对应的学科已被删除，不可录入哦~");
                    return dto;
                }
                List<StutotalityStuResult> resultList=stutotalityStuResultService.findListByParms(schoolId,acadyear,semester,studentId,new String[]{item.getId()});
                Map<String,StutotalityStuResult> resultMap=EntityUtils.getMap(resultList,StutotalityStuResult::getOptionId);
                List<StutotalityItemOption> optionList=stutotalityItemOptionService.findByItemIds(new String[]{item.getId()});
                String optionId=null;
                if(CollectionUtils.isNotEmpty(optionList)){
                    for(StutotalityItemOption option:optionList){
                        if(!resultMap.containsKey(option.getId())){
                            optionId=option.getId();
                            break;
                        }
                    }
                }
                if(StringUtils.isBlank(optionId)){
                    dto.setStatus("-1");
                    dto.setMsg("学科的内容成绩已全部维护，不可再录入哦~");
                    return dto;
                }
                StutotalityStuResult result=new StutotalityStuResult();
                result.setId(UuidUtils.generateUuid());
                result.setUnitId(schoolId);
                result.setAcadyear(acadyear);
                result.setSemester(semester);
                result.setStudentId(studentId);
                result.setResult(code.getScore());
                result.setItemHealthId(item.getId());
                result.setOptionId(optionId);
                result.setType("1");
                result.setCreationTime(new Date());
                result.setModifyTime(new Date());
                stutotalityStuResultService.save(result);
                stutotalityCodeService.delete(codeId);
                //获取该学生的班级所对应的科目
                List<ClassTeaching> courseList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, new String[]{stu.getClassId()}),new TR<List<ClassTeaching>>(){});
                Set<String> subjectIds = EntityUtils.getSet(courseList,ClassTeaching::getSubjectId);
                if(StringUtils.isNotBlank(item.getSubjectId()) && subjectIds.contains(item.getSubjectId())){
                    dto.setType("2");
                    //判断拓展课程
                }else if(optionList.size()==1){
                    dto.setType("3");
                }else{
                    dto.setType("1");
                }
                dto.setItemId(item.getId());
                dto.setItemName(item.getItemName());
                dto.setCodeName(code.getName());
                dto.setScore(code.getScore());
            } catch (Exception e) {
                e.printStackTrace();
                dto.setStatus("-1");
                dto.setMsg("录入出错，请再次尝试或联系管理员");
                return dto;
            }
        }
        dto.setStatus("200");
        dto.setMsg("录入成功");
        return dto;
    }
    @Override
    public List<StutotalityResultDto> getStutotalityItemResult(String studentId, String type) {
        List<StutotalityResultDto> resultDtoList=new ArrayList<>();
        Student stu=SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        if(stu!=null){
            StutotalityResultDto resultDto=null;
            StutotalityOptionDto optionDto1=null;
            List<StutotalityOptionDto> optionDtoList1=null;

            String schoolId=stu.getSchoolId();
            Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()), Clazz.class);
            Semester se=SUtils.dc(semesterRemoteService.getCurrentSemester(1,schoolId),Semester.class);
            String acadyear=se.getAcadyear();
            String semester=se.getSemester().toString();
            //日常比例
            List<StutotalityScale> scales2=stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(schoolId,acadyear,semester,clazz.getGradeId(),"2");
            Float scale2=CollectionUtils.isNotEmpty(scales2)?scales2.get(0).getScale():0.0f;
            //期末比例
            List<StutotalityScale> scales3=stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(schoolId,acadyear,semester,clazz.getGradeId(),"3");
            Float scale3=CollectionUtils.isNotEmpty(scales3)?scales3.get(0).getScale():0.0f;
            if("1".equals(type) || "2".equals(type) || "3".equals(type)){
                List<StutotalityItem> itemList=stutotalityItemService.getItemListByParams(schoolId,acadyear,semester,clazz.getGradeId(),1);
                if(CollectionUtils.isNotEmpty(itemList)){
                    Set<String> itemIds=EntityUtils.getSet(itemList,StutotalityItem::getId);
                    //通过项目获取内容
                    List<StutotalityItemOption> optionList=stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
                    Map<String,List<StutotalityItemOption>> itemIdOptionListMap=null;
                    if(CollectionUtils.isNotEmpty(optionList)){
                        itemIdOptionListMap=optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
                    }else{
                        itemIdOptionListMap=new HashMap<>();
                    }
                    //获取该学生的班级所对应的科目
                    List<ClassTeaching> courseList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByClassIds(acadyear, semester, new String[]{stu.getClassId()}),new TR<List<ClassTeaching>>(){});
                    Set<String> subjectIds = EntityUtils.getSet(courseList,ClassTeaching::getSubjectId);
                    //3种项目情况的结果成绩
                    List<StutotalityStuResult> resultList=stutotalityStuResultService.findListByParms(schoolId,acadyear,semester,studentId,itemIds.toArray(new String[0]));
                    Map<String,StutotalityStuResult> optionIdResultMap=new HashMap<>();
                    Map<String,StutotalityStuResult> itemIdResultMap=new HashMap<>();
                    if(CollectionUtils.isNotEmpty(resultList)){
                        for(StutotalityStuResult result:resultList){
                            if(StringUtils.isNotBlank(result.getOptionId())){//内容成绩
                                optionIdResultMap.put(result.getOptionId(),result);
                            } else if("2".equals(result.getType())){//期末
                                itemIdResultMap.put(result.getItemHealthId()+"_2",result);
                            }
                        }
                    }
                    List<StutotalityItem> itemList1=new ArrayList<>();
                    List<StutotalityItem> itemList2=new ArrayList<>();
                    List<StutotalityItem> itemList3=new ArrayList<>();
                    for(StutotalityItem item:itemList){
                        //第一种，判断基础课程
//                        List<StutotalityItemOption> inOptionList=itemIdOptionListMap.get
                        if(StringUtils.isNotBlank(item.getSubjectId()) && subjectIds.contains(item.getSubjectId())){
                            itemList2.add(item);
                            //判断拓展课程
                        }else if(itemIdOptionListMap.containsKey(item.getId()) && itemIdOptionListMap.get(item.getId()).size()==1){
                            itemList3.add(item);
                        }else{//剩余为阳光德育
                            itemList1.add(item);
                        }
                    }
                    //阳光德育
                    if("1".equals(type)){
                        setResultList(scale2,scale3,itemList1,optionIdResultMap,itemIdResultMap,itemIdOptionListMap,resultDtoList);
                    }else if("2".equals(type)){
                        setResultList(scale2,scale3,itemList2,optionIdResultMap,itemIdResultMap,itemIdOptionListMap,resultDtoList);
                    }else{
                        setResultList(scale2,scale3,itemList3,optionIdResultMap,itemIdResultMap,itemIdOptionListMap,resultDtoList);
                    }
                }

            }else if("4".equals(type)){//获奖情况
                List<StutotalityStuReward> stuRewardList = stutotalityStuRewardService.getByAcadyearAndSemesterAndUnitIdAndStudentId
                        (acadyear,semester.toString(),schoolId,studentId);
                if(CollectionUtils.isNotEmpty(stuRewardList)) {
                    Map<String,List<StutotalityStuReward>> listMap=stuRewardList.stream().collect(Collectors.groupingBy(StutotalityStuReward::getRewardId));
                    Set<String> rewardIds = stuRewardList.stream().map(StutotalityStuReward::getRewardId).collect(Collectors.toSet());
                    List<StutotalityReward> rewardList = stutotalityRewardService.findListByIds(rewardIds.toArray(new String[0]));
                    if(CollectionUtils.isNotEmpty(rewardList)){
                        Collections.sort(rewardList, new Comparator<StutotalityReward>() {
                            @Override
                            public int compare(StutotalityReward o1, StutotalityReward o2) {
                                return o2.getStarNumber()-o1.getStarNumber();
                            }
                        });
                        Map<String,StutotalityReward> rewardMap = rewardList.stream().collect(Collectors.toMap(StutotalityReward::getId, Function.identity()));
                        resultDto=new StutotalityResultDto();
                        resultDto.setItemId(BaseConstants.ZERO_GUID);
                        resultDto.setItemName("获奖情况");
                        optionDtoList1=new ArrayList<>();
                        for(StutotalityReward reward:rewardList){
                            if(listMap.containsKey(reward.getId())){
                                for(StutotalityStuReward stuReward:listMap.get(reward.getId())){
                                    optionDto1=new StutotalityOptionDto();
                                    optionDto1.setOptionName(reward.getRewardName());
                                    optionDto1.setScore(Float.valueOf(reward.getStarNumber()));
                                    optionDto1.setTimeStr("获奖于"+DateUtils.date2String(stuReward.getCreationTime(), "yyyy-MM-dd"));
                                    optionDto1.setDescription(stuReward.getDescription());
                                    optionDtoList1.add(optionDto1);
                                }
                            }
                            /*optionDto1=new StutotalityOptionDto();
                            optionDto1.setOptionName(reward.getRewardName());
                            if(listMap.containsKey(reward.getId())){
                                int l=listMap.get(reward.getId()).size()*reward.getStarNumber();
//                                if(l>10) l=10;//暂定不超过10颗星
                                optionDto1.setScore(Float.valueOf(l));
                            }
                            optionDtoList1.add(optionDto1);*/
                        }
                        resultDto.setOptionDtoList1(optionDtoList1);
                        resultDtoList.add(resultDto);
                    }
                }
            }else if("5".equals(type)){//品德行为
                List<StutotalityItem> itemList=stutotalityItemService.getItemListByParams(schoolId,acadyear,semester,clazz.getGradeId(),0);
                Set<String> itemIds=EntityUtils.getSet(itemList,StutotalityItem::getId);
                //通过项目获取内容
                List<StutotalityItemOption> optionList=stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
                Map<String,List<StutotalityItemOption>> itemIdOptionListMap=null;
                if(CollectionUtils.isNotEmpty(optionList)){
                    itemIdOptionListMap=optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
                }
                List<StutotalityStuResult> resultList=stutotalityStuResultService.findListByParms(schoolId,acadyear,semester,studentId,itemIds.toArray(new String[0]));
                Map<String,StutotalityStuResult> optionIdResultMap=EntityUtils.getMap(resultList,StutotalityStuResult::getOptionId);
                if(CollectionUtils.isNotEmpty(itemList)){
                    Float allScore=0.0f;
                    int size=0;
                    for(StutotalityItem item:itemList) {
                        allScore = 0.0f;
                        size = 0;
                        resultDto = new StutotalityResultDto();
                        resultDto.setItemName(item.getItemName());
                        resultDto.setItemId(item.getId());
                        optionDtoList1 = new ArrayList<>();//内容
                        if (itemIdOptionListMap.containsKey(item.getId())) {//取具体的内容
                            for (StutotalityItemOption option : itemIdOptionListMap.get(item.getId())) {
                                optionDto1 = new StutotalityOptionDto();
                                optionDto1.setOptionName(option.getOptionName());
                                if (optionIdResultMap.containsKey(option.getId())) {
                                    optionDto1.setScore(optionIdResultMap.get(option.getId()).getResult());
                                    allScore += optionDto1.getScore();
                                    size++;
                                }
                                optionDtoList1.add(optionDto1);
                            }
                        }
                        optionDto1 = new StutotalityOptionDto();
                        optionDto1.setOptionName("综合水平");
                        BigDecimal b1=new BigDecimal(allScore.toString());
                        if(size!=0) {
                            optionDto1.setScore(b1.divide(new BigDecimal(size), 1, BigDecimal.ROUND_HALF_UP).floatValue());
                        }
                        optionDtoList1.add(optionDto1);
                        resultDto.setOptionDtoList1(optionDtoList1);
                        resultDtoList.add(resultDto);
                    }
                }
            }
        }

        return resultDtoList;
    }
    public void setResultList(Float scale2,Float scale3,List<StutotalityItem> itemList,Map<String,StutotalityStuResult> optionIdResultMap,
                              Map<String,StutotalityStuResult> itemIdResultMap,Map<String,List<StutotalityItemOption>> itemIdOptionListMap,List<StutotalityResultDto> resultDtoList){
        Float allScore=0.0f;
        int size=0;
        StutotalityResultDto resultDto=null;
        StutotalityOptionDto optionDto1=null;
        StutotalityOptionDto optionDto2=null;
        List<StutotalityOptionDto> optionDtoList1=null;
        List<StutotalityOptionDto> optionDtoList2=null;
        for(StutotalityItem item:itemList){
            allScore=0.0f;
            size=0;
            resultDto=new StutotalityResultDto();
            resultDto.setItemName(item.getItemName());
            resultDto.setItemId(item.getId());
            optionDtoList1=new ArrayList<>();//内容
            optionDtoList2=new ArrayList<>();//日常期末综合等
            if(itemIdOptionListMap.containsKey(item.getId())){//取具体的内容
                for(StutotalityItemOption option:itemIdOptionListMap.get(item.getId())){
                    optionDto1=new StutotalityOptionDto();
                    optionDto1.setOptionName(option.getOptionName());
                    if(optionIdResultMap.containsKey(option.getId())){
                        optionDto1.setScore(optionIdResultMap.get(option.getId()).getResult());
                        allScore+=optionDto1.getScore();
                        size++;
                    }
                    optionDtoList1.add(optionDto1);
                }
            }
            resultDto.setOptionDtoList1(optionDtoList1);
            //获取右侧三种情况的结果
            StutotalityStuResult result=itemIdResultMap.get(item.getId()+"_2");
            Float score1=null;
            Float score2=result==null?null:result.getResult();
            optionDto2=new StutotalityOptionDto();
            optionDto2.setOptionName("日常水平");
            BigDecimal b1=new BigDecimal(allScore.toString());
            if(size!=0) {
                score1 = b1.divide(new BigDecimal(size), 1, BigDecimal.ROUND_HALF_UP).floatValue();
                optionDto2.setScore(score1);
            }
            optionDtoList2.add(optionDto2);

            optionDto2=new StutotalityOptionDto();
            optionDto2.setOptionName("期末水平");
            optionDto2.setScore(score2);
            optionDtoList2.add(optionDto2);

            optionDto2=new StutotalityOptionDto();
            optionDto2.setOptionName("综合水平");
            if(score1!=null && score2!=null){
//                new BigDecimal((score1*scale2+score2*scale3)/100).setScale(1,BigDecimal.ROUND_HALF_UP);
                optionDto2.setScore(new BigDecimal((score1*scale2+score2*scale3)/100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue());
            }
            optionDtoList2.add(optionDto2);
            resultDto.setOptionDtoList2(optionDtoList2);

            resultDtoList.add(resultDto);
        }
    }
}
