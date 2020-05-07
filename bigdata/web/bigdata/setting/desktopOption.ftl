<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<form id="submitForm">
    <div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>平台名称：</label>
			<div class="col-sm-6">
				<input type="text" name="platformName" id="name" class="width-1of1 form-control" nullable="false" maxLength="36" value="${desktopOption.platform_name!}">
			</div>
		</div>
          <div class="form-group">
            <@upload.picUpload businessKey="desktoplogo" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" size="1" fileNumLimit="1" handler="loadPhotoFile">
                <label class="col-sm-2 control-label no-padding-right">Logo：</label>
                <div class="col-sm-4">
                    <div class="input-group">
                        <div class="filter-item block">
                            <#if !desktopOption.logo?? >
                            <a href="javascript:void(0);" class="form-file pull-left js-pic js-addPhotos">
                                <i class="fa fa-plus"></i>
                            </a>
                            </#if>
                            <#if desktopOption.logo?? >
                            <a href="javascript:void(0);"
                               class="form-file pull-left js-pic no-padding js-addPhotos">
                                <img width="130px;" height="132px;" src="${systemFilePath!}${desktopOption.logo!}">
                            </a>
                            </#if>
                        </div>
                    </div>
                </div>
                <!--这里的id就是存放附件的文件夹地址 必须维护-->
                <input type="hidden" id="desktoplogo-path" name="logoPath" value="">
                <input type="hidden" id="desktoplogo-relativePath" value="">
                <input type="hidden" id="desktoplogoFileName" name="logoFileName" value="">
            </@upload.picUpload>
        </div>

		<div class="form-group">
	        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
	        <div class="col-sm-6" >
	            <button type="button" class="btn btn-long btn-blue js-added" id="optionSaveBtn">&nbsp;保存&nbsp;</button>
	        </div>
	    </div>
</div>
</form>
<script>
	var isSubmit=false;

	$("#optionSaveBtn").on("click",function(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
	
		var options = {
				url : "${request.contextPath}/bigdata/setting/option/saveDesktopParam",
				dataType : 'json',
				success : function(data){
			 		if(!data.success){
			 			showLayerTips4Confirm('error',data.message);
			 			isSubmit = false;
			 		}else{
			 			showLayerTips('success',data.message,'t');
			 			isSubmit = false;
	    			}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#submitForm").ajaxSubmit(options);
	});

	function loadPhotoFile() {
	    if (hasUploadSuc) {
	    	var path=$("#desktoplogo-relativePath").val();
	    	var fileName=$("#desktoplogoFileName").val();
	        var fullPath="${systemFilePath!}"+path+ "//"+fileName;
	        $(".js-pic").addClass('no-padding');
	        $(".js-pic").empty().append('<img src="' +  fullPath + '"/>');
	        $(".js-pic").append('<input type="hidden" id="logo" name="logo" value="' + fileName + '">');
	    }
	}

</script>