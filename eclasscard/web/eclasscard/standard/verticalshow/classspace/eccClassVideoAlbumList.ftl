<#if attachFolders?exists&&attachFolders?size gt 0>
<div id="media-video" class="tab-pane active">
	<div class="nav-bottom-list-wrap height-wrap-inner" id="videoListDiv">
	</div>
	<div class="nav-bottom-wrapper videos-wrap-dd">
		<div class="boundary">
			<ul class="nav nav-tabs nav-bottom videos-wrap">
				<#if attachFolders?exists&&attachFolders?size gt 0>
					<#list attachFolders as attach>
						<li <#if attach_index == 0>class="active"</#if> onClick="showVideoLists('${attach.id!}')">
							<a href="javascript:void(0);" id="${attach.id!}">${attach.title!}</a>
						</li>
					</#list>
				</#if>
			</ul>
		</div>
	</div>
</div>
<#else>
	<div class="nothing">
		<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
		<p>暂时没有视频哦~</p>
	</div>
</#if>
<script>
$(document).ready(function(){	
	<#if attachFolders?exists&&attachFolders?size gt 0>
	showVideoLists($(".nav-bottom li").first().find('a').attr('id'));
	
	$('.nav-bottom').each(function(index,ele){
		$(ele).css({
			width: $(this).children('li').length*$(this).children('li').width()
		})
	});
	var myScrolls = new IScroll('.boundary', { click:true, scrollX: true, scrollY: false, mouseWheel: true });
	
	$('.height-wrap-inner').each(function(){
		$(this).css({
			overflowY: 'auto',
			height: $(window).height() - $(this).offset().top - 200 
		});
	});
	<#else>
	$('.nothing').height($('.scroll-container').height() - 100 );
	</#if>
});
function showVideoLists(folderId) {
	$("#"+folderId).parent().addClass('active').siblings().removeClass('active');
	var url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/videolist?folderId="+folderId+'&view='+_view;
	$("#videoListDiv").load(url);
}
</script>