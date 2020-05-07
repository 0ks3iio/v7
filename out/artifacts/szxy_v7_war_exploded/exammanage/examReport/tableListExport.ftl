<script type="text/javascript" src="${resourceUrl}/js/LodopFuncs.js"></script>
<script type="text/javascript" src="${resourceUrl}/components/jquery/dist/jquery.js"></script>
<link rel="stylesheet" href="${resourceUrl}/css/pages.css" />
<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css" />
<link rel="stylesheet" href="${resourceUrl}/css/components.css" />
<link rel="stylesheet" href="${resourceUrl}/css/iconfont.css" />
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.min.css" />
<input type="hidden" id="batchIdLeft" value="${batchId!}">
<input type="hidden" id="doNotPrint" value="${doNotPrint!}">
<div class="box box-default">
	<div class="box-body clearfix print">
		<div class="row" >	
		<#if (dtoList?exists && dtoList?size>0)>
		<#list dtoList as item>
			<div class="col-xs-4"  <#if (item_index%15==0) && (item_index>0)>style="page-break-before:always"</#if>> 
				<div class="box-boder exaRoom-border">
					<h1 class="exaRoom-fontSize text-center">${item.seatNum!}</h1>
					<table class="table table-bordered table-striped table-condensed table-hover exaRoom-table" style="margin-bottom:0px">
						<tbody>
							<tr>
								<td class="text-center">姓名：</td>
								<td>${item.student.studentName!}</td>
							</tr>
							<tr>
								<td class="text-center">考号：</td>
								<td>${item.examNumber!}</td>
							</tr>
							<tr>
								<td class="text-center">班级：</td>
								<td>${item.className!}</td>
							</tr>
							<tr>
								<td class="text-center">考场：</td>
								<td>${examPlaceCode!}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</#list>
		</#if>
		</div>
	</div>
</div>
<script>
$(function(){
	window.parent.doPrint();
})

function getSubContent() {
	return getPrintContent(jQuery(".print"));
}
</script>

