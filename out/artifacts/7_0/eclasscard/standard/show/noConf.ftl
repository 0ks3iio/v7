<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>noconf</title>
	<script>
		_view = "${view!}";
		_cardId = "${cardId!}";
	</script>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/standard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/standard/show/css/style.css">
</head>
<body>
	<header class="header">
		<div class="logo"></div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<div class="main-container">
		<div class="box">
			<div class="box-body">
				<div class="no-data center">
					<div class="page-error">
						<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-image.png" alt="">
						<p class="page-error-tips">${msgDto.msg!}</p>
						<#if msgDto.btn>
							<a class="btn" href="javascript:void(0);" onclick="showIndex()">已配置，点击刷新</a>
						</#if>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="${request.contextPath}/static/eclasscard/standard/show/plugins/jquery/jquery.min.js"></script>
	<script>
		$(document).ready(function(){
			<#if msgDto.btn>
			setTimeout(function(){
				showIndex();
			},60*1000);
			</#if>
		})
		$(document).ready(function(){
			var container = $('.box:last-of-type > .box-body');
			container.css({
				height: $(window).height() - container.offset().top - 160
			})
		})
		function showIndex(){
			location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
		}
	</script>
</body>
</html>