package net.zdsoft.szxy.operation.record.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TaskState implements TaskStateCode<TaskState> {
    /*
     *排课
     */
    Task_Processing(0, "进行中"),

    /*
     *分班
     */
    Task_Completed(1,"已完成"),

    /*
     *其他
     */

    Task__Timeout(2,"已超时");


    private Integer code;
    private String name;

    private static Map<Integer, TaskState> cache = new HashMap<>(16);

    static {
        cache = Arrays.stream(TaskState.values())
                .collect(Collectors.toMap(TaskStateCode::getTaskStateCode, Function.identity()));
    }

    TaskState(Integer code, String name) {
        this.code = code;
        this.name = name;
    }



    @Override
    public Integer getTaskStateCode() {
        return this.code;
    }

    @Override
    public String getTaskStateName() {
        return  this.name;
    }

    public static TaskState valueOf(Integer code) throws NotSupportOperateCodeException {
        TaskState coder;
        if ((coder = cache.get(code)) == null && code!=null) {
            throw new NotSupportOperateCodeException("不支持的操作类型码");
        }
        return coder;
    }
}
