<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>排序号</th>
				<th>账号</th>
				<th>姓名</th>
				<th>用户状态</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
				<#if userList?exists&&userList?size gt 0>
		          	<#list userList as user>
					<tr>
						<td>${user_index+1!}</td>
						<td>${user.username!}</td>
						<td>${user.realName!}</td>
						<td><#if user.userState == 1>正常<#else> 注销  </#if></td>
						<td>${user.creationTime?string('yyyy-MM-dd')!}</td>
						<td>
					     	<a href="javascript:void(0);" class="js-show-userpower" value="${user.id!}">查看</a>
					    </td>
					</tr>
		      	    </#list>
		  	    <#else>
					<tr>
						<td  colspan="6" align="center">
						 暂无用户数据
						</td>
					<tr>
		        </#if>
		</tbody>
	</table>
</div>
<div class="table-container-footer">
	<nav class="nav-page no-margin clearfix">
		<ul class="pagination pagination-sm pull-right">
		  <#if userList?exists&&userList?size gt 0>
				<@htmlcom.pageToolBar container="#showUserList" class="noprint"/>
		  </#if>
		</ul>
	</nav>
</div>  <!-- table-container-footer -->
<script>
//查看
$('.js-show-userpower').on('click', function(e){
    var userId = $(this).attr("value");
    var url =  '${request.contextPath}/system/user/power/findUserPower/page?userId='+userId;
    $("#tabList").load(url);
})
</script>