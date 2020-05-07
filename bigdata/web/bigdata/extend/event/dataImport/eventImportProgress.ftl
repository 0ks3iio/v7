<div class="data-list-wrap  height-1of1">
	<div class="data-list-header clearfix transfering">
		<div class="clearfix">
			<div class="float-left margin-r-10">导入进度</div>
			<div class="progress no-radius">
				  <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 0%;" id="progress">
			  		0%
			  	</div>
			</div>
			<div class="warning-word color-red">
				导入完成之前请不要关闭页面
			</div>
		</div>
		
		<div class="clearfix">
			<span class="margin-r-15">总记录数：<span class="color-blue" id="totalNum">-</span> 条</span>
			<span class="margin-r-15">成功记录数：<span class="color-green" id="correctNum">-</span> 条</span>
			<span>失败记录数：<span class="color-red" id="errorNum" >-</span> 条</span>
			<span>耗时：<span class="color-blue" id="duration" >-</span> 秒</span>
		</div>
	</div>
	<ul class=""  id="detailDiv"></ul>
</div>
<script type="text/javascript">
	var processInterval;
	
	function loadPogressData(){
		$.ajax({
		            url:"${request.contextPath}/bigdata/event/import/getProgress",
		            data:{
		              'businessId':'${businessId!}'
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
				 				$('#detailDiv').empty();
				 				showLayerTips4Confirm('error',response.message);
 								clearInterval(processInterval);
				 		}else{
				 			if(response.data != ""){
				 				var result = JSON.parse(response.data);
				 				var processData=JSON.parse(result.processData);
					 		    $('#totalNum').html(processData.totalNum);
					 		    $('#progress').html(processData.progress+"%");
								$('#progress').css("width",processData.progress+"%");
					 		    $('#correctNum').html(processData.correctNum);
					 		    $('#errorNum').html(processData.errorNum);
					 		    $('#duration').html(processData.duration);
					 		    if(result.detailData !=""){
				 					var detailData=JSON.parse(result.detailData);
						 		    $('#detailDiv').empty();
					 				for(var i in detailData){ //循环取detailData中的对象 
										 $('#detailDiv').append('<li>'+detailData[i]+'</li>');
									} 
								}
					 		    if(processData.totalNum ==processData.currentRowNum){
				 					clearInterval(processInterval);
				 				}
				 		    }
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){

		          }
		    });
	}

	$(document).ready(function(){          	
		$('#detailDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />数据解析中......</h4></div>");
		processInterval=setInterval(loadPogressData,500); 
	});
</script>