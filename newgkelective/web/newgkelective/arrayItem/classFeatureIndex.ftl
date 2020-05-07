<script>
var classCourseObj = {
		<#if classXzbCourseMap?? && classXzbCourseMap?size gt 0>
		<#list classXzbCourseMap?keys as classId>
		<#if classId_index != 0>,</#if>
		"${classId}":[
			<#list classXzbCourseMap[classId] as course>
				<#if course_index != 0>,</#if>
				{subjectId:"${course.subjectId!}",subjectType:"${course.subjectType!}",subjectName:"${course.subjectName!}"}
			</#list>
		]
		</#list>
		</#if>
	};
var classCouTeaObj = {
		<#if classXzbCourseMap?? && classXzbCourseMap?size gt 0>
		<#list classXzbCourseMap?keys as classId>
		<#if classId_index != 0>,</#if>
		<#if classSubjTeacherMap[classId]?exists>
		<#assign tMap = classSubjTeacherMap[classId]>
		"${classId}":{
			<#list classXzbCourseMap[classId] as course>
				<#if course_index != 0>,</#if>
				"${course.subjectId!}":{teacherId:"${tMap[course.subjectId!]!}",teacherName:"${teacherNameMap[tMap[course.subjectId!]!]!}"}
			</#list>
		}
		<#else>
		"${classId}":{
			<#list classXzbCourseMap[classId] as course>
				<#if course_index != 0>,</#if>
				"${course.subjectId!}":{teacherId:"",teacherName:""}
			</#list>
		}
		</#if>
		</#list>
		</#if>
	};
var classNameTab = {
	<#if allClassList?? && allClassList?size gt 0>
	<#list allClassList as c>
		<#if c_index != 0>,</#if>
		"${c.id!}":"${c.className!}"
	</#list>
	</#if>
	};
var fakeXzbArr = [];
<#if fakeXzbList?? && fakeXzbList?size gt 0>
<#list fakeXzbList as c>
	fakeXzbArr.push("${c.id!}");
</#list>
</#if>


var copyItemClass = '';
<#if xzbClassList?exists && xzbClassList?size gt 0 >
    <#list xzbClassList as c>
    	copyItemClass +=  '<li id="class_${c.id!}" class="courseLi"><a href="#aaa_${c.id!}" data-value="${c.id!}">${c.className!}'
    	+ '<span class="badge badge-default"></span></a></li>';
    </#list>
</#if>

var xzbMoveCountMap= {};
<#if clsWorkTimeMap?exists && clsWorkTimeMap?size gt 0 >
<#list clsWorkTimeMap?keys as cid>
	xzbMoveCountMap["${cid!}"] = ${clsWorkTimeMap[cid][1]!0};
</#list>
</#if>
var xzbAllCountMap= {};
<#if clsWorkTimeMap?exists && clsWorkTimeMap?size gt 0 >
<#list clsWorkTimeMap?keys as cid>
	xzbAllCountMap["${cid!}"] = ${clsWorkTimeMap[cid][0]!0};
</#list>
</#if>
var firstsdSubjectMap = {};
<#if firstsdSubjectMap?exists && firstsdSubjectMap?size gt 0 >
<#list firstsdSubjectMap?keys as code>
	firstsdSubjectMap["${code!}"] = "${firstsdSubjectMap[code]!}";
</#list>
</#if>

// 复制到-> 班级课程
var classCourseHtml = buildClassCourseHtml();

function buildClassCourseHtml(){
	var bcsh = '';
	for(cid in classNameTab){
		var courses = classCourseObj[cid];
		 bcsh += '<div id="aaa_'+ cid +'"  data-value="'+ cid +'">\
					<div class="form-title ml-15 mt10 mb10">'+ classNameTab[cid] +'<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> <a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> </div>\
					<ul class="gk-copy-list">';
		if(courses && courses.length > 0){
			for(i in courses){
				var code = cid+ '-'+ courses[i].subjectId + '-' + courses[i].subjectType;
				var cn = courses[i].subjectName;
				bcsh +=	'<label class="mr20"><input type="checkbox" class="wp" name="copyTeacher" value="'+ code +'" data-value="'+ cn +'"><span class="lbl"> '+ cn +'</span></label>';
			}
		}
		bcsh += '</ul></div>';
	}
	return bcsh;
}

var arrayItemId = '${arrayItemId}';
function queryClass(newClassId){
	//console.log("query for class :"+newClassId);
	if(!newClassId){
		alerty("请输入正确的班级id");
		return;
	}
	
	//先还原课时数量
	var classId = $(".tree-list a.active").attr("id");
	$(".tree-list #"+classId+" .lecCount").text(xzbAllCountMap[classId]);
	
	var url = "${request.contextPath}/newgkelective/"+arrayItemId+"/classFeature/one?classId="+newClassId+"&divideId="+divideId;
	//console.log(url);
	$("#subjectItemList").load(url);
	
	$(".tree-list a").removeClass("active");
	$("#"+newClassId).addClass("active");
}

// 本地搜索 班级
var initClasss =  $(".tree-list a");
function searchClass(){
	var s = new String($("#searchKey").val());
	//console.log("s = "+s);
	if(!s){
		//console.log("s = "+s);
		$(".tree-list").html();
		$(".tree-list").append(initClasss);
	}
	
	var ct = $("<div></div>");
	initClasss.each(function(i,obj){
		if($(obj).html().indexOf(s) > -1){
			ct.append(obj);
		}
	});
	$(".tree-list").html(ct.html());
}

$('#searchKey').keydown(function(event){
    if(event.keyCode==13){
    	searchClass();
    }
});


var $window = $(window).height();
$(".tree-list").height($window-385);

$(function(){
	if($(".tree-list a.active")[0]){
		$(".tree-list a.active")[0].scrollIntoView();
        window.location.href=$(".tree-list a.active")[0].href;
    }
});
</script>

<div class="clearfix">
	<div class="tree-wrap">
		<h4>选择班级</h4>
		<div class="input-group">
			<input type="text" id="searchKey" class="form-control">
			<span class="input-group-addon" onclick="searchClass()" style="cursor:pointer;">
				<a href="javascript:void(0);" ><i class="fa fa-search"></i></a>
			</span>
		</div>
		<div class="tree-list" style="margin-top: 0;">
		<#if xzbClassList?? && xzbClassList?size gt 0>
		<#list xzbClassList as c>
			<a href="javascript:queryClass('${c.id!}');" id="${c.id!}" <#if (!classId?? && c_index == 0) || (classId?? && classId == c.id)>class="active"</#if>  >
				${c.className!} <span class="badge badge-default pull-right lecCount">${clsWorkTimeMap[c.id][0]!}</span>
			</a>
		</#list>
		</#if>
		<#if fakeXzbList?? && fakeXzbList?size gt 0>
		<#list fakeXzbList as c>
			<a href="javascript:queryClass('${c.id!}');" id="${c.id!}" <#if (!classId?? && c_index == 0) || (classId?? && classId == c.id)>class="active"</#if>  >
				${c.className!} <#if clsWorkTimeMap[c.id]??><span class="badge badge-default pull-right lecCount">${clsWorkTimeMap[c.id][0]!}</span></#if>
			</a>
		</#list>
		</#if>
		</div>
	</div>
	<#-- 补充内容 -->
	<div id="subjectItemList"></div>
</div>
