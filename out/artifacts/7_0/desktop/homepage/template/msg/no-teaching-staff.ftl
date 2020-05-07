<div class="col-sm-${(data.col)!}">
    <div class="box box-default">
        <#assign isMore = true>
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>            
        </div>
        <div class="box-body" <#--style="height: ${data.height!}px;-->>
            <ul class="msg-list">
                <#if data.noTeachingStaff?exists && data.noTeachingStaff?size gt 0>
                    <#list data.noTeachingStaff as noTeaching>
                        <li class="msg-item">
                            <#--(id,name,mode,fullUrl,serverName,parentName,subId,dirId)-->                                                      
                                <span class="msg-label">${noTeaching.deptName!}</span>
                                <span class="msg-text">${noTeaching.realName!}</span>
                                <span class="msg-time">${noTeaching.reason!}</span>                                                                               
                        </li>
                    </#list>
                <#else>
                	${data.messageEmpty!"暂无内容"}
                </#if>
            </ul>
        </div>
    </div>
</div>
