<!-- 单位管理首页 -->
<div class="page-sidebar">
    <div class="page-sidebar-header ml10 mr10">
        <div class="input-group mt20">
            <input type="text" class="form-control">
            <a href="#" class="input-group-addon">
                <i class="fa fa-search"></i>
            </a>
        </div>
    </div>
    <#import "../macro/unitTree.ftl" as u />
    <@u.unitTree dataType='serviceTask' callback='showTaskTypeAndState'/>

</div>

<div class="page-content-inner">

</div>
<script>
    var recordId;
    //TODO 点击页面调用
    $(function () {
        recordId='';
        $('.page-content-inner').load(_contextPath + '/operation/opUserLog/page');
    });
    function showTaskTypeAndState(pId) {
        recordId=pId;
        // $('#type').val('');
        // $('#start').val('');
        // $('#end').val('');
        doGetRecordAccounts('');
    }
</script>
