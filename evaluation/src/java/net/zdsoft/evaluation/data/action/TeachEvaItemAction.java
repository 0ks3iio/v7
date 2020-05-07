package net.zdsoft.evaluation.data.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dto.OptionDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemOptionService;
import net.zdsoft.evaluation.data.service.TeachEvaluateItemService;
import net.zdsoft.evaluation.data.service.TeachEvaluateResultService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/evaluate")
public class TeachEvaItemAction extends BaseAction{

	@Autowired
	private TeachEvaluateItemService teachEvaluateItemService;
	@Autowired
	private TeachEvaluateItemOptionService teachEvaluateItemOptionService;
	@Autowired
	private TeachEvaluateResultService teachEvaluateResultService;
	@Autowired
	McodeRemoteService mcodeRemoteService;
	
	
	@RequestMapping("/option/index/page")
	public String optionIndex(ModelMap map){
		//指标类型
		List<McodeDetail> zbmcodeList=SUtils.dt(mcodeRemoteService.findByMcodeIds(EvaluationConstants.EVA_ITEM_TYPE),new TR<List<McodeDetail>>(){});
		if(CollectionUtils.isEmpty(zbmcodeList)){
			return errorFtl(map, "未维护指标类型");
		}
		map.put("itemType",zbmcodeList.get(0).getThisId());
		map.put("zbmcodeList",zbmcodeList);
		return "/evaluation/option/evaluateItemIndex.ftl";
	}
	
	@RequestMapping("/option/list/page")
	public String optionList(ModelMap map,OptionDto dto){
		//评价类型
		List<McodeDetail> pjmcodeList=SUtils.dt(mcodeRemoteService.findByMcodeIds(EvaluationConstants.EVA_EVA_TYPE),new TR<List<McodeDetail>>(){});
		if(CollectionUtils.isEmpty(pjmcodeList)){
			return errorFtl(map, "未维护评价类型");
		}
		if(StringUtils.isBlank(dto.getEvaluateType())){
			dto.setEvaluateType(pjmcodeList.get(0).getThisId());
		}
		
		List<TeachEvaluateItem> itemList=teachEvaluateItemService.findByDto(getLoginInfo().getUnitId(),dto);

		map.put("itemList", itemList);
		map.put("pjmcodeList", pjmcodeList);
		map.put("dto", dto);
		return "/evaluation/option/evaluateItemList.ftl";
	}
	
	@RequestMapping("/option/editItem")
	public String editItem(ModelMap map,OptionDto dto,String id){
		TeachEvaluateItem item=null;
		if(StringUtils.isNotBlank(id)){
			item=teachEvaluateItemService.findOne(id);
		}
		if(item==null) {
			item=new TeachEvaluateItem();
			List<TeachEvaluateItem> itemList=teachEvaluateItemService.findByCon(getLoginInfo().getUnitId(), dto.getEvaluateType(), dto.getItemType());
			if(CollectionUtils.isNotEmpty(itemList)){
				item.setItemNo(itemList.size()+1);
			}else{
				item.setItemNo(1);
			}
		}
		
		List<TeachEvaluateItemOption> optionList=teachEvaluateItemOptionService.findByItemIds(getLoginInfo().getUnitId(),new String[]{id});
		if(CollectionUtils.isNotEmpty(optionList)){
			map.put("number", optionList.size());
		}else{
			map.put("number", 1);
		}
		
		map.put("dto", dto);
		map.put("item", item);
		map.put("optionList", optionList);
		return "/evaluation/option/evaluateItemEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/option/saveItem")
	public String saveItem(ModelMap map,TeachEvaluateItem evaItem){
		try {
			List<TeachEvaluateItem> itemList=teachEvaluateItemService.findForCheck(getLoginInfo().getUnitId(), evaItem.getEvaluateType(), evaItem.getItemName());
			boolean flag=CollectionUtils.isNotEmpty(itemList);
			
			if(StringUtils.isNotBlank(evaItem.getId())){
				if(flag){
					for(TeachEvaluateItem item:itemList){
						if(!item.getId().equals(evaItem.getId())){
							return error("已存在该名称的指标项");
						}
					}
				}
				teachEvaluateItemService.update(evaItem, evaItem.getId(),new String[]{"itemName","itemNo"});
				String message=teachEvaluateItemOptionService.updateOption(getLoginInfo().getUnitId(), evaItem.getId(), evaItem.getOptionList());
				if(!message.equals("success")){
					return error(message);
				}
			}else{
				if(flag){
					return error("已存在该名称的指标项");
				}
				teachEvaluateItemService.saveItem(getLoginInfo().getUnitId(),evaItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/option/deletItem")
	public String deletItem(ModelMap map,String id){
		try {
			/*if(CollectionUtils.isNotEmpty(teachEvaluateResultService.findResultByItemId(getLoginInfo().getUnitId(), id))){
				return error("已存在评教记录，无法删除");
			}*/
			teachEvaluateItemService.delete(id);
			teachEvaluateItemOptionService.deleteByItemId(getLoginInfo().getUnitId(),id);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
}
