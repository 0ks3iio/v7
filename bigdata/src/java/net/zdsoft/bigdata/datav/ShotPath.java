package net.zdsoft.bigdata.datav;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author shenke
 * @since 2018/10/25 下午3:08
 */
final public class ShotPath {

    public static String of(String filePath, String unitId, String screenId) {
        String path = filePath + File.separator + "bigdata" + File.separator + "datav"
                + File.separator + unitId;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return  path + File.separator + screenId + ".jpg";
    }

    public static void copy(String filePath, String originUnitId, String originScreenId, String newScreenId) throws IOException {
        Files.copy(Paths.get(of(filePath, originUnitId, originScreenId)), Paths.get(of(filePath, originUnitId, newScreenId)));
    }
}
