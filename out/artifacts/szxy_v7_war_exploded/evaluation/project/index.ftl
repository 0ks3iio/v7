<!-- /section:basics/sidebar -->
			<div class="page-head-btns">
				<button class="btn btn-blue js-addNewProject" onclick="addProject('');" <#if currentAcadyear != '1'>disabled="disabled"</#if>>新建评教项目</button>
				<div class="filter-item">
					<span class="filter-name">学年：</span>
					<div class="filter-content">
						<select name="acadyear" id="acadyear" class="form-control" onchange="changeAcadyear()">
							<#list acadyearList as ac>
								<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">学期：</span>
					<div class="filter-content">
						<select name="semester" id="semester" class="form-control" onchange="changeAcadyear()">
							<option value="1" <#if semester == '1'>selected</#if>>第一学期</option>
							<option value="2" <#if semester == '2'>selected</#if>>第二学期</option>
						</select>
					</div>
				</div>
			</div>
			<#if projectList?exists && projectList?size gt 0>
			<#list projectList as project>
				<div class="box box-default">
					<div class="box-header">
						<h4 class="box-title">
							<span id="${project.id!}">${project.projectName!}</span>
							<a class="box-title-icon js-editTutorProjectName" href="javascript:;"><i class="fa fa-pencil"></i></a>
						</h4>
					</div>
					<div class="box-body">
						<div class="tutor-project">
							<a href="javascript:;" onclick="showSubStu('${project.id!}');"><strong id="submitNum${project.id!}">${project.submitNum!}</strong><span>已提交</span></a>
							<a href="javascript:;" onclick="showNoSubStu('${project.id!}');"><strong id="noSubmitNum${project.id!}">${project.noSubmitNum!}</strong><span>未提交</span></a>
							<div class="tutor-project-state">
							<#assign canAdd="">
							
							<#if project.beginTime gt .now>
								<#assign canAdd="true">
								<span class="color-red">未开始</span>
							<#elseif project.endTime lt .now>
								<span class="color-grey">已结束</span>
							<#else>
								<span class="color-green">进行中</span>
							</#if>
								<p>开始时间：${project.beginTime?string('yyyy-MM-dd HH:mm')}</p>
							</div>
						</div>
					</div>
					<div class="box-tools dropdown">
						<a class="box-tools-btn" href="javascript:;" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>
						<ul class="box-tools-menu dropdown-menu">
							<span class="box-tools-menu-angle"></span>
							<li><a href="javascript:;" onclick="showItem('${project.id!}')">问卷预览</a></li>
							<#if project.evaluateType=="10" || project.evaluateType=="13">
								<li><a href="javascript:;" onclick="addRelation('${project.id!}','${project.evaluateType!}','${canAdd!}')">添加参评班级</a></li>
							</#if>
							<li><a class="js-addNewProject" href="javascript:;" onclick="addProject('${project.id!}')">查看调查设置</a></li>
							<li><a class="js-del" href="javascript:;"  onclick="deleteProject('${project.id!}')">删除</a></li>
						</ul>
					</div>
				</div>
			</#list>
			<#else>
				<div class="no-data-container">
					<div class="no-data no-data-hor">
						<span class="no-data-img">
							<img src="${request.contextPath}/evaluation/images/no-tutor-project.png" alt="">
						</span>
						<div class="no-data-body">
							<h3>暂无项目</h3>
							<#if currentAcadyear == '1'>
							<p class="no-data-txt">请点击左上角的“新建评教项目”按钮新建</p>
							</#if>
						</div>
					</div>
				</div>
			</#if>
<script>
function getSubNum(projectId){
	$.ajax({
		url:"${request.contextPath}/evaluate/project/getSubNum",
		data:{'projectId':projectId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
			$("#submitNum"+projectId).html(jsonO.submitNum);
			$("#noSubmitNum"+projectId).html(jsonO.noSubmitNum);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
	
}
function changeAcadyear(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/evaluate/project/index/page?acadyear='+acadyear+'&semester='+semester;
	$(".model-div").load(url);
}

function addProject(porjectId){
	var url = "${request.contextPath}/evaluate/project/edit/page?projectId="+porjectId;
	indexDiv = layerDivUrl(url,{title: "编辑评教项目",width:420,height:480});
}

function showItem(projectId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/evaluate/project/itemTable/page?projectId='+projectId+'&acadyear='+acadyear+'&semester='+semester;
	$(".model-div").load(url);
}
function addRelation(projectId,evaluateType,canAdd){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/evaluate/project/addRelation/page?projectId='+projectId+'&acadyear='+acadyear+'&semester='+semester+"&evaluateType="+evaluateType+"&canAdd="+canAdd;
	$(".model-div").load(url);
}
var projectOldName = '';
var projectId = '';
$('.js-editTutorProjectName').on('click', function(e){
	e.preventDefault();
	projectOldName = $(this).prev().html();
	projectId = $(this).prev().attr('id');
	$(this).prev().attr('contenteditable','true').focus().blur(function(){
			var projectName = $(this).html();
			$(this).removeAttr('contenteditable');
			projectName = projectName.replace(/<br>/g,"");
			if(!projectName || projectName == ''){
				layerTipMsg(false,"项目名称不能为空！","");
				$(this).html(projectOldName);
				return;
			}else{
				if(projectName.length > 50){
					layerTipMsg(false,"项目名称长度不能超过50！","");
					$(this).html(projectOldName);
					return;
				}else{
					$.ajax({
						url:"${request.contextPath}/evaluate/project/saveProjectName",
						data: {'projectId':projectId,'projectName':projectName},
						type:'post',
						success:function(data) {
							var jsonO = JSON.parse(data);
								if(jsonO.success){
									layerTipMsg(jsonO.success,"保存成功","");
									$(".model-div").load("${request.contextPath}/evaluate/project/index/page");
								}else{
									layerTipMsg(jsonO.success,"失败",jsonO.msg);
								}
						},error : function(XMLHttpRequest, textStatus, errorThrown) {
				 			
						}
					});
				}
			}
		});
})

function deleteProject(projectId){
	if(projectId && projectId.length > 0){
		var url = "${request.contextPath}/evaluate/project/delete/page?projectId="+projectId;
		indexDiv = layerDivUrl(url,{title: "删除评教项目",width:350,height:250});
	}
}

function showSubStu(projectId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/evaluate/project/showStuSubList/page?projectId='+projectId+'&acadyear='+acadyear+'&semester='+semester;
	$(".model-div").load(url);
}
function showNoSubStu(projectId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/evaluate/project/showStuNotSubList/page?projectId='+projectId+'&acadyear='+acadyear+'&semester='+semester;
	$(".model-div").load(url);
}
<#--
$(function(){
	alert('${projectList?size}')
	<#if projectList?exists && projectList?size gt 0>
		<#list projectList as project>
			getSubNum('${project.id!}');
		</#list>
	</#if>
});-->
<#if projectList?exists && projectList?size gt 0>
	<#list projectList as project>
		getSubNum('${project.id!}');
	</#list>
</#if>
</script>

