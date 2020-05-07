
<div class="layer-content height-1of1">
    <input type="hidden" id="groupId" value="${screenGroup.id!}" />
    <input type="hidden" id="groupUserId" value="${screenGroup.createUserId!}" />
    <div class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label">分组名称</label>
            <div class="col-sm-8">
                <input id="groupName" name="text" value="${screenGroup.name!}" class="form-control js-name" type="text" placeholder="请输入分组名称">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">是否轮播</label>
            <div class="col-sm-8" id="is_interval">
                <label><input <#if screenGroup?? && screenGroup.interval?default(0) gt 0>checked</#if> type="radio" name="radio" class="wp"><span class="lbl yes"> 是</span></label>
                <label><input <#if screenGroup?? && screenGroup.interval?default(0) gt 0><#else>checked</#if> type="radio" name="radio" class="wp"><span class="lbl no">否</span></label>
            </div>
        </div>


            <div class="form-group <#if screenGroup?? && screenGroup.interval?default(0) gt 0><#else>hide</#if>" id="interval-div">
                <label class="col-sm-3 control-label">间隔时间</label>
                <div class="col-sm-8">
                    <div class="form-group-txt">
                        <select name="" id="groupInterval">
                            <option <#if screenGroup?? && screenGroup.interval?default(0) == 15>selected</#if> value="15">15</option>
                            <option <#if screenGroup?? && screenGroup.interval?default(0) == 10>selected</#if> value="10">10</option>
                            <option <#if screenGroup?? && screenGroup.interval?default(0) == 5>selected</#if> value="5">5</option>
                            <option <#if screenGroup?? && screenGroup.interval?default(0) == 3>selected</#if> value="3">3</option>
                            <option <#if screenGroup?? && screenGroup.interval?default(0) == 2>selected</#if> value="2">2</option>
                        </select>
                        秒
                    </div>
                </div>
            </div>
    </div>
    <div class="clearfix height-calc-139">
        <p class="bold">选择大屏</p>
        <div class="box-screen">
                <#if screens?? && screens?size gt 0>
                    <#list screens as screen>
                        <div class="fake-form-group clearfix">
                            <div class="float-left lal-wrap">
                                <label>
                                    <input data-id="${screen.screenId!}" type="checkbox" class="wp screens" <#if screen.selected>checked</#if> >
                                    <span class="lbl"></span>
                                </label>
                            </div>
                            <div class="float-left img-wrap">
                                <img src="${screen.shotUrl}?${.now}"/>
                            </div>
                            <div class="float-left text-wrap">
                                <span>${screen.screenName!}</span>
                            </div>
                        </div>
                    </#list>
                </#if>
        </div>
    </div>
</div>
<script>
    $('#is_interval').find('.lbl').on('click', function () {
        if ($(this).hasClass('yes')) {
            $('#interval-div').removeClass('hide');
        } else {
            $('#interval-div').addClass('hide');
        }
    })
</script>