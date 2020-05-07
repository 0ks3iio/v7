<div class="box-header">
	<h4 class="box-title">通知公告</h4>
</div>
<div class="box-body">
	<div class="post-container post-container02">
		<ul class="post-list" data-action="scroll">
			<#if bulletins?exists&&bulletins?size gt 0>
  				<#list bulletins as item>
				<li class="post-item">
					<a href="javascript:void(0);"class="js-openPost" onclick="bulletinShowDetail('${item.id!}')">
						<h3 class="post-title">${item.title!}</h3>
						<span class="post-time">发布时间：${item.createTime?string('yyyy-MM-dd HH:mm:ss')}</span>
						<div class="post-content">
							${item.content!}
						</div>
					</a>
				</li>
				</#list>
			<#else>
				<div class="no-data">
					<div class="no-data-content">
						<img src="${request.contextPath}/static/eclasscard/show/images/icon-post.png" alt="">
						<p>暂无通知公告</p>
					</div>
				</div>
			</#if>
		</ul>
	</div>
</div>