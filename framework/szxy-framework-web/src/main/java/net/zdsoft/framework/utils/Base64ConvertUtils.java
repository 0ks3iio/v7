package net.zdsoft.framework.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * base64 转换成图片的工具类
 * @author shenke
 * @since 2018/10/18 9:35
 */
public class Base64ConvertUtils {

    private static Pattern pattern = Pattern.compile("data:image/[a-z]+;base64");

    /**
     * 转换并保存到指定路径
     * @param base64String base64字符串 data:image/jpeg;base64
     * @param filePath 包含文件名的路径地址
     * @param formatName 图片后缀
     * @throws IOException
     */
    public static void convert2Path(String base64String, String filePath, String formatName) throws IOException {
        Assert.hasLength(base64String);
        Base64 base64 = new Base64();

        if (!pattern.matcher(base64String).find()) {
            throw new IllegalArgumentException("not support base64String");
        }
        byte[] imageBytes = base64.decode(base64String.replaceFirst("data:image/[a-z]+;base64", ""));
        InputStream in = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(in);
        ImageIO.write(image, formatName, new File(filePath));
    }
}
