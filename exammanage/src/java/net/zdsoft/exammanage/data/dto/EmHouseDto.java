package net.zdsoft.exammanage.data.dto;

public class EmHouseDto {

    private String unitName;
    private int turnIn;//转入
    private int turnOut;//转出

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getTurnIn() {
        return turnIn;
    }

    public void setTurnIn(int turnIn) {
        this.turnIn = turnIn;
    }

    public int getTurnOut() {
        return turnOut;
    }

    public void setTurnOut(int turnOut) {
        this.turnOut = turnOut;
    }

}
