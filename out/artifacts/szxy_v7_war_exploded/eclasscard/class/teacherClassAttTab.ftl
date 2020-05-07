<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
<div class="box-body">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">日期：</span>
			<div class="filter-content">
				<div class="input-group">
					<input type="text" id="selectDate" class="form-control datepicker" readonly="readonly" onchange="changeSelect()" value="${(nowDate?default(.now))?string('yyyy-MM-dd')}">
					<span class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</span>
				</div>
			</div>
		</div>
	</div>
	<div id="showListDiv">
	</div>
</div>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

<script type="text/javascript">
$(function(){
	var maxDate = "${(nowDate?default(.now))?string('yyyy-MM-dd')}";
	$('.datepicker').datepicker({
		endDate:maxDate,
		language: 'zh-CN',
    	format: 'yyyy-mm-dd',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
});

function changeSelect(){
	var date = $("#selectDate").val();
	var url =  '${request.contextPath}/eclasscard/teacher/signin/list?date='+date;
	$("#showListDiv").load(url);
}

</script>