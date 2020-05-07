<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title"><#if jxbClass?exists>${jxbClass.className!}</#if>学生名单</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="gobackIndex()">返回</a>
		</div>
	</div>
	<div class="box-body">
		<div class="table-container">
			<#if studentList?exists && studentList?size gt 0>
			<div class="table-container-header">共${studentList?size}份结果</div>
			<#else>
			<div class="table-container-header">没有结果</div>
			</#if>
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						 <tr>
				            <th>序号</th>
				            <th>学号</th>
				            <th>姓名</th>
				            <th>性别</th>
				            <th>行政班</th>
				            <th><#if subjectType?default("A") == "A">选考<#else>学考</#if>科目</th>
				            <#if isCanEdit>
				             <th>操作</th>
				            </#if>
				        </tr>
					</thead>
					<tbody>
					<#if studentList?exists && studentList?size gt 0>
			            <#list studentList as student>
			                <tr>
			                    <td class="index_i">${student_index + 1}</td>
			                    <td>${student.studentCode!}</td>
			                    <td>${student.studentName!}</td>
			                    <td>${student.sex!}</td>
			                    <td>${student.className!}</td>
			                    <td>${student.chooseSubjects!}</td>
			                    <#if isCanEdit>
					             <td><a class="table-btn color-blue" href="javascript:void(0)" onclick="moveStu('${student.studentId!}')">调整</a></td>
					            </#if>
			                </tr>
			            </#list>
			        </#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="layer layer-stuMoveClass"></div>
<script>
	function gobackIndex(){
		<#if subjectType?default('')=='B'>
			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultB/page';
		<#else>
			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultA/page';
		</#if>
		$("#showList").load(url);
	}
	function refreshJxbStuList(){
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/showJxbStuResult?teachClassId=${jxbClass.id!}&subjectType=${subjectType!}';
		$("#showList").load(url);
	}
	<#if isCanEdit>
	function moveStu(studentId){
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/moveStudent?studentId=' + studentId+'&subjectType=${subjectType!}&containType=1';
		$(".layer-stuMoveClass").load(url, function () {
			layer.open({
				type: 1,
				shade: 0.5,
				title: '学生调整',
				move: true,
				area: ['600px', '500px'],
				btn: ['确定', '取消'],
				btnAlign: 'c',
				yes: function (index) {
					doSaveMoveClass();
				},
				content: $('.layer-stuMoveClass')
			});
		});
		
	}
	</#if>
</script>
