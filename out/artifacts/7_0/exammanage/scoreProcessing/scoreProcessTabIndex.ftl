<a href="#" class="page-back-btn gotoScoreProcessingClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li <#if type?default("1")=="1">class="active" </#if>>
						<a href="#aa"  id="aa" data-toggle="tab" aria-expanded="true" onclick="itemShowList(1)">录分权限</a>
					</li>
					<li  <#if type?default("1")=="2">class="active" </#if>>
						<a href="#bb"  id="bb" data-toggle="tab" aria-expanded="true" onclick="itemShowList(2)">成绩录入</a>
					</li>
				</ul>
			</div>
			<div class="tab-content" id="showTabDiv">
			</div>
		</div>
	</div>
</div>
										

<!-- page specific plugin scripts -->
<script type="text/javascript">
$(function(){
	itemShowList('${type?default("1")}');
	$('.gotoScoreProcessingClass').on('click',function(){
		var url =  '${request.contextPath}/exammanage/scoreProcessing/head/page';
		$("#scoreProcessDiv").load(url);
	});
});
function itemShowList(type){
	if("1"==type){
		var url =  '${request.contextPath}/exammanage/scorePermission/index/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("2"==type){
		var url =  '${request.contextPath}/exammanage/scoreInput/index/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else{
		
	}
}

</script>

