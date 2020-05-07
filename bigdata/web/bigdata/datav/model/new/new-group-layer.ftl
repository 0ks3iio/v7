<div class="layer-wrap layer-group">
    <div class="layer-made layer-bi-style">
        <div class="layer-made-head">
            <span class="layer-head-name"></span><img src="${request.contextPath}/bigdata/v3/static/images/bi/close.png" class="js-close" alt="">
        </div>
        <input type="hidden" id="groupId" value="${screenGroup.id!}" />
        <input type="hidden" id="groupUserId" value="${screenGroup.createUserId!}" />
        <div class="layer-made-body clearfix">
            <div class="filter-made mb-20">
                <div class="filter-item">
                    <span class="mr-10">分组名称</span><input id="groupName" value="${screenGroup.name!}"  type="text" class="form-control" placeholder="输入分组名称">
                </div>
                <div class="filter-item">
                    <span class="ml-20 mr-10">是否轮播</span>
                </div>
                <div class="filter-item">
                    <label class="choice ml-20">
                        <input <#if screenGroup?? && screenGroup.interval?default(0) gt 0>checked</#if> type="radio" name="r1" class="yes" />
                        <span  class="choice-name">是</span>
                    </label>
                </div>
                <div class="filter-item">
                    <label class="choice ml-20">
                        <input <#if screenGroup?? && screenGroup.interval?default(0) gt 0><#else>checked</#if> type="radio" name="r1"/>
                        <span class="choice-name">否</span>
                    </label>
                </div>
            </div>
            <div class="filter-content">
                <div class="row-made">
                    <#if screens?? && screens?size gt 0>
                        <#list screens as screen>
                            <div class="col-made-8">
                                <div class="screen-box">
                                    <div class="box-img clearfix">
                                        <img src="${screen.shotUrl}?${.now}">
                                    </div>
                                    <div class="box-text">
                                        <span>${screen.screenName!}</span>
                                    </div>
                                    <div class="pos-initial-checkbox">
                                        <label class="choice">
                                            <input type="checkbox" data-id="${screen.screenId!}" class="screens" name="c1" <#if screen.selected>checked</#if>/>
                                            <span class="choice-name"></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>
        <div class="layer-made-foot">
            <div class="btn-made unfilled-corner group-ok">确定</div>
            <div class="btn-made unfilled-corner js-close">取消</div>
        </div>
    </div>
</div>