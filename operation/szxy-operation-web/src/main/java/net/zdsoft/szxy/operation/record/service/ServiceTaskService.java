package net.zdsoft.szxy.operation.record.service;

import net.zdsoft.szxy.operation.record.controller.dto.*;

import net.zdsoft.szxy.operation.record.controller.vo.ServiceTaskExportVo;
import net.zdsoft.szxy.operation.record.entity.ServiceTask;
import net.zdsoft.szxy.operation.record.enums.NotSupportOperateCodeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ServiceTaskService {

    /**
     * 分页查找
     * @param serviceTaskQueryDto 查询条件
     * @param page 分页参数
     * @return
     */
    Page<ServiceTaskListDto> findByPage(ServiceTaskQueryDto serviceTaskQueryDto, Pageable page);

    /**
     * 通过id查询服务任务
     * @param id
     * @return
     */
    Optional<ServiceTask> findById(String id);

    void updateStateById(Integer state,String id);
    /**
     * 删除服务任务
     * @param taskId 服务任务id
     */
    void deleteServiceTaskById(String taskId);


    /**
     * 编辑任务
     * @param serviceTaskUpdateDto 编辑任务所需参数
     * taskId 服务任务主键id
     * content 服务任务内容
     * isMail 是否邮件告知
     * taskType 任务类型
     */
    void editServiceTask(ServiceTaskUpdateDto serviceTaskUpdateDto);

    /**
     * 发邮件告知
     * @param createUserId 任务创建人id
     * @param serviceTaskInsertDto
     */
    void sendEmail(ServiceTaskInsertDto serviceTaskInsertDto,String createUserId);

    /**
     * 新增服务记录
     * @param serviceTaskInsertDto 新增服务任务参数
     * @param createUserId 任务创建人id
     */
    void insertServiceTask(ServiceTaskInsertDto serviceTaskInsertDto, String createUserId) throws Exception;

    /**
     * 服务任务完成时间
     * @param taskId
     * @param state
     */
    void completeServiceTasks(String taskId,Integer state);


    /**
     * 任务延期
     * @param completionTime
     * @param id
     */
    void serviceTaskExtension(Date completionTime,String id);

    /**
     * 导出excel
     * @return
     */
    ServiceTaskExportDto exportExcel(Date creationTime) throws NotSupportOperateCodeException;
}
