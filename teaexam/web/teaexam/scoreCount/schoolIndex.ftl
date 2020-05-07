<div class="box box-default">
	<div class="box-body clearfix">
		<input type="hidden" id="type" value="0">
        <div class="filter">
			  <div class="filter-item">
                  <span class="filter-name">年份：</span>
                  <div class="filter-content">
	                  <select name="year" id="year" class="form-control" onchange="searchExamList()">
                            <#list minYear..maxYear as item>
                                <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                            </#list>
	                  </select>
                  </div>
              </div>
              <div class="filter-item">
				<span class="filter-name">考试：</span>
				<div class="filter-content">
					<select name="" id="examId" class="form-control" onChange="searchList();">
					<#if examList?exists && examList?size gt 0>
			            <#list examList as exam>
			                <option value="${exam.id!}" <#if examId?exists && examId==exam.id>selected="selected"</#if>>${exam.examName!}</option>
			            </#list>
			        <#else>
			            <option value="">--请选择--</option>
			        </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">类型：</span>
				<div class="filter-content">
					<select name="" id="resultType" class="form-control" onChange="searchList();">
			             <option value="1" <#if resultType?exists && resultType=='1'>selected="selected"</#if>>原始成绩</option>
			             <option value="2" <#if resultType?exists && resultType=='2'>selected="selected"</#if>>统计分析</option>
					</select>
				</div>
			</div>
			<button class="btn btn-blue" onclick="downloadScore()" id="addBtn">导出</button>
		</div>	
		<div class="table-container" id="detailDiv"></div>			
	</div>
</div>
<iframe style="display:none;" id="hiddenFrame" src=""></iframe>
<script>
searchList();

function searchExamList(){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/scoreCount/schIndex/page?year="+acadyear+"&type="+semester;
    $(".model-div").load(url);
}

function searchList(){
	var examId = $('#examId').val();
    var resultType = $('#resultType').val();
    if(examId == ''){
    	showMsgWarn('请选择要查询的考试！');
    	return;
    }
    var url = "${request.contextPath}/teaexam/scoreCount/sch/showDetail?examId="+examId+"&resultType="+resultType;
    $("#detailDiv").load(url);
}

function downloadScore(){
	var examId = $('#examId').val();
	var resultType = $('#resultType').val();
    if(examId == ''){
    	showMsgWarn('请选择要查询的考试！');
    	return;
    }
    var url ="${request.contextPath}/teaexam/scoreCount/sch/export?examId="+examId+"&resultType="+resultType;
    //window.open(url);
    document.getElementById('hiddenFrame').src=url;
}
</script>