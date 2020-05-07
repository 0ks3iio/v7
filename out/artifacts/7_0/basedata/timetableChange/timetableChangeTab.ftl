<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li <#if tabIndex?default('1')=='1'>class="active"</#if> role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="itemShowList(1)">代管月统计</a></li>
			<li role="presentation" <#if tabIndex?default('1')=='2'>class="active"</#if>><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="itemShowList(2)">调代管统计</a></li>
		</ul>
		<div class="tab-content" id="itemShowDiv">
		
		</div>
	</div>
</div>


<script>
$(function(){
	itemShowList(${tabIndex?default(1)});
});
	
function itemShowList(tabType){
	var url = '';
	if(tabType == 1){
		url =  '${request.contextPath}/basedata/timetableChange/monthCount/index/page';
	}else if(tabType == 2){
		url =  '${request.contextPath}/basedata/timetableChange/dayCount/index/page';
	}
    $("#itemShowDiv").load(url);
}
</script>