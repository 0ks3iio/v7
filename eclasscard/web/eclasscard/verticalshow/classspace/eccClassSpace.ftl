<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>班级空间</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<script>
	_deviceNumber = "${deviceNumber!}";
	_view = "${view!}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/gallery/css/blueimp-gallery.min.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/css/style.css">
</head>
<body class="classSpace">
	<header class="header">
		<div class="logo"><img src="${request.contextPath}/static/eclasscard/verticalshow/images/logo.png" alt=""></div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<#include "/eclasscard/verticalshow/showMsgtip.ftl">
	<div class="main-container">
		<div class="box">
			<div class="space">
				<div class="space-header">
					<h2 class="ecard-name">${className!}</h2>
					<div class="role">
						<#if userName?exists>
						<span class="role-img"><img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${userName!}" alt=""></span>
						<#else>
						<span class="role-img"><img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt=""></span>
						</#if>
						<span class="label label-fill label-fill-yellow">班主任</span>
						<h4 class="role-name">${teacherName!}</h4>
					</div>

					<div class="btn-group btn-group-lg">
						<a class="btn active" href="javascript:void(0);" onclick="showRightContent(1)" data-action="tab">班级简介</a>
						<a class="btn" href="javascript:void(0);" onclick="showRightContent(2,1)" data-action="tab">班级相册</a>
					</div>
				</div>
				<div class="space-content">
					<div class="tab-content" id="rightContent">
						
					</div>
				</div>
			</div>
		</div>
			
	</div>

	<footer id="footer" class="footer">
	</footer>

	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/jquery/jquery.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/gallery/js/blueimp-gallery.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/layer/layer.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/js/myscript.js"></script>
	<script>
		$(document).ready(function(){
			var footUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/showFooter?type=3&name="+_deviceNumber+"&view="+_view;
			$("#footer").load(footUrl);
			showRightContent(1);
		})
		
		function showRightContent(type,pageIndex){
			var url='';
			if(type==2){
				url =  "${request.contextPath}/eccShow/eclasscard/classSpace/album/page?name="+_deviceNumber;
			}else{
				url =  "${request.contextPath}/eccShow/eclasscard/classSpace/description?name="+_deviceNumber+"&view="+_view;
			}
			$("#rightContent").load(url);
		}
		
		function showStudentSpace(){
			location.href = "${request.contextPath}/eccShow/eclasscard/studentSpace/login?deviceNumber="+_deviceNumber+"&view="+_view;
		}
		
		function showClassSpace(){
			location.href = "${request.contextPath}/eccShow/eclasscard/classSpace/index?deviceNumber="+_deviceNumber+"&view="+_view;
		}
	</script>
</body>
</html>