<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="yes" name="apple-touch-fullscreen">
<meta content="telephone=no,email=no" name="format-detection">
<meta content="fullscreen=yes,preventMove=no" name="ML-Config">
<title>上课考勤-详情</title>
<script src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/js/flexible_css.debug.js"></script>
<script src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/js/flexible.debug.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/css/style.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/css/mobiscroll.custom-3.0.0-beta2.min.css">
</head>

<body class="gray">
<#--form需要用div隐藏 否则在submit时  追加的input元素会短暂的显示在页面上-->
<div style="display:none;">
	<form id="_macro_form_id" method="post">
	</form>
</div>
<div id="page">
	<div id="pageInner" class="fn-flex fn-flex-col hidde">
    	<header style="display:none;">
        	<h1 class="f-20">走班考勤详情</h1>
            <a href="#" class="abtn abtn-img abtn-back abtn-left"><span></span></a>
            <a href="#" class="abtn abtn-left2 f-18 mui-action-back"><span>关闭</span></a>
            <a href="#" class="abtn abtn-img abtn-more abtn-right"><span></span></a>
        </header>
        <div class="scroll-wrap fn-flex-auto ios-touch">
            <div id="container">
            	<ul class="stu-list">
            		<#list  eccStuclzAttences as item>     
            			 <li>
							<#if item.stuUserName?exists>
								<img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${item.stuUserName!}">
							 <#else>
								<img src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/images/temp/temp1.png" alt="">
							 </#if>
	                       
	                        <h3 class="f-17">${item.stuRealName!}<span class="f-12 <#if item.status==4>">正常<#elseif item.status==3>attend-label-leave ">请假<#elseif item.status==2>attend-label-late ">迟到<#elseif item.status==1>attend-label-absence ">缺课</#if></span></h3>
	                        <p class="f-12">
	                            <span>${item.stuCode!}</span>
	                            <span>${item.className!}</span>
	                            <span>${item.teacherName!}</span>
	                        </p>
	                    </li>       		
            		</#list>
                </ul>
            </div>
        </div>
    </div>
</div>

<script src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/js/mobiscroll.custom-3.0.0-beta2.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/js/jquery.Layer.js"></script>
<script src="${request.contextPath}/static/eclasscard/mobileh5/eccClassAttance/js/myscript.js"></script>
<script src="${request.contextPath}/static/mui/js/storage.js"></script>
<script src="${request.contextPath}/static/mui/js/weike.js"></script>
<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
<script type="text/javascript">
$(function () {
   $(".mui-action-back").click(function(){
		load("${request.contextPath}/mobile/open/eclasscard/classSign?syncUserId=${syncUserId!}&date=");
	});
});
</script>
</body>
</html>
