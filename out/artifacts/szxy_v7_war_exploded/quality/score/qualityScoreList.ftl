<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table id="example" class="table table-bordered">
	<thead>
		<tr>
			<th>序号</th>
			<th>姓名</th>
			<th>学号</th>
			<th>性别</th>
			<th>行政班</th>
			<th>总分</th>
			<th>年级名次</th>
			<th>班级名次</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if studentDtos?exists&&studentDtos?size gt 0>
          	<#list studentDtos as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.studentName!}</td>
				<td>${item.studentCode!}</td>
				<td>${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
				<td>${item.className!}</td>
				<td>${(item.totalScore)?string("0.##")!}</td>
				<td>${item.gradeRank!}</td>
				<td>${item.classRank!}</td>
				<td><a href="javascript:void(0);" onclick="showDetail('${item.studentId!}')">查看详情</a></td>
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
<#if studentDtos?exists&&studentDtos?size gt 0>
	<@htmlcom.pageToolBar container="#showListDiv" class="noprint"/>
</#if>