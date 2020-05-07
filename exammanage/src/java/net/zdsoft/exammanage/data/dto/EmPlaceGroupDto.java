package net.zdsoft.exammanage.data.dto;

public class EmPlaceGroupDto {
    private String groupId;
    private String groupName;
    private int arrangeStuNum;
    private int arrangePlaceNum;
    private int arrangeNum;
    private int noArrangeStuNum;

    public int getNoArrangeStuNum() {
        return noArrangeStuNum;
    }

    public void setNoArrangeStuNum(int noArrangeStuNum) {
        this.noArrangeStuNum = noArrangeStuNum;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getArrangeStuNum() {
        return arrangeStuNum;
    }

    public void setArrangeStuNum(int arrangeStuNum) {
        this.arrangeStuNum = arrangeStuNum;
    }

    public int getArrangePlaceNum() {
        return arrangePlaceNum;
    }

    public void setArrangePlaceNum(int arrangePlaceNum) {
        this.arrangePlaceNum = arrangePlaceNum;
    }

    public int getArrangeNum() {
        return arrangeNum;
    }

    public void setArrangeNum(int arrangeNum) {
        this.arrangeNum = arrangeNum;
    }


}
