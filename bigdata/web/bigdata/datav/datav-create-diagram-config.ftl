<!--大屏基础图表配置-->
<#import "datav-create-diagram-config-value-macro.ftl" as valueMacro />
<#import "datav-create-diagram-config-panel-macro.ftl" as configPanel />
<div class="set-tota text-center">
    <span class="inline-block padding-side-20 relative">
        <span class="fa-back js-back hidden" data-elementid=""><i class="arrow fa fa-angle-left"></i></span>
        <span data-toggle="collapse" href="#collapseExampleTwoaaaaaaaa" aria-expanded="true">图表设置</span>
    </span>
</div>
    <div id="elemet_show_div">
    <#if elementContrasts?? && elementContrasts?size gt 0>
    <div class="set-op">
        <div class=" width-1of2 collapsed active" data-toggle="collapse" aria-expanded="true"
             href="#diagram_element">
            组件
        </div>
        <div class="pos-right right-10">
            <img src="${request.contextPath}/static/bigdata/images/plus.png" class="pointer js-open-add"/>
        </div>
        <div class="add-son-set">
            <ul class="scrollbar-made js-son-active">
                <#list elementContrasts as elementContrast>
                    <li class="element-item" data-type="${elementContrast.elementDiagramType}">${elementContrast.name}</li>
                </#list>
            </ul>
        </div>
    </div>
    <div id="diagram_element" class="collapse in border-bottom-cfd2d4 bg-f5f5f5" aria-expanded="true">
        <div class="set-detail no-padding-bottom clearfix js-component-target">
            <#if elementVos?? && elementVos?size gt 0>
            <#list elementVos as elementVo>
                <div class="filter-item relative margin-b-10 hover-effect">
                    <div class="width-2of3" >${elementVo.name}</div>
                    <div class="pos-right right-0">
                        <img src="${request.contextPath}/static/bigdata/images/remove-grey.png"
                             class="pointer js-remove-element" data-elementid="${elementVo.elementId!}"/>
                    </div>
                </div>
            </#list>
            </#if>
        </div>
    </div>
    </#if>
    </div>
<div class="collapse in set-tota-wrap config-panel" data-diagramid="" id="collapseExampleTwo"
     aria-expanded="true">
    <#list diagramGroupMap?keys as parameterVosKey>
        <#assign diagram=diagramMap[parameterVosKey] />
        <#assign diagramGroups=diagramGroupMap[parameterVosKey] />
        <@configPanel.sameConfig diagram=diagram diagramGroups=diagramGroups
            datasourceTypes=datasourceTypes databases=databases apis=apis key=rootDiagramId root=rootDiagramId active=active />
    </#list>
</div>
<script>
    $(function () {
        //是否按钮
        $('.iColor').iColor(function () {
            let config = {};
            let $valdom = $(this);
            config.key = $valdom.attr('name');
            config.value = $valdom.val();
            config.dom = $('div.box-data.choose')[0];
            config.arrayName = $valdom.attr('data-array');
            dataConfig.onChange(config);
        });
        $('.iColor').on('click', function () {
            $('#iColorPicker').css({
                right: '0px',
                left: ''
            })
        });

        var countFor = 0;
        $('.parameter-config').find('input').each(function () {
            $(this).on('input propertychange', function () {
                if ($(this).attr('type') == 'number') {
                    let val = parseInt($(this).val());
                    let name = $(this).attr('name')
                    if ( !((name == 'yMin' || name =='yMax' || name=='xMin' || name=='xMax') && $.trim($(this).val()) == '')) {
                        if (isNaN(val)) {

                            let id = $(this).attr('name') + (countFor++);
                            $(this).attr('id', id);
                            layer.tips('只能输入数字', '#' + id, {
                                tipsMore: true,
                                tips: 3,
                                time: 2000
                            });
                            return
                        }
                    }
                    $(this).val(val);
                }
                 dataConfig.onChange({
                    key: $(this).attr('name'),
                    value: $(this).val(),
                    dom: $('div.box-data.choose')[0],
                    arrayName: $(this).data('array')
                })
            })
        });
        $('.bind-key').each(function () {
            $(this).on('input propertychange', function () {
                let key = $(this).data('key');
                let elementId = $('div.box-data.choose').attr('data-index');
                let bindKey = $(this).val();
                if (!bindKey || bindKey=='') {
                    layer.tips('绑定参数名称不能为空', '#'+key+'_', {
                        tipsMore: true,
                        tips: 3,
                        time: 2000
                    });
                    return;
                }
                dataVNet.updateBindKey(elementId, key, bindKey);
            });
        });
        $('.parameter-config').on('change', 'select', function () {
            let name = $(this).attr('name');
            if (name == 'backgroundColor') {
                $(this).css('background-color', $(this).val());
            }
            dataConfig.onChange({
                key: $(this).attr('name'),
                value: $(this).val(),
                dom: $('div.box-data.choose')[0],
                arrayName: $(this).data('array')
            });
        });

        $('.js-component-target').find('.filter-item').each(function () {
            $(this).children('div:first').on('click', dataVCommon.onLoadElement);
        });
    })
</script>