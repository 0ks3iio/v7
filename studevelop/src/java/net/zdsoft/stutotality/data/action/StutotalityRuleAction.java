package net.zdsoft.stutotality.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stutotality.data.dto.StutotalityDto;
import net.zdsoft.stutotality.data.entity.StutotalityItem;
import net.zdsoft.stutotality.data.entity.StutotalityScale;
import net.zdsoft.stutotality.data.service.StutotalityItemService;
import net.zdsoft.stutotality.data.service.StutotalityScaleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 规则设置
 */
@Controller
@RequestMapping("/stutotality")
public class StutotalityRuleAction extends BaseAction {
    @Autowired
    private StutotalityItemService stutotalityItemService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private StutotalityScaleService stutotalityScaleService;


    @RequestMapping("/rule/index/page")
    @ControllerInfo(value = "规则设置index")
    public String ruleIndex(ModelMap map, String acadyear, String semester,String gradeId) {
        String unitId=getLoginInfo().getUnitId();
        List<String> acadeyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        if(semesterObj != null){
            if(StringUtils.isBlank(acadyear)){
                acadyear = semesterObj.getAcadyear();
                semester  = semesterObj.getSemester()+"";
            }
            List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},acadyear), new TR<List<Grade>>() {});
            if(CollectionUtils.isNotEmpty(gradeList)){
                if(StringUtils.isEmpty(gradeId)){
                    gradeId=gradeList.get(0).getId();
                }
            }
            map.put("gradeList",gradeList);
        }
        map.put("acadyear",acadyear);
        map.put("semester",semester);
        map.put("gradeId",gradeId);
        map.put("acadeyearList",acadeyearList);
        return "/stutotality/rule/stutotalityScaleIndex.ftl";
    }
    /**
     * 规则展示
     */
    @RequestMapping("/rule/show")
    public String ruleShow(ModelMap map,String gradeId, String semester, String acadyear, String gradeCode){
        String unitId = getLoginInfo().getUnitId();
        List<StutotalityScale> scaleList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId,"1");

        if(CollectionUtils.isNotEmpty(scaleList)){
             List<StutotalityItem> items = stutotalityItemService.getItemListByParams(unitId,acadyear,semester,gradeId,1);
            for (StutotalityItem item : items) {
                for (StutotalityScale scale : scaleList) {
                    if(StringUtils.isNotBlank(scale.getItemId())){
                        if(scale.getItemId().equals(item.getId())){
                            item.setScale(scale.getScale());
                        }
                    }
                }
            }
            //日常
            List<StutotalityScale> dailyList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId,"2");
            if(CollectionUtils.isNotEmpty(dailyList)){
                StutotalityScale dailySacle = dailyList.get(0);
                map.put("dailySacle",dailySacle);
            }

            //期末
            List<StutotalityScale> termList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId,"3");
            if(CollectionUtils.isNotEmpty(termList)){
                StutotalityScale termScale = termList.get(0);
                Float a =  termScale.getScale();
                map.put("termScale",termScale);
            }
            map.put("itemScaleList",items);
        }
        String beforeSemester=null;
        String beforeAcadyear=null;
        List<StutotalityScale>  scales = null;
        if("1".equals(semester)){
            beforeSemester="2";
            String[] ss=acadyear.split("-");
            beforeAcadyear=(Integer.parseInt(ss[0])-1)+"-"+(Integer.parseInt(ss[1])-1);
            String beforeGradeId=EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},beforeAcadyear),Grade.class)
                    ,Grade::getGradeCode,Grade::getId).get(gradeCode);
            scales = stutotalityScaleService.findByUnitIdAndGradeId(unitId, beforeAcadyear, beforeSemester, beforeGradeId);
        }else{
            beforeSemester="1";
            scales   = stutotalityScaleService.findByUnitIdAndGradeId(unitId,acadyear,beforeSemester,gradeId);
        }
        // 0 是可同步 1 不可同步
        if(CollectionUtils.isEmpty(scales)){
            map.put("canSync",1);
        }else{
            map.put("canSync",0);
        }
        return  "/stutotality/rule/stutotalityScaleShow.ftl";
    }

    @RequestMapping("/rule/modify")
    public String ruleMofify(ModelMap map,String gradeId, String semeter, String acadyear){
        String unitId = getLoginInfo().getUnitId();
        //学科
        List<StutotalityScale> scaleList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, gradeId, semeter, acadyear,"1");
        if(CollectionUtils.isNotEmpty(scaleList)){
            Set<String> sacleIds=EntityUtils.getSet(scaleList,StutotalityScale::getId);
            List<StutotalityItem> items = stutotalityItemService.getListByIds(sacleIds.toArray(new String[0]));
            for (StutotalityItem item : items) {
                for (StutotalityScale scale : scaleList) {
                    if(scale.getItemId().equals(item.getId())){
                        item.setScale(scale.getScale());
                    }
                }
            }
            //日常
            List<StutotalityScale> dailyList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, gradeId, semeter, acadyear,"2");
            if(CollectionUtils.isNotEmpty(dailyList)){
                StutotalityScale dailySacle = dailyList.get(0);
                map.put("dailySacle",dailySacle);
            }
            //期末
            List<StutotalityScale> termList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, gradeId, semeter, acadyear,"3");
            if(CollectionUtils.isNotEmpty(termList)){
                StutotalityScale termScale = termList.get(0);
                map.put("termScale",termScale);
            }
            map.put("itemScaleList",items);
        }
        return  "/stutotality/rule/stutotalityScaleSave.ftl";
    }

    /**
     * 新增规则
     */
    @RequestMapping("rule/add/index")
    @ControllerInfo(value = "新建规则index")
    public String ruleAdd(ModelMap map, String gradeId, String semester, String acadyear) {
        String unitId=getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},acadyear), new TR<List<Grade>>() {});

        //排除gradeId为空的情况
        if(CollectionUtils.isNotEmpty(gradeList)){
            if(StringUtils.isEmpty(gradeId)){
                gradeId=gradeList.get(0).getId();
            }
        }
        map.put("semester",semester);
        map.put("acadyear",acadyear);
        map.put("gradeId",gradeId);
        map.put("gradeList",gradeList);
        return  "/stutotality/rule/stutotalityScaleSave.ftl";
    }
    /**
     * 编辑规则
     */
    @RequestMapping("rule/item/save")
    public String rulemodify(ModelMap map, String gradeId,String acadyear, String semester) {
        String unitId = getLoginInfo().getUnitId();
        List<StutotalityItem> items = stutotalityItemService.getItemListByParams(unitId,acadyear,semester,gradeId,1);
        if(CollectionUtils.isNotEmpty(items)){
            List<StutotalityScale> scaleList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId,"1");
            for (StutotalityItem item : items) {
                for (StutotalityScale scale : scaleList) {
                    if(scale.getItemId().equals(item.getId())){
                        item.setScale(scale.getScale());
                        item.setScaleId(scale.getId());
                    }
                }
            }
            //日常
            List<StutotalityScale> dailyList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId,"2");
            if(CollectionUtils.isNotEmpty(dailyList)){
                StutotalityScale dailySacle = dailyList.get(0);
                map.put("dailySacle",dailySacle);
            }

            //期末
            List<StutotalityScale> termList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId,"3");
            if(CollectionUtils.isNotEmpty(termList)){
                StutotalityScale termScale = termList.get(0);
                map.put("termScale",termScale);
            }
            map.put("itemList",items);
        }
        return "/stutotality/rule/stutotalityScaleNewModify.ftl";
    }


    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping("rule/add")
    public String ruleAdd(StutotalityDto stutotalityDto, String gradeId, String acadyear, String semester){
        String unitId=getLoginInfo().getUnitId();
        List<StutotalityScale> scaleList = stutotalityDto.getStutotalityScaleList();
        try {
            for (StutotalityScale scale : scaleList) {
                scale.setId(UuidUtils.generateUuid());
                scale.setUnitId(unitId);
                scale.setAcadyear(acadyear);
                scale.setSemester(semester);
                scale.setGradeId(gradeId);
                scale.setCreationTime(new Date());
                scale.setModifyTime(new Date());
                if(scale.getScale()==null||scale.getScale().equals("")){
                    scale.setScale(0.f);
                }
            }
            stutotalityScaleService.deleteByUnitIdAndAcadyearAndSemesterAndGradeId(unitId,acadyear,semester,gradeId);
            stutotalityScaleService.saveAll(scaleList.toArray(new StutotalityScale[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("保存成功");
    }
    /**
     * 同步上届数据
     */
    @ResponseBody
    @RequestMapping("rule/sync")
    public String syncRule(ModelMap map, String acadyear ,String semester ,String gradeCode, String gradeId){
        try{
            String unitId=getLoginInfo().getUnitId();
            List<StutotalityScale> sacleList = stutotalityScaleService.findByUnitIdAndGradeIdAndAcadyearAndSemester(unitId, acadyear, semester, gradeId, "1");
             List<StutotalityScale> beforeSacleList = null;
            if(CollectionUtils.isEmpty(sacleList)){
                String beforeSemester=null;
                String beforeAcadyear=null;
                if("1".equals(semester)){
                    beforeSemester="2";
                    String[] ss=acadyear.split("-");
                    beforeAcadyear=(Integer.parseInt(ss[0])-1)+"-"+(Integer.parseInt(ss[1])-1);
                    String beforeGradeId=EntityUtils.getMap(SUtils.dt(gradeRemoteService
                                    .findByUnitIdAndGradeCode(unitId,new Integer[]{BaseConstants.SECTION_PRIMARY},beforeAcadyear),Grade.class)
                            ,Grade::getGradeCode,Grade::getId).get(gradeCode);
                    beforeSacleList = stutotalityScaleService.findByUnitIdAndGradeId(unitId,beforeAcadyear,beforeSemester,beforeGradeId);
                }else{
                    beforeSemester="1";
                    beforeSacleList = stutotalityScaleService.findByUnitIdAndGradeId(unitId,acadyear,beforeSemester,gradeId);
                }
                //根据itemName 来匹配itemId
                Map<String,String> itemMap = new HashMap();
                List<StutotalityItem> itemList= stutotalityItemService.getItemListByParams(unitId, acadyear, semester, gradeId, 1);
                if(CollectionUtils.isEmpty(itemList)){
                    return error("请先维护当前评价项设置");
                }
/*                if(CollectionUtils.isEmpty(beforeSacleList)){
                    return error("暂无上届数据");
                }*/
                for (StutotalityItem item : itemList) {
                    itemMap.put(item.getItemName(),item.getId());
                }
                int i = 1;
                for (StutotalityScale scale : beforeSacleList) {
                        scale.setId(UuidUtils.generateUuid());
                        scale.setUnitId(unitId);
                        scale.setAcadyear(acadyear);
                        scale.setSemester(semester);
                        scale.setGradeId(gradeId);
                    if(StringUtils.isNotBlank(scale.getItemId())){
                        StutotalityItem one = stutotalityItemService.findOne(scale.getItemId());
                        scale.setItemId(itemMap.get(one.getItemName()));
                    }
                }
                stutotalityScaleService.saveAll(beforeSacleList.toArray(new StutotalityScale[0]));
            }
        }catch (Exception e){
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
}
