<div class="dialog-box grid scroll-container">
<#if needFamilys?exists&&needFamilys?size gt 0>
	<input type="hidden" id="receiverIdVal" name="receiverId" value="${showId!}" >
	<input type="hidden" id="receiver-pic-src" name="receiverId" value="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${showReceiverName!}" >
	<div class="friend-list grid-cell height-1of1 no-padding">
		<div class="part-name">我的留言</div>
		<ul class="scrollbar-made">
		<#list needFamilys as family>
			<li <#if showId == family.id>class="active"</#if> data-receiverid="${family.id!}">
				<#if family.unReadNum gt 0><div class="message-num">${family.unReadNum!}</div></#if>
				<div class="grid">
					<div class="friend-pic grid-cell no-padding">
						<img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${family.username!}"/>
					</div>
					<div class="friend-content grid-cell">
						<p class="friend-name">${family.name!}</p>
						<p class="last-message">${family.lastWord!}</p>
					</div>
				</div>
			</li>
		</#list>
		</ul>
	</div>
	
	<div class="dialog-content grid-cell no-padding">
		<div class="dialog-name">
		</div>
		<div class="dialog-show scrollbar-made">
			<div class="talking">
			</div>
		</div>
		<div class="dialogue-write">
			<textarea class="write-down scrollbar-made" name="" rows="" cols="" maxlength="1000"></textarea>
			<!-- div class="smile-face">
				<img src="images/smile-face.png" alt="" />
			</div -->
			<div class="enter-message text-right">
				<button class="btn btn-purple js-enter" onclick="submitMessage()">发送</button>
			</div>
		</div>
	</div>
<#else>
	<div class="nothing">
		<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
	</div>
</#if>
</div>

<script>
var _pageIndex = 1;
var lastTime = null;
var maxPage = 1;
var scrollHeightAll = 0;
$(document).ready(function(){
	$('.scroll-container').each(function(){
		$(this).css({
			overflow: 'auto',
			height: $(window).height() - $(this).offset().top - 160
		});
	});
	$('.nothing').height($('.scroll-container').height() - 100 );
	//对话框
	$('.friend-list').on('click','li',function(){
		_pageIndex = 1;
		scrollHeightAll = 0;
		$('.dialog-show .talking').html('');
		$(this).addClass('active').siblings('li').removeClass('active');
		$(this).find('.message-num').hide();
		$("#receiverIdVal").val($(this).data("receiverid"));
		$("#receiver-pic-src").val($(this).find('.friend-pic img').attr('src'));
		findLeaveWordList(_pageIndex);
		$('.dialog-name').text($(this).find('.friend-name').text());
		$('.write-down').val('');
	});
	$('.friend-list li.active').click();
})

var isSubmit=false;
function submitMessage(){
	var message = $('.write-down').val().trim();
	var receiverId = $('#receiverIdVal').val();
	if(isSubmit){
   		return;
	}
	isSubmit = true;
	if (message != ''){
		$.ajax({
			url:'${request.contextPath}/eccShow/eclasscard/standard/leavae/word/send',
			data: {'senderId':"${senderId!}",'content':message,'cardId':_cardId,'receiverId':receiverId},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
	 			if(jsonO.success){
	 				var resultVal = jsonO.businessValue;
	 				showMessage(message,resultVal);
	 			}
	 			isSubmit=false;
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}else{
		isSubmit=false;
	}
}

function showMessage(message,time){
	var senderPic = $('#student-img-src').find('img').attr('src');
	var str = '<div class="dialogue right-dialogue grid">\
				<div class="friend-pic grid-cell no-padding pull-right">\
					<img src="'+senderPic+'"/>\
				</div>\
				<div class="friend-dialogue grid-cell pull-right">\
					<div class="dialogue-content">'+ message +'</div>\
				</div>\
				<div class="dialogue-time">'+ time +'</div>\
			</div>';
			
	$('.dialog-show .talking').append(str);	
	$('.friend-list li.active').find('.last-message').text(message);
	$('.dialog-show').scrollTop($('.dialog-show')[0].scrollHeight);
	$('.write-down').val('');
}

function showMessageFamily(message,time){
	var receiver = $('#receiver-pic-src').val();
	var str ='<div class="dialogue left-dialogue grid">\
					<div class="friend-pic grid-cell no-padding">\
						<img src="'+receiver+'"/>\
					</div>\
					<div class="friend-dialogue grid-cell">\
						<div class="dialogue-content">'+ message +'</div>\
					</div>\
					<div class="dialogue-time">'+ time +'</div>\
				</div>';
			
	$('.dialog-show .talking').append(str);	
	$('.friend-list li.active').find('.last-message').text(message);
	$('.dialog-show').scrollTop($('.dialog-show')[0].scrollHeight);
	$('.write-down').val('');
}

function findLeaveWordList(_pageIndex,scrollHeight,lastTime){
	var receiverId = $('#receiverIdVal').val();
	$.ajax({
		url:'${request.contextPath}/eccShow/eclasscard/standard/leavae/word/history',
		data: {'senderId':"${senderId!}",'cardId':_cardId,'receiverId':receiverId,'_pageIndex':_pageIndex,'_pageSize':20,'lastTime':lastTime},
		type:'post',
		success:function(data) {
			var result = JSON.parse(data);
			showmsgList(result,_pageIndex,scrollHeight);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
function showmsgList(result,pageIndex,scrollHeight){
	var senderPic = $('#student-img-src').find('img').attr('src');
	var receiver = $('#receiver-pic-src').val();
	if(result!=null && result.length>0){
		var html = '';
		for(var i=result.length-1;i>=0;i--){
			var word =result[i];
			if(word.sender){
				html+='<div class="dialogue right-dialogue grid anomaly">\
					<div class="friend-pic grid-cell no-padding pull-right">\
						<img src="'+senderPic+'"/>\
					</div>\
					<div class="friend-dialogue grid-cell pull-right">\
						<div class="dialogue-content">'+ word.content +'</div>\
					</div>\
					<div class="dialogue-time">'+ word.timeStr +'</div>\
				</div>';
			}else{
				html+='<div class="dialogue left-dialogue grid">\
					<div class="friend-pic grid-cell no-padding">\
						<img src="'+receiver+'"/>\
					</div>\
					<div class="friend-dialogue grid-cell">\
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
			$('.dialog-show .talking').html(html);
			$('.dialog-show').scrollTop($('.dialog-show')[0].scrollHeight);
		}else{
			$('.dialog-show .talking').prepend(html);
			var sh = $(".dialog-show")[0].scrollHeight;
			$('.dialog-show').scrollTop(sh-scrollHeight);
		}
	}else{
		$('.dialog-show .talking').html('');
	}
}
$(".dialog-show").scroll(function(){
	var scrollTop = $(".dialog-show").scrollTop();
	var viewportHeight = $(".dialog-show").height();
	var scrollHeight = $(".dialog-show")[0].scrollHeight;
	if(!(scrollTop+viewportHeight==scrollHeight) && scrollTop==0 && _pageIndex<maxPage){
		_pageIndex++;
		scrollHeightAll=scrollHeight;
		findLeaveWordList(_pageIndex,scrollHeightAll);
	}
});

function showIndexLeaveWord(stuId,familyId,message,time){
	var senderId = "${senderId!}";
	var receiverId = $('#receiverIdVal').val();
	if(senderId==stuId && receiverId==familyId){
		showMessageFamily(message,time);
	}
}
</script>