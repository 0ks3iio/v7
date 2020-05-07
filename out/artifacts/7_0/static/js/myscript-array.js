$(function(){
	//框架结构
	function winLoad(){
		var all_w=$(window).width();
		var all_h=$(window).height();
		var header_h=$('#header').height();
		var group_list_len = $('.sidebar-left .group-list').height();
		var row_len=$('.content .syllabus-table tr:last').find('.sort').text();//总行数-除头、分隔行等
		var col_len=$('.content .syllabus-table th').length-2;//总列数-除序号列
		var con_tt_h=$('.content .syllabus-tt').outerHeight();//总课表-表头
		var con_th_h=$('.content .syllabus-table th').height();//总课表-th
		var con_from_h=$('.content .syllabus-from').outerHeight();//总课表-未排除
		var con_line_len=$('.content .syllabus-table td.line').length;
		var con_line_h=con_line_len*10;//总课表-分隔行  每行高度10
		var con_all_h=all_h-header_h-con_tt_h-con_th_h-con_from_h-con_line_h;//总课表-剩余总高度
		var con_all_w=$('.content').width()-68;//总课表-剩余总宽度 序号列宽30
		var con_item_h=parseInt(con_all_h/row_len)-3;//总课表-每个单元格高度
		if($('#container').hasClass('daike-container')){
			con_item_h=con_item_h-10;
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
		var side_all_w=Math.ceil(document.documentElement.clientWidth * 0.28) * 0.9;//教师课表剩余总宽度 序号列宽22
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
		$('.sidebar-left ul').height(con_h-373);
		if((con_h-332)-group_list_len<100){
			$('.sidebar-left .group-list').height(con_h-332);
		}else{
			$('.sidebar-left ol').height(con_h-332-group_list_len);
		}
		$('.tableContainer').height(con_h-con_tt_h);
	};
	winLoad();
	$(window).resize(function(){
		winLoad();		
	});
		
	//总课表操作
	$('.content .syllabus-table td .item').on('click','.replace a',function(e){
		e.stopPropagation();
		var siblings = $(this).parent(".replace").siblings();
		var prev = siblings.children(".s-d-week:first-child");
		var next = siblings.children(".s-d-week:last-child");
		prev.find(".type").text("双");
		next.find(".type").text("单");
		var prevhtml = prev.html();
		var nexthtml = next.html();
		siblings.empty().append(`<span class="s-d-week">${nexthtml}</span><span class="s-d-week">${prevhtml}</span>`);
	});

	//选中
	$('.content .syllabus-table td .item').click(function(){
		var data_sel=parseInt($('body').attr('data-sel'));
		var data_lock=parseInt($('body').attr('data-lock'));
		if(data_sel==0){
			$('body').attr('data-lock','1');
			var from_user=$('body > .int-from').attr('data-user');
			var to_user=$(this).attr('data-user');
			if(from_user!=to_user){
				if(!$(this).hasClass('item-lock')){
					var index_tr=$(this).parents('tr').index();
					var index_td=$(this).parent('td').index();
					var $table=$(this).parents('.syllabus-table');
					if(!$(this).hasClass('item-sel')){
						//处理选中状态
						$('.syllabus-table td .item').removeClass('item-sel');
						$('.syllabus-table').each(function(){
							$(this).children('tbody').children('tr:eq('+index_tr+')').children('td:eq('+index_td+')').children('.item').addClass('item-sel').removeClass('item-can');
						});
						//点击位置调换、处理可选状态
						var sel=$table.attr('data-sel');//判断有没选中元素，第一次还是第二次选中：0-未选中/第二次选中、1-第一次选中
						var user=$(this).attr('data-user');
						if(sel==0){
							$table.attr('data-sel',1);//改为选中状态
							//添加可选状态
							$('.syllabus-table td .item').not('.item-lock').each(function(){
								var my_user=$(this).attr('data-user');
								if(user!=my_user){
									$(this).addClass('item-can');
								};
							});
							//获取当前选中元素 插入body
							$(this).addClass('int-from').parent('td').addClass('int-from-wrap');
							from_top=parseInt($(this).offset().top)-1;
							from_left=parseInt($(this).offset().left);
							from_width=parseInt($(this).width());
							from_height=parseInt($(this).height());
							if(!$('body > .int-from').length){
								$('body > .int-from').remove();
							};
							$(this).clone().css({left:from_left,top:from_top,width:from_width,height:from_height}).prependTo('body');
							//alert('第一次选中');
							//鼠标滑过状态
							$('.content .syllabus-table td .item').hover(function(){
								var over_tr=$(this).parents('tr').index();
								var over_td=$(this).parent('td').index();
								$('.syllabus-table').each(function(){
									$(this).children('tbody').children('tr:eq('+over_tr+')').children('td:eq('+over_td+')').children('.item').addClass('item-over');
								});
							},function(){
								$('.syllabus-table').find('.item').removeClass('item-over');
							});
						}else{
							$table.attr('data-sel',0);//改为未选中状态
							//取消可选状态
							$('.syllabus-table td .item').not('.item-lock').each(function(){
								$(this).removeClass('item-can item-sel');
							});
							//获取当前选中元素 插入body
							$(this).addClass('int-to').parent('td').addClass('int-to-wrap');
							to_top=parseInt($(this).offset().top)-1;
							to_left=parseInt($(this).offset().left);
							to_width=parseInt($(this).width());
							to_height=parseInt($(this).height());
							if(!$('body > .int-to').length){
								$('body > .int-to').remove();
							};
							$(this).clone().css({left:to_left,top:to_top,width:to_width,height:to_height}).prependTo('body');
							$('body').attr('data-lock','0');
							//alert('第二次选中/取消选中');
							
							//移动效果
							$('body > .int-from').stop(true,true).animate({top:to_top,left:to_left},500).siblings('.int-to').stop(true,true).animate({top:from_top,left:from_left},500,function(){
								$('body > .int-from,body > .int-to').remove();//移除插入的元素
								//数据对调
								$('.syllabus-table td .int-from').appendTo('.int-to-wrap');
								$('.syllabus-table td .int-to').appendTo('.int-from-wrap');
								$('.syllabus-table td').removeClass('int-from-wrap int-to-wrap');
								$('.syllabus-table td .item').removeClass('int-from int-to');
							});
							//鼠标滑过状态
							$('.content .syllabus-table td .item').hover(function(){
								$('.syllabus-table').find('.item').removeClass('item-over');
							});
						};
					}else{
						//取消选中、可选状态；移除位置调换添加的元素
						$('.syllabus-table td .item').removeClass('item-can item-sel int-from int-to');
						$table.attr('data-sel',0).attr('data-lock',0);
						//移除插入的元素
						$('body span.item').remove();
					};
				}else{
					autoTips('已预约课程不能移动');
				};
			}else{
				autoTips('同一门课程不能移动');
			};
		};
	});
	//取消选中、可选状态；移除位置调换添加的元素
	$('html').on('click','body > .item',function(){
		$('body').attr('data-lock','0');
		$('.syllabus-table td').removeClass('int-from-wrap int-to-wrap').children('.item').removeClass('item-can item-sel int-from int-to');
		$('.content .syllabus-table').attr('data-sel',0);
		//移除插入的元素
		$('body > .int-from,body > .int-to').remove();
	});
	//滑过
	$('.content .syllabus-table td .item').not('.item-lock').mouseover(function(){
		var user=$(this).attr('data-user');
		if(user!=0){
			$('#allTable td .item[data-user='+user+'],#teacherTable1 td .item[data-user='+user+']').addClass('item-hover');
		};
	});
	$('.content .syllabus-table td .item').not('.item-lock').mouseout(function(){
		$('.syllabus-table td .item').removeClass('item-hover');
	});
	
	
	//底部未排课程
	$('.syllabus-from').on('click','li',function(){
		var s_d_week = $(this).hasClass("s-d-week") ? true : false;
		var data_lock=parseInt($('body').attr('data-lock'));
		if(data_lock == 0){
			//从外往里排课
			if(!$(this).hasClass('over')){
				$('body').attr('data-sel',1).children('.cln-item').remove();
				$('.content .syllabus-table').attr('data-sel',0).find('td').removeClass('int-from-wrap int-to-wrap').children('.item').removeClass('int-from int-to');
				$(this).addClass('sel').siblings('li').removeClass('sel');
				total_num=parseInt($(this).find('.surplus').attr('data-total'));//当前所选老师总课程数
				sur_num=parseInt($(this).find('.surplus').children('span').text());//当前所选老师剩余未排程数
				$('body > .int-from').remove()
				$('.syllabus-table span.item').removeClass('item-can item-sel');
				cln_user=$(this).attr('data-user');
				//添加课表可操作状态
				$('#allTable span.item').not('.item-lock').each(function(){
					var my_user=$(this).attr('data-user');
					if(my_user != cln_user){
						$(this).addClass('item-can');
					};
				});
				//获取选中元素位置、值、克隆元素为后面移动做准备
				cln_top=parseInt($(this).offset().top);
				cln_left=parseInt($(this).offset().left);
				cln_course=$(this).find('.course').text();
				cln_name=$(this).find('.name').text();
				cln_room=$(this).find('.room').text();
				cln_othercourse=$(this).find('.otherCourse').text();
				cln_othername=$(this).find('.otherName').text();
				cln_otherroom=$(this).find('.otherRoom').text();
				$(this).clone().addClass('cln-item').css({top:cln_top,left:cln_left}).prependTo($('body'));
				//加入课表
				$('#allTable').off('click').on('click','span.item',function(){
					if(!$(this).hasClass('item-lock')){
						var data_sel=parseInt($('body').attr('data-sel'));
						if(data_sel==1){
							if(!$(this).hasClass('item-can')){
								autoTips('同一门课程不能移动');
							}else{
								//var cln_user=$('.cln-item').attr('data-user');
								var from_top=parseInt($('.syllabus-from').offset().top);
								var from_left=parseInt($('.syllabus-from').offset().left);
								var into_top=parseInt($(this).offset().top)-1;
								var into_left=parseInt($(this).offset().left);
								var all_h=$(this).height();
								var span_len=3;
								if(cln_room == '' && cln_otherroom == ''){span_len=2};
								var span_h=parseInt(all_h/span_len)+'px';
								var type_top=parseInt((all_h/span_len - 15)/2)+'px'; 
								var $str1=`<span>
											   <span class="s-d-week">
						                    	   <span style="height:${span_h};line-height:${span_h};">
						                    	   	   <span class="course">${cln_course}</span>
						                    	   	   <span class="type" style="top:-${type_top}">单</span>
						                    	   </span>
						                    	   <span class="name" style="height:${span_h};line-height:${span_h};">${cln_name}</span>
						                    	   <span class="room" style="height:${span_h};line-height:${span_h};">${cln_room}</span>
						                       </span>
						                       <span class="s-d-week">
						                           <span style="height:${span_h};line-height:${span_h};">
						                    	   	   <span class="otherCourse">${cln_othercourse}</span>
						                    	   	   <span class="type" style="top:-${type_top}">双</span>
						                    	   </span>
						                    	   <span class="otherName" style="height:${span_h};line-height:${span_h};">${cln_othername}</span>
						                    	   <span class="otherRoom" style="height:${span_h};line-height:${span_h};">${cln_otherroom}</span>
						                       </span>
					                       </span>
					                       <span class="replace"><a href="#" class="fa fa-refresh"></a></span>`;
								var $str2='<span class="course" style="height:'+span_h+';line-height:'+span_h+'">'+cln_course+'</span><span class="name" style="height:'+span_h+';line-height:'+span_h+'">'+cln_name+'</span><span class="room" style="height:'+span_h+';line-height:'+span_h+'">'+cln_room+'</span>';
								var $str = s_d_week ? $str1 : $str2
								
								//移出已排课程
								$(this).clone().css({top:into_top,left:into_left}).prependTo($('body'));
								var my_user=$(this).attr('data-user');
								var $li=$('.syllabus-from  li[data-user='+my_user+']');
								var $sur=$li.find('.surplus').children('span');
								var sur=parseInt($sur.text())+1;
								$sur.text(sur);//未排课程赋值
								//如果未排课程大于0，移除排完状态
								if(sur>0){
									$sur.parents('li').removeClass('over').show();
								};
								//如果未排课程没有当前课程，插入增加一个
//								if(my_user != 0 && $li.length == 0){
//									my_total=$('#allTable').find('span.item[data-user='+my_user+']').length;//当前所选老师总课程数
//									my_course=$(this).find('.course').text();
//									my_name=$(this).find('.name').text();
//									my_room=$(this).find('.room').text();
//						
//									var $my_str='<li data-user="'+my_user+'"><p class="item"><span class="surplus" data-total="'+my_total+'"><span>1</span>节</span><span class="tt"><span class="course">'+my_course+'</span><span class="name">'+my_name+'</span><span class="room">'+my_room+'</span></span></p></li>'
//									$('.syllabus-from').append($my_str);
//									$('.syllabus-from li.empty').appendTo('.syllabus-from');
//								};
	
								//移动并插入数据
								$('#allTable span.item').removeClass('item-can');
								$(this).empty().hide().attr('data-user',cln_user).append($str).delay(500).fadeIn(0);
								//$('body > .int-from').stop(true,true).animate({top:to_top,left:to_left},500).siblings('.int-to').stop(true,true).animate({top:from_top,left:from_left},500,function(){
								$('.cln-item').stop(true,true).animate({top:into_top,left:into_left},500).siblings('.item-can').stop(true,true).animate({top:from_top,left:from_left},500,function(){
									$('body > .cln-item,body > .item-can').remove();
									winLoad();
								});
								
								//还原效果
								$('body').attr('data-sel',0);
								$('.syllabus-from li.sel').removeClass('sel');
								//处理剩余课数
								var yet_num=parseInt($('#allTable').find('span.item[data-user='+cln_user+']').length);//当前所选老师已排程数
								sur_num=total_num-yet_num;
								$('.syllabus-from li[data-user='+cln_user+']').find('.surplus').children('span').text(sur_num);
								if(sur_num==0){
									$('.syllabus-from li[data-user='+cln_user+']').addClass('over').hide();
								};
							};
						};
					}else{
						autoTips('锁定课程不能添加课程');
					};
				});
			};
		}else{
			//从里往外排课
			var from_top=parseInt($('.int-from').css('top'));
			var from_left=parseInt($('.int-from').css('left'));
			var from_user=$('.int-from').attr('data-user');
			var from_course=$('.int-from').children('.course').text();
			var from_name=$('.int-from').children('.name').text();
			var from_room=$('.int-from').children('.room').text();
			var from_othercourse=$('.int-from').children('.otherCourse').text();
			var from_othername=$('.int-from').children('.otherName').text();
			var from_otherroom=$('.int-from').children('.otherRoom').text();
			
			var into_top=parseInt($('.syllabus-from').offset().top);
			var into_left=parseInt($('.syllabus-from').offset().left);
			var into_user=$(this).attr('data-user');
			var into_course=$(this).find('.course').text();
			var into_name=$(this).find('.name').text();
			var into_room=$(this).find('.room').text();
			var into_othercourse=$(this).find('.otherCourse').text();
			var into_othername=$(this).find('.otherName').text();
			var into_otherroom=$(this).find('.otherRoom').text();
			
			var all_len=$('.int-from').children('span').length;
			var all_h=$('.int-from').height();
			var span_len=3;
			if(into_room == '' && into_otherroom == ''){span_len=2};
			var span_h=parseInt(all_h/span_len)+'px';
			var type_top=parseInt((all_h/span_len - 15)/2)+'px';

			var $str1=`<span>
				   <span class="s-d-week">
                	   <span style="height:${span_h};line-height:${span_h};">
                	   	   <span class="course">${into_course}</span>
                	   	   <span class="type" style="top:-${type_top}">单</span>
                	   </span>
                	   <span class="name" style="height:${span_h};line-height:${span_h};">${into_name}</span>
                	   <span class="room" style="height:${span_h};line-height:${span_h};">${into_room}</span>
                   </span>
                   <span class="s-d-week">
                       <span style="height:${span_h};line-height:${span_h};">
                	   	   <span class="otherCourse">${into_othercourse}</span>
                	   	   <span class="type" style="top:-${type_top}">双</span>
                	   </span>
                	   <span class="otherName" style="height:${span_h};line-height:${span_h};">${into_othername}</span>
                	   <span class="otherRoom" style="height:${span_h};line-height:${span_h};">${into_otherroom}</span>
                   </span>
               </span>
               <span class="replace"><a href="#" class="fa fa-refresh"></a></span>`;
			var $str2='<span class="course" style="height:'+span_h+';line-height:'+span_h+'">'+into_course+'</span><span class="name" style="height:'+span_h+';line-height:'+span_h+'">'+into_name+'</span><span class="room" style="height:'+span_h+';line-height:'+span_h+'">'+into_room+'</span>';
			var $str = s_d_week ? $str1 : $str2
			
			var $li_into=$('.syllabus-from  li[data-user='+into_user+']');
			var $sur_into=$li_into.find('.surplus').children('span');
			var sur_into=parseInt($sur_into.text());
			var $li_from=$('.syllabus-from  li[data-user='+from_user+']');
			var $sur_from=$li_from.find('.surplus').children('span');
			var sur_from=parseInt($sur_from.text());
			
			if(from_user == into_user || $(this).hasClass('over')){
				if(all_len != 0){
					$('.int-from').stop(true,true).animate({top:into_top,left:into_left},500,function(){
						$('body').attr('data-sel',0).attr('data-lock',0).children('span.item').remove();
						$('.syllabus-table td.int-from-wrap').children('.item').css({left: 0, top: 0}).attr('data-user','0').empty();
						winLoad();
					});
					//还原
					$('.syllabus-table td .item').removeClass('item-can item-sel int-from int-to');
					$('#allTable').attr('data-sel',0);
				}else{
					autoTips('课程已排完');
				};
				sur_from=sur_from+1;
				$sur_from.text(sur_from);
				if(sur_from>0){
					$sur_from.parents('li').removeClass('over').show();
				};
			}else{
				$(this).clone().addClass('sel cln-item').css({top:into_top,left:into_left}).prependTo($('body'));
				$('.int-from').stop(true,true).animate({top:into_top,left:into_left},500).siblings('.cln-item').stop(true,true).animate({top:from_top,left:from_left},500,function(){
					$('.int-from').css({left: 0, top: 0}).removeClass('int-from');
					$('body').attr('data-sel',0).attr('data-lock',0).children('span.item,.cln-item').remove();
					winLoad();
				});
				if(all_len != 0){
					$('.syllabus-table td.int-from-wrap').removeClass('int-from-wrap').children('.item').attr('data-user',into_user).empty().append($str);
				}else{
					$('.syllabus-table td.int-from-wrap').removeClass('int-from-wrap').children('.item').attr('data-user',into_user).append($str);
				};
				sur_into=sur_into-1;
				$sur_into.text(sur_into);
				if(sur_into == 0){
					$sur_into.parents('li').addClass('over').hide();
				};
				sur_from=sur_from+1;
				$sur_from.text(sur_from);
				if(sur_from > 0){
					$sur_from.parents('li').removeClass('over').show();
				};
				//还原
				$('.syllabus-table td .item').removeClass('item-can item-sel int-to');
				$('#allTable').attr('data-sel',0);
			};
		};
		
		
	});
	
	//未排课程取消选中效果
	$('body').on('click','.cln-item',function(){
		$(this).remove();
		$('body').attr('data-sel',0);
		$('#allTable span.item').removeClass('item-can');
		$('.syllabus-from li.sel').removeClass('sel');
	});


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
	
	
	$('.sidebar-left ul li').click(function(e){
		e.preventDefault();
		$(this).addClass('current').siblings('li').removeClass('current');
	});
	$(".group-list .group li").click(function(e){
		e.preventDefault();
		$(".group-list .group li").removeClass("current");
		$(this).addClass("current");
	});
	$(".group-list .group .title").click(function(){
		$(this).children().addClass("fa-angle-up").removeClass("fa-angle-down").parent().siblings().show().parent().siblings().children(".title").children().addClass("fa-angle-down").removeClass("fa-angle-up").parent().siblings().hide();
	});
	
	$('.sidebar-right-wrap .tab li').click(function(){
		$(this).addClass('current').siblings('li').removeClass('current');
		$('.tab-wrap .tab-item:eq('+$(this).index()+')').show().siblings('.tab-item').hide();
	});
	$('.tab-wrap .tab-item').each(function(){
        $(this).find('table tr:even').addClass('even');
    });
	$('.tab-item table tr').hover(function(){
		$(this).addClass('current').siblings('tr').removeClass('current');
	},function(){
		$(this).removeClass('current');
	});
	//打开代课详情
	$('.tab-item table tr .abtn-open').click(function(e){
		e.preventDefault();
		var $tr=$(this).parents('tr');
		//取消
		$('.tab-item table tr').removeClass('open');
		$('.tab-item table tr.inner').remove();
		//增加
		$tr.addClass('open');
		$tr.after(`<tr class="inner">
					<td colspan="4" class="py-5">
						<p>类型：代课</p>
						<p>时间：2016-11-10~2016-11-10</p>
						<p>节次：周三（第1、3节） </p>
						<p>班级：14港机运行4班 </p>
						<p>备注：<input type="text" class="input-txt" /></p>
						<p class="abtn">
							<a href="#" class="abtn-blue">确定</a>
							<a href="#" class="abtn-white">取消</a>
						</p>
					</td>
				</tr>`);
	});
	//关闭代课详情
	$('.tab-item table').on('click','tr .abtn-cancel,tr .abtn-send',function(e){
		e.preventDefault();
		var $tr=$(this).parents('tr').prev('tr');
		$tr.removeClass('open');
		$(this).parents('tr.inner').remove();
	});
	//关闭代课详情+发起申请
	$('.tab-item table').on('click','tr .abtn-send',function(e){
		e.preventDefault();
		alert('申请操作');
	});
	
	//班级查找定位
	//查找
	$('.sidebar-filter .txt').bind('focus keyup',function(e){
		var $list=$(this).parents('.sidebar-filter').find('.list');
		//输入显示隐藏
		if($(this).val().length){
			$list.show();
		}else{
			$list.hide();
		};
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
		var data_class=$(this).attr('data-class');
		var index=$('.sidebar-left li[data-class='+data_class+']').index();
		var pos_top=index*40;
		$('.sidebar-filter .list').hide();
		$('.sidebar-left ul').scrollTop(pos_top);
		$('.sidebar-left li[data-class='+data_class+']').addClass('current').siblings('li').removeClass();
	});
	
});


