package net.zdsoft.studevelop.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StudevelopPerformItemCodeDto;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;
import net.zdsoft.studevelop.data.service.StuDevelopPerformItemCodeService;
import net.zdsoft.studevelop.data.service.StudevelopPerformItemService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/studevelop")
public class StuDevelopPerformItemAction extends CommonAuthAction {

    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StudevelopPerformItemService studevelopPerformItemService;
    @Autowired
    private StuDevelopPerformItemCodeService stuDevelopPerformItemCodeService;
    @RequestMapping("/performanceItem/index/page")
    public String main(ModelMap map){
        if(!isAdmin(StuDevelopConstant.PERMISSION_TYPE_GROWTH)){
            return promptFlt(map , "不是成长手册管理员不能维护和查询表现项目维护内容");
        }
        String unitId = getUnitId();

        String sections =   schoolRemoteService.findSectionsById(unitId);

        String[] sectionArr = sections.split(",");
        String section = sectionArr[0];
        List<McodeDetail> detailList = new ArrayList<McodeDetail>();
        for(String str : sectionArr){
            McodeDetail detail = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XD" ,str) ,McodeDetail.class);
            detailList.add(detail);

        }

        map.put("detailList" ,detailList);
        map.put("section" , section);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[] {Integer.valueOf(section)}) ,Grade.class);
        map.put("gradeList" , gradeList);

        return "/studevelop/stuGrow/stuDevelopPerformanceItemMain.ftl";
    }

    @ResponseBody
    @RequestMapping("/performanceItem/gradeList/page")
    public String getGreadeList(@RequestParam String section ,ModelMap map){
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getUnitId(), new Integer[] {Integer.valueOf(section)}) ,Grade.class);
        map.put("gradeList" , gradeList);
        return JSON.toJSONString(gradeList);
//        return "/studevelop/stuGrow/stuDevelopMonthPerformanceGradeList.ftl";
    }
    @RequestMapping("/performanceItem/list/page")
    public String showList(@RequestParam String gradeCode , ModelMap map){
        String unitId = getUnitId();
        List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(unitId ,gradeCode);

        if(CollectionUtils.isEmpty(itemList)){
            itemList = new ArrayList<StuDevelopPerformItem>();
            map.put("itemList" ,itemList);
            return "/studevelop/stuGrow/stuDevelopPerforItemList.ftl";
        }
        List<String> itemIds = new ArrayList<String>();
        for(StuDevelopPerformItem item : itemList){
            itemIds.add(item.getId());
        }
        List<StuDevelopPerformItemCode> codeList = stuDevelopPerformItemCodeService.getPerformItemsByItemIds(itemIds.toArray(new String[]{}));
        Map<String,List<StuDevelopPerformItemCode>> itemMap = new HashMap<String, List<StuDevelopPerformItemCode>>();
        for(StuDevelopPerformItemCode code : codeList){
            List<StuDevelopPerformItemCode> list = itemMap.get(code.getItemId());
            if(list == null){
                list = new ArrayList<StuDevelopPerformItemCode>();
                itemMap.put(code.getItemId() ,list);
            }
            list.add(code);
        }
        for(StuDevelopPerformItem item : itemList){
            List<StuDevelopPerformItemCode> list =  itemMap.get(item.getId());
            if(CollectionUtils.isNotEmpty(list)){
                Collections.sort(list);
            }

            item.setCodeList(list);
        }
        map.put("itemList" ,itemList);
        return "/studevelop/stuGrow/stuDevelopPerforItemList.ftl";
    }
    @ResponseBody
    @RequestMapping("/performanceItemcode/delete")
    public String deleteItemCode(@RequestParam String itemCodeId){
        try{
            stuDevelopPerformItemCodeService.deleteItemCodebyId(getUnitId(),itemCodeId);
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }
       return  returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/performanceItem/delete")
    public String deleteItem(@RequestParam String itemId){
        try{
            studevelopPerformItemService.deleteItemAndItemCode(itemId);
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }
        return  returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/performanceItem/save")
    public String saveOrUpdate(StuDevelopPerformItem stuDevelopPerformItem , StudevelopPerformItemCodeDto studevelopPerformItemCodeDto , @RequestParam String codeContent ){
        try{
        	StuDevelopPerformItem item = studevelopPerformItemService.getStuDevelopPerformItemsByIdGrade(stuDevelopPerformItem.getUnitId(),
        			stuDevelopPerformItem.getItemGrade(), stuDevelopPerformItem.getItemName(), stuDevelopPerformItem.getId());
        	if(item != null) {
        		return error("评估项目名称在该年级下已存在，请修改！");
        	}
        	
        	List<StuDevelopPerformItemCode> addPerformItemCodeList = new ArrayList<StuDevelopPerformItemCode>();
            if(StringUtils.isNotEmpty(codeContent)){
                String[] arr = codeContent.split(",");

                for(String str : arr){
                    StuDevelopPerformItemCode code = new StuDevelopPerformItemCode();
                    code.setCodeContent(str);
                    addPerformItemCodeList.add(code);
                }
            }
            if(studevelopPerformItemCodeDto == null){
                studevelopPerformItemCodeDto = new StudevelopPerformItemCodeDto();
            }
            
            studevelopPerformItemService.savePerformItem(stuDevelopPerformItem,addPerformItemCodeList ,studevelopPerformItemCodeDto.getCodeList());

        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }


        return returnSuccess();
    }

    @RequestMapping("/performanceItem/getPerformItemById")
    public String getPerformItem(@RequestParam String itemId ,@RequestParam boolean copyIs , ModelMap map){
        StuDevelopPerformItem stuDevelopPerformItem = null;
        List<StuDevelopPerformItemCode> stuDevelopPerformItemCodeList = null;

        String unitId = getUnitId();
        if(StringUtils.isNotEmpty(itemId)){
            if(!copyIs){
                stuDevelopPerformItem = studevelopPerformItemService.findOne(itemId);
            }
            stuDevelopPerformItemCodeList = stuDevelopPerformItemCodeService.getAllByItemId(itemId);
        }
        if(stuDevelopPerformItem == null){
            stuDevelopPerformItem = new StuDevelopPerformItem();
            stuDevelopPerformItem.setUnitId(unitId);
        }
        if(stuDevelopPerformItemCodeList == null){
            stuDevelopPerformItemCodeList = new ArrayList<StuDevelopPerformItemCode>();
        }
        if(copyIs){
            for(StuDevelopPerformItemCode code :stuDevelopPerformItemCodeList ){
                code.setId(null);
            }
        }
        map.put("stuDevelopPerformItem" ,stuDevelopPerformItem);
        map.put("stuDevelopPerformItemCodeList" ,stuDevelopPerformItemCodeList);
        return "/studevelop/stuGrow/stuDevelopPerformItemEdit.ftl";
    }
    @RequestMapping("/performanceItem/copyPerformItemById")
    public String doCopyPerformItem(@RequestParam String itemId , ModelMap map){
        StuDevelopPerformItem stuDevelopPerformItem = null;

        List<StuDevelopPerformItemCode> stuDevelopPerformItemCodeList = null;

        String unitId = getUnitId();
        if(StringUtils.isNotEmpty(itemId)){
            stuDevelopPerformItemCodeList = stuDevelopPerformItemCodeService.getAllByItemId(itemId);
        }
        stuDevelopPerformItem = new StuDevelopPerformItem();
        stuDevelopPerformItem.setUnitId(unitId);

        if(stuDevelopPerformItemCodeList == null){
            stuDevelopPerformItemCodeList = new ArrayList<StuDevelopPerformItemCode>();
        }
        for(StuDevelopPerformItemCode code :stuDevelopPerformItemCodeList ){
            code.setId(null);
        }
        map.put("stuDevelopPerformItem" ,stuDevelopPerformItem);
        map.put("stuDevelopPerformItemCodeList" ,stuDevelopPerformItemCodeList);
        return "/studevelop/stuGrow/stuDevelopPerformItemEdit.ftl";
    }
    @ResponseBody
    @RequestMapping("/performanceItem/checkItemName")
    public String checkItemName(@RequestParam String unitId , @RequestParam String gradeCode ,@RequestParam String itemId ,@RequestParam String itemName){

        StuDevelopPerformItem item = studevelopPerformItemService.getStuDevelopPerformItemsByIdGrade(unitId,gradeCode,itemName,itemId);

        if(item == null){
            return returnSuccess();
        }else{
            return returnError();
        }
    }

    @RequestMapping("/performanceItem/copyToGrade")
    public String  copyToGradeLink(@RequestParam String gradeCode, ModelMap map){

        String unitId = getUnitId();
        String sections =   schoolRemoteService.findSectionsById(unitId);

        String[] sectionArr = sections.split(",");
        List<McodeDetail> detailList = new ArrayList<McodeDetail>();
        for(String str : sectionArr){
            McodeDetail detail = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XD" ,str) ,McodeDetail.class);
            detailList.add(detail);
        }

        map.put("detailList" ,detailList);

        List<Grade>  gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId) , Grade.class);
        Map<String , List<Grade>> gradeMap = new HashMap<String, List<Grade>>();
        for(Grade g : gradeList){
            if(gradeCode.equals(g.getGradeCode())){
                continue;
            }
            String key = String.valueOf(g.getSection());
            List<Grade> gradeList1 = gradeMap.get(key);
            if(gradeList1 == null){
                gradeList1 = new ArrayList<Grade>();
                gradeMap.put(key,gradeList1);
            }
            gradeList1.add(g);
        }
        map.put("gradeMap" , gradeMap);
        return "/studevelop/stuGrow/stuDevelopPerformCopyGrade.ftl";
    }
    @ResponseBody
    @RequestMapping("/performanceItem/copyToGradesEdit")
    public String copyToGradesEdit(@RequestParam String gradeCodes ,@RequestParam String itemGrade){
        try{
            String[] arrGrade = gradeCodes.split(",");
            List<StuDevelopPerformItem> itemList = studevelopPerformItemService.getStuDevelopPerformItemsByUnitIdAndGrade(getUnitId(),itemGrade);
            if(CollectionUtils.isEmpty(itemList)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-11").setMsg("该年级下没有考评项目可以复制！"));
            }
            studevelopPerformItemService.copyToGrade(getUnitId(),arrGrade,itemGrade);
        }catch (Exception e){
            e.printStackTrace();
            return  returnError();
        }

        return returnSuccess();
    }
    private String getUnitId(){
        LoginInfo login = getLoginInfo();
        String unitId = login.getUnitId();
        return unitId;
    }
}
