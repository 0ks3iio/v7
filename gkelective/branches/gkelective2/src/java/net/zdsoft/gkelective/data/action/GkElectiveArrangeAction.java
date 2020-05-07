package net.zdsoft.gkelective.data.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.GkSubjectArrangeDto;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;

@Controller
@RequestMapping("/gkelective")
public class GkElectiveArrangeAction extends BaseAction{
	
	//验证码
    public static final String VERIFY_CODE = "0123456789abcdefghijklmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ";
	@Autowired
	private GradeRemoteService gradeService;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	
	@RequestMapping("/arrange/index/page")
    @ControllerInfo(value = "7选3index")
    public String showIndex(ModelMap map, HttpSession httpSession) {
		return "/gkelective2/arrange/arrangeIndex.ftl";
	}
	
	@RequestMapping("/arrange/list/page")
    @ControllerInfo(value = "显示list")
	public String showList(ModelMap map){
		LoginInfo info = getLoginInfo();
		List<GkSubjectArrangeDto> dtos = gkSubjectArrangeService.findByUnitIdIsUsing(info.getUnitId(),null);
		map.put("dtos", dtos);
		return "/gkelective2/arrange/arrangeList.ftl";
	}
	
	@RequestMapping("/arrange/show/delete/page")
    @ControllerInfo(value = "新增排课")
	public String showDelete(HttpServletRequest request,ModelMap map){
		String deleteId = request.getParameter("deleteId");
		map.put("deleteId", deleteId);
		return "/gkelective2/arrange/deleteArrange.ftl";
	}
	
	@RequestMapping("/arrange/edit/page")
    @ControllerInfo(value = "新增排课")
	public String addArrange(ModelMap map){
		//年级
		List<Grade> gradeList = SUtils.dt(gradeService.findBySchoolId(getLoginInfo().getUnitId(),new Integer[]{BaseConstants.SECTION_HIGH_SCHOOL}),new TR<List<Grade>>(){});
		map.put("gradeList", gradeList);
		return "/gkelective2/arrange/arrangeEdit.ftl";
	}
	@ResponseBody
	@RequestMapping("/arrange/save")
    @ControllerInfo(value = "保存")
	public String saveArrange(GkSubjectArrange gkSubjectArrange,ModelMap map){
		try {
			LoginInfo info = getLoginInfo();
			if(gkSubjectArrange==null){
				return error("保存失败！");
			}
			//根据学年学期年级是不是已经存在
			GkSubjectArrangeDto dto = gkSubjectArrangeService.findByGradeId(gkSubjectArrange.getGradeId());
			if(dto.getGsaEnt()!=null){
				return error("该年级已经有对应选课项目！");
			}
			if(StringUtils.isBlank(gkSubjectArrange.getId())){
				gkSubjectArrange.setSubjectNum(3);//默认选3门
			}
			gkSubjectArrange.setUnitId(info.getUnitId());
			gkSubjectArrange.setIsUsing(GkElectveConstants.USE_FALSE);
			gkSubjectArrange.setIsLock(GkElectveConstants.USE_FALSE);
			gkSubjectArrange.setIsDeleted(GkElectveConstants.USE_FALSE);
			dto.setGsaEnt(gkSubjectArrange);
			gkSubjectArrangeService.saveDto(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败！", e.getMessage());
		}

		return success("新增成功！");
	}
	
	@ResponseBody
	@RequestMapping("/arrange/delete")
	@ControllerInfo("删除")
	public String doDelete(String id,String verifyCode,String examId, ModelMap map, HttpSession httpSession) {
		try{
            String verifyCodeKey = "xgk_verify_code_key" + httpSession.getId();
            
            String sessionVerifyCode = StringUtils.trim(RedisUtils.get(verifyCodeKey));
            if (StringUtils.isBlank(sessionVerifyCode)) {
                return error("验证码已失效");
            }
            if (StringUtils.equalsIgnoreCase(sessionVerifyCode, verifyCode)) {
                RedisUtils.del(verifyCodeKey);
            } else {
                return error("验证码错误");
            }
			gkSubjectArrangeService.deletedById(id);
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
	@ResponseBody
    @RequestMapping("/verifyImage")
    @ControllerInfo(ignoreLog=1,value="获取验证码")
    public String execute(@RequestParam(value = "codeLength",required = false,defaultValue = "4") Integer codeLength, HttpServletResponse response){
        try {
            String code = getCode(codeLength);
            RedisUtils.del("xgk_verify_code_key" + getSession().getId());
            RedisUtils.set("xgk_verify_code_key" + getSession().getId(),code,1*60);
            VerifyImage verifyImage = new VerifyImage(32, 86);
            verifyImage.setFont(25);
            verifyImage.setBgColor(Color.WHITE);
            OutputStream outputStream = response.getOutputStream();
            verifyImage.export(code,outputStream);
            outputStream.flush();
        }catch (Exception e){
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
	            code.append(VERIFY_CODE.charAt(RandomUtils.nextInt(0,VERIFY_CODE.length()-1)));
	        }

	        return code.toString();
	    }
	  
	  class VerifyImage {
//	        private static final Logger LOG = LoggerFactory.getLogger(VerifyImage.class);
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
	            	e.printStackTrace();
//	                LOG.error("无法创建验证码",e);

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
}
