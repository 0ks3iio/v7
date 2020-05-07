package net.zdsoft.basedata.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.zdsoft.basedata.constant.JobConstants;
import net.zdsoft.basedata.entity.TaskRecord;
import net.zdsoft.basedata.job.TaskJobReply;
import net.zdsoft.basedata.service.TaskRecordService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;

@Controller
@RequestMapping("/basedata")
public class TaskJobAction extends BaseAction {
	
	@Autowired
	private TaskRecordService taskRecordService;
	
	@RequestMapping("/taskJob/index/page")
	@ControllerInfo(value = "进入任务查看页面index")
	public String showIndexPage(String businessType,ModelMap map,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession){
		map.put("businessType", businessType);
		return "/basedata/taskJob/taskJobIndex.ftl"; 
	}
	
	@RequestMapping("/taskJob/list/page")
	@ControllerInfo(value = "进入任务查看页面list")
	public String showListPage(String businessType,ModelMap map,HttpServletRequest request, HttpServletResponse response, HttpSession httpSession){
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		Pagination page = createPagination(request);
		List<TaskRecord> findListByUnitId = taskRecordService.findListByUnitId(JobConstants.SERVER_TYPE_7,JobConstants.TYPE_1, unitId, businessType,page);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for (TaskRecord taskRecord : findListByUnitId) {
			if(taskRecord.getCreationTime()!=null){
				taskRecord.setCreationTimeStr(sdf.format(taskRecord.getCreationTime()));
			}
			if(taskRecord.getJobStartTime()!=null){
				taskRecord.setJobStartTimeStr(sdf.format(taskRecord.getJobStartTime()));
			}
			if(taskRecord.getJobEndTime()!=null){
				taskRecord.setJobEndTimeStr(sdf.format(taskRecord.getJobEndTime()));
			}
		}
		map.put("taskRecordList", findListByUnitId);
		map.put("businessType", businessType);
		sendPagination(request, map, page);
		return "/basedata/taskJob/taskJobList.ftl"; 
	}
	
	@ResponseBody
    @RequestMapping("/taskJob/fnRecycle/page")
    @ControllerInfo("查找任务cache")
    public String fnRecycle(final String jobId, HttpSession httpSession) {
    	TaskJobReply taskJobReply = RedisUtils.getObject(JobConstants.CACHE_KEY_TASK_JOB_+jobId, new TypeReference<TaskJobReply>() {
        });
    	if(taskJobReply == null){
    		taskJobReply = new TaskJobReply();
    		return Json.toJSONString(taskJobReply,SerializerFeature.DisableCircularReferenceDetect);
    	}
    	if(taskJobReply.isHasTask()){
    		TaskRecord taskRecord = taskRecordService.findTaskJob(jobId);
    		// 此种情况的原因：任务还未入库 或任务被删除，则页面会一直取，故取两次
            for (int i = 0; i < 2; i++) {
                if (taskRecord==null) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    taskRecord = taskRecordService.findTaskJob(jobId);
                } else {
                    break;
                }
            }
    		if(taskRecord!=null){
    			List<String> actionMessages = new ArrayList<String>();
    			Integer status = taskRecord.getStatus();
    			switch (status) {
	    			case JobConstants.TASK_STATUS_NO_HAND:
	    				actionMessages.add("系统正在处理其他任务，本次排在第 " + String.valueOf(taskRecord.getJobPos()) + " 位");
	    				actionMessages.add("您可以等待；或者 离开该功能，去处理其它事务，稍后从任务列表中查看任务情况");
	    				break;
	    			case JobConstants.TASK_STATUS_PRE_HAND:
	    				actionMessages.add("任务正在预处理······");
	    				break;
	    			case JobConstants.TASK_STATUS_IN_HAND:
	    				actionMessages.add("任务正在处理······");
	    				break;
	    			default:
	    				actionMessages.add("任务处理完成······");
	                    String msg = taskRecord.getResultMsg();
	                    if (StringUtils.isNotBlank(msg))
	                    	actionMessages.add(msg);
	                    taskJobReply.setActionErrors(null);
	                    taskJobReply.setValue(JobConstants.STATUS_END);
	    				break;
    			}
    			taskJobReply.setActionMessages(actionMessages);
    		}
    	}
    	return Json.toJSONString(taskJobReply,SerializerFeature.DisableCircularReferenceDetect);
    }
    
    @ResponseBody
    @RequestMapping("/taskJob/stopCycle/page")
    @ControllerInfo("移除任务chche")
    public String stopCycle(String jobId, HttpSession httpSession) {
    	RedisUtils.del(JobConstants.CACHE_KEY_TASK_JOB_+jobId);
    	return "";
    }
    
    @ResponseBody
	@RequestMapping("/taskJob/delete")
	@ControllerInfo("删除任务")
	public String doDeleteExamInfo(String id,String status, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try{
			TaskRecord findOne = taskRecordService.findOne(id);
			if(status.equals(String.valueOf(findOne.getStatus()))){
				taskRecordService.deleteByIds(id);
			}else{
				return error("操作失败！任务状态已变更，请刷新页面后重新操作");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
}
