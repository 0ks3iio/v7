<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="filter">
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" id="teacherName" value="${teacherName!}" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="输入教师姓名查询" >
		        </div>
			    <div class="input-group-btn">
			    	<a type="button" onclick="queryByName()" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<select name="" id="subjectId" class="form-control" data-placeholder="请选择科目" onchange="queryByName()">
				<option value="">请选择科目</option>
				<#if subs?exists && subs?size gt 0>
				<#list subs as sub>
				<option value="${sub.id!}" <#if subjectId?exists && subjectId == sub.id!>selected</#if>>${sub.subjectName!}</option>
				</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-left">
		<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		<a href="javascript:doExportAll();" class="btn btn-white">导出全部课表</a>
	</div>
</div>


<div class="table-container" id="teacherList">
	
</div>

<script>
$(function(){
	queryByName();
	$("#teacherName").bind('keypress',function(event){
	    if(event.keyCode == "13")    
	    {
	      queryByName();
	    }
	});
})
function queryByName(){
	var teacherName = $("#teacherName").val();
	var sid = $('#subjectId').val();
	var id = '${arrayId}';
	url = '${request.contextPath}/newgkelective/'+id+'/arrayResult/teacherList/page?teacherName=' + teacherName+'&subjectId='+sid;
	url = encodeURI(url);
	$("#teacherList").load(url);
}
function doExportAll(){
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/teacher/exportTimetableAll';
  	document.location.href=url;
}
</script>
