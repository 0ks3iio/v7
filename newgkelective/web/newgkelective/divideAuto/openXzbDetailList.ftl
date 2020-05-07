<div class="table-switch-data default">
	<span>总数：<em>${manNum+womanNum}</em></span>
	<span>男：<em>${manNum}</em></span>
	<span>女：<em>${womanNum}</em></span>
	<#list showSubjectName as sub>
	<span>${sub[1]!}：<em>${allScore[sub[0]]?default(0)?string("0.##")}</em></span>
	</#list>
</div>
<table class="table table-bordered table-striped js-sort-table show-stulist-table my-table">
	<thead>
		<tr>
			<th>序号</th>
			<th>姓名</th>
			<th>性别</th>
			<th>原行政班</th>
			<th>选课</th>
			<#list showSubjectName as sub>
			<th>${sub[1]!}</th>
			</#list>
			<th>总分</th>
		</tr>
	</thead>
	<tbody>
		<#if rightStuDtoList?exists && rightStuDtoList?size gt 0>
		<#list rightStuDtoList as item>
			<tr>
				<td>
					<span>${item_index+1}</span>
				</td>
				<td>${item.studentName!}</td>
				<td>${item.sex!}</td>
				<td><input type="hidden" class="classId" value="${item.classId!}">${item.className!}</td>
				<td><input type="hidden" class="subjectId" value="${item.chooseSubjects!}">${item.choResultStr!}</td>
				<#if item.subjectScore?exists && item.subjectScore?size gt 0>
					<#assign subjectScore=item.subjectScore>
					<#assign scores=0.0>
					<#list showSubjectName as sub>
						<#assign score1=subjectScore[sub[0]]?default(0)>
						<td>${score1}</td>
						<#assign scores=scores+score1>
					</#list>
					<td>${scores}</td>
				<#else>
					<#list showSubjectName as sub>
					<td>0</td>
					</#list>
					<td>0</td>
				</#if>
			</tr>
		</#list>
		</#if>
	</tbody>
</table>
<script>
	$(function(){
		// 通过js添加table水平垂直滚动条
		$('.my-table').DataTable({
			// 设置垂直方向高度
			// 禁用搜索
			searching: false,
	        // 禁止表格分页
	        paging: false,
	        // 禁止宽度自动
			autoWidth: false,
			info: false,
			order: [],
			// 禁用指定列排序
		    language: {
		    	 emptyTable:"暂无数据"
		    }
	    });
	  })
</script>