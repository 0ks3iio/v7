<#import "/fw/macro/webmacro.ftl" as w>
<#-- 从dto中获取部门对象o -->
<#assign o = dto.teachArea />
<div class="row teachAreaDetail" style="margin-top:10px;">
	<#-- 数据内容 -->
	<div class="clearfix">
		<#-- 根据entity的注解等，生成默认的页面 -->
		<#-- 注意！！！！此处可以按照常规的写法，一个个属性放上去，不一定调用此宏 -->
		<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fields cols=2/>
	</div>
</div>
<#-- 确定和取消按钮 -->
<div class="row" style="margin-top:10px;">
	<div class="clearfix form-actions center">
		<@w.btn btnId="teachArea-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="teachArea-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>



<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
// 需要用到的js脚本，延迟加载
var scripts = [null,
"${request.contextPath}/static/ace/components/moment/moment.js",
"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
"${request.contextPath}/static/ace/components/chosen/chosen.jquery.min.js",
"${request.contextPath}/static/js/validate.js",
null];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	// 初始化交互控件
	initOperationCheck('.teachAreaDetail');
	// 取消按钮操作功能
	$("#teachArea-close").on("click", function(){
		doLayerOk("#teachArea-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	var isSubmit=false;
	$("#teachArea-commit").on("click", function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var check = checkValue('.teachAreaDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.teachArea，是因为url所对应的接收对象是个dto，数据是存在dto.teachArea
		obj.teachArea = JSON.parse(dealValue('.teachAreaDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/teachArea/save',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({
			 			title: "操作失败!",
		    			text: jsonO.msg,
		    			type: "error",
		    			showConfirmButton: true,
		    			confirmButtonText: "确定"
		    		}, function(){
    					$("#teachArea-commit").removeClass("disabled");
    					isSubmit=false;
    				});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#teachArea-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#teachArea-commit", {
						redirect:function(){},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 2000);
							$("#teachAreaList").trigger("reloadGrid");
							if($("#schoolColl .fa").hasClass("fa-chevron-up")){
								$("#schoolColl").click();
							}
			 			}
		 			});
    			}
		     }
		});
	 });
});	
</script>
