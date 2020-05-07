<div class="logo">
<#if eccInfo.type == '10'>
<span>${eccInfo.className!}</span>
<#else>
<span>${eccInfo.placeName!}</span>
</#if>
</div>
<div class="date">
	<span class="time"></span>
	<div class="right">
		<span class="day"></span>
		<span class="week"></span>
	</div>
</div>

<script>
	var timestampnow = '';
	$(document).ready(function(){
	getDate();
	setInterval(getDate, 1000);
	
	<#if showBulletin>
		var	topbulletinUrl =  "${request.contextPath}/eccShow/eclasscard/standard/showindex/topbulletin?cardId="+_cardId;
		$("#topbulletinDiv").load(topbulletinUrl);
	</#if>
	});
	function getDate(){
		if(timestampnow != ''){
			var date = new Date(timestampnow);
			if(date.getMinutes() == 0 && date.getSeconds() == 0){
				getServerTimeStamp();
			}else{
				showPageDate(date);
			}
			timestampnow+=1000;
		}else{
			getServerTimeStamp();
		}
	}
	
	function getServerTimeStamp(){
		$.ajax({
	        url:"${request.contextPath}/eccShow/eclasscard/standard/get/system/nowtime",
	        data:{},
	        dataType:'json',
	        async: true,
	        type:'POST',
	        success: function(data) {
	        	timestampnow = data.time;
		        var date = new Date(timestampnow);
				showPageDate(date);
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown) {}
	    })
	}
	
	function showPageDate(date){
		var week = {
			0 : '日',
			1 : '一',
			2 : '二',
			3 : '三',
			4 : '四',
			5 : '五',
			6 : '六'
		};
		var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var cur_week = '星期' + week[date.getDay()];

		$('.date .time').text(hours + ":" + minutes);
		$('.date .day').text(month + '月' + day +'日');
		$('.date .week').text(cur_week);
	}
</script>