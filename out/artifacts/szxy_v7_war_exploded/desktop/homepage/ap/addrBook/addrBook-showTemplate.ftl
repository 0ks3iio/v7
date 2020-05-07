<#macro showTempla2  userLists  >
<ul class="user-list user-list-hover">
	 <#if userLists ?? && userLists ?size gt 0>
       <#list userLists as user>
		<li>
			<div id ="c" data-toggle="show-user-detail">
			<img src="${user.avatarUrl!}" alt="" class="user-avatar user-avatar-template">	
				<h5 class="user-name user-name-big">${user.userName!}</h5>
			<#--<div class="user-content">${user.content!}</div> -->
			    <div class="user-action"><a  class="i fa fa-envelope"></a></div> 
			</div>
			<div class="user-detail">
				<ul class="user-metadata">
				    <li>
					<i class="fa fa-envelope"></i>
					<span class="user-metadata-name">单位</span>
					<span class="user-metadata-content">${user.unitName!}</span>
					</li>
					<li>
						<i class="fa fa-envelope"></i>
						<span class="user-metadata-name">部门</span>
						<span class="user-metadata-content">${user.deptName!}</span>
					</li>
					<li>
						<i class="fa fa-briefcase"></i>
						<span class="user-metadata-name">职务</span>
						<span class="user-metadata-content">${user.duty!}</span>
					</li>
					<li>
						<i class="fa fa-phone"></i>
						<span class="user-metadata-name">电话</span>
						<span class="user-metadata-content">${user.linkPhone!}</span>
					</li>
					<li>
					<i class="fa fa-mobile fa-lg"></i>
					<span class="user-metadata-name">手机</span>
					<span class="user-metadata-content">${user.personTel!}</span>
					</li>
					<li>
						<i class="fa fa-envelope"></i>
						<span class="user-metadata-name">邮箱</span>
						<span class="user-metadata-content">${user.email!}</span>
					</li>
				</ul>
			</div>
		</li>
       </#list>
    </#if>	
</ul>

</#macro>
