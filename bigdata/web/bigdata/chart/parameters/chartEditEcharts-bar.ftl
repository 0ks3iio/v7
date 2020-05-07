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
                坐标系
            </p>
            <p>
                坐标轴
            </p>
            <p>
                提示框
            </p>
            <p>
                数据标签
            </p>
            <#if type != 27>
            <p>
                数据系列
            </p>
            </#if>
        </div>
        <div class="imgs-choice scroll-height-inner" style="overflow-x: auto;overflow-y: hidden;">
            <div class="litimg">
                <table class="table table-striped table-hover table-selfs option-base">
                    <tbody>
                        <@ec_e.ec_colors colors=optionExpose.colors />
                    <tr>
                        <th colspan="2">其他参数</th>
                    </tr>
                    <tr>
                        <td class="text-right">柱子宽度
                            <div class="card-tooltip">
                                <p>通常情况下您不用设置柱子宽度柱子的宽度将会自适应变化</p>
                            </div>
                        </td>
                        <td>
                            <input id="barWidth" type="number" name="" value="${optionExpose.barWidth!}" min="0"
                                   class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">柱子边框圆角
                            <div class="card-tooltip">
                                <p>大于等于0的数字可以将柱子四个角设置为相同的圆角</p>
                                <p>使用数组[5, 10, 0, 0]可以分别设置柱子四个边角</p>
                            </div>
                        </td>
                        <td>
                            <input id="barBorderRadius" type="text" name="" value="${optionExpose.barBorderRadius!}"
                                   min="0" class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">不同系列柱子间距
                            <div class="card-tooltip">
                                <p>可以理解为同一类目上的不同柱子的间距</p>
                                <p>正整数或者百分比例如：20 30%</p>
                            </div>
                        </td>
                        <td>
                            <input id="barGap" type="text" name="" value="${optionExpose.barGap!}" min="0"
                                   class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">相同系列柱子之前的间距
                            <div class="card-tooltip">
                                <p>可以理解为同一类目轴上的不同类目之前的柱子的间距</p>
                                <p>正整数或者百分比例如：20 30%</p>
                            </div>
                        </td>
                        <td>
                            <input id="barCategoryGap" type="text" name="" value="${optionExpose.barCategoryGap!}"
                                   min="0" class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">平滑曲线
                            <div class="card-tooltip">
                                <p>只有当存在线状图形的时候该参数才会生效</p>
                            </div>
                        </td>
                        <td>
                                <@ec_e.ec_yes_no v=optionExpose.smoothLine?default(false) dv=false id="smoothLine" />
                        </td>
                    </tr>
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
            <div class="litimg none grid-parameters">
                <table class="table table-striped table-hover table-selfs option-xy">
                    <tbody>
                    <tr>
                        <td class="text-right">Left
                            <div class="card-tooltip">
                                <p>坐标系相对左边的位置</p>
                                <p>可设置固定宽度或者百分比</p>
                            </div>
                        </td>
                        <td>
                            <input id="gridLeft" type="text" name="" value="${optionExpose.gridLeft!}"
                                   class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">Right
                            <div class="card-tooltip">
                                <p>坐标系相对右边的位置</p>
                                <p>可设置固定宽度或者百分比</p>
                            </div>
                        </td>
                        <td>
                            <input id="gridRight" type="text" name="" value="${optionExpose.gridRight!}"
                                   class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">Top
                            <div class="card-tooltip">
                                <p>坐标系相对顶部的位置</p>
                                <p>可设置固定宽度或者百分比</p>
                            </div>
                        </td>
                        <td>
                            <input id="gridTop" type="text" name="" value="${optionExpose.gridTop!}" class="width-1of1"
                                   placeholder="auto"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">Bottom
                            <div class="card-tooltip">
                                <p>坐标系相对底部的位置</p>
                                <p>可设置固定宽度或者百分比</p>
                            </div>
                        </td>
                        <td>
                            <input id="gridBottom" type="text" name="" value="${optionExpose.gridBottom!}"
                                   class="width-1of1" placeholder="auto"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs option-xy">
                    <tbody>
                            <@ec_e.ec_xy oe=optionExpose showXInverse=type!=27 />
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
                <table class="table table-striped table-hover table-selfs slabel-parameters">
                    <tbody>
                        <@ec_e.ec_series_label oe=optionExpose position=sLabelPositions />
                    </tbody>
                </table>
            </div>
            <#if type != 27>
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs series-parameters">
                    <tbody>
                        <@ec_e.ec_series oe=optionExpose series=optionExpose.exposeSeries types=["bar", "line"] />
                    </tbody>
                </table>
            </div>
            </#if>
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
