package net.zdsoft.diathesis.data.dto;

import net.zdsoft.diathesis.data.entity.DiathesisOption;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/4/3 9:48
 */
public class DiathesisStructureSettingDto {
    private String id;
    private Integer colNo;
    @NotBlank(message = "字段名不能为空")
    @Length(max = 20,message = "字段名字不能超过20字")
    private String title;
    private Integer isShow;
    private Integer isMust;
    private String dataType;
    //是否累计
    private Integer isCount;
    @Valid
    private List<DiathesisOption> option;


    public Integer getIsCount() {
        return isCount;
    }

    public void setIsCount(Integer isCount) {
        this.isCount = isCount;
    }

    public Integer getIsMust() {
        return isMust;
    }

    public void setIsMust(Integer isMust) {
        this.isMust = isMust;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getColNo() {
        return colNo;
    }

    public void setColNo(Integer colNo) {
        this.colNo = colNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<DiathesisOption> getOption() {
        return option;
    }

    public void setOption(List<DiathesisOption> option) {
        this.option = option;
    }
}
