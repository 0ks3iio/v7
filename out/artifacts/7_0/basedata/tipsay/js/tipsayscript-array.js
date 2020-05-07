$(function(){
	//框架结构
	function winLoad(){
		
		var all_w=$(window).width();
		var all_h=$(window).height();
		var header_h=$('#header').height();
		var group_list_len = $('.sidebar-left .group-list').height();
		//var row_len=$('.content .syllabus-table tr:last').find('.sort').text();//总行数-除头、分隔行等
		var row_len=$('.content .syllabus-table tbody').find('.sort_1').length;
		var col_len=$('.content .syllabus-table th').length-2;//总列数-除序号列
		var con_tt_h=$('.content .syllabus-tt').outerHeight();//总课表-表头
		var con_th_h=$('.content .syllabus-table th').height();//总课表-th
		//var con_from_h=$('.content .syllabus-from').outerHeight();//总课表-未排除
		var con_from_h=0;
		var con_line_len=$('.content .syllabus-table td.line').length;
		var con_line_h=con_line_len*10;//总课表-分隔行  每行高度10
		var con_all_h=all_h-header_h-con_tt_h-con_th_h-con_from_h-con_line_h;//总课表-剩余总高度
		var con_all_w=$('.content').width()-68;//总课表-剩余总宽度 序号列宽30
		var con_item_h=parseInt(con_all_h/row_len)-3;//总课表-每个单元格高度
		if($('#container').hasClass('daike-container')){
			//con_item_h=con_item_h-10;
		};
		var con_item_w=parseInt(con_all_w/col_len);//总课表-每个单元格宽度
		
		//总课表-每个单元格赋值
		$('.content .syllabus-table td .item').each(function(){
            $(this).css({width:con_item_w,height:con_item_h});
			var room_text=$(this).find('span.room').text();
			var otherroom_text=$(this).find('span.otherRoom').text();
			var course=$(this).find('.course');
			var name=$(this).find('.name');
			var room=$(this).find('.room');
			var other_course=$(this).find('.otherCourse');
			var other_name=$(this).find('.otherName');
			var other_room=$(this).find('.otherRoom');
			var type=$(this).find('.type');
			
			var my_len=3;
			if(room_text=='' && otherroom_text==''){
				my_len=my_len-1;
			};
			var my_h=parseInt(con_item_h/my_len);
			var type_top=parseInt((my_h - 15)/2);
			if(my_h>=14){
				course.css({height:my_h,'line-height':my_h+'px'});
				name.css({height:my_h,'line-height':my_h+'px'});
				room.css({height:my_h,'line-height':my_h+'px'});
				other_course.css({height:my_h,'line-height':my_h+'px'});
				other_name.css({height:my_h,'line-height':my_h+'px'});
				other_room.css({height:my_h,'line-height':my_h+'px'});
				type.css({'top':'-'+type_top+'px'});
			};
        });
		
		var side_row_len=row_len*2;
		var side_wrap_h=$('.sidebar-right-wrap').height();//教师选择高度
		var side_tt_h=$('.sidebar-right .syllabus-tt').height()*2;//教师课表表头 全双倍
		var side_th_h=$('.sidebar-right .syllabus-table th').height()*2;//教师课表th
		var side_remark_h=$('.sidebar-right .syllabus-remark').outerHeight();//教师课表备注
		var side_line_len=$('.sidebar-right .syllabus-table td.line').length*2;
		var side_line_h=side_line_len*3;//教师课表分隔行  每行高度3
		var side_all_h=all_h-header_h-side_wrap_h-side_tt_h-side_th_h-side_remark_h-side_line_h;//教师课表剩余总高度
		var side_all_w=$('.sidebar-right').width()-60;//教师课表剩余总宽度 序号列宽22
		var side_item_h=parseInt(side_all_h/side_row_len)-3;//教师课表每个单元格高度
		if($('#container').hasClass('daike-container')){
			side_item_h=(side_item_h+4)*2;
		};
		var side_item_w=parseInt(side_all_w/col_len);//教师课表每个单元格宽度
		//教师课表-每个单元格赋值
		$('.sidebar-right .syllabus-table td .item').each(function(){
            $(this).css({width:side_item_w,height:side_item_h});
//			var room_text=$(this).children('span.room').text();
			var $span=$(this).children('span');
			var my_len=2;
//			if(room_text==''){
//				my_len=my_len-1;
//			};
			var my_h=parseInt(side_item_h/my_len);
			if(my_h>=12){
				$span.css({height:my_h,'line-height':my_h+'px'});
			};
        });
		
		//总框架
		var con_h=all_h-header_h;
		$('.sidebar-left,.sidebar-right,.content').height(con_h);
		//$('.sidebar-left ul').height(con_h-373);
		var mm=332-250;//coursetip:290 防止最后出界 预留10
		if((con_h-mm)-group_list_len<100){
			$('.sidebar-left .group-list').height(con_h-mm);
		}else{
			$('.sidebar-left ol').height(con_h-mm-group_list_len);
		}
		$('.tableContainer').height(con_h-con_tt_h);
	};
	winLoad();
	$(window).resize(function(){
		winLoad();		
	});
		
	//课表选中操作
	$('.content .syllabus-table td .item').click(function(){
		if($(this).hasClass('item-lock')){
			return;
		}
		//选中的是不是存在课程
		if($(this).find(".courseScheduleId").length>0){
			//是否已经选中
			if(!$(this).hasClass('item-sel')){
				$(this).addClass('item-sel');
				//同时右下角的数据同样的位置选中
				var user=$(this).attr('data-user');
				$("#teacherTable1").find('td .item[data-user='+user+']').addClass('item-sel');
			}else{
				$(this).removeClass('item-sel');
				var user=$(this).attr('data-user');
				$("#teacherTable1").find('td .item[data-user='+user+']').removeClass('item-sel');
				//取消  如果没有选中 右边数据教师数据关闭
				var ll=$("#allTable").find("td .item-sel").length;
				if(ll==0){
					if($('.sidebar-right-wrap').find("table").length>0 && $('.sidebar-right-wrap').find("table").find(".inner").length>0){
						$('.sidebar-right-wrap').find("table").find(".inner").hide();
					}
				}
			}
			searchByTeacherName();
		}else{
			autoTips('这个时间没有课程，不能操作');
		}
	});

	//滑过样式
	$('.content .syllabus-table td .item').mouseover(function(){
		var user=$(this).attr('data-user');
		if(user!=0){
			$('#allTable td .item[data-user='+user+'],#teacherTable1 td .item[data-user='+user+']').addClass('item-hover');
		};
	});
	
	//离开样式
	$('.content .syllabus-table td .item').mouseout(function(){
		$('.syllabus-table td .item').removeClass('item-hover');
	});

	
	
	
	//右边操作
	
	$('.sidebar-right-wrap .tab li').click(function(){
		$(this).addClass('current').siblings('li').removeClass('current');
		//查询
		var teacherId=$("#chooseTeacherId").val();
		loadRightTeachers(teacherId);
		
	});
	
	
	//左边教师点击事件
	$(".group-list .group .title").click(function(){
		if($(this).children().hasClass("fa-angle-down")){
			$(this).children().addClass("fa-angle-up").removeClass("fa-angle-down").parent().siblings().show().parent().siblings().children(".title").children().addClass("fa-angle-down").removeClass("fa-angle-up").parent().siblings().hide();
		}else{
			$(this).children().addClass("fa-angle-down").removeClass("fa-angle-up");
			$(this).children().parent().siblings().hide();
		}
	});
	
	$(".group-list .group li").click(function(e){
		e.preventDefault();
		$(".group-list .group li").removeClass("current");
		$(this).addClass("current");
		//切换教师
		var teacherId=$(this).attr("data-value");
		var teacherName=$(this).html();
		changeTeacherIdFunction(teacherId,teacherName);
	});
	
	//左边教师查询
	//查找
	$('.sidebar-filter .txt').bind('focus keyup',function(e){
		var $list=$(this).parents('.sidebar-filter').find('.list');
		//查询
		var changeTName=$(this).val().trim();
		if($list.find("a").length){
			if(changeTName!=""){
				$list.find("a").each(function(){
					var ctName=$(this).html();
					if(ctName.indexOf(changeTName)>-1){
						$(this).show();
					}else{
						$(this).hide();
					}
				})
			}else{
				$list.find("a").show();
			}
		} 
	   $list.show();
		//关闭
		$(document).click(function(event){
			var eo=$(event.target);
			if($list.is(':visible') && !eo.parents('.sidebar-filter').length){
				$list.hide();
			};
		});
		
	});
	
	//定位
	$('.sidebar-filter .list a').click(function(e){
		e.preventDefault();
		var ctName=$(this).html();
		$('.sidebar-filter .txt').val(ctName);
		var data_class=$(this).attr('data-class');
		
		var index=$('.sidebar-left li[data-class='+data_class+']').index();
		var pos_top=index*40;
		
		$('.sidebar-filter .list').hide();
		
		$('.sidebar-left ul').scrollTop(pos_top);
		
		$('.sidebar-left li').removeClass('current');
		$('.sidebar-left li[data-class='+data_class+']').addClass('current');
		//所有关闭
		$(".group-list .group .title").children().addClass("fa-angle-down").removeClass("fa-angle-up");
		
		$(".group-list .group .title").siblings().hide();
		
		$('.sidebar-left li[data-class='+data_class+']').parent().parent().find('.title').children().addClass("fa-angle-up").removeClass("fa-angle-down");
		$('.sidebar-left li[data-class='+data_class+']').parent().parent().find('.title').siblings().show();
		
		var teacherId=$('.sidebar-left li[data-class='+data_class+']').attr("data-value");
		var teacherName=$('.sidebar-left li[data-class='+data_class+']').html();
		//切换教师
		changeTeacherIdFunction(teacherId,teacherName);
	});
	
});

//切换教师课表
function changeTeacherIdFunction(teacherId,teacherName){
	//页面中
	var oldTeacherId=$("#chooseTeacherId").val();
	if(oldTeacherId!=teacherId){
		//头部显示修改
		$("#chooseTeacherId").val(teacherId);
		$("#chooseTeacherName").find("p").html("<span>"+teacherName+"</span>老师</p>");
		
		//代课时间切换成默认周次
		$(".content .syllabus-tt .ui-radio").removeClass("ui-radio-current");
		$(".content .syllabus-tt .ui-radio").each(function(){
			if($(this).find(".radio").val()=='2'){
	    		
	    	}else{
	    		$(this).addClass("ui-radio-current");
	    		var index = $(".content .syllabus-tt .ui-radio").index(this);
		    	$(".content .js-time").hide()
		    	$(".content .js-time").eq(index).show();
	    	}
			$(".func2").prop("disabled",true);
    		$(".func1").prop("disabled",false);
	    });
		//课表清空
		$("#allTable").find(".item").html("");
		$("#allTable").find(".item").removeClass("item-sel");
		//中间课表填充
		loadTeacherByWeek(teacherId,true,"allTable");
		//右边
		$("#teacherTable2").find(".item").html("");
		$("#teacherTable2").find(".item").removeClass("item-sel");
		//右上角教师切换
		$(".tab_ul").find("li").removeClass("current");
		$(".tab_ul").find(".aa").addClass("current");
		$("#rightTeacherList").html("");
		$("#inputTeacherName").val("");
		searchTab(teacherId,"1");
		//班级列表更新
		findClazzListByTeacherId(teacherId);
	}
}

//自动关闭提示层
function autoTips(txt){
	var win_width=parseInt($(window).width());
	var win_height=parseInt($(window).height());
	$('body .auto-layer-mask').remove();//先清除？？？？
	$('body').prepend('<div class="auto-layer-mask" style="height:'+win_height+'px"><div class="auto-layer">'+txt+'</div></div>');
	//$('body').prepend('<div class="auto-layer">'+txt+'</div>');
	var layer_width=$('.auto-layer').outerWidth();
	var layer_height=$('.auto-layer').outerHeight();
	var layer_left=parseInt((win_width-layer_width)/2);
	var layer_top=parseInt((win_height-layer_height)/2);
	$('.auto-layer').css({'top':layer_top,'left':layer_left}).stop(true).fadeIn(300).delay(1000).fadeOut(300,function(){
		$('.auto-layer-mask').fadeOut(300).remove();
	});
};

//宽度重新计算
function reFreshTableWidth(){
	var con_all_w=$('.content').width()-68;//总课表-剩余总宽度 序号列宽30
	var col_len=$('.content .syllabus-table th').length-2;//总列数-除序号列
	var con_item_w=parseInt(con_all_w/col_len);//总课表-每个单元格宽度
	//总课表-每个单元格赋值
	$('.content .syllabus-table td .item').each(function(){
        $(this).css({width:con_item_w});
    });
	var side_all_w=$('.sidebar-right').width()-60;//教师课表剩余总宽度 序号列宽22
	var side_item_w=parseInt(side_all_w/col_len);//教师课表每个单元格宽度
	//教师课表-每个单元格赋值
	$('.sidebar-right .syllabus-table td .item').each(function(){
        $(this).css({width:side_item_w});
    });
}



