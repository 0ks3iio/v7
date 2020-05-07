package net.zdsoft.studevelop.data.dto;

import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;

import java.util.List;

public class StudevelopPerformItemCodeDto {

    private List<StuDevelopPerformItemCode> codeList;

    public List<StuDevelopPerformItemCode> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<StuDevelopPerformItemCode> codeList) {
        this.codeList = codeList;
    }
}
