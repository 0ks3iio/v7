<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div id="eccClassDescDiv">
<input type="hidden" id="pictrueUrl" name="pictrueUrl" value="${classDesc.picUrl!}">
<form id="eccClassDescForm">
<input type="hidden" id="eccClassDescId" name="id" value="${classDesc.id!}">
<div class="form-horizontal">
	<div class="form-group">
		<input type="hidden" id="photoPath" value="">
		<label class="col-sm-2 control-label no-padding-right">简介图片</label>
		<div class="col-sm-8" id="showPictureDiv" style="display:none" >
			<div class="form-group-imgUpload"><img id="eccDescPicture" style="width:272px;"src="${request.contextPath}/static/images/classCard/img-default.png" alt=""></div>
			<a class="btn btn-danger" href="javascript:;" onclick="deletePicture()">删除</a>
		</div>
		<div class="col-sm-8" id="upLoadPictureDiv">
			<div class="form-group-imgUpload"><img style="width:272px;"src="${request.contextPath}/static/images/classCard/img-default.png" alt=""></div>
			<@upload.picUpload businessKey="${photoDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="1" handler="loadPicFile">
			<a href="javascript:;" class="btn btn-blue js-addPhotos">上传图片</a>
			<!--这里的id就是存放附件的文件夹地址 必须维护-->
			<input type="hidden" id="${photoDirId!}-path" value="">
			</@upload.picUpload>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">简介内容</label>
		<div class="col-sm-8">
		<textarea id="content" name="content" type="text/plain" style="width:100%;height:360px;">${classDesc.content!}</textarea>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-8 col-sm-offset-2">
			<a id="clickBtn" href="javascript:;" class="btn btn-long btn-blue" onclick="saveClassDesc()">保存</a>
		</div>
	</div>
</div>
</form>
</div>
<script>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
UE.delEditor('content'); 
var ue = UE.getEditor('content',{
    //focus时自动清空初始化时的内容
    autoClearinitialContent:false,
    //关闭字数统计
    wordCount:false,
    //关闭elementPath
    elementPathEnabled:false,
    //默认的编辑区域高度
    toolbars:[[
         'fullscreen', 'source', '|', 'undo', 'redo', '|',
         'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
         'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
         'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
         'directionalityltr', 'directionalityrtl', 'indent', '|',
         'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
         'simpleupload','imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
         'horizontal', 'date', 'time', '|',
         'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts'
     	]],
    initialFrameHeight:300
    //更多其他参数，请参考ueditor.config.js中的配置项
});

$(function(){
	$("#eccClassDescDiv").css({
		height: $(window).height() - $("#eccClassDescDiv").offset().top - 60,
		overflowY: 'auto',
		overflowX: 'hidden'
	})
	<#if classDesc.picUrl?exists>
	showPicture("${classDesc.picUrl!}");
	</#if>
})
function loadPicFile(){
	loadDirFiles();
}
function loadDirFiles(){
	$.ajax({
		url:"${request.contextPath}/webuploader/dirfiles",
		data:{"path":"${photoPath!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
			var array = data;
			if(array.length > 0){
				saveEccAlbum(JSON.stringify(array));
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
		}
	});
}

var isSubmit = false;
function saveClassDesc(){
	if(isSubmit){
        return;
    }
    var pictrueUrl = $("#pictrueUrl").val();
    if(!pictrueUrl||pictrueUrl==''){
    	layerTipMsgWarn("简介图片","请上传图片");
		return false;
    }
    var content = UE.getEditor('content').getContent();
    if(!content||content==''){
    	layerTipMsgWarn("简介内容","不可为空");
		return false;
    }
    isSubmit = true;
	var options = {
		url : "${request.contextPath}/eclasscard/standard/description/save",
		dataType : 'json',
		success : function(data){
	 		if(!data.success){
	 			layerTipMsg(data.success,"保存失败",data.msg);
	 			isSubmit = false;
	 		}else{
				layer.msg("保存成功");
			}
			isSubmit = false;
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#eccClassDescForm").ajaxSubmit(options);
}
function saveEccAlbum(jsonStr){
	if(isSubmit){
        return;
    }
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/standard/photoalbum/save?objectId=${classDesc.id!}",
			contentType: "application/json;charset=utf-8",
			data:jsonStr,
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存图片失败",data.msg);
		 		}else{
		 			showPicture(data.businessValue);
		 			$("#pictrueUrl").val(data.businessValue);
    			}
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
function showPicture(picUrl){
	var path = "${request.contextPath}"+picUrl;
	$("#eccDescPicture").attr('src',path); 
	$("#showPictureDiv").show();
	$("#upLoadPictureDiv").hide();
}
function deletePicture(){
	var eccClassDescId = $("#eccClassDescId").val();
	var options = {
			url : "${request.contextPath}/eclasscard/standard/description/deletePictrue",
			data:{id:eccClassDescId},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"删除失败",data.msg);
		 		}else{
			 		$("#showPictureDiv").hide();
					$("#upLoadPictureDiv").show();
					$("#pictrueUrl").val('');
					removeWebUploadFile();
    			}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
//删除附件时，webupload中文件移除
function removeWebUploadFile(){
	if(picUploader){
		var files = picUploader.getFiles();
		for (var i = 0; i < files.length; i++) {
        	picUploader.removeFile(files[i], true);
			$("#"+files[i].id).remove();
        }
        $("#filePicker").show();  
	}
}
</script>