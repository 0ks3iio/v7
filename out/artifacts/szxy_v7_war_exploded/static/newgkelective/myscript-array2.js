function winLoad(){
	
	winLoad2();
	return ;
	var all_w=$(window).width();
	var all_h=$(window).height();
	var header_h=$('#header').height();
//		var row_len=$('.content .syllabus-table tr:last').find('.sort').text();//总行数-除头、分隔行等
	var row_len=$('.content .syllabus-table tbody tr').length;//总行数-除头、分隔行等
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
	var side_all_w=$('.sidebar-right').width()-60;//教师课表剩余总宽度 序号列宽22
	var side_item_h=parseInt(side_all_h/side_row_len)-4;//教师课表每个单元格高度
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
	var divid = 373
	divid = 80;
	$('.sidebar-left ul').height(con_h-divid);
};
$(function(){
	//框架结构
	
	winLoad();
	clearTeaTable("1");
	clearTeaTable("2");
	$(window).resize(function(){
		winLoad();
	});
		
	//总课表操作
	$('.content .syllabus-table td .item').on('click','.replace a',function(e){
		//console.log("1");
		e.stopPropagation();
		var siblings = $(this).parent(".replace").siblings();
		var prev = siblings.children(".s-d-week:first-child");
		var next = siblings.children(".s-d-week:last-child");
		
		var left_id = prev.find("[name='id']").val();
		var left_teacherId = prev.find("[name='teacherId']").val();
		var left_weekType = prev.find("[name='weekType']").val();
		var left_placeId = prev.find("[name='placeId']").val();
		var left_classId = prev.find("[name='classId']").val();
		var left_subjectId = prev.find("[name='subjectId']").val();
		var left_subjectType = prev.find("[name='subjectType']").val();
		
		var right_id = next.find("[name='id']").val();
		var right_teacherId = next.find("[name='teacherId']").val();
		var right_weekType = next.find("[name='weekType']").val();
		var right_placeId = next.find("[name='placeId']").val();
		var right_classId = next.find("[name='classId']").val();
		var right_subjectId = next.find("[name='subjectId']").val();
		var right_subjectType = next.find("[name='subjectType']").val();
		
		var param ="";
		param += "leftSchedules[0].id="+left_id;
		if(left_teacherId) param += "&leftSchedules[0].teacherId="+left_teacherId;
		param += "&leftSchedules[0].weekType="+left_weekType;
		if(left_placeId) param += "&leftSchedules[0].placeId="+left_placeId;
		param += "&leftSchedules[0].classId="+left_classId;
		param += "&leftSchedules[0].subjectId="+left_subjectId;
		param += "&leftSchedules[0].subjectType="+left_subjectType;
		
		param += "&leftSchedules[1].id="+right_id;
		if(right_teacherId) param += "&leftSchedules[1].teacherId="+right_teacherId;
		param += "&leftSchedules[1].weekType="+right_weekType;
		if(right_placeId) param += "&leftSchedules[1].placeId="+right_placeId;
		param += "&leftSchedules[1].classId="+right_classId;
		param += "&leftSchedules[1].subjectId="+right_subjectId;
		param += "&leftSchedules[1].subjectType="+right_subjectType;
		
		var period = $(this).parents("td").attr("data-time");
		param += "&period=" + period+"&type=C";
		
		var arrayId = indexId;
		var url = contextPath + "/newgkelective/scheduleModify/"+arrayId+"/swapWeekType";
		$.ajax({
			url: url,
			data:param,
			type:"POST",
			dataType:"JSON",
			success:function(data){
				if(data.success){
					autoTips("转换成功");
					
					prev.find(".type").text("双");
					next.find(".type").text("单");
					var prevhtml = prev.html();
					var nexthtml = next.html();
					var prevWeekType = prev.find("[name='weekType']").val();
					var nextWeekType = next.find("[name='weekType']").val();
					siblings.empty().append('<span class="s-d-week">'+nexthtml+'</span><span class="s-d-week">'+prevhtml+'</span>');
					siblings.children(".s-d-week:first-child").find("[name='weekType']").val(prevWeekType);
					siblings.children(".s-d-week:last-child").find("[name='weekType']").val(nextWeekType);
				}else{
					autoTips(""+data.msg);
				}
			}
		});
		
	});

	
	var lock = {};
	lock.on = false;
	var preTime = 0;
	//选中
	$('.content .syllabus-table td .item').click(function(){
		
		if ($(this).find("[name='subjectType']").val() == 3) {
			autoTips("无法操作虚拟课程");
			return;
		}
		//console.log("2");
		var data_sel=parseInt($('body').attr('data-sel'));
		var data_lock=parseInt($('body').attr('data-lock'));
		//console.log("data_lock = "+data_lock+"  - "+"  data_sel= "+data_sel);
		if(lock.on == true){
			autoTips("请不要点击过快");
			return;
		}
		if(data_sel==0 && lock.on == false){
			var from_user=$('body > .int-from').attr('data-user');
			var to_user=$(this).attr('data-user');
			if(data_lock==0 && (!to_user || to_user == "0")){
				return;
			}
//			console.log("from_user="+from_user+"  to_user= "+to_user);
			lock.on = true;
			if(from_user!=to_user){
				if(!$(this).hasClass('item-lock')){
					$('body').attr('data-lock','1');
					
					var index_tr=$(this).parents('tr').index();
					var index_td=$(this).parent('td').index();
					var $table=$(this).parents('.syllabus-table');
					if(!$(this).hasClass('item-sel')){
						//点击位置调换、处理可选状态
						var sel=$table.attr('data-sel');//判断有没选中元素，第一次还是第二次选中：0-未选中/第二次选中、1-第一次选中
						var user=$(this).attr('data-user');
						//console.log("sel= "+sel);
						if(sel==0){
							var nowTime = new Date().getTime();
							if(nowTime - preTime < 500){
								autoTips("请不要点击过快");
								return;
							}
							//console.log("spend time: "+(nowTime - preTime));
							preTime = nowTime;
							
							lock.on = false;
							//console.log("lock.on = "+lock.on);
							//console.log("0000000000000");
							
							//处理选中状态
							$('.syllabus-table td .item').removeClass('item-sel');
							$('.syllabus-table').each(function(){
								$(this).children('tbody').children('tr:eq('+index_tr+')').children('td:eq('+index_td+')').children('.item').addClass('item-sel').removeClass('item-can');
							});
							
							$table.attr('data-sel',1);//改为选中状态
							var tid1 = $(this).find("[name='teacherId']").val();
							var tid2 = $(this).find("[name='teacherId']:eq(1)").val();
							//console.log("tid = "+tid1+" tid2 = "+tid2);
							// 显示禁排时间
							if(teacher_limit && tid1){ // 教师
								notime = teacherNoTimeObj[tid1];
								insMainNoTime(notime);
							}
							var subjectId1 = $(this).find("[name='subjectId']").val();
							var subjectId2 = $(this).find("[name='subjectId']:eq(1)").val();
							if(course_limit && subjectId1){
								var notime = subjectNoTime[subjectId1];
								insMainNoTime(notime);
							}
							
							if($(this).find(".s-d-week").length > 0){
								if(course_limit && subjectId2){
									var notime = subjectNoTime[subjectId2];
									insMainNoTime(notime);
								}
								
								if(teacher_limit && tid2){ // 教师
									var notime = teacherNoTimeObj[tid2];
									insMainNoTime(notime);
									//console.log("tid notime2 = "+notime);
								}
							}
							
							// 将上方教师课表插入下方 
							fixTeacherTable(this);
							//TODO 显示此教师的所有 禁排时间
							showTeacherNoTime(this);
							
							//添加可选状态
							$('.content .syllabus-table td .item').not('.item-lock').each(function(){
								var my_user=$(this).attr('data-user');
								if(user!=my_user && !$(this).is(".item-conflict")){
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
							/*$('.content .syllabus-table td .item').hover(function(){
								var over_tr=$(this).parents('tr').index();
								var over_td=$(this).parent('td').index();
								$('.syllabus-table').each(function(){
									$(this).children('tbody').children('tr:eq('+over_tr+')').children('td:eq('+over_td+')').children('.item').addClass('item-over');
								});
							},function(){
								$('.syllabus-table').find('.item').removeClass('item-over');
							});*/
						}else{
							//lock.on = true;
							//console.log("2. lock.on = "+lock.on);
							//TODO 课表内 移动课程 冲突判断
							var now_time = $(this).parents("td").attr("data-time");
							var old_time = $("#teacherTable2 tbody td .item-sel").parents("td").attr("data-time");
							
							var teacherId1 = $(this).find("[name='teacherId']:eq(0)").val();
							var teacherId2 = $(this).find("[name='teacherId']:eq(1)").val();
							if(!teacherId1 && !teacherId2){
								clearTeaTable("1");
							}
							var f = true;
							var g = true;
							
							//sleep(300);
//							f = checkTeacherConflict("teacherTable2",now_time);
//							g = checkTeacherConflict("teacherTable1",old_time);
//							
//							if(!f || !g) return;
							// 科目禁排判断
							if(to_user != 0){
								var subIds = [];
								var subjectId1 = $(this).find("[name='subjectId']").val();
								var subjectId2 = $(this).find("[name='subjectId']:eq(1)").val();
								subIds.push(subjectId1);
								if(subjectId2){
									subIds.push(subjectId2);
								}
								for(i in subIds){
									var subId = subIds[i];
									var notime = subjectNoTime[subId];
									if(course_limit && notime && notime.search(old_time) > -1){
										autoTips($(this).find("[class$='ourse']:eq("+i+")").text()+"在目标时间禁排");
										lock.on = false;
										return;
									}
								}
							}
							
							// 老师禁排判断
							var tids = [teacherId1,teacherId2];
							for(i in tids){
								var tid = tids[i];
								var notime = teacherNoTimeObj[tid];
								//console.log("tt tid= "+tid+"  notime = "+notime);
								if(!tid || !notime) continue;
								if(teacher_limit && notime && notime.search(old_time) > -1){
									autoTips("教师 "+$(this).find("[class$='ame']:eq("+i+")").text()+" 在目标时间禁排");
									lock.on = false;
									return;
								}
							}
							
							
							
							var id_old = $(".content tbody td .item-sel").find("[name='id']").val();
							var placeId_old = $(".content tbody td .item-sel").find("[name='placeId']").val();
							var teacherId_old = $(".content tbody td .item-sel").find("[name='teacherId']").val();
							var weekType_old = $(".content tbody td .item-sel").find("[name='weekType']").val();
							var subjectId_old = $(".content tbody td .item-sel").find("[name='subjectId']").val();
							var subjectType_old = $(".content tbody td .item-sel").find("[name='subjectType']").val();
							
							var id_now = $(this).find("[name='id']").val();
							var placeId_now = $(this).find("[name='placeId']").val();
							var teacherId_now = $(this).find("[name='teacherId']").val();
							var weekType_now = $(this).find("[name='weekType']").val();
							var subjectId_now = $(this).find("[name='subjectId']").val();
							var subjectType_now = $(this).find("[name='subjectType']").val();
							
							var param = "";
							var classId = objId;
							if($(".content tbody td .item-sel").find(".s-d-week").length > 0){
								// 源头 单双周
								var id_old2 = $(".content tbody td .item-sel").find("[name='id']:eq(1)").val();
								var placeId_old2 = $(".content tbody td .item-sel").find("[name='placeId']:eq(1)").val();
								var teacherId_old2 = $(".content tbody td .item-sel").find("[name='teacherId']:eq(1)").val();
								var weekType_old2 = $(".content tbody td .item-sel").find("[name='weekType']:eq(1)").val();
								var subjectId_old2 = $(".content tbody td .item-sel").find("[name='subjectId']:eq(1)").val();
								var subjectType_old2 = $(".content tbody td .item-sel").find("[name='subjectType']:eq(1)").val();
								
								param += "&leftSchedules[1].id="+id_old2;
								param += "&leftSchedules[1].placeId="+placeId_old2;
								param += "&leftSchedules[1].classId="+classId;
								param += "&leftSchedules[1].teacherId="+teacherId_old2;
								param += "&leftSchedules[1].weekType="+weekType_old2;
								
								param += "&leftSchedules[1].subjectId="+subjectId_old2;
								param += "&leftSchedules[1].subjectType="+subjectType_old2;
							}
							if($(this).find(".s-d-week").length > 0){
								// 目的地 单双周
								var id_now2 = $(this).find("[name='id']:eq(1)").val();
								var placeId_now2 = $(this).find("[name='placeId']:eq(1)").val();
								var teacherId_now2 = $(this).find("[name='teacherId']:eq(1)").val();
								var weekType_now2 = $(this).find("[name='weekType']:eq(1)").val();
								var subjectId_now2 = $(this).find("[name='subjectId']:eq(1)").val();
								var subjectType_now2 = $(this).find("[name='subjectType']:eq(1)").val();
								
								param += "&rightSchedules[1].id="+id_now2;
								param += "&rightSchedules[1].placeId="+placeId_now2;
								param += "&rightSchedules[1].classId="+classId;
								param += "&rightSchedules[1].teacherId="+teacherId_now2;
								param += "&rightSchedules[1].weekType="+weekType_now2;
								
								param += "&rightSchedules[1].subjectId="+subjectId_now2;
								param += "&rightSchedules[1].subjectType="+subjectType_now2;
							}
							
							//console.log("id_now= "+id_now+" placeId_now= "+ placeId_now+" subjectId_now= "+subjectId_now);
							//console.log("id_old= "+id_old+" placeId_old= "+ placeId_old+" subjectId_old= "+subjectId_old);
							
							param += "&leftSchedules[0].id="+id_old;
							param += "&leftSchedules[0].placeId="+placeId_old;
							param += "&leftSchedules[0].classId="+classId;
							param += "&leftSchedules[0].teacherId="+teacherId_old;
							param += "&leftSchedules[0].weekType="+weekType_old;
							param += "&leftSchedules[0].subjectId="+subjectId_old;
							param += "&leftSchedules[0].subjectType="+subjectType_old;
							if(id_now){
								param += "&rightSchedules[0].id="+id_now;
								param += "&rightSchedules[0].placeId="+placeId_now;
								param += "&rightSchedules[0].classId="+classId;
								param += "&rightSchedules[0].teacherId="+teacherId_now;
								param += "&rightSchedules[0].weekType="+weekType_now;
								param += "&rightSchedules[0].subjectId="+subjectId_now;
								param += "&rightSchedules[0].subjectType="+subjectType_now;
							}
							
							param += "&leftPeriod="+old_time;
							param += "&rightPeriod="+now_time;
							param += "&fromAlone="+fromAlone;
							
							param = param.substr(1);
							//console.log(param);
//							return ;
							var arrayId = indexId;
							var url = contextPath + "/"+url_sub_sys+"/scheduleModify/"+arrayId+"/swapCourse";
							var obj = this;
							$.ajax({
								url: url,
								data:param,
								type:"POST",
								dataType:"JSON",
								success:function(data){
									if(data.success){
//										autoTips("交换成功");
										doSwap(obj);
									}else{
										autoTips(""+data.msg);
										lock.on = false;
									}
								}
							});
							
						};
					}else{
						//console.log("333333333333");
						//取消选中、可选状态；移除位置调换添加的元素
						$('.syllabus-table td .item').removeClass('item-can item-sel int-from int-to');
						$table.attr('data-sel',0).attr('data-lock',0);
						//移除插入的元素
						$('body span.item').remove();
						lock.on = false;
					};
				}else{
//					autoTips('已预约课程不能移动');
					if($(this).hasClass("movePeriod")){
						autoTips('部分学生走班时间，此时间禁排');
					}else if($(this).hasClass("item-conflict")){
						autoTips('教师有课或禁排时间，不能排课');
					}else{
						autoTips('此时间禁排');
					}
					lock.on = false;
				};
			}else{
				lock.on = false;
				autoTips('同一门课程不能移动');
			};
		};
	});
	
	function updateTeaSchedule(leftPeriod,rightPeriod){
		;
		var $item = $("#allTable tbody td[data-time='"+leftPeriod+"'] .item");
		$item.find("input[name='id']").each(function(){
			var id = $(this).val();
			var tid = $(this).parent().find("[name='teacherId']").val();
			if(tid && teacherScheduleMap[tid]){
				var tcs = teacherScheduleMap[tid];
				for(x in tcs){
					var cs = tcs[x];
					if(cs.id == id){
						
						var time = leftPeriod.split("_");
						cs.dayOfWeek = time[0];
						cs.periodInterval = time[1];
						cs.period = time[2];
						break;
					}
				}
			}
		});
		$item = $("#allTable tbody td[data-time='"+rightPeriod+"'] .item");
		$item.find("input[name='id']").each(function(){
			var id = $(this).val();
			var tid = $(this).parent().find("[name='teacherId']").val();
			if(tid && teacherScheduleMap[tid]){
				var tcs = teacherScheduleMap[tid];
				for(x in tcs){
					var cs = tcs[x];
					if(cs.id == id){
						var time = rightPeriod.split("_");
						cs.dayOfWeek = time[0];
						cs.periodInterval = time[1];
						cs.period = time[2];
						break;
					}
				}
			}
		});
		
		
		
	}
	//取消选中、可选状态；移除位置调换添加的元素
	$('html').off("click").on('click','body > .item',function(){
		if(lock.on == true){
			autoTips('请不要点击过快');
			return;
		}
		
		
		$('body').attr('data-lock','0');
		$('.syllabus-table td').removeClass('int-from-wrap int-to-wrap').children('.item').removeClass('item-can item-sel int-from int-to');
		$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
			$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
		//	console.log("取消科目禁排时间");
		});
		freshFixedNotime();
		// 取消下方课表内容
		clearTeaTable("2");
		$('.content .syllabus-table').attr('data-sel',0);
		//移除插入的元素
		$('body > .int-from,body > .int-to').remove();
	});
	
	
	//滑过
	var preTdf = ""; 
	var nowTdf = "";
	$('.content .syllabus-table td .item,.syllabus-from li').not('.item-lock').mouseenter(function(){
		var timestr = $(this).parent("td").attr("data-time");
		nowTdf = timestr;
		
		var user=$(this).attr('data-user');
		if($(this).find(".s-d-week").length > 0 || $(this).hasClass("s-d-week")){
			var users = user.split(",");
			
			$('#allTable td .item[data-user="'+users[0]+'"]').addClass('item-hover');
			$('#allTable td .item[data-user="'+users[1]+'"]').addClass('item-hover');
		}else{
			if(user!=0){
				$('#allTable td .item[data-user="'+user+'"]').addClass('item-hover');
			};
		}
		
		var over_tr=$(this).parents('tr').index();
		var over_td=$(this).parent('td').index();
		$('.syllabus-table').each(function(){
			$(this).children('tbody').children('tr:eq('+over_tr+')').children('td:eq('+over_td+')').children('.item').addClass('item-over');
		});
		var obj = this;
		// 延时半秒
		setTimeout(function(){
			if(timestr == nowTdf){
				//获取教师课表
				var teacherId1 = $(obj).find("[name='teacherId']:eq(0)").val();
				var teacherId2 = $(obj).find("[name='teacherId']:eq(1)").val();
				var teacherIds = [teacherId1, teacherId2];
				getTeSchedule(teacherIds);
				//console.log("------hover--------"+new Date().getTime());
			}else{
				//console.log("td 已经改变");
			}
		},500);
		
//		preTdf = timestr;
	});
	
	$('.content .syllabus-table td .item,.syllabus-from li').not('.item-lock').mouseleave(function(){
		$('.syllabus-table td .item').removeClass('item-hover').removeClass('item-over');
	});
	
	
	/* 业务js  */
	
	function checkTeacherConflict(tableId, theTime){
		//console.log("-check conflict- s t--"+$("#"+tableId).prev("p").find(".tab").length);
		if($("#"+tableId).prev("p").find(".tab").length > 0){
			// 单双周
			var scheduleObj = teacherTables[tableId];
//			console.log("theTime = "+theTime);
			for(tid in scheduleObj){
				var schedules = scheduleObj[tid];
				//console.log("tid = "+tid+" "+teacherNameMap[tid]+"schedules.l = "+schedules.length);
				for(i in schedules){
					var x = schedules[i];
					var timestr = x.dayOfWeek +"_"+x.periodInterval+"_"+x.period;	
//					console.log("timestr = "+timestr+(timestr == theTime));
					if(timestr == theTime){
						autoTips("教师 "+x.teacherName+" 冲突");
						return false;
					}
				}
			}
		}else{
			var subjectId = $("#"+tableId+" tbody td[data-time='"+theTime+"']").find(".item [name='subjectId']").val();
			//console.log("subId = "+subjectId);
			if(subjectId && subjectId != "0"){
				var tname = $("#"+tableId).prev("p").find("span").text();
				autoTips("教师 "+tname+" 冲突");
				return false;
			}
		}
		return true;
	}
	
	// 填写老师课表 fix:是否将老师课表固定到下方
	function getTeSchedule(teacherIds,fix){
		//console.log("old teacherIds= "+teacherIds);
		if(!teacherIds || (!teacherIds[0] && !teacherIds[1])){
			clearTeaTable("1");
			return;
		}
		if(!teacherIds[1])
			teacherIds.pop();
		else if(!teacherIds[0]) 
			teacherIds = teacherIds.slice(1);
		
		//console.log("teacherIds= "+teacherIds);
		var arrayId = indexId;
		var url = contextPath + "/"+url_sub_sys+"/scheduleModify/"+arrayId+"/teacherSchedule?";
		var searchWeek = $(".ui-select-value").val();
		//console.log("url: "+url);
		$.ajax({
			url: url,
			data: {"teacherIds" : teacherIds,"searchAcadyear":searchAcadyear,"searchSemester":searchSemester,"weekIndex":searchWeek},
			success:function(data){
				//console.log(data);
				insTeaSchedule(teacherIds,data);
				//TODO 将制定教师的课程 标为 禁排
				
				if(fix) fixTeacherTable();
			},
			dataType:"JSON",
			type:"POST"
		});
	}
	
	function insTeaSchedule(teacherIds, scheduleObj){
		//TODO 教室单双周课表的显示
		clearTeaTable("1");
		
		var spantitle = "";
		if(teacherIds.length > 1){
			// 单双周
			teacherTables["teacherTable1"] = scheduleObj;
			
			var tname1 = teacherNameMap[teacherIds[0]];
			var tname2 = teacherNameMap[teacherIds[1]];
			spantitle = '<span class="tab switch active" data-tid="'+teacherIds[0]+'">\
								<a class="ml-5" href="javascript:void(0);"><img style="position: relative;top: 2px;" src="/static/images/array/fullscreen.png"></a>\
								<span class="mr-5 ml-5">'+tname1+'</span>老师课程表\
							</span>\
							<span class="tab" data-tid="'+teacherIds[1]+'">\
								<a class="ml-5" href="javascript:void(0);"><img style="position: relative;top: 2px;" src="/static/images/array/fullscreen.png"></a>\
								<span class="mr-5 ml-5">'+tname2+'</span>老师课程表\
							</span>';
			
			$("#teacherTable1").prev("p").html(spantitle);
		}else{
			var tname = teacherNameMap[teacherIds[0]];
			spantitle = '<span class="switch" style="float:left;width:100%;" data-tid="'+teacherIds[0]+'"><span class="mr-5 ml-5 " >'+tname+'</span>老师课程表</span>';
		}
		$("#teacherTable1").prev("p").html(spantitle);
		var schedules = scheduleObj[teacherIds[0]];
		insScheduleOnly("teacherTable1",schedules);

		//winLoad();
	}
	
	// 添加科目禁排时间和 教师禁排时间
	function insMainNoTime(notime){
		if(!notime) return;
		var timeArr = notime.split(",");
		$('.content .syllabus-table td .item').not('.item-lock').each(function(){
			var timestr = $(this).parents("td").attr("data-time");
			if(timeArr.indexOf(timestr) > -1){
				$(this).addClass('item-lock').addClass('item-subject-no');
			};
		});
	} 
	
	// 点击后显示下方课表
	function fixTeacherTable(obj){
		if(obj){
			var teacherId1 = $(obj).find("[name='teacherId']:eq(0)").val();
			var teacherId2 = $(obj).find("[name='teacherId']:eq(1)").val();
			var teacherIds = [teacherId1, teacherId2];
			getTeSchedule(teacherIds,true);
			//console.log("--获取教师信息----");
		}else{
			$("#teacherTable2").html($("#teacherTable1").html());
			$("#teacherTable2").prev("p").html($("#teacherTable1").prev("p").html());
			teacherTables["teacherTable2"] = teacherTables["teacherTable1"];
			//console.log("固定教师");
		}
	}
	// 点击后 显示所有 老师不能排的课 
	function showTeacherNoTime(obj){
		var id_len = $(obj).find("[name='id']").length;
		var teacherId1 = $(obj).find("[name='teacherId']:eq(0)").val();
		var teacherId2 = $(obj).find("[name='teacherId']:eq(1)").val();
		var subjectId1 = $(obj).find("[name='subjectId']:eq(0)").val();
		var subjectId2 = $(obj).find("[name='subjectId']:eq(1)").val();
		var subjectType1 = $(obj).find("[name='subjectType']:eq(0)").val();
		var subjectType2 = $(obj).find("[name='subjectType']:eq(1)").val();
		var classId = objId;
		var time = $("#allTable tbody td .item-sel").parents("td").attr("data-time");
		
		$("#allTable tbody td .item:not(.item-lock)").each(function(){
			var t = $(this).parents("td").attr("data-time");
			if(t == time){
				return;
			}
			// 选定点有两节课（单双周）时的情况
			// 选定点 在此处的 禁排情况
			var weekType = parseInt($(obj).find("input[name='weekType']").val());
			var weekType2 = parseInt($(obj).find("input[name='weekType']:eq(1)").val());
			
			var f = checkConflict(this,t,weekType,subjectType1,teacherId1,subjectId1);
			if(f){
				return;
			}
			
			if(id_len ==2){
				// 第二个
				f = checkConflict(this,t,weekType2,subjectType2,teacherId2,subjectId2);
				if(f){
					return;
				}
			}
			
			// 其他点 在 选定点的冲突情况
			// 其他点 在选定点的 禁排显示
			if(!time){
				// 点击底部课程
				return;
			}
			
			var len = $(this).find("input[name='id']").length;
			if(len < 1){
				return;
			}
			var tid = $(this).find("input[name='teacherId']").val();
			var tid2 = $(this).find("input[name='teacherId']:eq(1)").val();
			var subId = $(this).find("input[name='subjectId']").val();
			var subId2 = $(this).find("input[name='subjectId']:eq(1)").val();
			var subType = $(this).find("input[name='subjectType']").val();
			var subType2 = $(this).find("input[name='subjectType']:eq(1)").val();
			var weekType = parseInt($(this).find("input[name='weekType']").val());
			var weekType2 = parseInt($(this).find("input[name='weekType']:eq(1)").val());
			
			if(!tid && !tid2){
				return;
			}
			
			f = checkConflict(this,time,weekType,subType,tid,subId);
			if(f){
				return;
			}
			
			if(len == 2){
				f = checkConflict(this,time,weekType2,subType2,tid2,subId2);
				if(f){
					return;
				}
			}
		});
	}
	
	function checkConflict(obj,time,weekType,subType,tid,subId){
		var tcs = teacherScheduleMap[tid];
		var gp = combineRes[oldId+"-"+subId+"-"+subType];
		if(!tcs){
			return false;
		}
		weekType = parseInt(weekType);
		for(x in tcs){
			var cs = tcs[x];
			
			var ts = cs.dayOfWeek+"_"+cs.periodInterval+"_"+cs.period;
			if(ts == time && (parseInt(cs.weekType) + weekType) != 3 && (!gp || !gp[cs.oldDivideClassId+"-"+cs.subjectId+"-"+cs.subjectType])){
				$(obj).addClass("item-conflict");
			}
		}
		var notime = teacherNoTimeObj[tid];
		if(teacher_limit && notime && notime.includes(time)){
			$(obj).addClass("item-lock").addClass("item-subject-no");
		}
		notime = subjectNoTime[subId];
		if(course_limit && notime && notime.includes(time)){
			$(obj).addClass("item-lock").addClass("item-subject-no");
		}
		if($(obj).is(".item-lock")){
			return true;
		}
		return false;
	}
	
	// 执行交换效果 obj最后 点击的td -> span.item
	function doSwap(obj){
		var leftPeriod = $("#allTable tbody .item-sel").parents("td").attr("data-time");
		var rightPeriod = $(obj).parents("td").attr("data-time");
		$(obj).parents('.syllabus-table').attr('data-sel',0);//改为未选中状态
		// 取消科目禁排时间
		$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
			$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
			//console.log("取消科目禁排时间");
		});
		freshFixedNotime();
		//取消可选状态
		$('.syllabus-table td .item').not('.item-lock').each(function(){
			$(this).removeClass('item-can item-sel');
		});
		clearTeaTable("2");
		
		//获取当前选中元素 插入body
		$(obj).addClass('int-to').parent('td').addClass('int-to-wrap');
		to_top=parseInt($(obj).offset().top)-1;
		to_left=parseInt($(obj).offset().left);
		to_width=parseInt($(obj).width());
		to_height=parseInt($(obj).height());
		if(!$('body > .int-to').length){
			$('body > .int-to').remove();
		};
		$(obj).clone().css({left:to_left,top:to_top,width:to_width,height:to_height}).prependTo('body');
		//alert('第二次选中/取消选中');
		
		//移动效果
		$('body > .int-from').stop(true,true).animate({top:to_top,left:to_left},500).siblings('.int-to').stop(true,true).animate({top:from_top,left:from_left},500,function(){
			$('body > .int-from,body > .int-to').remove();//移除插入的元素
			//数据对调
			$('.syllabus-table td .int-from').appendTo('.int-to-wrap');
			$('.syllabus-table td .int-to').appendTo('.int-from-wrap');
			$('.syllabus-table td').removeClass('int-from-wrap int-to-wrap');
			$('.syllabus-table td .item').removeClass('int-from int-to');
			lock.on = false;
			$('body').attr('data-lock','0');
			updateTeaSchedule(leftPeriod,rightPeriod);
		});
		//鼠标滑过状态
		/*$('.content .syllabus-table td .item').hover(function(){
			$('.syllabus-table').find('.item').removeClass('item-over');
		});*/
	}
	
	function ioSwap(obj){
		var s_d_week = $(obj).hasClass("s-d-week") ? true : false;
		//效果
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
		var into_user=$(obj).attr('data-user');
		var into_course=$(obj).find('.course').text();
		var into_name=$(obj).find('.name').text();
		var into_room=$(obj).find('.room').text();
		var into_othercourse=$(obj).find('.otherCourse').text();
		var into_othername=$(obj).find('.otherName').text();
		var into_otherroom=$(obj).find('.otherRoom').text();
		
		var all_len=$('.int-from').children('span').length;
		var all_h=$('.int-from').height();
		var span_len=3;
		if(into_room == '' && into_otherroom == ''){span_len=2};
		var span_h=parseInt(all_h/span_len)+'px';
		var type_top=parseInt((all_h/span_len - 15)/2)+'px';
		
		var $ts = $(obj).find(".tt").children('span');
		var $str1='<span>\
			   <span class="s-d-week">\
			   	    <input type="hidden" name="id" value=""/>\
	        		<input type="hidden" name="subjectId" value="'+$ts.eq(0).find("[name='subjectId']").val()+'"/>\
	        		<input type="hidden" name="subjectType" value="'+$ts.eq(0).find("[name='subjectType']").val()+'"/>\
	        		<input type="hidden" name="placeId" value="'+$ts.eq(0).find("[name='placeId']").val()+'"/>\
	        		<input type="hidden" name="teacherId" value="'+$ts.eq(0).find("[name='teacherId']").val()+'"/>\
	        		<input type="hidden" name="classId" value="'+$ts.eq(0).find("[name='classId']").val()+'"/>\
	        		<input type="hidden" name="classType" value="'+$ts.eq(0).find("[name='classType']").val()+'"/>\
	        		<input type="hidden" name="weekType" value="'+$ts.eq(0).find("[name='weekType']").val()+'"/>\
            	   <span style="height:'+span_h+';line-height:'+span_h+'">\
            	   	   <span class="course" title="'+into_course+'">'+into_course+'</span>\
            	   	   <span class="type" style="top:-'+type_top+'">单</span>\
            	   </span>\
            	   <span class="name" style="height:' + span_h + ';line-height:'+span_h+';">'+into_name+'</span>\
            	   <span class="room" style="height:'+span_h+';line-height:'+span_h+';">'+into_room+'</span>\
               </span>\
               <span class="s-d-week">\
	        		<input type="hidden" name="id" value=""/>\
	        		<input type="hidden" name="subjectId" value="'+$ts.eq(1).find("[name='subjectId']").val()+'"/>\
	        		<input type="hidden" name="subjectType" value="'+$ts.eq(1).find("[name='subjectType']").val()+'"/>\
	        		<input type="hidden" name="placeId" value="'+$ts.eq(1).find("[name='placeId']").val()+'"/>\
	        		<input type="hidden" name="teacherId" value="'+$ts.eq(1).find("[name='teacherId']").val()+'"/>\
	        		<input type="hidden" name="classId" value="'+$ts.eq(1).find("[name='classId']").val()+'"/>\
	        		<input type="hidden" name="classType" value="'+$ts.eq(1).find("[name='classType']").val()+'"/>\
	        		<input type="hidden" name="weekType" value="'+$ts.eq(1).find("[name='weekType']").val()+'"/>\
                   <span style="height:'+span_h+';line-height:'+span_h+';">\
            	   	   <span class="otherCourse" title="'+into_othercourse+'">'+into_othercourse+'</span>\
            	   	   <span class="type" style="top:-'+type_top+'">双</span>\
            	   </span>\
            	   <span class="otherName" style="height:'+span_h+';line-height:'+span_h+';">'+into_othername+'</span>\
            	   <span class="otherRoom" style="height:'+span_h+';line-height:'+span_h+';">'+into_otherroom+'</span>\
               </span>\
           </span>\
           <span class=\"replace"><a href="#" class="fa fa-refresh"></a></span>';
//		var $str2='<span class="course" style="height:'+span_h+';line-height:'+span_h+'">'+into_course+'</span><span class="name" style="height:'+span_h+';line-height:'+span_h+'">'+into_name+'</span><span class="room" style="height:'+span_h+';line-height:'+span_h+'">'+into_room+'</span>';
		$str2 = $(obj).find(".tt").html();
		var $str = s_d_week ? $str1 : $str2;
		
		var $li_into=$('.syllabus-from  li[data-user="'+into_user+'"]');
		var $sur_into=$li_into.find('.surplus').children('span');
		var sur_into=parseInt($sur_into.text());
		var $li_from=$('.syllabus-from  li[data-user="'+from_user+'"]');
		var $sur_from=$li_from.find('.surplus').children('span');
		var sur_from=parseInt($sur_from.text());
		
		if(from_user == into_user || $(obj).hasClass('empty')){
			// 从课表中 移除一节课
			console.log("-------从课表中 移除一节课----------");
			if(all_len != 0){
				$('body').attr('data-sel',0).attr('data-lock',0).children('span.item').remove();
				$('.int-from').stop(true,true).animate({top:into_top,left:into_left},500,function(){
					$('.syllabus-table td.int-from-wrap').children('.item').css({left: 0, top: 0}).attr('data-user','0').empty();
					winLoad();
					refreshTab(true);
				});
				//还原
				$('.syllabus-table td .item').removeClass('item-can item-sel int-from int-to');
				$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
					$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
				//	console.log("取消科目禁排时间");
				});
				freshFixedNotime();
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
			console.log("------- 从上 向下 交换一节课----------"+all_len);
			$(obj).clone().addClass('sel cln-item').css({top:into_top,left:into_left}).prependTo($('body'));
			$('.int-from').stop(true,true).animate({top:into_top,left:into_left},400).siblings('.cln-item').stop(true,true).animate({top:from_top,left:from_left},400,function(){
				$('.int-from').css({left: 0, top: 0}).removeClass('int-from');
				$('body').attr('data-sel',0).attr('data-lock',0).children('span.item,.cln-item').remove();
				winLoad();
				refreshTab(true);
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
			$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
				$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
				//console.log("取消科目禁排时间");
			});
			freshFixedNotime();
			$('#allTable').attr('data-sel',0);
			clearTeaTable("2");
		};
		//console.log("12");
	}
	// 从里往外排
	function ioSwap2(obj,e){
		var overObj = $(".syllabus-from li.sel");
		var s_d_week = overObj.hasClass("s-d-week") ? true : false;
		//console.log("s-d-week = "+s_d_week);
		cln_top=parseInt($(overObj).offset().top);
		cln_left=parseInt($(overObj).offset().left);
		cln_course=$(overObj).find('.course').text();
		cln_name=$(overObj).find('.name').text();
		cln_room=$(overObj).find('.room').text();
		cln_cnt =$(overObj).find('.tt').html();  //TODO 
		cln_othercourse=$(overObj).find('.otherCourse').text();
		cln_othername=$(overObj).find('.otherName').text();
		cln_otherroom=$(overObj).find('.otherRoom').text();
		
		//var cln_user=$('.cln-item').attr('data-user');
		var from_top=parseInt($('.syllabus-from').offset().top);
		var from_left=parseInt($('.syllabus-from').offset().left);
		var into_top=parseInt($(obj).offset().top)-1;
		var into_left=parseInt($(obj).offset().left);
		var all_h=$(obj).height();
		
		var span_len=3;
		if(cln_room == '' && cln_otherroom == ''){span_len=2};
		var span_h=parseInt(all_h/span_len)+'px';
		var type_top=parseInt((all_h/span_len - 15)/2)+'px'; 
		var $str1='<span>\
					   <span class="s-d-week">\
                    	   <span style="height:'+span_h+';line-height:'+span_h+';">\
                    	   	   <span class="course">'+cln_course+'</span>\
                    	   	   <span class="type" style="top:-'+type_top+'">单</span>\
                    	   </span>\
                    	   <span class="name" style="height:'+span_h+';line-height:'+span_h+';">'+cln_name+'</span>\
                    	   <span class="room" style="height:'+span_h+';line-height:'+span_h+';">'+cln_room+'</span>\
                       </span>\
                       <span class="s-d-week">\
                           <span style="height:'+span_h+';line-height:'+span_h+';">\
                    	   	   <span class="otherCourse">'+cln_othercourse+'</span>\
                    	   	   <span class="type" style="top:-'+type_top+'">双</span>\
                    	   </span>\
                    	   <span class="otherName" style="height:'+span_h+';line-height:'+span_h+';">'+cln_othername+'</span>\
                    	   <span class="otherRoom" style="height:'+span_h+';line-height:'+span_h+';">'+cln_otherroom+'</span>\
                       </span>\
                   </span>\
                   <span class="replace"><a href="#" class="fa fa-refresh"></a></span>';
		var $str2='<span class="course" style="height:'+span_h+';line-height:'+span_h+'">'+cln_course+'</span><span class="name" style="height:'+span_h+';line-height:'+span_h+'">'+cln_name+'</span><span class="room" style="height:'+span_h+';line-height:'+span_h+'">'+cln_room+'</span>';
		$str2 = cln_cnt;
		var $str = s_d_week ? $str1 : $str2;
		
		//移出已排课程
		$(obj).clone().css({top:into_top,left:into_left}).prependTo($('body'));
		var my_user=$(obj).attr('data-user');
		var $li=$('.syllabus-from  li[data-user="'+my_user+'"]');
		var $sur=$li.find('.surplus').children('span');
		var sur=parseInt($sur.text())+1;
		$sur.text(sur);//未排课程赋值
		//如果未排课程大于0，移除排完状态
		if(sur>0){
			$sur.parents('li').removeClass('over').show();
		};
		//如果未排课程没有当前课程，插入增加一个
//		if(my_user != 0 && $li.length == 0){
//			my_total=$('#allTable').find('span.item[data-user='+my_user+']').length;//当前所选老师总课程数
//			my_course=$(this).find('.course').text();
//			my_name=$(this).find('.name').text();
//			my_room=$(this).find('.room').text();
//
//			var $my_str='<li data-user="'+my_user+'"><p class="item"><span class="surplus" data-total="'+my_total+'"><span>1</span>节</span><span class="tt"><span class="course">'+my_course+'</span><span class="name">'+my_name+'</span><span class="room">'+my_room+'</span></span></p></li>'
//			$('.syllabus-from').append($my_str);
//			$('.syllabus-from li.empty').appendTo('.syllabus-from');
//		};
		console.log("下往上移动课程");
		//移动并插入数据
		$('#allTable span.item').removeClass('item-can');
		$(obj).empty().hide().attr('data-user',cln_user).append($str).delay(500).fadeIn(0);
		//$('body > .int-from').stop(true,true).animate({top:to_top,left:to_left},500).siblings('.int-to').stop(true,true).animate({top:from_top,left:from_left},500,function(){
		$('.cln-item').stop(true,true).animate({top:into_top,left:into_left},500).siblings('.item-can').stop(true,true).animate({top:from_top,left:from_left},500,function(){
			$('body > .cln-item,body > .item-can').remove();
			winLoad();
			refreshTab(true);
		});
		
		//还原效果
		$('body').attr('data-sel',0);
		$('.syllabus-from li.sel').removeClass('sel');
		
		$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
			$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
		    //console.log("取消科目禁排时间");
		});
		freshFixedNotime();
		clearTeaTable("2");
		
		//处理剩余课数
		var yet_num=parseInt($('#allTable').find('span.item[data-user="'+cln_user+'"]').length);//当前所选老师已排程数
		sur_num=total_num-yet_num;
		$('.syllabus-from li[data-user="'+cln_user+'"]').find('.surplus').children('span').text(sur_num);
		if(sur_num==0){
			$('.syllabus-from li[data-user="'+cln_user+'"]').addClass('over').hide();
		};
		$(obj).unbind(e);
		//console.log("21");
	}
	
	var isSaving = false;
	//底部未排课程
	$('.syllabus-from').on('click','li',function(){
		if(isSaving){
			return;
		}
		//console.log("4");
		var s_d_week = $(this).hasClass("s-d-week") ? true : false;
		var data_lock=parseInt($('body').attr('data-lock'));
		var surplus = parseInt($(this).find('.surplus > span').text());
		//console.log("data_lock = "+data_lock +"s-d-week = "+s_d_week);
		if(surplus && surplus<1)
			return;
		if(data_lock == 0){
			//从里往外排课
		    //console.log($(this).hasClass('over'));
			if(!$(this).hasClass('over')){
				cln_user=$(this).attr('data-user');
				if(!cln_user || cln_user=='0' || $(this).hasClass("empty") || surplus<1){
					return;
				}
				
				$('body').attr('data-sel',1).children('.cln-item').remove();
				$('.content .syllabus-table').attr('data-sel',0).find('td').removeClass('int-from-wrap int-to-wrap').children('.item').removeClass('int-from int-to');
				$(this).addClass('sel').siblings('li').removeClass('sel');
				total_num=parseInt($(this).find('.surplus').attr('data-total'));//当前所选老师总课程数
				sur_num=parseInt($(this).find('.surplus').children('span').text());//当前所选老师剩余未排程数
				$('body > .int-from').remove()
				$('.syllabus-table span.item').removeClass('item-can item-sel');
			    //console.log("取消科目禁排时间");
				$('.syllabus-table td .item-subject-no,.syllabus-table td .item-conflict').each(function(){
					$(this).removeClass('item-lock').removeClass("item-subject-no").removeClass("item-conflict");
				    //console.log("取消科目禁排时间");
				});
				freshFixedNotime();
				showTeacherNoTime(this);
				//添加课表可操作状态
				$('#allTable span.item').not('.item-lock').each(function(){
					var my_user=$(this).attr('data-user');
					if(my_user != cln_user && !$(this).is(".item-conflict")){
						$(this).addClass('item-can');
					};
				});
				// 禁排时间
				// 显示禁排时间
				var tid1 = $(this).find("[name='teacherId']").val();
				var tid2 = $(this).find("[name='teacherId']:eq(1)").val();
				if(teacher_limit && tid1){ // 教师
					notime = teacherNoTimeObj[tid1];
				    //console.log("tid notime1 = "+notime);
					insMainNoTime(notime);
				}
				if(s_d_week){
					var subjectId = $(this).find("[name='subjectId']").val();
					var subjectId2 = $(this).find("[name='subjectId']:eq(1)").val();
					var subjIds = [subjectId,subjectId2];
				    //console.log("单双周禁排   "+subjIds);
					for(i in subjIds){
						var notime = subjectNoTime[subjIds[i]];
//						console.log(notime);
						if(course_limit) insMainNoTime(notime);
					}
					if(teacher_limit && tid2){ // 教师
						var notime = teacherNoTimeObj[tid2];
						insMainNoTime(notime);
					    //console.log("tid notime2 = "+notime);
					}
				}else{
					var subjectId = $(this).find("[name='subjectId']").val();
					var notime = subjectNoTime[subjectId];
					if(course_limit) insMainNoTime(notime);
				}
				
				
				// 
				fixTeacherTable(this);
				$('body').attr('data-sel',1).children('.cln-item').remove();
				
				//获取选中元素位置、值、克隆元素为后面移动做准备
				cln_top=parseInt($(this).offset().top);
				cln_left=parseInt($(this).offset().left);
				cln_course=$(this).find('.course').text();
				cln_name=$(this).find('.name').text();
				cln_room=$(this).find('.room').text();
				cln_cnt =$(this).find('.tt').html();  //TODO 
				cln_othercourse=$(this).find('.otherCourse').text();
				cln_othername=$(this).find('.otherName').text();
				cln_otherroom=$(this).find('.otherRoom').text();
				$(this).clone().addClass('cln-item').css({top:cln_top,left:cln_left}).prependTo($('body'));
				
				var inner_subjectId = $(this).find("[name='subjectId']").val();
				var inner_subjectType = $(this).find("[name='subjectType']").val();
				var inner_placeId = $(this).find("[name='placeId']").val();
				var inner_teacherId = $(this).find("[name='teacherId']").val();
				var inner_weekType = $(this).find("[name='weekType']").val();
				
				var inner_subjectId2 = $(this).find("[name='subjectId']:eq(1)").val();
				var inner_subjectType2 = $(this).find("[name='subjectType']:eq(1)").val();
				var inner_placeId2 = $(this).find("[name='placeId']:eq(1)").val();
				var inner_teacherId2 = $(this).find("[name='teacherId']:eq(1)").val();
				var inner_weekType2 = $(this).find("[name='weekType']:eq(1)").val();
				
//				var obj = this;
				//加入课表
				$('#allTable').off('click').on('click','span.item',function(e){
					if(isSaving){
						return;
					}
					//console.log("5");
					if ($(this).find("[name='subjectType']").val() == 3) {
						autoTips("无法操作虚拟课程");
						return;
					}
					if($(this).hasClass("movePeriod")){
						autoTips('部分学生走班时间，此时间禁排');
						return;
					}
					if($(this).hasClass("item-conflict")){
						autoTips('教师有课或禁排时间，不能排课');
						return;
					}
					
					if(!$(this).hasClass('item-lock')){
						var data_sel=parseInt($('body').attr('data-sel'));
						if(data_sel==1){
							if(!$(this).hasClass('item-can')){
								autoTips('同一门课程不能移动');
							}else{
								isSaving = true;
								
								//先检验教师冲突
								var period = $(this).parents("td").attr("data-time");
								
//								if(s_d_week){
//									// 单双周
//									if(!checkTeacherConflict("teacherTable2",period)) return;
//								}else{
//									var the_subjId = $("#teacherTable2 tbody td[data-time='"+period+"'] .item").attr("data-user");
//									if(the_subjId && the_subjId != "0"){
//										autoTips("教师"+ teacherNameMap[inner_teacherId]+"存在冲突");
//										return;
//									}
//								}
								
								var outer_id1 = $(this).find("[name='id']").val();
								var outer_placeId1 = $(this).find("[name='placeId']").val();
								var outer_teacherId1 = $(this).find("[name='teacherId']").val();
								var outer_subjectType1 = $(this).find("[name='subjectType']").val();
								
								var outer_id2 = $(this).find("[name='id']:eq(1)").val();
								var outer_placeId2 = $(this).find("[name='placeId']:eq(1)").val();
								var outer_teacherId2 = $(this).find("[name='teacherId']:eq(1)").val();
								var outer_subjectType2 = $(this).find("[name='subjectType']:eq(1)").val();
								
								var classId = objId;
								var param = "";
								
								var subjId = $(this).attr("data-user");
								if(subjId && subjId!="0"){ 
									param += "&leftSchedules[0].id="+outer_id1;
									param += "&leftSchedules[0].classId="+classId;
									param += "&leftSchedules[0].placeId="+outer_placeId1;
									param += "&leftSchedules[0].teacherId="+outer_teacherId1;
									param += "&leftSchedules[0].subjectType="+outer_subjectType1;
									if($(".int-from").find(".s-d-week").length > 0){
										param += "&leftSchedules[1].id="+outer_id2;
										param += "&leftSchedules[1].placeId="+outer_placeId2;
										param += "&leftSchedules[1].teacherId="+outer_teacherId2;
										param += "&leftSchedules[1].subjectType="+outer_subjectType2;
									}
								}
								
								inner_weekType = inner_weekType?inner_weekType:3;
								inner_weekType2 = inner_weekType2?inner_weekType2:3;
								//debugger;
								param += "&rightSchedules[0].subjectId="+inner_subjectId;
								param += "&rightSchedules[0].subjectType="+inner_subjectType;
								param += "&rightSchedules[0].placeId="+inner_placeId;
								param += "&rightSchedules[0].teacherId="+inner_teacherId;
								param += "&rightSchedules[0].classId="+classId;
								param += "&rightSchedules[0].weekType="+inner_weekType;
								if(s_d_week){
									param += "&rightSchedules[1].subjectId="+inner_subjectId2;
									param += "&rightSchedules[1].subjectType="+inner_subjectType2;
									param += "&rightSchedules[1].placeId="+inner_placeId2;
									param += "&rightSchedules[1].teacherId="+inner_teacherId2;
									param += "&rightSchedules[1].classId="+classId;
									param += "&rightSchedules[1].weekType="+inner_weekType2;
								}
								param += "&period="+period+"&type="+type;
								
								param += "&searchAcadyear="+searchAcadyear;
								param += "&searchSemester="+searchSemester;
								param += "&weekIndex="+weekIndex;
								
								param = param.substr(1);
								
								var arrayId = indexId;
								var url = contextPath + "/"+url_sub_sys+"/scheduleModify/"+arrayId+"/delOrAddLecture";
								var obj = this;
								$.ajax({
									url: url,
									data:param,
									type:"POST",
									dataType:"JSON",
									success:function(data){
									    //console.log("---获取结果");
										if(data.success){
											ioSwap2(obj,e);
//											autoTips("操作成功");
											if(!subjId || subjId == "0")
												updateClassSurplus(classId,-1);
										}else{
											autoTips(""+data.msg);
											isSaving = false;
										}
									}
								});
								return;
								
							};
						};
					}else{
						if($(this).hasClass("movePeriod")){
							autoTips('部分学生走班时间，此时间禁排');
						}else if($(this).hasClass("item-conflict")){
							autoTips('教师有课或禁排时间，不能排课');
						}else{
							autoTips('此时间禁排');
						}
					};
				});
			};
		}else{
			isSaving = true;
			//从外往里排课
			//console.log("------- data-lock = 1");
			
			var outer_id1 = $(".int-from").find("[name='id']").val();
			var outer_placeId1 = $(".int-from").find("[name='placeId']").val();
			var outer_teacherId1 = $(".int-from").find("[name='teacherId']").val();
			
			var outer_id2 = $(".int-from").find("[name='id']:eq(1)").val();
			var outer_placeId2 = $(".int-from").find("[name='placeId']:eq(1)").val();
			var outer_teacherId2 = $(".int-from").find("[name='teacherId']:eq(1)").val();
			
			var inner_subjectId = $(this).find("[name='subjectId']").val();
			var inner_subjectType = $(this).find("[name='subjectType']").val();
			var inner_placeId = $(this).find("[name='placeId']").val();
			var inner_teacherId = $(this).find("[name='teacherId']").val();
			var inner_weekType = $(this).find("[name='weekType']").val();
			inner_weekType = inner_weekType?inner_weekType:3;
			//debugger;
			var inner_subjectId2 = $(this).find("[name='subjectId']:eq(1)").val();
			var inner_subjectType2 = $(this).find("[name='subjectType']:eq(1)").val();
			var inner_placeId2 = $(this).find("[name='placeId']:eq(1)").val();
			var inner_teacherId2 = $(this).find("[name='teacherId']:eq(1)").val();
			var inner_weekType2 = $(this).find("[name='weekType']:eq(1)").val();
			inner_weekType2 = inner_weekType2?inner_weekType2:3;
			
			// 检验教师冲突
			var period = $(".content tbody td .item-sel").parents("td").attr("data-time");
			
//			if($(this).hasClass("s-d-week")){
//				// 单双周
//				if(!checkTeacherConflict("teacherTable1",period)) return;
//			}else{
//				var the_subjId = $("#teacherTable1 tbody td[data-time='"+period+"'] .item").attr("data-user");
//			    //console.log("the_subjId = "+the_subjId);
//				if(the_subjId && the_subjId != "0"){
//					autoTips("教师"+ teacherNameMap[inner_teacherId]+"存在冲突");
//					return;
//				}
//			}
			
			var classId = objId;
			
			var param = "";
			param += "&leftSchedules[0].id="+outer_id1;
			param += "&leftSchedules[0].classId="+classId;
			param += "&leftSchedules[0].placeId="+outer_placeId1;
			param += "&leftSchedules[0].teacherId="+outer_teacherId1;
			if($(".int-from").find(".s-d-week").length > 0){
				param += "&leftSchedules[1].id="+outer_id2;
				param += "&leftSchedules[1].classId="+classId;
				param += "&leftSchedules[1].placeId="+outer_placeId2;
				param += "&leftSchedules[1].teacherId="+outer_teacherId2;
			}
			if(!$(this).hasClass("empty")){ 
				param += "&rightSchedules[0].subjectId="+inner_subjectId;
				param += "&rightSchedules[0].subjectType="+inner_subjectType;
				param += "&rightSchedules[0].placeId="+inner_placeId;
				param += "&rightSchedules[0].teacherId="+inner_teacherId;
				param += "&rightSchedules[0].classId="+classId;
				param += "&rightSchedules[0].weekType="+inner_weekType;
				if($(this).hasClass("s-d-week")){
					param += "&rightSchedules[1].subjectId="+inner_subjectId2;
					param += "&rightSchedules[1].subjectType="+inner_subjectType2;
					param += "&rightSchedules[1].placeId="+inner_placeId2;
					param += "&rightSchedules[1].teacherId="+inner_teacherId2;
					param += "&rightSchedules[1].classId="+classId;
					param += "&rightSchedules[1].weekType="+inner_weekType2;
				}
			}
			param += "&period="+period+"&type="+type;
			
			param += "&searchAcadyear="+searchAcadyear;
			param += "&searchSemester="+searchSemester;
			param += "&weekIndex="+weekIndex;
			if(course_limit) param += "&courseLimit=1";
			if(teacher_limit) param += "&teacherLimit=1";
			param = param.substr(1);
			
			var arrayId = indexId;
			var url = contextPath + "/"+url_sub_sys+"/scheduleModify/"+arrayId+"/delOrAddLecture";
			var obj = this;
			$.ajax({
				url: url,
				data:param,
				type:"POST",
				dataType:"JSON",
				success:function(data){
					if(data.success){
						ioSwap(obj);
						//autoTips("操作成功");
						if($(obj).hasClass("empty")){
							updateClassSurplus(classId,1);
						}
						
					}else{
						autoTips(""+data.msg);
						isSaving = false;
					}
				}
			});
//			return;
			//效果
		};
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
		$('.auto-layer').css({'top':layer_top,'left':layer_left}).stop(true).fadeIn(300).delay(800).fadeOut(300,function(){
			$('.auto-layer-mask').fadeOut(300).remove();
		});
	};
	
	
	$('.sidebar-left ul li').off("click").click(function(e){
		//console.log("7");
		e.preventDefault();
		$(this).addClass('current').siblings('li').removeClass('current');
	});
	
	$('.sidebar-right-wrap .tab li').click(function(){
		//console.log("8");
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
		//console.log("9");
		e.preventDefault();
		var $tr=$(this).parents('tr');
		//取消
		$('.tab-item table tr').removeClass('open');
		$('.tab-item table tr.inner').remove();
		//增加
		$tr.addClass('open');
		$tr.after('<tr class="inner"><td colspan="4" class="py-5"><p>时间：2016年11月10日 星期五</p><p>班级：14港机运行4班 </p><p>内容：第<span>3</span>节<span>数学课</span>申请<span>姚丽丽</span>老师代课</p><p>备注：<input type="text" class="input-txt" /></p><p class="abtn"><a href="#" class="abtn-send">发起申请</a><a href="#" class="abtn-cancel">取消</a></p></td></tr>');
	});
	//关闭代课详情
	$('.tab-item table').on('click','tr .abtn-cancel,tr .abtn-send',function(e){
		//console.log("10");
		e.preventDefault();
		var $tr=$(this).parents('tr').prev('tr');
		$tr.removeClass('open');
		$(this).parents('tr.inner').remove();
	});
	//关闭代课详情+发起申请
	$('.tab-item table').on('click','tr .abtn-send',function(e){
		//console.log("11");
		e.preventDefault();
		alert('申请操作');
	});
	
	//班级查找定位
	//查找
//	var filter_cnt1 = $('.sidebar-left .sidebar-filter .list:eq(0)').html();
//	var filter_cnt2 = $('.sidebar-left .sidebar-filter .list:eq(1)').html();
	function getFilterCnt(index){
		return filter_cnt[index];
	}
	var f = false;
	$('.sidebar-filter .txt').off('focus keyup').bind('focus keyup',function(e){
		if(f)
			return;
		f = true;
		var $list=$(this).parents('.sidebar-filter').find('.list');
		var index=$(this).parents('.sidebar-filter').parent().index();
		var $searchCnt = $(getFilterCnt(index));
		var key =$(this).val()+'';
	    //console.log('$filter_cnt  ='+getFilterCnt(index)+' index = '+index +' searchcnt = '+$searchCnt.length);
		if(key){
			$list.html('');
			//console.log('key = '+key);
			$searchCnt.each(function(i,obj){
				var t = $(obj).text()+'';
			    //console.log('t= ' +t);
				if(t && t.indexOf(key)>-1)
					$list.append(obj);
			});
		}
		//输入显示隐藏
		if($(this).val().length){
			$list.show();
		}else{
			$list.hide();
		};
		//关闭
		$(document).off("click").click(function(event){
			//console.log("12");
			var eo=$(event.target);
			if($list.is(':visible') && !eo.parents('.sidebar-filter').length){
				$list.hide();
				$(document).unbind(event);
			};
		});
		f=false;
	});
	//定位
	$('.sidebar-filter .list').off('click').on('click','a',function(e){
		//console.log("13");
		e.preventDefault();
		var data_class=$(this).attr('data-class');
		var index=$('.sidebar-left li[data-class='+data_class+']').index();
		var pos_top=index*40;
		$('.sidebar-filter .list').hide();
		$('.sidebar-left ul').scrollTop(pos_top);
		//$('.sidebar-left li[data-class='+data_class+']').addClass('current').siblings('li').removeClass();
		$('.sidebar-left li[data-class='+data_class+'] a').click();
	});
	
	function sleep(ms) {
	    for(var t = Date.now();Date.now() - t <= ms;);
	}
});

//单双周时  显示其中一个老师的课表
function insScheduleOnly(id,schedules){
	var $table = $("#"+id);
	$table.find("tbody .item").each(function(){
		$(this).html('');
		$(this).attr('data-user','0');
	});
	
	for(i in schedules){
		var x = schedules[i];
		var timestr = x.dayOfWeek +"_"+x.periodInterval+"_"+x.period;
		var subjectName = x.subjectName;
		var className = x.className;
		var placeName = x.placeName?x.placeName:"";
		
		var subjectType = x.subjectType;
		var subjectId = x.subjectId;
		var placeId = x.placeId;
		var classId = x.classId;
		// 
		var htmlstr =  '<input type="hidden" name="id" value=""/>\
			<input type="hidden" name="subjectId" value="'+subjectId+'"/>\
			<input type="hidden" name="subjectType" value="'+subjectType+'"/>\
			<input type="hidden" name="placeId" value="'+placeId+'"/>\
			<input type="hidden" name="teacherId" value=""/>\
			<input type="hidden" name="classId" value="'+classId+'"/>\
			<span class="name">'+className+'</span>\
			<span class="course">'+subjectName+'</span>\
			<span class="room">'+placeName+'</span>';
		var data_user = classId+"_"+subjectId;
		var old_data_user = $("#"+id+" tbody [data-time='"+timestr+"'] .item").attr("data-user");
		if(old_data_user && old_data_user!="0"){  // 单双周
			htmlstr = '<span class="odd-even">'+htmlstr+'</span>';
			var oldHtml = $("#"+id+" tbody [data-time='"+timestr+"'] .item").html();
			htmlstr += '<span class="odd-even">'+oldHtml+'</span>';
			$("#"+id+" tbody [data-time='"+timestr+"'] .item").attr("data-user",old_data_user+","+data_user);
		}else{
			//console.log("i = "+i+"  x = "+x);
			$("#"+id+" tbody [data-time='"+timestr+"'] .item").attr("data-user",data_user);
		}
		$("#"+id+" tbody [data-time='"+timestr+"'] .item").html(htmlstr);
	}
	winLoad();
}
