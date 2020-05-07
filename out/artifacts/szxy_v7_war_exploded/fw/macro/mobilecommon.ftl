<#--
手机端macro
各业务系统自己的style.css为业务样式文件，放在业务系统代码库，页面文件中单独引入
-->
<#macro moduleDiv titleName="" bodyClass="">
	<!DOCTYPE html>
	<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-touch-fullscreen" content="yes">
		<meta name="format-detection" content="telephone=no,email=no">
		<meta name="ML-Config" content="fullscreen=yes,preventMove=no">
		<title>${titleName!}</title>
		<link href="${request.contextPath}/static/mui/css/mui.css" rel="stylesheet"/>
		<link href="${request.contextPath}/static/mui/css/mui.picker.css" rel="stylesheet"/>
		<link href="${request.contextPath}/static/mui/css/mui.poppicker.css" rel="stylesheet"/>
		<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet"/>
		<link href="${request.contextPath}/static/mui/css/style.css" rel="stylesheet"/>
		<link href="${request.contextPath}/static/mui/css/iconfont.css" rel="stylesheet"/>
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
	</head>
	<body class="${bodyClass!}">
		<#--form需要用div隐藏 否则在submit时  追加的input元素会短暂的显示在页面上-->
		<div style="display:none;">
			<form id="_macro_form_id" method="post">
			</form>
		</div>
		<div id="container">
			<#--插入内容-->
			<#nested />
		</div>
	</body>
	</html>
</#macro>