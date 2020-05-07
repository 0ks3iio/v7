<div class="class-behaviour">
	<div class="nav-table-wrap grid">
		<div class="prev-arow grid-cell"></div>
		
		<div class="nav-table grid-cell">
			<ul class="nav-table-list clearfix">
			<#if weekList?exists && weekList?size gt 0>
			    <#list weekList as item>
				<li <#if '${item!}' == '${week!}'>class="active"</#if> onclick="search(${item!});">第${item!}周</li>
				</#list>
			</#if>
			</ul>
		</div>
		
		<div class="next-arow grid-cell"></div>
	</div>
	
	<div class="space-course" id="tableDiv1">
		
	</div>
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
	
	navWidth();
	$('.nav-table-list').find('li').eq(${week-1!}).trigger('click');
	$('body').on('click','.next-arow',function(){
		var click = $('.nav-table-list').css('transform').split(',')[4].split('.')[0] / 200;
		if(click < 0){
			click = -click;
		}
		var width = $('.nav-table-list').width() - 200*click - $('.nav-table').width();
		if (width > 0){
			if (width < 200){
				$('.nav-table-list').css({
					transform: 'translateX(-'+ (200*click + width) +'px)'
				});
			} else {
				click ++;
				$('.nav-table-list').css({
					transform: 'translateX(-'+ 200*click +'px)'
				});
			}
		} 
	});
	$('body').on('click','.prev-arow',function(){
		var click = $('.nav-table-list').css('transform').split(',')[4].split('.')[0] / 200;
		
		if(click < 0){
			click = -click;
		}
		if($('.nav-table').width() < $('.nav-table-list').width()){
			click --;
			if(click < 0){
				click = 0;
			}
			$('.nav-table-list').css({
				transform: 'translateX(-'+ 200*click +'px)'
			})
		}
	});
})

function navWidth(){
		var width = 0;
		var length = $('.nav-table-list').find('li').length - 1;
		$('.nav-table-list').find('li').each(function(){
			width += $(this).outerWidth()
		});
		width = width + length * 20 + 1;
		$('.nav-table-list').width(width);
		
		$('.nav-table-list').find('li').each(function(index,el){
			$(this).click(function(){
				$(this).addClass('active').siblings().removeClass('active');
				
				for (var i = 0,w = 0,w1 = 0;i < index;i ++) {
					w += $('.nav-table-list').find('li').eq(i).outerWidth();
				}
				w = parseInt(w + 20 * index - ($('.nav-table').width() / 2) + $(this).outerWidth() / 2);
				
				for (var j = (index + 1),w1 = 0; j < $('.nav-table-list').find('li').length; j ++) {
					w1 += $('.nav-table-list').find('li').eq(j).outerWidth();
				}
				w1 = w1 + 20 * ($('.nav-table-list').find('li').length - index) + $(this).outerWidth() / 2 - ($('.nav-table').width() / 2);
				
				if (w > 0 && w1 >0){
					$('.nav-table-list').css({
						transform: 'translateX(-'+ w +'px)'
					});
				} else if(w < 0){
					$('.nav-table-list').css({
						transform: 'translateX(-'+ 0 +'px)'
					})
				} else if(w1 < 0){
					$('.nav-table-list').css({
						transform: 'translateX(-'+ (width - $('.nav-table').width()) +'px)'
					});
				}
			})
		});
	}
	
function search(week){
   var url =  "${request.contextPath}/eccShow/classSpace/dyCheckList?classId=${classId!}&unitId=${unitId!}&acadyear=${acadyear!}&semester=${semester!}&week="+week;
   $("#tableDiv1").load(url);
}
</script>