<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<title>行政班首页</title>
	<script>
		_webSocketUrl = "${webSocketUrl!}";
		_eccIndexUrl = "${eccIndexUrl!}";
		_view = "${view!}";
		_sockJSUrl = "${sockJSUrl!}";
		_deviceNumber = "${deviceNumber!}";
		_contextPath = "${request.contextPath}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/css/style.css">
</head>
<body class="index">
	<header id="header" class="header">
		
	</header>
	<#include "/eclasscard/verticalshow/showMsgtip.ftl">
	<#include "/eclasscard/verticalshow/clockStateTip.ftl">
	<div class="main-container">
	<#if eccInfo.type=="10"||eccInfo.type=="30">
		<div class="sticky-container clearfix">
	</#if>
			<#if eccInfo.type=="10">
				<div class="box">	
					<div class="box-body">	
						<div id="classInfoDiv">
						</div>
						<div id="classDescDiv" class="class-summary-index">
						</div>
					</div>		
				</div>
				<div class="box">
					<div id="moralEducationDiv" class="box-body">				
					</div>
				</div>
				<div class="box">	
					<div class="box-header">
						<h3 class="box-title">通知公告</h3>
						<div class="box-header-tool">
						</div>
					</div>	
					<div class="box-body">
						<div id="bulletinDiv" class="post-container" style="height:138px;">		
						</div>
					</div>
				</div>
				<div id="albumDiv" class="box box-slider">
				</div>
			<#elseif eccInfo.type=="20">
				<div class="box">	
					<div id="tclassInfoDiv" class="box-body">			
					</div>
				</div>
				<div class="box">	
					<div class="box-header">
						<h4 class="box-title">通知公告</h4>
					</div>
					<div class="box-body">
						<div id="bulletinDiv" class="post-container" style="height:320px;">			
						</div>
					</div>
				</div>
				<div id="albumDiv" class="box box-slider">
				</div>
			<#elseif eccInfo.type=="30">
				<div class="box">	
					<div id="dormInfoDiv" class="box-body">			
					</div>
				</div>
				<div class="box">	
					<div class="box-body">
						<div class="img-title">
							<img src="${request.contextPath}/static/eclasscard/verticalshow/images/latest-dormitory.png" alt="">
						</div>
					</div>
					<div id="dormScoreDiv">
					</div>
					<div id="dormRemindDiv">
					</div>
				</div>
				<div class="box">	
					<div class="box-header">
						<h4 class="box-title">通知公告</h4>
					</div>
					<div class="box-body">
						<div id="bulletinDiv" class="post-container" style="height:320px;">			
						</div>
					</div>
				</div>
				<div class="grid">
					<div class="grid-cell grid-1of2">
						<div id="albumDiv" class="box box-slider">
						</div>
					</div>
					<div class="grid-cell grid-1of2">
					<div class="box">
						<div class="box-header">
							<div class="box-title">打卡情况</div>
						</div>
						<div class="box-body">
							<ul class="checkin-result-list">
								<div id="dormClockGradeDiv">
								</div>
								<div id="clockRecordDiv">
								</div>
							</ul>
						</div>
						<div  id="recordNodataDiv" class="no-data center">
							<div class="no-data-content">
								<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt="">
								<p>暂无打卡记录</p>
							</div>
						</div>
					</div>
				</div>
			<#else>
				<div class="box">
					<div class="box-header">
						<h4 class="box-title">打卡情况</h4>
					</div>
					<div id="clockRecordDiv" class="box-body">
					</div>
					<div  id="recordNodataDiv" class="no-data center">
						<div class="no-data-content">
							<img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt="">
							<p>暂无打卡记录</p>
						</div>
					</div>
				</div>
				<div class="box">	
					<div class="box-header">
						<h4 class="box-title">通知公告</h4>
					</div>
					<div class="box-body">
						<div id="bulletinDiv" class="post-container" style="height:320px;">			
						</div>
					</div>
				</div>
				<div id="albumDiv" class="box box-slider">
				</div>
			</#if>
	<#if eccInfo.type=="10"||eccInfo.type=="30">
		</div>
	</#if>
	</div>
	<footer id="footer" class="footer">
		
	</footer>

	<!-- S 通知公告弹出框 -->
	<div id="showDetailLayer" class="layer-post">
		
	</div><!-- E 通知公告弹出框 -->

<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/layer/layer.js"></script>
<script>
$(document).ready(function(){
	var headUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/showHeader?type=${eccInfo.type!}&back=1&view="+_view;
	var footUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/showFooter?type=1&name="+_deviceNumber+"&view="+_view;
	$("#header").load(headUrl);
	var	bulletinUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/bulletin?name="+_deviceNumber+"&view="+_view;
	var	albumUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/album?name="+_deviceNumber+"&view="+_view;
	$("#bulletinDiv").load(bulletinUrl);
	$("#albumDiv").load(albumUrl);
	var	clockRecordUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/clockIn?name="+_deviceNumber+"&view="+_view;
	<#if eccInfo.type=="10">
		var	classInfoUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/class/info?name="+_deviceNumber+"&view="+_view;
		var	classDescUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/description?name="+_deviceNumber+"&view="+_view;
		var	moralEducationUrl = "${request.contextPath}/eccShow/classdy/classpage?unitId=${eccInfo.unitId!}&classId=${eccInfo.classId!}&view="+_view;
		$("#classInfoDiv").load(classInfoUrl);
		$("#classDescDiv").load(classDescUrl);
		$("#moralEducationDiv").load(moralEducationUrl);
	<#elseif eccInfo.type=="20">
		var	tclassInfoUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/tclass/info?name="+_deviceNumber+"&view="+_view;
		$("#tclassInfoDiv").load(tclassInfoUrl);
	<#elseif eccInfo.type=="30">
		var	dormInfoUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/dorm/info?name="+_deviceNumber+"&view="+_view;
		var	dormScoreUrl =  "${request.contextPath}/eccShow/classdy/dormscorepage?unitId=${eccInfo.unitId!}&buildingId=${eccInfo.placeId!}&view="+_view;
		var	dormRemindUrl =  "${request.contextPath}/eccShow/classdy/dormremindpage?unitId=${eccInfo.unitId!}&buildingId=${eccInfo.placeId!}&view="+_view;
		var	dormClockGradeUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/clockGrade?name="+_deviceNumber+"&view="+_view;
		$("#dormInfoDiv").load(dormInfoUrl);
		$("#dormScoreDiv").load(dormScoreUrl);
		$("#dormRemindDiv").load(dormRemindUrl);
		$("#dormClockGradeDiv").load(dormClockGradeUrl);
		$("#clockRecordDiv").load(clockRecordUrl);
	<#else>
		$("#clockRecordDiv").load(clockRecordUrl);
	</#if>
	$("#footer").load(footUrl);

})

function bulletinShowDetail(id){
	var url =  '${request.contextPath}/eccShow/eclasscard/showIndex/bulletinDetail?id='+id+"&view="+_view;
	$("#showDetailLayer").load(url,function() {
		  showDetailLayer();
		});
}

<#if eccInfo.type=="41"||eccInfo.type=="42"||eccInfo.type=="30"||eccInfo.type=="50">
//打卡触发
function eccClockIn(cardNumber){
	if(!cardNumber||cardNumber==''||cardNumber=='0'){
		showMsgTip("无效卡")
		return;
	}
    $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        data:{"cardNumber":cardNumber,"deviceNumber":_deviceNumber,"clockType":0},
        type:'post',
        success:function(data){
            
            var result = JSON.parse(data);
            if(result.haveStu){
            	if(result.status==1){
            		showStuSuccess();
            	}
            	showStuClockMsg(result);
            	showVoiceTip(result);
            }else{
            	showMsgTip(result.msg);
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
function showVoiceTip(result){
	if (result.type == '41' || result.type == '42') {
		var msg = "";
		if(result.status==1){
			msg = "签到成功"+result.msg;
		}else if(result.status==2){
			msg = "签到失败，"+result.msg;
		}
		if(result.token && result.token!="" && msg !=""){
			clockAudioPlay(result.type,result.token,msg);
		}
	}
}
function showStuSuccess(){
	var clockUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/clockIn?name="+_deviceNumber+"&view="+_view;
	$("#clockRecordDiv").load(clockUrl);
}
</#if>
function showDetailLayer(){
		var index = layer.open({
			type: 1,
			title: false,
			shade: .5,
			shadeClose: true,
			closeBtn: 0,
			area: '1000px',
			content: $('.layer-post')
		})
		layer.style(index, {
			overflow: 'auto'
		})
		
		setTimeout(function(){  
	        layer.close(index);  
	    },1000*60);
}
</script>
<script type="text/javascript" src="${request.contextPath}/static/eclasscard/common/show/js/sockjs.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/js/eccPublic.js"></script>
</body>
</html>