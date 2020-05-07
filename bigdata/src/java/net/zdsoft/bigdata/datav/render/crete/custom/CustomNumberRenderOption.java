package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.datav.render.crete.RenderOption;

/**
 * @author shenke
 * @since 2018/10/14 15:12
 */
public class CustomNumberRenderOption extends RenderOption<AbstractNumber> {

    public static CustomNumberRenderOption of(AbstractNumber abstractNumber) {
        CustomNumberRenderOption customNumberRenderOption = new CustomNumberRenderOption();
        customNumberRenderOption.setOp(abstractNumber);
        return customNumberRenderOption;
    }
}
