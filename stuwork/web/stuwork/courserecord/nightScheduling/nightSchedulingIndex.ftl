<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="main-content-inner">
	<div class="page-content">
		<div class="box box-default">
			<div class="box-body" id = "showNightSchList">
			</div>
		</div>
	</div>
</div>
<script>
$(function(){
	showNigList();
});

function showNigList(){
    var url = "${request.contextPath}/stuwork/night/scheduling/head/page";
    $("#showNightSchList").load(url);
}

</script>


