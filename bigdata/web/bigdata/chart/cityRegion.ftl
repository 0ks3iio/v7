<div class="filter-content select-height-36">
    <select name="" id="map_sub" class="form-control" data-placeholder="未选择">
        <option value="">请选择</option>
        <#if regions?? && regions?size gt 0>
            <#list regions as r>
                <option value="${r.regionCode!}"
                        <#if selectedRegionCode! ==r.regionCode>selected</#if>>${r.regionName!}
                </option>
            </#list>
        </#if>
    </select>
</div>
<script>

</script>