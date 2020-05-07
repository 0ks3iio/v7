package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import net.zdsoft.basedata.remote.service.TreeRemoteService;
import net.zdsoft.bigdata.base.entity.PropertyTag;
import net.zdsoft.bigdata.base.service.PropertyTagService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;
import net.zdsoft.bigdata.extend.data.entity.TagRuleType;
import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.bigdata.extend.data.entity.WarningProjectUser;
import net.zdsoft.bigdata.extend.data.service.TagRuleRelationService;
import net.zdsoft.bigdata.extend.data.service.TagRuleSymbolService;
import net.zdsoft.bigdata.extend.data.service.TagRuleTypeService;
import net.zdsoft.bigdata.extend.data.service.WarningProjectService;
import net.zdsoft.bigdata.extend.data.service.WarningProjectUserService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTag;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTagService;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2018/7/12 17:10.
 */
@Controller
@RequestMapping("/bigdata/warningProject")
public class WarningProjectController extends BigdataBaseAction {

    @Resource
    private WarningProjectService warningProjectService;
    @Resource
    private TagRuleTypeService tagRuleTypeService;
    @Resource
    private TagRuleSymbolService tagRuleSymbolService;
    @Resource
    private TagRuleRelationService tagRuleRelationService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private EtlJobService etlJobService;
    @Resource
    private WarningProjectUserService warningProjectUserService;
    @Resource
    private TreeRemoteService treeRemoteService;
    @Resource
    private PropertyTagService propertyTagService;
    @Resource
    private MetadataTagService metadataTagService;

    @RequestMapping("/list")
    public String list(ModelMap model, HttpServletRequest request, String projectName) {
        Pagination page = createPagination(request);
        model.addAttribute("warningProjectList", warningProjectService.findALlByPage(page, projectName, getLoginInfo().getUnitId()));
        sendPagination(request, model,null, page);
        model.addAttribute("projectName", projectName);
        return "/bigdata/extend/warningProject/warningProjectList.ftl";
    }

    @RequestMapping("/editProjectUI")
    public String addProjectUI(Model model, String id) {
        String profileCode = "warning";
        List<TagRuleType> ruleTypes = tagRuleTypeService.findAll();
        Map<String, List<String>> tagRuleMap = Maps.newHashMap();
//        Map<String, List<TagRule>> tagRuleMap = Maps.newHashMap();
//        ruleTypes.forEach(e -> tagRuleMap.put(e.getRuleType().toString(), tagRuleService.getTagRuleByProfileCodeAndRuleType(profileCode, e.getRuleType())));

        Map<String, List<TagRuleSymbol>> tagRuleSymbolMap = Maps.newHashMap();
        List<PropertyTag> propertyTags = propertyTagService.findListBy("code", "warning");
        PropertyTag propertyTag = propertyTags.get(0);
        List<MetadataTag> metadataTags = metadataTagService.findByTagId(propertyTag.getId());
        String[] ids = metadataTags.stream().map(MetadataTag::getMdId).toArray(String[]::new);
        List<Metadata> metas = metadataService.findListByIdIn(ids);
        model.addAttribute("metas", metas);
        ruleTypes.forEach(e -> tagRuleSymbolMap.put(e.getRuleType().toString(), tagRuleSymbolService.getTagRuleSymbolByRuleType(e.getRuleType())));
        model.addAttribute("warningProject", StringUtils.isNotBlank(id) ? warningProjectService.findOne(id): new WarningProject());
        model.addAttribute("tagRuleMap", tagRuleMap);
        model.addAttribute("tagRuleSymbolMap", tagRuleSymbolMap);
        model.addAttribute("ruleTypes", ruleTypes);
        model.addAttribute("profileCode", profileCode);
        List<EtlJob> etlJobs = etlJobService.findByUnitId(getLoginInfo().getUnitId());
        etlJobs = etlJobs.stream().filter(job -> !Objects.equals(job.getEtlType(),EtlType.FLINK_STREAM.getValue())).collect(Collectors.toList());
        model.addAttribute("jobList", etlJobs);
        List<WarningProjectUser> userList = warningProjectUserService.findByProjectId(id);
        if (CollectionUtils.isNotEmpty(userList)) {
            StringBuilder userIds = new StringBuilder();
            StringBuilder userNames = new StringBuilder();
            userList.forEach(user->{
                userIds.append("," + user.getUsersId());
                userNames.append("," + user.getUserName());
            });
            model.addAttribute("userIds",userIds.toString().substring(1));
            model.addAttribute("userNames",userNames.toString().substring(1));
        }
        JSONArray teacherArray = treeRemoteService.deptTeacherForUnitInsetTree(getLoginInfo().getUnitId());
        model.addAttribute("teacherArray", JSONObject.toJSONString(teacherArray));
        return "/bigdata/extend/warningProject/warningProjectEdit.ftl";
    }

    @RequestMapping("/saveWarningProject")
    @ResponseBody
    public Response saveWarningProject(WarningProject warningProject, String tagRules) {
        try {
            // 判断项目名是否已存在
            long count = warningProjectService.countBy("projectName", warningProject.getProjectName());
            if (StringUtils.isBlank(warningProject.getId()) && count > 0) {
                return Response.error().message("项目名称不能重复").build();
            }
            if (StringUtils.isNotBlank(warningProject.getId())) {
                WarningProject one = warningProjectService.findOne(warningProject.getId());
                if (!one.getProjectName().equals(warningProject.getProjectName()) && count > 0) {
                    return Response.error().message("项目名称不能重复").build();
                }
            }
            List<TagRuleRelation> tagRuleRelations = JSON.parseArray(tagRules, TagRuleRelation.class);
            warningProject.setUnitId(getLoginInfo().getUnitId());
            warningProjectService.saveWarningProject(warningProject, tagRuleRelations);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.error().message("保存失败[" + e.getMessage() + "]").build();
        }
    }

    @RequestMapping("/deleteWarningProject")
    @ResponseBody
    public Response deleteWarningProject(String id) {
        try {
            warningProjectService.deleteWarningProject(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.error().message("删除失败[" + e.getMessage() + "]").build();
        }
    }

    @ResponseBody
    @RequestMapping("/getTagRuleRelationByProjectId")
    public Response getTagRuleRelationByProjectId(String id) {
        String profileCode = "warning";
        List<TagRuleRelation> tagRuleRelations = tagRuleRelationService.getTagRuleRelationByProjectId(id);
        Map<Short, List<TagRuleRelation>> map =tagRuleRelations.stream().collect(Collectors.groupingBy(TagRuleRelation::getGroupId,
                Collectors.toList()));
        return Response.ok().data(map).build();
    }

}
