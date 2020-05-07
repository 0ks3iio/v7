<div class="filter filter-f16 searchContentDiv_3">
	<div class="filter-item">
		<span class="filter-name">成绩类型：</span>
		<div class="filter-content">
			<select name="subjectId" id="subjectId_1" class="form-control" onChange="showInflection()">
			    <option value="">全部</option>
			     <#if courseList?exists && courseList?size gt 0>
			     	<#list courseList as course>
			     		<option value="${course.id!}">${course.subjectName!}</option>
			     	</#list>
			     </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">总分年级名次：</span>
		<div class="filter-content">
			<input type="text"  class="form-control pull-left" style="width:80px;" id="rank1" nullable="false" vtype="int"  maxlength="8" value="1">
			<span class="pull-left" style="margin-right: 10px">-</span><input type="text" id="rank2" class="form-control pull-left" style="width:80px;" maxlength="8" nullable="false" vtype="int"  value="50">
		</div>
	</div>
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:" onclick="showInflection()">查询</a>
	</div>
	<div class="filter-item pull-right">
		<span class="tip tip-grey">
		*说明：红色科目为偏科科目
		</span>
	</div>
</div>
<div class="filter"> 
    <div id="inflectionDiv" class="inflectionDiv">
    
    </div> 
</div>
<script>
	$(function(){
		showInflection();
	})
	
	function showInflection(){
		var check = checkValue('.searchContentDiv_3');
	    if(!check){
	        return;
	    }
	    var examId=$("#examId").val();
		var subjectId=$("#subjectId_1").val();
		var rank1=$("#rank1").val().trim();
		var rank2=$("#rank2").val().trim();
		var rankInt1=parseInt(rank1);
		var rankInt2=parseInt(rank2);
		if(rankInt1>rankInt2){
			layer.tips('不能小于前名次!', $("#rank2"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		var url =  '${request.contextPath}/examanalysis/examGrade/inflectionList/page?subjectId='+subjectId+'&examId='+examId+'&rank1='+rankInt1+'&rank2='+rankInt2;
		$("#inflectionDiv").load(url);
	}
</script>