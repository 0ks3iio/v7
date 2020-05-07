package net.zdsoft.qulity.data.action;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.qulity.data.constant.QualityConstants;
import net.zdsoft.qulity.data.dto.QualityParamListDto;
import net.zdsoft.qulity.data.entity.QualityParam;
import net.zdsoft.qulity.data.service.QualityParamService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quality/param")
public class QualityParamAction extends BaseAction{
	
	@Autowired
	private QualityParamService qualityParamService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	
	@RequestMapping("/index")
	public String paramIndex(ModelMap map){
		
		return "/quality/param/qualityParamIndex.ftl";
	}
	@RequestMapping("/list")
	public String paramList(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<QualityParam> params = qualityParamService.findByUnitId(unitId, true);
		String[] paramCodes = {QualityConstants.QULITY_5FESTIVAL_MAX_NUMBER,QualityConstants.QULITY_CXDD_MAX_NUMBER,
				QualityConstants.QULITY_JX_MAX_NUMBER,QualityConstants.QULITY_PYXJ_MAX_NUMBER,QualityConstants.QULITY_QXXHD_MAX_NUMBER,
				QualityConstants.QULITY_STGG_MAX_NUMBER,QualityConstants.QULITY_TCGX_MAX_NUMBER,QualityConstants.QULITY_TYCJ_MAX_NUMBER,
				QualityConstants.QULITY_XKCJ_MAX_NUMBER,QualityConstants.QULITY_XKJS_MAX_NUMBER,QualityConstants.QULITY_XSGB_MAX_NUMBER,
				QualityConstants.QULITY_YYBS_MAX_NUMBER,QualityConstants.QULITY_YYKS_MAX_NUMBER,QualityConstants.QULITY_ZZBX_MAX_NUMBER,
				QualityConstants.QULITY_XN_MAX_NUMBER, QualityConstants.QULITY_STUFAM_QUERY_SWITCH};
		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_HIGH_SCHOOL,BaseConstants.SECTION_COLLEGE}), Grade.class);
		List<String> gradeIdList = EntityUtils.getList(gradeList, Grade::getId);
		if(CollectionUtils.isNotEmpty(params)){
			Set<String> hasCode = EntityUtils.getSet(params, QualityParam::getParamType);
			paramCodes = Arrays.stream(paramCodes).filter(e->!hasCode.contains(e)).toArray(String[]::new);
			List<String> hasGradeId = params.stream().filter(e->QualityConstants.QULITY_SHOW_JUNIOR_SWITCH.equals(e.getParamType())).map(QualityParam::getGradeId).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(gradeIdList)&&CollectionUtils.isNotEmpty(hasGradeId)){
				gradeIdList.removeAll(hasGradeId);
			}
		}else{
			params = Lists.newArrayList();
		}
		List<QualityParam> newParams = Lists.newArrayList();
		if(ArrayUtils.isNotEmpty(paramCodes)){
			for(String paramCode:paramCodes){
				QualityParam qulityParam = new QualityParam();
				qulityParam.setId(UuidUtils.generateUuid());
				qulityParam.setUnitId(getLoginInfo().getUnitId());
				qulityParam.setParamType(paramCode);
				newParams.add(qulityParam);
				params.add(qulityParam);
			}
		}
		if(CollectionUtils.isNotEmpty(gradeIdList)){
			for (String gradeId : gradeIdList) {
				QualityParam qulityParam = new QualityParam();
				qulityParam.setId(UuidUtils.generateUuid());
				qulityParam.setUnitId(getLoginInfo().getUnitId());
				qulityParam.setParamType(QualityConstants.QULITY_SHOW_JUNIOR_SWITCH);
				qulityParam.setGradeId(gradeId);
				newParams.add(qulityParam);
				params.add(qulityParam);
			}
		}
		if(!newParams.isEmpty()){
			qualityParamService.saveAll(newParams.toArray(new QualityParam[newParams.size()]));
		}
		Map<String,QualityParam> paramMap = params.stream().filter(e->!QualityConstants.QULITY_SHOW_JUNIOR_SWITCH.equals(e.getParamType()))
				.collect(Collectors.toMap(QualityParam::getParamType, Function.identity()));
		map.put("paramMap", paramMap);
		Map<String,QualityParam> paramMap2 = params.stream().filter(e->QualityConstants.QULITY_SHOW_JUNIOR_SWITCH.equals(e.getParamType()))
				.collect(Collectors.toMap(QualityParam::getGradeId, Function.identity()));
		map.put("paramMap2", paramMap2);
		List<Grade> gradeList1 = new ArrayList<>();
		List<Grade> gradeList2 = new ArrayList<>();
		for (Grade grade : gradeList) {
			grade.setGradeName(grade.getGradeName()+"("+StringUtils.substringBefore( grade.getOpenAcadyear(),"-")+"入学)");
			if(BaseConstants.SECTION_HIGH_SCHOOL==grade.getSection()){
				gradeList1.add(grade);
			}else{
				gradeList2.add(grade);
			}
		}
		map.put("gradeList1", gradeList1);
		map.put("gradeList2", gradeList2);
		return "/quality/param/qualityParamList.ftl";
	}

	@ResponseBody
	@RequestMapping("/save")
	public String paramSave(QualityParamListDto paramsDto){
		try{
			Map<String,QualityParam> paramMap = EntityUtils.getMap(paramsDto.getParams(), e->e.getParamType()+e.getGradeId());
			List<QualityParam> oldParams = qualityParamService.findByUnitId(getLoginInfo().getUnitId(), true);
			for(QualityParam param:oldParams){
				QualityParam qualityParam = paramMap.get(param.getParamType()+param.getGradeId());
				if(qualityParam!=null){
					param.setParam(qualityParam.getParam());
					param.setParamPer(qualityParam.getParamPer());
				}
			}
			qualityParamService.saveAll(oldParams.toArray(new QualityParam[oldParams.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存参数失败");
		}
		return success("保存参数成功");
	}

}
