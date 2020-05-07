<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div id="showDiv" class="box box-default">
    <div class="nav-tabs-wrap clearfix" >
        <ul class="nav nav-tabs nav-tabs-1" id="roleStuShow"  role="tablist">
            <li role="presentation" class="active" val="1"><a href="#aa" onclick="stuShowList('1')" role="tab" data-toggle="tab">报告单管理员设置</a></li>
            <li role="presentation"  val="2" ><a href="#bb" role="tab" onclick="stuShowList('2')" data-toggle="tab">成长手册管理员设置</a></li>
        </ul>
    </div>
    <div id="showPermissionList">

    </div>
</div>
<script type="text/javascript">
    $(function(){
        stuShowList('1');
    });
    function stuShowList(tabIndex){
        var url =  '${request.contextPath}/studevelop/permissionSet/list?permissionType='+tabIndex;
        $("#showPermissionList").load(url);
    }
</script>