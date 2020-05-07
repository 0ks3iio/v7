package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.DataModelParam;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataModelParamDao extends BaseJpaRepositoryDao<DataModelParam, String> {

    List<DataModelParam> findAllByCodeAndTypeAndParentIdOrderByOrderId(String code, String type, String parentId);

    List<DataModelParam> findAllByCodeAndTypeOrderByOrderId(String code, String type);

    void deleteByCode(String code);


    @Query(nativeQuery = true,value = "select * from BG_MODEL_PARAM  " +
            "where USE_TABLE= ?1 and type='dimension' and id not in " +
            "(select m.id from BG_MODEL_PARAM m where m.USE_TABLE= ?1 and id!= ?2 start with m.id= ?2 connect by m.PARENT_ID= prior m.id)")
    List<DataModelParam> findNonDirectDimensionByIdAndUseTable( String useTable, String id);

    List<DataModelParam> findByParentId(String id);
}
