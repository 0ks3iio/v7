<div class="box-default">
   <div class="data-checking">
       <!--校验三种状态-->
       	<button class="btn btn-blue" onclick="checkData();" id="checkData">校验数据</button>
       	<button class="btn btn-default" onclick="analysisData();" id="analysisData">开始分析</button>
    </div>
    <ul class="data-checking-list data-list-bordered" id="messul">
	</ul>
								       
</div>
<script type="text/javascript">
$(function(){
	showBreadBack(goBack,true,"成绩统计");
	//$('.gotoScoreSetClass').on('click',function(){
		//var url =  '${request.contextPath}/exammanage/scoreStat/head/page';
		//$("#scoreStatDiv").load(url);
	//});
	<#if type?default('0')=='0'>
		$("#analysisData").addClass("disabled");
	<#elseif type?default('0')=='1'>
		$("#checkData").addClass("disabled");
		$("#analysisData").addClass("disabled");
		checkData2();
	<#else>
		$("#checkData").addClass("disabled");
		$("#analysisData").addClass("disabled");
		analysisData2();
	</#if>
	
});
function goBack(){
	var url =  '${request.contextPath}/exammanage/scoreStat/head/page';
	$("#scoreStatDiv").load(url);
}
function checkData(){
	if($("#checkData").hasClass("disabled")){
		return;
	}
	makeMess("goStat","准备数据校验...");
	
	$("#checkData").addClass("disabled");
	checkData2();
}

function checkData2(){
	$.ajax({
	  	url : "${request.contextPath}/exammanage/scoreStat/verificationData",
	  	data:{"examId":'${examId!}'},
		dataType : 'json',
		success : function(data){
			var obj=data;
 			if(obj.type=="success"){
 				makeMess("success",obj.mess,"校验");
	 			$("#analysisData").removeClass("disabled");
 			}else if(obj.type=="error"){
 				$("#checkData").removeClass("disabled");
	 			makeMess("error",obj.mess,"校验");
 			}else{
 				makeMess("",obj.mess,"校验");
 				//循环访问结果
 				window.setTimeout("checkData2()",800);
 			}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
	
	
}

function showSuccess(examId){
	var url = "${request.contextPath}/exammanage/scoreStat/showSuccess/page?examId="+examId;
	$("#scoreStatDiv").load(url);
}

function analysisData(){
	if($("#analysisData").hasClass("disabled")){
		return;
	}
	$("#messul").html('');
	makeMess("goStat","准备数据统计分析...");
	$("#analysisData").addClass("disabled");
	analysisData2();
}
function analysisData2(){
	$.ajax({
	  	url : "${request.contextPath}/exammanage/scoreStat/analysisData",
	  	data:{"examId":'${examId!}'},
		dataType : 'json',
		success : function(data){
			var obj = data;
 			if(obj.type=="success"){
 				makeMess("success",obj.mess);
	 			$("#analysisData").removeClass("disabled");
	 			showSuccess('${examId!}');
 			}else if(obj.type=="error"){
	 			makeMess("error",obj.mess);
	 			$("#analysisData").removeClass("disabled");
 			}else{
 				makeMess("",obj.mess);
 				//循环访问结果
 				window.setTimeout("analysisData2()",800);
 			}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}


function makeMess(type,mess){
	var date = new Date();
	var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var time1=+date.getFullYear() + seperator1 + month + seperator1 + strDate;
    var hours=date.getHours();
    if(hours>=1 && hours<=9){
    	hours="0"+hours;
    }
    var minutes=date.getMinutes();
    if(minutes>=0 && minutes<=9){
    	minutes="0"+minutes;
    }
    var seconds=date.getSeconds();
    if(seconds>=0 && seconds<=9){
    	seconds="0"+seconds;
    }
    var time2=hours + seperator2 + minutes + seperator2 + seconds;
    var $html='<li><span class="col-xs-5" title="'+mess+'"><i class="fa fa-check-circle color-green">';
    if(type=="error"){
    	 $html='<li><span class="col-xs-5 color-red" title="'+mess+'"><i class="fa fa-times-circle">';
    }
    if(type=="goStat"){
    	$html='<li id="goStat"><span class="col-xs-5"><i class="data-loading-s-blue">';
    }
    if(mess.length>25){
    	mess=mess.substring(0,25);
    	mess=mess+"...";
    }
	var $html=$html+'</i>'+mess+'</span><a href="" class="col-xs-1 color-blue"></a>'
	+'<span class="col-xs-4"><em>'+time1+'</em><em> '+time2+'</em></span>';
	if(type != "goStat"){
		$('#goStat').after($html);
	}else{
		$("#messul").append($html);
	}
	if(type=="error"){
		$('#goStat').remove();
	}
	if(type=="success"){
		$('#goStat').remove();
	}
}

</script>