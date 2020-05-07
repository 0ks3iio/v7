<div class="col-xs-12">
    <div class="box box-default scroll-height">
        <div class="form-horizontal" id="myForm-two">
            <div class="form-group">
                <label class="col-sm-4 control-label text-left padding-left-25">${dataModel.name!}</label>
                <div class="col-sm-8 text-right">
                    <button class="btn btn-lightblue js-add-kanban" id="addUserDatasetBtn" modelId="${dataModel.id!}">添加数据集</button>
                </div>
            </div>
        </div>

        <div class="table-container">
            <div class="table-container-body subscription">
                <table class="table table-striped hover-31eeff4a">
                    <thead>
                    <tr>
                        <th class="text-left">数据集名称</th>
                        <th>用户</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if modelDatasets?? && modelDatasets?size gt 0>
                        <#list modelDatasets as item>
                        <tr>
                            <td class="">${item.dsName!}</td>
                            <#if item.orderUsers?default("")=="">
                                        <td class="color-999"><p>暂无授权用户</p></td>
                            <#else>
                                        <td class="unit-self"><p>${item.orderUsers!}</p></td>
                            </#if>
                            <td>
                                <a href="javascript:void(0);" class="unfold">展开</a><span class="tables-line">|</span>
                                <a href="javascript:void(0);" class="single-authorization"  id="${item.id!}">授权</a><span class="tables-line">|</span>
                                <a href="javascript:void(0);" class="remove" onclick="editUserDataset('${item.id!}')">修改</a><span class="tables-line">|</span>
                                <a href="javascript:void(0);" class="remove"  onclick="deleteUserDataset('${item.id!}', '${item.dsName!}')">删除</a>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="layer layer-userDataset-add">
    <div id="userDatasetAddDiv">

    </div>
</div>
<div class="layer layer-user-power">
    <div class="form-horizontal" id="myForm-three">
        <div class="form-group">
            <div class="col-sm-7">
                <p class="choose-num">选择授权对象&nbsp;</p>
                <!--<p class="no-padding-right bold">选择授权对象&nbsp;</p>　-->
                <div class="bs-callout bs-callout-danger">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#aa" data-toggle="tab">本单位用户</a>
                        </li>
                    </ul>
                    <div class="tab-content tree-wrap tree-tab width-1of1">
                        <div class="row no-margin">
                            <div class="filter-item col-sm-8 no-margin-right">
                                <div class="pos-rel pull-left width-1of2">
                                    <input id="tree_search_keywords" type="text"
                                           class="typeahead scrollable form-control width-1of1" autocomplete="off"
                                           data-provide="typeahead" placeholder="请输入关键词">
                                </div>
                                <div class="input-group-btn">
                                    <button id="tree_search" type="button" class="btn btn-default">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="col-sm-2 switch-button text-right">
                                <span>是否级联:</span>
                            </div>
                            <div class="col-sm-2 switch-button">
                                <input id="tree_cascade" name="switch-field-1" class="wp wp-switch" type="checkbox">
                                <span class="lbl"></span>
                            </div>
                        </div>
                        <div class="tab-pane active" id="aa">
                            <ul id="treeDemo-one" class="ztree"></ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-sm-5">
                <div class="bs-callout bs-callout-danger">
                    <p class="choose-num">已选（<span>0</span>）</p>
                    <div class="choose-item">
                        <div class="no-data">
                        <#--<img src="../images/big-data/no-choice.png"/>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>
    $(function () {

        $('#addUserDatasetBtn').click(function () {
            editUserDataset('');
        });

        //表格
        $('.unfold').each(function () {
            var $unitSelf = $(this).parent().siblings('.unit-self').find('p').text();
            var $father = $(this).parent().parent();
            var str = '<tr class="none"><td class="text-right" colspan="1">本单位用户：</td><td colspan="4" style="color: #00CCE3">' + $unitSelf + '</td></tr><tr class="none"></tr>';
            $father.after(str);
        });
        $('.unfold').click(function () {
            var $father = $(this).parent().parent();
            var $text = $(this).text();
            $father.next().toggle();
            $father.next().next().toggle();
            if ($text == '展开') {
                $(this).text('收起');
                $(this).css('color', '#000')
            } else {
                $(this).text('展开');
                $(this).css('color', '#00CCE3')
            }
        });

        //单个修改授权
        $('.single-authorization').on('click', function () {
            var modelDatasetId = $(this).attr('id');
            mTree.authorizationId = modelDatasetId;
            mTree.iniTree();
            authorizationLayer(modelDatasetId);
        });

        function authorizationLayer(modelDatasetId) {
            var index = layer.open({
                type: 1,
                title: '批量授权',
                shade: .5,
                shadeClose: true,
                closeBtn: 1,
                btn: ['确定', '取消'],
                area: ['70%', 'auto'],
                content: $('.layer-user-power'),
                yes: function (index) {
                    authorization(modelDatasetId);
                    layer.close(index);
                },
                end: function () {
                    destroyTree();
                    //恢复显示，以备下次显示不回出错
                    $('.tree_teacher_tab').removeClass('active');
                    $('#bb').removeClass('active');
                }
            });
            layer.style(index, {
                'overflow-y': 'auto'
            });
        }

        var num = 0;

        function destroyTree() {
            // mTree.unitTree.checkAllNodes(false);
            // mTree.teacherTree.checkAllNodes(false);
            $('div.choose-item').find('div.teacher').remove();
            num = 0
        }

        function authorization(modelDatasetId) {
            var userArray = [];
            $('.choose-item .teacher').each(function () {
                userArray.push($(this).attr("id"));
            });
            httpInvoker.authorization(modelDatasetId, userArray, function () {
                var modelId = $('#addUserDatasetBtn').attr('modelId');
                $('.page-content').load(_contextPath + '/bigdata/model/user/list?modelId=' + modelId);
            });
        }


        $('.tree-wrap').height($('.layer-power').height());
        $('.choose-item').height($('.tree-wrap').height() + 44 + 20);
        $('.no-data').css('line-height', ($('.choose-item').height() - 2) + 'px');
        //已选
        $('.choose-item').on('click', '.fa-times-circle', function () {
            $(this).parent().remove();
            var tId = $(this).parent().attr('tid');
            if ($(this).parent().hasClass('unit')) {
                mTree.unSelectTreeNode('unit', tId);
            } else {
                mTree.unSelectTreeNode('teacher', tId);
            }
            num--;
            $('.choose-num span').text(num);
            if (num == 0) {
                $('.no-data').show()
            }
        });
        //搜索
        $('#tree_search').on('click', function () {
            var keywords = $('#tree_search_keywords').val();
            var matchKeywordsTreeNodeArray = mTree.doGetCurrentTree().getNodesByParamFuzzy('name', keywords);
            //匹配
            if (matchKeywordsTreeNodeArray.length > 0) {
                mTree.doGetCurrentTree().selectNode(matchKeywordsTreeNodeArray[0]);
            }
        });
        //是否级联
        $('#tree_cascade').on('click', function () {
            var cascade = $(this).is(':checked');
            if (cascade) {
                mTree.teacherTree.setting.check.chkboxType = {'Y': 's', N: 's'};
            } else {
                mTree.teacherTree.setting.check.chkboxType = {'Y': '', N: ''};
            }
        });

        var httpInvoker = {};

        /**
         * invoke layer to show http request error
         * but not show real error, only tell user
         * network is error
         */
        httpInvoker.httpInvokerError = function () {
            layer.msg("网络异常！", {icon: 5});
        };

        /**
         * 授权
         */
        httpInvoker.authorization = function (modelDatasetId, userArray, success) {
            $.ajax({
                url: _contextPath + '/bigdata/model/user/authorization',
                type: 'POST',
                dataType: 'json',
                data: {
                    modelId:$('#addUserDatasetBtn').attr('modelId'),
                    modelDatasetId: modelDatasetId,
                    orderUsers: userArray
                },
                success: function (response) {
                    if (response.success) {
                        showLayerTips('success','授权成功!','t', function () {
                            if (typeof success === 'function') {
                                success();
                            }
                        });
                    } else {
                        layer.msg(response.message ? "授权失败" : response.message, {icon: 2});
                    }
                },
                error: function (jqXHR, status, errorThrown) {
                    httpInvoker.httpInvokerError();
                }
            });
        };

        httpInvoker.doGetTree = function (isTeacher, success) {
            $.ajax({
                url: _contextPath + '/bigdata/model/user/getAllUser',
                dataType: 'json',
                data: {
                    "modelDatasetId": mTree.authorizationId //批量操作无法涵盖所有的已经授权的图表的状态,单个授权所需
                },
                type: 'GET',
                success: function (response) {
                    if (response.success) {
                        if (typeof success === 'function') {
                            success(JSON.parse(response.data));
                        }
                        else {
                            console.log("error")
                        }
                    }
                },
                error: function (jQueryXMLHttpRequest, textStatus, errorThrown) {
                    //网络错误
                    httpInvoker.httpInvokerError();
                }
            })
        };

        //tree
        var mTree = {};
        mTree.setting = {
            view: {
                selectedMulti: false
            },
            check: {
                enable: true,
                chkStyle: "checkbox",
                //是否级联
                chkboxType: {'Y': '', 'N': ''}
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {},
            edit: {
                enable: false
            }
        };
        //当选择单位或者教师，dom操作

        mTree._treeOnCheck = function (event, treeId, treeNode, type) {
            type = 'teacher';
            var id = treeNode.id;
            var tId = treeNode.tId;
            if (mTree.doGetCurrentTree().setting.check.chkboxType.Y === '') {
                if (treeNode.checked && !(treeNode.isParent && type === 'teacher')) {
                    //xuanzhong
                    mTree.addSelectedTreeNodeText2ChooseItem(tId, type, id, treeNode.name);
                    num++;
                } else {
                    if ($('#' + id).hasClass(type)) {
                        $('#' + id).remove();
                        num--;
                    }
                }
            } else {
                /*
                 * 因为ZTree 一旦初始化之后后续对setting的修改并不会起作用
                 * 所以当用户选择级联操作的时候需要额外的利用ZTree的 API勾选子节点
                 */
                var array = [];

                //获取当前的treeId和级联数据
                if (treeNode.checked) {
                    mTree.doGetCurrentTree().checkNode(treeNode, true, true, false);
                    array = mTree.doGetCurrentTree().getCheckedNodes(true);
                    // add but need checked
                    var $add_dom;
                    for (var i in array) {
                        $add_dom = $('#' + array[i].id);
                        if ($add_dom.length <= 0 && !(array[i].isParent && type === 'teacher')) {
                            mTree.addSelectedTreeNodeText2ChooseItem(array[i].tId, type, array[i].id, array[i].name);
                            num++;
                        }
                    }

                } else {
                    mTree.doGetCurrentTree().checkNode(treeNode, false, true, false);
                    array = mTree.doGetCurrentTree().getCheckedNodes(false);
                    var $delete_dom;
                    for (var t in array) {
                        $delete_dom = $('#' + array[t].id);
                        if ($delete_dom.length > 0 && $delete_dom.hasClass(type)) {
                            $delete_dom.remove();
                            num--;
                        }
                    }
                }
            }
            $('.choose-num span').text(num);
        };

        mTree.addSelectedTreeNodeText2ChooseItem = function (tId, type, id, text, hide) {
            var str = '<div tid="' + tId + '" class="' + type + '" id="' + id + '">' + text + '<i class="fa fa-times-circle"></i></div>';
            $('.no-data').hide();
            $('.choose-item').append(str);
        };
        mTree.addSelectedTreeNodeText2ChooseItemHide = function (tId, type, id, text) {
            var str = '<div tid="' + tId + '" class="' + type + '" id="' + id + '" style="display: none;">' + text + '<i class="fa fa-times-circle"></i></div>';
            $('.no-data').hide();
            $('.choose-item').append(str);
        };

        //递归获取所有被选中的子节点
        mTree._getAllChildren = function (treeNode, array) {
            if (treeNode.isParent) {
                var childrenNodes = treeNode.children;
                if (childrenNodes) {
                    for (var i = 0; i < childrenNodes.length; i++) {
                        array.push(childrenNodes[i]);
                        mTree._getAllChildren(childrenNodes[i], array);
                    }
                }
            }
        };

        mTree.unSelectTreeNode = function (type, tId) {
            mTree.teacherTree.checkNode(mTree.teacherTree.getNodeByTId(tId), false, false, false);
        };

        mTree.iniTree = function () {
            $('#tree_cascade').prop('checked', false);
            httpInvoker.doGetTree(true, function (nodes) {
                mTree.setting.callback.onCheck = function (event, treeId, treeNode) {
                    mTree._treeOnCheck(event, treeId, treeNode, 'teacher');
                };
                mTree.teacherTree = $.fn.zTree.init($('#treeDemo-one'), mTree.setting, nodes);
                var array = mTree.teacherTree.transformToArray(mTree.teacherTree.getNodes());
                for (var t in array) {
                    var node = array[t];
                    if (!node.chkDisabled && node.checked) {
                        num++;
                    }
                    if (node.checked) {
                        mTree.addSelectedTreeNodeText2ChooseItem(node.tId, 'teacher', node.id, node.name, node.chkDisabled);
                    }
                }
                $('.choose-num span').text(num);
            });
        };
        mTree.doGetCurrentTree = function () {
            return mTree.teacherTree;
        };
        //real send http request to Get tree data
        mTree.authorizationId = "";
        mTree.iniTree();
    });

    function editUserDataset(id) {
        var modelId = $('#addUserDatasetBtn').attr('modelId');
        $.ajax({
            url: '${request.contextPath}/bigdata/model/user/editUserDataset',
            type: 'POST',
            data: {id: id, modelId: modelId},
            dataType: 'html',
            success: function (response) {
                $('#userDatasetAddDiv').empty().append(response);
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增数据集' : '修改数据集',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#dsName').val() == "") {
                    layer.tips("不能为空", "#dsName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#dsConditionSql').val() == "") {
                    layer.tips("不能为空", "#dsConditionSql", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/model/user/saveUserDataset",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error', data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            $('#userDatasetAddDiv').empty();
                            $('.page-content').load(_contextPath + '/bigdata/model/user/list?modelId=' + modelId);
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#userDatasetForm").ajaxSubmit(options);
            },
            area: ['650px', '350px'],
            content: $('.layer-userDataset-add')
        });
    }
    
    function deleteUserDataset(id, name) {
        var title = '确定删除数据集【' + name + "】？";
        var modelId = $('#addUserDatasetBtn').attr('modelId');
        layer.confirm(title, {btn: ['确定', '取消'], title: '删除数据集', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/model/user/deleteUserDataset',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message);
                    }
                    else {
                        showLayerTips('success','保存成功!','t');
                        $('.page-content').load(_contextPath + '/bigdata/model/user/list?modelId=' + modelId);                    }
                }
            });
        });
    }
</script>