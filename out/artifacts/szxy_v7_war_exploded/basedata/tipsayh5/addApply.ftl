<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title><#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>管理</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.picker.min.css">
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body>
	<#--
	    <header class="mui-bar mui-bar-nav" style="height:0px;display:none;">
	        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"></a>
	        <h1 class="mui-title">申请代课</h1>
	    </header>
    -->
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none;"></a>
    <#--申请信息---applyContent-->		
    <div class="mui-content add-form applyContent" style="padding-bottom: 51px;">
    	<form id="applyForm">
    	<ul class="mui-table-view  applyschedule">
    		<#if applyType?default('1')!='3'>
	            <li class="mui-table-view-cell">
	            	<input type="hidden" name="teacherId" id="oldTeacherId" value="${teacherId!}">
	            	<span class="f-16 f-left">原任课教师</span>
	            	<span class="f-16 f-right" id="oldTeacherName">${teacherName!}</span>
	            </li>
	         <#else>
	         	<li class="mui-table-view-cell">
	         		<input type="hidden" name="teacherId" id="oldTeacherId" value="">
	            	<a class="mui-navigate-right addTeacherId">
	            		<span class="f-left">原任课教师</span>
	            		<span class="f-right mr-30" id="oldTeacherName"></span>
	            	</a>
	            </li>
            </#if>
            <li class="mui-table-view-cell radio_type">
            	<span class="f-16 f-left">时间类型</span>    	    
	        	<#--<form class="f-16 f-right">-->
	        		<div class="mui-radio mui-left f-left">
	        		    <label style="padding-right: 0;">按周次</label>
	        		    <input name="radio" type="radio" value="1" style="top: -4px;" checked>
	        		</div>
	        		<div class="mui-radio mui-left f-left">
	        		    <label style="padding-right: 0;">自定义时间</label>
	        		    <input name="radio" type="radio" style="top: -4px;" value="2">
	        		</div>
	        	<#--</form>-->
            </li>
        	<li class="mui-table-view-cell radio_type_2" style="display:none;">
                <a class="mui-navigate-right date" >
                	<span class="f-left">起始时间</span>
                	<span class="result result_start f-right mr-30"></span>
                </a>
            </li>
            <li class="mui-table-view-cell radio_type_2" style="display:none;">
                <a class="mui-navigate-right date">
                	<span class="f-left">截止时间</span>
                	<span class="result result_end f-right mr-30"></span>
                	</a>
            </li>
			<li class="mui-table-view-cell">
            	<span class="f-16 f-left">节次</span>
            	<span class="f-16 f-right addCourseList"><i class="c-blue fa fa-plus-circle f-24 mr-5 f-left"></i>添加</span>
            </li> 
            
    	</ul>
    	<#if applyType?default('1')!='1'>
    	<ul class="mui-table-view">
            <li class="mui-table-view-cell">
            	<input type="hidden" name="newTeacherId"  id="newTeacherId" value="">
                <a class="mui-navigate-right addNewTeacherId">
                	<span class="f-left"><#if type?default('')=='1'>代<#elseif type?default('')=='2'>管</#if>课教师</span>
	            	<span class="f-right mr-30" id="newTeacherName"></span>
         		</a>
            </li>
        </ul>
        </#if>
    	<ul class="mui-table-view">
            <li class="mui-table-view-cell">
            	<span class="f-16">备注</span>
            </li>
            <li class="mui-table-view-cell">
            	<textarea name="remark" rows="" cols="" placeholder="请输入" class="f-14" maxLength="100" style="border: none;margin: 0;"></textarea>
            </li>
        </ul>
      </form>
    </div>
    <nav class="mui-bar mui-bar-tab applyContent">
        <!--<a class="mui-tab-item f-16" href="#" style="background: #bbbcbc;color: #fff;">确定</a>-->
        <a class="mui-tab-item f-16 applySubmit" href="javascript:" style="background: #2f7bff;color: #fff;">确定</a>
    </nav>
    	
    <#---课程表-applyCourseSchedult-->
    <div class="tabMenu01 tab_menu mui-bg-white applyCourseSchedult" style="position:fixed;z-index:10;left:0px;right:0px; top: 0;display:none;">
        <ul class="fn-flex" id="weekList">
            <li class="mainlevel fn-flex-auto active"><a class="mainlevel_a" href="#">第1周</a></li>
        </ul>
    </div>
	<div class="mui-content mui-bg-white applyCourseSchedult" style="padding-top:44px;padding-bottom: 51px; display:none;">
    	<div class="tabBox01 tab_box">
	        <div>
	        	<div class="ml-15 mr-15">
	        		<div class="mt-10 mb-10 c-666">任课教师：<span id="scheTeacherName"></span></div>
	        		<table class="course-table">
		        		<tr>
		        			<th width="5%"></th>
		        			<th width="5%"></th>
		        			<#if weekDayList?exists && weekDayList?size gt 0>
		        			<#assign s=90/(weekDayList?size)>
							<#list weekDayList as weekDay>
								<th width="${s?floor}%">${weekDay[1]!}</th>
							</#list>
							</#if>
		        		</tr>
		        		<#if mm gt 0>
							<#list 1..mm as ii>
								
								<tr>
									<#if ii_index==0>
									<td rowspan="${mm}">早自习</td>
					            	</#if>
		        					<td>${ii}</td>
									<#if weekDayList?exists && weekDayList?size gt 0>
									<#list weekDayList as weekDay>
										<td class="edit weekDay_${weekDay[0]!} period_${weekDay[0]!}_1_${ii}" data-value="${weekDay[0]!}_1_${ii}" data-name="${weekDay[1]!}/早自习/第${ii}节" data-schedule=""></td>
									</#list>
									</#if>
								</tr>	
							</#list>
						</#if>
						<#if am gt 0>
							<#list 1..am as ii>
								<tr>
									<#if ii_index==0>
									<td rowspan="${am}">上午</td>
									</#if>
									<td>${ii}</td>
									<#if weekDayList?exists && weekDayList?size gt 0>
									<#list weekDayList as weekDay>
										<td class="edit weekDay_${weekDay[0]!} period_${weekDay[0]!}_2_${ii}" data-value="${weekDay[0]!}_2_${ii}" data-name="${weekDay[1]!}/上午/第${ii}节" data-schedule=""></td>
									</#list>
									</#if>
								</tr>	
							</#list>
						</#if>
						<#if pm gt 0>
							<#list 1..pm as ii>
								
								<tr>
									<#if ii_index==0>
									<td rowspan="${pm}">下午</td>
									</#if>
									<td>${ii}</td>
									<#if weekDayList?exists && weekDayList?size gt 0>
									<#list weekDayList as weekDay>
										<td class="edit weekDay_${weekDay[0]!} period_${weekDay[0]!}_3_${ii}" data-value="${weekDay[0]!}_3_${ii}" data-name="${weekDay[1]!}/下午/第${ii}节" data-schedule=""></td>
									</#list>
									</#if>
								</tr>	
							</#list>
						</#if>
						<#if nm gt 0>
							<#list 1..nm as ii>
								<tr>
									<#if ii_index==0>
									<td rowspan="${nm}">晚上</td>
									</#if>
									<td>${ii}</td>
									<#if weekDayList?exists && weekDayList?size gt 0>
									<#list weekDayList as weekDay>
										<td class="edit weekDay_${weekDay[0]!} period_${weekDay[0]!}_4_${ii}" data-value="${weekDay[0]!}_4_${ii}" data-name="${weekDay[1]!}/晚上/第${ii}节" data-schedule=""></td>
									</#list>
									</#if>
								</tr>	
							</#list>
						</#if>
		        	</table>
	        	</div>
	        </div>
	    </div>
    </div>
    <nav class="mui-bar mui-bar-tab fn-flex applyCourseSchedult" style="line-height: 50px;display:none;">
    	<span class="fn-flex-auto mui-checkbox mui-left f-16">	
		    <label>全选</label>
		    <input name="checkbox" type="checkbox" style="top: 10px;">
    	</span>
        <a class="mui-tab-item f-16 fn-flex-auto" id="chooseSubmit" href="javascript:" style="background: #2f7bff;color: #fff;">确定<span id="chooseNum">(0)</span></a>
    </nav>
    
    <#---老师列表-teacherDiv-->
    <#if applyType?default('1')!='1'>
    <div class="search-container teacherDiv" style="display:none;">
    	<input type="hidden" id="opopId" value="">
    	<div class="mui-content-padded">
	    	<div class="mui-input-row mui-search">
	    	    <input type="search" id="searchTeacherName" class="mui-input-clear" placeholder="请输入教师名称">
	    	</div>
	    </div>
    </div>
    <div class="mui-content teacherContent teacherDiv" style="padding-top: 40px;display:none;">
    	<div class="mui-page-noData"> 
	    	 <i></i>
	    	 <p class="f-16">未搜到结果</p>
	    </div>
    </div>
    </#if>
    
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/jquery.form.js"></script>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${request.contextPath}/static/mui/js/mui.picker.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
    <script type="text/javascript">
    mui.init();
	$('#cancelId').off('click').on('click',function(){
     	if((!$(".applyContent").is(":hidden") )){
     		var parm='&acadyear=${acadyear!}&semester=${semester!}&week=${week!}&statType=${statType!}';
			var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}'+parm;
			loadByHref(url);
     	}else{
     		$(".applyContent").show();
			$(".teacherDiv").hide();
			$(".applyCourseSchedult").hide();
     	}
	});
    
    (function($) {
		$.init();
		var results = $('.result');
		var btns = $('.date');
		btns.each(function(i, btn) {
			btn.addEventListener('tap', function() {
				var options={
					"type":"date",
					"beginYear":${startTimeStr[0]!},
					"beginMonth":${startTimeStr[1]!},
					"beginDay":${startTimeStr[2]!},
					"endYear":${endTimeStr[0]!},
					"endMonth":${endTimeStr[1]!},
					"endDay":${endTimeStr[2]!}
				}
				
				var id = this.getAttribute('id');
				var result=results[i];
				/*
				 * 首次显示时实例化组件
				 * 示例为了简洁，将 options 放在了按钮的 dom 上
				 * 也可以直接通过代码声明 optinos 用于实例化 DtPicker
				 */
				var picker = new $.DtPicker(options);
				picker.show(function(rs) {
					/*
					 * rs.value 拼合后的 value
					 * rs.text 拼合后的 text
					 * rs.y 年，可以通过 rs.y.vaue 和 rs.y.text 获取值和文本
					 * rs.m 月，用法同年
					 * rs.d 日，用法同年
					 * rs.h 时，用法同年
					 * rs.i 分（minutes 的第二个字母），用法同年
					 */
					result.innerText =  rs.text;
					/* 
					 * 返回 false 可以阻止选择框的关闭
					 * return false;
					 */
					/*
					 * 释放组件资源，释放后将将不能再操作组件
					 * 通常情况下，不需要示放组件，new DtPicker(options) 后，可以一直使用。
					 * 当前示例，因为内容较多，如不进行资原释放，在某些设备上会较慢。
					 * 所以每次用完便立即调用 dispose 进行释放，下次用时再创建新实例。
					 */
					picker.dispose();
				});
			}, false);
		});
	})(mui);
	
	
	
	<#---切换时间自定义-->
	var oldRadioValue="";
	$(".radio_type input[name='radio']").click(function(){
		if($(this).val()!=oldRadioValue){
    		if($(this).val()=='2'){
    			$(".radio_type_2").show();
    		}else{
    			$(".radio_type_2").hide();
    			$(".radio_type_2").find("span").text('');
    		}
    		oldRadioValue=$(this).val();
    	}
    	
    });
	<#---加载教师课表 隐藏申请显示课表-->
    var oldWeek="";
    var startWeekDay="";
    $(".addCourseList").click(function(){
    	var oldTeacherId=$("#oldTeacherId").val();
    	if(oldTeacherId==""){
    		toastMsg("请先选择原任课老师");
			return;
    	}
   		var searchType = $("input[type='radio']:checked").val();
		var startTime=$(".result_start").html();
		var endTime=$(".result_end").html();
    	if(searchType=="2"){
    		if(startTime=="" || endTime==""){
    			toastMsg("请先选择时间范围");
				return;
    		}
    	}
		oldWeek="";
		startWeekDay="";
		$("#scheTeacherName").html($("#oldTeacherName").html());
		
		loadWeekList(oldTeacherId,searchType,startTime,endTime);
	});
    
    function loadWeekList(chooseTeacherId,searchType,startTime,endTime){
    	$.ajax({
			url:'${request.contextPath}/mobile/open/adjusttipsay/findWeekList',
			data:{'unitId':'${unitId!}','acadyear':'${acadyear!}','semester':'${semester!}','searchType':searchType,'startTime':startTime,'endTime':endTime},
			type:'post', 
			dataType:'json',
			success:function(data){
				if(data.success){
					var weekList=data.weekList;
					startWeekDay=data.startWeekDay;
					var text="";
					var startWeek="";
					$("#weekList").html("");
					for(var mm in weekList){
						if(mm==0){
							startWeek=weekList[mm];
							text=text+'<li class="mainlevel fn-flex-auto active " data-value="'+weekList[mm]+'"><a class="mainlevel_a" href="#">第'+weekList[mm]+'周</a></li>';
						}else{
							text=text+'<li class="mainlevel fn-flex-auto" data-value="'+weekList[mm]+'"><a class="mainlevel_a" href="#">第'+weekList[mm]+'周</a></li>';
						}
					}
					
					$("#weekList").html(text);
					<#---隐藏申请显示课表-->
					$(".applyContent").hide();
					$(".applyCourseSchedult").find("table .edit").html("");
					$(".applyCourseSchedult").show();
					loadCourseSchedule(chooseTeacherId,startWeek);
				}else{
					toastMsg(data.msg);
				}

			}
		});		
    }
    
   
    <#--切换周次-->
    var $div_li1=$(".tab_menu > ul");
    $div_li1.on("click","li", function(){
    	$(this).addClass("active").siblings().removeClass("active");
    	var oldTeacherId=$("#oldTeacherId").val();
    	var week=$(this).attr("data-value");
    	if(oldWeek!=week){
    		loadCourseSchedule(oldTeacherId,week);
    	}
    })
    <#--取课表值-->
    function loadCourseSchedule(chooseTeacherId,week){
    	oldWeek=week;
    	$(".edit").removeClass("replace");
    	$(".edit").removeClass("active");
    	$(".edit").html("");
    	$(".edit").attr("data-schedule","");
    	clearTable(week);
    	$.ajax({
			url:'${request.contextPath}/mobile/open/adjusttipsay/schedultByTeacherId',
			data:{'unitId':'${unitId!}','acadyear':'${acadyear!}','semester':'${semester!}','teacherId':chooseTeacherId,'week':week},
			type:'post', 
			dataType:'json',
			success:function(data){
				var length=$(".schedule_"+week).length;
				var scheduleMap={};
				if(length>0){
					$(".schedule_"+week).each(function(){
						var scheduleId=$(this).attr("data-schedule");
						scheduleMap[scheduleId]=scheduleId;
					})
				}
				if(data.success){
					var chooseNum=0;
					var courseScheduleList=data.courseScheduleList;
					for(var yy in courseScheduleList){
						var schedule=courseScheduleList[yy];
						var key=schedule.dayOfWeek+'_'+schedule.periodInterval+'_'+schedule.period;
						if(schedule.className){
							$('.period_'+key).text(schedule.className);
							$('.period_'+key).attr("data-schedule",schedule.id);
							if(scheduleMap[schedule.id]){
								$('.period_'+key).addClass("active");
								chooseNum++;
							}
							
						}
			    	}
   					$("#chooseNum").html("("+chooseNum+")");
				}
			}
		});	
    }
    <#--清除课表原来选中状态-->
    function clearTable(week){
    	if(startWeekDay!="" && startWeekDay.indexOf(week+"_")>-1){
    		var arr=startWeekDay.split("_");
    		var weekday=parseInt(arr[1])-1;
    		var ii=0;
    		while(true){
    			if(ii<weekday){
    				$(".weekDay_"+ii).addClass("replace");
    				ii++;
    			}else{
    				break;
    			}
    		}
    	}
    }
    
   //td事件
   <#--课表td事件-->
   $(".applyCourseSchedult .edit").click(function(){
   		if($(this).hasClass("replace")){
   			toastMsg("不能操作");
   			return;
   		}
   		if($(this).hasClass("active")){
   			$(this).removeClass("active");
   		}else{
   			if($(this).attr("data-schedule")!=""){
   				$(this).addClass("active");
   			}else{
   				toastMsg("不能操作");
   				return;
   			}
   		}
   		var chooseNum=$(".applyCourseSchedult table .active").length;
   		$("#chooseNum").html("("+chooseNum+")");
   })
   
   <#--申请信息中 移除选中节次-->
   $(".applyContent").on("click",".deleteSchedule",function(){
   		$(this).parent().remove();
   })
   <#--申请信息中 增加某个周次选中节次-->
   function addScheduleLi(week,dataList){
   		$(".schedule_"+week).remove();
   		if(dataList){
   			var text="";
   			for(var s in dataList){
   				text=text+'<li class="mui-table-view-cell chooseSchedule schedule_'+week+' " data-schedule="'+dataList[s].id+'"><input type="hidden" name="courseScheduleId" value="'+dataList[s].id+'" >'
   				+'<i class="c-red fa fa-minus-circle f-22 mr-5 f-left deleteSchedule"></i>第'+week+'周'+dataList[s].timeStr
   				+'<span class="c-999"> - '+dataList[s].className+'</span></li> ';
   			}
   			$(".applyContent ul.applyschedule").append(text);
   		}
   }
   <#--课程表中确定选中节次 隐藏课表 显示申请-->
   $("#chooseSubmit").on('tap',function(){
   		var dataList=new Array();
   		var ss=0;
   		$(".applyCourseSchedult .edit").each(function(){
   			if($(this).hasClass("active")){
   				var id=$(this).attr("data-schedule");
   				var timeStr=$(this).attr("data-name");
   				var className=$(this).html();
   				if(id!=""){
   					var object = {'id':id,'timeStr': timeStr, 'className': className};
	   				dataList[ss]=object;
	   				ss++;
   				}
   			}
   		})
   		var week=$("#weekList li.active").attr("data-value");
   		addScheduleLi(week,dataList);
   		$(".applyCourseSchedult").hide();
   		$(".applyContent").show();
   })
   <#--课程表 全选-->
	$("input[name='checkbox']").change(function() { 
		if($(this).is(':checked')){
			//原来选中
			$(".applyCourseSchedult .edit").each(function(){
				if(!$(this).hasClass("replace")){
		   			if(!$(this).hasClass("active")){
		   				if($(this).attr("data-schedule")!=""){
		   					$(this).addClass("active");
			   			}
		   			}
		   		}
			})
		}else{
			$(".applyCourseSchedult .edit").removeClass("active");
		}
		var chooseNum=$(".applyCourseSchedult table .active").length;
   		$("#chooseNum").html("("+chooseNum+")");
	});
	<#--申请提交-->
	var submitFlsg=false
	$(".applySubmit").on("tap",function(){
		if(submitFlsg){
			return;
		}
		submitFlsg=true;
		var length=$(".chooseSchedule").length;
		if(length==0){
			toastMsg("请选择需要<#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>的节次");
			submitFlsg=false;
			return;
		}
		var options = {
			url : "${request.contextPath}/mobile/open/adjusttipsay/saveAllTipsay",
			data:{'unitId':'${unitId!}','acadyear':'${acadyear!}','semester':'${semester!}','operateuser':'${teacherId!}','applyType':'${applyType!}','type':'${type!}'},
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			submitFlsg=false;
		 			toastMsg(data.msg);
		 		}else{
		 			var parm='&acadyear=${acadyear!}&semester=${semester!}&week=${week!}';
					var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}';
					loadByHref(url);
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#applyForm").ajaxSubmit(options);
	})
	var oldChooseTeacherId="";
	<#if applyType?default('1')!='1'>
		<#--教师列表选中某个老师-->
		$(".teacherDiv").on("click",".teacher-cell",function(){
			var tId=$(this).attr("data-value");
			var tName=$(this).attr("data-name");
			var opopType=$("#opopId").val();
			if(opopType=="old"){
				if(oldChooseTeacherId!=tId){
					$("#oldTeacherId").val(tId);
					$("#oldTeacherName").html(tName);
					oldChooseTeacherId=tId;
					//清空原来选中的节次
					$(".chooseSchedule").remove();
				}
			}else{
				if($(this).hasClass("c-red")){
					toastMsg("冲突数据");
					return;
				}
				$("#newTeacherId").val(tId);
				$("#newTeacherName").html(tName);
			}
			
			$(".teacherDiv").hide();
			$(".applyContent").show();
			
		})
		<#--加载代课老师-->
		$(".addNewTeacherId").click(function(){
			$("#searchTeacherName").val("");
			$(".applyContent").hide();
			$("#opopId").val("new");
			$(".teacherDiv").show();
			var teacherId=$("#oldTeacherId").val();
			searchTeacherList(teacherId);
		})
		//教师列表
		function searchTeacherList(teacherId){
			var length=$(".chooseSchedule").length;
			var scheduleIds="";
			if(length>0){
				$(".chooseSchedule").each(function(){
					var id=$(this).find("input[name='courseScheduleId']").val();
					scheduleIds=scheduleIds+","+id;
				})
				scheduleIds=scheduleIds.substring(1);
			}
			var searchTeacherName=$("#searchTeacherName").val();
			$.ajax({
				url:'${request.contextPath}/mobile/open/adjusttipsay/findTeacherList',
				data:{'unitId':'${unitId!}','acadyear':'${acadyear!}','semester':'${semester!}','teacherId':teacherId,'scheduleIds':scheduleIds,'searchTeacherName':searchTeacherName},
				type:'post', 
				dataType:'json',
				success:function(data){
					$(".teacherContent").html("");
					if(data.success){
						var groupDtoList=data.groupDtoList;
						addTeacherGroup(groupDtoList);
					}else{
						$(".teacherContent").html('<div class="mui-page-noData"><i></i><p class="f-16">未搜到结果</p></div>');
					}
					
				}
			});	
		}
		
		
		function addTeacherGroup(groupDtoList){
			var text='<ul class="mui-table-view choose-teacher">';
			for(var mm in groupDtoList){
				var groupDto=groupDtoList[mm];
				text=text+' <li class="mui-table-view-cell mui-collapse">'
					+'<a class="mui-navigate-right" href="#">'+groupDto.teachGroupName+'</a><ul class="mui-table-view">';
				for(var kk in groupDto.showList){
					var dto=groupDto.showList[kk];
					if(dto[2]=='1'){
						text=text+'<li class="mui-table-view-cell teacher-cell c-red" data-value="'+dto[0]+'" data-name="'+dto[1]+'">'+dto[1]+'<span class="f-right">存在冲突</span>';
					}else{
						text=text+'<li class="mui-table-view-cell teacher-cell " data-value="'+dto[0]+'" data-name="'+dto[1]+'">'+dto[1];
					}
					text=text+'</li>';
				}
				text=text+'</ul></li>';
			}
			text=text+'</ul>';
			$(".teacherContent").html(text);
		}
		
		$('#searchTeacherName').bind('search',function(){
			searchTeacherList();
		})
	</#if>
	
	<#if applyType?default('1')=='3'>
		$(".addTeacherId").click(function(){
			$("#searchTeacherName").val("");
			 $(".applyContent").hide();
			 $("#opopId").val("old");
			 $(".teacherDiv").show();
			 searchTeacherList("");
		})
	</#if>
    </script>
</body>
</html>
