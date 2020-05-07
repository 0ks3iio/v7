<#import "/fw/macro/webmacro.ftl" as w>
<style>
#tongyongDivCopy .form-control{
	width:260px;
}
</style>
<div class="row copyDetail" style="margin-top:10px;">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
			<div class="win-box">
				<div class="win-header">
					<h4 class="win-title">选择复用数据</h4>
				</div>
				<div class="win-body" id="tongyongDivCopy">
					<div class="filter">
						<div class="filter-item block">
							<label for="" class="filter-name">考试选择：</label>
							<div class="filter-content">
								<select class="form-control" id="examIdSearchCopy" >
									<#if examInfoList?? && (examInfoList?size>0)>
										<option value="">--请选择以往考试--</option>
										<#list examInfoList as item>
											<#if item.id?default('a') != examId?default('b')>
											<option value="${item.id!}">${item.examNameOther!}</option>
											</#if>
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
<div class="layer-footer">
	<button class="btn btn-lightblue" id="copy-commit">确定</button>
	<button class="btn btn-grey" id="copy-close">取消</button>
</div>
<!-- /.row -->



<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
$(function(){
	// 取消按钮操作功能
	$("#copy-close").on("click", function(){
		doLayerOk("#copy-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	var isSubmit=false;
	$("#copy-commit").on("click", function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var examIdSearchCopy = $("#examIdSearchCopy").val();
		if(examIdSearchCopy ==''){
			layer.tips('不能为空!', $("#examIdSearchCopy"), {
				tipsMore: true,
				tips: 3
			});
			$(this).removeClass("disabled");
			isSubmit=false;
			return;
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/borderline/findCopy',
		    data: {'sourceExamId':oldExamId,'copyExamId':examIdSearchCopy},  
		    type:'post',  
		    dataType:'json',
		   	error:function(XMLHttpRequest, textStatus, errorThrown){
		   		 
		   	},
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.success){
		    		// 显示成功信息
		 			layer.tips(jsonO.msg, "#copy-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#copy-commit", {
						redirect:function(){},
						window:function(){
							setTimeout(function(){
								oldQuery();
								layer.closeAll();
							}, 2000);
			 			}
		 			});
		 			
		 		}
		 		else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
					$("#copy-commit").removeClass("disabled");
					isSubmit=false;
				}
				layer.close(ii);
		    }
		});
	 });
});	
</script>
