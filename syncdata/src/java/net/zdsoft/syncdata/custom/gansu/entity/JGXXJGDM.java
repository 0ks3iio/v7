package net.zdsoft.syncdata.custom.gansu.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/20 10:33
 */
@Entity
@Table(name = "JG_XXJGDM")
public class JGXXJGDM extends BaseEntity<String> {

    /**
     *年份
     */
   private String  NF ;
    /**
     *版本名称
     */
    private String   BBMC ;
    /**
     *学校(机构)名称
     */
    private String   XXJGMC ;
    /**
     *学校(机构)标识码
     */
    private String   XXJGBSM ;
    /**
     *学校（机构）所在地代码
     */
    private String  XXJGDZDM ;
    /**
     *学校（机构）属地管理教育行政部门代码
     */
    private String   XXJGSDGLJYXZBMDM ;
    /**
     *学校（机构）举办者码
     */
    private String   XXJGJBZM ;
    /**
     *学校（机构）办学类型码
     */
    private String   XXJGBXLXM ;
    /**
     *学校（机构）性质类别码
     */
    private String   XXJGXZLBM ;
    /**
     *学校（机构）所在地城乡分类码
     */
    private String   XXJGSZDCXFLM ;
    /**
     *独立设置少数民族学校（机构）
     */
    private String   DLSZSSMZXXJG ;
    /**
     *是否新设立学校（机构）
     */
    private String  SFXSLXX ;
    /**
     *是否撤销（在用）
     */
    private String  SFCX ;
    /**
     *撤并到学校（机构）标识码
     */
    private String  CBDXXJGBSM ;
    /**
     *系统更新日期
     */
    private String  XTGXRQ ;
    /**
     *学校(机构)唯一标识ID
     */
    private String  XXJGID ;
    /**
     *
     */
    private String  XXJGCJBMDM ;
    /**
     *学校所属主管教育行政部门代码
     */
    private String  XXSSZGJYXZDM ;

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

    public String getXXJGMC() {
        return XXJGMC;
    }

    public void setXXJGMC(String XXJGMC) {
        this.XXJGMC = XXJGMC;
    }

    public String getXXJGBSM() {
        return XXJGBSM;
    }

    public void setXXJGBSM(String XXJGBSM) {
        this.XXJGBSM = XXJGBSM;
    }

    public String getXXJGDZDM() {
        return XXJGDZDM;
    }

    public void setXXJGDZDM(String XXJGDZDM) {
        this.XXJGDZDM = XXJGDZDM;
    }

    public String getXXJGSDGLJYXZBMDM() {
        return XXJGSDGLJYXZBMDM;
    }

    public void setXXJGSDGLJYXZBMDM(String XXJGSDGLJYXZBMDM) {
        this.XXJGSDGLJYXZBMDM = XXJGSDGLJYXZBMDM;
    }

    public String getXXJGJBZM() {
        return XXJGJBZM;
    }

    public void setXXJGJBZM(String XXJGJBZM) {
        this.XXJGJBZM = XXJGJBZM;
    }

    public String getXXJGBXLXM() {
        return XXJGBXLXM;
    }

    public void setXXJGBXLXM(String XXJGBXLXM) {
        this.XXJGBXLXM = XXJGBXLXM;
    }

    public String getXXJGXZLBM() {
        return XXJGXZLBM;
    }

    public void setXXJGXZLBM(String XXJGXZLBM) {
        this.XXJGXZLBM = XXJGXZLBM;
    }

    public String getXXJGSZDCXFLM() {
        return XXJGSZDCXFLM;
    }

    public void setXXJGSZDCXFLM(String XXJGSZDCXFLM) {
        this.XXJGSZDCXFLM = XXJGSZDCXFLM;
    }

    public String getDLSZSSMZXXJG() {
        return DLSZSSMZXXJG;
    }

    public void setDLSZSSMZXXJG(String DLSZSSMZXXJG) {
        this.DLSZSSMZXXJG = DLSZSSMZXXJG;
    }

    public String getSFXSLXX() {
        return SFXSLXX;
    }

    public void setSFXSLXX(String SFXSLXX) {
        this.SFXSLXX = SFXSLXX;
    }

    public String getSFCX() {
        return SFCX;
    }

    public void setSFCX(String SFCX) {
        this.SFCX = SFCX;
    }

    public String getCBDXXJGBSM() {
        return CBDXXJGBSM;
    }

    public void setCBDXXJGBSM(String CBDXXJGBSM) {
        this.CBDXXJGBSM = CBDXXJGBSM;
    }

    public String getXTGXRQ() {
        return XTGXRQ;
    }

    public void setXTGXRQ(String XTGXRQ) {
        this.XTGXRQ = XTGXRQ;
    }

    public String getXXJGID() {
        return XXJGID;
    }

    public void setXXJGID(String XXJGID) {
        this.XXJGID = XXJGID;
    }

    public String getXXJGCJBMDM() {
        return XXJGCJBMDM;
    }

    public void setXXJGCJBMDM(String XXJGCJBMDM) {
        this.XXJGCJBMDM = XXJGCJBMDM;
    }

    public String getXXSSZGJYXZDM() {
        return XXSSZGJYXZDM;
    }

    public void setXXSSZGJYXZDM(String XXSSZGJYXZDM) {
        this.XXSSZGJYXZDM = XXSSZGJYXZDM;
    }

    @Override
    public String fetchCacheEntitName() {
        return "jGXXJGDM";
    }
}
