<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="main-content">
	<div class="main-content-inner">
		<div class="page-content">
			<div class="box box-default">
				<div class="box-body">
					<ul class="nav nav-tabs" role="tablist">
						<li class="active" role="presentation"><a href="#aa" onclick="itemShowList('1')" role="tab" data-toggle="tab">我的上课日志</a></li>
						<li role="presentation"><a href="#bb" role="tab" onclick="itemShowList('2')" data-toggle="tab">全校上课日志</a></li>
					</ul>
					<div id="itemShowDivId">
		
	                </div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
		itemShowList('1');
	});
	function itemShowList(tabIndex){
		if(!tabIndex || tabIndex == '1'){
			var url =  '${request.contextPath}/stuwork/courserecord/myCourseHead';
		}else if(tabIndex == '2'){
			var url =  '${request.contextPath}/stuwork/courserecord/allCourseHead';
		}
		$("#itemShowDivId").load(url);
	}
</script>