<form id="filterForm">
<input type="hidden" name="examId" value="${examId!}"/>
<div id="myDiv">
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th class="text-center">考区编号</th>
			<th class="text-center">考区名称</th>
			<th class="text-center">考点数</th>
 			<th class="text-center">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if emRegions?exists && (emRegions?size > 0)>
			<#list emRegions as dto>
				<tr>
					<td class="text-center"><input type="text" regionId="${dto.id!}" style="width:30%;" name="examCodeTxt" nullable="false" vtype="int" id="input_${dto_index!}" maxlength="5" min="1" value="${dto.examRegionCode!}" onblur="checkTab('${dto_index!}')"></td>
					<td class="text-center">${dto.regionName!}</td>
					<td class="text-center">${dto.examOptionNum!}</td>
					<td class="text-center">
						<a href="#aa"  id="aa" data-toggle="tab" aria-expanded="true" onclick="itemShowList('${dto.examId}','${dto.id!}')">设置考点</a>
					</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
</div>
</form>
<script>

function itemShowList(examId,regionId){
	<#--var url =  "${request.contextPath}/exammanage/edu/examArrange/examItemIndex2/page?examId="+examId+"&regionId="+regionId+"&type=1";-->
	var url =  '${request.contextPath}/exammanage/edu/examArrange/regionList/page?examId=${examId!}'+"&regionId="+regionId;
	aaa = true;
	$("#showTabDiv").load(url);
}

function doExamItem(){
	var url =  '${request.contextPath}/exammanage/edu/examArrange/examItemIndex/page?examId='+'${examId!}';
	$("#examArrangeDiv").load(url);	
}

function checkTab(index){
	setTimeout("if(!FamDearPlanService){itemSave("+index+")}",500);
}

function itemSave(index){
	var check = checkValue('#myDiv');
    if(check){
    var regionCode=$("#input_"+index).val();
	var regionId=$("#input_"+index).attr("regionId");
	$.ajax({
			url:'${request.contextPath}/exammanage/edu/examArrange/regionSave',
			data: {'regionCode':regionCode,'examId':'${examId!}','regionId':regionId},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				  	doExamItem();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"设置失败",jsonO.msg);
				}
				//layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
    }
}

</script>