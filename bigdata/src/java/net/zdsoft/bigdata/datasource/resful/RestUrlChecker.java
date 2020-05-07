package net.zdsoft.bigdata.datasource.resful;

import java.util.regex.Pattern;

/**
 * @author shenke
 * @since 2018/11/27 上午10:01
 */
final class RestUrlChecker {

    static Pattern URL_PATTERN = Pattern.compile("^([hH][tT]{2}[pP]:\\/\\/|[hH][tT]{2}[pP][sS]:\\/\\/)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\\\/])+$");

    static String check(String url) {
        boolean match = URL_PATTERN.matcher(url).matches();
        if (!match) {
            throw new RestUrlErrorException(String.format("不合法的URL [%s]", url));
        }
        return url;
    }
}
