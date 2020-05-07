<div id="photos" class="tab-pane active">
	<input type="hidden" id="pageIndex" value="${pageIndex!}">
	<input type="hidden" id="pageCount" value="${pageCount!}">
	<div class="class-photos">
		<ul id="gallery" class="photo-list">
		</ul>
		<div class="no-data-content" id="nullDiv" style="display:none;">
				<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing-big.png" alt="">
		</div>
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
		addPage();
		
		$('#gallery').on('click','li',function(event){
			event = event || window.event;
			    
			var link = $('#gallery li').index(this),
			    options = {
			       	index: link, 
			        event: event
			    },
			    links = $(this).parent().find('a');
			blueimp.Gallery(links, options);
		});
		
		var container = $('.class-photos');
		
		container.css({
			height: $(window).height() - container.offset().top - 220
		})

		container.on('scroll', function(){
			var scrollTop = $(this).scrollTop();
			var scrollHeight = $(this)[0].scrollHeight;
			var windowHeight = $(this).height();
			if (scrollTop + windowHeight == scrollHeight) {
				addPage();
			}
		}) 
	})
	
	function addPage() {
		var pageIndex = $("#pageIndex").val();
		var pageCount = $("#pageCount").val();
		if (pageIndex != "") {
			pageIndex = parseInt(pageIndex);
			pageCount = parseInt(pageCount);
			pageIndex++;
			if(pageIndex>pageCount){
				return;
			}
		}
		var contextPath = "${request.contextPath}";
		var options = {
			url : "${request.contextPath}/eccShow/eclasscard/classSpace/album/pageData",
			data:{'name':'${deviceNumber!}','pageIndex':pageIndex},
			success : function(data){
				var jsonO = JSON.parse(data);
				var list = jsonO[0];
				if (list.length > 0) {
					for (var i=0;i<list.length;i++) {
						var htmlOption = "<li><a href='"+contextPath+"/eccShow/eclasscard/showpicture?id="
						+list[i].id+"'><div class='img-wrap img-wrap-16by9' style='background:no-repeat url("+contextPath
						+"/eccShow/eclasscard/showpicture?id="+list[i].id+") center center / cover;'></div><h4>"+list[i].pictrueName
						+"</h4></a></li>";
						$("#gallery").append(htmlOption);
					}
				} else {
					$("#nullDiv").removeAttr("style");
					$("#gallery").attr("style","display:block");
				}
				$("#pageIndex").val(jsonO[1]);
				$("#pageCount").val(jsonO[2]);
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$.ajax(options);
	}
</script>