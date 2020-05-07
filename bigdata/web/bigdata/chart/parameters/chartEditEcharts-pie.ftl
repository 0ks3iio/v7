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
            <p class="">
                标题
            </p>
            <p>
                位置和直径
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
                <table class="table table-striped table-hover table-selfs option-base">
                    <tbody>
                        <@ec_e.ec_pie_cr crs=optionExpose.exposeCrs />
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
                <table class="table table-striped table-hover table-selfs slabel-parameters">
                    <tbody>
                    <tr>
                        <th colspan="2">数据标签</th>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <#if optionExpose.exposeLabels?? && optionExpose.exposeLabels?size gt 1>
                            <div class="dropdown js-choose">
                                <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <span class="choose-text pielabel-chosen" data-text="数据列1">${optionExpose.exposeLabels[0].name!}</span>
                                <div class="pos-right">
                                    <span class="glyphicon glyphicon-repeat"></span>
                                </div>
                                </p>
                                <ul class="dropdown-menu" aria-labelledby="dLabel3">
                                    <#if optionExpose.exposeLabels?? && optionExpose.exposeLabels?size gt 0>
                                        <#list optionExpose.exposeLabels as s>
                                            <li><a href="#" v="${s.name!}" data-s="${s.name!}">${s.name!}</a></li>
                                        </#list>
                                    </#if>
                                </ul>
                            </div>
                            </#if>
                            <#if optionExpose.exposeLabels?? && optionExpose.exposeLabels?size gt 0>
                                <#list optionExpose.exposeLabels as el>
                                    <div class="form-horizontal <#if el_index!=0>hidden</#if> pielabel_${el.name!} pielabel-child" id="myForm">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">启用数据标签</label>
                                            <div class="col-sm-10">
                                                <@ec_e.ec_yes_no v=el.showSLabel?default(false) dv=true id="showSLabel" s=el.name />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">标签位置</label>
                                            <div class="col-sm-10">
                                                <div class="dropdown">
                                                    <p class="dropdown-self no-margin clearfix" id="dLabel4" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                        <span data-s="${el.name!}" id="sLabelPosition" class="choose-text" data-text="inside" data-dt="内部" v="${el.sLabelPosition!}">${OEUtils.doSLabelPosition(el.sLabelPosition)}</span>
                                                    <div class="pos-right">
                                                        <span class="caret"></span>
                                                    </div>
                                                    </p>
                                                    <ul class="dropdown-menu" aria-labelledby="dLabel4">
                                                        <li><a href="#" v="outside">${OEUtils.doSLabelPosition("outside")}</a></li>
                                                        <li><a href="#" v="inside">${OEUtils.doSLabelPosition("inside")}</a></li>
                                                        <li><a href="#" v="center">${OEUtils.doSLabelPosition("center")}</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">标签文本样式</label>
                                            <div class="col-sm-10">
                                                <@ec_e.ec_composite_s fontSize=el.sLabelTextFontSize fontBold=el.sLabelTextFontBold fontItalic=el.sLabelTextFontItalic fontColor=el.sLabelTextFontColor idPrefix="sLabelText" s=el.name />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">启用标签引导线</label>
                                            <div class="col-sm-10">
                                                <@ec_e.ec_yes_no v=el.showPieLabelLine?default(false) dv=true id="showPieLabelLine" s=el.name />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">引导线样式</label>
                                            <div class="col-sm-10">
                                                <div class="dropdown">
                                                    <p class="dropdown-self no-margin clearfix" id="dLabel4" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                        <span data-s="${el.name!}" id="pieLabelLineType" class="choose-text" data-text="inside" data-dt="内部" v="${el.pieLabelLineType!}">${el.pieLabelLineType}</span>
                                                    <div class="pos-right">
                                                        <span class="caret"></span>
                                                    </div>
                                                    </p>
                                                    <ul class="dropdown-menu" aria-labelledby="dLabel4">
                                                        <li><a href="#" v="solid">solid</a></li>
                                                        <li><a href="#" v="dashed">dashed</a></li>
                                                        <li><a href="#" v="dotted">dotted</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">引导线宽度</label>
                                            <div class="col-sm-10">
                                                <input data-s="${el.name!}" type="number" min="1" name="" id="pieLabelLineWidth" value="${optionExpose.pieLabelLineWidth!}" class="width-1of1"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-margin">引导线光滑度</label>
                                            <div class="col-sm-10">
                                                <input data-s="${el.name!}" type="number" min="0" max="10" name="" id="pieLabelLineSmooth" value="${optionExpose.pieLabelLineSmooth!}" class="width-1of1"/>
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
