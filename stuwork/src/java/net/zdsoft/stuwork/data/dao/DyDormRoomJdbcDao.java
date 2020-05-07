package net.zdsoft.stuwork.data.dao;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DyDormRoomJdbcDao {
    public List<DyDormRoom> findByIdsAndPage(String[] ids , Pagination pagination);
}
