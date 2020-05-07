<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<a href="javascript:void(0);" class="page-back-btn" onclick="backIndex()"><i class="fa fa-arrow-left"></i> 返回</a>
	<div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">上课签到详情</h4>
		</div>
		<div class="box-body">

			<div class="filter">
				<div class="filter-item">
					<span class="filter-name">姓名：</span>
					<div class="filter-content">
						<p>${teacherName!}</p>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">时段：</span>
					<div class="filter-content">
						<div class="input-group" style="width:260px;">
							${beginDate!}
							<span class="">
								<i class="fa fa-minus"></i>
							</span>
							${endDate!}
						</div>
					</div>
				</div>
			</div>
			<table class="table table-striped">
				<thead>
					<tr>
						<th>序号</th>
						<th>签到日期</th>
						<th>节次</th>
						<th>班级</th>
						<th>签到情况</th>
					</tr>
				</thead>
				<tbody>
				<#if classAttences?exists&&classAttences?size gt 0>
		          	<#list classAttences as item>
					<tr>
						<td>${item_index+1}</td>
						<td>${item.clockDate?string('yyyy-MM-dd')}</td>
						<td>${item.sectionName!}</td>
						<td>${item.className!}</td>
						<td>
						<#if item.stuStatus==4>正常
						<#elseif item.stuStatus==2>迟到
						<#elseif item.stuStatus==1>缺课
						</#if>
						</td>
					</tr>
		      	    </#list>
		  	    <#else>
					<tr>
						<td  colspan="88" align="center">
						暂无数据
						</td>
					<tr>
		        </#if>
				</tbody>
			</table>
	
		</div>
	</div>
</div>
<#if classAttences?exists&&classAttences?size gt 0>
 <@htmlcom.pageToolBar container="#detailShowDiv" class="noprint"/>
 </#if>