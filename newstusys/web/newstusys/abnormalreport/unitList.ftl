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
	<div <#if unitClass==1>class="col-sm-9 print"</#if>  id="rosterDiv">
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
<input type="hidden" id="type" value="${type!}">
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>
$(function(){
    var type = $('#type').val();
	<#if unitClass==2>
	     <#if type=='1'>
	         var url =  '${request.contextPath}/newstusys/student/abnormalreport/classCount?unitId=${unitId!}';
	     <#elseif type=='3'>
	         var url =  '${request.contextPath}/newstusys/student/abnormalreport/normalFlowRecord?unitId=${unitId!}';
	     <#else>
	         var url =  '${request.contextPath}/newstusys/student/abnormalreport/gradeCount?unitId=${unitId!}';
	     </#if>
	     $("#rosterDiv").load(url);
	</#if>
});


function chooseSch(event, treeId, treeNode){
    var unitId = treeNode.id;
    var type = $('#type').val();
    var url="";
    if(type=='1'){
        url =  '${request.contextPath}/newstusys/student/abnormalreport/classCount?unitId='+unitId;
    }else if(type=='3'){
        url =  '${request.contextPath}/newstusys/student/abnormalreport/normalFlowRecord?unitId='+unitId;
    }else{
        url =  '${request.contextPath}/newstusys/student/abnormalreport/gradeCount?unitId='+unitId;
    }
    $("#rosterDiv").load(url);
}
</script>