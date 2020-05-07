<div class="layer-content">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">最大可带学生数：</span>
			<div class="filter-content">
				<input type="number" id="maxStuNum" class="form-control" value="${maxStuNum!}">
			</div>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/js/validate.js"></script>
<script>
$(function(){

});
var isSubmit = false;
function saveMaxNum(index){
	if(isSubmit){
        return;
    }
    var param = $("#maxStuNum").val();
    var maxStuNum=document.getElementById("maxStuNum")
    if(!checkInteger(maxStuNum,"可带学生数")){
        return;
    }
    var val=maxStuNum.value;
    if(val&&val.length>2){
	    addFieldError(maxStuNum,"可带学生数不超过2位数！");
        return;
    }
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/tutor/result/teacher/param/save",
			data:{"param":param},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
	    			isSubmit = false;
		 		}else{
		 			showTeaList();
					layerTipMsg(data.success,"保存成功","");
					layer.close(index);
    			}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
</script>