package net.zdsoft.comprehensive.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dto.CompreParamInfoDto;
import net.zdsoft.comprehensive.entity.CompreParameter;
import net.zdsoft.comprehensive.entity.CompreParameterInfo;
import net.zdsoft.comprehensive.service.CompreParamInfoService;
import net.zdsoft.comprehensive.service.CompreParameterService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comprehensive")
public class ComprParameterAction extends BaseAction {

	@Autowired
	private CompreParameterService compreParameterService;

	@Autowired
	private CompreParamInfoService compreParamInfoService;

	@RequestMapping("/quality/score/index/page")
	@ControllerInfo(value = "综合素质折分设置index")
	public String showIndex(ModelMap map, HttpSession httpSession) {
		return "/comprehensive/quality/qualityIndex.ftl";
	}

	@RequestMapping("/quality/score/listOther/page")
	@ControllerInfo(value = "显示其他科目折分列表")
	public String showOtherList(ModelMap map, HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		//列头
		List<CompreParameter> compreParameterList = getCompreParameterListByUnitId(unitId);

		// 封装表内容
		List<CompreParameterInfo> compreParameterInfoShowList = new ArrayList<CompreParameterInfo>();
		
		List<String> infoKeySet = EntityUtils.getList(compreParameterList, CompreParameter::getParkey);
		List<CompreParameterInfo> compreParameterInfoList = compreParamInfoService
				.findByInfoKey(unitId, infoKeySet.toArray(new String[] {}));
		Map<String,List<CompreParameterInfo>> infoListMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(compreParameterInfoList)) {
			for(CompreParameterInfo c:compreParameterInfoList) {
				if(!infoListMap.containsKey(c.getInfoKey())) {
					infoListMap.put(c.getInfoKey(), new ArrayList<>());
				}
				infoListMap.get(c.getInfoKey()).add(c);
			}
		}
		//显示的列
		for(CompreParameter compreParameter : compreParameterList) {
			String infoKey = compreParameter.getParkey();
			List<CompreParameterInfo> list1 = infoListMap.get(infoKey);
			if(CollectionUtils.isNotEmpty(list1)) {
				for(CompreParameterInfo inf:list1) {
					CompreParameterInfo compreParamInfoShow = new CompreParameterInfo();
					int prefix = inf.getMcPrefix();
					int suffix = inf.getMcSuffix();
					compreParamInfoShow.setMcPrefix(prefix);
					compreParamInfoShow.setMcSuffix(suffix);
					compreParameterInfoShowList.add(compreParamInfoShow);
				}
				break;
			}
		}
		if(CollectionUtils.isNotEmpty(compreParameterInfoShowList)) {
			//按顺序
			for(CompreParameter compreParameter : compreParameterList) {
				String infoKey = compreParameter.getParkey();
				List<CompreParameterInfo> list1 = infoListMap.get(infoKey);
				Map<String, CompreParameterInfo> mmmp = EntityUtils.getMap(list1, e->(e.getMcPrefix()+"_"+e.getMcSuffix()));
				for(CompreParameterInfo show:compreParameterInfoShowList) {
					if(mmmp.containsKey(show.getMcPrefix()+"_"+show.getMcSuffix())) {
						show.getScoreList().add(mmmp.get(show.getMcPrefix()+"_"+show.getMcSuffix()).getScore());
					}else {
						show.getScoreList().add(null);
					}
				}
			}
		}
		
//		// 封装第一列--这个都是一起维护 所以只需要取第一列的值
//		String infoKey = compreParameterList.get(0).getParkey();
//		List<CompreParameterInfo> compreParameterInfoList = compreParamInfoService
//				.findByInfoKey(unitId, infoKey);
//		for (CompreParameterInfo compreParameterInfo : compreParameterInfoList) {
//			CompreParameterInfo compreParamInfoShow = new CompreParameterInfo();
//			int prefix = compreParameterInfo.getMcPrefix();
//			int suffix = compreParameterInfo.getMcSuffix();
//			compreParamInfoShow.setMcPrefix(prefix);
//			compreParamInfoShow.setMcSuffix(suffix);
//			compreParameterInfoShowList.add(compreParamInfoShow);
//		}
//		// 封装其他数据
//		for (CompreParameter compreParameter : compreParameterList) {
//			infoKey = compreParameter.getParkey();
//			compreParameterInfoList = compreParamInfoService
//					.findByInfoKey(unitId, infoKey);
//			int index = 0;
//			for (CompreParameterInfo compreParameterInfo : compreParameterInfoList) {
//				compreParameterInfoShowList.get(index).getScoreList()
//						.add(compreParameterInfo.getScore());
//				index++;
//			}
//		}

//		compreParameterInfoShowList = SUtils.dt(
//				SUtils.s(compreParameterInfoShowList),
//				new TR<ArrayList<CompreParameterInfo>>() {
//				});

		map.put("compreParamInfoList", compreParameterInfoShowList);

		return "/comprehensive/quality/qualityOtherList.ftl";
	}

	@RequestMapping("/quality/score/listEnglish/page")
	@ControllerInfo(value = "显示英语科目折分列表")
	public String showEnglishList(ModelMap map, HttpSession httpSession) {

		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();

		List<CompreParameterInfo> compreParameterInfoShowList = compreParamInfoService
				.findByInfoKey(unitId,
						CompreStatisticsConstants.INFO_KEY_2);

		compreParameterInfoShowList = SUtils.dt(
				SUtils.s(compreParameterInfoShowList),
				new TR<ArrayList<CompreParameterInfo>>() {
				});

		map.put("compreParamInfoList", compreParameterInfoShowList);

		return "/comprehensive/quality/qualityEnglishList.ftl";

	}
	
	@RequestMapping("/quality/score/listXk/page")
	@ControllerInfo(value = "显示学考折分列表")
	public String showXkList(ModelMap map, HttpSession httpSession) {

		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();

		List<CompreParameterInfo> compreParameterInfoShowList = compreParamInfoService
				.findByInfoKey(unitId,
						CompreStatisticsConstants.INFO_KEY_5);

		compreParameterInfoShowList = SUtils.dt(
				SUtils.s(compreParameterInfoShowList),
				new TR<ArrayList<CompreParameterInfo>>() {
				});

		map.put("compreParamInfoList", compreParameterInfoShowList);

		return "/comprehensive/quality/qualityXkList.ftl";

	}

	@ResponseBody
	@RequestMapping("/quality/score/other/save")
	@ControllerInfo(value = "学科成绩折分设置保存")
	public String doSaveOtherList(CompreParamInfoDto dto,
			HttpSession httpSession) {

		List<CompreParameterInfo> compreParamInfoList = dto
				.getCompreParamInfoList();
		List<CompreParameterInfo> compreParamInfoListSave = new ArrayList<CompreParameterInfo>();
		
		// 先删除
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		String[] parkeys = getParkeys(unitId);
		
		// 空
		if (CollectionUtils.isEmpty(compreParamInfoList)) {
			//清空原始数据
			compreParamInfoService.deleteAllByInfoKeys(unitId, parkeys);
			return success("无需保存数据！");
		}

		int[] scope = new int[101];
		// 名次区间有重复
		for (CompreParameterInfo compreParameterInfo : compreParamInfoList) {
			if (compreParameterInfo == null) {
				continue;
			}
			if (compreParameterInfo.getMcPrefix() == null) {
				continue;
			}
			int prefix = compreParameterInfo.getMcPrefix();
			int suffix = compreParameterInfo.getMcSuffix();
			if (prefix > suffix) {
				return error(prefix + "到" + suffix + "名次段大小错误");
			}
			if (prefix == suffix) {
				return error(prefix + "到" + suffix + "名次段不能相等");
			}
			for (int i = prefix; i <= suffix; i++) {
				if (scope[i] == 1) {
					if (i == prefix || i == suffix) {
						continue;
					} else {
						return error(prefix + "到" + suffix + "名次段区间有重复");
					}
				}
				scope[i] = 1;
			}
			compreParamInfoListSave.add(compreParameterInfo);
		}

		// 名次段有空缺
		for (int i = 0; i < 101; i++) {
			if (scope[i] == 0) {
				return error("名次段有空缺");
			}
		}

		if (CollectionUtils.isEmpty(compreParamInfoListSave)) {
			compreParamInfoService.deleteAllByInfoKeys(unitId, parkeys);
			return success("无需保存数据！");
		}

		try {
			compreParamInfoService.deleteAndSaveOther(unitId, parkeys,
					compreParamInfoListSave);
		} catch (Exception e) {
			e.printStackTrace();
			return error("操作失败");
		}

		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/quality/score/english/save")
	@ControllerInfo(value = "英语成绩折分保存")
	public String doSaveEnglishList(CompreParamInfoDto dto,
			HttpSession httpSession) {

		List<CompreParameterInfo> compreParamInfoList = dto
				.getCompreParamInfoList();
		List<CompreParameterInfo> compreParamInfoListSave = new ArrayList<CompreParameterInfo>();

		// 先删除
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();

		List<String> parkey = new ArrayList<String>();
		parkey.add(CompreStatisticsConstants.INFO_KEY_2);
		String[] parkeys = parkey.toArray(new String[parkey.size()]);
		// 空
		if (CollectionUtils.isEmpty(compreParamInfoList)) {
			compreParamInfoService.deleteAllByInfoKeys(unitId, parkeys);
			return success("无需保存数据！");
		}

		int[] scope = new int[101];
		// 名次区间有重复
		for (CompreParameterInfo compreParameterInfo : compreParamInfoList) {
			if (compreParameterInfo == null) {
				continue;
			}
			if (compreParameterInfo.getMcPrefix() == null) {
				continue;
			}
			int prefix = compreParameterInfo.getMcPrefix();
			int suffix = compreParameterInfo.getMcSuffix();
			if (prefix > suffix) {
				return error(prefix + "到" + suffix + "名次段大小错误");
			}
			if (prefix == suffix) {
				return error(prefix + "到" + suffix + "名次段不能相等");
			}
			for (int i = prefix; i <= suffix; i++) {
				if (scope[i] == 1) {
					if (i == prefix || i == suffix) {
						continue;
					} else {
						return error(prefix + "到" + suffix + "名次段区间有重复");
					}
				}
				scope[i] = 1;
			}
			compreParamInfoListSave.add(compreParameterInfo);
		}

		// 名次段有空缺
		for (int i = 0; i < 101; i++) {
			if (scope[i] == 0) {
				return error("名次段有空缺");
			}
		}
		if (CollectionUtils.isEmpty(compreParamInfoListSave)) {
			compreParamInfoService.deleteAllByInfoKeys(unitId, parkeys);
			return success("无需保存数据！");
		}

		try {
			compreParamInfoService.deleteAndSaveEnglish(unitId, parkeys,
					compreParamInfoListSave);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}

		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/quality/score/xk/save")
	@ControllerInfo(value = "学考折分保存")
	public String doSaveXkList(CompreParamInfoDto dto,
			HttpSession httpSession) {

		List<CompreParameterInfo> compreParamInfoList = dto
				.getCompreParamInfoList();
		List<CompreParameterInfo> compreParamInfoListSave = new ArrayList<CompreParameterInfo>();

		// 先删除
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();

		List<String> parkey = new ArrayList<String>();
		parkey.add(CompreStatisticsConstants.INFO_KEY_5);
		String[] parkeys = parkey.toArray(new String[parkey.size()]);
		// 空
		if (CollectionUtils.isEmpty(compreParamInfoList)) {
			compreParamInfoService.deleteAllByInfoKeys(unitId, parkeys);
			return success("无需保存数据！");
		}

		// 名次区间有重复
		Set<String> gradeScores=new HashSet<>();
		for (CompreParameterInfo compreParameterInfo : compreParamInfoList) {
			if (compreParameterInfo == null) {
				continue;
			}
			String gradeScore=StringUtils.trim(compreParameterInfo.getGradeScore());
			if(StringUtils.isBlank(gradeScore)) {
				continue;
			}
			
			if(gradeScores.contains(gradeScore)) {
				return error("等第名称"+gradeScore+"重复");
			}
			compreParamInfoListSave.add(compreParameterInfo);
			gradeScores.add(gradeScore);
		}

		if (CollectionUtils.isEmpty(compreParamInfoListSave)) {
			compreParamInfoService.deleteAllByInfoKeys(unitId, parkeys);
			return success("无需保存数据！");
		}

		try {
			compreParamInfoService.deleteAndSaveXk(unitId, parkeys,
					compreParamInfoListSave);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}

		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/quality/score/other/delete")
	@ControllerInfo("学科成绩折分删除")
	public String doDeleteCompreParameterInfo(String mcPrefix, String mcSuffix,
			HttpSession httpSession) {

		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();

		String[] parkeys = getParkeys(unitId);
		try {
			compreParamInfoService.delete(Integer.parseInt(mcPrefix),
					Integer.parseInt(mcSuffix), unitId, parkeys);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}

		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/quality/score/english/delete")
	@ControllerInfo("英语成绩折分设置删除")
	public String doDeleteInfo(String mcPrefix, String mcSuffix,
			HttpSession httpSession) {

		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();

		try {
			compreParamInfoService.delete(Integer.parseInt(mcPrefix),
					Integer.parseInt(mcSuffix), unitId,
					CompreStatisticsConstants.INFO_KEY_2);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}

		return returnSuccess();
	}

	// 获取parkeys的通用方法
	public String[] getParkeys(String unitId) {
		List<CompreParameter> compreParameterList = compreParameterService
				.findCompreParameterByTypeAndUnitId(
						CompreStatisticsConstants.TYPE_OVERALL, unitId);
		if (compreParameterList.size() == 0) {
			compreParameterList = compreParameterService
					.findCompreParameterByTypeAndUnitId(
							CompreStatisticsConstants.TYPE_OVERALL,
							BaseConstants.ZERO_GUID);
			for (CompreParameter compreParameter : compreParameterList) {
				compreParameter.setUnitId(unitId);
				compreParameter.setId(UuidUtils.generateUuid());
			}
			compreParameterService.saveAllCompreParameter(compreParameterList);
		}
		List<String> parkey = new ArrayList<String>();
		for (CompreParameter compreParameter : compreParameterList) {
			parkey.add(compreParameter.getParkey());
		}
		String[] parkeys = parkey.toArray(new String[parkey.size()]);
		return parkeys;
	}
	
	//根据unit_id获取CompreParameter列表的方法
	public List<CompreParameter> getCompreParameterListByUnitId(String unitId){
		List<CompreParameter> compreParameterList = compreParameterService
				.findCompreParameterByTypeAndUnitId(
						CompreStatisticsConstants.TYPE_OVERALL, unitId);
		if (compreParameterList.size() == 0) {
			//所有参数初始化
			compreParameterList=new ArrayList<>();
			List<CompreParameter> compreParameterListAll = compreParameterService
					.findCompreParameterByTypeAndUnitId(
							null,
							BaseConstants.ZERO_GUID);
			for (CompreParameter compreParameter : compreParameterListAll) {
				compreParameter.setUnitId(unitId);
				compreParameter.setId(UuidUtils.generateUuid());
				if(CompreStatisticsConstants.TYPE_OVERALL.equals(compreParameter.getType())) {
					compreParameterList.add(compreParameter);
				}
			}
			compreParameterService.saveAllCompreParameter(compreParameterListAll);
		}
		//CompreStatisticsConstants.TYPE_OVERALL
		return compreParameterList;
	}
}