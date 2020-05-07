	<div class="layer-slider-wrap">	
		<div class="layer-slider-div">
			<#if attachFolder?exists&&attachments?exists&&attachments?size gt 0>
				<#if attachFolder.type==2>
				<div class="wrap-full">
					<video  id="showFullVideo" autoplay="true" width="100%" height="" src="" <#if mute == 1>muted</#if> >
					</video>
				</div>
				<#else>
					<#list attachments as item>
					<div class="wrap-full" >
						<img src="${request.contextPath}${item.showPicUrl!}" alt="">
					</div>
					</#list>
				</#if>
			<#else>
				<div class="wrap-full">
					<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
				</div>
			</#if>
				
		</div>
	</div>
<script language="javascript">  
<#if lockScreen == '1' >
	isLockFullScreen = true;
<#else>
	isLockFullScreen = false;
</#if>
isFullScreenNow = true;
var lockfulltimestop = null;
var lastlockState = isLockFullScreen;
$('#fullScreenLayer').off('touchstart').on('touchstart', function(event) {
	if(null != lockfulltimestop){  
       clearInterval(lockfulltimestop);  
   	}
 	if(lastlockState != isLockFullScreen){
	   	$('.lock-full').hide();
	   	$('.close-full').hide();
	   	lastlockState = isLockFullScreen;
   	}
	if(isLockFullScreen){
		$('.lock-full').show();
		setTimeout(function(){
			$('.lock-full').hide();
		},6000);
	}else{
		$('.close-full').show();
		setTimeout(function(){
			$('.close-full').hide();
		},6000);
	}
});
<#if attachFolder?exists&&attachments?exists&&attachments?size gt 0>
	<#if attachFolder.type==2>
    var fvList = [
    	<#list attachments as item>
    		'${item.dirPath!}'
    		<#if item_index+1 != attachments?size>,</#if>
    	</#list>  
    	];
    var fvLen = fvList.length; 
    var fcurr = 0; 
    var fvideo = document.getElementById("showFullVideo"); 
	
	$("#showFullVideo").on("timeupdate", function(event){
      	onfTrackedVideoFrame(this.currentTime, this.duration);
    });
	
	function onfTrackedVideoFrame(currentTime,duration) {
		if (duration != NaN && duration - currentTime < 2) {
			fullVideoPlay();
		}
	}
	
	$(document).ready(function(){
		fullVideoPlay();  
	});
                         
    function fullVideoPlay() { 
    	fvideo.src = fvList[fcurr];  
        fvideo.load();
        $("#showFullVideo").on("canplay", function(event){
	        fvideo.play();
	    });
        fcurr++;
        if(fcurr >= fvLen){  
        	fcurr = 0; //重新循环播放
        }  
	}
	<#else>
	//全屏弹窗
	var i=0;
	clearInterval(timers); 
	$(document).ready(function(){	
		$('.layer-slider-div').append($('.wrap-full').first().clone());
		$('.layer-slider-div').width(1920*($('.wrap-full').length));
		timers=setInterval(layerSlider,${speedValue!});
		$(".layer-slider-div .wrap-full img").each(function(){
			$(this).load(function(){
				var imgScale = ($(this).width() / $(this).height()).toFixed(2);
		  		if(imgScale>(108/192)){
		  			$(this).width("99%");
		  		}else{
		  			$(this).height("99%");
		  		}
			});
		});
	});
	function layerSlider(){
	 	i++;
	  	if (i==$('.layer-slider-div .wrap-full').length) {
	  		i=1;
	  		$('.layer-slider-div').css({marginLeft:0});
	  	};
	  	$('.layer-slider-div').stop().animate({marginLeft:-i*$('.layer-slider-div .wrap-full').width()},0);
	}
	</#if>  
</#if>
</script>  