package net.zdsoft.diathesis.data.vo;

import net.zdsoft.diathesis.data.dto.DiathesisIdAndNameDto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/4/10 15:26
 */
public class DiathesisIdAndNameVo {
    private String parentId;
    private List<DiathesisIdAndNameDto> diathesisIdAndNameDtoList;

    public List<DiathesisIdAndNameDto> getDiathesisIdAndNameDtoList() {
        return diathesisIdAndNameDtoList;
    }

    public void setDiathesisIdAndNameDtoList(List<DiathesisIdAndNameDto> diathesisIdAndNameDtoList) {
        this.diathesisIdAndNameDtoList = diathesisIdAndNameDtoList;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
