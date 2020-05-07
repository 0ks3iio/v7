<div class="tab-content" id="resultListDiv">
<table class="table table-striped table-hover no-margin">
	<thead>
		<tr>
			<th>组合班</th>
			<th>所属行政班</th>
			<#-->th>需走班情况</th-->
			<th>总人数</th>
			<th>男生</th>
			<th>女生</th>
			<th class="noprint">查看学生</th>
		</tr>
	</thead>
	<tbody>
		<#if groupClassList?? && (groupClassList?size>0)>
			<#list groupClassList as item>
			<tr>
				<td>${item.groupName!}</td>
				<td>${item.className!}</td>
				<#-- >td>
				<#if item.groupType == '1'>
				无需走班
				<#elseif item.groupType == '2'>
				<#if rounds.openClass?default('') == '1'>
				X+学考走班
				<#else>
				X走班
				</#if>
				<#elseif item.groupType == '3'>
				全走班
				</#if>
				</td -->
				<td>${item.number!}</td>
				<td>${item.manNumber!}</td>
				<td>${item.womanNumber!}</td>
				<td class="noprint">
				<#if (item.number?default(0)>0)>
				<a href="#" onclick="showGroupStu('${item.id!}')">查看</a>
				</#if>
				</td>
			</tr>
			</#list>
		</#if>
	</tbody>
</table>
</div>
<script>
	function showGroupStu(groupId){
		var url = "${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/detail/page?groupId="+groupId;
		$("#resultListDiv").load(url);
	}
	function reloadListData(){
		itemShowList(1);
	}
</script>
