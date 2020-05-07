<#if bulletin.templetType == 9>
	<div class="post-fullscreen" style="height:100%; width:100%; background-color:${bulletin.grounding!}">${bulletin.content!}</div>
<#else>
	<div class="post-fullscreen <#if bulletin.templetType == 3>post-fullscreen-welcome<#elseif bulletin.templetType == 2>post-fullscreen-happy<#else>post-fullscreen-default</#if>">
		<div class="post-fullscreen-content <#if bulletin.templetType == 3>post-fullscreen-content-welcome<#elseif bulletin.templetType == 2>post-fullscreen-content-happy<#else>post-fullscreen-content-default</#if>">
			<#if bulletin.templetType == 1><h2>${bulletin.title!}</h2>
			<p>${bulletin.content!}</p>
			<#else>
			<span>${bulletin.content!}</span>
			</#if>
		</div>
	</div>
</#if>
<script>
<#if bulletin.lockScreen>
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
		lockfulltimestop = setTimeout(function(){
			$('.lock-full').hide();
		},6000);
	}else{
		$('.close-full').show();
		lockfulltimestop = setTimeout(function(){
			$('.close-full').hide();
		},6000);
	}
});

</script>
