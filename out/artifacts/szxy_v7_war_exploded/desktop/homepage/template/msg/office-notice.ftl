
<div class="col-sm-${(data.col)!}">
    <div class="box box-default">
        <#assign isMore = true>
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>            
        </div>
        <#if (data.firstUrl!)?default("")!="">
        <iframe src = '${(data.firstUrl)!}' style="display:none" />
        </#if>
        <div class="box-body" <#--style="height: ${data.height!}px;-->>
            <ul class="msg-list">
                <#if data.officeNotice?exists && data.officeNotice?size gt 0>
                    <#list data.officeNotice as office>
                        <li class="msg-item">
                            <#--(id,name,mode,fullUrl,serverName,parentName,subId,dirId)-->                                                      
                            <a href="javascript:void(0);" onclick="openOffice('${office.url!}');" title=${office.titleString!}>                                
                                <span class="msg-label">${office.areaName!}</span>
                                <span class="msg-time">${office.createTime!}</span>
                                <span class="msg-text">${office.title!}</span>                                                                               
                            </a>
                        </li>
                    </#list>
                <#else>
                	${data.messageEmpty!"暂无内容"}
                </#if>
            </ul>
            <#if data.officeNotice?exists && data.officeNotice?size gt 0>
            <div class="box-more"><a href="javascript:void(0);" onclick="openModel('${data.modelId!}','${data.modelName!}','${data.openType!}','${data.isMoreUrl!}','','','','');">更多&gt;</a></div>
            </#if>
        </div>
    </div>
</div>
<script>
     function openOffice(fullUrl){
     
      window.open(fullUrl,'','fullscreen,scrollbars,resizable=yes,toolbar=no');
     }
     


</script>

