<div id="bb" class="tab-pane fade active in">
    <h3>成绩排名分布</h3>
	<div class="filter filter-f16 searchContentDiv_1">
           <div class="filter-item">
				<span class="filter-name">视图类型：</span>
				<div class="filter-content">
					<select name="viewType" id="viewType" class="form-control" onChange="showClassRank()">
						<option value="1">按图表展现</option>
						<option value="2">按列表展现</option>
					</select>
					
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">成绩类型：</span>
				<div class="filter-content">
					<select name="subjectId" id="subjectId_1" class="form-control" onChange="showClassRank()">
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
			<div class="filter-item">
				<span class="filter-name">名次：</span>
				<div class="filter-content">
					<input type="text"  class="form-control pull-left" style="width:80px;" id="rank1" nullable="false" vtype="int"  maxlength="8" value="1">
					<span class="pull-left" style="margin-right: 10px">-</span><input type="text" id="rank2" class="form-control pull-left" style="width:80px;" maxlength="8" nullable="false" vtype="int"  value="50">
				</div>
			</div>
			<div class="filter-item">
 				<a class="btn btn-blue" href="javascript:" onclick="showClassRank()">查询</a>
			</div>
    </div>
    <div class="filter"> 
		<span class="tip tip-grey ">
		统计人数大于名次区间，则是由于同名次存在
		</span>
	</div>
	<div class="filter"> 
        <div id="rankchart01" style="height:480px;overflow:auto;">
        
        </div> 
    </div>
	<h3>各科成绩分布</h3>
	<div class="filter filter-f16">
          <div class="filter-item">
				<span class="filter-name">科目：</span>
				<div class="filter-content">
					<select name="subjectId" id="subjectId_2" class="form-control" onChange="showScoreSection()">
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
    </div>
	<div class="filter"> 
        <div id="rankchart02" style="height:480px;overflow:auto;">
        
        </div> 
    </div>
</div>
<script>
	$(function(){
		showClassRank();
		showScoreSection();
	})
	function showClassRank(){
		var check = checkValue('.searchContentDiv_1');
	    if(!check){
	        return;
	    }
	    var examId=$("#examId").val();
		var subjectId=$("#subjectId_1").val();
		var viewType=$("#viewType").val();
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
		var url =  '${request.contextPath}/examanalysis/examGrade/rankNumChartOrList/page?subjectId='+subjectId+'&examId='+examId+'&viewType='+viewType+'&rank1='+rankInt1+'&rank2='+rankInt2;
		$("#rankchart01").load(url);
	}
	function showScoreSection(){
	    var examId=$("#examId").val();
		var subjectId=$("#subjectId_2").val();
		var url =  '${request.contextPath}/examanalysis/examGrade/sectionChart/page?subjectId='+subjectId+'&examId='+examId;
		$("#rankchart02").load(url);
	}
	
</script>