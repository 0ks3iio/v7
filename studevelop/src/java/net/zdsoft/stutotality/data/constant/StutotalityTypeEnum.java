package net.zdsoft.stutotality.data.constant;

import net.zdsoft.stutotality.data.dto.StutotalityTypeDto;

import java.util.ArrayList;
import java.util.List;

public enum StutotalityTypeEnum {
    TYPE_1("1","阳光德育"),
    TYPE_2("2","基础课程"),
    TYPE_3("3","拓展课程"),
    TYPE_4("4","获奖情况"),
    TYPE_5("5","品德行为");
    //TYPE_1("6","学分汇总");
    private String type;

    private String typeName;

    StutotalityTypeEnum(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public static List<StutotalityTypeDto> getTypeDtoList(){
        List<StutotalityTypeDto> typeDtoList=new ArrayList<>();
        StutotalityTypeDto typeDto=null;
        for(StutotalityTypeEnum typeEnum:StutotalityTypeEnum.values()){
            typeDto=new StutotalityTypeDto(typeEnum.getType(),typeEnum.getTypeName());
            typeDtoList.add(typeDto);
        }
        return typeDtoList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
