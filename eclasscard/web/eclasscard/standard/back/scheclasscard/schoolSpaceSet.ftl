<div class="filter">
	<div class="filter-item filter-item-left">
		<span class="filter-name">校园空间链接：</span>
		<div class="filter-content">
			<input type="text" class="form-control" style="width:300px;" id="urlParm" placeholder="请输入链接" value="${urlParm!}">
		</div>
	</div>
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:void(0);" id="saveUrlBtn">保存</a>
	</div>
</div>
<script type="text/javascript">
var isSaveSubmit=false;
$(function(){
	$("#saveUrlBtn").on("click",function(){
		if(isSaveSubmit){
			return;
		}
		isSaveSubmit=true;
		var url = $("#urlParm").val();
		if(url!=''){
			if(getLength(url)>500){
				layer.tips('长度不能超过500个字节！',$("#urlParm"), {
					tipsMore: true,
					tips: 3
				});
				isSaveSubmit=false;
				return;	
			}
			var reg = /^(http|https):\/\/.*$/;
			if(!reg.test(url)){
				layer.tips('链接需以http://或https://开头', $("#urlParm"), {
		  			tips: 3
				});
				isSaveSubmit=false;
				return;
			}
		}
		$.ajax({
	    	url:"${request.contextPath}/eclasscard/standard/schoolSpace/save",
	        	type:"post",
	            data:{"parmUrl":url},
	            success:function(data){
	            	isSaveSubmit=false;
	            	var jsonO = JSON.parse(data);
	            	if(jsonO.success){
	            		layer.msg("链接保存成功");
	 				}else{
	 					layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
	            }
	        });
	})
})
</script>