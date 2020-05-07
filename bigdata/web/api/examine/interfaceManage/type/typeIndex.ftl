<div class="box-header clearfix">
        <div class="filter-made">
            <div class="filter-item">
                <span class="filter-name">类型名称：</span>
                <div class="filter-content">
                    <input type="text" id="typeName" class="form-control">
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">类别：</span>
                <div class="filter-content">
                    <select id="classify" class="form-control" onChange="showInterTypeList()">
                       <option value="-1">全部</option>
                       <option value="1" >获取接口类型</option>
                       <option value="2" >获取结果类型</option> 
                       <option value="3" >获取公用类型</option>   
                    </select>
                </div>
            </div>
            <button class="btn btn-blue"    onclick="showInterTypeList()">查找</button>
        	<div class="filter-item filter-item-right clearfix">
                <button class="btn btn-default" onclick="editType()">新增类型</button>
            </div>
        </div>
</div>
<div class="box-body height-calc-32">
   <div class="wrap-full scrollBar4 table-container">
   
   
   </div>
</div>
<script>
$(function(){
   showInterTypeList();
});
   
   
function modifyType(){
  var typeId = $(this).parents('tr').data('typeid');
  editType (typeId);
}

function showInterTypeList(){
  var typeName =$('#typeName').val();
  typeName=typeName.replace(/\s+/g,"");
  var classify = $('#classify').val();
  $(".table-container").load("${request.contextPath}/bigdata/api/interType/showIntefaceTypes?typeName="+typeName+"&classify="+classify);
}

//参数的保存
var isSubmit=false;
function saveInterfaceType(index,layero){
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
        url:"${request.contextPath}/bigdata/api/interType/saveType",
        data:dealDValue(".layer-api"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showLayerTips('success','保存成功!','t');
               showInterTypeList();
               layer.close(index);
            }else{
               showLayerTips4Confirm('error',data.message);
            }
        }
  })
}

//删除接口
function deleteType(){
	var typeid = $(this).parents('tr').data('typeid');
	showConfirmTips('prompt',"提示","您确定要删除这条类型吗？",function(){
        $.ajax({
            url: "${request.contextPath}/bigdata/api/interType/delType?typeId="+typeid,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                	showLayerTips('success','删除成功!','t',showInterTypeList);
                }else{
                	showLayerTips4Confirm('error',data.message);
                }
            }
        });
    });
} 

function editType (typeId){
	  var titleContent = '新增类型';
	  if(typeId){
		  titleContent = '修改类型';
	  }
	  $('.layer-api').load("${request.contextPath}/bigdata/api/interType/editType?typeId="+typeId,function(){			
	  layer.open({
				type: 1,
				shade: .6,
				title: titleContent,
				area: '500px',
				btn: ['保存', '取消'],
				yes:function(index,layero){
					saveInterfaceType(index,layero);
	            },
				content: $('.layer-api')
	           })
	  });
}
</script>