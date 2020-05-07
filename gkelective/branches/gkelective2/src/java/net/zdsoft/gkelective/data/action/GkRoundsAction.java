package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.action.optaplanner.solver.ArrangeSingleSolver;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 轮次业务部分
 */
@Controller
@RequestMapping("/gkelective/{arrangeId}")
public class GkRoundsAction extends BaseAction{
	@Autowired
	private GkRoundsService gKRoundsService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkTeachPlacePlanService gkTeachPlacePlanService;
	
	private boolean isNowArrange(String roundsId){
		if(ArrangeSingleSolver.isSolverIdRunning(roundsId+"A") || ArrangeSingleSolver.isSolverIdRunning(roundsId+"B")){
			return true;
		}else{
			return false;
		}
	}
	@RequestMapping("/arrangeRounds/index/page")
    @ControllerInfo(value = "开班安排--轮次列表")
	public String gKRoundsList(@PathVariable String arrangeId,ModelMap map){
		GkSubjectArrange gkSubArr = gkSubjectArrangeService.findArrangeById(arrangeId);
		if(gkSubArr==null){
			return errorFtl(map,"对应选课项目已经不存在");
		}
		List<GkRounds> roundsList=gKRoundsService.findBySubjectArrangeId(arrangeId,null);
		for (GkRounds gkRounds : roundsList) {
			gkRounds.setOpenIng(isNowArrange(gkRounds.getId()));
		}
		List<GkTeachPlacePlan> list=gkTeachPlacePlanService.findBySubjectArrangeId(arrangeId,false);
		Map<String,List<GkTeachPlacePlan>> roundIdsMap=new HashMap<String, List<GkTeachPlacePlan>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(GkTeachPlacePlan p:list){
				if(!roundIdsMap.containsKey(p.getRoundsId())){
					roundIdsMap.put(p.getRoundsId(), new ArrayList<GkTeachPlacePlan>());
				}
				roundIdsMap.get(p.getRoundsId()).add(p);
			}
		}
		
		if(CollectionUtils.isNotEmpty(roundsList)){
			for(GkRounds r:roundsList){
				if(roundIdsMap.containsKey(r.getId())){
					r.setCanDelete(false);
				}else{
					r.setCanDelete(true);
				}
			}
		}
		
		map.put("roundsList", roundsList);
		map.put("arrangeId", arrangeId);
		map.put("gkSubArr", gkSubArr);
		return "/gkelective2/arrangeRounds/roundsList.ftl";
	}
	@RequestMapping("/arrangeRounds/edit/page")
    @ControllerInfo(value = "新增轮次")
	public String gKRoundsEdit(@PathVariable String arrangeId,ModelMap map){
		List<GkRounds> roundsList=gKRoundsService.findBySubjectArrangeId(arrangeId,GkRounds.OPENT_CLASS_TYPE_1);
		map.put("roundsList", roundsList);
		map.put("arrangeId", arrangeId);
		return "/gkelective2/arrangeRounds/roundsEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/arrangeRounds/save")
    @ControllerInfo(value = "保存")
	public String saveRounds(@PathVariable String arrangeId,GkRounds gkRounds,ModelMap map){
		try {
			GkSubjectArrange arrange = gkSubjectArrangeService.findArrangeById(arrangeId);
			if(arrange==null){
				return error("对应选课项目已经不存在，保存失败！");
			}
			if(gkRounds==null){
				return error("保存失败！");
			}
			
			gkRounds.setSubjectArrangeId(arrangeId);
			List<GkRounds> roundsList=gKRoundsService.findBySubjectArrangeId(arrangeId,null);
			if(CollectionUtils.isNotEmpty(roundsList)){
				//取最大值
				int max=1;
				for(GkRounds g:roundsList){
					if(g.getOrderId()>max){
						max=g.getOrderId();
					}
				}
				gkRounds.setOrderId(max+1);
			}else{
				gkRounds.setOrderId(1);
			}
			gkRounds.setId(UuidUtils.generateUuid());
			gkRounds.setCreationTime(new Date());
			gkRounds.setModifyTime(new Date());
			if(GkRounds.OPENT_CLASS_TYPE_0.equals(gkRounds.getOpenClassType())){
				//不重组
				gkRounds.setOpenTwo(GkElectveConstants.FALSE_STR);
			}
			if(StringUtils.isBlank(gkRounds.getOpenTwo())){
				gkRounds.setOpenTwo(GkElectveConstants.FALSE_STR);
			}
			//新增轮次时 是0 当一旦有保存科目设置 是1
			gkRounds.setStep(GkElectveConstants.STEP_0);
			gKRoundsService.saveRounds(gkRounds);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}
		return success("新增成功！");
	}
	@ResponseBody
	@RequestMapping("/arrangeRounds/doDelete")
    @ControllerInfo(value = "删除轮次")
    public String doDeleteRounds(@PathVariable String arrangeId,String roundsId) {
		try {
            GkRounds gkRounds = gKRoundsService.findOne(roundsId);
            if (gkRounds == null) {
                return error("你要删除的信息不存在,请刷新后重试");
            }else {
            	List<GkTeachPlacePlan> list=gkTeachPlacePlanService.findByRoundId(roundsId);
            	if(CollectionUtils.isNotEmpty(list)){
            		return error("该轮次已被选中,不能删除");
            	}
            	if(isNowArrange(roundsId)){
            		return error("正在排班中,不能删除");
    		    }
            	gKRoundsService.removeByRoundsId(roundsId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError("删除失败！", e.getMessage());
        }
        return success("删除成功！");
	}
}
