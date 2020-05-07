<div class="col-sm-${(data.col)!}">
    <div class="box box-default">
        <#assign isMore = true>
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>            
        </div>
        <#if (data.firstUrl!)?default("") != "">
        <iframe src = '${(data.firstUrl)!}' style="display:none" />
        </#if>
        <div class="box-body" <#--style="height: ${data.height!}px;-->>
            <ul class="msg-list">
                <#if data.behaveCalendar?exists && data.behaveCalendar?size gt 0>
                    <#list data.behaveCalendar as calendar>
                        <li class="msg-item">
                            <#--(id,name,mode,fullUrl,serverName,parentName,subId,dirId)-->                                                      
                            <a href="javascript:void(0);" onclick="openCalendar('${calendar.url!}');" title=${calendar.titleString!}>                                
                                <span class="msg-label">${calendar.areaName!}</span>
                                <span class="msg-time">${calendar.createTime!}</span>
                                <span class="msg-text">${calendar.title!}</span>                                                                               
                            </a>
                        </li>
                    </#list>
                <#else>
                	${data.messageEmpty!"暂无内容"}
                </#if>
            </ul>
            <#if data.behaveCalendar?exists && data.behaveCalendar?size gt 0>
                <div class="box-more"><a href="javascript:void(0);" onclick="openModel('${data.modelId!}','${data.modelName!}','${data.openType!}','${data.isMoreUrl!}','','','','');">更多&gt;</a></div>
            </#if>
        </div>
    </div>
</div>
<script>
     function openCalendar(fullUrl){
     
      window.open(fullUrl,'','fullscreen,scrollbars,resizable=yes,toolbar=no');
     }
     


</script>
