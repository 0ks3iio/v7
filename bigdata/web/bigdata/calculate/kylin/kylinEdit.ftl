<form id="submitForm">
<input type="hidden" name="id" value="${kylin.id!}">
<input type="hidden" name="etlType" value="${kylin.etlType!}">
<input type="hidden" name="jobType" value="${kylin.jobType!}">
<input type="hidden" name="jobCode" value="${kylinCube.name!}">
    <div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">Cube名称：</label>
			<div class="col-sm-6">
			<label>${kylinCube.name!}</label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-6">
				<input type="text" name="name" id="name" class="width-1of1 form-control" nullable="false" maxLength="36" value="${kylin.name!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>是否定时执行：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if kylin.isSchedule?default(0) ==1>checked="true"</#if> class="wp wp-switch js-isSchedule" type="checkbox">
					<span class="lbl"></span>
				</label>
				<input type="hidden" id="isSchedule" name="isSchedule" value="${kylin.isSchedule?default(0)}">
			</div>
		</div>
		
		<div class="form-group form-shechule-param <#if kylin.isSchedule?default(0) ==0>hidden</#if>" >
			<label class="col-sm-2 control-label no-padding-right">定时执行参数：</label>
			<div class="col-sm-6">
				<input type="text" name="scheduleParam" id="scheduleParam" class="form-control" nullable="true" maxLength="100" value="${kylin.scheduleParam!}">
			</div>
			<button class="btn btn-blue" type="button" id="paramBtn">参数配置实例</button>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">备注：</label>
			<div class="col-sm-6">
				<input type="text" name="remark" id="remark" class="form-control" nullable="true" maxLength="200" value="${kylin.remark!}">
			</div>
		</div>

		<div class="form-group">
	        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
	        <div class="col-sm-6" >
	            <button type="button" class="btn btn-long btn-blue js-added" id="saveKylinBtn">&nbsp;保存&nbsp;</button>
	        </div>
	    </div>
</div>
<div id="paramDiv"></div>
</form>
<script>
	var isSubmit=false;
	$('.js-isSchedule').on('click',function(){
		if($(this).prop('checked')===true){
			$('.form-shechule-param').removeClass('hidden');
			$("#isSchedule").val("1");
		}else{
			$('.form-shechule-param').addClass('hidden');
			$("#isSchedule").val("0");
		}
	})

	$("#saveKylinBtn").on("click",function(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		if($('.js-isSchedule').prop('checked')===true){
			if($('#scheduleParam').val() ==""){
				layer.tips("不能为空", "#scheduleParam", {
					tipsMore: true,
					tips:3				
				});
				isSubmit=false;
			 	return;
		 	}
		}
		
		var options = {
				url : "${request.contextPath}/bigdata/calculate/kylin/save",
				dataType : 'json',
				success : function(data){
			 		if(!data.success){
			 			layerTipMsg(data.success,data.msg,"");
			 		}else{
			 			layer.msg(data.msg, {offset: 't',time: 2000});
			 			showList('2')
	    			}
	    			isSubmit = false;
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#submitForm").ajaxSubmit(options);
	});

	$("#paramBtn").on("click",function(){
		var url =  '${request.contextPath}/bigdata/calculate/paramView';
		 $("#paramDiv").load(url,function(){
	        layer.open({
	            type: 1,
	            shade: .5,
	            title: ['参数配置实例说明','font-size:16px'],
	            area: ['900px','600px'],
	            maxmin: false,
	            btn:['确定'],
	            content: $('#paramDiv'),
	            resize:true,
	            yes:function (index) {
	                layer.closeAll();
	                $("#paramDiv").empty();
	            },
	            cancel:function (index) {
	                layer.closeAll();
	                $("#paramDiv").empty();
	            }
	        });
	        $("#paramDiv").parent().css('overflow','auto');
	    })
	});
</script>