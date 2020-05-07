<!doctype html>
<html>
<head>
<meta charset="utf-8">
<#-- 
<meta http-equiv="X-UA-Compatible" content="IE=11;IE=10;IE=9;IE=8;" />
  -->
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title><#if (sub_sys!) == "BASE">调课<#else>排课</#if></title>
<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/default.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/array.css">
</head>

<body class="fn-rel" data-sel="0" data-lock="0">
<div id="header" class="fn-clear">
	<p class="tt">手动调课</p>
    <div class="opt" <#if (sub_sys!) == "BASE">style="display:none;"</#if>>
       <#--  <a href="#">代课 &gt;</a>  -->
        <a href="#" id="btnSet">课程选项</a>
    </div>
    <div class="opt" <#if (sub_sys!) != "BASE" || fromAlone! != "1">style="display:none;"</#if>>
       <#--  <a href="#">代课 &gt;</a>  -->
        <a href="#" id="btnSet2" onclick=""><#-- 将本周课程应用至其他周  --> 应用本周课程至以后周</a>
    </div>
</div>

<div id="container" class="fn-clear">
	<div class="sidebar-left">
		<!-- tab切换 开始 -->
        <div>
            <div class="tabs_menu">
                <dl class="tab-menu fn-clear">
                    <dd <#if fromAlone! != "1" && sub_sys! == "BASE">style=" width:160px;"</#if> <#if type! == "C">class="active"</#if>><a href="#">班级</a></dd>
                    <#if !(fromAlone! != "1" && sub_sys! == "BASE")>
                    <dd <#if type! == "T">class="active"</#if>><a href="#">教师</a></dd>
						<#if !isXzb!false>
							<dd <#if type! == "P">class="active"</#if>><a href="#">场地</a></dd>
							<style>
								.tab-menu dd{width:53.3px;}
							</style>
						</#if>
					</#if>
                </dl>
            </div>
            <div class="tabs_box">
            	<#-- 班级课表 -->
                <div>
                    <div class="sidebar-filter">
			    		<p class="tt"><input type="text" class="txt" placeholder="班级查找"></p>
			            <div class="inner">
			                <p class="list">
					    	<#if classList?? && classList?size gt 0>
					    	<#list classList as class>
			                    <a href="#" data-class="${class.id!}">${class.className!}</a>
					    	</#list>
					    	</#if>
			                </p>
			            </div>
			    	</div>
			    	<ul class="tablist cl" style="overflow:auto;">
			    	<#if classList?? && classList?size gt 0>
		    		<#assign nowClassId = classList[0].id!>
			    	<#if (objId??)>
			    		<#assign nowClassId = objId!>
			    	</#if>
		    		<#assign lectureSurplus = lectureSurplus!>
			    	<#list classList as class>
			    		<li data-class="${class.id!}" <#if nowClassId == class.id!>class="current"</#if>>
			    			<a href="javascript:void(0);" onclick="manageClaorTea('${class.id!}','C')">
			    				<span class="fn-right mr-5 b c-red surp"><#if lectureSurplus[class.id!]?? && lectureSurplus[class.id] != 0>${lectureSurplus[class.id]}</#if></span>
			    				<span class="objName">${class.className!}</span>
			    			</a>
			    		</li>
			    	</#list>
			    	</#if>
			    	</ul>
                	<div class="coursetip">
			        	<div class="hd">提示信息</div>
			        	<div class="bd">
			        		<p>暂无</p>
			        	</div>
			        </div>
                </div>
                <#-- 教师课表 -->
                <div >
                	<div class="sidebar-filter">
			    		<p class="tt"><input type="text" class="txt" placeholder="教师查找"></p>
			            <div class="inner">
			                <p class="list">
					    	<#if teacherNameMap?? && teacherNameMap?size gt 0>
						    	<#list teacherNameMap?keys as tid>
				                    <a href="#" data-class="${tid!}">${teacherNameMap[tid]!}</a>
						    	</#list>
					    	</#if>
			                </p>
			            </div>
			    	</div>
			    	<ul class="tablist">
			    	<#if teacherNameMap?? && teacherNameMap?size gt 0>
			    	<#list teacherNameMap?keys as tid>
			    		<li data-class="${tid!}" <#if tid_index == 0>class="current"</#if>>
			    			<a href="javascript:void(0);" onclick="manageClaorTea('${tid!}','T')">
			    			<span class="objName">${teacherNameMap[tid]!}</span>
			    			</a>
			    		</li>
			    	</#list>
			    	<#else>
			    		<#-- <li data-class="c_101"><a href="#"><span class="fn-right mr-5 b c-red">5</span>14电气--test case</a></li>
			    		<li data-class="c_102"><a href="#">14机械 -test case</a></li>
			    		 -->
			    	</#if>
			    	</ul>
                	<div class="coursetip">
			        	<div class="hd">提示信息</div>
			        	<div class="bd">
			        		<p>暂无</p>
			        	</div>
			        </div>
			       
                </div>
                <#-- 场地课表 -->
                <div >  
                	<div class="sidebar-filter">
			    		<p class="tt"><input type="text" class="txt" placeholder="场地查找"></p>
			            <div class="inner">
			                <p class="list">
					    	<#if placeNameMap?? && placeNameMap?size gt 0>
						    	<#list placeNameMap?keys as pid>
				                    <a href="#" data-class="${pid!}">${placeNameMap[pid]!}</a>
						    	</#list>
					    	</#if>
			                </p>
			            </div>
			    	</div>
			    	<ul class="tablist">
			    	<#if placeNameMap?? && placeNameMap?size gt 0>
			    	<#list placeNameMap?keys as pid>
			    		<li data-class="${pid!}" <#if pid_index == 0>class="current"</#if>>
			    			<a href="javascript:void(0);" onclick="manageClaorTea('${pid!}','P')">
			    			<span class="objName">${placeNameMap[pid]!}</span>
			    			</a>
			    		</li>
			    	</#list>
			    	<#else>
			    		<#-- <li data-class="c_101"><a href="#"><span class="fn-right mr-5 b c-red">5</span>14电气--test case</a></li>
			    		<li data-class="c_102"><a href="#">14机械 -test case</a></li>
			    		 -->
			    	</#if>
			    	</ul>
                	<div class="coursetip">
			        	<div class="hd">提示信息</div>
			        	<div class="bd">
			        		<p>暂无</p>
			        	</div>
			        </div>
			       
                </div>
            </div>
        </div>
        <!-- tab切换 结束 -->
    </div>
	<div class="sidebar-right">
    	<p class="syllabus-tt"><span class="mr-5 ml-5">  </span></p>
        <table class="syllabus-table teacherTable" id="teacherTable1">
		    <#assign weekDays = (weekDays!7) - 1>
		    <#assign wratio = (100 - 5.5)/(weekDays+1)>
		    
        	<thead>
                <tr>
                  <th width="3%" class="sort">&nbsp;</th>
                    <th width="2.5%" class="sort">&nbsp;</th>
                    <#list 0..weekDays as day>
                    <th width="${wratio}%">${dayOfWeekMap[day+""]!}</th>
                    </#list>
                </tr>
            </thead>
            <tbody>
            <#list piMap?keys as piFlag>
			    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
			    <#assign interval = piMap[piFlag]>
			    <#assign intervalName = intervalNameMap[piFlag]>
			    <#list 1..interval as pIndex>
			    <tr>
			    <#if pIndex == 1>
			    	<td rowspan="${interval!}" class="sort"><p>${intervalName!}</p></td>
			    </#if>
		        	<td class="sort">${pIndex!}</td>
					<#list 0..weekDays as day>
					<td data-time="${day+"_"+piFlag+"_"+pIndex}">
						<span class="item" data-user="0"></span>
		            </td>
					</#list>
			    </tr>
			    </#list>
			    </#if>
		    </#list>
            </tbody>
        </table>
    	<p class="syllabus-tt"><span class="mr-5 ml-5"> </span></p>
        <table class="syllabus-table teacherTable" id="teacherTable2">
        	<thead>
                <tr>
                  <th width="3%" class="sort">&nbsp;</th>
                    <th width="2.5%" class="sort">&nbsp;</th>
                   	<#list 0..weekDays as day>
                    <th width="${wratio}%">${dayOfWeekMap[day+""]!}</th>
                    </#list>
                </tr>
            </thead>
            <tbody>
            	<#list piMap?keys as piFlag>
				    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
				    <#assign interval = piMap[piFlag]>
				    <#assign intervalName = intervalNameMap[piFlag]>
				    <#list 1..interval as pIndex>
				    <tr>
				    <#if pIndex == 1>
				    	<td rowspan="${interval!}" class="sort"><p>${intervalName!}</p></td>
				    </#if>
			        	<td class="sort">${pIndex!}</td>
						<#list 0..weekDays as day>
						<td data-time="${day+"_"+piFlag+"_"+pIndex}">
							<span class="item" data-user="0"></span>
			            </td>
						</#list>
				    </tr>
				    </#list>
				    </#if>
			    </#list>
            </tbody>
        </table>
    </div>
	<div class="content">
    	<div class="syllabus-tt <#if sub_sys! == "BASE">syllabus-tt2</#if> fn-clear">
			<p class="tips">
		    	<span class="lock">禁排课程</span>
		    	<span class="con">冲突课程</span>
		    	<span class="sel">选中课程</span>
		    	<span class="can">可选课程</span>
		    </p>
			<p class="tit"> 班级课表</p>
			<#if sub_sys! == "BASE">
			<div class="tit-info">
                <div class="fn-clear">
                	<a href="javascript:changeNum(this,-1);" class="abtn abtn-prev <#if weekList?size gt 0 && weekIndex?string == weekList[0]>abtn-no</#if>">&lt;</a>
                	<div class="ui-select-box fn-left mx-5" style="width:110px">
                        <input type="text" class="ui-select-txt" value="" />
                        <input type="hidden" value="" class="ui-select-value" />
                        <a class="ui-select-close"></a>
                        <div class="ui-option" >
                            <div class="a-wrap">
                            <#if weekList?? && weekList?size gt 0>
                            <#list weekList as week>
                                <a val="${week!}" <#if week! == weekIndex?string>class="selected"</#if>><span>第${week!}周</span></a>
                            </#list>
                            </#if>
                            </div>
                        </div>
                    </div>
                    <a href="javascript:changeNum(this,1);" class="abtn abtn-next <#if weekList?size gt 0 && weekIndex?string == weekList[weekList?size-1]>abtn-no</#if>">&gt;</a>
                    <#-- <a href="javascript:void(0);" onclick="coverAll()" class="abtn abtn-next">将本周课程应用至其他周</a> -->
                </div>
            </div>
			</#if>
		</div>
	<div class="content2">
		<table class="syllabus-table" id="allTable" data-sel="0">
			<thead>
		        <tr>
		          <th width="3%" class="sort">&nbsp;</th>
		            <th width="2.5%" class="sort">&nbsp;</th>
		            <#list 0..weekDays as day>
                    <th width="${wratio}%">${dayOfWeekMap[day+""]!}</th>
                    </#list>
		        </tr>
		    </thead>
		    <tbody>
		    <#list piMap?keys as piFlag>
			    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
			    <#assign interval = piMap[piFlag]>
			    <#assign intervalName = intervalNameMap[piFlag]>
			    <#list 1..interval as pIndex>
			    <tr>
			    <#if pIndex == 1>
			    	<td rowspan="${interval!}" class="sort"><p>${intervalName!}</p></td>
			    </#if>
		        	<td class="sort">${pIndex!}</td>
					<#list 0..weekDays as day>
					<td data-time="${day+"_"+piFlag+"_"+pIndex}">
						<span class="item" data-user="0"></span>
		            </td>
					</#list>
			    </tr>
			    </#list>
			    </#if>
		    </#list>
		    
		    </tbody>
		</table>
		<ul class="syllabus-from fn-clear">
			<li data-user="0" class="empty">
		    	<p class="item"><span></span></p>
		    </li>
		</ul>

    </div></div>
</div>

<div class="popUp-layer" id="setLayer" style="display:none;width:600px;">
	<p class="tt"><a href="#" class="close">关闭</a><span>课程调整选项</span></p>
	<div class="wrap array-set">
        <p class="tit"><span>是否可调</span></p>
        <div class="did fn-clear">
           <span class="ui-checkbox">
				<input type="checkbox" class="chk course-limit">启用课程禁排限制
			</span>
           <span class="ui-checkbox">
				<input type="checkbox" class="chk teacher-limit">启用老师禁排限制
			</span>
            <span class="ui-checkbox">
				<input type="checkbox" class="chk grade-limit">启用年级禁排限制
			</span>
            <#-- <span class="ui-checkbox"><input type="checkbox" class="chk">放开课程禁排限制</span>  -->
        </div>
    </div>
	<p class="dd">
		<a class="abtn-blue close" href="javascript:void(0);" onclick="saveLimitState()"><span>确定</span></a>
		<a class="abtn-blue close ml-5" href="javascript:void(0);" onclick="printLimitState()"><span>取消</span></a>
		<#-- <a class="abtn-blue close ml-5" href="#"><span>帮助</span></a>  -->
	</p>
</div>
<div class="popUp-layer" id="setLayer2" style="display:none;width:300px;">
	<p class="tt"><a href="#" class="close">关闭</a><span>扩展本周课表</span></p>
	<div class="wrap array-set">
        <p class="tit"><span>将会用本周的课表覆盖此班级其他周的课表<br>确定吗？</span></p>
    </div>
	<p class="dd">
		<a class="abtn-blue close" href="javascript:void(0);" onclick="coverAll()"><span>确定</span></a>
		<a class="abtn-blue close ml-5" href="javascript:void(0);" ><span>取消</span></a>
		<#-- <a class="abtn-blue close ml-5" href="#"><span>帮助</span></a>  -->
	</p>
</div>


<script type="text/javascript" src="${request.contextPath}/static/components/jquery-inputlimiter/jquery-inputlimiter/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/jquery.Layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/myscript-chkRadio.js"></script>
<#-- 
<script src="/static/components/layer/layer.js" async="async" defer="defer"></script>
<link rel="stylesheet" href="/static/components/layer/skin/layer.css">
 -->
<#-- /*********		业务 js 代码	*********/  -->
<script type="text/javascript">

var filter_cnt = [];
$('.sidebar-left .sidebar-filter .list').each(function(){
	filter_cnt.push($(this).html());
});

var classSubjTeachMap = {
	<#if classSubjTeacherMap?? && classSubjTeacherMap?size gt 0>
	<#list classSubjTeacherMap?keys as cid>
			<#if cid_index != 0>,</#if>
		<#assign classTeachers = classSubjTeacherMap[cid]!>
		"${cid!}":{
		<#if classTeachers?? && classTeachers?size gt 0>
		<#list classTeachers?keys as subjectId>
			<#if subjectId_index != 0>,</#if>
			"${subjectId!}":"${classTeachers[subjectId]!}"
		</#list>
		</#if>
		}
	</#list>
	</#if>
};

var classIdTypeMap = {
<#if allClassMap?? && allClassMap?size gt 0>
<#list allClassMap?keys as cid>
	<#if cid_index != 0>,</#if>
	"${cid!}":"${allClassMap[cid].classType!}"
</#list>
</#if>
};
var classNameMap = {
<#if allClassMap?? && allClassMap?size gt 0>
<#list allClassMap?keys as cid>
	<#if cid_index != 0>,</#if>
	"${cid!}":"${allClassMap[cid].className!}"
</#list>
</#if>
};
var classOldIdMap = {
<#if allClassMap?? && allClassMap?size gt 0>
<#list allClassMap?keys as cid>
	<#if cid_index != 0>,</#if>
	"${cid!}":"${allClassMap[cid].oldDivideClassId!}"
</#list>
</#if>
};
var teacherNameMap = {
<#if teacherNameMap?? && teacherNameMap?size gt 0>
<#list teacherNameMap?keys as tid>
	<#if tid_index != 0>,</#if>
	"${tid!}":"${teacherNameMap[tid]!}"
</#list>
</#if>
};
var classPlaceMap = {
<#if classPlaceMap?? && classPlaceMap?size gt 0>
<#list classPlaceMap?keys as cid>
	<#if cid_index != 0>,</#if>
	"${cid!}":"${classPlaceMap[cid]!}"
</#list>
</#if>
};
var placeNameMap = {
<#if placeNameMap?? && placeNameMap?size gt 0>
<#list placeNameMap?keys as pid>
	<#if pid_index != 0>,</#if>
	"${pid!}":"${placeNameMap[pid]!}"
</#list>
</#if>
};
var noNeedPlaceCourse = [
<#if noNeedPlaceCourse?? && noNeedPlaceCourse?size gt 0>
<#list noNeedPlaceCourse as code>
	<#if code_index != 0>,</#if>"${code!}"
</#list>
</#if>
];
var gradeNoClick = [
<#if gradeNoClick?? && gradeNoClick?size gt 0>
<#list gradeNoClick as timestr>
	<#if timestr_index != 0>,</#if>"${timestr!}"
</#list>
</#if>
];
var xzbPeriodTab = null;
<#if xzbPeriodList?? && xzbPeriodList?size gt 0>
xzbPeriodTab = {
<#list xzbPeriodList as timestr>
	<#if timestr_index != 0>,</#if>"${timestr!}":"${timestr!}"
</#list>
};
</#if>
var pure3XzbIdTab = {
<#if pure3XzbIds?? && pure3XzbIds?size gt 0>
<#list pure3XzbIds as xzbId>
	<#if xzbId_index != 0>,</#if>"${xzbId!}":"${xzbId!}"
</#list>
</#if>
};
var teacherNoTimeObj = {
<#if teacherNoTimeMap?? && teacherNoTimeMap?size gt 0>
<#list teacherNoTimeMap?keys as tid>
	<#if tid_index != 0>,</#if>
	"${tid!}":"${teacherNoTimeMap[tid]!}"
</#list>
</#if>
};
var classMovePeriodMap = {
<#if classMovePeriodMap?? && classMovePeriodMap?size gt 0>
<#list classMovePeriodMap?keys as cid>
	<#if cid_index != 0>,</#if>
	<#assign times = classMovePeriodMap[cid]![]>
	"${cid!}":[
	<#list times as time>
		<#if time_index != 0>,</#if>"${time!}"
	</#list>]
</#list>
</#if>
};
var teacherMoveTimeMap = {
<#if teacherMoveTimeMap?? && teacherMoveTimeMap?size gt 0>
<#list teacherMoveTimeMap?keys as tid>
	<#if tid_index != 0>,</#if>
	<#assign times = teacherMoveTimeMap[tid]![]>
	"${tid!}":[
	<#list times as time>
		<#if time_index != 0>,</#if>"${time!}"
	</#list>]
</#list>
</#if>
};
var placeMoveTimeMap = {
<#if placeMoveTimeMap?? && placeMoveTimeMap?size gt 0>
<#list placeMoveTimeMap?keys as pid>
	<#if pid_index != 0>,</#if>
	<#assign times = placeMoveTimeMap[pid]![]>
	"${pid!}":[
	<#list times as time>
		<#if time_index != 0>,</#if>"${time!}"
	</#list>]
</#list>
</#if>
};
var combineRelations = [];
<#if combineRelations?? && combineRelations?size gt 0>
<#list combineRelations as group>
	combineRelations.push({
	<#list group as clsSub>
		<#if clsSub_index !=0>,</#if>"${clsSub}":"${clsSub}"
	</#list>
	});
</#list>
</#if>

var arrayJsonData = JSON.parse('${jsonDataStr?default("{}")}');
var objArrayIdMap = arrayJsonData['objArrayIdMap'];
var arrayIds = arrayJsonData['arrayIds'];
var arrayIdParams = (function(){
	var ss = [];
	if(!arrayIds){
		return "arrayIds=";
	}
	for (var i = 0; i <arrayIds.length ; i++) {
		ss.push("arrayIds="+arrayIds[i]);
	}
	return ss.join("&");
})();


$(function(){
	$(".coursetip").hide();
	function layout(){
		console.log("layout");
		var sidebarrightwidth = Math.ceil(document.documentElement.clientWidth * 0.28);
		var marginright = sidebarrightwidth + 3
		$(".sidebar-right").css("width",sidebarrightwidth);
		$(".content").css("margin-right",marginright);
	};
	layout();
	winLoad2();
	window.onresize = function(){  
       layout();
    };
	var index = $(".tabs_menu > dl > dd.active").index();
	$(".tabs_box > div").eq(index).find(".tablist .current a").click();
	<#if sub_sys! == "BASE" && fromAlone! == "1">
	getSurplusInf();
	</#if>
	// 效果 js  代码
	layer('#btnSet','#setLayer','.close','标题1');
	layer('#btnSet2','#setLayer2','.close','标题2');
	//tab切换
	var $div_li1=$(".tabs_menu > dl > dd");
	$div_li1.click(function(){
		var needRefresh =  !$(this).hasClass("active");
	
		$(this).addClass("active").siblings().removeClass("active");
		var index=$div_li1.index(this);
		$(".tabs_box > div").eq(index).show().siblings().hide();
		if(needRefresh)	refreshTab();
	});
	
	printLimitState();
});

function refreshTab(useMaster){
	//console.log("---刷新----");
	var index = $(".tabs_menu > dl > dd.active").index();
	var cid = $(".tabs_box > div").eq(index).find(".tablist .current").attr("data-class");
	//console.log("index = "+index);
	
	if(index==0){
		if(cid) manageClaorTea(cid,"C",useMaster);
	}
	else if(index == 1){
		if(cid) manageClaorTea(cid,"T",useMaster);
	}else{
		if(cid) manageClaorTea(cid,"P",useMaster);
	}
}

var indexId = "${indexId!}";
var contextPath = "${request.contextPath}";
var sub_sys = "${sub_sys!}";
var url_sub_sys = <#if sub_sys! == "BASE">"basedata"<#else>"newgkelective"</#if>
var searchAcadyear = "${searchAcadyear!}";
var searchSemester = "${searchSemester!}";
var fromAlone = "${fromAlone!}";

// 获取班级 课表或者教师课表
function manageClaorTea(id,type, useMaster){ // classId | teacherId
	var param = "cType="+type+"&objId="+id+"&time="+new Date().getTime();
	if(!useMaster){
		useMaster = false;
	}
	param += "&useMaster="+useMaster+"&"+arrayIdParams;
	<#if sub_sys! == "BASE">
	//var searchAcadyear = "${searchAcadyear!}";
	//var searchSemester = "${searchSemester!}";
	var searchWeek = $(".ui-select-value").val();
	if(!searchWeek){
		alert("周次为空，请先维护基础信息节假日设置");
		return;
	}
	
	param += "&searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&weekIndex="+searchWeek+"&fromAlone="+fromAlone;
	</#if>
	var indexId = objArrayIdMap[id];
	var url = '${request.contextPath}/'+url_sub_sys+'/scheduleModify/mutil/'+indexId+'/queryClassOrTeacherTable/page?';
	//console.log(url+param);
	$(".content2").load(url+param);
	
	
	$('body').attr('data-sel',0);
	$('body .cln-item').remove();
	$('.syllabus-from li.sel').removeClass('sel');
	
	//移除插入的元素
	$('.syllabus-table td').removeClass('int-from-wrap int-to-wrap').children('.item').removeClass('item-can item-sel int-from int-to');
	$('body').attr('data-lock','0');
	$('body > .int-from,body > .int-to').remove();
	
	//$(document).click();
	return;
}
function changeNum(obj,addNum){
	var nowIndex = $(".ui-select-box .ui-option a.selected").index();
	var index = nowIndex + addNum;
	if(index<0){
		return;
	}
	var $dest =  $(".ui-select-box .ui-option a").eq(index);
	if(!$dest[0])
		return;
	
	var $val_txt=$dest.text();
	var $val_zhi=$dest.attr("val");
	$dest.parents('.ui-option').siblings(".ui-select-txt").val($val_txt);
	$dest.parents('.ui-option').siblings(".ui-select-value").val($val_zhi);
	$dest.addClass("selected").siblings("a").removeClass("selected");
	
	var weekIndex = $dest.attr("val");
	var tx = $dest.text();
	//console.log("week = "+weekIndex+"  tx = "+tx);
	//$(".ui-select-txt").val(tx);
	//$(".ui-select-value").val(weekIndex);
	
	refeshArrow();
	refreshTab();
}
function refeshArrow(){
	var index = $(".ui-select-box .ui-option a.selected").index();
	var length = $(".ui-select-box .ui-option a").length;
	if(index == 0){
		$(".abtn-prev").addClass("abtn-no");
	}else{
		$(".abtn-prev").removeClass("abtn-no");
	}
	if(index == (length-1)){
		$(".abtn-next").addClass("abtn-no");
	}else{
		$(".abtn-next").removeClass("abtn-no");
	}
	<#if (sub_sys!) == "BASE" && fromAlone! == "1">
	getSurplusInf();
	</#if>	
}
$(".ui-option a").click(function(){
	var $val_txt=$(this).text();
	var $val_zhi=$(this).attr("val");
	$(this).parents('.ui-option').siblings(".ui-select-txt").val($val_txt);
	$(this).parents('.ui-option').siblings(".ui-select-value").val($val_zhi);
	$(this).addClass("selected").siblings("a").removeClass("selected");
	
	refeshArrow();
	refreshTab();
});

function updateClassSurplus(cid,addNum){
	var $span = $("[data-class='"+cid+"'] span.surp");
	var su = 0;
	if($span.text())
		su = parseInt($span.text());
	su += addNum;
	if(su == 0)
		$span.text('');
	else
		$span.text(su);
}
// 更新 各个模块的 走班禁排时间
function updateMovePerios(){

	<#if sub_sys! == "BASE">return;</#if>
	var url = '${request.contextPath}/'+url_sub_sys+'/scheduleModify/mutil/${indexId!}/updateMovePeriods';
	$.ajax({
		url: url,
		type: "POST",
		data: {"indexId":"${indexId!}"},
		dataType: "JSON",
		success:function(data){
			// 场地 调了之后 执行此操作
			if(data.success){
				//更新 时间
				teacherMoveTimeMap = data["teacherMoveTimeMap"];
				placeMoveTimeMap = data["placeMoveTimeMap"];
				classMovePeriodMap = data["classMovePeriodMap"];
			}else{
				console.log("更新movePeriods失败: "+data.msg);
			}
		},
		error:function(){}
	});
}
// 将本周课程应用至以后周
function coverAll(){
	var searchWeek = $(".ui-select-value").val();
	if(!searchWeek){
		alert("周次为空，请先维护基础信息节假日设置");
		return;
	}
	if(!objId){
		alert("请先选择班级");
		return;
	}
	
	var url = '${request.contextPath}/'+url_sub_sys+'/scheduleModify/mutil/${indexId!}/coverAllWeek';
	
	var	params = "searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&weekIndex="+searchWeek+"&classId="+objId;
	$.ajax({
		url: url,
		type: "POST",
		data: params,
		dataType: "JSON",
		success:function(data){
			if(data.success){
				autoTips2("操作成功");
			}else{
				autoTips2(""+data.msg);
			}
		},
		error:function(){}
	});
	
}

//未排课程取消选中效果
$('body').on('click','.cln-item',function(){
	//console.log("6");
	$(this).remove();
	$('body').attr('data-sel',0);
	$('#allTable span.item').removeClass('item-can');
	$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
		$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
	    //console.log("取消科目禁排时间");
	});
	freshFixedNotime();
	$('.syllabus-from li.sel').removeClass('sel');
	clearTeaTable("2");
	$('#allTable').off('click');
});
$(".syllabus-tt").off("click").on("click",".tab",function(){
	//console.log("-- 切换 --");
	
	var tid = $(this).attr("data-tid");
	if($(this).hasClass("tab") && !$(this).hasClass("active")){
		var id = $(this).parent("p").next("table").attr("id");
		
		var scheduleObj = teacherTables[id];
		var schedules = scheduleObj[tid];
		
		insScheduleOnly(id,schedules);
		
		$(this).siblings(".tab").removeClass("active switch");
		$(this).addClass("active switch");
	}else{
		// 切换课表
		//switchTable(tid);
	}
});
$('.syllabus-tt').on('dblclick','.switch',function(){
	var oid = $(this).attr("data-tid");
	//console.log("oid = "+oid);
	switchTable(oid);
	
});
function switchTable(oid){
	var $li = $(".tablist li[data-class='"+oid+"']");
	$li.addClass('current').siblings('li').removeClass('current');
	var index = $li.parent("ul").parent("div").index();
	
	if(index < 0) return;
	//console.log("-----switch---- index = "+index);
	$(".tab-menu dd").eq(index).click();
	
	var order_index= $li.index();
	var pos_top=order_index*40;
	$('.sidebar-left ul').scrollTop(pos_top);
}

var course_limit = true;
var grade_limit = true;
var teacher_limit = true;
function printLimitState(){
	$("#setLayer .course-limit").prop("checked",course_limit);
	$("#setLayer .teacher-limit").prop("checked",teacher_limit);
	$("#setLayer .grade-limit").prop("checked",grade_limit);
	
	$("input.chk").each(function(i,obj){
		if($(obj).prop("checked")) $(obj).parent(".ui-checkbox").addClass("ui-checkbox-current");
	});
}
function saveLimitState(){
	course_limit = $("#setLayer .course-limit").prop("checked");
	teacher_limit = $("#setLayer .teacher-limit").prop("checked");
	grade_limit = $("#setLayer .grade-limit").prop("checked");
	
	freshGradeNoClick();
	//console.log(course_limit+"  "+teacher_limit+"  "+grade_limit);
}
// 获取班级 未排课 课时数，并更新
function getSurplusInf(){
	var searchWeek = $(".ui-select-value").val();
	if(!searchWeek){
		alert("周次为空，请先维护基础信息节假日设置");
		return;
	}
	
	var url = '${request.contextPath}/'+url_sub_sys+'/scheduleModify/mutil/${indexId!}/classSurplusHour';
	
	var	params = "searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&weekIndex="+searchWeek;
	$.ajax({
		url: url,
		type: "POST",
		data: params,
		dataType: "JSON",
		success:function(data){
			if(data.success){
				//autoTips2("操作成功");
				// TODO  
				updateSurplus(data.map);
			}else{
				autoTips2("更新未安排课时失败 "+data.msg);
			}
		},
		error:function(){alert("异常");}
	});
}
function updateSurplus(surplusMap){
	$("ul.tablist.cl li").each(function(i,obj){
		var cid = $(obj).attr("data-class");
		if(surplusMap[cid] && surplusMap[cid]!=0){
			$(obj).find("span.surp").text(surplusMap[cid]);
		}else{
			$(obj).find("span.surp").text("");
		}
	});
}

function hideOffday(tabid){
	//console.log("hide off day"+$("#"+tabid).length);
	$("#"+tabid+"").find("tr").each(function(i,obj){
		//console.log($(obj).html());
		if($(obj).find("rowspan").length>0){
			$(obj).find("td:eq(7),td:eq(8)").hide();
		}else{
			$(obj).find("td:eq(6),td:eq(7)").hide();
		}
		if($(obj).find("th").length>0)
			$(obj).find("th:eq(7),th:eq(8)").hide();
	});
	winLoad2();
}
function clearTeaTable(lid){
	var $table = $("#teacherTable"+lid);
	$table.find("tbody .item").each(function(){
		$(this).html('');
		$(this).attr('data-user','0');
	});
	$table.prev("p").html("");
}
function winLoad2(){
	
	var all_w=$(window).width();
	var all_h=$(window).height();
	var header_h=$('#header').height();
//		var row_len=$('.content .syllabus-table tr:last').find('.sort').text();//总行数-除头、分隔行等
	var row_len=$('.content .syllabus-table tbody tr').length;//总行数-除头、分隔行等
	var col_len=$('.content .syllabus-table th').length-2;//总列数-除序号列
	var con_tt_h=$('.content .syllabus-tt').outerHeight();//总课表-表头
	var con_th_h=$('.content .syllabus-table th').height();//总课表-th
	var con_from_h=$('.content .syllabus-from').outerHeight();//总课表-未排除
	<#if fromAlone! != "1" && (sub_sys!) == "BASE">con_from_h = 2;</#if>
	var con_line_len=$('.content .syllabus-table td.line').length;
	var con_line_h=con_line_len*10;//总课表-分隔行  每行高度10
	var con_all_h=all_h-header_h-con_tt_h-con_th_h-con_from_h-con_line_h;//总课表-剩余总高度
	var con_all_w=$('.content').width()-68;//总课表-剩余总宽度 序号列宽30
	var con_item_h=parseInt(con_all_h/row_len)-3;//总课表-每个单元格高度
	if($('#container').hasClass('daike-container')){
		con_item_h=con_item_h-10;
	};
	var con_item_w=parseInt(con_all_w/col_len);//总课表-每个单元格宽度
	
	//总课表-每个单元格赋值
	$('.content .syllabus-table td .item').each(function(){
        $(this).css({width:con_item_w,height:con_item_h});
		var room_text=$(this).find('span.room').text();
		var otherroom_text=$(this).find('span.otherRoom').text();
		var course=$(this).find('.course');
		var name=$(this).find('.name');
		var room=$(this).find('.room');
		var other_course=$(this).find('.otherCourse');
		var other_name=$(this).find('.otherName');
		var other_room=$(this).find('.otherRoom');
		var type=$(this).find('.type');
		
		var my_len=3;
		if(room_text=='' && otherroom_text==''){
			my_len=my_len-1;
		};
		var my_h=parseInt(con_item_h/my_len);
		var type_top=parseInt((my_h - 15)/2);
		if(my_h>=14){
			course.css({height:my_h,'line-height':my_h+'px'});
			name.css({height:my_h,'line-height':my_h+'px'});
			room.css({height:my_h,'line-height':my_h+'px'});
			other_course.css({height:my_h,'line-height':my_h+'px'});
			other_name.css({height:my_h,'line-height':my_h+'px'});
			other_room.css({height:my_h,'line-height':my_h+'px'});
			type.css({'top':'-'+type_top+'px'});
		};
    });
	
	var side_row_len=row_len*2;
	var side_wrap_h=$('.sidebar-right-wrap').height();//教师选择高度
	var side_tt_h=$('.sidebar-right .syllabus-tt').height()*2;//教师课表表头 全双倍
	var side_th_h=$('.sidebar-right .syllabus-table th').height()*2;//教师课表th
	var side_remark_h=$('.sidebar-right .syllabus-remark').outerHeight();//教师课表备注
	var side_line_len=$('.sidebar-right .syllabus-table td.line').length*2;
	var side_line_h=side_line_len*3;//教师课表分隔行  每行高度3
	var side_all_h=all_h-header_h-side_wrap_h-side_tt_h-side_th_h-side_remark_h-side_line_h;//教师课表剩余总高度
	var side_all_w=$('.sidebar-right').width()-60;//教师课表剩余总宽度 序号列宽22
	var side_item_h=parseInt(side_all_h/side_row_len)-4;//教师课表每个单元格高度
	if($('#container').hasClass('daike-container')){
		side_item_h=(side_item_h+4)*2;
	};
	var side_item_w=parseInt(side_all_w/col_len);//教师课表每个单元格宽度
	
	//教师课表-每个单元格赋值
	$('.sidebar-right .syllabus-table td .item').each(function(){
        $(this).css({width:side_item_w,height:side_item_h});
//			var room_text=$(this).children('span.room').text();
		var $span=$(this).children('span');
		var my_len=1;
//			if(room_text==''){
//				my_len=my_len-1;
//			};
		if($(this).find(".odd-even").length>0){
			var my_len=2;
		}
		var my_h=parseInt(side_item_h/my_len);
		if(my_h>=12){
			$span.css({height:my_h,'line-height':my_h+'px'});
		};
    });
	
	//总框架
	var con_h=all_h-header_h;
	$('.sidebar-left,.sidebar-right,.content').height(con_h);
	var divid = 373
	divid = 80;
	$('.sidebar-left ul').height(con_h-divid);
};
function autoTips2(txt){
	//alert(); 
	var win_width=parseInt($(window).width());
	var win_height=parseInt($(window).height());
	$('body .auto-layer-mask').remove();//先清除？？？？
	$('body').prepend('<div class="auto-layer-mask" style="height:'+win_height+'px"><div class="auto-layer">'+txt+'</div></div>');
	//$('body').prepend('<div class="auto-layer">'+txt+'</div>');
	var layer_width=$('.auto-layer').outerWidth();
	var layer_height=$('.auto-layer').outerHeight();
	var layer_left=parseInt((win_width-layer_width)/2);
	var layer_top=parseInt((win_height-layer_height)/2);
	$('.auto-layer').css({'top':layer_top,'left':layer_left}).stop(true).fadeIn(300).delay(1200).fadeOut(1000,function(){
		$('.auto-layer-mask').fadeOut(300).remove();
	});
};
window.console = window.console || (function () {
    var c = {}; c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile
    = c.clear = c.exception = c.trace = c.assert = function () { };
    return c;
})(); 
</script>
</body>
</html>