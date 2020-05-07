<div class="filter-made mb-10">
    <div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="addNode();">新增节点</button>
    </div>
</div>
<#if nodes?exists && nodes?size gt 0>
    <div class="row">
            <#list nodes as node>
                <div class="col-md-3 col-sm-12">
                    <div class="node-box">
                        <div class="node-basic-header">
                            <div class="bold ellipsis" style="width: 150px;" title="${node.name!?html}">${node.name!?html}</div>
                            <div class="btn-group fn-left">
                                <i class="iconfont icon-ellipsis-fill fa-trash dropdown-toggle"></i>
                                <ul class="dropdown-menu">
                                    <li><a href="javascript:void(0)" hidefocus="true"
                                           onclick="editNode('${node.id!}')">编辑</a></li>
                                    <li><a href="javascript:void(0)" hidefocus="true"
                                           onclick="deleteNode('${node.id!}','${node.name!?html?js_string}');">删除</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="node-basic-body row">
                            <img src="${request.contextPath}/bigdata/v3/static/images/node/nodeimg.png">
                            <div class="node-basic-title">
                        <#if node.status! == 1>
                            <div>
                                <i class="node-run"></i>
                                <span>运行中</span>
                            </div>
                        <#else >
                            <div>
                                <i class="node-stop"></i>
                                <span>连接断开</span>
                            </div>
                        </#if>
                            </div>
                            <span class="node-three-num" onclick="goToNodeServer('${node.id!}')">${node.count!}</span>
                        </div>
                        <#if node.nodeServers?exists && node.nodeServers?size gt 0>
                        <div class="node-modal">
                            <#list node.nodeServers as nodeServer>
                            <div class="node-modal-li">
                                <div>${nodeServer.type!}</div>
                                <#if nodeServer.status! == 1>
                                    <div class="node-run1">运行中</div>
                                    <#else >
                                     <div class="node-stop1">断开</div>
                                </#if>
                            </div>
                            </#list>
                        </div>
                        </#if>
                    </div>
                </div>
            </#list>
    </div>
<#else>
    <div class="no-data-common">
        <div class="text-center">
            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
            <p class="color-999">
                暂无节点信息
            </p>
        </div>
    </div>
</#if>

<div class="layer layer-editParam layui-layer-wrap" style="display: none;">
    <div class="layer-content">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
                <div class="col-sm-8">
                    <input type="text" id="name" class="form-control" maxlength="25">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>域名：</label>
                <div class="col-sm-8">
                    <input type="text" id="domain" class="form-control" maxlength="25">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>端口：</label>
                <div class="col-sm-8">
                    <input type="text" id="port" class="form-control" maxlength="9" oninput = "value=value.replace(/[^\d]/g,'')">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>用户名：</label>
                <div class="col-sm-8">
                    <input type="text" id="username" class="form-control" maxlength="25">
                </div>
            </div>
            <div class="form-group" style="color:#ff0000">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>密码：</label>
                <div class="col-sm-8">
                    <input type="password" id="password" class="form-control" maxlength="25">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">备注：</label>
                <div class="col-sm-8">
                    <textarea id="remark" maxlength="250" class="form-control" style="height:100px"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>

<link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/page.css"/>

<script>

    $(function(){
        $(".node-basic-body .node-three-num").hover(function(){
            $(this).parents(".node-box").find(".node-modal").show();
        },function(){
            $(this).parents(".node-box").find(".node-modal").hide();
        })
    })

    $('.dropdown-toggle').not(':disabled').click(function(e){
        e.preventDefault();
        $('.dropdown-toggle').each(function(){
            var $g = $(this).parent('.btn-group');
            if ($g.hasClass('open')) {
                $g.removeClass('open');
            }
        });
        $(this).parent('.btn-group').toggleClass('open');
    });
    $('.dropdown-menu li:not(.disabled)').click(function(e){
        //e.preventDefault();
        var $btnGroup = $(this).parents('.btn-group');
        $(this).addClass('active').siblings('li').removeClass('active');
        $btnGroup.removeClass('open');
        if ($btnGroup.hasClass('filter')) {
            e.preventDefault();
            var txt = $(this).text();
            $btnGroup.children('.dropdown-toggle').text(txt);
        }
    });
    $('.row').click(function(event){
        var eo=$(event.target);
        if($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-toggle' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
            $('.btn-group').removeClass('open');
    });

    function addNode() {
        router.go({
            path: '/bigdata/node/add',
            name: '新增节点',
            level: 2
        }, function () {
            var url = '${request.contextPath}/bigdata/node/add';
            $('.page-content').load(url);
        });
    }

    function editNode(id) {
        router.go({
            path: '/bigdata/node/add?id='+id,
            name: '编辑节点',
            level: 2
        }, function () {
            var url = '${request.contextPath}/bigdata/node/add?id='+id;
            $('.page-content').load(url);
        });
    }

    function goToNodeServer(id) {
        router.go({
            path: '/bigdata/node/server/index?id='+id,
            name: '节点服务',
            level: 2
        }, function () {
            var url = '${request.contextPath}/bigdata/node/server/index?id='+id;
            $('.page-content').load(url);
        });
    }

    function deleteNode(id,name){
        showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
            $.ajax({
                url:"${request.contextPath}/bigdata/node/deleteNode",
                data:{
                    'id':id
                },
                type:"post",
                clearForm : false,
                resetForm : false,
                dataType: "json",
                success:function(result){
                    layer.closeAll();
                    if(!result.success){
                        showLayerTips4Confirm('error',result.message);
                    }else{
                        showLayerTips('success',result.message,'t');
                        $('.page-content').load('${request.contextPath}/bigdata/node/index');
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }
</script>