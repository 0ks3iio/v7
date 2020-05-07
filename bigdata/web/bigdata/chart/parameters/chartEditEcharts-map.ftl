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
                通用
            </p>
            <p>
                标题
            </p>
        </div>
        <div class="imgs-choice scroll-height-inner" style="overflow-x: auto;overflow-y: hidden;">
            <div class="litimg mapcommon-parameters">
                <table class="table table-striped table-hover table-selfs">
                    <tbody>
                        <tr>
                            <th colspan="2">通用</th>
                        </tr>
                        <tr>
                            <td class="text-right">地区名称</td>
                            <td>
                                <@ec_e.ec_yes_no v=optionExpose.showGeoLabel?default(true) dv=true id="showGeoLabel" />
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">气泡</td>
                            <td>
                                <@ec_e.ec_yes_no v=optionExpose.showScatter?default(false) dv=false id="showScatter" />
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">气泡颜色

                            </td>
                            <td>
                                <input id="scatterColor" type="text"
                                       class="form-control iColor no-margin"
                                       value="${optionExpose.scatterColor!}" style="background-color: ${optionExpose.scatterColor!}" />
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">气泡大小

                            </td>
                            <td>
                                <input <#if !optionExpose.showScatter?default(false)>disabled="disabled"</#if> id="scatterSize" type="number" min="10" value="${optionExpose.scatterSize?default(10)}" class="width-1of1" placeholder="auto"/>
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