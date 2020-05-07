<div class="point-pro-box">
    <#if itemScaleList?exists && (itemScaleList?size gt 0)>
    <div class="evaluate-item clearfix">
        <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">评价类型得分占比：</span>
        <div class="evaluate-item-right">
            <div class="point-pro-item clearfix">

                <div class="point-proitem-left">日常：</div>
                <div class="point-proitem-right">${dailySacle.scale!?string('0.0')?default(0)}%</div>
            </div>
            <div class="point-pro-item clearfix">
                <div class="point-proitem-left">期末：</div>
                <div class="point-proitem-right">${termScale.scale!?string('0.0')}%</div>
            </div>
        </div>
    </div>
    <div class="evaluate-item clearfix" style="font-size: 0;"><span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">各项目得分占比：</span>
        <div class="evaluate-item-right point-probox-right">
            <#if itemScaleList?exists && (itemScaleList?size gt 0)>
              <#list itemScaleList as item>
                    <div class="point-pro-item clearfix">
                        <div class="point-proitem-left">${item.itemName!}：</div>
                        <div class="point-proitem-right">
                            <#if item.scale?exists && item.scale != 0>
                                ${item.scale?string('0.0')}%
                                <#else > 0%
                            </#if>
                        </div>
                    </div>
                </#list>
            </#if>
        </div>

    </div>

    <div class="evaluate-item clearfix mt10">
                        <span class="layer-evaluate-label evaluate-item-left" style="height: 1px;width: 140px;">
                        </span>
        <div class="evaluate-item-right">
            <div class="btn btn-blue font-14" onclick="newScale()">
                编辑
            </div>
        </div>
    </div>
    <#else>
    <div class="eva-no-content">
        <img src="/static/images/evaluate/nocontent.png">
        <div class="mb10">暂无数据可同步上届数据或新建数据哦~</div>
        <#if canSync?exists>
            <#if canSync == 0>
                <button class="btn btn-blue mr10" onclick="syncRule()">同步上届数据</button>
            <#else >
                <button class="btn btn-blue mr10 disabled" >同步上届数据</button>
            </#if>
        </#if>
        <div class="btn btn-white" onclick="newScale()">新建评分规则</div>
    </div>
    </#if>
</div>

<script type="text/javascript">
    $(function() {
        leftresize();  // js样式
    })
    function leftresize() {
        var onedom = null;
        $(".point-probox-right").find(".point-pro-item").each(function(i, v) {
                if (i % 2 == 0) {onedom = v;
                } else {
                    if ($(v).height() > $(onedom).height()) {
                        $(onedom).height($(v).height());
                    } else {
                        $(v).height($(onedom).height());
                    }
                }
            });
        $(".point-probox-basic").find(".point-pro-item").each(function(i, v) {
                if (i % 2 == 0) {
                    onedom = v;
                } else {
                    if ($(v).height() > $(onedom).height()) {
                        $(onedom).height($(v).height());
                    } else {
                        $(v).height($(onedom).height());
                    }
                }
            });
        $(".point-proitem-right").each(function(i, v) {
            $(v).css("height", $(v).siblings(".point-proitem-left").height() + 20);
        });

        $(window).resize(function() {
            $(".evaluate-body").css("min-height", $("#sidebar").height() - 103);
        });
    }
  </script>