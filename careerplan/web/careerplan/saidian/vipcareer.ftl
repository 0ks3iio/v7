<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>开通VIP</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/global-V5.8.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/career/css/style.css">
</head>

<body>
<div class="container-wrap container-vip-wrap">
	<div class="header-wrap">
		<div class="header-inner header-login-inner fn-clear">
	    	<div class="logo"><a href="#"><img src="${request.contextPath}/static/career/images/logo10.png" alt=""></a></div>
	        <p class="header-name">升级VIP</p>
	        <a href="#" id="back-home" class="back-home">返回首页</a>
	    </div>
	</div>
	<div id="container">
		<div class="price-outter">
			<a class="buy-btn" id="buy-btn" href="javascript:">立即购买</a>
			<span class="price-inner">
				<span class="price-vip">VIP特价￥<span class="f-44">${vipamount?default(200)?string("0.00")}</span>元</span>
				<span class="price-original">原价<s>￥${ptamount?default(300.00)?string("0.00")}</s></span>
			</span>
		</div>
		<div class="func"></div>
		<div class="obj"></div>
		<div class="vip"></div>
	</div>
	<div class="footer-wrap">
		<div class="footer-inner">
	     	Copyright 1997-2019 [ZDSoft] All Rights Reserved. 赛点教育 版权所有
		</div>
	</div>
</div>
<div class="ui-layer" id="tipsLayer" style="display:none;width:380px;left:50px;">
	<p class="tt"><a href="#" class="close">关闭</a><span class="title">提示</span></p>
	<div class="wrap">
    	<div class="pay-tips">
            <p class="f-16">根据您的付款结果选择：</p>
            <p class="pt-30 t-center"><a href="${request.contextPath!}/careerPlanning/vippayResult2/page" class="abtn abtn-red">支付完成</a>
            <a href="${request.contextPath!}/careerPlanning/vippayResult2/page" class="abtn abtn-orange-ring ml-10">支付遇到问题</a></p>
        </div>
    </div>
</div>
<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.Layer.js"></script>
<script>
	cre_contextPath='${request.contextPath}';
	isBuy=false;
	$(function(){
		$("#back-home").on('click',function(){
			backCareer();
		})
		$("#buy-btn").on('click',function(){
			buyVip();
		})
	})
</script>
</body>
</html>

