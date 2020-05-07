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
                <#if data.weeklyWork?exists && data.weeklyWork?size gt 0>
                    <#list data.weeklyWork as work>
                        <li class="msg-item">
                            <#--(id,name,mode,fullUrl,serverName,parentName,subId,dirId)-->                                                      
                            <a href="javascript:void(0);" onclick="openModel('${data.modelId!}','${data.modelName!}','${data.openType!}','${work.url!}','','','','');" title=${work.titleString!}>                                
                                <span class="msg-time">${work.showTime!}</span>
                                <span class="msg-text">${work.title!}</span>                                                                               
                            </a>
                        </li>
                    </#list>
                <#else>
                	${data.messageEmpty!"暂无内容"}
                </#if>
            </ul>
            <#if data.weeklyWork?exists && data.weeklyWork?size gt 0>
            <div class="box-more"><a href="javascript:void(0);" onclick="openModel('${data.modelId!}','${data.modelName!}','${data.openType!}','${data.isMoreUrl!}','','','','');">更多&gt;</a></div>
            </#if>
        </div>
    </div>
</div>
