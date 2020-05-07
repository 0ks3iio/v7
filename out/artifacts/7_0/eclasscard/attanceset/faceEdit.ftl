<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<table class="table table-bordered table-striped no-margin">
<thead>
    <tr>
        <th colspan="2" class="text-center">人脸信息</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="text-right"><span style="color:red;"></span>学生:</td>
        <td>
			<input type="hidden" id="studentId" name="studentId" value="${studentId!}"/>
			${studentName!}
        </td>
    </tr>
    <tr>
        <td rowspan="4" class="text-right">照片:</td>
        <td rowspan="4">
            <div class="form-horizontal">
                <div class="col-sm-10">
                    <div class="upload-img-container clearfix">
                        <div class="upload-img-item file-img-wrap  upload-img-new <#if !hasPic?default(false)>open<#else>hidden</#if> ">
                            <label class="upload-img" for="filePicker2"><span></span></label>
                        </div>
                        <div class="upload-img-item uploader-list <#if hasPic?default(false)>open<#else>hidden</#if> " style="width:130px;height:130px;">
                            <div id="file_id_face" class="file-item" style="width:130px;">
                                <div class="file-img-wrap">
                                <img width="130px;" height="130px;" src="${request.contextPath}${picUrl!}" alt="" layer-index="0">
                                </div>
                            </div>
                        </div>
                    </div>
                    <@upload.picUpload businessKey="${photoDirId!}" extensions="jpg,jpeg,png" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="1" fileNumLimit="1" handler="faceImagChange">
						<a href="javascript:;" class="btn btn-blue js-addPhotos"><#if hasPic?default(false)>重新上传<#else>选择照片</#if></a>
						<!--这里的id就是存放附件的文件夹地址 必须维护-->
						<input type="hidden" id="${photoDirId!}-path" value="">
					</@upload.picUpload>
                </div>

            </div>
        </td>
    </tr>
      </tbody>
</table>
<div class="base-bg-gray text-center">
    <a class="btn btn-blue" onclick="saveFaceInfo();" <#if hasPic?default(false)> style="display:none"</#if> href="#">保存</a>
    <a class="btn btn-white" onclick="changeSelect();"   href="#">返回</a>
</div>
<script>

</script>