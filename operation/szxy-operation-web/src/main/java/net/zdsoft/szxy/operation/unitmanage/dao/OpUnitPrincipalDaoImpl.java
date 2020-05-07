package net.zdsoft.szxy.operation.unitmanage.dao;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.hibernate.HumpAliasedEntityTransformer;
import net.zdsoft.szxy.operation.unitmanage.dto.OpUnitPrincipalQueryDto;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalVo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

public class OpUnitPrincipalDaoImpl {

    private Logger logger = LoggerFactory.getLogger(OpUnitPrincipalDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     *  单位表   单位扩展表  单位联系人表 三表联合查询
     * @param parentId
     * @param opUnitPrincipalQueryDto
     * @param pageable
     * @return
     */
    @Record(type= RecordType.SQL)
    @SuppressWarnings("unchecked")
    public Page<UnitPrincipalVo> findPageByParentId(Set<String> regionsSet, String parentId, OpUnitPrincipalQueryDto opUnitPrincipalQueryDto, Pageable pageable) {
        StringBuilder stringBuilder = new StringBuilder();

        // 统计剩余天数
        StringBuilder dayCountBuilder = new StringBuilder();
        dayCountBuilder.append("(SELECT ")
                .append("trunc(expire_time)-to_date('")
                .append(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).append("', 'yyyy-MM-dd') ")
                .append("from base_unit_extension ")
                .append("where unit_id = u.id) ");

        // 查询相关单位联系人
        StringBuilder unitPrincipalBuilder = new StringBuilder();
        unitPrincipalBuilder.append("SELECT id, ")
                            .append("type, ")
                            .append("real_name, ")
                            .append("phone, ")
                            .append("remark, ")
                            .append("unit_id ")
                            .append("from op_unit_principal ")
                            .append("where unit_id = ?");

        stringBuilder.append("SELECT ")
                .append(" distinct(u.id) as id, ")
                .append(" u.unit_name, ")
                .append(" ex.star_level, ")
                .append(" ex.expire_time, ")
                .append(dayCountBuilder).append("as expire_day ")
                .append(" from base_unit u, base_unit_extension ex, op_unit_principal principal ")
                .append(" where 1=1 and u.id = ex.unit_id and u.id = principal.unit_id");
        // 数据的总行数 分页需要
        StringBuilder countSql=new StringBuilder();
        countSql.append("select count(distinct(u.id)) from base_unit u, base_unit_extension ex, op_unit_principal principal where 1=1 and u.id = ex.unit_id and u.id = principal.unit_id ");
        if (opUnitPrincipalQueryDto != null) {
            // 判断是否需要通过星级过滤
            if (opUnitPrincipalQueryDto.getStarLevel() != null && !StringUtils.isBlank(opUnitPrincipalQueryDto.getStarLevel().toString())) {
                stringBuilder.append(" and ex.star_level = '").append(opUnitPrincipalQueryDto.getStarLevel()).append("' ");
                countSql.append(" and ex.star_level = '").append(opUnitPrincipalQueryDto.getStarLevel()).append("' ");
            }
            // 判断是否需要通过单位联系人名称过滤
            if (opUnitPrincipalQueryDto.getUnitHeader() != null && !StringUtils.isBlank(opUnitPrincipalQueryDto.getUnitHeader())) {
                stringBuilder.append(" and principal.real_name like '%").append(opUnitPrincipalQueryDto.getUnitHeader()).append("%' ");
                countSql.append(" and principal.real_name like '%").append(opUnitPrincipalQueryDto.getUnitHeader()).append("%' ");
            }
            // 判断是否需要通过单位联系人手机号过滤
            if (opUnitPrincipalQueryDto.getUnitHeaderPhone() != null && !StringUtils.isBlank(opUnitPrincipalQueryDto.getUnitHeaderPhone())) {
                stringBuilder.append(" and principal.phone like '%").append(opUnitPrincipalQueryDto.getUnitHeaderPhone()).append("%' ");
                countSql.append(" and principal.phone like '%").append(opUnitPrincipalQueryDto.getUnitHeaderPhone()).append("%' ");
            }
            //  通过parentId过滤
            if (StringUtils.isBlank(parentId)) {
              //  stringBuilder.append(" and (u.parent_Id = '00000000000000000000000000000000' or u.id =u.root_unit_id )");
              //  countSql.append(" and (u.parent_Id = '00000000000000000000000000000000' or u.id =u.root_unit_id )");
            } else {
                stringBuilder.append(" and (u.id = '").append(parentId).append("'").append("or u.parent_id='").append(parentId).append("') ");
                countSql.append(" and (u.id = '").append(parentId).append("'").append("or u.parent_id='").append(parentId).append("') ");
            }
            // 通过行政区划过滤
            if (!regionsSet.isEmpty()) {
                stringBuilder.append(" and (");
                countSql.append(" and (");
                int index = 0;
                for (String region : regionsSet) {
                    stringBuilder.append("substr(u.region_code,1,"+region.length()+")='").append(region).append("'");
                    countSql.append("substr(u.region_code,1,"+region.length()+")='").append(region).append("'");
                    if (++index < regionsSet.size()) {
                        stringBuilder.append(" or ");
                        countSql.append(" or ");
                    }
                }
                stringBuilder.append(" ) ");
                countSql.append(" ) ");
            }

        }
        // 根据星级排序
        stringBuilder.append(" order by ex.star_level, u.id");

        Query dataQuery = entityManager.createNativeQuery(stringBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // 设置数据分页
        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());
        Object singleResult = countQuery.getSingleResult();
        Integer count = Integer.valueOf(countQuery.getSingleResult().toString()).intValue();
        long total = count.longValue();
        List<UnitPrincipalVo> unitPrincipalVoList = dataQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer<>(UnitPrincipalVo.class)).list();
        StringBuilder unitPrincipalSql = null;
        Query unitPrincipalQuery = null;
        List<OpUnitPrincipal> opUnitPrincipalList = null;
        Iterator<UnitPrincipalVo> iterator = unitPrincipalVoList.iterator();
        while (iterator.hasNext()) {
            UnitPrincipalVo unitPrincipalVo = iterator.next();
            // 查询某个单位下的所有联系人 并放入unitPrincipalVo集合中
            unitPrincipalQuery = entityManager.createNativeQuery(unitPrincipalBuilder.toString());
            unitPrincipalQuery.setParameter(1,unitPrincipalVo.getId());
            opUnitPrincipalList = unitPrincipalQuery.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(new HumpAliasedEntityTransformer<>(OpUnitPrincipal.class)).list();
            unitPrincipalVo.setUnitPrincipals(opUnitPrincipalList);
        }
        return new PageImpl<UnitPrincipalVo>(unitPrincipalVoList, pageable, total);
    }
}
