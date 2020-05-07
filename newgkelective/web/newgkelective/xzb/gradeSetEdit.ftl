<div id="aa" class="tab-pane active" role="tabpanel">
	<#--div class="explain">
		<p>全部课时：${allPeriod!}，剩余课时<span id="freeCount"></span></p>
	</div-->
	<div class="table-container">
		<div class="table-container-header">
			<div class="schedule-labels js-schedule-labels" data-for="table01">
				<input type="hidden" name="filterType" id="type${gradeId!}" value="1">
				<input type="hidden" name="objId" id="objId1" value="${gradeId!}">
				<input type="hidden" name="groupType" id="groupType1" value="1">
				<input type="hidden" name="is_join" id="isJoin1" value="0">
				<input type="hidden" name="timeType" id="timeType1" value="01">
				<label class="schedule-label schedule-label-white schedule-label-1" id="data1" data-filter="1" data-filter-content="不可排课"><i class="fa fa-plus"></i>不可排课</label>
				
				<#if subs?exists && subs?size gt 0>
				<input type="hidden" name="filterType" id="type${fixGuid!}" value="2">
				<input type="hidden" name="objId" id="objId2" value="${fixGuid!}">
				<input type="hidden" name="groupType" id="groupType2" value="1">
				<input type="hidden" name="is_join" id="isJoin2" value="1">
				<input type="hidden" name="timeType" id="timeType2" value="02">
				<label class="schedule-label schedule-label-white schedule-label-2" id="data2" data-filter="2" data-objid="${fixGuid!}" data-filter-content="固定排课"><i class="fa fa-plus"></i>固定排课<span class="badge badge-yellow" id="freeCount${fixGuid!}">0</span></label>
				</#if>
				<label class="schedule-label schedule-label-white schedule-label-0" data-filter="0"><i class="fa fa-trash"></i>删除</label>
			</div>
		</div>
		<div class="table-container-body">
			<form id="gradeTime" action="" method="POST">
			<input type="hidden" name="needSource" value="true">
			<table id="table01" class="table table-bordered table-schedule" data-filter="100">
			<thead>
				<#assign ratio = (100-12.5)/weekDays>
				<tr>
					<th width="8%" class="text-center"></th>
					<th width="4.5%" class="text-center"></th>
					<#list 0..(weekDays-1) as day>
					<th width="${ratio}%" class="text-center">${dayOfWeekMap[day+""]}</th>
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
			    <tr data-interval="${piFlag}" class='tr_${piFlag}_${pIndex}' data-value="${pIndex}">
			    <#if pIndex == 1>
			    	<td rowspan="${interval!}" class="text-center">${intervalName!}<input type="hidden" disabled name="period_interval" value="${piFlag}" disable/></td>
			    </#if>
		        	<td class="text-center">${pIndex!}</td>
					<#list 0..(weekDays-1) as day>
					<td class="text-center edited <#if arrayItemId?default('') == '' && day gt 4>active</#if>" td-num="${editNum*(weekDays) + day}"></td>
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
<div class="navbar-fixed-bottom opt-bottom">
		<a src="javascript:void(0);" class="btn btn-blue" id="confirm-submit">保存</a>
	<!--	<button class="btn btn-white">取消</button>  -->
	</div>
	<!--确认框内容 -->
	<#-- data-toggle="modal" data-target="#confirm-submit"
	<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					请确认
				</div>
				<div class="modal-body">
					如果年级特征中设置为不可排课，总课表和各科排课表中相应位置也将无法安排<br>
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
			type=100;//没有选中
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
		var tempLabel = $(this).find('.label-children').attr("data-label");
		if (type == 100) {
			if($(this).children('.label-timeselect').length != 0){
				return;
			}
			layer.msg("请先选择需要进行的操作！", {
						offset: 't',
						time: 2000
					});
		} else if (type == 0){
			if($(this).children().length == 0){
				layer.msg("请选择其他操作！", {
						offset: 't',
						time: 2000
					});
				return false;
			}else{
				counter = $('.schedule-labels').find('[data-filter='+ tempLabel +']').find('.badge').text();
				$(this).empty();
				$(this).removeClass('active');
				refreshCounter($('.schedule-labels').find('[data-filter='+ tempLabel +']'), --counter);
			}
		} else {
			<#--if(counter <= 0 && type != 1){
				layerTipMsg(true,"提示","已无课时需要安排！");
				return false;
			}-->
			if($(this).children().length == 0){
				var label = getTdContent(this);
				$(this).empty().append(label);
				refreshCounter(currentLabel, ++counter);
			} else if(type==1){
				if( tempLabel == type ){
					$(this).empty();
					$(this).removeClass('active');
				}else{
					<#--
					var tempCounter = $('.schedule-labels').find('[data-filter='+ tempLabel +']').find('.badge').text();
					var label = getTdContent(this);
					$(this).empty().append(label);
					$('.schedule-labels').find('[data-filter='+ tempLabel +']').find('.badge').text(--tempCounter);-->
				}
			}
		};
	});
	
	function changeForSource(obj){
		var si = $(obj).attr('source-index');
		$('#sourceObjId'+si).val($(obj).val());
	}
	
	function getTdContent(obj, selType, selContent){
		if(!selType){
			selType = type;
		}
		if(!selContent){
			selContent = content;
		}
		var choseTr=$(obj).parents("tr");
		var period=parseInt($(choseTr).attr("data-value"));
		var period_interval=$(choseTr).attr("data-interval");

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
	
	function getTdContentStr(selType, selContent, tdi, weekday, period_interval, period,objId){
		if(selContent.length>7){
			selContent=selContent.substring(0,7);
		}
		
		var join = $('#isJoin'+selType).val();
		if(!objId){
			objId = $('#objId'+selType).val();
		}
		if(objId=='${fixGuid!}'){
			objId='';
		}
		var gtype = $('#groupType'+selType).val();
		var ttype = $('#timeType'+selType).val();
		var selCo = parseInt(selType);
		if(selCo>7){
			selCo = selCo-6;
		}
		var label = '<input type="hidden" name="lessonTimeDto['+tdi+'].weekday" value="'+weekday+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].period_interval" value="'+period_interval+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].period" value="'+period+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].is_join" value="'+join+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].timeType" value="'+ttype+'"/>';
		
		label = label + getObjStr(ttype,tdi,selType,selCo,selContent,objId)
			+'<input type="hidden" name="sourceTimeDto['+tdi+'].groupType" value="'+gtype+'"/>'
			+'<input type="hidden" name="sourceTimeDto['+tdi+'].is_join" value="'+join+'"/>';
			
		return label;
	}
	
	function getObjStr(ttype,tdi,selType,selCo,selContent,objId){
		var strs = '';
		if(ttype == '01'){
			strs = '<label class="schedule-label schedule-label-'+selCo+' thin selected label-children" data-label="'+selType+'">'+selContent+'</label>' 
			+'<input type="hidden" name="lessonTimeDto['+tdi+'].objId" value="'+objId+'"/>'
			+'<input type="hidden" name="sourceTimeDto['+tdi+'].objId" value="'+objId+'"/>';
		} else {
			strs = '<select name="lessonTimeDto['+tdi+'].objId" class="form-control label-children label-timeselect" source-index="'+tdi+'" onchange="$(\'#sourceObjId'+tdi+'\').val(this.value);" data-label="'+selType+'">';
			var sobjid = objId;
			<#list subs as sub>
			strs += '<option value="${sub.id!}"';
			if((objId!='' && '${sub.id!}'==objId) || (objId=='' && '${sub_index}'=='0')){
				strs+=' selected';
				sobjid = '${sub.id!}';
			}
			strs +='>${sub.subjectName!}</option>'
			</#list>
			strs += '</select>';
			strs += '<input type="hidden" name="sourceTimeDto['+tdi+'].objId" id="sourceObjId'+tdi+'" value="'+sobjid+'"/>'; 
		}
		return strs;
	}
	
	<#--$('.label-timeselect').change(function(){
		alert(11);
		var si = $(this).attr('source-index');
		$('#sourceObjId'+si).val($(this).val());
	});-->
	
	$("#table01 .active").each(function(i){
		var label = getTdContent(this, 1, '不可排课');
		$(this).empty().append(label);
	});
	
	<#-- 初始化数据组装，需要修改 -->
	//对不排课的位置，模拟点击
	function imitateClick(lessonInfJson){
		var length = lessonInfJson.length;
		//alert(length);
		$.each(lessonInfJson,function(i,e){
			var objId = e.objId;
			var period_interval = e.period_interval;
			var period = e.period;
			var weekday = e.weekday;
			
			//找到行
			var classNameStr='tr_'+period_interval+'_'+period;
			if($("."+classNameStr)){
				//所在行 
				var $tr = $("."+classNameStr);
				var typeobj = objId;
				if(typeobj != '${gradeId!}'){
					typeobj='${fixGuid!}';
				}
				var tdtype = $('#type'+typeobj).val(); 
				var tdlabel = $('#data'+tdtype);
				var tdcontent = $(tdlabel).data('filter-content');
				var tdobj = $tr.find(".edited:eq("+weekday+")");
				var tdi = $(tdobj).attr('td-num');
				var label = getTdContentStr(tdtype, tdcontent, tdi, weekday, period_interval, period,objId);
				$(tdobj).empty().append(label);
				
				var tempCounter = $(tdlabel).find('.badge').text();
				refreshCounter(tdlabel, ++tempCounter);
			}
			
			//找到period_interval所在行，加上period-1，获得行数
			//var rowIndex = $("[name='period_interval'][value='"+period_interval+"']:first").parent().parent()[0].rowIndex+period-1;
			//weekday+1为列数，如果period不为1，则为weekday
			
			//var	colIndex = weekday;
			
			//通过获取tr和td来定位 所时间段第一行 
			//var $tr = $("[name='period_interval'][value='"+period_interval+"']:first").parents("tr");
			//if(period != 1){
			//	for(var i=0;i<(period-1);i++){
			//		$tr = $tr.next("tr");
			//	}
			//}
			
		});
	}
	
	//如果是编辑功能，页面加载完毕时，获取课时安排数据，并且模拟点击
	$(function(){
		var array_item_id = $("input[name='array_item_id']").val();
		if(array_item_id==""){
			return;
		}
		var url = "${request.contextPath}/newgkelective/"+array_item_id+"/gradeLessonTimeinf/json";
		$.post(url, 
			function(data){
				var dataJson = $.parseJSON(data);
				//模拟点击总课表
				imitateClick(dataJson.lessonTimeDtosJson);
			});
	});
	
	function checkTimes(){
		return true;
	}
	
	var hasSubmit=false;
	function saveGrade(){
		if(hasSubmit){
			return;
		}
		
		hasSubmit=true;
		if(!checkTimes()){
			hasSubmit=false;
			return;
		}
		var divide_id = '${divideId!}';
		var array_item_id = $('[name="array_item_id"]').val();
		
		var url = "";
		var flag = "";
		if(array_item_id==""){
			url = '${request.contextPath}/newgkelective/'+divide_id+'/addLessonTimeTable';
			flag = "isAdd";
		}else{
			url = '${request.contextPath}/newgkelective/'+array_item_id+'/updateLessonTimeNew?objectType=${objectType!'0'}';
			flag = "isUpdate";
		}
		var params = $("#gradeTime").serialize();
		params += "&arrayId=" + arrayId;
		
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
		save_msg = '如果年级特征中设置为不可排课，总课表和各科排课表中相应位置也将无法安排，确认提交结果吗？';
	}
	$('#confirm-submit').click(function(){
		layer.confirm(save_msg,function(index){
				saveGrade();
			}
		);
	});
	
	$("#submit").click(function(){
		saveGrade();
	});
});
</script>