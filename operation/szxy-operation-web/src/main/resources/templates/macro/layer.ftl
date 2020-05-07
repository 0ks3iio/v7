<!-- 通用弹出层 -->

<#macro op_layer>
    <div id="op_error" class="layer layer-tip layui-layer-wrap" style="display: none;">
        <div class="layer-content">
            <div class="layer-body">
                <i class="layer-tip-icon layer-tip-icon-failed"></i>
                <h3 class="layer-title error-content">对不起，您没有该操作的权限</h3>
            </div>
        </div>
    </div>

    <div id="op_success" class="layer layer-tip layui-layer-wrap" style="display: none;">
        <div class="layer-content">
            <div class="layer-body">
                <i class="layer-tip-icon layer-tip-icon-success"></i>
                <h3 class="layer-title success-content">对不起，您没有该操作的权限</h3>
            </div>
        </div>
    </div>

    <div id="op_warn" class="layer layer-tip layui-layer-wrap" style="display: none;">
        <div class="layer-content">
            <div class="layer-body">
                <i class="layer-tip-icon layer-tip-icon-warning"></i>
                <h3 class="layer-title warn-content">对不起，您没有该操作的权限</h3>
            </div>
        </div>
    </div>
</#macro>

