<#if attachFolders?exists&&attachFolders?size gt 0>
<div id="ppts-group" class="tab-pane active">
	<div class="ppt-sign container-wrap" id="pptListDiv">
	</div>
	<div class="nav-bottom-wrapper">
		<div class="boundarys">
			<ul class="nav nav-tabs nav-bottom ppt-wrap">
				<#list attachFolders as attach>
					<li <#if attach_index == 0>class="active"</#if>>
						<a href="#" id="${attach.id!}" onClick="changePPTList('${attach.id!}')">${attach.title!}</a>
					</li>
				</#list>
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
	$('.nothing').height($('.scroll-container').height() - 100 );
	
	<#if attachFolders?exists&&attachFolders?size gt 0>
	$('.nav-bottom').each(function(index,ele){
		$(ele).css({
			width: $(this).children('li').length*$(this).children('li').width()
		})
	})
	var myScrollss = new IScroll('.boundarys', {  click:true,scrollX: true, scrollY: false, mouseWheel: true });
	
	changePPTList($(".nav-bottom li").first().find('a').attr('id'));
	
	</#if>
});

function changePPTList(folderId) {
	$("#"+folderId).parent().addClass('active').siblings().removeClass('active');
	var url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/pptlist?folderId="+folderId+'&view='+_view;
	$("#pptListDiv").load(url);
}
</script>