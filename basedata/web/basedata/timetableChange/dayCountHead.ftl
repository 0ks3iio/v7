<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<style>
.filter-item-right{margin-left:5px}
.filter-item-left{margin-right:5px}
</style>
<div class="filter">
	<div class="filter-item filter-item-right">
		<span class="filter-name">起止日期：</span>
		<div class="filter-content">
			<div class="input-group float-left"">
				<input id="startDate" autocomplete="off" name="startDate" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="开始时间" value="${startDate?string('yyyy-MM-dd')!}" />
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
			<span class="float-left mr10 ml10"> 至 </span>
			<div class="input-group float-left">
				<input id="endDate" autocomplete="off" name="endDate" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="结束时间" value="${endDate?string('yyyy-MM-dd')!}" />
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">类型：</span>
		<div class="filter-content">
			<select name="type" id="type" class="form-control" onchange="doSearch()">
				<option value="" title="-请选择-" selected="">-请选择-</option>
				<option value="3" title="调课">调课</option>
				<option value="1" title="代课">代课</option>
				<option value="2" title="管课">管课</option>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select name="semester" id="semester" class="form-control" style="width:110px" onchange="doSearch()">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyear" id="acadyear" class="form-control" style="width:120px" onchange="doSearch()">
				<#if acadyearList?exists && (acadyearList?size>0)>
                    <#list acadyearList as item>
	                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                    </#list>
                <#else>
                    <option value="">未设置</option>
                 </#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-left">
	<@htmlcomponent.printToolBar container=".print"  printDirection='true' btn2='false' printUp=0 printLeft=0 printBottom=0 printRight=0/>
	<a href="javascript:" class="btn btn-blue" onclick="doExcelResult()" data-toggle="tooltip" data-original-title="导出页面中查询出的结果">导出Excel</a>
	</div>
</div>

<div class="row print" id="showListDiv">
</div>
<script>
$(function(){
	doSearch();

	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	
	$("#startDate").on("change",function(){
		doSearch();
	})
	
	$("#endDate").on("change",function(){
		doSearch();
	})
})

function CompareDate(d1,d2){
	return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
}

function doSearch(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(CompareDate(startDate, endDate)){
		layer.tips('开始日期不能大于结束日期，请重新选择！', $("#endDate"), {
				tipsMore: true,
				tips:3				
			});
		return;
	}
	var acadyear = $("#acadyear").val();
	var semester = $("#semester").val();
	var type = $("#type").val();
	var url = '${request.contextPath}/basedata/timetableChange/dayCount/list/page?acadyear='+acadyear+'&semester='+semester+'&type='+type+'&startDate='+startDate+'&endDate='+endDate;
	$("#showListDiv").load(encodeURI(url));
}

function doExcelResult(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(CompareDate(startDate, endDate)){
		layer.tips('开始日期不能大于结束日期，请重新选择！', $("#endDate"), {
				tipsMore: true,
				tips:3				
			});
		return;
	}
	var acadyear = $("#acadyear").val();
	var semester = $("#semester").val();
	var type = $("#type").val();
	var url = '${request.contextPath}/basedata/timetableChange/dayCount/export?acadyear='+acadyear+'&semester='+semester+'&type='+type+'&startDate='+startDate+'&endDate='+endDate;
	document.location.href=url;
}
</script>