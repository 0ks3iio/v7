<div class="wrap-full scrollBar4" >
    <table class="tables">
      <thead>
          <tr>
              <th>接口名称</th>
              <th>结果类型</th>
              <th>操作</th>
          </tr>
      </thead>
      <tbody>
          <#if openInterfaceDtos?exists &&(openInterfaceDtos?size gt 0)>
             <#list openInterfaceDtos as dto>
                 <tr data-type="${dto.type!}" data-metadata = "${dto.metadataId!}">
                     <td>${dto.typeName!}</td>
                     <td>${dto.type!}</td>
                     <td>
                        <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                        <a href="javascript:;" class="table-btn color-red delete">删除</a>
                     </td>
                 </tr>
             </#list>
          <#else>
			<tr>
				<td  colspan="3" align="center">
				 暂无属性
				</td>
			<tr>
		  </#if>
      </tbody>
  </table>
</div>


<script>
$(function(){
  $('.delete').on('click',deleteType);//删除属性
  $('.modify').on('click',modifyType);//修改属性
});
   
   
function modifyType(){
  var type = $(this).parents('tr').data('type');
  var metadataId = $(this).parents('tr').data('metadata');
  //$(".box-default #mainDiv").load("${request.contextPath}/bigdata/interface/entity/index?type="+type+"&metadataId=" + metadataId);
  router.go({
      path: '/bigdata/api/interEntity/index?type='+type+'&metadataId=' + metadataId,
      type: 'item',
      name: '修改详情',
      level: 3,
  }, function () {
      $('#mainDiv').load('${request.contextPath}/bigdata/api/interEntity/index?type='+type+'&metadataId=' + metadataId);
  })
}   
   
//删除类型属性
function deleteType(){
	var type = $(this).parents('tr').data('type');
	showConfirmTips('prompt',"提示","您确定要清空类型属性吗？",function(){
		$.ajax({
            url:"${request.contextPath}/bigdata/api/interEntity/delEntityByType?type="+type,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                    showLayerTips('success','成功清空!','t',showTab(3));
    	        }else{
    	            showLayerTips4Confirm('error',data.message);
    	        }
            }
        });
    });
} 
</script>