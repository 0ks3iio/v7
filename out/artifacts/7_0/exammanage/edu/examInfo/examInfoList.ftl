<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
	<div class="table-container-body" id="showList2">
	<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
		<thead>
		<tr>
			<th class="text-center">考试编号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">考试类型</th>
			<th class="text-center">统考类型</th>
			<th class="text-center">年级</th>
			<th class="text-center">起始时间</th>
			<th class="text-center">截止时间</th>
			<th class="text-center">科目设置</th>
			<th class="text-center">操作</th>
		</tr>
		</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
			<td class="text-center">${item.examCode!}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${mcodeSetting.getMcode("DM-KSLB", item.examType?default(1)?string)}</td>
			
			<td class="text-center">${item.examUeTypeName!}</td>
			<td class="text-center">${item.gradeCodeName!}</td>
			<td class="text-center">${(item.examStartDate?string('yyyy-MM-dd'))!}</td>
			<td class="text-center">${(item.examEndDate?string('yyyy-MM-dd'))!}</td>
			<td class="text-center">
			<#if item.examUeType?default('')=='4'>
				<a href="javascript:doSetSubject('${item.id!}','0');" class="table-btn show-details-btn">去设置</a>
			<#else>
				<a href="javascript:doSetSubject('${item.id!}','1');" class="table-btn show-details-btn">去查看</a>
			</#if>
			</td>
			<td class="text-center">
				<#if unitId==item.unitId>
				<a href="javascript:doEdit('${item.id!}','${item.examUeType!}');" class="table-btn show-details-btn">编辑</a>
				<a href="javascript:doDelete('${item.id!}');" class="table-btn color-red show-details-btn">删除</a>
				</#if>
			</td>
		</tr>
		</#list>
		
	</#if>												
	</tbody>
	</table>	
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	function addExam(){
	   	var url = "${request.contextPath}/exammanage/edu/examInfo/edit/page";
	    indexDiv = layerDivUrl(url,{title: "信息",width:750,height:550});
	}
	function doDelete(id){
	     var ii = layer.load();
	     $.ajax({
				url:'${request.contextPath}/exammanage/edu/examInfo/delete',
				data: {'id':id},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
	                    layer.closeAll();
						layer.msg(jsonO.msg, {
								offset: 't',
								time: 2000
							});
					  	showList();
			 		}
			 		else{
			 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
			 			isSubmit=false;
					}
					layer.close(ii);
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
	}
	
	function doEdit(id){
	   var url = "${request.contextPath}/exammanage/edu/examInfo/edit/page?id="+id;
	   indexDiv = layerDivUrl(url,{title: "信息",width:750,height:550});
	}
	
	function doSetSubject(examId,isView){
		var url =  '${request.contextPath}/exammanage/edu/subjectClassIndex/index/page?examId='+examId+'&isView='+isView;
		$("#exammanageDiv").load(url);
	}
</script>
