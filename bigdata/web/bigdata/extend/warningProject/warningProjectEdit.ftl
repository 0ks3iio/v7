<script src="${request.contextPath}/bigdata/v3/static/plugs/jRange/jquery.range-min.js" type="text/javascript" charset="utf-8"></script>
<div class="box box-default rule-setup">
            <div class="form-horizontal" id="myForm">
                <div class="form-group">
                    <h3 class="col-sm-2 control-label no-padding-right bold">基本信息　</h3>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        项目名称　
                    </label>
                    <div class="col-sm-6">
                        <input type="text" maxlength="30" id="projectName" value="${warningProject.projectName!}" class="form-control js-file-name width-1of1" placeholder="请输入项目名称">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        开始生效时间　
                    </label>
                    <div class="col-sm-6">
                        <div class="input-group">
                            <input type="text" id="startTime" placeholder="请选择时间" value="${(warningProject.startTime?string('yyyy-MM-dd HH:mm'))!}" class="form-control" autocomplete="off">
                            <span class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        生效结束时间　
                    </label>
                    <div class="col-sm-6">
                        <div class="box-header-tool col-sm-4">
                            <label class="choice mr-10">
                                <input type="radio" name="timeType" value="2" <#if warningProject.isAllTime??><#if warningProject.isAllTime == 2>checked="checked"</#if></#if> class="wp" onclick="changeTimeType(this)">
                                <span class="choice-name"> 永久</span>
                            </label>
                            <label class="choice mr-10">
                                <input type="radio" name="timeType" id="setting" value="1" <#if warningProject.isAllTime??><#if warningProject.isAllTime == 1>checked="checked"</#if></#if> class="wp" onclick="changeTimeType(this)">
                                <span class="choice-name"> 自定义</span>
                            </label>
                        </div>
                        <div class="input-group col-sm-8" id="endTimeDiv">
                            <input type="text" placeholder="请选择时间" id="endTime" value="${(warningProject.endTime? string('yyyy-MM-dd HH:mm'))!}" class="form-control" autocomplete="off">
                            <span class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        定时任务　
                    </label>
                    <div class="col-sm-6">
                        <input type="text" maxlength="60" id="scheduleParam" value="${warningProject.scheduleParam!}" class="form-control js-file-name width-1of1" placeholder="请输入定时任务参数">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        预警级别　
                    </label>
                    <div class="col-sm-6">
                        <select name="warnLevel" id="warnLevel" class="form-control">
                            <option value="0" <#if warningProject.warnLevel?exists && warningProject.warnLevel == 0>selected</#if>>警告</option>
                            <option value="1" <#if warningProject.warnLevel?exists && warningProject.warnLevel == 1>selected</#if>>重要</option>
                            <option value="2" <#if warningProject.warnLevel?exists && warningProject.warnLevel == 2>selected</#if>>严重</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        预警结果类型　
                    </label>
                    <div class="col-sm-6">
                        <select name="warnResultType" id="warnResultType" class="form-control">
                            <option value="1" <#if warningProject.warnResultType?exists && warningProject.warnResultType == 1>selected</#if>>提醒</option>
                            <option value="2" <#if warningProject.warnResultType?exists && warningProject.warnResultType == 2>selected</#if>>跟踪处置</option>
                        </select>
                    </div>
                </div>
                <div class="form-group pt-50 pb-30">
                    <label class="col-sm-2 control-label">
                        <font style="color:red;">*</font>
                        预警结果时效　
                    </label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="effectiveDay" value="<#if warningProject.effectiveDay?exists>${warningProject.effectiveDay}<#else>5</#if>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        预警结果反馈方式　
                    </label>
                    <div class="col-sm-6">
                        <select name="feedbackType" id="feedbackType" class="form-control" onchange="selFeedbackType(this)">
                            <option value="1" <#if warningProject.feedbackType?exists && warningProject.feedbackType == 1>selected</#if>>指定提醒人</option>
                            <option value="2" <#if warningProject.feedbackType?exists && warningProject.feedbackType == 2>selected</#if>>回调API</option>
                        </select>
                    </div>
                </div>
                <div class="form-group" style="<#if warningProject.feedbackType?exists && warningProject.feedbackType == 2>display: none<#else>display: block</#if>" id="usersDiv">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        预警提醒人　
                    </label>
                    <div class="col-sm-6">
                        <input type="hidden" id="userIds" name="userIds" value="${userIds!}">
                        <input type="text" id="userNames" name="userNames" class="form-control" placeholder="请选择提醒人" readonly value="${userNames!}">
                    </div>
                </div>
                <div class="form-group" style="<#if warningProject.feedbackType?exists && warningProject.feedbackType == 2>display: block<#else>display: none</#if>" id="callbackApisDiv">
                    <label class="col-sm-2 control-label no-padding-right">
                        <font style="color:red;">*</font>
                        回调Api　
                    </label>
                    <div class="col-sm-6">
                        <textarea name="" rows="" cols="" class="width-1of1 border-ccc" id="callbackApis"
                                  placeholder="<请输入回调Api,多个用;隔开>">${warningProject.callbackApis!}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">ETL调度　</label>
                    <div class="col-sm-6">
                        <select name="jobId" id="jobId" class="form-control">
                            <option value="">--- 请选择(非必选) ---</option>
                            <#if jobList?exists && jobList?size gt 0>
                                <#list jobList as job>
                                    <option value="${job.id!}" <#if warningProject.jobId?exists && warningProject.jobId == job.id>selected</#if>>${job.name!}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <h4 class="col-sm-2 control-label no-padding-right bold no-margin">
                        <font style="color:red;">*</font>
                        预警规则　
                    </h4>
                    <div class="col-sm-6">
                        <div class="add-rules">
                            <#list ruleTypes as item>
                                <span class="add margin-bottom-15 addRootRule" groupId="1" onclick="addRule(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                            </#list>
                            <ul class="form-group all-rule no-margin">

                            </ul>
                            <ul hidden class="hide-rules">
                                <#list ruleTypes as type>
                    				<#assign key = type.ruleType?string>
                                    <div id="ruleType${key!}">
                                        <li class="rule clearfix">
	                                        <div class="col-sm-3 no-padding-left">
			                                    <select name="metaId" id="" ruletype="" class="form-control" onchange="getTableColumn(this,true)">
			                                        <#list metas as item>
			                                            <option value="${item.id}">${item.name!}</option>
			                                        </#list>
			                                    </select>
			                                </div>
                                            <div class="col-sm-3 no-padding-left">
                                                <select name="mdColumnId" id="" ruletype="" class="form-control" onchange="changeSelect(this)">
                                                </select>
                                            </div>
                                            <div class="col-sm-2 no-padding-left">
                                                <select name="symbolId" id="" class="form-control" onchange="changeSymbol(this)" placeholder="请选择">
                                                    <#list tagRuleSymbolMap[key] as item>
                                                        <option value="${item.id}">${item.symbolName!}</option>
                                                    </#list>
                                                </select>
                                            </div>
                                            <div class="col-sm-3 no-padding result-div">
                                                <input name="result" type="text"  class="form-control" placeholder="">
                                            </div>
                                            <i class="fa fa-times-circle"></i>
                                        </li>
                                        <li class="or">或者&nbsp;
                                            <#list ruleTypes as item>
                                                <span class="add" onclick="addRuleOr(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                            </#list>
                                        </li>
                                    </div>
                                </#list>
                                <div id="andHtml">
                                    <li class="and no-border">
                                        <span class="">并且</span>
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleAnd(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                                    </li>
                                </div>
                                <div id="orHtml">
                                    <span class="">或者</span>
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleOr(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                                </div>

                                <div id="orHtmlOut">
                                    <li class="or">或者&nbsp;
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleOr(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                                    </li>
                                </div>

                                <div id="andHtmlInner">
                                    <span class="">并且</span>
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleAnd(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                                </div>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-6 col-sm-offset-2">
                        <button class="btn btn-blue" type="button" id="saveBtn">保存</button>
                    </div>
                </div>
            </div>
        </div>
<input type="hidden" id="projectId" value="${warningProject.id!}">
<input type="hidden" id="profileCode" value="${profileCode!}">

<div class="layer layer-user-power">
    <div class="form-horizontal" id="myForm-three">
        <div class="form-group">
            <div class="col-sm-7">
                <p class="choose-num">选择提醒人&nbsp;</p>
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

<script type="text/javascript">
    $(function () {
        var startTime = laydate.render({
            elem: '#startTime',
            type:'datetime' ,
            format:'yyyy-MM-dd HH:mm',
            trigger: 'click',
            isInitValue: false,
            done: function (value, date, endD) {
                endTime.config.min={
                    year:date.year,
                    month:date.month-1,//关键
                    date:date.date,
                    hours:date.hours,
                    minutes:date.minutes,
                    seconds:date.seconds
                };
            }
        });

        var endTime = laydate.render({
            elem: '#endTime',
            type:'datetime' ,
            format:'yyyy-MM-dd HH:mm',
            trigger: 'click',
            isInitValue: false,
            done: function (value, date, endD) {
                startTime.config.max={
                    year:date.year,
                    month:date.month-1,//关键
                    date:date.date,
                    hours:date.hours,
                    minutes:date.minutes,
                    seconds:date.seconds
                }
            }
        });

        //数值范围选择
        $('#effectiveDay').jRange({
            theme: '',
            from: 1,
            to: 30,
            step: 1,
            scale: [1,5,10,15,20,25,30],
            format: '%s天',
            width: 300,
            showLabels: true,
            isRange:false
        });

        $("#userNames").on('click', function () {
            mTree.iniTree();
            usersLayer();
        });

        function usersLayer() {
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
                    showTeacherNameId();
                    layer.close(index);
                },
                end: function () {
                    destroyTree();
                    //恢复显示，以备下次显示不回出错
                    $('.tree_teacher_tab').removeClass('active');
                    $('#bb').removeClass('active');
                }
            });
            layer.style(index, {'overflow-y': 'auto'});
        }

        var num = 0;

        function destroyTree() {
            // mTree.unitTree.checkAllNodes(false);
            // mTree.teacherTree.checkAllNodes(false);
            $('div.choose-item').find('div.teacher').remove();
            num = 0
        }

        function showTeacherNameId() {
            var userArray = [];
            var nameArray = [];
            $('.choose-item .teacher').each(function () {
                userArray.push($(this).attr("id"));
                nameArray.push($(this).text());
            });
            if (userArray.length > 0) {
                var userIds = "";
                var userNames = "";
                $.each(userArray, function(key, val) {
                    userIds += "," + val;
                });
                $.each(nameArray, function(key, val) {
                    userNames += "," + val;
                });
                $("#userIds").val(userIds.substring(1));
                $("#userNames").val(userNames.substring(1));
            }
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
            var teacherIds = $("#userIds").val().split(",");
            var nodes = ${teacherArray};
            if (teacherIds.length > 0) {
                $.each(nodes, function(key, val) {
                    if (val.type == "teacher" && $.inArray(val.id,teacherIds) >= 0) {
                        val.checked = true;
                    }
                });
            }
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
        };

        mTree.doGetCurrentTree = function () {
            return mTree.teacherTree;
        };

        // mTree.iniTree();
    });


    function tips(msg, key) {
        layer.tips(msg, key, {
            tipsMore: true,
            tips: 3,
            time: 2000
        });
    }
   var isSubmit =false;
    $('#saveBtn').click(function () {
		if(isSubmit){
			return;
		}
        var projectName = $('#projectName').val();
        if ($.trim(projectName) == '') {
            tips('项目名称不能为空', '#projectName');
            return;
        }
        var startTime = $('#startTime').val();
        if ($.trim(startTime) == '') {
            tips('开始时间不能为空', '#startTime');
            return;
        }
        var scheduleParam = $('#scheduleParam').val();
        if ($.trim(scheduleParam) == '') {
            tips('定时任务参数不能为空', '#scheduleParam');
            return;
        }

        var timeType = $("input[name='timeType']:checked").val();
        var endTime = $('#endTime').val();
        if (timeType == '1' && $.trim(endTime) == '') {
            tips('结束时间不能为空', '#endTime');
            return;
        }

        if (timeType == '1' && startTime > endTime) {
            tips('结束时间不能小于开始时间', '#endTime');
            return;
        }

        var feedbackType = $("#feedbackType").val();
        if (feedbackType == 1) {
            if ($("#userIds").val() == "") {
                tips('请选择预警提醒人', '#userNames');
                return;
            }
        } else {
            if ($("#callbackApis").val() == "") {
                tips('请输入回调Api', '#callbackApis');
                return;
            }
        }

        var metaIdSet = new Set();
        $('.all-rule').find('select[name="metaId"]').each(function () {
            metaIdSet.add($(this).val());
        });
        if (metaIdSet.size === 0) {
            showLayerTips('error', '请编辑预警规则！', '');
            return;
        }
        if (metaIdSet.size > 1) {
            showLayerTips('error', '元数据必须一致！', '');
            return;
        }

        var isTrue = true;
        var list =[];
        var all_li = $('.all-rule').find('.rule');
        $.each(all_li, function (index, value) {
        	var metaId = $(value).find('select[name="metaId"]').val();
            if (metaId == null) {
                $('#addRuleDiv').scrollTop($(value).find('select[name="metaId"]').offset().top);
                tips("请先选择元数据", $(value).find('select[name="metaId"]'));
                isTrue = false;
                return;
            }
            var ruleName = $(value).find('select[name="mdColumnId"]').find("option:selected").text();
            var mdColumnId = $(value).find('select[name="mdColumnId"]').val();
            if (mdColumnId == null) {
                tips("请先维护规则属性", $(value).find('select[name="mdColumnId"]'));
                window.scrollTo(0, $(value).find('select[name="mdColumnId"]').offset().top);
                isTrue = false;
                return;
            }
            var symbolName = $(value).find('select[name="symbolId"]').find("option:selected").text();
            var symbolId = $(value).find('select[name="symbolId"]').val();
            if (symbolId == null) {
                tips("请先维护规则符号", $(value).find('select[name="symbolId"]'));
                window.scrollTo(0, $(value).find('select[name="symbolId"]').offset().top);
                isTrue = false;
                return;
            }
            var result =  $(value).find('input[name="result"],select[name="result"]').val();
            if (!$(value).find('input[name="result"],select[name="result"]').is(':hidden') && $.trim(result) == '') {
                tips(ruleName + "结果值不能为空!", $(value).find('input[name="result"],select[name="result"]'));
                window.scrollTo(0, $(value).find('input[name="result"],select[name="result"]').offset().top);
                isTrue = false;
                return;
            }
            var groupId =  $(value).find('input[name="groupId"]').val();
            var ruleType =  $(value).find('input[name="ruleType"]').val();
            var profileCode = $('#profileCode').val();
            var data = new Object();
            data.profileCode = profileCode;
            data.groupId = groupId;
            data.result = result;
            data.mdId = metaId;
            data.ruleType = ruleType;
            data.mdColumnId = mdColumnId;
            data.ruleName = ruleName;
            data.ruleSymbolName = symbolName;
            data.ruleSymbolId = symbolId;
            data.callbackApis = $('#callbackApis').val();
            list.push(data);
        });
        if (!isTrue) {
            return;
        }
		isSubmit = true;
        $.ajax({
            url: '${request.contextPath}/bigdata/warningProject/saveWarningProject',
            type: 'POST',
            data : {
                id : $('#projectId').val(),
                tagRules : JSON.stringify(list),
                projectName : projectName,
                startTime : startTime,
                isAllTime : timeType,
                endTime : endTime,
                scheduleParam : scheduleParam,
                callbackApis : $("#callbackApis").val(),
                warnLevel : $("#warnLevel").val(),
                warnResultType : $("#warnResultType").val(),
                effectiveDay : $("#effectiveDay").val(),
                feedbackType : $("#feedbackType").val(),
                userIds : $("#userIds").val(),
                jobId : $("#jobId").val()
            },
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    layer.msg('保存成功', {icon: 1, time: 1000}, function () {
                        var href = '${request.contextPath}/bigdata/warningProject/list';
                        routerProxy.go({
                            path: href,
                            level: 1,
                            name: '预警项目'
                        }, function () {
                            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
                        });
                    });
                }
                isSubmit = false;
            }
        });

    });

    function changeTimeType(e) {
        var value = $(e).val();
        if (value == '1') {
            $('#endTimeDiv').show();
        } else {
            $('#endTimeDiv').hide();
        }
    }
    
	function getTableColumn(obj,isChange,mdColumnId,resultval) {
		var metaId = $(obj).val();
	    $.ajax({
	        url: '${request.contextPath}/bigdata/userTag/meta/table/column',
	        type: 'POST',
	        data : {'metaId' : metaId},
	        dataType: 'json',
	        success: function (val) {
	            if (val.success) {
	            	var result = val.data;
	            	var html = '';
	            	for(i=0;i<result.length;i++){
	            		html+='<option value="'+result[i].id+'" remark=\''+result[i].dataDictionary+'\' holderstr="请填写'+result[i].name+'" >'+result[i].name+'</option>'
	            	}
	            	var $mdColumn = $(obj).parent().parent().find('select[name="mdColumnId"]');
	            	$mdColumn.html(html);
	            	if(result.length==0){
	            		var holder = $($mdColumn).find('option:selected').attr('holderstr');
	            		if(!holder)holder='';
				        var html = '<input name="result" type="text" value="" class="form-control" placeholder="'+holder+'">';
			        	$($mdColumn).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(html);
	            		changeSymbol($($mdColumn).parent().parent().find('select[name="symbolId"]'));
	            	}else if(isChange){
		            	changeSelect($mdColumn);
	            	}else{
	            		$mdColumn.val(mdColumnId);
	            		var re = $($mdColumn).find('option:selected').attr('remark');
				        if(re && re!='null' && re!='undefined' && re!=''){
				        	var rejson = JSON.parse(re); 
				        	var htmlstr = '<select name="result" id="" class="form-control" placeholder="请选择">';
				        	for(var p in rejson){
				        		htmlstr+='<option value="'+rejson[p]+'" >'+rejson[p]+'</option>';
				        	}
				        	htmlstr+='</select>';
				        	$($mdColumn).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(htmlstr);
		            	}
		            	var $result = $($mdColumn).parent().parent().find('input[name="result"],select[name="result"]');
	                    $result.val(resultval);
	            	}
	            } 
	        }
	    });
	}
    $(function(){
        if ($('#projectId').val() != '') {
            var isAllTime = '${warningProject.isAllTime!}';
            if (isAllTime == '2') {
                $('#endTimeDiv').hide();
            }
            $.ajax({
                url: '${request.contextPath}/bigdata/warningProject/getTagRuleRelationByProjectId',
                type: 'POST',
                data : {id:$('#projectId').val()},
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        layer.msg(val.message, {icon: 2});
                    }
                    else {
                        $('.addRootRule').show();
                        $('.all-rule').empty();
                        var gId = 1;
                        $.each(val.data, function (i, v) {
                            // 遍历或者
                            $.each(v, function (j, value) {
                                gId = value.groupId;
                                $('#ruleType' + value.ruleType).find('.rule').find('input[name="groupId"]').remove();
                                $('#ruleType' + value.ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + value.groupId + "'><input type='hidden' name='ruleType' value='" + value.ruleType + "'>");
                                var str = $('#ruleType' + value.ruleType).html();
                                //或者,添加规则
                                $('.all-rule').append(str);
                                var $tagMetaSelect = $('.all-rule').find('select[name="metaId"]').last();
                                var $symbolSelect = $('.all-rule').find('select[name="symbolId"]').last();
                                $tagMetaSelect.val(value.mdId);
                                getTableColumn($tagMetaSelect,false,value.mdColumnId,value.result);
                                $symbolSelect.val(value.ruleSymbolId);
                                changeSymbol($symbolSelect);
                                if ((j+1) < v.length) {
                                    $('.all-rule').find('.or').last().remove();
                                    $('.all-rule').append("<li class=\"or\">或者&nbsp;</li>");
                                } else {
                                    $('.all-rule').find('.or').last().find('.add').attr('groupId', value.groupId);
                                }
                            });
                            if (i == Object.keys(val.data).length) {
                                //并且,添加规则
                                $('#andHtml').find('.add').attr('groupId', gId + 1);
                                $('.all-rule').append($('#andHtml').html());
                            } else {
                                // 并且
                                $('.all-rule').append("<li class=\"and no-border\"><span class=\"\">并且</span></li>");
                            }
                        });
                        if ($('.all-rule li').length > 0) {
                            $('.addRootRule').hide();
                        }
                    }
                }
            });
        } else {
            $("#setting").attr('checked', 'checked');
        }
        //删除规则
        $('.add-rules').on('click','.fa-times-circle',function(){
            var $dad=$(this).parent();
            var $nextOne=$dad.next();
            var $nextTwo=$dad.next().next();
            var $prev=$dad.prev();
            if($('.add-rules ul.all-rule li').length==3){
                $('ul.all-rule').empty();
                $('.add-rules>.add').addClass('js-add-start').removeClass('useless');
            }else if($nextTwo.hasClass('and')&&$prev.hasClass('and')||$nextTwo.hasClass('and')&&$dad.index()==0){
                $dad.remove();
                if ($nextOne.find('.add').length == 0) {
                    $nextOne.remove();
                } else {
                    $prev.remove();
                }
                if ($nextTwo.hasClass('and')) {
                    $nextOne.remove();
                    $prev.remove();
                    if ($nextTwo.prev().html() == null) {
                        $nextTwo.remove();
                    }
                } else {
                    $nextTwo.remove();
                }
            }else{
                $dad.remove();
                if ($nextOne.find('.add').length == 0) {
                    $nextOne.remove();
                } else {
                    $prev.remove();
                }
            };
            var groupId = $dad.find("input[name='groupId']").val();
            $('#orHtml').find('.add').attr('groupId', groupId);

            var groupId1 = $('.all-rule').find('.rule').last().find("input[name='groupId']").val();
            // 只有一个时
            $('#andHtmlInner').find('.add').attr('groupId', parseInt(groupId1) + 1);
            var $last=$('.all-rule').find('.and').last();
            $last.empty().html($('#andHtmlInner').html());
            if ($('.all-rule li').length == 0) {
                $('.addRootRule').show();
            }
        });
		$('.hide-rules').find('.rule').find('select[name="metaId"]').each(function(){
            getTableColumn(this,true);
		})
    });

    function changeSelect(e) {
        var remark = $(e).find('option:selected').attr('remark');
        if(remark && remark!='null' && remark!='undefined' && remark!=''){
        	var obj = JSON.parse(remark); 
        	var html = '<select name="result" id="" class="form-control" placeholder="请选择">';
        	for(var p in obj){
        		html+='<option value="'+obj[p]+'" >'+obj[p]+'</option>';
        	}
        	html+='</select>';
        	$(e).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(html);
        }else{
	        var holder = $(e).find('option:selected').attr('holderstr');
	        if(!holder)holder='';
	        var html = '<input name="result" type="text" value="" class="form-control" placeholder="'+holder+'">';
        	$(e).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(html);
        }
        changeSymbol($(e).parent().parent().find('select[name="symbolId"]'));
    }

    function changeSymbol(e) {
        var text = $(e).find('option:selected').text();
        if (text == '是空值' || text == '不是空值') {
            $(e).parent().parent().find('input[name="result"],select[name="result"]').val('').hide();
        } else {
            $(e).parent().parent().find('input[name="result"],select[name="result"]').show();
        }
    }

    function addRule(e, ruleType) {
        var groupId = $(e).attr('groupId');
        $('#ruleType' + ruleType).find('.rule').find('input[name="groupId"]').remove();
        $('#ruleType' + ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + groupId + "'><input type='hidden' name='ruleType' value='" + ruleType + "'>");
        $('#ruleType' + ruleType).find('.or').find('.add').attr('groupId', groupId);
        // placeholer
        changeSelect($('#ruleType' + ruleType).find('select[name="mdColumnId"]'));
        var str = $('#ruleType' + ruleType).html();
        //添加规则
        $('#andHtml').find('.add').attr('groupId', parseInt(groupId) + 1);
        $('.all-rule').append(str).append($('#andHtml').html());
        $('.add-rules').removeClass('js-add-start').addClass('useless');
        $('.addRootRule').hide();
    }

    function addRuleAnd(e, ruleType) {
        var groupId = $(e).attr('groupId');
        $('#ruleType' + ruleType).find('.rule').find('input[name="groupId"]').remove();
        $('#ruleType' + ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + groupId + "'><input type='hidden' name='ruleType' value='" + ruleType + "'>");
        $('#ruleType' + ruleType).find('.or').find('.add').attr('groupId', groupId);
        changeSelect($('#ruleType' + ruleType).find('select[name="mdColumnId"]'));
        var str = $('#ruleType' + ruleType).html();
        //并且,添加规则
        $('#andHtml').find('.add').attr('groupId', parseInt(groupId) + 1);
        $('.all-rule').append(str).append($('#andHtml').html());
        $('.add-rules').removeClass('js-add-start').addClass('useless');
        $(e).parent().empty().html("<span class=\"\">并且</span>");
    }

    function addRuleOr(e, ruleType) {
        $('#ruleType' + ruleType).find('.rule').find('input[name="groupId"]').remove();
        $('#ruleType' + ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + $(e).attr('groupId') + "'><input type='hidden' name='ruleType' value='" + ruleType + "'>");
        $('#ruleType' + ruleType).find('.or').find('.add').attr('groupId', $(e).attr('groupId'));
        changeSelect($('#ruleType' + ruleType).find('select[name="mdColumnId"]'));
        var str = $('#ruleType' + ruleType).html();
        //或者,添加规则
        $(e).parent('.or').after(str);
        $(e).parent().empty().html("或者&nbsp;");
    }

    function selFeedbackType(objThis) {
        var feedbackType = $(objThis).val();
        if (feedbackType == 1) {
            $("#usersDiv").attr("style","display:block");
            $("#callbackApisDiv").attr("style","display:none");
            $("#callbackApis").val("");
        } else {
            $("#usersDiv").attr("style","display:none");
            $("#callbackApisDiv").attr("style","display:block");
            $("#userIds").val("");
            $("#userNames").val("");
        }
    }
</script>