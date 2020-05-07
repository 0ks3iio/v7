<div class="col-sm-${(data.col)!}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>
        </div>
        <div class="box-body" <#--style="height: ${data.height!}px;-->>
            <ul class="msg-list">
                <#if data.messageDtos?exists && data.messageDtos?size gt 0>
                    <#list data.messageDtos as messageDto>
                        <li class="msg-item">
                            <#--(id,name,mode,fullUrl,serverName,parentName,subId,dirId)-->
                            <a href="javascript:void(0);" onclick="openModel('${messageDto.modelId!}','${messageDto.modelName!}','2','${messageDto.url!}','','','','');" title = ${messageDto.message.title!}>
                                <span class="msg-label">${messageDto.messageTypeName}</span>
                                <span class="msg-time">${messageDto.sendTime!}</span>
                                <span class="msg-text">${messageDto.message.title!}</span>
                            </a>
                        </li>
                    </#list>
                <#else>
                	${data.messageEmpty!"暂无内容"}
                </#if>
            </ul>
            <#if data.messageDtos?exists && data.messageDtos?size gt 0>
            <div class="box-more"><a href="javascript:void(0);" onclick="openModel('${data.modelId!}','${data.modelName!}','2','${data.isMoreUrl!}','','','','');">更多&gt;</a></div>
            </#if>
        </div>
    </div>
</div>