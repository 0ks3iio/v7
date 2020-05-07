<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>新高考选课</title>
	<link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/iconfont.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<style>
  .mui-bar-tab~.mui-content {padding-bottom: 90px;} 
  .mui-popup-text {text-align: left;max-height: 380px;overflow-y: auto;}
</style>

<body class="mui-bg-white">
<#--
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">${choice.choiceName!}</h1>
</header>
-->
<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none"></a>
<#if timeState?default(0)==1>
<nav class="mui-bar mui-bar-tab">
		<div>
			<a class="mui-tab-item f-16 mui-active js-reChoose" href="#" style="background: #2f7bff;color: #fff;">重新选课</a>
        </div>
</nav>
</#if>
<div class="mui-content mui-bg-white" style="padding-top:0px;">
    <ul class="mui-style01 mui-table-view mui-table-view-chevron">
		<li class="mui-table-view-cell mui-media">
			<a class="mui-navigate-right">
				<span class="alertBtn mui-message f-17 c-999">说明</span>
				<div class="mui-media-body">
					<p class="mui-ellipsis f-15 c-333">选课时间：</p>
					<p class="mui-ellipsis f-14 c-333">${(choice.startTime?string('yyyy-MM-dd HH:mm'))?if_exists}至 ${(choice.endTime?string('yyyy-MM-dd HH:mm'))?if_exists}</p>
				</div>
			</a>
		</li>
	</ul>
	<div class="mui-bg-g"></div>
	<div class="box-selectCourse">
		<div class="box-header">已选科目：</div>
		<div class="box-body mui-km-course mui-adjust-course">
			<p class="gk-course-tip2" style="display:none"></p>
		    <ul class="gk-course-list">
	    		<#list resultList as item>
				<li class="mui-row">	
					<a class="mui-col-xs-3" href="javascript:;">
						<h3 class="gk-course-name">${courseNameMap[item.subjectId!]!}</h3>
					</a>
					<div class="mui-col-xs-7"></div>
					<div class="mui-col-xs-2 adjust-btn" style="display:block">
						<a class="active" href="javascript:;"><#if item.subjectType?default('1')=='1'>不可调剂<#elseif item.subjectType?default('1')=='2'>可调剂<#else>优先调剂</#if></a>
					</div>
				</li>
				</#list>
			</ul>
	    </div>
    </div>
    <#if wantToSubjectList?exists && wantToSubjectList?size gt 0>
    <div class="box-selectCourse box-border-top">
		<div class="box-header">优先调剂到（非必选）：</div>
		<div class="box-body mui-km-course">
			<ul class="gk-course-list">
				<#list wantToSubjectList as item>
				<li class="mui-col-xs-3">
					<a href="javascript:;">
						<h3 class="gk-course-name">${courseNameMap[item.subjectId!]!}</h3>
					</a>
				</li>
				</#list>
			</ul>
		</div>
	</div>
	</#if>
	<#if noWantToSubjectList?exists && noWantToSubjectList?size gt 0>
	<div class="box-selectCourse box-border-top">
		<div class="box-header">明确不选（非必选）：</div>
		<div class="box-body mui-km-course">
			<ul class="gk-course-list">
				<#list noWantToSubjectList as item>
				<li class="mui-col-xs-3">
					<a href="javascript:;">
						<h3 class="gk-course-name">${courseNameMap[item.subjectId!]!}</h3>
					</a>
				</li>
				</#list>
			</ul>
		</div>
	</div>
	</#if>
</div>
<div id="alertBtn" style="display:none">
	<div class="mui-popup-title pa-15">选课说明</div>
	<div class="mui-popup-text pa-15">${choice.notice!}</div>
</div>
</body>
<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/mui/js/layer/layer.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
<script src="${request.contextPath}/static/mui/js/weike.js"></script>
<script src="${request.contextPath}/static/mui/js/myWeike.js" async="async" defer="defer"></script>
<script src="${request.contextPath}/static/mui/js/common.js" async="async" defer="defer"></script>
<script>
mui.init();

$(function(){
	$('.mui-tab-item').on('touchstart', function(e){
		e.stopPropagation();
	});
	
	<#if isTips?default(false)>
		var resultCountMap = {};
		var resultNameMap = {};
		var resultIds = '${resultIds!}';
		var showNum = '${showNum!}';
		var hintContent='${hintContent!}';
		
		var jsonStringData=jQuery.parseJSON('${jsonStringData!}');
		var nameJson = jsonStringData.legendData;
	    var countJson=jsonStringData.loadingData;
	    
	    for(i=0;i<countJson.length;i++){
	   		resultNameMap[countJson[i].subjectId]=nameJson[i];
	    	resultCountMap[countJson[i].subjectId]=countJson[i].value;
		}
		
		if(resultIds!=''){
			if(resultCountMap[resultIds]!=null&&resultCountMap[resultIds]<showNum){
				$(".gk-course-tip2").text(hintContent).show();
			}
	    }
	</#if>	    
})
$('#cancelId').off('click').on('click',function(){
	var url = '${request.contextPath}/mobile/open/newgkelective/homepage?studentId=${studentId!}';
	loadByHref(url);
});

$('.js-reChoose').on('click',function(){
	var url = '${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/page?choiceId=${choiceId!}&toEdit=1';
	loadByHref(url);
});

 
$(".alertBtn").on("click",function(){
    layer.open({
	    type:1,
		title:false,
		content:$('#alertBtn'),
		area:'90%',
		btn:['知道了'],
		shadeClose:true,
		scrollbar:false
	});
});

</script>

</html>