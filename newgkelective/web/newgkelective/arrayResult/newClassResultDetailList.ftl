<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">查看学生</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="toBackNewClass()" >返回</a>
			<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		</div>
	</div>
	<div class="box-body">
	<div class="clearfix">
		<div class="tree-wrap">
			<h4>选择班级</h4>
			<div class="input-group">
				<input type="text" class="form-control" id="searchClsName" value="${searchClsName!}" placeholder="请输入班级名称">
				<span class="input-group-addon btn-search">
					<i class="fa fa-search"></i>
				</span>
			</div>
			<div class="tree-list" style="height:600px;margin-top: 0;">
				<#if newGkDivideClassList?exists && newGkDivideClassList?size gt 0>
					<#list newGkDivideClassList as item>
					<a  href="javascript:;" data-cls-id="${item.id!}" <#if classId?default('') == item.id>class="active"</#if>>${gradeName!}${item.className!}</a>
					</#list>
				</#if>
			</div>
		</div>
		<#--
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">新行政班：</span>
				<div class="filter-content">
					<select name="" id="classId" class="form-control" onChange="toDetailList2();">
					<#if newGkDivideClassList?exists && newGkDivideClassList?size gt 0>
					    <#list newGkDivideClassList as item>
						    <option value="${item.id!}" <#if '${item.id!}'== '${classId!}'>selected</#if>>${item.className!}</option>
						</#list>
					</#if>
					</select>
				</div>
			</div>
		    <div class="filter-item">
				<span class="filter-name">班级类型：</span>
				<div class="filter-content">
					<p>
					<#if newGkDivideClass?exists>
					    <#if '${newGkDivideClass.bestType!}' == '1'>
					       分层教学班
					    <#else>
					    平行班
					    </#if>
					</#if>
					</p>
				</div>
			</div>
			-->
			<#--
				<div class="filter-item">
					<span class="filter-name">教室：</span>
					<div class="filter-content">
						<p>${placeName!}</p>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">总人数：</span>
					<div class="filter-content">
						<p>${newGkDivideClass.studentList?size!}</p>
					</div>
				</div>
			</div>
		-->
		<div id="stuDataDiv" class="print">
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
var arrayId = '${arrayId!}';
function toBackNewClass(){
   	var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/newClassResult';
	$("#tableList").load(url);
}

<#if classId?default('') !=''>
	toDetailList2('${classId!}');
</#if>

$('.tree-list a').click(function(){
	$(this).addClass('active').siblings('a').removeClass('active');
	var classId = $(this).attr('data-cls-id');
	toDetailList2(classId);
});

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
    var url='${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/newClassResultDetailList?classId=&searchClsName='+encodeURIComponent(un);
    $('#tableList').load(url);
}

function toDetailList2(classId){
    var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/newClassResultDetailStuList?classId='+classId;
	$("#stuDataDiv").load(url);
}
</script>