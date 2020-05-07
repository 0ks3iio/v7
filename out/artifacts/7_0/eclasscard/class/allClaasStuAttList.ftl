<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>序号</th>
					<th>节次</th>
					<th>班级</th>
					<th>课程名</th>
					<th>任课老师</th>
					<th>正常</th>
					<th>请假</th>
					<th>迟到</th>
					<th>缺课</th>
					<th>详情</th>
				</tr>
			</thead>
			<tbody>
			<#if classAttences?exists&&classAttences?size gt 0>
          	<#list classAttences as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.sectionName!}</td>
				<td>${item.className!}</td>
				<td>${item.subjectName!}</td>
				<td>${item.teacherRealName!}</td>
				<td>${item.zcStuNum!}</td>
				<td>${item.qjStuNum!}</td>
				<td>${item.cdStuNum!}</td>
				<td>${item.qkStuNum!}</td>
				<td><a href="javascript:void(0);" onclick="showDetail('${item.id!}','2')">查看</a></td>
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
<#if classAttences?exists&&classAttences?size gt 0>
 <@htmlcom.pageToolBar container="#showListDiv" class="noprint"/>
 </#if>