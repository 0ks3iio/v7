package net.zdsoft.api.openapi.remote.openapi.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.api.base.dto.AuditApply;
import net.zdsoft.api.base.dto.OpenApiDeveloperAppCounter;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiDataSet;
import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.AppStatusEnum;
import net.zdsoft.api.base.enums.ApplyStatusEnum;
import net.zdsoft.api.base.exception.MailSenderException;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiDataSetRuleService;
import net.zdsoft.api.base.service.ApiDataSetService;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.base.service.ApiEntityService;
import net.zdsoft.api.base.service.ApiEntityTicketService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.api.openapi.remote.openapi.dto.ApiInterfaceApplyPass;
import net.zdsoft.api.openapi.remote.openapi.dto.ApplyIpInput;
import net.zdsoft.api.openapi.remote.openapi.vo.OpenApiAppDeveloperView;
import net.zdsoft.api.openapi.remote.openapi.vo.OpenApiDeveloperVo;
import net.zdsoft.api.openapi.remote.openapi.vo.OpenApiInterfaceApplyVo;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

/**
 * @author shenke
 * @since 2019/5/22 上午9:56
 */
@Controller
@RequestMapping("bigdata/developer")
public class OpenApiDeveloperManagementAction {

    private Logger logger = LoggerFactory.getLogger(OpenApiDeveloperManagementAction.class);

    @Resource
    private ApiDeveloperService apiDeveloperService;
    @Resource
    private OpenApiAppService openApiAppService;
    @Resource
    private ApiApplyService apiApplyService;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ApiInterfaceService apiInterfaceService;
    @Resource
    private ApiEntityTicketService apiEntityTicketService;
    @Resource
    private ApiEntityService apiEntityService;
    @Resource
    private BigLogService bigLogService;
    @Autowired
	private MetadataRelationService metadataRelationService;
    @Autowired
    private ApiDataSetService apiDataSetService;
    @Autowired
    private ApiDataSetRuleService apiDataSetRuleService;

    @RequestMapping("index")
    public String execute(Model model) {
        List<ApiDeveloper> developerList = apiDeveloperService.getAllOdereByCreationTime();
        String[] developerIds = developerList.stream().map(ApiDeveloper::getId).toArray(String[]::new);
        Map<String, Long> counterMap = openApiAppService.getDeveloperAppCounter(developerIds).stream()
                .collect(Collectors.toMap(OpenApiDeveloperAppCounter::getDeveloperId, OpenApiDeveloperAppCounter::getAppNumber));

        Map<String, Long> applyMap = apiApplyService.getAuditApplies(developerList.stream().map(ApiDeveloper::getTicketKey).toArray(String[]::new))
                .stream().collect(Collectors.toMap(AuditApply::getTicketKey, AuditApply::getCount));
        List<OpenApiDeveloperVo> developerVos = developerList.stream().map(e -> {
            OpenApiDeveloperVo developerVo = new OpenApiDeveloperVo();
            developerVo.setAppNumber(counterMap.getOrDefault(e.getId(), 0L).intValue());
            developerVo.setCreationTime(e.getCreationTime());
            developerVo.setId(e.getId());
            developerVo.setRealName(e.getRealName());
            developerVo.setHasApply(Optional.ofNullable(applyMap.get(e.getTicketKey())).orElse(0L) > 0);
            return developerVo;
        }).collect(Collectors.toList());
        model.addAttribute("developers", developerVos);
        return "/api/developer/developer-index.ftl";
    }

    @RequestMapping("view")
    public String doView(String developerId, Model model) {
        //开发者信息
    	ApiDeveloper developer = apiDeveloperService.findOne(developerId);
        model.addAttribute("developer", developer);
        //开发者应用信息
        String fileUrl = sysOptionRemoteService.findValue(Constant.FILE_URL);
        List<OpenApiApp> apiApps = openApiAppService.getAppsByDeveloperId(developerId).stream()
                .filter(e->!e.getStatus().equals(AppStatusEnum.UNCOMMITTED.getValue())
                        && !e.getStatus().equals(AppStatusEnum.NOTPASS.getValue())
                        && !e.getStatus().equals(AppStatusEnum.AUDIT.getValue()))
                .collect(Collectors.toList());
        List<OpenApiAppDeveloperView> appDeveloperViews = apiApps.stream()
                .map(e-> OpenApiAppDeveloperView.convert(e, fileUrl)).collect(Collectors.toList());
        model.addAttribute("apps", appDeveloperViews);
        return "/api/developer/developer-view2.ftl";
    }

    @RequestMapping("interface")
    public String doGetApplyInterface(String developerId, Integer applyStatus, Model model) {
        ApiDeveloper developer = apiDeveloperService.findOne(developerId);
        //接口申请信息
        String ticketKey = developer.getTicketKey();
        ApplyStatusEnum applyStatusEnum = Objects.requireNonNull(ApplyStatusEnum.from(applyStatus));
        List<ApiApply> applies = apiApplyService.findByTicketKeyAndStatus(ticketKey, applyStatusEnum.getValue());

        String[] interfaceIds = applies.stream().map(ApiApply::getInterfaceId).toArray(String[]::new);
        //查询申请的接口
        List<ApiInterface> interfaces = apiInterfaceService.findListByIn("id", interfaceIds);
        Map<String, List<ApiDataSetRule>> apiapplyIdMap = Maps.newHashMap();
        Map<String, List<ApiDataSetRule>> allRuleMap = Maps.newHashMap();
        
    	Map<String, String> interfaceIdMap = EntityUtils.getMap(interfaces, ApiInterface::getId, ApiInterface::getMetadataId);
    	Set<String> metadataidSet =  EntityUtils.getSet(interfaces, ApiInterface::getMetadataId);
    	List<ApiDataSet> apiDataSets = apiDataSetService.findByMdIdIn(metadataidSet.toArray(new String[metadataidSet.size()]));
    	Map<String, String> metadaIdMap = EntityUtils.getMap(apiDataSets, ApiDataSet::getMdId, ApiDataSet::getId);
    	Set<String> dsIds = EntityUtils.getSet(apiDataSets, ApiDataSet::getId);
    	List<ApiDataSetRule> apiDataSetRules = apiDataSetRuleService.findByDsIdIn(dsIds.toArray(new String[dsIds.size()]));
    	apiDataSetRules = EntityUtils.filter2(apiDataSetRules, t->{
				return t.getParamValue() != null;
		});
    	Map<String, List<ApiDataSetRule>> dsIdMap = EntityUtils.getListMap(apiDataSetRules, ApiDataSetRule::getDsId, Function.identity());
    	interfaces.forEach(c->{
    		String metadaId = interfaceIdMap.get(c.getId());
    		String dsId = metadaIdMap.get(metadaId);
    		allRuleMap.put(c.getId(), dsIdMap.get(dsId));
    	});
    	
        if(ApplyStatusEnum.IN_VERIFY.getValue() != applyStatus){	
        	List<ApiApply> apiApplies = EntityUtils.filter2(applies, t->{
        		return StringUtils.isNotBlank(t.getDataSetRuleIds());
        	});
        	Map<String, String> appIdMap = EntityUtils.getMap(apiApplies, ApiApply::getId, ApiApply::getDataSetRuleIds);
        	Set<String> datasetRuleIds = new HashSet<>();
        	appIdMap.forEach((k,v)->{
        		String[] ruleIds = v.split(",");
        		datasetRuleIds.addAll(Arrays.asList(ruleIds));
        	});
        	List<ApiDataSetRule> apiDataSetRules1 = apiDataSetRuleService.findListByIdIn(datasetRuleIds.toArray(new String[datasetRuleIds.size()]));
        	Map<String,ApiDataSetRule> ruleIdMap = EntityUtils.getMap(apiDataSetRules1, ApiDataSetRule::getId);
        	appIdMap.forEach((k,v)->{
        		String[] ruleIds = v.split(",");
        		List<ApiDataSetRule> dataSetRules = new ArrayList<>();
        		for (int i = 0; i < ruleIds.length; i++) {
        			String rid = ruleIds[i];
        			dataSetRules.add(ruleIdMap.get(rid));
        		}
        		apiapplyIdMap.put(k, dataSetRules);
        	});
        }
        
        Map<String, ApiInterface> applyInterfaces = interfaces.stream().collect(Collectors.toMap(ApiInterface::getId, Function.identity()));
        Map<String, List<String>> interfaceIdMap1 = new HashMap<String, List<String>>();
        if(CollectionUtils.isNotEmpty(applies)){
        	interfaceIdMap1.putAll(apiEntityTicketService.
        			fingByTicketKeyAndInterfaceIdIn(ticketKey,interfaceIds).stream().collect(
        					Collectors.groupingBy(ApiEntityTicket::getInterfaceId, Collectors.mapping(ApiEntityTicket::getEntityId, Collectors.toList())))); 
        }
        
        List<OpenApiInterfaceApplyVo> vos = applies.stream().map(e->{
            OpenApiInterfaceApplyVo applyVo = new OpenApiInterfaceApplyVo();
            applyVo.setId(e.getId());
            ApiInterface apiInterface = applyInterfaces.get(e.getInterfaceId());
            applyVo.setName(apiInterface.getTypeName());
            applyVo.setUri(apiInterface.getUri());
            applyVo.setInterfaceId(apiInterface.getId());
            List<ApiEntity> fields = null;
            if(MapUtils.isNotEmpty(interfaceIdMap1) &&  interfaceIdMap1.get(e.getInterfaceId()) != null){
            	String[] fieldIds = interfaceIdMap1.get(e.getInterfaceId()).stream().toArray(String[]::new);
            	fields = apiEntityService.findListByIds(fieldIds);
            }
            applyVo.setAlldataSetRules(allRuleMap.get(e.getInterfaceId()));
            if(ApplyStatusEnum.IN_VERIFY.getValue() != applyStatus){
            	if(MapUtils.isNotEmpty(apiapplyIdMap) && CollectionUtils.isNotEmpty(apiapplyIdMap.get(e.getId())) && apiapplyIdMap.get(e.getId()).get(0) != null){
            		applyVo.setDataSetRule(apiapplyIdMap.get(e.getId()).get(0));
            	}
            }
            applyVo.setFields(fields);
            applyVo.setMethod(apiInterface.getMethodType().toUpperCase());
            applyVo.setMaxCount(Optional.ofNullable(e.getLimitEveryTime()).orElse(500));
            applyVo.setMaxInvokeNumber(Optional.ofNullable(e.getMaxNumDay()).orElse(2000));
            applyVo.setStatus(e.getStatus());
            return applyVo;
        }).collect(Collectors.toList());

        model.addAttribute("applies", vos);
        return "/api/developer/developer-interfaceList.ftl";
    }

	/**
     * 通过申请
     */
    @ResponseBody
    @RequestMapping("interface-apply/pass")
    public Object doPassInterfaceApply(ApiInterfaceApplyPass applyPass) {
        if (CollectionUtils.isEmpty(applyPass.getFieldIds())) {
            return Response.error().message("至少保留一个字段").build();
        }
        apiApplyService.passApply(applyPass);
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("interface-apply/modify-limit")
    public Object doModifyLimit(ApiInterfaceApplyPass pass) {
        ApiApply apiApply = apiApplyService.findOne(pass.getId());
        apiApply.setLimitEveryTime(pass.getMaxCount());
        apiApply.setMaxNumDay(pass.getMaxInvokeNumber());
        apiApply.setModifyTime(new Date());
        apiApply.setDataSetRuleIds(pass.getDataSetRuleId());
        apiApplyService.save(apiApply);
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("interface-apply/modify-ip")
    public Object doModifyIp(ApplyIpInput applyIpInput) {
        ApiDeveloper developer = apiDeveloperService.findOne(applyIpInput.getId());
        if (developer == null) {
            return Response.error().message("开发者不存在，请刷新页面重试").build();
        }
        developer.setIps(applyIpInput.getIps());
        apiDeveloperService.save(developer);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-modifyIp");
        logDto.setDescription("开发者"+developer.getRealName()+"的ip信息 ");
        logDto.setOldData(developer);
        ApiDeveloper newDeveloper = apiDeveloperService.findOne(applyIpInput.getId());
        logDto.setNewData(newDeveloper);
        logDto.setBizName("开发者管理");
        bigLogService.updateLog(logDto);

        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("interface-apply/un-pass")
    public Object doUnPassInterfaceApply(@RequestParam("applyId") String applyId) {
        apiApplyService.updateBatchStatus(ApplyStatusEnum.UNPASS_VERIFY.getValue(), new Date(), new String[]{applyId});
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("interface-apply/delete")
    public Object doDeleteInterfaceApply(@RequestParam("applyId") String applyId) {
        apiApplyService.deleteApply(applyId);
        return Response.ok().build();
    }

    @ResponseBody
    @RequestMapping("reset-password")
    public Object doResetPassword(String developerId) {
        //
        String randomPassword = "ZDSoft" + UuidUtils.generateUuid().substring(0, 8) + "@.com";
        try {
            apiDeveloperService.updatePasswordAndSendEmail(PWD.encodeIfNot(randomPassword), developerId);
        } catch (MailSenderException e) {
            logger.error("密码重置失败", e);
            return Response.error().message("密码重置邮件发送失败").build();
        } catch (Throwable e) {
            logger.error("密码重置失败", e);
            return Response.error().message("密码重置失败").build();
        }
        return Response.ok().message("密码已重置，请检查您的邮箱").build();
    }
}
