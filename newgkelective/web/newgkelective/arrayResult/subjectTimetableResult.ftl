<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />

<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">查看课表</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="backNewClass()" >返回</a>
			<@htmlcomponent.printToolBar container=".print" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		</div>
	</div>
	<div class="box-body">
	<div class="clearfix">
		<div class="tree-wrap">
			<h4>选择班级</h4>
			<div class="input-group">
				<input type="text" class="form-control" id="searchClsName" value="${searchClsName!}" placeholder="请输入班级名称">
				<div class="input-group-btn btn-search">
					<button type="button" class="btn btn-default">
						<i class="fa fa-search"></i>
					</button>
				</div>
			</div>
			<div class="tree-list" style="height:600px;margin-top: 0;">
				<#if divideClassList?exists && divideClassList?size gt 0>
					<#list divideClassList as item>
					<a  href="javascript:;" data-cls-id="${item.id!}" <#if classId=item.id>class="active"</#if>>${item.className!}</a>
					</#list>
				</#if>
			</div>
		</div>
		
		
		
		<#--
		div class="filter">
			<div class="filter-item">
				<span class="filter-name">班级名称：</span>
				<div class="filter-content">
					<select id="classBtn" class="form-control">
						<#if divideClassList?exists && divideClassList?size gt 0>
						<#list divideClassList as item>
						<option value="${item.id!}" <#if classId=item.id>selected="selected"</#if>>${gradeName!}${item.className!}</option>
						</#list>
						</#if>
					</select>
				</div>
			</div>
			
		</div-->
		<div id="timetableDiv" class="">
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>请选择要查看的班级</h3>
					</div>
				</div>
			</div>
		</div>
	</div>	
	</div>
</div>
<script>
	function backNewClass(){
		<#if classType?default('1')=='1'>
	    var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/newClassResult';
		$("#tableList").load(url);
		<#else>
		var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectClassResult/tabHead/page';
		$("#tableList").load(url);
		</#if>
	}

$('.btn-search').on('click',function(){
	searchClass();
});

$('#searchClsName').keydown(function(event){
    if(event.keyCode==13){
    	searchClass();
    }
});

function searchClass(){
	var un = $.trim($('#searchClsName').val());
	if(un == '${searchClsName!}'){
		return;
	}
	if(un.indexOf('\'')>-1||un.indexOf('%')>-1){
        layer.tips('班级名称不能包含单引号、百分号等特殊符号！',$('#searchClsName'), {
					tipsMore: true,
					tips: 3
				});
        return ;
    }
    var url='${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectTimetableResult/index/page?classType=${classType!}&classId=&searchClsName='+encodeURIComponent(un);
    $('#tableList').load(url);
}

<#if classId?default('') != ''>
	toDataList('${classId!}');		
</#if>

$(".tree-list a").click(function(){
	$(this).addClass('active').siblings('a').removeClass('active');
	var classId = $(this).attr('data-cls-id');
	toDataList(classId);
})

function toDataList(classId){
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectTimetableResult/getDetailData?classType=${classType!}&classId='+classId;
	$("#timetableDiv").load(url);
}
</script>					