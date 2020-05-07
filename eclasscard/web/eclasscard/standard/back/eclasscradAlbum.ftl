<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div class="box box-default scroll-height-List">
	<div class="box-header">
		<h4 class="box-title">${folderName!}（${pvNum?default(0)}）</h4>
	</div>
	<div class="box-body">
		<#if changeable == 'true'>
		<div class="filter">
			<div class="state-default">
				<#if type == 1>
				<div class="filter-item">
					<@upload.picUpload businessKey="${fileDirId!}" contextPath="${request.contextPath}" extensions="gif,jpg,jpeg,png" resourceUrl="${resourceUrl}" size="10" fileNumLimit="10" handler="loadDirFiles">
					<a href="javascript:;" class="btn btn-blue js-addPhotos">上传图片</a>
					<!--这里的id就是存放附件的文件夹地址 必须维护-->
					<input type="hidden" id="${fileDirId!}-path" value="">
					</@upload.picUpload>
				</div>
				<#else>
				<div class="filter-item" id="uploadVideo">
					<@upload.fileUpload businessKey="${fileDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" extensions="mp4" mimeTypes=".mp4" size="300" fileNumLimit="1" handler="loadDirVideo">
					<a type="button" href="javascript:;" id="addVideoTag" class="btn btn-blue js-addFiles">上传视频</a>
					<!--这里的id就是存放附件的文件夹地址 必须维护-->
					<input type="hidden" id="${fileDirId!}-path" value="">
					</@upload.fileUpload>
				</div>
				</#if>
				<#if attachments?exists&&attachments?size gt 0>
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
		</#if>
		<#if attachments?exists&&attachments?size gt 0>
		<div id="albumDiv" class="card-list card-list-sm js-layer-photos clearfix">
		  	<#list attachments as item>
			<div class="card-item <#if type==2>js-show-video</#if>">
				<div class="card-content">
					<#if changeable == 'true'>
					<label class="card-checkbox">
						<input type="checkbox" class="wp checked-input" value="${item.id!}">
						<span class="lbl"></span>
					</label>
					<div class="bottom-tools dropdown">
						<a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>
						<ul class="box-tools-menu dropdown-menu">
							<span class="box-tools-menu-angle"></span>
							<li><a class="js-box-remove" href="javascript:void(0);" onclick="deleteAlbumPic('${item.id!}')">删除</a></li>
						</ul>
					</div>
					</#if>
					<a href="javascript:;">
						<#if type==1>
						<div class="card-img">
							<img src="${request.contextPath}${item.showPicUrl!}" alt="">
						</div>
						<#else>
						<div class="card-img ratio-16of9" onclick="playVideo('${item.filePath!}')">
							<video id="cutVideo" class="cut-video" preload="meta" style="width:100%">
							    <source src="${item.filePath!}">
							</video>
						</div>
						</#if>
					</a>
					<#if changeable == 'true'>
					<h4 class="card-name card-name-edit edit-file-name" id="${item.id!}">
						<input type="text" value="${item.filename!}"/>
					</h4>
					<input type="hidden" value="${item.id!}">
					<#else>
					<h4 class="card-name">${item.filename!}</h4>
					</#if>
				</div>
			</div>
			</#list>
		</div>
		<#else>
		<div class="no-data-container" style="padding-top:10%">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
				</span>
				<div class="no-data-body">
					<#if type == 1>
					<h3>暂无图片</h3>
					<p class="no-data-txt">请点击左上角的“上传图片”按钮添加</p>
					<#else>
					<h3>暂无视频</h3>
					<p class="no-data-txt">请点击左上角的“上传视频”按钮添加</p>
					</#if>
				</div>
			</div>
		</div>
		</#if>
	</div>
</div>
<script type="text/javascript">
function gotoMediaPage(){
	backFolderIndex("${tabType!}");
}
$(function(){
	<#--返回-->
	showBreadBack(gotoMediaPage,true,"多媒体");
	$('.scroll-height-List').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 80
		})
	});
	<#if attachments?exists&&attachments?size gt 0>
		$("#albumDiv").css({
			height: $(window).height() - $("#albumDiv").offset().top - 80,
			overflowY: 'auto'
		})
	</#if>
	$('#albumDiv').on('mouseover','.card-name-edit',function(){
		$(this).addClass('alter-after');
	});
	$('#albumDiv').on('mouseout','.card-name-edit',function(){
		$(this).removeClass('alter-after');
	});
});

// 编辑附件名称
var fileName = '';
var oldFileName= '';
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
	
	var id = '';
	$('.edit-file-name input').on('focus', function(e){
		e.preventDefault();
		e.stopPropagation();
		oldFileName = $(this).val();
		var i=0;
		$(this).on('keydown', function(e){
			if(e.keyCode === 13 && i<1){
				$(this).blur();
				i++;
			}
		});
	});
	$('.edit-file-name input').on('blur', function(e){
		fileName = $(this).val()
		id = $(this).parent('.edit-file-name').next().val();
		saveFileName(id,fileName);
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
		url : "${request.contextPath}/eccShow/eclasscard/delete/attachment",
		data:{ids:ids},
		dataType : 'json',
		success : function(data){
	 		if(!data.success){
	 			layerTipMsg(data.success,"删除失败",data.msg);
	 		}else{
		 		setTimeout(function(){
	    			showPVDetail('${folderId!}','${type!}','${tabType!}','${changeable!}');
				},500);
	 			photoDeletePush();
			}
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$.ajax(options);
}
function photoDeletePush(){
	$.ajax({
		url:"${request.contextPath}/eclasscard/standard/photo/delete/push",
		data:{"folderId":"${folderId!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
		
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		}
	});
}
function loadDirFiles(){
	$.ajax({
		url:"${request.contextPath}/webuploader/dirfiles",
		data:{"path":"${filePath!}"},
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
			url : "${request.contextPath}/eclasscard/standard/photoalbum/save?objectId=${folderId!}",
			contentType: "application/json;charset=utf-8",
			data:jsonStr,
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
			 		setTimeout(function(){
		    			showPVDetail('${folderId!}','${type!}','${tabType!}','${changeable!}');
					},500);
    			}
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}

function saveFileName(id,title){
	if(isSubmit){
        return;
    }
    if(!title||title.trim()==''){
		layer.tips('名称不可为空', '#'+id, {
  			tips: 3
		});
		return false;
    }
    title = $.trim(title);
    if($.trim(title)==oldFileName){
    	oldTitle='';
		return false;
    }
    if(title.length>120){
		layer.tips('名称内容不能超过120个字节（一个汉字为两个字节）！', '#'+id, {
  			tips: 3
		});
		return;
	}
    isSubmit = true;
    var options = {
			url : "${request.contextPath}/eclasscard/standard/fileName/update",
			data:{id:id,fileName:title},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"名称保存失败",data.msg);
		 		}
		 		setTimeout(function(){
	    			showPVDetail('${folderId!}','${type!}','${tabType!}','${changeable!}');
				},500);
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}


function loadDirVideo(){
	$.ajax({
		url:"${request.contextPath}/webuploader/dirfiles",
		data:{"path":"${filePath!}"},
		type:'post',
		dataType : 'json',
		success:function(data) {
			var array = data;
			if(array.length > 0){
				var jsonStr = JSON.stringify(array);
    			saveVideo(jsonStr);
    		}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			layer.msg(XMLHttpRequest.status);
		}
	});
}

function saveVideo(array){
	if(isSubmit){
        return;
    }
	isSubmit = true;
    var options = {
			url : "${request.contextPath}/eclasscard/standard/video/save?folderId=${folderId!}",
			contentType: "application/json;charset=utf-8",
			data:array,
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"视频保存失败",data.msg);
		 		}
		 		setTimeout(function(){
		    		showPVDetail('${folderId!}','${type!}','${tabType!}','${changeable!}');
				},500);
    			isSubmit = false;
    			isSave = true;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){alert(XMLHttpRequest.status);}//请求出错 
		};
	$.ajax(options);
}

function playVideo(videoUrl){
	//视频播放
	var video = '<video\
		id=""\
		class=""\
		controls\
		preload="meta"\
		poster=""\
		style="width:800px;height:600px;">\
		<source id="showVideo" src="'+videoUrl+'" type="video/mp4"></source>\
		<p class="vjs-no-js">\
			To view this video please enable JavaScript, and consider upgrading to a\
			web browser that\
		    <a href="http://videojs.com/html5-video-support/" target="_blank">\
		      supports HTML5 video\
		    </a>\
		</p>\
	</video>';
	var index = layer.open({
		type: 1,
		title: false,
		area: ['800px', '600px'],
		content: video
	});
	
	layer.style(index, {
		animation: 'none'
	});
}
</script>