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
		_cardId = "${cardId!}";
		_contextPath = "${request.contextPath}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/css/style.css">
</head>
<body class="index">
	<header id="header" class="header">
		
	</header>
	<#include "/eclasscard/show/showMsgtip.ftl">
	<#include "/eclasscard/show/clockStateTip.ftl">
	<div class="main-container">
		<div class="grid">
			<#if eccInfo.type=="10">
				<div class="grid-cell grid-1of3">
					<div class="box" id="classInfoDiv">
					</div>
					<div id="moralEducationDiv">
					</div>
				</div>
				<div class="grid-cell grid-1of3">
					<div class="box" id="albumDiv">
					</div>
					<div class="box box-orange" id="classDescDiv">
					</div>
				</div>
				<div class="grid-cell grid-1of3">
					<div class="box box-green" id="bulletinDiv">
					</div>
				</div>
			<#elseif eccInfo.type=="20">
				<div class="grid-cell grid-1of3">
					<div id="tclassInfoDiv" class="box">
					</div>
					<div id="bulletinDiv" class="box box-green">
					</div>
				</div>
				<div  class="grid-cell grid-2of3">
					<div class="box box-lightblue">
						<div class="box-header">
							<h4 class="box-title">校园风采</h4>
						</div>
						<div id="albumDiv" class="box-body no-padding">
						</div>
					</div>
				</div>
			<#elseif eccInfo.type=="30">
				<div class="grid-cell grid-1of3">
					<div id="dormInfoDiv" class="box">
					</div>
					<div id="bulletinDiv" class="box box-green">
					</div>
				</div>
				<div class="grid-cell grid-1of3">
					<div id="dormScoreDiv">
					</div>
					<div id="dormRemindDiv">
					</div>
				</div>
				<div class="grid-cell grid-1of3">
					<div class="box box-greenyellow">
						<div class="box-header">
							<h4 class="box-title">打卡情况</h4>
						</div>
						<div class="box-body no-padding">
							<ul class="checkin-result-list">
							<div id="dormClockGradeDiv">
							</div>
							<div id="clockRecordDiv">
							</div>
							</ul>
							<div class="no-data" id="recordNodataDiv" style="display:none">
								<div class="no-data-content">
									<img src="${request.contextPath}/static/eclasscard/show/images/img-sound-wave.png" alt="">
									<p>当前无打卡记录</p>
								</div>
							</div>
						</div>
					</div>
					<div id="albumDiv" class="box">
					</div>
				</div>
			<#else>
				<div class="grid-cell grid-2of3">
					<div class="box box-lightblue">
						<div class="box-header">
							<h4 class="box-title">校园风采</h4>
						</div>
						<div id="albumDiv" class="box-body no-padding">
						</div>
					</div>
				</div>
				<div class="grid-cell grid-1of3">
					<div  class="box box-greenyellow">
						<div class="box-header">
							<h4 class="box-title">打卡情况</h4>
						</div>
						<div class="box-body no-padding">
							<ul class="checkin-result-list">
								<div id="clockRecordDiv">
								</div>
							</ul>
							<div class="no-data" id="recordNodataDiv" style="display:none">
								<div class="no-data-content">
									<img src="${request.contextPath}/static/eclasscard/show/images/img-sound-wave.png" alt="">
									<p>当前无打卡记录</p>
								</div>
							</div>
						</div>
					</div>
					<div id="bulletinDiv" class="box box-green">
					</div>
				</div>
			</#if>
		</div>
	</div>
	<footer class="footer">
		<ul class="menu-list">
		<#if eccInfo.type=="10" || eccInfo.type=="20">
			<li>
				<a href="javascript:void(0);" onclick="showClassClock()">
					<span class="icon icon-sign"></span>
					<h5 class="menu-name">上课签到</h5>
				</a>
			</li>
		</#if>
		<#if eccInfo.type=="10">
			<li>
				<a href="javascript:void(0);" onclick="showClassSpace()">
					<span class="icon icon-classSpace"></span>
					<h5 class="menu-name">班级空间</h5>
				</a>
			</li>
		</#if>
			<li>
				<a href="javascript:void(0);" onclick="showStudentSpace()">
					<span class="icon icon-stuSpace"></span>
					<h5 class="menu-name">学生空间</h5>
				</a>
			</li>
		</ul>
	</footer>

	<!-- S 通知公告弹出框 -->
	<div id="showDetailLayer" class="layer-post">
		
	</div><!-- E 通知公告弹出框 -->

<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/layer/layer.js"></script>
<script>
var isSubmit =false;
var scheduleId = '';
$(document).ready(function(){
	var headUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/showHeader?type=${eccInfo.type!}&back=1";
	$("#header").load(headUrl);
	var	bulletinUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/bulletin?cardId="+_cardId+"&view="+_view;
	var	albumUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/album?cardId="+_cardId+"&view="+_view;
	$("#bulletinDiv").load(bulletinUrl);
	$("#albumDiv").load(albumUrl);
	var	clockRecordUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/clockIn?cardId="+_cardId+"&view="+_view;
	<#if eccInfo.type=="10">
		var	classInfoUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/class/info?cardId="+_cardId+"&view="+_view;
		var	classDescUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/description?cardId="+_cardId+"&view="+_view;
		var	moralEducationUrl = "${request.contextPath}/eccShow/classdy/classpage?unitId=${eccInfo.unitId!}&classId=${eccInfo.classId!}&view="+_view;
		$("#classInfoDiv").load(classInfoUrl);
		$("#classDescDiv").load(classDescUrl);
		$("#moralEducationDiv").load(moralEducationUrl);
	<#elseif eccInfo.type=="20">
		var	tclassInfoUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/tclass/info?cardId="+_cardId+"&view="+_view;
		$("#tclassInfoDiv").load(tclassInfoUrl);
	<#elseif eccInfo.type=="30">
		var	dormInfoUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/dorm/info?cardId="+_cardId+"&view="+_view;
		var	dormScoreUrl =  "${request.contextPath}/eccShow/classdy/dormscorepage?unitId=${eccInfo.unitId!}&buildingId=${eccInfo.placeId!}&view="+_view;
		var	dormRemindUrl =  "${request.contextPath}/eccShow/classdy/dormremindpage?unitId=${eccInfo.unitId!}&buildingId=${eccInfo.placeId!}&view="+_view;
		var	dormClockGradeUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/clockGrade?cardId="+_cardId+"&view="+_view;
		$("#dormInfoDiv").load(dormInfoUrl);
		$("#dormScoreDiv").load(dormScoreUrl);
		$("#dormRemindDiv").load(dormRemindUrl);
		$("#dormClockGradeDiv").load(dormClockGradeUrl);
		$("#clockRecordDiv").load(clockRecordUrl);
	<#else>
		$("#clockRecordDiv").load(clockRecordUrl);
	</#if>

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
        data:{"cardNumber":cardNumber,"cardId":_cardId,"clockType":0},
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
	var clockUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/clockIn?cardId="+_cardId+"&view="+_view;
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
//wesocket调用此方法，修改方法名，后台也要修改
function showClassClock(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	 $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/classClockIn/chechClock',
        data:{"cardId":_cardId},
        type:'post',
        success:function(data){
            var result = JSON.parse(data);
            if(result.success){
            	if(scheduleId!=result.businessValue){
	            	scheduleId = result.businessValue;
					location.href = "${request.contextPath}/eccShow/eclasscard/classClockIn/index?cardId="+_cardId+"&id="+scheduleId+"&view="+_view;
            	}
            }else{
            	showMsgTip('当前未到打卡时间，请于上课前10分钟前来打卡');
            }
            isSubmit = false;
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        	isSubmit = false;
        }
    });

}

function showStudentSpace(){
	location.href = "${request.contextPath}/eccShow/eclasscard/studentSpace/login?cardId="+_cardId+"&view="+_view;
}
function showClassSpace(){
	location.href = "${request.contextPath}/eccShow/eclasscard/classSpace/index?cardId="+_cardId+"&view="+_view;
}

</script>
<script type="text/javascript" src="${request.contextPath}/static/eclasscard/common/show/js/sockjs.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/js/eccPublic.js"></script>
</body>
</html>