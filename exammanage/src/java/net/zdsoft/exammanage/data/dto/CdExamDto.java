package net.zdsoft.exammanage.data.dto;

import java.io.Serializable;
import java.util.Date;

public class CdExamDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String examTableName;
    private String examName;
    private String examKey;
    private String ownedby;//ukey
    private Date startTime;
    private String gradeName;

    public String getExamTableName() {
        return examTableName;
    }

    public void setExamTableName(String examTableName) {
        this.examTableName = examTableName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamKey() {
        return examKey;
    }

    public void setExamKey(String examKey) {
        this.examKey = examKey;
    }

    public String getOwnedby() {
        return ownedby;
    }

    public void setOwnedby(String ownedby) {
        this.ownedby = ownedby;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
