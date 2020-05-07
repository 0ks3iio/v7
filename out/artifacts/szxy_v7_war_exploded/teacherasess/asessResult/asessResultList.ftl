<table class="table table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<#if classType?default("1")=="1">
					<th>班级</th>
					<th>班主任</th>
				<#else>
					<th>教学班</th>
					<th>任课教师</th>
				</#if>
				<#if resultType?default("")=="1">
					<th>本次考核方案考核分系数</th>
					<th>原始参照方案考核分系数</th>
					<th>考核分</th>
					<th>名次</th>
				<#elseif resultType?default("")=="2">
					<th>班级本次对比原始参照的进步人数</th>
					<th>进步率</th>
					<th>进步率排名</th>
				<#else>
					<th>总名次排名</th>
					<th>进步人次排名</th>
					<th>最终考核分</th>
					<th>最终考核名次</th>
				</#if>
			</tr>
		</thead>
		<tbody>
		<#if teacherAsessResults?exists>
			<#if resultType?default("")=="1">
				<#list teacherAsessResults as item>
					<tr>
						<td>${item_index+1}</td>
						<td>${item.className!}</td>
						<td>${item.teacherName!}</td>
						<td>${item.convertParam!}</td>
						<td>${item.referConvertParam!}</td>
						<td>${item.asessScore!}</td>
						<td>${item.rank!}</td>
					</tr>
				</#list>
			<#elseif resultType?default("")=="2">
				<#list teacherAsessResults as item>
					<tr>
						<td>${item_index+1}</td>
						<td>${item.className!}</td>
						<td>${item.teacherName!}</td>
						<td>${item.upStuNum!}</td>
						<td>${item.upScale?default(0)?string("#.#")}%</td>
						<td>${item.upScaleRank!}</td>
					</tr>
				</#list>
			<#else>
				<#list teacherAsessResults as item>
					<tr>
						<td>${item_index+1}</td>
						<td>${item.className!}</td>
						<td>${item.teacherName!}</td>
						<td>${item.rank!}</td>
						<td>${item.upScaleRank!}</td>
						<td>${item.score?default(0)?string("#.#")}</td>
						<td>${item.scoreRank!}</td>
					</tr>
				</#list>
			</#if>
		</#if>
		</tbody>
</table>
