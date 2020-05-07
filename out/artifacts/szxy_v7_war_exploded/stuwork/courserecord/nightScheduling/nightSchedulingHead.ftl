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
		<span class="filter-name">学段：</span>
		<div class="filter-content">
			<select name="section" id="section" class="form-control" onChange="searchList()">
			<#if tis?exists && (tis?size>0)>
			    <#list tis as i>     
				 <option value="${i}">${mcodeSetting.getMcode('DM-RKXD',i)}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
	<#--  <a href="javascript:void(0);"  class="btn btn-blue js-checkIn" onclick = "setAllTea();">设为各班班主任</a> -->	  
	    <a href="javascript:void(0);"  class="btn btn-blue js-checkIn" onclick = "setDutyTea();">安排老师</a>
		<a href="javascript:void(0);"  class="btn btn-blue js-checkIn" onclick = "showImport();">导入</a>
	</div>
</div>
<div class="table table-striped" id="nightSchList">
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
    var queryDate = $("#queryDate").val();
    var section = $('#section').val();
    var url = "${request.contextPath}/stuwork/night/scheduling/list/page?queryDate="+queryDate+'&section='+section;
    $("#nightSchList").load(url);
}

//导入
function showImport(){
	var url =  '${request.contextPath}/stuwork/night/import/scheduling/main';
	$("#showNightSchList").load(url);
}
</script>