<#import "/fw/macro/treemacro.ftl" as treemacro>
<div class="tab-content">
	<div class="tab-pane active">
		<div class="clearfix">
			<div class="tree-wrap tree-warp-before">
				<div class="list-group">
					<#if unitClass?default(-1) != 2>
						<@treemacro.deptForDirectUnitInsetTree height="550" click="onTreeClick"/>
					<#else>
						<@treemacro.deptForUnitInsetTree height="550" click="onTreeClick"/>
					</#if>
				</div>
			</div>
			<div id="loadLeftDivId">
				<#--隐藏参数-->
				<div style="display:none" id="hidden_P">
					<input type="hidden" id="exam_id" value="${examId!}">
					<input type="hidden" id="teacher_id" value="">
					<input type="hidden" id="subject_id" value="">
				</div>
				<div class="filter" id="queryChartsList" style="">
					<#if parMap['teacher_id']??>
					<div class="filter-item">
						<label for="" class="filter-name">教师：</label>
						<div class="filter-content">
							<select <#if parMap['teacher_id'].isMultiple == 1>multiple</#if> class="form-control" id="teacherIdCharts" data-placeholder="选择教师" onchange="doChangeTeacher()">
								<#if parMap['teacher_id'].isMultiple == 0>
								<option value="">暂无数据</option>
								</#if>
							</select>
						</div>
					</div>
					</#if>
					<#if parMap['subject_id']??>
					<div class="filter-item">
						<label for="" class="filter-name">学科：</label>
						<div class="filter-content">
							<select <#if parMap['subject_id'].isMultiple == 1>multiple</#if> class="form-control" id="subjectIdCharts" data-placeholder="选择科目" onchange="doChangeSubject()">
								<#if parMap['subject_id'].isMultiple == 0>
								<option value="">暂无数据</option>
								</#if>
							</select>
						</div>
					</div>
					</#if>
				</div>
				<div class="box-boder bmClass" style="height:610px">
				</div>
			</div>
		</div>
	</div>
</div>
<script>
$(function(){
	$("#showDiv").show();
	chosenBang();
	<#if unitClass?default(-1) != 2>
		var setting = {
			check:{
				enable:false
			},
			data: {				
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pId"
				}
			},
			callback: {
				onClick: onTreeClick
			}
		};
		$.ajax({
			url:"${request.contextPath}/scoremanage/scoreStatistic/findSchoolList?documentLabel=${documentLabel}",
			data:{'examId':'${examId!}'},
			success:function(data){
				var jsonO = JSON.parse(data);
				if(jsonO.length == 0){
					alert("没有找到数据");
					return;
				}
	 			$.fn.zTree.init($("#schoolTree"), setting, jsonO);
			}
		});
	</#if>
});
function chosenBang(){
	$("#subjectIdCharts").chosen({
		width:'200px',
		multi_container_height:'50px',
		results_height:'80px',
		no_results_text:"未找到",
		allow_single_deselect:true,
		disable_search:false,
		search_contains:true
	});
	$("#teacherIdCharts").chosen({
		width:'200px',
		multi_container_height:'50px',
		results_height:'80px',
		no_results_text:"未找到",
		allow_single_deselect:true,
		disable_search:false,
		search_contains:true
	});
}
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "dept"){
		var id = treeNode.id;
		<#if parMap['teacher_id']??>
			doChangeFindTeacher(id);
		</#if>
		<#if parMap['dept_id']?? && parMap['dept_id'].isAssistPar == 0>
			doShowBICharts();
		</#if>
	}
}
<#if parMap['teacher_id']??>
var teacherMap={};
function doChangeFindTeacher(deptId){
	$("#queryChartsList").show();
	$("#teacherIdCharts option").remove();
	if(teacherMap['${examId!}'+deptId]){
		var jsonO = teacherMap['${examId!}'+deptId];
		if(jsonO.length>0){
			var htmlOption="<option value=''></option>";
			$("#teacherIdCharts").append(htmlOption);
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#teacherIdCharts").append(htmlOption);
	    	});
    	}else{
    		$("#teacherIdCharts").append('<option value="">暂无数据</option>');
    	}
    	$('#teacherIdCharts').trigger("chosen:updated");
    	doTeacherValue();
    	return;
	}
	$.ajax({
	    url:'${request.contextPath}/scoremanage/scoreStatistic/findTeacherList',
	    data: {'id':deptId},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	teacherMap['${examId!}'+deptId]=jsonO;
	    	if(jsonO.length>0){
	    		var htmlOption="<option value=''></option>";
				$("#teacherIdCharts").append(htmlOption);
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    			$("#teacherIdCharts").append(htmlOption);
		    	});
		    }else{
	    		$("#teacherIdCharts").append('<option value="">暂无数据</option>');
	    	}
	    	$('#teacherIdCharts').trigger("chosen:updated");
	    	doTeacherValue();
	    }
	});
}
function doTeacherValue(){
	<#if parMap['teacher_id'].isMultiple == 1>
		var teacherIdCharts = '';
		$("#teacherIdCharts").children("option").each(function(){
			if($(this).is(":selected")){
				teacherIdCharts+=','+$(this).val();
			}
		});
		if(teacherIdCharts == ''){
			$("#teacher_id").val("");
			<#if parMap['teacher_id'].isMust == 1>
			return false;
			</#if>
		}else{
			teacherIdCharts=teacherIdCharts.substring(1,teacherIdCharts.length);
		}
		$("#teacher_id").val(teacherIdCharts);
	<#else>
		var teacherIdCharts = $("#teacherIdCharts").val();
		doChangeFindCourse(teacherIdCharts);
		if(teacherIdCharts == ''){
			$("#teacher_id").val("");
			<#if parMap['teacher_id'].isMust == 1>
			return false;
			</#if>
		}
		$("#teacher_id").val(teacherIdCharts);
	</#if>
	return true;
}
function doChangeTeacher(){
	if(doTeacherValue()){
		doShowBICharts();
	}else{
		return;
	}
}
</#if>
<#if parMap['subject_id']??>
var subjectMap={};
function doChangeFindCourse(teacherId){
	if(teacherId == ''){
		$("#subjectIdCharts option").remove();
		$("#subjectIdCharts").append('<option value="">暂无数据</option>');
		$('#subjectIdCharts').trigger("chosen:updated");
		doSubjectValue();
		return;
	}
	$("#queryChartsList").show();
	$("#subjectIdCharts option").remove();
	if(subjectMap['${examId!}'+teacherId]){
		var jsonO = subjectMap['${examId!}'+teacherId];
		if(jsonO.length>0){
			var htmlOption="<option value=''></option>";
    		$("#subjectIdCharts").append(htmlOption);
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#subjectIdCharts").append(htmlOption);
	    	});
    	}else{
    		$("#subjectIdCharts").append('<option value="">暂无数据</option>');
    	}
    	$('#subjectIdCharts').trigger("chosen:updated");
    	doSubjectValue();
    	return;
	}
	$.ajax({
	    url:'${request.contextPath}/scoremanage/scoreStatistic/findCourseListForTea',
	    data: {'id':teacherId,'examId':'${examId!}'},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	subjectMap['${examId!}'+teacherId]=jsonO;
	    	if(jsonO.length>0){
	    		var htmlOption="<option value=''></option>";
    			$("#subjectIdCharts").append(htmlOption);
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    			$("#subjectIdCharts").append(htmlOption);
		    	});
		    }else{
	    		$("#subjectIdCharts").append('<option value="">暂无数据</option>');
	    	}
	    	$('#subjectIdCharts').trigger("chosen:updated");
	    	doSubjectValue();
	    }
	});
}
function doSubjectValue(){
	<#if parMap['subject_id'].isMultiple == 1>
		var subjectIdCharts = '';
		$("#subjectIdCharts").children("option").each(function(){
			if($(this).is(":selected")){
				subjectIdCharts+=','+$(this).val();
			}
		});
		if(subjectIdCharts == ''){
			$("#subject_id").val("");
			<#if parMap['subject_id'].isMust == 1>
			return false;
			</#if>
		}else{
			subjectIdCharts=subjectIdCharts.substring(1,subjectIdCharts.length);
		}
		$("#subject_id").val(subjectIdCharts);
	<#else>
		var subjectIdCharts = $("#subjectIdCharts").val();
		if(subjectIdCharts == ''){
			$("#subject_id").val("");
			<#if parMap['subject_id'].isMust == 1>
			return false;
			</#if>
		}
		$("#subject_id").val(subjectIdCharts);
	</#if>
	return true;
}
function doChangeSubject(){
	if(doSubjectValue()){
		doShowBICharts();
	}else{
		return;
	}
}
</#if>
function doShowBICharts(){
	<#list parMap?keys as key>
		<#if parMap[key].isMust == 1>
			var par_${key} = $("#${key}").val();
			if(par_${key} == ''){
				return;
			}
		</#if>
	</#list>
	$(".bmClass").hide();
	var param = "";
	var valPar = "";
	var valParSZ = [];
	<#list parMap?keys as key>
		<#if parMap[key].isAssistPar == 0>
			if(param != ""){
				param+="&";
			}
			<#if parMap[key].isMultiple == 0>
				param+='${key}=' + $("#${key}").val();
			<#else>
				varPar = $("#${key}").val();
				if(varPar != ""){
					valParSZ = varPar.split(",");
					for(var ind = 0 ; ind < valParSZ.length ;ind++){
						if(ind != 0){
							param+='&${key}=' + valParSZ[ind];
						}else{
							param+='${key}=' + valParSZ[ind];
						}
					}
				}else{
					param+='${key}=' + varPar;
				}
			</#if>
		</#if>
	</#list>
	var mapKey = '${documentLabel}';
	$("#hidden_P").find("input").each(function(){
		if($(this).val() != '')
			mapKey += $(this).val();
	});
	var replaceStr = ",";
	mapKey = mapKey.replace(new RegExp(replaceStr,"gm"),"");
	if($("#chartDivId_"+mapKey).length>0){
		$("#chartDivId_"+mapKey).show();
		return;
	}
	$("#loadLeftDivId").append('<div class="box-boder bmClass" id="chartDivId_'+mapKey+'" style="height:610px"></div>');
	loadChartsDiv('${request.contextPath}','#chartDivId_'+mapKey,'${documentLabel}',param,'100%','600px','myframe_'+mapKey);
}
</script>