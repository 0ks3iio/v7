<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>排序号</th>
			<th>班级</th>
			<th>姓名</th>
			<th>学号</th>
			<th>类型</th>
			<th>项目</th>
			<th width="200px;">备注</th>
			<th>级别</th>
			<th>奖级</th>
			<th>分值</th>
			<th>奖励时间</th>
		</tr>
	</thead>
	<tbody>
	<#if dyStudentRewardPointList?exists && dyStudentRewardPointList?size gt 0>
	    <#list dyStudentRewardPointList as item>
		<tr>
			<td style="white-space: nowrap">${item.orderNum!}</td>
			<td style="white-space: nowrap">${item.className!}</td>
			<td style="white-space: nowrap">${item.studentName!}</td>
			<td style="white-space: nowrap">${item.stucode!}</td>
			<td>${item.rewardClasses!}<#if item.projectRemark?default("")!="">/${item.projectRemark!}</#if></td>
			<td>${item.projectName!}</td>
			<td>${item.remark!}</td>
			<td style="white-space: nowrap">${item.rewardGrade!}</td>
			<td style="white-space: nowrap">${item.rewardLevel!}</td>
			<td style="white-space: nowrap">${(item.rewardPoint)?string('#.#')!}</td>
			<td style="white-space: nowrap">${(item.creationTime?string("yyyy-MM-dd HH:mm"))!}</td>
		</tr>
		</#list>
    </#if>
	</tbody>
</table>
<#-- <#if dyStudentRewardPointList?exists && dyStudentRewardPointList?size gt 0>
  	<@htmlcom.pageToolBar container="#showList" class="noprint"/>
</#if> -->