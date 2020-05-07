package net.zdsoft.eclasscard.data.action.show;

import java.util.List;

import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.service.EccAttenceGateGradeService;
import net.zdsoft.eclasscard.data.service.EccInOutAttanceService;
import net.zdsoft.eclasscard.data.service.EccTaskService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccSyncAttenceTimeAction extends BaseAction {
	@Autowired
	private EccAttenceGateGradeService eccAttenceGateGradeService;
	@Autowired
	private EccInOutAttanceService eccInOutAttanceService;
	@Autowired
	private EccTaskService eccTaskService;
	
	
	@ResponseBody
	@RequestMapping("/sync/inout")
    public String syncInoutTime(String unitId){
		if(StringUtils.isBlank(unitId)&&getLoginInfo()!=null){
			unitId = getLoginInfo().getUnitId();
		}
		if(StringUtils.isBlank(unitId)||unitId.length()!=32){
			return error("unitId error");
		}
		String lockkey = EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+unitId;
    	try{
    		if(RedisUtils.get(lockkey) == null){
    			RedisUtils.set(lockkey, "1",10*60);//至少间隔10分钟才可以下次同步
				List<EccAttenceGateGrade> inoutPeriods = eccAttenceGateGradeService.findByInOutAndClassify(unitId,EccConstants.ECC_CLASSIFY_2);
				for(EccAttenceGateGrade inout:inoutPeriods){
					eccTaskService.deleteByKey(inout.getPeriodId()+"start");
					eccTaskService.deleteByKey(inout.getPeriodId()+"end");
				}
				try{//等待异步删除延时队列中历史数据
					Thread.sleep(10*1000);
				}catch(Exception e){
					e.printStackTrace();
				}
				eccInOutAttanceService.addInOutAttenceQueue(unitId);
			}else{
				return error("频繁操作，请10分钟后再试");
			}
		}catch (Exception e) {
			return error("同步失败");
		}
		return success("同步成功");
	}
}
