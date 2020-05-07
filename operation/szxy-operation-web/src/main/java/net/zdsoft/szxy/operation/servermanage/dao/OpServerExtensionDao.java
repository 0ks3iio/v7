package net.zdsoft.szxy.operation.servermanage.dao;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 张帆远
 * @since 2019/1/17  上午11:16
 */

@Repository
public interface OpServerExtensionDao extends JpaRepository<ServerExtension, String> {

    /**
     * 单位下的子系统停用和恢复
     * @param usingState  系统使用状态
     * @param id           子系统扩展表主键ID
     * @author  zhangfy
     */
    @Modifying
    @Query("update ServerExtension set usingState=?1 where id=?2")
    void updateUsingStateById(Integer usingState, String id);

    /**
     * 单位下的子系统试用转正式
     * @param usingNature   系统使用性质
     * @param id            子系统扩展表主键ID
     * @author  zhangfy
     */
    @Modifying
    @Query("update ServerExtension set usingNature=?1 where id =?2")
    void updateUsingNatureById(Integer usingNature,String id);

    /**
     * 单位下的子系统续期
     * @param expireTime   系统到期时间（null：默认跟随单位时间）
     * @param id            子系统扩展表主键ID
     * @author  zhangfy
     */
    @Modifying
    @Query("update ServerExtension set expireTime=?1 where id =?2")
    void updateExpireTimeById(Date expireTime, String id);

    /**
     * 根据单位ID查询系统名称，系统使用性质，系统过期时间
     * @param unitId   单位ID
     * @return   返回查询结果
     * @author  zhangfy
     */
    List<ServerExtensionDto> findSystemByUnitId(String unitId);

    /**
     * 根据id查询
     * @param id   子系统扩展表主键ID
     * @return     Optional
     * @author  zhangfy
     */
    @Override
    Optional<ServerExtension> findById(String id);

    @Query(value = "select serverCode from ServerExtension where unitId=?1")
    List<String> findServerCodeByUnitId(String unitId);

    /**
     *
     * @param unitId
     * @return
     * @author panlf
     */
    @Query(value="SELECT COUNT(*) FROM base_unit WHERE is_deleted = '0' ",nativeQuery = true)
    Integer existsByUnitId(String unitId);

    List<ServerExtension> findByUnitId(String unitId);

}
