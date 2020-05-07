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
	    	<div class="logo"><a href="#"><img src="${request.contextPath}/static/career/images/logo.png" alt=""></a></div>
	        <p class="header-name">支付中心</p>
	        <a href="javascript:" class="back-home" onclick="backCareer();">返回首页</a>
	    </div>
	</div>

	<div id="container">
		<div class="cart-tt"><span class="step step1"></span><span>${paymentDetails.userName!}</span> 的支付订单</div>
	    <div class="cart-wrap order-wrap">
	    	<div class="order-info mt-30">
	    		<p class="order-price mt-3">订单金额：<span class="f-26 c-orange">￥${paymentDetails.commodityAmount?default(0)?string("0.00")}</span></p>
	    		<div class="order-des c-333">
	            	<p class="f-16">订单提交成功！请在<span class="c-orange">${timeleter?string('yyyy-MM-dd HH:mm')}</span>前完成付款，超出时间后订单需重新下单购买！</p>
	                <p class="f-14">订单号: ${paymentDetails.id!}<span class="mx-20 c-999">|</span>购买内容：${paymentDetails.orderName!}</p>
	            </div>
	    	</div>
	        <div class="fn-pay">
	            <p class="dt">选择以下支付方式付款</p>
	            <ul class="fn-bank fn-bank-platform">
	                <li class="fn-clear">
	                	<span class="ui-radio ui-radio-current" data-name="a" checked="checked"><input type="radio" class="radio"></span>
	                    <span class="bank-box alipay">支付宝</span>
	                </li>
	            </ul>
	            <div class="pt-30">
	                <a href="javascript:" class="abtn abtn-big abtn-red btnPay">确认支付</a>
	                <span class="tips ml-20">温馨提示：为确保支付成功，在订单提交成功之前，请勿关闭任何支付页面。</span>
	               
	            </div>
	        </div>
	    </div>
	</div>

	<div class="footer-wrap">
		<div class="footer-inner">
	     	浙江万朋教育科技股份有限公司版权所有 备案号:浙ICP备05070430号
		</div>
	</div>
</div>
<div class="ui-layer" id="alipaylay" style="display:none;width:420px;">
	<p class="tt"><a href="#" class="close">关闭</a><span class="title">支付宝支付</span></p>
	<div class="wrap">
    	<div class="weixinpay-info">
            <p class="pt-10 t-center f-14 c-333">应付总额：<span class="f-18 c-orange">￥${paymentDetails.commodityAmount?default(0)?string("0.00")}</span></p>
            <div class="qr-wrap"><img src="${request.contextPath!}/careerPlanning/payImage" width="194" height="194" id="payImg">
            <p class="tit" style="diaplay:none">二维码已失效<br>请重新提交付款</p>
            </div>
            <p class="pt-10 t-center f-14 c-333">请使用支付宝扫一扫二维码支付</p>
            <p class="t-center f-14 c-333">二维码有效时长为半小时</p>
            <p class="pt-20 t-center c-666">支付如遇问题请拨打客服热线 400-617-1997</p>
        </div>
    </div>
</div>

<div class="ui-layer" id="tipsLayer" style="display:none;width:380px;left:50px;">
	<p class="tt"><a href="#" class="close">关闭</a><span class="title">提示</span></p>
	<div class="wrap">
    	<div class="pay-tips">
            <p class="f-16">根据您的付款结果选择：</p>
            <p class="pt-30 t-center"><a href="${request.contextPath!}/careerPlanning/vippayresult/page?orderId=${paymentDetails.id!}" class="abtn abtn-red">支付完成</a>
            <a href="${request.contextPath!}/careerPlanning/vippayresult/page?orderId=${paymentDetails.id!}" class="abtn abtn-orange-ring ml-10">支付遇到问题</a></p>
        </div>
    </div>
</div>
<div class="ui-layer" id="errortipsLayer" style="display:none;width:380px;left:50px;">
	<p class="tt"><a href="#" class="close">关闭</a><span class="title">提示</span></p>
	<div class="wrap">
    	<div class="error-info">
            <div class="qr-wrap"><img src="${request.contextPath}/static/images/public/limit.png" alt="" width="100" height="100" >
            </div>
            <p class="pt-10 t-center f-14 c-333">服务器开小差了...</p>
        </div>
    </div>
</div>


</body>
<script src="${request.contextPath}/static/career/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.Layer.js"></script>
<script src="${request.contextPath}/static/career/js/jquery.FormNice.js"></script>
<script src="${request.contextPath}/static/career/js/myscript.js"></script>
<script type="text/javascript">
	cre_contextPath='${request.contextPath}';
	var closeFun=function(){
		var url="${request.contextPath!}/careerPlanning/vippayindex/page";
		window.location.href=url; 
	}
	
	var imgInitFun=function(){
		//先验证在弹出框
		var url=cre_contextPath+"/careerPlanning/paycheck?t="+(new Date).getTime();
		$.ajax({
			url: url,
			data: {'orderId':'${paymentDetails.id!}'},
			type:'post',
			async:false,
			success:function(data) {
				var jsonO = JSON.parse(data);
				if(jsonO.msg=="3"){
					gotoResult();
				}else if(jsonO.msg!="0"){
					//订单失效1，2
		 			$("#alipaylay").find(".tit").show();
		 		}else{
		 			$("#alipaylay").find(".tit").hide();
		 			document.getElementById("payImg").src="${request.contextPath!}/careerPlanning/payImage?orderId=${paymentDetails.id!}&t="+(new Date).getTime();
		 			checkPay();
		 		}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){
                //弹出失败
             	closeCode();
             	showSuccessMsg('#errortipsLayer','.close','提示');
			}
			
		})
	}
	
	$(function(){
		layer('.btnPay','#alipaylay','.close','提示',imgInitFun,closeFun);
	})
	
	function gotoResult(){
		var url="${request.contextPath!}/careerPlanning/vippayresult/page?orderId=${paymentDetails.id!}";
		window.location.href=url; 
	}
	
	//循环调用根据页面btnPay是否存在来判断是否循环调用
	var checkPay = function(){
    	if($(".btnPay").hasClass('disabled')){
    		$.ajax({
    			url:cre_contextPath+'/careerPlanning/paycheckResult',
				data: {'orderId':'${paymentDetails.id!}'},
				type:'post',
				success:function(data) {
			 		var jsonO = JSON.parse(data);
			 		if(jsonO.msg=="2"){
	              	 	$("#alipaylay").find(".tit").show();
			 		}else if(jsonO.msg!="0"){
			 			closeCode();
	              	 	showSuccessMsg('#tipsLayer','.close','提示');
			 		}else{
			 			setTimeout("checkPay()",5000);
			 		}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){
	                //弹出失败--提示信息
	                closeCode();
	                showSuccessMsg('#errortipsLayer','.close','提示');
				}
    		})
    		
    	}else{
    		//停止校验
    		//setTimeout(checkPay(),10000);
    	}
	}
	
	function closeCode(){
		$("#alipaylay").hide();
	  	$('.ui-layer-mask').remove();
 	 	$(".btnPay").removeClass('disabled');
	}

</script>
</html>
