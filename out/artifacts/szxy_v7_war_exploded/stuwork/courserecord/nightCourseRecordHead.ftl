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
								<input type="text" class="form-control datepicker" id="queryDate" style="width:120px" onChange="searchList();" value="${(nowDate?string('yyyy-MM-dd'))!}">
								<span class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">年级：</span>
						<div class="filter-content">
							<select name="" id="gradeId" class="form-control" onChange="searchList();">
							<#if gradeList?exists && gradeList?size gt 0>
								<#list gradeList as item>
                                    <option value="${item.id!}" <#if gradeId?default('') == item.id!>selected</#if>>${item.gradeName!}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
				</div>
				<div class="table table-striped" id="showList">
		        </div>
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
	//searchList();
});

function searchList(){
    var queryDate = $('#queryDate').val();
    var gradeId = $('#gradeId').val();
    var str = "?queryDate="+queryDate+"&gradeId="+gradeId+"&acadyear=${acadyear!}&semester=${semester!}";
    var url = "${request.contextPath}/stuwork/courserecord/nightCourseList"+str;
    $("#showList").load(url);
}

function toImport(){
	var queryDate = $('#queryDate').val();
	var gradeId = $('#gradeId').val();
	var url = "${request.contextPath}/stuwork/courserecord/import/index?type=2&acadyear=${acadyear!}&semester=${semester!}&queryDate="+queryDate+"&gradeId="+gradeId;
	$('.model-div').load(url);
}
</script>