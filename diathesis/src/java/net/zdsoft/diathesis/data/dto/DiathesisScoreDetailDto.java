package net.zdsoft.diathesis.data.dto;

/**
 * @Author: panlf
 * @Date: 2019/6/21 16:28
 */
public class DiathesisScoreDetailDto {
    private String score;
    private String credit;
    private String GPA;
    private String principal;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String GPA) {
        this.GPA = GPA;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
