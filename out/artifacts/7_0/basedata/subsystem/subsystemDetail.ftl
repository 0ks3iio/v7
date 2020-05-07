<#import "/fw/macro/webmacro.ftl" as w>
<title>科目信息</title>
<#assign o = subsystem />
<div class="row subsystemDetail" style="margin:10px;">
		<div class="clearfix">
			<fieldset>
				<legend>应用系统信息</legend>
				<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fields cols=1/>
			</fieldset>
		</div>
		<div class="clearfix form-actions center">
			<@w.btn btnId="btn-commit" btnClass="fa-check" btnValue="确定"  />
			<@w.btn btnId="btn-cancel" btnClass="fa-times" btnValue="取消" />
		</div>
</div><!-- /.row -->

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
var scripts = [];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {

	// 取消按钮操作功能
	$("#btn-cancel").on("click", function(){
		layer.closeAll();
	 });
	// 确定按钮操作功能
	$("#btn-commit").on("click", function(){
		$(this).addClass("disabled");
		var check = checkValue('.subsystemDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	return;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.grade，是因为url所对应的接收对象是个dto，数据是存在dto.grade
		obj = JSON.parse(dealValue('.subsystemDetail'));
		// 提交数据
	 	$.ajax({
	 		<#if subsystem.intId?exists>
		    url:'${request.contextPath}/basedata/subsystem/update',
		    <#else>
		    url:'${request.contextPath}/basedata/subsystem/add',
		    </#if>
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    			$("#btn-commit").removeClass("disabled");
	    			});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#btn-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				setTimeout(function(){layer.closeAll();}, 300);
	 				$("#subsystemList").trigger("reloadGrid");
    			}
		     }
		});
	 });
});
</script>
