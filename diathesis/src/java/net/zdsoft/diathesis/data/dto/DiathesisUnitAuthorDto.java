package net.zdsoft.diathesis.data.dto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/24 15:38
 */
public class DiathesisUnitAuthorDto {
    private String unitId;
    private List<Integer> authorList;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<Integer> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Integer> authorList) {
        this.authorList = authorList;
    }
}
