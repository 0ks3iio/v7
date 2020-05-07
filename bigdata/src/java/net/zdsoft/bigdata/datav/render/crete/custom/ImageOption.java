package net.zdsoft.bigdata.datav.render.crete.custom;

import java.io.File;
import java.util.Comparator;

/**
 * @author shenke
 * @since 2018/10/31 上午11:23
 */
final public class ImageOption implements Comparable<ImageOption> {

    public static final String PATH = "bigdata" + File.separator + "diagram-image";

    private String imagePath;
    /**
     * for remove and update
     */
    private String index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int compareTo(ImageOption o) {
        if (o == null) {
            return -1;
        }
        return index.compareTo(o.getIndex());
    }
}
