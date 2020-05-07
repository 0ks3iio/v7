//屏蔽不可用状态事件
$('.disabled a, a.disabled').click(function(e){
	e.preventDefault();
});

//屏蔽a链接选中虚线
$('a').attr('hidefocus', 'true');

//下拉菜单
$('.dropdown-toggle').not(':disabled').click(function(e){
	e.preventDefault();
	$('.dropdown-toggle').each(function(){
		var $g = $(this).parent('.btn-group'); 
		if ($g.hasClass('open')) {
			$g.removeClass('open');
		}
	});
	$(this).parent('.btn-group').toggleClass('open');
});
$('.dropdown-menu li:not(.disabled)').click(function(e){
	//e.preventDefault();
	var $btnGroup = $(this).parents('.btn-group');
	$(this).addClass('active').siblings('li').removeClass('active');
	$btnGroup.removeClass('open');
	if ($btnGroup.hasClass('filter')) {
		e.preventDefault();
		var txt = $(this).text();
		$btnGroup.children('.dropdown-toggle').text(txt);
	}
});
$(document).click(function(event){
	var eo=$(event.target);
	if($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-toggle' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
	$('.btn-group').removeClass('open');
});

//表格折叠
$('.tables-nest .toggle-next').click(function(){
	var $next = $(this).parents('.has-next').next('.has-inner');
	$(this).toggleClass('open').parents('.has-next').next('.has-inner').toggleClass('open-inner');
});
//表格全选
$('.tables .choice-all input').click(function(){
	var chk = $(this).prop('checked'),
		$choice = $(this).parents('.tables').children('tbody, tr').find('> tr > td > .choice > input, > td > .choice > input');
	if (chk === true) {
		$choice.prop('checked', true);
	} else{
		$choice.prop('checked', false);
	}
});

//文件上传相关
$('.webuploader-list').on('click', '.file-edit,.file-remove,.file-save', function(e){
	e.preventDefault();
	var $li = $(this).parents('.webuploader-list-item'),
		$name = $li.children('.file-name'),
		$loading = $li.children('.file-loading');
	if ($(this).hasClass('file-edit')) {
		var val = $name.text(),
			str = '<p class="file-text"><a href="#" class="file-save" title="保存"></a><input type="text" value="' + val + '" class="form-control" /></p>';
		$li.append(str);
		$name.hide();
		$loading.hide();
	} else if($(this).hasClass('file-remove')) {
		$li.remove();
	} else{
		val = $(this).siblings('.form-control').val();
		$name.show().text(val);
		$loading.show().siblings('.file-text').remove();
	}
});

//下拉多选输入框
if($('.chosen-select')){
	$('.chosen-select').chosen({
		allow_single_deselect:true,
		disable_search_threshold: 10
	}); 
	//resize the chosen on window resize

	$(window)
	.off('resize.chosen')
	.on('resize.chosen', function() {
		$('.chosen-select').each(function() {
			$(this).next().css({'width': $(this).width()});
		})
	}).trigger('resize.chosen');
}


