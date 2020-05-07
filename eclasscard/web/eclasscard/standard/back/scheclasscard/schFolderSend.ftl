<#import "/eclasscard/common/back/groupSelection.ftl" as group />
<form id="submitForm">
<input type="hidden" id="id" name="id" value="${id!}">
<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group ">
			<label class="col-sm-1 control-label no-padding-right">发布对象&nbsp;</label>
			<div class="col-sm-11">
			<@group.group_div dataUrl="${request.contextPath}/eclasscard/group/eccinfo/groupData" id="eccInfoIds">
				<input type="hidden" id="eccInfoIds" name="eccInfoIds"  class="form-control" value="${eccInfoIds!}">
			</@group.group_div>
			</div>
		</div>
	</div>
</div>

</form>
<script type="text/javascript">
var isSubmit = false;
function sendFolderTo(index){
	if(isSubmit){
        return;
    }
	isSubmit = true;
	
	var options = {
			url : "${request.contextPath}/eclasscard/standard/send/types",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
				  	showContent('3');
	    			layer.close(index);
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
}

function notShowMultimedia(index){
	if(isSubmit){
        return;
    }
	isSubmit = true;
	
	var id = $("#id").val();
	$.ajax({
		url:'${request.contextPath}/eclasscard/standard/send/notshow',
		data: {'id':id},
		type:'post',
		dataType : 'json',
		success:function(data) {
			if(!data.success){
		 		layerTipMsg(data.success,"设置失败",data.msg);
		 		isSubmit = false;
		 	}else{
		 		layer.closeAll();
				showContent('3');
	    		layer.close(index);
    		}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</script>