package net.zdsoft.bigdata.datav.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datav.QueryUtils;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramElement;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.Renders;
import net.zdsoft.bigdata.datav.render.crete.CreateException;
import net.zdsoft.bigdata.datav.render.crete.Creators;
import net.zdsoft.bigdata.datav.render.crete.NullRenderOption;
import net.zdsoft.bigdata.datav.service.DiagramElementService;
import net.zdsoft.bigdata.datav.service.DiagramParameterService;
import net.zdsoft.bigdata.datav.service.DiagramService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/9/27 14:58
 */
@Lazy(false)
@Controller
@RequestMapping("/bigdata/datav/diagram/render")
public class DiagramRenderController extends BigdataBaseAction {

    @Resource
    private DiagramService diagramService;
    @Resource
    private DiagramParameterService diagramParameterService;
    @Resource
    private DiagramElementService diagramElementService;
    @Resource
    private Renders renders;
    @Resource
    private ApiService apiService;

    private Logger logger = LoggerFactory.getLogger(DiagramRenderController.class);

    @ResponseBody
    @GetMapping(
            value = "/{screenId}/{diagramId}"
    )
    public Response doGetDiagramRender(@PathVariable("screenId") String screenId,
                                       @PathVariable("diagramId") String diagramId,
                                       @RequestParam(value = "interaction", defaultValue = "false") boolean interaction) {
        //加载配置，配置Option
        Diagram diagram = diagramService.findOne(diagramId);

        //是否因为交互引起的重新渲染
        boolean noReRender = diagram == null || (interaction && noNeedReRender(diagram, getRequest()));
        if (noReRender) {
            NullRenderOption renderOption = new NullRenderOption();
            renderOption.setRender(false);
            return Response.ok().data(JSONObject.toJSONString(renderOption)).build();
        }
        try {
            List<DiagramParameter> diagramParameters = diagramParameterService.getOrDefault(diagramId, diagram.getDiagramType());
            Result result = QueryUtils.query(diagram, diagram.getScreenId());
            if (result.hasError()) {
                logger.error("渲染图表出错", result.getException());
                String message = result.getException().getMessage();
                message = StringUtils.isBlank(message) ? "渲染图表出错" : message;
                return Response.error().message(message).build();
            }
            Object renderOption = Creators.create(result, diagram, getEarlyParameters(diagramParameters));
            renders.render(renderOption, diagramParameters, diagram);
            List<DiagramElement> elements = diagramElementService.getElementsByDiagramId(diagramId);
            for (DiagramElement element : elements) {
                renders.render(renderOption,
                        diagramParameterService.getOrDefault(element.getId(), element.getDiagramType()), convert(element));
            }

            //可交互

            return Response.ok().data(JSONObject.toJSONString(renderOption)).build();
        } catch (EntryUtils.DataException | CreateException e) {
            logger.error("图表渲染出错", e);
            String message = e.getMessage();
            message = StringUtils.isBlank(message) ? "渲染图表出错" : message;
            return Response.error().message(message).build();
        } catch (Throwable e) {
            logger.error("图表渲染出错", e);
            return Response.error().message("图表渲染出错").build();
        }
    }

    private boolean noNeedReRender(Diagram diagram, HttpServletRequest request) {
        String beCheckedString = null;
        //db
        if (Integer.valueOf(DataSourceType.DB.getValue()).equals(diagram.getDatasourceType())) {
            beCheckedString = diagram.getDatasourceValueSql();
        }
        else if (Integer.valueOf(DataSourceType.API.getValue()).equals(diagram.getDatasourceType())) {
            Api api = apiService.findOne(diagram.getDatasourceId());
            if (api != null) {
                beCheckedString = api.getUrl();
            }
        }
        if (beCheckedString == null) {
            return true;
        }
        Enumeration<String> ps = request.getParameterNames();
        while (ps.hasMoreElements()) {

            if (StringUtils.contains(beCheckedString, "${" + ps.nextElement() + "}")) {
                return false;
            }
        }
        return true;
    }

    private Diagram convert(DiagramElement element) {
        Diagram diagram = new Diagram();
        diagram.setDiagramType(element.getDiagramType());
        diagram.setId(element.getId());
        diagram.setDatasourceValueSql(element.getDatasourceValueSql());
        diagram.setDatasourceValueJson(element.getDatasourceValueJson());
        return diagram;
    }

    private List<EarlyParameter> getEarlyParameters(List<DiagramParameter> allParameters) {
        List<EarlyParameter> earlyParameters = new ArrayList<>();
        Map<String, List<DiagramParameter>> earlyMap = new HashMap<>();
        for (DiagramParameter allParameter : allParameters) {
            earlyMap.computeIfAbsent(allParameter.getGroupKey(), k -> new ArrayList<>()).add(allParameter);
        }
        for (Map.Entry<String, List<DiagramParameter>> entry : earlyMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                EarlyParameter earlyParameter = new EarlyParameter();
                earlyParameter.setGroupKey(entry.getKey());
                earlyParameter.setEarlyParameters(entry.getValue());
                earlyParameters.add(earlyParameter);
            }
        }
        return earlyParameters;
    }


}
