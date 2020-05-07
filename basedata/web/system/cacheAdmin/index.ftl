<#import "/fw/macro/webmacro.ftl" as w>
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="box box-default">
    <div class="box-body">

        <div class="clearfix">
            <div class="col-sm-3 col-md-3 col-xs-3">
                <!--  redis tree  -->
                <div class="tree-wrap" style="height:500px;">
                    <ul id="redisTree" class="ztree" ></ul>
                </div> <!--tree end-->
            </div>

            <div class="row col-sm-offset-3 col-md-offset-3">
                <div class="box box-default">
                    <div class="box-body" >
                        <div class="filter filter-f16" style="height: 60px;">
                            <div class="filter-item">
                                <div class="btn-group" style="float:left;margin-top:1px;">
                                    <button type="button" id="btn-dataType" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                        数据类型<span class="caret"></span>
                                    </button>
                                    <input type="hidden" id="dataType" />
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="javascript:selectDataType('NONE');">ALL</a></li>
                                        <li><a href="javascript:selectDataType('hash');">HASH</a></li>
                                        <li><a href="javascript:selectDataType('string');">STRING</a></li>
                                        <li><a href="javascript:selectDataType('set');">SET</a></li>
                                        <li><a href="javascript:selectDataType('zset');">ZSET</a></li>
                                        <li><a href="javascript:selectDataType('list');">LIST</a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="filter-item">
                                <div class="btn-group" style="float:left;margin-top:1px;">
                                    <button type="button" id="btn-queryType" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                        查询类型<span class="caret"></span>
                                    </button>
                                    <input type="hidden" id="queryType" />
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="javascript:selectQueryType('head');">HEAD</a></li>
                                        <li><a href="javascript:selectQueryType('middle');">MIDDLE</a></li>
                                        <li><a href="javascript:selectQueryType('end');">TAIL</a></li>
                                    </ul>
                                </div>
                                <span class="filter-name">
                                    <input id="query_input" type="text" placeholder="" class="typeahead scrollable form-control tt-input" style="margin-top: 1px; margin-right: 0px; position: relative; vertical-align: top; background-color: transparent;" >
                                </span>
                                <div class="filter-content">
                                    <button id="btn-query" class="btn btn-lightblue">Q</button>
                                </div>
                            </div>
                            <div class="filter-item">

                                <#--<button class="btn btn-green" id="btn-add">添加</button>-->
                                <button class="btn btn-orange " id="btn-refresh">刷新</button>
                            </div>
                            <div class="filter-item-right">
                                <button class="btn btn-orange " id="btn-addServer">Add-Server</button>
                            </div>
                        </div>
                        <div id="keyContainer">
                            <#include "keyList.ftl" />
                        </div>
                    </div>
                </div>


            </div>

        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        var redis_host = "";
        var redis_port = "";
        var dbIndex = "";
        var setting = {
            callback: {
                onDblClick : function(event, treeId, treeNode) {
                    var zTree = $.fn.zTree.getZTreeObj("redisTree");

                    if ( treeNode.isParent ) {
                        zTree.expandNode(treeNode);
                        return ;
                    }
                    var nodeName = treeNode.name;
                },

                onClick : function (event, treeId, treeNode) {
                    if ( !treeNode.isParent ) {
                        redis_host  = treeNode.host;
                        redis_port = treeNode.port;
                        dbIndex  = treeNode.dbIndex;
                        refreshKeyList(getParameterFromTree());
                    }
                }
            }
        };

        initServerTree(setting);

        $("#btn-refresh").bind("click",function () {
            $.ajax({
                type: "get",
                url: "${request.contextPath}/redis/refresh/keyCache",
                dataType: "json",
                data: {},
                success: function (data) {
                    showSuccessMsgWithCall(data.msg,function () {
                        refreshKeyList(getParameterFromTree());
                    });
                }
            });

            $.ajax({
                type: "get",
                url: "${request.contextPath}/redis/serverTree",
                dataType: "json",
                data: {
                    refresh:true,
                },
                success: function (data) {
                    if (data.success){
                        var treeJSON = JSON.parse(data.tree);
                        $.fn.zTree.destroy();
                        $.fn.zTree.init($("#redisTree"), setting, treeJSON);
                        layer.closeAll();
                    } else {
                        showErrorMsg("刷新失败，请查看后台日志");
                    }
                }
            });
        });

        $("#btn-query").bind("click",function () {
            if ( redis_host == "" || redis_port == "" || dbIndex == "" ) {
                showWarnMsg("请选择右侧Server和DB");
                return ;
            }
            refreshKeyList(getParameterFromTree()+"&queryType="+$("#queryType").val()+"&dataType="+$("#dataType").val()+"&queryKey="+$("#query_input").val());
        });

        $("#btn-add").bind("click",function () {
            var parameter = "";
            parameter += "?dbIndex=" + dbIndex;
            parameter += "&key="  ;
            parameter += "&dataType=" + "STRING";
            parameter += "&redis_host=${redis_host!}";
            parameter += "&redis_port=${redis_port!}";
            $("#common-Layer").load("${request.contextPath}/redis/add/page" + parameter,function () {
                layer.open({
                    type: 1,
                    shade: .5,
                    title: ['View/Updaate','font-size:16px'],
                    area: ['940px'],
                    content: $('#common-Layer'),
                    resize:true,
                    yes:function (index) {

                    },
                    btn2:function (index) {
                        layer.closeAll();
                    }
                });
            });
        });

        $("#btn-addServer").bind("click",function () {
            $("#common-Layer").load("${request.contextPath}/redis/add/server/page",function () {
                layer.open({
                    type: 1,
                    shade: .5,
                    title: ['Add-Server','font-size:16px'],
                    area: ['540px'],
                    content: $('#common-Layer'),
                    btn:['确定'],
                    resize:true,
                    yes:function (index) {
                        var host = $("#common-Layer #redis_host").val();
                        var port = $("#common-Layer #redis_port").val();
                        var pwd = $("#common-Layer #password").val();
                        if ( host == null || host == "" ) {
                            layerError("#common-Layer #redis_host","host 不能为空");
                            return ;
                        }
                        if ( port == null || port == "" ) {
                            layerError("#common-Layer #redis_port", "port 不能为空");
                            return ;
                        }
                        $.ajax({
                            type: "get",
                            url: "${request.contextPath}/redis/add/server",
                            dataType: "json",
                            data: {
                                host:host,
                                port: port,
                                pasword: pwd,
                            },
                            success: function (data) {
                                if (data.success){
                                    showSuccessMsgWithCall("添加成功",function () {
                                        initServerTree(setting,true);
                                        layer.closeAll();
                                    })
                                } else {
                                    showErrorMsg("Redis Server 添加失败，"+data.msg);
                                }
                            }
                        });
                    },
                    btn2:function (index) {
                        layer.closeAll();
                    }
                });
            });
        });

        function getParameterFromTree() {
            var parameter = "";
            parameter += "?redis_host=" + redis_host;
            parameter += "&redis_port=" + redis_port;
            parameter += "&dbIndex=" + dbIndex;
            return parameter;
        }
    });

    function initServerTree(setting,refresh) {
        var tree = $.fn.zTree.getZTreeObj("redisTree");
        if ( tree !=null) {
            tree.destroy();
        }
        $.ajax({
            type: "get",
            url: "${request.contextPath}/redis/serverTree",
            dataType: "json",
            data: {
                refresh:!refresh?false:refresh,
            },
            success: function (data) {
                if ( data.success ){
                    var treeJSON = JSON.parse(data.tree);
                    $.fn.zTree.init($("#redisTree"), setting, treeJSON);
                } else {
                    showErrorMsg("Redis Tree 初始化失败，请查看后台日志");
                }
            }
        });
    }


    function refreshKeyList(parameters) {
        $("#keyContainer").load("${request.contextPath}/redis/keyList"+parameters);
    }

    function selectQueryType(queryType) {
        $("#btn-queryType").html(queryType + "<span class=\"caret\"></span>")
        $("#queryType").val(queryType);
    }
    function selectDataType(dataType) {
        if (dataType == "NONE") {
            $("#dataType").val("");
            $("#btn-dataType").html("ALL<span class=\"caret\"></span>")
            return ;
        }
        $("#btn-dataType").html(dataType + "<span class=\"caret\"></span>")
        $("#dataType").val(dataType);
    }

</script>