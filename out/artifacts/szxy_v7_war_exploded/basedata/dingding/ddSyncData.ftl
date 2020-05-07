<script>
	function syncData2DD(){
		$.ajax({
			url:'${request.contextPath}/syncdata/dingding/handlebyHand',
			type:'post',
			success:function(data) {
				layerTipMsg("提示",data);
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
	 			
			}
		});
	}
	
	function sendMsg2DD(){
		$.ajax({
			url:'${request.contextPath}/syncdata/dingding/sendMsg',
			type:'post',
			success:function(data) {
				layerTipMsg("提示",data);
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
	 			
			}
		});
	}
	
	function dingdingInit(){
		layerTipMsg("提示","此功能暂未实现");
	}
</script>
<div class="box box-default box-roleEntry">
	<div class="box-body">
		<h3><span>钉钉数据同步</span></h3>
		<a class="btn btn-block btn-blue text-left" href="javascript:void(0);" onclick="syncData2DD();">同步(部门、用户)<i class="fa fa-long-arrow-right"></i></a>
		<br>
		<a class="btn btn-block btn-blue text-left" href="javascript:void(0);" onclick="dingdingInit();">钉钉数据初始化<i class="fa fa-long-arrow-right"></i></a>
	</div>
</div>