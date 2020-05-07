<table class="table no-margin">
    <tbody>
    <tr>
        <td>
            <span class="color-999">负责区块：</span>${regionName}
        </td>
    </tr>
    </tbody>
</table>
<div id="groupAuth" class="page-sidebar chosen-tree-outter" style="height: 317px;overflow: auto;">
    <div class="page-sidebar-body chosen-tree-inner">
        <ul class="chosen-tree chosen-tree-tier1">
        <#if modules?? && modules?size gt 0>
            <#list modules as module>
            <li class="sub-tree">
                    <div class="chosen-tree-item" data-index="1">
                        <span class="arrow"></span>
                        <span class="name">${module.moduleName}</span>
                    </div>
                    <ul class="chosen-tree chosen-tree-tier2">
                        <#if module.operates?? && module.operates?size gt 0>
                            <#list module.operates as operate>
                            <li>
                                <div class="chosen-tree-item" data-index="10">
                                    <span class="arrow"></span>
                                    <span class="name">${operate.operateName}</span>
                                </div>
                            </li>
                            </#list>
                        </#if>
                    </ul>
            </li>
            </#list>
        </#if>
        </ul>
    </div>
</div>
<script>
    $(function () {
        $('#groupAuth').szxyTree();
    });
</script>