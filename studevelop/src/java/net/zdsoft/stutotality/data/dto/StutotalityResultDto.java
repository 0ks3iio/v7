package net.zdsoft.stutotality.data.dto;

import java.util.List;

public class StutotalityResultDto {

    private String itemName;

    private String itemId;

    private List<StutotalityOptionDto> optionDtoList1;//1：普通内容

    private List<StutotalityOptionDto> optionDtoList2;//2：日常期末综合等



    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<StutotalityOptionDto> getOptionDtoList1() {
        return optionDtoList1;
    }

    public void setOptionDtoList1(List<StutotalityOptionDto> optionDtoList1) {
        this.optionDtoList1 = optionDtoList1;
    }

    public List<StutotalityOptionDto> getOptionDtoList2() {
        return optionDtoList2;
    }

    public void setOptionDtoList2(List<StutotalityOptionDto> optionDtoList2) {
        this.optionDtoList2 = optionDtoList2;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
