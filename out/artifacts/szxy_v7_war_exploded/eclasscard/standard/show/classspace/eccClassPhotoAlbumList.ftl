<#if attachFolders?exists&&attachFolders?size gt 0>
	<div class="tab-pane active album-wrapper">
		<div class="album-list-wrapper"></div>
		<div class="album-nav-wrapper">
			<ul class="album-nav"></ul>
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
	
	var data = 
			<#if attachFolders?exists&&attachFolders?size gt 0>
			[
			<#list attachFolders as attach>
			{
				id: '${attach.id!}',
				name: '${attach.title!}',
				num: ${attach.number?default(0)},
				photos: [
					<#if attach.attachments?exists&&attach.attachments?size gt 0>
					<#list attach.attachments as photot>
					{
						title: '${photot.filename!}',
						src: '${request.contextPath}${photot.showPicUrl!}'
					}<#if photot_index+1 != attach.attachments?size>,</#if>
					</#list>
					</#if>
				]
			}<#if attach_index+1 != attachFolders?size>,</#if>
			</#list>
			];
			<#else>
			null;
			</#if>
		
		(function(){
				var nav = $('.album-nav');
				var container = $('.album-list-wrapper');
				var STEP = 16;	/*每次加载16张图片*/
				var counter = []; /*各个相册已加载图片数量*/
				var current = {}; /*当前显示相册对象*/
					
				// 加载图片
				function loadingPhotos(option){
					var photos = document.createDocumentFragment();
					for(var i=option.start; i<option.end; i++){
		                var photo_item = '<li>\
											<a href="' + option.photos[i].src + '">\
												<div class="img-wrap img-wrap-16by9" style="background:no-repeat url(' + option.photos[i].src + ') center center / cover;">\
												</div>\
												<h4>' + option.photos[i].title + '</h4>\
											</a>\
										</li>';
						$(photos).append(photo_item);
					}
					$('#'+option.id).find('ul').append(photos);
					browseAlbum(option.id);
				}

				// 设置当前相册滚动高度
				function setHeight(album){
					$('#' + album).css({
						height: $(window).height() - $('#' + album).offset().top - 220
					});
				}

				// 相册导航横向滚动
				function scrollAlbumNav(){
					var nav_width = 0;
					nav.find('li').each(function(){
						nav_width = nav_width + parseFloat($(this).width());
					});
					nav.css({
						width: nav_width + 'px'
					});

					var myScroll = new IScroll('.album-nav-wrapper', { click:true, scrollX: true, scrollY: false, mouseWheel: true });
				}

				// 初始化加载
				(function initLoading(){
					
					if (data == null) {
						return;
					}
					
					var navs = document.createDocumentFragment();
					var albums = document.createDocumentFragment();
					
					for(var i=0; i<data.length; i++){
						var nav_item = '<li class="' + (i === 0 ? 'active' : '') + '">\
											<a href="#' + data[i].id + '">' + data[i].name + '<span>（' + data[i].num + '）</span></a>\
										</li>';
						var album = '<div id="' + data[i].id + '" class="class-photos ' + (i === 0 ? 'active' : '') + '">\
										<ul id="gallery" class="photo-list"></ul>\
									</div>';
									
						$(navs).append(nav_item);
						$(albums).append(album);
					}

					container.append(albums);
					nav.append(navs);
					scrollAlbumNav();
					setHeight(data[0].id);

					current.id = data[0].id;
					current.photos = data[0].photos;
					current.start = 0;
					current.end = STEP;

					if(current.end > current.photos.length){
						current.end = current.photos.length;
					}
					loadingPhotos(current);

					// 获取每个相册初始加载图片的数量，非当前相册全部设置为0
					counter[0] = current.end;
					counter.length = data.length;
					counter.fill(0, 1);

					browseAlbum(current.id);
					scrollLoading(current.id, 0);
				})();

				// 获取当前相册的序号
				function getCurrentAlbumIndex(id){
					var index;
					for(var i=0; i<data.length; i++){
						if(data[i].id === id){
							index = i;
						}
					}
					return index;
				}

				// 滚动加载
				function scrollLoading(album, index){
					var container = $('#' + album);
					container.off('scroll');
					container.on('scroll', function(){
						if(container.scrollTop() > container.height() - container.find('ul').height()){
							if(counter[index] === current.photos.length) return;
							current.start = current.end;
							current.end = current.end + STEP;
							if(current.end > current.photos.length){
								current.end = current.photos.length;
							}
							counter[index] = current.end;
							loadingPhotos(current);
						}
					});
				}

				// 浏览相册
				function browseAlbum(album){
					var photos = $('#'+album).find('#gallery li');
					photos.on('click', function(event) {
					    event = event || window.event;
					    
					    var link = photos.index(this),
					        options = {
					        	index: link,
					        	event: event
					        },
					        links = $(this).parent().find('a');
					    blueimp.Gallery(links, options);
					});
				}

				// 相册导航
				nav.find('a').on('click', function(e){
					e.preventDefault();
					var id = $(this).attr('href').split('#')[1];
					var index;
					
					$(this).parent().addClass('active').siblings().removeClass('active');
					$('#'+id).addClass('active').siblings().removeClass('active');

					setHeight(id);
					index = getCurrentAlbumIndex(id);

					// 每次切换滚动到顶部
					$('#'+id).scrollTop(0);

					// 获取当前相册的信息
					current.id = data[index].id;
					current.photos = data[index].photos;
					current.start = counter[index];
					current.end = counter[index] + STEP;

					// 判断是否加载到最后一张
					if(current.end > current.photos.length){
						current.end = current.photos.length;
					}
					counter[index] = current.end;

					// 如果当前相册没有图片时，切换到当前相册时就开始加载图片
					if(current.start === 0){
						loadingPhotos(current);
					}

					browseAlbum(current.id);
					scrollLoading(current.id, index);
				});

			})();
});	
</script>