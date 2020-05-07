<a href="javascript:moduleContentLoad('${request.contextPath}/datacenter/examine/developer/index');" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="main-content-inner">
    <div class="">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <input id="developerId" type="hidden" value="${developerDto.id!}">
                <input id="ticketKey" type="hidden" value="${developerDto.ticketKey!}">
                <div class="box box-default">
                    <div class="box-body">
                        <h4><b>接口信息</b></h4>
	                        <ul class="nav nav-tabs" role="tablist">
								<li role="presentation" class="active">
								    <a href="#" onclick="javascript:lookInterface('aa','3');" role="tab" data-toggle="tab">
										审核中接口
									</a>
								</li>
								<li role="presentation">
								    <a href="#" onclick="javascript:lookInterface('bb','1');" role="tab" data-toggle="tab">
										已通过接口
									</a>
								</li>
								<li role="presentation">
								    <a href="#" onclick="javascript:lookInterface('cc','2');" role="tab" data-toggle="tab">
										未通过接口
									</a>
								</li>
							</ul>
	                        <div class="tab-content">
	                        
	                        </div>
				    </div>
				</div>
                <div class="box box-default" >
                    <div class="box-body">
                      <h4><b>单位列表</b></h4>
                      <div class="block" id = "developerEmpower">
                      
                      
                      </div>
                    </div>
                </div>
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div>
<div class="layer layer-scrollContainer layer-sensitive">

</div>
<script>
$(function(){
     lookInterface('aa','3');
});
   
function delInterface(type, tab){
  $.ajax({
    url:"${request.contextPath}/datacenter/examine/developer/delInterface",
    data:{'type':type,'ticketKey':$('#ticketKey').val()},
    type:'get',
    dataType:'json',
    success:function(result){
      if(result.success){
        showMsgSuccess('删除成功！','');
        if(tab == 'bb'){
        	lookInterface('bb','1');
        }else{
        	lookInterface('cc','2');
        }
        return;
      }
    }
  });
};
function loadPage2(url){
  moduleContentLoad(url);
}
//查看要通过订阅的接口
function showPassDiv(type){
	var url =  '${request.contextPath}/datacenter/examine/developer/checkInVerify?type='+type + '&developerId=' +$('#developerId').val();
	$('.layer-sensitive').load(url,function(){			
		layer.open({
               type: 1,
               shade: .5,
               title: ['审核通过','font-size:20px;'],
               area: '720px',
               btn: ['确定','取消'],
   yes:function(index,layero){
		savePassInter();
         },
         content: $('.layer-sensitive')
            });
	});
}
//接口审核不通过
function toUnPass(types){
  $.ajax({
    url:'${request.contextPath}/datacenter/examine/developer/unpassApply',
    data:{'types':types,'ticketKey':$('#ticketKey').val()},
    type:'post',
    dataType:'json',
    success:function(result){
      if(result.success){
          showSuccessMsgWithCall(result.msg,lookInterface('aa','3'));
      }else{
          showErrorMsg(result.msg);
      }
    }
  })
};
          
function lookInterface(activeId,type){
    var url =  '${request.contextPath}/datacenter/examine/developer/interface?developerId='+$('#developerId').val();
	$(".tab-content").load(url,{"activeId":activeId,"state":type});
};


//参数的保存
var isSubmit=false;
function saveParam(contextPath){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-sensitive');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-sensitive"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showSuccessMsgWithCall(data.msg,lookInterface('bb','1'));
            }else{
               showErrorMsg(data.msg);
            }
        }
  })
}
</script>
