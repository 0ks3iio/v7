<#if bulletins?exists&&bulletins?size gt 0>
<div class="notice">
	<span class="icon icon-voice"></span>
	<div class="notice-list-wrapper">
		<ul class="notice-list wordsLoop">
			<#list bulletins as item>
			<li>${item_index+1}、${item.content!}</li>
			</#list>
		</ul>
	</div>
</div>
</#if>
<script>
(function(){
	var container = $('.notice-list-wrapper');
	var list = $('.notice-list');
	var w = 0;

	list.find('li').each(function(){
		w = w + $(this).width();
	});

	list.width(w);

	setInterval(function(){
		var cur_left = parseFloat(list.css('margin-left'));
			
		list.animate({
			marginLeft: '-=60'
		}, 1000, 'linear', function(){
			if(cur_left < container.width() - w){
				list.css({
					'margin-left': 0
				});
			}
		});
	}, 1000);
})();
</script>