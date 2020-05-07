<div class="bottom-explain-part">
    <p class="mb-15">${remark!'暂无描述！'}</p>
    <span class="color-999">更新时间：${statTime!'无'}</span>
</div>
<div class="bottom-explain-detail scrollBar4">
    <div class="box-module">
        <div class="">
            <span>总数据量：</span>
            <p>${totalAmount!'0'}</p>
        </div>
    </div>
    <div class="box-module">
        <div class="">
            <span>接口数：</span>
            <p>${apiAmount!'0'}</p>
        </div>
    </div>
    <div class="box-module">
        <div class="">
            <span>数据质量：</span>
            <#if dataQuality?exists>
            <p class="rating-show centered">
                <span class="rating-list">
                    <a href="javascript:void(0)" hidefocus="true" <#if dataQuality gt 0>class="active <#if dataQuality lte 10>half</#if>"</#if>></a>
                    <a href="javascript:void(0)" hidefocus="true" <#if dataQuality gt 20>class="active <#if dataQuality lte 30>half</#if>"</#if>></a>
                    <a href="javascript:void(0)" hidefocus="true" <#if dataQuality gt 40>class="active <#if dataQuality lte 50>half</#if>"</#if>></a>
                    <a href="javascript:void(0)" hidefocus="true" <#if dataQuality gt 60>class="active <#if dataQuality lte 70>half</#if>"</#if>></a>
                    <a href="javascript:void(0)" hidefocus="true" <#if dataQuality gt 80>class="active <#if dataQuality lte 90>half</#if>"</#if>></a>
                </span>
            </p>
            <#else>
            <p class="rating-show">
                无
            </p>
            </#if>
        </div>
    </div>
    <div class="box-module">
        <div class="">
            <span>应用数量（来源/去向）：</span>
            <p>${sourceAmount!'0'}/${targetAmount!'0'}</p>
        </div>
    </div>
</div>