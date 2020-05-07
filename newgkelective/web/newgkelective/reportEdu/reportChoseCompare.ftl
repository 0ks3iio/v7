<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">机构1：</span>
				<div class="filter-content">${names[0]!}</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">机构2：</span>
				<div class="filter-content">${names[1]!}</div>
			</div>
		</div>
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>单位名称 </th>
					<#list courses1 as course>
						<th>${course!}</th>
					</#list>
				</tr>
			</thead>
			<tbody>
				<#list compareIdArr as id>
					<tr>
					<td>${names[id_index]!}</td>
					<#assign dataList=chooseCourse1[id]>
					<#list courses1 as course>
					<td>
						
						<span <#if maxSingleList[course_index]==id_index>class="color-red"</#if>>${dataList[course_index][0]!}</span>
						<span class="color-999">（${dataList[course_index][1]!}）</span>
					</td>
					</#list>
				</tr>
				</#list>
				
	
			</tbody>
		</table>
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th rowspan="2">序号 </th>
					<th rowspan="2">课程组合</th>
					<th colspan="2" class="text-center">${names[0]!}</th>
					<th colspan="2" class="text-center">${names[1]!}</th>
				</tr>
				<tr>
					<th>选择人数</th>
					<th>占比</th>
					<th>选择人数</th>
					<th>占比</th>
				</tr>
			</thead>
			<tbody>
			<#list chooseCourse2 as item>
				<tr>
					<td>${item_index+1}</td>
					<td>${item[0]!}</td>
					<td><span <#if maxThreeList[item_index]==0>class="color-red"</#if> >${item[1]!}</span></td>
					<td>${item[2]!}</td>
					<td><span <#if maxThreeList[item_index]==1>class="color-red"</#if>>${item[3]!}</span></td>
					<td>${item[4]!}</td>
				</tr>
			</#list>
			</tbody>
		</table>
	</div>
</div>
<script>
	$(function(){
		showBreadBack(toMyBack,true,"对比结果");
	})
	function toMyBack(){
		$("#showDate").load("${request.contextPath}/newgkelective/edu/itemList/page?type=1");
	}
</script>
