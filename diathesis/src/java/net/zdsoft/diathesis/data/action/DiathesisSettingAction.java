package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.DiathesisGlobalSettingDto;
import net.zdsoft.diathesis.data.dto.DiathesisRecordSettingDto;
import net.zdsoft.diathesis.data.dto.DiathesisTreeDto;
import net.zdsoft.diathesis.data.dto.DiathesisUnitAuthorDto;
import net.zdsoft.diathesis.data.entity.DiathesisCustomAuthor;
import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/3/29 16:28
 */
@RestController
@RequestMapping("/diathesis/setting")
public class DiathesisSettingAction extends BaseAction {
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private DiathesisSubjectFieldService diathesisSubjectFieldService;
    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private RegionRemoteService regionRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;



    @PostMapping("/saveGlobalSchool")
    public String saveGlobalSchool(@RequestBody DiathesisGlobalSettingDto dto, Errors errors) {

        try {
            String unitId = getLoginInfo().getUnitId();
            dto.setUnitId(unitId);
            dto.setOperator(getLoginInfo().getRealName());
            //判断是否是学校
            if(!getLoginInfo().getUnitClass().equals(Unit.UNIT_CLASS_SCHOOL))return error("学校专用接口");
            boolean hasSetAuthor=diathesisCustomAuthorService.hasSetAuthor(unitId);
            if(hasSetAuthor){
                //有设置权限
                if(errors.hasFieldErrors())return error(errors.getFieldError().getDefaultMessage());
                checkEduParams(dto);
                checkSchoolParams(dto);
                //todo 校验
                diathesisSetService.saveSchoolSet(dto);
            }else{
                checkSchoolParams(dto);
                //没有设置权限,  只修改 录入人 审核人 小组评价方式 评价人类型
                diathesisSetService.saveSchoolNoAuthorSet(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("保存成功");

    }

    private void checkSchoolParams(DiathesisGlobalSettingDto dto) {//DIATHESIS_SCORE_TYPE_MAP
        if(CollectionUtils.isEmpty(dto.getEvaluation())){
            throw new RuntimeException("评价人至少选择一个");
        }
        if(!DiathesisConstant.DIATHESIS_SCORE_TYPE_MAP.keySet().containsAll(dto.getEvaluation())){
           throw new RuntimeException("没有这种评价人类型");
        }
        if(!DiathesisConstant.MUTUAL_TYPE_MAP.keySet().contains(dto.getMutualType())){
            throw new RuntimeException("非法的小组评价方式");
        }
        if(!checkType(dto.getAuditorTypes(),dto.getInputTypes()) ){
            throw new RuntimeException("非法的审核人code 或者 录入人code");
        }
    }

    // 教育局端 设置
    @PostMapping("/saveGlobalEducation")
    public String saveGlobalEducation(@RequestBody @Valid DiathesisGlobalSettingDto dto, Errors errors) {

        if(!getLoginInfo().getUnitClass().equals(Unit.UNIT_CLASS_EDU))return error("教育局专用接口");
        String unitId = getLoginInfo().getUnitId();
        dto.setUnitId(unitId);
        dto.setOperator(getLoginInfo().getRealName());
        boolean hasSetAuthor=diathesisCustomAuthorService.hasSetAuthor(unitId);
        if(!hasSetAuthor){
            return error("该教育局没有权限设置");
        }
        try {
            if(errors.hasFieldErrors())return error(errors.getFieldError().getDefaultMessage());
            checkEduParams(dto);
            diathesisSetService.saveEduSet(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }

        return success("保存成功");
    }
    /**
     * 教育局设置校验
     */
    private void checkEduParams(DiathesisGlobalSettingDto dto) {
        Double total = 0d;
        try {
            for (String s : dto.getSemesterScoreProp()) {
                if(Double.parseDouble(s)<0 || Double.parseDouble(s)>100){
                    throw new RuntimeException();
                }
                total+=Double.parseDouble(s);
            }
            if(dto.getSemesterScoreProp().size()!=6){
                throw new RuntimeException("学期占比数量错误");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("学期占比格式错误,请输入整数");
        }
        if( total!=100d){
            throw new RuntimeException("学期占比和必须为100");
        }

        DiathesisSet setting = diathesisSetService.findByUnitId(dto.getUnitId());
        if(!setting.getInputValueType().equals(dto.getInputValueType()) && diathesisScoreInfoService.isUsingByUnitId(dto.getUnitId())){
            throw new RuntimeException("当前评价录入形式正被使用，无法修改");
        }
        if(!DiathesisConstant.DIATHESIS_YES.equals(dto.getIsAssignPoint()) && !DiathesisConstant.DIATHESIS_NO.equals(dto.getIsAssignPoint())){
            throw new RuntimeException("是否赋分参数错误");
        }

        List<String> rankValues = dto.getRankValues();
        List<String> rankItems = dto.getRankItems();
        List<String> scoreScopes = dto.getScoreScopes();
        if(rankValues.size()!=rankItems.size()){
            throw new RuntimeException("等第,等第对应值 设置的个数必须一致");
        }
        if(dto.getScoreType().equals(DiathesisConstant.SCORE_RANGE_REGULAR)){
            checkRowScore(scoreScopes);
        }else if(dto.getScoreType().equals(DiathesisConstant.SCORE_PROPORTION_REGULAR)){
            //比例制度需要重新弄
            if(getLoginInfo().getUnitClass()== Unit.UNIT_CLASS_EDU){
                for (List<String> x : dto.getEducationScoreScopesMap().values()) {
                    checkSortScopes(x);
                }
            }else{
                checkSortScopes(scoreScopes);
            }
        }else{
            throw new RuntimeException("没有这个分数换算制度");
        }

    }


    //教育局设置下面单位的权限
    @PostMapping("/saveChildUnitAuthor")
    public String saveChildUnitAuthor(@RequestBody DiathesisUnitAuthorDto dto){
        String unitId = dto.getUnitId();
        if(StringUtils.isBlank(unitId))return error("unitId不能为空!");
        try {
            List<DiathesisCustomAuthor> currentAuthor = diathesisCustomAuthorService.findAuthorListByUnitId(getLoginInfo().getUnitId());
            Set<Integer> authorSet = EntityUtils.getSet(currentAuthor, x -> x.getAuthorType());
            Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
            if(authorSet.contains(DiathesisConstant.AUTHOR_ADMIN)){
                authorSet.addAll(DiathesisConstant.AUTHOR_TREE_LIST);
            }
            if(authorSet.contains(DiathesisConstant.AUTHOR_PROJECT_ALL)){
                authorSet.add(DiathesisConstant.AUTHOR_PROJECT_CHILD);
                authorSet.add(DiathesisConstant.AUTHOR_PROJECT_RECORD);
            }
            for (Integer author : dto.getAuthorList()) {
                if (!authorSet.contains(author)){
                    return error("权限不足");
                }
            }
            if (dto.getAuthorList().contains(DiathesisConstant.AUTHOR_ADMIN) && unit.getUnitClass().equals(Unit.UNIT_CLASS_SCHOOL)){
                return error("不能给学校设置下放权限");
            }
            diathesisCustomAuthorService.saveChildUnitAuthor(unitId,dto.getAuthorList(),getLoginInfo().getRealName());
            return success("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return success(e.getMessage());
        }
    }

    /**
     * 获得单位的权限信息
     * @param unitId
     * @return
     */
    @GetMapping("/findAuthorByUnitId")
    public String findAuthorByUnitId(String unitId){
        if(StringUtils.isBlank(unitId))return error("unitId不能为空");
        List<Integer> authorList = EntityUtils.getList(diathesisCustomAuthorService.findAuthorListByUnitId(unitId), x -> x.getAuthorType());
        changeAuthor(authorList);
        return JSON.toJSONString(CollectionUtils.isEmpty(authorList)?new ArrayList<>():authorList);
    }

    //全局设置获取
    @GetMapping("/findGlobal")
    public String findGlobal(){
        String unitId = getLoginInfo().getUnitId();
        List<DiathesisCustomAuthor> authorList = diathesisCustomAuthorService.findAuthorListByUnitId(unitId);

        DiathesisGlobalSettingDto set = diathesisSetService.findGlobalByUnitId(diathesisCustomAuthorService.findUsingUnitId(unitId,DiathesisConstant.AUTHOR_GOBAL_SET));
        //审核人 录入人 小组互评形式 人员map  用自己的设置
        DiathesisGlobalSettingDto mySet = diathesisSetService.findGlobalByUnitId(unitId);
        if(DiathesisConstant.SCORE_PROPORTION_REGULAR.equals(set.getScoreType())){
            set.setScoreScopes(mySet.getScoreScopes());
        }
        set.setMutualType(mySet.getMutualType());
        set.setAuditorTypes(mySet.getAuditorTypes());
        set.setInputTypes(mySet.getInputTypes());
        set.setPeopleTypesMap(mySet.getPeopleTypesMap());
        set.setEvaluation(mySet.getEvaluation());
        List<Integer> authors = EntityUtils.getList(authorList, x -> x.getAuthorType());
        changeAuthor(authors);
        set.setAuthor(authors);
        return JSON.toJSONString(set);
    }

    //教育端 获得单位树
    @GetMapping("/findUnitTree")
    public String findUnitTree(String unitId){
        String regionCode = getLoginInfo().getRegion();
        if(StringUtils.isBlank(unitId))unitId=getLoginInfo().getUnitId();
        List<Unit> list = SUtils.dt(unitRemoteService.findDirectUnits(unitId, null), Unit.class);
        List<DiathesisTreeDto> unitTree=new ArrayList<>();
        for (Unit x : list) {
            DiathesisTreeDto tree = new DiathesisTreeDto();
            tree.setId(x.getId());
            tree.setName(x.getUnitName());
            tree.setpId(x.getParentId());
            tree.setType("" + x.getUnitClass());
            tree.setParent(unitRemoteService.existsUnderUnits(x.getId()));
            unitTree.add(tree);
        }
        unitTree.sort((x,y)->{
            if(x.getParent())return -1;
            if(x.getType().equals(Unit.UNIT_CLASS_EDU))return -1;
            return 0;
        });
        return JSON.toJSONString(unitTree);
    }


    //分数范围获取
    @GetMapping("/findScoreScopes")
    public String findScoreScopes(String unitId){

        if(!getLoginInfo().getUnitClass().equals(Unit.UNIT_CLASS_EDU)){
            return error("教育局端专用接口");
        }
        Unit unit = SUtils.dc(unitRemoteService.findOneBy(new String[]{"id", "isDeleted"}, new String[]{unitId, "0"}), Unit.class);
        if(unit==null){
            return error("不存在这个单位");
        }
        if(!unit.getUnitClass().equals(Unit.UNIT_CLASS_SCHOOL)){
            return error("只能查看学校的分数设置");
        }
        DiathesisGlobalSettingDto set = diathesisSetService.findGlobalByUnitId(unitId);
        if(set.getScoreType().equals(DiathesisConstant.SCORE_PROPORTION_REGULAR) && CollectionUtils.isNotEmpty(set.getScoreScopes()) ){
            return JSON.toJSONString(set.getScoreScopes());
        }else {
            return JSON.toJSONString(Arrays.asList("0-10","10-35","35-75","75-100"));
        }

    }

    // 单位权限查看
    @GetMapping("/findAuthorList")
    public String findAuthorList(){
        List<Integer> authorList = EntityUtils.getList(diathesisCustomAuthorService.findAuthorListByUnitId(getLoginInfo().getUnitId()), x -> x.getAuthorType());
        changeAuthor(authorList);
        return JSON.toJSONString(authorList.isEmpty()?new ArrayList<>():authorList);
    }

    private void changeAuthor(List<Integer> authorList) {
        if(authorList.contains(DiathesisConstant.AUTHOR_ADMIN)){
            authorList.remove(0);
            authorList.addAll(DiathesisConstant.AUTHOR_TREE_LIST);
        }else if(authorList.contains(DiathesisConstant.AUTHOR_PROJECT_ALL)){
            authorList.add(DiathesisConstant.AUTHOR_PROJECT_CHILD);
            authorList.add(DiathesisConstant.AUTHOR_PROJECT_RECORD);
        }
    }

    //教育局端 单位基本信息获取
    @GetMapping("/findUnitInfo")
    public String findUnitInfo(String unitId){
        if(StringUtils.isBlank(unitId))return error("单位id不能为空");
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        if(unit==null || unit.getIsDeleted()!=0)return error("查不到这个单位");
        List<Integer> authorList = EntityUtils.getList(diathesisCustomAuthorService.findAuthorListByUnitId(unitId), x -> x.getAuthorType());
        McodeDetail mcode = SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-DWLX", "" + unit.getUnitType()), McodeDetail.class);
        Region region = SUtils.dc(regionRemoteService.findByFullCode(StringUtils.rightPad(unit.getRegionCode(),6,"0")), Region.class);
        School school = SUtils.dc(schoolRemoteService.findOneById(unitId), School.class);

        Json json = new Json();
        json.put("unitName",unit.getUnitName());
        json.put("unitType",mcode==null?"":mcode.getMcodeContent());
        json.put("region",region==null?"":region.getRegionName());
        String principal;
        if(Unit.UNIT_CLASS_SCHOOL==unit.getUnitClass()){
            if(StringUtils.isBlank(school.getSchoolmaster())){
                principal="无";
            }else{
                principal=school.getSchoolmaster();
                if(StringUtils.isBlank(school.getLinkPhone())){
                    principal+="(无)";
                }else{
                    principal+="("+school.getLinkPhone()+")";
                }
            }
        }else{
            if(StringUtils.isBlank(unit.getLinkMan())){
                principal="无";
            }else{
                principal=unit.getLinkMan();
                if(StringUtils.isBlank(unit.getLinkPhone())){
                    principal+="(无)";
                }else{
                    principal+="("+unit.getLinkPhone()+")";
                }
            }
            principal=StringUtils.defaultString(unit.getLinkMan(),"")+"("+
                    StringUtils.defaultString(unit.getLinkPhone(),"无") +")";
        }
        json.put("principal",principal);
        changeAuthor(authorList);
        json.put("author",authorList);
        return json.toJSONString();

    }



    private void checkSortScopes(List<String> scoreScopes) {
        List<String> list=new ArrayList<>();
        list.addAll(scoreScopes);
        Collections.reverse(list);
        checkRowScore(list);
        try {
            if(Double.parseDouble(scoreScopes.get(0).split("-")[0])!=0){
                throw new RuntimeException("分值必须从0开始");
            }
            if(Double.parseDouble(scoreScopes.get(scoreScopes.size()-1).split("-")[1])!=100d){
                throw new RuntimeException("总分必须为100");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("分值设置数字格式错误");
        }
    }

    private void checkRowScore(List<String> scoreScopes) {
        double temp=Double.parseDouble(scoreScopes.get(scoreScopes.size()-1).split("-")[0]);
        for (int i = scoreScopes.size()-1; i >=0 ; i--) {
            String[] split = scoreScopes.get(i).split("-");
            if(split.length!=2){
                throw new RuntimeException("分数设置格式错误,分数范围用 - 分开");
            }
            double small = Double.parseDouble(split[0]);

            double big = Double.parseDouble(split[1]);
            if(small<0 || big<0){
                throw new RuntimeException("分值必须设置为正数");
            }
            if(small>=big){
                throw new RuntimeException("分值设置右边必须大于左边");
            }
            if(small!=temp){
                throw new RuntimeException("平均分设置必须连续");
            }
            temp=big;
        }
    }

    //全局设置 写实设置 审核人 和录入人code验证
    private boolean checkType(List<String> types1,List<String> types2) {
        if(CollectionUtils.isEmpty(types1)|| CollectionUtils.isEmpty(types2)){
            return false;
        }
        ArrayList<String> types = new ArrayList<>();
        types.addAll(types1);
        types.addAll(types2);

        List<CustomRole> list = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId","subsystems"},new String[]{getLoginInfo().getUnitId(),"78,"}), CustomRole.class);
        if(list==null || list.size()==0){
            return false;
        }
        List<String> roleList = list.stream().map(x -> x.getRoleCode()).collect(Collectors.toList());

        return roleList.containsAll(types);
    }

    @PostMapping("/saveRecord")
    public String saveRecord(@RequestBody @Valid DiathesisRecordSettingDto dto,Errors errors) {

        try {
            String realName = getLoginInfo().getRealName();
            dto.setUnitId(getLoginInfo().getUnitId());
            String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(getLoginInfo().getUnitId(), DiathesisConstant.AUTHOR_PROJECT_RECORD);
            if(usingUnitId.equals(getLoginInfo().getUnitId())){
                //有权限的
                if(errors.hasFieldErrors())return error(errors.getFieldError().getDefaultMessage());
            }else{
                //没有权限的
                if(!checkType(dto.getAuditorTypes(), dto.getInputTypes()) ){
                    return error("审核人,录入人都至少选则一条");
                }
                diathesisSetService.saveUnAuthorRecordSet(dto,realName);
                return success("保存成功");
            }

            if (!diathesisProjectService.findProjectById(dto.getId()).isPresent()) {
                return error("保存失败,不存在这个写实记录");
            }
            if(!checkType(dto.getAuditorTypes(), dto.getInputTypes()) ){
                return error("非法的审核人code 或者 录入人code");
            }
            if(dto.getStructure().stream().filter(x->x.getIsShow().equals(DiathesisConstant.STRUCTURE_SHOW)).count()>3){
                return error("显示在列表的字段最多3条");
            }
            diathesisSetService.saveRecordSetting(dto,realName);
            return success("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }

    }

    @GetMapping("/findRecord")
    public String findRecordSettingByProjectId(String projectId) {
        if(StringUtils.isBlank(projectId))return error("写实记录id不能为空");
        DiathesisRecordSettingDto record = diathesisSetService.findRecordSettingByProjectId(projectId,getLoginInfo().getUnitId(),getLoginInfo().getRealName());
        if(record==null)return error("找不到这个写实记录");
        return JSON.toJSONString(record);
    }

    @GetMapping("/findUserInfo")
    public String findUserInfo(){
        Json json = new Json();
        json.put("unitType",getLoginInfo().getUnitClass());
        json.put("ownerType",getLoginInfo().getOwnerType());
        json.put("userId",getLoginInfo().getUserId());
        json.put("unitId",getLoginInfo().getUnitId());
        return json.toJSONString();
    }
}
