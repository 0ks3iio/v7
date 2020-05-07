<form id="submitForm">
	<input type="hidden" name="id" value="${dataxJob.id!}">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-6">
				<input type="text" name="name" id="name" class="width-1of1 form-control" nullable="false" maxLength="36" value="${dataxJob.name!}">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>是否定时执行：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if dataxJob.isSchedule?default(0) ==1>checked="true"</#if> class="wp wp-switch js-isSchedule" type="checkbox">
					<span class="lbl"></span>
				</label>
				<input type="hidden" id="isSchedule" name="isSchedule" value="${dataxJob.isSchedule?default(0)}">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>执行节点：</label>
			<div class="col-sm-6">
				<select id="nodeSelect" name="nodeId" class="form-control">
					<#list nodeList as node>
						<option <#if node.id == dataxJob.nodeId?default('')>selected="selected"</#if> value="${node.id!}">${node.name!}</option>
					</#list>
				</select>
			</div>
		</div>

		<div class="form-group form-shechule-param <#if dataxJob.isSchedule?default(0) ==0>hidden</#if>" >
			<label class="col-sm-2 control-label no-padding-right">定时执行参数：</label>
			<div class="col-sm-6">
				<input type="text" name="scheduleParam" id="scheduleParam" class="form-control" nullable="true" maxLength="100" value="${dataxJob.scheduleParam!}">
			</div>
			<button class="btn btn-blue" type="button" id="paramBtn">参数配置实例</button>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">备注：</label>
			<div class="col-sm-6">
				<input type="text" name="remark" id="remark" class="form-control" nullable="true" maxLength="200" value="${dataxJob.remark!}">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
			<div class="col-sm-6" >
				<button type="button" class="btn btn-long btn-blue js-added" id="dataxSaveBtn">&nbsp;保存&nbsp;</button>
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
			$('.form-shechule-has-param').addClass('hidden');
			$("#isSchedule").val("1");
		}else{
			$('.form-shechule-param').addClass('hidden');
			$('.form-shechule-has-param').removeClass('hidden');
			$("#isSchedule").val("0");
		}
	})

	$("#dataxSaveBtn").on("click",function(){
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

		if ($('#nodeSelect').val() == null || $('#nodeSelect').val() == '') {
			layer.tips("请选择节点", "#nodeSelect", {
				tipsMore: true,
				tips:3
			});
			isSubmit=false;
			return;
		}

		var options = {
			url : "${request.contextPath}/bigdata/datax/job/saveJob",
			dataType : 'json',
			success : function(data){
				if(!data.success){
					showLayerTips4Confirm('error', data.msg);
					isSubmit = false;
				}else{
					showLayerTips('success', '保存成功', 't');
					router.go({
						path: '/bigdata/etl/datax',
						name: '数据同步',
						level: 2
					}, function () {
						$('.page-content').load('${request.contextPath}/bigdata/etl/datax');
					});
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
		};
		$("#submitForm").ajaxSubmit(options);
	});

	$("#paramBtn").on("click",function(){
		var url =  '${request.contextPath}/bigdata/etl/paramView';
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