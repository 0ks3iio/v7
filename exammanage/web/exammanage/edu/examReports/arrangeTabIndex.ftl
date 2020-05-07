<a href="#" class="page-back-btn gotoExamArrangeClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li <#if type?default("1")=="1">class="active" </#if>>
						<a  id="aa" data-toggle="tab" aria-expanded="true" onclick="itemShowList1(1)">考场门贴</a>
					</li>
					<li  <#if type?default("1")=="2">class="active" </#if>>
						<a  id="dd" data-toggle="tab" aria-expanded="true" onclick="itemShowList1('2')">考场对照单</a>
					</li>
					<li  <#if type?default("1")=="3">class="active" </#if>>
						<a  id="cc" data-toggle="tab" aria-expanded="true" onclick="itemShowList1('3')">考生桌贴</a>
					</li>
					<li  <#if type?default("1")=="4">class="active" </#if>>
						<a  id="ff" data-toggle="tab" aria-expanded="true" onclick="itemShowList1('4')">考生准考证</a>
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
	$('.gotoExamArrangeClass').on('click',function(){
		var url =  '${request.contextPath}/exammanage/edu/examReports/head/page';
		$("#examArrangeDiv").load(url);
	});
});

function itemShowList(type){
	if("1"==type){
	
	}else if("2" == type){
	
	}else if("3" == type){
		var url =  '${request.contextPath}/exammanage/edu/examArrange/seatSetting/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}
	
}


</script>

