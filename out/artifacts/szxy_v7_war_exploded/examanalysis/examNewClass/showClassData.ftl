<#if summary?exists>
<div class="explain">
	<p>${summary!}</p>
</div>
</#if>
	<#if statList?exists && statList?size gt 0>
<div class="table-container">
	<div class="table-container-body" style="overflow-x: auto;">
	<#if tableType?default("1")=='1'>
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
				<#if subjectDtoList?exists && subjectDtoList?size gt 0>
				<th class="text-center" colspan="${subjectDtoList?size + 8}">${title!}</th>
				<#else>
					<th class="text-center" colspan="">${title!}</th>
					</#if>
				</tr>
				<tr>
					<th>序号</th>
					<th>考号</th>
					<th>学生</th>
					<#if subjectDtoList?exists && subjectDtoList?size gt 0>
						<#list subjectDtoList as subjectDto>
							<th>${subjectDto.subjectName!}</th>
						</#list>
					</#if>			
					<th>总分</th>
					<th>标准分T（年级）</th>
					<th>百分等级分数（年级）</th>
					<th>年级排名</th>
					<th>班级排名</th>
				</tr>
			</thead>
			<tbody>
				<#if statList?exists && statList?size gt 0>
					<#list statList as item>
		 				<tr>
						    <td>${item_index+1}</td>
						    <td>${item.examNum!}</td>
						    <td>${item.studentName!}</td>
						    <#if subjectDtoList?exists && subjectDtoList?size gt 0>
								<#list subjectDtoList as subjectDto>
									<th><#if emStatMap[item.studentId+subjectDto.subjectId]?exists>${emStatMap[item.studentId+subjectDto.subjectId].score?default(0.0)?string('0.#')}<#else>-</#if></th>
								</#list>
							</#if>
							<td>${item.score?default(0.0)?string('0.#')}</td>
							<td>${item.scoreT?default(0.0)?string('0.#')}</td>
							<td>${item.scoreLevel?default(0.0)?string('0.#')}</td>
						    <td>${item.gradeRank!}</td>
						    <td>${item.classRank!}</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	<#else>
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
				<#if subjectDtoList?exists && subjectDtoList?size gt 0>
					<#assign col = 0>
					<#list subjectDtoList as subjectDto>
						<#if subjectDto.subType?default("")=="1">
						<#assign col = col+4>
						<#else>
						<#assign col = col+3>
						</#if> 
					</#list>
					<th class="text-center" colspan="${col+3}">${title!}</th>
				<#else>
					<th class="text-center" colspan="3">${title!}</th>
				</#if>
				</tr>
				<tr>
					<th rowspan="2">序号</th>
					<th rowspan="2">考号</th>
					<th rowspan="2">学生</th>
					<#if subjectDtoList?exists && subjectDtoList?size gt 0>
						<#list subjectDtoList as subjectDto>
							<th <#if subjectDto.subType?default("")=="1">colspan="4"<#else>colspan="3"</#if> class="text-center">${subjectDto.subjectName!}</th>
						</#list>
					</#if>
				</tr>
				<tr>
					<#if subjectDtoList?exists && subjectDtoList?size gt 0>
						<#list subjectDtoList as subjectDto>
							<#if subjectDto.subType?default("")=="1">
								<th aria-controls="dynamic-table" aria-sort="descending" >考试分</th>
								<th>年级排名</th>
								<th>班级排名</th>
								<th>赋分</th>
							<#else>
								<th aria-controls="dynamic-table" aria-sort="descending" >考试分</th>
								<th>年级排名</th>
								<th>班级排名</th>
							</#if>
						</#list>
					</#if>
				</tr>
			</thead>
			<tbody>
 				<#if statList?exists && statList?size gt 0>
					<#list statList as item>
					<tr>
						<td>${item_index+1}</td>
					    <td>${item.examNum!}</td>
					    <td>${item.studentName!}</td>
					    <#if subjectDtoList?exists && subjectDtoList?size gt 0>
							<#list subjectDtoList as subjectDto>
								<#if emStatMap[item.studentId+subjectDto.subjectId]?exists>
									<td>${emStatMap[item.studentId+subjectDto.subjectId].score?default(0.0)?string('0.#')}</td>
									<td>${emStatMap[item.studentId+subjectDto.subjectId].gradeRank}</td>
									<td>${emStatMap[item.studentId+subjectDto.subjectId].classRank}</td>
									<#if subjectDto.subType?default("")=="1">
									<td>${emStatMap[item.studentId+subjectDto.subjectId].conScore?default(0.0)?string('0.#')}</td>
									</#if>
								<#else>
									<td>-</td><td>-</td><td>-</td>
									<#if subjectDto.subType?default("")=="1"><td>-</td></#if>
								</#if>
							</#list>
						</#if>
					</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</#if>
	</div>
</div>
<#else>
		<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
	</#if>
<script>
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	LODOP.SAVE_TO_FILE("${title!}.xls");
}
</script>