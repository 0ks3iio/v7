<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	    <title>${studentName!}</title>
	    <link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
	    <link href="${request.contextPath}/static/eclasscard/mobileh5/leaveword/css/style.css" rel="stylesheet"/>
	</head>
	<body>
	<#--form需要用div隐藏 否则在submit时  追加的input元素会短暂的显示在页面上-->
	<div style="display:none;">
		<form id="_macro_form_id" method="post">
		</form>
	    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
	</div>

		<nav class="mui-bar mui-bar-tab dialogue-write bt-grey">
		    <div class="mui-row">
				<div class="mui-input-row">
				    <input type="text" class="mui-input-clear no-margin js-write" maxlength="1000" placeholder="输入孩子留言">
				</div>
				<div class="enter-message">
					<button type="button" class="btn btn-deepgreen js-enter">发送</button>
				</div>
			</div>
		</nav>

		<div class="mui-content full-content">
			<div class="talking bt-grey">
			</div>
			
		</div>
<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
<script src="${request.contextPath}/static/mui/js/weike.js"></script>
<script src="${request.contextPath}/static/mui/js/myWeike.js" async="async" defer="defer"></script>
<script src="${request.contextPath}/static/mui/js/common.js" async="async" defer="defer"></script>
<script type="text/javascript">
var studentPicUrl = "${request.contextPath}${studentPic!}";
var _pageIndex = 1;
var lastTime = null;
var maxPage = 1;
var scrollHeightAll = 0;
var familyPicUrl = "${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${showReceiverName!}";
$(function () {
	mui.init();
	findLeaveWordList(_pageIndex);
	//提交message
	$('.js-enter').on('click',submitMessage);
<#if closepage>	
	if (weikeJsBridge) {
		weikeJsBridge.windowClose(".mui-action-back");//绑定关闭事件
	}
<#else>
	$(".mui-action-back").click(function(){
		load("${request.contextPath}/mobile/open/eclasscard/leavae/word/index?familyId=${senderId!}");
	});
</#if>
});

//微课返回方法
function wkGoBack(){
	$(".mui-action-back").click();
}

var isSubmit=false;
function submitMessage(){
	var message = $('.js-write').val().trim();
	if(isSubmit){
   		return;
	}
	isSubmit = true;
	if (message != ''){
		$.ajax({
			url:'${request.contextPath}/mobile/open/eclasscard/leavae/word/send',
			data: {'senderId':"${senderId!}",'content':message,'receiverId':"${receiverId!}"},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
 				var resultVal = jsonO;
 				showMessage(message,resultVal);
	 			isSubmit=false;
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}else{
		isSubmit=false;
	}
}
	
function showMessage(message,time){
	var str = '<div class="dialogue right-dialogue mui-row anomaly">\
					<div class="friend-pic no-padding fr">\
						<img class="full" src="'+familyPicUrl+'"/>\
					</div>\
					<div class="friend-dialogue fr">\
						<div class="dialogue-content">'+ message +'</div>\
					</div>\
					<div class="dialogue-time">'+ time +'</div>\
				</div>';
			
	$('.talking').append(str);	
	$('.mui-content').scrollTop($('.mui-content')[0].scrollHeight);
	$('.js-write').val('');
}

//时间
function getDate(){
	var date = new Date();
	var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
	var year = date.getFullYear();
	var day = date.getDate();
	return year +'-'+ month + '-' + day + ' ' + hours + ":" + minutes
}

function findLeaveWordList(_pageIndex,scrollHeight,lastTime){
	$.ajax({
		url:'${request.contextPath}/mobile/open/eclasscard/leavae/word/history',
		data: {'senderId':"${senderId!}",'receiverId':"${receiverId!}",'_pageIndex':_pageIndex,'_pageSize':20,'lastTime':lastTime},
		type:'post',
		success:function(data) {
			var result = JSON.parse(data);
			showmsgList(result,_pageIndex,scrollHeight);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
function showmsgList(result,pageIndex,scrollHeight){
	if(result!=null && result.length>0){
		var html = '';
		for(var i=result.length-1;i>=0;i--){
			var word =result[i];
			if(word.sender){
				html+='<div class="dialogue right-dialogue mui-row anomaly">\
					<div class="friend-pic no-padding fr">\
						<img class="full" src="'+familyPicUrl+'"/>\
					</div>\
					<div class="friend-dialogue fr">\
						<div class="dialogue-content">'+ word.content +'</div>\
					</div>\
					<div class="dialogue-time">'+ word.timeStr +'</div>\
				</div>';
			}else{
				html+='<div class="dialogue left-dialogue mui-row">\
					<div class="friend-pic fl no-padding">\
						<img class="full" src="'+studentPicUrl+'"/>\
					</div>\
					<div class="friend-dialogue fl">\
						<div class="dialogue-content">'+ word.content +'</div>\
					</div>\
					<div class="dialogue-time">'+ word.timeStr +'</div>\
				</div>';
			}
			if(pageIndex==1 && i==0){
				lastTime = word.creationTime;
				maxPage = word.maxPage;
			}
		}
		if(pageIndex==1){
			$('.mui-content .talking').html(html);
			$('.mui-content').scrollTop($('.mui-content')[0].scrollHeight);
		}else{
			$('.mui-content .talking').prepend(html);
			var sh = $(".mui-content")[0].scrollHeight;
			$('.mui-content').scrollTop(sh-scrollHeight);
		}
	}else{
		$('.mui-content .talking').html('');
	}
}
$(".mui-content").scroll(function(){
	var scrollTop = $(".mui-content").scrollTop();
	var viewportHeight = $(".mui-content").height();
	var scrollHeight = $(".mui-content")[0].scrollHeight;
	//console.log(scrollTop+'=='+viewportHeight+'=='+scrollHeight+'=='+maxPage);
	if(!(scrollTop+viewportHeight==scrollHeight) && scrollTop==0 && _pageIndex<maxPage){
		_pageIndex++;
		scrollHeightAll=scrollHeight;
		findLeaveWordList(_pageIndex,scrollHeightAll);
	}
});
</script>
	</body>
</html>