<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="explain">
		<p>剩余可排课课时：<span id="freeCount"></span>&emsp;&emsp;&emsp;&emsp;小提示：大于等于每周课时数，才有可能排课成功</p>
	</div>
		<form id="gradeTime" action="" method="POST">
		<input type="hidden" name="lessonArrayItemId" value="${array_item_id!}">
		<input type="hidden" name="objType" value="${objectType!}">
	<table class="table table-bordered layout-fixed table-editable" data-label="不可排课">
		<thead>
			<tr>
				<th width="8%" class="text-center"></th>
				<th width="4.5%" class="text-center"></th>
				<th width="12.5%" class="text-center">周一</th>
				<th width="12.5%" class="text-center">周二</th>
				<th width="12.5%" class="text-center">周三</th>
				<th width="12.5%" class="text-center">周四</th>
				<th width="12.5%" class="text-center">周五</th>
				<th width="12.5%" class="text-center">周六</th>
				<th width="12.5%" class="text-center">周日</th>
			</tr>
		</thead>
		<tbody>
		<#if amList?exists && amList?size gt 0>
		<#list amList as i>
			<tr>
				<#if (i=='1')>
					<td class="text-center" rowspan="${amList?size}">上午<input type="hidden" disabled name="period_interval" value="2" disable/></td>
				</#if>
				<td class="text-center">${i}</td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable <#if !(array_item_id?exists)>active</#if> ">
					
				</td>
				<td class="text-center editable <#if !(array_item_id?exists)>active</#if> ">
					
				</td>
			</tr>
		</#list>
		</#if>
		
		<#if pmList?exists && pmList?size gt 0>
		<#list pmList as i>
			<tr>
				<#if (i=='1')>
					<td class="text-center" rowspan="${pmList?size}">下午<input type="hidden" disabled name="period_interval" value="3" disable/></td>
				</#if>
				<td class="text-center">${i}</td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable <#if !(array_item_id?exists)>active</#if> ">
					
				</td>
				<td class="text-center editable <#if !(array_item_id?exists)>active</#if> ">
					
				</td>
			</tr>
		</#list>
		</#if>
		
		<#if nightList?exists && nightList?size gt 0>
		<#list nightList as i>
			<tr>
				<#if (i=='1')>
					<td class="text-center" rowspan="${nightList?size}">晚上<input type="hidden" disabled name="period_interval" value="4" disable/></td>
				</#if>
				<td class="text-center">${i}</td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable"></td>
				<td class="text-center editable <#if !(array_item_id?exists)>active</#if> ">
				
				</td>
				<td class="text-center editable <#if !(array_item_id?exists)>active</#if> ">
					
				</td>
			</tr>
		</#list>
		</#if>
			
		</tbody>
	</table>
	</form>
	<div class="text-right">
		<button class="btn btn-blue" data-toggle="modal" data-target="#confirm-submit">确定</button>
	<!--	<button class="btn btn-white">取消</button>  -->
	</div>
	<!--确认框内容 -->
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
	</div>
</div>
<script>
$(function(){
	//刚进入时就更新可排课课时数
	refreshLessonCount();
	
	$(".editable").each(function(i){
		//找出在一天中的那个时间段
		var rowHeadInf = getHeadInf(this.parentNode);
		var period_interval = rowHeadInf.value;
		//这个时间段中的第几节课
		var rowIndex = this.parentNode.rowIndex;
		var period = rowIndex-rowHeadInf.rowIndex+1;
		//找出是周几
		var weekday;
		if($(this).parent().children("[rowspan]").length>0){
			weekday = this.cellIndex-2;
		}else{
			weekday = this.cellIndex-1;
		}
		
		//插入<span>
		var content ='';
		var labledata = $(this).parents("table").first().data('label');
		
		//对每个单元格插入input和<span/>
		if($(this).hasClass('active')){
			content = '<span class="color-red">'+labledata+'</span>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].is_join" value="0"/>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].objId" value="${gradeId!}"/>';
		}else{
			content = '<span class="color-red" style="visibility:hidden;">'+labledata+'</span>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].is_join" value="1"/>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].objId" value="${gradeId!}"/>';
		}
		
		content = content+'<input type="hidden" name="lessonTimeDto['+i+'].weekday" value="'+weekday+'"/>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].period_interval" value="'+period_interval+'"/>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].groupType" value="1"/>'
					+'<input type="hidden" name="lessonTimeDto['+i+'].period" value="'+period+'"/>';
		
		$(this).html(content);
	});
	
	//对不排课的位置，模拟点击
	function imitateClick(lessonInfJson){
		var length = lessonInfJson.length;
		$.each(lessonInfJson,function(i,e){
			var period_interval = e.period_interval;
			var period = e.period;
			var weekday = e.weekday;
			//找到period_interval所在行，加上period-1，获得行数
			var rowIndex = $("[name='period_interval'][value='"+period_interval+"']:first").parent().parent()[0].rowIndex+period-1;
			//weekday+1为列数，如果period不为1，则为weekday
			
			var	colIndex = weekday;
			
			//通过获取tr和td来定位
			var $tr = $("[name='period_interval'][value='"+period_interval+"']:first").parents("tr");
			if(period == 1){
				//alert("period = "+period);
			}else{
				for(var i=0;i<(period-1);i++){
					$tr = $tr.next("tr");
				}
			}
			
			$parm = $tr.find(".editable:eq("+weekday+")");
			$parm.click();
			
		});
	}
	//如果是编辑功能，页面加载完毕时，获取课时安排数据，并且模拟点击
	$(function(){
		var array_item_id = $("input[name='array_item_id']").val();
		if(array_item_id==""){
			//alert("this is add");
			return;
		}
		var url = "${request.contextPath}/newgkelective/"+array_item_id+"/lessonTimeinf/json";
		$.post(url, 
			function(data){
				var dataJson = $.parseJSON(data);
				//模拟点击总课表
				imitateClick(dataJson.lessonTimeDtosJson);
				
			});
	});
	
	//总课表提交结果  .btn-blue:first
	$("#submit").click(function(){
		var divide_id = '${divide_id!}';
		var array_item_id = '${array_item_id!}';
		
		var url = "";
		var flag = "";
		if(array_item_id==""){
			url = '${request.contextPath}/newgkelective/'+divide_id+'/addLessonTimeTable';
			flag = "isAdd";
		}else{
			url = '${request.contextPath}/newgkelective/'+array_item_id+'/updateLessonTimeTable';
			flag = "isUpdate";
		}
		var params = $("#gradeTime").serialize();
		
		$.post(url, 
			params, 
			function(data){
				var dataJson = $.parseJSON(data);
				
				if(flag=="isAdd"){
					//插入arrayItemId到隐藏<input/>
					var arrayItemId = dataJson.arrayItemId;
					$("[name='array_item_id']").val(arrayItemId);
				}
				
				var msg = dataJson.msg;
				if(msg=="SUCCESS"){
					layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
				}else{
					layerTipMsg(false,"失败","");
				}
			});
		
	});
});
</script>