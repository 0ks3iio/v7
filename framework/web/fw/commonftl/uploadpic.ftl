<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<script>
function loadPicFile(){
	alert("上传图片回调方法");
	alert("path===="+$("#eeeeee-path").val());
}

function loadFile(){
	alert("上传文件回调方法");
	alert("path===="+$("#ffffff-path").val());
}
</script>

<@upload.picUpload businessKey="eeeeee" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="30" handler="loadPicFile">
<div class="filter">
	<div class="filter-item">
		<button class="btn btn-blue js-addPhotos">上传图片</button>
	</div>
</div>
<!--这里的id就是存放附件的文件夹地址 必须维护-->
<input type="hidden" id="eeeeee-path" value="">
</@upload.picUpload>
<br>
<br>
<br>
<@upload.fileUpload businessKey="ffffff" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="2" handler="loadFile">
<div class="filter">
	<div class="filter-item">
		<button class="btn btn-blue js-addFiles">上传文件</button>
	</div>
</div>
<!--这里的id就是存放附件的文件夹地址 必须维护-->
<input type="hidden" id="ffffff-path" value="">
</@upload.fileUpload>

