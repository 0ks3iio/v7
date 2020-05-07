<ul class="post-list <#if infoType=='10'>post-list-1of2</#if>" data-action="scroll">
	<#if bulletins?exists&&bulletins?size gt 0>
		<#list bulletins as item>
		<li>
			<a href="javascript:void(0);" onclick="bulletinShowDetail('${item.id!}')">
				<h4 class="post-title">${item.title!}</h4>
				<p class="post-summary">${item.content!}<span>查看更多</span></p>
				<p class="post-time">发布时间：${item.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
			</a>
		</li>
		</#list>
	<#else>
		<div class="no-data center">
			<div class="no-data-content">
				<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt="">
				<p>暂无公告，请前往后台发布</p>
			</div>
		</div>
	</#if>
</ul>
<script>
// 通知滚动
(function($){
	var $scrollBoxs = $('[data-action=scroll]');

	if($scrollBoxs.length > 0){
		$scrollBoxs.each(function(){
			var _this = $(this);
			if(_this.outerHeight() > _this.parent().outerHeight()){
				var timer = setInterval(function(){
					if(_this.hasClass('post-list-1of2')){
						_this.children().eq(0).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
						_this.children().eq(1).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
					}else{
						_this.children().eq(0).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
					}
				},5000)
			}
		})
	}
})(jQuery)
</script>