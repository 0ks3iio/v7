<#import "/fw/macro/webmacro.ftl" as w>
<style>
#tongyongDivCopy .form-control{
	width:260px;
}
</style>
<div class="row teacherclassCopyDetail" style="margin-top:10px;">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
			<div class="win-box">
				<div class="win-header">
					<h4 class="win-title">选择复用数据</h4>
				</div>
				<div class="win-body" id="tongyongDivCopy">
					<div class="filter">
						<div class="filter-item block">
							<label for="" class="filter-name">教学班：</label>
							<div class="filter-content">
								<select class="form-control" id="teachclassid" >
									<#if tclist?? && (tclist?size>0)>
										<option value="">请选择${acadyear!}/第${semester!}学期</option>
										<#list tclist as item>
											<option value="${item.id!}">${item.name!}</option>
										</#list>
									<#else>
										<option value="">暂无数据</option>
									</#if>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
	</div>
</div>
<#-- 确定和取消按钮 -->
<div class="row" style="margin-top:10px;">
	<div class="clearfix form-actions center">
		<@w.btn btnId="teaclassCopy-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="teaclassCopy-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>
<!-- /.row -->



<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
// 需要用到的js脚本，延迟加载
var scripts = [];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	// 初始化交互控件
	initOperationCheck('.teacherclassCopyDetail');
	// 取消按钮操作功能
	$("#teaclassCopy-close").on("click", function(){
		doLayerOk("#teaclassCopy-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	var isSubmit=false;
	$("#teaclassCopy-commit").on("click", function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var teachclassid = $("#teachclassid").val();
		if(teachclassid ==''){
			layer.tips('不能为空!', $("#teachclassid"), {
				tipsMore: true,
				tips: 3
			});
			$(this).removeClass("disabled");
			isSubmit=false;
			return;
		}
		$.ajax({
		    url:'${request.contextPath}/basedata/teachclass/findTeachClaCopy',
		    data: {'teachclassid':teachclassid},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.success){
		    		// 显示成功信息
		 			layer.tips(jsonO.msg, "#teaclassCopy-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#teaclassCopy-commit", {
						redirect:function(){},
						window:function(){
							setTimeout(function(){
								selchange();
								layer.closeAll();
							}, 2000);
			 			}
		 			});
		 			
		 		}
		 		else{
					swal({title: "操作失败!",
						text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
					);
					$("#teaclassCopy-commit").removeClass("disabled");
					isSubmit=false;
				}
		    }
		});
	 });
});	
</script>
