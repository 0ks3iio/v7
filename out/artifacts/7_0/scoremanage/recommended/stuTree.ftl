<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">

<div class="widget-box box-default" id="tree-box" style="border-color: #5090C1;">
    <div class="widget-body">
        <div class="widget-main padding-8"
             style="height:800px;overflow:auto;">
            <ul id="tree" class="ztree"></ul>
        </div>
    </div>
</div>
<script>
    var setting = {
        view: {
            dblClickExpand: true,
            selectedMulti: false,
            showLine: true
        },
        check: {
            enable: false
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId"
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                ${callback}(treeId, treeNode);
            }

        }
    };

    var zNodes = [];
    $(document).ready(function () {
        var url = '${request.contextPath}/scoremanage/recommended/stuTreeJson?gradeId=${gradeId}';
        $.ajax({
            url: url,
            async: false,
            success: function (data) {
                data = JSON.parse(data);
                if (data['code'] == '00') {
                    zNodes = data['nodeList'];
                } else {
                    $('#tree-box').removeClass('box-default')
                }
            }
        })

        var t = $("#tree");
        t = $.fn.zTree.init(t, setting, zNodes);
    });

</script>

