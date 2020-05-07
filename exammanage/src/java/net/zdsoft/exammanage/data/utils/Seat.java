package net.zdsoft.exammanage.data.utils;

public class Seat {
    private int posX;
    private int posY;
    private String groupId;
    private int index;
    private int state; //1 = 正常；-1=空；-2=不可用; -3冲突

    public Seat(int posX, int posY, int index, String groupId, int state) {
        this.posX = posX;
        this.posY = posY;
        this.index = index;
        this.state = state;
        this.groupId = groupId;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
