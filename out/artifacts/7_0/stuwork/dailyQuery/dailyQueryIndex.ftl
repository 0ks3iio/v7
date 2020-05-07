<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">日期：</span>
				<div class="filter-content">
					<div class="input-group">
						<input type="text" style="width:120px" id="queryDate" onChange="searchCls();" class="form-control datepicker" value="${(nowDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学段：</span>
				<div class="filter-content">
					<select name="section" id="section" class="form-control" onchange="searchCls()">
						<#list sections as sec>
							<option value="${sec[0]}">${sec[1]}</option>
						</#list>		
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
                <div class="filter-content">
                    <a class="btn btn-white" onclick="exportCls();" >导出</a>
                </div>
            </div>
		</div>
		<div id="clsListDiv"></div>
	</div>
</div>	
<script>
$(function(){
	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	searchCls();
	<#--,
		minDate: '${(minDate?string('yyyy-MM-dd'))!}',
		maxDate: '${(maxDate?string('yyyy-MM-dd'))!}'-->
})

function searchCls(){
	var queryDate = $('#queryDate').val();
	var section = $('#section').val();
	var url = '${request.contextPath}/stuwork/dailyCheck/clsList/page?acadyear=${acadyear!}&semester=${semester!}&queryDate='+queryDate+'&section='+section;
	$('#clsListDiv').load(url);
}

function exportCls(){
	var queryDate = $('#queryDate').val();
	var section = $('#section').val();
	var url = '${request.contextPath}/stuwork/dailyCheck/clsList/export?acadyear=${acadyear!}&semester=${semester!}&queryDate='+queryDate+'&section='+section;
	window.open(url);
}
</script>