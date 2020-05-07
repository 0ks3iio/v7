<a href="javascript:" onclick="showTab('2')" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item filter-item-right">
				<a href="javascript:void(0);"  class="btn btn-blue js-checkIn">新增属性</a>
			</div>
		</div>
		<div class="table-container" id="showEntityList">
		
		
		</div>
	</div>
</div>
<div class="layer layer-entity-detailedId ">

</div><!-- E 登记记录 -->


<script> 
$(document).ready(function(){
  showEntityList();
})
function showEntityList(){
	var url =  '${request.contextPath}/system/interface/entity/showEntityList?type='+'${type!}';
	$(".table-container").load(url);
}

//登记属性
$('.filter .js-checkIn').on('click', function(e){
	$('.layer-entity-detailedId').load("${request.contextPath}/system/interface/entity/editEntity?type="+'${type!}',function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: '新增属性',
				area: '500px',
				btn: ['确定','取消'],
				yes:function(index,layero){
				   saveEntity("${request.contextPath}/system/interface/entity/saveEntity");
	            },
				content: $('.layer-entity-detailedId')
	           })
    });
})

//参数的保存
var isSubmit=false;
function saveEntity(contextPath){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-entity-detailedId');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-entity-detailedId"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showSuccessMsgWithCall(data.msg,showEntityList);
            }else{
               showErrorMsg(data.msg);
            }
        }
  })
}
</script>