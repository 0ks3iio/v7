<div class="filter">
    <div class="filter-item filter-item-left">
		<span class="filter-name">考试名称：${examName!}</span>
		<span class="filter-name ml10">中心校：${cenSchName!}</span>
	</div>
	<div class="filter-item filter-item-left">
		<span class="filter-name">学科：</span>
		<div class="filter-content">
			<select name="" id="subjectId" class="form-control" onChange="searchCenList();">
			<#if subList?exists && subList?size gt 0>
			       <option value="">--全部--</option>
			    <#list subList as item>
			       <option value="${item.id!}" <#if '${item.id}'=='${subjectId!}'>selected="selected"</#if>>${item.subjectName!}</option>
			    </#list>
			<#else>
		        <option value="">--请选择--</option>
		    </#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<a class="btn btn-blue" href="#" onClick="doExportCen();">导出</a>
		<a class="btn btn-blue" href="#" onClick="toDetails();">返回</a>
	</div>
</div>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学科</th>
					<th>单位</th>
					<th>参与考试人数</th>
					<th>优秀人数</th>
					<th>优秀率</th>
					<th>合格人数</th>
					<th>合格率</th>
					<th>不合格人数</th>
					<th>不合格率</th>
					<th>平均分</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			    <#list dtoList as item>
				<tr>
				    <td>${item_index+1!}</td>
				    <td>${item.subjectName!}</td>
				    <td>${item.schoolName!}</td>
				    <td>${item.countNum!}</td>
				    <td>${item.yxNum!}</td>
				    <td>${item.yxPer!?string("#.##")}%</td>
				    <td>${item.hgNum!}</td>				  
				    <td>${item.hgPer!?string("#.##")}%</td>
				    <td>${item.bhgNum!}</td>				 
				    <td>${item.bhgPer!?string("#.##")}%</td>
				    <td>${item.avgScore!?string("#.##")}</td>
				</tr>
				</#list>
			<#else>
				<tr>
					<td colspan="11" align="center">暂无数据</td>
				</tr>
			</#if>
			</tbody>
		</table>
	</div>
</div>
<iframe style="display:none;" id="hiddenFrame" src=""></iframe>
<script>
function searchCenList(){
    var subjectId = $('#subjectId').val();
    var url = "${request.contextPath}/teaexam/scoreCount/centerSchool/list?examId=${examId!}&cenSchId=${cenSchId!}&subjectId="+subjectId;
    $('#scoreTabDiv').load(url);
}

function toDetails(){
	var url = "${request.contextPath}/teaexam/scoreCount/showScoreDetail?examId=${examId!}&subjectId=${subjectId!}&schoolId=${cenSchId!}";
    $('#scoreTabDiv').load(url);
}

function doExportCen(){
    var subjectId = $('#subjectId').val();
    //document.location.href
    var url="${request.contextPath}/teaexam/scoreCount/centerSchool/export?examId=${examId!}&cenSchId=${cenSchId!}&subjectId="+subjectId;
    document.getElementById('hiddenFrame').src=url;
}
</script>