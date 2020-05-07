package net.zdsoft.bigdata.extend.data.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.FormSet;
import net.zdsoft.bigdata.extend.data.service.FormSetService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bigdata/metadata")
public class FormSetController extends BigdataBaseAction {

    private static final Logger logger = LoggerFactory.getLogger(FormSetController.class);
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private FormSetService formSetService;
    @Resource
    private MetadataService metadataService;

    @RequestMapping("/forms")
    public String list(String formName, ModelMap map, HttpServletRequest request) {
        Pagination page = createPagination(request);
        List<FormSet> forms = formSetService.findListByOrder(formName, page);
        map.put("forms", forms);
        map.put("formName", formName);
        sendPagination(request, map, null, page);
        return "/bigdata/extend/forms/formList.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/forms/save")
    public Response save(FormSet form, String content) {
        try {
            formSetService.saveOrUpdate(form, content);
            return Response.ok().message("保存成功").data(form.getId()).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("保存失败，" + e.getMessage()).build();
        }
    }

    @ResponseBody
    @RequestMapping("/forms/delete")
    public Response delete(String id) {
        try {
            formSetService.delete(id);
            return Response.ok().message("删除成功").build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("删除失败，" + e.getMessage()).build();
        }
    }

    @RequestMapping("/forms/edit")
    public String edit(String id, ModelMap map) {
        boolean isEdit = false;
        FormSet form = new FormSet();
        if (StringUtils.isNotEmpty(id)) {
            isEdit = true;
            form = formSetService.findOne(id);
            Metadata meta = metadataService.findOne(form.getMdId());
            String html = formSetService.findHtmlByMdId(form.getMdId());
            if (meta != null) form.setMetaName(meta.getName());
            map.put("html", html);
            map.put("meta", meta);
        } else {
            List<Metadata> metas = metadataService.findPropertyMetadata();
            List<FormSet> forms = formSetService.findListByOrder("", null);
            Map<String, FormSet> maps = EntityUtils.getMap(forms, FormSet::getMdId);
            metas = metas.stream().filter(line -> !maps.containsKey(line.getId())).collect(Collectors.toList());
            map.put("metas", metas);
        }
        map.put("form", form);
        map.put("isEdit", isEdit);
        return "/bigdata/extend/forms/formEdit.ftl";
    }

}
