package net.zdsoft.syncdata.custom.gansu.dao.impl;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.syncdata.custom.gansu.dao.JGXXJGSXBHQKDao;
import net.zdsoft.syncdata.custom.gansu.entity.JGXXJGSXBHQK;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/21 11:23
 */
@Repository
public class JGXXJGSXBHQKDaoImpl extends BaseDao<JGXXJGSXBHQK> implements JGXXJGSXBHQKDao {

    @Override
    public JGXXJGSXBHQK setField(ResultSet rs) throws SQLException {
        JGXXJGSXBHQK jgxxjgsxbhqk = new JGXXJGSXBHQK();
        jgxxjgsxbhqk.setNF(rs.getString("NF"));
        jgxxjgsxbhqk.setBBMC(rs.getString("BBMC"));
        jgxxjgsxbhqk.setXXJGBSM(rs.getString("XXJGBSM"));
        jgxxjgsxbhqk.setXXJGMC(rs.getString("XXJGMC"));
        jgxxjgsxbhqk.setBGNR(rs.getString("BGNR"));
        jgxxjgsxbhqk.setBGQ(rs.getString("BGQ"));
        jgxxjgsxbhqk.setBGH(rs.getString("BGH"));
        jgxxjgsxbhqk.setXXJGID(rs.getString("XXJGID" ));
        jgxxjgsxbhqk.setXXJGBXLXM(rs.getString("XXJGBXLXM"));
        return jgxxjgsxbhqk;
    }

    @Override
    public List<JGXXJGSXBHQK> getList() {
        String sql = "select * from JG_XXJGSXBHQK";

        return query(sql,new MultiRow());
    }
}
