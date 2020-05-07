<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<div id="mediaSet" class="btn-group hover">
	<button type="button" id="addFolderTag" class="btn btn-blue js-add" data-for="multimediaDiv">创建相册/视频集</button>
	<button id="dropdownToggle" type="button" class="btn btn-blue dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	</button>
	<ul class="dropdown-menu no-margin">
		<li><a href="javascript:void(0);" onclick="addFolder(1)">创建相册集</a></li>
		<li><a href="javascript:void(0);" onclick="addFolder(2)">创建视频集</a></li>
	</ul>
</div>
<a class="btn btn-default" id="uploadingPPT" href="">上传PPT</a><span id="ppttips" style="padding-left: 20px;color: #999;font-size: 14px;display:none;">PPT上传成功后，请点击"多媒体"，刷新当前页面</span>
<p></p>
<div id="multimediaDiv" class="card-list clearfix">
<#if attachFolders?exists&&attachFolders?size gt 0>
  	<#list attachFolders as item>
	<div class="card-item">
		<div class="card-content">
			<div class="bottom-tools dropdown">
				<a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>
				<ul class="box-tools-menu dropdown-menu">
					<span class="box-tools-menu-angle"></span>
					<li><a class="js-common-setting" href="javascript:void(0);" onclick="folderSend('${item.id!}','${item.title!}')">设置展示对象</a></li>
					<li><a class="js-box-remove" href="javascript:void(0);" onclick="deleteFolder('${item.id!}','${item.type!}','<#if item.show>true<#else>false</#if>')">删除</a></li>
				</ul>
			</div>
			<a href="javascript:;"  draggable="false">
				<div class="card-img ratio-16of9" onclick="<#if item.type != 3 >showPVDetail('${item.id!}','${item.type}','${tabType!}','true')<#else>showPPTDetail('${item.id!}','${item.title!}')</#if>">
					<#if item.type == 1>
						<img src="${request.contextPath}/static/images/growth-manual/photos.png" alt="">
					<#elseif item.type == 2>
						<img src="${request.contextPath}/static/images/growth-manual/videos.png" alt="">
					<#else>
						<img src="${request.contextPath}${item.coverUrl!}" alt="">
					</#if>
					<#if item.show>
						<span class="card-label-block bg-red">班牌展示中</span>
					</#if>
				</div>
			</a>
			<h4 class="card-name card-name-edit edit-folder-name" id="${item.id!}">
				<input type="text" class="group-png" value="${item.title!}"/>
				<#if item.type == 1>
					<img src="${request.contextPath}/static/images/growth-manual/jpg-icon.png" alt="">
				<#elseif item.type == 2>
					<img src="${request.contextPath}/static/images/growth-manual/mov-icon.png" alt="">
				<#else>
					<img src="${request.contextPath}/static/images/growth-manual/ppt-icon.png" alt="">
				</#if>
			</h4>
			<input type="hidden" class="folder-id-class" value="${item.id!}">
			<input type="hidden" class="folder-type-class" value="${item.type!}">
		</div>
	</div>
    </#list>
<#else>
<div id="noDateContainer"class="no-data-container" style="padding-top:20px;">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
		</span>
		<div class="no-data-body">
			<h3>暂无多媒体</h3>
			<p class="no-data-txt">请点击左上角的“创建”按钮添加</p>
		</div>
	</div>
</div>
</#if>
</div>
<div id="folderSendLayer" class="layer layer-editCheckTime">

</div>

<div id="showPPTDiv" >

</div>

<script type="text/javascript">
$(function(){
	var timeStamp = Date.parse(new Date());
	var host = window.location.host+"${request.contextPath}";
	var pptUrl = "ppt2img://URLProtocol?param=${param!}&type=eis7&backUrl=http://"+host+"/eccShow/eclasscard/pptConvertIn?timeStamp="+timeStamp+"&noticeurl=http://"+host+"/eccShow/eclasscard/pptConvertNotice&dirName=${dirName!}";
	$("#uploadingPPT").attr("href",pptUrl);
	$("#multimediaDiv").css({
		height: $(window).height() - $("#multimediaDiv").offset().top - 60,
		overflowY: 'auto'
	})
	$('#multimediaDiv').on('mouseover','.card-name-edit',function(){
		$(this).addClass('alter-after');
	});
	$('#multimediaDiv').on('mouseout','.card-name-edit',function(){
		$(this).removeClass('alter-after');
	});
	$(".card-content .card-name-edit input").each(function(){
  		if($(this).val() == '' || $(this).val() == '在此处编辑名称'){
			layer.tips('在此处编辑名称', $(this), {
	  			tips: 3
			});
  		}
	});
	
	var indexClose;
	var isPost = true;
	var times = 1;
	$("#uploadingPPT").click(function(){
		indexClose = layer.msg('正在启动PPT上传工具,请稍等...',{
			time:30000
		},function(){});
		isPost = true;
		$("#ppttips").show();
		pollGetPPTCutState();
	});
	
	function pollGetPPTCutState(){
		var time = window.setInterval(function(){
			if(!isPost){
				window.clearInterval(time);
				return;
			}
			getPPTCutState();
		},5000);
	}
	
	function getPPTCutState(){
		$.ajax({
	    	url:"${request.contextPath}/eccShow/eclasscard/pptConvertTime",
	        	type:"post",
	            data:{"timeStamp":timeStamp},
	            success:function(data){
	            	var jsonO = JSON.parse(data);
	            	if(jsonO.success){
	            		isPost = false;
	                    layer.close(indexClose);
	            	} else {
	            		if (times==6) {
	            			layer.open({
	                        	btn: ['下载客户端']
	                            ,content: '上传PPT需要安装客户端,请先下载并安装'
	                            ,yes: function(index){
	                            	window.location.href="http://msyk.wpstatic.cn/ppt2img/美师优课PPT传输工具v2.2.4.exe";
	                            	//window.location.href="${request.contextPath}/eclasscard/common/back/美师优课PPT传输工具V2.2.2.exe";
	                                layer.close(index);//如果设定了yes回调，需进行手工关闭
	                            }
	                        });
	            			isPost = false;
	               			layer.close(indexClose);
	            		} 
	            	}
	            }
	        });
		times++;
	}
});
function showPPTDetail(folderId,title){
	// 编辑考勤时间
	var url =  '${request.contextPath}/eclasscard/standard/pptalbum/list?folderId='+folderId;
	$("#showPPTDiv").load(url,function() {
		  showPPT(title);
		});
}

function showPPT(title){
	layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: title,
	    	area: ['900px', '600px'],
	    	zIndex: 1030,
	    	content: $('#showPPTDiv')
	})
}

// 编辑相册名称
var title = '';
var oldTitle = '';
var id = '';
var type = '';
$('#multimediaDiv').on('focus','.edit-folder-name input',function(e){
	e.stopPropagation();
	oldTitle = $(this).val();
	if(oldTitle.trim()=='在此处编辑名称'){
		$(this).val('');
	}
	var i=0;
	$(this).on('keydown', function(e){
		if(e.keyCode === 13 && i<1){
			$(this).blur();
			i++;
		}
	});
	
});

$('#multimediaDiv').on('blur','.edit-folder-name input', function(e){
	title = $(this).val();
	id = $(this).parent('.edit-folder-name').next().val();
	type = $(this).parent('.edit-folder-name').next().next().val();
	saveFolder(id,title,type);
});

function folderSend(id,title){
	var url =  '${request.contextPath}/eclasscard/standard/eccfolder/send?id='+id;
	$("#folderSendLayer").load(url,function() {
		  folderSendLayer(title);
		});
}

function folderSendLayer(name){
	layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: name,
	    	area: ['900px', '600px'],
	    	btn: ['确定','取消','不展示'],
	    	yes: function(index){
			    sendFolderTo(index);
			},btn3: function(index, layero){
    			notShowMultimedia(index);
  			},
	    	zIndex: 1030,
	    	content: $('#folderSendLayer')
	})
}

var isSubmit = false;
var isSave = true;
function saveFolder(id,title,type){
	if(isSubmit){
        return;
    }
    if(!title||title.trim()==''){
		layer.tips('多媒体名称不可为空', '#'+id, {
  			tips: 3
		});
		return false;
    }
    if(title.trim()==oldTitle){
    	oldTitle='';
		return false;
    }
	if(title.length>25){
		layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！', '#'+id, {
  			tips: 3
		});
		return;
	}
    isSubmit = true;
    var options = {
			url : "${request.contextPath}/eclasscard/standard/eccfolder/save",
			data:{id:id,title:title,range:2,type:type},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"多媒体集保存失败",data.msg);
		 		}
		 		setTimeout(function(){
	    			showContent('${tabType!}');
				},500);
    			isSubmit = false;
    			isSave = true;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}

function deleteFolder(id,type,isShow){
	var msg = "";
	if(type==1){
		msg = '删除相册集时，其包含的照片也将一起被删除。确定要删除吗？';
	}else if (type==2){
		msg = '删除视频集时，其包含的视频也将一起被删除。确定要删除吗？';
	} else {
		msg = '确定要删除PPT吗？';
	}
	layer.confirm(msg, function(index){
	    var options = {
				url : "${request.contextPath}/eclasscard/standard/eccfolder/delete",
				data:{id:id,range:2,isShow:isShow},
				dataType : 'json',
				success : function(data){
			 		if(!data.success){
			 			layerTipMsg(data.success,"删除多媒体集失败",data.msg);
			 		}
			 		setTimeout(function(){
		    			showContent('${tabType!}');
					},500);
	    			isSave = true;
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
		$.ajax(options);
		layer.close(index);
	});
}
// 创建相册
function addFolder(type){
	if(!isSave){
		return;
    }
    var hasNoName = false;
    $(".card-content .card-name-edit input").each(function(){
  		if($(this).val() == '' || $(this).val() == '在此处编辑名称'){
  			layer.tips('请先编辑此处文件名', $(this), {
	  			tips: 3
			});
			hasNoName = true;
			return;
  		}
	});
	if(hasNoName){
		return;
    }
<#--var item = '<div class="card-item">\
				<div class="card-content">\
					<div class="bottom-tools dropdown">\
						<a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>\
					</div>\
					<a href="javascript:;" draggable="false">\
						<div class="card-img ratio-16of9">';
	if (type == 1) {
		item += '<img src="${request.contextPath}/static/images/growth-manual/photos.png" alt="">';
	} else {
		item += '<img src="${request.contextPath}/static/images/growth-manual/videos.png" alt="">';
	}
		item +=			'</div>\
					</a>\
					<h4 class="card-name card-name-edit edit-folder-name"><input type="text" class="group-png" value="在此处编辑名称"/></h4>\
					<input type="hidden" class="folder-id-class"  value="">\
					<input type="hidden" class="folder-type-class"  value="'+ type +'">\
				</div>\
			</div>';
	var goal = $('#' + $("#addFolderTag").data('for'));
	
	$(item).fadeIn('500', function(){
	}).prependTo(goal); -->
	saveFolder("","在此处编辑名称",type);
<#--$("#noDateContainer").hide();-->
	isSave = false;
}
</script>