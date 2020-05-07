package net.zdsoft.bigdata.metadata.action;

import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.dataimport.entity.QualityResultDetailInfo;
import net.zdsoft.bigdata.dataimport.utils.ExcelExportUtils;
import net.zdsoft.bigdata.metadata.entity.QualityDim;
import net.zdsoft.bigdata.metadata.entity.QualityDimResult;
import net.zdsoft.bigdata.metadata.entity.QualityResultDetail;
import net.zdsoft.bigdata.metadata.entity.QualityRule;
import net.zdsoft.bigdata.metadata.service.QualityDimResultService;
import net.zdsoft.bigdata.metadata.service.QualityDimService;
import net.zdsoft.bigdata.metadata.service.QualityResultDetailService;
import net.zdsoft.bigdata.metadata.service.QualityRuleService;
import net.zdsoft.bigdata.stat.service.BgDataQualityStatService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bigdata/setting")
public class QualitySettingController extends BigdataBaseAction {

	@Autowired
	private QualityDimService qualityDimService;

	@Autowired
	private QualityRuleService qualityRuleService;

	@Autowired
	private BgDataQualityStatService bgDataQualityStatService;

	@Autowired
	private QualityDimResultService qualityDimResultService;

	@Autowired
	private QualityResultDetailService qualityResultDetailService;

	@Resource
	private BigLogService bigLogService;

	@RequestMapping("/quality")
	public String index(ModelMap map) {
		return "/bigdata/metadata/quality/setting/qualitySetting.ftl";
	}

	@RequestMapping("/quality/list")
	public String list(String type, String dimCode, ModelMap map) {
		if ("dim".equals(type)) {
			List<QualityDim> dimList = qualityDimService
					.findQualityDimsByType(2);
			map.put("dimList", dimList);
			return "/bigdata/metadata/quality/setting/qualityDimList.ftl";
		} else if ("rule".equals(type)) {
			List<QualityRule> ruleList = qualityRuleService
					.findQualityRulesByDimCode(dimCode);
			map.put("ruleList", ruleList);
			List<QualityDim> dimList = qualityDimService
					.findQualityDimsByType(2);
			map.put("dimList", dimList);
			map.put("dimCode", dimCode);
			return "/bigdata/metadata/quality/setting/qualityRuleList.ftl";
		}
		return "/bigdata/v3/common/error.ftl";
	}

	@RequestMapping("/quality/dimEdit")
	public String editDim(String id, ModelMap map) {
		QualityDim dim = StringUtils.isNotBlank(id) ? qualityDimService
				.findOne(id) : new QualityDim();
		map.put("dim", dim);
		return "/bigdata/metadata/quality/setting/qualityDimEdit.ftl";
	}

	/**
	 * 更新质量维度
	 * @param qualityDim
	 * @return
	 */
	@RequestMapping("/quality/saveDim")
	@ResponseBody
	public Response saveDim(QualityDim qualityDim) {
		try {
			// List<QualityDim> dimList = qualityDimService
			// .findQualityDimsByType(2);
			// int totalWeight = 0;
			// for (QualityDim dim : dimList) {
			// if (dim.getId().equals(qualityDim.getId())) {
			// totalWeight += qualityDim.getWeight();
			// } else {
			// totalWeight += dim.getWeight();
			// }
			// }
			// if (totalWeight != 100) {
			// return Response.error().message("权重总和不等于100").build();
			// }
			QualityDim oldQualityDim = qualityDimService.findOne(qualityDim.getId());
			qualityDimService.updateQualityDim(qualityDim);

			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-qualityDim");
			logDto.setDescription("质量维度 "+oldQualityDim.getName());
			logDto.setOldData(oldQualityDim);
			logDto.setNewData(qualityDim);
			logDto.setBizName("数据质量设置");
			bigLogService.updateLog(logDto);

			return Response.ok().message("保存成功").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/quality/ruleEdit")
	public String editRule(String id, String dimCode, ModelMap map) {
		if (StringUtils.isNotBlank(id)) {
			QualityRule rule = StringUtils.isNotBlank(id) ? qualityRuleService
					.findOne(id) : new QualityRule();
			map.put("rule", rule);
		} else {
			QualityRule rule = new QualityRule();
			if (StringUtils.isNotBlank(dimCode))
				rule.setDimCode(dimCode);
			map.put("rule", rule);
		}
		List<QualityDim> dimList = qualityDimService.findQualityDimsByType(2);
		map.put("dimList", dimList);
		return "/bigdata/metadata/quality/setting/qualityRuleEdit.ftl";
	}

	/**
	 * 新增 更新质量规则
	 * @param qualityRule
	 * @return
	 */
	@RequestMapping("/quality/saveRule")
	@ResponseBody
	public Response saveRule(QualityRule qualityRule) {
		try {
			qualityRuleService.saveQualityRule(qualityRule);
			return Response.ok().message("保存成功").build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	@ResponseBody
	@ControllerInfo("删除rule")
	@RequestMapping("/quality/deleteRule")
	public String deleteRule(String id) {
		try {
			// if (count > 0) {
			// return error(api.getName() + "被引用，不能删除。");
			// }
			QualityRule oldQualityRule = qualityRuleService.findOne(id);
			qualityRuleService.delete(id);
			//业务日志埋点  删除
			LogDto logDto=new LogDto();
			logDto.setBizCode("delete-qualityRule");
			logDto.setDescription("质量规则 "+oldQualityRule.getRuleName());
			logDto.setBizName("数据质量设置");
			logDto.setOldData(oldQualityRule);
			bigLogService.deleteLog(logDto);

			return success("删除规则成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@RequestMapping("/quality/stat")
	public String stat(ModelMap map) {
		List<QualityDimResult> resultList = qualityDimResultService
				.findQualityDimResultsByTypeDesc(1);
		map.put("resultList", resultList);
		return "/bigdata/metadata/quality/setting/qualityStat.ftl";
	}

	@RequestMapping("/quality/stat/computer")
	@ResponseBody
	public Response statComputer(Integer saveDetail) {
		try {
			bgDataQualityStatService.dataQualityStat(1 == saveDetail ? true
					: false);
			return Response.ok().message("统计成功").build();
		} catch (Exception e) {
			return Response.error().message("统计失败" + e.getMessage()).build();
		}
	}

	@RequestMapping("/quality/stat/delete")
	@ResponseBody
	public Response statDelete() {
		try {
			List<QualityResultDetail> QualityResultDetails = qualityResultDetailService.findAll();
			qualityDimResultService.deleteAll();
			qualityResultDetailService.deleteAll();

			//业务日志埋点  删除所有
			LogDto logDto=new LogDto();
			logDto.setBizCode("delete-all");
			logDto.setDescription("所有统计结果");
			logDto.setBizName("数据质量设置");
			logDto.setOldData(QualityResultDetails);
			bigLogService.deleteLog(logDto);

			return Response.ok().message("清除成功").build();
		} catch (Exception e) {
			return Response.error().message("清除失败" + e.getMessage()).build();
		}
	}

	@RequestMapping("/quality/stat/export")
	@ResponseBody
	public Response statExport() {
		try {
			List<QualityResultDetail> resultList = qualityResultDetailService
					.findAll();
			String fileName = "数据统计明细.xlsx";
			List<QualityResultDetailInfo> infoList = new ArrayList<QualityResultDetailInfo>();
			resultList.forEach(result -> {
				QualityResultDetailInfo info = new QualityResultDetailInfo();
				info.setColumnName(result.getColumnName());
				info.setResult(result.getResult());
				info.setTableName(result.getTableName());
				infoList.add(info);
			});

			SysOptionRemoteService sysOptionRemoteService = (SysOptionRemoteService) Evn
					.getBean("sysOptionRemoteService");
			String systemFilePath = sysOptionRemoteService
					.findValue(Constant.FILE_PATH);
			String filePath = systemFilePath + java.io.File.separator
					+ "export" + java.io.File.separator + "quality"
					+ java.io.File.separator + "detail";
			String fullFilePath = filePath + java.io.File.separator + fileName;
			ExcelExportUtils.export(fileName, filePath, 1, infoList,
					QualityResultDetailInfo.class);
			return Response.ok().message("导出成功").data(fullFilePath).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.error().message("导出失败" + e.getMessage()).build();
		}

	}
}
