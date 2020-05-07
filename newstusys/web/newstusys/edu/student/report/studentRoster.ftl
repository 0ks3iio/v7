<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<#import "/fw/macro/treemacro.ftl" as treemacro>


<div class="row">
<#if unitClass==1>
	<div class="col-sm-3">
		<div class="box box-default">
			<div class="box-header">
				<h3 class="box-title">学校菜单</h3>
			</div>
			<#--<div class="box-body">
				<ul id="tree" class="ztree"></ul>
			</div>-->
			<!-- 树菜单开始 -->
            <div class="box-body">
                <@treemacro.unitForSubInsetTree height="450" checkEnable=false click="chooseSch" notRelate=true/>
            </div><!-- 树菜单结束 -->
		</div>
	</div>
</#if>
	<div <#if unitClass==1>class="col-sm-9 print"</#if>>
		<#if type == '1' || type == '3'>
		<div class="filter" <#if unitClass==1>style="display:none;"</#if>>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeCode" id="gradeCode" class="form-control" onchange="searchStu();">
						<option value="">全部</option>
						<#if gradeCodes?exists && gradeCodes?size gt 0>
						<#list gradeCodes as gc>
						<option value="${gc[0]}">${gc[1]}</option>
						</#list>
						</#if>
					</select>
				</div>
			</div>
			<#if type == '1'>
			<div class="filter-item">
				<span class="filter-name">学生条件：</span>
				<div class="filter-content">
					<select name="searchType" id="searchType" class="form-control" onchange="searchStu();">
						<option value="">全部</option>
						<option value="1">市外户籍</option>
						<option value="2">无户口学生</option>
						<option value="3">随迁子女（总）</option>
						<option value="4">随迁子女（省外）</option>
						<option value="5">留守儿童</option>
						<option value="6">随班就读</option>
						<option value="7">外籍​学生</option>
						<option value="8">港澳台生</option>
					</select>
				</div>
			</div>
			</#if>
			<div class="filter-item filter-item-right export-div" <#if unitClass==1 && type == '1'>style="display:none;"</#if>>
				<button type="button" class="btn btn-blue " onclick="doExport()">导出</button>
			</div>
		</div>
		</#if>
		<div id="rosterDiv">
			<#if unitClass==1>
				<div class="no-data-container mt50">
					<div class="no-data mt50">
						<span class="no-data-img mt50">
							<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
						</span>
						<div class="no-data-body">
							<h3>请选择单位</h3>
						</div>
					</div>
				</div>
			</#if>
		</div>
	</div>
</div>
<input type="hidden" id="type" value="${type!}">
<input type="hidden" id="nowUnitId" value="${unitId!}">
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>
$(function(){
    var type = $('#type').val();
	<#if unitClass==2>
	     <#if type=='1'>
	         var url =  '${request.contextPath}/newstusys/student/report/studentRosterList?unitId=${unitId!}';
	     <#elseif type=='2'>
	         var url =  '${request.contextPath}/newstusys/student/report/classCount?unitId=${unitId!}';
	     <#else>
	         var url =  '${request.contextPath}/newstusys/student/report/gradeCount?unitId=${unitId!}';
	     </#if>
	     $("#rosterDiv").load(url);
	</#if>     
});


function chooseSch(event, treeId, treeNode){
	$('.no-data-container').hide();
    var unitId = treeNode.id;
    var type = $('#type').val();
    $('#nowUnitId').val(unitId);
    var url="";
    if(type=='1'){
    	if('${topUnitId!}'==unitId){
    		$('.export-div').hide();
    	} else {
    		$('.export-div').show();
    	}
    	$('.filter').show();
    	var gradeCode = $('#gradeCode').val();
		var searchType = $('#searchType').val();
        url =  '${request.contextPath}/newstusys/student/report/studentRosterList?unitId='+unitId+'&gradeCode='+gradeCode+'&searchType='+searchType;
    }else if(type=='2'){
        url =  '${request.contextPath}/newstusys/student/report/classCount?unitId='+unitId;
    }else if(type=='3'){
    	$('.filter').show();
        var gradeCode = $('#gradeCode').val();
        url =  '${request.contextPath}/newstusys/student/report/gradeCount?unitId='+unitId+'&gradeCode='+gradeCode;
    }
    $("#rosterDiv").load(url);
}

function searchStu(){
	var unitId = $('#nowUnitId').val();
	var gradeCode = $('#gradeCode').val();
	var type = $('#type').val();
	if(type=='1'){
		var searchType = $('#searchType').val();
		var url =  '${request.contextPath}/newstusys/student/report/studentRosterList?unitId='+unitId+'&gradeCode='+gradeCode+'&searchType='+searchType;	
	} else {
		var url =  '${request.contextPath}/newstusys/student/report/gradeCount?unitId='+unitId+'&gradeCode='+gradeCode;
	}
	$("#rosterDiv").load(url);
}
</script>