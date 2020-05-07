<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="filter filter-f16">
	<div class="filter-item filter-item-right">
		<@htmlcomponent.printToolBar container=".print" btn2="false" printDirection='true' printUp=5/>
		<a href="javascript:" class="btn btn-blue  detaileBack">返回</a>
	</div>
</div>
<div class="box box-default">
	<div class="box-body clearfix">
	    <div class="table-container">
			<div class="table-container-body print">
				<div class="filter">
					<div class="filter-item">
						<div class="filter-cotnent">考试编号：${emPlace.examPlaceCode!}</div>
					</div>
					<div class="filter-item">
						<div class="filter-cotnent">考试场地：${emPlace.placeName!}</div>
					</div>
					<#if groupName?exists>
					<div class="filter-item">
						<div class="filter-cotnent">科目组：${groupName!}</div>
					</div>
					</#if>
				</div>
			
				<table class="table table-bordered  table-condensed table-striped table-hover">
					<thead>
						<tr>
							<th class="text-center">座位号</th>
							<th class="text-center">姓名</th>
							<th class="text-center">考号</th>
							<th class="text-center">行政班</th>
							<th class="text-center">学号</th>
						</tr>
					</thead>
					<tbody>
						<#if (dtoList?exists && dtoList?size>0)>
						<#list dtoList as dto>
						<tr>
							<td class="text-center">${dto.seatNum!}</td>
							<td class="text-center">${dto.student.studentName!}</td>
							<td class="text-center">${dto.examNumber!}</td>
							<td class="text-center">${dto.className!}</td>
							<td class="text-center">${dto.student.studentCode!}</td>
						</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div>
		<div>
	</div>
</div>
<script>
$(function(){
	$('.detaileBack').on("click",function(){
		itemShowReport('1');
	});
});
</script>