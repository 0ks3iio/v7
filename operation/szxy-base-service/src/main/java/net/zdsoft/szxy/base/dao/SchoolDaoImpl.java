package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhujy
 * 日期:2019/3/19 0019
 */
public class SchoolDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据字段名更新学校数据，school必须包含主键数据
     * @param school 学校数据
     * @param properties 要更新的字段名列表
     */
    @Record(type = RecordType.SQL)
    public void updateSchool(School school, String[] properties){
        String sql = "update School set ";
        List<String> ps=new ArrayList<>();
        for (int i = 0; i < properties.length; i++) {
            ps.add(properties[i] + " = ?" + (i + 1));
        }
        sql += StringUtils.join(ps.toArray(new String[0]), ",");
        sql += " WHERE id = ?" + (properties.length + 1);
        Query query = entityManager.createQuery(sql);

        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("target", school);

        for (int i = 0; i < properties.length; i++) {
            query.setParameter((i + 1), parser.parseExpression("#target." + properties[i]).getValue(context));
        }
        query.setParameter((properties.length + 1), school.getId());
        query.executeUpdate();
    }
}
