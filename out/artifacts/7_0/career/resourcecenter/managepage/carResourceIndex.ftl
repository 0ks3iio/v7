<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<div class="page-content">
	<form id="carResourceForm">
	<div class="row mb20">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
                    <div class="form-horizontal" role="form">
                    	<input type="hidden" id="id" name="id" value="${carResource.id!}"/>
                    	<input type="hidden" id="resourceType" name="resourceType" value="${carResource.resourceType!}"/>
						<div class="form-group">
							<label class="col-sm-2 control-title no-padding-right"><span class="form-title">基本信息</span></label>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right">标题：</label>
							<div class="col-sm-6">
								<input class="form-control" type="text" autocomplete="off" id="title" name="title" value="${carResource.title!}"/>
							</div>
							<div class="col-sm-4 control-tips"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right">资源配图：</label>
							<div class="col-sm-6" id="showPictureDiv" <#if !carResource.pictureUrl?exists>style="display:none"</#if>>
								<div class="form-group-imgUpload"><img id="carResourcePicture" style="width:180px;" <#if carResource.pictureUrl?exists>src="${request.contextPath}${carResource.pictureUrl!}"<#else>src="${request.contextPath}/static/images/classCard/img-default.png"</#if> alt=""></div>
								<a class="btn btn-danger" href="javascript:;" onclick="upLoadNewPicture()">重新上传</a>
							</div>
							<div class="col-sm-6" id="upLoadPictureDiv" <#if carResource.pictureUrl?exists>style="display:none"</#if>>
								<@upload.picUpload businessKey="${photoDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="1" handler="loadPicFile">
								<a class="btn-add-default js-addPhotos" href="javascript:;">+</a>
								<!--这里的id就是存放附件的文件夹地址 必须维护-->
								<input type="hidden" id="${photoDirId!}-path" value="">
								</@upload.picUpload>
								<div class="color-999"><i class="fa fa-exclamation-circle color-yellow"></i> 尺寸建议：178*108 ，图片大小不得超过10M</div>
							</div>
							<input type="hidden" name="pictureArray" id="pictureArray" value=""/>
							<div class="col-sm-4 control-tips"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right">文字描述：</label>
							<div class="col-sm-6">
								<textarea cols="30" rows="5" class="form-control" id="description" name="description">${carResource.description!}</textarea>
							</div>
							<div class="col-sm-4 control-tips"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right">类型：</label>
							<#if carResource.type?exists>
							<div class="col-sm-6">
								<p>
									<label class="inline">
										<#if carResource.type == 0>
											<span class="lbl"> 视频、资讯</span>
										<#elseif carResource.type == 1>
											<span class="lbl"> 视频</span>
										<#else>
											<span class="lbl"> 资讯</span>
										</#if>
									</label>
								</p>
							</div>
							<#else>
							<div class="col-sm-6">
								<p class="typeOption">
									<label class="inline">
										<input type="radio" name="type" class="wp" checked="checked" value="1"/>
										<span class="lbl"> 视频</span>
									</label>
									<label class="inline">
										<input type="radio" name="type" class="wp" value="2"/>
										<span class="lbl"> 资讯</span>
									</label>
									<label class="inline">
										<input type="radio" name="type" class="wp" value="0"/>
										<span class="lbl"> 视频、资讯</span>
									</label>
								</p>
							</div>
							</#if>
							<div class="col-sm-4 control-tips"></div>
						</div>
						<div class="form-group videoSource" <#if carResource.type?exists && carResource.type == 2>style="display:none"</#if>>
							<label class="col-sm-2 control-label no-padding-right">视频来源：</label>
							<div class="col-sm-6">
								<p class="js-explain">
									<#if carResource.videoSource?exists>
									<label class="inline">
										<#if carResource.videoSource == 1>
										<span class="lbl"> 本地上传</span>
										<#else>
										<span class="lbl"> 链接</span>
										</#if>
									</label>
									<#else>
									<label class="inline unifylabel">
										<input type="radio" name="videoSource" class="wp" checked="checked" value="1"/>
										<span class="lbl"> 本地上传</span>
									</label>
									<label class="inline disunitylabel">
										<input type="radio" name="videoSource" class="wp" value="2"/>
										<span class="lbl"> 链接</span>
									</label>
									</#if>
								</p>
								<div class="promptContainer unify" style="padding-left: 15px;padding-right: 15px;<#if carResource.videoSource?exists && carResource.videoSource == 2>display:none;</#if>">
									<table width="100%">
										<tbody>	
										    <tr>
												<td width="80" valign="top">选择文件：</td>
												<td>
													<div class="filter-item" id="upLoadVideoDiv" <#if carResource.videoName?exists>style="display:none;"</#if>>
														<@upload.fileUpload businessKey="${videoDirId!}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" extensions="mp4" mimeTypes=".mp4" size="50" fileNumLimit="1" handler="loadDirVideo">
															<a type="button" href="javascript:;" class="btn btn-blue js-addFiles">上传视频</a>
															<!--这里的id就是存放附件的文件夹地址 必须维护-->
															<input type="hidden" id="${videoDirId!}-path" value="">
														</@upload.fileUpload>
													</div>
													<div id="showVideoDiv" <#if !carResource.videoName?exists>style="display:none;"</#if>>
														<span class="color-red" id="showVideoName">文件名称：<#if carResource.videoName?exists>${carResource.videoName!}</#if></span>
														<a class="color-blue ml10" href="#" onClick="upLoadNewVideo()">重新上传</a>
													</div>
												</td>
												<input type="hidden" name="videoArray" id="videoArray" value=""/>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="promptContainer disunity" style="padding-left: 15px;padding-right: 15px;<#if !carResource.linkUrl?exists>display:none;</#if>">
									<table width="100%">
										<tbody>
										    <tr>
												<td width="80">链接地址：</td>
												<td><input class="form-control" type="text" autocomplete="off" id="linkUrl" name="linkUrl" value="${carResource.linkUrl!}"/></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="col-sm-4 control-tips"></div>
						</div>
						<div class="form-group contents" <#if !carResource.type?exists || carResource.type == 1 || (carResource.type == 0 && carResource.videoSource == 2)>style="display:none"</#if>>
							<label class="col-sm-2 control-label no-padding-right">资讯内容：</label>
							<div class="col-sm-6">
								<textarea id="content" name="content" type="text/plain" style="width:100%;height:360px;">${carResource.content!}</textarea>
							</div>
							<div class="col-sm-4 control-tips"></div>
						</div>												
					</div>
				</div>
			</div>

		</div><!-- /.col -->
	</div><!-- /.row -->
	</form>
	<div class="navbar-fixed-bottom opt-bottom">
        <a class="btn btn-blue" href="#" onClick="saveResource()">保存</a>
    </div>
</div><!-- /.page-content -->
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
         	'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
         	'horizontal', 'date', 'time', '|','simpleupload',
         	'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts'
     		]],
    	initialFrameHeight:300,
    	autoHeightEnabled:false
    	//更多其他参数，请参考ueditor.config.js中的配置项
	});

	$(function(){
		showBreadBack(goback,true,"资源管理");
		
		$(".typeOption label").on('click',function(){
			if ($(this).index() == 0) {
				$('.videoSource').show();
				$('.contents').hide();
			} else if ($(this).index() == 1) {
				$('.videoSource').hide();
				$('.contents').show();
			} else {
				$('.videoSource').show();
				$('.contents').show();
				if ($("input[name='videoSource']:checked").val() == 2) {
					$('.contents').hide();
				}
			}
		});
		
		$('.js-explain label').on('click',function(){
			if ($(this).hasClass('unifylabel')) {
				$('.disunity').hide();
				$('.unify').show();
				if ($("input[name='type']:checked").val() == 0) {
					$('.contents').show();
				}
			} else if ($(this).hasClass('disunitylabel')) {
				$('.unify').hide();
				$('.disunity').show();
				if ($("input[name='type']:checked").val() == 0) {
					$('.contents').hide();
				}
			}
		});
		
	});
	
	function showPicture(pictureUrl) {
		$("#carResourcePicture").attr("src","${request.contextPath}" + pictureUrl);
		$("#showPictureDiv").show();
		$("#upLoadPictureDiv").hide();
	}
	
	function upLoadNewPicture() {
		$.ajax({
			url:"${request.contextPath}/webuploader/removefiles",
			data:{"path":"${photoPath!}"},
			type:'post',
			success:function(data) {
				if(data == "success"){
					$("#carResourcePicture").attr("src","${request.contextPath}/static/images/classCard/img-default.png");
					$("#showPictureDiv").hide();
					$("#upLoadPictureDiv").show();
					$(".js-addPhotos").click();
					$("#pictureArray").val("");
    			} else {
    				layer.msg("删除源文件失败！");
    			}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
		
	}
	
	function showVideo(videoName) {
		$("#showVideoName").text("文件名称：" + videoName);
		$("#upLoadVideoDiv").hide();
		$("#showVideoDiv").show();
	}
	
	function upLoadNewVideo() {
		$.ajax({
			url:"${request.contextPath}/webuploader/removefiles",
			data:{"path":"${videoPath!}"},
			type:'post',
			success:function(data) {
				if(data == "success"){
					$("#showVideoName").text("");
					$("#showVideoDiv").hide();
					$("#upLoadVideoDiv").show();
					$(".js-addFiles").click();
					$("#videoArray").val("");
    			} else {
    				layer.msg("删除源文件失败！");
    			}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
	}
	
	function loadPicFile() {
		$.ajax({
			url:"${request.contextPath}/webuploader/dirfiles",
			data:{"path":"${photoPath!}"},
			type:'post',
			dataType : 'json',
			success:function(data) {
				if(data.length > 0){
					showPicture("/webuploader/showpicture?filePath="+encodeURIComponent(data[0].filePath));
					$("#pictureArray").val(JSON.stringify(data));
    			}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
	}
	
	function loadDirVideo() {
		$.ajax({
			url:"${request.contextPath}/webuploader/dirfiles",
			data:{"path":"${videoPath!}"},
			type:'post',
			dataType : 'json',
			success:function(data) {
				if(data.length > 0){
					showVideo(data[0].fileName);
					$("#videoArray").val(JSON.stringify(data));
    			}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
	}
	
	var isSubmit=false;
	function saveResource() {
		if(isSubmit){
        	return;
    	}
    	
    	var title = $("#title").val();
    	if (title == "" || title == null) {
    		layer.msg("标题不能为空！");
    		return;
    	} else {
    		if (title.length > 50) {
    			layer.msg("标题长度不能大于50字！");
    			return;
    		}
    	}
    	
    	var description = $("#description").val();
    	if (description == "" || description == null) {
    		layer.msg("文字描述不能为空！");
    		return;
    	} else {
    		if (description.length > 500) {
    			layer.msg("文字描述长度不能大于500字！");
    			return;
    		}
    	}
    	
    	<#if carResource.id?exists>
    		<#if carResource.videoSource?exists && carResource.videoSource == 2>
    			var linkUrl = $("#linkUrl").val();
    			if (linkUrl == null || linkUrl == "") {
    				layer.msg("链接地址不能为空！");
    				return;
    			}
    		</#if>
    		<#if carResource.type == 2 || (carResource.type == 0 && carResource.videoSource == 1)>
    			var notice = UE.getEditor('content').getContent();
    			if(!notice||notice==''){
    				layer.msg("资讯内容不能为空！");
					return;
    			}
    		</#if>
    	<#else>
    		var pictureArray = $("#pictureArray").val();
    		if (pictureArray == "" || pictureArray == null) {
    			layer.msg("资源配图不能为空！");
    			return;
    		}
    		
    		var type = $("input[name='type']:checked").val();
    		var videoSource = $("input[name='videoSource']:checked").val();
    		var videoArray = $("#videoArray").val();
    		var linkUrl = $("#linkUrl").val();
    		var notice = UE.getEditor('content').getContent();
    		
    		if (type == 0) {
    			if (videoSource == 1) {
    				if (videoArray == null || videoArray == "") {
    					layer.msg("上传视频不能为空！");
    					return;
    				}
    				if(!notice||notice==''){
    					layer.msg("资讯内容不能为空！");
						return;
    				}
    				$("#linkUrl").val("");
    			} else {
    				if (linkUrl == null || linkUrl == "") {
    					layer.msg("链接地址不能为空！");
    					return;
    				}
    				$("#videoArray").val("");
    				UE.getEditor('content').setContent("");
    			}
    		} else if (type == 1) {
    			if (videoSource == 1) {
    				if (videoArray == null || videoArray == "") {
    					layer.msg("上传视频不能为空！");
    					return;
    				}
    				$("#linkUrl").val("");
    			} else {
    				if (linkUrl == null || linkUrl == "") {
    					layer.msg("链接地址不能为空！");
    					return;
    				}
    				$("#videoArray").val("");
    			}
    			UE.getEditor('content').setContent("");
    		} else {
    			if(!notice||notice==''){
    				layer.msg("资讯内容不能为空！");
					return;
    			}
    			$("#linkUrl").val("");
    			$("#videoArray").val("");
    		}
    	</#if>
    	
    	isSubmit = true;
    	layerTime();
		var options = {
			url : "${request.contextPath}/career/resourcecenter/managepage/saveresource",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
		 			var resourceType = $("#resourceType").val();
		 			showResourceList(resourceType);
		 			layerClose();
					goback();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#carResourceForm").ajaxSubmit(options);
	}
	
	function goback() {
		$("#showResourceTabDiv").attr("style","display:block");
		$("#showResourceDiv").attr("style","display:none");
		$("#showResourceDiv").html("");
	}
</script>