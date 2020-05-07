package net.zdsoft.remote.openapi.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

@Controller
@RequestMapping(value = { "/remote/openapi/office" })
public class OpenApiOfficeDocAction extends BaseAction {

    private static OpenApiOfficeService openApiOfficeService;

    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
        }
        return openApiOfficeService;
    }

    /**
     * 手写批注--上传
     * 
     * @param remoteParam
     * @param request
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/remoteFileUpload")
    public String remoteFileUpload(String remoteParam, HttpServletRequest request) throws Exception {
        try {
            MultipartFile file = StorageFileUtils.getFile(request);
            InputStream stream = file.getInputStream();
            byte[] bytes = this.toByteArray(stream);

            return getOpenApiOfficeService().remoteFileUpload(remoteParam, bytes);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("程序异常")));
    }

    /**
     * 工作界面待处理数接口
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/weikeOfficeCount")
    public String weikeOfficeCount(String remoteParam) {
        return getOpenApiOfficeService().weikeOfficeCount(remoteParam);
    }

    /**
     * 应用权限接口
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/officeAuth")
    public String officeAuth(String remoteParam) {
        return getOpenApiOfficeService().officeAuth(remoteParam);
    }

    /**
     * 公文待处理数
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteOfficedocHaveDoNumber")
    public String remoteOfficedocHaveDoNumber(String remoteParam) {
    	return getOpenApiOfficeService().remoteOfficedocHaveDoNumber(remoteParam);
    }

    /**
     * 公文编辑--保存
     * 
     * @param remoteParam
     * @param request
     */
    @ResponseBody
    @RequestMapping("/remoteSaveAttachment")
    public String remoteSaveAttachment(String remoteParam, HttpServletRequest request) {
        try {
            MultipartFile file = StorageFileUtils.getFile(request);
            InputStream stream = file.getInputStream();
            byte[] bytes = this.toByteArray(stream);

            return getOpenApiOfficeService().remoteSaveAttachment(remoteParam, bytes);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("程序异常")));
    }

    /**
     * 发文督办阅办已处理列表
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteHaveDoOfficedocList")
    public String remoteHaveDoOfficedocList(String remoteParam) {
        return getOpenApiOfficeService().remoteHaveDoOfficedocList(remoteParam);
    }

    /**
     * 发文督办阅办待处理列表
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteOfficedocList")
    public String remoteOfficedocList(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteOfficedocList(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 发文督办阅办详情
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteOfficedoc")
    public String remoteOfficedoc(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteOfficedoc(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 批注处理--加签
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remotePassWithAddStepOfficedoc")
    public String remotePassWithAddStepOfficedoc(String remoteParam, HttpServletRequest request) {
        try {
             MultipartFile file = StorageFileUtils.getFile(request);
             if(file==null){
            	 return getOpenApiOfficeService().remotePassWithAddStepOfficedoc(remoteParam,null);
             }
             InputStream stream = file.getInputStream();
             byte[] bytes = toByteArray(stream);

            return getOpenApiOfficeService().remotePassWithAddStepOfficedoc(remoteParam,bytes);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 批注处理--退回
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteBackOfficedoc")
    public String remoteBackOfficedoc(String remoteParam, HttpServletRequest request) {
        try {
        	MultipartFile file = StorageFileUtils.getFile(request);
        	 if(file==null){
        		 return getOpenApiOfficeService().remoteBackOfficedoc(remoteParam,null);
        	 }
            InputStream stream = file.getInputStream();
            byte[] bytes = toByteArray(stream);
            return getOpenApiOfficeService().remoteBackOfficedoc(remoteParam,bytes);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 批注处理--通过
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remotePassOfficedoc")
    public String remotePassOfficedoc(String remoteParam, HttpServletRequest request) {
        try {
        	MultipartFile file = StorageFileUtils.getFile(request);
        	if(file==null){
        		return getOpenApiOfficeService().remotePassOfficedoc(remoteParam,null);
        	}
            InputStream stream = file.getInputStream();
            byte[] bytes = toByteArray(stream);
            return getOpenApiOfficeService().remotePassOfficedoc(remoteParam,bytes);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 签收(待签收、已签收)
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteRegisterOfficedocList")
    public String remoteRegisterOfficedocList(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteRegisterOfficedocList(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }
    /**
     * 进入登记编辑页面
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteToRegisterOfficedoc")
    public String remoteToRegisterOfficedoc(String remoteParam) {
    	try {
    		return getOpenApiOfficeService().remoteToRegisterOfficedoc(remoteParam);
    	}
    	catch (Exception ex) {
    		return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
    	}
    }
    /**
     * 登记开始流程
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteDoReFlowOfficedoc")
    public String remoteDoReFlowOfficedoc(String remoteParam) {
    	try {
    		return getOpenApiOfficeService().remoteDoReFlowOfficedoc(remoteParam);
    	}
    	catch (Exception ex) {
    		return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
    	}
    }
    /**
     * 登记跳过流程到未转发
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteDoRegisterOfficedoc")
    public String remoteDoRegisterOfficedoc(String remoteParam) {
    	try {
    		return getOpenApiOfficeService().remoteDoRegisterOfficedoc(remoteParam);
    	}
    	catch (Exception ex) {
    		return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
    	}
    }

    /**
     * 签收处理
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteSignOfficedoc")
    public String remoteSignOfficedoc(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteSignOfficedoc(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 签收详情
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteRegisterOfficedoc")
    public String remoteRegisterOfficedoc(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteRegisterOfficedoc(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 详情页主送列表
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteMainOfficedoc")
    public String remoteMainOfficedoc(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteMainOfficedoc(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 详情页抄送列表
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteCopyOfficedoc")
    public String remoteCopyOfficedoc(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteCopyOfficedoc(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }
    /**
     * 详情页传阅列表
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteReadOfficedoc")
    public String remoteReadOfficedoc(String remoteParam) {
    	try {
    		return getOpenApiOfficeService().remoteReadOfficedoc(remoteParam);
    	}
    	catch (Exception ex) {
    		return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
    	}
    }

    /**
     * 转发(未转发、已转发)
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteSendOfficedocList")
    public String remoteSendOfficedocList(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteSendOfficedocList(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 转发处理
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteOfficedocSend")
    public String remoteOfficedocSend(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteOfficedocSend(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }

    /**
     * 转发详情
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteSendOfficedoc")
    public String remoteSendOfficedoc(String remoteParam) {
        try {
            return getOpenApiOfficeService().remoteSendOfficedoc(remoteParam);
        }
        catch (Exception ex) {
            return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
        }
    }
    
    /**
     * 转发保存---主送、抄送人员
     * 
     * @param remoteParam
     */
    @ResponseBody
    @RequestMapping("/remoteSaveSendOfficedoc")
    public String remoteSaveSendOfficedoc(String remoteParam) {
    	try {
    		return getOpenApiOfficeService().remoteSaveSendOfficedoc(remoteParam);
    	}
    	catch (Exception ex) {
    		return RemoteCallUtils.encode(SUtils.s(RemoteCallUtils.convertError("调用远程接口失败")));
    	}
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
