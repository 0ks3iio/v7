package net.zdsoft.szxy.operation.record.dao;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.hibernate.HumpAliasedEntityTransformer;
import net.zdsoft.szxy.operation.record.controller.dto.ServiceTaskListDto;
import net.zdsoft.szxy.operation.record.controller.dto.ServiceTaskUpdateDto;
import net.zdsoft.szxy.utils.AssertUtils;
import org.apache.logging.log4j.util.Strings;
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

/**
 * @description:
 * @author: Fubicheng
 * @create: 2019-04-04 14:17
 **/
public class ServiceTaskDaoImpl {

    private Logger logger = LoggerFactory.getLogger(ServiceTaskDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Record(type = RecordType.SQL)
    public void updateByTaskTypeAndContentAndIsEmail(ServiceTaskUpdateDto serviceTaskUpdateDto){
        AssertUtils.notNull(serviceTaskUpdateDto,"serviceTask can't be null");
        AssertUtils.notNull(serviceTaskUpdateDto.getId(),"serviceTaskId can't be null");
        Map<Integer, Object> parameters = new HashMap<>(8);
        int index=0;
        StringBuilder sql=new StringBuilder();
        sql.append("update ServiceTask set");
        if(Objects.nonNull(serviceTaskUpdateDto.getTaskType())){
            sql.append(" taskType =?").append(++index).append(",");
            parameters.put(index,serviceTaskUpdateDto.getTaskType());
        }
        if(Objects.nonNull(serviceTaskUpdateDto.getTaskContent())){
            sql.append(" content=?").append(++index).append(",");
            parameters.put(index,serviceTaskUpdateDto.getTaskContent());
        }
        if(Objects.nonNull((serviceTaskUpdateDto.getIsEmail()))){
            sql.append(" isEmail=?").append(++index);
            parameters.put(index,serviceTaskUpdateDto.getIsEmail());
        }
        sql.append(" where id= ?").append(++index);
        parameters.put(index,serviceTaskUpdateDto.getId());
        if (index > 0) {
            Query query = entityManager.createQuery(sql.toString());
            for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            query.executeUpdate();
        }
    }

    @Record(type = RecordType.SQL)
    public Page<ServiceTaskListDto> findByPage(String ownerUserName, Integer taskType, Integer state, Pageable page){
        StringBuilder countSql=new StringBuilder();

        countSql.append("SELECT count(1)")
                .append("from op_service_task t left join op_user u on t.create_user_id = u.id");
        queryConditions(countSql,ownerUserName,taskType,state);

        StringBuilder sb=selectTable();
        queryConditions(sb,ownerUserName,taskType,state);

        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        Query dataQuery = entityManager.createNativeQuery(sb.toString());
        //设置分页
        dataQuery.setMaxResults(page.getPageSize());
        dataQuery.setFirstResult((int) page.getOffset());
        Integer count = Integer.valueOf(countQuery.getSingleResult().toString()).intValue();
        Long total = count.longValue();
        List<ServiceTaskListDto> taskList = dataQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(ServiceTaskListDto.class)).list();
        return new PageImpl<ServiceTaskListDto>(taskList, page, total);
    }

    private StringBuilder queryConditions(StringBuilder sb,String ownerUserName, Integer taskType, Integer state){
        if(Strings.isNotBlank(ownerUserName)){
            sb.append(" where t.owner_user_name like '%"+ownerUserName+"%'");
        }
        if(null!=taskType){
            if(Strings.isNotBlank(ownerUserName)){
                sb.append(" and   t.task_type ="+taskType);
            }else{
                sb.append(" where t.task_type ="+taskType);
            }
        }
        if(null!=state) {
            if (null == taskType && !Strings.isNotBlank(ownerUserName)) {
                sb.append(" where t.state = " + state);
            } else {
                sb.append(" and t.state = " + state);
            }
        }
        sb.append(" order by t.creation_time desc");
        return sb;
    }
    private StringBuilder selectTable(){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT t.id as id , ")
                .append("t.task_type , ")
                .append("t.creation_time , ")
                .append("t.completion_time , ")
                .append("t.owner_user_id , ")
                .append("t.owner_user_name , ")
                .append("u.real_name , ")
                .append("t.content , ")
                .append("t.state , ")
                .append("t.is_email ")
                .append("from op_service_task t left join op_user u on t.create_user_id = u.id");
        return sb;
    }


    @Record(type = RecordType.SQL)
    public List<ServiceTaskListDto>findTaskDtoByCreationTimeAfter(Date creationTime){
        StringBuilder sb=selectTable();
        if(creationTime!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(creationTime.getTime());
            sb.append(" where t.creation_time >= to_date('"+time+"', 'yyyy-mm-dd hh24:mi:ss')");
        }
        Query nativeQuery = entityManager.createNativeQuery(sb.toString());
        List<ServiceTaskListDto> list = nativeQuery.unwrap(NativeQueryImpl.class)
                .setResultTransformer(new HumpAliasedEntityTransformer(ServiceTaskListDto.class)).list();
        return list;
    }
}
