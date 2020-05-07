<div class="filter">
    <div class="filter-item">
        <div class="filter-content">
            <a class="btn btn-blue" href="#/operation/management/group/add">新增组</a>
        </div>
    </div>
</div>
<div id="groupContainer">
</div>

<script>
    $(function () {
        $('#groupContainer').loading(_contextPath + '/operation/management/group/list');
        routeUtils.add('/operation/management/group/add', function () {
           $('#managementContainer').loading(_contextPath + '/operation/management/group/add');
        });
    })
</script>