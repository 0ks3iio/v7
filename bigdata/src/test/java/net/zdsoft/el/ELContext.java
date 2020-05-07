/**
 * FileName: ELContext.java
 * Author:   shenke
 * Date:     2018/5/23 下午5:29
 * Descriptor:
 */
package net.zdsoft.el;

import org.springframework.expression.ParserContext;

/**
 * @author shenke
 * @since 2018/5/23 下午5:29
 */
public class ELContext implements ParserContext {

    @Override
    public boolean isTemplate() {
        return false;
    }

    @Override
    public String getExpressionPrefix() {
        return "#{";
    }

    @Override
    public String getExpressionSuffix() {
        return "}";
    }
}
