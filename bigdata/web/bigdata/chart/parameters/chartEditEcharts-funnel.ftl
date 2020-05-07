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
                图例
            </p>
            <p>
                提示框
            </p>
            <p>
                数据标签
            </p>
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
            <div class="litimg none legend-parameters">
                <table class="table table-striped table-hover table-selfs">
                    <tbody>
                        <@ec_e.ec_legend oe=optionExpose />
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
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs tooltip-parameters">
                    <tbody>
                    <tr>
                        <th colspan="2">通用</th>
                    </tr>
                    <tr>
                        <td class="text-right">启用数据标签</td>
                        <td>
                            <@ec_e.ec_yes_no v=optionExpose.showSLabel dv=false id="showSLabel" />
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">位置</td>
                        <td>
                            <div class="dropdown">
                                <p class="dropdown-self no-margin clearfix" id="dLabel4" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <span id="sLabelPosition" class="choose-text" data-text="inside" data-dt="内部" v="${optionExpose.sLabelPosition!}">${OEUtils.doSLabelPosition(optionExpose.sLabelPosition)}</span>
                                <div class="pos-right">
                                    <span class="caret"></span>
                                </div>
                                </p>
                                <ul class="dropdown-menu" aria-labelledby="dLabel4">
                                    <li><a href="#" v="left">${OEUtils.doSLabelPosition('left')}</a></li>
                                    <li><a href="#" v="inside">${OEUtils.doSLabelPosition('inside')}</a></li>
                                    <li><a href="#" v="right">${OEUtils.doSLabelPosition('right')}</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
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

</script>
