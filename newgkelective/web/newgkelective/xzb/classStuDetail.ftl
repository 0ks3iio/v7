<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${divideClass.className!}学生名单</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="gobackIndex()" >返回</a>
		</div>
	</div>
	<div class="box-body">
		<div class="clearfix">
		<#if type=="Y">
			<div class="tree-wrap" style="height:auto;width:12%;min-width:100px;margin-right:10px;">
				<h4>选择班级</h4>
			<#-- 	<div class="input-group">
					<input type="text" class="form-control">
					<span class="input-group-addon">
						<i class="fa fa-search"></i>
					</span>
				</div>  -->
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
								<#--  --<th>原行政班</th>-->
								<th>行政班</th>
								<th>走班班级</th>
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
								<#-- <td>${dto.oldClassName!}</td>  -->
								<td>${dto.className!}</td>
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
</script>