<#---
    约定：
        1、对于echarts的所有参数，所有标签上data-text表示默认值
        2、对input标签使用value表示当前值
        3、其他的标签则使用v 标签当前值

-->


<#macro ec_yes_no v dv id s="">
    <div class="dropdown">
        <p class="dropdown-self no-margin clearfix"
           id="dLabel4" type="button"
           data-toggle="dropdown"
           aria-haspopup="true"
           aria-expanded="false">
            <span data-s="${s!}" id="${id!}" class="choose-text" data-text="${dv?string}" data-dt="${OEUtils.doYesNo(dv)}"
                  v="${v?default(false)?string}">${OEUtils.doYesNo(v?default(false))}</span>
        <div class="pos-right">
            <span class="caret"></span>
        </div>
        </p>
        <ul class="dropdown-menu"
            aria-labelledby="dLabel4">
            <li><a href="javascript:void(0);" v="true">是</a></li>
            <li><a href="javascript:void(0);" v="false">否</a></li>
        </ul>
    </div>
</#macro>
<#macro ec_yes_no_color v dv id color dcolor colorId>
    <div class="dropdown">
        <p class="dropdown-self no-margin clearfix"
           id="dLabel4" type="button"
           data-toggle="dropdown"
           aria-haspopup="true"
           aria-expanded="false">
            <span id="${id!}" class="choose-text" data-text="${dv?string}" data-dt="${OEUtils.doYesNo(dv)}"
                  v="${v?default(false)?string}">${OEUtils.doYesNo(v?default(false))}</span>
        <div class="pos-right">
            <span data-text="${dcolor?default('#ccc')}" data-dt="${dcolor?default('#ccc')}"   v="${color!}" id="${colorId}" class="color-choose iColor"
                  style="background-color: ${color!};"></span>
            <span class="iconfont icon-refresh-fill"></span>
        </div>
        </p>
        <ul class="dropdown-menu"
            aria-labelledby="dLabel4">
            <li><a href="javascript:void(0);" v="true">是</a></li>
            <li><a href="javascript:void(0);" v="false">否</a></li>
        </ul>
    </div>
</#macro>

<#macro ec_composite_s fontSize fontBold fontItalic fontColor idPrefix s="">
    <div class="dropdown">
        <p class="dropdown-self no-margin clearfix"
           id="dLabel3" type="button"
           data-toggle="dropdown"
           aria-haspopup="true"
           aria-expanded="false">
            <span data-s="${s!}" id="${idPrefix}FontSize" class="choose-text" data-text="12" data-dt="12" v="${fontSize?default(12)}">
                ${fontSize?default(12)}
            </span>
        <div class="pos-right">
            <span data-s="${s!}" data-text="false" v="${fontBold?default(false)?string}" id="${idPrefix}FontBold"
                  class="iconfont icon-bold-fill ${OEUtils.doActive(fontBold?default(false))}"></span>
            <span data-s="${s!}" data-text="false" v="${fontItalic?default(false)?string}" id="${idPrefix}FontItalic"
                  class="iconfont icon-italics-fill ${OEUtils.doActive(fontItalic?default(false))}"></span>
            <span data-s="${s!}" data-text="#ccc" v="${fontColor!}" id="${idPrefix}FontColor" class="color-choose iColor"
                  style="background-color: ${fontColor!};"></span>
            <span class="iconfont icon-refresh-fill"></span>
        </div>
        </p>
        <ul class="dropdown-menu"
            aria-labelledby="dLabel3">
            <li><a href="javascript:void(0);" v="8">8</a></li>
            <li><a href="javascript:void(0);" v="10">10</a></li>
            <li><a href="javascript:void(0);" v="12">12</a></li>
            <li><a href="javascript:void(0);" v="14">14</a></li>
            <li><a href="javascript:void(0);" v="16">16</a></li>
            <li><a href="javascript:void(0);" v="18">18</a></li>
            <li><a href="javascript:void(0);" v="20">20</a></li>
        </ul>
    </div>
</#macro>

<#macro ec_colors colors>
    <tr>
        <th colspan="2">系列颜色</th>
    </tr>
    <tr>
        <td class="text-right">颜色</td>
        <td>
            <form class="color-set">
                <div class="form-group js-color-group-default" style="display: none;">
                    <#if colors?? && colors?size gt 0>
                        <#list colors as  color>
                            <div class="color-wrap">
                                <input index="${color_index}" type="text"
                                       class="form-control iColor"
                                       value="${color}" style="background-color: ${color};"/>
                                <a class="color-red" href="javascript:void(0);"><i class="fa fa-trash"></i></a>
                            </div>
                        </#list>
                    </#if>
                </div>
                <div class="form-group js-color-group">
                    <#if colors?? && colors?size gt 0>
                        <#list colors as  color>
                            <div class="color-wrap">
                                <input index="${color_index}" type="text"
                                       class="form-control iColor"
                                       value="${color}" style="background-color: ${color};"/>
                                <a class="color-red" href="javascript:void(0);"><i class="fa fa-trash"></i></a>
                            </div>
                        </#list>
                    </#if>
                </div>
                <div class="text-right">
                    <button type="button"
                            class="btn btn-default js-add-input">
                        +
                    </button>
                </div>
            </form>
        </td>
    </tr>
</#macro>

<#macro ec_xy oe idPrefixName="" showXInverse=true>
    <tr>
        <th colspan="2">轴设置</th>
    </tr>
    <tr>
        <td class="text-right">交换轴</td>
        <td>
            <@ec_yes_no v=oe.exchangeXY?default(false) dv=false id="${idPrefixName}exchangeXY" />
        </td>
    </tr>
    <tr>
        <th colspan="2">X轴</th>
    </tr>
    <tr>
        <td class="text-right">显示X轴</td>
        <td>
            <@ec_yes_no v=oe.showX?default(true) dv=true id="${idPrefixName}showX" />
        </td>
    </tr>
    <tr>
        <td class="text-right">Z
            <div class="card-tooltip">
                <p>Z值大的会覆盖Z值小的，这个在标签内侧显示的时候特别有用</p>
            </div>
        </td>
        <td>
            <input type="number" min="0" name="" id="${idPrefixName}xZ" value="${oe.xZ?default(0)}"
                   class="width-1of1"
                   placeholder=""/>
        </td>
    </tr>
    <tr>
        <td class="text-right">轴标题</td>
        <td>
            <input type="" name="" id="${idPrefixName}xTitle" value="${oe.xTitle?default('')}"
                   class="width-1of1"
                   placeholder="请输入标题"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">轴标题样式</td>
        <td>
            <@ec_composite_s fontSize=oe.xTitleFontSize fontBold=oe.xTitleFontBold fontItalic=oe.xTitleFontItalic fontColor=oe.xTitleFontColor idPrefix=idPrefixName+"xTitle"></@ec_composite_s>
        </td>
    </tr>
    <tr>
        <td class="text-right">对侧显示</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix"
                   id="dLabel4" type="button"
                   data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="false">
                    <span id="${idPrefixName}xPosition" class="choose-text"
                          data-text="bottom" v="${oe.xPosition}">${OEUtils.doXYLocation(oe.xPosition)}</span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel4">
                    <li><a href="javascript:void(0);" v="top">是</a></li>
                    <li><a href="javascript:void(0);" v="bottom">否</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <#if showXInverse>
    <tr>
        <td class="text-right">反序显示</td>
        <td>
            <@ec_yes_no v=oe.xInverse dv=false id="${idPrefixName}xInverse"></@ec_yes_no>
        </td>
    </tr>
    </#if>
    <#--<tr>-->
        <#--<td class="text-right">最大值</td>-->
        <#--<td>-->
            <#--<input id="${idPrefixName}xMax" type="number"-->
                   <#--class="form-control no-margin"-->
                   <#--value="${oe.xMax!}"/>-->
        <#--</td>-->
    <#--</tr>-->
    <#--<tr>-->
        <#--<td class="text-right">最小值</td>-->
        <#--<td>-->
            <#--<input id="${idPrefixName}xMin" type="number"-->
                   <#--class="form-control no-margin"-->
                   <#--value="${oe.xMin!}"/>-->
        <#--</td>-->
    <#--</tr>-->
    <tr>
        <td class="text-right">显示X轴线</td>
        <td>
            <@ec_yes_no_color v=oe.showXLine dv=true id=idPrefixName+"showXLine" color=oe.xLineColor dcolor="#ccc" colorId="xLineColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">显示X轴在坐标系中的分隔线</td>
        <td>
            <@ec_yes_no_color v=oe.showXSplitLine dv=false id=idPrefixName+"showXSplitLine" color="${oe.xSplitLineColor!}" dcolor="#ccc" colorId="xSplitLineColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">显示X轴刻度</td>
        <td>
            <@ec_yes_no_color v=oe.showXTick dv=true id=idPrefixName+"showXTick" color=oe.xTickColor dcolor="#ccc" colorId="xTickColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">显示X轴刻度标签</td>
        <td>
            <@ec_yes_no_color v=oe.showXLabel dv=true id=idPrefixName+"showXLabel" color=oe.xLabelColor dcolor="#ccc" colorId="xLabelColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">标签内侧显示</td>
        <td>
            <@ec_yes_no v=oe.xLabelInside?default(false) dv=false id=idPrefixName+"xLabelInside" />
        </td>
    </tr>
    <tr>
        <td class="text-right">标签旋转角度</td>
        <td>
            <input type="number" min="-90" max="90" name="" id="${idPrefixName}xLabelRotate" value="${oe.xLabelRotate?default(0)}"
                   class="width-1of1"
                   placeholder=""/>
        </td>
    </tr>
    <tr>
        <td class="text-right">标签间隔</td>
        <td>
            <input type="number" min="0" name="" id="${idPrefixName}xLabelInterval" value="${oe.xLabelInterval!}"
                   class="width-1of1"
                   placeholder="auto"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">标签距离坐标轴间距</td>
        <td>
            <input type="number" min="0" name="" id="${idPrefixName}xLabelMargin" value="${oe.xLabelMargin?default(8)}"
                   class="width-1of1"
                   placeholder=""/>
        </td>
    </tr>
    <tr>
        <th colspan="2">Y轴</th>
    </tr>
    <tr>
        <td class="text-right">显示Y轴</td>
        <td>
            <@ec_yes_no v=oe.showY?default(true) dv=true id=idPrefixName+"showY" />
        </td>
    </tr>
    <tr>
        <td class="text-right">Z
            <div class="card-tooltip">
                <p>Z值大的会覆盖Z值小的，这个在标签内侧显示的时候特别有用</p>
            </div>
        </td>
        <td>
            <input type="number" min="0" name="" id="${idPrefixName}yZ" value="${oe.yZ?default(0)}"
                   class="width-1of1"
                   placeholder=""/>
        </td>
    </tr>
    <tr>
        <td class="text-right">轴标题</td>
        <td>
            <input type="" name="" id="${idPrefixName}yTitle" value="${oe.yTitle!}"
                   class="width-1of1"
                   placeholder="请输入标题"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">轴标题样式</td>
        <td>
            <@ec_composite_s fontSize=oe.yTitleFontSize fontBold=oe.yTitleFontBold fontItalic=oe.yTitleFontItalic fontColor=oe.yTitleFontColor idPrefix=idPrefixName+"yTitle"></@ec_composite_s>
        </td>
    </tr>
    <tr>
        <td class="text-right">对侧显示</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix"
                   id="dLabel4" type="button"
                   data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="false">
                    <span id="${idPrefixName}yPosition" class="choose-text"
                          data-text="left" v="${oe.yPosition}">${OEUtils.doXYLocation(oe.yPosition)}</span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel4">
                    <li><a href="javascript:void(0);" v="right">是</a></li>
                    <li><a href="javascript:void(0);" v="left">否</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <td class="text-right">反序显示</td>
        <td>
            <@ec_yes_no v=oe.yInverse dv=true id=idPrefixName+"yInverse"></@ec_yes_no>
        </td>
    </tr>
    <#--<tr>-->
        <#--<td class="text-right">最大值</td>-->
        <#--<td>-->
            <#--<input id="${idPrefixName}yMax" type="number"-->
                   <#--class="form-control no-margin"-->
                   <#--value="${oe.yMax!}"/>-->
        <#--</td>-->
    <#--</tr>-->
    <#--<tr>-->
        <#--<td class="text-right">最小值</td>-->
        <#--<td>-->
            <#--<input id="${idPrefixName}yMin" type="number"-->
                   <#--class="form-control no-margin"-->
                   <#--value="${oe.yMin!}"/>-->
        <#--</td>-->
    <#--</tr>-->
    <tr>
        <td class="text-right">显示Y轴线</td>
        <td>
            <@ec_yes_no_color v=oe.showYLine dv=true id=idPrefixName+"showYLine" color=oe.yLineColor dcolor="#ccc" colorId="yLineColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">显示Y轴在坐标系中的分隔线</td>
        <td>
            <@ec_yes_no_color v=oe.showYSplitLine dv=false id=idPrefixName+"showYSplitLine" color="${oe.ySplitLineColor!}" dcolor="#ccc" colorId="ySplitLineColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">显示Y轴刻度</td>
        <td>
            <@ec_yes_no_color v=oe.showYTick dv=true id=idPrefixName+"showYTick" color=oe.yTickColor dcolor="#ccc" colorId="yTickColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">显示Y轴刻度标签</td>
        <td>
            <@ec_yes_no_color v=oe.showYLabel dv=true id=idPrefixName+"showYLabel" color=oe.yLabelColor dcolor="#ccc" colorId="yLabelColor" />
        </td>
    </tr>
    <tr>
        <td class="text-right">标签内侧显示</td>
        <td>
            <@ec_yes_no v=oe.yLabelInside?default(false) dv=false id=idPrefixName+"yLabelInside" />
        </td>
    </tr>
    <tr>
        <td class="text-right">标签旋转角度</td>
        <td>
            <input type="number" min="-90" max="90" name="" id="${idPrefixName}yLabelRotate" value="${oe.yLabelRotate?default(0)}"
                   class="width-1of1"
                   placeholder=""/>
        </td>
    </tr>
    <tr>
        <td class="text-right">标签间隔
            <div class="card-tooltip">
                <p>当您交换了坐标轴之后为了保证Y轴上的标签可以全部显示您务必将该参数设置为0</p>
            </div>
        </td>
        <td>
            <input type="number" min="0" name="" id="${idPrefixName}yLabelInterval" value="${oe.yLabelInterval!}"
                   class="width-1of1"
                   placeholder="auto"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">标签距离坐标轴间距</td>
        <td>
            <input type="number"  name="" id="${idPrefixName}yLabelMargin" value="${oe.yLabelMargin?default(8)}"
                   class="width-1of1"
                   placeholder=""/>
        </td>
    </tr>
</#macro>
<#macro ec_series_label oe position>
    <tr>
        <th colspan="2">通用</th>
    </tr>
    <tr>
        <td class="text-right">启用数据标签</td>
        <td>
            <@ec_yes_no v=oe.showSLabel dv=false id="showSLabel" />
        </td>
    </tr>
    <tr>
        <td class="text-right">位置</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix" id="dLabel4" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span id="sLabelPosition" class="choose-text" data-text="inside" data-dt="内部" v="${oe.sLabelPosition!}">${OEUtils.doSLabelPosition(oe.sLabelPosition)}</span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu" aria-labelledby="dLabel4">
                    <#if position?? && position?size gt 0>
                        <#list position as ps>
                            <li><a href="javascript:void(0);" v="${ps}">${OEUtils.doSLabelPosition(ps)}</a></li>
                        </#list>
                    </#if>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <th colspan="2">边框及颜色</th>
    </tr>
    <tr>
        <td class="text-right">文本样式</td>
        <td>
            <@ec_composite_s fontSize=oe.sLabelTextFontSize fontBold=oe.sLabelTextFontBold fontItalic=oe.sLabelTextFontItalic fontColor=oe.sLabelTextFontColor idPrefix="sLabelText" />
        </td>
    </tr>
    <tr>
        <td class="text-right">背景颜色</td>
        <td>
            <input id="sLabelBackgroundColor" type="text" name="" value="${oe.sLabelBackgroundColor!}" style="background-color: ${oe.sLabelBackgroundColor!};" class="width-1of1 iColor" placeholder="auto"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">背景不透明度
            <div class="card-tooltip">
                <p>数值越大透明度越低</p>
            </div>
        </td>
        <td>
            <input id="sLabelBackgroundColorTransparent" type="number" min="0" max="10" name="" value="${oe.sLabelBackgroundColorTransparent!}" class="width-1of1" placeholder="auto"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框颜色</td>
        <td>
            <input id="sLabelBorderColor" type="text" name="" value="${oe.sLabelBorderColor!}" style="background-color: ${oe.sLabelBorderColor!};" class="width-1of1 iColor" placeholder="auto"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框不透明度
            <div class="card-tooltip">
                <p>数值越大透明度越低</p>
            </div>
        </td>
        <td>
            <input id="sLabelBorderColorTransparent" type="number" min="0" max="10" name="" value="${oe.sLabelBorderColorTransparent!}" class="width-1of1" placeholder="auto"/>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框宽度</td>
        <td>
            <input id="sLabelBorderWidth" type="number" min="0" name="" value="${oe.sLabelBorderWidth!}" class="width-1of1" placeholder="auto"/>
        </td>
    </tr>
</#macro>
<#macro ec_tooltip oe>
    <tr>
        <th colspan="2">通用</th>
    </tr>
    <tr>
        <td class="text-right">启动提示框</td>
        <td>
            <@ec_yes_no v=oe.showTooltip dv=true id="showTooltip"></@ec_yes_no>
        </td>
    </tr>
    <tr>
        <td class="text-right">系列间共享
            <div class="card-tooltip">
                <p>系列共享主要用于柱状图、折线等使用类目轴的图表中可用</p>
            </div>
        </td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix" id="dLabel4" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span id="tooltipTrigger" class="choose-text" data-text="item" data-dt="否" v="${oe.tooltipTrigger?default('item')}">${OEUtils.doTooltipTrigger(oe.tooltipTrigger)}</span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu" aria-labelledby="dLabel4">
                    <li><a href="javascript:void(0);" v="axis">是</a></li>
                    <li><a href="javascript:void(0);" v="item">否</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <td class="text-right">限制提示框在图表区域内</td>
        <td>
            <@ec_yes_no v=oe.tooltipConfine dv=true id="tooltipConfine"></@ec_yes_no>
        </td>
    </tr>
    <tr>
        <th colspan="2">颜色和边框</th>
    </tr>
    <tr>
        <td class="text-right">背景颜色</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="tooltipBackgroundColor" type="text" class="form-control iColor no-margin" value="${oe.tooltipBackgroundColor}" style="background-color: ${oe.tooltipBackgroundColor};"/>
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">背景不透明度
            <div class="card-tooltip">
                <p>数值越大透明度越低</p>
            </div>
        </td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="tooltipBackgroundColorTransparent" type="number" min="0" max="10" class="form-control no-margin" value="${oe.tooltipBackgroundColorTransparent}" />
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框宽度</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="tooltipBorderWidth" type="number" class="form-control no-margin" value="${oe.tooltipBorderWidth!0}" min="0"/>
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框颜色</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="tooltipBorderColor" type="text" class="form-control iColor no-margin" value="${oe.tooltipBorderColor!}" style="background-color: ${oe.tooltipBorderColor!};"/>
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <th colspan="2">内容</th>
    </tr>
    <tr>
        <td class="text-right">格式化</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="tooltipFormatter" value="${oe.tooltipFormatter!}" type="text" class="form-control" placeholder='auto'/>
                </div>
            </form>
        </td>
    </tr>
</#macro>
<#macro ec_series oe series types=[] showType=false>
    <tr>
        <th colspan="2">数据列</th>
    </tr>
    <tr>
        <td colspan="2">
            <#if series?? && series?size gt 1>
            <div class="dropdown js-choose">
                <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="choose-text series-chosen" data-text="数据列1" >${series[0].name!}</span>
                </p>
                <ul class="dropdown-menu" aria-labelledby="dLabel3">
                    <#if series?? && series?size gt 0>
                        <#list series as s>
                            <li><a href="javascript:void(0);" v="${s.name!}" data-s="${s.name!}">${s.name!}</a></li>
                        </#list>
                    </#if>
                </ul>
            </div>
            </#if>
            <#if series?? && series?size gt 0>
                <#list series as s>
                    <div class="form-horizontal <#if s_index!=0>hidden</#if> series_${s.name!} series-child" id="myForm">
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">系列类型</label>
                            <div class="col-sm-10">
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span id="type" data-s="${s.name!}" class="choose-text" data-text="${s.type!}" data-dt="${OEUtils.doSeriesType(s.type?default(""))}" v="${s.type!}">${OEUtils.doSeriesType(s.type?default(""))}</span>
                                    <div class="pos-right">
                                    <span class="iconfont icon-refresh-fill"></span>
                                </div>
                                    </p>
                                    <ul class="dropdown-menu" aria-labelledby="dLabel3">
                                        <#if types?? && types?size gt 0>
                                            <#list types as t>
                                                <li><a href="javascript:void(0);" v="${t!}">${OEUtils.doSeriesType(t)}</a></li>
                                            </#list>
                                        </#if>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">颜色
                                <div class="card-tooltip">
                                    <p>此处定义的颜色优先级高于图表外观处颜色设置</p>
                                </div>
                            </label>
                            <div class="col-sm-10">
                                <input id="color" data-s="${s.name!}" id="color" type="text" class="form-control iColor no-margin" value="${s.color!}" style="background-color: ${s.color!};" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">线条样式</label>
                            <div class="col-sm-10">
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span data-s="${s.name!}" id="lineType" class="choose-text" data-text="${s.lineType?default("solid")}" data-dt="${s.lineType?default("solid")}" v="${s.lineType?default("solid")}">solid</span>
                                    <div class="pos-right">
                                    <span class="iconfont icon-refresh-fill"></span>
                                </div>
                                    </p>
                                    <ul class="dropdown-menu" aria-labelledby="dLabel3">
                                        <li><a href="javascript:void(0);" v="solid">solid</a></li>
                                        <li><a href="javascript:void(0);" v="dashed">dashed</a></li>
                                        <li><a href="javascript:void(0);" v="dotted">dotted</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">是否使用面积图</label>
                            <div class="col-sm-10">
                                <@ec_yes_no v=s.lineArea?default(false) dv=false id="lineArea" s=s.name />
                            </div>
                        </div>
                    </div>
                </#list>
            </#if>
        </td>
    </tr>
</#macro>

<#macro ec_legend oe>
    <tr>
        <th colspan="2">通用</th>
    </tr>
    <tr>
        <td class="text-right">启用图例</td>
        <td>
            <@ec_yes_no v=oe.showLegend dv=true id="showLegend"></@ec_yes_no>
        </td>
    </tr>
    <tr>
        <td class="text-right">图例布局</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix"
                   id="dLabel3" type="button"
                   data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="false">
                    <span id="legendOrient" class="choose-text" data-text="horizontal" data-dt="${OEUtils.doLegendOrient('horizontal')}"
                          v="${oe.legendOrient?default('horizontal')}">${OEUtils.doLegendOrient(oe.legendOrient)}</span>
                <div class="pos-right">
                    <span class="iconfont icon-refresh-fill"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel3">
                    <li><a href="javascript:void(0);" v="horizontal">水平</a></li>
                    <li><a href="javascript:void(0);" v="vertical">垂直</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <th colspan="2">位置</th>
    </tr>
    <tr>
        <td class="text-right">水平对齐</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix"
                   id="dLabel3" type="button"
                   data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="false">
                    <span id="legendLeft" class="choose-text" data-text="center" data-dt="${OEUtils.doLegendLocation('center')}"
                          v="${oe.legendLeft}">${OEUtils.doLegendLocation(oe.legendLeft)}</span>
                <div class="pos-right">
                    <span class="iconfont icon-refresh-fill"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel3">
                    <li><a href="javascript:void(0);" v="left">左对齐</a></li>
                    <li><a href="javascript:void(0);" v="right">右对齐</a></li>
                    <li><a href="javascript:void(0);" v="center">居中</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <td class="text-right">垂直对齐</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix"
                   id="dLabel3" type="button"
                   data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="false">
                    <span id="legendTop" class="choose-text" data-text="top" data-dt="顶部"
                          v="${oe.legendTop}">${OEUtils.doLegendLocation(oe.legendTop)}</span>
                <div class="pos-right">
                    <span class="iconfont icon-refresh-fill"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel3">
                    <li><a href="javascript:void(0);" v="top">顶部</a></li>
                    <li><a href="javascript:void(0);" v="bottom">底部</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <th colspan="2">外观</th>
    </tr>
    <tr>
        <td class="text-right">文本样式</td>
        <td>
            <@ec_composite_s fontSize=oe.legendTextFontSize fontBold=oe.legendTextFontBold fontItalic=oe.legendTextFontItalic fontColor=oe.legendTextFontColor idPrefix="legendText"  ></@ec_composite_s>
        </td>
    </tr>
    <tr>
        <td class="text-right">背景颜色</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="legendBackgroundColor" type="text" class="form-control iColor no-margin" value="${oe.legendBackgroundColor!}" style="background-color: ${oe.legendBackgroundColor!};" />
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">背景不透明度
            <div class="card-tooltip">
                <p>数值越大透明度越低</p>
            </div>
        </td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="legendBackgroundColorTransparent" type="number" min="0" max="10" class="form-control no-margin" value="${oe.legendBackgroundColorTransparent?default(0)}"  />
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框宽度</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="legendBorderWidth" type="number" min="0"
                           class="form-control no-margin"
                           value="${oe.legendBorderWidth!}"/>
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框角半径</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="legendBorderRadius" type="number" min="0"
                           class="form-control no-margin"
                           value="${oe.legendBorderRadius!}"/>
                </div>
            </form>
        </td>
    </tr>
    <tr>
        <td class="text-right">边框颜色</td>
        <td>
            <form class="color-set">
                <div class="form-group no-margin">
                    <input id="legendBorderColor" type="text"
                           class="form-control iColor no-margin"
                           value="${oe.legendBorderColor!}" style="background-color: ${oe.legendBorderColor!}" />
                </div>
            </form>
        </td>
    </tr>
</#macro>
<!-- 饼图设置center 和 内外半径 -->
<#macro ec_pie_cr crs>
    <tr>
        <th colspan="2">CR</th>
    </tr>
    <tr>
        <td colspan="2">
            <#if crs?? && crs?size gt 1>
            <div class="dropdown js-choose">
                <p class="dropdown-self no-margin clearfix" id="dLabel3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="choose-text crs-chosen" data-text="数据列1">${crs[0].name!}</span>
                </p>
                <ul class="dropdown-menu" aria-labelledby="dLabel3">
                    <#if crs?? && crs?size gt 0>
                        <#list crs as s>
                            <li><a href="javascript:void(0);" v="${s.name!}" data-s="${s.name!}">${s.name!}</a></li>
                        </#list>
                    </#if>
                </ul>
            </div>
            </#if>
            <#if crs?? && crs?size gt 0>
                <#list crs as cr>
                    <div class="form-horizontal <#if cr_index!=0>hidden</#if> crs_${cr.name!} crs-child" >
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">饼图的中心</label>
                            <div class="col-sm-10">
                                <input data-s="${cr.name!}"  id="pieCenter" type="text"
                                       class="form-control no-margin"
                                       value="${cr.pieCenter!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">饼图的内外半径</label>
                            <div class="col-sm-10">
                                <input data-s="${cr.name!}" id="pieRadius" type="text" class="form-control no-margin" value="${cr.pieRadius!}" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">扇区是否顺时针排布</label>
                            <div class="col-sm-10">
                                <@ec_yes_no v=cr.pieClockWise?default(false) dv=false id="pieClockWise" s=cr.name />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">南丁格尔图</label>
                            <div class="col-sm-10">
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix"
                                       id="dLabel4" type="button"
                                       data-toggle="dropdown"
                                       aria-haspopup="true"
                                       aria-expanded="false">
                                    <span data-s="${cr.name!}" id="pieRoseType" class="choose-text" data-text="false" data-dt="否"
                                          v="${cr.pieRoseType!}"><#if cr.pieRoseType=='false'>否<#else >${cr.pieRoseType!}</#if></span>
                                    <div class="pos-right">
                                        <span class="caret"></span>
                                    </div>
                                    </p>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dLabel4">
                                        <li><a href="javascript:void(0);" v="false">否</a></li>
                                        <li><a href="javascript:void(0);" v="radius">radius</a></li>
                                        <li><a href="javascript:void(0);" v="area">area</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">是否支持选中模式</label>
                            <div class="col-sm-10">
                                <div class="dropdown">
                                    <p class="dropdown-self no-margin clearfix"
                                       id="dLabel4" type="button"
                                       data-toggle="dropdown"
                                       aria-haspopup="true"
                                       aria-expanded="false">
                                    <span data-s="${cr.name!}" id="pieSelectedMode" class="choose-text" data-text="false" data-dt="否"
                                          v="${cr.pieSelectedMode!}"><#if cr.pieSelectedMode?default('false')=="false">否<#else>${cr.pieSelectedMode!}</#if></span>
                                    <div class="pos-right">
                                        <span class="caret"></span>
                                    </div>
                                    </p>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dLabel4">
                                        <li><a href="javascript:void(0);" v="false">否</a></li>
                                        <li><a href="javascript:void(0);" v="single">single</a></li>
                                        <li><a href="javascript:void(0);" v="multiple">multiple</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-margin">选中扇区的偏移距离</label>
                            <div class="col-sm-10">
                                <input data-s="${cr.name!}" id="pieSelectedOffset" type="text" class="form-control no-margin" value="${cr.pieSelectedOffset?default(10)}" />
                            </div>
                        </div>
                    </div>
                </#list>
            </#if>
        </td>
    </tr>
</#macro>
<!-- 标题 -->
<#macro ec_title oe={}>
    <tr>
        <th colspan="2">标题</th>
    </tr>
    <tr>
        <td class="text-right">显示标题</td>
        <td>
            <@ec_yes_no v=oe.showTitle?default(false) dv=false id="showTitle"></@ec_yes_no>
        </td>
    </tr>
    <tr>
        <td class="text-right">文本样式</td>
        <td>
            <@ec_composite_s fontSize=oe.titleFontSize?default(18) fontBold=oe.titleFontBold?default(true) fontItalic=oe.titleFontItalic?default(false) fontColor=oe.titleFontColor?default('#333') idPrefix="title" />
        </td>
    </tr>
    <tr>
        <td class="text-right">标题链接
            <div class="card-tooltip">
                <p>例子：http://www.winupon.com/</p>
            </div>
        </td>
        <td>
            <input id="titleLink" type="text" name="" value="${oe.titleLink!}" class="width-1of1" placeholder=""/>
        </td>
    </tr>
    <tr>
        <td class="text-right">链接打开方式</td>
        <td>
            <div class="dropdown">
                <p class="dropdown-self no-margin clearfix"
                   id="dLabel4" type="button"
                   data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="false">
                <span id="titleLinkTarget" class="choose-text" data-text="${oe.titleLinkTarget?default('blank')}" data-dt="${OEUtils.doLinkTarget(oe.titleLinkTarget?default('blank'))}"
                  v="${oe.titleLinkTarget?default('blank')}">${OEUtils.doLinkTarget(oe.titleLinkTarget?default('blank'))}
                </span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel4">
                    <li><a href="javascript:void(0);" v="blank">新标签页</a></li>
                    <li><a href="javascript:void(0);" v="self">当前窗口</a></li>
                </ul>
            </div>
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
                <span id="titleLeft" class="choose-text" data-text="${oe.titleLeft?default('left')}" data-dt="${OEUtils.doLegendLocation(oe.titleLeft?default('left'))}"
                      v="${oe.titleLeft?default('left')}">${OEUtils.doLegendLocation(oe.titleLeft?default('left'))}
                </span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel4">
                    <li><a href="javascript:void(0);" v="left">左对齐</a></li>
                    <li><a href="javascript:void(0);" v="right">右对齐</a></li>
                    <li><a href="javascript:void(0);" v="center">居中</a></li>
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
                <span id="titleTop" class="choose-text" data-text="${oe.titleTop?default('top')}" data-dt="${OEUtils.doLegendLocation(oe.titleTop?default('top'))}"
                      v="${oe.titleTop?default('top')}">${OEUtils.doLegendLocation(oe.titleTop?default('top'))}
                </span>
                <div class="pos-right">
                    <span class="caret"></span>
                </div>
                </p>
                <ul class="dropdown-menu"
                    aria-labelledby="dLabel4">
                    <li><a href="javascript:void(0);" v="top">顶部</a></li>
                    <li><a href="javascript:void(0);" v="bottom">底部</a></li>
                </ul>
            </div>
        </td>
    </tr>
</#macro>
