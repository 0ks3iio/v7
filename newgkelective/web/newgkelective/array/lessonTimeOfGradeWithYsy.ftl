<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="explain explain-white">
		<div class="mb10"><b>课时说明：</b></div>
		<#if dtos?exists && dtos?size gt 0>
		<#list dtos as dto>
			<div>${dto.objName!}：
			<#if dto.subTimes?exists && dto.subTimes?size gt 0>
			<#list dto.subTimes as st>
			${st.subjectName!}（${st.period?default(0)}）<#if st_has_next>，</#if>
			</#list>；
			</#if>共计${dto.period?default(0)}课时</div>
		</#list>
		</#if>
		<div class="color-999"><i class="glyphicon glyphicon-info-sign color-yellow"></i> 小提示：大于等于每周课时数，才有可能排课成功</div>
	</div>
	<div class="row">
	<div class="col-sm-9">
	<div class="table-container">
		<div class="table-container-header">
			<div class="schedule-labels js-schedule-labels" data-for="table01">
				<#--<input type="hidden" name="filterType" id="type${gradeId!}" value="1">
				<input type="hidden" name="objId" id="objId1" value="${gradeId!}">
				<input type="hidden" name="groupType" id="groupType1" value="1">
				<input type="hidden" name="is_join" id="isJoin1" value="0">
				<input type="hidden" name="timeType" id="timeType1" value="01">
				<label class="schedule-label schedule-label-white schedule-label-1" id="data1" data-filter="1" data-filter-content="不可排课"><i class="fa fa-plus"></i>不可排课</label>
				-->
				<#if dtos?exists && dtos?size gt 0>
				<#list dtos as dto>
				<input type="hidden" name="filterType" id="type${dto.objId!}" value="${dto_index+1}">
				<input type="hidden" name="objId" id="objId${dto_index+1}" value="${dto.objId!}">
				<input type="hidden" name="groupType" id="groupType${dto_index+1}" value="${dto.groupType!}">
				<input type="hidden" name="is_join" id="isJoin${dto_index+1}" value="1">
				<input type="hidden" name="timeType" id="timeType${dto_index+1}" value="02">
				<label class="schedule-label schedule-label-white schedule-label-${dto_index+1}" id="data${dto_index+1}" data-filter="${dto_index+1}" data-objid="${dto.objId!}" data-filter-content="${dto.objName!}"><i class="fa fa-plus"></i>${dto.objName!}<span class="badge badge-yellow" id="freeCount${dto.objId!}">0</span></label>
				</#list>
				</#if>
				<label class="schedule-label schedule-label-white schedule-label-0" data-filter="0"><i class="fa fa-trash"></i>删除</label>
			</div>
		</div>
		<div class="table-container-body">
			<form id="gradeTime" action="" method="POST">
			<input type="hidden" name="needSource" value="true">
			<table id="table01" class="table table-bordered table-schedule" data-filter="100">
			<thead>
				<tr>
					<th width="30" class="text-center"></th>
					<th width="25" class="text-center"></th>
					<th width="12.5%" class="text-center" id="wk-0">周一</th>
					<th width="12.5%" class="text-center" id="wk-1">周二</th>
					<th width="12.5%" class="text-center" id="wk-2">周三</th>
					<th width="12.5%" class="text-center" id="wk-3">周四</th>
					<th width="12.5%" class="text-center" id="wk-4">周五</th>
					<th width="12.5%" class="text-center" id="wk-5">周六</th>
					<th width="12.5%" class="text-center" id="wk-6">周日</th>
				</tr>
			</thead>
			<tbody>
			<#assign editNum = 0 />
			<#assign hasPeriodTip = periodTipMap?exists && periodTipMap?size gt 0 >
			<#if amList?exists && amList?size gt 0>
			<#list amList as i>
				<tr data-interval='2'>
					<#if (i=='1')>
						<td class="text-center" rowspan="${amList?size}">上午<input type="hidden" disabled name="period_interval" value="2" disable/></td>
					</#if>
					<td class="text-center">${i}</td>
					
					<#list 0..6 as wd>
						<#assign strs = [] />
						<#if hasPeriodTip>
							<#--周几-上下午-节次-->
							<#assign strs = periodTipMap[wd+'-2-'+i]?default([]) >
							<#assign tdBatchIds = ''/>
							<#if strs?exists && strs?size == 3>
								<#assign tdBatchIds = strs[0]?default('') />
							</#if>
						</#if>
					
					<td class="text-center content-td edited" td-num="${editNum*7+wd}" td-time="${wd+'-2-'+i}" batchIds="${tdBatchIds!}">
						<div></div>
						<#if strs?exists && strs?size == 3>
							<p class="tip">
							<#if strs[1]?default('') != ''>
							<span >${strs[1]}</span>
							</#if>
							</p>
						<#elseif hasPeriodTip>
							<p class="tip"><span>不支持</span></p>
						</#if>
					</td>
					</#list>
					
					<#--<td class="text-center edited" td-num="${editNum*7}"></td>
					<td class="text-center edited" td-num="${editNum*7+1}"></td>
					<td class="text-center edited" td-num="${editNum*7+2}"></td>
					<td class="text-center edited" td-num="${editNum*7+3}"></td>
					<td class="text-center edited" td-num="${editNum*7+4}"></td>
					<td class="text-center edited" td-num="${editNum*7+5}">
						
					</td>
					<td class="text-center edited" td-num="${editNum*7+6}">
						
					</td>-->
				</tr>
				<#assign editNum = editNum+1 />
			</#list>
			</#if>
			
			<#if pmList?exists && pmList?size gt 0>
			<#list pmList as i>
				<tr data-interval='3'>
					<#if (i=='1')>
						<td class="text-center" rowspan="${pmList?size}">下午<input type="hidden" disabled name="period_interval" value="3" disable/></td>
					</#if>
					<td class="text-center">${i}</td>
					
					<#list 0..6 as wd>
						<#assign strs = [] />
						<#if hasPeriodTip>
							<#--周几-上下午-节次-->
							<#assign strs = periodTipMap[wd+'-3-'+i]?default([]) >
							<#assign tdBatchIds = ''/>
							<#if strs?exists && strs?size == 3>
								<#assign tdBatchIds = strs[0]?default('') />
							</#if>
						</#if>
					<td class="text-center content-td edited" td-num="${editNum*7+wd}" td-time="${wd+'-3-'+i}" batchIds="${tdBatchIds!}">
						<div></div>
						<#--周几-上下午-节次  id="time${(strs[0])?default(0)}"-->
						<#if strs?exists && strs?size == 3>
							<p class="tip">
							<#if strs[1]?default('') != ''>
							<span >${strs[1]}</span>
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
			
			<#if nightList?exists && nightList?size gt 0>
			<#list nightList as i>
				<tr data-interval='4'>
					<#if (i=='1')>
						<td class="text-center" rowspan="${nightList?size}">晚上<input type="hidden" disabled name="period_interval" value="4" disable/></td>
					</#if>
					<td class="text-center">${i}</td>
					
					<#list 0..6 as wd>
					
						<#assign strs = [] />
						<#if hasPeriodTip>
							<#--周几-上下午-节次-->
							<#assign strs = periodTipMap[wd+'-4-'+i]?default([]) >
							<#assign tdBatchIds = ''/>
							<#if strs?exists && strs?size == 3>
								<#assign tdBatchIds = strs[0]?default('') />
							</#if>
						</#if>
					
					<td class="text-center content-td edited" td-num="${editNum*7+wd}" td-time="${wd+'-4-'+i}" batchIds="${tdBatchIds!}">
						<div></div>
						<#--周几-上下午-节次-->
						<#if strs?exists && strs?size == 3>
							<p class="tip">
							<#if strs[1]?default('') != ''>
							<span >${strs[1]}</span>
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
				
			</tbody>
			</table>
		</form>
		</div>
	</div>
	</div>
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
				<#--<table width="100%" class="box-line bg-warning">
					<tbody>
					<tr>
						<th colspan="2">
							<span class="float-left">选考1（星期二第一节）</span>
							<a class="float-right" href="javascript:;"><i class="glyphicon glyphicon-trash"></i></a>
						</th>
					</tr>
					<tr>
						<td width="85" valign="top">不可排科目：</td>
						<td>物理</td>
					</tr>
					<tr>
						<td valign="top">不可排教师：</td>
						<td>小甜甜、小甜甜、小甜甜、小甜甜、小甜甜</td>
					</tr>
					</tbody>
				</table>-->
			</div>
		</div>
	</div>
	</div>	
<div class="navbar-fixed-bottom opt-bottom">
		<button class="btn btn-blue" id="confirmSubmitYsy">确定</button>
	<!--	<button class="btn btn-white">取消</button>  -->
	</div>
	<!--确认框内容 -->
	<#--data-toggle="modal" data-target="#confirm-submit"
	<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					请确认
				</div>
				<div class="modal-body">
					如果总课表中设置为不可排课，各科排课表中相应位置也将无法安排<br>
					确认提交结果吗？
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<a class="btn btn-blue btn-ok" id="submit" data-dismiss="modal" href="javascript:">提交</a>
				</div>
			</div>
		</div>
	</div>-->
	
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
		//whtml += '<tr><td width="100" valign="top">不可排科目：</td><td>'+wsubs+'</td></tr>';
		whtml += '<tr><td width="100" valign="top">不可排教师：</td><td>'+wteas+'</td></tr></tbody></table>';
		
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

	$('#table01').find('.edited:not(".active")').on('click', function(e){
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
				$(this).removeClass('active');
				refreshCounter($('.schedule-labels').find('[data-filter='+ tempLabel +']'), --counter);
			}
		} else {
			<#--if(counter <= 0 && type != 1){
				layerTipMsg(true,"提示","已无课时需要安排！");
				return false;
			}-->
			
			var objId = $('#objId'+type).val();
			var baIds = $(this).attr('batchIds');
			if(baIds.indexOf(objId) == -1){
				appendWarn(this, objId);
				return;
			}
			
			if($(this).find('label').length == 0){
				var label = getTdContent(this);
				$(this).children('div').empty().append(label);
				refreshCounter(currentLabel, ++counter);
			}else{
				if( tempLabel == type ){
					$(this).children('div').empty();
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
	
	<#--function getHeadInfNew(tdObj){
		var tr = $(tdObj).parents('tr:first')[0];
		var ri = tr.rowIndex;
		var iner = $(tr).attr('data-interval');
		return {value:iner,rowIndex:ri};
	}-->
	
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
		if(selContent.length>4){
			selContent=selContent.substring(0,4);
		}
		
		var join = $('#isJoin'+selType).val();
		var objId = $('#objId'+selType).val();
		var gtype = $('#groupType'+selType).val();
		var ttype = $('#timeType'+selType).val();
		
		var label = '<label class="schedule-label schedule-label-'+selType+' thin selected" data-label="'+selType+'">'+selContent+'</label>';
		
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
			});
	});
	
	//总课表提交结果  .btn-blue:first
	var hasSubmit=false;
	function saveGrade2(){
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
					layerTipMsg(false,"失败","");
				}
			});
	}
	
	//总课表提交结果  .btn-blue:first
	$('#confirmSubmitYsy').click(function(){
		layer.confirm("如果总课表中设置为不可排课，各科排课表中相应位置也将无法安排，确认提交结果吗？",function(index){
				saveGrade2();
			}
		);
	});
});
</script>