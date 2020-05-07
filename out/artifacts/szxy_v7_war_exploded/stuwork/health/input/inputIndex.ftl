<#import "/fw/macro/treemacro.ftl" as treemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default">
					<div class="box-header">
						<h4 class="box-title">体检信息录入</h4>
					</div>
					<div class="box-body">
						<div class="filter-container">
							<div class="filter">
								<div class="filter-item">
									<span class="filter-name">学年：</span>
									<div class="filter-content">
										<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showList('')">
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
										<select id="searchSemester" name="searchSemester" class="form-control" onChange="showList('')">
												${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
										</select>
									</div>
								</div>
								<div class="filter-item filter-item-right">
									<button class="btn btn-blue" onclick="showImport()">导入</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
						<div class="box box-default">
							<div class="box-header">
								<h3 class="box-title">选择学生</h3>
							</div>
							<div class="box-body">
								<ul id="tree" class="ztree">
								  <@treemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
								</ul>
							</div>
							<input type="hidden" id="studentId" value="">
						</div>
					</div>
					<div class="col-sm-9">
						<div class="box box-default">
							<div class="box-body" id ="inputList">

								
										
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->

<script>
$(function(){
	//showList('1','');
});
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "student"){
		var id = treeNode.id;
		$("#studentId").val(id);
		showList(id);
	}
}
function showList(id){
    var acadyear = $("#searchAcadyear").val();
	var semester =  $("#searchSemester").val();
	if(id==undefined ||id==""){
		id=$("#studentId").val();
	}
	var url =  '${request.contextPath}/stuwork/health/input/list?studentId='+id+'&acadyear='+acadyear+'&semester='+semester;
	$("#inputList").load(url);
}

//导入
function showImport(){
    var acadyear = $("#searchAcadyear").val();
	var semester =  $("#searchSemester").val();
	var url =  '${request.contextPath}/stuwork/health/import/main?acadyear='+acadyear+'&semester='+semester;
	$("#inputIndex").load(url);
}

</script>
