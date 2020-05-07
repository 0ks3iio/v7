package net.zdsoft.bigdata.extend.data.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.TagRuleType;
import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.bigdata.extend.data.entity.WarningProjectUser;
import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.bigdata.extend.data.entity.WarningResultStat;
import net.zdsoft.bigdata.extend.data.service.TagRuleRelationService;
import net.zdsoft.bigdata.extend.data.service.TagRuleTypeService;
import net.zdsoft.bigdata.extend.data.service.WarningProjectService;
import net.zdsoft.bigdata.extend.data.service.WarningProjectUserService;
import net.zdsoft.bigdata.extend.data.service.WarningResultService;
import net.zdsoft.bigdata.extend.data.service.WarningResultStatService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2018/7/12 17:10.
 */
@Controller
@RequestMapping("/bigdata/warningResult")
public class WarningResultController extends BigdataBaseAction {

    @Resource
    private WarningResultService warningResultService;
    @Resource
    private WarningProjectService warningProjectService;
    @Resource
    private TagRuleRelationService tagRuleRelationService;
    @Resource
    private WarningResultStatService warningResultStatService;
    @Resource
    private TagRuleTypeService tagRuleTypeService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private WarningProjectUserService warningProjectUserService;

    @RequestMapping("/list")
    public String list(ModelMap model, HttpServletRequest request, String projectName) {
        String ownerId = getLoginInfo().getOwnerId();
        Pagination page = createPagination(request);
        List<WarningProjectUser> warningProjectUserList = warningProjectUserService.findByUsersId(ownerId);
        String[] projectIds = warningProjectUserList.stream().map(warn -> warn.getProjectId()).toArray(String[]::new);
        List<WarningProject> warningProjectList = warningProjectService.findByProjectNameAndIdInPage(projectName,projectIds,page);
        model.addAttribute("warningProjectList", warningProjectList);
        sendPagination(request, model,null, page);
        model.addAttribute("projectName", projectName);
        return "/bigdata/extend/warningResult/warningResultList.ftl";
    }

    @RequestMapping("/viewWarningResult")
    public String viewWarningResult(String id, ModelMap model) {
        String profileCode = "warning";
        Map<Short, String> tagRuleMap = tagRuleTypeService.findAll().stream().collect(Collectors.toMap(TagRuleType::getRuleType, TagRuleType::getRuleTypeName));
        List<TagRuleRelation> tagRuleRelations = tagRuleRelationService.getTagRuleRelationByProjectId(id);
        tagRuleRelations.forEach(e-> {
            e.setRuleTypeName(tagRuleMap.get(e.getRuleType()));
            Metadata metadata = metadataService.findOne(e.getMdId());
            e.setMetadataName(Optional.ofNullable(metadata).map(Metadata::getName).orElse("该元数据不存在"));
            MetadataTableColumn column = metadataTableColumnService.findOne(e.getMdColumnId());
            e.setMetadataColumnName(Optional.ofNullable(column).map(MetadataTableColumn::getName).orElse("该元数据字段不存在"));
        });
        Map<String, List<TagRuleRelation>> map = new HashMap<>();
        for (TagRuleRelation tagRuleRelation : tagRuleRelations) {
            map.computeIfAbsent(String.valueOf(tagRuleRelation.getGroupId()), k -> new ArrayList<>()).add(tagRuleRelation);
        }
        List<WarningResultStat> list = warningResultStatService.findListBy(new String[]{"projectId", "unitId"}, new String[]{id, getLoginInfo().getUnitId()});
        long totalRecord = list.size() > 0 ? list.get(0).getStatResult() : 0;
        model.addAttribute("totalRecord", totalRecord);
        WarningProject warningProject = warningProjectService.findOne(id);
        warningProject.setWarnTimes((int) totalRecord);
        model.addAttribute("warningProject", warningProject);
        model.addAttribute("tagRuleMap", map);
        model.addAttribute("projectId", id);
        return "/bigdata/extend/warningResult/warningResultDetail.ftl";
    }


    @RequestMapping("/detailList")
    public String detailList(String id, Integer pageIndex, ModelMap model) {
        Pagination page = new Pagination(10, false);
        page.setPageIndex(pageIndex == null ? 1 : pageIndex);
        List<WarningResult> warningResults = warningResultService.getWarningResultByProjectId(id, getLoginInfo().getUnitId(), page);
        warningResults.forEach(e -> e.setTips(e.getTips().replaceAll("@result", e.getResult())));
        model.addAttribute("page", page);
        model.addAttribute("warningResults", warningResults);
        model.addAttribute("hasNextPage", page.getMaxRowCount() > pageIndex * 10);
        return "/bigdata/extend/warningResult/detailList.ftl";
    }

    @RequestMapping("/clearWarningResult")
    @ResponseBody
    public Response clearWarningResult(String id) {
        try {
            warningProjectService.clearWarningResult(id, getLoginInfo().getUnitId());
        } catch (Exception e) {
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().build();
    }
}
