<div class="col-sm-${(data.col)!}">
    <div class="box box-default">
        <#assign isMore = true>
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>
        </div>
        <div class="box-body" <#--style="height: ${data.height!}px;-->>
            <ul class="msg-list">
                <#if data.todo?exists && data.todo?size gt 0>
                    <#list data.todo as to>
                        <li class="msg-item">
                            <#--(id,name,mode,fullUrl,serverName,parentName,subId,dirId)-->
                            <a href="javascript:void(0);" onclick="openModel('${to.modelId!}','${to.modelName!}','${to.openType!}','${to.url!}','','','','');">              
                                <span class="msg-label">${to.moduleSimpleName!}</span>
                                <span class="msg-time"></span>
                                <span class="msg-text">${to.moduleContent!}</span>                                                              
                            </a>
                        </li>
                    </#list>
                <#else>
                	${data.messageEmpty!"暂无内容"}
                </#if>
            </ul>
        </div>
    </div>
</div>