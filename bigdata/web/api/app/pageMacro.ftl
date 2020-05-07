<#macro paginataion pages containerId pageCallFunction>
    <div class="pagination">
        <ul class="pagination-list float-right">
            <li class="page-prev <@page_pre_enabled number=pages.number+1 />"><a href="javascript:doGet${containerId}AssignPage(${pages.number});;">&lt;</a></li>
            <#if pages.totalPages lt 10>
                <@page_list start=1 end=pages.totalPages target=pages.number+1 containerId=containerId />
            <#else>
                <#if pages.number+1 lt 4>
                    <@page_list start=1 end=4 target=pages.number+1 containerId=containerId/>
                    <li>...</li>
                    <@page_single number=pages.totalPages target=pages.number+1 containerId=containerId/>
                <#elseif pages.number+1 gt (pages.totalPages-3)>
                    <@page_single number=1 target=pages.number+1 containerId=containerId/>
                    <li>...</li>
                    <@page_list start=pages.totalPages-3 end=pages.totalPages target=pages.number+1 containerId=containerId/>
                <#else >
                    <@page_single number=1 target=pages.number+1 containerId=containerId/>
                    <li>...</li>
                    <@page_list start=pages.number end=pages.number+2 target=pages.number+1 containerId=containerId/>
                    <li>...</li>
                    <@page_single number=pages.totalPages target=pages.number+1 containerId=containerId/>
                </#if>
            </#if>
            <li class="page-next <@page_next_enabled number=pages.number+1 total=pages.totalPages/>">
                <a href="javascript:doGet${containerId}AssignPage(${pages.number+2});;">&gt;</a>
            </li>

            <div class="form-group ml-10'">
                <span class='label'>跳到：</span>
                <input min="1" id="jump_${containerId}" type="text" class="form-control form-paging">
                <span class='label'>页</span>
                <button class="btn btn-default" onclick="doJump${containerId}Page();">确定</button>
            </div>
            <div class="form-group ml-10">
                <span class='label'>共${pages.totalElements}条</span>
                <span class='label ml-10'>每页</span>
                <select name="" id="" class="form-control" onchange="doChange${containerId}Size(this);">
                    <option value="5" <#if pages.size==5>selected</#if>>5</option>
                    <option value="10" <#if pages.size==10>selected</#if>>10</option>
                    <option value="15" <#if pages.size==15>selected</#if>>15</option>
                    <option value="20" <#if pages.size==20>selected</#if>>20</option>
                    <option value="30" <#if pages.size==30>selected</#if>>30</option>
                    <option value="50" <#if pages.size==50>selected</#if>>50</option>
                    <option value="100" <#if pages.size==100>selected</#if>>100</option>
                </select>
                <span class='label'>条</span>
            </div>
        </ul>
    </div>
    <script>
        function doJump${containerId}Page() {
            let target = parseInt($('#jump_${containerId}').val());
            if (isNaN(target)) {
                return;
            }
            doGet${containerId}AssignPage(target);
        }
        function doGet${containerId}AssignPage(page) {
            if (page > ${pages.totalPages}) {
                return ;
            }
            if (typeof ${pageCallFunction} === 'function') {
                    ${pageCallFunction}('size=${pages.size}&page='+page);
            }
        }
        function doChange${containerId}Size(el) {
            let size = $(el).val();
            if (typeof ${pageCallFunction} === 'function') {
                    ${pageCallFunction}('size=' + size + '&page=1');
            }
        }

    </script>
</#macro>

<#macro page_pre_enabled number>
    <#if number == 1>
        disabled
    </#if>
</#macro>
<#macro page_next_enabled number total>
    <#if number == total>
        disabled
    </#if>
</#macro>
<#macro page_item_active current target>
    <#if current == target>
        active
    </#if>
</#macro>
<#macro page_single number target containerId>
    <li class="page-item <@page_item_active current=number target=target />"><a href="javascript:doGet${containerId}AssignPage(${number});">${number}</a></li>
</#macro>
<#macro page_list start end target containerId>
    <#list start..end as number>
        <@page_single number=number target=target containerId=containerId />
    </#list>
</#macro>