/**
 * FileName: ChartClassificationUtils.java
 * Author:   shenke
 * Date:     2018/5/28 下午1:19
 * Descriptor:
 */
package net.zdsoft.bigdata.data.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/5/28 下午1:19
 */
public class ChartClassificationUtils {

    public static List<ChartClassification> getEnableClassifications(final String currentUrl) {
        ChartSeries[] series = ChartSeries.values();


        ChartCategory[] categories = ChartCategory.values();
        Map<ChartSeries, List<ChartCategory>> chartSeriesListMap = new HashMap<>(series.length);
        for (ChartCategory category : categories) {
            List<ChartCategory> c2 = chartSeriesListMap.getOrDefault(category.getChartSeries(), new ArrayList<>());
            c2.add(category);
            chartSeriesListMap.put(category.getChartSeries(), c2);
        }

        List<ChartClassification> classifications = new ArrayList<>(series.length);
        for (ChartSeries s : series) {
            List<ChartCategory> categoryList = chartSeriesListMap.get(s);
            if (categoryList.isEmpty()) {
                continue;
            }
            //暂时屏蔽其他类型
            if (ChartSeries.other.equals(s)) {
                continue;
            }
            ChartClassification chartClassification = new ChartClassification();
            chartClassification.setName(s.getName());
            chartClassification.setOrder(s.getOrder());
            chartClassification.setChartCategoryClassifications(
                    categoryList.stream().map(chartCategory -> {
                        ChartClassification.ChartCategoryClassification categoryClassification
                                = new ChartClassification.ChartCategoryClassification();
                        categoryClassification.setName(chartCategory.getName());
                        categoryClassification.setOrder(chartCategory.getOrder());
                        categoryClassification.setThumbnail(currentUrl +
                                "/static/bigdata/images/chart-" + chartCategory.getChartType() + ".png");
                        categoryClassification.setChartType(chartCategory.getChartType());
                        categoryClassification.setSeriesName(s.name());
                        return categoryClassification;
                    }).sorted(Comparator.comparing(Classification::getOrder)).collect(Collectors.toList())
            );
            chartClassification.setSeriesName(s.name());
            classifications.add(chartClassification);
        }

        classifications.sort(Comparator.comparing(ChartClassification::getOrder));
        return classifications;
    }

    private static Set<Integer> other_type ;
    private static final Object lock = new Object();

    public static Set<Integer> getOtherChartType() {
        if (other_type == null) {
            synchronized (lock) {
                other_type = Arrays.stream(ChartCategory.values())
                        .filter(chartCategory -> ChartSeries.other.equals(chartCategory.getChartSeries()))
                        .map(ChartCategory::getChartType).collect(Collectors.toSet());
            }
        }
        return other_type;
    }
}
