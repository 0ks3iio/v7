<form id="outTeacherForm">
<div id="ee" class="tab-pane fade active in">
	<div class="table-container">
	<div class="filter filter-f16">
		<div class="filter-item">
			<a href="javascript:" class="btn btn-blue" onClick="outTeacherAdd();">新增</a>
			<a href="javascript:" class="btn btn-white" onClick="outTeacherImport();">导入</a>
		</div>
		</div>
		<div class="table-container-body">
		<#if teacherOutLists?exists && (teacherOutLists?size > 0)>
			<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
					<tr>
						<th >序号</th>
						<th >老师姓名</th>
						<th >电话号码</th>
						<th >操作</th>
					</tr>
				</thead>
				
				<tbody>
					<#list teacherOutLists as list>
					<tr>
						<td >${list_index+1}</td>
						<td >${list.teacherName!}</td>
						<td >${list.mobilePhone!}</td>
						<td ><a class="table-btn color-red js-deleTeacher" href="javascript:deleteOutTeacher('${list.id!}');">删除</a></td>
					</tr>
					</#list>
				</tbody>
				
			</table>
			<#else>
            <div class="no-data-container">
	            <div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
					</span>
	                <div class="no-data-body">
	                    <p class="no-data-txt">暂无相关数据</p>
	                </div>
	            </div>
	        </div>
	</#if>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">

function outTeacherAdd() {
	var url = "${request.contextPath}/exammanage/examTeacher/outTeacherAdd?examId=${examId!}";
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
		url : '${request.contextPath}/exammanage/examTeacher/outTeacherDelete?id='+id+'&examId=${examId!}',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
                layer.msg("删除成功！", {
                    offset: 't',
                    time: 2000
                });
				var url =  '${request.contextPath}/exammanage/examTeacher/outTeacherIndex/page?examId=${examId!}';
				$("#showTabDiv").load(url);
	 		}
	 		else{
                layer.msg("删除失败！", {
                    offset: 't',
                    time: 2000
                });
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