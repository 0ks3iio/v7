<#if attachFolders?exists&&attachFolders?size gt 0>
<div id="ppts-group" class="tab-pane active">
	<div class="ppt-sign container-wrap" id="pptListDiv">
	</div>
	<div class="nav-bottom-wrapper">
		<div class="boundarys">
			<ul class="nav nav-tabs nav-bottom ppt-wrap">
				<#if attachFolders?exists&&attachFolders?size gt 0>
					<#list attachFolders as attach>
						<li <#if attach_index == 0>class="active"</#if>>
							<a href="javascript:void(0);" id="${attach.id!}" onClick="showPPTLists('${attach.id!}')">${attach.title!}</a>
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
		<p>暂时没有PPT哦~</p>
	</div>
</#if>
<script>
$(document).ready(function(){
	<#if attachFolders?exists&&attachFolders?size gt 0>
	$('.container-wrap').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 220
		});
	});
	
	$('.nav-bottom').each(function(index,ele){
		$(ele).css({
			width: $(this).children('li').length*$(this).children('li').width()
		})
	})
	var myScrollss = new IScroll('.boundarys', { click:true,scrollX: true, scrollY: false, mouseWheel: true });
	
	showPPTLists($(".nav-bottom li").first().find('a').attr('id'));
	
	<#else>
	$('.nothing').height($('.scroll-container').height() - 100 );
	</#if>
});

function showPPTLists(folderId) {
	$("#"+folderId).parent().addClass('active').siblings().removeClass('active');
	var url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/pptlist?folderId="+folderId+'&view='+_view;
	$("#pptListDiv").load(url);
}
</script>