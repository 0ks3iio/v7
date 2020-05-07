package net.zdsoft.syncdata.custom.gansu.dao.impl;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.syncdata.custom.gansu.dao.JGXXJGDMDao;
import net.zdsoft.syncdata.custom.gansu.entity.JGXXJGDM;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/21 11:09
 */
@Repository
public  class JGXXJGDMDaoImpl extends BaseDao<JGXXJGDM> implements JGXXJGDMDao {

    @Override
    public JGXXJGDM setField(ResultSet rs) throws SQLException {
        JGXXJGDM jgxxjgdm = new JGXXJGDM();
        jgxxjgdm.setNF(rs.getString("NF"));
        jgxxjgdm.setBBMC(rs.getString("BBMC"));
        jgxxjgdm.setXXJGMC(rs.getString("XXJGMC"));
        jgxxjgdm.setXXJGBSM(rs.getString("XXJGBSM"));
        jgxxjgdm.setXXJGDZDM(rs.getString("XXJGDZDM"));
        jgxxjgdm.setXXJGSDGLJYXZBMDM(rs.getString("XXJGSDGLJYXZBMDM"));
        jgxxjgdm.setXXJGJBZM(rs.getString("XXJGJBZM"));
        jgxxjgdm.setXXJGBXLXM(rs.getString("XXJGBXLXM"));
        jgxxjgdm.setXXJGXZLBM(rs.getString("XXJGXZLBM"));
        jgxxjgdm.setXXJGSZDCXFLM(rs.getString("XXJGSZDCXFLM"));
        jgxxjgdm.setSFXSLXX(rs.getString("SFXSLXX"));
        jgxxjgdm.setSFCX(rs.getString("SFCX"));
        jgxxjgdm.setXTGXRQ(rs.getString("XTGXRQ"));
        jgxxjgdm.setXXJGID(rs.getString("XXJGID"));
        jgxxjgdm.setXXSSZGJYXZDM(rs.getString("XXSSZGJYXZDM"));
        jgxxjgdm.setCBDXXJGBSM(rs.getString("CBDXXJGBSM"));
        jgxxjgdm.setDLSZSSMZXXJG(rs.getString("DLSZSSMZXXJG"));
        return jgxxjgdm;
    }


    @Override
    public List<JGXXJGDM> getAllList() {
        String sql = "select * from JG_XXJGDM";
        return query(sql,new MultiRow());
    }
}
