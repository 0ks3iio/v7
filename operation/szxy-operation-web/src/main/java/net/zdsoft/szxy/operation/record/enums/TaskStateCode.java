package net.zdsoft.szxy.operation.record.enums;

public interface TaskStateCode<E extends Enum<E>>{

    /*
     *任务状态码
     */
    Integer getTaskStateCode();

    /*
     *任务状态名称
     */
    String getTaskStateName();
}
