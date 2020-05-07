package net.zdsoft.szxy.operation.unitmanage.dao;

import net.zdsoft.szxy.base.enu.ID;
import net.zdsoft.szxy.base.enu.UnitExtensionNature;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.dto.TreeNode;
import net.zdsoft.szxy.operation.hibernate.HumpAliasedEntityTransformer;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitExportDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitQuery;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhujy
 * 日期:2019/1/23 0023
 */
public class OpUnitDaoImpl {

    private Logger logger = LoggerFactory.getLogger(OpUnitDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Record(type = RecordType.SQL)
    @SuppressWarnings("unchecked")
    public Page<UnitDto> findPageByParentId(String parentId,String unitType,String usingNature, Pageable pageable){
        StringBuilder sb = new StringBuilder();

        StringBuilder countBuilder = new StringBuilder();
        countBuilder.append("(SELECT ")
                .append("count(*) as count ")
                .append("from base_server_extension ")
                .append("where unit_id = u.id ")
                .append("and trunc(expire_time)<to_date('")
                .append(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).append("', 'yyyy-MM-dd') ")
                .append("and using_state <> 1) ");

        StringBuilder dayCountBuilder = new StringBuilder();
        dayCountBuilder.append("(SELECT ")
                .append("trunc(expire_time)-to_date('")
                .append(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).append("', 'yyyy-MM-dd') ")
                .append("from base_unit_extension ")
                .append("where unit_id = u.id) ");

        StringBuilder regionNameBuilder = new StringBuilder();
        regionNameBuilder.append("(SELECT ")
                .append("region_name from base_region ")
                .append("where full_code=u.region_code and rownum=1) ");


        sb.append("SELECT ")
                .append("u.id as id,")
                .append("u.unit_name ,")
                .append("u.union_code ,")
                .append("u.region_code ,")
                .append("u.unit_type ,")
                .append("ex.expire_time ,")
                .append("ex.expire_time_type,")
                .append("ex.using_nature ,")
                .append("ex.using_state ,")
                .append(countBuilder).append(" as count, ")
                .append(regionNameBuilder).append(" as region_name, ")
                .append(dayCountBuilder).append("as expire_day ")
                .append("from base_unit u left join base_unit_extension ex on u.id=ex.unit_id ")
                .append("where 1=1 and u.is_deleted='0' ");
        StringBuilder countSql=new StringBuilder();
        countSql.append("select count(1) from base_unit u left join base_unit_extension ex on u.id=ex.unit_id  where 1=1 and u.is_Deleted='0' ");
        //判断是否有传入单位类型
        if (!StringUtils.isBlank(unitType)){
            sb.append(" and u.unit_type = '").append(unitType).append("'");
            countSql.append(" and u.unit_type = '").append(unitType).append("'");
        }
        //判断是否有传入单位性质
        if (!StringUtils.isBlank(usingNature)){
            if (Integer.valueOf(usingNature)==-1){
                sb.append(" and ex.using_state=1");
                countSql.append(" and ex.using_state=1");
            }else {
                sb.append(" and ex.using_nature='").append(usingNature).append("'");
                countSql.append(" and ex.using_nature='").append(usingNature).append("'");
            }
        }
        if (StringUtils.isBlank(parentId)){
            sb.append(" and (u.parent_Id = '00000000000000000000000000000000' or u.id =u.root_unit_id )");
            countSql.append(" and (u.parent_Id = '00000000000000000000000000000000' or u.id =u.root_unit_id )");
        }else{
            sb.append(" and (u.id = '").append(parentId).append("'").append("or u.parent_id='").append(parentId).append("')");
            countSql.append(" and (u.id = '").append(parentId).append("'").append("or u.parent_id='").append(parentId).append("')");
        }
        sb.append("order by case when u.id='").append(parentId).append("' then 0 else 1 end,ex.expire_time asc,ex.expire_time_type asc");

        Query dataQuery = entityManager.createNativeQuery(sb.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        //设置分页
        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());
        Integer count = Integer.valueOf( countQuery.getSingleResult().toString()).intValue();
        Long total = count.longValue();
        List<UnitDto> unitDtos = dataQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(UnitDto.class)).list();
        return new PageImpl<UnitDto>(unitDtos, pageable, total);

    }

    @Record(type = RecordType.SQL)
    public List<UnitExportDto> findAllUnitVo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today=sdf.format(Calendar.getInstance().getTime());
        String sql="select b.id,b.unit_name,b.union_Code,b.region_code,b.unit_type,e.expire_Time_Type," +
                "e.expire_Time,e.using_Nature,e.using_State, " +
                "(select count(*) from base_server_extension where unit_id=b.id and using_state<>1 and " +
                "(trunc(expire_time)<to_date('"+today+"','yyyy-MM-dd') or " +
                "(e.expire_time_type='1' and trunc(e.expire_time)<to_date('"+today+"','yyyy-MM-dd') ))) " +
                "as sys_expired_count, (select region_name from base_region where region_code=b.region_code) as " +
                "region_name from base_unit b left join base_unit_extension e on b.id=e.unit_id where b.is_Deleted='0' ";
        List<Object[]> list = entityManager.createNativeQuery(sql).getResultList();

        return list.stream().map(x->{
            UnitExportDto unitExportDto = new UnitExportDto();
            unitExportDto.setId(x[0].toString());
            if(x[1]!=null){
                unitExportDto.setUnitName(x[1].toString());
            }
            if(x[2]!=null){
                unitExportDto.setUnionCode(x[2].toString());
            }
            if(x[3]!=null){
                unitExportDto.setRegionCode(x[3].toString());
            }
            if(x[4]!=null){
                unitExportDto.setUnitType(Integer.parseInt(x[4].toString()));
            }
            if(x[5]!=null){
                unitExportDto.setExpireTimeType(Integer.parseInt(x[5].toString()));
            }

            if(x[6]!=null){
                unitExportDto.setExpireTime((Date)x[6]);
            }
            if(x[7]!=null){
                unitExportDto.setUsingNature(Integer.parseInt(x[7].toString()));
            }
            if(x[8]!=null){
                unitExportDto.setUsingState(Integer.parseInt(x[8].toString()));
            }
            if(x[9]!=null){
                unitExportDto.setSysExpiredCount(Integer.parseInt(x[9].toString()));
            }
            if(x[10]!=null){
                unitExportDto.setRegionName(x[10].toString());
            }
            return unitExportDto;
        }).collect(Collectors.toList());
    }


    public Page<UnitDto> queryUnitsByUnitQuery(UnitQuery unitQuery, Pageable pageable) {

        //处理查询参数
        unitQuery.setParentId(Optional.ofNullable(unitQuery.getParentId()).orElse(ID.ZERO_32));
        unitQuery.setUsingNature(Optional.ofNullable(unitQuery.getUsingNature()).orElse(UnitExtensionNature.OFFICIAL));


        StringBuilder counter = new StringBuilder();
        counter.append("(SELECT count(*) ")
                .append("from base_unit u left join base_server_extension ex on u.id=ex.unit_id ")
                .append("where u.is_deleted = '0' ")
                .append("and u.parent_id = '").append(unitQuery.getParentId()).append("' ")
                .append("and ex.using_nature='").append(unitQuery.getUsingNature()).append("') ");

        StringBuilder sb = new StringBuilder();

        StringBuilder countBuilder = new StringBuilder();
        countBuilder.append("(SELECT ")
                .append("count(*) as count ")
                .append("from base_server_extension ")
                .append("where unit_id = u.id ")
                .append("and trunc(expire_time)<to_date('")
                .append(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).append("', 'yyyy-MM-dd') ")
                .append("and using_state <> 1) ");

        StringBuilder regionNameBuilder = new StringBuilder();
        regionNameBuilder.append("(SELECT ")
                .append("region_name from base_region ")
                .append("where full_code=u.region_code) ");


        sb.append("SELECT ")
                .append("u.id as id,")
                .append("u.unit_name ,")
                .append("u.union_code ,")
                .append("u.region_code ,")
                .append("u.unit_type ,")
                .append("ex.expire_time ,")
                .append("ex.expire_time_type,")
                .append("ex.using_nature ,")
                .append(countBuilder).append(" as count, ")
                .append(regionNameBuilder).append(" as region_name ")
                //.append("")
                .append("from base_unit u left join base_unit_extension ex on u.id=ex.unit_id ")
                .append("where u.is_deleted='0' ");
        //上级单位ID
        sb.append("and u.parent_id='").append(unitQuery.getParentId()).append("' ")
                .append("or u.id=u.root_unit_id ");
        //使用性质
        sb.append("and ex.using_nature='").append(unitQuery.getUsingNature()).append("' ");
        //单位类型
        if (Objects.nonNull(unitQuery.getUnitType())) {
            StringBuilder unitTypeBuilder = new StringBuilder();
            unitTypeBuilder.append("u.unit_type='").append(unitQuery.getUnitType()).append("' ");

            sb.append(unitTypeBuilder);
            counter.append(unitTypeBuilder);
        }
        //排序
        sb.append("order by case when u.id='").append(unitQuery.getParentId())
                .append("' then 0 else 1 end, ex.expire_time asc, ex.expire_time_type asc");

        if (logger.isDebugEnabled()) {
            logger.debug("Query Data SQL: {}", sb.toString());
            logger.debug("Query Count SQL: {}", counter.toString());
        }

        Query unitDataQuery = entityManager.createNativeQuery(sb.toString());

        Query countQuery = entityManager.createNativeQuery(counter.toString());

        List<UnitDto> unitDtos = unitDataQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(UnitDto.class)).list();

        return new PageImpl<UnitDto>(unitDtos, pageable, Long.valueOf(countQuery.getSingleResult().toString()));
    }

    public List<TreeNode> findAllTopUnits(Set<String> regionCodes){
        StringBuilder sql= new StringBuilder();
        String isParentQuery = "(case when ((select count(1) from base_unit c where c.is_deleted='0' and c.parent_id=b.id )>0) then 'true' else 'false' end)";
        sql.append("select b.id, b.parent_id as p_id, b.unit_name as name ,")
                .append(isParentQuery).append(" as is_parent ")
                .append("from base_unit b where ");
        if (regionCodes.isEmpty()) {
            sql.append("b.is_deleted='0' and (b.parent_id='00000000000000000000000000000000' or b.id=b.root_unit_id) ");
        } else {
            sql.append("(");
            int regionCounter = 0;
            for (String regionCode : regionCodes) {
                sql.append("b.region_code='").append(StringUtils.rightPad(regionCode, 6, "0")).append("'");
                if (++regionCounter < regionCodes.size()) {
                    sql.append("or ");
                }
            }
            //已经确定了行政区划只能选择到区县，因此在查顶级单位的时候过滤掉学校和乡镇教育局即可
            sql.append(") and b.unit_class=1 and b.unit_type <> 8");
        }
        sql.append(" order by is_parent desc, b.unit_type");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        List<TreeNode> list = nativeQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(TreeNode.class)).list();
        return list;
    }
    public List<TreeNode> findTreeNodeByParentId(String parentId){
        String sql="select b.id,b.parent_id as pId,b.unit_name as name,(case when ((select count(1) from base_unit c " +
                " where c.is_deleted='0' and c.parent_id=b.id )>0) then 'true' else 'false' end) as is_parent from base_unit b " +
                " where b.parent_id='"+parentId+"' and b.is_deleted='0' order by is_parent desc,b.unit_type";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<TreeNode> list = nativeQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(TreeNode.class)).list();
        return list;
    }
}
