/*
2015-12-09
基于jquery的表单美化插件：radio、checkbox、select、回车切换及提交、获取焦点效果
不用对表单做任何修改，启用此插件自动美化，禁用此插件恢复默认效果
*/

//----------=S 模拟下拉框 Start ----------//
//初始化，将默认下拉框控件转换成模拟下拉框
var zIndex=99;
$('select').each(function(){
	var sStyle=$(this).attr('style');
	if(sStyle == undefined){sStyle=''};
	var sDis=$(this).attr('disabled');
	if(sDis=='disabled'){
		sClass='ui-select ui-select-disable';
		readonly='readonly="readonly"';
	}else{
		sClass='ui-select';
		readonly='';
	};
	var $opt=$(this).children('option');
	var olen=$opt.length-1;
	var oStr='';
	for(var i=0;i<=olen;i++){oStr=oStr+'<a href="#" data-val="'+$opt.eq(i).val()+'">'+$opt.eq(i).text()+'</a>';}
	var myStr='<span class="ui-select-inner"><input type="text" class="ui-select-txt" '+readonly+' value="'+$opt.eq(0).text()+'" data-val="'+$opt.eq(0).text()+'" data-txt="'+$opt.eq(0).val()+'" /><i></i></span><span class="ui-option-wrap"><span class="ui-option">'+oStr+'</span></span>';
	$(this).removeAttr('name').hide().wrap('<span class="'+sClass+'" style="'+sStyle+'"></span>');
	$(myStr).appendTo($(this).parent('.ui-select').css("z-index",zIndex));
	zIndex--;
});
//点击打开效果
$('.ui-select:not(".ui-select-disable")').click(function(event){   
	event.stopPropagation();
	var index=$('.ui-select').index(this);
	$('.ui-select:not(:eq('+index+'))').removeClass('ui-select-open').children('.ui-option-wrap').hide();
	$(this).toggleClass('ui-select-open').children('.ui-option-wrap').toggle();
});
//点击空白部分关闭效果
$(document).click(function(event){
	var eo=$(event.target);
	if($('.ui-select .ui-option-wrap').is(':visible') && eo.attr('class')!='ui-option-wrap' && !eo.parents('.ui-option-wrap').length)
	$('.ui-option-wrap').hide();  
	$('.ui-select').removeClass('ui-select-open');         
});
//鼠标滑过效果
$('.ui-option a').hover(function(){
	$(this).addClass('hover no').siblings('a').addClass('no').removeClass('hover');
},function(){
	$(this).removeClass('hover no').siblings('a').removeClass('hover no');
})
/*点击后赋值给文本框*/
$('.ui-option a').click(function(e){
	e.preventDefault();
	var txt=$(this).text();
	var val=$(this).attr('data-val');
	$(this).parents('.ui-select').find('select option:eq('+$(this).index()+')').attr('selected','true').siblings('option').removeAttr('selected');//赋值原始select，最终值从此次获取
	$(this).parents('.ui-select').find('.ui-select-txt').val(txt).attr({'data-val':val,'data-txt':txt});//赋值显示效果
	$(this).addClass('current').siblings('a').removeClass('current');
});
//----------=E 模拟下拉框 End ----------//

//----------=S 模拟单选框 Start ----------//
//将默认单选框控件转换成模拟单选框
$('input:radio').each(function(){
	var rName=$(this).attr('name');
	var rVal=$(this).attr('value');
	var rDis=$(this).attr('disabled');
	var rChk=$(this).attr('checked');
	var rClass=$(this).attr('class');
	var rStyle=$(this).attr('style');
	if(rClass==undefined){rClass='';};
	if(rStyle==undefined){rStyle='';};
	if(rDis==undefined && rChk==undefined){
		rClass='ui-radio '+rClass;
	}else if(rDis=='disabled' && rChk==undefined){
		rClass='ui-radio ui-radio-disabled '+rClass;
	}else if(rDis==undefined && rChk=='checked'){
		rClass='ui-radio ui-radio-current '+rClass;
	}else if(rDis=='disabled' && rChk=='checked'){
		rClass='ui-radio ui-radio-disabled-checked '+rClass;
	};
	$(this).addClass('radio').wrap('<span class="'+rClass+'" style="'+rStyle+'" val="'+rVal+'" data-name="'+rName+'"></span>');
});
//	模拟单选框
$('.ui-radio:not(".ui-radio-disabled,.ui-radio-disabled-checked")').click(function(){
	var $radioName=$(this).attr('data-name');
	$(document).find('.ui-radio[data-name='+$radioName+']').removeClass('ui-radio-current');
	$(this).addClass('ui-radio-current');
	$(this).children('.radio').attr('checked','checked');
});
//----------=E 模拟单选框 End ----------//

//----------=S 模拟复选框 Start ----------//
//将默认复选框控件转换成模拟复选框
$('input:checkbox').each(function(){
	var cDis=$(this).attr('disabled');
	var cChk=$(this).attr('checked');
	var cClass=$(this).attr('class');
	var cStyle=$(this).attr('style');
	if(cClass==undefined){cClass='';};
	if(cStyle==undefined){cStyle='';};
	if(cDis==undefined && cChk==undefined){
		cClass='ui-checkbox '+cClass;
	}else if(cDis=='disabled' && cChk==undefined){
		cClass='ui-checkbox ui-checkbox-disabled '+cClass;
	}else if(cDis==undefined && cChk=='checked'){
		cClass='ui-checkbox ui-checkbox-current '+cClass;
	}else if(cDis=='disabled' && cChk=='checked'){
		cClass='ui-checkbox ui-checkbox-disabled-checked '+cClass;
	};
	$(this).addClass('chk').wrap('<span class="'+cClass+'" style="'+cStyle+'"></span>');
});
//模拟复选框
$('.ui-checkbox:not(".ui-checkbox-all,.ui-checkbox-disabled,.ui-checkbox-disabled-checked")').click(function(){
	var chkLen=$(this).parents('form').find('.ui-checkbox').not('.ui-checkbox-all').length;
	if(!$(this).hasClass('ui-checkbox-current')){
		$(this).addClass('ui-checkbox-current').find('.chk').attr('checked','checked');
		var chkedLen=$(this).parents('form').find('.ui-checkbox-current').not('.ui-checkbox-all').length;
		if(chkLen==chkedLen){
			$(this).parents('form').find('.ui-checkbox-all').addClass('ui-checkbox-current').find('.chk').attr('checked','checked');
			$(this).parents('form').find('.ui-checkbox-all').attr('data-all','yes');
		};
	}else{
		$(this).removeClass('ui-checkbox-current').find('.chk').removeAttr('checked');
		$(this).parents('form').find('.ui-checkbox-all').removeClass('ui-checkbox-current').find('.chk').removeAttr('checked');
		$(this).parents('form').find('.ui-checkbox-all').attr('data-all','no');
	};
});
$('.ui-checkbox-all').click(function(){
	var chkAll=$(this).attr('data-all');
	if(chkAll=="no"){
		$(this).attr('data-all','yes').parents('form').find('.ui-checkbox').addClass('ui-checkbox-current').find('.chk').attr('checked','checked');
	}else{
		$(this).attr('data-all','no').parents('form').find('.ui-checkbox').removeClass('ui-checkbox-current').find('.chk').removeAttr('checked');
	};
});
//----------=E 模拟复选框 End ----------//

//表单：回车 - Tab - 提交
$('form').on('keypress','input:text,input:password',function(e){
	if(e.which == 13){					// 判断所按是否回车键  
		var $form=$(this).parents('form');
		var $inputs=$form.find('input:text,input:password')
		var len=$inputs.length;			// 获取表单中的所有输入框
		var idx=$inputs.index(this);	// 获取当前焦点输入框所处的位置
		if (idx==len-1){				// 判断是否是最后一个输入框  
			$form.submit();				// 提交表单 
		}else{  
			$inputs[idx + 1].focus();	// 设置焦点  
			$inputs[idx + 1].select();	// 选中文字  
		}  
		return false;					// 取消默认的提交行为 
	};
});