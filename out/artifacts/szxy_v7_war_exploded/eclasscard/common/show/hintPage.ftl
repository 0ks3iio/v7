<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>无网络</title>
	<meta name="viewport">
	<script src="${request.contextPath}/static/eclasscard/common/show/js/flexible.js"></script>
	<script>
		_view = "${view!}";
		_deviceNumber = "${deviceNumber!}";
		cardId = "${cardId!}";
		_contextPath = "${request.contextPath}";
	</script>
	<style>
		*{padding:0;margin:0;}
		html{height:100%;}
		body{
			min-height:100%;
			display:-webkit-box;
			display:-webkit-flex;
			display:flex;
			font-family: "Microsoft Yahei", "Helvetica Neue", Helvetica, Arial, sans-serif;
			font-size: 14px;
			line-height: 1.5;
			background:-webkit-gradient(linear, left top, left bottom, from(#d4e9ff), to(#fff));
			background:-webkit-linear-gradient(top, #d4e9ff, #fff);
			background:linear-gradient(to bottom, #d4e9ff, #fff);
		}
		.container{
			width:666px;
			max-width:100%;
			max-height:100%;
			padding:0 10px;
			margin: auto;
			text-align:center;
		}
		.container img{
			max-width:100%;
			max-height: 40vh;
		}
		.text{
			padding:45px 0;
			border-top:2px solid;
			margin-top:20px;
			font-size:24px;
			text-align:left;
			color:#539be7;
		}
		.btn{
			min-width: 150px;
			height: 52px;
			padding: 0 30px;
			border:none;
			-webkit-border-radius:8px;
			        border-radius:8px;
			margin-top:30px;
			-webkit-box-shadow: 0 8px #307ac7;
			        box-shadow: 0 8px #307ac7;
			font-size:24px;
			line-height:52px;
			color:#fff;
			background-color: #539be7;
			cursor:pointer;
		}
	</style>
</head>
<body>
	<div class="container">
		<img src="${request.contextPath}/static/eclasscard/common/show/images/offline.png" alt="">
		<div class="text">
			<p>${msgDto.msg!}</p>
		</div>
		<#if msgDto.btn>
			<button type="button" class="btn" onclick="refresh()">刷新</button>
		</#if>
	</div>
<script src="${request.contextPath}/static/eclasscard/common/show/js/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/common/show/js/common.use.js"></script>
<script>
	$(document).ready(function(){
		<#if msgDto.btn>
		setInterval(function(){
			showIndex();
		},60*1000);
		</#if>
		if (window.jsInterface && jsInterface.hideFaceWindow){
			jsInterface.hideFaceWindow();
		}
	})
	
	function refresh(){
		doSkipPage(_contextPath);
		showIndex();
	}
	
	function showIndex(){
		location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?deviceNumber="+_deviceNumber+"&cardId="+cardId+"&view="+_view;
	}
</script>
</body>
</html>