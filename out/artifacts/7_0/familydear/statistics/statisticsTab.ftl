<div class="box box-default">
	<div class="box-body clearfix">
        <div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs nav-tabs-1">
					<li class="active">
						<a data-toggle="tab" href="#a1" onClick="searchBeginList('1')">走访见面统计</a>
					</li>
					<li class="">
						<a data-toggle="tab" href="#a2" onClick="searchBeginList('2')">办实事办好事统计</a>
					</li>
					<li class="">
						<a data-toggle="tab" href="#a3" onClick="searchBeginList('3')">每月活动统计</a>
					</li>
				</ul>
			</div>
		    <div class="tab-content" id="bmTblDiv"></div>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script>
$(function(){	
	searchBeginList('1');
});

function searchBeginList(index){
    var url = "${request.contextPath}/familydear/statistics/beginRegister?index="+index;
    $('#bmTblDiv').load(url);
}
</script>												