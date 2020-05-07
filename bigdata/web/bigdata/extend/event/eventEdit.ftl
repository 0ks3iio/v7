<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<script src="${request.contextPath}/bigdata/v3/static/js/jquery-editable-select-master/dist/jquery-editable-select.min.js"></script>
<link href="${request.contextPath}/bigdata/v3/static/js/jquery-editable-select-master/dist/jquery-editable-select.min.css"
      rel="stylesheet">
<script src="${request.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js"></script>
<div class="eventEdit">
    <div class="condition-set bg-fff" id="eventEditDiv">
        <div class="filter-container no-margin bg-fff padding-20 border-bottom-E6E6E6 pos-rel">
            <div class="edit-name">
                <span id="favoriteTitle">${eventFavorite.favoriteName?default('事件名称')}</span>
            </div>
            <div class="pos-right-c set-made clearfix">
                <button class="btn btn-blue mr-10 pull-left js-save">保存</button>
                <button class="btn btn-default js-bank">事件库</button>
                <div class="target-bank">
                    <div class="target-name">事件库</div>
                    <div class="pa-10">
                        <div class="input-group">
                            <input type="text" id="searchName" class="form-control" placeholder="请输入关键字搜索数据">
                            <a href="javascript:void(0);" onclick="searchEventFavorite()" class="input-group-addon js-search" hidefocus="true"><i class="wpfont icon-search"></i></a>
                        </div>
                    </div>
                    <div class="target-content">
                        <table class="tables">
                            <thead>
                            <tr>
                                <th style="width: 100px;">事件名</th>
                                <th>更新时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                        </table>
                        <div class="scrollBar4" style="height: 260px;">
                            <table class="tables">
                                <tbody id="eventFavoriteList">

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="on-off">
                <span class="on-off-switch text-center pointer js-toggle" data-toggle="collapse"
                      href="#collapseEvent">-</span>
            <div>请添加事件</div>
        </div>

        <div id="collapseEvent" class="collapse in" aria-expanded="true">
            <div class="all-events">
                <div class="events first-event">
                    <div class="full-event js-remove-target">
                        <div class="event-head pos-rel clearfix">
                            <#--<div hidden="hidden" class="add-btn pos-left-20 pointer js-add-event">+</div>-->
                            <div class="filter-item">
                                <div class="event-word line-h-36">事件</div>
                            </div>
                            <div class="filter-item">
                                <div class="filter-content">
                                    <select name="" class="form-control eventSelect" id="eventSelect1" index="1"
                                            style="width: 300px;">
                                        <#list eventTypeMap?keys as key>
                                            <optgroup label="${eventTypeMap[key].typeName!}"></optgroup>
                                            <#if eventMap[key]?exists>
                                                <#list eventMap[key] as item>
                                                    <option granularity="${item.granularity!}"
                                                            <#if eventFavorite.eventId?exists><#if eventFavorite.eventId == item.id>selected="selected"</#if></#if>
                                                            value="${item.id!}">
                                                        &nbsp;&nbsp;&nbsp;${item.eventName!}</option>
                                                </#list>
                                            </#if>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <div class="filter-name line-h-36">的</div>
                            </div>
                            <div class="filter-item">
                                <div class="filter-content">
                                    <select name="" class="form-control width-auto eventIndexSelect"
                                            id="eventIndexSelect1">
                                        <#if indexList?exists>
                                            <#list indexList as item>
                                                <option
                                                        <#if indexId?default('') == item.id>selected="selected"</#if>
                                                        value="${item.id!}">${item.indicatorName!}</option>
                                            </#list>
                                        </#if>
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <div class="event-remove pointer"><i class="fa fa-times-circle hide js-remove"></i>
                                </div>
                            </div>
                            <#--<div class="filter-item">-->
                            <#--<div class="filter-name line-h-36 pointer filter-condition js-add-condition">+筛选条件-->
                            <#--</div>-->
                            <#--</div>-->
                        </div>

                    </div>
                </div>

                <div class="events second-event">
                    <#if eventPropertyIdList?exists && eventPropertyIdList?size gt 0>
                        <#list eventPropertyIdList as property>
                            <div class="full-event js-remove-target">
                                <div class="event-head pos-rel clearfix">
                                    <div class="add-btn pos-left-20 pointer js-add-event">+</div>
                                    <div class="filter-item">
                                        <div class="event-word line-h-36">按</div>
                                    </div>
                                    <div class="filter-item">
                                        <div class="filter-content">
                                            <select name="" class="form-control eventPropertySelect">
                                                <#if property_index == 0>
                                                    <option value="">请选择属性</option>
                                                </#if>
                                                <#list propertyMap?keys as key>
                                                    <optgroup label="${key!}"></optgroup>
                                                    <#list propertyMap[key] as item>
                                                        <option
                                                                <#if property?default('') == item.id>selected="selected"</#if>
                                                                value="${item.id!}">
                                                            &nbsp;&nbsp;&nbsp;${item.propertyName!}</option>
                                                    </#list>
                                                </#list>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="filter-item">
                                        <div class="filter-name line-h-36">查看</div>
                                    </div>
                                    <div class="filter-item">
                                        <div class="event-remove pointer"><i
                                                    class="fa fa-times-circle <#if property_index == 0>hide</#if> js-remove"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <div class="full-event js-remove-target">
                            <div class="event-head pos-rel clearfix">
                                <div class="add-btn pos-left-20 pointer js-add-event hidden">+</div>
                                <div class="filter-item">
                                    <div class="event-word line-h-36">按</div>
                                </div>
                                <div class="filter-item">
                                    <div class="filter-content">
                                        <select name="" class="form-control eventPropertySelect"
                                                id="eventPropertySelect1">
                                        </select>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <div class="filter-name line-h-36">查看</div>
                                </div>
                                <div class="filter-item">
                                    <div class="event-remove pointer"><i
                                                class="fa fa-times-circle hide js-remove"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#if>
                </div>

                <div class="events third-event">
                    <div class="event-body">
                        <div class="filter-condition-left pos-left-20 pointer js-add-condition-third">
                            <div class="add-btn inline-block show">+</div>
                            <span class="">筛选条件</span>
                        </div>
                        <div class="condition-body">
                            <div class="more-condition active"
                                 <#if conditions?exists && conditions?size gt 1>style="display: block"</#if> >
                                <div class="and js-and-or js-and-or-all">
                                    <#if conditionRelation?default('or') == 'and'>
                                        且
                                    <#else>
                                        或
                                    </#if>
                                </div>
                            </div>
                            <#if conditions?exists && conditions?size gt 0>
                                <#list conditions as conditionItem>
                                    <div class="condition-body">
                                        <div class="more-condition"
                                             <#if conditionItem?exists && conditionItem?size gt 1>style="display: block" </#if>>
                                            <div class="and js-and-or">
                                                <#if conditionRelation?default('or') == 'and'>
                                                    或
                                                <#else>
                                                    且
                                                </#if>
                                            </div>
                                        </div>
                                        <div class="condition-body-set" style="left: 550px;">
                                            <div class="">
                                                <span class="add-son-condition color-00cce3 pointer ml-10">新增</span>
                                                <span class="remove-condition-body color-00cce3 pointer ml-10">删除</span>
                                            </div>
                                        </div>
                                        <#list conditionItem as condition>
                                            <div class="condition-detail clearfix js-condition-remove-target">
                                                <div class="filter-item">
                                                    <div class="filter-content"><select name=""
                                                                                        class="form-control conditionPropertySelect">
                                                            <#list propertyMap?keys as key>
                                                                <optgroup label="${key!}"></optgroup>
                                                                <#list propertyMap[key] as item>
                                                                    <option <#if condition.eventPropertyId == item.id>selected="selected"</#if>
                                                                            value="${item.id!}">
                                                                        &nbsp;&nbsp;&nbsp;${item.propertyName!}</option>
                                                                </#list>
                                                            </#list>
                                                        </select></div>
                                                </div>
                                                <div class="filter-item">
                                                    <div class="filter-content"><select name="" class="form-control width-auto symbolSelect">
                                                            <option <#if condition.ruleSymbol == 'selector'>selected="selected"</#if> value="selector">等于</option>
                                                            <option <#if condition.ruleSymbol == 'not'>selected="selected"</#if> value="not">不等于</option>
                                                            <option <#if condition.ruleSymbol == 'in'>selected="selected"</#if> value="in">包含</option>
                                                            <option <#if condition.ruleSymbol == 'notIn'>selected="selected"</#if> value="notIn">不包含</option>
                                                            <option <#if condition.ruleSymbol == 'isNull'>selected="selected"</#if> value="isNull">为空</option>
                                                            <option <#if condition.ruleSymbol == 'isNotNull'>selected="selected"</#if> value="isNotNull">不为空</option>
                                                            <option <#if condition.ruleSymbol == 'upperStrict'>selected="selected"</#if> value="upperStrict">大于</option>
                                                            <option <#if condition.ruleSymbol == 'lowerStrict'>selected="selected"</#if> value="lowerStrict">小于</option>
                                                            <option <#if condition.ruleSymbol == 'upper'>selected="selected"</#if> value="upper">大于等于</option>
                                                            <option <#if condition.ruleSymbol == 'lower'>selected="selected"</#if> value="lower">小于等于</option>
                                                            <option <#if condition.ruleSymbol == 'regex'>selected="selected"</#if> value="regex">正则匹配</option>
                                                            <option <#if condition.ruleSymbol == 'regexNot'>selected="selected"</#if> value="regexNot">正则不匹配</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="filter-item">
                                                    <div class="filter-content">
                                                        <select class="form-control conditionValue">
                                                            <#if propertyDictionaryMap[condition.eventPropertyId!]?exists>
                                                                <#if !propertyDictionaryMap[condition.eventPropertyId!]?seq_contains(condition.conditionValue!)>
                                                                    <option selected>${condition.conditionValue!}</option>
                                                                </#if>
                                                                <#list propertyDictionaryMap[condition.eventPropertyId!] as item>
                                                                    <option <#if condition.conditionValue! == item!>selected</#if>>${item!}</option>
                                                                </#list>
                                                            <#else>
                                                                <option selected>${condition.conditionValue!}</option>
                                                            </#if>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="filter-item">
                                                    <div class="event-remove pointer"><i
                                                                class="fa fa-times-circle js-condition-remove"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </#list>
                                    </div>
                                </#list>
                            </#if>
                        </div>
                    </div>
                    <div class="condition-detail clearfix js-condition-remove-target" id="conditionTemplate"
                         hidden="hidden">
                        <div class="filter-item">
                            <div class="filter-content"><select name=""
                                                                class="form-control conditionPropertySelect conditionPropertySelectFilter">
                                </select></div>
                        </div>
                        <div class="filter-item">
                            <div class="filter-content"><select name="" class="form-control width-auto symbolSelect">
                                    <option value="selector">等于</option>
                                    <option value="not">不等于</option>
                                    <option value="in">包含</option>
                                    <option value="notIn">不包含</option>
                                    <option value="isNull">为空</option>
                                    <option value="isNotNull">不为空</option>
                                    <option value="upperStrict">大于</option>
                                    <option value="lowerStrict">小于</option>
                                    <option value="upper">大于等于</option>
                                    <option value="lower">小于等于</option>
                                    <option value="regex">正则匹配</option>
                                    <option value="regexNot">正则不匹配</option>
                                </select></div>
                        </div>
                        <div class="filter-item">
                            <div class="filter-content">
                                <select class="form-control conditionValue">
                                </select>
                            </div>
                        </div>
                        <div class="filter-item">
                            <div class="event-remove pointer"><i
                                        class="fa fa-times-circle js-condition-remove"></i></div>
                        </div>
                    </div>
                    <div class="condition-body" hidden="hidden" id="conditionTemplateFather">
                        <div class="more-condition">
                            <div class="and js-and-or">且</div>
                        </div>
                        <div class="condition-body-set" style="left: 550px;">
                            <div class="">
                                <span class="add-son-condition color-00cce3 pointer ml-10">新增</span>
                                <span class="remove-condition-body color-00cce3 pointer ml-10">删除</span>
                            </div>
                        </div>
                        <div class="condition-detail clearfix js-condition-remove-target">
                            <div class="filter-item">
                                <div class="filter-content"><select name=""
                                                                    class="form-control conditionPropertySelect conditionPropertySelectFilter">
                                    </select></div>
                            </div>
                            <div class="filter-item">
                                <div class="filter-content"><select name=""
                                                                    class="form-control width-auto symbolSelect">
                                        <option value="selector">等于</option>
                                        <option value="not">不等于</option>
                                        <option value="in">包含</option>
                                        <option value="notIn">不包含</option>
                                        <option value="isNull">为空</option>
                                        <option value="isNotNull">不为空</option>
                                        <option value="upperStrict">大于</option>
                                        <option value="lowerStrict">小于</option>
                                        <option value="upper">大于等于</option>
                                        <option value="lower">小于等于</option>
                                        <option value="regex">正则匹配</option>
                                        <option value="regexNot">正则不匹配</option>
                                    </select></div>
                            </div>
                            <div class="filter-item">
                                <div class="filter-content">
                                    <select class="form-control conditionValue">
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <div class="event-remove pointer"><i
                                            class="fa fa-times-circle js-condition-remove"></i></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="box box-default mt-20 js-height">
        <div class="date-self clearfix">
            <div class="select-imitate pull-left mb-10 js-select-time" data-type="less">
                <span class="days-num" type="最近7天" timeInterval="最近7天">最近7天</span><span
                        class="wpfont icon-calendar pos-right height-auto"></span>
            </div>
            <div class="contrast-box-wrap pull-left mb-10 js-select-contrast-time" data-type="more">
                <i class="iconfont icon-add-fill"></i>
                <span class="">对比日期</span>
            </div>
            <div class="contrast-date-wrap clearfix js-blur self-date-select" data-type="">
                <div class="less-date clearfix">
                    <div class="date-box-big">
                        <span>最近</span>
                        <input type="number" id="recentDayInput" class="form-control" min="1"
                               oninput="if(this.value.length>0){$('.self-date').removeClass('active')}if(value.length>3)value=value.slice(0,3)"
                               onkeyup="if(this.value>365){this.value=365}if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
                        <span>天</span>
                    </div>
                    <#list timeMap?keys as key>
                        <div class="date-box self-date" time="${timeMap[key]!}" type="${key!}">${key!}</div>
                    </#list>
                </div>
                <div class="calendar-date pull-left">
                    <div class="input-group">
                        <input type="text" readonly="readonly" class="form-control beginTime">
                        <span class="input-group-addon">
                                			<i class="wpfont icon-minus"></i>
                                		</span>
                        <input type="text" readonly="readonly" class="form-control endTime">
                    </div>
                    <div class="calendar-box-wrap mb-5 clearfix">
                        <div class="calendar-box beginTime-static"></div>
                        <div class="calendar-box endTime-static"></div>
                    </div>
                </div>
                <div class="btn-wrap-self clearfix btn-self-date">
                    <button type="button" class="btn btn-default">取消</button>
                    <button type="button" class="btn btn-blue ">确定</button>
                </div>
            </div>
            <#if compareTimeIntervalList?exists && compareTimeIntervalList?size gt 0>
                <#list compareTimeIntervalList as item>
                    <div class="contrast-date-box width-auto" timeinterval="${compareTimeMap[item]?default(item!)}"><span>${item!}</span> <i
                                class="fa fa-times-circle remove-compare-date"></i></div>
                </#list>
            </#if>
            <div class="contrast-date-wrap clearfix js-blur contrast-date-select" data-type="">
                <div class="less-date clearfix">
                    <#list compareTimeMap?keys as key>
                        <div class="date-box compare-date" time="${compareTimeMap[key]!}">${key!}</div>
                    </#list>
                </div>
                <div class="calendar-date pull-left">
                    <div class="input-group">
                        <input type="text" readonly="readonly" class="form-control contrastBeginTime">
                        <span class="input-group-addon">
                                			<i class="wpfont icon-minus"></i>
                                		</span>
                        <input type="text" readonly="readonly" class="form-control contrastEndTime">
                    </div>
                    <div class="calendar-box-wrap mb-5 clearfix">
                        <div class="calendar-box contrastBeginTime-static"></div>
                        <div class="calendar-box contrastEndTime-static"></div>
                    </div>
                </div>
                <div class="btn-wrap-self clearfix btn-compare-date">
                    <button type="button" class="btn btn-default">取消</button>
                    <button type="button" class="btn btn-blue ">确定</button>
                </div>
            </div>
        </div>
        <div class="filter margin-b-10 clearfix">
            <div class="row">
                <div class="col-md-5">
                    <h4 class="no-margin line-h-36"><b
                                id="chartName">${eventFavorite.favoriteName?default('事件名称')}</b>
                    </h4>
                </div>
                <div class="col-md-7 select-team">
                    <div class="filter-item-right">
                        <select name="" class="form-control" id="timeUnitSelect" onchange="changeTimeUnit(this)">
                            <option <#if eventFavorite.granularity?default('day') == 'second'>selected="selected"</#if>
                                    value="second">按秒
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'minute'>selected="selected"</#if>
                                    value="minute">按分钟
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'fifteen_minute'>selected="selected"</#if>
                                    value="fifteen_minute">按15分钟
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'thirty_minute'>selected="selected"</#if>
                                    value="thirty_minute">按30分钟
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'hour'>selected="selected"</#if>
                                    value="hour">按小时
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'day'>selected="selected"</#if>
                                    value="day">按天
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'week'>selected="selected"</#if>
                                    value="week">按周
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'month'>selected="selected"</#if>
                                    value="month">按月
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'quarter'>selected="selected"</#if>
                                    value="quarter">按季度
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'year'>selected="selected"</#if>
                                    value="year">按年
                            </option>
                        </select>
                    </div>
                    <div class="filter-item-right no-margin">
                        <select name="" class="form-control" id="chartTypeSelect">
                            <option <#if eventFavorite.chartType?default('line') == 'line'>selected="selected"</#if>
                                    value="line">线图
                            </option>
                            <option <#if eventFavorite.chartType?default('line') == 'bar'>selected="selected"</#if>
                                    value="bar">柱图
                            </option>
                            <option <#if eventFavorite.chartType?default('line') == 'pie'>selected="selected"</#if>
                                    value="pie">饼图
                            </option>
                            <option <#if eventFavorite.chartType?default('line') == 'dynamic'>selected="selected"</#if>
                                    value="dynamic">动态图
                            </option>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="js-canvas-wrap">
            <div class="canvas-box height-1of1" id="noDataDiv">

            </div>
            <div class="canvas-box height-1of1" id="chartDiv">

            </div>
        </div>
        <div class="line-h-36 text-center color-999 pos-rel hide" id="chartTip">
            <span id="exportSpan">&nbsp;</span>
            <div class="toggle-table">
                <span>展开报表</span> <i class="wpfont icon-angle-single-down"></i>
            </div>
        </div>
        <div class="table-wrap mb-20 hide" id="reportDiv">

        </div>
    </div>
</div>
<div class="layer layer-set clearfix">
    <form id="myForm">
        <#if isCopy?exists && isCopy>

        <#else>
            <input type="hidden" id="favoriteId" name="id" value="${eventFavorite.id!}">
        </#if>
        <div class="col-md-6">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">名称</label>
                    <div class="col-sm-9">
                        <input class="form-control" type="text" name="favoriteName" maxlength="50" id="favoriteName"
                               value="${eventFavorite.favoriteName!}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">时间范围</label>
                    <div class="col-sm-9">
                        <div class="select-imitate no-margin">
                            <span class="days-num-save"><#if beginDate?exists>${beginDate!}~${endDate}<#else></#if></span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">聚合时间单位</label>
                    <div class="col-sm-9">
                        <select name="granularity" class="form-control" id="granularitySelect">
                            <option <#if eventFavorite.granularity?default('day') == 'second'>selected="selected"</#if>
                                    value="second">按秒
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'minute'>selected="selected"</#if>
                                    value="minute">按分钟
                            </option>
                            <option
                                    <#if eventFavorite.granularity?default('day') == 'fifteen_minute'>selected="selected"</#if>
                                    value="fifteen_minute">按15分钟
                            </option>
                            <option
                                    <#if eventFavorite.granularity?default('day') == 'thirty_minute'>selected="selected"</#if>
                                    value="thirty_minute">按30分钟
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'hour'>selected="selected"</#if>
                                    value="hour">按小时
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'day'>selected="selected"</#if>
                                    value="day">按天
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'week'>selected="selected"</#if>
                                    value="week">按周
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'month'>selected="selected"</#if>
                                    value="month">按月
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'quarter'>selected="selected"</#if>
                                    value="quarter">按季度
                            </option>
                            <option <#if eventFavorite.granularity?default('day') == 'year'>selected="selected"</#if>
                                    value="year">按年
                            </option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">显示指标</label>
                    <div class="col-sm-9">
                        <select name="" class="form-control" id="showIndexSelect">
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">显示分组</label>
                    <div class="col-sm-9 chosen-1of1-36">
                        <select multiple name="" id="eventGroupSelect" class="form-control chosen-select"
                                data-placeholder="未选择"
                                style="width:100%;height:100px">
                            <#list eventGroups as item>
                                <option <#if favoriteGroupIds?exists><#if favoriteGroupIds?contains(item.id)>selected=selected</#if></#if>
                                        value="${item.id!}">${item.groupName!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">排序号</label>
                    <div class="col-sm-9">
                        <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                               onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               maxlength="3" value="${eventFavorite.orderId!}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label text-left line-h-36 no-padding">备注</label>
                    <div class="col-sm-9">
                        <textarea class="textarea-set" name="remark" rows="3"
                                  cols="">${eventFavorite.remark!}</textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-horizontal" id="myFormTwo">
                <div class="form-group">
                    <label class="col-sm-9 control-label text-left line-h-36"><b>图表类型</b></label>
                </div>
                <div class="form-group">
                    <div class="col-sm-9 clearfix js-add-active">
                        <div type="line"
                             class="chart-type line-type <#if eventFavorite.chartType?default('line') == 'line'>active</#if>">
                            <div class="text-center">
                                <i class="iconfont icon-diagram-fill"></i>
                            </div>
                            <div class="">线图</div>
                        </div>
                        <div type="bar"
                             class="chart-type bar-type <#if eventFavorite.chartType?default('line') == 'bar'>active</#if>">
                            <div class="text-center">
                                <i class="iconfont icon-columnchart-fill"></i>
                            </div>
                            <div class="">柱图</div>
                        </div>
                        <div type="pie"
                             class="chart-type pie-type <#if eventFavorite.chartType?default('line') == 'pie'>active</#if>">
                            <div class="text-center">
                                <i class="iconfont icon-piechart-fill"></i>
                            </div>
                            <div class="">饼图</div>
                        </div>
                    </div>
                </div>
                <input type="hidden" id="chartType" name="chartType" value="${eventFavorite.chartType?default('line')}">
                <div class="form-group">
                    <label class="col-sm-9 control-label text-left line-h-36"><b>窗口大小</b></label>
                </div>
                <div class="form-group">
                    <div class="col-sm-9 clearfix js-add-active">
                        <div type="1"
                             class="screen-type middle-screen clearfix <#if eventFavorite.windowSize?default(1) == 1>active</#if>">
                            <div class=""></div>
                            <div class="dash-box"></div>
                            <span class="">中</span>
                        </div>
                        <div type="2"
                             class="screen-type big-screen <#if eventFavorite.windowSize?default(1) == 2>active</#if>">
                            <div class=""></div>
                            <span class="">大</span>
                        </div>
                    </div>
                </div>
                <input type="hidden" id="windowSize" name="windowSize" value="${eventFavorite.windowSize?default(1)}">
            </div>
        </div>
        <input type="hidden" id="eventGroupIds" name="eventGroupIds" value='${eventGroupIds!}'>
        <input type="hidden" id="conditionList" name="conditionList" value='${conditionList!}'>
        <input type="hidden" id="indexId" name="indexId" value='${indexId!}'>
        <input type="hidden" id="eventId" name="eventId" value='${eventFavorite.eventId!}'>
        <input type="hidden" id="eventPropertyIds" name="eventPropertyIds" value='${eventPropertyIds!}'>
        <input type="hidden" id="conditionRelation" name="conditionRelation">
        <input type="hidden" id="contrastTimeInterval" name="contrastTimeInterval">
        <input type="hidden" id="timeInfo" name="timeInfo"
               value='<#if beginDate?exists>${beginDate!}~${endDate}<#else></#if>'>
    </form>
</div>
<script defer
        src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script defer
        src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script defer src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script type="text/javascript">
    var chartTimer;
    if (chartTimer) {
        window.clearInterval(chartTimer);
    }
    var properties = [];

    var beginTime = laydate.render({
        elem: '.beginTime-static',
        position: 'static',
        type: 'date',
        format: 'yyyy-MM-dd',
        trigger: 'click',
        btns: ['now'],
        max: 'nowTime',
        done: function (value, date, endD) {
            var eT = $('.endTime').val();
            var startDate = new Date(value.replace(/\-/g, "\/"));
            var endDate = new Date(eT.replace(/\-/g, "\/"));
            if (startDate > endDate) {
                initTime($('.beginTime').val(), eT);
                beginTime.hint("开始时间不能早于结束时间!");
            } else {
                $('.beginTime').val(value);
                $('.self-date').removeClass('active');
                $('#recentDayInput').val('');
            }
        }
    });

    var endTime = laydate.render({
        elem: '.endTime-static',
        position: 'static',
        type: 'date',
        format: 'yyyy-MM-dd',
        trigger: 'click',
        btns: ['now'],
        max: 'nowTime',
        done: function (value, date, endD) {

            var bT = $('.beginTime').val();
            var beginDate = new Date(bT.replace(/\-/g, "\/"));
            if (date < beginDate) {
                initTime(bT, $('.endTime').val());
                beginTime.hint("结束时间不能早于开始时间!");
            } else {
                $('.endTime').val(value);
                $('.self-date').removeClass('active');
                $('#recentDayInput').val('');
            }
        }
    });

    var contrastBeginTime = laydate.render({
        elem: '.contrastBeginTime-static',
        position: 'static',
        type: 'date',
        format: 'yyyy-MM-dd',
        trigger: 'click',
        btns: ['now'],
        max: 'nowTime',
        done: function (value, date, endD) {
            var eT = $('.contrastEndTime').val();
            var startDate = new Date(value.replace(/\-/g, "\/"));
            var endDate = new Date(eT.replace(/\-/g, "\/"));
            if (startDate > endDate) {
                initCompareTime($('.contrastBeginTime').val(), eT);
                contrastBeginTime.hint("开始时间不能早于结束时间!");
            } else {
                $('.contrastBeginTime').val(value);
                $('.compare-date').removeClass('active');
            }
        }
    });

    var contrastEndTime = laydate.render({
        elem: '.contrastEndTime-static',
        position: 'static',
        type: 'date',
        format: 'yyyy-MM-dd',
        trigger: 'click',
        btns: ['now'],
        max: 'nowTime',
        done: function (value, date, endD) {
            var eT = $('.contrastBeginTime').val();
            var startDate = new Date(eT.replace(/\-/g, "\/"));
            var endDate = new Date(value.replace(/\-/g, "\/"));
            if (startDate > endDate) {
                initCompareTime(eT, $('.contrastEndTime').val());
                contrastEndTime.hint("结束时间不能早于开始时间!");
            } else {
                $('.contrastEndTime').val(value);
                $('.compare-date').removeClass('active');
            }
        }
    });

    $(function () {
        function height() {
            $('.js-height').each(function () {
                $(this).css({
                    height: $(window).height() - $(this).offset().top - 40 + ($('.eventPropertySelect').length - 1) * 60 + $('.condition-body').height(),
                    // overflowY: 'auto',
                });
            });
            $('.js-canvas-wrap').each(function () {
                $(this).css({
                    height: $(window).height() - $(this).offset().top - 40 + ($('.eventPropertySelect').length - 1) * 60 + $('.condition-body').height()
                    // overflow: 'hidden'
                });
            })
        }

        height();

        //切换隐藏
        $('.js-toggle').on('click', function () {
            if ($(this).text() == '-') {
                $(this).addClass('active').text('+')
                $('#collapseEvent').collapse('hide');
            } else {
                $(this).removeClass('active').text('-')
                $('#collapseEvent').collapse('show');
            }
        });
        //添加事件
        $('.eventEdit').on('click', '.first-event .js-add-event', function () {
            var count = $('.first-event').find('.full-event').length + 1;
            var copydiv = $('.first-event').find('.full-event').first().clone();
            copydiv.find('.event-num').text("事件" + count);
            copydiv.find('.eventSelect').attr('index', count);
            copydiv.find('.eventSelect').attr('id', 'eventSelect' + count);
            copydiv.find('.eventIndexSelect').attr('id', 'eventIndexSelect' + count);
            $(this).parents('.full-event').find('.js-remove').removeClass('hide');
            $(this).parents('.full-event').after(copydiv);
        });
        $('.eventEdit').on('click', '.second-event .js-add-event', function () {
            // 获取已选择的属性
            getSelectProperty();

            var copydiv = $('.second-event').find('.full-event').first().clone();
            $('#eventPropertySelect1').children().first().attr('disabled', 'true');
            copydiv.find('.js-remove').removeClass('hide');
            // 排除已选择的属性
            copydiv.find('.eventPropertySelect option').each(function () {
                var property = $(this).attr('value');
                if (property == '') {
                    $(this).remove();
                } else {
                    if (properties.indexOf(property) > -1) {
                        // 若已经选择 则变为不可选
                        $(this).attr('disabled', 'true');
                        $(this).removeAttr('selected');
                    } else {
                        $(this).removeAttr('disabled');
                    }
                }
            });
            $(this).parents('.full-event').after(copydiv);
            // 修改所有属性下拉框
            resetEventPropertySelect();
            showChart();
        });

        //添加筛选3
        $('.eventEdit').on('click', '.js-add-condition-third', function () {

            var copydiv = $('#conditionTemplateFather').clone();
            copydiv.show();
            copydiv.removeAttr('id');
            $(this).next('.condition-body').append(copydiv);
            if ($(this).next('.condition-body').find('.condition-detail').length > 1) {
                $(this).next('.condition-body').children('.more-condition').show();
            }
            $(this).next('.condition-body').find('.conditionPropertySelect').last().trigger("change");
            afterShow();
        });

        //添加子筛选
        $('.eventEdit').on('click', '.add-son-condition', function () {
            var copydiv = $('#conditionTemplate').clone();
            copydiv.removeAttr('id');
            copydiv.show();
            $(this).closest('.condition-body').append(copydiv);
            if ($(this).closest('.condition-body').find('.condition-detail').length > 1) {
                $(this).closest('.condition-body').children('.more-condition').show();
            }
            $(this).closest('.condition-body').find('.conditionPropertySelect').last().trigger("change");
            afterShow();
        });

        //删除父筛选
        $('body').on('click', '.remove-condition-body', function () {
            if ($(this).closest('.condition-body').siblings('.condition-body').length == 1) {
                $(this).closest('.condition-body').siblings('.more-condition').hide()
            }
            $(this).closest('.condition-body').remove();
            afterShow();
            showChart();
        });

        function afterShow() {
            if ($('.condition-body .condition-body').length > 1) {
                $('.condition-body .condition-body').children('.more-condition').removeClass('no-after');
            } else {
                $('.condition-body .condition-body').children('.more-condition').addClass('no-after');
            }
        }

        //且，或
        $('.eventEdit').on('click', '.js-and-or-all', function () {
            if ($(this).text() == '且') {
                $('.js-and-or').text('且');
                $(this).text('或');
            } else {
                $('.js-and-or').text('或');
                $(this).text('且');
            }
            showChart();
        });

        //删除事件
        $('.eventEdit').on('click', '.js-remove', function () {
            var $f = $(this).parents('.first-event');
            if ($(this).closest('.events').find('.full-event').length == 2) {
                $(this).closest('.full-event').siblings('.full-event').find('.js-remove').addClass('hide');
                $('#eventPropertySelect1').children().first().removeAttr('disabled');
            }
            $(this).closest('.js-remove-target').remove();
            $f.find('.event-num').each(function (index, ele) {
                $(this).text('事件' + (index + 1))
            });
            resetEventPropertySelect();
            showChart();
        });

        //删除筛选
        $('.eventEdit').on('click', '.js-condition-remove', function () {
            if ($(this).closest('.condition-body').find('.condition-detail').length < 3) {
                $(this).closest('.condition-body').children('.more-condition').hide();

                if ($(this).closest('.condition-body').find('.condition-detail').length == 1) {

                    if ($(this).closest('.condition-body').siblings('.condition-body').length == 1) {
                        $(this).closest('.condition-body').siblings('.more-condition').hide()
                    }
                    $(this).closest('.condition-body').remove();
                }
            }

            $(this).closest('.condition-detail').remove();
            afterShow();
            showChart();
        });

        var isSubmit;
        $('.eventEdit').on('click', '.js-save', function () {
            var indexName = $('#eventSelect1 option:selected').text() + '的' + $('#eventIndexSelect1 option:selected').text();
            $('#showIndexSelect').empty().append('<option>' + indexName + '</option>');
            // 事件id
            var eventId = $('#eventSelect1').val();
            // 指标id
            var indexId = $('#eventIndexSelect1').val();
            if (indexId == null || indexId == '') {
                layer.tips("请选择事件", "#eventIndexSelect1", {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
            // 事件属性集合
            var eventPropertyIds = [];
            // 条件集合
            var conditionList = [];

            $('.eventPropertySelect').each(function () {
                if ($(this).val() != '') {
                    eventPropertyIds.push($(this).val());
                }
            });

            $('.condition-body .condition-body').each(function () {
                var condition = [];
                $(this).find('.conditionPropertySelect').each(function () {
                    if ($(this).val() != null && $(this).val() != '') {
                        con = new Object();
                        con.eventPropertyId = $(this).val();
                        con.ruleSymbol = $(this).parent().parent().next().find('.symbolSelect').val();
                        con.conditionValue = $(this).parent().parent().next().next().find('.conditionValue').val();
                        condition.push(con);
                    }
                });
                conditionList.push(condition);
            });
            // 条件关系 and or
            var conditionRelation = $('.js-and-or-all').text().replace(/\s+/g, "") == '或' ? 'or' : 'and';

            // 对比日期
            var contrastTimeInterval = [];
            $('.contrast-date-box').each(function (i, v) {
                contrastTimeInterval.push($(v).text().replace(/\s+/g, ""));
            });

            $('#eventPropertyIds').val(JSON.stringify(eventPropertyIds));
            $('#conditionList').val(JSON.stringify(conditionList));
            $('#indexId').val(indexId);
            $('#eventId').val(eventId);
            $('#conditionRelation').val(conditionRelation);
            $('#contrastTimeInterval').val(JSON.stringify(contrastTimeInterval));
            $('#favoriteName').val($('#chartName').text());

            layer.open({
                type: 1,
                shade: .6,
                title: '设置',
                btn: ['确定', '取消'],
                area: ['800px', '530px'],
                yes: function (index, layero) {
                    if (isSubmit) {
                        return;
                    }
                    $('#eventGroupIds').val(JSON.stringify($('#eventGroupSelect').val()));

                    if ($('#favoriteName').val() == "") {
                        layer.tips("不能为空", "#favoriteName", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                    if ($('#orderId').val() == "") {
                        layer.tips("排序号不能为空", "#orderId", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                    var options = {
                        url: "${request.contextPath}/bigdata/event/saveEventFavorite",
                        dataType: 'json',
                        success: function (data) {
                            if (!data.success) {
                                showLayerTips4Confirm('error', data.message, 't', null);
                                isSubmit = false;
                            } else {
                                layer.msg('保存成功', {icon: 1, time: 1000});
                                $('#favoriteId').val(data.data);
                                layer.close(index);
                            }
                        },
                        clearForm: false,
                        resetForm: false,
                        type: 'post',
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }//请求出错
                    };
                    $("#myForm").ajaxSubmit(options);
                },
                content: $('.layer-set')
            });
            $('.layui-layer-btn').css('border', 'none');
        });

        $('body').on('click', '.js-add-active>div', function () {
            $(this).addClass('active').siblings().removeClass('active')
        }).on('mouseenter', '.js-add-active>div', function () {
            $(this).addClass('hover')
        }).on('mouseleave', '.js-add-active>div', function () {
            $(this).removeClass('hover')
        });

        //选择图表类型
        $('body').on('click', '.chart-type', function () {
            $('#chartType').val($(this).attr('type'));
        });

        //选择图表类型
        $('body').on('click', '.screen-type', function () {
            $('#windowSize').val($(this).attr('type'));
        });

        //时间设置显现
        $('.select-imitate').on('click', function (event) {
            event.stopPropagation();
            $(this).siblings('.select-imitate-content').toggle();
            var tag = $(this).siblings('.select-imitate-content');
            var flag = true;
            $(document).bind("click", function (e) {
                var target = $(e.target);
                if (target.closest(tag).length == 0 && flag == true) {
                    $(tag).hide();
                    flag = false;
                    $(document).unbind('click');
                }
            });
        });

        $('body').on('click', '.select-imitate-content li', function () {
            $(this).addClass('active').siblings('li').removeClass('active');
            $(this).parent().next().find('.tab-pane').eq($(this).index()).addClass('active').siblings('.tab-pane').removeClass('active');
        });

        $('body').on('click', '.select-imitate-content .pos-rel', function () {
            $(this).find().addClass('active').siblings('li').removeClass('active');
        });

        // 选择事件
        $('.eventEdit').on('change', '.eventSelect', function () {
            // 根据事件查询事件指标
            var eventId = $(this).val();
            // 查询聚合粒度
            initGranularity(true);
            // 查询指标下拉框
            getIndexByEvent(eventId);
            // 删除属性筛选
            $('.js-add-event').addClass('hidden');
            $('.second-event').children().first().nextAll().remove();
            $('.condition-body').children().first().nextAll().remove();
            $('.more-condition').css('display', 'none');
            // 查询属性下拉框
            getPropertyByEvent(eventId);
            var name = $('#eventSelect1 option:selected').text().replace(/\s+/g, "");
            $('#chartName').text(name);
            $('#favoriteTitle').text(name);
            // 清空时间信息
            $('.days-num').attr('type', '最近7天');
            $('.days-num').attr('timeInterval', '最近7天');
            $('.days-num').text('最近7天');
            $('#recentDayInput').val('');
            $('.contrast-date-box').remove();

            if (chartTimer) {
                window.clearInterval(chartTimer);
            }
            showChart();
        });

        // 选择事件
        $('.eventEdit').on('change', '.eventIndexSelect', function () {
            showChart();
        });

        // 选择属性
        $('.eventEdit').on('change', '.eventPropertySelect', function () {
            if ($(this).val() != '') {
                $(this).parent().parent().prev().prev().removeClass('hidden');
            } else {
                $(this).parent().parent().prev().prev().addClass('hidden');
            }
            resetEventPropertySelect();
            showChart();
        });

        // 选择图表类型
        $('.eventEdit').on('change', '#chartTypeSelect', function () {
            if ($(this).val() == 'pie') {
                if ($('#eventPropertySelect1').val() == '') {
                    layer.msg('选择属性后才能查看饼图!');
                    $('#chartTypeSelect').val('line');
                    return;
                }
                $('#timeUnitSelect').attr('disabled', 'disabled');
            } else {
                $('#timeUnitSelect').removeAttr('disabled', 'disabled');
            }
            if ($(this).val() == 'dynamic') {
                // 动态刷新 自动选择按分钟刷新
                $('#timeUnitSelect').val('minute');
                $('.days-num').text(getDay(0) + "~" + getDay(0));
                $('.days-num').attr('timeInterval', getDay(0) + "~" + getDay(0));
                $('.days-num').attr('type', getDay(0) + "~" + getDay(0));
                chartTimer = window.setInterval(function () {
                    showChart();
                }, 60000);
            } else {
                if (chartTimer) {
                    window.clearInterval(chartTimer);
                }
            }
            showChart();
        });

        // 选择条件
        $('.eventEdit').on('change', '.conditionPropertySelect', function () {
            // 下拉框赋值
            var propertyId = $(this).val();
            var parent = $(this).closest('.condition-detail').find('.conditionValue').parent();
            $.ajax({
                url: '${request.contextPath}/bigdata/event/getPropertyDictionary',
                type: 'POST',
                data: {propertyId: propertyId},
                dataType: 'json',
                async: false,
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        parent.empty().append('<select class="form-control conditionValue"></select>');
                        var con = parent.find('.conditionValue');
                        $.each(response.data, function (i, v) {
                            var option = "<option>" + v + "</option>";
                            con.append(option);
                        });

                        con.editableSelect({
                            effects: 'slide',filter: false});
                        parent.find('.conditionValue').editableSelect('show');
                        parent.find('.conditionValue').on('select.editable', function(e){
                            showChart();
                        });
                        $('.es-list').addClass('scrollBar4');
                    }

                }
            });
        });

        // 选择条件
        $('.eventEdit').on('change', '.conditionValue', function () {
            showChart();
        });

        // 选择符号
        $('.eventEdit').on('change', '.symbolSelect', function () {
            if ($(this).val() == 'isNull' || $(this).val() == 'isNotNull') {
                $(this).parent().parent().next().css('display', 'none');
                showChart();
            } else {
                $(this).parent().parent().next().css('display', 'block');
                if ($(this).parent().parent().next().val() == '') {
                    layer.tips("请输入值筛选", $(this).parent().parent().next(), {
                        tipsMore: true,
                        tips: 3
                    });
                } else {
                    showChart();
                }
            }
        });

        $('head').append('<style>.datepicker{z-index: 9999999999!important;}</style>')

        $("body").keydown(function () {
            if (event.keyCode == "13") {//keyCode=13是回车键
                if ($('.self-date-select').css('display') != 'none') {
                    $('.btn-self-date>.btn-blue').click();
                }

                if ($('.contrast-date-select').css('display') != 'none') {
                    $('.btn-compare-date>.btn-blue').click();
                }

                if ($('.target-bank').css('display') != 'none') {
                    searchEventFavorite();
                }

            }
        });

        //自选日期
        $('.js-select-time').on('click', function (event) {
            $('.contrast-date-wrap').css('display', 'none');
            event.stopPropagation();
            $(this).siblings('.self-date-select').toggle();
            var tag = $(this).siblings('.self-date-select');
            var flag = true;
            $(document).bind("click", function (e) {
                var target = $(e.target);
                if (target.closest(tag).length == 0 && flag == true) {
                    $(tag).hide();
                    flag = false;
                    $(document).unbind('click');
                }
            });
            $(this).siblings('.self-date-select').find('.active').removeClass('active');
            $('.self-date[type="' + $('.days-num').attr('type') + '"]').addClass('active');
            var timeInfo = $('.self-date[type="' + $('.days-num').attr('type') + '"]').attr('time');
            if (timeInfo == null || timeInfo.indexOf('~') == -1) {
                timeInfo = $('.days-num').attr('timeinterval');
            }
            if (timeInfo.indexOf('最近') != -1) {
                var num = timeInfo.replace('最近', '').replace('天', '');
                var days = parseInt(num);
                ce = getDay(0);
                cb = getDay(-days);
                initTime(cb, ce);
            } else {
                var split = timeInfo.split("~");
                initTime(split[0], split[1]);
            }
        });

        //对比日期
        $('.js-select-contrast-time').on('click', function (event) {
            $('.contrast-date-wrap').css('display', 'none');
            event.stopPropagation();
            $(this).siblings('.contrast-date-select').toggle();
            var tag = $(this).siblings('.contrast-date-select');
            var flag = true;
            $(document).bind("click", function (e) {
                var target = $(e.target);
                if (target.closest(tag).length == 0 && flag == true) {
                    $(tag).hide();
                    flag = false;
                    $(document).unbind('click');
                }
            });
            $(this).siblings('.contrast-date-select').attr('data-type', $(this).data('type'));
            $(this).siblings('.contrast-date-select').find('.active').removeClass('active');
            $(this).siblings('.contrast-date-select').find('input').val('');
            $('.compare-date').first().click();
        });

        $('.eventEdit').on('click', '.self-date', function () {
            $(this).addClass('active').siblings().removeClass('active');
            // 日期赋值
            var timeInfo = $(this).attr('time');
            var split = timeInfo.split("~");
            $('#recentDayInput').val('');
            initTime(split[0], split[1]);
        });

        $('.eventEdit').on('click', '.compare-date', function () {
            $(this).addClass('active').siblings().removeClass('active');
            // 填充时间
            var currentTime = $('.days-num').attr('timeInterval');
            if ($(this).text() == '去年同期') {
                var ce;
                var cb;
                // 去年同期
                if (currentTime.indexOf('最近') != -1) {
                    var days = parseInt(currentTime.replace('最近', '').replace('天', ''));
                    ce = getDay(0);
                    cb = getDay(-days);
                } else if (currentTime.indexOf('~') != -1) {
                    var split = currentTime.split("~");
                    ce = split[1];
                    cb = split[0];
                }
                var lastB = getLastYear(cb);
                var lastE = getLastYear(ce);
                $(this).attr('time', lastB+'~'+lastE);
                initCompareTime(lastB, lastE);
            } else if ($(this).text() == '上一时段') {
                var ce;
                var cb;
                if (currentTime.indexOf('最近') != -1) {
                    var days = parseInt(currentTime.replace('最近', '').replace('天', ''));
                    ce = getDay(-days - 1);
                    cb = getDay(-days - days - 1);
                } else if (currentTime.indexOf('~') != -1) {
                    var split = currentTime.split("~");
                    var beginTime = Date.parse(split[0]);
                    var endTime = Date.parse(split[1]);
                    var differ = Math.floor(Math.abs(endTime - beginTime) / (24 * 3600 * 1000));
                    ce = addDate(split[0], -1);
                    cb = addDate(split[0], -differ - 1);
                }
                $(this).attr('time', cb+'~'+ce);
                initCompareTime(cb, ce);
            } else {
                // 日期赋值
                var timeInfo = $(this).attr('time');
                var split = timeInfo.split("~");
                initCompareTime(split[0], split[1]);
            }
        });

        $('.eventEdit').on('click', '.btn-self-date button', function () {
            if ($(this).text() == '确定') {
                var timeInfo;
                var timeInterval;
                $('.days-num').removeAttr('type');
                if ($('.self-date.active').length > 0) {
                    timeInfo = $('.self-date.active').text();
                    timeInterval = $('.self-date.active').attr('time');
                    $('.days-num').attr('type', $('.self-date.active').text());
                } else if ($('#recentDayInput').val() != '') {
                    var days = parseInt($('#recentDayInput').val());
                    timeInfo = '最近' + days + '天';
                    var begin = getDay(1 - days);
                    var end = getDay(0);
                    timeInterval = begin + '~' + end;
                    initTime(begin, end);
                } else {
                    timeInfo = $('.beginTime').val() + '~' + $('.endTime').val();
                    timeInterval = timeInfo;
                }
                $('.days-num').text(timeInfo);
                $('.days-num').attr('timeInterval', timeInterval);
                $('#timeInfo').val(timeInfo);
                $('.days-num-save').text(timeInfo);

                var timeUnit = $('#timeUnitSelect').val();
                if (timeUnit == 'minute' || timeUnit == 'fifteen_minute' || timeUnit == 'thirty_minute') {
                    var interval = $('.days-num').attr('timeInterval');
                    if (interval == '当日' || interval == '昨日' ) {
                    } else {
                        var split = interval.split("~");
                        if (split[0] != split[1]) {
                            layer.msg(' 按分钟查看，时间范围一次最多展示 1 天!');
                            $('.days-num').text(getDay(0) + '~' + getDay(0));
                            $('.days-num').attr('timeInterval', getDay(0) + '~' + getDay(0));
                            $('.days-num').attr('type', getDay(0) + '~' + getDay(0));
                        }
                    }
                }

                if (timeUnit == 'second') {
                    var interval = $('.days-num').attr('timeInterval');
                    if (interval == '当日' || interval == '昨日' ) {

                    } else {
                        var split = interval.split("~");
                        if (split[0] != split[1]) {
                            layer.msg(' 按秒查看，时间范围一次最多展示 1 天!');
                            $('.days-num').text(getDay(0) + '~' + getDay(0));
                            $('.days-num').attr('timeInterval', getDay(0) + '~' + getDay(0));
                            $('.days-num').attr('type', getDay(0) + '~' + getDay(0));
                        }
                    }
                }

                showChart();
            }
            $(this).closest('.contrast-date-wrap').hide();
        });

        // 对比日期按钮
        $('.eventEdit').on('click', '.btn-compare-date button', function () {
            if ($(this).text() == '确定') {
                var timeInfo;
                var timeInterval;
                if ($('.compare-date.active').length > 0) {
                    if ($('.compare-date.active').text() == '去年同期' || $('.compare-date.active').text() == '上一时段') {
                        timeInfo = $('.compare-date.active').attr('time');
                    } else {
                        timeInfo = $('.compare-date.active').text();
                    }
                    timeInterval = $('.compare-date.active').attr('time');
                } else {
                    timeInfo = $('.contrastBeginTime').val() + '~' + $('.contrastEndTime').val();
                    timeInterval = timeInfo;
                }
                if (timeInterval == '~' || timeInterval == '') {
                    showLayerTips4Confirm('error', '请选择对比日期!');
                    return;
                }
                var str = '<div class="contrast-date-box width-auto" timeInterval="' + timeInterval + '">\
                                        <span>' + timeInfo + '</span>\
                                        <i class="fa fa-times-circle remove-compare-date"></i>\
                                    </div>';
                $(this).closest('.contrast-date-wrap').before(str);
                showChart();
            }
            $(this).closest('.contrast-date-wrap').hide();
        });

        // 删除对比
        $('.eventEdit').on('click', '.remove-compare-date', function () {
            $(this).parent().remove();
            showChart();
        });

        //展开报表
        $('.toggle-table').on('click', function () {
            if ($(this).find('span').text() == '展开报表') {
                $(this).find('span').text('收起报表');
                $(this).find('i').css('transform', 'rotateZ(180deg)')

            } else {
                $(this).find('span').text('展开报表');
                $(this).find('i').css('transform', 'rotateZ(0deg)')
            }
            $('.table-wrap').toggleClass('hide');
        });

        var eventId = $('#eventSelect1').val();
        var indexId = '${indexId?default('')}';
        var isInit = true;
        if (indexId == '') {
            getIndexByEvent(eventId);
            getPropertyByEvent(eventId);
        } else {
            $('.conditionValue').editableSelect({
                effects: 'slide',filter: false
            });
            $('.conditionValue').on('select.editable', function(e){
                showChart();
            });
            $('.es-list').addClass('scrollBar4');

            getPropertyFilterByEvent(eventId);
            if ('${eventPropertyIds?default('[]')}' == '[]') {
                getPropertyByEvent(eventId);
            }
            isInit = false;
        }
        initSelection();
        initGranularity(isInit);

        if ('${eventFavorite.favoriteName?default('')}' == '') {
            var name = $('#eventSelect1 option:selected').text().replace(/\s+/g, "");
            $('#chartName').text(name);
            $('#favoriteTitle').text(name);
        }

        if ('${timeInfo?default('')}' != '') {
            $('.days-num-save').text('${timeInfo?default('')}');
            $('.days-num').text('${timeInfo?default('')}');
            $('.days-num').attr('timeInterval', '${timeInfo?default('')}');
            $('.days-num').attr('type', '${timeInfo?default('')}');
            $('#timeInfo').val('${timeInfo?default('')}');
            initTimeInfo();
        } else {
            if ('${beginDate?default('')}' == '') {
                $('.days-num-save').text('最近7天');
                $('#timeInfo').val('最近7天');
            } else {
                $(".beginTime").val('${beginDate?default('')}');
                $(".endTime").val('${endDate?default('')}');
                $('.days-num-save').text('${beginDate?default('')}~${endDate?default('')}');
                $('.days-num').text('${beginDate?default('')}~${endDate?default('')}');
                $('.days-num').attr('timeInterval', '${beginDate?default('')}~${endDate?default('')}');
                $('.days-num').attr('type', '${beginDate?default('')}~${endDate?default('')}');
                initTime('${beginDate?default('')}', '${endDate?default('')}');
            }
        }
        // 进入展示图表
        showChart();

        //事件库
        $('.eventEdit').on('click', '.js-layer', function () {

            layer.open({
                type: 1,
                shade: .6,
                title: '事件库',
                btn: false,
                area: ['300px', '400px'],
                content: $('.layer-delete')
            });
        });

        //指标库
        $('.js-bank').on('click', function (event) {
            event.stopPropagation();
            $(this).siblings('.target-bank').toggle();
            var tag = $(this).siblings('.target-bank');

            $.ajax({
                url: '${request.contextPath}/bigdata/event/eventLibrary',
                type: 'POST',
                dataType: 'html',
                success: function (response) {
                    $('#eventFavoriteList').empty().append(response);
                }
            });

            var flag = true;
            $(document).bind("click", function (e) {
                var target = $(e.target);
                if (target.closest(tag).length == 0 && flag == true) {
                    $(tag).hide();
                    flag = false;
                    $(document).unbind('click');
                }
            });
        });

        $('.eventEdit').on('click', '.target-bank a', function () {
            console.info($(this).hasClass('js-search'));
            if ($(this).hasClass('js-search')) {
                return;
            } else {
                $(this).closest('.target-bank').hide();
            }
        });
    });

    function initGranularity(isInit) {
        // 查询聚合粒度
        var granularity = $('#eventSelect1 option:selected').attr('granularity');
        var granularityIndex = $('#timeUnitSelect option[value="' + granularity + '"]')[0].index;
        $('#timeUnitSelect option').each(function () {
            if ($(this)[0].index < granularityIndex) {
                $(this).attr('style', 'display:none');
            } else {
                $(this).removeAttr('style');
            }
        });

        $('#granularitySelect option').each(function () {
            if ($(this)[0].index < granularityIndex) {
                $(this).attr('style', 'display:none');
                $(this).removeAttr('selected');
            } else {
                $(this).removeAttr('style');
            }
        });

        if (isInit) {
            $('#timeUnitSelect').val(granularity);
            $('#granularitySelect').val(granularity);
            if (granularityIndex > 4) {
                $('#chartTypeSelect').val('line');
            }
        }
        if (granularityIndex > 4) {
            $('#chartTypeSelect option[value="dynamic"]').attr('style', 'display:none');
        } else {
            $('#chartTypeSelect option[value="dynamic"]').removeAttr('style');
           // $('#timeUnitSelect').val('day');
        }
    }

    function getIndexByEvent(eventId) {
        // 查询指标下拉框
        $.ajax({
            url: '${request.contextPath}/bigdata/event/getEventIndex',
            type: 'POST',
            data: {eventId: eventId},
            dataType: 'json',
            async: false,
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('#eventIndexSelect1').empty();
                    $.each(data, function (i, v) {
                        $('#eventIndexSelect1').append("<option value='" + v.id + "'>" + v.indicatorName + "</option>")
                    });
                }
            }
        });
    }

    function getPropertyByEvent(eventId) {
        $.ajax({
            url: '${request.contextPath}/bigdata/event/getEventProperty',
            type: 'POST',
            data: {eventId: eventId},
            dataType: 'json',
            async: false,
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('.eventPropertySelect').empty();
                    $('.eventPropertySelect').append('<option value="">请选择属性</option>');
                    $('.conditionPropertySelectFilter').empty();
                    for (var key in data) {
                        var optgroup = "<optgroup label='" + key + "'></optgroup>";
                        $('.eventPropertySelect').append(optgroup);
                        $('.conditionPropertySelectFilter').append(optgroup);
                        $.each(data[key], function (i, v) {
                            var option = "<option value='" + v.id + "' >&nbsp;&nbsp;&nbsp;" + v.propertyName + "</option>";
                            $('.eventPropertySelect').append(option);
                            $('.conditionPropertySelectFilter').append(option);
                        });
                    }
                }
            }
        });

    }

    function getPropertyFilterByEvent(eventId) {
        $.ajax({
            url: '${request.contextPath}/bigdata/event/getEventProperty',
            type: 'POST',
            data: {eventId: eventId},
            dataType: 'json',
            async: false,
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('.conditionPropertySelectFilter').empty();
                    for (var key in data) {
                        var optgroup = "<optgroup label='" + key + "'></optgroup>";
                        $('.conditionPropertySelectFilter').append(optgroup);
                        $.each(data[key], function (i, v) {
                            var option = "<option value='" + v.id + "' >&nbsp;&nbsp;&nbsp;" + v.propertyName + "</option>";
                            $('.conditionPropertySelectFilter').append(option);
                        });
                    }
                }
            }
        });

    }

    function resetEventPropertySelect() {
        getSelectProperty();
        $('.eventPropertySelect').each(function () {
            var selectVal = $(this).val();
            $(this).find('option').each(function () {
                if (selectVal == $(this).val()) {
                    return;
                }
                // 如果当前的没有没选择 被其他选择了 则变成灰色 否则变成可选择
                if (properties.indexOf($(this).val()) > -1) {
                    $(this).attr('disabled', 'true');
                } else {
                    $(this).removeAttr('disabled');
                }
            });
        });
    }

    function getSelectProperty() {
        properties = [];
        $('.eventPropertySelect').each(function () {
            properties.push($(this).val());
        });
    }

    function showChart() {
        if ($('#chartTypeSelect').val() == 'pie') {
            if ($('#eventPropertySelect1').val() == '') {
                $('#chartTypeSelect').val('line');
            }
        }

        $('#noDataDiv').hide();
        $('#chartDiv').show();
        // 事件id
        var eventId = $('#eventSelect1').val();
        // 指标id
        var indexId = $('#eventIndexSelect1').val();
        if (indexId == null || indexId == '') {
            layer.tips("请选择事件", "#eventIndexSelect1", {
                tipsMore: true,
                tips: 3
            });
            $('#chartDiv').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                "                        <div class=\"text-center\">\n" +
                "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png\"/>\n" +
                "                            <p>暂无数据</p>\n" +
                "                        </div>\n" +
                "                    </div>");
            return;
        }
        // 事件属性集合
        var eventPropertyIds = [];
        // 图表类型
        var chartType = $('#chartTypeSelect').val();
        // 时间单位
        var timeUnit = $('#timeUnitSelect').val();
        // 时间间隔
        var timeInterval = $('.days-num').attr('timeInterval');
        // 条件集合
        var conditionList = [];
        // 条件关系 and or
        var conditionRelation = $('.js-and-or-all').text().replace(/\s+/g, "") == '或' ? 'or' : 'and';

        // 对比日期
        var contrastTimeInterval = [];
        $('.contrast-date-box').each(function (i, v) {
            contrastTimeInterval.push($(v).attr('timeInterval'));
        });

        $('.eventPropertySelect').each(function () {
            if ($(this).val() != '') {
                eventPropertyIds.push($(this).val());
            }
        });

        $('.condition-body .condition-body').each(function () {
            var condition = [];
            $(this).find('.conditionPropertySelect').each(function () {
                if ($(this).val() != null && $(this).val() != '') {
                    con = new Object();
                    con.eventPropertyId = $(this).val();
                    con.ruleSymbol = $(this).parent().parent().next().find('.symbolSelect').val();
                    con.conditionValue = $(this).parent().parent().next().next().find('.conditionValue').val();
                    condition.push(con);
                }
            });
            conditionList.push(condition);
        });


        var echart_div = echarts.init(document.getElementById('chartDiv'));
        if ($('#chartTypeSelect').val() != 'dynamic') {
            echart_div.showLoading({
                text: '数据正在努力加载...'
            });
        }
        $('#reportDiv').empty();
        // 筛选条件
        $.ajax({
            url: '${request.contextPath}/bigdata/event/getChartOption',
            type: 'POST',
            data: {
                eventId: eventId,
                eventPropertyIds: JSON.stringify(eventPropertyIds),
                chartType: chartType,
                timeUnit: timeUnit,
                timeInterval: timeInterval,
                conditionList: JSON.stringify(conditionList),
                conditionRelation: conditionRelation,
                indexId: indexId,
                contrastTimeInterval: contrastTimeInterval
            },
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#noDataDiv').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                        "                        <div class=\"text-center\">\n" +
                        "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png\"/>\n" +
                        "                            <p>暂无数据</p>\n" +
                        "                        </div>\n" +
                        "                    </div>");
                    $('#noDataDiv').show();
                    $('#chartDiv').hide();
                    echart_div.hideLoading();
                    $('#chartTip').addClass('hide');
                } else {
                    if ($('#chartTypeSelect').val() != 'dynamic') {
                        document.getElementById('chartDiv').setAttribute('_echarts_instance_', '');
                    }
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                    if ($('#chartTypeSelect').val() != 'dynamic') {
                        echart_div.hideLoading();
                    }
                    $('#chartTip').removeClass('hide');

                    // 时间间隔
                    timeInterval = $('.days-num').text().replace(/\s+/g, "");
                    // 对比日期
                    contrastTimeInterval = [];
                    $('.contrast-date-box').each(function (i, v) {
                        contrastTimeInterval.push($(v).text().replace(/\s+/g, ""));
                    });
                    // 筛选条件
                    $.ajax({
                        url: '${request.contextPath}/bigdata/event/getReport',
                        type: 'POST',
                        data: {
                            eventId: eventId,
                            eventPropertyIds: JSON.stringify(eventPropertyIds),
                            chartType: chartType,
                            timeUnit: timeUnit,
                            conditionList: JSON.stringify(conditionList),
                            conditionRelation: conditionRelation,
                            indexId: indexId,
                            timeInterval: timeInterval,
                            contrastTimeInterval: contrastTimeInterval
                        },
                        dataType: 'html',
                        success: function (response) {
                            $('#reportDiv').empty().html(response);
                            $('.toggle-table').find('span').text('收起报表');
                            $('.toggle-table').find('i').css('transform', 'rotateZ(0deg)')
                            $('.table-wrap').removeClass('hide');
                        }
                    });
                }
            }
        });

        $('#exportBtn').click(function (e) {
            e.stopPropagation();
            exportToExcel();
        });
    }

    function changeTimeUnit(e) {
        var timeUnit = $(e).val();
        $('#granularitySelect').val(timeUnit);
        // 若选择的是动态图,则
        if ($('#chartTypeSelect').val() == 'dynamic') {
            if (timeUnit != 'second' && timeUnit != 'minute' && timeUnit != 'fifteen_minute' && timeUnit != 'thirty_minute') {
                //捕获页
                layer.msg('动态图时，时间不能大于分钟!');
                return;
            }
            var frequency = 1000;
            if (timeUnit == 'minute') {
                frequency = frequency * 60;
            }
            if (timeUnit == 'fifteen_minute') {
                frequency = frequency * 60 * 15;
            }
            if (timeUnit == 'thirty_minute') {
                frequency = frequency * 60 * 30;
            }
            if (chartTimer) {
                window.clearInterval(chartTimer);
                chartTimer = window.setInterval(function () {
                    showChart();
                }, frequency);
            }
        }

        if (timeUnit == 'minute' || timeUnit == 'fifteen_minute' || timeUnit == 'thirty_minute') {
            var interval = $('.days-num').attr('timeInterval');
            if (interval == '当日' || interval == '昨日' ) {
            } else {
                var split = interval.split("~");
                if (split[0] != split[1]) {
                    layer.msg(' 按分钟查看，时间范围一次最多展示 1 天!');
                    $('.days-num').text(getDay(0) + '~' + getDay(0));
                    $('.days-num').attr('timeInterval', getDay(0) + '~' + getDay(0));
                    $('.days-num').attr('type', getDay(0) + '~' + getDay(0));
                }
            }
        }

        if (timeUnit == 'second') {
            var interval = $('.days-num').attr('timeInterval');
            if (interval == '当日' || interval == '昨日' ) {

            } else {
                var split = interval.split("~");
                if (split[0] != split[1]) {
                    layer.msg(' 按秒查看，时间范围一次最多展示 1 天!');
                    $('.days-num').text(getDay(0) + '~' + getDay(0));
                    $('.days-num').attr('timeInterval', getDay(0) + '~' + getDay(0));
                    $('.days-num').attr('type', getDay(0) + '~' + getDay(0));
                }
            }
        }
        showChart();
    }
    
    function initTimeInfo() {
        var timeInterval = $('.days-num').attr('timeInterval');
        if (timeInterval.indexOf('最近') != -1) {
            var num = timeInterval.replace('最近', '').replace('天', '');
            $('#recentDayInput').val(num);
        }
    }

    //初始化标签查询下拉框
    function initSelection() {
        if ($('.chosen-select').length > 0) {
            $('.chosen-select').chosen({
                allow_single_deselect: true,
                disable_search_threshold: 10,
                no_results_text: '没有找到组'
            });
            //resize the chosen on window resize

            $(window).off('resize.chosen')
                .on('resize.chosen', function () {
                    $('.chosen-select').each(function () {
                        var $this = $(this);
                        $this.next().css({'width': $this.width()});
                        $this.next().find('.chosen-results').css('height', '150px').addClass('scrollbar-made');
                    })
                }).trigger('resize.chosen');

        }
    }

    function getDay(day) {
        var today = new Date();
        var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
        today.setTime(targetday_milliseconds); //注意，这行是关键代码
        var tYear = today.getFullYear();
        var tMonth = today.getMonth();
        var tDate = today.getDate();
        tMonth = doHandleMonth(tMonth + 1);
        tDate = doHandleMonth(tDate);
        return tYear + "-" + tMonth + "-" + tDate;
    }

    function doHandleMonth(month) {
        var m = month;
        if (month.toString().length == 1) {
            m = "0" + month;
        }
        return m;
    }

    function exportToExcel() {
        if ($('#chartTypeSelect').val() == 'pie') {
            if ($('#eventPropertySelect1').val() == '') {
                $('#chartTypeSelect').val('line');
            }
        }

        $('#noDataDiv').hide();
        $('#chartDiv').show();
        // 事件id
        var eventId = $('#eventSelect1').val();
        // 指标id
        var indexId = $('#eventIndexSelect1').val();
        if (indexId == null || indexId == '') {
            layer.tips("请选择事件", "#eventIndexSelect1", {
                tipsMore: true,
                tips: 3
            });
            $('#chartDiv').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                "                        <div class=\"text-center\">\n" +
                "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png\"/>\n" +
                "                            <p>暂无数据</p>\n" +
                "                        </div>\n" +
                "                    </div>");
            return;
        }
        // 事件属性集合
        var eventPropertyIds = [];
        // 图表类型
        var chartType = $('#chartTypeSelect').val();
        // 时间单位
        var timeUnit = $('#timeUnitSelect').val();
        // 时间间隔
        var timeInterval = $('.days-num').text().replace(/\s+/g, "");
        // 条件集合
        var conditionList = [];
        // 条件关系 and or
        var conditionRelation = $('.js-and-or-all').text().replace(/\s+/g, "") == '或' ? 'or' : 'and';

        // 对比日期
        var contrastTimeInterval = [];
        $('.contrast-date-box').each(function (i, v) {
            contrastTimeInterval.push($(v).text().replace(/\s+/g, ""));
        });

        $('.eventPropertySelect').each(function () {
            if ($(this).val() != '') {
                eventPropertyIds.push($(this).val());
            }
        });

        $('.condition-body .condition-body').each(function () {
            var condition = [];
            $(this).find('.conditionPropertySelect').each(function () {
                if ($(this).val() != null && $(this).val() != '') {
                    con = new Object();
                    con.eventPropertyId = $(this).val();
                    con.ruleSymbol = $(this).parent().parent().next().find('.symbolSelect').val();
                    con.conditionValue = $(this).parent().parent().next().next().find('.conditionValue').val();
                    condition.push(con);
                }
            });
            conditionList.push(condition);
        });

        var data = {
            eventId: eventId,
            eventPropertyIds: JSON.stringify(eventPropertyIds),
            chartType: chartType,
            timeUnit: timeUnit,
            timeInterval: timeInterval,
            conditionList: JSON.stringify(conditionList),
            conditionRelation: conditionRelation,
            indexId: indexId,
            contrastTimeInterval: JSON.stringify(contrastTimeInterval)
        };

        var pForm = document.createElement("form");
        document.body.appendChild(pForm);
        $.each(data, function (key, value) {
            var i = document.createElement("input");
            i.type = "hidden";
            i.value = value;
            i.name = key;
            pForm.appendChild(i);
        });
        pForm.action = "${request.contextPath}/bigdata/event/exportReport";
        pForm.target = "_blank";
        pForm.method = "post";
        pForm.submit();
    }

    function initTime(begin, end) {
        $('.beginTime-static').empty();
        beginTime = laydate.render({
            elem: '.beginTime-static',
            position: 'static',
            type: 'date',
            value: begin,
            format: 'yyyy-MM-dd',
            trigger: 'click',
            btns: ['now'],
            max: 'nowTime',
            done: function (value, date, endD) {
                var eT = $('.endTime').val();
                var startDate = new Date(value.replace(/\-/g, "\/"));
                var endDate = new Date(eT.replace(/\-/g, "\/"));
                if (startDate > endDate) {
                    initTime($('.beginTime').val(), eT);
                    beginTime.hint("开始时间不能早于结束时间!");
                } else {
                    $('.beginTime').val(value);
                    $('.self-date').removeClass('active');
                    $('#recentDayInput').val('');
                }
            }
        });
        $('.beginTime').val(begin);

        $('.endTime-static').empty();
        endTime = laydate.render({
            elem: '.endTime-static',
            position: 'static',
            type: 'date',
            value: end,
            format: 'yyyy-MM-dd',
            trigger: 'click',
            btns: ['now'],
            max: 'nowTime',
            done: function (value, date, endD) {
                var bT = $('.beginTime').val();
                var startDate = new Date(bT.replace(/\-/g, "\/"));
                var endDate = new Date(value.replace(/\-/g, "\/"));
                if (endDate < startDate) {
                    initTime(bT, $('.endTime').val());
                    endTime.hint("结束时间不能早于开始时间!");
                } else {
                    $('.endTime').val(value);
                    $('.self-date').removeClass('active');
                    $('#recentDayInput').val('');
                }
            }
        });
        $('.endTime').val(end);
    }

    function initCompareTime(begin, end) {
        $('.contrastBeginTime-static').empty();
        contrastBeginTime = laydate.render({
            elem: '.contrastBeginTime-static',
            position: 'static',
            type: 'date',
            value: begin,
            format: 'yyyy-MM-dd',
            trigger: 'click',
            btns: ['now'],
            max: 'nowTime',
            done: function (value, date, endD) {

                var eT = $('.contrastEndTime').val();
                var startDate = new Date(value.replace(/\-/g, "\/"));
                var endDate = new Date(eT.replace(/\-/g, "\/"));
                if (startDate > endDate) {
                    initCompareTime($('.contrastBeginTime').val(), eT);
                    contrastBeginTime.hint("开始时间不能早于结束时间!");
                } else {
                    $('.contrastBeginTime').val(value);
                    $('.compare-date').removeClass('active');
                }
            }
        });
        $('.contrastBeginTime').val(begin);

        $('.contrastEndTime-static').empty();
        contrastEndTime = laydate.render({
            elem: '.contrastEndTime-static',
            position: 'static',
            type: 'date',
            value: end,
            format: 'yyyy-MM-dd',
            trigger: 'click',
            btns: ['now'],
            max: 'nowTime',
            done: function (value, date, endD) {

                var eT = $('.contrastBeginTime').val();
                var startDate = new Date(eT.replace(/\-/g, "\/"));
                var endDate = new Date(value.replace(/\-/g, "\/"));
                if (startDate > endDate) {
                    initCompareTime(eT, $('.contrastEndTime').val());
                    contrastEndTime.hint("结束时间不能早于开始时间!");
                } else {
                    $('.contrastEndTime').val(value);
                    $('.compare-date').removeClass('active');
                }
            }
        });
        $('.contrastEndTime').val(end);
    }

    function searchEventFavorite() {

        $.ajax({
            url: '${request.contextPath}/bigdata/event/eventLibrary?favoriteName=' + $('#searchName').val(),
            type: 'POST',
            dataType: 'html',
            success: function (response) {
                $('#eventFavoriteList').empty().append(response);
            }
        });

    }

    function getLastYear(now) {
        var localdate = new Date(now);
        var lastyear = new Date(localdate - 365 * 24 * 60 * 60 * 1000); //Fri Jul 20 2012 10:43:36 GMT+0800 (中国标准时间) 减一个月只需要把365改为30即可
        var year = lastyear.getFullYear(); //2012
        var month = lastyear.getMonth() + 1; //7
        month = month < 10 ? '0' + month : month; //"07"
        var day = lastyear.getDate();//20
        day = day < 10 ? '0' + day : day;//"20"
        return year + '-' + month + '-' + day;
    }

    function addDate(date, days) {
        var d = new Date(date);
        d.setDate(d.getDate() + days);
        var m = d.getMonth() + 1;
        return d.getFullYear() + '-' + m + '-' + d.getDate();
    }
</script>