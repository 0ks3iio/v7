<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#if eccSignInList?exists && (eccSignInList?size > 0)>
<div class="filter-item filter-item-right">
	<@htmlcomponent.printToolBar btn1="false" btn2="true" btn3="false" container=".print"/>
</div>
</#if>
<div class="table-container-body print">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>序号</th>
				<th>姓名</th>
				<th>行政班级</th>
				<th>刷卡班牌场地</th>
				<th>刷卡状态</th>
			</tr>
		</thead>
		<tbody>
		<#if eccSignInList?exists && eccSignInList?size gt 0>
			<#list eccSignInList as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.studentName!}</td>
				<td>${item.className!}</td>
				<td>${item.placeName!}</td>
				<td>
					<#if item.state == 0>
						未刷卡
					<#elseif item.state == 3>
						请假
					<#else>
						已刷卡
					</#if>
				</td>
			</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="8" align="center">暂无数据</td>
			</tr>
		</#if>
		</tbody>
	</table>
</div>