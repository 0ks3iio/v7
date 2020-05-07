<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if carPlanTestResults?exists&&carPlanTestResults?size gt 0>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>
						<label><input type="checkbox" id="carTestCheckboxAll" name="carTestCheckboxAll" class="wp" onchange="carCheckboxAllSelect()"><span class="lbl"></span></label>
					</th>
					<th>测评人姓名</th>
					<th>测评人班级</th>
					<th>调查名称</th>
					<th>最近测评时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list carPlanTestResults as test>
				<tr>
					<td>
						<label><input type="checkbox" name="carTestCheckbox" value="${test.studentId!}" class="wp"><span class="lbl"></span></label>
					</td>
					<td>${test.studentName!}</td>
					<td>${test.className!}</td>
					<td>生涯兴趣调查</td>
					<td>${(test.creationTime?string('yyyy-MM-dd HH:mm'))!}</td>
					<td>
						<a class="color-blue mr10" href="#" onClick="showTestResult('${test.studentId!}')">查看结果</a><a class="color-blue mr10" href="#" onClick="printPDF('${test.studentId!}')">导出pdf</a><a class="color-blue mr10" href="#" onClick="onBatchPrint('${test.studentId!}')">打印</a><a class="color-blue" href="#" onClick="deleteOne('${test.studentId!}')">删除</a>
					</td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
<@htmlcom.pageToolBar container="#resultListDiv" class="noprint"/>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">暂无相关数据</p>
		</div>
	</div>
</div>
</#if>
<script>
	function showTestResult(studentId) {
		var url = "${request.contextPath}/careerplan/teacher/testresult/show?studentId="+studentId;
		$("#showTestResultDiv").load(url);
		$("#showTestResultListDiv").attr("style","display:none");
		$("#showTestResultDiv").attr("style","display:block");
	}

	function carCheckboxAllSelect() {
		if($("#carTestCheckboxAll").is(':checked')){
			$('input:checkbox[name=carTestCheckbox]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$('input:checkbox[name=carTestCheckbox]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
</script>