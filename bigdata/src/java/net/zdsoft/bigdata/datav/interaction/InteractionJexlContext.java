package net.zdsoft.bigdata.datav.interaction;

import net.zdsoft.bigdata.data.utils.JexlUtils;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.http.util.Asserts;

import java.util.HashMap;

/**
 * @author shenke
 * @since 2018/10/25 下午12:48
 */
final public class InteractionJexlContext extends HashMap<String, Object> {

    private LoginInfo session;

    public InteractionJexlContext(LoginInfo session) {
        Asserts.notNull(session, "loginInfo");
        this.session = session;
    }

    public InteractionJexlContext() {

    }

    public InteractionJexlContext(int initialCapacity, LoginInfo session) {
        super(initialCapacity);
        Asserts.notNull(session, "loginInfo");
        this.session = session;
    }

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value == null) {
            value = JexlUtils.evaluate(buildExpression(key), session).replaceAll("'", "");
        }
        return value;
    }

    private String buildExpression(Object key) {
        StringBuilder expressionBuilder = new StringBuilder("${");
        if (key == null) {
            expressionBuilder.append("null");
        } else {
            expressionBuilder.append(key.toString());
        }
        return expressionBuilder.append("}").toString();
    }
}
