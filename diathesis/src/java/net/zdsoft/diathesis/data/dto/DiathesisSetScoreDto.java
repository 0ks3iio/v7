package net.zdsoft.diathesis.data.dto;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: panlf
 * @Date: 2019/7/26 20:44
 */
public class DiathesisSetScoreDto  implements Serializable {
    @NotBlank(message = "学生id不能为空")
    private String studentId;
    @NotBlank(message = "params不能为空")
    private String params;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
