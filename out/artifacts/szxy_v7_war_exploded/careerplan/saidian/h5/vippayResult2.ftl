<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>支付结果</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body class="mui-bg-white">
    <header class="mui-bar mui-bar-nav">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
        <h1 class="mui-title">支付结果</h1>
    </header>		
    <div class="mui-content mui-bg-white">
    	<#if paymentDetails.tradeStatus?default('')=='03'>
	    	<div class="career-hd-pay">
	    		<div class="mui-inline">
	    			<img class="f-left mr-10" width="50px" height="50px" src="${request.contextPath}/static/mui/images/career/icon_success.png">支付成功
	    		</div>
	    	</div>
	    	<ul>
	    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">备注</span><span class="f-16">您已经是VIP用户，无需重复购买!</span></li>
	    	</ul>
	    <#else>
	    	<div class="career-hd-pay">
	    		<div class="mui-inline">
	    			<img class="f-left mr-10" width="50px" height="50px" src="${request.contextPath}/static/mui/images/career/icon_fail.png">支付失败
	    		</div>
	    	</div>
	    	<ul>
	    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">备注</span><span class="f-16">支付失败，请重新购买!</span></li>
	    	</ul>
    	</#if>
    	
    	
		<div class="mui-text-center" style="margin-top: 200px;">
			<button type="button" class="mui-btn mui-btn-blue mui-btn-outlined btn-radius-16 js-back">返回首页</button>
			<#if paymentDetails.tradeStatus?default('')!='03'>
			<button type="button" class="mui-btn mui-btn-blue btn-radius-16 ml-10 js-back-vip">重新支付</button>
			</#if>
		</div>
    </div>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
</body>
<script type="text/javascript">
	$(".js-back").on('tap',function(){
		var url="${request.contextPath}/careerPlanning/careerplanIndex/page";
		location.href=url;
	})
	<#if paymentDetails.tradeStatus?default('')!='03'>
	$(".js-back-vip").on('tap',function(){
		var url="${request.contextPath}/careerPlanning/commodityDetails";
		location.href=url;
	})
	</#if>
</script>
</html>
