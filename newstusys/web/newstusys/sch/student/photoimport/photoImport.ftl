<title>活动图片列表</title>
<meta http-equiv="cache-control" content="no-cache">
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div class="box box-default">
	<div class="box-body">
		<div class="activity-info">
			<h3 class="activity-name">批量上传学生照片</h3>
			<p class="activity-description" style="width:100%;">注意：照片要以<strong style="color:red;">学号</strong>命名，否则学生可能取不到头像信息；上传之后，请点击更新学生照片按钮，更新学生照片</p>
		</div>
		<div class="filter">
			<div class="state-default">
				<@upload.picUpload businessKey="${key!}" contextPath="${request.contextPath}" resourceUrl="${request.contextPath}/static" size="5" fileNumLimit="100">
					<div class="filter-item">
						<button class="btn btn-blue js-addPhotos">上传图片</button>
						<button class="btn btn-blue js-check">更新学生照片</button>
					</div>
				</@upload.picUpload>
			</div>
		</div>
	</div>
</div>
<script>
$(function(){
});

$('.js-check').on('click',function(){
	savePicToStu();
});

// 保存文件到附件表
function savePicToStu(){
	if(hasUploadSuc){
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/newstusys/sch/student/photoimport/checkSave',
			data: {},
			type:'post',
			success:function(data) {
				layer.close(ii);
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					if(picUploader){
						var fs = picUploader.getFiles('complete');
						if(fs && fs.length > 0){
							for(var i=0;i<fs.length;i++){
								var fsid = fs[i].id;
								picUploader.removeFile(fs[i]);
								$("#"+fsid).remove();
							}
						}
					}
					layer.closeAll();
					layerTipMsg(jsonO.success,"提示",jsonO.msg);
		 		}
		 		else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"提示",jsonO.msg);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(ii);
				layerTipMsg(false,'提示','更新失败！');
				refreshList();
			}
		});
	} else {
		layerTipMsg(true,"提示","没有检测到上传图片！");
	}
}
</script>