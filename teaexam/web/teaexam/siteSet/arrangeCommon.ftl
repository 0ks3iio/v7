<script>
var oInterval = "";
function autoArrange(type){
	$('#ingArrange').show().siblings('div').hide();
	var sid = '';
	if(type== '0'){
		
		if($('#subInfoId')){
			sid = $('#subInfoId').val();
		}
	}
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/arrange/saveAuto';
	$.ajax({
            type: "POST",
            url: url,
            data: {'subInfoId':sid},
            success: function(msg){
                if(msg.success){
                    fnStartInterval();
                }else{
                    layerTipMsg(false,"失败",msg.msg);
                }
            },
            dataType: "JSON"
        });
}

function fnStartInterval(){
	oInterval = window.setTimeout("fnRecycle()",500);
}

function fnRecycle(){
	$.post("${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/arrange/autoMsg", {}, function(data) {
		if(data.code=='00'){
            var msg = data.msg;
            if(msg && msg != ''){
            	msg = '分配成功！，其中：'+msg;
            } else {
            	msg = '分配成功！';
            }
            layer.msg(msg, {
					offset: 't',
					time: 2000
				});
            toStu();
        }else if(data.code=='11'){
        	$('#ingArrange').show().siblings().hide();
        	oInterval = window.setTimeout("fnRecycle()",500);
        } else if(data.code == '-1'){
        	$('#reArrange').show().siblings().hide();	
        }
	}, 'json').error(function() {
		showMsgError("获取分配信息失败！");
    }); 
}
</script>