<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs" role="tablist">
			<li class="active" role="presentation"><a href="#aa" onclick="itemShowList('1')"  role="tab" data-toggle="tab">学生刷卡日志</a></li>
			<li role="presentation"><a href="#bb" onclick="itemShowList('2')" role="tab" data-toggle="tab">教师刷卡日志</a></li>
		</ul>
		<div id="itemShowDivId">

		</div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
		itemShowList('1');
	});
	function itemShowList(tabIndex){
		if(!tabIndex || tabIndex == '1'){
			var url =  '${request.contextPath}/eccShow/eclasscard/studentClockInHead';
		}else if(tabIndex == '2'){
			var url =  '${request.contextPath}/eccShow/eclasscard/teacherClockInHead';
		}
		$("#itemShowDivId").load(url);
	}
</script>