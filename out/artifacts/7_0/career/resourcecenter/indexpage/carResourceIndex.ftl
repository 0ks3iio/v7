<div class="page-content">
	<div class="row">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
					<div class="mb20 box-line text-center">
						<h3><b>${carResourceCenter.title}</b></h3>
						<p><span class="color-blue mr20">
						<#if carResourceCenter.resourceType == 1>	
                			志愿填报指导
                		<#elseif carResourceCenter.resourceType == 2>
                			选科指导
                		<#else>
                			自主招生指导
                		</#if>
						</span><span class="color-grey">${(carResourceCenter.creationTime?string('yyyy-MM-dd'))!}</span></p>
					</div>
					<div class="mx100">
						<#if carResourceCenter.videoUrl?exists>
						<div class="text-center mb20">
							<video src="${request.contextPath}${carResourceCenter.videoUrl!}" preload="meta" controlsList="nodownload" controls="controls" style="width:100%;"></video>
						</div>
						</#if>
						<#if carResourceCenter.content?exists>
						<label style="font-weight: normal;width:100%;" class="font-16 text-indent"><span class="lbl">${carResourceCenter.content!}</span></label>
						</#if>
					</div>
				</div>
			</div>

		</div><!-- /.col -->
	</div><!-- /.row -->
</div><!-- /.page-content -->
<script>
	$(function(){
		showBreadBack(goback,true,"资源中心");
	});
	
	function goback() {
		$("#showResourceTabDiv").attr("style","display:block");
		$("#showResourceDiv").attr("style","display:none");
		$("#showResourceDiv").html("");
	}
</script>