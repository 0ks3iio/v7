<#import "functionAreaSet-common.ftl" as areaSet />
<@areaSet.functionAreaSet hasComentSettings=true>
    <div class="commonApp">
        <h4 class="commonApp-title">已添加模块<small style="color: red;">（注：最多12个，拖拽可排序）</small></h4>
        <div class="commonApp-body">
            <ul class="app-list app-list-big clearfix js-dragsort pos-rel app-list-added">
                <#if authorityModelDtoList?exists && authorityModelDtoList?size gt 0>
                    <#list authorityModelDtoList as modelDto>
                    <li class="app edited modelId${modelDto.model.id!}" >
                        <span class="app-btn app-btn-remove"><i class="wpfont icon-minus-round-fill"></i></span>
                        <img width="32" height="32" src="${modelDto.imageUrl!}" alt="" class="app-img">
                        <span class="app-name">${modelDto.model.name!}</span>
                        <input type="hidden" name="modelId" value="${modelDto.model.id!}" />
                        <input type="hidden" name="subsystem" value="${modelDto.model.subSystem!}" />
                        <input type="hidden" name="displayOrder" value="" />
                    </li>
                    </#list>
                </#if>
            </ul>
        </div>
    </div>
    <div class="commonApp">
        <h4 class="commonApp-title">所有模块</h4>
        <div class="commonApp-body no-padding clearfix">
            <div class="commonApp-nav">
                <h4>子系统分类</h4>
                <ul class="nav nav-list" id="operationSystem">
                    <#if serverDtos?exists && serverDtos?size gt 0>
                        <#list serverDtos as serverDto>
                        <li>
                            <a href="#" onclick="showSystemModels('${serverDto.server.subId}')">
                                <img class="menu-icon" src="${serverDto.imageUrl!}" alt="" width="20px" height="20px">
                                <span class="menu-text">${serverDto.server.name!}</span>
                            </a>
                        </li>
                        </#list>
                    </#if>
                </ul>
            </div>
            <div class="commonApp-content">
                <#if serverDtos?exists && serverDtos?size gt 0>
                    <#list serverDtos as serverDto>
                    <ul class="app-list app-list-big clearfix <#if serverDto_index!=0 >close<#else>open</#if> " id="server${serverDto.server.subId!}" <#if serverDto_index!=0>style="display:none;" </#if>>
                        <#if serverDto.modelDtos?exists && serverDto.modelDtos?size gt 0>
                            <#list serverDto.modelDtos as modelDto>
                            <li class="app edited modelId${modelDto.model.id!}" id="modelId${modelDto.model.id!}">
                                <span class="app-btn app-btn-add"><i class="wpfont icon-plus-round-fill"></i></span>
                                <img width="32" height="32" src="${modelDto.imageUrl!}" alt="" class="app-img">
                                <span class="app-name">${modelDto.model.name!}</span>
                                <input type="hidden" name="modelId" value="${modelDto.model.id!}" dataType="map" exclude="true" />
                                <input type="hidden" name="subsystem" value="${serverDto.server.subId!}"  exclude="true" />
                                <input type="hidden" name="displayOrder" value="" exclude="true" />
                            </li>
                            </#list>
                        </#if>
                    </ul>
                    </#list>
                </#if>
            </div>

        </div>
    </div>
    <script>
        $(document).ready(function () {
            $('.commonApp-nav .nav-list').slimscroll({
                height:'250px'
            })
            $('.commonApp-content').wrapInner('<div></div>').find('>div').slimscroll({
                height:'260px'
            })
            initDragSort();
            $('.app-btn-remove').on('click',function(){
                appRemove($(this).parent(),initSortNumber);
            });
            $("#operationSystem").on("click",function (e) {
                e.stopPropagation();
                function getNode(node){
                    return node.nodeName==='A' ? node : getNode(node.parentNode);
                }
                var $self=$(getNode(e.target));
                if($self.hasClass('dropdown-toggle')){
                    if($self.closest('li').hasClass('open')){
                        hideSubmenu( $self.next() );
                    }else{
                        $self.closest('li').siblings().removeClass('open active')
                                .find('li').removeClass('active');
                    }
                }else{
                    $self.closest('li').addClass('active')
                            .siblings().removeClass('open active')
                            .find('li').removeClass('active');
                }
            });
            $(".commonApp-content .app-list").find("li.app").each(function () {
                $(this).unbind("click").bind("click",function () {
                    appAdd($(this),initSortNumber);
                });
                var modelId = $(this).attr("id");
                if ($("li." +  modelId).length > 1) {
                    $(this).remove();
                }

            });

        });

        function showSystemModels(subId) {
            var $system = $("#server"+subId);
            if($system.hasClass("close")) {
                $(".commonApp-content").find("ul.open").removeClass("open").addClass("close").css("display","none");
                $system.removeClass("close").addClass("open").css("display","");
            }
        }
        function appRemove(obj,callBack) {
            if (!obj) return ;
            var serverId = $(obj).find("input[name='subsystem']").val();
            var modelId = $(obj).find("input[name='modelId']").val();
            if($("li.modelId"+modelId).length>1){
                $(obj).remove();
                callBack();
                return ;
            }
            var $removeLi = $(obj).clone(true).appendTo($("#server"+serverId)).unbind().bind("click",function () {
                appAdd($(this),initSortNumber);
            });
            $removeLi.find("span.app-btn-remove").removeClass("app-btn-remove").addClass("app-btn-add").unbind("click")
            .find("i.icon-minus-round-fill").removeClass("icon-minus-round-fill").addClass("icon-plus-round-fill");
            $removeLi.find("input").each(function(){
                $(this).attr("exclude","true");
            })
            $(obj).remove();

            callBack();
        }

        function appAdd(app,callBack) {
            var maxNumber = parseInt($(".commonApp-body .app-list-added").find("li:last").attr("data-itemidx")) + 1;
            if (maxNumber>=12) {
                showWarnMsg("您最多添加12个常用操作!");
                return ;
            }
            $(app).clone(true).appendTo(".commonApp-body .app-list-added")
                    .unbind("click").find("span.app-btn").removeClass("app-btn-add")
                    .addClass("app-btn-remove").bind("click",function () {
                appRemove($(this).parent(),initSortNumber)
            }).find("i.icon-plus-round-fill").removeClass("icon-plus-round-fill").addClass("icon-minus-round-fill");
            $(app).remove();
//            $('.js-dragsort').dragsort("destroy")
            callBack();
        }

        function initDragSort() {
            $('.js-dragsort').dragsort({
                dragSelector: "li",
                dragSelectorExclude:".app-btn i",
                placeHolderTemplate:"<li class='app edited'></li>",
                dragEnd:function () {
                    $(".commonApp-body .app-list-added").find("li.app").each(function () {
                        var index = $(this).attr("data-itemidx");
                        $(this).find("input[name='displayOrder']").val(parseInt(index)+1);
                    })
                }
            });
            initSortNumber();
        }
        function initSortNumber() {
            function redragsort(){
                $('.js-dragsort li').each(function(index){
                    $(this).attr('data-itemidx',index)
                })
            }
            redragsort();
            $(".commonApp-body .app-list-added").find("li.app").each(function () {
               $(this).find("input[name='displayOrder']").val(parseInt($(this).attr("data-itemidx")) + 1);
               $(this).find("input").each(function () {
                   $(this).removeAttr("exclude");
               })
            });
        }
    </script>
</@areaSet.functionAreaSet>
