package net.zdsoft.diathesis.data.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/10/29 16:48
 */
public class DiathesisMsgDto {

    @NotBlank(message = "留言不能为空!")
    @Length(max = 1000,message = "留言不能超过1000字")
    private String msg;
    @NotBlank(message = "recordId不能为空")
    private String recordId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}