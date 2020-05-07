package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupText;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/14 12:59
 */
public class TitleCreator implements RenderOptionCreator<CustomTitleRenderOption> {

    @Override
    public CustomTitleRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.TITLE.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        TitleInnerOption titleInnerOption = ObjectParseUtils.parseTitle(result);
        if (titleInnerOption == null) {
            throw new EntryUtils.DataException("数据不合法");
        }
        earlyRender(earlyParameters, titleInnerOption);

        titleInnerOption.setUrl(StringUtils.trimToEmpty(titleInnerOption.getUrl()));
        if (StringUtils.isNotBlank(titleInnerOption.getUrl())) {
            if (!StringUtils.startsWithIgnoreCase(titleInnerOption.getUrl(), "http")) {
                titleInnerOption.setUrl("http://" + titleInnerOption.getUrl());
            }
        }

        return CustomTitleRenderOption.of(titleInnerOption);
    }

    private void earlyRender(List<EarlyParameter> earlyParameters, TitleInnerOption titleInnerOption) {
        earlyParameters.stream().filter(ep -> GroupKey.text.name().equals(ep.getGroupKey()))
                .findFirst().ifPresent(earlyParameter -> {
            GroupText groupText = NeedRenderGroupBuilder.buildGroup(earlyParameter.getEarlyParameters(), GroupKey.text, GroupText.class);
            if (groupText != null) {
                titleInnerOption.setTextFontColor(groupText.getTextFontColor());
                titleInnerOption.setTextFontSize(groupText.getTextFontSize());
                titleInnerOption.setTextFontWeight(groupText.getTextFontWeight());
            }
        });
    }
}
