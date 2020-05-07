package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmPlaceSetting;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmPlaceSettingDao extends BaseJpaRepositoryDao<EmPlaceSetting, String> {

    @Query("From EmPlaceSetting where examId=?1 order by columnNo")
    public List<EmPlaceSetting> findByExamId(String examId);

    public void deleteByExamId(String examId);

}