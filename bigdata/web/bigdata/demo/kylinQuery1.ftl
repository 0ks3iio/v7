<div class="table-container no-margin">
<div class="table-container-body ">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th >列1</th>
				<th >列2</th>
				<th >列3</th>
			</tr>
		</thead>
		<tbody>
			<#if kylinList?exists&&kylinList?size gt 0>
	          	<#list kylinList as kylin>
					<tr>
						<td>${kylin.acadyear!}</td>
						<td>${kylin.semester!}</td>
						<td>${kylin.count!}</td>
					</tr>
	      	    </#list>
	  	    <#else>
				<tr >
					<td  colspan="2" align="center">
					暂无数据
					</td>
				<tr>
	        </#if>
		</tbody>
	</table>
</div>
</div>
<script type="text/javascript">

</script>