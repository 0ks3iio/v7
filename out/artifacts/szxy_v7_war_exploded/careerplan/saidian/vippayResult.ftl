<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>生涯规划</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/global-V5.8.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/style.css">
</head>

<body>
<div class="container-wrap">
	<div class="header-wrap header-login-wrap">
		<div class="header-inner header-login-inner fn-clear">
	    	<div class="logo"><a href="#"><img src="${request.contextPath}/static/career/images/logo10.png" alt=""></a></div>
	        <p class="header-name">支付中心</p>
	        <a href="javascript:" class="back-home backTofirst" >返回首页</a>
	    </div>
	</div>
	<div id="container" class="careerdiv">
		<div class="cart-tt"><span class="step step2"></span><span>${paymentDetails.userName!}</span> 的支付订单</div>
	    <div class="cart-wrap">
	    	<#if paymentDetails.tradeStatus?default('')=='03'>
	    	<div class="order-result order-result-right">
	        	<div class="tt t-center"><p class="fn-inline-block"><span></span>您的订单已支付成功！</p></div>
	            <p class="f-14 t-center mt-10">系统将在 <span class="c-orange timeclass">5秒</span> 后返回首页，您也可直接点击<a class="c-blue backTofirst" href="javascript:">返回首页</a></p>
	            <ul class="list">
	            	<li>产品名称：${paymentDetails.orderName!}</li>
	                <li>合计金额：<span class="c-orange">￥${paymentDetails.orderAmount?default(0)?string("0.00")}</span></li>
	                <li>订单编号：${paymentDetails.id!}</li>
	                <li>操作时间：${paymentDetails.modifyTime?string('yyyy-MM-dd HH:mm')}</li>
	            </ul>
	        </div>
	        <#elseif paymentDetails.tradeStatus?default('')=='04'>
	    	<div class="order-result order-result-right">
	        	<div class="tt t-center"><p class="fn-inline-block"><span></span>订单金额与付款金额不一致，请及时联系管理员！</div>
	           	<p class="f-14 t-center mt-10">&nbsp;</p>
	            <ul class="list">
	            	<li>产品名称：${paymentDetails.orderName!}</li>
	                <li>支付金额：<span class="c-orange">￥${paymentDetails.orderAmount?default(0)?string("0.00")}</span></li>
	                <li>订单编号：${paymentDetails.id!}</li>
	                <li>操作时间：${paymentDetails.modifyTime?string('yyyy-MM-dd HH:mm')}</li>
	            </ul>
	        </div>
	        <#else>
	        <div class="order-result order-result-error">
	        	<div class="inner">
	                <p class="tt"><span></span>支付失败，请重新提交订单！！</p>
	                <p class="opt">返回<a href="javascript:" class="backTofirst">首页</a>，重新订购支付</p>
	            </div>
	        </div>
	        </#if>
	    </div>
	</div>
	<div class="footer-wrap">
		<div class="footer-inner">
	     	Copyright 1997-2019 [ZDSoft] All Rights Reserved. 赛点教育 版权所有
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.Layer.js"></script>
</body>
</html>
<script type="text/javascript">
	cre_contextPath='${request.contextPath}';
	<#if paymentDetails.tradeStatus?default('')=='03'>
		var i=5;
		function clockBack(){
			if(i==0){
				backCareer();
			}else{
				$(".timeclass").html(i+"秒");
				i--;
				setTimeout("clockBack()",1000);
			}
		}
	</#if>
	$(function(){
		$(".backTofirst").on("click",function(){
			backCareer();
		})
		<#if paymentDetails.tradeStatus?default('')=='03'>
			clockBack();
		</#if>
	})
</script>
