<#if attachFolders?exists&&attachFolders?size gt 0>
    <#list attachFolders as item>
    <div class="box-media" onclick="showMultimedia('${item.id}',${item.type})">
        <div class="box-media-img clearfix bg-green">
            <#if item.type == 1>
                <img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/photos.png" >
            <#else>
                <img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/videos.png" >
            </#if>
            <div class="left-top" <#if !item.show>style="display: none"</#if>>
                <img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/triangle.png" >
            </div>
        </div>
        <div class="box-media-name">${item.title!}</div>
    </div>
    </#list>
</#if>