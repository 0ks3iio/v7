/**
 * FileName: SeriesTypeVO.java
 * Author:   shenke
 * Date:     2018/5/7 上午10:28
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;

/**
 * @author shenke
 * @since 2018/5/7 上午10:28
 */
public class SeriesTypeVO {

    private static Set<String> include = new HashSet<String>(6){
        {
            add(SeriesType.bar.category());
            add(SeriesType.line.category());
            add(SeriesType.pie.category());
            add(SeriesType.scatter.category());
        }
    };

    private String name;
    private Object value;

    //public static Set<SeriesTypeVO> seriesTypes ;

    //static {
    //    SeriesType[] types = SeriesType.values();
    //    seriesTypes = Arrays.stream(types).map(seriesType -> {
    //        SeriesTypeVO vo = new SeriesTypeVO();
    //        vo.name = seriesType.showName();
    //        vo.value = seriesType.category();
    //        return vo;
    //    }).filter(c->include.contains(c.value)).collect(Collectors.toSet());
    //}

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeriesTypeVO that = (SeriesTypeVO) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, value);
    }

    //public static Set<String> getIncludeChartCategory() {
    //    return include;
    //}
}
