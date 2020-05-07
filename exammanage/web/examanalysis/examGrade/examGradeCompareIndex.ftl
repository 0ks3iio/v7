<div class="filter filter-f16 searchClassIdsDiv_4" id="searchClassIdsDiv_4">
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select multiple vtype="selectMore" nullable="false" name="classIds" id="classIds" class="form-control chosen-select" data-placeholder="未选择" >
			     <#if clazzList?exists && clazzList?size gt 0>
			     	<#list clazzList as clazz>
			     		<option value="${clazz.id!}" selected="selected">${clazz.classNameDynamic!}</option>
			     	</#list>
			     </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:" onclick="showCompareList()">查询</a>
	</div>
</div>
<div class="filter filter-f16 searchContentDiv_5" id="searchContentDiv_5">
	<div class="filter-item">
		<span class="filter-name">成绩类型：</span>
		<div class="filter-content">
			<select name="subjectId" id="subjectId" class="form-control" nullable="false" onChange="showCompareList()">
			     <#if courseList?exists && courseList?size gt 0>
			     	<#list courseList as course>
			     		<option value="${course.id!}">${course.subjectName!}</option>
			     	</#list>
			     <#else>
			     	<option value="">--请选择--</option>
			     </#if>
			</select>
		</div>
	</div>

	 <div class="filter-item" id="headMess"> 
		
	</div>
</div>
<div class="filter"> 
    <div id="compareDiv" class="compareDiv">
    
    </div> 
</div>
<script>
	
	$(function(){
		var viewContent1={
			'width' : '800px',//输入框的宽度
			'multi_container_height' : '28px',//输入框的高度
			'results_height':'200px'
		}
		initChosenMore("#searchClassIdsDiv_4","",viewContent1);
		showCompareList();
	})
	
	function showCompareList(){
		var check = checkValue('.searchClassIdsDiv_4');
	    if(!check){
	        return;
	    }
	    check = checkValue('.searchContentDiv_4');
	    if(!check){
	        return;
	    }
	    var examId=$("#examId").val();
		var subjectId=$("#subjectId").val();
		var classIds=$("#classIds").val();
		if(classIds==null || classIds==""){
			layer.tips("请先选择班级",$("#searchClassIdsDiv_4") , {
				tipsMore: true,
				tips:3		
			});
			return;
		}
		var url =  '${request.contextPath}/examanalysis/examGrade/compareList/page?subjectId='+subjectId+'&examId='+examId+'&classIds='+classIds;
		$("#compareDiv").load(url);
	}
</script>