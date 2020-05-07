<#if clsChecks?exists && clsChecks?size gt 0>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th>班级</th>
			<th>扣分情况</th>
		</tr>
	</thead>
	<tbody>
		<#list clsChecks as item>
		   <tr>
		      <td width="110px;">${item[0]}</td>
		      <td style="word-break:break-all;">${item[1]}</td>
		   </tr>
	    </#list>
	</tbody>
</table>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有班级信息</p>
		</div>
	</div>
</div>
</#if>