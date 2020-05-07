<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body">
                <div class="filter filter-f16">
                    <a href="javascript:void(0);" id="addInterface" class="btn btn-blue pull-right">新增接口</a>
                    <div class="filter-item">
                        <span class="filter-name">接口名称：</span>
                        <div class="filter-content">
                            <input type="text" id="interfaceName" class="form-control">
                        </div>
                    </div>
                     <div class="filter-item">
                        <span class="filter-name">调用类型：</span>
                        <div class="filter-content">
                            <select id="dataType" class="form-control">
                               <option value="-1">全部</option>
                               <option value="1" >获取基础接口</option>
                               <option value="2" >获取业务接口</option>  
                               <option value="3" >推送基础接口</option>                         
                            </select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <span class="filter-name">状态：</span>
                        <div class="filter-content">
                            <select id="isUsing" class="form-control">
                                <option value="-1" >全部</option>
                                <option value="1"  >启用</option>
                                <option value="0"  >停用</option>
                            </select>
                        </div>
                    </div>
                   
                    <div class="filter-item">
                        <a href="javascript:void(0);" id="findInterface" class="btn btn-blue">查找</a>
                    </div>
                </div>
                <div class = "table-container"> 
                
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
  $('#findInterface').on('click',showInterfaceList);
  showInterfaceList();
});
   
//登记接口
$('#addInterface').on('click', function(e){
	$('.layer-interface-detailedId').load("${request.contextPath}/system/interface/editInterface",function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: '新增接口',
				area: '500px',
				btn: ['确定','取消'],
				yes:function(index,layero){
				   saveInterface("${request.contextPath}/system/interface/saveInterface");
	            },
				content: $('.layer-interface-detailedId')
	           })
    });
})

function startInte(){
  var inteId = $(this).parents('tr').data('interid');
  updateInterface("${request.contextPath}/system/interface/isUsingInterface?interfaceId="+inteId+"&isUsing=1");
}  

function stopInte(){
  var inteId = $(this).parents('tr').data('interid');
  updateInterface("${request.contextPath}/system/interface/isUsingInterface?interfaceId="+inteId+"&isUsing=0");
} 

function modifyDetails(){
  var inteId = $(this).parents('tr').data('interid');
  $("#tabList").load("${request.contextPath}/system/interface/param/showIndex?interfaceId="+inteId);
} 
   
function modifyApp(){
  var interid = $(this).parents('tr').data('interid');
  $('.layer-interface-detailedId').load("${request.contextPath}/system/interface/editInterface?interfaceId="+interid,function(){			
  layer.open({
			type: 1,
			shade: .5,
			title: '修改参数',
			area: '500px',
			btn: ['确定','取消'],
			yes:function(index,layero){
			   saveInterface("${request.contextPath}/system/interface/saveInterface");
            },
			content: $('.layer-interface-detailedId')
           })
  });
}


function showInterfaceList(){
  var interfaceName =$('#interfaceName').val();
  interfaceName=interfaceName.replace(/\s+/g,"");
  var dataType = $('#dataType').val();
  var isUsing = $('#isUsing').val();
  $(".table-container").load("${request.contextPath}/system/interface/findInterfaces?typeName="+interfaceName+"&isUsing="+isUsing+"&dataType="+dataType);
}

//参数的保存
var isSubmit=false;
function saveInterface(contextPath){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-interface-detailedId');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-interface-detailedId"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showSuccessMsgWithCall(data.msg,showInterfaceList);
            }else{
               showErrorMsg(data.msg);
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
                showSuccessMsgWithCall(data.msg,showInterfaceList);
            }else{
                showErrorMsg(data.msg);
            }
        }
    });
}

//删除接口
function deleteInte(){
	var inteId = $(this).parents('tr').data('interid');
	var that = $(this);
	var index = layer.confirm("是否删除这条接口？", {
	 btn: ["确定", "取消"]
	}, 
	function(){
		$.ajax({
            url:"${request.contextPath}/system/interface/delInterface?interfaceId="+inteId,
            data:{},
            dataType:'json',
            contentType:'application/json',
            type:'GET',
            success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,showInterfaceList);
                }else{
                    showErrorMsg(data.msg);
                }
            }
        });
	  layer.close(index);
	})
} 
</script>