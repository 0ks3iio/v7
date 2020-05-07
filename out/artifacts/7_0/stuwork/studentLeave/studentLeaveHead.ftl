<div class="main-content">
				
				<div class="main-content-inner">
					<div class="page-content">
						<div class="box box-default">
							<div class="box-header">
								<h4 class="box-title">学生请假</h4>
							</div>
							<div class="box-body">
								<div class="tab-content">
									<div id="aa" class="tab-pane active" role="tabpanel">

										<div class="filter">
											<div class="filter-item">
												<span class="filter-name">请假时间：</span>
												<div class="filter-content">
													<div class="input-group">
														<input type="text" id="startTime1" class="form-control datetimepicker" style="width:140px;" onChange="searchList();">
														<span class="input-group-addon">
															<i class="fa fa-minus"></i>
														</span>
														<input type="text" id="endTime1" class="form-control datetimepicker" style="width:140px;" onChange="searchList();">
													</div>
											</div>
										</div>
											
											
											<div class="filter-item">
										       <span class="filter-name">审核状态：</span>
										       <div class="filter-content">
											      <select name="" id="state" class="form-control" onChange="searchList();">
												     <option value="">--请选择--</option>
												     <option value="1">未提交</option>
												     <option value="2">待审核</option>
												     <option value="3">通过</option>
												     <option value="4">不通过</option>
											      </select>
										       </div>
									       </div>
										</div>																												
									</div>
									<div class="table-container-header text-right">
										<a class="btn btn-blue" onClick="toAdd();">新增</a>
									</div>
									<div class="table-container-body" id="showList">
									</div>
								</div>
							</div>
						</div>
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
<div id="cardEditLayer" class="layer layer-edit">
</div>	
<script>
$(function(){	
	$('.datetimepicker').datetimepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd hh:ii',
		autoclose: true
	});
	
	//$('.btn-blue').on('click',function(e){
    	//e.preventDefault();
    	//var url =  '${request.contextPath}/stuwork/studentLeave/edit';
		//$("#cardEditLayer").load(url,function() {
			//layerShow();
		//});
    //});	
	searchList();
});

function toAdd(){
    var url =  '${request.contextPath}/stuwork/studentLeave/edit';
	indexDiv = layerDivUrl(url,{title: "新增学生请假",width:380,height:420});
}

//function layerShow(){
     //layer.open({
	    	//type: 1,
	    	//shade: 0.5,
	    	//title: '新增学生请假',
	    	//area: ['380px','420px'],
	    	//content: $('.layer-edit')
	    //})
  //  }

function searchList(){
    var startTime = $('#startTime1').val();
    var endTime = $('#endTime1').val();
    var state = $('#state').val();
    if(startTime != '' && endTime != ''){
        if(startTime>endTime){
           layerTipMsgWarn("提示","开始时间不能大于结束时间");
	       return;
        }
        var url = "${request.contextPath}/stuwork/studentLeave/studentLeaveList?startTime="+startTime+"&endTime="+endTime+"&state="+state;
        $("#showList").load(url.replaceAll(" ",","));
    }else if(startTime == '' && endTime == ''){
        var url = "${request.contextPath}/stuwork/studentLeave/studentLeaveList?state="+state;
        $("#showList").load(url);
    }else if((startTime != '' && endTime == '') && state != ''){
        layerTipMsgWarn("提示","结束时间不能为空");
	    return;
    }else if((startTime == '' && endTime != '') && state != ''){
        layerTipMsgWarn("提示","开始时间不能为空");
	    return;
    }
    
}	
</script>