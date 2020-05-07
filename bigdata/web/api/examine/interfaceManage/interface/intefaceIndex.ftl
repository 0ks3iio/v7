<div class="box-header clearfix">
        <div class="filter-made">
            <div class="filter-item">
                  <span class="filter-name">接口名称：</span>
                  <div class="filter-content">
                      <input type="text" id="interfaceName" class="form-control">
                  </div>
             </div>
             <div class="filter-item">
                <span class="filter-name">调用类型：</span>
                <div class="filter-content">
                    <select id="dataType" class="form-control" onChange="showInterfaceList()">
                       <option value="-1">全部</option>
                       <option value="1" >获取基础接口</option>
                       <option value="2" >获取业务接口</option>  
                       <option value="3" >保存数据接口</option> 
                       <option value="4" >更新数据接口</option> 
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">状态：</span>
                <div class="filter-content">
                    <select id="isUsing" class="form-control" onChange="showInterfaceList()">
                        <option value="-1" >全部</option>
                        <option value="1"  >启用</option>
                        <option value="0"  >停用</option>
                    </select>
                </div>
            </div>
            <button class="btn btn-blue"    onclick="showInterfaceList()">查找</button>
        	<div class="filter-item filter-item-right clearfix">
                <button class="btn btn-default" onclick="modifyInte()">新增接口</button>
            </div>
        </div>
</div>
<div class="box-body height-calc-32">
   <div class="wrap-full scrollBar4 table-container">
   
   
   </div>
</div>
<script>
$(function(){
   showInterfaceList();
});
   
function startInte(){
  var inteId = $(this).parents('tr').data('interid');
  updateInterface("${request.contextPath}/bigdata/api/inteManager/isUsingInterface?interfaceId="+inteId+"&isUsing=1");
}  

function stopInte(){
  var inteId = $(this).parents('tr').data('interid');
  updateInterface("${request.contextPath}/bigdata/api/inteManager/isUsingInterface?interfaceId="+inteId+"&isUsing=0");
} 

function modifyDetails(){
  var inteId = $(this).parents('tr').data('interid');
  router.go({
      path: '/bigdata/api/interParam/showIndex?interfaceId='+inteId,
      type: 'item',
      name: '修改详情',
      level: 3,
  }, function () {
      $('#mainDiv').load('${request.contextPath}/bigdata/api/interParam/showIndex?interfaceId='+inteId);
  })
} 
   
function modifyInte(){
  var interid = $(this).parents('tr').data('interid');
  var titleContent = '新增接口';
  if(interid){
	  titleContent = '修改接口';
  }
  $('.layer-api').load("${request.contextPath}/bigdata/api/inteManager/editInterface?interfaceId="+interid,function(){			
  layer.open({
			type: 1,
			shade: .5,
			title: titleContent,
			area: '500px',
			btn: ['保存', '取消'],
			yes:function(index,layero){
			   saveInterface(index,layero);
            },
			content: $('.layer-api')
           })
  });
}


function showInterfaceList(){
  var interfaceName =$('#interfaceName').val();
  interfaceName=interfaceName.replace(/\s+/g,"");
  var dataType = $('#dataType').val();
  var isUsing = $('#isUsing').val();
  $(".table-container").load("${request.contextPath}/bigdata/api/inteManager/showInterfaces?typeName="+interfaceName+"&isUsing="+isUsing+"&dataType="+dataType);
}

//参数的保存
var isSubmit=false;
function saveInterface(index,layero){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-api');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:"${request.contextPath}/bigdata/api/inteManager/saveInterface",
        data:dealDValue(".layer-api"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
                showLayerTips('success','保存成功!','t',showInterfaceList);
                layer.close(index);
            }else{
                showLayerTips4Confirm('error',data.message);
            }
        }
  })
}

//启用或停用
function updateInterface(contextPath){
	$.ajax({
        url:contextPath,
        data:{},
        dataType:'json',
        contentType:'application/json',
        type:'GET',
        success:function (data) {
            if(data.success){
            	showLayerTips('success','更新成功!','t',showInterfaceList);
            }else{
            	showLayerTips4Confirm('error',data.message);
            }
        }
    });
}

//删除接口
function deleteInte(){
	var inteId = $(this).parents('tr').data('interid');
	showConfirmTips('prompt',"提示","您确定要删除这条接口吗？",function(){
		$.ajax({
            url:"${request.contextPath}/bigdata/api/inteManager/delInterface?interfaceId="+inteId,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                	showLayerTips('success','删除成功!','t',showInterfaceList);
                }else{
                	showLayerTips4Confirm('error',data.message);
                }
            }
        });
    });
} 

</script>