<#import "../macro/unitTree.ftl" as unitTree />
<div class="page-sidebar">
    <div class="page-sidebar-header ml10 mr10">
        <div class="input-group mt20">
            <input type="text" class="form-control">
            <a href="#" class="input-group-addon">
                <i class="fa fa-search"></i>
            </a>
        </div>
    </div>
    <@unitTree.unitTree dataType='unit' callback='f'/>

</div>
<div class="page-content-inner">
    <div id="unitListContainer" class="box box-default">
        <div class="box-header">
            <h3 class="box-caption">所有单位</h3>
        </div>
        <div class="box-body">
            <div class="filter">
                <div class="filter-item">
                    <a href="#/operation/unit-manage/add-unit" type="button" class="btn btn-blue" id="addUnit">新增</a>
                </div>
                <div class="filter-item">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default">导入</button>
                        <a href="/operation/unit/manage/export/getAllunits" type="button"
                           class="btn btn-default">导出Excel</a>
                    </div>
                </div>
                <div class="filter-item filter-item-right">
                    <span class="filter-name">单位类型：</span>
                    <div class="filter-content">
                        <select name="" id="unitTypeOption" onchange="doGetUnitAccountList('');"
                                class="form-control">
                            <option value="">全部</option>
                        <#if unitTypeList?? && unitTypeList?size gt 0>
                            <#list unitTypeList as unitTypeVo>
                                <option value="${unitTypeVo.unitType}">${unitTypeVo.humanText}</option>
                            </#list>
                        </#if>
                        </select>
                    </div>
                </div>
                <div class="filter-item filter-item-right">
                    <span class="filter-name">单位性质：</span>
                    <div class="filter-content">
                        <select name="" id="usingNatureOption"
                                onchange="doGetUnitAccountList('');" class="form-control">
                            <option value="">全部</option>
                        <#if usingNatureList?? && usingNatureList?size gt 0>
                            <#list usingNatureList as usingNatureVo>
                                <option value="${usingNatureVo.usingNature}">${usingNatureVo.humanText}</option>
                            </#list>
                        </#if>
                        </select>
                    </div>
                </div>
            </div>
            <div id="unitList">
            </div>
        </div>
    </div>
    <div id="unitAuthorizationContainer" class="box box-default hide">

    </div>
</div>

<script>
    $(function () {
       //加载列表
        doGetUnitList();
        let addUnit = false;
        routeUtils.add('/operation/unit-manage/add-unit', function () {
            if (!addUnit) {
                $('#unitListContainer').load(_contextPath + '/operation/unit-manage/addUnit');
                addUnit = true;
            } else {
                $('#unitListContainer').removeClass('hide').siblings('#unitAuthorizationContainer').addClass('hide');
            }
        })
    });

    function doGetUnitList() {
        $('#unitList').load(_contextPath + '/operation/unit-manage/unitList');
    }
</script>