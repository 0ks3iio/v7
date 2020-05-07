<div id="resultListDiv">
	<table class="table table-bordered table-striped mainTable">
	    <thead>
	        <tr>
	            <th width="10%"><#if searchViewTypeRedio?default('') == '1'>${PCKC!}<#elseif searchViewTypeRedio?default('') == '2'>科目</#if></th>
	            <th width="15%">班级</th>
	            <th width="10%">选考类型</th>
	            <th ><#if searchViewTypeRedio?default('') == '1'>科目<#elseif searchViewTypeRedio?default('') == '2'>${PCKC!}</#if></th>
	            <th >总人数</th>
	            <th >男生</th>
	            <th >女生</th>
	            <th >班级平均分</th>
	            <th class="noprint">查看学生</th>
	        </tr>
	    </thead>
	    <tbody>
		<#if gkBatchMap?? && (gkBatchMap?size>0)>
			<#list gkBatchMap?keys as key>
				<#list gkBatchMap[key] as item>
					<tr>
						<#if item_index == 0>
							<td rowSpan='${gkBatchMap[key]?size}'>${key}</td>
						</#if>
						<td>${item.className!}</td>
						<td><#if item.classType?default('') == 'A'>选考<#else>学考</#if></td>
						<td><#if searchViewTypeRedio?default('') == '1'>${item.subjectName!}<#elseif searchViewTypeRedio?default('') == '2'>${PCKC!}${item.batch!}</#if></td>
						<td>${item.number!}</td>
						<td>${item.manNumber!}</td>
						<td>${item.womanNumber!}</td>
						<td>${item.averageScore!}</td>
						<td class="noprint">
						<a href="#" onclick="showSingleStu('${item.id!}','${item.teachClassId!}')">查看</a>
						<#if (item.number?default(0) == 0)>
						<a href="javascript:" style="color:red" onclick="doDelClass('${item.id!}','${item.teachClassId!}')">删除</a>
						</#if>
						</td>
					</tr>
				</#list>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	function showSingleStu(batchId,teachClassId){
		var url = "${request.contextPath}/gkelective/${roundsId!}/openClassArrange/single/detail/page?batchId="+batchId+"&teachClassId="+teachClassId;
		$("#resultListDiv").load(url);
	}
	var isSubmit = false;
	function doDelClass(batchId,teachClassId){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/single/classDelete',
		    data:{'batchId':batchId,'teachClassId':teachClassId},
		    dataType : 'json',
		    success:function(data) {
		    	var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 		}else{
		 			layer.closeAll();
					reloadListData();
				}
	 			isSubmit = false;
				layer.close(ii);
		    }
		});
	}
</script>