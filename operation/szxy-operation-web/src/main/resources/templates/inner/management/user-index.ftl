<div class="filter">
    <div class="filter-item">
        <div class="filter-content">
            <a class="btn btn-blue" href="#/operation/management/user/add">新增</a>
        </div>
    </div>
</div>
<div id="userContainer">
</div>
<script>
    $(function () {
        $('#userContainer').loading('${springMacroRequestContext.contextPath}/operation/management/user/list');
        routeUtils.add('/operation/management/user/add', function () {
            $('#managementContainer').loading('${springMacroRequestContext.contextPath}/operation/management/user/add')
        })
    });
</script>