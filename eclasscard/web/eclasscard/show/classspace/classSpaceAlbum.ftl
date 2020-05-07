<div id="photos" class="tab-pane active">
	<div class="class-photos">
	<#if albums?exists&&albums?size gt 0>
		<ul id="gallery" class="photo-list">
  		<#list albums as item>
			<li>
				<a href="${request.contextPath}/eccShow/eclasscard/showpicture?id=${item.id!}">
					<div class="img-wrap img-wrap-big" style="background:no-repeat
						url(${request.contextPath}/eccShow/eclasscard/showpicture?id=${item.id!}) center center / cover;">
					</div>
					<h4>${item.pictrueName!}</h4>
				</a>
			</li>
			</#list>
		</ul>
		<div class="paging">
			<a onclick="changePage(1)" class="paging-btn">&lt;</a>
			<span class="paging-number"><b id="pageIndex">${pageIndex!}</b>/<span id="pageCount">${pageCount!}</span></span>
			<a onclick="changePage(2)" class="paging-btn">&gt;</a>
		</div>
	<#else>
		<div class="no-data">
			<div class="no-data-content">
				<img src="${request.contextPath}/static/eclasscard/show/images/img-default-circle.png" alt="">
			</div>
		</div>
	</#if>
	</div>
</div>
<div id="blueimp-gallery" class="blueimp-gallery">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>
<script>
$(document).ready(function(){
		$('#gallery li').on('click',function (event) {
		    event = event || window.event;
		    
		    var link = $('#gallery li').index(this),
		        options = {
		        	index: link, 
		        	event: event
		        },
		        links = $(this).parent().find('a');
		    blueimp.Gallery(links, options);
		    
		    
		});
})
function changePage(type){
	var pageIndex = $("#pageIndex").text();
	var pageCount = $("#pageCount").text();
	if(type==1){
		pageIndex--;
		if(pageIndex<1){
			return;
		}
	}else{
		pageIndex++;
		if(pageIndex>pageCount){
			return;
		}
	}
	showRightContent(2,pageIndex);
}
</script>