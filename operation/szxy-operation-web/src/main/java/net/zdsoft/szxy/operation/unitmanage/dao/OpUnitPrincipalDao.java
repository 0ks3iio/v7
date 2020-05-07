package net.zdsoft.szxy.operation.unitmanage.dao;

import net.zdsoft.szxy.operation.unitmanage.dto.OpUnitPrincipalQueryDto;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * 操作单位联系人
 * @author zhanWenze
 * @since 2019年4月2日
 */
public interface OpUnitPrincipalDao extends JpaRepository<OpUnitPrincipal, String>, JpaSpecificationExecutor<OpUnitPrincipal> {

    /**
     *  根据单位id查询单位负责人
     * @param unitId
     * @return
     */
    List<OpUnitPrincipal> findByUnitId(String unitId);


    /**
     *  分页查询单位联系人相关信息  包括单位信息 联系人信息
     *
     * @param regionsSet
     * @param parentId
     * @param opUnitPrincipalQueryDto
     * @param pageable
     * @return
     */
    Page<UnitPrincipalVo>  findPageByParentId(Set<String> regionsSet, String parentId, OpUnitPrincipalQueryDto opUnitPrincipalQueryDto, Pageable pageable);

    void deleteByUnitId(String unitId);
}
