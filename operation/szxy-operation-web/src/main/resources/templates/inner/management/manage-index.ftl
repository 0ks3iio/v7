<div class="box box-default">
    <div class="box-body" id="managementBodyContainer">
        <ul class="nav nav-tabs nav-tabs-1">
            <li class="management-user"><a href="#/operation/management/user" <#--data-toggle="tab"-->>运营用户</a></li>
            <li class="management-group"><a href="#/operation/management/group" <#--data-toggle="tab"-->>运营组</a></li>
        </ul>
        <div class="tab-content">
            <div id="managementContainer" class="tab-pane active">

            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        routeUtils.go('/operation/management/user', function () {
            $('#managementContainer').loading('${springMacroRequestContext.contextPath}/operation/management/user/index');
            $('.management-user').addClass('active').siblings('li').removeClass('active');
        });
        routeUtils.add('/operation/management/group', function () {
            $('#managementContainer').loading('${springMacroRequestContext.contextPath}/operation/management/group/index');
            $('.management-group').addClass('active').siblings('li').removeClass('active');
        });
    });
</script>