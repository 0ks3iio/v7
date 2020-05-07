<p class="tree-name border-bottom-cfd2d4 no-margin">
    <b>日志</b>
</p>
<div class="js-scroll-height padding-side-20 padding-t-20">
	<#if logList?exists&&logList?size gt 0>
        <div class="log padding-b-20 border-left-cfd2d4">
			<#list logList as log>
                <div class="padding-l-15 margin-b-10 border-bottom-cfd2d4 position-relative">
				<#if log.state! ==1>
				<img src="${request.contextPath}/static/bigdata/images/success-icon.png" class="pos-left left-minus-9 top-9" alt="" />
                <#elseif log.state! ==2>
				<img src="${request.contextPath}/static/bigdata/images/fail-icon.png" class="pos-left left-minus-9 top-9" alt="" />
                <#elseif log.state! ==-1>
				<img src="${request.contextPath}/static/bigdata/images/over-icon.png" class="pos-left left-minus-9 top-9" alt="" />
                <#elseif log.state! ==3>
			    <img src="${request.contextPath}/static/images/big-data/doing-icon.png" class="pos-left left-minus-9 top-9"/>
                </#if>
                    <p class="padding-l-25 position-relative line-h-18">
                        <span class="color-999">${log.logTime?string("yyyy-MM-dd HH:mm:ss")}</span>
                        <img src="${request.contextPath}/static/bigdata/images/time-icon.png" class="pos-left lef-0" alt="" />
                    </p>
                    <p class="two-line">${log.logDescription!}</p>
                    <div class="margin-b-10">
                        <img src="${request.contextPath}/static/bigdata/images/detail-icon.png" onclick="viewErrorLog('${log.id!}')" class="pointer js-log"/>
                    </div>
                </div>
            </#list>
        </div>
    <#else>
	<b>暂无日志</b>
    </#if>
</div>
<input type="hidden" id="status" value="${status!}">
<script type="text/javascript">
    function viewErrorLog(logId){
        var url =  '${request.contextPath}/bigdata/calculate/viewLog?logId='+logId;
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
<script>
    $(function () {
        var status = $('#status').val();
        var currentId = $('#calculateCurrentId').val();
        if (status == "RUNNING") {
            $('#' + currentId).find('.pos-right').attr('src', '${request.contextPath}/static/images/big-data/doing-icon.png');
            $('#running_btn').attr('disabled', 'disabled');
            $('#stop_btn').removeAttr('disabled');
        } else if (status == "FAILED") {
            $('#' + currentId).find('.pos-right').attr('src', '${request.contextPath}/static/images/big-data/fail-icon.png');
            $('#running_btn').removeAttr('disabled');
            $('#stop_btn').attr('disabled', 'disabled');
        } else if (status == "FINISHED") {
            $('#' + currentId).find('.pos-right').attr('src', '${request.contextPath}/static/images/big-data/success-icon.png');
            $('#running_btn').removeAttr('disabled');
            $('#stop_btn').attr('disabled', 'disabled');
        } else if (status == "KILLED") {
            $('#' + currentId).find('.pos-right').attr('src', '${request.contextPath}/static/images/big-data/over-icon.png');
            $('#stop_btn').attr('disabled', 'disabled');
            $('#running_btn').removeAttr('disabled');
        } else if (status == "SUBMITTED") {
            $('#' + currentId).find('.pos-right').attr('src', '${request.contextPath}/static/images/big-data/over-icon.png');
            $('#running_btn').attr('disabled', 'disabled');
            $('#stop_btn').removeAttr('disabled');
        } else if (status == "NULL") {
            $('#' + currentId).find('.pos-right').attr('src', '${request.contextPath}/static/images/big-data/not-begin-icon.png');
            $('#stop_btn').attr('disabled', 'disabled');
            $('#running_btn').removeAttr('disabled');
        }
    });
</script>