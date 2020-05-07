<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div class="filter">
	<div class="state-default">
		<div class="filter-item">
			<@upload.picUpload businessKey="${photoDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="10" handler="loadDirFiles">
			<a href="javascript:;" class="btn btn-blue js-addPhotos">上传图片</a>
			<!--这里的id就是存放附件的文件夹地址 必须维护-->
			<input type="hidden" id="${photoDirId!}-path" value="">
			</@upload.picUpload>
		</div>
		<#if albums?exists&&albums?size gt 0>
		<div class="filter-item">
			<a href="javascript:;" class="btn btn-blue js-toManage">批量管理</a>
		</div>
		</#if>
	</div>
	<div class="state-inManage hidden">
		<div class="filter-item">
			<label><input type="checkbox" class="wp" id="checkAll"><span class="lbl"> 全选</span></label>
		</div>
		<div class="filter-item"><a href="javascript:;" class="btn btn-danger" onclick="deleteAlbumPic()">删除</a></div>
		<div class="filter-item"><a href="javascript:;" class="btn btn-blue js-confirm">确定</a></div>
	</div>
</div>
<#if albums?exists&&albums?size gt 0>
<div class="card-list card-list-sm js-layer-photos clearfix">
  	<#list albums as item>
	<div class="card-item">
		<div class="card-content">
			<label class="card-checkbox">
				<input type="checkbox" class="wp checked-input" value="${item.id!}">
				<span class="lbl"></span>
			</label>
			<div class="card-tools">
				<a href="javascript:;" data-toggle="dropdown"><i class="fa fa-angle-down"></i></a>
				<div class="dropdown-menu card-tools-menu">
					<a class="" href="javascript:;" onclick="deleteAlbumPic('${item.id!}')">删除</a>
				</div>
			</div>
			<a href="javascript:;">
				<div class="card-img">
					<img src="${request.contextPath}${item.pictrueDirpath!}" alt="">
				</div>
				<h4 class="card-name">${item.pictrueName!}</h4>
			</a>
		</div>
	</div>
	</#list>
</div>
<#else>
<div class="no-data-container">
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
</#if>
<script type="text/javascript">
$(function(){
	layer.photos({
		shade: .6,
		photos:'.js-layer-photos',
		anim: 5
	})
	$('.js-toManage').on('click',function(){
		$('.state-default').addClass('hidden');
		$('.state-inManage').removeClass('hidden');
		$('.card-list').addClass('in-manage');
	})
	$('.js-confirm').on('click',function(){
		$('.state-default').removeClass('hidden');
		$('.state-inManage').addClass('hidden');
		$('.card-list').removeClass('in-manage');
	})
	$("#checkAll").click(function(){
		var ischecked = false;
    	if($(this).is(':checked')){
    		ischecked = true;
    	}
	  	$(".checked-input").each(function(){
	  		if(ischecked){
	  			$(this).prop('checked',true);
	  		}else{
	  			$(this).prop('checked',false);
	  		}
  		});
	});
})
function deleteAlbumPic(id){
	var ids = "";
	if(id&&id!=''){
		ids = id;
	}else{
		$(".checked-input").each(function(){
	  		if($(this).is(':checked')){
	  			if(ids==''){
	  				ids = $(this).val();
	  			}else{
	  				ids+=','+$(this).val();
	  			}
	  		}
  		});
	}
	var options = {
		url : "${request.contextPath}/eccShow/eclasscard/deletepicture",
		data:{ids:ids},
		dataType : 'json',
		success : function(data){
	 		if(!data.success){
	 			layerTipMsg(data.success,"删除失败",data.msg);
	 		}else{
	 			var url = '${request.contextPath}/eclasscard/photoAlbum/list?eccInfoId=${eccInfoId!}';
				$("#tabContent").load(url);
				photoModifyPush();
			}
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$.ajax(options);
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
				var jsonStr = JSON.stringify(array);
    			saveEccAlbum(jsonStr);
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
		}
	});
}
isSubmit = false;
function saveEccAlbum(jsonStr){
	if(isSubmit){
        return;
    }
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/photoAlbum/save?objectId=${eccInfoId!}",
			contentType: "application/json;charset=utf-8",
			data:jsonStr,
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存相册失败",data.msg);
		 		}else{
		 			var url = '${request.contextPath}/eclasscard/photoAlbum/list?eccInfoId=${eccInfoId!}';
					$("#tabContent").load(url);
					photoModifyPush();
    			}
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}

function photoModifyPush(){
	$.ajax({
		url:"${request.contextPath}/eclasscard/photo/modify/push",
		data:{"objectIds":"${eccInfoId!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
		
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		}
	});
}
</script>