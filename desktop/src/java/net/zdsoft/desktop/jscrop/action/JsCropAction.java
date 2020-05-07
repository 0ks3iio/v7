package net.zdsoft.desktop.jscrop.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.JsCropUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;

import net.zdsoft.desktop.jscrop.define.JsCropConstant;
import net.zdsoft.desktop.jscrop.define.JsCropFile;
import net.zdsoft.desktop.jscrop.define.JsCropFileType;
import net.zdsoft.desktop.jscrop.define.JsCropState;
import net.zdsoft.desktop.jscrop.upload.JsCropUploader;
import net.zdsoft.desktop.jscrop.utils.JsCropUploaderUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.LoginInfo;

/**
 * Created by shenke on 2016/12/16.
 */
@Controller
@RequestMapping("/zdsoft/crop")
public class JsCropAction extends BaseAction{

    @Autowired private UserRemoteService userRemoteService;

    @RequestMapping("/doPage")
    public String execute(){

        return "/static/jscrop/page/jscrop.ftl";
    }

    @RequestMapping("/doUpload")
    @ResponseBody
    public String upload(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> json = Maps.newHashMap();
        try {

            JsCropUploader jsCropUploader = new JsCropUploader(getLoginInfo().getUserId(),getLoginInfo().getUnitId());
            JsCropState jsCropState = jsCropUploader.pretreatment(request);

            if(jsCropState.isSuccess()){
                json.put("key",getLoginInfo().getUserId());
                json.put("success",Boolean.TRUE.toString());
            }else{
                json.put("success",Boolean.FALSE.toString());
                json.put("msg",jsCropState.getDetailError());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONUtils.toJSONString(json);
    }

    @RequestMapping("/doPreview")
    @ResponseBody
    public String doPretreatment(HttpServletResponse response,HttpServletRequest request){
        try {
            JsCropFile jsCropFile = JsCropUploaderUtils.getPreviewFile(getLoginInfo().getUserId(),getLoginInfo().getUnitId(),null);
            if(jsCropFile != null){
                jsCropFile.getFile().delete();
                int descWidth = JsCropConstant.DEFAULT_DESC_WIDTH;//NumberUtils.toInt(request.getParameter("originZoomWidth").toString(), JsCropConstant.DEFAULT_DESC_WIDTH);
                int descHeight = JsCropConstant.DEFAULT_DESC_HEIGHT;//NumberUtils.toInt(request.getParameter("originZoomHeight").toString(), JsCropConstant.DEFAULT_DESC_HEIGHT);
                BufferedImage zoomImage = JsCropUtils.zoom(jsCropFile.getBytes(),descWidth,descHeight);
                OutputStream out = response.getOutputStream();//.write(JsCropUtils.toBytes(zoomImage));
                ImageIO.write(zoomImage,"png",out);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/doSave")
    @ResponseBody
    public String doSave(HttpServletResponse response, HttpServletRequest request){
        String msg = null;
        try {
            JsCropUploader jsCropUploader = new JsCropUploader(getLoginInfo().getUserId(),getLoginInfo().getUnitId());
            JsCropState jsCropState = jsCropUploader.save(request);

            if (jsCropState.isSuccess()) {
                User user = new User();
                user.setId(getLoginInfo().getUserId());
                user.setAvatarUrl(File.separator + jsCropState.getBigFilePath());
                userRemoteService.update(user,user.getId(),new String[]{"avatarUrl"});
            }


            return jsCropState.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            msg = "保存头像失败" + e.getMessage();
        }
        return new ResultDto().setMsg(msg).setSuccess(false).toJSONString();
    }

    @RequestMapping("/doPortrait")
    @ResponseBody
    public String doPortrait(String type, HttpServletResponse response, @RequestParam(name = "userName", required = false) String userName){
        try{
            String userId = null;
            String unitId = null;
            Integer sex = null;
            if ( StringUtils.isNotBlank(userName) ) {
                User user = SUtils.dc(userRemoteService.findByUsername(userName),User.class);
                if ( user != null) {
                    userId = user.getId();
                    unitId = user.getUnitId();
                    sex = user.getSex();
                }
            }
            LoginInfo login = getLoginInfo();
            if(login==null){
            	login = new LoginInfo();
            }
            userId = userId == null? login.getUserId() : userId;
            unitId = unitId == null ? login.getUnitId() : unitId;
            User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
            if(user != null)
            	sex = sex == null ? user.getSex() : sex;

            JsCropFileType jsCropFileType = getType(type);

            JsCropFile jsCropFile = JsCropUploaderUtils.getJsCropFile(userId,unitId,jsCropFileType, sex);
            if(jsCropFile != null){
                response.getOutputStream().write(jsCropFile.getBytes());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private JsCropFileType getType(String type){
        if(StringUtils.equalsIgnoreCase("big",type)){
            return JsCropFileType.BIG;
        }
        return JsCropFileType.SMALLER;
    }
}
