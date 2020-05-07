package net.zdsoft.szxy.operation.record.dao;

import net.zdsoft.szxy.operation.record.controller.dto.ServiceTaskListDto;
import net.zdsoft.szxy.operation.record.controller.dto.ServiceTaskUpdateDto;
import net.zdsoft.szxy.operation.record.entity.ServiceTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author: Fubicheng
 * @create: 2019-04-02 16:49
 **/
@Repository
public interface ServiceTaskDao extends JpaSpecificationExecutor<ServiceTask>,JpaRepository<ServiceTask, String> {

    /**
     *完成时间延期
     * @param completionTime 任务完成时间
     * @param id
     */
    @Modifying
    @Query("update ServiceTask s set s.completionTime= ?1 where s.id= ?2")
    void updateCompletionTime(Date completionTime,String id);

    @Modifying
    @Query("update ServiceTask  set state= ?1 where id= ?2")
    void updateStateById(Integer state,String id);

    /*
     * 删除任务
     */
    void deleteById(String id);


    List<ServiceTask>findAll();

    /**
     * 任务完成
     * @param state 服务任务状态
     * @param id
     */
    @Modifying
    @Query("update  ServiceTask s set s.state= ?1 where s.id= ?2")
    void updateTaskStateById(Integer state,String id);


    void updateByTaskTypeAndContentAndIsEmail(ServiceTaskUpdateDto serviceTaskUpdateDto);
    /**
     * 导出excel的List
     * @param creationTime
     * @return
     */
    List<ServiceTaskListDto>findTaskDtoByCreationTimeAfter(Date creationTime);
    /**
     * 分页查询服务任务
     * @param ownerUserName
     * @param taskType
     * @param state
     * @param page
     * @return
     */
    Page<ServiceTaskListDto> findByPage(String ownerUserName, Integer taskType, Integer state, Pageable page);

}
