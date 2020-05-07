<link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/js/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js"></script>
<div class="form-group" style="display: block;">
    <div class="col-sm-8 col-sm-offset-2 model-order">
        <div class="bs-callout bs-callout-danger">
            <ul class="nav nav-tabs">
                <li class="active tree_unit_tab">
                    <a href="#" data-toggle="tab">下级单位</a>
                </li>
            </ul>
            <div class="tab-content tree-wrap tree-tab width-1of1" style="overflow: hidden">
                <div class="row no-margin">
                    <div class="filter-item col-sm-7 no-margin-right">
                        <div class="input-group">
                            <input type="text" id="tree_search_keywords"  class="form-control" />
                            <a href="javascript:void(0);" onClick="unitTreeSearch();return false;" class="input-group-addon"><img src="${request.contextPath}/bigdata/v3/static/images/icon/search_gray_16x16.png"/></a>
                        </div>
                    </div>
                    <div class="col-sm-3 switch-button text-right">
                        <span id="cascade_span">是否级联:</span>
                    </div>
                    <div class="col-sm-2 switch-button">
                        <label class="switch switch-txt">
                            <input type="checkbox" id="tree_cascade" name="switch-field-1">
                            <span class="switch-name">显示</span>
                        </label>
                    </div>
                </div>
                <div class="tab-pane active">
                    <ul id="unitTree" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="form-group" style="display: block;">
    <div class="col-sm-8 col-sm-offset-2 model-order">
        <div class="bs-callout bs-callout-danger">
            <p class="choose-num">已选（<span>0</span>）</p>
            <div class="choose-item scrollBar4">
                <div class="no-data">
                    <img src="${request.contextPath}/bigdata/v3/static/images/big-data/no-choice.png"/>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var num=0;
    var zTreeSelectedUnitIdMap = new Map();
    var unitTree;
    $('.tree-wrap').height($('.layer-power').height());
    $('.choose-item').height($('.tree-wrap').height() + 10);
    $('.no-data').css('line-height',($('.choose-item').height()-2)+'px');

    //已选
    $('.choose-item').on('click','.fa-times-circle',function(){
        var selectedNodeId=$(this).parent().attr("id");
        if(unitTree){
            var selectedNode=unitTree.getNodeByParam('id',selectedNodeId);
            if(selectedNode)
                unitTree.checkNode(selectedNode,false,false,false);
        }
        zTreeSelectedUnitIdMap.delete(selectedNodeId);
        $(this).parent().remove();
        num--;
        $('.choose-num span').text(num);
        if(num==0){
            $('.no-data').show()
        }
    });

    function unitTreeSearch(){
        var keywords = $('#tree_search_keywords').val();
        var matchKeywordsTreeNodeArray = unitTree.getNodesByParamFuzzy('name', keywords);
        //匹配
        if (matchKeywordsTreeNodeArray.length > 0) {
            unitTree.selectNode(matchKeywordsTreeNodeArray[0]);
        }
    }

    function loadUnitTree(){
        var setting = {
            view: {
                selectedMulti: false
            },
            check:{
                enable:true,
                autoCheckTrigger: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onCheck:onUnitTreeClick
            }
        };
        $.ajax({
            url:"${request.contextPath}/bigdata/common/tree/unitTree",
            beforeSend: function(){
                $('#unitTree').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            },
            success:function(data){
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    unitTree=$.fn.zTree.init($("#unitTree"), setting, JSON.parse(jsonO.msg));
                    unitTree.setting.check.chkboxType = {'Y': '', N: ''};
                    var parentNodes = unitTree.getNodes();
                    var allNodes = unitTree.transformToArray(parentNodes);
                    //遍历所有节点
                    allNodes.forEach(function(e){
                        if(zTreeSelectedUnitIdMap.has(e.id)){
                            unitTree.checkNode(e, true, true);
                        }
                    });
                }
            }
        });
    }

    function onUnitTreeClick(event, treeId, treeNode, clickFlag){
        if(treeNode.checked){
            if($('#' + treeNode.id).length > 0) {
                return;
            }
            zTreeSelectedUnitIdMap.set(treeNode.id,treeNode.name);
            var str='<div id="'+treeNode.id+'" class="unit-tree-item">'+treeNode.name+'<i class="fa fa-times-circle"></i></div>';
            $('.choose-item').append(str);
            num++;
        }else{
            if($('#' + treeNode.id).length == 0) {
                return;
            }
            $('#' + treeNode.id).remove();
            zTreeSelectedUnitIdMap.delete(treeNode.id);
            num--;
        }
        $('.choose-num span').text(num);
        if(num==0){
            $('.no-data').show()
        }else{
            $('.no-data').hide();
        }
    }

    function loadOldUnits(){
        var jsondata =JSON.parse('${units!}');
        for (var i = 0; i < jsondata.length; i++){
            var unitId=jsondata[i].unitId;
            var unitName=jsondata[i].unitName;
            zTreeSelectedUnitIdMap.set(unitId,unitName);
            var str='<div id="'+unitId+'" class="unit-tree-item">'+unitName+'<i class="fa fa-times-circle"></i></div>';
            $('.choose-item').append(str);
            num++;
        }
        $('.choose-num span').text(num);
        if(num>0)
            $('.no-data').hide();
    }

    //是否级联
    $('#tree_cascade').on('click', function () {
        var cascade = $(this).is(':checked');
        if (cascade) {
            unitTree.setting.check.chkboxType = {'Y': 's', N: 's'};
        } else {
            unitTree.setting.check.chkboxType = {'Y': '', N: ''};
        }
    });

    $(document).ready(function(){
        loadOldUnits();
        loadUnitTree();
    });

</script>