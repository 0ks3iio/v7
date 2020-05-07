<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<@upload.picUpload businessKey="${key!}" contextPath="${request.contextPath}" size="1" fileNumLimit="${limitNum}" tips="${tipInfo!}" extensions="jpg,jpeg,png" resourceUrl="${resourceUrl}" handler="bacthUploadFace">
	<button class="btn btn-blue js-addPhotos hidden">上传图片</button>
</@upload.picUpload>
<script>
$(function(){
	$(".js-addPhotos").click();
})

var isSubmit = false;
function saveFaceInfo(arrayStr) {
	if(isSubmit){
        return;
    }
    var ownerId = "${ownerId}";
    
    isSubmit = true;
	var options = {
		url : "${request.contextPath}/eclasscard/faceinfo/save?ownerId="+ownerId,
		contentType: "application/json;charset=utf-8",
		data:arrayStr,
		dataType : 'json',
		success : function(data){
	 		if(!data.success){
	 			layerTipMsg(data.success,"保存失败",data.msg);
	 			isSubmit = false;
	 		}else{
				layer.msg("保存成功");
				arrayStr = '';
				changeSelect();
			}
			isSubmit = false;
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){
			isSubmit = false;
		}//请求出错 
	};
	$.ajax(options);
}
function faceImagChange() {
	$.ajax({
		url:"${request.contextPath}/webuploader/dirfiles",
		data:{"path":"${photoPath!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
			var array = data;
			if(array.length > 0){
				arrayStr = JSON.stringify(array);
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
		}
	});
}
function bacthUploadFace(){
	$.ajax({
		url:"${request.contextPath}/webuploader/dirfiles",
		data:{"path":"${photoPath!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
			var array = data;
			if(array.length > 0){
				var jsonStr = JSON.stringify(array);
				<#if type == '1'>
				bacthSaveUploadFace(jsonStr);
				$("#show-uploading").removeClass('hide')
				<#else>
				saveFaceInfo(jsonStr);
				</#if>
				layer.msg("照片验证中，请稍等...");
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
		}
	});
}

function showUploadOver(resultVal){
	if(resultVal && resultVal!=''){
	    var val = JSON.parse(resultVal);
		$("#showUploadOver").html('本次批量上传成功：'+val.susNum+'，<span class="color-red">学号未匹配：'+val.wppNum+'，</span><span class="color-red">验证失败：'+val.failNum+'。</span>');
	}
}

function bacthSaveUploadFace(jsonStr){
	if(isSubmit){
		$("#show-uploading").addClass('hide');
        return;
    }
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/face/bacth/upload/save",
			contentType: "application/json;charset=utf-8",
			data:jsonStr,
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
		 			var resultVal = data.businessValue;
		 			changeSelect();
		 			showUploadOver(resultVal);
		 			layer.msg("上传完成");
    			}
    			isSubmit = false;
    			$("#show-uploading").addClass('hide');
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
</script>