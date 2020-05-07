<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="#tabList" onclick="showTab('1')" role="tab" data-toggle="tab">各班学生记录查询</a></li>
		</ul>
		<div class="tab-content">
			<div id="tabList" class="tab-pane active" role="tabpanel">
               <div class="filter">
					<div class="filter-item">
						<span class="filter-name">学年：</span>
						<div class="filter-content">
							<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showList('${type!}','')">
									<#if acadyearList?exists && (acadyearList?size>0)>
					                    <#list acadyearList as item>
						                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
					                    </#list>
				                    </#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">学期：</span>
						<div class="filter-content">
							<select id="searchSemester" name="searchSemester" class="form-control" onChange="showList('${type!}','')">
									${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
							</select>
						</div>
					</div>
				</div>
					<input type="hidden" id="studentId" value="">
					<input type="hidden" id="deptId" value="">
					<div class="clearfix">
						<div class="tree-wrap">
						<h4>学生选择</h4>
							<@dytreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
						</div>
						<div id="showList" class="table-container">
							
						</div>
					</div>
			</div>
		</div>
	</div>
</div>
<script>

function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "student"){
		var id = treeNode.id;
		$("#studentId").val(id);
		showList('1',id);
	}
}
function showList(type,id){
	var acadyear = $("#searchAcadyear").val();
	var semester =  $("#searchSemester").val();
	if(id==undefined ||id==""){
		id=$("#studentId").val();
	}
	var url =  '${request.contextPath}/tutor/class/record/student/record/list?studentId='+id+'&acadyear='+acadyear+'&semester='+semester;
	$("#showList").load(url);
}





</script>