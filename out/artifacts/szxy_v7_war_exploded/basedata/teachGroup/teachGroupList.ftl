<div class="filter-item filter-item-right">
	<a class="btn btn-white js-add" onclick="importTeachGroup();">导入</a>
	<a class="btn btn-blue js-add" onclick="addTeachGroup();">新增</a>
	<a class="btn btn-danger js-add" onclick="deleteMoreTeachGroup();">删除</a>
</div>
<div class="table-container-body">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th width="6%">
					<label>
						<input type="checkbox" class="wp" onclick="swapCheck()">
						<span class="lbl"> 全选</span>
					</label>
				</th>
				<th>排序号</th>
				<th>教研组名称</th>
				<th>科目</th>
				<th>负责人</th>
				<th>成员</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
		<#if teachGroupDtoList?exists && (teachGroupDtoList?size>0)>
			<#list teachGroupDtoList as item>
				<tr>
					<td>
						<label>
							<input type="checkbox" class="wp other">
							<span class="lbl"> </span>
							<input type="hidden" value="${item.teachGroupId!}">
						</label>
					</td>
					<td>${item.orderId!}</td>
					<td>${item.teachGroupName!}</td>
					<td>${item.subjectName!}</td>
					<td>
						<#if item.mainTeacherList?exists && (item.mainTeacherList?size>0)>
							<#list item.mainTeacherList as ite>
								<#if ite_index == 0>
									<input type="hidden" value="${ite.teacherId}">
									${ite.teacherName}
								<#else>
									<input type="hidden" value="${ite.teacherId}">
									,${ite.teacherName}
								</#if>
							</#list>
						</#if>
						
					</td>
					<td>
						<#if item.memberTeacherList?exists && (item.memberTeacherList?size>0)>
							<#list item.memberTeacherList as ite>
								<#if ite_index == 0>
									<input type="hidden" value="${ite.teacherId}">
									${ite.teacherName}
								<#else>
									<input type="hidden" value="${ite.teacherId}">
									,${ite.teacherName}
								</#if>
							</#list>
						</#if>
					</td>
					<td>
						<input type="hidden" value="${item.teachGroupId!}">
						<a class="table-btn" href="javascript:void(0);" onclick="alertTeachGroup(this);">修改</a>
						<a class="table-btn" href="javascript:void(0);" onclick="deleteTeachGroup(this);">删除</a>
					</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="88" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
		</tbody>
	</table>
</div>

<script>
var isCheckAll = false;  
function swapCheck() {
	if (isCheckAll) {  
        $("input[type='checkbox']").each(function() {  
            this.checked = false;  
        });  
        isCheckAll = false;  
    } else {  
        $("input[type='checkbox']").each(function() {  
            this.checked = true;  
        });  
        isCheckAll = true;  
    }  
}

function addTeachGroup() {
	var url = "${request.contextPath}/basedata/teachgroup/add/index/page";
	indexDiv = layerDivUrl(url,{title: "新增教研组",width:540,height:700});
}

function alertTeachGroup(obj) {
	var teachGroupId1 = $(obj).prev().val();
	var url = "${request.contextPath}/basedata/teachgroup/add/index/page?teachGroupId=" + teachGroupId1;
	indexDiv = layerDivUrl(url,{title: "修改教研组",width:540,height:700});
}

function deleteTeachGroup(obj) {
	var teachGroupId = $(obj).prev().prev().val();
	layer.confirm('确认删除么？', function(index){
        layer.close(index);
        $.ajax({
			url:"${request.contextPath}/basedata/teachGroup/delete",
			data:{"teachGroupId":teachGroupId},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
					load("1");
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
				}
			}
		});
	});
}

function deleteMoreTeachGroup() {
	var items = $("input.other:checked");
	if(items.length == 0) {
		layer.alert('请勾选教研组',{icon:7});
	}else {
		var teachGroupId = "";
		items.each(function(i){
			if(i == 0) {
				teachGroupId = $(this).next().next().val();
			}else {
				teachGroupId = teachGroupId + "," + $(this).next().next().val();
			}
		});
		layer.confirm('确认删除么？', function(index){
	        layer.close(index);
	        $.ajax({
				url:"${request.contextPath}/basedata/teachGroup/delete",
				data:{"teachGroupId":teachGroupId},
				dataType: "json",
				success: function(data){
					if(data.success){
						layer.msg(data.msg, {offset: 't',time: 2000});
						load("1");
			 		}
			 		else{
			 			layerTipMsg(data.success,"失败",data.msg);
					}
				}
			});
		});
	}
}

function importTeachGroup(){
	var url = '${request.contextPath}/basedata/teachGroup/import/index/page';
	$('#itemShowDiv').load(url);
}

</script>