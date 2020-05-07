<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div class="box box-default">
<div class="box-body">
	<div class="form-horizontal">
		<input type="hidden" id="picArray" name="array" value="">
		<form id="photoAllForm">
		<div class="form-group" id="sendTypeLabel">
			<label class="col-sm-2 control-label no-padding-right" >发布对象：</label>
			<div class="col-sm-8" id="sendTypeDiv">
				<label><input type="radio" name="sendType" class="wp" checked value=1><span class="lbl" > 全部班牌</span></label>
				<label><input type="radio" name="sendType" class="wp"  value=2><span class="lbl" > 行政班班牌</span></label>
				<label><input type="radio" name="sendType" class="wp"  value=3><span class="lbl" > 非行政班班牌</span></label>
				<label><input type="radio" name="sendType" class="wp"  value=4><span class="lbl" > 寝室班牌</span></label>
				<label><input type="radio" name="sendType" class="wp"  value=5><span class="lbl" > 校门班牌</span></label>
				<label><input type="radio" name="sendType" class="wp"  value=6><span class="lbl" > 签到班牌</span></label>
				<label id="customSendType"><input type="radio" name="sendType" class="wp custom" value=9><span class="lbl" > 自定义选择</span></label>
			</div>
		</div>
		<div class="form-group hidden">
		<@popup.popup_div clickId="eccInfoName" columnName="班牌（多选）" dataUrl="${request.contextPath}/eclasscard/div/eccinfo/popupData" id="eccInfoIds" name="eccInfoName" dataLevel="2" type="duoxuan" recentDataUrl="${request.contextPath}/eclasscard/div/eccinfo/recentData" resourceUrl="${resourceUrl}" handler="" popupType="all">
			<label class="col-sm-2 control-label no-padding-right">选择对象：</label>
			<div class="col-sm-4">
				<input type="hidden" id="eccInfoIds" name="eccInfoIds"  class="form-control" value="">
				<input type="text" id="eccInfoName" class="form-control" value="">
			</div>
		</@popup.popup_div>
		</div>
		</form>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">上传图片：</label>
			<@upload.picUpload businessKey="${photoDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="10" handler="loadDirFiles">
			<a href="javascript:;" class="btn btn-white js-addPhotos">选择图片</a>
			<!--这里的id就是存放附件的文件夹地址 必须维护-->
			<input type="hidden" id="${photoDirId!}-path" value="">
			</@upload.picUpload>
		</div>
		<div class="form-group" >
			<label id="hideLabel" class="col-sm-2 control-label no-padding-right"></label>
			<div id="showPictruesDiv" class="card-list card-list-sm js-layer-photos clearfix">
			</div>
		</div>
		<div id="noDataContainer" class="no-data-container">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
				</span>
				<div class="no-data-body">
					<h3>暂无图片</h3>
					<p class="no-data-txt">请点击左上角的“上传图片”按钮添加</p>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-8 col-sm-offset-2">
				<a class="btn btn-long btn-blue" onclick="savePhotoAll()">确定</a>
				<a class="btn btn-long btn-white" onclick="cancelPhotoAll()" >取消</a>
			</div>
		</div>
	</form>
	</div>
</div>
</div>
<script>
var sysPath = "${sysFPath!}";
$(function(){
    $('#sendTypeLabel label').on('click', function(){
		if($('.custom').prop('checked') === true){
			$(this).closest('.form-group').next().removeClass('hidden');
		}else{
			$(this).closest('.form-group').next().addClass('hidden');
			$("#eccInfoIds").val('');
			$("#eccInfoName").val('');
		}
    })
});

function cancelPhotoAll(){
	var picArray = $("#picArray").val();
	var array = new Array();
	if(!picArray||picArray==''){
		eclasscardPhotoEdit();
		return;
	}else{
		array = JSON.parse(picArray);
		if(array.length>0){
			$.each(array, function(index, json) {
				if(json.filePath != ''){
					$.ajax({
						url:"${request.contextPath}/webuploader/remove",
						data:{"fullPath":decodeURIComponent(sysPath)+json.filePath},
						type:'post',
						success:function(data) {
							if(data=="success"){
		
							}
						},
				 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
				 		
						}
					});
				}
			});
		}
		eclasscardPhotoEdit();
	}
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
				showPictrues(array);
				$("#picArray").val(JSON.stringify(array));
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
		}
	});
}

function showPictrues(array){
	if(array.length>0){
		var htmlStr = '';
		$.each(array, function(index, json) {
			var picName = json.fileName;
			var picPath = json.filePath;
			htmlStr += '<div class="card-item">';
			htmlStr += '<div class="card-content">';
			htmlStr += '<a href="javascript:;">';
			htmlStr += '<div class="card-img">';
			htmlStr += '<img src="${request.contextPath}/webuploader/showpicture?filePath='+encodeURIComponent(picPath)+'" alt="">';
			htmlStr += '</div>';
			htmlStr += '<h4 class="card-name">'+picName+'</h4>';
			htmlStr += '</a>';
			htmlStr += '</div>';
			htmlStr += '</div>';
		});
		if(array.length>4){
			$("#hideLabel").hide();
		}else{
			$("#hideLabel").show();
		}
		
		$("#noDataContainer").hide();
		$("#showPictruesDiv").html(htmlStr);
	}
}
var isSubmit=false;
function savePhotoAll(){
	if(isSubmit){
        return;
    }
    var eccInfoIds = $("#eccInfoIds").val();
    if($('.custom').prop('checked') === true){
	    if(!eccInfoIds||eccInfoIds==''){
	    	layerTipMsgWarn("自定义对象不可为空","");
			return;
	    }
    }
    var sendType = $("#sendTypeDiv input[type='radio']:checked").val();
    var picArray = $("#picArray").val();
    if(!picArray||picArray==''){
		layerTipMsgWarn("请上传图片","");
		return;
	}
	
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/photoall/save?sendType="+sendType+"&eccInfoIds="+eccInfoIds,
			contentType: "application/json;charset=utf-8",
			data:picArray,
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
					layer.msg("保存成功");
					eclasscardPhotoEdit();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$.ajax(options);
}
</script>
