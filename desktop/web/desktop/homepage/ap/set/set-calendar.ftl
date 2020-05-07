<!-- 桌面日历新增时间提醒需要 -->
<script src="${request.contextPath}/static/components/moment/locale/zh-cn.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/js/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet"
      href="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/css/bootstrap-datetimepicker.min.css">

<div class="box box-default">
    <#--<div class="box-header">-->
        <#--<h3 class="box-title">日历</h3>-->
    <#--</div>-->
    <div class="box-body">
        <div class="js-calendar"></div>
    </div>
</div>


<script>
    $(function () {

        function getNotifyTypeTime(type, notifyTime) {
            switch (type) {
                case "1": return 5 * 60 * 1000;
                case "3": return 15 * 60 * 1000;
                case "4": return 30 * 60 * 1000;
                case "5": return 60 * 60 * 1000;
                case "6": return 24 * 60 * 60  * 1000;
                case "99": return new Date(notifyTime.replaceAll("-","/")).getTime();
            }
        }

        function getNowMoreHour(hourStep,start, view) {
            var nowStr = view.name === 'month' ? (start.format("YYYY/MM/DD") + " " + moment().format("HH:mm:ss")) : start.format("YYYY/MM/DD HH:mm:ss");
            var now = new Date(nowStr);
            var stepTime = new Date(now.getTime() + (hourStep * 60 * 60 * 1000));
            return view.name === 'month' ? (stepTime.format("yyyy-MM-dd hh") + "00:00") : (stepTime.format("yyyy-MM-dd hh:mm") + ":00")
        }

        function remindTime(){
            $('.js-isNeedTime').on('click', function(){
                if($(this).prop('checked')===true){
                    $(this).parent().next().removeClass('hidden')
                }else{
                    $(this).parent().next().addClass('hidden')
                }
            })

            $('.calendar-remind-option').on('change', function(e){
                if($(this).val() === '99'){
                    $('.calendar-remind-time').removeClass('hidden').val("");
                }else{
                    $('.calendar-remind-time').addClass('hidden').val("");
                }
            })

            $('.calendar-remind-time').datetimepicker4({
                format: 'YYYY-MM-DD HH:mm',
                sideBySide: true,
                locale: moment.locale('zh-cn'),
                dayViewHeaderFormat: 'YYYY MMMM',
                useCurrent: true,
                minDate:moment(moment().format('YYYY-MM-DD HH:mm'))
            }).next().on('click', function(){
                $(this).prev().focus();
            })
        }

        function remoteSave(url, modal) {

            var isOk = deskTopCheckVal(".layer-calendar");
            if ( !isOk ) {
                return false;
            }
            var title = modal.find('input[id=title]').val();
            if (title && title !== '' && getLength(title) > 50) {
                layerError(".layer-calendar #title", "主题 长度过长,不能超过25个汉字或50个字符！")
                return false;
            }
            var start = modal.find('input[id=start]').val();
            var end   = modal.find('input[id=end]').val();
            var className = modal.find('input[type=radio]:checked').val();

            if ( new Date(start.replaceAll("-","/")).getTime() > new Date(end.replaceAll("-","/")).getTime() ) {
                layerError(".layer-calendar #end", "结束时间应大于开始时间");
                return false;
            }

            var isNeedNotify     = modal.find(".js-isNeedTime").is(":checked");
            var systemNotifyType = modal.find(".calendar-remind-option option:selected").attr("value");
            if ( isNeedNotify && (systemNotifyType === "" || systemNotifyType == null) ) {
                layerError(".layer-calendar .calendar-remind-option", "请选择提醒时间");
                return false;
            }
            var stTime = new Date(start.replaceAll("-","/")).getTime();
            var ntTime = getNotifyTypeTime(systemNotifyType, $("#notifyTime").val());
            var noTime = new Date().getTime();
            var isExpired = false;
            if ( systemNotifyType === '99' ) {
                isExpired = ntTime - noTime < 0;
            } else {
                isExpired = stTime - ntTime - noTime < 0;
            }

            if ( isNeedNotify && isExpired ) {
                var nNode = systemNotifyType == "99" ? "input[id=notifyTime]" : ".calendar-remind-option";
                layerError(".layer-calendar " + nNode,  "提醒时间已过期(相对当前时间或主题日程开始时间)");
                return false;
            }

            var cEvent = {
                title: title,
                start: start,
                end: end,
                className: className,
                allDay: 0,
                systemNotifyType: isNeedNotify ? systemNotifyType : "",
                notifyTime: isNeedNotify ? modal.find('input[id=notifyTime]').val() : "",
                id:modal.find('input[id=id]').val()
            }
            $.ajax({
                url: url,
                type: "post",
                data: JSON.stringify(cEvent),
                dataType: 'json',
                contentType: "application/json",
                success: function (data) {
                    if (data.success) {
                        calendar.fullCalendar('refetchEvents');
                        layer.closeAll();
                        layer.msg(data.msg);
                    } else {
                        layer.msg("新增日程失败");
                    }
                },
            });
            return false;
        }

        var calendar = $('.js-calendar').fullCalendar({
            height: $(window).height() - $('.js-calendar').offset().top - 60,
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay',
                timeFormat: 'H:mm'
            },
            monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            firstDay: 0,
            nowIndicator: true,
            scrollTime: moment().format('HH:mm:ss'),
            editable: true,
            selectable: true,
            date: new Date(),
            events: function (start, end, callback) {

                var params = {
                    start: start,
                    end: end,
                };
                var obj = new Object();
                $.ajax({
                    url: "${request.contextPath}/desktop/calendar/findByTime",
                    type: "post",
                    data: JSON.stringify(params),
                    dataType: 'json',
                    contentType: "application/json",
                    success: function (res) {
                        for (i in res.events) {
                            obj.id = res.events[i].id;
                            obj.title = res.events[i].content;
                            obj.className = res.events[i].color;
                            obj.start = res.events[i].beginTime;
                            obj.end = res.events[i].endTime;
                            obj.allDay = false;
                            obj.notifyTime = res.events[i].notifyTime;
                            obj.systemNotifyType = res.events[i].systemNotifyType;
                            if (res.events[i].isAllday == "1") {
                                obj.allDay = true;
                            } else {
                                obj.allDay = false;
                            }
                            calendar.fullCalendar('renderEvent', obj, false);
                        }
                    }
                });
            },
            select: function (start, end, jsEvent, view, allDay) {
                var format = (view.name === 'month') ? "YYYY/MM/DD" : "YYYY/MM/DD HH:mm:ss";
                var enTime = (view.name === 'month') ? moment(moment().format("YYYY-MM-DD")).valueOf() : new Date().getTime();
                if(new Date(start.format(format)).getTime() < enTime){
                    layer.msg('过去的时间不能添加事件', {
                        offset: 't'
                    })
                    calendar.fullCalendar( 'unselect' )
                    return false;
                }
                var modal =
                        '<div class="layer layer-calendar">\
							<div class="layer-content">\
								<div class="">\
									<div class="form-group">\
									    <input type="hidden" id="id" value="">\
										<label class="control-label">选择颜色：</label>\
										<div class="calendar-event-label">\
											<label>\
												<input type="radio" name="calendar-event-label" checked class="wp" value="label-info" />\
												<span class="lbl">\
													<span class="label-info"></span>\
												</span>\
											</label>\
											<label>\
												<input type="radio" name="calendar-event-label" class="wp" value="label-black" />\
												<span class="lbl">\
													<span class="label-black"></span>\
												</span>\
											</label>\
											<label>\
												<input type="radio" name="calendar-event-label" class="wp" value="label-red" />\
												<span class="lbl">\
													<span class="label-red"></span>\
												</span>\
											</label>\
											<label>\
												<input type="radio" name="calendar-event-label" class="wp" value="label-purple" />\
												<span class="lbl">\
													<span class="label-purple"></span>\
												</span>\
											</label>\
										</div>\
									</div>\
									<div class="form-group">\
										<label class="control-label">主题：</label>\
										<input type="text" id="title" nullable="false" msgName="主题" class="form-control" value="" placeholder="请输入事件">\
									</div>\
									<div class="form-group">\
										<label class="control-label">时间：</label>\
										<div class="input-group">\
											<input type="text" nullable="false" msgName="时间" id="start" class="form-control date-picker calendar-time-start" />\
											<span class="input-group-addon"><i class="fa fa-minus"></i></span>\
											<input type="text" nullable="false" msgName="时间" id="end" class="form-control date-picker calendar-time-end" />\
										</div>\
									</div>\
									<div class="form-group">\
										<label class="control-label">\
											<input type="checkbox" class="wp js-isNeedTime" /><span class="lbl"> 是否添加提醒时间</span>\
										</label>\
										<div class="row hidden">\
											<div class="col-sm-6">\
												<select name="" id="" class="form-control calendar-remind-option">\
												    <option value="">请选择提醒时间</option>\
													<option value="1">提前5分钟</option>\
													<option value="3">提前15分钟</option>\
													<option value="4">提前30分钟</option>\
													<option value="5">提前1小时</option>\
													<option value="6">提前1天</option>\
													<option value="99">指定时间</option>\
												</select>\
											</div>\
											<div class="col-sm-6">\
												<input type="text" nullable="false" msgName="提醒时间" id="notifyTime" class="form-control calendar-remind-time hidden" placeholder="自定义提醒时间"/>\
											</div>\
										</div>\
									</div>\
								</div>\
							</div>\
							<div class="layer-footer text-center">\
								<button class="btn btn-blue js-save">保存</button>\
								<button class="btn btn-white js-cancel">取消</button>\
							</div>\
						</div>';
                $('.layer-calendar').remove();
                var modal = $(modal).appendTo('body');
                var index = layer.open({
                    type: 1,
                    shade: .5,
                    title: ['添加事件'],
                    area: '560px',
                    btn: false,
                    content: $('.layer-calendar')
                });

                modal.find('input[id=start]').datetimepicker4({
                    format: 'YYYY-MM-DD HH:mm',
                    sideBySide: true,
                    locale: moment.locale('zh-cn'),
                    dayViewHeaderFormat: 'YYYY MMMM',
                    useCurrent: false,
                    defaultDate: getNowMoreHour(view.name === 'month' ? 1 : 0 , start, view),
                    minDate: moment(moment().format('YYYY-MM-DD HH:mm')),  //提醒时间限制
                    //maxDate: end,

                }).next().on('click', function () {
                    $(this).prev().focus();
                })

                modal.find('input[id=end]').datetimepicker4({
                    format: 'YYYY-MM-DD HH:mm',
                    sideBySide: true,
                    locale: moment.locale('zh-cn'),
                    dayViewHeaderFormat: 'YYYY MMMM',
                    useCurrent: false,
                    defaultDate: getNowMoreHour(view.name === 'month' ? 2 : 0, view.name==='month' ? start : end, view),
                    minDate: moment(moment().format('YYYY-MM-DD HH:mm')),  //提醒时间限制
                }).next().on('click', function () {
                    $(this).prev().focus();
                });
                remindTime();

                modal.find('.js-save').on('click', function (ev) {
                    remoteSave("${request.contextPath}/desktop/calendar/save", modal);
                });
                modal.find('.js-cancel').on('click', function (ev) {
                    layer.close(index);
                });
                calendar.fullCalendar('unselect');
            },
            eventClick: function (calEvent, jsEvent, view) {
                var modal =
                        '<div class="layer layer-calendar">\
							<div class="layer-content">\
								<div class="">\
									<div class="form-group">\
									<input type="hidden" id="id" value="'+calEvent.id+'">\
										<label class="control-label">选择颜色：</label>\
										<div class="calendar-event-label">\
											<label>\
												<input type="radio" name="calendar-event-label" checked class="wp" value="label-info" />\
												<span class="lbl">\
													<span class="label-info"></span>\
												</span>\
											</label>\
											<label>\
												<input type="radio" name="calendar-event-label" class="wp" value="label-black" />\
												<span class="lbl">\
													<span class="label-black"></span>\
												</span>\
											</label>\
											<label>\
												<input type="radio" name="calendar-event-label" class="wp" value="label-red" />\
												<span class="lbl">\
													<span class="label-red"></span>\
												</span>\
											</label>\
											<label>\
												<input type="radio" name="calendar-event-label" class="wp" value="label-purple" />\
												<span class="lbl">\
													<span class="label-purple"></span>\
												</span>\
											</label>\
										</div>\
									</div>\
									<div class="form-group">\
										<label class="control-label">主题：</label>\
										<input type="text" id="title" class="form-control" value="' + calEvent.title + '" placeholder="请输入事件">\
									</div>\
									<div class="form-group">\
										<label class="control-label">时间：</label>\
										<div class="input-group">\
											<input type="text" id="start" class="form-control date-picker calendar-time-start" value="'+ calEvent.start.format("YYYY-MM-DD HH:mm") +'" />\
											<span class="input-group-addon"><i class="fa fa-minus"></i></span>\
											<input type="text" id="end" class="form-control date-picker calendar-time-end" value="'+ calEvent.end.format("YYYY-MM-DD HH:mm")+'" />\
										</div>\
									</div>\
									<div class="form-group">\
										<label class="control-label">\
											<input type="checkbox" class="wp js-isNeedTime" /><span class="lbl"> 是否添加提醒时间</span>\
										</label>\
										<div class="row hidden">\
											<div class="col-sm-6">\
												<select name="" id="" class="form-control calendar-remind-option">\
													<option value="1">提前5分钟</option>\
													<option value="3">提前15分钟</option>\
													<option value="4">提前30分钟</option>\
													<option value="5">提前1小时</option>\
													<option value="6">提前1天</option>\
													<option value="99">指定时间</option>\
												</select>\
											</div>\
											<div class="col-sm-6">\
												<input type="text" id="notifyTime" class="form-control calendar-remind-time hidden" placeholder="自定义提醒时间"/>\
											</div>\
										</div>\
									</div>\
								</div>\
							</div>\
							<div class="layer-footer text-center">\
								<button class="btn btn-blue js-save">保存</button>\
								<button class="btn btn-grey js-delete">删除</button>\
								<button class="btn btn-white js-cancel">取消</button>\
							</div>\
						</div>';
                $('.layer-calendar').remove();
                var modal = $(modal).appendTo('body');
                var index = layer.open({
                    type: 1,
                    shade: .5,
                    title: ['编辑'],
                    area: '560px',
                    btn: false,
                    content: $('.layer-calendar')
                });
                //是否过期（不允许编辑）
                var start = new Date(calEvent.start._i.replaceAll("-","/")).getTime();
                var end   = new Date(calEvent.end._i.replaceAll("-","/")).getTime();
                var isExpire = end < new Date().getTime();
                // 回显选中的颜色
                modal.find('input[type=radio]').each(function (index) {
                    if (isExpire) {
                        $(this).attr("disabled", "true");
                    }
                    if ($(this).val() == calEvent.className) {
                        $(this).prop('checked', true);
                    }
                });

                if( !isExpire ) {
                    $('.date-picker').datetimepicker4({
                        format: 'YYYY-MM-DD HH:mm',
                        sideBySide: true,
                        locale: moment.locale('zh-cn'),
                        dayViewHeaderFormat: 'YYYY MMMM',
                        useCurrent: true,

                        minDate: moment(moment().format('YYYY-MM-DD HH:mm')),  //提醒时间限制
                        //maxDate: calEvent.end
                    }).next().on('click', function () {
                        $(this).prev().focus();
                    })
                }

                modal.find('input[id=start]').val(calEvent.start.format("YYYY-MM-DD HH:mm"))

                remindTime();

                if ( calEvent.notifyTime || calEvent.systemNotifyType ) {
                    modal.find('.js-isNeedTime').click();
                    modal.find('select.calendar-remind-option').find("option[value='"+calEvent.systemNotifyType+"']").attr("selected","true");
                    if ( calEvent.systemNotifyType == 99 ) {
                        modal.find('input[id=notifyTime]').attr("placeholder", calEvent.notifyTime).removeClass("hidden");
                    }
                }

                modal.find('.js-save').on('click', function (ev) {
                    remoteSave("${request.contextPath}/desktop/calendar/update", modal);
                });
                modal.find('.js-delete').on('click', function (ev) {
                    calendar.fullCalendar('removeEvents', function (ev) {
                        return (ev._id == calEvent._id);
                    });

                    var schedule = {
                        id: calEvent.id,
                    }
                    $.ajax({
                        url: "${request.contextPath}/desktop/calendar/delete",
                        type: "post",
                        data: JSON.stringify(schedule),
                        dataType: 'json',
                        contentType: "application/json",
                        success: function (data) {
                            if (data.success) {
                                layer.msg(data.msg);
                            } else {
                                layer.msg("删除失败");
                            }
                        },
                    });
                    layer.close(index);
                });
                modal.find('.js-cancel').on('click', function (ev) {
                    layer.close(index);
                });

                if ( isExpire ) {
                    modal.find('input[id=title]').attr("readonly","readonly");
                    modal.find('input[id=start]').attr("readonly","readonly");
                    modal.find('input[id=end]').attr("readonly","readonly");
                    modal.find('.js-isNeedTime').attr("disabled","true");
                    modal.find('.calendar-remind-option').attr("disabled","true");
                    modal.find('input[id=notifyTime]').attr("readonly","readonly");
                }
            },
            <#-- 拖动日历-->
            eventDrop: function (calEvent, dayDelta, minuteDelta, allDay, revertFunc) {
                var beginTime = new Date(calEvent.start.format("YYYY/MM/DD HH:mm"));
                var endTime   = new Date(calEvent.end.format("YYYY/MM/DD HH:mm"));

                if ( endTime.getTime() < new Date().getTime() ) {
                    layer.msg("日程将改为过期日程，更新失败！");
                    calendar.fullCalendar('refetchEvents');
                    return ;
                }

                var notify = true;
                var update = true;
                if ( endTime.getTime() > new Date().getTime() && beginTime.getTime() < new Date().getTime() ) {
                    notify = false;
                    update = false;
                    showWarnMsg("日程开始时间将改为过去时间，我们将不会通知您该日程");
                }
                if (update) {
                    if ( calEvent.notifyTime ) {
                        var notifyTime = new Date(calEvent.notifyTime.replace("-", "/").replace("-", "/"));
                        notifyTime.setDate(notifyTime.getDate() + dayDelta._days);
                        calEvent.notifyTime = notifyTime.format("yyyy-MM-dd hh:mm");
                    }
                    calendar.fullCalendar('updateEvent', calEvent);
                    var isAllday;
                    if (calEvent.allDay) {
                        isAllday = '1';
                    } else {
                        isAllday = '0';
                    }
                    var calEvent = {
                        title: calEvent.title,
                        start: calEvent.start,
                        end: calEvent.end,
                        allDay: isAllday,
                        id: calEvent.id,
                        notifyTime: notify ? calEvent.notifyTime : "",
                        systemNotifyType: calEvent.systemNotifyType
                    }
                    $.ajax({
                        url: "${request.contextPath}/desktop/calendar/dragUpdate",
                        type: "post",
                        data: JSON.stringify(calEvent),
                        dataType: 'json',
                        contentType: "application/json",
                        success: function (data) {
                            if (data.success) {
                                calendar.fullCalendar('refetchEvents');
                                layer.closeAll();
                                layer.msg(data.msg);
                            } else {
                                layer.msg("更新失败");
                            }
                        },
                    });
                }
            },
            <#--下拉修改日历时间-->
            eventResize: function (calEvent, delta, revertFunc) {

                if ( new Date(calEvent.end.format("YYYY/MM/DD HH:mm")).getTime() < new Date().getTime() ) {
                    layer.msg("该日程信息已经过期无法修改");
                    return ;
                }

                var isAllday = '0';
                var calEvent = {
                    title: calEvent.title,
                    start: calEvent.start,
                    end: calEvent.end,
                    allDay: isAllday,
                    id: calEvent.id,
                    notifyTime: calEvent.notifyTime,
                    systemNotifyType: calEvent.systemNotifyType
                }
                $.ajax({
                    url: "${request.contextPath}/desktop/calendar/dragUpdate",
                    type: "post",
                    data: JSON.stringify(calEvent),
                    dataType: 'json',
                    contentType: "application/json",
                    success: function (data) {
                        if (data.success) {
                            calendar.fullCalendar('refetchEvents');
                            layer.closeAll();
                            layer.msg(data.msg);
                        } else {
                            layer.msg("更新失败");
                        }
                    },
                });
            },

            dayClick: function(date, jsEvent, view) {

                var currentDate = moment(moment().format('YYYY-MM-DD')).valueOf();
                if(date < currentDate){
                    calendar.fullCalendar('select', date);
                    return false
                }
            },
            //处理班休标识
            viewRender: function(view, element){

                if(view.name !== 'month'){
                    return ;
                }
                $.ajax({
                    url : "${request.contextPath}/desktop/calendar/restDay",
                    data:{beginTime:view.start.format("YYYY-MM-DD"),endTime:view.end.format("YYYY-MM-DD")},
                    type : "GET",
                    dataType:"json",
                    contentType:"application/json",
                    success:function (data) {
                        var dataInfos = data;
                        element.find("div.fc-bg").each(function (e) {
                            $(this).find("td.fc-day").each(function () {
                                var day = $(this).attr("data-date");
                                var isSun = $(this).hasClass("fc-sun") || $(this).hasClass("fc-sat");
                                if ( "true" === dataInfos[day] && !isSun) {
                                    $(this).addClass("fc-rest");
                                }
                                else if( "false" === dataInfos[day] && isSun) {
                                    $(this).addClass("fc-duty");
                                }
                                else if( "true" === dataInfos[day] && isSun) {
                                    $(this).addClass("fc-rest");
                                }
                                else if ("undefined"== typeof dataInfos[day] && isSun) {
                                    $(this).addClass("fc-rest");
                                }
                                else if ("undefined"== typeof dataInfos[day]) {
                                    $(this).addClass("fc-rest");
                                }
                            })
                        })
                    }
                })

            },
            dayRender:function (date, cell) {

            }
        });
    })
</script>

