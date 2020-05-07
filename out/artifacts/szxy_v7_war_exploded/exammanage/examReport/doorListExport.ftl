<script type="text/javascript" src="${resourceUrl}/js/LodopFuncs.js"></script>
<script type="text/javascript" src="${resourceUrl}/components/jquery/dist/jquery.js"></script>
<link rel="stylesheet" href="${resourceUrl}/css/pages.css" />
<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css" />
<link rel="stylesheet" href="${resourceUrl}/css/components.css" />
<link rel="stylesheet" href="${resourceUrl}/css/iconfont.css" />
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.min.css" />
<style>
</style>
<input type="hidden" id="batchIdLeft" value="${batchId!}">
<input type="hidden" id="doNotPrint" value="${doNotPrint!}">
<div class="row">
	<div class="col-sm-12">
		<div class="box box-default">
			<div class="box-body">
			<div class="print">
				<div class="filter">
					<div class="filter-item">
						<div class="filter-cotnent" style="font-size:10px;">考试名称：${examName!}${emPlace.examPlaceCode!}考场</div>
					</div>
					<div class="filter-item">
						<div class="filter-cotnent" style="font-size:10px;">考试场地：${emPlace.placeName!}</div>
					</div>
					<div class="filter-item">
						<div class="filter-cotnent" style="font-size:10px;">考生数量：${emPlace.stuNum?default(0)}人</div>
					</div>
					<div class="filter-item">
						<div class="filter-cotnent" style="font-size:10px;">考号范围：${emPlace.stuNumRange!}</div>
					</div>
				</div>
				<table class="table table-bordered  table-condensed table-striped table-hover" style="font-size:12px;">
					<thead>
						<tr>
							<th class="text-center" style="padding:2.1px">座位号</th>
							<th class="text-center" style="padding:2.1px">姓名</th>
							<th class="text-center" style="padding:2.1px">考号</th>
							<th class="text-center" style="padding:2.1px">行政班</th>
							<th class="text-center" style="padding:2.1px">学号</th>
						</tr>
					</thead>
					<tbody>
						<#if (dtoList?exists && dtoList?size>0)>
						<#list dtoList as dto>
						<tr>
							<td class="text-center" style="padding:2.1px">${dto.seatNum!}</td>
							<td class="text-center" style="padding:2.1px">${dto.student.studentName!}</td>
							<td class="text-center" style="padding:2.1px">${dto.examNumber!}</td>
							<td class="text-center" style="padding:2.1px">${dto.className!}</td>
							<td class="text-center" style="padding:2.1px">${dto.student.studentCode!}</td>
						</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div>
			</div>
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
