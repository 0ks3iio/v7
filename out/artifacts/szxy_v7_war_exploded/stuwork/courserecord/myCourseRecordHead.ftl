<div class="tab-content">
	<div id="aa" class="tab-pane active" role="tabpanel">
        <div class="filter">
			<div class="filter-item">
				<span class="filter-name">日期：</span>
				<div class="filter-content">
					<div class="input-group">
						<input type="text" style="width:120px" id="queryDate" onChange="searchMyList();" class="form-control datepicker" value="${(nowDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
			<#if recordAdmin?default(false)>
			<div class="filter-item">
				<span class="filter-name">节次：</span>
				<div class="filter-content">
					<select name="period" id="period" class="form-control" onChange="searchMyList();">
						<option value="" <#if period?default(0)==0>selected</#if>>全部</option>
					<#if periodCount?exists && periodCount gt 0>
					   <#list 1..periodCount as item>
					        <option value="${item}" <#if period?default(0)==item>selected</#if>>第${item}节</option>
					   </#list>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
                <div class="filter-content">
                    <a class="btn btn-white" onclick="toImport();" >导入</a>
                </div>
            </div>
            <#else>
            <input type="hidden" name="period" id="period" value="${period?default(0)}">
            </#if>
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
});

function searchMyList(){
	var period=$('#period').val();
    var queryDate = $('#queryDate').val();
    var str = "?recordAdmin=${recordAdmin?default(false)?string("true","false")}&acadyear=${acadyear!}&semester=${semester!}&teacherId=${teacherId!}&queryDate="+queryDate+"&period="+period;
    var url = "${request.contextPath}/stuwork/courserecord/myCourseList"+str;
    $("#showList").load(url);
}

function toImport(){
	var queryDate = $('#queryDate').val();
	var period=$('#period').val();
	var url = "${request.contextPath}/stuwork/courserecord/import/index?type=1&acadyear=${acadyear!}&semester=${semester!}&queryDate="+queryDate+"&period="+period;
	$("#itemShowDivId").load(url);
}
</script>