<div class="page-sidebar-body-title">组织架构</div>
<ul class="chosen-tree chosen-tree-tier1" id="leftUnitTree">
</ul>
<script>
$(function(){
	var ii=1;
	var leftUnitTree=$("#leftUnitTree");
	<#list 0..maxIndex as key>
		<#assign itemList=unitMakeMap[key+'']>
		<#if itemList?exists && itemList?size gt 0>
			<#list itemList as item1>
			    var hh='';
			    var isReport=false;
			    var isNext=false;
			    <#if item1.report>
					isReport=true;	
			    </#if>
				<#if item1.childUnitList?exists && item1.childUnitList?size gt 0>
					isNext=true;
				</#if>
				hh=makeUl('${item1.unit.id!}','${item1.unit.unitName!}',ii,isReport,isNext);
				<#if key==0>
					$(leftUnitTree).append(hh);
				<#else>
					var treeItem=$('.chosen-tree-item[data-id="${item1.unit.parentId!}"]').parent('li');
					if($(treeItem).find(".chosen-tree").length==0){
						$(treeItem).append('<ul class="chosen-tree chosen-tree-tier${key+1}">'+hh+'</ul>');
					}else{
						$(treeItem).find("ul:first").append(hh);
					}
				</#if>
				ii++;
			</#list>
		</#if>
	</#list>
	layer.closeAll();
	initClick();
})
function makeUl(unitId,unitName,index,isReport,isNext){
	var ss='<li class="';
	if(isNext){
		ss=ss+' sub-tree ';
	}
	if(isReport){
		ss=ss+' report';
	}
	ss=ss+'">'
		+'<div class="chosen-tree-item" data-index="'+index+'" data-id="'+unitId+'">'
		+'<span class="arrow"></span>'
		+'<span class="name">'+unitName+'</span>'
		+'</div></li>';
	return ss;
}
function initClick(){
	var $scroll=$('.page-sidebar-body'),
	$tree=$scroll.children('.chosen-tree');
	var sLeft=$scroll.scrollLeft(),
		sWidth=$scroll.width(),
		tWidth=sLeft+sWidth;
	$tree.width(tWidth);
	<#if type?default('1') == '1'>
	if ($('.page-sidebar-compare').length) {
		var $body = $('.page-sidebar-body'),
		$footer = $('.page-sidebar-footer'),
		$btn = $('.chosen-compare-btn'),
		$list = $('.chosen-compare-list');
		//滑过显示对比按钮
		$('.page-sidebar-compare .chosen-tree-item').hover(function(){
			var $li = $(this).parent('li');
			if ($li.hasClass('report') || $li.hasClass('sub-tree')) {
				$(this).append('<span class="compare">+对比</span>');
		}
		if ($li.hasClass('is-lock')) {
			$(this).append('<span class="compare">已加入</span>');
		}
		var sLeft = $('.page-sidebar-body').scrollLeft(),
		sRight = $('.page-sidebar-body').children('.chosen-tree').width() - $('.page-sidebar-body').width() + 10 - sLeft;
		$(this).children('.compare').css('right', sRight);
	},function(){
		$(this).children('.compare').remove();
	});
	//加入对比项
	$('.page-sidebar-compare').off('click','.chosen-tree-item .compare').on('click', '.chosen-tree-item .compare', function(e){
		e.stopPropagation();
		//已经加入的不能再次添加
		var len = $list.children('li').length,
		txt = $(this).siblings('.name').text(),
		index = $(this).parent('.chosen-tree-item').data('index');
		id = $(this).parent('.chosen-tree-item').data('id');
		
		if (len >= 2) {
			layer.msg('最多只能选择两个机构，请删除一个后再选择');
		} else{
			if($(this).text()=='已加入'){
				//已经加入 无需添加
				layer.msg('已经加入啦');
				return;
			}
			$(this).text('已加入').parent('.chosen-tree-item').parent('li').addClass('is-lock');
			$list.show().append('<li data-index="' + index + '" data-id="'+id+'"><a href="#" class="remove"></a>'+ txt + '</li>');
			var sLen = $list.children('li').length;
			$btn.children('span').show().text('（' + sLen + '）');
			if (sLen === 2) {
				$btn.removeAttr('disabled');
			}
			//获取高度，处理滚动条
			var footerH = $footer.height();
			$body.css('bottom', footerH);
		}
	});
	//删除对比项
	$('.chosen-compare-list').off('click','li .remove').on('click', 'li .remove', function(e){
			var $li = $(this).parent('li'),
			index = $li.data('index');
			$li.remove();
			var len = $list.children('li').length;
			$btn.children('span').text('（' + len + '）');
			$('.chosen-tree-item[data-index="' + index + '"]').parent('li').removeClass('is-lock');
			if (len === 0) {
				$list.hide();
				$btn.attr('disabled','disabled').children('span').hide();
			}else if(len<2){
				$btn.attr('disabled','disabled');
			}
			//获取高度，处理滚动条
			var footerH = $footer.height();
			$body.css('bottom', footerH);
		});
	} 
	</#if>
	
}
</script>
