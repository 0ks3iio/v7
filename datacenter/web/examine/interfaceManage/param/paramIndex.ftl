<a href="javascript:" onclick="showTab('2')" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item filter-item-right">
				<a href="javascript:void(0);"  class="btn btn-blue js-checkIn">新增参数</a>
			</div>
		</div>
		<div class="table-container" id="showParamList">
		
		
		</div>
	</div>
</div>
<div class="layer layer-param-detailedId ">

</div><!-- E 登记记录 -->


<script> 
$(document).ready(function(){
  showParamList();
})
function showParamList(){
	var url =  '${request.contextPath}/datacenter/examine/interface/param/findParam?interfaceId='+'${interfaceId!}';
	$(".table-container").load(url);
}

//登记参数
$('.filter .js-checkIn').on('click', function(e){
	$('.layer-param-detailedId').load("${request.contextPath}/datacenter/examine/interface/param/editParam?interfaceId="+'${interfaceId!}',function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: '新增参数',
				area: '500px',
				btn: ['确定','取消'],
				yes:function(index,layero){
				   saveParam("${request.contextPath}/datacenter/examine/interface/param/saveParam");
	            },
				content: $('.layer-param-detailedId')
	           })
    });
})

//参数的保存
var isSubmit=false;
function saveParam(contextPath){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-param-detailedId');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-param-detailedId"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showSuccessMsgWithCall(data.msg,showParamList);
            }else{
               showErrorMsg(data.msg);
            }
        }
  })
}
</script>