<div class="box-header clearfix">
        <div class="filter-made">
        	<div class="filter-item filter-item-right clearfix">
                <button class="btn btn-default" onclick="modifyParam()">新增参数</button>
            </div>
        </div>
</div>
<div class="box-body height-calc-32">
   <div class="wrap-full scrollBar4 table-container" id="showParamList">
   
   
   </div>
</div>
<script> 
$(document).ready(function(){
  showParamList();
})
function showParamList(){
	var url =  '${request.contextPath}/bigdata/api/interParam/findParam?interfaceId='+'${interfaceId!}';
	$(".table-container").load(url);
}

//登记参数
function modifyParam(){
  var paramId = $(this).parents('tr').data('paramid');
  var titleContent = '新增参数';
  if(paramId){
	  titleContent = '修改参数';
  }
  $('.layer-api').load("${request.contextPath}/bigdata/api/interParam/editParam?interfaceId="+'${interfaceId!}' + "&paramId="+paramId,function(){		
  layer.open({
			type: 1,
			shade: .5,
			title: titleContent,
			area: '500px',
			btn: ['保存', '取消'],
			yes:function(index,layero){
				saveParam(index,layero);
            },
			content: $('.layer-api')
           })
  });
}

//参数的保存
var isSubmit=false;
function saveParam(index,layero){
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
        url:"${request.contextPath}/bigdata/api/interParam/saveParam",
        data:dealDValue(".layer-api"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
                showLayerTips('success','保存成功!','t',showParamList);
                layer.close(index);
            }else{
                showLayerTips4Confirm('error',data.message);
            }
        }
  })
}

//删除接口
function deleteParam(){
	var paramId = $(this).parents('tr').data('paramid');
	showConfirmTips('prompt',"提示","您确定要删除这条参数吗？",function(){
		$.ajax({
            url:"${request.contextPath}/bigdata/api/interParam/delParam?paramId="+paramId,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                    showLayerTips('success','删除成功!','t',showParamList);
                }else{
                    showLayerTips4Confirm('error',data.message);
                }
            }
        });
    });
} 
</script>