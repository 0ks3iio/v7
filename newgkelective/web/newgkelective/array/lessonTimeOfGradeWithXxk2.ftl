<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="explain explain-white">
		<div class="mb10"><b>课时说明：</b></div>
		<div>选中点为行政班课程上课时间，其他时间为两科组合班上课时间</div>
		<div class="color-999"><i class="glyphicon glyphicon-info-sign color-yellow"></i>
		
		小提示：行政班课程至少需要时间  ${maxXzbLecCount!0}，两科组合班 至少需要时间  ${maxFakeLecCount!0};
		课表总可安排时间要大于 ${(maxXzbLecCount+maxFakeLecCount)!0}才有可能排课成功；
		</div>
	</div>
	<div class="row">
	<div class="col-sm-9">
	<div class="table-container">
		<div class="table-container-header" style="">
			<div class="schedule-labels js-schedule-labels" data-for="table01">
				<b>行政班时间：</b>
				<#if xdtos?exists && xdtos?size gt 0>
				<#list xdtos as dto>
				<input type="hidden" name="filterType" id="type${dto.objId!}" value="${dto_index+1}">
				<input type="hidden" name="objId" id="objId${dto_index+1}" value="${dto.objId!}">
				<input type="hidden" name="groupType" id="groupType${dto_index+1}" value="${dto.groupType!}">
				<input type="hidden" name="is_join" id="isJoin${dto_index+1}" value="1">
				<input type="hidden" name="timeType" id="timeType${dto_index+1}" value="02">
				<#assign labColorIndex = dto_index+1 />
				<label class="schedule-label schedule-label-white schedule-label-${labColorIndex}" id="data${dto_index+1}" 
						data-filter="${dto_index+1}" data-objid="${dto.objId!}" data-filter-content="${dto.objName!}">
						<i class="fa fa-plus"></i>${dto.objName!}
						<span class="badge badge-yellow" id="freeCount${dto.objId!}">0</span>
				</label>
				</#list>
				</#if>
				<#-- 
				<label class="schedule-label schedule-label-white schedule-label-0" data-filter="0"><i class="fa fa-trash"></i>删除</label>
				 -->
			</div>
		</div>
		<div class="table-container-body">
			<form id="gradeTime" action="" method="POST">
			<input type="hidden" name="needSource" value="true">
			<table id="table01" class="table table-bordered table-schedule" data-filter="100">
			<#assign weekDays = (weekDays!7) - 1>
			<thead>
				<tr>
					<th class="text-center" width="30"></th>
					<th class="text-center" width="25"></th>
					<#list 0..weekDays as day>
		            <th class="text-center" id="wk-${day}">${dayOfWeekMap[day+""]!}</th>
		            </#list>
				</tr>
			</thead>
			<tbody>
			<#assign editNum = 0 />
			<#assign hasPeriodTip = periodTipMap?exists && periodTipMap?size gt 0 >
			
			<#list piMap?keys as piFlag>
			    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
			    <#assign interval = piMap[piFlag]>
			    <#assign intervalName = intervalNameMap[piFlag]>
			    <#list 1..interval as pIndex>
			    <tr data-interval='${piFlag}'>
			    <#if pIndex == 1>
			    	<td rowspan="${interval!}" class="text-center">${intervalName!}<input type="hidden" disabled name="period_interval" value="${piFlag}" disable/></td>
			    </#if>
		        	<td class="text-center">${pIndex!}</td>
					<#list 0..weekDays as day>
			            <#assign tc = day+"-"+piFlag+"-"+pIndex />
			            <#assign strs = [] />
						<#if hasPeriodTip>
							<#--周几-上下午-节次-->
							<#assign strs = periodTipMap[tc]?default([]) >
							<#assign tdBatchIds = ''/>
							<#if strs?exists && strs?size == 3>
								<#assign tdBatchIds = strs[0]?default('') />
							</#if>
						</#if>
						
						<td class="text-center content-td edited" td-num="${editNum*(weekDays+1)+day}" td-time="${tc}" batchIds="${tdBatchIds!}">
							<div height="36px;">&nbsp;</div>
							<#--周几-上下午-节次-->
							<#if strs?exists && strs?size == 3>
								<p class="tip">
								<#if strs[1]?default('') != ''>
								<span <#if strs[2]?default('') != ''>class="mr10"</#if>>选(${strs[1]})</span>
								</#if>
								<#if strs[2]?default('') != ''>
								<span>学(${strs[2]})</span>
								</#if>
								</p>
							<#elseif hasPeriodTip>
								<p class="tip"><span>不支持</span></p>
							</#if>
						</td>
					</#list>
			    </tr>
			    <#assign editNum = editNum+1 />
			    </#list>
			    </#if>
		    </#list>
			</tbody>
			</table>
		</form>
		</div>
	</div>
	</div>
	<#-- 
	<div class="col-sm-3">
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="box-title">冲突记录</h3>
			</div>
			<div class="box-body" id="warningDiv" style="overflow-y:auto;">
				<div class="no-data-container">
					<div class="no-data">
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
						</span>
						<div class="no-data-body">
							<p class="no-data-txt">暂无记录</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	 -->
	</div>	
	<div class="navbar-fixed-bottom opt-bottom">
		<#-- <a src="javascript:void(0);"  class="btn btn-blue" id="autoArrange">自动安排</a>  -->
		<a src="javascript:void(0);"  class="btn btn-blue" id="confirmSubmit">保存</a>
	</div>
</div>
<script>
$(function(){
	var mh = $('.col-sm-9').height()-60;
	$('#warningDiv').css("max-height",mh+"px");
	
	function removeWarn(twarnId){
		$('#'+twarnId).remove();
	}
	
	// 显示
	function appendWarn(tdObj, baId){
		var tk = $(tdObj).attr('td-time');
		var batchId = baId+';'+tk;
		var tks = tk.split('-');
		var wd = $("#wk-"+tks[0]).text();
		var inter = '上午';
		if(tks[1] == '3'){
			inter = '下午';
		} else if(tks[1] == '4'){
			inter = '晚上';
		}
		var wtitle = content+'（'+wd+inter+'第'+tks[2]+'节）';
		var wsubs = '';
		var wteas = '';
		<#if timeMap?exists && timeMap?size gt 0>
			<#assign keys = timeMap?keys>
			<#assign getData = false />
			<#list keys as timeKey>
				if('${timeKey}'==batchId){
					<#assign times = timeMap[timeKey] />
					wsubs='${times[0]}';
					wteas='${times[1]}';
					<#assign getData = false />
				}
				<#if getData>
					<#break />
				</#if>
			</#list>
		</#if>
		if(wsubs == '' && wteas == ''){
			return;
		}
		$(tdObj).addClass('warn');

		var twarnId = baId+'-'+tk;
		$('#'+twarnId).remove();
		
		var whtml = '<table width="100%" class="box-line table bg-warning" id="'+twarnId+'"><tbody><tr><td colspan="2">';
		whtml += '<b class="float-left">'+wtitle+'</b><a class="float-right" href="javascript:;" onclick="$(\'#'+twarnId+'\').remove();"><i class="glyphicon glyphicon-trash"></i></a></td></tr>';
		whtml += '<tr><td width="100" valign="top">不可排科目：</td><td>'+wsubs+'</td></tr>';
		whtml += '<tr><td valign="top">不可排教师：</td><td>'+wteas+'</td></tr></tbody></table>';
		
		$('#warningDiv .no-data-container').remove();
		$('#warningDiv table').removeClass('bg-warning');
		$('#warningDiv').prepend(whtml);
	}
	
	/*当前操作类型*/
	var type = 100; 
	var content;
	/*课时计数器*/
	var counter = 0;	
	var currentLabel;

	// 刷新课时计数器
	function refreshCounter(el, c){
		$(el).find('.badge').text(c);
	}

	$('.js-schedule-labels label').on('click',function(){
		if($(this).hasClass('selected')){
			$(this).removeClass('selected');
			type=100;
			return;
		}else{
			$(this).addClass('selected').siblings().removeClass('selected');
		}

		currentLabel = $(this);
		type = $(this).data('filter');
		content = $(this).data('filter-content');
		counter = $(this).find('.badge').text();
	});

	$('#table01').find('.edited').on('click', function(e){
		if($(this).hasClass('disabled')){
			return;
		}
		$('#table01 .warn').removeClass('warn');
		var tempLabel = $(this).find('label').attr("data-label");
		if (type == 100) {
			layer.msg("请先选择需要进行的操作！", {
						offset: 't',
						time: 2000
					});
		} else if (type == 0){
			if($(this).find('label').length == 0){
				layer.msg("请选择其他操作！", {
						offset: 't',
						time: 2000
					});
				return false;
			}else{
				counter = $('.schedule-labels').find('[data-filter='+ tempLabel +']').find('.badge').text();
				$(this).children('div').empty();
				$(this).children('div').append("&nbsp;");
				$(this).removeClass('active');
				refreshCounter($('.schedule-labels').find('[data-filter='+ tempLabel +']'), --counter);
			}
		} else {
			var objId = $('#objId'+type).val();
			var baIds = $(this).attr('batchIds');
			if(baIds.indexOf(objId) == -1){
				//appendWarn(this, objId);
				//return;
			}
			
			if($(this).find('label').length == 0){
				var label = getTdContent(this);
				$(this).children('div').empty().append(label);
				refreshCounter(currentLabel, ++counter);
			}else{
				if( tempLabel == type ){
					$(this).children('div').empty();
					$(this).children('div').append("&nbsp;");
					$(this).removeClass('active');
					refreshCounter(currentLabel, --counter);
				}else{
					var tempCounter = $('.schedule-labels').find('[data-filter='+ tempLabel +']').find('.badge').text();
					var label = getTdContent(this);
					$(this).children('div').empty().append(label);
					$('.schedule-labels').find('[data-filter='+ tempLabel +']').find('.badge').text(--tempCounter);
					refreshCounter(currentLabel, ++counter);
				}
			}
		};
	});
	
	function getTdContent(obj, selType, selContent){
		if(!selType){
			selType = type;
		}
		if(!selContent){
			selContent = content;
		}
		
		//找出在一天中的那个时间段
		var rowHeadInf = getHeadInf(obj.parentNode);
		var period_interval = rowHeadInf.value;
		//这个时间段中的第几节课
		var rowIndex = obj.parentNode.rowIndex;
		var period = rowIndex-rowHeadInf.rowIndex+1;
		//找出是周几
		var weekday;
		if($(obj).parent().children("[rowspan]").length>0){
			weekday = obj.cellIndex-2;
		}else{
			weekday = obj.cellIndex-1;
		}
		var tdi = $(obj).attr('td-num');
		return getTdContentStr(selType, selContent,tdi, weekday,period_interval,period);
	}
	
	function getTdContentStr(selType, selContent, tdi, weekday, period_interval, period){
		if(selContent.length>7){
			selContent=selContent.substring(0,7);
		}
		
		var join = $('#isJoin'+selType).val();
		var objId = $('#objId'+selType).val();
		var gtype = $('#groupType'+selType).val();
		var ttype = $('#timeType'+selType).val();
		var selCo = parseInt(selType);
		if(selCo>7){
			selCo = selCo-6;
		}
		var label = '<label class="schedule-label schedule-label-'+selCo+' thin selected" data-label="'+selType+'">'+selContent+'</label>';
		
		label = label+'<input type="hidden" name="lessonTimeDto['+tdi+'].weekday" value="'+weekday+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].period_interval" value="'+period_interval+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].period" value="'+period+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].is_join" value="'+join+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].objId" value="'+objId+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].timeType" value="'+ttype+'"/>';
		
		label = label+'<input type="hidden" name="sourceTimeDto['+tdi+'].objId" value="'+objId+'"/>'
			+'<input type="hidden" name="sourceTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
			+'<input type="hidden" name="sourceTimeDto['+tdi+'].is_join" value="'+join+'"/>';
			
		return label;
	}
	
	$("#table01 .active").each(function(i){
		var label = getTdContent(this, 1, '不可排课');
		$(this).children('div').empty().append(label);
	});
	
	<#-- 初始化数据组装，需要修改 -->
	//对不排课的位置，模拟点击
	function imitateClick(lessonInfJson){
		var length = lessonInfJson.length;
		$.each(lessonInfJson,function(i,e){
			var objId = e.objId;
			var period_interval = e.period_interval;
			var period = e.period;
			var weekday = e.weekday;
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
			
			var tdtype = $('#type'+objId).val(); 
			var tdlabel = $('#data'+tdtype);
			var tdcontent = $(tdlabel).data('filter-content');
			var tdobj = $tr.find(".edited:eq("+weekday+")");
			var tdi = $(tdobj).attr('td-num');
			var label = getTdContentStr(tdtype, tdcontent, tdi, weekday, period_interval, period);
			$(tdobj).children('div').empty().append(label);
			
			var tempCounter = $(tdlabel).find('.badge').text();
			refreshCounter(tdlabel, ++tempCounter);
		});
		console.log("counter = "+counter);
	}
	
	//根据总课表限制 科目课表
	function limitTable(lessonTimeDtosJson){
		for(var i=0;i<lessonTimeDtosJson.length;i++){
			var weekday = lessonTimeDtosJson[i].weekday;
			var period_interval = lessonTimeDtosJson[i].period_interval;
			var period = lessonTimeDtosJson[i].period;
			
			//通过获取tr和td来定位
			var $tr = $("[disabled][name='period_interval'][value='"+period_interval+"']").parents("tr");
			if(period != 1){
				for(var j=0;j<(period-1);j++){
					$tr = $tr.next("tr");
				}
			}
			$parm = $tr.find(".content-td:eq("+weekday+")");
			$parm.removeClass("edited");
			$parm.addClass("editable");
			$parm.addClass("disabled");
			$parm.html("<span class='color-ccc'>不可排课</span>");
		}
	}
	
	//如果是编辑功能，页面加载完毕时，获取课时安排数据，并且模拟点击
	$(function(){
		var array_item_id = $("input[name='array_item_id']").val();
		if(array_item_id==""){
			return;
		}
		var url = "${request.contextPath}/newgkelective/"+array_item_id+"/lessonTimeinf/json";
		$.post(url, 
			function(data){
				var dataJson = $.parseJSON(data);
				//模拟点击总课表
				imitateClick(dataJson.lessonTimeDtosJson);
				limitTable(dataJson.gradeTimeDtosJson);
				$(".js-schedule-labels label:eq(0)").click();
			});
	});
	
	var xuangt = '${groupType5!}';
	function checkTimes(){
		var fts = $('input[name="filterType"]');
		if(!fts || fts.length==1){
			return true;
		}
		var xuanks = '';
		var xueks = '';
		for(var i=1;i<fts.length;i++){
			var ftnum = $(fts[i]).val();
			var gt = $('#groupType'+ftnum).val();
			var gtcount = $('#data'+ftnum).find('.badge').text();
			if(gt == xuangt){
				if(xuanks==''){
					xuanks=gtcount;
				} else if(xuanks != gtcount){
					layerTipMsg(false,"失败","选考类型各时间点的设置数应一致！");
					return false;
				}
			} else {
				if(xueks==''){
					xueks=gtcount;
				} else if(xueks != gtcount){
					layerTipMsg(false,"失败","学考类型各时间点的设置数应一致！");
					return false;
				}
			}
		}
		return true;
	}
	
	//总课表提交结果  .btn-blue:first
	var hasSubmit=false;
	function saveGrade(){
		if(hasSubmit){
			return;
		}
		hasSubmit=true;
		var divide_id = '${divide_id!}';
		var array_item_id = $('[name="array_item_id"]').val();
		
		var url = "";
		var flag = "";
		url = '${request.contextPath}/newgkelective/'+array_item_id+'/updateLessonTimeNew?objectType=${objectType!'7'}';
		flag = "isUpdate";
		var params = $("#gradeTime").serialize();
		params += "&arrayId="+arrayId;
		
		$.post(url, 
			params, 
			function(data){
				hasSubmit=false;
				
				var dataJson = $.parseJSON(data);
				if(flag=="isAdd"){
					//插入arrayItemId到隐藏<input/>
					var arrayItemId = dataJson.arrayItemId;
					$("[name='array_item_id']").val(arrayItemId);
				}
				
				var msg = dataJson.msg;
				if(msg=="SUCCESS"){
					layer.closeAll();
					layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
				}else{
					layerTipMsg(false,"失败",msg);
				}
			});
	}
	
	//总课表提交结果  .btn-blue:first
	var save_msg = '';
	if(arrayId){
		save_msg = '如果您还未完成排课，修改此结果可能会影响预排课表部分课程，继续吗？';
	}else{
		save_msg = '确认提交结果吗？';
	}
	$('#confirmSubmit').click(function(){
		var msg = save_msg;
		if(!check()) return;
		layer.confirm(msg,function(index){
				saveGrade();
			}
		);
	});
	
	function check(){
		var availableTimes = $("#table01 .edited").length;
		
		var len = $("tbody label.selected").length;
		if(len < ${maxXzbLecCount!0}){
			layer.msg('行政班时间点至少需要 ${maxXzbLecCount!0}', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
			return false;
		}
		if((availableTimes-len) < ${maxFakeLecCount!0}){
			layer.msg('两科组合班时间点至少需要 ${maxFakeLecCount!0}', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
			return false;
		}
		//if(availableTimes < ${(maxXzbLecCount+maxFakeLecCount)!0}){
		//	layer.msg('总可安排时间至少需要  ${availableTimes!0}', {
		//						icon: 2,
		//						time: 1500,
		//						shade: 0.2
		//					});
		//	return false;
		//}
		return true;
	}
	
	$('#autoArrange').click(function(){
		var tips = "自动安排结果仅供参考，请检查结果是否符合您的要求,继续吗？";
		layer.confirm(tips,function(index){
			var array_item_id = $('[name="array_item_id"]').val();
			var url = "${request.contextPath}/newgkelective/${divide_id!}/autoBatchSolve";
			var params = "arrayItemId="+array_item_id;
			if(arrayId){
				params += "&arrayId="+arrayId;
			}
			var ii = layer.load();
			$.post(url, 
				params, 
				function(data){
					layer.close(ii);
					layer.close(index);
					if(data.success){
						layer.msg("安排成功", {
								offset: 't',
								time: 2000
							});
						timeTable();
					}else{
						layerTipMsg(false,"失败",data.msg);
					}
				},"JSON");
			}
		);
	});
});
</script>