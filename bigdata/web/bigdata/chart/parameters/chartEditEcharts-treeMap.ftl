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
            <p>
                面包屑
            </p>

        </div>
        <div class="imgs-choice scroll-height-inner" style="overflow-x: auto;overflow-y: hidden;">
            <div class="litimg">
                <table class="table table-striped table-hover table-selfs option-base">
                    <tbody>
                        <@ec_e.ec_colors colors=optionExpose.colors />
                        <tr>
                            <td class="text-right">展示层级
                                <div class="card-tooltip">
                                    <p>表示展示第几层，更深层级的节点会被隐藏，点击可下钻看到更深层的节点</p>
                                </div>
                            </td>
                            <td>
                                <input id="leafDepth" type="number" name="" value="${optionExpose.leafDepth!}" min="1" class="width-1of1" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">下钻提示符
                                <div class="card-tooltip">
                                    <p>当可以下钻点击时才会出现提示符</p>
                                </div>
                            </td>
                            <td>
                                <input id="drillDownIcon" type="text" name="" value="${optionExpose.drillDownIcon!}" class="width-1of1" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">最小可见节点
                                <div class="card-tooltip">
                                    <p>节点的值小于这个值，这个节点就不显示</p>
                                </div>
                            </td>
                            <td>
                                <input id="visibleMin" type="number" name="" min="0" value="${optionExpose.visibleMin?default(10)}" class="width-1of1" placeholder="10"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">最小可见子节点
                                <div class="card-tooltip">
                                    <p>节点的值小于这个值，这个节点的子节点就不显示</p>
                                </div>
                            </td>
                            <td>
                                <input id="childrenVisibleMin" type="number" name="" min="0" value="${optionExpose.childrenVisibleMin!}" class="width-1of1" placeholder="auto"/>
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
                <table class="table table-striped table-hover table-selfs breadcrumb-parameters">
                    <tbody>
                        <tr>
                            <td class="text-right">显示面包屑

                            </td>
                            <td>
                                <@ec_e.ec_yes_no v=optionExpose.showBreadCrumb?default(true) dv=true id="showBreadCrumb" />
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">高度

                            </td>
                            <td>
                                <input id="height" type="number" name="" min="0" value="${optionExpose.height?default(22)}" class="width-1of1" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">颜色

                            </td>
                            <td>
                                <input id="breadColor" type="text" name="" min="0" value="${optionExpose.breadColor!}" style="background-color: ${optionExpose.breadColor!};" class="iColor width-1of1" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">边框宽度

                            </td>
                            <td>
                                <input id="borderWidth" type="number" name="" min="0" value="${optionExpose.borderWidth?default(1)}" class="width-1of1" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">边框颜色

                            </td>
                            <td>
                                <input id="borderColor" type="text" name="" min="0" value="${optionExpose.borderColor!}" style="background-color: ${optionExpose.borderColor!};" class="width-1of1 iColor" placeholder="auto"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">左右对齐</td>
                            <td>
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix"
                                       id="dLabel4" type="button"
                                       data-toggle="dropdown"
                                       aria-haspopup="true"
                                       aria-expanded="false">
                                    <span id="left" class="choose-text" data-text="${optionExpose.left?default('center')}" data-dt="${OEUtils.doLegendLocation(optionExpose.left?default('center'))}"
                                          v="${optionExpose.left?default('center')}">${OEUtils.doLegendLocation(optionExpose.left?default('center'))}
                                    </span>
                                    <div class="pos-right">
                                        <span class="caret"></span>
                                    </div>
                                    </p>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dLabel4">
                                        <li><a href="#" v="left">左对齐</a></li>
                                        <li><a href="#" v="right">右对齐</a></li>
                                        <li><a href="#" v="center">居中</a></li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">垂直对齐</td>
                            <td>
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix"
                                       id="dLabel4" type="button"
                                       data-toggle="dropdown"
                                       aria-haspopup="true"
                                       aria-expanded="false">
                                    <span id="top" class="choose-text" data-text="${optionExpose.top?default('bottom')}" data-dt="${OEUtils.doLegendLocation(optionExpose.top?default('bottom'))}"
                                          v="${optionExpose.top?default('bottom')}">${OEUtils.doLegendLocation(optionExpose.top?default('bottom'))}
                                    </span>
                                    <div class="pos-right">
                                        <span class="caret"></span>
                                    </div>
                                    </p>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dLabel4">
                                        <li><a href="#" v="top">顶部</a></li>
                                        <li><a href="#" v="bottom">底部</a></li>
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
