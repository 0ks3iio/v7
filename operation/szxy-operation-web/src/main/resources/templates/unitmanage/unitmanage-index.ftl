<!-- 单位管理首页 -->
<div class="page-sidebar">
    <div class="page-sidebar-header ml10 mr10">
        <div class="input-group mt20">
            <input type="text" class="form-control">
            <a href="#" class="input-group-addon">
                <i class="fa fa-search"></i>
            </a>
        </div>
    </div>
    <#import "../macro/unitTree.ftl" as u />
    <@u.unitTree dataType='unit' callback='f'/>

</div>

						<div class="page-content-inner">
                            <div id="unitListContainer" class="box box-default">
                                <div class="box-header">
                                    <h3 class="box-caption">所有单位</h3>
                                </div>
                                <div class="box-body">
                                    <div class="filter">
                                        <div class="filter-item">
                                            <button type="button" class="btn btn-blue" id="toInsert">新增</button>
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
                                    <div id="unitAccounts">

                                    </div>
                                </div>
                            </div>
                            <div id="createUnit" class="box box-default hide">

                            </div>
                            <div id="unitAuthorizationContainer" class="box box-default hide">

                            </div>
                        </div>

<!--页面离开 确认框 -->
		<div class="layer layer-confirm">
            <div class="layer-content">
                <table width="100%">
                    <tr>
                        <td class="text-right" valign="top">
                            <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                        </td>
                        <td>
                            <div class="font-16 mb10">确定要离开当前页面吗？</div>
                            <div class="color-grey">离开后当前页面的数据将不被保存。</div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
<#import "../macro/staticImport.ftl" as s />
<@s.datepicker/>
<script>

    var trUnit = {};
    var recordId;
    var ztreeId = '';
    var unitNameVaild = 0;//验证单位名称是否重复 0为不重复 1为重复
    var parentRegionCode = '000000';
    var pId = '';
    //TODO 点击页面调用
    $(function () {
        doGetUnitAccountList('');
        $(".filter-item").on("click", "#toInsert", turnInsert);

    });

    function turnInsert() {
        // $.get()
        $('#createUnit').load(_contextPath + '/operation/unit/manage/page/insert?flag=false', function (res, status) {
            if (status === "error") return
            $('#unitListContainer').addClass('hide');
            $('#createUnit').removeClass('hide');
        });
    }

    //返回列表首页
    function returnUnitList() {
        $('#createUnit').addClass('hide');
        $("#unitListContainer").removeClass('hide');
        $('#unitAuthorizationContainer').addClass('hide');
    }

    function doGetUnitAccountList(pageURL) {
        let pURL = doBuildDynamicParameter();
        if ($.trim(pageURL) !== '') {
            pURL = pURL + '&' + pageURL;
        }
        $('#unitAccounts').load(_contextPath + '/operation/unit/manage/page/unitList?parentId=' + ztreeId + '&' + pURL);
    }

    function doBuildDynamicParameter() {
        let pURL = 'unitType=' + $('#unitTypeOption').val() + '&' + 'usingNature=' + $('#usingNatureOption').val();
        return pURL;
    }

    function f(pId) {
        if ($("#createUnit").hasClass("hide")) {
            //主页显示的情况下
            $('#unitTypeOption').val('');
            $('#usingNatureOption').val('');
            ztreeId = pId;
            $('#unitAccounts').load(_contextPath + '/operation/unit/manage/page/unitList?parentId=' + pId);
        } else {
            //主页隐藏的情况下
            layer.open({
                type: 1,
                shadow: 0.5,
                title: false,
                area: '350px',
                btn: ['不离开', '离开'],
                content: $('.layer-confirm'),
                btn2: function () {
                    ztreeId = pId;
                    $('#unitListContainer').removeClass('hide');
                    $('#createUnit').addClass("hide");
                    $('#unitAccounts').load(_contextPath + '/operation/unit/manage/page/unitList?parentId=' + pId);
                }
            });
        }

    }