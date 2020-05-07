<link rel="stylesheet" type="text/css" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/awesome.css"/>

<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.core.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.excheck.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.exedit.js" type="text/javascript" charset="utf-8"></script>
<div class="layer-made layer-bi-style">
     <div class="layer-made-head">
         <span class="layer-head-name"></span><img src="${request.contextPath}/bigdata/v3/static/images/bi/close.png" class="js-ust-div-close" alt="">
     </div>
     <div class="layer-made-body clearfix">
         <div class="half-box mr-20">
             <div class="tabs-made">
                 <ul class="tabs tabs-card" tabs-group="bdw">
                     <li class="tab active"><a href="javascript:void(0)">本单位用户</a></li>
                 </ul>
                 <div class="tabs-panel scrollBar4 active" tabs-group="bdw" id="bdw-1">
                     <ul id="userTree" class="ztree"></ul>
                 </div>
             </div>
         </div>
         <div class="half-box">
             <div class="choosed choose-bi-num">已选 (<span>2</span>)</div>
             <div class="choosed-content scrollBar4">
                 <ul class="choose-item-bi"></ul>
             </div>
         </div>
     </div>
     <div class="layer-made-foot">
         <div class="btn-made unfilled-corner active js-ust-div-ok">确定</div>
         <div class="btn-made unfilled-corner js-close js-ust-div-close">取消</div>
     </div>
 </div>
<script>
    var num = 0;
    var zTreeSelectedUserIdMap = new Map();
    var userTree;
    //$('.tree-wrap').height($('.layer-power').height());
    //$('.choosed-content').height($('.tree-wrap').height() + 50);
    //$('.no-data').css('line-height', ($('.choosed-content').height() - 2) + 'px');

    //已选
    $('.choose-item-bi').on('click', '.selectd-node', function () {
        var selectedNodeId = $(this).parent().attr("id");
        if (userTree) {
            var selectedNode = userTree.getNodeByParam('id', selectedNodeId);
            if (selectedNode)
                userTree.checkNode(selectedNode, false, false, false);
        }
        zTreeSelectedUserIdMap.delete(selectedNodeId);
        $(this).parent().remove();
        num--;
        $('.choose-bi-num span').text(num);
        // if (num == 0) {
        //     $('.no-data').show()
        // }
    });

    function userTreeSearch() {
        var keywords = $('#tree_search_keywords').val();
        var matchKeywordsTreeNodeArray = userTree.getNodesByParamFuzzy('name', keywords);
        //匹配
        if (matchKeywordsTreeNodeArray.length > 0) {
            userTree.selectNode(matchKeywordsTreeNodeArray[0]);
        }
    }

    function loadUserTree() {
        var setting = {
            view: {
                selectedMulti: false
            },
            check: {
                enable: true,
                chkStyle: 'checkbox',
                radioType: "level"
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onCheck: onUserTreeClick
            }
        };
        $.ajax({
            url: "${request.contextPath}/bigdata/common/tree/userTreeByUnit?unitId=${unitId!}",
            beforeSend: function () {
                $('#userTree').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            },
            success: function (data) {
                var jsonO = JSON.parse(data);
                if (jsonO.success) {
                    userTree = $.fn.zTree.init($("#userTree"), setting, JSON.parse(jsonO.msg));
                    userTree.setting.check.chkboxType = {'Y': '', N: ''};
                    var parentNodes = userTree.getNodes();
                    var allNodes = userTree.transformToArray(parentNodes);
                    //遍历所有节点
                    allNodes.forEach(function (e) {
                        if (zTreeSelectedUserIdMap.has(e.id)) {
                            userTree.checkNode(e, true, true);
                        }
                    });
                }
            }
        });
    }

    function onUserTreeClick(event, treeId, treeNode, clickFlag) {
        if (treeNode.type == "dept") {
            return;
        }
        if (treeNode.checked) {
            if ($('#' + treeNode.id).length > 0) {
                return;
            }
            zTreeSelectedUserIdMap.set(treeNode.id, treeNode.name);
            var str = '<li id="' + treeNode.id + '"><span>' + treeNode.name + '</span><i class="wpfont icon-close js-delete selectd-node"></i></li>';
            $('.choose-item-bi').append(str);
            num++;
        } else {
            if ($('#' + treeNode.id).length == 0) {
                return;
            }
            $('#' + treeNode.id).remove();
            zTreeSelectedUserIdMap.delete(treeNode.id);
            num--;
        }
        $('.choose-bi-num span').text(num);
        // if (num == 0) {
        //     $('.no-data').show()
        // } else {
        //     $('.no-data').hide();
        // }
    }

    function loadOldUsers() {
        var jsondata = JSON.parse('${users!}');
        for (var i = 0; i < jsondata.length; i++) {
            var userId = jsondata[i].userId;
            var userName = jsondata[i].userName;
            zTreeSelectedUserIdMap.set(userId, userName);
            var str = '<li id="' + userId + '"><span>' + userName + '</span><i class="wpfont icon-close js-delete selectd-node"></i></li>';
            $('.choose-item-bi').append(str);
            num++;
        }
        $('.choose-bi-num span').text(num);
        // if (num > 0)
        //     $('.no-data').hide();
    }

    // 自定义弹窗设置
    function openUserSelectDiv(title,width,height,yesFunction,cancelFunction){
        var ele=$('.layer-user-select');

        if(!title)title='提示';
        if(!(typeof yesFunction === "function")){
            yesFunction = function (){$(ele).hide();}
        }
        if(!(typeof cancelFunction === "function")){
            cancelFunction = function (){ $(ele).hide();}
        }

        $(ele).show();
        $(ele).children().width(width).height(height);
        $(ele).find('.layer-head-name').text(title);
        $(ele).on('click','.js-ust-div-ok',yesFunction)
        $(ele).on('click','.js-ust-div-close',cancelFunction)
    }

    //是否级联
    $('#tree_cascade').on('click', function () {
        var cascade = $(this).is(':checked');
        if (cascade) {
            userTree.setting.check.chkboxType = {'Y': 's', N: 's'};
        } else {
            userTree.setting.check.chkboxType = {'Y': '', N: ''};
        }
    });

    $(function () {
        loadOldUsers();
        loadUserTree();
    });

</script>