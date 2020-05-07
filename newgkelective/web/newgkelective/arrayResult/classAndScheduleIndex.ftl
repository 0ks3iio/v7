<!-- 时间 -->
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
	<div class="layer-body">
		<form id="myform" class="planDetail">
			 <div class="filter filter-f16">
	            <div class="filter-item">
	                <span class="filter-name">学年：</span>
					<div class="filter-content">
						<select class="form-control" name="acadyear" id="acadyear" nullable="false" onchange="findStartEndTime()" style="width:184px">
							<#if acadyearList?? && (acadyearList?size>0)>
								<#list acadyearList as item>
									<option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}学年</option>
								</#list>
							<#else>
								<option value="">暂无数据</option>
							</#if>
						</select>
					</div>
	            </div>
	            <div class="filter-item">
	                <span class="filter-name">学期：</span>
					<div class="filter-content">
						<select class="form-control" id="semester" name="semester" nullable="false" onchange="findStartEndTime()" style="width:184px">
							${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
						</select>
					</div>
	            </div>
	            <div class="filter-item">
	            	<span class="filter-name">上课开始时间：</span>
					<div class="filter-content">
						<div class="input-group">
							<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 146px" type="text" nullable="false" name="startTime" id="startTime" placeholder="开始时间" value="${workBegin!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					</div>
	            </div>
				<div class="filter-item">
				<span class="filter-name"><em>说明：</em></span>
					<em>默认上课结束时间为学期末</em>
				</div>
	        </div>
        </form>
	</div>
</div>
<div class="layui-layer-btn">
	<a class="layui-layer-btn0" id="arrange-commit">确定</a>
	<a class="layui-layer-btn1" id="arrange-close">取消</a>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	// 取消按钮操作功能
	$("#arrange-close").on("click", function(){
	    doLayerOk("#arrange-commit", {
	    redirect:function(){},
	    window:function(){layer.closeAll()}
	    });     
	 });
 
	function findStartEndTime(){
		//用于页面判断开始与结束时间在不在学期内
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		//学年学期的开始时间与结束时间
		$.ajax({
		    url:'${request.contextPath}/newgkelective/${arrayId}/saveClassAndSchedule/findSemester',
		    data: {'acadyear':acadyear,'semester':semester},
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	$('.startTime-date').val(jsonO.startDate1);
		    	$('.date-picker-time').datetimepicker('setStartDate',jsonO.startDate);
		    	if(jsonO.endDate!=""){
		    		$('.date-picker-time').datetimepicker('setEndDate',jsonO.endDate);
		    	}
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
	});
	
	$("#arrange-commit").on("click", function(){
		var ii;
		if(${divideType?default('')}==2){
	          ii=layer.confirm("当前方案已重组行政班，如果应用该方案，原行政班相关数据将会被解散，确定应用该方案吗？", {
	          title: ['提示','font-size:20px;'],
	          btn: ['确定','取消'] //按钮
	        }, function(){
	            layer.close(ii);
	            useThisArrayResult();
	        }, function(){
	            layer.closeAll();
	        });
		}else{
			useThisArrayResult();
		}
		
	})
	
	var isSubmit=false;
	function useThisArrayResult(){
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
			url : '${request.contextPath}/newgkelective/${arrayId}/saveClassAndSchedule/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
					layer.msg(data.msg, {offset: 't',time: 2000});
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
				<#if arrangeType?default('01')=='01'>
				var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/index/page?useMaster=1';
				<#else>
				var url =  '${request.contextPath}/newgkelective/xzb/index/page?useMaster=1';
				</#if>
				$("#showList").load(url);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){} 
		};
		$("#myform").ajaxSubmit(options);
	}

</script>
