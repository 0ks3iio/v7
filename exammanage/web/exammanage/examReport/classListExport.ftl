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
	<div class="box-body clearfix">
	<div class="print">
	  	<div class="table-container">
		    <div class="table-container-body">
				<#if (stuDtoList?exists && stuDtoList?size>0)>
				<table class="table table-bordered table-condensed table-striped table-hover" style="font-size:12px;">
					<thead>
						<tr>
							<th class="text-center" style="padding:2.3px">学号</th>
							<th class="text-center" style="padding:2.3px">姓名</th>
							<th class="text-center" style="padding:2.3px">班级</th>
							<th class="text-center" style="padding:2.3px">考号</th>
							<th class="text-center" style="padding:2.3px">考场编号</th>
							<th class="text-center" style="padding:2.3px">考试场地</th>
							<th class="text-center" style="padding:2.3px">座位号</th>
						</tr>
					</thead>
					<tbody>
						    <#list stuDtoList as item>
							<tr>
								<td class="text-center" style="padding:2.3px">${item.student.studentCode!}</td>
								<td class="text-center" style="padding:2.3px">${item.student.studentName!}</td>
								<td class="text-center" style="padding:2.3px">${className!}</td>
								<td class="text-center" style="padding:2.3px">${item.examNumber!}</td>
								<td class="text-center" style="padding:2.3px">${item.placeNumber!}</td>
								<td class="text-center" style="padding:2.3px">${item.placeName!}</td>
								<td class="text-center" style="padding:2.3px">${item.seatNum!}</td>
							</tr>
							</#list>
					</tbody>
				</table>
				<#else>
					<h3 class="lighter smaller">
						考试班级不存在
					</h3>
				</#if>												
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