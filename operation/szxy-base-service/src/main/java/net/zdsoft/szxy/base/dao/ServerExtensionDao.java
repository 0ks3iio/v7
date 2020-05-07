package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.ServerExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/22 下午2:28
 */
@Repository
public interface ServerExtensionDao extends JpaRepository<ServerExtension, String> {

    /**
     * 获取指定单位的授权关系数据
     * @param unitId 单位ID
     * @return Stream
     */
    @Query(value = "from ServerExtension where unitId=?1")
    List<ServerExtension> getExtensionsByUnitId(String unitId);

    /**
     * 根据单位删除扩展数据
     * @param unitId 单位ID
     */
    @Modifying
    void deleteByUnitId(String unitId);
}
