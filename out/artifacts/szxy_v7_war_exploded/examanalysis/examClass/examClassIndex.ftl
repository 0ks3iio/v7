<div class="box box-default">
	<div class="box-body clearfix">
	     <div class="filter filter-f16">
            <div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control" onChange="showExamIdByGradeId()">
						<#if gradeList?exists && gradeList?size gt 0>
					     	<#list gradeList as grade>
					     		<option value="${grade.id!}">${grade.gradeName!}</option>
					     	</#list>
					     <#else>
					     	<option value="">--请选择--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">考试：</span>
				<div class="filter-content">
					<select name="examId" id="examId" class="form-control" style="width:300px;" onChange="showClassByExamId()">
					     <option value="">--请选择--</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" style="width:300px;" onChange="searchList()">
					     <option value="">--请选择--</option>
					</select>
				</div>
			</div>
		</div>
		<div class="tab-header clearfix">
			<ul class="nav nav-tabs nav-tabs-1">
			    <li class="active">
			 		<a data-toggle="tab" href="#a0" aria-expanded="true" onclick="itemClassList(1)">班级成绩概况</a>
			 	</li>
			 	<li class="">
			 		<a data-toggle="tab" href="#a1" aria-expanded="true" onclick="itemClassList(2)">成绩分布分析</a>
			 	</li>
			</ul>
		</div>
		<div class="tab-content" id="classTabList">
		</div>

	</div>
</div>
<script>
	var type="";
	$(function(){
		showExamIdByGradeId();
		type="1";
	});
	function showExamIdByGradeId(){
		var gradeId=$("#gradeId").val();
		if(gradeId==""){
			$("#examId").html('');
			$("#examId").append('<option value="">--请选择--</option>');
			return false;
		}
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/examanalysis/findExamIdByGradeId",
			data:{gradeId:gradeId},
			dataType: "json",
			success: function(data){
				examClass.html("");
				if(data.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
					}
				}
				showClassByExamId();
			}
		});
		
	}
	
	function showClassByExamId(){
		var gradeId=$("#gradeId").val();
		var examId=$("#examId").val();
		if(examId==""){
			$("#classId").html('');
			$("#classId").append('<option value="">--请选择--</option>');
			return false;
		}
		var clazzClass=$("#classId");
		$.ajax({
			url:"${request.contextPath}/examanalysis/findClassIdByExamId",
			data:{gradeId:gradeId,examId:examId},
			dataType: "json",
			success: function(data){
				clazzClass.html("");
				if(data.length==0){
					clazzClass.append("<option value='' >--请选择--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						clazzClass.append("<option value='"+data[i].id+"' >"+data[i].classNameDynamic+"</option>");
					}
				}
				searchList();
			}
		});
	}
	
	function searchList(){
		var gradeId=$("#gradeId").val();
		var examId=$("#examId").val();
		var classId=$("#classId").val();
		if(gradeId=="" || examId=="" || classId==""){
			$("#classTabList").text('');
			return;
		}
		if(type=="2"){
			var url =  '${request.contextPath}/examanalysis/examClass/showClassScore/page?gradeId='+gradeId+'&examId='+examId+'&classId='+classId+'&type='+type;
			$("#classTabList").load(url);
		}else{
			var url =  '${request.contextPath}/examanalysis/examClass/showClassScore/page?gradeId='+gradeId+'&examId='+examId+'&classId='+classId+'&type='+type;
			$("#classTabList").load(url);
		}
		
	}
	function itemClassList(searchtype){
		type=searchtype;
		searchList();
	}
</script>