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
					<select name="examId" id="examId" class="form-control" style="width:300px;" onChange="searchList()">
					     <option value="">--请选择--</option>
					</select>
				</div>
			</div>
		</div>
		<div class="tab-header clearfix">
			<ul class="nav nav-tabs nav-tabs-1">
			    <li class="active">
			 		<a data-toggle="tab" href="#a0" aria-expanded="true" onclick="itemGradeList(1)">年级成绩概况</a>
			 	</li>
			 	<li class="">
			 		<a data-toggle="tab" href="#a1" aria-expanded="true" onclick="itemGradeList(2)">成绩分布分析</a>
			 	</li>
			 	<li class="">
			 		<a data-toggle="tab" href="#a1" aria-expanded="true" onclick="itemGradeList(3)">拐点分析</a>
			 	</li>
			 	<li class="">
			 		<a data-toggle="tab" href="#a1" aria-expanded="true" onclick="itemGradeList(4)">班级对比分析概况</a>
			 	</li>
			 	<li class="">
			 		<a data-toggle="tab" href="#a1" aria-expanded="true" onclick="itemGradeList(5)">班级成绩分布分析</a>
			 	</li>
			</ul>
		</div>
		<div class="tab-content" id="gradeTabList">
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
					examClass.append("<option value='' >--请选择--</option>");
					for(var i = 0; i < data.length; i ++){
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
					}
				}
				searchList();
			}
		});
		
	}
	
	function searchList(){
		var gradeId=$("#gradeId").val();
		var examId=$("#examId").val();
		var url =  '${request.contextPath}/examanalysis/examGrade/showScoreByGrade/page?gradeId='+gradeId+'&examId='+examId+'&type='+type;
		$("#gradeTabList").load(url);
		
	}
	function itemGradeList(searchtype){
		type=searchtype;
		searchList();
	}
</script>