<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<@upload.fileUpload businessKey="${accFileDitId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="30" fileNumLimit="${fileNum!}" handler="loadAttFiles">
	<a type="button" class="btn btn-white js-addFiles <#if canUse=="false"||fileNum=="0">disabled</#if>" href="javascript:;" >上传附件</a>
	<!--这里的id就是存放附件的文件夹地址 必须维护-->
	<input type="hidden" id="${accFileDitId!}-path" value="">
</@upload.fileUpload>