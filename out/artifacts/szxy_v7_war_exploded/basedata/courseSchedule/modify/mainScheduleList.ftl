<table class="syllabus-table" id="allTable" data-sel="0">
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
		    <#assign lineScheduleMap = scheduleMap[piFlag +'-'+ pIndex]!>
			<#list 0..weekDays as day>
				<#assign schedules = lineScheduleMap[day+'']!>
			<td data-time="${day+"_"+piFlag+"_"+pIndex}">
				<#if schedules?? && schedules?size gt 1>
				<#-- 单双周 -->
				
            	<span class="item" data-user="${(schedules[0].classId+"_"+schedules[0].subjectId)!},${(schedules[1].classId+"_"+schedules[1].subjectId)!}">
				<span>
					<span class="s-d-week">
						<input type="hidden" name="id" value="${schedules[0].id!}"/>
	            		<input type="hidden" name="subjectId" value="${schedules[0].subjectId!}"/>
	            		<input type="hidden" name="subjectType" value="${schedules[0].subjectType!}"/>
	            		<input type="hidden" name="placeId" value="${schedules[0].placeId!}"/>
	            		<input type="hidden" name="teacherId" value="${schedules[0].teacherId!}"/>
	            		<input type="hidden" name="classId" value="${schedules[0].classId!}"/>
	            		<input type="hidden" name="classType" value="${schedules[0].classType!}"/>
	            		<input type="hidden" name="weekType" value="${schedules[0].weekType!}"/>
					
	            	   <span>
		            	   <span class="course" title="${schedules[0].subjectName!}">${schedules[0].subjectName!}</span>
	            	   	   <#if schedules[0].weekType! == 1><span class="type">单</span></#if>
	            	   </span>
	                    <span class="name"><#if type == "C">${schedules[0].teacherName!}<#else>${schedules[0].className!}</#if></span>
	                    <span class="room"><#if type == "P">${schedules[0].teacherName!}<#else>${schedules[0].placeName!}</#if></span>
	               </span>
	               <span class="s-d-week">
	               		<input type="hidden" name="id" value="${schedules[1].id!}"/>
	            		<input type="hidden" name="subjectId" value="${schedules[1].subjectId!}"/>
	            		<input type="hidden" name="subjectType" value="${schedules[1].subjectType!}"/>
	            		<input type="hidden" name="placeId" value="${schedules[1].placeId!}"/>
	            		<input type="hidden" name="teacherId" value="${schedules[1].teacherId!}"/>
	            		<input type="hidden" name="classId" value="${schedules[1].classId!}"/>
	            		<input type="hidden" name="classType" value="${schedules[1].classType!}"/>
	            		<input type="hidden" name="weekType" value="${schedules[1].weekType!}"/>
	               		
	                   <span>
	            	   	   <span class="otherCourse" title="${schedules[1].subjectName!}">${schedules[1].subjectName!}</span>
	            	   	   <#if schedules[1].weekType! == 2><span class="type">双</span></#if>
	            	   </span>
	            	   <span class="otherName"><#if type == "C">${schedules[1].teacherName!}<#else>${schedules[1].className!}</#if></span>
	            	   <span class="otherRoom"><#if type == "P">${schedules[1].teacherName!}<#else>${schedules[1].placeName!}</#if></span>
	               </span>
	             </span>
	             <#if (schedules[0].weekType + schedules[1].weekType)! == 3>
				 <span class="replace"><a href="#" class="fa fa-refresh"></a></span>
				 </#if>
				 </span>

				<#elseif schedules?? && schedules?size gt 0>
				<#assign data_user = ''>
				<#assign data_user = (schedules[0].classId+"_"+schedules[0].subjectId)!>
            	<span class="item <#if fixedSubjects?? && fixedSubjects?seq_contains(schedules[0].subjectId!)>fixedSubject</#if>" data-user="${data_user!}">
            		<input type="hidden" name="id" value="${schedules[0].id!}"/>
            		<input type="hidden" name="subjectId" value="${schedules[0].subjectId!}"/>
            		<input type="hidden" name="subjectType" value="${schedules[0].subjectType!}"/>
            		<input type="hidden" name="placeId" value="${schedules[0].placeId!}"/>
            		<input type="hidden" name="teacherId" value="${schedules[0].teacherId!}"/>
            		<input type="hidden" name="classId" value="${schedules[0].classId!}"/>
            		<input type="hidden" name="classType" value="${schedules[0].classType!}"/>
            		<input type="hidden" name="weekType" value="${schedules[0].weekType!}"/>
            		
                	<span class="course">${schedules[0].subjectName!}</span>
                    <span class="name"><#if type == "C">${schedules[0].teacherName!}<#else>${schedules[0].className!}</#if></span>
                    <span class="room"><#if type == "P">${schedules[0].teacherName!}<#else>${schedules[0].placeName!}</#if></span>
                </span>
				<#else>
				<span class="item" data-user="0"></span>
				</#if>
            </td>
			</#list>
	    </tr>
	    </#list>
	    </#if>
    </#list>
    
    </tbody>
</table>
<ul class="syllabus-from fn-clear" <#if fromAlone! != "1" && sub_sys! == "BASE">style="display:none;"</#if>>
<#if classSubjectList?? && classSubjectList?size gt 0>
<#list classSubjectList as csts>
	<#if csts?size gt 1>
	<#-- 单双周 -->
	<li data-user="${(csts[0].classId+"_"+csts[0].subjectId)!},${(csts[1].classId+"_"+csts[1].subjectId)!}" class="s-d-week">
    	<p class="item">
            <span class="surplus" data-total="1"><span>1</span>节</span>
            <span class="tt">
            	<span>
            		<input type="hidden" name="id" value="${csts[0].id!}"/>
	        		<input type="hidden" name="subjectId" value="${csts[0].subjectId!}"/>
	        		<input type="hidden" name="subjectType" value="${csts[0].subjectType!}"/>
	        		<input type="hidden" name="placeId" value=""/>
	        		<input type="hidden" name="teacherId" value=""/>
	        		<input type="hidden" name="classId" value="${csts[0].classId!}"/>
	        		<input type="hidden" name="classType" value="${csts[0].classType!}"/>
	        		<input type="hidden" name="weekType" value="${csts[0].weekType!}"/>
            	
            		<span class="course" title="${csts[0].subjectName!}">${csts[0].subjectName!}</span>
            		<span class="name"></span>
            		<span class="room"></span>
            	</span>
            	<span>
            		<input type="hidden" name="id" value="${csts[1].id!}"/>
	        		<input type="hidden" name="subjectId" value="${csts[1].subjectId!}"/>
	        		<input type="hidden" name="subjectType" value="${csts[1].subjectType!}"/>
	        		<input type="hidden" name="placeId" value=""/>
	        		<input type="hidden" name="teacherId" value=""/>
	        		<input type="hidden" name="classId" value="${csts[1].classId!}"/>
	        		<input type="hidden" name="classType" value="${csts[1].classType!}"/>
	        		<input type="hidden" name="weekType" value="${csts[1].weekType!}"/>
            	
            		<span class="otherCourse" title="${csts[1].subjectName!}">${csts[1].subjectName!}</span>
            		<span class="otherName"></span>
            		<span class="otherRoom"></span>
            	</span>
            </span>
        </p>
    </li>

	
	<#else>
	<#assign cst = csts[0]!>
    <li data-user="${cst.classId+"_"+cst.subjectId!}" data-subType="${cst.subjectType!}">
    	<p class="item">
            <span class="surplus" data-total="${cst.courseWorkDay!}"><span>${cst.courseWorkDay!}</span>节</span>
            <span class="tt">
            	<input type="hidden" name="id" value="${cst.id!}"/>
        		<input type="hidden" name="subjectId" value="${cst.subjectId!}"/>
        		<input type="hidden" name="subjectType" value="${cst.subjectType!}"/>
        		<input type="hidden" name="placeId" value=""/>
        		<input type="hidden" name="teacherId" value=""/>
        		<input type="hidden" name="classId" value="${cst.classId!}"/>
        		<input type="hidden" name="classType" value="${cst.classType!}"/>
        		<input type="hidden" name="weekType" value="${cst.weekType!}"/>
            	
            	<span class="course">${cst.subjectName!}</span>
            	<span class="name"></span>
            	<span class="room"></span>
            </span>
        </p>
    </li>
	</#if>
</#list>
</#if>
	<#-- <li data-user="1,4" class="s-d-week">
    	<p class="item">
            <span class="surplus" data-total="1"><span>1</span>节</span>
            <span class="tt">
            	<span>
            		<span class="course">美术</span><span class="name">王志</span><span class="room">(教室1)</span>
            	</span>
            	<span>
            		<span class="otherCourse">班会</span><span class="otherName">姚丽丽</span><span class="otherRoom">(教室2)</span>
            	</span>
            </span>
        </p>
    </li>  -->
	
	<li data-user="0" class="empty">
    	<p class="item"><span></span></p>
    </li>
</ul>

<#-- 
 <script type="text/javascript" src="${request.contextPath}/basedata/courseSchedule/modify/myscript-array.js"></script>
 -->
 
<style>
<#if type! == "C" || type! == "P">
.teacherTable span.item .name{color:#444;font-weight: bold;font-size:15px;}
.teacherTable span.item .course{color:#0863d5;font-weight: normal;}
<#else>
.teacherTable span.item .course{font-size:15px;}
</#if>

</style>
<script type="text/javascript">
<#if extraClassNameMap?? && extraClassNameMap?size gt 0>
<#list extraClassNameMap?keys as cid>
classNameMap["${cid!}"] = "${extraClassNameMap[cid]!}";
console.log("add..");
</#list>
</#if>
winLoad2();
var combineRes = {};
$(document).ready(function(){

	//
	/**** 业务 js 代码 ****/
	function title(){
		var index = $(".tabs_menu > dl > dd.active").index();
		var title = $(".tabs_box > div").eq(index).find(".tablist .current a .objName").text();
		$(".content p.tit").html(title+" 课程表");
	}
	title();
	// 教师冲突 显示
	<#if type! == "T">
	updateTeaConflict();
	</#if>


	console.log("as = ");
	// 填充底部 信息 教师 场地等
	var subjectTeachers = classSubjTeachMap['${objId!}'];
	var placeId = classPlaceMap['${objId!}'];
	$(".syllabus-from li").each(function(i,obj){
		var data_user = $(obj).attr("data-user");
		var subjectType = $(obj).attr("data-subtype");

		var classId = $(obj).find("[name='classId']").val();
		subjectTeachers = classSubjTeachMap[classId];
		placeId = classPlaceMap[classId];
		//console.log("placeId = "+placeId+"  subteachers"+subjectTeachers);
		// 底部待排课程
		var period = $(obj).find(".surplus").attr("data-total");
		var s_d_week = $(obj).hasClass("s-d-week");
		var subjectId1 = $(obj).find("[name='subjectId']").val();
		var subjectId2 = $(obj).find("[name='subjectId']:eq(1)").val();
		var split = data_user.split(",");

		if(classIdTypeMap[classId]){
			$(obj).find("[name='classType']").val(classIdTypeMap[classId]);
		}
		if(s_d_week){
			var classId2 = $(obj).find("[name='classId']:eq(1)").val();
			if(classIdTypeMap[classId2]){
				$(obj).find("[name='classType']:eq(1)").val(classIdTypeMap[classId2]);
			}

			// 单双周
			var used = $("#allTable").find('[data-user="'+data_user+'"]').length;
			//console.log("subjectId="+subjectId+" used = "+used);
			var left = 1-used;
			if(left == 0){
				$(obj).addClass("over").hide();
				$(obj).find(".surplus span").text(left);
			}else{
				var du2 = split[1]+","+split[0];
				used = $("#allTable").find('[data-user="'+du2+'"]').length;
				var left = 1-used;
				//console.log("subId2="+subId2+" used = "+used);
				if(left == 0){
					$(obj).addClass("over").hide();
					$(obj).find(".surplus span").text(left);
				}
			}
			// 显示教师
			if(subjectTeachers){
				var tid = subjectTeachers[subjectId1];
				var tName = teacherNameMap[tid];
				//console.log(tid+"-tid  tname-"+tName);
				if(tid) $(obj).find("[name='teacherId']:eq(0)").val(tid);
				if(tName) $(obj).find(".name").text(tName);

				tid = subjectTeachers[subjectId2];
				tName = teacherNameMap[tid];
				//console.log(tid+"-tid  tname-"+tName);
				if(tid) $(obj).find("[name='teacherId']:eq(1)").val(tid);
				if(tName) $(obj).find(".otherName").text(tName);
			}

			// 底部教室
			var code1 = $(obj).find("[name='subjectId']:eq(0)").val()+"-"+$(obj).find("[name='subjectType']:eq(0)").val();
			var code2 = $(obj).find("[name='subjectId']:eq(1)").val()+"-"+$(obj).find("[name='subjectType']:eq(1)").val();
			if(placeId){
				//console.log("code1= "+code1+"  code2 = "+code2);
				if($.inArray(code1,noNeedPlaceCourse)<0){
					$(obj).find("[name='placeId']:eq(0)").val(placeId);
					$(obj).find(".room").text(placeNameMap[placeId]);
				}
				if($.inArray(code2,noNeedPlaceCourse)<0){
					$(obj).find("[name='placeId']:eq(1)").val(placeId);
					$(obj).find(".otherRoom").text(placeNameMap[placeId]);
				}
			}
		}else{
			var used = 0;
			used = 0;

			var weekType = $(obj).find("[name='weekType']").val();
			$("#allTable").find('[data-user*="'+data_user+'"]').each(function(i,obj){
				var du = $(obj).attr("data-user");
				var split2 = du.split(",");
				for(i in split2){
					if(split2[i] == data_user){
						var wt = $(obj).find("[name='weekType']:eq("+i+")").val();
						if(sub_sys == "BASE"){
							if(wt == weekType)
								used++;
						}else if(wt == 3){
							 used++;
						}
					}
				}
			});

			var left = period - used;
			$(obj).find(".surplus span").text(left);
			if(left == 0){
				$(obj).addClass("over").hide();
			}
			//console.log($(obj).find(".course").text() + '----'+ (left));
			// 显示教师
			if(("${type!}" == "C" || "${type!}" == "P") && subjectTeachers){
				var tid = subjectTeachers[subjectId1];
				var tName = teacherNameMap[tid];
				//console.log(tid+"-tid  tname-"+tName);

				if(tid) $(obj).find("[name='teacherId']").val(tid);
				if("${type!}" == "P"){
					var cn = classNameMap[classId];
					if(cn) $(obj).find(".name").text(cn);
				}else{
					if(tName) $(obj).find(".name").text(tName);
				}
			}else if("${type!}" == "T" ){
				var cn = classNameMap[classId];
				if(cn) $(obj).find(".name").text(cn);


				$(obj).find("[name='teacherId']").val("${objId!}");
			}


			// 教室
			var code = subjectId1+"-"+subjectType;
			//console.log("classId = "+classId+" placeId = "+ placeId+"  f = "+($.inArray(code,noNeedPlaceCourse)<0));
			if(placeId && $.inArray(code,noNeedPlaceCourse)<0){
				$(obj).find("[name='placeId']").val(placeId);
				$(obj).find(".room").text(placeNameMap[placeId]);
			}
		}
	});
	// 年级禁排
	freshGradeNoClick();
	// 行政班 走班时间点 禁排
	classMovePeriod();
	// 和此行政班有关的 合班 情况
	selCombieRes();
	<#if type! == "C">
	freshXzbPeriods("${objId!}");
	</#if>

	$("#allTable tbody td span.item.fixedSubject").addClass("item-lock");
});

var weekIndex = ${weekIndex!0};

// 年级禁排
function freshGradeNoClick(){
	if(!grade_limit){
		$('#allTable tbody td span.item.gradeNo').not(".fixedSubject").removeClass("item-lock");
		return;
	}

	for(x in gradeNoClick){
		//console.log("grade_limit = "+grade_limit);
		var timestr = gradeNoClick[x];
		$('#allTable tbody td[data-time="'+timestr+'"]').find("span.item").addClass("gradeNo").addClass("item-lock");
	}
}
function freshXzbPeriods(clsId){
	if(!xzbPeriodTab || pure3XzbIdTab[clsId]){
		return;
	}
	$("#allTable tbody td").each(function(){
		var time = $(this).attr("data-time");
		if($(this).find(".item-lock").length<1 && !xzbPeriodTab[time]){
			<#if type! == "C">
			$(this).find(".item").addClass("fixedSubject");
			$(this).find(".item").addClass("item-lock");
			<#else>
			$(this).find(".item").addClass("item-subject-no"); // 组合固定模式 的 时间限制
			$(this).find(".item").addClass("item-lock");
			</#if>
		}
	});
}
// 组合固定 班级 的 禁排时间；来自课表设置
function freshZhbPeriods(){
	if(!xzbPeriodTab){
		return;
	}
	$("#allTable tbody td").each(function(){
		var time = $(this).attr("data-time");
		if($(this).find(".item-lock").length<1 && xzbPeriodTab[time]){
			$(this).find(".item").addClass("item-subject-no"); // 组合固定模式 的 时间限制
			$(this).find(".item").addClass("item-lock");
		}
	});
}
// 每个行政班学生走班的时间点
function classMovePeriod(){
	var times = [];
	<#if type! == "C">
		times = classMovePeriodMap['${objId!}'];
	<#elseif type! == "T">
		times = teacherMoveTimeMap['${objId!}'];
	<#else>
		times = placeMoveTimeMap['${objId!}'];
	</#if>
	<#if movePeriods?? && movePeriods?size gt 0>
	if(!times) times = [];
	console.log("modePeriod ");
	<#list movePeriods as time>
	times.push("${time}");
	</#list>
	</#if>
	for(i in times){
		var time = times[i];
		$('#allTable tbody td[data-time="'+time+'"]').find("span.item").removeClass("fixedSubject").addClass("movePeriod").addClass("item-lock");
	}
}
// 和此行政班有关的 合班班级
function selCombieRes(){
	for(x in combineRelations){
		var group = combineRelations[x];
		for(id in group){
			<#if type! == "T" || type! == "C">
			if(id.includes('${oldId!}')){
				combineRes[id] = group;
				break;
			}
			<#elseif type! == "P">
			var f = false;
			for(i in relaCids){
				var cid = relaCids[i];
				if(id.includes(cid)){
					combineRes[id] = group;
					f = true;
					break;
				}
			}
			if(f){
				break;
			}
			</#if>
		}
	}
}
function freshFixedNotime(){
	freshGradeNoClick();
	classMovePeriod();
}
var teacherTables = {};
var teacherScheduleMap = {};
<#if teacherScheduleMap?? && teacherScheduleMap?size gt 0>
<#list teacherScheduleMap?keys as tid>
teacherScheduleMap["${tid!}"] = [];
	<#list teacherScheduleMap[tid] as cs>
	teacherScheduleMap["${tid!}"].push({"classId":"${cs.classId!}","oldDivideClassId":"${cs.oldDivideClassId!}","subjectId":"${cs.subjectId!}",
	"subjectType":"${cs.subjectType!}", "teacherId":"${cs.teacherId!}", "placeId":"${cs.placeId!}", "dayOfWeek":"${cs.dayOfWeek!}",
	"periodInterval":"${cs.periodInterval!}", "period":"${cs.period!}","subjectName":"${cs.subjectName!}","weekType":"${cs.weekType!}",
	"className":"${cs.className!}", "teacherName":"${cs.teacherName!}","placeName":"${cs.placeName!}","id":"${cs.id!}"});
	</#list>
</#list>
</#if>
var objId = "${objId!}";
var oldId = "${oldId!}";
var type = "${type!}";
var subjectNoTime = {
<#if classSubjectList?? && classSubjectList?size gt 0>
<#list classSubjectList as css>
	<#list css as cs>
		<#if css_index !=0 || cs_index!=0>,</#if>"${cs.subjectId!}":"${cs.noArrangeTime!}"
	</#list>
</#list>
</#if>
};

var classVirtualTimeMap = {};
<#if classVirtualTimeMap?? && classVirtualTimeMap?size gt 0>
<#list classVirtualTimeMap?keys as cid>
	<#assign times = classVirtualTimeMap[cid]>
	classVirtualTimeMap['${cid}'] = [
	<#list times as t>
		<#if t_index !=0>,</#if>"${t!}"
	</#list>
	];
</#list>
</#if>


<#if teacherNameMap?? && teacherNameMap?size gt 0>
teacherNameMap = {
<#list teacherNameMap?keys as tid>
	<#if tid_index != 0>,</#if>
	"${tid!}":"${teacherNameMap[tid]!}"
</#list>
};
</#if>

var relaCids = [];
<#if relaCids?? && relaCids?size gt 0>
<#list relaCids as cid>
relaCids.push(classOldIdMap["${cid}"]);
</#list>
</#if>
</script>
<#if type! == "C">
<#-- <script type="text/javascript" src="${request.contextPath}/static/newgkelective/myscript-array2.js"></script> -->
<script type="text/javascript" src="${request.contextPath}/basedata/scheduleModify/${indexId!}/myscript-array2"></script> 
<#elseif type! == "T">
<#--<script type="text/javascript" src="${request.contextPath}/static/newgkelective/myscript-array-t.js"></script> -->
<script type="text/javascript" src="${request.contextPath}/basedata/scheduleModify/${indexId!}/myscript-array2?type=${type}"></script> 
<#elseif type! == "P">
<script type="text/javascript" src="${request.contextPath}/basedata/scheduleModify/${indexId!}/myscript-array2?type=${type}"></script> 
</#if>
<#--
<script type="text/javascript" src="${request.contextPath}/static/js/myscript-array2.js"></script> 
 -->