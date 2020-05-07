<div id="inputIndex" >
</div>
<div id="showTutorIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	showIndex();
});
function showIndex(){
	var url =  '${request.contextPath}/stuwork/health/inputIndex';
	$("#inputIndex").load(url);
}

function showSuccessMsg(msg) {
	    showMsg("成功",msg,true);
	}
function showErrorMsg(msg) {
    showMsg("失败",msg,false);
}
function showMsg(title,msg,success,call){
    layer.alert(msg,{
        icon:success?1:2,
        title:title,
        btn:['确定'],
        end:function () {
            if (call!=null && call instanceof Function) {
                call();
            }
        },
        yes:function (index) {
            if (success){
                layer.closeAll();
            } else {
                layer.close(index);
            }
        }
    });
}
</script>