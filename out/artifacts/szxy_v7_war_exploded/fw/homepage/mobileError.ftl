<#import "/fw/macro/mobilecommon.ftl" as common>
<@common.moduleDiv titleName="提示">
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">提示</h1>
</header>
<div class="mui-content" style="padding-top:0px;">
	<div class="mui-page-error mui-noRecord">
    	<p><i></i></p>
        <p>${_errorMsg?default("")}</p>
        <#--
        <p class="opt">
        	<button class="mui-btn mui-btn-blue mui-btn-outlined">刷新页面</button>
        	<button class="mui-btn mui-btn-blue">返回</button>
        </p>-->
    </div>
</div>


<script type="text/javascript" charset="utf-8">
  	mui.init();
</script>
	
<script type="text/javascript">
$(function(){
	var window_h = $(window).height();
	var header_h = $('header').height();
	var content_h = window_h - header_h;
	$('.mui-noRecord').height(content_h);
});

storage.set(WeikeConstants.WEIKE_FLAG_KEY, WeikeConstants.WEIKE_FLAG_VALUE_TYPE_2);

$("#cancelId").click(function(){
	<#if indexPage?default(false)>
	$('.mui-btn-close').click();
	<#else>
	window.history.back(-1);
	</#if>
});
</script>
</@common.moduleDiv>