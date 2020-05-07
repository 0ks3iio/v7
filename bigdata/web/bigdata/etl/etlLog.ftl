<div class="js-scroll-height padding-side-20 padding-t-20">
	<#if logList?exists&&logList?size gt 0>
	<div class="log padding-b-20 border-left-cfd2d4">
			<#list logList as log>
			<div class="padding-l-15 margin-b-10 border-bottom-cfd2d4 position-relative">
				<#if log.state! ==1>
				<img src="${request.contextPath}/static/bigdata/images/success-icon.png" class="pos-left left-minus-9 top-9" alt="" />
				<#elseif log.state! ==2>
				<img src="${request.contextPath}/static/bigdata/images/fail-icon.png" class="pos-left left-minus-9 top-9" alt="" />
				</#if>
				<p class="padding-l-25 position-relative line-h-18">
					<span class="color-999">${log.logTime?string("yyyy-MM-dd HH:mm:ss")}</span>
					<img src="${request.contextPath}/static/bigdata/images/time-icon.png" class="pos-left lef-0" alt="" />
				</p>
				<p class="two-line">${log.logDescription!}</p>
				<div class="margin-b-10">
					<img src="${request.contextPath}/static/bigdata/images/detail-icon.png" onclick="viewErrorLog('${log.id!}')" class="pointer js-log"/>
					<div class="float-right color-999">
						耗时：${log.durationTime!}ms
					</div>
				</div>
			</div>
			</#list>
	</div>
	<#else>
	<div class="wrap-1of1 centered no-data-state">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
				<p>暂无日志</p>
			</div>
		</div>
	</#if>
</div>
<script type="text/javascript">
	function viewErrorLog(logId){
		var url =  '${request.contextPath}/bigdata/etl/viewLog?logId='+logId;
		 $("#logDiv").load(url,function(){
	        layer.open({
	            type: 1,
	            shade: .5,
	            title: ['日志','font-size:16px'],
	            area: ['800px','600px'],
	            maxmin: false,
	            btn:['确定'],
	            content: $('#logDiv'),
	            resize:true,
	            yes:function (index) {
	                layer.closeAll();
	                 $("#logDiv").empty();
	            },
	            cancel:function (index) {
	                layer.closeAll();
	                $("#logDiv").empty();
	            }
	        });
	        $("#logDiv").parent().css('overflow','auto');
	    })
	}

    $(function(){
       $('.js-scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top -41,
                overflow: 'auto'
            })
        });
        
        $('.tree').each(function(){
            $(this).css({
                height: $(window).height() - $(this).offset().top - 40
            })
        });
    })
</script>