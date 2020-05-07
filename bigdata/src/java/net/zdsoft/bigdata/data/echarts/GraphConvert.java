package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.FontStyle;
import net.zdsoft.echarts.enu.FontWeightEnum;
import net.zdsoft.echarts.enu.Layout;
import net.zdsoft.echarts.enu.LineType;
import net.zdsoft.echarts.enu.RoamEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.SymbolEnum;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.echarts.series.Graph;
import net.zdsoft.echarts.series.data.GraphData;
import net.zdsoft.echarts.series.data.SData;
import net.zdsoft.echarts.series.inner.Category;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.echarts.style.Label;
import net.zdsoft.echarts.style.LineStyle;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author shenke
 * @since 18-8-27 下午1:11
 */
public class GraphConvert {

    public static Option graph(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        JData data = createData(result);
        Option option = new Option();
        JConverter.convert(data, option);
        triggerExpose(option, (OptionExposeGraph) oe, name, data);

        return option;
    }


    private static JData createData(Result result) throws EntryUtils.DataException {
        JData data = EntryUtils.parseLinkData(result.getValue(), EntryUtils.EntryMapping.entry);
        data.setType(SeriesEnum.graph);
        return data;
    }

    private static void triggerExpose(Option option, OptionExposeGraph optionExpose, String title, JData data) {
        if (option == null) {
            return ;
        }
        if (optionExpose != null) {
            triggerExposeTooltip(option, optionExpose);
            triggerDataCategory(option, optionExpose, data);
            triggerLinkCategory(option, optionExpose, data);
            triggerExposeCommon(option, optionExpose);
        }
        triggerExposeTitle(option, optionExpose == null ? null : optionExpose.getExposeTitle(), title);
    }

    private static void triggerForce(Graph graph, OptionExposeGraph.Force  force) {
        Graph.Force gf = new Graph.Force();
        if (force == null) {
            force = OptionExposeGraph.Force.create();
        }
        gf.setEdgeLength(force.getEdgeLength());
        if (StringUtils.startsWith(force.getRepulsion(), "[")) {
            gf.setRepulsion(OptionExposeUtils.toIntArray(force.getRepulsion()));
        } else {
            gf.setRepulsion(NumberUtils.toInt(force.getRepulsion()));
        }
        graph.setForce(gf);
    }

    private static void triggerExposeCommon(Option option, OptionExposeGraph optionExpose) {
        option.series().stream().findFirst().ifPresent(series -> {
            if (StringUtils.isNotBlank(optionExpose.getGraphLayout())) {
                ((Graph) series).setLayout(Layout.valueOf(optionExpose.getGraphLayout()));
            }
            ((Graph) series).setSymbolSize(optionExpose.getGraphSymbolSize());
            //if (StringUtils.isNotBlank(optionExpose.getGraphSymbol())) {
            ((Graph) series).setSymbol(SymbolEnum.valueOf(optionExpose.getGraphSymbol()));
            Label<Graph> label = new Label<>();
            label.show(optionExpose.getShowSLabel())
                    .fontSize(optionExpose.getsLabelTextFontSize())
                    .color(optionExpose.getsLabelTextFontColor())
                    .fontStyle(Optional.ofNullable(optionExpose.getsLabelTextFontItalic()).orElse(Boolean.FALSE) ? FontStyle.italic : FontStyle.normal)
                    .fontWeight(Optional.ofNullable(optionExpose.getsLabelTextFontBold()).orElse(Boolean.FALSE) ? FontWeightEnum.bold : FontWeightEnum.normal);
            ((Graph) series).setLabel(label);
            /*
             * Force 仅仅引力布局的时候才需要这个参数
             * 但是考虑到页面上可能会切换布局方式，因此在初始化参数的时候会初始化该参数
             * triggerForce 方法会检查 force ，若其为空则会创建默认的Force 参数
             */
            triggerForce(((Graph) series), optionExpose.getForce());
        });
        option.setColor(optionExpose.getColors());
    }

    private static void triggerLinkCategory(Option option, OptionExposeGraph optionExpose, JData data) {
        List<OptionExposeGraph.LinkCategory> linkCategories = optionExpose.getLinkCategories();
        if (isCreateLinkCategory(linkCategories, data.getLinkList())) {
            linkCategories = createLinkCategories(data.getLinkList());
            optionExpose.setLinkCategories(linkCategories);
        }

        option.series().stream().findFirst().ifPresent(series -> {
            Map<String, OptionExposeGraph.LinkCategory> linkCategoryMap =
                    optionExpose.getLinkCategories().stream().collect(Collectors.toMap(OptionExposeGraph.LinkCategory::getLinkCategoryName, Function.identity()));
            Arrays.stream(((Graph) series).getLinks()).forEach(link -> {
                OptionExposeGraph.LinkCategory linkCategory = linkCategoryMap.get(link.getCategory());
                if (linkCategory != null) {
                    LineStyle<Graph.Link, LineStyle> lineStyle = new LineStyle<>();
                    lineStyle.setColor(linkCategory.getLinkCategoryLineColor());
                    lineStyle.setWidth(linkCategory.getLinkCategoryLineWidth());
                    Optional.ofNullable(linkCategory.getLinkCategoryLineType()).ifPresent(lineType->{
                        if (StringUtils.isNotBlank(lineType)) {
                            lineStyle.type(LineType.valueOf(lineType));
                        }
                    });
                    link.setLineStyle(lineStyle);
                    Label<Graph.Link> label = new Label<>();
                    label.show(linkCategory.getLinkCategoryShowLabel())
                            .color(linkCategory.getLinkCategoryLabelFontColor())
                            .fontSize(linkCategory.getLinkCategoryLabelFontSize())
                            .fontStyle(Optional.ofNullable(linkCategory.getLinkCategoryLabelFontItalic()).orElse(Boolean.FALSE) ? FontStyle.italic : FontStyle.normal)
                            .fontWeight(Optional.ofNullable(linkCategory.getLinkCategoryLabelFontBold()).orElse(Boolean.FALSE) ? FontWeightEnum.bold :  FontWeightEnum.normal);
                    link.setLabel(label);
                }
            });
        });
    }

    private static boolean isCreateLinkCategory(List<OptionExposeGraph.LinkCategory> linkCategories, List<JData.Link> links) {
        if (linkCategories == null) {
            return true;
        }
        return !linkCategories.stream().map(OptionExposeGraph.LinkCategory::getLinkCategoryName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .containsAll(links.stream().map(JData.Link::getCategory).collect(Collectors.toSet()));
    }

    private static List<OptionExposeGraph.LinkCategory> createLinkCategories(List<JData.Link> links) {
        Set<String> linkCategoryNames = new HashSet<>();
        for (JData.Link link : links) {
            linkCategoryNames.add(link.getCategory());
        }
        return linkCategoryNames.stream().map(categoryName -> {
            OptionExposeGraph.LinkCategory linkCategory = new OptionExposeGraph.LinkCategory();
            linkCategory.setLinkCategoryName(categoryName);
            linkCategory.setLinkCategoryShowLabel(false);
            linkCategory.setLinkCategoryLabelFontBold(false);
            linkCategory.setLinkCategoryLabelFontItalic(false);
            return linkCategory;
        }).collect(Collectors.toList());
    }

    private static void triggerDataCategory(Option option, OptionExposeGraph optionExpose, JData data) {
        List<OptionExposeGraph.DataCategory> dataCategoris = optionExpose.getDataCategories();
        if (isCreateDataCategories(data.getEntryList(), dataCategoris)) {
            dataCategoris = createDataCategories(data.getEntryList());
            optionExpose.setDataCategories(dataCategoris);
        }
        triggerDataCategoryToOptionData(option, optionExpose.getDataCategories());
    }

    private static boolean isCreateDataCategories(List<JData.Entry> entries, List<OptionExposeGraph.DataCategory> dataCategories) {
        if (dataCategories == null) {
            return true;
        }
        Set<String> categoryNames = new HashSet<>();
        for (JData.Entry entry : entries) {
            if (entry.getCategory() != null) {
                categoryNames.add(entry.getCategory());
            }
        }
        return !dataCategories.stream().map(OptionExposeGraph.DataCategory::getDataCategoryName)
                .collect(Collectors.toSet()).containsAll(categoryNames);

    }

    private static void triggerDataCategoryToOptionData(Option option, List<OptionExposeGraph.DataCategory> dataCategories) {
        Map<String, OptionExposeGraph.DataCategory> dataCategoryMap =
                dataCategories.stream().collect(Collectors.toMap(OptionExposeGraph.DataCategory::getDataCategoryName, Function.identity(), (k, v)->v));
        option.series().stream().findFirst().ifPresent(series -> {
            ((Graph) series).getData().stream().forEach(sData -> {
                OptionExposeGraph.DataCategory dataCategory = dataCategoryMap.get(((GraphData)sData).getCategory());
                if (dataCategory != null) {
                    Optional.ofNullable(dataCategory.getDataCategorySymbol()).ifPresent(symbol-> {
                        if (StringUtils.isNotBlank(symbol)) {
                            ((GraphData) sData).setSymbol(SymbolEnum.valueOf(symbol));
                        }
                    });
                    Optional.ofNullable(dataCategory.getDataCategorySymbolSize()).ifPresent(size-> {
                        if (size != 0) {
                            ((GraphData) sData).setSymbolSize(size);
                        }
                    });
                    ItemStyle<GraphData, ItemStyle> itemStyle = new ItemStyle<>();
                    itemStyle.color(dataCategory.getDataCategoryColor());
                    ((GraphData) sData).setItemStyle(itemStyle);
                    Label<GraphData> label = new Label<>();
                    label.show(dataCategory.getDataCategoryShowLabel())
                            .color(dataCategory.getDataCategoryLabelFontColor())
                            .fontStyle(!Optional.ofNullable(dataCategory.getDataCategoryLabelFontItalic()).orElse(Boolean.FALSE) ? FontStyle.normal : FontStyle.italic)
                            .fontWeight(!Optional.ofNullable(dataCategory.getDataCategoryLabelFontBold()).orElse(Boolean.FALSE) ? FontWeightEnum.normal : FontWeightEnum.bold)
                            .fontSize(dataCategory.getDataCategoryLabelFontSize());
                    ((GraphData) sData).setLabel(label);
                }
            });
        });
    }

    private static Category[] createCategories(List<OptionExposeGraph.DataCategory> dataCategories) {
        List<Category> categories = new ArrayList<>(dataCategories.size());
        for (OptionExposeGraph.DataCategory dataCategory : dataCategories) {
            Category category = new Category();
            category.setName(dataCategory.getDataCategoryName());
            Optional.ofNullable(dataCategory.getDataCategorySymbol())
                    .ifPresent(symbol->category.setSymbol(SymbolEnum.valueOf(symbol)));
            category.setSymbolSize(dataCategory.getDataCategorySymbolSize());
            Label<Category> label = new Label<>();
            label.show(dataCategory.getDataCategoryShowLabel())
                    .color(dataCategory.getDataCategoryLabelFontColor())
                    .fontStyle(!Optional.ofNullable(dataCategory.getDataCategoryLabelFontItalic()).orElse(Boolean.FALSE) ? FontStyle.normal : FontStyle.italic)
                    .fontWeight(!Optional.ofNullable(dataCategory.getDataCategoryLabelFontBold()).orElse(Boolean.FALSE) ? FontWeightEnum.normal : FontWeightEnum.bold)
                    .fontSize(dataCategory.getDataCategoryLabelFontSize());
            category.setLabel(label);
            ItemStyle<Category, ItemStyle> itemStyle = new ItemStyle<>();
            itemStyle.color(dataCategory.getDataCategoryColor());
            category.setItemStyle(itemStyle);
            if (category.getName().equals("综合")){
                category.setSymbol(SymbolEnum.pin);
            }
            categories.add(category);
        }
        return categories.toArray(new Category[0]);
    }

    private static List<OptionExposeGraph.DataCategory> createDataCategories(List<JData.Entry> entries) {
        Set<String> categoryNames = new HashSet<>();
        for (JData.Entry entry : entries) {
            categoryNames.add(entry.getCategory());
        }
        List<OptionExposeGraph.DataCategory> categories = new ArrayList<>(categoryNames.size());
        for (String categoryName : categoryNames) {
            if (StringUtils.isBlank(categoryName)) {
                continue;
            }
            OptionExposeGraph.DataCategory category = new OptionExposeGraph.DataCategory();
            category.setDataCategoryName(categoryName);
            category.setDataCategoryShowLabel(true);
            categories.add(category);
        }
        return categories;
    }

    private static void triggerExposeTooltip(Option option, OptionExposeGraph optionExpose) {
        Tooltip tooltip = new Tooltip().parent(option);
        tooltip.show(Optional.ofNullable(optionExpose.getShowTooltip()).orElse(Boolean.FALSE))
                .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                .confine(optionExpose.getTooltipConfine())
                .formatter(optionExpose.getTooltipFormatter())
                .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                .borderWidth(optionExpose.getTooltipBorderWidth())
                .borderColor(optionExpose.getTooltipBorderColor());
        option.tooltip(tooltip);
    }

    private static void triggerExposeTitle(Option option, OptionExposeTitle exposeTitle, String title) {
        if (exposeTitle != null) {
            WrapConvert.exposeTitle(option, exposeTitle, title);
        } else {
            option.title().text(title).show(false);
        }
    }
}
