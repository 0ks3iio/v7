package net.zdsoft.basedata.dingding.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.dingding.dao.DingDingSyncdataLogJdbcDao;
import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.UuidUtils;

public class DingDingSyncdataLogDaoImpl extends BaseDao<DdSyncdataLog>
		implements DingDingSyncdataLogJdbcDao {

	public void insertLogs(DdSyncdataLog... logs) {
		String sql = "INSERT INTO dd_syncdata_log (id, unt_id, data_type,handler_type,object_name, result,remark,log_time) values (?,?,?,?,?,?,?,?)";
		List<Object[]> listOfArgs = new ArrayList<Object[]>();
		for (DdSyncdataLog log : logs) {
			if (log == null)
				continue;
			listOfArgs.add(new Object[] { UuidUtils.generateUuid(),
					log.getUnitId(), log.getDataType(), log.getHandlerType(),
					log.getObjectName(), log.getResult(), log.getRemark(),
					new Date() });
		}
		batchUpdate(sql, listOfArgs, new int[] { Types.CHAR, Types.CHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.VARCHAR, Types.TIMESTAMP });
	}

	@Override
	public DdSyncdataLog setField(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
