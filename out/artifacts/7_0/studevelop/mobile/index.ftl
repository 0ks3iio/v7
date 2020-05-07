<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-touch-fullscreen" content="yes">
	<meta name="format-detection" content="telephone=no,email=no">
	<meta name="ML-Config" content="fullscreen=yes,preventMove=no">
	<title>成长记录首页</title>
	<link href="${request.contextPath}/static/mui/css/mui.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.picker.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.poppicker.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/iconfont.css" rel="stylesheet"/>
	<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
</head>

<body>
	<div style="display:none;">
		<form id="_macro_form_id" method="post">
		</form>
	</div>
	<div class="mui-content">
		<div class="mui-index-banner">
			<div class="mui-index-banner-header">
				<a href="javascript:void(0);" onclick="doLoad('1');">
					<span class="avatar">
					<#if imgPath?exists && imgPath!="">
						<img src="${imgPath?default('')}"/>
					<#else>
						<img src="${request.contextPath}/studevelop/mobile/images/icon/img_default_photo.png"/>
					</#if>	
					</span>
					<span class="name">
						${stuName?default("")}
						<i class="mui-icon mui-icon-arrowright"></i>
					</span>
				</a>
			</div>
			<div class="mui-index-banner-body">
				<a href="javascript:void(0);" onclick="doLoad('2');">
					<i class="home"></i>
					幸福一家
				</a>
				<a href="javascript:void(0);" onclick="doLoad('3');">
					<i class="book"></i>
					成长手册
				</a>
			</div>
		</div>
		
		<#--成长手册-->
		<#if isBook?default(false)>
			<div class="mui-index-dt">
				<img src="${request.contextPath}/studevelop/mobile/images/index-dt.png" alt="" />
			</div>
		</#if>
		
		<!--=S 九宫格 Start 十二列的栅格系统 xs代表的是极小屏，sm代表的是小屏 -->
		<!--mui-col-xs-3 mui-col-sm-3 四列一行， mui-col-xs-4 mui-col-sm-4 三列一行 -->
		<ul class="mui-table-view mui-grid-view mui-grid-8">
			<#if isFamily?default(false)>
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
		        	<a href="javascript:void(0);" onclick="doLoad('4');">
			            <img src="${request.contextPath}/studevelop/mobile/images/channel/grid-1.png" class="mui-media-img" />
			            <div class="mui-media-body">校外表现 </div>
			        </a>
			    </li>
			    <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
			        <a href="javascript:void(0);" onclick="doLoad('5');">
			            <img src="${request.contextPath}/studevelop/mobile/images/channel/grid-2.png" class="mui-media-img" />
			            <div class="mui-media-body">孩子荣誉</div>
			        </a>
			    </li>
			</#if>
		    <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
		        <a href="javascript:void(0);" onclick="doLoad('6');">
		            <img src="${request.contextPath}/studevelop/mobile/images/channel/grid-3.png" class="mui-media-img" />
		            <div class="mui-media-body">假期生活</div>
		        </a>
		    </li>
		</ul>
		<!--=E 九宫格 End-->
		
	</div>
	<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/jquery.form.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.zoom.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.previewimage.js"></script>
	<script src="${request.contextPath}/static/mui/js/img.ratio.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.picker.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.poppicker.js"></script>

	<script src="${request.contextPath}/static/mui/js/storage.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
	<script type="text/javascript" charset="utf-8">
	mui.init();
	</script>
	<script type="text/javascript">
$(function(){	
	var type = storage.get("type");
	if("1" == type){//如果是自我介绍页面返回  则需要刷新当前页面
		location.reload();
		storage.remove("type");
	}
});	
	
	function doLoad(type){
		var str = "studentId=${studentId!}&acadyear=${acadyear!}&semester=${semester!}";
		if("1"==type){
			load("${request.contextPath}/mobile/open/studevelop/instroduction/index?stuName=${stuName?default("")}&"+str);
		}else if("2"==type){
			load("${request.contextPath}/mobile/open/studevelop/family/index?"+str);
		}else if("3"==type){
			load("${request.contextPath}/mobile/open/studevelop/handbook/index?"+str);
		}else if("4"==type){
		    load("${request.contextPath}/mobile/open/studevelop/outside/index?type=1&"+str);
		}else if("5"==type){
			load("${request.contextPath}/mobile/open/studevelop/honor/index?"+str);
		}else if("6"==type){
			load("${request.contextPath}/mobile/open/studevelop/outside/index?type=2&"+str);
		}
	}
	</script>
</body>
</html>