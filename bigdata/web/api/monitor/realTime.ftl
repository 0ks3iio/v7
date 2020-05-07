<!-- PAGE CONTENT BEGINS -->
<div class="inter-timeout">
    <div class="timeout-header">响应超时</div>
    <div class="timeout-body gun-content" id="gunshow1">
       <#if warning?exists &&(warning?size gt 0)>
            <#list warning as dto>
		        <div class="timeout-li">
		            <i></i>
		            <span>${dto.creationTime?string("HH:mm")}接口api${dto.pushUrl!}超时</span>
		        </div>
	        </#list>
	   </#if>
    </div>
</div>

<div class="inter-realtime">
    <div class="timeout-header">实时调用</div>
    <div class="timeout-body gun-content" id="gunshow2">
      <#if detail?exists && (detail?size gt 0)>
            <#list detail as dto>
		         <div class="timeout-li">
		            <i></i>
		            <span>${dto.creationTime?string("HH:mm")}接口api${dto.pushUrl!}被${dto.developerName!}调用</span>
		         </div>
	        </#list>
	   </#if>
    </div>
</div>
<!-- PAGE CONTENT ENDS -->
<script type="text/javascript">
     function startmarquee(lh, speed, delay, id) {
        var t;
        var oHeight = speed; /** div的高度 **/
        var p = false;
        var o = document.getElementById(id);
        var preTop = 0;
        o.scrollTop = 0;
        function start() {
            t = setInterval(scrolling);
            o.scrollTop += 1;
        }
        function scrolling() {
            if (o.scrollTop % lh != 0 && o.scrollTop % (o.scrollHeight - oHeight - 1) != 0) {
                preTop = o.scrollTop;
                o.scrollTop += 1;
                if (preTop >= o.scrollHeight || preTop == o.scrollTop) {
                    o.scrollTop = 0;
                }
            } else {
                clearInterval(t);
                setTimeout(start, delay);
            }
        }
        setTimeout(start, delay);
    }
		
	startmarquee(1,84,100,"gunshow1"); 
    startmarquee(1,84,100,"gunshow2"); 
    
    
</script>