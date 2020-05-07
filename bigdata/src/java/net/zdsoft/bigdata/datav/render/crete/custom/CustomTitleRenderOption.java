package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.datav.render.crete.RenderOption;

/**
 * @author shenke
 * @since 2018/10/14 13:00
 */
public class CustomTitleRenderOption extends RenderOption<TitleInnerOption> {

    public static CustomTitleRenderOption of(TitleInnerOption titleInnerOption) {
        CustomTitleRenderOption customTitleRenderOption = new CustomTitleRenderOption();
        customTitleRenderOption.setOp(titleInnerOption);
        return customTitleRenderOption;
    }
}
