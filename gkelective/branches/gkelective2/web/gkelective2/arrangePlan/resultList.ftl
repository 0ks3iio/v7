<div id="resultListDiv">
	<table class="table table-bordered table-striped mainTable">
	    <thead>
	        <tr>
	            <th width="10%"><#if searchViewTypeRedio?default('') == '1'>${PCKC!}<#elseif searchViewTypeRedio?default('') == '2'>科目</#if></th>
	            <th width="15%">班级</th>
	            <th width="10%">选考类型</th>
	            <th ><#if searchViewTypeRedio?default('') == '1'>科目<#elseif searchViewTypeRedio?default('') == '2'>${PCKC!}</#if></th>
	            <th >总人数</th>
	            <th >男生</th>
	            <th >女生</th>
	            <th >班级平均分</th>
	            <th >老师安排</th>
	            <th >教室安排</th>
	            <th class="noprint">查看学生</th>
	        </tr>
	    </thead>
	    <tbody>
		<#if gkBatchMap?? && (gkBatchMap?size>0)>
			<#list gkBatchMap?keys as key>
				<#list gkBatchMap[key] as item>
					<tr>
						<#if item_index == 0>
							<td rowSpan='${gkBatchMap[key]?size}'>${key}</td>
						</#if>
						<td>${item.className!}</td>
						<td><#if item.classType?default('') == 'A'>选考<#else>学考</#if></td>
						<td><#if searchViewTypeRedio?default('') == '1'>${item.subjectName!}<#elseif searchViewTypeRedio?default('') == '2'>${PCKC!}${item.batch!}</#if></td>
						<td>${item.number!}</td>
						<td>${item.manNumber!}</td>
						<td>${item.womanNumber!}</td>
						<td>${item.averageScore!}</td>
						<td>${item.teacherName!}</td>
						<td>${item.placeName!}</td>
						<td>
						<a href="#" onclick="showSingleStu('${item.id!}','${item.teachClassId!}')">查看</a>
						</td>
					</tr>
				</#list>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	function showSingleStu(batchId,teachClassId){
		var url = "${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/single/detail/page?planId=${planId!}&viewtype=${viewtype!}&batchId="+batchId+"&teachClassId="+teachClassId;
		$("#resultListDiv").load(url);
	}
</script>