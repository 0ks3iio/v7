package net.zdsoft.szxy.operation.record.enums;

public interface TaskTypeCode<E extends Enum<E>> {

    /*
     *任务类型码
     */
    Integer getTaskTypeCode();

    /*
     *任务名称
     */
    String getTaskTypeName();
}
