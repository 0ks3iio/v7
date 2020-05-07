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
                布局
            </p>
            <p>
                标题
            </p>
            <p>
                提示框
            </p>
            <p>
                数据标签
            </p>
            <p>
                节点明细
            </p>
            <p>
                连接明细
            </p>
        </div>
        <div class="imgs-choice scroll-height-inner" style="overflow-x: auto;overflow-y: hidden;">
            <div class="litimg">
                <table class="table table-striped table-hover table-selfs option-base">
                    <tbody>
                        <@ec_e.ec_colors colors=optionExpose.colors />
                        <tr>
                            <td class="text-right">
                                图形形状
                            </td>
                            <td>
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix"
                                       id="dLabel4" type="button"
                                       data-toggle="dropdown"
                                       aria-haspopup="true"
                                       aria-expanded="false">
                                        <span id="graphSymbol" class="choose-text" data-text="circle" data-dt="circle"
                                              v="${optionExpose.graphSymbol!}">${optionExpose.graphSymbol!}</span>
                                    </p>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dLabel4">
                                        <li><a href="#" v="pin">pin</a></li>
                                        <li><a href="#" v="circle">circle</a></li>
                                        <li><a href="#" v="arrow">arrow</a></li>
                                        <li><a href="#" v="diamond">diamond</a></li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">
                                图形大小
                            </td>
                            <td>
                                <input id="graphSymbolSize" type="number" name="" min="0" value="${optionExpose.graphSymbolSize?default(10)}" class="width-1of1" placeholder="10"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="litimg none">
                <table class="table table-striped table-hover table-selfs layout-parameters">
                    <tbody>
                        <tr>
                            <th colspan="2">布局</th>
                        </tr>
                        <tr>
                            <td class="text-right">布局方式
                                <div class="card-tooltip">
                                    <p>circular为环形布局force是力引导布局</p>
                                </div>
                            </td>
                            <td>
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix"
                                       id="dLabel4" type="button"
                                       data-toggle="dropdown"
                                       aria-haspopup="true"
                                       aria-expanded="false">
                                        <span id="graphLayout" class="choose-text" data-text="force" data-dt="force"
                                          v="${optionExpose.graphLayout}">${optionExpose.graphLayout}</span>
                                    </p>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dLabel4">
                                        <li><a href="#" v="force">force</a></li>
                                        <li><a href="#" v="circular">circular</a></li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th colspan="2">引力布局明细

                            </th>
                        </tr>
                        <tr>
                            <td class="text-right">斥力因子
                                <div class="card-tooltip">
                                    <p>斥力因子越大线之间的斥力则越大</p>
                                </div>
                            </td>
                            <td>
                                <input id="repulsion" type="number" name="" min="0" value="${optionExpose.force.repulsion!}" class="width-1of1" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">节点间的距离
                                <div class="card-tooltip">
                                    <p>该值越大线越大</p>
                                </div>
                            </td>
                            <td>
                                <input id="edgeLength" type="number" name="" min="0" value="${optionExpose.force.edgeLength!}" class="width-1of1" placeholder="auto"/>
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
                        <th colspan="2">通用</th>
                    </tr>
                    <tr>
                        <td class="text-right">启用数据标签</td>
                        <td>
                            <@ec_e.ec_yes_no v=optionExpose.showSLabel dv=false id="showSLabel" />
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right">文本样式</td>
                        <td>
                            <@ec_e.ec_composite_s fontSize=optionExpose.sLabelTextFontSize fontBold=optionExpose.sLabelTextFontBold fontItalic=optionExpose.sLabelTextFontItalic fontColor=optionExpose.sLabelTextFontColor idPrefix="sLabelText" />
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="litimg none ">
                <table class="table table-striped table-hover table-selfs datacategory-parameters">
                    <tbody>
                    <tr>
                        <th colspan="2">节点明细</th>
                    </tr>
                    <tr>
                        <td colspan="2">
                    <#if optionExpose.dataCategories?? && optionExpose.dataCategories?size gt 1>
                    <div class="dropdown js-choose">
                        <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="choose-text datacategory-chosen" data-text="系列１">${optionExpose.dataCategories[0].dataCategoryName!}</span>
                        </p>
                        <ul class="dropdown-menu" aria-labelledby="dLabel3">
                            <#if optionExpose.dataCategories?? && optionExpose.dataCategories?size gt 0>
                                <#list optionExpose.dataCategories as dataCategory>
                                    <li><a href="#" v="${dataCategory.dataCategoryName!}" data-s="${dataCategory.dataCategoryName!}">${dataCategory.dataCategoryName!}</a></li>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                    </#if>
                    <#if optionExpose.dataCategories?? && optionExpose.dataCategories?size gt 0>
                        <#list optionExpose.dataCategories as dataCategory>
                            <div class="form-horizontal <#if dataCategory_index!=0>hidden</#if> datacategory_${dataCategory.dataCategoryName!} datacategory-child" >
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">节点形状</label>
                                    <div class="col-sm-10">
                                    <div class="dropdown">
                                        <p class="dropdown-self no-margin clearfix"
                                           id="dLabel4" type="button"
                                           data-toggle="dropdown"
                                           aria-haspopup="true"
                                           aria-expanded="false">
                                        <span data-s="${dataCategory.dataCategoryName}" id="dataCategorySymbol" class="choose-text" data-text="circle" data-dt="circle"
                                              v="${dataCategory.dataCategorySymbol!}">${dataCategory.dataCategorySymbol!}
                                        </span>
                                        </p>
                                        <ul class="dropdown-menu"
                                            aria-labelledby="dLabel4">
                                            <li><a href="#" v="pin">pin</a></li>
                                            <li><a href="#" v="circle">circle</a></li>
                                            <li><a href="#" v="arrow">arrow</a></li>
                                            <li><a href="#" v="diamond">diamond</a></li>
                                        </ul>
                                    </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">节点大小</label>
                                    <div class="col-sm-10">
                                        <input data-s="${dataCategory.dataCategoryName}" id="dataCategorySymbolSize" type="number" name="" min="0" value="${dataCategory.dataCategorySymbolSize!}" class="width-1of1" placeholder="auto"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">节点颜色</label>
                                    <div class="col-sm-10">
                                        <input data-s="${dataCategory.dataCategoryName}" id="dataCategoryColor" name="" value="${dataCategory.dataCategoryColor!}" style="background-color: ${dataCategory.dataCategoryColor!};" class="width-1of1 iColor"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">显示节点名称</label>
                                    <div class="col-sm-10">
                                        <div class="dropdown">
                                            <p class="dropdown-self no-margin clearfix"
                                               id="dLabel4" type="button"
                                               data-toggle="dropdown"
                                               aria-haspopup="true"
                                               aria-expanded="false">
                                            <span data-s="${dataCategory.dataCategoryName!}" id="dataCategoryShowLabel" class="choose-text" data-text="false" data-dt="否"
                                                  v="${dataCategory.dataCategoryShowLabel?default(true)?string}"><#if dataCategory.dataCategoryShowLabel?default(true)?string=='false'>否<#else>是</#if></span>
                                            <div class="pos-right">
                                                <span class="caret"></span>
                                            </div>
                                            </p>
                                            <ul class="dropdown-menu"
                                                aria-labelledby="dLabel4">
                                                <li><a href="#" v="true">是</a></li>
                                                <li><a href="#" v="false">否</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">节点字体</label>
                                    <div class="col-sm-10">
                                        <div class="dropdown">
                                            <p class="dropdown-self no-margin clearfix"
                                               id="dLabel3" type="button"
                                               data-toggle="dropdown"
                                               aria-haspopup="true"
                                               aria-expanded="false">
                                                <span data-s="${dataCategory.dataCategoryName}" id="dataCategoryLabelFontSize" class="choose-text" data-text="12" data-dt="12" v="${dataCategory.dataCategoryLabelFontSize!}">
                                                    ${dataCategory.dataCategoryLabelFontSize!}
                                                </span>
                                            <div class="pos-right">
                                                <span data-s="${dataCategory.dataCategoryName}" data-text="false" v="${dataCategory.dataCategoryLabelFontBold?default(false)?string}" id="dataCategoryLabelFontBold"
                                                      class="glyphicon glyphicon-bold ${OEUtils.doActive(dataCategory.dataCategoryLabelFontBold?default(false))}"></span>
                                                <span data-s="${dataCategory.dataCategoryName}" data-text="false" v="${dataCategory.dataCategoryLabelFontItalic?default(false)?string}" id="dataCategoryLabelFontItalic"
                                                      class="glyphicon glyphicon-italic ${OEUtils.doActive(dataCategory.dataCategoryLabelFontItalic?default(false))}"></span>
                                                <span data-s="${dataCategory.dataCategoryName}" data-text="#ffffff" v="${dataCategory.dataCategoryLabelFontColor?default("#ffffff")}" id="dataCategoryLabelFontColor" class="color-choose iColor"
                                                      style="background-color: ${dataCategory.dataCategoryLabelFontColor?default("#ffffff")};"></span>
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
            <div class="litimg none linkcategory-parameters">
                <table class="table table-striped table-hover table-selfs linkcategory-parameters">
                    <tbody>
                    <tr>
                        <th colspan="2">连接明细</th>
                    </tr>
                    <tr>
                        <td colspan="2">
                    <#if optionExpose.linkCategories?? && optionExpose.linkCategories?size gt 1>
                    <div class="dropdown js-choose">
                        <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="choose-text linkcategory-chosen" data-text="系列１">${optionExpose.linkCategories[0].linkCategoryName!}</span>
                        </p>
                        <ul class="dropdown-menu" aria-labelledby="dLabel3">
                            <#if optionExpose.linkCategories?? && optionExpose.linkCategories?size gt 0>
                                <#list optionExpose.linkCategories as linkCategory>
                                    <li><a href="#" v="${linkCategory.linkCategoryName!}" data-s="${linkCategory.linkCategoryName!}">${linkCategory.linkCategoryName!}</a></li>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                    </#if>
                    <#if optionExpose.linkCategories?? && optionExpose.linkCategories?size gt 0>
                        <#list optionExpose.linkCategories as linkCategory>
                            <div class="form-horizontal <#if linkCategory_index!=0>hidden</#if> linkcategory_${linkCategory.linkCategoryName!} linkcategory-child" >
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">线颜色</label>
                                    <div class="col-sm-10">
                                        <input data-s="${linkCategory.linkCategoryName}" id="linkCategoryLineColor" name="" value="${linkCategory.linkCategoryLineColor!}" style="background-color: ${linkCategory.linkCategoryLineColor!};" class="width-1of1 iColor"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">线宽度</label>
                                    <div class="col-sm-10">
                                        <input data-s="${linkCategory.linkCategoryName}" id="linkCategoryLineWidth" name="" value="${linkCategory.linkCategoryLineWidth!}" class="width-1of1" placeholder="auto"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">线样式</label>
                                    <div class="col-sm-10">
                                        <div class="dropdown">
                                            <p class="dropdown-self no-margin clearfix"
                                               id="dLabel4" type="button"
                                               data-toggle="dropdown"
                                               aria-haspopup="true"
                                               aria-expanded="false">
                                            <span data-s="${linkCategory.linkCategoryName!}" id="linkCategoryLineType" class="choose-text" data-text="false" data-dt="否"
                                                  v="${linkCategory.linkCategoryLineType!}">${linkCategory.linkCategoryLineType?default("solid")}</span>
                                            <div class="pos-right">
                                                <span class="caret"></span>
                                            </div>
                                            </p>
                                            <ul class="dropdown-menu"
                                                aria-labelledby="dLabel4">
                                                <li><a href="#" v="solid">solid</a></li>
                                                <li><a href="#" v="dotted">dotted</a></li>
                                                <li><a href="#" v="dashed">dashed</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">显示连接线名称</label>
                                    <div class="col-sm-10">
                                        <div class="dropdown">
                                            <p class="dropdown-self no-margin clearfix"
                                               id="dLabel4" type="button"
                                               data-toggle="dropdown"
                                               aria-haspopup="true"
                                               aria-expanded="false">
                                            <span data-s="${linkCategory.linkCategoryName!}" id="linkCategoryShowLabel" class="choose-text" data-text="false" data-dt="否"
                                                  v="${linkCategory.linkCategoryShowLabel?default(false)?string}"><#if linkCategory.linkCategoryShowLabel?default(false)?string=='false'>否<#else >是</#if></span>
                                            <div class="pos-right">
                                                <span class="caret"></span>
                                            </div>
                                            </p>
                                            <ul class="dropdown-menu"
                                                aria-labelledby="dLabel4">
                                                <li><a href="#" v="true">是</a></li>
                                                <li><a href="#" v="false">否</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label no-margin">连接线字体</label>
                                    <div class="col-sm-10">
                                        <div class="dropdown">
                                            <p class="dropdown-self no-margin clearfix"
                                               id="dLabel3" type="button"
                                               data-toggle="dropdown"
                                               aria-haspopup="true"
                                               aria-expanded="false">
                                                <span data-s="${linkCategory.linkCategoryName}" id="linkCategoryLabelFontSize" class="choose-text" data-text="12" data-dt="12" v="${linkCategory.linkCategoryLabelFontSize?default(12)}">
                                                    ${linkCategory.linkCategoryLabelFontSize?default(12)}
                                                </span>
                                            <div class="pos-right">
                                                <span data-s="${linkCategory.linkCategoryName}" data-text="false" v="${linkCategory.linkCategoryLabelFontBold?default(false)?string}" id="linkCategoryLabelFontBold"
                                                      class="glyphicon glyphicon-bold ${OEUtils.doActive(linkCategory.linkCategoryLabelFontBold?default(false))}"></span>
                                                <span data-s="${linkCategory.linkCategoryName}" data-text="false" v="${linkCategory.linkCategoryLabelFontItalic?default(false)?string}" id="linkCategoryLabelFontItalic"
                                                      class="glyphicon glyphicon-italic ${OEUtils.doActive(linkCategory.linkCategoryLabelFontItalic?default(false))}"></span>
                                                <span data-s="${linkCategory.linkCategoryName}" data-text="#ccc" v="${linkCategory.linkCategoryLabelFontColor!}" id="linkCategoryLabelFontColor" class="color-choose iColor"
                                                      style="background-color: ${linkCategory.linkCategoryLabelFontColor!};"></span>
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
