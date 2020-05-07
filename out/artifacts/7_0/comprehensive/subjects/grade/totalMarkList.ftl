<div class="table-container-body">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>学年</th>
				<th>学期</th>
				<th>年级</th>
				<th>包含科目</th>
				<th>查看总评成绩</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<#if compreInfosList?exists && (compreInfosList?size > 0)>
			<#list compreInfosList as comInfo>
			<tr>
				<td>${comInfo_index+1}</td>
				<td>${comInfo.acadyear!}</td>
				<td><#if (comInfo.semester!) == "1">第一学期<#else>第二学期</#if></td>
				<td>${comInfo.gradeName!}</td>
				<td>${comInfo.subjectNames!}</td>
				<td><a href="javascript:findTotalMarkScore('${comInfo.id!}','${comInfo.gradeId!}');">去查看</a></td>
				<td>
					<a class="js-add" href="javascript:doEditMark('${comInfo.id!}','${comInfo.gradeId!}');">编辑</a>
					<a class="" href="###" onClick="doDelete('${comInfo.id!}');">删除</a>
				</td>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	function findTotalMarkScore(comInfoId,gradeId) {
		var searchAcadyear = $("#searchAcadyear").val();
		var searchSemester = $("#searchSemester").val();
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&gradeId="+gradeId+"&comInfoId="+comInfoId;
	    var url = '${request.contextPath}/comprehensive/subjects/totalMarkScore/Index/page'+str;
		$("#showIndex").load(url);
	}

	function doEditMark(id,gradeId) {
		var searchAcadyear = $("#searchAcadyear").val();
		var searchSemester = $("#searchSemester").val();
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&gradeId="+gradeId+"&id="+id;
		var url = "${request.contextPath}/comprehensive/subjects/totalMark/Edit/page"+str;
		indexDiv = layerDivUrl(url,{title: "编辑",width:450,height:450});
	}

	function doDelete(id) {
		showConfirmMsg('确认删除？','提示',function(){
		var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/comprehensive/subjects/totalMark/delete/page',
				data: {'id':id},
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
					  	totalMarkList();
			 		}else{
			 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
					layer.close(ii);
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}
</script>