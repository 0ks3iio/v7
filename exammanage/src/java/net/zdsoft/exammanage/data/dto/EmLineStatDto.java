package net.zdsoft.exammanage.data.dto;

public class EmLineStatDto {
    private String classId;
    private String examId;
    private String line;
    private int scoreNum;//上线人数
    private float blance;
    private int rank;//排名
    private int num;//上线人数差
    private int numRank;//人数差排名

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(int scoreNum) {
        this.scoreNum = scoreNum;
    }

    public float getBlance() {
        return blance;
    }

    public void setBlance(float blance) {
        this.blance = blance;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNumRank() {
        return numRank;
    }

    public void setNumRank(int numRank) {
        this.numRank = numRank;
    }


}
