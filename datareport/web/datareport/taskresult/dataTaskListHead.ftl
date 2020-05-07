<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
			<div class="box-body clearfix">
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">状态：</span>
						<div class="filter-content">
							<select name="task_state" id="task_state" class="form-control" onChange="showTaskList()">
								<option value="0">---请选择---</option>
								<option value="1">未提交</option>
								<option value="3">已提交</option>
							</select>
						</div>
					</div>
				</div>
				<div id="taskListDiv">
					
				</div>
			</div>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->
<script>
	$(function(){
		showTaskList();
	})
	
	function showTaskList() {
		var state = $("#task_state").val();
		var url = "${request.contextPath}/datareport/taskresult/tasklist?ownerType="+_ownerType+"&state="+state;
		$("#taskListDiv").load(url);
	}
</script>