package net.zdsoft.szxy.operation.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/7 下午5:53
 */
@Configuration
public class SzxyWebAutoconfigure implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }

    private static class DateConverter implements Converter<String, Date> {

        private static final String[] formarts = new String[4];

        static {
            formarts[0] = "yyyy-MM";
            formarts[1] = "yyyy-MM-dd";
            formarts[2] = "yyyy-MM-dd HH:mm";
            formarts[3] = "yyyy-MM-dd HH:mm:ss";
        }

        public Date convert(String source) {
            if (source == null) {
                return null;
            }
            String value = source.trim();
            if ("".equals(value)) {
                return null;
            }
            if (source.matches("^\\d{4}-\\d{1,2}$")) {
                return parseDate(source, formarts[0]);
            } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                return parseDate(source, formarts[1]);
            } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return parseDate(source, formarts[2]);
            } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return parseDate(source, formarts[3]);
            } else {
                throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
            }
        }

        /**
         * 功能描述：格式化日期
         *
         * @param dateStr String 字符型日期
         * @param format  String 格式
         * @return Date 日期
         */
        public Date parseDate(String dateStr, String format) {
            Date date = null;
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                date = (Date) dateFormat.parse(dateStr);
            } catch (Exception e) {
            }
            return date;
        }
    }
}
