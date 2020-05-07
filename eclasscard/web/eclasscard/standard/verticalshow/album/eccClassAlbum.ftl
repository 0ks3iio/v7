<#if attachFolder?exists&&attachments?exists&&attachments?size gt 0 && attachFolder.type==2>
<div class="slider-item">
	<video id="showVideo" autoplay="true" <#if mute == 1>muted</#if> src="" width="100%" type="video/mp4"></video>
</div>
<#else>
<div class="slider-wrap">
	<div class="slider-btn">
		<a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
		<a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
	</div>
	<div class="slider-counter"></div>
	<div class="slider">
	<#if attachFolder?exists&&attachments?exists&&attachments?size gt 0>
		<#list attachments as item>
			<div class="slider-item">
				<div class="img-wrap img-wrap-16by9" style="background-image:url(${request.contextPath}${item.showPicUrl!});"></div>
				<#if attachFolder.type==1>
				<h4 class="slider-title">${item.filename!}</h4>
				</#if>
			</div>
		</#list>
	<#else>
		<div class="slider-item">
			<div class="img-wrap img-wrap-16by9">
				<div class="no-data center">
					<div class="no-data-content">
						<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
						<p>暂无多媒体</p>
					</div>
				</div>
			</div>
		</div>
	</#if>
	</div>
</div>
</#if>
<script id="albumScript" src="${request.contextPath}/static/eclasscard/standard/verticalshow/js/myscript.js" data="${speedValue!}"></script>
<script language="javascript">  
<#if attachFolder?exists&&attachments?exists&&attachments?size gt 0 && attachFolder.type==2>
    var vList = [
    	<#list attachments as item>
    		'${item.dirPath!}'
    		<#if item_index+1 != attachments?size>,</#if>
    	</#list>  
    	];
    	
    var vLen = vList.length; 
    var curr = 0; 
    var video = document.getElementById("showVideo"); 
<#--video.addEventListener('ended', function(){
		play();
	});   --> 
	
	$("#showVideo").on("timeupdate", function(event){
      	onTrackedVideoFrame(this.currentTime, this.duration);
    });
    
    video.addEventListener("loadedmetadata", function () {
    	var height = $(this).height();
    	$(this).parent().parent().css('height',height);
	});
	
	function onTrackedVideoFrame(currentTime,duration) {
		if (duration != NaN && duration - currentTime < 2) {
			play();
		}
	}
	
	$(document).ready(function(){
		play();  
	});
                         
    function play() { 
    	video.src = vList[curr];  
        video.load();   
        video.play();  
        curr++;  
        if(curr >= vLen){  
        	curr = 0; //重新循环播放
        }  
	}
</#if>  
</script>  