<div id="ee" class="tab-pane fade active in">
	<div class="filter filter-f16 searchClassIdsDiv_5" id="searchClassIdsDiv_5">
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
	</div>
    <h3>各科成绩分布分析</h3>
    <div class="filter filter-f16 searchContentDiv_5">
       <div class="filter-item">
			<span class="filter-name">科目：</span>
			<div class="filter-content">
				<select name="subjectId" id="subjectId_1" class="form-control" nullable="false" onChange="showCompareChart1()">
				
				<#if courseList?exists && courseList?size gt 0>
				<#list courseList as dto>
				     <option value="${dto.id!}">${dto.subjectName!}</option>
				</#list>
				<#else>
					<option value="">--请选择--</option>
				</#if>
				</select>
			</div>
		</div>
    </div>
	<div class="filter"> 
        <div id="comparechart01" style="height:450px;width:1100px;overflow:auto;">
        
        </div> 
    </div>
	<h3>成绩对比分析</h3>
	<div class="filter filter-f16 searchContentDiv_6">
       <div class="filter-item">
			<span class="filter-name">科目：</span>
			<div class="filter-content">
				<select name="subjectId" id="subjectId_2" class="form-control"  nullable="false" onChange="showCompareChart2()">
				
				<#if courseList?exists && courseList?size gt 0>
				<#list courseList as dto>
				     <option value="${dto.id!}">${dto.subjectName!}</option>
				</#list>
				<#else>
					<option value="">--请选择--</option>
				</#if>
				</select>
				
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">分析类型：</span>
			<div class="filter-content">
				<select name="analysisType" id="analysisType" nullable="false" class="form-control" onChange="showCompareChart2()">
				     <option value="0_allAvg">总体平均分</option>
				     <option value="0_max">最高分</option>
				     <option value="0_min">最低分</option>
				     <#if avgTitle?exists && avgTitle?size gt 0>
						<#list avgTitle as title>
						<option value="1_${title!}">年级${title!}平均分</option>
						</#list>
					</#if>
				</select>
			</div>
		</div>
    </div>
	<div class="filter"> 
        <div id="comparechart02" style="height:450px;width:1100px;overflow:auto;" >
        
        </div> 
    </div>
</div>
<script>
	$(function(){
		var viewContent1={
			'width' : '800px',//输入框的宽度
			'multi_container_height' : '28px',//输入框的高度
			'results_height':'200px'
		}
		initChosenMore("#searchClassIdsDiv_5","",viewContent1);
		showCompareChart1();
		showCompareChart2();
	})
	function showCompareChart1(){
		var check = checkValue('.searchClassIdsDiv_5');
	    if(!check){
	    	$("#comparechart01").text('');
	        return;
	    }
	    check = checkValue('.searchContentDiv_5');
	    if(!check){
	    	$("#comparechart01").text('');
	        return;
	    }
	    var examId=$("#examId").val();
		var subjectId=$("#subjectId_1").val();
		var classIds=$("#classIds").val();
		if(classIds==null || classIds==""){
			layer.tips("请先选择班级",$("#searchClassIdsDiv_5") , {
				tipsMore: true,
				tips:3		
			});
			$("#comparechart01").text('');
			return;
		}
		var url =  '${request.contextPath}/examanalysis/examGrade/compareChart/page?examId='+examId+'&subjectId='+subjectId+'&classIds='+classIds+'&type=1';
		url=encodeURI(url);
		$("#comparechart01").load(url);
		
	}
	function showCompareChart2(){
		var check = checkValue('.searchClassIdsDiv_5');
	    if(!check){
	    	$("#comparechart02").text('');
	        return;
	    }
	    check = checkValue('.searchContentDiv_6');
	    if(!check){
	    	$("#comparechart02").text('');
	        return;
	    }
	    var examId=$("#examId").val();
		var subjectId=$("#subjectId_2").val();
		var classIds=$("#classIds").val();
		if(classIds==null || classIds==""){
			layer.tips("请先选择班级",$("#searchClassIdsDiv_5") , {
				tipsMore: true,
				tips:3		
			});
			$("#comparechart02").text('');
			return;
		}
		var analysisType=$("#analysisType").val();
		var url =  '${request.contextPath}/examanalysis/examGrade/compareChart/page?examId='+examId+'&subjectId='+subjectId+'&classIds='+classIds+'&analysisType='+analysisType+'&type=2';
		url=encodeURI(url);
		$("#comparechart02").load(url);
	}
</script>