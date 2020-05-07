<!-- S 消息 -->
<a class="message-btn" href="javascript:void(0);"><span id="show-message-bell" class="icon icon-bell shake-bell"></span><span id="show-message-num" class="message-num"></span></a>
<div class="message-mask"></div>
<div class="message-box">
	<div class="message-header"><span>消息</span></div>
	<div class="message-body">
		<ul id="show-message-class" class="message-list">
			<li>
				<a href="javascript:void(0);">
					<span class="message-avatar message-avatar-blue">曹</span>
					<div class="message-content">曹斌斌同学，你收到一张小纸条~</div>
					<p class="message-time">2018-02-18</p>
				</a>
			</li>
		</ul>
	</div>
</div><!-- E 消息 -->
<script>
function showIndexLeaveWord(){
	findLeaveWordClass();
}
$(document).ready(function(){
	findLeaveWordClass();
	var btn = $('.message-btn'),
		mask = $('.message-mask'),
		box = $('.message-box');

	btn.on('click', function(e){
		e.preventDefault();
		mask.show();
		box.addClass('show');
		box.find('.message-body').css({
			height: $(window).innerHeight() - 60
		});
	});

	mask.on('click', function(e){
		$(this).hide();
		box.removeClass('show');
	});
})
function findLeaveWordClass(){
	$.ajax({
		url:'${request.contextPath}/eccShow/eclasscard/standard/leavae/word/class',
		data: {'cardId':_cardId},
		type:'post',
		success:function(data) {
			var result = JSON.parse(data);
			showmsgClass(result);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function showmsgClass(result){
	if(result!=null && result.length>0){
		$('#show-message-num').text(result.length);
		var html = '';
		for(var i=0;i<result.length;i++){
			var word =result[i];
			var str = word.remindStr;
			var showClass = 'message-avatar-blue';
			if(i%4 == 1){
				showClass = 'message-avatar-yellow';
			}else if(i%4 == 2){
				showClass = 'message-avatar-green';
			}else if(i%4 == 3){
				showClass = 'message-avatar-purple';
			}
			if(str && str!=''){
				str = str.substring(0,1);
			}else{
				str = ''
			}
			html+='<li>\
				<a href="javascript:void(0);" onclick="showStudentSpace(\''+word.receiverId+'\')">\
					<span class="message-avatar '+showClass+'">'+str+'</span>\
					<div class="message-content">'+word.remindStr+'</div>\
					<p class="message-time">'+word.timeStr+'</p>\
				</a>\
			</li>';
		}
		$('#show-message-class').html(html);
		$('#show-message-bell').addClass('shake-bell');
	}else{
		$('#show-message-class').html('');
		$('#show-message-num').text('0');
		$('#show-message-bell').removeClass('shake-bell');
	}
}
</script>