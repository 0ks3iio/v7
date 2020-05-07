<!--大图标-->
<div class="col-sm-${(data.col)!12}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>
        </div>
        <div class="box-body" <#--style="height: ${data.height!}px;"-->>
            <ul class="app-list app-list-big clearfix">
            <#if data?? && data.apps?exists>
                <#list data.apps as app>
                    <li class="app" style="width: 110px;">
                        <a href="${app.url!}" onclick="openApp('${app.id!}','${app.name!}','${app.openType!}','${app.url!}','','','','',true);" title="${app.fullName!}" target="_blank">
                            <img src="${app.icon}" alt="" class="app-img" height="64px" width="64px" />
                            <span class="app-name">${app.name}</span>
                        </a>
                    </li>
                </#list>
            </#if>
            </ul>
        </div>
    </div>
</div>

<script>
    //记录应用点击次数
  function openApp(id,name,mode,fullUrl,serverName,parentName,subId,dirId,thirdAp){
   $.ajax({
            url:"${request.contextPath}/desktop/recommendApp/openApp?serverId="+id,
            contentType:'application/json',
            dataType:'json',
            type:"post",
            success:function (data) {
                //即使出现问题，此处都不应展示给用户
            }
      });
  }

</script>