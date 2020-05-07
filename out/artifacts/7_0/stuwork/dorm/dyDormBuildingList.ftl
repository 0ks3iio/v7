<div class="box print">
	<div class="tab-pane chosenSubjectHeaderClass">
        <div class="filter filter-f16">
            <div class="filter">
			<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="addBuilding()">新增寝室楼</a>
			</div>
        </div>
    </div>
    <table class="table table-striped table-hover table-layout-fixed no-margin">
      <thead>
          <tr>
                <th style="width:60%" class="text-center">寝室楼</th>
				<th style="width:40%" class="text-center">操作</th>
          </tr>
      </thead>
      <tbody>
      	  <#if dormBuildingList?exists && dormBuildingList?size gt 0>
      	  	  <#list dormBuildingList as building>
      	  	  	 <tr>
      	  	  	 	<td class="text-center">${building.name!}</td>
      	  	  	 	<td class="text-center">
      	  	  	 		<a href="javascript:" class="color-lightblue"  onclick="editBuilding('${building.id!}');return false;">修改</a>
      	  	  	 		<a href="javascript:" class="color-blue"  onclick="deleteBuilding('${building.id!}');return false;">删除</a>
      	  	  	 	</td>
      	  	  	 </tr>
      	  	  </#list>
      	  <#else>
	          <tr >
	          	<td colspan="2" align="center">
	          		暂无数据
	          	</td>
	          <tr>
          </#if>
      </tbody>
  </table>
</div>
<script>
	function addBuilding(){
		var url = "${request.contextPath}/stuwork/dorm/building/edit/page";
		indexDiv = layerDivUrl(url,{title: "寝室楼维护",width:500,height:200});
	}
	function editBuilding(id){
		var url = "${request.contextPath}/stuwork/dorm/building/edit/page?id="+id;
		indexDiv = layerDivUrl(url,{title: "寝室楼维护",width:530,height:200});
	}
	function deleteBuilding(id){
		showConfirmMsg('确认删除？','提示',function(){
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/stuwork/dorm/building/delete',
				data: {'id':id},
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
					  	itemShowList(1);
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