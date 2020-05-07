package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.RenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/12/3 上午9:46
 */
public class DigitalCardTurnerCreator implements RenderOptionCreator<RenderOption<DigitalCardTurnerOption>> {

    @Override
    public RenderOption<DigitalCardTurnerOption> create(Result result, Diagram diagram,
                                                        List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.DIGITAL_CARD_TURNER.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        AbstractNumber number = ObjectParseUtils.parseUpNumber(result);
        String value = null;
        if (number != null) {
            value = number.getValue();
        }

        RenderOption<DigitalCardTurnerOption> renderOption = new RenderOption<>();
        DigitalCardTurnerOption option = earlyRender(earlyParameters);
        option.setNumber(format(value));
        renderOption.setOp(option);
        return renderOption;
    }

    private DigitalCardTurnerOption earlyRender(List<EarlyParameter> earlyParameters) {
        Optional<DigitalCardTurnerOption.DigitalCardTurnerTitleStyle> styleOptional =
                earlyParameters.stream().filter(earlyParameter -> GroupKey.digital_card_title.name().equals(earlyParameter.getGroupKey()))
                        .findAny().map(earlyParameter -> {

                    return NeedRenderGroupBuilder.buildGroup(earlyParameter.getEarlyParameters(),
                            GroupKey.digital_card_title, DigitalCardTurnerOption.DigitalCardTurnerTitleStyle.class);
                });
        Optional<DigitalCardTurnerOption.DigitalCardTurner> turnerOptional =
                earlyParameters.stream().filter(earlyParameter -> GroupKey.digital_card_turner.name().equals(earlyParameter.getGroupKey()))
                        .findAny().map(earlyParameter -> {

                    return NeedRenderGroupBuilder.buildGroup(earlyParameter.getEarlyParameters(),
                            GroupKey.digital_card_turner, DigitalCardTurnerOption.DigitalCardTurner.class);
                });
        DigitalCardTurnerOption option = new DigitalCardTurnerOption();
        option.setTurner(turnerOptional.get());
        option.setTitleStyle(styleOptional.get());
        return option;
    }

    private String format(String value) {
        if (StringUtils.contains(value, ".")) {
            return value;
        }
        if (value == null || Integer.valueOf(0).equals(value)) {
            return "0";
        }
        StringBuilder formatNumberBuilder = new StringBuilder();
        value = StringUtils.reverse(value);

        for (int i = 1, length = value.length(); i <= length; i++) {
            formatNumberBuilder.append(value.charAt(i - 1));
            if (i % 3 == 0) {
                formatNumberBuilder.append(",");
            }
        }
        formatNumberBuilder.reverse();
        if (formatNumberBuilder.charAt(0) == ',') {
            return formatNumberBuilder.substring(1);
        }
        return formatNumberBuilder.toString();
    }
}
