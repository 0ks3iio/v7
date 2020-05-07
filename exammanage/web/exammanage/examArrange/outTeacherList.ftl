<form id="outTeacherForm">
<div id="ee" class="tab-pane fade active in">
	<div class="table-container">
		<div class="table-container-header text-right">
			<a href="javascript:" class="btn btn-blue" onClick="outTeacherAdd();">新增</a>
			<a href="javascript:" class="btn btn-blue" onClick="outTeacherImport();">导入</a>
		</div>
		<div class="table-container-body">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th class="">序号</th>
						<th class="">老师姓名</th>
						<th class="">电话号码</th>
						<th class="">操作</th>
					</tr>
				</thead>
				<#if teacherOutLists?exists && (teacherOutLists?size > 0)>
				<tbody>
					<#list teacherOutLists as list>
					<tr>
						<td class="">${list_index+1}</td>
						<td class="">${list.teacherName!}</td>
						<td class="">${list.mobilePhone!}</td>
						<td class=""><a class="table-btn color-red js-deleTeacher" href="javascript:deleteOutTeacher('${list.id!}');">删除</a></td>
					</tr>
					</#list>
				</tbody>
				</#if>
			</table>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">

function outTeacherAdd() {
	var url = "${request.contextPath}/exammanage/examArrange/outTeacherAdd?examId=${examId!}";
	indexDiv = layerDivUrl(url,{title: "新增校外老师",width:300,height:250});
}

function outTeacherImport() {
	var url="${request.contextPath}/exammanage/outTeacherImport/main?examId=${examId!}"
	$("#showTabDiv").load(url);
}

var isSubmit=false;
function deleteOutTeacher(id) {
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/outTeacherDelete?id='+id+'&examId=${examId!}',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				var url =  '${request.contextPath}/exammanage/examArrange/outTeacherIndex/page?examId=${examId!}';
				$("#showTabDiv").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#outTeacherForm").ajaxSubmit(options);
}	
</script>