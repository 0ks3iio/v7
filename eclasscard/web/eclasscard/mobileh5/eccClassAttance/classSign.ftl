<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="yes" name="apple-touch-fullscreen">
<meta content="telephone=no,email=no" name="format-detection">
<meta content="fullscreen=yes,preventMove=no" name="ML-Config">
<title>上课考勤</title>
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
        	<h1 class="f-20">走班考勤</h1>
            <a href="#" class="abtn abtn-img abtn-back abtn-left"><span></span></a>
            <a href="#" class="abtn abtn-left2 f-18 mui-btn-close"><span>关闭</span></a>
            <a href="#" class="abtn abtn-img abtn-more abtn-right"><span></span></a>
        </header>
        <div class="scroll-wrap fn-flex-auto ios-touch">
            <div id="container">
            	<div class="select-date f-15">
                    <input type="text" id="startDate" class="txt f-15" value="${date!}" placeholder="请选择" >
                </div>
                <ul class="attend-timeline">
                	<#list classAttences as item>
               			 <li dataval="${item.id!}">
	                        <span class="f-12">${item.sectionName!}</span>
	                        <a href="javascript:;">
                            <div class="attend-box">
                                <div class="attend-box-header f-15">${item.subjectName!}&emsp;&emsp;${item.className!}</div>
                                <div class="attend-box-body">
                                    <ul class="attend-data">
                                        <li class="attend-data-normal"><em class="f-24">${item.zcStuNum?default(0)}</em><span class="f-9">正常</span></li>
                                        <li class="attend-data-leave"><em class="f-24">${item.qjStuNum?default(0)}</em><span class="f-9">请假</span></li>
                                        <li class="attend-data-late"><em class="f-24">${item.cdStuNum?default(0)}</em><span class="f-9">迟到</span></li>
                                        <li class="attend-data-absence"><em class="f-24">${item.qkStuNum?default(0)}</em><span class="f-9">缺课</span></li>
                                    </ul>
                                </div>
                            </div>
                            </a>
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
<script type="text/javascript">
$(function () {
    var now = new Date();
	$('#startDate').mobiscroll().date({
        theme: 'mobiscroll',
        lang: 'zh',
        display: 'bottom',
        min: new Date(now.getFullYear()-1, now.getMonth(), now.getDate()),
        invalid: [''],
        dateOrder: 'Mddyy',
        dateFormat: 'yy-mm-dd',
        timeWheels: 'yy MM dd',
    });
    
    $("#startDate").change(function(){
	 	load("${request.contextPath}/mobile/open/eclasscard/classSign?syncUserId=${syncUserId!}&date="+$(this).val());
	});
	$(".attend-timeline li").click(function(){
		load("${request.contextPath}/mobile/open/eclasscard/classStudentSign?syncUserId=${syncUserId!}&id="+$(this).attr("dataval"));
	});
	if (weikeJsBridge) {
		weikeJsBridge.windowClose(".mui-btn-close");//绑定关闭事件
	}
});

//微课返回方法
function wkGoBack(){
	$(".mui-btn-close").click();
}
</script>
</body>
</html>
