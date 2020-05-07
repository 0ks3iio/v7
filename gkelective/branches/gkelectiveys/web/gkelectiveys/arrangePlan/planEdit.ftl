<script src="${request.contextPath}/gkelectiveys/js/myscriptCommon.js"></script> 
<script src="${request.contextPath}/gkelectiveys/openClassArrange/openClassArrange.js"></script>
<a href="javascript:" class="page-back-btn gotoLcIndex"><i class="fa fa-arrow-left"></i>返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}---新增安排方案</h4>
	</div>
	<div class="box-body">
		<form id="myform" class="planDetail">
			 <div class="filter filter-f16">
	            <div class="filter-item">
	                <span class="filter-name">学年学期：</span>
					<div class="filter-content">
						<select class="form-control" name="acadyear" id="acadyear" nullable="false" onchange="findStartEndTime()" style="width:150px">
							<#if acadyearList?? && (acadyearList?size>0)>
								<#list acadyearList as item>
									<option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}学年</option>
								</#list>
							<#else>
								<option value="">暂无数据</option>
							</#if>
						</select>
					</div>
					<div class="filter-content">
						<select class="form-control" id="semester" name="semester" nullable="false" onchange="findStartEndTime()">
							${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
						</select>
					</div>
	            </div>
	            <div class="filter-item">
	            	<span class="filter-name">上课时间段：</span>
					<div class="filter-content">
						<div class="input-group">
							<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 146px" type="text" nullable="false" name="startTime" id="startTime" placeholder="开始时间" value="${startDate!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					</div>
					<div class="filter-content">
						<div class="input-group">
							<input class="form-control date-picker endTime-date date-picker-time" vtype="data" style="width: 146px" type="text" nullable="false" name="endTime" id="endTime" placeholder="结束时间" value="${endDate!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					</div>
					<div class="filter-content">
						<a class="btn btn-blue" id="plan-commit" href="javascript:" onclick="savePlan()" style="margin-left:10px;">保存</a>
	            	</div>
	            </div>
	            <div class="filter-item">
	            	<span class="filter-name">选择开班轮次：</span>
					<div class="filter-content">
						<select class="form-control" name="roundsId" id="roundsId" nullable="false" onchange="findByRound()">
							<#if roundslist?? && (roundslist?size>0)>
								<#list roundslist as item>
									<option value="${item.id}">第${item.orderId}轮</option>
								</#list>
							<#else>
								<option value="">暂无数据</option>
							</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<em>说明：一旦保存，该方案上课时间不能进行修改并且上课时间范围内不能再次安排。</em>
				</div>
	        </div>
        </form>
		<div id="itemShowHeadId">
		
		</div>
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var contextPath = '${request.contextPath}';
	var arrangeId = '${arrangeId!}';
	
	function findStartEndTime(){
		//用于页面判断开始与结束时间在不在学期内
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		//学年学期的开始时间与结束时间
		$.ajax({
		    url:'${request.contextPath}/gkelective/${arrangeId}/arrangePlan/findSemester',
		    data: {'acadyear':acadyear,'semester':semester},
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	$('.startTime-date').val(jsonO.startDate1);
		    	$('.endTime-date').val(jsonO.endDate1);
		    	$('.date-picker-time').datetimepicker('setStartDate',jsonO.startDate);
				$('.date-picker-time').datetimepicker('setEndDate',jsonO.endDate);
		    }
		});
	}
	
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2',
			'startDate' : '${startDate!}',
			'endDate' : '${endDate!}',
		};
		initCalendarData("#myform",".date-picker",viewContent);
		findByRound();
		
		
		$('.gotoLcIndex').on('click',function(){
			toback();
		});
	});
	
	function toback(){
		var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/index/page';
		$("#showList").load(url);
	}
	
	var isSubmit=false;
	function savePlan(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var check = checkValue('.planDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
		// 提交数据
		var ii = layer.load();
		var options = {
			url : '${request.contextPath}/gkelective/${arrangeId}/arrangePlan/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
					layerTipMsg(data.success,"成功",data.msg);
				  	toback();
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			$("#plan-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){} 
		};
		$("#myform").ajaxSubmit(options);
	}
	function findByRound(){
		var roundsId=$("#roundsId").val();
		var url='${request.contextPath}/gkelective/${arrangeId}/arrangePlan/editHead?roundsId='+roundsId;
		$("#itemShowHeadId").load(url);
	}

</script>
