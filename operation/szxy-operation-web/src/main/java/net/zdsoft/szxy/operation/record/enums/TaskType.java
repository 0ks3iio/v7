package net.zdsoft.szxy.operation.record.enums;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务类型
 *
 * @author fubc
 */
public enum TaskType implements TaskTypeCode<TaskType> {

    /*
     *排课
     */

    Arrange_Course(0, "排课"),

    /*
     *分班
     */
    Divide_Class(1,"分班"),

    /*
     *其他
     */

    Other_Task(2,"其他");

    private Integer code;
    private String name;

    private static Map<Integer, TaskType> map = new HashMap<>(16);

    static {

        map = Arrays.stream(TaskType.values())
                .collect(Collectors.toMap(TaskTypeCode::getTaskTypeCode, Function.identity()));
    }

    TaskType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getTaskTypeCode() {
        return this.code;
    }

    @Override
    public String getTaskTypeName() {
        return this.name;
    }

    public static TaskType valueOf(Integer code) throws NotSupportOperateCodeException {
        TaskType coder;
        if ((coder = map.get(code)) == null && code!=null) {
            throw new NotSupportOperateCodeException("不支持的操作类型码");
        }
        return coder;
    }
}
