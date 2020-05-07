/**
 * FileName: JexlTest.java
 * Author:   shenke
 * Date:     2018/5/23 下午3:46
 * Descriptor:
 */
package net.zdsoft.el;

import net.zdsoft.bigdata.data.utils.JexlUtils;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/5/23 下午3:46
 */
public class JexlTest {

    @Test
    public void _test() {
        String express = "select * from base_user where username=${username}";
        String val = JexlUtils.evaluate(express, "username", "shengld");
        Assert.assertEquals(val, "select * from base_user where username=shengld");

        val = JexlUtils.evaluate(express, "username", Integer.valueOf(10));
        Assert.assertEquals(val, "select * from base_user where username=10");

        User user = new User();
        user.setUsername("ceshi");
        Assert.assertEquals( "select * from base_user where username=ceshi",
                JexlUtils.evaluate(express, user));

        String sql = "select * from base_user where age > ${age}";

        Assert.assertEquals("select * from base_user where age > 10",
                JexlUtils.evaluate(sql, "age", 10));
    }

    public static class User {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    @Test
    public void testSpringEL() {
        User user = new User();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "ccc");
        user.setUsername("shengld");
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(user);
        context.setVariable("username", "shengld");
        ParserContext parserContext = new TemplateParserContext();
        org.springframework.expression.Expression expression =
                parser.parseExpression("select * from base_user where username = #{username}", parserContext);
        System.out.println(expression.getValue(context).toString());
    }


    @Test
    public void _test2() {
        ExpressionParser parser = null;
        Map<String, Object> dynamic = new HashMap<>(1);
        dynamic.put("userId", "1111");
        String sql = "select * from base_user where id>${userId}";
        JexlContext context = new MapContext(dynamic);
        JexlEngine engine = new JexlEngine();
        UnifiedJEXL unifiedJEXL = new UnifiedJEXL(engine);
        System.out.println(unifiedJEXL.parse(sql).evaluate(context).toString());

    }
}
