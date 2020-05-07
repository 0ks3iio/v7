<#import "functionAreaSet-common.ftl" as areaSet />
<@areaSet.functionAreaSet hasComentSettings=true isApp = isApp>
<div class="commonApp">
    <h4 class="commonApp-title">已订阅应用<small style="color: red;">（注：拖拽排序）</small></h4>
    <div class="commonApp-body">
        <ul class="app-list app-list-big clearfix js-dragsort pos-rel app-list-added">
            <#if serverDtos?exists && serverDtos?size gt 0>
                <#list serverDtos as serverDto>
                    <li class="app edited" >
                        <#--<span class="app-btn app-btn-remove"><i class="wpfont icon-minus-round-fill"></i></span>-->
                        <img width="32" height="32" src="${serverDto.icon}" alt="" class="app-img">
                        <span class="app-name" title = ${serverDto.nameAll!}>${serverDto.name!}</span>
                        <input type="hidden" name="id" value="${serverDto.id!}" />
                        <input type="hidden" name="orderId" value="${serverDto.orderId!}" />
                    </li>
                </#list>
            <#else>

            </#if>
        </ul>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('.js-dragsort').dragsort({
            dragSelector: "li",
            dragSelectorExclude:".app-btn i",
            placeHolderTemplate:"<li class='app edited'></li>",
            dragEnd:function () {
                $(".commonApp-body .app-list-added").find("li.app").each(function () {
                    var index = $(this).attr("data-itemidx");
                    $(this).find("input[name='orderId']").val(parseInt(index)+1);
                })
            }
        });
    });
</script>
</@areaSet.functionAreaSet>