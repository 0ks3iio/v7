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
	    <div class="cart-wrap">
	    	<#if paymentDetails.tradeStatus?default('')=='03'>
	    	<div class="order-result order-result-right">
	        	<div class="tt t-center"><p class="fn-inline-block"><span></span>您已经是VIP用户<#if isBuy?default(true)>，无需重复购买</#if>！</p></div>
	            <p class="f-14 t-center mt-10">系统将在 <span class="c-orange timeclass">5秒</span> 后返回首页，您也可直接点击<a class="c-blue backTofirst" href="javascript:">返回首页</a></p>
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
	<#if paymentDetails.tradeStatus?default('')=='03' || paymentDetails.tradeStatus?default('')=='04'>
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
		<#if paymentDetails.tradeStatus?default('')=='03' || paymentDetails.tradeStatus?default('')=='04'>
			clockBack();
		</#if>
	})
</script>

