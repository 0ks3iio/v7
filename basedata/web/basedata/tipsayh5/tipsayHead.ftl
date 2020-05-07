<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>代课管理</title>
	<link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/iconfont.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.picker.min.css"/>
	<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/mui.picker.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/storage.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
</head>

<body>
	<header class="mui-bar mui-bar-nav" style="height:0px;display:none;">
	    <a class="mui-btn-close">关闭</a>
	    <h1 class="mui-title">代课管理</h1>
	</header>
	<div style="display:none;">
		<form id="_macro_form_id" method="post">
		</form>
	</div>
	<div class="mui-content" style="padding-bottom: 51px;padding-top:5px;">
    	<div class="chooseBtn  fn-flex">
    		<input type="hidden" id="acadyear" value="">
    		<input type="hidden" id="semester" value="">
    		<input type="hidden" id="week" value="">

			<a id="acadyearText" class="mui-btn">未设置</a>
			<span class="mui-icon mui-icon-arrowdown fn-flex-auto"></span>
			<a id="semesterText" class="mui-btn">未设置</a>
			<span class="mui-icon mui-icon-arrowdown fn-flex-auto"></span>
			<a id="weekText" class="mui-btn">未设置</a>
			<span class="mui-icon mui-icon-arrowdown fn-flex-auto"></span>
		
			
    	</div>
    	<div id="tipsayListDiv">
    	</div>
    </div>
    <#if adminType?default('')=='1'>
    <nav class="mui-bar mui-bar-tab">
        <a class="mui-tab-item mui-active f-16 add_tapsay" href="javascript:">+ 新增代课</a>
    </nav>
    </#if>
</body>
<script type="text/javascript" charset="utf-8">
	mui.init();
	
    (function($, doc) {
    	var yearDataArr=new Array();
    	var ii=0;
    	var chooseiiAcad=0;
    	<#if acadyearList?exists && acadyearList?size gt 0>
    		<#list acadyearList as acad>
    			yearDataArr[ii]={"value":"${acad!}","text":"${acad!}学年"};
    			<#if semesterObj.acadyear?default('')==acad>
    				chooseiiAcad=ii;
    			</#if>
    			ii++;
    		</#list>
    	<#else>
    		yearDataArr[0]={"value":"","text":"未设置"};
    	</#if>
    	
    	var semesterDataArr=[{"value":"1","text":"第一学期"},{"value":"2","text":"第二学期"}];
   		var chooseiiSemes=0;
   		<#if semesterObj.semester?default(1)==1>
   			chooseiiSemes=0;
   		<#else>
   			chooseiiSemes=1;
   		</#if>
   		
   		ii=0;
   		var chooseiiWeek=0;
   		var weekDataArr=new Array();
   		<#if weekList?exists && weekList?size gt 0>
    		<#list weekList as week>
    			weekDataArr[ii]={"value":"${week!}","text":"第${week!}周"};
    			<#if nowWeek==week>
    				chooseiiWeek=ii;
    			</#if>
    			ii++;
    		</#list>
    	<#else>
    		weekDataArr[0]={"value":"","text":"未设置"};
    	</#if>
    	//初始化
    	var year = doc.getElementById('acadyearText');
    	year.innerHTML = yearDataArr[chooseiiAcad].text;
    	doc.getElementById('acadyear').value=yearDataArr[chooseiiAcad].value;
    	
    	var semester = doc.getElementById('semesterText');
    	semester.innerHTML = semesterDataArr[chooseiiSemes].text ;
    	doc.getElementById('semester').value=semesterDataArr[chooseiiSemes].value;
    	
    	
    	var week = doc.getElementById('weekText');
    	week.innerHTML = weekDataArr[chooseiiWeek].text;
    	doc.getElementById('week').value=weekDataArr[chooseiiWeek].value;
    	
    	changeWeek();
		$.ready(function() {
			//学年
			var yearPicker = new $.PopPicker();
			yearPicker.setData(yearDataArr);
			yearPicker.pickers[0].setSelectedValue(yearDataArr[chooseiiAcad].value, 2000);
			
			year.addEventListener('tap', function(event) {
				yearPicker.show(function(items) {
					year.innerHTML = items[0].text;
					doc.getElementById('acadyear').value=items[0].value;
					changeSemester();
					//返回 false 可以阻止选择框的关闭
					//return false;
				});
			}, false);
			
			//学期
			var semesterPicker = new $.PopPicker();
			semesterPicker.setData(semesterDataArr);
			semesterPicker.pickers[0].setSelectedValue(semesterDataArr[chooseiiSemes].value, 2000);
			semester.addEventListener('tap', function(event) {
				semesterPicker.show(function(items) {
					semester.innerHTML = items[0].text;
					doc.getElementById('semester').value=items[0].value;
					changeSemester();
				});
			}, false);
			
			//周次
			var weekPicker = new $.PopPicker();
			weekPicker.setData(weekDataArr);
			
			week.addEventListener('tap', function(event) {
				weekPicker.show(function(items) {
					week.innerHTML = items[0].text;
					doc.getElementById('week').value=items[0].value;
					changeWeek();
				});
			}, false);
		});
	})(mui, document);
	
	function changeSemester(){
		var acadyear=document.getElementById('acadyear').value;
		var semester=document.getElementById('semester').value;
		var week=document.getElementById('week').value;
		var parm='&acadyear='+acadyear+'&semester='+semester+'&week='+week;
		var url = '${request.contextPath}/mobile/open/tipsay/head/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}'+parm;
		loadByHref(url);
	}
	function changeWeek(){
		var acadyear=document.getElementById('acadyear').value;
		var semester=document.getElementById('semester').value;
		var week=document.getElementById('week').value;
		var parm='&acadyear='+acadyear+'&semester='+semester+'&week='+week;
		var url = '${request.contextPath}/mobile/open/tipsay/list/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}'+parm;
		$("#tipsayListDiv").load(url);
	}
	<#if adminType?default('')=='1'>
	$(".add_tapsay").on('tap',function(){
		var acadyear=document.getElementById('acadyear').value;
		var semester=document.getElementById('semester').value;
		var week=document.getElementById('week').value;
		if(week==""){
			toastMsg("请先去节假日设置");
			return;
		}
		var parm='&acadyear='+acadyear+'&semester='+semester+'&week='+week;
		var url = '${request.contextPath}/mobile/open/tipsay/addTipsay/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}'+parm;
		loadByHref(url);
	})
	</#if>
	
</script>
</html>