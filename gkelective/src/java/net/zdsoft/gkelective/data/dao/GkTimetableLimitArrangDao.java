package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkTimetableLimitArrang;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkTimetableLimitArrangDao extends BaseJpaRepositoryDao<GkTimetableLimitArrang, String> {
    @Query("From GkTimetableLimitArrang where acadyear = ?1 and semester = ?2 and limitId = ?3 and limitType = ?4")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangList(String acadyear, String semester,
            String limitId, String limitType);

    @Query("From GkTimetableLimitArrang where acadyear = ?1 and semester = ?2  and limitType = ?3 and limitId in(?4)")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListbyLimitIds(String acadyear, String semester,
            String limitType, List<String> list);

    @Modifying
    @Query("delete from GkTimetableLimitArrang where id = ?1")
    void deleteById(String id);

    @Query("From GkTimetableLimitArrang where acadyear = ?1 and semester = ?2 and limitType = ?3 and arrangId = ?4 and limitId in(?5)")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListBylimitIdIn(String acadyear, String semester,
            String limitType, String arrangId, String[] limitIds);

    @Modifying
    @Query("delete from GkTimetableLimitArrang where acadyear = ?1 and semester = ?2 and limitId = ?3 and period = ?4 and periodInterval = ?5 and weekday = ?6")
    public void deleteBySelectXyz(String acadyear, String semester, String limitId, String period,
            String periodInterval, String weekday);

    @Query("From GkTimetableLimitArrang where acadyear = ?1 and semester = ?2 and limitId = ?3 and period = ?4 and periodInterval = ?5 and weekday = ?6")
    public GkTimetableLimitArrang findGkTimetableLimitArrangBySelectXyz(String acadyear, String semester,
            String limitId, String period, String periodInterval, String weekday);
    
    @Query("From GkTimetableLimitArrang where isUsing = 1 and acadyear = ?1 and semester = ?2  and period = ?3 and periodInterval = ?4 and weekday = ?5 and limitId in (?6)")
    public List<GkTimetableLimitArrang> findGkTimetableLimitArrangBySelectXyz(String acadyear, String semester,
            String period, String periodInterval, String weekday,String[] limitIds);
    @Modifying
    @Query("delete from GkTimetableLimitArrang where id in (?1) ")
    public void deleteByIds(String[] ids);
    @Modifying
    @Query("delete from GkTimetableLimitArrang where acadyear=?1 and semester = ?2 and limitId=?3 and limitType = ?4 ")
	public void deleteByLimitId(String acadyear, String semester,
			String roundsId, String type);
    @Modifying
    @Query("delete from GkTimetableLimitArrang where acadyear=?1 and semester = ?2 and arrangId=?3 and limitType = ?4 ")
	public void deleteByArrangeId(String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 0 where acadyear = ?1 and semester = ?2 and limitId=?3 and limitType = ?4 ")
	public void notUsingByLimitId(String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 0 where acadyear = ?1 and semester = ?2 and arrangId=?3 and limitType = ?4 ")
	public void notUsingByArrangeId(String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 1 where acadyear=?1 and semester = ?2 and limitId=?3 and limitType = ?4 ")
	public void yesUsingByLimitId(String acadyear, String semester,
			String roundsId, String type);
    
    @Modifying
    @Query("update GkTimetableLimitArrang set isUsing = 1 where acadyear=?1 and semester = ?2 and arrangId=?3 and limitType = ?4 ")
	public void yesUsingByArrangeId(String acadyear, String semester,
			String roundsId, String type);
    
    @Query("From GkTimetableLimitArrang where acadyear = ?1 and semester = ?2 and limitId = ?3 and limitType = ?4 and isUsing=1 ")
	public List<GkTimetableLimitArrang> findGkTimetableLimitArrangListIsUsing(
			String searchAcadyear, String searchSemester, String limitId,
			String limitType);
    @Query("From GkTimetableLimitArrang where acadyear=?1 and semester = ?2 and arrangId=?3 and limitType = ?4 ")
	public List<GkTimetableLimitArrang> findByArrangeIdType(String acadyear,String semester,String arrangeId,
			String limitType);
}
