<#import "datav-create-diagram-config-value-macro.ftl" as valueMacro />
<#macro sameConfig diagram diagramGroups datasourceTypes databases apis key root active=false>
    <#if key !="">
    <div class="js-same <#if key!=diagram.id>hidden<#else>root-same</#if>" id="${diagram.id}">
    </#if>
        <ul class="nav nav-tabs nav-tabs-1">
            <#if key==diagram.id>
                <#if interactionItems?? && interactionItems?size gt 0>
                    <#assign class_name=3>
                <#else >
                    <#assign class_name=2>
                </#if>
            <#else >
                <#assign class_name=2>
            </#if>
            <li class="width-1of${class_name} text-center active">
                <a data-toggle="tab" href="#aa${diagram.id}">数据源</a>
            </li>
            <li class="width-1of${class_name} text-center">
                <a data-toggle="tab" href="#bb${diagram.id}">自定义参数</a>
            </li>
            <#if key==diagram.id>
            <#if interactionItems?? && interactionItems?size gt 0>
                <li class="width-1of3 text-center">
                    <a data-toggle="tab" href="#cc${diagram.id}">交互</a>
                </li>
            </#if>
            </#if>

        </ul>
        <div class="tab-content bg-f5f5f5 no-padding">
            <div class="tab-pane font-detail active" id="aa${diagram.id}">
                <#if diagram.diagramType?default(0) != 201 && diagram.diagramType?default(0) != 110
                    && diagram.diagramType?default(0) != 203 && diagram.diagramType?default(0) != 204
                    && diagram.diagramType?default(0) != 205 && diagram.diagramType?default(0) != 112
                    && diagram.diagramType?default(0) != 111 && diagram.diagramType?default(0) != 202>
                <div class="filter-item margin-t-10">
                    <span class="front-title">数据类型：</span>
                    <select id="datasourceType${diagram.id}" name="datasourceType" class="form-control chosen-select chosen-width inside-select"
                            data-placeholder="默认">
                    <#if datasourceTypes?? && datasourceTypes?size gt 0>
                        <#list datasourceTypes as datasourceType>
                            <option value="${datasourceType.getValue()}"
                                    <#if diagram.datasourceType==datasourceType.getValue()>selected</#if>>${datasourceType.getName()}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
                <div class="filter-item margin-t-10 <#if diagram.datasourceType!=2>hidden</#if> api">
                    <span class="front-title">数据源：</span>
                    <select id="api${diagram.id}" name="datasourceId" class="form-control chosen-select chosen-width inside-select"
                            data-placeholder="默认">
                    <#if apis?? && apis?size gt 0>
                        <#list apis as api>
                            <option value="${api.id}"
                                    <#if diagram.datasourceId?default("")==api.id>selected</#if> >${api.name}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
                <div class="filter-item margin-t-10 <#if diagram.datasourceType!=1>hidden</#if> database">
                    <span class="front-title">数据源：</span>
                    <select id="database${diagram.id}" name="datasourceId"
                            class="form-control chosen-select chosen-width inside-select" data-placeholder="默认">
                    <#if databases?? && databases?size gt 0>
                        <#list databases as database>
                            <option value="${database.id}"
                                    <#if diagram.datasourceId?default("")==database.id>selected</#if>>${database.name}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
                <div class="text scroll-height-area <#if diagram.datasourceType!=3>hidden</#if>">
                <pre id="editor_json${diagram.id}" class="scroll-height-area">
                    ${diagram.datasourceValueJson!}
                </pre>
                </div>
                <div class="text scroll-height-area <#if diagram.datasourceType!=1>hidden</#if>">
                <pre id="editor_sql${diagram.id}" class="scroll-height-area">

                </pre>
                </div>
                <#if key !="">
                    <div class="filter-item margin-t-10">
                        <span class="front-title">自动更新(秒)：</span>
                        <input id="updateInterval" value="${diagram.updateInterval!}" class="form-control inside-select" />
                    </div>
                </#if>
                <#elseif diagram.diagramType?default(0) == 110 || diagram.diagramType?default(0) == 111>
                    <div class="js-set-img set-tota-wrap">
                        <div class="set-detail no-padding-left">
                            <span class="front-title">图片添加：</span>
                        </div>
                        <ul class="add-img-ul clearfix">
                            <#if imageOptions?? && imageOptions?size gt 0>
                                <#list imageOptions as io>
                                    <li>
                                        <#if diagram.diagramType?default(0) == 111>
                                            <img class="delete" src="${request.contextPath}/static/bigdata/images/delate.png" alt="">
                                        </#if>
                                        <div class="vetically-center">
                                            <a href="javascript:void(0);" class="form-file js-add-pic">
                                                <input data-index="${io.index!}" class="js-change-pic" type="file" accept=".tiff,.tif,.svf,.png,.jpg,.jpeg,.jpe,.jp2,.gif,.dwg" name="file" id="">
                                            </a>
                                        </div>
                                        <img src="${fileUrl!}/${io.imagePath!}"/>
                                    </li>
                                </#list>
                            <#else >
                                <#if diagram.diagramType?default(0) == 110>
                                    <li class="js-add-pic-wrap">
                                        <a href="javascript:void(0);" class="form-file vetically-center js-add-pic" data-type="${diagram.diagramType?default(0)}">
                                            <img src="${request.contextPath}/static/bigdata/images/add.png" alt="">
                                            <input type="file" accept=".tiff,.tif,.svf,.png,.jpg,.jpeg,.jpe,.jp2,.gif,.dwg" name="file" id="upFile">
                                        </a>
                                    </li>
                                </#if>
                            </#if>
                            <#if diagram.diagramType?default(0) == 111>
                            <li class="js-add-pic-wrap">
                                <a href="javascript:void(0);" class="form-file vetically-center js-add-pic" data-type="${diagram.diagramType?default(0)}">
                                    <img src="${request.contextPath}/static/bigdata/images/add.png" alt="">
                                    <input type="file" accept=".tiff,.tif,.svf,.png,.jpg,.jpeg,.jpe,.jp2,.gif,.dwg" name="file" id="upFile">
                                </a>
                            </li>
                            </#if>
                        </ul>
                    </div>
                </#if>
            </div>
            <div class="tab-pane parameter-config" id="bb${diagram.id}">
            <#if diagramGroups?exists && diagramGroups?size gt 0>
                <#list diagramGroups as group>
                    <#if !group.array?default(false)>
                        <div class="set-op set-op-toggle collapsed active" data-toggle="collapse" aria-expanded="false"
                             href="#collapseExample${group_index}${diagram.id}">
                            ${group.groupName}
                            <div class="assist">
                                <i class="arrow fa fa-angle-down"></i>
                            </div>
                        </div>
                    <#else>
                        <div class="set-op set-op-toggle " data-toggle="collapse" aria-expanded="false"
                             href="#collapseExample${group_index}${diagram.id}">
                            <div class="set-op-toggle width-1of2 collapsed active">
                                ${group.groupName}
                            </div>
                            <div class="pos-right right-10">
                                <img src="${request.contextPath}/static/bigdata/datav/images/plus.png"
                                     data-groupkey="${group.groupKey}"
                                     data-max="${group.diagramParameterCategoryVos?size}" class="pointer js-add-array"/>&nbsp;
                                <img src="${request.contextPath}/static/bigdata/datav/images/remove-grey.png"
                                     class="pointer js-remove-array"/>
                            </div>
                            <div class="assist">
                                <i class="arrow fa fa-angle-down"></i>
                            </div>
                        </div>
                    </#if>
                    <div id="collapseExample${group_index}${diagram.id}" class="collapse <#if group_index==0>in</#if>" aria-expanded="false">
                        <div class="set-detail no-padding-bottom clearfix <#if group.array?default(false)>js-array-target</#if>">
                            <#if group.diagramParameterVos?exists && group.diagramParameterVos?size gt 0>
                                <#list group.diagramParameterVos as diagramParameterVo>
                                    <#if diagramParameterVo.key!='level'>
                                    <@valueMacro.config_value valueType=diagramParameterVo.valueType selects=diagramParameterVo.selects?default([]) value=diagramParameterVo.value?default("") name=diagramParameterVo.name key=diagramParameterVo.key />
                                    </#if>
                                </#list>
                            </#if>
                            <#if group.diagramParameterCategoryVos?exists && group.diagramParameterCategoryVos?size gt 0>
                                <#list group.diagramParameterCategoryVos as category>
                                    <#if group.array?default(false)>
                                    <div class="clearfix">
                                    </#if>
                                    <div class="filter-item margin-b-10 <#if group.array?default(false) && category_index+1=group.diagramParameterCategoryVos?size> pointer js-hover</#if>">
                                        <span class="front-title">
                                            <b>${category.categoryName!}</b>
                                        </span>
                                    </div>
                                    <#if category.diagramParameterVos?exists && category.diagramParameterVos?size gt 0>
                                        <#list category.diagramParameterVos as diagramParameterVo>
                                            <#if category.array?default(false)>
                                                <@valueMacro.config_value valueType=diagramParameterVo.valueType selects=diagramParameterVo.selects?default([]) value=diagramParameterVo.value?default("") name=diagramParameterVo.name key=diagramParameterVo.key arrayName=category.categoryName margin=group.array?default(false)/>
                                            <#else >
                                                <@valueMacro.config_value valueType=diagramParameterVo.valueType selects=diagramParameterVo.selects?default([]) value=diagramParameterVo.value?default("") name=diagramParameterVo.name key=diagramParameterVo.key/>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <#if group.array?default(false)>
                                    </div>
                                    </#if>
                                </#list>

                            </#if>
                        </div>
                    </div>
                </#list>
            </#if>
            </div>

            <div class="tab-pane font-detail" id="cc${diagram.id}">
                <div class="set-detail no-padding-bottom clearfix interaction-actives">
                    <#if key==diagram.id>
                    <#if interactionItems?? && interactionItems?size gt 0>
                        <@valueMacro.config_value valueType=2 selects=[] value='${active?string}' name='启用' key='active'/>
                        <#list interactionItems as item>
                            <div class="filter-item ">
                                <span class="front-title">${item.key}：</span>
                                <input id="${item.key}_" type="text" name="bindKeys" <#if !active>readonly</#if> data-key="${item.key}" value="${item.bindKey}" class="bind-key form-control inside-select interaction-active-input" />
                            </div>
                        </#list>
                    </#if>
                    </#if>
                </div>
            </div>
        </div>
        <script>
            <#if diagram.diagramType?default(0) != 202 && diagram.diagramType?default(0) != 201
                && diagram.diagramType?default(0) != 203 && diagram.diagramType?default(0) != 204
                && diagram.diagramType?default(0) != 205 && diagram.diagramType?default(0) != 112
                && diagram.diagramType?default(0) != 110 && diagram.diagramType?default(0) != 111>
            $('#editor_json${diagram.id}').height(400);
            $('#editor_sql${diagram.id}').height(400);
            let aceEditorSQL${diagram.id} = ace.edit('editor_sql${diagram.id}');
            let aceEditorJSON${diagram.id} = ace.edit('editor_json${diagram.id}');

            let langTools = ace.require("ace/ext/language_tools");
            <#--aceEditorSQL${diagram.id}.enableLiveAutocompletion.set()-->
            langTools.setCompleters([{
               getCompletions: function (editor, session, pos, prefix, callback) {
                    if (prefix == '$' || prefix=='\{') {
                        //
                        return callback(null, dataVAce.getTips());
                    } else {
                        return callback(null, []);
                    }
               }
            }]);
            aceEditorSQL${diagram.id}.setOptions({
                enableBasicAutocompletion: true,
                enableLiveAutocompletion: true
            });


            aceEditorSQL${diagram.id}.on('blur', function () {
                updateDG${diagram.id}();
            });
            aceEditorJSON${diagram.id}.on('blur', function () {
                updateDG${diagram.id}();
            });

            aceEditorSQL${diagram.id}.setTheme("ace/theme/twilight");
            aceEditorSQL${diagram.id}.session.setUseWrapMode(true);
            aceEditorSQL${diagram.id}.session.setMode("ace/mode/sql");
            aceEditorSQL${diagram.id}.session.setTabSize(2);

            aceEditorJSON${diagram.id}.session.setMode("ace/mode/json");
            aceEditorJSON${diagram.id}.setTheme("ace/theme/twilight");

            <#--let textSql = aceEditorSQL${diagram.id}.session.getValue();-->
            let textSql = "${diagram.datasourceValueSql!}";
            aceEditorSQL${diagram.id}.session.setValue($.trim(textSql));
            let textJson = aceEditorJSON${diagram.id}.session.getValue();
            if ($.trim(textJson) != '') {
                try {
                    let t = JSON.stringify(JSON.parse($.trim(textJson)), null, 4);
                    aceEditorJSON${diagram.id}.session.setValue(t);
                } catch (e) {
                    aceEditorJSON${diagram.id}.session.setValue($.trim(textJson));
                }

            } else {
                aceEditorJSON${diagram.id}.session.setValue('');
            }
            $('#datasourceType${diagram.id}').on('change', function () {
                let datasourceType = $(this).val();
                if (datasourceType == 1) {
                    $('#database${diagram.id}').parent().removeClass('hidden');
                    $('#api${diagram.id}').parent().addClass('hidden');
                    $('#editor_sql${diagram.id}').parent().removeClass('hidden');
                    $('#editor_json${diagram.id}').parent().addClass('hidden');
                }
                else if (datasourceType == 2) {
                    $('#database${diagram.id}').parent().addClass('hidden');
                    $('#api${diagram.id}').parent().removeClass('hidden');
                    $('#editor_sql${diagram.id}').parent().addClass('hidden');
                    $('#editor_json${diagram.id}').parent().addClass('hidden');
                } else {
                    $('#database${diagram.id}').parent().addClass('hidden');
                    $('#api${diagram.id}').parent().addClass('hidden');
                    $('#editor_sql${diagram.id}').parent().addClass('hidden');
                    $('#editor_json${diagram.id}').parent().removeClass('hidden');
                }
                updateDG${diagram.id}();
                //
            });
            $('#database${diagram.id}').on('change', function () {
                updateDG${diagram.id}();
            });
            $('#api${diagram.id}').on('change', function () {
                updateDG${diagram.id}();
            });

            function updateDG${diagram.id}() {
                let dg = createModifyDiagram${diagram.id}();
                dataVNet.updateDiagram(dg.diagramId, dg);
                //重新渲染
                let allowRender = dg.datasourceType == 1 && $.trim(dg.datasourceValueSql) != '';
                allowRender = allowRender || (dg.datasourceType == 3 && $.trim(dg.datasourceValueJson) != '');
                allowRender = allowRender || dg.datasourceType ==2;

                if (allowRender) {
                    dataVRender.doRender({diagramId: dg.diagramId, screenId: _screenId, dispose:true});
                }
            };

            function createModifyDiagram${diagram.id}() {
                let diagram = {};
                diagram.diagramId = "${root}";
                diagram.datasourceType = $('#datasourceType${diagram.id}').val();
                if (diagram.datasourceType == 1) {
                    diagram.datasourceId = $('#database${diagram.id}').val();
                } else {
                    diagram.datasourceId = $('#api${diagram.id}').val();
                }
                diagram.datasourceValueJson = aceEditorJSON${diagram.id}.session.getValue();
                diagram.datasourceValueSql = aceEditorSQL${diagram.id}.session.getValue();
                <#if key != "">
                    diagram.updateInterval = $("#updateInterval").val();
                </#if>
                diagram.elementId = $('.js-back').attr('data-elementid');
                if (!diagram.elementId) {
                    diagram.elementId = "";
                }
                return diagram;
            }
            <#if key != "">
            $("#updateInterval").on('blur', function () {
                let val = $(this).val();
                let diagramId = "${root}";
                if (!val) {
                    dataVTimer.removeTask(diagramId);
                } else {
                    if (dataVTimer.existsTask(diagramId)) {
                        dataVTimer.changeTaskInterval(diagramId, val * 1000);
                    } else {
                        dataVTimer.addTask(function () {
                            dataVRender.doRender({screenId: _screenId, diagramId: diagramId, dispose: false});
                        }, diagramId, val * 1000);
                    }
                }
                updateDG${diagram.id}();
            });
            </#if>
            </#if>
        </script>
    <#if key !="">
    </div>
    </#if>
</#macro>