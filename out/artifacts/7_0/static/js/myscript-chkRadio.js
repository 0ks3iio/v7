$(function(){
	//模拟下拉框
	var $zIndex=999;
	$(".ui-select-box").each(function(index){
		var $sel_w=$(this).width()+2;
		var $txt_w=$sel_w-25;
		$(this).find(".ui-option").width($sel_w);
		$(this).find(".ui-select-txt").width($txt_w);
		i=--$zIndex
		$(this).css("z-index",i);
	});
	$(".ui-select-box-disable").each(function(index){
		$(this).find('.ui-select-txt').attr("readonly","readonly");
	});
	$("body").on('click','.ui-select-box:not(".ui-select-box-disable")',function(event){
	//$(".ui-select-box:not('.ui-select-box-disable')").click(function(event){
		event.stopPropagation();
		var $index=$(".ui-select-box").index(this);
		$(this).find(".ui-option").toggle();		
		$(this).find(".ui-select-close").toggleClass("ui-select-open");
		$(".ui-select-box .ui-select-close").not(".ui-select-close:eq("+$index+")").removeClass("ui-select-open");
		$(".ui-select-box .ui-option").not(".ui-option:eq("+$index+")").hide();
	});
	$(document).click(function(event){
		var eo=$(event.target);
		if($(".ui-select-box").is(":visible") && eo.attr("class")!="ui-option" && !eo.parent(".ui-option").length)
		$('.ui-option').hide();  
		$(".ui-select-close").removeClass("ui-select-open");         
	});
	$(".ui-option a").mouseover(function(){
		$(this).parents().find("a").addClass("no");
		$(this).addClass("hover").siblings("a").removeClass("hover");
	})
	$(".ui-option a").mouseout(function(){
		$(this).parents().find("a").removeClass("no").removeClass("hover");
	})
	/*默认赋值给文本框*/
	$(".ui-select-box").each(function(index){
		var $len=$(this).children(".ui-option").find("a.selected").length;
		if($len==0){
			var $val_txt=$(this).find(".a-wrap").children("a:first").text();
			var $val_zhi=$(this).find(".a-wrap").children("a:first").attr("val");
			$(this).find(".a-wrap").children("a:first").addClass("selected");
		}else{
			var $val_txt=$(this).find(".a-wrap").find("a.selected").text();
			var $val_zhi=$(this).find(".a-wrap").find("a.selected").attr("val");
		}
		$(this).children(".ui-select-txt").val($val_txt);
		$(this).children(".ui-select-value").val($val_zhi);
	})
	/*点击后赋值给文本框*/
	$("body").on('click','.ui-option .a-wrap a',function(event){
	//$(".ui-option a").click(function(){
		var $val_txt=$(this).text();
		var $val_zhi=$(this).attr("val");
		$(this).parents('.ui-option').siblings(".ui-select-txt").val($val_txt);
		$(this).parents('.ui-option').siblings(".ui-select-value").val($val_zhi);
		$(this).addClass("selected").siblings("a").removeClass("selected");
	});
	
	//	模拟单选框
	$('body').on('click','.ui-radio:not(".ui-radio-disabled,.ui-radio-disabled-checked")',function(){
		var $radioName=$(this).attr('data-name');
		$(document).find('.ui-radio[data-name='+$radioName+']').removeClass('ui-radio-current');
		$(this).addClass('ui-radio-current');
		$(this).children('.radio').trigger("onclick");
	});
	
	//模拟复选框
	$('body').on('click','.ui-checkbox:not(".ui-checkbox-all,.ui-checkbox-no,.ui-checkbox-disabled,.ui-checkbox-disabled-checked")',function(){
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
	
})