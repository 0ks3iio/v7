package net.zdsoft.szxy.operation.servermanage.dao;

import net.zdsoft.szxy.operation.hibernate.HumpAliasedEntityTransformer;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import org.hibernate.query.internal.NativeQueryImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author panlf 2019/3/12
 */
public class OpServerExtensionDaoImpl {
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * 根据单位ID查询系统名称，系统使用性质，系统过期时间
     * @param unitId   单位ID
     * @return   返回查询结果
     * @author  zhangfy
     */
    public List<ServerExtensionDto> findSystemByUnitId(String unitId){
        String sql="SELECT bse.id, bs.name,bse.using_nature,bse.expire_time,bse.USING_STATE,bse.server_code" +
                " FROM base_server bs INNER JOIN base_server_extension bse ON bs.code=bse.server_code" +
                " WHERE unit_id='"+unitId+"' order by case when bse.EXPIRE_TIME='' then 1 else 0 end,EXPIRE_TIME";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<ServerExtensionDto> list = nativeQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(ServerExtensionDto.class)).list();
        return list;
    }
}
