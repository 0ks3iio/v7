<div class="box box-default">
    <div class="box-body" id="managementBodyContainer">
        <ul class="nav nav-tabs nav-tabs-1">
            <li class="user-info"><a href="#/operation/setting/info" >个人信息</a></li>
            <li class="user-password"><a href="#/operation/setting/password">修改密码</a></li>
        </ul>
        <div class="tab-content">
            <div id="userSettingContainer" class="tab-pane active">

            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        routeUtils.go('/operation/setting/info', function () {
            $('#userSettingContainer').loading('${springMacroRequestContext.contextPath}/operation/setting/info');
            $('.user-info').addClass('active').siblings('li').removeClass('active');
        });
        routeUtils.add('/operation/setting/password', function () {
            $('#userSettingContainer').loading('${springMacroRequestContext.contextPath}/operation/setting/password');
            $('.user-password').addClass('active').siblings('li').removeClass('active');
        });
    });
</script>