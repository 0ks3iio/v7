<div id="exportDiv">
		<p>
			<label>
				<span class="lbl"> <b>参考学校</b></span>
			</label>
		</p>
		<ul class="label-list clearfix">
			<#if schlist?exists && schlist?size gt 0>
			<#list schlist as sch>
				<li>
					<label>
						<input type="radio" name="schIds" class="wp" value="${sch.id!}">
						<span class="lbl"> ${sch.schoolName!}</span>
					</label>
				</li>
			</#list>
			</#if>
		</ul>
	<form id="templateform" name="templateform" method="post">
		<input type="hidden" name="examId" value="${examId!}">
		<input type="hidden" name="type" value="4">
		<input type="hidden" id="schIds" name="schIds" value="">
		<input type="hidden" id="flag" name="flag">
	</form>
	<div class="layer-footer">
		<a href="javascript:" class="btn btn-lightblue" onclick="exportHtmlToPdf();">确定</a>
		<a href="javascript:" class="btn btn-grey" id="export-close">取消</a>
	</div>
</div>
<script>
	$(function(){
		// 取消按钮操作功能
		$("#export-close").on("click", function(){
		     layer.closeAll();  
		});
	})
	var isSubmit=false;
	function exportHtmlToPdf(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var schIds=$("input:radio[name='schIds']:checked").val();
		if(schIds==undefined || schIds==''){
			layerTipMsg(false,"提示","请先选择导出学校！");
			isSubmit=false;
			return;
		}
		$("#schIds").val(schIds);
		var flag=false;
		if(confirm('是否覆盖已生成的pdf')){
			var flag=true;
		}
		$("#flag").val(flag);
		var templateform=document.getElementById('templateform');
		if(templateform){
			templateform.action="${request.contextPath}/exammanage/edu/examReports/exportHtmlToPdf/page";
			templateform.target="hiddenIframe";
			templateform.submit();
			isSubmit=false;
		}
	}
</script>


