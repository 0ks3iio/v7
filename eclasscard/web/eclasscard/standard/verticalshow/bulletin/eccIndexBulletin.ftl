		<#if bulletins?exists&&bulletins?size gt 0>
			<ul class="post-list" data-action="scroll">
				<#list bulletins as item>
					<li>
						<a href="javascript:void(0);" onclick="bulletinShowDetail('${item.id!}')">
							<#if item.pictureUrl?exists>
							<img width="140" height="112" src="${item.pictureUrl!}" alt="" class="post-img">
							</#if>
							<h4 class="post-title">
							<#if item.bulletinLevel==1>
							<span class="label label-line label-line label-line-yellow">校级</span>
							<#else>
							<span class="label label-line label-line-red">班级</span>
							</#if>
							${item.title!}</h4>
							<p class="post-summary">${item.content!}</p>
							<p class="post-time">发布时间：${item.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
						</a>
					</li>
				</#list>
			</ul>
		<#else>
			<div class="no-data center">
				<div class="no-data-content">
					<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-post.png" alt="">
					<p>暂无公告，请前往后台发布</p>
				</div>
			</div>
		</#if>

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