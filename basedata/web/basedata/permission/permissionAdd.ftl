<#import "/fw/macro/webmacro.ftl" as w>
<title>部门信息</title>
<#-- 从dto中获取部门对象o -->
<#assign o = permission />
<div class="row permissionDetail" style="margin-top:10px;">
		<#-- 数据内容 -->
		<div class="clearfix">
			<#-- 根据entity的注解等，生成默认的页面 -->
			<#-- 注意！！！！此处可以按照常规的写法，一个个属性放上去，不一定调用此宏 -->
			<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fields cols=2/>
		</div>
		<#-- 确定和取消按钮 -->
		<div class="clearfix form-actions center">
			<@w.btn btnId="permission-commit" btnClass="fa-check" btnValue="确定" />
			<@w.btn btnId="permission-close" btnClass="fa-times" btnValue="取消" />
		</div>
</div><!-- /.row -->

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
// 需要用到的js脚本，延迟加载
var scripts = [];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	// 初始化交互控件
	// initOperationCheck('.gradeDetail');
	// 取消按钮操作功能
	$("#permission-close").on("click", function(){
		layer.closeAll();
	 });
	// 确定按钮操作功能
	$("#permission-commit").on("click", function(){
		$(this).addClass("disabled");
		var check = checkValue('.permissionDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	return;
		}
		var obj = JSON.parse(dealValue('.permissionDetail'));
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.grade，是因为url所对应的接收对象是个dto，数据是存在dto.grade
		// obj.grade = JSON.parse(dealValue('.permissionDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/permission/save',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    			$("#permission-commit").removeClass("disabled");
	    			});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#permission-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
					gotoHash("${request.contextPath}/basedata/permission/index/page");
    			}
		     }
		});
	 });
});	
</script>
