package net.zdsoft.szxy.operation.record.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.dao.OpUserDao;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.record.controller.dto.*;
import net.zdsoft.szxy.operation.record.controller.vo.ServiceTaskExportVo;
import net.zdsoft.szxy.operation.record.controller.vo.ServiceTaskExtension;
import net.zdsoft.szxy.operation.record.dao.ServiceTaskDao;
import net.zdsoft.szxy.operation.record.entity.ServiceTask;
import net.zdsoft.szxy.operation.record.enums.NotSupportOperateCodeException;
import net.zdsoft.szxy.operation.record.enums.TaskState;
import net.zdsoft.szxy.operation.record.enums.TaskType;
import net.zdsoft.szxy.operation.record.service.ServiceTaskService;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.zdsoft.szxy.alarm.SzxyMailSender;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @author: Fubicheng
 * @create: 2019-04-04 09:48
 **/
@Service("serviceTaskService")
public class ServiceTaskServiceImpl implements ServiceTaskService {

    private Logger logger = LoggerFactory.getLogger(ServiceTaskServiceImpl.class);

    private Lock lock;

    {
        lock = new ReentrantLock();
    }

    @Resource
    private ServiceTaskDao serviceTaskDao;

    @Resource
    private OpUserDao opUserDao;

    @Autowired
    private SzxyMailSender javaMailSender;

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Page<ServiceTaskListDto> findByPage(ServiceTaskQueryDto serviceTaskQueryDto, Pageable page) {
        final Long dateTime=new Date().getTime();
        Page<ServiceTaskListDto>pages=serviceTaskDao.findByPage(serviceTaskQueryDto.getOwnerUserName(),
                serviceTaskQueryDto.getTaskType(),
                serviceTaskQueryDto.getState(),page);
        pages.getContent().stream().forEach(serviceTaskListDto -> {
            if(serviceTaskListDto.getCompletionTime().getTime()<dateTime
                    && serviceTaskListDto.getState().equals(TaskState.Task_Processing.getTaskStateCode())){
                serviceTaskListDto.setState(TaskState.Task__Timeout.getTaskStateCode());
                updateStateById(serviceTaskListDto.getState(),serviceTaskListDto.getId());
            }
        });
        return pages;
    }

    @Override
    public Optional<ServiceTask> findById(String id) {
        return serviceTaskDao.findById(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateStateById(Integer state, String id) {
        serviceTaskDao.updateTaskStateById(state,id);
    }


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteServiceTaskById(String taskId) {
        AssertUtils.notNull(taskId,"服务任务id不可为空");
        serviceTaskDao.deleteById(taskId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void editServiceTask(ServiceTaskUpdateDto serviceTaskUpdateDto) {
        serviceTaskDao.updateByTaskTypeAndContentAndIsEmail(serviceTaskUpdateDto);
    }


    @Override
    public void sendEmail(ServiceTaskInsertDto serviceTaskInsertDto,String createUserId) {
        new Thread(() -> {
            lock.lock();
            try {
                OpUser opUser=opUserDao.findById(serviceTaskInsertDto.getOwnerUserId()).get();
                String createUserName=opUserDao.findById(createUserId).get().getRealName();
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                String time=format.format(serviceTaskInsertDto.getCompletionTime());
                String recipientEmail = opUser.getEmail();
                String subject = TaskType.valueOf(serviceTaskInsertDto.getTaskType()).getTaskTypeName() + "  :  " + serviceTaskInsertDto.getContent();
                StringBuilder sb = new StringBuilder();
                sb.append("你有新的服务任务<br>")
                        .append("  任务类型：  " + TaskType.valueOf(serviceTaskInsertDto.getTaskType()).getTaskTypeName() + "<br>")
                        .append("  任务内容：  " + serviceTaskInsertDto.getContent() + "<br>")
                        .append("  完成日期： " + time + "<br>")
                        .append("  指派人  " + createUserName);
                String content = sb.toString();
                javaMailSender.sendHtmlMessage(content, subject, recipientEmail);
            } catch (MessagingException e) {
                logger.error("邮件发送失败", e);
            } catch (NotSupportOperateCodeException e) {
                logger.error("不支持的操作类型码",e);
            } finally {
                lock.unlock();
            }
        }).start();
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void insertServiceTask(ServiceTaskInsertDto serviceTaskInsertDto, String createUserId) throws Exception {
        ServiceTask serviceTask=new ServiceTask();
        Date completionTime=serviceTaskInsertDto.getCompletionTime();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String completionDateString = format.format(completionTime);
            Date completionDate = format.parse(completionDateString);
            int dayMis = 1000 * 60 * 60 * 24;//一天的毫秒-1
            long resultMis = completionDate.getTime() + (dayMis - 1); //当天最后一秒
            completionTime = new Date(resultMis);
        }catch (ParseException e){
            logger.error("字符串日期转换错误",e);
            throw e;
        }
        Optional<OpUser> user=opUserDao.findById(serviceTaskInsertDto.getOwnerUserId());
        OpUser opUser = user.orElseThrow(()->new Exception("查询错误"));
        serviceTask.setOwnerUserName(opUser.getRealName());
        serviceTask.setId(UuidUtils.generateUuid());
        serviceTask.setCreationTime(new Date());
        serviceTask.setCreateUserId(createUserId);
        serviceTask.setCompletionTime(completionTime);
        serviceTask.setOwnerUserId(serviceTaskInsertDto.getOwnerUserId());
        serviceTask.setTaskType(serviceTaskInsertDto.getTaskType());
        serviceTask.setIsEmail(serviceTaskInsertDto.getIsEmail());
        serviceTask.setContent(serviceTaskInsertDto.getContent());
        serviceTask.setState(TaskState.Task_Processing.getTaskStateCode());
        serviceTaskDao.save(serviceTask);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void completeServiceTasks(String taskId, Integer state) {
        AssertUtils.notNull(taskId,"服务任务id不可为空");
        AssertUtils.notNull(state,"服务任务状态不可为空");
        Integer taskState=serviceTaskDao.findById(taskId).get().getState();
        if(taskState.equals(TaskState.Task_Processing.getTaskStateCode())){
            serviceTaskDao.updateTaskStateById(state,taskId);
        }
    }
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void serviceTaskExtension(Date completionTime,String id) {
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(completionTime);
        calendar.add(calendar.DATE, ServiceTaskExtension.defaultDate);
        completionTime=calendar.getTime();
        serviceTaskDao.updateCompletionTime(completionTime,id);
    }

    @Override
    public ServiceTaskExportDto exportExcel(Date creationTime)throws NotSupportOperateCodeException{
        List<ServiceTaskListDto>list=serviceTaskDao.findTaskDtoByCreationTimeAfter(creationTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        ArrayList<ServiceTaskExportVo> voList = new ArrayList<>();
        int j= 1;
        for (ServiceTaskListDto task : list) {
            ServiceTaskExportVo excel = new ServiceTaskExportVo();
            //序号
            excel.setIndex(j++);
            //任务执行人
            if (task.getOwnerUserName() != null)
                excel.setOwnerUserName(task.getOwnerUserName());
            //任务类型
            if (task.getTaskType() != null) {
                excel.setTaskType(TaskType.valueOf(task.getTaskType()).getTaskTypeName());
            }
            //任务内容
            if (task.getContent() != null)
                excel.setContent(task.getContent());
            //创建时间
            if (task.getCreationTime() != null )
                excel.setCreateTime(format.format(task.getCreationTime()));
            //完成时间
            if (task.getCompletionTime()!=null)
                excel.setEndTime(format.format(task.getCompletionTime()));
            //任务状态
            if (task.getState() != null){
                excel.setTaskState(TaskState.valueOf(task.getState()).getTaskStateName());
            }
            //任务创建人
            if (task.getRealName() !=null)
                excel.setCreateUserName(task.getRealName());
            voList.add(excel);
        }
        String fileName = "服务任务-" + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
        Sheet sheet1 = new Sheet(1, 0, ServiceTaskExportVo.class);
        sheet1.setSheetName("sheet1");
        writer.write(voList, sheet1);
        writer.finish();
        ServiceTaskExportDto serviceTaskExportDto=new ServiceTaskExportDto();
        serviceTaskExportDto.setFileName(fileName);
        serviceTaskExportDto.setOutputStream(out);
        return serviceTaskExportDto;
    }

}
