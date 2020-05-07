<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body">
                <div class="filter filter-f16">
                    <a href="javascript:void(0);" id="addInterType" class="btn btn-blue pull-right">新增类型</a>
                    <div class="filter-item">
                        <span class="filter-name">类型名称：</span>
                        <div class="filter-content">
                            <input type="text" id="typeName" class="form-control">
                        </div>
                    </div>
                     <div class="filter-item">
                        <span class="filter-name">类别：</span>
                        <div class="filter-content">
                            <select id="classify" class="form-control">
                               <option value="-1">全部</option>
                               <option value="1" >获取接口类型</option>
                               <option value="2" >获取结果类型</option> 
                               <option value="3" >获取公用类型</option>   
                            </select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <a href="javascript:void(0);" id="findInterType" class="btn btn-blue">查找</a>
                    </div>
                </div>
                <div class = "table-container"> 
                   
	            </div> <!-- table-container -->
            </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->
<div class="layer layer-intertype-detailedId ">

</div><!-- E 登记记录 -->

<script>
$(function(){
  $('#findInterType').on('click',showInterTypeList);
  showInterTypeList();
});
   
//登记接口
$('#addInterType').on('click', function(e){
	editType ();
})
   
function modifyType(){
  var typeId = $(this).parents('tr').data('typeid');
  editType (typeId);
}

function showInterTypeList(){
  var typeName =$('#typeName').val();
  typeName=typeName.replace(/\s+/g,"");
  var classify = $('#classify').val();
  $(".table-container").load("${request.contextPath}/system/interface/type/showIntefaceTypes?typeName="+typeName+"&classify="+classify);
}

//参数的保存
var isSubmit=false;
function saveInterfaceType(){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-intertype-detailedId');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:"${request.contextPath}/system/interface/type/saveType",
        data:dealDValue(".layer-intertype-detailedId"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showSuccessMsgWithCall(data.msg,showInterTypeList);
            }else{
               showErrorMsg(data.msg);
            }
        }
  })
}

//删除接口
function deleteType(){
	var typeid = $(this).parents('tr').data('typeid');
	var index = layer.confirm("是否删除这条类型？", {
	 btn: ["确定", "取消"]
	}, 
	function(){
		$.ajax({
            url:"${request.contextPath}/system/interface/type/delType?typeId="+typeid,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,showInterTypeList);
                }else{
                    showErrorMsg(data.msg);
                }
            }
        });
	  layer.close(index);
	})
} 

function editType (typeId){
	  var titleContent = '新增类型';
	  if(typeId){
		  titleContent = '修改类型';
	  }
	  $('.layer-intertype-detailedId').load("${request.contextPath}/system/interface/type/editType?typeId="+typeId,function(){			
	  layer.open({
				type: 1,
				shade: .5,
				title: titleContent,
				area: '500px',
				btn: ['确定','取消'],
				yes:function(index,layero){
					saveInterfaceType();
	            },
				content: $('.layer-intertype-detailedId')
	           })
	  });
}

</script>