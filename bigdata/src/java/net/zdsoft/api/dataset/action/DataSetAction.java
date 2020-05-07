package net.zdsoft.api.dataset.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.api.base.entity.eis.ApiDataSet;
import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.api.base.service.ApiDataSetRuleService;
import net.zdsoft.api.base.service.ApiDataSetService;
import net.zdsoft.api.dataset.vo.DataSetRuleDto;
import net.zdsoft.api.dataset.vo.DataSetRuleDto.RuleXX;
import net.zdsoft.api.dataset.vo.DateSetDto;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/bigdata/dataset")
public class DataSetAction extends BigdataBaseAction {
	
	@Autowired
	private ApiDataSetService apiDataSetService;
	@Autowired
	private MetadataService metadataService;
	@Autowired
	private ApiDataSetRuleService apiDataSetRuleService;
	@Autowired
	private MetadataTableColumnService metadataTableColumnService;

	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap model,HttpServletRequest request, String dataSetName) throws JSONException {
		Pagination page = createPagination(request);
		List<ApiDataSet> dataSets = apiDataSetService.findAllByPage(page, dataSetName);
		List<Metadata> metadaList = metadataService.findListByIdIn(
				dataSets.stream().map(ApiDataSet::getMdId).toArray(String[]::new));
		Map<String, Metadata> mdIdMap = EntityUtils.getMap(metadaList, Metadata::getId);
        sendPagination(request, model,null, page);
        List<DateSetDto> dataSetDtos = new ArrayList<>();
        dataSets.forEach(c->{
        	DateSetDto dto = new DateSetDto();
        	dto.setId(c.getId());
        	dto.setName(c.getName());
        	dto.setCreationTime(c.getCreationTime());
        	dto.setMetadataId(c.getMdId());
        	dto.setMetadataName(mdIdMap.get(c.getMdId()).getName());
        	dto.setTableName(mdIdMap.get(c.getMdId()).getTableName());
        	dto.setRemark(c.getRemark());
        	dataSetDtos.add(dto);
        });
        model.addAttribute("dataSetDtoList", dataSetDtos);
        model.addAttribute("dataSetName", dataSetName);
        return "/api/dataset/dataSetList.ftl";
    }
	
	@RequestMapping("/edit")
    public String addDataSet(Model model, String id) {
        List<Metadata> metas = metadataService.findSupportApiMetadata();
        if(CollectionUtils.isNotEmpty(metas)){
        	model.addAttribute("metadatas", metas);
        	model.addAttribute("dataSet", StringUtils.isNotBlank(id) ? apiDataSetService.findOne(id): new WarningProject());
        	if(StringUtils.isNotBlank(id)){
        		List<ApiDataSetRule> apiDataSetRules = apiDataSetRuleService.findByDsId(id);
        		List<DataSetRuleDto> dataSetRuleDtos = new ArrayList<>();
        		if(CollectionUtils.isNotEmpty(apiDataSetRules)){
        			apiDataSetRules.forEach(c->{
        				DataSetRuleDto dto = new DataSetRuleDto();
        				dto.setId(c.getId());
        				dto.setParamType(c.getParamType());
        				dto.setRuleXXs(JSON.parseArray(c.getParamJson(), RuleXX.class));
        				dto.setValue(c.getParamValue());
        				if(StringUtils.isBlank(c.getParamValue()) || dto.getRuleXXs().size() == 1){
        					dto.setRelationName ("且");
        				}else{
        					dto.setRelationName((StringUtils.isNotBlank(c.getParamValue()) &&  (c.getParamValue().contains("and") || c.getParamValue().contains("AND")))
        							? "且" : "或");
        				}
        				dataSetRuleDtos.add(dto);
        			});
        		}
        		model.addAttribute("dataSetRuleDtos",dataSetRuleDtos);
        		List<MetadataTableColumn> columns = metadataTableColumnService.findByMetadataId(apiDataSetService.findOne(id).getMdId());
        		model.addAttribute("columnNames",columns);
        	}
        	return "/api/dataset/dataSetEdit.ftl";
        }
		return "/api/dataset/error.ftl";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Response saveDataSet(DateSetDto dataDateSetDto) {
        try {
        	String id = dataDateSetDto.getId();
            // 判断是否已存在
            long count = apiDataSetService.countBy("mdId", dataDateSetDto.getMetadataId());
            if (StringUtils.isBlank(id) && count > 0) {
                return Response.error().message("数据集不能重复").build();
            }
            if (StringUtils.isNotBlank(id)) {
                ApiDataSet one = apiDataSetService.findOne(id);
                if (!one.getMdId().equals(dataDateSetDto.getMetadataId()) && count > 0) {
                    return Response.error().message("项目名称不能重复").build();
                }
            }
            apiDataSetService.save(dataDateSetDto);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.error().message("保存失败[" + e.getMessage() + "]").build();
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response deleteDataSet(String id) {
        try {
        	apiDataSetService.deleteDataSet(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.error().message("删除失败[" + e.getMessage() + "]").build();
        }
    }

    @ResponseBody
    @RequestMapping("/getApiDataRule")
    public Response getApiDataRuleByDsId(String dsId) {
        List<ApiDataSetRule> apiDataSetRules = apiDataSetRuleService.findByDsId(dsId);
        return Response.ok().data(apiDataSetRules).build();
    }
    
    @ResponseBody
    @RequestMapping("/getMetadataTableColumn")
    public Response getMetadataTableColumnByMdId(String mdId) {
        List<MetadataTableColumn> columns = metadataTableColumnService.findByMetadataId(mdId);
        return Response.ok().data(columns).build();
    }
    
}
