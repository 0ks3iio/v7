package net.zdsoft.szxy.operation.unitmanage.dao;

import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.operation.dto.TreeNode;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitExportDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * @author ZhuJinY
 * @since 2019年1月18日下午6:59:27
 */
@Repository
public interface OpUnitDao extends PagingAndSortingRepository<Unit,String>,JpaSpecificationExecutor<Unit>,JpaRepository<Unit, String>{
    /**
     * 查询教育局（父级单位）
     * @param unitType
     * @return
     */
    @Query(value = "select * from base_unit where unit_Type=?1",nativeQuery = true)
    List<Unit> findByUnitType(String unitType);


    /**
     * 分页查询单位
     * @return
     */
    Page<UnitDto> findPageByParentId(String parentId,String unitType,String usingNature, Pageable page);

    /**
     * 根据父id 找 子单位树
     * @author panlf 2019/2/1
     */
    List<TreeNode> findTreeNodeByParentId(String parentId);

    /**
     * 顶层单位单位树
     * @author panlf 2019/2/1
     */
    List<TreeNode> findAllTopUnits(Set<String> regionCodes);

    /**
     * 根据单位名称,模糊查询单位
     * @author panlf 2019/2/1
     */
    @Query("select new net.zdsoft.szxy.operation.dto.TreeNode(u.id,u.unitName) " +
            "from Unit u where  u.unitName like %?1% and u.isDeleted='0'")
    List<TreeNode> findTreeNodeByName(String name);

    /**
     * 根据单位名称以及登入账户的权限,模糊查询单位
     * @param name
     * @param regionCodes
     * @return
     */
    @Query("select new net.zdsoft.szxy.operation.dto.TreeNode(u.id,u.unitName) " +
            "from Unit u where  u.unitName like %?1% and u.isDeleted='0'and u.regionCode in (?2)")
    List<TreeNode> findTreeNodeByNameAndRegionCode(String name,List<String> regionCodes);

    /**
     * 返回所有单位基本信息(excel导出时调用)
     * @return
     */
    List<UnitExportDto> findAllUnitVo();

    /**
     * 分页查询单位列表
     * @return
     */
    Page<UnitDto> queryUnitsByUnitQuery(UnitQuery unitQuery, Pageable page);
    List<Unit> findByParentId(String parentId);

    @Query(value = "select * from base_unit where unit_class=?1 and unit_type!=?2 and is_deleted='0'",nativeQuery = true)
    List<Unit> findByUnitClassAndUnitType(Integer unitClass,Integer unitType);

}
