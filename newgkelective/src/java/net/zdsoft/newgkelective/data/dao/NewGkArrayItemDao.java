package net.zdsoft.newgkelective.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;




import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;

public interface NewGkArrayItemDao extends BaseJpaRepositoryDao<NewGkArrayItem, String>{

	@Query("from NewGkArrayItem where isDeleted=0 and divideId=?1 order by divideType,times desc")
	List<NewGkArrayItem> findByDivideId(String divideId);
	
	@Query("from NewGkArrayItem where isDeleted=0 and divideId=?1 and divideType in (?2) order by divideType,times desc")
	List<NewGkArrayItem> findByDivideIdAndType(String divideId,
			String[] divideType);
	@Query("select id from NewGkArrayItem where isDeleted=0 and divideId=?1 and divideType =?2 and itemName = ?3")
	List<String> findIdsByDivideIdName(String divideId, String divideType, String arrayName);
	
	@Query("from NewGkArrayItem where isDeleted=0 and divideId=?1 and divideType =(?2) order by times desc")
	List<NewGkArrayItem> findByDivideIdAndDivideType(String divideId,
			String divideType);

	@Modifying
	@Query("update NewGkArrayItem set isDeleted=1, modifyTime=?1 where id=?2 ")
	void deleteId(Date creationTime, String id);

}
