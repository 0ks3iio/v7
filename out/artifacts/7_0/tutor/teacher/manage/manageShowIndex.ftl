<div id="mangeStudentIndex" >
</div>
<div id="showRecordIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	mangeStudentIndex();
});
function mangeStudentIndex(){
	var url =  '${request.contextPath}/tutor/manage/showStudentIndex';
	$("#mangeStudentIndex").load(url);
	$("#mangeStudentIndex").show();
	$("#showRecordIndex").hide();
}
function showRecordIndex(studentId){
	var url =  '${request.contextPath}/tutor/manage/showRecordIndex?studentId='+studentId;
	$("#showRecordIndex").load(url);
	$("#showRecordIndex").show();
	$("#mangeStudentIndex").hide();
}
//从desktop.js中引用的方法
    function dealDValue(container){
    var tags = ["input","select","textarea"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var value = $this.val();
            var type = $this.attr("type");
            if ((type == 'number' || type=='int') && value!='' && value!=null) {
                value = parseInt(value);
            }
            var name = $this.attr("name");
            name = name || $this.attr("id");
            var exclude = $this.attr("exclude");
            if (!exclude) {
                if (obj[name] && !(obj[name] instanceof Array)) {
                    var array = new Array();
                    array.push(obj[name]);
                    array.push(value);
                    obj[name] = array;
                } else if (obj[name] && obj[name] instanceof Array){
                    obj[name].push(value);
                }else{
                    obj[name] = value;
                }
            }
        });
    }
    return JSON.stringify(obj);
}

   function showSuccessMsgWithCall(msg, call){
	    if (!(call == null || !(call instanceof Function))) {
	        showMsg("成功", msg, true, call);
	    } else {
	        showSuccessMsg(msg)
	    }
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
function layerError(key,msg){
    layer.tips(msg, key, {
        tipsMore: true,
        tips:2,
        time:3000
    });
}

</script>