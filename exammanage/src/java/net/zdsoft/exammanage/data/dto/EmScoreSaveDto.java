package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;

import java.util.ArrayList;
import java.util.List;

public class EmScoreSaveDto {
    private EmSubjectInfo emSubjectInfo;
    private List<EmScoreInfo> scoreInfoList = new ArrayList<EmScoreInfo>();

    public List<EmScoreInfo> getScoreInfoList() {
        return scoreInfoList;
    }

    public void setScoreInfoList(List<EmScoreInfo> scoreInfoList) {
        this.scoreInfoList = scoreInfoList;
    }

    public EmSubjectInfo getEmSubjectInfo() {
        return emSubjectInfo;
    }

    public void setEmSubjectInfo(EmSubjectInfo emSubjectInfo) {
        this.emSubjectInfo = emSubjectInfo;
    }

}
