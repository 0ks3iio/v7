package net.zdsoft.szxy.operation.record.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @description:
 * @author: Fubicheng
 * @create: 2019-04-14 14:18
 **/
public class ServiceTaskExportVo extends BaseRowModel {
    @ExcelProperty(value ="序号",index = 0)
    private Integer index;
    @ExcelProperty(value = "任务执行人",index = 1)
    private String ownerUserName;
    @ExcelProperty(value = "任务类型",index = 2)
    private String taskType;
    @ExcelProperty(value = "任务内容",index = 3)
    private String content;
    @ExcelProperty(value = "创建日期",index = 4)
    private String createTime;
    @ExcelProperty(value = "完成日期",index = 5)
    private String endTime;
    @ExcelProperty(value = "任务状态",index = 6)
    private String taskState;
    @ExcelProperty(value = "创建人",index = 7)
    private String createUserName;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
