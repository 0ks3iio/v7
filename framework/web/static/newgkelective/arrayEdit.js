//ajax 复制功能
var isCopy=false;
function copyDivideItem(divideId){
	if(isCopy){
		return;
	}
	isCopy=true;
	layer.confirm('确定复制分班数据到新一轮吗？', function(index){
		var ii = layer.load();
		$.ajax({
			url:_contextPath+"/newgkelective/"+divideId+"/divideClass/copy",
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg("复制成功", {
						offset: 't',
						time: 2000
					});
					refreshEdit();
				}else{
					layerTipMsg(data.success,"失败","原因："+data.msg);
					isCopy=false;
				}
				layer.close(ii);
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	},function(){
		isCopy=false;
	});
}

// 删除场地
function deletePlItem(itemId){
	$.ajax({
	        url:_contextPath+'/newgkelective/'+nowDivideId+'/placeArrange/placeArrangeDelete',
	        data:{arrayItemId:itemId},
	        dataType : 'json',
	        success : function(data){
	 	       if(!data.success){
	 		      layerTipMsg(data.success,"删除失败",data.msg);
	 		      return;
	 	       }else{
	 	    	  $('#li'+itemId).remove();
	 	    	  layer.closeAll();
			      layer.msg(data.msg, {offset: 't',time: 2000});
		       }
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){} 
        });
}

// 删除排课特征
function deleteLsItem(arrayItemId){
	var url = _contextPath+"/newgkelective/"+arrayItemId+"/gradeArrange/delete";
	$.ajax({
		url: url,
		dataType: "json",
		success: function(data){
			if(data.success){
				$('#li'+arrayItemId).remove();
				layer.msg(data.msg, {offset: 't',time: 2000});
				layer.closeAll();
			}else{
				layerTipMsg(data.success,"失败",data.msg);
			}
		},
	});
}