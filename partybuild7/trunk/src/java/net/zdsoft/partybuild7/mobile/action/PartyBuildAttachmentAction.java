package net.zdsoft.partybuild7.mobile.action;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.partybuild7.data.entity.PartyBuildAttachment;
import net.zdsoft.partybuild7.data.service.PartyBuildAttachmentService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mobile/open/attachment")
public class PartyBuildAttachmentAction extends MobileAction {
    @Autowired
    private PartyBuildAttachmentService partyBuildAttachmentService;
    @RequestMapping("/downloadAttachment")
    @ControllerInfo("下载附件")
    public void downloadAttachment(String attachmentId , HttpServletRequest request , HttpServletResponse response){

        if (StringUtils.isBlank(attachmentId)){
            return;
        }
        PartyBuildAttachment attachment = partyBuildAttachmentService.getAttachmentById(attachmentId);


        File file = new File(attachment.getFilePath() + File.separator + attachment.getFileName());

        if(file.exists()){
//            response.setContentType("multipart/form-data");
            try {
                response.setContentType(Files.probeContentType(Paths.get(file.toURI())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String fileName = URLEncoder.encode(attachment.getFileName(), StandardCharsets.UTF_8.toString());
//                response.setHeader("Content-Disposition" ,"attachment;fileName=" + URLEncoder.encode(attachment.getFileName(), "UTF-8"));
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bufferedInputStream = null;
            OutputStream outputStream = null;
            try{
                fis = new FileInputStream(file);

                bufferedInputStream = new BufferedInputStream(fis);
                 outputStream = response.getOutputStream();
                int i= bufferedInputStream.read(buffer);
                while( i != -1){
                    outputStream.write(buffer , 0,i);
                    i = bufferedInputStream.read(buffer);
                }


            }catch(Exception e){
                e.printStackTrace();
            }finally{

                try {
                    if(bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if(fis != null){
                        fis.close();
                    }
                    if(outputStream != null){
                        outputStream.close();
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }




        }


    }
}
