package net.zdsoft.eclasscard.data.dao;

import net.zdsoft.eclasscard.data.entity.EccSeatItem;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EccSeatItemDao extends BaseJpaRepositoryDao<EccSeatItem, String>{

	@Query("From EccSeatItem where unitId=?1 and seatId=?2")
	List<EccSeatItem> findListBySeatId(String unitId, String seatId);

	@Modifying
	@Query("delete from EccSeatItem where classId=?1")
    void deleteByClassId(String classId);

    void deleteByUnitIdAndSeatIdAndRowNumIn(String unitId, String seatId, Integer[] rowNums);

    void deleteByUnitIdAndSeatIdAndStudentIdIn(String unitId, String seatId, String[] studentIds);
}
