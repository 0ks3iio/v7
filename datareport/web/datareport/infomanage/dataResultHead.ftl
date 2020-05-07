<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
			<div class="box-body clearfix">
				<div class="filter">
					<div class="filter-item">
						<a class="btn btn-blue" href="#" onClick="<#if existExcel>loadExcel('${taskId!}','REPORT_TASK_ATT')<#else>printExcel('${infoId!}','${taskId!}','${tableType!}','1',this)</#if>">导出excel</a>
						<a class="btn btn-white" href="#" onClick="printPDF('${infoId!}','${taskId!}',1,'${tableType!}')">导出pdf</a>
					</div>
					<#if isAttachment==1&&fileNum!=0>
					<div class="filter-item filter-item-right">
						<a class="btn btn-blue" href="${request.contextPath}/datareport/table/downattfiles?taskId=${taskId!}">下载附件</a>
					</div>
					</#if>
				</div>
				<div class="box-body" style="background: #f2f6f9;" id="taskResultDiv">
					
				</div>
			</div>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->
<script>
	$(function(){
		showBreadBack(goBackInfoDetail,true,"问卷数据");		
		$("#taskResultDiv").load("${request.contextPath}/datareport/infomanage/showresults?infoId=${infoId!}&taskId=${taskId!}&tableType=${tableType!}");
	})
	
	function goBackInfoDetail() {
		showInfoHead('${infoId!}');
	}
</script>