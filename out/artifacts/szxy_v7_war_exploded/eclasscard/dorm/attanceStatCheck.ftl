<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<a href="javascript:goback()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">宿舍考勤汇总详情</h4>
	</div>
	<div class="box-body">

		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<p>${studentName!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">寝室号：</span>
				<div class="filter-content">
					<p>${roomName!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">时段：</span>
				<div class="filter-content">
					<div class="input-group" style="width:260px;">
						${startTime!}
						<span class="">
							<i class="fa fa-minus"></i>
						</span>
						${endTime!}
					</div>
				</div>
			</div>
		</div>
		
		<div class="table-container">
			
			<div class="table-container-body">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>序号</th>
							<th>签到日期</th>
							<th>考勤时段</th>
							<th>签到情况</th>
						</tr>
					</thead>
					<tbody>
						<#if attenceList?exists && attenceList?size gt 0>
							<#list attenceList as item>
								<tr>
									<td>${item_index+1}</td>
									<td>${(item.dateTime?string("yyyy-MM-dd"))!}</td>
									<td>${item.periodTime!}</td>
									<td><#if item.status==1><span class="color-red">未签到</span><#elseif item.status==2>请假<#else>已签到</#if></td>
								</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<@htmlcom.pageToolBar container="#checkDiv">
				</@htmlcom.pageToolBar>
			</div>
		</div>

	</div>
</div>
