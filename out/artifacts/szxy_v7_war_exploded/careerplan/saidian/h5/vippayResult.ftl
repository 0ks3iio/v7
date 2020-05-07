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
	    <#else>
	    	<div class="career-hd-pay">
	    		<div class="mui-inline">
	    			<img class="f-left mr-10" width="50px" height="50px" src="${request.contextPath}/static/mui/images/career/icon_fail.png">支付失败
	    		</div>
	    	</div>
    	</#if>
    	<ul>
    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">产品名称</span><span class="f-16">${paymentDetails.orderName!}</span></li>
    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">支付金额</span><span class="f-16">${paymentDetails.orderAmount?default(0)?string("0.00")}元</span></li>
    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">订单号</span><span class="f-16">${paymentDetails.id!}</span></li>
    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">交易时间</span><span class="f-16">${paymentDetails.modifyTime?string('yyyy-MM-dd HH:mm')} </span></li>
    		<#if paymentDetails.tradeStatus?default('')=='04'>
    		<li class="fn-flex px-15 py-10"><span class="fn-flex-auto c-999 f-14">备注</span><span class="f-16">订单金额与付款金额不一致</span></li>
    		</#if>
    	</ul>
		<div class="mui-text-center" style="margin-top: 200px;">
			<button type="button" class="mui-btn mui-btn-blue mui-btn-outlined btn-radius-16 js-back">返回首页</button>
			<button type="button" class="mui-btn mui-btn-blue btn-radius-16 ml-10 js-back-vip">重新支付</button>
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
	$(".js-back-vip").on('tap',function(){
		var url="${request.contextPath}/careerPlanning/commodityDetails";
		location.href=url;
	})
</script>
</html>
