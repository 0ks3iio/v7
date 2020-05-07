<#import "../macro/staticImport.ftl" as s />
<@s.datepicker/>
<div class="box box-default recordPage">
    <div class="box-header">
        <h3 class="box-caption">操作记录</h3>
    </div>
    <div class="box-body">
        <div class="filter">

            <div class="filter-item filter-item-right">

                <span class="filter-name">服务类型：</span>
                <div class="filter-content">
                    <select name="" id="type" onchange="doGetRecordAccounts('');" class="form-control" >
                        <option value="">全部</option>
                        <#list operateType as type>
                            <option value="${type.operateCode}">${type.operateName}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="filter-item filter-item-right" style="margin-left:1px;">
                <label class="filter-name">—</label>
                <div class="col-sm-3">
                    <div class="input-group">
                        <input id="end" onchange="doGetRecordAccounts('');" class="form-control datetimepicker" type="text">
                    </div>
                </div>
            </div>


            <div class="filter-item filter-item-right">
                <label class="filter-name">服务时间:</label>
                <div class="col-sm-3">
                    <div class="input-group">
                        <input id="start" onchange="doGetRecordAccounts('');" class="form-control datetimepicker" type="text">
                    </div>

                </div>
            </div>
        </div>
        <div id="recordAccounts">

        </div>
    </div>
</div>

<script>

    $(function () {
        doGetRecordAccounts('');
    });
    //pageURL从分页宏的回调函数获取
    function doGetRecordAccounts(pageURL) {
        let pURL = doBuildDynamicParameter();
        if ($.trim(pageURL) !== '') {
            pURL = pURL + '&' + pageURL;
        }
        $('#recordAccounts').load(_contextPath + '/operation/systemLog/account/'+pURL);
    }
    //动态拼接参数
    function doBuildDynamicParameter() {
        var ms = 1 * (1000 * 60 * 60 * 24)
        var date = new Date(new Date($('#end').val().trim()).getTime()+ms); //处理时间问题--例如:选26号截止的记录,只能看到25号的,访问服务器的时候,自动加一天
        var str = "" + date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "";
        let pURL ='?unitId='+recordId+ '&type=' + $('#type').val();
        if($('#start').val()!=''){pURL+='&start='+$('#start').val()}
        if($('#end').val()!=''){pURL+='&end='+str}
        return pURL;
    }

    $('.datetimepicker').datepicker({
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        format: 'yyyy-mm-dd'
    })
</script>