<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="tab-content">
	<div id="a1" class="tab-pane active">
		<div class="form-horizontal" role="form">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">申请类型：</label>
				<div class="col-sm-6">
					<p>
						<label class="inline">
							<input type="radio" class="wp" checked="checked" name="type" value="1"/>
							<span class="lbl"> 代课</span>
						</label>
						<label class="inline">
							<input type="radio" class="wp" name="type" value="2"/>
							<span class="lbl"> 管课</span>
						</label>
					</p>
				</div>
			</div>						
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">时间：</label>
				<div class="col-sm-9">
						<label class="inline pull-left">
							<input type="radio" class="wp" name="searchType" checked value="1"/>
							<span class="lbl"> 按周次</span>
						</label>
						<select id="weekId" class="pull-left form-control width150 mr70" onchange="doSearchSchedule(1)">
							<#if weekList?? && weekList?size gt 0>
							<#list weekList as week>
							<option value="${week!}">第${week!}周</option>
							</#list>
							</#if>
						</select>
						<label class="inline pull-left">
							<input type="radio" class="wp" name="searchType" value="2"/>
							<span class="lbl"> 自定义</span>
						</label>
						<div class="filter-item">
							<div class="filter-content">
								<div class="input-group float-left"">
									<input id="startDate" autocomplete="off" name="startDate" class="form-control datepicker" disabled style="width:120px" type="text" nullable="false"  placeholder="开始日期" value="${startDate?string('yyyy-MM-dd')!}" />
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</div>
								<span class="float-left mr10 ml10"> 至 </span>
								<div class="input-group float-left">
									<input id="endDate" autocomplete="off" name="endDate" class="form-control datepicker" disabled style="width:120px" type="text" nullable="false"  placeholder="结束日期" value="" />
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</div>
							</div>
						</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">节次：</label>
				<div class="col-sm-6">
					<table class="table table-bordered table-striped table-hover" id="hasData">
						<thead>
							<tr>
								<th width="50">
									<label><input type="checkbox" id="selectAll" class="wp"><span class="lbl"></span></label>
								</th>
								<th>时间</th>
								<th>节次</th>
								<th>班级</th>
								<th>课程</th>
							</tr>
						</thead>
						<tbody id="csList">
						</tbody>
					</table>
					<div class="no-data-container " style="display:none" id="noData">
						<div class="no-data">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt">暂无相关数据</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">备注：</label>
				<div class="col-sm-6">
					<input type="text" name="remark" class="form-control" placeholder="">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"></label>
				<div class="col-sm-6">
					<button class="btn btn-blue" onclick="doSave()">确认</button>
					<button class="btn btn-white" onclick="doCancel()">取消</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
$(function(){
	$('input[name="searchType"]').on("click",function(){
		var searchType=$('input[name="searchType"]:checked').val();
		if(searchType=='1'){
			$("#startDate").prop('disabled',true);
			$("#endDate").prop('disabled',true);
			$("#weekId").prop('disabled',false);
		}else{
			$("#startDate").prop('disabled',false);
			$("#endDate").prop('disabled',false);
			$("#weekId").prop('disabled',true);
		}
		doSearchSchedule(searchType);
	})

	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true,
		orientation:'bottom',
		startDate:'${startDate?string('yyyy-MM-dd')!}',
		endDate:'${endDate?string('yyyy-MM-dd')!}'
	}).next().on('click', function(){
		$(this).prev().focus();
	});

	$("#startDate").on("change",function(){
		doSearchSchedule(2);
	})
	
	$("#endDate").on("change",function(){
		doSearchSchedule(2);
	})
	
	$("#selectAll").on("click",function(){
		if($(this).prop("checked")){
			$('input[name="courseScheduleIds"]').prop("checked",true);
		}else{
			$('input[name="courseScheduleIds"]').prop("checked",false);
		}
	})
	
	$('input[name="searchType"][value="1"]').click();
})

function doBind(){
	$('input[name="courseScheduleIds"]').on("click",function(){
		if($('input[name="courseScheduleIds"]:checked').length==$('input[name="courseScheduleIds"]').length){
			$("#selectAll").prop("checked",true);
		}else{
			$("#selectAll").prop("checked",false);
		}
	})
}

function CompareDate(d1,d2){
	return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
}

function doSearchSchedule(searchType){
	var week = '';
	var startDate = '';
	var endDate = '';
	if('1'==searchType){
		week = $("#weekId").val();
	}else{
		startDate = $("#startDate").val();
		endDate = $("#endDate").val();
		if(startDate==''){
			layer.tips('请选择开始日期！', $("#startDate"), {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(CompareDate('${startDate?string('yyyy-MM-dd')!}',startDate)){
			layer.tips('开始日期不能小于当天日期，请重新选择！', $("#startDate"), {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(endDate==''){
			layer.tips('请选择结束日期！', $("#endDate"), {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(CompareDate(startDate, endDate)){
			layer.tips('开始日期不能大于结束日期，请重新选择！', $("#endDate"), {
					tipsMore: true,
					tips:3				
				});
			return;
		}
	}
	$.ajax({
		url:'${request.contextPath}/basedata/tipsay/applyArrange/getScheduleList',
		data:{'acadyear':'${acadyear!}','semester':'${semester!}','searchType':searchType,'week':week,'startDate':startDate,'endDate':endDate},
		type:'post', 
		dataType:'json',
		success:function(jsonO){
			$("#csList").html('');
			var jsonArr = jsonO.jsonArr;
			if(jsonArr!=undefined){
				var html = '';
				for(var i=0;i<jsonArr.length;i++){
					html+='<tr><td><label><input type="checkbox" name="courseScheduleIds" value="'+jsonArr[i].id+'" class="wp"><span class="lbl"></span></label></td>'
					html+='<td>'+jsonArr[i].date+'</td>';
					html+='<td>'+jsonArr[i].period+'</td>';
					html+='<td>'+jsonArr[i].className+'</td>';
					html+='<td>'+jsonArr[i].subjectName+'</td></tr>';
				}
				$("#csList").append(html);
				$("#hasData").show();
				$("#noData").hide();
				doBind();
			}else{
				$("#hasData").hide();
				$("#noData").show();
			}
		}
	});	
}

function doCancel(){
	showList(1);
}

var isSubmit=false;
function doSave(){
	if(isSubmit){
		return;
	}
	var type = $('input[name="type"]:checked').val();
	var csObj = $('input[name="courseScheduleIds"]:checked');
	if(csObj.length==0){
		layer.alert('请至少选择一个节次！',{icon:7});
		isSubmit=false;
		return;
	}
	var courseScheduleIds = [];
	csObj.each(function(){
		courseScheduleIds.push($(this).val());
	})
	var remark = $('input[name="remark"]').val();
    if (getByteLen(remark) > 500) {
        layer.alert('备注不能超过500个字符',{icon:7});
        isSubmit=false;
        return;
    }
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/basedata/tipsay/applyArrange/doSave',
		data:{'type':type,'courseScheduleIds':courseScheduleIds.join(","),'remark':remark},
		type:'post', 
		dataType:'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				isSubmit=true;
				showList(1);
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
			layer.close(ii);
		}
	});
}

function getByteLen(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var a = str.charAt(i);
        if (a.match(/[^\x00-\xff]/ig) != null) {
            len += 2;
        } else{
            len += 1;
        }
    }
    return len;
}

</script>