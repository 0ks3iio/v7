<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<style>
.filter-item-right{margin-left:5px}
.filter-item-left{margin-right:5px}
</style>
<div class="filter">
	<div class="filter-item filter-item-right">
		<span class="filter-name">教师名称：</span>
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
					<input type="text" id="teacherName" class="typeahead scrollable form-control" style="width:110px!important" autocomplete="off" data-provide="typeahead" placeholder="输入教师姓名">
		        </div>
			    <div class="input-group-btn">
			    	<a type="button" onclick="doSearch()" class="btn btn-default">
				    	<i class="fa fa-search" onclick="doSearch()"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">起止日期：</span>
		<div class="filter-content">
			<div class="input-group float-left"">
				<input id="startDate" autocomplete="off" name="startDate" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="开始时间" value="${startDate?string('yyyy-MM-dd')!}"/>
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
			<span class="float-left mr10 ml10"> 至 </span>
			<div class="input-group float-left">
				<input id="endDate" autocomplete="off" name="endDate" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="结束时间" value="${endDate?string('yyyy-MM-dd')!}"/>
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
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
	
	$("#teacherName").bind('keypress',function(event){
	    if(event.keyCode == "13"){
	      doSearch();
	    }
	});
})

function CompareDate(d1,d2){
	return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
}

function doSearch(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(CompareDate(startDate, endDate)){
		layer.tips('开始日期不能大于结束日期，请重新选择！', $("#startDate"), {
				tipsMore: true,
				tips:3				
			});
		return;
	}
	var acadyear = $("#acadyear").val();
	var semester = $("#semester").val();
	var teacherName = $("#teacherName").val();
	var url = '${request.contextPath}/basedata/timetableChange/monthCount/list/page?acadyear='+acadyear+'&semester='+semester+'&startDate='+startDate+'&endDate='+endDate+'&teacherName='+teacherName;
	$("#showListDiv").load(encodeURI(url));
}

function doExcelResult(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(CompareDate(startDate, endDate)){
		layer.tips('开始日期不能大于结束日期，请重新选择！', $("#startDate"), {
				tipsMore: true,
				tips:3				
			});
		return;
	}
	var acadyear = $("#acadyear").val();
	var semester = $("#semester").val();
	var teacherName = $("#teacherName").val();
	var url = '${request.contextPath}/basedata/timetableChange/monthCount/export?acadyear='+acadyear+'&semester='+semester+'&startDate='+startDate+'&endDate='+endDate+'&teacherName='+teacherName;
	document.location.href=url;
}
</script>