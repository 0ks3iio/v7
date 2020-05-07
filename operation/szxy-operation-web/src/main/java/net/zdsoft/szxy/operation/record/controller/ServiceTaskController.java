package net.zdsoft.szxy.operation.record.controller;


import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import net.zdsoft.szxy.operation.record.TaskOperateCode;
import net.zdsoft.szxy.operation.record.controller.dto.*;
import net.zdsoft.szxy.operation.record.controller.vo.ServiceTaskEmailVo;
import net.zdsoft.szxy.operation.record.entity.ServiceTask;
import net.zdsoft.szxy.operation.record.enums.NotSupportOperateCodeException;
import net.zdsoft.szxy.operation.record.enums.TaskState;
import net.zdsoft.szxy.operation.record.enums.TaskType;
import net.zdsoft.szxy.operation.record.service.ServiceTaskService;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;



/**
 * @description: 服务任务
 * @author: Fubicheng
 * @create: 2019-04-03 10:21
 **/
@Controller
@RequestMapping(value = "/operation/opUserLog")
public class ServiceTaskController {

    private Logger logger = LoggerFactory.getLogger(ServiceTaskController.class);
    @Resource
    private ServiceTaskService serviceTaskService;
    @Resource
    private OpUserService opUserService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index() {
        return "/record/userlog-index.ftl";
    }
    /**
     * 服务任务基础页面
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String page(Model model,@CurrentUser("id") String operatorId) throws Exception {
        List<OpUser> opUsersList=opUserService.getAllUsers();
        model.addAttribute("userList",opUsersList);
        model.addAttribute("userId",operatorId);
        model.addAttribute("taskType", TaskType.values());
        model.addAttribute("taskState", TaskState.values());
        return "/record/userlog-page.ftl";
    }


    /**
     * 服务任务列表
     * @param serviceTaskQueryDto
     * @param page
     * @param model
     */
    @Record(type = RecordType.URL)
    @RequestMapping(value = "/taskList",method =RequestMethod.GET )
    public String taskList(ServiceTaskQueryDto serviceTaskQueryDto, Pageable page, Model model,
                           @CurrentUser("id") String operatorId){
        Page<ServiceTaskListDto> pages=serviceTaskService.findByPage(serviceTaskQueryDto,page);
        List<OpUser> opUsersList=opUserService.getAllUsers();
        model.addAttribute("userList",opUsersList);
        model.addAttribute("taskType", TaskType.values());
        model.addAttribute("userId",operatorId);
        model.addAttribute("pages",pages);
        return "/record/userlog-taskList.ftl";
    }

    /**
     * 删除任务
     * @param taskId
     */
    @Secured(TaskOperateCode.TASK_DELETE)
    @RequestMapping(value = "/deleteById/{taskId}",method = RequestMethod.POST)
    public String deleteTask(@PathVariable("taskId")String taskId) throws Exception {
        if(Strings.isNotBlank(taskId)) {
            serviceTaskService.deleteServiceTaskById(taskId);
        }else{
            throw new Exception("删除任务时，id为空");
        }
        return "/record/userlog-taskList.ftl";
    }


    /**
     * 新增服务任务
     * @param serviceTaskInsertDto
     * @param operatorId
     * @return
     */
    @Secured(TaskOperateCode.TASK_ADD)
    @RequestMapping(value = "/saveTask",method = RequestMethod.POST)
    public String saveServiceTask(@Valid @RequestBody ServiceTaskInsertDto serviceTaskInsertDto,
                                  @CurrentUser("id") String operatorId,
                                  BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            throw new Exception("新增服务任务时，传入非法要求的参数");
        }
        serviceTaskService.insertServiceTask(serviceTaskInsertDto,operatorId);
        //判断是否需要发邮件
        if(serviceTaskInsertDto.getIsEmail().equals(ServiceTaskEmailVo.isEmail)
                &&!operatorId.equals(serviceTaskInsertDto.getOwnerUserId())){
            serviceTaskService.sendEmail(serviceTaskInsertDto,operatorId);
        }
        return "/record/userlog-taskList.ftl";
    }

    /**
     * 编辑任务
     * @param serviceTaskUpdateDto
     * @param bindingResult
     * @return
     */
    @Secured(TaskOperateCode.TASK_EDIT)
    @RequestMapping(value = "/editTask",method = RequestMethod.POST)
    @ResponseBody
    public Response editServiceTask(@Valid @RequestBody ServiceTaskUpdateDto serviceTaskUpdateDto,
                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return Response.error().message(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        ServiceTask serviceTask=serviceTaskService.findById(serviceTaskUpdateDto.getId()).get();
        //判断更新任务
        if(!serviceTask.getContent().equals(serviceTaskUpdateDto.getTaskContent())
                ||!serviceTask.getTaskType().equals(serviceTaskUpdateDto.getTaskType())) {
            serviceTaskUpdateDto.setIsEmail(ServiceTaskEmailVo.isEmail);
            serviceTaskService.editServiceTask(serviceTaskUpdateDto);
            ServiceTaskInsertDto serviceTaskInsertDto = new ServiceTaskInsertDto();
            serviceTaskInsertDto.setContent(serviceTaskUpdateDto.getTaskContent());
            serviceTaskInsertDto.setCompletionTime(serviceTask.getCompletionTime());
            serviceTaskInsertDto.setIsEmail(serviceTask.getIsEmail());
            serviceTaskInsertDto.setTaskType(serviceTaskUpdateDto.getTaskType());
            serviceTaskInsertDto.setOwnerUserId(serviceTask.getOwnerUserId());
            serviceTaskService.sendEmail( serviceTaskInsertDto, serviceTask.getCreateUserId());
        }
            return Response.ok().message("编辑任务成功").build();
    }

    /**
     * 完成任务
     * @param id
     * @return
     */
    @Secured(TaskOperateCode.TASK_COMPLETE)
    @RequestMapping(value = "/completeTask",method = RequestMethod.POST)
    @ResponseBody
    public  Response completeTask(@RequestParam(value = "taskId") String id) throws Exception {
        if (Strings.isNotBlank(id)){
            serviceTaskService.completeServiceTasks(id, TaskState.Task_Completed.getTaskStateCode());
        }else {
            return Response.error().message("服务任务id不可为空").build();
        }
        return Response.ok().message("完成任务").build();
    }

    /**
     * 查询单条任务记录
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOneServiceTask",method = RequestMethod.GET)
    public Response getOneServiceTask (@RequestParam (value = "id")String id) {
        if (Strings.isNotBlank(id)) {
            Optional<ServiceTask> opServiceTask = serviceTaskService.findById(id);
            if (opServiceTask.isPresent()) {
                return Response.ok().data("serviceTask", opServiceTask.get()).build();
            } else {
                return Response.error().message("没找到该条记录").build();
            }
        } else {
            return Response.error().message("查询id不可为空").build();
        }
    }
    /**
     * 任务延期，默认为3天
     * @param id
     * @return
     */
    @Secured(TaskOperateCode.TASK_DELAY)
    @RequestMapping(value = "/serviceTaskExtension/{id}",method = RequestMethod.POST)
    public String serviceTaskExtension(@PathVariable(value = "id")String id) throws Exception {
        if(Strings.isNotBlank(id)) {
            ServiceTask serviceTask = serviceTaskService.findById(id).orElseThrow(()-> new Exception("查询错误"));
            serviceTaskService.serviceTaskExtension(serviceTask.getCompletionTime(), id);
        }else{
            throw new Exception("查询id不可为空");
        }
        return "/record/userlog-taskList.ftl";

    }




    /**
     * 导出excel
     * @param creationTime
     * @param response
     * @throws NotSupportOperateCodeException
     * @throws IOException
     */
    @Secured(TaskOperateCode.TASK_EXPORT)
    @Record(type = RecordType.URL)
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public void exportExcel(@RequestParam(value = "creationTime",required = false)Date creationTime, HttpServletResponse response) throws NotSupportOperateCodeException, IOException {
        try{
            ServiceTaskExportDto serviceTaskExportDto=serviceTaskService.exportExcel(creationTime);
            String fileName=serviceTaskExportDto.getFileName();
            ByteArrayOutputStream out=serviceTaskExportDto.getOutputStream();
            response.setHeader("Cache-Control", "");
            response.setContentType("application/data;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream ro = response.getOutputStream();
            ro.write(out.toByteArray());
            ro.flush();
        } catch (IOException e) {
            logger.error("导出单位失败", e);
            throw e;
        }
    }
}
