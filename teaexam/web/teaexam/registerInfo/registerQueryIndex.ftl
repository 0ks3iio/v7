<div class="box box-default">
	<div class="box-body">
		<div class="filter" id="searchType">
			<div class="filter-item">
				<span class="filter-name">年份：</span>
                <div class="filter-content">
                    <select name="year" id="year" class="form-control" onchange="searchExam()" style="width:135px">
                            <#list minYear..maxYear as item>
                                <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                            </#list>
                    </select>
                </div>
			</div>
			<div class="filter-item">
				<span class="filter-name">考试/培训：</span>
                <div class="filter-content">
                    <select name="examId" id="examId" class="form-control" onchange="searchList()" style="width:200px">
                    	<#if examList?exists && examList?size gt 0>
                    	<#list examList as ex>
                    		<option value="${ex.id!}">${ex.examName!}</option>
                    	</#list>
                    	</#if>
                    </select>
                </div>
			</div>
		<button class="btn btn-blue" onclick="exportList()" id="addBtn">导出</button>
		</div>						
		<div class="table-container" id="showList">
			
		</div>
	</div>
</div>


<script>
$(function(){	
	searchList();
});

function searchExam(){
	var year = $('#year').val();
    var url = "${request.contextPath}/teaexam/query/registerInfo/page?year="+year;
    $(".model-div").load(url);
}

function searchList(){
    var examId = $('#examId').val();
    var url = "${request.contextPath}/teaexam/query/registerInfo/list?examId="+examId;
    $("#showList").load(url);
}

</script>