package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.MicrocodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/22 上午9:24
 */
@Repository
public interface MicrocodeDetailDao extends JpaRepository<MicrocodeDetail, String> {

    /**
     * 根据微代码ID查询微代码明细数据，自动过滤掉is_using=0的数据
     * @param microcodeId 微代码ID
     * @return List
     */
    @Query(value = "from MicrocodeDetail where microcodeId=?1 and isUsing=1")
    List<MicrocodeDetail> getMicrocodeByMicrocodeId(String microcodeId);
}
