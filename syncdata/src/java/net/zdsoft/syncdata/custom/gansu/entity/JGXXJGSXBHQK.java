package net.zdsoft.syncdata.custom.gansu.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/20 10:53
 */
@Entity
@Table(name = "JG_XXJGSXBHQK")
public class JGXXJGSXBHQK extends BaseEntity<String> {

   private String  NF ;
    private String   BBMC ;
    private String   XXJGBSM ;
    private String   XXJGMC ;
    private String   BGNR;
    private String  BGQ ;
    private String   BGH ;
    private String   XXJGID ;
    private String   XXJGBXLXM ;

    public String getNF() {
        return NF;
    }

    public void setNF(String NF) {
        this.NF = NF;
    }

    public String getBBMC() {
        return BBMC;
    }

    public void setBBMC(String BBMC) {
        this.BBMC = BBMC;
    }

    public String getXXJGBSM() {
        return XXJGBSM;
    }

    public void setXXJGBSM(String XXJGBSM) {
        this.XXJGBSM = XXJGBSM;
    }

    public String getXXJGMC() {
        return XXJGMC;
    }

    public void setXXJGMC(String XXJGMC) {
        this.XXJGMC = XXJGMC;
    }

    public String getBGNR() {
        return BGNR;
    }

    public void setBGNR(String BGNR) {
        this.BGNR = BGNR;
    }

    public String getBGQ() {
        return BGQ;
    }

    public void setBGQ(String BGQ) {
        this.BGQ = BGQ;
    }

    public String getBGH() {
        return BGH;
    }

    public void setBGH(String BGH) {
        this.BGH = BGH;
    }

    public String getXXJGID() {
        return XXJGID;
    }

    public void setXXJGID(String XXJGID) {
        this.XXJGID = XXJGID;
    }

    public String getXXJGBXLXM() {
        return XXJGBXLXM;
    }

    public void setXXJGBXLXM(String XXJGBXLXM) {
        this.XXJGBXLXM = XXJGBXLXM;
    }

    @Override
    public String fetchCacheEntitName() {
        return "jGXXJGSXBHQK";
    }
}
