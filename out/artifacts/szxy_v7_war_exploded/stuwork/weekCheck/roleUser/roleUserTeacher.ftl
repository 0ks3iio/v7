<#import "/fw/macro/popupMacro.ftl" as popup />
			<div class="box box-default">
				<div class="box-body">
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('01')">总管理员</a></li>
						<li class="active" role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('02')">值周干部</a></li>
						<li role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('03')">值周班</a></li>
						<li role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('04')">学生处</a></li>
						<li role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('05')">保卫处</a></li>
						<li role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('06')">年级组</a></li>
						<li role="presentation"><a href="#" role="tab" data-toggle="tab" onclick="changeRoleType('07')">体育老师</a></li>
						
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" role="tabpanel">
							<div class="js-calendar"></div>
							<form id="" class="print">
								<div class="table-container-body"  style="display:none">
									<table class="table table-striped">
										<thead>
											<tr>
												<th>日期</th>
												<th>周次</th>
												<th>星期</th>
												<#if sections?exists && sections?size gt 0>
												<#list sections as sec>
												<th>${mcodeSetting.getMcode("DM-RKXD","${sec?default('0')}")}</th>
												</#list>
												</#if>
											</tr>
										</thead>
										<tbody>
									<#if dateInfoList?exists && dateInfoList?size gt 0>
										<#list dateInfoList as info>
											<tr>
												<td>${info.infoDate?string('yyyy-MM-dd')!}</td>
												<td>第${info.week!}周</td>
												<td>${info.weekday!}</td>
												<#if sections?exists && sections?size gt 0>
												<#list sections as sec>
												<td>
													<#if roles?exists && roles?size gt 0>
														<#list roles as ro>
															<#if ro.section== sec && ro.dutyDate?string('yyyy-MM-dd') == info.infoDate?string('yyyy-MM-dd')>
																${ro.roleName!}
															</#if>
														</#list>
													</#if>
												</td>
												</#list>
												</#if>
											</tr>
											</#list>
											</#if>
										</tbody>
									</table>
								</div>
								</form>
						</div>
					</div>
				</div>
			</div>
			<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script type="text/javascript">
function changeRoleType(type){
	if(type == '02'){
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUserTeacher/page?roleType=02");
	}else{
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUser/page?roleType="+type);
	}
}
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	LODOP.SAVE_TO_FILE("值周干部"+getNowFormatDate()+".xls");
}
	$(function(){
		var calendar=$('.js-calendar').fullCalendar({
		/**
			//weekends:false,//是否显示星期六和星期天
			dayClick: function() {
		        var moment = $('.js-calendar').fullCalendar('getDate');
		    },
		    
		    validRange: {
		        start: '2017-08',
		        end: '2017-09'
		    },
		    name: 'month',
		    visibleRange: {
		        start: '2017-08-22',
		        end: '2017-09-25'
		    }, **/
		     defaultView: 'month',
		    weekMode:'fixed',//固定显示6行
		    firstDay:0,//以周日为起点
			customButtons: {
		        importExcel: {
		            text: 'Excel导入',
		            click: function() {
		            	$(".model-div").load("${request.contextPath}/stuwork/roleTeacher/main");
		            }
		        },
		        exportExcel: {
		            text: '导出',
		            click: function() {
		                doExport();
		            }
		        }
		    },
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'importExcel, exportExcel'
			},
			monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
			events:[
				<#if roles?exists && roles?size gt 0>
				<#list roles as ro>
					{
						title:'${mcodeSetting.getMcode("DM-RKXD","${ro.section?default('0')}")}--${ro.roleName!}',
						start:'${ro.dutyDate?string('yyyy-MM-dd')}'
					}
					<#if ro_index lt roles?size - 1>
					,
					</#if>
				</#list>
				</#if>
			],
			editable: false,//事件是否可编辑，可编辑是指可以移动, 改变大小等。
			selectable: true,//是否允许用户通过单击或拖动选择日历中的对象，包括天和时间。
			selectHelper: true,//当点击或拖动选择时间时，显示默认加载的提示信息，该属性只在周/天视图里可用。
			select: function(start, end, allDay) {
				if(compareDate(start.format(),"${startDate?string('yyyy-MM-dd')}") < 0 || compareDate(start.format(),"${endDate?string('yyyy-MM-dd')}")>0){
					layerTipMsg(false,"错误","只能维护本学期的数据，本学期范围为${startDate?string('yyyy-MM-dd')}~${endDate?string('yyyy-MM-dd')}");
				}else{
					var url = "${request.contextPath}/stuwork/checkweek/roleUserTeacherEdit/page?dutyDate="+start.format();
					indexDiv = layerDivUrl(url,{title: "编辑值周干部",width:350,height:330});
				}
			},
			eventClick: function(calEvent, jsEvent, view) {
				if(compareDate(calEvent.start.format(),"${startDate?string('yyyy-MM-dd')}") < 0 || compareDate(calEvent.start.format(),"${endDate?string('yyyy-MM-dd')}")>0){
					layerTipMsg(false,"错误","只能维护本学期的数据，本学期范围为${startDate?string('yyyy-MM-dd')}~${endDate?string('yyyy-MM-dd')}");
				}else{
					var url = "${request.contextPath}/stuwork/checkweek/roleUserTeacherEdit/page?dutyDate="+calEvent.start.format();
					indexDiv = layerDivUrl(url,{title: "编辑值周干部",width:350,height:330});
				}
			}
		})
	});
function compareDate(elem1,elem2){
	if(elem1 !="" && elem2 !=""){
		 var date1 ;
		 var date2 ;
		date1 = elem1.split('-');
		date2 = elem2.split('-');
		 if(eval(date1[0])>eval(date2[0])){
		 	return 1;
		 }else if(eval(date1[0])==eval(date2[0])){
		 	if(eval(date1[1])>eval(date2[1])){
		 		return 1;
		 	}else if(eval(date1[1])==eval(date2[1])){
		 	    if(eval(date1[2])>eval(date2[2])){
			 		return 1;
			 	}else if(eval(date1[2])==eval(date2[2])){
			 		return 0;
			 	}else{
			 	    return -1;
			 	}	
		 	}else{
		 		return -1;
		 	}
		 }else{
		 	return -1;
		 }
	}
}
</script>
