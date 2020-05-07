<div class="box box-default">
    <div class="box-body">
        <ul class="nav nav-tabs moduleTabList" role="tablist">
            <#list tabList as tab>
                <li role="presentation" <#if (tab_index==0 && !tabCode??) || (tabCode?? && tabCode=tab.tabCode)>class="active"</#if> data-url="${request.contextPath}${tab.url }">
                    <a href="#aa" role="tab" data-toggle="tab">${tab.menuName }</a>
                </li>
            </#list>
        </ul>
        <!-- tab页内容加载区域 -->
        <div class="tab-content" id="TabContentDiv">
        </div>
    </div>
</div>

<script>
  $(function(){
      window.tabContentLoad = function(url,data){
         if(data){
            $("#TabContentDiv").load(url,data);
            return;
          }
        $("#TabContentDiv").load(url);
      };

      //默认加载tab内容页
      tabContentLoad($('.moduleTabList li[class="active"]').data("url"));

      //Tab切换
      $('.moduleTabList li').on("click",function(){
        tabContentLoad($(this).data("url"));
      });
  })
</script>

