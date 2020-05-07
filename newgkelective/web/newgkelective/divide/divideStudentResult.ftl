<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${divideClass.className!}学生名单</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="gobackIndex()" >返回</a>
			<#if (type! == 'A' || type! == 'B') && divideClass.classType?default('')=='2'>
			<a href="javascript:" class="btn btn-default" onclick="adjStudent('${divideClass.id!}')">调班</a>
			</#if>
		</div>
	</div>
	<div class="box-body">
		<div class="clearfix">
		<#if type! == 'Y' || type! == 'X' || type! == 'J'>
			<div class="tree-wrap" style="height:auto;width:12%;min-width:100px;margin-right:10px;">
				<h4>选择班级</h4>
				<div class="tree-list" style="height:600px;margin-top: 0;">
				<#if xzbList?exists && xzbList?size gt 0>
				<#list xzbList as xzb>
					<a <#if divideClass.id! == xzb.id!>class="active"</#if> href="javascript:showDetail('${xzb.id!}','${type!}');">${xzb.className!}</a>
				</#list>
				</#if>
				</div>
			</div>
		</#if>
		<div style="margin:auto;">
			<div class="table-container">
				<#if dtoList?exists && dtoList?size gt 0>
				<div class="table-container-header">共${dtoList?size}份结果</div>
				<#else>
				<div class="table-container-header">没有结果</div>
				</#if>
				<div class="table-container-body">
					<table class="table table-bordered table-striped table-hover">
						<thead>
							<tr>
								<th>序号</th>
								<th>姓名</th>
								<th>学号</th>
								<th>性别</th>
								<th>原行政班</th>
								<#if showNewXZB!true>
									<th>新行政班</th>
								</#if>
								<th>已选学科</th>
								<#if isShowAB?default(true)>
								<th>选考班</th>
								<th>学考班</th>
								<#elseif type! == 'Y' || type! == 'X'>
								<th>二科组合班</th>
								</#if>
							</tr>
						</thead>
						<tbody>
						<#if dtoList?exists && dtoList?size gt 0>
						<#list dtoList as dto>
							<tr>
								<td>${dto_index+1}</td>
								<td>${dto.studentName!}</td>
								<td>${dto.studentCode!}</td>
								<td>${dto.sex!}</td>
								<td>${dto.oldClassName!}</td>
								<#if showNewXZB!true>
									<td>${dto.className!}</td>
								</#if>
								<td>${dto.chooseSubjects}</td>
								<#if isShowAB?default(true)>
									<td>
									<#if dto.jxbAClasss?exists && dto.jxbAClasss?size gt 0>
									<#list dto.jxbAClasss as className>
										<#if className_index != 0>
										、
										</#if>
										${className!}					
									</#list>
									</#if>
									</td>
									<td>
									<#if dto.jxbBClasss?exists && dto.jxbBClasss?size gt 0>
									<#list dto.jxbBClasss as className>
										<#if className_index != 0>
										、
										</#if>
										${className!}					
									</#list>
									</#if>
									</td>
								<#elseif type! == 'Y' || type! == 'X'>
									<td>
									<#if dto.otherClasss?exists && dto.otherClasss?size gt 0>
									<#list dto.otherClasss as className>
										<#if className_index != 0>
										、
										</#if>
										${className!}					
									</#list>
									</#if>
									</td>
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
	</div>
</div>
<script>
	function gobackIndex(){
		$('#tablist li[class="active"] a').click();
	}
	<#if (type! == 'A' || type! == 'B') && divideClass.classType?default('')=='2'>
	function adjStudent(jxbId){
		var url = '${request.contextPath}/newgkelective/studentAdjust/${divideId!}/changeStudentByBatch/page?jxbId='+jxbId+'&type=${type!}';
		$("#aa").load(url);
	}
	</#if>
</script>