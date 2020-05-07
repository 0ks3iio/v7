<div id="resultListDiv">
	<table class="table table-bordered table-striped mainTable">
	    <thead>
	        <tr>
	            <th width="10%">姓名</th>
	            <th width="15%">科目</th>
	            <th width="10%">班级</th>
	            <th >考试类型</th>
	            <th >教室</th>
	            <th >${PCKC!}</th>
	            <th >上课时间</th>
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
						<td>${item.subjectName!}</td>
						<td>${item.className!}</td>
						<td><#if item.classType?default('') == 'A'>选考<#else>学考</#if></td>
						<td>${item.placeName!}</td>
						<td>${PCKC!}${item.batch}</td>
						<td>
						<#if timeMap?exists>
							${timeMap[item.batch+'']!}
						</#if>
						</td>
					</tr>
				</#list>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
