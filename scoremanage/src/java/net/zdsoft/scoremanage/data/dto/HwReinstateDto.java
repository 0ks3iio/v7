package net.zdsoft.scoremanage.data.dto;

/**
 * @author niuchao
 * @date 2019/11/14 14:54
 */
public class HwReinstateDto {

    private String reinstateId;
    private String oldExamId;
    private String oldExamName;
    private String examId;
    private String examName;

    public String getReinstateId() {
        return reinstateId;
    }

    public void setReinstateId(String reinstateId) {
        this.reinstateId = reinstateId;
    }

    public String getOldExamId() {
        return oldExamId;
    }

    public void setOldExamId(String oldExamId) {
        this.oldExamId = oldExamId;
    }

    public String getOldExamName() {
        return oldExamName;
    }

    public void setOldExamName(String oldExamName) {
        this.oldExamName = oldExamName;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }
}
