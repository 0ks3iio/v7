<div class="table-switch-filter">
	<div class="filter filter-sm">
		<div class="filter-item">
			<span class="filter-name">班级&nbsp;</span>
			<input type="hidden" id="hcb" value="${hcb!0}">
			<div class="filter-content">
				<select id="classId" onchange="showBatchClass(this.value)">   
				<#if jxbList?exists && jxbList?size gt 0>
				<#list jxbList as jxb>	
					<option value="${jxb.id!}" <#if classId! == jxb.id!>selected</#if>>${jxb.className!}</option>
				</#list>	
				<#else>
				<option value="">暂无班级</option>
				</#if>
				</select>
			</div>
		</div>													
		<div class="filter-item">
			<span class="filter-name"></span>
			<div class="filter-content">
				<#-- <a class="btn btn-sm btn-blue" onclick="">新增班级</a>  -->
				<#if jxbList?exists && jxbList?size gt 0>
				<a class="btn btn-sm btn-blue" onclick="deleteClass()">删除</a>
				</#if>
			</div>
		</div>													
	</div>
</div>
<#-- leftTable content -->
<script language="javascript" type="text/javascript">
$(document).ready(function(){
	$("#${right!'0'}_sort").tablesorter({
		headers:{
			0:{sorter:false},
			1:{sorter:false},
			2:{sorter:false},
		},
		sortInitialOrder: 'desc',
		widgets: ['fixFirstNumber']  
	});
});
</script>
<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass">
	<div class="table-switch-data default labelInf">
		<span>总数：<em>${(dtoList?size)!0}</em></span>
		<span>男：<em>${manCount!0}</em></span>
		<span>女：<em>${woManCount!0}</em></span>
		<#if courseName?default('')!="">
		<span>${courseName!}：<em>${(courseAvg?string("#.##"))!'0'}</em></span>
		</#if>
	</div>
	<table class="table table-bordered table-striped table-hover no-margin mainTable tablesorter" style="font-size:1em;" id="${right!'0'}_sort">
		<thead>
			<tr>
				<th>
					<label class="pos-rel">
						<input onclick="selAll(event)" name="course-checkbox" type="checkbox" class="wp">
						<span class="lbl"></span>
					</label>
				</th>
				<th>序号</th>
				<th>姓名</th>
				<th>性别</th>
				<th>班级</th>
				<th>${courseName!}</th>
			</tr>
		</thead>
		<tbody>
			<#if dtoList?exists && (dtoList?size>0)>
				<#list dtoList as dto>
					<tr>
						<td>
							<label class="pos-rel">
								<#if xzbStus?exists && xzbStus?seq_contains(dto.studentId!'')>
									<input disabled name="course-checkbox" type="checkbox" class="wp disabled" value="">
								<#else>
									<input name="course-checkbox" type="checkbox" class="wp" value="${dto.studentId!}">
								</#if>
								<span class="lbl"></span>
							</label>
						</td>
						<td>${dto_index +1}</td>
						<td>${dto.studentName!}</td>
						<td>${dto.sex!}</td>
						<td>${dto.className!}</td>
						<td>${dto.subjectScore[subjectId]!}</td>
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
</div>