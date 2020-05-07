<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<style>
    #tree-two li span.button {
        background: url('${request.contextPath}/static/bigdata/images/ztree-self.png') no-repeat;
        background-position: -69px 0px;
        position: absolute;
        top: 10px;
    }
</style>
<div class="box box-default padding-70 js-report" id="add-content">
    <form id="submitForm">
            <div class="form-horizontal" id="myForm">
                <div class="form-group">
                    <h3 class="col-sm-2 control-label no-padding-right bold">新增报表　</h3>
                </div>
                <div class="form-group">
                    <@upload.fileUpload businessKey="template" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" extensions="jasper" size="2" fileNumLimit="1" handler="loadFile">
                        <label class="col-sm-2 control-label no-padding-right">上传模板文件　</label>
                        <div class="col-sm-4">
                            <a class="file">
                                <input type="button" name="templateFileName"
                                       class="form-control datetimepicker js-addFiles"
                                       id="file">上传文件
                                <input type="hidden" name="templatePath" id="templatePath"
                                       value="${template.templatePath!}">
                                <input type="hidden" name="templateFileName" id="templateFileName"
                                       value="${template.templateFileName!}">
                            </a>
                            <p class="js-file-content" class="form-control">${template.templateFileName!}</p>
                        </div>
                        <!--这里的id就是存放附件的文件夹地址 必须维护-->
                        <input type="hidden" id="template-path" value="">
                    </@upload.fileUpload>
                </div>
                <div class="form-group">
                    <@upload.picUpload businessKey="thumbnail" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" size="1" fileNumLimit="1" handler="loadPhotoFile">
                        <label class="col-sm-2 control-label no-padding-right">上传缩略图　</label>
                        <div class="col-sm-4">
                            <div class="input-group">
                                <div class="filter-item block">
                                    <#if !template.id?? >
                                    <a href="javascript:void(0);" class="form-file pull-left js-pic js-addPhotos" style="background-image: url('${request.contextPath}/bigdata/v3/static/images/file/plus.png');
                                            background-repeat: no-repeat;background-position: center center;border-style: solid;cursor: pointer;">
                                        <input type="hidden" name="thumbnailFileName" id="thumbnailFileName"
                                               value="${template.thumbnailFileName!}">
                                    </a>
                                    </#if>
                                    <#if template.id?? >
                                    <a href="javascript:void(0);"
                                       class="form-file pull-left js-pic no-padding js-addPhotos">
                                        <img width="130px;" height="132px;" src="${fileUrl!}/${template.thumbnailPath!}"
                                             alt="${template.thumbnailFileName!}" onerror="this.src='${request.contextPath}/bigdata/v3/static/images/toast/error.png'">
                                        <input type="hidden" name="thumbnailFileName" id="thumbnailFileName"
                                               value="${template.thumbnailFileName!}">
                                    </a>
                                    </#if>
                                    <input type="hidden" name="thumbnailPath" id="thumbnailPath"
                                           value="${template.thumbnailPath!}">
                                </div>
                            </div>
                        </div>
                        <!--这里的id就是存放附件的文件夹地址 必须维护-->
                        <input type="hidden" id="thumbnail-path" value="">
                    </@upload.picUpload>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">数据类型　</label>
                    <div class="col-sm-4">
                        <div class="filter">
                            <div class="filter-item block">
                                <select name="datasourceType" id="datasource_type"
                                        class="form-control chosen-select chosen-width"
                                        onchange="changeDatasourceType(this);"
                                        data-placeholder="<#if dataSourceTypeVOs?exists && dataSourceTypeVOs?size gt 0>请选择数据源类型<#else>请先维护数据源信息</#if>">
                                <#if dataSourceTypeVOs?exists && dataSourceTypeVOs?size gt 0>
                                    <#list dataSourceTypeVOs as datasourceVO>
                                        <option value="${datasourceVO.value!}"
                                                <#if chart.dataSourceType! == datasourceVO.value!>selected</#if>>${datasourceVO.name!}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" id="datasource_label">数据源　</label>
                    <div class="col-sm-4">
                        <div class="filter">
                            <div class="filter-item block">
                                <select name="datasourceId" id="datasource_id"
                                        class=" form-control  chosen-select chosen-width" data-placeholder="未选择">
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right" id="data_set_label">sql语句　</label>
                    <div class="col-sm-4">
                        <textarea name="" rows="" cols="" class="width-1of1 border-ccc" id="data_set"
                                  placeholder="<请在此插入数据>">${chart.dataSet!}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">条件设置　</label>
                    <div class="col-sm-8">
                        <div class="col-sm-6 no-padding-left">
                            <div class="tree" style="height: 400px;">
                                <p class="tree-name">
                                    &nbsp;
                                    <button type="button" class="btn btn-blue btn-small-one js-add-fatherNode">新增</button>
                                </p>
                                <ul id="tree-two" class="ztree ztree-extand tree-body ztree-input"></ul>
                            </div>

                        </div>
                        <div class="col-sm-6 no-padding-left" style="display: none;" id="prop_set">
                            <div class="tree" style="height: 400px;">
                                <p class="tree-name">
                                    属性
                                </p>
                                <div class="tree-body">
                                    <input type="hidden" id="prarent_node">
                                    <p class="attribute-item">
                                        <span style="width: 150px">条件名称：</span><input type="text" readonly="readonly" class="" placeholder=""
                                                                 name="termName" id="termName">
                                    </p>
                                    <p class="attribute-item">
                                        <span style="width: 150px">条件唯一标识：</span><input type="text" class="" placeholder="" name="termKey"
                                                                  id="termKey">
                                    </p>
                                    <p class="attribute-item">
                                        <span style="width: 150px">及联字段：</span><input type="text" class="" placeholder="" name="termColumn"
                                                                  id="termColumn">
                                    </p>
                                    <p class="attribute-item">
                                        <span style="width: 150px">是否最终条件：</span>是<input type="radio" value="1" checked="checked" class="" placeholder="" name="isFinalTerm" id="isFinalTerm1">&nbsp;否<input
                                            type="radio" class="" placeholder="" value="0" name="isFinalTerm" id="isFinalTerm0">
                                    </p>
                                    <p class="attribute-item">
                                        <span style="width: 150px">是否执行查询：</span>是<input type="radio" value="1" checked="checked" class="" placeholder="" name="isQuery" id="isQuery1">&nbsp;否<input
                                            type="radio" class="" placeholder="" name="isQuery" value="0" id="isQuery0">
                                    </p>
                                    <p class="attribute-item">
                                        <span style="width: 150px">数据类型：</span>
                                        <select style="width: 150px;" name="termDatasourceType" id="term_datasource_type"
                                                onchange="changeTermDatasourceType(this);"
                                                data-placeholder="<#if dataSourceTypeVOs?exists && dataSourceTypeVOs?size gt 0>请选择数据源类型<#else>请先维护数据源信息</#if>">
                                                <#if dataSourceTypeVOs?exists && dataSourceTypeVOs?size gt 0>
                                                    <#list dataSourceTypeVOs as datasourceVO>
                                                        <#if datasourceVO.value != 3>
                                                        <option value="${datasourceVO.value!}"
                                                                <#if chart.dataSourceType! == datasourceVO.value!>selected</#if>>${datasourceVO.name!}</option>
                                                        </#if>
                                                    </#list>
                                                </#if>
                                        </select>
                                    </p>
                                    <p class="attribute-item">
                                        <span style="width: 150px">数据源：</span>
                                        <select style="width: 150px;" name="termDatasourceId" id="term_datasource_id" data-placeholder="未选择">
                                        </select>
                                    </p>
                                    <div class="attribute-item">
                                        数据集：<textarea name="" rows="5" cols="" class="width-1of1 border-ccc" id="dataSet"
                                                      placeholder="<sql写法必须包含id和name select xxx as id，yyy as name from table………………..>"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <input type="hidden" id="chart_id" value="${chart.id!}"/>
                <input type="hidden" id="chart_unitId" value="${chart.unitId!}"/>
                <input type="hidden" id="order_type" value="${chart.orderType!}"/>
                <input type="hidden" id="datasourceId" value="${datasourceId!}"/>
                <div class="form-group">
                    <div class="col-sm-8 col-sm-offset-2">
                        <button type="button" class="btn btn-long btn-blue" onclick="saveChart(true, false, true);">保存报表</button>
                        <button type="button" class="btn btn-long btn-blue js-check">预览报表</button>
                    </div>
                </div>
            </div>
    </form>
</div>

<div class="layer layer-power">
    <div class="form-horizontal" id="myForm-two">
        <div class="form-group">
                    <div class="filter-item block" id="termMain">
                    </div>
        </div>
    </div>
    <div class="form-horizontal" id="myForm-two">
    <div class="table-container" id="table-container" style="height: 600px; overflow:auto;">

    </div>
    </div>
</div>

<div class="layer layer-save">
    <div class="filter-item block">
        <span class="filter-name">报表名称　</span>
        <div class="filter-content">
            <input type="text" id="chart_name" value="${chart.name!}" class="form-control width-300"
                   placeholder="请输入报表名称">
        </div>
    </div>
    <div id="tagsList">
        <div class="filter-item block">
            <span class="filter-name">设置标签　</span>
            <div class="">
                <select multiple name="" id="tag_selection" class="form-control chosen-select"
                        data-placeholder="<#if tags?? && tags?size gt 0>请选择合适的标签<#else>没有可选标签，去基础设置新建一个吧</#if>"
                        style="width:300px;">
                <#if tags?? && tags?size gt 0>
                    <#list tags as tag>
                        <option value="${tag.id!}" <#if tag.selected!false>selected</#if>>${tag.name!}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
    </div>
</div>

<script>

    // 保存条件临时map
    var termMap = {};

    $(function () {
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: false,
                showIcon: false,
                showLine: false,
                fontCss: getFont,
                nameIsHTML: true
            },
            edit: {
                enable: true,
                editNameSelectAll: true,
                showRemoveBtn: true,
                showRenameBtn: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeDrag: beforeDrag,
                beforeEditName: beforeEditName,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename,
                onRemove: onRemove,
                onRename: onRename
            }
        };
        var zNodes = [];
        // 初始化树和map
        initData();
        function initData() {
            var terms = '${reportTerms!}';
            if (terms != null && terms != '') {
                var idMap ={};
                var jsonObject = JSON.parse(terms);
                var j=0;
                for(var i=0; i<jsonObject.length; i++) {
                    zNodes[j++] = {id:jsonObject[i].id, pId:jsonObject[i].cascadeTermId, name:jsonObject[i].termName};
                    idMap[jsonObject[i].id]=jsonObject[i].termName;
                    var map = {};
                    map['termName']=jsonObject[i].termName;
                    map['termKey']=jsonObject[i].termKey;
                    map['termColumn']=jsonObject[i].termColumn;
                    map['dataSet']=jsonObject[i].dataSet.replaceAll('@', '[').replaceAll('#', ']');
                    map['isFinalTerm']=jsonObject[i].isFinalTerm;
                    map['isQuery']=jsonObject[i].isQuery;
                    map['termDataSourceType']=jsonObject[i].dataSourceType;
                    if (jsonObject[i].dataSourceType == '1') {
                        map['termDataSourceId']=jsonObject[i].termDatabaseId;
                    } else {
                        map['termDataSourceId']=jsonObject[i].termApiId;
                    }

                    map['isQuery']=jsonObject[i].isQuery;
                    var parentNode = idMap[jsonObject[i].cascadeTermId];
                    if (parentNode != null) {
                        map['parentNode']=parentNode;
                    }
                    termMap[jsonObject[i].termName]=map;
                }
            }
        }

        var num = zNodes.length;

        //背景颜色
        function getFont(treeId, node) {
            return node.font ? node.font : {};
        };
        var className = "dark";

        function beforeDrag(treeId, treeNodes) {
            return false;
        };

        function beforeEditName(treeId, treeNode) {
            className = (className === "dark" ? "" : "dark");
            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            zTree.selectNode(treeNode);
            zTree.editName(treeNode);
            return false;
        };

        //删除节点
        function beforeRemove(treeId, treeNode) {
            className = (className === "dark" ? "" : "dark");
            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            var isRemove = false;
            zTree.selectNode(treeNode);
            showConfirmTips('prompt', '删除条件', "确认删除 条件 -- " + treeNode.name + " 吗？", function (index) {
                layer.close(index);
                num--;
                deleteTermMap(zTree, treeNode);
                // 清除表单并隐藏
                if (treeNode.name == $('#termName').val()) {
                    $('#prop_set').hide();
                }
                zTree.removeNode(treeNode);
            });
            return false;
        };

        function deleteTermMap(zTree, treeNode) {
            // 删除map
            var tm = termMap[treeNode.name];
            if (tm != null) {
                delete termMap[treeNode.name];
            }
            var childs = treeNode.children;
            if (childs != null) {
                childs.forEach(function (val, index) {
                    deleteTermMap(zTree, val);
                });
            }
        }
        
        function onRemove(e, treeId, treeNode) {

        };

        function beforeRename(treeId, treeNode, newName, isCancel) {
            className = (className === "dark" ? "" : "dark");

            if (newName.length == 0) {
                setTimeout(function () {
                    var zTree = $.fn.zTree.getZTreeObj("tree-two");
                    zTree.cancelEditName();
                    layer.msg('条件名称不能为空.', {icon: 2});
                }, 0);
                return false;
            }
            // 名字没变化　
            if (treeNode.name == newName) {
                return true;
            }
            // 判断是否存在
            if (termMap[newName] != null) {
                layer.msg('条件名称不能重复.', {icon: 2});
                return false;
            }
            // 删除map
            var map = termMap[treeNode.name];
            if (map != null) {
                map['termName']=newName;
                termMap[newName] = map;
                delete termMap[treeNode.name];
            }
            $("#termName").val(newName);
            return true;
        };

        function onRename(e, treeId, treeNode, isCancel) {
        };
        var newCount = zNodes.length + 1;

        // 添加子节点
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_" + treeNode.tId);
            if (btn) btn.bind("click", function () {
                if (!addNode(false, treeNode)) {
                    return;
                }
                addTermEvent();
                return false;
            });
        };

        // 添加节点
        function addNode(isParent, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            num++;
            if (num > 8) {
                alert('最多只能添加八个条件');
                return false;
            }
            var nodeId = newCount;
            var nodeName = "条件" + (newCount++);
            if (treeNode != null) {
                zTree.addNodes(treeNode, {id: nodeId, pId: treeNode.id, name: nodeName});
            } else {
                zTree.addNodes(null, {
                    id: nodeId,
                    pId: 0,
                    isParent: isParent,
                    name: nodeName
                });
            }
            // 放入termMap
            termMap[nodeName]={};
            return true;
        }

        // 添加父节点
        function add(e) {
            isParent = e.data.isParent;
            if (!addNode(isParent)) {
                return;
            }
            addTermEvent();
        };

        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_" + treeNode.tId).unbind().remove();
        };
        $('.js-add-fatherNode').bind("click", {isParent: true}, add);
        $(document).ready(function () {
            $.fn.zTree.init($("#tree-two"), setting, zNodes);
            $(".js-addParent").bind("click", {isParent: true}, add);
            var treeObj = $.fn.zTree.getZTreeObj("tree-two");
            treeObj.expandAll(true);
            addTermEvent();
        });
        function addTermEvent() {
            $("a[id^='tree']").each(function (index) {
                if ($._data($(this)[0], "events")) {
                    return;
                }
                $(this).on('click', function () {
                    // 保存之前的名称到map
                    pushCurrentNodeToMap();
                    // 赋值数据
                    initMapValue($(this));
                    $('#prop_set').show();
                });
            })
        }
    })

    $(function () {
        changeDatasourceType($('#datasource_type'), '${datasourceId!''}');
        //标签下拉
        $('#tag_selection').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            max_selected_options: 3,
            no_results_text: '没有找到该标签，您可以前往基础设置新增标签'
        });
    });

    //更新数据源类型需要更新数据源列表选择
    function changeDatasourceType(el, datasourceId) {
        var type = $(el).val();
        //静态数据源不需要选择
        if (type === '-1') {
            $('#data_set').parent().hide();
            $('#data_set_label').hide();
            $('#datasource_id').parent('div.filter-item').hide();
            $('#datasource_label').hide();
            return;
        }
        else if (type === '1') {
            $('#datasource_id').parent('div.filter-item').show();
            $('#datasource_label').show();
            $('#data_set_label').show();
            $('#data_set').parent().show().attr('placeholder', '<请在此插入SQL>');
            $('#term_datasource_id').parent('div.filter-item').hide();
            $('#term_datasource_label').hide();
        }
        else if (type === '2') {
            $('#data_set').parent().hide();
            $('#data_set_label').hide();
            $('#datasource_id').parent('div.filter-item').show();
            $('#datasource_label').show();
            $('#term_datasource_id').parent('div.filter-item').show();
            $('#term_datasource_label').show();
        }
        else if (type === '3') {
            $('#datasource_id').parent('div.filter-item').hide();
            $('#datasource_label').hide();
            $('#data_set_label').show();
            $('#data_set').attr('placeholder', '<请在此插入数据>').parent().show();
            $('#term_datasource_label').show();
            $('#term_datasource_id').parent('div.filter-item').show();
            return;
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/datasource/' + type,
            type: 'GET',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    //更新
                    var optionText = '';
                    $.each(val.data, function (n, e) {
                        var selected = '';
                        if (e.id === datasourceId) {
                            selected = 'selected';
                        }
                        optionText += '<option value="' + e.id + '"' + selected + '>' + e.name + '</option>';
                    });
                    $('#datasource_id').html(optionText);
                }
            }
        });
    }

    //更新数据源类型需要更新数据源列表选择
    function changeTermDatasourceType(el, datasourceId) {
        var type = $(el).val();
        //静态数据源不需要选择
        if (type === '-1') {
            return;
        }
        else if (type === '1') {
           $('#dataSet').parent().show().attr('placeholder', '<sql写法必须包含id和name select xxx as id，yyy as name from table………………..>')
        }
        else if (type === '2') {
           $('#dataSet').parent().hide();
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/datasource/' + type,
            type: 'GET',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    //更新
                    var optionText = '';
                    $.each(val.data, function (n, e) {
                        var selected = '';
                        if (e.id === datasourceId) {
                            selected = 'selected';
                        }
                        optionText += '<option value="' + e.id + '"' + selected + '>' + e.name + '</option>';
                    });
                    $('#term_datasource_id').html(optionText);
                }
            }
        });
    }

    function pushCurrentNodeToMap() {
        var termK = $('#termName').val();
        var currentNode = termMap[termK];
        if (currentNode != null) {
            var map = {};
            map['termName']=$('#termName').val();
            map['termKey']=$('#termKey').val();
            map['termColumn']=$('#termColumn').val();
            map['termDataSourceType']=$('#term_datasource_type').val();
            map['termDataSourceId']=$('#term_datasource_id').val();
            map['dataSet']=$('#dataSet').val();
            map['isFinalTerm']=$("input[name='isFinalTerm']:checked").val();
            map['isQuery']=$("input[name='isQuery']:checked").val();

            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            var sNodes = zTree.getSelectedNodes();
            if (sNodes.length > 0) {
                var node = sNodes[0].getParentNode();
                if(node != null){
                    map['parentNode']=node.name;
                }
            }
            termMap[termK]=map;
        }
    }

    function initMapValue(treeNode) {
        // 赋值当前数据
        $('#termKey').val('');
        $('#termName').val('');
        $('#termColumn').val('');
        $('#dataSet').val('');
        $('#term_datasource_id').empty();

        var termName = treeNode.attr('title');
        var tm = termMap[termName];
        $('#termName').val(termName);
        if (tm != null) {
            $('#termKey').val(tm['termKey']);
            $('#termColumn').val(tm['termColumn']);
            $('#dataSet').val(tm['dataSet']);
            if ($('#isFinalTerm1').val() == tm['isFinalTerm']) {
                $("#isFinalTerm0").removeAttr("checked");
                $("#isFinalTerm1").prop("checked",true);//选中
            } else {
                $("#isFinalTerm1").removeAttr("checked");
                $("#isFinalTerm0").prop("checked",true);//选中
            }

            if ($('#isQuery1').val() == tm['isQuery']) {
                $("#isQuery0").removeAttr("checked");
                $('#isQuery1').prop("checked",true);
            } else {
                $("#isQuery1").removeAttr("checked");
                $('#isQuery0').prop("checked",true);
            }
            // 赋值数据类型和数据源
            $('#term_datasource_type').val(tm['termDataSourceType']);
            if (tm['termDataSourceType'] != null) {
                changeTermDatasourceType($('#term_datasource_type'), tm['termDataSourceId']);
            }
        }
    }
    
    function saveChart(isSave, isFirst, isNext) {
        pushCurrentNodeToMap();
        if (!checkTermMap()) {
            return;
        }
        var data = {
            'id':'${chart.id!}',
            'dataSourceType':$('#datasource_type').val(),
            'dataSourceId':$('#datasource_id').val(),
            'dataSet':$('#data_set').val(),
            'orderType' : '${chart.orderType}',
            'templateId':'${template.id!}',
            'templatePath':$('#templatePath').val(),
            'templateFileName':$('#templateFileName').val(),
            'thumbnailPath':$('#thumbnailPath').val(),
            'thumbnailFileName':$('#thumbnailFileName').val(),
            'termDatabaseId':$('#term_datasource_id').val(),
            'remark':$('#remark').val(),
            'termMap' :  JSON.stringify(termMap)
        };

        if (!checkVal(data)) {
            return;
        }
        // 基础设置
        if (isNext) {
            nextStep(data);
            return;
        }
        if (!isSave) {
            data.name = $('#chart_name').val();
            data.databaseId = data.dataSourceId;
            var pForm = document.createElement("form");
            document.body.appendChild(pForm);
            $.each(data, function (key, value) {
                var i = document.createElement("input");
                i.type = "hidden";
                i.value = value;
                i.name = key;
                pForm.appendChild(i);
            });
            pForm.action="${request.contextPath}/bigdata/report/template/previewReport";
            pForm.target="_blank";
            pForm.method = "post";
            pForm.submit();
            return;
        }

        // 保存报表
        var index = layer.open({
            type: 1,
            title: '保存报表',
            shade: .5,
            shadeClose: true,
            btn: ['确定', '取消'],
            area: '410px',
            content: $('.layer-save'),
            yes: function () {
                var chartName = $('#chart_name').val();
                if (!chartName || $.trim(chartName) === '') {
                    tips('图表名称', '#chart_name');
                    return;
                }
                if ($.trim(chartName).length > 50) {
                    tips('图表名称最大长度为50个字符', '#chart_name');
                    return;
                }
                data.tags = $('#tag_selection').val();
                data.name = chartName;
                //ajax调用保存
                $.ajax({
                    url: '${request.contextPath}/bigdata/report/template/save',
                    data: data,
                    type: 'POST',
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            layer.msg(val.message, {icon: 2, time: 2000});
                        }
                        else {
                            layer.close(index);
                            $('#chart_id').val(val.data);
                            // createReportScreen();
                            layer.msg('保存成功', {icon: 1, time: 1000}, function () {
                                openModel('708224', '报表模版设置', 1, '${request.contextPath}/bigdata/report/template/index', '大数据管理', '数据管理', null, false);
                                layer.closeAll();
                            });
                        }
                    },
                    error: function () {
                        $('#tagsList').load('${request.contextPath}/bigdata/chart/layer/tags?chartId=' + $('#chart_id').val());
                    }
                });
            }
        });

        layer.style(index, {
            'border-radius': '15px'
        });
        $(document).find('#tag_selection').css({width: '300px', height: '36px'});
        $('#tag_selection').next().css({width: '300px', height: '36px'});
        $(document).find('.chosen-container-multi .chosen-choices').css('padding', '0px');
    }

    function nextStep(data) {
        data.name = $('#chart_name').val();
        data.databaseId = data.dataSourceId;
        $.post(_contextPath + '/bigdata/report/template/next', data, function (response) {
            try {
                var val = JOSN.parse(response);
                if (!val.success) {
                    //error
                }
            } catch (e) {
                // html
                $('#add-content').parent().html(response);
            }
        })
    }
    
    function checkTermMap() {
        var isTrue = true;
        // 校验termMap
        $.each(termMap, function (key, value) {
            if (value['termKey'] == null || value['termKey'] == '') {
                layer.msg(key + '条件唯一标识不能为空！', {icon: 2});
                isTrue = false;
                return;
            }
            if (value['parentNode'] != undefined && value['parentNode'] != '' && value['termColumn'] == '') {
                layer.msg(key + '条件及联字段不能为空！', {icon: 2});
                isTrue = false;
                return;
            }
            if (value['termDataSourceType'] == null) {
                layer.msg(key + '条件数据类型不能为空！', {icon: 2});
                isTrue = false;
                return;
            }
            if (value['termDataSourceId'] == null) {
                layer.msg(key + '条件数据源不能为空！', {icon: 2});
                isTrue = false;
                return;
            }
            if (value['termDataSourceType'] == '1') {
                if (value['dataSet'] == '') {
                    layer.msg(key + '条件数据集不能为空！', {icon: 2});
                    isTrue = false;
                    return;
                }
            }
        });
        return isTrue;
    }

    function checkVal(data) {
        var templatePath = $('#templatePath').val();
        if (!templatePath || $.trim(templatePath) === '') {
            $('.page-content').scrollTop(document.getElementById('file').offsetTop);
            tips('模版文件不能为空', '#file');
            return false;
        }
        var success = true;
        if (!data.dataSourceType || $.trim(data.dataSourceType) === ''
                || $.trim(data.dataSourceType) === '-1') {
            success = false;
            tips('数据源类型不能为空', '#datasource_type');
        } else {
            //检查datasourceId
            if ($.trim(data.dataSourceType) !== '3') {
                if (!data.dataSourceId || $.trim(data.dataSourceId) === '') {
                    success = false;
                    tips('数据源不能为空', '#datasource_id');
                }
                if (data.dataSourceType === '1' && (!data.dataSet || $.trim(data.dataSet) === '')) {
                    success = false;
                    tips('请输入sql', '#data_set');
                    window.scrollTo(0, document.getElementById('data_set').offsetTop);
                }
            } else {
                if (!data.dataSet || $.trim(data.dataSet) === '') {
                    success = false;
                    tips('静态数据不能为空', '#data_set');
                }
            }
            if (data.dataSourceType != '1' && data.termDatabaseId =='') {
                success = false;
                tips('条件数据源不能为空!', '#data_set');
            }
        }
        return success;
    }

    function tips(msg, key) {
        layer.tips(msg, key, {
            tipsMore: true,
            tips: 3,
            time: 2000
        });
    }

    //预览
    $('.js-check').on('click', function(e){
        e.preventDefault();
        saveChart(false, true);
    });

    function loadFile() {
        if (hasUploadSuc) {
            $("#templatePath").val($("#template-path").val());
            $("#templateFileName").val(fileName);
            $(".js-file-content").html(fileName);
        }
    }

    function loadPhotoFile() {
        if (hasUploadSuc) {
            $("#thumbnailPath").val($("#thumbnail-path").val());
            var fileName = $("#thumbnailFileName").val();
            if ($("#thumbnail-path").val() != '') {
                var path = $("#thumbnailPath").val() + "\\" + fileName;
                path = path.substring(path.indexOf('upload'));
                path = '${fileUrl!}' + '/'+ path;
                $(".js-pic").addClass('no-padding');
                $(".js-pic").empty().append('<img src="' +  path + '"/>');
                $(".js-pic").append('<input type="hidden" id="thumbnailFileName" name="thumbnailFileName" value="' + fileName + '">');
                $(".js-pic").css('border-style', 'none');
            }
        }
    }
</script>

<script type="text/javascript">

    $('#file').change(function () {
        $('.js-file-content').text(this.value.slice(12));
    });
</script>