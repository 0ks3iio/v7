package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.dao.NewGkPlaceItemJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;

@Repository
public class NewGkPlaceItemJdbcDaoImpl extends BaseDao<NewGkPlaceItem> implements NewGkPlaceItemJdbcDao {

	@Override
	public NewGkPlaceItem setField(ResultSet rs) throws SQLException {
		return null;
	}

}
