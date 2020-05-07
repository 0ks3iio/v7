package net.zdsoft.stuwork.data.dao.impl;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.dao.DyDormRoomJdbcDao;
import net.zdsoft.stuwork.data.entity.DyDormRoom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @ProjectName: v7
 * @Package: net.zdsoft.stuwork.data.dao.impl
 * @ClassName: DyDormRoomJdbcDaoImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2018/11/21 11:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/21 11:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DyDormRoomDaoImpl extends BaseDao<DyDormRoom> implements DyDormRoomJdbcDao{

    @Override
    public List<DyDormRoom> findByIdsAndPage(String[] ids, Pagination pagination) {
        String sql=new String("select * from dy_dorm_room where id in ");
        if(pagination!=null){
            return queryForInSQL(sql, null, ids, new MultiRow(), "",pagination);
        }else{
            return queryForInSQL(sql, null, ids, new MultiRow(), "");
        }
    }

    @Override
    public DyDormRoom setField(ResultSet rs) throws SQLException {
        DyDormRoom dyDormRoom = new DyDormRoom();
        dyDormRoom.setId(rs.getString("id"));
        dyDormRoom.setUnitId(rs.getString("unit_id"));
        dyDormRoom.setBuildingId(rs.getString("building_id"));
        dyDormRoom.setRoomName(rs.getString("room_name"));
        dyDormRoom.setRoomType(rs.getString("room_type"));
        dyDormRoom.setCapacity(rs.getInt("capacity"));
        dyDormRoom.setRemark(rs.getString("remark"));
        dyDormRoom.setFloor(rs.getInt("floor"));
        dyDormRoom.setRoomProperty(rs.getString("room_property"));
        return dyDormRoom;
    }
}
