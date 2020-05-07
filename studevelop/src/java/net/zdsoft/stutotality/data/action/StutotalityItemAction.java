package net.zdsoft.stutotality.data.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stutotality.data.dto.StutoSearchDto;
import net.zdsoft.stutotality.data.dto.StutotalityDto;
import net.zdsoft.stutotality.data.entity.StutotalityItem;
import net.zdsoft.stutotality.data.entity.StutotalityItemOption;
import net.zdsoft.stutotality.data.entity.StutotalityType;
import net.zdsoft.stutotality.data.service.StutotalityItemOptionService;
import net.zdsoft.stutotality.data.service.StutotalityItemService;
import net.zdsoft.stutotality.data.service.StutotalityTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 评价项目设置
 */

@Controller
@RequestMapping("/stutotality")
public class StutotalityItemAction extends BaseAction {
    @Autowired
    private StutotalityTypeService stutotalityTypeService;
    @Autowired
    private StutotalityItemService stutotalityItemService;
    @Autowired
    private StutotalityItemOptionService stutotalityItemOptionService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;

    /**
     * 初始同步单位模板-一个单位只同步一次
     */
    @RequestMapping("/item/index/page")
    @ControllerInfo(value = "评价项设置index")
    public String itemIndex(ModelMap map, HttpServletRequest request) {
        String unitId=getLoginInfo().getUnitId();
        List<StutotalityType> templateTypes = stutotalityTypeService.findByUnitIdGradeId(unitId,BaseConstants.ZERO_GUID);
        if(CollectionUtils.isEmpty(templateTypes)){//初始化开始
            try {
                List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection
                        (unitId, BaseConstants.SUBJECT_TYPE_BX,BaseConstants.SECTION_PRIMARY.toString()), Course.class);
                Map<String,String> subjectNameMap=EntityUtils.getMap(courseList,Course::getSubjectName,Course::getId);
                Set<String> subjectNames=subjectNameMap.keySet();
                //获取类别对应的项目
                List<StutotalityType> types = stutotalityTypeService.findByUnitId(BaseConstants.ZERO_GUID);
                Set<String> typeIds=EntityUtils.getSet(types,StutotalityType::getId);
                List<StutotalityItem> allItemList = stutotalityItemService.getListByTypeIds(typeIds.toArray(new String[0]));
                Set<String> itemIds=EntityUtils.getSet(allItemList,StutotalityItem::getId);
                //获取项目对应的内容
                List<StutotalityItemOption> allOptionist = stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));
                Map<String,List<StutotalityItem>> itemsMap=allItemList.stream().collect(Collectors.groupingBy(StutotalityItem::getTypeId));
                Map<String,List<StutotalityItemOption>> optionsMap=allOptionist.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
                List<StutotalityItem> itemInsertList=new ArrayList<>();
                List<StutotalityItemOption> optionInsertList=new ArrayList<>();
                for (StutotalityType type : types) {
                    String typeUuid = UuidUtils.generateUuid();
                    List<StutotalityItem> items = itemsMap.get(type.getId());
                    for (StutotalityItem item : items) {
                        String itemUuid = UuidUtils.generateUuid();
                        List<StutotalityItemOption> options = optionsMap.get(item.getId());
                        if(CollectionUtils.isNotEmpty(options)){
                            for (StutotalityItemOption option : options) {
                                option.setId(UuidUtils.generateUuid());
                                option.setUnitId(unitId);
                                option.setItemId(itemUuid);
                                option.setCreationTime(new Date());
                                option.setModifyTime(new Date());
                                optionInsertList.add(option);
                            }
                        }
                        item.setId(itemUuid);
                        item.setUnitId(unitId);
                        item.setGradeId(BaseConstants.ZERO_GUID);
                        item.setTypeId(typeUuid);
                        item.setCreationTime(new Date());
                        item.setModifyTime(new Date());
                        for(String subjectName:subjectNames){//匹配科目名称
                            String subjectId=subjectNameMap.get(subjectName);
                            if(subjectName.equals(item.getItemName())){//&& courseIds.contains(subjectId)
                                item.setSubjectId(subjectId);
                                item.setSubjectType("2");
                                break;
                            }
                        }
                        itemInsertList.add(item);
                    }
                    type.setId(typeUuid);
                    type.setUnitId(unitId);
                    type.setGradeId(BaseConstants.ZERO_GUID);
                    type.setCreationTime(new Date());
                    type.setModifyTime(new Date());
                }
                stutotalityTypeService.saveAll(types.toArray(new StutotalityType[0]));
                stutotalityItemService.saveAll(itemInsertList.toArray(new StutotalityItem[0]));
                stutotalityItemOptionService.saveAll(optionInsertList.toArray(new StutotalityItemOption[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("acadyear",request.getParameter("acadyear"));
        map.put("semester",request.getParameter("semester"));
        map.put("gradeId",request.getParameter("gradeId"));
        return "/stutotality/item/stutotalityItemIndex.ftl";
    }

    /**
     * 评价项目查询列
     */
    @RequestMapping("/item/show/index")
    public String itemTableIndex(ModelMap map, String acadyear,String semester,String gradeId) {
        String unitId=getLoginInfo().getUnitId();
        List<String> acadeyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        if(semesterObj != null){
            if(StringUtils.isBlank(acadyear)){
                acadyear = semesterObj.getAcadyear();
            }
            if(StringUtils.isBlank(semester)){
                semester = semesterObj.getSemester().toString();
            }
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},acadyear), new TR<List<Grade>>() {});
            if(CollectionUtils.isNotEmpty(gradeList)){
                if(!(EntityUtils.getSet(gradeList,Grade::getId).contains(gradeId))){
                    gradeId=gradeList.get(0).getId();
                }
            }
            map.put("gradeList",gradeList);
        }
        map.put("acadyear",acadyear);
        map.put("gradeId",gradeId);
        map.put("semester",semester);
        map.put("acadeyearList",acadeyearList);
        return "/stutotality/item/stutotalityItemShow.ftl";
    }
    /**
     * 评价项目-针对年级的预览表
     */
    @RequestMapping("/item/tableShow")
    public String templateIndex(ModelMap map ,HttpServletRequest request, String acadyear, String semester, String gradeId, String gradeName, String gradeCode) {
        String unitId=getLoginInfo().getUnitId();
        //这里只有对应年级 学年学期的数据
        List<StutotalityType>  typesList = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeIdWithMaster(unitId,acadyear,semester,gradeId);
        if(CollectionUtils.isNotEmpty(typesList)){
            //得到所有的typeIds
            Set<String> typeIds=EntityUtils.getSet(typesList,StutotalityType::getId);
            //得到所有的项目items
            List<StutotalityItem> itemList=stutotalityItemService.getListByTypeIdsWithMaster(typeIds.toArray(new String[0]));
            Set<String> itemIds=EntityUtils.getSet(itemList, StutotalityItem::getId);
            if(CollectionUtils.isNotEmpty(itemIds)){
                //得到所有的内容options
                List<StutotalityItemOption> optionList=stutotalityItemOptionService.findByItemIdsWithMaster(itemIds.toArray(new String[0]));
                Map<String,List<StutotalityItem>> typeIdItemMap=new HashMap<>();
                if(CollectionUtils.isNotEmpty(itemList)){
                    typeIdItemMap=itemList.stream().collect(Collectors.groupingBy(StutotalityItem::getTypeId));
                }
                Map<String,List<StutotalityItemOption>> itemIdItemMap=new HashMap<>();
                if(CollectionUtils.isNotEmpty(optionList)){
                    itemIdItemMap=optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
                }
                for (StutotalityType type : typesList) {
                    List<StutotalityItem> inItemList=typeIdItemMap.get(type.getId());
                    if(CollectionUtils.isNotEmpty(inItemList)){
                        type.setStutotalityItems(inItemList);
                        for (StutotalityItem item : inItemList) {
                            item.setStutotalityItemOptions(itemIdItemMap.get(item.getId()));
                        }
                    }
                }
            }
        }
        // 当前学年 和 传入的学年 比较来判断是否可编辑
        String nowadadyear = null;
       Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        if(semesterObj != null){
            nowadadyear = semesterObj.getAcadyear();
        }
        String[] s0=nowadadyear.split("-");
        //当前学年数
        Integer num0 = (Integer.parseInt(s0[0]))+(Integer.parseInt(s0[1]));
        String[] s1=acadyear.split("-");
        //要比较的学年数
        Integer num1 = (Integer.parseInt(s1[0]))+(Integer.parseInt(s1[1]));
        // 0 是可编辑 1 不可编辑
        if(num1<num0){
            map.put("canSave",1);
        }else{
            map.put("canSave",0);
        }
        String beforeSemester=null;
        String beforeAcadyear=null;
        List<StutotalityType>  types = null;
        if("1".equals(semester)){
            beforeSemester="2";
            String[] ss=acadyear.split("-");
            beforeAcadyear=(Integer.parseInt(ss[0])-1)+"-"+(Integer.parseInt(ss[1])-1);
            String beforeGradeId=EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},beforeAcadyear),Grade.class)
                    ,Grade::getGradeCode,Grade::getId).get(gradeCode);
            types = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId, beforeAcadyear, beforeSemester, beforeGradeId);

        }else{
            beforeSemester="1";
            types    = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeIdWithMaster(unitId,acadyear,beforeSemester,gradeId);
        }
        // 0 是可同步 1 不可同步
        if(CollectionUtils.isEmpty(types)){
            map.put("canSync",1);
        }else{
            map.put("canSync",0);
        }
        map.put("acadyear",acadyear);
        map.put("gradeName",gradeName);
        map.put("semester",semester);
        map.put("listStutotality",typesList);
        return "/stutotality/item/stutotalityItemTable.ftl";
    }
    /**
     * 同步评价项模板
     * 	private String acadyear;
     */
    @RequestMapping("/template/sync")
    @ResponseBody
    public String syncTemplate(ModelMap map, StutoSearchDto dto){
        String unitId=getLoginInfo().getUnitId();
        String acadyear=dto.getAcadyear();
        String semester=dto.getSemester();
        String gradeId=dto.getGradeId();
        try {
            List<StutotalityType> typeList = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId, acadyear, semester, gradeId);
            if(CollectionUtils.isEmpty(typeList)) {
                List<StutotalityType> types =null;
                if("before".equals(dto.getSyncType())){//同步上届数据
                    String beforeSemester=null;
                    String beforeAcadyear=null;
                    if("1".equals(semester)){
                        beforeSemester="2";
                        String[] ss=acadyear.split("-");
                        beforeAcadyear=(Integer.parseInt(ss[0])-1)+"-"+(Integer.parseInt(ss[1])-1);
                        String beforeGradeId=EntityUtils.getMap(SUtils.dt(gradeRemoteService
                                .findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},beforeAcadyear),Grade.class)
                                ,Grade::getGradeCode,Grade::getId).get(dto.getGradeCode());
                        types = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId, beforeAcadyear, beforeSemester, beforeGradeId);
                    }else{
                        beforeSemester="1";
                        types = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId, acadyear, beforeSemester, gradeId);
                    }
/*                    if(CollectionUtils.isEmpty(types)){
                        return error("暂无上届数据");
                    }*/
                }else{
                    types = stutotalityTypeService.findByUnitIdGradeId(unitId, BaseConstants.ZERO_GUID);
                }
                Set<String> typeIds=EntityUtils.getSet(types,StutotalityType::getId);
                List<StutotalityItem> allItemList = stutotalityItemService.getListByTypeIds(typeIds.toArray(new String[0]));
                Set<String> itemIds=EntityUtils.getSet(allItemList,StutotalityItem::getId);
                List<StutotalityItemOption> allOptionist = stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0]));

                Map<String,List<StutotalityItem>> itemsMap=allItemList.stream().collect(Collectors.groupingBy(StutotalityItem::getTypeId));
                Map<String,List<StutotalityItemOption>> optionsMap=allOptionist.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
                List<StutotalityItem> itemInsertList=new ArrayList<>();
                List<StutotalityItemOption> optionInsertList=new ArrayList<>();

                for (StutotalityType type : types) {
                    String typeUuid = UuidUtils.generateUuid();
                    List<StutotalityItem> items = itemsMap.get(type.getId());
                    if(CollectionUtils.isNotEmpty(items)){
                        for (StutotalityItem item : items) {
                            String itemUuid = UuidUtils.generateUuid();
                            List<StutotalityItemOption> options = optionsMap.get(item.getId());
                            if(CollectionUtils.isNotEmpty(options)){
                                for (StutotalityItemOption option : options) {
                                    option.setId(UuidUtils.generateUuid());
                                    option.setItemId(itemUuid);
                                    option.setCreationTime(new Date());
                                    option.setModifyTime(new Date());
                                    optionInsertList.add(option);
                                }
                            }
                            item.setId(itemUuid);
                            item.setGradeId(gradeId);
                            item.setSemester(semester);
                            item.setAcadyear(acadyear);
                            item.setTypeId(typeUuid);
                            item.setCreationTime(new Date());
                            item.setModifyTime(new Date());
                            itemInsertList.add(item);
                        }
                    }
                    type.setId(typeUuid);
                    type.setGradeId(gradeId);
                    type.setAcadyear(acadyear);
                    type.setSemester(semester);
                    type.setCreationTime(new Date());
                    type.setModifyTime(new Date());
                }
                stutotalityTypeService.saveAll(types.toArray(new StutotalityType[0]));
                stutotalityItemService.saveAll(itemInsertList.toArray(new StutotalityItem[0]));
                stutotalityItemOptionService.saveAll(optionInsertList.toArray(new StutotalityItemOption[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }


    /**
     * type左侧类型展示
     */
    @RequestMapping("typetemplate/show")
    public String showTypeTemplate(ModelMap map, String acadyear, String semester, String gradeId, String gradeName){

        String unitId=getLoginInfo().getUnitId();
        List<StutotalityType> typesList = null;
        if(StringUtils.isNotBlank(acadyear) && StringUtils.isNotBlank(semester)){
            typesList = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeIdWithMaster(unitId,acadyear,semester,gradeId);
        }else {
            typesList =  stutotalityTypeService.findByUnitIdGradeIdWithMaster(unitId,BaseConstants.ZERO_GUID);
        }
        if(CollectionUtils.isNotEmpty(typesList)){
            map.put("typeId",typesList.get(0).getId());
        }
        map.put("listStutotality",typesList);
        map.put("gradeId",gradeId);
        map.put("acadyear",acadyear);
        map.put("gradeName",gradeName);
        map.put("semester",semester);
        return "/stutotality/item/stutotalityTypeShow.ftl";
    }

    /**
     * 项目及内容展示
     */
    @RequestMapping("/itemtemplate/show")
    public String  getItemtemplate(String typeId, ModelMap map, String gradeName){
        if (StringUtils.isNotBlank(typeId)){
            StutotalityType type = stutotalityTypeService.findOne(typeId);
            String gradeId = type.getGradeId();
            if(gradeId.equals(BaseConstants.ZERO_GUID)){
                map.put("buttonName",0);  //0是模板预览
            }else {
                Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
                map.put("buttonName",1);  //1 是项目预览
                map.put("acadyear",type.getAcadyear());
                map.put("gradeName",gradeName);
                map.put("semester",type.getSemester());
            }
            List<StutotalityItem> itemList = stutotalityItemService.getListByTypeIdsWithMaster(new String[]{typeId});
            Set<String> itemIds=EntityUtils.getSet(itemList,StutotalityItem::getId);
            if(CollectionUtils.isNotEmpty(itemIds)){
                List<StutotalityItemOption> optionList=stutotalityItemOptionService.findByItemIdsWithMaster(itemIds.toArray(new String[0]));
                Map<String,List<StutotalityItemOption>> optionListMap=optionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
                for (StutotalityItem item : itemList) {
                    item.setStutotalityItemOptions(optionListMap.get(item.getId()));
                }
            }
            map.put("listByTypeId",itemList);
            map.put("hasStat",type.getHasStat());
        }

        return "/stutotality/item/stutotalityTemplate.ftl";
    }
    /**
     * 预览窗口展示
     */
    @ResponseBody
    @RequestMapping("/item/showyu")
    public List<StutotalityItem> getItemtemplate(String typeId){
        List<StutotalityItem> listByTypeId = stutotalityItemService.getListByTypeId(typeId);
        for (StutotalityItem item : listByTypeId) {
            List<StutotalityItemOption> options = stutotalityItemOptionService.getListByItemId(item.getId());
            item.setStutotalityItemOptions(options);
        }
        return listByTypeId;
    }

    /**
     * 保存type类别
     */
    @ResponseBody
    @RequestMapping("type/save")
    public String saveType(ModelMap map,String acadyear,String semester,String gradeId, StutotalityDto stutotalityDto){
        String unitId=getLoginInfo().getUnitId();
        String userId=getLoginInfo().getUserId();
        List<StutotalityType> stutotalityTypeList = stutotalityDto.getStutotalityTypeList();
        try {
            if(StringUtils.isBlank(gradeId)){
                gradeId=BaseConstants.ZERO_GUID;
            }
            Set<String> typeIds= EntityUtils.getSet(stutotalityTypeList, StutotalityType::getId);
            Map<String,StutotalityType> typeMap=null;
            if(CollectionUtils.isNotEmpty(typeIds)){
                typeMap=EntityUtils.getMap(stutotalityTypeService.findListByIdIn(typeIds.toArray(new String[0])), StutotalityType::getId);
            }else {
                typeMap=new HashMap<>();
            }
            List<StutotalityType> insertList=new ArrayList<>();
            int i=1;
            StutotalityType oldType=null;
            int hasStat=0;
            if(CollectionUtils.isNotEmpty(stutotalityTypeList)){
                for(StutotalityType type:stutotalityTypeList){
                    hasStat=0;
                    if(StringUtils.isBlank(type.getTypeName())){
                        continue;
                    }
                    if(StringUtils.equals(type.getHaveOn(),"on")){
                        hasStat=1;
                    }
                    if(typeMap.containsKey(type.getId())){
                        oldType=typeMap.get(type.getId());
                        oldType.setTypeName(type.getTypeName());
                        oldType.setHasStat(hasStat);
                        oldType.setOrderNumber(i);
                        oldType.setModifyTime(new Date());
                        oldType.setModifyId(userId);
                        insertList.add(oldType);
                    }else{
                        type.setId(UuidUtils.generateUuid());
                        type.setUnitId(unitId);
                        type.setAcadyear(acadyear);
                        type.setSemester(semester);
                        type.setGradeId(gradeId);
                        type.setOrderNumber(i);
                        type.setHasStat(hasStat);
                        type.setCreationTime(new Date());
                        type.setModifyTime(new Date());
                        insertList.add(type);
                    }
                    i++;
                }
            }
            // 拿到保存前的旧数据
            List<StutotalityType> typeList = null;
            Set<String> oldTypeIds = null;
            if(StringUtils.isNotBlank(acadyear)){
                typeList = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId,acadyear,semester,gradeId);
                oldTypeIds = EntityUtils.getSet(typeList, StutotalityType::getId);
                stutotalityTypeService.deleteByCons(unitId,acadyear,semester,gradeId);
            }else{
                typeList = stutotalityTypeService.findByUnitIdAndAcadyearAndSemesterAndGradeId(unitId,null,null,gradeId);
                oldTypeIds = EntityUtils.getSet(typeList, StutotalityType::getId);
                stutotalityTypeService.deleteByUnitIdAndGradeId(unitId,gradeId);
            }
                //拿到原有类别被删除的部分
            Set<String> deleteTypeIds=new HashSet<>();
            for(String oldTypeId:oldTypeIds){
                if(!typeIds.contains(oldTypeId)){
                    deleteTypeIds.add(oldTypeId);
                }
            }
            if(CollectionUtils.isNotEmpty(deleteTypeIds)){
                // 拿到原有类别下的 二级三级脏数据
                List<StutotalityItem> items = stutotalityItemService.getListByTypeIds(deleteTypeIds.toArray(new String[0]));
                if(CollectionUtils.isNotEmpty(items)){
                    Set<String>  itemIds = EntityUtils.getSet(items, StutotalityItem::getId);
                    stutotalityItemService.deleteByIds(itemIds.toArray(new String[0]));
                    stutotalityItemOptionService.deleteByUnitIdAndItemId(unitId,itemIds.toArray(new String[0]));
                }
            }
            stutotalityTypeService.saveAll(insertList.toArray(new StutotalityType[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("保存成功");
    }

    /**
     *  复选框删除项目
     */
    @ResponseBody
    @RequestMapping("/item/deleteItems")
    public String removeItems(String itemIds){
        String unitId=getLoginInfo().getUnitId();
        try {
            String[] itemIdArr=itemIds.split(",");
            stutotalityItemService.deleteByIds(itemIdArr);
            stutotalityItemOptionService.deleteByUnitIdAndItemId(unitId,itemIdArr);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    /**
     * 弹窗新增项目
     */
    @ResponseBody
    @RequestMapping("/item/add")
    public String  addItemOption(ModelMap map,  StutotalityDto stutotalityDto){
        List<StutotalityItemOption> opttionList=stutotalityDto.getOptionList();
        String itemName=stutotalityDto.getItemName();
        String subjeectId=stutotalityDto.getSubjectId();
        String subjectType=stutotalityDto.getSubjectType();
        String typeId=stutotalityDto.getTypeId();
        String unitId=getLoginInfo().getUnitId();
        int orderNumber=stutotalityDto.getOrderNumber();
        try {
            StutotalityType type=stutotalityTypeService.findOne(typeId);
            //保存项目
            StutotalityItem item=new StutotalityItem();
            item.setId(UuidUtils.generateUuid());
            if(StringUtils.isBlank(type.getGradeId())){
                item.setGradeId(BaseConstants.ZERO_GUID);
            }else{
                item.setGradeId(type.getGradeId());
            }
            item.setTypeId(typeId);
            item.setAcadyear(type.getAcadyear());
            item.setSemester(type.getSemester());
            item.setUnitId(unitId);
            item.setItemName(itemName);
            item.setSubjectType(subjectType);
            item.setSubjectId(subjeectId);
            item.setOrderNumber(orderNumber+1);
            item.setCreationTime(new Date());
            item.setModifyTime(new Date());
            stutotalityItemService.save(item);
            //保存内容
            int num = 1;
            if(CollectionUtils.isNotEmpty(opttionList)){
                List<StutotalityItemOption> insertList=new ArrayList<>();
                for (StutotalityItemOption option : opttionList) {
                    if(StringUtils.isBlank(option.getOptionName())){
                        continue;
                    }
                    option.setId(UuidUtils.generateUuid());
                    option.setUnitId(unitId);
                    option.setItemId(item.getId());
                    option.setOrderNumber(num);
                    option.setCreationTime(new Date());
                    option.setModifyTime(new Date());
                    insertList.add(option);
                    num++;
                }
                stutotalityItemOptionService.saveAll(insertList.toArray(new StutotalityItemOption[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }


    /**
     * 保存项目名及新增内容操作
     */
    @ResponseBody
    @RequestMapping("/item/save")
    public String modifyItemOption(ModelMap map, StutotalityDto stutotalityDto){
        String typeId = stutotalityDto.getTypeId();
        StutotalityType type = stutotalityTypeService.findOne(typeId);
        String unitId = type.getUnitId();
        String acadyear = type.getAcadyear();
        String semester = type.getSemester();
        String gradeId = type.getGradeId();
        List<StutotalityItem> items = stutotalityDto.getStutotalityItemList();
        if(CollectionUtils.isEmpty(items)){
            return error("请新增项目!");
        }
        for (StutotalityItem item : items) {
            item.setTypeId(typeId);
            item.setUnitId(unitId);
            item.setAcadyear(acadyear);
            item.setSemester(semester);
            item.setGradeId(gradeId);
            if(item.getCreationTime()==null){
                item.setCreationTime(new Date());
            }
            item.setModifyTime(new Date());
        }
        List<StutotalityItemOption> options = stutotalityDto.getOptionList();
        try {
            List<StutotalityItemOption> lastOptionList=new ArrayList<>();
            for (StutotalityItemOption option : options) {
                if(StringUtils.isBlank(option.getOptionName())){
                    continue;
                }
                lastOptionList.add(option);
            }
            Map<String,List<StutotalityItemOption>> optionListMap=lastOptionList.stream().collect(Collectors.groupingBy(StutotalityItemOption::getItemId));
            Set<String> itemIds=EntityUtils.getSet(items,StutotalityItem::getId);
//            Map<String,StutotalityItemOption> oldOptionMap=EntityUtils.getMap(stutotalityItemOptionService.findByItemIds(itemIds.toArray(new String[0])),StutotalityItemOption::getId);
            options=new ArrayList<>();
            for (StutotalityItem item : items) {
                int i = 1;
                List<StutotalityItemOption> inOptionList=optionListMap.get(item.getId());
                if(CollectionUtils.isNotEmpty(inOptionList)){
                    for (StutotalityItemOption option : inOptionList) {
                        if(StringUtils.isBlank(option.getId())){
                            option.setId(UuidUtils.generateUuid());
                        }
                        option.setUnitId(unitId);
                        option.setOrderNumber(i);
                        option.setCreationTime(new Date());
                        option.setModifyTime(new Date());
                        options.add(option);
                        i++;
                    }
                }
            }
            stutotalityItemOptionService.deleteByUnitIdAndItemId(unitId,itemIds.toArray(new String[0]));
            stutotalityItemOptionService.saveAll(options.toArray(new StutotalityItemOption[0]));
            stutotalityItemService.saveAll(items.toArray(new StutotalityItem[0]));

        }catch (Exception e){
            e.printStackTrace();
            return returnError();
        }
        return success("保存成功");
    }

    /**
     * 点击查看项目内容的标准
     */
    @ResponseBody
    @RequestMapping("optiondescription/show")
    public StutotalityItemOption getStutotalityById(String id){
        StutotalityItemOption stutotalityOptionById = stutotalityItemOptionService.findOne(id);
        return stutotalityOptionById;
    }
    /**
     *确定修改项目内容的标准
     */
    @ResponseBody
    @RequestMapping("optiondescription/save")
    public void saveStutotalityById(StutotalityItemOption stutotalityItemOption){
        if(stutotalityItemOption.getOptionName() != null){
            StutotalityItemOption stutotalityOptionById = stutotalityItemOptionService.findOne(stutotalityItemOption.getId());
            stutotalityOptionById.setDescription(stutotalityItemOption.getDescription());
            stutotalityItemOptionService.saveStutotalityItemOption(stutotalityOptionById);
        }else {
            stutotalityItemOption.setUnitId(getLoginInfo().getUnitId());
            stutotalityItemOption.setCreationTime(new Date());
            stutotalityItemOptionService.saveStutotalityItemOption(stutotalityItemOption);
        }
    }
    /**
     * 删除小项目
     */
   /* @ResponseBody
    @RequestMapping("/option/remove")
    public String removeStutotalityById(ModelMap map, String id, String typeId){
        stutotalityItemOptionService.removeStutotalityById(id);
        List<StutotalityItem> listByTypeId = stutotalityItemService.getListByTypeId(typeId);
        for (StutotalityItem item : listByTypeId) {
            List<StutotalityItemOption> listByItemId = stutotalityItemOptionService.getListByItemId(item.getId());
            item.setStutotalityItemOptions(listByItemId);
        }
        map.put("listByTypeId",listByTypeId);
        return "/stutotality/item/stutotalityTemplate.ftl";

    }*/
    /**
     * 点击科目 查询出学科
     */
    @ResponseBody
    @RequestMapping("/subjectShow")
    public String subjectShow(ModelMap map,String buttonName,String acadyear,String semester,String gradeId){
        JSONObject json=new JSONObject();
        String unitId=getLoginInfo().getUnitId();
        JSONArray array=new JSONArray();
        JSONObject ob=null;
        List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection
                (unitId, BaseConstants.SUBJECT_TYPE_BX,BaseConstants.SECTION_PRIMARY.toString()), Course.class);
        for(Course c:courseList){
            ob=new JSONObject();
            ob.put("id",c.getId());
            ob.put("subjectName",c.getSubjectName());
            array.add(ob);
        }

        json.put("courseList",array);
        return json.toJSONString();
    }

}
