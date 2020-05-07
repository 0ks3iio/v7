<#import "/fw/macro/treemacro.ftl" as treemacro>
<div class="tab-content">
	<div class="tab-pane active">
		<div class="clearfix">
			<div class="tree-wrap tree-warp-before">
				<div class="list-group">
					<#if unitClass?default(-1) != 2>
						<div class="widget-box" style="border-color: #5090C1;">
							<div class="widget-body">
								<div class="widget-main padding-8" style="height:600px;overflow:auto;">
									<ul id="schoolTree" class="ztree"></ul>
								</div>
							</div>
						</div>
					<#else>
						<#if parMap['class_id']??>
							<@treemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
						<#else>
							<@treemacro.gradeForSchoolInsetTree height="550" click="onTreeClick"/>
						</#if>
					</#if>
				</div>
			</div>
			<div id="loadLeftDivId">
				<#--隐藏参数-->
				<div style="display:none" id="hidden_P">
					<input type="hidden" id="exam_id" value="${examId!}">
					<input type="hidden" id="grade_id" value="">
					<input type="hidden" id="class_id" value="">
					<input type="hidden" id="subject_id" value="">
					<input type="hidden" id="student_id" value="">
				</div>
				<div class="filter" id="queryChartsList" style="">
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
					<#if parMap['student_id']??>
					<div class="filter-item">
						<label for="" class="filter-name">学生：</label>
						<div class="filter-content">
							<select <#if parMap['student_id'].isMultiple == 1>multiple</#if> class="form-control" id="studentIdCharts" data-placeholder="选择学生" onchange="doChangeStudent()">
								<#if parMap['student_id'].isMultiple == 0>
								<option value="">暂无数据</option>
								</#if>
							</select>
						</div>
					</div>
					</#if>
				</div>
				<div class="box-boder bmClass"  style="height:610px;overflow-y:auto; overflow-x:auto;">
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
		width:'220px',
		multi_container_height:'60px',
		results_height:'80px',
		no_results_text:"未找到",
		allow_single_deselect:true,
		disable_search:false,
		search_contains:true
	});
	$("#studentIdCharts").chosen({
		width:'220px',
		multi_container_height:'60px',
		results_height:'80px',
		no_results_text:"未找到",
		allow_single_deselect:true,
		disable_search:false,
		search_contains:true
	});
}
function onTreeClick(event, treeId, treeNode, clickFlag){
	var myLabel=$("#documentLabel").val();
	if(myLabel=="score7choose3Grade"){
		var url="${request.contextPath}/scoremanage/scoreStatistic/list1/page?lable="+myLabel;
		$(".bmClass").load(url);
	}else if(myLabel=="score7choose3All"){
		var url="${request.contextPath}/scoremanage/scoreStatistic/list1/page?lable="+myLabel;
		$(".bmClass").load(url);	
			
	}else{
		if(treeNode.type == "grade"){
			var id = treeNode.id;
			$("#grade_id").val(id);
			$("#class_id").val("");
			<#if parMap['subject_id']??>
				doChangeFindCourse(id,'2');
			</#if>
			<#if parMap['student_id']??>
				doChangeFindStudent(id,'2');
			</#if>
			<#if parMap['grade_id']?? && parMap['grade_id'].isAssistPar == 0>
				doShowBICharts();
			</#if>
		}
		if(treeNode.type == "class"){
			var id = treeNode.id;
			var pId = treeNode.pId;
			$("#grade_id").val(pId);
			$("#class_id").val(id);
			<#if parMap['subject_id']??>
				doChangeFindCourse(id,'1');
			</#if>
			<#if parMap['student_id']??>
				doChangeFindStudent(id,'1');
			</#if>
			<#if parMap['class_id']?? && parMap['class_id'].isAssistPar == 0>
				doShowBICharts();
			</#if>
		}
	}
}
<#if parMap['subject_id']??>
var subjectMap={};
function doChangeFindCourse(id,idType){
	$("#queryChartsList").show();
	$("#subjectIdCharts option").remove();
	if(subjectMap['${examId!}'+id]){
		var jsonO = subjectMap['${examId!}'+id];
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
	    url:'${request.contextPath}/scoremanage/scoreStatistic/findCourseList',
	    data: {'id':id,'type':idType,'examId':'${examId!}'},  
	    type:'post',  
	    success:function(data) {
	    	var jsonArr = JSON.parse(data);
	    	var jsonO = jsonArr[0];
	    	subjectMap['${examId!}'+id]=jsonO;
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
<#if parMap['student_id']??>
var studentMap={};
function doChangeFindStudent(id,idType){
	$("#queryChartsList").show();
	$("#studentIdCharts option").remove();
	if(studentMap['${examId!}'+id]){
		var jsonO = studentMap['${examId!}'+id];
		if(jsonO.length>0){
			var htmlOption="<option value=''></option>";
			$("#studentIdCharts").append(htmlOption);
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#studentIdCharts").append(htmlOption);
	    	});
    	}else{
    		$("#studentIdCharts").append('<option value="">暂无数据</option>');
    	}
    	$('#studentIdCharts').trigger("chosen:updated");
    	doStudentValue();
    	return;
	}
	$.ajax({
	    url:'${request.contextPath}/scoremanage/scoreStatistic/findStudentList',
	    data: {'id':id,'type':idType},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	studentMap['${examId!}'+id]=jsonO;
	    	if(jsonO.length>0){
	    		var htmlOption="<option value=''></option>";
				$("#studentIdCharts").append(htmlOption);
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    			$("#studentIdCharts").append(htmlOption);
		    	});
		    }else{
	    		$("#studentIdCharts").append('<option value="">暂无数据</option>');
	    	}
	    	$('#studentIdCharts').trigger("chosen:updated");
	    	doStudentValue();
	    }
	});
}
function doStudentValue(){
	<#if parMap['student_id'].isMultiple == 1>
		var studentIdCharts = '';
		$("#studentIdCharts").children("option").each(function(){
			if($(this).is(":selected")){
				studentIdCharts+=','+$(this).val();
			}
		});
		if(studentIdCharts == ''){
			$("#student_id").val("");
			<#if parMap['student_id'].isMust == 1>
			return false;
			</#if>
		}else{
			studentIdCharts=studentIdCharts.substring(1,studentIdCharts.length);
		}
		$("#student_id").val(studentIdCharts);
	<#else>
		var studentIdCharts = $("#studentIdCharts").val();
		if(studentIdCharts == ''){
			$("#student_id").val("");
			<#if parMap['student_id'].isMust == 1>
			return false;
			</#if>
		}
		$("#student_id").val(studentIdCharts);
	</#if>
	return true;
}
function doChangeStudent(){
	if(doStudentValue()){
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