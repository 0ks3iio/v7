<style>
.w-e-text-container{
    height: 600px !important;/*!important是重点，因为原div是行内样式设置的高度300px*/
}
</style>
<form id="nodeSubmitForm">
<input type="hidden" name="id"  id="id" value="${node.id!}">
<input type="hidden" name="status" id="status" value="${node.status!}">
<div class="">
	<div class="form-horizontal form-made form-made-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-8">
				<input type="text" id="name" class="form-control" maxlength="25" value="${node.name!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>域名：</label>
			<div class="col-sm-8">
				<input type="text" id="domain" class="form-control" maxlength="25" value="${node.domain!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>端口：</label>
			<div class="col-sm-8">
				<input type="text" id="port" class="form-control" maxlength="9" value="${node.port!}" oninput = "value=value.replace(/[^\d]/g,'')">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>用户名：</label>
			<div class="col-sm-8">
				<input type="text" id="username" class="form-control" maxlength="25" value="${node.username!}">
			</div>
		</div>
		<div class="form-group" style="color:#ff0000">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>密码：</label>
			<div class="col-sm-8">
				<input type="password" id="password" class="form-control" maxlength="25" value="${node.password!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">备注：</label>
			<div class="col-sm-8">
				<textarea id="remark" maxlength="250" class="form-control" style="height:100px">${node.remark!}</textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
			<div class="col-sm-8">
				<button type="button" class="btn btn-long btn-blue" id="testConnect" onclick="nodeConnect();">&nbsp;测试连接&nbsp;</button>
				<#--<button type="button" class="btn btn-long btn-blue hide" id="saveNode" onclick="saveNodeInfo();">&nbsp;保存&nbsp;</button>-->
				<button type="button" class="btn btn-long btn-blue" id="backToNodeList" onclick="backToNode();">&nbsp;返回&nbsp;</button>
			</div>
		</div>
	    </div>
	</div>
</form>

<script type="text/javascript">

	var isSubmit=false;

	function nodeConnect() {
		if (isSubmit) {
			return;
		}
		isSubmit=true;
		if(!check()){
			isSubmit=false;
			return;
		}
		layer.msg('正在尝试连接，请稍后', {
			offset: 't',
			time: 30000
		});
		var params = {
			id:$("#id").val(),
			name:$("#name").val(),
			domain:$("#domain").val(),
			port:$("#port").val(),
			username:$("#username").val(),
			password:$("#password").val(),
			remark:$("#remark").val(),
			status:$("#status").val()
		};
		$.ajax({
			url: '${request.contextPath}/bigdata/node/testConnect',
			type: 'POST',
			data: {
				domain:params.domain,
				port:params.port,
				username:params.username,
				password:params.password
			},
			success: function (result) {
				if (!result.success) {
					params.status=0;
					layer.closeAll();
					isSubmit=false;
					showConfirmTips('error',"提示","连接失败，是否保存节点信息",function(){
						saveNodeInfo(params);
					});
				} else {
					params.status=1;
					isSubmit=false;
					showConfirmTips('success',"提示","连接成功，是否保存节点信息",function(){
						saveNodeInfo(params);
					});
				}
			}
		});
	}

	function saveNodeInfo(params){
		if (params.id===""||typeof (params.id)=="undefined"){
			$.ajax({
				url: '${request.contextPath}/bigdata/node/addNode',
				type: 'POST',
				data: params,
				success: function (result) {
					if (!result.success) {
						showLayerTips('error',result.message,'t');
						isSubmit=false;
					} else {
						showLayerTips('success','保存成功!','t');
						isSubmit=false;
						router.go({
							path: '/bigdata/node/index'
						});
						var url = '${request.contextPath}/bigdata/node/index';
						$('.page-content').load(url);
					}
				},
				clearForm : false,
				resetForm : false,
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
			});
		}else {
			$.ajax({
				url: '${request.contextPath}/bigdata/node/editNode',
				type: 'POST',
				data: params,
				success: function (result) {
					if (!result.success) {
						showLayerTips('error',result.message,'t');
						isSubmit=false;
					} else {
						showLayerTips('success','保存成功!','t');
						isSubmit=false;
						router.go({
							path: '/bigdata/node/index'
						});
						var url = '${request.contextPath}/bigdata/node/index';
						$('.page-content').load(url);
					}
				},
				clearForm : false,
				resetForm : false,
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
			});
		}
	}

	function backToNode() {
		if (isSubmit){
			return;
		}
		layer.closeAll();
		router.go({
			path: '/bigdata/node/index'
		});
	}

	function check() {
		if ($('#name').val() == "") {
			layer.tips("不能为空", "#name", {
				tipsMore: true,
				tips: 3
			});
			return false;
		}

		if ($('#domain').val() == "") {
			layer.tips("不能为空", "#domain", {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
		// var reg =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		// if (!reg.test($('#domain').val())){
		//     layer.tips("请输入合法的域名，格式:{0-255}.{0-255}.{0-255}.{0-255}", "#domain", {
		//         tipsMore: true,
		//         tips: 3
		//     });
		//     return false;
		// }

		if ($('#port').val() == "") {
			layer.tips("不能为空", "#port", {
				tipsMore: true,
				tips: 3
			});
			return false;
		}

		if ($('#username').val() == "") {
			layer.tips("不能为空", "#username", {
				tipsMore: true,
				tips: 3
			});
			return false;
		}

		if ($('#password').val() == "") {
			layer.tips("不能为空", "#password", {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
		return true;
	}

</script>