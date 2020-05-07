package net.zdsoft.szxy.operation.record.dto;

import lombok.Data;

import java.util.Date;
@Data
public class OperateRecordDetail {
    private Date time;
    private Integer oldState;
    private Integer newState;
    private String servereCode;

    @Override
    public String toString() {
        return "OperateRecordDetail{" +
                "time=" + time +
                ", oldState=" + oldState +
                ", newState=" + newState +
                ", serverCode=" + servereCode +
                '}';
    }
}
