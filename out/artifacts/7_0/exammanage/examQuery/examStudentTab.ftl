<#if (studentName!) != "">
	<div class="clearfix base-bg-gray">
		<div class="filter-item">
			<strong><span class="color-blue">${studentName!}</span>的考试成绩</strong>
			<span class="tip tip-grey ">(若姓名相同请通过其他方式查询！)</span>
		</div>
		<div class="filter-item filter-item-right">
			<span class="filter-name">考试名称：</span>
			<div class="filter-content">
				<select name="examId" id="examId" class="form-control" style="width:220px" onChange="changeExamId()">
					<#if examList?exists && (examList?size>0)>
						<#list examList as list>
							<option value="${list.id!}">${list.examName!}</option>
						</#list>
					<#else>
						<option value="">---请选择---</option>
					</#if>
				</select>
			</div>
		</div>
	</div>
	<div id="tableDiv">
		
	</div>
<#else>
	<div class="seachPersonal-query seachPersonal-result"> 
    	<span class="seachPersonal-icon"></span>
        <p class="seachPersonal-font1">暂无搜索结果</p>
        <p class="seachPersonal-font2">请检查输入的查询信息是否有误</p>
	</div>
</#if>
<script>
	$(function(){
		<#if (studentName!) != "">
		changeExamId();
		</#if>
	});
	
	function changeExamId() {
		var examId = $("#examId").val();
		var studentId = "${studentId!}";
		var clazzId = "${clazzId!}";
		var gradeId = "${gradeId!}";
		var str = "?examId="+examId+"&studentId="+studentId+"&clazzId="+clazzId+"&gradeId="+gradeId;
		var url = '${request.contextPath}/examquery/examStudent/List/page'+str;
		$("#tableDiv").load(url);
	}
	
</script>