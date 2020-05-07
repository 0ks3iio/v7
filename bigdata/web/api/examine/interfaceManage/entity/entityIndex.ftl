<div class="box-header clearfix">
        <div class="filter-made">
        	<div class="filter-item filter-item-right clearfix">
                <button class="btn btn-default" onclick="modifyEntity()">新增属性</button>
            </div>
        </div>
</div>
<div class="box-body height-calc-32">
   <div class="wrap-full scrollBar4 table-container" id="showEntityList">
   
   
   </div>
</div>
<script> 
$(document).ready(function(){
  showEntityList();
})
function showEntityList(){
	var url =  '${request.contextPath}/bigdata/api/interEntity/showEntityList?type='+'${type!}'+"&metadataId=" +'${metadataId!}';
	$(".table-container").load(url);
}

//修改或新增属性
function modifyEntity(){
  var entityId = $(this).parents('tr').data('entityid');
  var titleContent = '新增属性';
  if(entityId){
	  titleContent = '修改属性';
  }
  $('.layer-api').load("${request.contextPath}/bigdata/api/interEntity/editEntity?type="+'${type!}' + "&metadataId=" +'${metadataId!}'
		  +"&entityId=" +entityId ,function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: titleContent,
				area: '500px',
				btn: ['确定','取消'],
				yes:function(index,layero){
				   saveEntity(index,layero);
	            },
				content: $('.layer-api')
	           })
    });
}
//参数的保存
var isSubmit=false;
function saveEntity(index,layero){
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
        url:"${request.contextPath}/bigdata/api/interEntity/saveEntity",
        data:dealDValue(".layer-api"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
                showLayerTips('success','保存成功!','t',showEntityList);
                layer.close(index);
	        }else{
	            showLayerTips4Confirm('error',data.message);
	        }
        }
  })
}

//删除属性
function deleteEntity(){
	var entityId = $(this).parents('tr').data('entityid');
	showConfirmTips('prompt',"提示","您确定要删除这条属性吗？",function(){
		$.ajax({
            url:"${request.contextPath}/bigdata/api/interEntity/delEntity?entityId="+entityId,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                    showLayerTips('success','删除成功!','t',showEntityList);
    	        }else{
    	            showLayerTips4Confirm('error',data.message);
    	        }
            }
        });
    });
}

function startEntity(){
	  var entityId = $(this).parents('tr').data('entityid');
	  updateEntity("${request.contextPath}/bigdata/api/interEntity/isUsingEntity?entityId="+entityId+"&isUsing=1");
}  

function stopEntity(){
  var entityId = $(this).parents('tr').data('entityid');
  updateEntity("${request.contextPath}/bigdata/api/interEntity/isUsingEntity?entityId="+entityId+"&isUsing=0");
} 

//启用或停用
function updateEntity(contextPath){
	$.ajax({
        url:contextPath,
        data:{},
        dataType:'json',
        contentType:'application/json',
        type:'GET',
        success:function (data) {
        	if(data.success){
                showLayerTips('success','更新成功!','t',showEntityList);
	        }else{
	            showLayerTips4Confirm('error',data.message);
	        }
        }
    });
}
</script>