package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.diathesis.data.vo.DiathesisChildProjectVo;
import net.zdsoft.diathesis.data.vo.DiathesisIdAndNameVo;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/3/29 17:32
 */
@RestController
@RequestMapping("/diathesis/project")
public class DiathesisProjectAction extends BaseAction {
    @Autowired
    private DiathesisProjectService diathesisProjectService;

    @Autowired
    private DiathesisRecordSetService diathesisRecordSetService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;

    @Autowired
    private DiathesisOptionService diathesisOptionService;
    @Autowired
    private DiathesisStructureService diathesisStructureService;
    @Autowired
    private DiathesisProjectExService diathesisProjectExService;
    /**
     * 查询顶级类目
     * @return
     */
    @GetMapping("/findTopProject")
    public String findTopProject(){
        String unitId = getLoginInfo().getUnitId();
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        List<DiathesisIdAndNameDto> list = diathesisProjectService.findTopProjectByUnitId(usingUnitId);
        return Json.toJSONString(list);
    }


    /**
     * 一级类目 更新
     * @param diathesisIdAndNameVo
     * @return
     */
    @PostMapping("/updateTopProjects")
    public String updateTopProjects(@RequestBody DiathesisIdAndNameVo diathesisIdAndNameVo){
        try{
            LoginInfo loginInfo = getLoginInfo();
            String unitId = getLoginInfo().getUnitId();
            String realName = loginInfo.getRealName();
            if(!diathesisCustomAuthorService.hasAuthorByType(unitId,DiathesisConstant.AUTHOR_PROJECT_ALL)){
                return error("改单位没有修改一级项目的权限");
            }
            List<DiathesisIdAndNameDto> list = diathesisIdAndNameVo.getDiathesisIdAndNameDtoList();
            if(list==null ){
                list=new ArrayList<DiathesisIdAndNameDto>();
            }
            List<String> nameList = list.stream().map(x -> x.getName()).distinct().collect(Collectors.toList());
            for(String name:nameList){
                if(StringUtils.isBlank(name)){
                    return error("一级类目名字不能为空");
                }else if(name.length()>250){
                    return error("类目名字最大250个字");
                }
            }
            if(nameList.size()!=list.size()){
                return error("同一单位下,不能有相同的一级项目名字");
            }
            List<Integer> prop = EntityUtils.getList(list, x -> x.getProportion());
            int sum=0;
            for (Integer p : prop) {
                sum+=p;
                if(p==null || p<0 || p>100){
                    throw new RuntimeException("比例分设置错误,不能为空,且和为100");
                }
            }
            if(sum!=100){
                throw new RuntimeException("比例分设置错误,不能为空,且和为100");
            }
            diathesisProjectService.updateTopProjects(list,unitId,realName);
            return success("更新成功");
        }catch (RuntimeException e){
            e.printStackTrace();
            return error(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return error("更新失败");
        }
    }
    /**
     * 写实记录列表更新
     */
    @PostMapping("/updateRecords")
    public String updateRecords(@RequestBody DiathesisIdAndNameVo diathesisIdAndNameVo){

        try{
            if(!diathesisCustomAuthorService.hasAuthorByType(getLoginInfo().getUnitId(),DiathesisConstant.AUTHOR_PROJECT_RECORD)){
                return error("该单位没有修改写实记录设置的权限");
            }
            if(diathesisIdAndNameVo==null){
                return error("参数不能为空");
            }
            List<DiathesisIdAndNameDto> list = diathesisIdAndNameVo.getDiathesisIdAndNameDtoList();
            if(list==null){
                list=new ArrayList<DiathesisIdAndNameDto>();
            }
            List<String> recordNameList = list.stream().map(x -> x.getName()).distinct().collect(Collectors.toList());
            for(String name:recordNameList){
                if(StringUtils.isBlank(name)){
                    return error("名称不能为空");
                }else if(name.length()>250){
                    return error("名称不能超过250个字");
                }
            }
            if(recordNameList.size()!=list.size()){
                return error("同一个一级类目下,写实记录名字不能重复");
            }
            String parentId = diathesisIdAndNameVo.getParentId();
            Optional<DiathesisProject> parentProject = diathesisProjectService.findProjectById(parentId);
            if(!parentProject.isPresent() || !parentProject.get().getProjectType().equals(DiathesisConstant.PROJECT_TOP)){
                return error("查不到该一级类目");
            }
            LoginInfo loginInfo = getLoginInfo();
            String unitId = getLoginInfo().getUnitId();
            String realName = loginInfo.getRealName();
            diathesisProjectService.updateRecords(list,unitId,realName,diathesisIdAndNameVo.getParentId());
            return success("更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return error("更新失败");
        }
    }



    /**
     * 保存二级和三级的类目信息
     */
    @PostMapping("/saveChildProject")
    public String saveChildProjects(@RequestBody DiathesisChildProjectVo diathesisChildProjectVo){
        try{
            diathesisChildProjectVo.setUnitId(getLoginInfo().getUnitId());
            String realName = getLoginInfo().getRealName();
            List<String> proportions = diathesisChildProjectVo.getProportions();
            if(proportions==null||proportions.size()!=5){
                return error("proportions数组长度错误,必须是5");
            }
            int sum = (int)proportions.stream().collect(Collectors.summarizingInt(x -> Integer.valueOf(x==""?"0":x))).getSum();
            if(sum!=100){
                return error("评价占比之和必须为100");
            }
            if(!diathesisCustomAuthorService.hasAuthorByType(getLoginInfo().getUnitId(),DiathesisConstant.AUTHOR_PROJECT_CHILD)){
                //没有权限
                diathesisChildProjectVo.setRealName(getLoginInfo().getRealName());
                diathesisProjectExService.saveChildProjectEx(diathesisChildProjectVo);
                //todo  可以修改 评价人 和 占比
            }else{
                //有权限
                List<DiathesisChildProjectDto> list =
                        diathesisChildProjectVo.getChildProjectList();
                if(list!=null){
                    for(DiathesisChildProjectDto d:list){
                        if(d.getProjectName().length()>250){
                            return error("二级项目名字不能超过250个字");
                        }
                        if(d.getChildProjectList() != null && d.getChildProjectList().size()>0){
                            for (DiathesisChildProjectDto t:d.getChildProjectList()){
                                if(StringUtils.isBlank(t.getProjectName())){
                                    return error("三级项目名字不能为空");
                                }
                                if(t.getProjectName().length()>250){
                                    return error("三级项目名字不能超过250字");
                                }
                                if(t.getRemark()!=null && t.getRemark().length()>250){
                                    return error("备注不能超过250字");
                                }
                            }
                        }
                    }
                }

                diathesisProjectService.saveChildProjects(diathesisChildProjectVo,realName);
            }
            return success("设置成功");
        }catch (NumberFormatException e){
            return error("评价占比只能为0-100的数字");
        }catch (Exception e){
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 查询二级和三级的类目信息
     */
    @GetMapping("/findChildProject")
    public String findChildProject(String parentId){
        String unitId = getLoginInfo().getUnitId();
        String realName = getLoginInfo().getRealName();
        Json json = new Json();
        Optional<DiathesisProject> project = diathesisProjectService.findProjectById(parentId);
        if(!project.isPresent())return error("不存在该一级项目");
       // RedisUtils.hasLocked("diathesisInitLock_" + unitId + "_78,");
        //v1.2.0 proporitons 从project表中分离
        String topUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        //String secUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_CHILD);
        DiathesisProjectEx ex = diathesisProjectExService.findByUnitIdAndProjectId(topUnitId, parentId);

        String[] proporitons = ex.getProportions().split(",");
        json.put("proportions",proporitons);
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_CHILD);
        //List<DiathesisProject> projectList = diathesisProjectService.findListBy(new String[]{"unitId", "parentId","projectType"}, new String[]{usingUnitId, parentId,DiathesisConstant.PROJECT_CHILD});

        List<DiathesisChildProjectDto> result = diathesisProjectService.findChildProjectByParentIdAndUnitId(parentId, unitId, usingUnitId, getLoginInfo().getRealName());

        result.sort((x,y)->{

            if(x.getParentId().equals(parentId) && y.getParentId().equals(parentId)){
                return x.getSortNumber()-y.getSortNumber();
            }
            if(x.getParentId().equals(parentId) && !y.getParentId().equals(parentId)){
                return -1;
            }
            if(!x.getParentId().equals(parentId) && y.getParentId().equals(parentId)){
                return 1;
            }
            return x.getSortNumber()-y.getSortNumber();
        });
        json.put("childProjectList", result);
        return json.toString();
    }

    /**
     * 写实记录列表
     * @param parentId
     * @return
     */
    @RequestMapping("/findRecordsByParentId")
    public String findRecordsByParentId(String parentId){
        if(StringUtils.isBlank(parentId))return error("一级类目id不能为空");
        Optional<DiathesisProject> project = diathesisProjectService.findProjectById(parentId);
        if(!(project.isPresent()&& project.get().getProjectType().equals(DiathesisConstant.PROJECT_TOP))){
            return error("不存在该一级类目");
        }
        String unitId = getLoginInfo().getUnitId();
        String realName = getLoginInfo().getRealName();
        //RedisUtils.hasLocked("diathesisInitLock_" + unitId + "_78,");
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisProject> projectList = diathesisProjectService.findListBy(new String[]{"unitId", "parentId","projectType"}, new String[]{usingUnitId, parentId,DiathesisConstant.PROJECT_RECORD});

        List<DiathesisIdAndNameDto> recordList = projectList.stream().sorted((x, y) -> x.getSortNumber() - y.getSortNumber()).map(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getId());
            dto.setName(x.getProjectName());
            return dto;
        }).collect(Collectors.toList());
        //diathesisProjectService.findRecordsByParentIdAndUnitId(parentId,unitId);
        if(recordList==null){
            recordList=new ArrayList<DiathesisIdAndNameDto>();
        }
        return JSON.toJSONString(recordList);
    }

/*    *//**
     * 写实记录赋分设置
     */
    @RequestMapping("/saveRecordScoreSetting")
    public String saveRecordScoreSetting(@RequestBody DiathesisProjectDto dto){

        try {
            //权限
            String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(getLoginInfo().getUnitId(), DiathesisConstant.AUTHOR_PROJECT_RECORD);
            if(!getLoginInfo().getUnitId().equals(usingUnitId)){
                return error("该单位没有设置写实记录的权限");
            }
            //参数校验
            List<DiathesisProjectDto> projectlist = dto.getChildList();
            DiathesisRecordSetErrorDto errorDto = checkRecordSetting(projectlist);
            if(errorDto!=null)return JSON.toJSONString(errorDto);
            diathesisRecordSetService.saveRecordSet(projectlist,getLoginInfo().getUnitId(),getLoginInfo().getRealName());
            return success("保存成功");
        }catch (NumberFormatException e){
            return error("数字格式错误,请检查");
        } catch (RuntimeException e ){
            e.printStackTrace();
            return error(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return error("保存失败");
        }
    }

    private DiathesisRecordSetErrorDto checkRecordSetting(List<DiathesisProjectDto> projectlist) {
        DiathesisRecordSetErrorDto error =null;
        A:
        for (DiathesisProjectDto dto : projectlist) {
            Integer semesterMax=null;
            if(StringUtils.isNotBlank(dto.getSemesterMax())){
                semesterMax=Integer.parseInt(dto.getSemesterMax());
                if(semesterMax<0){
                    error= new DiathesisRecordSetErrorDto(false,"学期最大分不能为负数",dto.getId(),1);
                    break;
                }
            }
            Integer allMax=null;
            if(StringUtils.isNotBlank(dto.getAllMax())){
                allMax=Integer.parseInt(dto.getAllMax());
                if(allMax<0){
                    error= new DiathesisRecordSetErrorDto(false,"在校最高分值不能设置为负数",dto.getId(),2);
                    break;
                }
                if(semesterMax!=null && allMax<semesterMax){
                    error= new DiathesisRecordSetErrorDto(false,"学期最高分不能超过在校最高分",dto.getId(),1);
                    break;
                }
            }
            if(DiathesisConstant.SCORE_ADD_TYPE_0.equals(dto.getScoreType())){
                if(StringUtils.isBlank(dto.getScore())){
                    error= new DiathesisRecordSetErrorDto(false,"按项目赋分的时候,必须设置每次的分值",dto.getId(),0);
                    break;
                }
                int score = Integer.parseInt(dto.getScore());
                if(semesterMax !=null && score>semesterMax){
                    error= new DiathesisRecordSetErrorDto(false,"每次的分值不能超过学期最大分值",dto.getId(),0);
                    break;
                }
            }else if(DiathesisConstant.SCORE_ADD_TYPE_1.equals(dto.getScoreType())){
                List<DiathesisOption> optionList = dto.getOptionList();
                if(CollectionUtils.isEmpty(optionList) || StringUtils.isBlank(dto.getScoreStructureId())){
                    error= new DiathesisRecordSetErrorDto(false,"按级别赋分的时候,必须指定单选",dto.getId(),3);
                    break;
                }
                for (DiathesisOption option : optionList) {
                    String score = option.getScore();
                    if(StringUtils.isBlank(score)){
                        error= new DiathesisRecordSetErrorDto(false,"单选不能为空",dto.getId(),4);
                        break A;
                    }
                    int n = Integer.parseInt(score);
                    if(semesterMax!=null && semesterMax<n || (allMax!=null && allMax<n) || n<0){
                        error= new DiathesisRecordSetErrorDto(false,"选项分设置不能超过学期最高分和在校最高分!",dto.getId(),4);
                        break A;
                    }
                }
            }

        }
        return error;
    }



    /**
     * 赋分页面数据
     * /diathesis/project/getRecordProject
     */
    @RequestMapping("/getRecordProject")
    public String getRecordProject(){
        String unitId = getLoginInfo().getUnitId();
        String topProUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        String recordUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisIdAndNameDto> topProject = diathesisProjectService.findTopProjectByUnitId(topProUnitId);
        List<String> topIds = EntityUtils.getList(topProject, x -> x.getId());
        List<DiathesisProject> recordList = diathesisProjectService.findByUnitIdAndParentIdIn(recordUnitId, topIds)
                .stream().filter(x -> DiathesisConstant.PROJECT_RECORD.equals(x.getProjectType())).collect(Collectors.toList());

        List<String> recordIds = EntityUtils.getList(recordList, x -> x.getId());


        Map<String, List<DiathesisOption>> optionMap = diathesisOptionService.findListByProjectIdIn(recordIds).stream().collect(Collectors.groupingBy(x -> x.getStructureId()));

        Map<String,List<DiathesisStructureSettingDto>> projectOptionMap=new HashMap<>();

        //有单选的structure
       List<DiathesisStructure> structureList=diathesisStructureService.findListBySingleTypeAndProjectTypeIn(recordIds);

        for (DiathesisStructure x : structureList) {
            //不是单选就过滤
            if(!DiathesisConstant.DATA_TYPE_2.equals(x.getDataType()))continue;

            List<DiathesisStructureSettingDto> structures;
            DiathesisStructureSettingDto settingDto = new DiathesisStructureSettingDto();
            settingDto.setId(x.getId());
            settingDto.setTitle(x.getTitle());
            if(optionMap!=null && CollectionUtils.isNotEmpty(optionMap.get(x.getId()))){
                settingDto.setOption(optionMap.get(x.getId()));
                if(projectOptionMap.get(x.getProjectId())==null){
                    structures=new ArrayList<>();
                    projectOptionMap.put(x.getProjectId(),structures);
                }else{
                    structures=projectOptionMap.get(x.getProjectId());
                }
                structures.add(settingDto);
            }
        }

       List<DiathesisRecordSet> recordSet= diathesisRecordSetService.findListByIdIn(recordIds.toArray(new String[0]));
        HashMap<String, DiathesisRecordSet> recordSetHashMap = new HashMap<>();
        for (DiathesisRecordSet x : recordSet) {
            recordSetHashMap.put(x.getId(),x);
        }
        //有单选的字段
        Map<String, List<DiathesisProjectDto>> recordListMap = recordList.stream().collect(Collectors.groupingBy(x -> x.getParentId(),Collectors.mapping(x->{
            DiathesisProjectDto dto = new DiathesisProjectDto();
            dto.setId(x.getId());
            dto.setProjectName(x.getProjectName());
            DiathesisRecordSet y = recordSetHashMap.get(x.getId());
            if(y!=null){
                dto.setScoreStructureId(y.getScoreStructureId());
                dto.setScoreType(y.getScoreType());
                dto.setScore(y.getScore());
                dto.setSemesterMax(y.getSemesterMaxScore());
                dto.setAllMax(y.getAllMaxScore());
            }
            if(projectOptionMap!=null)
                dto.setStructureList(projectOptionMap.get(x.getId()));
            return dto;
        },Collectors.toList())));

        List<DiathesisProjectDto> resultList=new ArrayList<>();
        for (DiathesisIdAndNameDto top : topProject) {
            DiathesisProjectDto one = new DiathesisProjectDto();
            one.setId(top.getId());
            one.setProjectName(top.getName());
            if(recordListMap==null || CollectionUtils.isEmpty(recordListMap.get(top.getId())))continue;
            one.setChildList(recordListMap.get(top.getId()));
            resultList.add(one);
        }
        return Json.toJSONString(resultList);
    }

    /**
     * 考评标准设置首页
     * @return
     */
    @GetMapping("/findProjectDetial")
    public String findProjectDetial(){
        String unitId = getLoginInfo().getUnitId();
        String topUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        String secUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_CHILD);
        String recordUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
        List<DiathesisIdAndNameDto> topList = diathesisProjectService.findTopProjectByUnitId(topUnitId);
        Map<String,Integer> secMap=diathesisProjectService.countTopProjectMap(EntityUtils.getList(topList,x->x.getId()),secUnitId,DiathesisConstant.PROJECT_CHILD);
        Map<String,Integer> recordMap=diathesisProjectService.countTopProjectMap(EntityUtils.getList(topList,x->x.getId()),recordUnitId,DiathesisConstant.PROJECT_RECORD);
        List<DiathesisIdAndNameDto> result = topList.stream().map(x -> {
            Integer sec = secMap.get(x.getId());
            x.setChildProjectCount(sec == null ? 0 : sec);
            Integer record = recordMap.get(x.getId());
            x.setChildRecordCount(record == null ?0: record);
            return x;
        }).collect(Collectors.toList());
        return JSON.toJSONString(result);
    }
}
