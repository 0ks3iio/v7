package net.zdsoft.api.openapi.remote.openapi.utils;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * @author shenke
 */
public class VerifyCoder {
    private static final Logger LOG = LoggerFactory.getLogger(VerifyCoder.class);
    private Integer height;
    private Integer width;

    private Font font;
    private Color fontColor = Color.PINK;
    private Color bgColor = Color.LIGHT_GRAY;

    private Integer randomPointNum = 30;

    public VerifyCoder(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    public VerifyCoder(Integer height, Integer width, Color fontColor, Color bgColor) {
        this(height,width);
        this.fontColor = fontColor;
        this.bgColor = bgColor;
    }

    public VerifyCoder(Integer height, Integer width, Font font, Color fontColor, Color bgColor) {
        this(height,width,fontColor,bgColor);
        this.font = font;
    }

    public void export(String code, OutputStream out){
        Assert.hasLength(code);
        Assert.notNull(out);
        try {
            BufferedImage verifyImage = createImage(code);
            ImageIO.write(verifyImage, "jpeg",out);
            out.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private BufferedImage createImage(String code){
        BufferedImage verifyImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics = verifyImage.getGraphics();
        graphics.setColor(bgColor);
        graphics.fillRect(0,0,width,height);

        graphics.setColor(fontColor);

        int maxFontSize = font.getSize();
        for (int i = 0; i < code.length(); i++) {
            Font newFont = new Font("", RandomUtils.nextInt(0, 5), maxFontSize - RandomUtils.nextInt(0, 8));
            graphics.setFont(newFont);
            graphics.drawString(String.valueOf(code.charAt(i)),
                    10 + (i * (maxFontSize / 2)) + (i > 0 ? RandomUtils.nextInt(0, 5) : 0),
                    16 + RandomUtils.nextInt(0, 6));
            graphics.setColor(getRandomColor());
        }

        //干扰线
        for (int i = 0; i < randomPointNum; i++) {
            graphics.setColor(getRandomColor());
            int randomX = RandomUtils.nextInt(0, width);
            int randomY = RandomUtils.nextInt(0, height);
            graphics.drawLine(randomX, randomY, randomX, randomY);
        }
        return verifyImage;
    }

    private Color getRandomColor(){
        return new Color(RandomUtils.nextInt(0,256),RandomUtils.nextInt(0,256),RandomUtils.nextInt(0,256));
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setFont(int size){
        this.font = new Font("",0,size);
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setRandomPointNum(Integer randomPointNum) {
        this.randomPointNum = randomPointNum;
    }
}
