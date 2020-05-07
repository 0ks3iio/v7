package net.zdsoft.operation.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "OP_TRIALUSER")
public class TrialUser extends BaseEntity<String> {


    @Length(max=25,message = "姓名最多包含25个字符")
    @NotBlank(message = "姓名不能为空")
    private String realName;
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "电话信息不合法")
    @NotNull(message = "电话不能为空")
    private String telphone;
    @Length(max=50,message = "单位信息最多包含50个字符")
    @NotBlank(message = "单位信息不能为空")
    private String company;
    @NotNull(message = "邮箱不能为空")
    @Email(regexp = "^([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$", message = "不合法的邮箱")
    private String email;
    private Date creationTime;

    @Override
    public String fetchCacheEntitName() {
        return "trialUser";
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    
}
