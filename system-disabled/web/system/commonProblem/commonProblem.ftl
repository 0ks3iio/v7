<script>
    _contextPath = "${request.contextPath?default("")}";
</script>

<script type="text/javascript" charset="utf-8" src="${resourceUrl}/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${resourceUrl}/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${resourceUrl}/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${resourceUrl}/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${resourceUrl}/ueditor/themes/iframe.css"/>
<div class="main-content">
    <div class="page-tab-wrap clearfix">
        <a class="btn-tab-scroll left js-pageTabLeft" href="#">
            <i class="wpfont icon-arrow-left"></i>
        </a>
        <div class="page-tabs js-pageTabs">
            <div class="page-tabs-container js-pageTabsContainer">
                <a class="js-pageTab active" href="">7选3排课系统<i class="wpfont icon-close"></i></a>
            </div>
        </div>
        <a class="btn-tab-scroll right js-pageTabRight" href="#">
            <i class="wpfont icon-arrow-right"></i>
        </a>
    </div>
    <div class="main-content-inner">
        <div class="page-content">
            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="box box-default">
                        <div class="box-body">
                            <h3 class="no-margin">常见问题管理</h3>
                            <hr />
                            <div class="filter filter-f16">
                                <div class="filter-item">
                                    <span class="filter-name">应用名称：</span>
                                    <div class="filter-content">
                                        <select name="serverCode" id="serverCodeSelect" class="form-control">
                                        <#if serverList??>
                                            <#list serverList as server>
                                                <option value="${server.code}" id="serverCodeSel" <#if severCode?exists && severCode==server.code >selected</#if>>${server.serverName}</option>
                                            </#list>
                                        </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="pull-right">
                                    <button class="btn btn-blue js-addClassify">新增分类</button>
                                </div>
                            </div>
                            <div class="row">
                            <#if typeList??>
                                <#list typeList as type>
                                    <div class="col-md-12">
                                        <div class="base-border-line">
                                            <h3 class="box-title" data-i="${type.id}">${type.name}</h3>
                                            <div class="pull-right">
                                                <button class="btn btn-sm btn-blue js-removeClassify" data-i="${type.id}">删除分类</button>
                                                <button class="btn btn-sm btn-blue js-changeClassify">修改分类</button>
                                                <button class="btn btn-sm btn-green js-addFaq">新增问题</button>
                                            </div>
                                        </div>
                                        <table class="table table-bordered table-striped table-hover no-margin">
                                            <thead>
                                            <tr>
                                                <th class="text-center" width="40%">问题</th>
                                                <th class="text-center" width="30%">时间</th>
                                                <th class="text-center" width="30%">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                                <#if typeAndproblems??&&typeAndproblems[type.getId()]??>
                                                    <#list typeAndproblems[type.getId()] as problem>
                                                    <tr>
                                                        <td>${problem.question}</td>
                                                        <td class="text-center">${problem.modifyTime}</td>
                                                        <td class="text-center">
                                                            <a href="#" class="table-btn color-blue js-changeFaq" data-i="${problem.id}" data-q="${problem.question}" data-a="${problem.answer}">修改</a>
                                                            <a href="#" class="table-btn color-red delFag" data-i="${problem.id}">删除</a>
                                                        </td>
                                                    </tr>
                                                    </#list>
                                                </#if>
                                            </tbody>
                                        </table>
                                    </div>
                                </#list>
                            </#if>
                            </div>
                        </div>
                    </div>

                    <!-- PAGE CONTENT ENDS -->
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content-inner -->
</div><!-- /.main-content -->

<div class="layer layer-classify" style="display: none;">
    <form class="form-horizontal" role="form">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding" for="classify-name">新增分类</label>
            <div class="col-sm-7">
                <input type="text" value="" placeholder="分类名称不超过20个字符" class="form-control" id="classify-name">
            </div>
            <div class="col-sm-2"></div>
        </div>
    </form>
</div>

<div class="layer layer-faq" style="display: none;">
    <form class="form-horizontal" role="form" id="questionForm">
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding" for="faq-title">问题标题</label>
            <div class="col-sm-9">
                <input type="text" name="question" value="" placeholder="不超过30个字" class="form-control" id="faq-title">
            </div>
            <div class="col-sm-1"></div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding" for="faq-content">问题答案</label>
            <div class="col-sm-9">
                <textarea name="content" id="faq-content" type="text/plain">
                </textarea>
            </div>
            <div class="col-sm-1"></div>
        </div>
        <input value="${serverCode!!}" name ="serverCode" type="hidden">
        <input value="" name ="typeId" id="formTypeId" type="hidden">
        <input value="" name ="id" id="formProblemId" type="hidden">
    </form>
</div>

<script>
    $(function(){
        var ue =getEditor();
        function getEditor(){
            UE.delEditor('faq-content');
            var ue = UE.getEditor('faq-content',{
                //设置工具栏
                toolbars: [
                    ['bold','indent','justifyleft','justifyright','justifycenter','justifyjustify','italic','simpleupload','forecolor','fontfamily',
                        'fontsize']
                ],
                //设置宽高
                initialFrameWidth : 460,
                initialFrameHeight: 200,
                autoHeightEnabled: false,
                elementPathEnabled:false,
                maximumWords: 2000
            });
            return ue;
        }
        $("#serverCodeSel").on("change",returnIndexPage);
        // 新增分类
        $('.js-addClassify').click(function(){
            $('.layer-classify').find('#classify-name').val("");
            layer.open({
                type: 1,
                shade: .5,
                title: ['新增分类','font-size:20px;'],
                area: '420px',
                btn: ['保存','取消'],
                btnAlign: 'c',
                content: $('.layer-classify'),
                success: function(layero){
                    //重新设置保存按钮class属性
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').attr({class:'layui-layer-btn0 addTypeBtn'});
                    //绑定保存按钮事件
                    $('.addTypeBtn').click(addType);
                }
            });
        });
        //删除分类
        $('.js-removeClassify').click(function(){
            id =$(this).data('i');
            var $problem = $(this).parents('.col-md-12').find('tbody');
            layer.confirm('确定要删除分类？', {
                btn: ['是','否'], //按钮
                shade: false //不显示遮罩
            }, function(){
                if($problem.children().length > 0){
                    layer.msg('请先删除问题', {icon: 2});
                    return;
                }

                $.ajax({
                    url:"${request.contextPath}/system/problem/removeProblemType",
                    type:"post",
                    data:{
                        'id':id
                    },
                    dataType: "json",
                    success:function(result){
                        showMsgSuccess(result.msg,"提示",function(index){
                            layer.close(index);
                            if(result.success){
                                returnIndexPage();
                            }
                        });
                    }
                });
                //removeProblemType
                layer.msg('删除成功', {icon: 1});
            });
        });
        //修改分类
        $('.js-changeClassify').click(function(){
            var txt=$(this).parent('div').prev('.box-title').text();
            var typeId = $(this).parent('div').prev('.box-title').data("i");
            $('.layer-classify').find('#classify-name').val(txt);
            layer.open({
                type: 1,
                shade: .5,
                title: ['修改分类','font-size:20px;'],
                area: '420px',
                btn: ['保存','取消'],
                btnAlign: 'c',
                content: $('.layer-classify'),
                success: function(layero){
                    //重新设置保存按钮class属性
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').attr({class:'layui-layer-btn0 modifyTypeBtn'});
                    //绑定保存按钮事件
                    $('.modifyTypeBtn').on("click",{typeId:typeId},modifyType);
                }
            });
        });
        // 新增问题
        $('.js-addFaq').click(function(){
            var typeId=$(this).parent('div').prev('.box-title').data('i');
            $('#formTypeId').val(typeId);
            $('#formProblemId').val('');
            $('#faq-title').val('');
            //清空编辑器内容
            ue =getEditor();
            //判断ueditor 编辑器是否创建成功
            ue.addListener("ready", function () {
                // editor准备好之后才可以使用
                ue.setContent('');
            });
            layer.open({
                type: 1,
                shade: .5,
                title: ['新增','font-size:20px;'],
                area: '660px',
                zIndex:'100',
                btn: ['保存','取消'],
                btnAlign: 'c',
                content: $('.layer-faq'),
                success: function(layero){
                    //重新设置保存按钮class属性
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').attr({class:'layui-layer-btn0 addProblemBtn'});
                    //绑定保存按钮事件
                    $('.addProblemBtn').on("click",{ue:ue},addProblem);
                }
            });
        });
        // 修改问题
        $('.js-changeFaq').click(function(){
            var typeId = $(this).parents('table').prev('.base-border-line').find('.box-title').data('i');
            var filePath = $(this).data('a');
            var answer ="";
            var $question = $(this).data('q');
            var $problemId = $(this).data('i');
            //异步获取答案内容
            $.ajax({
                url:"${request.contextPath}/system/problem/getContent",
                type:"post",
                data:{
                    'filePath':filePath
                },
                dataType: "json",
                success:function(result){
                    if(result.success){
                        answer = result.msg;
                        $('#formTypeId').val(typeId);
                        $('#formProblemId').val($problemId);
                        $('#faq-title').val($question);
                        ue =getEditor();
                        //判断ueditor 编辑器是否创建成功
                        ue.addListener("ready", function () {
                            // editor准备好之后才可以使用
                            ue.setContent(answer);
                        });
                        layer.open({
                            type: 1,
                            shade: .5,
                            title: ['修改问题','font-size:20px;'],
                            area: '660px',
                            zIndex:'100',
                            btn: ['保存','取消'],
                            btnAlign: 'c',
                            content: $('.layer-faq'),
                            success: function(layero){
                                //重新设置保存按钮class属性
                                var btn = layero.find('.layui-layer-btn');
                                btn.find('.layui-layer-btn0').attr({class:'layui-layer-btn0 modifyProblemBtn'});
                                //绑定保存按钮事件
                                $('.modifyProblemBtn').on("click",modifyProblem);
                            }
                        });
                    }
                    else{
                        return;
                    }
                }
            });
        });

        //删除按钮事件
        $('.delFag').click(function(){
            $.ajax({
                url:"${request.contextPath}/system/problem/delProblem",
                type:"post",
                data:{
                    'id':$(this).data('i')
                },
                dataType: "json",
                success:function(result){
                    showMsgSuccess(result.msg,"提示",function(index){
                        layer.close(index);
                        if(result.success){
                            returnIndexPage();
                        }
                    });
                }
            });
        });
        //新增分类
        function addType(){
            var className = $.trim($('#classify-name').val());
            var serverCode = $('#serverCodeSelect option:selected').val();
            if(className==""){
                layer.alert("分类名不能为空");
                return;
            }
            if(jbkLength(className)>20){
                layer.alert("分类名称不超过20个字符");
                return;
            }
            $.ajax({
                url:"${request.contextPath}/system/problem/addProblemType",
                type:"post",
                data:{
                    'name':className,
                    'serverCode':serverCode
                },
                dataType: "json",
                success:function(result){
                    showMsgSuccess(result.msg,"提示",function(index){
                        layer.close(index);
                        if(result.success){
                            returnIndexPage();
                        }
                    });
                }
            });
        }

        //修改问题类型
        function modifyType(event){
            var className = $.trim($('#classify-name').val());
            var typeId = event.data.typeId;
            if(className==""){
                layer.alert("分类名不能为空");
                return;
            }
            if(jbkLength(className)>20){
                layer.alert("分类名称不超过20个字符");
                return;
            }
            $.ajax({
                url:"${request.contextPath}/system/problem/updateProblemType",
                type:"post",
                data:{
                    'name':className,
                    'id':typeId
                },
                dataType: "json",
                success:function(result){
                    showMsgSuccess(result.msg,"提示",function(index){
                        layer.close(index);
                        if(result.success){
                            returnIndexPage();
                        }
                    });
                }
            });
        }
        //新增问题
        function addProblem(event){
            var title = $.trim($("#faq-title").val());
            var contentTxt = event.data.ue.getContentTxt();
            if(title==""){
                layer.alert("问题标题不能为空");
                return;
            }
            if(title.length>30){
                layer.alert("问题标题不超过30个字");
                return;
            }
            if(contentTxt==""){
                layer.alert("问题答案不能为空");
                return;
            }
            if(contentTxt.length>2000){
                layer.alert("问题答案不超过2000个字");
                return;
            }
            $("#questionForm").ajaxSubmit({
                url:"${request.contextPath}/system/problem/addProblem",
                type:"post",
                dataType:"json",
                success:function(result){
                    showMsgSuccess(result.msg,"提示",function(index){
                        layer.close(index);
                        if(result.success){
                            returnIndexPage();
                        }
                    });
                }
            });
        }

        //修改问题
        function modifyProblem(){
            var title = $.trim($("#faq-title").val());
            var content = $.trim($("#faq-content").val());
            if(title==""){
                layer.alert("问题标题不能为空");
                return;
            }
            if(title.length>30){
                layer.alert("问题标题不超过30个字");
                return;
            }
            $("#questionForm").ajaxSubmit({
                url:"${request.contextPath}/system/problem/updateProblem",
                type:"post",
                dataType:"json",
                success:function(result){
                    showMsgSuccess(result.msg,"提示",function(index){
                        layer.close(index);
                        if(result.success){
                            returnIndexPage();
                        }
                    });
                }
            });
        }

        function returnIndexPage(){
            var url = "${request.contextPath}/system/problem/index";
            var serverCode = $('#serverCodeSelect option:selected').val();
            moduleContentLoad(url,{"serverCode":serverCode});
        }
    })
</script>