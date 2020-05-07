<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>行政班首页</title>
	<meta name="viewport">
	<script src="${request.contextPath}/static/eclasscard/common/show/js/flexible.js"></script>
	<script>
		_view = "${view!}";
		_cardId = "${cardId!}";
		_webSocketUrl = "${webSocketUrl!}";
		_sockJSUrl = "${sockJSUrl!}";
		_contextPath = "${request.contextPath}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/slick/slick.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/eclasscard/standard/verticalshow/font-awesome/css/font-awesome.css"/>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/standard/verticalshow/css/style.css?v=201912091">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/mobiscroll/mobiscroll.full.min.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/gallery/css/blueimp-gallery.min.css">
</head>
<body class="index">
		<input type="hidden" id="fullscreenobjId" value="" >
		<input type="hidden" id="clock-tab-type" value=0 >
		<header id="header" class="header">
		</header>
		<div class="lock-full">
			<span>屏幕已锁定</span>
		</div>
		<div onclick="closeFullScreen()" class="close-full">
			<i class="fa fa-times" aria-hidden="true"></i>
		</div>
		<div id="full-screen-body">
		</div><!--full-screen-body-->
		<#include "/eclasscard/standard/verticalshow/showMsgtip.ftl">
		<#include "/eclasscard/standard/verticalshow/clockStateTip.ftl">
		<div id="mainContainerDiv" class="main-container">
			
		</div>
		<footer id="footer" class="footer">
		</footer>

	<!-- S 消息 -->
	<div id="indexMsg" class="message">
	</div><!-- E 消息 -->

	<!-- S 通知公告弹出框 -->
	<div id="showDetailLayer" class="layer layer-post">
	</div><!-- E 通知公告弹出框 -->
	<div id="fullScreenLayer" class="layer">
	</div>

<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/layer/layer.js"></script>
<script src="${request.contextPath}/static/eclasscard/common/show/js/common.use.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/echarts/echarts.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/iScroll/iscroll.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/mobiscroll/mobiscroll.full.min.js"></script>
<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/plugins/gallery/js/blueimp-gallery.min.js"></script>
<script>
var load_time = null;//定时跳转标记
var interval_init_course = null;//定时居中课程标记
var isLoadpage = false;//是否需要判断跳入考勤页
var isFullScreenNow = false;//当前是否已经全屏
var isLockFullScreen = false;//当前是否已经全屏
var fullOpenIndex = new Array();//全屏layer的index
var classsapcetimer = null;
var timers=null;
var changeSpan = null; //签到提示
$(document).ready(function(){
	var headUrl = "${request.contextPath}/eccShow/eclasscard/standard/showIndex/showHeader?showBulletin=true&cardId="+_cardId+"&view="+_view;
	var footUrl = "${request.contextPath}/eccShow/eclasscard/standard/showIndex/showFooter?cardId="+_cardId+"&view="+_view;
	$("#header").load(headUrl);
	$("#footer").load(footUrl);
	showIndex();
	firstCheckFullScreen();
	closeFaceView();
})

function showIndex(){
	if(null != interval_init_course){  
       clearInterval(interval_init_course);  
   	}
	isLoadpage = true;
	timeoutJump();
	$('.message-mask').hide();
	$('.message-box').removeClass('show');
	$("#indexMsg").show();
	var homeUrl = "${request.contextPath}/eccShow/eclasscard/standard/showHomePage?cardId="+_cardId+"&view="+_view;
	$("#mainContainerDiv").load(homeUrl);
}
//进入上课考勤详情页面
function skipToClassClock(id){
	if(null != interval_init_course){  
       clearInterval(interval_init_course);  
   	}
	if(!isLockFullScreen){
   		closeFullScreen();
   	}
   	$("#indexMsg").hide();
	var classClockUrl = "${request.contextPath}/eccShow/eclasscard/standard/classClockIn/index?cardId="+_cardId+"&id="+id+"&view="+_view;
	$("#mainContainerDiv").load(classClockUrl);
}
//进入上下学详情页面
function skipInOutAttence(periodId){
   	if(!isLockFullScreen){
   		closeFullScreen();
   	}
   	$("#indexMsg").hide();
	var inOutUrl = "${request.contextPath}/eccShow/eclasscard/standard/inout/students?cardId="+_cardId+"&periodId="+periodId+"&view="+_view;
	$("#mainContainerDiv").load(inOutUrl);
}

function timeoutJump() {
	if(null != load_time){  
       clearInterval(load_time);  
   	}
	//打开页面60秒不操作就跳转  
   	load_time = setTimeout(function(){
   		<#if eccInfo.type=="10" || eccInfo.type=="20">
   		$.ajax({
    		url:'${request.contextPath}/eccShow/eclasscard/classClockIn/chechClock',
    		data:{"cardId":_cardId},
    		type:'post',
    		success:function(data){
        		var result = JSON.parse(data);
        		isLoadpage = false;
        		if(result.success){
        			<#if eccInfo.type=="10">
        				checkInoutSkip(result.businessValue);
        			<#else>
	        			if (!$("#footTabClsClock").hasClass('active')) {
	        				skipToClassClock(result.businessValue);
							if (isActivate == "true") {
								closeFaceView();
							}
	        			}
        			</#if>
        		}else{
        			<#if eccInfo.type=="10">
        				checkInoutSkip("");
        			<#else>
	        			if (!$("#footTabHome").hasClass('active')) {
	        				showIndex();
							if (isActivate == "true") {
								closeFaceView();
							}
	        			}
        			</#if>
        		}
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    		}
		});
		<#else>
		if ($("#footTabHome").attr('class') != 'active') {
			showIndex();
			if (isActivate == "true") {
				closeFaceView();
			}
		}
		</#if>
		setTimeout(function(){
			if(!isFullScreenNow)
			checkFullScreenObj();
		},2000); 
    },1000*60); 
}

document.addEventListener('touchstart', function(event) {
     // 如果这个元素的位置内只有一个手指的话
    if ((isLoadpage||isFullScreenNow) && event.targetTouches.length == 1) {
　　　	 //event.preventDefault();
         timeoutJump();
    }
}, false); 

//判断是否跳到上下学考勤页，classAttId为空不用判定与上课考勤先后关系
function checkInoutSkip(classAttId){
	$.ajax({
		url:'${request.contextPath}/eccShow/eclasscard/standard/check/inout/attence',
		data:{"cardId":_cardId},
		type:'post',
		success:function(data){
    		var result = JSON.parse(data);
    		isLoadpage = false;
    		if(result.success){
    			if(classAttId!=''){
    				checkLastAtt(classAttId,result.businessValue);
    			}else{
    				if (!$("#footTabInOutAttence").hasClass('active')) {
	    				skipInOutAttence(result.businessValue);
						if (isActivate == "true") {
							closeFaceView();
						}
	    			}
    			}
    		}else{
    			if(classAttId!=''){
	    			if (!$("#footTabClsClock").hasClass('active')) {
	    				skipToClassClock(classAttId);
						if (isActivate == "true") {
							closeFaceView();
						}
	    			}
    			}else{
    				if (!$("#footTabHome").hasClass('active')) {
        				showIndex();
						if (isActivate == "true") {
							closeFaceView();
						}
        			}
    			}
    		}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}
//检测上课与上下学考勤先后关系
function checkLastAtt(classAttId,periodId){
	$.ajax({
		url:'${request.contextPath}/eccShow/eclasscard/standard/check/last/attence',
		data:{"cardId":_cardId,"classAttId":classAttId,"periodId":periodId},
		type:'post',
		success:function(data){
    		var result = JSON.parse(data);
    		isLoadpage = false;
    		if(result.success){
    			if(result.businessValue =='1'){
    				if ($("#footTabClsClock").hasClass('active')) {
	    				skipToClassClock(classAttId);
						if (isActivate == "true") {
							closeFaceView();
						}
	    			}
    			}else{
    				if ($("#footTabInOutAttence").hasClass('active')) {
	    				skipInOutAttence(periodId);
						if (isActivate == "true") {
							closeFaceView();
						}
	    			}
    			}
    		}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function closeFullScreen(){
	$('.close-full').hide();
	for(j = 0; j < fullOpenIndex.length; j++) {
		layer.close(fullOpenIndex[j]);
	}
	$("#fullscreenobjId").val('');
	clearInterval(timers); 
	fullOpenIndex = new Array();
	isFullScreenNow = false;
	isLockFullScreen = false;
	setTimeout(function(){
		var indexVideo = document.getElementById("showVideo");
		if(indexVideo){
			indexVideo.play();
		}
	},200);
}
function showExamDoorSticker(id){
	if(id && id != ""){
		var examDoorUrl = "${request.contextPath}/eccShow/eclasscard/standard/exam/door/sticker?cardId="+_cardId+"&id="+id+"&view="+_view;
		$("#fullScreenLayer").load(examDoorUrl,function() {
			$("#fullScreenLayer").removeClass('layer-lock layer-slider');
			showFullObjectLayer();
		});
	}
}

function showFullScreenBulletin(id){
	if(id && id != ""){
		var fullBulletinUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/fullbulletin?cardId="+_cardId+"&id="+id+"&view="+_view;
		$("#fullScreenLayer").load(fullBulletinUrl,function() {
			$("#fullScreenLayer").removeClass('layer-lock layer-slider');
			showFullObjectLayer();
		});
	}
}

function showFullScreenMedia(id,lockScreen){
	if(id && id != ""){
		var fullMediaUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/fullalbum?cardId="+_cardId+"&showFolderId="+id+"&view="+_view+"&lockScreen="+lockScreen;
		$("#fullScreenLayer").load(fullMediaUrl,function() {
			$("#fullScreenLayer").addClass('layer-lock layer-slider');
			showFullObjectLayer();
		});
	}
}

function firstCheckFullScreen(){
	setTimeout(function(){
		checkFullScreenObj();
	 },5000); 
}

function checkFullScreenObj(){
	$.ajax({
		url:'${request.contextPath}/eccShow/eclasscard/standard/check/fullscreen/object',
		data:{"cardId":_cardId},
		type:'post',
		success:function(data){
    		var result = JSON.parse(data);
    		if(result.success){
    			var resultVal = result.businessValue;
    			var valArr = resultVal.split('==');
    			if (isFullScreenNow || '1' == valArr[2] || (!$("#footTabClsClock").hasClass('active') && !$("#footTabInOutAttence").hasClass('active'))) {
	    			if('02' == valArr[1]){
	    				showExamDoorSticker(valArr[0]);
	    			}else if('01' == valArr[1]){
	    				showFullScreenBulletin(valArr[0]);
	    			}else{
	    				showFullScreenMedia(valArr[0],valArr[2]);
	    			}
	    			$("#fullscreenobjId").val(valArr[3]);
    			}
    		}else{
    			closeFullScreen();
    		}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function showFullObjectLayer(){
	$('#fullScreenLayer').height($(window).height());
	var index = layer.open({
		type: 1,
		title: false,
		shade: 0,
		closeBtn: 0,
		area: ['100%'],
		content: $('#fullScreenLayer')
	})
	layer.style(index, {
		overflow: 'auto'
	})
	setTimeout(function(){
			var indexVideo = document.getElementById("showVideo");
			if(indexVideo){
				indexVideo.pause();
			}
		},600);
	fullOpenIndex.push(index);
	if (isActivate == "true") {
		closeFaceView();
	}
}
function fullScreenLockCheck(){
	var fullscreenobjId = $("#fullscreenobjId").val();
	if(fullscreenobjId != ""){
		$.ajax({
			url:'${request.contextPath}/eccShow/eclasscard/standard/check/fullscreen/lock',
			data:{"id":fullscreenobjId},
			type:'post',
			success:function(data){
	    		var result = JSON.parse(data);
	    		if(result.success){
	    			var resultVal = result.businessValue;
	    			if(resultVal=='1'){
	    				isLockFullScreen = true;
	    			}else{
	    				isLockFullScreen = false;
	    			}
	    		}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	}else{
		if(!isFullScreenNow){
			checkFullScreenObj();
		}
	}
}
//脸部识别
var isActivate = "false";
<#if isActivate>
	isActivate = "true";
</#if>
var faceTime = null; //定时关摄像头
function openFaceView() {
	if (window.jsInterface && window.jsInterface.hideFaceWindow) {
		jsInterface.showFaceWindow();
	}
	var activeTab = $(".menu-list li[class='active']").attr('id');
	if (activeTab != "footTabStuSpace") {
		faceTimeOut();
	}
}
var faceSubmit = false;
function closeFaceView() {
	faceSubmit = false;
	if (window.jsInterface && window.jsInterface.hideFaceWindow) {
		jsInterface.hideFaceWindow();
	}
}

function faceTimeOut() {
	if(null != faceTime){
       clearInterval(faceTime);
   	}
	//3分钟不刷脸就关闭
   	faceTime = setTimeout(function(){
   		closeFaceView();
    },1000*60*3);
}


function eccFaceMatched(ownerId) {
	if (faceSubmit) {
		return;
	}
	var activeTab = $(".menu-list li[class='active']").attr('id');
	if (activeTab == "footTabHome") {
		faceSubmit = true;
		$.ajax({
			url:'${request.contextPath}/eccShow/eclasscard/clockIn',
			data:{"ownerId":ownerId,"cardId":_cardId, "clockType":1},
			type:'post',
			success:function(data){
				var result = JSON.parse(data);
				if(result.haveStu){
					if(result.status==1){
						showStuSuccess();
						<#if eccInfo.type=="41">
						showPrompt("inSuc");
						<#elseif eccInfo.type=="42">
						showPrompt("outSuc");
						<#elseif eccInfo.type=="30" || eccInfo.type=="50">
						showPrompt("signSuc");
						</#if>
					} else if(result.status==2) {
						<#if eccInfo.type=="42">
						showPrompt("outFail");
						<#elseif eccInfo.type=="30" || eccInfo.type=="50">
						showPrompt("signFail");
						</#if>
					}
					showStuClockMsg(result);
				}else{
					showMsgTip(result.msg);
					<#if eccInfo.type=="42">
					showPrompt("outFail");
					<#elseif eccInfo.type=="30" || eccInfo.type=="50">
					showPrompt("signFail");
					</#if>
				}
				faceSubmit = false;
				faceTimeOut();
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				faceSubmit = false;
				<#if eccInfo.type=="42">
				showPrompt("outFail");
				<#elseif eccInfo.type=="30" || eccInfo.type=="50">
				showPrompt("signFail");
				</#if>
			}
		});
	} else if (activeTab == "footTabClsClock") {
		var classAttenceId = $("#classAttenceId").val();
		faceSubmit = true;
    	$.ajax({
       		url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        	data:{"ownerId":ownerId,"cardId":_cardId,"objectId":classAttenceId,"clockType":1,"type":0},
        	type:'post',
        	success:function(data){
            	var result = JSON.parse(data);
            	if(result.haveStu){
            		showStuClockMsg(result);
            		if(result.status!=2){
            			if(result.ownerType=='2'){
	            			showTeaSuccess(result);
            			}else{
	            			showStuSuccess(result);
            			}
						showPrompt("signSuc");
            		} else {
						showPrompt("signFail");
					}
            	}else{
            		showMsgTip(result.msg);
					showPrompt("signFail");
            	}
            	faceSubmit = false;
            	faceTimeOut();
        	},
        	error : function(XMLHttpRequest, textStatus, errorThrown) {
        		faceSubmit = false;
				showPrompt("signFail");
        	}
    	});
	} else if(activeTab == "footTabInOutAttence"){
		var inoutPeriodId = $("#inoutPeriodId").val();
		faceSubmit = true;
    	$.ajax({
       		url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        	data:{"ownerId":ownerId,"cardId":_cardId,"objectId":inoutPeriodId, "clockType":1,"type":1},
        	type:'post',
        	success:function(data){
            	var result = JSON.parse(data);
            	if(result.haveStu){
            		showStuClockMsg(result);
	            	if(result.status!=2){
	            		showInoutStuSuccess(result);
	            		if(result.status==3){
							showPrompt("signFail");
	            		}else{
							showPrompt("signSuc");
	            		}
	            	} else {
						showPrompt("signFail");
						faceSubmit = false;
						if(result.msg=="超过考勤时间"){
							//回到首页
							setTimeout(function(){
	    						var freshUrl="${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
								location.href = freshUrl;
	    					},2000);
						}
					}
            	}else{
            		showMsgTip(result.msg);
					showPrompt("signFail");
            	}
            	faceSubmit = false;
            	faceTimeOut();
        	},
        	error : function(XMLHttpRequest, textStatus, errorThrown) {
        		faceSubmit = false;
				showPrompt("signFail");
        	}
    	});
		
	}else if (activeTab == "footTabStuSpace") {
		faceSubmit = true;
		$.ajax({
			url:'${request.contextPath}/eccShow/eclasscard/standard/stuLoginUser/page',
			data:{"cardId":_cardId,"studentId":ownerId,"type":"faceType"},
			type:'post',
			success:function(data){
    			var jsonO = JSON.parse(data);
    			if(jsonO.success){
					showPrompt("faceSuc");
    				$('input[type=password]').parent().removeClass('error');
        			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/index?cardId="+_cardId+"&view="+_view;
   		 			$("#mainContainerDiv").load(studentSpaceUrl);
   		 			closeFaceView();
   		 		}else{
        			layer.msg(jsonO.msg);
        			faceSubmit = false;
        			faceTimeOut();
    			}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
    			faceSubmit = false;
  		  		layer.msg("登录失败，请联系系统管理员");
	 		}
		});
	}
}

function eccClockIn(cardNumber){
	if(isFullScreenNow){//全屏模式下刷卡无效
		return;
	}
	if(!cardNumber||cardNumber==''||cardNumber=='0'){
		showMsgTip("无效卡")
		return;
	}
	var type = $("#clock-tab-type").val();
	if(type<1){
		return;
	}
	if (type == 1) {
		homeClockMethed(cardNumber);
	}else if(type == 2){
		classAttenceBy(cardNumber);
	}else if(type == 3){
		$("#cardNumber").text(cardNumber);
        stuScheduleLogin(cardNumber);
    }else if(type == 4){
        $("#cardNumber").text(cardNumber);
    }else if(type == 7){
    	inoutAttenceBy(cardNumber);
    }
}

var prompt = "${prompt!}";
var clockSuc = "${request.contextPath}/static/eclasscard/show/mp3/clock_suc.mp3";
var signSuc = "${request.contextPath}/static/eclasscard/show/mp3/sign_suc.mp3";
var signFail = "${request.contextPath}/static/eclasscard/show/mp3/sign_fail.mp3";
var inSuc = "${request.contextPath}/static/eclasscard/show/mp3/in_suc.mp3";
var outSuc = "${request.contextPath}/static/eclasscard/show/mp3/out_suc.mp3";
var outFail = "${request.contextPath}/static/eclasscard/show/mp3/out_fail.mp3";
var promptAudio = new Audio();
function showPrompt(type) {
	if (prompt == "true") {
		promptAudio.pause();
		if (type == "clockSuc") {
			promptAudio.src = clockSuc;
		} else if (type == "signSuc") {
			promptAudio.src = signSuc;
		} else if (type == "signFail") {
			promptAudio.src = signFail;
		} else if (type == "inSuc") {
			promptAudio.src = inSuc;
		} else if (type == "outSuc") {
			promptAudio.src = outSuc;
		} else if (type == "outFail") {
			promptAudio.src = outFail;
		}
		promptAudio.play();
	}
}
</script>
<script type="text/javascript" src="${request.contextPath}/static/eclasscard/common/show/js/sockjs.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/standard/verticalshow/js/eccPublic.js?version=20180518"></script>
</body>
</html>