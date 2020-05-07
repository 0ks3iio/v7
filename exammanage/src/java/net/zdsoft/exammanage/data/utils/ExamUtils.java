package net.zdsoft.exammanage.data.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ExamUtils {

    public static String showPicUrl(String dirId, String filePath, Integer sex) {
        String defaultPath = "/static/jscrop/images/default-head.png";
		/* if (2 == sex) {
         }else {
        	 defaultPath = "/static/jscrop/images/portrait_big_male.png";
         }*/
        if (StringUtils.isNotBlank(filePath)) {
            try {
                filePath = URLEncoder.encode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            filePath = "";
        }
        if (dirId == null) {
            dirId = "";
        }
        String showPicUrl = "/common/showpicture?dirId=" + dirId + "&filePath="
                + filePath + "&defaultPath=" + defaultPath;
        return showPicUrl;
    }
}
