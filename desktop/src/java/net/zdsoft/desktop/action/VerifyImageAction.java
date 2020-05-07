package net.zdsoft.desktop.action;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.utils.CodeUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.RedisUtils;

/**
 * created by shenke 2017/2/25 9:13
 * 验证码，一分钟后过期
 */
@Controller
@RequestMapping("/desktop")
public class VerifyImageAction extends BaseAction {



    @ResponseBody
    @RequestMapping("/verifyImage")
    @ControllerInfo(ignoreLog=1,value="获取验证码")
    public String execute(@RequestParam(value = "codeLength",required = false,defaultValue = "4") Integer codeLength, HttpServletResponse response){
        try {
            String code = getCode(codeLength);
            RedisUtils.del(DeskTopConstant.VERIFY_CODE_CACHE_KEY + getSession().getId());
            RedisUtils.set(DeskTopConstant.VERIFY_CODE_CACHE_KEY + getSession().getId(),code,1*60);
            VerifyImage verifyImage = new VerifyImage(32, 86);
            verifyImage.setFont(25);
            verifyImage.setBgColor(Color.WHITE);
            OutputStream outputStream = response.getOutputStream();
            //byte[] bytes = verifyImage.export(code);
            //outputStream.write(bytes);
            //outputStream.flush();
            verifyImage.export(code,outputStream);
            outputStream.flush();
        }catch (Exception e){
            log.error("验证码生成失败",e);
        }
        return "";
    }
    
    @ResponseBody
    @RequestMapping("/verifyQuestionImage")
    @ControllerInfo(ignoreLog=1,value="获取问答式验证码")
    public String verifyQuestionImage(HttpServletResponse response,HttpServletRequest request){
    	try {
			CodeUtils.export(request, response, "",60*5, 150, 30);
		} catch (Exception e) {
			log.error("验证码生成失败",e);
		}
    	return "";
    }
    
    
    
    private String getCode(int length){
        if(length <= 0){
            throw new IllegalArgumentException("验证码长度非负");
        }

        StringBuilder code = new StringBuilder();

        for( int i=0; i<length; i++){
            code.append(DeskTopConstant.VERIFY_CODE.charAt(RandomUtils.nextInt(0,DeskTopConstant.VERIFY_CODE.length()-1)));
        }

        return code.toString();
    }

    public static void main(String[] args){
        try {
            VerifyImage verifyImage = new VerifyImage(20, 100);
            verifyImage.setFont(20);
            //verifyImage.export("1sd4", new FileOutputStream(new File("D:\\a.jpg")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
    class VerifyImage {
        private static final Logger LOG = LoggerFactory.getLogger(VerifyImage.class);
        private Integer height;
        private Integer width;

        private Font font;
        private Color fontColor = Color.PINK;
        private Color bgColor = Color.LIGHT_GRAY;

        private Integer randomPointNum = 30;

        public VerifyImage(Integer height, Integer width) {
            this.height = height;
            this.width = width;
        }

        public VerifyImage(Integer height, Integer width, Color fontColor, Color bgColor) {
            this(height,width);
            this.fontColor = fontColor;
            this.bgColor = bgColor;
        }

        public VerifyImage(Integer height, Integer width, Font font, Color fontColor, Color bgColor) {
            this(height,width,fontColor,bgColor);
            this.font = font;
        }

        //优化GC
        @Deprecated
        public byte[] export(String code){
            Assert.hasLength(code);
            try {
                BufferedImage verifyImage = createImage(code);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //去掉失效的JPEGCodec，用IMageIO
                ImageIO.write(verifyImage,"png",byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }catch (Exception e){
                LOG.error("无法创建验证码",e);

            }

            return null;
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
