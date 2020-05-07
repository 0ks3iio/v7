package net.zdsoft.stutotality.data.dto;

public class StutotalityTypeDto {

    public static String STUTOTALITY_TYPE_1 ="1";//阳光德育

    public static String STUTOTALITY_TYPE_2 ="2";//基础课程

    public static String STUTOTALITY_TYPE_3 ="3";//拓展课程

    /**
     * 1：阳光德育  2：基础课程   3：拓展课程
     */
    private String type;

    private String typeName;

    private String status;

    private String msg;

    private Float score;

    private String codeName;

    private String itemName;

    private String itemId;

    public StutotalityTypeDto(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }
    public StutotalityTypeDto() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
