<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>班级空间</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<script>
	_cardId = "${cardId!}";
	_view = "${view!}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/gallery/css/blueimp-gallery.min.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/css/style.css?v=20190108">
</head>
<body class="classSpace">
	<header class="header">
		<div class="back">
			<a class="arrow" href="javascript:void(0);" onclick="backIndex()"></a>
			<span>班级空间</span>
		</div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<div class="main-container">
		<div class="space">
			<div class="space-left">
				<div class="classSpace-left">
					<div class="class-info">
						<div class="owner">
							<h3 class="owner-name">${className!}</h3>
							<div class="owner-meta">
								<div><span class="icon icon-teacher"></span>${teacherName!}</div>
								<div><span class="icon icon-student"></span>${classStuNum!}人</div>
							</div>
						</div>
					</div>
					<ul class="side-nav">
						<li class="active"><a href="javascript:void(0);" onclick="showRightContent(1)"><i class="icon icon-txt"></i>班级简介</a></li>
						<li><a href="javascript:void(0);" onclick="showRightContent(2,1)"><i class="icon icon-photo"></i>班级相册</a></li>
						<li><a href="javascript:void(0);" onclick="showRightContent(3,1)"><i class="icon icon-book"></i>德育考核</a></li>
					</ul>
				</div>
			</div>
			<div class="space-right">
				<div id="rightContent" class="tab-content">
					
					
				</div>
			</div>
		</div>
	</div>
<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/gallery/js/blueimp-gallery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>
<script>


$(document).ready(function(){
	showRightContent(1);
})
function backIndex(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
}
function showRightContent(type,pageIndex){
	var url='';
	if(type==2){
		url =  "${request.contextPath}/eccShow/eclasscard/classSpace/album?cardId="+_cardId+"&pageIndex="+pageIndex;
	}else if(type==3){
	    url =  "${request.contextPath}/eccShow/classSpace/dyCheckHead?classId=${classId!}&unitId=${unitId!}";
	}else{
		url =  "${request.contextPath}/eccShow/eclasscard/classSpace/description?cardId="+_cardId+"&view="+_view;
	}
	$("#rightContent").load(url);
}
</script>
</body>
</html>