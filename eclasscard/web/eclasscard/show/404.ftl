<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>404</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/css/style.css">
	<script>
	_view = "${view!}";
	_cardId = "${msgDto.cardId!}";
	</script>
</head>
<body>
	<header class="header">
	</header>
	<div class="main-container">
		<div class="no-data">
			<div class="page-error">
				<h1 class="page-error-state">404</h1>
				<p class="page-error-tips">${msgDto.msg!}</p>
				<#if msgDto.back>
				<a class="btn" href="javascript:void(0);" onclick="backIndex()">已配置，点击刷新</a>
				</#if>
			</div>
		</div>
			
	</div>
	
<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>
<script>
$(document).ready(function(){
	<#if msgDto.btn>
	setTimeout(function(){
		backIndex();
	},60*1000);
	</#if>
})
function backIndex(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
}
</script>
</body>
</html>