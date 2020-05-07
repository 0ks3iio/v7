<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>vip</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body>
    <header class="mui-bar mui-bar-nav">
    	<button class="mui-btn mui-btn-link mui-btn-nav mui-pull-left back_index"><span class="mui-icon mui-icon-left-nav"></span>返回</button>
    	<#--<button class="mui-btn mui-btn-link mui-btn-nav mui-pull-left">关闭</button>-->
        <h1 class="mui-title">升级VIP</h1>
        <#--<a class="mui-icon mui-icon-more mui-pull-right"></a>-->
    </header>		
    <div class="mui-content career-vip">
    	<div class="vip1">
    		<div class="vip-buy">
    			<div class="hd">升级VIP畅享选科测评核心功能</div>
    			<div class="price">￥<span>${vipamount?default(200)?string("0.00")}</span><s>￥${ptamount?default(300.00)?string("0.00")}</s></div>
    			<a class="btn vip_button" href="javascript:">立即购买</a>
    		</div>
    	</div>
    	<div class="vip2"></div>
    	<div class="vip3"></div>
    </div>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
</body>
<script type="text/javascript">
	$(".vip_button").on('tap',function(){
		var url="${request.contextPath}/careerPlanning/vippayindex/page";
		location.href=url;
	})
	$(".back_index").on('tap',function(){
		var url="${request.contextPath}/careerPlanning/careerplanIndex/page";
		location.href=url;
	})
</script>
</html>

