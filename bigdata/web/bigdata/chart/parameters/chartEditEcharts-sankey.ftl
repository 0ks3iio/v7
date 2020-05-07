<#import "ec_element.ftl" as ec_e />
<ul class="nav nav-tabs nav-tabs-1" id="echarts_parameters_tab">
    <li class="active">
        <a href="#echarts_basic_config" data-toggle="tab">基础配置</a>
    </li>
    <#--<li class="">-->
        <#--<a href="#echarts_advanced_config" data-toggle="tab">高级配置</a>-->
    <#--</li>-->
    <#--<li class="">-->
        <#--<a href="#echarts_code_config" data-toggle="tab">配置代码</a>-->
    <#--</li>-->
</ul>
<div class="tab-content no-padding">
    <div class="tab-pane clearfix basic-set active" id="echarts_basic_config">
        <div class="left-part left-part-two scroll-height-inner left-part-echarts-parameters">
            <p class="active">
                图表外观
            </p>
            <p>
                标题
            </p>
            <p>
                提示框
            </p>

        <#--<p>-->
        <#--数据系列-->
        <#--</p>-->
        </div>
        <div class="imgs-choice scroll-height-inner" style="overflow-x: auto;overflow-y: hidden;">
            <div class="litimg">
                <table class="table table-striped table-hover table-selfs option-base">
                    <tbody>
                        <@ec_e.ec_colors colors=optionExpose.colors />
                    </tbody>
                </table>
            </div>
            <div class="litimg none title-parameters">
                <table class="table table-striped table-hover table-selfs">
                    <tbody>
                        <@ec_e.ec_title oe=optionExpose.exposeTitle! />
                    </tbody>
                </table>
            </div>
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs tooltip-parameters">
                    <tbody>
                        <@ec_e.ec_tooltip oe=optionExpose />
                    </tbody>
                </table>
            </div>
        <#--<div class="litimg none">-->
        <#--<@ec_e.ec_series oe=optionExpose series=optionExpose.exposeSeries />-->
        <#--</div>-->
        </div>

    </div>
    <div class="tab-pane" id="echarts_advanced_config">
        <div class="left-part left-part-two js-inner-height">
            <p>
                图标配置
            </p>
            <p>
                颜色集合
            </p>
            <p>
                导出
            </p>
            <p>
                标签
            </p>
            <p>
                数据列
            </p>
            <p>
                副标题
            </p>
            <p>
                标题
            </p>
        </div>
        <div class="imgs-choice js-inner-height">
            <div class="litimg none">
                44
            </div>
            <div class="litimg none">
                25
            </div>
            <div class="litimg none">
                36
            </div>
        </div>
    </div>
    <div class="tab-pane" id="echarts_code_config">

    </div>
</div>
<script>
    /*
     * 检查图表类型，当图表类型为27 有些参数将不能开放
     * 包括
     *      x轴反序显示
     *      y轴设置除了
     */

    $(function () {
        var type = ${type};
        if (type == 27) {

        }
    });
</script>
