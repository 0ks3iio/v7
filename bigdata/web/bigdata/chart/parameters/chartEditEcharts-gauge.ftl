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
            <p>
                标题
            </p>
            <p>
                位置和半径
            </p>
            <p>
                样式设置
            </p>
        </div>
        <div class="imgs-choice scroll-height-inner" style="overflow-x: auto;overflow-y: hidden;">
            <#--<div class="litimg">-->
                <#--<table class="table table-striped  table-hover table-selfs option-base">-->
                    <#--<tbody>-->
                        <#--<@ec_e.ec_colors colors=optionExpose.colors />-->
                    <#--</tbody>-->
                <#--</table>-->
            <#--</div>-->
                <div class="litimg title-parameters">
                    <table class="table table-striped table-hover table-selfs">
                        <tbody>
                        <@ec_e.ec_title oe=optionExpose.exposeTitle! />
                        </tbody>
                    </table>
                </div>
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs tooltip-parameters">
                    <tbody>
                    <tr>
                        <th colspan="2">CR</th>
                    </tr>
                    <tr>
                        <td colspan="2">
                        <#if optionExpose.crs?? && optionExpose.crs?size gt 1>
                        <div class="dropdown js-choose">
                            <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="choose-text gaugecrs-chosen" data-text="数据列1">${optionExpose.crs[0].name!}</span>
                            <div class="pos-right">
                                <span class="glyphicon glyphicon-repeat"></span>
                            </div>
                            </p>
                            <ul class="dropdown-menu" aria-labelledby="dLabel3">
                                <#if optionExpose.crs?? && optionExpose.crs?size gt 0>
                                    <#list optionExpose.crs as s>
                                        <li><a href="#" v="${s.name!}" data-s="${s.name!}">${s.name!}</a></li>
                                    </#list>
                                </#if>
                            </ul>
                        </div>
                        </#if>
                        <#if optionExpose.crs?? && optionExpose.crs?size gt 0>
                            <#list optionExpose.crs as cr>
                                <div class="form-horizontal <#if cr_index!=0>hidden</#if> gaugecrs_${cr.name!} gaugecrs-child" >
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">中心</label>
                                        <div class="col-sm-10">
                                            <input data-s="${cr.name!}"  id="gaugeCenter" type="text"
                                                   class="form-control no-margin"
                                                   value="${cr.gaugeCenter!}"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">半径</label>
                                        <div class="col-sm-10">
                                            <input data-s="${cr.name!}" id="gaugeRadius" type="text" class="form-control no-margin" value="${cr.gaugeRadius!}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">开始角度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${cr.name!}" id="gaugeStartAngle" type="number" class="form-control no-margin" value="${cr.gaugeStartAngle?default(225)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">结束角度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${cr.name!}" id="gaugeEndAngle" type="number" class="form-control no-margin" value="${cr.gaugeEndAngle?default(-45)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">最大值</label>
                                        <div class="col-sm-10">
                                            <input data-s="${cr.name!}" id="gaugeMax" type="number" class="form-control no-margin" value="${cr.gaugeMax?default(100)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">最小值</label>
                                        <div class="col-sm-10">
                                            <input data-s="${cr.name!}" id="gaugeMin" type="number" class="form-control no-margin" value="${cr.gaugeMin?default(0)}" />
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </#if>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs slabel-parameters">
                    <tbody>
                    <tr>
                        <td colspan="2">
                        <#if optionExpose.gaugeStyles?? && optionExpose.gaugeStyles?size gt 1>
                        <div class="dropdown js-choose">
                            <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="choose-text gaugestyle-chosen" data-text="数据列1">${optionExpose.crs[0].name!}</span>
                            <div class="pos-right">
                                <span class="glyphicon glyphicon-repeat"></span>
                            </div>
                            </p>
                            <ul class="dropdown-menu" aria-labelledby="dLabel3">
                                <#if optionExpose.gaugeStyles?? && optionExpose.gaugeStyles?size gt 0>
                                    <#list optionExpose.gaugeStyles as s>
                                        <li><a href="#" v="${s.name!}" data-s="${s.name!}">${s.name!}</a></li>
                                    </#list>
                                </#if>
                            </ul>
                        </div>
                        </#if>
                        <#if optionExpose.gaugeStyles?? && optionExpose.gaugeStyles?size gt 0>
                            <#list optionExpose.gaugeStyles as gs>
                                <div class="form-horizontal <#if gs_index!=0>hidden</#if> gaugestyle_${gs.name!} gaugestyle-child" >
                                    <#--<div class="form-group">-->
                                        <#--<label class="col-sm-2 control-label no-margin">显示仪表盘轴线</label>-->
                                        <#--<div class="col-sm-10">-->
                                            <#--<@ec_e.ec_yes_no v=gs.showGaugeAxisLine dv=true id="showGaugeAxisLine" s=gs.name />-->
                                        <#--</div>-->
                                    <#--</div>-->
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">轴线宽度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeAxisLineWidth" type="number" min="8" class="form-control no-margin" value="${gs.gaugeAxisLineWidth?default(30)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">显示分隔线</label>
                                        <div class="col-sm-10">
                                            <@ec_e.ec_yes_no v=gs.showGaugeSplitLine dv=true id="showGaugeSplitLine" s=gs.name />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">分隔线长度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeSplitLineLength" type="number" min="0" class="form-control no-margin" value="${gs.gaugeSplitLineLength?default(30)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">分隔线宽度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeSplitLineWidth" type="number" min="0" class="form-control no-margin" value="${gs.gaugeSplitLineWidth?default(2)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">显示刻度</label>
                                        <div class="col-sm-10">
                                            <@ec_e.ec_yes_no v=gs.showGaugeAxisTick dv=true id="showGaugeAxisTick" s=gs.name />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">分隔线之前的刻度数</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeAxisTickSplitNumber" type="number" min="0" class="form-control no-margin" value="${gs.gaugeAxisTickSplitNumber?default(5)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">刻度线长</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeAxisTickLength" type="number" min="0" class="form-control no-margin" value="${gs.gaugeAxisTickLength?default(8)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">刻度线宽</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeAxisTickWidth" type="number" min="0" class="form-control no-margin" value="${gs.gaugeAxisTickWidth?default(1)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">显示刻度标签</label>
                                        <div class="col-sm-10">
                                            <@ec_e.ec_yes_no v=gs.showGaugeAxisLabel dv=true id="showGaugeAxisLabel" s=gs.name />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">标签到刻度线的距离</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeAxisLabelDistance" type="number" min="0" class="form-control no-margin" value="${gs.gaugeAxisLabelDistance?default(5)}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">刻度标签字体</label>
                                        <div class="col-sm-10">
                                            <div class="dropdown">
                                                <p class="dropdown-self no-margin clearfix"
                                                   id="dLabel3" type="button"
                                                   data-toggle="dropdown"
                                                   aria-haspopup="true"
                                                   aria-expanded="false">
                                                    <span data-s="${gs.name!}" id="gaugeAxisLabelFontSize" class="choose-text" data-text="12" data-dt="12" v="${gs.gaugeAxisLabelFontSize?default(12)}">
                                                        ${gs.gaugeAxisLabelFontSize?default(12)}
                                                    </span>
                                                <div class="pos-right">
                                                    <span data-s="${gs.name!}" data-text="false" v="${gs.gaugeAxisLabelFontBold?default(false)?string}" id="gaugeAxisLabelFontBold"
                                                          class="glyphicon glyphicon-bold ${OEUtils.doActive(gs.gaugeAxisLabelFontBold?default(false))}"></span>
                                                    <span data-s="${gs.name!}" data-text="false" v="${fontItalic?default(false)?string}" id="gaugeAxisLabelFontItalic"
                                                          class="glyphicon glyphicon-italic ${OEUtils.doActive(gs.gaugeAxisLabelFontItalic?default(false))}"></span>
                                                    <span class="glyphicon glyphicon-repeat"></span>
                                                </div>
                                                </p>
                                                <ul class="dropdown-menu"
                                                    aria-labelledby="dLabel3">
                                                    <li><a href="#" v="8">8</a></li>
                                                    <li><a href="#" v="10">10</a></li>
                                                    <li><a href="#" v="12">12</a></li>
                                                    <li><a href="#" v="14">14</a></li>
                                                    <li><a href="#" v="16">16</a></li>
                                                    <li><a href="#" v="18">18</a></li>
                                                    <li><a href="#" v="20">20</a></li>
                                                    <li><a href="#" v="22">22</a></li>
                                                    <li><a href="#" v="24">24</a></li>
                                                    <li><a href="#" v="30">30</a></li>
                                                    <li><a href="#" v="26">36</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">显示仪表盘指针</label>
                                        <div class="col-sm-10">
                                            <@ec_e.ec_yes_no v=gs.showGaugePointer dv=true id="showGaugePointer" s=gs.name />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">指针长度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugePointerLength" type="text" class="form-control no-margin" value="${gs.gaugePointerLength?default("80%")}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">指针宽度</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugePointerWidth" type="number" min="0" class="form-control no-margin" value="${gs.gaugePointerWidth?default("8")}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">显示仪表盘标题</label>
                                        <div class="col-sm-10">
                                            <@ec_e.ec_yes_no v=gs.showGaugeTitle dv=true id="showGaugeTitle" s=gs.name />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">仪表盘标题偏移量</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeTitleOffsetCenter" type="text" class="form-control no-margin" value="${gs.gaugeTitleOffsetCenter?default("[0, '-40%']")}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">仪表盘标题字体</label>
                                        <div class="col-sm-10">
                                            <div class="dropdown">
                                                <p class="dropdown-self no-margin clearfix"
                                                   id="dLabel3" type="button"
                                                   data-toggle="dropdown"
                                                   aria-haspopup="true"
                                                   aria-expanded="false">
                                                    <span data-s="${gs.name!}" id="gaugeTitleFontSize" class="choose-text" data-text="12" data-dt="12" v="${gs.gaugeTitleFontSize?default(12)}">
                                                        ${gs.gaugeTitleFontSize?default(12)}
                                                    </span>
                                                <div class="pos-right">
                                                    <span data-s="${gs.name!}" data-text="false" v="${gs.gaugeTitleFontBold?default(false)?string}" id="gaugeTitleFontBold"
                                                          class="glyphicon glyphicon-bold ${OEUtils.doActive(gs.gaugeTitleFontBold?default(false))}"></span>
                                                    <span data-s="${gs.name!}" data-text="false" v="${fontItalic?default(false)?string}" id="gaugeTitleFontItalic"
                                                          class="glyphicon glyphicon-italic ${OEUtils.doActive(gs.gaugeTitleFontItalic?default(false))}"></span>
                                                    <span data-s="${gs.name!}" data-text="#ccc" v="${gs.gaugeTitleFontColor!}" id="gaugeTitleFontColor" class="color-choose iColor"
                                                      style="background-color: ${gs.gaugeTitleFontColor!};"></span>
                                                    <span class="glyphicon glyphicon-repeat"></span>
                                                </div>
                                                </p>
                                                <ul class="dropdown-menu"
                                                    aria-labelledby="dLabel3">
                                                    <li><a href="#" v="8">8</a></li>
                                                    <li><a href="#" v="10">10</a></li>
                                                    <li><a href="#" v="12">12</a></li>
                                                    <li><a href="#" v="14">14</a></li>
                                                    <li><a href="#" v="16">16</a></li>
                                                    <li><a href="#" v="18">18</a></li>
                                                    <li><a href="#" v="20">20</a></li>
                                                    <li><a href="#" v="22">22</a></li>
                                                    <li><a href="#" v="24">24</a></li>
                                                    <li><a href="#" v="30">30</a></li>
                                                    <li><a href="#" v="26">36</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">显示仪明细</label>
                                        <div class="col-sm-10">
                                            <@ec_e.ec_yes_no v=gs.showGaugeDetail dv=true id="showGaugeDetail" s=gs.name />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">仪表盘明细偏移量</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeDetailOffsetCenter" type="text" class="form-control no-margin" value="${gs.gaugeDetailOffsetCenter?default("[0, '40%']")}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">仪表盘明细格式化</label>
                                        <div class="col-sm-10">
                                            <input data-s="${gs.name!}" id="gaugeDetailFormatter" type="text" class="form-control no-margin" value="${gs.gaugeDetailFormatter!}" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-margin">仪表盘明细字体</label>
                                        <div class="col-sm-10">
                                            <div class="dropdown">
                                                <p class="dropdown-self no-margin clearfix"
                                                   id="dLabel3" type="button"
                                                   data-toggle="dropdown"
                                                   aria-haspopup="true"
                                                   aria-expanded="false">
                                                    <span data-s="${gs.name!}" id="gaugeDetailFontSize" class="choose-text" data-text="15" data-dt="15" v="${gs.gaugeDetailFontSize?default(15)}">
                                                        ${gs.gaugeDetailFontSize?default(15)}
                                                    </span>
                                                <div class="pos-right">
                                                    <span data-s="${gs.name!}" data-text="false" v="${gs.gaugeDetailFontBold?default(false)?string}" id="gaugeDetailFontBold"
                                                          class="glyphicon glyphicon-bold ${OEUtils.doActive(gs.gaugeDetailFontBold?default(false))}"></span>
                                                    <span data-s="${gs.name!}" data-text="false" v="${fontItalic?default(false)?string}" id="gaugeDetailFontItalic"
                                                          class="glyphicon glyphicon-italic ${OEUtils.doActive(gs.gaugeDetailFontItalic?default(false))}"></span>
                                                    <span class="glyphicon glyphicon-repeat"></span>
                                                </div>
                                                </p>
                                                <ul class="dropdown-menu"
                                                    aria-labelledby="dLabel3">
                                                    <li><a href="#" v="8">8</a></li>
                                                    <li><a href="#" v="10">10</a></li>
                                                    <li><a href="#" v="12">12</a></li>
                                                    <li><a href="#" v="14">14</a></li>
                                                    <li><a href="#" v="16">16</a></li>
                                                    <li><a href="#" v="18">18</a></li>
                                                    <li><a href="#" v="20">20</a></li>
                                                    <li><a href="#" v="22">22</a></li>
                                                    <li><a href="#" v="24">24</a></li>
                                                    <li><a href="#" v="30">30</a></li>
                                                    <li><a href="#" v="26">36</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </#if>
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
