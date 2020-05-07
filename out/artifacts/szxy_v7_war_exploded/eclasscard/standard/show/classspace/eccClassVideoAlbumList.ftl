<#if attachFolders?exists&&attachFolders?size gt 0>
<div id="videos-group" class="tab-pane active">
	<div class="nav-bottom-list-wrap container-wrap" id="videoListDiv">
	</div>
	<div class="nav-bottom-wrapper videos-wrap-dd">
		<div class="boundary">
			<ul class="nav nav-tabs nav-bottom videos-wrap">
				<#if attachFolders?exists&&attachFolders?size gt 0>
					<#list attachFolders as attach>
						<li <#if attach_index == 0>class="active"</#if>>
							<a href="#" id="${attach.id!}" onClick="showVideoLists('${attach.id!}')">${attach.title!}</a>
						</li>
					</#list>
				</#if>
			</ul>
		</div>
	</div>
</div>
<#else>
	<div class="nothing">
		<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
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
	})
	
	var myScrolls = new IScroll('.boundary', { click:true, scrollX: true, scrollY: false, mouseWheel: true });
	
	</#if>
	$('.nothing').height($('.scroll-container').height() - 100 );
			
});

function showVideoLists(folderId) {
	$("#"+folderId).parent().addClass('active').siblings().removeClass('active');
	var url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/videolist?folderId="+folderId+'&view='+_view;
	$("#videoListDiv").load(url);
}

</script>