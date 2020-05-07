package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.echarts.enu.Position;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.PositionEx;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.regex.Pattern;

/**
 * @author shenke
 * @since 2018/7/25 下午2:44
 */
public class OptionExposeUtils {

    private static Pattern regex_hex = Pattern.compile("^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$");

    /**
     * 将十六进制的颜色转换成RGB格式
     * 当参数不合法时返回null
     * @param hex         16进制颜色
     * @param transparent 透明度 1-10
     * @return RGB 格式的字符串
     */
    public static String toRGB(String hex, Integer transparent) {

        if (StringUtils.isBlank(hex) || transparent == null || transparent > 10 || !regex_hex.matcher(hex).matches()) {
            return null;
        }

        if (hex.length() == 4) {
            String sColorNew = "#";
            for (int i = 1; i < 4; i += 1) {
                sColorNew += hex.substring(i, i + 1).concat(hex.substring(i, i + 1));
            }
            hex = sColorNew;
        }
        //处理六位的颜色值
        List<Integer> sColorChange = new ArrayList<>(3);
        for (int i = 1; i < 7; i += 2) {
            sColorChange.add(Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return "rgb(" + StringUtils.join(sColorChange, ',') + "," + transparent / 10d + ")";
    }

    public static Position toPosition(String position) {
        try {
            return PositionEnum.valueOf(position);
        } catch (IllegalArgumentException e) {
            return PositionEx.create(position);
        }
    }

    public static String[] toCenter(String center) {
        return toCenter(center, String[]::new);
    }

    public static <T> T[] toCenter(String center, IntFunction<T[]> arrayFunction) {
        return Arrays.stream(center.substring(1, center.length()-1).split(",")).map(String::trim).toArray(arrayFunction);
    }

    public static Integer[] toIntArray(String radius) {
        return Arrays.stream(radius.substring(1, radius.length()-1).split(",")).map(Integer::valueOf).toArray(Integer[]::new);
    }

    public static Object[] toRadius(String radius) {
        return toCenter(radius, Object[]::new);
    }

    public static void main(String[] args) {
        System.out.println(toCenter("[5, 50%]")[0]);
        System.out.println(toCenter("[5, 50%]")[1]);
    }
}
