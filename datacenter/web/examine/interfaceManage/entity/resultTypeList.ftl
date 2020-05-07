<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body">
                <div class = "table-container"> 
                    <div class="table-container-body">
		                  <table class="table table-striped table-hover no-margin">
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
		                                  <tr data-type="${dto.type!}">
		                                      <td>${dto.typeName!}</td>
		                                      <td>${dto.type!}</td>
		                                      <td>
		                                         <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
		                                         <a href="javascript:;" class="table-btn color-red delete">删除</a>
		                                      </td>
		                                  </tr>
		                              </#list>
		                          </#if>
		                      </tbody>
		                  </table>
                     </div>
	            </div> <!-- table-container -->
            </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->
<div class="layer layer-interface-detailedId ">

</div><!-- E 登记记录 -->

<script>
$(function(){
  $('.delete').on('click',deleteType);//删除接口
  $('.modify').on('click',modifyType);//修改应用
});
   
   
function modifyType(){
  var type = $(this).parents('tr').data('type');
  $("#tabList").load("${request.contextPath}/datacenter/examine/interface/entity/index?type="+type);
}   
   
//删除类型属性
function deleteType(){
	var type = $(this).parents('tr').data('type');
	var that = $(this);
	var index = layer.confirm("是否清空类型属性？", {
	 btn: ["确定", "取消"]
	}, 
	function(){
		$.ajax({
            url:"${request.contextPath}/datacenter/examine/interface/entity/delEntityByType?type="+type,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,showTab(3));
                }else{
                    showErrorMsg(data.msg);
                }
            }
        });
	  layer.close(index);
	})
} 
</script>