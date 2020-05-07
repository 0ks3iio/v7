package net.zdsoft.stutotality.data.dto;

import net.zdsoft.stutotality.data.entity.StutotalityItem;
import net.zdsoft.stutotality.data.entity.StutotalityItemOption;
import net.zdsoft.stutotality.data.entity.StutotalityScale;
import net.zdsoft.stutotality.data.entity.StutotalityType;

import java.util.List;

public class StutotalityDto {

    private List<StutotalityItemOption> stutotalityItemOptionList;

    private List<StutotalityType> stutotalityTypeList;

    private List<StutotalityItem> stutotalityItemList;

    private List<StutotalityScale> stutotalityScaleList;

    private String subjectType;

    private String itemName;

    private String subjectId;

    private String typeId;

    private Integer orderNumber;

    private List<StutotalityItemOption> optionList;


    public List<StutotalityItemOption> getStutotalityItemOptionList() {
        return stutotalityItemOptionList;
    }
    public void setStutotalityItemOptionList(List<StutotalityItemOption> stutotalityItemOptionList) {
        this.stutotalityItemOptionList = stutotalityItemOptionList;
    }
    public List<StutotalityType> getStutotalityTypeList() {
        return stutotalityTypeList;
    }
    public void setStutotalityTypeList(List<StutotalityType> stutotalityTypeList) {
        this.stutotalityTypeList = stutotalityTypeList;
    }



    public List<StutotalityItem> getStutotalityItemList() {
        return stutotalityItemList;
    }

    public void setStutotalityItemList(List<StutotalityItem> stutotalityItemList) {
        this.stutotalityItemList = stutotalityItemList;
    }

    public List<StutotalityScale> getStutotalityScaleList() {
        return stutotalityScaleList;
    }

    public void setStutotalityScaleList(List<StutotalityScale> stutotalityScaleList) {
        this.stutotalityScaleList = stutotalityScaleList;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public List<StutotalityItemOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<StutotalityItemOption> optionList) {
        this.optionList = optionList;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
