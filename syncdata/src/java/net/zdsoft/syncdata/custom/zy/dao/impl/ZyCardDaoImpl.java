package net.zdsoft.syncdata.custom.zy.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.framework.config.SpringContextHolder;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.syncdata.custom.zy.dao.ZyCardDao;
import net.zdsoft.syncdata.custom.zy.entity.ZyCard;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;

@Repository
@Lazy(true)
public class ZyCardDaoImpl extends BaseDao<ZyCard> implements ZyCardDao {

	@Override
	public ZyCard setField(ResultSet rs) throws SQLException {
		ZyCard card = new ZyCard();
		card.setCardNumber(rs.getString("CardID"));
		card.setIdentityCard(rs.getString("CertCode"));
		card.setCardOwner(rs.getString("AccName"));
		card.setType(rs.getString("ClsName"));
		return card;
	}

	@Override
	public List<ZyCard> getAllCards() {
		setDataSource((DruidDataSource) SpringContextHolder
				.getBean("dataSourceZY"));
		String sql = "select CardID,CertCode,AccName,ClsName from vw_id_cardholder_wp where AccStatusValue='有效卡' and CertCode<>'' and CertCode is not null";
		return query(sql, new MultiRow());
	}
}
