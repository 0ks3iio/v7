		<#--<ul class="nav nav-tabs hidden" role="tablist">
			<li class="active" role="presentation"><a href="#FamDearPlanService" role="tab" data-toggle="tab">不排课时间</a></li>
			<li role="presentation"><a href="#bbb" role="tab" data-toggle="tab">要排课时间</a></li>
		</ul>-->
		<div class="">
			<!-- 这里用来存储 正在编辑的是第几行的数据  从0开始 -->
			<input type="hidden" name="rowIndexFrom"/>
			<form id="gradeTime" action="" method="POST">
			<input type="hidden" name="needSource" value="true">
			<input type="hidden" name="groupType" id="groupType" value="${groupType!}">
			<input type="hidden" name="sourceType" id="sourceType" value="${sourceType!}">
			<input type="hidden" name="basicSave" id="basicSave" value="${basicSave?string("true","false")}">
			<input type="hidden" name="sourceTimeDto[0].objId" id="objId" value="${teacherId!}"/>
			<input type="hidden" name="sourceTimeDto[0].groupType" value="${groupType!}"/>
			<input type="hidden" name="sourceTimeDto[0].is_join" value="1"/>
			<div id="aaa" class="tab-pane active" role="tabpanel" style="overflow-y:auto;">
				<div class="explain">
					<p>小提示：灰色背景的不排课需要在年级特征里设置</p>
				</div>
				<table class="table table-bordered layout-fixed table-editable" timeType="01" data-label="不排课">
					<#assign weekDays = (weekDays!7) - 1>
					<#assign wratio = (100 - 12.5)/(weekDays+1)>
					<thead>
						<tr>
							<th width="8%" class="text-center"></th>
							<th width="4.5%" class="text-center"></th>
							<#list 0..weekDays as day>
				            <th width="${wratio}%" class="text-center">${dayOfWeekMap[day+""]!}</th>
				            </#list>
						</tr>
					</thead>
					<tbody>						
						<#assign editNum = 0 />
						<#list piMap?keys as piFlag>
						    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
						    <#assign interval = piMap[piFlag]>
						    <#assign intervalName = intervalNameMap[piFlag]>
						    <#list 1..interval as pIndex>
						    <tr>
						    <#if pIndex == 1>
						    	<td rowspan="${interval!}" class="text-center">${intervalName!}<input type="hidden" disabled name="period_interval" value="${piFlag}" disable/></td>
						    </#if>
					        	<td class="text-center">${pIndex!}</td>
								<#list 0..weekDays as day>
					            <td class="text-center editable" td-num="${editNum*(weekDays+1)+day}"></td>
								</#list>
						    </tr>
						    <#assign editNum = editNum+1 />
						    </#list>
						    </#if>
					    </#list>
					    
					</tbody>
				</table>
			</div>
			</form>
		</div>
		<div class="layui-layer-btn">
			<a class="layui-layer-btn0" id="act-commit">确定</a>
			<a class="layui-layer-btn1" id="act-close">取消</a>
		</div>
<script>
$(function(){
	$('#FamDearPlanService').height(465);

<#--课表时间-->
	// 取消按钮操作功能
	$("#act-close").on("click", function(){
		doLayerOk("#act-commit", {
			redirect:function(){},
			window:function(){layer.closeAll()}
		});		
	 });

	var isSubmit=false;	
	$('#act-commit').on('click',function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		saveTime();
	});
	
	//生成显式课时信息 在各科目课表中
	function getTime(weekday,period_interval,period){
		var content = "";
		var weekJson = ["一","二","三","四","五","六","日"];
		weekday = weekJson[weekday];
		if(period_interval=='1'){
			content = content+"周"+weekday+"早自习"+"第"+period+"节";
		}else if(period_interval=='2'){
			content = content+"周"+weekday+"上午"+"第"+period+"节";
		}else if(period_interval=='3'){
			content = content+"周"+weekday+"下午"+"第"+period+"节";
		}else if(period_interval=='4'){
			content = content+"周"+weekday+"晚自习"+"第"+period+"节";
		}else if(period_interval=='9'){
			content = content+"周"+weekday+"特殊时间"+"第"+period+"节";
		}
		return content;
	}
	
	var oldi = 0;
	function dealListData(){
		var actives  = $("#FamDearPlanService").find(".active");
		var content01 = '';
		var content02 = '';
		var submitInf = '';
		var iii=0;
		//遍历每个选中的TD
		for(var i=0;i<actives.length;i++){
			//先获取td中的信息
			var actobj = actives.eq(i);
			var tdi = $(actobj).attr('td-num');
			var weekday = $("#weekday"+tdi).val();
			var period_interval = $("#period_interval"+tdi).val();
			var period = $("#period"+tdi).val();
			var time_type = $("#timeType"+tdi).val();
			
			//根据是否排课构造显示信息
			if(time_type=="01"){
				iii++;
				//content01 = content01 + '<span>'+getTime(weekday,period_interval,period)+'</span><br>';
			}
			<#--else if(time_type=="02"){
				content02 = content02 + '<span>'+$("#FamDearPlanService table th:eq("+n+")").text()+intervalName+'第'+period+'节</span><br>'
				content02 = content02 + '<input type="hidden" name="coordinate" value="'+coordinate+'"/>';
			}-->
		}
		//将结果插入到主表的相应位置
		
		//修改只插入数量
		console.log("-----0  "+oldi);
		iii = parseInt($(".notime_${teacherId!}").html()) + iii - oldi;
		$(".notime_${teacherId!}").html(iii);
	}
	
	function saveTime(){
		var url = '${request.contextPath}/newgkelective/${itemId!}/updateLessonTimeNew?objectType=${objectType!'2'}';
		var params = $("#gradeTime").serialize();
		
		$.post(url, 
			params, 
			function(data){
				var dataJson = $.parseJSON(data);
				var msg = dataJson.msg;
				isSubmit=false;	
				if(msg=="SUCCESS"){
					dealListData();
					$("#act-close").click();
					layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
				}else{
					layerTipMsg(false,"失败",msg);
				}
			});
	}
	
	function getHeadInf(rowObj){
		//获取当前行中的rowspan属性的单元格数量
		var head = $(rowObj).children("[rowspan]");
		//如果数量大于0 说明存在这样的单元格
		if(head.length>0){
			
			return {value:head.children(":hidden").val(),rowIndex:rowObj.rowIndex};
		}
		else {
			//如果数量为零，找前一行中的具有rowspan属性的单元格
			return getHeadInf($(rowObj).prev("tr")[0]);
		}
	}
	
	//为每个单元格填充数据
	$('.table-editable').find('.editable').on('click', function(e){
		var obj=this;
		if($(this).hasClass('active')){
			//可能存在年级不排课与教师不排课冲突 可以 取消 
			$(this).empty();
			$(this).removeClass('active');			
			return;
		}else if($(this).hasClass('disabled')){
			//年级不排课
			return;
		}
		
		//找出在一天中的那个时间段
		var rowHeadInf = getHeadInf(this.parentNode);
		var period_interval = rowHeadInf.value;
		//这个时间段中的第几节课
		var rowIndex = this.parentNode.rowIndex;
		var period = rowIndex-rowHeadInf.rowIndex+1;
		//找出是周几
		var weekday;
		if($(obj).parent().children("[rowspan]").length>0){
			weekday = obj.cellIndex-2;
		}else{
			weekday = obj.cellIndex-1;
		}
		var tdi = $(obj).attr('td-num');
		
		//插入<span>
		var content ='';
		var tableobj = $(this).parents("table").first();
		var labledata = tableobj.data('label');
		var timeType = tableobj.attr('timeType');
		//针对不同的表插入不同的<input/>内容
		if($(this).parents("#FamDearPlanService").length>0){
			content = getTdContentStr(labledata,tdi,weekday,period_interval,period,timeType);
		}
		<#--else if($(this).parents("#bbb").length>0){
			//id为bbb表明位置是在不排课 表  02.必排课
			content = '<input type="hidden" name="time_type" value="02"/>';
			if($(this).hasClass('active')){
				content = content + '<span class="color-red">'+labledata+'</span>';
			}else{
				content = content + '<span class="color-red" style="visibility:hidden;">'+labledata+'</span>';
			}
			
			content = content +'<input type="hidden" name="weekday" value="'+weekday+'"/>'
							+'<input type="hidden" name="period_interval" value="'+period_interval+'"/>'
							+'<input type="hidden" name="period" value="'+period+'"/>';
		}-->
		$(this).addClass('active');
		$(this).html(content);
		
	});
	
	//页面加载完毕时，获取课时安排数据，并且模拟点击
	$(function(){
		var url = "${request.contextPath}/newgkelective/teacherClass/teacherTime/json?arrayItemId=${itemId!}&teacherId=${teacherId!}";
		$.post(url, 
			function(data){
				var dataJson = $.parseJSON(data);
				//动态生成各班课表
				var subjectTimeDtos = dataJson.timeDtosJson;
				oldi = subjectTimeDtos.length;
				if(subjectTimeDtos.length>0){
					imitateClick(subjectTimeDtos);
				}
				var lessonTimeDtosJson = dataJson.lessonTimeDtosJson;
				limitTable(lessonTimeDtosJson);
			});
	});
	
	//根据总课表限制 科目课表
	function limitTable(lessonTimeDtosJson){
		for(var i=0;i<lessonTimeDtosJson.length;i++){
			var weekday = lessonTimeDtosJson[i].weekday;
			var period_interval = lessonTimeDtosJson[i].period_interval;
			var period = lessonTimeDtosJson[i].period;
			var timeType = lessonTimeDtosJson[i].timeType;
			
			limitTd(weekday, period_interval, period,timeType);
		}
	}
	
	function limitTd(weekday, period_interval, period,timeType){
		//通过获取tr和td来定位
		var $tr = $("[disabled][name='period_interval'][value='"+period_interval+"']").parents("tr");
		if(period == 1){
		
		}else{
			for(var j=0;j<(period-1);j++){
				$tr = $tr.next("tr");
			}
		}
		$parm = $tr.find(".editable:eq("+weekday+")");
		$parm.addClass("disabled");
		
		if(timeType == "04"){
			$parm.html("已安排");
		}else if(timeType == "05"){
			$parm.html("教师组");
		}
	}
<#--课表时间 end-->
	
	function getTdContentStr(selContent, tdi, weekday, period_interval, period, timeType){
		var objId = $('#objId').val();
		var gtype = $('#groupType').val();
		var label = '<span style="color:red;">'+selContent+'</label>';
		
		label = label+'<input type="hidden" id="weekday'+tdi+'" name="lessonTimeDto['+tdi+'].weekday" value="'+weekday+'"/>'
			+'<input type="hidden" id="period_interval'+tdi+'" name="lessonTimeDto['+tdi+'].period_interval" value="'+period_interval+'"/>'
			+'<input type="hidden" id="period'+tdi+'" name="lessonTimeDto['+tdi+'].period" value="'+period+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].is_join" value="1"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].objId" value="'+objId+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
			+'<input type="hidden" id="timeType'+tdi+'" name="lessonTimeDto['+tdi+'].timeType" value="'+timeType+'"/>';
			
		return label;
	}
	
	<#-- 初始化数据组装，需要修改 -->
	//对不排课的位置，模拟点击
	function imitateClick(lessonInfJson){
		var length = lessonInfJson.length;
		$.each(lessonInfJson,function(i,e){
			var objId = e.objId;
			var period_interval = e.period_interval;
			var period = e.period;
			var weekday = e.weekday;
			var ttype = e.timeType;
			//找到period_interval所在行，加上period-1，获得行数
			
			var rowIndex = $("[name='period_interval'][value='"+period_interval+"']:first").parent().parent()[0].rowIndex+period-1;
			//weekday+1为列数，如果period不为1，则为weekday
			
			var	colIndex = weekday;
			
			//通过获取tr和td来定位
			var $tr = $("[name='period_interval'][value='"+period_interval+"']:first").parents("tr");
			if(period != 1){
				for(var i=0;i<(period-1);i++){
					$tr = $tr.next("tr");
				}
			}
			
			var tdcontent = $tr.parents("table").first().data('label');
			var tdobj = $tr.find(".editable:eq("+weekday+")");
			var tdi = $(tdobj).attr('td-num');
			var label = getTdContentStr(tdcontent, tdi, weekday, period_interval, period,ttype);
			$(tdobj).empty().append(label);
			$(tdobj).addClass('active');
		});
	}
})
</script>