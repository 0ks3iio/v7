<a data-toggle="dropdown" href="javascript:;" class="dropdown-toggle">
<#if user.photoPath?default("") != "">
	<img class="nav-user-photo" src="${user.photoPath}" alt="" />
<#else>
	<img class="nav-user-photo" src="${resourceUrl}/images/default-user.png" alt="" />
</#if>
	<span class="user-info">
		<small>${welcome!"你好"},</small>
	${user.realName!}
	</span>
	<i class="ace-icon fa fa-caret-down"></i>
</a>

<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
	<li>
		<a href="${request.contextPath}/homepage/logout/page">
			<i class="ace-icon fa fa-power-off"></i>
			退出
		</a>
	</li>
</ul>
						