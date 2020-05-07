<div class="box-header">
	<h4 class="box-title">通知公告</h4>
</div>
<div class="box-body">
	<div class="post-container <#if infoType=='10'>post-container03<#elseif infoType=='20'>post-container02<#elseif infoType=='30'>post-container04<#else>post-container01</#if> ">
		<ul class="post-list" data-action="scroll">
			<#if bulletins?exists&&bulletins?size gt 0>
  				<#list bulletins as item>
				<li class="post-item">
					<a href="javascript:void(0);" class="js-openPost" onclick="bulletinShowDetail('${item.id!}')">
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
<script>
// 通知滚动
(function($){
	var $scrollBoxs = $('[data-action=scroll]');

	if($scrollBoxs.length > 0){
		$scrollBoxs.each(function(){
			var _this = $(this);
			if(_this.outerHeight() > _this.parent().outerHeight()){
				var timer = setInterval(function(){
					_this.children().eq(0).slideUp(300, function(){
						$(this).appendTo(_this).slideDown(300);
					})
				},60000)
			}
		})
	}
})(jQuery)
</script>