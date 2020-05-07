<!-- 大屏图表的参数类型宏 -->
<#assign NUMBER=1 />
<#assign YES_NO=2 />
<#assign COLOR=3 />
<#assign FONT_WEIGHT=4 />
<#assign STRING=5 />
<#assign POSITION_TRANSVERSE=6 />
<#assign POSITION_PORTRAIT=7 />
<#assign BACKGROUND_COLOR=8 />
<#assign ORIENT=9 />
<#assign SERIES_TYPE=10 />
<#assign LABEL_POSITION=11 />
<#assign LABEL_POSITION_COMMON=12 />
<#assign LABEL_POSITION_FUNNEL=13 />
<#assign BORDER=14 />
<#macro config_value valueType selects value name key arrayName="" margin=false readOnly=false>
    <#if NUMBER==valueType>
        <div class="sizes clearfix active margin-b-10">
            <p class="mm small width-1of1">
                <span>${name}：</span>
                <input type="number" name="${key}" value="${value}" data-array="${arrayName}" class="width100-105"/>
                <span class="plus-minus js-click">
                    <button type="button" class="btn btn-default">
                        <i class="arrow fa fa-angle-up"></i>
                    </button>
                    <button type="button" class="btn btn-default">
                        <i class="arrow fa fa-angle-down"></i>
                    </button>
                </span>
            </p>
        </div>
    <#elseif YES_NO==valueType>
        <div class="filter-item sizes clearfix active <#if margin>margin-b-10</#if>">
            <p class="mm small width-1of1">
                <span>${name}：</span>
                <input readonly type="text" name="${key}" value="<#if value?boolean>是<#else>否</#if>" data-array="${arrayName}" class="width100-105"/>
                <span class="plus-minus js-click-yes">
                    <button type="button" class="btn btn-default" data-text="是"><i class="arrow fa fa-angle-up"></i></button>
                    <button type="button" class="btn btn-default" data-text="否"><i class="arrow fa fa-angle-down"></i></button>
                </span>
            </p>
        </div>
    <#elseif COLOR==valueType>
        <div class="filter-item <#if margin>margin-b-10</#if>">
            <span class="front-title">${name}：</span>
            <input style="background-color: ${value};" type="text" value="${value}" name="${key}" data-array="${arrayName}" class="form-control iColor inside-select" placeholder="请选择颜色"/>
        </div>
    <#elseif FONT_WEIGHT==valueType>
        <div class="filter-item <#if margin>margin-b-10</#if>">
            <span class="front-title">${name}：</span>
            <select name="${key}" data-array="${arrayName}" class="form-control chosen-select chosen-width inside-select" value="${value}">
                <#list selects as item>
                    <option value="${item.javaCode}" <#if value==item.javaCode>selected="selected"</#if>>${item.humanText}</option>
                </#list>
            </select>
        </div>
    <#elseif STRING==valueType>
        <div class="filter-item <#if margin>margin-b-10</#if>">
            <span class="front-title">${name}：</span>
            <input type="text" data-array="${arrayName}" name="${key}" value="${value}" <#if readOnly> readonly</#if> class="form-control inside-select" placeholder="请输入文本内容"/>
        </div>
    <#elseif POSITION_PORTRAIT==valueType || POSITION_TRANSVERSE==valueType
            || ORIENT==valueType || SERIES_TYPE==valueType || LABEL_POSITION==valueType || LABEL_POSITION_COMMON==valueType
            || LABEL_POSITION_FUNNEL==valueType || BORDER==valueType || 15==valueType || 16==valueType || 17==valueType || 18==valueType
            || 19==valueType || 20==valueType || 21==valueType>
        <div class="filter-item <#if margin>margin-b-10</#if>">
            <span class="front-title">${name}：</span>
            <select name="${key}" data-array="${arrayName}" class="form-control chosen-select chosen-width inside-select border" data-placeholder="">
                <#list selects as item>
                    <option value="${item.javaCode!}" <#if value?default("")==item.javaCode?default("")>selected="selected"</#if> style="background-color: ${item.javaCode!};">${item.humanText!}</option>
                </#list>
            </select>
        </div>
    <#elseif BACKGROUND_COLOR==valueType>

<#--        <div class="filter-item <#if margin>margin-b-10</#if>">-->
<#--            <span class="front-title">${name}：</span>-->
<#--            <select name="${key}" data-array="${arrayName}" class="form-control chosen-select chosen-width inside-select border" style="background-color: ${value?default("")};" data-placeholder="默认背景">-->
<#--                <#list selects as item>-->
<#--                    <option value="${item.javaCode?default("")}" <#if value?default("")==item.javaCode?default("")>selected="selected"</#if>-->
<#--                            <#if item.javaCode=='transparent'>-->
<#--                                style="background-color: white;"-->
<#--                            <#elseif item.humanText=="默认背景">-->
<#--                                style="background-color: rgba(127,219,244,0.8);"-->
<#--                            <#else >-->
<#--                                style="background-color: ${item.javaCode};"-->
<#--                            </#if>-->
<#--                            >-->
<#--                        ${item.humanText}-->
<#--                    </option>-->
<#--                </#list>-->
<#--            </select>-->
<#--        </div>-->
    </#if>
</#macro>
