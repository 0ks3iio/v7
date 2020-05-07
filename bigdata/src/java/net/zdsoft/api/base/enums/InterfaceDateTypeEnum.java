package net.zdsoft.api.base.enums;


public enum InterfaceDateTypeEnum {

	FINDBASE(1,"获取基础数据"),
	FINDBUSINESS(2,"获取业务数据"),
	SAVEDATE(3,"保存数据"),
	UPDATEDATE(4,"更新数据");
	
	private InterfaceDateTypeEnum (int dateType, String description){
		this.dateType = dateType;
		this.description = description;
	}
	private int dateType;
	private String description;
	public int getDateType() {
		return dateType;
	}
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static InterfaceDateTypeEnum get(int dateType) {
        for (InterfaceDateTypeEnum type : InterfaceDateTypeEnum.values()) {
            if (type.getDateType() == dateType) {
                return type;
            }
        }
        return null;
    }
}
