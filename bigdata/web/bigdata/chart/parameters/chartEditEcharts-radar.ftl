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
                 指示器
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
                <table class="table table-striped table-hover table-selfs indicator-parameters">
                    <tbody>
                        <tr>
                            <th colspan="2">指示器</th>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <#if optionExpose.exposeIndicators?? && optionExpose.exposeIndicators?size gt 0>
                                <div class="dropdown js-choose">
                                    <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span class="choose-text ict-chosen" data-text="">${optionExpose.exposeIndicators[0].indicatorName!}</span>
                                    <div class="pos-right">
                                        <span class="glyphicon glyphicon-repeat"></span>
                                    </div>
                                    </p>
                                    <ul class="dropdown-menu" aria-labelledby="dLabel3">
                                        <#if optionExpose.exposeIndicators?? && optionExpose.exposeIndicators?size gt 0>
                                            <#list optionExpose.exposeIndicators as ict>
                                                <li><a href="#" v="${ict.indicatorName!}" data-s="${ict.indicatorName!}">${ict.indicatorName!}</a></li>
                                            </#list>
                                        </#if>
                                    </ul>
                                </div>
                                    <#list optionExpose.exposeIndicators as ict>
                                        <div class="form-horizontal <#if ict_index!=0>hidden</#if> ict_${ict.indicatorName!} ict-child" >
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-margin">最大值</label>
                                                <div class="col-sm-10">
                                                    <input data-s="${ict.indicatorName!}" id="indicatorMax" type="text" class="form-control no-margin" value="${ict.indicatorMax!}" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-margin">最小值</label>
                                                <div class="col-sm-10">
                                                    <input data-s="${ict.indicatorName!}" id="indicatorMin" type="text" class="form-control no-margin" value="${ict.indicatorMin?default(0)}" />
                                                </div>
                                            </div>
                                            <#--<div class="form-group">-->
                                                <#--<label class="col-sm-2 control-label no-margin">颜色</label>-->
                                                <#--<div class="col-sm-10">-->
                                                    <#--<input data-s="${ict.name!}" id="indicatorColor" type="text" class="form-control iColor no-margin" value="${ict.indicatorColor!}" />-->
                                                <#--</div>-->
                                            <#--</div>-->
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
